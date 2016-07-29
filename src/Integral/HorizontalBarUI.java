/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Integral;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.plaf.basic.BasicScrollBarUI;

/**
 *
 * @author salvador
 */

public class HorizontalBarUI extends BasicScrollBarUI {

	@Override
	protected void configureScrollBarColors() {
		//thumbColor = Color.BLUE;
		//thumbHighlightColor = Color.BLUE;
		//thumbDarkShadowColor = Color.BLACK;
		//thumbLightShadowColor = Color.YELLOW;
		//trackColor = Color.WHITE;
		//trackHighlightColor = Color.GREEN;
	}

	@Override
	protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
		super.paintTrack(g, c, trackBounds);
	}
	
	@Override
	protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
		g.translate(thumbBounds.x, thumbBounds.y); 
		g.setColor(new java.awt.Color(2, 135, 242));
                if(thumbBounds.height==0)
                    thumbBounds.height=5;
		g.fillRoundRect(0, 5, thumbBounds.width-1, 6, 5, 5); 
		Graphics2D g2 = (Graphics2D) g;
		RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.addRenderingHints(rh);
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
		//g2.setPaint(new GradientPaint(c.getWidth() / 2, 1, Color.GRAY, c.getWidth() / 2, c.getHeight(), Color.BLACK));
		g2.fillRoundRect(0, 5, thumbBounds.width-1, 6, 5, 5);
	}
	
	@Override
	protected JButton createIncreaseButton(int orientation) {
                JButton button = new JButton();
                button.setIcon(new ImageIcon("imagenes/right.png"));
		button.setOpaque(false);
                button.setContentAreaFilled(false);
                button.setBorderPainted(false);
		return button;
	}
	
	@Override
	protected JButton createDecreaseButton(int orientation) {
                JButton button = new JButton();
                button.setIcon(new ImageIcon("imagenes/left.png"));
		button.setOpaque(false);
                button.setContentAreaFilled(false);
                button.setBorderPainted(false);
		return button;
	}
}
