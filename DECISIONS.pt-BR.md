# Decisões do projeto

[English](DECISIONS.md) · **Português (Brasil)**

Registrem aqui as decisões importantes do time. Cada entrada deve ser curta e trazer o que foi decidido, as alternativas consideradas e o motivo da escolha.

A documentação do projeto deve ser escrita **em inglês** (a versão principal é o [`DECISIONS.md`](DECISIONS.md)), com uma **versão em português** ao lado. Mantenham as duas versões alinhadas.

Mantenham este arquivo na **raiz** do repositório. Incluam uma entrada nova para cada decisão relevante (stack, arquitetura, recorte do MVP, o que ficou de fora, caminhos que vocês tentaram e abandonaram etc.). Este arquivo serve de roteiro para o bloco de decisões da apresentação.

Copiem o modelo abaixo sempre que precisarem de uma entrada nova.

## Modelo

```markdown
## [data] — [título da decisão]
- **Decisão:**
- **Alternativas consideradas:**
- **Motivo:**
```

## 23/11/2026 — Escolha da stack de back-end (exemplo)

- **Decisão:** usar Node.js com Express no back-end.
- **Alternativas consideradas:** Java com Spring Boot; Python com FastAPI.
- **Motivo:** o time já tem mais familiaridade com JavaScript, e o prazo de uma semana favorece uma stack na qual todos conseguem contribuir desde o primeiro dia.

## 24/11/2026 — Escolha da stack de back-end (Java + Spring Boot)

- **Decisão:** Utilizar Java 17 com Spring Boot (Spring Web) e Maven para a API back-end, organizada em camadas (`controller`, `service`, `model`).
- **Alternativas consideradas:** Node.js com Express; Python com FastAPI.
- **Motivo:** O time já possui familiaridade com Java no curso técnico, e o Spring Boot oferece servidor embutido (Tomcat) e suporte nativo a testes com JUnit, agilizando o desenvolvimento e a validação no CI.

## 24/11/2026 — Escolha da stack de front-end (HTML, CSS e JavaScript puro)

- **Decisão:** Utilizar HTML5, CSS3 e JavaScript puro (`index.html`, `css/style.css`, `js/api.js`, `js/main.js`) para o front-end web.
- **Alternativas consideradas:** React com Vite.
- **Motivo:** Dado o prazo de uma semana e a curva de aprendizado atual do time com React, o JS puro evita complexidade de build, simplifica a integração com a API REST via `fetch` e facilita a configuração de lint e testes na pipeline de CI.

## 24/11/2026 — Configuração da pipeline de CI com GitHub Actions

- **Decisão:** Configurar um workflow no GitHub Actions (`.github/workflows/ci.yml`) disparado em todo Pull Request e push na `main`, cobrindo build, lint e testes automatizados para back-end e front-end.
- **Alternativas consideradas:** Rodar verificações manualmente antes de aprovar PRs.
- **Motivo:** Garante a qualidade do código, impede que código quebrado entre na `main` e atende aos requisitos obrigatórios de CI/CD desde o início do projeto.

## 25/11/2026 — Origem livre para as caronas

- **Decisão:** Permitir que o usuário informe a origem em texto livre no cadastro, na oferta e na busca de caronas; a busca usa correspondência parcial sem diferenciar maiúsculas e minúsculas.
- **Alternativas consideradas:** Restringir a origem a uma lista fixa de bairros de Canoas ou adicionar um serviço externo de busca de locais.
- **Motivo:** O texto livre aceita origens fora dos bairros predefinidos sem introduzir dependência externa.

## 25/11/2026 — Simulação de falha de conexão

- **Decisão:** Manter o controle de conexão como uma simulação repetível de falha da API, atualizando o banner offline e recarregando a tela atual.
- **Alternativas consideradas:** Alterar a conexão real do navegador ou apenas exibir uma notificação de confirmação.
- **Motivo:** A falha simulada permite demonstrar e verificar estados de erro previsivelmente, sem afetar a rede do dispositivo.

## 25/11/2026 — Identidade visual Vai Junto

