package br.org.tabletoprpg.soundtrack.service.playback;

import java.util.Collections;
import java.util.List;

import br.org.tabletoprpg.soundtrack.audio.AudioPlayer;

/**
 * Implementação padrão do serviço de reprodução.
 */
public class PlaybackServiceImpl implements PlaybackService {

    public PlaybackServiceImpl(
            PlaybackStrategy strategy,
            AudioPlayer audioPlayer) {

        this.strategy = strategy;
        this.audioPlayer = audioPlayer;
    }

    /**
     * Responsável por escolher a próxima faixa da playlist.
     */
    private final PlaybackStrategy strategy;

    /**
     * Player responsável por reproduzir um único arquivo.
     */
    private final AudioPlayer audioPlayer;

    /**
     * Playlist atualmente configurada.
     */
    private List<String> playlist = Collections.emptyList();

    /**
     * Thread responsável pela reprodução contínua.
     */
    private Thread playbackThread;

    /**
     * Indica se a reprodução está ativa.
     */
    private volatile boolean playing;

    @Override
    public synchronized void setPlaylist(List<String> playlist) {

        if (playlist == null) {
            this.playlist = Collections.emptyList();
            return;
        }

        this.playlist = playlist;
    }

    @Override
    public synchronized void play() {

        if (playing) {
            return;
        }

        if (playlist.isEmpty()) {
            return;
        }

        playing = true;

        playbackThread = new Thread(new Runnable() {

            @Override
            public void run() {
                playbackLoop();
            }

        });

        playbackThread.setDaemon(true);

        playbackThread.start();
    }

    /**
     * Loop principal de reprodução.
     */
    private void playbackLoop() {

        while (playing) {

            // Escolhe a próxima música.
            String nextTrack = strategy.nextTrack(playlist);

            // Solicita ao player que reproduza a faixa.
            audioPlayer.play(nextTrack);

            // Aguarda o término da reprodução.
            while (playing && audioPlayer.isPlaying()) {

                try {

                    Thread.sleep(100);

                } catch (InterruptedException ignored) {
                    return;
                }
            }
        }
    }

    @Override
    public synchronized void stop() {

        playing = false;

        audioPlayer.stop();

        if (playbackThread != null) {

            playbackThread.interrupt();

            try {

                playbackThread.join();

            } catch (InterruptedException ignored) {
            }

            playbackThread = null;
        }
    }

    @Override
    public boolean isPlaying() {
        return playing;
    }

}