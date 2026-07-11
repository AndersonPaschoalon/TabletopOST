package br.org.tabletoprpg.soundtrack.controller.command;

import br.org.tabletoprpg.soundtrack.controller.Command;
import br.org.tabletoprpg.soundtrack.controller.CommandHandler;
import br.org.tabletoprpg.soundtrack.service.session.SessionService;

public class GetCurrentThemeHandler implements CommandHandler {

    public static final String COMMAND_NAME = "GET_CURRENT_THEME";

    private final SessionService sessionService;

    public GetCurrentThemeHandler(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @Override
    public String handle(Command command) {
        requireParamCount(command.getParameters(), 0, COMMAND_NAME);

        String themeName = this.sessionService.getCurrentThemeName();

        if (themeName == null) {
            return "Nenhum tema selecionado.";
        }

        return "Tema atual: " + themeName;
    }
}
