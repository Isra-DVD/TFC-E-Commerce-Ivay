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
import PaymentIcon from "@mui/icons-material/Payment";
import CartItemService from "../../service/cartItem.service";
import { useAuth } from "../../context/AuthContext";
import { colors, layout } from "../../constants/styles";

const AddressPage = () => {
  const { user, isAuthenticated } = useAuth();
  const navigate = useNavigate();

  // eslint-disable-next-line no-unused-vars
  const [loading, setLoading] = useState(true);
  const [cartItems, setCartItems] = useState([]);
  const [addressForm, setaddressForm] = useState({
    name: "",
    phone: "",
    address: "",
    zipCode: "",
    province: "",
    locality: "",
  });

  const fetchCart = useCallback(async () => {
    if (!user?.id) {
      setLoading(false);
      return;
    }
    try {
      setLoading(true);
      const items = await CartItemService.getCartItemsByUserId(user.id);
      const processedItems = items.map((item) => ({
        ...item,
        product: {
          ...item.product,
          price: parseFloat(item.product?.price) || 0,
        },
        quantity: parseInt(item.quantity, 10) || 0,
      }));
      setCartItems(processedItems);
    } catch (err) {
      console.error("Error fetching cart:", err);
      setCartItems([]);
    } finally {
      setLoading(false);
    }
  }, [user?.id]);

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
    setaddressForm((prev) => ({ ...prev, [name]: value }));
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    console.log("Enviar dirección:", addressForm);
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
        sx={{
          textAlign: "center",
          mb: 3,
          fontWeight: "bold",
        }}
      >
        Dirección de envío
      </Typography>

      <Grid
        container
        spacing={4}
        component="addressForm"
        onSubmit={handleSubmit}
        sx={{
          width: "70%",
          justifyContent: "center",
        }}
      >
        {/* --- COLUMNA IZQUIERDA: addressFormULARIO DE DIRECCIÓN --- */}

        <Grid item xs={12} md={6}>
          <Box
            sx={{
              p: { xs: 2, sm: 3 },
              border: `1px solid ${colors.grey?.[300] || "#e0e0e0"}`,
              borderRadius: 2,
              backgroundColor: colors.background?.paper || "#FFFFFF",
              boxShadow: "0px 3px 6px rgba(0,0,0,0.1)",
            }}
          >
            <Grid container spacing={2.5}>
              <Grid item xs={12} sm={6}>
                <TextField
                  fullWidth
                  required
                  label="Nombre y apellidos"
                  name="name"
                  value={addressForm.name}
                  onChange={handleChange}
                  variant="outlined"
                />
              </Grid>
              <Grid item xs={12} sm={6}>
                <TextField
                  fullWidth
                  required
                  label="Teléfono"
                  name="phone"
                  type="tel"
                  value={addressForm.phone}
                  onChange={handleChange}
                  variant="outlined"
                />
              </Grid>
              <Grid item xs={12}>
                <TextField
                  fullWidth
                  required
                  label="Dirección (calle, número, piso, etc.)"
                  name="address"
                  value={addressForm.address}
                  onChange={handleChange}
                  variant="outlined"
                />
              </Grid>
              <Grid item xs={12} sm={6}>
                <TextField
                  fullWidth
                  required
                  label="Código postal"
                  name="zipCode"
                  value={addressForm.zipCode}
                  onChange={handleChange}
                  variant="outlined"
                />
              </Grid>
              <Grid item xs={12} sm={6}>
                <TextField
                  fullWidth
                  required
                  label="Provincia"
                  name="province"
                  value={addressForm.province}
                  onChange={handleChange}
                  variant="outlined"
                />
              </Grid>
              <Grid item xs={12} sm={6}>
                <TextField
                  fullWidth
                  required
                  label="Localidad"
                  name="locality"
                  value={addressForm.locality}
                  onChange={handleChange}
                  variant="outlined"
                />
              </Grid>
            </Grid>
          </Box>
        </Grid>

        {/* --- COLUMNA DERECHA: RESUMEN DEL PEDIDO Y BOTÓN --- */}
        {/* md={4} hace que esta columna ocupe 4/12 del espacio */}
        {/* Total md={10} (6+4), por lo que justifyContent: "center" en el padre las centrará */}
        <Grid item xs={12} md={4}>
          <Paper
            sx={{
              p: 3,
              border: `1px solid ${colors.grey[300]}`,
              borderRadius: 1,
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
              sx={{
                display: "flex",
                justifyContent: "space-between",
                mb: 2,
              }}
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
              onClick={() => navigate("/checkout/address")}
              startIcon={<PaymentIcon />}
              sx={{
                mt: 2,
                fontWeight: "bold",
                backgroundColor: "#673AB7",
                "&:hover": {
                  backgroundColor: "#512DA8",
                },
              }}
              disabled={cartItems.length === 0}
            >
              Realizar pedido
            </Button>
          </Paper>
        </Grid>
      </Grid>
    </Container>
  );
};

export default AddressPage;
