import {
    Button,
    Drawer,
    DrawerOverlay,
    DrawerContent,
    DrawerHeader,
    DrawerBody,
    DrawerFooter,
    useDisclosure, DrawerCloseButton, Flex, Tooltip, chakra, Icon,
} from "@chakra-ui/react"
import CreateCarForm from "./CreateCarForm.jsx";
import {MdEdit} from "react-icons/md";
import UpdateCarForm from "./UpdateCarForm.jsx";
import React from "react";

const CloseIcon = () => "X"

const UpdateCarDrawerForm = ({regNumber, rentalPricePerDay, brand, electric, fetchCars}) => {
    const { isOpen, onOpen, onClose } = useDisclosure()
    return<>
        <Flex justify="flex-end" alignContent="center">
            <Tooltip
                label="Edit car"
                bg="white"
                placement="top"
                color="gray.800"
                fontSize="1.2em"
                hasArrow
            >
                <chakra.a href="#" display="inline-flex" onClick={onOpen}>
                    <Icon as={MdEdit} h={5} w={7} alignSelf="center" mt={2}/>
                </chakra.a>
            </Tooltip>
        </Flex>

        <Drawer isOpen={isOpen} onClose={onClose} size={"xl"}>
            <DrawerOverlay />
            <DrawerContent>
                <DrawerCloseButton/>
                <DrawerHeader>Edit Car</DrawerHeader>

                <DrawerBody>
                    <UpdateCarForm regNumber={regNumber}
                                   rentalPricePerDay={rentalPricePerDay}
                                   brand={brand}
                                   electric={electric}
                                   fetchCars={fetchCars}/>
                </DrawerBody>

                <DrawerFooter>
                    <Button
                        leftIcon={<CloseIcon/>}
                        colorScheme="teal"
                        onClick={onClose}
                    >
                        Close
                    </Button>
                </DrawerFooter>
            </DrawerContent>
        </Drawer>
    </>
}

export default UpdateCarDrawerForm;


