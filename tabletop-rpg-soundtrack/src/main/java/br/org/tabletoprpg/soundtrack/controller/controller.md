CommandDispatcher.java
```
package br.org.tabletoprpg.soundtrack.controller;

import java.util.HashMap;
import java.util.Map;

import br.org.tabletoprpg.soundtrack.controller.command.NextAmbienceHandler;
import br.org.tabletoprpg.soundtrack.controller.command.NextSongHandler;
import br.org.tabletoprpg.soundtrack.controller.command.PauseAmbienceHandler;
import br.org.tabletoprpg.soundtrack.controller.command.PauseBothHandler;
import br.org.tabletoprpg.soundtrack.controller.command.PauseSongHandler;
import br.org.tabletoprpg.soundtrack.controller.command.PlayAmbienceHandler;
import br.org.tabletoprpg.soundtrack.controller.command.PlayBothHandler;
import br.org.tabletoprpg.soundtrack.controller.command.PlaySongHandler;
import br.org.tabletoprpg.soundtrack.controller.command.PreviousAmbienceHandler;
import br.org.tabletoprpg.soundtrack.controller.command.PreviousSongHandler;
import br.org.tabletoprpg.soundtrack.controller.command.SetOstHandler;
import br.org.tabletoprpg.soundtrack.controller.command.SetThemeHandler;
import br.org.tabletoprpg.soundtrack.controller.command.UnsetOstHandler;
import br.org.tabletoprpg.soundtrack.controller.command.UnsetThemeHandler;
import br.org.tabletoprpg.soundtrack.controller.query.GetCurrentThemeHandler;
import br.org.tabletoprpg.soundtrack.controller.query.GetThemeImageHandler;
import br.org.tabletoprpg.soundtrack.controller.query.GetThemeImagesHandler;
import br.org.tabletoprpg.soundtrack.controller.query.ListOstsHandler;
import br.org.tabletoprpg.soundtrack.controller.query.ListThemesHandler;
import br.org.tabletoprpg.soundtrack.controller.query.StatusHandler;
import br.org.tabletoprpg.soundtrack.controller.result.Result;
import br.org.tabletoprpg.soundtrack.service.session.SessionService;

public class CommandDispatcher {

    private final Map<String, CommandHandler> handlers;

    public CommandDispatcher(SessionService sessionService) {
        this.handlers = createHandlers(sessionService);
    }

    private Map<String, CommandHandler> createHandlers(SessionService sessionService) {

        Map<String, CommandHandler> h = new HashMap<>();

        h.put(SetOstHandler.COMMAND_NAME, new SetOstHandler(sessionService));
        h.put(UnsetOstHandler.COMMAND_NAME, new UnsetOstHandler(sessionService));
        h.put(ListOstsHandler.COMMAND_NAME, new ListOstsHandler(sessionService));

        h.put(SetThemeHandler.COMMAND_NAME, new SetThemeHandler(sessionService));
        h.put(UnsetThemeHandler.COMMAND_NAME, new UnsetThemeHandler(sessionService));

        h.put(PlaySongHandler.COMMAND_NAME, new PlaySongHandler(sessionService));
        h.put(PauseSongHandler.COMMAND_NAME, new PauseSongHandler(sessionService));
        h.put(NextSongHandler.COMMAND_NAME, new NextSongHandler(sessionService));
        h.put(PreviousSongHandler.COMMAND_NAME, new PreviousSongHandler(sessionService));

        h.put(PlayAmbienceHandler.COMMAND_NAME, new PlayAmbienceHandler(sessionService));
        h.put(PauseAmbienceHandler.COMMAND_NAME, new PauseAmbienceHandler(sessionService));
        h.put(NextAmbienceHandler.COMMAND_NAME, new NextAmbienceHandler(sessionService));
        h.put(PreviousAmbienceHandler.COMMAND_NAME, new PreviousAmbienceHandler(sessionService));

        h.put(PlayBothHandler.COMMAND_NAME, new PlayBothHandler(sessionService));
        h.put(PauseBothHandler.COMMAND_NAME, new PauseBothHandler(sessionService));

        h.put(ListThemesHandler.COMMAND_NAME, new ListThemesHandler(sessionService));
        h.put(GetCurrentThemeHandler.COMMAND_NAME, new GetCurrentThemeHandler(sessionService));
        h.put(GetThemeImagesHandler.COMMAND_NAME, new GetThemeImagesHandler(sessionService));
        h.put(GetThemeImageHandler.COMMAND_NAME, new GetThemeImageHandler(sessionService));

        h.put(StatusHandler.COMMAND_NAME, new StatusHandler(sessionService));

        return h;
    }

    public Result dispatch(Command command) {

        CommandHandler handler = handlers.get(command.getAction());

        if (handler == null) {
            throw new RuntimeException("Unknown command: " + command.getAction());
        }

        return handler.handle(command);
    }
}

```

