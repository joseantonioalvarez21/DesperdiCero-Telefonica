package es.ufv.DesperdiCero_front.views;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

@Route("/errorAcceso")
public class ErrorView extends VerticalLayout {
    public ErrorView (){
        Image logo = new Image("icons/logo.png", "Logo");
        logo.setWidth("250px");

        // Crear los elementos de texto
        H1 titulo1Inicio = new H1("Error 403 Forbidden");
        titulo1Inicio.getStyle().set("color", "red");
        H3 titulo2Inicio = new H3("No se ha podido acceder a la página solicitada");
        Paragraph parrafo = new Paragraph("Puede ser que no tengas acceso o haya surgido algún error desconocido al hacer la petición.");

        // Crear contenedores para centrar los elementos
        VerticalLayout contenido = new VerticalLayout();
        contenido.setAlignItems(FlexComponent.Alignment.CENTER);
        contenido.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        contenido.setWidth("100%");
        contenido.setHeight("90vh");

        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        horizontalLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        horizontalLayout.setWidth("100%");
        horizontalLayout.setHeight("90vh");

        // Agregar los elementos al contenedor
        contenido.add(logo, titulo1Inicio, titulo2Inicio, parrafo);

        // Agregar el contenedor al contenedor horizontal
        horizontalLayout.add(contenido);

        // Establecer el diseño principal como un contenedor flexible
        setSizeFull();

        // Agregar el contenedor horizontal al diseño principal
        add(horizontalLayout);
    }
}
