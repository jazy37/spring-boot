import {
    AlertDialog,
    AlertDialogBody,
    AlertDialogContent, AlertDialogFooter,
    AlertDialogHeader,
    AlertDialogOverlay,
    Button, useDisclosure
} from "@chakra-ui/react";
import {useRef} from "react";
import {deleteCustomer} from "../../services/client.js";
import {errorNotification, successNotification} from "../../services/notification.js";



function DeleteCustomerAlert({id, fetchCustomers}) {
    const { isOpen, onOpen, onClose } = useDisclosure()
    const cancelRef = useRef()

    return (
        <>
            <Button colorScheme='red' onClick={onOpen}  m={2} mt={5}>
                Delete
            </Button>

            <AlertDialog
                isOpen={isOpen}
                leastDestructiveRef={cancelRef}
                onClose={onClose}
            >
                <AlertDialogOverlay>
                    <AlertDialogContent>
                        <AlertDialogHeader fontSize='lg' fontWeight='bold'>
                            Delete Customer
                        </AlertDialogHeader>

                        <AlertDialogBody>
                            Are you sure? You can't undo this action afterwards.
                        </AlertDialogBody>

                        <AlertDialogFooter>
                            <Button ref={cancelRef} onClick={onClose}>
                                Cancel
                            </Button>
                            <Button colorScheme='red'
                                    ml={3}
                                    onClick={() => {
                                        deleteCustomer(id)
                                            .then(res => {
                                                successNotification("Success", "Customer Deleted Successfully")
                                                fetchCustomers()
                                            }).catch(err => {
                                            errorNotification("Error","Oooops! Something went wrong!")
                                        }). finally(() => onClose())
                                    }}>
                                Delete
                            </Button>
                        </AlertDialogFooter>
                    </AlertDialogContent>
                </AlertDialogOverlay>
            </AlertDialog>
        </>
    )
}

export default DeleteCustomerAlert;