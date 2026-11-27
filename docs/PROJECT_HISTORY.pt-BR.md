# Histórico do projeto e pull requests

[English](PROJECT_HISTORY.md) · **Português (Brasil)**

Este documento registra a linha do tempo do repositório e todos os pull requests
encontrados no GitHub durante a revisão da documentação em **27 de novembro de
2026**. O histórico recuperado da branch padrão do GitHub contém 99 commits, de
24 a 27 de novembro de 2026. O último commit era `5433bc6` (merge do PR #37); os
PRs #38–#40 também foram incorporados a essa branch.

## Linha do tempo

- **24 de novembro — Fundação do repositório:** estrutura inicial, backend com
  Maven e Spring Boot, estrutura do frontend, workflow de CI e primeiros
  documentos do projeto.
- **25 de novembro — Fluxos principais de carona:** ofertas e solicitações,
  recomendações, validações no backend e testes automatizados dos serviços.
- **26 de novembro — Evolução do produto e persistência:** modais do frontend e
  chat para caronas confirmadas; configuração PostgreSQL e persistência JPA;
  contratos de usuário, oferta, solicitação e chat; verificações de propriedade,
  restrições de CORS, erros padronizados e remoção do fluxo legado de reservas.
  Vários PRs de documentação registraram esses contratos.
- **27 de novembro — Integração e ajustes operacionais:** correções nos
  relacionamentos JPA, cancelamento de ofertas na API e no frontend, instruções
  de configuração e integração local e esclarecimento da validação de e-mail
  Senac.

## Pull requests

Todos os 40 pull requests retornados pela consulta do repositório foram
integrados. Os títulos abaixo permanecem como publicados originalmente no
GitHub; cada número leva ao PR de origem.

| PR | Merge | Título |
|---:|:---:|---|
| [#1](https://github.com/ArthurPontes08/senac-carona-solidaria/pull/1) | 2026-11-24 | chore(repo): move official docs templates to root and clean placeholders |
| [#2](https://github.com/ArthurPontes08/senac-carona-solidaria/pull/2) | 2026-11-24 | chore: configura estrutura maven e spring boot no backend |
| [#3](https://github.com/ArthurPontes08/senac-carona-solidaria/pull/3) | 2026-11-24 | Feat/estrutura carona |
| [#4](https://github.com/ArthurPontes08/senac-carona-solidaria/pull/4) | 2026-11-24 | Chore/estrutura frontend ci |
| [#5](https://github.com/ArthurPontes08/senac-carona-solidaria/pull/5) | 2026-11-24 | Docs/documentacao inicial |
| [#6](https://github.com/ArthurPontes08/senac-carona-solidaria/pull/6) | 2026-11-25 | feat(backend): implementa fluxo de caronas, solicitacoes e recomendacao |
| [#7](https://github.com/ArthurPontes08/senac-carona-solidaria/pull/7) | 2026-11-25 | feat(backend): add validations and request tests |
| [#8](https://github.com/ArthurPontes08/senac-carona-solidaria/pull/8) | 2026-11-26 | Feat/frontend modais |
| [#9](https://github.com/ArthurPontes08/senac-carona-solidaria/pull/9) | 2026-11-26 | fix: fixed MR conflicts |
| [#10](https://github.com/ArthurPontes08/senac-carona-solidaria/pull/10) | 2026-11-26 | Feat(chat): Add chat option between driver and passenger |
| [#11](https://github.com/ArthurPontes08/senac-carona-solidaria/pull/11) | 2026-11-26 | Docs/backend db setup |
| [#12](https://github.com/ArthurPontes08/senac-carona-solidaria/pull/12) | 2026-11-26 | chore(backend): externalize PostgreSQL configuration |
| [#13](https://github.com/ArthurPontes08/senac-carona-solidaria/pull/13) | 2026-11-26 | Feat/backend jpa solicitacao repository |
| [#14](https://github.com/ArthurPontes08/senac-carona-solidaria/pull/14) | 2026-11-26 | docs(backend): document ride request repository |
| [#15](https://github.com/ArthurPontes08/senac-carona-solidaria/pull/15) | 2026-11-26 | feat(backend): persist ride workflows with JPA |
| [#16](https://github.com/ArthurPontes08/senac-carona-solidaria/pull/16) | 2026-11-26 | fix(backend): allow optional phone on registration |
| [#17](https://github.com/ArthurPontes08/senac-carona-solidaria/pull/17) | 2026-11-26 | feat(api): align user registration contract |
| [#18](https://github.com/ArthurPontes08/senac-carona-solidaria/pull/18) | 2026-11-26 | feat(api): add ride offer contract and filters |
| [#19](https://github.com/ArthurPontes08/senac-carona-solidaria/pull/19) | 2026-11-26 | feat(api): persist ride request messages |
| [#20](https://github.com/ArthurPontes08/senac-carona-solidaria/pull/20) | 2026-11-26 | feat(api): add user ride snapshot contract |
| [#21](https://github.com/ArthurPontes08/senac-carona-solidaria/pull/21) | 2026-11-26 | fix(api): enforce request action ownership |
| [#22](https://github.com/ArthurPontes08/senac-carona-solidaria/pull/22) | 2026-11-26 | feat(chat): persist ride conversation messages |
| [#23](https://github.com/ArthurPontes08/senac-carona-solidaria/pull/23) | 2026-11-26 | fix(api): restrict CORS to configured origins |
| [#24](https://github.com/ArthurPontes08/senac-carona-solidaria/pull/24) | 2026-11-26 | fix(api): restrict ride request listing to driver |
| [#25](https://github.com/ArthurPontes08/senac-carona-solidaria/pull/25) | 2026-11-26 | fix(api): standardize validation and database errors |
| [#26](https://github.com/ArthurPontes08/senac-carona-solidaria/pull/26) | 2026-11-26 | refactor: remove legacy reservation flow |
| [#27](https://github.com/ArthurPontes08/senac-carona-solidaria/pull/27) | 2026-11-26 | feat: implements chat between passenger and driver |
| [#28](https://github.com/ArthurPontes08/senac-carona-solidaria/pull/28) | 2026-11-26 | chore: remove incorrect folder |
| [#29](https://github.com/ArthurPontes08/senac-carona-solidaria/pull/29) | 2026-11-26 | Feat/backend jpa solicitacao repository |
| [#30](https://github.com/ArthurPontes08/senac-carona-solidaria/pull/30) | 2026-11-26 | Docs/backend solicitacao repository |
| [#31](https://github.com/ArthurPontes08/senac-carona-solidaria/pull/31) | 2026-11-26 | Docs/backend jpa core flows |
| [#32](https://github.com/ArthurPontes08/senac-carona-solidaria/pull/32) | 2026-11-26 | Docs/backend registration phone optional |
| [#33](https://github.com/ArthurPontes08/senac-carona-solidaria/pull/33) | 2026-11-26 | Docs/backend user registration contract |
| [#34](https://github.com/ArthurPontes08/senac-carona-solidaria/pull/34) | 2026-11-26 | Docs/backend offer api contract |
| [#35](https://github.com/ArthurPontes08/senac-carona-solidaria/pull/35) | 2026-11-26 | Docs/backend request message api |
| [#36](https://github.com/ArthurPontes08/senac-carona-solidaria/pull/36) | 2026-11-26 | Feat/chat confirmed |
| [#37](https://github.com/ArthurPontes08/senac-carona-solidaria/pull/37) | 2026-11-27 | Fix/backend jpa integration |
| [#38](https://github.com/ArthurPontes08/senac-carona-solidaria/pull/38) | 2026-11-27 | feat(frontend): add ride offer cancellation |
| [#39](https://github.com/ArthurPontes08/senac-carona-solidaria/pull/39) | 2026-11-27 | Docs/mvp setup and integration |
| [#40](https://github.com/ArthurPontes08/senac-carona-solidaria/pull/40) | 2026-11-27 | fix(frontend): clarify signup email validation |

