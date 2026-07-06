package br.org.tabletoprpg.soundtrack.service.catalog;

import java.util.Collection;

/**
 * Os arquivos da OST são armazenados na pasta local_storage.
 * É uma implementação mais simples que o armazenamento remoto.
 */
public class LocalCatalogService implements CatalogService {

    private final String localStoragePath;

    public LocalCatalogService(String localStoragePath) {
        this.localStoragePath = localStoragePath;
    }

    @Override
    public Collection<String> listAvailableOsts() {
        // Retorna os nomes das pastas dentro do diretório localStoragePath e retorna
        // como uma coleção de strings.
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public String downloadOst(String ostName) {
        // Copia todos os arquivos da pasta localStoragePath/ostName para o cache local
        // e retorna o caminho do cache local.
        // throw new UnsupportedOperationException("Not implemented yet");
        System.out.println("Downloading OST: " + ostName);
        return "./cache/" + ostName;

    }

}
