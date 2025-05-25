#!/usr/bin/env bash
#
# wait-for-it.sh
# ----------------
# This script waits for a specific TCP host and port to become available.
# It is typically used to delay the start of dependent services (e.g., wait for a database to be ready).
#
# USAGE:
#   ./wait-for-it.sh host:port [-s] [-t timeout] [-q] [-- command args]
#
# OPTIONS:
#   -h HOST | --host=HOST           Host or IP to test
#   -p PORT | --port=PORT           TCP port to test
#   -t TIMEOUT | --timeout=TIMEOUT Timeout in seconds (0 = no timeout; default = 15)
#   -s | --strict                   Only execute the following command if the host:port becomes available
#   -q | --quiet                    Suppress output messages
#   -- COMMAND ARGS                 Command to execute after the host:port becomes available
#

WAITFORIT_cmdname=${0##*/}

echoerr() {
  if [[ $WAITFORIT_QUIET -ne 1 ]]; then echo "$@" 1>&2; fi
}

usage() {
  cat << USAGE >&2
Usage:
  $WAITFORIT_cmdname host:port [-s] [-t timeout] [-- command args]
  -h HOST | --host=HOST       Host or IP under test
  -p PORT | --port=PORT       TCP port under test
                              Alternatively, use host:port as positional argument
  -t TIMEOUT | --timeout=TIMEOUT
                              Timeout in seconds, zero for no timeout
  -s | --strict               Only execute subcommand if the test succeeds
  -q | --quiet                Don't output any status messages
  -- COMMAND ARGS             Execute command with args after the test finishes
USAGE
  exit 1
}

wait_for() {
  if [[ $WAITFORIT_TIMEOUT -gt 0 ]]; then
    echoerr "$WAITFORIT_cmdname: waiting $WAITFORIT_TIMEOUT seconds for $WAITFORIT_HOST:$WAITFORIT_PORT"
  else
    echoerr "$WAITFORIT_cmdname: waiting for $WAITFORIT_HOST:$WAITFORIT_PORT without a timeout"
  fi
  WAITFORIT_start_ts=$(date +%s)
  while :
  do
    if [[ $WAITFORIT_ISBUSY -eq 1 ]]; then
      nc -z $WAITFORIT_HOST $WAITFORIT_PORT
      WAITFORIT_result=$?
    else
      (echo > /dev/tcp/$WAITFORIT_HOST/$WAITFORIT_PORT) >/dev/null 2>&1
      WAITFORIT_result=$?
    fi
    if [[ $WAITFORIT_result -eq 0 ]]; then
      WAITFORIT_end_ts=$(date +%s)
      echoerr "$WAITFORIT_cmdname: $WAITFORIT_HOST:$WAITFORIT_PORT is available after $((WAITFORIT_end_ts - WAITFORIT_start_ts)) seconds"
      break
    fi
    sleep 1
  done
  return $WAITFORIT_result
}

wait_for_wrapper() {
  # This allows SIGINT to be passed through to the timeout process
  if [[ $WAITFORIT_QUIET -eq 1 ]]; then
    timeout $WAITFORIT_BUSYTIMEFLAG $WAITFORIT_TIMEOUT "$0" --quiet --child --host=$WAITFORIT_HOST --port=$WAITFORIT_PORT --timeout=$WAITFORIT_TIMEOUT &
  else
    timeout $WAITFORIT_BUSYTIMEFLAG $WAITFORIT_TIMEOUT "$0" --child --host=$WAITFORIT_HOST --port=$WAITFORIT_PORT --timeout=$WAITFORIT_TIMEOUT &
  fi
  WAITFORIT_PID=$!
  trap "kill -INT -$WAITFORIT_PID" INT
  wait $WAITFORIT_PID
  WAITFORIT_RESULT=$?
  if [[ $WAITFORIT_RESULT -ne 0 ]]; then
    echoerr "$WAITFORIT_cmdname: timeout occurred after waiting $WAITFORIT_TIMEOUT seconds for $WAITFORIT_HOST:$WAITFORIT_PORT"
  fi
  return $WAITFORIT_RESULT
}

WAITFORIT_HOST=""
WAITFORIT_PORT=""
WAITFORIT_TIMEOUT=15
WAITFORIT_STRICT=0
WAITFORIT_QUIET=0

# Check if 'timeout' is the BusyBox version
WAITFORIT_BUSYTIMEFLAG=""
if timeout --help 2>&1 | grep -q BusyBox; then
  WAITFORIT_ISBUSY=1
  WAITFORIT_BUSYTIMEFLAG="-t"
else
  WAITFORIT_ISBUSY=0
fi

# Argument parsing
for arg in "$@"
do
  case "$arg" in
    *:* )
    WAITFORIT_HOST=$(echo $arg | cut -d : -f 1)
    WAITFORIT_PORT=$(echo $arg | cut -d : -f 2)
    shift
    ;;
    --child)
    WAITFORIT_CHILD=1
    shift
    ;;
    -q | --quiet)
    WAITFORIT_QUIET=1
    shift
    ;;
    -s | --strict)
    WAITFORIT_STRICT=1
    shift
    ;;
    -h)
    WAITFORIT_HOST="$2"
    if [[ $WAITFORIT_HOST == "" ]]; then break; fi
    shift 2
    ;;
    --host=*)
    WAITFORIT_HOST="${arg#*=}"
    shift
    ;;
    -p)
    WAITFORIT_PORT="$2"
    if [[ $WAITFORIT_PORT == "" ]]; then break; fi
    shift 2
    ;;
    --port=*)
    WAITFORIT_PORT="${arg#*=}"
    shift
    ;;
    -t)
    WAITFORIT_TIMEOUT="$2"
    if [[ $WAITFORIT_TIMEOUT == "" ]]; then break; fi
    shift 2
    ;;
    --timeout=*)
    WAITFORIT_TIMEOUT="${arg#*=}"
    shift
    ;;
    --)
    shift
    WAITFORIT_CLI=("$@")
    break
    ;;
    --help)
    usage
    ;;
    *)
    echoerr "Unknown argument: $arg"
    usage
    ;;
  esac
done

if [[ "$WAITFORIT_HOST" == "" || "$WAITFORIT_PORT" == "" ]]; then
  echoerr "Error: you need to provide a host and port to test."
  usage
fi

# Child execution path
if [[ "${WAITFORIT_CHILD}" -gt 0 ]]; then
  wait_for
  WAITFORIT_RESULT=$?
  exit $WAITFORIT_RESULT
else
  # Decide between direct wait or wrapper with timeout
  if [[ "$WAITFORIT_TIMEOUT" -gt 0 ]]; then
    wait_for_wrapper
    WAITFORIT_RESULT=$?
  else
    wait_for
    WAITFORIT_RESULT=$?
  fi
fi

# Run the target command if available
if [[ "$WAITFORIT_CLI" != "" ]]; then
  if [[ $WAITFORIT_RESULT -ne 0 && $WAITFORIT_STRICT -eq 1 ]]; then
    echoerr "$WAITFORIT_cmdname: strict mode, refusing to execute subprocess"
    exit $WAITFORIT_RESULT
  fi
  exec "${WAITFORIT_CLI[@]}"
else
  exit $WAITFORIT_RESULT
fi