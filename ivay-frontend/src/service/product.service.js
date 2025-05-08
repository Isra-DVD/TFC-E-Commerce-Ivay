import axios from "axios";
import { API_BASE_URL } from "../constants/api";

const PRODUCT_API_URL = `${API_BASE_URL}/products`;

const getAllProducts = async () => {
  const response = await axios.get(PRODUCT_API_URL);
  return response.data.data;
};

const findProductsByName = async (name) => {
  const response = await axios.get(`${PRODUCT_API_URL}/filter`, {
    params: { name },
  });
  return response.data.data;
};

const getProductById = async (productId) => {
  const response = await axios.get(`${PRODUCT_API_URL}/${productId}`);
  return response.data.data;
};

const createProduct = async (productRequestDto) => {
  const response = await axios.post(PRODUCT_API_URL, productRequestDto);
  return response.data.data;
};

const updateProduct = async (productId, productRequestDto) => {
  const response = await axios.put(
    `${PRODUCT_API_URL}/${productId}`,
    productRequestDto
  );
  return response.data.data;
};

const deleteProduct = async (productId) => {
  await axios.delete(`${PRODUCT_API_URL}/${productId}`);
};

const getOrderItemsByProductId = async (productId) => {
  const response = await axios.get(
    `${PRODUCT_API_URL}/${productId}/order-items`
  );
  return response.data.data;
};

const getCartItemsByProductId = async (productId) => {
  const response = await axios.get(
    `${PRODUCT_API_URL}/${productId}/cart-items`
  );
  return response.data.data;
};

const getProductsPaginated = async (page = 0, size = 20) => {
  const response = await axios.get(`${PRODUCT_API_URL}/paginated`, {
    params: { page, size },
  });
  return response.data.data;
};

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
