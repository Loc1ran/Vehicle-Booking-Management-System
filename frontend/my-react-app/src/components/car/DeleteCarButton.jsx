import React from "react";
import {
    AlertDialog,
    AlertDialogBody,
    AlertDialogContent, AlertDialogFooter,
    AlertDialogHeader,
    AlertDialogOverlay, Button,
    useDisclosure
} from "@chakra-ui/react";
import {deleteCar} from "../../services/client.js";
import {errorNotification, successNotification} from "../../services/notification.js";

const DeleteCarButton = ( {regNumber, fetchCars}) => {
    const { isOpen, onOpen, onClose } = useDisclosure()
    const cancelRef = React.useRef()


    return (
        <>
            <Button colorScheme='red' onClick={onOpen} size="xs" marginLeft={"180px"}>
                Delete Car
            </Button>

            <AlertDialog
                isOpen={isOpen}
                leastDestructiveRef={cancelRef}
                onClose={onClose}
            >
                <AlertDialogOverlay>
                    <AlertDialogContent>
                        <AlertDialogHeader fontSize='lg' fontWeight='bold'>
                            Delete Car
                        </AlertDialogHeader>

                        <AlertDialogBody>
                            Are you sure? You can't undo this action afterwards.
                        </AlertDialogBody>

                        <AlertDialogFooter>
                            <Button ref={cancelRef} onClick={onClose}>
                                Cancel
                            </Button>
                            <Button colorScheme='red' onClick={() => {deleteCar(regNumber).then(res=> {
                                fetchCars()
                                console.log(res);
                                successNotification(
                                    "Car Deleted",
                                    `Car with regNumber ${regNumber} deleted sucessfully`
                                )
                            }).catch(err => {
                                errorNotification(
                                    err.code,
                                    err.response.data.message
                                )}).finally(() => {
                                onClose()
                            })}}
                                 ml={3}>
                                Delete
                            </Button>
                        </AlertDialogFooter>
                    </AlertDialogContent>
                </AlertDialogOverlay>
            </AlertDialog>
        </>
    )
}

export default DeleteCarButton