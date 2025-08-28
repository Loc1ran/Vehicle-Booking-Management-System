import { Center, Wrap, WrapItem, Button, Spinner, Text } from '@chakra-ui/react'
import SidebarWithHeader from '../components/shared/Sidebar.jsx';
import {useEffect, useState} from 'react';
import {getBooking} from "./services/client.js";
import ProductAddToCart from '../components/products/product.jsx';

const App = () => {
    const [getBookings, setGetBookings] = useState([]);
    const [loading, setLoading] = useState(false);

    useEffect(() => {
        setLoading(true);
        getBooking().then(res => {
            setGetBookings(res.data);
        }).catch(err => {
            console.log(err);
        }).finally(() =>
        {
            setLoading(false);
        })
    }, [])
    if(loading){
        return (
            <SidebarWithHeader><Spinner color="teal.500" size="lg" /></SidebarWithHeader>
            )
    }

    if(getBookings.length <= 0){
        return (
            <SidebarWithHeader><Text>No Booking Available</Text></SidebarWithHeader>
        )
    }
    return (
        <SidebarWithHeader>
            <Wrap justify={"center"} spacing={"10px"}>
                {getBookings.map((booking,index) => (
                    <WrapItem key={index}>
                        <ProductAddToCart
                            {...booking}
                        />
                    </WrapItem>
                ))}
            </Wrap>

        </SidebarWithHeader>
    )
}

export default App;