/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Catalogo;

import Hibernate.Util.HibernateUtil;
import Hibernate.entidades.Catalogo;
import Hibernate.entidades.Especialidad;
import Hibernate.entidades.Partida;
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
import javax.swing.DefaultCellEditor;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
/**
 *
 * @author ESPECIALIZADO TOLUCA
 */
public class editaCatalogo extends javax.swing.JPanel {

    int[] especial;
    private Session session;
    private Catalogo actor;
    DefaultTableModel model;
    Especialidad especialidad;
    String[] columnas = new String [] {"Clave","Descripción", "Grupo Mecánico"};
    Usuario usr;
    String sessionPrograma="";
    Herramientas h;
    int x=0;
    int []id;
    
    public editaCatalogo(Usuario usuario, String ses) 
    {
        initComponents();
        cargaEspecialidad();
        sessionPrograma=ses;
        formatoTabla();
        usr=usuario;
        buscaDato();
    }

    
    DefaultTableModel ModeloTablaReporte(int renglones, String columnas[])
    {
        model = new DefaultTableModel(new Object [renglones][3], columnas)
        {
            Class[] types = new Class [] {
                java.lang.Integer.class,
                java.lang.String.class, 
                java.lang.String.class
                
            };
            boolean[] canEdit = new boolean [] {
                false,false,true
            };
            public void setValueAt(Object value, int row, int col)
            {
                Vector vector = (Vector)this.dataVector.elementAt(row);
                Object celda = ((Vector)this.dataVector.elementAt(row)).elementAt(col);
                switch(col)
                {
                    default:
                        vector.setElementAt(value, col);
                        this.dataVector.setElementAt(vector, row);
                        fireTableCellUpdated(row, col);
                        break;
                    case 2:
                        if(vector.get(col)==null)
                        {
                            vector.setElementAt(value, col);
                            this.dataVector.setElementAt(vector, row);
                            fireTableCellUpdated(row, col);
                        }
                        else
                        {
                            Session session = HibernateUtil.getSessionFactory().openSession();
                            try
                            {
                                session.beginTransaction().begin();
                                Catalogo cat= (Catalogo) session.createCriteria(Catalogo.class).add(Restrictions.eq("idCatalogo", Integer.parseInt(t_datos.getValueAt(t_datos.getSelectedRow(), 0).toString()))).setMaxResults(1).uniqueResult();
                                if(cat!=null)
                                {
                                    Especialidad esp=(Especialidad) session.createCriteria(Especialidad.class).add(Restrictions.eq("descripcion", (String)value)).setMaxResults(1).uniqueResult();
                                    if(esp!=null)
                                    {
                                        cat.setEspecialidad(esp);
                                        session.update(cat);
                                        session.getTransaction().commit();
                                        vector.setElementAt(value, col);
                                        this.dataVector.setElementAt(vector, row);
                                        fireTableCellUpdated(row, col);
                                    }
                                }
                            }
                            catch(Exception e)
                            {
                                session.getTransaction().rollback();
                                System.out.println(e);
                                JOptionPane.showMessageDialog(null, "Error al actualizar los datos"); 
                            }
                            finally
                            {
                                if(session.isOpen()==true)
                                    session.close();
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

        numeros = new javax.swing.JComboBox();
        jPanel1 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        t_datos = new javax.swing.JTable();
        Selecciona2 = new javax.swing.JButton();
        Eliminar1 = new javax.swing.JButton();
        Selecciona1 = new javax.swing.JButton();
        bt_actualiza1 = new javax.swing.JButton();
        t_busca = new javax.swing.JTextField();
        b_busca = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jLabel20 = new javax.swing.JLabel();
        t_numero = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        t_nombre = new javax.swing.JTextField();
        b_cancelar = new javax.swing.JButton();
        b_guardar = new javax.swing.JButton();
        c_catalogo = new javax.swing.JComboBox();
        jLabel17 = new javax.swing.JLabel();

        numeros.setFont(new java.awt.Font("Dialog", 0, 9)); // NOI18N
        numeros.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                numerosFocusLost(evt);
            }
        });

        setBackground(new java.awt.Color(255, 255, 255));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "Edición de descripciones", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Arial", 1, 12))); // NOI18N

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "Lista", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Arial", 1, 11))); // NOI18N

        jScrollPane1.setBackground(new java.awt.Color(254, 254, 254));

        t_datos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Clave", "Descripción", "Grupo Mecánico"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false
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
        t_datos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                t_datosMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(t_datos);

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

        Eliminar1.setBackground(new java.awt.Color(2, 135, 242));
        Eliminar1.setForeground(new java.awt.Color(254, 254, 254));
        Eliminar1.setIcon(new ImageIcon("imagenes/del-user.png"));
        Eliminar1.setText("Eliminar");
        Eliminar1.setToolTipText("Eliminar el registro actual");
        Eliminar1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        Eliminar1.setMaximumSize(new java.awt.Dimension(87, 23));
        Eliminar1.setMinimumSize(new java.awt.Dimension(87, 23));
        Eliminar1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Eliminar1ActionPerformed(evt);
            }
        });

