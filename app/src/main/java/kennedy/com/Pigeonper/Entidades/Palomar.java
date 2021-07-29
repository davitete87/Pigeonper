package kennedy.com.Pigeonper.Entidades;

import java.io.Serializable;

public class Palomar implements Serializable {

    private int idPalomar, idLocalidad, idUsuario;
    private String nombre, direccion;

    public Palomar(){}

    public Palomar(int idPalomar, int idLocalidad, int idUsuario, String nombre, String direccion) {
        this.idPalomar = idPalomar;
        this.idLocalidad = idLocalidad;
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.direccion = direccion;
    }

    public int getIdPalomar() {
        return idPalomar;
    }

    public void setIdPalomar(int idPalomar) {
        this.idPalomar = idPalomar;
    }

    public int getIdLocalidad() {
        return idLocalidad;
    }

    public void setIdLocalidad(int idLocalidad) {
        this.idLocalidad = idLocalidad;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
}
