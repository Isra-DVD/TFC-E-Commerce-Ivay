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
  Link as MuiLink,
  CircularProgress,
  Alert,
  Paper,
  useTheme,
} from "@mui/material";
import { Link as RouterLink } from "react-router-dom";
import ArrowForwardIcon from "@mui/icons-material/ArrowForward";
import AddCircleOutlineIcon from "@mui/icons-material/AddCircleOutline";

import { layout, colors } from "../../constants/styles";
import ProductCard from "../common/ProductCard";
import ProductService from "../../service/product.service";
import promoLaptopImg from "../../assets/images/category/promo-laptop.jpg";
import promoMonitorImg from "../../assets/images/category/promo-monitor.jpg";
import promoComponentsImg from "../../assets/images/category/promo-components.jpg";
import promoTvImg from "../../assets/images/category/promo-tv.jpg";
import iconOferta from "../../assets/images/icons/icon-oferta.png";
import iconEnvio from "../../assets/images/icons/icon-envio.png";
import icon24h from "../../assets/images/icons/icon-24h.png";
import iconDev from "../../assets/images/icons/icon-dev.png";
import iconGarantia from "../../assets/images/icons/icon-garantia.png";
import iconNovedades from "../../assets/images/icons/icon-novedades.png";
import bannerOfertas from "../../assets/images/banner-ofertas-semana.png";

/**
 * Renders a small icon and title for a feature.
 */
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

const promoCategories = [
  {
    title: "Los mejores portátiles",
    image: promoLaptopImg,
    path: "/products?categoryId=1",
  },
  {
    title: "Monitores en tendencia",
    image: promoMonitorImg,
    path: "/products?categoryId=2",
  },
  {
    title: "Estrena en smartphone",
    image: promoComponentsImg,
    path: "/products?categoryId=3",
  },
  {
    title: "Increíbles periféricos y regletas",
    image: promoTvImg,
    path: "/products?categoryId=4",
  },
];

const features = [
  { title: "Ofertas especiales", iconUrl: iconOferta },
  { title: "Envío gratis >50€", iconUrl: iconEnvio },
  { title: "Recibe en 24h", iconUrl: icon24h },
  { title: "Devoluciones gratuitas", iconUrl: iconDev },
  { title: "Garantía y sustitución", iconUrl: iconGarantia },
  { title: "Lanzamientos y novedades", iconUrl: iconNovedades },
];

const MAX_PRODUCTS_DISPLAY = 6;

/**
 * Renders the home page of the application, featuring promotional banners,
 * top products, category highlights, key features, and products with special offers.
 */
