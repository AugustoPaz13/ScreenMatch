package com.codigopiojoso.screenmatch.calculos;

import com.codigopiojoso.screenmatch.modelos.Titulo;

public class CalculadoraDeTiempo {
    private int tiempoTotal;

    public int getTiempoTotal() {
        return tiempoTotal;
    }

    public void incluir(Titulo titulo) {
        this.tiempoTotal += titulo.getDuracion();
    }
}
