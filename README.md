# ShortLink

A small URL shortening REST API built with Java, Spring Boot, PostgreSQL, Maven, and Docker.

## Features

- Generate a random 7-character short code for a URL
- Redirect short URLs to their original destination
- Track click counts
- Optional expiration date/time
- JSON error responses for invalid, missing, and expired links
- PostgreSQL persistence
- Docker Compose setup
- Unit tests and GitHub Actions CI

## Run with Docker

```bash
docker compose up --build
```

The API will be available at `http://localhost:8080`.

## API

### Create a short link

```bash
curl -X POST http://localhost:8080/api/links \
  -H "Content-Type: application/json" \
  -d '{"url":"https://example.com"}'
```

You can optionally include an ISO local expiration time:

```json
{
  "url": "https://example.com",
  "expiresAt": "2030-01-01T12:00:00"
}
```

### Redirect

Open `http://localhost:8080/{code}` in a browser.

### View statistics

```bash
curl http://localhost:8080/api/links/{code}
```

## Run tests

```bash
mvn test
```

## Project structure

The `link` package contains the JPA entity, repository, service, controller, and link-specific exceptions. `ApiExceptionHandler` converts common failures into useful HTTP responses.
