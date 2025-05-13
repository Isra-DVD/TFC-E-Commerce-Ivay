// src/components/pages/HomePage.jsx
import React, { useEffect, useState } from "react";
import {
  Box,
  Container,
  Typography,
  Button,
  Grid,
  Card,
  CardMedia,
  CardContent,
  Link as MuiLink, // Changed from Link to MuiLink for consistency with MUI
  CircularProgress,
  Alert,
  Paper, // Added Paper for better section styling
  useTheme, // Added useTheme for accessing theme properties
} from "@mui/material";
import { Link as RouterLink } from "react-router-dom";
import ArrowForwardIcon from "@mui/icons-material/ArrowForward";
import AddCircleOutlineIcon from "@mui/icons-material/AddCircleOutline";

import { layout, colors } from "../../constants/styles"; // Make sure colors is used or remove if not
import ProductCard from "../common/ProductCard";
import ProductService from "../../service/product.service"; // Import ProductService

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

// Update promoCategories paths to point to ProductsPage with categoryId filter
const promoCategories = [
  { title: "Los mejores portátiles", image: "/category/promo-laptop.jpg", path: "/products?categoryId=1" /* Replace 1 with actual laptop category ID */ },
  { title: "Monitores en tendencia", image: "/category/promo-monitor.jpg", path: "/products?categoryId=2" /* Replace 2 with actual monitor category ID */ },
  { title: "Estrena smartphone", image: "/category/promo-components.jpg", path: "/products?categoryId=3" /* Replace 3 with actual smartphone category ID */ },
  { title: "Increíbles periféricos y regletas", image: "/category/promo-tv.jpg", path: "/products?categoryId=4" /* Replace 4 with actual peripherals category ID */ },
];

const features = [
  { title: "Ofertas especiales", iconUrl: "/icons/icon-oferta.png" },
  { title: "Envío gratis >50€", iconUrl: "/icons/icon-envio.png" },
  { title: "Recibe en 24h", iconUrl: "/icons/icon-24h.png" },
  { title: "Devoluciones gratuitas", iconUrl: "/icons/icon-dev.png" },
  { title: "Garantía y sustitución", iconUrl: "/icons/icon-garantia.png" },
  { title: "Lanzamientos y novedades", iconUrl: "/icons/icon-novedades.png" },
];

const MAX_PRODUCTS_DISPLAY = 6;

