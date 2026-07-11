CommandDispatcher.java
```
package br.org.tabletoprpg.soundtrack.controller;

import java.util.HashMap;
import java.util.Map;

import br.org.tabletoprpg.soundtrack.controller.command.GetCurrentThemeHandler;
import br.org.tabletoprpg.soundtrack.controller.command.GetThemeImageHandler;
import br.org.tabletoprpg.soundtrack.controller.command.GetThemeImagesHandler;
import br.org.tabletoprpg.soundtrack.controller.command.ListOstsHandler;
import br.org.tabletoprpg.soundtrack.controller.command.ListThemesHandler;
import br.org.tabletoprpg.soundtrack.controller.command.PauseAmbienceHandler;
import br.org.tabletoprpg.soundtrack.controller.command.PauseBothHandler;
import br.org.tabletoprpg.soundtrack.controller.command.PauseSongHandler;
import br.org.tabletoprpg.soundtrack.controller.command.PlayAmbienceHandler;
import br.org.tabletoprpg.soundtrack.controller.command.PlayBothHandler;
import br.org.tabletoprpg.soundtrack.controller.command.PlaySongHandler;
import br.org.tabletoprpg.soundtrack.controller.command.SetOstHandler;
import br.org.tabletoprpg.soundtrack.controller.command.SetThemeHandler;
import br.org.tabletoprpg.soundtrack.controller.command.StatusHandler;
import br.org.tabletoprpg.soundtrack.controller.command.UnsetOstHandler;
import br.org.tabletoprpg.soundtrack.controller.command.UnsetThemeHandler;
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

        h.put(PlayAmbienceHandler.COMMAND_NAME, new PlayAmbienceHandler(sessionService));
        h.put(PauseAmbienceHandler.COMMAND_NAME, new PauseAmbienceHandler(sessionService));

        h.put(PlayBothHandler.COMMAND_NAME, new PlayBothHandler(sessionService));
        h.put(PauseBothHandler.COMMAND_NAME, new PauseBothHandler(sessionService));

        h.put(ListThemesHandler.COMMAND_NAME, new ListThemesHandler(sessionService));
        h.put(GetCurrentThemeHandler.COMMAND_NAME, new GetCurrentThemeHandler(sessionService));
        h.put(GetThemeImagesHandler.COMMAND_NAME, new GetThemeImagesHandler(sessionService));
        h.put(GetThemeImageHandler.COMMAND_NAME, new GetThemeImageHandler(sessionService));

        h.put(StatusHandler.COMMAND_NAME, new StatusHandler(sessionService));

        return h;
    }

    public String dispatch(Command command) {

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

public interface CommandHandler {

    String handle(Command command);

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

command/UnsetThemeHandler.java
```
package br.org.tabletoprpg.soundtrack.controller.command;

import br.org.tabletoprpg.soundtrack.controller.Command;
import br.org.tabletoprpg.soundtrack.controller.CommandHandler;
import br.org.tabletoprpg.soundtrack.service.session.SessionService;

public class UnsetThemeHandler implements CommandHandler {

    public static final String COMMAND_NAME = "UNSET_THEME";

    private final SessionService sessionService;

    public UnsetThemeHandler(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @Override
    public String handle(Command command) {
        requireParamCount(command.getParameters(), 0, COMMAND_NAME);
        this.sessionService.unsetTheme();
        return "Tema resetado para o padrão: '" + this.sessionService.getCurrentThemeName() + "'.";
    }
}

```

command/PlayAmbienceHandler.java
```
package br.org.tabletoprpg.soundtrack.controller.command;

import br.org.tabletoprpg.soundtrack.controller.Command;
import br.org.tabletoprpg.soundtrack.controller.CommandHandler;
import br.org.tabletoprpg.soundtrack.service.session.SessionService;

public class PlayAmbienceHandler implements CommandHandler {

    public static final String COMMAND_NAME = "PLAY_AMBIENCE";

    private final SessionService sessionService;

    public PlayAmbienceHandler(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @Override
    public String handle(Command command) {
        requireParamCount(command.getParameters(), 0, COMMAND_NAME);
        this.sessionService.playAmbience();
        return "▶ Tocando som ambiente do tema '" + this.sessionService.getCurrentThemeName() + "'.";
    }
}

```

command/GetThemeImageHandler.java
```
package br.org.tabletoprpg.soundtrack.controller.command;

import java.util.List;

import br.org.tabletoprpg.soundtrack.controller.Command;
import br.org.tabletoprpg.soundtrack.controller.CommandHandler;
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
    public String handle(Command command) {
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

        ConsolePrompt.println(images.get(index));
        return null;
    }
}

```

command/GetCurrentThemeHandler.java
```
package br.org.tabletoprpg.soundtrack.controller.command;

import br.org.tabletoprpg.soundtrack.controller.Command;
import br.org.tabletoprpg.soundtrack.controller.CommandHandler;
import br.org.tabletoprpg.soundtrack.service.session.SessionService;

public class GetCurrentThemeHandler implements CommandHandler {

    public static final String COMMAND_NAME = "GET_CURRENT_THEME";

    private final SessionService sessionService;

