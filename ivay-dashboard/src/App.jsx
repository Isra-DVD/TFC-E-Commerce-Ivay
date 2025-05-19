import React, { useEffect } from "react";
import {
  BrowserRouter as Router,
  Routes,
  Route,
  Navigate,
  useLocation,
  Link as RouterLink
} from "react-router-dom";
import {
  Box,
  CssBaseline,
  ThemeProvider,
  createTheme,
  CircularProgress,
  Container,
  Typography,
  Button
} from "@mui/material";

import AdminLayout from "./components/layout/Layout";
import LoginPage from "./components/pages/LoginPage";
import ProductsPage from "./components/pages/ProductsPage";
import CategoriesPage from "./components/pages/CategoriesPage";
import SuppliersPage from "./components/pages/SuppliersPage";
import ProfilePage from "./components/pages/ProfilePage";

import { colors } from "./constants/styles";
import { AuthProvider, useAuth } from "./context/AuthContext";

const adminTheme = createTheme({
  palette: {
    primary: colors.primary,
    secondary: colors.secondary,
    background: colors.background,
    paper: colors.background.paper,
    text: colors.text,
    grey: colors.grey,
    error: colors.error,
    success: colors.success,
  },
  typography: {
    fontFamily: '-apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, "Helvetica Neue", Arial, sans-serif',
  },
  components: {
    MuiButton: {
      styleOverrides: {
        root: {
          textTransform: 'none',
        }
      }
    }
  }
});

function RequireAuth({ children }) {
  const { isAuthenticated, isLoading, user, logout } = useAuth();
  const location = useLocation();

  useEffect(() => {
    if (!isLoading && isAuthenticated && user) {
      const isAdminOrGestor = user.roleId === 2 || user.roleId === 3;
      if (!isAdminOrGestor) {
        console.warn("User does not have admin/gestor role. Logging out.");
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

const NotFound = () => (
  <Container sx={{ py: 4, textAlign: "center", mt: 5 }}>
    <Typography variant="h4" gutterBottom>404 - Página no encontrada</Typography>
    <Typography variant="body1" color="text.secondary" sx={{ mb: 3 }}>
      La página que buscas no existe en el panel de gestor.
    </Typography>
    <Button component={RouterLink} to="/products" variant="contained">
      Ir a Productos
    </Button>
  </Container>
);

export default function App() {
  return (
    <Router>
      <CssBaseline />
      <ThemeProvider theme={adminTheme}>
        <AuthProvider>
          <Routes>
            <Route path="/login" element={<LoginPage />} />
            <Route
              path="/"
              element={
                <RequireAuth>
                  <AdminLayout>
                    <Navigate to="/products" replace />
                  </AdminLayout>
                </RequireAuth>
              }
            />
            <Route
              path="/products"
              element={
                <RequireAuth>
                  <AdminLayout>
                    <ProductsPage />
                  </AdminLayout>
                </RequireAuth>
              }
            />
            <Route
              path="/categories"
              element={
                <RequireAuth>
                  <AdminLayout>
                    <CategoriesPage />
                  </AdminLayout>
                </RequireAuth>
              }
            />
            <Route
              path="/suppliers"
              element={
                <RequireAuth>
                  <AdminLayout>
                    <SuppliersPage />
                  </AdminLayout>
                </RequireAuth>
              }
            />
            <Route
              path="/profile"
              element={
                <RequireAuth>
                  <AdminLayout>
                    <ProfilePage />
                  </AdminLayout>
                </RequireAuth>
              }
            />
            <Route path="*" element={<AdminLayout><NotFound /></AdminLayout>} />
          </Routes>
        </AuthProvider>
      </ThemeProvider>
    </Router>
  );
}