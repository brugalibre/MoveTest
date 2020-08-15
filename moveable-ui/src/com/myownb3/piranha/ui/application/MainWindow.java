/**
 * 
 */
package com.myownb3.piranha.ui.application;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.myownb3.piranha.core.grid.Grid;
import com.myownb3.piranha.core.grid.gridelement.GridElement;
import com.myownb3.piranha.core.grid.gridelement.obstacle.Obstacle;
import com.myownb3.piranha.launch.weapon.listener.KeyListener;
import com.myownb3.piranha.launch.weapon.listener.MouseListener;
import com.myownb3.piranha.ui.image.ImageScaler;
import com.myownb3.piranha.ui.render.Renderer;
import com.myownb3.piranha.ui.render.impl.PositionListPainter;

/**
 * @author Dominic
 * 
 */
public class MainWindow {
   private JFrame mainWindow;
   private Container content;
   private int padding;
   private int pointWidth;
   private String backgroundImage;

   public MainWindow(int width, int height, int padding, int pointWidth) {

      mainWindow = new JFrame();
      this.padding = padding;
      this.pointWidth = pointWidth;
      setLocation();
      mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      mainWindow.setPreferredSize(new Dimension(width, height));
      mainWindow.setAlwaysOnTop(true);
      Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
      int y = (screenSize.height / 2) - height / 2;
      int x = (screenSize.width / 2) - width / 2;

      content = new JPanel(new BorderLayout());
      mainWindow.add(content);
      mainWindow.setBounds(x, y, width, height);
   }

   public void withBackground(String backgroundImage) {
      this.backgroundImage = backgroundImage;
   }

   public void withImageIcon(String tankImage) {

      try {
         BufferedImage bufferedImage = ImageIO.read(new File(tankImage));
         bufferedImage = ImageScaler.scaleImage(bufferedImage, bufferedImage.getWidth() * 2d, bufferedImage.getHeight() * 2d);
         mainWindow.setIconImage(new ImageIcon(bufferedImage).getImage());
      } catch (IOException e) {
         e.printStackTrace();
         // ignore and use default
      }
   }

   public void showCollisionInfo() {
      JLabel label = new JLabel("Kollision!");
      content.add(label, BorderLayout.PAGE_END);
      mainWindow.pack();
   }

   public void showDoneInfo() {
      JLabel label = new JLabel("Fertig!");
      content.add(label, BorderLayout.PAGE_END);
      mainWindow.pack();
   }

   public void addSpielfeld(List<Renderer<?>> renderers, Grid grid) {
      addSpielfeldInternal(renderers, Collections.emptyList(), grid);
   }

   private void addSpielfeldInternal(List<Renderer<?>> renderers, List<PositionListPainter> endPositionRenderers,
         Grid grid) {
      com.myownb3.piranha.core.grid.Dimension gridDimension = grid.getDimension();
      Dimension spielfeldDimension = new Dimension(gridDimension.getWidth() + 40 + padding,
            gridDimension.getHeight() + 40 + padding);
      SpielFeld spielFeld = new SpielFeld(backgroundImage, grid, renderers, endPositionRenderers, padding, pointWidth);
      spielFeld.setPreferredSize(spielfeldDimension);
      spielFeld.setMinimumSize(spielfeldDimension);
      spielFeld.setSize(spielfeldDimension);
      content.add(spielFeld, BorderLayout.CENTER);
      mainWindow.setPreferredSize(spielfeldDimension);
      mainWindow.setMinimumSize(spielfeldDimension);
      mainWindow.pack();
   }

   public void addSpielfeld(List<Renderer<?>> renderers, List<PositionListPainter> endPositionRenderers, Grid grid) {
      addSpielfeldInternal(renderers, endPositionRenderers, grid);
   }

   public MainWindow(List<Obstacle> obstacles, List<GridElement> gridElements, int width, int height) {

      ChartSpielFeld spielFeld = new ChartSpielFeld(gridElements, obstacles);

      mainWindow = new JFrame();
      setLocation();
      mainWindow.add(spielFeld.getContent());
      mainWindow.setPreferredSize(new Dimension(width, height));
      mainWindow.pack();
      mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
   }

   private void setLocation() {
      int top = (Toolkit.getDefaultToolkit().getScreenSize().height - mainWindow.getSize().height) / 2;
      int left = (Toolkit.getDefaultToolkit().getScreenSize().width - mainWindow.getSize().width) / 2;
      mainWindow.setLocation(left, top);
   }

   public void dispose() {
      mainWindow.dispose();
   }

   public void show() {
      SwingUtilities.invokeLater(() -> {
         mainWindow.setVisible(true);
      });
   }

   public void refresh() {
      mainWindow.repaint();
   }

   public void addMouseListener(MouseListener mouseListener) {
      mainWindow.addMouseMotionListener(mouseListener);
      mainWindow.addMouseListener(mouseListener);
   }

   public void addKeyListener(KeyListener keyListener) {
      mainWindow.addKeyListener(keyListener);
   }
}
