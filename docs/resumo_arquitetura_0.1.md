# Tabletop RPG Soundtrack

## Documento de Arquitetura de Software (Resumo)

**Versão 1.0**

---

# 1. Objetivo

Este documento descreve a arquitetura de software do projeto **Tabletop RPG Soundtrack**, apresentando a organização geral do código, as responsabilidades de cada módulo, o fluxo de execução da aplicação e as principais decisões arquiteturais adotadas durante o projeto.

Este documento complementa a Especificação Técnica do projeto, descrevendo **como** o sistema será implementado, enquanto a especificação técnica descreve **o que** o sistema deve fazer.

---

# 2. Organização Geral

O projeto será organizado em módulos independentes, seguindo os princípios da arquitetura MVC e o princípio da responsabilidade única.

A estrutura principal será composta pelos seguintes módulos:

```
Main
│
├── app
├── controller
├── service
├── model
├── audio
├── view
└── exception
```

Cada módulo possui responsabilidades bem definidas e dependências unidirecionais, reduzindo o acoplamento entre os componentes da aplicação.

---

# 3. Organização dos Módulos

## Main

Representa o ponto de entrada da aplicação.

Suas responsabilidades são:

* inicializar a aplicação;
* criar todos os componentes através do AppBootstrap;
* iniciar a interface do usuário;
* controlar o ciclo de vida da aplicação.

O fluxo principal da aplicação permanece centralizado nesta classe.

---

## app

O módulo **app** contém os componentes responsáveis por armazenar o estado global da aplicação, bem como componentes que devem permanecer ativos durante toda a execução da aplicação.
Ele não implementa regras de negócio.
É composto principalmente por:

* AppBootstrap;
* ApplicationContext.

O **AppBootstrap** realiza toda a injeção manual de dependências da aplicação.

O **ApplicationContext** armazena o estado global da aplicação e disponibiliza acesso aos serviços compartilhados.

Inicialmente o contexto armazenará:

* OST atualmente selecionada;
* Theme atualmente selecionado;
* referências para os serviços principais.

Futuramente poderá armazenar também:

* configurações da aplicação;
* estado dos players;
* informações de sessão.

---

## view

A View representa exclusivamente a interface com o usuário.

Sua única responsabilidade é converter ações do usuário em objetos **Command**.

A View nunca implementa regras de negócio.

Ela nunca acessa diretamente serviços da aplicação.

A implementação inicial será uma interface de linha de comando (CLI), mas a arquitetura foi projetada para permitir a futura substituição por uma interface Android sem qualquer alteração na lógica de negócio.

---

## controller

O Controller representa a camada responsável por interpretar comandos da aplicação.

Toda interação do usuário é convertida em um objeto **Command**.

O Controller recebe estes comandos, identifica o Handler correspondente e delega a execução ao serviço apropriado.

Cada operação do sistema é implementada por um Handler independente.

Exemplos:

* PlaySongHandler
* PauseSongHandler
* SetThemeHandler
* ListThemesHandler

Essa organização implementa o padrão **Command Pattern**, permitindo adicionar novos comandos sem modificar o restante da aplicação.

---

## service

O módulo Service concentra toda a lógica de negócio do sistema.

Ele é dividido em quatro submódulos independentes.

### catalog

Responsável por garantir que uma OST esteja disponível no cache local.

Em uma aplicação real, não é aceitável que todo o conteúdo de dados que serão consumidos pelos usuários venha imediatamente com o software assim que instalado. Primeiramente pois isso tornará o aplicativo muito pesado, e em segundo lugar porque o usuário gostaria de ter a opção de baixar novos temos conforme desejasse.

Tendo isso em vista, o CatalogService consulta um repositório de conteúdo (local ou remoto), verifica a disponibilidade da OST solicitada e sincroniza seus arquivos para o armazenamento local da aplicação. Ele será primeiramente implementado com um repositório local (a pasta `local_storage`, dentro do próprio projeto), mas poderá ser subistituido por um repositorio em nuvem assim que possível, sem alteração alguma no código.

O Catalog não interpreta arquivos nem conhece o modelo. Sua responsabilidade limita-se exclusivamente à sincronização dos recursos da fonte com o cache local.

---

### storage

Representa a camada responsável pela manipulação física de arquivos.

Essa camada conhece apenas operações de sistema de arquivos, como:

* Listar arquivos;
* Listar diretórios;
* Verificar a existência;
* Remover (limpar o cache);

O Storage não possui qualquer conhecimento sobre OSTs, Themes ou Manifestos.

---

### loader

Responsável por transformar os arquivos presentes no cache local em objetos do domínio da aplicação.
A partir do arquivo `manifest.json` e da estrutura de diretórios correspondente, o Loader constrói os objetos `Ost` e `Theme`.
Toda interpretação do formato dos arquivos está concentrada nesta camada.

---

### playback

