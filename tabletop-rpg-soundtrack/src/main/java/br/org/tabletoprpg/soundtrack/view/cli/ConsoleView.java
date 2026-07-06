package br.org.tabletoprpg.soundtrack.view.cli;

import java.util.Scanner;

import br.org.tabletoprpg.soundtrack.controller.Command;
import br.org.tabletoprpg.soundtrack.controller.CommandDispatcher;

public class ConsoleView {

    private final Scanner scanner;
    private final CommandDispatcher dispatcher;

    public ConsoleView(CommandDispatcher dispatcher) {

        this.dispatcher = dispatcher;
        this.scanner = new Scanner(System.in);
    }

    public void start() {

        System.out.println("=== Tabletop RPG Soundtrack ===");
        System.out.println("Digite HELP para ajuda.");
        System.out.println();

        while (true) {

            System.out.print("> ");

            String line = scanner.nextLine().trim();

            if (line.isBlank()) {
                continue;
            }

            if (line.equalsIgnoreCase("exit")) {
                break;
            }

            try {

                Command command = CommandParser.parse(line);

                dispatcher.dispatch(command);

            } catch (Exception ex) {

                System.out.println("Erro: " + ex.getMessage());
            }
        }

        scanner.close();
    }
}