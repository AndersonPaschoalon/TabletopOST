package br.org.tabletoprpg.soundtrack;

import br.org.tabletoprpg.soundtrack.app.AppBootstrap;
import br.org.tabletoprpg.soundtrack.app.ApplicationContext;
import br.org.tabletoprpg.soundtrack.view.cli.ConsoleView;
import br.org.tabletoprpg.soundtrack.view.swing.MainWindow;

public class Main {

    public static void main(String[] args) {

        if (isCliMode(args)) {

            // Modo CLI: síncrono, então o try-with-resources fecha o
            // ApplicationContext corretamente assim que cli.start() retornar
            // (ao digitar EXIT).
            try (ApplicationContext context = AppBootstrap.create()) {
                startCli(context);
            } catch (Exception ex) {
                System.err.println("Fatal error: " + ex.getMessage());
                ex.printStackTrace();
            }

        } else {

            // Modo GUI: a janela Swing roda de forma assíncrona (Event
            // Dispatch Thread). Por isso NÃO usamos try-with-resources
            // aqui — main() precisa retornar imediatamente após abrir a
            // janela, e o ApplicationContext só deve ser fechado quando
            // o usuário efetivamente fechar a janela.
            ApplicationContext context = AppBootstrap.create();
            startGui(context);
        }
    }

    /**
     * Verifica se a aplicação deve iniciar em modo CLI.
     *
     * Uso: java -jar app.jar --cli
     */
    private static boolean isCliMode(String[] args) {

        for (String arg : args) {
            if (arg.equalsIgnoreCase("--cli") || arg.equalsIgnoreCase("cli")) {
                return true;
            }
        }

        return false;
    }

    private static void startCli(ApplicationContext context) {

        ConsoleView cli = new ConsoleView(
                context.getDispatcher(),
                context.getSessionService());

        cli.start();
    }

    /**
     * Inicia a GUI Swing.
     *
     * A janela abre de forma assíncrona (SwingUtilities.invokeLater
     * dentro de MainWindow.open()). Por isso o encerramento do
     * ApplicationContext não pode depender de main() retornar — ele é
     * feito explicitamente quando a janela for fechada pelo usuário.
     */
    private static void startGui(ApplicationContext context) {

        MainWindow window = new MainWindow(context.getDispatcher());

        // MainWindow já está configurada com EXIT_ON_CLOSE (encerra a
        // JVM). Este listener roda antes disso, garantindo que os
        // recursos do ApplicationContext (ex.: threads de audio,
        // conexões) sejam liberados corretamente.
        window.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                try {
                    context.close();
                } catch (Exception ex) {
                    System.err.println("Erro ao encerrar aplicação: " + ex.getMessage());
                }
            }
        });

        window.open();
    }
}