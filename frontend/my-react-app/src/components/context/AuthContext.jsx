import{
    createContext,
    useContext,
    useEffect,
    useState,
} from 'react';

import { login as performLogin} from "../../services/client.js"
import {jwtDecode} from "jwt-decode";

const AuthContext = createContext({});

const AuthProvider = ({ children }) => {
    const [user, setUser] = useState(null);

    const setUserFromToken = () => {
        let jwtToken = localStorage.getItem("access_token");
        if (jwtToken){
            jwtToken = jwtDecode(jwtToken);
            setUser({
                username: jwtToken.sub,
                userId: jwtToken.userId,
                roles: jwtToken.roles,
            })
        }
    }

    useEffect( () => {
        setUserFromToken();
    }, [])

    const login = async (usernameAndPassword) => {
        return new Promise((resolve, reject) => {
            performLogin(usernameAndPassword).then(
                res => {
                    const jwtToken = res.headers["authorization"];
                    localStorage.setItem("access_token", jwtToken);
                    const decodedToken = jwtDecode(jwtToken);
                    //save the token
                    setUser({
                        username: decodedToken.sub,
                        userId: decodedToken.userId,
                        roles: decodedToken.roles
                    })
                    resolve(res);
                }
            ).catch(err => reject(err));
        })
    }

    const logout = () => {
        localStorage.removeItem("access_token");
        setUser(null);
    }

    const isUserAuthenticated = () => {
        const jwtToken = localStorage.getItem("access_token");

        if (!jwtToken) {
            return false;
        }

        const {exp : expiration} = jwtDecode(jwtToken);

        if ( Date.now() > expiration * 1000){
            logout();
            return false;
        }

        return true;
    }

    return (
        <AuthContext.Provider value={{
        user,
        login,
        logout,
        isUserAuthenticated,
        setUserFromToken,
    }}>
            {children}
        </AuthContext.Provider>)
}

export const useAuth = () => useContext(AuthContext);

export default AuthProvider;