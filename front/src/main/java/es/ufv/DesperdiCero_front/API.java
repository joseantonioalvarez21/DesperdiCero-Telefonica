package es.ufv.DesperdiCero_front;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import es.ufv.DesperdiCero_front.models.*;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.util.ArrayList;

public class API {
    private static final String URL = "http://localhost:8081/";

    public boolean validarCorreoExistente(String correo) {
        String fullUrl = "http://localhost:8081/recuperar-contrasena";
        RestTemplate restTemplate = new RestTemplate();

        // Configurar la solicitud POST con los datos del correo electrónico
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestEntity = new HttpEntity<>(correo, headers);

        // Realizar la solicitud POST
        ResponseEntity<String> response = restTemplate.exchange(fullUrl, HttpMethod.POST, requestEntity, String.class);

        // Verificar si la respuesta contiene el texto esperado
        String responseBody = response.getBody();
        return responseBody != null && responseBody.contains("Correo electrónico válido");
    }

    // Método para llamar al nuevo endpoint de recuperación de contraseña


    public void PutEntidad(Entidad entidad) throws Exception {
        String fullUrl = "http://localhost:8081/entidadActualizada";
        RestTemplate restTemplate = new RestTemplate();

        // Configuración de la solicitud PUT
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Entidad> requestEntity = new HttpEntity<>(entidad, headers);

        // Realizar la solicitud PUT
        ResponseEntity<Boolean> response = restTemplate.exchange(fullUrl, HttpMethod.PUT, requestEntity, Boolean.class);

        new ResponseEntity<>(response.getBody(), response.getStatusCode());
    }
    // Método para actualizar la lista de productos
    public void PutListaProductos(Producto producto) throws Exception {
        String fullUrl = URL + "productosActualizados";
        RestTemplate restTemplate = new RestTemplate();

        // Configuración de la solicitud PUT
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Producto> requestEntity = new HttpEntity<>(producto, headers);

        // Realizar la solicitud PUT
        ResponseEntity<Boolean> response = restTemplate.exchange(fullUrl, HttpMethod.PUT, requestEntity, Boolean.class);

        new ResponseEntity<>(response.getBody(), response.getStatusCode());
    }
    public ArrayList<Producto> getDatos() throws Exception {//Clase para leer datos de la api json 1
        String fullUrl = URL + "productos";
        java.net.URL url = new URL(fullUrl);

        // Crea un objeto HttpClient
        HttpClient httpClient = HttpClient.newBuilder().build();
        java.net.http.HttpRequest request = java.net.http.HttpRequest.newBuilder().uri(new URI(fullUrl)).GET().build();
        // Obtiene la respuesta de la API y la convierte a ArrayList<Datos>
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        // Manejo de excepciones durante la deserialización
        if (response.statusCode() == 200) {//Indica solicitud exitosa
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(response.body(), new TypeReference<ArrayList<Producto>>() {});
        } else {
            // Manejar el caso en el que la respuesta no sea exitosa (código de estado diferente de 200)
            throw new RuntimeException("Error al obtener datos. Código de estado: " + response.statusCode());
        }
    }
    //Método para modificar productos
    public void PutProductoModificado(Producto producto) throws Exception {
        String fullUrl = URL + "productosModificados";
        RestTemplate restTemplate = new RestTemplate();

        // Configuración de la solicitud PUT
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Producto> requestEntity = new HttpEntity<>(producto, headers);

        // Realizar la solicitud PUT
        ResponseEntity<Boolean> response = restTemplate.exchange(fullUrl, HttpMethod.PUT, requestEntity, Boolean.class);

        new ResponseEntity<>(response.getBody(), response.getStatusCode());
    }
    //Eliminar un producto
    public void deleteProducto(int idProducto) {
        String fullUrl = URL + "productos/" + idProducto;
        RestTemplate restTemplate = new RestTemplate();

        restTemplate.delete(fullUrl);
    }
    // Método para obtener datos de usuarios
    public ArrayList<Usuario> getUsuarios() throws Exception {
        String fullUrl = URL + "usuarios";
        java.net.URL url = new URL(fullUrl);

        // Crea un objeto HttpClient
        HttpClient httpClient = HttpClient.newBuilder().build();

        // Crea un objeto HttpRequest con la URL
        java.net.http.HttpRequest request = java.net.http.HttpRequest.newBuilder().uri(new URI(fullUrl)).GET().build();

        // Obtiene la respuesta de la API y la convierte a ArrayList<Datos>
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        // Manejo de excepciones durante la deserialización
        if (response.statusCode() == 200) {//Indica solicitud exitosa
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(response.body(), new TypeReference<ArrayList<Usuario>>() {
            });
        } else {
            // Manejar el caso en el que la respuesta no sea exitosa (código de estado diferente de 200)
            throw new RuntimeException("Error al obtener datos. Código de estado: " + response.statusCode());

        }

    }
    // Método para validar las credenciales del usuario
    public boolean validarCredenciales(Usuario usuario) throws Exception {
        String fullUrl = URL + "login";
        RestTemplate restTemplate = new RestTemplate();

        // Configurar la solicitud POST con los datos del usuario
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Usuario> requestEntity = new HttpEntity<>(usuario, headers);

        // Realizar la solicitud POST
        ResponseEntity<String> response = restTemplate.exchange(fullUrl, HttpMethod.POST, requestEntity, String.class);

        // Analizar la respuesta
        if (response.getStatusCode() == HttpStatus.OK) {
            // Si la respuesta es 200 OK, las credenciales son válidas
            return true;
        } else {
            // Si la respuesta no es 200 OK, las credenciales son inválidas
            return false;
        }
    }
    //Método para añadir usuarios
    public boolean anadirUsuarioRemotamente(Usuario usuario) {
        String fullUrl = URL + "/usuariosAñadidos"; // Asegúrate de que la URL sea correcta
        RestTemplate restTemplate = new RestTemplate();

        // Configuración de la solicitud PUT
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Usuario> requestEntity = new HttpEntity<>(usuario, headers);

        // Realizar la solicitud PUT
        ResponseEntity<Boolean> response = restTemplate.exchange(fullUrl, HttpMethod.PUT, requestEntity, Boolean.class);

        // Devolver el cuerpo de la respuesta
        return response.getBody();
    }
    //Método para eliminar un usuario
    //Eliminar un producto
    public void deleteUsuario(String idProducto) {
        String fullUrl = URL + "usuario/" + idProducto;
        RestTemplate restTemplate = new RestTemplate();

        restTemplate.delete(fullUrl);
    }
    //Método para validar que tipo de entidad es
    public String obtenerTipoEntidadPorCorreo(String correo) throws Exception {
        // Primero, obtén el NIF del usuario con el correo dado
        String nif = obtenerNifPorCorreo(correo);
        // Luego, usa ese NIF para obtener el tipo de entidad
        return obtenerTipoEntidadPorNif(nif);
    }

