package com.codigopiojoso.screenmatch.modelos;

public record TituloOmdb(String title, String year, String runtime) {
    public String toString() {
        return """
                --------------------------
                Pelicula: %s
                Año: %s
                Duración: %s
                --------------------------
                """.formatted(title, year, runtime);
    }
}
