import axios from "axios";

const getAuthConfig = () => ({
    headers: {
        Authorization: `Bearer ${localStorage.getItem("access_token")}`
    }
})

export const getUserBooking = async (userId) => {
    try {
        return await axios.get(`${import.meta.env.VITE_API_BASE_URL}/api/v1/booking/viewUserBookedCars/${userId}`, getAuthConfig())
    } catch (err){
        throw err;
    }
}

export const addBooking = async (booking) => {
    try {
        return await axios.post(`${import.meta.env.VITE_API_BASE_URL}/api/v1/booking`, booking, getAuthConfig())
    } catch (err) {
        throw err;
    }
}

export const getAvailableCar = async () => {
    try {
        return await axios.get(`${import.meta.env.VITE_API_BASE_URL}/api/v1/booking/getAvailableCars`, getAuthConfig())
    } catch (err){
        throw err;
    }
}