    public String obtenerNifPorCorreo(String correo) throws Exception {
        String fullUrl = URL + "nifPorCorreo/" + correo;
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(fullUrl, String.class);
        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            return response.getBody();
        } else {
            throw new HttpClientErrorException(response.getStatusCode(), "Error al obtener el NIF por correo");
        }
    }
    public String obtenerDniPorCorreo(String correo) throws Exception {
        String fullUrl = URL + "dniPorCorreo/" + correo;
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(fullUrl, String.class);
        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            return response.getBody();
        } else {
            throw new HttpClientErrorException(response.getStatusCode(), "Error al obtener el DNI por correo");
        }
    }
    public String obtenerNombrePorCorreo(String correo) throws Exception {
        String fullUrl = URL + "nombrePorCorreo/" + correo;
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(fullUrl, String.class);
        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            return response.getBody();
        } else {
            throw new HttpClientErrorException(response.getStatusCode(), "Error al obtener el nombre por correo");
        }
    }

    public String obtenerTipoEntidadPorNif(String nif) throws Exception {
        String fullUrl = URL + "tipoEntidad/" + nif;
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(fullUrl, String.class);
        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            return response.getBody();
        } else {
            throw new HttpClientErrorException(response.getStatusCode(), "Error al obtener el tipo de entidad por NIF");
        }
    }
    public String obtenerNombreEntidadPorNif(String nif) throws Exception {
        String fullUrl = URL + "nombreEntidad/" + nif;
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(fullUrl, String.class);
        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            return response.getBody();
        } else {
            throw new HttpClientErrorException(response.getStatusCode(), "Error al obtener el nombre de la entidad por NIF");
        }
    }
    public ArrayList<Entidad> getEntidades(String tipo) throws Exception {
        String fullUrl = URL + "entidades/" + tipo;
        java.net.URL url = new URL(fullUrl);

        HttpClient httpClient = HttpClient.newBuilder().build();
        java.net.http.HttpRequest request = java.net.http.HttpRequest.newBuilder().uri(new URI(fullUrl)).GET().build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {//Indica solicitud exitosa
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(response.body(), new TypeReference<ArrayList<Entidad>>() {});
        } else {
            // Manejar el caso en el que la respuesta no sea exitosa (código de estado diferente de 200)
            throw new RuntimeException("Error al obtener las entidades. Código de estado: " + response.statusCode());
        }
    }
    //----------------------------------------- Manejo de suscripciones----------------------------------------------
    public ArrayList<Suscripcion> getSuscripciones(String dni) throws Exception {//Clase para leer datos de la api json 1
        String fullUrl = URL + "suscripciones/" + dni;
        java.net.URL url = new URL(fullUrl);

        HttpClient httpClient = HttpClient.newBuilder().build();
        java.net.http.HttpRequest request = java.net.http.HttpRequest.newBuilder().uri(new URI(fullUrl)).GET().build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {//Indica solicitud exitosa
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(response.body(), new TypeReference<ArrayList<Suscripcion>>() {});
        } else {
            // Manejar el caso en el que la respuesta no sea exitosa (código de estado diferente de 200)
            throw new RuntimeException("Error al obtener las suscripciones. Código de estado: " + response.statusCode());
        }
    }
    public void PutSuscripcion(Suscripcion suscripcion) throws Exception {
        String fullUrl = "http://localhost:8081/nuevaSuscripcion";
        RestTemplate restTemplate = new RestTemplate();

        // Configuración de la solicitud PUT
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Suscripcion> requestEntity = new HttpEntity<>(suscripcion, headers);

        // Realizar la solicitud PUT
        ResponseEntity<Boolean> response = restTemplate.exchange(fullUrl, HttpMethod.PUT, requestEntity, Boolean.class);

        new ResponseEntity<>(response.getBody(), response.getStatusCode());
    }
    public void eliminarSuscripcion(int id_suscripcion) {
        String fullUrl = URL + "suscripcion/" + id_suscripcion;
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.delete(fullUrl);
    }
    //---------------------------------------------------------------------------------------------------------------
    public String obteneEntidadPorCorreo(String correo) throws Exception {
        String fullUrl = URL + "/EntidadPorCorreo/" + correo; // Asegúrate de que la URL es correcta
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(fullUrl, String.class);

        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            // Si el estado es OK y el cuerpo no es nulo, devolvemos el cuerpo de la respuesta
            return response.getBody();
        } else if (response.getStatusCode() == HttpStatus.NOT_FOUND) {
            // Si el estado es NOT_FOUND, lanzamos una excepción específica
            throw new Exception("Correo no encontrado en ninguna tabla.");
        } else {
            // Para cualquier otro caso, lanzamos una excepción con el estado HTTP y la posible respuesta del cuerpo
            throw new Exception("Error al obtener la entidad por correo: " +
                    response.getStatusCode() +
                    " " +
                    (response.getBody() != null ? response.getBody() : ""));
        }
    }
    //-----------------------------Javi-------------------
    /*public ArrayList<Entidad> getEntidades(String tipo) throws Exception {
        String fullUrl = URL + "entidades/" + tipo;
        java.net.URL url = new URL(fullUrl);

        HttpClient httpClient = HttpClient.newBuilder().build();
        java.net.http.HttpRequest request = java.net.http.HttpRequest.newBuilder().uri(new URI(fullUrl)).GET().build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {//Indica solicitud exitosa
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(response.body(), new TypeReference<ArrayList<Entidad>>() {
            });
        } else {
            // Manejar el caso en el que la respuesta no sea exitosa (código de estado diferente de 200)
            throw new RuntimeException("Error al obtener las entidades. Código de estado: " + response.statusCode());
        }
    }*/

    public String cambiarContrasena(Usuario request) { // Cambiar contraseña del usuario de la entidad
        String fullUrl = URL + "/datosUsuario"; // Asegúrate de que la URL sea la correcta para tu aplicación
        RestTemplate restTemplate = new RestTemplate();

        // Configurar la solicitud POST con los datos de cambio de contraseña
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Usuario> requestEntity = new HttpEntity<>(request, headers);

        // Realizar la solicitud POST
        ResponseEntity<String> response = restTemplate.exchange(fullUrl, HttpMethod.POST, requestEntity, String.class);

        // Retornar el resultado de la solicitud
        return response.getBody();
    }


    public boolean validarContrasena(String correoElectronico, String contrasenaActual) {
        try {
            // Lógica para validar la contraseña en la base de datos (Front-end)
            String fullUrl = URL + "/obtenerContrasena";
            RestTemplate restTemplate = new RestTemplate();

            // Configurar la solicitud POST con los datos del usuario
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            // Crear un nuevo objeto Usuario solo con la información necesaria (correo electrónico y contraseña)
            Usuario usuario = new Usuario();
            usuario.setCorreo_Electronico(correoElectronico);
            usuario.setContraseña(contrasenaActual);

            HttpEntity<Usuario> requestEntity = new HttpEntity<>(usuario, headers);

            // Realizar la solicitud POST
            ResponseEntity<String> response = restTemplate.exchange(fullUrl, HttpMethod.POST, requestEntity, String.class);

            // Analizar la respuesta
            return response.getStatusCode() == HttpStatus.OK;
        } catch (HttpClientErrorException e) {
            // Manejar el error, si es necesario
            if (e.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                // Contraseña inválida
                return false;
            } else {
                // Otro manejo de errores según sea necesario
                throw e;
            }
        } catch (Exception e) {
            e.printStackTrace();  // o cualquier otro manejo de errores que necesites
            return false;
        }
    }

    public boolean validarContrasenaEntidad(String correoElectronico, String contrasenaActual) {
        try {
            // Lógica para validar la contraseña en la base de datos (Front-end)
            String fullUrl = URL + "/obtenerContrasenaEntidad";
            RestTemplate restTemplate = new RestTemplate();

            // Configurar la solicitud POST con los datos del usuario
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            // Crear un nuevo objeto Usuario solo con la información necesaria (correo electrónico y contraseña)
            Entidad entidad = new Entidad();
            entidad.setCorreo(correoElectronico);
            entidad.setContraseña(contrasenaActual);

            HttpEntity<Entidad> requestEntity = new HttpEntity<>(entidad, headers);

            // Realizar la solicitud POST
            ResponseEntity<String> response = restTemplate.exchange(fullUrl, HttpMethod.POST, requestEntity, String.class);

            // Analizar la respuesta
            return response.getStatusCode() == HttpStatus.OK;
        } catch (HttpClientErrorException e) {
            // Manejar el error, si es necesario
            if (e.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                // Contraseña inválida
                return false;
            } else {
                // Otro manejo de errores según sea necesario
                throw e;
            }
        } catch (Exception e) {
            e.printStackTrace();  // o cualquier otro manejo de errores que necesites
            return false;
        }
    }








    public String cambiarContrasenaEntidad(Entidad request) { // Cambiar contraseña del correo de la entidad
        String fullUrl = URL + "/datosUsuario/entidad"; // Asegúrate de que la URL sea la correcta para tu aplicación
        RestTemplate restTemplate = new RestTemplate();

        // Configurar la solicitud POST con los datos de cambio de contraseña
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Entidad> requestEntity = new HttpEntity<>(request, headers);

        // Realizar la solicitud POST
        ResponseEntity<String> response = restTemplate.exchange(fullUrl, HttpMethod.POST, requestEntity, String.class);

        // Retornar el resultado de la solicitud
        return response.getBody();
    }




    public Usuario getUsuarioPorCorreoAPI(String correo) throws Exception {
        String fullUrl = URL + "datosUsuario/" + correo;
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(fullUrl, String.class);
        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            // Convertir el JSON recibido en un objeto Usuario
            ObjectMapper objectMapper = new ObjectMapper();
            Usuario usuario = objectMapper.readValue(response.getBody(), Usuario.class);
            return usuario;
        } else {
            throw new HttpClientErrorException(response.getStatusCode(), "Error al obtener el nombre de la entidad por NIF");
        }
    }

    /*public String obtenerNombreUsuarioPorCorreo(String correo) throws Exception {
        String fullUrl = URL + "datosUsuario"; // Ajusta la URL según la ruta correcta en tu backend
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(fullUrl, String.class);
        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            return response.getBody();
        } else {
            throw new HttpClientErrorException(response.getStatusCode(), "Error al obtener el nombre del usuario por correo electrónico");
        }
    }*/
    public String obteneEntidadPorCorreoView(String correo) throws Exception {
        String fullUrl = URL + "/datosUsuario/" + correo; // Asegúrate de que la URL es correcta
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(fullUrl, String.class);

        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            // Si el estado es OK y el cuerpo no es nulo, devolvemos el cuerpo de la respuesta
            return response.getBody();
        } else if (response.getStatusCode() == HttpStatus.NOT_FOUND) {
            // Si el estado es NOT_FOUND, lanzamos una excepción específica
            throw new Exception("Correo no encontrado en ninguna tabla.");
        } else {
            // Para cualquier otro caso, lanzamos una excepción con el estado HTTP y la posible respuesta del cuerpo
            throw new Exception("Error al obtener la entidad por correo: " +
                    response.getStatusCode() +
                    " " +
                    (response.getBody() != null ? response.getBody() : ""));
        }
    }

    public String obtenerDireccionPorCorreo(String correo) throws Exception {
        String fullUrl = URL + "datosUsuario/" + correo + "/direccion";
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(fullUrl, String.class);

        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            // Si el estado es OK y el cuerpo no es nulo, devolvemos el cuerpo de la respuesta
            return response.getBody();
        } else if (response.getStatusCode() == HttpStatus.NOT_FOUND) {
            // Si el estado es NOT_FOUND, lanzamos una excepción específica
            throw new Exception("Correo no encontrado en ninguna tabla.");
        } else {
            // Para cualquier otro caso, lanzamos una excepción con el estado HTTP y la posible respuesta del cuerpo
            throw new Exception("Error al obtener la entidad por correo: " +
                    response.getStatusCode() +
                    " " +
                    (response.getBody() != null ? response.getBody() : ""));
        }

    }
    public String obtenerNombrePorCorreoView(String correo) throws Exception {
        String fullUrl = URL + "datosUsuario/" + correo + "/nombre";
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(fullUrl, String.class);

        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            // Si el estado es OK y el cuerpo no es nulo, devolvemos el cuerpo de la respuesta
            return response.getBody();
        } else if (response.getStatusCode() == HttpStatus.NOT_FOUND) {
            // Si el estado es NOT_FOUND, lanzamos una excepción específica
            throw new Exception("Correo no encontrado en ninguna tabla.");
        } else {
            // Para cualquier otro caso, lanzamos una excepción con el estado HTTP y la posible respuesta del cuerpo
            throw new Exception("Error al obtener la entidad por correo: " +
                    response.getStatusCode() +
                    " " +
                    (response.getBody() != null ? response.getBody() : ""));
        }

    }

    public String obtenerCifEntidadPorCorreo(String correo) throws Exception {
        String fullUrl = URL + "datosUsuario/" + correo + "/CIF_entidad";
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(fullUrl, String.class);

        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            // Si el estado es OK y el cuerpo no es nulo, devolvemos el cuerpo de la respuesta
            return response.getBody();
        } else if (response.getStatusCode() == HttpStatus.NOT_FOUND) {
            // Si el estado es NOT_FOUND, lanzamos una excepción específica
            throw new Exception("Correo no encontrado en ninguna tabla.");
        } else {
            // Para cualquier otro caso, lanzamos una excepción con el estado HTTP y la posible respuesta del cuerpo
            throw new Exception("Error al obtener la entidad por correo: " +
                    response.getStatusCode() +
                    " " +
                    (response.getBody() != null ? response.getBody() : ""));
        }

    }
    public String obtenerNombreTrabajadorPorCorreo(String correo) throws Exception {
        String fullUrl = URL + "datosUsuario/" + correo + "/nombreTrabajador";
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(fullUrl, String.class);

        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            // Si el estado es OK y el cuerpo no es nulo, devolvemos el cuerpo de la respuesta
            return response.getBody();
        } else if (response.getStatusCode() == HttpStatus.NOT_FOUND) {
            // Si el estado es NOT_FOUND, lanzamos una excepción específica
            throw new Exception("Correo no encontrado en ninguna tabla.");
        } else {
            // Para cualquier otro caso, lanzamos una excepción con el estado HTTP y la posible respuesta del cuerpo
            throw new Exception("Error al obtener la entidad por correo: " +
                    response.getStatusCode() +
                    " " +
                    (response.getBody() != null ? response.getBody() : ""));
        }

    }
    public String obtenerApellidosTrabajadorPorCorreo(String correo) throws Exception {
        String fullUrl = URL + "datosUsuario/" + correo + "/apellidosTrabajador";
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(fullUrl, String.class);

        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            // Si el estado es OK y el cuerpo no es nulo, devolvemos el cuerpo de la respuesta
            return response.getBody();
        } else if (response.getStatusCode() == HttpStatus.NOT_FOUND) {
            // Si el estado es NOT_FOUND, lanzamos una excepción específica
            throw new Exception("Correo no encontrado en ninguna tabla.");
        } else {
            // Para cualquier otro caso, lanzamos una excepción con el estado HTTP y la posible respuesta del cuerpo
            throw new Exception("Error al obtener la entidad por correo: " +
                    response.getStatusCode() +
                    " " +
                    (response.getBody() != null ? response.getBody() : ""));
        }

    }
    public String obtenerDNITrabajadorPorCorreo(String correo) throws Exception {
        String fullUrl = URL + "datosUsuario/" + correo + "/DNITrabajador";
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(fullUrl, String.class);

        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            // Si el estado es OK y el cuerpo no es nulo, devolvemos el cuerpo de la respuesta
            return response.getBody();
        } else if (response.getStatusCode() == HttpStatus.NOT_FOUND) {
            // Si el estado es NOT_FOUND, lanzamos una excepción específica
            throw new Exception("Correo no encontrado en ninguna tabla.");
        } else {
            // Para cualquier otro caso, lanzamos una excepción con el estado HTTP y la posible respuesta del cuerpo
            throw new Exception("Error al obtener la entidad por correo: " +
                    response.getStatusCode() +
                    " " +
                    (response.getBody() != null ? response.getBody() : ""));
        }

    }
    public void PutUsuarioModificado(Usuario usuario) throws Exception {
        String fullUrl = URL + "usuarioModificados";
        RestTemplate restTemplate = new RestTemplate();

        // Configuración de la solicitud PUT
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Usuario> requestEntity = new HttpEntity<>(usuario, headers);

        // Realizar la solicitud PUT
        ResponseEntity<Boolean> response = restTemplate.exchange(fullUrl, HttpMethod.PUT, requestEntity, Boolean.class);

        new ResponseEntity<>(response.getBody(), response.getStatusCode());
    }

    //----------------------------------------------Pedidos------------------------------------------------------------
    public ArrayList<Pedido> getPedidos(String NIF) throws Exception {
        String fullUrl = URL + "pedidos/" + NIF;
        java.net.URL url = new URL(fullUrl);

        // Crea un objeto HttpClient
        HttpClient httpClient = HttpClient.newBuilder().build();

        // Crea un objeto HttpRequest con la URL
        java.net.http.HttpRequest request = java.net.http.HttpRequest.newBuilder().uri(new URI(fullUrl)).GET().build();

        // Obtiene la respuesta de la API y la convierte a ArrayList<Pedidos>
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        // Manejo de excepciones durante la deserialización
        if (response.statusCode() == 200) {//Indica solicitud exitosa
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(response.body(), new TypeReference<ArrayList<Pedido>>() {
            });
        } else {
            // Manejar el caso en el que la respuesta no sea exitosa (código de estado diferente de 200)
            throw new RuntimeException("Error al obtener los pedidos. Código de estado: " + response.statusCode());

        }
    }
    //------------------------------------------------------------------------------------------------------------------
}