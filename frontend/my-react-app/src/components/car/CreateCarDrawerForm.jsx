import {
    Button,
    Drawer,
    DrawerOverlay,
    DrawerContent,
    DrawerHeader,
    DrawerBody,
    DrawerFooter,
    useDisclosure, DrawerCloseButton,
} from "@chakra-ui/react"
import CreateCarForm from "./CreateCarForm.jsx";

const AddIcon = () => "+"
const CloseIcon = () => "X"

const CreateCarDrawerForm = ({fetchCars}) => {
    const { isOpen, onOpen, onClose } = useDisclosure();
    return<>
            <Button
                leftIcon={<AddIcon />}
                colorScheme="teal"
                onClick={onOpen}
            >
                Add Available Cars
            </Button>

            <Drawer isOpen={isOpen} onClose={onClose} size={"xl"}>
                <DrawerOverlay />
                <DrawerContent>
                    <DrawerCloseButton/>
                    <DrawerHeader>Add Available Cars</DrawerHeader>

                    <DrawerBody>
                        <CreateCarForm fetchCars={fetchCars}/>
                    </DrawerBody>

                    <DrawerFooter>
                        <Button
                            leftIcon={<CloseIcon />}
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

export default CreateCarDrawerForm


