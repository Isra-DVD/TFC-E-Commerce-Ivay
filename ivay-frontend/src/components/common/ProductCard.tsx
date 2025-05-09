import React from "react";
import {
  Card,
  CardMedia,
  CardContent,
  Typography,
  Box,
  Link,
} from "@mui/material";
import { Link as RouterLink } from "react-router-dom";
import { colors } from "../../constants/styles";

const ProductCard = ({ product }) => {
  const discountPercentage =
    product.discount > 0 ? Math.round(product.discount * 100) : 0;

  return (
    <Card
      sx={{
        height: "100%",
        border: "1px solid #e0e0e0",
        boxShadow: "none",
        display: "flex",
        flexDirection: "column",
        position: "relative",
      }}
    >
      {/* Discount Badge */}
      {discountPercentage > 0 && (
        <Box
          sx={{
            position: "absolute",
            top: 8,
            left: 8,
            backgroundColor: colors.primary.main,
            color: "white",
            p: "2px 6px",
            borderRadius: 1,
            zIndex: 1,
            fontSize: "0.7rem",
            fontWeight: "bold",
          }}
        >
          -{discountPercentage}%
        </Box>
      )}

      {/* Product Image Link */}
      <Link
        component={RouterLink}
        to={`/products/${product.id}`}
        sx={{ textDecoration: "none", color: "inherit" }}
      >
        <CardMedia
          component="img"
          sx={{
            height: 140,
            objectFit: "contain",
            p: 1,
            bgcolor: "#fff",
          }}
          image={product.imageUrl || "/placeholder-product.png"}
          alt={product.name}
        />
      </Link>

      {/* Card Content */}
      <CardContent
        sx={{
          flexGrow: 1,
          display: "flex",
          flexDirection: "column",
          justifyContent: "space-between",
          p: 1,
          pt: 1.5,
        }}
      >
        {/* Product Name */}
        <Typography
          gutterBottom
          variant="body2"
          component="div"
          title={product.name}
          sx={{
            overflow: "hidden",
            textOverflow: "ellipsis",
            display: "-webkit-box",
            WebkitLineClamp: 2,
            WebkitBoxOrient: "vertical",
            minHeight: "2.5em",
            mb: 1,
            color: "text.primary",
            "&:hover": {},
          }}
        >
          <Link
            component={RouterLink}
            to={`/products/${product.id}`}
            sx={{ textDecoration: "none", color: "inherit" }}
          >
            {product.name}
          </Link>
        </Typography>

        {/* Price */}
        <Typography variant="h6" color="primary.main" sx={{ mt: "auto" }}>
          €{product.price.toFixed(2)}
        </Typography>
      </CardContent>
    </Card>
  );
};

export default ProductCard;
