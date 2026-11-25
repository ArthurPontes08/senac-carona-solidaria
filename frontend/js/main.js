const State = { view: "dashboard", tab: "offered", data: null, results: null, pending: null };
const $ = (s, r = document) => r.querySelector(s);
const $$ = (s, r = document) => [...r.querySelectorAll(s)];
const esc = (s) => String(s ?? "").replace(/[&<>"']/g, (c) => ({ "&": "&amp;", "<": "&lt;", ">": "&gt;", '"': "&quot;", "'": "&#39;" })[c]);
const fmtDate = (d) => new Date(d + "T12:00").toLocaleDateString("pt-BR", { weekday: "short", day: "2-digit", month: "2-digit" });
const STATUS = {
  pending: ["◷", "Pendente"],
  confirmed: ["✓", "Confirmada"],
  refused: ["✕", "Recusada"],
  cancelled: ["⊘", "Cancelada"],
  done: ["★", "Concluída"],
  open: ["◎", "Oferta aberta"],
  full: ["▣", "Sem vagas"],
};
const badge = (s) => `<span class="badge b-${s}"><span aria-hidden="true">${STATUS[s][0]}</span>${STATUS[s][1]}</span>`;

const UI = {
  toast(msg, error) {
    const t = document.createElement("div");
    t.className = "toast" + (error ? " error" : "");
    t.textContent = msg;
    $("#toasts").append(t);
    setTimeout(() => t.remove(), 4000);
  },
  skeleton: (n) => Array.from({ length: n }, () => '<div class="skel" aria-hidden="true"></div>').join(""),
  empty: (t, p, btn) => `<div class="state"><h3>${t}</h3><p class="muted">${p}</p>${btn || ""}</div>`,
  error: (act) => `<div class="state state-err" role="alert"><h3>Não conseguimos carregar as caronas agora.</h3><p>Verifique sua conexão e tente de novo.</p><button class="btn btn-neutral" data-retry="${act}">Tentar novamente</button></div>`,
  async busy(btn, fn) {
    btn.disabled = true;
    btn.classList.add("loading");
    try {
      return await fn();
    } finally {
      btn.disabled = false;
      btn.classList.remove("loading");
    }
  },
  confirm(title, text, yesLabel) {
    return new Promise((res) => {
      const d = $("#dlg-confirm");
      $("#t-conf").textContent = title;
      $("#confText").textContent = text;
      $("#confYes").textContent = yesLabel;
      d.onclose = () => res(d.returnValue === "yes");
      d.returnValue = "no";
      d.showModal();
    });
  },
  field(el, msg) {
    const e = el.parentElement.querySelector(".err");
    el.classList.toggle("invalid", !!msg);
    el.classList.toggle("valid", !msg && !!el.value);
    el.setAttribute("aria-invalid", !!msg);
    if (e) {
      e.textContent = msg || "";
      e.id = e.id || "e" + Math.random().toString(36).slice(2);
      el.setAttribute("aria-describedby", e.id);
    }
    return !msg;
  },
};

const offerCard = (o) => `<article class="card ride"><h3>${esc(o.origin)} → Senac Canoas ${o.seats ? badge("open") : badge("full")}</h3>
  <p>${esc(o.summary)}</p>
  <div class="meta"><span>${esc(o.driver)} · vínculo Senac</span><span>${fmtDate(o.date)}, ${o.time}</span><span>${o.seats} vaga(s)</span></div>
  <p class="match">Compatibilidade aproximada: ${o.match}%. Confira antes de solicitar.</p>
  <p class="private">Ponto aproximado: ${esc(o.ref)}. Contato visível após a confirmação.</p>
  <div class="actions"><button class="btn btn-primary" data-request="${o.id}" ${o.seats ? "" : "disabled"}>Solicitar vaga</button></div></article>`;

function renderDashboard() {
  const { offers, requests } = State.data,
    mineO = offers.filter((o) => o.mine),
    mineR = requests.filter((r) => r.mine);
  const received = requests.filter((r) => !r.mine),
    pend = received.filter((r) => r.status === "pending");
  const next = mineR.filter((r) => r.status === "confirmed" && r.date >= new Date().toISOString().slice(0, 10)).sort((a, b) => a.date.localeCompare(b.date))[0];
  const nextOffer = next && offers.find((o) => o.id === next.offerId);
  const cnt = (s) => requests.filter((r) => r.status === s).length;
  $("#view-dashboard").innerHTML = `
    <p class="lead">${next ? "Sua próxima carona está confirmada." : "Você ainda não tem carona confirmada."}</p>
    ${
      next
        ? `<div class="card next"><h2>${fmtDate(next.date)}, ${next.time} · ${esc(nextOffer.origin)} → Senac Canoas</h2>
      <p class="muted">Motorista: ${esc(nextOffer.driver)}</p><p class="private">Ponto de encontro combinado: ${esc(nextOffer.ref)}</p></div>`
        : UI.empty("Nada confirmado por enquanto", "Busque uma carona compatível com seu horário.", '<button class="btn btn-primary" data-go="search">Buscar caronas</button>')
    }
    <div class="stats">
      <div class="card stat"><b>${mineO.length}</b><span>Caronas oferecidas</span></div>
      <div class="card stat"><b>${mineR.length}</b><span>Vagas solicitadas</span></div>
      <div class="card stat"><b>${cnt("confirmed")}</b><span>Confirmadas</span></div>
      <div class="card stat"><b>${cnt("cancelled")}</b><span>Cancelamentos</span></div></div>
    <div class="card"><h2>Solicitações pendentes</h2>${pend.length ? `<p class="muted">${pend.length} pessoa(s) aguardando sua resposta.</p><div class="actions"><button class="btn btn-secondary" data-go="requests">Ver solicitações</button></div>` : '<p class="muted">Nenhuma solicitação aguardando resposta.</p>'}</div>`;
  const c = $("#reqCount");
  c.textContent = pend.length;
  c.hidden = !pend.length;
}

function renderRequests() {
  const list = State.data.requests.filter((r) => !r.mine);
  $("#view-requests").innerHTML = list.length
    ? `<div class="grid">${list
        .map(
          (r) => `<article class="card ride"><h3>${esc(r.who)} ${badge(r.status)}</h3>
    <div class="meta"><span>Região: ${esc(r.region)}</span><span>${fmtDate(r.date)}, ${r.time}</span></div>
    ${r.msg ? `<p>“${esc(r.msg)}”</p>` : '<p class="muted">Sem mensagem.</p>'}
    ${r.status === "pending" ? `<div class="actions"><button class="btn btn-danger" data-act="recusar" data-id="${r.id}">Recusar</button><button class="btn btn-primary" data-act="aceitar" data-id="${r.id}">Aceitar</button></div>` : r.status === "confirmed" ? `<p class="private">Confirmado. Combinem o ponto de encontro exato.</p><div class="actions"><button class="btn btn-danger" data-act="cancelar" data-id="${r.id}">Cancelar carona</button></div>` : ""}</article>`,
        )
        .join("")}</div>`
    : UI.empty("Nenhuma solicitação recebida", "Quando alguém pedir uma vaga, ela aparece aqui.", '<button class="btn btn-primary" data-open="publish">Oferecer carona</button>');
}

function renderRides() {
  $$(".tab").forEach((t) => t.setAttribute("aria-selected", t.dataset.tab === State.tab));
  const { offers, requests } = State.data;
  let rows;
  if (State.tab === "offered")
    rows = offers
      .filter((o) => o.mine)
      .map((o) => {
        const p =
          requests
            .filter((r) => r.offerId === o.id && r.status === "confirmed")
            .map((r) => r.who)
            .join(", ") || "Ninguém ainda";
        return `<tr><td>${esc(o.origin)}</td><td>Senac Canoas</td><td>${fmtDate(o.date)}</td><td>${o.time}</td><td>${esc(p)}</td><td>${o.seats}</td><td>${badge(o.seats ? "open" : "full")}</td><td></td></tr>`;
      });
  else
    rows = requests
      .filter((r) => r.mine)
      .map((r) => {
        const o = offers.find((x) => x.id === r.offerId);
        return `<tr><td>${esc(r.region)}</td><td>Senac Canoas</td><td>${fmtDate(r.date)}</td><td>${r.time}</td><td>${esc(o.driver)}${r.status === "confirmed" ? `<div class="private">Encontro: ${esc(o.ref)}</div>` : ""}</td><td>${o.seats}</td><td>${badge(r.status)}</td>
    <td>${["pending", "confirmed"].includes(r.status) ? `<button class="btn btn-danger" data-act="cancelar" data-id="${r.id}">Cancelar</button>` : ""}</td></tr>`;
      });
  $("#ridesTable").innerHTML = rows.length ? `<table><thead><tr><th>Origem</th><th>Destino</th><th>Data</th><th>Horário</th><th>Participantes</th><th>Vagas</th><th>Estado</th><th><span class="sr">Ação</span></th></tr></thead><tbody>${rows.join("")}</tbody></table>` : UI.empty("Nada por aqui esta semana", "Ofereça ou busque uma carona para começar.");
}

function renderAll() {
  renderDashboard();
  renderRequests();
  renderRides();
}

async function load() {
  $("#view-dashboard").innerHTML = UI.skeleton(3);
  try {
    State.data = await Api.snapshot();
    renderAll();
  } catch {
    $("#view-dashboard").innerHTML = UI.error("load");
  }
}
async function search(e) {
  e && e.preventDefault();
  const box = $("#searchResults");
  box.innerHTML = UI.skeleton(4);
  try {
    State.results = await Api.listOffers({ data: $("#fDate").value, regiao: $("#fRegion").value, horario: $("#fTime").value, vagas: $("#fSeats").value });
    box.innerHTML = State.results.length ? State.results.map(offerCard).join("") : `<div style="grid-column:1/-1">${UI.empty("Nenhuma carona encontrada", "Tente outro dia ou deixe a região em “Todas”.")}</div>`;
  } catch {
    box.innerHTML = `<div style="grid-column:1/-1">${UI.error("search")}</div>`;
  }
}
function go(v) {
  State.view = v;
  $$(".view").forEach((s) => (s.hidden = s.id !== "view-" + v));
  $$(".nav-item").forEach((n) => n.toggleAttribute("aria-current", n.dataset.view === v) || n.removeAttribute("aria-current"));
  $$(".nav-item").forEach((n) => (n.dataset.view === v ? n.setAttribute("aria-current", "page") : n.removeAttribute("aria-current")));
  $("#pageTitle").textContent = { dashboard: "Início", search: "Buscar caronas", requests: "Solicitações recebidas", rides: "Minhas caronas" }[v];
  if (v === "search" && State.results === null) search();
}
async function runAction(btn, fn, ok) {
  try {
    await UI.busy(btn, fn);
    State.data = await Api.snapshot();
    renderAll();
    UI.toast(ok);
  } catch {
    UI.toast("Não foi possível concluir. Tente novamente.", true);
  }
}

const req = (el, m) => UI.field(el, el.value.trim() ? "" : m);
function publishSummary() {
  const f = $("#publishForm"),
    v = (k) => f.elements[k].value;
  $("#pubSummary").textContent = v("origin") && v("date") && v("time") ? `Resumo: ${v("origin")} → Senac Canoas, ${fmtDate(v("date"))} às ${v("time")}, ${v("seats")} vaga(s).` : "";
  const [h] = (v("time") || "0:0").split(":");
  if (v("time") && +h >= 22) $("#pubSummary").textContent += " Este horário parece apertado. Confira antes de publicar.";
}

function bind() {
  document.addEventListener("click", async (e) => {
    const t = e.target.closest("button");
    if (!t) return;
    if (t.dataset.view) go(t.dataset.view);
    if (t.dataset.go) go(t.dataset.go);
    if (t.dataset.open) {
      $("#dlg-" + t.dataset.open).showModal();
    }
    if (t.hasAttribute("data-close")) t.closest("dialog").close();
    if (t.dataset.retry) t.dataset.retry === "search" ? search() : load();
    if (t.dataset.tab) {
      State.tab = t.dataset.tab;
      renderRides();
    }
    if (t.dataset.request) {
      const o = State.results.find((x) => x.id == t.dataset.request);
      State.pending = o;
      $("#reqInfo").textContent = `${o.origin} → Senac Canoas · ${fmtDate(o.date)}, ${o.time} · ${o.driver}`;
      $("#dlg-request").showModal();
    }
    if (t.dataset.act) {
      const a = t.dataset.act,
        id = +t.dataset.id;
      const txt = { aceitar: ["Aceitar esta solicitação?", "A pessoa verá o ponto de encontro e poderá combinar os detalhes.", "Aceitar"], recusar: ["Recusar esta solicitação?", "A pessoa será avisada de que a vaga não está disponível.", "Recusar"], cancelar: ["Cancelar esta carona?", "Quem participa será avisado. Essa ação não pode ser desfeita.", "Cancelar carona"] }[a];
      if (await UI.confirm(...txt)) runAction(t, () => Api.respond(id, a), { aceitar: "Solicitação aceita.", recusar: "Solicitação recusada.", cancelar: "Carona cancelada." }[a]);
    }
  });
  $("#searchForm").addEventListener("submit", search);
  $("#requestForm").addEventListener("submit", async (e) => {
    e.preventDefault();
    const b = e.submitter;
    try {
      await UI.busy(b, () => Api.requestSeat(State.pending.id, e.target.msg.value));
      $("#dlg-request").close();
      e.target.reset();
      State.data = await Api.snapshot();
      renderAll();
      UI.toast("Solicitação enviada. Status: pendente.");
      go("rides");
      State.tab = "requested";
      renderRides();
    } catch {
      UI.toast("Não foi possível enviar a solicitação.", true);
    }
  });
  $("#publishForm").addEventListener("input", publishSummary);
  $("#publishForm").addEventListener("submit", async (e) => {
    e.preventDefault();
    const f = e.target,
      el = f.elements;
    const ok = [req(el.origin, "Escolha a região de origem."), req(el.date, "Informe a data."), req(el.time, "Verifique o horário informado."), req(el.ref, "Informe um ponto de referência aproximado."), UI.field(el.seats, +el.seats.value >= 1 && +el.seats.value <= 4 ? "" : "Informe de 1 a 4 vagas.")].every(Boolean);
    if (!ok) return;
    try {
      await UI.busy(e.submitter, () => Api.createOffer({ origin: el.origin.value, date: el.date.value, time: el.time.value, seats: +el.seats.value, ref: el.ref.value, notes: el.notes.value }));
      $("#dlg-publish").close();
      f.reset();
      $$(".valid", f).forEach((x) => x.classList.remove("valid"));
      $("#pubSummary").textContent = "";
      State.data = await Api.snapshot();
      renderAll();
      UI.toast("Oferta publicada.");
      go("rides");
      State.tab = "offered";
      renderRides();
    } catch {
      UI.toast("Não foi possível publicar a oferta.", true);
    }
  });
  $("#onboardForm").addEventListener("submit", async (e) => {
    e.preventDefault();
    const el = e.target.elements;
    const ok = [req(el.name, "Informe seu nome."), UI.field(el.email, /^\S+@\S+\.\S+$/.test(el.email.value) ? "" : "Use seu e-mail institucional."), req(el.region, "Escolha seu bairro.")].every(Boolean);
    if (!ok) return;
    try {
      const u = await UI.busy(e.submitter, () => Api.register({ name: el.name.value, email: el.email.value, role: el.role.value, region: el.region.value }));
      $("#dlg-onboard").close();
      setUser(u);
      UI.toast("Cadastro concluído. Bem-vindo(a)!");
      load();
    } catch {
      UI.toast("Não foi possível concluir o cadastro.", true);
    }
  });
  $("#dlg-onboard").addEventListener("cancel", (e) => e.preventDefault());
  const pb = $("#profileBtn");
  pb.addEventListener("click", () => {
    const m = $("#profileMenu");
    m.hidden = !m.hidden;
    pb.setAttribute("aria-expanded", !m.hidden);
  });
  $("#simError").addEventListener("click", (e) => {
    Api.config.fail = !Api.config.fail;
    e.target.textContent = Api.config.fail ? "Restaurar conexão" : "Simular falha de conexão";
    UI.toast(Api.config.fail ? "Falha simulada ativada." : "Conexão restaurada.");
  });
  $("#resetDemo").addEventListener("click", () => {
    Api.reset();
    location.reload();
  });
  const off = () => ($("#offline").hidden = navigator.onLine);
  addEventListener("online", off);
  addEventListener("offline", off);
  off();
  document.addEventListener("keydown", (e) => {
    if (e.key === "Escape") {
      $("#profileMenu").hidden = true;
      pb.setAttribute("aria-expanded", "false");
    }
  });
}
function setUser(u) {
  $("#avatar").textContent = u.name
    .split(" ")
    .map((p) => p[0])
    .slice(0, 2)
    .join("")
    .toUpperCase();
  $("#profileName").textContent = u.name;
}

bind();
go("dashboard");
const u = Api.session();
if (u) {
  setUser(u);
  load();
} else $("#dlg-onboard").showModal();
