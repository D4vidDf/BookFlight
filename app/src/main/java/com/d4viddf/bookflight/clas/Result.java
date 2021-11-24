package com.d4viddf.bookflight.clas;

public class Result {
    String tipo, desde, hacia,precio, identificador;
    int asientos;

    public Result() {
    }

    public Result(String tipo, String desde, String hacia, String precio, int asientos) {
        this.tipo = tipo;
        this.desde = desde;
        this.hacia = hacia;
        this.precio = precio;
        this.asientos = asientos;
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

    public int getAsientos() {
        return asientos;
    }

    public void setAsientos(int asientos) {
        this.asientos = asientos;
    }
}