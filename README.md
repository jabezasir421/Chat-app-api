# Chat App API

A real-time chat application with task management built using Spring Boot and WebSocket technology.

## Overview

Chat App API is a full-stack web application that combines real-time messaging and task tracking. Users can engage in live conversations through WebSocket connections and manage their tasks with status tracking and categorization.

## Features

### Chat Features
- **Real-time Messaging**: WebSocket-based communication for instant message delivery
- **Message History**: Retrieve recent messages with configurable limits (default: 50)
- **Persistent Storage**: All messages are stored in the database for future retrieval

### Task Management
- **Create Tasks**: Add new tasks with title, description, and category
- **View Tasks**: List tasks with filtering by status and category
- **Update Tasks**: Modify task details and status
- **Delete Tasks**: Remove tasks as needed
- **Task Status Tracking**: Track task progress with PENDING and ACTIONED states
- **User-scoped Tasks**: Each user can manage their own tasks independently

## Tech Stack

### Backend
- **Framework**: Spring Boot 3.3.5
- **Language**: Java 17
- **Data Access**: Spring Data JPA with Hibernate
- **Real-time Communication**: Spring WebSocket with STOMP protocol
- **Database**: MySQL
- **Build Tool**: Maven
- **ORM**: Hibernate with automatic schema generation

### Frontend
- **HTML5** for structure
- **CSS3** for styling
- **JavaScript** for client-side logic

### Key Dependencies
- `spring-boot-starter-web` - REST API support
- `spring-boot-starter-websocket` - WebSocket communication
- `spring-boot-starter-data-jpa` - Database ORM
- `spring-boot-starter-validation` - Input validation
- `mysql-connector-j` - MySQL database driver
- `lombok` - Code generation for getters/setters
- `spring-boot-starter-test` - Testing framework

## Project Structure

```
src/main/java/com/example/chatapp/
├── ChatAppApiApplication.java          # Main application entry point
├── config/
│   ├── WebConfig.java                  # Web configuration
│   └── WebSocketConfig.java            # WebSocket STOMP configuration
├── controller/
│   ├── ChatController.java             # Chat message endpoints
│   └── TaskController.java             # Task management endpoints
├── dto/
│   ├── ChatMessageRequest.java         # Chat message request DTO
│   ├── ChatMessageResponse.java        # Chat message response DTO
│   ├── TaskCreateRequest.java          # Task creation request DTO
│   ├── TaskResponse.java               # Task response DTO
│   └── TaskUpdateRequest.java          # Task update request DTO
├── model/
│   ├── ChatMessage.java                # Chat message entity
│   ├── Task.java                       # Task entity
│   └── TaskStatus.java                 # Task status enum
├── repo/
│   ├── ChatMessageRepository.java      # Chat message persistence
│   └── TaskRepository.java             # Task persistence
└── service/
    ├── ChatMessageService.java         # Chat business logic
    └── TaskService.java                # Task business logic

src/main/resources/
├── application.yml                     # Application configuration
└── static/
    ├── index.html                      # Main HTML page
    ├── app.css                         # Styling
    └── app.js                          # Client-side JavaScript
```

## API Endpoints

### Chat Endpoints

#### Send Message (WebSocket)
```
Message Mapping: /app/chat.send
Subscribe To: /topic/messages
```
Send a chat message via WebSocket connection.

**Request Body**:
```json
{
  "username": "string",
  "content": "string",
  "timestamp": "ISO-8601 datetime"
}
```

**Response**:
```json
{
  "id": "number",
  "username": "string",
  "content": "string",
  "timestamp": "ISO-8601 datetime"
}
```

#### Get Recent Messages
```
GET /api/messages?limit=50
```
Retrieve recent chat messages.

**Query Parameters**:
- `limit` (optional, default: 50) - Maximum number of messages to retrieve

**Response**:
```json
[
  {
    "id": "number",
    "username": "string",
    "content": "string",
    "timestamp": "ISO-8601 datetime"
  }
]
```

### Task Endpoints

All task endpoints require the `X-User-Id` header for user identification.

#### Create Task
```
POST /api/tasks
Header: X-User-Id: {userId}
```

**Request Body**:
```json
{
  "title": "string",
  "description": "string (optional)",
  "category": "string (optional)"
}
```

**Response** (201 Created):
```json
{
  "id": "number",
  "title": "string",
  "description": "string",
  "category": "string",
  "status": "PENDING|ACTIONED",
  "createdAt": "ISO-8601 datetime",
  "updatedAt": "ISO-8601 datetime"
}
```

#### List Tasks
```
GET /api/tasks
Header: X-User-Id: {userId}
```

**Query Parameters**:
- `status` (optional) - Filter by task status: PENDING, ACTIONED
- `category` (optional) - Filter by category

**Response**:
```json
[
  {
    "id": "number",
    "title": "string",
    "description": "string",
    "category": "string",
    "status": "PENDING|ACTIONED",
    "createdAt": "ISO-8601 datetime",
    "updatedAt": "ISO-8601 datetime"
  }
]
```

#### Get Task Details
```
GET /api/tasks/{id}
Header: X-User-Id: {userId}
```

