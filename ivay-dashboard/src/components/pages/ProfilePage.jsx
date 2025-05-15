import React, { useState, useEffect } from 'react';
import { Typography, Paper, TextField, Button, CircularProgress, Alert, Box } from '@mui/material';
import { useAuth } from '../../context/AuthContext';
import UserService from '../../service/user.service';
import { colors } from '../../constants/styles';

const ProfilePage = () => {
    const { user, setUser, logout } = useAuth();
    const [form, setForm] = useState({ name: '', fullName: '', email: '', phone: '' });
    const [passwordForm, setPasswordForm] = useState({ currentPassword: '', newPassword: '', confirmPassword: '' });
    const [loading, setLoading] = useState(true);
    const [savingDetails, setSavingDetails] = useState(false);
    const [savingPassword, setSavingPassword] = useState(false);
    const [error, setError] = useState('');
    const [success, setSuccess] = useState('');

    useEffect(() => {
        if (user) {
            setForm({
                name: user.name || '',
                fullName: user.fullName || '',
                email: user.email || '',
                phone: user.phone || '',
                // address: user.address || '', // If admin has address
            });
            setLoading(false);
        } else {
            setLoading(false);
            setError("No se pudo cargar la información del perfil.");
        }
    }, [user]);

    const handleChange = (e) => setForm(prev => ({ ...prev, [e.target.name]: e.target.value }));
    const handleChangePassword = (e) => setPasswordForm(prev => ({ ...prev, [e.target.name]: e.target.value }));

    const handleSaveDetails = async () => {
        setError(''); setSuccess(''); setSavingDetails(true);
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

    const handleSavePassword = async () => {
        setError(''); setSuccess(''); setSavingPassword(true);
        if (passwordForm.newPassword !== passwordForm.confirmPassword) {
            setError("Las nuevas contraseñas no coinciden.");
            setSavingPassword(false);
            return;
        }
        try {
            await UserService.changePassword({
                currentPassword: passwordForm.currentPassword,
                newPassword: passwordForm.newPassword,
            });
            setSuccess("Contraseña cambiada con éxito.");
            setPasswordForm({ currentPassword: '', newPassword: '', confirmPassword: '' });
        } catch (e) {
            setError(e.response?.data?.message || "Error al cambiar la contraseña.");
        } finally {
            setSavingPassword(false);
        }
    };


    if (loading) return <CircularProgress />;

    return (
        <>
            <Typography variant="h5" component="h1" gutterBottom fontWeight="bold">
                Mi Perfil (Gestor)
            </Typography>
            <Paper sx={{ p: 3, maxWidth: 600, mx: 'auto' }}>
                {error && <Alert severity="error" sx={{ mb: 2 }}>{error}</Alert>}
                {success && <Alert severity="success" sx={{ mb: 2 }}>{success}</Alert>}

                <Typography variant="h6" gutterBottom>Detalles Personales</Typography>
                <TextField fullWidth margin="normal" label="Nombre de Usuario" name="name" value={form.name} onChange={handleChange} InputProps={{ readOnly: true }} />
                <TextField fullWidth margin="normal" label="Nombre Completo" name="fullName" value={form.fullName} onChange={handleChange} />
                <TextField fullWidth margin="normal" label="Email" name="email" value={form.email} onChange={handleChange} InputProps={{ readOnly: true }} />
                <TextField fullWidth margin="normal" label="Teléfono" name="phone" value={form.phone} onChange={handleChange} />
                <Button variant="contained" onClick={handleSaveDetails} disabled={savingDetails} sx={{ mt: 2, mb: 4 }}>
                    {savingDetails ? <CircularProgress size={24} /> : "Guardar Detalles"}
                </Button>

                <Typography variant="h6" gutterBottom>Cambiar Contraseña</Typography>
                <TextField fullWidth margin="normal" type="password" label="Contraseña Actual" name="currentPassword" value={passwordForm.currentPassword} onChange={handleChangePassword} />
                <TextField fullWidth margin="normal" type="password" label="Nueva Contraseña" name="newPassword" value={passwordForm.newPassword} onChange={handleChangePassword} />
                <TextField fullWidth margin="normal" type="password" label="Confirmar Nueva Contraseña" name="confirmPassword" value={passwordForm.confirmPassword} onChange={handleChangePassword} />
                <Button variant="contained" onClick={handleSavePassword} disabled={savingPassword} sx={{ mt: 2, mb: 4 }}>
                    {savingPassword ? <CircularProgress size={24} /> : "Cambiar Contraseña"}
                </Button>

                <Button variant="outlined" color="error" onClick={logout} sx={{ display: 'block', mx: 'auto', mt: 3 }}>
                    Cerrar Sesión
                </Button>
            </Paper>
        </>
    );
};

export default ProfilePage;