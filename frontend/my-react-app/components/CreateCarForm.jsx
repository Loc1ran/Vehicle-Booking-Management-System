import React from 'react';
import { Formik, Form, useField } from 'formik';
import * as Yup from 'yup';
import {Alert, AlertIcon, Box, Button, FormLabel, Input, Select, Stack} from "@chakra-ui/react";
import {addCar} from "../src/services/client.js";
import {errorNotification, successNotification} from "../src/services/notification.js";

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

const MySelect = ({ label, ...props }) => {
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

// And now we can use these
const CreateCarForm = ({ fetchCars }) => {
  return (
    <>
      <Formik
        initialValues={{
          regNumber: '',
          rentalPricePerDay: '',
          brand: '',
          isElectric: '',
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
        onSubmit={(values, { setSubmitting }) => {
            setSubmitting(true);

            const car = {
                ...values,
                isElectric: values.isElectric === "true",
                rentalPricePerDay: parseFloat(values.rentalPricePerDay)
            }

            addCar(car).then(res => {
                    console.log(res);
                    fetchCars();
                    successNotification(
                        "Car saved",
                        `${car.brand} was successfully saved`
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
                      <Button disable={!isValid || isSubmitting} type="submit">Submit</Button>
                  </Stack>
              </Form>
          )}
      </Formik>
    </>
  );
};

export default CreateCarForm;