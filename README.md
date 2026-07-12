# TeleHealth Pro - Online Doctor Consultation Platform

A full-stack web application for booking online doctor consultations, built as **two
separate projects**:

- **`backend/`** — a Java 21 + Spring Boot 3 REST API (Spring Security, Spring Data JPA,
  MySQL 8, Maven)
- **`frontend/`** — a plain HTML5 + CSS3 + Bootstrap 5 + JavaScript static site that talks
  to the backend over HTTP (fetch)

They run as two independent processes and communicate purely over JSON REST calls.

---

## 1. Features

- Public landing page (hero, about, services, categories, testimonials, contact, footer)
- User registration with validation and BCrypt password hashing
- Spring Security session-based login with role-based access (`ROLE_USER`, `ROLE_ADMIN`)
- User dashboard showing doctor categories and available-doctor counts
- Doctor listing with search by name, filter by category, filter by availability
- Appointment booking (date + time slot) with status lifecycle: Pending → Approved/Cancelled/Completed
- Admin panel: manage doctors, categories, view users, view/update appointment status
- Admin dashboard with live stats (total users, doctors, categories, appointments, today's appointments)
- Global exception handling → consistent JSON error responses
- Bean validation (`@NotBlank`, `@Email`, `@Pattern`, `@Size`, etc.)
- Fully responsive Bootstrap 5 UI with a blue/white medical theme

---

## 2. Technology Stack

| Layer | Technology |
|---|---|
| Backend Language | Java 21 |
| Backend Framework | Spring Boot 3.3.4 (Spring MVC as REST, Spring Security, Spring Data JPA) |
| Frontend | HTML5, CSS3, Bootstrap 5, Bootstrap Icons, vanilla JavaScript (fetch API) |
| Database | MySQL 8 |
| Build Tool | Maven |
| IDE | Visual Studio Code |

---

## 3. Project Structure

```
TeleHealthPro/
├── .vscode/                    (settings, launch config, extension recommendations)
├── sql/
│   └── telehealth_pro_schema_and_data.sql
├── backend/                    <-- Spring Boot REST API
│   ├── pom.xml
│   ├── Dockerfile
│   ├── .dockerignore
│   └── src/main/
│       ├── java/com/telehealthpro/
│       │   ├── TeleHealthProApplication.java
│       │   ├── controller/     (AuthRestController, CategoryRestController,
│       │   │                    DoctorRestController, AppointmentRestController,
│       │   │                    AdminRestController)
│       │   ├── service/        (UserService, DoctorService, DoctorCategoryService,
│       │   │                    AppointmentService — each with a toResponseDto mapper)
│       │   ├── repository/     (Spring Data JPA repositories)
│       │   ├── entity/         (User, Doctor, DoctorCategory, Appointment)
│       │   ├── dto/            (request DTOs + response DTOs)
│       │   ├── config/         (SecurityConfig, DataInitializer)
│       │   ├── security/       (UserPrincipal, CustomUserDetailsService,
│       │   │                    RestAuthEntryPoint, RestAccessDeniedHandler)
│       │   └── exception/      (GlobalExceptionHandler + custom exceptions)
│       └── resources/
│           ├── application.properties       (local/dev defaults, env-var overridable)
│           └── application-prod.properties  (cross-domain deployment settings)
└── frontend/                   <-- static site, no build step required
    ├── index.html               (landing page)
    ├── register.html
    ├── login.html
    ├── dashboard.html
    ├── doctors.html
    ├── book-appointment.html
    ├── my-appointments.html
    ├── admin/
    │   ├── dashboard.html
    │   ├── doctors.html
    │   ├── add-doctor.html
    │   ├── edit-doctor.html
    │   ├── categories.html
    │   ├── users.html
    │   └── appointments.html
    ├── css/style.css
    ├── js/
    │   ├── config.js             (API_BASE_URL — the one line you edit to deploy)
    │   ├── api.js                (fetch wrapper — talks to the backend)
    │   └── auth.js               (shared navbar/session logic)
    └── images/doctor-placeholder.png
```

---

## 4. Prerequisites

1. **Java Development Kit 21** — https://adoptium.net/
2. **Maven 3.9+**
3. **MySQL Server 8.x** — https://dev.mysql.com/downloads/mysql/
4. **Visual Studio Code** — https://code.visualstudio.com/
5. A way to serve the frontend as static files (VS Code's **Live Server** extension is the
   easiest option — see below)

Verify:
```bash
java -version      # should show 21.x
mvn -version        # should show 3.9+ pointing at the Java 21 JDK
mysql --version      # should show 8.x
```

---

## 5. VS Code Setup

### 5.1 Required Extensions

Open the project folder — VS Code will prompt you to install the recommended extensions
from `.vscode/extensions.json`, or install manually:

- **Extension Pack for Java** (`vscjava.vscode-java-pack`)
- **Spring Boot Extension Pack** (`vmware.vscode-spring-boot`)
- **MySQL** (`cweijan.vscode-mysql-client2`)
- **Live Server** (`ritwickdey.liveserver`) — serves the `frontend/` folder with one click

### 5.2 Importing the Project

1. Unzip the project, e.g. to `~/projects/TeleHealthPro`.
2. **File → Open Folder…** → select the `TeleHealthPro` folder (containing both
   `backend/` and `frontend/`).
3. VS Code's Java extension will detect `backend/pom.xml` and import it as a Maven
   project automatically.

---

## 6. Database Setup

### Option A — Let Hibernate create everything (fastest)

Start MySQL, make sure a user with the credentials in `backend/src/main/resources/application.properties`
(default `root` / `root`) can connect, then just run the backend — `spring.jpa.hibernate.ddl-auto=update`
creates all tables automatically, and `DataInitializer.java` seeds the admin account and the
10 doctor categories on first run.

### Option B — Run the SQL script manually

```bash
mysql -u root -p < sql/telehealth_pro_schema_and_data.sql
```

Creates the `telehealth_pro` database, all tables, the admin account, the 10 categories,
and one sample doctor per category.

### Update credentials

Edit `backend/src/main/resources/application.properties` if your MySQL username/password differ:
```properties
spring.datasource.username=root
spring.datasource.password=root
```

---

## 7. Running the Project

You need **two things running at once**: the backend API and the frontend static site.

### 7.1 Start the backend (port 8080)

```bash
cd backend
mvn spring-boot:run
```

Wait for:
```
TeleHealth Pro is running at http://localhost:8080
```

Or run/debug `TeleHealthProApplication.java` directly from VS Code.

### 7.2 Serve the frontend

**Easiest: VS Code Live Server**
1. Right-click `frontend/index.html` in the Explorer.
2. Choose **"Open with Live Server"**.
3. It opens at something like `http://localhost:5500/frontend/index.html`.

**Alternative: Python's built-in server**
```bash
cd frontend
python -m http.server 5500
```
Then visit `http://localhost:5500`.

> **Important:** the backend's CORS configuration only allows specific origins (see
> `app.cors.allowed-origins` in `application.properties`). It already includes the common
> Live Server ports (5500, 5501) and `http://localhost:3000` / `8081`. If your frontend
> ends up on a different port, add it to that comma-separated list and restart the backend.
> Also make sure you access the frontend via `http://localhost:...` and not `http://127.0.0.1:...`
> — the session cookie is set for same-site requests, and `localhost` vs `127.0.0.1` are
> treated as different sites by the browser.

