# Plano Técnico de Implementação da GUI Swing

## Tabletop RPG Soundtrack

### Versão 2.1

---

# 1. Objetivo

Descrever a implementação da interface gráfica (GUI) da aplicação **Tabletop RPG Soundtrack** utilizando **Java Swing**, reutilizando integralmente a arquitetura MVC já existente.

Este documento servirá como guia oficial para implementação da camada de apresentação Desktop, sem necessidade de novas decisões arquiteturais durante o desenvolvimento.

A implementação reutilizará integralmente:

* Model
* Controller
* Services
* Audio
* Regras de negócio
* Fluxo da aplicação

A única camada substituída será a **View**.

---

# 2. Princípios Arquiteturais

## A View não possui regra de negócio

A interface gráfica possui somente quatro responsabilidades:

* apresentar informações ao usuário;
* capturar eventos da interface;
* converter eventos em objetos `Command`;
* encaminhar comandos ao Controller.

Toda a lógica permanece exclusivamente no domínio.

---

## Reaproveitamento integral do domínio

Não serão realizadas alterações em:

```
model/
controller/
service/
audio/
```

Toda a implementação Swing será construída sobre essas camadas já existentes.

---

## Comunicação através do Controller

Toda interação continuará seguindo exatamente o mesmo fluxo utilizado atualmente pela CLI.

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

A diferença em relação à primeira versão da CLI é que os handlers **não imprimem mais diretamente na interface**.

Agora toda consulta retorna um objeto de resposta estruturado.

Exemplos:

```
ListResponse

StringResponse

StatusResponse

ImageListResponse
```

A CLI converte esses objetos para texto através de `toDisplayString()`.

A GUI Swing utilizará diretamente os dados estruturados desses objetos.

Essa alteração elimina qualquer dependência entre Controller e tecnologia de interface.

---

# 3. Arquitetura Geral

A arquitetura permanece:

```
                 Swing

                   │

                   ▼

          CommandDispatcher

                   │

                   ▼

             CommandHandler

                   │

                   ▼

               Services

                   │

                   ▼

                 Model
```

As respostas retornam pelo mesmo caminho:

```
Model

↓

Service

↓

Response

↓

Swing
```

A View nunca acessa diretamente o domínio.

---

# 4. Estrutura da View

A implementação Swing permanecerá propositalmente pequena.

```
view/
└── swing/
    ├── MainWindow.java
    ├── SelectionScreen.java
    ├── PlaybackScreen.java
    ├── ThemeMenu.java
    └── ImageViewer.java
```

Não serão criados subpacotes (`components`, `widgets`, `panels`, etc.).

O projeto possui escopo reduzido e a simplicidade da estrutura facilita manutenção.

---

# 5. Responsabilidade das Classes

## MainWindow

Única `JFrame` da aplicação.

Responsável por:

* criar a janela;
* configurar o `CardLayout`;
* registrar as telas;
* alternar entre elas;
* atualizar a interface.

Não possui regra de negócio.

---

## SelectionScreen

Tela exibida quando nenhuma OST estiver carregada.

Responsabilidades:

* executar a Query `LIST_OSTS`;
* construir dinamicamente os botões das OSTs;
* disparar `SET_OST`;
* solicitar ao `MainWindow` a troca de tela.

Não acessa diretamente nenhum Service.

---

## PlaybackScreen

Tela principal da aplicação.

Responsável por:

* exibir OST atual;
* exibir tema atual;
* integrar controles de reprodução;
* integrar ThemeMenu;
* integrar ImageViewer.

Toda alteração ocorre através do `CommandDispatcher`.

---

## ThemeMenu

Responsável pelo menu "hambúrguer".

Fluxo:

```
Clique

↓

LIST_THEMES

↓

PopupMenu

↓

SET_THEME
```

---

## ImageViewer

Única classe da GUI com comportamento periódico.

Responsabilidades:

* executar `GET_THEME_IMAGES`;
* exibir imagens do tema;
* alternar automaticamente a cada 30 segundos;
* reiniciar quando o tema mudar.

Toda essa lógica pertence exclusivamente à camada de apresentação.

---

# 6. Organização das Telas

## Tela 1 — SelectionScreen

Exibida quando nenhuma OST estiver carregada.

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

Ao ser criada:

```
LIST_OSTS

↓

ListResponse

↓

Criação dinâmica dos botões
```

Ao clicar:

```
SET_OST <nome>
```

Caso a operação seja bem sucedida:

```
MainWindow

↓

showPlaybackScreen()
```

---

## Tela 2 — PlaybackScreen

