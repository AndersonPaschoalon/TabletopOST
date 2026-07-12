package br.org.tabletoprpg.soundtrack.audio;

import br.org.tabletoprpg.soundtrack.view.cli.ConsolePrompt;

/**
 * Implementação fictícia de um player de áudio.
 *
 * Esta classe NÃO reproduz áudio de verdade.
 *
 * Seu objetivo é simular o comportamento de um player real para permitir o
 * desenvolvimento e teste da arquitetura da aplicação.
 *
 * Enquanto estiver "tocando", ela imprime o nome do arquivo a cada cinco
 * segundos.
 */
public class DummyAudioPlayer implements AudioPlayer {

    /**
     * Thread responsável pela reprodução.
     *
     * Sempre existe no máximo uma thread de reprodução por player.
     */
    private Thread playbackThread;

    /**
     * Indica se existe uma reprodução em andamento.
     *
     * O modificador "volatile" garante que alterações realizadas por uma thread
     * sejam imediatamente visíveis para as demais.
     */
    private volatile boolean playing;

    /**
     * Caminho do arquivo atualmente carregado.
     */
    private String currentFile;

    public DummyAudioPlayer(){
        




    }


    /**
     * Inicia a reprodução de um arquivo.
     *
     * Caso já exista uma reprodução em andamento, ela é interrompida antes de
     * iniciar a nova.
     */
    @Override
    public synchronized void play(String file) {

        // Garante que nenhuma reprodução anterior continue executando.
        stop();

        // Guarda qual arquivo será reproduzido.
        this.currentFile = file;

        // Marca que o player está ativo.
        this.setPlayingTrue();

        // Cria uma nova thread responsável pela reprodução.
        playbackThread = new Thread(new Runnable() {

            @Override
            public void run() {
                playbackLoop();
            }

        });

        // Esta thread é encerrada automaticamente quando a aplicação termina.
        playbackThread.setDaemon(true);

        // Inicia a execução da thread.
        playbackThread.start();
    }

    /**
     * Loop principal de reprodução.
     *
     * Enquanto o player estiver ativo, o arquivo atual é exibido na tela.
     */
    private void playbackLoop() {

        while (playing) {

            ConsolePrompt.println(
                    "[DummyAudioPlayer] Playing: " + currentFile);

            try {

                // Simula cinco segundos de reprodução.
                Thread.sleep(5000);

            } catch (InterruptedException ignored) {

                // A thread foi interrompida pelo método stop().
                // Encerramos imediatamente a execução.
                break;
            }
        }

        ConsolePrompt.println(
                "[DummyAudioPlayer] Playback finished.\n");
    }

    /**
     * Interrompe completamente a reprodução.
     */
    @Override
    public synchronized void stop() {

        // Caso não exista nenhuma reprodução ativa,
        // não há nada para fazer.
        if (this.playing == false) {
            return;
        }

        // Solicita ao loop principal que termine.
        this.setPlayingFalse();

        // Caso a thread esteja dormindo no Thread.sleep(),
        // acordamos imediatamente.
        if (playbackThread != null) {

            playbackThread.interrupt();

            try {

                // Aguarda a thread terminar completamente.
                playbackThread.join();

            } catch (InterruptedException ignored) {
            }

            // Remove a referência para a thread encerrada.
            playbackThread = null;
        }

        // Remove o arquivo atualmente carregado.
        currentFile = null;
    }

    /**
     * Informa se existe uma reprodução em andamento.
     */
    @Override
    public boolean isPlaying() {
        return playing;
    }


    private void setPlayingTrue(){
        this.playing = true;
        return;
    }

    private void setPlayingFalse(){
        this.playing = false;
        return;
    }

}