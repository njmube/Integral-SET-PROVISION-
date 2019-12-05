/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Integral;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

/**
 *
 * @author I.S.C.Salvador
 */
public class ListaEtiqueta extends JLabel implements ListCellRenderer{
    private JLabel label;
    public Component getListCellRendererComponent(JList list, Object value,
                                                 int index, boolean isSelected,
                                                 boolean cellHasFocus) {
        ListaObjeto entry = (ListaObjeto) value;
  
        setText(value.toString());
        setIcon(entry.getIcono());
   
        if (isSelected) {
            setBackground(Color.BLUE);
            setForeground(Color.WHITE);
        }
        else {
            setBackground(Color.WHITE);
            setForeground(Color.BLACK);
        }
        setEnabled(list.isEnabled());
        setFont(list.getFont());
        setOpaque(true);
        return this;
   }

}
