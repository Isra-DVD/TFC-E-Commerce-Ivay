import axios from "axios";
import { API_BASE_URL } from "../constants/api";

const ADDRESS_API_URL = `${API_BASE_URL}/addresses`;

const getAllAddresses = async () => {
  const response = await axios.get(ADDRESS_API_URL);
  return response.data.data;
};

const getAddressById = async (addressId) => {
  const response = await axios.get(`${ADDRESS_API_URL}/${addressId}`);
  return response.data.data;
};

const getAddressesByUserId = async (userId) => {
  const response = await axios.get(`${ADDRESS_API_URL}/users/${userId}`);
  return response.data.data;
};

const createAddress = async (addressRequestDto) => {
  const response = await axios.post(ADDRESS_API_URL, addressRequestDto);
  return response.data.data;
};

const updateAddress = async (addressId, addressRequestDto) => {
  const response = await axios.put(
    `${ADDRESS_API_URL}/${addressId}`,
    addressRequestDto
  );
  return response.data.data;
};

const deleteAddress = async (addressId) => {
  await axios.delete(`${ADDRESS_API_URL}/${addressId}`);
};

const AddressService = {
  getAllAddresses,
  getAddressById,
  getAddressesByUserId,
  createAddress,
  updateAddress,
  deleteAddress,
};

export default AddressService;
