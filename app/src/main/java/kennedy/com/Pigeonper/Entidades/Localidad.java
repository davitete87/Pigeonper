package kennedy.com.Pigeonper.Entidades;

import java.io.Serializable;

public class Localidad implements Serializable {
    int idLocalidad;
    String nombreLocalidad;
    double lat,lon;

    public Localidad(int idLocalidad, String nombreLocalidad, double lat, double lon) {
        this.idLocalidad = idLocalidad;
        this.nombreLocalidad = nombreLocalidad;
        this.lat = lat;
        this.lon = lon;
    }

    public Localidad(){}

    public int getIdLocalidad() {
        return idLocalidad;
    }

    public void setIdLocalidad(int idLocalidad) {
        this.idLocalidad = idLocalidad;
    }

    public String getNombreLocalidad() {
        return nombreLocalidad;
    }

    public void setNombreLocalidad(String nombreLocalidad) {
        this.nombreLocalidad = nombreLocalidad;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }
}
