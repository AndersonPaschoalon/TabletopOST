package br.org.tabletoprpg.soundtrack.controller.command;

import br.org.tabletoprpg.soundtrack.controller.Command;
import br.org.tabletoprpg.soundtrack.controller.CommandHandler;
import br.org.tabletoprpg.soundtrack.service.session.SessionService;

public class PreviousSongHandler implements CommandHandler {

    public static final String COMMAND_NAME = "PREVIOUS_SONG";

    private final SessionService sessionService;

    public PreviousSongHandler(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @Override
    public String handle(Command command) {
        requireParamCount(command.getParameters(), 0, COMMAND_NAME);
        this.sessionService.previousSong();
        return "◀◀ Música anterior.";
    }
}
