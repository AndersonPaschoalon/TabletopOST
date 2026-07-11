package br.org.tabletoprpg.soundtrack.app;

import br.org.tabletoprpg.soundtrack.audio.AudioPlayer;
import br.org.tabletoprpg.soundtrack.audio.AudioPlayerFactory;
import br.org.tabletoprpg.soundtrack.controller.CommandDispatcher;
import br.org.tabletoprpg.soundtrack.service.catalog.CatalogService;
import br.org.tabletoprpg.soundtrack.service.catalog.LocalCatalogService;
import br.org.tabletoprpg.soundtrack.service.loader.OstLoader;
import br.org.tabletoprpg.soundtrack.service.playback.LinearPlaybackStrategy;
import br.org.tabletoprpg.soundtrack.service.playback.PlaybackService;
import br.org.tabletoprpg.soundtrack.service.playback.PlaybackServiceImpl;
import br.org.tabletoprpg.soundtrack.service.session.SessionService;

public final class AppBootstrap {

    private AppBootstrap() {
    }

    public static ApplicationContext create() {

        /*
         * Um AudioPlayer para cada canal.
         */

        AudioPlayer musicPlayer = AudioPlayerFactory.create();

        AudioPlayer ambiencePlayer = AudioPlayerFactory.create();

        /*
         * Cada PlaybackService controla um canal independente.
         */

        PlaybackService musicPlayback = new PlaybackServiceImpl(
                new LinearPlaybackStrategy(),
                musicPlayer);

        PlaybackService ambiencePlayback = new PlaybackServiceImpl(
                new LinearPlaybackStrategy(),
                ambiencePlayer);

        /*
         * Serviço de catálogo (sincroniza OSTs de local_storage para o cache).
         */
        CatalogService catalogService = new LocalCatalogService("local_storage");

        /*
         * Loader responsável por transformar o manifest.json em objetos de domínio.
         */
        OstLoader loader = new OstLoader();

        /*
        * Serviço de sessão da aplicação. Inicia sem nenhuma OST selecionada.
        */
        SessionService sessionService = new SessionService(
                null,
                catalogService,
                loader,
                ambiencePlayback,
                musicPlayback);

        /*
         * Dispatcher da aplicação.
         */
        CommandDispatcher dispatcher = new CommandDispatcher(sessionService);

        return new ApplicationContext(
                dispatcher,
                musicPlayback,
                ambiencePlayback,
                sessionService
        );
    }
}