# VAI JUNTO | Carona Solidária — Vida no campus e comunidade.

[English](README.md) · **Português (Brasil)**

## Equipe

- **Turma:** TDS | turmas mistas 6570, 6756 e 6863
- **Tech Lead:** Arthur
- **Responsável pela apresentação:** João
- **Integrantes:** Livia
  - Arthur | GitHub
  - Lívia | GitHub
  - João Cenerelli | GitHub

## Tema / área

Carona solidária | Vida no campus e comunidade.

## Problema

O problema que vamos resolver é sobre a dificuldade de organizar e conectar pessoas da comunidade Senac Canoas que possuem trajetos semelhantes, mas precisam encontrar ou oferecer caronas de forma prática e segura.

## Solução

A aplicação Carona Solidária conecta alunos e funcionários do Senac Canoas que oferecem caronas com pessoas que precisam de transporte, permitindo organizar trajetos, horários e vagas em um único ambiente.

O usuário pode cadastrar-se, oferecer ou procurar caronas, visualizar opções compatíveis, solicitar uma vaga e acompanhar o status das solicitações e das caronas combinadas.

A aplicação permite que alunos e funcionários do Senac Canoas ofereçam e encontrem caronas entre pessoas da comunidade, possibilitando a publicação e busca de trajetos, solicitação e aprovação de vagas, além do gerenciamento das caronas em um único ambiente. O MVP contempla cadastro de usuários, publicação de ofertas, busca, solicitações, aprovação ou recusa, cancelamentos e acompanhamento das caronas, priorizando um fluxo simples, funcional e seguro.

O usuário pode informar qualquer local de origem, como bairro, cidade ou endereço; a busca de caronas aceita correspondências parciais.

O backend oferece conversas persistidas via JPA para solicitações confirmadas. O front-end ainda usa um mock local por padrão; João precisa conectá-lo à API e enviar o `userId` para que as mensagens sejam compartilhadas entre usuários e dispositivos.

**O que não entra no MVP:**
- Pagamentos
- Rastreamento em tempo real
- Marketplace de transporte
- Sistema complexo de avaliações
- Denúncias avançadas
- Chat compartilhado entre sessões de usuários ou dispositivos diferentes
- Algoritmo sofisticado de rotas
- Caronas abertas para pessoas de fora da comunidade Senac

## Stack

Java 17 com Spring Boot, Spring Data JPA e Maven são utilizados no back-end. PostgreSQL é o banco de dados configurado. O front-end usa HTML5, CSS3 e JavaScript puro.

A conexão com o PostgreSQL é configurada pelas variáveis de ambiente `DB_URL`, `DB_USERNAME` e `DB_PASSWORD`. O banco local precisa ser criado antes de iniciar a aplicação. O Hibernate usa `ddl-auto=update` por padrão neste momento; isso facilita o desenvolvimento, mas não substitui migrations versionadas. O service principal agora usa repositories Spring Data JPA para persistir usuários, caronas e solicitações. Ainda é necessário verificar a integração PostgreSQL com o schema da equipe.

O endpoint de cadastro aceita telefone ausente ou em branco e o armazena como `null`; quando informado, o telefone precisa seguir o formato aceito.

O `SolicitacaoCaronaRepository` oferece operações JPA e consultas de solicitações por carona, passageiro e status, e é usado pelo `CaronaService`. As operações que alteram vagas rodam em transações e bloqueiam a linha da carona correspondente. O backend não cria mais usuários e caronas de demonstração ao iniciar; o front-end continua usando um mock local por padrão, e ainda precisa enviar o ID do passageiro ao solicitar uma vaga.

`POST /api/usuarios` aceita os campos do front-end `name`, `email`, `role` (`both`, `driver` ou `rider`) e `region`; a resposta contém `id`, `name`, `email`, `role` e `region`. Os nomes dos campos em português também são aceitos para compatibilidade. O telefone é opcional e não aparece nas respostas nem na listagem de usuários.

`GET /api/ofertas` aceita os filtros opcionais `data`, `regiao`, `horario` (`HH:mm`, horário mínimo de saída) e `vagas` (mínimo de vagas disponíveis). A resposta usa campos do front-end como `driver`, `origin`, `destination`, `date`, `time`, `seats`, `ref`, `summary` e `match`. `POST /api/ofertas` recebe `driverId`, `origin`, `date`, `time`, `seats`, `ref` e `notes` opcional; se `destination` não for enviado, usa `Campus Senac Canoas`. `driverId` é obrigatório porque o backend não consegue inferir o usuário atual sem autenticação.