Command.java
```
package br.org.tabletoprpg.soundtrack.controller;

public class Command {

    private final String action;
    private final String[] parameters;

    public Command(String action, String... parameters) {

        this.action = action.toUpperCase();
        this.parameters = parameters;
    }

    public String getAction() {
        return this.action;
    }

    public String[] getParameters() {
        return this.parameters;
    }
}
```

CommandHandler.java
```
package br.org.tabletoprpg.soundtrack.controller;

import java.util.Objects;

import br.org.tabletoprpg.soundtrack.controller.result.Result;

public interface CommandHandler {

    Result handle(Command command);

    default void requireParamCount(
            String[] params,
            int expected,
            String commandName) {

        if (params == null) {
            params = new String[0];
        }

        if (params.length != expected) {
            throw new RuntimeException(
                    String.format(
                            "Command '%s' expects %d parameter(s), but received %d.",
                            commandName,
                            expected,
                            params.length));
        }
    }

    default String extractString(
            String[] params,
            int index,
            String parameterName) {

        Objects.requireNonNull(params);

        try {

            String value = params[index];

            if (value == null || value.isBlank()) {
                throw new RuntimeException(
                        "Missing parameter: " + parameterName);
            }

            return value;

        } catch (ArrayIndexOutOfBoundsException ex) {

            throw new RuntimeException(
                    "Missing parameter: " + parameterName);
        }
    }

}
```

result/EmptyResult.java
```
package br.org.tabletoprpg.soundtrack.controller.result;

/**
 * Resultado utilizado quando um comando não possui informações para retornar.
 */
public final class EmptyResult implements Result {

    public static final EmptyResult INSTANCE = new EmptyResult();

    private EmptyResult() {
    }

    @Override
    public String asText() {
        return "";
    }

}
```

result/Result.java
```
package br.org.tabletoprpg.soundtrack.controller.result;

/**
 * Resultado produzido por uma operação do Controller.
 *
 * Todo resultado deve ser capaz de produzir uma representação textual,
 * permitindo que a CLI simplesmente imprima o retorno recebido.
 *
 * Interfaces gráficas podem utilizar o tipo concreto para acessar
 * informações estruturadas.
 */
public interface Result {

    /**
     * Retorna uma representação textual do resultado.
     */
    String asText();

}
```

result/StatusResult.java
```
package br.org.tabletoprpg.soundtrack.controller.result;

/**
 * Estado atual da aplicação.
 */
public record StatusResult(

        String currentOst,

        String currentTheme,

        boolean songPlaying,

        boolean ambiencePlaying)

        implements Result {

    @Override
    public String asText() {

        return String.format(
                """
                        OST       : %s
                        Theme     : %s
                        Song      : %s
                        Ambience  : %s
                        """,
                currentOst,
                currentTheme,
                songPlaying ? "Playing" : "Stopped",
                ambiencePlaying ? "Playing" : "Stopped");
    }

}
```

result/StringListResult.java
```
package br.org.tabletoprpg.soundtrack.controller.result;

import java.util.Collection;
import java.util.List;
import java.util.StringJoiner;

/**
 * Resultado contendo uma lista de Strings.
 */
public record StringListResult(
        Collection<String> values)
        implements Result {

    @Override
    public String asText() {

        StringJoiner joiner = new StringJoiner(System.lineSeparator());

        for (String value : values) {
            joiner.add(value);
        }

        return joiner.toString();
    }

}
```

