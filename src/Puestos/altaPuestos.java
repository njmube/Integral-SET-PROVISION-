/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Puestos;

import Hibernate.Util.HibernateUtil;
import Hibernate.entidades.Puestos;
import Hibernate.entidades.Usuario;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.InputMap;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import Integral.Herramientas;
/**
 *
 * @author ESPECIALIZADO TOLUCA
 */
public class altaPuestos extends javax.swing.JDialog {

    public static final Puestos RET_CANCEL =null;
    InputMap map = new InputMap();
    DefaultTableModel model;
    int[] puestos;
    Usuario user;
    String sessionPrograma;
    Herramientas h;
    private Puestos returnStatus;
    

    public altaPuestos(java.awt.Frame parent, boolean modal, Usuario u, String ses) {
        super(parent, modal);
        user=u;
        sessionPrograma=ses;
        initComponents();
    }
    
    private void doClose(Puestos o) {
        returnStatus = o;
        setVisible(false);
        dispose();
    }
    
    public Puestos getReturnStatus() {
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
        t_nombre_puesto = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        t_descripcion_puesto = new javax.swing.JTextField();
        b_guardar1 = new javax.swing.JButton();
        b_cancelar = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Alta de puestos");
        setResizable(false);

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        t_nombre_puesto.setNextFocusableComponent(t_descripcion_puesto);
        t_nombre_puesto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                t_nombre_puestoActionPerformed(evt);
            }
        });
        t_nombre_puesto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                t_nombre_puestoKeyTyped(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(83, 103, 211));
        jLabel1.setText("Nombre");

        jLabel2.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel2.setText("Descripción");

        t_descripcion_puesto.setNextFocusableComponent(b_guardar1);
        t_descripcion_puesto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                t_descripcion_puestoActionPerformed(evt);
            }
        });
        t_descripcion_puesto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                t_descripcion_puestoKeyTyped(evt);
            }
        });

        b_guardar1.setBackground(new java.awt.Color(2, 135, 242));
        b_guardar1.setForeground(new java.awt.Color(254, 254, 254));
        b_guardar1.setIcon(new ImageIcon("imagenes/guardar.png"));
        b_guardar1.setText("Guardar");
        b_guardar1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        b_guardar1.setNextFocusableComponent(b_cancelar);
        b_guardar1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_guardar1ActionPerformed(evt);
            }
        });

        b_cancelar.setBackground(new java.awt.Color(2, 135, 242));
        b_cancelar.setForeground(new java.awt.Color(254, 254, 254));
        b_cancelar.setIcon(new ImageIcon("imagenes/cancelar.png"));
        b_cancelar.setText("Cancelar");
        b_cancelar.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        b_cancelar.setNextFocusableComponent(t_nombre_puesto);
        b_cancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_cancelarActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel3.setText("Nota: Los puestos identifican la función de cada empleado en el taller.");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addComponent(b_cancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(b_guardar1, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel2)
                                .addComponent(jLabel1))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(t_descripcion_puesto, javax.swing.GroupLayout.DEFAULT_SIZE, 342, Short.MAX_VALUE)
                                .addComponent(t_nombre_puesto))))
                    .addComponent(jLabel3))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(t_nombre_puesto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(t_descripcion_puesto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(b_guardar1, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(b_cancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel3)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void t_nombre_puestoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_t_nombre_puestoActionPerformed
        // TODO add your handling code here:
        t_descripcion_puesto.requestFocus();
    }//GEN-LAST:event_t_nombre_puestoActionPerformed

    private void t_descripcion_puestoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_t_descripcion_puestoActionPerformed
        // TODO add your handling code here:
        b_guardar1.requestFocus();
    }//GEN-LAST:event_t_descripcion_puestoActionPerformed

    private void b_guardar1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_guardar1ActionPerformed
        // TODO add your handling code here:
        b_cancelar.requestFocus();
        h=new Herramientas(user, 0);
        h.session(sessionPrograma);
        t_nombre_puesto.requestFocus();
        if(t_nombre_puesto.getText().compareTo("")!=0)
        {
            if(consultaPuesto(t_nombre_puesto.getText())==false)
            {
                Puestos nuevoPuesto = new Puestos();
                nuevoPuesto.setNombre(t_nombre_puesto.getText());
                nuevoPuesto.setDescripcion(t_descripcion_puesto.getText());
                Integer respuesta=guardarPuesto(nuevoPuesto);
                if(respuesta==null)
                    JOptionPane.showMessageDialog(null, "Error al guardar los datos");
                else
                {
                    JOptionPane.showMessageDialog(null, "Registro almacenado con la clave:  " +respuesta);
                    this.borra_cajas();
                    t_nombre_puesto.requestFocus();
                }
            }
            else
            {
                JOptionPane.showMessageDialog(null, "¡El nombre del puesto ya existe!");
                this.borra_cajas();
                t_nombre_puesto.requestFocus();
            }
        }   
        else
        {
            JOptionPane.showMessageDialog(null, "¡Debe introducir el nombre del puesto!");
            t_nombre_puesto.requestFocus();
        }
    }//GEN-LAST:event_b_guardar1ActionPerformed

    private void b_cancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_cancelarActionPerformed
        t_nombre_puesto.requestFocus();
        h=new Herramientas(user, 0);
        h.session(sessionPrograma);
        int opt=JOptionPane.showConfirmDialog(this, "¡Los datos capturados se eliminarán!");
        System.out.println(opt);
        if(opt==0)
            borra_cajas();
        t_nombre_puesto.requestFocus(); 
    }//GEN-LAST:event_b_cancelarActionPerformed

    private void t_nombre_puestoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_nombre_puestoKeyTyped
        evt.setKeyChar(Character.toUpperCase(evt.getKeyChar()));
        if(t_nombre_puesto.getText().length()>=30)
            evt.consume();
    }//GEN-LAST:event_t_nombre_puestoKeyTyped

    private void t_descripcion_puestoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_descripcion_puestoKeyTyped
        evt.setKeyChar(Character.toUpperCase(evt.getKeyChar()));
        if(t_descripcion_puesto.getText().length()>=150)
            evt.consume();
    }//GEN-LAST:event_t_descripcion_puestoKeyTyped


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton b_cancelar;
    private javax.swing.JButton b_guardar1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    public javax.swing.JPanel jPanel1;
    private javax.swing.JTextField t_descripcion_puesto;
    public javax.swing.JTextField t_nombre_puesto;
    // End of variables declaration//GEN-END:variables
    
    
    private List<Object[]> executeHQLQuery(String hql) {
        try {
            Session session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            Query q = session.createQuery(hql);
            List resultList = q.list();
            session.getTransaction().commit();
            session.disconnect();
            return resultList;
        }catch (HibernateException he) {
            he.printStackTrace();
            List lista= null;
            return lista;
        }
    }
    
    public boolean consultaPuesto(String nombre)
    {
        List <Object[]> resultList=executeHQLQuery("from Puestos obj where obj.nombre='"+nombre+"'");
        if(resultList.size()>0)
            return true;
        else
            return false;
    }
    
    private Integer guardarPuesto(Puestos obj) 
    {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Integer IdClientes = null;
        try 
        {
            session.beginTransaction();
            IdClientes=(Integer) session.save(obj);
            session.save(obj);
            session.getTransaction().commit();
        } 
        catch (HibernateException he) 
        {
            he.printStackTrace();
            session.getTransaction().rollback();
            IdClientes = null;
        }   
        finally
        {
            session.close();
            return IdClientes;
        }
    }
    
    private void cajas( boolean nombre, boolean descripcion, boolean guardar)
    {
        this.t_nombre_puesto.setEnabled(nombre);
        this.t_descripcion_puesto.setEnabled(descripcion);
        this.b_guardar1.setEnabled(guardar); 
    }
    
    private void borra_cajas()
    {
        this.t_nombre_puesto.setText("");
        this.t_descripcion_puesto.setText("");  
    }
}