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

## 25/11/2026 — Free-form ride origins
- **Decision:** Allow users to enter an origin as free text in registration, ride offers, and ride search; search uses partial, case-insensitive matching.
- **Alternatives considered:** Restricting origins to a fixed list of Canoas neighborhoods or adding an external place lookup service.
- **Rationale:** Free text supports origins outside the predefined neighborhoods without introducing an external dependency.

## 25/11/2026 — Connection failure simulation
- **Decision:** Keep the connection control as a repeatable API-failure simulation that updates the offline banner and reloads the current view.
- **Alternatives considered:** Changing the browser's real network state or only displaying a confirmation toast.
- **Rationale:** The mock failure provides a predictable way to demonstrate and verify error states without affecting the device's network.

## 25/11/2026 — Vai Junto visual identity
- **Decision:** Use the supplied Vai Junto logo as the frontend brand asset, including the sidebar and browser favicon.
- **Alternatives considered:** Keeping the previous logo or loading the image from an external service.
- **Rationale:** A local asset keeps the visual identity consistent and avoids an external runtime dependency.

## 25/11/2026 — Basic conversation flow for confirmed rides
- **Decision:** In the frontend demo, expose a per-request conversation to both the driver and passenger after confirmation; store its messages in the browser.
- **Alternatives considered:** Opening an external messaging app or implementing a fully shared backend chat immediately.
- **Rationale:** The current frontend uses a local mock API and has no authentication. Shared conversations across user sessions or devices require backend persistence and identity integration and are not provided by this demo.

<<<<<<< HEAD
## 24/11/2026 — Detailed division of team responsibilities
- **Decision:** João Cenerelli will be responsible for the HTML, CSS, and JavaScript front-end implementation and for UI/UX. Arthur will be responsible for the complete back end. Lívia will be responsible for the database, including modeling, relationships, and queries.
- **Alternatives considered:** Splitting work by complete features; alternating front-end and back-end responsibilities; or distributing all tasks without clearly assigned primary owners.
- **Rationale:** The division considers the team's current strengths and uses João's front-end expertise, Arthur's back-end expertise, and Lívia's database expertise. It reduces context switching, preserves interface consistency, and improves delivery predictability within the one-week deadline. The division does not prevent collaboration: integrations, reviews, and technical decisions will continue to be made by the whole group.

## 24/11/2026 — Collaborative documentation
- **Decision:** All project documentation will be written simultaneously by the three team members in Google Docs.
- **Alternatives considered:** Splitting individual documents among team members or assigning all documentation to one person.
- **Rationale:** Google Docs allows every member to see others' changes in real time and makes comments and joint reviews easier. It also prevents mismatches between the Portuguese and English documents.

## 24/11/2026 — Desktop interface priority
- **Decision:** Prioritize the desktop version of the front end and implement responsiveness only if sufficient time remains after completing the essential MVP flow.
- **Alternatives considered:** Starting with full responsiveness across every screen size or building desktop and mobile simultaneously.
- **Rationale:** A desktop-first implementation allows the team to deliver a complete, testable visual flow within the one-week deadline. Responsiveness will be added later so that it does not compromise mandatory MVP features.
=======
## 25/11/2026 — Back-end validation and testing
- **Decision:** Keep the MVP in memory while implementing defensive validations, synchronized state transitions, automated unit tests and centralized HTTP error responses.
- **Alternatives considered:** Introducing JPA and a database immediately.
- **Rationale:** The in-memory implementation preserves the local hackathon demo; it should be replaced by the database module after the schema and JPA entities are approved.

## 25/11/2026 — Backend responsibility and API state rules
- **Decision:** Arthur will keep the in-memory service and REST contract stable for integration; the database and frontend will implement against the documented routes and status values.
- **Alternatives considered:** Adding authentication or persistence before Lívia and João finish their modules.
- **Rationale:** The deadline favors a stable MVP contract. The backend now validates dates and times, duplicate requests, available seats, request transitions, and ride cancellation, while persistence and authentication remain integration topics.
>>>>>>> 7ff614e3ac232e0f9e4c6ca00c77065ed1f5e75a
