package dev.danipraivet.Vista;

import javax.swing.*;
import java.awt.*;

public class VentanaPrincipal extends JFrame {
    JTextPane panelMensajes;
    JTextField campoEscribirMensaje;
    JButton botonEnviar;
    // JList<> usuariosConectados;

    public VentanaPrincipal() {
        initGUI();
        this.setVisible(true);
        this.setSize(300, 300);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
    }

    private void initGUI() {
        this.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        panelMensajes = new JTextPane();
        panelMensajes.setEditable(false);
        panelMensajes.setOpaque(false);
        panelMensajes.setContentType("text/html");
        campoEscribirMensaje = new JTextField(30);
        campoEscribirMensaje.setOpaque(false);
        botonEnviar = new JButton("Enviar");
        botonEnviar.addActionListener(e -> {});
        this.add(panelMensajes, c);
        c.gridy = 1;
        this.add(campoEscribirMensaje, c);
        c.gridy = 2;
        this.add(botonEnviar, c);
    }
}
