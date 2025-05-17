import React, { useState, useEffect } from "react";
import {
  Modal,
  Box,
  Typography,
  TextField,
  Button,
  Grid,
  Select,
  MenuItem,
  FormControl,
  InputLabel,
  IconButton,
  CircularProgress,
  Alert,
  Paper,
} from "@mui/material";
import CloseIcon from "@mui/icons-material/Close";
import PhotoCamera from "@mui/icons-material/PhotoCamera";
import { styled } from "@mui/material/styles";
import { colors } from "../../constants/styles";
import CategoryService from "../../service/category.service";
import CloudinaryService from "../../service/cloudinary.service";

const VisuallyHiddenInput = styled("input")({
  clip: "rect(0 0 0 0)",
  clipPath: "inset(50%)",
  height: 1,
  overflow: "hidden",
  position: "absolute",
  bottom: 0,
  left: 0,
  whiteSpace: "nowrap",
  width: 1,
});

const modalStyle = {
  position: "absolute",
  top: "50%",
  left: "50%",
  transform: "translate(-50%, -50%)",
  width: { xs: "95%", sm: "80%", md: "1000px" },
  maxHeight: "90vh",
  bgcolor: "background.paper",
  boxShadow: 24,
  borderRadius: 2,
  outline: "none",
  display: "flex",
  flexDirection: "column",
};

