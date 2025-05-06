import axios from "axios";
import { API_BASE_URL } from "../constants/api";

const SUPPLIER_API_URL = `${API_BASE_URL}/suppliers`;

const getAllSuppliers = async () => {
    const response = await axios.get(SUPPLIER_API_URL);
    return response.data.data;
};

const findSuppliersByName = async (name) => {
    const response = await axios.get(`${SUPPLIER_API_URL}/filter`, { params: { name } });
    return response.data.data;
};

const getSupplierById = async (supplierId) => {
    const response = await axios.get(`${SUPPLIER_API_URL}/${supplierId}`);
    return response.data.data;
};

const createSupplier = async (supplierRequestDto) => {
    const response = await axios.post(SUPPLIER_API_URL, supplierRequestDto);
    return response.data.data;
};

const updateSupplier = async (supplierId, supplierRequestDto) => {
    const response = await axios.put(`${SUPPLIER_API_URL}/${supplierId}`, supplierRequestDto);
    return response.data.data;
};

const deleteSupplier = async (supplierId) => {
    await axios.delete(`${SUPPLIER_API_URL}/${supplierId}`);
};

const getProductsBySupplierId = async (supplierId) => {
    const response = await axios.get(`${SUPPLIER_API_URL}/${supplierId}/products`);
    return response.data.data;
};

const SupplierService = {
    getAllSuppliers,
    findSuppliersByName,
    getSupplierById,
    createSupplier,
    updateSupplier,
    deleteSupplier,
    getProductsBySupplierId,
};

export default SupplierService;