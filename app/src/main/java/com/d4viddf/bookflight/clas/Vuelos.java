package com.d4viddf.bookflight.clas;

import androidx.annotation.NonNull;

public class Vuelos {
    String tipo, from, to, salida, volver, paradas, identificador, precio, id, category;
    int pasajeros;

    public Vuelos() {
    }

    public Vuelos(String tipo, String from, String to) {
        this.tipo = tipo;
        this.from = from;
        this.to = to;
    }

    public Vuelos(String tipo, String from, String to, String salida, String volver, String paradas, String identificador, String precio, String id, String cateogry, int pasajeros) {
        this.tipo = tipo;
        this.from = from;
        this.to = to;
        this.salida = salida;
        this.volver = volver;
        this.paradas = paradas;
        this.identificador = identificador;
        this.precio = precio;
        this.id = id;
        this.category = cateogry;
        this.pasajeros = pasajeros;
    }

    public Vuelos(String tipo, String from, String to, String salida, String volver, String paradas, String identificador, int pasajeros) {
        this.tipo = tipo;
        this.from = from;
        this.to = to;
        this.salida = salida;
        this.volver = volver;
        this.paradas = paradas;
        this.identificador = identificador;
        this.pasajeros = pasajeros;
    }

    public String getIdentificador() {
        return identificador;
    }

    public void setIdentificador(String identificador) {
        this.identificador = identificador;
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

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
