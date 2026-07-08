package br.org.tabletoprpg.soundtrack.service.loader;

import java.util.List;

import br.org.tabletoprpg.soundtrack.model.Ost;
import br.org.tabletoprpg.soundtrack.model.Theme;

public class MockOstLoader extends OstLoader {

    @Override
    public Ost load(String ostName) {

        Theme dungeon = new Theme(
                "dungeon",
                List.of(
                        "dungeon/songs/file1.mp3",
                        "dungeon/songs/file2.mp3",
                        "dungeon/songs/file3.mp3"),
                List.of(
                        "dungeon/ambience/file1.mp3",
                        "dungeon/ambience/file2.mp3",
                        "dungeon/ambience/file3.mp3"),
                List.of(
                        "dungeon/images/file1.png",
                        "dungeon/images/file2.png",
                        "dungeon/images/file3.png"));

        Theme forest = new Theme(
                "forest",
                List.of(
                        "forest/songs/file1.mp3",
                        "forest/songs/file2.mp3",
                        "forest/songs/file3.mp3"),
                List.of(
                        "forest/ambience/file1.mp3",
                        "forest/ambience/file2.mp3",
                        "forest/ambience/file3.mp3"),
                List.of(
                        "forest/images/file1.png",
                        "forest/images/file2.png",
                        "forest/images/file3.png"));

        Theme village = new Theme(
                "village",
                List.of(
                        "village/songs/file1.mp3",
                        "village/songs/file2.mp3",
                        "village/songs/file3.mp3"),
                List.of(
                        "village/ambience/file1.mp3",
                        "village/ambience/file2.mp3",
                        "village/ambience/file3.mp3"),
                List.of(
                        "village/images/file1.png",
                        "village/images/file2.png",
                        "village/images/file3.png"));

        return new Ost(
                "dnd",
                List.of(
                        dungeon,
                        forest,
                        village));
    }

}