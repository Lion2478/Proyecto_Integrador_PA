package Vista.Inventario;

import javax.swing.*;
import Controlador.Controlador;
import Vista.Interfaces.ActualizarTable;
import Vista.Plantillas.GenerarComponentes;

import java.awt.*;
import java.util.ArrayList;

public class PantallaInventario extends JPanel implements ActualizarTable {

    private static final String[] COLUMNAS = {"ID", "Nombre", "Grado de Relvancia", "Cantidad", "Última Importación"};
    private JLabel searchLabel;
    private JTextField campoBusqueda;
    private JTable tablaInventario;
    private JButton botonExportarJSON, botonExportarCSV, botonExportarXML, botonComprar;

    public PantallaInventario() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Margen general
        setOpaque(true);

        // Panel superior que contiene el motor de búsqueda y el botón "Comprar"
        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.setOpaque(false);

        JPanel panelBusqueda = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panelBusqueda.setOpaque(false);

        searchLabel = new JLabel("Buscar por Nombre:");
        campoBusqueda = GenerarComponentes.crearCampoTexto(25);

        panelBusqueda.add(searchLabel); // Añadir el label
        panelBusqueda.add(campoBusqueda); // Añadir el cuadro de texto
        panelSuperior.add(panelBusqueda, BorderLayout.WEST);

        // Botón "Comprar" en el lado derecho
        botonComprar = GenerarComponentes.crearBoton("Comprar");
        JPanel panelBotonComprar = GenerarComponentes.crearPanelFlowLayout(FlowLayout.RIGHT, 10, 10);
        panelBotonComprar.add(botonComprar);
        panelSuperior.add(panelBotonComprar, BorderLayout.EAST);

        // Configuración de ActionListener para el botón "Comprar"
        configurarBotonComprar();

        // Tabla de inventario deshabilitada
        tablaInventario = GenerarComponentes.crearTabla(COLUMNAS);
        JScrollPane scrollTabla = GenerarComponentes.crearScrollPane(tablaInventario);

        // Panel inferior para los botones de exportación
        JPanel panelBotones = GenerarComponentes.crearPanelFlowLayout(FlowLayout.CENTER, 20, 10);

        botonExportarJSON = GenerarComponentes.crearBoton("Exportar en JSON");
        botonExportarCSV = GenerarComponentes.crearBoton("Exportar en CSV");
        botonExportarXML = GenerarComponentes.crearBoton("Exportar en XML");

        panelBotones.add(botonExportarJSON);
        panelBotones.add(botonExportarCSV);
        panelBotones.add(botonExportarXML);

        // Configuración de ActionListeners para los botones de exportación
        configurarBotonesExportacion();

        // Añadir componentes al panel principal
        add(panelSuperior, BorderLayout.NORTH);       // Panel superior con búsqueda y botón "Comprar"
        add(scrollTabla, BorderLayout.CENTER);        // Tabla de inventario en el centro
        add(panelBotones, BorderLayout.SOUTH);        // Botones de exportación en la parte inferior

