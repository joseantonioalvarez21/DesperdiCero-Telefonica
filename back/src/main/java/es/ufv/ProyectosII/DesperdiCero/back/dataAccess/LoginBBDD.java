package es.ufv.ProyectosII.DesperdiCero.back.dataAccess;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
@Component("loginDataAccess")
@Service
public class LoginBBDD {

    private static final String URL = "jdbc:mysql://localhost:3306/desperdicero?serverTimezone=UTC";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "root1234";

    public static boolean actualizarContrasena(String correoElectronico) {
        // Generar una contraseña aleatoria
        String nuevaContrasena = generarContrasenaAleatoria();

        // Actualizar la contraseña en la base de datos
        String updateQuery = "UPDATE usuarioentidad SET Contraseña = ? WHERE Correo_electronico = ?";

        try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(updateQuery)) {
            pstmt.setString(1, nuevaContrasena);
            pstmt.setString(2, correoElectronico);

            int filasActualizadas = pstmt.executeUpdate();
            System.out.println("filas: " + filasActualizadas);

            if (filasActualizadas > 0) {
                // Enviar correo electrónico solo si la actualización fue exitosa
                enviarCorreoElectronico(correoElectronico, nuevaContrasena);
            }

            return true;
        } catch (SQLException e) {
            System.err.println("Error al actualizar la contraseña en la base de datos");
            e.printStackTrace();
            return false; // Error en la actualización de la contraseña
        }
    }

    private static String generarContrasenaAleatoria() {
        // Caracteres permitidos para la contraseña
        String caracteres = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()-_=+";

        // Longitud de la contraseña
        int longitud = 8;

        // Generar una contraseña aleatoria
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < longitud; i++) {
            int index = random.nextInt(caracteres.length());
            sb.append(caracteres.charAt(index));
        }

        return sb.toString();
    }

    private static void enviarCorreoElectronico(String destinatario, String nuevaContrasena) {
        // Configuración del servidor de correo
        final String correoOrigen = "desperdicer0@outlook.es";
        final String contraseñaOrigen = "jose123ivonne";
        String host = "smtp-mail.outlook.com";
        int port = 587;

        // Configuración de las propiedades
        Properties propiedades = new Properties();
        propiedades.put("mail.smtp.auth", "true");
        propiedades.put("mail.smtp.starttls.enable", "true");
        propiedades.put("mail.smtp.host", host);
        propiedades.put("mail.smtp.port", String.valueOf(port));

        String contenidoMensaje = "<!-- Contenedor principal -->\n" +
                "<div style=\"max-width: 600px; margin: auto; padding: 20px; color: #333;\">\n" +
                "    <h1 style=\"font-weight: bold; text-align: left; color: #2ecc71;\">DesperdiCero</h1>\n" +
                "    <p>Estimado cliente,</p>\n" +
                "    <p style=\"font-size: 16px;\">Te informamos que la contraseña de tu cuenta en DesperdiCero ha sido actualizada con éxito.</p>\n" +
                "    <p style=\"font-size: 20px; font-weight: bold; color: #2ecc71;\">Tu nueva contraseña es: <span style=\"color: #2ecc71;\">" + nuevaContrasena + "</span></p>\n" +
                "    <p>En nombre de todo el equipo de DesperdiCero, queremos asegurarnos de que tu cuenta esté segura y protegida.</p>\n" +
                "    <p style=\"font-size: 12px; color: #888;\">Si no has realizado esta actualización, por favor, ponte en contacto con nuestro equipo de soporte de inmediato.</p>\n" +
                "    <p>&nbsp;</p>\n" +
                "    <p>Saludos cordiales,</p>\n" +
                "    <p>DesperdiCero.</p>\n" +
                "    <p style=\"font-size: 12px; color: #888;\">Departamento de comunicación.</p>\n" +
                "</div>";

        // Crear la sesión de correo
        Session session = Session.getInstance(propiedades, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(correoOrigen, contraseñaOrigen);
            }
        });

        // Crear el mensaje de correo
        Message mensaje = new MimeMessage(session);
        try {
            mensaje.setFrom(new InternetAddress(correoOrigen));
            mensaje.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));
            mensaje.setSubject("Recuperación de Contraseña - Desperdicer0");
            mensaje.setContent(contenidoMensaje, "text/html; charset=utf-8");

            // Enviar el mensaje
            Transport.send(mensaje);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public static boolean validarCorreoExistente(String correoElectronico) {
        String query = "SELECT * FROM usuarioentidad WHERE Correo_electronico = ?";

        try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, correoElectronico);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    // Si el correo existe, actualizar la contraseña y enviar el correo
                    return true;
                } else {
                    return false; // No se encontró el correo en la base de datos
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Manejo del error: devuelve false en caso de excepción
        }
    }


    // Método para validar las credenciales del usuario
    public boolean validarCredenciales(String correoElectronico, String contrasena) {
        // Primera consulta en la tabla usuarioentidad
        String queryUsuarioEntidad = "SELECT * FROM usuarioentidad WHERE Correo_electronico = ? AND Contraseña = ?";
        // Segunda consulta en la tabla Entidad
        String queryEntidad = "SELECT * FROM Entidad WHERE Correo_electronico = ? AND Contraseña = ?";

        try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD)) {
            // Intenta con usuarioentidad primero
            try (PreparedStatement pstmt = conn.prepareStatement(queryUsuarioEntidad)) {
                pstmt.setString(1, correoElectronico);
                pstmt.setString(2, contrasena);

                System.out.println("Ejecutando consulta SQL en usuarioentidad: " + queryUsuarioEntidad);

                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        System.out.println("Inicio de sesión exitoso en usuarioentidad");
                        return true;
                    }
                }
            }

            // Si no se encuentra en usuarioentidad, intenta con Entidad
            try (PreparedStatement pstmt = conn.prepareStatement(queryEntidad)) {
                pstmt.setString(1, correoElectronico);
                pstmt.setString(2, contrasena);

                System.out.println("Ejecutando consulta SQL en Entidad: " + queryEntidad);

                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        System.out.println("Inicio de sesión exitoso en Entidad");
                        return true;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        System.out.println("No se encontraron credenciales válidas");
        return false; // Retorna false si no se encuentra el usuario en ninguna de las dos tablas
    }

    /**
     * public boolean validarCredenciales(String correoElectronico, String contrasena) {
     *         // Primera consulta en la tabla usuarioentidad
     *         String queryUsuarioEntidad = "SELECT * FROM usuarioentidad WHERE Correo_electronico = ? AND Contraseña = ?";
     *         // Segunda consulta en la tabla Entidad
     *         String queryEntidad = "SELECT * FROM Entidad WHERE Correo_electronico = ? AND Contraseña = ?";
     *
     *         try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD)) {
     *             // Intenta con usuarioentidad primero
     *             try (PreparedStatement pstmt = conn.prepareStatement(queryUsuarioEntidad)) {
     *                 pstmt.setString(1, correoElectronico);
     *                 pstmt.setString(2, contrasena);
     *
     *                 System.out.println("Ejecutando consulta SQL en usuarioentidad: " + queryUsuarioEntidad);
     *
     *                 try (ResultSet rs = pstmt.executeQuery()) {
     *                     if (rs.next()) {
     *                         System.out.println("Inicio de sesión exitoso en usuarioentidad");
     *                         return true;
     *                     }
     *                 }
     *             }
     *
     *             // Si no se encuentra en usuarioentidad, intenta con Entidad
     *             try (PreparedStatement pstmt = conn.prepareStatement(queryEntidad)) {
     *                 pstmt.setString(1, correoElectronico);
     *                 pstmt.setString(2, contrasena);
     *
     *                 System.out.println("Ejecutando consulta SQL en Entidad: " + queryEntidad);
     *
     *                 try (ResultSet rs = pstmt.executeQuery()) {
     *                     if (rs.next()) {
     *                         System.out.println("Inicio de sesión exitoso en Entidad");
     *                         return true;
     *                     }
     *                 }
     *             }
     *         } catch (SQLException e) {
     *             e.printStackTrace();
     *             return false;
     *         }
     *         System.out.println("No se encontraron credenciales válidas");
     *         return false; // Retorna false si no se encuentra el usuario en ninguna de las dos tablas
     *     }**/
}

