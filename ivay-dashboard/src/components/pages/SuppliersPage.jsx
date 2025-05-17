import React, { useEffect, useState, useCallback } from 'react';
import {
    Box, Typography, Button, Paper, TextField, InputAdornment, IconButton,
    Table, TableBody, TableCell, TableContainer, TableHead, TableRow, TablePagination,
    CircularProgress, Alert, Menu, MenuItem, ListItemIcon, ListItemText,
} from '@mui/material';
import AddIcon from '@mui/icons-material/Add';
import SearchIcon from '@mui/icons-material/Search';
import PersonOutlineIcon from '@mui/icons-material/PersonOutline';
import MoreVertIcon from '@mui/icons-material/MoreVert';
import EditIcon from '@mui/icons-material/Edit';
import DeleteIcon from '@mui/icons-material/Delete';
import SupplierService from '../../service/supplier.service';
import { colors } from '../../constants/styles';
import SupplierFormModal from '../common/SupplierFormModal';

const ROWS_PER_PAGE_OPTIONS = [10, 20, 50];

function SuppliersPage() {
    const [suppliers, setSuppliers] = useState([]);
    const [allSuppliersCache, setAllSuppliersCache] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState('');
    const [searchTerm, setSearchTerm] = useState('');

    const [page, setPage] = useState(0);
    const [rowsPerPage, setRowsPerPage] = useState(ROWS_PER_PAGE_OPTIONS[1]);

    const [isModalOpen, setIsModalOpen] = useState(false);
    const [editingSupplier, setEditingSupplier] = useState(null);
    const [isFormSubmitting, setIsFormSubmitting] = useState(false);
    const [formServerError, setFormServerError] = useState('');

    const [anchorEl, setAnchorEl] = useState(null);
    const [currentSupplierMenu, setCurrentSupplierMenu] = useState(null);

    const handleOpenMenu = (event, supplier) => {
        setAnchorEl(event.currentTarget);
        setCurrentSupplierMenu(supplier);
    };
    const handleCloseMenu = () => {
        setAnchorEl(null);
        setCurrentSupplierMenu(null);
    };

    const fetchSuppliers = useCallback(async () => {
        setLoading(true);
        setError('');
        try {
            const allFetchedSuppliers = await SupplierService.getAllSuppliers();
            setAllSuppliersCache(allFetchedSuppliers || []);
            setSuppliers(allFetchedSuppliers || []);
        } catch (err) {
            console.error("Error fetching suppliers:", err);
            setError('No se pudieron cargar los proveedores.');
            setAllSuppliersCache([]);
            setSuppliers([]);
        } finally {
            setLoading(false);
        }
    }, []);

    useEffect(() => {
        fetchSuppliers();
    }, [fetchSuppliers]);

    useEffect(() => {
        if (searchTerm.trim()) {
            const filtered = allSuppliersCache.filter(sup =>
                sup.name.toLowerCase().includes(searchTerm.trim().toLowerCase()) ||
                (sup.email && sup.email.toLowerCase().includes(searchTerm.trim().toLowerCase())) ||
                (sup.phone && sup.phone.includes(searchTerm.trim())) ||
                (sup.address && sup.address.toLowerCase().includes(searchTerm.trim().toLowerCase()))
            );
            setSuppliers(filtered);
        } else {
            setSuppliers(allSuppliersCache);
        }
        setPage(0);
    }, [searchTerm, allSuppliersCache]);


    const handleSearchChange = (event) => {
        setSearchTerm(event.target.value);
    };

    const handleChangePage = (event, newPage) => {
        setPage(newPage);
    };
    const handleChangeRowsPerPage = (event) => {
        setRowsPerPage(parseInt(event.target.value, 10));
        setPage(0);
    };

    const handleOpenCreateModal = () => {
        setEditingSupplier(null);
        setFormServerError('');
        setIsModalOpen(true);
    };
    const handleOpenEditModal = () => {
        if (currentSupplierMenu) {
            setEditingSupplier(currentSupplierMenu);
            setFormServerError('');
            setIsModalOpen(true);
        }
        handleCloseMenu();
    };
    const handleCloseModal = () => {
        setIsModalOpen(false);
        setEditingSupplier(null);
        setFormServerError('');
    };

    const handleSupplierFormSubmit = async (supplierData, supplierId) => {
        setIsFormSubmitting(true);
        setFormServerError('');
        try {
            if (supplierId) {
                await SupplierService.updateSupplier(supplierId, supplierData);
            } else {
                await SupplierService.createSupplier(supplierData);
            }
            handleCloseModal();
            fetchSuppliers();
        } catch (err) {
            console.error("Error submitting supplier form:", err);
            setFormServerError(err.response?.data?.message || err.message || "Error al guardar el proveedor.");
        } finally {
            setIsFormSubmitting(false);
        }
    };

    const handleDeleteSupplier = async () => {
        if (currentSupplierMenu && window.confirm(`¿Estás seguro de que quieres eliminar el proveedor "${currentSupplierMenu.name}"?`)) {
            setError('');
            try {
                await SupplierService.deleteSupplier(currentSupplierMenu.id);
                fetchSuppliers();
            } catch (err) {
                console.error("Error deleting supplier:", err);
                setError("Error al eliminar el proveedor. Verifique si está asociado a productos.");
            }
        }
        handleCloseMenu();
    };

    const paginatedSuppliers = suppliers.slice(page * rowsPerPage, page * rowsPerPage + rowsPerPage);

    return (
        <>
            <Typography variant="h5" component="h1" gutterBottom fontWeight="bold">
                Proveedores / Vista de gestor
            </Typography>

            <Paper elevation={0} sx={{ p: 2, mb: 3, display: 'flex', alignItems: 'center', gap: 1.5, backgroundColor: 'transparent', flexWrap: 'wrap' }}>
                <Button
                    variant="contained"
                    startIcon={<AddIcon />}
                    onClick={handleOpenCreateModal}
                    sx={{ backgroundColor: colors.primary.main, '&:hover': { backgroundColor: colors.primary.dark } }}
                >
                    Nuevo
                </Button>
                <TextField
                    variant="outlined"
                    placeholder="Buscar proveedor..."
                    value={searchTerm}
                    onChange={handleSearchChange}
                    size="small"
                    sx={{ width: { xs: '100%', sm: 500 }, ml: { sm: 'auto' }, mt: { xs: 1.5, sm: 0 } }}
                    slotProps={{
                        input: {
                            endAdornment: (
                                <InputAdornment position="end">
                                    <IconButton type="submit" aria-label="search" edge="end">
                                        <SearchIcon />
                                    </IconButton>
                                </InputAdornment>
                            ),
                        }
                    }}
                />
            </Paper>

            {loading && (<Box sx={{ display: "flex", justifyContent: "center", py: 5 }}><CircularProgress /></Box>)}
            {error && !loading && (<Alert severity="error" sx={{ my: 2 }}>{error}</Alert>)}

            {!loading && !error && (
                <Paper sx={{ width: '100%', overflow: 'hidden' }}>
                    <TableContainer sx={{ maxHeight: { xs: 'calc(100vh - 320px)', md: 'calc(100vh - 300px)' } }}>
                        <Table stickyHeader aria-label="tabla de proveedores">
                            <TableHead>
                                <TableRow>
                                    <TableCell sx={{ width: '60px', borderBottomColor: colors.grey[400], pl: { xs: 1, sm: 2 } }}></TableCell>
                                    <TableCell sx={{ fontWeight: 'bold', borderBottomColor: colors.grey[400] }}>Nombre de proveedor</TableCell>
                                    <TableCell sx={{ fontWeight: 'bold', borderBottomColor: colors.grey[400], display: { xs: 'none', md: 'table-cell' } }}>Dirección de proveedor</TableCell>
                                    <TableCell sx={{ fontWeight: 'bold', borderBottomColor: colors.grey[400], display: { xs: 'none', sm: 'table-cell' } }}>Email de proveedor</TableCell>
                                    <TableCell sx={{ fontWeight: 'bold', borderBottomColor: colors.grey[400], display: { xs: 'none', sm: 'table-cell' } }}>Teléfono de proveedor</TableCell>
                                    <TableCell align="right" sx={{ width: '80px', borderBottomColor: colors.grey[400], pr: { xs: 1, sm: 2 } }}>Acciones</TableCell>
                                </TableRow>
                            </TableHead>
                            <TableBody>
                                {paginatedSuppliers.length === 0 && (
                                    <TableRow>
                                        <TableCell colSpan={6} align="center" sx={{ py: 3 }}>
                                            <Typography color="text.secondary">
                                                {searchTerm ? `No se encontraron proveedores para "${searchTerm}".` : "No hay proveedores disponibles."}
                                            </Typography>
                                        </TableCell>
                                    </TableRow>
                                )}
                                {paginatedSuppliers.map((supplier) => (
                                    <TableRow hover role="checkbox" tabIndex={-1} key={supplier.id}>
                                        <TableCell sx={{ borderBottomColor: colors.grey[300], pl: { xs: 1, sm: 2 } }}>
                                            <PersonOutlineIcon sx={{ color: colors.primary.light, verticalAlign: 'middle' }} />
                                        </TableCell>
                                        <TableCell sx={{ borderBottomColor: colors.grey[300] }}>{supplier.name}</TableCell>
                                        <TableCell sx={{ borderBottomColor: colors.grey[300], display: { xs: 'none', md: 'table-cell' } }}>{supplier.address || '-'}</TableCell>
                                        <TableCell sx={{ borderBottomColor: colors.grey[300], display: { xs: 'none', sm: 'table-cell' } }}>{supplier.email || '-'}</TableCell>
                                        <TableCell sx={{ borderBottomColor: colors.grey[300], display: { xs: 'none', sm: 'table-cell' } }}>{supplier.phone || '-'}</TableCell>
                                        <TableCell align="right" sx={{ borderBottomColor: colors.grey[300], pr: { xs: 1, sm: 2 } }}>
                                            <IconButton aria-label="actions" onClick={(event) => handleOpenMenu(event, supplier)}>
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
                        count={suppliers.length}
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
                <MenuItem onClick={handleOpenEditModal}><ListItemIcon><EditIcon fontSize="small" /></ListItemIcon><ListItemText>Editar</ListItemText></MenuItem>
                <MenuItem onClick={handleDeleteSupplier} sx={{ color: colors.error.main }}><ListItemIcon><DeleteIcon fontSize="small" sx={{ color: colors.error.main }} /></ListItemIcon><ListItemText>Eliminar</ListItemText></MenuItem>
            </Menu>

            {isModalOpen && (
                <SupplierFormModal
                    open={isModalOpen}
                    onClose={handleCloseModal}
                    onSubmit={handleSupplierFormSubmit}
                    supplierToEdit={editingSupplier}
                    isSubmitting={isFormSubmitting}
                    serverError={formServerError}
                />
            )}
        </>
    );
}

export default SuppliersPage;