Responsável pela reprodução de áudio.

O PlaybackService implementa toda a lógica relacionada à execução das playlists.

A seleção da próxima faixa é delegada para uma estratégia de reprodução (`PlaybackStrategy`), permitindo futuras estratégias sem modificar o restante do sistema.

A implementação inicial utilizará apenas reprodução aleatória (`RandomPlaybackStrategy`).

**IMPORTANTE**
É importante notar que a reprodução do audio deve ocorrer de forma não bloqueante as demais atividades que serão executadas no app. O Playback deverá continuar executando continuamente a playlist que lhe foi atribuida até que a aplicação seja fechada, ou que ele receba explicitamente um comando do tipo "pause". Portanto, será necessária a utilização de threads para que o processo permaneça sendo executado em background. Creio que a implementação será mais simples se fizermos esse gerenciamento na classe `AudioPlayer`. Mas é algo que devemos pensar.

---

## model

O Model representa exclusivamente os dados manipulados pela aplicação.

Na primeira versão o domínio será composto apenas por duas entidades:

### Ost

Representa um universo de RPG.

Contém:

* nome;
* lista de Themes.

### Theme

Representa um contexto narrativo.

Contém:

* nome;
* lista de trilhas sonoras;
* lista de sons ambientes;
* lista de imagens.

Os arquivos individuais serão representados inicialmente apenas como caminhos (`String`), simplificando a implementação da primeira versão.

---

## audio

Representa a camada responsável pela reprodução física de áudio.

Essa camada abstrai completamente a plataforma utilizada.

O acesso ocorre através da interface `AudioPlayer`.

A implementação utilizada será escolhida em tempo de execução pela `AudioPlayerFactory`.

Na primeira versão será implementado um `DummyAudioPlayer`, permitindo validar toda a arquitetura mesmo em ambientes Docker ou servidores sem suporte à reprodução de áudio.

No Android, esta implementação será substituída por uma implementação baseada na API nativa de reprodução de áudio.

A interface `AudioPlayer` desse componente deverá ser bastante simples, justamente para facilitar a sua implementação em diferentes tipos de devices.
Ele deve ser imaginado como um "simples toca fitas mecânico", com comandos básicos como `setAudio(String audioFile)`, `play()`, `stop()`. Deverá ser tomada aqui a decisão

---

## exception

Centraliza todas as exceções específicas da aplicação.

Permite separar erros de infraestrutura, erros de domínio e erros de utilização da interface.

---

# 4. Fluxo Geral da Aplicação

Toda interação da aplicação segue exatamente o mesmo fluxo.

```
Usuário

↓

View

↓

CommandParser

↓

Command

↓

CommandDispatcher

↓

CommandHandler

↓

Service

↓

Model

↓

View
```

Essa arquitetura garante que a interface permaneça completamente desacoplada da lógica de negócio.

---

# 5. Fluxo de Sincronização e Carregamento de uma OST

Quando o usuário seleciona uma OST, a aplicação executa duas etapas independentes.

Primeiramente ocorre a sincronização dos arquivos para o cache local.

```
Storage Remoto

↓

CatalogService

↓

Cache Local
```

Caso essa etapa já tenha sido feita antes, o CatalogService não deverá fazer nada. Após a sincronização, ocorre o carregamento do modelo.

```
Cache Local

↓

manifest.json

↓

OstLoader

↓

Model

↓

ApplicationContext
```

Essa separação permite modificar o mecanismo de armazenamento sem alterar a lógica responsável por interpretar os arquivos.

---

# 6. Fluxo de Execução dos Comandos

Todos os comandos da aplicação seguem exatamente o mesmo fluxo de execução.

Por exemplo:

```
play-song

↓

CommandParser

↓

Command

↓

CommandDispatcher

↓

PlaySongHandler

↓

PlaybackService

↓

PlaybackStrategy

↓

AudioPlayer

↓

DummyAudioPlayer
```

Outro exemplo:

```
set-theme dungeon

↓

CommandParser

↓

Command

↓

CommandDispatcher

↓

SetThemeHandler

↓

ApplicationContext
```

O Controller nunca interpreta texto.

Toda interpretação sintática ocorre exclusivamente na View.

# 7. Extensibilidade

A arquitetura foi projetada para permitir evolução incremental.

Novas funcionalidades poderão ser adicionadas sem modificações significativas na estrutura existente.

Entre as extensões previstas estão:

* novos repositórios de OSTs (GitHub, MinIO, S3, etc.);
* novos mecanismos de armazenamento;
* novas estratégias de reprodução;
* novos players de áudio;
* novas interfaces gráficas;
* novos comandos;
* novos temas e universos de RPG.

A separação de responsabilidades entre View, Controller, Service, Model e Audio garante que essas evoluções possam ocorrer preservando a arquitetura original e reutilizando integralmente a lógica de negócio implementada.
