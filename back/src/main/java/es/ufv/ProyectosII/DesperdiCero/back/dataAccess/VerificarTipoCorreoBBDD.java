package es.ufv.ProyectosII.DesperdiCero.back.dataAccess;

import org.springframework.stereotype.Service;

import java.sql.*;

@Service
public class VerificarTipoCorreoBBDD {
    public static String url = "jdbc:mysql://localhost:3306/DesperdiCero?serverTimezone=UTC";
    public static String username = "root";
    public static String password = "root1234";

    public static String obteneEntidadPorCorreo(String correo) {
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
                    System.out.println("UsuarioEntidad");
                    return "UsuarioEntidad"; // Devolvemos el nombre de la tabla "UsuarioEntidad"
                }
            }

            // Establecemos el correo electrónico como parámetro de la consulta para Entidad
            psEntidad.setString(1, correo);
            try (ResultSet resultSet = psEntidad.executeQuery()) {
                if (resultSet.next()) {
                    // Si se encuentra un registro, significa que el correo está asociado a un registro en Entidad
                    System.out.println("Hola soy una Entidad");
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

    // Método para obtener la dirección de la entidad asociada al correo
    public static String obtenerDireccionEntidad(String correo) {
        String queryEntidad = "SELECT direccion FROM Entidad WHERE Correo_electronico = ?";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement psEntidad = connection.prepareStatement(queryEntidad)) {

            psEntidad.setString(1, correo);
            try (ResultSet resultSet = psEntidad.executeQuery()) {
                if (resultSet.next()) {
                    System.out.println("Direccion he entrado aqui");
                    return resultSet.getString("Direccion");
                }
            }

            return null; // Si no se encuentra el correo en la tabla Entidad, devolvemos null

        } catch (SQLException e) {
            e.printStackTrace();
            return null; // Error al consultar la base de datos
        }
    }
    public static String obtenerNombreEntidad(String correo) {
        String queryEntidad = "SELECT nombre FROM Entidad WHERE Correo_electronico = ?";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement psEntidad = connection.prepareStatement(queryEntidad)) {

            psEntidad.setString(1, correo);
            try (ResultSet resultSet = psEntidad.executeQuery()) {
                if (resultSet.next()) {
                    System.out.println("Nombre he entrado aqui");

                    return resultSet.getString("Nombre");
                }
            }

            return null; // Si no se encuentra el correo en la tabla Entidad, devolvemos null

        } catch (SQLException e) {
            e.printStackTrace();
            return null; // Error al consultar la base de datos
        }
    }

    public static String obtenerCifEntidadEntidad(String correo) {
        String queryEntidad = "SELECT cif_entidad FROM Entidad WHERE Correo_electronico = ?";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement psEntidad = connection.prepareStatement(queryEntidad)) {

            psEntidad.setString(1, correo);
            try (ResultSet resultSet = psEntidad.executeQuery()) {
                if (resultSet.next()) {
                    System.out.println("Nombre he entrado aqui");

                    return resultSet.getString("Nombre");
                }
            }

            return null; // Si no se encuentra el correo en la tabla Entidad, devolvemos null

        } catch (SQLException e) {
            e.printStackTrace();
            return null; // Error al consultar la base de datos
        }
    }
    public static String obtenerNombreTrabajador(String correo) {
        String queryEntidad = "SELECT nombre FROM Usuarioentidad WHERE Correo_electronico = ?";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement psEntidad = connection.prepareStatement(queryEntidad)) {

            psEntidad.setString(1, correo);
            try (ResultSet resultSet = psEntidad.executeQuery()) {
                if (resultSet.next()) {
                    System.out.println("Nombre he entrado aqui");

                    return resultSet.getString("Nombre");
                }
            }

            return null; // Si no se encuentra el correo en la tabla Entidad, devolvemos null

        } catch (SQLException e) {
            e.printStackTrace();
            return null; // Error al consultar la base de datos
        }
    }
    public static String obtenerApellidoTrabajador(String correo) {
        String queryEntidad = "SELECT Apellidos FROM Usuarioentidad WHERE Correo_electronico = ?";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement psEntidad = connection.prepareStatement(queryEntidad)) {

            psEntidad.setString(1, correo);
            try (ResultSet resultSet = psEntidad.executeQuery()) {
                if (resultSet.next()) {
                    System.out.println("Nombre he entrado aqui");

                    return resultSet.getString("Apellidos");
                }
            }

            return null; // Si no se encuentra el correo en la tabla Entidad, devolvemos null

        } catch (SQLException e) {
            e.printStackTrace();
            return null; // Error al consultar la base de datos
        }
    }
    public static String obtenerDNITrabajador(String correo) {
        String queryEntidad = "SELECT DNI FROM Usuarioentidad WHERE Correo_electronico = ?";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement psEntidad = connection.prepareStatement(queryEntidad)) {

            psEntidad.setString(1, correo);
            try (ResultSet resultSet = psEntidad.executeQuery()) {
                if (resultSet.next()) {
                    System.out.println("Nombre he entrado aqui");

                    return resultSet.getString("DNI");
                }
            }

            return null; // Si no se encuentra el correo en la tabla Entidad, devolvemos null

        } catch (SQLException e) {
            e.printStackTrace();
            return null; // Error al consultar la base de datos
        }
    }

}
