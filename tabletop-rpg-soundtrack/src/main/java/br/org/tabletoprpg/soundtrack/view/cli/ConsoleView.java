package br.org.tabletoprpg.soundtrack.view.cli;

import br.org.tabletoprpg.soundtrack.controller.Command;
import br.org.tabletoprpg.soundtrack.controller.CommandDispatcher;
import br.org.tabletoprpg.soundtrack.service.session.SessionService;

public class ConsoleView {

    private final CommandDispatcher dispatcher;
    private final SessionService sessionService;
    private final ConsolePrompt prompt;

    public ConsoleView(CommandDispatcher dispatcher, SessionService sessionService) {
        this.dispatcher = dispatcher;
        this.sessionService = sessionService;
        this.prompt = new ConsolePrompt();
    }

    public void start() {
        prompt.printWelcome();
        while (true) {
            String line = prompt.readString(buildPromptText());

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
                String message = dispatcher.dispatch(command);

                if (message != null) {
                    prompt.printSuccess(message);
                }
            } catch (Exception ex) {
                prompt.printError(ex.getMessage());
            }
        }

        prompt.close();
    }

    /**
     * Monta o prompt exibindo a OST e o tema atualmente selecionados,
     * para que o usuário nunca perca a referência de onde está.
     *
     * Ex: "[dnd/forest]> " ou "[nenhuma OST]> "
     */
    private String buildPromptText() {
        String ost = sessionService.getCurrentOstName();

        if (ost == null) {
            return "[sem OST]> ";
        }

        String theme = sessionService.getCurrentThemeName();
        return "[" + ost + "/" + theme + "]> ";
    }
}
