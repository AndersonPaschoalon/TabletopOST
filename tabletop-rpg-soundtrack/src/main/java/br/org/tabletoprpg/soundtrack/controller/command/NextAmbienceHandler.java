package br.org.tabletoprpg.soundtrack.controller.command;

import br.org.tabletoprpg.soundtrack.controller.Command;
import br.org.tabletoprpg.soundtrack.controller.CommandHandler;
import br.org.tabletoprpg.soundtrack.service.session.SessionService;

public class NextAmbienceHandler implements CommandHandler {

    public static final String COMMAND_NAME = "NEXT_AMBIENCE";

    private final SessionService sessionService;

    public NextAmbienceHandler(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @Override
    public String handle(Command command) {
        requireParamCount(command.getParameters(), 0, COMMAND_NAME);
        this.sessionService.nextAmbience();
        return "▶▶ Próximo som ambiente.";
    }
}
