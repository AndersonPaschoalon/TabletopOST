package br.org.tabletoprpg.soundtrack.controller.command;

import br.org.tabletoprpg.soundtrack.controller.Command;
import br.org.tabletoprpg.soundtrack.controller.CommandHandler;
import br.org.tabletoprpg.soundtrack.service.session.SessionService;

public class PlaySongHandler implements CommandHandler {

    public static final String COMMAND_NAME = "PLAY_SONG";

    private final SessionService sessionService;

    public PlaySongHandler(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @Override
    public String handle(Command command) {
        requireParamCount(command.getParameters(), 0, COMMAND_NAME);
        this.sessionService.playSong();
        return "▶ Tocando música do tema '" + this.sessionService.getCurrentThemeName() + "'.";
    }
}
