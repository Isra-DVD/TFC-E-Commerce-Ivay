import axios from "axios";
import { API_BASE_URL } from "../constants/api";

const ORDER_API_URL = `${API_BASE_URL}/orders`;
const USER_API_URL = `${API_BASE_URL}/users`; // For user-specific order endpoints

const getAllOrders = async () => {
    const response = await axios.get(ORDER_API_URL);
    return response.data.data;
};

const getOrdersByUserId = async (userId) => {
    const response = await axios.get(`${USER_API_URL}/${userId}/orders`);
    return response.data.data;
};

const getOrderById = async (orderId) => {
    const response = await axios.get(`${ORDER_API_URL}/${orderId}`);
    return response.data.data;
};

const getOrderItemsByOrderId = async (orderId) => { // Duplicates OrderItemService, keep for API structure
    const response = await axios.get(`${ORDER_API_URL}/${orderId}/items`);
    return response.data.data;
};

const createOrder = async (createOrderRequestDto) => {
    const response = await axios.post(ORDER_API_URL, createOrderRequestDto);
    return response.data.data;
};

const updateOrder = async (orderId, updateOrderDto) => {
    const response = await axios.put(`${ORDER_API_URL}/${orderId}`, updateOrderDto);
    return response.data.data;
};

const deleteOrder = async (orderId) => {
    await axios.delete(`${ORDER_API_URL}/${orderId}`);
};

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