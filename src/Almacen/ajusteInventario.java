/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Almacen;

import Hibernate.Util.HibernateUtil;
import Hibernate.entidades.Ejemplar;
import Ejemplar.buscaEjemplar;
import Ejemplar.editaEjemplar;
import Hibernate.entidades.Almacen;
import Hibernate.entidades.Movimiento;
import Hibernate.entidades.Usuario;
import Integral.Herramientas;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.swing.JOptionPane;
import org.hibernate.HibernateException;
import org.hibernate.Session;

/**
 *
 * @author Angel
 */
public class ajusteInventario extends javax.swing.JPanel {

    /**
     * Creates new form ajusteInventario
     */
    Usuario actor;
    String sessionPrograma="";
    Herramientas h;
    editaEjemplar eEjemplar;
    int configuracion=1;
    
    public ajusteInventario(Usuario usuario, String ses, int configuracion) {
        initComponents();
        this.configuracion=configuracion;
        actor=usuario;
        sessionPrograma=ses;
        
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        cb_almacen = new javax.swing.JComboBox();
        jPanel2 = new javax.swing.JPanel();
        l_catalogo = new javax.swing.JLabel();
        l_marca = new javax.swing.JLabel();
        l_tipo1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();
        jTextField6 = new javax.swing.JTextField();
        jTextField7 = new javax.swing.JTextField();
        jTextField9 = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jTextField4 = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox();
        jLabel7 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jButton3 = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();

        setBackground(new java.awt.Color(255, 255, 255));
        setLayout(new java.awt.BorderLayout());

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setPreferredSize(new java.awt.Dimension(800, 60));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel1.setText("AJUSTE DE INVENTARIO");

        cb_almacen.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "ALMACEN 1", "ALMACEN 2" }));
        cb_almacen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cb_almacenActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cb_almacen, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(455, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cb_almacen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addContainerGap(18, Short.MAX_VALUE))
        );

        add(jPanel1, java.awt.BorderLayout.PAGE_START);

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setPreferredSize(new java.awt.Dimension(800, 480));

        l_catalogo.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        l_catalogo.setText("Descripción:");

        l_marca.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        l_marca.setText("Marca:");

        l_tipo1.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        l_tipo1.setText("Unidad:");

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel2.setText("N° Ejemplar:");

        jTextField1.setEditable(false);
        jTextField1.setBackground(new java.awt.Color(255, 255, 255));
        jTextField1.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(0, 0, 0)));

        jButton2.setBackground(new java.awt.Color(2, 135, 242));
        jButton2.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jButton2.setForeground(new java.awt.Color(255, 255, 255));
        jButton2.setText("Buscar");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jTextField6.setEditable(false);
        jTextField6.setBackground(new java.awt.Color(255, 255, 255));
        jTextField6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField6ActionPerformed(evt);
            }
        });

        jTextField7.setEditable(false);
        jTextField7.setBackground(new java.awt.Color(255, 255, 255));

        jTextField9.setEditable(false);
        jTextField9.setBackground(new java.awt.Color(255, 255, 255));

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel5.setText("Existencias:");

        jTextField3.setEditable(false);
        jTextField3.setBackground(new java.awt.Color(255, 255, 255));

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel6.setText("Ajuste:");

        jTextField4.setEditable(false);
        jTextField4.setText("0.0");
        jTextField4.setToolTipText("");
        jTextField4.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextField4FocusLost(evt);
            }
        });
        jTextField4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField4ActionPerformed(evt);
            }
        });
        jTextField4.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField4KeyTyped(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel3.setText("Tipo Ajuste:");

        jComboBox1.setBackground(new java.awt.Color(2, 135, 242));
        jComboBox1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jComboBox1.setForeground(new java.awt.Color(255, 255, 255));
        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Entrada", "Salida" }));
        jComboBox1.setEnabled(false);

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel7.setText("Nota:");

        jTextArea1.setEditable(false);
        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jTextArea1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextArea1KeyTyped(evt);
            }
        });
        jScrollPane2.setViewportView(jTextArea1);

        jButton3.setBackground(new java.awt.Color(2, 135, 242));
        jButton3.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jButton3.setForeground(new java.awt.Color(255, 255, 255));
        jButton3.setText("Cancelar");
        jButton3.setEnabled(false);
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton1.setBackground(new java.awt.Color(2, 135, 242));
        jButton1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText("Guardar");
        jButton1.setEnabled(false);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(21, 21, 21)
                                .addComponent(jLabel2)
                                .addGap(7, 7, 7))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addGap(42, 42, 42)
                                .addComponent(l_marca)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)))
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton2)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jTextField7)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(l_tipo1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextField9, javax.swing.GroupLayout.PREFERRED_SIZE, 339, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel3)
                                    .addComponent(jLabel5)
                                    .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.TRAILING))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jComboBox1, 0, 131, Short.MAX_VALUE)
                                    .addComponent(jTextField3)
                                    .addComponent(jTextField4))
                                .addGap(109, 109, 109)
                                .addComponent(jLabel7)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(2, 2, 2)
                                .addComponent(l_catalogo)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, 715, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE)))))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(l_marca, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(l_tipo1, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(l_catalogo, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                            .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel2Layout.createSequentialGroup()
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel7)
                                .addComponent(jLabel5)
                                .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel3))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel6)
                                .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGap(23, 23, 23)))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(301, Short.MAX_VALUE))
        );

        add(jPanel2, java.awt.BorderLayout.CENTER);

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setPreferredSize(new java.awt.Dimension(800, 120));
        jPanel3.setRequestFocusEnabled(false);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 827, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 120, Short.MAX_VALUE)
        );

        add(jPanel3, java.awt.BorderLayout.PAGE_END);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        borrar();
        jTextField4.setEditable(false);
        jTextField4.setText("0.0");
        jTextArea1.setEditable(false);
        jComboBox1.setEnabled(false);
        h=new Herramientas(this.actor, 0);
        h.session(sessionPrograma);
        Session session = HibernateUtil.getSessionFactory().openSession();
        try{
            actor = (Usuario)session.get(Usuario.class, actor.getIdUsuario());
            if(actor.getConsultarEjemplar()==true)
             {
                buscaEjemplar obj = new buscaEjemplar(new javax.swing.JFrame(), true, sessionPrograma, actor, 1);
                obj.t_busca.requestFocus();
                Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
                obj.setLocation((d.width/2)-(obj.getWidth()/2), (d.height/2)-(obj.getHeight()/2));
                obj.setVisible(true);
                Ejemplar orden_act=obj.getReturnStatus();
                
                if (orden_act!=null)
                {
                    orden_act=(Ejemplar)session.get(Ejemplar.class, orden_act.getIdParte());
                    jTextField1.setText(orden_act.getIdParte());
                    if(orden_act.getMarca()!=null)
                        jTextField7.setText(orden_act.getMarca().getMarcaNombre());
                    jTextField9.setText(orden_act.getMedida());
                    jTextField6.setText(orden_act.getComentario());
                    if(cb_almacen.getSelectedIndex()==0)
                    {
                        if(orden_act.getExistencias()!=null)
                            jTextField3.setText(orden_act.getExistencias().toString());
                        else
                            jTextField3.setText("0");
                    }
                    else
                    {
                        if(orden_act.getExistencias2()!=null)
                            jTextField3.setText(orden_act.getExistencias2().toString());
                        else
                            jTextField3.setText("0");
                    }
                    
                    jTextField4.setEditable(true);
                    jTextField4.setText("0.0");
                    jTextArea1.setEditable(true);
                    jComboBox1.setEnabled(true);
                    jButton3.setEnabled(true);
                    jButton1.setEnabled(true);
                    session.beginTransaction().commit();
                }
             }else
                JOptionPane.showMessageDialog(null, "¡Acceso denegados!");
        }catch(Exception e){
            e.printStackTrace();
        }
         if(session!=null)
            if(session.isOpen())
                session.close();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
         h=new Herramientas(actor, 0);
        h.session(sessionPrograma);
        try{
            if(jTextField4.getText().compareTo("")!=0){
                if(jTextArea1.getText().compareTo("")!=0){
                    if(Double.parseDouble(jTextField4.getText())>0.0){
                        Date fecha_almacen = new Date();
                        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                        if(jComboBox1.getSelectedItem().toString().compareTo("Entrada")==0){
                            //ENTRADA
                            try{
                                Almacen almacen = new Almacen();
                                almacen.setUsuario(actor);
                                almacen.setEntrego("");
                                String valor=dateFormat.format(fecha_almacen);
                                String [] fecha = valor.split("-");
                                String [] hora=fecha[2].split(":");
                                String [] aux=hora[0].split(" ");
                                fecha[2]=aux[0];
                                hora[0]=aux[1];
                                Calendar calendario = Calendar.getInstance();
                                calendario.set(
                                    Integer.parseInt(fecha[2]),
                                    Integer.parseInt(fecha[1])-1,
                                    Integer.parseInt(fecha[0]),
                                    Integer.parseInt(hora[0]),
                                    Integer.parseInt(hora[1]),
                                    Integer.parseInt(hora[2]));
                                almacen.setFecha(calendario.getTime());
                                almacen.setTipoMovimiento(1);
                                almacen.setOperacion(9);
                                almacen.setNotas(jTextArea1.getText()+"["+cb_almacen.getSelectedItem().toString()+"]");
                                almacen.setAlmacen(""+cb_almacen.getSelectedIndex());

                                Integer respuesta = guardaAlmacen(almacen);
                                if(respuesta!=null){
                                    //session.close();
                                    JOptionPane.showMessageDialog(null, "Ajuste Almacenado con la Clave "+respuesta);
                                    Almacen actual=new Almacen();
                                    actual.setIdAlmacen(respuesta);
                                    formatosOrden f1=new formatosOrden(this.actor, this.sessionPrograma, actual, configuracion);
                                    f1.formato(false);
                                    borrar();
                                    jTextField4.setEditable(false);
                                    jTextField4.setText("0.0");
                                    jComboBox1.setEnabled(false);
                                    jTextArea1.setEditable(false);
                                    jButton3.setEnabled(false);
                                    jButton1.setEnabled(false);
                                }else{
                                    JOptionPane.showMessageDialog(null, "Error al Relaizar el Ajuste");
                                }
                            }catch(Exception e){
                                e.printStackTrace();
                                JOptionPane.showMessageDialog(this, "Error al Realizar el Ajuste");
                            }
                        }else{
                            //SALIDA
                            if(Double.parseDouble(jTextField4.getText())> Double.parseDouble(jTextField3.getText())){
                                JOptionPane.showMessageDialog(this, "El Número Máximo para Realizar el Ajuste es "+jTextField3.getText());
                            }else{
                                try{
                                    Almacen almacen = new Almacen();
                                    almacen.setUsuario(actor);
                                almacen.setEntrego("");
                                String valor=dateFormat.format(fecha_almacen);
                                String [] fecha = valor.split("-");
                                String [] hora=fecha[2].split(":");
                                String [] aux=hora[0].split(" ");
                                fecha[2]=aux[0];
                                hora[0]=aux[1];
                                Calendar calendario = Calendar.getInstance();
                                calendario.set(
                                    Integer.parseInt(fecha[2]),
                                    Integer.parseInt(fecha[1])-1,
                                    Integer.parseInt(fecha[0]),
                                    Integer.parseInt(hora[0]),
                                    Integer.parseInt(hora[1]),
                                    Integer.parseInt(hora[2]));
                                almacen.setFecha(calendario.getTime());
                                almacen.setTipoMovimiento(2);
                                almacen.setOperacion(9);
                                almacen.setNotas(jTextArea1.getText());
                                Integer respuesta = guardaAlmacen(almacen);
                                    if(respuesta!=null){
                                        JOptionPane.showMessageDialog(null, "Ajuste Almacenado con la Clave "+respuesta);
                                        Almacen actual=new Almacen();
                                        actual.setIdAlmacen(respuesta);
                                        formatosOrden f1=new formatosOrden(this.actor, this.sessionPrograma, actual, configuracion);
                                        f1.formato(false);
                                        borrar();
                                        jTextField4.setEditable(false);
                                        jTextField4.setText("0.0");
                                        jComboBox1.setEnabled(false);
                                        jTextArea1.setEditable(false);
                                        jButton3.setEnabled(false);
                                        jButton1.setEnabled(false);
                                    }else{
                                        JOptionPane.showMessageDialog(null, "Error al Realizar el Ajuste");
                                    }

                                }catch(Exception e){
                                    e.printStackTrace();
                                    JOptionPane.showMessageDialog(null, "Error al Realizar el Ajuste");
                                }
                            }
                        }
                    }else{
                        JOptionPane.showMessageDialog(null, "El Ajuste debe ser mayor a 0.0");
                        jTextField4.requestFocus();
                    }
                }else{
                    jTextArea1.requestFocus();
                }
            }else{
                jTextField4.requestFocus();
            }
            
        }catch(Exception e){
            e.printStackTrace();
        }
    }//GEN-LAST:event_jButton1ActionPerformed
      private Integer guardaAlmacen(Almacen obj) 
    {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Integer  IdAlmacen = null;
        try 
        {
            session.beginTransaction().begin();
            IdAlmacen=(Integer) session.save(obj);
            Almacen alm = (Almacen)session.get(Almacen.class, IdAlmacen);
            
               //MOVIMIENTOS
               Movimiento move = new Movimiento();
                move.setAlmacen(alm);
                move.setCantidad(Double.parseDouble(jTextField4.getText()));
                Ejemplar ejemplar=(Ejemplar)session.get(Ejemplar.class, jTextField1.getText());
                move.setEjemplar(ejemplar);
                alm.addMovimiento(move);

                //EJEMPLARES
                double ex=0.0;
                if(jComboBox1.getSelectedItem().toString().compareTo("Entrada")==0){
                    if(cb_almacen.getSelectedIndex()==0)
                    {
                        if(ejemplar.getExistencias()!=null)
                            ex=ejemplar.getExistencias();
                        ejemplar.setExistencias(ex+Double.parseDouble(jTextField4.getText().toString()));
                    }
                    else
                    {
                        if(ejemplar.getExistencias2()!=null)
                            ex=ejemplar.getExistencias2();
                        ejemplar.setExistencias2(ex+Double.parseDouble(jTextField4.getText().toString()));
                    }
                    session.update(ejemplar);
                }else{
                    if(cb_almacen.getSelectedIndex()==0)
                    {
                        if(ejemplar.getExistencias()!=null)
                            ex=ejemplar.getExistencias();
                        ejemplar.setExistencias(ex-Double.parseDouble(jTextField4.getText().toString()));
                    }
                    else
                    {
                        if(ejemplar.getExistencias2()!=null)
                            ex=ejemplar.getExistencias2();
                        ejemplar.setExistencias2(ex-Double.parseDouble(jTextField4.getText().toString()));
                    }
                    session.update(ejemplar);
                }
                
            session.update(alm);
            session.beginTransaction().commit();
        } 
        catch (HibernateException he) 
        {
            session.beginTransaction().rollback();
            he.printStackTrace();
            IdAlmacen= null;
        }   
        finally
        {
            session.close();
            return IdAlmacen;
        }
    }
    
    private void jTextField4KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField4KeyTyped
        // TODO add your handling code here:
        char caracter = evt.getKeyChar();
        if(((caracter < '0') || (caracter > '9')) && (caracter != KeyEvent.VK_BACK_SPACE) && (caracter !='.')){
                evt.consume();
        }
        if(jTextField4.getText().contains(".")==true && caracter =='.')
            evt.consume();
    }//GEN-LAST:event_jTextField4KeyTyped

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        borrar();
        jTextField4.setEditable(false);
        jTextField4.setText("0.0");
        jTextArea1.setEditable(false);
        jComboBox1.setEnabled(false);
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jTextArea1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextArea1KeyTyped
        // TODO add your handling code here:
        evt.setKeyChar(Character.toUpperCase(evt.getKeyChar()));
        if(jTextArea1.getText().length()>=255)
        evt.consume();
    }//GEN-LAST:event_jTextArea1KeyTyped

    private void jTextField4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField4ActionPerformed
        // TODO add your handling code here:
        this.jTextField4FocusLost(null);
    }//GEN-LAST:event_jTextField4ActionPerformed

    private void jTextField4FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField4FocusLost
        // TODO add your handling code here:
        double ajuste=Double.parseDouble(jTextField4.getText());
        jTextField4.setText(String.valueOf(ajuste));
    }//GEN-LAST:event_jTextField4FocusLost

    private void cb_almacenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cb_almacenActionPerformed
        // TODO add your handling code here:
        borrar();
        jTextField4.setEditable(false);
        jTextField4.setText("0.0");
        jComboBox1.setEnabled(false);
        jTextArea1.setEditable(false);
        jButton3.setEnabled(false);
        jButton1.setEnabled(false);
    }//GEN-LAST:event_cb_almacenActionPerformed

    private void jTextField6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField6ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField6ActionPerformed
    
    public void borrar(){
        jTextField1.setText("");
        jTextField7.setText("");
        jTextField9.setText("");
        jTextField6.setText("");
        jTextField3.setText("");
        jTextField4.setText("");
        jComboBox1.setSelectedIndex(0);
        jTextArea1.setText("");
    }
public javax.swing.JTabbedPane P_pestana;
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox cb_almacen;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JTextField jTextField7;
    private javax.swing.JTextField jTextField9;
    private javax.swing.JLabel l_catalogo;
    private javax.swing.JLabel l_marca;
    private javax.swing.JLabel l_tipo1;
    // End of variables declaration//GEN-END:variables
}
