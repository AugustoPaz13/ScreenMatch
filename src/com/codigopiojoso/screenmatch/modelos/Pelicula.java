package com.codigopiojoso.screenmatch.modelos;

import com.codigopiojoso.screenmatch.calculos.Clasificacion;

public class Pelicula extends Titulo implements Clasificacion{
    String director;

    public Pelicula(String nombre, int fechaEstreno, int duracion, boolean incluidoEnElPlan, String director) {
        super(nombre, fechaEstreno, duracion, incluidoEnElPlan);
        this.director = director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getDirector() {
        return director;
    }

    public int getClasificacion() {
        return (int) calcularPromedio();
    }

    @Override
    public String toString() {
        return "Pelicula: " +this.getNombre() + " (" +this.getFechaEstreno() + ")";
    }
}
