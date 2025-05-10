import React, { useState } from "react";
import { Link as RouterLink, useNavigate } from "react-router-dom";
import {
  AppBar,
  Toolbar,
  IconButton,
  Typography,
  InputBase,
  Box,
  Drawer,
  List,
  ListItem,
  ListItemIcon,
  ListItemText,
  Divider,
  styled,
  alpha,
  useTheme,
  useMediaQuery,
  Link,
} from "@mui/material";
import MenuIcon from "@mui/icons-material/Menu";
import SearchIcon from "@mui/icons-material/Search";
import PersonOutlineIcon from "@mui/icons-material/PersonOutline";
import ShoppingCartOutlinedIcon from "@mui/icons-material/ShoppingCartOutlined";
import CategoryIcon from "@mui/icons-material/Category";
import HomeIcon from "@mui/icons-material/Home";
import logo from "../../assets/images/ivay-logo.png";
import { useAuth } from "../../context/AuthContext";

const Search = styled("div")(({ theme }) => ({
  position: "relative",
  borderRadius: theme.shape.borderRadius,
  backgroundColor: alpha(theme.palette.common.white, 0.15),
  "&:hover": {
    backgroundColor: alpha(theme.palette.common.white, 0.25),
  },
  marginRight: theme.spacing(2),
  marginLeft: 0,
  width: "100%",
  [theme.breakpoints.up("sm")]: {
    marginLeft: theme.spacing(3),
    width: "auto",
  },
  border: `1px solid ${theme.palette.grey[300]}`,
  backgroundColor: theme.palette.background.paper,
  color: theme.palette.text.primary,
}));

const SearchIconWrapper = styled("div")(({ theme }) => ({
  padding: theme.spacing(0, 2),
  height: "100%",
  position: "absolute",
  right: 0,
  pointerEvents: "none",
  display: "flex",
  alignItems: "center",
  justifyContent: "center",
  color: theme.palette.primary.main,
}));

const StyledInputBase = styled(InputBase)(({ theme }) => ({
  color: "inherit",
  width: "100%",
  "& .MuiInputBase-input": {
    padding: theme.spacing(1, 1, 1, 2),
    paddingRight: `calc(1em + ${theme.spacing(4)})`,
    transition: theme.transitions.create("width"),
    width: "100%",
    [theme.breakpoints.up("md")]: {
      width: "30ch",
      "&:focus": {
        width: "40ch",
      },
    },
  },
}));

const drawerWidth = 240;

function Header() {
  const theme = useTheme();
  const isMobile = useMediaQuery(theme.breakpoints.down("md"));
  const [mobileOpen, setMobileOpen] = useState(false);
  const [searchTerm, setSearchTerm] = useState("");
  const { isAuthenticated } = useAuth();
  const navigate = useNavigate();

  const handleDrawerToggle = () => {
    setMobileOpen(!mobileOpen);
  };

  const handleSearchChange = (e) => setSearchTerm(e.target.value);
  const handleSearchSubmit = (e) => {
    if (e.key === "Enter" || e.type === "click") {
      console.log("Buscando:", searchTerm);
    }
  };

  const drawerCategories = [
    {
      text: "Electrónica",
      icon: <CategoryIcon />,
      path: "/category/electronics",
    },
    { text: "Libros", icon: <CategoryIcon />, path: "/category/books" },
    { text: "Ropa", icon: <CategoryIcon />, path: "/category/clothing" },
    {
      text: "Hogar",
      icon: <CategoryIcon />,
      path: "/category/home-appliances",
    },
  ];

  const drawer = (
    <Box onClick={handleDrawerToggle} sx={{ textAlign: "center" }}>
      <Typography variant="h6" sx={{ my: 2 }}>
        Categorías
      </Typography>
      <Divider />
      <List>
        <ListItem button component={RouterLink} to="/">
          <ListItemIcon>
            {" "}
            <HomeIcon />{" "}
          </ListItemIcon>
          <ListItemText primary="Inicio" />
        </ListItem>
        {drawerCategories.map((item) => (
          <ListItem
            button
            key={item.text}
            component={RouterLink}
            to={item.path}
          >
            <ListItemIcon>{item.icon}</ListItemIcon>
            <ListItemText primary={item.text} />
          </ListItem>
        ))}
      </List>
    </Box>
  );

  const handleProfileClick = () => {
    if (isAuthenticated) {
      navigate("/me");
    } else {
      navigate("/login");
    }
  };


  return (
    <>
      <AppBar
        position="sticky"
        color="inherit"
        elevation={1}
        sx={{ backgroundColor: theme.palette.background.paper }}
      >
        <Toolbar>
          <Link
            component={RouterLink}
            to="/"
            sx={{ display: "flex", alignItems: "center", mr: 2 }}
          >
            <img
              src={logo}
              alt="IVAY Logo"
              style={{
                height: 80,
                width: "auto",
                marginRight: isMobile ? 0 : "auto",
              }}
            />
          </Link>

          <IconButton
            color="inherit"
            aria-label="open drawer"
            edge="start"
            onClick={handleDrawerToggle}
            sx={{ mr: 2 }}
          >
            <MenuIcon />
          </IconButton>
          {!isMobile && (
            <Typography
              variant="button"
              component="div"
              sx={{ mr: 2, cursor: "pointer" }}
              onClick={handleDrawerToggle}
            >
              Todas las categorías
            </Typography>
          )}

          <Box sx={{ flexGrow: 1, display: "flex", justifyContent: "center" }}>
            <Search>
              <StyledInputBase
                placeholder="Buscar…"
                inputProps={{ "aria-label": "search" }}
                value={searchTerm}
                onChange={handleSearchChange}
                onKeyDown={handleSearchSubmit}
              />
              <IconButton
                size="small"
                sx={{
                  position: "absolute",
                  right: 5,
                  top: "50%",
                  transform: "translateY(-50%)",
                }}
                onClick={handleSearchSubmit}
                aria-label="search"
              >
                <SearchIcon />
              </IconButton>
            </Search>
          </Box>

          <Box sx={{ display: "flex", alignItems: "center" }}>
            <IconButton
              color="inherit"
              onClick={handleProfileClick}
              sx={{
                display: "flex",
                flexDirection: "column",
                alignItems: "center",
                ml: 1,
              }}
            >
              <PersonOutlineIcon />
              {!isMobile && (
                <Typography variant="caption">
                  {isAuthenticated ? "Mi perfil" : "Iniciar Sesión"}
                </Typography>
              )}
            </IconButton>
            <IconButton
              color="inherit"
              component={RouterLink}
              to="/cart"
              sx={{
                display: "flex",
                flexDirection: "column",
                alignItems: "center",
                ml: 1,
              }}
            >
              <ShoppingCartOutlinedIcon />
              {!isMobile && <Typography variant="caption">Carrito</Typography>}
            </IconButton>
          </Box>
        </Toolbar>
      </AppBar>

      <Box component="nav">
        <Drawer
          variant="temporary"
          open={mobileOpen}
          onClose={handleDrawerToggle}
          ModalProps={{ keepMounted: true }}
          sx={{
            "& .MuiDrawer-paper": {
              boxSizing: "border-box",
              width: drawerWidth,
            },
          }}
        >
          {drawer}
        </Drawer>
      </Box>
    </>
  );
}

export default Header;
