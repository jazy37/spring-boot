import axios from "axios";
import {object} from "yup";

export const getCustomers = async () => {
    try {
        return await axios.get(`${import.meta.env.VITE_API_BASE_URL}/api/v1/customers`)
    } catch (err) {
        throw err
    }
}

export const saveCustomer = async (customer) => {
    try {
        return await axios.post(`${import.meta.env.VITE_API_BASE_URL}/api/v1/customers`, customer)
    } catch (err) {
        throw err
    }
}

export const deleteCustomer = async (customerId) => {
    try {
        return await axios.delete(`${import.meta.env.VITE_API_BASE_URL}/api/v1/customers/${customerId}`)
    } catch (err) {
        throw err
    }
}

export const updateCustomer = async (customerId, customer) => {
    try {
        return await axios.put(`${import.meta.env.VITE_API_BASE_URL}/api/v1/customers/${customerId}`, customer)
    } catch (err) {
        throw err;
    }
}