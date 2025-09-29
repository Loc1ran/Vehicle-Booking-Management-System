'use client'

import {
    Flex,
    Box,
    FormControl,
    FormLabel,
    Input,
    InputGroup,
    HStack,
    InputRightElement,
    Stack,
    Button,
    Heading,
    Text,
    useColorModeValue,
    Link, Alert, AlertIcon,
} from '@chakra-ui/react'
import React, { useState } from 'react'
import {Form, Formik, useField} from "formik";
import * as Yup from "yup";
import {errorNotification, successNotification} from "../../services/notification.js";
import { saveUser } from "../../services/client.js";
import {useNavigate} from "react-router-dom";
import {useAuth} from "../context/AuthContext.jsx";

const MyTextInput = ({ label, ...props }) => {
    // useField() returns [formik.getFieldProps(), formik.getFieldMeta()]
    // which we can spread on <input>. We can use field meta to show an error
    // message if the field is invalid and it has been touched (i.e. visited)
    const [field, meta] = useField(props);
    return (
        <Box>
            <FormLabel htmlFor={props.id || props.name}>{label}</FormLabel>
            <Input className="text-input" {...field} {...props} />
            {meta.touched && meta.error ? (
                <Alert className="error" status={"error"} mt={2}>
                    <AlertIcon color="red"/>
                    {meta.error}</Alert>
            ) : null}
        </Box>
    );
};

const SignUpForm = ({ onSuccess }) => {

    return (
        <Formik
            validateOnMount = {true}
            validationSchema ={Yup.object({
                username: Yup.string().required('Username is required'),
                password: Yup.string().max(20).required('Password is required')
            })}
            initialValues={{username: '', password: ''}}
            onSubmit={(values, { setSubmitting }) => {
                setSubmitting(true);

                const user = { ...values, name: values.username };

                saveUser(user).then(res => {
                        successNotification(
                            "User created",
                            `${user.username} was successfully created`)
                       onSuccess(res.headers["authorization"]);
                    }
                ).catch( err => {
                    errorNotification(
                        err.code,
                        err.response.data.message
                    )
                }).finally(() => { setSubmitting(false) })
            }}>

            {({isValid, isSubmitting}) => (
                <Form>
                    <Stack spacing={8}>
                        <MyTextInput
                            label={"USERNAME"}
                            name={"username"}
                            type={"name"}
                            placeholder={"Enter username"}
                        ></MyTextInput>

                        <MyTextInput
                            label={"PASSWORD"}
                            name={"password"}
                            type={"password"}
                            placeholder={"Enter password"}
                        >
                        </MyTextInput>

                        <Text align={'center'}>
                            Already a user? <Link color={'blue.400'} href="/">Login</Link>
                        </Text>

                        <Button
                            loadingText="Submitting"
                            size="lg"
                            bg={'blue.400'}
                            color={'white'}
                            _hover={{
                                bg: 'blue.500',
                            }}
                            type={"submit"}
                            disabled={!isValid || isSubmitting }
                        >
                            Sign up
                        </Button>

                    </Stack>

                </Form>
            )}


        </Formik>
    )
}

const RegisterUserForm = () => {
    const {setUserFromToken} = useAuth();
    const navigate = useNavigate();

    return (
        <Flex
            minH={'100vh'}
            align={'center'}
            justify={'center'}
            bg={useColorModeValue('gray.50', 'gray.800')}>
            <Stack spacing={8} w={'full'} maxW={'md'} py={12} px={6}>
                <Stack align={'center'}>
                    <Heading fontSize={'4xl'} textAlign={'center'}>
                        Sign up
                    </Heading>
                    <Text fontSize={'lg'} color={'gray.600'}>
                        to enjoy all of our cool features ✌️
                    </Text>
                </Stack>
                <Box
                    rounded={'lg'}
                    bg={useColorModeValue('white', 'gray.700')}
                    boxShadow={'lg'}
                    p={8}>
                        <SignUpForm onSuccess={(token) => {
                            localStorage.setItem("access_token", token);
                            setUserFromToken();
                            navigate("/dashboard");
                        }}/>
                </Box>
            </Stack>
        </Flex>
    )
}
export default RegisterUserForm;