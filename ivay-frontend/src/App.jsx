import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import { Box, CssBaseline, ThemeProvider, createTheme } from '@mui/material';
import Header from './components/layout/Header';
import Footer from './components/layout/Footer';
import HomePage from './components/pages/HomePage';
// FUTURE IMPORTS
// import ProductListPage from './components/pages/ProductListPage';
// import ProductDetailPage from './components/pages/ProductDetailPage';
// import CartPage from './components/pages/CartPage';
// import ProfilePage from './components/pages/ProfilePage';

import { colors, layout } from './constants/styles'; // Import constants

// Create a basic theme using constants 
const theme = createTheme({
  palette: {
    primary: colors.primary,
    secondary: colors.secondary,
    background: colors.background,
    text: colors.text,
    grey: colors.grey,
  },
});


function App() {
  return (
    <ThemeProvider theme={theme}> {/* Apply the theme */}
      <CssBaseline /> {/* Normalizes CSS across browsers */}
      <Router>
        <Box sx={{ display: 'flex', flexDirection: 'column', minHeight: '100vh' }}>
          <Header />
          <Box
            component="main"
            sx={{
              flexGrow: 1,
              pb: 4,
              backgroundColor: colors.background.default,
            }}
          >
            <Routes>
              <Route path="/" element={<HomePage />} />
              {/* Add routes for other pages */}
              {/* <Route path="/products" element={<ProductListPage />} /> */}
              {/* <Route path="/products/:productId" element={<ProductDetailPage />} /> */}
              {/* <Route path="/category/:categoryName" element={<ProductListPage />} /> */}
              {/* <Route path="/cart" element={<CartPage />} /> */}
              {/* <Route path="/profile" element={<ProfilePage />} /> */}
              {/* Add a 404 Not Found route */}
              <Route path="*" element={<div>404 Not Found</div>} />
            </Routes>
          </Box>
          <Footer />
        </Box>
      </Router>
    </ThemeProvider>
  );
}

export default App;