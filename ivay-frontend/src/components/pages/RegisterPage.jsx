import React, { useState } from "react";
import { useNavigate, Link as RouterLink } from "react-router-dom";
import {
  Box,
  TextField,
  Button,
  Link as MuiLink,
  CircularProgress,
  Alert,
} from "@mui/material";
import PersonAddOutlinedIcon from "@mui/icons-material/PersonAddOutlined";
import AuthLayout from "../layout/AuthLayout";
import UserService from "../../service/user.service.js";
import { useAuth } from "../../context/AuthContext";

function RegisterPage() {
  const navigate = useNavigate();
  const { login } = useAuth();
  const [formData, setFormData] = useState({
    name: "",
    fullName: "",
    email: "",
    phone: "",
    password: "",
    confirmPassword: "",
    roleId: 4,
    isEnabled: true,
    accountNoExpired: true,
    accountNoLocked: true,
    credentialNoExpired: true,
  });
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");
  const [success, setSuccess] = useState("");

  const handleChange = (e) => {
    setFormData((prev) => ({ ...prev, [e.target.name]: e.target.value }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError("");
    setSuccess("");

    if (formData.password !== formData.confirmPassword) {
      setError("Las contraseñas no coinciden.");
      return;
    }

    setLoading(true);
    try {
      const dto = {
        name: formData.name,
        fullName: formData.fullName,
        email: formData.email,
        password: formData.password,
        phone: formData.phone,
        roleId: formData.roleId,
        isEnabled: formData.isEnabled,
        accountNoExpired: formData.accountNoExpired,
        accountNoLocked: formData.accountNoLocked,
        credentialNoExpired: formData.credentialNoExpired,
      };

      await UserService.createUser(dto);
      await login({ username: formData.name, password: formData.password });
      setSuccess("¡Cuenta creada con éxito! Iniciando sesión…");
      navigate("/me", { replace: true });
    } catch (err) {
      setError(
        err.response?.data?.message ||
          err.message ||
          "Error al registrar la cuenta."
      );
    } finally {
      setLoading(false);
    }
  };

  return (
    <AuthLayout pageTitle="Crear cuenta" formIcon={<PersonAddOutlinedIcon />}>
      <Box
        component="form"
        onSubmit={handleSubmit}
        noValidate
        sx={{ mt: 1, maxWidth: 400, width: "100%" }}
      >
        {error && (
          <Alert severity="error" sx={{ mb: 2 }}>
            {error}
          </Alert>
        )}
        {success && (
          <Alert severity="success" sx={{ mb: 2 }}>
            {success}
          </Alert>
        )}

        <TextField
          margin="normal"
          required
          fullWidth
          id="name"
          label="Nombre de cuenta"
          name="name"
          autoComplete="name"
          autoFocus
          value={formData.name}
          onChange={handleChange}
          disabled={loading}
        />
        <TextField
          margin="normal"
          required
          fullWidth
          id="fullName"
          label="Nombre completo"
          name="fullName"
          autoComplete="fullName"
          value={formData.fullName}
          onChange={handleChange}
          disabled={loading}
        />
        <TextField
          margin="normal"
          required
          fullWidth
          id="email"
          label="Correo Electrónico"
          name="email"
          autoComplete="email"
          value={formData.email}
          onChange={handleChange}
          disabled={loading}
        />
        <TextField
          margin="normal"
          required
          fullWidth
          id="phone"
          label="Teléfono"
          name="phone"
          autoComplete="tel"
          value={formData.phone}
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
          autoComplete="new-password"
          value={formData.password}
          onChange={handleChange}
          disabled={loading}
        />
        <TextField
          margin="normal"
          required
          fullWidth
          name="confirmPassword"
          label="Confirmar contraseña"
          type="password"
          id="confirmPassword"
          autoComplete="new-password"
          value={formData.confirmPassword}
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
            "Crear cuenta"
          )}
        </Button>

        <Box sx={{ textAlign: "center" }}>
          <MuiLink component={RouterLink} to="/login" variant="body2">
            ¿Ya tienes cuenta? Inicia sesión
          </MuiLink>
        </Box>
      </Box>
    </AuthLayout>
  );
}

export default RegisterPage;
