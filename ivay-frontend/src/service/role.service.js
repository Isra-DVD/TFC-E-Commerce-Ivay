import axios from "axios";
import { API_BASE_URL } from "../constants/api";

/**
 * Base URL for role related API endpoints.
 */
const ROLE_API_URL = `${API_BASE_URL}/roles`;

/**
 * Fetches all roles from the API.
 * @returns {Promise<Array<Object>>} Array of role response DTOs.
 */
const getAllRoles = async () => {
    const response = await axios.get(ROLE_API_URL);
    return response.data.data;
};

/**
 * Fetches a specific role by its ID.
 * @param {number|string} roleId - The ID of the role.
 * @returns {Promise<Object>} Role response DTO.
 */
const getRoleById = async (roleId) => {
    const response = await axios.get(`${ROLE_API_URL}/${roleId}`);
    return response.data.data;
};

/**
 * Fetches a specific role by its name.
 * @param {string} name - The name of the role (e.g., "ROLE_GESTOR", "ROLE_ADMIN").
 * @returns {Promise<Object>} Role response DTO.
 */
const getRoleByName = async (name) => {
    const response = await axios.get(`${ROLE_API_URL}/by-name`, { params: { name } });
    return response.data.data;
};

/**
 * Fetches all users assigned to a specific role.
 * @param {number|string} roleId - The ID of the role.
 * @returns {Promise<Array<Object>>} Array of user response DTOs.
 */
const getUsersByRoleId = async (roleId) => {
    const response = await axios.get(`${ROLE_API_URL}/${roleId}/users`);
    return response.data.data;
};

/**
 * Creates a new role.
 * @param {Object} roleRequestDto - The data required to create a role.
 * @param {string} roleRequestDto.name - The name of the role.
 * @returns {Promise<Object>} The created role response DTO.
 */
const createRole = async (roleRequestDto) => {
    const response = await axios.post(ROLE_API_URL, roleRequestDto);
    return response.data.data;
};

/**
 * Updates an existing role by its ID.
 * @param {number|string} roleId - The ID of the role to update.
 * @param {Object} roleRequestDto - The updated role data.
 * @returns {Promise<Object>} The updated role response DTO.
 */
const updateRole = async (roleId, roleRequestDto) => {
    const response = await axios.put(`${ROLE_API_URL}/${roleId}`, roleRequestDto);
    return response.data.data;
};

/**
 * Deletes a role by its ID.
 * @param {number|string} roleId - The ID of the role to delete.
 * @returns {Promise<void>}
 */
const deleteRole = async (roleId) => {
    await axios.delete(`${ROLE_API_URL}/${roleId}`);
};

/**
 * Service object for interacting with Role related API endpoints.
 */
const RoleService = {
    getAllRoles,
    getRoleById,
    getRoleByName,
    getUsersByRoleId,
    createRole,
    updateRole,
    deleteRole,
};

export default RoleService;