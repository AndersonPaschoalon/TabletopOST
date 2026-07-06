package br.org.tabletoprpg.soundtrack.exception;

public class ConnectionError extends RuntimeException {

    public ConnectionError(String message) {
        super(message);
    }

}