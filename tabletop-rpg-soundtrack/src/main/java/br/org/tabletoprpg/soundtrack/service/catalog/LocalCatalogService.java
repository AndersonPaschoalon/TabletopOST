package br.org.tabletoprpg.soundtrack.service.catalog;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;


import br.org.tabletoprpg.soundtrack.exception.TabletopExeption;
import br.org.tabletoprpg.soundtrack.exception.DownloadError;
import br.org.tabletoprpg.soundtrack.exception.OstNotFoundError;




/**
 * Os arquivos da OST são armazenados na pasta local_storage.
 * É uma implementação mais simples que o armazenamento remoto.
 */
public class LocalCatalogService implements CatalogService {

    private final String localStoragePath;
    private final String cachePath = "./cache/";

    public LocalCatalogService(String localStoragePath) {
        this.localStoragePath = localStoragePath;
    }

    @Override
    public Collection<String> listAvailableOsts() {
        // Retorna os nomes das pastas dentro do diretório localStoragePath e retorna
        // como uma coleção de strings.
        
        Path rootPath = getLocalPath();

         try (Stream<Path> paths = Files.list(rootPath)) {
            return paths.filter(Files::isDirectory).map(path -> path.getFileName().toString()).collect(Collectors.toList());
        } catch (IOException e) {
            throw new TabletopExeption("Erro ao listar OSTs");
        }


    }

    @Override
    public String downloadOst(String ostName) {
        // Copia todos os arquivos da pasta localStoragePath/ostName para o cache local
        // e retorna o caminho do cache local.

        Path sourcePath = Paths.get(localStoragePath, ostName);
        Path targetPath = Paths.get(cachePath, ostName);

        if (!Files.exists(sourcePath)) {
            throw new OstNotFoundError("A OST" + ostName + " não foi encontrada");
        }

        try (Stream<Path> stream = Files.walk(sourcePath)) { //entra na pasta de origem da OST
            
            stream.forEach(source -> {
                try {
                    Path destination = targetPath.resolve(sourcePath.relativize(source)); //entra em ./cache/
                    
                    //se for um diretório, temos que cria-lo no cache antes de copiarmos arquivos para eles
                    //se for um arquivo, apenas os copiamos

                    if (Files.isDirectory(source)) {
                        if (!Files.exists(destination)) {
                            Files.createDirectory(destination);

                        }

                    } else {
                        Files.copy(source, destination, StandardCopyOption.REPLACE_EXISTING); //Copia todos os arquivos da pasta localStoragePath/ostName para o cache local
                    }
                } catch (IOException ex) {
                    throw new DownloadError("falha na copia da OST");
                }
            });

        } catch (IOException e) {
            throw new TabletopExeption("erro ao realizar o cache local da OST");
        }

        System.out.println("Downloading OST: " + ostName);
        return targetPath.toString();
        

    }

    private Path getLocalPath() {
        try {
            Path rootPath = Paths.get(localStoragePath);

            if (!Files.exists(rootPath)) {
                throw new TabletopExeption("O diretório do storage local nao encontrado " + localStoragePath);
            }

            return rootPath;

        } catch (Exception e) {
           throw new TabletopExeption("Path local nao encontrado");
        }

        

    }

}
