const State = { view: "dashboard", tab: "offered", data: null, results: null, pending: null, chat: null, profilePhoto: "", searchRequest: 0, photoRequest: 0 };
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
const chatButton = (requestId) => `<button class="btn btn-secondary" data-chat="${requestId}">Iniciar conversa</button>`;

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
    ${r.status === "pending" ? `<div class="actions"><button class="btn btn-danger" data-act="recusar" data-id="${r.id}">Recusar</button><button class="btn btn-primary" data-act="aceitar" data-id="${r.id}">Aceitar</button></div>` : r.status === "confirmed" ? `<p class="private">Confirmado. Agora vocês podem combinar os detalhes da carona.</p><div class="actions"><button class="btn btn-danger" data-act="cancelar" data-id="${r.id}">Cancelar carona</button>${chatButton(r.id)}</div>` : ""}</article>`,
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
        const chats = requests.filter((r) => r.offerId === o.id && r.status === "confirmed").map((r) => chatButton(r.id)).join("");
        const status = o.status || (o.seats ? "open" : "full");
        const cancel = status === "cancelled" ? "" : `<button class="btn btn-danger" data-cancel-offer="${o.id}">Cancelar oferta</button>`;
        return `<tr><td>${esc(o.origin)}</td><td>Senac Canoas</td><td>${fmtDate(o.date)}</td><td>${o.time}</td><td>${esc(p)}</td><td>${o.seats}</td><td>${badge(status)}</td><td><div class="table-actions">${chats}${cancel}</div></td></tr>`;
      });
  else
    rows = requests
      .filter((r) => r.mine)
      .map((r) => {
        const o = offers.find((x) => x.id === r.offerId);
        return `<tr><td>${esc(r.region)}</td><td>Senac Canoas</td><td>${fmtDate(r.date)}</td><td>${r.time}</td><td>${esc(o.driver)}${r.status === "confirmed" ? `<div class="private">Encontro: ${esc(o.ref)}</div>` : ""}</td><td>${o.seats}</td><td>${badge(r.status)}</td>
    <td><div class="table-actions">${r.status === "confirmed" ? chatButton(r.id) : ""}${["pending", "confirmed"].includes(r.status) ? `<button class="btn btn-danger" data-act="cancelar" data-id="${r.id}">Cancelar</button>` : ""}</div></td></tr>`;
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
  } catch (error) {
    $("#view-dashboard").innerHTML = UI.error("load");
    UI.toast(failureMessage(error, "Não foi possível carregar os dados."), true);
  }
}
async function search(e) {
  e && e.preventDefault();
  const request = ++State.searchRequest;
  const date = $("#fDate");
  if (!date.validity.valid) {
    UI.field(date, "Informe uma data válida com ano de até 4 dígitos.");
    return;
  }
  if (date.value) UI.field(date, "");
  const box = $("#searchResults");
  box.innerHTML = UI.skeleton(4);
  try {
    const results = await Api.listOffers({ q: $("#fQuery").value, data: $("#fDate").value, regiao: $("#fRegion").value, horario: $("#fTime").value, vagas: $("#fSeats").value });
    if (request !== State.searchRequest) return;
    State.results = results;
    box.innerHTML = State.results.length ? State.results.map(offerCard).join("") : `<div style="grid-column:1/-1">${UI.empty("Nenhuma carona encontrada", "Tente outro dia ou deixe a região em “Todas”.")}</div>`;
  } catch (error) {
    if (request === State.searchRequest) {
      box.innerHTML = `<div style="grid-column:1/-1">${UI.error("search")}</div>`;
      UI.toast(failureMessage(error, "Não foi possível buscar caronas."), true);
    }
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
  } catch (error) {
    UI.toast(failureMessage(error, "Não foi possível concluir. Tente novamente."), true);
  }
}

function renderChat() {
  const { partner, ride, messages } = State.chat;
  $("#chatPartner").textContent = partner;
  $("#chatRide").textContent = ride;
  $("#chatMessages").innerHTML = messages.length
    ? messages
        .map(
          (m) => `<article class="chat-message${m.mine ? " mine" : ""}"><p>${esc(m.text)}</p><span class="chat-message-meta">${esc(m.sender)} · ${new Date(m.createdAt).toLocaleTimeString("pt-BR", { hour: "2-digit", minute: "2-digit" })}</span></article>`,
        )
        .join("")
    : '<p class="muted">Conversa iniciada. Envie uma mensagem para combinar os detalhes da carona.</p>';
  $("#chatMessages").scrollTop = $("#chatMessages").scrollHeight;
}

const req = (el, m) => UI.field(el, el.value.trim() ? "" : m);
const failureMessage = (error, fallback) => {
  const message = error instanceof Error ? error.message : "";
  return message && !["Failed to fetch", "Load failed", "network"].includes(message) ? message : fallback;
};
function publishSummary() {
  const f = $("#publishForm"),
    v = (k) => f.elements[k].value;
  $("#pubSummary").textContent = v("origin") && v("date") && v("time") ? `Resumo: ${v("origin")} → Senac Canoas, ${fmtDate(v("date"))} às ${v("time")}, ${v("seats")} vaga(s).` : "";
  const [h] = (v("time") || "0:0").split(":");
  if (v("time") && +h >= 22) $("#pubSummary").textContent += " Este horário parece apertado. Confira antes de publicar.";
}

function resetSearchFilters() {
  const form = $("#searchForm");
  form.reset();
  $$(".invalid, .valid", form).forEach((input) => {
    input.classList.remove("invalid", "valid");
    input.removeAttribute("aria-invalid");
    input.removeAttribute("aria-describedby");
  });
  $$(".err", form).forEach((error) => (error.textContent = ""));
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
    if (t.dataset.chat) {
      try {
        State.chat = await Api.conversation(t.dataset.chat);
        renderChat();
        $("#dlg-chat").showModal();
        $("#chatForm").elements.message.focus();
      } catch (error) {
        UI.toast(failureMessage(error, "Não foi possível abrir esta conversa."), true);
      }
    }
    if (t.dataset.cancelOffer) {
      if (await UI.confirm("Cancelar esta oferta?", "As solicitações ativas desta carona também serão canceladas.", "Cancelar oferta")) {
        runAction(t, () => Api.cancelOffer(t.dataset.cancelOffer), "Oferta de carona cancelada.");
      }
    }
    if (t.dataset.act) {
      const a = t.dataset.act,
        id = +t.dataset.id;
      const txt = { aceitar: ["Aceitar esta solicitação?", "A pessoa verá o ponto de encontro e poderá combinar os detalhes.", "Aceitar"], recusar: ["Recusar esta solicitação?", "A pessoa será avisada de que a vaga não está disponível.", "Recusar"], cancelar: ["Cancelar esta carona?", "Quem participa será avisado. Essa ação não pode ser desfeita.", "Cancelar carona"] }[a];
      if (await UI.confirm(...txt)) runAction(t, () => Api.respond(id, a), { aceitar: "Solicitação aceita.", recusar: "Solicitação recusada.", cancelar: "Carona cancelada." }[a]);
    }
  });
  $("#resetSearch").addEventListener("click", () => {
    resetSearchFilters();
    search();
  });
  $("#resetBenchmark").addEventListener("click", async (e) => {
    const button = e.currentTarget;
    if (!Api.config.MOCK) {
      UI.toast("O reset do benchmark está disponível apenas no modo local.", true);
      return;
    }
    if (!(await UI.confirm("Zerar dados do benchmark?", "Todas as caronas, solicitações, mensagens e conversas serão apagadas. Seu perfil será mantido.", "Apagar tudo"))) return;
    State.searchRequest += 1;
    try {
      State.data = await UI.busy(button, () => Api.clearBenchmark());
      State.results = null;
      State.chat = null;
      State.pending = null;
      resetSearchFilters();
      renderAll();
      $("#searchResults").innerHTML = `<div style="grid-column:1/-1">${UI.empty("Nenhuma carona encontrada", "Os dados do benchmark foram apagados.")}</div>`;
      UI.toast("Benchmark zerado. Seu perfil foi mantido.");
    } catch (error) {
      console.error("Failed to clear benchmark data", error);
      UI.toast("Não foi possível zerar os dados do benchmark.", true);
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
    } catch (error) {
      UI.toast(failureMessage(error, "Não foi possível enviar a solicitação."), true);
    }
  });
  $("#chatForm").addEventListener("submit", async (e) => {
    e.preventDefault();
    const form = e.currentTarget;
    const text = form.elements.message.value.trim();
    if (!text) {
      form.elements.message.focus();
      return;
    }
    try {
      await UI.busy(e.submitter, () => Api.sendMessage(State.chat.requestId, text));
      State.chat = await Api.conversation(State.chat.requestId);
      renderChat();
      form.reset();
      form.elements.message.focus();
    } catch (error) {
      UI.toast(failureMessage(error, "Não foi possível enviar a mensagem."), true);
    }
  });
  $("#deleteChat").addEventListener("click", async (e) => {
    const button = e.currentTarget;
    const requestId = State.chat.requestId;
    if (!(await UI.confirm("Apagar esta conversa?", "Todas as mensagens serão excluídas para os participantes. Esta ação não pode ser desfeita.", "Apagar conversa"))) return;
    try {
      await UI.busy(button, () => Api.deleteConversation(requestId));
      State.chat = null;
      $("#chatMessages").innerHTML = "";
      $("#chatForm").reset();
      $("#dlg-chat").close();
      UI.toast("Conversa apagada.");
    } catch (error) {
      console.error("Failed to delete conversation", error);
      UI.toast(failureMessage(error, "Não foi possível apagar a conversa."), true);
    }
  });
  $$('input[type="date"]').forEach((input) => {
    input.addEventListener("input", () => {
      const year = input.value.split("-")[0];
      if (year.length > 4) {
        input.value = "";
        UI.field(input, "O ano deve ter no máximo 4 dígitos.");
      } else if (input.value && input.validity.valid) {
        UI.field(input, "");
      }
    });
  });
  $("#publishForm").addEventListener("input", publishSummary);
  $("#publishForm").addEventListener("submit", async (e) => {
    e.preventDefault();
    const f = e.target,
      el = f.elements;
    const dateError = !el.date.value ? "Informe a data." : !el.date.validity.valid ? "Use uma data válida com ano de até 4 dígitos." : "";
    const ok = [req(el.origin, "Informe a região de origem."), UI.field(el.date, dateError), req(el.time, "Verifique o horário informado."), req(el.ref, "Informe um ponto de referência aproximado."), UI.field(el.seats, +el.seats.value >= 1 && +el.seats.value <= 4 ? "" : "Informe de 1 a 4 vagas.")].every(Boolean);
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
    } catch (error) {
      UI.toast(failureMessage(error, "Não foi possível publicar a oferta."), true);
    }
  });
  $("#onboardForm").addEventListener("submit", async (e) => {
    e.preventDefault();
    const el = e.target.elements;
    const ok = [req(el.name, "Informe seu nome."), UI.field(el.email, /^\S+@\S+\.\S+$/.test(el.email.value) ? "" : "Use seu e-mail institucional."), req(el.region, "Informe seu bairro de origem.")].every(Boolean);
    if (!ok) return;
    try {
      const u = await UI.busy(e.submitter, () => Api.register({ name: el.name.value, email: el.email.value, role: el.role.value, region: el.region.value }));
      $("#dlg-onboard").close();
      setUser(u);
      UI.toast("Cadastro concluído. Bem-vindo(a)!");
      load();
    } catch (error) {
      UI.toast(failureMessage(error, "Não foi possível concluir o cadastro."), true);
    }
  });
  $("#dlg-onboard").addEventListener("cancel", (e) => e.preventDefault());
  const pb = $("#profileBtn");
  pb.addEventListener("click", () => {
    const m = $("#profileMenu");
    m.hidden = !m.hidden;
    pb.setAttribute("aria-expanded", !m.hidden);
  });
  $("#editProfile").addEventListener("click", () => {
    const user = Api.session();
    if (!user) return;
    State.profilePhoto = user.photo || "";
    $("#profileNameInput").value = user.name || "";
    $("#profilePreviewName").textContent = user.name || "Seu nome";
    $("#profilePhoto").value = "";
    renderAvatar($("#profilePreview"), user);
    $("#profileMenu").hidden = true;
    pb.setAttribute("aria-expanded", "false");
    $("#dlg-profile").showModal();
    $("#profileNameInput").focus();
  });
  $("#profileNameInput").addEventListener("input", (e) => {
    const name = e.currentTarget.value.trim() || "Seu nome";
    $("#profilePreviewName").textContent = name;
    if (!State.profilePhoto) renderAvatar($("#profilePreview"), { name });
  });
  $("#profilePhoto").addEventListener("change", async (e) => {
    const input = e.currentTarget;
    const file = input.files[0];
    if (!file) return;
    const request = ++State.photoRequest;
    if (!file.type.startsWith("image/")) {
      input.value = "";
      UI.toast("Selecione um arquivo de imagem.", true);
      return;
    }
    if (file.size > 8 * 1024 * 1024) {
      input.value = "";
      UI.toast("A imagem deve ter no máximo 8 MB.", true);
      return;
    }
    try {
      const photo = await imageToDataUrl(file);
      if (request !== State.photoRequest) return;
      State.profilePhoto = photo;
      renderAvatar($("#profilePreview"), { name: $("#profileNameInput").value, photo });
    } catch (error) {
      console.error("Failed to load profile photo", error);
      UI.toast("Não foi possível carregar essa imagem.", true);
    }
  });
  $("#profileForm").addEventListener("submit", async (e) => {
    e.preventDefault();
    const nameInput = e.currentTarget.elements.name;
    if (!req(nameInput, "Informe seu nome.")) return;
    try {
      const user = await UI.busy(e.submitter, () => Api.updateProfile({ name: nameInput.value.trim(), photo: State.profilePhoto }));
      setUser(user);
      $("#dlg-profile").close();
      UI.toast("Perfil atualizado.");
    } catch (error) {
      console.error("Failed to update profile", error);
      UI.toast(failureMessage(error, "Não foi possível atualizar o perfil."), true);
    }
  });
  $("#simError").addEventListener("click", (e) => {
    Api.config.fail = !Api.config.fail;
    e.target.textContent = Api.config.fail ? "Restaurar conexão" : "Simular falha de conexão";
    syncConnectionStatus();
    $("#profileMenu").hidden = true;
    pb.setAttribute("aria-expanded", "false");
    UI.toast(Api.config.fail ? "Falha simulada ativada." : "Conexão restaurada.");
    if (State.view === "search") search();
    else load();
  });
  $("#resetDemo").addEventListener("click", () => {
    if (!Api.config.MOCK) {
      UI.toast("A sessão integrada é mantida para preservar seu acesso.", true);
      return;
    }
    Api.reset();
    location.reload();
  });
  $("#resetDemo").hidden = !Api.config.MOCK;
  const syncConnectionStatus = () => {
    const offline = Api.config.fail || !navigator.onLine;
    $("#offline").hidden = !offline;
  };
  addEventListener("online", syncConnectionStatus);
  addEventListener("offline", syncConnectionStatus);
  syncConnectionStatus();
  document.addEventListener("keydown", (e) => {
    if (e.key === "Escape") {
      $("#profileMenu").hidden = true;
      pb.setAttribute("aria-expanded", "false");
    }
  });
}
function renderAvatar(element, user) {
  if (user.photo) {
    const image = document.createElement("img");
    image.src = user.photo;
    image.alt = "";
    element.replaceChildren(image);
    return;
  }
  const initials = String(user.name || "?").trim().split(/\s+/).filter(Boolean).slice(0, 2).map((part) => part[0]).join("").toUpperCase() || "?";
  element.textContent = initials;
}
function imageToDataUrl(file) {
  return new Promise((resolve, reject) => {
    const reader = new FileReader();
    reader.onerror = () => reject(reader.error || new Error("image read failed"));
    reader.onload = () => {
      const image = new Image();
      image.onerror = () => reject(new Error("invalid image"));
      image.onload = () => {
        const largestSide = Math.max(image.naturalWidth, image.naturalHeight);
        if (!largestSide) {
          reject(new Error("empty image"));
          return;
        }
        const scale = Math.min(1, 256 / largestSide);
        const canvas = document.createElement("canvas");
        canvas.width = Math.max(1, Math.round(image.naturalWidth * scale));
        canvas.height = Math.max(1, Math.round(image.naturalHeight * scale));
        const context = canvas.getContext("2d");
        if (!context) {
          reject(new Error("canvas unavailable"));
          return;
        }
        try {
          context.fillStyle = "#fff";
          context.fillRect(0, 0, canvas.width, canvas.height);
          context.drawImage(image, 0, 0, canvas.width, canvas.height);
          resolve(canvas.toDataURL("image/jpeg", 0.85));
        } catch (error) {
          reject(error);
        }
      };
      image.src = String(reader.result);
    };
    reader.readAsDataURL(file);
  });
}
function setUser(u) {
  State.profilePhoto = u.photo || "";
  renderAvatar($("#avatar"), u);
  $("#profileName").textContent = u.name;
  $("#profilePreviewName").textContent = u.name;
  renderAvatar($("#profilePreview"), u);
}

bind();
go("dashboard");
const u = Api.session();
if (u) {
  setUser(u);
  load();
} else $("#dlg-onboard").showModal();
