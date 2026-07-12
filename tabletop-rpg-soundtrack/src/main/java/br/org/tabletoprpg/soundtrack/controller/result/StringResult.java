package br.org.tabletoprpg.soundtrack.controller.result;

import java.util.Objects;

/**
 * Resultado contendo apenas uma String.
 */
public record StringResult(String value) implements Result {

    public StringResult {

        Objects.requireNonNull(value);

    }

    @Override
    public String asText() {
        return value;
    }

}