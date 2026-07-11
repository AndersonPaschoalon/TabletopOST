package br.org.tabletoprpg.soundtrack.controller.command;

import br.org.tabletoprpg.soundtrack.controller.Command;
import br.org.tabletoprpg.soundtrack.controller.CommandHandler;
import br.org.tabletoprpg.soundtrack.view.cli.ConsolePrompt;
import br.org.tabletoprpg.soundtrack.service.session.SessionService;

/**
 * Lista as OSTs disponíveis no catálogo (local_storage).
 */
public class ListOstsHandler implements CommandHandler {

    public static final String COMMAND_NAME = "LIST_OSTS";

    private final SessionService sessionService;

    public ListOstsHandler(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @Override
    public String handle(Command command) {
        requireParamCount(command.getParameters(), 0, COMMAND_NAME);

        var osts = this.sessionService.getListOfOsts();

        if (osts.isEmpty()) {
            ConsolePrompt.println("Nenhuma OST disponível.");
            return null;
        }

        ConsolePrompt.println("OSTs disponíveis:");
        for (String ost : osts) {
            ConsolePrompt.println(" - " + ost);
        }
        return null;
    }
}
