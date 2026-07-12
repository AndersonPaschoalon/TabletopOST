package br.org.tabletoprpg.soundtrack.view.swing;

import java.util.Collection;

import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import br.org.tabletoprpg.soundtrack.controller.Command;
import br.org.tabletoprpg.soundtrack.controller.CommandDispatcher;
import br.org.tabletoprpg.soundtrack.controller.result.StringListResult;

/**
 * Responsável pelo menu "hambúrguer" da PlaybackScreen.
 *
 * Fluxo:
 *
 * Clique → LIST_THEMES → PopupMenu → SET_THEME
 */
public class ThemeMenu extends JButton {

    private final CommandDispatcher dispatcher;

    private final Runnable onChange;

    public ThemeMenu(CommandDispatcher dispatcher, Runnable onChange) {

        super("\u2630"); // ☰

        this.dispatcher = dispatcher;
        this.onChange = onChange;

        addActionListener(e -> openMenu());
    }

    /**
     * Executa LIST_THEMES e exibe o popup com os temas da OST atual.
     */
    private void openMenu() {

        StringListResult result = (StringListResult) dispatcher.dispatch(
                new Command("LIST_THEMES"));

        Collection<String> themes = result.values();

        JPopupMenu popup = new JPopupMenu();

        for (String themeName : themes) {

            JMenuItem item = new JMenuItem(themeName);
            item.addActionListener(e -> selectTheme(themeName));
            popup.add(item);
        }

        popup.show(this, 0, this.getHeight());
    }

    /**
     * Dispara SET_THEME <nome> e solicita a atualização da interface.
     */
    private void selectTheme(String themeName) {

        dispatcher.dispatch(new Command("SET_THEME", themeName));

        onChange.run();
    }

}
