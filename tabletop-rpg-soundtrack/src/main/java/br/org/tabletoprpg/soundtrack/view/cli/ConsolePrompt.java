package br.org.tabletoprpg.soundtrack.view.cli;

import java.io.IOException;

import org.jline.reader.EndOfFileException;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.UserInterruptException;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.utils.InfoCmp.Capability;

public class ConsolePrompt {
    private static volatile LineReader activeReader;

    private final Terminal terminal;
    private final LineReader reader;

    public ConsolePrompt() {
        try {
            this.terminal = TerminalBuilder.builder()
                    .system(true)
                    .build();

            this.reader = LineReaderBuilder.builder()
                    .terminal(terminal)
                    .build();

            activeReader = reader;

        } catch (IOException e) {
            throw new RuntimeException("Não foi possível inicializar o terminal.", e);
        }
    }

    public static void println(String message) {
        LineReader current = activeReader;
        if (current != null) {
            current.printAbove(message);
            return;
        }

        System.out.println(message);
    }

    public String readString(String promptText) {
        try {
            return reader.readLine(promptText).trim();
        } catch (UserInterruptException e) {
            return "";
        } catch (EndOfFileException e) {
            return "exit";
        }
    }

    public void printWelcome() {
        println("\n\u001B[36m===========================================\u001B[0m");
        println("\u001B[1;36m       Tabletop RPG Soundtrack - CLI       \u001B[0m");
        println("\u001B[36m===========================================\u001B[0m\n");
        println("Bem-vindo! Digite \u001B[33mHELP\u001B[0m para ver os comandos ou \u001B[33mEXIT\u001B[0m para sair.\n");
    }

    public void printHelp() {
        println("\n\u001B[1m--- Comandos Disponíveis ---\u001B[0m\n");

        println("\u001B[36m[ Trilha Sonora (OST) ]\u001B[0m");
        printCommandHelp("LIST_OSTS", "Lista as OSTs disponíveis.");
        printCommandHelp("SET_OST <nome>", "Seleciona a OST especificada (baixa se necessário).");
        printCommandHelp("UNSET_OST", "Desseleciona a OST atual e para a reprodução.");
        println("");

        println("\u001B[36m[ Temas ]\u001B[0m");
        printCommandHelp("LIST_THEMES", "Lista os temas da OST atual.");
        printCommandHelp("SET_THEME <nome>", "Seleciona o tema especificado.");
        printCommandHelp("UNSET_THEME", "Volta ao tema padrão da OST atual.");
        printCommandHelp("GET_CURRENT_THEME", "Mostra o tema atualmente selecionado.");
        printCommandHelp("GET_THEME_IMAGES", "Lista as imagens do tema atual.");
        printCommandHelp("GET_THEME_IMAGE <index>", "Mostra a imagem de índice informado do tema atual.");
        println("");

        println("\u001B[36m[ Reprodução ]\u001B[0m");
        printCommandHelp("PLAY_SONG", "Reproduz uma música do tema atual.");
        printCommandHelp("PAUSE_SONG", "Pausa a música atual.");
        printCommandHelp("NEXT_SONG", "Pula para a próxima música do tema atual.");
        printCommandHelp("PREVIOUS_SONG", "Volta para a música anterior do tema atual.");
        printCommandHelp("PLAY_AMBIENCE", "Reproduz o som ambiente do tema atual.");
        printCommandHelp("PAUSE_AMBIENCE", "Pausa o som ambiente atual.");
        printCommandHelp("NEXT_AMBIENCE", "Pula para o próximo som ambiente do tema atual.");
        printCommandHelp("PREVIOUS_AMBIENCE", "Volta para o som ambiente anterior do tema atual.");
        printCommandHelp("PLAY_BOTH", "Reproduz música e ambiente juntos.");
        printCommandHelp("PAUSE_BOTH", "Pausa música e ambiente juntos.");
        println("");

        println("\u001B[36m[ Sistema ]\u001B[0m");
        printCommandHelp("STATUS", "Mostra a OST, tema e reprodução atuais.");
        printCommandHelp("HELP", "Exibe este menu de ajuda.");
        printCommandHelp("CLEAR", "Limpa a tela do terminal.");
        printCommandHelp("EXIT", "Sai do aplicativo.");
        println("");
    }

    private void printCommandHelp(String command, String description) {
        println(String.format("  \u001B[33m%-22s\u001B[0m - %s", command, description));
    }

    public void printSuccess(String message) {
        println("\u001B[32m✔ " + message + "\u001B[0m");
    }

    public void printError(String message) {
        println("\u001B[31m❌ Erro: " + message + "\u001B[0m");
    }

    public void printWarning(String message) {
        println("\u001B[33m⚠️ " + message + "\u001B[0m");
    }

    public void clearScreen() {
        try {
            terminal.puts(Capability.clear_screen);
            terminal.flush();
        } catch (Exception e) {
            for (int i = 0; i < 50; i++) {
                println("");
            }
        }
        printWelcome();
    }

    public void close() {
        activeReader = null;
        try {
            terminal.close();
        } catch (IOException ignored) {
        }
    }
}
