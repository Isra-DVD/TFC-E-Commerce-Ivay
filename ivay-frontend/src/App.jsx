// src/App.jsx
import React from 'react';
import { BrowserRouter as Router, Routes, Route, useLocation } from 'react-router-dom';
import { Box, CssBaseline, ThemeProvider, createTheme, Typography } from '@mui/material'; // Added Typography
import Header from './components/layout/Header';
import Footer from './components/layout/Footer';
import HomePage from './components/pages/HomePage';
import LoginPage from './components/pages/LoginPage';
import RegisterPage from './components/pages/RegisterPage';

import { colors } from './constants/styles'; // Assuming layout constants are not needed here directly

const theme = createTheme({
  palette: {
    primary: colors.primary,
    secondary: colors.secondary,
    background: colors.background,
    text: colors.text,
    grey: colors.grey,
  },
});

const LayoutWrapper = () => {
  const location = useLocation();
  const authPaths = ['/login', '/register'];
  const isAuthPage = authPaths.includes(location.pathname);

  return (
    <Box
      sx={{
        display: 'flex',
        flexDirection: 'column',
        minHeight: '100vh', // Ensure the root Box takes full viewport height
        backgroundColor: colors.background.default, // Apply background to the whole page
      }}
    >
      {!isAuthPage && <Header />}
      <Box
        component="main"
        sx={{
          flexGrow: 1, // Allows this Box to take up remaining space
          py: isAuthPage ? 0 : 4, // Vertical padding for non-auth pages
          // px: isAuthPage ? 0 : 2, // Horizontal padding for non-auth pages - Container in pages will handle this

          // Centering styles for auth pages
          display: isAuthPage ? 'flex' : 'block',
          flexDirection: isAuthPage ? 'column' : 'initial', // Ensure column for potential multiple children
          alignItems: isAuthPage ? 'center' : 'initial',
          justifyContent: isAuthPage ? 'center' : 'initial',
        }}
      >
        <Routes>
          <Route path="/" element={<HomePage />} />
          <Route path="/login" element={<LoginPage />} />
          <Route path="/register" element={<RegisterPage />} />
          <Route path="*" element={<Box sx={{ p: 4, textAlign: 'center' }}><Typography variant="h4">404 - Página no encontrada</Typography></Box>} />
        </Routes>
      </Box>
      {!isAuthPage && <Footer />}
    </Box>
  );
}

function App() {
  return (
    <ThemeProvider theme={theme}>
      <CssBaseline />
      <Router>
        <LayoutWrapper />
      </Router>
    </ThemeProvider>
  );
}

export default App;