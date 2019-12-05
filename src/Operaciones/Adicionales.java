/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Operaciones;

import Empleados.buscaEmpleado;
import Hibernate.Util.HibernateUtil;
import Hibernate.entidades.Configuracion;
import Hibernate.entidades.Empleado;
import Hibernate.entidades.Orden;
import Hibernate.entidades.TrabajoExtra;
import Hibernate.entidades.Usuario;
import Integral.ExtensionFileFilter;
import Integral.Render1;
import Integral.configuracion;
import Servicios.buscaOrden;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Vector;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author I.S.C.Salvador
 */
public class Adicionales extends javax.swing.JPanel {

    private Usuario usr;
    private int ventana;
    private String sessionPrograma;
    private int configuracion;
    private DefaultTableModel model3;
    private String[] columna = new String[] {"id", "Empleado", "Descripcion","Monto", "Aut"};
    /**
     * Creates new form adicionales
     */
    public Adicionales(Usuario usr, int ventana, String sessionPrograma, int configuracion) {
        initComponents();
        this.usr=usr;
        this.ventana=ventana;
        this.sessionPrograma=sessionPrograma;
        this.configuracion=configuracion;
        DefaultTableModel modelo= ModeloTablaReporte(0,columna);
        t_datos.setModel(modelo);
        tabla_tamaños();
    }

    void tabla_tamaños()
    {
        TableColumnModel col_model = t_datos.getColumnModel();
        for (int i=0; i<t_datos.getColumnCount(); i++)
        {
  	      TableColumn column = col_model.getColumn(i);
              switch(i)
              {
                  case 0:
                      column.setPreferredWidth(90);
                      break;
                  case 1:
                      column.setPreferredWidth(220);
                      break;
                  case 2:
                      column.setPreferredWidth(400);
                      break;    
                  case 3:
                      column.setPreferredWidth(90);
                      break;
                  case 4:
                      column.setPreferredWidth(10);
                      break;
              }
        }
        JTableHeader header = t_datos.getTableHeader();
        header.setForeground(Color.white);
        Color c1 = new java.awt.Color(2, 135, 242);
        for(int x=0; x<t_datos.getColumnModel().getColumnCount(); x++)
            t_datos.getColumnModel().getColumn(x).setHeaderRenderer(new Render1(c1));
        t_datos.setShowVerticalLines(true);
        t_datos.setShowHorizontalLines(true);
    }
    
