package br.org.tabletoprpg.soundtrack.model;

import java.util.Collection;

import com.google.gson.annotations.SerializedName;

public record Theme(
        String name,

        @SerializedName("songs") //o nome no json é songs, mas aqui está songTracks
        Collection<String> songTracks,

        @SerializedName("ambience") //o nome no manifest.json é ambience, mas aqui está songTracks
        Collection<String> ambienceTracks,
        
        Collection<String> images) {
}
