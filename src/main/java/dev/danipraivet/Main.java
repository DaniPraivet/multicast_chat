package dev.danipraivet;

import dev.danipraivet.Controlador.ControladorChat;
import dev.danipraivet.Modelo.Usuario;

import javax.swing.*;

public class Main {
    private static final String DIRECCION_MULTICAST = "231.0.0.5";
    private static final int PUERTO_MULTICAST = 10000;

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        String nombreUsuario = solicitarNombreUsuario();

        if (nombreUsuario == null || nombreUsuario.trim().isEmpty()) {
            System.out.println("Nombre de usuario no válido. Cerrando aplicación.");
            System.exit(0);
        }

        Usuario usuario = new Usuario(nombreUsuario);

        ControladorChat controlador = new ControladorChat(
                usuario,
                DIRECCION_MULTICAST,
                PUERTO_MULTICAST
        );

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                controlador.iniciarAplicacion();
            }
        });
    }

    private static String solicitarNombreUsuario() {
        String nombre = null;

        while (nombre == null || nombre.trim().isEmpty()) {
            nombre = JOptionPane.showInputDialog(
                    null,
                    "Ingrese su nombre de usuario:",
                    "Chat Multicast - Login",
                    JOptionPane.QUESTION_MESSAGE
            );

            if (nombre == null) {
                return null;
            }

            if (nombre.trim().isEmpty()) {
                JOptionPane.showMessageDialog(
                        null,
                        "El nombre de usuario no puede estar vacío",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        }

        return nombre.trim();
    }
}