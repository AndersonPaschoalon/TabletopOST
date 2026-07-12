package br.org.tabletoprpg.soundtrack.controller.result;

/**
 * Estado atual da aplicação.
 */
public record StatusResult(

        String currentOst,

        String currentTheme,

        boolean songPlaying,

        boolean ambiencePlaying)

        implements Result {

    @Override
    public String asText() {

        return String.format(
                """
                        OST       : %s
                        Theme     : %s
                        Song      : %s
                        Ambience  : %s
                        """,
                currentOst,
                currentTheme,
                songPlaying ? "Playing" : "Stopped",
                ambiencePlaying ? "Playing" : "Stopped");
    }

}