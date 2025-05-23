# IVAY - E-commerce Platform

## Overview

IVAY is a modern e-commerce platform designed to provide a seamless and enjoyable online shopping experience. It features a user-friendly interface, secure authentication, and a robust backend for managing products, orders, and customer data.

This repository contains the source code for both the backend (Java Spring Boot) and the frontend (React) components of IVAY, along with Docker configurations for full deployment.

---

## Technologies Used

### 🔧 Backend

- Java 17
- Spring Boot
- Spring Data JPA
- Spring Security + JWT
- MySQL
- Maven

### 💻 Frontend

- React
- Vite
- JavaScript
- TailwindCSS
- React Router
- Axios

---

## Project Structure

```plaintext
.
├── ivay-backend/         # Spring Boot backend
│   ├── src/main/java     # Java source files
│   ├── src/main/resources
│   └── target/           # Built JAR file
│
├── ivay-frontend/        # Customer-facing React frontend
├── ivay-dashboard/       # Admin dashboard frontend
├── docker-compose.yml    # Docker service orchestration
├── .env                  # Environment configuration
└── README.md
```

---

## Setup and Installation (Manual)

### Backend (Spring Boot)

1. **Prerequisites**:

   - Java 17
   - Maven
   - MySQL

2. **Run locally**:

   ```bash
   cd ivay-backend
   ./mvn clean package -DskipTests
   java -jar target/ivay-backend-0.0.1-SNAPSHOT.jar
   ```

   By default it runs on `http://localhost:8081`.

### Frontend (React cliente/admin)

1. **Prerequisites**:

   - Node.js (v16+)
   - npm

2. **Run Frontend**:

   ```bash
   cd ivay-frontend        # or ivay-dashboard
   npm install
   npm run dev             # runs at http://localhost:3000 or 3001
   ```

---

## Docker Deployment (Recommended)

1. **Prerequisites**:

   - Docker
   - Docker Compose

2. **Build and run**:

   ```bash
   docker-compose up --build
   ```

3. **Available services**:

   - Backend: [http://localhost:8081](http://localhost:8081)
   - Cliente: [http://localhost:3000](http://localhost:3000)
   - Gestor: [http://localhost:3001](http://localhost:3001)
   - PhpMyAdmin: [http://localhost:8080](http://localhost:8080)

---

## Environment Variables

These are passed via Docker Compose or `.env` file:

| Variable         | Description                           |
| ---------------- | ------------------------------------- |
| `DB_URL`         | JDBC connection string                |
| `DB_USER`        | MySQL username                        |
| `DB_PASSWORD`    | MySQL password                        |
| `JWT_SECRET_KEY` | JWT signing secret for authentication |

---

## Initial Data Load

When the backend starts, it executes a custom class `LoadDatabase` which:

- Creates predefined roles (ADMIN, USER, etc.)
- Inserts admin/test users
- Optionally runs an SQL script (`data.sql`) for additional data

> ⚠️ Make sure your database is clean to avoid errors like `Duplicate entry`.

---

## Troubleshooting

- **Duplicate key constraint errors**:  
  This means your initial data (users, roles, etc.) already exist. You can:

  - Drop the database from MySQL / PhpMyAdmin
  - Or comment out the specific inserts in `LoadDatabase.java`.

- **Frontend MIME error**:

  ```text
  Failed to load module script: MIME type is "text/html"
  ```

  This usually means Vite wasn't properly built. Run:

  ```bash
  npm run build
  ```

- **Port conflicts**:  
  Check if `8081`, `3000`, `3001`, or `8080` are already in use.

---

## Contributing

We welcome contributions to IVAY! Please follow these guidelines:

1. Fork the repository.
2. Create a new branch: `git checkout -b feature/your-feature-name`
3. Commit your changes: `git commit -m "Describe your feature"`
4. Push and create a pull request.

---

## License

This project is licensed under the GNU GENERAL PUBLIC LICENSE - see the LICENSE file for details.

---

## Authors

- Álvaro Negrín ([AlvaroNegrin](https://github.com/AlvaroNegrin)) – `usr3856@salesianos-lacuesta.net`
- Israel Delgado ([Isra-DVD](https://github.com/Isra-DVD)) – `usr3849@salesianos-lacuesta.net`
