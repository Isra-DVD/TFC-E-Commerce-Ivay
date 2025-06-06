import React, { useEffect, useState, useCallback, useMemo } from "react";
import { useNavigate, useLocation } from "react-router-dom";
import {
  Container,
  Box,
  Typography,
  Paper,
  Divider,
  Button,
  Alert,
} from "@mui/material";
import CartItemService from "../../service/cartItem.service";
import ProductService from "../../service/product.service";
import OrderService from "../../service/order.service";
import { useAuth } from "../../context/AuthContext";
import { colors, layout } from "../../constants/styles";

/**
 * Renders the checkout summary page, displaying the cart items,
 * subtotal, shipping cost, and total. Allows the user to confirm
 * the purchase and finalize the order.
 */
const SummaryPage = () => {
  const { user, isAuthenticated } = useAuth();
  const navigate = useNavigate();
  const location = useLocation();
  const paymentMethod = location.state?.paymentMethod || "unknown";

  const [cartItems, setCartItems] = useState([]);
  const [errorMsg, setErrorMsg] = useState("");

  /**
   * Fetches the current user's cart items, including detailed product data if missing.
   */
  const fetchCart = useCallback(async () => {
    if (!user?.id) {
      setCartItems([]);
      return;
    }

    try {
      const itemsFromApi = await CartItemService.getCartItemsByUserId(user.id);
      const processedItems = [];

      for (const item of itemsFromApi) {
        let productDetails = null;

        if (item.productId && (!item.product || !item.product.id)) {
          try {
            const fetchedProduct = await ProductService.getProductById(
              item.productId
            );
            if (fetchedProduct) {
              productDetails = {
                ...fetchedProduct,
                price:
                  (parseFloat(fetchedProduct.price) || 0) *
                  (1 - fetchedProduct.discount),
              };
            }
          } catch (productError) {
            console.error(
              `Error fetching product ${item.productId} for cart item ${item.id}:`,
              productError
            );
          }
        } else if (item.product) {
          productDetails = {
            ...item.product,
            price:
              (parseFloat(item.product.price) || 0) *
              (1 - item.product.discount),
          };
        }

        processedItems.push({
          ...item,
          product: productDetails,
        });
      }
      setCartItems(processedItems);
    } catch (err) {
      console.error("Error fetching cart for summary:", err);
      setCartItems([]);
    }
  }, [user?.id]);

  /* Effect hook to fetch the user's cart data when the component mounts
   or when authentication status or fetchCart changes. */
  useEffect(() => {
    if (isAuthenticated) fetchCart();
  }, [isAuthenticated, fetchCart]);

  /* Calculates the subtotal price of all items currently in the cart
   (price * quantity for each item). */
  const subtotal = useMemo(
    () =>
      cartItems.reduce(
        (sum, item) => sum + (item.product.price || 0) * (item.quantity || 1),
        0
      ),
    [cartItems]
  );
  const shipping = useMemo(
    () => Math.floor(Math.random() * (50 - 15 + 1)) + 15,
    []
  );
  const total = useMemo(() => subtotal + shipping, [subtotal, shipping]);

  /**
   * Handles the confirmation of the order.
   * Creates a new order via the OrderService, clears the user's cart,
   * and navigates to the home page upon success. Handles errors.
   */
  const handleConfirm = async () => {
    setErrorMsg("");
    try {
      if (!user?.id) throw new Error("Usuario no autenticado");
      const createOrderDto = {
        userId: user.id,
        paymentMethod: paymentMethod,
        globalDiscount: 0,
        items: cartItems.map((item) => ({
          productId: item.product.id,
          quantity: item.quantity,
        })),
      };
      await OrderService.createOrder(createOrderDto);
      await CartItemService.clearUserCart(user.id);
      navigate("/");
    } catch (err) {
      console.error("Error clearing cart:", err);
      setErrorMsg(
        err.response?.data?.message || "Error al confirmar la compra"
      );
    }
  };

  return (
    <Container
      maxWidth={layout.containerMaxWidth}
      sx={{
        display: "flex",
        justifyContent: "center",
        alignItems: "flex-start",
        mt: 4,
        mb: 4,
      }}
    >
      <Paper
        sx={{
          width: "100%",
          maxWidth: 600,
          p: 4,
          borderRadius: 2,
          boxShadow: "0px 4px 12px rgba(0,0,0,0.1)",
        }}
      >
        <Typography
          variant="h5"
          align="center"
          gutterBottom
          sx={{ fontWeight: "bold" }}
        >
          Resumen de la compra
        </Typography>

        {/* Productos */}
        <Box sx={{ display: "flex", flexDirection: "column", gap: 1, mt: 2 }}>
          {cartItems.map((item) => (
            <Box
              key={item.id}
              sx={{ display: "flex", justifyContent: "space-between" }}
            >
              <Typography>{item.product.name}</Typography>
              <Typography sx={{ color: colors.primary.light }}>
                €{(item.product.price * (item.quantity || 1)).toFixed(2)}
              </Typography>
            </Box>
          ))}
        </Box>

        <Divider sx={{ my: 2 }} />

        {/* Subtotal */}
        <Box sx={{ display: "flex", justifyContent: "space-between", mb: 1 }}>
          <Typography>Subtotal</Typography>
          <Typography sx={{ color: colors.primary.light }}>
            €{subtotal.toFixed(2)}
          </Typography>
        </Box>

        {/* Envío */}
        <Box sx={{ display: "flex", justifyContent: "space-between", mb: 2 }}>
          <Typography>Envío</Typography>
          <Typography sx={{ color: colors.primary.light }}>
            €{shipping.toFixed(2)}
          </Typography>
        </Box>

        <Divider sx={{ my: 2 }} />

        {/* Total */}
        <Box sx={{ display: "flex", justifyContent: "space-between", mb: 3 }}>
          <Typography variant="h6" sx={{ fontWeight: "bold" }}>
            Total
          </Typography>
          <Typography
            variant="h6"
            sx={{ fontWeight: "bold", color: colors.primary.light }}
          >
            €{total.toFixed(2)}
          </Typography>
        </Box>

        {errorMsg && (
          <Alert severity="error" sx={{ mb: 2 }}>
            {errorMsg}
          </Alert>
        )}

        <Button
          fullWidth
          variant="contained"
          onClick={handleConfirm}
          sx={{ fontWeight: "bold" }}
        >
          Confirmar compra y volver al menú
        </Button>
      </Paper>
    </Container>
  );
};

export default SummaryPage;
