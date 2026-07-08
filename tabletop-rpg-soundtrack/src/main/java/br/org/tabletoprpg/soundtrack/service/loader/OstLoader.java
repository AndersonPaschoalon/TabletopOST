package br.org.tabletoprpg.soundtrack.service.loader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;


import br.org.tabletoprpg.soundtrack.model.Ost;
import br.org.tabletoprpg.soundtrack.model.Theme;

/**
 * 
 * Transformar uma OST armazenada localmente em objetos do domínio (model).
 */
public class OstLoader implements OstLoaderInterface {

    @Override
    public Ost load(String localPath) { //o SessionService exige localPath, não ostName

        
        Path manifestPath = Paths.get(localPath, "manifest.json");

        if (!Files.exists(manifestPath)) {
            throw new RuntimeException("manifest.json não encontrado em");
        }

        try {
            String jsonContent = Files.readString(manifestPath);
            return parseJsonToOst(jsonContent);
        } catch (IOException e) {
            throw new RuntimeException("Erro ao ler manifest.json");
        }


    }

    //TODO: Fazer o parsing do json manifest da forma correta
    private Ost parseJsonToOst(String json) {

        String ostName = null;
        List<Theme> themes = null;

        return new Ost(ostName, themes); //arrumar depois com o formato certo do objeto Ost
    }
    



}
