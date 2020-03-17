/**
 * 
 */
package com.myownb3.piranha.ui.application;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.myownb3.piranha.grid.Grid;
import com.myownb3.piranha.grid.gridelement.GridElement;
import com.myownb3.piranha.grid.gridelement.Obstacle;
import com.myownb3.piranha.ui.render.Renderer;

/**
 * @author Dominic
 * 
 */
public class MainWindow {
    private JFrame mainWindow;
    private Container content;
    private int padding; 
    private int pointWidth; 
    
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

    public void showCollisionInfo() {
	JLabel label = new JLabel("Kollision!");
	content.add(label, BorderLayout.PAGE_END);
	mainWindow.pack();
    }

    public void addSpielfeld(List<Renderer> renderers, Grid grid) {
	com.myownb3.piranha.grid.Dimension gridDimension = grid.getDimension();
	Dimension spielfeldDimension = new Dimension(gridDimension.getWidth() + 3 * padding,
		gridDimension.getHeight() + 3 * padding);
	SpielFeld spielFeld = new SpielFeld(grid, renderers, padding, pointWidth);
	spielFeld.setPreferredSize(spielfeldDimension);
	spielFeld.setSize(spielfeldDimension);
	content.add(spielFeld, BorderLayout.CENTER);
	mainWindow.setPreferredSize(spielfeldDimension);
	mainWindow.setMinimumSize(spielfeldDimension);
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
