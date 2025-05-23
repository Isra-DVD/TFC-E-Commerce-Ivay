import React from "react";
import {
  Card,
  CardContent,
  CardMedia,
  Typography,
  Box,
  IconButton,
  Menu,
  MenuItem,
  ListItemIcon,
  ListItemText,
} from "@mui/material";
import MoreVertIcon from "@mui/icons-material/MoreVert";
import EditIcon from "@mui/icons-material/Edit";
import DeleteIcon from "@mui/icons-material/Delete";
import { colors } from "../../constants/styles";
import {
  AdvancedImage,
  responsive,
  placeholder,
  lazyload,
} from "@cloudinary/react";
import { Cloudinary } from "@cloudinary/url-gen";
import { fill } from "@cloudinary/url-gen/actions/resize";
import ProductPlaceholder from "../../assets/images/product-placeholder.png";

/* Initializes the Cloudinary service with the cloud name. */
const cld = new Cloudinary({
  cloud: {
    cloudName: import.meta.env.VITE_CLOUDINARY_CLOUD_NAME,
  },
});

/**
 * Displays a card representing a product, including its image, name, price, stock,
 * and options to edit or delete via a context menu.
 *
 * @param {object} props - The component props.
 * @param {object} props.product - The product object to display.
 * @param {function} [props.onEdit] - Callback function to execute when the "Edit" action is selected.
 * @param {function} [props.onDelete] - Callback function to execute when the "Delete" action is selected.
 */
