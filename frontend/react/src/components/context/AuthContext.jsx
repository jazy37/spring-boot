import {createContext, useContext, useEffect, useRef, useState} from "react";

const AuthContext = createContext({})
import {login as performLogin, logout as performLogout} from "../../services/client.js";



const AuthProvider = ({ children }) => {
    const [customer, setCustomer] = useState(null);

    const setCustomerFromStorage = () => {
        let user = localStorage.getItem("_user")
        if(user) {
            setCustomer(JSON.parse(user))
        }
    }
    useEffect(() => {
        setCustomerFromStorage()
    }, [])

    const login = async (usernameAndPassword) => {
        try {
            const res = await performLogin(usernameAndPassword);
            console.log(res)
            localStorage.setItem("_user", JSON.stringify({username: res?.data?.email, roles: res?.data?.roles}))
            setCustomer({
              username: res?.data?.email,
              roles: res?.data?.roles
            })
            return res;
        } catch (err) {
            throw err;
        }
    };

    const logout = async () => {
        try {
            const res = await performLogout();
            localStorage.removeItem("_user")
            setCustomer(null)
            return res;
        } catch (err) {
            throw err
        }

    }
    return (
        <AuthContext.Provider value={{
            customer,
            login,
            logout,
        }}>
            {children}
        </AuthContext.Provider>
    )
}

export const useAuth = () => useContext(AuthContext);

export default AuthProvider;