package com.codigopiojoso.screenmatch.dto;

import com.codigopiojoso.screenmatch.model.Categoria;

public record SerieDTO(
            Long id,
            String titulo,
            Integer totalTemporadas,
            Double clasificacion,
            String posterUrl,
            Categoria genero,
            String actores,
            String sinopsis
) {
}
