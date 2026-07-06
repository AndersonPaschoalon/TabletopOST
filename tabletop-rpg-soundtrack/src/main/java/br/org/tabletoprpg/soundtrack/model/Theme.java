package br.org.tabletoprpg.soundtrack.model;

import java.util.Collection;

public record Theme(
        String name,
        Collection<String> songTracks,
        Collection<String> ambienceTracks,
        Collection<String> images) {
}
