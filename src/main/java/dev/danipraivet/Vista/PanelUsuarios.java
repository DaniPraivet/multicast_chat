package dev.danipraivet.Vista;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class PanelUsuarios extends JPanel {
    private DefaultListModel<String> modeloLista;
    private JList<String> listaUsuarios;
    private JScrollPane scrollLista;
    private JLabel etiquetaContador;

    public PanelUsuarios() {
        configurarPanel();
        inicializarComponentes();
        ensamblarPanel();
    }

    private void configurarPanel() {
        setLayout(new BorderLayout(5, 5));
        setPreferredSize(new Dimension(180, 0));
        setBorder(new EmptyBorder(10, 5, 10, 10));
    }

    private void inicializarComponentes() {

        modeloLista = new DefaultListModel<>();


        listaUsuarios = new JList<>(modeloLista);
        listaUsuarios.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listaUsuarios.setFont(new Font("SansSerif", Font.PLAIN, 12));


        scrollLista = new JScrollPane(listaUsuarios);
        scrollLista.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(),
                "Usuarios Conectados",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("SansSerif", Font.BOLD, 12)
        ));


        etiquetaContador = new JLabel("0 usuarios", SwingConstants.CENTER);
        etiquetaContador.setFont(new Font("SansSerif", Font.ITALIC, 11));
        etiquetaContador.setForeground(Color.GRAY);
    }

    private void ensamblarPanel() {
        add(scrollLista, BorderLayout.CENTER);
        add(etiquetaContador, BorderLayout.SOUTH);
    }


    public void agregarUsuario(String nombreUsuario) {
        if (!modeloLista.contains(nombreUsuario)) {
            modeloLista.addElement(nombreUsuario);
            actualizarContador();
        }
    }


    public void eliminarUsuario(String nombreUsuario) {
        modeloLista.removeElement(nombreUsuario);
        actualizarContador();
    }


    public void limpiarLista() {
        modeloLista.clear();
        actualizarContador();
    }


    public boolean contieneUsuario(String nombreUsuario) {
        return modeloLista.contains(nombreUsuario);
    }


    public int obtenerCantidadUsuarios() {
        return modeloLista.getSize();
    }


    private void actualizarContador() {
        int cantidad = modeloLista.getSize();
        etiquetaContador.setText(cantidad + (cantidad == 1 ? " usuario" : " usuarios"));
    }


    public String[] obtenerTodosUsuarios() {
        String[] usuarios = new String[modeloLista.getSize()];
        modeloLista.copyInto(usuarios);
        return usuarios;
    }
}