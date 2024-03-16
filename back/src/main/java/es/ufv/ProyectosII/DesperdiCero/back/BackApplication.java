package es.ufv.ProyectosII.DesperdiCero.back;

import es.ufv.ProyectosII.DesperdiCero.back.dataAccess.ProductosBBDD;
import es.ufv.ProyectosII.DesperdiCero.back.dataAccess.UsuarioBBDD;
import es.ufv.ProyectosII.DesperdiCero.back.models.Producto;
import es.ufv.ProyectosII.DesperdiCero.back.models.Usuario;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class BackApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackApplication.class, args);
		List<Producto> listaProductos = new ArrayList<Producto>();
		ProductosBBDD productosBBDD = new ProductosBBDD();
		listaProductos=productosBBDD.leerProductosBBDD();
		List<Usuario>listaUsuario=new ArrayList<>();
		UsuarioBBDD usuarioBBDD= new UsuarioBBDD();
		listaUsuario=usuarioBBDD.leerUsuariosBBDD();

	}
}
