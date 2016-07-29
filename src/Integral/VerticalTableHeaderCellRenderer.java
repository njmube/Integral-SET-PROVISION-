/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Integral;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JTable;
import javax.swing.RowSorter.SortKey;
import javax.swing.SortOrder;
import javax.swing.UIManager;
import javax.swing.border.Border;

/**
 *
 * @author I.S.C Salvador
 */
public class VerticalTableHeaderCellRenderer
        extends DefaultTableHeaderCellRenderer {

  public VerticalTableHeaderCellRenderer() {
    setHorizontalAlignment(LEFT);
    setHorizontalTextPosition(CENTER);
    setVerticalAlignment(CENTER);
    setVerticalTextPosition(TOP);
    setUI(new VerticalLabelUI());
    Border border = BorderFactory.createLineBorder(Color.black); 
    setBorder(border);
  }

  @Override
  protected Icon getIcon(JTable table, int column) {
    SortKey sortKey = getSortKey(table, column);
    if (sortKey != null && table.convertColumnIndexToView(sortKey.getColumn()) == column) {
      SortOrder sortOrder = sortKey.getSortOrder();
      switch (sortOrder) {
        case ASCENDING:
          return VerticalSortIcon.ASCENDING;
        case DESCENDING:
          return VerticalSortIcon.DESCENDING;
      }
    }
    return null;
  }

  private enum VerticalSortIcon implements Icon {

    ASCENDING(UIManager.getIcon("Table.ascendingSortIcon")),
    DESCENDING(UIManager.getIcon("Table.descendingSortIcon"));
    private final Icon icon;// = ;

    private VerticalSortIcon(Icon icon) {
      this.icon = icon;
    }

    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {
      int maxSide = Math.max(getIconWidth(), getIconHeight());
      Graphics2D g2 = (Graphics2D) g.create(x, y, maxSide, maxSide);
      g2.rotate((Math.PI / 2));
      g2.translate(0, -maxSide);
      icon.paintIcon(c, g2, 0, 0);
      g2.dispose();
    }

    @Override
    public int getIconWidth() {
      return icon.getIconHeight();
    }

    @Override
    public int getIconHeight() {
      return icon.getIconWidth();
    }
  }
}