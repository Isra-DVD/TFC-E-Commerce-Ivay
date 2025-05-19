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
import ProductDetailsPage from "./components/pages/ProductDetailsPage";
import CheckoutHeader from "./components/layout/CheckoutHeader";
import CartPage from "./components/pages/CartPage";
import ProductsPage from "./components/pages/ProductsPage";
import SearchResultsPage from "./components/pages/SearchResultPage";
import AddressPage from "./components/pages/AddressPage";
import SummaryPage from "./components/pages/SummaryPage";
import PaymentPage from "./components/pages/PaymentPage";
import { useEffect } from "react";

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
  const { isAuthenticated, isLoading, user, logout } = useAuth();
  const location = useLocation();

  useEffect(() => {
    if (!isLoading && isAuthenticated && user) {
      const isAdminOrGestor = user.roleId === 2 || user.roleId === 4;
      if (!isAdminOrGestor) {
        console.warn("User does not have admin/client role. Logging out.");
        logout();
      }
    }
  }, [isLoading, isAuthenticated, user, logout, location]);

  if (isLoading) {
    return (
      <Box sx={{ display: 'flex', justifyContent: 'center', alignItems: 'center', height: 'calc(100vh - 70px)' }}>
        <CircularProgress />
      </Box>
    );
  }
  if (!isAuthenticated || !user) {
    const message = location.state?.message || (user && !(user.roleId === 2 || user.roleId === 3) ? "Acceso no autorizado por rol." : "Por favor, inicia sesión.");
    return <Navigate to="/login" state={{ from: location, message: message }} replace />;
  }
  return children;
}

const CHECKOUT_PATHS = [
  "/cart",
  "/checkout/address",
  "/checkout/payment",
  "/checkout/summary",
];

const LayoutWrapper = () => {
  const location = useLocation();
  const authPaths = ["/login", "/register", "/me"];
  const isAuthPageLayout = authPaths.includes(location.pathname);

  const isCheckoutFlow = CHECKOUT_PATHS.some((path) =>
    location.pathname.startsWith(path)
  );

  return (
    <Box
      sx={{
        display: "flex",
        flexDirection: "column",
        minHeight: "100vh",
        backgroundColor: colors.background.default,
      }}
    >
      {/* Conditional Header Rendering */}
      {!isAuthPageLayout && (isCheckoutFlow ? <CheckoutHeader /> : <Header />)}

      <Box
        component="main"
        sx={{
          flexGrow: 1,
          py: isAuthPageLayout
            ? 0
            : isCheckoutFlow
              ? { xs: 2, sm: 3 }
              : { xs: 2, sm: 4 },
          display: isAuthPageLayout ? "flex" : "block",
          alignItems: isAuthPageLayout ? "center" : undefined,
          justifyContent: isAuthPageLayout ? "center" : undefined,
        }}
      >
        <Routes>
          <Route path="/" element={<HomePage />} />
          <Route path="/login" element={<LoginPage />} />
          <Route path="/register" element={<RegisterPage />} />
          <Route path="/search" element={<SearchResultsPage />} />
          <Route path="/products/:productId" element={<ProductDetailsPage />} />
          <Route path="/products" element={<ProductsPage />} />

          <Route
            path="/cart"
            element={
              <RequireAuth>
                <CartPage />
              </RequireAuth>
            }
          />
          {/* Add placeholder routes for other checkout steps */}
          <Route
            path="/checkout/address"
            element={
              <RequireAuth>
                <AddressPage />
              </RequireAuth>
            }
          />
          <Route
            path="/checkout/payment"
            element={
              <RequireAuth>
                <Container sx={{ py: 1 }}>
                  <PaymentPage />
                </Container>
              </RequireAuth>
            }
          />
          <Route
            path="/checkout/summary"
            element={
              <RequireAuth>
                <Container sx={{ py: 1 }}>
                  <SummaryPage />
                </Container>
              </RequireAuth>
            }
          />
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

      {/* Footer should not appear on auth pages or checkout flow pages */}
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
