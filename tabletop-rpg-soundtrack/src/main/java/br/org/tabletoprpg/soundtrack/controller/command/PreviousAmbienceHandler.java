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
