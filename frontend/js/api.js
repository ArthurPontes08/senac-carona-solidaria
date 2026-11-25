const Api = (() => {
  const CONFIG = { BASE: 'http://localhost:8080/api', MOCK: true, DELAY: 700, fail: false };
  const iso = n => { const d = new Date(); d.setDate(d.getDate() + n); return d.toISOString().slice(0, 10); };
  const seed = () => ({
    me: null,

    offers: [
      { id: 1, driver: 'Rafael M.', origin: 'Centro', date: iso(1), time: '18:30', seats: 2, ref: 'Próximo à praça central', summary: 'Centro → Av. Guilherme Schell → Campus', match: 92, mine: false },
      { id: 2, driver: 'Prof. Helena T.', origin: 'Marechal Rondon', date: iso(1), time: '18:10', seats: 1, ref: 'Perto do mercado', summary: 'Rondon → Av. Boqueirão → Campus', match: 85, mine: false },
      { id: 3, driver: 'Bianca L.', origin: 'Niterói', date: iso(2), time: '07:20', seats: 3, ref: 'Próximo à estação', summary: 'Niterói → BR-116 → Campus', match: 74, mine: false },
      { id: 4, driver: 'Diego P.', origin: 'Mathias Velho', date: iso(2), time: '18:00', seats: 0, ref: 'Perto da escola estadual', summary: 'Mathias Velho → Campus', match: 60, mine: false },
      { id: 5, driver: 'Camila R.', origin: 'Igara', date: iso(3), time: '12:40', seats: 2, ref: 'Próximo ao parque', summary: 'Igara → Av. Santos Ferreira → Campus', match: 68, mine: false },
      { id: 6, driver: 'Você', origin: 'Harmonia', date: iso(1), time: '18:20', seats: 1, ref: 'Perto da padaria', summary: 'Harmonia → Campus', match: 100, mine: true }
    ],
    requests: [
      { id: 101, offerId: 6, who: 'Lucas A.', region: 'Harmonia', time: '18:20', date: iso(1), msg: 'Posso esperar na esquina.', status: 'pending', mine: false },
      { id: 102, offerId: 6, who: 'Aline S.', region: 'Igara', time: '18:20', date: iso(1), msg: '', status: 'confirmed', mine: false },
      { id: 103, offerId: 1, who: 'Você', region: 'Centro', time: '18:30', date: iso(1), msg: '', status: 'confirmed', mine: true },
      { id: 104, offerId: 5, who: 'Você', region: 'Igara', time: '12:40', date: iso(3), msg: '', status: 'refused', mine: true },
      { id: 105, offerId: 3, who: 'Você', region: 'Niterói', time: '07:20', date: iso(-1), msg: '', status: 'done', mine: true },
      { id: 106, offerId: 2, who: 'Você', region: 'Marechal Rondon', time: '18:10', date: iso(1), msg: '', status: 'cancelled', mine: true }
    ]
  });
  let db = JSON.parse(localStorage.getItem('cs-db') || 'null') || seed();
  const save = () => localStorage.setItem('cs-db', JSON.stringify(db));
  const wait = fn => new Promise((ok, no) => setTimeout(() => {
    if (CONFIG.fail || !navigator.onLine) return no(new Error('network'));
    ok(fn());
  }, CONFIG.DELAY));
  async function call(path, opts, mock) {
    if (CONFIG.MOCK) return wait(() => { const r = mock(); save(); return structuredClone(r); });
    const res = await fetch(CONFIG.BASE + path, { headers: { 'Content-Type': 'application/json' }, ...opts });
    if (!res.ok) throw new Error('http');
    return res.json();
  }
  const find = (list, id) => list.find(x => x.id === id);
  return {
    config: CONFIG,
    reset() { localStorage.removeItem('cs-db'); db = seed(); },
    session: () => db.me,
    register: u => call('/usuarios', { method: 'POST', body: JSON.stringify(u) }, () => (db.me = u)),
    listOffers: f => call('/ofertas?' + new URLSearchParams(f), {}, () => db.offers.filter(o => !o.mine &&
      (!f.data || o.date === f.data) && (!f.regiao || o.origin === f.regiao) && (!f.horario || o.time >= f.horario) && o.seats >= (+f.vagas || 0))),
    createOffer: o => call('/ofertas', { method: 'POST', body: JSON.stringify(o) }, () => { const n = { ...o, id: Date.now(), driver: 'Você', mine: true, match: 100, summary: o.origin + ' → Campus' }; db.offers.push(n); return n; }),
    requestSeat: (offerId, msg) => call(`/ofertas/${offerId}/solicitacoes`, { method: 'POST', body: JSON.stringify({ msg }) }, () => {
      const o = find(db.offers, offerId);
      const r = { id: Date.now(), offerId, who: 'Você', region: o.origin, time: o.time, date: o.date, msg, status: 'pending', mine: true }; db.requests.push(r); return r; }),
    respond: (id, acao) => call(`/solicitacoes/${id}`, { method: 'PATCH', body: JSON.stringify({ acao }) }, () => {
      const r = find(db.requests, id), o = find(db.offers, r.offerId);
      r.status = { aceitar: 'confirmed', recusar: 'refused', cancelar: 'cancelled' }[acao];
      if (acao === 'aceitar' && o.mine) o.seats = Math.max(0, o.seats - 1);
      return r; }),
    snapshot: () => call('/minhas-caronas', {}, () => db)
  };
})();
