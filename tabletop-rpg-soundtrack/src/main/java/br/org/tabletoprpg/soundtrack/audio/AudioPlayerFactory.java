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
    public static AudioPlayer create(boolean Debug) {
        if(Debug){
            return new DummyAudioPlayer();
        }
        if (System.getProperty("os.name").startsWith("Windows")) {
            return new WindowsAudioPlayer();
        } else {
            return new LinuxAudioPlayer();
        } 
        
           
    }

}