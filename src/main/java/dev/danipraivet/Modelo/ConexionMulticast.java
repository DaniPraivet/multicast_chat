package dev.danipraivet.Modelo;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;


public class ConexionMulticast {
    private MulticastSocket socket;
    private InetAddress grupoMulticast;
    private int puerto;
    private boolean conectado;
    private static final int TAMANIO_BUFFER = 8192;

    public ConexionMulticast(String direccionGrupo, int puerto) throws IOException {
        this.puerto = puerto;
        this.grupoMulticast = InetAddress.getByName(direccionGrupo);
        this.conectado = false;
    }

    public void conectar() throws IOException {
        if (conectado) {
            System.out.println("Ya estas conectado.");
            return;
        }

        socket = new MulticastSocket(puerto);

        socket.joinGroup(grupoMulticast);

        conectado = true;
        System.out.println("Conectado al grupo: " + grupoMulticast + ":" + puerto);
    }

    public void enviarMensaje(Mensaje mensaje) throws IOException {
        if (!conectado) {
            throw new IllegalStateException("No estas conectado al grupo.");
        }
        String texto = mensaje.aTextoTransmision();
        byte[] buffer = texto.getBytes("UTF-8");

        DatagramPacket paquete = new DatagramPacket(buffer, buffer.length, grupoMulticast, puerto);
    }

    public Mensaje recibirMensaje() throws IOException {
        if (!conectado) {
            throw new IllegalStateException("No estas conectado al grupo.");
        }

        byte[] buffer = new byte[TAMANIO_BUFFER];
        DatagramPacket paquete = new DatagramPacket(buffer, buffer.length);

        socket.receive(paquete);

        String texto = new String(paquete.getData(), 0, paquete.getLength(), "UTF-8");

        return Mensaje.desdeTextoTransmision(texto);
    }

    public void desconectar() throws IOException {
        if (!conectado) {
            throw new IllegalStateException("No estas conectado.");
        }

        if (socket != null) {
            socket.leaveGroup(grupoMulticast);
            socket.close();
        }
        conectado = false;
        System.out.println("Desconectado al grupo: " + grupoMulticast);
    }

    public boolean estaConectado() {
        return conectado;
    }

    public String getDireccionGrupo() {
        return grupoMulticast.getHostAddress();
    }

    public int getPuerto() {
        return puerto;
    }

    public void setSoTimeout(int timeout) throws SocketException {
        if (socket != null) {
            socket.setSoTimeout(timeout);
        }
    }
}