result/StringResult.java
```
package br.org.tabletoprpg.soundtrack.controller.result;

import java.util.Objects;

/**
 * Resultado contendo apenas uma String.
 */
public record StringResult(String value) implements Result {

    public StringResult {

        Objects.requireNonNull(value);

    }

    @Override
    public String asText() {
        return value;
    }

}
```

query/GetThemeImageHandler.java
```
package br.org.tabletoprpg.soundtrack.controller.query;

import java.util.List;

import javax.naming.spi.DirStateFactory.Result;

import br.org.tabletoprpg.soundtrack.controller.Command;

import br.org.tabletoprpg.soundtrack.controller.CommandHandler;
import br.org.tabletoprpg.soundtrack.controller.result.StringResult;
import br.org.tabletoprpg.soundtrack.model.Theme;
import br.org.tabletoprpg.soundtrack.view.cli.ConsolePrompt;
import br.org.tabletoprpg.soundtrack.service.session.SessionService;

public class GetThemeImageHandler implements CommandHandler {

    public static final String COMMAND_NAME = "GET_THEME_IMAGE";

    private final SessionService sessionService;

    public GetThemeImageHandler(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @Override
    public StringResult handle(Command command) {
        requireParamCount(command.getParameters(), 1, COMMAND_NAME);

        String indexParam = extractString(command.getParameters(), 0, "index");

        int index;
        try {
            index = Integer.parseInt(indexParam);
        } catch (NumberFormatException ex) {
            throw new RuntimeException("O parâmetro 'index' deve ser um número inteiro.");
        }

        Theme theme = this.sessionService.getCurrentTheme();

        if (theme == null) {
            ConsolePrompt.println("Nenhum tema selecionado.");
            return null;
        }

        List<String> images = List.copyOf(theme.images());

        if (index < 0 || index >= images.size()) {
            throw new RuntimeException(
                    "Índice inválido. O tema '" + theme.name() + "' possui " + images.size() + " imagem(ns).");
        }
        String image = images.get(index);
        return new StringResult(image);
    }
}

```

query/GetCurrentThemeHandler.java
```
package br.org.tabletoprpg.soundtrack.controller.query;

import br.org.tabletoprpg.soundtrack.controller.Command;
import br.org.tabletoprpg.soundtrack.controller.CommandHandler;
import br.org.tabletoprpg.soundtrack.controller.result.StringResult;
import br.org.tabletoprpg.soundtrack.service.session.SessionService;

public class GetCurrentThemeHandler implements CommandHandler {

    public static final String COMMAND_NAME = "GET_CURRENT_THEME";

    private final SessionService sessionService;

    public GetCurrentThemeHandler(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @Override
    public StringResult handle(Command command) {
        requireParamCount(command.getParameters(), 0, COMMAND_NAME);

        String themeName = this.sessionService.getCurrentThemeName();

        if (themeName == null) {
            return new StringResult("");
        }

        return new StringResult(themeName);
    }
}

```

