package br.org.tabletoprpg.soundtrack.controller;

public class Command {

    private final String action;
    private final String[] parameters;

    public Command(String action, String... parameters) {

        this.action = action.toUpperCase();
        this.parameters = parameters;
    }

    public String getAction() {
        return this.action;
    }

    public String[] getParameters() {
        return this.parameters;
    }
}