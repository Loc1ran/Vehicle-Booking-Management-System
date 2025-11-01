import {Badge, Box, Button, Card, CardBody, Center, Flex, Heading, HStack, SimpleGrid, VStack, Text} from "@chakra-ui/react";
import React from "react";

const BookingCard = ({ booking }) => {
    const getStatusColor = (status) => {
        switch (status) {
            case 'Active': return 'green';
            case 'Completed': return 'gray';
            case 'Upcoming': return 'yellow';
            default: return 'gray';
        }
    };

    return (
        <Card variant="outline">
            <CardBody>
                <VStack spacing={4} align="stretch">
                    <Box>
                        <Heading size="xl" mb={2}>My Bookings</Heading>
                        <Text color="gray.600">Manage your current and past reservations</Text>
                    </Box>
                    <Flex justify="space-between" align="center">
                        <HStack spacing={4}>
                            <Center
                                w={16}
                                h={16}
                                bg="blue.100"
                                borderRadius="lg"
                            >
                                <Text fontSize="2xl">{booking.icon}</Text>
                            </Center>
                            <Box>
                                <Heading size="md">{booking.car}</Heading>
                                <Text color="gray.600">Booking #{booking.id}</Text>
                            </Box>
                        </HStack>
                        <Badge colorScheme={getStatusColor(booking.status)}>
                            {booking.status}
                        </Badge>
                    </Flex>

                    <SimpleGrid columns={{ base: 2, md: 4 }} spacing={4}>
                        <Box>
                            <Text fontSize="sm" color="gray.500">Pickup Date</Text>
                            <Text fontWeight="medium">{booking.pickupDate}</Text>
                        </Box>
                        <Box>
                            <Text fontSize="sm" color="gray.500">Return Date</Text>
                            <Text fontWeight="medium">{booking.returnDate}</Text>
                        </Box>
                        <Box>
                            <Text fontSize="sm" color="gray.500">Duration</Text>
                            <Text fontWeight="medium">{booking.duration}</Text>
                        </Box>
                        <Box>
                            <Text fontSize="sm" color="gray.500">Total Cost</Text>
                            <Text fontWeight="medium" color="blue.500">{booking.cost}</Text>
                        </Box>
                    </SimpleGrid>

                    <HStack spacing={3}>
                        {booking.status === 'Active' || booking.status === 'Upcoming' ? (
                            <>
                                <Button colorScheme="red" size="sm">Cancel Booking</Button>
                                <Button variant="outline" size="sm">Modify Dates</Button>
                            </>
                        ) : (
                            <Button colorScheme="blue" size="sm">Book Again</Button>
                        )}
                    </HStack>
                </VStack>
            </CardBody>
        </Card>
    );
};

export default BookingCard;