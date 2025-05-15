import axios from "axios";
import { API_BASE_URL } from "../constants/api";

const AUTH_API_URL = `${API_BASE_URL}/auth`;

/**
 * Sends a login request to the authentication endpoint.
 *
 * @param {Object} loginRequestDto - The login credentials.
 *   Typically an object like: { email: 'user@example.com', password: 'yourpassword' }
 *   (This should match your AuthLoginRequestDto on the backend).
 * @returns {Promise<Object>} A promise that resolves to the authentication response DTO
 *   (e.g., containing a JWT token and user details).
 *   This should match your AuthResponseDto on the backend.
 */
const login = async (loginRequestDto) => {
  try {
    const response = await axios.post(`${AUTH_API_URL}/login`, loginRequestDto);

    return response.data;
  } catch (error) {
    if (error.response && error.response.data) {
      throw new Error(
        error.response.data.message ||
          "Login failed. Please check your credentials."
      );
    }

    throw new Error("An unexpected error occurred during login.");
  }
};

const setAuthHeader = (token) => {
  axios.defaults.headers.common["Authorization"] = token;
};

const AuthService = {
  login,
  setAuthHeader,
};

export default AuthService;
