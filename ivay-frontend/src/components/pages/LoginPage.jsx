import React, { useState } from "react";
import { useNavigate, Link as RouterLink } from "react-router-dom";
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
import AuthService from "../../service/auth.service";

function LoginPage() {
  const navigate = useNavigate();
  const [formData, setFormData] = useState({
    username: "",
    password: "",
  });
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");

  const handleChange = (e) => {
    setFormData((prev) => ({ ...prev, [e.target.name]: e.target.value }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError("");
    try {
      // Llamada al servicio con { username, password }
      const authResponse = await AuthService.login({
        username: formData.username,
        password: formData.password,
      });
      // authResponse → { accessToken, tokenType }
      const token = authResponse.tokenType + authResponse.accessToken;
      // Guardar token
      localStorage.setItem("authToken", token);
      // Configurar encabezado por defecto de axios
      AuthService.setAuthHeader(token);
      // Redirigir al dashboard o página principal
      navigate("/");
    } catch (err) {
      setError(
        err.message || "Error al iniciar sesión. Verifica tus credenciales."
      );
    } finally {
      setLoading(false);
    }
  };

  return (
    <AuthLayout pageTitle="Iniciar Sesión" formIcon={<LockOutlinedIcon />}>
      <Box
        component="form"
        onSubmit={handleSubmit}
        noValidate
        sx={{ mt: 1, width: "100%", maxWidth: 400 }}
      >
        {error && (
          <Alert severity="error" sx={{ mb: 2 }}>
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
          disabled={loading}
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
          disabled={loading}
        />
        <Button
          type="submit"
          fullWidth
          variant="contained"
          sx={{ mt: 3, mb: 2 }}
          disabled={loading}
        >
          {loading ? (
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
