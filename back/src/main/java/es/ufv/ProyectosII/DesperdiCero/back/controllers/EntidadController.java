package es.ufv.ProyectosII.DesperdiCero.back.controllers;


import es.ufv.ProyectosII.DesperdiCero.back.dataAccess.EntidadesBBDD;
import es.ufv.ProyectosII.DesperdiCero.back.models.Entidad;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;
import java.util.ArrayList;
@Component("entidadController1")
@RestController
public class EntidadController {
    @GetMapping("/entidades")
    public ArrayList<Entidad> entidades() {
        ArrayList<Entidad> listaEntidades = new ArrayList<Entidad>();
        EntidadesBBDD entidadesBBDD = new EntidadesBBDD();
        listaEntidades = entidadesBBDD.leerEntidadesBBDD();
        return listaEntidades;
    }
    @GetMapping("/entidades/{tipo}")
    public ArrayList<Entidad> entidadesTipo(@PathVariable String tipo) {
        ArrayList<Entidad> listaEntidades = new ArrayList<Entidad>();
        EntidadesBBDD entidadesBBDD = new EntidadesBBDD();
        listaEntidades = entidadesBBDD.leerEntidadesPoTipoBBDD(tipo);
        return listaEntidades;
    }
    @PutMapping("/entidadActualizada")
    public boolean actualizarEntidades(@RequestBody Entidad entidad) {
        return EntidadesBBDD.escribirEntidadesBBDD(entidad.getNif(), entidad.getNombre(), entidad.getTelefono(), entidad.getCorreo(), entidad.getContraseña(), entidad.getTipo(), entidad.getFechaAlta(), entidad.getDireccion());
    }
}
