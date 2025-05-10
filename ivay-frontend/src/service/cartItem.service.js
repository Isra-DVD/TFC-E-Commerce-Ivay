import axios from "axios";
import { API_BASE_URL } from "../constants/api";

const CART_ITEM_API_URL = `${API_BASE_URL}/cart-items`;
const USER_API_URL = `${API_BASE_URL}/users`; // For user-specific cart endpoints

/**
 * Fetches a specific cart item by its ID.
 * @param {number|string} cartItemId - The ID of the cart item.
 * @returns {Promise<Object>} Cart item response DTO.
 */
const getCartItemById = async (cartItemId) => {
  const response = await axios.get(`${CART_ITEM_API_URL}/${cartItemId}`);
  return response.data.data;
};

/**
 * Fetches all cart items for a specific user.
 * @param {number|string} userId - The ID of the user.
 * @returns {Promise<Array>} Array of cart item response DTOs.
 */
const getCartItemsByUserId = async (userId) => {
  const response = await axios.get(`${USER_API_URL}/${userId}/cart-items`);
  return response.data.data;
};

/**
 * Adds an item to the cart or updates its quantity if it already exists.
 * @param {Object} cartItemRequestDto - Contains userId, productId, quantity.
 * @returns {Promise<Object>} The added/updated cart item response DTO.
 */
const addOrUpdateCartItem = async (cartItemRequestDto) => {
  const response = await axios.post(CART_ITEM_API_URL, cartItemRequestDto);
  return response.data.data;
};

/**
 * Updates the quantity of a specific cart item.
 * @param {number|string} cartItemId - The ID of the cart item to update.
 * @param {Object} updateDto - Object containing the new quantity (e.g., { quantity: 3 }).
 * @returns {Promise<Object>} The updated cart item response DTO.
 */
const updateCartItemQuantity = async (cartItemId, updateDto) => {
  const response = await axios.patch(
    `${CART_ITEM_API_URL}/${cartItemId}/quantity`,
    updateDto
  );
  return response.data.data;
};

/**
 * Deletes a specific cart item by its ID.
 * @param {number|string} cartItemId - The ID of the cart item to delete.
 * @returns {Promise<void>}
 */
const deleteCartItem = async (cartItemId) => {
  await axios.delete(`${CART_ITEM_API_URL}/${cartItemId}`);
};

/**
 * Clears all items from a specific user's cart.
 * @param {number|string} userId - The ID of the user whose cart is to be cleared.
 * @returns {Promise<void>}
 */
const clearUserCart = async (userId) => {
  await axios.delete(`${USER_API_URL}/${userId}/cart-items`);
};

const CartItemService = {
  getCartItemById,
  getCartItemsByUserId,
  addOrUpdateCartItem,
  updateCartItemQuantity,
  deleteCartItem,
  clearUserCart,
};

export default CartItemService;
