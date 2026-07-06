package br.org.tabletoprpg.soundtrack.app;

import br.org.tabletoprpg.soundtrack.controller.CommandDispatcher;
import br.org.tabletoprpg.soundtrack.service.playback.PlaybackService;
import br.org.tabletoprpg.soundtrack.service.session.SessionService;

public class ApplicationContext implements AutoCloseable {

    /*
     * ==========================================================
     * Estado global da aplicação.
     * ==========================================================
     */

    /**
     * OST atualmente selecionada.
     */
    private String currentOst;

    /**
     * Tema atualmente selecionado.
     */
    private String currentTheme;

    /*
     * ==========================================================
     * Componentes compartilhados.
     * ==========================================================
     */

    /**
     * Dispatcher responsável por encaminhar comandos aos handlers.
     */
    private final CommandDispatcher dispatcher;

    /**
     * Player responsável pela reprodução das músicas.
     */
    private final PlaybackService musicPlayback;

    /**
     * Player responsável pela reprodução dos sons ambientes.
     */
    private final PlaybackService ambiencePlayback;

    private final SessionService sessionService;

    public ApplicationContext(
            CommandDispatcher dispatcher,
            PlaybackService musicPlayback,
            PlaybackService ambiencePlayback,
            SessionService sessionService) {

        this.dispatcher = dispatcher;
        this.musicPlayback = musicPlayback;
        this.ambiencePlayback = ambiencePlayback;
        this.sessionService = sessionService;
    }

    /*
     * ==========================================================
     * Estado da aplicação.
     * ==========================================================
     */

    public String getCurrentOst() {
        return this.currentOst;
    }

    public void setCurrentOst(String currentOst) {
        this.currentOst = currentOst;
    }

    public String getCurrentTheme() {
        return this.currentTheme;
    }

    public void setCurrentTheme(String currentTheme) {
        this.currentTheme = currentTheme;
    }

    /*
     * ==========================================================
     * Componentes compartilhados.
     * ==========================================================
     */

    public CommandDispatcher getDispatcher() {
        return this.dispatcher;
    }

    public PlaybackService getMusicPlayback() {
        return this.musicPlayback;
    }

    public PlaybackService getAmbiencePlayback() {
        return this.ambiencePlayback;
    }

    public SessionService getSessionService() {
        return this.sessionService;
    }

    @Override
    public void close() {

        /*
         * Garante que nenhuma thread permaneça executando
         * ao encerrar a aplicação.
         */

        musicPlayback.stop();
        ambiencePlayback.stop();
    }
}