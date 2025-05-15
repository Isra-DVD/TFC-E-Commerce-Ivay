import React, { useState, useEffect } from "react";
import { useNavigate, useLocation } from "react-router-dom";
import {
    Box,
    TextField,
    Button,
    Typography,
    CircularProgress,
    Alert,
    Paper,
    Avatar,
} from "@mui/material";
import LockOutlinedIcon from "@mui/icons-material/LockOutlined";
import { useAuth } from "../../context/AuthContext";
import logo from "../../assets/images/ivay-logo.png";
import { colors } from "../../constants/styles";

function LoginPage() {
    const navigate = useNavigate();
    const location = useLocation();
    const { login, isAuthenticated, isLoading: authIsLoading, user } = useAuth();

    const [formData, setFormData] = useState({
        username: "",
        password: "",
    });
    const [isSubmitting, setIsSubmitting] = useState(false);
    const [error, setError] = useState(location.state?.message || "");

    useEffect(() => {
        if (!authIsLoading && isAuthenticated && user) {
            // TODO: More robust role check from user object if available
            // Example: if (user.roles && user.roles.includes('ROLE_ADMIN'))
            navigate(location.state?.from?.pathname || "/products", { replace: true });
        }
    }, [isAuthenticated, authIsLoading, user, navigate, location.state]);


    const handleChange = (e) => {
        setFormData((prev) => ({ ...prev, [e.target.name]: e.target.value }));
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setIsSubmitting(true);
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
                "Error al iniciar sesión. Verifica tus credenciales o contacta al administrador."
            );
            setIsSubmitting(false);
        }
    };

    // Handles overall auth loading state (e.g. checking token on app load)
    if (authIsLoading && !isSubmitting) {
        return (
            <Box display="flex" justifyContent="center" alignItems="center" minHeight="100vh">
                <CircularProgress />
            </Box>
        );
    }

    return (
        <Box
            sx={{
                display: "flex",
                flexDirection: "column",
                alignItems: "center",
                justifyContent: "center",
                minHeight: "100vh",
                backgroundColor: colors.background.default,
                p: 2,
            }}
        >
            <Paper
                elevation={6}
                sx={{
                    p: { xs: 3, sm: 4 },
                    display: "flex",
                    flexDirection: "column",
                    alignItems: "center",
                    maxWidth: 450,
                    width: "100%",
                    borderRadius: 2,
                }}
            >
                <img
                    src={logo}
                    alt="IVAY Logo"
                    style={{ height: 80, width: "auto", marginBottom: 24 }}
                />
                <Avatar sx={{ m: 1, bgcolor: colors.primary.main }}>
                    <LockOutlinedIcon />
                </Avatar>
                <Typography component="h1" variant="h5" sx={{ fontWeight: "bold" }}>
                    Acceso Gestor
                </Typography>
                <Box
                    component="form"
                    onSubmit={handleSubmit}
                    noValidate
                    sx={{ mt: 1, width: "100%" }}
                >
                    {error && (
                        <Alert severity="error" sx={{ mb: 2, width: '100%' }}>
                            {error}
                        </Alert>
                    )}
                    <TextField
                        margin="normal"
                        required
                        fullWidth
                        id="username"
                        label="Usuario"
                        name="username"
                        autoComplete="username"
                        autoFocus
                        value={formData.username}
                        onChange={handleChange}
                        disabled={isSubmitting || authIsLoading}
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
                        disabled={isSubmitting || authIsLoading}
                    />
                    <Button
                        type="submit"
                        fullWidth
                        variant="contained"
                        sx={{ mt: 3, mb: 2, py: 1.5 }}
                        disabled={isSubmitting || authIsLoading}
                    >
                        {(isSubmitting || authIsLoading) ? (
                            <CircularProgress size={24} color="inherit" />
                        ) : (
                            "Iniciar Sesión"
                        )}
                    </Button>
                </Box>
            </Paper>
        </Box>
    );
}

export default LoginPage;