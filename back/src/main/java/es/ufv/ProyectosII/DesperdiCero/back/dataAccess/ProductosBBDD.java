package es.ufv.ProyectosII.DesperdiCero.back.dataAccess;

import es.ufv.ProyectosII.DesperdiCero.back.models.Producto;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.sql.*;
import java.util.ArrayList;
import java.util.Properties;

public class ProductosBBDD {
    //Parámetros de la base de datos para realizar la conexión
    public static String url = "jdbc:mysql://localhost:3306/DesperdiCero?serverTimezone=UTC";
    public static String username = "root";
    public static String password = "root1234";
    public ArrayList<Producto> leerProductosBBDD() {

        Connection connection = null;
        ArrayList<Producto> listaProductos = new ArrayList<>();

        try {
            connection = DriverManager.getConnection(url, username, password);
            Statement statement = connection.createStatement();
            String query = "SELECT productodisp.*, producto.Nombre AS Nombre, producto.Tipo AS Tipo " +
                    "FROM productodisp " + "INNER JOIN producto ON productodisp.Codigo_barras = producto.Codigo_barras";
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                Producto producto = new Producto(
                        resultSet.getInt("Id_productoDisp"),
                        resultSet.getString("Codigo_barras"),
                        resultSet.getString("Nombre"),
                        resultSet.getString("Tipo"),
                        resultSet.getString("Fecha_caducidad"),
                        resultSet.getString("Estado"),
                        resultSet.getInt("Cantidad"),
                        resultSet.getString("CIF_entidad"));

                listaProductos.add(producto);
            }
            connection.close();
            statement.close();
            resultSet.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return listaProductos;
    }
    public static boolean comprobarExistenciaProducto(String codigoBarras) {
        String selectQuery = "SELECT COUNT(*) AS count FROM producto WHERE Codigo_barras = ?";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(selectQuery)) {

            statement.setString(1, codigoBarras);
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

    public static boolean escribirProductosBBDD(int idProductoDisp, String codigoBarras, String nombreProducto, String tipoProducto, String fechaCaducidad, String estado, int cantidad, String nifEntidad) {
        String insertQueryproductos = "INSERT INTO producto (Codigo_barras, Nombre, Tipo) VALUES (?, ?, ?)";
        String insertQueryproductosdisp = "INSERT INTO productodisp (Codigo_barras, Fecha_caducidad, Estado, Cantidad, CIF_entidad) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            // Comprobar si el producto ya existe
            if (comprobarExistenciaProducto(codigoBarras)) {
                System.out.println("El producto ya existe en la base de datos.");
              return false; // Producto ya existe, no se puede insertar de nuevo
            }

            // Insertar en la tabla Productos
            try (PreparedStatement statement = connection.prepareStatement(insertQueryproductos)) {
                statement.setString(1, codigoBarras);
                statement.setString(2, nombreProducto);
                statement.setString(3, tipoProducto);

                statement.executeUpdate();
            }

            // Insertar en la tabla ProductosDisp
            try (PreparedStatement statement = connection.prepareStatement(insertQueryproductosdisp)) {
                statement.setString(1, codigoBarras);
                statement.setString(2, fechaCaducidad);
                statement.setString(3, estado);
                statement.setInt(4, cantidad);
                statement.setString(5, nifEntidad);

                statement.executeUpdate();
            }
            enviarCorreoSuscripcion(nombreProducto, nifEntidad);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public static boolean modificarProductoBBDD(int idProductoDisp, String codigoBarras, String nombreProducto, String tipoProducto, String fechaCaducidad, String estado, int cantidad, String nifEntidad) {
        String queryCodigoBarrasExistente = "SELECT COUNT(*) FROM producto WHERE codigo_barras = ? AND NOT EXISTS (SELECT codigo_barras FROM productodisp WHERE id_productoDisp = ? AND codigo_barras = ?)";
        String queryProductoDisp = "SELECT codigo_barras FROM productodisp WHERE id_productoDisp = ?";
        String updateProducto = "UPDATE producto SET codigo_barras = ?, nombre = ?, tipo = ? WHERE codigo_barras = ?";
        String updateProductoDisp = "UPDATE productodisp SET codigo_barras = ?, CIF_entidad = ?, cantidad = ?, estado = ?, Fecha_caducidad = ? WHERE id_productoDisp = ?";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement psQueryCodigoBarrasExistente = connection.prepareStatement(queryCodigoBarrasExistente);
             PreparedStatement psQueryProductoDisp = connection.prepareStatement(queryProductoDisp);
             PreparedStatement psUpdateProducto = connection.prepareStatement(updateProducto);
             PreparedStatement psUpdateProductoDisp = connection.prepareStatement(updateProductoDisp)) {

            // Comprobar si el nuevo código de barras ya existe
            psQueryCodigoBarrasExistente.setString(1, codigoBarras);
            psQueryCodigoBarrasExistente.setInt(2, idProductoDisp);
            psQueryCodigoBarrasExistente.setString(3, codigoBarras);
            ResultSet rsExistente = psQueryCodigoBarrasExistente.executeQuery();
            if (rsExistente.next() && rsExistente.getInt(1) > 0) {
                System.out.println("El código de barras ya existe para otro producto.");
                return false; // El nuevo código de barras ya existe
            }

            // Obtener el código de barras actual para el producto disponible
            psQueryProductoDisp.setInt(1, idProductoDisp);
            ResultSet rs = psQueryProductoDisp.executeQuery();
            if (!rs.next()) {
                return false; // El producto disponible no existe
            }
            String currentCodigoBarras = rs.getString(1);

            // Actualizar la tabla de productos si el código de barras ha cambiado
            psUpdateProducto.setString(1, codigoBarras);
            psUpdateProducto.setString(2, nombreProducto);
            psUpdateProducto.setString(3, tipoProducto);
            psUpdateProducto.setString(4, currentCodigoBarras);
            psUpdateProducto.executeUpdate();

            // Actualizar la tabla productosdisp
            psUpdateProductoDisp.setString(1, codigoBarras);
            psUpdateProductoDisp.setString(2, nifEntidad);
            psUpdateProductoDisp.setInt(3, cantidad);
            psUpdateProductoDisp.setString(4, estado);
            psUpdateProductoDisp.setString(5, fechaCaducidad);
            psUpdateProductoDisp.setInt(6, idProductoDisp);
            psUpdateProductoDisp.executeUpdate();

            return true; // Producto actualizado con éxito

        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Error al actualizar el producto
        }
    }
    public static boolean eliminarProductoBBDD(int idProductoDisp) {
        String queryObtenerCodigoBarras = "SELECT codigo_barras FROM productodisp WHERE id_productoDisp = ?";
        String deleteProductoDisp = "DELETE FROM productodisp WHERE id_productoDisp = ?";
        String deleteProducto = "DELETE FROM producto WHERE codigo_barras = ?";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement psObtenerCodigoBarras = connection.prepareStatement(queryObtenerCodigoBarras);
             PreparedStatement psDeleteProductoDisp = connection.prepareStatement(deleteProductoDisp);
             PreparedStatement psDeleteProducto = connection.prepareStatement(deleteProducto)) {

            // Obtener el código de barras del producto a eliminar
            psObtenerCodigoBarras.setInt(1, idProductoDisp);
            ResultSet rs = psObtenerCodigoBarras.executeQuery();
            if (!rs.next()) {
                return false; // El producto no existe
            }
            String codigoBarras = rs.getString(1);

            // Eliminar el producto de la tabla productosdisp
            psDeleteProductoDisp.setInt(1, idProductoDisp);
            psDeleteProductoDisp.executeUpdate();

            // Eliminar el producto de la tabla productos
            psDeleteProducto.setString(1, codigoBarras);
            psDeleteProducto.executeUpdate();

            return true; // Producto eliminado con éxito

        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Error al eliminar el producto
        }
    }
    public static boolean enviarCorreoSuscripcion(String nombreProducto, String nifEntidad) {
        // Configuración del servidor de correo saliente (SMTP)
        final String correoOrigen = "pruebais129@outlook.es";
        final String contraseñaOrigen = "JoseCelia129";
        String host = "smtp-mail.outlook.com";
        int port = 587;

        // Configuración de las propiedades
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", port);

        // Obtener el nombre de la entidad
        String nombreEntidad = "";
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            String queryEntidad = "SELECT nombre FROM Entidad WHERE CIF_entidad = ?";
            try (PreparedStatement stmtEntidad = connection.prepareStatement(queryEntidad)) {
                stmtEntidad.setString(1, nifEntidad);
                try (ResultSet rsEntidad = stmtEntidad.executeQuery()) {
                    if (rsEntidad.next()) {
                        nombreEntidad = rsEntidad.getString("nombre");
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener el nombre de la entidad", e);
        }

        // Obtener los correos electrónicos de los suscriptores
        String destinatarios = "";
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            String querySuscriptores = "SELECT ue.Correo_electronico " +
                    "FROM Suscripcion s " +
                    "JOIN UsuarioEntidad ue ON s.DNI = ue.DNI " +
                    "WHERE s.CIF_entidad = ?";
            try (PreparedStatement stmtSuscriptores = connection.prepareStatement(querySuscriptores)) {
                stmtSuscriptores.setString(1, nifEntidad);
                try (ResultSet rsSuscriptores = stmtSuscriptores.executeQuery()) {
                    while (rsSuscriptores.next()) {
                        destinatarios += rsSuscriptores.getString("Correo_electronico") + ",";
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener los correos electrónicos de los suscriptores", e);
        }

        // Creación del mensaje de correo
        String mensaje = "<!-- Contenedor principal -->\n" +
                "<div style=\"max-width: 600px; margin: auto; padding: 20px;\">\n" +
                "    <h1 style=\"font-weight: bold; text-align: left;\">DesperdiCero</h1>\n" +
                "    <p>Estimado cliente,</p>\n" +
                "    <!-- Breve descripción -->\n" +
                "    <p>En nombre de todo el equipo de DesperdiCero, nos complace informarte sobre la emocionante novedad que tenemos para ti. <strong>¡El supermercado "+ nombreEntidad + " ha agregado un nuevo producto denominado " + nombreProducto + " a nuestra plataforma!</strong></p>\n" +
                "    <p>Como suscriptor del supermercado "+ nombreEntidad + ", queremos asegurarnos de que siempre estés al tanto de las últimas incorporaciones a nuestra gama de productos. Estamos constantemente buscando formas de satisfacer tus necesidades de manera integral.</p>\n" +
                "    <p style=\"text-align: left;\">En el caso de que desees no recibir más información sobre este supermercado, debes acceder al apartado de <em>suscripciones</em> dentro de nuestra plataforma y anular la suscripción.</p>\n" +
                "    <p style=\"text-align: left;\">&nbsp;</p>\n" +
                "    <p style=\"text-align: left;\">Saludos cordiales,</p>\n" +
                "    <p style=\"text-align: left;\">DesperdiCero.</p>\n" +
                "    <p style=\"text-align: left;\">Departamento de comunicación.</p>\n" +
                "</div>";

        // Envío del mensaje a los destinatarios
        try {
            Session session = Session.getInstance(props, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(correoOrigen, contraseñaOrigen);
                }
            });

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(correoOrigen));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatarios));
            message.setSubject("¡Descubre los nuevos productos!");
            message.setContent(mensaje, "text/html; charset=utf-8");

            Transport.send(message);
            System.out.println("El mensaje fue enviado correctamente.");
        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

}
