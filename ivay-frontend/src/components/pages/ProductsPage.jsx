import React, { useEffect, useState, useMemo } from 'react';
import { useParams, useSearchParams, useLocation } from 'react-router-dom';
import {
    Container,
    Typography,
    CircularProgress,
    Alert,
    Grid,
    Box,
    Pagination,
    FormControl,
    InputLabel,
    Select,
    MenuItem,
    Breadcrumbs,
    Link as MuiLink,
    Paper
} from '@mui/material';
import NavigateNextIcon from '@mui/icons-material/NavigateNext';
import HomeIcon from '@mui/icons-material/Home';
import { Link as RouterLink } from 'react-router-dom';

import ProductService from '../../service/product.service';
import CategoryService from '../../service/category.service';
import ProductCard from '../common/ProductCard';
import { layout } from '../../constants/styles';

const PRODUCTS_PER_PAGE = 12;

const ProductsPage = () => {
    const { categoryIdFromParams } = useParams();

    const [searchParams, setSearchParams] = useSearchParams();
    const categoryIdFromQuery = searchParams.get('categoryId');

    const effectiveCategoryId = categoryIdFromParams || categoryIdFromQuery;

    const location = useLocation();

    const [products, setProducts] = useState([]);
    const [category, setCategory] = useState(null);
    const [page, setPage] = useState(1);
    const [totalPages, setTotalPages] = useState(0);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState('');
    const [sortBy, setSortBy] = useState('default');

    useEffect(() => {
        const fetchProductsAndCategory = async () => {
            setLoading(true);
            setError('');
            setCategory(null);
            try {
                let fetchedProductsData;
                if (effectiveCategoryId) {
                    try {
                        const catData = await CategoryService.getCategoryById(effectiveCategoryId);
                        setCategory(catData);
                    } catch (catError) {
                        console.warn(`Could not fetch details for category ID/slug: ${effectiveCategoryId}`, catError);
                    }
                    const allCategoryProducts = await CategoryService.getProductsByCategoryId(effectiveCategoryId);
                    fetchedProductsData = { content: allCategoryProducts, totalPages: Math.ceil(allCategoryProducts.length / PRODUCTS_PER_PAGE), totalElements: allCategoryProducts.length };

                } else {
                    fetchedProductsData = await ProductService.getProductsPaginated(page - 1, PRODUCTS_PER_PAGE);
                }

                setProducts(fetchedProductsData.content || []);
                setTotalPages(fetchedProductsData.totalPages || 0);
            } catch (err) {
                console.error("Error fetching products:", err);
                setError(err.response?.data?.message || 'No se pudieron cargar los productos.');
                setProducts([]);
                setTotalPages(0);
            } finally {
                setLoading(false);
            }
        };

        fetchProductsAndCategory();
    }, [effectiveCategoryId, page, sortBy]);

    const handlePageChange = (event, value) => {
        setPage(value);
        window.scrollTo(0, 0);
    };

    const handleSortChange = (event) => {
        setSortBy(event.target.value);
        setPage(1);
    };

    const processedProducts = useMemo(() => {
        let sortedProducts = [...products];

        switch (sortBy) {
            case 'price_asc':
                sortedProducts.sort((a, b) => a.price - b.price);
                break;
            case 'price_desc':
                sortedProducts.sort((a, b) => b.price - a.price);
                break;
            case 'name_asc':
                sortedProducts.sort((a, b) => a.name.localeCompare(b.name));
                break;
            default:
                break;
        }

        if (effectiveCategoryId && products.length > 0) {
            const startIndex = (page - 1) * PRODUCTS_PER_PAGE;
            return sortedProducts.slice(startIndex, startIndex + PRODUCTS_PER_PAGE);
        }
        return sortedProducts;
    }, [products, sortBy, page, effectiveCategoryId]);


    const pageTitle = category ? `Categoría: ${category.name}` : "Todos los productos";

    return (
        <Container maxWidth={layout.containerMaxWidth} sx={{ py: 3 }}>
            <Breadcrumbs aria-label="breadcrumb" sx={{ mb: 2 }}>
                <MuiLink component={RouterLink} underline="hover" color="inherit" to="/">
                    <HomeIcon sx={{ mr: 0.5 }} fontSize="inherit" />
                    Inicio
                </MuiLink>
                {category ?
                    [ // Return an array of elements
                        <MuiLink
                            key="products-link" // Add keys when rendering arrays of elements
                            component={RouterLink}
                            underline="hover"
                            color="inherit"
                            to="/products"
                        >
                            Productos
                        </MuiLink>,
                        <Typography key="category-name" color="text.primary"> {/* Add keys */}
                            {category.name}
                        </Typography>
                    ]
                    : (
                        <Typography color="text.primary">Todos los productos</Typography>
                    )}
            </Breadcrumbs>

            <Paper elevation={0} sx={{ p: 2, mb: 3, display: 'flex', justifyContent: 'space-between', alignItems: 'center', backgroundColor: 'transparent' }}>
                <Typography variant="h4" component="h1" gutterBottom sx={{ mb: 0 }}>
                    {pageTitle}
                </Typography>
                {/* Sorting Dropdown */}
                {products.length > 0 && (
                    <FormControl size="small" sx={{ minWidth: 180 }}>
                        <InputLabel id="sort-by-label">Ordenar por</InputLabel>
                        <Select
                            labelId="sort-by-label"
                            id="sort-by-select"
                            value={sortBy}
                            label="Ordenar por"
                            onChange={handleSortChange}
                        >
                            <MenuItem value="default">Relevancia</MenuItem>
                            <MenuItem value="price_asc">Precio: Menor a Mayor</MenuItem>
                            <MenuItem value="price_desc">Precio: Mayor a Menor</MenuItem>
                            <MenuItem value="name_asc">Nombre: A-Z</MenuItem>
                        </Select>
                    </FormControl>
                )}
            </Paper>


            {loading ? (
                <Box sx={{ display: 'flex', justifyContent: 'center', py: 5 }}><CircularProgress /></Box>
            ) : error ? (
                <Alert severity="error" sx={{ my: 3 }}>{error}</Alert>
            ) : processedProducts.length > 0 ? (
                <>
                    <Grid container spacing={{ xs: 1, sm: 2, md: 3 }}>
                        {processedProducts.map(product => (
                            <Grid item xs={6} sm={4} md={3} key={product.id}>
                                <ProductCard product={product} />
                            </Grid>
                        ))}
                    </Grid>
                    {totalPages > 1 && (
                        <Box sx={{ display: 'flex', justifyContent: 'center', mt: 4, mb: 2 }}>
                            <Pagination
                                count={totalPages}
                                page={page}
                                onChange={handlePageChange}
                                color="primary"
                                size={layout.isMobile ? "small" : "medium"}
                            />
                        </Box>
                    )}
                </>
            ) : (
                <Typography sx={{ textAlign: 'center', py: 5, color: 'text.secondary' }}>
                    No hay productos disponibles que coincidan con tu selección.
                </Typography>
            )}
        </Container>
    );
};

export default ProductsPage;