# FL-Finances

FL-Finances is a Kotlin-based web application for managing financial transactions. It provides functionalities for
creating users, managing user entries, and handling attachments related to entries. The application is built using Ktor
framework for the backend and SQLite for the database.

## Features

- User Management: Allows creating users with login credentials securely stored in the database.

- Entry Management: Supports creating, updating, and retrieving financial entries, including details such as amount,
  date, destination, and description.

- Attachment Handling: Enables attaching files or additional information to entries, facilitating document management.

- Authentication: Utilizes JWT (JSON Web Tokens) for authentication, ensuring secure access to the application
  endpoints.

## Installation

Clone the repository:
`git clone https://github.com/your_username/fl-finances.git`

Navigate to the project directory:
`cd fl-finances`

Build and run the application:
`./gradlew run`

Access the application at http://localhost:3000.

## Configuration

Before running the application, ensure to configure the database connection in `src/main/kotlin/Application.kt`:

```kotlin
AppDB.initialize(
  jdbcUrl = "jdbc:sqlite:./data.db",
  jdbcDriverClassName = "org.sqlite.JDBC",
  username = "your_username",
  password = "your_password"
) {
  SchemaUtils.createMissingTablesAndColumns(
    UsersTable,
    EntriesTable,
    AttachmentsTable
  )
}
```

## Usage

- Login: Obtain a JWT token by sending a POST request to `/flfinances/login` with user credentials. Use the token for
  authentication by adding it to the Authorization header:

```
Authorization: Bearer your_token_here
```

- Create Entry: Send a `POST` request to `/flfinances/users/{user_id}/entries/create` with JSON body containing entry
  details.

- Retrieve Entries: Send a `GET` request to `/flfinances/entries/` to retrieve all entries,
  or `/flfinances/users/{user_id}/entries/` to retrieve entries for a specific user.

- Update Password: Send a `PATCH` request to `/flfinances/users/{user_id}/update_password` with the new password in the
  request body.

- Create Attachment: Send a `POST` request to `/flfinances/entries/{entry_id}/attachments/create` with the attachment
  content and related entry ID.

- Retrieve Attachments: Send a `GET` request to `/flfinances/entries/{entry_id}/attachments` to retrieve attachments for
  a specific entry.

## Dependencies

- `Kotlin`: Programming language used for development;
- `Ktor`: Framework used for building the web application;
- `SQLite`: Embedded SQL database engine;
- `Exposed`: Kotlin SQL framework;
- `JWT`: JSON Web Token library for authentication.

## Contributing

Contributions are welcome! Please feel free to submit issues or pull requests.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.