    public GetCurrentThemeHandler(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @Override
    public String handle(Command command) {
        requireParamCount(command.getParameters(), 0, COMMAND_NAME);

        String themeName = this.sessionService.getCurrentThemeName();

        if (themeName == null) {
            return "Nenhum tema selecionado.";
        }

        return "Tema atual: " + themeName;
    }
}

```

command/UnsetOstHandler.java
```
package br.org.tabletoprpg.soundtrack.controller.command;

import br.org.tabletoprpg.soundtrack.controller.Command;
import br.org.tabletoprpg.soundtrack.controller.CommandHandler;
import br.org.tabletoprpg.soundtrack.service.session.SessionService;

public class UnsetOstHandler implements CommandHandler {

    public static final String COMMAND_NAME = "UNSET_OST";

    private final SessionService sessionService;

    public UnsetOstHandler(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @Override
    public String handle(Command command) {
        requireParamCount(command.getParameters(), 0, COMMAND_NAME);
        this.sessionService.unsetOst();
        return "OST desselecionada. Reprodução interrompida.";
    }
}

```

command/StatusHandler.java
```
package br.org.tabletoprpg.soundtrack.controller.command;

import br.org.tabletoprpg.soundtrack.controller.Command;
import br.org.tabletoprpg.soundtrack.controller.CommandHandler;
import br.org.tabletoprpg.soundtrack.view.cli.ConsolePrompt;
import br.org.tabletoprpg.soundtrack.service.session.SessionService;

public class StatusHandler implements CommandHandler {

    public static final String COMMAND_NAME = "STATUS";

    private final SessionService sessionService;

    public StatusHandler(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @Override
    public String handle(Command command) {
        requireParamCount(command.getParameters(), 0, COMMAND_NAME);

        String ost = this.sessionService.getCurrentOstName();
        String theme = this.sessionService.getCurrentThemeName();

        ConsolePrompt.println("\u001B[1m--- Status ---\u001B[0m");
        ConsolePrompt.println("OST atual:      " + (ost == null ? "(nenhuma)" : ost));
        ConsolePrompt.println("Tema atual:     " + (theme == null ? "(nenhum)" : theme));
        ConsolePrompt.println("Música:         " + (this.sessionService.isSongPlaying() ? "▶ tocando" : "▌▌ pausada"));
        ConsolePrompt.println("Ambiente:       " + (this.sessionService.isAmbiencePlaying() ? "▶ tocando" : "▌▌ pausado"));

        return null;
    }
}

```

command/ListOstsHandler.java
```
package br.org.tabletoprpg.soundtrack.controller.command;

import br.org.tabletoprpg.soundtrack.controller.Command;
import br.org.tabletoprpg.soundtrack.controller.CommandHandler;
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
    public String handle(Command command) {
        requireParamCount(command.getParameters(), 0, COMMAND_NAME);

        var osts = this.sessionService.getListOfOsts();

        if (osts.isEmpty()) {
            ConsolePrompt.println("Nenhuma OST disponível.");
            return null;
        }

        ConsolePrompt.println("OSTs disponíveis:");
        for (String ost : osts) {
            ConsolePrompt.println(" - " + ost);
        }
        return null;
    }
}

```

command/SetThemeHandler.java
```
package br.org.tabletoprpg.soundtrack.controller.command;

import br.org.tabletoprpg.soundtrack.controller.Command;
import br.org.tabletoprpg.soundtrack.controller.CommandHandler;
import br.org.tabletoprpg.soundtrack.service.session.SessionService;

public class SetThemeHandler implements CommandHandler {

    public static final String COMMAND_NAME = "SET_THEME";

    private final SessionService sessionService;

    public SetThemeHandler(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @Override
    public String handle(Command command) {
        requireParamCount(command.getParameters(), 1, COMMAND_NAME);
        String themeName = extractString(command.getParameters(), 0, "theme");

        this.sessionService.setCurrentTheme(themeName);

        return "Tema '" + themeName + "' selecionado (OST: '"
                + this.sessionService.getCurrentOstName() + "').";
    }
}

```

command/GetThemeImagesHandler.java
```
package br.org.tabletoprpg.soundtrack.controller.command;

import br.org.tabletoprpg.soundtrack.controller.Command;
import br.org.tabletoprpg.soundtrack.controller.CommandHandler;
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
    public String handle(Command command) {
        requireParamCount(command.getParameters(), 0, COMMAND_NAME);

        Theme theme = this.sessionService.getCurrentTheme();

        if (theme == null) {
            ConsolePrompt.println("Nenhum tema selecionado.");
            return null;
        }

        ConsolePrompt.println("Imagens do tema '" + theme.name() + "':");
        for (String image : theme.images()) {
            ConsolePrompt.println(" - " + image);
        }
        return null;
    }
}

```

command/PauseSongHandler.java
```
package br.org.tabletoprpg.soundtrack.controller.command;

import br.org.tabletoprpg.soundtrack.controller.Command;
import br.org.tabletoprpg.soundtrack.controller.CommandHandler;
import br.org.tabletoprpg.soundtrack.service.session.SessionService;

public class PauseSongHandler implements CommandHandler {

