package br.org.tabletoprpg.soundtrack.controller.query;

import br.org.tabletoprpg.soundtrack.controller.Command;
import br.org.tabletoprpg.soundtrack.controller.CommandHandler;
import br.org.tabletoprpg.soundtrack.controller.result.StringListResult;
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
