import React from "react";
import {
  BrowserRouter as Router,
  Routes,
  Route,
  Navigate,
  useLocation,
  Link as RouterLink,
} from "react-router-dom";
import {
  Box,
  CssBaseline,
  ThemeProvider,
  createTheme,
  Typography,
  CircularProgress,
  Container,
  Button,
} from "@mui/material";

import Header from "./components/layout/Header";
import Footer from "./components/layout/Footer";
import HomePage from "./components/pages/HomePage";
import LoginPage from "./components/pages/LoginPage";
import RegisterPage from "./components/pages/RegisterPage";
import DetailsPage from "./components/pages/DetailsPage";

import { colors } from "./constants/styles";
import { AuthProvider, useAuth } from "./context/AuthContext";

const theme = createTheme({
  palette: {
    primary: colors.primary,
    secondary: colors.secondary,
    background: colors.background,
    text: colors.text,
    grey: colors.grey,
  },
});

function RequireAuth({ children }) {
  const { isAuthenticated, isLoading } = useAuth();
  const location = useLocation();

  if (isLoading) {
    return (
      <Box textAlign="center" mt={10}>
        <CircularProgress />
      </Box>
    );
  }

  if (!isAuthenticated) {
    return <Navigate to="/login" state={{ from: location }} replace />;
  }
  return children;
}

const LayoutWrapper = () => {
  const location = useLocation();
  const authPaths = ["/login", "/register"];
  const isAuthPageLayout = authPaths.includes(location.pathname);

  return (
    <Box
      sx={{
        display: "flex",
        flexDirection: "column",
        minHeight: "100vh",
        backgroundColor: colors.background.default,
      }}
    >
      {!isAuthPageLayout && <Header />}

      <Box
        component="main"
        sx={{
          flexGrow: 1,
          py: isAuthPageLayout ? 0 : { xs: 2, sm: 4 },
          display: isAuthPageLayout ? "flex" : "block",
          alignItems: isAuthPageLayout ? "center" : undefined,
          justifyContent: isAuthPageLayout ? "center" : undefined,
        }}
      >
        <Routes>
          <Route path="/" element={<HomePage />} />
          <Route path="/login" element={<LoginPage />} />
          <Route path="/register" element={<RegisterPage />} />
          <Route
            path="/me"
            element={
              <RequireAuth>
                <DetailsPage />
              </RequireAuth>
            }
          />
          <Route
            path="*"
            element={
              <Container sx={{ py: 4, textAlign: "center" }}>
                <Typography variant="h4">404 - Página no encontrada</Typography>
                <Button
                  component={RouterLink}
                  to="/"
                  variant="contained"
                  sx={{ mt: 2 }}
                >
                  Volver al inicio
                </Button>
              </Container>
            }
          />
        </Routes>
      </Box>
      {!isAuthPageLayout && <Footer />}
    </Box>
  );
};

export default function App() {
  return (
    <Router>
      <CssBaseline />
      <ThemeProvider theme={theme}>
        <AuthProvider>
          <LayoutWrapper />
        </AuthProvider>
      </ThemeProvider>
    </Router>
  );
}
