package Vista.Ventas;

import Vista.Interfaces.ActualizarTable;
import Vista.Plantillas.GenerarVentanaFormulario;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import Controlador.Controlador;

import java.awt.*;
import java.util.ArrayList;

public class VentanaProductosCarro extends JFrame implements ActualizarTable{

    private JTable tablaProductos;
    public VentanaProductosCarro() {
        // Crear la ventana base con la plantilla
        super("Gestión de Productos");
        setSize(800, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        setLocationRelativeTo(null);

        // Panel superior para ID y Nombre
        JPanel panelSuperior = new JPanel(new GridLayout(1, 4, 10, 10));
        panelSuperior.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
        JLabel labelID = GenerarVentanaFormulario.crearEtiqueta("ID:");
        JTextField campoID = GenerarVentanaFormulario.crearCampoTexto(10);
        JLabel labelNombre = GenerarVentanaFormulario.crearEtiqueta("Nombre:");
        JTextField campoNombre = GenerarVentanaFormulario.crearCampoTexto(10);
        panelSuperior.add(labelID);
        panelSuperior.add(campoID);
        panelSuperior.add(labelNombre);
        panelSuperior.add(campoNombre);

        // Panel central con tabla a la izquierda y detalles a la derecha
        JPanel panelCentral = new JPanel(new GridLayout(1, 2, 10, 10));
        panelCentral.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Tabla de productos (lado izquierdo)
        String[] columnas = {"ID", "Nombre", "Precio"};
        Object[][] datos = {}; // Datos iniciales vacíos
        tablaProductos = new JTable(datos, columnas);
        JScrollPane scrollTabla = new JScrollPane(tablaProductos);
        panelCentral.add(scrollTabla);

        // Panel derecho para cantidad, detalles y botón
        JPanel panelDerecho = new JPanel(new BorderLayout(10, 10));

        // Panel para cantidad (centrado)
        JPanel panelCantidad = new JPanel(new FlowLayout(FlowLayout.CENTER)); // Alineación centrada
        JLabel labelCantidad = GenerarVentanaFormulario.crearEtiqueta("Cantidad:");
        JTextField campoCantidad = GenerarVentanaFormulario.crearCampoTexto(10);
        panelCantidad.add(labelCantidad);
        panelCantidad.add(campoCantidad);
        panelDerecho.add(panelCantidad, BorderLayout.NORTH);

        // Botón para agregar al carrito con ícono
        JButton botonAgregarCarrito = GenerarVentanaFormulario.crearBoton("Agregar al Carrito");
        botonAgregarCarrito.setPreferredSize(new Dimension(200, 40));
        botonAgregarCarrito.setHorizontalTextPosition(SwingConstants.RIGHT);
        botonAgregarCarrito.setIcon(cargarIcono("src/resources/icons/carrito.png")); // Ruta del ícono
        JPanel panelBoton = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelBoton.add(botonAgregarCarrito);

        // Acción del botón para agregar producto al carrito
        botonAgregarCarrito.addActionListener(e -> {
            int selectedRow = tablaProductos.getSelectedRow();
            if (selectedRow != -1) {
                String id = (String) tablaProductos.getValueAt(selectedRow, 0);
                String nombre = (String) tablaProductos.getValueAt(selectedRow, 1);
                String precio = (String) tablaProductos.getValueAt(selectedRow, 2);
                String cantidad = campoCantidad.getText();
        
                if (!cantidad.isEmpty() && cantidad.matches("\\d+")) {
                    int cantidadInt = Integer.parseInt(cantidad);
                    double precioUnitario = Double.parseDouble(precio);
        
                    // Obtener el ID de la venta actual
                    String idVenta = "VENTA-ACTUAL"; // Asegúrate de obtener el ID_VENTA actual según tu lógica
        
                    // Llamar al método del controlador para manejar el producto
                    Controlador.agregarProductoCarro(id, nombre, precioUnitario, cantidadInt, idVenta);
        
                    JOptionPane.showMessageDialog(this, "Producto agregado/actualizado en el carrito.");
                } else {
                    JOptionPane.showMessageDialog(this, "Por favor, ingrese una cantidad válida.");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Por favor, seleccione un producto de la tabla.");
            }
        });
        
        
        

        // Panel para detalles del producto (más pequeño)
        JPanel panelDetalles = GenerarVentanaFormulario.crearPanelFormulario();
        panelDetalles.setBorder(BorderFactory.createTitledBorder("Detalles del Producto Seleccionado"));
        JTextArea areaDetalles = new JTextArea(5, 20); // Altura menor para el área de texto
        areaDetalles.setEditable(false);
        JScrollPane scrollDetalles = new JScrollPane(areaDetalles);
        panelDetalles.add(scrollDetalles);
        panelDetalles.setPreferredSize(new Dimension(200, 100)); // Tamaño más pequeño
        panelDerecho.add(panelDetalles, BorderLayout.CENTER);

        // Agregar el botón al final del panel derecho
        panelDerecho.add(panelBoton, BorderLayout.SOUTH);

        panelCentral.add(panelDerecho);

        // Agregar los paneles a la ventana
        add(panelSuperior, BorderLayout.NORTH);
        add(panelCentral, BorderLayout.CENTER);

        // Acciones del botón agregar al carrito
        // Agregar un ListSelectionListener a la tabla para deshabilitar el botón si el producto está agotado
        tablaProductos.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) { // Verificar que el evento no se está ajustando
                int selectedRow = tablaProductos.getSelectedRow();
                if (selectedRow != -1) {
                    int cantidadDisponible = Integer.parseInt((String) tablaProductos.getValueAt(selectedRow, 3)); // Columna de cantidad
                    if (cantidadDisponible == 0) {
                        botonAgregarCarrito.setEnabled(false); // Deshabilitar el botón
                    } else {
                        botonAgregarCarrito.setEnabled(true); // Habilitar el botón
                    }
                }
            }
        });

