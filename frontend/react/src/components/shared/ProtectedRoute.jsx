import {useNavigate} from "react-router-dom";
import {useEffect} from "react";
import {useAuth} from "../context/AuthContext.jsx";

const ProtectedRoute = ( { children }) => {

    const { customer } = useAuth()
    const navigate = useNavigate();

    useEffect(() => {
        if(!customer) {
            navigate("/")
        }
    })
    return customer ? children : "";
}

export default ProtectedRoute;