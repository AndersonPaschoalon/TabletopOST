package br.org.tabletoprpg.soundtrack.controller.command;

import br.org.tabletoprpg.soundtrack.controller.Command;
import br.org.tabletoprpg.soundtrack.controller.CommandHandler;
import br.org.tabletoprpg.soundtrack.controller.result.StringResult;
import br.org.tabletoprpg.soundtrack.service.session.SessionService;

public class SetThemeHandler implements CommandHandler {

    public static final String COMMAND_NAME = "SET_THEME";

    private final SessionService sessionService;

    public SetThemeHandler(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @Override
    public StringResult handle(Command command) {
        requireParamCount(command.getParameters(), 1, COMMAND_NAME);
        String themeName = extractString(command.getParameters(), 0, "theme");

        this.sessionService.setCurrentTheme(themeName);

        return new StringResult("Tema '" + themeName + "' selecionado (OST: '"
                + this.sessionService.getCurrentOstName() + "').");
    }
}
