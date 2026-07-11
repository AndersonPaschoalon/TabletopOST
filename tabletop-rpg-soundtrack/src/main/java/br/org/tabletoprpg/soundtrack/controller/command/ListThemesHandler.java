package br.org.tabletoprpg.soundtrack.controller.command;

import br.org.tabletoprpg.soundtrack.controller.Command;
import br.org.tabletoprpg.soundtrack.controller.CommandHandler;
import br.org.tabletoprpg.soundtrack.view.cli.ConsolePrompt;
import br.org.tabletoprpg.soundtrack.service.session.SessionService;

public class ListThemesHandler implements CommandHandler {

    public static final String COMMAND_NAME = "LIST_THEMES";

    private final SessionService sessionService;

    public ListThemesHandler(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @Override
    public String handle(Command command) {
        requireParamCount(command.getParameters(), 0, COMMAND_NAME);

        var themes = this.sessionService.getListOfThemes();

        ConsolePrompt.println("Temas disponíveis:");
        for (String theme : themes) {
            ConsolePrompt.println(" - " + theme);
        }
        return null;
    }
}
