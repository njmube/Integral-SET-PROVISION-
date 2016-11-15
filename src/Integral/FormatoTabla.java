/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Integral;

import java.awt.Color;
import java.awt.Component;
import java.math.BigDecimal;
import java.math.RoundingMode;
import javax.swing.BorderFactory;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.TableCellRenderer;
import javax.swing.JCheckBox;

/**
 *
 * @author I.S.C Salvador
 */
public class FormatoTabla implements TableCellRenderer{
    JFormattedTextField campoTexto;
    JLabel b=new JLabel("");
    JCheckBox cb;
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        
        campoTexto = new JFormattedTextField();
        //campoTexto.setBorder(BorderFactory.createEmptyBorder());
        campoTexto.setBorder(null);
        campoTexto.setAlignmentX(0.0F);
        campoTexto.setAlignmentY(0.0F);
        campoTexto.setBorder(null);
        campoTexto.setMinimumSize(new java.awt.Dimension(3, 14));
        campoTexto.setPreferredSize(new java.awt.Dimension(3, 14));
        campoTexto.setForeground(new java.awt.Color(102, 102, 102));
        cb = new JCheckBox();
        cb.setHorizontalAlignment(SwingConstants.CENTER);
        /*campoTexto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void t_datosKeyTyped(java.awt.event.KeyEvent evt) {
                evt.setKeyChar(Character.toUpperCase(evt.getKeyChar()));
            }
        });*/
        campoTexto.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                campoTexto.selectAll();
            }
        });
        //cb.setOpaque(false);
        
        
        if(value instanceof JLabel)
        {
            b=(JLabel)value;
        }
        
        if(value instanceof String)
        {
            campoTexto.setText(((String)value).toUpperCase());
        }
        
        if(value instanceof Boolean)
        {
            cb.setSelected((Boolean) value);
        }
        
        if(value instanceof Integer)
        {
            campoTexto.setText(value+"");
            campoTexto.setHorizontalAlignment(SwingConstants.CENTER);
        }
        
        if(value instanceof Double)
        {
            Double valor = (Double)value;
            BigDecimal big = new BigDecimal(valor+"");
            big = big.setScale(2, RoundingMode.HALF_UP);
            campoTexto.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.00")))); 
            campoTexto.setHorizontalAlignment(SwingConstants.TRAILING); 
            campoTexto.setValue(big.doubleValue());
            if(value==null)
                campoTexto.setEnabled(false);
            
            if(valor.compareTo(new Double(0))==-1) 
            { 
              campoTexto.setBackground(new Color(0xFE899B)); 
              campoTexto.setOpaque(true); 
            }
        }
        if(isSelected){
            campoTexto.setBackground(new java.awt.Color(57,105,138)); 
            campoTexto.setForeground(Color.white);
            campoTexto.setBorder(BorderFactory.createRaisedBevelBorder());
            campoTexto.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(115, 164, 209), new java.awt.Color(57,105,138), new java.awt.Color(72, 120, 155), new java.awt.Color(72, 120, 155)));
            /*b.setOpaque(true);
            b.setBackground(new java.awt.Color(57,105,138)); 
            b.setForeground(Color.white);*/
            cb.setBackground(new java.awt.Color(57,105,138));
            cb.setOpaque(true);
        }
        else
        {
            /*b.setOpaque(true);
            b.setBackground(new java.awt.Color(255,255,255)); 
            b.setForeground(Color.BLACK);*/
            cb.setBackground(new java.awt.Color(255,255,255));
            cb.setOpaque(true);
            
            if(table.getColumnCount()==9 || table.getColumnCount()==11) 
            {
                if(String.valueOf(table.getValueAt(row,8)).compareToIgnoreCase("CANCELADO")==0)
                {
                    campoTexto.setBackground(new java.awt.Color(200,0,0));
                    campoTexto.setForeground(new java.awt.Color(0,0,0));
                    campoTexto.setBorder(BorderFactory.createLineBorder(new java.awt.Color(200,0,0)));
                }
                else  
                    campoTexto.setBackground(new java.awt.Color(255,255,255));
            }
            if(table.getColumnCount()==30){
                if(String.valueOf(table.getValueAt(row,26)).compareToIgnoreCase("")!=0)
                {
                    campoTexto.setBackground(new java.awt.Color(12,191,10));
                    campoTexto.setForeground(new java.awt.Color(0,0,0));
                    campoTexto.setBorder(BorderFactory.createLineBorder(new java.awt.Color(12,191,10)));
                }
                else
                    campoTexto.setBackground(new java.awt.Color(255,255,255));
            }
            if(table.getColumnCount()==24){
                if(Double.parseDouble(String.valueOf(table.getValueAt(row,21)))==0.0d)
                {
                    if(Double.parseDouble(String.valueOf(table.getValueAt(row,20)))!=Double.parseDouble(String.valueOf(table.getValueAt(row,19))))
                        campoTexto.setBackground(new java.awt.Color(255,255,0));
                    else
                    {
                        campoTexto.setBackground(new java.awt.Color(12,191,10));
                        campoTexto.setForeground(new java.awt.Color(0,0,0));
                        campoTexto.setBorder(BorderFactory.createLineBorder(new java.awt.Color(12,191,10)));
                    }
                }
                else
                {
                    if(Double.parseDouble(String.valueOf(table.getValueAt(row,20)))!=Double.parseDouble(String.valueOf(table.getValueAt(row,19))))
                        campoTexto.setBackground(new java.awt.Color(255,255,0));
                    else
                        campoTexto.setBackground(new java.awt.Color(255,255,255));
                }
            }
        }
        
        if(hasFocus){
            cb.setBackground(Color.BLACK);
            cb.setOpaque(true);
            campoTexto.setBackground(Color.BLACK); 
            if(table.getColumnName(column).compareTo("$ Aut. c/u")==0 && table.isCellEditable(row, column)==true)
            {
                Double actual = (Double)table.getValueAt(row, column);
                if(actual==0.0)
                {
                    Double valor = (Double)table.getValueAt(row, 16);
                    BigDecimal big = new BigDecimal(valor+"");
                    big = big.setScale(2, RoundingMode.HALF_UP);
                    campoTexto.setValue(big.doubleValue());
                    table.setValueAt(big.doubleValue(), row, column);
                }
            }
        }
        /*else
        {
            //cb.setOpaque(false);
        }*/
         
        if(value instanceof JLabel)
            return b;
        
        if(value instanceof Boolean)
            return cb;
        
        return campoTexto;
    }
}
