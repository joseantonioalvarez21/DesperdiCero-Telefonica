package es.ufv.DesperdiCero_front.views;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.server.VaadinSession;
import es.ufv.DesperdiCero_front.UserService;
import es.ufv.DesperdiCero_front.models.Producto;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;


@Route("/panelControl")
public class MainView extends VerticalLayout {
    public MainView(@Autowired UserService service) throws Exception {
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
        //-----------------------------------------------------------------------------------------------------------
        try{
            MenuBar menuBar = new MenuBar(service);
            add(menuBar);

            VerticalLayout contentTab1 = new VerticalLayout();

            H1 titulo1Inicio = new H1("Tablero principal");
            Paragraph parrafo = new Paragraph("Puedes consultar, modificar, añadir y eliminar los alimentos publicados por la entidades");
            contentTab1.add(titulo1Inicio, parrafo);

            // Agregar leyenda de colores a la derecha de los textos
            Span leyenda = new Span("**Leyenda de colores: ");
            leyenda.getStyle().set("font-size", "small");

            Span buenEstado = new Span("Buen estado");
            buenEstado.getStyle().set("color", "green");
            leyenda.add(buenEstado);

            Span conDefectos = new Span("Con defectos");
            conDefectos.getStyle().set("color", "orange");
            leyenda.add(new Text(" | ")); // Separador
            leyenda.add(conDefectos);

            Span malo = new Span("Malo");
            malo.getStyle().set("color", "red");
            leyenda.add(new Text(" | ")); // Separador
            leyenda.add(malo);

            Div textWithLegendLayout = new Div(leyenda);
            textWithLegendLayout.getStyle().set("display", "flex");
            textWithLegendLayout.getStyle().set("flexDirection", "column");


            //Tabla para ver los alimentos

            Grid<Producto> grid = new Grid<>(Producto.class);
            grid.setColumns("nif_entidad","codigo_barras", "nombre", "tipo", "fecha_caducidad", "estado", "cantidad");

            // Crear un FlexLayout para contener los productos
            HorizontalLayout productosLayout = new HorizontalLayout();
            productosLayout.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.STRETCH);



            TextField buscarField = new TextField("Filtrar por nombre");
            buscarField.setWidth("300px");

            // Botón para realizar la búsqueda
            Button buscarButton = new Button("Buscar", event -> {
                String nombreBuscado = buscarField.getValue();
                if (nombreBuscado != null && !nombreBuscado.isEmpty()) {
                    // Filtrar productos por nombre
                    List<Producto> productosFiltrados = null;
                    try {
                        productosFiltrados = service.buscarProductosPorNombre(nombreBuscado);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }

                    // Limpiar el contenedor antes de agregar los productos filtrados
                    productosLayout.removeAll();

                    int productosPorFila = 5;
                    int contadorProductosEnFila = 0;
                    HorizontalLayout filaActual = new HorizontalLayout();


                    // Agregar los nuevos elementos Div al contenedor
                    for (Producto producto : productosFiltrados) {
                        Div productoBox = crearProductoBox(producto, service);

                        // Agregar el producto al contenedor de la fila actual
                        filaActual.add(productoBox);

                        // Incrementar el contador de productos en la fila
                        contadorProductosEnFila++;

                        // Verificar si se alcanzó el límite de productos por fila
                        if (contadorProductosEnFila == productosPorFila) {
                            // Agregar la fila actual al contenedor principal
                            productosLayout.add(filaActual);

                            // Reiniciar el contador y crear un nuevo contenedor para la siguiente fila
                            contadorProductosEnFila = 0;
                            filaActual = new HorizontalLayout();
                        }
                    }
                    // Verificar si hay productos restantes en la fila actual
                    if (contadorProductosEnFila > 0) {
                        // Agregar la fila actual al contenedor principal si hay productos restantes
                        productosLayout.add(filaActual);
                    }
                } else {
                    // Si el campo de búsqueda está vacío, mostrar todos los productos
                    productosLayout.removeAll();
                }
            });


            //-----------------Filtro para mostrar el desplegable

            /*Select<String> filtroSelect = new Select<>();
            filtroSelect.setItems("Supermercado", "Estado", "Cantidad","Fecha de caducidad","Tipo de alimento");
            filtroSelect.setPlaceholder("Selecciona un filtro");

            filtroSelect.addValueChangeListener(event -> {
                String filtroSeleccionado = event.getValue();

                try {
                    List<Producto> productos = service.getDatos(); // Obtén todos los productos

                    // Aplicar filtros según el tipo de entidad
                    if ("Organización Benéfica".equals(tipoEntidad)) {
                        productos = productos.stream()
                                .filter(p -> "Bueno".equals(p.getEstado()) || "Con defectos".equals(p.getEstado()))
                                .collect(Collectors.toList());
                    } else if ("Granja".equals(tipoEntidad)) {
                        productos = productos.stream()
                                .filter(p -> "Malo".equals(p.getEstado()))
                                .collect(Collectors.toList());
                    } else if ("Supermercado".equals(tipoEntidad)) {
                        String nifValidar1 = null;
                        try {
                            nifValidar1 = service.obtenerNifPorCorreo(correoUsuario);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                        final String finalNifValidar = nifValidar1;
                        productos = productos.stream()
                                .filter(p -> p.getNif_entidad().equals(finalNifValidar))
                                .collect(Collectors.toList());
                    }

                    // Aplicar filtro según la selección del usuario
                    switch (filtroSeleccionado) {
                        case "Supermercado":
                            productos.sort(Comparator.comparing(Producto::getNif_entidad));
                            break;
                        case "Estado":
                            productos.sort(Comparator.comparing(Producto::getEstado));
                            break;
                        case "Cantidad":
                            productos.sort(Comparator.comparing(Producto::getCantidad));
                            break;
                        case "Fecha de caducidad":
                            productos.sort(Comparator.comparing(Producto::getFecha_caducidad));
                            break;
                        case "Tipo de alimento":
                            productos.sort(Comparator.comparing(Producto::getTipo));
                            break;
                        default:
                            // No se realiza ninguna acción para otros casos
                            break;
                    }
                    productosLayout.removeAll();
                    int productosPorFila = 5;
                    int contadorProductosEnFila = 0;
                    int contadorFilas = 1;

                    // Crear un contenedor principal para todas las filas
                    VerticalLayout contenedorFilas = new VerticalLayout();

                    HorizontalLayout filaActual = new HorizontalLayout();
                    filaActual.setId("fila" + contadorFilas);

                    for (Producto producto : productos) {
                        Div productoBox = crearProductoBox(producto, service);

                        filaActual.add(productoBox);
                        contadorProductosEnFila++;

                        if (contadorProductosEnFila == productosPorFila) {
                            // Agregar la fila actual al contenedor de filas
                            contenedorFilas.add(filaActual);
                            contadorProductosEnFila = 0;
                            contadorFilas++;

                            // Crear un nuevo HorizontalLayout con un nombre único para cada fila
                            filaActual = new HorizontalLayout();
                            filaActual.setId("fila" + contadorFilas);
                        }
                    }

                    // Verificar si hay productos restantes en la fila actual
                    if (contadorProductosEnFila > 0) {
                        // Agregar la última fila al contenedor de filas si hay productos restantes
                        contenedorFilas.add(filaActual);
                    }

                    // Agregar el contenedor de filas al diseño principal
                    productosLayout.add(contenedorFilas);

                    System.out.println("Cantidad total de productos: " + productos.size());

                } catch (Exception e) {
                    // Manejar cualquier excepción que pueda ocurrir al obtener o filtrar los datos
                    Notification.show("Error al obtener los productos: " + e.getMessage());
                }

                // Resto del código para mostrar los productos en filas de hasta 5 elementos


            });*/






            // Obtener los datos de los productos del servicio
            try {
                List<Producto> productos = service.getDatos(); // Obtén todos los productos
                if ("Organización Benéfica".equals(tipoEntidad)) {
                    // Filtra para obtener solo productos 'bueno' o 'con defectos' si el usuario pertenece a una organización benéfica
                    productos = productos.stream()
                            .filter(p -> "Bueno".equals(p.getEstado()) || "Con defectos".equals(p.getEstado()))
                            .collect(Collectors.toList());
                } else if ("Granja".equals(tipoEntidad)) {
                    // Filtra para obtener solo productos 'bueno' o 'con defectos' si el usuario pertenece a una organización benéfica
                    productos = productos.stream()
                            .filter(p -> "Malo".equals(p.getEstado()))
                            .collect(Collectors.toList());
                } else if ("Supermercado".equals(tipoEntidad)) {
                    nifValidar = null;
                    try {
                        nifValidar = service.obtenerNifPorCorreo(correoUsuario);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    String finalNifValidar = nifValidar;
                    productos = productos.stream()
                            .filter(p -> p.getNif_entidad().equals(finalNifValidar))
                            .collect(Collectors.toList());
                }

                int productosPorFila = 5;
                int contadorProductosEnFila = 0;
                int contadorFilas = 1;

                // Crear un contenedor principal para todas las filas
                VerticalLayout contenedorFilas = new VerticalLayout();

                HorizontalLayout filaActual = new HorizontalLayout();
                filaActual.setId("fila" + contadorFilas);

                for (Producto producto : productos) {
                    Div productoBox = crearProductoBox(producto, service);

                    filaActual.add(productoBox);
                    contadorProductosEnFila++;

                    if (contadorProductosEnFila == productosPorFila) {
                        // Agregar la fila actual al contenedor de filas
                        contenedorFilas.add(filaActual);
                        contadorProductosEnFila = 0;
                        contadorFilas++;

                        // Crear un nuevo HorizontalLayout con un nombre único para cada fila
                        filaActual = new HorizontalLayout();
                        filaActual.setId("fila" + contadorFilas);
                    }
                }

                // Verificar si hay productos restantes en la fila actual
                if (contadorProductosEnFila > 0) {
                    // Agregar la última fila al contenedor de filas si hay productos restantes
                    contenedorFilas.add(filaActual);
                }

                // Agregar el contenedor de filas al diseño principal
                productosLayout.add(contenedorFilas);

                System.out.println("Cantidad total de productos: " + productos.size());

            } catch (Exception e) {
                // Manejar cualquier excepción que pueda ocurrir al obtener o filtrar los datos
                Notification.show("Error al obtener los productos: " + e.getMessage());
            }

//-------------------------------------------------------------------------------------------------
            // Manejador de evento de doble clic
            if (!"Granja".equals(tipoEntidad) && !"Organización Benéfica".equals(tipoEntidad)) {
                //si un supermercado
                grid.addItemDoubleClickListener(event -> {
                    Producto selectedProducto = event.getItem(); // Aquí obtienes el producto seleccionado
                    Dialog dialog = new Dialog();
                    dialog.setWidth("450px");
                    dialog.add(new H2("Editar producto"));
                    dialog.add(new Paragraph("Edita un nuevo producto al tablero principal de la entidad."));

                    dialog.add(new H3("Información de la entidad"));

                    String nif = null;
                    try {
                        nif = service.obtenerNifPorCorreo(correoUsuario);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    String nombreDeLaEntidad = null;
                    try {
                        nombreDeLaEntidad = service.obtenerNombreEntidadPorNif(nif);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    TextField nombre_entidad = new TextField();
                    nombre_entidad.setReadOnly(true);
                    nombre_entidad.setLabel("Nombre");
                    nombre_entidad.setValue(nombreDeLaEntidad);


                    TextField nif_entidad = new TextField();
                    nif_entidad.setReadOnly(true);
                    nif_entidad.setLabel("NIF");
                    nif_entidad.setValue(selectedProducto.getNif_entidad());
                    HorizontalLayout horizontalLayout0 = new HorizontalLayout(nombre_entidad, nif_entidad);
                    dialog.add(horizontalLayout0);

                    dialog.add(new H3("Información del producto"));

                    TextField codigoBarrasField = new TextField("Código de Barras");
                    codigoBarrasField.setValue(selectedProducto.getCodigo_barras());


                    TextField nombreField = new TextField("Nombre del producto");
                    nombreField.setValue(selectedProducto.getNombre()); // Corrección aquí para asignar correctamente el valor


                    HorizontalLayout camposLayout = new HorizontalLayout(codigoBarrasField, nombreField);
                    dialog.add(camposLayout);

                    DatePicker fechaPicker = new DatePicker("Fecha");
                    fechaPicker.setValue(LocalDate.parse(selectedProducto.getFecha_caducidad()));


                    IntegerField cantidadField = new IntegerField("Cantidad");
                    cantidadField.setValue(selectedProducto.getCantidad());
                    cantidadField.setStep(1); // Establece el paso de incremento/decremento a 1
                    cantidadField.setMin(1); // Opcional: Establece el valor mínimo permitido
                    cantidadField.setStepButtonsVisible(true);
                    add(cantidadField);

                    HorizontalLayout layaout2 = new HorizontalLayout(fechaPicker, cantidadField);
                    dialog.add(layaout2);

                    Select<String> tipoField = new Select<>();
                    tipoField.setLabel("Tipo");
                    tipoField.setItems("Frutas", "Verduras", "Carnes", "Pescados", "Lácteos", "Cereales y derivados", "Frutos secos y semillas", "Legumbres", "Huevos", "Bebidas", "Aceites y grasas", "Dulces y postres");
                    tipoField.setValue(selectedProducto.getTipo());

                    Select<String> estadoField = new Select<>();
                    estadoField.setLabel("Estado");
                    estadoField.setItems("Bueno", "Con defectos", "Malo");
                    estadoField.setValue(selectedProducto.getEstado());

                    HorizontalLayout layout3 = new HorizontalLayout(tipoField, estadoField);
                    dialog.add(layout3);

                    Button deleteButton = new Button("Eliminar ", e -> {
                        if (selectedProducto != null && selectedProducto.getId_productoDisp() > 0) {
                            try {
                                // Llamar al método de servicio para eliminar el producto
                                service.eliminarProducto(selectedProducto.getId_productoDisp());
                                List<Producto> productos = service.getDatos();
                                if("Supermercado".equals(tipoEntidad)){
                                    //String nifValidar = service.obtenerNifPorCorreo(correoUsuario); // Obtiene el NIF sin necesidad de manejar excepciones aquí
                                    String nifValidar2 = null;

                                    nifValidar2 = service.obtenerNifPorCorreo(correoUsuario);

                                    String finalNifValidar = nifValidar2;
                                    // Suponiendo que existe un método para obtener todos los productos relacionados con el usuario o la entidad.
                                    productos = productos.stream()
                                            .filter(p -> p.getNif_entidad().equals(finalNifValidar))
                                            .collect(Collectors.toList());
                                }

                                // Configura los productos filtrados en el grid
                                grid.setItems(productos);
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
                    deleteButton.getStyle().set("margin-right", "auto");

                    Button saveButton = new Button("Guardar", e -> {

                        selectedProducto.setCodigo_barras(String.valueOf(codigoBarrasField.getValue()));
                        selectedProducto.setNombre(String.valueOf(nombreField.getValue()));
                        selectedProducto.setTipo(String.valueOf(tipoField.getValue()));

                        String fechaOriginal = String.valueOf(fechaPicker.getValue());

                        LocalDate fecha = LocalDate.parse(fechaOriginal);
                        DateTimeFormatter formatoDeseado = DateTimeFormatter.ofPattern("yy-MM-dd");
                        String fechaFormateada = fecha.format(formatoDeseado);

                        selectedProducto.setFecha_caducidad(fechaFormateada);
                        selectedProducto.setFecha_caducidad(String.valueOf(fechaPicker.getValue()));
                        selectedProducto.setEstado(String.valueOf(estadoField.getValue()));
                        selectedProducto.setCantidad(cantidadField.getValue());
                        selectedProducto.setNif_entidad(nif_entidad.getValue());

                        // Acción 2: Actualizar el grid
                        try {
                            List<Producto> productos = service.getDatos();
                            if("Supermercado".equals(tipoEntidad)){
                                //String nifValidar = service.obtenerNifPorCorreo(correoUsuario); // Obtiene el NIF sin necesidad de manejar excepciones aquí
                                String nifValidar2 = null;

                                nifValidar2 = service.obtenerNifPorCorreo(correoUsuario);

                                String finalNifValidar = nifValidar2;
                                // Suponiendo que existe un método para obtener todos los productos relacionados con el usuario o la entidad.
                                productos = productos.stream()
                                        .filter(p -> p.getNif_entidad().equals(finalNifValidar))
                                        .collect(Collectors.toList());
                            }

                            // Configura los productos filtrados en el grid
                            grid.setItems(productos);

                            // Notificación de éxito
                            Notification.show("Producto actualizado correctamente.", 3000, Notification.Position.BOTTOM_START)
                                    .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                        } catch (Exception ex) {
                            // Manejo de cualquier excepción que pueda ocurrir tanto al filtrar como al actualizar
                            Notification.show("Error: " + ex.getMessage(), 5000, Notification.Position.BOTTOM_START)
                                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
                        }
                        dialog.close(); // Asumimos que este diálogo se debe cerrar independientemente del resultado


                    });
                    saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
                    saveButton.getStyle().set("background-color", "green");
                    Button closeButton = new Button("Cerrar", e -> dialog.close());
                    closeButton.getStyle().set("color", "green");

                    HorizontalLayout buttonLayout = new HorizontalLayout(deleteButton, closeButton, saveButton);
                    buttonLayout.setWidthFull(); // Asegura que el layout ocupe todo el ancho disponible
                    buttonLayout.setJustifyContentMode(JustifyContentMode.END); // Alinea los botones a la derecha

                    dialog.add(buttonLayout);
                    dialog.open();

                });
            }



            // Botón para abrir el popup
            if (correoUsuario != null && !correoUsuario.isEmpty()) {

                HorizontalLayout searchLayout = new HorizontalLayout(buscarField, buscarButton);
                //se pondria el de debajo apra el filtro:
                //                HorizontalLayout searchLayout = new HorizontalLayout(buscarField, buscarButton,filtroSelect);
                if (!"Granja".equals(tipoEntidad) && !"Organización Benéfica".equals(tipoEntidad)) {
                    Button popupButton = new Button("+");
                    popupButton.getStyle().set("background-color", "green");
                    popupButton.getStyle().set("color", "white");
                    popupButton.addClickListener(event -> {
                        Dialog popup = new Dialog();
                        VerticalLayout dialogContent = new VerticalLayout();
                        popup.setWidth("450px");

                        popup.add(new H2("Nuevo producto"));
                        popup.add(new Paragraph("Añada un nuevo producto al tablero principal de la entidad."));

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

                        popup.add(new H3("Información del producto"));

                        IntegerField codigoBarras = new IntegerField("Código de barras");


                        TextField nombre = new TextField("Nombre");
                        DatePicker fechaCaducidad = new DatePicker("Fecha de caducidad");
                        add(fechaCaducidad);

                        IntegerField cantidad = new IntegerField();
                        cantidad.setLabel("Cantidad");
                        cantidad.setMin(1);
                        cantidad.setValue(1);
                        cantidad.setStepButtonsVisible(true);
                        add(cantidad);

                        Select<String> tipoAlimento = new Select<>();
                        tipoAlimento.setLabel("Tipo de alimento");
                        tipoAlimento.setItems("Frutas", "Verduras", "Carnes", "Pescados", "Lácteos", "Cereales y derivados", "Frutos secos y semillas", "Legumbres", "Huevos", "Bebidas", "Aceites y grasas", "Dulces y postres");

                        Select<String> estado = new Select<>();
                        estado.setLabel("Estado del alimento");
                        estado.setItems("Bueno", "Con defectos", "Malo");
                        popup.add(codigoBarras, nombre, fechaCaducidad, cantidad, tipoAlimento, estado);


                        HorizontalLayout horizontalLayout1 = new HorizontalLayout(codigoBarras, nombre);
                        HorizontalLayout horizontalLayout2 = new HorizontalLayout(fechaCaducidad, cantidad);
                        HorizontalLayout horizontalLayout3 = new HorizontalLayout(tipoAlimento, estado);

                        // Botón dentro del popup
                        Button guardarButton = new Button("Añadir producto", event2 -> {
                            // Verificar si todos los campos están completados
                            if (codigoBarras.isEmpty() || nombre.isEmpty() || tipoAlimento.isEmpty() || fechaCaducidad.isEmpty()
                                    || estado.isEmpty() || cantidad.isEmpty() || nif_entidad.isEmpty()) {
                                Notification notification = Notification.show("Por favor, complete todos los campos.");
                                notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                                return; // No proceder con el guardado si algún campo está vacío
                            }

                            Producto productoNuevo = new Producto();
                            productoNuevo.setCodigo_barras(String.valueOf(codigoBarras.getValue()));
                            productoNuevo.setNombre(String.valueOf(nombre.getValue()));
                            productoNuevo.setTipo(String.valueOf(tipoAlimento.getValue()));

                            String fechaOriginal = String.valueOf(fechaCaducidad.getValue());

                            LocalDate fecha = LocalDate.parse(fechaOriginal);
                            DateTimeFormatter formatoDeseado = DateTimeFormatter.ofPattern("yy-MM-dd");
                            String fechaFormateada = fecha.format(formatoDeseado);

                            productoNuevo.setFecha_caducidad(fechaFormateada);
                            productoNuevo.setFecha_caducidad(String.valueOf(fechaCaducidad.getValue()));
                            productoNuevo.setEstado(String.valueOf(estado.getValue()));
                            productoNuevo.setCantidad(cantidad.getValue());
                            productoNuevo.setNif_entidad(nif_entidad.getValue());

                            try {
                                service.PutListaDatosProductos(productoNuevo);
                                grid.setItems(service.getDatos());
                            } catch (Exception e) {
                                Notification notification = Notification
                                        .show("El producto no se pudo añadir.");
                                notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                                throw new RuntimeException(e);
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

                        HorizontalLayout botonesLayout = new HorizontalLayout(guardarButton, cancelarButton);
                        botonesLayout.getStyle().set("padding-top", "35px");
                        botonesLayout.setSpacing(true); // Agregar espacio entre los botones (opcional)
                        popup.add(horizontalLayout1, horizontalLayout2, horizontalLayout3, botonesLayout);
                        // Abrir el popup
                        popup.open();
                    });
                    searchLayout.add(popupButton);

                }


                searchLayout.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.BASELINE); // Alinear verticalmente a la misma altura




                // Agregar el layout de búsqueda y el botón al contenido de la pestaña
                contentTab1.add(textWithLegendLayout);
                contentTab1.add(searchLayout);
                // Agregar el layout vertical al contenido de la pestaña
                contentTab1.add(productosLayout);

                // Agregar el contenidoTab1 al layout principal
                add(contentTab1);


            }
        }catch (Exception e) {
            if (e instanceof HttpClientErrorException && ((HttpClientErrorException) e).getStatusCode() == HttpStatus.NOT_FOUND) {
                // Si se captura una excepción de HttpClientErrorException.NotFound, redirige al usuario a la página de login
                UI.getCurrent().getPage().setLocation("/login");
            } else {
                // Para cualquier otra excepción, podrías querer logear el error y/o redirigir a una página de error genérica
                Notification.show("Ha ocurrido un error inesperado. Por favor, inténtalo de nuevo más tarde.");
            }
        }


    }
    private List<Producto> filtrarProductosPorSupermercado(UserService service, String correoUsuario, String tipoEntidad) throws Exception {
        List<Producto> productos = service.getDatos(); // Obtén todos los productos
        if("Supermercado".equals(tipoEntidad)) {
            String nifValidar = service.obtenerNifPorCorreo(correoUsuario); // Obtener NIF del correo del usuario
            return productos.stream()
                    .filter(p -> p.getNif_entidad().equals(nifValidar))
                    .collect(Collectors.toList());
        }
        return productos; // Retorna la lista sin filtrar si no es un supermercado
    }

    private void mostrarProductosEnLayout(List<Producto> productos, VerticalLayout layout, UserService service) {
        layout.removeAll(); // Limpiar el layout antes de agregar nuevos productos

        for (Producto producto : productos) {
            Div productoBox = crearProductoBox(producto, service); // Utiliza la función auxiliar
            layout.add(productoBox);
        }
    }


    public void mostrarInformacionCompleta(Producto producto, UserService service){
        Dialog dialog = new Dialog();
        dialog.setWidth("400px");

        // Agregar imagen según el tipo de alimento
        String tipoImagen = obtenerImagenSegunTipo(producto.getTipo());
        if (tipoImagen != null) {
            Image image = new Image(tipoImagen, "Imagen del tipo de alimento");
            image.getStyle().set("display", "block");
            image.getStyle().set("margin", "auto");
            image.setWidth("100px"); // Ajusta el ancho de la imagen según tus necesidades
            image.setHeight("100px"); // Ajusta la altura de la imagen según tus necesidades
            dialog.add(image);
        } else {
            // En caso de que la imagen no esté disponible
            dialog.add(new Span("Imagen no disponible"));
        }

        // Agregar toda la información del producto al dialog
        dialog.add(new H3(producto.getNombre()));
        dialog.add(new Paragraph("Código de Barras: " + producto.getCodigo_barras()));
        dialog.add(new Paragraph("Tipo: " + producto.getTipo()));
        dialog.add(new Paragraph("Fecha de Caducidad: " + producto.getFecha_caducidad()));
        dialog.add(new Paragraph("Estado: " + producto.getEstado()));
        dialog.add(new Paragraph("Cantidad: " + producto.getCantidad()));

        dialog.open();
    }
    private void mostrarInformacionCompleta2(Producto producto, UserService service) {
        Dialog dialog = new Dialog();
        dialog.setWidth("450px");

        // Agregar imagen según el tipo de alimento
        String tipoImagen = obtenerImagenSegunTipo(producto.getTipo());
        if (tipoImagen != null) {
            Image image = new Image(tipoImagen, "Imagen del tipo de alimento");
            image.getStyle().set("display", "block");
            image.getStyle().set("margin", "auto");
            image.setWidth("100px"); // Ajusta el ancho de la imagen según tus necesidades
            image.setHeight("100px"); // Ajusta la altura de la imagen según tus necesidades
            image.getStyle().set("margin-bottom", "45px");
            image.getStyle().set("margin-top", "25px");
            dialog.add(image);
        } else {
            // En caso de que la imagen no esté disponible
            dialog.add(new Span("Imagen no disponible"));
        }

        // Agregar toda la información del producto al dialog
        dialog.add(new H2(producto.getNombre()));
        dialog.add(new Paragraph("Puedes consultar los detalles del producto de forma detallada."));

        String nombreDeLaEntidad = null;
        try {
            nombreDeLaEntidad = service.obtenerNombreEntidadPorNif(producto.getNif_entidad());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        TextField nombre_entidad = new TextField();
        nombre_entidad.setReadOnly(true);
        nombre_entidad.setLabel("Nombre del supermercado");
        nombre_entidad.setValue(nombreDeLaEntidad);

        TextField codigoBarras = new TextField();
        codigoBarras.setReadOnly(true);
        codigoBarras.setLabel("Código de barras");
        codigoBarras.setValue(producto.getCodigo_barras());

        HorizontalLayout horizontalLayout0 = new HorizontalLayout(nombre_entidad, codigoBarras);
        horizontalLayout0.getElement().setAttribute("style", "padding-bottom: 20px;");

        Select<String> tipoProducto = new Select<>();
        tipoProducto.setReadOnly(true);
        tipoProducto.setLabel("Tipo");
        tipoProducto.setItems("Frutas", "Verduras", "Carnes", "Pescados", "Lácteos", "Cereales y derivados", "Frutos secos y semillas", "Legumbres", "Huevos", "Bebidas", "Aceites y grasas", "Dulces y postres");
        tipoProducto.setValue(producto.getTipo());

        DatePicker fechaCaducidad = new DatePicker("Fecha de caducidad");
        fechaCaducidad.setReadOnly(false);
        fechaCaducidad.setValue(LocalDate.parse(producto.getFecha_caducidad()));

        HorizontalLayout horizontalLayout1 = new HorizontalLayout(tipoProducto, fechaCaducidad);
        horizontalLayout1.getElement().setAttribute("style", "padding-bottom: 20px;");

        Select<String> estadoProducto = new Select<>();
        estadoProducto.setReadOnly(true);
        estadoProducto.setLabel("Estado");
        estadoProducto.setItems("Bueno", "Con defectos", "Malo");
        estadoProducto.setValue(producto.getEstado());

        IntegerField cantidadProducto = new IntegerField("Cantidad");
        cantidadProducto.setReadOnly(true);
        cantidadProducto.setValue(producto.getCantidad());
        cantidadProducto.setStep(1); // Establece el paso de incremento/decremento a 1
        cantidadProducto.setMin(1); // Opcional: Establece el valor mínimo permitido
        cantidadProducto.setStepButtonsVisible(true);

        HorizontalLayout horizontalLayout2 = new HorizontalLayout(estadoProducto, cantidadProducto);
        horizontalLayout2.getElement().setAttribute("style", "padding-bottom: 20px;");

        Button botonGuardar = new Button("Guardar");
        botonGuardar.getStyle().set("background-color", "green");
        botonGuardar.getStyle().set("color", "white");

        Button botonCancelar = new Button("Cancelar");
        botonCancelar.getStyle().set("color", "black");

        Button botonEliminar = new Button("Eliminar");
        botonEliminar.getStyle().set("background-color", "red");
        botonEliminar.getStyle().set("color", "white");

        HorizontalLayout horizontalLayout3 = new HorizontalLayout(botonCancelar, botonEliminar, botonGuardar);

        dialog.add(horizontalLayout0, horizontalLayout1, horizontalLayout2, horizontalLayout3);
        dialog.open();
    }



    private Div crearProductoBox(Producto productoSeleccionado, UserService service) {
        Div productoBox = new Div();
        productoBox.getStyle().set("padding", "10px");
        productoBox.getStyle().set("margin", "10px");
        productoBox.getStyle().set("width", "200px");
        productoBox.getStyle().set("overflow-x", "hidden"); // Evitar barras de desplazamiento horizontal

        productoBox.getStyle().set("border-radius", "10px");
        productoBox.getStyle().set("cursor", "pointer");
        productoBox.getStyle().set("transition", "box-shadow 0.3s");

        // Cambiar el color del borde según el estado del producto
        String borderColor;
        switch (productoSeleccionado.getEstado()) {
            case "Bueno":
                borderColor = "#008008";
                break;
            case "Con defectos":
                borderColor = "#FF9721";
                break;
            case "Malo":
                borderColor = "red";
                break;
            default:
                borderColor = "white"; // Color por defecto si el estado no coincide
        }
        productoBox.getStyle().set("border", "3px solid " + borderColor);

        String tipoImagen = obtenerImagenSegunTipo(productoSeleccionado.getTipo());
        if (tipoImagen != null) {
            Image image = new Image(tipoImagen, "Imagen del tipo de alimento");
            image.getStyle().set("margin", "auto");
            image.setWidth("50px"); // Ajusta el ancho de la imagen según tus necesidades
            image.setHeight("50px"); // Ajusta la altura de la imagen según tus necesidades
            image.getStyle().set("margin-bottom", "15px");
            image.getStyle().set("margin-top", "15px");
            productoBox.add(image);
        }

        // Agregar información simplificada del producto a la caja
        H3 productoTitle = new H3(productoSeleccionado.getNombre());
        productoBox.add(productoTitle);

        String nombreDeLaEntidad = null;
        try {
            nombreDeLaEntidad = service.obtenerNombreEntidadPorNif(productoSeleccionado.getNif_entidad());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        Paragraph entidadNombre = new Paragraph(nombreDeLaEntidad);
        entidadNombre.getStyle().set("font-style", "italic");
        entidadNombre.getStyle().set("font-weight", "bold"); // Negrita
        productoBox.add(entidadNombre);

        Paragraph fechac = new Paragraph("Fecha de caducidad: " + productoSeleccionado.getFecha_caducidad());
        fechac.getStyle().set("font-style", "italic"); // Cursiva

        productoBox.add(fechac);

        // Crear un Div para envolver la cantidad y el icono
        Div cantidadDiv = new Div();
        cantidadDiv.getStyle().set("display", "flex");
        cantidadDiv.getStyle().set("align-items", "center");

        Paragraph cantidad = new Paragraph("Cantidad: " + productoSeleccionado.getCantidad());
        cantidad.getStyle().set("font-style", "italic"); // Cursiva
        cantidad.getStyle().set("margin-right", "65px"); // Ajuste del margen derecho
        productoBox.add(cantidad);
        Button botonGuardar = new Button("Añadir a la cesta");
        botonGuardar.getStyle().set("color", "black");
        botonGuardar.setWidth("200px");

        productoBox.add(botonGuardar);

        productoBox.getElement().addEventListener("click", event -> {
            String correoUsuario = (String) VaadinSession.getCurrent().getAttribute("usuarioCorreo");
            String tipoEntidad = null;
            try {
                tipoEntidad = service.obtenerTipoEntidadPorCorreo(correoUsuario);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            if (!"Granja".equals(tipoEntidad) && !"Organización Benéfica".equals(tipoEntidad)) {
                mostrarInformacionCompleta2(productoSeleccionado, service);

            }
            else{
                mostrarInformacionCompleta(productoSeleccionado, service);
            }
            // Lógica para mostrar la información completa del producto

        });

        // Agregar el resto de la información y estilos según tus necesidades

        return productoBox;
    }

    private String obtenerImagenSegunTipo(String tipoAlimento) {
        String imagePath;
        switch (tipoAlimento) {
            case "Frutas":
                imagePath = "icons/fruta.png";
                break;
            case "Verduras":
                imagePath = "icons/verduras.png";
                break;
            case "Carnes":
                imagePath = "icons/carne.png";
                break;
            case "Pescados":
                imagePath = "icons/pescado.png";
                break;
            case "Lácteos":
                imagePath = "icons/productos-lacteos.png";
                break;
            case "Cereales y derivados":
                imagePath = "icons/cereal.png";
                break;
            case "Frutos secos y semillas":
                imagePath = "icons/tuerca.png";
                break;
            case "Legumbres":
                imagePath = "icons/chicharos.png";
                break;
            case "Huevos":
                imagePath = "icons/huevo.png";
                break;
            case "Bebidas":
                imagePath = "icons/refresco.png";
                break;
            case "Aceites y grasas":
                imagePath = "icons/aceite-de-oliva.png";
                break;
            case "Dulces y postres":
                imagePath = "icons/dulces.png";
                break;
            default:
                imagePath = null; // Imagen por defecto si el tipo no coincide
        }
        return imagePath;
    }
}