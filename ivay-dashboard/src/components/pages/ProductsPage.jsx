import React, { useEffect, useState, useCallback } from "react";
import { useNavigate } from "react-router-dom";
import {
    Box,
    Typography,
    Button,
    Grid,
    CircularProgress,
    Alert,
    TextField,
    InputAdornment,
    Paper,
    Pagination,
    IconButton,
} from "@mui/material";
import AddIcon from "@mui/icons-material/Add";
import SearchIcon from "@mui/icons-material/Search";
import ProductService from "../../service/product.service";
import { colors } from "../../constants/styles";
import ProductCard from "../common/ProductCard";
import ProductFormModal from "../common/ProductFormModal";

const PRODUCTS_PER_PAGE = 12;

/**
 * ProductsPage component displays a grid of product cards,
 * provides functionality to add new products, search existing ones by name,
 * and edit or delete products via a modal form. It handles pagination for the product list.
 */
function ProductsPage() {
    const navigate = useNavigate();
    const [products, setProducts] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");
    const [searchTerm, setSearchTerm] = useState("");
    const [page, setPage] = useState(1);
    const [totalPages, setTotalPages] = useState(0);
    const [totalProducts, setTotalProducts] = useState(0);
    const [triggerFetch, setTriggerFetch] = useState(0);

    const [isModalOpen, setIsModalOpen] = useState(false);
    const [editingProduct, setEditingProduct] = useState(null);
    const [isFormSubmitting, setIsFormSubmitting] = useState(false);
    const [formServerError, setFormServerError] = useState('');

    /**
     * Fetches products from the service. If a search term is active, it searches by name.
     * Otherwise, it fetches a paginated list. Updates product list, total pages, and total products states.
     * Handles loading state, errors, and resets page for search results if needed.
     */
    const fetchProducts = useCallback(async () => {
        setLoading(true);
        setError("");
        try {
            let productDataResponse;
            if (searchTerm.trim()) {
                const results = await ProductService.findProductsByName(searchTerm.trim());
                setProducts(results || []);
                setTotalProducts(results?.length || 0);
                setTotalPages(Math.ceil((results?.length || 0) / PRODUCTS_PER_PAGE));
                if (page !== 1 && results?.length > 0) setPage(1);
                else if (results?.length === 0) setPage(1);

            } else {
                productDataResponse = await ProductService.getProductsPaginated(page - 1, PRODUCTS_PER_PAGE);
                setProducts(productDataResponse.content || []);
                setTotalPages(productDataResponse.totalPages || 0);
                setTotalProducts(productDataResponse.totalElements || 0);
            }
        } catch (err) {
            console.error("Error fetching products:", err);
            setError("No se pudieron cargar los productos.");
            setProducts([]);
            setTotalPages(0);
            setTotalProducts(0);
        } finally {
            setLoading(false);
        }
    }, [page, searchTerm, triggerFetch]);

    /* Effect hook to fetch products whenever the fetchProducts callback changes (due to its dependencies). */
    useEffect(() => {
        fetchProducts();
    }, [fetchProducts]);

    /**
     * Handles changes in the search input field.
     *
     * @param {object} event - The input change event object.
     */
    const handleSearchChange = (event) => {
        setSearchTerm(event.target.value);
    };

    /**
     * Handles the submission of the search form.
     * Triggers a refetch of products based on the current search term, resetting the page to 1.
     *
     * @param {object} event - The form submit event object.
     */
    const handleSearchSubmit = (event) => {
        event.preventDefault();
        if (page !== 1 && !searchTerm.trim()) setPage(1);
        else if (page !== 1 && searchTerm.trim()) setPage(1);
        setTriggerFetch(c => c + 1);
    };

    /**
     * Handles the change of the current page in the pagination component.
     *
     * @param {object} event - The pagination change event object.
     * @param {number} value - The new page number (1-based).
     */
    const handlePageChange = (event, value) => {
        setPage(value);
    };

    /**
     * Opens the product form modal for creating a new product.
     * Resets editingProduct and clears server errors.
     */
    const handleOpenCreateModal = () => {
        setEditingProduct(null);
        setFormServerError('');
        setIsModalOpen(true);
    };

    /**
     * Opens the product form modal for editing an existing product.
     * Populates the modal with the provided product data and clears server errors.
     *
     * @param {object} productToEdit - The product object to pre-fill the form with.
     */
    const handleOpenEditModal = (productToEdit) => {
        setEditingProduct(productToEdit);
        setFormServerError('');
        setIsModalOpen(true);
    };

    /**
     * Closes the product form modal and resets related states (editing product 
     * and server errors).
     */
    const handleCloseModal = () => {
        setIsModalOpen(false);
        setEditingProduct(null);
        setFormServerError('');
    };

    /**
     * Handles the submission of the product form modal (create or edit).
     * Calls the appropriate product service method (create or update),
     * closes the modal on success, and triggers a refetch of the product list.
     * Sets submitting state and handles server errors.
     *
     * @param {object} productData - The product data from the form.
     * @param {number} [productId] - The ID of the product being edited.
     */
    const handleProductFormSubmit = async (productData, productId) => {
        setIsFormSubmitting(true);
        setFormServerError('');
        try {
            if (productId) {
                await ProductService.updateProduct(productId, productData);
            } else {
                await ProductService.createProduct(productData);
            }
            handleCloseModal();
            setTriggerFetch(c => c + 1);
        } catch (err) {
            console.error("Error submitting product form:", err);
            setFormServerError(err.response?.data?.message || err.message || "Error al guardar el producto.");
        } finally {
            setIsFormSubmitting(false);
        }
    };

    /**
     * Handles the deletion of a product after a confirmation prompt.
     * Calls the product service to delete the product and triggers a refetch of the product list.
     * Handles errors.
     *
     * @param {number} productId - The ID of the product to delete.
     */
    const handleDeleteProduct = async (productId) => {
        if (window.confirm("¿Estás seguro de que quieres eliminar este producto?")) {
            try {
                await ProductService.deleteProduct(productId);
                setTriggerFetch(c => c + 1);
            } catch (err) {
                console.error("Error deleting product:", err);
                setError("Error al eliminar el producto.");
            }
        }
    };

    /* Computes the subset of products to display on the current page when searching. */
    const productsToDisplay = searchTerm.trim()
        ? products.slice((page - 1) * PRODUCTS_PER_PAGE, page * PRODUCTS_PER_PAGE)
        : products;

    /* Computes the total number of pages to show in the pagination component. */
    const currentTotalPages = searchTerm.trim() ? Math.ceil(products.length / PRODUCTS_PER_PAGE) : totalPages;


    return (
        <>
            <Typography variant="h5" component="h1" gutterBottom fontWeight="bold">
                Productos / Vista de gestor
            </Typography>

            <Paper elevation={0} sx={{ p: 2, mb: 3, display: 'flex', alignItems: 'center', gap: 2, backgroundColor: 'transparent', flexWrap: 'wrap' }}>
                <Button
                    variant="contained"
                    startIcon={<AddIcon />}
                    onClick={handleOpenCreateModal}
                    sx={{ backgroundColor: colors.primary.main, '&:hover': { backgroundColor: colors.primary.dark } }}
                >
                    Nuevo
                </Button>
                <Box component="form" onSubmit={handleSearchSubmit} sx={{ flexGrow: 1, minWidth: { xs: '100%', sm: 300 }, maxWidth: { sm: 500 }, ml: { sm: 'auto' } }}>
                    <TextField
                        fullWidth
                        variant="outlined"
                        placeholder="Buscar producto..."
                        value={searchTerm}
                        onChange={handleSearchChange}
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

                        size="small"
                    />
                </Box>
            </Paper>

            {loading && (<Box sx={{ display: "flex", justifyContent: "center", py: 5 }}><CircularProgress /></Box>)}
            {error && !loading && (<Alert severity="error" sx={{ my: 2 }}>{error}</Alert>)}
            {!loading && !error && productsToDisplay.length === 0 && (
                <Typography sx={{ textAlign: "center", py: 5, color: "text.secondary" }}>
                    {searchTerm ? `No se encontraron productos para "${searchTerm}".` : "No hay productos disponibles."}
                </Typography>
            )}

            {!loading && !error && productsToDisplay.length > 0 && (
                <Grid container spacing={2.5}>
                    {productsToDisplay.map((product) => (
                        <Grid item xs={12} sm={6} md={4} lg={3} key={product.id} sx={{ display: 'flex', alignItems: 'stretch' }}>
                            <ProductCard
                                product={product}
                                onEdit={() => handleOpenEditModal(product)}
                                onDelete={() => handleDeleteProduct(product.id)}
                            />
                        </Grid>
                    ))}
                </Grid>
            )}

            {!loading && currentTotalPages > 1 && productsToDisplay.length > 0 && (
                <Box sx={{ display: 'flex', justifyContent: 'center', alignItems: 'center', mt: 4, mb: 2, p: 1 }}>
                    <Pagination
                        count={currentTotalPages}
                        page={page}
                        onChange={handlePageChange}
                        color="primary"
                    />
                </Box>
            )}
            <Box sx={{ display: 'flex', justifyContent: 'flex-end', alignItems: 'center', mt: 1, mb: 2, pr: 1 }}>
                {!loading && (searchTerm.trim() ? products.length > 0 : totalProducts > 0) && (
                    <Typography variant="body2" color="text.secondary">
                        {searchTerm.trim() ? `Mostrando ${productsToDisplay.length} de ${products.length} para "${searchTerm}"` : `Total: ${totalProducts} productos`}
                    </Typography>
                )}
            </Box>

            <ProductFormModal
                open={isModalOpen}
                onClose={handleCloseModal}
                onSubmit={handleProductFormSubmit}
                productToEdit={editingProduct}
                isSubmitting={isFormSubmitting}
                serverError={formServerError}
            />
        </>
    );
}

export default ProductsPage;