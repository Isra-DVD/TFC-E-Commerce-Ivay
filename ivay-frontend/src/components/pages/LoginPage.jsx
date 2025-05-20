import React, { useState, useEffect } from "react";
import { useNavigate, Link as RouterLink, useLocation } from "react-router-dom";
import {
  Box,
  TextField,
  Button,
  Typography,
  Link as MuiLink,
  CircularProgress,
  Alert,
} from "@mui/material";
import LockOutlinedIcon from "@mui/icons-material/LockOutlined";
import AuthLayout from "../layout/AuthLayout";
import { useAuth } from "../../context/AuthContext";

function LoginPage() {
  const navigate = useNavigate();
  const location = useLocation();
  const {
    login,
    isAuthenticated,
    user,
    isLoading: authIsLoading,
    logout,
  } = useAuth();

  const [formData, setFormData] = useState({
    username: "",
    password: "",
  });
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(location.state?.message || "");

  useEffect(() => {
    if (location.state?.message && error === location.state.message) {
      navigate(location.pathname, { replace: true, state: {} });
    }
  }, [location, error, navigate]);

  useEffect(() => {
    if (!authIsLoading && isAuthenticated && user) {
      const VALID_CLIENT_ROLES = [2, 4];

      if (VALID_CLIENT_ROLES.includes(user.roleId)) {
        const from = location.state?.from?.pathname || "/";
        navigate(from, { replace: true });
      } else {
        setError(
          "Acceso no autorizado. No tienes el rol permitido para ingresar aquí."
        );
        logout();
      }
    }
  }, [isAuthenticated, user, authIsLoading, navigate, location.state, logout]);

  const handleChange = (e) => {
    setFormData((prev) => ({ ...prev, [e.target.name]: e.target.value }));
    if (error) setError("");
  };

  const handleSubmitWithFinally = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError("");
    try {
      await login({
        username: formData.username,
        password: formData.password,
      });
    } catch (err) {
      setError(
        err.response?.data?.message ||
          err.message ||
          "Error al iniciar sesión. Verifica tus credenciales."
      );
    } finally {
      setLoading(false);
    }
  };

  if (authIsLoading && !loading) {
    return (
      <Box
        sx={{
          display: "flex",
          justifyContent: "center",
          alignItems: "center",
          height: "100vh",
        }}
      >
        <CircularProgress />
      </Box>
    );
  }

  return (
    <AuthLayout pageTitle="Iniciar Sesión" formIcon={<LockOutlinedIcon />}>
      <Box
        component="form"
        onSubmit={handleSubmitWithFinally}
        noValidate
        sx={{ mt: 1, width: "100%", maxWidth: 400 }}
      >
        {error && (
          <Alert severity="error" sx={{ mb: 2, width: "100%" }}>
            {error}
          </Alert>
        )}
        <TextField
          margin="normal"
          required
          fullWidth
          id="username"
          label="Usuario / Correo"
          name="username"
          autoComplete="username"
          autoFocus
          value={formData.username}
          onChange={handleChange}
          disabled={loading || authIsLoading}
        />
        <TextField
          margin="normal"
          required
          fullWidth
          name="password"
          label="Contraseña"
          type="password"
          id="password"
          autoComplete="current-password"
          value={formData.password}
          onChange={handleChange}
          disabled={loading || authIsLoading}
        />
        <Button
          type="submit"
          fullWidth
          variant="contained"
          sx={{ mt: 3, mb: 2 }}
          disabled={loading || authIsLoading}
        >
          {loading || authIsLoading ? (
            <CircularProgress size={24} color="inherit" />
          ) : (
            "Iniciar Sesión"
          )}
        </Button>
        <Box sx={{ textAlign: "center" }}>
          <MuiLink component={RouterLink} to="/register" variant="body2">
            ¿No tienes cuenta? Regístrate
          </MuiLink>
        </Box>
      </Box>
    </AuthLayout>
  );
}

export default LoginPage;
