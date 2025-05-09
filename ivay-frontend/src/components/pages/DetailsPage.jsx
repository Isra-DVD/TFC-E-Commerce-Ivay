import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import {
  Box,
  Container,
  Typography,
  TextField,
  Button,
  CircularProgress,
  Alert,
} from "@mui/material";
import AuthService from "../../service/auth.service";
import UserService from "../../service/user.service";
import { useAuth } from "../../context/AuthContext";

export default function DetailsPage() {
  const { logout, user, setUser } = useAuth();
  const navigate = useNavigate();

  const [loading, setLoading] = useState(true);
  const [readOnly, setReadOnly] = useState(true);
  const [form, setForm] = useState({
    name: "",
    email: "",
    phone: "",
    address: "",
  });
  const [error, setError] = useState("");
  const [success, setSuccess] = useState("");

  useEffect(() => {
    async function fetchProfile() {
      try {
        if (user) {
          setForm({
            name: user.name || "",
            email: user.email || "",
            phone: user.phone || "",
            address: user.address || "",
          });
        } else {
          const data = await UserService.getMyProfile();
          setForm({
            name: data.name,
            email: data.email,
            phone: data.phone,
            address: data.address || "",
          });
        }
      } catch (e) {
        setError("No se pudo cargar el perfil.");
        if (e.response && e.response.status === 401) {
          logout();
          navigate("/login", { replace: true });
        }
      } finally {
        setLoading(false);
      }
    }
    fetchProfile();
  }, [user, setUser, logout, navigate]);

  const handleChange = (e) => {
    setForm((prev) => ({ ...prev, [e.target.name]: e.target.value }));
  };

  const handleSaveDetails = async () => {
    setError("");
    setSuccess("");
    try {
      const updatedUser = await UserService.updateMyProfile(form);
      setSuccess("Datos actualizados con éxito.");
      setReadOnly(true);
    } catch (e) {
      setError("Error actualizando datos.");
    }
  };

  const handleChangePassword = async () => {
    setError("");
    setSuccess("");
    const current = prompt("Contraseña actual:");
    const next = prompt("Nueva contraseña:");
    if (!current || !next) return;
    try {
      await UserService.changePassword({
        currentPassword: current,
        newPassword: next,
      });
      setSuccess("Contraseña cambiada con éxito.");
    } catch (e) {
      setError(e.response?.data?.message || "Error cambiando la contraseña.");
    }
  };

  const handleLogout = () => {
    logout();
    navigate("/login", { replace: true });
  };

  if (loading) {
    return (
      <Box textAlign="center" mt={4}>
        <CircularProgress />
      </Box>
    );
  }

  return (
    <Container maxWidth="sm" sx={{ mt: 4 }}>
      <Typography variant="h4" gutterBottom>
        Mi Perfil
      </Typography>
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
        fullWidth
        label="Nombre"
        name="name"
        value={form.name}
        onChange={handleChange}
        margin="normal"
        InputProps={{ readOnly }}
      />
      <TextField
        fullWidth
        label="Email"
        name="email"
        value={form.email}
        onChange={handleChange}
        margin="normal"
        InputProps={{ readOnly }}
      />
      <TextField
        fullWidth
        label="Teléfono"
        name="phone"
        value={form.phone}
        onChange={handleChange}
        margin="normal"
        InputProps={{ readOnly }}
      />
      <TextField
        fullWidth
        label="Dirección"
        name="address"
        value={form.address}
        onChange={handleChange}
        margin="normal"
        InputProps={{ readOnly }}
      />

      <Box sx={{ display: "flex", gap: 2, mt: 3, flexWrap: "wrap" }}>
        {readOnly ? (
          <Button variant="contained" onClick={() => setReadOnly(false)}>
            Editar
          </Button>
        ) : (
          <Button variant="contained" onClick={handleSaveDetails}>
            Guardar
          </Button>
        )}
        <Button
          variant="outlined"
          color="secondary"
          onClick={handleChangePassword}
        >
          Cambiar contraseña
        </Button>
        <Button variant="text" color="error" onClick={handleLogout}>
          Cerrar sesión
        </Button>
      </Box>
    </Container>
  );
}