function HomePage() {
  const theme = useTheme();

  const [topProducts, setTopProducts] = useState([]);
  const [sinIvaProducts, setSinIvaProducts] = useState([]);
  const [loadingTop, setLoadingTop] = useState(true);
  const [loadingSinIva, setLoadingSinIva] = useState(true);
  const [errorTop, setErrorTop] = useState("");
  const [errorSinIva, setErrorSinIva] = useState("");

  /* Effect hook to fetch initial product data (top sellers and "Sin IVA") on component mount. */
  useEffect(() => {
    const fetchTopProducts = async () => {
      setLoadingTop(true);
      setErrorTop("");
      try {
        const data = await ProductService.getProductsPaginated(
          0,
          MAX_PRODUCTS_DISPLAY
        );
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
        const data = await ProductService.getProductsPaginated(
          1,
          MAX_PRODUCTS_DISPLAY
        );
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

  /* Renders a section displaying a list of products with a title,
   handling loading, error, and empty states, and a 'View more' button. */
  const renderProductSection = (
    title,
    products,
    loading,
    error,
    viewMorePath
  ) => {
    if (loading) {
      return (
        <Box
          sx={{
            display: "flex",
            justifyContent: "center",
            alignItems: "center",
            height: 200,
            my: 4,
          }}
        >
          <CircularProgress />
        </Box>
      );
    }
    if (error) {
      return (
        <Alert severity="error" sx={{ my: 4 }}>
          {error}
        </Alert>
      );
    }
    if (products.length === 0 && !loading) {
      return (
        <Typography
          sx={{ my: 4, textAlign: "center", color: "text.secondary" }}
        >
          No hay productos disponibles en esta sección.
        </Typography>
      );
    }
    return (
      <Box
        sx={{
          mb: 5,
          maxWidth: 1250,
          mx: "auto",
          px: { xs: 1, sm: 2 },
        }}
      >
        <Box
          sx={{
            display: "flex",
            justifyContent: "space-between",
            alignItems: "center",
            mb: 2.5,
          }}
        >
          <Typography variant="h5" component="h2" fontWeight="bold">
            {title}
          </Typography>
          <Button
            component={RouterLink}
            to={viewMorePath}
            endIcon={<ArrowForwardIcon />}
          >
            Ver más
          </Button>
        </Box>
        <Grid container spacing={{ xs: 1.5, sm: 2, md: 2.5 }}>
          {products.map((product) => (
            <Grid
              item
              xs={6}
              sm={4}
              md={2}
              key={`${title.replace(/\s+/g, "-")}-${product.id}`}
            >
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
      <Box
        sx={{
          mb: 4,
          cursor: "pointer",
          borderRadius: { xs: 0, sm: 2 },
          overflow: "hidden",
        }}
      >
        <MuiLink component={RouterLink} to="/products?filter=ofertas-semana">
          <img
            src={bannerOfertas}
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
          "/products?filter=top-sellers"
        )}

        {/* 3. Promotional Category Blocks */}
        <Grid
          container
          spacing={{ xs: 2, sm: 3, md: "6%" }}
          sx={{ mb: 5 }}
          justifyContent={"center"}
        >
          {promoCategories.map((cat) => (
            <Grid
              item
              xs={12}
              sm={6}
              md={3}
              key={cat.title}
              sx={{
                width: { xs: "100%", sm: 100, md: 210 },
                height: { xs: "auto", sm: 300, md: 280 },
              }}
            >
              <MuiLink
                component={RouterLink}
                to={cat.path}
                sx={{
                  textDecoration: "none",
                  display: "block",
                  height: "100%",
                }}
              >
                <Card
                  sx={{
                    position: "relative",
                    height: "100%",
                    display: "flex",
                    flexDirection: "column",
                    transition:
                      "transform 0.2s ease-in-out, box-shadow 0.2s ease-in-out",
                    "&:hover": {
                      transform: "translateY(-5px)",
                      boxShadow: theme.shadows[8],
                    },
                    "&:hover .MuiCardMedia-root": { transform: "scale(1.04)" },
                  }}
                >
                  <CardMedia
                    component="img"
                    height="200"
                    image={cat.image}
                    alt={cat.title}
                    sx={{
                      transition: "transform 0.3s ease-in-out",
                      objectFit: "cover",
                    }}
                  />
                  <CardContent
                    sx={{
                      flexGrow: 1,
                      p: 0,
                      display: "flex",
                      alignItems: "flex-end",
                    }}
                  >
                    <Box
                      sx={{
                        width: "100%",
                        bgcolor: "rgba(0, 0, 0, 0.7)",
                        color: "white",
                        padding: "12px 16px",
                        display: "flex",
                        justifyContent: "center",
                        alignItems: "center",
                        textAlign: "center",
                      }}
                    >
                      <Typography variant="subtitle1" fontWeight="medium">
                        {cat.title}
                      </Typography>
                      <AddCircleOutlineIcon sx={{ opacity: 0.85 }} />
                    </Box>
                  </CardContent>
                </Card>
              </MuiLink>
            </Grid>
          ))}
        </Grid>

        {/* 4. Features Icons Section */}
        <Paper
          elevation={0}
          sx={{
            mb: 5,
            p: { xs: 1.5, sm: 2.5 },
            borderRadius: 2,
            border: `1px solid ${theme.palette.divider}`,
          }}
        >
          <Grid container justifyContent={"space-around"}>
            {features.map((feature) => (
              <Grid
                item
                xs={4}
                sm={"auto"}
                key={feature.title}
                sx={{ p: { xs: 0.5, sm: 1 } }}
              >
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
          "/products?filter=sin-iva"
        )}
      </Container>
    </Box>
  );
}

export default HomePage;