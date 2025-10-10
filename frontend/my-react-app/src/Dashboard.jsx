import { Center, Wrap, WrapItem, Button, Spinner, Text } from '@chakra-ui/react'
import SidebarWithHeader from '../src/components/shared/Sidebar.jsx';
import {useEffect, useState} from 'react';
import {getCar} from "./services/client.js";
import ProductAddToCart from '../src/components/car/product.jsx';
import CreateCarDrawerForm from '../src/components/car/CreateCarDrawerForm.jsx';
import {errorNotification} from "./services/notification.js";

const DashBoard = () => {
    return (
        <SidebarWithHeader>
            <text>DashBoard</text>
        </SidebarWithHeader>
    )
}

export default DashBoard;