package es.ufv.ProyectosII.DesperdiCero.back.controllers;

import es.ufv.ProyectosII.DesperdiCero.back.dataAccess.UsuarioBBDD;
import es.ufv.ProyectosII.DesperdiCero.back.models.Usuario;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
public class UsuarioController {
    @GetMapping("/usuarios") // Endpoint para obtener todos los usuarios
    public ArrayList<Usuario> usuarios() {
        ArrayList<Usuario> listaUsuarios = new ArrayList<>();
        UsuarioBBDD usuarioBBDD = new UsuarioBBDD();
        listaUsuarios = usuarioBBDD.leerUsuariosBBDD();
        return listaUsuarios;
    }

    @GetMapping("/usuarios/{dni}") // Endpoint para obtener un usuario por su DNI
    public ResponseEntity<Usuario> getPorDNI(@PathVariable String dni) {
        ArrayList<Usuario> listaUsuarios = new UsuarioBBDD().leerUsuariosBBDD();
        Usuario encontrado = null;
        for (Usuario usuario : listaUsuarios) {
            if (usuario.getDNI() == dni) {
                encontrado = usuario;
                break;
            }
        }
        if (encontrado != null) {
            return new ResponseEntity<>(encontrado, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @PutMapping("/usuariosAñadidos")
    public boolean anadirUsuario(@RequestBody Usuario usuario) {

        return UsuarioBBDD.anadirUsuario(
                usuario.getDNI(),
                usuario.getNombre(),
                usuario.getApellidos(),
                usuario.getCorreo_Electronico(),
                usuario.getContraseña(),
                usuario.getNIF_Entidad()
        );
    }
    @DeleteMapping("/usuario/{DNI}")
    public ResponseEntity<String> eliminarUsuario(@PathVariable String DNI) {
        boolean resultado = UsuarioBBDD.eliminarUsuarioBBDD(DNI);
        if (resultado) {
            return new ResponseEntity<>("Usuario eliminado con éxito.", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Error al eliminar el usuario. Es posible que el usuario no exista.", HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/tipoEntidad/{nif}")
    public ResponseEntity<String> obtenerTipoEntidadPorNIF(@PathVariable String nif) {
        UsuarioBBDD entidadBBDD = new UsuarioBBDD();
        String tipo = entidadBBDD.obtenerTipoEntidadPorNIF(nif);

        if (tipo != null) {
            return new ResponseEntity<>(tipo, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/nifPorCorreo/{correo}")
    public ResponseEntity<String> obtenerNifPorCorreo(@PathVariable String correo) {
        UsuarioBBDD usuarioBBDD = new UsuarioBBDD();
        String nif = usuarioBBDD.obtenerNifPorCorreo(correo);

        if (nif != null) {
            return new ResponseEntity<>(nif, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/dniPorCorreo/{correo}")
    public ResponseEntity<String> obtenerDniPorCorreo(@PathVariable String correo) {
        UsuarioBBDD usuarioBBDD = new UsuarioBBDD();
        String dni = usuarioBBDD.obtenerDniPorCorreo(correo);

        if (dni != null) {
            return new ResponseEntity<>(dni, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/nombrePorCorreo/{correo}")
    public ResponseEntity<String> obtenerNombrePorCorreo(@PathVariable String correo) {
        UsuarioBBDD usuarioBBDD = new UsuarioBBDD();
        String nombre = usuarioBBDD.obtenerNombrePorCorreo(correo);

        if (nombre != null) {
            return new ResponseEntity<>(nombre, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/nombreEntidad/{nif}")
    public ResponseEntity<String> obtenerNombreEntidadPorNifB(@PathVariable String nif) {
        UsuarioBBDD usuarioBBDD = new UsuarioBBDD();
        String nombre = usuarioBBDD.obtenerNombreEntidadPorNif(nif);

        if (nombre != null) {
            return new ResponseEntity<>(nombre, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/EntidadPorCorreo/{correo}")
    public ResponseEntity<String> obteneEntidadPorCorreo(@PathVariable String correo) {
        UsuarioBBDD usuarioBBDD = new UsuarioBBDD();
        String resultado = usuarioBBDD.obteneEntidadPorCorreo(correo);

        switch (resultado) {
            case "error_bd":
                return new ResponseEntity<>("Error al consultar la base de datos.", HttpStatus.INTERNAL_SERVER_ERROR);
            case "no encontrado":
                return new ResponseEntity<>("Correo no encontrado en ninguna tabla.", HttpStatus.NOT_FOUND);
            default:
                return new ResponseEntity<>(resultado, HttpStatus.OK);
        }
    }
    @PutMapping("/usuarioModificados")
    public boolean modificarProductos(@RequestBody Usuario usuario) {
        return UsuarioBBDD.modificarUsuarioBBDD(usuario.getDNI(), usuario.getNombre(), usuario.getApellidos(), usuario.getCorreo_Electronico(), usuario.getContraseña(), usuario.getNIF_Entidad());
    }

}


