import axios from "axios";



export const getCustomers = async () => {
    try {
        return await axios.get(`${import.meta.env.VITE_API_BASE_URL}/api/v1/customers`,
            {withCredentials: true})

    } catch (err) {
        throw err
    }
}

export const saveCustomer = async (customer) => {
    try {
        return await axios.post(`${import.meta.env.VITE_API_BASE_URL}/api/v1/customers`, customer,
            {withCredentials: true})
    } catch (err) {
        throw err
    }
}

export const deleteCustomer = async (customerId) => {
    try {
        return await axios.delete(`${import.meta.env.VITE_API_BASE_URL}/api/v1/customers/${customerId}`, {withCredentials: true})
    } catch (err) {
        throw err
    }
}

export const updateCustomer = async (customerId, customer) => {
    try {
        return await axios.put(`${import.meta.env.VITE_API_BASE_URL}/api/v1/customers/${customerId}`, customer, {withCredentials: true})
    } catch (err) {
        throw err;
    }
}

export const login = async (usernameAndPassword) => {
    try {
        return await axios.post(`${import.meta.env.VITE_API_BASE_URL}/api/v1/auth/login`, usernameAndPassword, {withCredentials: true})
    } catch (err) {
        throw err
    }
}

export const logout = async () => {
    try {
        return await axios.post(`${import.meta.env.VITE_API_BASE_URL}/api/v1/auth/logout`, {withCredentials: true})
    } catch (err) {
        throw err
    }
}