        actualizarTabla();
    }

    private void configurarBotonComprar() {
        botonComprar.addActionListener(e -> {
            int selectedRow = tablaInventario.getSelectedRow();
            if (selectedRow != -1) {
                String idProducto = (String) tablaInventario.getValueAt(selectedRow, 0);
                String nombreProducto = (String) tablaInventario.getValueAt(selectedRow, 1);
                String cantidadProductoStr = (String) tablaInventario.getValueAt(selectedRow, 2);
                int cantidadProducto = Integer.parseInt(cantidadProductoStr);

                JTextField campoCantidad = new JTextField(10);

                JPanel panelFormulario = new JPanel(new GridLayout(3, 2, 10, 10));
                panelFormulario.add(new JLabel("ID Producto:"));
                JTextField campoID = new JTextField(idProducto);
                campoID.setEnabled(false);
                panelFormulario.add(campoID);

                panelFormulario.add(new JLabel("Nombre Producto:"));
                JTextField campoNombre = new JTextField(nombreProducto);
                campoNombre.setEnabled(false);
                panelFormulario.add(campoNombre);

                panelFormulario.add(new JLabel("Cantidad:"));
                panelFormulario.add(campoCantidad);

                int result = JOptionPane.showConfirmDialog(null, panelFormulario, "Comprar Producto",
                        JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

                if (result == JOptionPane.OK_OPTION) {
                    try {
                        int cantidad = Integer.parseInt(campoCantidad.getText());
                        if (cantidad <= 0) {
                            JOptionPane.showMessageDialog(null, "La cantidad debe ser mayor a 0.", "Error",
                                    JOptionPane.ERROR_MESSAGE);
                            return;
                        }

                        String fechaActual = new java.sql.Date(System.currentTimeMillis()).toString();
                        Controlador.modificar("PRODUCTO_INVENTARIO", "ID_PRODUCTO", idProducto, "=",
                                "CANTIDAD", String.valueOf(cantidadProducto + cantidad));
                        Controlador.modificar("PRODUCTO_INVENTARIO", "ID_PRODUCTO", idProducto, "=",
                                "ULTIMA_IMPORTACION", fechaActual);

                        JOptionPane.showMessageDialog(null, "Producto actualizado correctamente.");
                        actualizarTabla();
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(null, "Por favor, ingrese un número válido para la cantidad.",
                                "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(null, "Por favor, seleccione un producto de la tabla.", "Advertencia",
                        JOptionPane.WARNING_MESSAGE);
            }
        });
    }

    private void configurarBotonesExportacion() {
        botonExportarJSON.addActionListener(e -> {
            String archivo = "inventario.json";
            ArrayList<String[]> datos = Controlador.llenarDatosDB(
                    "PRODUCTO_INVENTARIO", "PRODUCTOS",
                    "PRODUCTOS.ID_PRODUCTO, PRODUCTOS.NOMBRE, PRODUCTOS.GRADO_RELEVANCIA, PRODUCTO_INVENTARIO.CANTIDAD, PRODUCTO_INVENTARIO.ULTIMA_IMPORTACION",
                    "PRODUCTOS.ID_PRODUCTO = PRODUCTO_INVENTARIO.ID_PRODUCTO");
            Controlador.exportarAJSON(datos, COLUMNAS, archivo);
        });

        botonExportarCSV.addActionListener(e -> {
            String archivo = "inventario.csv";
            ArrayList<String[]> datos = Controlador.llenarDatosDB(
                    "PRODUCTO_INVENTARIO", "PRODUCTOS",
                    "PRODUCTOS.ID_PRODUCTO, PRODUCTOS.NOMBRE, PRODUCTOS.GRADO_RELEVANCIA, PRODUCTO_INVENTARIO.CANTIDAD, PRODUCTO_INVENTARIO.ULTIMA_IMPORTACION",
                    "PRODUCTOS.ID_PRODUCTO = PRODUCTO_INVENTARIO.ID_PRODUCTO");
            Controlador.exportarACSV(datos, COLUMNAS, archivo);
        });

        botonExportarXML.addActionListener(e -> {
            String archivo = "inventario.xml";
            ArrayList<String[]> datos = Controlador.llenarDatosDB(
                    "PRODUCTO_INVENTARIO", "PRODUCTOS",
                    "PRODUCTOS.ID_PRODUCTO, PRODUCTOS.NOMBRE, PRODUCTOS.GRADO_RELEVANCIA, PRODUCTO_INVENTARIO.CANTIDAD, PRODUCTO_INVENTARIO.ULTIMA_IMPORTACION",
                    "PRODUCTOS.ID_PRODUCTO = PRODUCTO_INVENTARIO.ID_PRODUCTO");
            Controlador.exportarAXML(datos, COLUMNAS, archivo);
        });
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
        ArrayList<String[]> productosInventario = Controlador.llenarDatosDB(
                "PRODUCTO_INVENTARIO", "PRODUCTOS",
                "PRODUCTOS.ID_PRODUCTO, PRODUCTOS.NOMBRE, PRODUCTOS.GRADO_RELEVANCIA, PRODUCTO_INVENTARIO.CANTIDAD, PRODUCTO_INVENTARIO.ULTIMA_IMPORTACION",
                "PRODUCTOS.ID_PRODUCTO = PRODUCTO_INVENTARIO.ID_PRODUCTO");
        String[][] datos = new String[productosInventario.size()][COLUMNAS.length];

        for (int i = 0; i < productosInventario.size(); i++) {
            datos[i] = productosInventario.get(i);
        }

        tablaInventario.setModel(new javax.swing.table.DefaultTableModel(datos, COLUMNAS));

        tablaInventario.getColumnModel().getColumn(0).setPreferredWidth(80);  // ID
        tablaInventario.getColumnModel().getColumn(1).setPreferredWidth(200); // Nombre
        tablaInventario.getColumnModel().getColumn(2).setPreferredWidth(150); // Grado de Relevancia
        tablaInventario.getColumnModel().getColumn(3).setPreferredWidth(100); // Cantidad
        tablaInventario.getColumnModel().getColumn(4).setPreferredWidth(150); // Última Importación

        for (int i = 0; i < COLUMNAS.length; i++) {
            tablaInventario.getColumnModel().getColumn(i).setResizable(false);
        }
    }
}