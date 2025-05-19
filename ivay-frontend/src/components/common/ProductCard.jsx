// src/components/common/ProductCard.tsx
import React from "react";
import {
  Card,
  CardMedia,
  CardContent,
  Typography,
  Box,
  Link as MuiLink,
} from "@mui/material";
import { Link as RouterLink } from "react-router-dom";
import { colors } from "../../constants/styles";

const ProductCard = ({ product }) => {
  const discountPercentage =
    product.discount > 0 ? Math.round(product.discount * 100) : 0;

  const currentPrice = product.price.toFixed(2);
  let originalPrice = null;
  if (product.discount > 0 && product.price > 0) {
    originalPrice = (product.price / (1 - product.discount)).toFixed(2);
  }

  return (
    <Card
      sx={{
        width: { xs: "100%", sm: 100, md: 170 },
        height: { xs: "auto", sm: 300, md: 250 },
        display: "flex",
        flexDirection: "column",
        border: `1px solid ${colors.grey[300]}`,
        boxShadow: "none",
        transition: "transform 0.2s, box-shadow 0.2s",
        position: "relative",
        "&:hover": {
          transform: "translateY(-4px)",
          boxShadow: (theme) => theme.shadows[4],
        },
      }}
    >
      {discountPercentage > 0 && (
        <Box
          sx={{
            position: "absolute",
            top: 8,
            left: 8,
            bgcolor: colors.primary.light,
            color: colors.primary.contrastText,
            p: "2px 6px",
            borderRadius: 1,
            fontSize: "0.75rem",
            fontWeight: "bold",
            zIndex: 2,
          }}
        >
          -{discountPercentage}%
        </Box>
      )}

      <MuiLink
        component={RouterLink}
        to={`/products/${product.id}`}
        sx={{
          textDecoration: "none",
          color: "inherit",
          height: { xs: 140, sm: 180, md: 200 },
          display: "flex",
          alignItems: "center",
          justifyContent: "center",
          p: 1,
          overflow: "hidden",
          bgcolor: "#fff",
        }}
      >
        <CardMedia
          component="img"
          image={product.imageUrl || "/intel-product.jpg"}
          alt={product.name}
          sx={{
            maxHeight: "100%",
            maxWidth: "100%",
            objectFit: "contain",
          }}
        />
      </MuiLink>

      <CardContent
        sx={{
          flexGrow: 1,
          p: 1.5,
          pt: 1,
          display: "flex",
          flexDirection: "column",
          justifyContent: "space-between",
        }}
      >
        <Typography
          variant="body2"
          title={product.name}
          sx={{
            mb: 1,
            fontWeight: 500,
            height: { xs: "2.4em", sm: "2.8em" },
            lineHeight: 1.4,
            overflow: "hidden",
            textOverflow: "ellipsis",
            display: "-webkit-box",
            WebkitLineClamp: 2,
            WebkitBoxOrient: "vertical",
            textAlign: "center",
          }}
        >
          <MuiLink
            component={RouterLink}
            to={`/products/${product.id}`}
            sx={{
              textDecoration: "none",
              color: "inherit",
              "&:hover": { color: colors.primary.main },
            }}
          >
            {product.name}
          </MuiLink>
        </Typography>

        <Box sx={{ textAlign: "center", mt: "auto" }}>
          <Typography

            component="p"
            color={colors.primary.light}
            sx={{ fontWeight: "bold", lineHeight: 1.2 }}
          >
            €{currentPrice}
          </Typography>
          {originalPrice && (
            <Typography
              variant="caption"
              color="text.secondary"
              sx={{ textDecoration: "line-through", ml: 0.5 }}
            >
              PVPR €{originalPrice}
            </Typography>
          )}
        </Box>
      </CardContent>
    </Card>
  );
};

export default ProductCard;