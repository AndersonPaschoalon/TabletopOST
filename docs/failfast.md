# Testes Manuais — CLI (Tabletop RPG Soundtrack)

> Cobertura: comandos válidos, fail-fast de parâmetros, fail-fast de estado,
> erros de domínio, comandos desconhecidos, fluxo feliz completo e
> concorrência (logs de background vs. digitação).

---

## 1. Fail-fast de parâmetros

Testa se `requireParamCount(...)` rejeita a chamada **antes** de qualquer
efeito colateral (nenhum estado deve mudar quando o comando é rejeitado).

- [ ] `set_ost` (sem parâmetro) → erro pedindo 1 parâmetro
- [ ] `set_theme` (sem parâmetro) → erro pedindo 1 parâmetro
- [ ] `get_theme_image` (sem parâmetro) → erro pedindo 1 parâmetro
- [ ] `get_theme_image abc` (parâmetro não numérico) → erro "índice deve ser um número inteiro"
- [ ] `play_song extra_param` (parâmetro a mais) → erro pedindo 0 parâmetros
- [ ] `pause_both algumacoisa` (parâmetro a mais) → erro pedindo 0 parâmetros

## 2. Fail-fast de estado inválido (operação fora de ordem)

Testa se o sistema recusa operações quando o pré-requisito (OST/tema
selecionado) não foi cumprido.

- [ ] `list_themes` sem nenhuma OST selecionada → erro "Nenhuma OST selecionada"
- [ ] `set_theme <nome>` sem nenhuma OST selecionada → erro "Nenhuma OST selecionada"
- [ ] `play_song` sem tema selecionado → erro "Nenhum tema selecionado"
- [ ] `play_ambience` sem tema selecionado → erro "Nenhum tema selecionado"
- [ ] `play_both` sem tema selecionado → erro "Nenhum tema selecionado"
- [ ] `get_theme_images` sem tema selecionado → mensagem "Nenhum tema selecionado"
- [ ] `get_theme_image 0` sem tema selecionado → mensagem "Nenhum tema selecionado"

## 3. Erros de domínio (recurso inexistente)

- [ ] `set_ost ost_que_nao_existe` → erro informando que a OST não foi encontrada
- [ ] `set_ost dnd` → `set_theme tema_que_nao_existe` → erro de tema não encontrado
- [ ] `get_theme_image 999` (índice fora do range de imagens do tema) → erro de índice inválido
- [ ] `get_theme_image -1` (índice negativo) → erro de índice inválido

## 4. Comando desconhecido / entrada malformada

- [ ] `blablabla` → erro "Unknown command"
- [ ] linha vazia / só espaços → não deve fazer nada nem quebrar, apenas reexibir o prompt
- [ ] comando com hífen (`get-theme-image 0`) → deve ser normalizado e funcionar igual a `get_theme_image 0`

## 5. Concorrência (logs de background vs. digitação)

- [ ] `play_both` e, **enquanto o `DummyAudioPlayer` está logando**
      `[DummyAudioPlayer] Playing: ...`, digitar `PAUSE_BOTH` bem devagar,
      letra por letra
- [ ] Confirmar que o log aparece **acima** da linha do prompt, sem
      embaralhar o texto sendo digitado (sem repetir o bug
      `PAUSE_BO[DummyAudioPlayer] Playing... TH`)

## 6. Teste de cache/local_storage (camada adjacente, mas afeta a CLI)

- [ ] Apagar `cache/<ost>` e rodar `set_ost <ost>` de novo → deve
      reconstruir o cache automaticamente
- [ ] Apagar a pasta `cache/` inteira e rodar `set_ost <ost>` → **requer
      o fix `createDirectory` → `createDirectories` em
      `LocalCatalogService`**; sem o fix, quebra com `DownloadError`