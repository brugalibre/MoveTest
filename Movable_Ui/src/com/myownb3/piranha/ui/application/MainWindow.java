/**
 * 
 */
package com.myownb3.piranha.ui.application;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import com.myownb3.piranha.grid.GridElement;
import com.myownb3.piranha.grid.Obstacle;

/**
 * @author Dominic
 * 
 */
public class MainWindow {
    private JFrame mainWindow;

    public MainWindow(List<Obstacle> obstacles, List<GridElement> gridElements, int width, int height) {

	SpielFeld spielFeld = new SpielFeld(gridElements, obstacles);

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