`POST /api/ofertas/{id}/solicitacoes` exige `passengerId` e aceita `msg` opcional com até 140 caracteres. A mensagem é salva na coluna anulável `mensagem VARCHAR(140)` da tabela `solicitacoes_carona`. O schema/migration do banco precisa incluir essa coluna; o `update` do Hibernate pode criá-la no desenvolvimento local, mas não substitui uma migration versionada.

`GET /api/minhas-caronas?userId={id}` retorna o formato de snapshot do front-end, com `offers` e `requests`, estados em inglês e indicadores `mine`. O front-end precisa enviar o ID do usuário cadastrado. Como não há autenticação, esse ID enviado pelo cliente serve apenas como contexto e não comprova a identidade.

`PATCH /api/solicitacoes/{id}` recebe `{"userId": 7, "action": "accept"}` (também aceita `refuse` ou `cancel`). Somente o motorista pode aceitar ou recusar; o motorista ou o passageiro solicitante pode cancelar. A rota legada `/responder` exige `motoristaId`. Esses IDs são verificações de propriedade dentro da API, não autenticação.

O chat usa `GET /api/solicitacoes/{id}/conversa?userId={id}` e `POST /api/solicitacoes/{id}/conversa` com `{"userId": 7, "text": "..."}`. Somente o motorista e o passageiro de uma solicitação confirmada podem ler ou enviar mensagens; o texto pode ter até 500 caracteres. A migration do banco deve criar `mensagens_chat` com `id BIGSERIAL`, `solicitacao_id BIGINT`, `remetente_id BIGINT`, `texto VARCHAR(500)` e `criado_em TIMESTAMPTZ`, além de chaves estrangeiras para `solicitacoes_carona(id)` e `usuarios(id)` e um índice em `(solicitacao_id, criado_em)`.

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
- PostgreSQL, com o banco local criado
- Navegador web atualizado (Chrome, Edge, Firefox)

### Back-end

Defina as variáveis de conexão no mesmo terminal em que iniciará a aplicação. Não faça commit de credenciais reais.

PowerShell:
```powershell
cd backend
$env:DB_URL = "jdbc:postgresql://localhost:5432/carona_solidaria"
$env:DB_USERNAME = "seu_usuario_postgres"
$env:DB_PASSWORD = "sua_senha_postgres"
.\mvnw.cmd spring-boot:run
```

Linux/macOS:
```bash
cd backend
export DB_URL="jdbc:postgresql://localhost:5432/carona_solidaria"
export DB_USERNAME="seu_usuario_postgres"
export DB_PASSWORD="sua_senha_postgres"
./mvnw spring-boot:run
```

A API ficará disponível em `http://localhost:8080`. `DB_DDL_AUTO` pode sobrescrever o modo de schema do Hibernate; o padrão atual é `update`. `DB_SHOW_SQL` controla a exibição de SQL e o padrão é `false`.

### Front-end
Abra o arquivo `frontend/index.html` diretamente no navegador ou utilize a extensão **Live Server** no VS Code.

## CI/CD

O repositório utiliza **GitHub Actions** (`.github/workflows/ci.yml`), executado automaticamente em todo Pull Request e em todo push na `main`. A pipeline roda dois jobs (`backend-ci` e `frontend-ci`) contemplando:
1. **Build:** Compila o projeto Java (`./mvnw clean compile`) e valida os arquivos principais do front-end.
2. **Lint:** Executa verificações de estilo e sintaxe de código.
3. **Testes:** Roda os testes automatizados por camada (`./mvnw test`).

## Conventional Commits

Use o formato `<tipo>(<escopo>): <descrição curta>` nas mensagens de commit. Os tipos aceitos são `feat` (nova funcionalidade), `fix` (correção de bug), `docs` (documentação), `style` (formatação sem mudança de comportamento), `refactor` (organização do código sem funcionalidade nova ou correção), `test` (testes), `chore` (manutenção) e `ci` (CI/CD).

Exemplos:
- `feat(chat): adiciona interface de conversa para caronas confirmadas`
- `fix(api): corrige validação do cadastro`
- `docs(readme): descreve como rodar o projeto localmente`
- `ci(actions): adiciona job de lint no workflow`

## Deploy

O deploy automático em nuvem não foi configurado; o MVP é executado localmente para fins de demonstração.

## Links

- **Repositório:**
- **Deploy (se houver):**
- **Board / projeto no GitHub (se houver):**
