package dev.danipraivet.Controlador;

import dev.danipraivet.Modelo.ConexionMulticast;
import dev.danipraivet.Modelo.Mensaje;

import java.io.IOException;
import java.net.SocketTimeoutException;

public class ReceptorMensajes implements Runnable {
    private ConexionMulticast conexion;
    private ControladorChat controlador;
    private volatile boolean ejecutando;

    public ReceptorMensajes(ConexionMulticast conexion, ControladorChat controlador) {
        this.conexion = conexion;
        this.controlador = controlador;
        this.ejecutando = true;
    }

    @Override
    public void run() {
        System.out.println("Receptor de mensajes iniciado");


        try {
            conexion.setSoTimeout(1000);
        } catch (Exception e) {
            System.err.println("Error al configurar timeout: " + e.getMessage());
        }


        while (ejecutando) {
            try {

                Mensaje mensaje = conexion.recibirMensaje();

                if (mensaje != null) {
                    controlador.recibirMensaje(mensaje);
                }

            } catch (SocketTimeoutException e) {
                continue;
            } catch (IOException e) {
                if (ejecutando) {
                    System.err.println("Error al recibir mensaje: " + e.getMessage());
                }
                if (ejecutando) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                }
            }
        }

        System.out.println("Receptor de mensajes detenido");
    }

    public void detener() {
        ejecutando = false;
    }

    public boolean estaEjecutando() {
        return ejecutando;
    }
}