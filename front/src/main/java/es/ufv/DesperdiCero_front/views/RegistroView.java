package es.ufv.DesperdiCero_front.views;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import es.ufv.DesperdiCero_front.UserService;
import es.ufv.DesperdiCero_front.UserService;
import es.ufv.DesperdiCero_front.models.Entidad;
import es.ufv.ProyectosII.DesperdiCero.back.dataAccess.EntidadesBBDD;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;

@Route("/registro")
public class RegistroView extends VerticalLayout {

    public RegistroView(@Autowired UserService service) throws Exception {
        Image logo = new Image("icons/logo.png", "Logo");
        logo.setWidth("200px");

        // Título
        H2 title = new H2("Nueva cuenta empresarial");

        FormLayout formLayout = new FormLayout();

        //------------------------------------------------ Campos -----------------------------------------------------
        VerticalLayout columna = new VerticalLayout();
        HorizontalLayout row1 = new HorizontalLayout();

        TextField nombreField = new TextField("Nombre de la entidad");
        nombreField.setWidth("515px");
        row1.add(nombreField);

        HorizontalLayout row10 = new HorizontalLayout();
        TextField telefonoField = new TextField("Teléfono");
        telefonoField.setWidth("250px");
        telefonoField.getElement().executeJs(
                "function restrictToNumbersAndBackspace(e) {\n" +
                        "    if (!(e.key >= '0' && e.key <= '9') && e.key !== 'Backspace') {\n" +
                        "        e.preventDefault();\n" +
                        "    }\n" +
                        "}\n" +
                        "this.addEventListener('keydown', restrictToNumbersAndBackspace);"
        );
        telefonoField.setMaxLength(9);
        row10.add(telefonoField);

        TextField direccionField = new TextField("Dirección");
        direccionField.setWidth("250px");
        row10.add(direccionField);

        HorizontalLayout row2 = new HorizontalLayout();
        TextField nifEntidadField = new TextField("NIF de la entidad");
        nifEntidadField.setWidth("250px");
        row2.add(nifEntidadField);

        ComboBox<String> tipoEntidad = new ComboBox<>("Tipo de entidad");
        tipoEntidad.setItems("Supermercado", "Organización Benéfica", "Granja");
        tipoEntidad.setWidth("250px");
        row2.add(tipoEntidad);

        H4 textoCredenciales = new H4("Información del administrador de la entidad");
        textoCredenciales.getStyle().set("margin-bottom", "-15px");
        Paragraph descripcionAdmin = new Paragraph("El administrador de la entidad podrá iniciar sesión con estas credenciales.");


        HorizontalLayout row3 = new HorizontalLayout();
        PasswordField contraseñaField = new PasswordField("Contraseña");
        contraseñaField.setWidth("250px");
        row3.add(contraseñaField);

        PasswordField confirmarContraseñaField = new PasswordField("Confirmar contraseña");
        confirmarContraseñaField.setWidth("250px");
        row3.add(confirmarContraseñaField);


        HorizontalLayout row4 = new HorizontalLayout();
        EmailField correoField = new EmailField("Correo electrónico");
        correoField.setWidth("515px");
        row4.add(correoField);

        HorizontalLayout botonRegistrar = new HorizontalLayout();
        Button registrarButton = new Button("Crear cuenta", event -> {

            // Verificar que todos los campos estén rellenados
            if (nombreField.isEmpty() || telefonoField.isEmpty() || correoField.isEmpty() ||
                    tipoEntidad.isEmpty() || contraseñaField.isEmpty() || confirmarContraseñaField.isEmpty() ||
                    nifEntidadField.isEmpty() || direccionField.isEmpty()) {
                Notification notification = Notification.show("Por favor, complete todos los campos");
                notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                return;
            }

            // Verificar si las contraseñas coinciden
            if (!contraseñaField.getValue().equals(confirmarContraseñaField.getValue())) {
                Notification notification = Notification.show("Las contraseñas no coinciden");
                notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                return;
            }

            if (EntidadesBBDD.entidadExistente(nifEntidadField.getValue())) {
                Notification notification = Notification.show("La entidad con NIF " + nifEntidadField.getValue() + " ya existe en la base de datos");
                notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                return;
            }

            Entidad entidadNueva = new Entidad();
            entidadNueva.setNombre(String.valueOf(nombreField.getValue()));
            entidadNueva.setTelefono(String.valueOf(telefonoField.getValue()));
            entidadNueva.setCorreo(String.valueOf(correoField.getValue()));
            entidadNueva.setContraseña(String.valueOf(contraseñaField.getValue()));
            entidadNueva.setTipo(String.valueOf(tipoEntidad.getValue()));
            entidadNueva.setNif(String.valueOf((nifEntidadField.getValue())));
            entidadNueva.setDireccion(String.valueOf(direccionField.getValue()));

            // Establecer la fecha de alta como la fecha actual
            entidadNueva.setFechaAlta(String.valueOf(LocalDate.now()));

            try {
                service.PutListaDatosEntidades(entidadNueva);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            Notification.show("Entidad creada correctamente");

            UI currentUI = UI.getCurrent();

            currentUI.navigate("");

        });
        registrarButton.setWidth("350px");
        registrarButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        Button yaTengoUnaCuenta = new Button("Tengo una cuenta", buttonClickEvent -> {
            UI.getCurrent().navigate(LoginView.class);
        });

        botonRegistrar.add(registrarButton);
        columna.add(row1, row10, row2, textoCredenciales,descripcionAdmin, row4, row3);
        formLayout.add(columna);


        // Centrar verticalmente la imagen y el formulario
        VerticalLayout centeredContent = new VerticalLayout();
        centeredContent.setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        centeredContent.setHorizontalComponentAlignment(Alignment.CENTER, logo, title, formLayout);
        centeredContent.add(logo, title, formLayout, botonRegistrar, yaTengoUnaCuenta);

        setSizeFull();
        setAlignItems(Alignment.CENTER);
        add(centeredContent);

    }
}