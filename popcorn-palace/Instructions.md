# Project Setup, Build, and Run Instructions

This document provides step-by-step instructions to set up, build, and run the project.

## Prerequisites

Before setting up the project, ensure you have the following installed on your machine:

- **Java 21** (The project uses Java 21, so make sure you have a compatible version of JDK installed)
    - Download from [OpenJDK](https://openjdk.java.net/) or use a package manager.
    - Verify installation with the command:
      ```bash
      java -version
      ```

- **Maven** (for dependency management and building the project)
    - Download from [Maven](https://maven.apache.org/download.cgi).
    - Verify installation with the command:
      ```bash
      mvn -v
      ```

- **Docker** (if you're using Docker for PostgreSQL or other services)
    - Download from [Docker](https://www.docker.com/products/docker-desktop).
    - Verify installation with the command:
      ```bash
      docker --version
      ```


## Project Setup

1. **Clone the repository**:

   If you haven't already cloned the repository, do it with the following command:

```bash
git clone "https://github.com/nadavktalav/Movie-Ticket-Booking-System-Popcorn-Palace---Backend-Development.git"
cd Movie-Ticket-Booking-System-Popcorn-Palace---Backend-Development
cd popcorn-palace
```

### 2. Install Project Dependencies

The project uses Maven for dependency management. To install the required dependencies, run the following command:

```bash
mvn install
```



### Running with Docker

The project includes Docker and Docker Compose configuration for easy setup:


1. Use Docker Compose to start both the database and application:

```bash
docker-compose up --build
```


## Database Schema

The application uses the following main entities:

- `Movie`: Contains movie details like title, genre, rating, etc.
- `Showtime`: Represents a scheduled screening of a movie at a specific time
- `Booking`: Represents a user's booking of a specific seat for a showtime

## API Endpoints

### Movies

- `GET /movies` - Get all movies
- `POST /movies` - Add a new movie
- `POST /movies/update/{movieTitle}` - Update movie details
- `DELETE /movies/{movieTitle}` - Delete a movie

### Showtimes

- `GET /showtimes/{showtimeId}` - Get showtime by ID
- `POST /showtimes` - Add a new showtime
- `POST /showtimes/update/{showtimeId}` - Update showtime details
- `DELETE /showtimes/{showtimeId}` - Delete a showtime

### Bookings

- `POST /bookings` - Book a ticket 


## Testing

The project includes unit tests and integration tests. To run the tests:

```bash
./mvnw test
```


