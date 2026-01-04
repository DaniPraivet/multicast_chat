package dev.danipraivet.Modelo;

import java.util.ArrayList;
import java.util.List;

public class GestorMensajes {
    private List<Mensaje> mensajes;
    private static final int LIMITE_MENSAJES = 1000;

    public GestorMensajes() {
        this.mensajes = new ArrayList<>();
    }

    public void agregarMensaje(Mensaje mensaje) {
        if (mensaje != null) {
            mensajes.add(mensaje);
            if (mensajes.size() >= LIMITE_MENSAJES){
                mensajes.remove(0);
            }
        }
    }

    public List<Mensaje> obtenerTodosMensajes() {
        return new ArrayList<>(mensajes);
    }

    public List<Mensaje> obtenerUltimosMensajes(int cantidad) {
        if (cantidad <= 0) {
            return new ArrayList<>();
        }
        int size = mensajes.size();
        if (cantidad >= size) {
            return new ArrayList<>(mensajes);
        }

        return new ArrayList<>(mensajes.subList(size - cantidad, size));
    }

    public Mensaje obtenerUltimoMensaje() {
        if (mensajes.isEmpty()) {
            return null;
        }
        return mensajes.getLast();
    }

    public int obtenerCantidadMensajes() {
        return mensajes.size();
    }

    public void limpiarHistorial() {
        mensajes.clear();
    }

    public List<Mensaje> buscarMensajesPorUsuario(String nombreUsuario) {
        List<Mensaje> resultado = new ArrayList<>();
        for (Mensaje mensaje : mensajes) {
            if (mensaje.getNombreUsuario().equals(nombreUsuario)) {
                resultado.add(mensaje);
            }
        }
        return resultado;
    }


}