    DefaultTableModel ModeloTablaReporte(int renglones, String columnas[])
        {
            model3 = new DefaultTableModel(new Object [renglones][5], columnas)
            {
                Class[] types = new Class [] {
                    java.lang.String.class, 
                    java.lang.String.class, 
                    java.lang.String.class, 
                    java.lang.Double.class, 
                    java.lang.Boolean.class
                };
                boolean[] canEdit = new boolean [] {
                    false, false, true, true, true
                };

                public void setValueAt(Object value, int row, int col)
                {
                        Vector vector = (Vector)this.dataVector.elementAt(row);
                        Object celda = ((Vector)this.dataVector.elementAt(row)).elementAt(col);
                        Session session = HibernateUtil.getSessionFactory().openSession();
                        switch(col)
                        {
                            case 2:
                                    session.beginTransaction().begin();
                                    try{
                                        TrabajoExtra extra=(TrabajoExtra)session.get(TrabajoExtra.class, Integer.parseInt(t_datos.getValueAt(t_datos.getSelectedRow(), 0).toString())); 
                                        usr = (Usuario)session.get(Usuario.class, usr.getIdUsuario());

                                        if(usr.getAutorizaTrabajo()==true || usr.getGeneraAdicional()==true){
                                            extra.setNotas(value.toString());
                                            session.saveOrUpdate(extra);
                                            session.beginTransaction().commit();
                                            vector.setElementAt(value, col);
                                            this.dataVector.setElementAt(vector, row);
                                            fireTableCellUpdated(row, col);
                                        }else{
                                            session.beginTransaction().rollback();
                                            JOptionPane.showMessageDialog(null, "!Acceso denegado¡");
                                        }

                                    }catch(Exception e)
                                    {
                                        session.beginTransaction().rollback();
                                        System.out.println(e);
                                        e.printStackTrace();
                                        JOptionPane.showMessageDialog(null, "Error al autorizar el trabajo adicional");
                                    }
                                    if(session!=null)
                                        if(session.isOpen())
                                        {
                                            session.flush();
                                            session.clear();
                                            session.close();
                                        }
                                break;
                            case 3:
                                session.beginTransaction().begin();
                                    try{
                                        TrabajoExtra extra=(TrabajoExtra)session.get(TrabajoExtra.class, Integer.parseInt(t_datos.getValueAt(t_datos.getSelectedRow(), 0).toString())); 
                                        usr = (Usuario)session.get(Usuario.class, usr.getIdUsuario());

                                        if(usr.getAutorizaTrabajo()==true || usr.getGeneraAdicional()==true){
                                            extra.setImporte(Double.parseDouble(value.toString()));
                                            session.saveOrUpdate(extra);
                                            session.beginTransaction().commit();
                                            vector.setElementAt(value, col);
                                            this.dataVector.setElementAt(vector, row);
                                            fireTableCellUpdated(row, col);
                                        }else{
                                            session.beginTransaction().rollback();
                                            JOptionPane.showMessageDialog(null, "!Acceso denegado¡");
                                        }

                                    }catch(Exception e)
                                    {
                                        session.beginTransaction().rollback();
                                        System.out.println(e);
                                        e.printStackTrace();
                                        JOptionPane.showMessageDialog(null, "Error al autorizar el trabajo adicional");
                                    }
                                    if(session!=null)
                                        if(session.isOpen())
                                        {
                                            session.flush();
                                            session.clear();
                                            session.close();
                                        }
                                break;
                            case 4:
                                    session.beginTransaction().begin();
                                    try{
                                        TrabajoExtra extra=(TrabajoExtra)session.get(TrabajoExtra.class, Integer.parseInt(t_datos.getValueAt(t_datos.getSelectedRow(), 0).toString())); 
                                        usr = (Usuario)session.get(Usuario.class, usr.getIdUsuario());

                                        if(usr.getAutorizaTrabajo()==true){
                                            if((Boolean)t_datos.getValueAt(t_datos.getSelectedRow(), 4)==false){
                                                extra.setAutorizado(true);
                                                extra.setUsuario(usr);
                                                session.saveOrUpdate(extra);
                                                session.beginTransaction().commit();
                                                vector.setElementAt(value, col);
                                                this.dataVector.setElementAt(vector, row);
                                                fireTableCellUpdated(row, col);
                                            }else{
                                                Query query = session.createSQLQuery("select * from pago_adicional where id_trabajo_extra = "+Integer.parseInt(t_datos.getValueAt(t_datos.getSelectedRow(), 0).toString()));
                                                query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
                                                ArrayList consumido = (ArrayList)query.list();
                                                if(consumido.size()>0){
                                                    session.beginTransaction().rollback();
                                                    JOptionPane.showMessageDialog(null, "!La partida contiene pagos adicionales!");
                                                }else{
                                                    extra.setAutorizado(false);
                                                    extra.setUsuario(null);
                                                    session.saveOrUpdate(extra);
                                                    session.beginTransaction().commit();
                                                    vector.setElementAt(value, col);
                                                    this.dataVector.setElementAt(vector, row);
                                                    fireTableCellUpdated(row, col);
                                                }
                                            }
                                        }else{
                                            session.beginTransaction().rollback();
                                            JOptionPane.showMessageDialog(null, "!Acceso denegado¡");
                                        }

                                    }catch(Exception e)
                                    {
                                        session.beginTransaction().rollback();
                                        System.out.println(e);
                                        e.printStackTrace();
                                        JOptionPane.showMessageDialog(null, "Error al autorizar el trabajo adicional");
                                    }
                                    if(session!=null)
                                        if(session.isOpen())
                                        {
                                            session.flush();
                                            session.clear();
                                            session.close();
                                        }
                                    break;

                            default:
                                    vector.setElementAt(value, col);
                                    this.dataVector.setElementAt(vector, row);
                                    fireTableCellUpdated(row, col);
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
            return model3;
        }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        d_nuevo = new javax.swing.JDialog();
        jPanel5 = new javax.swing.JPanel();
        l_importe_pago1 = new javax.swing.JLabel();
        b_guardar_pago = new javax.swing.JButton();
        b_cancelar_pago = new javax.swing.JButton();
        t_importe_pago = new javax.swing.JFormattedTextField();
        t_notas_pago = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        t_orden1 = new javax.swing.JTextField();
        t_nombre_empleado = new javax.swing.JTextField();
        jButton3 = new javax.swing.JButton();
        t_id_empleado = new javax.swing.JTextField();
        rb_autorizado = new javax.swing.JRadioButton();
        jLabel2 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        t_datos = new javax.swing.JTable();
        t_orden = new javax.swing.JFormattedTextField();
        b_menos = new javax.swing.JButton();
        b_mas = new javax.swing.JButton();
        b_consumible = new javax.swing.JButton();
        l_orden = new javax.swing.JLabel();
        b_consumible1 = new javax.swing.JButton();

        d_nuevo.setModalityType(java.awt.Dialog.ModalityType.APPLICATION_MODAL);

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));
        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "Nuevo Importe pagado adicional"));

        l_importe_pago1.setText("Importe Pagado: $");

        b_guardar_pago.setText("Guardar");
        b_guardar_pago.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_guardar_pagoActionPerformed(evt);
            }
        });

        b_cancelar_pago.setText("Cancelar");
        b_cancelar_pago.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_cancelar_pagoActionPerformed(evt);
            }
        });

        t_importe_pago.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.00"))));
        t_importe_pago.setValue(0.0d);

        jLabel10.setText("Notas:");

        t_orden1.setEditable(false);

        t_nombre_empleado.setEditable(false);

        jButton3.setText("Empleado:");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        t_id_empleado.setEditable(false);

        rb_autorizado.setText("Autorizado");
        rb_autorizado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rb_autorizadoActionPerformed(evt);
            }
        });

        jLabel2.setText("OT");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(t_notas_pago))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                        .addComponent(rb_autorizado)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(b_cancelar_pago)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(b_guardar_pago, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(1, 1, 1))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(t_orden1, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 87, Short.MAX_VALUE)
                        .addComponent(l_importe_pago1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(t_importe_pago, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jButton3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(t_id_empleado, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(t_nombre_empleado)))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(t_orden1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(l_importe_pago1)
                    .addComponent(t_importe_pago, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(t_nombre_empleado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton3)
                    .addComponent(t_id_empleado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(t_notas_pago, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(b_cancelar_pago)
                    .addComponent(b_guardar_pago)
                    .addComponent(rb_autorizado))
                .addContainerGap())
        );

        javax.swing.GroupLayout d_nuevoLayout = new javax.swing.GroupLayout(d_nuevo.getContentPane());
        d_nuevo.getContentPane().setLayout(d_nuevoLayout);
        d_nuevoLayout.setHorizontalGroup(
            d_nuevoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        d_nuevoLayout.setVerticalGroup(
            d_nuevoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "Trabajos Adicionales", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.TOP));

        jLabel1.setText("Id Orden:");

        jButton1.setText("Buscar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        t_datos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "id", "Empleado", "Descripción", "Monto", "Aut"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Double.class, java.lang.Boolean.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, true
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

        t_orden.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0"))));

        b_menos.setIcon(new ImageIcon("imagenes/boton_menos.png"));
        b_menos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_menosActionPerformed(evt);
            }
        });

        b_mas.setIcon(new ImageIcon("imagenes/boton_mas.png"));
        b_mas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_masActionPerformed(evt);
            }
        });

        b_consumible.setIcon(new ImageIcon("imagenes/calendario.png"));
        b_consumible.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_consumibleActionPerformed(evt);
            }
        });

        b_consumible1.setIcon(new ImageIcon("imagenes/xls_icon.png"));
        b_consumible1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_consumible1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 631, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(l_orden, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(t_orden, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(b_mas, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(b_menos, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(b_consumible, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(b_consumible1, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel1)
                        .addComponent(jButton1)
                        .addComponent(t_orden, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(b_consumible, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(b_menos, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(b_mas, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(b_consumible1, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(l_orden, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 194, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        if(t_orden.getText().compareTo("")!=0)
        {
            Session session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction().begin();
            Orden orden=(Orden)session.get(Orden.class, Integer.parseInt(t_orden.getText()));
            if(orden!=null)
            {
                TrabajoExtra[] listaExtra= (TrabajoExtra[])orden.getTrabajoExtras().toArray(new TrabajoExtra[0]);
                DefaultTableModel modelo = (DefaultTableModel) t_datos.getModel();
                modelo.setNumRows(0);
                for(int x=0; x<listaExtra.length; x++)
                {
                    modelo.addRow(new Object[]{listaExtra[x].getIdAdicional(), listaExtra[x].getEmpleado().getNombre(), listaExtra[x].getNotas(),listaExtra[x].getImporte(), listaExtra[x].getAutorizado()});
                }
                l_orden.setText(t_orden.getText());
            }
            else
            {
                l_orden.setText("");
                DefaultTableModel modelo = (DefaultTableModel) t_datos.getModel();
                modelo.setNumRows(0);
                l_orden.setText("");
            }
            t_orden.setText("");
        }
        else
        {
            l_orden.setText("");
            DefaultTableModel modelo = (DefaultTableModel) t_datos.getModel();
            modelo.setNumRows(0);
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void b_menosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_menosActionPerformed
        // TODO add your handling code here:
        Session session = HibernateUtil.getSessionFactory().openSession();
        try
        {
            usr = (Usuario)session.get(Usuario.class, usr.getIdUsuario());
            if(usr.getRespaldar()==true)
            {
                if(t_datos.getSelectedRow()>-1)
                {
                    int opt=JOptionPane.showConfirmDialog(this, "¡Confirma que deseas eliminar el registro seleccionado");
                    if(opt==0)
                    {
                        DefaultTableModel modelo = (DefaultTableModel)t_datos.getModel();
                        session.beginTransaction().begin();
                        Hibernate.entidades.TrabajoExtra elimina=(Hibernate.entidades.TrabajoExtra) session.get(Hibernate.entidades.TrabajoExtra.class, Integer.parseInt(t_datos.getValueAt(t_datos.getSelectedRow(), 0).toString()));
                        if(elimina!=null)
                        {
                            if(elimina.getAlmacens().isEmpty()==true)
                            {
                                if(elimina.getPagoAdicionals().isEmpty()==true)
                                {
                                    session.delete(elimina);
                                    session.getTransaction().commit();
                                    modelo.removeRow(t_datos.getSelectedRow());
                                }
                                else
                                {
                                    session.getTransaction().rollback();
                                    JOptionPane.showMessageDialog(null, "El trabajo ya tiene Pagos y no se puede eliminar");
                                }
                            }
                            else
                            {
                                session.getTransaction().rollback();
                                JOptionPane.showMessageDialog(null, "El trabajo ya tiene material entregado y no se puede eliminar");
                            }
                        }
                        else
                        {
                            session.getTransaction().rollback();
                            JOptionPane.showMessageDialog(null, "La partida ya fue eliminada");
                        }
                    }
                }
                else
                {
                    javax.swing.JOptionPane.showMessageDialog(null, "Debes seleccionar una partida de la tabla para porde eliminarla");
                }
            }
            else
            {
                if(session.isOpen()==true)
                {
                    session.flush();
                    session.clear();
                    session.close();
                }
                JOptionPane.showMessageDialog(null, "¡Acceso denegado!");

            }
        }
        catch(Exception e)
        {
            System.out.println(e);
            JOptionPane.showMessageDialog(null, "¡No se pudo eliminar la partida seleccionada!");
        }
        if(session!=null)
        if(session.isOpen())
        {
            session.flush();
            session.clear();
            session.close();
        }
    }//GEN-LAST:event_b_menosActionPerformed

    private void b_masActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_masActionPerformed
        // TODO add your handling code here:
        t_orden1.setText(l_orden.getText());
        if(l_orden.getText().compareTo("")!=0)
        {
            Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
            d_nuevo.setLocation((d.width/2)-(486/2), (d.height/2)-(210/2));
            d_nuevo.setSize(486, 210);
            t_importe_pago.setValue(0.0d);
            t_importe_pago.setText("0.00");
            t_id_empleado.setText("");
            t_nombre_empleado.setText("");
            t_notas_pago.setText("");
            d_nuevo.setVisible(true);
        }
        else
            JOptionPane.showMessageDialog(null, "Debes seleccionar una OT");
    }//GEN-LAST:event_b_masActionPerformed

    private void b_consumibleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_consumibleActionPerformed
        // TODO add your handling code here:
        if(t_datos.getSelectedRow()>-1)
        {
            Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
            Material material = new Material(new JFrame(), true, l_orden.getText(), "A", t_datos.getValueAt(t_datos.getSelectedRow(), 0).toString(), t_datos.getValueAt(t_datos.getSelectedRow(), 1).toString());
            material.setLocation((d.width/2)-(739/2), (d.height/2)-(345/2));
            material.setSize(739, 345);
            material.setVisible(true);
        }
        else
            JOptionPane.showMessageDialog(null, "Debes seleccionar un registro de la tabla ");
    }//GEN-LAST:event_b_consumibleActionPerformed

    private void b_guardar_pagoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_guardar_pagoActionPerformed
        // TODO add your handling code here:
        if(t_orden1.getText().compareTo("")!=0)
        {
            if(t_id_empleado.getText().compareTo("")!=0)
            {
                Session session = HibernateUtil.getSessionFactory().openSession();
                try
                {
                    session.beginTransaction().begin();
                    //usr = (Usuario)session.get(Usuario.class, usr.getIdUsuario());
                    TrabajoExtra nuevo= new TrabajoExtra();

                    Calendar calendario = Calendar.getInstance();
                    calendario = Calendar.getInstance();
                    nuevo.setFechaDestajo(calendario.getTime());
                    nuevo.setImporte(((Number)t_importe_pago.getValue()).doubleValue());
                    nuevo.setNotas(t_notas_pago.getText());
                    Orden trabajo=(Orden)session.get(Orden.class, Integer.parseInt(t_orden1.getText()));
                    nuevo.setOrden(trabajo);
                    Empleado emp=(Empleado)session.get(Empleado.class, Integer.parseInt(t_id_empleado.getText()));
                    nuevo.setEmpleado(emp);
                    nuevo.setAutorizado(rb_autorizado.isSelected());
                    usr=(Usuario)session.get(Usuario.class, usr.getIdUsuario());
                    if(usr.getAutorizaTrabajo())
                        if(rb_autorizado.isSelected())
                            nuevo.setUsuario(usr);
                    Integer registro=(Integer)session.save(nuevo);
                    session.beginTransaction().commit();

                    DefaultTableModel mod=(DefaultTableModel)t_datos.getModel();
                    mod.addRow(new Object[]{registro,t_nombre_empleado.getText(),t_notas_pago.getText(),Double.parseDouble(t_importe_pago.getText()), rb_autorizado.isSelected()});

                    t_importe_pago.setValue(0.0d);
                    t_notas_pago.setText("");
                    t_orden.setText("");
                    t_id_empleado.setText("");
                    t_nombre_empleado.setText("");
                    d_nuevo.setVisible(false);

                }
                catch(Exception e)
                {
                    e.printStackTrace();
                    session.getTransaction().rollback();
                    JOptionPane.showMessageDialog(null, "¡No se pudo Agregar el destajo!");
                }
                if(session!=null)
                {
                    if(session.isOpen())
                    {
                        session.flush();
                        session.clear();
                        session.close();
                    }

                }
            }
            else
            {
                JOptionPane.showMessageDialog(null, "¡Debe ingresar un Empleado!");
            }
        }
        else
        {
            JOptionPane.showMessageDialog(null, "¡Debe ingresar un OT!");
        }

    }//GEN-LAST:event_b_guardar_pagoActionPerformed

    private void b_cancelar_pagoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_cancelar_pagoActionPerformed
        // TODO add your handling code here:
        // t_porcentaje_pago1.setValue(0.0d);
        t_orden1.setText("");
        t_importe_pago.setValue(0.0d);
        t_importe_pago.setText("0.00");
        t_id_empleado.setText("");
        t_nombre_empleado.setText("");
        t_notas_pago.setText("");
        d_nuevo.setVisible(false);
    }//GEN-LAST:event_b_cancelar_pagoActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        buscaEmpleado obj = new buscaEmpleado(new javax.swing.JFrame(), true, usr, this.sessionPrograma, false);
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        obj.setLocation((d.width/2)-(obj.getWidth()/2), (d.height/2)-(obj.getHeight()/2));
        obj.setVisible(true);
        Empleado emp_act=obj.getReturnStatus();
        if (emp_act!=null)
        {
            Session session = HibernateUtil.getSessionFactory().openSession();
            emp_act=(Empleado)session.get(Empleado.class, emp_act.getIdEmpleado());
            t_id_empleado.setText(""+emp_act.getIdEmpleado());
            t_nombre_empleado.setText(emp_act.getNombre());
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void rb_autorizadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rb_autorizadoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rb_autorizadoActionPerformed

    private void b_consumible1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_consumible1ActionPerformed
        // TODO add your handling code here:
        javax.swing.JFileChooser jF1= new javax.swing.JFileChooser(); 
        jF1.setFileFilter(new ExtensionFileFilter("Excel document (*.xls)", new String[] { "xls" }));
        String ruta = null; 
        if(jF1.showSaveDialog(null)==jF1.APPROVE_OPTION)
        { 
            ruta = jF1.getSelectedFile().getAbsolutePath(); 
            if(ruta!=null)
            {
                File archivoXLS = new File(ruta+".xls");
                try
                {
                    if(archivoXLS.exists()) 
                        archivoXLS.delete();
                    archivoXLS.createNewFile();
                    Workbook libro = new HSSFWorkbook();
                    FileOutputStream archivo = new FileOutputStream(archivoXLS);
                    Sheet hoja = libro.createSheet("Adicionales");
                    for(int ren=0;ren<(t_datos.getRowCount()+1);ren++)
                    {
                        Row fila = hoja.createRow(ren);
                        for(int col=0; col<t_datos.getColumnCount(); col++)
                        {
                            Cell celda = fila.createCell(col);
                            if(ren==0)
                            {
                                celda.setCellValue(t_datos.getColumnName(col));
                            }
                            else
                            {
                                try
                                {
                                    celda.setCellValue(t_datos.getValueAt(ren-1, col).toString());
                                }catch(Exception e)
                                {
                                    celda.setCellValue("");
                                }
                            }
                        }
                    }
                    libro.write(archivo);
                    archivo.close();
                    Desktop.getDesktop().open(archivoXLS);
                }catch(Exception e)
                {
                    System.out.println(e);
                    JOptionPane.showMessageDialog(this, "No se pudo realizar el reporte");
                }
            }
        }
    }//GEN-LAST:event_b_consumible1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton b_cancelar_pago;
    private javax.swing.JButton b_consumible;
    private javax.swing.JButton b_consumible1;
    private javax.swing.JButton b_guardar_pago;
    private javax.swing.JButton b_mas;
    private javax.swing.JButton b_menos;
    private javax.swing.JDialog d_nuevo;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel l_importe_pago1;
    private javax.swing.JLabel l_orden;
    private javax.swing.JRadioButton rb_autorizado;
    private javax.swing.JTable t_datos;
    private javax.swing.JTextField t_id_empleado;
    private javax.swing.JFormattedTextField t_importe_pago;
    private javax.swing.JTextField t_nombre_empleado;
    private javax.swing.JTextField t_notas_pago;
    public javax.swing.JFormattedTextField t_orden;
    private javax.swing.JTextField t_orden1;
    // End of variables declaration//GEN-END:variables
}
