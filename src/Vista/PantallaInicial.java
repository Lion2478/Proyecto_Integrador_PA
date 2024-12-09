package Vista;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class PantallaInicial extends JFrame {

    private JPanel panelPrincipal;
    private CardLayout layoutPantallas;

    public PantallaInicial(ActionListener controlador) {
        setTitle("Sistema Integral de Ventas");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        layoutPantallas = new CardLayout();
        panelPrincipal = new JPanel(layoutPantallas); // Aplicar CardLayout a panelPrincipal
        add(panelPrincipal, BorderLayout.CENTER);

        crearBarraDeMenu(controlador);
    }

    private void crearBarraDeMenu(ActionListener controlador) {
        JMenuBar barraMenu = new JMenuBar();

        // Menú Ventas
        JMenu ventasMenu = new JMenu("Ventas");
        agregarItemDeMenu(ventasMenu, "Nueva Venta", controlador);
        agregarItemDeMenu(ventasMenu, "Devoluciones", controlador);
        barraMenu.add(ventasMenu);

        // Menú Productos
        JMenu productosMenu = new JMenu("Productos");
        agregarItemDeMenu(productosMenu, "Gestionar", controlador);
        barraMenu.add(productosMenu);

        // Menú Inventario
        JMenu inventarioMenu = new JMenu("Inventario");
        agregarItemDeMenu(inventarioMenu, "Consultar Inventario", controlador);
        agregarItemDeMenu(inventarioMenu, "Alertas de Stock", controlador);
        barraMenu.add(inventarioMenu);

        // Menú Reportes
        JMenu reportesMenu = new JMenu("Reportes");
        agregarItemDeMenu(reportesMenu, "Generar Reporte", controlador);
        barraMenu.add(reportesMenu);

        // Menú Empleados
        JMenu empleadosMenu = new JMenu("Empleados");
        agregarItemDeMenu(empleadosMenu, "Gestionar Empleados", controlador);
        barraMenu.add(empleadosMenu);

        // Menú "Cerrar" como opción separada
        JMenu menuCerrar = new JMenu("Cerrar");
        agregarItemDeMenu(menuCerrar, "Cerrar", controlador); 
        barraMenu.add(menuCerrar);

        setJMenuBar(barraMenu);
    }

    private void agregarItemDeMenu(JMenu menu, String nombreItem, ActionListener controlador) {
        JMenuItem item = new JMenuItem(nombreItem);
        item.setActionCommand(nombreItem);
        item.addActionListener(controlador);
        menu.add(item);
    }

    public void agregarPantalla(String nombrePantalla, JPanel pantalla) {
        panelPrincipal.add(pantalla, nombrePantalla);
    }

    public void mostrarPantalla(String nombrePantalla) {
        layoutPantallas.show(panelPrincipal, nombrePantalla);
    }
}
