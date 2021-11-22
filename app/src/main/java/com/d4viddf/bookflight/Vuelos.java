package com.d4viddf.bookflight;

public class Vuelos {
    String tipo, from, to, salida, volver, paradas;
    int pasajeros;

    public Vuelos(){}

    public Vuelos(String ti, String fr, String t, String sal, String vol, String para, int pasa) {
        this.tipo = ti;
        this.from = fr;
        this.to = t;
        this.salida = sal;
        this.volver = vol;
        this.paradas = para;
        this.pasajeros = pasa;
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

    public int getPasajeros() {
        return pasajeros;
    }

    public void setPasajeros(int pasajeros) {
        this.pasajeros = pasajeros;
    }
}