function HomePage() {
  const theme = useTheme(); // For accessing theme properties like shadows

  const [topProducts, setTopProducts] = useState([]);
  const [sinIvaProducts, setSinIvaProducts] = useState([]);
  const [loadingTop, setLoadingTop] = useState(true);
  const [loadingSinIva, setLoadingSinIva] = useState(true);
  const [errorTop, setErrorTop] = useState("");
  const [errorSinIva, setErrorSinIva] = useState("");

  useEffect(() => {
    const fetchTopProducts = async () => {
      setLoadingTop(true);
      setErrorTop("");
      try {
        // Placeholder: Fetch first page of products.
        // TODO: Replace with actual API call for top sellers if available
        // e.g., ProductService.getTopSellers(MAX_PRODUCTS_DISPLAY)
        // or ProductService.getProductsPaginated(0, MAX_PRODUCTS_DISPLAY, { sortBy: 'sales_desc' });
        const data = await ProductService.getProductsPaginated(0, MAX_PRODUCTS_DISPLAY);
        setTopProducts(data.content || []);
      } catch (err) {
        console.error("Error fetching top products:", err);
        setErrorTop("No se pudieron cargar los productos más vendidos.");
      } finally {
        setLoadingTop(false);
      }
    };

    const fetchSinIvaProducts = async () => {
      setLoadingSinIva(true);
      setErrorSinIva("");
      try {
        // Placeholder: Fetch a different set of products.
        // TODO: Replace with actual API call for "Sin IVA" products.
        // e.g., ProductService.getProductsByTag('sin-iva', MAX_PRODUCTS_DISPLAY);
        // or ProductService.getProductsPaginated(0, MAX_PRODUCTS_DISPLAY, { promotion: 'sinIva' });
        const data = await ProductService.getProductsPaginated(1, MAX_PRODUCTS_DISPLAY); // Fetching page 1 (second page) for variety
        setSinIvaProducts(data.content || []);
      } catch (err) {
        console.error("Error fetching Sin IVA products:", err);
        setErrorSinIva("No se pudieron cargar los productos Sin IVA.");
      } finally {
        setLoadingSinIva(false);
      }
    };

    fetchTopProducts();
    fetchSinIvaProducts();
  }, []);

  const renderProductSection = (title, products, loading, error, viewMorePath) => {
    if (loading) {
      return (
        <Box sx={{ display: 'flex', justifyContent: 'center', alignItems: 'center', height: 200, my: 4 }}>
          <CircularProgress />
        </Box>
      );
    }
    if (error) {
      return <Alert severity="error" sx={{ my: 4 }}>{error}</Alert>;
    }
    if (products.length === 0 && !loading) { // Check !loading to avoid showing this during initial load
      return <Typography sx={{ my: 4, textAlign: 'center', color: 'text.secondary' }}>No hay productos disponibles en esta sección.</Typography>;
    }
    return (
      <Box sx={{ mb: 5 }}> {/* Increased margin bottom */}
        <Box
          sx={{
            display: "flex",
            justifyContent: "space-between",
            alignItems: "center",
            mb: 2.5, // Increased margin bottom
          }}
        >
          <Typography variant="h5" component="h2" fontWeight="bold">
            {title}
          </Typography>
          <Button
            component={RouterLink}
            to={viewMorePath} // Updated path
            // sx={{ color: "black" }} // Consider using theme colors or default
            endIcon={<ArrowForwardIcon />}
          >
            Ver más
          </Button>
        </Box>
        <Grid container spacing={{ xs: 1.5, sm: 2, md: 2.5 }}> {/* Adjusted spacing */}
          {products.map((product) => (
            <Grid item xs={6} sm={4} md={2} key={`${title.replace(/\s+/g, '-')}-${product.id}`}>
              <ProductCard product={product} />
            </Grid>
          ))}
        </Grid>
      </Box>
    );
  };

  return (
    <Box>
      {/* 1. Promotional Banner */}
      <Box sx={{ mb: 4, cursor: "pointer", borderRadius: { xs: 0, sm: 2 }, overflow: 'hidden' }}>
        <MuiLink component={RouterLink} to="/products?filter=ofertas-semana">
          <img
            src="/banner-ofertas-semana.png"
            alt="Ofertas TOP de la Semana"
            style={{ width: "100%", display: "block" }}
          />
        </MuiLink>
      </Box>

      <Container
        maxWidth={layout.containerMaxWidth}
        sx={{ px: { xs: 1, sm: 2 } }}
      >
        {/* 2. Top Ventas Section */}
        {renderProductSection(
          "Top Ventas Del Momento!",
          topProducts,
          loadingTop,
          errorTop,
          "/products?filter=top-sellers" // Link to ProductsPage with a filter
        )}

        {/* 3. Promotional Category Blocks */}
        <Grid container spacing={{ xs: 2, sm: 3, md: "6%" }} sx={{ mb: 5 }} justifyContent={"center"}> {/* Adjusted spacing & mb */}
          {promoCategories.map((cat) => (
            <Grid item xs={12} sm={6} md={3} key={cat.title}>
              <MuiLink // Changed from Link to MuiLink
                component={RouterLink}
                to={cat.path} // Use updated paths
                sx={{ textDecoration: "none", display: 'block', height: '100%' }}
              >
                <Card
                  sx={{
                    position: "relative",
                    height: '100%',
                    display: 'flex',
                    flexDirection: 'column',
                    transition: "transform 0.2s ease-in-out, box-shadow 0.2s ease-in-out",
                    "&:hover": {
                      transform: "translateY(-5px)", // Enhanced hover effect
                      boxShadow: theme.shadows[8]    // Enhanced hover shadow
                    },
                    "&:hover .MuiCardMedia-root": { transform: "scale(1.04)" },
                  }}
                >
                  <CardMedia
                    component="img"
                    height="200"
                    image={cat.image}
                    alt={cat.title}
                    sx={{ transition: "transform 0.3s ease-in-out", objectFit: 'cover' }}
                  />
                  <CardContent sx={{ flexGrow: 1, p: 0, display: 'flex', alignItems: 'flex-end' }}> {/* Modified CardContent */}
                    <Box
                      sx={{
                        width: "100%",
                        bgcolor: "rgba(0, 0, 0, 0.7)", // Darker overlay
                        color: "white",
                        padding: "12px 16px", // Adjusted padding
                        display: "flex",
                        justifyContent: "space-between",
                        alignItems: "center",
                      }}
                    >
                      <Typography variant="subtitle1" fontWeight="medium">{cat.title}</Typography>
                      <AddCircleOutlineIcon sx={{ opacity: 0.85 }} />
                    </Box>
                  </CardContent>
                </Card>
              </MuiLink>
            </Grid>
          ))}
        </Grid>

        {/* 4. Features Icons Section */}
        {/* Using Paper component for a slightly elevated, distinct section */}
        <Paper
          elevation={0} // Subtle elevation or use 0 for just border
          sx={{
            mb: 5, // Increased margin bottom
            // backgroundColor: "#fff", // theme.palette.background.paper is default for Paper
            p: { xs: 1.5, sm: 2.5 }, // Adjusted padding
            borderRadius: 2, // Consistent border radius
            border: `1px solid ${theme.palette.divider}`, // Use theme divider color
          }}
        >
          <Grid
            container
            justifyContent={"space-around"} // Or space-between for more spread
          // spacing prop on container adds padding around items, direct padding on items gives more control
          >
            {features.map((feature) => (
              <Grid item xs={4} sm={"auto"} key={feature.title} sx={{ p: { xs: 0.5, sm: 1 } }}> {/* Allow sm to size automatically */}
                <FeatureIcon feature={feature} />
              </Grid>
            ))}
          </Grid>
        </Paper>

        {/* 5. Sin IVA Section */}
        {renderProductSection(
          "Sin IVA, solo con IVAY!",
          sinIvaProducts,
          loadingSinIva,
          errorSinIva,
          "/products?filter=sin-iva" // Link to ProductsPage with a filter
        )}
      </Container>
    </Box>
  );
}

export default HomePage;