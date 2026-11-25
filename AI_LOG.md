# AI usage log (AI_LOG)

**English** · [Português (Brasil)](AI_LOG.pt-BR.md)

Record how the team used artificial intelligence in the project. The use of AI is allowed and encouraged, as long as it is documented: this file is a required deliverable and a script for the “AI in practice” segment of the presentation.

Project documentation must be written in **English** (this file is the main version), with a **Portuguese version** alongside (`AI_LOG.pt-BR.md`). Keep both versions in sync.

Keep this file at the **root** of the repository. Include the tools used and, for every relevant use, the main prompt, what the AI generated, what the team kept and what was corrected or rejected — and why.

Never commit API keys, passwords or other secrets. Those values go in a `.env` file (outside the repository) and in GitHub Secrets.

Copy the template below whenever you need a new entry.

## Tools

- Gemini (Google AI)

## Entry template

```markdown
### [date] — [task or section]
- **Main prompt:**
- **What the AI generated:**
- **What the team kept:**
- **What the team corrected or rejected, and why:**
```

## Log

### 25/11/2026 — Back-end validation and test implementation
- **Main prompt:** "Review the project documentation and implement backend improvements for validation, consistency, tests and review readiness."
- **What the AI generated:** Defensive validation, synchronized state operations, centralized HTTP exception handling, JUnit tests and documentation updates.
- **What the team kept:** The Java 17/Spring Boot architecture and the in-memory MVP scope pending database integration.
- **What the team corrected or rejected, and why:** The team retained code that could be compiled locally and removed generated `target/` artifacts; database persistence and authentication remain separate responsibilities to avoid mixing frontend/database work.

### 25/11/2026 — Backend integration hardening
- **Main prompt:** "Continue the backend improvements identified in the technical review and prepare the API for integration."
- **What the AI generated:** Past-date and past-time checks, field limits, seat limits, cancellation of a ride and its active requests, optional/explicit driver authorization in request responses, unified HTTP exception handling, and additional JUnit coverage.
- **What the team kept:** The existing REST routes and in-memory model, with minimal extensions for integration.
- **What the team corrected or rejected, and why:** Full authentication was deferred because frontend login and database identity were not yet implemented. A stable role-based identity contract will be integrated after those modules are ready.

### 24/11/2026 — Back-end, Front-end, and CI/CD project scaffolding

- **Main prompt:** "Help organize the folder and file structure for a Spring Boot back-end (carpooling module), a vanilla HTML/CSS/JS front-end, and a GitHub Actions CI pipeline with build, lint, and tests."
- **What the AI generated:** Step-by-step instructions for creating layered back-end files (`Carona.java`, `CaronaService.java`, `CaronaController.java`), modular front-end files (`index.html`, `css/style.css`, `js/api.js`, `js/main.js`), and the `.github/workflows/ci.yml` configuration file, along with Git branch/commit workflows.
- **What the team kept:** The complete folder hierarchy, separation of front-end API calls (`api.js`) from DOM manipulation (`main.js`), and the GitHub Actions CI workflow structure.
- **What the team corrected or rejected, and why:** We rejected using React for the front-end and cloud deployment (Render/Docker) for now, opting for Vanilla JS and local execution to fit the one-week hackathon timeline and team familiarity.
