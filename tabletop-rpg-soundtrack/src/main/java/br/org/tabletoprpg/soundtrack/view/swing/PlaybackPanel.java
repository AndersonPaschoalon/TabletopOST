package br.org.tabletoprpg.soundtrack.view.swing.panels;

import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JPanel;

import br.org.tabletoprpg.soundtrack.controller.Command;
import br.org.tabletoprpg.soundtrack.controller.CommandDispatcher;

/**
 * Painel responsável pelos controles de reprodução.
 *
 * Este componente não possui nenhuma regra de negócio.
 *
 * Sua única responsabilidade é transformar os cliques dos botões
 * em objetos Command e enviá-los ao CommandDispatcher.
 *
 * A lógica de reprodução permanece inteiramente na camada Controller
 * e nos Services.
 */
public class PlaybackPanel extends JPanel {

    private final CommandDispatcher dispatcher;

    /*
     * Botões do player de música.
     */
    private final JButton playSongButton;
    private final JButton pauseSongButton;

    /*
     * Botões do player de ambiência.
     */
    private final JButton playAmbienceButton;
    private final JButton pauseAmbienceButton;

    /*
     * Controle simultâneo.
     */
    private final JButton playBothButton;
    private final JButton pauseBothButton;

    public PlaybackPanel(CommandDispatcher dispatcher) {

        this.dispatcher = dispatcher;

        /*
         * Utilizamos um FlowLayout simples.
         *
         * No futuro poderemos trocar facilmente por GridBagLayout
         * caso desejemos uma aparência mais refinada.
         */
        setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));

        playSongButton = new JButton("▶ Música");
        pauseSongButton = new JButton("■ Música");

        playAmbienceButton = new JButton("▶ Ambiente");
        pauseAmbienceButton = new JButton("■ Ambiente");

        playBothButton = new JButton("▶ Ambos");
        pauseBothButton = new JButton("■ Ambos");

        add(playSongButton);
        add(pauseSongButton);

        add(playAmbienceButton);
        add(pauseAmbienceButton);

        add(playBothButton);
        add(pauseBothButton);

        registerEvents();
    }

    /**
     * Registra todos os listeners dos botões.
     */
    private void registerEvents() {

        playSongButton.addActionListener(e -> dispatcher.dispatch(new Command(
                "PLAY_SONG",
                new String[0])));

        pauseSongButton.addActionListener(e -> dispatcher.dispatch(new Command(
                "PAUSE_SONG",
                new String[0])));

        playAmbienceButton.addActionListener(e -> dispatcher.dispatch(new Command(
                "PLAY_AMBIENCE",
                new String[0])));

        pauseAmbienceButton.addActionListener(e -> dispatcher.dispatch(new Command(
                "PAUSE_AMBIENCE",
                new String[0])));

        playBothButton.addActionListener(e -> dispatcher.dispatch(new Command(
                "PLAY_BOTH",
                new String[0])));

        pauseBothButton.addActionListener(e -> dispatcher.dispatch(new Command(
                "PAUSE_BOTH",
                new String[0])));
    }

}