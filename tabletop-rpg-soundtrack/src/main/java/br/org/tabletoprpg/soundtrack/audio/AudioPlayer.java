package br.org.tabletoprpg.soundtrack.audio;

public interface AudioPlayer {

    void play(String audioFile);

    void stop();

    boolean isPlaying();

}