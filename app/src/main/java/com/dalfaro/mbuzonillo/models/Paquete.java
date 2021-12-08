package com.dalfaro.mbuzonillo.models;


import java.io.Serializable;

public class Paquete implements Serializable {

    String uid;
    String nombre;
    long fecha;
    String proveedor;
    String imagenUrl;
    String peso;

    //Constructor
    public Paquete(String nombre, long fecha, String proveedor, String imagenUrl, String peso) {
        this.nombre = nombre;
        this.fecha = fecha;
        this.proveedor = proveedor;
        this.imagenUrl = imagenUrl;
        this.peso = peso;
    }
    public Paquete(){}

    //Getters and Setters

    public String getUid() {
        return uid;
    }
    public void setUid(String uid) { this.uid = uid; }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public long getFecha() { return fecha; }

    public void setFecha(long fecha) {
        this.fecha = fecha;
    }

    public String getProveedor() {return proveedor;}

    public void setProveedor(String proveedor) {
        this.proveedor = proveedor;
    }

    public String getImagenUrl() {
        return imagenUrl;
    }

    public void setImagenUrl(String imagenUrl) {
        this.imagenUrl = imagenUrl;
    }

    public String getPeso() {
        return peso;
    }

    public void setPeso(String peso) {
        this.peso = peso;
    }
}
