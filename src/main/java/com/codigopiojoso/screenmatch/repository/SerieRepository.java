package com.codigopiojoso.screenmatch.repository;

import com.codigopiojoso.screenmatch.model.Categoria;
import com.codigopiojoso.screenmatch.model.Serie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SerieRepository extends JpaRepository<Serie,Long> {
    Optional<Serie> findByTituloContainsIgnoreCase(String nombreSerie);

    List<Serie> findTop3ByOrderByClasificacionDesc();

    List<Serie> findByGenero(Categoria categoria);
}
