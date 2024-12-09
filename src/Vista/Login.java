package Vista;

import javax.swing.*;
import java.awt.*;

public class Login extends JFrame {

    private JTextField campoUsuario;
    private JPasswordField campoContraseña;
    private JCheckBox recordarCheckBox;
    private JButton botonLogin;
    private JLabel olvidoContraseñaLabel;

    public Login() {
        setTitle("Login");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Panel principal con color de fondo degradado
        JPanel panelPrincipal = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                // Crear un fondo degradado de rosa a naranja
                Color colorInicio = new Color(255, 94, 98);
                Color colorFinal = new Color(255, 175, 123);
                GradientPaint gradiente = new GradientPaint(0, 0, colorInicio, 0, getHeight(), colorFinal);
                g2d.setPaint(gradiente);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        panelPrincipal.setLayout(new BoxLayout(panelPrincipal, BoxLayout.Y_AXIS));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        // Espacio para avatar (sin imagen)
        JLabel espacioAvatar = new JLabel("Login");
        espacioAvatar.setAlignmentX(Component.CENTER_ALIGNMENT);
        espacioAvatar.setFont(new Font("Arial", Font.BOLD, 24));
        espacioAvatar.setForeground(Color.WHITE);
        panelPrincipal.add(espacioAvatar);
        panelPrincipal.add(Box.createVerticalStrut(20));

        // Campo de usuario
        JPanel panelUsuario = new JPanel(new BorderLayout(5, 5));
        panelUsuario.setOpaque(false);
        panelUsuario.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        campoUsuario = new JTextField();
        campoUsuario.setBackground(Color.WHITE);
        campoUsuario.setForeground(Color.GRAY);
        campoUsuario.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panelUsuario.add(campoUsuario, BorderLayout.CENTER);
        panelPrincipal.add(panelUsuario);
        panelPrincipal.add(Box.createVerticalStrut(15));

        // Campo de contraseña
        JPanel panelContraseña = new JPanel(new BorderLayout(5, 5));
        panelContraseña.setOpaque(false);
        panelContraseña.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        campoContraseña = new JPasswordField();
        campoContraseña.setBackground(Color.WHITE);
        campoContraseña.setForeground(Color.GRAY);
        campoContraseña.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panelContraseña.add(campoContraseña, BorderLayout.CENTER);
        panelPrincipal.add(panelContraseña);
        panelPrincipal.add(Box.createVerticalStrut(15));

        // Panel de opciones adicionales
        JPanel panelOpciones = new JPanel(new BorderLayout());
        panelOpciones.setOpaque(false);
        recordarCheckBox = new JCheckBox("Remember me");
        recordarCheckBox.setForeground(Color.WHITE);
        recordarCheckBox.setOpaque(false);
        olvidoContraseñaLabel = new JLabel("Forgot Password?");
        olvidoContraseñaLabel.setForeground(Color.WHITE);
        panelOpciones.add(recordarCheckBox, BorderLayout.WEST);
        panelOpciones.add(olvidoContraseñaLabel, BorderLayout.EAST);
        panelPrincipal.add(panelOpciones);
        panelPrincipal.add(Box.createVerticalStrut(20));

        // Botón de Login
        botonLogin = new JButton("LOGIN");
        botonLogin.setAlignmentX(Component.CENTER_ALIGNMENT);
        botonLogin.setPreferredSize(new Dimension(200, 40));
        botonLogin.setBackground(new Color(80, 80, 80));
        botonLogin.setForeground(Color.WHITE);
        botonLogin.setFocusPainted(false);
        botonLogin.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panelPrincipal.add(botonLogin);
        
        add(panelPrincipal);
    }

    public JButton getBotonLogin() {
        return botonLogin;
    }

    public JTextField getCampoUsuario() {
        return campoUsuario;
    }

    public JPasswordField getCampoContraseña() {
        return campoContraseña;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Login loginFrame = new Login();
            loginFrame.setVisible(true);
        });
    }
}
