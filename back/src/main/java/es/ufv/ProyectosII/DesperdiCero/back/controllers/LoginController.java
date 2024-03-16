package es.ufv.ProyectosII.DesperdiCero.back.controllers;
import es.ufv.ProyectosII.DesperdiCero.back.dataAccess.LoginBBDD;
import es.ufv.ProyectosII.DesperdiCero.back.models.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

    private final LoginBBDD loginBBDD;

    @Autowired
    public LoginController(LoginBBDD loginBBDD) {
        this.loginBBDD = loginBBDD;
    }

    @PostMapping("/recuperar-contrasena")
    public ResponseEntity<String> recuperarContrasena(@RequestBody String correoElectronico) {
        boolean correoExiste = LoginBBDD.validarCorreoExistente(correoElectronico);
        if (correoExiste) {
            boolean actualizacionExitosa = LoginBBDD.actualizarContrasena(correoElectronico);
            if (actualizacionExitosa) {
                return ResponseEntity.ok("Correo electrónico válido. Se ha enviado un correo para restablecer la contraseña.");
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al actualizar la contraseña en la base de datos.");
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("El correo electrónico proporcionado no existe en la base de datos.");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Usuario usuario) {
        // Obtener credenciales del usuario
        String correoElectronico = usuario.getCorreo_Electronico();
        String contrasena = usuario.getContraseña();

        // Validar credenciales en la base de datos
        boolean credencialesValidas = loginBBDD.validarCredenciales(correoElectronico, contrasena);

        if (credencialesValidas) {
            return ResponseEntity.ok("Inicio de sesión exitoso para el usuario: " + correoElectronico);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales inválidas");
        }
    }
}
