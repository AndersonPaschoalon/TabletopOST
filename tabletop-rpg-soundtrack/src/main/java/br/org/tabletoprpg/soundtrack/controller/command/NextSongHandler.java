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
