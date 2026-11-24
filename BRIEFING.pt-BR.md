# Briefing do tema: Carona solidária

[English](BRIEFING.md) · **Português (Brasil)**

**Hack com IA: Codando com Inteligência**  
Senac na Prática · Técnico em Desenvolvimento de Sistemas  
Área: vida no campus e comunidade  
Imersão de 23/11 a 29/11 · apresentações em 30/11 e 01/12

Este briefing é o ponto de partida do **seu** tema. Leiam com calma, recortem um MVP que caiba em uma semana e registrem as decisões. Vocês vão transformar este problema em um produto real, com front-end e back-end, repositório organizado e uso documentado de IA. Esta é uma imersão colaborativa: a banca avalia o que o time construiu, decidiu e aprendeu.

## Tema e contexto

**Carona solidária** — App de caronas entre alunos e funcionários do Senac, combinando rotas e horários.

Muita gente chega ao Senac de Canoas de cidades e bairros vizinhos, em horários parecidos, com carro pela metade ou dependendo de ônibus e combinações improvisadas. As caronas existem, mas vivem em grupos de mensagens: a oferta se perde, o horário não bate, e falta um jeito simples de combinar rota com antecedência — com o recorte desta comunidade, não de um serviço aberto a qualquer pessoa.

Vocês vão facilitar o combinado entre alunos e funcionários do Senac que já fazem caminhos semelhantes, com clareza de horário, pontos e vagas, e com cuidado com segurança e privacidade.

## Público e personas

- **Quem dirige:** aluno ou funcionário que tem vagas no carro e um trajeto recorrente ou pontual até o campus (ou de volta).
- **Quem precisa de carona:** quer encontrar uma oferta compatível com o horário e a região, sem expor demais seus dados antes do combinado.
- **Quem organiza a rotina da semana:** precisa ver, em um só lugar, o que já está combinado e o que ainda está aberto.

## Objetivo

Entregar um MVP em que pessoas da comunidade Senac oferecem ou buscam caronas, o sistema (com apoio de IA) ajuda a combinar rotas e horários, e o combinado pode ser confirmado ou cancelado de forma clara.

## Requisitos mínimos (MVP)

O recorte abaixo é realista para uma semana. Fechem-no no primeiro dia e registrem no `DECISIONS.md` o que entra, o que fica de fora e por quê. É melhor um fluxo completo e demonstrável do que uma lista longa pela metade.

1. Cadastro simples, indicando se a pessoa oferece carona, busca carona, ou os dois, com bairro ou região de origem e destino no campus.
2. Publicar uma oferta: origem, destino, data, horário e número de vagas.
3. Buscar ofertas por dia e por região, vendo o essencial do trajeto.
4. Solicitar uma vaga e permitir que quem dirige aceite ou recuse.
5. Confirmar ou cancelar o combinado, com status visível para os dois lados.
6. Listar “minhas caronas” da semana — oferecidas e pedidas.

## Onde a IA entra

O uso de IA é liberado e incentivado — no produto e no processo — e precisa aparecer no `AI_LOG.md`. Algumas frentes concretas neste tema:

- Sugerir ofertas compatíveis com o horário e a região informados.
- Ajudar a descrever o trajeto em linguagem clara a partir de pontos simples (bairro, rua principal, campus).
- Avisar quando o encaixe de horário estiver apertado demais.
- Resumir, para a pessoa, as opções do dia em uma lista curta e legível.

Não publiquem, sem revisão de vocês, um texto ou uma decisão automática da IA como se já fosse a versão final.

## Extras opcionais

Só depois que o MVP estiver em pé. São ideias, não obrigações:

- Caronas recorrentes (por exemplo, toda segunda às 18h).
- Estimativa simples de vagas aproveitadas na semana.
- Ponto de encontro sugerido no entorno do campus.
- Lembrete do combinado no dia da carona.

## Fora de escopo

Para proteger o prazo e o foco do produto:

- Pagamento, tarifa ou mercado de corridas.
- Rastreamento em tempo real no estilo de aplicativo de transporte pago.
- Caronas abertas ao público em geral — o recorte é a comunidade do Senac.
- Um sistema completo de denúncias e verificação formal de antecedentes.

## Perguntas para guiar o time

Usem estas perguntas no primeiro dia, ao definir o MVP, e de novo antes da demo:

- Como vocês indicam, no MVP, que as duas pessoas fazem parte da comunidade do Senac?
- Que dados aparecem antes do combinado, e o que só aparece depois da confirmação?
- O que acontece se alguém cancelar de última hora?
- Como a interface comunica combinados de segurança (ponto visível, respeito aos horários) sem transformar o app em um manual?
- Como o encaixe de rotas se comporta quando origem e destino são só aproximados?

## Entregáveis e como a banca avalia

Esta é uma **imersão colaborativa** de uma semana, com times de 3 ou 4 estudantes do TDS. A banca avalia o trabalho de **vocês**, no próprio projeto — um retorno formativo, não uma comparação com outros times.

**O que precisa estar pronto no repositório** (raiz), em **inglês** (versão principal) e em **português** (`.pt-BR`), com o link entre os idiomas no topo de cada arquivo:

- `README.md` e `README.pt-BR.md`
- `DECISIONS.md` e `DECISIONS.pt-BR.md`
- `AI_LOG.md` e `AI_LOG.pt-BR.md`

A aplicação precisa ter **front-end e back-end**. O back-end pode ser **Java**, **Python** ou **Node.js** (isolados ou combinados, desde que integrados). O front-end é obrigatório (web, desktop ou mobile). Justifiquem a stack no `DECISIONS.md`. Também são obrigatórios o histórico com Conventional Commits, o trabalho via Pull Request com revisão, a proteção da `main` e uma pipeline de **GitHub Actions** verde (build, lint e testes) em PRs e na `main`. Não commitem segredos. No primeiro dia, adicionem o professor como colaborador: **adrianoanschau@gmail.com**.

O **congelamento do código** é no domingo, **29/11, às 23h59**. As apresentações são em **15 minutos + 5 minutos de perguntas**, nos dias 30/11 e 01/12, no Senac de Canoas, Sala 103. Ensaiem a demo e o roteiro (problema e solução, demo ao vivo, fluxo de trabalho, decisões, IA na prática e CI/CD).

A avaliação tem **duas bancas, com o mesmo peso**:

- **Banca Técnica (50%):** qualidade de código e arquitetura; Git, Conventional Commits e PRs; CI/CD; domínio do código nas perguntas.
- **Banca de Inovação e Impacto (50%):** originalidade; impacto (o problema real e para quem); experiência do usuário; clareza do pitch; uso criativo da IA.

Qualquer pessoa do time pode ser perguntada sobre qualquer parte do projeto. Usem a IA com inteligência, registrem o que ela gerou e o que vocês corrigiram, e assumam o código que forem apresentar.

Bom trabalho — e que a semana seja intensa, colaborativa e bem documentada.

