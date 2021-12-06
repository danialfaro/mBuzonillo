package com.dalfaro.mbuzonillo.models;

public class Buzon {

    String uid;
    String nombre;

    //Constructor
    public Buzon(String nombre) {
        this.nombre = nombre;
    }
    public Buzon(){}

    //Getters and Setters
    public void setUid(String uid) { this.uid = uid; }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
