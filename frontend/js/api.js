const Api = (() => {
  const CONFIG = {
    BASE: (window.VAIJUNTO_CONFIG && window.VAIJUNTO_CONFIG.apiBase) || "http://localhost:8080/api",
    MOCK: false,
    DELAY: 700,
    fail: false,
  };
  const iso = (n) => {
    const d = new Date();
    d.setDate(d.getDate() + n);
    return d.toISOString().slice(0, 10);
  };
  const seed = () => ({
    me: null,

    offers: [
      { id: 1, driver: "Rafael M.", origin: "Centro", date: iso(1), time: "18:30", seats: 2, ref: "Próximo à praça central", summary: "Centro → Av. Guilherme Schell → Campus", match: 92, mine: false },
      { id: 2, driver: "Prof. Helena T.", origin: "Marechal Rondon", date: iso(1), time: "18:10", seats: 1, ref: "Perto do mercado", summary: "Rondon → Av. Boqueirão → Campus", match: 85, mine: false },
      { id: 3, driver: "Bianca L.", origin: "Niterói", date: iso(2), time: "07:20", seats: 3, ref: "Próximo à estação", summary: "Niterói → BR-116 → Campus", match: 74, mine: false },
      { id: 4, driver: "Diego P.", origin: "Mathias Velho", date: iso(2), time: "18:00", seats: 0, ref: "Perto da escola estadual", summary: "Mathias Velho → Campus", match: 60, mine: false },
      { id: 5, driver: "Camila R.", origin: "Igara", date: iso(3), time: "12:40", seats: 2, ref: "Próximo ao parque", summary: "Igara → Av. Santos Ferreira → Campus", match: 68, mine: false },
      { id: 6, driver: "Você", origin: "Harmonia", date: iso(1), time: "18:20", seats: 1, ref: "Perto da padaria", summary: "Harmonia → Campus", match: 100, mine: true },
    ],
    requests: [
      { id: 101, offerId: 6, who: "Lucas A.", region: "Harmonia", time: "18:20", date: iso(1), msg: "Posso esperar na esquina.", status: "pending", mine: false },
      { id: 102, offerId: 6, who: "Aline S.", region: "Igara", time: "18:20", date: iso(1), msg: "", status: "confirmed", mine: false },
      { id: 103, offerId: 1, who: "Você", region: "Centro", time: "18:30", date: iso(1), msg: "", status: "confirmed", mine: true },
      { id: 104, offerId: 5, who: "Você", region: "Igara", time: "12:40", date: iso(3), msg: "", status: "refused", mine: true },
      { id: 105, offerId: 3, who: "Você", region: "Niterói", time: "07:20", date: iso(-1), msg: "", status: "done", mine: true },
      { id: 106, offerId: 2, who: "Você", region: "Marechal Rondon", time: "18:10", date: iso(1), msg: "", status: "cancelled", mine: true },
    ],
    chats: [],
  });
  let db = JSON.parse(localStorage.getItem("cs-db") || "null") || seed();
  if (!Array.isArray(db.chats)) db.chats = [];
  const save = () => localStorage.setItem("cs-db", JSON.stringify(db));
  const saveSession = (user) => {
    localStorage.setItem("cs-session", JSON.stringify(user));
    if (CONFIG.MOCK) {
      db.me = user;
      save();
    }
    return user;
  };
  const currentUser = () => CONFIG.MOCK ? db.me : JSON.parse(localStorage.getItem("cs-session") || "null");
  const normalizeSearch = (value) => String(value || "").toLocaleLowerCase("pt-BR").normalize("NFD").replace(/[\u0300-\u036f]/g, "");
  const wait = (fn) =>
    new Promise((ok, no) =>
      setTimeout(() => {
        if (CONFIG.fail || !navigator.onLine) return no(new Error("network"));
        ok(fn());
      }, CONFIG.DELAY),
    );
  async function call(path, opts, mock) {
    if (CONFIG.MOCK)
      return wait(() => {
        const r = mock();
        save();
        return structuredClone(r);
      });
    if (CONFIG.fail || !navigator.onLine) throw new Error("Conexão indisponível. Verifique a rede e tente novamente.");
    const res = await fetch(CONFIG.BASE + path, { headers: { "Content-Type": "application/json" }, ...opts });
    if (!res.ok) {
      let message = `Erro HTTP ${res.status}`;
      try {
        const body = await res.json();
        if (body.erro) message = body.erro;
      } catch {}
      throw new Error(message);
    }
    if (res.status === 204) return null;
    return res.json();
  }
  const find = (list, id) => list.find((x) => x.id === id);
  return {
    config: CONFIG,
    reset() {
      localStorage.removeItem("cs-db");
      localStorage.removeItem("cs-session");
      db = seed();
    },
    logout() {
      localStorage.removeItem("cs-session");
      if (CONFIG.MOCK) {
        db.me = null;
        save();
      }
    },
    clearBenchmark() {
      if (!CONFIG.MOCK) throw new Error("O reset do benchmark só está disponível no modo de demonstração local.");
      db = { me: db.me, offers: [], requests: [], chats: [] };
      save();
      return structuredClone(db);
    },
    session: currentUser,
    register: async (u) => {
      const user = await call("/usuarios", { method: "POST", body: JSON.stringify(u) }, () => {
        const profile = { ...u };
        delete profile.password;
        db.me = { ...profile, id: Date.now(), photo: "" };
        return db.me;
      });
      return saveSession(user);
    },
    login: async (credentials) => {
      const user = await call("/usuarios/login", { method: "POST", body: JSON.stringify(credentials) }, () => {
        throw new Error("Login não disponível no modo de demonstração.");
      });
      return saveSession(user);
    },
    updateProfile: (profile) =>
      call(`/usuarios/${currentUser().id}`, { method: "PATCH", body: JSON.stringify(profile) }, () => {
        if (!db.me) throw new Error("profile unavailable");
        const name = String(profile.name || "").trim();
        if (!name || name.length > 80) throw new Error("invalid profile name");
        const updated = { ...db.me, name };
        if (profile.photo) {
          if (typeof profile.photo !== "string" || !profile.photo.startsWith("data:image/")) throw new Error("invalid profile photo");
          updated.photo = profile.photo;
        }
        return updated;
      }).then(saveSession),
    listOffers: (f) =>
      call("/ofertas?" + new URLSearchParams({ ...f, userId: currentUser().id }), {}, () => {
        const terms = normalizeSearch(f.q).trim().split(/\s+/).filter(Boolean);
        const region = normalizeSearch(f.regiao).trim();
        return db.offers.filter((o) => {
          const text = normalizeSearch([o.driver, o.origin, o.summary, o.ref, o.date, o.time, o.seats, "Senac Canoas"].join(" "));
          return (
            !o.mine &&
            terms.every((term) => text.includes(term)) &&
            (!f.data || o.date === f.data) &&
            (!region || normalizeSearch(o.origin).includes(region)) &&
            (!f.horario || o.time >= f.horario) &&
            o.seats >= (+f.vagas || 0)
          );
        });
      }),
    createOffer: (o) =>
      call("/ofertas", { method: "POST", body: JSON.stringify({ ...o, driverId: currentUser().id, destination: "Campus Senac Canoas" }) }, () => {
        const n = { ...o, id: Date.now(), driver: "Você", mine: true, match: 100, summary: o.origin + " → Campus" };
        db.offers.push(n);
        return n;
      }),
    cancelOffer: (offerId) =>
      call(`/caronas/${offerId}/cancelar?motoristaId=${currentUser().id}`, { method: "PATCH" }, () => {
        const offer = find(db.offers, Number(offerId));
        if (!offer || !offer.mine) throw new Error("offer unavailable");
        offer.status = "cancelled";
        return offer;
      }),
    requestSeat: (offerId, msg) =>
      call(`/ofertas/${offerId}/solicitacoes`, { method: "POST", body: JSON.stringify({ passengerId: currentUser().id, msg }) }, () => {
        const o = find(db.offers, offerId);
        const r = { id: Date.now(), offerId, who: "Você", region: o.origin, time: o.time, date: o.date, msg, status: "pending", mine: true };
        db.requests.push(r);
        return r;
      }),
    respond: (id, acao) =>
      call(`/solicitacoes/${id}`, { method: "PATCH", body: JSON.stringify({ userId: currentUser().id, acao }) }, () => {
        const r = find(db.requests, id),
          o = find(db.offers, r.offerId);
        r.status = { aceitar: "confirmed", recusar: "refused", cancelar: "cancelled" }[acao];
        if (acao === "aceitar" && o.mine) o.seats = Math.max(0, o.seats - 1);
        return r;
      }),
    conversation: (requestId) =>
      call(`/solicitacoes/${requestId}/conversa?userId=${currentUser().id}`, {}, () => {
        const request = find(db.requests, +requestId);
        if (!request || request.status !== "confirmed") throw new Error("conversation unavailable");
        const offer = find(db.offers, request.offerId);
        let chat = db.chats.find((item) => item.requestId === request.id);
        if (!chat) {
          chat = { requestId: request.id, messages: [] };
          db.chats.push(chat);
        }
        return {
          requestId: request.id,
          partner: request.mine ? offer.driver : request.who,
          ride: `${offer.origin} → Senac Canoas · ${offer.date} às ${offer.time}`,
          messages: chat.messages,
        };
      }),
    sendMessage: (requestId, text) =>
      call(`/solicitacoes/${requestId}/conversa`, { method: "POST", body: JSON.stringify({ userId: currentUser().id, text }) }, () => {
        const request = find(db.requests, +requestId);
        if (!request || request.status !== "confirmed") throw new Error("conversation unavailable");
        const message = String(text || "").trim();
        if (!message || message.length > 500) throw new Error("invalid message");
        let chat = db.chats.find((item) => item.requestId === request.id);
        if (!chat) {
          chat = { requestId: request.id, messages: [] };
          db.chats.push(chat);
        }
        const sent = { id: Date.now(), sender: db.me ? db.me.name : "Você", text: message, createdAt: new Date().toISOString(), mine: true };
        chat.messages.push(sent);
        return sent;
      }),
    deleteConversation: (requestId) =>
      call(`/solicitacoes/${requestId}/conversa?userId=${currentUser().id}`, { method: "DELETE" }, () => {
        const id = Number(requestId);
        if (!Number.isSafeInteger(id) || id <= 0) throw new Error("invalid conversation id");
        db.chats = db.chats.filter((chat) => Number(chat.requestId) !== id);
        return true;
      }),
    snapshot: () => {
      const user = currentUser();
      if (!user || !user.id) throw new Error("Sessão de usuário indisponível.");
      return call(`/minhas-caronas?userId=${user.id}`, {}, () => db);
    },
  };
})();