    public static final String COMMAND_NAME = "PAUSE_SONG";

    private final SessionService sessionService;

    public PauseSongHandler(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @Override
    public String handle(Command command) {
        requireParamCount(command.getParameters(), 0, COMMAND_NAME);
        this.sessionService.pauseSong();
        return "▌▌ Música pausada.";
    }
}

```

command/PauseAmbienceHandler.java
```
package br.org.tabletoprpg.soundtrack.controller.command;

import br.org.tabletoprpg.soundtrack.controller.Command;
import br.org.tabletoprpg.soundtrack.controller.CommandHandler;
import br.org.tabletoprpg.soundtrack.service.session.SessionService;

public class PauseAmbienceHandler implements CommandHandler {

    public static final String COMMAND_NAME = "PAUSE_AMBIENCE";

    private final SessionService sessionService;

    public PauseAmbienceHandler(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @Override
    public String handle(Command command) {
        requireParamCount(command.getParameters(), 0, COMMAND_NAME);
        this.sessionService.pauseAmbience();
        return "▌▌ Som ambiente pausado.";
    }
}

```

command/PauseBothHandler.java
```
package br.org.tabletoprpg.soundtrack.controller.command;

import br.org.tabletoprpg.soundtrack.controller.Command;
import br.org.tabletoprpg.soundtrack.controller.CommandHandler;
import br.org.tabletoprpg.soundtrack.service.session.SessionService;

public class PauseBothHandler implements CommandHandler {

    public static final String COMMAND_NAME = "PAUSE_BOTH";

    private final SessionService sessionService;

    public PauseBothHandler(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @Override
    public String handle(Command command) {
        requireParamCount(command.getParameters(), 0, COMMAND_NAME);
        this.sessionService.pauseBoth();
        return "▌▌ Música e ambiente pausados.";
    }
}

```

command/SetOstHandler.java
```
package br.org.tabletoprpg.soundtrack.controller.command;

import br.org.tabletoprpg.soundtrack.controller.Command;
import br.org.tabletoprpg.soundtrack.controller.CommandHandler;
import br.org.tabletoprpg.soundtrack.service.session.SessionService;

public class SetOstHandler implements CommandHandler {

    public static final String COMMAND_NAME = "SET_OST";

    private final SessionService sessionService;

    public SetOstHandler(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @Override
    public String handle(Command command) {
        requireParamCount(command.getParameters(), 1, COMMAND_NAME);
        String ostName = extractString(command.getParameters(), 0, "ost");

        this.sessionService.setCurrentOst(ostName);

        String theme = this.sessionService.getCurrentThemeName();
        return "OST '" + ostName + "' selecionada. Tema atual: '" + theme + "'.";
    }
}

```

command/ListThemesHandler.java
```
package br.org.tabletoprpg.soundtrack.controller.command;

import br.org.tabletoprpg.soundtrack.controller.Command;
import br.org.tabletoprpg.soundtrack.controller.CommandHandler;
import br.org.tabletoprpg.soundtrack.view.cli.ConsolePrompt;
import br.org.tabletoprpg.soundtrack.service.session.SessionService;

public class ListThemesHandler implements CommandHandler {

    public static final String COMMAND_NAME = "LIST_THEMES";

    private final SessionService sessionService;

    public ListThemesHandler(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @Override
    public String handle(Command command) {
        requireParamCount(command.getParameters(), 0, COMMAND_NAME);

        var themes = this.sessionService.getListOfThemes();

        ConsolePrompt.println("Temas disponíveis:");
        for (String theme : themes) {
            ConsolePrompt.println(" - " + theme);
        }
        return null;
    }
}

```

command/PlaySongHandler.java
```
package br.org.tabletoprpg.soundtrack.controller.command;

import br.org.tabletoprpg.soundtrack.controller.Command;
import br.org.tabletoprpg.soundtrack.controller.CommandHandler;
import br.org.tabletoprpg.soundtrack.service.session.SessionService;

public class PlaySongHandler implements CommandHandler {

    public static final String COMMAND_NAME = "PLAY_SONG";

    private final SessionService sessionService;

    public PlaySongHandler(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @Override
    public String handle(Command command) {
        requireParamCount(command.getParameters(), 0, COMMAND_NAME);
        this.sessionService.playSong();
        return "▶ Tocando música do tema '" + this.sessionService.getCurrentThemeName() + "'.";
    }
}

```

command/PlayBothHandler.java
```
package br.org.tabletoprpg.soundtrack.controller.command;

import br.org.tabletoprpg.soundtrack.controller.Command;
import br.org.tabletoprpg.soundtrack.controller.CommandHandler;
import br.org.tabletoprpg.soundtrack.service.session.SessionService;

public class PlayBothHandler implements CommandHandler {

    public static final String COMMAND_NAME = "PLAY_BOTH";

    private final SessionService sessionService;

    public PlayBothHandler(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @Override
    public String handle(Command command) {
        requireParamCount(command.getParameters(), 0, COMMAND_NAME);
        this.sessionService.playBoth();
        return "▶ Tocando música + ambiente do tema '" + this.sessionService.getCurrentThemeName() + "'.";
    }
}

```

