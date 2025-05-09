import React from "react";
import {
  Box,
  Container,
  Typography,
  Button,
  Grid,
  Card,
  CardMedia,
  CardContent,
  Link,
} from "@mui/material";
import { Link as RouterLink } from "react-router-dom";
import ArrowForwardIcon from "@mui/icons-material/ArrowForward";
import AddCircleOutlineIcon from "@mui/icons-material/AddCircleOutline";
import { layout } from "../../constants/styles";
import ProductCard from "../common/ProductCard";

const FeatureIcon = ({ feature }) => (
  <Box sx={{ textAlign: "center", p: 1 }}>
    <img
      src={feature.iconUrl}
      alt={feature.title}
      style={{ height: "48px", marginBottom: "8px" }}
    />
    <Typography variant="caption" display="block">
      {feature.title}
    </Typography>
  </Box>
);

const topProducts = [
  {
    id: 1,
    name: "Intel Core i5 Processor",
    price: 199.99,
    discount: 0.1,
    imageUrl: "/intel-product.jpg",
  },
  {
    id: 2,
    name: "High-Speed RAM Module",
    price: 75.5,
    discount: 0,
    imageUrl: "/intel-product.jpg",
  },
  {
    id: 3,
    name: "Gaming Motherboard XYZ",
    price: 250.0,
    discount: 0.05,
    imageUrl: "/intel-product.jpg",
  },
  {
    id: 4,
    name: "SSD NVMe 1TB Drive",
    price: 120.0,
    discount: 0,
    imageUrl: "/intel-product.jpg",
  },
  {
    id: 5,
    name: "Generic Product Name",
    price: 99.99,
    discount: 0,
    imageUrl: "/intel-product.jpg",
  },
  {
    id: 6,
    name: "Another Intel Product",
    price: 300.0,
    discount: 0.15,
    imageUrl: "/intel-product.jpg",
  },
];
const sinIvaProducts = [...topProducts].reverse();
const promoCategories = [
  {
    title: "Los mejores portátiles",
    image: "/category/promo-laptop.jpg",
    path: "/category",
  },
  {
    title: "Monitores en tendencia",
    image: "/category/promo-monitor.jpg",
    path: "/category",
  },
  {
    title: "Estrena smartphone",
    image: "/category/promo-components.jpg",
    path: "/category",
  },
  {
    title: "Increíbles periféricos y regletas",
    image: "/category/promo-tv.jpg",
    path: "/category",
  },
];
const features = [
  { title: "Ofertas especiales", iconUrl: "/icons/icon-oferta.png" },
  { title: "Envío gratis >50€", iconUrl: "/icons/icon-envio.png" },
  { title: "Recibe en 24h", iconUrl: "/icons/icon-24h.png" },
  { title: "Devoluciones gratuitas", iconUrl: "/icons/icon-dev.png" },
  { title: "Garantía y sustitución", iconUrl: "/icons/icon-garantia.png" },
  { title: "Lanzamientos y novedades", iconUrl: "/icons/icon-novedades.png" },
];

function HomePage() {
  return (
    <Box>
      {" "}
      {/* Main container for the page content */}
      {/* 1. Promotional Banner */}
      <Box sx={{ mb: 4, cursor: "pointer" }}>
        {/* TODO: Replace with actual banner image/component */}
        <img
          src="/banner-ofertas-semana.png"
          alt="Ofertas TOP de la Semana"
          style={{ width: "100%", display: "block" }}
        />
      </Box>
      <Container
        maxWidth={layout.containerMaxWidth}
        sx={{ px: { xs: 1, sm: 2 } }}
      >
        {" "}
        {/* Add padding */}
        {/* 2. Top Ventas Section */}
        <Box sx={{ mb: 4 }}>
          <Box
            sx={{
              display: "flex",
              justifyContent: "space-between",
              alignItems: "center",
              mb: 2,
            }}
          >
            <Typography variant="h5" component="h2" fontWeight="bold">
              Top Ventas Del Momento!
            </Typography>
            <Button
              component={RouterLink}
              to="/products/top-sellers" // TODO: Update path
              endIcon={<ArrowForwardIcon />}
            >
              Ver más
            </Button>
          </Box>
          <Grid container spacing={{ xs: 1, sm: 2 }}>
            {topProducts.map((product) => (
              <Grid item xs={6} sm={4} md={2} key={`top-${product.id}`}>
                <ProductCard product={product} />
              </Grid>
            ))}
          </Grid>
        </Box>
        {/* 3. Promotional Category Blocks */}
        <Grid container spacing={"8%"} sx={{ mb: 4 }} justifyContent={"center"}>
          {promoCategories.map((cat) => (
            <Grid item xs={12} sm={6} md={3} key={cat.title}>
              <Link
                component={RouterLink}
                to={cat.path}
                sx={{ textDecoration: "none" }}
              >
                <Card
                  sx={{
                    position: "relative",
                    "&:hover .MuiCardMedia-root": { transform: "scale(1.03)" },
                  }}
                >
                  <CardMedia
                    component="img"
                    height="200"
                    image={cat.image}
                    alt={cat.title}
                    sx={{ transition: "transform 0.3s ease-in-out" }}
                  />
                  {/* Optional overlay or text */}
                  <Box
                    sx={{
                      position: "absolute",
                      bottom: 0,
                      left: 0,
                      width: "100%",
                      bgcolor: "rgba(0, 0, 0, 0.6)",
                      color: "white",
                      padding: "8px",
                      display: "flex",
                      justifyContent: "space-between",
                      alignItems: "center",
                    }}
                  >
                    <Typography variant="subtitle1">{cat.title}</Typography>
                    <AddCircleOutlineIcon />
                  </Box>
                </Card>
              </Link>
            </Grid>
          ))}
        </Grid>
        {/* 4. Features Icons Section */}
        <Grid
          container
          justifyContent={"center"}
          spacing={5}
          sx={{
            mb: 4,
            backgroundColor: "#fff",
            p: 2,
            borderRadius: 1,
            border: "1px solid #e0e0e0",
          }}
        >
          {features.map((feature) => (
            <Grid item xs={4} sm={2} key={feature.title}>
              <FeatureIcon feature={feature} />
            </Grid>
          ))}
        </Grid>
        {/* 5. Sin IVA Section */}
        <Box sx={{ mb: 4 }}>
          <Box
            sx={{
              display: "flex",
              justifyContent: "space-between",
              alignItems: "center",
              mb: 2,
            }}
          >
            <Typography variant="h5" component="h2" fontWeight="bold">
              Sin IVA, solo con IVAY!
            </Typography>
            <Button
              component={RouterLink}
              to="/products/sin-iva" // TODO: Update path
              endIcon={<ArrowForwardIcon />}
            >
              Ver más
            </Button>
          </Box>
          <Grid container spacing={{ xs: 1, sm: 2 }}>
            {sinIvaProducts.map((product) => (
              <Grid item xs={6} sm={4} md={2} key={`siniva-${product.id}`}>
                <ProductCard product={product} />
              </Grid>
            ))}
          </Grid>
        </Box>
      </Container>
    </Box>
  );
}

export default HomePage;
