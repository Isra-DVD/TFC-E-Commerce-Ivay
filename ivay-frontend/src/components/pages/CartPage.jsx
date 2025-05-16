import React, { useEffect, useState, useMemo, useCallback } from "react";
import { useNavigate, Link as RouterLink } from "react-router-dom";
import {
    Container,
    Grid,
    Box,
    Typography,
    Button,
    CircularProgress,
    Alert,
    Paper,
    Divider,
    Stepper,
    Step,
    StepLabel,
} from "@mui/material";
import ShoppingCartIcon from "@mui/icons-material/ShoppingCart";
import DeleteSweepIcon from "@mui/icons-material/DeleteSweep";
import StorefrontIcon from "@mui/icons-material/Storefront";
import PaymentIcon from "@mui/icons-material/Payment";

import CartItemService from "../../service/cartItem.service";
import ProductService from "../../service/product.service";
import { useAuth } from "../../context/AuthContext";
import ProductCard from "../common/ProductCard";
import CartItemRow from "../cart/CartItemRow";
import { colors, layout } from "../../constants/styles";

const CartPage = () => {
    const { user, isAuthenticated } = useAuth();
    const navigate = useNavigate();

    const [cartItems, setCartItems] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");
    const [actionMessage, setActionMessage] = useState({ type: "", text: "" });
    const [similarProducts, setSimilarProducts] = useState([]);
    const [activeStep, setActiveStep] = useState(0);

    const fetchCartData = useCallback(async () => {
        if (!user?.id) {
            setCartItems([]);
            return;
        }

        setLoading(true);
        setError("");
        try {
            const itemsFromApi = await CartItemService.getCartItemsByUserId(user.id);

            if (itemsFromApi && itemsFromApi.length > 0) {
                const itemsWithProductDetails = await Promise.all(
                    itemsFromApi.map(async (item) => {
                        if (item.productId && (!item.product || !item.product.id)) {
                            try {
                                const productDetails = await ProductService.getProductById(item.productId);
                                return { ...item, product: productDetails };
                            } catch (prodError) {
                                console.error(`Error fetching product details for ID ${item.productId}:`, prodError);
                                return { ...item, product: null };
                            }
                        }
                        return item;
                    })
                );
                setCartItems(itemsWithProductDetails);
            } else {
                setCartItems([]);
            }
        } catch (e) {
            console.error("Error fetching cart items or their products:", e);
            setError(e.response?.data?.message || "No se pudo cargar el carrito.");
            setCartItems([]);
        } finally {
            setLoading(false);
        }
    }, [user?.id]);

    useEffect(() => {
        if (!isAuthenticated) {
            navigate("/login", { state: { from: { pathname: "/cart" } } });
            return;
        }

        if (user?.id) {
            fetchCartData();
        }
        ProductService.getProductsPaginated(0, 6)
            .then((data) => setSimilarProducts(data.content || []))
            .catch((e) => console.error("Error fetching similar products:", e));

    }, [isAuthenticated, user?.id, navigate, fetchCartData]);

    const handleQuantityChange = async (cartItemId, newQuantity) => {
        setActionMessage({ type: "", text: "" });
        try {
            const updatedItemFromApi = await CartItemService.updateCartItemQuantity(
                cartItemId,
                { quantity: newQuantity }
            );
            const originalItem = cartItems.find(item => item.id === cartItemId);

            if (originalItem && originalItem.product) {
                const finalUpdatedItem = {
                    ...updatedItemFromApi,
                    product: originalItem.product
                };
                setCartItems((prevItems) =>
                    prevItems.map((item) =>
                        item.id === cartItemId ? finalUpdatedItem : item
                    )
                );
            } else {
                console.warn("Original item or product details not found for quantity update. Re-fetching cart.");
                fetchCartData();
                setActionMessage({ type: "warning", text: "Cantidad actualizada. Refrescando carrito..." });
            }
        } catch (e) {
            console.error("Error updating quantity:", e);
            setActionMessage({
                type: "error",
                text: e.response?.data?.message || "Error al actualizar la cantidad.",
            });
        }
    };

    const handleRemoveItem = async (cartItemId) => {
        setActionMessage({ type: "", text: "" });
        try {
            await CartItemService.deleteCartItem(cartItemId);
            setCartItems((prevItems) =>
                prevItems.filter((item) => item.id !== cartItemId)
            );
        } catch (e) {
            console.error("Error removing item:", e);
            setActionMessage({
                type: "error",
                text: "Error al eliminar el producto.",
            });
        }
    };

    const handleClearCart = async () => {
        if (!user?.id) return;
        setActionMessage({ type: "", text: "" });
        try {
            await CartItemService.clearUserCart(user.id);
            setCartItems([]);
            setActionMessage({ type: "success", text: "Carrito vaciado con éxito." });
        } catch (e) {
            console.error("Error clearing cart:", e);
            setActionMessage({ type: "error", text: "Error al vaciar el carrito." });
        }
    };


    const totalPrice = useMemo(() => {
        return cartItems.reduce((total, item) => {
            if (item && item.product && typeof item.product.price === 'number' && typeof item.quantity === 'number') {
                return total + (Number(item.product.price) * item.quantity);
            }
            return total;
        }, 0);
    }, [cartItems]);


    if (loading && cartItems.length === 0 && !error) {
        return (
            <Container sx={{ py: 4, textAlign: "center" }}>
                <CircularProgress />
            </Container>
        );
    }

    return (
        <Container maxWidth={layout.containerMaxWidth} sx={{
            py: { xs: 4, md: 4 }, display: 'flex',
            flexDirection: 'column',
            alignItems: 'center'
        }}>
            <Typography variant="h4" component="h1" gutterBottom sx={{ textAlign: "center", mb: 3, fontWeight: "bold" }}>
                <ShoppingCartIcon sx={{ mr: 1, verticalAlign: "middle" }} /> Mi cesta
            </Typography>

            {error && (
                <Alert severity="error" sx={{ mb: 2 }}>
                    {error}
                </Alert>
            )}
            {actionMessage.text && !error && (
                <Alert severity={actionMessage.type || "info"} sx={{ mb: 2 }}>
                    {actionMessage.text}
                </Alert>
            )}

            {!loading && !error && cartItems.length === 0 ? (
                <Paper sx={{ p: 3, textAlign: "center" }}>
                    <Typography variant="h6" gutterBottom>
                        Tu cesta está vacía.
                    </Typography>
                    <Button
                        variant="contained"
                        color="primary"
                        component={RouterLink}
                        to="/"
                        startIcon={<StorefrontIcon />}
                        sx={{ mt: 2 }}
                    >
                        Seguir comprando
                    </Button>
                </Paper>
            ) : !error && cartItems.length > 0 ? (
                <Grid container spacing={4}>
                    {/* Left Column: Cart Items & Actions */}
                    <Grid item xs={12} md={8}>
                        <Box
                            sx={{
                                ...(cartItems.length > 4 && {
                                    maxHeight: '500px',
                                    overflowY: 'auto',
                                    pr: 1,
                                    '&::-webkit-scrollbar': {
                                        width: '8px',
                                    },
                                    '&::-webkit-scrollbar-track': {
                                        backgroundColor: colors.grey[200] || '#f1f1f1',
                                        borderRadius: '4px',
                                    },
                                    '&::-webkit-scrollbar-thumb': {
                                        backgroundColor: colors.grey[400] || '#888',
                                        borderRadius: '4px',
                                    },
                                    '&::-webkit-scrollbar-thumb:hover': {
                                        backgroundColor: colors.grey[600] || '#555',
                                    },
                                }),
                            }}
                        >
                            {cartItems.map((item) => (
                                <CartItemRow
                                    key={item.id}
                                    item={item}
                                    onQuantityChange={handleQuantityChange}
                                    onRemoveItem={handleRemoveItem}
                                />
                            ))}
                        </Box>
                        <Box
                            sx={{
                                mt: 3,
                                display: "flex",
                                justifyContent: "space-between",
                                flexWrap: "wrap",
                                gap: 2
                            }}
                        >
                            <Button
                                variant="outlined"
                                onClick={handleClearCart}
                                startIcon={<DeleteSweepIcon />}
                                sx={{
                                    borderColor: colors.secondary.main,
                                    color: colors.secondary.main,
                                    '&:hover': {
                                        borderColor: colors.secondary.dark,
                                        backgroundColor: 'rgba(245, 127, 23, 0.08)'
                                    }
                                }}
                            >
                                Vaciar cesta
                            </Button>
                            <Button
                                variant="contained"
                                component={RouterLink}
                                to="/"
                                startIcon={<StorefrontIcon />}
                                sx={{ backgroundColor: "#4CAF50", '&:hover': { backgroundColor: "#388E3C" } }}
                            >
                                Seguir comprando
                            </Button>
                        </Box>
                    </Grid>

                    {/* Right Column: Summary */}
                    <Grid item xs={12} md={4}>
                        <Paper sx={{ p: 3, border: `1px solid ${colors.grey[300]}`, borderRadius: 1 }}>
                            <Typography variant="h6" component="h2" gutterBottom sx={{ fontWeight: "bold" }}>
                                Resumen
                            </Typography>
                            <Divider sx={{ my: 2 }} />
                            <Box
                                sx={{
                                    display: "flex",
                                    justifyContent: "space-between",
                                    mb: 2,
                                }}
                            >
                                <Typography variant="body1" fontWeight="medium">Total (Impuestos incluidos)</Typography>
                                <Typography variant="h6" fontWeight="bold" color="primary.main">
                                    €{totalPrice.toFixed(2)}
                                </Typography>
                            </Box>
                            <Button
                                fullWidth
                                variant="contained"
                                size="large"
                                onClick={console.log("ha pagado")}
                                startIcon={<PaymentIcon />}
                                sx={{
                                    mt: 2,
                                    fontWeight: "bold",
                                    backgroundColor: "#673AB7",
                                    '&:hover': {
                                        backgroundColor: "#512DA8",
                                    }
                                }}
                                disabled={cartItems.length === 0}
                            >
                                Realizar pedido
                            </Button>
                        </Paper>
                    </Grid>
                </Grid>
            ) : null}


            {/* Similar Products Section (can remain as is) */}
            {similarProducts.length > 0 && (
                <Box sx={{ mt: 6 }}>
                    <Typography variant="h5" component="h2" fontWeight="bold" gutterBottom>
                        Otros usuarios también compraron
                    </Typography>
                    <Divider sx={{ mb: 3 }} />
                    <Grid container spacing={{ xs: 1, sm: 2, md: 3 }}>
                        {similarProducts.map((p) => (
                            <Grid item xs={6} sm={4} md={2} key={`similar-${p.id}`}>
                                <ProductCard product={p} />
                            </Grid>
                        ))}
                    </Grid>
                </Box>
            )}
        </Container>
    );
};

export default CartPage;