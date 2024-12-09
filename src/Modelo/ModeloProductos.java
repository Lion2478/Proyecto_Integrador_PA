package Modelo;

import java.util.ArrayList;
import java.util.List;

public class ModeloProductos {

    private String idProducto;
    private String nombreProducto;
    private String categoria;
    private double precioProducto;
    private int GradoRelevancia;

    private static List<ModeloProductos> listaProductos = new ArrayList<>();

    public ModeloProductos(String id, String nombre, String categoria, double precio, int GradoRelevancia) {
        this.idProducto = id;
        this.nombreProducto = nombre;
        this.categoria = categoria;
        this.precioProducto = precio;
        this.GradoRelevancia = GradoRelevancia;
    }

    
    

    public static List<ModeloProductos> obtenerListaProductos() {
        return listaProductos;
    }


    // Getters
    public String getNombreProducto() { return nombreProducto; }
    public String getCategoria() { return categoria; }
    public double getPrecioProducto() { return precioProducto; }
}
