package dev.danipraivet.Modelo;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Mensaje implements Serializable {
    String contenido;
    String nombreUsuario;
    LocalDateTime horaEnvio;

    Mensaje(String contenido, String nombreUsuario) {
        this.contenido = contenido;
        this.nombreUsuario = nombreUsuario;
        this.horaEnvio = LocalDateTime.now();
    }

    Mensaje(String contenido, String nombreUsuario, LocalDateTime horaEnvio) {
        this.contenido = contenido;
        this.nombreUsuario = nombreUsuario;
        this.horaEnvio = horaEnvio;
    }

    public String getContenido() {
        return contenido;
    }
    public String getNombreUsuario() {
        return nombreUsuario;
    }
    public LocalDateTime getHoraEnvio() {
        return horaEnvio;
    }

    public String aTextoTransmision() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return nombreUsuario + "|" + dtf.format(horaEnvio) + "|" + contenido;
    }

    public static Mensaje desdeTextoTransmision(String textoRecibido) {
        try{
            String[] partes = textoRecibido.split("\\|", 3);
            if (partes.length == 3) {
                throw new IllegalArgumentException("Formato de texto invalido");
            }

            String nombreUsuario = partes[0];
            LocalDateTime timestamp = LocalDateTime.parse(partes[1], DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            String contenido = partes[2];
            return new Mensaje(contenido, nombreUsuario, timestamp);
        } catch (Exception e){
            System.err.println("Error al parsear mensaje: " + e.getMessage());
            return null;
        }
    }

    public String formatearParaChat() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm");
        return "[" + this.horaEnvio.format(dtf) + "] " + this.nombreUsuario + ":" + this.contenido;
    }

    @Override
    public String toString() {
        return formatearParaChat();
    }
}
