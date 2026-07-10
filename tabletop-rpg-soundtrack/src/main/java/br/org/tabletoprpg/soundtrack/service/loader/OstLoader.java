package br.org.tabletoprpg.soundtrack.service.loader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


import br.org.tabletoprpg.soundtrack.model.Ost;
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
            return parseJsonToOst(jsonContent);
        } catch (IOException e) {
            throw new RuntimeException("Erro ao ler manifest.json");
        }


    }

    private Ost parseJsonToOst(String json) {
     
        return gson.fromJson(json, Ost.class);
    }
    



}
