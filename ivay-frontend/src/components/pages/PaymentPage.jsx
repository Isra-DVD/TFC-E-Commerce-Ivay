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
  Checkbox,
  FormControlLabel,
  FormHelperText,
} from "@mui/material";
import CreditCardIcon from "@mui/icons-material/CreditCard";
import PayPalIcon from "@mui/icons-material/AccountBalanceWallet";
import ReceiptIcon from "@mui/icons-material/Receipt";
import PaymentIcon from "@mui/icons-material/Payment";
import CartItemService from "../../service/cartItem.service";
import ProductService from "../../service/product.service";
import { useAuth } from "../../context/AuthContext";
import { colors, layout } from "../../constants/styles";

const PaymentPage = () => {
  const { user, isAuthenticated } = useAuth();
  const navigate = useNavigate();

  const [loading, setLoading] = useState(true);
  const [cartItems, setCartItems] = useState([]);
  const [method, setMethod] = useState("debit");
  const [cardForm, setCardForm] = useState({
    cardNumber: "",
    expiryDate: "",
    cvv: "",
    cardHolder: "",
  });
  const [paypalForm, setPaypalForm] = useState({
    email: "",
    phone: "",
    acceptPolicy: false,
  });
  const [paysafeForm, setPaysafeForm] = useState({ code: "" });
  const [errors, setErrors] = useState({});

  const fetchCart = useCallback(async () => {
    if (!user?.id) {
      setCartItems([]);
      return;
    }

    setLoading(true);

    try {
      const itemsFromApi = await CartItemService.getCartItemsByUserId(user.id);
      const processedCartItems = [];

      for (const currentCartItem of itemsFromApi) {
        let productDataForCartItem = null;
        const quantityInCart = Number(currentCartItem.quantity) || 0;

        if (
          currentCartItem.productId &&
          (!currentCartItem.product || !currentCartItem.product.id)
        ) {
          try {
            const fetchedProductDetails = await ProductService.getProductById(
              currentCartItem.productId
            );

            if (fetchedProductDetails) {
              productDataForCartItem = {
                ...fetchedProductDetails,
                price: parseFloat(fetchedProductDetails.price) || 0,
              };
            } else {
              console.warn(
                `Producto con ID ${currentCartItem.productId} no encontrado para el ítem del carrito ${currentCartItem.id}. El ítem no tendrá detalles de producto.`
              );
            }
          } catch (productFetchError) {
            console.error(
              `Error al obtener detalles del producto con ID ${currentCartItem.productId} (ítem del carrito ${currentCartItem.id}):`,
              productFetchError
            );
          }
        } else {
          if (currentCartItem.product) {
            productDataForCartItem = {
              ...currentCartItem.product,
              price: parseFloat(currentCartItem.product.price) || 0,
            };
          } else {
            console.warn(
              `El ítem del carrito ${currentCartItem.id} no tiene productId ni datos de producto embebidos.`
            );
          }
        }

        processedCartItems.push({
          ...currentCartItem,
          product: productDataForCartItem,
          quantity: quantityInCart,
        });
      }
      setCartItems(processedCartItems);
    } catch (errorFetchingInitialCart) {
      console.error(
        "Error al obtener los ítems del carrito:",
        errorFetchingInitialCart
      );
      setCartItems([]);
    } finally {
      setLoading(false);
    }
  }, [user?.id]);

  useEffect(() => {
    if (!isAuthenticated) {
      navigate("/login", { state: { from: "/checkout/payment" } });
      return;
    }
    fetchCart();
  }, [isAuthenticated, fetchCart, navigate]);

  const totalPrice = useMemo(
    () =>
      cartItems.reduce(
        (sum, it) => sum + (it.product.price || 0) * it.quantity,
        0
      ),
    [cartItems]
  );

  const handleChange = (e) => {
    const { name, value, checked } = e.target;
    if (method === "debit") setCardForm((prev) => ({ ...prev, [name]: value }));
    if (method === "paypal") {
      if (name === "acceptPolicy")
        setPaypalForm((prev) => ({ ...prev, acceptPolicy: checked }));
      else setPaypalForm((prev) => ({ ...prev, [name]: value }));
    }
    if (method === "paysafe")
      setPaysafeForm((prev) => ({ ...prev, [name]: value }));

    setErrors((prev) => ({ ...prev, [name]: null }));
  };

  const handleSubmit = () => {
    const errs = {};
    let valid = true;
    if (method === "debit") {
      if (!/^[0-9]{16}$/.test(cardForm.cardNumber)) {
        errs.cardNumber = "Debe tener 16 dígitos";
        valid = false;
      }
      if (!/^(0[1-9]|1[0-2])\/\d{2}$/.test(cardForm.expiryDate)) {
        errs.expiryDate = "Formato MM/AA";
        valid = false;
      }
      if (!/^[0-9]{3,4}$/.test(cardForm.cvv)) {
        errs.cvv = "3 o 4 dígitos";
        valid = false;
      }
      if (!cardForm.cardHolder.trim()) {
        errs.cardHolder = "Requerido";
        valid = false;
      }
    }
    if (method === "paypal") {
      if (!/^[^@\s]+@[^@\s]+\.[^@\s]+$/.test(paypalForm.email)) {
        errs.email = "Email inválido";
        valid = false;
      }
      if (!/^\+?\d{7,15}$/.test(paypalForm.phone)) {
        errs.phone = "Teléfono inválido";
        valid = false;
      }
      if (!paypalForm.acceptPolicy) {
        errs.acceptPolicy = "Debes aceptar la política";
        valid = false;
      }
    }
    if (method === "paysafe") {
      if (!/^[0-9]{16}$/.test(paysafeForm.code)) {
        errs.code = "Debe tener 16 dígitos";
        valid = false;
      }
    }
    setErrors(errs);
    if (!valid) return;
    navigate("/checkout/summary", {
      state: {
        paymentMethod: method,
      },
    });
  };

  return (
    <Container
      maxWidth={layout.containerMaxWidth}
      sx={{
        py: 4,
        display: "flex",
        flexDirection: "column",
        alignItems: "center",
      }}
    >
      <Typography variant="h4" gutterBottom sx={{ fontWeight: "bold", mb: 3 }}>
        Datos de pago
      </Typography>
      <Box sx={{ width: "80%" }}>
        <Grid
          container
          spacing={4}
          justifyContent="center"
          alignItems="flex-start"
        >
          <Grid item xs={12} md={6}>
            <Box
              sx={{ mb: 3, display: "flex", justifyContent: "center", gap: 2 }}
            >
              {["debit", "paypal", "paysafe"].map((m) => {
                const labels = {
                  debit: "Tarjeta de débito",
                  paypal: "PayPal",
                  paysafe: "PaysafeCard",
                };
                const icons = {
                  debit: <CreditCardIcon fontSize="large" />,
                  paypal: <PayPalIcon fontSize="large" />,
                  paysafe: <ReceiptIcon fontSize="large" />,
                };
                return (
                  <Paper
                    key={m}
                    onClick={() => setMethod(m)}
                    sx={{
                      p: 2,
                      textAlign: "center",
                      flex: 1,
                      minWidth: 90,
                      cursor: "pointer",
                      border:
                        method === m
                          ? `2px solid ${colors.primary?.main}`
                          : "1px solid transparent",
                    }}
                  >
                    <Typography>{labels[m]}</Typography>
                    {icons[m]}
                  </Paper>
                );
              })}
            </Box>
            <Box
              sx={{
                p: 2,
                border: `1px solid ${colors.grey[300]}`,
                borderRadius: 2,
                backgroundColor: colors.background.paper,
                boxShadow: "0px 3px 6px rgba(0,0,0,0.1)",
                display: "flex",
                flexDirection: "column",
                gap: 2,
              }}
            >
              {method === "debit" && (
                <>
                  <TextField
                    fullWidth
                    required
                    label="Número de tarjeta"
                    name="cardNumber"
                    value={cardForm.cardNumber}
                    onChange={handleChange}
                    error={!!errors.cardNumber}
                    helperText={errors.cardNumber}
                    slotProps={{ maxLength: 16 }}
                  />
                  <Box sx={{ display: "flex", flexDirection: "row" }}>
                    <TextField
                      fullWidth
                      required
                      label="Fecha de vencimiento (MM/AA)"
                      name="expiryDate"
                      value={cardForm.expiryDate}
                      onChange={handleChange}
                      error={!!errors.expiryDate}
                      helperText={errors.expiryDate}
                      placeholder="MM/AA"
                      sx={{ pr: 2 }}
                    />
                    <TextField
                      fullWidth
                      required
                      label="CVV"
                      name="cvv"
                      value={cardForm.cvv}
                      onChange={handleChange}
                      error={!!errors.cvv}
                      helperText={errors.cvv}
                      slotProps={{ maxLength: 4 }}
                      sx={{ width: "35%" }}
                    />
                  </Box>
                  <TextField
                    fullWidth
                    required
                    label="Titular de la tarjeta"
                    name="cardHolder"
                    value={cardForm.cardHolder}
                    onChange={handleChange}
                    error={!!errors.cardHolder}
                    helperText={errors.cardHolder}
                  />
                </>
              )}
              {method === "paypal" && (
                <>
                  <TextField
                    fullWidth
                    required
                    label="Email PayPal"
                    name="email"
                    type="email"
                    value={paypalForm.email}
                    onChange={handleChange}
                    error={!!errors.email}
                    helperText={errors.email}
                  />
                  <TextField
                    fullWidth
                    required
                    label="Teléfono asociado"
                    name="phone"
                    type="tel"
                    value={paypalForm.phone}
                    onChange={handleChange}
                    error={!!errors.phone}
                    helperText={errors.phone}
                    placeholder="+34123456789"
                  />
                  <FormControlLabel
                    control={
                      <Checkbox
                        checked={paypalForm.acceptPolicy}
                        name="acceptPolicy"
                        onChange={handleChange}
                      />
                    }
                    label={
                      <Typography>
                        He leído y acepto la{" "}
                        <a
                          href="https://www.paypal.com/webapps/mpp/ua/privacy-full"
                          target="_blank"
                          rel="noopener noreferrer"
                        >
                          Política de Seguridad
                        </a>
                      </Typography>
                    }
                  />
                  {errors.acceptPolicy && (
                    <FormHelperText error>{errors.acceptPolicy}</FormHelperText>
                  )}
                </>
              )}
              {method === "paysafe" && (
                <TextField
                  fullWidth
                  required
                  label="Código PaysafeCard (16 dígitos)"
                  name="code"
                  value={paysafeForm.code}
                  onChange={handleChange}
                  error={!!errors.code}
                  helperText={errors.code}
                  slotProps={{ maxLength: 16 }}
                />
              )}
            </Box>
          </Grid>
          <Grid item xs={12} md={4}>
            <Paper
              sx={{
                p: 3,
                border: `1px solid ${colors.grey[300]}`,
                borderRadius: 1,
              }}
            >
              <Typography variant="h6" gutterBottom sx={{ fontWeight: "bold" }}>
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
                type="button"
                fullWidth
                variant="contained"
                size="large"
                startIcon={<PaymentIcon />}
                sx={{
                  mt: 2,
                  fontWeight: "bold",
                  backgroundColor: "#673AB7",
                  "&:hover": { backgroundColor: "#512DA8" },
                }}
                onClick={handleSubmit}
              >
                Continuar
              </Button>
            </Paper>
          </Grid>
        </Grid>
      </Box>
    </Container>
  );
};

export default PaymentPage;
