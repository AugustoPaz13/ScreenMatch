package com.codigopiojoso.screenmatch.modelos;

import com.codigopiojoso.screenmatch.calculos.Clasificacion;

public class Episodio implements Clasificacion {
    private int numero;
    private String titulo;
    private String serie;
    private int totalVistas;

    public Episodio(int numero, int totalVistas, String serie, String titulo) {
        this.numero = numero;
        this.totalVistas = totalVistas;
        this.serie = serie;
        this.titulo = titulo;
    }

    public int getNumero() {
        return numero;
    }
    public String getSerie() {
        return serie;
    }
    public String getTitulo() {
        return titulo;
    }
    public void setNumero(int numero) {
        this.numero = numero;
    }
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
    public void setSerie(String serie) {
        this.serie = serie;
    }
    public void setTotalVistas(int totalVistas) {
        this.totalVistas = totalVistas;
    }

    public void mostrarInformacion() {
        System.out.println("Titulo: " + titulo);
        System.out.println("Número de episodio: " + numero);
        System.out.println("Serie: " + serie);
        System.out.println("Total de vistas: " + totalVistas);
    } 

    @Override
    public int getClasificacion() {
        if(totalVistas > 100){
            return 5; // Alta popularidad
        } else if(totalVistas > 50) {
            return 4; // Moderada popularidad
        } else if(totalVistas > 20) {
            return 3; // Baja popularidad
        } else {
            return 2; // Muy baja popularidad
        }
         
    }
}
