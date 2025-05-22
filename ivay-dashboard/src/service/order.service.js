import axios from "axios";
import { API_BASE_URL } from "../constants/api";

/**
 * Base URL for order related API endpoints.
 */
const ORDER_API_URL = `${API_BASE_URL}/orders`;

/**
 * Base URL for user related API endpoints, used for user-specific order actions.
 */
const USER_API_URL = `${API_BASE_URL}/users`;

/**
 * Fetches all orders from the API.
 * @returns {Promise<Array<Object>>} Array of order response DTOs.
 */
const getAllOrders = async () => {
  const response = await axios.get(ORDER_API_URL);
  return response.data.data;
};

/**
 * Fetches all orders belonging to a specific user.
 * @param {number|string} userId - The ID of the user.
 * @returns {Promise<Array<Object>>} Array of order response DTOs for the user.
 */
const getOrdersByUserId = async (userId) => {
  const response = await axios.get(`${USER_API_URL}/${userId}/orders`);
  return response.data.data;
};

/**
 * Fetches a specific order by its ID.
 * @param {number|string} orderId - The ID of the order.
 * @returns {Promise<Object>} Order response DTO.
 */
const getOrderById = async (orderId) => {
  const response = await axios.get(`${ORDER_API_URL}/${orderId}`);
  return response.data.data;
};

/**
 * Fetches all items associated with a specific order.
 * @param {number|string} orderId - The ID of the order.
 * @returns {Promise<Array<Object>>} Array of order item response DTOs.
 */
const getOrderItemsByOrderId = async (orderId) => {
  const response = await axios.get(`${ORDER_API_URL}/${orderId}/items`);
  return response.data.data;
};

/**
 * Creates a new order. This typically involves details like user ID,
 * shipping address, payment info, and the items being ordered.
 * @param {Object} createOrderRequestDto - The data required to create an order.
 * @returns {Promise<Object>} The created order response DTO.
 */
const createOrder = async (createOrderRequestDto) => {
  const response = await axios.post(ORDER_API_URL, createOrderRequestDto);
  return response.data.data;
};

/**
 * Updates an existing order by its ID.
 * @param {number|string} orderId - The ID of the order to update.
 * @param {Object} updateOrderDto - The updated order data.
 * @returns {Promise<Object>} The updated order response DTO.
 */
const updateOrder = async (orderId, updateOrderDto) => {
  const response = await axios.put(
    `${ORDER_API_URL}/${orderId}`,
    updateOrderDto
  );
  return response.data.data;
};

/**
 * Deletes an order by its ID.
 * @param {number|string} orderId - The ID of the order to delete.
 * @returns {Promise<void>}
 */
const deleteOrder = async (orderId) => {
  await axios.delete(`${ORDER_API_URL}/${orderId}`);
};

/**
 * Service object for interacting with Order related API endpoints.
 */
const OrderService = {
  getAllOrders,
  getOrdersByUserId,
  getOrderById,
  getOrderItemsByOrderId,
  createOrder,
  updateOrder,
  deleteOrder,
};

export default OrderService;