        Selecciona1.setBackground(new java.awt.Color(2, 135, 242));
        Selecciona1.setForeground(new java.awt.Color(254, 254, 254));
        Selecciona1.setIcon(new ImageIcon("imagenes/update-user.png"));
        Selecciona1.setText("Seleccionar");
        Selecciona1.setToolTipText("Seleccionar un registro de la tabla para editar");
        Selecciona1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        Selecciona1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Selecciona1ActionPerformed(evt);
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

        t_busca.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                t_buscaActionPerformed(evt);
            }
        });

        b_busca.setIcon(new ImageIcon("imagenes/buscar1.png"));
        b_busca.setToolTipText("Busca una partida");
        b_busca.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_buscaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addGap(0, 142, Short.MAX_VALUE)
                        .addComponent(bt_actualiza1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(Selecciona1)
                        .addGap(18, 18, 18)
                        .addComponent(Eliminar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(Selecciona2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addComponent(t_busca)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(b_busca, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel3Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 441, Short.MAX_VALUE)
                    .addContainerGap()))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(t_busca, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(b_busca, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Selecciona2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Eliminar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Selecciona1)
                    .addComponent(bt_actualiza1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel3Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 331, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(80, Short.MAX_VALUE)))
        );

        jPanel4.setBackground(new java.awt.Color(254, 254, 254));
        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "Actualizar", javax.swing.border.TitledBorder.RIGHT, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Arial", 1, 11))); // NOI18N

        jLabel20.setText("Id:");

        t_numero.setEnabled(false);

        jLabel8.setForeground(new java.awt.Color(51, 0, 255));
        jLabel8.setText("Descripción:");

        t_nombre.setEnabled(false);
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
        b_cancelar.setEnabled(false);
        b_cancelar.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        b_cancelar.setMaximumSize(new java.awt.Dimension(87, 23));
        b_cancelar.setMinimumSize(new java.awt.Dimension(87, 23));
        b_cancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_cancelarActionPerformed(evt);
            }
        });

        b_guardar.setBackground(new java.awt.Color(2, 135, 242));
        b_guardar.setForeground(new java.awt.Color(254, 254, 254));
        b_guardar.setIcon(new ImageIcon("imagenes/guardar.png"));
        b_guardar.setText("Actualizar");
        b_guardar.setEnabled(false);
        b_guardar.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        b_guardar.setMaximumSize(new java.awt.Dimension(87, 23));
        b_guardar.setMinimumSize(new java.awt.Dimension(87, 23));
        b_guardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_guardarActionPerformed(evt);
            }
        });

        c_catalogo.setEnabled(false);
        c_catalogo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                c_catalogoActionPerformed(evt);
            }
        });

        jLabel17.setForeground(new java.awt.Color(51, 0, 255));
        jLabel17.setText("Grupo Mecánico:");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(34, 34, 34)
                        .addComponent(jLabel8)
                        .addGap(18, 18, 18)
                        .addComponent(t_nombre))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addGap(32, 32, 32)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                                .addComponent(b_cancelar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(b_guardar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                                .addComponent(jLabel20)
                                .addGap(18, 18, 18)
                                .addComponent(t_numero, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(jLabel17)
                                .addGap(18, 18, 18)
                                .addComponent(c_catalogo, 0, 264, Short.MAX_VALUE)))))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(t_numero, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(57, 57, 57)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(t_nombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(c_catalogo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(161, 161, 161)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(b_guardar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(b_cancelar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(29, 29, 29))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 32, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void t_nombreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_t_nombreActionPerformed
        this.c_catalogo.requestFocus();
    }//GEN-LAST:event_t_nombreActionPerformed

    private void t_nombreKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_nombreKeyTyped
        evt.setKeyChar(Character.toUpperCase(evt.getKeyChar()));
        if(t_nombre.getText().length()>=100)
        evt.consume();
    }//GEN-LAST:event_t_nombreKeyTyped

    private void b_cancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_cancelarActionPerformed
        int opt=JOptionPane.showConfirmDialog(this, "¡Los datos capturados se eliminarán!");
        if(opt==0)
        {
            borra_cajas();
            cajas(false, false, false, false, false, false);
        }
    }//GEN-LAST:event_b_cancelarActionPerformed

    private void b_guardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_guardarActionPerformed
        h=new Herramientas(usr, 0);
        h.session(sessionPrograma);
        session = HibernateUtil.getSessionFactory().openSession();
        usr = (Usuario)session.get(Usuario.class, usr.getIdUsuario());
        if(usr.getEditaCatalogo()==true)
        {
            t_nombre.setText(t_nombre.getText().trim());
            if(t_nombre.getText().compareTo("")!=0)
            {
                if(this.c_catalogo.getItemCount()>0)
                {
                    try
                    {
                        List <Object[]> resultList=executeHQLQuery("from Catalogo obj where obj.nombre='"+t_nombre.getText()+"'");
                        int bandera=0;
                        if(resultList.size()>0)
                        {
                            for (Object o : resultList)
                            {
                                Catalogo aux = (Catalogo) o;
                                if(aux.getIdCatalogo()!=Integer.parseInt(t_numero.getText()))
                                    bandera=1;
                            }
                        }
                        session.beginTransaction();
                        actor = (Catalogo)session.get(Catalogo.class, Integer.parseInt(t_numero.getText()));
                        if(bandera==0)
                        {
                            actor.setNombre(t_nombre.getText());
                            especialidad = (Especialidad)session.get(Especialidad.class, this.especial[c_catalogo.getSelectedIndex()]);//Articulo
                            actor.setEspecialidad(especialidad);
                            session.update(actor);
                            session.getTransaction().commit();
                            borra_cajas();
                            cargaEspecialidad();
                            buscaDato();
                            cajas(false, false, false, false, false, false);
                            JOptionPane.showMessageDialog(null, "Registro actualizado");
                        }
                        else
                        {
                            session.getTransaction().rollback();
                            session.close();
                            JOptionPane.showMessageDialog(null, "Ya existe una descripción identica");
                        }
                    }
                    catch (HibernateException he)
                    {
                        he.printStackTrace();
                        session.getTransaction().rollback();
                        session.close();
                        JOptionPane.showMessageDialog(null, "Error al guardar");
                    }
                }
                else
                    JOptionPane.showMessageDialog(null, "¡Debe dar de alta primero un grupo mecánico!");
            }
            else
            {
                JOptionPane.showMessageDialog(null, "¡Debe seleccionar la descripción!");
                t_nombre.requestFocus();
            }
        }
        else
            JOptionPane.showMessageDialog(null, "¡Acceso denegado!");
    }//GEN-LAST:event_b_guardarActionPerformed

    private void Selecciona2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Selecciona2ActionPerformed
        h=new Herramientas(usr, 0);
        h.session(sessionPrograma);
        session = HibernateUtil.getSessionFactory().openSession();
        usr = (Usuario)session.get(Usuario.class, usr.getIdUsuario());
        if(usr.getEditaCatalogo()==true)
        {
            altaCatalogo obj = new altaCatalogo(new javax.swing.JFrame(), true, usr, sessionPrograma);
            Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
            obj.setLocation((d.width/2)-(obj.getWidth()/2), (d.height/2)-(obj.getHeight()/2));
            obj.setVisible(true);
            borra_cajas();
            cajas(false, false, false, false, false, false);
            buscaDato();
        }
        else
            JOptionPane.showMessageDialog(null, "¡Acceso denegado!");
    }//GEN-LAST:event_Selecciona2ActionPerformed

    private void Eliminar1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Eliminar1ActionPerformed
        h=new Herramientas(usr, 0);
        h.session(sessionPrograma);
        session = HibernateUtil.getSessionFactory().openSession();
        usr = (Usuario)session.get(Usuario.class, usr.getIdUsuario());
        if(usr.getEditaCatalogo()==true)
        {
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
                        JOptionPane.showMessageDialog(null, "La descripción ha sido eliminada");
                        model.removeRow(a);
                        this.borra_cajas();
                        this.cajas(false, false, false, false, false, false);
                        buscaDato();
                    }
                }
            }
            else
            {
                JOptionPane.showMessageDialog(null, "Selecciona la descripción que desees eliminar");
                t_nombre.requestFocus();
            }
            cajas(false, false, false, false, false, false);
        }
        else
            JOptionPane.showMessageDialog(null, "Acceso denegado");
    }//GEN-LAST:event_Eliminar1ActionPerformed

    private void Selecciona1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Selecciona1ActionPerformed
        h=new Herramientas(usr, 0);
        h.session(sessionPrograma);
        session = HibernateUtil.getSessionFactory().openSession();
        usr = (Usuario)session.get(Usuario.class, usr.getIdUsuario());
        if(usr.getEditaCatalogo()==true)
        {
            if(t_datos.getSelectedRow()>=0)
            {
                borra_cajas();
                cargaEspecialidad();
                this.t_numero.setText(t_datos.getValueAt(t_datos.getSelectedRow(), 0).toString());
                this.t_nombre.setText(t_datos.getValueAt(t_datos.getSelectedRow(), 1).toString());
                this.c_catalogo.setSelectedItem(t_datos.getValueAt(t_datos.getSelectedRow(), 2).toString());//Articulo            
                this.cajas(false, true, true, true, true, true);
            }
            else
            JOptionPane.showMessageDialog(null, "¡No hay una descripción seleccionada!");
        }
        else
            JOptionPane.showMessageDialog(null, "¡Acceso denegado!");
    }//GEN-LAST:event_Selecciona1ActionPerformed

    private void bt_actualiza1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bt_actualiza1ActionPerformed
        this.borra_cajas();
        this.cajas(false, false, false, false, false, false);
        buscaDato();
    }//GEN-LAST:event_bt_actualiza1ActionPerformed

    private void c_catalogoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_c_catalogoActionPerformed
        //b_guardar.requestFocus();
    }//GEN-LAST:event_c_catalogoActionPerformed

    private void t_datosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_t_datosMouseClicked
        // TODO add your handling code here:
        if(t_datos.getSelectedRow()>=0)
        {
            if(t_datos.getSelectedColumn()==2)
            {
                numeros.removeAllItems();
                Session session = HibernateUtil.getSessionFactory().openSession();
                try
                {
                    session.beginTransaction().begin();
                    Especialidad[] datos=(Especialidad[]) session.createCriteria(Especialidad.class).addOrder(Order.desc("idGrupoMecanico")).list().toArray(new Especialidad[0]);
                    for(int k=0;k<datos.length;k++) 
                    {
                        id=new int[datos.length];
                        for(int f=0;f<(datos.length-1)-k;f++) 
                        {
                           id[f]=datos[f].getIdGrupoMecanico();
                           numeros.addItem(datos[f].getDescripcion());
                        }
                    }
                    session.beginTransaction().commit();
                }catch(Exception e)
                {
                     e.printStackTrace();
                }
                if(session!=null)
                     if(session.isOpen()==true)
                         session.close();
            }
        }
    }//GEN-LAST:event_t_datosMouseClicked

    private void numerosFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_numerosFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_numerosFocusLost

    private void b_buscaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_buscaActionPerformed
        // TODO add your handling code here:
        if(this.t_busca.getText().compareToIgnoreCase("")!=0)
        {
            //buscaCuentas();
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
    }//GEN-LAST:event_b_buscaActionPerformed

    private void t_buscaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_t_buscaActionPerformed
        // TODO add your handling code here:
        if(this.t_busca.getText().compareToIgnoreCase("")!=0)
        {
            //buscaCuentas();
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


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Eliminar1;
    private javax.swing.JButton Selecciona1;
    private javax.swing.JButton Selecciona2;
    private javax.swing.JButton b_busca;
    private javax.swing.JButton b_cancelar;
    private javax.swing.JButton b_guardar;
    private javax.swing.JButton bt_actualiza1;
    public javax.swing.JComboBox c_catalogo;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    public javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JComboBox numeros;
    private javax.swing.JTextField t_busca;
    private javax.swing.JTable t_datos;
    public javax.swing.JTextField t_nombre;
    public javax.swing.JTextField t_numero;
    // End of variables declaration//GEN-END:variables
    
    public void cargaEspecialidad()
    {
        List <Object[]> resultList=executeHQLQuery("from Especialidad");
        if(resultList.size()>0)
        {
            c_catalogo.removeAllItems();
            especial= new int [resultList.size()];
            int x=0;
            for (Object o : resultList)
            {
                especialidad = (Especialidad) o;
                c_catalogo.addItem(especialidad.getDescripcion());
                especial[x]=especialidad.getIdGrupoMecanico();
                x++;
            }
        }
        if(session.isOpen())            
        session.close();
    }

    public void borra_cajas()
    {
        this.t_numero.setText("");
        this.t_nombre.setText("");
    }

    public void cajas(boolean numero, boolean nombre, boolean direccion, boolean cancelar, boolean guardar, boolean puesto)
    {
        this.t_numero.setEnabled(numero);
        this.t_nombre.setEnabled(nombre);
        this.c_catalogo.setEnabled(puesto);
        this.b_cancelar.setEnabled(cancelar);
        this.b_guardar.setEnabled(guardar);
    }
    
    private List<Object[]> executeHQLQuery(String hql) {
        session = HibernateUtil.getSessionFactory().openSession();
        try {
            session.beginTransaction();
            Query q = session.createQuery(hql);
            List resultList = q.list();
            session.getTransaction().commit();
            return resultList;
        } catch (HibernateException he) {
            he.printStackTrace();
            List lista= null;
            return lista;
        }
    }
    
    private boolean eliminar(String idCatalogo)
    {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try
        {
            session.beginTransaction();
            actor = (Catalogo)session.get(Catalogo.class, Integer.parseInt(idCatalogo));
            
            if(actor.getPartidas().isEmpty()==false) 
            {
                session.getTransaction().rollback();
                JOptionPane.showMessageDialog(null, "¡La descripción esta en uso una partida no se puede eliminar!");
                return false;
            }
            else
            {
                session.delete(actor);
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
        String consulta="from Catalogo cat ORDER BY cat.especialidad.descripcion, cat.nombre ";
        List <Object[]> resultList=executeHQLQuery(consulta);
        if(resultList.size()>0)
        {
            t_datos.setModel(ModeloTablaReporte(resultList.size(), columnas));
            int i=0;
            for (Object o : resultList)
            {
                actor = (Catalogo) o;
                model.setValueAt(actor.getIdCatalogo(), i, 0);
                model.setValueAt(actor.getNombre(), i, 1);
                model.setValueAt(actor.getEspecialidad().getDescripcion(), i, 2);
                i++;
            }
        }
        else
            t_datos.setModel(ModeloTablaReporte(0, columnas));
        formatoTabla();
        session.close();
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
                      column.setPreferredWidth(300);
                      break;
                  case 2:
                      column.setPreferredWidth(100);
                      DefaultCellEditor miEditor = new DefaultCellEditor(numeros);
                      miEditor.setClickCountToStart(2);
                      column.setCellEditor(miEditor); 
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
