import React, { useEffect, useState, useCallback } from 'react';
import {
    Box, Typography, Button, Paper, TextField, InputAdornment, IconButton,
    Table, TableBody, TableCell, TableContainer, TableHead, TableRow, TablePagination,
    CircularProgress, Alert, Menu, MenuItem, ListItemIcon, ListItemText,
} from '@mui/material';
import AddIcon from '@mui/icons-material/Add';
import SearchIcon from '@mui/icons-material/Search';
import StorefrontIcon from '@mui/icons-material/Storefront';
import MoreVertIcon from '@mui/icons-material/MoreVert';
import EditIcon from '@mui/icons-material/Edit';
import DeleteIcon from '@mui/icons-material/Delete';

import CategoryService from '../../service/category.service';
import { colors } from '../../constants/styles';
import CategoryFormModal from '../common/CategoryFormModal';

const ROWS_PER_PAGE_OPTIONS = [10, 20, 50];

function CategoriesPage() {
    const [categories, setCategories] = useState([]);
    const [allCategoriesCache, setAllCategoriesCache] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState('');
    const [newCategoryName, setNewCategoryName] = useState('');
    const [isCreating, setIsCreating] = useState(false);
    const [globalSearchTerm, setGlobalSearchTerm] = useState('');

    const [page, setPage] = useState(0);
    const [rowsPerPage, setRowsPerPage] = useState(ROWS_PER_PAGE_OPTIONS[1]);

    const [isEditModalOpen, setIsEditModalOpen] = useState(false);
    const [editingCategory, setEditingCategory] = useState(null);
    const [isEditFormSubmitting, setIsEditFormSubmitting] = useState(false);
    const [editFormServerError, setEditFormServerError] = useState('');

    const [anchorEl, setAnchorEl] = useState(null);
    const [currentCategoryMenu, setCurrentCategoryMenu] = useState(null);

    const handleOpenMenu = (event, category) => {
        setAnchorEl(event.currentTarget);
        setCurrentCategoryMenu(category);
    };
    const handleCloseMenu = () => {
        setAnchorEl(null);
        setCurrentCategoryMenu(null);
    };

    const fetchCategories = useCallback(async () => {
        setLoading(true);
        setError('');
        try {
            const allFetchedCategories = await CategoryService.getAllCategories();
            setAllCategoriesCache(allFetchedCategories || []);
            setCategories(allFetchedCategories || []);
        } catch (err) {
            console.error("Error fetching categories:", err);
            setError('No se pudieron cargar las categorías.');
            setAllCategoriesCache([]);
            setCategories([]);
        } finally {
            setLoading(false);
        }
    }, []);

    useEffect(() => {
        fetchCategories();
    }, [fetchCategories]);

    useEffect(() => {
        if (globalSearchTerm.trim()) {
            const filtered = allCategoriesCache.filter(cat =>
                cat.name.toLowerCase().includes(globalSearchTerm.trim().toLowerCase())
            );
            setCategories(filtered);
        } else {
            setCategories(allCategoriesCache);
        }
        setPage(0);
    }, [globalSearchTerm, allCategoriesCache]);


    const handleNewCategoryNameChange = (event) => {
        setNewCategoryName(event.target.value);
    };

    const handleCreateNewCategory = async (event) => {
        event.preventDefault();
        if (!newCategoryName.trim()) {
            setError("El nombre para la nueva categoría no puede estar vacío.");
            return;
        }
        setIsCreating(true);
        setError('');
        try {
            await CategoryService.createCategory({ name: newCategoryName.trim() });
            setNewCategoryName('');
            fetchCategories();
        } catch (err) {
            console.error("Error creating category:", err);
            setError(err.response?.data?.message || err.message || "Error al crear la categoría.");
        } finally {
            setIsCreating(false);
        }
    };

    const handleGlobalSearchChange = (event) => {
        setGlobalSearchTerm(event.target.value);
    };

    const handleChangePage = (event, newPage) => {
        setPage(newPage);
    };
    const handleChangeRowsPerPage = (event) => {
        setRowsPerPage(parseInt(event.target.value, 10));
        setPage(0);
    };

    const handleOpenEditModal = () => {
        if (currentCategoryMenu) {
            setEditingCategory(currentCategoryMenu);
            setEditFormServerError('');
            setIsEditModalOpen(true);
        }
        handleCloseMenu();
    };
    const handleCloseEditModal = () => {
        setIsEditModalOpen(false);
        setEditingCategory(null);
        setEditFormServerError('');
    };

    const handleEditCategoryFormSubmit = async (categoryData, categoryId) => {
        setIsEditFormSubmitting(true);
        setEditFormServerError('');
        try {
            await CategoryService.updateCategory(categoryId, categoryData);
            handleCloseEditModal();
            fetchCategories();
        } catch (err) {
            console.error("Error updating category:", err);
            setEditFormServerError(err.response?.data?.message || err.message || "Error al actualizar la categoría.");
        } finally {
            setIsEditFormSubmitting(false);
        }
    };

    const handleDeleteCategory = async () => {
        if (currentCategoryMenu && window.confirm(`¿Estás seguro de que quieres eliminar la categoría "${currentCategoryMenu.name}"?`)) {
            setError('');
            try {
                await CategoryService.deleteCategory(currentCategoryMenu.id);
                fetchCategories();
            } catch (err) {
                console.error("Error deleting category:", err);
                setError("Error al eliminar la categoría. Verifique si tiene productos asociados.");
            }
        }
        handleCloseMenu();
    };

    const paginatedCategories = categories.slice(page * rowsPerPage, page * rowsPerPage + rowsPerPage);

    return (
        <>
            <Typography variant="h5" component="h1" gutterBottom fontWeight="bold">
                Categorías / Vista de gestor
            </Typography>

            <Paper elevation={0} sx={{ p: 2, mb: 3, display: 'flex', alignItems: 'center', gap: 1.5, backgroundColor: 'transparent', flexWrap: 'wrap' }}>
                {/* Input for New Category */}
                <TextField
                    variant="outlined"
                    placeholder="Nombre de la categoría"
                    value={newCategoryName}
                    onChange={handleNewCategoryNameChange}
                    size="small"
                    sx={{ width: { xs: 'calc(100% - 130px)', sm: 220 } }}
                    disabled={isCreating}
                    onKeyUp={(e) => { if (e.key === 'Enter' && newCategoryName.trim()) handleCreateNewCategory(e); }}
                />
                <Button
                    variant="contained"
                    startIcon={isCreating ? <CircularProgress size={20} color="inherit" /> : <AddIcon />}
                    onClick={handleCreateNewCategory}
                    disabled={isCreating || !newCategoryName.trim()}
                    sx={{ backgroundColor: colors.primary.main, '&:hover': { backgroundColor: colors.primary.dark } }}
                >
                    Nuevo
                </Button>

                {/* Global "Buscar" Textfield */}
                <TextField
                    variant="outlined"
                    placeholder="Buscar..."
                    value={globalSearchTerm}
                    onChange={handleGlobalSearchChange}
                    size="small"

                    sx={{ width: { xs: '100%', sm: 400 }, mt: { xs: 1.5, sm: 0 }, ml: { sm: 'auto' } }}
                    slotProps={{
                        endAdornment: (
                            <InputAdornment position="end">
                                <IconButton size="small" edge="end" aria-label="search categories">
                                    <SearchIcon fontSize="small" />
                                </IconButton>
                            </InputAdornment>
                        ),
                    }}
                />
            </Paper>

            {/* Table and other elements */}
            {loading && (<Box sx={{ display: "flex", justifyContent: "center", py: 5 }}><CircularProgress /></Box>)}
            {error && !loading && (<Alert severity="error" sx={{ my: 2 }}>{error}</Alert>)}

            {!loading && !error && (
                <Paper sx={{ width: '100%', overflow: 'hidden' }}>
                    <TableContainer sx={{ maxHeight: { xs: 'calc(100vh - 350px)', md: 'calc(100vh - 320px)' } }}>
                        <Table stickyHeader aria-label="tabla de categorías">
                            <TableHead>
                                <TableRow>
                                    <TableCell sx={{ width: '60px', borderBottomColor: colors.grey[400], pl: { xs: 1, sm: 2 } }}> </TableCell>
                                    <TableCell sx={{ fontWeight: 'bold', borderBottomColor: colors.grey[400] }}>Nombre de categoría</TableCell>
                                    <TableCell align="right" sx={{ width: '80px', borderBottomColor: colors.grey[400], pr: { xs: 1, sm: 2 } }}>Acciones</TableCell>
                                </TableRow>
                            </TableHead>
                            <TableBody>
                                {paginatedCategories.length === 0 && (
                                    <TableRow>
                                        <TableCell colSpan={3} align="center" sx={{ py: 3 }}>
                                            <Typography color="text.secondary">
                                                {globalSearchTerm ? `No se encontraron categorías para "${globalSearchTerm}".` : "No hay categorías disponibles."}
                                            </Typography>
                                        </TableCell>
                                    </TableRow>
                                )}
                                {paginatedCategories.map((category) => (
                                    <TableRow hover role="checkbox" tabIndex={-1} key={category.id}>
                                        <TableCell sx={{ borderBottomColor: colors.grey[300], pl: { xs: 1, sm: 2 } }}>
                                            <StorefrontIcon sx={{ color: colors.primary.light, verticalAlign: 'middle' }} />
                                        </TableCell>
                                        <TableCell sx={{ borderBottomColor: colors.grey[300] }}>
                                            {category.name}
                                        </TableCell>
                                        <TableCell align="right" sx={{ borderBottomColor: colors.grey[300], pr: { xs: 1, sm: 2 } }}>
                                            <IconButton aria-label="actions" onClick={(event) => handleOpenMenu(event, category)}>
                                                <MoreVertIcon />
                                            </IconButton>
                                        </TableCell>
                                    </TableRow>
                                ))}
                            </TableBody>
                        </Table>
                    </TableContainer>
                    <TablePagination
                        rowsPerPageOptions={ROWS_PER_PAGE_OPTIONS}
                        component="div"
                        count={categories.length}
                        rowsPerPage={rowsPerPage}
                        page={page}
                        onPageChange={handleChangePage}
                        onRowsPerPageChange={handleChangeRowsPerPage}
                        labelRowsPerPage="Filas por página:"
                        labelDisplayedRows={({ from, to, count }) => `${from}-${to} de ${count !== -1 ? count : `más de ${to}`}`}
                    />
                </Paper>
            )}

            <Menu anchorEl={anchorEl} open={Boolean(anchorEl)} onClose={handleCloseMenu}>
                <MenuItem onClick={handleOpenEditModal}>
                    <ListItemIcon><EditIcon fontSize="small" /></ListItemIcon>
                    <ListItemText>Editar</ListItemText>
                </MenuItem>
                <MenuItem onClick={handleDeleteCategory} sx={{ color: colors.error.main }}>
                    <ListItemIcon><DeleteIcon fontSize="small" sx={{ color: colors.error.main }} /></ListItemIcon>
                    <ListItemText>Eliminar</ListItemText>
                </MenuItem>
            </Menu>

            {editingCategory && (
                <CategoryFormModal
                    open={isEditModalOpen}
                    onClose={handleCloseEditModal}
                    onSubmit={handleEditCategoryFormSubmit}
                    categoryToEdit={editingCategory}
                    isSubmitting={isEditFormSubmitting}
                    serverError={editFormServerError}
                />
            )}
        </>
    );
}

export default CategoriesPage;