'use client'

import {
    Button,
    Checkbox,
    Flex,
    Text,
    FormControl,
    FormLabel,
    Heading,
    Input,
    Stack,
    Image,
    Box,
    Alert,
    AlertIcon,
    Link
} from '@chakra-ui/react';
import {Formik, Form, useField} from 'formik';
import * as Yup from 'yup';
import { useAuth }   from '../context/AuthContext.jsx'
import { errorNotification } from "../../services/notification.js"
import { useNavigate } from "react-router-dom"
import React, {useEffect} from "react";

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

const LoginForm = () => {
    const { login } = useAuth();
    const navigate = useNavigate();
    return (
        <Formik
            validateOnMount = {true}
            validationSchema ={Yup.object({
                username: Yup.string().required('Username is required'),
                password: Yup.string().max(20).required('Password is required')
            })
            }
            initialValues={{username: '', password: ''}}
            onSubmit={(values, { setSubmitting }) => {
                setSubmitting(true);
                login(values).then(res => {
                    navigate("/dashboard/cars")
                    console.log("Login Successfully");
                }).catch ( err => {
                    errorNotification(
                        err.code,
                        err.response.data.message,
                    )
                }).finally(() => {
                    setSubmitting(false);
                });
            }}
        >
            {({isValid, isSubmitting}) => (
                <Form>
                    <Stack spacing={12}>
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
                        ></MyTextInput>

                        <Stack pt={6}>
                            <Text align={'center'} mb={-10} mt={-10} >
                                Not a user? <Link color={'blue.400'} href="/register">Sign up</Link>
                            </Text>
                        </Stack>

                        <Button mt = {-7}
                            type={"submit"}
                            disabled={!isValid || isSubmitting}>
                            Login
                        </Button>
                    </Stack>
                </Form>
            )}

        </Formik>
    )
}

const Login = () => {
    const { user } = useAuth();
    const navigate = useNavigate();

    useEffect(() => {
        if( user ){
            navigate("/dashboard");
        }
    });

    return (
        <Stack minH={'100vh'} direction={{ base: 'column', md: 'row' }}>
            <Flex p={8} flex={1} align={'center'} justify={'center'}>
                <Stack spacing={4} w={'full'} maxW={'md'}>
                    <Heading fontSize={'2xl'} mb={15}>Sign in to your account</Heading>
                        <LoginForm/>
                </Stack>
            </Flex>
            <Flex
                flex={1}
                p={6}
                flexDirection={"column"}
                alignItems={"center"}
                justifyContent={"center"}
            >
                <Image
                    alt={'Login Image'}
                    objectFit={'cover'}
                    src={
                        'https://api.memechua.com/upload/memes/cho-tang-ban-bong-hoa-meme.jpg'
                    }
                />
            </Flex>
        </Stack>
    )
}

export default Login;