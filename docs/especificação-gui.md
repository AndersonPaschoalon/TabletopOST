# Plano Técnico de Implementação da GUI Swing

## Tabletop RPG Soundtrack

### Versão 2.0

## 1. Objetivo

Descrever a implementação da interface gráfica (GUI) da aplicação **Tabletop RPG Soundtrack** utilizando **Java Swing**, reutilizando integralmente a arquitetura já existente baseada em MVC.

Este documento tem como objetivo servir de guia para implementação da GUI, minimizando novas decisões arquiteturais durante o desenvolvimento.

A implementação deverá reutilizar integralmente:

* Model
* Controller
* Services
* Commands
* Regras de negócio
* Fluxo da aplicação

A única camada substituída será a **View**.

---

# 2. Princípios Arquiteturais

A GUI seguirá exatamente os mesmos princípios utilizados na implementação da CLI.

## A View não possui regra de negócio

A interface gráfica é responsável apenas por:

* apresentar informações ao usuário;
* capturar eventos de interface;
* converter esses eventos em comandos;
* encaminhar os comandos ao Controller.

Toda a lógica permanece nas camadas inferiores.

---

## Reaproveitamento integral do domínio

Não serão realizadas alterações em:

```
model/
controller/
service/
audio/
```

A implementação Swing será apenas uma nova View.

---

## Utilização do padrão Command

Cada interação do usuário será convertida em um objeto `Command`.

Fluxo:

```
Botão

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
```

A GUI nunca acessará diretamente serviços do domínio.

---

# 3. Arquitetura Geral

A arquitetura completa permanecerá:

```
               Swing

                 │

                 ▼

              Controller

                 │

                 ▼

              Services

                 │

                 ▼

                Model
```

Não existirão dependências da camada de domínio para a interface gráfica.

---

# 4. Estrutura da View

A implementação Swing será composta por apenas cinco classes.

```
view/
└── swing/
    ├── MainWindow.java
    ├── SelectionScreen.java
    ├── PlaybackScreen.java
    ├── ThemeMenu.java
    └── ImageViewer.java
```

Optou-se deliberadamente por uma estrutura enxuta, compatível com o pequeno escopo da aplicação.

Não serão criados subpacotes (`panels`, `widgets`, `components`, etc.), evitando fragmentação desnecessária.

---

# 5. Responsabilidade das Classes

## MainWindow

Única `JFrame` da aplicação.

Responsabilidades:

* criar a janela principal;
* configurar o `CardLayout`;
* registrar as telas;
* alternar entre elas;
* atualizar a interface após mudanças de estado.

Não possui qualquer regra de negócio.

---

## SelectionScreen

Tela exibida quando nenhuma OST está carregada.

Responsável por:

* listar OSTs disponíveis;
* permitir carregar uma OST;
* permitir sincronizar catálogo (quando implementado);
* encaminhar comandos ao Controller.

---

## PlaybackScreen

Tela principal da aplicação.

Responsável por:

* exibir informações da OST atual;
* exibir o tema atual;
* integrar os controles de reprodução;
* integrar o menu de temas;
* integrar o componente de imagens.

Toda a lógica permanece no Controller.

---

## ThemeMenu

Componente responsável pelo menu "hambúrguer".

Ao ser acionado:

* consulta a lista de temas disponíveis;
* apresenta um menu suspenso (`JPopupMenu`);
* dispara `SET_THEME` quando um item é selecionado.

---

## ImageViewer

Componente responsável pela apresentação das imagens do tema.

Suas responsabilidades são:

* solicitar ao Controller as imagens do tema atual;
* alternar automaticamente entre as imagens;
* reiniciar a apresentação quando o tema mudar.

Esta é a única classe da GUI que possui uma lógica própria de atualização periódica.

---

# 6. Organização das Telas

## Tela 1 — SelectionScreen

Apresentada quando nenhuma OST está selecionada.

Interface:

```
+--------------------------------------+

Tabletop RPG Soundtrack

----------------------------------------

[ Dungeons & Dragons ]

[ Lovecraft ]

[ Iron Kingdoms ]

----------------------------------------

(Sincronizar Catálogo)

+--------------------------------------+
```

Cada item representa uma OST disponível.