**Response**:
```json
{
  "id": "number",
  "title": "string",
  "description": "string",
  "category": "string",
  "status": "PENDING|ACTIONED",
  "createdAt": "ISO-8601 datetime",
  "updatedAt": "ISO-8601 datetime"
}
```

#### Update Task
```
PUT /api/tasks/{id}
Header: X-User-Id: {userId}
```

**Request Body**:
```json
{
  "title": "string (optional)",
  "description": "string (optional)",
  "category": "string (optional)",
  "status": "PENDING|ACTIONED (optional)"
}
```

**Response**:
```json
{
  "id": "number",
  "title": "string",
  "description": "string",
  "category": "string",
  "status": "PENDING|ACTIONED",
  "createdAt": "ISO-8601 datetime",
  "updatedAt": "ISO-8601 datetime"
}
```

#### Delete Task
```
DELETE /api/tasks/{id}
Header: X-User-Id: {userId}
```

**Response**: 204 No Content

## Configuration

The application is configured via `application.yml`:

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/chatapp
    username: chatapp
    password: chatapp
  jpa:
    hibernate:
      ddl-auto: update
server:
  port: 8080
```

### Configuration Properties

| Property | Description | Default |
|----------|-------------|---------|
| `spring.datasource.url` | MySQL database connection URL | `jdbc:mysql://localhost:3306/chatapp` |
| `spring.datasource.username` | Database user | `chatapp` |
| `spring.datasource.password` | Database password | `chatapp` |
| `spring.jpa.hibernate.ddl-auto` | Schema generation strategy | `update` |
| `server.port` | Application server port | `8080` |
| `app.auth.header` | User identification header name | `X-User-Id` |

## Setup and Installation

### Prerequisites
- Java 17 or higher
- Maven 3.6+
- MySQL 5.7 or higher
- Modern web browser with WebSocket support

### Database Setup

1. Create MySQL database and user:
```sql
CREATE DATABASE chatapp;
CREATE USER 'chatapp'@'localhost' IDENTIFIED BY 'chatapp';
GRANT ALL PRIVILEGES ON chatapp.* TO 'chatapp'@'localhost';
FLUSH PRIVILEGES;
```

2. Update `application.yml` with your database credentials if different

### Build and Run

1. Clone the repository:
```bash
git clone https://github.com/yourusername/chat-app-api.git
cd chat-app-api
```

2. Build the project:
```bash
mvn clean package
```

3. Run the application:
```bash
mvn spring-boot:run
```

Or run the JAR file:
```bash
java -jar target/chat-app-api-0.0.1-SNAPSHOT.jar
```

4. Access the application:
- Open your browser and navigate to `http://localhost:8080`
- Enter a User ID
- Click "Connect" to join the chat
- Start messaging and managing tasks

## Usage

### Chat
1. Enter your User ID in the input field
2. Click "Connect" to establish WebSocket connection
3. Type your message in the chat input field
4. Click "Send" or press Enter to broadcast message to all connected users
5. Messages from all users appear in real-time in the chat log

### Tasks
1. Navigate to the Tasks section
2. **Create**: Fill in the task form and submit
3. **View**: Your tasks are listed with status and category
4. **Update**: Click on a task to edit its details or change status
5. **Delete**: Remove tasks using the delete button
6. **Filter**: Use status and category filters to organize your view

## WebSocket Connection Details

The application uses STOMP (Simple Text Oriented Messaging Protocol) over WebSocket for real-time communication.

### Connection
- **Endpoint**: `/ws`
- **Broker Prefix**: `/topic`
- **Application Prefix**: `/app`

### Message Flow
- **Send**: `/app/chat.send` → Server processes message
- **Subscribe**: `/topic/messages` → Receive broadcasted messages

## Development

### Building from Source
```bash
mvn clean install
```

### Running Tests
```bash
mvn test
```

### Code Structure
- **Controllers**: Handle HTTP requests and WebSocket messages
- **Services**: Contain business logic for chat and tasks
- **Repositories**: Manage data persistence via Spring Data JPA
- **DTOs**: Transfer data between controller and client
- **Models**: JPA entities representing database tables
- **Config**: Application configuration including WebSocket setup

## Error Handling

The API returns standard HTTP status codes:

| Status | Description |
|--------|-------------|
| 200 | Success |
| 201 | Created |
| 204 | No Content (successful delete) |
| 400 | Bad Request (validation error) |
| 404 | Not Found (resource doesn't exist) |
| 500 | Internal Server Error |

## Performance Considerations

- **Message Limits**: Default limit of 50 messages for history retrieval
- **Database**: Automatic schema generation with Hibernate
- **WebSocket**: Efficient real-time delivery with STOMP protocol
- **Connection Management**: Graceful handling of disconnections

## Future Enhancements

- User authentication and authorization
- Message editing and deletion
- Task priority levels
- Due dates for tasks
- User presence indicators
- Typing indicators
- Message attachments
- Task comments and collaboration

## License

This project is provided as-is for educational and development purposes.

## Support

For issues or questions, please create an issue in the repository or contact the development team.
