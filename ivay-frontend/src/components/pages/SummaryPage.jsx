import React, { useEffect, useState, useCallback, useMemo } from "react";
import { useNavigate } from "react-router-dom";
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
import { useAuth } from "../../context/AuthContext";
import { colors, layout } from "../../constants/styles";

const SummaryPage = () => {
  const { user, isAuthenticated } = useAuth();
  const navigate = useNavigate();
  const [cartItems, setCartItems] = useState([]);
  const [errorMsg, setErrorMsg] = useState("");

  const fetchCart = useCallback(async () => {
    if (!user?.id) return;
    try {
      const items = await CartItemService.getCartItemsByUserId(user.id);
      const withProduct = await Promise.all(
        items.map(async (it) => {
          if (it.productId && (!it.product || !it.product.id)) {
            const prod = await ProductService.getProductById(it.productId);
            return {
              ...it,
              product: { ...prod, price: parseFloat(prod.price) || 0 },
            };
          }
          return {
            ...it,
            product: {
              ...it.product,
              price: parseFloat(it.product.price) || 0,
            },
          };
        })
      );
      setCartItems(withProduct);
    } catch (err) {
      console.error("Error fetching cart for summary:", err);
      setCartItems([]);
    }
  }, [user?.id]);

  useEffect(() => {
    if (isAuthenticated) fetchCart();
  }, [isAuthenticated, fetchCart]);

  const subtotal = useMemo(
    () =>
      cartItems.reduce(
        (sum, it) => sum + (it.product.price || 0) * (it.quantity || 1),
        0
      ),
    [cartItems]
  );
  const shipping = useMemo(
    () => Math.floor(Math.random() * (50 - 15 + 1)) + 15,
    []
  );
  const total = useMemo(() => subtotal + shipping, [subtotal, shipping]);

  const handleConfirm = async () => {
    setErrorMsg("");
    try {
      if (!user?.id) throw new Error("Usuario no autenticado");
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
