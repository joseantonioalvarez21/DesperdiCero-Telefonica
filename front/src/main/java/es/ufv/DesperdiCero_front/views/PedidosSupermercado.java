package es.ufv.DesperdiCero_front.views;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import es.ufv.DesperdiCero_front.UserService;
import es.ufv.DesperdiCero_front.models.Pedido;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;

@Route("/listadoPedidos")
public class PedidosSupermercado extends VerticalLayout{
    public PedidosSupermercado(@Autowired UserService service) throws Exception {
        //-------------------------------- Obtener datos del usuario registrado -------------------------------------
        String correoUsuario = (String) VaadinSession.getCurrent().getAttribute("usuarioCorreo");
        if (correoUsuario == null || correoUsuario.isEmpty()) {
            UI.getCurrent().navigate(LoginView.class);
            return;
        }
        System.out.println(correoUsuario);
        String TablaEntidad = service.obteneEntidadPorCorreo(correoUsuario);
        String tipoEntidad = service.obtenerTipoEntidadPorCorreo(correoUsuario);
        String nifValidar = service.obtenerNifPorCorreo(correoUsuario);
        System.out.println(nifValidar);
        //-----------------------------------------------------------------------------------------------------------

        MenuBar menuBar = new MenuBar(service);
        add(menuBar);

        VerticalLayout contentTab1 = new VerticalLayout();
        H1 titulo1Inicio = new H1("Listado de pedidos");
        Paragraph parrafo = new Paragraph("En esta página puedes consultar y modificar el estado de los pedidos recibidos.");


        //--------------------------------------------Listado de pedidos-----------------------------------------------
        ArrayList<Pedido> pedidos = service.getPedidos(nifValidar);

        Grid<Pedido> grid = new Grid<>();
        grid.setHeight("500px");
        grid.setWidth("900px");
        grid.addColumn(Pedido::getId_pedido).setHeader("Id del pedido");
        grid.addColumn(Pedido::getFecha).setHeader("Fecha");
        grid.addColumn(Pedido::getDNI).setHeader("DNI del cliente");


        // Crear un componente desplegable para los estados de pedido
        ComboBox<String> estadoCombo = new ComboBox<>();
        estadoCombo.setItems("Registrado", "Preparando", "Listo para entregar", "Entregado");

        // Agregar la columna de desplegables a la cuadrícula
        grid.addComponentColumn(pedido -> {
            ComboBox<String> combo = new ComboBox<>();
            combo.setItems("Registrado", "Preparando", "Listo para entregar", "Entregado");
            combo.setValue(pedido.getEstado()); // Establecer el valor inicial del desplegable como el estado actual del pedido
            return combo;
        }).setHeader("Modificar estado del pedido");

        grid.setItems(service.getPedidos(nifValidar));

        contentTab1.add(titulo1Inicio, parrafo, grid);
        add(contentTab1);


    }

}
