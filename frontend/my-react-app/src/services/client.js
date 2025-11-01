import axios from "axios";

const getAuthConfig = () => ({
    headers: {
        Authorization: `Bearer ${localStorage.getItem("access_token")}`
    }
})

export const login = async (usernameAndPassword) => {
    try{
        return await axios.post(`${import.meta.env.VITE_API_BASE_URL}/api/v1/auth/login`, usernameAndPassword)
    } catch (err){
        throw err;
    }
}

export const saveUser = async (usernameAndPassword) => {
    try {
        return await axios.post(`${import.meta.env.VITE_API_BASE_URL}/api/v1/users`, usernameAndPassword)
    } catch (err) {
        throw err;
    }
}

export const getUserById = async (userId) => {
    try{
        return await axios.get(`${import.meta.env.VITE_API_BASE_URL}/api/v1/users/${userId}`, getAuthConfig())
    } catch (err){
        throw err;
    }
}



