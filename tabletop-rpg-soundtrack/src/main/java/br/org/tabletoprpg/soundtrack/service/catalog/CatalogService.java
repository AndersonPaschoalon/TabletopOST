package br.org.tabletoprpg.soundtrack.service.catalog;

import java.util.Collection;

import br.org.tabletoprpg.soundtrack.exception.OstNotFoundError;
import br.org.tabletoprpg.soundtrack.exception.ConnectionError;
import br.org.tabletoprpg.soundtrack.exception.DownloadError;

/**
 * Garantir que uma OST esteja disponível no cache local
 * 
 * Representa um serviço de armazenamento (remoto ou local) de arquivos de OSTs.
 * Pode ser:
 * - Um serviço de armazenamento remoto (ex: S3, Google Cloud Storage, etc)
 * - Um repositorio de arquivos (ex: GitHub, GitLab, etc)
 * - Um serviço de armazenamento local (ex: uma pasta local do sistema de
 * arquivos)
 * 
 * A responsabilidade desse serviço deve ser gravar no cache local os arquivos
 * de OSTs e disponibilizar a listagem de OSTs disponíveis no serviço de
 * armazenamento.
 * 
 * Este armazenamento
 */
public interface CatalogService {

    /**
     * Este método retorna a listagem de OSTs disponíveis no serviço storage.
     * 
     * @return
     * 
     * @throws ConnectionError
     */
    public Collection<String> listAvailableOsts();

    /**
     * Realiza o download de uma OST do serviço de armazenamento para o cache local.
     * 
     * @param ostName
     * @return
     * @throws DownloadError
     * @throws OstNotFoundError
     */
    public String downloadOst(String ostName);

}
