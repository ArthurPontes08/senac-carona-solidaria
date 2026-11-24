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

## Registro

### 24/11/2026 — Estruturação inicial de pastas do Back-end, Front-end e CI/CD
- **Prompt principal:** "Ajude a organizar a estrutura de pastas e arquivos para um back-end Spring Boot (módulo de caronas), um front-end em HTML/CSS/JS puro e uma pipeline de CI no GitHub Actions com build, lint e testes."
- **O que a IA gerou:** Passo a passo para criar os arquivos em camadas do back-end (`Carona.java`, `CaronaService.java`, `CaronaController.java`), os arquivos modulares do front-end (`index.html`, `css/style.css`, `js/api.js`, `js/main.js`) e o arquivo `.github/workflows/ci.yml`, além do fluxo de branches e commits.
- **O que o time aproveitou:** A hierarquia completa de pastas, a separação das chamadas de API (`api.js`) da manipulação de tela (`main.js`) no front-end e a estrutura do workflow de CI no GitHub Actions.
- **O que o time corrigiu ou rejeitou, and why:** Rejeitamos o uso de React no front-end e o deploy em nuvem (Render/Docker) neste momento, optando por JS puro e execução local para adequar o escopo ao prazo de uma semana do hackathon e à familiaridade do time.

