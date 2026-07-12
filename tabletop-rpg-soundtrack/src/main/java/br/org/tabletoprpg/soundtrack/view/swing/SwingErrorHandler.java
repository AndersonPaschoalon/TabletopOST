package br.org.tabletoprpg.soundtrack.view.swing;

import java.awt.Component;

import javax.swing.JOptionPane;

import br.org.tabletoprpg.soundtrack.exception.TabletopExeption;

public final class SwingErrorHandler {

    private SwingErrorHandler() {
    }

    public static void run(Component parent, Runnable action) {
        try {
            action.run();
        } catch (TabletopExeption ex) {
            showError(parent, ex.getMessage());
        } catch (RuntimeException ex) {
            showError(parent, "Erro inesperado: " + ex.getMessage());
        }
    }

    public static void showError(Component parent, String message) {
        JOptionPane.showMessageDialog(
                parent,
                message == null ? "Ocorreu um erro desconhecido." : message,
                "Erro",
                JOptionPane.ERROR_MESSAGE);
    }
}
