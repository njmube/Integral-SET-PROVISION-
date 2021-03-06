/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Servicios;

import Hibernate.Util.HibernateUtil;
import Hibernate.entidades.Mensaje;
import Hibernate.entidades.Orden;
import Hibernate.entidades.Usuario;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import javax.swing.ImageIcon;
import org.hibernate.Session;
import Integral.Herramientas;

/**
 *
 * @author I.S.C Salvador
 */
public class Comentario extends javax.swing.JPanel{

    /**
     * Creates new form Comentario
     */
    Usuario user;
    String orden;
    String titulo="";
    Orden ord;
    Mensaje ms;
    String sessionPrograma="";
    Herramientas h;
    
    public Comentario(String ord, Usuario u, Mensaje msg, int index, String edo, String ses) {
        user=u;
        sessionPrograma=ses;
        orden=ord;
        ms=msg;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try
        {
            ms=(Mensaje)session.get(Mensaje.class, msg.getIdMensaje());
            DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");//YYYY-MM-DD HH:MM:SS
            String valor=dateFormat.format(ms.getFecha());
            titulo=ms.getUsuario().getEmpleado().getNombre()+"   ("+valor+")";
            initComponents();
            mensaje.setLineWrap(true); 
            mensaje.setText(ms.getMensaje());
            this.setVisible(true);
            if(u.getIdUsuario().compareTo(ms.getUsuario().getIdUsuario())==0)
            {
                this.btn1.setVisible(true);
                this.btn2.setVisible(true);
                this.btn3.setVisible(true);
                this.mensaje.setEditable(true);
            }
            else
            {
                this.btn1.setVisible(false);
                this.btn2.setVisible(false);
                this.btn3.setVisible(false);
                this.mensaje.setEditable(false);
            }
            btn1.setActionCommand("elimina_" + index);
            btn2.setActionCommand("actualiza_" + index);
            btn3.setActionCommand("correo_" + index);
            if(edo.compareTo("")==0)
                visualiza(true);
            else
                visualiza(false);

            h=new Herramientas(user, 0);
            if(h.isCerrada(orden)==true)
                visualiza(false);
        }catch(Exception e)
        {
            System.out.println(e);
        }
        if(session!=null)
            if(session.isOpen())
                session.close();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        p_general = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        mensaje = new javax.swing.JTextArea();
        btn2 = new javax.swing.JButton();
        btn1 = new javax.swing.JButton();
        btn3 = new javax.swing.JButton();

        setBackground(new java.awt.Color(255, 51, 204));
        setOpaque(false);

        p_general.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), titulo, javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Arial", 0, 10))); // NOI18N

        jScrollPane1.setBackground(new java.awt.Color(255, 255, 255));
        jScrollPane1.setOpaque(false);

        mensaje.setEditable(false);
        mensaje.setBackground(new java.awt.Color(240, 240, 240));
        mensaje.setColumns(20);
        mensaje.setFont(new java.awt.Font("Monospaced", 0, 10)); // NOI18N
        mensaje.setRows(3);
        mensaje.setTabSize(4);
        mensaje.setToolTipText("Ahora puedes modificar el comentario");
        mensaje.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                mensajeKeyTyped(evt);
            }
        });
        jScrollPane1.setViewportView(mensaje);

        btn2.setBackground(new java.awt.Color(2, 135, 242));
        btn2.setIcon(new ImageIcon("imagenes/boton_editar1.png"));
        btn2.setToolTipText("Actualizar comentario");
        btn2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn2ActionPerformed(evt);
            }
        });

        btn1.setBackground(new java.awt.Color(2, 135, 242));
        btn1.setIcon(new ImageIcon("imagenes/boton_cerrar1.png"));
        btn1.setToolTipText("Eliminar Comentario");
        btn1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn1ActionPerformed(evt);
            }
        });

        btn3.setBackground(new java.awt.Color(2, 135, 242));
        btn3.setIcon(new ImageIcon("imagenes/mail.png"));
        btn3.setToolTipText("Actualizar comentario");
        btn3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout p_generalLayout = new javax.swing.GroupLayout(p_general);
        p_general.setLayout(p_generalLayout);
        p_generalLayout.setHorizontalGroup(
            p_generalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, p_generalLayout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 694, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(p_generalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btn2, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn1, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );
        p_generalLayout.setVerticalGroup(
            p_generalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addComponent(jScrollPane1)
            .addGroup(p_generalLayout.createSequentialGroup()
                .addComponent(btn1, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(btn2, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btn3, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(p_general, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(p_general, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void mensajeKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_mensajeKeyTyped
        // TODO add your handling code here:
        if(mensaje.getText().length()>=500) 
            evt.consume();
    }//GEN-LAST:event_mensajeKeyTyped

    private void btn2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btn2ActionPerformed

    private void btn1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btn1ActionPerformed

    private void btn3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btn3ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton btn1;
    public javax.swing.JButton btn2;
    public javax.swing.JButton btn3;
    private javax.swing.JScrollPane jScrollPane1;
    public javax.swing.JTextArea mensaje;
    public javax.swing.JPanel p_general;
    // End of variables declaration//GEN-END:variables

    public void visualiza(Boolean valor)
    {
        this.btn1.setEnabled(valor);
        this.btn2.setEnabled(valor);
        this.mensaje.setEnabled(valor);
    }
}
