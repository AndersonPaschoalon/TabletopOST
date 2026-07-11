package br.org.tabletoprpg.soundtrack;

import br.org.tabletoprpg.soundtrack.app.AppBootstrap;
import br.org.tabletoprpg.soundtrack.app.ApplicationContext;
import br.org.tabletoprpg.soundtrack.view.cli.ConsoleView;

public class Main {

    public static void main(String[] args) {

        try (ApplicationContext context = AppBootstrap.create()) {
            ConsoleView cli = new ConsoleView(context.getDispatcher(), context.getSessionService());
            cli.start();
        } catch (Exception ex) {
            System.err.println("Fatal error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}