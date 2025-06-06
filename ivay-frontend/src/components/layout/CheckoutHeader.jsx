import React from "react";
import { Link as RouterLink, useLocation } from "react-router-dom";
import {
  AppBar,
  Toolbar,
  Box,
  Container,
  Stepper,
  Step,
  StepLabel,
  useTheme,
  styled,
  StepConnector,
  stepConnectorClasses,
  alpha,
} from "@mui/material";
import Check from "@mui/icons-material/Check";

import ivayLogoPurple from "../../assets/images/ivay-logo.png";

const steps = ["Mi cesta", "Dirección de envío", "Método de pago", "Resumen"];

const CustomConnector = styled(StepConnector)(({ theme }) => ({
  [`&.${stepConnectorClasses.alternativeLabel}`]: {
    top: 12,
    left: "calc(-50% + 16px)",
    right: "calc(50% + 16px)",
  },
  [`&.${stepConnectorClasses.active}`]: {
    [`& .${stepConnectorClasses.line}`]: {
      borderColor: theme.palette.primary.main,
    },
  },
  [`&.${stepConnectorClasses.completed}`]: {
    [`& .${stepConnectorClasses.line}`]: {
      borderColor: theme.palette.primary.main,
    },
  },
  [`& .${stepConnectorClasses.line}`]: {
    borderColor:
      theme.palette.mode === "dark"
        ? theme.palette.grey[800]
        : theme.palette.grey[300],
    borderTopWidth: 2,
    borderRadius: 1,
  },
}));

const CustomStepIconRoot = styled("div")(({ theme, ownerState }) => ({
  backgroundColor:
    theme.palette.mode === "dark"
      ? theme.palette.grey[700]
      : theme.palette.grey[300],
  zIndex: 1,
  color: theme.palette.common.white,
  width: 28,
  height: 28,
  display: "flex",
  borderRadius: "50%",
  justifyContent: "center",
  alignItems: "center",
  fontSize: "0.875rem",
  fontWeight: "bold",
  ...(ownerState.active && {
    backgroundColor: theme.palette.primary.main,
    boxShadow: `0 0 0 3px ${alpha(theme.palette.primary.main, 0.3)}`,
  }),
  ...(ownerState.completed && {
    backgroundColor: theme.palette.primary.main,
  }),
}));

/**
 * Custom component to render step icons in the stepper, showing a checkmark for completed steps.
 */
function CustomStepIcon(props) {
  const { active, completed, className, icon } = props;
  return (
    <CustomStepIconRoot
      ownerState={{ completed, active }}
      className={className}
    >
      {completed ? (
        <Check sx={{ fontSize: "1rem", color: "white" }} />
      ) : (
        String(icon)
      )}
    </CustomStepIconRoot>
  );
}

/**
 * Renders the header component for the checkout process,
 * displaying the site logo and a step-by-step progress indicator.
 */
function CheckoutHeader() {
  const theme = useTheme();
  const location = useLocation();
  let activeStep = 0;
  switch (location.pathname) {
    case "/checkout/address":
      activeStep = 1;
      break;
    case "/checkout/payment":
      activeStep = 2;
      break;
    case "/checkout/summary":
      activeStep = 3;
      break;
    default:
      activeStep = 0;
  }
  return (
    <AppBar
      position="sticky"
      color="inherit"
      elevation={0}
      sx={{
        backgroundColor: theme.palette.background.paper,
        borderBottom: `1px solid ${theme.palette.divider}`,
      }}
    >
      <Container maxWidth="lg">
        <Toolbar disableGutters sx={{ minHeight: { xs: 70, md: 80 }, py: 1 }}>
          <Box
            sx={{
              display: "flex",
              alignItems: "center",
              flexShrink: 0,
              mr: { xs: 2, md: 4 },
            }}
          >
            <RouterLink to="/">
              <img
                src={ivayLogoPurple}
                alt="IVAY Logo"
                style={{ height: 75, width: "auto", display: "block" }}
              />
            </RouterLink>
          </Box>
          <Box
            sx={{
              flexGrow: 1,
              display: "flex",
              justifyContent: "center",
              alignItems: "center",
            }}
          >
            <Stepper
              activeStep={activeStep}
              alternativeLabel
              connector={<CustomConnector />}
              sx={{ width: "100%", maxWidth: "700px" }}
            >
              {steps.map((label, index) => (
                <Step key={label}>
                  <StepLabel
                    slots={CustomStepIcon}
                    sx={{
                      "& .MuiStepLabel-label": {
                        color:
                          activeStep >= index
                            ? theme.palette.text.primary
                            : theme.palette.text.secondary,
                        fontWeight: activeStep === index ? "bold" : "normal",
                        mt: 0.5,
                        fontSize: { xs: "0.75rem", sm: "0.875rem" },
                      },
                    }}
                  >
                    {label}
                  </StepLabel>
                </Step>
              ))}
            </Stepper>
          </Box>
        </Toolbar>
      </Container>
    </AppBar>
  );
}

export default CheckoutHeader;