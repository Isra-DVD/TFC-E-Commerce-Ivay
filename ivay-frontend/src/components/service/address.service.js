// src/service/address.service.js
import axios from "axios";
import { API_BASE_URL } from "../constants/api";

const ADDRESS_API_URL = `${API_BASE_URL}/addresses`;
// User specific addresses are fetched via user service/controller: /users/{userId}/addresses

const getAllAddresses = async () => { // Less common, usually per user
    const response = await axios.get(ADDRESS_API_URL);
    return response.data.data;
};

const getAddressById = async (addressId) => {
    const response = await axios.get(`${ADDRESS_API_URL}/${addressId}`);
    return response.data.data;
};

const getAddressesByUserId = async (userId) => { // Endpoint is on UserController
    const response = await axios.get(`${API_BASE_URL}/users/${userId}/addresses`);
    return response.data.data;
};

const createAddress = async (addressRequestDto) => { // Requires userId in DTO
    const response = await axios.post(ADDRESS_API_URL, addressRequestDto);
    return response.data.data;
};

const updateAddress = async (addressId, addressRequestDto) => {
    const response = await axios.put(`${ADDRESS_API_URL}/${addressId}`, addressRequestDto);
    return response.data.data;
};

const deleteAddress = async (addressId) => {
    await axios.delete(`${ADDRESS_API_URL}/${addressId}`);
};

const AddressService = {
    getAllAddresses,
    getAddressById,
    getAddressesByUserId, // Convenience, calls user endpoint
    createAddress,
    updateAddress,
    deleteAddress,
};

export default AddressService;