package es.ufv.ProyectosII.DesperdiCero.back.models;

public class Producto {
    private int id_productoDisp;
    private String codigo_barras;
    private String nombre;
    private String tipo;
    private String fecha_caducidad;
    private String estado;
    private int cantidad;
    private String nif_entidad;

    public int getId_productoDisp() {
        return id_productoDisp;
    }

    public void setId_productoDisp(int id_productoDisp) {
        this.id_productoDisp = id_productoDisp;
    }

    public String getCodigo_barras() {
        return codigo_barras;
    }

    public void setCodigo_barras(String codigo_barras) {
        this.codigo_barras = codigo_barras;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getFecha_caducidad() {
        return fecha_caducidad;
    }

    public void setFecha_caducidad(String fecha_caducidad) {
        this.fecha_caducidad = fecha_caducidad;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public String getNif_entidad() {
        return nif_entidad;
    }

    public void setNif_entidad(String nif_entidad) {
        this.nif_entidad = nif_entidad;
    }

    public Producto(int id_productoDisp, String codigo_barras, String nombre, String tipo, String fecha_caducidad, String estado, int cantidad, String nif_entidad) {
        this.id_productoDisp = id_productoDisp;
        this.codigo_barras = codigo_barras;
        this.nombre = nombre;
        this.tipo = tipo;
        this.fecha_caducidad = fecha_caducidad;
        this.estado = estado;
        this.cantidad = cantidad;
        this.nif_entidad = nif_entidad;
    }
}
