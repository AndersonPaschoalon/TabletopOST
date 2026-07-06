package br.org.tabletoprpg.soundtrack.service.playback;

import java.util.List;

/**
 * Serviço responsável por controlar a reprodução contínua de uma playlist.
 * <p>
 * Esta classe representa a lógica de reprodução da aplicação, sendo
 * independente da plataforma onde está sendo executada.
 * </p>
 *
 * <p>
 * Suas responsabilidades são:
 * </p>
 *
 * <ul>
 * <li>armazenar a playlist atualmente selecionada;</li>
 * <li>iniciar e interromper a reprodução;</li>
 * <li>utilizar uma {@link PlaybackStrategy} para escolher a próxima faixa;</li>
 * <li>delegar a reprodução de uma faixa ao
 * {@link br.org.tabletoprpg.soundtrack.audio.AudioPlayer};</li>
 * <li>aguardar o término da reprodução da faixa atual;</li>
 * <li>repetir o processo até que {@link #stop()} seja chamado.</li>
 * </ul>
 *
 * <p>
 * Cada instância controla exatamente uma playlist. Na aplicação existirão
 * normalmente duas instâncias independentes:
 * </p>
 *
 * <ul>
 * <li>Playback da trilha sonora (Songs)</li>
 * <li>Playback do som ambiente (Ambience)</li>
 * </ul>
 *
 * Dessa forma ambos podem executar simultaneamente sem qualquer interferência.
 */
public interface PlaybackService {

    /**
     * Define a playlist que será utilizada nas próximas reproduções.
     * <p>
     * Este método apenas altera o estado interno do serviço.
     * Nenhuma reprodução é iniciada automaticamente.
     * </p>
     *
     * @param playlist lista contendo os caminhos dos arquivos de áudio.
     */
    void setPlaylist(List<String> playlist);

    /**
     * Inicia a reprodução contínua da playlist atualmente configurada.
     * <p>
     * A escolha da próxima faixa é realizada pela
     * {@link PlaybackStrategy} configurada.
     * </p>
     */
    void play();

    /**
     * Interrompe imediatamente a reprodução.
     * <p>
     * Após este método retornar, nenhuma faixa continuará sendo reproduzida.
     * </p>
     */
    void stop();

    /**
     * Informa se existe uma reprodução em andamento.
     *
     * @return true caso exista reprodução ativa; false caso contrário.
     */
    boolean isPlaying();

}