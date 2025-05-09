import React from "react";
import {
  Box,
  Container,
  Grid,
  Paper,
  Typography,
  Link as MuiLink,
  Avatar,
} from "@mui/material";
import { Link as RouterLink } from "react-router-dom";
import ManageAccountsOutlinedIcon from "@mui/icons-material/ManageAccountsOutlined";
import FavoriteBorderOutlinedIcon from "@mui/icons-material/FavoriteBorderOutlined";
import LockOutlinedIcon from "@mui/icons-material/LockOutlined";
import logo from "../../assets/images/ivay-logo.png";
import { colors, layout } from "../../constants/styles";

// Reusable Benefits Section for the left column
const BenefitsSection = () => (
  <Box
    sx={{
      p: { xs: 2, md: 14 },
      display: "flex",
      flexDirection: "column",
      alignItems: "center",
    }}
  >
    <Typography
      variant="h5"
      component="h2"
      gutterBottom
      sx={{ mb: 3, fontWeight: "bold" }}
    >
      Beneficios
    </Typography>
    <Box
      sx={{
        display: "flex",
        alignItems: "center",
        mb: 2,
        width: "100%",
        maxWidth: 300,
      }}
    >
      <ManageAccountsOutlinedIcon
        sx={{ mr: 1.5, color: colors.primary.main }}
      />
      <Typography variant="body1">Gestiona tus pedidos fácilmente.</Typography>
    </Box>
    <Box
      sx={{
        display: "flex",
        alignItems: "center",
        width: "100%",
        maxWidth: 400,
      }}
    >
      <FavoriteBorderOutlinedIcon
        sx={{ mr: 1.5, color: colors.primary.main }}
      />
      <Typography variant="body1">Guarda tus productos favoritos.</Typography>
    </Box>
  </Box>
);

const AuthLayout = ({ children, pageTitle, formIcon }) => {
  return (
    <Container component="main" maxWidth="lg" sx={{}}>
      <Box sx={{ display: "flex", justifyContent: "center", mb: 3 }}>
        <MuiLink component={RouterLink} to="/">
          <img
            src={logo}
            alt="IVAY Logo"
            style={{ height: 100, width: "auto" }}
          />
        </MuiLink>
      </Box>

      <Grid
        container
        component={Paper}
        elevation={3}
        sx={{
          overflow: "hidden",
          borderRadius: 2,
          maxWidth: 1000,
          margin: "0 auto",
        }}
      >
        {/* Left Column: Benefits */}
        <Grid
          item
          md={5}
          sx={{
            display: { xs: "none", md: "flex" },
            alignItems: "center",
            justifyContent: "center",
            borderRight: `1px solid ${colors.grey[300]}`,
            backgroundColor: "#f9f9f9",
          }}
        >
          <BenefitsSection />
        </Grid>

        {/* Right Column: Form (Login or Register) */}
        <Grid
          item
          xs={12}
          md={7}
          component={Box}
          sx={{ p: { xs: 2, sm: 3, md: 4 } }}
        >
          <Box
            sx={{
              display: "flex",
              flexDirection: "column",
              alignItems: "center",
            }}
          >
            {formIcon && (
              <Avatar sx={{ m: 1, bgcolor: colors.primary.main }}>
                {formIcon}
              </Avatar>
            )}
            <Typography component="h1" variant="h5" sx={{ fontWeight: "bold" }}>
              {pageTitle}
            </Typography>
            {children}
          </Box>
        </Grid>
      </Grid>
    </Container>
  );
};

export default AuthLayout;
