package es.ufv.DesperdiCero_front.views;


//import com.vaadin.demo.DemoExporter;

import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.router.Route;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import es.ufv.DesperdiCero_front.UserService;
import es.ufv.DesperdiCero_front.models.Entidad;
import es.ufv.DesperdiCero_front.models.Usuario;

@Route("/datosUsuario")
public class DatosPersonalesUsuarioView extends VerticalLayout {

    public DatosPersonalesUsuarioView(UserService userService) throws Exception {
        String correoUsuario = (String) VaadinSession.getCurrent().getAttribute("usuarioCorreo");
        if (correoUsuario == null || correoUsuario.isEmpty()) {
            UI.getCurrent().navigate(LoginView.class);
            return;
        }
        System.out.println(correoUsuario);

        String tipo = userService.obteneEntidadPorCorreoView(correoUsuario);
        Usuario usuario = obtenerUsuario(correoUsuario);
        System.out.println("correo electronico prueba:"+correoUsuario);
        System.out.println("El tipo es: "+tipo);


        MenuBar menuBar = new MenuBar(userService);
        add(menuBar);
        String nombre = null;
        try {

            System.out.println(tipo);
            if ("UsuarioEntidad".equals(tipo)) {

                if (correoUsuario == null || usuario==null) {
                    UI.getCurrent().navigate(LoginView.class);
                    return;
                }

                /*Añadir icono usuario*/

                VerticalLayout layout = new VerticalLayout();
                layout.setSpacing(true);

                // Crear VerticalLayout para la información del usuario
                HorizontalLayout userInfoLayout1 = new HorizontalLayout();
                userInfoLayout1.setSpacing(true);
                userInfoLayout1.addClassName("items-center");

                // Añadir solo la imagen sin el icono
                Icon userIcon = new Icon(VaadinIcon.USER);

                // Agrega el icono a tu diseño
                add(userIcon);
                userIcon.setSize("100px");

//------------------------------------------------------------------------------------------------------------------
                String NombreTrabajador=userService.obtenerNombreTrabajadorPorCorreo(correoUsuario);
                System.out.println("Nombre del usuario: "+ NombreTrabajador);
                String Apellidos=userService.obtenerApellidosTrabajadorPorCorreo(correoUsuario);
                System.out.println(Apellidos);
                String DNITrabajador=userService.obtenerDNITrabajadorPorCorreo(correoUsuario);
                System.out.println("DNI :"+ DNITrabajador);

                H1 title = new H1("Bienvenido al área personal, " + NombreTrabajador + " " + Apellidos);
                setJustifyContentMode(JustifyContentMode.CENTER);
                setAlignItems(Alignment.CENTER);



                H5 texto = new H5("En esta sección podrás modificar la contraseña, visualizar tu información de trabajador y cerrar la sesión en la aplicación.");
                texto.getStyle().set("font-weight", "normal"); // Establece el peso de la fuente a normal

                TextField correoTextField = new TextField("Correo electrónico");
                correoTextField.setValue(usuario.getCorreo_Electronico());
                correoTextField.setReadOnly(true);
                correoTextField.setWidth("300px"); // Ajusta el ancho según tus necesidades


                TextField DNITrabajadorTextField = new TextField("DNI");
                DNITrabajadorTextField.setValue(userService.obtenerDNITrabajadorPorCorreo(correoUsuario));
                DNITrabajadorTextField.setReadOnly(true);
                DNITrabajadorTextField.setWidth("300px"); // Ajusta el ancho según tus necesidades


                userInfoLayout1.add(correoTextField, DNITrabajadorTextField);

                HorizontalLayout userInfoLayout2 = new HorizontalLayout();
                userInfoLayout2.setSpacing(true);
                userInfoLayout2.addClassName("items-center");


                Button cambiarContrasenaButton = new Button("Cambiar contraseña");
                cambiarContrasenaButton.addClickListener(event -> {
                    // Crear un diálogo para que el usuario ingrese la nueva contraseña
                    Dialog dialog = new Dialog();
                    H2 titulo = new H2("Restablecer contraseña");
                    H5 texto2 = new H5("En este apartado podrás modificar la contraseña actual por una nueva.");
                    H5 texto3 = new H5("Requisitos para la contraseña nueva: 8 caracteres alfanuméricos (números y letras)");
                    texto3.getStyle().set("font-size", "small");  // Establecer el tamaño del texto
                    texto3.getStyle().set("color", "gray");        // Establecer el color del texto

                    texto2.getStyle().set("font-weight", "normal"); // Establece el peso de la fuente a normal


                    // Usar un VerticalLayout para los campos de contraseña
                    VerticalLayout layoutContrasena = new VerticalLayout();
                    layoutContrasena.setSpacing(true);
                    layoutContrasena.setDefaultHorizontalComponentAlignment(Alignment.CENTER); // Centra los componentes horizontalmente

                    PasswordField contrasenaActualField = new PasswordField("Contraseña actual*");
                    contrasenaActualField.setWidth("300px"); // Ajusta el ancho según tus necesidades

                    PasswordField nuevaContrasenaField = new PasswordField("Nueva contraseña*");
                    nuevaContrasenaField.setWidth("300px"); // Ajusta el ancho según tus necesidades

                    PasswordField confirmarContrasenaField = new PasswordField("Confirmar contraseña*");
                    confirmarContrasenaField.setWidth("300px"); // Ajusta el ancho según tus necesidades



                    Button guardarButton = new Button("Guardar");
                    guardarButton.getStyle().set("background-color", "green"); // Establece el color de fondo a verde
                    guardarButton.getStyle().set("color", "white"); // Establece el color del texto a blanco

                    Button cancelarButton = new Button("Cancelar", e -> dialog.close());
                    cancelarButton.getStyle().set("color", "black");

                    HorizontalLayout buttonLayout = new HorizontalLayout();
                    buttonLayout.setWidthFull(); // Opcionalmente, puedes hacer que el layout use todo el ancho disponible
                    buttonLayout.setJustifyContentMode(JustifyContentMode.CENTER); // Centra los botones en el diálogo
                    buttonLayout.add(cancelarButton, guardarButton);

                    guardarButton.addClickListener(e -> {
                        String correoActual = correoTextField.getValue();
                        String contrasenaActual = contrasenaActualField.getValue();
                        System.out.println("Contraseña actual: "+ userService.validarContrasena(correoActual,contrasenaActual));


                        try {
                            // Verificar que la contraseña actual coincide con la almacenada en la base de datos
                            boolean contrasenaActualCorrecta = userService.validarContrasena(correoActual, contrasenaActual);

                            if (!contrasenaActualCorrecta) {
                                Notification.show("La contraseña actual no es correcta").addThemeVariants(NotificationVariant.LUMO_ERROR);
                                return;
                            }

                            // Continuar con la lógica de validación y actualización de la contraseña
                            String nuevaContraseña = nuevaContrasenaField.getValue();
                            String confirmarContraseña = confirmarContrasenaField.getValue();
                            String passwordPattern = "^(?=.*[0-9])(?=.*[a-zA-Z]).{8,}$"; // Patrón para requerir al menos 8 caracteres alfanuméricos

                            if (!nuevaContraseña.matches(passwordPattern)) {
                                Notification.show("La contraseña debe tener al menos 8 caracteres y contener números y letras.").addThemeVariants(NotificationVariant.LUMO_ERROR);
                                return; // No proceder con el guardado si la nueva contraseña no cumple con los requisitos
                            }

                            if (!nuevaContraseña.equals(confirmarContraseña)) {
                                Notification.show("Las contraseñas no coinciden").addThemeVariants(NotificationVariant.LUMO_ERROR);
                                return;
                            }

                            // Actualizar la contraseña del usuario
                            usuario.setContraseña(nuevaContraseña);
                            Usuario usuarioActualizado = userService.PutCambiarContrasena(usuario);

                            if (usuarioActualizado != null) {
                                dialog.close();
                                Notification.show("Contraseña actualizada correctamente").addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                            } else {
                                throw new Exception("Error al actualizar la contraseña");
                            }
                        } catch (Exception ex) {
                            Notification.show("Error al procesar la solicitud: " + ex.getMessage()).addThemeVariants(NotificationVariant.LUMO_ERROR);
                        }
                    });



                    // Agregar los componentes al VerticalLayout
                    layoutContrasena.add(contrasenaActualField, nuevaContrasenaField, confirmarContrasenaField, buttonLayout);

                    // Agregar el VerticalLayout al diálogo
                    dialog.add(titulo, texto2, texto3, layoutContrasena);
                    dialog.open();


                });


                Button cerrarSesionButton = new Button("Cerrar Sesión", e -> {
                    UI.getCurrent().navigate(LoginView.class);

                });

                // Crear un diseño vertical para los botones
                VerticalLayout buttonLayout = new VerticalLayout(cambiarContrasenaButton, cerrarSesionButton);
                buttonLayout.setSpacing(true);
                buttonLayout.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);

                layout.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
                layout.setSizeFull();

                layout.add(title, texto,  userInfoLayout1, userInfoLayout2,buttonLayout);

                add(layout);


            } else if ("Entidad".equals(tipo)) {
                System.out.println("Entidad");

                VerticalLayout layout2 = new VerticalLayout();
                layout2.setSpacing(true);

                // Crear Horizontallayout para la información del usuario
                HorizontalLayout userInfoLayout3 = new HorizontalLayout();
                userInfoLayout3.setSpacing(true);
                userInfoLayout3.addClassName("items-center");

                if (correoUsuario == null) {
                    System.out.println("He entrado aqui en excepcion de la entidad Entidad");
                    UI.getCurrent().navigate(LoginView.class);
                    return;
                }

                String direccion = userService.obtenerDireccionPorCorreo(correoUsuario);
                System.out.println("direccion entidad: "+direccion);
                String nombre1=userService.obtenerNombrePorCorreoView(correoUsuario);

                System.out.println(nombre1);
                System.out.println(direccion);

                Entidad entidad = obtenerEntidad(correoUsuario);
                System.out.println("Entidad 2");


                if (entidad == null) {
                    System.out.println("He entrado en excepcion entidad 2");
                    UI.getCurrent().navigate(LoginView.class);
                    return;
                }

                setJustifyContentMode(JustifyContentMode.CENTER);
                setAlignItems(Alignment.CENTER);
                System.out.println("Entidad 3");
                H2 title = new H2("Datos de la entidad");
                title.addClassName("items-center");

                H5 texto = new H5("En esta sección podrás modificar la contraseña, visualizar tu información como entidad y cerrar la sesión en la aplicación.");
                texto.getStyle().set("font-weight", "normal"); // Establece el peso de la fuente a normal


                Icon userIcon = new Icon(VaadinIcon.USER_STAR);

                // Agrega el icono a tu diseño
                add(userIcon);
                userIcon.setSize("100px");
                FlexLayout layout = new FlexLayout();
                layout.setJustifyContentMode(JustifyContentMode.CENTER);
                layout.setAlignItems(Alignment.CENTER);
                System.out.println("Entidad 3");


                HorizontalLayout infoMapaDatos = new HorizontalLayout();
                // Bloque izquierdo con correo, nombre y dirección
                VerticalLayout infoLayout = new VerticalLayout();
                infoLayout.setWidth("300px"); // Ajustar el ancho según sea necesario

                TextField correoTextField2 = new TextField("Correo electrónico");
                correoTextField2.setValue(entidad.getCorreo());
                correoTextField2.setReadOnly(true);
                correoTextField2.setWidth("300px"); // Ajusta el ancho según tus necesidades

                System.out.println("Entidad 5");
                TextField NombreTextField2 = new TextField("Nombre");
                NombreTextField2.setValue(userService.obtenerNombrePorCorreoView(correoUsuario));
                NombreTextField2.setReadOnly(true);
                NombreTextField2.setWidth("300px"); // Ajusta el ancho según tus necesidades

                userInfoLayout3.add(correoTextField2, NombreTextField2);


                System.out.println("Entidad 6");
                TextField DireccionTextField2 = new TextField("Dirección");
                DireccionTextField2.setValue(userService.obtenerDireccionPorCorreo(correoUsuario));
                DireccionTextField2.setReadOnly(true);
                DireccionTextField2.setWidth("300px"); // Ajusta el ancho según tus necesidades

                System.out.println("Entidad 8");
                TextField NIFTextField2 = new TextField("NIF");
                NIFTextField2.setValue(userService.obtenerNifPorCorreo(correoUsuario));
                NIFTextField2.setReadOnly(true);
                NIFTextField2.setWidth("300px"); // Ajusta el ancho según tus necesidades

                System.out.println("Entidad 9");

                HorizontalLayout userInfoLayout4 = new HorizontalLayout();
                userInfoLayout4.setSpacing(true);
                userInfoLayout4.addClassName("items-center");

                userInfoLayout4.add(DireccionTextField2, NIFTextField2);

                // Bloque derecho con el mapa
                Div mapaDiv = new Div();
                mostrarMapa(mapaDiv, direccion);


                Button cambiarContrasenaButton2 = new Button("Cambiar contraseña");
                cambiarContrasenaButton2.addClickListener(event -> {
                    // Crear un diálogo para que el usuario ingrese la nueva contraseña
                    Dialog dialog = new Dialog();
                    H2 titulo = new H2("Restablecer contraseña");
                    H5 texto2 = new H5("En este apartado podrás modificar la contraseña actual por una nueva.");
                    H5 texto3 = new H5("Requisitos para la contraseña nueva: 8 caracteres alfanuméricos (números y letras)");
                    texto3.getStyle().set("font-size", "small");  // Establecer el tamaño del texto
                    texto3.getStyle().set("color", "gray");        // Establecer el color del texto

                    texto2.getStyle().set("font-weight", "normal"); // Establece el peso de la fuente a normal


                    // Usar un VerticalLayout para los campos de contraseña
                    VerticalLayout layoutContrasena = new VerticalLayout();
                    layoutContrasena.setSpacing(true);
                    layoutContrasena.setDefaultHorizontalComponentAlignment(Alignment.CENTER); // Centra los componentes horizontalmente

                    PasswordField contrasenaActual2Field = new PasswordField("Contraseña actual*");
                    contrasenaActual2Field.setWidth("300px"); // Ajusta el ancho según tus necesidades

                    PasswordField nuevaContrasena2Field = new PasswordField("Nueva contraseña*");
                    nuevaContrasena2Field.setWidth("300px"); // Ajusta el ancho según tus necesidades

                    PasswordField confirmarContrasena2Field = new PasswordField("Confirmar contraseña*");
                    confirmarContrasena2Field.setWidth("300px"); // Ajusta el ancho según tus necesidades


                    Button guardarButton = new Button("Guardar");
                    guardarButton.getStyle().set("background-color", "green"); // Establece el color de fondo a verde
                    guardarButton.getStyle().set("color", "white"); // Establece el color del texto a blanco

                    Button cancelarButton = new Button("Cancelar", e -> dialog.close());
                    cancelarButton.getStyle().set("color", "black");

                    HorizontalLayout buttonLayout = new HorizontalLayout();
                    buttonLayout.setWidthFull(); // Opcionalmente, puedes hacer que el layout use todo el ancho disponible
                    buttonLayout.setJustifyContentMode(JustifyContentMode.CENTER); // Centra los botones en el diálogo
                    buttonLayout.add(cancelarButton, guardarButton);

                    guardarButton.addClickListener(e -> {
                        String correoActual2 = correoTextField2.getValue();
                        String contrasenaActual2 = contrasenaActual2Field.getValue();

                        System.out.println("Contraseña actual: "+ userService.validarContrasenaEntidad(correoActual2,contrasenaActual2));
                        try {
                            // Verificar que la contraseña actual coincide con la almacenada en la base de datos
                            boolean contrasenaActualCorrecta2 = userService.validarContrasenaEntidad(correoActual2, contrasenaActual2);

                            if (!contrasenaActualCorrecta2) {
                                Notification.show("La contraseña actual no es correcta").addThemeVariants(NotificationVariant.LUMO_ERROR);
                                return;
                            }

                            // Continuar con la lógica de validación y actualización de la contraseña
                            String nuevaContrasena2 = nuevaContrasena2Field.getValue();
                            String confirmarContrasena2 = confirmarContrasena2Field.getValue();
                            String passwordPattern = "^(?=.*[0-9])(?=.*[a-zA-Z]).{8,}$"; // Patrón para requerir al menos 8 caracteres alfanuméricos

                            if (!nuevaContrasena2.matches(passwordPattern)) {
                                Notification.show("La contraseña debe tener al menos 8 caracteres y contener números y letras.").addThemeVariants(NotificationVariant.LUMO_ERROR);
                                return; // No proceder con el guardado si la nueva contraseña no cumple con los requisitos
                            }

                            if (!nuevaContrasena2.equals(confirmarContrasena2)) {
                                Notification.show("Las contraseñas no coinciden").addThemeVariants(NotificationVariant.LUMO_ERROR);
                                return;
                            }

                            // Actualizar la contraseña de la entidad
                            entidad.setContraseña(nuevaContrasena2);
                            Entidad entidadActualizada = userService.PutCambiarContrasenaEntidad(entidad);

                            if (entidadActualizada != null) {
                                dialog.close();
                                Notification.show("Contraseña actualizada correctamente").addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                            } else {
                                throw new Exception("Error al actualizar la contraseña");
                            }
                        } catch (Exception ex) {
                            Notification.show("Error al procesar la solicitud: " + ex.getMessage()).addThemeVariants(NotificationVariant.LUMO_ERROR);
                        }
                    });



                    // Agregar los componentes al VerticalLayout
                    layoutContrasena.add(contrasenaActual2Field, nuevaContrasena2Field, confirmarContrasena2Field, buttonLayout);

                    // Agregar el VerticalLayout al diálogo
                    dialog.add(titulo, texto2, texto3, layoutContrasena);
                    dialog.open();


                });

                Button cerrarSesionButton2 = new Button("Cerrar Sesión", e -> {
                    VaadinSession.getCurrent().close();
                    UI.getCurrent().navigate(LoginView.class);

                });
                UI.getCurrent().navigate("");

                VerticalLayout infoDatos = new VerticalLayout();
                infoDatos.add(userInfoLayout3, userInfoLayout4);

                VerticalLayout infoMapa = new VerticalLayout();
                infoMapa.add(mapaDiv);


                HorizontalLayout mapaInfo = new HorizontalLayout();
                mapaInfo.add(infoDatos, infoMapa);

                // Crear un diseño vertical para los botones
                VerticalLayout buttonLayout = new VerticalLayout(cambiarContrasenaButton2, cerrarSesionButton2);
                buttonLayout.setSpacing(true);
                buttonLayout.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);

                layout2.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
                layout2.setSizeFull();

                layout2.add(title, texto, mapaInfo,buttonLayout);

                add(layout2);

            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Usuario obtenerUsuario(String correo) {
        Usuario usuario = new Usuario();
        usuario.setCorreo_Electronico(correo);

        return usuario;
    }

    private Entidad obtenerEntidad(String correo) {

        Entidad entidad = new Entidad();
        entidad.setCorreo(correo);

        return entidad;
    }

    private Entidad obtenerDireccion(String correo) {

        Entidad entidad = new Entidad();
        entidad.setCorreo(correo);

        return entidad;
    }
    public void setAddress(String address) {

        getElement().executeJs("updateAddress($0)", address);
    }
    private void mostrarMapa(Div mapaDiv, String direccion) {
        if (direccion.isEmpty()) {
            Notification.show("Por favor, ingresa una dirección", 3000, Notification.Position.TOP_CENTER)
                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
            return;
        }

        String url = "https://www.google.com/maps/embed/v1/place?key=AIzaSyA3cmdUmg2yRFTN9Wu5RZBaIDbmYtgnYq8&q=" + direccion;
        Div mapDiv = new Div();
        mapDiv.getElement().setProperty("innerHTML", "<iframe width=\"400\" height=\"300\" frameborder=\"0\" style=\"border:0\" src=\"" + url + "\" allowfullscreen></iframe>");

        mapaDiv.removeAll();
        mapaDiv.add(mapDiv);
    }
}
