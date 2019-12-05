/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Especialidad;

import Hibernate.Util.HibernateUtil;
import Hibernate.entidades.Especialidad;
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
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
/**
 *
 * @author ESPECIALIZADO TOLUCA
 */
public class altaEspecialidad extends javax.swing.JDialog {

    public static final Especialidad RET_CANCEL =null;
    InputMap map = new InputMap();
    DefaultTableModel model;
    int[] puestos;
    Usuario user;
    String sessionPrograma;
    Herramientas h;
    private Especialidad returnStatus;
    
    public altaEspecialidad(java.awt.Frame parent, boolean modal, Usuario u, String ses) {
        super(parent, modal);
        user=u;
        sessionPrograma=ses;
        initComponents();
        grupomecanico.requestFocus();
    }
    
    private void doClose(Especialidad o) {
        returnStatus = o;
        setVisible(false);
        dispose();
    }
    
    public Especialidad getReturnStatus() {
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
        jLabel2 = new javax.swing.JLabel();
        grupomecanico = new javax.swing.JTextField();
        b_guardar1 = new javax.swing.JButton();
        b_cancelar = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Alta de grupo mecanico");
        setResizable(false);

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel2.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(51, 0, 255));
        jLabel2.setText("Grupo Mecánico");

        grupomecanico.setNextFocusableComponent(b_guardar1);
        grupomecanico.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                grupomecanicoKeyTyped(evt);
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
        b_cancelar.setNextFocusableComponent(grupomecanico);
        b_cancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_cancelarActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel3.setText("Nota: Los grupos mecanicos sirven para agrupar las partidas de una orden.");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(grupomecanico, javax.swing.GroupLayout.PREFERRED_SIZE, 342, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel3))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(b_cancelar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(b_guardar1)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(0, 12, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(grupomecanico, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(b_guardar1, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(b_cancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void b_guardar1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_guardar1ActionPerformed
        h=new Herramientas(user, 0);
        h.session(sessionPrograma);   
        grupomecanico.requestFocus();
        if(grupomecanico.getText().compareTo("")==0)
        {
            JOptionPane.showMessageDialog(null, "¡Debe introducir el nombre de la especialidad!");
            grupomecanico.requestFocus();
        }
        else
        {
            if(consultaEspecialidad(grupomecanico.getText())==false)
            {
                Especialidad nuevoEspecialidad = new Especialidad();
                if(grupomecanico.getText().compareTo("")!=0)
                nuevoEspecialidad.setDescripcion(grupomecanico.getText());
                nuevoEspecialidad.setPlantilla(true);
                Integer respuesta=guardarEspecialidad(nuevoEspecialidad);
                if(respuesta==null)
                {
                    cajas(true, true);
                    b_guardar1.setEnabled(true);
                }
                else
                {
                    JOptionPane.showMessageDialog(null, "Registro almacenado con la clave:  " +respuesta);
                    this.borra_cajas();
                    grupomecanico.requestFocus();
                    this.returnStatus=nuevoEspecialidad;
                }
            }
            else
            {
                JOptionPane.showMessageDialog(null, "¡El nombre de la especialidad ya existe!");
                this.borra_cajas();
                grupomecanico.requestFocus();
            }
        }    
    }//GEN-LAST:event_b_guardar1ActionPerformed

    private void b_cancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_cancelarActionPerformed
        int opt=JOptionPane.showConfirmDialog(this, "¡Los datos capturados se eliminarán!");
        if(opt==0)
            borra_cajas();
        grupomecanico.requestFocus(); 
    }//GEN-LAST:event_b_cancelarActionPerformed

    private void grupomecanicoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_grupomecanicoKeyTyped
        evt.setKeyChar(Character.toUpperCase(evt.getKeyChar()));
        if(grupomecanico.getText().length()>=100)
            evt.consume();
    }//GEN-LAST:event_grupomecanicoKeyTyped

    /**
     * @param args the command line arguments
     */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton b_cancelar;
    private javax.swing.JButton b_guardar1;
    public javax.swing.JTextField grupomecanico;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    public javax.swing.JPanel jPanel1;
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
    
    public boolean consultaEspecialidad(String descripcion)
    {
        List <Object[]> resultList=executeHQLQuery("from Especialidad obj where obj.descripcion='"+descripcion+"'");
        if(resultList.size()>0)
            return true;
        else
            return false;
    }
    
    private Integer guardarEspecialidad(Especialidad obj) 
    {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Integer IdClientes = null;
        try 
        {
            session.beginTransaction();
            Especialidad esp =(Especialidad) session.createCriteria(Especialidad.class).addOrder(Order.desc("orden")).setMaxResults(1).uniqueResult();
            obj.setOrden(esp.getOrden()+1);
            IdClientes=(Integer) session.save(obj);
            session.save(obj);
            session.getTransaction().commit();
            return null;
        } 
        catch (HibernateException he) 
        {
            he.printStackTrace();
            session.getTransaction().rollback();
            return null;
         }   
         finally
         {
            session.close();
            return IdClientes;
        }
    }
    
    private void cajas( boolean descripcion, boolean guardar)
    {
        this.grupomecanico.setEnabled(descripcion);
        this.b_guardar1.setEnabled(guardar); 
    }
    
    private void borra_cajas()
    {
        this.grupomecanico.setText("");  
    }
}