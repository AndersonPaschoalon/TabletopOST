package br.org.tabletoprpg.soundtrack.view.swing;

import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.Timer;

import br.org.tabletoprpg.soundtrack.controller.Command;
import br.org.tabletoprpg.soundtrack.controller.CommandDispatcher;
import br.org.tabletoprpg.soundtrack.controller.result.StringListResult;

/**
 * Única classe da GUI com comportamento periódico.
 *
 * Responsabilidades:
 *
 * • executar GET_THEME_IMAGES;
 * • exibir imagens do tema;
 * • alternar automaticamente a cada 30 segundos;
 * • reiniciar quando o tema mudar.
 *
 * Toda essa lógica pertence exclusivamente à camada de apresentação.
 * Não utiliza Observer, EventBus ou qualquer mecanismo semelhante — a
 * troca é detectada por comparação simples de nome de tema, feita pelo
 * PlaybackScreen a cada chamada de refresh().
 */
public class ImageViewer extends JPanel {

    private static final int SLIDESHOW_INTERVAL_MS = 30_000;

    private final CommandDispatcher dispatcher;

    private final JLabel imageLabel;

    private final Timer timer;

    private List<String> images = new ArrayList<>();

    private int currentIndex = 0;

    /**
     * Nome do último tema carregado, usado apenas para detectar mudanças.
     */
    private String lastThemeName;

    public ImageViewer(CommandDispatcher dispatcher) {

        this.dispatcher = dispatcher;

        setLayout(new BorderLayout());

        this.imageLabel = new JLabel("", SwingConstants.CENTER);
        add(imageLabel, BorderLayout.CENTER);

        this.timer = new Timer(SLIDESHOW_INTERVAL_MS, e -> showNextImage());
        this.timer.setRepeats(true);
    }

    /**
     * Chamado pelo PlaybackScreen a cada refresh().
     *
     * Se o tema atual for diferente do último tema carregado, reinicia
     * o slideshow (nova consulta GET_THEME_IMAGES, índice zerado).
     */
    public void onThemeChanged(String currentThemeName) {

        if (Objects.equals(currentThemeName, lastThemeName)) {
            return;
        }

        lastThemeName = currentThemeName;

        restart();
    }

    /**
     * Interrompe o ciclo atual, executa novamente GET_THEME_IMAGES e
     * reinicia o slideshow a partir da primeira imagem.
     */
    private void restart() {

        timer.stop();

        StringListResult result = (StringListResult) dispatcher.dispatch(
                new Command("GET_THEME_IMAGES"));

        this.images = new ArrayList<>(result.values());
        this.currentIndex = 0;

        showCurrentImage();

        if (!images.isEmpty()) {
            timer.start();
        }
    }

    private void showNextImage() {

        if (images.isEmpty()) {
            return;
        }

        currentIndex = (currentIndex + 1) % images.size();

        showCurrentImage();
    }

    private void showCurrentImage() {

        if (images.isEmpty()) {
            imageLabel.setIcon(null);
            imageLabel.setText("(sem imagens para este tema)");
            return;
        }

        String path = images.get(currentIndex);

        try {

            // ImageIO.read é síncrono (bloqueia até decodificar totalmente
            // o arquivo), diferente de new ImageIcon(Image) +
            // getScaledInstance(...), que carrega/escala de forma
            // assíncrona e pode deixar o label em branco na primeira
            // exibição.
            BufferedImage original = ImageIO.read(new File(path));

            if (original == null) {
                imageLabel.setIcon(null);
                imageLabel.setText("(formato de imagem não suportado: " + path + ")");
                return;
            }

            int targetWidth = Math.max(getWidth(), 400);
            int targetHeight = Math.max(getHeight(), 250);

            Image scaled = original.getScaledInstance(
                    targetWidth,
                    targetHeight,
                    Image.SCALE_SMOOTH);

            imageLabel.setText("");
            imageLabel.setIcon(new ImageIcon(scaled));

        } catch (IOException ex) {

            imageLabel.setIcon(null);
            imageLabel.setText("(falha ao carregar imagem: " + path + " — " + ex.getMessage() + ")");
        }
    }

}
