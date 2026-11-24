# Theme briefing: Community carpooling

**English** · [Português (Brasil)](BRIEFING.pt-BR.md)

**Hack with AI: Coding with Intelligence**  
Senac na Prática · Technical Program in Systems Development  
Area: campus life and community  
Immersion from 23 to 29 November · presentations on 30 November and 1 December

This briefing is the starting point for **your** theme. Read it carefully, cut an MVP that fits in one week, and record your decisions. You will turn this problem into a real product, with a front end and a back end, an organized repository, and documented use of AI. This is a collaborative immersion: the panel evaluates what the team built, decided, and learned.

## Theme and context

**Community carpooling** — A carpooling app for Senac students and staff, matching routes and schedules.

Many people reach Senac Canoas from nearby cities and neighborhoods, at similar times, with empty seats in the car or depending on buses and improvised arrangements. Carpooling already happens, but it lives in chat groups: the offer gets lost, the time does not match, and there is no simple way to agree on a route in advance — within this community, not as a service open to anyone.

You will make it easier for Senac students and staff who already travel similar routes to arrange a ride, with clear times, meeting points, and seats, and with care for safety and privacy.

## Audience and personas

- **Someone who drives:** a student or staff member who has seats in the car and a recurring or one-off route to campus (or back).
- **Someone who needs a ride:** wants an offer that matches their time and area, without exposing too much data before the arrangement is confirmed.
- **Someone planning the week:** needs to see, in one place, what is already agreed and what is still open.

## Goal

Ship an MVP in which people in the Senac community offer or look for rides, the system (with help from AI) supports matching routes and times, and an arrangement can be confirmed or cancelled clearly.

## Minimum requirements (MVP)

The scope below is realistic for one week. Lock it on the first day and record in `DECISIONS.md` what is in, what is out, and why. A complete, demonstrable flow is better than a long list left half-done.

1. A simple registration, indicating whether the person offers rides, looks for rides, or both, with a neighborhood or origin area and the campus as destination.
2. Publish an offer: origin, destination, date, time, and number of seats.
3. Search offers by day and area, seeing the essentials of the route.
4. Request a seat and let the driver accept or decline.
5. Confirm or cancel the arrangement, with a status visible to both sides.
6. List “my rides” for the week — offered and requested.

## Where AI comes in

The use of AI is allowed and encouraged — in the product and in the process — and it must appear in `AI_LOG.md`. Some concrete starting points for this theme:

- Suggest offers that fit the stated time and area.
- Help describe the route in plain language from simple points (neighborhood, main street, campus).
- Warn when the time fit is too tight.
- Summarize the day's options for the person in a short, readable list.

Do not publish, without your review, an AI-generated text or automatic decision as if it were already the final version.

## Optional extras

Only after the MVP is standing. These are ideas, not obligations:

- Recurring rides (for example, every Monday at 6 p.m.).
- A simple weekly count of seats used.
- A suggested meeting point near campus.
- A reminder on the day of the ride.

## Out of scope

To protect the deadline and the focus of the product:

- Payments, fares, or a ride marketplace.
- Real-time tracking in the style of a paid ride-hailing app.
- Rides open to the general public — the scope is the Senac community.
- A full formal complaints system or background-check process.

## Questions to guide the team

Use these questions on the first day, when you define the MVP, and again before the demo:

- In the MVP, how do you indicate that both people belong to the Senac community?
- What data is visible before the arrangement, and what appears only after confirmation?
- What happens if someone cancels at the last minute?
- How does the interface communicate safety basics (a visible meeting point, respect for times) without turning the app into a manual?
- How does matching behave when origin and destination are only approximate?

## Deliverables and how the panel evaluates

This is a one-week **collaborative immersion**, with teams of 3 or 4 TDS students. The panel evaluates **your** work, on your own project — formative feedback, not a comparison with other teams.

**What must be in the repository** (at the root), in **English** (the main version) and in **Portuguese** (`.pt-BR`), with a language link at the top of each file:

- `README.md` and `README.pt-BR.md`
- `DECISIONS.md` and `DECISIONS.pt-BR.md`
- `AI_LOG.md` and `AI_LOG.pt-BR.md`

The application must have a **front end and a back end**. The back end may be **Java**, **Python**, or **Node.js** (alone or combined, as long as they are integrated). A front end is required (web, desktop, or mobile). Justify the stack in `DECISIONS.md`. Also required: a Conventional Commits history, work through reviewed Pull Requests, a protected `main` branch, and a green **GitHub Actions** pipeline (build, lint, and tests) on PRs and on `main`. Do not commit secrets. On the first day, add the instructor as a collaborator: **adrianoanschau@gmail.com**.

**Code freeze** is on Sunday, **29 November, at 23:59**. Presentations are **15 minutes + 5 minutes of questions**, on 30 November and 1 December, at Senac Canoas, Room 103. Rehearse the demo and the required outline (problem and solution, live demo, workflow, decisions, AI in practice, and CI/CD).

Evaluation is split equally between **two panels**:

- **Technical panel (50%):** code quality and architecture; Git, Conventional Commits, and PRs; CI/CD; command of the code in the Q&A.
- **Innovation and Impact panel (50%):** originality; impact (the real problem and for whom); user experience; clarity of the pitch; creative use of AI.

Anyone on the team may be asked about any part of the project. Use AI thoughtfully, record what it generated and what you corrected, and own the code you present.

Good work — and may the week be intense, collaborative, and well documented.

