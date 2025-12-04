const BASE = 'http://localhost:8080/api';

export const encodeCredentials = (username, password) => btoa(`${username}:${password}`);
export const decodeCredentials = (encoded) => {
    try {
        const decoded = atob(encoded);
        const [username, password] = decoded.split(':');
        return { username, password };
    } catch { return null; }
};

const authHeader = () => {
    const token = localStorage.getItem('auth');
    return token ? { Authorization: `Basic ${token}` } : {};
};

const handleResponse = async (res) => {
    if (res.status === 401) {
        localStorage.removeItem('auth');
        localStorage.removeItem('role');
        window.location.href = '/login';
        throw new Error('Oturum süresi doldu');
    }
    if (!res.ok) {
        const error = await res.json().catch(() => ({ message: 'İstek başarısız' }));
        throw new Error(error.message || 'İstek başarısız');
    }
    return res.json();
};

export const apiGet = (path) =>
    fetch(`${BASE}${path}`, { headers: authHeader() }).then(handleResponse);

export const apiPost = (path, body) =>
    fetch(`${BASE}${path}`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json', ...authHeader() },
        body: JSON.stringify(body)
    }).then(handleResponse);

export const apiPatch = (path, body) =>
    fetch(`${BASE}${path}`, {
        method: 'PATCH',
        headers: { 'Content-Type': 'application/json', ...authHeader() },
        body: JSON.stringify(body)
    }).then(handleResponse);

export const apiDelete = (path) =>
    fetch(`${BASE}${path}`, { method: 'DELETE', headers: authHeader() }).then(handleResponse);

export const validateCredentials = async (username, password) => {
    const encoded = encodeCredentials(username, password);
    const res = await fetch(`${BASE}/auth/me`, {
        headers: { Authorization: `Basic ${encoded}` }
    });
    if (!res.ok) throw new Error('Geçersiz kullanıcı adı veya şifre');
    return res.json();
};
