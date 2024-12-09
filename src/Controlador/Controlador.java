package Controlador;

import Vista.*;
import Vista.Empleados.PantallaGestionEmpleados;
import Vista.Interfaces.GestorActualizaciones;
import Vista.Inventario.PantallaAlertasStock;
import Vista.Inventario.PantallaInventario;
import Vista.Productos.PantallaGestionProductos;
import Vista.Reportes.PantallaGenerarReporte;
import Vista.Ventas.PantallaDevoluciones;
import Vista.Ventas.PuntoDeVentaPanel;

import javax.swing.*;



import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

import Modelo.BaseDatos;
import Modelo.ConexionBD;

public class Controlador implements ActionListener {

    private PantallaInicial pantallaInicial;
    private Login login;
    private JPanel panelCentral;
    private static PuntoDeVentaPanel puntoDeVentaPanel;
    private PantallaDevoluciones pantallaDevoluciones;
    private PantallaGestionProductos pantallaGestionProductos;
    private PantallaInventario pantallaInventario;
    private PantallaAlertasStock pantallaAlertasStock;
    private PantallaGenerarReporte pantallaGenerarReporte;
    private PantallaGestionEmpleados pantallaGestionEmpleados;
    private static BaseDatos baseDatos;
    private  Connection conexion;
    private static String rolUsuario;

    public Controlador() {


        try {
            
            conexion = ConexionBD.conectarMySQL();
            baseDatos = new BaseDatos(conexion);
        } catch (Exception e) {
            System.out.println("Excepcion");
        }


        // Inicializar la pantalla de login y mostrarla
        login = new Login();
        login.setVisible(true);

        
        // Añadir el controlador como listener al botón de login
        login.getBotonLogin().addActionListener(this);
        login.getBotonLogin().setActionCommand("Login");


    }

    @Override
public void actionPerformed(ActionEvent e) {
    String command = e.getActionCommand();

    switch (command) {
        case "Login":
            realizarLogin();
            break;
        case "Nueva Venta":
            mostrarPantallaPuntoVenta();
            break;
        case "Devoluciones":
            mostrarPantallaDevoluciones();
            break;
        case "Gestionar":
            mostrarPantallaGestionProductos();
            break;
        case "Consultar Inventario":
            mostrarPantallaInventario();
            break;
        case "Alertas de Stock":
            mostrarPantallaAlertasStock();
            break;
        case "Generar Reporte":
            mostrarPantallaGenerarReporte(rolUsuario); // Pasar el rol del usuario
            break;
        case "Gestionar Empleados":
            mostrarPantallaGestionEmpleados();
            break;
        case "Cerrar":
            regresarALogin();
            break;
        default:
            JOptionPane.showMessageDialog(pantallaInicial, 
                "Funcionalidad para '" + command + "' no implementada.", 
                "En desarrollo", 
                JOptionPane.INFORMATION_MESSAGE);
            break;
    }
}

    // Método para regresar al login
    private void regresarALogin() {
        if (pantallaInicial != null) {
            pantallaInicial.dispose(); // Cierra la ventana actual
        }

        // Mostrar la pantalla de login
        login = new Login();
        login.setVisible(true);
    }


    private void realizarLogin() {
        ArrayList<String[]> cuentas = baseDatos.consultar("EMPLEADOS", "IDEMPLEADO", null);
    
        if (cuentas.isEmpty()) {
            JOptionPane.showMessageDialog(
                login,
                "No existen cuentas registradas. Entrando al sistema en modo básico.",
                "Aviso",
                JOptionPane.INFORMATION_MESSAGE
            );
            login.dispose();
    
            pantallaInicial = new PantallaInicial(this);
            pantallaInicial.setVisible(true);
            deshabilitarPestañasExcluyendo("Gestionar Empleados");
            return;
        }
    
        String idUsuario = login.getCampoUsuario().getText().trim();
        String contraseña = new String(login.getCampoContraseña().getPassword()).trim();
    
        if (idUsuario.isEmpty() || contraseña.isEmpty()) {
            JOptionPane.showMessageDialog(login, "Por favor, complete todos los campos.", "Error de Login", JOptionPane.ERROR_MESSAGE);
            return;
        }
    
        ArrayList<String[]> resultados = baseDatos.consultar(
            "EMPLEADOS",
            "IDEMPLEADO, ROL",
            "IDEMPLEADO = '" + idUsuario + "' AND CONTRASENA = '" + contraseña + "'"
        );
    
        if (resultados.isEmpty()) {
            JOptionPane.showMessageDialog(login, "Credenciales inválidas. Inténtelo de nuevo.", "Error de Login", JOptionPane.ERROR_MESSAGE);
        } else {
            rolUsuario = resultados.get(0)[1]; // Guardar el rol en la variable estática
            JOptionPane.showMessageDialog(login, "Bienvenido, has iniciado sesión como: " + rolUsuario, "Login Exitoso", JOptionPane.INFORMATION_MESSAGE);
            login.dispose();
    
            pantallaInicial = new PantallaInicial(this);
            pantallaInicial.setVisible(true);
            panelCentral = new JPanel(new BorderLayout());
            pantallaInicial.add(panelCentral, BorderLayout.CENTER);
    
            if (rolUsuario.equals("Jefe de Inventario")) {
                mostrarPantallaInventario();
            } else {
                mostrarPantallaPuntoVenta();
            }
    
            configurarPantallasSegunRol(rolUsuario);
        }
    }
    
    
    
