import axios from "axios";

const CLOUDINARY_CLOUD_NAME = import.meta.env.VITE_CLOUDINARY_CLOUD_NAME;
const CLOUDINARY_UPLOAD_PRESET = import.meta.env.VITE_CLOUDINARY_UPLOAD_PRESET;
const CLOUDINARY_UPLOAD_URL = import.meta.env.VITE_CLOUDINARY_UPLOAD_URL;

const cloudinaryApi = axios.create({});

/**
 * Uploads an image file to Cloudinary using an unsigned upload preset.
 * @param {File} file - The image file to upload.
 * @param {string} uploadPreset - Your Cloudinary unsigned upload preset name.
 * @returns {Promise<object>} A promise that resolves to the Cloudinary upload response object.
 *                            Typically includes 'secure_url', 'public_id', etc.
 * @throws {Error} If the upload fails.
 */
const uploadImage = async (file) => {
  const formData = new FormData();
  formData.append("file", file);
  formData.append("upload_preset", CLOUDINARY_UPLOAD_PRESET);

  try {
    const response = await cloudinaryApi.post(CLOUDINARY_UPLOAD_URL, formData, {
      headers: {
        "Content-Type": "multipart/form-data",
      },
    });
    return response.data;
  } catch (error) {
    console.error(
      "Cloudinary Service Upload Error:",
      error.response?.data || error.message,
      error
    );
    throw new Error(
      error.response?.data?.error?.message || "Cloudinary image upload failed."
    );
  }
};

const CloudinaryService = {
  uploadImage,
};

export default CloudinaryService;
