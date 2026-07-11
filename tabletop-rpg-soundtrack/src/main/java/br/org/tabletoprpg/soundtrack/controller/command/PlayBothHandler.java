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
