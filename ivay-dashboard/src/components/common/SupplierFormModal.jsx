import React, { useState, useEffect } from "react";
import {
  Modal,
  Box,
  Typography,
  TextField,
  Button,
  Grid,
  IconButton,
  CircularProgress,
  Alert,
  Paper,
} from "@mui/material";
import CloseIcon from "@mui/icons-material/Close";
import PhotoCamera from "@mui/icons-material/PhotoCamera";
import { styled } from "@mui/material/styles";
import { colors } from "../../constants/styles";
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
  width: { xs: "95%", sm: "80%", md: "800px" },
  maxHeight: "90vh",
  bgcolor: "background.paper",
  boxShadow: 24,
  borderRadius: 2,
  outline: "none",
  display: "flex",
  flexDirection: "column",
};

const SupplierFormModal = ({
  open,
  onClose,
  onSubmit,
  supplierToEdit,
  isSubmitting: parentIsSubmitting,
  serverError,
}) => {
  const initialFormState = {
    name: "",
    address: "",
    email: "",
    phone: "",
    z: "",
  };

  const [formData, setFormData] = useState(initialFormState);
  const [imagePreview, setImagePreview] = useState(null);
  const [internalError, setInternalError] = useState("");
  const [isUploading, setIsUploading] = useState(false);

  useEffect(() => {
    if (open) {
      if (supplierToEdit) {
        setFormData({
          name: supplierToEdit.name || "",
          address: supplierToEdit.address || "",
          email: supplierToEdit.email || "",
          phone: supplierToEdit.phone || "",
          imageUrl: supplierToEdit.imageUrl || "",
        });
        setImagePreview(supplierToEdit.imageUrl || null);
      } else {
        setFormData(initialFormState);
        setImagePreview(null);
      }
      setInternalError("");
      setIsUploading(false);
    }
  }, [supplierToEdit, open]);

  const handleChange = (e) => {
    setFormData((prev) => ({ ...prev, [e.target.name]: e.target.value }));
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
      } catch (error) {
        setInternalError(error.message || "Error al subir la imagen.");
        setImagePreview(formData.imageUrl || supplierToEdit?.imageUrl || null);
      } finally {
        setIsUploading(false);
      }
    }
  };

  const validateForm = () => {
    if (!formData.name.trim()) return "El nombre del proveedor es obligatorio.";
    if (!formData.email.trim()) return "El email es obligatorio.";
    else if (!/\S+@\S+\.\S+/.test(formData.email))
      return "El formato del email no es válido.";
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
    const id = supplierToEdit ? supplierToEdit.id : undefined;
    onSubmit(formData, id);
  };

  const title = supplierToEdit ? "Editar Proveedor" : "Nuevo Proveedor";
  const currentOverallSubmitting = parentIsSubmitting || isUploading;

  return (
    <Modal
      open={open}
      onClose={onClose}
      aria-labelledby="supplier-form-modal-title"
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
            id="supplier-form-modal-title"
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
            {/* Left Column: Form Fields */}
            <Grid item xs={12} md={8}>
              <TextField
                fullWidth
                label="Nombre del proveedor"
                name="name"
                value={formData.name}
                onChange={handleChange}
                margin="dense"
                required
                disabled={currentOverallSubmitting}
                autoFocus
              />
              <TextField
                fullWidth
                label="Dirección del proveedor"
                name="address"
                value={formData.address}
                onChange={handleChange}
                margin="dense"
                multiline
                rows={2}
                disabled={currentOverallSubmitting}
              />
              <Grid container spacing={{ xs: 1, sm: 2 }}>
                <Grid item xs={12} sm={6}>
                  <TextField
                    fullWidth
                    label="Teléfono del proveedor"
                    name="phone"
                    value={formData.phone}
                    onChange={handleChange}
                    margin="dense"
                    disabled={currentOverallSubmitting}
                  />
                </Grid>
                <Grid item xs={12} sm={6}>
                  <TextField
                    fullWidth
                    label="Email del proveedor"
                    name="email"
                    type="email"
                    value={formData.email}
                    onChange={handleChange}
                    margin="dense"
                    required
                    disabled={currentOverallSubmitting}
                  />
                </Grid>
              </Grid>
            </Grid>

            {/* Right Column: Image Upload */}
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
                ml: 4,
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
                    Subir imagen (Opcional){" "}
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
          {supplierToEdit && (
            <Button
              variant="outlined"
              color="error"
              onClick={() => {
                onClose();
              }}
              disabled={currentOverallSubmitting}
            >
              Eliminar
            </Button>
          )}
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

export default SupplierFormModal;
