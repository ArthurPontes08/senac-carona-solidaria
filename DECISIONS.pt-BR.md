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

