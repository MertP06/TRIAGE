import { createContext, useContext, useState, useEffect } from 'react';
import { encodeCredentials, validateCredentials } from '../api';

const AuthContext = createContext(null);

export const AuthProvider = ({ children }) => {
    const [user, setUser] = useState(null);
    const [isLoading, setIsLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        const stored = localStorage.getItem('auth');
        const role = localStorage.getItem('role');
        if (stored && role) {
            setUser({ role });
        }
        setIsLoading(false);
    }, []);

    const login = async (username, password) => {
        setError(null);
        setIsLoading(true);
        try {
            const data = await validateCredentials(username, password);
            const encoded = encodeCredentials(username, password);
            localStorage.setItem('auth', encoded);
            localStorage.setItem('role', data.role);
            setUser({ role: data.role, username: data.username });
            return true;
        } catch (err) {
            setError(err.message);
            return false;
        } finally {
            setIsLoading(false);
        }
    };

    const logout = () => {
        localStorage.removeItem('auth');
        localStorage.removeItem('role');
        setUser(null);
    };

    return (
        <AuthContext.Provider value={{ user, login, logout, isLoading, error }}>
            {children}
        </AuthContext.Provider>
    );
};

export const useAuth = () => useContext(AuthContext);
