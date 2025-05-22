import React, { useEffect, useState } from "react";
import {
  Container,
  Box,
  Grid,
  Paper,
  Typography,
  TextField,
  Button,
  CircularProgress,
  Alert,
} from "@mui/material";
import ManageAccountsOutlinedIcon from "@mui/icons-material/ManageAccountsOutlined";
import FavoriteBorderOutlinedIcon from "@mui/icons-material/FavoriteBorderOutlined";
import ExitToAppOutlinedIcon from "@mui/icons-material/ExitToAppOutlined";
import { useNavigate } from "react-router-dom";
import { Link as RouterLink } from "react-router-dom";
import { useAuth } from "../../context/AuthContext";
import UserService from "../../service/user.service";
import { colors } from "../../constants/styles";
import logo from "../../assets/images/ivay-logo.png";

/**
 * Renders the user details page, allowing authenticated users to view,
 * edit their profile information, and change their password.
 */
export default function DetailsPage() {
  const { logout } = useAuth();
  const navigate = useNavigate();

  const [loading, setLoading] = useState(true);
  const [readOnly, setReadOnly] = useState(true);
  const [form, setForm] = useState({
    name: "",
    fullName: "",
    email: "",
    phone: "",
    userAddress: "",
  });
  const [passwordForm, setPasswordForm] = useState({
    currentPassword: "",
    newPassword: "",
    confirmPassword: "",
  });
  const [error, setError] = useState("");
  const [success, setSuccess] = useState("");

  /**
   * Fetches the authenticated user's profile details from the backend.
   */
  const fetchProfile = async () => {
    setLoading(true);
    setError("");
    try {
      const data = await UserService.getMyProfile();
      setForm({
        name: data.name || "",
        fullName: data.fullName || "",
        email: data.email || "",
        phone: data.phone || "",
        userAddress: data.userAddress || "",
      });
    } catch (e) {
      setError("No se pudo cargar el perfil.");
      if (e.response?.status === 401) {
        // If unauthorized, log out the user
        logout();
        navigate("/login", { replace: true });
      }
    } finally {
      setLoading(false);
    }
  };

  /* Effect hook to fetch the user's profile data when the component mounts
   or when navigate/logout dependencies change. */
  useEffect(() => {
    fetchProfile();
  }, [navigate, logout]);

  /**
   * Handles changes in the main user details form input fields.
   */
  const handleChange = (e) =>
    setForm((prev) => ({ ...prev, [e.target.name]: e.target.value }));

  /**
   * Handles changes in the password change form input fields.
   */
  const handleChangePasswordForm = (e) =>
    setPasswordForm((prev) => ({ ...prev, [e.target.name]: e.target.value }));

  /**
   * Handles saving the updated user details.
   * Communicates with the backend service and updates the local state.
   */
  const handleSaveDetails = async () => {
    setError("");
    setSuccess("");
    try {
      await UserService.updateMyProfile(form);
      setSuccess("Datos actualizados con éxito.");
      setReadOnly(true);
      await fetchProfile(); // Re-fetch to ensure data is consistent
    } catch {
      setError("Error actualizando datos.");
    }
  };

  /**
   * Handles changing the user's password.
   * Validates the new password confirmation and communicates with the backend service.
   */
  const handleSavePassword = async () => {
    setError("");
    setSuccess("");
    const { currentPassword, newPassword, confirmPassword } = passwordForm;
    if (newPassword !== confirmPassword) {
      setError("Las nuevas contraseñas no coinciden.");
      return;
    }
    try {
      await UserService.changePassword({ currentPassword, newPassword });
      setSuccess("Contraseña cambiada con éxito.");
      setPasswordForm({
        currentPassword: "",
        newPassword: "",
        confirmPassword: "",
      }); // Clear password fields on success
    } catch (e) {
      setError(e.response?.data?.message || "Error al cambiar la contraseña.");
    }
  };

  /**
   * Handles logging out the user.
   * Clears authentication state and navigates to the login page.
   */
  const handleLogout = () => {
    logout();
    navigate("/login", { replace: true });
  };

  if (loading) {
    return (
      <Container component="main" maxWidth="lg">
        <Box
          component={RouterLink}
          to="/"
          display="flex"
          justifyContent="center"
          mb={2}
        >
          <Box component="img" src={logo} alt="IVAY Logo" sx={{ height: 60 }} />
        </Box>
        <Grid
          container
          component={Paper}
          elevation={3}
          sx={{
            overflow: "hidden",
            borderRadius: 2,
            maxWidth: 1000,
            margin: "0 auto",
          }}
        >
          <Grid
            item
            md={5}
            sx={{
              display: { xs: "none", md: "flex" },
              alignItems: "center",
              justifyContent: "center",
              borderRight: `1px solid ${colors.grey[300]}`,
              backgroundColor: "#f9f9f9",
            }}
          >
            <CircularProgress />
          </Grid>
          <Grid item xs={12} md={7} sx={{ p: 4, textAlign: "center" }}>
            <CircularProgress />
          </Grid>
        </Grid>
      </Container>
    );
  }

  return (
    <Container component="main" maxWidth="lg">
      {/* Logo en el centro */}
      <Box
        component={RouterLink}
        to="/"
        display="flex"
        justifyContent="center"
        mb={3}
      >
        <Box component="img" src={logo} alt="IVAY Logo" sx={{ height: 100 }} />
      </Box>

      <Grid
        container
        component={Paper}
        elevation={3}
        sx={{
          overflow: "hidden",
          borderRadius: 2,
          maxWidth: 1000,
          margin: "0 auto",
        }}
      >
        {/* IZQUIERDA: Beneficios + Cerrar sesión */}
        <Grid
          item
          md={5}
          sx={{
            display: { xs: "none", md: "flex" },
            alignItems: "center",
            justifyContent: "center",
            borderRight: `1px solid ${colors.grey[300]}`,
            backgroundColor: "#f9f9f9",
          }}
        >
          <Box
            sx={{
              p: { xs: 2, md: 14 },
              display: "flex",
              flexDirection: "column",
              alignItems: "center",
            }}
          >
            <Typography
              variant="h5"
              gutterBottom
              sx={{ mb: 3, fontWeight: "bold" }}
            >
              Beneficios
            </Typography>

            <Box
              sx={{
                display: "flex",
                alignItems: "center",
                mb: 2,
                maxWidth: 300,
                width: "100%",
              }}
            >
              <ManageAccountsOutlinedIcon
                sx={{ mr: 1.5, color: colors.primary.main }}
              />
              <Typography>Gestiona tus pedidos fácilmente.</Typography>
            </Box>

            <Box
              sx={{
                display: "flex",
                alignItems: "center",
                mb: 4,
                maxWidth: 400,
                width: "100%",
              }}
            >
              <FavoriteBorderOutlinedIcon
                sx={{ mr: 1.5, color: colors.primary.main }}
              />
              <Typography>Guarda tus productos favoritos.</Typography>
            </Box>

            <Button
              variant="contained"
              sx={{
                backgroundColor: colors.primary.light,
                color: "#fff",
                "&:hover": { backgroundColor: colors.primary.dark },
              }}
              startIcon={<ExitToAppOutlinedIcon />}
              onClick={handleLogout}
            >
              Cerrar sesión
            </Button>
          </Box>
        </Grid>

        {/* DERECHA: Formulario de Detalles de usuario */}
        <Grid
          item
          xs={12}
          md={7}
          sx={{ width: "50%", p: { xs: 2, sm: 3, md: 4 } }}
        >
          <Box
            sx={{
              display: "flex",
              flexDirection: "column",
              alignItems: "center",
            }}
          >
            <ManageAccountsOutlinedIcon
              sx={{ m: 1, fontSize: 40, color: colors.primary.main }}
            />
            <Typography
              component="h1"
              variant="h5"
              sx={{ fontWeight: "bold", mb: 2 }}
            >
              Detalles de usuario
            </Typography>

            {error && (
              <Alert severity="error" sx={{ width: "100%", mb: 2 }}>
                {error}
              </Alert>
            )}
            {success && (
              <Alert severity="success" sx={{ width: "100%", mb: 2 }}>
                {success}
              </Alert>
            )}

            {/* Datos personales */}
            <TextField
              fullWidth
              label="Nombre"
              name="name"
              value={form.name}
              onChange={handleChange}
              margin="normal"
              // Using InputProps instead of slotProps for readOnly in TextField
              InputProps={{ readOnly: readOnly }}
            />
            <TextField
              fullWidth
              label="Nombre completo"
              name="fullName"
              value={form.fullName}
              onChange={handleChange}
              margin="normal"
              InputProps={{ readOnly: readOnly }}
            />
            <TextField
              fullWidth
              label="Email"
              name="email"
              value={form.email}
              onChange={handleChange}
              margin="normal"
              InputProps={{ readOnly: readOnly }}
            />
            <TextField
              fullWidth
              label="Teléfono"
              name="phone"
              value={form.phone}
              onChange={handleChange}
              margin="normal"
              InputProps={{ readOnly: readOnly }}
            />
            <TextField
              fullWidth
              label="Dirección"
              name="userAddress"
              value={form.userAddress}
              onChange={handleChange}
              margin="normal"
              InputProps={{ readOnly: readOnly }}
            />
            <Button
              variant="contained"
              sx={{ mt: 2 }}
              onClick={readOnly ? () => setReadOnly(false) : handleSaveDetails}
            >
              {readOnly ? "Editar información" : "Guardar cambios"}
            </Button>

            {/* Sección de cambio de contraseña */}
            <TextField
              fullWidth
              label="Contraseña actual"
              name="currentPassword"
              type="password"
              value={passwordForm.currentPassword}
              onChange={handleChangePasswordForm}
              margin="normal"
            />
            <TextField
              fullWidth
              label="Nueva contraseña"
              name="newPassword"
              type="password"
              value={passwordForm.newPassword}
              onChange={handleChangePasswordForm}
              margin="normal"
            />
            <TextField
              fullWidth
              label="Confirmar nueva contraseña"
              name="confirmPassword"
              type="password"
              value={passwordForm.confirmPassword}
              onChange={handleChangePasswordForm}
              margin="normal"
            />
            <Button
              variant="contained"
              sx={{ mt: 2 }}
              onClick={handleSavePassword}
            >
              Cambiar contraseña
            </Button>
          </Box>
        </Grid>
      </Grid>
    </Container>
  );
}