/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Marca;

import Hibernate.Util.HibernateUtil;
import Hibernate.entidades.Marca;
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

public class altaMarca extends javax.swing.JDialog {
    
    public static final Marca RET_CANCEL =null;
    InputMap map = new InputMap();
    DefaultTableModel model;
    Usuario user;
    String sessionPrograma;
    Herramientas h;
    private Marca returnStatus;
    
    /** Creates new form acceso */
    public altaMarca(java.awt.Frame parent, boolean modal, Usuario u, String ses) {
        super(parent, modal);
        user=u;
        sessionPrograma=ses;
        initComponents();
        id_marca.requestFocus();  
    }    
    
    private void doClose(Marca o) {
        returnStatus = o;
        setVisible(false);
        dispose();
    }
    
    public Marca getReturnStatus() {
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
        id_marca = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        marca_nombre = new javax.swing.JTextField();
        b_guardar1 = new javax.swing.JButton();
        b_cancelar = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        cb_ejemplar = new javax.swing.JComboBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Alta de marca");
        setResizable(false);

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        id_marca.setNextFocusableComponent(marca_nombre);
        id_marca.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                id_marcaActionPerformed(evt);
            }
        });
        id_marca.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                id_marcaKeyTyped(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(51, 0, 255));
        jLabel1.setText("Clave");

        jLabel2.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(51, 0, 255));
        jLabel2.setText("Nombre");

        marca_nombre.setNextFocusableComponent(b_guardar1);
        marca_nombre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                marca_nombreActionPerformed(evt);
            }
        });
        marca_nombre.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                marca_nombreKeyTyped(evt);
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
        b_cancelar.setNextFocusableComponent(id_marca);
        b_cancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_cancelarActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel3.setText("Nota: Identifica la marca de la unidad");

        cb_ejemplar.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Unidad", "Inventario", "Ambos" }));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(cb_ejemplar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(b_cancelar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(b_guardar1))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addGap(239, 239, 239))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(marca_nombre, javax.swing.GroupLayout.DEFAULT_SIZE, 379, Short.MAX_VALUE)
                            .addComponent(id_marca))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(id_marca, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(marca_nombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(b_guardar1, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(b_cancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cb_ejemplar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 15, Short.MAX_VALUE)
                .addComponent(jLabel3)
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

    private void id_marcaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_id_marcaActionPerformed
        id_marca.requestFocus();
    }//GEN-LAST:event_id_marcaActionPerformed

    private void marca_nombreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_marca_nombreActionPerformed
        b_guardar1.requestFocus();
    }//GEN-LAST:event_marca_nombreActionPerformed

    private void b_guardar1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_guardar1ActionPerformed
        h=new Herramientas(user, 0);
        h.session(sessionPrograma);               
        if(id_marca.getText().compareTo("")!=0)
        {
            if(this.marca_nombre.getText().compareTo("")!=0)
            {
                if(consultaMarca(id_marca.getText())==false)
                {
                    Marca nuevoMarca = new Marca();
                    if(id_marca.getText().compareTo("")!=0)
                        nuevoMarca.setIdMarca(id_marca.getText());
                    if(marca_nombre.getText().compareTo("")!=0)
                        nuevoMarca.setMarcaNombre(marca_nombre.getText());

                    nuevoMarca.setEjemplar(cb_ejemplar.getSelectedIndex());
                    String respuesta=guardarMarca(nuevoMarca);
                    if(respuesta==null)
                        JOptionPane.showMessageDialog(null, "Error al guardar los datos");
                    else
                    {
                        JOptionPane.showMessageDialog(null, "Registro almacenado");
                        this.borra_cajas();
                        id_marca.requestFocus();
                    }
                }
                else
                {
                    JOptionPane.showMessageDialog(null, "¡La clave de la marca ya existe!");
                    this.borra_cajas();
                    id_marca.requestFocus();
                }
            }
            else
            {
                JOptionPane.showMessageDialog(null, "¡Debe introducir el nombre de la marca!");            
                marca_nombre.requestFocus();
            }
        }
        else
        {
            JOptionPane.showMessageDialog(null, "¡Debe introducir la clave de la marca!");            
            id_marca.requestFocus();
        }
    }//GEN-LAST:event_b_guardar1ActionPerformed

    private void b_cancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_cancelarActionPerformed
        id_marca.requestFocus();
        int opt=JOptionPane.showConfirmDialog(this, "¡Los datos capturados se eliminarán!");
        if(opt==0)
        {
            borra_cajas();
            cajas(false, false);
        }
        id_marca.requestFocus();        
    }//GEN-LAST:event_b_cancelarActionPerformed

    private void id_marcaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_id_marcaKeyTyped
        evt.setKeyChar(Character.toUpperCase(evt.getKeyChar()));
        if(id_marca.getText().length()>=4)
            evt.consume();
    }//GEN-LAST:event_id_marcaKeyTyped

    private void marca_nombreKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_marca_nombreKeyTyped
        evt.setKeyChar(Character.toUpperCase(evt.getKeyChar()));
        if(marca_nombre.getText().length()>=20)
            evt.consume();
    }//GEN-LAST:event_marca_nombreKeyTyped

    /**
     * @param args the command line arguments
     */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton b_cancelar;
    private javax.swing.JButton b_guardar1;
    private javax.swing.JComboBox cb_ejemplar;
    public javax.swing.JTextField id_marca;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    public javax.swing.JPanel jPanel1;
    private javax.swing.JTextField marca_nombre;
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
    public boolean consultaMarca(String id_marca)
    {
        List <Object[]> resultList=executeHQLQuery("from Marca obj where obj.idMarca='"+id_marca+"'");
        if(resultList.size()>0)
            return true;
        else
            return false;
    }
    
    private String guardarMarca(Marca obj) 
    {
        Session session = HibernateUtil.getSessionFactory().openSession();
        String IdMarca = null;
        try 
        {
            session.beginTransaction();
            IdMarca=(String) session.save(obj);
            session.save(obj);
            session.getTransaction().commit();
        } 
        catch (HibernateException he) 
        {
            he.printStackTrace();
            session.getTransaction().rollback();
            IdMarca= null;
        }   
        finally
        {
            session.close();
            return IdMarca;
        }
    }
        
    private void cajas( boolean id, boolean marca)
    {
        this.id_marca.setEnabled(id);
        this.marca_nombre.setEnabled(marca);    
    }
    
    private void borra_cajas()
    {
        this.id_marca.setText("");
        this.marca_nombre.setText("");       
        this.cb_ejemplar.setSelectedIndex(0);
    }
}