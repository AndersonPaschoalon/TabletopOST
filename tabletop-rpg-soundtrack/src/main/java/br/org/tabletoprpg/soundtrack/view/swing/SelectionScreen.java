package br.org.tabletoprpg.soundtrack.view.swing;

import java.awt.BorderLayout;
import java.awt.Font;
import java.util.Collection;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import br.org.tabletoprpg.soundtrack.controller.Command;
import br.org.tabletoprpg.soundtrack.controller.CommandDispatcher;
import br.org.tabletoprpg.soundtrack.controller.result.StatusResult;
import br.org.tabletoprpg.soundtrack.controller.result.StringListResult;
import br.org.tabletoprpg.soundtrack.exception.TabletopExeption;

/**
 * Tela exibida quando nenhuma OST estiver carregada.
 *
 * Responsabilidades:
 *
 * • executar a Query LIST_OSTS;
 * • construir dinamicamente os botões das OSTs;
 * • disparar SET_OST;
 * • solicitar (via callback) a troca de tela ao MainWindow.
 *
 * Não acessa diretamente nenhum Service.
 */
public class SelectionScreen extends JPanel {

    private final CommandDispatcher dispatcher;

    /**
     * Callback executado após qualquer comando, para que o MainWindow
     * atualize toda a interface e decida qual tela deve ficar visível.
     */
    private final Runnable onChange;

    /**
     * Painel onde os botões das OSTs são inseridos dinamicamente.
     */
    private final JPanel ostListPanel;

    public SelectionScreen(CommandDispatcher dispatcher, Runnable onChange) {

        this.dispatcher = dispatcher;
        this.onChange = onChange;

        setLayout(new BorderLayout(10, 10));
        setBorder(javax.swing.BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("Tabletop RPG Soundtrack", SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 22f));
        add(title, BorderLayout.NORTH);

        this.ostListPanel = new JPanel();
        this.ostListPanel.setLayout(new BoxLayout(ostListPanel, BoxLayout.Y_AXIS));
        add(ostListPanel, BorderLayout.CENTER);

        JButton syncButton = new JButton("Sincronizar Catálogo");
        // Não existe comando SYNC_CATALOG no CommandDispatcher atual.
        // A sincronização já ocorre implicitamente dentro de SET_OST
        // (via CatalogService). Botão mantido apenas como placeholder
        // visual da especificação, desabilitado até existir um comando
        // dedicado no Controller.
        syncButton.setEnabled(false);
        add(syncButton, BorderLayout.SOUTH);

        loadOsts();
    }

    /**
     * Executa LIST_OSTS e reconstrói os botões dinamicamente.
     */
    private void loadOsts() {

        ostListPanel.removeAll();

        Collection<String> osts;

        try {
            StringListResult result = (StringListResult) dispatcher.dispatch(
                    new Command("LIST_OSTS"));
            osts = result.values();
        } catch (TabletopExeption ex) {
            showErrorState(ex.getMessage());
            return;
        } catch (RuntimeException ex) {
            showErrorState("Erro inesperado ao listar OSTs: " + ex.getMessage());
            return;
        }

        for (String ostName : osts) {

            JButton button = new JButton(ostName);
            button.setAlignmentX(CENTER_ALIGNMENT);
            button.addActionListener(e -> selectOst(ostName));

            ostListPanel.add(Box.createVerticalStrut(8));
            ostListPanel.add(button);
        }

        ostListPanel.revalidate();
        ostListPanel.repaint();
    }

    private void showErrorState(String message) {

        JLabel errorLabel = new JLabel(
                "<html><body style='width: 300px; text-align: center;'>"
                        + "⚠ " + message
                        + "</body></html>",
                SwingConstants.CENTER);
        errorLabel.setAlignmentX(CENTER_ALIGNMENT);
        errorLabel.setForeground(java.awt.Color.RED);

        ostListPanel.add(Box.createVerticalStrut(20));
        ostListPanel.add(errorLabel);

        ostListPanel.revalidate();
        ostListPanel.repaint();
    }

    /**
     * Dispara SET_OST <nome> e solicita a atualização/troca de tela.
     */
    private void selectOst(String ostName) {

        try {
            dispatcher.dispatch(new Command("SET_OST", ostName));
        } catch (TabletopExeption ex) {
            SwingErrorHandler.showError(this, ex.getMessage());
            return;
        }

        onChange.run();
    }

    /**
     * Atualiza a tela. Chamado pelo MainWindow após qualquer comando.
     *
     * Reconstrói a lista de OSTs, já que esta tela só volta a ficar
     * visível quando currentOst == null (por exemplo após UNSET_OST).
     */
    public void refresh(StatusResult status) {
        loadOsts();
    }

}
