import { Center, Wrap, WrapItem, Button, Spinner, Text } from '@chakra-ui/react'
import SidebarWithHeader from '../src/components/shared/Sidebar.jsx';
import {useEffect, useState} from 'react';
import {getCar} from "./services/car.js";
import ProductAddToCart from '../src/components/car/product.jsx';
import CreateCarDrawerForm from '../src/components/car/CreateCarDrawerForm.jsx';
import {errorNotification} from "./services/notification.js";
import {getAvailableCar} from "./services/booking.js";
import {useAuth} from "./components/context/AuthContext.jsx";

const App = () => {
    const [getCars, setGetCars] = useState([]);
    const [loading, setLoading] = useState(false);

    const { user } = useAuth();

    const fetchCars = () => {
        setLoading(true);
        setGetCars([]);
        getAvailableCar().then(res => {
            setGetCars(res.data);
        }).catch(err => {
            console.log(err);
        }).finally(() =>
        {
            setLoading(false);
        })
    }

    useEffect(() => {
        fetchCars();
    }, [])

    if(loading){
        return (
            <SidebarWithHeader><Spinner color="teal.500" size="lg" /></SidebarWithHeader>
        )
    }

    if(getCars.length <= 0){
        return (
            <SidebarWithHeader>
                <CreateCarDrawerForm fetchCars={fetchCars}/>
                <Text mt={4}>
                    No Car Available For Rent
                </Text>
            </SidebarWithHeader>
        )
    }

    if(!user){
        return (
            <SidebarWithHeader><Spinner color="teal.500" size="lg" /></SidebarWithHeader>
        )
    }

    return (
        <SidebarWithHeader>
            <CreateCarDrawerForm fetchCars={fetchCars}/>
            <Wrap justify={"center"} spacing={"10px"}>
                {getCars.map((car,index) => (
                    <WrapItem key={index}>
                        <ProductAddToCart fetchCars={fetchCars}
                                          {...car}
                        />
                    </WrapItem>
                ))}
            </Wrap>

        </SidebarWithHeader>
    )
}

export default App;