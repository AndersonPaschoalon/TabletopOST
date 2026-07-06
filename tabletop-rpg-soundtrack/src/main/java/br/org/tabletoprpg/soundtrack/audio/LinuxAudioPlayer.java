package br.org.tabletoprpg.soundtrack.audio;

import java.io.IOException;

public class LinuxAudioPlayer implements AudioPlayer {

    private Process process;

    @Override
    public void play(String file) {

        stop();

        try {

            ProcessBuilder builder = new ProcessBuilder(
                    "ffplay",
                    "-nodisp",
                    "-autoexit",
                    file);

            this.process = builder.start();

            this.process.waitFor();

            this.process = null;

        } catch (IOException e) {

            throw new RuntimeException(
                    "Unable to start audio player.",
                    e);

        } catch (InterruptedException e) {

            Thread.currentThread().interrupt();

            throw new RuntimeException(
                    "Audio playback interrupted.",
                    e);
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