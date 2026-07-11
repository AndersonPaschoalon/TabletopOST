package br.org.tabletoprpg.soundtrack.view.swing;

import java.awt.CardLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 * Janela principal da aplicação.
 *
 * Esta classe representa a única JFrame existente durante toda a execução do
 * programa.
 *
 * Ela não possui nenhuma regra de negócio.
 *
 * Sua única responsabilidade é:
 *
 * - criar a janela;
 * - registrar as telas da aplicação;
 * - alternar entre elas.
 *
 * Atualmente existem apenas duas telas:
 *
 * - seleção de OST;
 * - controle da reprodução.
 *
 * A troca entre elas ocorre através do CardLayout.
 */
public class MainWindow extends JFrame {

    /**
     * Identificadores utilizados pelo CardLayout.
     */
    public static final String SELECTION_SCREEN = "selection";
    public static final String PLAYBACK_SCREEN = "playback";

    /**
     * Layout responsável por alternar entre as telas.
     */
    private final CardLayout cardLayout;

    /**
     * Painel principal da janela.
     *
     * Todas as telas são adicionadas neste painel.
     */
    private final JPanel rootPanel;

    /**
     * Constrói a janela principal.
     *
     * @param selectionPanel tela de seleção da OST
     * @param playbackPanel  tela principal de reprodução
     */
    public MainWindow(
            JPanel selectionPanel,
            JPanel playbackPanel) {

        super("Tabletop RPG Soundtrack");

        // -------------------------------------------------------------
        // Configuração básica da janela.
        // -------------------------------------------------------------

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setSize(1000, 700);

        setLocationRelativeTo(null);

        // -------------------------------------------------------------
        // Configuração do CardLayout.
        // -------------------------------------------------------------

        this.cardLayout = new CardLayout();

        this.rootPanel = new JPanel(cardLayout);

        // -------------------------------------------------------------
        // Registro das telas.
        // -------------------------------------------------------------

        this.rootPanel.add(
                selectionPanel,
                SELECTION_SCREEN);

        this.rootPanel.add(
                playbackPanel,
                PLAYBACK_SCREEN);

        // -------------------------------------------------------------
        // O painel principal passa a ser o conteúdo da janela.
        // -------------------------------------------------------------

        setContentPane(rootPanel);
    }

    /**
     * Exibe a tela de seleção de OST.
     */
    public void showSelectionScreen() {

        this.cardLayout.show(
                this.rootPanel,
                SELECTION_SCREEN);

        refresh();
    }

    /**
     * Exibe a tela principal.
     */
    public void showPlaybackScreen() {

        this.cardLayout.show(
                this.rootPanel,
                PLAYBACK_SCREEN);

        refresh();
    }

    /**
     * Atualiza a interface gráfica.
     *
     * Deve ser chamado sempre que ocorrer uma troca de tela.
     */
    public void refresh() {

        this.rootPanel.revalidate();

        this.rootPanel.repaint();
    }

    /**
     * Exibe a janela.
     *
     * Este método garante que a operação seja executada na Event Dispatch
     * Thread do Swing.
     */
    public void open() {

        SwingUtilities.invokeLater(() -> setVisible(true));
    }

}
