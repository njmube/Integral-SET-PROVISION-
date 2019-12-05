/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Agente;

import Hibernate.Util.HibernateUtil;
import Hibernate.entidades.Agente;
import Hibernate.entidades.Usuario;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.List;
import java.util.Vector;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import Integral.Herramientas;
import Integral.Render1;
/**
 *
 * @author ESPECIALIZADO TOLUCA
 */
public class editaAgente extends javax.swing.JPanel {
    
    //private Session session;
    DefaultTableModel model;
    Usuario usr;
    String sessionPrograma="";
    Herramientas h;
    private Agente actor1;
    String ic="";
    String[] columnas = new String [] {"Clave","Agente"};
    int x=0;
    /**
     * Creates new form EditaCiclo
     */
    public editaAgente(Usuario usuario, String ses) {
        initComponents();
        sessionPrograma=ses;
        usr=usuario;
        formatoTabla();
        buscaDato();
    }
    
    DefaultTableModel ModeloTablaReporte(int renglones, String columnas[])
    {
        model = new DefaultTableModel(new Object [renglones][2], columnas)
        {
            Class[] types = new Class [] {
                java.lang.String.class,
                java.lang.String.class                
            };
            boolean[] canEdit = new boolean [] {
                true,true
            };
            public void setValueAt(Object value, int row, int col)
            {
                Vector vector = (Vector)this.dataVector.elementAt(row);
                switch(col)
                {
                    case 0:
                        if(vector.get(col)==null)
                        {
                            vector.setElementAt(value, col);
                            this.dataVector.setElementAt(vector, row);
                            fireTableCellUpdated(row, col);
                        }
                        else
                        {
                            Session session = HibernateUtil.getSessionFactory().openSession();
                            String valor = (String)value;
                            String valorAnterior = t_datos.getValueAt(row, col).toString();
                            if(valorAnterior.compareToIgnoreCase(valor)!=0)
                            {
                                try   
                                {
                                    session.beginTransaction();
                                    Object resp = session.createQuery("from Agente obj where obj.idAgente='"+valor+"'").uniqueResult();
                                    if(resp==null)
                                    {
                                        Query q = session.createQuery("update Agente obj set obj.idAgente='"+valor+"' where obj.idAgente = '"+valorAnterior+"'");
                                        q.executeUpdate();
                                        session.getTransaction().commit();
                                        vector.setElementAt(value, col);
                                        this.dataVector.setElementAt(vector, row);
                                        fireTableCellUpdated(row, col);
                                    }
                                    else
                                        JOptionPane.showMessageDialog(null, "No se pueden guardar Nombres duplicadas");  
                                }
                                catch (HibernateException he)
                                {
                                    he.printStackTrace(); 
                                    session.getTransaction().rollback();
                                }
                                finally
                                {
                                    if(session.isOpen())
                                        session.close();
                                }
                            }
                        }
                        break;
                    default:
                        if(vector.get(col)==null)
                        {
                            vector.setElementAt(value, col);
                            this.dataVector.setElementAt(vector, row);
                            fireTableCellUpdated(row, col);
                        }
                        else
                        {
                            Session session = HibernateUtil.getSessionFactory().openSession();
                            String valor = (String)value;
                            String valorAnterior = t_datos.getValueAt(row, col).toString();
                            if(valorAnterior.compareToIgnoreCase(valor)!=0)
                            {
                                try   
                                {
                                    session.beginTransaction();
                                    ic = t_datos.getValueAt(row, 0).toString();
                                    Object resp = session.createQuery("from Agente obj where obj.nombre='"+valor+"'").uniqueResult();
                                    if(resp==null)
                                    {
                                        Query q = session.createQuery("update Agente obj set obj.nombre='"+valor+"' where obj.idAgente = '"+ic+"'");
                                        q.executeUpdate();
                                        session.getTransaction().commit();
                                        vector.setElementAt(value, col);
                                        this.dataVector.setElementAt(vector, row);
                                        fireTableCellUpdated(row, col);
                                    }
                                    else
                                        JOptionPane.showMessageDialog(null, "No se pueden guardar Nombres duplicadas");  
                                }
                                catch (HibernateException he)
                                {
                                    he.printStackTrace(); 
                                    session.getTransaction().rollback();
                                }
                                finally
                                {
                                    if(session.isOpen())
                                        session.close();
                                }
                            }
                        }
                        break;
                }
            }
            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];  
            }
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        };
        return model;
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        t_datos = new javax.swing.JTable();
        t_busca = new javax.swing.JTextField();
        bt_actualiza1 = new javax.swing.JButton();
        Eliminar1 = new javax.swing.JButton();
        Selecciona2 = new javax.swing.JButton();
        b_busca = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();

        setBackground(new java.awt.Color(254, 254, 254));
        setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "Lista de Agentes", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP));

        jScrollPane1.setBackground(new java.awt.Color(254, 254, 254));

        t_datos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Clave", "Agente"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        t_datos.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        t_datos.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(t_datos);

        t_busca.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                t_buscaActionPerformed(evt);
            }
        });

        bt_actualiza1.setBackground(new java.awt.Color(2, 135, 242));
        bt_actualiza1.setForeground(new java.awt.Color(254, 254, 254));
        bt_actualiza1.setIcon(new ImageIcon("imagenes/tabla.png"));
        bt_actualiza1.setText("Actualizar");
        bt_actualiza1.setToolTipText("Actualizar los datos de la tabla");
        bt_actualiza1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        bt_actualiza1.setMaximumSize(new java.awt.Dimension(87, 23));
        bt_actualiza1.setMinimumSize(new java.awt.Dimension(87, 23));
        bt_actualiza1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bt_actualiza1ActionPerformed(evt);
            }
        });

        Eliminar1.setBackground(new java.awt.Color(2, 135, 242));
        Eliminar1.setForeground(new java.awt.Color(254, 254, 254));
        Eliminar1.setIcon(new ImageIcon("imagenes/del-user.png"));
        Eliminar1.setText("Eliminar");
        Eliminar1.setToolTipText("Eliminar el registro actual");
        Eliminar1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        Eliminar1.setMaximumSize(new java.awt.Dimension(87, 23));
        Eliminar1.setMinimumSize(new java.awt.Dimension(87, 23));
        Eliminar1.setNextFocusableComponent(Selecciona2);
        Eliminar1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Eliminar1ActionPerformed(evt);
            }
        });

        Selecciona2.setBackground(new java.awt.Color(2, 135, 242));
        Selecciona2.setForeground(new java.awt.Color(254, 254, 254));
        Selecciona2.setIcon(new ImageIcon("imagenes/add-user.png"));
        Selecciona2.setText("Nuevo");
        Selecciona2.setToolTipText("Agregar un registo actual");
        Selecciona2.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        Selecciona2.setMaximumSize(new java.awt.Dimension(87, 23));
        Selecciona2.setMinimumSize(new java.awt.Dimension(87, 23));
        Selecciona2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Selecciona2ActionPerformed(evt);
            }
        });

        b_busca.setIcon(new ImageIcon("imagenes/buscar1.png"));
        b_busca.setToolTipText("Busca una partida");
        b_busca.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_buscaActionPerformed(evt);
            }
        });

        jLabel1.setText("Buscar:");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(t_busca, javax.swing.GroupLayout.PREFERRED_SIZE, 196, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(b_busca, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 478, Short.MAX_VALUE)
                        .addComponent(Selecciona2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(bt_actualiza1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Eliminar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(7, 7, 7)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(t_busca, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel1))
                    .addComponent(b_busca, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Eliminar1, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bt_actualiza1, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Selecciona2, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 407, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void Selecciona2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Selecciona2ActionPerformed
        h=new Herramientas(usr, 0);
        h.session(sessionPrograma);
        Session session = HibernateUtil.getSessionFactory().openSession();
        usr = (Usuario)session.get(Usuario.class, usr.getIdUsuario());
        altaAgente obj = new altaAgente(new javax.swing.JFrame(), true, usr, sessionPrograma);
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        obj.setLocation((d.width/2)-(obj.getWidth()/2), (d.height/2)-(obj.getHeight()/2));
        obj.setVisible(true);
        buscaDato();
        if(session!=null)
            if(session.isOpen())
                session.close();
    }//GEN-LAST:event_Selecciona2ActionPerformed

    private void Eliminar1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Eliminar1ActionPerformed
        Selecciona2.requestFocus();
        h=new Herramientas(usr, 0);
        h.session(sessionPrograma);
        Session session = HibernateUtil.getSessionFactory().openSession();
        if(this.t_datos.getSelectedRow()>=0)
        {
            DefaultTableModel model = (DefaultTableModel) t_datos.getModel();
            int a = t_datos.getSelectedRow();
            int opt=JOptionPane.showConfirmDialog(this, "¡Los datos capturados se eliminarán!");
            if (JOptionPane.YES_OPTION == opt)
            {
                boolean respuesta=eliminar(t_datos.getValueAt(t_datos.getSelectedRow(), 0).toString());
                if(respuesta==true)
                {
                    JOptionPane.showMessageDialog(null, "El Agente ha sido eliminado");
                    model.removeRow(a);
                    buscaDato();
                }
            }
        }
        if(session!=null)
            if(session.isOpen())
                session.close();
    }//GEN-LAST:event_Eliminar1ActionPerformed

    private void bt_actualiza1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bt_actualiza1ActionPerformed
        buscaDato();
    }//GEN-LAST:event_bt_actualiza1ActionPerformed
    private void t_buscaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_t_buscaActionPerformed
        // TODO add your handling code here:
       if(this.t_busca.getText().compareToIgnoreCase("")!=0)
        {
            if(x>=t_datos.getRowCount())
            {
                x=0;
                java.awt.Rectangle r = t_datos.getCellRect( x, 1, true);
                t_datos.scrollRectToVisible(r);
            }
            for(; x<t_datos.getRowCount(); x++)
            {
                if(t_datos.getValueAt(x, 1).toString().indexOf(t_busca.getText().toUpperCase()) != -1)
                {
                    t_datos.setRowSelectionInterval(x, x);
                    t_datos.setColumnSelectionInterval(1, 1);
                    java.awt.Rectangle r = t_datos.getCellRect( x, 1, true);
                    t_datos.scrollRectToVisible(r);
                    break;
                }
            }
            x++;
        }
    }//GEN-LAST:event_t_buscaActionPerformed

    private void b_buscaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_buscaActionPerformed
        // TODO add your handling code here:
         this.t_buscaActionPerformed(null);
    }//GEN-LAST:event_b_buscaActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Eliminar1;
    private javax.swing.JButton Selecciona2;
    private javax.swing.JButton b_busca;
    private javax.swing.JButton bt_actualiza1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField t_busca;
    private javax.swing.JTable t_datos;
    // End of variables declaration//GEN-END:variables
    
    
    private boolean eliminar(String idAgente)
    {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try
        {
            session.beginTransaction();
            actor1 = (Agente)session.get(Agente.class, Integer.parseInt(idAgente));
            
            if(actor1.getOrdens().isEmpty()==false) 
            {
                session.getTransaction().rollback();
                JOptionPane.showMessageDialog(null, "¡El Agente esta en uso en una orden no se puede eliminar!");
                return false;
            }
            else
            {
                session.delete(actor1);
                session.getTransaction().commit();
                return true;
            }
        }catch(Exception e)
        {
            e.printStackTrace();
            session.getTransaction().rollback();
            return false;
        }
        finally
        {
            if(session.isOpen())
            session.close();
        }
    }
    
    private void buscaDato()
    {
        String consulta="from Agente";
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            session.beginTransaction();
            Query q = session.createQuery(consulta);
            List resultList = q.list();
            session.getTransaction().commit();
            session.disconnect();
            if(resultList.size()>0)
            {
                t_datos.setModel(ModeloTablaReporte(resultList.size(), columnas));
                int i=0;
                for (Object o : resultList)
                {
                    actor1 = (Agente) o;
                    model.setValueAt(actor1.getIdAgente(), i, 0);
                    model.setValueAt(actor1.getNombre(), i, 1);
                    i++;
                }
            }
            else
                t_datos.setModel(ModeloTablaReporte(0, columnas));
        } catch (HibernateException he) {
            he.printStackTrace();
            t_datos.setModel(ModeloTablaReporte(0, columnas));
        }
        formatoTabla();
    }
    
    public void formatoTabla()
    {
        Color c1 = new java.awt.Color(2, 135, 242);   
        for(int x=0; x<t_datos.getColumnModel().getColumnCount(); x++)
            t_datos.getColumnModel().getColumn(x).setHeaderRenderer(new Render1(c1));
        tabla_tamaños();
        t_datos.setShowVerticalLines(true);
        t_datos.setShowHorizontalLines(true);
    }
    
    public void tabla_tamaños()
    {
        TableColumnModel col_model = t_datos.getColumnModel();
        for (int i=0; i<t_datos.getColumnCount(); i++)
        {
  	      TableColumn column = col_model.getColumn(i);
              switch(i)
              {
                  case 0:
                      column.setPreferredWidth(10);
                      break;
                  case 1:
                      column.setPreferredWidth(500);
                      break;
                  default:
                      column.setPreferredWidth(40);
                      break; 
              }
        }
        JTableHeader header = t_datos.getTableHeader();
        header.setForeground(Color.white);
    }
}