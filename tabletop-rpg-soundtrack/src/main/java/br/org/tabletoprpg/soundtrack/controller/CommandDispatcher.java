package br.org.tabletoprpg.soundtrack.controller;

import java.util.HashMap;
import java.util.Map;

import br.org.tabletoprpg.soundtrack.controller.command.GetCurrentThemeHandler;
import br.org.tabletoprpg.soundtrack.controller.command.GetThemeImageHandler;
import br.org.tabletoprpg.soundtrack.controller.command.GetThemeImagesHandler;
import br.org.tabletoprpg.soundtrack.controller.command.ListOstsHandler;
import br.org.tabletoprpg.soundtrack.controller.command.ListThemesHandler;
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

    public String dispatch(Command command) {

        CommandHandler handler = handlers.get(command.getAction());

        if (handler == null) {
            throw new RuntimeException("Unknown command: " + command.getAction());
        }

        return handler.handle(command);
    }
}
