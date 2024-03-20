import SidebarWithHeader from "./components/shared/SideBar.jsx";
import {Spinner, Text, Card, Wrap, WrapItem} from "@chakra-ui/react";
import {useEffect, useState} from "react";
import {getCustomers, deleteCustomer} from "./services/client.js";
import CardWithImage from "./components/Card.jsx";
import DrawerForm from "./components/CreateCustomerDrawer.jsx";
import {errorNotification} from "./services/notification.js";


function App() {

    const [customers, setCustomers] = useState([]);
    const [loading, setLoading] = useState(false);
    const [error, serError] = useState("")

    function fetchCustomers() {
        getCustomers()
            .then(res => {
                setCustomers(res.data)
            }).catch(err => {
                serError(err.response.data.message)
                errorNotification(err.code, err.response.data.message)
        }).finally(() => setLoading(false))
    }

    useEffect(() => {
        setLoading(true);
        fetchCustomers();
    }, []);

    if(loading) {
        return (
            <SidebarWithHeader>
                <Spinner
                    thickness='4px'
                    speed='0.65s'
                    emptyColor='gray.200'
                    color='blue.500'
                    size='xl'
                />
            </SidebarWithHeader>
            )
    }

    if(error) {
        return (
            <SidebarWithHeader>
                <DrawerForm fetchCustomers={fetchCustomers}/>
                <Text mt={5}>Ooooops there was an error</Text>
            </SidebarWithHeader>
        )
    }

    if(customers.length <= 0) {
        return (
                <SidebarWithHeader>
                    <DrawerForm fetchCustomers={fetchCustomers}/>
                    <Text mt={5}>No customers available</Text>
                </SidebarWithHeader>
        )
    }

    return (
        <div>
            <SidebarWithHeader>
                <DrawerForm fetchCustomers={fetchCustomers}/>
                <Wrap jusify={"center"} spacing={"30px"}>
                {customers.map((customer, index) => (
                     (<WrapItem key={index}>
                        <CardWithImage key={index} {...customer} imageNumber={index} fetchCustomers={fetchCustomers}/>
                    </WrapItem>)
                ))}
                </Wrap>
            </SidebarWithHeader>
        </div>
    )
}


export default App
