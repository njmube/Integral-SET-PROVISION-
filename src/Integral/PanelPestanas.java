/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Integral;

import Hibernate.entidades.Usuario;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

/**
 *
 * @author I.S.C Salvador
 */
public class PanelPestanas extends JPanel {
     JTabbedPane pestañas;
     int tipo;
     Usuario user;
     int ven;
     public PanelPestanas(JTabbedPane p,int op, Usuario u) {
        if (p != null) 
        {
            this.pestañas = p;
            tipo=op;
            setOpaque(false);
            
            JLabel titulo = new JLabel() 
            {
                public String getText() 
                {
                    int i = pestañas.indexOfTabComponent(PanelPestanas.this);
                    if (i != -1) 
                    {
                        return pestañas.getTitleAt(i);
                    }
                    return null;
                }
            };
            add(titulo);
            titulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
            JButton button = new BotonX(pestañas,this,tipo, u);
            add(button);
        }
    }
}


