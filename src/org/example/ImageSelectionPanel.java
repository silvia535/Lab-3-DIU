package org.example;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

public class ImageSelectionPanel extends JPanel {

    private int finalWidth;
    private int finalHeight;
    private int id;
    public JPanel overlayPanel;
    private ToggleListener listener;

    public interface ToggleListener {
        void onMouseClick(int id, boolean isSelected);
    }

    public ImageSelectionPanel(int width, int height, String imagePath, String tickPath, int id) throws IOException {
        setLayout(new BorderLayout());
        this.finalWidth = width;
        this.finalHeight = height;
        this.id = id;

        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(finalWidth, finalHeight));
        add(layeredPane, BorderLayout.CENTER);


        BufferedImage img = ImageIO.read(new URL(imagePath));
        BufferedImage imgTick = ImageIO.read(new File(tickPath));

        double aspectRatio = (double) img.getWidth() / img.getHeight();
        int newWidth, newHeight;
        if (finalWidth / (double) finalHeight > aspectRatio) {
            newWidth = finalWidth;
            newHeight = (int) (newWidth / aspectRatio);
        } else {
            newHeight = finalHeight;
            newWidth = (int) (newHeight * aspectRatio);
        }
        Image scaledImage = img.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
        int xOffset = (finalWidth - newWidth) / 2;
        int yOffset = (finalHeight - newHeight) / 2;


        JPanel imagePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.drawImage(scaledImage, xOffset, yOffset, this);
            }
        };
        imagePanel.setBounds(0, 0, finalWidth, finalHeight);
        layeredPane.add(imagePanel, JLayeredPane.DEFAULT_LAYER);


        overlayPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setColor(new Color(0, 0, 0, 150));
                g2d.fillRect(0, 0, getWidth(), getHeight());

                int tickX = getWidth() - imgTick.getWidth() - 10;
                int tickY = getHeight() - imgTick.getHeight() - 10;
                g2d.drawImage(imgTick, tickX, tickY, this);
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            }
        };
        overlayPanel.setBounds(0, 0, finalWidth, finalHeight);
        overlayPanel.setOpaque(false);
        overlayPanel.setVisible(false);  // Por defecto no se muestra
        layeredPane.add(overlayPanel, JLayeredPane.PALETTE_LAYER);


        overlayPanel.setVisible(false);


        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                overlayPanel.setVisible(!overlayPanel.isVisible());
                if (listener != null) {
                    listener.onMouseClick(id, overlayPanel.isVisible());
                }
            }
        });
    }

    public boolean isSelected() {
        return overlayPanel.isVisible();
    }

    public void setToggleListener(ToggleListener toggleListener) {
        this.listener = toggleListener;
    }

}
