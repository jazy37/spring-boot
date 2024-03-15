import SidebarWithHeader from "./components/shared/SideBar.jsx";
import {Spinner, Text, Card, Wrap, WrapItem} from "@chakra-ui/react";
import {useEffect, useState} from "react";
import {getCustomers} from "./services/client.js";
import SocialProfileWithImage from "./components/Card.jsx";
import CardWithImage from "./components/Card.jsx";


function App() {

    const [customers, setCustomers] = useState([]);
    const [loading, setLoading] = useState(false);

    useEffect(() => {
        setLoading(true);
        getCustomers()
            .then(res => {
               setCustomers(res.data)
            }).catch(err => {
                console.log(err)
        }).finally(() => setLoading(false))
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

    if(customers.length <= 0) {
        return (
            <div>
                <SidebarWithHeader>
                    <Text>No customers available</Text>
                </SidebarWithHeader>
            </div>
        )
    }

    return (
        <div>
            <SidebarWithHeader>
                <Wrap jusify={"center"} spacing={"30px"}>
                {customers.map((customer, index) => (
                     (<WrapItem key={index}>
                        <CardWithImage key={index} {...customer} imageNumber={index} />
                    </WrapItem>)
                ))}
                </Wrap>
            </SidebarWithHeader>
        </div>
    )
}


export default App
