package br.org.tabletoprpg.soundtrack.controller.command;

import br.org.tabletoprpg.soundtrack.app.ApplicationContext;
import br.org.tabletoprpg.soundtrack.controller.Command;
import br.org.tabletoprpg.soundtrack.controller.CommandHandler;
import br.org.tabletoprpg.soundtrack.service.catalog.CatalogService;
import br.org.tabletoprpg.soundtrack.service.session.SessionService;

public class SetOstHandler implements CommandHandler {

    public static final String COMMAND_NAME = "SET_OST";

    private final SessionService sessionService;

    public SetOstHandler(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @Override
    public void handle(Command command) {

        requireParamCount(command.getParameters(), 1, COMMAND_NAME);

        String ostName = extractString(
                command.getParameters(),
                0,
                "ost");

        this.sessionService.setCurrentOst(ostName);
    }

}