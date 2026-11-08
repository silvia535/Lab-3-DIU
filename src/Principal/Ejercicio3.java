package Principal;

import org.example.ImageSelectionPanel;
import org.example.ToolButton;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


//SILVIA MIGUEL MARTÍN

public class Ejercicio3 extends JFrame implements ImageSelectionPanel.ToggleListener {

    private JPanel panelRejilla;
    private JLabel etiquetaEstado;
    private ToolButton botonEliminar;
    private ToolButton botonSeleccionarTodo;
    private List<ImageSelectionPanel> listaPanelesImagen;
    private ImageIcon iconoSeleccionarTodo;
    private ImageIcon iconoDeseleccionarTodo;
    private ImageIcon iconoEliminar;
    private boolean isSelected = true;

    public Ejercicio3() {
        setTitle("Álbum de Fotos");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLayout(new BorderLayout());

        // Creación e inicialización
        try {
            iconoSeleccionarTodo = redimensionarIcono(new ImageIcon(getClass().getResource("select_icon.png")), 24, 24);
            iconoDeseleccionarTodo = redimensionarIcono(new ImageIcon(getClass().getResource("deselect_icon.png")), 24, 24);
            iconoEliminar = redimensionarIcono(new ImageIcon(getClass().getResource("delete_icon.png")), 24, 24);
        } catch (Exception e) {
            System.err.println("Iconos no encontrados, usando fallback de texto");
            iconoSeleccionarTodo = crearIconoTexto("Seleccionar");
            iconoDeseleccionarTodo = crearIconoTexto("Deseleccionar");
            iconoEliminar = crearIconoTexto("Eliminar");
        }

        panelRejilla = new JPanel(new GridLayout(4, 3, 0, 0));
        panelRejilla.setBackground(Color.BLACK);
        listaPanelesImagen = new ArrayList<>();

        cargarImagenes();

        etiquetaEstado = new JLabel();
        etiquetaEstado.setForeground(Color.WHITE);
        etiquetaEstado.setHorizontalAlignment(JLabel.CENTER);

        botonEliminar = new ToolButton(iconoEliminar, Color.BLACK);
        botonEliminar.setEnabled(false);
        botonEliminar.addActionListener(e -> confirmarEliminacion());

        botonSeleccionarTodo = new ToolButton(iconoSeleccionarTodo, Color.BLACK);
        botonSeleccionarTodo.addActionListener(e -> alternarSeleccionGeneral());

        add(panelRejilla, BorderLayout.CENTER);
        add(crearBarraHerramientas(), BorderLayout.SOUTH);

        updateButtonStates();
        setLocationRelativeTo(null);
    }

    private JPanel crearBarraHerramientas() {
        JPanel barraHerramientas = new JPanel(new BorderLayout());
        barraHerramientas.setBackground(Color.BLACK);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBotones.setBackground(Color.BLACK);
        panelBotones.add(botonEliminar);
        panelBotones.add(botonSeleccionarTodo);

        barraHerramientas.add(panelBotones, BorderLayout.EAST);
        barraHerramientas.add(etiquetaEstado, BorderLayout.CENTER);

        return barraHerramientas;
    }

    private void cargarImagenes() {
        for (int i = 0; i < 12; i++) {
            try {
                String urlImagen = "https://picsum.photos/100/100?random=" + i;
                ImageSelectionPanel panelImg = new ImageSelectionPanel(125, 100, urlImagen,"tick_icon.png",i);
                panelImg.setToggleListener(this);
                listaPanelesImagen.add(panelImg);
                panelRejilla.add(panelImg);
            } catch (IOException e) {
                e.printStackTrace();
                JLabel etiquetaError = new JLabel("Error cargando imagen");
                panelRejilla.add(etiquetaError);
            }
        }
    }

    private void alternarSeleccionGeneral() {
        if (isSelected) {
            for (ImageSelectionPanel panel : listaPanelesImagen) {
                panel.overlayPanel.setVisible(true);
                panel.repaint();
            }
            botonSeleccionarTodo.changeIcon(iconoDeseleccionarTodo);
        } else {
            for (ImageSelectionPanel panel : listaPanelesImagen) {
                panel.overlayPanel.setVisible(false);
                panel.repaint();
            }
            botonSeleccionarTodo.changeIcon(iconoSeleccionarTodo);
        }
        isSelected = !isSelected;
        updateStatusLabel();
        updateButtonStates();
    }

    private void confirmarEliminacion() {
        int opcion = JOptionPane.showConfirmDialog(this,
                "¿Estás seguro de que quieres eliminar las imágenes seleccionadas?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION);

        if (opcion == JOptionPane.YES_OPTION) {
            for (ImageSelectionPanel panel : listaPanelesImagen) {
                panel.overlayPanel.setVisible(false);
                panel.repaint();
            }
            isSelected = true;
            botonSeleccionarTodo.changeIcon(iconoSeleccionarTodo);
            updateStatusLabel();
            updateButtonStates();
        }
    }

    private void updateStatusLabel() {
        int seleccionadas = contarImagenesSeleccionadas();
        etiquetaEstado.setText(seleccionadas > 0 ? seleccionadas + " fotos seleccionadas" : "");
    }

    private void updateButtonStates() {
        botonEliminar.setEnabled(contarImagenesSeleccionadas() > 0);
    }

    private int contarImagenesSeleccionadas() {
        int count = 0;
        for (ImageSelectionPanel panel : listaPanelesImagen) {
            if (panel.isSelected()) count++;
        }
        return count;
    }

    private ImageIcon crearIconoTexto(String texto) {
        BufferedImage imagen = new BufferedImage(80, 30, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = imagen.createGraphics();
        g2d.setColor(Color.WHITE);
        g2d.drawString(texto, 5, 20);
        g2d.dispose();
        return new ImageIcon(imagen);
    }

    private ImageIcon redimensionarIcono(ImageIcon icono, int ancho, int alto) {
        Image imagen = icono.getImage();
        Image imagenRedimensionada = imagen.getScaledInstance(ancho, alto, Image.SCALE_SMOOTH);
        return new ImageIcon(imagenRedimensionada);
    }


    public void onMouseClick(int id, boolean isSelected) {
        updateStatusLabel();
        updateButtonStates();
    }



    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Ejercicio3().setVisible(true));
    }
}
