package br.org.tabletoprpg.soundtrack.controller.result;

/**
 * Resultado produzido por uma operação do Controller.
 *
 * Todo resultado deve ser capaz de produzir uma representação textual,
 * permitindo que a CLI simplesmente imprima o retorno recebido.
 *
 * Interfaces gráficas podem utilizar o tipo concreto para acessar
 * informações estruturadas.
 */
public interface Result {

    /**
     * Retorna uma representação textual do resultado.
     */
    String asText();

}