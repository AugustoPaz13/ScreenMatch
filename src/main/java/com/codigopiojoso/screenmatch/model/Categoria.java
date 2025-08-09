package com.codigopiojoso.screenmatch.model;

public enum Categoria {
    ACCION("Action", "Acción"),
    AVENTURA("Adventure", "Aventura"),
    ANIMACION("Animation", "Animación"),
    COMEDIA("Comedy", "Comedia"),
    CRIMEN("Crime", "Crimen"),
    DOCUMENTAL("Documentary", "Documental"),
    DRAMA("Drama", "Drama"),
    FAMILIA("Family", "Familiar"),
    FANTASIA("Fantasy", "Fantasía"),
    HISTORICO("History", "Histórico"),
    HORROR("Horror", "Horror"),
    MUSICAL("Musical", "Musical"),
    MISTERIO("Mystery", "Misterio"),
    ROMANCE("Romance", "Romance"),
    CIENCIA_FICCION("Sci-Fi", "Ciencia Ficción"),
    TV_MOVIE("TV Movie", "Película de TV"),
    THRILLER("Thriller", "Suspenso"),
    GUERRA("War", "Guerra"),
    WESTERN("Western", "Viejo Oeste");

    public final String categoriaOmdb;
    public final String categoriaEspanol;

    private Categoria(String categoriaOmdb, String categoriaEspañol) {
        this.categoriaOmdb = categoriaOmdb;
        this.categoriaEspanol = categoriaEspañol;
    }

    public static Categoria fromString(String text) {
        for (Categoria categoria : Categoria.values()) {
            if (categoria.categoriaOmdb.equalsIgnoreCase(text)) {
                return categoria;
            }
        }
        throw new IllegalArgumentException("Ninguna categoria encontrada: " + text);
    }

    public static Categoria fromEspanol(String text) {
        for (Categoria categoria : Categoria.values()) {
            if (categoria.categoriaEspanol.equalsIgnoreCase(text)) {
                return categoria;
            }
        }
        throw new IllegalArgumentException("Ninguna categoria encontrada: " + text);
    }
}
