# AI usage log (AI_LOG)

**English** · [Português (Brasil)](AI_LOG.pt-BR.md)

Record how the team used artificial intelligence in the project. The use of AI is allowed and encouraged, as long as it is documented: this file is a required deliverable and a script for the “AI in practice” segment of the presentation.

Project documentation must be written in **English** (this file is the main version), with a **Portuguese version** alongside (`AI_LOG.pt-BR.md`). Keep both versions in sync.

Keep this file at the **root** of the repository. Include the tools used and, for every relevant use, the main prompt, what the AI generated, what the team kept and what was corrected or rejected — and why.

Never commit API keys, passwords or other secrets. Those values go in a `.env` file (outside the repository) and in GitHub Secrets.

Copy the template below whenever you need a new entry.

## Tools

- Gemini (Google AI)
- GitHub Copilot using Copilot SDK in VS Code

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

<<<<<<< HEAD

### 25/11/2026 — Accept free-form ride origins

- **Main prompt:** "Let users enter any origin instead of choosing only Canoas neighborhoods."
- **What the AI generated:** Text inputs for registration, ride offers, and origin search, with partial, case-insensitive matching in the frontend demo.
- **What the team kept:** Users can enter a neighborhood, city, or address without a fixed list.
- **What the team corrected or rejected, and why:** The front end remains a local mock; the change does not add geocoding or location validation.

### 25/11/2026 — Connection failure simulation

- **Main prompt:** "Fix the Simular falha de conexão control, which did nothing when clicked."
- **What the AI generated:** Connection status synchronization, the offline banner, a view reload on activation/restoration, and menu closure.
- **What the team kept:** The existing API mock failure toggle and its network-error behavior.
- **What the team corrected or rejected, and why:** The control simulates an API failure; it does not change the device's actual network state.

### 25/11/2026 — Vai Junto visual identity

- **Main prompt:** "Use the supplied Vai Junto logo throughout the project."
- **What the AI generated:** The logo asset in the frontend, sidebar branding, browser favicon, page title, and sizing styles.
- **What the team kept:** The provided image and the existing Senac context in ride-related content.
- **What the team corrected or rejected, and why:** The image was added as a local asset rather than loaded from an external service.

### 25/11/2026 — Basic chat for confirmed rides

- **Main prompt:** "After a ride request is accepted, let the driver and passenger start and exchange messages regardless of who offered the ride."
- **What the AI generated:** A conversation interface and per-request message storage in the frontend demo, with entry points for both driver and passenger.
- **What the team kept:** The conversation is only available for confirmed requests, each request has its own thread, and message content is escaped before rendering.
- **What the team corrected or rejected, and why:** The frontend still uses a local mock API, so messages are not shared across separate users or devices; shared backend persistence and user authentication remain future integration work.

=======

> > > > > > > 7ff614e3ac232e0f9e4c6ca00c77065ed1f5e75a

### 24/11/2026 — Back-end, Front-end, and CI/CD project scaffolding

- **Main prompt:** "Help organize the folder and file structure for a Spring Boot back-end (carpooling module), a vanilla HTML/CSS/JS front-end, and a GitHub Actions CI pipeline with build, lint, and tests."
- **What the AI generated:** Step-by-step instructions for creating layered back-end files (`Carona.java`, `CaronaService.java`, `CaronaController.java`), modular front-end files (`index.html`, `css/style.css`, `js/api.js`, `js/main.js`), and the `.github/workflows/ci.yml` configuration file, along with Git branch/commit workflows.
- **What the team kept:** The complete folder hierarchy, separation of front-end API calls (`api.js`) from DOM manipulation (`main.js`), and the GitHub Actions CI workflow structure.
- **What the team corrected or rejected, and why:** We rejected using React for the front-end and cloud deployment (Render/Docker) for now, opting for Vanilla JS and local execution to fit the one-week hackathon timeline and team familiarity.

### 24/11/2026 — Documentation optimization for the team responsibility division

- **Main prompt:** Organize the team's technical division in the README and both briefings, provide a detailed rationale for the decision, and record the use of AI in the documentation.
- **What the AI generated:** A bilingual responsibilities section in the README and briefings, a detailed entry in `DECISIONS.md` and `DECISIONS.pt-BR.md`, and a record of this usage in `AI_LOG.md` and `AI_LOG.pt-BR.md`.
- **What the team kept:** AI was used to accelerate writing, organize the text more clearly, and keep the English version aligned with the Portuguese version. It also helped turn a short decision into a complete rationale covering the team's strengths and the goal of reducing rework.
- **What the team corrected or rejected, and why:** Before recording the division, the team confirmed the names and responsible areas. Generic text was expanded and reviewed to reflect the project's reality without assigning work that had not been agreed upon. AI supported writing and organization; the final responsibility decision was defined and approved by the team.

### 24/11/2026 — Technical division, collaborative documentation, and desktop priority update

- **Main prompt:** Update the documentation to assign Arthur complete responsibility for the back end, Lívia responsibility for the database, and João responsibility for the front end/UI/UX; record that all documentation will be produced simultaneously by the three members in Google Docs; and record that the desktop version will be prioritized, with responsiveness added only if time remains.
- **What the AI generated:** Revised responsibility sections in the bilingual README and briefing files, updated technical decisions in `DECISIONS.md` and `DECISIONS.pt-BR.md`, and organized documentation of this update in the bilingual AI log.
- **What the team kept:** The team used AI to keep terminology and content consistent across all document versions. It also made the alternatives considered and the reasons for decisions about responsibility, collaborative documentation, and desktop priority explicit.
- **What the team corrected or rejected, and why:** The team confirmed Arthur's back-end responsibility and Lívia's database responsibility before the final documentation. They also confirmed that Google Docs will be used for real-time collaborative editing and that responsiveness is optional rather than guaranteed. AI assisted with optimizing and standardizing the documentation; the team reviewed and approved all final decisions.

### 26/11/2026 — Keyword search and profile editing
- **Main prompt:** Add keyword search to the ride-finding screen, place the benchmark reset button in the lower-right corner, and let users edit their name and profile photo from the profile menu.
- **What the AI generated:** A keyword field integrated with existing filters, searching visible ride details without accent sensitivity; a profile editor with a local image picker; image resizing to reduce storage before saving it to the local profile; and the benchmark button repositioned while keeping notifications visible.
- **What the team kept:** Search continues to work with the existing date, region, time, and seat filters, and clearing filters also clears the keyword. Profile changes update the interface and persist in the local storage used by the mock API. The existing Senac visual identity and logo were preserved.
- **What the team corrected or rejected, and why:** Photos are not sent to an external service; they remain in the browser to match the current mock API. Files larger than 8 MB or files that are not images are rejected with a visible message; accepted images are resized to at most 256 px before storage.

### 26/11/2026 — Frontend API integration and persistence
- **Main prompt:** Connect the existing frontend to the Spring Boot backend and configured database, including user session, search, offers, requests, profile, and chat.
- **What the AI generated:** Enabled frontend HTTP requests and browser persistence for the current user; added the user identifier to flows that require it; implemented backend keyword search; added persisted profile name/photo updates; added authorized conversation-message deletion; and allowed `DELETE` through CORS.
- **What the team kept:** Existing response contracts for offers, requests, and the account snapshot were retained where possible. The frontend resizes profile images before storing the JPEG Base64 value in the database; benchmark reset remains mock-only to protect shared data.
- **What the team corrected or rejected, and why:** The backend lacked profile-update and conversation-delete routes, so both were added. Search now excludes rides owned by the current user. A browser-stored `userId` supports the demo but is not a substitute for production authentication and authorization.
