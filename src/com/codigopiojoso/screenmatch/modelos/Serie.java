package com.codigopiojoso.screenmatch.modelos;

public class Serie extends Titulo {
    int temporadas;
    int episodiosPorTemporada;
    int minutosPorEpisodio;

    public Serie(String nombre, int fechaEstreno, int temporadas, int episodiosPorTemporada, int minutosPorEpisodio) {
        super(nombre, fechaEstreno, 0, false);
        this.temporadas = temporadas;
        this.episodiosPorTemporada = episodiosPorTemporada;
        this.minutosPorEpisodio = minutosPorEpisodio;
    }

    public int getDuracion() {
        return temporadas * episodiosPorTemporada * minutosPorEpisodio;
    }

    public int getEpisodiosPorTemporada() {
        return episodiosPorTemporada;
    }
    public int getMinutosPorEpisodio() {
        return minutosPorEpisodio;
    }
    public int getTemporadas() {
        return temporadas;
    }
    public void setEpisodiosPorTemporada(int episodiosPorTemporada) {
        this.episodiosPorTemporada = episodiosPorTemporada;
    }
    public void setMinutosPorEpisodio(int minutosPorEpisodio) {
        this.minutosPorEpisodio = minutosPorEpisodio;
    }
    public void setTemporadas(int temporadas) {
        this.temporadas = temporadas;
    }

    @Override
    public String toString() {
        return """
                -------------------------
                Nombre: %s
                Fecha de Estreno: %d
                Temporadas: %d
                Episodios por Temporada: %d
                Minutos por Episodio: %d
                Duración Total: %d minutos
                
                """.formatted(getNombre(), getFechaEstreno(), temporadas, episodiosPorTemporada, minutosPorEpisodio, getDuracion());
    }
}
