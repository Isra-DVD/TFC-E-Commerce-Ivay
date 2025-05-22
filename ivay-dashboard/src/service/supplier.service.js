import axios from "axios";
import { API_BASE_URL } from "../constants/api";

/**
 * Base URL for supplier related API endpoints.
 */
const SUPPLIER_API_URL = `${API_BASE_URL}/suppliers`;

/**
 * Fetches all suppliers from the API.
 * @returns {Promise<Array<Object>>} Array of supplier response DTOs.
 */
const getAllSuppliers = async () => {
    const response = await axios.get(SUPPLIER_API_URL);
    return response.data.data;
};

/**
 * Finds suppliers by name using a filter endpoint.
 * @param {string} name - The name or part of the name to search for.
 * @returns {Promise<Array<Object>>} Array of supplier response DTOs matching the name.
 */
const findSuppliersByName = async (name) => {
    const response = await axios.get(`${SUPPLIER_API_URL}/filter`, { params: { name } });
    return response.data.data;
};

/**
 * Fetches a specific supplier by its ID.
 * @param {number|string} supplierId - The ID of the supplier.
 * @returns {Promise<Object>} Supplier response DTO.
 */
const getSupplierById = async (supplierId) => {
    const response = await axios.get(`${SUPPLIER_API_URL}/${supplierId}`);
    return response.data.data;
};

/**
 * Creates a new supplier.
 * @param {Object} supplierRequestDto - The data required to create a supplier.
 * @returns {Promise<Object>} The created supplier response DTO.
 */
const createSupplier = async (supplierRequestDto) => {
    const response = await axios.post(SUPPLIER_API_URL, supplierRequestDto);
    return response.data.data;
};

/**
 * Updates an existing supplier by its ID.
 * @param {number|string} supplierId - The ID of the supplier to update.
 * @param {Object} supplierRequestDto - The updated supplier data. Same structure as createSupplierRequestDto.
 * @returns {Promise<Object>} The updated supplier response DTO.
 */
const updateSupplier = async (supplierId, supplierRequestDto) => {
    const response = await axios.put(`${SUPPLIER_API_URL}/${supplierId}`, supplierRequestDto);
    return response.data.data;
};

/**
 * Deletes a supplier by its ID.
 * Deleting suppliers might be restricted if they are linked to products.
 * @param {number|string} supplierId - The ID of the supplier to delete.
 * @returns {Promise<void>}
 */
const deleteSupplier = async (supplierId) => {
    await axios.delete(`${SUPPLIER_API_URL}/${supplierId}`);
};

/**
 * Fetches all products supplied by a specific supplier.
 * @param {number|string} supplierId - The ID of the supplier.
 * @returns {Promise<Array<Object>>} Array of product response DTOs.
 */
const getProductsBySupplierId = async (supplierId) => {
    const response = await axios.get(`${SUPPLIER_API_URL}/${supplierId}/products`);
    return response.data.data;
};

/**
 * Service object for interacting with Supplier related API endpoints.
 */
const SupplierService = {
    getAllSuppliers,
    findSuppliersByName,
    getSupplierById,
    createSupplier,
    updateSupplier,
    deleteSupplier,
    getProductsBySupplierId,
};

export default SupplierService;