query/StatusHandler.java
```
package br.org.tabletoprpg.soundtrack.controller.query;

import br.org.tabletoprpg.soundtrack.controller.Command;
import br.org.tabletoprpg.soundtrack.controller.CommandHandler;
import br.org.tabletoprpg.soundtrack.controller.result.StatusResult;
import br.org.tabletoprpg.soundtrack.view.cli.ConsolePrompt;
import br.org.tabletoprpg.soundtrack.service.session.SessionService;

public class StatusHandler implements CommandHandler {

    public static final String COMMAND_NAME = "STATUS";

    private final SessionService sessionService;

    public StatusHandler(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @Override
    public StatusResult handle(Command command) {
        requireParamCount(command.getParameters(), 0, COMMAND_NAME);

        // ConsolePrompt.println("\u001B[1m--- Status ---\u001B[0m");
        // String ost = this.sessionService.getCurrentOstName();
        // String theme = this.sessionService.getCurrentThemeName();
        // ConsolePrompt.println("OST atual: " + (ost == null ? "(nenhuma)" : ost));
        // ConsolePrompt.println("Tema atual: " + (theme == null ? "(nenhum)" : theme));
        // ConsolePrompt.println("Música: " + (this.sessionService.isSongPlaying() ? "▶
        // tocando" : "▌▌ pausada"));
        // ConsolePrompt
        // .println("Ambiente: " + (this.sessionService.isAmbiencePlaying() ? "▶
        // tocando" : "▌▌ pausado"));

        String ost = this.sessionService.getCurrentOstName();
        String theme = this.sessionService.getCurrentThemeName();
        StatusResult r = new StatusResult(ost, theme, this.sessionService.isSongPlaying(),
                this.sessionService.isAmbiencePlaying());
        return r;
    }
}

```

query/ListOstsHandler.java
```
package br.org.tabletoprpg.soundtrack.controller.query;

import java.util.ArrayList;
import java.util.Collection;

import br.org.tabletoprpg.soundtrack.controller.Command;
import br.org.tabletoprpg.soundtrack.controller.CommandHandler;
import br.org.tabletoprpg.soundtrack.controller.result.StringListResult;
import br.org.tabletoprpg.soundtrack.view.cli.ConsolePrompt;
import br.org.tabletoprpg.soundtrack.service.session.SessionService;

/**
 * Lista as OSTs disponíveis no catálogo (local_storage).
 */
public class ListOstsHandler implements CommandHandler {

    public static final String COMMAND_NAME = "LIST_OSTS";

    private final SessionService sessionService;

    public ListOstsHandler(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @Override
    public StringListResult handle(Command command) {
        requireParamCount(command.getParameters(), 0, COMMAND_NAME);

        var osts = this.sessionService.getListOfOsts();
        return new StringListResult(osts);
    }
}

```

query/GetThemeImagesHandler.java
```
package br.org.tabletoprpg.soundtrack.controller.query;

import java.util.ArrayList;
import java.util.Collection;

import br.org.tabletoprpg.soundtrack.controller.Command;
import br.org.tabletoprpg.soundtrack.controller.CommandHandler;
import br.org.tabletoprpg.soundtrack.controller.result.StringListResult;
import br.org.tabletoprpg.soundtrack.model.Theme;
import br.org.tabletoprpg.soundtrack.view.cli.ConsolePrompt;
import br.org.tabletoprpg.soundtrack.service.session.SessionService;

public class GetThemeImagesHandler implements CommandHandler {

    public static final String COMMAND_NAME = "GET_THEME_IMAGES";

    private final SessionService sessionService;

    public GetThemeImagesHandler(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @Override
    public StringListResult handle(Command command) {
        requireParamCount(command.getParameters(), 0, COMMAND_NAME);

        Theme theme = this.sessionService.getCurrentTheme();
        Collection<String> allImages = new ArrayList<String>();

        if (theme == null) {
            return new StringListResult(allImages);
        }

        allImages.addAll(theme.images());
        StringListResult result = new StringListResult(allImages);
        return result;
    }
}

```

query/ListThemesHandler.java
```
package br.org.tabletoprpg.soundtrack.controller.query;

import br.org.tabletoprpg.soundtrack.controller.Command;
import br.org.tabletoprpg.soundtrack.controller.CommandHandler;
import br.org.tabletoprpg.soundtrack.controller.result.StringListResult;
import br.org.tabletoprpg.soundtrack.view.cli.ConsolePrompt;
import br.org.tabletoprpg.soundtrack.service.session.SessionService;

public class ListThemesHandler implements CommandHandler {

    public static final String COMMAND_NAME = "LIST_THEMES";

    private final SessionService sessionService;

    public ListThemesHandler(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @Override
    public StringListResult handle(Command command) {
        requireParamCount(command.getParameters(), 0, COMMAND_NAME);

        var themes = this.sessionService.getListOfThemes();
        return new StringListResult(themes);
    }
}

```

