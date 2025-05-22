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

/**
 * Wrapper component for protected routes that require authentication and 
 * specific roles.
 * Redirects to the login page if the user is not authenticated or lacks the 
 * necessary role.
 * Displays a loading spinner during authentication checks.
 *
 * @param {object} props - The component props.
 * @param {React.ReactNode} props.children - The components to render if 
 * authentication and role checks pass.
 */
function RequireAuth({ children }) {
  const { isAuthenticated, isLoading, user, logout } = useAuth();
  const location = useLocation();

  /* Effect hook that checks if the authenticated user has the required role (Admin or Gestor) and logs them out if not. */
  useEffect(() => {
    if (!isLoading && isAuthenticated && user) {
      const isAdminOrGestor = user.roleId === 2 || user.roleId === 3;
      if (!isAdminOrGestor) {
        console.warn("User does not have admin/gestor role. Logging out.");
        logout();
      }
    }
  }, [isLoading, isAuthenticated, user, logout, location]);

  /* Displays a loading spinner while the authentication status is being determined. */
  if (isLoading) {
    return (
      <Box sx={{ display: 'flex', justifyContent: 'center', alignItems: 'center', height: 'calc(100vh - 70px)' }}>
        <CircularProgress />
      </Box>
    );
  }

  /* Redirects to the login page if the user is not authenticated or the user object is missing/invalid after loading. */
  if (!isAuthenticated || !user) {
    const message = location.state?.message || (user && !(user.roleId === 2 || user.roleId === 3) ? "Acceso no autorizado por rol." : "Por favor, inicia sesión.");
    return <Navigate to="/login" state={{ from: location, message: message }} replace />;
  }

  return children;
}

/**
 * Component displayed for routes that do not match any defined paths.
 * Informs the user that the page was not found and provides a link back to the 
 * products page.
 */
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

/**
 * The main application component.
 * Sets up the React Router, Material UI theme provider, and the authentication 
 * context.
 * Defines the application's routes, including public routes (like login) and 
 * protected routes that require authentication.
 */
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