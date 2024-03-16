package es.ufv.DesperdiCero_front.views;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import es.ufv.DesperdiCero_front.UserService;
import es.ufv.DesperdiCero_front.UserService;
import es.ufv.DesperdiCero_front.models.Entidad;
import es.ufv.DesperdiCero_front.models.Suscripcion;
import org.springframework.beans.factory.annotation.Autowired;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import org.springframework.web.client.HttpClientErrorException;

import java.util.ArrayList;

@Route("/suscripcion")
public class SuscripcionView extends VerticalLayout{
    public SuscripcionView(@Autowired UserService service) throws Exception {
        try {
            //---------------------------------- Obtención de datos del usuario -------------------------------------------
            String correoUsuario = (String) VaadinSession.getCurrent().getAttribute("usuarioCorreo");
            if (correoUsuario == null || correoUsuario.isEmpty()) {
                UI.getCurrent().navigate(LoginView.class);
                return;
            }
            String dniUsuario = service.obtenerDniPorCorreo(correoUsuario);
            System.out.println(dniUsuario);
            String nombreUsuario = service.obtenerNombrePorCorreo(correoUsuario);
            String nifEntidad = service.obtenerNifPorCorreo(correoUsuario);
            String nombreEntidad = service.obtenerNombreEntidadPorNif(nifEntidad);
            //-------------------------------------------------------------------------------------------------------------

            MenuBar menuBar = new MenuBar(service);
            add(menuBar);

            VerticalLayout contentTab1 = new VerticalLayout();

            H1 titulo1Inicio = new H1("Suscripciones");
            Paragraph parrafo = new Paragraph("Puedes consultar las suscripciones activas a los supermercados.");
            contentTab1.add(titulo1Inicio, parrafo);

            Button popupButton = new Button("+");
            popupButton.getStyle().set("background-color", "green");
            popupButton.getStyle().set("color", "white");
            popupButton.addClickListener(event -> {
                Dialog popup = new Dialog();
                VerticalLayout dialogContent = new VerticalLayout();
                popup.setWidth("550px");

                popup.add(new H2("Nueva suscripción"));
                popup.add(new Paragraph("Suscríbete a un supermercado para estar informado sobre los nuevos productos."));
                popup.add(new H3("Información personal"));
                popup.add(new Paragraph("Las notificaciones se realizarán automáticamente al correo electrónico guardado."));

                TextField nombre_usuario = new TextField();
                nombre_usuario.setWidth("180px");
                nombre_usuario.setReadOnly(true);
                nombre_usuario.setLabel("Nombre");
                nombre_usuario.setValue(nombreUsuario);

                TextField campoCorreo = new TextField();
                campoCorreo.setWidth("300px");
                campoCorreo.setReadOnly(true);
                campoCorreo.setLabel("Correo electrónico");
                campoCorreo.setValue(correoUsuario);

                HorizontalLayout horizontalLayout0 = new HorizontalLayout(nombre_usuario, campoCorreo);
                horizontalLayout0.getElement().setAttribute("style", "padding-bottom: 20px;");
                popup.add(horizontalLayout0);

                popup.add(new H3("Entidad a la que perteneces"));

                TextField nombre_entidad = new TextField();
                nombre_entidad.setWidth("300px");
                nombre_entidad.setReadOnly(true);
                nombre_entidad.setLabel("Nombre");
                nombre_entidad.setValue(nombreEntidad);

                TextField nif_entidad = new TextField();
                nif_entidad.setWidth("180px");
                nif_entidad.setReadOnly(true);
                nif_entidad.setLabel("CIF");
                nif_entidad.setValue(nifEntidad);

                HorizontalLayout horizontalLayout01 = new HorizontalLayout(nombre_entidad, nif_entidad);
                horizontalLayout01.getElement().setAttribute("style", "padding-bottom: 20px;");
                popup.add(horizontalLayout01);

                popup.add(new H3("Información de la suscripción"));

                // Obtener la lista de entidades de tipo "Supermercado"
                var ref = new Object() {
                    ArrayList<Entidad> entidadesSupermercado = null;
                };
                try {
                    ref.entidadesSupermercado = service.getEntidades("Supermercado");
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

                Select<String> nombreSuper = new Select<>();
                nombreSuper.setLabel("Nombre del supermercado");
                ArrayList<String> nombresSupermercado = new ArrayList<>();

                for (Entidad entidad : ref.entidadesSupermercado) {
                    nombresSupermercado.add(entidad.getNombre());
                }
                nombreSuper.setItems(nombresSupermercado);

                nombreSuper.setWidth("480px");
                HorizontalLayout horizontalLayout1 = new HorizontalLayout(nombreSuper);

                // Botón dentro del popup
                Button guardarButton = new Button("Crear la suscripción", event2 -> {
                    // Verificar si todos los campos están completados
                    if (nombreSuper.isEmpty()) {
                        Notification notification = Notification.show("Por favor, complete todos los campos.");
                        notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                        return; // No proceder con el guardado si algún campo está vacío
                    }

                    // Obtener el nombre del supermercado seleccionado en el desplegable
                    String nombreSupermercadoSeleccionado = nombreSuper.getValue();

                    // Buscar el objeto Entidad correspondiente al supermercado seleccionado
                    Entidad supermercadoSeleccionado = ref.entidadesSupermercado.stream()
                            .filter(entidad -> entidad.getNombre().equals(nombreSupermercadoSeleccionado))
                            .findFirst()
                            .orElse(null);

                    // Verificar si se encontró el supermercado seleccionado
                    if (supermercadoSeleccionado == null) {
                        Notification notification = Notification.show("No se pudo encontrar el supermercado seleccionado.");
                        notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                        return; // No proceder si no se encontró el supermercado seleccionado
                    }

                    // Obtener el CIF del supermercado seleccionado
                    String cifSupermercado = supermercadoSeleccionado.getNif();

                    // Crear un objeto de tipo Suscripcion con los datos obtenidos
                    Suscripcion suscripcion = new Suscripcion(dniUsuario, cifSupermercado);

                    try {
                        service.PutSuscripcion(suscripcion);
                    } catch (Exception e) {
                        Notification notification = Notification
                                .show("La suscripción no se pudo crear.");
                        notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                        throw new RuntimeException(e);
                    }
                    Notification notification = Notification
                            .show("Suscripción creada correctamente.");
                    notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                    popup.close();

                });

                guardarButton.getStyle().set("background-color", "green");
                guardarButton.getStyle().set("color", "white");


                // Botón para cerrar el popup
                Button cancelarButton = new Button("Cancelar", event2 -> {
                    popup.close();
                });
                cancelarButton.getStyle().set("color", "black");

                HorizontalLayout botonesLayout = new HorizontalLayout(guardarButton, cancelarButton);
                botonesLayout.getStyle().set("padding-top", "35px");
                botonesLayout.setSpacing(true); // Agregar espacio entre los botones (opcional)
                popup.add(horizontalLayout1, botonesLayout);
                // Abrir el popup
                popup.open();
            });

            contentTab1.add(popupButton);
            add(contentTab1);
            //------------------------------------- Listado de suscripciones ----------------------------------------------
            try {
                ArrayList<Suscripcion> suscripciones = service.getSuscripciones(dniUsuario);
                int cantidadSuscripciones = suscripciones.size();
                Grid<Suscripcion> grid = new Grid<>();
                grid.setHeight("300px");
                grid.setWidth("500px");
                grid.addColumn(Suscripcion::getId_suscripcion).setHeader("Id suscripcion");
                grid.addColumn(Suscripcion::getCif).setHeader("CIF Entidad");
                grid.addColumn(Suscripcion::getFecha_alta).setHeader("Fecha creación");

                grid.setItems(service.getSuscripciones(dniUsuario));
                grid.addColumn(new ComponentRenderer<>(suscripcion -> {
                    Button eliminarButton = new Button(VaadinIcon.TRASH.create(), clickEvent -> {
                        try {
                            service.eliminarSuscripcion(suscripcion.getId_suscripcion());
                            Notification.show("La suscripción ha sido dada de baja correctamente").addThemeVariants(NotificationVariant.LUMO_SUCCESS);


                        } catch (Exception e) {
                            Notification.show("Error al dar de baja la suscripción: " + e.getMessage()).addThemeVariants(NotificationVariant.LUMO_ERROR);
                        }
                    });

                    // Personalizar el botón con el icono de basura y fondo rojo
                    eliminarButton.addThemeVariants(ButtonVariant.LUMO_ERROR, ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_CONTRAST);
                    eliminarButton.getStyle().set("background-color", "white").set("color", "red");

                    return eliminarButton;
                })).setHeader("");
                add(grid);

            } catch (Exception e) {
                System.out.println("No hay suscripciones");
                Paragraph textoNoHaySuscripciones = new Paragraph("No existen suscripciones");
                contentTab1.add(textoNoHaySuscripciones);
            }
        }
        catch (HttpClientErrorException.NotFound e){
            UI.getCurrent().navigate("http://localhost:8080/errorAcceso");

        }
    }
}
