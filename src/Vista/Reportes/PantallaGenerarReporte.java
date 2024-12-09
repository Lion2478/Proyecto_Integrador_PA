package Vista.Reportes;


import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import Controlador.Controlador;
import Vista.Interfaces.ActualizarTable;
import Vista.Plantillas.GenerarComponentes;
import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class PantallaGenerarReporte extends JPanel implements ActualizarTable {

    private JComboBox<String> comboPeriodos;
    private JComboBox<String> comboTipo;
    private JTable tablaReportes;
    private JButton botonExportar;

    public PantallaGenerarReporte(String rolUsuario) {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Margen general
        setOpaque(true);

        // Panel superior para los combobox
        JPanel panelCombos = GenerarComponentes.crearPanelFlowLayout(FlowLayout.CENTER, 20, 10);

        // Crear los combobox usando GenerarComponentes
        comboPeriodos = GenerarComponentes.crearComboBox(
                new String[]{"Hoy", "Última Semana", "Último Mes", "Histórico"},
                "Períodos de Tiempo"
        );

        if (rolUsuario.equals("Jefe de Inventario")) {
            comboTipo = GenerarComponentes.crearComboBox(
                    new String[]{"Inventario"},
                    "Tipo"
            );
        } else {
            comboTipo = GenerarComponentes.crearComboBox(
                    new String[]{"Inventario", "Administradores", "Ventas"},
                    "Tipo"
            );
        }

        panelCombos.add(comboPeriodos);
        panelCombos.add(comboTipo);

        // Crear la tabla usando GenerarComponentes
        tablaReportes = GenerarComponentes.crearTabla(
                new String[]{"Columna1", "Columna2", "Columna3", "Columna4", "Columna5", "Columna6"}
        );
        JScrollPane scrollTabla = GenerarComponentes.crearScrollPane(tablaReportes);

        // Crear el botón usando GenerarComponentes
        botonExportar = GenerarComponentes.crearBoton("Exportar");
        JPanel panelBoton = GenerarComponentes.crearPanelFlowLayout(FlowLayout.CENTER, 0, 0);
        panelBoton.add(botonExportar);

        // Añadir componentes al panel principal
        add(panelCombos, BorderLayout.NORTH);
        add(scrollTabla, BorderLayout.CENTER);
        add(panelBoton, BorderLayout.SOUTH);

        // Agregar listener al comboBox para actualizar la tabla según la selección
        comboTipo.addActionListener(e -> actualizarTabla());
        comboPeriodos.addActionListener(e -> actualizarTabla());

        // Acción del botón para exportar el reporte
        botonExportar.addActionListener(e -> exportarReportePDF());

        actualizarTabla();
    }

    private void exportarReportePDF() {
        try {
            // Ruta al archivo .jrxml
            String tipoReporte = (String) comboTipo.getSelectedItem();
            String rutaJrxml = "src/Vista/Reportes/Reporte" + tipoReporte + ".jrxml";
    
            // Compilar el archivo .jrxml en un archivo .jasper
            String rutaJasper = "src/Vista/Reportes/Reporte" + tipoReporte + ".jasper";
            JasperCompileManager.compileReportToFile(rutaJrxml, rutaJasper);
    
            // Cargar el reporte compilado (.jasper)
            JasperReport reporte = (JasperReport) JRLoader.loadObjectFromFile(rutaJasper);
    
            // Obtener datos de la tabla
            ArrayList<Map<String, String>> datos = obtenerDatosParaReporte();

            
            
            // Crear el JRBeanCollectionDataSource
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(datos);
            
            // Imprimir todos los datos del dataSource
            for (Map<String, String> dato : datos) {
                for (Map.Entry<String, String> entry : dato.entrySet()) {
                    System.out.println(entry.getKey() + ": " + entry.getValue());
                }
            }

            

            // Parámetros para el reporte
            Map<String, Object> parametros = new HashMap<>();
            parametros.put("titulo", "Reporte de " + tipoReporte);
            parametros.put("REPORT_DATE", new Date());

    
            // Rellenar el reporte
            JasperPrint print = JasperFillManager.fillReport(reporte, parametros, dataSource);
            
            // Exportar a PDF
            String archivoSalida = "Reporte_" + tipoReporte + ".pdf";
            JasperExportManager.exportReportToPdfFile(print, archivoSalida);
    
            JOptionPane.showMessageDialog(this, "Reporte exportado exitosamente a " + archivoSalida, "Éxito", JOptionPane.INFORMATION_MESSAGE);
    
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al generar el reporte: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    


    
    

    private ArrayList<Map<String, String>> obtenerDatosParaReporte() {
    ArrayList<Map<String, String>> listaDatos = new ArrayList<>();

    DefaultTableModel modelo = (DefaultTableModel) tablaReportes.getModel();
    int columnas = modelo.getColumnCount();
    int filas = modelo.getRowCount();

    // Iterar sobre las filas y columnas de la tabla
    for (int i = 0; i < filas; i++) {
        Map<String, String> fila = new HashMap<>();
        for (int j = 0; j < columnas; j++) {
            String columna = modelo.getColumnName(j);
            String valor = String.valueOf(modelo.getValueAt(i, j));

            System.out.println("Columna: " + columna + ", Valor: " + valor);

            

            fila.put(columna, valor);
        }
        listaDatos.add(fila);
    }

    return listaDatos;
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
        String tipoReporte = (String) comboTipo.getSelectedItem();
        String periodo = (String) comboPeriodos.getSelectedItem();

        String[] columnas;
        String[][] datos;

        switch (tipoReporte) {
            case "Inventario":
                columnas = new String[]{"ID_Producto", "Producto", "Última_Importación", "Cantidad"};
                datos = obtenerDatos("Inventario", periodo);
                break;

            case "Administradores":
                columnas = new String[]{"ID_Empleado", "Nombre", "Inicio_Contrato", "Final_Contrato", "Rol"};
                datos = obtenerDatos("Administradores", periodo);
                break;

            case "Ventas":
                columnas = new String[]{"ID_Venta", "Nombre del Cliente", "Fecha", "Total"};
                datos = obtenerDatos("Ventas", periodo);
                break;

            default:
                columnas = new String[]{}; 
                datos = new String[][]{};
        }

        DefaultTableModel modelo = new DefaultTableModel(datos, columnas);
        tablaReportes.setModel(modelo);

        ajustarTamanioColumnas(tipoReporte);
    }

    private void ajustarTamanioColumnas(String tipoReporte) {
        switch (tipoReporte) {
            case "Inventario":
                tablaReportes.getColumnModel().getColumn(0).setPreferredWidth(100); // ID_Producto
                tablaReportes.getColumnModel().getColumn(1).setPreferredWidth(200); // Producto
                tablaReportes.getColumnModel().getColumn(2).setPreferredWidth(150); // Última_Importación
                tablaReportes.getColumnModel().getColumn(3).setPreferredWidth(100); // Cantidad
                break;

            case "Administradores":
                tablaReportes.getColumnModel().getColumn(0).setPreferredWidth(100); // ID_Empleado
                tablaReportes.getColumnModel().getColumn(1).setPreferredWidth(200); // Nombre
                tablaReportes.getColumnModel().getColumn(2).setPreferredWidth(150); // Inicio_Contrato
                tablaReportes.getColumnModel().getColumn(3).setPreferredWidth(150); // Final_Contrato
                tablaReportes.getColumnModel().getColumn(4).setPreferredWidth(150); // Rol
                break;

            case "Ventas":
                tablaReportes.getColumnModel().getColumn(0).setPreferredWidth(100); // ID_Venta
                tablaReportes.getColumnModel().getColumn(1).setPreferredWidth(200); // Nombre del Cliente
                tablaReportes.getColumnModel().getColumn(2).setPreferredWidth(150); // Fecha
                tablaReportes.getColumnModel().getColumn(3).setPreferredWidth(100); // Total
                break;
        }
    }

    private String[][] obtenerDatos(String tipoReporte, String periodo) {
        ArrayList<String[]> datosLista = new ArrayList<>();
        String filtroFecha = generarFiltroFecha(periodo);

        switch (tipoReporte) {
            case "Inventario":
                datosLista = Controlador.llenarDatosDB(
                        "PRODUCTO_INVENTARIO",
                        "PRODUCTOS",
                        "PRODUCTOS.ID_PRODUCTO, PRODUCTOS.NOMBRE, PRODUCTO_INVENTARIO.ULTIMA_IMPORTACION, PRODUCTO_INVENTARIO.CANTIDAD",
                        "PRODUCTOS.ID_PRODUCTO = PRODUCTO_INVENTARIO.ID_PRODUCTO AND PRODUCTO_INVENTARIO.ULTIMA_IMPORTACION >= '" + filtroFecha + "'"
                );
                break;

            case "Administradores":
                datosLista = Controlador.consultar(
                        "EMPLEADOS",
                        null,
                        "ID_EMPLEADO, NOMBRE, INICIO_CONTRATO, FINAL_CONTRATO, ROL",
                        "INICIO_CONTRATO >= '" + filtroFecha + "'"
                );
                break;

            case "Ventas":
                datosLista = Controlador.consultar(
                        "VENTAS",
                        null,
                        "ID_VENTA, NOMBRE_CLIENTE, FECHA, TOTAL",
                        "FECHA >= '" + filtroFecha + "'"
                );
                break;
        }

        return datosLista.toArray(new String[0][0]);
    }
    public JComboBox<String> getComboTipo() {
        return comboTipo;
    }
    
    

    private String generarFiltroFecha(String periodo) {
        SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendario = Calendar.getInstance();

        switch (periodo) {
            case "Hoy":
                return formatoFecha.format(new Date());

            case "Última Semana":
                calendario.add(Calendar.DAY_OF_YEAR, -7);
                return formatoFecha.format(calendario.getTime());

            case "Último Mes":
                calendario.add(Calendar.MONTH, -1);
                return formatoFecha.format(calendario.getTime());

            case "Histórico":
                return "1900-01-01"; // Fecha muy antigua para incluir todos los datos
        }
        return formatoFecha.format(new Date());
    }
}
