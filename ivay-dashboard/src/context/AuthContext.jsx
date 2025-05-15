import React, { createContext, useState, useContext, useEffect } from "react";
import axios from "axios";
import UserService from "../service/user.service";

const AuthContext = createContext();
const API_BASE_URL = "http://localhost:8081/api";

export const AuthProvider = ({ children }) => {
    const [user, setUser] = useState(null);
    const [token, setToken] = useState(() => localStorage.getItem("authToken"));
    const [isLoading, setLoading] = useState(true);

    useEffect(() => {
        if (token) {
            axios.defaults.headers.common["Authorization"] = `Bearer ${token}`;
        }
        setLoading(false);
    }, [token]);

    useEffect(() => {
        const id = axios.interceptors.request.use((config) => {
            if (token && !config.headers["Authorization"]) {
                config.headers["Authorization"] = `Bearer ${token}`;
            }
            return config;
        });
        return () => axios.interceptors.request.eject(id);
    }, [token]);

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

    const logout = () => {
        localStorage.removeItem("authToken");
        delete axios.defaults.headers.common["Authorization"];
        setToken(null);
        setUser(null);
    };

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

export const useAuth = () => useContext(AuthContext);
