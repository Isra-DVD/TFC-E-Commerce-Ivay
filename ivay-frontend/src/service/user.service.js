import axios from "axios";
import { API_BASE_URL } from "../constants/api";

const USER_API_URL = `${API_BASE_URL}/users`;

const getAllUsers = async () => {
    const response = await axios.get(USER_API_URL);
    return response.data.data;
};

const findUsersByName = async (name) => {
    const response = await axios.get(`${USER_API_URL}/filter`, { params: { name } });
    return response.data.data;
};

const findUsersByRole = async (roleId) => {
    const response = await axios.get(`${USER_API_URL}/filter`, { params: { roleId } });
    return response.data.data;
};

const getUserById = async (userId) => {
    const response = await axios.get(`${USER_API_URL}/${userId}`);
    return response.data.data;
};

const getUserByEmail = async (email) => {
    const response = await axios.get(`${USER_API_URL}/by-email`, { params: { email } });
    return response.data.data;
};

const getAddressesByUserId = async (userId) => {
    const response = await axios.get(`${USER_API_URL}/${userId}/addresses`);
    return response.data.data;
};
// getOrdersByUserId and getCartItemsByUserId are already in OrderService and CartItemService

const createUser = async (createUserRequestDto) => {
    const response = await axios.post(USER_API_URL, createUserRequestDto);
    return response.data.data;
};

const updateUser = async (userId, updateUserRequestDto) => {
    const response = await axios.put(`${USER_API_URL}/${userId}`, updateUserRequestDto);
    return response.data.data;
};

const deleteUser = async (userId) => {
    await axios.delete(`${USER_API_URL}/${userId}`);
};

const UserService = {
    getAllUsers,
    findUsersByName,
    findUsersByRole,
    getUserById,
    getUserByEmail,
    getAddressesByUserId,
    createUser,
    updateUser,
    deleteUser,
};

export default UserService;