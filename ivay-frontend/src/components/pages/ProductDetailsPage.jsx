import React, { useEffect, useState } from "react";
import { useParams, Link as RouterLink, useNavigate } from "react-router-dom";
import {
  Container,
  Grid,
  Box,
  Typography,
  Button,
  CircularProgress,
  Alert,
  CardMedia,
  Divider,
  Paper,
  IconButton,
  Chip,
} from "@mui/material";
import AddShoppingCartIcon from "@mui/icons-material/AddShoppingCart";

import ProductService from "../../service/product.service";
import CartItemService from "../../service/cartItem.service";
import { useAuth } from "../../context/AuthContext";
import ProductCard from "../common/ProductCard";
import { colors, layout } from "../../constants/styles";
import placeholder from "../../assets/images/product-placeholder.png";

/**
 * Renders the details page for a specific product.
 * Displays product information, price, stock status, an add-to-cart button,
 * and a section for similar products.
 */
const ProductDetailsPage = () => {
  const { productId } = useParams();
  const { user, isAuthenticated } = useAuth();
  const navigate = useNavigate();

  const [product, setProduct] = useState(null);
  const [similarProducts, setSimilarProducts] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [cartMessage, setCartMessage] = useState({ type: "", text: "" });

  /**
   * Effect hook that fetches product details based on the productId from the
   * URL, and also fetches a list of similar products.
   */
  useEffect(() => {
    const fetchProductData = async () => {
      setLoading(true);
      setError("");
      setCartMessage({ type: "", text: "" });
      try {
        const productData = await ProductService.getProductById(productId);
        setProduct(productData);

        const allProducts = await ProductService.getProductsPaginated(0, 7);
        setSimilarProducts(
          allProducts.content.filter((p) => p.id !== productData.id).slice(0, 6)
        );
      } catch (e) {
        console.error("Error fetching product data:", e);
        setError(
          e.response?.data?.message ||
            "No se pudo cargar la información del producto."
        );
        setProduct(null);
      } finally {
        setLoading(false);
      }
    };

    if (productId) {
      fetchProductData();
    }
  }, [productId]);

  /**
   * Handles adding the current product to the user's cart.
   * Requires user authentication. Communicates with the backend service.
   */
  const handleAddToCart = async () => {
    if (!product) return;

    if (!isAuthenticated || !user || !user.id) {
      setCartMessage({
        type: "warning",
        text: "Por favor, inicia sesión para añadir al carrito.",
      });
      return;
    }

    setCartMessage({ type: "", text: "" });
    try {
      const cartItemDto = {
        userId: user.id,
        productId: product.id,
        quantity: 1,
      };
      await CartItemService.addOrUpdateCartItem(cartItemDto);
      setCartMessage({
        type: "success",
        text: "¡Producto añadido al carrito!",
      });
    } catch (e) {
      console.error("Error adding to cart:", e);
      setCartMessage({
        type: "error",
        text: e.response?.data?.message || "Error al añadir al carrito.",
      });
    }
  };

  if (loading) {
    return (
      <Container sx={{ py: 4, textAlign: "center" }}>
        <CircularProgress />
      </Container>
    );
  }

  if (error) {
    return (
      <Container sx={{ py: 4 }}>
        <Alert severity="error">{error}</Alert>
      </Container>
    );
  }

  if (!product) {
    return (
      <Container sx={{ py: 4 }}>
        <Alert severity="warning">Producto no encontrado.</Alert>
      </Container>
    );
  }

  const discountedPrice =
    product.discount > 0
      ? product.price * (1 - product.discount)
      : product.price;

  return (
    <Container
      maxWidth={layout.containerMaxWidth}
      sx={{ py: { xs: 2, md: 4 } }}
    >
      {/* Main Product Info Box */}
      <Paper
        elevation={3}
        sx={{
          p: { xs: 2, md: 3 },
          mb: 4,
          border: `1px solid ${colors.grey[300]}`,
          borderRadius: 2,
          overflow: "hidden",
          backgroundColor: colors.background.paper,
        }}
      >
        <Grid
          container
          spacing={{ xs: 2, md: 4 }}
          sx={{ justifyContent: "center" }}
        >
          {/* Product Image - Left Column */}
          <Grid
            item
            xs={12}
            md={5}
            sx={{
              display: "flex",
              alignItems: "center",
              justifyContent: "center",
              borderRight: { md: `1px solid ${colors.grey[300]}` },
              pr: { md: 12 },
            }}
          >
            <CardMedia
              component="img"
              image={product.imageUrl || placeholder}
              alt={product.name}
              sx={{
                width: "100%",
                maxWidth: 400,
                maxHeight: { xs: 300, md: 450 },
                objectFit: "contain",
              }}
            />
          </Grid>

          {/* Product Details - Right Column */}
          <Grid
            item
            xs={12}
            md={7}
            sx={{
              pl: { md: 11 },
              display: "flex",
              flexDirection: "column",
              alignItems: "center",
              textAlign: "center",
            }}
          >
            <Typography
              variant="h4"
              component="h1"
              gutterBottom
              fontWeight="bold"
            >
              {product.name}
            </Typography>

            {/* Description */}
            <Paper
              variant="outlined"
              sx={{
                p: 2,
                my: 2,
                backgroundColor: "rgba(245, 245, 245, 0.8)",
                border: `1px solid ${colors.grey[300]}`,
                borderRadius: 1,
              }}
            >
              <Typography variant="body1" color="text.secondary">
                {product.description ||
                  "Descripción detallada del producto no disponible."}
              </Typography>
            </Paper>

            {/* Price and Discount */}
            <Box sx={{ my: 3 }}>
              {product.discount > 0 && (
                <Typography
                  variant="h6"
                  color="text.secondary"
                  sx={{
                    textDecoration: "line-through",
                    display: "inline-block",
                    mr: 1.5,
                    fontSize: "1.1rem",
                  }}
                >
                  PVPR: €{product.price.toFixed(2)}
                </Typography>
              )}
              <Typography
                variant="h4"
                component="p"
                color="primary.main"
                fontWeight="bold"
                sx={{ display: "inline-block" }}
              >
                Precio: €{discountedPrice.toFixed(2)}
              </Typography>
              {product.discount > 0 && (
                <Chip
                  label={`-${Math.round(product.discount * 100)}%`}
                  size="small"
                  sx={{
                    ml: 2,
                    fontWeight: "bold",
                    fontSize: "0.8rem",
                    color: colors.primary.contrastText,
                    backgroundColor: colors.primary.light,
                  }}
                />
              )}
            </Box>

            {/* Stock Status */}
            <Typography
              variant="body2"
              sx={{
                color: product.stock > 0 ? "green" : "red",
                mb: 2,
                fontWeight: "medium",
              }}
            >
              {product.stock > 0
                ? `En stock (${product.stock} unidades)`
                : "Agotado temporalmente"}
            </Typography>

            {/* Add to Cart Button */}
            <Button
              variant="contained"
              startIcon={<AddShoppingCartIcon />}
              onClick={handleAddToCart}
              disabled={!product || product.stock === 0}
              size="large"
              sx={{
                mt: 2,
                mb: 1,
                fontWeight: "bold",
                backgroundColor: "#673AB7",
                "&:hover": {
                  backgroundColor: "#512DA8",
                },
                px: 3,
              }}
            >
              Añadir al carrito
            </Button>
            {/* Cart Action Message */}
            {cartMessage.text && (
              <Alert severity={cartMessage.type} sx={{ mt: 2, maxWidth: 400 }}>
                {cartMessage.text}
              </Alert>
            )}
          </Grid>
        </Grid>
      </Paper>

      {/* Similar Products Section (remains outside the main product box) */}
      {similarProducts.length > 0 && (
        <Box sx={{ mt: { xs: 4, md: 6 } }}>
          <Typography
            variant="h5"
            component="h2"
            fontWeight="bold"
            gutterBottom
          >
            Productos similares
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

export default ProductDetailsPage;
