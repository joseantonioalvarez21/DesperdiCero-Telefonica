package es.ufv.ProyectosII.DesperdiCero.back.controllers;

import es.ufv.ProyectosII.DesperdiCero.back.dataAccess.PedidoBBDD;
import es.ufv.ProyectosII.DesperdiCero.back.dataAccess.ProductosBBDD;
import es.ufv.ProyectosII.DesperdiCero.back.dataAccess.SuscripcionBBDD;
import es.ufv.ProyectosII.DesperdiCero.back.models.Pedido;
import es.ufv.ProyectosII.DesperdiCero.back.models.Producto;
import es.ufv.ProyectosII.DesperdiCero.back.models.Suscripcion;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
@RestController
public class PedidosController {
    @GetMapping("/pedidos")//FUNCION PARA PASAR TODOS LOS DATOS DEL PRIMER JSON
    public ArrayList<Pedido> pedidos() {
        ArrayList<Pedido> listaPedidos = new ArrayList<Pedido>();
        PedidoBBDD pedidoBBDD = new PedidoBBDD();
        listaPedidos = pedidoBBDD.leerPedidosBBDD();
        return listaPedidos;
    }
    @GetMapping("/pedidos/{cif}")
    public ResponseEntity<ArrayList<Pedido>> getPorCif (@PathVariable String cif) {
        ArrayList<Pedido> listaPedidos = new PedidoBBDD().leerPedidosBBDD();
        ArrayList<Pedido> listaPedidosEncontrados = new ArrayList<>();

        for (Pedido pedido : listaPedidos) {
            if (pedido.getCIF_entidad().equalsIgnoreCase(cif)) {
                listaPedidosEncontrados.add(pedido);
            }
        }

        if (listaPedidosEncontrados.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(listaPedidosEncontrados, HttpStatus.OK);
        }
    }
}
