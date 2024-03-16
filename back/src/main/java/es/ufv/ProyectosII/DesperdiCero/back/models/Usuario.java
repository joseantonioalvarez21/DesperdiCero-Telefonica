package es.ufv.ProyectosII.DesperdiCero.back.models;


public class Usuario {


    private String DNI;
    private String Nombre;
    private String Contraseña;
    private String NIF_Entidad;
    private String Apellidos;
    private String Correo_Electronico;

    public Usuario(String DNI, String Nombre, String Apellidos, String Correo_Electronico, String Contraseña, String NIF_Entidad) {
        this.DNI = DNI;
        this.Nombre = Nombre;
        this.Apellidos = Apellidos;
        this.Correo_Electronico = Correo_Electronico;
        this.Contraseña = Contraseña;
        this.NIF_Entidad = NIF_Entidad;
    }

    public void setDNI(String DNI) {

        this.DNI = DNI;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public void setApellidos(String apellidos) {
        Apellidos = apellidos;
    }

    public void setCorreo_Electronico(String correo_Electronico) {
        Correo_Electronico = correo_Electronico;
    }

    public void setContraseña(String contraseña) {
        Contraseña = contraseña;
    }

    public void setNIF_Entidad(String NIF_Entidad) {
        this.NIF_Entidad = NIF_Entidad;
    }



    public String getDNI() {
        return DNI;
    }

    public String getNombre() {
        return Nombre;
    }

    public String getApellidos() {
        return Apellidos;
    }

    public String getCorreo_Electronico() {
        return Correo_Electronico;
    }

    public String getContraseña() {
        return Contraseña;
    }

    public String getNIF_Entidad() {
        return NIF_Entidad;
    }





}
