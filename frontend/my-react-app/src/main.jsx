import React from "react";
import ReactDOM from "react-dom/client";
import App from "./App.jsx";
import { ChakraProvider } from "@chakra-ui/react";
import { createStandaloneToast } from '@chakra-ui/toast'
import { createBrowserRouter, RouterProvider } from "react-router-dom"
import Login from "./components/login/Login.jsx"
import AuthProvider from "./components/context/AuthContext.jsx"
import ProtectedRouter from "./components/shared/ProtectedRouter.jsx";
import RegisterUserForm from "./components/user/RegisterUserForm.jsx";
import './index.css'
import Booking from "./Booking.jsx";
import Display from "./Dashboard.jsx";

const { ToastContainer } = createStandaloneToast();
const router = createBrowserRouter([
    {
        path: "/",
        element: <Login/>

    },
    {
        path: "/dashboard",
        element: <ProtectedRouter><Display/></ProtectedRouter>
    },
    {
        path: "/dashboard/cars",
        element: <ProtectedRouter><App /></ProtectedRouter>
    },
    {
        path: "/dashboard/booking",
        element: <ProtectedRouter><Booking /></ProtectedRouter>
    },
    {
        path: "/register",
        element: <RegisterUserForm/>
    }
]);

ReactDOM.createRoot(document.getElementById("root")).render(
    <React.StrictMode>
        <ChakraProvider>
            <AuthProvider>
                <RouterProvider router = {router} />
            </AuthProvider>
            <ToastContainer/>
        </ChakraProvider>
    </React.StrictMode>
);
