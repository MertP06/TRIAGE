import { createContext, useContext, useEffect, useMemo, useState } from "react";

const AuthContext = createContext(null);

export function AuthProvider({ children }) {
    const [user, setUser] = useState(null); // null = henüz yüklenmedi, false = kullanıcı yok

    useEffect(() => {
        const loadUser = () => {
            const raw = localStorage.getItem("er_user");
            if (!raw) {
                setUser(false); // hiç giriş yapılmamış
                return;
            }

            try {
                const parsed = JSON.parse(raw);
                if (parsed?.username && parsed?.password && parsed?.role) {
                    setUser(parsed); // geçerli kullanıcı bulundu
                } else {
                    setUser(false); // veri eksik veya bozuk
                }
            } catch {
                setUser(false); // JSON parse hatası
            }
        };

        loadUser();
    }, []);

    // Giriş fonksiyonu
    const login = ({ username, password }) => {
        const role = username.toLowerCase() === "doctor" ? "DOCTOR" : "NURSE";
        const u = { username, password, role };
        setUser(u);
        localStorage.setItem("er_user", JSON.stringify(u));
    };

    // Çıkış fonksiyonu
    const logout = () => {
        setUser(false);
        localStorage.removeItem("er_user");
    };

    const value = useMemo(() => ({ user, login, logout }), [user]);
    return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}

// Hook olarak kullanılabilir
// eslint-disable-next-line react-refresh/only-export-components
export function useAuth() {
    const ctx = useContext(AuthContext);
    if (!ctx) throw new Error("useAuth must be used within AuthProvider");
    return ctx;
}
