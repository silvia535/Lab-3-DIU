package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ToolButton extends JButton {
    private ImageIcon originalIcon;
    private ImageIcon hoveredIcon;
    private ImageIcon clickedIcon;
    private boolean hovered = false;
    private Color bgColor;

    public ToolButton(ImageIcon icon, Color bgColor) {
        this.originalIcon = icon;
        this.hoveredIcon = darkenIcon(icon, 0.4f);
        this.clickedIcon = darkenIcon(icon, 0.6f);
        this.bgColor = bgColor;

        setIcon(this.originalIcon);
        setBackground(bgColor);
        setOpaque(false);
        setBorderPainted(false);
        setFocusPainted(false);
        setContentAreaFilled(false);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                hovered = true;
                setIcon(hoveredIcon);
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                hovered = false;
                setIcon(originalIcon);
                repaint();
            }

            @Override
            public void mousePressed(MouseEvent e) {
                setIcon(clickedIcon);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                setIcon(hovered ? hoveredIcon : originalIcon);
            }
        });
    }

    public void changeIcon(ImageIcon newIcon) {
        this.originalIcon = newIcon;
        this.hoveredIcon = darkenIcon(newIcon, 0.4f);
        this.clickedIcon = darkenIcon(newIcon, 0.6f);
        setIcon(this.originalIcon);
        repaint();
    }

    public void changeBackground(Color newColor) {
        this.bgColor = newColor;
        setBackground(newColor);
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        g.setColor(bgColor);
        g.fillRect(0, 0, getWidth(), getHeight());
        super.paintComponent(g);
    }

    private ImageIcon darkenIcon(ImageIcon icon, float factor) {
        Image image = icon.getImage();
        BufferedImage bufferedImage = new BufferedImage(
                image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_ARGB
        );
        Graphics2D g2d = bufferedImage.createGraphics();
        g2d.drawImage(image, 0, 0, null);
        g2d.setComposite(AlphaComposite.SrcOver.derive(factor));
        g2d.setColor(new Color(0, 0, 0, 100));
        g2d.fillRect(0, 0, image.getWidth(null), image.getHeight(null));
        g2d.dispose();
        return new ImageIcon(bufferedImage);
    }
}