package es.ufv.ProyectosII.DesperdiCero.back.dataAccess;

import es.ufv.ProyectosII.DesperdiCero.back.models.Usuario;

import java.sql.*;
import java.util.ArrayList;

public class UsuarioBBDD {
    public static String url = "jdbc:mysql://localhost:3306/DesperdiCero?serverTimezone=UTC";
    public static String username = "root";

    public static String password = "root1234";
    public ArrayList<Usuario> leerUsuariosBBDD() {
        Connection connection = null;
        ArrayList<Usuario> listaUsuarios = new ArrayList<>();

        try {
            connection = DriverManager.getConnection(url, username, password);
            Statement statement = connection.createStatement();
            String query = "SELECT * FROM UsuarioEntidad";
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                Usuario usuario = new Usuario(
                        resultSet.getString("DNI"),
                        resultSet.getString("Nombre"),
                        resultSet.getString("Apellidos"),
                        resultSet.getString("Correo_Electronico"),
                        resultSet.getString("Contraseña"),
                        resultSet.getString("CIF_Entidad"));

                listaUsuarios.add(usuario);
            }
            connection.close();
            statement.close();
            resultSet.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return listaUsuarios;
    }
    public static boolean anadirUsuario(String dni, String nombre, String apellidos, String correoElectronico, String contrasena, String nifEntidad) {
        String queryVerificarDNI = "SELECT * FROM usuarioentidad WHERE DNI = ?";
        String queryVerificarNIF = "SELECT * FROM entidad WHERE CIF_entidad = ?";
        String queryInsertarUsuario = "INSERT INTO usuarioentidad (DNI, Nombre, Apellidos, Correo_electronico, Contraseña, CIF_entidad) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(url, username, password)) {
            // Verificar si ya existe un usuario con el mismo DNI
            try (PreparedStatement pstmtVerificarDNI = conn.prepareStatement(queryVerificarDNI)) {
                pstmtVerificarDNI.setString(1, dni);
                try (ResultSet rsDNI = pstmtVerificarDNI.executeQuery()) {
                    if (rsDNI.next()) {
                        System.out.println("Ya existe un usuario con el DNI proporcionado.");
                        return false;
                    }
                }
            }

            // Verificar si el NIF proporcionado existe en la tabla Entidad
            try (PreparedStatement pstmtVerificarNIF = conn.prepareStatement(queryVerificarNIF)) {
                pstmtVerificarNIF.setString(1, nifEntidad);
                try (ResultSet rsNIF = pstmtVerificarNIF.executeQuery()) {
                    if (!rsNIF.next()) {
                        System.out.println("El NIF de la entidad no existe en la tabla Entidad.");
                        return false;
                    }
                }
            }

            // Insertar el nuevo usuario
            try (PreparedStatement pstmtInsertarUsuario = conn.prepareStatement(queryInsertarUsuario)) {
                pstmtInsertarUsuario.setString(1, dni);
                pstmtInsertarUsuario.setString(2, nombre);
                pstmtInsertarUsuario.setString(3, apellidos);
                pstmtInsertarUsuario.setString(4, correoElectronico);
                pstmtInsertarUsuario.setString(5, contrasena);
                pstmtInsertarUsuario.setString(6, nifEntidad);

                int filasInsertadas = pstmtInsertarUsuario.executeUpdate();
                if (filasInsertadas > 0) {
                    System.out.println("Usuario añadido con éxito.");
                    return true;
                } else {
                    System.out.println("No se pudo añadir el usuario.");
                    return false;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Manejar la excepción
            return false;
        }
    }
    public static boolean dniExistente(String DNI) {
        String selectQuery = "SELECT COUNT(*) FROM usuarioentidad WHERE DNI = ?";

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            try (PreparedStatement statement = connection.prepareStatement(selectQuery)) {
                statement.setString(1, DNI);

                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        int count = resultSet.getInt(1);
                        return count > 0; // Si count es mayor que 0, significa que ya existe un usuario con ese DNI
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return true; // Tratamiento de error, considerando que ya existe para evitar una posible inserción incorrecta
        }

        return false; // Si no hay errores y el count es 0, el DNI no existe
    }
    //Método para eliminar un usuario de la bbdd
    public static boolean eliminarUsuarioBBDD(String DNI) {
        String deleteUsuarioQuery = "DELETE FROM usuarioentidad WHERE DNI = ?";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement psDeleteUsuario = connection.prepareStatement(deleteUsuarioQuery)) {

            // Eliminar el usuario de la tabla usuarioentidad
            psDeleteUsuario.setString(1, DNI);
            int rowsAffected = psDeleteUsuario.executeUpdate();

            return rowsAffected > 0; // Devuelve true si el usuario ha sido eliminado correctamente

        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Error al eliminar el usuario
        }
    }
    public String obtenerTipoEntidadPorNIF(String NIF) {
        String queryTipoEntidad = "SELECT Tipo FROM Entidad WHERE CIF_entidad = ?";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement psTipoEntidad = connection.prepareStatement(queryTipoEntidad)) {

            psTipoEntidad.setString(1, NIF);
            try (ResultSet resultSet = psTipoEntidad.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString("Tipo");
                } else {
                    return null; // NIF no encontrado
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null; // Error al consultar la base de datos
        }
    }

