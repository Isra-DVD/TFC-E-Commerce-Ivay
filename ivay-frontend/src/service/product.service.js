import axios from "axios";
import { API_BASE_URL } from "../constants/api";

/**
 * Base URL for product related API endpoints.
 */
const PRODUCT_API_URL = `${API_BASE_URL}/products`;

/**
 * Fetches all products from the API.
 * @returns {Promise<Array<Object>>} Array of product response DTOs.
 */
const getAllProducts = async () => {
  const response = await axios.get(PRODUCT_API_URL);
  return response.data.data;
};

/**
 * Finds products by name using a filter endpoint.
 * @param {string} name - The name or part of the name to search for.
 * @returns {Promise<Array<Object>>} Array of product response DTOs matching the name.
 */
const findProductsByName = async (name) => {
  const response = await axios.get(`${PRODUCT_API_URL}/filter`, {
    params: { name },
  });
  return response.data.data;
};

/**
 * Fetches a specific product by its ID.
 * @param {number|string} productId - The ID of the product.
 * @returns {Promise<Object>} Product response DTO.
 */
const getProductById = async (productId) => {
  const response = await axios.get(`${PRODUCT_API_URL}/${productId}`);
  return response.data.data;
};

/**
 * Creates a new product.
 * @param {Object} productRequestDto - The data required to create a product.
 * @returns {Promise<Object>} The created product response DTO.
 */
const createProduct = async (productRequestDto) => {
  const response = await axios.post(PRODUCT_API_URL, productRequestDto);
  return response.data.data;
};

/**
 * Updates an existing product by its ID.
 * @param {number|string} productId - The ID of the product to update.
 * @param {Object} productRequestDto - The updated product data. Same structure as createProductRequestDto.
 * @returns {Promise<Object>} The updated product response DTO.
 */
const updateProduct = async (productId, productRequestDto) => {
  const response = await axios.put(
    `${PRODUCT_API_URL}/${productId}`,
    productRequestDto
  );
  return response.data.data;
};

/**
 * Deletes a product by its ID.
 * @param {number|string} productId - The ID of the product to delete.
 * @returns {Promise<void>}
 */
const deleteProduct = async (productId) => {
  await axios.delete(`${PRODUCT_API_URL}/${productId}`);
};

/**
 * Fetches all order items associated with a specific product.
 * @param {number|string} productId - The ID of the product.
 * @returns {Promise<Array<Object>>} Array of order item response DTOs.
 */
const getOrderItemsByProductId = async (productId) => {
  const response = await axios.get(
    `${PRODUCT_API_URL}/${productId}/order-items`
  );
  return response.data.data;
};

/**
 * Fetches all cart items associated with a specific product.
 * @param {number|string} productId - The ID of the product.
 * @returns {Promise<Array<Object>>} Array of cart item response DTOs.
 */
const getCartItemsByProductId = async (productId) => {
  const response = await axios.get(
    `${PRODUCT_API_URL}/${productId}/cart-items`
  );
  return response.data.data;
};

/**
 * Fetches products with pagination.
 * @param {number} [page=0] - The page number (0-indexed).
 * @param {number} [size=20] - The number of items per page.
 * @returns {Promise<Object>} A pagination object containing an array of product response DTOs and pagination info (like total elements, total pages, etc.).
 */
const getProductsPaginated = async (page = 0, size = 20) => {
  const response = await axios.get(`${PRODUCT_API_URL}/paginated`, {
    params: { page, size },
  });
  return response.data.data;
};

/**
 * Service object for interacting with Product related API endpoints.
 */
const ProductService = {
  getAllProducts,
  findProductsByName,
  getProductById,
  createProduct,
  updateProduct,
  deleteProduct,
  getOrderItemsByProductId,
  getCartItemsByProductId,
  getProductsPaginated,
};

export default ProductService;