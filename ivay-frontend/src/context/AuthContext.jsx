import React, { createContext, useState, useContext, useEffect } from "react";
import axios from "axios";
import UserService from "../service/user.service";

const AuthContext = createContext();
const API_BASE_URL = "http://localhost:8081/api";

/**
 * Provides authentication context and state to the application.
 * Manages user login, logout, authentication token, and user data.
 */
export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null);
  const [token, setToken] = useState(() => localStorage.getItem("authToken"));
  const [isLoading, setLoading] = useState(true);

  /* Effect hook to set the default Authorization header for Axios
   whenever the authentication token changes. */
  useEffect(() => {
    if (token) {
      axios.defaults.headers.common["Authorization"] = `Bearer ${token}`;
    }
    setLoading(false);
  }, [token]);

  /* Effect hook to add an Axios request interceptor that ensures the
   Authorization header is present for every outgoing request if a token exists. */
  useEffect(() => {
    const id = axios.interceptors.request.use((config) => {
      if (token && !config.headers["Authorization"]) {
        config.headers["Authorization"] = `Bearer ${token}`;
      }
      return config;
    });
    return () => axios.interceptors.request.eject(id);
  }, [token]);

  /**
   * Handles user login. Sends credentials to the backend,
   * stores the received token, sets user state, and updates Axios headers.
   * @param {object} credentials - An object containing username and password.
   * @returns {Promise<object>} A promise that resolves with the login response data.
   */
  const login = async ({ username, password }) => {
    const { data } = await axios.post(`${API_BASE_URL}/auth/login`, {
      username,
      password,
    });
    const jwt = data.accessToken;
    localStorage.setItem("authToken", jwt);
    setToken(jwt);
    if (data.user && data.user.id) {
      setUser(data.user);
    } else if (data.userId) {
      try {
        const profile = await UserService.getMyProfile();
        setUser(profile);
      } catch (error) {
        console.error("Failed to fetch profile after login:", error);
        setUser({ id: data.userId });
      }
    }
    return data;
  };

  /**
   * Handles user logout. Removes the authentication token from local storage,
   * clears Axios headers, and resets user and token states.
   */
  const logout = () => {
    localStorage.removeItem("authToken");
    delete axios.defaults.headers.common["Authorization"];
    setToken(null);
    setUser(null);
  };

  /* Effect hook to fetch user profile data when the component loads
   if a token exists but the user state is null (e.g., on page refresh). */
  useEffect(() => {
    const fetchUserOnLoad = async () => {
      if (token && !user) {
        setLoading(true);
        try {
          axios.defaults.headers.common["Authorization"] = `Bearer ${token}`;
          const profile = await UserService.getMyProfile();
          setUser(profile);
        } catch (error) {
          console.error("Session expired or invalid token:", error);
          logout();
        } finally {
          setLoading(false);
        }
      } else {
        setLoading(false);
      }
    };

    fetchUserOnLoad();
  }, [token]);

  return (
    <AuthContext.Provider
      value={{
        user,
        token,
        isAuthenticated: Boolean(token),
        isLoading,
        login,
        logout,
        setUser,
      }}
    >
      {children}
    </AuthContext.Provider>
  );
};

/**
 * Custom hook to easily access the authentication context values.
 * @returns {object} The authentication context value (user, token, isAuthenticated, etc.).
 */
export const useAuth = () => useContext(AuthContext);
