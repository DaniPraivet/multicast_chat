package dev.danipraivet.Modelo;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Mensaje implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final String SEPARADOR = ":::"; // Separador simple

    private String contenido;
    private String nombreUsuario;
    private LocalDateTime timestamp;

    public Mensaje(String contenido, String nombreUsuario) {
        this.contenido = contenido;
        this.nombreUsuario = nombreUsuario;
        this.timestamp = LocalDateTime.now();
    }

    public String getContenido() {
        return contenido;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String aTextoTransmision() {
        return nombreUsuario + SEPARADOR + contenido;
    }

    // Parseo SIMPLE
    public static Mensaje desdeTextoTransmision(String textoRecibido) {
        try {
            System.out.println("DEBUG - Recibido: [" + textoRecibido + "]");

            if (textoRecibido == null || textoRecibido.trim().isEmpty()) {
                return null;
            }

            String[] partes = textoRecibido.split(":::", 2);

            if (partes.length != 2) {
                System.err.println("ERROR - Se esperaban 2 partes, se encontraron: " + partes.length);
                return null;
            }

            String nombreUsuario = partes[0].trim();
            String contenido = partes[1];

            System.out.println("DEBUG - Usuario: [" + nombreUsuario + "]");
            System.out.println("DEBUG - Contenido: [" + contenido + "]");

            return new Mensaje(contenido, nombreUsuario);

        } catch (Exception e) {
            System.err.println("Error al parsear: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public String formatearParaChat() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        return "[" + timestamp.format(formatter) + "] " + nombreUsuario + ": " + contenido;
    }

    @Override
    public String toString() {
        return formatearParaChat();
    }
}