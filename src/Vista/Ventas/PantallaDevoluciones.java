package Vista.Ventas;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import Controlador.Controlador;
import Vista.Interfaces.ActualizarTable;
import Vista.Plantillas.GenerarComponentes;

import java.awt.*;
import java.util.ArrayList;

public class PantallaDevoluciones extends JPanel implements ActualizarTable {

    private JLabel searchLabel;
    private JPanel searchPanel;
    private JTable tableVentas;
    private JTable tableProductos;
    private JScrollPane scrollPaneVentas, scrollPaneProductos;

    public PantallaDevoluciones() {
        // Configurar el diseño y margen del panel principal
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Margen de separación con los bordes
        setOpaque(false); // Fondo transparente del panel principal

        // Barra superior con campo de búsqueda por ID
        searchPanel = GenerarComponentes.crearPanelFlowLayout(FlowLayout.LEFT, 10, 10);
        searchPanel.setOpaque(false); // Fondo transparente del panel
        searchLabel = new JLabel("Buscar por ID:");
        JTextField searchField = GenerarComponentes.crearCampoTexto(20);
        JButton botonBuscar = GenerarComponentes.crearBoton("Buscar");

        searchPanel.add(searchLabel);
        searchPanel.add(searchField);
        searchPanel.add(botonBuscar);


        // Tabla de ventas a la izquierda
        String[] columnNamesVentas = {"ID", "Nombre Cliente", "Fecha", "Total"};
        tableVentas = GenerarComponentes.crearTabla(columnNamesVentas);
        scrollPaneVentas = GenerarComponentes.crearScrollPane(tableVentas);

        // Tabla de productos a la derecha
        String[] columnNamesProductos = {"ID Producto", "Nombre del Producto", "Cantidad", "Subtotal"};
        tableProductos = GenerarComponentes.crearTabla(columnNamesProductos);
        scrollPaneProductos = GenerarComponentes.crearScrollPane(tableProductos);

        // Panel para contener las dos tablas
        JPanel tablesPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        tablesPanel.setOpaque(false); // Fondo transparente
        tablesPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        tablesPanel.add(scrollPaneVentas);
        tablesPanel.add(scrollPaneProductos);

        // Panel inferior para botones debajo de la tabla de productos
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        panelBotones.setOpaque(false); // Fondo transparente
        JButton botonEliminar = GenerarComponentes.crearBoton("Eliminar");
        JButton botonEditarCantidad = GenerarComponentes.crearBoton("Editar Cantidad");

        // Configurar botones con fondo transparente
        botonEliminar.setOpaque(false);
        botonEliminar.setContentAreaFilled(false);
        botonEliminar.setBorderPainted(true);

        botonEditarCantidad.setOpaque(false);
        botonEditarCantidad.setContentAreaFilled(false);
        botonEditarCantidad.setBorderPainted(true);

        panelBotones.add(botonEliminar);
        panelBotones.add(botonEditarCantidad);

        // Agregar componentes al panel principal
        add(searchPanel, BorderLayout.NORTH);
        add(tablesPanel, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);

        actualizarTabla();

        // Listeners para las tablas y botones
        tableVentas.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) { // Verificar que la selección no se está ajustando
                int selectedRow = tableVentas.getSelectedRow();
                if (selectedRow != -1) { // Asegurarse de que hay una fila seleccionada
                    String ventaID = (String) tableVentas.getValueAt(selectedRow, 0); // Obtener el ID de la venta seleccionada
                    actualizarTablaProductos(ventaID); // Actualizar la tabla de productos
                }
            }
        });

        
        botonEditarCantidad.addActionListener(e -> {
            int selectedRow = tableProductos.getSelectedRow();
            if (selectedRow != -1) {
                String idProducto = (String) tableProductos.getValueAt(selectedRow, 0);
                String idVenta = (String) tableVentas.getValueAt(tableVentas.getSelectedRow(), 0);
                String cantidadActual = (String) tableProductos.getValueAt(selectedRow, 2);
        
                JTextField campoCantidad = new JTextField(cantidadActual);
        
                JPanel panelEdicion = new JPanel(new GridLayout(2, 2, 10, 10));
                panelEdicion.setOpaque(false);
                panelEdicion.add(new JLabel("ID Producto:"));
                panelEdicion.add(new JLabel(idProducto));
                panelEdicion.add(new JLabel("Nueva Cantidad:"));
                panelEdicion.add(campoCantidad);
        
                int resultado = JOptionPane.showConfirmDialog(
                        this,
                        panelEdicion,
                        "Editar Cantidad del Producto",
                        JOptionPane.OK_CANCEL_OPTION,
                        JOptionPane.PLAIN_MESSAGE
                );
        
                if (resultado == JOptionPane.OK_OPTION) {
                    try {
                        int nuevaCantidad = Integer.parseInt(campoCantidad.getText());
                        if (nuevaCantidad <= 0) {
                            JOptionPane.showMessageDialog(this, "La cantidad debe ser mayor a 0.", "Error", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
        
                        // Llamar al método modificar con dos condiciones
                        Controlador.modificar("PRODUCTO_VENTAS", 
                                              "ID_VENTA", idVenta, "=", 
                                              "ID_PRODUCTO", idProducto, "=",
                                              "CANTIDAD", String.valueOf(nuevaCantidad));
        
                        actualizarTablaProductos(idVenta);
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(this, "Ingrese un número válido para la cantidad.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Seleccione un producto para editar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            }
        });

        botonEliminar.addActionListener(e -> {
            int selectedRow = tableProductos.getSelectedRow();
            if (selectedRow != -1) {
                String idProducto = (String) tableProductos.getValueAt(selectedRow, 0); // ID del producto
                String idVenta = (String) tableVentas.getValueAt(tableVentas.getSelectedRow(), 0); // ID de la venta seleccionada
        
                int confirmacion = JOptionPane.showConfirmDialog(
                        this,
                        "¿Está seguro de que desea eliminar este producto?",
                        "Confirmar Eliminación",
                        JOptionPane.YES_NO_OPTION
                );
        
                if (confirmacion == JOptionPane.YES_OPTION) {
                    // Llamar al método eliminar con dos condiciones
                    Controlador.eliminarDB("PRODUCTO_VENTAS", "ID_VENTA", idVenta, "ID_PRODUCTO", idProducto);
        
                    // Actualizar la tabla de productos
                    actualizarTablaProductos(idVenta);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Seleccione un producto para eliminar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            }
        });

        // Listener para la búsqueda
        searchField.addActionListener(e -> {
            String idBusqueda = searchField.getText().trim();

            if (!idBusqueda.isEmpty()) {
                ArrayList<String[]> ventasFiltradas = Controlador.consultar(
                    "VENTAS", 
                    null, 
                    null, 
                    "ID_VENTA = '" + idBusqueda + "'"
                );

                if (ventasFiltradas.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "No se encontró ninguna venta con el ID proporcionado.", "Sin resultados", JOptionPane.WARNING_MESSAGE);
                } else {
                    // Actualizar la tabla de ventas con los resultados filtrados
                    String[] columnas = {"ID", "Nombre Cliente", "Fecha", "Total"};
                    String[][] datos = new String[ventasFiltradas.size()][columnas.length];

                    for (int i = 0; i < ventasFiltradas.size(); i++) {
                        datos[i] = ventasFiltradas.get(i);
                    }

                    tableVentas.setModel(new DefaultTableModel(datos, columnas));

                    // Limpiar la tabla de productos
                    tableProductos.setModel(new DefaultTableModel(new String[0][4], new String[]{"ID Producto", "Nombre del Producto", "Cantidad", "Subtotal"}));
                }
            } else {
                JOptionPane.showMessageDialog(this, "Por favor, ingrese un ID para realizar la búsqueda.", "Campo vacío", JOptionPane.WARNING_MESSAGE);
            }
        });
        botonBuscar.addActionListener(e -> searchField.postActionEvent());


        
        
        
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
        String[] columnas = {"ID", "Nombre Cliente", "Fecha", "Total"};
        ArrayList<String[]> ventas = Controlador.consultar("VENTAS", null, null, null);
        String[][] datos = new String[ventas.size()][columnas.length];
        for (int i = 0; i < ventas.size(); i++) {
            datos[i] = ventas.get(i);
        }
        tableVentas.setModel(new DefaultTableModel(datos, columnas));
    }

    private void actualizarTablaProductos(String ventaID) {
        String[] columnas = {"ID Producto", "Nombre del Producto", "Cantidad", "Subtotal"};
        ArrayList<String[]> productos = Controlador.consultar("PRODUCTOS", "PRODUCTO_VENTAS", "PRODUCTOS.ID_PRODUCTO, PRODUCTOS.NOMBRE, PRODUCTO_VENTAS.CANTIDAD, (PRODUCTO_VENTAS.CANTIDAD*PRODUCTOS.PRECIO_UNITARIO)", "PRODUCTO_VENTAS.ID_VENTA = '" + ventaID + "' AND PRODUCTOS.ID_PRODUCTO = PRODUCTO_VENTAS.ID_PRODUCTO");
        String[][] datos = new String[productos.size()][columnas.length];
        for (int i = 0; i < productos.size(); i++) {
            datos[i] = productos.get(i);
        }
        tableProductos.setModel(new DefaultTableModel(datos, columnas));
    }
}
