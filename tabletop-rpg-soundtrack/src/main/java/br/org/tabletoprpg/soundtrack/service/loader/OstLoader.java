package br.org.tabletoprpg.soundtrack.service.loader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.List;


import br.org.tabletoprpg.soundtrack.model.Ost;
import br.org.tabletoprpg.soundtrack.model.Theme;
import com.google.gson.Gson;
/**
 * 
 * Transformar uma OST armazenada localmente em objetos do domínio (model).
 */
public class OstLoader implements OstLoaderInterface {

    private final Gson gson = new Gson();


    @Override
    public Ost load(String localPath) { //o SessionService exige localPath, não ostName

        
        Path manifestPath = Paths.get(localPath, "manifest.json");

        if (!Files.exists(manifestPath)) {
            throw new RuntimeException("manifest.json não encontrado em");
        }

        try {
            String jsonContent = Files.readString(manifestPath);
            return parseJsonToOst(jsonContent, localPath);
        } catch (IOException e) {
            throw new RuntimeException("Erro ao ler manifest.json");
        }


    }

    private Ost parseJsonToOst(String json, String localPath) {

        Ost raw = gson.fromJson(json, Ost.class);

        List<Theme> resolvedThemes = raw.themes().stream()
                .map(theme -> new Theme(
                        theme.name(),
                        resolvePaths(localPath, theme.songTracks()),
                        resolvePaths(localPath, theme.ambienceTracks()),
                        resolvePaths(localPath, theme.images())))
                .toList();

        return new Ost(raw.name(), resolvedThemes);
    }

    private List<String> resolvePaths(String localPath, Collection<String> relativePaths) {

        if (relativePaths == null) {
            return List.of();
        }

        return relativePaths.stream()
                .map(relative -> Paths.get(localPath, relative).toString())
                .toList();
    }




}
