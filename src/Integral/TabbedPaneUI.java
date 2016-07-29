/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Integral;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Polygon;
import java.awt.Rectangle;
import javax.swing.JComponent;
import javax.swing.JTabbedPane;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicTabbedPaneUI;

/**
 *
 * @author salvador
 */
public class TabbedPaneUI extends BasicTabbedPaneUI {
	private static final Insets NO_INSETS = new Insets(2, 0, 0, 0);

	private Font boldFont;
	private FontMetrics boldFontMetrics;
	private Color fillColor;
	public static ComponentUI createUI(JComponent c)
	{
		return new TabbedPaneUI();
	}

	protected void installDefaults()
	{
		super.installDefaults();
		tabAreaInsets.left = 4;
		selectedTabPadInsets = new Insets(0, 0, 0, 0);
		tabInsets = selectedTabPadInsets;

		Color background = tabPane.getBackground();
		fillColor = background.darker();

		boldFont = tabPane.getFont().deriveFont(Font.BOLD);
		boldFontMetrics = tabPane.getFontMetrics(boldFont);
	}

	public int getTabRunCount(JTabbedPane pane)
	{
		return 1;
	}

	protected Insets getContentBorderInsets(int tabPlacement)
	{
		return NO_INSETS;
	}

	protected int calculateTabHeight(int tabPlacement, int tabIndex, int fontHeight)
	{
		int vHeight = fontHeight;
		if (vHeight % 2 > 0)
		{
			vHeight += 1;
		}
                vHeight+=4;
		return vHeight;
	}

	protected int calculateTabWidth(int tabPlacement, int tabIndex, FontMetrics metrics)
	{
		return super.calculateTabWidth(tabPlacement, tabIndex, metrics) + metrics.getHeight();
	}

	protected void paintTabBackground(Graphics g, int tabPlacement, int tabIndex, int x, int y, int w, int h, boolean isSelected)
	{
		Polygon shape = new Polygon();
                y+=2;
                shape.addPoint(x, y);
                shape.addPoint(x, y + h);
                shape.addPoint(x + w - (h/2), y+h);
                shape.addPoint(x + w, y+(h/2));
                shape.addPoint(x + w, y);
		if (isSelected)
                    g.setColor(Color.white);
		else
                    g.setColor(Color.lightGray);
		g.fillPolygon(shape);
	}

	protected void paintTabBorder(Graphics g, int tabPlacement, int tabIndex, int x, int y, int w, int h, boolean isSelected)
	{
            Polygon shape = new Polygon();
                y+=2;
                shape.addPoint(x, y);
                shape.addPoint(x, y + h);
                shape.addPoint(x + w - (h/2), y+h);
                shape.addPoint(x + w, y+(h/2));
                shape.addPoint(x + w, y);
                g.setColor(Color.BLACK);
		g.drawPolygon(shape);
	}

	protected void paintContentBorderTopEdge(Graphics g, int tabPlacement, int selectedIndex, int x, int y, int w, int h)
	{
            g.setColor(Color.WHITE);
            g.drawLine(x, y, x+w, y);
            g.drawLine(x, y+1, x+w, y+1);
            g.setColor(Color.WHITE);
	}

	protected void paintContentBorderRightEdge(Graphics g, int tabPlacement, int selectedIndex, int x, int y, int w, int h)
	{
		// Do nothing
	}

	protected void paintContentBorderLeftEdge(Graphics g, int tabPlacement, int selectedIndex, int x, int y, int w, int h)
	{
		// Do nothing
	}

	protected void paintContentBorderBottomEdge(Graphics g, int tabPlacement, int selectedIndex, int x, int y, int w, int h)
	{
            g.setColor(Color.WHITE);
            g.drawLine(x, y+h, x+w, y+h);
            g.drawLine(x, y+h+1, x+w, y+h+1);
            g.setColor(Color.WHITE);
	}

	protected void paintFocusIndicator(Graphics g, int tabPlacement, Rectangle[] rects, int tabIndex, Rectangle iconRect, Rectangle textRect, boolean isSelected)
	{
		// Do nothing
	}

	protected void paintTabArea(Graphics g, int tabPlacement, int selectedIndex)
	{
		int tw = tabPane.getBounds().width;
		g.setColor(fillColor);
		g.fillRect(0, 0, tw, rects[0].height + 3);
		super.paintTabArea(g, tabPlacement, selectedIndex);
	}

	protected void paintText(Graphics g, int tabPlacement, Font font, FontMetrics metrics, int tabIndex, String title, Rectangle textRect, boolean isSelected)
	{
            super.paintText(g, tabPlacement, font, metrics, tabIndex, title, textRect, isSelected);
	}

	protected int getTabLabelShiftY(int tabPlacement, int tabIndex, boolean isSelected)
	{
		return 0;
	}
}
