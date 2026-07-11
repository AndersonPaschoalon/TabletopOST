package br.org.tabletoprpg.soundtrack.controller.command;

import br.org.tabletoprpg.soundtrack.controller.Command;
import br.org.tabletoprpg.soundtrack.controller.CommandHandler;
import br.org.tabletoprpg.soundtrack.model.Theme;
import br.org.tabletoprpg.soundtrack.view.cli.ConsolePrompt;
import br.org.tabletoprpg.soundtrack.service.session.SessionService;

public class GetThemeImagesHandler implements CommandHandler {

    public static final String COMMAND_NAME = "GET_THEME_IMAGES";

    private final SessionService sessionService;

    public GetThemeImagesHandler(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @Override
    public String handle(Command command) {
        requireParamCount(command.getParameters(), 0, COMMAND_NAME);

        Theme theme = this.sessionService.getCurrentTheme();

        if (theme == null) {
            ConsolePrompt.println("Nenhum tema selecionado.");
            return null;
        }

        ConsolePrompt.println("Imagens do tema '" + theme.name() + "':");
        for (String image : theme.images()) {
            ConsolePrompt.println(" - " + image);
        }
        return null;
    }
}
