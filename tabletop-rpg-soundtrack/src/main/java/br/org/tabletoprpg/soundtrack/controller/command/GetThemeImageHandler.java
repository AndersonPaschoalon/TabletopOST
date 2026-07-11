package br.org.tabletoprpg.soundtrack.controller.command;

import java.util.List;

import br.org.tabletoprpg.soundtrack.controller.Command;
import br.org.tabletoprpg.soundtrack.controller.CommandHandler;
import br.org.tabletoprpg.soundtrack.model.Theme;
import br.org.tabletoprpg.soundtrack.view.cli.ConsolePrompt;
import br.org.tabletoprpg.soundtrack.service.session.SessionService;

public class GetThemeImageHandler implements CommandHandler {

    public static final String COMMAND_NAME = "GET_THEME_IMAGE";

    private final SessionService sessionService;

    public GetThemeImageHandler(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @Override
    public String handle(Command command) {
        requireParamCount(command.getParameters(), 1, COMMAND_NAME);

        String indexParam = extractString(command.getParameters(), 0, "index");

        int index;
        try {
            index = Integer.parseInt(indexParam);
        } catch (NumberFormatException ex) {
            throw new RuntimeException("O parâmetro 'index' deve ser um número inteiro.");
        }

        Theme theme = this.sessionService.getCurrentTheme();

        if (theme == null) {
            ConsolePrompt.println("Nenhum tema selecionado.");
            return null;
        }

        List<String> images = List.copyOf(theme.images());

        if (index < 0 || index >= images.size()) {
            throw new RuntimeException(
                    "Índice inválido. O tema '" + theme.name() + "' possui " + images.size() + " imagem(ns).");
        }

        ConsolePrompt.println(images.get(index));
        return null;
    }
}
