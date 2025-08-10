package com.codigopiojoso.screenmatch.repository;

import com.codigopiojoso.screenmatch.dto.EpisodioDTO;
import com.codigopiojoso.screenmatch.model.Categoria;
import com.codigopiojoso.screenmatch.model.Episodio;
import com.codigopiojoso.screenmatch.model.Serie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SerieRepository extends JpaRepository<Serie,Long> {
    Optional<Serie> findByTituloContainsIgnoreCase(String nombreSerie);

    List<Serie> findTop3ByOrderByClasificacionDesc();

    List<Serie> findByGenero(Categoria categoria);

    //List<Serie> findByTotalTemporadasLessThanEqualAndClasificacionGreaterThanEqual(int totalTemporadas, Double clasificacion);

    @Query("SELECT s FROM Serie s WHERE s.totalTemporadas <= :totalTemporadas AND s.clasificacion >= :clasificacion")
    List<Serie> seriesPorTemporadaYClasificacion(int totalTemporadas, Double clasificacion);

    @Query("SELECT e FROM Serie s JOIN s.episodios e WHERE e.titulo ILIKE %:nombreEpisodio%")
    List<Episodio> episodiosPorNombre(String nombreEpisodio);

    @Query("SELECT e FROM Serie s JOIN s.episodios e WHERE s = :serie ORDER BY e.clasificacion DESC LIMIT 3")
    List<Episodio> top3Episodios(Serie serie);

    @Query("SELECT s FROM Serie s " + "JOIN s.episodios e " + "GROUP BY s " + "ORDER BY MAX(e.fechaDeLanzamiento) DESC LIMIT 3")
    List<Serie> lanzamientosMasRecientes();

    @Query("SELECT e FROM Serie s JOIN s.episodios e WHERE s.id = :id AND e.temporada = :numeroTemporada")
    List<Episodio> getTemporadasByNumber(Long id, Long numeroTemporada);
}
