# Notes Service API

Welcome to the Notes Service API documentation. This API provides endpoints to manage and retrieve notes.

## Table of Contents

- [Introduction](#introduction)
- [Getting Started](#getting-started)
- [REST Endpoints and Usage](#rest-endpoints-and-usage)

## Introduction

The Notes Service API allows you to perform CRUD operations on notes. It supports features such as retrieving all notes,
getting a specific note by ID, creating a new note, updating an existing note, deleting a note, and fetching statistics
for a note.

## Getting Started

To run the Notes Service API locally, follow these steps:

```bash
# Clone the repository
git clone https://github.com/your-username/notes-service.git

# Change into the project directory
cd notes-service

# Build the application
./gradlew build

# Build docker image for the application
docker build -t notes .

# Start mongodb and the application
 docker-compose up -d

```

## REST Endpoints and Usage

### `GET /notes`

Get a list of notes.

#### Parameters:

- `page` (optional, default: 0): Page number.
- `sizePerPage` (optional, default: 5): Number of items per page.
- `tags` (optional, default: []): List of tags to filter notes.

#### Example:

```bash
curl -X GET "http://localhost:8080/notes?page=0&sizePerPage=5&tags=BUSINESS,PERSONAL"
```

### `GET /notes/{id}`

Get a specific note by ID.

#### Example:

```bash
curl -X GET "http://localhost:8080/notes/658899726d24166cd8696efe"
```

### `POST /notes`

Create a new note.

#### Example:

```bash
curl -X POST -H "Content-Type: application/json" -d '{"title":"New Note","text":"This is a new note."}' "http://localhost:8080/notes"
```

### `PUT /notes/{id}`

Update an existing note by ID.

#### Example:

```bash
curl -X PUT -H "Content-Type: application/json" -d '{"title":"Updated Note","text":"This note has been updated."}' "http://localhost:8080/notes/658899726d24166cd8696efe"
```

### `DELETE /notes/{id}`

Delete a note by ID.

#### Example:

```bash
curl -X DELETE "http://localhost:8080/notes/658899726d24166cd8696efe"
```

### `GET /notes/{id}/stats`

Get statistics for a specific note by ID.

#### Example:

```bash
curl -X GET "http://localhost:8080/notes/658899726d24166cd8696efe/stats"
```