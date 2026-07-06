package br.org.tabletoprpg.soundtrack.controller;

import java.util.HashMap;
import java.util.Map;

import br.org.tabletoprpg.soundtrack.app.ApplicationContext;
import br.org.tabletoprpg.soundtrack.controller.command.SetOstHandler;

public class CommandDispatcher {

    private final Map<String, CommandHandler> handlers;

    public CommandDispatcher(ApplicationContext context) {
        this.handlers = createHandlers(context);
    }

    private Map<String, CommandHandler> createHandlers(ApplicationContext ctx) {

        Map<String, CommandHandler> h = new HashMap<>();

        h.put(SetOstHandler.COMMAND_NAME, new SetOstHandler(ctx.getSessionService()));
        // h.put(UnsetOstHandler.COMMAND_NAME, new
        // UnsetOstHandler(ctx.getSessionService()));

        // h.put(SetThemeHandler.COMMAND_NAME, new
        // SetThemeHandler(ctx.getSessionService()));
        // h.put(UnsetThemeHandler.COMMAND_NAME, new
        // UnsetThemeHandler(ctx.getSessionService()));

        // h.put(PlaySongHandler.COMMAND_NAME, new
        // PlaySongHandler(ctx.getMusicPlayback()));
        // h.put(PauseSongHandler.COMMAND_NAME, new
        // PauseSongHandler(ctx.getMusicPlayback()));

        return h;
    }

    public void dispatch(Command command) {

        CommandHandler handler = handlers.get(command.getAction());

        if (handler == null) {
            throw new RuntimeException("Unknown command: " + command.getAction());
        }

        handler.handle(command);
    }
}