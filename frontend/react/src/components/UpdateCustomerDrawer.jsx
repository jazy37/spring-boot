import {
    Button,
    Drawer, DrawerBody,
    DrawerCloseButton,
    DrawerContent, DrawerFooter,
    DrawerHeader,
    DrawerOverlay,
    useDisclosure
} from "@chakra-ui/react";
import UpdateCustomerForm from "./UpdateCustomerForm.jsx";

const CloseIcon = () => "X";

const UpdateCustomerDrawer = ({fetchCustomers, id, initialValues}) => {
    const { isOpen, onOpen, onClose } = useDisclosure()
    return (
        <>
            <Button
                colorScheme={"teal"}
                onClick={onOpen}
                mt={5}
            >
                Update
            </Button>

            <Drawer isOpen={isOpen} onClose={onClose} size={"xl"}>
                <DrawerOverlay />
                <DrawerContent>
                    <DrawerCloseButton />
                    <DrawerHeader>Update Customer</DrawerHeader>

                    <DrawerBody>
                        {/*<CreateCustomerForm fetchCustomers={fetchCustomers} onClose={onClose}/>*/}
                        <UpdateCustomerForm
                            fetchCustomers={fetchCustomers}
                            id={id}
                            initialValues={initialValues}
                            onClose={onClose}
                        />
                    </DrawerBody>

                    <DrawerFooter>
                        <Button
                            leftIcon={<CloseIcon />}
                            colorScheme={"teal"}
                            onClick={onClose}
                        >
                            Close
                        </Button>
                    </DrawerFooter>
                </DrawerContent>
            </Drawer>
        </>
    )
}

export default UpdateCustomerDrawer;