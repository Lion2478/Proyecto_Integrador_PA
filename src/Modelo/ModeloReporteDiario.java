package Modelo;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ModeloReporteDiario {

    private int idVenta;
    private String hora;
    private String producto;
    private int cantidad;
    private double montoTotal;

    private static List<ModeloReporteDiario> ventasDiarias = new ArrayList<>();

    public ModeloReporteDiario(int idVenta, String hora, String producto, int cantidad, double montoTotal) {
        this.idVenta = idVenta;
        this.hora = hora;
        this.producto = producto;
        this.cantidad = cantidad;
        this.montoTotal = montoTotal;
    }

    public static List<ModeloReporteDiario> obtenerVentasDiarias() {
        return ventasDiarias;
    }

    public static void agregarVenta(ModeloReporteDiario venta) {
        ventasDiarias.add(venta);
    }

    public static boolean exportarReporteDiario(String rutaArchivo) {
        try (FileWriter writer = new FileWriter(rutaArchivo)) {
            writer.write("ID Venta,Hora,Producto,Cantidad,Monto Total\n");
            for (ModeloReporteDiario venta : ventasDiarias) {
                writer.write(venta.idVenta + "," + venta.hora + "," + venta.producto + "," + venta.cantidad + "," + venta.montoTotal + "\n");
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Getters
    public int getIdVenta() { return idVenta; }
    public String getHora() { return hora; }
    public String getProducto() { return producto; }
    public int getCantidad() { return cantidad; }
    public double getMontoTotal() { return montoTotal; }
}



