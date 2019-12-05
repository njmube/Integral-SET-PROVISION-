/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * acceso.java
 *
 * Created on 19/01/2012, 02:01:25 PM
 */
package Catalogo;

import Integral.Herramientas;
import Hibernate.Util.HibernateUtil;
import java.util.List;
import javax.swing.InputMap;
import Hibernate.entidades.Catalogo;
import Hibernate.entidades.Especialidad;
import Hibernate.entidades.Usuario;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author ISC_SALVADOR
 */
public class altaCatalogo extends javax.swing.JDialog {

    public static final Catalogo RET_CANCEL =null;
    InputMap map = new InputMap();
    DefaultTableModel model;
    int[] especialidad;
    //private Session session;
    Usuario user;
    String sessionPrograma;
    Herramientas h;
    
    
    /** Creates new form acceso */
    public altaCatalogo(java.awt.Frame parent, boolean modal, Usuario u, String ses) {    
        super(parent, modal);
        user=u;
        sessionPrograma=ses;
        initComponents();
        cargaEspecialidad();
        t_nombre.requestFocus();
        user=u;
    }
    
    private void doClose(Catalogo o) {
        returnStatus = o;
        setVisible(false);
        dispose();
    }
    
    public Catalogo getReturnStatus() {
        return returnStatus;
    }
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        t_nombre = new javax.swing.JTextField();
        b_cancelar = new javax.swing.JButton();
        b_guardar = new javax.swing.JButton();
        c_gm = new javax.swing.JComboBox();
        jLabel17 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Alta de Descripciones");
        setResizable(false);

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel8.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(51, 0, 255));
        jLabel8.setText("Descripción:");

        t_nombre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                t_nombreActionPerformed(evt);
            }
        });
        t_nombre.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                t_nombreKeyTyped(evt);
            }
        });

        b_cancelar.setBackground(new java.awt.Color(2, 135, 242));
        b_cancelar.setForeground(new java.awt.Color(254, 254, 254));
        b_cancelar.setIcon(new ImageIcon("imagenes/cancelar.png"));
        b_cancelar.setText("Cancelar");
        b_cancelar.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        b_cancelar.setNextFocusableComponent(t_nombre);
        b_cancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_cancelarActionPerformed(evt);
            }
        });

        b_guardar.setBackground(new java.awt.Color(2, 135, 242));
        b_guardar.setForeground(new java.awt.Color(254, 254, 254));
        b_guardar.setIcon(new ImageIcon("imagenes/guardar.png"));
        b_guardar.setText("Guardar");
        b_guardar.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        b_guardar.setNextFocusableComponent(b_cancelar);
        b_guardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_guardarActionPerformed(evt);
            }
        });

        c_gm.setNextFocusableComponent(b_guardar);
        c_gm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                c_gmActionPerformed(evt);
            }
        });

        jLabel17.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(51, 0, 255));
        jLabel17.setText("Grupo Mecánico:");

        jLabel10.setText("Nota: Son utilizados para identificar cada una de las partidas de una orden de trabajo.");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel17)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(c_gm, javax.swing.GroupLayout.PREFERRED_SIZE, 256, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel10)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(b_cancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(b_guardar, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel8)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(t_nombre, javax.swing.GroupLayout.PREFERRED_SIZE, 444, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addGap(0, 12, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(t_nombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(c_gm, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(b_guardar, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(b_cancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void t_nombreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_t_nombreActionPerformed
        this.c_gm.requestFocus();
    }//GEN-LAST:event_t_nombreActionPerformed

    private void b_cancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_cancelarActionPerformed
        t_nombre.requestFocus();
        int opt=JOptionPane.showConfirmDialog(this, "¡Los datos capturados se eliminarán!");
        System.out.println(opt);
        if(opt==0)
            borra_cajas();
    }//GEN-LAST:event_b_cancelarActionPerformed

    private void b_guardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_guardarActionPerformed
        h=new Herramientas(user, 0);
        h.session(sessionPrograma);
        t_nombre.setText(t_nombre.getText().trim());
        if(t_nombre.getText().compareTo("")!=0)
        {
            if(c_gm.getItemCount()>0)
            {
                if(c_gm.getSelectedItem().toString().compareTo("BUSCAR")!=0)
                {
                    Session session = HibernateUtil.getSessionFactory().openSession();
                    try 
                    {
                        session.beginTransaction();
                        if(consultaNombre()==false)
                        {
                            Catalogo cat= new Catalogo();
                            cat.setNombre(t_nombre.getText());
                            c_gm.getSelectedIndex();
                            Especialidad registro = (Especialidad)session.get(Especialidad.class,especialidad[c_gm.getSelectedIndex()]);
                            cat.setEspecialidad(registro);
                            cat.setActual(true);
                            Integer ID=(Integer) session.save(cat);
                            session.getTransaction().commit();
                            returnStatus=cat;
                            if(ID!=null)
                            {
                                JOptionPane.showMessageDialog(null, "Registro almacenado");
                                borra_cajas();
                                t_nombre.requestFocus();
                            }
                        }
                        else
                        {
                            session.getTransaction().rollback();
                            JOptionPane.showMessageDialog(null, "¡La descripción ya existe!");
                        }
                    }
                    catch (HibernateException he) 
                    {
                        he.printStackTrace();
                        session.getTransaction().rollback();
                        JOptionPane.showMessageDialog(null, "Error al guardar");
                    }
                    finally 
                     {
                        session.close(); 
                     }
                }
                else
                    JOptionPane.showMessageDialog(null, "¡Debe seleccionar un grupo mecánico!");
            }
            else
                JOptionPane.showMessageDialog(null, "¡Debe dar de alta primero grupo mecánico!");
        }
        else
        {
            JOptionPane.showMessageDialog(null, "¡Debe introducir la descripción!");
            t_nombre.requestFocus();
        }
    }//GEN-LAST:event_b_guardarActionPerformed

    private void t_nombreKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_nombreKeyTyped
        evt.setKeyChar(Character.toUpperCase(evt.getKeyChar()));
        char car = evt.getKeyChar();
        if(t_nombre.getText().length()>=100) 
            evt.consume();
    }//GEN-LAST:event_t_nombreKeyTyped

    private void c_gmActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_c_gmActionPerformed
        b_guardar.requestFocus();
    }//GEN-LAST:event_c_gmActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton b_cancelar;
    private javax.swing.JButton b_guardar;
    public javax.swing.JComboBox c_gm;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    public javax.swing.JTextField t_nombre;
    // End of variables declaration//GEN-END:variables

    private List<Object[]> executeHQLQuery(String hql) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            session.beginTransaction();
            Query q = session.createQuery(hql);
            List resultList = q.list();
            session.getTransaction().commit();
            session.disconnect();
            return resultList;
        } catch (HibernateException he) {
            he.printStackTrace();
            List lista= null;//new List(5);
            return lista;
        }
    }

    private void borra_cajas()
    {
        t_nombre.setText("");
    }
    
    public void cargaEspecialidad()
    {
        List <Object[]> resultList=executeHQLQuery("from Especialidad where plantilla=1");
        if(resultList.size()>0)
        {
            c_gm.removeAllItems();
            especialidad= new int [resultList.size()+1];
            especialidad[0]=0;
            int x=1;
            c_gm.addItem("BUSCAR");
            for (Object o : resultList)
            {
                Especialidad actor = (Especialidad) o;
                c_gm.addItem(actor.getDescripcion());
                especialidad[x]=actor.getIdGrupoMecanico();
                x++;
            }
        }
    }
    
    private boolean consultaNombre()
    {
        List <Object[]> resultList=executeHQLQuery("from Catalogo obj where obj.nombre='"+t_nombre.getText()+"' and actual=1");
        if(resultList.size()>0)
            return true;
        else
            return false;
    }
    private Catalogo returnStatus = RET_CANCEL;
}
