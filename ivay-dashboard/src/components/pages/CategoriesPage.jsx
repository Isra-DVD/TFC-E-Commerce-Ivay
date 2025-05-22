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

/**
 * CategoriesPage component displays a list of product categories,
 * provides functionality to add new categories, search existing ones,
 * and edit or delete categories via a modal form.
 */
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

    /**
     * Opens the action menu for a specific category, anchored to the click event target.
     *
     * @param {object} event - The click event object.
     * @param {object} category - The category object for which the menu is opened.
     */
    const handleOpenMenu = (event, category) => {
        setAnchorEl(event.currentTarget);
        setCurrentCategoryMenu(category);
    };
    /**
     * Closes the action menu and resets the current category in the menu state.
     */
    const handleCloseMenu = () => {
        setAnchorEl(null);
        setCurrentCategoryMenu(null);
    };

    /**
     * Fetches all categories from the service and updates the categories and cache states.
     * Sets loading state during fetch and handles errors.
     */
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

    /* Effect hook to fetch categories when the component mounts or fetchCategories callback changes. */
    useEffect(() => {
        fetchCategories();
    }, [fetchCategories]);

    /* Effect hook to filter categories based on the global search term whenever it or the cache changes. Resets to the first page. */
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

    /**
     * Handles changes in the new category name input field.
     *
     * @param {object} event - The input change event object.
     */
    const handleNewCategoryNameChange = (event) => {
        setNewCategoryName(event.target.value);
    };

    /**
     * Handles the creation of a new category.
     * Validates the input, calls the category service, updates states, and refetches categories on success.
     *
     * @param {object} event - The form submit event object.
     */
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

    /**
     * Handles changes in the global search input field.
     *
     * @param {object} event - The input change event object.
     */
    const handleGlobalSearchChange = (event) => {
        setGlobalSearchTerm(event.target.value);
    };

    /**
     * Handles the change of the current page in the pagination component.
     *
     * @param {object} event - The pagination change event object.
     * @param {number} newPage - The new page number.
     */
    const handleChangePage = (event, newPage) => {
        setPage(newPage);
    };
    /**
     * Handles the change in the number of rows displayed per page in the pagination component.
     * Resets the page to 0.
     *
     * @param {object} event - The pagination change event object.
     */
    const handleChangeRowsPerPage = (event) => {
        setRowsPerPage(parseInt(event.target.value, 10));
        setPage(0);
    };

    /**
     * Opens the edit modal, populating it with the currently selected category data.
     * Clears any previous server errors for the form and closes the action menu.
     */
    const handleOpenEditModal = () => {
        if (currentCategoryMenu) {
            setEditingCategory(currentCategoryMenu);
            setEditFormServerError('');
            setIsEditModalOpen(true);
        }
        handleCloseMenu();
    };
    /**
     * Closes the edit modal and resets related states (editing category and server errors).
     */
    const handleCloseEditModal = () => {
        setIsEditModalOpen(false);
        setEditingCategory(null);
        setEditFormServerError('');
    };

    /**
     * Handles the submission of the edit category form.
     * Calls the category service to update the category, closes the modal on success, and refetches categories.
     * Sets submitting state and handles server errors.
     *
     * @param {object} categoryData - The updated category data from the form.
     * @param {number} categoryId - The ID of the category being edited.
     */
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

    /**
     * Handles the deletion of the currently selected category after a confirmation prompt.
     * Calls the category service to delete the category, refetches categories on success.
     * Sets submitting state and handles errors, particularly for constraint violations.
     */
    const handleDeleteCategory = async () => {
        if (currentCategoryMenu && window.confirm(`¿Estás seguro de que quieres eliminar la categoría "${currentCategoryMenu.name}"?`)) {
            setError('');
            try {
                await CategoryService.deleteCategory(currentCategoryMenu.id);
                fetchCategories();
            } catch (err) {
                console.error("Error deleting category:", err);
                if (err.response?.status === 400 || err.response?.data?.message?.includes('foreign key constraint')) {
                    setError("No se puede eliminar la categoría porque tiene productos asociados.");
                } else {
                    setError(err.response?.data?.message || err.message || "Error al eliminar la categoría.");
                }
            }
        }
        handleCloseMenu();
    };

    /* Computes the subset of categories to display based on the current page and rows per page. */
    const paginatedCategories = categories.slice(page * rowsPerPage, page * rowsPerPage + rowsPerPage);

    return (
        <>
            <Typography variant="h5" component="h1" gutterBottom fontWeight="bold">
                Categorías / Vista de gestor
            </Typography>

            <Paper elevation={0} sx={{ p: 2, mb: 3, display: 'flex', alignItems: 'center', gap: 1.5, backgroundColor: 'transparent', flexWrap: 'wrap' }}>
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

                <TextField
                    variant="outlined"
                    placeholder="Buscar categoría..."
                    value={globalSearchTerm}
                    onChange={handleGlobalSearchChange}
                    size="small"

                    sx={{ width: { xs: '100%', sm: 500 }, mt: { xs: 1.5, sm: 0 }, ml: { sm: 'auto' } }}
                    InputProps={{
                        endAdornment: (
                            <InputAdornment position="end">
                                <SearchIcon color="action" />
                            </InputAdornment>
                        ),
                    }}
                />
            </Paper>

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