command/UnsetThemeHandler.java
```
package br.org.tabletoprpg.soundtrack.controller.command;

import br.org.tabletoprpg.soundtrack.controller.Command;
import br.org.tabletoprpg.soundtrack.controller.CommandHandler;
import br.org.tabletoprpg.soundtrack.controller.result.StringResult;
import br.org.tabletoprpg.soundtrack.service.session.SessionService;

public class UnsetThemeHandler implements CommandHandler {

    public static final String COMMAND_NAME = "UNSET_THEME";

    private final SessionService sessionService;

    public UnsetThemeHandler(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @Override
    public StringResult handle(Command command) {
        requireParamCount(command.getParameters(), 0, COMMAND_NAME);
        this.sessionService.unsetTheme();
        return new StringResult("Tema resetado para o padrão: '" + this.sessionService.getCurrentThemeName() + "'.");
    }
}

```

command/PlayAmbienceHandler.java
```
package br.org.tabletoprpg.soundtrack.controller.command;

import br.org.tabletoprpg.soundtrack.controller.Command;
import br.org.tabletoprpg.soundtrack.controller.CommandHandler;
import br.org.tabletoprpg.soundtrack.controller.result.StringResult;
import br.org.tabletoprpg.soundtrack.service.session.SessionService;

public class PlayAmbienceHandler implements CommandHandler {

    public static final String COMMAND_NAME = "PLAY_AMBIENCE";

    private final SessionService sessionService;

    public PlayAmbienceHandler(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @Override
    public StringResult handle(Command command) {
        requireParamCount(command.getParameters(), 0, COMMAND_NAME);
        this.sessionService.playAmbience();
        return new StringResult("▶ Tocando som ambiente do tema '" + this.sessionService.getCurrentThemeName() + "'.");
    }
}

```

command/UnsetOstHandler.java
```
package br.org.tabletoprpg.soundtrack.controller.command;

import br.org.tabletoprpg.soundtrack.controller.Command;
import br.org.tabletoprpg.soundtrack.controller.CommandHandler;
import br.org.tabletoprpg.soundtrack.controller.result.StringResult;
import br.org.tabletoprpg.soundtrack.service.session.SessionService;

public class UnsetOstHandler implements CommandHandler {

    public static final String COMMAND_NAME = "UNSET_OST";

    private final SessionService sessionService;

    public UnsetOstHandler(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @Override
    public StringResult handle(Command command) {
        requireParamCount(command.getParameters(), 0, COMMAND_NAME);
        this.sessionService.unsetOst();
        return new StringResult("OST desselecionada. Reprodução interrompida.");
    }
}

```

command/SetThemeHandler.java
```
package br.org.tabletoprpg.soundtrack.controller.command;

import br.org.tabletoprpg.soundtrack.controller.Command;
import br.org.tabletoprpg.soundtrack.controller.CommandHandler;
import br.org.tabletoprpg.soundtrack.controller.result.StringResult;
import br.org.tabletoprpg.soundtrack.service.session.SessionService;

public class SetThemeHandler implements CommandHandler {

    public static final String COMMAND_NAME = "SET_THEME";

    private final SessionService sessionService;

    public SetThemeHandler(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @Override
    public StringResult handle(Command command) {
        requireParamCount(command.getParameters(), 1, COMMAND_NAME);
        String themeName = extractString(command.getParameters(), 0, "theme");

        this.sessionService.setCurrentTheme(themeName);

        return new StringResult("Tema '" + themeName + "' selecionado (OST: '"
                + this.sessionService.getCurrentOstName() + "').");
    }
}

```

command/PauseSongHandler.java
```
package br.org.tabletoprpg.soundtrack.controller.command;

import br.org.tabletoprpg.soundtrack.controller.Command;
import br.org.tabletoprpg.soundtrack.controller.CommandHandler;
import br.org.tabletoprpg.soundtrack.controller.result.StringResult;
import br.org.tabletoprpg.soundtrack.service.session.SessionService;

public class PauseSongHandler implements CommandHandler {

    public static final String COMMAND_NAME = "PAUSE_SONG";

    private final SessionService sessionService;

    public PauseSongHandler(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @Override
    public StringResult handle(Command command) {
        requireParamCount(command.getParameters(), 0, COMMAND_NAME);
        this.sessionService.pauseSong();
        return new StringResult("▌▌ Música pausada.");
    }
}

```

