package es.ufv.ProyectosII.DesperdiCero.back.dataAccess;

import es.ufv.ProyectosII.DesperdiCero.back.models.Pedido;

import java.sql.*;
import java.util.ArrayList;

public class PedidoBBDD {
    private static final String url = "jdbc:mysql://localhost:3306/desperdicero?serverTimezone=UTC";
    private static final String username = "root";
    private static final String password = "root1234";
    public ArrayList<Pedido> leerPedidosBBDD() {
        Connection connection = null;
        ArrayList<Pedido> listaPedidos = new ArrayList<>();

        try {
            connection = DriverManager.getConnection(url, username, password);
            Statement statement = connection.createStatement();
            String query = "SELECT * FROM Pedido";
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                Pedido pedido = new Pedido(
                        resultSet.getInt("Id_pedido"),
                        resultSet.getString("Estado_pedido"),
                        resultSet.getString("CIF_entidad"),
                        resultSet.getString("DNI"),
                        resultSet.getString("Fecha"));
                listaPedidos.add(pedido);
            }
            connection.close();
            statement.close();
            resultSet.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return listaPedidos;
    }
}
