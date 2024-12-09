package Modelo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ModeloUsuario {

    private int id;
    private String nombre;
    private String rol;
    private LocalDate fechaRegistro;
    private String estado;

    // Lista estática para simular almacenamiento de usuarios
    private static List<ModeloUsuario> listaUsuarios = new ArrayList<>();

    public ModeloUsuario(int id, String nombre, String rol, LocalDate fechaRegistro, String estado) {
        this.id = id;
        this.nombre = nombre;
        this.rol = rol;
        this.fechaRegistro = fechaRegistro;
        this.estado = estado;
    }

    public static List<ModeloUsuario> obtenerListaUsuarios() {
        return listaUsuarios;
    }

    public static void agregarUsuario(ModeloUsuario usuario) {
        listaUsuarios.add(usuario);
    }

    public static void editarUsuario(int id, ModeloUsuario usuarioEditado) {
        for (ModeloUsuario usuario : listaUsuarios) {
            if (usuario.getId() == id) {
                usuario.setNombre(usuarioEditado.getNombre());
                usuario.setRol(usuarioEditado.getRol());
                usuario.setEstado(usuarioEditado.getEstado());
                break;
            }
        }
    }

    public static void eliminarUsuario(int id) {
        listaUsuarios.removeIf(usuario -> usuario.getId() == id);
    }

    // Getters y Setters
    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public String getRol() { return rol; }
    public LocalDate getFechaRegistro() { return fechaRegistro; }
    public String getEstado() { return estado; }

    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setRol(String rol) { this.rol = rol; }
    public void setEstado(String estado) { this.estado = estado; }
}

