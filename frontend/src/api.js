const BASE_URL = (import.meta.env.VITE_API_BASE_URL || "http://localhost:8080").replace(/\/$/, "");

function buildAuthHeader(auth) {
    if (!auth?.username || !auth?.password) {
        return {};
    }
    const token = btoa(`${auth.username}:${auth.password}`);
    return { Authorization: `Basic ${token}` };
}

async function handleResponse(res) {
    const isNoContent = res.status === 204 || res.headers.get("Content-Length") === "0";
    if (res.ok) {
        if (isNoContent) return null;
        const text = await res.text();
        return text ? JSON.parse(text) : null;
    }

    const fallbackError = { status: res.status, message: res.statusText };
    let payload;
    try {
        payload = isNoContent ? fallbackError : await res.json();
    } catch {
        payload = fallbackError;
    }

    throw typeof payload === "object" && payload !== null ? payload : fallbackError;
}

async function request(path, { method = "GET", body, auth, headers = {} } = {}) {
    const normalizedPath = path.startsWith("/") ? path : `/${path}`;
    const init = {
        method,
        headers: {
            ...buildAuthHeader(auth),
            ...headers,
        },
    };

    if (body !== undefined) {
        init.headers = {
            "Content-Type": "application/json",
            ...init.headers,
        };
        init.body = typeof body === "string" ? body : JSON.stringify(body);
    }

    const res = await fetch(`${BASE_URL}${normalizedPath}`, init);
    return handleResponse(res);
}

export const apiGet = (path, auth, init = {}) => request(path, { ...init, method: "GET", auth });
export const apiPost = (path, body, auth, init = {}) => request(path, { ...init, method: "POST", body, auth });
export const apiPatch = (path, auth, init = {}) => request(path, { ...init, method: "PATCH", auth });
