# Tabletop RPG Soundtrack

## Documento de Especificação Técnica (Versão 3.0)

---

# 1. Objetivo

O **Tabletop RPG Soundtrack** é uma aplicação destinada a auxiliar mestres de RPG de mesa durante sessões de jogo, permitindo controlar de forma simples e eficiente duas trilhas de áudio independentes:

* uma trilha sonora (OST);
* um som ambiente.

O principal problema que a aplicação pretende resolver é a dificuldade de controlar simultaneamente múltiplas fontes de áudio utilizando players convencionais (YouTube, Spotify, VLC, etc.), especialmente em dispositivos móveis.

A aplicação permitirá ao mestre selecionar temas narrativos (por exemplo *Dungeon*, *Battle*, *Enchanted Forest*, *Tavern*) e utilizar esses temas tocar rapidamente músicas e sons ambientes adequadas à cena atual da aventura.

O projeto foi concebido para possuir um núcleo completamente independente da interface gráfica, permitindo reutilização integral da lógica de negócio entre diferentes plataformas.

---

# 2. Objetivos Técnicos

Os principais objetivos técnicos são:

* desenvolver toda a lógica utilizando Java puro;
* separar completamente regra de negócio e interface;
* aplicar arquitetura MVC;
* utilizar padrões clássicos de projeto;
* possibilitar reutilização do núcleo em diferentes plataformas;
* permitir evolução incremental da aplicação.

A filosofia adotada é:

> Desenvolver uma única implementação da regra de negócio, reutilizando-a integralmente em qualquer interface (CLI, Android ou futuras plataformas).

---

# 3. Estratégia de Desenvolvimento

O projeto será desenvolvido em duas etapas independentes.

## Etapa 1 — Proof of Concept (CLI)

A primeira etapa consiste na implementação completa do núcleo da aplicação utilizando exclusivamente uma interface de linha de comando.

Esta etapa deverá validar completamente:

* arquitetura do sistema;
* gerenciamento de estados;
* carregamento de OSTs;
* carregamento de Themes;
* reprodução de músicas;
* reprodução de sons ambientes;
* reprodução simultânea;
* comandos da aplicação;
* padrões de projeto.

Toda interação ocorrerá através de comandos de terminal.

O projeto deverá utilizar:

* Java
* Maven
* Docker (quando possível)

Ao final desta etapa toda a lógica do sistema estará concluída.

---

## Etapa 2 — Aplicação Android

A segunda etapa consiste apenas na implementação da interface Android.

Serão desenvolvidos exclusivamente:

* telas;
* integração com APIs Android;
* implementação do player de áudio Android.

Toda a lógica desenvolvida anteriormente deverá ser reutilizada sem modificações.

A expectativa é que a migração para Android seja praticamente uma substituição da camada de apresentação.

---

# 4. Arquitetura Geral

O projeto será dividido em módulos independentes.

## Core

Contém toda a regra de negócio. Não possui dependências de Android ou qualquer tipo de interface gráfica.
Este módulo representa o verdadeiro produto da aplicação.

---

## CLI

Responsável apenas pela interface de linha de comando.

Sua função é converter comandos digitados pelo usuário em comandos do Core.

Não deve possuir regra de negócio.

---

## Android

Responsável exclusivamente pela interface gráfica.

Toda lógica permanecerá localizada no módulo Core.

---

# 5. Conceitos Fundamentais

## OST

Representa um universo de RPG.

Exemplos:

* Medieval Fantasy
* Cosmic Horror
* Cyberpunk
* Post Apocalypse

Cada OST será distribuída independentemente.
Inicialmente será armazenada localmente.
Futuramente poderá ser hospedada em um repositório GitHub.
Cada OST possui diversos Themes.

Cada OST é descrita por um arquivo manifest.json, que representa a fonte de verdade da estrutura do conteúdo. O manifesto descreve os Themes disponíveis e os recursos associados a cada um deles. O sistema não interpreta diretamente a estrutura física dos diretórios.

---

## Theme

Representa uma situação narrativa.

Exemplos:

