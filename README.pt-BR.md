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

O backend oferece conversas persistidas via JPA para solicitações confirmadas. O front-end está em modo API (`MOCK: false`); o `localStorage` mantém somente a sessão do usuário no navegador, não ofertas, solicitações ou mensagens. A API não tem mecanismo de login/autenticação, então o ID de usuário enviado pelo cliente serve apenas como contexto de propriedade.

**O que não entra no MVP:**
- Pagamentos
- Rastreamento em tempo real
- Marketplace de transporte
- Sistema complexo de avaliações
- Denúncias avançadas
- Autenticação completa e recuperação de conta
- Algoritmo sofisticado de rotas
- Caronas abertas para pessoas de fora da comunidade Senac

## Stack

Java 17 com Spring Boot, Spring Data JPA e Maven são utilizados no back-end. PostgreSQL é o banco de dados configurado. O front-end usa HTML5, CSS3 e JavaScript puro.

A conexão com o PostgreSQL é configurada pelas variáveis de ambiente `DB_URL`, `DB_USERNAME` e `DB_PASSWORD`. O banco local precisa ser criado antes de iniciar a aplicação. O Hibernate usa `ddl-auto=update` por padrão neste momento; isso facilita o desenvolvimento, mas não substitui migrations versionadas. O service principal usa repositories Spring Data JPA para persistir usuários, caronas, solicitações e mensagens. A conexão real ainda deve ser validada na instância PostgreSQL de cada equipe.

Por padrão, o CORS da API permite somente `http://localhost:5500` e `http://127.0.0.1:5500`. Defina `CORS_ALLOWED_ORIGINS` com uma lista separada por vírgulas se o front-end for servido por outra origem confiável.

O endpoint de cadastro aceita telefone ausente ou em branco e o armazena como `null`; quando informado, o telefone precisa seguir o formato aceito.

O `SolicitacaoCaronaRepository` oferece operações JPA e consultas de solicitações por carona, passageiro e status, e é usado pelo `CaronaService`. As operações que alteram vagas rodam em transações e bloqueiam a linha da carona correspondente. O backend não cria usuários ou caronas de demonstração ao iniciar. O front-end está em modo API (`MOCK: false`); o `localStorage` mantém somente a sessão do usuário no navegador, não ofertas, solicitações ou mensagens. A API não tem mecanismo de login/autenticação, então o ID de usuário enviado pelo cliente serve apenas como contexto de propriedade.

Ao abrir o site sem sessão, o usuário vê primeiro o modal de login e pode alternar para o cadastro. `POST /api/usuarios` aceita `name`, `email`, `password`, `role` (`both`, `driver` ou `rider`) e `region`; senhas de 8 a 128 caracteres são armazenadas com hash PBKDF2. `POST /api/usuarios/login` recebe `email` e `password` e retorna os dados públicos do usuário; credenciais inválidas retornam HTTP 401. O campo `senha_hash` é adicionado pelo Hibernate, mas usuários já existentes não têm senha: é necessário provisionar uma senha para essas contas antes do primeiro login. Senhas nunca são incluídas nas respostas.

Os nomes dos campos em português também são aceitos no cadastro para compatibilidade. O telefone é opcional e não aparece nas respostas nem na listagem de usuários.

`GET /api/ofertas` aceita os filtros opcionais `data`, `regiao`, `horario` (`HH:mm`, horário mínimo de saída) e `vagas` (mínimo de vagas disponíveis). A resposta usa campos do front-end como `driver`, `origin`, `destination`, `date`, `time`, `seats`, `ref`, `summary`, `match` e `status` (`open`, `full` ou `cancelled`). `POST /api/ofertas` recebe `driverId`, `origin`, `date`, `time`, `seats`, `ref` e `notes` opcional; se `destination` não for enviado, usa `Campus Senac Canoas`. `driverId` é obrigatório porque o backend não consegue inferir o usuário atual sem autenticação.

`POST /api/ofertas/{id}/solicitacoes` exige `passengerId` e aceita `msg` opcional com até 140 caracteres. A mensagem é salva na coluna anulável `mensagem VARCHAR(140)` da tabela `solicitacoes_carona`. O repositório ainda não inclui migrations versionadas; o `ddl-auto=update` do Hibernate cria ou atualiza o schema de desenvolvimento, mas não substitui um processo de migrations revisado para produção.

`GET /api/minhas-caronas?userId={id}` retorna o formato de snapshot do front-end, com `offers` e `requests`, estados em inglês e indicadores `mine`. O login verifica e-mail e senha, mas ainda não emite token nem protege as demais rotas; o ID enviado pelo cliente serve apenas como contexto e não comprova a identidade.

`PATCH /api/solicitacoes/{id}` recebe `{"userId": 7, "action": "accept"}` (também aceita `refuse` ou `cancel`). Somente o motorista pode aceitar ou recusar; o motorista ou o passageiro solicitante pode cancelar. A rota legada `/responder` exige `motoristaId`. Esses IDs são verificações de propriedade dentro da API, não autenticação.

