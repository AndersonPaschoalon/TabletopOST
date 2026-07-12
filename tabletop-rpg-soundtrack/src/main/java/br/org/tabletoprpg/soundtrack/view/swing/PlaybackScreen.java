package br.org.tabletoprpg.soundtrack.view.swing;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import br.org.tabletoprpg.soundtrack.controller.Command;
import br.org.tabletoprpg.soundtrack.controller.CommandDispatcher;
import br.org.tabletoprpg.soundtrack.controller.result.StatusResult;

/**
 * Tela principal da aplicação.
 *
 * Responsável por:
 *
 * • exibir OST atual;
 * • exibir tema atual;
 * • integrar controles de reprodução;
 * • integrar ThemeMenu;
 * • integrar ImageViewer.
 *
 * Toda alteração ocorre através do CommandDispatcher. Esta classe não
 * possui qualquer regra de negócio.
 */
public class PlaybackScreen extends JPanel {

    private final CommandDispatcher dispatcher;

    private final Runnable onChange;

    private final JLabel ostLabel;

    private final JLabel themeLabel;

    private final ImageViewer imageViewer;

    /**
     * Estado puramente visual do botão global Play/Stop. Não consulta o
     * estado interno do PlaybackService, conforme especificação.
     */
    private boolean globalPlaying = false;

    private JButton globalPlayStopButton;

    public PlaybackScreen(CommandDispatcher dispatcher, Runnable onChange) {

        this.dispatcher = dispatcher;
        this.onChange = onChange;

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // ------------------------------------------------------------
        // Barra superior: menu hambúrguer + OST atual + Tema atual.
        // ------------------------------------------------------------

        JPanel topBar = new JPanel(new GridLayout(1, 3));

        ThemeMenu themeMenu = new ThemeMenu(dispatcher, onChange);
        JPanel westWrapper = new JPanel(new FlowLayout(FlowLayout.LEFT));
        westWrapper.add(themeMenu);
        topBar.add(westWrapper);

        this.ostLabel = new JLabel("", SwingConstants.CENTER);
        this.ostLabel.setFont(ostLabel.getFont().deriveFont(Font.BOLD, 16f));
        topBar.add(ostLabel);

        this.themeLabel = new JLabel("", SwingConstants.CENTER);

        JButton changeOstButton = new JButton("↩ Trocar OST");
        changeOstButton.setToolTipText("Volta para a tela de seleção de OST");
        changeOstButton.addActionListener(e -> SwingErrorHandler.run(this, () -> {
            dispatcher.dispatch(new Command("UNSET_OST"));
            onChange.run();
        }));

        JPanel eastPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        eastPanel.add(themeLabel);
        eastPanel.add(changeOstButton);

        topBar.add(eastPanel);

        add(topBar, BorderLayout.NORTH);

        // ------------------------------------------------------------
        // Centro: ImageViewer + controles de reprodução.
        // ------------------------------------------------------------

        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));

        this.imageViewer = new ImageViewer(dispatcher);
        centerPanel.add(imageViewer, BorderLayout.CENTER);

        JPanel controls = new JPanel();
        controls.setLayout(new BoxLayout(controls, BoxLayout.Y_AXIS));

        controls.add(buildTrackControlGroup(
                "Music",
                "PLAY_SONG", "PAUSE_SONG", "PREVIOUS_SONG", "NEXT_SONG"));

        controls.add(buildTrackControlGroup(
                "Ambience",
                "PLAY_AMBIENCE", "PAUSE_AMBIENCE", "PREVIOUS_AMBIENCE", "NEXT_AMBIENCE"));

        controls.add(buildGlobalControlGroup());

        centerPanel.add(controls, BorderLayout.SOUTH);

        add(centerPanel, BorderLayout.CENTER);
    }

    /**
     * Constrói um grupo de controles (Music ou Ambience), com os botões
     * Play, Stop, Previous, Next e um Shuffle placeholder.
     */
    private JPanel buildTrackControlGroup(
            String title,
            String playCommand,
            String stopCommand,
            String previousCommand,
            String nextCommand) {

        JPanel group = new JPanel(new BorderLayout());
        group.setBorder(BorderFactory.createTitledBorder(title));

        JPanel buttons = new JPanel(new GridLayout(1, 5, 5, 5));

        buttons.add(commandButton("\u25C0", previousCommand)); // ◀
        buttons.add(commandButton("\u25B6", playCommand));     // ▶
        buttons.add(commandButton("\u25A0", stopCommand));     // ■

        JButton shuffle = new JButton("\uD83D\uDD00"); // 🔀
        shuffle.setEnabled(false); // placeholder — sem comando SHUFFLE no Controller
        buttons.add(shuffle);

        buttons.add(commandButton("\u25B6\u25B6", nextCommand)); // ▶▶ (next)

        group.add(buttons, BorderLayout.CENTER);

        return group;
    }

    /**
     * Constrói o grupo de controle global (Theme anterior/próximo + Play/Stop).
     */
    private JPanel buildGlobalControlGroup() {

        JPanel group = new JPanel(new BorderLayout());
        group.setBorder(BorderFactory.createTitledBorder("Global"));

        JPanel buttons = new JPanel(new GridLayout(1, 3, 5, 5));

        // Placeholders: não existem comandos PREVIOUS_THEME / NEXT_THEME
        // no CommandDispatcher atual. Botões ficam desabilitados por ora.
        JButton previousTheme = new JButton("\u25C0 Theme");
        previousTheme.setEnabled(false);
        buttons.add(previousTheme);

        this.globalPlayStopButton = new JButton("\u25B6 / \u25A0");
        this.globalPlayStopButton.addActionListener(e -> toggleGlobalPlayback());
        buttons.add(globalPlayStopButton);

        JButton nextTheme = new JButton("Theme \u25B6");
        nextTheme.setEnabled(false);
        buttons.add(nextTheme);

        group.add(buttons, BorderLayout.CENTER);

        return group;
    }

    /**
     * Alterna entre PLAY_BOTH e PAUSE_BOTH a cada clique, usando apenas
     * estado visual interno da tela (não consulta o PlaybackService).
     */
    private void toggleGlobalPlayback() {

        boolean nextState = !globalPlaying;

        String command = nextState ? "PLAY_BOTH" : "PAUSE_BOTH";

        try {
            dispatcher.dispatch(new Command(command));
        } catch (br.org.tabletoprpg.soundtrack.exception.TabletopExeption ex) {
            SwingErrorHandler.showError(this, ex.getMessage());
            return;
        }

        // Só atualiza o estado visual se o comando não tiver falhado.
        globalPlaying = nextState;

        onChange.run();
    }

    /**
     * Cria um botão que dispara o Command informado (sem parâmetros) e
     * solicita a atualização da interface após a execução.
     */
    private JButton commandButton(String label, String commandName) {

        JButton button = new JButton(label);

        button.addActionListener(e -> SwingErrorHandler.run(this, () -> {
            dispatcher.dispatch(new Command(commandName));
            onChange.run();
        }));

        return button;
    }

    /**
     * Atualiza a tela. Chamado pelo MainWindow após qualquer comando.
     */
    public void refresh(StatusResult status) {

        ostLabel.setText(status.currentOst() == null ? "" : status.currentOst());
        themeLabel.setText(status.currentTheme() == null ? "" : status.currentTheme());

        globalPlayStopButton.setText(globalPlaying ? "\u25A0 Stop" : "\u25B6 Play");

        imageViewer.onThemeChanged(status.currentTheme());
    }

}
