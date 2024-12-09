package Vista.Plantillas;

import javax.swing.*;
import java.awt.*;

public class GenerarVentanaFormulario {

    // Método para crear una ventana estilizada
    public static JFrame crearVentana(String titulo, int ancho, int alto) {
        JFrame ventana = new JFrame(titulo);
        ventana.setSize(ancho, alto);
        ventana.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        ventana.setLocationRelativeTo(null); // Centrar la ventana
        ventana.setLayout(new BorderLayout(10, 10));
        ventana.setResizable(false);

        // Estilización adicional (color de fondo, bordes)
        ventana.getContentPane().setBackground(Color.WHITE);
        return ventana;
    }

    // Método para crear un panel de formulario estilizado
    public static JPanel crearPanelFormulario() {
        JPanel panelFormulario = new JPanel();
        panelFormulario.setLayout(new GridBagLayout());
        panelFormulario.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(255, 94, 98), 2),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        panelFormulario.setBackground(Color.WHITE); // Fondo blanco
        return panelFormulario;
    }

    // Método para crear etiquetas estilizadas
    public static JLabel crearEtiqueta(String texto) {
        JLabel etiqueta = new JLabel(texto);
        etiqueta.setFont(new Font("Arial", Font.BOLD, 14));
        etiqueta.setForeground(new Color(255, 94, 98)); // Color de texto
        return etiqueta;
    }

    // Método para crear ComboBox estilizado
    public static JComboBox<String> crearComboBox(String[] opciones) {
        JComboBox<String> comboBox = new JComboBox<>(opciones);
        comboBox.setFont(new Font("Arial", Font.PLAIN, 14));
        comboBox.setForeground(new Color(255, 94, 98));
        comboBox.setBackground(Color.WHITE);
        comboBox.setBorder(BorderFactory.createLineBorder(new Color(255, 94, 98), 1));
        return comboBox;
    }
    // Método para crear campos de texto estilizados
    public static JTextField crearCampoTexto(int columnas) {
        return GenerarComponentes.crearCampoTexto(columnas); // Usa la lógica estilizada de GenerarComponentes
    }

    // Método para crear botones estilizados
    public static JButton crearBoton(String texto) {
        return GenerarComponentes.crearBoton(texto); // Usa la lógica estilizada de GenerarComponentes
    }

    // Método para agregar componentes al panel con posiciones
    public static void agregarComponente(JPanel panel, Component componente, int x, int y, int anchura, int altura) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.gridwidth = anchura;
        gbc.gridheight = altura;
        gbc.insets = new Insets(10, 10, 10, 10); // Márgenes más amplios
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(componente, gbc);
    }
}
