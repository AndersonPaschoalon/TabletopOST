package br.org.tabletoprpg.soundtrack.service.playback;

import java.util.List;
import java.util.Random;

/**
 * Estratégia de reprodução aleatória.
 *
 * A cada solicitação escolhe uma faixa aleatória da playlist.
 */
public class RandomPlaybackStrategy implements PlaybackStrategy {

    /**
     * Gerador de números aleatórios.
     *
     * É mantido como atributo para evitar recriá-lo
     * a cada chamada do método.
     */
    private final Random random = new Random();

    @Override
    public String nextTrack(List<String> playlist) {

        if (playlist == null || playlist.isEmpty()) {
            return null;
        }

        int index = random.nextInt(playlist.size());

        return playlist.get(index);
    }

}