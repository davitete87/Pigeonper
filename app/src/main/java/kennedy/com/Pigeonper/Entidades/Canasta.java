package kennedy.com.Pigeonper.Entidades;


import java.io.Serializable;

public class Canasta implements Serializable {

    private int idCanasta,idLocalidad,idPalomar,idHabitaculo, idEstado;
    private String nombre, dia, hora;

    public Canasta(){}

    public Canasta(int idCanasta, int idLocalidad, int idPalomar, String nombre, String dia, String hora,int idHabitaculo, int idEstado) {
        this.idCanasta = idCanasta;
        this.idLocalidad = idLocalidad;
        this.idPalomar = idPalomar;
        this.nombre = nombre;
        this.dia = dia;
        this.hora = hora;
        this.idEstado=idEstado;
        this.idHabitaculo=idHabitaculo;

    }

    public int getIdHabitaculo() {
        return idHabitaculo;
    }

    public void setIdHabitaculo(int idHabitaculo) {
        this.idHabitaculo = idHabitaculo;
    }

    public int getIdEstado() {
        return idEstado;
    }

    public void setIdEstado(int idEstado) {
        this.idEstado = idEstado;
    }

    public int getIdCanasta() {
        return idCanasta;
    }

    public void setIdCanasta(int idCanasta) {
        this.idCanasta = idCanasta;
    }

    public int getIdLocalidad() {
        return idLocalidad;
    }

    public void setIdLocalidad(int idLocalidad) {
        this.idLocalidad = idLocalidad;
    }

    public int getIdPalomar() {
        return idPalomar;
    }

    public void setIdPalomar(int idPalomar) {
        this.idPalomar = idPalomar;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDia() {
        return dia;
    }

    public void setDia(String dia) {
        this.dia = dia;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }
}
