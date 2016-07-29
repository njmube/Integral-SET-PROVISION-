/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Integral;

/**
 *
 * @author salvador
 */
 
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
 
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
 
public class Render1 extends JLabel implements TableCellRenderer 
{
    public Render1(Color c1) {
        setFont(new Font("Consolas", Font.BOLD, 10));
        setOpaque(true);
        setForeground(Color.WHITE);
        setBackground(c1);
        setBorder(BorderFactory.createEtchedBorder());
    }
     
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {
        setText(value.toString());
        return this;
    }
 
}