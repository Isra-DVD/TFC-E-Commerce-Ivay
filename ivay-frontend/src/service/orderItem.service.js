import axios from "axios";
import { API_BASE_URL } from "../constants/api";

const ORDER_ITEM_API_URL = `${API_BASE_URL}/order-items`;

/**
 * Fetches a specific order item by its ID.
 * @param {number|string} orderItemId - The ID of the order item.
 * @returns {Promise<Object>} Order item response DTO.
 */
const getOrderItemById = async (orderItemId) => {
  const response = await axios.get(`${ORDER_ITEM_API_URL}/${orderItemId}`);
  return response.data.data;
};

/**
 * Fetches all order items for a specific order.
 * (Endpoint defined in OrderController)
 * @param {number|string} orderId - The ID of the order.
 * @returns {Promise<Array>} Array of order item response DTOs.
 */
const getOrderItemsByOrderId = async (orderId) => {
  const response = await axios.get(`${API_BASE_URL}/orders/${orderId}/items`);
  return response.data.data;
};

/**
 * Fetches all order items related to a specific product.
 * (Endpoint defined in ProductController)
 * @param {number|string} productId - The ID of the product.
 * @returns {Promise<Array>} Array of order item response DTOs.
 */
const getOrderItemsByProductId = async (productId) => {
  const response = await axios.get(
    `${API_BASE_URL}/products/${productId}/order-items`
  );
  return response.data.data;
};

const OrderItemService = {
  getOrderItemById,
  getOrderItemsByOrderId,
  getOrderItemsByProductId,
};

export default OrderItemService;
