package br.org.tabletoprpg.soundtrack.model;

import java.util.List;

public record Ost(
        String name,
        List<Theme> themes) {
}