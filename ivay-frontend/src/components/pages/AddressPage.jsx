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
  Select,
  MenuItem,
  FormControl,
  InputLabel,
  IconButton,
} from "@mui/material";
import EditIcon from "@mui/icons-material/Edit";
import DeleteIcon from "@mui/icons-material/Delete";
import AddCircleOutlineIcon from "@mui/icons-material/AddCircleOutline";

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

  const [selectedAddressId, setSelectedAddressId] = useState("");
  const [editingAddressId, setEditingAddressId] = useState(null);
  const [addressForm, setAddressForm] = useState({
    name: "",
    phone: "",
    address: "",
    zipCode: "",
    province: "",
    locality: "",
  });

  const fetchCart = useCallback(async () => {
    if (!user?.id) {
      setCartItems([]);
      return;
    }
    setLoading(true);
    try {
      const itemsFromApi = await CartItemService.getCartItemsByUserId(user.id);
      const processedItems = [];
      for (const item of itemsFromApi) {
        let finalProductData = null;
        const quantity = Number(item.quantity) || 0;
        if (item.productId && (!item.product || !item.product.id)) {
          try {
            const fetchedProduct = await ProductService.getProductById(
              item.productId
            );
            if (fetchedProduct) {
              finalProductData = {
                ...fetchedProduct,
                price: parseFloat(fetchedProduct.price) || 0,
              };
            } else {
              console.warn(
                `Product with ID ${item.productId} not found for cart item ${item.id}. Item will have no product details.`
              );
            }
          } catch (productError) {
            console.error(
              `Error fetching product details for ID ${item.productId} (cart item ${item.id}):`,
              productError
            );
          }
        } else {
          if (item.product) {
            finalProductData = {
              ...item.product,
              price: parseFloat(item.product.price) || 0,
            };
          } else {
            console.warn(
              `Cart item ${item.id} has no productId and no embedded product data.`
            );
          }
        }
        processedItems.push({
          ...item,
          product: finalProductData,
          quantity: quantity,
        });
      }
      setCartItems(processedItems);
    } catch (cartServiceError) {
      console.error("Error fetching cart items:", cartServiceError);
      setCartItems([]);
    } finally {
      setLoading(false);
    }
  }, [user?.id]);

  const fetchAddresses = useCallback(async () => {
    if (!user?.id) return;
    try {
      const data = await AddressService.getAddressesByUserId(user.id);
      setAddresses(data);
      if (data.length === 0) {
        setSelectedAddressId("");
        setEditingAddressId(null);
      }
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
        (sum, item) => sum + (item.product?.price || 0) * item.quantity,
        0
      ),
    [cartItems]
  );

  const handleChange = (e) => {
    const { name, value } = e.target;
    setAddressForm((prev) => ({ ...prev, [name]: value }));
  };

  const handleSelectAddressChange = (eventOrId) => {
    const newSelectedId =
      typeof eventOrId === "string" || typeof eventOrId === "number"
        ? eventOrId
        : eventOrId.target.value;

    setSelectedAddressId(newSelectedId);
    setEditingAddressId(null);

    if (newSelectedId && newSelectedId !== "new") {
      const selectedAddr = addresses.find((addr) => addr.id === newSelectedId);
      if (selectedAddr) {
        setAddressForm((prev) => ({
          ...prev,
          name: user.name || prev.name,
          phone: user.phone || prev.phone,
          address: selectedAddr.address,
          zipCode: selectedAddr.zipCode,
          province: selectedAddr.province,
          locality: selectedAddr.locality,
        }));
      }
    } else {
      setAddressForm((prev) => ({
        ...prev,
        name: user.name || prev.name,
        phone: user.phone || prev.phone,
        address: "",
        zipCode: "",
        province: "",
        locality: "",
      }));
    }
  };

  const handleEditSelectedAddress = () => {
    if (selectedAddressId && selectedAddressId !== "new") {
      setEditingAddressId(selectedAddressId);
    }
  };

  const handleSaveOrUpdateAddress = async () => {
    const { address, zipCode, province, locality } = addressForm;
    if (!(address && zipCode && province && locality)) {
      alert("Por favor, completa todos los campos de la dirección.");
      return;
    }

    const dto = {
      userId: user.id,
      address,
      zipCode,
      province,
      locality,
    };

    try {
      if (editingAddressId && editingAddressId !== "new") {
        await AddressService.updateAddress(editingAddressId, dto);
        setEditingAddressId(null);
        await fetchAddresses();
      } else {
        const newAddress = await AddressService.createAddress(dto);
        await fetchAddresses();
        if (newAddress && newAddress.id) {
          handleSelectAddressChange(newAddress.id);
        } else {
          handleSelectAddressChange("");
        }
      }
    } catch (err) {
      console.error("Error al guardar la dirección:", err);
      alert("Error al guardar la dirección. Inténtalo de nuevo.");
    }
  };

  const handleDeleteSelectedAddress = async () => {
    if (!selectedAddressId || selectedAddressId === "new") {
      alert("No hay dirección seleccionada para eliminar.");
      return;
    }
    if (!window.confirm("¿Seguro que quieres eliminar esta dirección?")) return;

    try {
      await AddressService.deleteAddress(selectedAddressId);
      await fetchAddresses();
      handleSelectAddressChange("");
    } catch (err) {
      console.error("Error al eliminar la dirección:", err);
      alert("Error al eliminar la dirección. Inténtalo de nuevo.");
    }
  };

  const isFormForNewAddress = !selectedAddressId || selectedAddressId === "new";
  const isFormEditable = editingAddressId !== null || isFormForNewAddress;

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
        sx={{
          width: "100%",
          maxWidth: "1100px",
          justifyContent: "center",
        }}
      >
        <Grid item xs={12} md={8}>
          <Paper sx={{ p: 2, mb: 2, width: 600 }}>
            <FormControl fullWidth sx={{ mb: 2 }}>
              <InputLabel id="address-select-label">
                Selecciona una dirección
              </InputLabel>
              <Select
                labelId="address-select-label"
                id="address-select"
                value={selectedAddressId}
                label="Selecciona una dirección"
                onChange={handleSelectAddressChange}
              >
                <MenuItem value="">
                  <em>Ninguna / Crear nueva</em>
                </MenuItem>
                {addresses.map((addr) => (
                  <MenuItem key={addr.id} value={addr.id}>
                    {addr.address}, {addr.province}, {addr.locality} (
                    {addr.zipCode})
                  </MenuItem>
                ))}
              </Select>
            </FormControl>

            {selectedAddressId && selectedAddressId !== "new" && (
              <Box
                sx={{
                  display: "flex",
                  gap: 1,
                  justifyContent: "flex-end",
                  mb: 2,
                }}
              >
                <Button
                  variant="outlined"
                  size="small"
                  onClick={handleEditSelectedAddress}
                  disabled={editingAddressId === selectedAddressId}
                >
                  <EditIcon fontSize="small" sx={{ mr: 0.5 }} /> Editar
                </Button>
                <Button
                  variant="outlined"
                  size="small"
                  color="error"
                  onClick={handleDeleteSelectedAddress}
                >
                  <DeleteIcon fontSize="small" sx={{ mr: 0.5 }} /> Eliminar
                </Button>
              </Box>
            )}
          </Paper>

          <Paper
            elevation={3}
            sx={{
              p: { xs: 2, sm: 3 },
              border: `1px solid ${colors.grey[300]}`,
              borderRadius: 2,
              backgroundColor: colors.background.paper,
            }}
          >
            <Typography variant="h6" gutterBottom sx={{ mb: 2 }}>
              {editingAddressId
                ? "Editando Dirección"
                : isFormForNewAddress
                ? "Nueva Dirección"
                : "Detalles de Dirección"}
            </Typography>
            <Box
              component="form"
              sx={{
                display: "flex",
                flexDirection: "column",
                gap: 2,
              }}
            >
              <Box sx={{ display: "flex", flexDirection: "row" }}>
                <TextField
                  fullWidth
                  label="Nombre y apellidos"
                  name="name"
                  value={addressForm.name}
                  disabled
                  sx={{ paddingRight: 4 }}
                />
                <TextField
                  fullWidth
                  label="Teléfono"
                  name="phone"
                  value={addressForm.phone}
                  disabled
                />
              </Box>
              <TextField
                fullWidth
                required
                label="Dirección"
                name="address"
                value={addressForm.address}
                onChange={handleChange}
                disabled={!isFormEditable}
              />
              <Box sx={{ display: "flex", flexDirection: "row" }}>
                <TextField
                  fullWidth
                  required
                  label="Código postal"
                  name="zipCode"
                  value={addressForm.zipCode}
                  onChange={handleChange}
                  disabled={!isFormEditable}
                  sx={{ paddingRight: 4 }}
                />
                <TextField
                  fullWidth
                  required
                  label="Provincia"
                  name="province"
                  value={addressForm.province}
                  onChange={handleChange}
                  disabled={!isFormEditable}
                />
              </Box>
              <TextField
                fullWidth
                required
                label="Localidad"
                name="locality"
                value={addressForm.locality}
                onChange={handleChange}
                disabled={!isFormEditable}
              />
              {isFormEditable && (
                <Button
                  variant="contained"
                  color="primary"
                  onClick={handleSaveOrUpdateAddress}
                  disabled={
                    !addressForm.address ||
                    !addressForm.zipCode ||
                    !addressForm.province ||
                    !addressForm.locality
                  }
                  sx={{ mt: 1 }}
                >
                  {editingAddressId
                    ? "Guardar Cambios"
                    : "Crear y Usar Dirección"}
                </Button>
              )}
              {!isFormForNewAddress && !editingAddressId && (
                <Button
                  variant="outlined"
                  onClick={() => handleSelectAddressChange("")}
                  startIcon={<AddCircleOutlineIcon />}
                  sx={{ mt: 1 }}
                >
                  Añadir Otra Dirección
                </Button>
              )}
            </Box>
          </Paper>
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
              fullWidth
              variant="contained"
              size="large"
              startIcon={<PaymentIcon />}
              sx={{
                mt: 2,
                fontWeight: "bold",
                backgroundColor: colors.primary.main,
                "&:hover": { backgroundColor: colors.primary.dark },
              }}
              disabled={!selectedAddressId || selectedAddressId === "new"}
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
