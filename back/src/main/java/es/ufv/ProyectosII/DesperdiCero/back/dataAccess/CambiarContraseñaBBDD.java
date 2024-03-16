package es.ufv.ProyectosII.DesperdiCero.back.dataAccess;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.sql.*;

@Service
public class CambiarContraseñaBBDD {
    public static String url = "jdbc:mysql://localhost:3306/DesperdiCero?serverTimezone=UTC";
    public static String username = "root";
    public static String password = "root1234";

    public static String cambiarContrasena(String correoElectronico, String nuevaContrasena) {
        String updateQuery = "UPDATE usuarioentidad SET Contraseña = ? WHERE Correo_electronico = ?";

        try (Connection conn = DriverManager.getConnection(url, username, password);
             PreparedStatement pstmt = conn.prepareStatement(updateQuery)) {

            // Establecer los parámetros de la consulta preparada
            pstmt.setString(1, nuevaContrasena);
            pstmt.setString(2, correoElectronico);

            // Ejecutar la actualización
            int filasActualizadas = pstmt.executeUpdate();

            // Verificar si la contraseña se actualizó correctamente
            if (filasActualizadas > 0) {
                return "Contraseña actualizada correctamente";
            } else {
                return "No se pudo actualizar la contraseña. Usuario no encontrado o contraseña actual igual a la nueva contraseña.";
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return "Error al cambiar la contraseña: " + e.getMessage();
        }
    }


    public static String obtenerContrasena(String correoElectronico, String contrasena) {
        String query = "SELECT Contraseña FROM usuarioentidad WHERE Correo_electronico = ? AND Contraseña = ?";

        try (Connection conn = DriverManager.getConnection(url, username, password);
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            // Establecer los parámetros de la consulta preparada
            pstmt.setString(1, correoElectronico);
            pstmt.setString(2, contrasena);

            System.out.println("Ejecutando consulta SQL en usuarioentidad: " + query);

            // Ejecutar la consulta y obtener la contraseña si se encuentra el usuario con la contraseña
            try (ResultSet resultSet = pstmt.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString("Contraseña");
                }
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null; // Retorna null si no se encuentra el usuario con la contraseña
    }

    public static String obtenerContrasenaEntidad(String correoElectronico, String contrasena) {
        String query = "SELECT Contraseña FROM entidad WHERE Correo_electronico = ? AND Contraseña = ?";

        try (Connection conn = DriverManager.getConnection(url, username, password);
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            // Establecer los parámetros de la consulta preparada
            pstmt.setString(1, correoElectronico);
            pstmt.setString(2, contrasena);

            System.out.println("Ejecutando consulta SQL en entidad: " + query);

            // Ejecutar la consulta y obtener la contraseña si se encuentra el usuario con la contraseña
            try (ResultSet resultSet = pstmt.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString("Contraseña");
                }
            }

            System.out.println("Contraseña: "+ contrasena);


        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null; // Retorna null si no se encuentra el usuario con la contraseña
    }





    public static String cambiarContrasenaEntidad(String correoElectronico, String nuevaContrasena) {
        String updateQuery = "UPDATE entidad SET Contraseña = ? WHERE Correo_electronico = ?";

        try (Connection conn = DriverManager.getConnection(url, username, password);
             PreparedStatement pstmt = conn.prepareStatement(updateQuery)) {

            // Establecer los parámetros de la consulta preparada
            pstmt.setString(1, nuevaContrasena);
            pstmt.setString(2, correoElectronico);

            // Ejecutar la actualización
            int filasActualizadas = pstmt.executeUpdate();

            // Verificar si la contraseña se actualizó correctamente
            if (filasActualizadas > 0) {
                return "Contraseña actualizada correctamente";
            } else {
                return "No se pudo actualizar la contraseña. Usuario no encontrado o contraseña actual igual a la nueva contraseña.";
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return "Error al cambiar la contraseña: " + e.getMessage();
        }
    }

}
