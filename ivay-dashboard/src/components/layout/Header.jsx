import React from "react";
import { Link as RouterLink, useNavigate, useLocation } from "react-router-dom";
import {
    AppBar,
    Toolbar,
    Button,
    Box,
    IconButton,
    useTheme,
    useMediaQuery,
    Typography,
} from "@mui/material";
import PersonIcon from '@mui/icons-material/Person';
import logo from "../../assets/images/ivay-logo.png";
import { colors } from "../../constants/styles";

const Header = () => {
    const theme = useTheme();
    const isMobile = useMediaQuery(theme.breakpoints.down("sm"));
    const navigate = useNavigate();
    const location = useLocation();

    const navItems = [
        { label: "Categorías", path: "/categories" },
        { label: "Proveedores", path: "/suppliers" },
        { label: "Productos", path: "/products" },
    ];

    const handleProfileClick = () => {
        navigate("/profile");
    };

    return (
        <AppBar
            position="sticky"
            elevation={1}
            sx={{ backgroundColor: colors.background.paper, color: colors.text.primary }}
        >
            <Toolbar sx={{ minHeight: { xs: 60, sm: 70 } }}>
                <Box sx={{ display: "flex", alignItems: "center", mr: 2 }}>
                    <RouterLink to="/products">
                        <img
                            src={logo}
                            alt="IVAY Logo"
                            style={{
                                height: isMobile ? 60 : 70,
                                width: "auto",
                                display: "block",
                            }}
                        />
                    </RouterLink>
                </Box>

                <Box sx={{ flexGrow: 1, display: "flex", gap: { xs: 1, sm: 2 } }}>
                    {navItems.map((item) => (
                        <Button
                            key={item.label}
                            component={RouterLink}
                            to={item.path}
                            variant={location.pathname.startsWith(item.path) ? "outlined" : "text"}
                            sx={{
                                fontWeight: 'medium',
                                color: location.pathname.startsWith(item.path) ? colors.primary.main : 'inherit',
                                borderColor: location.pathname.startsWith(item.path) ? colors.primary.main : 'transparent',
                                '&:hover': {
                                    color: colors.primary.main,
                                    backgroundColor: location.pathname.startsWith(item.path) ? colors.primary.light + '1A' : colors.grey[200],
                                    borderColor: colors.primary.main,
                                }
                            }}
                        >
                            {item.label}
                        </Button>
                    ))}
                </Box>

                <Button
                    color="inherit"
                    onClick={handleProfileClick}
                    startIcon={<PersonIcon />}
                    sx={{
                        textTransform: 'none',
                        fontWeight: 'medium',
                        '&:hover': { color: colors.primary.main }
                    }}
                >
                    Mi perfil
                </Button>
            </Toolbar>
        </AppBar>
    );
};

export default Header;