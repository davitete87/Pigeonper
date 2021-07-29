package kennedy.com.Pigeonper.Entidades;

import java.io.Serializable;

public class Palomas implements Serializable {

    int  edad, numero, idHabitaculo,id_Paloma, estado;
    String raza;

    public Palomas(){}

    public Palomas(int edad, int numero, int idHabitaculo, String raza, int id_Paloma,int estado) {
        this.edad = edad;
        this.numero = numero;
        this.idHabitaculo = idHabitaculo;
        this.raza = raza;
        this.id_Paloma=id_Paloma;
        this.estado=estado;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public int getIdHabitaculo() {
        return idHabitaculo;
    }

    public void setIdHabitaculo(int idHabitaculo) {
        this.idHabitaculo = idHabitaculo;
    }

    public String getRaza() {
        return raza;
    }

    public void setRaza(String raza) {
        this.raza = raza;
    }

    public int getId_Paloma() {
        return id_Paloma;
    }

    public void setId_Paloma(int id_Paloma) {
        this.id_Paloma = id_Paloma;
    }
}
