import React, { createContext, useState, useContext, useEffect } from "react";
import axios from "axios";

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
    return data;
  };

  const logout = () => {
    localStorage.removeItem("authToken");
    delete axios.defaults.headers.common["Authorization"];
    setToken(null);
    setUser(null);
  };

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
