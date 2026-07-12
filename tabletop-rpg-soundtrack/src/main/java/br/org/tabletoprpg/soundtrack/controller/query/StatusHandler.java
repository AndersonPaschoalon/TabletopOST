package br.org.tabletoprpg.soundtrack.controller.query;

import br.org.tabletoprpg.soundtrack.controller.Command;
import br.org.tabletoprpg.soundtrack.controller.CommandHandler;
import br.org.tabletoprpg.soundtrack.controller.result.StatusResult;
import br.org.tabletoprpg.soundtrack.view.cli.ConsolePrompt;
import br.org.tabletoprpg.soundtrack.service.session.SessionService;

public class StatusHandler implements CommandHandler {

    public static final String COMMAND_NAME = "STATUS";

    private final SessionService sessionService;

    public StatusHandler(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @Override
    public StatusResult handle(Command command) {
        requireParamCount(command.getParameters(), 0, COMMAND_NAME);

        // ConsolePrompt.println("\u001B[1m--- Status ---\u001B[0m");
        // String ost = this.sessionService.getCurrentOstName();
        // String theme = this.sessionService.getCurrentThemeName();
        // ConsolePrompt.println("OST atual: " + (ost == null ? "(nenhuma)" : ost));
        // ConsolePrompt.println("Tema atual: " + (theme == null ? "(nenhum)" : theme));
        // ConsolePrompt.println("Música: " + (this.sessionService.isSongPlaying() ? "▶
        // tocando" : "▌▌ pausada"));
        // ConsolePrompt
        // .println("Ambiente: " + (this.sessionService.isAmbiencePlaying() ? "▶
        // tocando" : "▌▌ pausado"));

        String ost = this.sessionService.getCurrentOstName();
        String theme = this.sessionService.getCurrentThemeName();
        StatusResult r = new StatusResult(ost, theme, this.sessionService.isSongPlaying(),
                this.sessionService.isAmbiencePlaying());
        return r;
    }
}