* Battle
* Dungeon
* Forest
* Tavern
* Night
* Castle

Cada Theme contém:

* lista de caminhos para músicas;
* lista de caminhos para sons ambientes;
* lista de caminhos para imagens.

Importante:
O Theme **não representa o conteúdo atualmente sendo reproduzido**.
Ele representa apenas o tema atualmente selecionado pelo usuário para utilização nos próximos comandos.

---

## Playlist

Representa uma coleção de faixas de áudio.
Existem dois tipos:

* Music Playlist
* Ambience Playlist
Inicialmente a reprodução será realizada utilizando estratégia aleatória.

---

# 6. Estado da Aplicação

O sistema possui dois tipos distintos de estado.

## Estado da Interface

Representa o contexto atual da aplicação.

Inclui:

* OST selecionada;
* Theme selecionado.

Esses valores apenas indicam qual conteúdo será utilizado pelos próximos comandos.

Alterar esse estado **não modifica automaticamente os players**.

---

## Estado dos Players

Cada player mantém seu próprio estado interno.

Cada player conhece:

* playlist carregada;
* faixa atual;
* estado de reprodução.

Esses estados são independentes do Theme atualmente selecionado.

Consequentemente, alterar o Theme não interrompe, substitui ou reinicia nenhuma reprodução em andamento.

---

# 7. Modelo de Reprodução

O sistema possui dois players completamente independentes.

## Music Player

Responsável exclusivamente pela reprodução da trilha sonora.

---

## Ambience Player

Responsável exclusivamente pela reprodução dos sons ambientes.

---

Cada player mantém:

* playlist própria;
* faixa atual;
* estado próprio.

Os estados possíveis são:

* Playing
* Paused
* Stopped

Os dois players podem funcionar simultaneamente.

Também podem possuir playlists pertencentes a Themes diferentes.

Exemplo:

Music Player

* Dungeon

Ambience Player

* Tavern

Essa independência permite que o mestre combine livremente músicas e ambientes distintos durante uma sessão.

---

# 8. Funcionamento dos Comandos

Os comandos que carregam conteúdo consultam o Theme atualmente selecionado.

Exemplo:

```
set-theme dungeon
```

Apenas altera o contexto da aplicação.

Nenhuma música é iniciada.

Nenhum player sofre alteração.

Posteriormente:

```
play-song
```

O comando consulta o Theme atual ("Dungeon"), carrega sua playlist de músicas e inicia a reprodução.

Se posteriormente o usuário executar:

```
set-theme tavern
```

A música de Dungeon continuará sendo reproduzida normalmente.

Somente quando executar:

```
play-ambience
```

o Ambience Player carregará o ambiente de Tavern.

Essa separação permite alterar continuamente o contexto da aplicação sem interferir nos players ativos.

---

# 9. Interface da Aplicação

Independentemente da plataforma utilizada, todas as interfaces deverão oferecer exatamente as mesmas funcionalidades.

A interface Android deverá possuir dois players independentes.

Player Superior

* música

Player Inferior

* ambiente

Cada player deverá possuir controles independentes.

Além disso, existirão controles globais.

Controles mínimos:

Music

* Play
* Pause

Ambience

* Play
* Pause

Global

* Play Both
* Pause Both

---

## 10. Repositório de Conteúdo

O acesso ao conteúdo será dividido em três responsabilidades independentes.

### StorageService

Responsável por acessar a origem dos arquivos.

A implementação inicial utilizará armazenamento local (`LocalStorage`).

Implementações futuras poderão utilizar:

* GitHub;
* armazenamento em nuvem;
* servidores HTTP;
* outras fontes.

O restante da aplicação nunca conhecerá diretamente a origem do conteúdo.

---

### CatalogService

Responsável por disponibilizar uma OST no armazenamento local da aplicação.

Na primeira versão, utilizando apenas `LocalStorage`, sua responsabilidade será localizar e disponibilizar as OSTs existentes.

Em versões futuras, o mesmo serviço será responsável por sincronizar conteúdos remotos para o cache local.

---

### OstLoader

Responsável por transformar uma OST armazenada localmente em objetos do domínio.

