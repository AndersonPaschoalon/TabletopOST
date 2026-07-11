package br.org.tabletoprpg.soundtrack.audio;


/**
 * Factory responsável por selecionar automaticamente
 * a implementação de AudioPlayer compatível com
 * a plataforma em execução.
 *
 * Nesta primeira versão sempre retornamos um
 * DummyAudioPlayer.
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

/*
 * public static AudioPlayer create() {
 * 
 * String os = System.getProperty("os.name").toLowerCase();
 * 
 * if (os.contains("android")) {
 * return new AndroidAudioPlayer();
 * }
 * 
 * if (os.contains("linux")) {
 * return new LinuxAudioPlayer();
 * }
 * 
 * if (os.contains("windows")) {
 * return new WindowsAudioPlayer();
 * }
 * 
 * return new DummyAudioPlayer();
 * }
 * 
 */