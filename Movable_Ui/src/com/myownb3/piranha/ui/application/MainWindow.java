/**
 * 
 */
package com.myownb3.piranha.ui.application;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

import com.myownb3.piranha.grid.gridelement.GridElement;
import com.myownb3.piranha.grid.gridelement.Obstacle;
import com.myownb3.piranha.ui.render.Renderer;

/**
 * @author Dominic
 * 
 */
public class MainWindow {
    private JFrame mainWindow;

    public MainWindow(int width, int height) {

	mainWindow = new JFrame();
	setLocation();
	mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	mainWindow.setPreferredSize(new Dimension(width, height));

	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	int y = (screenSize.height / 2) - height / 2;
	int x = (screenSize.width / 2) - width / 2;
	mainWindow.setBounds(x, y, width, height);
    }

    public void showCollisionInfo() {
	mainWindow.removeAll();
	JLabel label = new JLabel("Kollision!");
	mainWindow.add(label);
	mainWindow.pack();
    }

    public void addSpielfeld(List<Renderer> renderer, int width, int height) {
	SpielFeld spielFeld = new SpielFeld(renderer);
	spielFeld.setPreferredSize(new Dimension(width, height));
	mainWindow.add(spielFeld);
	mainWindow.pack();
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
}
