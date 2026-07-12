// ==========================================================
// TeleHealth Pro - API Helper
// Central place that talks to the Spring Boot backend.
// API_BASE_URL is defined in config.js (loaded before this file).
// ==========================================================

/**
 * Wrapper around fetch() that:
 *  - always sends/receives cookies (credentials: 'include') so the
 *    Spring Security session cookie works across the two origins
 *  - JSON-encodes the request body
 *  - throws a normalized Error with a readable message on failure
 */
async function apiRequest(path, { method = "GET", body, headers = {} } = {}) {
    const options = {
        method,
        credentials: "include",
        headers: {
            "Content-Type": "application/json",
            ...headers,
        },
    };
    if (body !== undefined) {
        options.body = JSON.stringify(body);
    }

    const response = await fetch(`${API_BASE_URL}${path}`, options);
    let data = null;
    try {
        data = await response.json();
    } catch (e) {
        // No JSON body (e.g. some 204 responses) - that's fine.
    }

    if (!response.ok) {
        const message = data && (data.message || Object.values(data)[0]) || `Request failed (${response.status})`;
        const error = new Error(message);
        error.status = response.status;
        error.data = data;
        throw error;
    }
    return data;
}

/**
 * Login uses Spring Security's form-login processing URL, which expects
 * standard form-encoded data (not JSON), so it gets its own helper.
 */
async function apiLogin(email, password) {
    const params = new URLSearchParams();
    params.append("email", email);
    params.append("password", password);

    const response = await fetch(`${API_BASE_URL}/auth/login`, {
        method: "POST",
        credentials: "include",
        headers: { "Content-Type": "application/x-www-form-urlencoded" },
        body: params.toString(),
    });

    let data = null;
    try {
        data = await response.json();
    } catch (e) { /* ignore */ }

    if (!response.ok) {
        throw new Error((data && data.message) || "Invalid email or password");
    }
    return data; // { id, fullName, email, role }
}

async function apiLogout() {
    return apiRequest("/auth/logout", { method: "POST" });
}

const Api = {
    // Auth
    register: (dto) => apiRequest("/auth/register", { method: "POST", body: dto }),
    login: apiLogin,
    logout: apiLogout,
    me: () => apiRequest("/auth/me"),

    // Public
    getCategories: () => apiRequest("/categories"),
    getDoctors: (params = {}) => {
        const qs = new URLSearchParams(params).toString();
        return apiRequest(`/doctors${qs ? "?" + qs : ""}`);
    },
    getDoctor: (id) => apiRequest(`/doctors/${id}`),

    // Appointments (requires auth)
    bookAppointment: (dto) => apiRequest("/appointments", { method: "POST", body: dto }),
    myAppointments: () => apiRequest("/appointments/my"),

    // Admin (requires ROLE_ADMIN)
    admin: {
        stats: () => apiRequest("/admin/stats"),
        listDoctors: () => apiRequest("/admin/doctors"),
        getDoctor: (id) => apiRequest(`/admin/doctors/${id}`),
        addDoctor: (dto) => apiRequest("/admin/doctors", { method: "POST", body: dto }),
        updateDoctor: (id, dto) => apiRequest(`/admin/doctors/${id}`, { method: "PUT", body: dto }),
        deleteDoctor: (id) => apiRequest(`/admin/doctors/${id}`, { method: "DELETE" }),

        listCategories: () => apiRequest("/admin/categories"),
        addCategory: (dto) => apiRequest("/admin/categories", { method: "POST", body: dto }),
        deleteCategory: (id) => apiRequest(`/admin/categories/${id}`, { method: "DELETE" }),

        listUsers: () => apiRequest("/admin/users"),

        listAppointments: () => apiRequest("/admin/appointments"),
        updateAppointmentStatus: (id, status) =>
            apiRequest(`/admin/appointments/${id}/status`, { method: "PUT", body: { status } }),
    },
};
