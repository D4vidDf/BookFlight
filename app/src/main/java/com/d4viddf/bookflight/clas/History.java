package com.d4viddf.bookflight.clas;

public class History {
    String tipo, from, to, salida, volver, paradas, identificador;
    int pasajeros;
    long timestamp;

    public History(String tipo, String from, String to, String salida, String volver, String paradas, String identificador, int pasajeros) {
        this.tipo = tipo;
        this.from = from;
        this.to = to;
        this.salida = salida;
        this.volver = volver;
        this.paradas = paradas;
        this.identificador = identificador;
        this.pasajeros = pasajeros;
    }
    public History(String tipo, String from, String to, String salida, String volver, String paradas, String identificador, int pasajeros, long timestamp) {
        this.tipo = tipo;
        this.from = from;
        this.to = to;
        this.salida = salida;
        this.volver = volver;
        this.paradas = paradas;
        this.identificador = identificador;
        this.pasajeros = pasajeros;
        this.timestamp = timestamp;
    }

    public History(String tipo, String from, String to, String salida, String paradas, String identificador, int pasajeros) {
        this.tipo = tipo;
        this.from = from;
        this.to = to;
        this.salida = salida;
        this.paradas = paradas;
        this.identificador = identificador;
        this.pasajeros = pasajeros;
    }

    public History() {
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getSalida() {
        return salida;
    }

    public void setSalida(String salida) {
        this.salida = salida;
    }

    public String getVolver() {
        return volver;
    }

    public void setVolver(String volver) {
        this.volver = volver;
    }

    public String getParadas() {
        return paradas;
    }

    public void setParadas(String paradas) {
        this.paradas = paradas;
    }

    public String getIdentificador() {
        return identificador;
    }

    public void setIdentificador(String identificador) {
        this.identificador = identificador;
    }

    public int getPasajeros() {
        return pasajeros;
    }

    public void setPasajeros(int pasajeros) {
        this.pasajeros = pasajeros;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
