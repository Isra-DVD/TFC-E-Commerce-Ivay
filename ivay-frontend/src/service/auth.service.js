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
        // The backend directly returns AuthResponseDto in the body with HttpStatus.OK
        // So, we directly return response.data
        return response.data;
    } catch (error) {
        // Handle specific error responses from the backend if needed
        // For example, if the backend returns a 401 with a specific error message
        if (error.response && error.response.data) {
            // You might want to throw an error with a specific message
            // or return a specific structure for the calling component to handle
            throw new Error(error.response.data.message || 'Login failed. Please check your credentials.');
        }
        // For other errors (network, etc.)
        throw new Error('An unexpected error occurred during login.');
    }
};

const AuthService = {
    login,
    // You can add other auth-related functions here later, e.g.:
    // register: async (registerRequestDto) => { ... },
    // logout: async () => { ... }, // Might involve clearing local storage/cookies
    // refreshToken: async (refreshTokenDto) => { ... },
};

export default AuthService;