package Vista.Inventario;

import javax.swing.*;
import Controlador.Controlador;
import Modelo.ModeloInventario;
import Vista.Interfaces.ActualizarTable;
import Vista.Plantillas.GenerarComponentes;
import Vista.Plantillas.GenerarVentanaFormulario;

import java.awt.*;
import java.util.ArrayList;

public class PantallaAlertasStock extends JPanel implements ActualizarTable {

    private JTextField campoBusqueda;
    private JTable tablaAlertas;
    private JButton botonEditarGradoRelevancia;

    public PantallaAlertasStock() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Margen alrededor del panel
        setOpaque(true);

        // Panel superior para el motor de búsqueda
        JPanel panelBusqueda = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panelBusqueda.setOpaque(false);
        JLabel etiquetaBusqueda = new JLabel("Buscar por Nombre:");
        campoBusqueda = GenerarComponentes.crearCampoTexto(30);
        panelBusqueda.add(etiquetaBusqueda);
        panelBusqueda.add(campoBusqueda);

        // Tabla de alertas
        String[] columnas = {"ID", "Nombre", "Grado de Relevancia", "Cantidad", "Última Importación", "Relevancia Total"};
        tablaAlertas = GenerarComponentes.crearTabla(columnas);
        JScrollPane scrollTabla = GenerarComponentes.crearScrollPane(tablaAlertas);

        // Botón para editar grado de relevancia
        botonEditarGradoRelevancia = GenerarComponentes.crearBoton("Editar Grado de Relevancia");

        botonEditarGradoRelevancia.addActionListener(e -> {
            int selectedRow = tablaAlertas.getSelectedRow();
            if (selectedRow != -1) {
                String idProducto = (String) tablaAlertas.getValueAt(selectedRow, 0);
        
                // Crear la ventana usando la plantilla
                JFrame ventanaRelevancia = GenerarVentanaFormulario.crearVentana("Editar Grado de Relevancia", 400, 200);
        
                // Crear el panel principal usando la plantilla
                JPanel panelFormulario = GenerarVentanaFormulario.crearPanelFormulario();
        
                // Crear componentes
                JLabel etiquetaGradoRelevancia = GenerarVentanaFormulario.crearEtiqueta("Nuevo Grado de Relevancia:");
                JTextField campoGradoRelevancia = GenerarVentanaFormulario.crearCampoTexto(20);
                JButton botonGuardar = GenerarVentanaFormulario.crearBoton("Guardar");
        
                // Agregar componentes al panel con posiciones específicas
                GenerarVentanaFormulario.agregarComponente(panelFormulario, etiquetaGradoRelevancia, 0, 0, 1, 1);
                GenerarVentanaFormulario.agregarComponente(panelFormulario, campoGradoRelevancia, 1, 0, 2, 1);
                GenerarVentanaFormulario.agregarComponente(panelFormulario, botonGuardar, 1, 1, 1, 1);
        
                // Agregar el panel al centro de la ventana
                ventanaRelevancia.add(panelFormulario, BorderLayout.CENTER);
        
                // Acción del botón Guardar
                botonGuardar.addActionListener(ev -> {
                    String nuevoGradoRelevancia = campoGradoRelevancia.getText();
                    if (nuevoGradoRelevancia != null && !nuevoGradoRelevancia.trim().isEmpty()) {
                        try {
                            int gradoRelevancia = Integer.parseInt(nuevoGradoRelevancia);
                            Controlador.modificar("PRODUCTOS", "ID_PRODUCTO", idProducto, "=", "GRADO_RELEVANCIA", String.valueOf(gradoRelevancia));
                            actualizarTabla();
                            ventanaRelevancia.dispose();
                        } catch (NumberFormatException ex) {
                            JOptionPane.showMessageDialog(ventanaRelevancia, "Por favor, ingrese un número válido.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        JOptionPane.showMessageDialog(ventanaRelevancia, "El campo no puede estar vacío.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                });
        
                // Mostrar la ventana
                ventanaRelevancia.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Por favor, seleccione una fila.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            }
        });
        


        // Panel para centrar el botón en la parte inferior
        JPanel panelBoton = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        panelBoton.setOpaque(false);
        panelBoton.add(botonEditarGradoRelevancia);

        // Añadir componentes al panel principal
        add(panelBusqueda, BorderLayout.NORTH);      // Motor de búsqueda en la parte superior
        add(scrollTabla, BorderLayout.CENTER);       // Tabla de alertas en el centro
        add(panelBoton, BorderLayout.SOUTH);         // Botón en la parte inferior
        actualizarTabla();
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
        String[] columnas = {"ID", "Nombre", "Grado de Relevancia","Cantidad", "Última Importación", "Relevancia Total"};

        ArrayList<String[]> productosInventario = Controlador.llenarDatosDB("PRODUCTO_INVENTARIO", "PRODUCTOS", "PRODUCTOS.ID_PRODUCTO, PRODUCTOS.NOMBRE, PRODUCTOS.GRADO_RELEVANCIA, PRODUCTO_INVENTARIO.CANTIDAD, PRODUCTO_INVENTARIO.ULTIMA_IMPORTACION, ROUND((PRODUCTO_INVENTARIO.CANTIDAD / 10) + PRODUCTOS.GRADO_RELEVANCIA, 0) AS resultado", "PRODUCTOS.ID_PRODUCTO = PRODUCTO_INVENTARIO.ID_PRODUCTO");
        String[][] datos = new String[productosInventario.size()][columnas.length];

        for (int i = 0; i < productosInventario.size(); i++) {

            datos[i] = productosInventario.get(i);
        }

        tablaAlertas.setModel(new javax.swing.table.DefaultTableModel(datos, columnas));

        tablaAlertas.getColumnModel().getColumn(0).setPreferredWidth(80);  // ID
        tablaAlertas.getColumnModel().getColumn(1).setPreferredWidth(200); // Nombre
        tablaAlertas.getColumnModel().getColumn(2).setPreferredWidth(120); // Grado de Relevancia
        tablaAlertas.getColumnModel().getColumn(3).setPreferredWidth(100); // Cantidad
        tablaAlertas.getColumnModel().getColumn(4).setPreferredWidth(120); // Grado por Cantidad
        tablaAlertas.getColumnModel().getColumn(5).setPreferredWidth(150); // Última Importación
        for (int i = 0; i < columnas.length; i++) {
            tablaAlertas.getColumnModel().getColumn(i).setResizable(false);
        }
        

    }


}
