package com.codigopiojoso.screenmatch.principal;

import com.codigopiojoso.screenmatch.calculos.CalculadoraDeTiempo;
import com.codigopiojoso.screenmatch.calculos.Filtro;
import com.codigopiojoso.screenmatch.modelos.Episodio;
import com.codigopiojoso.screenmatch.modelos.Pelicula;
import com.codigopiojoso.screenmatch.modelos.Serie;

import java.util.ArrayList;

public class Principal {
    public static void main(String[] args) {
        Pelicula nuevaPelicula = new Pelicula("The Batman", 2022, 155, true, "Matt Reeves");
        nuevaPelicula.setIncluidoEnElPlan(true);
        nuevaPelicula.evaluarPelicula(4.5);
        nuevaPelicula.evaluarPelicula(3.8);
        nuevaPelicula.evaluarPelicula(5.0);
        nuevaPelicula.mostrarInformacion();
        System.out.println("Clasificación de la pelicula: " + nuevaPelicula.calcularPromedio());
        Filtro filtro = new Filtro();
        filtro.filtraClasificacion(nuevaPelicula);
        System.out.println("-----------------------------------");
        Pelicula nuevaPelicula2 = new Pelicula("Superman", 2025, 120, true, "James Gunn");
        nuevaPelicula2.setIncluidoEnElPlan(false);
        nuevaPelicula2.mostrarInformacion();
        System.out.println("-----------------------------------");

        Serie nuevaSerie = new Serie("Game of Thrones", 2011, 8, 10, 55);
        nuevaSerie.setDuracion(nuevaSerie.getDuracion());
        nuevaSerie.mostrarInformacion();
        System.out.println("Duración total de la serie: " + nuevaSerie.getDuracion() + " minutos");
        
        System.out.println("-----------------------------------");
        Episodio episodio1 = new Episodio(1, 100, "Game of Thrones", "Winter is Coming");
        episodio1.mostrarInformacion();
        Filtro filtroEpisodio = new Filtro();
        filtroEpisodio.filtraClasificacion(episodio1);

        System.out.println("-----------------------------------");
        CalculadoraDeTiempo calculadora = new CalculadoraDeTiempo();
        calculadora.incluir(nuevaPelicula);
        calculadora.incluir(nuevaPelicula2);
        calculadora.incluir(nuevaSerie);
        System.out.println("Tiempo total de películas y series incluidas: " + calculadora.getTiempoTotal() + " minutos");
        System.out.println("-----------------------------------");
        ArrayList listaDePeliculas = new ArrayList<>();
        listaDePeliculas.add(nuevaPelicula);
        listaDePeliculas.add(nuevaPelicula2);
        System.out.println("Tamaño de la lista: " + listaDePeliculas.size());
        System.out.println("Primera película: " + listaDePeliculas.get(0));
        System.out.println(listaDePeliculas);
    }
    
}
