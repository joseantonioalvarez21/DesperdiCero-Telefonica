package es.ufv.ProyectosII.DesperdiCero.back.controllers;

import es.ufv.ProyectosII.DesperdiCero.back.dataAccess.VerificarTipoCorreoBBDD;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class VerificarTipoCorreoController {

    private VerificarTipoCorreoBBDD verificarCorreoEnTabla;

    @Autowired
    public VerificarTipoCorreoController(VerificarTipoCorreoBBDD verificarCorreoEnTabla) {
        this.verificarCorreoEnTabla = verificarCorreoEnTabla;
    }

    @GetMapping("/datosUsuario/{correo}")
    public ResponseEntity<String> obteneEntidadPorCorreo(@PathVariable String correo) {
        VerificarTipoCorreoBBDD usuarioBBDD = new VerificarTipoCorreoBBDD();
        String resultado = VerificarTipoCorreoBBDD.obteneEntidadPorCorreo(correo);

        switch (resultado) {
            case "error_bd":
                return new ResponseEntity<>("Error al consultar la base de datos.", HttpStatus.INTERNAL_SERVER_ERROR);
            case "no encontrado":
                return new ResponseEntity<>("Correo no encontrado en ninguna tabla.", HttpStatus.NOT_FOUND);
            default:
                return new ResponseEntity<>(resultado, HttpStatus.OK);
        }
    }
    @GetMapping("/datosUsuario/{correo}/direccion")
    public ResponseEntity<String> obteneDireccionPorCorreo(@PathVariable String correo) {
        VerificarTipoCorreoBBDD usuarioBBDD = new VerificarTipoCorreoBBDD();
        String resultado = VerificarTipoCorreoBBDD.obtenerDireccionEntidad(correo);

        switch (resultado) {
            case "error_bd":
                return new ResponseEntity<>("Error al consultar la base de datos.", HttpStatus.INTERNAL_SERVER_ERROR);
            case "no encontrado":
                return new ResponseEntity<>("Correo no encontrado en ninguna tabla.", HttpStatus.NOT_FOUND);
            default:
                return new ResponseEntity<>(resultado, HttpStatus.OK);
        }
    }
    @GetMapping("/datosUsuario/{correo}/nombre")
    public ResponseEntity<String> obtenerNombrePorCorreo(@PathVariable String correo) {
        VerificarTipoCorreoBBDD usuarioBBDD = new VerificarTipoCorreoBBDD();
        String resultado = VerificarTipoCorreoBBDD.obtenerNombreEntidad(correo);

        switch (resultado) {
            case "error_bd":
                return new ResponseEntity<>("Error al consultar la base de datos.", HttpStatus.INTERNAL_SERVER_ERROR);
            case "no encontrado":
                return new ResponseEntity<>("Correo no encontrado en ninguna tabla.", HttpStatus.NOT_FOUND);
            default:
                return new ResponseEntity<>(resultado, HttpStatus.OK);
        }
    }

    @GetMapping("/datosUsuario/{correo}/CIF_entidad")
    public ResponseEntity<String> obtenerCifEntidadPorCorreo(@PathVariable String correo) {
        VerificarTipoCorreoBBDD usuarioBBDD = new VerificarTipoCorreoBBDD();
        String resultado = VerificarTipoCorreoBBDD.obtenerCifEntidadEntidad(correo);

        switch (resultado) {
            case "error_bd":
                return new ResponseEntity<>("Error al consultar la base de datos.", HttpStatus.INTERNAL_SERVER_ERROR);
            case "no encontrado":
                return new ResponseEntity<>("Correo no encontrado en ninguna tabla.", HttpStatus.NOT_FOUND);
            default:
                return new ResponseEntity<>(resultado, HttpStatus.OK);
        }
    }
    @GetMapping("/datosUsuario/{correo}/nombreTrabajador")
    public ResponseEntity<String> obtenerNombreTrabajadorPorCorreo(@PathVariable String correo) {
        VerificarTipoCorreoBBDD usuarioBBDD = new VerificarTipoCorreoBBDD();
        String resultado = VerificarTipoCorreoBBDD.obtenerNombreTrabajador(correo);

        switch (resultado) {
            case "error_bd":
                return new ResponseEntity<>("Error al consultar la base de datos.", HttpStatus.INTERNAL_SERVER_ERROR);
            case "no encontrado":
                return new ResponseEntity<>("Correo no encontrado en ninguna tabla.", HttpStatus.NOT_FOUND);
            default:
                return new ResponseEntity<>(resultado, HttpStatus.OK);
        }
    }
    @GetMapping("/datosUsuario/{correo}/apellidosTrabajador")
    public ResponseEntity<String> obtenerApellidosTrabajadorPorCorreo(@PathVariable String correo) {
        VerificarTipoCorreoBBDD usuarioBBDD = new VerificarTipoCorreoBBDD();
        String resultado = VerificarTipoCorreoBBDD.obtenerApellidoTrabajador(correo);

        switch (resultado) {
            case "error_bd":
                return new ResponseEntity<>("Error al consultar la base de datos.", HttpStatus.INTERNAL_SERVER_ERROR);
            case "no encontrado":
                return new ResponseEntity<>("Correo no encontrado en ninguna tabla.", HttpStatus.NOT_FOUND);
            default:
                return new ResponseEntity<>(resultado, HttpStatus.OK);
        }
    }
    @GetMapping("/datosUsuario/{correo}/DNITrabajador")
    public ResponseEntity<String> obtenerDNITrabajadorPorCorreo(@PathVariable String correo) {
        VerificarTipoCorreoBBDD usuarioBBDD = new VerificarTipoCorreoBBDD();
        String resultado = VerificarTipoCorreoBBDD.obtenerDNITrabajador(correo);

        switch (resultado) {
            case "error_bd":
                return new ResponseEntity<>("Error al consultar la base de datos.", HttpStatus.INTERNAL_SERVER_ERROR);
            case "no encontrado":
                return new ResponseEntity<>("Correo no encontrado en ninguna tabla.", HttpStatus.NOT_FOUND);
            default:
                return new ResponseEntity<>(resultado, HttpStatus.OK);
        }
    }
}
