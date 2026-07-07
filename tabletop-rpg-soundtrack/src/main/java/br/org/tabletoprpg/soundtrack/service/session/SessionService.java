package br.org.tabletoprpg.soundtrack.service.session;

import br.org.tabletoprpg.soundtrack.model.Ost;
import br.org.tabletoprpg.soundtrack.service.catalog.CatalogService;
import br.org.tabletoprpg.soundtrack.service.loader.OstLoader;
import br.org.tabletoprpg.soundtrack.service.playback.PlaybackService;

public class SessionService {

    private String currentOst;
    private String currentTheme;

    private final CatalogService catalogService;
    private final OstLoader loader;
    private final PlaybackService playbackService;

    public SessionService(
            CatalogService catalogService,
            OstLoader loader,
            PlaybackService playbackService) {

        this.catalogService = catalogService;
        this.loader = loader;
        this.playbackService = playbackService;
    }

    /**
     * Seleciona uma nova OST para a sessão.
     *
     * Fluxo:
     * 1) garante a OST no cache local;
     * 2) carrega o manifest;
     * 3) atualiza o contexto;
     * 4) seleciona automaticamente o primeiro tema;
     * 5) interrompe qualquer reprodução em andamento.
     */
    public void setCurrentOst(String ostName) {

        String localPath = this.catalogService.downloadOst(ostName);

        Ost ost = this.loader.load(localPath);

        this.currentOst = ost.name();

        if (ost.themes() != null && !ost.themes().isEmpty()) {
            this.currentTheme = ost.themes().getFirst().name();
        }
        // this.playbackService.pauseBoth();
    }

    public String getCurrentOst() {
        return currentOst;
    }

    public String getCurrentTheme() {
        return currentTheme;
    }

}