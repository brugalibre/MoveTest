/**
 * 
 */
package com.myownb3.piranha.ui.application;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.myownb3.piranha.grid.DefaultGrid;
import com.myownb3.piranha.ui.render.Renderer;

/**
 * @author Dominic
 * 
 */
public class MainWindow {
    private JFrame mainWindow;
    private JPanel content;

    public MainWindow(List<Renderer> renderer, int width, int height) {

	content = new JPanel();
	SpielFeld spielFeld = new SpielFeld(renderer);
	spielFeld.setPreferredSize(new Dimension(width, height));
	content.add(spielFeld);

	mainWindow = new JFrame();
	setLocation();
	mainWindow.add(content);
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