const ProductCard = ({ product, onEdit, onDelete }) => {
  const [anchorEl, setAnchorEl] = React.useState(null);
  const open = Boolean(anchorEl);

  /**
   * Opens the options menu anchored to the click event target.
   *
   * @param {object} event - The click event.
   */
  const handleClick = (event) => {
    event.stopPropagation();
    setAnchorEl(event.currentTarget);
  };

  /**
   * Closes the options menu.
   */
  const handleClose = () => {
    setAnchorEl(null);
  };

  /**
   * Executes the onEdit callback if provided and closes the menu.
   */
  const handleEdit = () => {
    if (onEdit) onEdit();
    handleClose();
  };

  /**
   * Executes the onDelete callback if provided and closes the menu.
   */
  const handleDelete = () => {
    if (onDelete) onDelete();
    handleClose();
  };

  /* Renders a placeholder card if the product data is missing or invalid. */
  if (!product || !product.id) {
    return (
      <Card
        sx={{
          p: 2,
          height: { xs: "auto", sm: 300, md: 250 },
          width: { xs: "100%", sm: 100, md: 170 },
          display: "flex",
          alignItems: "center",
          justifyContent: "center",
          border: `1px solid ${colors.error?.main || "red"}`,
        }}
      >
        <Typography color="error">Inválido</Typography>
      </Card>
    );
  }

  /* Processes the product's imageUrl to extract the public ID for Cloudinary AdvancedImage component. */
  let cldImage;
  if (product.imageUrl) {
    try {
      const parts = product.imageUrl.split("/");
      const uploadIndex = parts.indexOf("upload");
      if (uploadIndex !== -1 && parts.length > uploadIndex + 1) {
        let publicIdPathParts = [];
        for (let i = uploadIndex + 1; i < parts.length; i++) {
          if (
            parts[i].startsWith("v") &&
            Number.isInteger(Number(parts[i].substring(1)))
          ) {
            if (i + 1 < parts.length) continue;
          }
          publicIdPathParts.push(parts[i]);
        }
        let publicIdWithFolderAndExtension = publicIdPathParts.join("/");
        const publicId = publicIdWithFolderAndExtension.substring(
          0,
          publicIdWithFolderAndExtension.lastIndexOf(".")
        );

        if (publicId) {
          cldImage = cld.image(publicId);
          cldImage.resize(fill().width(300).height(225).gravity("auto"));
        } else {
          console.warn(
            "Could not extract public ID from Cloudinary URL:",
            product.imageUrl
          );
        }
      } else {
        console.warn(
          "Cloudinary URL does not seem to contain 'upload' segment or public_id:",
          product.imageUrl
        );
      }
    } catch (e) {
      console.warn(
        "Error parsing Cloudinary URL for AdvancedImage:",
        product.imageUrl,
        e
      );
    }
  }

  return (
    <Card
      sx={{
        height: { xs: "auto", sm: 300, md: 260 },
        width: { xs: "100%", sm: 100, md: 170 },
        position: "relative",
        display: "flex",
        flexDirection: "column",
        minHeight: "100%",
        minWidth: "100%",
        border: `1px solid ${colors.grey[300]}`,
        transition: "box-shadow 0.3s ease-in-out, transform 0.2s ease-in-out",
        "&:hover": {
          boxShadow: `0 4px 12px rgba(0,0,0,0.08)`,
          transform: "translateY(-2px)",
        },
        overflow: "hidden",
      }}
    >
      <IconButton
        aria-label="actions"
        size="small"
        onClick={handleClick}
        sx={{
          position: "absolute",
          top: 4,
          right: 4,
          zIndex: 2,
          backgroundColor: "rgba(255,255,255,0.5)",
          "&:hover": { backgroundColor: "rgba(255,255,255,0.8)" },
        }}
      >
        <MoreVertIcon fontSize="small" />
      </IconButton>

      <Box
        sx={{
          minWidth: "100%",
          paddingTop: "75%",
          backgroundColor: colors.grey[100],
          overflow: "hidden",
          position: "relative",
        }}
      >
        {cldImage ? (
          /* Renders the image using Cloudinary AdvancedImage if public ID was extracted. */
          <AdvancedImage
            cldImg={cldImage}
            plugins={[
              lazyload(),
              responsive({ steps: [200, 300, 400] }),
              placeholder({ mode: "blur" }),
            ]}
            style={{
              position: "absolute",
              top: 0,
              left: 0,
              width: "100%",
              height: "100%",
              objectFit: "cover",
            }}
            alt={product.name}
          />
        ) : (
          /* Renders a standard CardMedia image if Cloudinary processing fails or no image URL is provided. */
          <CardMedia
            component="img"
            sx={{
              position: "absolute",
              top: 0,
              left: 0,
              width: "100%",
              height: "100%",
              objectFit: "contain",
              p: 1,
            }}
            image={product.imageUrl || ProductPlaceholder}
            alt={product.name}
            onError={(e) => {
              e.target.onerror = null;
              e.target.src = { ProductPlaceholder };
            }}
          />
        )}
      </Box>

      <CardContent
        sx={{
          flexGrow: 1,
          p: 1.5,
          pt: 1,
          "&:last-child": { pb: 1.5 },
          display: "flex",
          flexDirection: "column",
          justifyContent: "space-between",
          overflow: "hidden",
        }}
      >
        <Box sx={{ minHeight: "4.5em" }}>
          <Typography>{product.name || "Nombre no disponible"}</Typography>
          {product.discount > 0 && (
            <Box sx={{ display: "flex", alignItems: "baseline", gap: 0.5 }}>
              <Typography
                variant="body2"
                color="text.secondary"
                component="div"
                noWrap
                sx={{ textDecoration: "line-through" }}
              >
                €{product.price?.toFixed(2)}
              </Typography>
              <Typography
                variant="h6"
                color="primary.main"
                component="div"
                sx={{ lineHeight: 1.2, fontWeight: "bold" }}
              >
                €{(product.price * (1 - product.discount)).toFixed(2)}
              </Typography>
              <Typography
                variant="caption"
                color="primary.light"
                sx={{ fontWeight: "bold" }}
              >
                (-{(product.discount * 100).toFixed(0)}%)
              </Typography>
            </Box>
          )}
          {!(product.discount > 0) && (
            <Typography
              variant="h6"
              color="primary.main"
              component="div"
              sx={{ lineHeight: 1.2, fontWeight: "bold" }}
            >
              €{product.price?.toFixed(2) ?? "N/A"}
            </Typography>
          )}
          <Typography
            variant="body2"
            color="text.secondary"
            component="div"
            noWrap
          >
            Stock: {product.stock ?? "N/A"} unidades
          </Typography>
        </Box>
        <Box sx={{ height: "24px" }} />
      </CardContent>

      <Menu anchorEl={anchorEl} open={open} onClose={handleClose}>
        <MenuItem onClick={handleEdit}>
          {" "}
          <ListItemIcon>
            <EditIcon fontSize="small" />
          </ListItemIcon>{" "}
          <ListItemText>Editar</ListItemText>{" "}
        </MenuItem>
        <MenuItem onClick={handleDelete} sx={{ color: colors.error.main }}>
          {" "}
          <ListItemIcon>
            <DeleteIcon fontSize="small" sx={{ color: colors.error.main }} />
          </ListItemIcon>{" "}
          <ListItemText>Eliminar</ListItemText>{" "}
        </MenuItem>
      </Menu>
    </Card>
  );
};

export default ProductCard;
