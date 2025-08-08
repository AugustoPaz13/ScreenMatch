package com.codigopiojoso.screenmatch.excepcion;

public class NotFoundedDateException extends RuntimeException {
    private String mensaje;

    public NotFoundedDateException(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getMessage() {
        return this.mensaje;
    }
}