```
+------------------------------------------------+

☰        OST Atual          Tema Atual

--------------------------------------------------

              ImageViewer

--------------------------------------------------

Music

◀   ▶   ■   🔀

--------------------------------------------------

Ambience

◀   ▶   ■   🔀

--------------------------------------------------

◀ Theme     ▶/■     Theme ▶

+------------------------------------------------+
```

A interface utilizará apenas componentes Swing padrão.

Não haverá preocupação estética nesta primeira versão.

---

# 7. Controles de Reprodução

Existem três grupos independentes.

## Música

Botões:

* Play Song
* Stop Song
* Previous Song
* Next Song
* Shuffle (placeholder)

Todos convertem-se em Commands.

---

## Ambiência

Mesmo comportamento.

Botões:

* Play Ambience
* Stop Ambience
* Previous Ambience
* Next Ambience
* Shuffle (placeholder)

---

## Controle Global

Possui três botões.

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

Mantém apenas estado visual interno da GUI.

Clique ímpar:

```
PLAY_BOTH
```

Clique par:

```
STOP_BOTH
```

Não consulta o estado interno do PlaybackService.

---

### Next Theme

Dispara:

```
NEXT_THEME
```

---

# 8. ImageViewer

Fluxo completo:

```
Tema Atual

↓

GET_THEME_IMAGES

↓

ImageListResponse

↓

Imagem 1

↓

30 segundos

↓

Imagem 2

↓

30 segundos

↓

...
```

Quando ocorrer:

```
SET_THEME

NEXT_THEME

PREVIOUS_THEME
```

o componente:

* interrompe o ciclo atual;
* executa novamente `GET_THEME_IMAGES`;
* reinicia o slideshow.

Não utiliza Observer, EventBus ou qualquer mecanismo semelhante.

---

# 9. Fluxo dos Comandos

Todos os botões seguem exatamente o mesmo fluxo.

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

↓

Response

↓

Swing
```

O Controller nunca conhece Swing.

---

# 10. Troca de Telas

A aplicação utiliza apenas uma `JFrame`.

A troca ocorre via `CardLayout`.

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

A decisão pertence exclusivamente ao `MainWindow`.

---

# 11. Atualização da Interface

Após qualquer ação:

```
Botão

↓

Command

↓

Controller

↓

Response

↓

Atualização dos componentes

↓

revalidate()

↓

repaint()
```

Cada componente atualiza apenas suas próprias informações.

Não haverá atualização contínua ("polling").

---

# 12. Dependências

A GUI utilizará exclusivamente bibliotecas presentes no JDK.

Não serão adicionadas dependências externas.

O projeto continuará sendo compilado normalmente via Maven.

---

# 13. Sequência de Implementação

A implementação ocorrerá incrementalmente.

## Etapa 1

Implementar `MainWindow`.

Critério:

* janela abre;
* CardLayout funcional.

---

## Etapa 2

Implementar `SelectionScreen`.

Critério:

* consulta `LIST_OSTS`;
* cria botões dinamicamente;
* executa `SET_OST`.

---

## Etapa 3

Implementar `PlaybackScreen`.

Critério:

* exibir informações básicas;
* controles funcionais;
* integração com Controller.

---

## Etapa 4

Implementar integração completa com `CommandDispatcher`.

Critério:

* todos os botões executam Commands;
* nenhuma regra de negócio na View.

---

## Etapa 5

Implementar `ThemeMenu`.

Critério:

* consulta `LIST_THEMES`;
* executa `SET_THEME`.

---

## Etapa 6

Implementar `ImageViewer`.

Critério:

* slideshow automático;
* atualização após mudança de tema.

---

## Etapa 7

Integração final.

Critério:

* troca automática de telas;
* atualização dos componentes;
* validação completa da GUI.

---

# 14. Considerações Arquiteturais

A evolução do projeto levou à consolidação de um contrato entre a camada de apresentação e o Controller baseado em **objetos de resposta**, em substituição ao retorno de textos simples.

Essa decisão torna o Controller completamente independente da tecnologia de interface utilizada, permitindo que a mesma camada de aplicação seja reutilizada por:

* CLI (atual);
* Swing (implementação em andamento);
* Android (implementação futura);
* qualquer outra interface que venha a ser desenvolvida.

Com essa arquitetura, a GUI passa a ser apenas uma nova implementação da View, mantendo o domínio íntegro e respeitando integralmente os princípios do padrão MVC e da separação de responsabilidades. A estrutura permanece simples, adequada ao porte do projeto, mas suficientemente flexível para suportar futuras interfaces sem alterações nas regras de negócio.
