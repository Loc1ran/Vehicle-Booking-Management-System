import {Form, Formik, useField} from "formik";
import {Alert, AlertIcon, Box, Button, FormLabel, Input, Stack, Text} from "@chakra-ui/react";
import React, {useState} from "react";
import * as Yup from 'yup';
import {addBooking} from "../../services/booking.js";
import {successNotification, errorNotification} from "../../services/notification.js";
import {getUserById} from "../../services/client.js";
import {useAuth} from "../context/AuthContext.jsx";

const MyTextInput = ({ label, ...props }) => {
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

const BookingForm = ({regNumber, isOpen, onClose, fetchCars}) => {
    const { user } = useAuth();

    return(
        <Formik
            validateOnMount={true}
            validationSchema={Yup.object({
                bookingDay: Yup.date()
                    .required("Booking day is required")
                    .min(new Date(), "Date must be in the future"),
                location: Yup.string()
                    .required("Location is required")
                    .min(2, "Location must be at least 2 characters"),
            })}
            onSubmit={(values, {setSubmitting}) => {
                const bookingData = {
                    regNumber: regNumber,
                    userId: user.userId
                };
                addBooking(bookingData)
                    .then(res => {
                        console.log(res);
                        successNotification(
                            "Booking created",
                            `Booking for ${regNumber} was successfully created`
                        );
                        if (onClose) onClose();
                        if (fetchCars) {
                            fetchCars();
                        }
                    })
                    .catch(err => {
                        console.log(err);
                        errorNotification(
                            err.code,
                            err.response?.data?.message || "Failed to create booking"
                        );
                    })
                    .finally(() => {
                        setSubmitting(false);
                    });
            }}
            initialValues={{
                bookingDay: "",
                location: ""
            }}>
            {({isValid, isSubmitting}) => (
                <Form>
                    <Stack spacing={4}>
                        <MyTextInput
                            label="Booking Day"
                            name="bookingDay"
                            type="date"
                            placeholder="Select a date"
                        />
                        
                        <MyTextInput
                            label="Location"
                            name="location"
                            type="text"
                            placeholder="Enter pickup location"
                        />

                        <Button
                            type="submit"
                            colorScheme="teal"
                            isDisabled={!isValid || isSubmitting}
                            isLoading={isSubmitting}
                        >
                            Book Now
                        </Button>
                    </Stack>
                </Form>
            )}
        </Formik>
    )
}

export default BookingForm;