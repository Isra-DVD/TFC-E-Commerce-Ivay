import axios from "axios";
import { API_BASE_URL } from "../constants/api";

const CATEGORY_API_URL = `${API_BASE_URL}/categories`;

/**
 * Fetches all categories.
 * @returns {Promise<Array>} A promise that resolves to an array of category response DTOs.
 */
const getAllCategories = async () => {
    const response = await axios.get(CATEGORY_API_URL);
    return response.data.data; // Assuming your ApiResponseDto wraps data
};

/**
 * Fetches categories whose names contain the given string.
 * @param {string} name - The name to filter by.
 * @returns {Promise<Array>} A promise that resolves to an array of category response DTOs.
 */
const findCategoriesByName = async (name) => {
    const response = await axios.get(`${CATEGORY_API_URL}/filter`, { params: { name } });
    return response.data.data;
};

/**
 * Fetches a category by its ID.
 * @param {number|string} categoryId - The ID of the category.
 * @returns {Promise<Object>} A promise that resolves to a category response DTO.
 */
const getCategoryById = async (categoryId) => {
    const response = await axios.get(`${CATEGORY_API_URL}/${categoryId}`);
    return response.data.data;
};

/**
 * Creates a new category.
 * @param {Object} categoryRequestDto - The category data to create.
 * @returns {Promise<Object>} A promise that resolves to the created category response DTO.
 */
const createCategory = async (categoryRequestDto) => {
    const response = await axios.post(CATEGORY_API_URL, categoryRequestDto);
    return response.data.data;
};

/**
 * Updates an existing category.
 * @param {number|string} categoryId - The ID of the category to update.
 * @param {Object} categoryRequestDto - The updated category data.
 * @returns {Promise<Object>} A promise that resolves to the updated category response DTO.
 */
const updateCategory = async (categoryId, categoryRequestDto) => {
    const response = await axios.put(`${CATEGORY_API_URL}/${categoryId}`, categoryRequestDto);
    return response.data.data;
};

/**
 * Deletes a category by its ID.
 * @param {number|string} categoryId - The ID of the category to delete.
 * @returns {Promise<void>} A promise that resolves when deletion is successful.
 */
const deleteCategory = async (categoryId) => {
    await axios.delete(`${CATEGORY_API_URL}/${categoryId}`);
    // No specific data returned on 204 No Content
};

/**
 * Fetches products associated with a specific category ID.
 * @param {number|string} categoryId - The ID of the category.
 * @returns {Promise<Array>} A promise that resolves to an array of product response DTOs.
 */
const getProductsByCategoryId = async (categoryId) => {
    const response = await axios.get(`${CATEGORY_API_URL}/${categoryId}/products`);
    return response.data.data;
};


const CategoryService = {
    getAllCategories,
    findCategoriesByName,
    getCategoryById,
    createCategory,
    updateCategory,
    deleteCategory,
    getProductsByCategoryId,
};

export default CategoryService;