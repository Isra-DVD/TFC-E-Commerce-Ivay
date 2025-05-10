# IVAY - E-commerce Platform

## Overview

IVAY is a modern e-commerce platform designed to provide a seamless and enjoyable online shopping experience. It features a user-friendly interface, secure payment processing, and a robust backend for managing products, orders, and customer data.

This repository contains the source code for both the backend (Java Spring Boot) and the frontend (React) components of IVAY.

## Technologies Used

*   **Backend:**
    *   Java
    *   Spring Boot
    *   Spring Data JPA (for database interaction)
    *   MySQL database
    *   Maven for build management

*   **Frontend:**
    *   React
    *   JavaScript
    *   [List any major React libraries used]
    *   npm for package management

## Project Structure

The project is organized into two main directories:

*   `ivay-backend/`: Contains the Java Spring Boot backend application.
    *   `src/main/java`: Source code for the backend.
    *   `src/main/resources`: Configuration files (e.g., `application.properties`).
    *   `pom.xml`: Maven (or Gradle) build file.
*   `ivay-frontend/`: Contains the React frontend application.
    *   `src/`: Source code for the frontend.
    *   `public/`: Static assets (e.g., `index.html`, images).
    *   `package.json`: npm package file.

## Setup and Installation

Follow these steps to set up and run IVAY on your local machine:

### Backend (Spring Boot)

1.  **Prerequisites:**
    *   Java Development Kit (JDK) 8 or higher
    *   Maven

2.  **Build and Run:**
    *   Navigate to the ivay-backend/` directory:  `cd ivay-backend`
    *   Build the application:  `mvn clean install` 
    *   Run the application:  `mvn spring-boot:run`

    The backend will start on the default port (usually 8080). You can configure this in `src/main/resources/application.properties`.

### Frontend (React)

1.  **Prerequisites:**
    *   Node.js (version 14 or higher)
    *   npm 

2.  **Install Dependencies:**
    *   Navigate to the `ivay-frontend/` directory:  `cd ivay-frontend`
    *   Install the dependencies:  `npm install`

3.  **Run the Application:**
    *   Start the development server:  `npm start`

    The frontend will start in your browser, usually on port 3000.

## Configuration

*   **Backend:**
    *   Database connection details (URL, username, password) are configured in `src/main/resources/application.properties`.
    *   CORS (Cross-Origin Resource Sharing) is configured in the backend to allow requests from the frontend.

*   **Frontend:**
    *   The API base URL for the backend is configured in the React application (usually in a configuration file or environment variable).  Make sure this points to the correct address of your running backend.

## Usage

*   **Frontend:**
    *   Open your web browser and navigate to the URL where the React application is running (e.g., `http://localhost:3000`).
    *   Browse the available products, add items to your cart, and proceed to checkout.

*   **Backend:**
    *   The backend provides REST APIs for managing products, users, and other data.
    *   [main API endpoints here]

## Contributing

We welcome contributions to IVAY! Please follow these guidelines:

1.  Fork the repository.
2.  Create a new branch for your feature or bug fix: `git checkout -b feature/your-feature-name`
3.  Make your changes and commit them with clear, concise commit messages.
4.  Push your branch to your forked repository: `git push origin feature/your-feature-name`
5.  Create a pull request to the `development` branch of the main IVAY repository.

## License

This project is licensed under the GNU GENERAL PUBLIC LICENSE - see the LICENSE file for details.

## Contact

Alvaro ([AlvaroNegrin](https://github.com/AlvaroNegrin))  <br>
usr3856@salesianos-lacuesta.net <br>
Israel ([Isra-DVD](https://github.com/Isra-DVD))  <br>
usr3849@salesianos-lacuesta.net