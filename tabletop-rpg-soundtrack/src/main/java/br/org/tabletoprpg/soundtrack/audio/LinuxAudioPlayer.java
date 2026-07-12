package br.org.tabletoprpg.soundtrack.audio;


import java.io.IOException;



public class LinuxAudioPlayer implements AudioPlayer {


    private volatile Process process;


    public LinuxAudioPlayer(){
        this.process = null;


    }

    @Override
    public void play(String fileDir) {

        this.stop();

        try {

            ProcessBuilder builder = new ProcessBuilder(
                    "setsid",
                    "ffplay",
                    "-nodisp",
                    "-autoexit",
                    "-loglevel", "quiet",
                    fileDir);

            builder.redirectOutput(ProcessBuilder.Redirect.DISCARD);
            builder.redirectError(ProcessBuilder.Redirect.DISCARD);

            this.process = builder.start();

            this.process.waitFor();

            this.process = null;

            


        } catch (IOException e) {

            throw new RuntimeException(
                    "Unable to start audio player.",
                    e);

        } catch (InterruptedException e) {
            if (this.process != null) {
                this.process.destroy();
                this.process = null;
            }

            Thread.currentThread().interrupt();
        }
        
        


    }

    @Override
    public void stop() {

        if (this.process != null) {

            this.process.destroy();

            this.process = null;
        }

        


    }

    @Override
    public boolean isPlaying() {

        return this.process != null && this.process.isAlive();

        

    }
}