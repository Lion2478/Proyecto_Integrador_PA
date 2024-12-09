package Vista.Plantillas;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class GenerarComponentes {

    // Método para crear un JComboBox estilizado con un título
    public static JComboBox<String> crearComboBox(String[] opciones, String titulo) {
        JComboBox<String> comboBox = new JComboBox<>(opciones);
        comboBox.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(255, 94, 98), 2),
                titulo,
                TitledBorder.CENTER,
                TitledBorder.TOP,
                new Font("Arial", Font.BOLD, 12),
                new Color(255, 94, 98)
        ));
        return comboBox;
    }

    // Método para crear una JTable estilizada con un modelo no editable
    public static JTable crearTabla(String[] columnas) {
        DefaultTableModel modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Deshabilitar edición
            }
        };
        JTable tabla = new JTable(modeloTabla);
        tabla.setRowHeight(30);
        tabla.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        tabla.getTableHeader().setBackground(new Color(255, 94, 98));
        tabla.getTableHeader().setForeground(Color.WHITE);
        tabla.setFont(new Font("Arial", Font.PLAIN, 12));
        tabla.getTableHeader().setReorderingAllowed(false); // Deshabilitar reordenamiento de columnas
        tabla.setDefaultEditor(Object.class, null); // Deshabilitar edición de celdas
        return tabla;
    }

    // Método para crear un botón estilizado
    public static JButton crearBoton(String texto) {
        JButton boton = new JButton(texto) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2.dispose();
                super.paintComponent(g);
            }

            @Override
            protected void paintBorder(Graphics g) {
                g.setColor(getForeground());
                ((Graphics2D) g).setStroke(new BasicStroke(2));
                g.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
            }
        };
        boton.setOpaque(false);
        boton.setContentAreaFilled(false);
        boton.setFocusPainted(false);
        boton.setForeground(new Color(255, 94, 98)); // Texto en rosado
        boton.setBackground(Color.WHITE); // Fondo blanco
        boton.setFont(new Font("Arial", Font.BOLD, 14));
        boton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        boton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                boton.setBackground(new Color(220, 220, 220)); // Color más oscuro
            }
    
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                boton.setBackground(Color.WHITE); // Color original
            }
        });
        
        return boton;
    }
    public static JTextField crearCampoTexto(int columnas) {
        JTextField campoTexto = new JTextField(columnas);
        campoTexto.setFont(new Font("Arial", Font.PLAIN, 14));
        campoTexto.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(255, 94, 98), 2),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        return campoTexto;
    }

    // Método para crear un TitledBorder estilizado
    public static TitledBorder crearTitledBorder(String titulo) {
        return BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(255, 94, 98), 2),
                titulo,
                TitledBorder.CENTER,
                TitledBorder.TOP,
                new Font("Arial", Font.BOLD, 12),
                new Color(255, 94, 98)
        );
    }
    // Método para crear un JScrollPane con márgenes
    public static JScrollPane crearScrollPane(Component componente) {
        JScrollPane scrollPane = new JScrollPane(componente);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        return scrollPane;
    }

    // Método para crear un panel con un FlowLayout y fondo transparente
    public static JPanel crearPanelFlowLayout(int alineacion, int hgap, int vgap) {
        JPanel panel = new JPanel(new FlowLayout(alineacion, hgap, vgap));
        panel.setOpaque(false);
        return panel;
    }
    
    
}
