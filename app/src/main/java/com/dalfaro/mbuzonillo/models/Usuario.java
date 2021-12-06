package com.dalfaro.mbuzonillo.models;

public class Usuario {

    private String nombre;
    private String correo;
    private String profilePicUrl;
    private String[] buzones;
    private long inicioSesion;

    public Usuario () {}

    public Usuario (String nombre, String correo, String profilePicUrl, String[] buzones, long inicioSesion) {
        this.nombre = nombre;
        this.correo = correo;
        this.profilePicUrl = profilePicUrl;
        this.inicioSesion = inicioSesion;
        this.buzones = buzones;
    }

    public Usuario (String nombre, String correo, String profilePicUrl, String[] buzones) {
        this(nombre, correo, profilePicUrl, buzones, System.currentTimeMillis());
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getProfilePicUrl() {
        return profilePicUrl;
    }

    public void setProfilePicUrl(String profilePicUrl) {
        this.profilePicUrl = profilePicUrl;
    }

    public long getInicioSesion() {
        return inicioSesion;
    }

    public void setInicioSesion(long inicioSesion) {
        this.inicioSesion = inicioSesion;
    }
}