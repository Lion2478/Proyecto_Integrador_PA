package Vista.Empleados;

import javax.swing.*;

import Vista.Interfaces.ActualizarTable;
import Vista.Plantillas.GenerarComponentes;

import java.awt.*;

public class PantallaGestionEmpleados extends JPanel implements ActualizarTable {

    private JTextField campoBusqueda;
    private JTable tablaEmpleadosPantallaGestionEmpleados;
    private JButton botonAgregar, botonEliminar, botonModificar, botonExportar;

    public PantallaGestionEmpleados() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Margen general
        setOpaque(true);

        JPanel panelBusqueda = GenerarComponentes.crearPanelFlowLayout(FlowLayout.CENTER, 10, 10);
        panelBusqueda.setBorder(GenerarComponentes.crearTitledBorder("Buscar Empleado"));

        // Agregar un JLabel para describir el campo de texto
        JLabel etiquetaBusqueda = new JLabel("Nombre o ID:");
        etiquetaBusqueda.setFont(new Font("Arial", Font.BOLD, 14));
        etiquetaBusqueda.setForeground(new Color(255, 94, 98)); // Estilo opcional

        // Campo de búsqueda
        campoBusqueda = GenerarComponentes.crearCampoTexto(25);

        // Agregar componentes al panel de búsqueda
        panelBusqueda.add(etiquetaBusqueda);
        panelBusqueda.add(campoBusqueda);

        // Panel lateral para los botones de control
        JPanel panelBotones = new JPanel();
        panelBotones.setLayout(new BoxLayout(panelBotones, BoxLayout.Y_AXIS));
        panelBotones.setOpaque(false);

        botonAgregar = GenerarComponentes.crearBoton("Agregar");
        botonEliminar = GenerarComponentes.crearBoton("Eliminar");
        botonModificar = GenerarComponentes.crearBoton("Modificar");

        panelBotones.add(botonAgregar);
        panelBotones.add(Box.createVerticalStrut(10)); // Espacio entre botones
        panelBotones.add(botonEliminar);
        panelBotones.add(Box.createVerticalStrut(10)); // Espacio entre botones
        panelBotones.add(botonModificar);

        
        // Tabla de empleados
        String[] columnas = {"ID", "Nombre", "Rol", "Sueldo", "Turno", "Fin de Contrato"};
        tablaEmpleadosPantallaGestionEmpleados = GenerarComponentes.crearTabla(columnas);
        JScrollPane scrollTabla = GenerarComponentes.crearScrollPane(tablaEmpleadosPantallaGestionEmpleados);

        // Botón de exportar en la parte inferior izquierda
        botonExportar = GenerarComponentes.crearBoton("Exportar");
        JPanel panelBotonExportar = GenerarComponentes.crearPanelFlowLayout(FlowLayout.LEFT, 0, 0);
        panelBotonExportar.add(botonExportar);

        // Añadir componentes al panel principal
        add(panelBusqueda, BorderLayout.NORTH);        // Barra de búsqueda en la parte superior
        add(panelBotones, BorderLayout.WEST);          // Botones de control en el lado izquierdo
        add(scrollTabla, BorderLayout.CENTER);         // Tabla de empleados en el centro
        add(panelBotonExportar, BorderLayout.SOUTH);   // Botón de exportar en la parte inferior izquierda
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
        // Lógica para actualizar la tabla de ventas
        System.out.println("Actualizando tabla de ...");
        // Recarga los datos y actualiza el modelo de la tabla

        tablaEmpleadosPantallaGestionEmpleados.getColumnModel().getColumn(0).setPreferredWidth(80);  // ID
        tablaEmpleadosPantallaGestionEmpleados.getColumnModel().getColumn(1).setPreferredWidth(200); // Nombre
        tablaEmpleadosPantallaGestionEmpleados.getColumnModel().getColumn(2).setPreferredWidth(150); // Rol
        tablaEmpleadosPantallaGestionEmpleados.getColumnModel().getColumn(3).setPreferredWidth(100); // Sueldo
        tablaEmpleadosPantallaGestionEmpleados.getColumnModel().getColumn(4).setPreferredWidth(100); // Turno
        tablaEmpleadosPantallaGestionEmpleados.getColumnModel().getColumn(5).setPreferredWidth(150); // Fin de Contrato

    }
}
