import React, { useState } from 'react';
import { useNavigate, Link as RouterLink } from 'react-router-dom';
import {
    Box,
    TextField,
    Button,
    Typography,
    Link as MuiLink,
    CircularProgress,
    Alert
} from '@mui/material';
import LockOutlinedIcon from '@mui/icons-material/LockOutlined';
import AuthLayout from '../layout/AuthLayout';
import AuthService from '../../service/auth.service'; // Your auth service
// import { useAuth } from '../../context/AuthContext'; // Assuming you'll have an AuthContext
import logo from '../../assets/images/ivay-logo.png';

function LoginPage() {
    const navigate = useNavigate();
    // const { login: contextLogin } = useAuth(); // From your AuthContext
    const [formData, setFormData] = useState({
        email: '', // Assuming backend uses 'email' for login field (adjust if 'nombre')
        password: '',
    });
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState('');

    const handleChange = (event) => {
        setFormData({ ...formData, [event.target.name]: event.target.value });
    };

    const handleSubmit = async (event) => {
        event.preventDefault();
        setLoading(true);
        setError('');
        try {
            // Use the DTO structure your backend expects for AuthLoginRequestDto
            const authLoginRequest = {
                email: formData.email, // or username if that's what your backend expects
                password: formData.password,
            };
            const authResponse = await AuthService.login(authLoginRequest);
            console.log('Login successful:', authResponse);
            // TODO: Store token and user data (e.g., contextLogin(authResponse.token, authResponse.user);)
            localStorage.setItem('authToken', authResponse.token); // Example: store token
            localStorage.setItem('user', JSON.stringify(authResponse.user)); // Example: store user

            navigate('/'); // Redirect to home page or dashboard after login
        } catch (err) {
            console.error("Login error:", err);
            setError(err.message || 'Error al iniciar sesión. Verifique sus credenciales.');
        } finally {
            setLoading(false);
        }
    };

    return (
        <AuthLayout pageTitle="Iniciar Sesión" formIcon={<LockOutlinedIcon />}>
            <Box component="form" onSubmit={handleSubmit} noValidate sx={{ mt: 1, width: '100%', maxWidth: 400 }}>
                {error && <Alert severity="error" sx={{ mb: 2 }}>{error}</Alert>}
                <TextField
                    margin="normal"
                    required
                    fullWidth
                    id="email"
                    label="Correo Electrónico / Nombre" // Label can be "Correo Electrónico"
                    name="email" // Change to "username" if backend expects that
                    autoComplete="email"
                    autoFocus
                    value={formData.email}
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
                {/* TODO: Add "Forgot password?" link if needed */}
                <Button
                    type="submit"
                    fullWidth
                    variant="contained"
                    sx={{ mt: 3, mb: 2 }}
                    disabled={loading}
                >
                    {loading ? <CircularProgress size={24} color="inherit" /> : 'Iniciar Sesión'}
                </Button>
                <Box sx={{ textAlign: 'center' }}>
                    <MuiLink component={RouterLink} to="/register" variant="body2">
                        {"¿No tienes cuenta? Crea una"}
                    </MuiLink>
                </Box>
            </Box>
        </AuthLayout>
    );
}

export default LoginPage;