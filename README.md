# VAI JUNTO | Carona Solidária — Campus life and community.

**English** · [Português (Brasil)](README.pt-BR.md)

## Team

- **Class:** TDS | mixed classes 6570, 6756 and 6863
- **Tech Lead:** Arthur
- **Presentation lead:** João
- **Members:**
  - João Cenerelli | Front-end with HTML, CSS, and JavaScript, responsible for UI/UX | GitHub
  - Arthur | Back-end | GitHub
  - Lívia | Database | GitHub

### Detailed responsibilities

- **João Cenerelli:** responsible for the complete front-end implementation with HTML, CSS, and JavaScript and for the product's UI/UX. This choice reflects João's stronger knowledge of these technologies and the need to maintain a consistent visual interface.
- **Arthur:** responsible for the complete back end, including the API, business rules, services, and integration with the front end.
- **Lívia:** responsible for the database, including entity modeling, relationships, queries, and integration of data with the back end.

The areas will be reviewed collaboratively, but this division defines each member's primary responsibility during MVP development.

All project documentation will be produced by the three team members simultaneously using Google Docs for real-time collaborative editing. Completed texts will be recorded in the repository in both English and Portuguese.

The desktop version will have visual priority. Responsiveness will be implemented only if time remains after completing the essential MVP flow.

## Theme / area

Carona solidária | Campus life and community.

## Problem

The problem we aim to solve is the difficulty of organizing and connecting people from the Senac Canoas community who have similar routes but need to find or offer rides in a practical and safe way.

## Solution

The Carona Solidária application connects students and staff from Senac Canoas who offer rides with people who need transportation, allowing routes, schedules, and available seats to be organized in a single environment.

Users can register, offer or search for rides, view compatible options, request a seat, and track the status of requests and arranged rides.

The application allows students and staff from Senac Canoas to offer and find rides among members of the community, enabling route publication and search, seat requests and approvals, as well as ride management in a single environment. The MVP includes user registration, ride offer publication, search, requests, approval or rejection, cancellations, and ride tracking, prioritizing a simple, functional, and safe flow.

Users can enter any origin location, including a neighborhood, city, or address; ride search supports partial matches.

The front end also provides a basic conversation interface for confirmed ride requests. In the current demo, messages are stored in the browser and are not synchronized between different users or devices.

**What is not included in the MVP:**
- Payments
- Real-time tracking
- Transportation marketplace
- Complex rating system
- Advanced reporting system
- Shared chat between different user sessions or devices
- Sophisticated route algorithm
- Rides open to people outside the Senac community

## Stack

Java 17 with Spring Boot (Spring Web) and Maven are used for the back-end, and pure HTML5, CSS3, and Vanilla JavaScript are used for the front-end. We have not yet chosen which database we will use.

## Folder structure

```text
/
├── .github/
│   └── workflows/
│       └── ci.yml
├── assets/
├── backend/
│   └── src/main/java/com/senac/carona/
│       ├── controller/CaronaController.java
│       ├── model/Carona.java
│       ├── service/CaronaService.java
│       └── CaronaApplication.java
├── frontend/
│   ├── css/style.css
│   ├── js/api.js
│   ├── js/main.js
│   └── index.html
└── docs/
    ├── README.md
    ├── README.pt-BR.md
    ├── DECISIONS.md
    ├── DECISIONS.pt-BR.md
    ├── AI_LOG.md
    └── AI_LOG.pt-BR.md
```

## How to run

### Prerequisites
- Java 17 (JDK)
- Maven (or the included `./mvnw` wrapper)
- Modern web browser (Chrome, Edge, Firefox)

### Back-end
```bash
cd backend
./mvnw spring-boot:run
```
The API will be available at `http://localhost:8080`.

### Front-end
Open the `frontend/index.html` file directly in your browser or use the **Live Server** extension in VS Code.

## CI/CD

The repository uses **GitHub Actions** (`.github/workflows/ci.yml`), which runs automatically on every Pull Request and push to `main`. It executes two jobs (`backend-ci` and `frontend-ci`) covering:
1. **Build:** Compiles the Java project (`./mvnw clean compile`) and validates front-end entry files.
2. **Lint:** Runs code style and syntax checks.
3. **Tests:** Runs automated unit/layer tests (`./mvnw test`).

## Conventional Commits

Use the format `<type>(<scope>): <short description>` for commit messages. Accepted types are `feat` (feature), `fix` (bug fix), `docs` (documentation), `style` (formatting without behavior changes), `refactor` (code organization without a feature or bug fix), `test` (tests), `chore` (maintenance), and `ci` (CI/CD).

Examples:
- `feat(chat): add a conversation interface for confirmed rides`
- `fix(api): correct registration validation`
- `docs(readme): explain how to run the project locally`
- `ci(actions): add a lint job to the workflow`

## Deploy

Automatic cloud deployment is not configured; the MVP runs locally for demonstration purposes.

## Links

- **Repository:**
- **Deploy (if applicable):**
- **Board / GitHub project (if applicable):**
