package br.org.tabletoprpg.soundtrack.controller.result;

import java.util.Collection;
import java.util.StringJoiner;

/**
 * Resultado contendo uma lista de Strings.
 */
public record StringListResult(
        Collection<String> values)
        implements Result {

    @Override
    public String asText() {

        StringJoiner joiner = new StringJoiner(System.lineSeparator());

        for (String value : values) {
            joiner.add(value);
        }

        return joiner.toString();
    }

}