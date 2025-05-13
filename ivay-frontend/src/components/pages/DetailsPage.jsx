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

export default function DetailsPage() {
  const { logout, user } = useAuth();
  const navigate = useNavigate();

  const [loading, setLoading] = useState(true);
  const [readOnly, setReadOnly] = useState(true);
  const [form, setForm] = useState({
    name: "",
    fullName: "",
    email: "",
    phone: "",
    address: "",
  });
  const [passwordForm, setPasswordForm] = useState({
    currentPassword: "",
    newPassword: "",
    confirmPassword: "",
  });
  const [error, setError] = useState("");
  const [success, setSuccess] = useState("");

  useEffect(() => {
    async function fetchProfile() {
      try {
        const data = user || (await UserService.getMyProfile());
        setForm({
          name: data.name || "",
          fullName: data.fullName || "",
          email: data.email || "",
          phone: data.phone || "",
          address: data.address || "",
        });
      } catch (e) {
        setError("No se pudo cargar el perfil.");
        if (e.response?.status === 401) {
          logout();
          navigate("/login", { replace: true });
        }
      } finally {
        setLoading(false);
      }
    }
    fetchProfile();
  }, [user, logout, navigate]);

  const handleChange = (e) =>
    setForm((prev) => ({ ...prev, [e.target.name]: e.target.value }));

  const handleChangePasswordForm = (e) =>
    setPasswordForm((prev) => ({ ...prev, [e.target.name]: e.target.value }));

  const handleSaveDetails = async () => {
    setError("");
    setSuccess("");
    try {
      await UserService.updateMyProfile(form);
      setSuccess("Datos actualizados con éxito.");
      setReadOnly(true);
    } catch {
      setError("Error actualizando datos.");
    }
  };

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
      });
    } catch (e) {
      setError(e.response?.data?.message || "Error al cambiar la contraseña.");
    }
  };

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
                "&:hover": {
                  backgroundColor: colors.primary.dark,
                },
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
              InputProps={{ readOnly }}
            />
            <TextField
              fullWidth
              label="Nombre completo"
              name="fullName"
              value={form.fullName}
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