command/PreviousSongHandler.java
```
package br.org.tabletoprpg.soundtrack.controller.command;

import br.org.tabletoprpg.soundtrack.controller.Command;
import br.org.tabletoprpg.soundtrack.controller.CommandHandler;
import br.org.tabletoprpg.soundtrack.controller.result.StringResult;
import br.org.tabletoprpg.soundtrack.service.session.SessionService;

public class PreviousSongHandler implements CommandHandler {

    public static final String COMMAND_NAME = "PREVIOUS_SONG";

    private final SessionService sessionService;

    public PreviousSongHandler(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @Override
    public StringResult handle(Command command) {
        requireParamCount(command.getParameters(), 0, COMMAND_NAME);
        this.sessionService.previousSong();
        return new StringResult("◀◀ Música anterior.");
    }
}

```

command/PauseAmbienceHandler.java
```
package br.org.tabletoprpg.soundtrack.controller.command;

import br.org.tabletoprpg.soundtrack.controller.Command;
import br.org.tabletoprpg.soundtrack.controller.CommandHandler;
import br.org.tabletoprpg.soundtrack.controller.result.StringResult;
import br.org.tabletoprpg.soundtrack.service.session.SessionService;

public class PauseAmbienceHandler implements CommandHandler {

    public static final String COMMAND_NAME = "PAUSE_AMBIENCE";

    private final SessionService sessionService;

    public PauseAmbienceHandler(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @Override
    public StringResult handle(Command command) {
        requireParamCount(command.getParameters(), 0, COMMAND_NAME);
        this.sessionService.pauseAmbience();
        return new StringResult("▌▌ Som ambiente pausado.");
    }
}

```

command/PauseBothHandler.java
```
package br.org.tabletoprpg.soundtrack.controller.command;

import br.org.tabletoprpg.soundtrack.controller.Command;
import br.org.tabletoprpg.soundtrack.controller.CommandHandler;
import br.org.tabletoprpg.soundtrack.controller.result.StringResult;
import br.org.tabletoprpg.soundtrack.service.session.SessionService;

public class PauseBothHandler implements CommandHandler {

    public static final String COMMAND_NAME = "PAUSE_BOTH";

    private final SessionService sessionService;

    public PauseBothHandler(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @Override
    public StringResult handle(Command command) {
        requireParamCount(command.getParameters(), 0, COMMAND_NAME);
        this.sessionService.pauseBoth();
        return new StringResult("▌▌ Música e ambiente pausados.");
    }
}

```

command/NextAmbienceHandler.java
```
package br.org.tabletoprpg.soundtrack.controller.command;

import br.org.tabletoprpg.soundtrack.controller.Command;
import br.org.tabletoprpg.soundtrack.controller.CommandHandler;
import br.org.tabletoprpg.soundtrack.controller.result.StringResult;
import br.org.tabletoprpg.soundtrack.service.session.SessionService;

public class NextAmbienceHandler implements CommandHandler {

    public static final String COMMAND_NAME = "NEXT_AMBIENCE";

    private final SessionService sessionService;

    public NextAmbienceHandler(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @Override
    public StringResult handle(Command command) {
        requireParamCount(command.getParameters(), 0, COMMAND_NAME);
        this.sessionService.nextAmbience();
        return new StringResult("▶▶ Próximo som ambiente.");
    }
}

```

