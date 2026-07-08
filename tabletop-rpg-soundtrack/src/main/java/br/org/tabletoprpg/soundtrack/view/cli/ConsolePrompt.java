package br.org.tabletoprpg.soundtrack.view.cli;

import java.util.Scanner;

public class ConsolePrompt {

    private final Scanner scanner;

    public ConsolePrompt() {
        this.scanner = new Scanner(System.in);
    }

    public String readString(String promptText) {
        System.out.print(promptText);
        return scanner.nextLine().trim();
    }

    public void printWelcome() {
        System.out.println("\n\u001B[36m===========================================\u001B[0m");
        System.out.println("\u001B[1;36m       Tabletop RPG Soundtrack - CLI       \u001B[0m");
        System.out.println("\u001B[36m===========================================\u001B[0m\n");
        System.out.println("Bem-vindo! Digite \u001B[33mHELP\u001B[0m para ver os comandos ou \u001B[33mEXIT\u001B[0m para sair.\n");
    }

    public void printHelp() {
        System.out.println("\n\u001B[1m--- Comandos Disponíveis ---\u001B[0m\n");

        System.out.println("\u001B[36m[ Trilha Sonora (OST) ]\u001B[0m");
        printCommandHelp("SET_OST <nome>", "Seleciona a OST especificada.");
        printCommandHelp("UNSET_OST", "Desseleciona a OST atual.");
        System.out.println();

        System.out.println("\u001B[36m[ Temas ]\u001B[0m");
        printCommandHelp("SET_THEME <nome>", "Seleciona o tema especificado.");
        printCommandHelp("UNSET_THEME", "Desseleciona o tema atual.");
        System.out.println();

        System.out.println("\u001B[36m[ Reprodução ]\u001B[0m");
        printCommandHelp("PLAY_SONG <nome>", "Reproduz a música especificada.");
        printCommandHelp("PAUSE_SONG", "Pausa a música atual.");
        System.out.println();

        System.out.println("\u001B[36m[ Sistema ]\u001B[0m");
        printCommandHelp("HELP", "Exibe este menu de ajuda.");
        printCommandHelp("EXIT", "Sai do aplicativo.");
        System.out.println();
    }

    private void printCommandHelp(String command, String description) {
        System.out.printf("  \u001B[33m%-22s\u001B[0m - %s%n", command, description);
    }

    public void printSuccess(String message) {
        System.out.println("\u001B[32m✔ " + message + "\u001B[0m");
    }

    public void printError(String message) {
        System.out.println("\u001B[31m❌ Erro: " + message + "\u001B[0m");
    }

    public void printWarning(String message) {
        System.out.println("\u001B[33m⚠️ " + message + "\u001B[0m");
    }
    
    public void println(String message) {
        System.out.println(message);
    }

    public void clearScreen() {
        try {
            System.out.print("\033[H\033[2J");
            System.out.flush();
        } catch (Exception e) {
            for (int i = 0; i < 50; i++) {
                System.out.println();
            }
        }
        printWelcome();
    }

    public void close() {
        scanner.close();
    }
}