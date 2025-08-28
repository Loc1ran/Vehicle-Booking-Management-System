import axios from 'axios';

export const getBooking = async ()=> {
    try {
        return await axios.get(`${import.meta.env.VITE_API_BASE_URL}/api/v1/booking`)

    } catch (err) {
        throw err;
    }
}