package es.ufv.DesperdiCero_front.views;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import es.ufv.DesperdiCero_front.UserService;
import es.ufv.DesperdiCero_front.models.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.HttpClientErrorException;

@Route("")
public class LoginView extends VerticalLayout {
    public LoginView(@Autowired UserService userService) {
        Image logo = new Image("icons/logo.png", "Logo");
        logo.setWidth("250px");
        logo.getStyle().set("margin-bottom", "-35px"); // Puedes ajustar el valor según tus necesidades

        LoginI18n i18n = LoginI18n.createDefault();
        LoginI18n.Form i18nForm = i18n.getForm();
        i18nForm.setTitle("Inicio de sesión");
        i18nForm.setUsername("Usuario");
        i18nForm.setPassword("Contraseña");
        i18nForm.setSubmit("Iniciar sesión");
        i18nForm.setForgotPassword("¿Has olvidado la contraseña?");
        i18n.setAdditionalInformation("Contacta con el administrador de la entidad a la que perteneces en el caso de que no puedas acceder.");
        i18n.setForm(i18nForm);

        LoginI18n.ErrorMessage i18nErrorMessage = i18n.getErrorMessage();
        i18nErrorMessage.setTitle("Error al validar las credenciales");
        i18nErrorMessage.setMessage("Es posible que el usuario o la contraseña introducidos no sean correctos.");
        i18n.setErrorMessage(i18nErrorMessage);

        LoginForm loginForm = new LoginForm();
        loginForm.setI18n(i18n);
        loginForm.getStyle().set("margin-bottom", "-30px"); // Puedes ajustar el valor según tus necesidades

        loginForm.addForgotPasswordListener(event -> {
// Crear un diálogo para recuperar contraseña
            Dialog forgotPasswordDialog = new Dialog();
            forgotPasswordDialog.setWidth("500px");

// Título del diálogo
            H2 dialogTitle = new H2("¿Has olvidado tu contraseña?");
            dialogTitle.getStyle().set("margin-bottom", "-5px"); // Puedes ajustar el valor según tus necesidades
            Paragraph descripcion = new Paragraph("Te enviaremos una nueva clave generada automáticamente. Podrás iniciar sesión con ella y cambiarla en la pestaña de perfil.");
            descripcion.setWidth("360px"); // Establecer el ancho del párrafo

// Campos del diálogo
            TextField emailField = new TextField("Correo electrónico");
            emailField.setWidth("360px");
            emailField.setPlaceholder("Introduce tu correo electrónico");

// Botones del diálogo
            Button cancelButton = new Button("Cancelar", e -> forgotPasswordDialog.close());
            Button sendButton = new Button("Enviar", e -> {
                String correoElectronico = emailField.getValue();

                try {
                    // Verificar si el correo electrónico existe en la base de datos
                    boolean correoExiste = userService.validarCorreoExistente(correoElectronico);

                    if (correoExiste) {
                        Notification.show("Se ha enviado un correo de recuperación de contraseña al correo electrónico proporcionado.");
                        forgotPasswordDialog.close();
                    } else {
                        Notification.show("Error al enviar el correo de recuperación de contraseña.").addThemeVariants(NotificationVariant.LUMO_ERROR);
                    }

                } catch (Exception ex) {
                    // Manejar cualquier excepción que pueda ocurrir durante la validación del correo electrónico
                    Notification.show("Ocurrió un error al validar el correo electrónico.").addThemeVariants(NotificationVariant.LUMO_ERROR);
                    ex.printStackTrace();
                }

            });
            sendButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            sendButton.setWidth("250px");

// Diseño del diálogo
            VerticalLayout dialogLayout = new VerticalLayout(dialogTitle, descripcion, emailField, new HorizontalLayout(cancelButton, sendButton));
            dialogLayout.getStyle().set("margin", "auto"); // Centrar el contenido
            dialogLayout.setAlignItems(FlexComponent.Alignment.CENTER);
            dialogLayout.setHorizontalComponentAlignment(FlexComponent.Alignment.CENTER, descripcion); // Centrar el párrafo
            forgotPasswordDialog.add(dialogLayout);

// Mostrar el diálogo
            forgotPasswordDialog.open();


        });


        // Agrega un listener para manejar el evento de inicio de sesión
        loginForm.addLoginListener(event -> {
            // Obtiene el usuario y la contraseña ingresados por el usuario
            String username = event.getUsername();
            String password = event.getPassword();

            // Crea un objeto Usuario y guarda el usuario y la contraseña en él
            Usuario usuario = new Usuario();
            usuario.setCorreo_Electronico(username);
            usuario.setContraseña(password);

            try {
                Usuario usuarioAutenticado = userService.validarCredenciales(usuario);
                if (usuarioAutenticado != null) {
                    System.out.println("Se ha iniciado sesión correctamente");

                    // Guarda el correo electrónico del usuario en la sesión de Vaadin
                    VaadinSession.getCurrent().setAttribute("usuarioCorreo", usuarioAutenticado.getCorreo_Electronico());
                    UI.getCurrent().navigate(MainView.class);
                } else {
                    loginForm.setError(true); // Muestra el mensaje de error directamente en el formulario
                }
            } catch (HttpClientErrorException.Unauthorized e) {
                loginForm.setError(true);
            } catch (Exception e) {
                Notification.show("Error durante el proceso de inicio de sesión.").addThemeVariants(NotificationVariant.LUMO_ERROR);
            }
        });
        Button botonCrearUsuario = new Button("Crear una cuenta empresarial");
        botonCrearUsuario.addThemeVariants(ButtonVariant.LUMO_SMALL);
        botonCrearUsuario.addClickListener(event -> {
            UI.getCurrent().navigate(RegistroView.class);
        });

        // Establece el fondo blanco y centra el contenido vertical y horizontalmente
        setClassName("login-view");
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        setSizeFull(); // Ocupa toda la altura disponible

        add(logo, loginForm, botonCrearUsuario);
    }
}
