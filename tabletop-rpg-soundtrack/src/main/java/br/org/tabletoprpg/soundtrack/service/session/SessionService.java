package br.org.tabletoprpg.soundtrack.service.session;

import java.util.List;

import br.org.tabletoprpg.soundtrack.exception.DownloadError;
import br.org.tabletoprpg.soundtrack.exception.ErrorLoadingOst;
import br.org.tabletoprpg.soundtrack.model.Ost;
import br.org.tabletoprpg.soundtrack.model.Theme;
import br.org.tabletoprpg.soundtrack.service.catalog.CatalogService;
import br.org.tabletoprpg.soundtrack.service.loader.OstLoader;
import br.org.tabletoprpg.soundtrack.service.playback.PlaybackService;

public class SessionService {

    private String currentOst;
    private String currentTheme;
    private Ost ost;
    private Theme theme;

    private final CatalogService catalogService;
    private final OstLoader loader;
    private final PlaybackService playbackServiceAmbience;
    private final PlaybackService playbackServiceMusic;

    /**
     * Inicializa o serviço de uma sessão.
     * 
     * @param ostName
     * @param catalogService
     * @param loader
     * @param playbackService
     */
    public SessionService(
            String ostName,
            CatalogService catalogService,
            OstLoader loader,
            PlaybackService playbackServiceAmbience,
            PlaybackService playbackServiceMusic) {
        this.playbackServiceAmbience = playbackServiceAmbience;
        this.playbackServiceMusic = playbackServiceMusic;
        this.catalogService = catalogService;
        this.loader = loader;
        this.setCurrentOst(ostName);
    }

    ///////////////////////////////////////////////////////////////////////////
    /// Getters
    ///////////////////////////////////////////////////////////////////////////

    /**
     * Retorna o nome da OST da sessão.
     * 
     * @return
     */
    public String getCurrentOstName() {
        return this.currentOst;
    }

    /**
     * Retorna a lista de temas disponiveis nessa OST.
     * 
     * @return
     */
    public List<String> getListOfThemes() {
        List<String> lthemes = this.ost.listThemes();
        return lthemes;
    }

    /**
     * Retorna o objeto de thema atualmente setado.
     * 
     * @return
     */
    public Theme getCurrentTheme() {
        return this.theme;
    }

    /**
     * Retorna o objeto do tema atualmente setado.
     * 
     * @return
     */
    public String getCurrentThemeName() {
        return this.currentTheme;
    }

    /**
     * Diz se musica está sendo reproduzida.
     * 
     * @return
     */
    public boolean isSongPlaying() {
        return this.playbackServiceMusic.isPlaying();
    }

    /**
     * Diz se som ambiente está sendo reproduzido.
     */
    public boolean isAmbiencePlaying() {
        return this.playbackServiceAmbience.isPlaying();
    }

    ///////////////////////////////////////////////////////////////////////////
    /// Commands
    ///////////////////////////////////////////////////////////////////////////

    /**
     * Seta um novo tema.
     * 
     * @param themeName
     */
    public void setCurrentTheme(String themeName) {
        this.theme = this.ost.getThemeByName(themeName);
        this.currentTheme = themeName;

    }

    /**
     * Desseleciona tema.
     */
    public void unsetTheme() {
        this.setCurrentTheme(ost.getFirstTheme().name());
    }

    /***
     * Começa a reproduzir musica.
     */
    public void playSong() {
        this.playbackServiceMusic.stop();
        this.playbackServiceMusic.setPlaylist(
                List.copyOf(theme.songTracks()));
        this.playbackServiceMusic.play();
    }

    /**
     * Começa a reproduzir musica ambiente.
     */
    public void playAmbience() {
        this.playbackServiceAmbience.stop();
        this.playbackServiceAmbience.setPlaylist(
                List.copyOf(theme.ambienceTracks()));
        playbackServiceAmbience.play();
    }

    /**
     * Pausa música.
     */
    public void pauseSong() {
        this.playbackServiceMusic.stop();
    }

    /**
     * Pausa som ambiente.
     */
    public void pauseAmbience() {
        this.playbackServiceAmbience.stop();
    }

    /**
     * Pausa musica e som ambiente.
     */
    public void pauseBoth() {
        this.pauseSong();
        this.pauseAmbience();
    }

    /**
     * Pausa ambas as faixas, de musica e ambiente.
     */
    public void playBoth() {
        this.playSong();
        this.playAmbience();
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
    private void setCurrentOst(String ostName) {
        this.pauseBoth();

        // Faz o download da OST caso não exista localmente.
        try {
            this.catalogService.downloadOst(ostName);
        } catch (Exception e) {
            throw new DownloadError("Erro ao baixar a OST: " + e.getMessage());
        }

        // seta estado e thema default
        this.ost = this.loader.load(ostName);
        this.currentOst = ostName;

        this.setCurrentTheme(ost.getFirstTheme().name());
    }

}