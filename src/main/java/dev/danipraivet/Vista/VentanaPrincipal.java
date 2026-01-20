package dev.danipraivet.Vista;


import javax.swing.*;
import java.awt.*;

public class VentanaPrincipal extends JFrame {
    private PanelChat panelChat;
    private PanelUsuarios panelUsuarios;

    public VentanaPrincipal(String nombreUsuario) {
        configurarVentana(nombreUsuario);
        inicializarComponentes();
        ensamblarVentana();
    }

    private void configurarVentana(String nombreUsuario) {
        setTitle("Chat Multicast - " + nombreUsuario);
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centrar en pantalla
        setMinimumSize(new Dimension(600, 400));
    }

    private void inicializarComponentes() {
        panelChat = new PanelChat();
        panelUsuarios = new PanelUsuarios();
    }

    private void ensamblarVentana() {
        setLayout(new BorderLayout());


        add(panelChat, BorderLayout.CENTER);


        add(panelUsuarios, BorderLayout.EAST);
    }


    public PanelChat getPanelChat() {
        return panelChat;
    }

    public PanelUsuarios getPanelUsuarios() {
        return panelUsuarios;
    }


    public void mostrar() {
        setVisible(true);
    }


    public void cerrar() {
        dispose();
    }
}