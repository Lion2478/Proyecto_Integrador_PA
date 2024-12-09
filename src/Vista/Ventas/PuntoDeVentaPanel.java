package Vista.Ventas;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import Controlador.Controlador;
import Vista.Interfaces.ActualizarTable;
import Vista.Interfaces.GestorActualizaciones;
import Vista.Plantillas.GenerarComponentes;
import Vista.Plantillas.GenerarVentanaFormulario;

import java.awt.*;
import java.util.ArrayList;

public class PuntoDeVentaPanel extends JPanel implements ActualizarTable {

    private JTable tablaProductos;
    private JButton botonAgregarProducto, botonEliminarProducto, botonEditarProducto, botonAlertasStock, botonPagar;
    private JRadioButton radioEfectivo, radioTarjeta;
    private JTextField campoNumeroTarjeta, campoFechaExpiracion, campoCVV;
    private JPanel panelMetodoPago;

    public PuntoDeVentaPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setOpaque(true);

        // Panel izquierdo para los botones de productos
        JPanel panelBotonesProductos = new JPanel();
        panelBotonesProductos.setLayout(new BoxLayout(panelBotonesProductos, BoxLayout.Y_AXIS));
        panelBotonesProductos.setOpaque(false);

        botonAgregarProducto = GenerarComponentes.crearBoton("Agregar Producto");
        botonEliminarProducto = GenerarComponentes.crearBoton("Eliminar Producto");
        botonEditarProducto = GenerarComponentes.crearBoton("Editar Producto");

        botonAgregarProducto.addActionListener(e -> {
            VentanaProductosCarro ventanaGestionProductos = new VentanaProductosCarro();
            GestorActualizaciones.registrar(ventanaGestionProductos);
            ventanaGestionProductos.setVisible(true);
        });

