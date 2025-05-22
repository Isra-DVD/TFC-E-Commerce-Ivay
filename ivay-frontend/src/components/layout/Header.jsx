import React, { useState, useEffect } from "react";
import { Link as RouterLink, useNavigate, useLocation } from "react-router-dom";
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
  Link as MuiLink,
  CircularProgress,
  Alert,
  Collapse,
} from "@mui/material";
import MenuIcon from "@mui/icons-material/Menu";
import SearchIcon from "@mui/icons-material/Search";
import PersonOutlineIcon from "@mui/icons-material/PersonOutline";
import ShoppingCartOutlinedIcon from "@mui/icons-material/ShoppingCartOutlined";
import CategoryIcon from "@mui/icons-material/Category";
import HomeIcon from "@mui/icons-material/Home";
import AppsIcon from "@mui/icons-material/Apps";
import ExpandLess from "@mui/icons-material/ExpandLess";
import ExpandMore from "@mui/icons-material/ExpandMore";

import logo from "../../assets/images/ivay-logo.png";
import { useAuth } from "../../context/AuthContext";
import CategoryService from "../../service/category.service";
import { colors } from "../../constants/styles";

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

const drawerWidth = 250;

/**
 * Renders the main header of the application, including site logo,
 * a search bar, navigation icons, and a mobile drawer with category links.
 */
