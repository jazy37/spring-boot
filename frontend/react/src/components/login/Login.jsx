'use client'

import {
    Flex,
    Box,
    Stack,
    Button,
    Heading,
    useColorModeValue, Spinner, Center,
} from '@chakra-ui/react'
import {Form, Formik} from "formik";
import * as Yup from "yup";
import {useAuth} from "../context/AuthContext.jsx";
import {MyTextInput} from "../shared/MyTextInput.jsx";
import {useNavigate} from "react-router-dom";
import {errorNotification} from "../../services/notification.js";
import {useEffect} from "react";

const LoginForm = () => {
    const { login } = useAuth()
    const navigate = useNavigate();

    return (
        <Formik
            initialValues={{
            username: '',
            password: '',
        }}
            validationSchema={Yup.object({
                username: Yup.string()
                    .email('Invalid email')
                    .required('Required'),
                password: Yup.string()
                    .max(20, "Password cannot be more than 20 characters")
                    .required('Password is required')
            })}
            onSubmit={(values, {setSubmitting}) => {
                setSubmitting(true)
                login(values).then(res => {
                    navigate("/dashboard")
                }).catch(err =>{
                    errorNotification(err.code, err.response.data.message)
                }).finally(() => setSubmitting(false))
            }}>
            {({isValid, isSubmitting}) => (
                <Form>
                    <Stack mt={15} spacing = {15}>
                        <MyTextInput
                            label="Email"
                            name="username"
                            type="text"
                        />
                        <MyTextInput
                            label="Password"
                            name="password"
                            type="password"
                        />

                        {isSubmitting ?
                            (<Center><Spinner
                            thickness='4px'
                            speed='0.65s'
                            emptyColor='gray.200'
                            color='blue.500'
                            size='xl'
                        /></Center>) : (<Button
                            type = {'submit'}
                            bg={'blue.400'}
                            color={'white'}
                            isDisabled={!isValid || isSubmitting}
                            _hover={{
                                bg: 'blue.500',
                            }}>
                            Sign in
                        </Button>)}

                    </Stack>
                </Form>
            )}
        </Formik>
    )
}

const Login = () => {
    const { customer } = useAuth()
    const navigate = useNavigate();

    useEffect(() => {
        if(customer){
            navigate("/dashboard")
        }
    })

    return (
        <Flex
            minH={'100vh'}
            align={'center'}
            justify={'center'}
            bg={useColorModeValue('gray.50', 'gray.800')}>
            <Stack spacing={8} mx={'auto'} maxW={'lg'} py={12} px={6}>
                <Stack align={'center'}>
                    <Heading fontSize={'4xl'}>Sign in to your account</Heading>
                </Stack>
                <Box
                    rounded={'lg'}
                    bg={useColorModeValue('white', 'gray.700')}
                    boxShadow={'lg'}
                    p={8}>
                    <LoginForm />
                </Box>
            </Stack>
        </Flex>
    )
}

export default Login