Ao clicar:

```
SET_OST <nome>
```

---

## Tela 2 — PlaybackScreen

Exibida quando existe uma OST carregada.

Organização:

```
+------------------------------------------------------+

☰          OST Atual             Tema Atual

--------------------------------------------------------

              ImageViewer

--------------------------------------------------------

Music

◀   ▶   ■   🔀

--------------------------------------------------------

Ambience

◀   ▶   ■   🔀

--------------------------------------------------------

◀ Theme        ▶/■        Theme ▶

+------------------------------------------------------+
```

A interface será propositalmente simples, utilizando componentes Swing padrão.

---

# 7. Controles de Reprodução

Serão implementados três grupos independentes de controles.

## Controle de Música

Comandos:

* Play Song
* Stop Song
* Next Song (placeholder)
* Shuffle / Linear (placeholder)

Todos os botões disparam comandos existentes no Controller.

---

## Controle de Ambiência

Mesmo funcionamento do controle de música.

Comandos:

* Play Ambience
* Stop Ambience
* Next Ambience (placeholder)
* Shuffle / Linear (placeholder)

---

## Controle Global

Responsável pelo controle simultâneo dos dois players.

Possui três botões:

```
◀ Theme

▶ / ■

Theme ▶
```

### Previous Theme

Dispara:

```
PREVIOUS_THEME
```

(placeholder)

---

### Play / Stop

Este botão mantém apenas um estado visual interno da GUI.

Primeiro clique:

```
PLAY_BOTH
```

Segundo clique:

```
STOP_BOTH
```

Terceiro clique:

```
PLAY_BOTH
```

...

A GUI não consulta o estado interno do PlaybackService.

Trata-se apenas de um mecanismo de alternância visual.

---

### Next Theme

Dispara:

```
NEXT_THEME
```

---

# 8. ImageViewer

O componente ImageViewer possui comportamento próprio.

Funcionamento:

```
Tema Atual

↓

GET_THEME_IMAGES

↓

Lista de imagens

↓

Exibe primeira imagem

↓

30 segundos

↓

Próxima imagem

↓

30 segundos

↓

...
```

Caso ocorra um comando `SET_THEME`, o componente:

1. interrompe o ciclo atual;
2. solicita novamente a lista de imagens;
3. reinicia a apresentação.

Essa lógica pertence exclusivamente à camada de apresentação.

---

# 9. Fluxo dos Comandos

Todo botão executa exatamente o mesmo fluxo da CLI.

```
Clique

↓

ActionListener

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
```

A GUI nunca acessa diretamente o domínio.

---

# 10. Troca de Telas

A troca de telas será realizada exclusivamente pelo `MainWindow`, utilizando `CardLayout`.

Critério:

```
currentOst == null

↓

SelectionScreen
```

Caso contrário:

```
PlaybackScreen
```

---

# 11. Atualização da Interface

Após qualquer comando que altere o estado da aplicação, a interface executará:

```
refreshScreen()
```

Responsabilidades:

* atualizar rótulos;
* atualizar tema;
* atualizar menu;
* atualizar imagens;
* executar `revalidate()`;
* executar `repaint()`.

Não serão utilizados mecanismos como Observer, EventBus ou PropertyChangeListener nesta primeira versão.

---

# 12. Dependências

A implementação utilizará exclusivamente bibliotecas padrão do Java.

Não serão adicionadas dependências externas para construção da GUI.

Todo o projeto continuará sendo compilado via Maven.

---

# 13. Sequência de Implementação

A implementação será realizada incrementalmente.

1. **MainWindow** — criação da janela principal e configuração do `CardLayout`.
2. **SelectionScreen** — implementação da tela de seleção de OST e integração com `SET_OST`.
3. **PlaybackScreen** — criação da tela principal, contendo os controles de reprodução e áreas de informação.
4. **ThemeMenu** — implementação do menu "hambúrguer" e seleção de temas.
5. **ImageViewer** — implementação do slideshow de imagens com atualização automática após mudança de tema.
6. **Integração final** — atualização automática das telas, testes de navegação e validação da interação completa com o Controller.

Cada etapa deverá resultar em uma aplicação compilável e funcional, permitindo validação incremental durante o desenvolvimento.
