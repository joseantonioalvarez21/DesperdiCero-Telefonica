package es.ufv.ProyectosII.DesperdiCero.back.dataAccess;


import es.ufv.ProyectosII.DesperdiCero.back.models.Entidad;

import java.sql.*;
import java.util.ArrayList;

public class EntidadesBBDD {
    public static String url = "jdbc:mysql://localhost:3306/DesperdiCero?serverTimezone=UTC";
    public static String username = "root";
    public static String password = "root1234";
    public static boolean entidadExistente(String NIF_entidad) {
        String selectQuery = "SELECT COUNT(*) FROM Entidad WHERE CIF_entidad = ?";

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            try (PreparedStatement statement = connection.prepareStatement(selectQuery)) {
                statement.setString(1, NIF_entidad);

                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        int count = resultSet.getInt(1);
                        return count > 0; // Si count es mayor que 0, significa que ya existe la entidad con ese NIF
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return true; // Tratamiento de error, considerando que ya existe para evitar una posible inserción incorrecta
        }

        return true; // Por defecto, asumir que la entidad existe para evitar una posible inserción incorrecta
    }
    public static boolean escribirEntidadesBBDD(String NIF_entidad, String nombreEntidad, String telefonoEntidad, String correoEntidad, String contrasenaEntidad, String tipoEntidad, String fechaEntidad, String direccionEntidad) {

        String insertQueryEntidades = "INSERT INTO Entidad (CIF_entidad, Nombre, Telefono, Correo_electronico, Contraseña, Tipo, Fecha_alta, Direccion) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            // Desactivar autocommit

            connection.setAutoCommit(false);

            try (PreparedStatement statement = connection.prepareStatement(insertQueryEntidades)) {
                statement.setString(1, NIF_entidad);
                statement.setString(2, nombreEntidad);
                statement.setString(3, telefonoEntidad);
                statement.setString(4, correoEntidad);
                statement.setString(5, contrasenaEntidad);
                statement.setString(6, tipoEntidad);
                statement.setString(7, fechaEntidad);
                statement.setString(8, direccionEntidad);

                // Ejecutar la consulta
                int rowsAffected = statement.executeUpdate();

                // Verificar si la inserción fue exitosa
                if (rowsAffected > 0) {
                    System.out.println("Entidad insertada exitosamente.");
                    // Commit explicito si todo está bien
                    connection.commit();
                    return true;
                } else {
                    System.out.println("Error al insertar la entidad.");
                    // Rollback en caso de error
                    connection.rollback();
                    return false;
                }
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("No va la conexión");
                // Rollback en caso de excepción en la consulta
                connection.rollback();
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Rollback en caso de excepción al establecer la conexión
            return false;
        }
    }
    public ArrayList<Entidad> leerEntidadesBBDD() {

        Connection connection = null;
        ArrayList<Entidad> listaEntidades = new ArrayList<>();

        try {
            connection = DriverManager.getConnection(url, username, password);
            Statement statement = connection.createStatement();
            String query = "SELECT * FROM Entidad";
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                Entidad entidad = new Entidad(
                        resultSet.getString("CIF_entidad"),
                        resultSet.getString("Nombre"),
                        resultSet.getString("Telefono"),
                        resultSet.getString("Correo_electronico"),
                        resultSet.getString("Contraseña"),
                        resultSet.getString("Tipo"),
                        resultSet.getString("Fecha_alta"),
                        resultSet.getString("Direccion"));

                listaEntidades.add(entidad);
            }
            connection.close();
            statement.close();
            resultSet.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return listaEntidades;
    }
    public ArrayList<Entidad> leerEntidadesPoTipoBBDD(String tipo) {

        Connection connection = null;
        ArrayList<Entidad> listaEntidades = new ArrayList<>();

        try {
            connection = DriverManager.getConnection(url, username, password);
            Statement statement = connection.createStatement();
            String query = "SELECT * FROM Entidad WHERE Tipo = '" + tipo + "'";
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                Entidad entidad = new Entidad(
                        resultSet.getString("CIF_entidad"),
                        resultSet.getString("Nombre"),
                        resultSet.getString("Telefono"),
                        resultSet.getString("Correo_electronico"),
                        resultSet.getString("Contraseña"),
                        resultSet.getString("Tipo"),
                        resultSet.getString("Fecha_alta"),
                        resultSet.getString("Direccion"));

                listaEntidades.add(entidad);
            }
            connection.close();
            statement.close();
            resultSet.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return listaEntidades;
    }
}
