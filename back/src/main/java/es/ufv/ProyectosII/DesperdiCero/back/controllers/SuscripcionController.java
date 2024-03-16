package es.ufv.ProyectosII.DesperdiCero.back.controllers;

import es.ufv.ProyectosII.DesperdiCero.back.dataAccess.SuscripcionBBDD;
import es.ufv.ProyectosII.DesperdiCero.back.models.Suscripcion;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
public class SuscripcionController {
    @GetMapping("/suscripciones")
    public ArrayList<Suscripcion> suscripciones() {
        ArrayList<Suscripcion> listaSuscripciones = new ArrayList<Suscripcion>();
        SuscripcionBBDD suscripcionBBDD = new SuscripcionBBDD();
        listaSuscripciones = suscripcionBBDD.leerSuscripcionBBDD();
        return listaSuscripciones;
    }

    @GetMapping("/suscripciones/{dni}")
    public ResponseEntity<ArrayList<Suscripcion>> getPorDni(@PathVariable String dni) {
        ArrayList<Suscripcion> listaSuscripciones = new SuscripcionBBDD().leerSuscripcionBBDD();
        ArrayList<Suscripcion> suscripcionesEncontradas = new ArrayList<>();

        for (Suscripcion suscripcion : listaSuscripciones) {
            if (suscripcion.getDni().equalsIgnoreCase(dni)) {
                suscripcionesEncontradas.add(suscripcion);
            }
        }

        if (suscripcionesEncontradas.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(suscripcionesEncontradas, HttpStatus.OK);
        }
    }
    @PutMapping("/nuevaSuscripcion")
    public boolean nuevaSuscripcion (@RequestBody Suscripcion suscripcion) {
        return SuscripcionBBDD.escribirSuscripcionBBDD(suscripcion.getDni(), suscripcion.getCif());
    }
    @DeleteMapping("/suscripcion/{id_suscripcion}")
    public ResponseEntity<String> eliminarUsuario(@PathVariable int id_suscripcion) {
        boolean resultado = SuscripcionBBDD.eliminarSuscripcionBBDD(id_suscripcion);
        if (resultado) {
            return new ResponseEntity<>("Suscripción eliminada con éxito.", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Error al eliminar la suscripción. Es posible que no exista.", HttpStatus.NOT_FOUND);
        }
    }

}
