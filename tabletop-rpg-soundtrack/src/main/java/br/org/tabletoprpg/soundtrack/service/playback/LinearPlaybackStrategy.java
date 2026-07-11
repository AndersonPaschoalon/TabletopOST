package br.org.tabletoprpg.soundtrack.service.playback;

import java.util.List;

/**
 * Estratégia de reprodução linear (sequencial).
 *
 * As faixas são reproduzidas na ordem em que aparecem na playlist,
 * uma após a outra. Ao chegar na última faixa, a reprodução
 * recomeça a partir da primeira.
 */
public class LinearPlaybackStrategy implements PlaybackStrategy {

    /**
     * Referência da última playlist recebida.
     *
     * Usada para detectar quando a playlist foi trocada
     * (por exemplo, após uma troca de OST ou tema), momento em que
     * o índice de reprodução deve ser reiniciado.
     */
    private List<String> lastPlaylist;

    /**
     * Índice da última faixa reproduzida dentro da playlist atual.
     *
     * Começa em -1 para que a primeira chamada retorne o índice 0.
     */
    private int currentIndex = -1;

    @Override
    public synchronized String nextTrack(List<String> playlist) {

        if (playlist == null || playlist.isEmpty()) {
            return null;
        }

        // Se a playlist mudou desde a última chamada,
        // reiniciamos a contagem para começar do início.
        if (playlist != lastPlaylist) {
            lastPlaylist = playlist;
            currentIndex = -1;
        }

        // Avança para a próxima faixa, voltando ao início
        // ao ultrapassar o fim da playlist.
        currentIndex = (currentIndex + 1) % playlist.size();

        return playlist.get(currentIndex);
    }

    @Override
    public synchronized String previousTrack(List<String> playlist) {

        if (playlist == null || playlist.isEmpty()) {
            return null;
        }

        if (playlist != lastPlaylist) {
            lastPlaylist = playlist;
            currentIndex = 0;
            return playlist.get(currentIndex);
        }

        currentIndex = Math.floorMod(currentIndex - 1, playlist.size());

        return playlist.get(currentIndex);
    }

}
