import React from 'react';
import { Typography, Paper, Button } from '@mui/material';
import AddIcon from '@mui/icons-material/Add';
import { colors } from '../../constants/styles';


// This will be expanded with a table/list of categories and CRUD operations
const CategoriesPage = () => {
    return (
        <>
            <Typography variant="h5" component="h1" gutterBottom fontWeight="bold">
                Gestión de Categorías
            </Typography>
            <Paper elevation={0} sx={{ p: 2, mb: 3, display: 'flex', alignItems: 'center', gap: 2, backgroundColor: 'transparent' }}>
                <Button
                    variant="contained"
                    startIcon={<AddIcon />}
                    // onClick={handleAddNewCategory}
                    sx={{ backgroundColor: colors.primary.main, '&:hover': { backgroundColor: colors.primary.dark } }}
                >
                    Nueva Categoría
                </Button>
                {/* Add search or filters if needed */}
            </Paper>
            <Paper sx={{ p: 2 }}>
                <Typography>
                    Listado de categorías (tabla o lista) y funcionalidades CRUD irán aquí.
                </Typography>
                {/* Example: <CategoryTable categories={categories} onEdit={...} onDelete={...} /> */}
            </Paper>
        </>
    );
};

export default CategoriesPage;