    private void configurarPantallasSegunRol(String rol) {
        JMenuBar menuBar = pantallaInicial.getJMenuBar();
    
        for (int i = 0; i < menuBar.getMenuCount(); i++) {
            JMenu menu = menuBar.getMenu(i);
            String menuNombre = menu.getText();
            System.out.println(rol);
            switch (rol) {
                case "Cajero":
                    // Habilitar solo "Ventas" y "Cerrar"
                    System.out.println("cajero");
                    menu.setEnabled(menuNombre.equals("Ventas") || menuNombre.equals("Cerrar"));
                    break;
    
                case "Jefe de inventario":
                    // Habilitar "Inventario", "Reportes" y "Cerrar"
                    System.out.println("jefe");
                    if (menuNombre.equals("Reportes")) {
                        configurarReportesParaInventario();
                    }
                    menu.setEnabled(menuNombre.equals("Inventario") || menuNombre.equals("Reportes") || menuNombre.equals("Cerrar"));
                    break;
    

                case "Gerente":
                System.out.println("gernte");
                    // Habilitar todo
                    menu.setEnabled(true);
                    break;
    
                default:
                    // Deshabilitar todo menos "Cerrar" para usuarios sin rol
                    System.out.println("cerrar");
                    menu.setEnabled(menuNombre.equals("Cerrar"));
            }
        }
    }
    
    // Método para configurar el combo de reportes para el rol "Jefe de Inventario"
    private void configurarReportesParaInventario() {
        if (pantallaGenerarReporte != null) {
            JComboBox<String> comboTipo = pantallaGenerarReporte.getComboTipo();
            comboTipo.removeAllItems(); // Limpiar opciones
            comboTipo.addItem("Inventario"); // Agregar solo la opción Inventario
        }
    }
    
    

    private void deshabilitarPestañasExcluyendo(String menuActivo) {
        JMenuBar menuBar = pantallaInicial.getJMenuBar();
    
        for (int i = 0; i < menuBar.getMenuCount(); i++) {
            JMenu menu = menuBar.getMenu(i);
            if (!menu.getText().equals(menuActivo)) {
                menu.setEnabled(false);
            }
        }
    }
    
    
    

    public static ArrayList<String[]> llenarDatosDB(String tabla, String tabla2, String campo, String valor){
        
        if (tabla2 != null) {
            ArrayList<String[]> lista = baseDatos.consultar(tabla, tabla2, campo, valor);
            return lista;
        }else{
            ArrayList<String[]> lista = baseDatos.consultar(tabla, campo, valor);
            return lista;
        }
    }
    
