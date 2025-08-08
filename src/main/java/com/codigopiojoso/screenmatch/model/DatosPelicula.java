package com.codigopiojoso.screenmatch.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DatosPelicula(
        @JsonAlias ("Title") String titulo,
        @JsonAlias ("Year") String anio,
        @JsonAlias("Runtime") String duracion,
        @JsonAlias("imdbRating") String clasificacion) {
}
