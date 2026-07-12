package br.org.tabletoprpg.soundtrack.controller.query;

import java.util.ArrayList;
import java.util.Collection;

import br.org.tabletoprpg.soundtrack.controller.Command;
import br.org.tabletoprpg.soundtrack.controller.CommandHandler;
import br.org.tabletoprpg.soundtrack.controller.result.StringListResult;
import br.org.tabletoprpg.soundtrack.model.Theme;
import br.org.tabletoprpg.soundtrack.service.session.SessionService;

public class GetThemeImagesHandler implements CommandHandler {

    public static final String COMMAND_NAME = "GET_THEME_IMAGES";

    private final SessionService sessionService;

    public GetThemeImagesHandler(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @Override
    public StringListResult handle(Command command) {
        requireParamCount(command.getParameters(), 0, COMMAND_NAME);

        Theme theme = this.sessionService.getCurrentTheme();
        Collection<String> allImages = new ArrayList<String>();

        if (theme == null) {
            return new StringListResult(allImages);
        }

        allImages.addAll(theme.images());
        StringListResult result = new StringListResult(allImages);
        return result;
    }
}
