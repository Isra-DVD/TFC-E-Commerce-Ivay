import axios from "axios";
import { API_BASE_URL } from "../constants/api";

/**
 * Base URL for address related API endpoints.
 */
const ADDRESS_API_URL = `${API_BASE_URL}/addresses`;

/**
 * Fetches all addresses from the API.
 * @returns {Promise<Array<Object>>} Array of address response DTOs.
 */
const getAllAddresses = async () => {
  const response = await axios.get(ADDRESS_API_URL);
  return response.data.data;
};

/**
 * Fetches a specific address by its ID.
 * @param {number|string} addressId - The ID of the address.
 * @returns {Promise<Object>} Address response DTO.
 */
const getAddressById = async (addressId) => {
  const response = await axios.get(`${ADDRESS_API_URL}/${addressId}`);
  return response.data.data;
};

/**
 * Fetches all addresses belonging to a specific user.
 * @param {number|string} userId - The ID of the user.
 * @returns {Promise<Array<Object>>} Array of address response DTOs for the user.
 */
const getAddressesByUserId = async (userId) => {
  const response = await axios.get(`${ADDRESS_API_URL}/users/${userId}`);
  return response.data.data;
};

/**
 * Creates a new address.
 * @param {Object} addressRequestDto - The address data to be created.
 * @returns {Promise<Object>} The created address response DTO.
 */
const createAddress = async (addressRequestDto) => {
  const response = await axios.post(ADDRESS_API_URL, addressRequestDto);
  return response.data.data;
};

/**
 * Updates an existing address by its ID.
 * @param {number|string} addressId - The ID of the address to update.
 * @param {Object} addressRequestDto - The updated address data. Same structure as createAddressRequestDto.
 * @returns {Promise<Object>} The updated address response DTO.
 */
const updateAddress = async (addressId, addressRequestDto) => {
  const response = await axios.put(
    `${ADDRESS_API_URL}/${addressId}`,
    addressRequestDto
  );
  return response.data.data;
};

/**
 * Deletes an address by its ID.
 * @param {number|string} addressId - The ID of the address to delete.
 * @returns {Promise<void>}
 */
const deleteAddress = async (addressId) => {
  await axios.delete(`${ADDRESS_API_URL}/${addressId}`);
};

/**
 * Service object for interacting with Address related API endpoints.
 */
const AddressService = {
  getAllAddresses,
  getAddressById,
  getAddressesByUserId,
  createAddress,
  updateAddress,
  deleteAddress,
};

export default AddressService;