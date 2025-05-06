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
import PersonAddOutlinedIcon from '@mui/icons-material/PersonAddOutlined';
import AuthLayout from '../layout/AuthLayout';
import UserService from '../../service/user.service.js'; // Your user service for registration

function RegisterPage() {
    const navigate = useNavigate();
    const [formData, setFormData] = useState({
        name: '',
        email: '',
        phone: '',
        address: '', // Assuming address is a single string for simplicity here
        password: '',
        confirmPassword: '',
        roleId: 2, // Default roleId for new users (e.g., ROLE_USER) - adjust as needed
    });
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState('');
    const [success, setSuccess] = useState('');


    const handleChange = (event) => {
        setFormData({ ...formData, [event.target.name]: event.target.value });
    };

    const handleSubmit = async (event) => {
        event.preventDefault();
        if (formData.password !== formData.confirmPassword) {
            setError('Las contraseñas no coinciden.');
            return;
        }
        setLoading(true);
        setError('');
        setSuccess('');

        try {
            // Prepare the DTO matching CreateUserRequestDto from backend
            const createUserRequest = {
                name: formData.name,
                email: formData.email,
                password: formData.password, // Send plain password, backend will hash
                phone: formData.phone,
                // Address might be part of a separate profile update later or included here
                // For now, assuming it's not part of initial user creation via UserService.createUser
                // If it is, add it to the DTO:
                // address: formData.address,
                roleId: formData.roleId, // Make sure this is set (e.g., default user role)
                isEnabled: true, // Or handle activation email logic
                accountNoExpired: true,
                accountNoLocked: true,
                credentialNoExpired: true,
            };

            const registeredUser = await UserService.createUser(createUserRequest);
            console.log('Registration successful:', registeredUser);
            setSuccess('¡Cuenta creada con éxito! Ya puedes iniciar sesión.');
            // Optionally auto-login or redirect after a delay
            setTimeout(() => {
                navigate('/login');
            }, 2000);

        } catch (err) {
            console.error("Registration error:", err);
            setError(err.response?.data?.message || err.message || 'Error al registrar la cuenta.');
        } finally {
            setLoading(false);
        }
    };

    return (
        <AuthLayout pageTitle="Crear cuenta" formIcon={<PersonAddOutlinedIcon />}>
            <Box component="form" onSubmit={handleSubmit} noValidate sx={{ mt: 1, width: '100%', maxWidth: 400 }}>
                {error && <Alert severity="error" sx={{ mb: 2 }}>{error}</Alert>}
                {success && <Alert severity="success" sx={{ mb: 2 }}>{success}</Alert>}
                <TextField
                    margin="normal"
                    required
                    fullWidth
                    id="name"
                    label="Nombre completo"
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
                    id="email"
                    label="Correo Electrónico"
                    name="email"
                    autoComplete="email"
                    value={formData.email}
                    onChange={handleChange}
                    disabled={loading}
                />
                <TextField // Optional: Phone
                    margin="normal"
                    fullWidth
                    id="phone"
                    label="Teléfono (Opcional)"
                    name="phone"
                    autoComplete="tel"
                    value={formData.phone}
                    onChange={handleChange}
                    disabled={loading}
                />
                <TextField // Optional: Address (might be better in a profile page)
                    margin="normal"
                    fullWidth
                    id="address"
                    label="Dirección (Opcional)"
                    name="address"
                    autoComplete="street-address"
                    value={formData.address}
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
                    label="Confirmar Contraseña"
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
                    {loading ? <CircularProgress size={24} color="inherit" /> : 'Crear cuenta'}
                </Button>
                <Box sx={{ textAlign: 'center' }}>
                    <MuiLink component={RouterLink} to="/login" variant="body2">
                        {"¿Ya tienes cuenta? Inicia sesión"}
                    </MuiLink>
                </Box>
            </Box>
        </AuthLayout>
    );
}

export default RegisterPage;