const ProductFormModal = ({
  open,
  onClose,
  onSubmit,
  productToEdit,
  isSubmitting: parentIsSubmitting,
  serverError,
}) => {
  const initialFormState = {
    name: "",
    description: "",
    price: "",
    stock: "",
    categoryId: "",
    imageUrl: "",
    discount: "",
  };

  const [formData, setFormData] = useState(initialFormState);
  const [imagePreview, setImagePreview] = useState(null);
  const [categories, setCategories] = useState([]);
  const [categoryLoading, setCategoryLoading] = useState(true);
  const [internalError, setInternalError] = useState("");
  const [isUploading, setIsUploading] = useState(false);

  useEffect(() => {
    const fetchCategories = async () => {
      if (!open) return;
      setCategoryLoading(true);
      try {
        const fetchedCategories = await CategoryService.getAllCategories();
        setCategories(fetchedCategories || []);
      } catch (error) {
        console.error("Error fetching categories for form:", error);
        setInternalError("No se pudieron cargar las categorías.");
      } finally {
        setCategoryLoading(false);
      }
    };
    fetchCategories();
  }, [open]);

  useEffect(() => {
    if (open) {
      if (productToEdit) {
        setFormData({
          name: productToEdit.name || "",
          description: productToEdit.description || "",
          price: productToEdit.price?.toString() || "",
          stock: productToEdit.stock?.toString() || "",
          categoryId:
            productToEdit.categoryId?.toString() ||
            productToEdit.category?.id?.toString() ||
            "",
          imageUrl: productToEdit.imageUrl || "",
          discount: productToEdit.discount
            ? (productToEdit.discount * 100).toString()
            : "",
        });
        setImagePreview(productToEdit.imageUrl || null);
      } else {
        setFormData(initialFormState);
        setImagePreview(null);
      }
      setInternalError("");
      setIsUploading(false);
    }
  }, [productToEdit, open]);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
  };

  const handleImageChangeAndUpload = async (e) => {
    if (e.target.files && e.target.files[0]) {
      const file = e.target.files[0];
      setImagePreview(URL.createObjectURL(file));
      setIsUploading(true);
      setInternalError("");

      try {
        const cloudinaryResponse = await CloudinaryService.uploadImage(file);
        setFormData((prev) => ({
          ...prev,
          imageUrl: cloudinaryResponse.secure_url,
        }));
        setImagePreview(cloudinaryResponse.secure_url);
        setInternalError("");
      } catch (error) {
        setInternalError(
          error.message || "Error al subir la imagen. Intente de nuevo."
        );
        setImagePreview(formData.imageUrl || productToEdit?.imageUrl || null);
      } finally {
        setIsUploading(false);
      }
    }
  };

  const validateForm = () => {
    if (!formData.name.trim()) return "El nombre es obligatorio.";
    if (isNaN(parseFloat(formData.price)) || parseFloat(formData.price) < 0)
      return "El precio debe ser un número no negativo.";
    if (isNaN(parseInt(formData.stock)) || parseInt(formData.stock) < 0)
      return "La cantidad debe ser un número no negativo.";
    if (formData.discount) {
      const discountValue = parseFloat(formData.discount);
      if (isNaN(discountValue) || discountValue < 0 || discountValue > 100) {
        return "El descuento debe ser un número entre 0 y 100.";
      }
    }
    if (!formData.categoryId) return "Debe seleccionar una categoría.";
    if (!formData.imageUrl) return "La imagen del producto es obligatoria.";
    return "";
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    const validationError = validateForm();
    if (validationError) {
      setInternalError(validationError);
      return;
    }
    if (isUploading) {
      setInternalError("Espere a que la imagen termine de subirse.");
      return;
    }
    setInternalError("");

    let discountDecimal = 0;
    if (formData.discount) {
      const discountValue = parseFloat(formData.discount);
      if (!isNaN(discountValue) && discountValue >= 0 && discountValue <= 100) {
        discountDecimal = discountValue / 100;
      }
    }

    const submissionData = {
      name: formData.name,
      description: formData.description,
      price: parseFloat(formData.price),
      stock: parseInt(formData.stock),
      categoryId: parseInt(formData.categoryId),
      imageUrl: formData.imageUrl,
      discount: discountDecimal,
    };
    const id = productToEdit ? productToEdit.id : undefined;
    onSubmit(submissionData, id);
  };

  const title = productToEdit ? "Editar Producto" : "Nuevo Producto";
  const currentOverallSubmitting = parentIsSubmitting || isUploading;

  return (
    <Modal
      open={open}
      onClose={onClose}
      aria-labelledby="product-form-modal-title"
    >
      <Paper sx={modalStyle}>
        <Box
          sx={{
            p: 2,
            display: "flex",
            justifyContent: "space-between",
            alignItems: "center",
            borderBottom: `1px solid ${colors.grey[300]}`,
          }}
        >
          <Typography
            id="product-form-modal-title"
            variant="h6"
            component="h2"
            fontWeight="bold"
          >
            {title}
          </Typography>
          <IconButton
            onClick={onClose}
            aria-label="close"
            disabled={currentOverallSubmitting}
          >
            <CloseIcon />
          </IconButton>
        </Box>

        <Box
          component="form"
          onSubmit={handleSubmit}
          sx={{ p: { xs: 1.5, sm: 2.5 }, overflowY: "auto", flexGrow: 1 }}
        >
          {(internalError || serverError) && (
            <Alert severity="error" sx={{ mb: 2 }}>
              {internalError || serverError}
            </Alert>
          )}
          <Grid container spacing={{ xs: 1.5, sm: 2.5 }}>
            <Grid item xs={12} md={8}>
              <TextField
                fullWidth
                label="Nombre del producto"
                name="name"
                value={formData.name}
                onChange={handleChange}
                margin="dense"
                required
                disabled={currentOverallSubmitting}
              />
              <TextField
                fullWidth
                label="Descripción del producto"
                name="description"
                value={formData.description}
                onChange={handleChange}
                margin="dense"
                multiline
                rows={3}
                disabled={currentOverallSubmitting}
              />

              <Grid container spacing={{ xs: 1, sm: 2 }}>
                <Grid item xs={12} sm={4}>
                  <TextField
                    fullWidth
                    label="Precio €"
                    name="price"
                    type="number"
                    value={formData.price}
                    onChange={handleChange}
                    margin="dense"
                    required
                    inputProps={{ step: "0.01", min: "0" }}
                    disabled={currentOverallSubmitting}
                  />
                </Grid>
                <Grid item xs={12} sm={4}>
                  <TextField
                    fullWidth
                    label="Stock"
                    name="stock"
                    type="number"
                    value={formData.stock}
                    onChange={handleChange}
                    margin="dense"
                    required
                    inputProps={{ min: "0" }}
                    disabled={currentOverallSubmitting}
                  />
                </Grid>
                <Grid item xs={12} sm={4}>
                  <TextField
                    fullWidth
                    label="Descuento %"
                    name="discount"
                    type="number"
                    value={formData.discount}
                    onChange={handleChange}
                    margin="dense"
                    placeholder="Ej: 10 para 10%"
                    inputProps={{ min: "0", max: "100", step: "1" }}
                    disabled={currentOverallSubmitting}
                    helperText="0 para sin descuento"
                  />
                </Grid>
              </Grid>
              <FormControl
                fullWidth
                margin="dense"
                required
                disabled={currentOverallSubmitting || categoryLoading}
              >
                <InputLabel id="category-select-label">Categoría</InputLabel>
                <Select
                  labelId="category-select-label"
                  name="categoryId"
                  value={formData.categoryId}
                  label="Categoría"
                  onChange={handleChange}
                >
                  {categoryLoading && (
                    <MenuItem value="" disabled>
                      <CircularProgress size={20} sx={{ mr: 1 }} />
                      Cargando...
                    </MenuItem>
                  )}
                  {!categoryLoading && categories.length === 0 && (
                    <MenuItem value="" disabled>
                      No hay categorías
                    </MenuItem>
                  )}
                  {!categoryLoading &&
                    categories.map((cat) => (
                      <MenuItem key={cat.id} value={cat.id.toString()}>
                        {cat.name}
                      </MenuItem>
                    ))}
                </Select>
              </FormControl>
            </Grid>

            <Grid
              item
              xs={12}
              md={4}
              sx={{
                display: "flex",
                flexDirection: "column",
                alignItems: "center",
                justifyContent: "flex-start",
                mt: { xs: 1, md: 0.5 },
                ml: 8,
              }}
            >
              <Box
                sx={{
                  width: "100%",
                  maxWidth: "250px",
                  aspectRatio: "1 / 1",
                  border: `2px dashed ${colors.grey[400]}`,
                  borderRadius: 1,
                  display: "flex",
                  alignItems: "center",
                  justifyContent: "center",
                  mb: 1,
                  overflow: "hidden",
                  position: "relative",
                }}
              >
                {isUploading ? (
                  <Box sx={{ textAlign: "center" }}>
                    {" "}
                    <CircularProgress size={40} />{" "}
                    <Typography
                      variant="caption"
                      display="block"
                      sx={{ mt: 1 }}
                    >
                      Subiendo...
                    </Typography>{" "}
                  </Box>
                ) : imagePreview ? (
                  <img
                    src={imagePreview}
                    alt="Previsualización"
                    style={{
                      width: "100%",
                      height: "100%",
                      objectFit: "contain",
                    }}
                  />
                ) : (
                  <Typography
                    variant="caption"
                    color="text.secondary"
                    sx={{ textAlign: "center", p: 1 }}
                  >
                    {" "}
                    Previsualización de imagen{" "}
                  </Typography>
                )}
              </Box>
              <Button
                component="label"
                role={undefined}
                variant="outlined"
                tabIndex={-1}
                startIcon={<PhotoCamera />}
                fullWidth
                sx={{ maxWidth: "250px" }}
                disabled={currentOverallSubmitting}
              >
                Subir imagen
                <VisuallyHiddenInput
                  type="file"
                  accept="image/*"
                  onChange={handleImageChangeAndUpload}
                />
              </Button>
              {formData.imageUrl && !isUploading && (
                <Typography
                  variant="caption"
                  sx={{
                    mt: 0.5,
                    wordBreak: "break-all",
                    textAlign: "center",
                    maxWidth: "250px",
                  }}
                  color="text.secondary"
                ></Typography>
              )}
            </Grid>
          </Grid>
        </Box>

        <Box
          sx={{
            p: 2,
            display: "flex",
            justifyContent: "flex-end",
            alignItems: "center",
            borderTop: `1px solid ${colors.grey[300]}`,
            gap: 1.5,
          }}
        >
          <Button
            onClick={onClose}
            variant="outlined"
            color="inherit"
            disabled={currentOverallSubmitting}
          >
            Cancelar
          </Button>
          <Button
            type="submit"
            variant="contained"
            onClick={handleSubmit}
            disabled={currentOverallSubmitting}
            sx={{
              backgroundColor: colors.primary.main,
              "&:hover": { backgroundColor: colors.primary.dark },
            }}
          >
            {currentOverallSubmitting ? (
              <CircularProgress size={24} color="inherit" />
            ) : (
              "Confirmar"
            )}
          </Button>
        </Box>
      </Paper>
    </Modal>
  );
};

export default ProductFormModal;
