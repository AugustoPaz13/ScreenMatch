package com.codigopiojoso.screenmatch.principal;

import com.codigopiojoso.screenmatch.modelos.Pelicula;
import com.codigopiojoso.screenmatch.modelos.Serie;
import com.codigopiojoso.screenmatch.modelos.Titulo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class PrincipalListas {
    public static void main(String[] args) {
        Pelicula nuevaPelicula = new Pelicula("Reino de los cielos", 2005, 144, true, "Ridley Scott");
        nuevaPelicula.setIncluidoEnElPlan(true);
        Pelicula nuevaPelicula2 = new Pelicula("Gladiador", 2000, 155, true, "Ridley Scott");
        nuevaPelicula2.setIncluidoEnElPlan(true);
        var nuevaPelicula3 = new Pelicula("El origen", 2010, 148, true, "Christopher Nolan");
        nuevaPelicula3.setIncluidoEnElPlan(true);

        Serie nuevaSerie = new Serie("Breaking Bad", 2008, 5, 13, 47);
        nuevaSerie.setIncluidoEnElPlan(true);

        ArrayList<Titulo> listaDeContenido = new ArrayList<>();
        listaDeContenido.add(nuevaPelicula);
        listaDeContenido.add(nuevaPelicula2);
        listaDeContenido.add(nuevaPelicula3);
        listaDeContenido.add(nuevaSerie);

        Collections.sort(listaDeContenido);
        
        for (Titulo contenido : listaDeContenido) {
            contenido.mostrarInformacion();
            System.out.println("Duración: " + contenido.getDuracion() + " minutos");
            System.out.println("Incluido en el plan: " + (contenido.isIncluidoEnElPlan() ? "Sí" : "No"));
            System.out.println("-----------------------------------");
        }

        listaDeContenido.sort(Comparator.comparing(Titulo::getFechaEstreno));
        System.out.println("Contenido ordenado por fecha de estreno:");
        for (Titulo contenido : listaDeContenido) {
            System.out.println(contenido.getNombre() + " - " + contenido.getFechaEstreno());
        }
    }
}