### 7.3 Log in

**Default admin login:**
- Email: `admin@telehealthpro.com`
- Password: `Admin@123`

Or register a new account from the Register page to try the regular user flow.

---

## 8. How the Frontend Talks to the Backend

- `frontend/js/config.js` defines `API_BASE_URL` (`http://localhost:8080/api` by default) and
  wraps every backend call. Change this constant if you deploy the backend elsewhere.
- Every request is sent with `credentials: "include"` so the Spring Security session cookie
  travels with it — this is what keeps you logged in across page loads.
- Login posts form-encoded data to `/api/auth/login` (Spring Security's own login processing
  URL); everything else uses regular JSON.

### API Endpoints

| Method | Path | Access | Purpose |
|---|---|---|---|
| POST | `/api/auth/register` | Public | Register a new user |
| POST | `/api/auth/login` | Public | Log in (form-encoded email/password) |
| POST | `/api/auth/logout` | Authenticated | Log out |
| GET | `/api/auth/me` | Authenticated | Current user info |
| GET | `/api/categories` | Public | List categories with doctor counts |
| GET | `/api/doctors` | Public | List/search/filter doctors |
| GET | `/api/doctors/{id}` | Public | Doctor details |
| POST | `/api/appointments` | ROLE_USER/ADMIN | Book an appointment |
| GET | `/api/appointments/my` | ROLE_USER/ADMIN | Your own appointments |
| GET/POST/PUT/DELETE | `/api/admin/doctors[...]` | ROLE_ADMIN | Manage doctors |
| GET/POST/DELETE | `/api/admin/categories[...]` | ROLE_ADMIN | Manage categories |
| GET | `/api/admin/users` | ROLE_ADMIN | List all users |
| GET | `/api/admin/appointments` | ROLE_ADMIN | List all appointments |
| PUT | `/api/admin/appointments/{id}/status` | ROLE_ADMIN | Change appointment status |
| GET | `/api/admin/stats` | ROLE_ADMIN | Dashboard stats |

