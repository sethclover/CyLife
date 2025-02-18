# CyLife

CyLife is a centralized platform for managing student organizations, providing features such as club directories, role management, event scheduling, and real-time chat. Built using Java Spring Boot for the backend, the platform enables students to discover, join, and engage with clubs efficiently.

## Features

- **Club Directory:** Browse and search for student organizations.
- **Member & Role Management:** Assign roles and manage club members.
- **Event Scheduling:** Create, update, and view upcoming club events.
- **Real-time Notifications:** Receive alerts when someone joins a club or an event is created.
- **WebSocket-based Chat:** Engage in real-time discussions within clubs.

## Tech Stack

### Backend

- **Spring Boot:** Handles API endpoints and business logic.
- **WebSockets:** Enables real-time chat and notifications.
- **Spring Security:** Manages authentication and authorization.
- **JPA (Hibernate):** Interacts with the database.
- **MySQL:** Stores user, club, and event data.

## Setup Instructions

### Prerequisites

- Java 17+
- MySQL Server
- Maven

### Installation

Clone the repository:
```sh
git clone https://github.com/yourusername/cylife.git
cd cylife
```

Configure the database in `application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/cylife
spring.datasource.username=root
spring.datasource.password=yourpassword
```

Build and run the application:
```sh
mvn spring-boot:run
```

## API Endpoints

### Authentication

- `POST /api/auth/register` - Register a new user.
- `POST /api/auth/login` - Authenticate and retrieve a JWT.

### Clubs

- `GET /api/clubs` - Fetch all clubs.
- `GET /api/clubs/{id}` - Get details of a specific club.
- `POST /api/clubs` - Create a new club (admin only).
- `PUT /api/clubs/{id}` - Update club details.
- `DELETE /api/clubs/{id}` - Delete a club.
- `GET /api/clubId/{clubEmail}` - Get club ID by email.

### Club Requests

- `GET /api/club-requests` - Get all club requests.
- `POST /api/club-requests` - Request to create a new club.
- `DELETE /api/club-requests/{id}` - Delete a club request.
- `PUT /api/club-requests/{id}/status?status={APPROVED|DECLINED}` - Update the status of a club request.

### Events

- `GET /api/events` - Get all events.
- `GET /api/upcomingEvents/{userId}` - Get upcoming events for a user within the next 7 days.
- `GET /api/events/{id}` - Get details of a specific event.
- `POST /api/events` - Create an event for a club.
- `PUT /api/events/{id}` - Update an event.
- `DELETE /api/events/{id}` - Delete an event.

### Users

- `GET /api/users` - Get all users.
- `GET /api/user/{id}` - Get details of a specific user.
- `PUT /api/update/byId/{id}` - Update user details.
- `DELETE /api/delete/{id}` - Delete a user.
- `POST /api/signup` - Register a new user.
- `POST /api/login` - Authenticate a user.
- `PUT /api/joinClub/{studentId}/{clubId}` - User joins a club.
- `GET /api/user/{userId}/clubs` - Get clubs a user is part of.
- `PUT /api/leaveClub/{userId}/{clubId}` - User leaves a club.
- `PUT /api/user/{id}/changePassword` - Change user password.
- `GET /api/checkMembershipStatus/{userId}/{clubId}` - Check if a user is a member of a club.

### WebSockets

- `/ws/events` - Notify members when a new event is created.
- `/ws/chat/{clubId}` - Club-specific real-time chat.
- `GET /api/chat/active` - Retrieve active chat sessions.
- `GET /api/chat/history/{clubId}` - Retrieve chat history for a specific club.
- `/ws/joinClub/{clubId}/{name}` - WebSocket endpoint for when a user joins a club, broadcasting a message to members.

