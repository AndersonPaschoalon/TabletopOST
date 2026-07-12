package br.org.tabletoprpg.soundtrack.audio;

/**
 * Factory responsável por selecionar automaticamente
 * a implementação de AudioPlayer compatível com
 * a plataforma em execução.
 *
 * Caso a plataforma não tenha uma implementação real disponível,
 * caímos no DummyAudioPlayer para que a aplicação continue
 * funcionando (sem áudio real).
 */
public final class AudioPlayerFactory {

    private AudioPlayerFactory() {
    }

    /**
     * Cria um AudioPlayer compatível com a plataforma atual.
     */
    public static AudioPlayer create() {

        String os = System.getProperty("os.name").toLowerCase();

        if (os.contains("linux")) {
            return new LinuxAudioPlayer();
        }

        // Ainda não há implementação real para Android/Windows.
        // Utilizamos o Dummy como fallback seguro nesses casos.
        return new DummyAudioPlayer();
    }

}