package br.org.tabletoprpg.soundtrack.view.cli;

import java.util.Arrays;

import br.org.tabletoprpg.soundtrack.controller.Command;

public final class CommandParser {

    private CommandParser() {
    }

    public static Command parse(String line) {

        if (line == null || line.isBlank()) {
            return null;
        }

        String[] tokens = line.trim().split("\\s+");

        String action = tokens[0]
                .trim()
                .replace('-', '_')
                .toUpperCase();

        String[] parameters = Arrays.copyOfRange(tokens, 1, tokens.length);

        return new Command(action, parameters);
    }

}