package com.codigopiojoso.screenmatch.model;

import jakarta.persistence.*;
import java.util.List;
import java.util.OptionalDouble;
import java.util.ArrayList;

@Entity
@Table(name = "series")
public class Serie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String titulo;
    private Integer totalTemporadas;
    private Double clasificacion;
    private String posterUrl;
    @Enumerated(EnumType.STRING)
    private Categoria genero;
    private String actores;
    private String sinopsis;
    @OneToMany(mappedBy = "serie", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Episodio> episodios = new ArrayList<>();

    public Serie() {
    }

    public Serie(DatosSerie datosSerie) {
        this.titulo = datosSerie.titulo();
        this.totalTemporadas = datosSerie.totalTemporadas();
        this.clasificacion = OptionalDouble.of(Double.valueOf(datosSerie.clasificacion())).orElse(0.0);
        this.posterUrl = datosSerie.posterUrl();
        this.genero = Categoria.fromString(datosSerie.genero().split(",")[0].trim());
        this.sinopsis = datosSerie.sinopsis();
        this.actores = datosSerie.actores();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Episodio> getEpisodios() {
        return episodios;
    }

    public void setEpisodios(List<Episodio> episodios) {
        episodios.forEach(e -> e.setSerie(this));
        this.episodios = episodios;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Integer getTotalTemporadas() {
        return totalTemporadas;
    }

    public void setTotalTemporadas(Integer totalTemporadas) {
        this.totalTemporadas = totalTemporadas;
    }

    public Double getClasificacion() {
        return clasificacion;
    }

    public void setClasificacion(Double clasificacion) {
        this.clasificacion = clasificacion;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }

    public Categoria getGenero() {
        return genero;
    }

    public void setGenero(Categoria genero) {
        this.genero = genero;
    }

    public String getActores() {
        return actores;
    }

    public void setActores(String actores) {
        this.actores = actores;
    }

    public String getSinopsis() {
        return sinopsis;
    }

    public void setSinopsis(String sinopsis) {
        this.sinopsis = sinopsis;
    }

    @Override
    public String toString() {
        return """
                ----------------------------------
                Título: %s
                Género: %s
                Clasificación: %.1f
                Total de temporadas: %d
                Actores: %s
                Episodios: %s
                ----------------------------------
                Sinopsis: %s
                ----------------------------------
                Poster: %s
                """.formatted(titulo, genero.categoriaOmdb, clasificacion, totalTemporadas, actores, episodios, sinopsis, posterUrl);
    }
}