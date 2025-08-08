package com.codigopiojoso.screenmatch.model;

public enum Categoria {
    ACCION("Action"),
    AVENTURA("Adventure"),
    ANIMACION("Animation"),
    COMEDIA("Comedy"),
    CRIMEN("Crime"),
    DOCUMENTAL("Documentary"),
    DRAMA("Drama"),
    FAMILIA("Family"),
    FANTASIA("Fantasy"),
    HISTORICO("History"),
    HORROR("Horror"),
    MUSICAL("Musical"),
    MISTERIO("Mystery"),
    ROMANCE("Romance"),
    CIENCIA_FICCION("Sci-Fi"),
    TV_MOVIE("TV Movie"),
    THRILLER("Thriller"),
    GUERRA("War"),
    WESTERN("Western");

    public final String categoriaOmdb;

    private Categoria(String categoriaOmdb) {
        this.categoriaOmdb = categoriaOmdb;
    }

    public static Categoria fromString(String text) {
        for (Categoria categoria : Categoria.values()) {
            if (categoria.categoriaOmdb.equalsIgnoreCase(text)) {
                return categoria;
            }
        }
        throw new IllegalArgumentException("Ninguna categoria encontrada: " + text);
    }
}
