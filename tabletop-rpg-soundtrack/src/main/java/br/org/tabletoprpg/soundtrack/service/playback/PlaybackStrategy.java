package br.org.tabletoprpg.soundtrack.service.playback;

import java.util.List;

public interface PlaybackStrategy {

    String nextTrack(List<String> playlist);

}