package com.codigopiojoso.screenmatch.service;

public interface IConvierteDatos {
    <T> T obtenerDatos(String json, Class <T> clase);
}
