import { Formik, Form } from 'formik';
import * as Yup from 'yup';
import { Button, Stack} from "@chakra-ui/react";
import {updateCustomer} from "../services/client.js";
import {errorNotification, successNotification} from "../services/notification.js";
import {MyTextInput} from "./shared/MyTextInput.jsx";

// And now we can use these
const UpdateCustomerForm = ({fetchCustomers, onClose, id, initialValues}) => {
    return (
        <>
            <Formik
                initialValues={initialValues}
                validationSchema={Yup.object({
                    name: Yup.string()
                        .max(15, 'Must be 15 characters or less')
                        .required('Required'),
                    email: Yup.string()
                        .email('Invalid email address')
                        .required('Required'),
                    age: Yup.number()
                        .min(16, "Must be at least 16 years of age")
                        .max(100, 'Must be less than 100 years of age')
                        .required('Required'),
                })}
                onSubmit={(customer, { setSubmitting }) => {
                    setSubmitting(true)
                    updateCustomer(id, customer)
                        .then((res) => {
                            if(res.status===200) {
                                successNotification("Customer updated", `${customer.name} updated successfully`)
                                fetchCustomers();
                                onClose()
                            }
                        })
                        .catch(err => {
                            errorNotification(err.code, err.response.data.message)
                        })
                        .finally(() => {
                            setSubmitting(false)
                        })
                }}
            >
                {({ isValid, isSubmitting, dirty}) => (
                    <Form>
                        <Stack spacing ={"24px"}>
                            <MyTextInput
                                label="First Name"
                                name="name"
                                type="text"
                                placeholder="Jane"
                            />

                            <MyTextInput
                                label="Email Address"
                                name="email"
                                type="email"
                                placeholder="jane@formik.com"
                            />

                            <MyTextInput
                                label="Age"
                                name="age"
                                type="number"
                            />

                            <Button
                                isDisabled={ !(isValid && dirty) || isSubmitting}
                                type="submit"
                            >
                                Submit
                            </Button>
                        </Stack>
                    </Form>
                )}
            </Formik>
        </>
    );
}

export default UpdateCustomerForm;