O `OstLoader` interpreta o `manifest.json`, valida sua estrutura e constrói os modelos `Ost` e `Theme` utilizados pelo restante da aplicação.

---

Essa seção, na minha opinião, ficou muito mais alinhada com a arquitetura.

---

# 11. Design Patterns

## MVC

Responsável pela separação entre:

* Model
* View
* Controller

Toda regra de negócio permanecerá concentrada no módulo Core, distribuída entre Models e Services. O Model representa exclusivamente os dados do domínio, enquanto os Services implementam os comportamentos da aplicação.

---

## Command Pattern

Toda ação executada pelo usuário será representada por um objeto Command.

Exemplos:

* SetThemeCommand
* PlaySongCommand
* PlayAmbienceCommand
* PauseBothCommand

A CLI e o Android executarão exatamente os mesmos comandos. Mudará apenas a origem do evento.

A interface de usuário é responsável apenas por converter ações do usuário em objetos `Command`. A execução desses comandos é realizada pelo `CommandDispatcher`, que delega cada operação a um `CommandHandler` especializado. Dessa forma, nenhuma interface conhece diretamente a lógica de negócio da aplicação.

---

## Strategy

Utilizado para definir estratégias de reprodução.

Primeira implementação:

* RandomPlaybackStrategy

Novas estratégias poderão ser adicionadas futuramente sem modificar o restante do sistema.

A estratégia de reprodução será abstraída através da interface `PlaybackStrategy`, permitindo adicionar novos algoritmos de seleção de faixas sem modificar o restante do sistema.

## Factory

Utilizado para selecionar automaticamente o mecanismo de reprodução adequado à plataforma.

Exemplos:

* AndroidAudioPlayer
* LinuxAudioPlayer

A escolha deverá ocorrer automaticamente durante a inicialização da aplicação.

---

## Singleton (não disponível na primeira versão)

Responsável pelo gerenciamento global das configurações da aplicação.

Também será utilizado para disponibilizar acesso único às preferências do usuário.

A primeira versão não irá armazenar configurações. Versões futuras poderão incluir configurações de volume e aparência do aplicativo (dark, light, etc..).

---

# 12. Interface de Linha de Comando

A primeira versão deverá implementar os seguintes comandos:

```
set-ost <ost>

unset-ost

list-themes

set-theme <theme>

unset-theme

play-song

play-ambience

play-both

pause-song

pause-ambience

pause-both

get-theme-image

get-theme-images

get-current-theme
```

Todos esses comandos deverão existir também na interface Android.

A diferença será apenas a forma de acionamento.

---

# 13. Escopo da Primeira Versão

A primeira versão deverá validar completamente:

* arquitetura MVC;
* arquitetura modular;
* reutilização do Core;
* reprodução simultânea;
* independência entre os dois players;
* independência entre o estado da interface e o estado dos players;
* carregamento de OSTs;
* carregamento de Themes;
* reprodução aleatória;
* Command Pattern;
* Strategy Pattern;
* Repository Pattern;
* Factory Pattern;
* armazenamento local;
* execução via CLI.

Não fazem parte da primeira versão:

* controle de volume;
* modo claro/escuro;
* download automático de repositórios GitHub;
* cache local de conteúdo remoto;
* sincronização online;
* gerenciamento avançado de playlists.

Essas funcionalidades poderão ser incorporadas futuramente sem necessidade de alterações significativas na arquitetura do sistema.

---

# 14. Princípios Arquiteturais

O projeto será guiado pelos seguintes princípios:

* Separação entre regra de negócio e interface.
* Independência entre plataforma e lógica da aplicação.
* Baixo acoplamento entre componentes.
* Uso extensivo de interfaces para permitir múltiplas implementações.
* Evolução incremental da aplicação.
* Reutilização máxima do código entre diferentes plataformas.
* Cada classe deve possuir uma única responsabilidade claramente definida (Single Responsibility Principle).

A interface gráfica nunca deverá conter regra de negócio.

Toda funcionalidade relevante deverá existir inicialmente no módulo Core e ser reutilizada pelas demais interfaces.
