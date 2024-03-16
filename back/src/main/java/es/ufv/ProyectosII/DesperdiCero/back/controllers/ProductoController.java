package es.ufv.ProyectosII.DesperdiCero.back.controllers;

import es.ufv.ProyectosII.DesperdiCero.back.dataAccess.ProductosBBDD;
import es.ufv.ProyectosII.DesperdiCero.back.models.Producto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
public class ProductoController {
    @GetMapping("/productos")//FUNCION PARA PASAR TODOS LOS DATOS DEL PRIMER JSON
    public ArrayList<Producto> productos() {
        ArrayList<Producto> listaProductos = new ArrayList<Producto>();
        ProductosBBDD productosBBDD = new ProductosBBDD();
        listaProductos = productosBBDD.leerProductosBBDD();
        return listaProductos;
    }
    @GetMapping("/productos/codigoBarras/{codigo_barras}")//FUNCION PARA PASAR TODOS LOS DATOS DEL PRIMER JSON
    public ResponseEntity<Producto> getPorCodigoBarras(@PathVariable String codigo_barras) {
        ArrayList<Producto> listaProductos = new ProductosBBDD().leerProductosBBDD();
        Producto encontrado = null;
        for (Producto datoAbuscar : listaProductos) {
            if (datoAbuscar.getCodigo_barras().equalsIgnoreCase(codigo_barras)) {
                encontrado = datoAbuscar;
            }
        }
        return new ResponseEntity<>(encontrado, HttpStatus.OK);
    }

    @GetMapping("/productos/nifEntidad/{nif_entidad}")//FUNCION PARA PASAR TODOS LOS DATOS DEL PRIMER JSON
    public ResponseEntity<Producto> getPorNifEntidad(@PathVariable String nif_entidad) {
        ArrayList<Producto> listaProductos = new ProductosBBDD().leerProductosBBDD();
        Producto encontrado = null;
        for (Producto datoAbuscar : listaProductos) {
            if (datoAbuscar.getNif_entidad().equalsIgnoreCase(nif_entidad)) {
                encontrado = datoAbuscar;
            }
        }
        return new ResponseEntity<>(encontrado, HttpStatus.OK);
    }
    @GetMapping("/productos/tipo/{tipo}")//FUNCION PARA PASAR TODOS LOS DATOS DEL PRIMER JSON
    public ResponseEntity<Producto> getPorTipo(@PathVariable String tipo) {
        ArrayList<Producto> listaProductos = new ProductosBBDD().leerProductosBBDD();
        Producto encontrado = null;
        for (Producto datoAbuscar : listaProductos) {
            if (datoAbuscar.getTipo().equalsIgnoreCase(tipo)) {
                encontrado = datoAbuscar;
            }
        }
        return new ResponseEntity<>(encontrado, HttpStatus.OK);
    }

    @PutMapping("/productosActualizados")
    public boolean actualizarProductos(@RequestBody Producto producto) {
        return ProductosBBDD.escribirProductosBBDD(producto.getId_productoDisp(), producto.getCodigo_barras(), producto.getNombre(), producto.getTipo(), producto.getFecha_caducidad(), producto.getEstado(), producto.getCantidad(), producto.getNif_entidad());
    }
    @PutMapping("/productosModificados")
    public boolean modificarProductos(@RequestBody Producto producto) {
        return ProductosBBDD.modificarProductoBBDD(producto.getId_productoDisp(), producto.getCodigo_barras(), producto.getNombre(), producto.getTipo(), producto.getFecha_caducidad(), producto.getEstado(), producto.getCantidad(), producto.getNif_entidad());
    }
    @DeleteMapping("/productos/{id_productoDisp}")
    public ResponseEntity<String> eliminarProducto(@PathVariable int id_productoDisp) {
        boolean resultado = ProductosBBDD.eliminarProductoBBDD(id_productoDisp);
        if (resultado) {
            return new ResponseEntity<>("Producto eliminado con éxito.", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Error al eliminar el producto.", HttpStatus.NOT_FOUND);
        }
    }
}
