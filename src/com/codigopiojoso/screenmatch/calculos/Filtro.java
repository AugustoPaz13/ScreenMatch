package com.codigopiojoso.screenmatch.calculos;

public class Filtro {
    public void filtraClasificacion(Clasificacion clasificacion){
        if(clasificacion.getClasificacion() >= 4) {
            System.out.println("Tiene una buena clasificación: " + clasificacion.getClasificacion());
        } else if (clasificacion.getClasificacion() >= 2) {
            System.out.println("Tiene una clasificación aceptable: " + clasificacion.getClasificacion());
        }else{
            System.out.println("Juzgala tu mismo...");
        }

    }
}
