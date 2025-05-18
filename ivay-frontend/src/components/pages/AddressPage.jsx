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
import AddressService from "../../service/address.service";
import ProductService from "../../service/product.service";
import { useAuth } from "../../context/AuthContext";
import { colors, layout } from "../../constants/styles";

const AddressPage = () => {
  const { user, isAuthenticated } = useAuth();
  const navigate = useNavigate();

  const [loading, setLoading] = useState(true);
  const [cartItems, setCartItems] = useState([]);
  const [addresses, setAddresses] = useState([]);

  const [selectedAddressId, setSelectedAddressId] = useState(null);
  const [editingAddressId, setEditingAddressId] = useState(null);
  const [addressForm, setAddressForm] = useState({
    name: "",
    phone: "",
    address: "",
    zipCode: "",
    province: "",
    locality: "",
  });

  // Carga carrito con precio real
  const fetchCart = useCallback(async () => {
    if (!user?.id) {
      setLoading(false);
      return;
    }
    try {
      setLoading(true);
      const items = await CartItemService.getCartItemsByUserId(user.id);
      const withProduct = await Promise.all(
        items.map(async (it) => {
          if (it.productId && (!it.product || !it.product.id)) {
            const prod = await ProductService.getProductById(it.productId);
            return {
              ...it,
              product: { ...prod, price: parseFloat(prod.price) || 0 },
              quantity: Number(it.quantity) || 0,
            };
          }
          return {
            ...it,
            product: {
              ...it.product,
              price: parseFloat(it.product.price) || 0,
            },
            quantity: Number(it.quantity) || 0,
          };
        })
      );
      setCartItems(withProduct);
    } catch (err) {
      console.error(err);
      setCartItems([]);
    } finally {
      setLoading(false);
    }
  }, [user?.id]);

  // Carga de direcciones
  const fetchAddresses = useCallback(async () => {
    if (!user?.id) return;
    try {
      const data = await AddressService.getAddressesByUserId(user.id);
      setAddresses(data);
    } catch (err) {
      console.error(err);
    }
  }, [user?.id]);

  useEffect(() => {
    if (!isAuthenticated) {
      navigate("/login", { state: { from: "/checkout/address" } });
      return;
    }
    fetchCart();
    fetchAddresses();
    setAddressForm((prev) => ({
      ...prev,
      name: user.name || "",
      phone: user.phone || "",
    }));
  }, [isAuthenticated, user, fetchCart, fetchAddresses, navigate]);

  const totalPrice = useMemo(
    () =>
      cartItems.reduce(
        (sum, it) => sum + (it.product.price || 0) * it.quantity,
        0
      ),
    [cartItems]
  );

  const handleChange = (e) => {
    const { name, value } = e.target;
    setAddressForm((prev) => ({ ...prev, [name]: value }));
  };

  const handleSelect = (addr) => {
    setSelectedAddressId(addr.id);
    setEditingAddressId(null);
    setAddressForm((prev) => ({
      ...prev,
      address: addr.address,
      zipCode: addr.zipCode,
      province: addr.province,
      locality: addr.locality,
    }));
  };

  const handleUpdateClick = async (addr) => {
    if (editingAddressId !== addr.id) {
      setSelectedAddressId(addr.id);
      setEditingAddressId(addr.id);
      setAddressForm((prev) => ({
        ...prev,
        address: addr.address,
        zipCode: addr.zipCode,
        province: addr.province,
        locality: addr.locality,
      }));
      return;
    }
    // Guardar actualización
    const dto = {
      userId: user.id,
      address: addressForm.address,
      zipCode: addressForm.zipCode,
      province: addressForm.province,
      locality: addressForm.locality,
    };
    try {
      await AddressService.updateAddress(addr.id, dto);
      setEditingAddressId(null);
      fetchAddresses();
    } catch (err) {
      console.error(err);
    }
  };

  const handleDelete = async (id) => {
    if (!window.confirm("¿Seguro que quieres eliminar esta dirección?")) return;
    try {
      await AddressService.deleteAddress(id);
      if (selectedAddressId === id) setSelectedAddressId(null);
      setEditingAddressId(null);
      fetchAddresses();
    } catch (err) {
      console.error(err);
    }
  };

  const handleCreateNew = async () => {
    const { address, zipCode, province, locality } = addressForm;
    if (!(address && zipCode && province && locality)) return;
    const dto = { userId: user.id, address, zipCode, province, locality };
    try {
      await AddressService.createAddress(dto);
      setSelectedAddressId(null);
      setEditingAddressId(null);
      setAddressForm((prev) => ({
        ...prev,
        address: "",
        zipCode: "",
        province: "",
        locality: "",
      }));
      await fetchAddresses();
    } catch (err) {
      console.error(err);
    }
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
      <Typography
        variant="h4"
        component="h1"
        gutterBottom
        sx={{ fontWeight: "bold" }}
      >
        Dirección de envío
      </Typography>

      <Grid
        container
        spacing={4}
        sx={{ width: "70%", justifyContent: "center" }}
      >
        {/* Lista con scroll horizontal */}
        <Grid item xs={12} md={6}>
          <Box sx={{ display: "flex", overflowX: "auto", gap: 1, py: 1 }}>
            {addresses.map((addr) => (
              <Paper key={addr.id} sx={{ minWidth: 250, p: 2, flexShrink: 0 }}>
                <Typography>
                  {addr.address}, {addr.locality} ({addr.zipCode})
                </Typography>
                <Box sx={{ mt: 1, display: "flex", gap: 1 }}>
                  <Button size="small" onClick={() => handleSelect(addr)}>
                    Seleccionar
                  </Button>
                  <Button
                    size="small"
                    color="warning"
                    disabled={selectedAddressId !== addr.id}
                    onClick={() => handleUpdateClick(addr)}
                  >
                    {editingAddressId === addr.id ? "Guardar" : "Actualizar"}
                  </Button>
                  <Button
                    size="small"
                    color="error"
                    onClick={() => handleDelete(addr.id)}
                  >
                    Borrar
                  </Button>
                </Box>
              </Paper>
            ))}
          </Box>
        </Grid>

        {/* Formulario centrado */}
        <Grid item xs={12} md={6}>
          <Box
            sx={{
              p: 2.5,
              border: `1px solid ${colors.grey[300]}`,
              borderRadius: 2,
              backgroundColor: colors.background.paper,
              boxShadow: "0px 3px 6px rgba(0,0,0,0.1)",
            }}
          >
            <Box
              sx={{
                display: "flex",
                flexDirection: "column",
                alignItems: "center",
                gap: 2,
              }}
            >
              <TextField
                sx={{ width: { xs: "100%", sm: 400 } }}
                label="Nombre y apellidos"
                name="name"
                value={addressForm.name}
                disabled
              />
              <TextField
                sx={{ width: { xs: "100%", sm: 400 } }}
                label="Teléfono"
                name="phone"
                value={addressForm.phone}
                disabled
              />
              <TextField
                sx={{ width: { xs: "100%", sm: 400 } }}
                required
                label="Dirección"
                name="address"
                value={addressForm.address}
                onChange={handleChange}
                disabled={
                  !(
                    editingAddressId === selectedAddressId ||
                    selectedAddressId === null
                  )
                }
              />
              <TextField
                sx={{ width: { xs: "100%", sm: 400 } }}
                required
                label="Código postal"
                name="zipCode"
                value={addressForm.zipCode}
                onChange={handleChange}
                disabled={
                  !(
                    editingAddressId === selectedAddressId ||
                    selectedAddressId === null
                  )
                }
              />
              <TextField
                sx={{ width: { xs: "100%", sm: 400 } }}
                required
                label="Provincia"
                name="province"
                value={addressForm.province}
                onChange={handleChange}
                disabled={
                  !(
                    editingAddressId === selectedAddressId ||
                    selectedAddressId === null
                  )
                }
              />
              <TextField
                sx={{ width: { xs: "100%", sm: 400 } }}
                required
                label="Localidad"
                name="locality"
                value={addressForm.locality}
                onChange={handleChange}
                disabled={
                  !(
                    editingAddressId === selectedAddressId ||
                    selectedAddressId === null
                  )
                }
              />

              <Button
                variant="outlined"
                onClick={handleCreateNew}
                disabled={
                  selectedAddressId !== null ||
                  !addressForm.address ||
                  !addressForm.zipCode ||
                  !addressForm.province ||
                  !addressForm.locality
                }
                sx={{ width: { xs: "100%", sm: 400 } }}
              >
                Crear nueva dirección
              </Button>
            </Box>
          </Box>
        </Grid>

        {/* Resumen */}
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
              disabled={!selectedAddressId}
              onClick={() =>
                navigate("/checkout/payment", {
                  state: { addressId: selectedAddressId },
                })
              }
            >
              Seleccionar y continuar
            </Button>
          </Paper>
        </Grid>
      </Grid>
    </Container>
  );
};

export default AddressPage;
