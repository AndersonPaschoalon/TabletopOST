package br.org.tabletoprpg.soundtrack.model;

import java.util.List;

import br.org.tabletoprpg.soundtrack.exception.ErrorLoadingTheme;
import br.org.tabletoprpg.soundtrack.exception.ThemeNotFoundError;

public record Ost(
        String name,
        List<Theme> themes) {

    /**
     * Retorna o primeiro tema da OST, ou null caso não haja nenhum tema.
     */
    public Theme getFirstTheme() {
        if (themes == null || themes.isEmpty()) {
            throw new ErrorLoadingTheme("Lista de temas para " + this.name + " está vazia.");
        }
        return themes.get(0);
    }

    /**
     * Retorna o tema especificado.
     * 
     * @param name Nome.
     * @return Objeto do tema especificado
     * @throws ThemeNotFoundError Se tema não existe.
     */
    public Theme getThemeByName(String name) {
        return themes.stream()
                .filter(theme -> theme.name().equals(name))
                .findFirst()
                .orElseThrow(() -> new ThemeNotFoundError(
                        "Tema '%s' não encontrado na OST '%s'."
                                .formatted(name, this.name)));
    }

    /**
     * Retorna a listagem de temas disponíveis na OST especificada.
     * 
     * @return
     */
    public List<String> listThemes() {
        return themes.stream()
                .map(Theme::name)
                .toList();
    }

}