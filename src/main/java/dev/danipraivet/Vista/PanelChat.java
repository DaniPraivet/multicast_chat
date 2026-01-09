package dev.danipraivet.Vista;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class PanelChat extends JPanel {
    private JTextArea areaChat;
    private JScrollPane scrollChat;
    private JTextField campoTexto;
    private JButton botonEnviar;
    private JPanel panelInferior;

    public PanelChat() {
        configurarPanel();
        inicializarComponentes();
        ensamblarPanel();
    }

    private void configurarPanel() {
        setLayout(new BorderLayout(5, 5));
        setBorder(new EmptyBorder(10, 10, 10, 10));
    }

    private void inicializarComponentes() {

        areaChat = new JTextArea();
        areaChat.setEditable(false);
        areaChat.setLineWrap(true);
        areaChat.setWrapStyleWord(true);
        areaChat.setFont(new Font("SansSerif", Font.PLAIN, 13));
        areaChat.setMargin(new Insets(5, 5, 5, 5));


        scrollChat = new JScrollPane(areaChat);
        scrollChat.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);


        campoTexto = new JTextField();
        campoTexto.setFont(new Font("SansSerif", Font.PLAIN, 13));


        botonEnviar = new JButton("Enviar");
        botonEnviar.setFocusPainted(false);


        panelInferior = new JPanel(new BorderLayout(5, 0));
        panelInferior.add(campoTexto, BorderLayout.CENTER);
        panelInferior.add(botonEnviar, BorderLayout.EAST);
    }

    private void ensamblarPanel() {

        add(scrollChat, BorderLayout.CENTER);


        add(panelInferior, BorderLayout.SOUTH);
    }


    public void agregarMensaje(String mensaje) {
        areaChat.append(mensaje + "\n");

        // Auto-scroll hacia abajo para ver el último mensaje
        areaChat.setCaretPosition(areaChat.getDocument().getLength());
    }


    public String obtenerTextoEscrito() {
        return campoTexto.getText();
    }


    public void limpiarCampoTexto() {
        campoTexto.setText("");
    }


    public void limpiarChat() {
        areaChat.setText("");
    }


    public void enfocarCampoTexto() {
        campoTexto.requestFocus();
    }


    public void habilitarControles(boolean habilitar) {
        campoTexto.setEnabled(habilitar);
        botonEnviar.setEnabled(habilitar);
    }


    public JTextField getCampoTexto() {
        return campoTexto;
    }

    public JButton getBotonEnviar() {
        return botonEnviar;
    }

    public JTextArea getAreaChat() {
        return areaChat;
    }
}