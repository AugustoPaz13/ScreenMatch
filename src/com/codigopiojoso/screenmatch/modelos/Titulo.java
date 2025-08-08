package com.codigopiojoso.screenmatch.modelos;

import com.codigopiojoso.screenmatch.excepcion.NotFoundedDateException;
import com.google.gson.annotations.SerializedName;

public class Titulo implements Comparable<Titulo> {
    private String nombre;
    private int fechaEstreno;
    private int duracion;
    private boolean incluidoEnElPlan;
    private double sumaClasificaciones;
    private int cantidadCalificaciones = 0;

    public Titulo(String nombre, int fechaEstreno, int duracion, boolean incluidoEnElPlan) {
        this.nombre = nombre;
        this.fechaEstreno = fechaEstreno;
        this.duracion = duracion;
        this.incluidoEnElPlan = incluidoEnElPlan;
    }

    public Titulo(TituloOmdb miTituloOmbd) {
        this.nombre = miTituloOmbd.title();
        this.fechaEstreno = Integer.valueOf(miTituloOmbd.year());
        if ((miTituloOmbd.runtime().contains("N/A")) || (miTituloOmbd.runtime().isEmpty())) {
            throw new NotFoundedDateException("La duración de la película no está disponible");
        }
        this.duracion = Integer.valueOf(miTituloOmbd.runtime().substring(0,2).replace(" ", ""));
    }

    //GETTERS
    public int getCantidadCalificaciones() {
        return cantidadCalificaciones;
    }
    public int getDuracion() {
        return duracion;
    }
    public int getFechaEstreno() {
        return fechaEstreno;
    }
    public String getNombre() {
        return nombre;
    }
    public double getSumaClasificaciones() {
        return sumaClasificaciones;
    }
    //SETTERS
    public void setCantidadCalificaciones(int cantidadCalificaciones) {
        this.cantidadCalificaciones = cantidadCalificaciones;
    }
    public void setDuracion(int duracion) {
        this.duracion = duracion;
    }
    public void setFechaEstreno(int fechaEstreno) {
        this.fechaEstreno = fechaEstreno;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public void setIncluidoEnElPlan(boolean incluidoEnElPlan) {
        this.incluidoEnElPlan = incluidoEnElPlan;
    }
    public void setSumaClasificaciones(double sumaClasificaciones) {
        this.sumaClasificaciones = sumaClasificaciones;
    }

    public void mostrarInformacion() {
        System.out.println("Nombre: " + nombre);
        System.out.println("Fecha de Estreno: " + fechaEstreno);
    }  
    
    public void evaluarPelicula(double calificacion) {
        sumaClasificaciones += calificacion;
        cantidadCalificaciones++;
    }

    public double calcularPromedio() {
        return sumaClasificaciones / cantidadCalificaciones; 
    }

    public boolean isIncluidoEnElPlan() {
        return incluidoEnElPlan;
    }

    @Override
    public int compareTo(Titulo otroTitulo) {
        return this.nombre.compareTo(otroTitulo.nombre);
    }

    @Override
    public String toString() {
        return """
                \n
                Nombre: %s
                Fecha de Estreno: %d
                Duración: %d minutos
                Incluido en el plan: %s
                Promedio de calificaciones: %.2f
                ------------------------------------
                """.formatted(nombre, fechaEstreno, duracion, incluidoEnElPlan ? "Sí" : "No", calcularPromedio());
    }
}
