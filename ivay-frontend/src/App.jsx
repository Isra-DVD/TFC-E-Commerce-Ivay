import React, { useEffect, useRef } from "react";
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

import '@n8n/chat/style.css';
import './styles/n8n-chat-custom.css'
import { createChat } from '@n8n/chat';

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
      const VALID_CLIENT_ROLES = [2, 4];
      if (!VALID_CLIENT_ROLES.includes(user.roleId)) {
        console.warn("User does not have a valid client/admin role. Logging out.");
        logout();
        navigate("/login", { state: { message: "Unauthorized role." } });
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
    const message = location.state?.message || "Por favor, inicia sesión.";
    return <Navigate to="/login" state={{ from: location, message: message }} replace />;
  }
  const VALID_CLIENT_ROLES = [2, 4];
  if (user && !VALID_CLIENT_ROLES.includes(user.roleId)) {
    return <Navigate to="/login" state={{ from: location, message: "Acceso no autorizado por rol." }} replace />;
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
            element={<RequireAuth><CartPage /></RequireAuth>}
          />
          <Route
            path="/checkout/address"
            element={<RequireAuth><AddressPage /></RequireAuth>}
          />
          <Route
            path="/checkout/payment"
            element={<RequireAuth><Container sx={{ py: 1 }}><PaymentPage /></Container></RequireAuth>}
          />
          <Route
            path="/checkout/summary"
            element={<RequireAuth><Container sx={{ py: 1 }}><SummaryPage /></Container></RequireAuth>}
          />
          <Route
            path="/me"
            element={<RequireAuth><DetailsPage /></RequireAuth>}
          />
          <Route
            path="*"
            element={
              <Container sx={{ py: 4, textAlign: "center" }}>
                <Typography variant="h4">404 - Página no encontrada</Typography>
                <Button component={RouterLink} to="/" variant="contained" sx={{ mt: 2 }}>
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
  const n8nChatInstanceRef = useRef(null);

  useEffect(() => {
    console.log('App useEffect running. Current chat instance in ref:', n8nChatInstanceRef.current);

    if (n8nChatInstanceRef.current) {
      console.log('Chat instance already exists in ref. Cleanup should handle this.');
      // In React StrictMode (dev), this effect runs twice.
      // The cleanup from the first run should destroy the instance and nullify the ref.
    }

    const n8nWebhookUrl = 'http://localhost:5678/webhook/b59f4673-ff9e-47ff-b1b6-06b4f58d5c65/chat';

    const chatOptions = {
      webhookUrl: n8nWebhookUrl, // Your n8n webhook
      // webhookConfig: { method: 'POST', headers: {} }, // Defaults are usually fine
      // target: '#n8n-chat', // Default target where the widget mounts
      mode: 'window', // Default is 'window'
      initialResponseType: 'open', // <<< ADDED: To make it open by default as in your screenshot
      // chatInputKey: 'chatInput', // Default key for input in localStorage
      // chatSessionKey: 'sessionId', // Default key for session ID
      // metadata: {}, // Any extra metadata you want to send with each message

      showWelcomeScreen: false, // Set to true if you want a "New Conversation" button first

      defaultLanguage: 'en', // Or 'es' if you translate below
      // Your custom client-side initial messages.
      // The message "My name is Nathan..." in the default likely comes from the *n8n workflow's first response*,
      // not from this client-side initialMessages array.
      initialMessages: [
        "Hello! I'm the IVAY AI Assistant.", // Your first desired client-side message
        "How can I help you with our products today?" // Your second desired client-side message
      ],
      i18n: {
        // Assuming 'en' for English, or use 'es' for Spanish and set defaultLanguage: 'es'
        en: {
          title: 'IVAY AI Assistant', // <<<< THIS IS YOUR HEADER TEXT
          subtitle: "Start a chat. We're here to help you 24/7.", // Default, can be customized
          // footer: '', // Optional footer text
          getStarted: 'New Conversation', // Text for the button if showWelcomeScreen is true
          inputPlaceholder: 'Ask me about products, stock, or discounts...', // <<<< YOUR INPUT PLACEHOLDER
          // closeButtonTooltip: 'Close chat' // Default, can be customized
        },
        // Example if you wanted Spanish:
        // es: {
        //   title: 'Asistente IA de IVAY',
        //   subtitle: "Inicia un chat. Estamos aquí para ayudarte 24/7.",
        //   getStarted: 'Nueva Conversación',
        //   inputPlaceholder: 'Pregúntame sobre productos, stock, descuentos...',
        // }
      },
    };

    console.log('Attempting to create n8n chat with CUSTOMIZED options:', chatOptions);
    n8nChatInstanceRef.current = createChat(chatOptions);
    console.log('n8n chat instance CREATED and assigned to ref.');

    return () => {
      console.log('useEffect cleanup: Attempting to destroy chat instance.');
      if (n8nChatInstanceRef.current && typeof n8nChatInstanceRef.current.destroy === 'function') {
        console.log('Destroying n8n chat instance:', n8nChatInstanceRef.current);
        n8nChatInstanceRef.current.destroy();
        n8nChatInstanceRef.current = null;
        console.log('n8n chat instance destroyed and ref set to null.');
      } else {
        console.log('useEffect cleanup: No chat instance to destroy, or destroy function not found.');
      }
    };
  }, []); // Empty dependency array


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