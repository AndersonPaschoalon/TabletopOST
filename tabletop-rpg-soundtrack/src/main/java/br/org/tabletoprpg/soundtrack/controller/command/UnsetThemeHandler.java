package br.org.tabletoprpg.soundtrack.controller.command;

import br.org.tabletoprpg.soundtrack.controller.Command;
import br.org.tabletoprpg.soundtrack.controller.CommandHandler;
import br.org.tabletoprpg.soundtrack.service.session.SessionService;

public class UnsetThemeHandler implements CommandHandler {

    public static final String COMMAND_NAME = "UNSET_THEME";

    private final SessionService sessionService;

    public UnsetThemeHandler(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @Override
    public String handle(Command command) {
        requireParamCount(command.getParameters(), 0, COMMAND_NAME);
        this.sessionService.unsetTheme();
        return "Tema resetado para o padrão: '" + this.sessionService.getCurrentThemeName() + "'.";
    }
}
