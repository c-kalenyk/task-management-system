# Task Management System

---

### Introduction

---

The "Task Management System" is a web application for organizing your projects and tasks.
In this app, users can create projects, which they are planning to develop, add other users to join these projects, organize the development process by creating tasks, assigning users to them, uploading attachments for task if required, and commenting on tasks in case if something is not clear.
This helps users stay focused on their work without needing to jot things down on paper, and with email notifications, you’ll never have to worry about forgetting your tasks.

---

### Technologies and tools

---

- Java 21
- Spring Boot 3.3.3, Spring Security, Spring Data JPA
- MySQL 9, Liquibase
- JWT
- Maven, Docker
- Lombok, Mapstruct, Swagger
- Junit, Mockito, Testcontainers

---

### How to run:

---

1. Clone repo.
2. Install and run Docker.
3. Create a Dropbox account, create dropbox app and generate access token ([Guide](https://developers.dropbox.com/oauth-guide)).
4. Configure access parameters in the `.env` file (refer to the required fields in the `.env.sample` file).
5. Open a terminal and navigate to the root directory of the project on your machine.
6. Run the application using Docker Compose: `docker-compose up`
7. Voilà! :)

Feel free to test the available functionality using endpoints with tools like Postman or via Swagger UI, which you can find below ⬇️

---

### Application functionality

---

Currently, the application supports two roles: `ADMIN` and `USER`. The `USER` role is automatically assigned to each new user but has limited access to certain endpoints.

**Base URL**: - `http://localhost:8080/api`

### Available endpoints

**Authentication** - Endpoints for managing authentication:
- `POST: /auth/register` - Register a new user
- `POST: /auth/login` - Log in

**User** - Endpoints for managing users:
- `GET: /users/me` - Get current user's profile info
- `PUT: /users/me` - Update current user's profile info
- `PATCH: /users/{id}/roles` - Update user's roles (ADMIN only)

**Project** - Endpoints for managing projects:
- `POST: /projects` - Create a new project
- `GET: /projects` - Retrieve all projects of current user
- `GET: /projects/search` - Search for projects by parameters such as name, end date and status
- `GET: /projects/{id}` - Retrieve project details by its ID
- `PUT: /projects/{id}` - Update a project info
- `PATCH: /projects/{id}/status` - Update a project status
- `PUT: /projects/{id}/users` - Assign additional user for the project
- `PATCH: /projects/{id}/users` - Remove user from the project
- `DELETE: /projects/{id}` - Delete a project by its id

**Task** - Endpoints for managing tasks:
- `POST: /tasks` - Create a new task
- `GET: /tasks/project/{projectId}` - Retrieve all tasks for the project
- `GET: /tasks/search` - Search for tasks by parameters such as name, priority, status and date
- `GET: /tasks/{id}` - Get a task by its ID
- `PUT: /tasks/{id}` - Update a task
- `DELETE: /tasks/{id}` - Delete a task by its ID

**Label** - Endpoints for label management:
- `POST: /labels` - Create a new label
- `GET: /labels` - Retrieve all labels
- `PUT: /labels/{id}` - Update a label
- `DELETE: /labels/{id}` - Delete a label by its ID

**Comment** - Endpoints for managing comments to tasks:
- `POST: /comments` - Create a new comment
- `GET: /comments` - Retrieve all comments

**Attachment** - Endpoints for managing attachments to tasks:
- `POST: /attachments` - Upload an attachment
- `GET: /attachments` - Retrieve attachments for a task

For examples of request bodies, refer to the Swagger documentation.

---

#### [SWAGGER UI](http://localhost:8080/api/swagger-ui/index.html#/)

---