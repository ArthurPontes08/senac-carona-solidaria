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

### 25/11/2026 — Implementação de validações e testes no back-end
- **Prompt principal:** "Leia a documentação do projeto e implemente melhorias no back-end para validação, consistência, testes e revisão."
- **O que a IA gerou:** Validações defensivas, operações sincronizadas de estado, tratamento HTTP centralizado de exceções, testes JUnit e atualização da documentação.
- **O que o time aproveitou:** Mantemos a arquitetura Java 17/Spring Boot e o escopo em memória até a integração com o banco.
- **O que o time corrigiu ou rejeitou, e por quê:** Mantivemos apenas código compilável localmente e removemos artefatos gerados em `target/`; persistência e autenticação continuam separadas para não misturar responsabilidades do banco e do frontend.

### 25/11/2026 — Fortalecimento do back-end para integração
- **Prompt principal:** "Continue as melhorias apontadas na revisão técnica e prepare a API para integração."
- **O que a IA gerou:** Validações de datas e horários passados, limites de campos e vagas, cancelamento da carona e das solicitações ativas, identificação opcional/obrigatória do motorista ao responder, exceções HTTP centralizadas e novos testes JUnit.
- **O que o time aproveitou:** Mantivemos as rotas REST e os modelos em memória, com extensões mínimas para integração.
- **O que o time corrigiu ou rejeitou, e por quê:** Autenticação completa foi adiada porque login no frontend e identidade no banco ainda não foram implementados. A identidade por perfil será integrada após esses módulos ficarem prontos.

<<<<<<< HEAD
### 25/11/2026 — Permitir origem livre nas caronas
- **Prompt principal:** "Permita que o usuário informe qualquer origem em vez de escolher apenas bairros de Canoas."
- **O que a IA gerou:** Campos de texto no cadastro, na oferta de carona e na busca, com correspondência parcial sem diferenciar maiúsculas e minúsculas na demonstração do front-end.
- **O que o time aproveitou:** O usuário pode informar bairro, cidade ou endereço sem depender de uma lista fixa.
- **O que o time corrigiu ou rejeitou, e por quê:** O front-end continua usando uma API simulada; a mudança não adiciona geocodificação nem validação do local.

### 25/11/2026 — Simulação de falha de conexão
- **Prompt principal:** "Corrija o controle Simular falha de conexão, que não fazia nada ao ser clicado."
- **O que a IA gerou:** Sincronização do estado da conexão, exibição do banner offline, recarga da tela ao ativar/restaurar a falha e fechamento do menu.
- **O que o time aproveitou:** A alternância existente de falha da API simulada e seu comportamento de erro de rede.
- **O que o time corrigiu ou rejeitou, e por quê:** O controle simula uma falha da API; ele não altera a conexão real do dispositivo.

### 25/11/2026 — Identidade visual Vai Junto
- **Prompt principal:** "Utilize a logo Vai Junto fornecida em todo o projeto."
- **O que a IA gerou:** Arquivo local da logo, marca na barra lateral, favicon, título da página e estilos de dimensionamento.
- **O que o time aproveitou:** A imagem fornecida e o contexto Senac existente nas informações das caronas.
- **O que o time corrigiu ou rejeitou, e por quê:** A imagem foi adicionada como recurso local, sem carregamento por serviço externo.

### 25/11/2026 — Chat básico para caronas confirmadas
- **Prompt principal:** "Após aceitar uma solicitação de carona, permitir que motorista e passageiro iniciem e troquem mensagens, independentemente de quem ofereceu a carona."
- **O que a IA gerou:** Interface de conversa e armazenamento de mensagens por solicitação na demonstração do front-end, com acesso para motorista e passageiro.
- **O que o time aproveitou:** A conversa só abre para solicitações confirmadas, cada solicitação tem sua própria conversa e o conteúdo das mensagens é escapado antes de ser renderizado.
- **O que o time corrigiu ou rejeitou, e por quê:** O front-end ainda usa uma API simulada local, então as mensagens não são compartilhadas entre usuários ou dispositivos diferentes; persistência no back-end e autenticação continuam pendentes de integração.

=======
>>>>>>> 7ff614e3ac232e0f9e4c6ca00c77065ed1f5e75a

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

