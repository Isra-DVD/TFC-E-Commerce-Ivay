import React, { useEffect, useState } from 'react';
import { useSearchParams, Link as RouterLink } from 'react-router-dom';
import {
    Container,
    Typography,
    CircularProgress,
    Alert,
    Grid,
    Box,
    Paper,
    List,
    ListItem,
    ListItemText,
    ListItemIcon,
    Divider,
} from '@mui/material';
import SearchIcon from '@mui/icons-material/Search';
import CategoryIcon from '@mui/icons-material/Category';

import ProductService from '../../service/product.service';
import CategoryService from '../../service/category.service';
import ProductCard from '../common/ProductCard';
import { layout } from '../../constants/styles';

/**
 * Renders the search results page based on a query parameter.
 * Displays lists of matching products and categories.
 */
const SearchResultsPage = () => {
    const [searchParams] = useSearchParams();
    const query = searchParams.get('q');

    const [products, setProducts] = useState([]);
    const [categories, setCategories] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState('');

    /* Effect hook that fetches search results for products and categories
     based on the 'query' parameter. Handles loading and error states. */
    useEffect(() => {
        if (!query) {
            setLoading(false);
            setProducts([]);
            setCategories([]);
            return;
        }

        const fetchData = async () => {
            setLoading(true);
            setError('');
            setProducts([]);
            setCategories([]);
            let productsError = false;
            let categoriesError = false;

            try {
                const fetchedProducts = await ProductService.findProductsByName(query);
                setProducts(fetchedProducts || []);
            } catch (err) {
                console.error("Error fetching products for search:", err);
                productsError = true;
            }

            try {
                const fetchedCategories = await CategoryService.findCategoriesByName(query);
                setCategories(fetchedCategories || []);
            } catch (err) {
                console.error("Error fetching categories for search:", err);
                categoriesError = true;
            }

            if (productsError && categoriesError) {
                setError(`No se pudieron cargar los resultados para "${query}".`);
            } else if (productsError) {
                setError(`No se pudieron cargar los productos para "${query}". Las categorías se cargaron.`);
            } else if (categoriesError) {
                setError(`No se pudieron cargar las categorías para "${query}". Los productos se cargaron.`);
            }

            setLoading(false);
        };

        fetchData();
    }, [query]);

    if (loading) {
        return (
            <Container sx={{ py: 5, textAlign: 'center' }}>
                <CircularProgress />
                <Typography sx={{ mt: 2 }}>Buscando resultados para "{query}"...</Typography>
            </Container>
        );
    }

    return (
        <Container maxWidth={layout.containerMaxWidth} sx={{ py: 3 }}>
            <Typography variant="h4" component="h1" gutterBottom>
                Resultados de búsqueda para: <Typography component="span" variant="h4" color="primary.main">"{query}"</Typography>
            </Typography>

            {error && <Alert severity="warning" sx={{ my: 2 }}>{error}</Alert>}

            {(!products || products.length === 0) && (!categories || categories.length === 0) && !loading && !error && (
                <Typography sx={{ textAlign: 'center', py: 5, color: 'text.secondary' }}>
                    No se encontraron resultados para tu búsqueda. Intenta con otros términos.
                </Typography>
            )}

            {/* Categories Section */}
            {categories && categories.length > 0 && (
                <Box sx={{ mb: 4 }}>
                    <Typography variant="h5" component="h2" gutterBottom sx={{ mt: 3 }}>
                        Categorías Encontradas
                    </Typography>
                    <Paper elevation={1}>
                        <List dense>
                            {categories.map((category, index) => (
                                <React.Fragment key={category.id}>
                                    <ListItem
                                        button="true"
                                        component={RouterLink}
                                        to={`/products?categoryId=${category.id}`}
                                    >
                                        <ListItemIcon>
                                            <CategoryIcon />
                                        </ListItemIcon>
                                        <ListItemText primary={category.name} />
                                    </ListItem>
                                    {index < categories.length - 1 && <Divider component="li" />}
                                </React.Fragment>
                            ))}
                        </List>
                    </Paper>
                </Box>
            )}

            {/* Products Section */}
            {products && products.length > 0 && (
                <Box>
                    <Typography variant="h5" component="h2" gutterBottom sx={{ mt: 3 }}>
                        Productos Encontrados
                    </Typography>
                    <Grid container spacing={{ xs: 1, sm: 2, md: 3 }}>
                        {products.map(product => (
                            <Grid item xs={6} sm={4} md={3} key={product.id}>
                                <ProductCard product={product} />
                            </Grid>
                        ))}
                    </Grid>
                </Box>
            )}
        </Container>
    );
};

export default SearchResultsPage;