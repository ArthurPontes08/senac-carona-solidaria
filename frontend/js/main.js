const State = { view: 'dashboard', tab: 'offered', data: null, results: null, pending: null };

const $ = (seletor, raiz = document) => raiz.querySelector(seletor);
const $$ = (seletor, raiz = document) => [...raiz.querySelectorAll(seletor)];
const esc = texto => String(texto ?? '').replace(/[&<>"']/g, c => ({ '&': '&amp;', '<': '&lt;', '>': '&gt;', '"': '&quot;', "'": '&#39;' }[c]));
const fmtDate = data => new Date(data + 'T12:00').toLocaleDateString('pt-BR', { weekday: 'short', day: '2-digit', month: '2-digit' });

const ESTADOS = {
  pending: { chave: 'pendente', icone: 'relogio', texto: 'Pendente' },
  confirmed: { chave: 'confirmada', icone: 'check', texto: 'Confirmada' },
  refused: { chave: 'recusada', icone: 'x', texto: 'Recusada' },
  cancelled: { chave: 'cancelada', icone: 'proibido', texto: 'Cancelada' },
  done: { chave: 'concluida', icone: 'estrela', texto: 'Concluída' },
  open: { chave: 'aberta', icone: 'alvo', texto: 'Oferta aberta' },
  full: { chave: 'cheia', icone: 'quadrado', texto: 'Sem vagas' }
};

const selo = (bloco, estado) => {
  const e = ESTADOS[estado];
  return `<span class="${bloco}__selo ${bloco}__selo--${e.chave}">${Icones.svg(bloco + '__selo-icone', e.icone)}${e.texto}</span>`;
};

const UI = {
  toast(mensagem, erro) {
    const aviso = document.createElement('div');
    aviso.className = 'aviso-toast' + (erro ? ' aviso-toast--erro' : '');
    aviso.textContent = mensagem;
    $('#toasts').append(aviso);
    setTimeout(() => aviso.remove(), 4000);
  },
  skeleton: (bloco, quantidade) => Array.from({ length: quantidade }, () => `<div class="${bloco}__esqueleto" aria-hidden="true"></div>`).join(''),
  empty: (bloco, titulo, texto, botao = '') => `<div class="${bloco}__vazio"><h3 class="${bloco}__vazio-titulo">${titulo}</h3><p class="${bloco}__vazio-texto">${texto}</p>${botao}</div>`,
  error: (bloco, acao) => `<div class="${bloco}__falha" role="alert"><h3 class="${bloco}__falha-titulo">Não conseguimos carregar as caronas agora.</h3><p class="${bloco}__falha-texto">Verifique sua conexão e tente de novo.</p><button type="button" class="${bloco}__falha-botao" data-retry="${acao}">Tentar novamente</button></div>`,
  async busy(botao, funcao) {
    botao.disabled = true;
    botao.classList.add('botao--carregando');
    try { return await funcao(); }
    finally { botao.disabled = false; botao.classList.remove('botao--carregando'); }
  },
  confirm(titulo, texto, rotuloSim) {
    return new Promise(resolver => {
      const dialogo = $('#dlg-confirm');
      $('#t-conf').textContent = titulo;
      $('#confText').textContent = texto;
      $('#confYes').textContent = rotuloSim;
      dialogo.onclose = () => resolver(dialogo.returnValue === 'yes');
      dialogo.returnValue = 'no';
      dialogo.showModal();
    });
  },
  field(campo, mensagem) {
    const erro = campo.parentElement.querySelector('[data-erro]');
    campo.classList.toggle('estado-invalido', !!mensagem);
    campo.classList.toggle('estado-valido', !mensagem && !!campo.value);
    campo.setAttribute('aria-invalid', !!mensagem);
    if (erro) erro.textContent = mensagem || '';
    return !mensagem;
  }
};

const cartaoOferta = o => `<article class="card-oferta">
  <h3 class="card-oferta__titulo">${esc(o.origin)} → Senac Canoas ${selo('card-oferta', o.seats ? 'open' : 'full')}</h3>
  <p class="card-oferta__trajeto">${esc(o.summary)}</p>
  <div class="card-oferta__metadados">
    <span class="card-oferta__motorista">${esc(o.driver)} · vínculo Senac</span>
    <span class="card-oferta__data">${fmtDate(o.date)}, ${o.time}</span>
    <span class="card-oferta__vagas">${o.seats} vaga(s)</span>
  </div>
  <p class="card-oferta__compatibilidade">Compatibilidade aproximada: ${o.match}%. Confira antes de solicitar.</p>
  <p class="card-oferta__privacidade">Ponto aproximado: ${esc(o.ref)}. Contato visível após a confirmação.</p>
  <div class="card-oferta__acoes"><button type="button" class="card-oferta__botao-solicitar" data-request="${o.id}" ${o.seats ? '' : 'disabled'}>Solicitar vaga</button></div>
</article>`;

const estatistica = (nome, numero, rotulo) => `<div class="inicio__estatistica-${nome}"><b class="inicio__estatistica-${nome}-numero">${numero}</b><span class="inicio__estatistica-${nome}-rotulo">${rotulo}</span></div>`;

function renderDashboard() {
  const { offers, requests } = State.data;
  const minhasOfertas = offers.filter(o => o.mine);
  const minhasSolicitacoes = requests.filter(r => r.mine);
  const pendentes = requests.filter(r => !r.mine && r.status === 'pending');
  const hoje = new Date().toISOString().slice(0, 10);
  const proxima = minhasSolicitacoes.filter(r => r.status === 'confirmed' && r.date >= hoje).sort((a, b) => a.date.localeCompare(b.date))[0];
  const ofertaProxima = proxima && offers.find(o => o.id === proxima.offerId);
  const conta = estado => requests.filter(r => r.status === estado).length;
  $('#view-dashboard').innerHTML = `
    <p class="inicio__frase">${proxima ? 'Sua próxima carona está confirmada.' : 'Você ainda não tem carona confirmada.'}</p>
    ${proxima ? `<div class="inicio__proxima">
      <h2 class="inicio__proxima-titulo">${fmtDate(proxima.date)}, ${proxima.time} · ${esc(ofertaProxima.origin)} → Senac Canoas</h2>
      <p class="inicio__proxima-motorista">Motorista: ${esc(ofertaProxima.driver)}</p>
      <p class="inicio__proxima-encontro">Ponto de encontro combinado: ${esc(ofertaProxima.ref)}</p>
    </div>` : UI.empty('inicio', 'Nada confirmado por enquanto', 'Busque uma carona compatível com seu horário.', '<button type="button" class="inicio__vazio-botao" data-go="search">Buscar caronas</button>')}
    <div class="inicio__estatisticas">
      ${estatistica('oferecidas', minhasOfertas.length, 'Caronas oferecidas')}
      ${estatistica('solicitadas', minhasSolicitacoes.length, 'Vagas solicitadas')}
      ${estatistica('confirmadas', conta('confirmed'), 'Confirmadas')}
      ${estatistica('cancelamentos', conta('cancelled'), 'Cancelamentos')}
    </div>
    <div class="inicio__pendentes">
      <h2 class="inicio__pendentes-titulo">Solicitações pendentes</h2>
      <p class="inicio__pendentes-texto">${pendentes.length ? `${pendentes.length} pessoa(s) aguardando sua resposta.` : 'Nenhuma solicitação aguardando resposta.'}</p>
      ${pendentes.length ? '<div class="inicio__pendentes-acoes"><button type="button" class="inicio__pendentes-botao" data-go="requests">Ver solicitações</button></div>' : ''}
    </div>`;
  const contador = $('#reqCount');
  contador.textContent = pendentes.length;
  contador.hidden = !pendentes.length;
}

function cartaoSolicitacao(r) {
  const botoes = r.status === 'pending'
    ? `<div class="card-solicitacao__acoes"><button type="button" class="card-solicitacao__botao-recusar" data-act="recusar" data-id="${r.id}">Recusar</button><button type="button" class="card-solicitacao__botao-aceitar" data-act="aceitar" data-id="${r.id}">Aceitar</button></div>`
    : r.status === 'confirmed'
      ? `<p class="card-solicitacao__privacidade">Confirmado. Combinem o ponto de encontro exato.</p><div class="card-solicitacao__acoes"><button type="button" class="card-solicitacao__botao-cancelar" data-act="cancelar" data-id="${r.id}">Cancelar carona</button></div>`
      : '';
  return `<article class="card-solicitacao">
    <h3 class="card-solicitacao__titulo">${esc(r.who)} ${selo('card-solicitacao', r.status)}</h3>
    <div class="card-solicitacao__metadados"><span class="card-solicitacao__regiao">Região: ${esc(r.region)}</span><span class="card-solicitacao__data">${fmtDate(r.date)}, ${r.time}</span></div>
    ${r.msg ? `<p class="card-solicitacao__mensagem">“${esc(r.msg)}”</p>` : '<p class="card-solicitacao__sem-mensagem">Sem mensagem.</p>'}
    ${botoes}
  </article>`;
}

function renderRequests() {
  const lista = State.data.requests.filter(r => !r.mine);
  $('#view-requests').innerHTML = lista.length
    ? `<div class="solicitacoes__lista">${lista.map(cartaoSolicitacao).join('')}</div>`
    : UI.empty('solicitacoes', 'Nenhuma solicitação recebida', 'Quando alguém pedir uma vaga, ela aparece aqui.', '<button type="button" class="solicitacoes__vazio-botao" data-open="publish">Oferecer carona</button>');
}

const tabela = (nome, colunas, linhas) => `<table class="${nome}">
  <thead class="${nome}__cabecalho"><tr class="${nome}__linha-cabecalho">${colunas.map(([chave, titulo]) => `<th class="${nome}__titulo-${chave}">${titulo}</th>`).join('')}<th class="${nome}__titulo-acao"><span class="${nome}__titulo-acao-texto">Ação</span></th></tr></thead>
  <tbody class="${nome}__corpo">${linhas.join('')}</tbody>
</table>`;
const linha = (nome, celulas) => `<tr class="${nome}__linha">${celulas.map(([chave, html]) => `<td class="${nome}__celula-${chave}">${html}</td>`).join('')}</tr>`;

function renderRides() {
  $$('[data-tab]').forEach(aba => aba.setAttribute('aria-selected', aba.dataset.tab === State.tab));
  const { offers, requests } = State.data;
  let html;
  if (State.tab === 'offered') {
    const nome = 'tabela-oferecidas';
    const colunas = [['origem', 'Origem'], ['destino', 'Destino'], ['data', 'Data'], ['horario', 'Horário'], ['participantes', 'Participantes'], ['vagas', 'Vagas'], ['estado', 'Estado']];
    const linhas = offers.filter(o => o.mine).map(o => {
      const participantes = requests.filter(r => r.offerId === o.id && r.status === 'confirmed').map(r => r.who).join(', ') || 'Ninguém ainda';
      return linha(nome, [['origem', esc(o.origin)], ['destino', 'Senac Canoas'], ['data', fmtDate(o.date)], ['horario', o.time], ['participantes', esc(participantes)], ['vagas', o.seats], ['estado', selo(nome, o.seats ? 'open' : 'full')], ['acao', '']]);
    });
    html = linhas.length ? tabela(nome, colunas, linhas) : '';
  } else {
    const nome = 'tabela-solicitadas';
    const colunas = [['origem', 'Origem'], ['destino', 'Destino'], ['data', 'Data'], ['horario', 'Horário'], ['motorista', 'Motorista'], ['vagas', 'Vagas'], ['estado', 'Estado']];
    const linhas = requests.filter(r => r.mine).map(r => {
      const o = offers.find(x => x.id === r.offerId);
      const encontro = r.status === 'confirmed' ? `<div class="tabela-solicitadas__encontro">Encontro: ${esc(o.ref)}</div>` : '';
      const cancelar = ['pending', 'confirmed'].includes(r.status) ? `<button type="button" class="tabela-solicitadas__botao-cancelar" data-act="cancelar" data-id="${r.id}">Cancelar</button>` : '';
      return linha(nome, [['origem', esc(r.region)], ['destino', 'Senac Canoas'], ['data', fmtDate(r.date)], ['horario', r.time], ['motorista', esc(o.driver) + encontro], ['vagas', o.seats], ['estado', selo(nome, r.status)], ['acao', cancelar]]);
    });
    html = linhas.length ? tabela(nome, colunas, linhas) : '';
  }
  $('#ridesTable').innerHTML = html || UI.empty('caronas', 'Nada por aqui esta semana', 'Ofereça ou busque uma carona para começar.');
}

function renderAll() { renderDashboard(); renderRequests(); renderRides(); }

async function load() {
  $('#view-dashboard').innerHTML = UI.skeleton('inicio', 3);
  try { State.data = await Api.snapshot(); renderAll(); }
  catch { $('#view-dashboard').innerHTML = UI.error('inicio', 'load'); }
}

async function search(evento) {
  if (evento) evento.preventDefault();
  const caixa = $('#searchResults');
  caixa.innerHTML = UI.skeleton('busca', 4);
  try {
    State.results = await Api.listOffers({ data: $('#fDate').value, regiao: $('#fRegion').value, horario: $('#fTime').value, vagas: $('#fSeats').value });
    caixa.innerHTML = State.results.length
      ? State.results.map(cartaoOferta).join('')
      : UI.empty('busca', 'Nenhuma carona encontrada', 'Tente outro dia ou deixe a região em “Todas”.');
  } catch { caixa.innerHTML = UI.error('busca', 'search'); }
}

function go(tela) {
  State.view = tela;
  $$('[data-tela]').forEach(secao => secao.hidden = secao.id !== 'view-' + tela);
  $$('[data-view]').forEach(item => tela === item.dataset.view ? item.setAttribute('aria-current', 'page') : item.removeAttribute('aria-current'));
  $('#pageTitle').textContent = { dashboard: 'Início', search: 'Buscar caronas', requests: 'Solicitações recebidas', rides: 'Minhas caronas' }[tela];
  if (tela === 'search' && State.results === null) search();
}

async function runAction(botao, funcao, mensagemOk) {
  try { await UI.busy(botao, funcao); State.data = await Api.snapshot(); renderAll(); UI.toast(mensagemOk); }
  catch { UI.toast('Não foi possível concluir. Tente novamente.', true); }
}

const obrigatorio = (campo, mensagem) => UI.field(campo, campo.value.trim() ? '' : mensagem);

function resumoPublicacao() {
  const f = $('#publishForm').elements;
  const resumo = $('#pubSummary');
  resumo.textContent = f.origin.value && f.date.value && f.time.value ? `Resumo: ${f.origin.value} → Senac Canoas, ${fmtDate(f.date.value)} às ${f.time.value}, ${f.seats.value} vaga(s).` : '';
  if (f.time.value && +f.time.value.split(':')[0] >= 22) resumo.textContent += ' Este horário parece apertado. Confira antes de publicar.';
}

function definirUsuario(usuario) {
  $('#avatar').textContent = usuario.name.split(' ').map(p => p[0]).slice(0, 2).join('').toUpperCase();
  $('#profileName').textContent = usuario.name;
}

const TEXTOS_CONFIRMACAO = {
  aceitar: ['Aceitar esta solicitação?', 'A pessoa verá o ponto de encontro e poderá combinar os detalhes.', 'Aceitar'],
  recusar: ['Recusar esta solicitação?', 'A pessoa será avisada de que a vaga não está disponível.', 'Recusar'],
  cancelar: ['Cancelar esta carona?', 'Quem participa será avisado. Essa ação não pode ser desfeita.', 'Cancelar carona']
};
const MENSAGENS_SUCESSO = { aceitar: 'Solicitação aceita.', recusar: 'Solicitação recusada.', cancelar: 'Carona cancelada.' };

function bind() {
  document.addEventListener('click', async evento => {
    const botao = evento.target.closest('button');
    if (!botao) return;
    if (botao.dataset.view) go(botao.dataset.view);
    if (botao.dataset.go) go(botao.dataset.go);
    if (botao.dataset.open) $('#dlg-' + botao.dataset.open).showModal();
    if (botao.hasAttribute('data-close')) botao.closest('dialog').close();
    if (botao.dataset.retry) botao.dataset.retry === 'search' ? search() : load();
    if (botao.dataset.tab) { State.tab = botao.dataset.tab; renderRides(); }
    if (botao.dataset.request) {
      const oferta = State.results.find(o => o.id == botao.dataset.request);
      State.pending = oferta;
      $('#reqInfo').textContent = `${oferta.origin} → Senac Canoas · ${fmtDate(oferta.date)}, ${oferta.time} · ${oferta.driver}`;
      $('#dlg-request').showModal();
    }
    if (botao.dataset.act) {
      const acao = botao.dataset.act, id = +botao.dataset.id;
      if (await UI.confirm(...TEXTOS_CONFIRMACAO[acao])) runAction(botao, () => Api.respond(id, acao), MENSAGENS_SUCESSO[acao]);
    }
  });

  document.addEventListener('input', evento => {
    const campo = evento.target;
    if (campo.classList.contains('estado-invalido') && campo.value.trim()) UI.field(campo, '');
  });

  $('#searchForm').addEventListener('submit', search);

  $('#requestForm').addEventListener('submit', async evento => {
    evento.preventDefault();
    try {
      await UI.busy(evento.submitter, () => Api.requestSeat(State.pending.id, evento.target.msg.value));
      $('#dlg-request').close();
      evento.target.reset();
      State.data = await Api.snapshot();
      renderAll();
      UI.toast('Solicitação enviada. Status: pendente.');
      State.tab = 'requested';
      go('rides');
      renderRides();
    } catch { UI.toast('Não foi possível enviar a solicitação.', true); }
  });

  $('#publishForm').addEventListener('input', resumoPublicacao);
  $('#publishForm').addEventListener('submit', async evento => {
    evento.preventDefault();
    const formulario = evento.target, c = formulario.elements;
    const valido = [
      obrigatorio(c.origin, 'Escolha a região de origem.'),
      obrigatorio(c.date, 'Informe a data.'),
      obrigatorio(c.time, 'Verifique o horário informado.'),
      obrigatorio(c.ref, 'Informe um ponto de referência aproximado.'),
      UI.field(c.seats, +c.seats.value >= 1 && +c.seats.value <= 4 ? '' : 'Informe de 1 a 4 vagas.')
    ].every(Boolean);
    if (!valido) return;
    try {
      await UI.busy(evento.submitter, () => Api.createOffer({ origin: c.origin.value, date: c.date.value, time: c.time.value, seats: +c.seats.value, ref: c.ref.value, notes: c.notes.value }));
      $('#dlg-publish').close();
      formulario.reset();
      $$('.estado-valido', formulario).forEach(campo => campo.classList.remove('estado-valido'));
      $('#pubSummary').textContent = '';
      State.data = await Api.snapshot();
      renderAll();
      UI.toast('Oferta publicada.');
      State.tab = 'offered';
      go('rides');
      renderRides();
    } catch { UI.toast('Não foi possível publicar a oferta.', true); }
  });

  $('#onboardForm').addEventListener('submit', async evento => {
    evento.preventDefault();
    const c = evento.target.elements;
    const valido = [
      obrigatorio(c.name, 'Informe seu nome.'),
      UI.field(c.email, /^\S+@\S+\.\S+$/.test(c.email.value) ? '' : 'Use seu e-mail institucional.'),
      obrigatorio(c.region, 'Escolha seu bairro.')
    ].every(Boolean);
    if (!valido) return;
    try {
      const usuario = await UI.busy(evento.submitter, () => Api.register({ name: c.name.value, email: c.email.value, role: c.role.value, region: c.region.value }));
      $('#dlg-onboard').close();
      definirUsuario(usuario);
      UI.toast('Cadastro concluído. Bem-vindo(a)!');
      load();
    } catch { UI.toast('Não foi possível concluir o cadastro.', true); }
  });
  $('#dlg-onboard').addEventListener('cancel', evento => evento.preventDefault());

  const botaoPerfil = $('#profileBtn');
  botaoPerfil.addEventListener('click', () => {
    const menu = $('#profileMenu');
    menu.hidden = !menu.hidden;
    botaoPerfil.setAttribute('aria-expanded', !menu.hidden);
  });
  $('#simError').addEventListener('click', evento => {
    Api.config.fail = !Api.config.fail;
    evento.target.textContent = Api.config.fail ? 'Restaurar conexão' : 'Simular falha de conexão';
    UI.toast(Api.config.fail ? 'Falha simulada ativada.' : 'Conexão restaurada.');
  });
  $('#resetDemo').addEventListener('click', () => { Api.reset(); location.reload(); });

  const atualizarOffline = () => $('#offline').hidden = navigator.onLine;
  addEventListener('online', atualizarOffline);
  addEventListener('offline', atualizarOffline);
  atualizarOffline();

  document.addEventListener('keydown', evento => {
    if (evento.key === 'Escape') { $('#profileMenu').hidden = true; botaoPerfil.setAttribute('aria-expanded', 'false'); }
  });
}

bind();
go('dashboard');
const usuarioSalvo = Api.session();
if (usuarioSalvo) { definirUsuario(usuarioSalvo); load(); }
else $('#dlg-onboard').showModal();
