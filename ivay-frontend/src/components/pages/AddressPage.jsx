// src/pages/AddressPage.jsx
import React, { useEffect, useState, useCallback, useMemo } from "react";
import { useNavigate } from "react-router-dom";
import {
  Container,
  Grid,
  Box,
  Typography,
  Button,
  Paper,
  Divider,
  TextField,
} from "@mui/material";
import ShoppingCartIcon from "@mui/icons-material/ShoppingCart";
import CartItemService from "../../service/cartItem.service";
import { useAuth } from "../../context/AuthContext";
import { colors, layout } from "../../constants/styles";

const AddressPage = () => {
  const { user, isAuthenticated } = useAuth();
  const navigate = useNavigate();

  const [loading, setLoading] = useState(true);
  const [cartItems, setCartItems] = useState([]);
  const [form, setForm] = useState({
    name: "",
    phone: "",
    address: "",
    postalCode: "",
    province: "",
    city: "",
  });

  const fetchCart = useCallback(async () => {
    if (!user?.id) return;
    try {
      const items = await CartItemService.getCartItemsByUserId(user.id);
      setCartItems(items);
    } catch {
      setCartItems([]);
    } finally {
      setLoading(false);
    }
  }, [user]);

  useEffect(() => {
    if (!isAuthenticated) {
      navigate("/login", { state: { from: "/checkout/address" } });
      return;
    }
    fetchCart();
  }, [isAuthenticated, fetchCart, navigate]);

  const totalPrice = useMemo(
    () =>
      cartItems.reduce(
        (sum, it) => sum + (it.product?.price || 0) * (it.quantity || 0),
        0
      ),
    [cartItems]
  );

  const handleChange = (e) => {
    const { name, value } = e.target;
    setForm((prev) => ({ ...prev, [name]: value }));
  };

  const handleSubmit = () => {
    console.log("Enviar dirección:", form);
    navigate("/checkout/payment");
  };

  return (
    <Container
      maxWidth={layout.containerMaxWidth}
      sx={{
        py: { xs: 4, md: 4 },
        display: "flex",
        flexDirection: "column",
        alignItems: "center",
      }}
    >
      <Typography
        variant="h4"
        component="h1"
        gutterBottom
        sx={{ textAlign: "center", mb: 3, fontWeight: "bold" }}
      >
        Dirección de envío
      </Typography>

      <Grid container spacing={4}>
        {/* Left Column: Address Form */}
        <Grid item xs={12} md={8}>
          <Paper
            sx={{
              p: { xs: 2, md: 4 },
              border: `1px solid ${colors.grey[300]}`,
              borderRadius: 2,
            }}
          >
            <Grid container spacing={2}>
              <Grid item xs={12} sm={6}>
                <TextField
                  fullWidth
                  label="Nombre"
                  name="name"
                  value={form.name}
                  onChange={handleChange}
                />
              </Grid>
              <Grid item xs={12} sm={6}>
                <TextField
                  fullWidth
                  label="Teléfono"
                  name="phone"
                  value={form.phone}
                  onChange={handleChange}
                />
              </Grid>

              <Grid item xs={12}>
                <TextField
                  fullWidth
                  label="Dirección"
                  name="address"
                  value={form.address}
                  onChange={handleChange}
                />
              </Grid>

              <Grid item xs={12} sm={4}>
                <TextField
                  fullWidth
                  label="Código postal"
                  name="postalCode"
                  value={form.postalCode}
                  onChange={handleChange}
                />
              </Grid>
              <Grid item xs={12} sm={4}>
                <TextField
                  fullWidth
                  label="Provincia"
                  name="province"
                  value={form.province}
                  onChange={handleChange}
                />
              </Grid>
              <Grid item xs={12} sm={4}>
                <TextField
                  fullWidth
                  label="Localidad"
                  name="city"
                  value={form.city}
                  onChange={handleChange}
                />
              </Grid>
            </Grid>
          </Paper>
        </Grid>

        {/* Right Column: Summary */}
        <Grid item xs={12} md={4}>
          <Paper
            sx={{
              p: { xs: 2, md: 3 },
              border: `1px solid ${colors.grey[300]}`,
              borderRadius: 2,
            }}
          >
            <Typography
              variant="h6"
              component="h2"
              gutterBottom
              sx={{ fontWeight: "bold" }}
            >
              Resumen
            </Typography>
            <Divider sx={{ my: 2 }} />

            <Box
              sx={{ display: "flex", justifyContent: "space-between", mb: 2 }}
            >
              <Typography variant="body1" fontWeight="medium">
                Total (Impuestos incluidos)
              </Typography>
              <Typography variant="h6" fontWeight="bold" color="primary.main">
                €{totalPrice.toFixed(2)}
              </Typography>
            </Box>

            <Button
              fullWidth
              variant="contained"
              size="large"
              onClick={handleSubmit}
              startIcon={<ShoppingCartIcon />}
              sx={{
                mt: 2,
                fontWeight: "bold",
                backgroundColor: colors.primary.light,
                "&:hover": { backgroundColor: colors.primary.main },
              }}
            >
              Guardar y continuar
            </Button>
          </Paper>
        </Grid>
      </Grid>
    </Container>
  );
};

export default AddressPage;
