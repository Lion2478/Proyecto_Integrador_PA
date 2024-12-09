package Modelo;

import java.util.ArrayList;
import java.util.List;

public class ModeloVenta {

    private int idProducto;
    private String nombreProducto;
    private double precioUnitario;
    private int cantidad;
    private double subtotal;

    private static List<ModeloVenta> carrito = new ArrayList<>();

    public ModeloVenta(int idProducto, String nombreProducto, double precioUnitario, int cantidad) {
        this.idProducto = idProducto;
        this.nombreProducto = nombreProducto;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.subtotal = precioUnitario * cantidad;
    }

    public static void agregarProductoAlCarrito(ModeloVenta producto) {
        carrito.add(producto);
    }

    public static void eliminarProductoDelCarrito(int idProducto) {
        carrito.removeIf(producto -> producto.idProducto == idProducto);
    }

    public static double procesarVenta() {
        double total = carrito.stream().mapToDouble(ModeloVenta::getSubtotal).sum();
        carrito.clear(); // Vacía el carrito después de procesar la venta
        return total;
    }

    public static List<ModeloVenta> obtenerCarrito() {
        return carrito;
    }

    // Getters
    public int getIdProducto() { return idProducto; }
    public String getNombreProducto() { return nombreProducto; }
    public double getPrecioUnitario() { return precioUnitario; }
    public int getCantidad() { return cantidad; }
    public double getSubtotal() { return subtotal; }

    
}