    public static void agregarDB( String tabla, String campos, String[] datosProducto){
        String campo = "ID_" + tabla;
        tabla = tabla + "S";
        if (baseDatos.existe(tabla, campo, datosProducto[0])) {
            JOptionPane.showMessageDialog(null, "El producto ya existe", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }else{
            baseDatos.insertar(tabla, campos,datosProducto);
        }

        if (tabla.equals("PRODUCTOS")) {

            String[] datoString = {datosProducto[0], "0", "1969-12-31"};

            baseDatos.insertar("PRODUCTO_INVENTARIO", "ID_PRODUCTO, CANTIDAD, ULTIMA_IMPORTACION", datoString);
        }

        GestorActualizaciones.actualizarTodo();
    }

    public static void eliminarDB(String tabla, String campo, String valor){
        baseDatos.eliminar(tabla, campo, valor);
        GestorActualizaciones.actualizarTodo();
    }

    public static ArrayList<String[]> consultar(String tabla1, String tabla2, String campos, String condicion) {
        if (tabla2 != null) {
            return baseDatos.consultar(tabla1, tabla2, campos, condicion);
        }
        return baseDatos.consultar(tabla1, campos, condicion);
    }
    private static ArrayList<String[]> productosCarro = new ArrayList<>();


    public static ArrayList<String[]> obtenerProductosCarro() {
        return productosCarro;
    }

    public static boolean existeProductoEnVenta(String idVenta, String idProducto) {
        ArrayList<String[]> resultados = baseDatos.consultar(
            "PRODUCTO_VENTAS", 
            null, 
            "ID_PRODUCTO", 
            "ID_VENTA = '" + idVenta + "' AND ID_PRODUCTO = '" + idProducto + "'"
        );
        return !resultados.isEmpty();
    }
    

    public static void eliminarProductoCarro(String idProducto) {
        productosCarro.removeIf(producto -> producto[0].equals(idProducto));
    }

    public static void eliminarDB(String tabla, String campocondicion1, String valorCampo1, 
                              String campocondicion2, String valorCampo2) {
        try {
            baseDatos.eliminar(tabla, campocondicion1, valorCampo1, campocondicion2, valorCampo2);
            GestorActualizaciones.actualizarTodo(); // Actualiza todas las vistas si es necesario
        } catch (Exception e) {
            System.err.println("Error al eliminar datos: " + e.getMessage());
        }
    }


    public static void agregarProductoCarro(String idProducto, String nombre, double precioUnitario, int cantidad, String idVenta) {
        boolean productoEncontrado = false;

        // Validar si el producto ya existe en la tabla PRODUCTO_VENTAS para esta venta
        if (existeProductoEnVenta(idVenta, idProducto)) {
            // Obtener la cantidad actual del producto
            ArrayList<String[]> resultado = baseDatos.consultar(
                "PRODUCTO_VENTAS",
                null,
                "CANTIDAD",
                "ID_VENTA = '" + idVenta + "' AND ID_PRODUCTO = '" + idProducto + "'"
            );
            if (!resultado.isEmpty()) {
                int cantidadActual = Integer.parseInt(resultado.get(0)[0]);
                int nuevaCantidad = cantidadActual + cantidad;

                // Actualizar la cantidad del producto en PRODUCTO_VENTAS
                baseDatos.modificar(
                    "PRODUCTO_VENTAS",
                    "ID_VENTA", idVenta,
                    "AND ID_PRODUCTO = '" + idProducto + "'",
                    "CANTIDAD", String.valueOf(nuevaCantidad)
                );
                productoEncontrado = true;
            }
        }

        // Si no se encontró, agregar un nuevo producto al carrito
        if (!productoEncontrado) {
            String subtotal = String.valueOf(precioUnitario * cantidad);
            String[] productoVenta = {idVenta, idProducto, String.valueOf(cantidad)};
            baseDatos.insertar("PRODUCTO_VENTAS", null, productoVenta);

            String[] productoCarro = {idProducto, nombre, String.valueOf(precioUnitario), String.valueOf(cantidad), subtotal};
            productosCarro.add(productoCarro);
        }

        // Actualizar el panel de PuntoDeVenta
        actualizarPuntoDeVenta();
    }


    
    public static void editarCantidadProductoCarro(String idProducto, int nuevaCantidad) {
        for (String[] producto : productosCarro) {
            if (producto[0].equals(idProducto)) {
                double precioUnitario = Double.parseDouble(producto[2]);
                producto[3] = String.valueOf(nuevaCantidad); // Actualizar cantidad
                producto[4] = String.valueOf(precioUnitario * nuevaCantidad); // Recalcular subtotal
                break;
            }
        }
        actualizarPuntoDeVenta();
    }

    

    public static boolean modificar(String tabla, String campocondicion1, String valorCampo1, String condicion1,
                                String campocondicion2, String valorCampo2, String condicion2,
                                String campoActualizar, String valorActualizar) {
    try {
        baseDatos.modificar(tabla, campocondicion1, valorCampo1, condicion1,
                            campocondicion2, valorCampo2, condicion2,
                            campoActualizar, valorActualizar);
        return true;
    } catch (Exception e) {
        System.err.println("Error al modificar datos: " + e.getMessage());
        return false;
    }
}

    


    public static void agregarProductoCarro(String id, String nombre, String precio, int cantidad) {
        String subtotal = String.valueOf(Double.parseDouble(precio) * cantidad);
        String[] producto = {id, nombre, precio, String.valueOf(cantidad), subtotal};
        productosCarro.add(producto);
        actualizarPuntoDeVenta();
    }

    public static void registrarPuntoDeVenta(PuntoDeVentaPanel panel) {
        puntoDeVentaPanel = panel;
    }
    
    private static void actualizarPuntoDeVenta() {
        if (puntoDeVentaPanel != null) {
            puntoDeVentaPanel.actualizarTabla();
        }
    }

    
    
    public static int getCantidad(String tabla, String tabla2, String campo, String valor) {
        ArrayList<String[]> lista = Controlador.consultar(tabla, tabla2, "CATEGORIA", valor);
        return lista.size();
    }
    
    
    public static boolean modificar(String tabla, String campocondicion, String valorCampo, String condicion, String campoActualizar, String valorActualizar) {
        try {
            baseDatos.modificar(tabla, campocondicion, valorCampo, condicion, campoActualizar, valorActualizar);
            return true;
        } catch (Exception e) {
            System.err.println("Error al modificar datos: " + e.getMessage());
            return false;
        }
    }
    
    
    private void mostrarPantallaPuntoVenta() {
        if (puntoDeVentaPanel == null) {
            puntoDeVentaPanel = new PuntoDeVentaPanel();
            GestorActualizaciones.registrar(puntoDeVentaPanel);
        }
        actualizarPanelCentral(puntoDeVentaPanel);
    }

    private void mostrarPantallaDevoluciones() {
        if (pantallaDevoluciones == null) {
            pantallaDevoluciones = new PantallaDevoluciones();
            GestorActualizaciones.registrar(pantallaDevoluciones);
        }
        actualizarPanelCentral(pantallaDevoluciones);
    }

    private void mostrarPantallaGestionProductos() {
        if (pantallaGestionProductos == null) {
            pantallaGestionProductos = new PantallaGestionProductos();
            GestorActualizaciones.registrar(pantallaGestionProductos);
        }
        actualizarPanelCentral(pantallaGestionProductos);
    }

    private void mostrarPantallaInventario() {
        if (pantallaInventario == null) {
            pantallaInventario = new PantallaInventario();
            GestorActualizaciones.registrar(pantallaInventario);
        }
        actualizarPanelCentral(pantallaInventario);
    }


    
    public static void exportarAJSON(ArrayList<String[]> datos, String[] columnas, String archivoDestino) {
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(archivoDestino))) {
        writer.write("[\n");
        for (int i = 0; i < datos.size(); i++) {
            writer.write("  {\n");
            for (int j = 0; j < columnas.length; j++) {
                writer.write(String.format("    \"%s\": \"%s\"%s\n", columnas[j], datos.get(i)[j], 
                                            j < columnas.length - 1 ? "," : ""));
            }
            writer.write(i < datos.size() - 1 ? "  },\n" : "  }\n");
        }
        writer.write("]");
        JOptionPane.showMessageDialog(null, "Datos exportados a JSON con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
    } catch (IOException e) {
        JOptionPane.showMessageDialog(null, "Error al exportar a JSON: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}

public static void exportarACSV(ArrayList<String[]> datos, String[] columnas, String archivoDestino) {
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(archivoDestino))) {
        writer.write(String.join(",", columnas) + "\n");
        for (String[] fila : datos) {
            writer.write(String.join(",", fila) + "\n");
        }
        JOptionPane.showMessageDialog(null, "Datos exportados a CSV con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
    } catch (IOException e) {
        JOptionPane.showMessageDialog(null, "Error al exportar a CSV: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}

public static void exportarAXML(ArrayList<String[]> datos, String[] columnas, String archivoDestino) {
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(archivoDestino))) {
        writer.write("<inventario>\n");
        for (String[] fila : datos) {
            writer.write("  <producto>\n");
            for (int i = 0; i < columnas.length; i++) {
                writer.write(String.format("    <%s>%s</%s>\n", columnas[i], fila[i], columnas[i]));
            }
            writer.write("  </producto>\n");
        }
        writer.write("</inventario>");
        JOptionPane.showMessageDialog(null, "Datos exportados a XML con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
    } catch (IOException e) {
        JOptionPane.showMessageDialog(null, "Error al exportar a XML: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}


    private void mostrarPantallaAlertasStock() {
        if (pantallaAlertasStock == null) {
            pantallaAlertasStock = new PantallaAlertasStock();
            GestorActualizaciones.registrar(pantallaAlertasStock);
        }
        actualizarPanelCentral(pantallaAlertasStock);
    }

    private void mostrarPantallaGenerarReporte(String rolUsuario) {
        if (pantallaGenerarReporte == null || !rolUsuario.equals(this.rolUsuario)) {
            pantallaGenerarReporte = new PantallaGenerarReporte(rolUsuario);
            GestorActualizaciones.registrar(pantallaGenerarReporte);
        }
        actualizarPanelCentral(pantallaGenerarReporte);
    }
    

    private void mostrarPantallaGestionEmpleados() {
        if (pantallaGestionEmpleados == null) {
            pantallaGestionEmpleados = new PantallaGestionEmpleados();
            GestorActualizaciones.registrar(pantallaGestionEmpleados);
        }
        actualizarPanelCentral(pantallaGestionEmpleados);
    }

    
    

    private void actualizarPanelCentral(JPanel nuevoPanel) {
        if (panelCentral == null) {
            System.err.println("Error: panelCentral no está inicializado.");
            return;
        }

        panelCentral.removeAll();
        panelCentral.add(nuevoPanel, BorderLayout.CENTER);
        panelCentral.revalidate();
        panelCentral.repaint();
    }
}