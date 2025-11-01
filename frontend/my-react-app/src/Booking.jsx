import SidebarWithHeader from "./components/shared/Sidebar.jsx";
import {useAuth} from "./components/context/AuthContext.jsx";
import React, {useEffect, useState} from "react";
import {getUserBooking} from "./services/booking.js";
import {Spinner, WrapItem} from "@chakra-ui/react";
import BookingCard from "./components/booking/BookingCard.jsx";

const Booking = () => {
    const { user } = useAuth();
    const [loading, setLoading] = useState(false);
    const [getBookings, setBooking] = useState([]);

    useEffect(() => {
        if (!user?.userId) return;

        setLoading(true);
        getUserBooking(user.userId)
            .then((res) => setBooking(res.data))
            .catch((err) => console.error(err))
            .finally(() => setLoading(false));
    }, [user?.userId]);

    if (loading) {
        return (
            <SidebarWithHeader>
                <Spinner color="teal.500" size="lg" />
            </SidebarWithHeader>
        );
    }

    const totalBookings = getBookings.length;

    if (totalBookings === 0) {
        return (
            <SidebarWithHeader>
                <BookingCard booking={0}/>
            </SidebarWithHeader>
        );
    }

    return (
            <SidebarWithHeader>
                {getBookings.map((booking, index) => (
                    <WrapItem key={index}>
                        <BookingCard
                            booking={booking}
                        />
                    </WrapItem>
                ))}
            </SidebarWithHeader>
    );
}

export default Booking;

