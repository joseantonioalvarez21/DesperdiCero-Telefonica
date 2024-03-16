package es.ufv.ProyectosII.DesperdiCero.back.models;

public class Pedido {
    private int id_pedido;
    private String Estado;
    private String CIF_entidad;
    private String DNI;
    private String Fecha;


    public String getFecha() {
        return Fecha;
    }

    public void setFecha(String fecha) {
        Fecha = fecha;
    }



    public int getId_pedido() {
        return id_pedido;
    }

    public void setId_pedido(int id_pedido) {
        this.id_pedido = id_pedido;
    }

    public String getEstado() {
        return Estado;
    }

    public void setEstado(String estado) {
        Estado = estado;
    }

    public String getCIF_entidad() {
        return CIF_entidad;
    }

    public void setCIF_entidad(String CIF_entidad) {
        this.CIF_entidad = CIF_entidad;
    }

    public String getDNI() {
        return DNI;
    }

    public void setDNI(String DNI) {
        this.DNI = DNI;
    }

    public Pedido(int id_pedido, String estado, String CIF_entidad, String DNI, String Fecha) {
        this.id_pedido = id_pedido;
        Estado = estado;
        this.CIF_entidad = CIF_entidad;
        this.DNI = DNI;
        this.Fecha = Fecha;
    }
}
