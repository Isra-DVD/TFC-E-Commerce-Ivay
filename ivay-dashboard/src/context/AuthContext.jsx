import React, { createContext, useState, useContext, useEffect } from "react";
import axios from "axios";
import UserService from "../service/user.service";

/* Creates the AuthContext object that will hold the authentication state and functions. */
const AuthContext = createContext();
const API_BASE_URL = "http://localhost:8081/api";

/**
 * Provides authentication context to the application.
 * Manages user state, authentication token, loading state, login, and logout functionality.
 * Automatically loads the user and sets the token in axios headers on application load or token change.
 * Includes an Axios interceptor to attach the token to outgoing requests.
 */
export const AuthProvider = ({ children }) => {
    const [user, setUser] = useState(null);
    const [token, setToken] = useState(() => localStorage.getItem("authToken"));
    const [isLoading, setLoading] = useState(true);

    /* Effect hook that sets the default Authorization header for Axios when the token state changes. */
    useEffect(() => {
        if (token) {
            axios.defaults.headers.common["Authorization"] = `Bearer ${token}`;
        }
        setLoading(false);
    }, [token]);

    /* Effect hook that adds an Axios request interceptor to ensure the token is included in all requests if available. */
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
     * Handles the login process.
     * Sends credentials to the auth login endpoint, stores the token, and fetches/sets the user profile.
     * Updates state with the received token and user data.
     *
     * @param {object} credentials - An object containing username and password.
     * @returns {Promise<object>} A promise that resolves with the API response data.
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
     * Handles the logout process.
     * Removes the token from localStorage, deletes the Authorization header from Axios defaults,
     * and resets the token and user states to null.
     */
    const logout = () => {
        if (token) {
            localStorage.removeItem("authToken");
            delete axios.defaults.headers.common["Authorization"];
            setToken(null);
            setUser(null);
        }
    };

    /* Effect hook to fetch the user's profile upon application load if a token exists, effectively restoring the session. */
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
    }, [token, user]);


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
 * Custom hook to consume the AuthContext.
 * Provides easy access to authentication state and functions within functional 
 * components.
 *
 * @returns {object} The authentication context value (user, token, 
 * isAuthenticated, isLoading, login, logout, setUser).
 */
export const useAuth = () => useContext(AuthContext);