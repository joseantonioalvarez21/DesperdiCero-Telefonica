package es.ufv.DesperdiCero_front;
import es.ufv.DesperdiCero_front.models.*;
import org.apache.commons.io.input.buffer.PeekableInputStream;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService implements Serializable {

    public boolean validarCorreoExistente(String correo) throws Exception {
        API api = new API();
        // Llama al método de la API que valida el correo electrónico existente
        return api.validarCorreoExistente(correo);
    }



    public List<Producto> buscarProductosPorNombre(String nombre) throws Exception {
        API api = new API();
        ArrayList<Producto> todosLosProductos = api.getDatos();

        // Filtrar los productos por nombre usando Java Stream
        List<Producto> productosFiltrados = todosLosProductos.stream()
                .filter(producto -> producto.getNombre().toLowerCase().contains(nombre.toLowerCase()))
                .collect(Collectors.toList());

        return productosFiltrados;
    }

    public Entidad PutListaDatosEntidades(Entidad entidad) throws Exception{//FUNCION PARA OBTENER TODOS DATOS JSON1
        API api = new API();
        api.PutEntidad(entidad);
        return entidad;
    }
    public Producto PutListaDatosProductos(Producto producto) throws Exception{//FUNCION PARA OBTENER TODOS DATOS JSON1
        API api = new API();
        api.PutListaProductos(producto);
        return producto;
    }
    public ArrayList<Producto> getDatos() throws Exception{//FUNCION PARA OBTENER TODOS DATOS JSON1
        API api = new API();
        ArrayList<Producto> listProducto =api.getDatos();
        return listProducto;
    }
    public Producto PutProductoModificado(Producto producto) throws Exception{
        API api = new API();
        api.PutProductoModificado(producto);
        return producto;
    }
    public void eliminarProducto(int idProducto) throws Exception {
        API api = new API();
        api.deleteProducto(idProducto);
    }
    public ArrayList<Usuario> getDatosUsuario() throws Exception{//FUNCION PARA OBTENER TODOS DATOS JSON1
        API api = new API();
        ArrayList<Usuario> listUsuario =api.getUsuarios();
        return listUsuario;
    }
    public Usuario validarCredenciales(Usuario usuario) throws Exception {
        API api = new API();
        // Si api.validarCredenciales devuelve true, regresa el usuario, de lo contrario null
        boolean resultado = api.validarCredenciales(usuario);
        if (resultado) {
            return usuario; // Regresa el usuario si las credenciales son válidas
        } else {
            return null; // Regresa null si las credenciales son inválidas
        }
    }

    public Usuario PutListaDatosUsuarios(Usuario usuario) throws Exception{
        API api = new API();
        api.anadirUsuarioRemotamente(usuario);
        return usuario;
    }
    public void eliminarUsuario(String DNI) throws Exception {
        API api = new API();
        api.deleteUsuario(DNI);
    }

    public String obtenerTipoEntidadPorCorreo(String correo) throws Exception {
        API api = new API();
        return api.obtenerTipoEntidadPorCorreo(correo);
    }
    public String obtenerNifPorCorreo(String correo) throws Exception {
        API api = new API();
        return api.obtenerNifPorCorreo(correo); // Delega la llamada a API
    }
    public String obtenerNombrePorCorreo(String correo) throws Exception {
        API api = new API();
        return api.obtenerNombrePorCorreo(correo); // Delega la llamada a API
    }
    public String obtenerDniPorCorreo(String correo) throws Exception {
        API api = new API();
        return api.obtenerDniPorCorreo(correo); // Delega la llamada a API
    }
    public String obtenerNombreEntidadPorNif(String nif) throws Exception {
        API api = new API();
        return api.obtenerNombreEntidadPorNif(nif); // Delega la llamada a API
    }


    public ArrayList<Entidad> getEntidades (String tipo) throws Exception{//FUNCION PARA OBTENER TODOS DATOS JSON1
        API api = new API();
        ArrayList<Entidad> listaEntidades =api.getEntidades(tipo);
        return listaEntidades;
    }
    //----------------------------------------- Manejo de suscripciones----------------------------------------------
    public ArrayList<Suscripcion> getSuscripciones(String dni) throws Exception{
        API api = new API();
        ArrayList<Suscripcion> listaSuscripciones =api.getSuscripciones(dni);
        return listaSuscripciones;
    }
    public void eliminarSuscripcion(int id_suscripcion) throws Exception {
        API api = new API();
        api.eliminarSuscripcion(id_suscripcion);
    }
    public Suscripcion PutSuscripcion (Suscripcion suscripcion) throws Exception{
        API api = new API();
        api.PutSuscripcion(suscripcion);
        return suscripcion;
    }
    //-----------------------------------------------------------------------------------------------------------------
    public String obteneEntidadPorCorreo(String correo) throws Exception {
        API api = new API();
        return api.obteneEntidadPorCorreo(correo);
    }
    //-------------Javi---------------
    public Usuario PutCambiarContrasena(Usuario usuario) throws Exception{
        API api = new API();
        api.cambiarContrasena(usuario);
        return usuario;
    }
    public Entidad PutCambiarContrasenaEntidad(Entidad entidad) throws Exception{
        API api = new API();
        api.cambiarContrasenaEntidad(entidad);
        return entidad;
    }





    public Usuario getUsuarioPorCorreo(String correo) throws Exception {
        API api = new API();
        return api.getUsuarioPorCorreoAPI(correo); // Delega la llamada a API
    }
    /*public String obtenerNombreUsuarioPorCorreo(String correo) throws Exception {
        API api = new API();
        return api.obtenerNombreUsuarioPorCorreo(correo);
    }*/
    public String obteneEntidadPorCorreoView(String correo) throws Exception {
        API api = new API();
        return api.obteneEntidadPorCorreoView(correo);
    }
    public String obtenerDireccionPorCorreo(String correo) throws Exception {
        API api = new API();
        return api.obtenerDireccionPorCorreo(correo);
    }
    public String obtenerNombrePorCorreoView(String correo) throws Exception {
        API api = new API();
        return api.obtenerNombrePorCorreoView(correo);
    }
    public String obtenerCifEntidadPorCorreo(String correo) throws Exception {
        API api = new API();
        return api.obtenerCifEntidadPorCorreo(correo);
    }
    public String obtenerNombreTrabajadorPorCorreo(String correo) throws Exception {
        API api = new API();
        return api.obtenerNombreTrabajadorPorCorreo(correo);
    }
    public String obtenerApellidosTrabajadorPorCorreo(String correo) throws Exception {
        API api = new API();
        return api.obtenerApellidosTrabajadorPorCorreo(correo);
    }
    public String obtenerDNITrabajadorPorCorreo(String correo) throws Exception {
        API api = new API();
        return api.obtenerDNITrabajadorPorCorreo(correo);
    }


    public Usuario PutUsuarioModificado(Usuario usuario) throws Exception{
        API api = new API();
        api.PutUsuarioModificado(usuario);
        return usuario;
    }

    public boolean validarContrasena(String correoElectronico, String contrasenaActual) {
        API api = new API();
        try {
            // Lógica para validar la contraseña en la base de datos
            return api.validarContrasena(correoElectronico, contrasenaActual);
        } catch (Exception ex) {
            // Manejo de excepciones
            ex.printStackTrace();  // o cualquier otro manejo de errores que necesites
            return false;
        }
    }

    public boolean validarContrasenaEntidad(String correoElectronico, String contrasenaActual) {
        API api = new API();
        try {
            // Lógica para validar la contraseña en la base de datos
            return api.validarContrasenaEntidad(correoElectronico, contrasenaActual);
        } catch (Exception ex) {
            // Manejo de excepciones
            ex.printStackTrace();  // o cualquier otro manejo de errores que necesites
            return false;
        }
    }
    //------------------------------------------------Pedidos-----------------------------------------------------------
    public ArrayList<Pedido> getPedidos(String NIF) throws Exception{
        API api = new API();
        ArrayList<Pedido> listaPedidos =api.getPedidos(NIF);
        return listaPedidos;
    }
}
