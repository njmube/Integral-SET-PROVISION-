/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Integral;

import java.awt.Color;
import java.awt.Component;
import javax.swing.AbstractCellEditor;
import javax.swing.JComponent;
import javax.swing.JTextField;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;


/**
 *
 * @author I.S.C Salvador
 */
public class FormatoEditorTexto extends AbstractCellEditor implements TableCellEditor{
    private static final long serialVersionUID = 1L;
    private JComponent component = new JTextField();
   

    @Override
    public Object getCellEditorValue() {
        return ((JTextField)component).getText();
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        ((JTextField)component).setBorder(null);
        ((JTextField)component).setHorizontalAlignment(JTextField.LEFT);
        ((JTextField)component).setAlignmentX(0.0F);
        ((JTextField)component).setAlignmentY(0.0F);
        ((JTextField)component).setBorder(null);
        ((JTextField)component).setMinimumSize(new java.awt.Dimension(3, 14));
        ((JTextField)component).setPreferredSize(new java.awt.Dimension(3, 14));
        ((JTextField)component).setForeground(new java.awt.Color(102, 102, 102));
        ((JTextField)component).setText(value.toString());
        ((JTextField)component).selectAll();
        ((JTextField)component).addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stopCellEditing();
            }
        });
        
        ((JTextField)component).addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                evt.setKeyChar(Character.toUpperCase(evt.getKeyChar()));
            }
        });
        
        return component;
    }
}
