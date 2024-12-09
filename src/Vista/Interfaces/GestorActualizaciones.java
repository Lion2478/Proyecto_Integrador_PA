package Vista.Interfaces;

import java.util.ArrayList;

public class GestorActualizaciones {
    private static ArrayList<ActualizarTable> listeners = new ArrayList<>();

    public static void registrar(ActualizarTable ventana) {
        listeners.add(ventana);
    }

    public static void actualizarTodo() {
        for (ActualizarTable listener : listeners) {
            listener.actualizarTabla();
        }
    }
}
