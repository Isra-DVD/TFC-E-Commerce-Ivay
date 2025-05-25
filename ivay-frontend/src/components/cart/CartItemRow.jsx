import React from "react";
import {
    Box,
    Grid,
    Typography,
    IconButton,
    TextField,
    Link as MuiLink,
    CardMedia,
} from "@mui/material";
import { Link as RouterLink } from "react-router-dom";
import AddCircleOutlineIcon from "@mui/icons-material/AddCircleOutline";
import RemoveCircleOutlineIcon from "@mui/icons-material/RemoveCircleOutline";
import DeleteOutlineIcon from "@mui/icons-material/DeleteOutline";
import { colors } from "../../constants/styles";
import placeholder from "../../assets/images/product-placeholder.png"

/**
 * Renders a single row for an item in the shopping cart,
 * displaying product details, quantity controls, and a remove option.
 */
const CartItemRow = ({ item, onQuantityChange, onRemoveItem }) => {
    const product = item.product;

    if (!product) {
        return (
            <Typography color="error" sx={{ my: 2, p: 2, border: `1px solid ${colors.error?.main || 'red'}`, borderRadius: 1 }}>
                Error: Datos del producto no disponibles para un artículo del carrito (ID Artículo: {item.id}, ID Producto: {item.productId || 'N/A'}).
            </Typography>
        );
    }

    /**
     * Handles updating the quantity of the item, validating the new quantity.
     */
    const handleQuantityUpdate = (newQuantity) => {
        const quantity = parseInt(newQuantity, 10);
        if (!isNaN(quantity) && quantity >= 1 && quantity <= (product.stock || 99)) {
            onQuantityChange(item.id, quantity);
        } else if (!isNaN(quantity) && quantity < 1) {
            onQuantityChange(item.id, 1);
        }
    };

    const itemOriginalSubtotal = product.price * item.quantity;
    const itemSubtotal = product.discount > 0
        ? (product.price * (1 - product.discount)) * item.quantity
        : null;

    return (
        <Grid
            container
            spacing={2}
            alignItems="center"
            justifyContent={"space-between"}
            sx={{
                p: 2,
                border: `1px solid ${colors.grey[300]}`,
                borderRadius: 1,
                mb: 2,
                backgroundColor: colors.background.paper,
            }}
        >
            {/* Product Image */}
            <Grid item xs={12} sm={2} md={1.5}>
                <MuiLink component={RouterLink} to={`/products/${product.id}`}>
                    <CardMedia
                        component="img"
                        image={product.imageUrl || placeholder}
                        alt={product.name}
                        sx={{
                            width: "100%",
                            maxWidth: 80,
                            height: 80,
                            objectFit: "contain",
                            borderRadius: 1,
                            border: `1px solid ${colors.grey[300]}`,
                        }}
                    />
                </MuiLink>
            </Grid>

            {/* Product Name & Price */}
            <Grid item xs={12} sm={5} md={5.5}>
                <MuiLink
                    component={RouterLink}
                    to={`/products/${product.id}`}
                    variant="subtitle1"
                    fontWeight="medium"
                    color="text.primary"
                    sx={{ textDecoration: "none", "&:hover": { textDecoration: "underline" } }}
                >
                    {product.name}
                </MuiLink>
                <Box>
                    <Typography variant="h6" color="primary.main" component="span">
                        €{itemSubtotal.toFixed(2)}
                    </Typography>
                    {itemOriginalSubtotal && (
                        <Typography
                            variant="body2"
                            color="text.secondary"
                            component="span"
                            sx={{ textDecoration: "line-through", ml: 1 }}
                        >
                            €{itemOriginalSubtotal.toFixed(2)}
                        </Typography>
                    )}
                </Box>
            </Grid>

            {/* Quantity Control */}
            <Grid item xs={8} sm={3} md={3} sx={{ display: "flex", alignItems: "center" }}>
                <IconButton
                    size="small"
                    onClick={() => handleQuantityUpdate(item.quantity - 1)}
                    disabled={item.quantity <= 1}
                    aria-label="decrease quantity"
                >
                    <RemoveCircleOutlineIcon />
                </IconButton>
                <TextField
                    type="number"
                    value={item.quantity}
                    onChange={(e) => handleQuantityUpdate(e.target.value)}
                    inputProps={{
                        min: 1,
                        max: product.stock || 99,
                        style: { textAlign: "center", width: "40px", padding: "8px" },
                        "aria-label": "quantity",
                    }}
                    size="small"
                    sx={{ mx: 0.5 }}
                />
                <IconButton
                    size="small"
                    onClick={() => handleQuantityUpdate(item.quantity + 1)}
                    disabled={item.quantity >= (product.stock || 99)}
                    aria-label="increase quantity"
                >
                    <AddCircleOutlineIcon />
                </IconButton>
            </Grid>

            {/* Remove Button */}
            <Grid item xs={4} sm={2} md={2} sx={{ textAlign: "right" }}>
                <IconButton
                    onClick={() => onRemoveItem(item.id)}
                    color="error"
                    aria-label="remove item"
                >
                    <DeleteOutlineIcon />
                </IconButton>
            </Grid>
        </Grid>
    );
};

export default CartItemRow;