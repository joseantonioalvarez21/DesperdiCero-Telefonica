package es.ufv.DesperdiCero_front.models;

public class Suscripcion {
    int id_suscripcion;
    String dni;
    String cif;
    String fecha_alta;

    public Suscripcion(String dni, String cif) {
        this.dni = dni;
        this.cif = cif;
    }

    public String getFecha_alta() {
        return fecha_alta;
    }

    public void setFecha_alta(String fecha_alta) {
        this.fecha_alta = fecha_alta;
    }



    public int getId_suscripcion() {
        return id_suscripcion;
    }

    public void setId_suscripcion(int id_suscripcion) {
        this.id_suscripcion = id_suscripcion;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getCif() {
        return cif;
    }

    public void setCif(String cif) {
        this.cif = cif;
    }

    public Suscripcion() {
        this.id_suscripcion = id_suscripcion;
        this.dni = dni;
        this.cif = cif;
        this.fecha_alta = fecha_alta;
    }

}