        botonEliminarProducto.addActionListener(e -> {
            int selectedRow = tablaProductos.getSelectedRow();
            if (selectedRow != -1) {
                String idProducto = (String) tablaProductos.getValueAt(selectedRow, 0);

                int confirmacion = JOptionPane.showConfirmDialog(
                        this,
                        "¿Está seguro de que desea eliminar este producto?",
                        "Confirmar Eliminación",
                        JOptionPane.YES_NO_OPTION
                );

                if (confirmacion == JOptionPane.YES_OPTION) {
                    Controlador.eliminarProductoCarro(idProducto);
                    actualizarTabla();
                }
            } else {
                JOptionPane.showMessageDialog(this, "Seleccione un producto para eliminar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            }
        });

        botonEditarProducto.addActionListener(e -> {
            int selectedRow = tablaProductos.getSelectedRow();
            if (selectedRow != -1) {
                String idProducto = (String) tablaProductos.getValueAt(selectedRow, 0);
                String nombreProducto = (String) tablaProductos.getValueAt(selectedRow, 1);
                String cantidadActual = (String) tablaProductos.getValueAt(selectedRow, 3);

                JTextField campoCantidad = new JTextField(cantidadActual);

                JPanel panelEdicion = new JPanel(new GridLayout(2, 2, 10, 10));
                panelEdicion.add(new JLabel("Producto:"));
                panelEdicion.add(new JLabel(nombreProducto));
                panelEdicion.add(new JLabel("Cantidad:"));
                panelEdicion.add(campoCantidad);

                int resultado = JOptionPane.showConfirmDialog(
                        this,
                        panelEdicion,
                        "Editar Cantidad de Producto",
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

                        Controlador.editarCantidadProductoCarro(idProducto, nuevaCantidad);
                        actualizarTabla();
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(this, "Ingrese un número válido para la cantidad.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Seleccione un producto para editar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            }
        });

        panelBotonesProductos.add(botonAgregarProducto);
        panelBotonesProductos.add(Box.createVerticalStrut(10));
        panelBotonesProductos.add(botonEliminarProducto);
        panelBotonesProductos.add(Box.createVerticalStrut(10));
        panelBotonesProductos.add(botonEditarProducto);

        JPanel panelTabla = new JPanel(new BorderLayout());
        panelTabla.setOpaque(false);
        String[] columnas = {"ID", "Nombre", "Precio Unitario", "Cantidad", "Subtotal"};
        tablaProductos = GenerarComponentes.crearTabla(columnas);
        JScrollPane scrollTabla = GenerarComponentes.crearScrollPane(tablaProductos);
        panelTabla.add(scrollTabla, BorderLayout.CENTER);

        panelMetodoPago = crearPanelMetodoPago();

        botonPagar = GenerarComponentes.crearBoton("PAGAR");
        botonPagar.setPreferredSize(new Dimension(panelMetodoPago.getPreferredSize().width, 60));

        botonPagar.addActionListener(e -> mostrarVentanaCliente());

        botonAlertasStock = GenerarComponentes.crearBoton("Alertas de Stock");
        botonAlertasStock.setPreferredSize(new Dimension(150, 40));


        botonAlertasStock.addActionListener(e -> {
            // Crear la ventana para las alertas de stock
            JFrame ventanaAlertas = GenerarVentanaFormulario.crearVentana("Alertas de Stock", 800, 600);
        
            // Crear un panel para la tabla
            JPanel panelAlertas = new JPanel(new BorderLayout(10, 10));
            panelAlertas.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
            panelAlertas.setOpaque(false);
        
            // Definir las columnas para la tabla de alertas
            String[] columnasStrings = {"ID", "Nombre", "Cantidad", "Nivel de Alerta"};
        
            // Consultar datos desde las tablas relacionadas
            ArrayList<String[]> productos = Controlador.consultar(
                "PRODUCTO_INVENTARIO",
                "PRODUCTOS",
                "PRODUCTOS.ID_PRODUCTO, PRODUCTOS.NOMBRE, PRODUCTO_INVENTARIO.CANTIDAD, ROUND((PRODUCTO_INVENTARIO.CANTIDAD / 10) + (PRODUCTOS.GRADO_RELEVANCIA / 2), 2) AS NIVEL_ALERTA",
                "PRODUCTOS.ID_PRODUCTO = PRODUCTO_INVENTARIO.ID_PRODUCTO"
            );
        
            // Validar que los datos obtenidos tengan el número correcto de columnas
            int numColumnas = columnasStrings.length;
            String[][] datos = new String[productos.size()][numColumnas];
            for (int i = 0; i < productos.size(); i++) {
                String[] fila = productos.get(i);
                // Asegurarse de que la fila tiene el número correcto de columnas
                if (fila.length == numColumnas) {
                    datos[i] = fila;
                } else {
                    // Completar con valores predeterminados en caso de inconsistencia
                    datos[i] = new String[numColumnas];
                    System.arraycopy(fila, 0, datos[i], 0, Math.min(fila.length, numColumnas));
                }
            }
        
            // Crear la tabla con los datos
            JTable tablaAlertas = new JTable(new DefaultTableModel(datos, columnasStrings));
            JScrollPane scrollTablaAlertas = new JScrollPane(tablaAlertas);
        
            // Ajustar columnas
            tablaAlertas.getColumnModel().getColumn(0).setPreferredWidth(80);  // ID
            tablaAlertas.getColumnModel().getColumn(1).setPreferredWidth(200); // Nombre
            tablaAlertas.getColumnModel().getColumn(2).setPreferredWidth(100); // Cantidad
            tablaAlertas.getColumnModel().getColumn(3).setPreferredWidth(120); // Nivel de Alerta
        
            // Hacer las columnas no redimensionables
            for (int i = 0; i < columnasStrings.length; i++) {
                tablaAlertas.getColumnModel().getColumn(i).setResizable(false);
            }
        
            // Añadir la tabla al panel
            panelAlertas.add(scrollTablaAlertas, BorderLayout.CENTER);
        
            // Agregar el panel al centro de la ventana
            ventanaAlertas.add(panelAlertas, BorderLayout.CENTER);
        
            // Hacer visible la ventana
            ventanaAlertas.setVisible(true);
        });
        
        
        JPanel panelInferior = new JPanel(new BorderLayout(10, 10));
        panelInferior.setOpaque(false);
        JPanel panelPagoYPagar = GenerarComponentes.crearPanelFlowLayout(FlowLayout.LEFT, 10, 10);
        panelPagoYPagar.add(panelMetodoPago);
        panelPagoYPagar.add(botonPagar);
        panelInferior.add(panelPagoYPagar, BorderLayout.WEST);
        panelInferior.add(botonAlertasStock, BorderLayout.EAST);



        add(panelBotonesProductos, BorderLayout.WEST);
        add(panelTabla, BorderLayout.CENTER);
        add(panelInferior, BorderLayout.SOUTH);


    }

    private void mostrarVentanaCliente() {
        JFrame ventanaCliente = GenerarVentanaFormulario.crearVentana("Nombre del Cliente", 400, 200);
        JPanel panelFormulario = GenerarVentanaFormulario.crearPanelFormulario();
        JLabel etiquetaNombre = GenerarVentanaFormulario.crearEtiqueta("Nombre del Cliente:");
        JTextField campoNombre = GenerarVentanaFormulario.crearCampoTexto(20);
        JButton botonAceptar = GenerarVentanaFormulario.crearBoton("Aceptar");

        GenerarVentanaFormulario.agregarComponente(panelFormulario, etiquetaNombre, 0, 0, 1, 1);
        GenerarVentanaFormulario.agregarComponente(panelFormulario, campoNombre, 1, 0, 2, 1);
        GenerarVentanaFormulario.agregarComponente(panelFormulario, botonAceptar, 1, 1, 1, 1);
        ventanaCliente.add(panelFormulario);

        botonAceptar.addActionListener(e -> {
            String nombreCliente = campoNombre.getText().trim();
            if (nombreCliente.isEmpty()) {
                JOptionPane.showMessageDialog(ventanaCliente, "El nombre del cliente no puede estar vacío.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            registrarVenta(nombreCliente);
            ventanaCliente.dispose();
        });

        ventanaCliente.setVisible(true);
    }

    private void registrarVenta(String nombreCliente) {
        double totalVenta = 0.0;
        int rowCount = tablaProductos.getRowCount();
        if (rowCount == 0) {
            JOptionPane.showMessageDialog(this, "No hay productos en el carrito.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String idVenta = "VENTA-" + System.currentTimeMillis();
        java.sql.Date fechaActual = new java.sql.Date(System.currentTimeMillis());
        for (int i = 0; i < rowCount; i++) {
            totalVenta += Double.parseDouble(tablaProductos.getValueAt(i, 4).toString());
        }
        String[] datosVenta = {idVenta, nombreCliente, fechaActual.toString(), String.format("%.2f", totalVenta)};
        Controlador.agregarDB("venta", null, datosVenta);

        for (int i = 0; i < rowCount; i++) {
            String idProducto = tablaProductos.getValueAt(i, 0).toString();
            int cantidad = Integer.parseInt(tablaProductos.getValueAt(i, 3).toString());
            String[] datosProductoVenta = {idVenta, idProducto, String.valueOf(cantidad)};
            Controlador.agregarDB("producto_venta", "ID_VENTA, ID_PRODUCTO, CANTIDAD", datosProductoVenta);
        }

        JOptionPane.showMessageDialog(this, "Venta registrada exitosamente.");
        ((DefaultTableModel) tablaProductos.getModel()).setRowCount(0);
    }

    private JPanel crearPanelMetodoPago() {
        JPanel panelMetodoPago = new JPanel();
        panelMetodoPago.setLayout(new BoxLayout(panelMetodoPago, BoxLayout.Y_AXIS));
        panelMetodoPago.setBorder(GenerarComponentes.crearTitledBorder("Método de Pago"));
        panelMetodoPago.setPreferredSize(new Dimension(200, 150));
        panelMetodoPago.setOpaque(false);

        radioEfectivo = new JRadioButton("Efectivo");
        radioTarjeta = new JRadioButton("Tarjeta");
        ButtonGroup grupoMetodoPago = new ButtonGroup();
        grupoMetodoPago.add(radioEfectivo);
        grupoMetodoPago.add(radioTarjeta);
        panelMetodoPago.add(radioEfectivo);
        panelMetodoPago.add(radioTarjeta);
        return panelMetodoPago;
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

    @Override
    public void actualizarTabla() {
        // Definir las columnas de la tabla
        String[] columnas = {"ID", "Nombre", "Precio Unitario", "Cantidad", "Subtotal"};

        // Obtener los productos del carrito desde el controlador
        ArrayList<String[]> productosCarro = Controlador.obtenerProductosCarro();

        // Crear una matriz para los datos de la tabla
        String[][] datos = new String[productosCarro.size()][columnas.length];
        for (int i = 0; i < productosCarro.size(); i++) {
            datos[i] = productosCarro.get(i);
        }

        // Actualizar el modelo de la tabla
        tablaProductos.setModel(new DefaultTableModel(datos, columnas));

        // Ajustar los anchos de las columnas
        tablaProductos.getColumnModel().getColumn(0).setPreferredWidth(40);  // ID
        tablaProductos.getColumnModel().getColumn(1).setPreferredWidth(150); // Nombre
        tablaProductos.getColumnModel().getColumn(2).setPreferredWidth(100); // Precio Unitario
        tablaProductos.getColumnModel().getColumn(3).setPreferredWidth(80);  // Cantidad
        tablaProductos.getColumnModel().getColumn(4).setPreferredWidth(120); // Subtotal

        // Configurar que las columnas no sean redimensionables
        for (int i = 0; i < columnas.length; i++) {
            tablaProductos.getColumnModel().getColumn(i).setResizable(false);
        }

        // Revalidar y repintar para asegurarse de que los cambios sean visibles
        revalidate();
        repaint();
    }


    
}
