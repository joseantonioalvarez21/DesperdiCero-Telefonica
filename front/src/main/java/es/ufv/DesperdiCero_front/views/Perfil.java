package es.ufv.DesperdiCero_front.views;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import es.ufv.DesperdiCero_front.UserService;
import es.ufv.DesperdiCero_front.UserService;
import es.ufv.DesperdiCero_front.models.Usuario;
import es.ufv.ProyectosII.DesperdiCero.back.dataAccess.UsuarioBBDD;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Route("/usuariosRegistrados")

public class Perfil extends VerticalLayout {

    @Autowired
    public Perfil(UserService service) throws Exception {
        String correoUsuario = (String) VaadinSession.getCurrent().getAttribute("usuarioCorreo");
        if (correoUsuario == null || correoUsuario.isEmpty()) {
            UI.getCurrent().navigate(LoginView.class);
            return;
        }
        System.out.println(correoUsuario);
        MenuBar menuBar = new MenuBar(service);
        add(menuBar);

        try {
            String tipoEntidad = service.obtenerTipoEntidadPorCorreo(correoUsuario);
            String resultado = service.obteneEntidadPorCorreo(correoUsuario);
            String nifEntidad = service.obtenerNifPorCorreo(correoUsuario);


            if (("UsuarioEntidad".equalsIgnoreCase(resultado))) {
                // Si el tipo de entidad es "Organización Benéfica", restringe el acceso
                UI.getCurrent().navigate("http://localhost:8080/errorAcceso");
                UI.getCurrent().navigate("panelControl");
            } else {
                VerticalLayout contentTab1 = new VerticalLayout();
                H1 titulo1Inicio = new H1("Entidades y usuarios");
                Paragraph parrafo = new Paragraph("Puedes consultar los usuarios registrados en la plataforma.");
                contentTab1.add(titulo1Inicio, parrafo);

                Grid<Usuario> grid = new Grid<>();
                grid.addColumn(Usuario::getDNI).setHeader("DNI");
                grid.addColumn(Usuario::getNombre).setHeader("Nombre");
                grid.addColumn(Usuario::getApellidos).setHeader("Apellidos");
                grid.addColumn(Usuario::getCorreo_Electronico).setHeader("Correo Electrónico");
                grid.addColumn(Usuario::getContraseña).setHeader("Contraseña");
                grid.addColumn(Usuario::getNIF_Entidad).setHeader("CIF Entidad");


                // Añadir columna de botón de eliminar
                grid.addColumn(new ComponentRenderer<>(usuario -> {
                    Button eliminarButton = new Button(VaadinIcon.TRASH.create(), clickEvent -> {
                        // Aquí va la lógica para eliminar el usuario de la base de datos
                        try {
                            // Eliminar el usuario de la base de datos
                            service.eliminarUsuario(usuario.getDNI());

                            // Obtener la lista actualizada de usuarios directamente de la base de datos
                            List<Usuario> usuariosActualizados = service.getDatosUsuario();

                            // Filtrar los usuarios por la entidad del usuario que ha hecho login
                            List<Usuario> usuariosFiltrados = usuariosActualizados.stream()
                                    .filter(p -> p.getNIF_Entidad().equals(nifEntidad))
                                    .collect(Collectors.toList());

                            // Configurar los usuarios filtrados en el grid
                            grid.setItems(usuariosFiltrados);
                        } catch (Exception e) {
                            // Manejar cualquier excepción que pueda ocurrir
                            Notification.show("Error al eliminar el usuario: " + e.getMessage()).addThemeVariants(NotificationVariant.LUMO_ERROR);
                        }
                    });

                    // Personalizar el botón con el icono de basura y fondo rojo
                    eliminarButton.addThemeVariants(ButtonVariant.LUMO_ERROR, ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_CONTRAST);
                    eliminarButton.getStyle().set("background-color", "white").set("color", "red");

                    return eliminarButton;
                })).setHeader("");

                try {
                    ArrayList<Usuario> usuarios = service.getDatosUsuario();
                    usuarios = (ArrayList<Usuario>) usuarios.stream()
                            .filter(p -> p.getNIF_Entidad().equals(nifEntidad))
                            .collect(Collectors.toList());
                    grid.setItems(usuarios); // Configura los usuarios filtrados en el grid
                } catch (Exception e) {
                    // Manejar cualquier excepción que pueda ocurrir al obtener o filtrar los datos
                    Notification.show("Error al obtener los usuarios: " + e.getMessage());
                }

                //Añadir nuevo usuario
                Button popupButton = new Button("Añadir Usuario");
                popupButton.getStyle().set("background-color", "green");
                popupButton.getStyle().set("color", "white");
                popupButton.addClickListener(event -> {
                    Dialog popup = new Dialog();
                    VerticalLayout dialogContent = new VerticalLayout();
                    popup.setWidth("660px");

                    popup.add(new H2("Nuevo usuario"));
                    popup.add(new Paragraph("De de alta un nuevo trabajador en su entidad"));

                    popup.add(new H3("Información de la entidad"));

                    // Obtener NIF de la entidad a partir del correo del usuario
                    String nif = null;
                    try {
                        nif = service.obtenerNifPorCorreo(correoUsuario);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }

                    // Obtener el nombre de la entidad a partir del NIF
                    String nombreDeLaEntidad = null;
                    try {
                        nombreDeLaEntidad = service.obtenerNombreEntidadPorNif(nif);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }

                    TextField nombre_entidad = new TextField("Nombre de la Entidad");
                    nombre_entidad.setValue(nombreDeLaEntidad);
                    nombre_entidad.setReadOnly(true);

                    TextField nif_entidad = new TextField("NIF de la Entidad");
                    nif_entidad.setValue(nif);
                    nif_entidad.setReadOnly(true);

                    HorizontalLayout horizontalLayout0 = new HorizontalLayout(nombre_entidad, nif_entidad);
                    horizontalLayout0.getElement().setAttribute("style", "padding-bottom: 20px;");
                    popup.add(horizontalLayout0);


                    popup.add(new H3("Información del usuario"));

                    TextField DNI = new TextField("DNI *");
                    TextField nombre = new TextField("Nombre *");
                    TextField apellidos = new TextField("Apellidos *");
                    TextField correo = new TextField("Correo electrónico *");
                    PasswordField contraseña = new PasswordField("Contraseña *");
                    contraseña.setAllowedCharPattern("[A-Za-z0-9]");
                    contraseña.setMinLength(6);
                    contraseña.setMaxLength(12);
                    PasswordField confirmarContraseñaField = new PasswordField("Confirmar contraseña *");
                    confirmarContraseñaField.setAllowedCharPattern("[A-Za-z0-9]");
                    confirmarContraseñaField.setMinLength(6);
                    confirmarContraseñaField.setMaxLength(12);

                    popup.add(DNI, nombre, apellidos, correo, contraseña, confirmarContraseñaField);


                    HorizontalLayout horizontalLayout1 = new HorizontalLayout(DNI, nombre, apellidos);
                    HorizontalLayout horizontalLayout2 = new HorizontalLayout(correo, contraseña,confirmarContraseñaField);

                    // Botón dentro del popup
                    Button guardarButton = new Button("Añadir usuario", event2 -> {
                        // Verificar si todos los campos están completados
                        if (DNI.isEmpty() || nombre.isEmpty() || apellidos.isEmpty() || correo.isEmpty()
                                || contraseña.isEmpty() || confirmarContraseñaField.isEmpty()) {
                            Notification notification = Notification.show("Por favor, complete todos los campos.");
                            notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                            return; // No proceder con el guardado si algún campo está vacío
                        }
                        //Comprobar si el dni del usuario ya está registrado en la bbdd
                        if (UsuarioBBDD.dniExistente(DNI.getValue())) {
                            Notification notification = Notification.show("La entidad con NIF " + DNI.getValue() + " ya existe en la base de datos");
                            notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                            return;
                        }

                        //Comprueba que el dni tenga el formato que debe tener
                        String dniPattern = "^\\d{8}[A-Za-z]$";
                        if (!DNI.getValue().matches(dniPattern)) {
                            Notification notification = Notification.show("El DNI debe tener 8 dígitos y un carácter alfabético al final.");
                            notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                            return; // No proceder con el guardado si el DNI no cumple con el formato
                        }

                        //Comprueba que la contraseña tenga al menos 8 caracteres alfanuméricos
                        String passwordPattern = "^(?=.*[0-9])(?=.*[a-zA-Z]).{8,}$";
                        if (!contraseña.getValue().matches(passwordPattern)) {
                            Notification.show("La contraseña debe tener al menos 8 caracteres alfanuméricos.").addThemeVariants(NotificationVariant.LUMO_ERROR);
                            return; // No proceder con el guardado si la contraseña no cumple con los requisitos
                        }
                        //Comprueba que las contraseñas son iguales
                        if (!contraseña.getValue().equals(confirmarContraseñaField.getValue())) {
                            Notification notification = Notification.show("Las contraseñas no coinciden");
                            notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                            return;
                        }

                        Usuario usuarioNuevo = new Usuario();
                        usuarioNuevo.setDNI(String.valueOf(DNI.getValue()));
                        usuarioNuevo.setNombre(String.valueOf(nombre.getValue()));
                        usuarioNuevo.setApellidos(String.valueOf(apellidos.getValue()));
                        usuarioNuevo.setCorreo_Electronico(String.valueOf(correo.getValue()));
                        usuarioNuevo.setContraseña(String.valueOf(contraseña.getValue()));
                        usuarioNuevo.setNIF_Entidad(nif_entidad.getValue());
                        try {
                            // Añadir el usuario a la base de datos
                            service.PutListaDatosUsuarios(usuarioNuevo);
                            // Obtener y filtrar usuarios actualizados
                            List<Usuario> usuariosFiltrados = service.getDatosUsuario().stream()
                                    .filter(p -> p.getNIF_Entidad().equals(nifEntidad))
                                    .collect(Collectors.toList());

                            // Actualizar el grid
                            grid.setItems(usuariosFiltrados);
                        } catch (Exception e) {
                            Notification.show("Error al añadir el usuario: " + e.getMessage()).addThemeVariants(NotificationVariant.LUMO_ERROR);
                        }

                        Notification notification = Notification
                                .show("Producto creado correctamente.");
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

                    HorizontalLayout botonesLayout = new HorizontalLayout(cancelarButton, guardarButton);
                    botonesLayout.setWidthFull(); // Establece el ancho completo para el layout de botones
                    botonesLayout.setJustifyContentMode(JustifyContentMode.CENTER); // Centra los botones en el layout
                    botonesLayout.getStyle().set("padding-top", "35px");
                    botonesLayout.setSpacing(true); // Agrega espacio entre los botones
                    popup.add(horizontalLayout1, horizontalLayout2, botonesLayout);

                    // Abrir el popup
                    popup.open();
                });


                grid.addItemDoubleClickListener(event -> {
                    Usuario selectedProducto = event.getItem(); // Aquí obtienes el producto seleccionadoç
                    Usuario selectedUsuario=event.getItem();
                    Dialog dialog = new Dialog();
                    dialog.setWidth("450px");
                    dialog.add(new H2("Editar usuario"));
                    dialog.add(new Paragraph("Edita un usuario al tablero principal de Usuarios."));

                    dialog.add(new H3("Información de la entidad"));

                    TextField nombre_entidad = new TextField();
                    /*nombre_entidad.setReadOnly(true);
                    nombre_entidad.setLabel("Nombre");
                    nombre_entidad.setValue("Carrefour");*/

                    /*TextField nif_entidad = new TextField();
                    nif_entidad.setReadOnly(true);
                    nif_entidad.setLabel("NIF");
                    nif_entidad.setValue("71987732");
                    HorizontalLayout horizontalLayout0 = new HorizontalLayout(nombre_entidad, nif_entidad);
                    dialog.add(horizontalLayout0);*/

                    TextField CIF_Entidad = new TextField();
                    CIF_Entidad.setReadOnly(true);
                    CIF_Entidad.setValue(selectedUsuario.getNIF_Entidad());
                    //CIF_Entidad.setValue(selectedUsuario.getNIF_Entidad()); // Corrección aquí para asignar correctamente el valor
                    CIF_Entidad.setLabel("CIF Entidad");
                    HorizontalLayout horizontalLayout0 = new HorizontalLayout(CIF_Entidad);
                    dialog.add(horizontalLayout0);


                    dialog.add(new H3("Datos del usuario"));


                    TextField DNIField = new TextField();
                    DNIField.setReadOnly(true);
                    DNIField.setValue(selectedUsuario.getDNI());
                    DNIField.setLabel("DNI");
                    DNIField.setValue(selectedUsuario.getDNI());

                    /*TextField nif_entidad = new TextField();
                    nif_entidad.setReadOnly(true);
                    nif_entidad.setLabel("NIF");
                    nif_entidad.setValue("71987732");*/




                    TextField nombreField = new TextField("Nombre del Usuario");
                    nombreField.setValue(selectedUsuario.getNombre()); // Corrección aquí para asignar correctamente el valor


                    HorizontalLayout camposLayout = new HorizontalLayout(DNIField, nombreField);
                    dialog.add(camposLayout);

                    TextField ApellidosField = new TextField("Apellidos");
                    ApellidosField.setValue(selectedUsuario.getApellidos());


                    TextField correoElectronicoField = new TextField("Correo Electronico");
                    correoElectronicoField.setValue(selectedUsuario.getCorreo_Electronico()); // Corrección aquí para asignar correctamente el valor

                    HorizontalLayout camposLayout2 = new HorizontalLayout(ApellidosField,correoElectronicoField );
                    dialog.add(camposLayout2);

                    PasswordField contraseñaField = new PasswordField("Contraseña");
                    contraseñaField.setValue(selectedUsuario.getContraseña());
                    contraseñaField.setReadOnly(true);



                    PasswordField confirmarContraseñaField = new PasswordField("Confirmar contraseña");
                    confirmarContraseñaField.setValue(selectedUsuario.getContraseña()); // Corrección aquí para asignar correctamente el valor
                    confirmarContraseñaField.setReadOnly(true);

                    HorizontalLayout camposLayout3 = new HorizontalLayout(contraseñaField,confirmarContraseñaField );
                    dialog.add(camposLayout3);






                    /*Button deleteButton = new Button("Eliminar ", e -> {
                        if (selectedUsuario != null && selectedUsuario.getDNI() > 0) {
                            try {
                                // Llamar al método de servicio para eliminar el producto
                                service.eliminarProducto(selectedProducto.getId_productoDisp());
                                grid.setItems(service.getDatos());
                                Notification.show("Producto eliminado correctamente.", 3000, Notification.Position.BOTTOM_START)
                                        .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                                dialog.close();
                            } catch (Exception ex) {
                                Notification.show("Error al eliminar el producto: " + ex.getMessage(), 5000, Notification.Position.BOTTOM_START)
                                        .addThemeVariants(NotificationVariant.LUMO_ERROR);
                            }
                        } else {
                            Notification.show("Producto no seleccionado o inválido.", 3000, Notification.Position.BOTTOM_START)
                                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
                        }
                    });
                    deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
                    deleteButton.getStyle().set("margin-right", "auto");*/

                    Button saveButton = new Button("Guardar", e -> {

                        selectedUsuario.setDNI(String.valueOf(DNIField.getValue()));
                        selectedUsuario.setNombre(String.valueOf(nombreField.getValue()));
                        selectedUsuario.setApellidos(String.valueOf(ApellidosField.getValue()));

                        //selectedUsuario.setCorreo_Electronico(fechaFormateada);
                        selectedUsuario.setCorreo_Electronico(String.valueOf(correoElectronicoField.getValue()));
                        selectedUsuario.setContraseña(String.valueOf(contraseñaField.getValue()));
                        selectedUsuario.setContraseña(String.valueOf(confirmarContraseñaField.getValue()));
                        selectedUsuario.setNIF_Entidad(CIF_Entidad.getValue());

                        // Acción 2: Actualizar el grid
                        try {
                            service.PutUsuarioModificado(selectedUsuario); // Actualizar la base de datos
                            grid.getDataProvider().refreshItem(selectedUsuario);
                            Notification.show("Usuario actualizado correctamente.", 3000, Notification.Position.BOTTOM_START)
                                    .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                        } catch (Exception ex) {
                            Notification.show("Error al actualizar el Usuario: " + ex.getMessage(), 5000, Notification.Position.BOTTOM_START)
                                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
                        }
                        dialog.close();

                    });
                    saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
                    saveButton.getStyle().set("background-color", "green");
                    Button closeButton = new Button("Cerrar", e -> dialog.close());
                    closeButton.getStyle().set("color", "green");

                    HorizontalLayout buttonLayout = new HorizontalLayout( closeButton, saveButton);
                    buttonLayout.setWidthFull(); // Asegura que el layout ocupe todo el ancho disponible
                    buttonLayout.setJustifyContentMode(JustifyContentMode.END); // Alinea los botones a la derecha

                    dialog.add(buttonLayout);
                    dialog.open();

                });


                add(contentTab1,popupButton, grid);


            }
        } catch (Exception e) {
            Notification.show("Error al verificar el tipo de entidad: " + e.getMessage()).addThemeVariants(NotificationVariant.LUMO_ERROR);
            e.printStackTrace();
        }


    }
}


