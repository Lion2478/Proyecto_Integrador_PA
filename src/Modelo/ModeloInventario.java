package Modelo;


public class ModeloInventario extends ModeloProductos{
    

    private String fechaUltimaImportacion;
    private int cantidadDisponible;

    public ModeloInventario(String idProducto, String nombre,double precioProducto, int cantidadDisponible, String fechaUltimaImportacion, int gradoRelevancia, String categoria) {
        super(idProducto, nombre, categoria, precioProducto, gradoRelevancia);
        this.fechaUltimaImportacion = fechaUltimaImportacion;
        this.cantidadDisponible = cantidadDisponible;
    }

    // Getters y setters
    public String getFechaUltimaImportacion() {
        return fechaUltimaImportacion;
    }

    public void setFechaUltimaImportacion(String fechaUltimaImportacion) {
        this.fechaUltimaImportacion = fechaUltimaImportacion;
    }

    public int getCantidadDisponible() {
        return cantidadDisponible;
    }

    public void setCantidadDisponible(int cantidadDisponible) {
        this.cantidadDisponible = cantidadDisponible;
    }
    
    
}
