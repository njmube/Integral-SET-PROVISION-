/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Integral;

import java.awt.Component;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractCellEditor;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.TableCellEditor;


/**
 *
 * @author I.S.C Salvador
 */
public class FormatoEditor extends AbstractCellEditor implements TableCellEditor{
    private static final long serialVersionUID = 1L;
    private JComponent component = new JFormattedTextField();
    private NumberFormat nf;

    @Override
    public Object getCellEditorValue() {
        NumberFormat nf = new DecimalFormat("#,###.###");
        //String text = ((JFormattedTextField)component).getText();
        //return Double.parseDouble(text);
        if(((JFormattedTextField)component).isEditValid())
            try 
            {
                ((JFormattedTextField)component).commitEdit();
            } catch (ParseException ex) 
            {
                Logger.getLogger(FormatoEditor.class.getName()).log(Level.SEVERE, null, ex);
            }
        return Double.parseDouble(((JFormattedTextField)component).getValue().toString());
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        ((JFormattedTextField)component).setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.00")))); 
        ((JFormattedTextField)component).setHorizontalAlignment(SwingConstants.TRAILING); 
        ((JFormattedTextField)component).setBorder(null);
        ((JFormattedTextField)component).setAlignmentX(0.0F);
        ((JFormattedTextField)component).setAlignmentY(0.0F);
        ((JFormattedTextField)component).setBorder(null);
        ((JFormattedTextField)component).setMinimumSize(new java.awt.Dimension(3, 14));
        ((JFormattedTextField)component).setPreferredSize(new java.awt.Dimension(3, 14));
        ((JFormattedTextField)component).setForeground(new java.awt.Color(102, 102, 102));
        ((JFormattedTextField)component).setHorizontalAlignment(SwingConstants.RIGHT);
        ((JFormattedTextField)component).setValue(value);
        ((JFormattedTextField)component).selectAll();
        ((JFormattedTextField)component).addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stopCellEditing();
            }
        });
        return component;
    }
}
