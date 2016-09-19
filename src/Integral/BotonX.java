/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Integral;

import Hibernate.Util.HibernateUtil;
import Hibernate.entidades.Orden;
import Hibernate.entidades.Pedido;
import Hibernate.entidades.Usuario;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JTabbedPane;
import javax.swing.plaf.basic.BasicButtonUI;
import org.hibernate.Session;

/**
 *
 * @author I.S.C Salvador
 */
public class BotonX extends JButton implements MouseListener {
 
 JTabbedPane panel;
 PanelPestanas btc;
 int tipo;
 Usuario user;
 
    public BotonX(JTabbedPane pane,PanelPestanas btc,int op, Usuario u) {
     panel=pane;
     this.btc=btc;
     tipo=op;
     user=u;
        int size = 17;
        setPreferredSize(new Dimension(size, size));
        setToolTipText("Cerrar Pestaña");
        setUI(new BasicButtonUI());
        setContentAreaFilled(false);
        setFocusable(false);
        setBorder(BorderFactory.createEtchedBorder());
        setBorderPainted(false);
        addMouseListener(this);
        setRolloverEnabled(true);
        addActionListener(new ActionListener(){


   @Override
   public void actionPerformed(ActionEvent e) {
    int i = panel.indexOfTabComponent(BotonX.this.btc);
          if (i != -1) {
              if(panel.getTitleAt(i).compareTo("E. Orden")==0 || panel.getTitleAt(i).compareTo("A. Valuacion")==0 || panel.getTitleAt(i).compareTo("Ref. cotiza")==0 || panel.getTitleAt(i).compareTo("Autorización")==0 || panel.getTitleAt(i).compareTo("Genera Cotizaciones")==0 || panel.getTitleAt(i).compareTo("Genera Pedidos")==0 || panel.getTitleAt(i).compareTo("Avance de Pedidos")==0 || panel.getTitleAt(i).compareTo("Autorizar Pedidos")==0  || panel.getTitleAt(i).compareTo("Modificar Pedido")==0 || panel.getTitleAt(i).compareTo("Nuevo Pedido")==0 || panel.getTitleAt(i).compareTo("Pre-factura")==0  || panel.getTitleAt(i).compareTo("Nuevo Almacén")==0 || panel.getTitleAt(i).compareTo("Autorizar Pedidos")==0 || panel.getTitleAt(i).compareTo("Eliminar Pedido")==0 || panel.getTitleAt(i).compareTo("Destajos")==0)
              {
                  Session session = HibernateUtil.getSessionFactory().openSession();
                  try
                  {
                      if(tipo>-1)
                      {
                        session.beginTransaction().begin();
                        user=(Usuario)session.get(Usuario.class, user.getIdUsuario());
                        Orden[] bloqueadas = (Orden[]) user.getOrdensForBloqueada().toArray(new Orden[0]);
                        for(int a=0; a<bloqueadas.length; a++)
                        {
                            String v=""+tipo;
                            if(bloqueadas[a].getVentana().compareTo(v)==0)
                            {
                                user.eliminaOrdensForBloqueada(bloqueadas[a]);
                                bloqueadas[a].setUsuarioByBloqueada(null);
                                bloqueadas[a].setVentana(null);
                                session.update(bloqueadas[a]);
                                session.update(user);
                            }
                        }
                        
                        Pedido[] bloqueados = (Pedido[]) user.getPedidosForBloqueado().toArray(new Pedido[0]);
                        for(int a=0; a<bloqueados.length; a++)
                        {
                            String v=""+tipo;
                            if(bloqueados[a].getVentana().compareTo(v)==0)
                            {
                                user.getPedidosForBloqueado().remove(bloqueados[a]);
                                bloqueados[a].setUsuarioByBloqueado(null);
                                bloqueados[a].setVentana(null);
                                session.update(bloqueados[a]);
                                session.update(user);
                            }
                        }
                        session.getTransaction().commit();
                      }
                  }catch(Exception ex)
                  {
                      System.out.println(ex);
                  }
                  finally
                  {
                      if(session!=null)
                          if(session.isOpen())
                              session.close();
                  }
              }
              Component obj = panel.getComponentAt(i);
              //panel.removeTabAt(i);
              panel.remove(i);
              obj=null;
              System.gc();
          }
   }
        });
   }
   
    public void updateUI() {
    }
   
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        if(tipo==0){
         if (getModel().isPressed()) {
          g2.translate(1, 1);
         }
         g2.setStroke(new BasicStroke(3));
         g2.setColor(Color.BLACK);
         if (getModel().isRollover()) {
          g2.setColor(Color.RED);
         }
         g2.drawLine(5, 5, 12, 12);
         g2.drawLine(12, 6, 6, 12);
         g2.dispose();
        }else{
         ImageIcon img=new ImageIcon("imagenes/boton_cerrar.png");
         g2.drawImage(img.getImage(), 0, 0,18,18, this);
         g2.dispose();
        }
    }


 @Override
 public void mouseClicked(MouseEvent e) {
  // TODO Auto-generated method stub
  
 }


 @Override
 public void mouseEntered(MouseEvent e) {
  Component component = e.getComponent();
        if (component instanceof AbstractButton) {
            AbstractButton button = (AbstractButton) component;
            button.setBorderPainted(true);
        }
 }


 @Override
 public void mouseExited(MouseEvent e) {
  Component component = e.getComponent();
        if (component instanceof AbstractButton) {
            AbstractButton button = (AbstractButton) component;
            button.setBorderPainted(false);
        }
 }


 @Override
 public void mousePressed(MouseEvent e) {
  // TODO Auto-generated method stub
  
 }


 @Override
 public void mouseReleased(MouseEvent e) {
  // TODO Auto-generated method stub  
 }
}