    public String obtenerNifPorCorreo(String correo) {
        // Primera consulta en la tabla UsuarioEntidad
        String queryUsuarioEntidad = "SELECT CIF_Entidad FROM UsuarioEntidad WHERE Correo_Electronico = ?";
        // Segunda consulta en la tabla Entidad
        String queryEntidad = "SELECT CIF_Entidad FROM Entidad WHERE Correo_Electronico = ?";

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            // Intentar obtener CIF_Entidad de UsuarioEntidad primero
            try (PreparedStatement preparedStatement = connection.prepareStatement(queryUsuarioEntidad)) {
                preparedStatement.setString(1, correo);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        return resultSet.getString("CIF_Entidad");
                    }
                }
            }

            // Si no se encuentra en UsuarioEntidad, buscar en Entidad
            try (PreparedStatement preparedStatement = connection.prepareStatement(queryEntidad)) {
                preparedStatement.setString(1, correo);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        return resultSet.getString("CIF_Entidad");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Retorna null si no se encuentra el CIF_Entidad en ninguna de las dos tablas
    }


    public String obtenerNombreEntidadPorNif(String NIF) {
        String queryNombreEntidad = "SELECT Nombre FROM Entidad WHERE CIF_entidad = ?";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement psNombreEntidad = connection.prepareStatement(queryNombreEntidad)) {

            psNombreEntidad.setString(1, NIF);
            try (ResultSet resultSet = psNombreEntidad.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString("Nombre");
                } else {
                    return null; // NIF no encontrado
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null; // Error al consultar la base de datos
        }
    }

    public String obtenerNombrePorCorreo(String correo) {
        // Primera consulta en la tabla UsuarioEntidad
        String queryUsuarioEntidad = "SELECT Nombre FROM UsuarioEntidad WHERE Correo_Electronico = ?";

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            // Intentar obtener CIF_Entidad de UsuarioEntidad primero
            try (PreparedStatement preparedStatement = connection.prepareStatement(queryUsuarioEntidad)) {
                preparedStatement.setString(1, correo);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        return resultSet.getString("Nombre");
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Retorna null si no se encuentra el CIF_Entidad en ninguna de las dos tablas
    }
    public String obtenerDniPorCorreo(String correo) {
        // Primera consulta en la tabla UsuarioEntidad
        String queryUsuarioEntidad = "SELECT DNI FROM UsuarioEntidad WHERE Correo_Electronico = ?";

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            // Intentar obtener CIF_Entidad de UsuarioEntidad primero
            try (PreparedStatement preparedStatement = connection.prepareStatement(queryUsuarioEntidad)) {
                preparedStatement.setString(1, correo);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        return resultSet.getString("DNI");
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Retorna null si no se encuentra el CIF_Entidad en ninguna de las dos tablas
    }
    public String obteneEntidadPorCorreo(String correo) {
        // Intentamos obtener el nombre desde la tabla UsuarioEntidad usando el correo electrónico
        String queryNombreUsuarioEntidad = "SELECT Nombre FROM UsuarioEntidad WHERE Correo_electronico = ?";
        // Intentamos obtener el nombre desde la tabla Entidad usando el correo electrónico
        String queryNombreEntidad = "SELECT Nombre FROM Entidad WHERE Correo_electronico = ?";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement psUsuarioEntidad = connection.prepareStatement(queryNombreUsuarioEntidad);
             PreparedStatement psEntidad = connection.prepareStatement(queryNombreEntidad)) {

            // Establecemos el correo electrónico como parámetro de la consulta para UsuarioEntidad
            psUsuarioEntidad.setString(1, correo);
            try (ResultSet resultSet = psUsuarioEntidad.executeQuery()) {
                if (resultSet.next()) {
                    // Si se encuentra un registro, significa que el correo está asociado a un registro en UsuarioEntidad
                    return "UsuarioEntidad"; // Devolvemos el nombre de la tabla "UsuarioEntidad"
                }
            }

            // Establecemos el correo electrónico como parámetro de la consulta para Entidad
            psEntidad.setString(1, correo);
            try (ResultSet resultSet = psEntidad.executeQuery()) {
                if (resultSet.next()) {
                    // Si se encuentra un registro, significa que el correo está asociado a un registro en Entidad
                    return "Entidad"; // Devolvemos el nombre de la tabla "Entidad"
                }
            }

            // Si no se encuentra el correo en ninguna de las dos tablas, devolvemos "no encontrado"
            return "no encontrado";

        } catch (SQLException e) {
            e.printStackTrace();
            return "error_bd"; // Error al consultar la base de datos
        }
    }
    //Funcion para modificar el usuario
    public static boolean modificarUsuarioBBDD(String DNI, String Nombre, String Apellidos, String Correo_Electronico, String Contraseña, String NIF_Entidad) {
        String updateUsuario = "UPDATE usuarioentidad SET dni = ?, nombre = ?, apellidos = ?, correo_electronico = ?, contraseña = ? , CIF_entidad = ? WHERE dni = ?";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement psUpdateUsuario = connection.prepareStatement(updateUsuario)) {

            // Actualizar la tabla de usuarios
            psUpdateUsuario.setString(1, DNI);
            psUpdateUsuario.setString(2, Nombre);
            psUpdateUsuario.setString(3, Apellidos);
            psUpdateUsuario.setString(4, Correo_Electronico);
            psUpdateUsuario.setString(5, Contraseña);
            psUpdateUsuario.setString(6, NIF_Entidad);
            psUpdateUsuario.setString(7, DNI);
            System.out.println("Usuarios Modificados");

            //psUpdateUsuario.setString(7, dni); // El DNI es la clave primaria, por lo que lo usamos como criterio de búsqueda

            int rowsAffected = psUpdateUsuario.executeUpdate();

            // Verificar si se actualizaron filas
            if (rowsAffected > 0) {
                return true; // Usuario actualizado con éxito
            } else {
                return false; // No se encontró el usuario para actualizar
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Error al actualizar el usuario
        }
    }



}