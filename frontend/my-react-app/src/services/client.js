import axios from 'axios';

const getAuthConfig = () => ({
    headers: {
        Authorization: `Bearer ${localStorage.getItem("access_token")}`
    }
})

export const getCar = async ()=> {
    try {
        return await axios.get(`${import.meta.env.VITE_API_BASE_URL}/api/v1/cars`, getAuthConfig());

    } catch (err) {
        throw err;
    }
}

export const addCar = async (car) => {
    try {
        return await axios.post(`${import.meta.env.VITE_API_BASE_URL}/api/v1/cars`, car, getAuthConfig())
    } catch (err){
        throw err;
    }
}

export const deleteCar = async (regNumber) => {
    try{
        return await axios.delete(`${import.meta.env.VITE_API_BASE_URL}/api/v1/cars/${regNumber}`, getAuthConfig())
    } catch (err){
        throw err;
    }
}

export const updateCar = async (regNumber, car) => {
    try{
        return await axios.put(`${import.meta.env.VITE_API_BASE_URL}/api/v1/cars/${regNumber}`, car, getAuthConfig())
    } catch (err){
        throw err;
    }
}

export const uploadCarImage = async (regNumber, formData) => {
    try {
        return await axios.post(`${import.meta.env.VITE_API_BASE_URL}/api/v1/cars/${regNumber}/car-images`,
            formData,
            {
                ...getAuthConfig(),
                contentType: "multipart/form-data",
            }

        )

    } catch (err){
        throw err;
    }
}

export const carImageUrl = (regNumber) =>
    `${import.meta.env.VITE_API_BASE_URL}/api/v1/cars/${regNumber}/car-images`;

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

