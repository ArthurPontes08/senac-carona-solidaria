const Icones = {
  caminhos: {
    inicio: 'M3 11l9-8 9 8M5 10v10h14V10',
    buscar: 'M11 4a7 7 0 1 0 0 14 7 7 0 0 0 0-14zM21 21l-4.5-4.5',
    solicitacoes: 'M3 6h18v12H3zM3 7l9 7 9-7',
    caronas: 'M8 6h13M8 12h13M8 18h13M3 6h.01M3 12h.01M3 18h.01',
    mais: 'M12 5v14M5 12h14',
    check: 'M5 12l5 5 9-10',
    info: 'M12 3a9 9 0 1 0 0 18 9 9 0 0 0 0-18zM12 11v6M12 7.5v.01',
    relogio: 'M12 3a9 9 0 1 0 0 18 9 9 0 0 0 0-18zM12 7v5l3 2',
    x: 'M6 6l12 12M18 6L6 18',
    proibido: 'M12 3a9 9 0 1 0 0 18 9 9 0 0 0 0-18zM5.6 5.6l12.8 12.8',
    estrela: 'M12 3l2.7 5.6 6.1.9-4.4 4.3 1 6.1L12 17l-5.4 2.9 1-6.1L3.2 9.5l6.1-.9z',
    alvo: 'M12 3a9 9 0 1 0 0 18 9 9 0 0 0 0-18zM12 8a4 4 0 1 0 0 8 4 4 0 0 0 0-8z',
    quadrado: 'M5 5h14v14H5z',
    cadeado: 'M6 11h12v9H6zM8 11V8a4 4 0 0 1 8 0v3'
  },
  svg(classe, nome) {
    return `<svg class="${classe}" viewBox="0 0 24 24" aria-hidden="true" focusable="false"><path d="${this.caminhos[nome]}"/></svg>`;
  }
};
