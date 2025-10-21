'use client'

import {
    Flex,
    Circle,
    Box,
    Image,
    Badge,
    useColorModeValue,
    Icon,
    chakra,
    Tooltip,
    useDisclosure,
} from '@chakra-ui/react'
import { BsStar, BsStarFill, BsStarHalf } from 'react-icons/bs'
import { FiShoppingCart } from 'react-icons/fi'
import React from "react";
import DeleteCarButton from "./DeleteCarButton.jsx";
import UpdateCarDrawerForm from "./UpdateCarDrawerForm.jsx";
import {carImageUrl} from "../../services/client.js";

const data = {
    isNew: true,
    price: 4.5,
    rating: 4.2,
    numReviews: 34,
}

const AddIcon = () => "+"
const CloseIcon = () => "X"


function Rating({ rating, numReviews }) {
    return (
        <Box display="flex" alignItems="center">
            {Array(5)
                .fill('')
                .map((_, i) => {
                    const roundedRating = Math.round(rating * 2) / 2
                    if (roundedRating - i >= 1) {
                        return (
                            <BsStarFill
                                key={i}
                                style={{ marginLeft: '1' }}
                                color={i < rating ? 'teal.500' : 'gray.300'}
                            />
                        )
                    }
                    if (roundedRating - i === 0.5) {
                        return <BsStarHalf key={i} style={{ marginLeft: '1' }} />
                    }
                    return <BsStar key={i} style={{ marginLeft: '1' }} />
                })}
            <Box as="span" ml="2" color="gray.600" fontSize="sm">
                {numReviews} review{numReviews > 1 && 's'}
            </Box>
        </Box>
    )
}

function ProductAddToCart({regNumber, rentalPricePerDay, brand, electric, fetchCars}) {
    const { isOpen, onOpen, onClose } = useDisclosure()
    const cancelRef = React.useRef()
    const imageSrc = carImageUrl(regNumber);

    return (
        <Flex p={50} w="full" alignItems="center" justifyContent="center">
            <Box
                bg={useColorModeValue('white', 'gray.800')}
                maxW="sm"
                borderWidth="1px"
                rounded="lg"
                shadow="lg"
                position="relative">
                {data.isNew && (
                    <Circle size="10px" position="absolute" top={2} right={2} bg="green.200" />
                )}

                <Image src={carImageUrl(regNumber)?.trim() || "https://cdn.pixabay.com/photo/2012/05/29/00/43/car-49278_1280.jpg"}
                       alt={`Picture of ${brand}`}
                       roundedTop="lg"
                       onError={(e) => {
                           e.target.src = "https://cdn.pixabay.com/photo/2012/05/29/00/43/car-49278_1280.jpg"
                       }}/>

                <UpdateCarDrawerForm regNumber={regNumber}
                               rentalPricePerDay={rentalPricePerDay}
                               brand={brand}
                               electric={electric}
                               fetchCars={fetchCars}/>

                <Box p="6" mt={-5}>
                    <Box display="flex" alignItems="baseline">
                        {data.isNew && (
                            <Badge rounded="full" px="2" fontSize="0.8em" colorScheme="green">
                                Available
                            </Badge>
                        )}
                        <DeleteCarButton regNumber={regNumber} fetchCars={fetchCars}/>
                    </Box>
                    <Flex mt="1" justifyContent="space-between" alignContent="center">
                        <Box
                            fontSize="2xl"
                            fontWeight="semibold"
                            as="h4"
                            lineHeight="tight"
                            isTruncated>
                            {brand}
                        </Box>
                        <Tooltip
                            label="Add to cart"
                            bg="white"
                            placement={'top'}
                            color={'gray.800'}
                            fontSize={'1.2em'}>
                            <chakra.a href={'#'} display={'flex'}>
                                <Icon as={FiShoppingCart} h={7} w={7} alignSelf={'center'}  />
                            </chakra.a>
                        </Tooltip>
                    </Flex>

                    <Flex justifyContent="space-between" alignContent="center">
                        <Rating rating={data.rating} numReviews={data.numReviews} />
                        <Box fontSize="2xl" color={useColorModeValue('gray.800', 'white')}>
                            <Box as="span" color={'gray.600'} fontSize="lg">
                                $
                            </Box>
                            {rentalPricePerDay.toFixed(2)}
                        </Box>
                    </Flex>
                </Box>
            </Box>
        </Flex>
    )
}

export default ProductAddToCart