---

## 9. Security Notes

- Passwords are hashed with **BCrypt**.
- Authentication is **session-cookie based** (`JSESSIONID`), scoped with CORS
  `allowCredentials(true)` for the configured frontend origins.
- Role-based authorization is enforced server-side in `SecurityConfig` for every
  `/api/admin/**`, `/api/appointments/**`, and `/api/auth/me` route.
- **CSRF protection is disabled for `/api/**`.** This is a deliberate simplification for
  this decoupled REST + static-frontend setup, common for demo/learning projects. In a
  production system, prefer either (a) a dedicated CSRF-token endpoint the frontend fetches
  and echoes back on state-changing requests, or (b) a stateless token-based auth scheme
  (e.g. JWT) instead of session cookies, so CSRF doesn't apply at all.

---

## 10. Deployment

The backend and frontend deploy independently, to any host that can run each kind of
app. Here's a combination that's genuinely free right now (verified as of mid-2026 —
hosting free tiers change often, so double-check before you commit if it's been a while):

| Piece | Recommended host | Why |
|---|---|---|
| Backend (Spring Boot) | **Render** (free Web Service) | Auto-detects the `Dockerfile` in `backend/`, free HTTPS. Free instances spin down after 15 min idle and take ~30–60s to wake back up on the next request — fine for a personal/demo project, not for something that needs to always respond instantly. |
| MySQL database | **Aiven** (Always-Free MySQL) | A genuinely permanent free MySQL tier (1 GB storage/RAM), no credit card required. *(Railway and PlanetScale both dropped their free MySQL offerings — Railway now requires a card and burns through a $5 trial credit in days; PlanetScale is paid-only since 2024. Skip both for a free setup.)* |
| Frontend (static files) | **Netlify** (or Vercel / Cloudflare Pages) | Free static hosting with HTTPS, no build step needed since this is plain HTML/CSS/JS. |

### 10.1 The one thing that will bite you: cross-domain cookies

Locally, the frontend (`localhost:5500`) and backend (`localhost:8080`) share the same
site (just different ports), so the session cookie flows fine. Once deployed, they'll
live on **completely different domains** (e.g. `your-app.netlify.app` and
`your-api.onrender.com`) — that's genuinely cross-site, and browsers block a
`SameSite=Lax` cookie (the default) from being sent on cross-site `fetch()` calls.

This project already ships the fix as a Spring profile — `application-prod.properties`
sets `SameSite=None; Secure`, which browsers *do* allow cross-site, but only over
HTTPS. Every host listed above gives you HTTPS automatically, so just activate the
profile when you deploy (see step 3 below).

### 10.2 Set up the database first (Aiven, free MySQL)

1. Sign up at [aiven.io](https://aiven.io) (no credit card needed for the free tier).
2. Create a new service → **MySQL** → select the Free plan.
3. Once it's provisioned, open the service's **Overview** tab and copy the connection
   details: host, port, username, password, and default database name.
4. You'll plug these into the backend's environment variables in the next step.

### 10.3 Deploy the backend (Render, using the Dockerfile)

1. Push this project to a GitHub repo.
2. On [Render](https://render.com) → **New → Web Service** → connect the repo →
   set **Root Directory** to `backend`. Render will detect the `Dockerfile` automatically.
3. Choose the **Free** instance type.
4. Add environment variables (Render → your service → Environment):
   ```
   SPRING_PROFILES_ACTIVE=prod
   DATABASE_URL=jdbc:mysql://<aiven-host>:<aiven-port>/<database-name>?useSSL=true&serverTimezone=UTC&allowPublicKeyRetrieval=true
   DATABASE_USERNAME=<your-aiven-username>
   DATABASE_PASSWORD=<your-aiven-password>
   CORS_ALLOWED_ORIGINS=https://your-frontend-site.netlify.app
   ```
   (`PORT` is injected automatically by Render — no need to set it.)
5. Deploy. Your API will be live at something like `https://your-api.onrender.com`.
6. Sanity check: visit `https://your-api.onrender.com/api/categories` — you should see JSON.
   (First request may take ~30–60s if the free instance had spun down.)

### 10.4 Deploy the frontend (Netlify)

1. Before deploying, edit **one line** in `frontend/js/config.js`:
   ```javascript
   const API_BASE_URL = "https://your-api.onrender.com/api";
   ```
2. On [Netlify](https://netlify.com) → **Add new site → Import an existing project** →
   connect the repo → set **Base directory** to `frontend`, leave the build command empty
   (there's nothing to build — it's static files), publish directory `frontend`.
3. Deploy. You'll get a URL like `https://your-frontend-site.netlify.app`.
4. Go back to the backend's `CORS_ALLOWED_ORIGINS` environment variable on Render and make
   sure it exactly matches this URL (no trailing slash), then redeploy the backend if you changed it.

### 10.5 Verify end-to-end

Visit your Netlify URL, try logging in with the admin demo account. Open DevTools →
Application → Cookies and confirm a `JSESSIONID` cookie appears against the backend's
domain after login — that's the sign the cross-site cookie config is working.

---

## 11. Troubleshooting

| Problem | Fix |
|---|---|
| Frontend shows "Could not load..." everywhere | Backend isn't running, or CORS is blocking the request — check the browser console for the exact error. |
| Login works but subsequent requests get 401 | Make sure you're browsing the frontend on `http://localhost:...`, not `http://127.0.0.1:...`, and that fetch calls use `credentials: "include"` (already set in `api.js`). |
| `Communications link failure` on backend startup | MySQL isn't running, or the port/credentials in `application.properties` are wrong. |
| CORS error in the browser console | Add your frontend's actual origin to `app.cors.allowed-origins` in `application.properties` and restart the backend. |
| Port 8080 already in use | Change `server.port` in `application.properties`, and update `API_BASE_URL` in `frontend/js/config.js` to match. |
| Deployed frontend can't log in / session doesn't persist | Confirm the backend is running with `SPRING_PROFILES_ACTIVE=prod` (sets `SameSite=None; Secure`) and that both sites are on HTTPS. Check DevTools → Application → Cookies for a `JSESSIONID`. |
| Deployed backend rejects requests from the frontend | `CORS_ALLOWED_ORIGINS` on the backend must exactly match the frontend's deployed URL (scheme + domain, no trailing slash). |

---

Built as a complete reference implementation — feel free to extend it with features like
video consultations, payment integration, email notifications, or JWT-based auth for a
mobile client.
