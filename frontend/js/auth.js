// ==========================================================
// TeleHealth Pro - Shared Auth / Navbar Logic
// Include this after api.js on every page.
// ==========================================================

/**
 * Renders the login/register vs. dashboard/logout links in the navbar
 * based on whether a session is currently active.
 * Call this once on page load from every page.
 *
 * Expects elements tagged with classes:
 *   .nav-guest-link  -> shown only when logged out
 *   .nav-user-link   -> shown only when logged in as ROLE_USER
 *   .nav-admin-link  -> shown only when logged in as ROLE_ADMIN
 *   .nav-auth-link   -> shown when logged in as either role (e.g. Logout button)
 */
async function initNavbar() {
    const guestLinks = document.querySelectorAll(".nav-guest-link");
    const userLinks = document.querySelectorAll(".nav-user-link");
    const adminLinks = document.querySelectorAll(".nav-admin-link");
    const authLinks = document.querySelectorAll(".nav-auth-link");

    try {
        const user = await Api.me();
        guestLinks.forEach(el => el.classList.add("d-none"));
        authLinks.forEach(el => el.classList.remove("d-none"));

        if (user.role === "ROLE_ADMIN") {
            adminLinks.forEach(el => el.classList.remove("d-none"));
            userLinks.forEach(el => el.classList.add("d-none"));
        } else {
            userLinks.forEach(el => el.classList.remove("d-none"));
            adminLinks.forEach(el => el.classList.add("d-none"));
        }
        return user;
    } catch (e) {
        // Not logged in - show guest links, hide the rest.
        guestLinks.forEach(el => el.classList.remove("d-none"));
        userLinks.forEach(el => el.classList.add("d-none"));
        adminLinks.forEach(el => el.classList.add("d-none"));
        authLinks.forEach(el => el.classList.add("d-none"));
        return null;
    }
}

// Pages under /admin/ need "../" prepended when redirecting to root-level pages.
const ROOT_PREFIX = window.location.pathname.includes("/admin/") ? "../" : "";

/**
 * Wires up any element with id="logoutBtn" to call the logout API
 * and redirect home.
 */
function wireLogoutButton() {
    const btn = document.getElementById("logoutBtn");
    if (btn) {
        btn.addEventListener("click", async (e) => {
            e.preventDefault();
            try {
                await Api.logout();
            } catch (err) {
                // Even if the request fails, drop the user back to the login page.
            }
            window.location.href = ROOT_PREFIX + "login.html";
        });
    }
}

/**
 * Call at the top of pages that require the user to be logged in.
 * Redirects to login.html if there's no active session.
 * Returns the current user object if authenticated.
 */
async function requireAuth() {
    try {
        return await Api.me();
    } catch (e) {
        window.location.href = ROOT_PREFIX + "login.html";
        return null;
    }
}

/**
 * Call at the top of admin pages. Redirects non-admins away.
 */
async function requireAdmin() {
    const user = await requireAuth();
    if (user && user.role !== "ROLE_ADMIN") {
        window.location.href = ROOT_PREFIX + "dashboard.html";
        return null;
    }
    return user;
}

function showAlert(containerId, message, type = "danger") {
    const container = document.getElementById(containerId);
    if (!container) return;
    container.innerHTML = `
        <div class="alert alert-${type} alert-auto-dismiss" role="alert">
            <i class="bi ${type === 'danger' ? 'bi-exclamation-triangle' : 'bi-check-circle'} me-2"></i>${message}
        </div>`;
}

document.addEventListener("DOMContentLoaded", () => {
    initNavbar();
    wireLogoutButton();
});
