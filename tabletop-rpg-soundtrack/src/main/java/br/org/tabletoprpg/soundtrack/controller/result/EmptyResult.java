package br.org.tabletoprpg.soundtrack.controller.result;

/**
 * Resultado utilizado quando um comando não possui informações para retornar.
 */
public final class EmptyResult implements Result {

    public static final EmptyResult INSTANCE = new EmptyResult();

    private EmptyResult() {
    }

    @Override
    public String asText() {
        return "";
    }

}