- **Decisão:** Usar a logo fornecida Vai Junto como recurso visual do front-end, incluindo a barra lateral e o favicon do navegador.
- **Alternativas consideradas:** Manter a logo anterior ou carregar a imagem de um serviço externo.
- **Motivo:** Um recurso local mantém a identidade visual consistente e evita dependência externa durante a execução.

## 25/11/2026 — Fluxo básico de conversa para caronas confirmadas

- **Decisão:** Na demonstração do front-end, disponibilizar uma conversa por solicitação tanto para motorista quanto para passageiro após a confirmação; armazenar as mensagens no navegador.
- **Alternativas consideradas:** Abrir um aplicativo externo de mensagens ou implementar imediatamente um chat compartilhado completo no back-end.
- **Motivo:** O front-end atual usa uma API simulada local e não possui autenticação. Conversas compartilhadas entre sessões ou dispositivos exigem persistência no back-end e integração de identidade, não oferecidas por esta demonstração.

## 24/11/2026 — Divisão detalhada das responsabilidades do time

- **Decisão:** João Cenerelli ficará responsável pela implementação do front-end em HTML, CSS e JavaScript e pela UI/UX. Arthur ficará responsável pelo back-end completo. Lívia ficará responsável pelo banco de dados, incluindo modelagem, relacionamentos e consultas.
- **Alternativas consideradas:** Dividir o trabalho por funcionalidades completas; alternar responsabilidades entre front-end e back-end; ou distribuir todas as tarefas sem responsáveis principais definidos.
- **Motivo:** A divisão considera os pontos fortes atuais do time e aproveita o domínio de João no front-end, o domínio de Arthur no back-end e o domínio de Lívia no banco de dados. Isso reduz trocas de contexto, preserva a consistência da interface e aumenta a previsibilidade das entregas no prazo de uma semana. A divisão não impede colaboração: integrações, revisões e decisões técnicas continuam sendo feitas pelo grupo.

## 24/11/2026 — Colaboração na documentação

- **Decisão:** Toda a documentação do projeto será escrita pelos três integrantes de forma simultânea no Google Docs.
- **Alternativas consideradas:** Dividir documentos únicos entre integrantes ou designar uma única pessoa para toda a documentação.
- **Motivo:** O Google Docs permite que todos vejam as alterações dos demais em tempo real, facilita alinhamentos, comentários e revisões conjuntas e evita conflitos de informações entre os documentos em português e inglês.

## 24/11/2026 — Prioridade da interface desktop

- **Decisão:** Priorizar a versão desktop do front-end e implementar responsividade somente se houver tempo suficiente após concluir o fluxo essencial do MVP.
- **Alternativas consideradas:** Começar pela responsividade completa em todos os tamanhos de tela ou construir desktop e mobile simultaneamente.
- **Motivo:** A implementação desktop permite entregar primeiro um fluxo visual completo e testável dentro do prazo de uma semana. Responsividade será adicionada como etapa posterior, sem comprometer as funcionalidades indispensáveis do MVP.

## 25/11/2026 — Validações e testes do back-end

- **Decisão:** Manter o MVP em memória e implementar validações defensivas, transições sincronizadas de estado, testes unitários e respostas HTTP centralizadas.
- **Alternativas consideradas:** Introduzir JPA e banco de dados imediatamente.
- **Motivo:** A implementação em memória preserva a demonstração local do hackathon e deve ser substituída pelo módulo de banco após a aprovação do esquema e das entidades JPA.

## 25/11/2026 — Responsabilidade do back-end e regras de estado da API

- **Decisão:** Arthur manterá estáveis o serviço em memória e o contrato REST para integração; banco de dados e frontend usarão as rotas e os estados documentados.
- **Alternativas consideradas:** Adicionar autenticação ou persistência antes de Lívia e João concluírem seus módulos.
- **Motivo:** O prazo favorece um contrato estável do MVP. O back-end agora valida datas e horários, solicitações duplicadas, vagas disponíveis, transições e cancelamento da carona; persistência e autenticação permanecem temas de integração.
