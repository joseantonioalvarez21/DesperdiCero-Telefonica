package es.ufv.ProyectosII.DesperdiCero.back.controllers;

import es.ufv.ProyectosII.DesperdiCero.back.dataAccess.CambiarContraseñaBBDD;
import es.ufv.ProyectosII.DesperdiCero.back.models.Entidad;
import es.ufv.ProyectosII.DesperdiCero.back.models.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class CambiarContraseñaController {

    private CambiarContraseñaBBDD cambiarContrasena;


    @Autowired
    public CambiarContraseñaController(CambiarContraseñaBBDD cambiarContrasena) {
        this.cambiarContrasena = cambiarContrasena;
    }
    @PostMapping("/obtenerContrasena")
    public ResponseEntity<String> validarContrasena(@RequestBody Usuario usuario) {
        // Obtener credenciales del usuario
        String correoElectronico = usuario.getCorreo_Electronico();
        String contrasena = usuario.getContraseña();

        // Validar la contraseña en la base de datos y obtenerla
        String contrasenaEnBaseDeDatos = CambiarContraseñaBBDD.obtenerContrasena(correoElectronico, contrasena);

        if (contrasenaEnBaseDeDatos != null) {
            return ResponseEntity.ok("Inicio de sesión exitoso para el usuario: " + correoElectronico);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Contraseña inválida");
        }
    }

    @PostMapping("/obtenerContrasenaEntidad")
    public ResponseEntity<String> validarContrasenaEntidad(@RequestBody Entidad entidad) {
        // Obtener credenciales del usuario
        String correoElectronico = entidad.getCorreo();
        String contrasena = entidad.getContraseña();

        // Validar la contraseña en la base de datos y obtenerla
        String contrasenaEnBaseDeDatos = CambiarContraseñaBBDD.obtenerContrasenaEntidad(correoElectronico, contrasena);

        if (contrasenaEnBaseDeDatos != null) {
            return ResponseEntity.ok("Inicio de sesión exitoso para la entidad: " + correoElectronico);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Contraseña inválida");
        }
    }



    @PostMapping("/datosUsuario")
    public ResponseEntity<String> cambiarContrasena(@RequestBody Usuario usuario) {
        String correoElectronico = usuario.getCorreo_Electronico();
        String nuevaContrasena = usuario.getContraseña();

        String resultadoCambio = cambiarContrasena.cambiarContrasena(correoElectronico, nuevaContrasena);

        if (resultadoCambio.startsWith("Contraseña actualizada correctamente")) {
            return ResponseEntity.ok(resultadoCambio);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al cambiar la contraseña: " + resultadoCambio);
        }
    }

    @PostMapping("/datosUsuario/entidad")
    public ResponseEntity<String> cambiarContrasenaEntidad(@RequestBody Entidad entidad) {
        String correoElectronico = entidad.getCorreo();
        String nuevaContrasena = entidad.getContraseña();

        String resultadoCambio = cambiarContrasena.cambiarContrasenaEntidad(correoElectronico, nuevaContrasena);

        if (resultadoCambio.startsWith("Contraseña actualizada correctamente")) {
            return ResponseEntity.ok(resultadoCambio);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al cambiar la contraseña: " + resultadoCambio);
        }
    }

}
