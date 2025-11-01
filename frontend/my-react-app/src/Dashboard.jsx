import {
    Box,
    Card,
    CardBody,
    CardHeader,
    Container,
    Flex,
    Heading,
    HStack,
    SimpleGrid, Spinner,
    Text,
    VStack, WrapItem
} from '@chakra-ui/react'
import SidebarWithHeader from '../src/components/shared/Sidebar.jsx';
import React, {useEffect, useState} from "react";
import {useAuth} from "./components/context/AuthContext.jsx";
import {carImageUrl} from "./services/car.js"
import {getUserBooking} from "./services/booking.js";


const Dashboard = ( {name, id, cars, user, totalBooking}) => {
    const [getCars, setCars] = useState([]);

    useEffect(() => {
        if (cars) {
            setCars(cars);
        }
    }, [cars]);

    const totalSpent = getCars.reduce((sum, car) => sum + (car.rentalPricePerDay || 0), 0);
    const tripsCompleted = getCars.length;

    const generateRecentActivity = () => {
        return getCars.map((car, index) => ({
            id: car.regNumber,
            car: `${car.brand} - ${car.regNumber}`,
            status: 'Booking Confirmed',
            pricePerDay: `$${car.rentalPricePerDay || 0}`,
            icon: car.isElectric ? '⚡' : '🚘',
        }))
    };

    const stats = [
        { label: 'Active Bookings', value: totalBooking, icon: '🚗', color: 'blue' },
        { label: 'Total Spent', value: `$${totalSpent.toFixed(2)}`, icon: '💰', color: 'green' },
        { label: 'Trips Completed', value: tripsCompleted, icon: '✅', color: 'purple' }
    ];

    const recentActivity = generateRecentActivity();

    return (
        <Container maxW="7xl" py={8}>
            <VStack spacing={8} align="stretch">
                <Box>
                    <Heading size="xl" mb={2}>Welcome back, {name}</Heading>
                    <Text color="gray.600">Here's your rental overview</Text>
                </Box>

                <SimpleGrid columns={{ base: 1, md: 3 }} spacing={6}>
                    {stats.map((stat, index) => (
                        <Card key={index} variant="outline">
                            <CardBody>
                                <Flex justify="space-between" align="center">
                                    <Box>
                                        <Text fontSize="sm" color="gray.500">{stat.label}</Text>
                                        <Text fontSize="2xl" fontWeight="bold" color={`${stat.color}.500`}>
                                            {stat.value}
                                        </Text>
                                    </Box>
                                    <Text fontSize="3xl">{stat.icon}</Text>
                                </Flex>
                            </CardBody>
                        </Card>
                    ))}
                </SimpleGrid>

                <Card variant="outline">
                    <CardHeader>
                        <Heading size="md">Recent Activity</Heading>
                    </CardHeader>
                    <CardBody>
                        <VStack spacing={4}>
                            {recentActivity.map((activity) => (
                                <Card key={activity.id} w="full" bg="gray.50">
                                    <CardBody>
                                        <Flex justify="space-between" align="center">
                                            <HStack spacing={3}>
                                                <Box
                                                    w={10}
                                                    h={10}
                                                    bg="blue.100"
                                                    borderRadius="full"
                                                    display="flex"
                                                    alignItems="center"
                                                    justifyContent="center"
                                                >
                                                    <Text fontSize="xl">{activity.icon}</Text>
                                                </Box>
                                                <Box>
                                                    <Text fontWeight="medium">{activity.car} - {activity.status}</Text>
                                                    <Text fontSize="sm" color="gray.500">{activity.dates}</Text>
                                                </Box>
                                            </HStack>
                                            <Text fontWeight="medium" color="green.600">{activity.amount}</Text>
                                        </Flex>
                                    </CardBody>
                                </Card>
                            ))}
                        </VStack>
                    </CardBody>
                </Card>
            </VStack>
        </Container>
    );
};

const Display = () => {
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
                <Dashboard
                    name={user?.username}
                    noBooking
                    totalBooking={totalBookings}
                />
            </SidebarWithHeader>
        );
    }

    return (
        <SidebarWithHeader>
            <Dashboard
                name={user?.username}
                cars={getBookings.flatMap(b => b.cars || [])}
                totalBooking={totalBookings}
            />
        </SidebarWithHeader>
    );
};


export default Display;