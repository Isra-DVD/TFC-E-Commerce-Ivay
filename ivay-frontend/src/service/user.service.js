import axios from "axios";
import { API_BASE_URL } from "../constants/api";

/**
 * Base URL for user related API endpoints.
 */
const USER_API_URL = `${API_BASE_URL}/users`;

/**
 * Fetches all users from the API.
 * @returns {Promise<Array<Object>>} Array of user response DTOs.
 */
const getAllUsers = async () => {
  const response = await axios.get(USER_API_URL);
  return response.data.data;
};

/**
 * Finds users by name using a filter endpoint.
 * @param {string} name - The name or part of the name to search for (e.g., first name, last name, or full name).
 * @returns {Promise<Array<Object>>} Array of user response DTOs matching the name.
 */
const findUsersByName = async (name) => {
  const response = await axios.get(`${USER_API_URL}/filter`, {
    params: { name },
  });
  return response.data.data;
};

/**
 * Finds users by role using a filter endpoint.
 * @param {number|string} roleId - The ID of the role to filter by.
 * @returns {Promise<Array<Object>>} Array of user response DTOs having the specified role.
 */
const findUsersByRole = async (roleId) => {
  const response = await axios.get(`${USER_API_URL}/filter`, {
    params: { roleId },
  });
  return response.data.data;
};

/**
 * Fetches a specific user by their ID.
 * @param {number|string} userId - The ID of the user.
 * @returns {Promise<Object>} User response DTO.
 */
const getUserById = async (userId) => {
  const response = await axios.get(`${USER_API_URL}/${userId}`);
  return response.data.data;
};

/**
 * Fetches a specific user by their email address.
 * @param {string} email - The email address of the user.
 * @returns {Promise<Object>} User response DTO.
 */
const getUserByEmail = async (email) => {
  const response = await axios.get(`${USER_API_URL}/by-email`, {
    params: { email },
  });
  return response.data.data;
};

/**
 * Fetches all addresses belonging to a specific user.
 * @param {number|string} userId - The ID of the user.
 * @returns {Promise<Array<Object>>} Array of address response DTOs for the user.
 */
const getAddressesByUserId = async (userId) => {
  const response = await axios.get(`${USER_API_URL}/${userId}/addresses`);
  return response.data.data;
};

/**
 * Creates a new user.
 * @param {Object} createUserRequestDto - The data required to create a user.
 * @returns {Promise<Object>} The created user response DTO.
 */
const createUser = async (createUserRequestDto) => {
  const response = await axios.post(USER_API_URL, createUserRequestDto);
  return response.data.data;
};

/**
 * Updates an existing user by their ID.
 * @param {number|string} userId - The ID of the user to update.
 * @param {Object} updateUserRequestDto - The updated user data.
 * @returns {Promise<Object>} The updated user response DTO.
 */
const updateUser = async (userId, updateUserRequestDto) => {
  const response = await axios.put(
    `${USER_API_URL}/${userId}`,
    updateUserRequestDto
  );
  return response.data.data;
};

/**
 * Deletes a user by their ID.
 * @param {number|string} userId - The ID of the user to delete.
 * @returns {Promise<void>}
 */
const deleteUser = async (userId) => {
  await axios.delete(`${USER_API_URL}/${userId}`);
};

/**
 * Fetches the profile of the currently authenticated user.
 * @returns {Promise<Object>} The current user's response DTO.
 */
const getMyProfile = async () => {
  const response = await axios.get(`${USER_API_URL}/me`);
  return response.data.data;
};

/**
 * Updates the profile of the currently authenticated user.
 * @param {Object} profileDto - The updated profile data.
 * @returns {Promise<Object>} The updated user profile response DTO.
 */
const updateMyProfile = async (profileDto) => {
  const response = await axios.put(`${USER_API_URL}/me/profile`, profileDto);
  return response.data.data;
};

/**
 * Changes the password of the currently authenticated user.
 * @param {Object} dto - Object containing the old and new password.
 * @returns {Promise<void>}
 */
const changePassword = async (dto) => {
  await axios.patch(`${USER_API_URL}/me/password`, dto);
};

/**
 * Service object for interacting with User related API endpoints.
 */
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
  getMyProfile,
  updateMyProfile,
  changePassword,
};

export default UserService;