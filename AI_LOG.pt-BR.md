# Registro de uso de IA (AI_LOG)

[English](AI_LOG.md) · **Português (Brasil)**

Registrem aqui como o time usou inteligência artificial no projeto. O uso de IA é liberado e incentivado, desde que documentado: este arquivo é entregável obrigatório e serve de roteiro para o bloco “IA na prática” da apresentação.

A documentação do projeto deve ser escrita **em inglês** (a versão principal é o [`AI_LOG.md`](AI_LOG.md)), com uma **versão em português** ao lado. Mantenham as duas versões alinhadas.

Mantenham este arquivo na **raiz** do repositório. Incluam as ferramentas utilizadas e, para cada uso relevante, o prompt principal, o que a IA gerou, o que o time aproveitou e o que foi corrigido ou rejeitado — e por quê.

Nunca commitem chaves de API, senhas ou outros segredos. Esses valores vão em `.env` (fora do repositório) e em GitHub Secrets.

Copiem o modelo abaixo sempre que precisarem de uma entrada nova.

## Ferramentas

- (ex.: ChatGPT, GitHub Copilot, Cursor, outro)

## Modelo de registro

```markdown
### [data] — [tarefa ou trecho]
- **Prompt principal:**
- **O que a IA gerou:**
- **O que o time aproveitou:**
- **O que o time corrigiu ou rejeitou, e por quê:**
```

## Registro


### [data] — [tarefa ou trecho]

- **Prompt principal:**
- **O que a IA gerou:**
- **O que o time aproveitou:**
- **O que o time corrigiu ou rejeitou, e por quê:**


## Ferramentas
- Gemini (Google AI)
- GitHub Copilot usando o Copilot SDK no VS Code

## Registro

### 24/11/2026 — Estruturação inicial de pastas do Back-end, Front-end e CI/CD
- **Prompt principal:** "Ajude a organizar a estrutura de pastas e arquivos para um back-end Spring Boot (módulo de caronas), um front-end em HTML/CSS/JS puro e uma pipeline de CI no GitHub Actions com build, lint e testes."
- **O que a IA gerou:** Passo a passo para criar os arquivos em camadas do back-end (`Carona.java`, `CaronaService.java`, `CaronaController.java`), os arquivos modulares do front-end (`index.html`, `css/style.css`, `js/api.js`, `js/main.js`) e o arquivo `.github/workflows/ci.yml`, além do fluxo de branches e commits.
- **O que o time aproveitou:** A hierarquia completa de pastas, a separação das chamadas de API (`api.js`) da manipulação de tela (`main.js`) no front-end e a estrutura do workflow de CI no GitHub Actions.
- **O que o time corrigiu ou rejeitou, e por quê:** Rejeitamos o uso de React no front-end e o deploy em nuvem (Render/Docker) neste momento, optando por JS puro e execução local para adequar o escopo ao prazo de uma semana do hackathon e à familiaridade do time.

### 24/11/2026 — Otimização da documentação da divisão de responsabilidades
- **Prompt principal:** Solicitar a organização da divisão técnica do grupo no README e nos dois briefings, com uma justificativa detalhada da decisão e o registro do uso de IA na documentação.
- **O que a IA gerou:** Uma seção bilíngue de responsabilidades no README e nos briefings, uma entrada detalhada em `DECISIONS.md` e `DECISIONS.pt-BR.md`, e um registro deste uso em `AI_LOG.md` e `AI_LOG.pt-BR.md`.
- **O que o time aproveitou:** A IA foi usada para acelerar a escrita, organizar melhor os textos e manter a versão em inglês alinhada à versão em português. Também ajudou a transformar uma decisão curta em uma justificativa completa sobre os pontos fortes do time e a redução de retrabalho.
- **O que o time corrigiu ou rejeitou, e por quê:** Antes de registrar, o time confirmou os nomes e as áreas responsáveis. Textos genéricos foram detalhados e revisados para refletir a realidade do projeto, sem atribuir trabalho que não tenha sido combinado. A IA foi usada como apoio à redação e à organização; a decisão final sobre as responsabilidades foi definida e aprovada pelo grupo.

### 24/11/2026 — Atualização da divisão técnica, documentação colaborativa e prioridade desktop
- **Prompt principal:** Atualizar a documentação para definir Arthur como responsável pelo back-end completo, Lívia como responsável pelo banco de dados e João como responsável pelo front-end/UI/UX; registrar que toda a documentação será feita pelos três simultaneamente no Google Docs; e registrar que o projeto será priorizado em desktop, com responsividade apenas se houver tempo.
- **O que a IA gerou:** A revisão das responsabilidades no README e nos briefings bilíngues, a alteração das decisões técnicas em `DECISIONS.md` e `DECISIONS.pt-BR.md`, e a organização do uso da IA nesta atualização no registro bilíngue de IA.
- **O que o time aproveitou:** Aproveitamos a IA para manter a terminologia e o conteúdo consistentes entre todas as versões dos documentos. A ferramenta também tornou explícitas as alternativas consideradas e os motivos das decisões sobre responsabilidade, colaboração documental e prioridade desktop.
- **O que o time corrigiu ou rejeitou, e por quê:** O time confirmou a troca da responsabilidade de back-end para Arthur e a responsabilidade de banco de dados para Lívia antes da geração final. Também confirmou que Google Docs será usado para edição compartilhada em tempo real e que responsividade não é uma garantia, mas uma etapa opcional dependendo do tempo restante. A IA auxiliou a otimizar e padronizar a documentação; todas as decisões finais foram verificadas pelo grupo.

