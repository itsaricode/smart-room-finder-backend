# 🏠 Smart Room Finder — Backend

The backend REST API for **Smart Room Finder**, developed as a Final Year Project (FYP).

The backend is responsible for application logic, API endpoints, data processing, authentication, and communication with the database.

## 🚀 Tech Stack

* Java
* Spring Boot
* Maven
* REST APIs
* Spring Data JPA
* MySQL
* JWT

## ✨ Features

* RESTful API development
* User management
* Room management
* Room-related operations
* Database integration
* Authentication and authorization
* API communication with web and mobile applications
  

## 🔗 Related Repositories

### Frontend

The web frontend is available here:

**smart-room-finder-frontend**
`https://github.com/itsaricode/smart-room-finder-frontend`

### Mobile Application

The mobile application is available here:

**smart-room-finder-mobile**

`https://github.com/itsaricode/smart-room-finder-mobile`

## 📁 Project Structure

```text
smart-room-finder-backend/
│
├── src/
│   ├── main/
│   │   ├── java/
│   │   └── resources/
│   │
│   └── test/
│
├── pom.xml
└── README.md
```

## ⚙️ Installation & Setup

### 1. Clone the repository

```bash
git clone https://github.com/itsaricode/smart-room-finder-backend.git
```

### 2. Open the project

```
cd smart-room-finder-backend
```

### 3. Configure the database

Update the database configuration in:

```text
src/main/resources/application.properties
```

### 4. Run the Spring Boot application

Using Maven:

```
mvn spring-boot:run
```

Or, if using the Maven Wrapper:

```
./mvnw spring-boot:run
```

The backend will start on the configured local port.

## 🔌 API

The backend provides REST APIs consumed by the:

* Web Frontend
* Mobile Application

API endpoints can be tested using tools such as **Postman**.

## 🔄 Application Flow

```text
Web Frontend ──────┐
                   │
                   ▼
            Spring Boot API
                   │
                   ▼
               Database
                   ▲
                   │
Mobile App ────────┘
```

