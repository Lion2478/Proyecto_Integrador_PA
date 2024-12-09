package Vista.Productos;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import Controlador.Controlador;
import Vista.Interfaces.ActualizarTable;
import Vista.Plantillas.GenerarComponentes;
import Vista.Plantillas.GenerarVentanaFormulario;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public class PantallaGestionProductos extends JPanel implements ActualizarTable {

    private JTextField campoBusqueda;
    private JTable tablaProductos;
    private JButton botonAlta, botonBaja, botonModificar;

    public PantallaGestionProductos() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Margen general
        setOpaque(true);

        JPanel panelBusqueda = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panelBusqueda.setOpaque(false);

        // Agregar un JLabel para describir el campo de texto de búsqueda
        JLabel etiquetaBusqueda = new JLabel("Buscar por Nombre:");
        campoBusqueda = GenerarComponentes.crearCampoTexto(25);

        panelBusqueda.add(etiquetaBusqueda); // Añadir la etiqueta
        panelBusqueda.add(campoBusqueda);   // Añadir el cuadro de texto

        add(panelBusqueda, BorderLayout.NORTH);

        // Botones de control a la izquierda
        JPanel panelBotones = new JPanel();
        panelBotones.setLayout(new BoxLayout(panelBotones, BoxLayout.Y_AXIS));
        panelBotones.setOpaque(false);

        botonAlta = GenerarComponentes.crearBoton("Dar de Alta");
        botonBaja = GenerarComponentes.crearBoton("Dar de Baja");
        botonModificar = GenerarComponentes.crearBoton("Modificar");

        // Añadir los botones al panel de botones con separación
        panelBotones.add(botonAlta);
        panelBotones.add(Box.createVerticalStrut(10)); // Espacio entre botones
        panelBotones.add(botonBaja);
        panelBotones.add(Box.createVerticalStrut(10)); // Espacio entre botones
        panelBotones.add(botonModificar);

        botonAlta.addActionListener(e -> {
            // Acción para el botón Dar de Alta
            JFrame formularioProducto = crearFormularioProducto();
            formularioProducto.setVisible(true);
        });

        botonBaja.addActionListener(e -> {
            // Acción para el botón Dar de Baja
            int selectedRow = tablaProductos.getSelectedRow();
            if (selectedRow != -1) {
                String idProducto = (String) tablaProductos.getValueAt(selectedRow, 0);
                Controlador.eliminarDB("PRODUCTO_INVENTARIO", "ID_PRODUCTO", idProducto);
                Controlador.eliminarDB("PRODUCTOS", "ID_PRODUCTO", idProducto);
                actualizarTabla();
            } else {
                JOptionPane.showMessageDialog(this, "Seleccione un producto para dar de baja.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            }
        });

        botonModificar.addActionListener(e -> {
            int selectedRow = tablaProductos.getSelectedRow();
            if (selectedRow != -1) {
                // Obtener el ID del producto seleccionado desde la tabla
                String idProducto = (String) tablaProductos.getValueAt(selectedRow, 0);
        
                // Crear la ventana usando la plantilla
                JFrame formularioPrecio = GenerarVentanaFormulario.crearVentana("Modificar Precio Unitario", 400, 200);
        
                // Crear el panel de formulario estilizado
                JPanel panelFormulario = GenerarVentanaFormulario.crearPanelFormulario();
        
                // Crear los componentes estilizados
                JLabel etiquetaPrecio = GenerarVentanaFormulario.crearEtiqueta("Nuevo Precio:");
                JTextField campoPrecio = GenerarVentanaFormulario.crearCampoTexto(20);
                JButton botonGuardar = GenerarVentanaFormulario.crearBoton("Guardar");
        
                // Añadir componentes al panel con posiciones
                GenerarVentanaFormulario.agregarComponente(panelFormulario, etiquetaPrecio, 0, 0, 1, 1);
                GenerarVentanaFormulario.agregarComponente(panelFormulario, campoPrecio, 1, 0, 2, 1);
                GenerarVentanaFormulario.agregarComponente(panelFormulario, botonGuardar, 1, 1, 1, 1);
        
                // Añadir el panel al centro de la ventana
                formularioPrecio.add(panelFormulario, BorderLayout.CENTER);
        
                // Acción del botón Guardar
                botonGuardar.addActionListener(ev -> {
                    String nuevoPrecio = campoPrecio.getText();
                    if (!nuevoPrecio.isEmpty()) {
                        try {
                            // Validar que el precio es numérico
                            Double.parseDouble(nuevoPrecio);
        
                            // Llamar al método modificar del Controlador
                            boolean resultado = Controlador.modificar(
                                "PRODUCTOS",           // Tabla
                                "ID_PRODUCTO",         // Campo de la condición
                                idProducto,            // Valor del campo para la condición
                                "=",                   // Operador de comparación
                                "PRECIO_UNITARIO",     // Campo a actualizar
                                nuevoPrecio            // Nuevo valor
                            );
        
                            if (resultado) {
                                JOptionPane.showMessageDialog(formularioPrecio, "Precio modificado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                                actualizarTabla(); // Actualizar la tabla para reflejar los cambios
                            } else {
                                JOptionPane.showMessageDialog(formularioPrecio, "Error al modificar el precio.", "Error", JOptionPane.ERROR_MESSAGE);
                            }
                            formularioPrecio.dispose();
                        } catch (NumberFormatException ex) {
                            JOptionPane.showMessageDialog(formularioPrecio, "Ingrese un precio válido.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        JOptionPane.showMessageDialog(formularioPrecio, "El campo de precio no puede estar vacío.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                });
        
                formularioPrecio.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Seleccione un producto para modificar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            }
        });
        

        campoBusqueda.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent e) {
                String textoBusqueda = campoBusqueda.getText().trim();
                if (!textoBusqueda.isEmpty()) {
                    String condicion = "NOMBRE LIKE '%" + textoBusqueda + "%'";
                    actualizarTablaConFiltro(condicion);
                } else {
                    actualizarTabla(); // Si el cuadro de búsqueda está vacío, mostrar todos los datos
                }
            }
        });
        
        // Método para actualizar la tabla con un filtro
        
        
        // Tabla de productos deshabilitada
        String[] columnas = {"ID", "Nombre", "Precio Unitario", "Categoría", "Grado de Relevancia"};
        tablaProductos = GenerarComponentes.crearTabla(columnas);
        JScrollPane scrollTabla = GenerarComponentes.crearScrollPane(tablaProductos);

        // Añadir componentes al panel principal
        add(panelBusqueda, BorderLayout.NORTH);      // Motor de búsqueda en la parte superior
        add(panelBotones, BorderLayout.WEST);        // Botones a la izquierda
        add(scrollTabla, BorderLayout.CENTER);       // Tabla de productos en el centro
        
        
        actualizarTabla();
    }

    public static String idCategoria(String categoriaSeleccionada){
        String idCategoria = "";
        for (String[] categorias : capturarCategorias()) {
            if (categorias[1].equals(categoriaSeleccionada)) {
                idCategoria = categorias[0];
            }
        }
        return idCategoria;
    }

    private void actualizarTablaConFiltro(String condicion) {
        String[] columnas = {"ID", "Nombre", "Precio Unitario", "Categoría", "Grado de Relevancia"};
        ArrayList<String[]> productos = Controlador.consultar("PRODUCTOS", null, null, condicion);
        String[][] datos = new String[productos.size()][columnas.length];
        for (int i = 0; i < productos.size(); i++) {
            datos[i] = productos.get(i);
        }
        tablaProductos.setModel(new DefaultTableModel(datos, columnas));
    
        // Ajustar anchos de columnas
        tablaProductos.getColumnModel().getColumn(0).setPreferredWidth(80);  // ID
        tablaProductos.getColumnModel().getColumn(1).setPreferredWidth(200); // Nombre
        tablaProductos.getColumnModel().getColumn(2).setPreferredWidth(100); // Precio Unitario
        tablaProductos.getColumnModel().getColumn(3).setPreferredWidth(150); // Categoría
        tablaProductos.getColumnModel().getColumn(4).setPreferredWidth(120); // Grado de Relevancia
        for (int i = 0; i < columnas.length; i++) {
            tablaProductos.getColumnModel().getColumn(i).setResizable(false);
        }
    }


    public static JFrame crearFormularioProducto() {
        // Crear la ventana principal
        JFrame ventana = GenerarVentanaFormulario.crearVentana("Formulario de Producto", 400, 300);

        // Crear el panel principal
        JPanel panelFormulario = GenerarVentanaFormulario.crearPanelFormulario();

        // Crear componentes del formulario
        JLabel etiquetaNombre = GenerarVentanaFormulario.crearEtiqueta("Nombre:");
        JTextField campoNombre = GenerarVentanaFormulario.crearCampoTexto(20);

        JLabel etiquetaCategoria = GenerarVentanaFormulario.crearEtiqueta("Categoría:");
        
        String[] categorias_Descripcion = new String[capturarCategorias().size()];
        int i = 0;
        for (String[] categoria : capturarCategorias()) {

            categorias_Descripcion[i] = categoria[1];
            i++;
        }

        JComboBox<String> campoCategoria = GenerarVentanaFormulario.crearComboBox(categorias_Descripcion);

        
        


        JLabel etiquetaPrecio = GenerarVentanaFormulario.crearEtiqueta("Precio:");
        JTextField campoPrecio = GenerarVentanaFormulario.crearCampoTexto(20);

        JLabel etiquetaRelevancia = GenerarVentanaFormulario.crearEtiqueta("Grado de Relevancia:");
        JTextField campoRelevancia = GenerarVentanaFormulario.crearCampoTexto(20);

        JButton botonGuardar = GenerarVentanaFormulario.crearBoton("Guardar");

        // Agregar componentes al panel usando posiciones específicas
        GenerarVentanaFormulario.agregarComponente(panelFormulario, etiquetaNombre, 0, 0, 1, 1);
        GenerarVentanaFormulario.agregarComponente(panelFormulario, campoNombre, 1, 0, 2, 1);

        GenerarVentanaFormulario.agregarComponente(panelFormulario, etiquetaCategoria, 0, 1, 1, 1);
        GenerarVentanaFormulario.agregarComponente(panelFormulario, campoCategoria, 1, 1, 2, 1);

        GenerarVentanaFormulario.agregarComponente(panelFormulario, etiquetaPrecio, 0, 2, 1, 1);
        GenerarVentanaFormulario.agregarComponente(panelFormulario, campoPrecio, 1, 2, 2, 1);

        GenerarVentanaFormulario.agregarComponente(panelFormulario, etiquetaRelevancia, 0, 3, 1, 1);
        GenerarVentanaFormulario.agregarComponente(panelFormulario, campoRelevancia, 1, 3, 2, 1);

        GenerarVentanaFormulario.agregarComponente(panelFormulario, botonGuardar, 1, 4, 1, 1);

        // Agregar el panel a la ventana
        ventana.add(panelFormulario);

        // JRBeanCollection

        // JRDataCollection
        botonGuardar.addActionListener(e -> {
            String categoriaSeleccionada = (String) campoCategoria.getSelectedItem();
            String categoria = idCategoria(categoriaSeleccionada);
            String id = generarID(categoriaSeleccionada);
            String nombre = campoNombre.getText();
            String precio = campoPrecio.getText();
            String gradoDeRelevancia = campoRelevancia.getText();
            String[] datosProducto = {id, nombre, precio, categoria, gradoDeRelevancia};


            // Aquí puedes agregar el código para manejar los datos guardados, por ejemplo, imprimirlos
            System.out.println("Datos del producto guardados: " + Arrays.toString(datosProducto));
            Controlador.agregarDB("PRODUCTO", null, datosProducto);
            ventana.dispose();
        });
        
        // Retornar la ventana creada
        return ventana;
    }

    

    public static ArrayList<String[]> capturarCategorias() {
        return Controlador.consultar("CATEGORIAS", null, null, null);
    }
    

    public static String generarID(String categoriaSeleccionada){
        System.out.println("Categoría seleccionada: " + categoriaSeleccionada);
        String id_Categoria = "";
        for (String[] categoria : capturarCategorias()) {
            if (categoria[1].equals(categoriaSeleccionada)) {
                id_Categoria = categoria[0];
            }
        }


        int getCantidadProductos = Controlador.getCantidad("PRODUCTOS", null, "IDCATEGORIA", id_Categoria) + 1;
        String idCantidadProductos = String.valueOf(getCantidadProductos);        
        idCantidadProductos = String.format("%03d", getCantidadProductos);
        String id_Producto = id_Categoria + "-" + idCantidadProductos;
        return id_Producto;
    }
    

    @Override

    
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        Color colorInicio = new Color(255, 94, 98);
        Color colorFinal = new Color(255, 175, 123);
        int width = getWidth();
        int height = getHeight();
        GradientPaint gp = new GradientPaint(0, 0, colorInicio, 0, height, colorFinal);
        g2d.setPaint(gp);
        g2d.fillRect(0, 0, width, height);
        
    }

    
    public void actualizarTabla() {
        String[] columnas = {"ID", "Nombre", "Precio Unitario", "Categoría", "Grado de Relevancia"};
        ArrayList<String[]> productos = Controlador.consultar("PRODUCTOS", null, null, null);
        String[][] datos = new String[productos.size()][columnas.length];
        for (int i = 0; i < productos.size(); i++) {
            datos[i] = productos.get(i);
        }
        tablaProductos.setModel(new DefaultTableModel(datos, columnas));
    
        tablaProductos.getColumnModel().getColumn(0).setPreferredWidth(80);  // ID
        tablaProductos.getColumnModel().getColumn(1).setPreferredWidth(200); // Nombre
        tablaProductos.getColumnModel().getColumn(2).setPreferredWidth(100); // Precio Unitario
        tablaProductos.getColumnModel().getColumn(3).setPreferredWidth(150); // Categoría
        tablaProductos.getColumnModel().getColumn(4).setPreferredWidth(120); // Grado de Relevancia
        for (int i = 0; i < columnas.length; i++) {
            tablaProductos.getColumnModel().getColumn(i).setResizable(false);
        }    
    }

    
}
