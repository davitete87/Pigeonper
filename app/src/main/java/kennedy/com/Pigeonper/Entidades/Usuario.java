package kennedy.com.Pigeonper.Entidades;

import java.io.Serializable;
import java.util.ArrayList;

public class Usuario implements Serializable { //SE IMPLEMENTA SERIALIZABLE PARA ENVIAR LA CLASE EN UN BUNDLE

    private String nom, ape, email, psw, direccion, telefono, dni;
    private int idUsu;
    //ArrayList<Usuario> model;

    public Usuario() {
    }

    public Usuario(String nom, String ape, String email, String psw, String direccion, String telefono, String dni, int idUsu) {
        this.nom = nom;
        this.ape = ape;
        this.email = email;
        this.psw = psw;
        this.direccion = direccion;
        this.telefono = telefono;
        this.dni = dni;
        this.idUsu = idUsu;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getApe() {
        return ape;
    }

    public void setApe(String ape) {
        this.ape = ape;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPsw() {
        return psw;
    }

    public void setPsw(String psw) {
        this.psw = psw;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public int getIdUsu() {
        return idUsu;
    }

    public void setIdUsu(int idUsu) {
        this.idUsu = idUsu;
    }
}