function Header() {
  const theme = useTheme();
  const location = useLocation();
  const isMobile = useMediaQuery(theme.breakpoints.down("md"));
  const [mobileOpen, setMobileOpen] = useState(false);
  const [searchTerm, setSearchTerm] = useState("");
  const { isAuthenticated } = useAuth();
  const navigate = useNavigate();

  const [categories, setCategories] = useState([]);
  const [categoryLoading, setCategoryLoading] = useState(true);
  const [categoryError, setCategoryError] = useState("");

  /* Fetches the list of categories from the CategoryService on component mount. */
  useEffect(() => {
    const fetchCategories = async () => {
      setCategoryLoading(true);
      setCategoryError("");
      try {
        const fetchedCategories = await CategoryService.getAllCategories();
        setCategories(fetchedCategories || []);
      } catch (error) {
        console.error("Error fetching categories:", error);
        setCategoryError("No se pudieron cargar las categorías.");
      } finally {
        setCategoryLoading(false);
      }
    };
    fetchCategories();
  }, []);

  /**
   * Toggles the state of the mobile navigation drawer.
   */
  const handleDrawerToggle = () => {
    setMobileOpen(!mobileOpen);
  };

  /**
   * Updates the search term state based on the input value.
   */
  const handleSearchChange = (e) => setSearchTerm(e.target.value);

  /**
   * Handles the search submission event, navigating to the search results page
   * and clearing the search term input.
   */
  const handleSearchSubmit = (e) => {
    if ((e.key && e.key === "Enter") || e.type === "click") {
      e.preventDefault();
      if (searchTerm.trim()) {
        navigate(`/search?q=${encodeURIComponent(searchTerm.trim())}`);
        setSearchTerm("");
        if (mobileOpen) setMobileOpen(false);
      }
    }
  };

  const dynamicDrawerCategories = categories.map((category) => ({
    id: category.id,
    text: category.name,
    icon: <CategoryIcon />,
    path: `/products?categoryId=${category.id}`,
  }));

  const drawer = (
    <Box onClick={(e) => e.stopPropagation()} sx={{ textAlign: "left" }}>
      <Box sx={{ p: 2, borderBottom: `1px solid ${theme.palette.divider}` }}>
        <Typography
          variant="h6"
          component="div"
          sx={{ fontWeight: "bold", color: colors.primary.dark }}
        >
          Explorar Tienda
        </Typography>
      </Box>
      {categoryLoading ? (
        <Box
          sx={{
            display: "flex",
            justifyContent: "center",
            alignItems: "center",
            py: 3,
            height: 150,
          }}
        >
          <CircularProgress />
        </Box>
      ) : categoryError ? (
        <Alert severity="error" sx={{ m: 2 }}>
          {categoryError}
        </Alert>
      ) : (
        <List sx={{ py: 0, "& .MuiListItem-root": { letterSpacing: "0.5px" } }}>
          <ListItem
            button="true"
            component={RouterLink}
            to="/"
            onClick={handleDrawerToggle}
            selected={location.pathname === "/"}
            sx={{
              py: 1.5,
              borderBottom: `1px solid ${theme.palette.divider}`,
              "&.Mui-selected": {
                backgroundColor: alpha(colors.primary.main, 0.08),
                "& .MuiListItemIcon-root, & .MuiListItemText-primary": {
                  color: colors.primary.main,
                  fontWeight: 500,
                },
              },
              "&:hover": {
                backgroundColor: alpha(colors.primary.main, 0.04),
              },
            }}
          >
            <ListItemIcon
              sx={{
                minWidth: 40,
                color:
                  location.pathname === "/" ? colors.primary.main : "inherit",
              }}
            >
              <HomeIcon />
            </ListItemIcon>
            <ListItemText primary="Inicio" />
          </ListItem>

          <ListItem
            button="true"
            component={RouterLink}
            to="/products"
            onClick={handleDrawerToggle}
            selected={
              location.pathname === "/products" &&
              !location.search.includes("categoryId")
            }
            sx={{
              py: 1.5,
              borderBottom: `1px solid ${theme.palette.divider}`,
              "&.Mui-selected": {
                backgroundColor: alpha(colors.primary.main, 0.08),
                "& .MuiListItemIcon-root, & .MuiListItemText-primary": {
                  color: colors.primary.main,
                  fontWeight: 500,
                },
              },
              "&:hover": {
                backgroundColor: alpha(colors.primary.main, 0.04),
              },
            }}
          >
            <ListItemIcon
              sx={{
                minWidth: 40,
                color:
                  location.pathname === "/products" &&
                    !location.search.includes("categoryId")
                    ? colors.primary.main
                    : "inherit",
              }}
            >
              <AppsIcon />
            </ListItemIcon>
            <ListItemText primary="Todos los productos" />
          </ListItem>

          {/* Separator for categories if needed */}
          {dynamicDrawerCategories.length > 0 && (
            <Box
              sx={{
                pt: 1,
                mt: 1,
                borderTop: `1px solid ${theme.palette.divider}`,
              }}
            >
              <Typography
                variant="overline"
                sx={{
                  pl: 2,
                  display: "block",
                  color: "text.secondary",
                  fontSize: "0.65rem",
                  letterSpacing: "0.075em",
                }}
              >
                CATEGORÍAS
              </Typography>
            </Box>
          )}

          {dynamicDrawerCategories.length > 0
            ? dynamicDrawerCategories.map((item) => {
              const isActive =
                location.pathname === "/products" &&
                location.search === `?categoryId=${item.id}`;
              return (
                <ListItem
                  button="true"
                  key={item.id}
                  component={RouterLink}
                  to={item.path}
                  onClick={handleDrawerToggle}
                  selected={isActive}
                  sx={{
                    py: 1.2,
                    pl: isActive ? 1.5 : 2,
                    "&.Mui-selected": {
                      backgroundColor: alpha(colors.primary.main, 0.08),
                      borderLeft: `4px solid ${colors.primary.main}`,
                      "& .MuiListItemIcon-root, & .MuiListItemText-primary": {
                        color: colors.primary.main,
                        fontWeight: 500,
                      },
                    },
                    "&:hover": {
                      backgroundColor: alpha(colors.primary.main, 0.04),
                    },
                  }}
                >
                  <ListItemIcon sx={{ minWidth: 40 }}>
                    {React.cloneElement(item.icon, {
                      sx: {
                        color: isActive
                          ? colors.primary.main
                          : theme.palette.action.active,
                      },
                    })}
                  </ListItemIcon>
                  <ListItemText primary={item.text} />
                </ListItem>
              );
            })
            : !categoryLoading && (
              <ListItem sx={{ py: 1.5 }}>
                <ListItemText
                  primary="No hay categorías disponibles."
                  sx={{ textAlign: "center", color: "text.secondary" }}
                />
              </ListItem>
            )}
        </List>
      )}
    </Box>
  );

  /**
   * Handles clicking the profile icon, navigating to the profile page
   * if authenticated, or the login page otherwise. Closes the drawer if open.
   */
  const handleProfileClick = () => {
    if (mobileOpen) setMobileOpen(false);
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
        <Toolbar sx={{ minHeight: { xs: 70, sm: 80 } }}>
          <MuiLink
            component={RouterLink}
            to="/"
            sx={{
              display: "flex",
              alignItems: "center",
              mr: 1,
              textDecoration: "none",
            }}
            onClick={() => mobileOpen && setMobileOpen(false)}
          >
            <img
              src={logo}
              alt="IVAY Logo"
              style={{
                height: isMobile ? 50 : 60,
                width: "auto",
              }}
            />
          </MuiLink>

          <Box sx={{ display: "flex", alignItems: "center", mr: "auto" }}>
            <IconButton
              color="inherit"
              aria-label="open drawer"
              edge="start"
              onClick={handleDrawerToggle}
              sx={{ mr: isMobile ? 0.5 : 1 }}
            >
              <MenuIcon />
            </IconButton>
            {!isMobile && (
              <Typography
                variant="button"
                component="div"
                sx={{
                  cursor: "pointer",
                  "&:hover": { color: theme.palette.primary.main },
                  whiteSpace: "nowrap",
                }}
                onClick={handleDrawerToggle}
              >
                Todas las Categorías
              </Typography>
            )}
          </Box>

          <Box
            sx={{
              flexGrow: 1,
              display: "flex",
              justifyContent: "center",
              px: { xs: 0.5, sm: 2 },
            }}
          >
            <Search sx={{ maxWidth: 600, width: "100%" }}>
              <StyledInputBase
                placeholder="Buscar productos..."
                inputProps={{ "aria-label": "search" }}
                value={searchTerm}
                onChange={handleSearchChange}
                onKeyDown={handleSearchSubmit}
              />
              <IconButton
                type="button"
                size="small"
                sx={{
                  p: "6px",
                  position: "absolute",
                  right: 5,
                  top: "50%",
                  transform: "translateY(-50%)",
                  color: theme.palette.primary.main,
                }}
                onClick={handleSearchSubmit}
                aria-label="search"
              >
                <SearchIcon />
              </IconButton>
            </Search>
          </Box>

          <Box
            sx={{
              display: "flex",
              alignItems: "center",
              ml: { xs: 0.5, sm: 1 },
            }}
          >
            <IconButton
              color="inherit"
              onClick={handleProfileClick}
              sx={{
                p: isMobile ? 0.75 : 1,
                display: "flex",
                flexDirection: "column",
                alignItems: "center",
              }}
            >
              <PersonOutlineIcon />
              {!isMobile && (
                <Typography
                  variant="caption"
                  sx={{ lineHeight: 1.1, mt: 0.1, fontSize: "0.65rem" }}
                >
                  {isAuthenticated ? "Mi perfil" : "Iniciar Sesión"}
                </Typography>
              )}
            </IconButton>
            <IconButton
              color="inherit"
              component={RouterLink}
              to="/cart"
              onClick={() => mobileOpen && setMobileOpen(false)}
              sx={{
                p: isMobile ? 0.75 : 1,
                display: "flex",
                flexDirection: "column",
                alignItems: "center",
                ml: { xs: 0.25, sm: 0.5 },
              }}
            >
              <ShoppingCartOutlinedIcon />
              {!isMobile && (
                <Typography
                  variant="caption"
                  sx={{ lineHeight: 1.1, mt: 0.1, fontSize: "0.65rem" }}
                >
                  Carrito
                </Typography>
              )}
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