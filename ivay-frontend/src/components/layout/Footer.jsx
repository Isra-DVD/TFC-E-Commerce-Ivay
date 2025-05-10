import React from "react";
import { Box, Container, Grid, Typography, Link, Divider } from "@mui/material";
import { layout, colors, commonStyles } from "../../constants/styles";

const brandLogos = [
  { src: "/logos/hp.jpg", alt: "HP Logo" },
  { src: "/logos/samsung.jpg", alt: "Samsung Logo" },
  { src: "/logos/apple.jpg", alt: "Apple Logo" },
  { src: "/logos/intel.jpg", alt: "Intel Logo" },
  { src: "/logos/windows.jpg", alt: "Windows 11 Logo" },
  { src: "/logos/corsair.jpg", alt: "Corsair Logo" },
];

function Footer() {
  return (
    <Box
      component="footer"
      sx={{
        py: 4,
        px: 2,
        mt: "auto",
        backgroundColor: colors.background.paper,
        borderTop: `1px solid ${colors.grey[300]}`,
      }}
    >
      <Container maxWidth={layout.containerMaxWidth}>
        {/* Brand Logos */}
        <Grid
          container
          spacing={"10%"}
          justifyContent="center"
          alignItems="center"
          sx={{ mb: 4 }}
        >
          {brandLogos.map((logo) => (
            <Grid item key={logo.alt}>
              <img
                src={logo.src}
                alt={logo.alt}
                style={{ maxHeight: "40px", maxWidth: "100px", opacity: 0.6 }}
              />
            </Grid>
          ))}
        </Grid>

        <Divider sx={{ mb: 4 }} />

        {/* Footer Content */}
        <Grid container spacing={4}>
          <Grid item xs={12} md={6}>
            <Typography variant="h6" gutterBottom>
              IVAY es tu tienda online de tecnología e informática
            </Typography>
            <Typography variant="body2" color="text.secondary">
              Somos la tienda online líder en tecnología, basada en la
              confianza, la experiencia de compra y el conocimiento del sector
              tecnológico. Desde 2005 somos expertos en informática y
              electrónica. Buscamos innovar, descubrir y responder día a día a
              las últimas necesidades de la comunidad más tecnológica siendo el
              cliente nuestra razón de ser, superando siempre sus expectativas.
              Actualmente contamos con más de 100.000 artículos disponibles y
              31.700 m2 repartidos entre todas nuestras instalaciones, además de
              cuatro tiendas físicas en Madrid, Barcelona, Murcia y Alhama de
              Murcia.
            </Typography>
          </Grid>
          <Grid item xs={12} md={6}>
            <Typography variant="h6" gutterBottom>
              Los expertos en tecnología más comprometidos contigo
            </Typography>
            <Typography variant="body2" color="text.secondary" component="div">
              Nuestros compromisos se centran en ser expertos en lo que vendemos
              y garantizar al cliente todo lo que necesita:
              <ul>
                <li>
                  Envíos gratuitos a partir de 50€ siempre en 24 horas* (si
                  excepcionalmente incumplimos la fecha de entrega, te
                  devolvemos los gastos de envío).
                </li>
                <li>Garantía con la mejor solución para ti en 24h.</li>
                <li>Devoluciones gratuitas.</li>
                <li>Atención al cliente local y sin esperas.</li>
                <li>
                  Nos tomamos muy en serio las opiniones de nuestros clientes, y
                  puedes verlas siempre en nuestros artículos
                </li>
              </ul>
              Bienvenido a Ivay.
            </Typography>
          </Grid>
        </Grid>

        <Divider sx={{ my: 4 }} />

        {/* Copyright/Company Info */}
        <Box sx={{ textAlign: "center" }}>
          <Typography variant="caption" color="text.secondary">
            IVAY PRODUCTS SLU CIF C73497014. AVDA Micasa, Parcela 2-5 y 2-6.
            Polígono Industrial de Granadilla, 38604, Granadilla de Abona,
            Tenerife. ESPAÑA.
          </Typography>
          <Typography
            variant="caption"
            display="block"
            color="text.secondary"
            sx={{ mt: 1 }}
          >
            © {new Date().getFullYear()} IVAY Tienda Online. Todos los derechos
            reservados.
          </Typography>
        </Box>
      </Container>
    </Box>
  );
}

export default Footer;