command/SetOstHandler.java
```
package br.org.tabletoprpg.soundtrack.controller.command;

import br.org.tabletoprpg.soundtrack.controller.Command;
import br.org.tabletoprpg.soundtrack.controller.CommandHandler;
import br.org.tabletoprpg.soundtrack.controller.result.StringResult;
import br.org.tabletoprpg.soundtrack.service.session.SessionService;

public class SetOstHandler implements CommandHandler {

    public static final String COMMAND_NAME = "SET_OST";

    private final SessionService sessionService;

    public SetOstHandler(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @Override
    public StringResult handle(Command command) {
        requireParamCount(command.getParameters(), 1, COMMAND_NAME);
        String ostName = extractString(command.getParameters(), 0, "ost");

        this.sessionService.setCurrentOst(ostName);

        String theme = this.sessionService.getCurrentThemeName();
        return new StringResult("OST '" + ostName + "' selecionada. Tema atual: '" + theme + "'.");
    }
}

```

command/NextSongHandler.java
```
package br.org.tabletoprpg.soundtrack.controller.command;

import br.org.tabletoprpg.soundtrack.controller.Command;
import br.org.tabletoprpg.soundtrack.controller.CommandHandler;
import br.org.tabletoprpg.soundtrack.controller.result.StringResult;
import br.org.tabletoprpg.soundtrack.service.session.SessionService;

public class NextSongHandler implements CommandHandler {

    public static final String COMMAND_NAME = "NEXT_SONG";

    private final SessionService sessionService;

    public NextSongHandler(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @Override
    public StringResult handle(Command command) {
        requireParamCount(command.getParameters(), 0, COMMAND_NAME);
        this.sessionService.nextSong();
        return new StringResult("▶▶ Próxima música.");
    }
}

```

command/PlaySongHandler.java
```
package br.org.tabletoprpg.soundtrack.controller.command;

import br.org.tabletoprpg.soundtrack.controller.Command;
import br.org.tabletoprpg.soundtrack.controller.CommandHandler;
import br.org.tabletoprpg.soundtrack.controller.result.StringResult;
import br.org.tabletoprpg.soundtrack.service.session.SessionService;

public class PlaySongHandler implements CommandHandler {

    public static final String COMMAND_NAME = "PLAY_SONG";

    private final SessionService sessionService;

    public PlaySongHandler(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @Override
    public StringResult handle(Command command) {
        requireParamCount(command.getParameters(), 0, COMMAND_NAME);
        this.sessionService.playSong();
        return new StringResult("▶ Tocando música do tema '" + this.sessionService.getCurrentThemeName() + "'.");
    }
}

```

command/PlayBothHandler.java
```
package br.org.tabletoprpg.soundtrack.controller.command;

import br.org.tabletoprpg.soundtrack.controller.Command;
import br.org.tabletoprpg.soundtrack.controller.CommandHandler;
import br.org.tabletoprpg.soundtrack.controller.result.StringResult;
import br.org.tabletoprpg.soundtrack.service.session.SessionService;

public class PlayBothHandler implements CommandHandler {

    public static final String COMMAND_NAME = "PLAY_BOTH";

    private final SessionService sessionService;

    public PlayBothHandler(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @Override
    public StringResult handle(Command command) {
        requireParamCount(command.getParameters(), 0, COMMAND_NAME);
        this.sessionService.playBoth();
        return new StringResult(
                "▶ Tocando música + ambiente do tema '" + this.sessionService.getCurrentThemeName() + "'.");
    }
}

```

command/PreviousAmbienceHandler.java
```
package br.org.tabletoprpg.soundtrack.controller.command;

import br.org.tabletoprpg.soundtrack.controller.Command;
import br.org.tabletoprpg.soundtrack.controller.CommandHandler;
import br.org.tabletoprpg.soundtrack.controller.result.StringResult;
import br.org.tabletoprpg.soundtrack.service.session.SessionService;

public class PreviousAmbienceHandler implements CommandHandler {

    public static final String COMMAND_NAME = "PREVIOUS_AMBIENCE";

    private final SessionService sessionService;

    public PreviousAmbienceHandler(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @Override
    public StringResult handle(Command command) {
        requireParamCount(command.getParameters(), 0, COMMAND_NAME);
        this.sessionService.previousAmbience();
        return new StringResult("◀◀ Som ambiente anterior.");
    }
}

```

