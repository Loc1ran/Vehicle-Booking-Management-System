import axios from 'axios';

export const getCar = async ()=> {
    try {
        return await axios.get(`${import.meta.env.VITE_API_BASE_URL}/api/v1/cars`)

    } catch (err) {
        throw err;
    }
}

export const addCar = async (car) => {
    try {
        return await axios.post(`${import.meta.env.VITE_API_BASE_URL}/api/v1/cars`, car)
    } catch (err){
        throw err;
    }
}

export const deleteCar = async (regNumber) => {
    try{
        return await axios.delete(`${import.meta.env.VITE_API_BASE_URL}/api/v1/cars/${regNumber}`)
    } catch (err){
        throw err;
    }
}

export const updateCar = async (regNumber, car) => {
    try{
        return await axios.put(`${import.meta.env.VITE_API_BASE_URL}/api/v1/cars/${regNumber}`, car)
    } catch (err){
        throw err;
    }
}