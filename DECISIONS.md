# Project decisions

**English** · [Português (Brasil)](DECISIONS.pt-BR.md)

Record the team's important decisions here. Each entry should be short and state what was decided, the alternatives considered and the reason for the choice.

Project documentation must be written in **English** (this file is the main version), with a **Portuguese version** alongside (`DECISIONS.pt-BR.md`). Keep both versions in sync.

Keep this file at the **root** of the repository. Add a new entry for every relevant decision (stack, architecture, MVP scope, what was left out, paths you tried and abandoned, etc.). This file is a script for the decisions segment of the presentation.

Copy the template below whenever you need a new entry.

## Template

```markdown
## [date] — [decision title]
- **Decision:**
- **Alternatives considered:**
- **Rationale:**
```

## 23/11/2026 — Back-end stack choice (example)

- **Decision:** use Node.js with Express on the back-end.
- **Alternatives considered:** Java with Spring Boot; Python with FastAPI.
- **Rationale:** the team is already more familiar with JavaScript, and a one-week deadline favors a stack everyone can contribute to from day one.


## 24/11/2026 — Back-end stack choice (Java + Spring Boot)
- **Decision:** Use Java 17 with Spring Boot (Spring Web) and Maven for the back-end API, structured in layers (`controller`, `service`, `model`).
- **Alternatives considered:** Node.js with Express; Python with FastAPI.
- **Rationale:** The team already has familiarity with Java from the technical course, and Spring Boot provides an embedded server (Tomcat) and built-in JUnit testing support, speeding up development and CI validation.

## 24/11/2026 — Front-end stack choice (HTML, CSS, and Vanilla JavaScript)
- **Decision:** Use pure HTML5, CSS3, and Vanilla JavaScript (`index.html`, `css/style.css`, `js/api.js`, `js/main.js`) for the web front-end.
- **Alternatives considered:** React with Vite.
- **Rationale:** Given the one-week deadline and the team's current learning curve with React, Vanilla JS avoids build complexity, simplifies integration with the REST API via `fetch`, and makes unit testing and linting easier in the CI pipeline.

## 24/11/2026 — CI Pipeline setup with GitHub Actions
- **Decision:** Configure a GitHub Actions workflow (`.github/workflows/ci.yml`) triggered on every Pull Request and push to `main`, covering build, lint, and automated tests for both back-end and front-end.
- **Alternatives considered:** Running checks manually before merging PRs.
- **Rationale:** Ensures code quality, prevents broken code from reaching `main`, and fulfills the project's mandatory CI/CD requirements from day one.

## 25/11/2026 — Back-end validation and testing
- **Decision:** Keep the MVP in memory while implementing defensive validations, synchronized state transitions, automated unit tests and centralized HTTP error responses.
- **Alternatives considered:** Introducing JPA and a database immediately.
- **Rationale:** The in-memory implementation preserves the local hackathon demo; it should be replaced by the database module after the schema and JPA entities are approved.
