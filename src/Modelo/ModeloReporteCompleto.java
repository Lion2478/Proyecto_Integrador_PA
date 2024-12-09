package Modelo;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ModeloReporteCompleto {

    private int idVenta;
    private String fecha;
    private String producto;
    private int cantidad;
    private double montoTotal;

    private static List<ModeloReporteCompleto> ventasTotales = new ArrayList<>();

    public ModeloReporteCompleto(int idVenta, String fecha, String producto, int cantidad, double montoTotal) {
        this.idVenta = idVenta;
        this.fecha = fecha;
        this.producto = producto;
        this.cantidad = cantidad;
        this.montoTotal = montoTotal;
    }

    public static List<ModeloReporteCompleto> obtenerVentasTotales() {
        return ventasTotales;
    }

    public static List<ModeloReporteCompleto> obtenerVentasPorPeriodo(String periodo) {
        LocalDate hoy = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        return ventasTotales.stream().filter(venta -> {
            LocalDate fechaVenta = LocalDate.parse(venta.fecha, formatter);
            switch (periodo) {
                case "Hoy":
                    return fechaVenta.equals(hoy);
                case "Última Semana":
                    return fechaVenta.isAfter(hoy.minusDays(7)) && !fechaVenta.isAfter(hoy);
                case "Último Mes":
                    return fechaVenta.isAfter(hoy.minusDays(30)) && !fechaVenta.isAfter(hoy);
                default:
                    return true;
            }
        }).collect(Collectors.toList());
    }

    public static boolean exportarReporteCompleto(String rutaArchivo) {
        try (FileWriter writer = new FileWriter(rutaArchivo)) {
            writer.write("ID Venta,Fecha,Producto,Cantidad,Monto Total\n");
            for (ModeloReporteCompleto venta : ventasTotales) {
                writer.write(venta.idVenta + "," + venta.fecha + "," + venta.producto + "," + venta.cantidad + "," + venta.montoTotal + "\n");
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Getters
    public int getIdVenta() { return idVenta; }
    public String getFecha() { return fecha; }
    public String getProducto() { return producto; }
    public int getCantidad() { return cantidad; }
    public double getMontoTotal() { return montoTotal; }
}

