package br.org.tabletoprpg.soundtrack.view.swing;

import java.awt.CardLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import br.org.tabletoprpg.soundtrack.controller.Command;
import br.org.tabletoprpg.soundtrack.controller.CommandDispatcher;
import br.org.tabletoprpg.soundtrack.controller.result.StatusResult;

/**
 * Janela principal da aplicação.
 *
 * Existe apenas uma instância desta classe durante toda a execução.
 *
 * Responsabilidades:
 *
 * • criar as telas da aplicação;
 * • registrar as telas no CardLayout;
 * • decidir qual tela deve permanecer visível;
 * • solicitar a atualização das telas.
 *
 * Esta classe não possui qualquer regra de negócio.
 */
public class MainWindow extends JFrame {

    /**
     * Identificadores utilizados pelo CardLayout.
     */
    public static final String SELECTION_SCREEN = "selection";

    public static final String PLAYBACK_SCREEN = "playback";

    /**
     * Dispatcher utilizado por toda a View.
     */
    private final CommandDispatcher dispatcher;

    /**
     * Responsável pela troca das telas.
     */
    private final CardLayout cardLayout;

    /**
     * Painel raiz da aplicação.
     */
    private final JPanel rootPanel;

    /**
     * Tela inicial.
     */
    private final SelectionScreen selectionScreen;

    /**
     * Tela principal.
     */
    private final PlaybackScreen playbackScreen;

    /**
     * Constrói a janela principal.
     */
    public MainWindow(CommandDispatcher dispatcher) {

        super("Tabletop RPG Soundtrack");

        this.dispatcher = dispatcher;

        // ----------------------------------------------------------
        // Configuração básica da janela.
        // ----------------------------------------------------------

        setDefaultCloseOperation(EXIT_ON_CLOSE);

        setSize(1000, 700);

        setLocationRelativeTo(null);

        // ----------------------------------------------------------
        // Configuração do CardLayout.
        // ----------------------------------------------------------

        this.cardLayout = new CardLayout();

        this.rootPanel = new JPanel(cardLayout);

        // ----------------------------------------------------------
        // Criação das telas.
        //
        // Cada tela recebe o Dispatcher (para enviar Commands) e uma
        // referência para este método refresh() (para solicitar a
        // atualização completa da interface após qualquer comando).
        // ----------------------------------------------------------

        this.selectionScreen = new SelectionScreen(dispatcher, this::refresh);

        this.playbackScreen = new PlaybackScreen(dispatcher, this::refresh);

        // ----------------------------------------------------------
        // Registro das telas.
        // ----------------------------------------------------------

        this.rootPanel.add(
                selectionScreen,
                SELECTION_SCREEN);

        this.rootPanel.add(
                playbackScreen,
                PLAYBACK_SCREEN);

        setContentPane(rootPanel);
    }

    /**
     * Atualiza completamente a interface.
     *
     * Este método:
     *
     * • consulta o estado atual da aplicação;
     * • escolhe qual tela deve ficar visível;
     * • solicita que ambas as telas atualizem seus componentes;
     * • repinta a interface.
     */
    public void refresh() {

        StatusResult status = getApplicationStatus();

        updateVisibleScreen(status);

        selectionScreen.refresh(status);

        playbackScreen.refresh(status);

        rootPanel.revalidate();

        rootPanel.repaint();
    }

    /**
     * Decide qual tela deverá permanecer visível.
     */
    private void updateVisibleScreen(StatusResult status) {

        if (status.currentOst() == null) {

            cardLayout.show(
                    rootPanel,
                    SELECTION_SCREEN);

        } else {

            cardLayout.show(
                    rootPanel,
                    PLAYBACK_SCREEN);
        }
    }

    /**
     * Consulta o Controller para obter o estado atual da aplicação.
     */
    private StatusResult getApplicationStatus() {

        return (StatusResult) dispatcher.dispatch(
                new Command("STATUS"));
    }

    /**
     * Exibe a janela.
     */
    public void open() {

        SwingUtilities.invokeLater(() -> {

            refresh();

            setVisible(true);

        });
    }

    /**
     * Retorna a tela de seleção.
     */
    public SelectionScreen getSelectionScreen() {
        return selectionScreen;
    }

    /**
     * Retorna a tela principal.
     */
    public PlaybackScreen getPlaybackScreen() {
        return playbackScreen;
    }

}
