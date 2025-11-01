import React, {useCallback} from 'react';
import {Formik, Form, useField} from 'formik';
import * as Yup from 'yup';
import {Alert, AlertIcon, Box, Button, FormLabel, Input, Select, Stack, VStack, Image} from "@chakra-ui/react";
import {carImageUrl, updateCar, uploadCarImage} from "../../services/car.js";
import {errorNotification, successNotification} from "../../services/notification.js";
import {useDropzone} from "react-dropzone";

const MyTextInput = ({label, ...props}) => {
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

const MySelect = ({label, ...props}) => {
    const [field, meta] = useField(props);
    return (
        <Box>
            <FormLabel htmlFor={props.id || props.name}>{label}</FormLabel>
            <Select {...field} {...props} />
            {meta.touched && meta.error ? (
                <Alert className="error" status={"error"} mt={2}>
                    <AlertIcon color="red"/>
                    {meta.error}</Alert>
            ) : null}
        </Box>
    );
};

const MyDropzone = ( {fetchCars, regNumber} ) => {
    const onDrop = useCallback(acceptedFiles => {
        const formData = new FormData();
        formData.append("file", acceptedFiles[0]);
        uploadCarImage(
            regNumber, formData
        ).then((res) => {
            successNotification("Success", "Upload car image successfully");
            fetchCars();
        }).catch((err) => {
            errorNotification("Error", "Failed to upload car image")
        })
    }, [])
    const {getRootProps, getInputProps, isDragActive} = useDropzone({onDrop})

    return (
        <Box {...getRootProps()}
             w={'100%'}
             textAlign={'center'}
             border={'dashed'}
             borderColor={'gray.200'}
             borderRadius={'3xl'}
             p={6}
             rounded={'md'}
        >
            <input {...getInputProps()} />
            {
                isDragActive ?
                    <p>Drop the files here ...</p> :
                    <p>Drag 'n' drop some files here, or click to select files</p>
            }
        </Box>
    )
}

// And now we can use these
const UpdateCarForm = ({regNumber, rentalPricePerDay, brand, electric, fetchCars}) => {
    return (
        <>
            <VStack spacing={'5'} mb={'5'}>
                <Image
                    boxSize="150px"
                    borderRadius="full"
                    objectFit="cover"
                    border="2px solid"
                    borderColor="gray.300"
                    boxShadow="md"
                    src={carImageUrl(regNumber)?.trim() || "https://cdn.pixabay.com/photo/2012/05/29/00/43/car-49278_1280.jpg"}
                    alt={`Picture of ${brand}`}
                    onError={(e) => {
                        e.target.src = "https://cdn.pixabay.com/photo/2012/05/29/00/43/car-49278_1280.jpg";
                    }}
                />
                <MyDropzone
                    regNumber={regNumber}
                    fetchCars={fetchCars}
                />
            </VStack>
            <Formik
                initialValues={{
                    regNumber: regNumber,
                    rentalPricePerDay: rentalPricePerDay,
                    brand: brand,
                    isElectric: electric,
                }}
                validationSchema={Yup.object({
                    regNumber: Yup.string()
                        .max(15, 'Must be 15 numbers or less')
                        .required('Required'),
                    rentalPricePerDay: Yup.string()
                        .matches(/^\d+(\.\d{1,2})?$/, "Must be a valid decimal with up to 2 decimals")
                        .required("Rental price is required"),
                    brand: Yup.string()
                        .oneOf(
                            ['TESLA', 'MERCEDES', 'AUDI'],
                            'Invalid Brand Type')
                        .required('Required'),

                    isElectric: Yup.boolean()
                        .oneOf(
                            [true, false]
                        )
                        .required('Required'),
                })}
                onSubmit={(values, {setSubmitting}) => {
                    setSubmitting(true);

                    const car = {
                        ...values,
                        isElectric: values.isElectric === "true",
                        rentalPricePerDay: parseFloat(values.rentalPricePerDay)
                    }

                    updateCar(regNumber, car).then(res => {
                        console.log(res);
                        fetchCars();
                        successNotification(
                            "Car updated",
                            `${car.brand} was successfully updated`
                        )
                    }).catch(err => {
                        console.log(err);
                        errorNotification(
                            err.code,
                            err.response.data.message
                        )
                    }).finally(() => setSubmitting(false))
                }}
            >
                {({isValid, isSubmitting}) => (
                    <Form>
                        <Stack spacing={"4px"}>
                            <MyTextInput
                                label="Register Number"
                                name="regNumber"
                                type="text"
                                placeholder="1234"
                            />

                            <MyTextInput
                                label="Rental Price"
                                name="rentalPricePerDay"
                                type="text"
                                placeholder="123.45"
                            />

                            <MySelect label="Brand" name="brand">
                                <option value="">Select a brand type</option>
                                <option value="TESLA">Tesla</option>
                                <option value="MERCEDES">Mercedes</option>
                                <option value="AUDI">Audi</option>
                            </MySelect>

                            <MySelect label="isElectric" name="isElectric">
                                <option value="">Select Electric</option>
                                <option value="true">True</option>
                                <option value="false">False</option>
                            </MySelect>
                            <Button isDisabled={!isValid || isSubmitting} type="submit">Submit</Button>
                        </Stack>
                    </Form>
                )}
            </Formik>
        </>
    );
};

export default UpdateCarForm;