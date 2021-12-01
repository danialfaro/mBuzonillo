package com.dalfaro.mbuzonillo.models;


import java.io.Serializable;

public class Paquete implements Serializable {

    String nombre, fecha, proveedor;

    //Constructor
    public Paquete(String nombre, String fecha, String proveedor) {
        this.nombre = nombre;
        this.fecha = fecha;
        this.proveedor = proveedor;
    }
    public Paquete(){}

    //Getters and Settes
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getProveedor() {return proveedor;}

    public void setProveedor(String proveedor) {
        this.proveedor = proveedor;
    }
}
