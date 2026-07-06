package br.org.tabletoprpg.soundtrack.service.session;

import br.org.tabletoprpg.soundtrack.app.ApplicationContext;
import br.org.tabletoprpg.soundtrack.model.Ost;
import br.org.tabletoprpg.soundtrack.service.catalog.CatalogService;
import br.org.tabletoprpg.soundtrack.service.loader.OstLoader;
import br.org.tabletoprpg.soundtrack.service.playback.PlaybackService;

public class SessionService {

    private final ApplicationContext context;

    private final CatalogService catalogService;
    private final OstLoader loader;
    private final PlaybackService playbackService;

    public SessionService(
            ApplicationContext context,
            CatalogService catalogService,
            OstLoader loader,
            PlaybackService playbackService) {

        this.context = context;
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

        this.context.setCurrentOst(ost.name());

        this.context.setCurrentTheme(ost.themes().getFirst().name());

        // this.playbackService.pauseBoth();
    }

}