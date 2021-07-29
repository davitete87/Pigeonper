package kennedy.com.Pigeonper.Entidades;


import java.io.Serializable;

public class HistorialPerformance implements Serializable{

    private int idHistorialPerformance, idPaloma,idLocalidad,hora;
    private String fecha;
    private double velocidad, distancia;

    public HistorialPerformance(){};

    public HistorialPerformance(int idHistorialPerformance, int idPaloma, int idLocalidad, int hora, String fecha, double velocidad, double distancia) {
        this.idHistorialPerformance = idHistorialPerformance;
        this.idPaloma = idPaloma;
        this.idLocalidad = idLocalidad;
        this.hora = hora;
        this.fecha = fecha;
        this.velocidad = velocidad;
        this.distancia = distancia;
    }

    public int getIdHistorialPerformance() {
        return idHistorialPerformance;
    }

    public void setIdHistorialPerformance(int idHistorialPerformance) {
        this.idHistorialPerformance = idHistorialPerformance;
    }

    public int getIdPaloma() {
        return idPaloma;
    }

    public void setIdPaloma(int idPaloma) {
        this.idPaloma = idPaloma;
    }

    public int getIdLocalidad() {
        return idLocalidad;
    }

    public void setIdLocalidad(int idLocalidad) {
        this.idLocalidad = idLocalidad;
    }

    public int getHora() {
        return hora;
    }

    public void setHora(int hora) {
        this.hora = hora;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public double getVelocidad() {
        return velocidad;
    }

    public void setVelocidad(float velocidad) {
        this.velocidad = velocidad;
    }

    public double getDistancia() {
        return distancia;
    }

    public void setDistancia(float distancia) {
        this.distancia = distancia;
    }
}
