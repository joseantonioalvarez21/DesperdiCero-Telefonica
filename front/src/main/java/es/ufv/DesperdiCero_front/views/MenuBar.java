package es.ufv.DesperdiCero_front.views;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.theme.lumo.LumoUtility;
import es.ufv.DesperdiCero_front.UserService;
import es.ufv.DesperdiCero_front.UserService;

public class MenuBar extends AppLayout {
    private UserService userService;

    // Modifica el constructor para aceptar GreetService
    public MenuBar(UserService greetService) {
        this.userService = greetService;

        H1 title = new H1("DesperdiCero");
        title.getStyle().set("font-size", "var(--lumo-font-size-l)")
                .set("left", "var(--lumo-space-l)").set("margin", "0")
                .set("position", "absolute");

        HorizontalLayout navigation = getNavigation(); // Asume que getNavigation ahora usa greetService
        navigation.getElement();

        addToNavbar(title, navigation);
    }

    private HorizontalLayout getNavigation() {
        HorizontalLayout navigation = new HorizontalLayout();
        navigation.addClassNames(LumoUtility.JustifyContent.CENTER,
                LumoUtility.Gap.SMALL, LumoUtility.Height.MEDIUM,
                LumoUtility.Width.FULL);

        navigation.add(createLink("Tablero principal", MainView.class));

        // Ahora verifica el tipo de entidad antes de añadir el enlace de Usuarios
        try {
            String correoUsuario = (String) VaadinSession.getCurrent().getAttribute("usuarioCorreo");

            if (correoUsuario != null && !correoUsuario.isEmpty()) {
                String resultado = userService.obteneEntidadPorCorreo(correoUsuario);
                if ("Entidad".equalsIgnoreCase(resultado)) {
                    navigation.add(createLink("Usuarios", Perfil.class));
                }
                else if("UsuarioEntidad".equalsIgnoreCase(resultado)){
                    navigation.add(createLink("Suscripciones", SuscripcionView.class));
                }

            }
        } catch (Exception e) {
            e.printStackTrace(); // Considera un manejo de errores más adecuado
        }

        navigation.add(createLink("Perfil", DatosPersonalesUsuarioView.class));
        return navigation;
    }

    private RouterLink createLink(String viewName, Class<?> viewClass) {
        RouterLink link = new RouterLink();
        link.add(viewName);
        link.setRoute((Class<? extends Component>) viewClass);

        link.addClassNames(LumoUtility.Display.FLEX,
                LumoUtility.AlignItems.CENTER,
                LumoUtility.Padding.Horizontal.MEDIUM,
                LumoUtility.TextColor.SECONDARY, LumoUtility.FontWeight.MEDIUM);
        link.getStyle().set("text-decoration", "none");

        return link;
    }
}