`GET /api/caronas/{id}/solicitacoes?userId={id}` é restrita ao motorista da carona e retorna um DTO que não inclui o telefone do motorista. Outros IDs recebem HTTP 403. O ID informado não é validado por um sistema de autenticação.

O chat usa `GET /api/solicitacoes/{id}/conversa?userId={id}` e `POST /api/solicitacoes/{id}/conversa` com `{"userId": 7, "text": "..."}`. Somente o motorista e o passageiro de uma solicitação confirmada podem ler ou enviar mensagens; o texto pode ter até 500 caracteres. O JPA mapeia as mensagens e as referências a solicitação, remetente, carona e motorista, declarando as chaves estrangeiras e o índice em `(solicitacao_id, criado_em)`. Com a configuração de desenvolvimento `ddl-auto=update`, o Hibernate aplica esses mapeamentos ao iniciar.

Os erros da API usam o JSON `{"erro":"..."}`: entrada inválida retorna 400, acesso negado retorna 403, conflitos de estado ou dados retornam 409 e falhas de acesso ao banco retornam 503. Detalhes internos do banco não são enviados ao cliente.

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

No Windows, verifique se o serviço do PostgreSQL está em execução (por exemplo, no aplicativo **Serviços**) e crie o banco uma vez pelo terminal: `psql -U postgres -h localhost -p 5432 -c "CREATE DATABASE carona_solidaria;"`, usando a conta correta do PostgreSQL. Defina as variáveis de conexão no mesmo terminal em que iniciará a aplicação. Não faça commit de credenciais reais.

PowerShell:
```powershell
cd backend
$env:DB_URL = "jdbc:postgresql://localhost:5432/carona_solidaria"
$env:DB_USERNAME = "seu_usuario_postgres"
$env:DB_PASSWORD = "sua_senha_postgres"
$env:DB_DDL_AUTO = "update"
$env:DB_SHOW_SQL = "false"
.\mvnw.cmd spring-boot:run
```

Linux/macOS:
```bash
cd backend
export DB_URL="jdbc:postgresql://localhost:5432/carona_solidaria"
export DB_USERNAME="seu_usuario_postgres"
export DB_PASSWORD="sua_senha_postgres"
export DB_DDL_AUTO="update"
export DB_SHOW_SQL="false"
./mvnw spring-boot:run
```

A API ficará disponível em `http://localhost:8080`. `DB_DDL_AUTO` pode sobrescrever o modo de schema do Hibernate; o padrão atual é `update`. `DB_SHOW_SQL` controla a exibição de SQL e o padrão é `false`.

Os testes unitários usam repositories mockados e não exigem PostgreSQL ativo. Execute-os dentro de `backend/` com `.\mvnw.cmd test` no Windows ou `./mvnw test` no Linux/macOS. Esses testes não substituem um teste de integração conectado ao PostgreSQL.

### Front-end
Mantenha o backend em execução, abra `frontend/index.html` no VS Code e selecione **Open with Live Server**. Use a porta padrão `5500`; as origens `http://127.0.0.1:5500` e `http://localhost:5500` são permitidas pelo CORS do backend. Não abra a página como `file://`, pois essa origem não é permitida. O frontend chama `http://localhost:8080/api` e não usa dados mockados por padrão.

### Teste rápido ponta a ponta
1. Inicie o PostgreSQL e crie o banco vazio `carona_solidaria` (por exemplo, com `psql -U postgres -h localhost -c "CREATE DATABASE carona_solidaria;"`).
2. Inicie o backend com as variáveis de ambiente acima e aguarde o Spring Boot terminar a inicialização.
3. Abra o frontend pelo Live Server e cadastre um usuário Senac com e-mail institucional válido.
4. Publique uma oferta. Em outro perfil de navegador, cadastre um segundo usuário, busque a oferta e solicite uma vaga.
5. Volte ao perfil do motorista e aceite a solicitação; atualize a página do passageiro para ver a confirmação. Abra a conversa por qualquer um dos lados e envie uma mensagem.
6. Na tela **Minhas caronas** do motorista, teste cancelar a solicitação ou a oferta. O estado atualizado deve continuar após recarregar, pois fica armazenado no PostgreSQL.

O frontend guarda o ID do usuário selecionado no `localStorage` daquele navegador; use perfis separados para testar os dois usuários. Isso não é autenticação e não deve ser considerado uma proteção de segurança em produção.

## CI/CD

O repositório utiliza **GitHub Actions** (`.github/workflows/ci.yml`) em todo Pull Request e push na `main`. O job do backend executa compilação limpa pelo Maven e os testes unitários. O job do front-end verifica `index.html`, a sintaxe JavaScript e os testes do Node.js em `frontend/tests`. Esses testes não substituem um fluxo ponta a ponta no navegador nem um teste de integração conectado ao banco de dados.

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
