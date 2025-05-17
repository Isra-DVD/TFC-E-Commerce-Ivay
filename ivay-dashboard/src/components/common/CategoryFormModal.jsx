import React, { useState, useEffect } from 'react';
import {
    Modal, Box, Typography, TextField, Button, IconButton,
    CircularProgress, Alert, Paper,
} from '@mui/material';
import CloseIcon from '@mui/icons-material/Close';
import { colors } from '../../constants/styles';

const modalStyle = {
    position: 'absolute',
    top: '50%',
    left: '50%',
    transform: 'translate(-50%, -50%)',
    width: { xs: '90%', sm: '400px' },
    bgcolor: 'background.paper',
    boxShadow: 24,
    borderRadius: 2,
    outline: 'none',
    display: 'flex',
    flexDirection: 'column',
};

const CategoryFormModal = ({ open, onClose, onSubmit, categoryToEdit, isSubmitting, serverError }) => {
    const initialFormState = { name: '' };
    const [formData, setFormData] = useState(initialFormState);
    const [internalError, setInternalError] = useState('');

    useEffect(() => {
        if (open) {
            if (categoryToEdit) {
                setFormData({
                    name: categoryToEdit.name || '',
                });
            } else {
                setFormData(initialFormState);
            }
            setInternalError('');
        }
    }, [categoryToEdit, open]);

    const handleChange = (e) => {
        setFormData(prev => ({ ...prev, [e.target.name]: e.target.value }));
    };

    const validateForm = () => {
        if (!formData.name.trim()) return "El nombre de la categoría es obligatorio.";
        return "";
    };

    const handleSubmit = (e) => {
        e.preventDefault();
        const validationError = validateForm();
        if (validationError) {
            setInternalError(validationError);
            return;
        }
        setInternalError('');
        const id = categoryToEdit ? categoryToEdit.id : undefined;
        onSubmit({ name: formData.name }, id);
    };

    const title = categoryToEdit ? "Editar Categoría" : "Nueva Categoría";

    return (
        <Modal open={open} onClose={onClose} aria-labelledby="category-form-modal-title">
            <Paper sx={modalStyle}>
                <Box sx={{ p: 2, display: 'flex', justifyContent: 'space-between', alignItems: 'center', borderBottom: `1px solid ${colors.grey[300]}` }}>
                    <Typography id="category-form-modal-title" variant="h6" component="h2" fontWeight="bold">
                        {title}
                    </Typography>
                    <IconButton onClick={onClose} aria-label="close" disabled={isSubmitting}>
                        <CloseIcon />
                    </IconButton>
                </Box>

                <Box component="form" onSubmit={handleSubmit} sx={{ p: 2.5, overflowY: 'auto', flexGrow: 1 }}>
                    {(internalError || serverError) && (
                        <Alert severity="error" sx={{ mb: 2 }}>{internalError || serverError}</Alert>
                    )}
                    <TextField
                        fullWidth
                        label="Nombre de la categoría"
                        name="name"
                        value={formData.name}
                        onChange={handleChange}
                        margin="normal"
                        required
                        autoFocus
                        disabled={isSubmitting}
                    />
                </Box>

                <Box sx={{ p: 2, display: 'flex', justifyContent: 'flex-end', alignItems: 'center', borderTop: `1px solid ${colors.grey[300]}`, gap: 1.5 }}>
                    <Button onClick={onClose} variant="outlined" color="inherit" disabled={isSubmitting}>
                        Cancelar
                    </Button>
                    <Button type="submit" variant="contained" onClick={handleSubmit} disabled={isSubmitting} sx={{ backgroundColor: colors.primary.main, '&:hover': { backgroundColor: colors.primary.dark } }}>
                        {isSubmitting ? <CircularProgress size={24} color="inherit" /> : "Confirmar"}
                    </Button>
                </Box>
            </Paper>
        </Modal>
    );
};

export default CategoryFormModal;