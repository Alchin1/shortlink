# ShortLink

A full-stack URL shortener built with Java, Spring Boot, PostgreSQL, vanilla JavaScript, Maven, and Docker.

## Features

- Clean responsive web interface
- Generate a random 7-character short code for a URL
- Copy the generated short URL
- Redirect short URLs to their original destination
- Track click counts and view statistics
- Optional expiration date/time
- JSON error responses for invalid, missing, and expired links
- PostgreSQL persistence
- Docker Compose setup
- Unit tests and GitHub Actions CI

## Run with Docker

```bash
docker compose up --build
```

Then open `http://localhost:8080` in your browser. The web interface talks directly to the Spring Boot API.

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

The `link` package contains the JPA entity, repository, service, controller, and link-specific exceptions. `ApiExceptionHandler` converts common failures into useful HTTP responses. The frontend lives in `src/main/resources/static` and is served by Spring Boot.
