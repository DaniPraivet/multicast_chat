package dev.danipraivet.Controlador;

import dev.danipraivet.Modelo.*;
import dev.danipraivet.Vista.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

public class ControladorChat {
    private Usuario usuarioActual;
    private GestorMensajes gestorMensajes;
    private ConexionMulticast conexionMulticast;

    private VentanaPrincipal ventana;

    private ReceptorMensajes receptorMensajes;
    private Thread hiloReceptor;

    public ControladorChat(Usuario usuario, String direccionMulticast, int puerto) {
        this.usuarioActual = usuario;
        this.gestorMensajes = new GestorMensajes();

        try {
            this.conexionMulticast = new ConexionMulticast(direccionMulticast, puerto);
        } catch (IOException e) {
            mostrarError("Error al crear la conexión multicast: " + e.getMessage());
            System.exit(1);
        }

        this.ventana = new VentanaPrincipal(usuario.getNombre());
    }

    public void iniciarAplicacion() {
        try {
            conexionMulticast.conectar();

            configurarEventos();

            iniciarReceptor();

            ventana.mostrar();

            ventana.getPanelChat().agregarMensaje("=== Bienvenido al chat, " + usuarioActual.getNombre() + " ===");
            ventana.getPanelChat().agregarMensaje("Conectado al grupo: " + conexionMulticast.getDireccionGrupo() + ":" + conexionMulticast.getPuerto());
            ventana.getPanelChat().agregarMensaje(""); // Línea en blanco

            ventana.getPanelChat().enfocarCampoTexto();
            ventana.getPanelUsuarios().agregarUsuario(usuarioActual.getNombre());

            enviarMensajeEntrada();

        } catch (IOException e) {
            mostrarError("Error al conectar: " + e.getMessage());
            cerrarAplicacion();
        }
    }

    private void configurarEventos() {
        PanelChat panelChat = ventana.getPanelChat();

        panelChat.getBotonEnviar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                enviarMensaje();
            }
        });

        panelChat.getCampoTexto().addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    enviarMensaje();
                }
            }
        });

        ventana.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                cerrarAplicacion();
            }
        });
    }

    private void iniciarReceptor() {
        receptorMensajes = new ReceptorMensajes(conexionMulticast, this);
        hiloReceptor = new Thread(receptorMensajes);
        hiloReceptor.setDaemon(true); // Para que se cierre cuando termine el programa
        hiloReceptor.start();
    }

    private void enviarMensaje() {
        String texto = ventana.getPanelChat().obtenerTextoEscrito().trim();

        // Validar que el mensaje no esté vacío
        if (texto.isEmpty()) {
            return;
        }

        try {
            Mensaje mensaje = new Mensaje(texto, usuarioActual.getNombre());

            conexionMulticast.enviarMensaje(mensaje);

            ventana.getPanelChat().limpiarCampoTexto();

            ventana.getPanelChat().enfocarCampoTexto();

        } catch (IOException e) {
            mostrarError("Error al enviar el mensaje: " + e.getMessage());
        }
    }

    public void recibirMensaje(Mensaje mensaje) {
        if (mensaje != null) {
            gestorMensajes.agregarMensaje(mensaje);

            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    ventana.getPanelChat().agregarMensaje(mensaje.formatearParaChat());
                }
            });
            if (!ventana.getPanelUsuarios().contieneUsuario(mensaje.getNombreUsuario())) {
                ventana.getPanelUsuarios().agregarUsuario(mensaje.getNombreUsuario());
            }
            if (mensaje.getContenido().equals("*** Ha salido del chat ***")) {
                ventana.getPanelUsuarios().eliminarUsuario(mensaje.getNombreUsuario());
            }
        }
    }

    private void enviarMensajeEntrada() {
        try {
            Mensaje mensajeEntrada = new Mensaje("*** Ha entrado al chat ***", usuarioActual.getNombre());
            conexionMulticast.enviarMensaje(mensajeEntrada);
        } catch (IOException e) {
            System.err.println("Error al enviar mensaje de entrada: " + e.getMessage());
        }
    }

    private void enviarMensajeSalida() {
        try {
            Mensaje mensajeSalida = new Mensaje("*** Ha salido del chat ***", usuarioActual.getNombre());
            conexionMulticast.enviarMensaje(mensajeSalida);
        } catch (IOException e) {
            System.err.println("Error al enviar mensaje de salida: " + e.getMessage());
        }
    }

    private void cerrarAplicacion() {
        try {
            enviarMensajeSalida();

            if (receptorMensajes != null) {
                receptorMensajes.detener();
            }

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (conexionMulticast != null && conexionMulticast.estaConectado()) {
                conexionMulticast.desconectar();
            }

            ventana.cerrar();

            System.out.println("Aplicación cerrada correctamente");
            System.exit(0);

        } catch (IOException e) {
            System.err.println("Error al cerrar la aplicación: " + e.getMessage());
            System.exit(1);
        }
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(
                ventana,
                mensaje,
                "Error",
                JOptionPane.ERROR_MESSAGE
        );
    }

    public Usuario getUsuarioActual() {
        return usuarioActual;
    }

    public GestorMensajes getGestorMensajes() {
        return gestorMensajes;
    }
}