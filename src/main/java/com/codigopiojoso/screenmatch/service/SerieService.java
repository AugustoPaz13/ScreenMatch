package com.codigopiojoso.screenmatch.service;

import com.codigopiojoso.screenmatch.dto.EpisodioDTO;
import com.codigopiojoso.screenmatch.dto.SerieDTO;
import com.codigopiojoso.screenmatch.model.Categoria;
import com.codigopiojoso.screenmatch.model.Episodio;
import com.codigopiojoso.screenmatch.model.Serie;
import com.codigopiojoso.screenmatch.repository.SerieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SerieService {
    @Autowired
    private SerieRepository serieRepository;

    @GetMapping("/series")
    public List<SerieDTO> getSeries() {
        return convierteDatos(serieRepository.findAll());
    }

    public List<SerieDTO> getTop3Series() {
        return convierteDatos(serieRepository.findTop3ByOrderByClasificacionDesc());
    }

    public List<SerieDTO> getLanzamientosMasRecientes() {
        return convierteDatos(serieRepository.lanzamientosMasRecientes());
    }

    public List<SerieDTO> convierteDatos(List<Serie> serie){
        return serie.stream()
                .map(s -> new SerieDTO(
                        s.getId(),
                        s.getTitulo(),
                        s.getTotalTemporadas(),
                        s.getClasificacion(),
                        s.getPosterUrl(),
                        s.getGenero(),
                        s.getActores(),
                        s.getSinopsis()))
                .collect(Collectors.toList());
    }

    public SerieDTO getSerieById(long id) {
        Optional<Serie> serie = serieRepository.findById(id);
        if (serie.isPresent()) {
            Serie s = serie.get();
            return new SerieDTO(
                    s.getId(),
                    s.getTitulo(),
                    s.getTotalTemporadas(),
                    s.getClasificacion(),
                    s.getPosterUrl(),
                    s.getGenero(),
                    s.getActores(),
                    s.getSinopsis());
        } else {
            return null;
        }
    }

    public List<EpisodioDTO> getAllTemporadas(long id) {
        Optional<Serie> serie = serieRepository.findById(id);
        if(serie.isPresent()){
            Serie s = serie.get();
            return s.getEpisodios().stream()
                    .map(e-> new EpisodioDTO(
                            e.getTemporada(),
                            e.getTitulo(),
                            e.getNumeroEpisodio()
                    ))
                    .collect(Collectors.toList());
        }
        return null;
    }

    public List<EpisodioDTO> getTemporadasByNumber(Long id, Long numeroTemporada) {
        return serieRepository.getTemporadasByNumber(id, numeroTemporada).
                stream()
                .map(e-> new EpisodioDTO(
                        e.getTemporada(),
                        e.getTitulo(),
                        e.getNumeroEpisodio()
                ))
                .collect(Collectors.toList());
    }

    public List<SerieDTO> getSeriesByCategory(String nombreGenero) {
        Categoria categoria =  Categoria.fromEspanol(nombreGenero);
        return convierteDatos(serieRepository.findByGenero(categoria));
    }
}
