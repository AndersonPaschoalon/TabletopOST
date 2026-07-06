package br.org.tabletoprpg.soundtrack.app;

import br.org.tabletoprpg.soundtrack.audio.AudioPlayer;
import br.org.tabletoprpg.soundtrack.audio.AudioPlayerFactory;
import br.org.tabletoprpg.soundtrack.controller.CommandDispatcher;
import br.org.tabletoprpg.soundtrack.service.playback.PlaybackService;
import br.org.tabletoprpg.soundtrack.service.playback.PlaybackServiceImpl;
import br.org.tabletoprpg.soundtrack.service.playback.RandomPlaybackStrategy;

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
                new RandomPlaybackStrategy(),
                musicPlayer);

        PlaybackService ambiencePlayback = new PlaybackServiceImpl(
                new RandomPlaybackStrategy(),
                ambiencePlayer);

        /*
         * Dispatcher da aplicação.
         */

        CommandDispatcher dispatcher = new CommandDispatcher();

        return new ApplicationContext(
                dispatcher,
                musicPlayback,
                ambiencePlayback,
            );
    }
}