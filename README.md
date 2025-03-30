# Taskify 2.0

## Overview
Taskify is a microservice based web application built using Spring Boot. It allows users to streamline their daily
task organization.

---
## Features
* **User Authentication**: Secure login and registration system.
* **OAuth 2.0 Login**: Users can log in with their Google and GitHub.
* **Role-based Access**: Different permission for Admin and Users.
* **Password Security**: Uses encryption to keep password safe.
* **Email Verification**: Users verify their identify for added security.
* **API Rate Limiting**: Prevents too many login attempts using Spring Cloud Gateway.
* **Database Support**: Stores user details in a MySQL database for further reference.

## Tech stack
* **Spring Boot** - A Java-based framework for backend development.
* **Spring Security** - Handles user authentication and permissions.
* **OAuth 2.0** - Allows login using third party accounts.
* **JWT (JSON Web Token)** - Provides a secure way to log users in (yet to implement).
* **MySQL** - Store user information securely.
* **Redis** - Improves performance while performing CRUD operations (High throughput, Low latency).
* **BCrypt** - Encrypts passwords before storing them.