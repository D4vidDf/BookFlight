package com.d4viddf.bookflight.clas;

public class Result {
    String tipo, desde, hacia,precio, identificador,salida,vuelta;
    long asientos, position, disponibles;

    public Result() {
    }

    public Result(String tipo, String desde, String hacia, String precio, long asientos, long disponibles) {
        this.tipo = tipo;
        this.desde = desde;
        this.hacia = hacia;
        this.precio = precio;
        this.asientos = asientos;
        this.disponibles = disponibles;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getDesde() {
        return desde;
    }

    public void setDesde(String desde) {
        this.desde = desde;
    }

    public String getHacia() {
        return hacia;
    }

    public void setHacia(String hacia) {
        this.hacia = hacia;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

    public String getIdentificador() {
        return identificador;
    }

    public void setIdentificador(String identificador) {
        this.identificador = identificador;
    }

    public long getAsientos() {
        return asientos;
    }

    public void setAsientos(long asientos) {
        this.asientos = asientos;
    }

    public String getSalida() {
        return salida;
    }

    public void setSalida(String salida) {
        this.salida = salida;
    }

    public String getVuelta() {
        return vuelta;
    }

    public void setVuelta(String vuelta) {
        this.vuelta = vuelta;
    }

    public long getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public long getDisponibles() {
        return disponibles;
    }

    public void setDisponibles(long disponibles) {
        this.disponibles = disponibles;
    }
}