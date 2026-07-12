package br.org.tabletoprpg.soundtrack.controller;

import java.util.Objects;

import br.org.tabletoprpg.soundtrack.controller.result.Result;

public interface CommandHandler {

    Result handle(Command command);

    default void requireParamCount(
            String[] params,
            int expected,
            String commandName) {

        if (params == null) {
            params = new String[0];
        }

        if (params.length != expected) {
            throw new RuntimeException(
                    String.format(
                            "Command '%s' expects %d parameter(s), but received %d.",
                            commandName,
                            expected,
                            params.length));
        }
    }

    default String extractString(
            String[] params,
            int index,
            String parameterName) {

        Objects.requireNonNull(params);

        try {

            String value = params[index];

            if (value == null || value.isBlank()) {
                throw new RuntimeException(
                        "Missing parameter: " + parameterName);
            }

            return value;

        } catch (ArrayIndexOutOfBoundsException ex) {

            throw new RuntimeException(
                    "Missing parameter: " + parameterName);
        }
    }

}