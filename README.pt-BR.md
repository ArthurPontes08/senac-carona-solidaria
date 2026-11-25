# CARONA SOLIDÁRIA | Vida no campus e comunidade.

[English](README.md) · **Português (Brasil)**

## Equipe

- **Turma:** TDS | turmas mistas 6570, 6756 e 6863
- **Tech Lead:** Arthur
- **Responsável pela apresentação:** João
- **Integrantes:**
  - João Cenerelli | Front-end com HTML, CSS e JavaScript, responsável por UI/UX | GitHub
  - Arthur | Back-end | GitHub
  - Lívia | Banco de dados | GitHub

### Responsabilidades detalhadas

- **João Cenerelli:** responsável pela implementação completa do front-end em HTML, CSS e JavaScript e pela UI/UX do produto. A escolha considera o maior domínio de João nessas tecnologias e a necessidade de manter a interface visual consistente.
- **Arthur:** responsável pelo back-end completo, incluindo API, regras de negócio, serviços e integração com o front-end.
- **Lívia:** responsável pelo banco de dados, incluindo modelagem das entidades, relacionamentos, consultas e integração dos dados com o back-end.

As áreas podem ser revisadas em conjunto, mas esta divisão define a responsabilidade principal de cada integrante durante o desenvolvimento do MVP.

Toda a documentação será feita pelos três integrantes de forma simultânea, utilizando Google Docs para edição compartilhada em tempo real. Os textos concluídos serão registrados no repositório nas versões em inglês e português.

A prioridade visual será a versão desktop. Responsividade será implementada somente se houver tempo após concluir o fluxo essencial do MVP.

## Tema / área

Carona solidária | Vida no campus e comunidade.

## Problema

O problema que vamos resolver é sobre a dificuldade de organizar e conectar pessoas da comunidade Senac Canoas que possuem trajetos semelhantes, mas precisam encontrar ou oferecer caronas de forma prática e segura.

## Solução

A aplicação Carona Solidária conecta alunos e funcionários do Senac Canoas que oferecem caronas com pessoas que precisam de transporte, permitindo organizar trajetos, horários e vagas em um único ambiente.

O usuário pode cadastrar-se, oferecer ou procurar caronas, visualizar opções compatíveis, solicitar uma vaga e acompanhar o status das solicitações e das caronas combinadas.

A aplicação permite que alunos e funcionários do Senac Canoas ofereçam e encontrem caronas entre pessoas da comunidade, possibilitando a publicação e busca de trajetos, solicitação e aprovação de vagas, além do gerenciamento das caronas em um único ambiente. O MVP contempla cadastro de usuários, publicação de ofertas, busca, solicitações, aprovação ou recusa, cancelamentos e acompanhamento das caronas, priorizando um fluxo simples, funcional e seguro.

**O que não entra no MVP:**
- Pagamentos
- Rastreamento em tempo real
- Marketplace de transporte
- Sistema complexo de avaliações
- Denúncias avançadas
- Chat interno completo
- Algoritmo sofisticado de rotas
- Caronas abertas para pessoas de fora da comunidade Senac

## Stack

Java 17 com Spring Boot (Spring Web) e Maven são utilizados no back-end, e HTML5, CSS3 e JavaScript puro no front-end. Ainda não escolhemos qual banco de dados vamos utilizar.

## Estrutura de pastas

```text
/
├── .github/
│   └── workflows/
│       └── ci.yml
├── assets/
├── backend/
│   └── src/main/java/com/senac/carona/
│       ├── controller/CaronaController.java
│       ├── model/Carona.java
│       ├── service/CaronaService.java
│       └── CaronaApplication.java
├── frontend/
│   ├── css/style.css
│   ├── js/api.js
│   ├── js/main.js
│   └── index.html
└── docs/
    ├── README.md
    ├── README.pt-BR.md
    ├── DECISIONS.md
    ├── DECISIONS.pt-BR.md
    ├── AI_LOG.md
    └── AI_LOG.pt-BR.md
```

## Como rodar

### Pré-requisitos
- Java 17 (JDK)
- Maven (ou o wrapper `./mvnw` incluso no projeto)
- Navegador web atualizado (Chrome, Edge, Firefox)

### Back-end
```bash
cd backend
./mvnw spring-boot:run
```
A API ficará disponível em `http://localhost:8080`.

### Front-end
Abra o arquivo `frontend/index.html` diretamente no navegador ou utilize a extensão **Live Server** no VS Code.

## CI/CD

O repositório utiliza **GitHub Actions** (`.github/workflows/ci.yml`), executado automaticamente em todo Pull Request e em todo push na `main`. A pipeline roda dois jobs (`backend-ci` e `frontend-ci`) contemplando:
1. **Build:** Compila o projeto Java (`./mvnw clean compile`) e valida os arquivos principais do front-end.
2. **Lint:** Executa verificações de estilo e sintaxe de código.
3. **Testes:** Roda os testes automatizados por camada (`./mvnw test`).

## Deploy

O deploy automático em nuvem não foi configurado; o MVP é executado localmente para fins de demonstração.

## Links

- **Repositório:**
- **Deploy (se houver):**
- **Board / projeto no GitHub (se houver):**
