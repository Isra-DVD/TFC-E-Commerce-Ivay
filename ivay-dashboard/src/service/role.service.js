import axios from "axios";
import { API_BASE_URL } from "../constants/api";

const ROLE_API_URL = `${API_BASE_URL}/roles`;

const getAllRoles = async () => {
    const response = await axios.get(ROLE_API_URL);
    return response.data.data;
};

const getRoleById = async (roleId) => {
    const response = await axios.get(`${ROLE_API_URL}/${roleId}`);
    return response.data.data;
};

const getRoleByName = async (name) => {
    const response = await axios.get(`${ROLE_API_URL}/by-name`, { params: { name } });
    return response.data.data;
};

const getUsersByRoleId = async (roleId) => {
    const response = await axios.get(`${ROLE_API_URL}/${roleId}/users`);
    return response.data.data;
};

const createRole = async (roleRequestDto) => {
    const response = await axios.post(ROLE_API_URL, roleRequestDto);
    return response.data.data;
};

const updateRole = async (roleId, roleRequestDto) => {
    const response = await axios.put(`${ROLE_API_URL}/${roleId}`, roleRequestDto);
    return response.data.data;
};

const deleteRole = async (roleId) => {
    await axios.delete(`${ROLE_API_URL}/${roleId}`);
};

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