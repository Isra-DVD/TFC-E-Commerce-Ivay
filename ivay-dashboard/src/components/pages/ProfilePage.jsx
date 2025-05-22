import React, { useState, useEffect } from "react";
import {
  Typography,
  Paper,
  TextField,
  Button,
  CircularProgress,
  Alert,
  Box,
} from "@mui/material";
import { useAuth } from "../../context/AuthContext";
import UserService from "../../service/user.service";

/**
 * ProfilePage component displays and allows editing of the authenticated 
 * user's profile information.
 * It fetches user data, handles updates to personal details, and provides a 
 * logout option.
 * Password change functionality is present in the state but not implemented in 
 * the UI provided.
 */
const ProfilePage = () => {
  const { user, setUser, logout } = useAuth();
  const [form, setForm] = useState({
    name: "",
    fullName: "",
    email: "",
    phone: "",
  });
  const [passwordForm, setPasswordForm] = useState({
    currentPassword: "",
    newPassword: "",
    confirmPassword: "",
  });
  const [loading, setLoading] = useState(true);
  const [savingDetails, setSavingDetails] = useState(false);
  const [savingPassword, setSavingPassword] = useState(false);
  const [error, setError] = useState("");
  const [success, setSuccess] = useState("");

  /* Effect hook that populates the form state with user data when the component mounts or the user object changes. */
  useEffect(() => {
    if (user) {
      setForm({
        name: user.name || "",
        fullName: user.fullName || "",
        email: user.email || "",
        phone: user.phone || "",
        userAddress: user.userAddress || "",
      });
      setLoading(false);
    } else {
      setError(
        "No se pudo cargar la información del perfil o no estás autenticado."
      );
    }
  }, [user]);

  /* Effect hook that clears error and success messages whenever the form or password form data changes. */
  useEffect(() => {
    setError("");
    setSuccess("");
  }, [form, passwordForm]);

  /**
   * Handles changes in the personal details input fields and updates the form state.
   * @param {object} e - The input change event object.
   */
  const handleChange = (e) =>
    setForm((prev) => ({ ...prev, [e.target.name]: e.target.value }));

  /**
   * Handles the action to save the personal details form.
   * Sets saving state, calls the user service to update the profile, updates 
   * the user in context on success,
   * and handles errors.
   */
  const handleSaveDetails = async () => {
    setError("");
    setSuccess("");
    setSavingDetails(true);
    try {
      const updatedUser = await UserService.updateMyProfile(form);
      setUser(updatedUser);
      setSuccess("Datos actualizados con éxito.");
    } catch (e) {
      setError(e.response?.data?.message || "Error actualizando datos.");
    } finally {
      setSavingDetails(false);
    }
  };

  /* Displays a loading spinner if user data is being fetched and the user object is not yet available. */
  if (loading && !user) {
    return (
      <Box
        sx={{
          display: "flex",
          justifyContent: "center",
          alignItems: "center",
          height: "80vh",
        }}
      >
        <CircularProgress />
      </Box>
    );
  }

  /* Displays an error message and logout button if user data could not be loaded or the user object is null after loading. */
  if (!user) {
    return (
      <Paper
        sx={{ p: 3, maxWidth: 600, mx: "auto", mt: 4, textAlign: "center" }}
      >
        <Typography variant="h6">
          No se pudo cargar la información del perfil.
        </Typography>
        <Typography>Por favor, intenta iniciar sesión de nuevo.</Typography>
        <Button variant="contained" onClick={logout} sx={{ mt: 2 }}>
          Ir a Inicio de Sesión
        </Button>
      </Paper>
    );
  }

  return (
    <>
      <Typography
        variant="h5"
        component="h1"
        gutterBottom
        fontWeight="bold"
        sx={{ textAlign: "center", mb: 3 }}
      >
        Mi Perfil (Gestor)
      </Typography>
      <Paper sx={{ p: { xs: 2, sm: 3 }, maxWidth: 600, mx: "auto" }}>
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

        <Box component="div" sx={{ mb: 4 }}>
          <Typography variant="h6" gutterBottom>
            Detalles Personales
          </Typography>
          <TextField
            fullWidth
            margin="normal"
            label="Nombre de Usuario"
            name="name"
            value={form.name}
            onChange={handleChange}
            slotProps={{ readOnly: true }}
          />
          <TextField
            fullWidth
            margin="normal"
            label="Nombre Completo"
            name="fullName"
            value={form.fullName}
            onChange={handleChange}
          />
          <TextField
            fullWidth
            margin="normal"
            label="Email"
            name="email"
            value={form.email}
            onChange={handleChange}
            slotProps={{ readOnly: true }}
          />
          <TextField
            fullWidth
            margin="normal"
            label="Teléfono"
            name="phone"
            value={form.phone}
            onChange={handleChange}
          />
          <Button
            variant="contained"
            onClick={handleSaveDetails}
            disabled={savingDetails}
            sx={{ mt: 2 }}
            fullWidth
          >
            {savingDetails ? (
              <CircularProgress size={24} color="inherit" />
            ) : (
              "Guardar Detalles"
            )}
          </Button>
        </Box>

        <Button
          variant="outlined"
          color="error"
          onClick={logout}
          sx={{ display: "block", mx: "auto", mt: 3 }}
          fullWidth
        >
          Cerrar Sesión
        </Button>
      </Paper>
    </>
  );
};

export default ProfilePage;