        // Acción del botón para agregar producto al carrito
        botonAgregarCarrito.addActionListener(e -> {
            int selectedRow = tablaProductos.getSelectedRow();
            if (selectedRow != -1) {
                // Obtener información del producto seleccionado
                String id = (String) tablaProductos.getValueAt(selectedRow, 0);
                String nombre = (String) tablaProductos.getValueAt(selectedRow, 1);
                String precio = (String) tablaProductos.getValueAt(selectedRow, 2);
                String cantidad = campoCantidad.getText();

                // Validar la cantidad ingresada
                if (!cantidad.isEmpty() && cantidad.matches("\\d+")) {
                    int cantidadInt = Integer.parseInt(cantidad);
                    double precioUnitario = Double.parseDouble(precio);

                    // Llamar al método del controlador para manejar el producto
                    String idVenta = "VENTA-ACTUAL"; // Asegúrate de obtener el ID_VENTA actual según tu lógica
                    Controlador.agregarProductoCarro(id, nombre, precioUnitario, cantidadInt, idVenta);

                    JOptionPane.showMessageDialog(this, "Producto agregado/actualizado en el carrito.");
                } else {
                    JOptionPane.showMessageDialog(this, "Por favor, ingrese una cantidad válida.");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Por favor, seleccione un producto de la tabla.");
            }
        });

        
        
        
        actualizarTabla();
    }

    // Método para cargar íconos desde la ruta especificada
    private ImageIcon cargarIcono(String ruta) {
        ImageIcon icono = new ImageIcon(ruta);
        Image imagenEscalada = icono.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        return new ImageIcon(imagenEscalada);
    }

    @Override
    public void actualizarTabla() {
        String[] columnas = {"ID", "Nombre", "Precio Unitario", "Cantidad"};

        ArrayList<String[]> productosInventario = Controlador.llenarDatosDB(
            "PRODUCTO_INVENTARIO", 
            "PRODUCTOS", 
            "PRODUCTOS.ID_PRODUCTO, PRODUCTOS.NOMBRE, PRODUCTOS.PRECIO_UNITARIO, PRODUCTO_INVENTARIO.CANTIDAD", 
            "PRODUCTOS.ID_PRODUCTO = PRODUCTO_INVENTARIO.ID_PRODUCTO"
        );

        String[][] datos = new String[productosInventario.size()][columnas.length];
        for (int i = 0; i < productosInventario.size(); i++) {
            datos[i] = productosInventario.get(i);
        }

        DefaultTableModel modelo = new DefaultTableModel(datos, columnas) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Deshabilitar edición
            }
        };

        tablaProductos.setModel(modelo);

        // Ajustar columnas
        tablaProductos.getColumnModel().getColumn(0).setPreferredWidth(40);  // ID
        tablaProductos.getColumnModel().getColumn(1).setPreferredWidth(100); // Nombre
        tablaProductos.getColumnModel().getColumn(2).setPreferredWidth(50);  // Precio Unitario
        tablaProductos.getColumnModel().getColumn(3).setPreferredWidth(50);  // Cantidad

        // Aplicar renderer para deshabilitar visualmente filas con cantidad 0
        tablaProductos.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                int cantidad = Integer.parseInt((String) table.getValueAt(row, 3)); // Columna "Cantidad"
                if (cantidad == 0) {
                    c.setForeground(Color.GRAY); // Mostrar texto en gris
                    c.setEnabled(false);         // Deshabilitar visualmente
                } else {
                    c.setForeground(Color.BLACK); // Texto normal
                    c.setEnabled(true);           // Habilitar visualmente
                }
                return c;
            }
        });
    }

}

