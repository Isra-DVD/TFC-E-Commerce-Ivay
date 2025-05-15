import React from "react";
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
  const { isAuthenticated, isLoading, user, logout }
    = useAuth();
  const location = useLocation();

  if (isLoading) {
    return (
      <Box sx={{ display: 'flex', justifyContent: 'center', alignItems: 'center', height: 'calc(100vh - 70px)' }}>
        <CircularProgress />
      </Box>
    );
  }

  if (!isAuthenticated || !user) {
    return <Navigate to="/login" state={{ from: location }} replace />;
  }

  // Optional: More specific role check
  // Replace 'ROLE_ADMIN' or 'ROLE_GESTOR' with your actual role names from backend
  // const isAdminOrGestor = user.roles && user.roles.some(role => role.name === 'ROLE_ADMIN' || role.name === 'ROLE_GESTOR');
  // if (!isAdminOrGestor) {
  //     console.warn("User does not have admin/gestor role. Logging out.");
  //     logout(); // Ensure logout is available from useAuth
  //     return <Navigate to="/login" state={{ from: location, message: "Acceso no autorizado." }} replace />;
  // }

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
            {/* Add more admin routes here (e.g., for creating/editing items) */}
            {/* <Route path="/products/new" element={<RequireAuth><AdminLayout><ProductFormPage/></AdminLayout></RequireAuth>} /> */}
            {/* <Route path="/products/edit/:id" element={<RequireAuth><AdminLayout><ProductFormPage/></AdminLayout></RequireAuth>} /> */}

            <Route path="*" element={<AdminLayout><NotFound /></AdminLayout>} />
          </Routes>
        </AuthProvider>
      </ThemeProvider>
    </Router>
  );
}