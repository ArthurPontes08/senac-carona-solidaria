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
