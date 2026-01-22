package dev.danipraivet.Modelo;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;

public class ConexionMulticast {
    private MulticastSocket socketReceptor;
    private MulticastSocket socketEmisor;
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
            System.out.println("Ya está conectado al grupo multicast");
            return;
        }

        socketReceptor = new MulticastSocket(puerto);
        socketReceptor.joinGroup(grupoMulticast);

        socketEmisor = new MulticastSocket();

        conectado = true;
        System.out.println("Conectado al grupo multicast: " + grupoMulticast + ":" + puerto);
    }

    public void enviarMensaje(Mensaje mensaje) throws IOException {
        if (!conectado) {
            throw new IllegalStateException("No está conectado al grupo multicast");
        }

        String textoMensaje = mensaje.aTextoTransmision();
        byte[] buffer = textoMensaje.getBytes("UTF-8");

        DatagramPacket paquete = new DatagramPacket(
                buffer,
                buffer.length,
                grupoMulticast,
                puerto
        );

        socketEmisor.send(paquete);
    }

    public Mensaje recibirMensaje() throws IOException {
        if (!conectado) {
            throw new IllegalStateException("No está conectado al grupo multicast");
        }

        byte[] buffer = new byte[TAMANIO_BUFFER];
        DatagramPacket paquete = new DatagramPacket(buffer, buffer.length);

        socketReceptor.receive(paquete);

        String textoRecibido = new String(
                paquete.getData(),
                0,
                paquete.getLength(),
                "UTF-8"
        );

        return Mensaje.desdeTextoTransmision(textoRecibido);
    }

    public void desconectar() throws IOException {
        if (!conectado) {
            return;
        }

        if (socketReceptor != null) {
            socketReceptor.leaveGroup(grupoMulticast);
            socketReceptor.close();
        }

        if (socketEmisor != null) {
            socketEmisor.close();
        }

        conectado = false;
        System.out.println("Desconectado del grupo multicast");
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
        if (socketReceptor != null) {
            socketReceptor.setSoTimeout(timeout);
        }
    }
}