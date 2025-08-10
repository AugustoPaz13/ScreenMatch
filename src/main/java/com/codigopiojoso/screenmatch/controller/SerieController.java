package com.codigopiojoso.screenmatch.controller;

import com.codigopiojoso.screenmatch.dto.EpisodioDTO;
import com.codigopiojoso.screenmatch.dto.SerieDTO;
import com.codigopiojoso.screenmatch.service.SerieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/series")
public class SerieController {

    @Autowired
    private SerieService servicio;

    @GetMapping
    public List<SerieDTO> getSeries() {
        return servicio.getSeries();
    }

    @GetMapping("/top3")
    public List<SerieDTO> getTop3Series() {
        return servicio.getTop3Series();
    }

    @GetMapping("/lanzamientos")
    public List<SerieDTO> getLanzamientosMasRecientes() {
        return servicio.getLanzamientosMasRecientes();
    }

    @GetMapping("/{id}")
    public SerieDTO getSerieById(@PathVariable Long id) {
        return servicio.getSerieById(id);
    }

    @GetMapping("/{id}/temporadas/todas")
    public List<EpisodioDTO> getAllTemporadas(@PathVariable Long id){
        return servicio.getAllTemporadas(id);
    }

    @GetMapping("/{id}/temporadas/{numeroTemporada}")
    public List<EpisodioDTO> getTemporadasByNumber(@PathVariable Long id,
                                                   @PathVariable Long numeroTemporada){
        return servicio.getTemporadasByNumber(id,numeroTemporada);
    }

    @GetMapping("/categoria/{nombreGenero}")
    public List<SerieDTO> getSeriesByCategory(@PathVariable String nombreGenero){
        return servicio.getSeriesByCategory(nombreGenero);
    }
}
