'use client'

import {
    Heading,
    Avatar,
    Box,
    Center,
    Image,
    Flex,
    Text,
    Stack,
    useColorModeValue, Tag,
} from '@chakra-ui/react'
import DeleteCustomerAlert from "./Alerts/DeleteCustomerAlert.jsx";
import UpdateCustomerDrawer from "./UpdateCustomerDrawer.jsx";

export default function CardWithImage({id, name, email, age, gender, imageNumber, fetchCustomers}) {
    gender = gender === "MALE" ? "men" : "women";
    return (
        <Center py={6}>
            <Box
                minW={'270px'}
                maxW={'300px'}
                w={'full'}
                bg={useColorModeValue('white', 'gray.800')}
                boxShadow={'2xl'}
                rounded={'md'}
                overflow={'hidden'}>
                <Image
                    h={'120px'}
                    w={'full'}
                    src={
                        'https://images.unsplash.com/photo-1612865547334-09cb8cb455da?ixid=MXwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHw%3D&ixlib=rb-1.2.1&auto=format&fit=crop&w=634&q=80'
                    }
                    objectFit="cover"
                    alt="#"
                />
                <Flex justify={'center'} mt={-12}>
                    <Avatar
                        size={'xl'}
                        src={
                            `https://randomuser.me/api/portraits/${gender}/${imageNumber}.jpg`
                        }
                        css={{
                            border: '2px solid white',
                        }}
                    />
                </Flex>

                <Box p={5}>
                    <Stack spacing={0} align={'center'} my={2}>
                        <Tag variant='solid' colorScheme='blue' borderRadius='full'>
                            {id}
                        </Tag>
                        <Heading fontSize={'2xl'} fontWeight={500} fontFamily={'body'}>
                            {name}
                        </Heading>
                        <Text color={'gray.500'}>{email}</Text>
                        <Text color={'gray.500'}>Age {age}</Text>
                        <Flex>
                            <DeleteCustomerAlert  id={id} fetchCustomers={fetchCustomers}/>
                            <UpdateCustomerDrawer id={id} fetchCustomers={fetchCustomers} initialValues={{name,email,age}}/>
                        </Flex>
                    </Stack>
                </Box>
            </Box>
        </Center>
    )
}