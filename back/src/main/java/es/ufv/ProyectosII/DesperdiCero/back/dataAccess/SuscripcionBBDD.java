package es.ufv.ProyectosII.DesperdiCero.back.dataAccess;

import es.ufv.ProyectosII.DesperdiCero.back.models.Suscripcion;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.sql.*;
import java.util.ArrayList;

public class SuscripcionBBDD {
    private static final String url = "jdbc:mysql://localhost:3306/desperdicero?serverTimezone=UTC";
    private static final String username = "root";
    private static final String password = "root1234";

    public ArrayList<Suscripcion> leerSuscripcionBBDD() {

        Connection connection = null;
        ArrayList<Suscripcion> listaSuscripciones = new ArrayList<>();

        try {
            connection = DriverManager.getConnection(url, username, password);
            Statement statement = connection.createStatement();
            String query = "SELECT * FROM Suscripcion";
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                Suscripcion suscripcion = new Suscripcion(
                        resultSet.getInt("Id_suscripcion"),
                        resultSet.getString("DNI"),
                        resultSet.getString("CIF_entidad"),
                        resultSet.getString("fecha_alta"));
                listaSuscripciones.add(suscripcion);
            }
            connection.close();
            statement.close();
            resultSet.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return listaSuscripciones;
    }

    public static boolean escribirSuscripcionBBDD(String dni, String cif) {

        String insertQueryEntidades = "INSERT INTO Suscripcion (DNI, CIF_entidad, Fecha_alta) VALUES (?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(url, username, password)) {

            if (comprobarExistenciaSuscripcion(dni, cif)) {
                System.out.println("La suscripción ya existe en la base de datos.");
                return false;
            }
            try (PreparedStatement statement = connection.prepareStatement(insertQueryEntidades)) {
                statement.setString(1, dni);
                statement.setString(2, cif);

                LocalDate fechaActual = LocalDate.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                String fechaFormateada = fechaActual.format(formatter);
                statement.setString(3, fechaFormateada);

                // Ejecutar la consulta
                int rowsAffected = statement.executeUpdate();

                // Verificar si la inserción fue exitosa
                if (rowsAffected > 0) {
                    System.out.println("Suscipción insertada exitosamente.");
                    // Commit explicito si todo está bien
                    connection.commit();
                    return true;
                } else {
                    System.out.println("Error al insertar la suscripcion.");
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
    public static boolean comprobarExistenciaSuscripcion(String dni, String cif) {
        String selectQuery = "SELECT COUNT(*) AS count FROM Suscripcion WHERE DNI = ? AND CIF_entidad = ?";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(selectQuery)) {

            statement.setString(1, dni);
            statement.setString(2, cif);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                int count = resultSet.getInt("count");
                return count > 0;
            }
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean eliminarSuscripcionBBDD(int id_suscripcion) {
        String deleteSuscripcionQuery = "DELETE FROM Suscripcion WHERE Id_suscripcion = ?";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement psDeleteSuscripcion = connection.prepareStatement(deleteSuscripcionQuery)) {
            psDeleteSuscripcion.setInt(1, id_suscripcion);
            int rowsAffected = psDeleteSuscripcion.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Error al eliminar el usuario
        }
    }

}
