package br.org.tabletoprpg.soundtrack.view.cli;

import br.org.tabletoprpg.soundtrack.controller.Command;
import br.org.tabletoprpg.soundtrack.controller.CommandDispatcher;

public class ConsoleView {

    private final CommandDispatcher dispatcher;
    private final ConsolePrompt prompt;

    public ConsoleView(CommandDispatcher dispatcher) {
        this.dispatcher = dispatcher;
        this.prompt = new ConsolePrompt();
    }

    public void start() {
        prompt.printWelcome();
        while (true) {
            String line = prompt.readString("> ");

            if (line.isBlank()) {
                continue;
            }

            if (line.equalsIgnoreCase("help")) {
                prompt.printHelp();
                continue;
            }

            if (line.equalsIgnoreCase("clear")) {
                prompt.clearScreen();
                continue;
            }

            if (line.equalsIgnoreCase("exit")) {
                break;
            }

            try {
                Command command = CommandParser.parse(line);
                dispatcher.dispatch(command);
            } catch (Exception ex) {
                prompt.printError(ex.getMessage());
            }
        }

        prompt.close();
    }
}