/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * acceso.java
 *
 * Created on 19/01/2012, 02:01:25 PM
 */
package Contabilidad;

import Integral.FormatoTabla;
import Integral.Herramientas;
import Integral.Render1;
import Hibernate.Util.HibernateUtil;
import java.util.List;
import java.util.Vector;
import javax.swing.InputMap;
import javax.swing.JOptionPane;
import Hibernate.entidades.Concepto;
import Hibernate.entidades.Configuracion;
import Hibernate.entidades.Nota;
import Hibernate.entidades.Usuario;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.xml.datatype.XMLGregorianCalendar;
import mx.com.fact.schema.ws.RequestTransaction;
import mx.com.fact.schema.ws.RequestTransactionResponse;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author ISC_SALVADOR
 */
public class buscaNota extends javax.swing.JDialog {

    public static final Nota RET_CANCEL =null;
    InputMap map = new InputMap();
    DefaultTableModel model;
    String[] columnas = new String [] {"ID", "Fecha", "RFC Receptor", "Razon Social", "Folio Fiscal", "Serie", "Folio", "Monto sin IVA", "Estado"};
    String sessionPrograma="";
    Herramientas h;
    Usuario usr;
    FormatoTabla formato;
    Nota factura=new Nota();
    String idBuscar="";
    int opcion=1;
    
    /** Creates new form acceso */
    public buscaNota(java.awt.Frame parent, boolean modal, String ses, Usuario usuario, int op) {
        super(parent, modal);
        opcion=op;
        sessionPrograma=ses;
        usr=usuario;
        initComponents();
        getRootPane().setDefaultButton(b_seleccionar);
        formato =new FormatoTabla();
        t_datos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        t_datos.setModel(ModeloTablaReporte(0, columnas));
        formatoTabla();
        buscaDato();
        if(usr.getCancelarFactura()==true)
            b_cancelar.setEnabled(true);
        else
            b_cancelar.setEnabled(false);
    }
    
    DefaultTableModel ModeloTablaReporte(int renglones, String columnas[])
        {
            model = new DefaultTableModel(new Object [renglones][6], columnas)
            {
                Class[] types = new Class [] {
                    java.lang.String.class, 
                    java.lang.String.class, 
                    java.lang.String.class, 
                    java.lang.String.class,
                    java.lang.String.class,
                    java.lang.String.class, 
                    java.lang.String.class, 
                    java.lang.Double.class, 
                    java.lang.String.class
                };
                boolean[] canEdit = new boolean [] {
                    false, false, false, false, false, false, false, false, false
                };

                public void setValueAt(Object value, int row, int col)
                 {
                        Vector vector = (Vector)this.dataVector.elementAt(row);
                        Object celda = ((Vector)this.dataVector.elementAt(row)).elementAt(col);
                        switch(col)
                        {
                            case 0:
                                    vector.setElementAt(value, col);
                                    this.dataVector.setElementAt(vector, row);
                                    fireTableCellUpdated(row, col);
                                    //calcula_totales();
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
            return model;
        }

    
    private void doClose(Nota o) {
        returnStatus = o;
        //setVisible(false);
        dispose();
    }
    
    public Nota getReturnStatus() {
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
        jPanel2 = new javax.swing.JPanel();
        t_busca = new javax.swing.JTextField();
        c_filtro = new javax.swing.JComboBox();
        jLabel1 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        t_datos = new javax.swing.JTable();
        b_seleccionar = new javax.swing.JButton();
        b_cancelar = new javax.swing.JButton();
        progreso = new javax.swing.JProgressBar();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Consulta de Notas de Credito");
        setResizable(false);

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(90, 66, 126), 1, true), "Filtrar por:", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Arial", 1, 11))); // NOI18N

        t_busca.setBackground(new java.awt.Color(204, 255, 255));
        t_busca.setToolTipText("Frase a buscar");
        t_busca.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                t_buscaActionPerformed(evt);
            }
        });
        t_busca.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                t_buscaKeyReleased(evt);
            }
        });

        c_filtro.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Folio", "ID", "RFC", "Razon Social", "Folio Fiscal", "Serie", "Estado" }));
        c_filtro.setToolTipText("Filtrar por este campo");
        c_filtro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                c_filtroActionPerformed(evt);
            }
        });

        jLabel1.setText("contiene:");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(c_filtro, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(t_busca)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(t_busca, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(c_filtro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addGap(0, 9, Short.MAX_VALUE))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(90, 66, 126), 1, true), "Resultados", javax.swing.border.TitledBorder.RIGHT, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Arial", 1, 11))); // NOI18N

        t_datos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Fecha", "RFC Receptor", "Razon Social", "Folio Fiscal", "Serie", "Folio", "Monto", "Estado"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false
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
        t_datos.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                t_datosKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(t_datos);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 950, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 235, Short.MAX_VALUE)
                .addContainerGap())
        );

        b_seleccionar.setBackground(new java.awt.Color(2, 135, 242));
        b_seleccionar.setForeground(new java.awt.Color(255, 255, 255));
        b_seleccionar.setIcon(new ImageIcon("imagenes/seleccionar.png"));
        b_seleccionar.setText("Seleccionar");
        b_seleccionar.setToolTipText("Cargar nota de crétido seleccionada");
        b_seleccionar.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        b_seleccionar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_seleccionarActionPerformed(evt);
            }
        });

        b_cancelar.setBackground(new java.awt.Color(2, 135, 242));
        b_cancelar.setForeground(new java.awt.Color(255, 255, 255));
        b_cancelar.setIcon(new ImageIcon("imagenes/cancelFactura.png"));
        b_cancelar.setText("Cancelar");
        b_cancelar.setToolTipText("Cancelar nota de crédito");
        b_cancelar.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        b_cancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_cancelarActionPerformed(evt);
            }
        });

        progreso.setString("Listo");
        progreso.setStringPainted(true);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(progreso, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(b_cancelar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(b_seleccionar)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(b_seleccionar, javax.swing.GroupLayout.DEFAULT_SIZE, 36, Short.MAX_VALUE)
                        .addComponent(b_cancelar, javax.swing.GroupLayout.DEFAULT_SIZE, 36, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(progreso, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))))
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
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void b_seleccionarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_seleccionarActionPerformed
        // TODO add your handling code here:
        h=new Herramientas(usr, 0);
        h.session(sessionPrograma);
        Session session = HibernateUtil.getSessionFactory().openSession();
        try
        {
            session.beginTransaction().begin();
            usr = (Usuario)session.get(Usuario.class, usr.getIdUsuario());
            if(usr.getGenerarFactura()==true)
            {
                if(t_datos.getSelectedRow()>=0)
                {
                    try
                    {
                        session.beginTransaction().begin();
                        factura=(Nota)session.get(Nota.class, Integer.parseInt(t_datos.getValueAt(t_datos.getSelectedRow(), 0).toString()));
                        session.beginTransaction().commit();
                        if(opcion==1)
                        {
                            if(factura!=null)
                            {
                                Dimension d;
                                GeneraNota genera3=new GeneraNota(new Frame(), true, this.usr, sessionPrograma, factura);
                                d = Toolkit.getDefaultToolkit().getScreenSize();
                                genera3.setLocation((d.width/2)-(genera3.getWidth()/2), (d.height/2)-(genera3.getHeight()/2));
                                genera3.consulta();
                                genera3.setVisible(true);
                                genera3.dispose();
                                genera3=null;
                                //factura=genera3.getReturnStatus();
                            }
                        }
                    }catch(Exception e)
                    { 
                        e.printStackTrace();
                        JOptionPane.showMessageDialog(null, "error al consultar la nota");
                    }
                    doClose(factura);
                }
                else
                    JOptionPane.showMessageDialog(null, "¡No hay una nota seleccionada!");
            }
            else
                JOptionPane.showMessageDialog(null, "¡Acceso denegado!");
        }catch(Exception e)
        {
            System.out.println(e);
        }
        finally
        {
            if(session!=null)
                if(session.isOpen())
                    session.close();
        }
    }//GEN-LAST:event_b_seleccionarActionPerformed

    private void t_buscaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_buscaKeyReleased
        buscaDato();
    }//GEN-LAST:event_t_buscaKeyReleased

    private void c_filtroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_c_filtroActionPerformed
        buscaDato();
    }//GEN-LAST:event_c_filtroActionPerformed

    private void b_cancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_cancelarActionPerformed
        // TODO add your handling code here:
        if(t_datos.getSelectedRow()>-1)
        {
            if(usr.getCancelarFactura()==true)
            {
                if(t_datos.getValueAt(t_datos.getSelectedRow(), 8).toString().compareTo("Cancelado")!=0)
                {
                    int opt=JOptionPane.showConfirmDialog(this, "¡Confirma que desea cancelar la nota de credito seleccionada (Esto cancelará en SAT)");
                    if(opt==0)
                    {
                        Session session = HibernateUtil.getSessionFactory().openSession();
                        try
                        {
                            idBuscar=t_datos.getValueAt(t_datos.getSelectedRow(), 4).toString();
                            habilita(false);
                            progreso.setString("Conectando al servidor SAT Espere");
                            progreso.setIndeterminate(true);
                            if(idBuscar.compareTo("")!=0)
                            {
                                session.beginTransaction().begin();
                                Configuracion config=(Configuracion)session.get(Configuracion.class, 1);
                                //factura=(Factura)session.get(Factura.class, factura.getIdFactura());
                                factura=(Nota) session.get(Nota.class, Integer.parseInt(t_datos.getValueAt(t_datos.getSelectedRow(), 0).toString()));
                                RequestTransaction rq=new RequestTransaction();
                                rq.setRequestor(config.getRequestor());//Lo proporcionará MySuite
                                rq.setTransaction("CANCEL_XML");//Tipo de Transaccion
                                rq.setCountry("MX");//Codigo de pais
                                rq.setUser(config.getRequestor());//igual que Requestor
                                rq.setUserName(config.getUsuario_1());//Country.Entity.nombre_usuario
                                rq.setEntity(config.getRfc());
                                rq.setData1(idBuscar);//GUIID.
                                rq.setData2("");
                                rq.setData3("");
                                 if(session.isOpen())
                                    session.close();
                                llamarSoapCancela(rq);
                            }
                            else
                            {
                                try 
                                {
                                    session.beginTransaction().begin();
                                    Nota resp=(Nota) session.get(Nota.class, Integer.parseInt(t_datos.getValueAt(t_datos.getSelectedRow(), 0).toString()));
                                    if(resp!=null)
                                    {
                                        resp.setOrden(null);
                                        resp.setEstadoFactura("Cancelado");
                                        resp.setEstatus("CANCELADO");
                                        session.update(resp);
                                        session.beginTransaction().commit();
                                        t_datos.setValueAt("Cancelado", t_datos.getSelectedRow(), 8);
                                        habilita(true);
                                        progreso.setString("Listo");
                                        progreso.setIndeterminate(false);
                                        JOptionPane.showMessageDialog(null, "Nota cancelada");
                                    }
                                }catch(Exception e)
                                {
                                    session.beginTransaction().rollback();
                                    e.printStackTrace();
                                    habilita(true);
                                    progreso.setString("Listo");
                                    progreso.setIndeterminate(false);
                                    JOptionPane.showMessageDialog(null, "Error al almacenar los datos");
                                }
                            }
                        }catch(Exception e)
                        {
                            if(session!=null)
                                if(session.isConnected())
                                    session.close();
                            e.printStackTrace();
                            habilita(true);
                            progreso.setString("Listo");
                            progreso.setIndeterminate(false);
                            JOptionPane.showMessageDialog(null, "Error al consultar la base de datos");
                        }
                        finally
                        {
                            if(session!=null)
                                if(session.isOpen())
                                    session.close();
                        }
                    }
                }
            }
            else
                JOptionPane.showMessageDialog(null, "¡Acceso denegado!");
        }
        else
            JOptionPane.showMessageDialog(null, "Debes seleccionar una nota de la lista primero");
    }//GEN-LAST:event_b_cancelarActionPerformed

    private void t_datosKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_datosKeyPressed
        // TODO add your handling code here:
        int code = evt.getKeyCode(); 
        if(code == KeyEvent.VK_ENTER)
        {
            t_datos.getInputMap(t_datos.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER,0,false), "selectColumnCell");
            this.b_seleccionar.requestFocus();
        }
    }//GEN-LAST:event_t_datosKeyPressed

    private void t_buscaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_t_buscaActionPerformed
        // TODO add your handling code here:
        t_datos.requestFocus();
        t_datos.getSelectionModel().setSelectionInterval(0,0);
    }//GEN-LAST:event_t_buscaActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton b_cancelar;
    private javax.swing.JButton b_seleccionar;
    private javax.swing.JComboBox c_filtro;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JProgressBar progreso;
    public javax.swing.JTextField t_busca;
    private javax.swing.JTable t_datos;
    // End of variables declaration//GEN-END:variables

    
    private void buscaDato()
    {
        //ID, RFC, Razon Social, Folio Fiscal, Serie, Folio, Estado
        String consulta="select id_nota as id, rfc_receptor, nombre_receptor, f_fiscal, serie, folio, estado_factura, (select sum(if(descuento=0, (precio*cantidad), (precio-(precio*(descuento/100)))*cantidad )) as total from concepto where id_nota=id) as tot, fecha from nota ";
        if(c_filtro.getSelectedItem().toString().compareTo("ID")==0)
            consulta+="where id_nota like '%" + t_busca.getText() +"%'";
        if(c_filtro.getSelectedItem().toString().compareTo("RFC")==0)
            consulta+="where rfc_receptor like '%" + t_busca.getText() +"%'";
        if(c_filtro.getSelectedItem().toString().compareTo("Razon Social")==0)
            consulta+="where nombre_receptor like '%" + t_busca.getText() +"%'";
        if(c_filtro.getSelectedItem().toString().compareTo("Folio Fiscal")==0)
            consulta+="where f_fiscal like '%" + t_busca.getText() +"%'";
        if(c_filtro.getSelectedItem().toString().compareTo("Serie")==0)
            consulta+="where serie like '%" + t_busca.getText() +"%'";
        if(c_filtro.getSelectedItem().toString().compareTo("Folio")==0)
            consulta+="where folio like '%" + t_busca.getText() +"%'";
        if(c_filtro.getSelectedItem().toString().compareTo("Estado")==0)
            consulta+="where estado_factura like '%" + t_busca.getText() +"%'";
        consulta+=" order by id_nota desc";
        Session session = HibernateUtil.getSessionFactory().openSession();
        try
        {
            session.beginTransaction().begin();
            Query query = session.createSQLQuery(consulta);
            query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
            ArrayList resultList=(ArrayList)query.list();
            if(resultList.size()>0)
            {
                int i=0;
                model.getDataVector().removeAllElements();
                for (int x=0; x<resultList.size(); x++) 
                {
                    java.util.HashMap map=(java.util.HashMap)resultList.get(x);
                    Object[] renglon=new Object[]{map.get("id"),map.get("fecha").toString(),map.get("rfc_receptor"),map.get("nombre_receptor"),map.get("f_fiscal"),map.get("serie"),map.get("folio"),map.get("tot"),map.get("estado_factura")};
                    model.addRow(renglon);
                    i++;
                }
            }
            else
                model.getDataVector().removeAllElements();
            t_datos.revalidate();
            t_busca.requestFocus();
            //formatoTabla();
        }catch(Exception e)
        {
            System.out.println(e);
        }
        finally
        {
            if(session!=null)
                if(session.isOpen())
                    session.close();
        }
}
    private Nota returnStatus = RET_CANCEL;
    
     public void tabla_tamaños()
    {
        TableColumnModel col_model = t_datos.getColumnModel();
        for (int i=0; i<t_datos.getColumnCount(); i++)
        {
  	      TableColumn column = col_model.getColumn(i);
              switch(i)
              {
                  case 0://id
                      column.setPreferredWidth(50);
                      break;
                  case 1://fecha
                      column.setPreferredWidth(100);
                      break;
                  case 2://RFC
                      column.setPreferredWidth(100);
                      break;
                  case 3://nombre
                      column.setPreferredWidth(200);
                      break;
                  case 4://Folio fiscal
                      column.setPreferredWidth(150);
                      break;
                  case 5://Serie
                      column.setPreferredWidth(50);
                      break;
                  case 6://Folio
                      column.setPreferredWidth(50);
                      break;
                  case 7://Monto
                      column.setPreferredWidth(100);
                      break;
                  case 8:
                      column.setPreferredWidth(100);
                      break;
                  case 9:
                      column.setPreferredWidth(80);
                      break;
                  default:
                      column.setPreferredWidth(40);
                      break;
              }
        }
        JTableHeader header = t_datos.getTableHeader();
        header.setForeground(Color.white);
    }
     
     public void formatoTabla()
    {
        Color c1 = new java.awt.Color(2, 135, 242);   
        for(int x=0; x<t_datos.getColumnModel().getColumnCount(); x++)
            t_datos.getColumnModel().getColumn(x).setHeaderRenderer(new Render1(c1));
        tabla_tamaños();
        t_datos.setShowVerticalLines(true);
        t_datos.setShowHorizontalLines(true);
        t_datos.setDefaultRenderer(Double.class, formato); 
        t_datos.setDefaultRenderer(String.class, formato);
        t_datos.setDefaultRenderer(Boolean.class, formato);
        t_datos.setDefaultRenderer(Integer.class, formato);
    }
     
     public void llamarSoapCancela(RequestTransaction rq)
    {
        System.setProperty("javax.net.ssl.keyStore", "cacerts");
        System.setProperty("javax.net.ssl.keyStorePassword", "changeit");
        System.setProperty("javax.net.ssl.trustStore", "cacerts");
        try 
        { // Call Web Service Operation(async. callback)
            if(rq!=null)
            {
                mx.com.fact.schema.ws.FactWSFront service = new mx.com.fact.schema.ws.FactWSFront();
                mx.com.fact.schema.ws.FactWSFrontSoap port = service.getFactWSFrontSoap();
                
                // TODO initialize WS operation arguments here
                java.lang.String requestor = rq.getRequestor();
                java.lang.String transaction = rq.getTransaction();
                java.lang.String country = rq.getCountry();
                java.lang.String entity = rq.getEntity();
                java.lang.String userSAP = rq.getUser();
                java.lang.String userName = rq.getUserName();
                java.lang.String data1 = rq.getData1();
                java.lang.String data2 = rq.getData2();
                java.lang.String data3 = rq.getData3();
                javax.xml.ws.AsyncHandler<mx.com.fact.schema.ws.RequestTransactionResponse> asyncHandler = new javax.xml.ws.AsyncHandler<mx.com.fact.schema.ws.RequestTransactionResponse>() {
                    public void handleResponse(final javax.xml.ws.Response<mx.com.fact.schema.ws.RequestTransactionResponse> response) 
                    {
                        try 
                        {
                            // TODO process asynchronous response here
                            RequestTransactionResponse rtr=response.get();
                            if(rtr.getRequestTransactionResult().getResponse().isResult()==true)//la transaccion se genero
                            {
                                XMLGregorianCalendar fecha_ingreso=rtr.getRequestTransactionResult().getResponse().getTimeStamp();
                                Session session = HibernateUtil.getSessionFactory().openSession();
                                try 
                                {
                                    session.beginTransaction().begin();
                                    Nota resp=(Nota) session.createCriteria(Nota.class).add(Restrictions.eq("FFiscal", idBuscar)).uniqueResult();
                                    if(resp!=null)
                                    {
                                        resp.setOrden(null);
                                        resp.setEstadoFactura("Cancelado");
                                        resp.setEstatus("CANCELADO");
                                        session.update(resp);
                                        session.beginTransaction().commit();
                                        t_datos.setValueAt("Cancelado", t_datos.getSelectedRow(), 8);
                                    }
                                    habilita(true);
                                    progreso.setString("Listo");
                                    progreso.setIndeterminate(false);
                                    JOptionPane.showMessageDialog(null, "El UUID ya esta cancelado");
                                }catch(Exception e)
                                {
                                    session.beginTransaction().rollback();
                                    e.printStackTrace();
                                    habilita(true);
                                    progreso.setString("Listo");
                                    progreso.setIndeterminate(false);
                                    JOptionPane.showMessageDialog(null, "El UUID ya esta cancelado, pero no se pudo almacenar en la base de datos");
                                }
                                finally
                                {
                                    if(session!=null)
                                        if(session.isOpen())
                                            session.close();
                                }
                            }
                            else
                            {
                                String error=rtr.getRequestTransactionResult().getResponseData().getResponseData2();
                                String codigo=""+rtr.getRequestTransactionResult().getResponse().getCode();
                                String desccripcion=rtr.getRequestTransactionResult().getResponse().getDescription();
                                String aux="";
                                String numeros="0123456789";
                                if(error.length()==0)
                                    error=codigo;
                                for(int pal=0; pal<error.length(); pal++)
                                {
                                    if(numeros.contains(""+error.charAt(pal)))
                                        aux+=""+error.charAt(pal);
                                }
                                if(aux.length()>0)
                                {
                                    switch(aux)
                                    {
                                        case "201"://UUID Cancelado 
                                            Session session = HibernateUtil.getSessionFactory().openSession();
                                            try 
                                            {
                                                session.beginTransaction().begin();
                                                Nota resp=(Nota) session.createCriteria(Nota.class).add(Restrictions.eq("FFiscal", idBuscar)).uniqueResult();
                                                if(resp!=null)
                                                {
                                                    resp.setOrden(null);
                                                    resp.setEstadoFactura("Cancelado");
                                                    resp.setEstatus("CANCELADO");
                                                    session.update(resp);
                                                    session.beginTransaction().commit();
                                                    t_datos.setValueAt("Cancelado", t_datos.getSelectedRow(), 8);
                                                }
                                                habilita(true);
                                                progreso.setString("Listo");
                                                progreso.setIndeterminate(false);
                                                JOptionPane.showMessageDialog(null, "El UUID ya esta Cancelado");
                                            }catch(Exception e)
                                            {
                                                session.beginTransaction().rollback();
                                                e.printStackTrace();
                                                habilita(true);
                                                progreso.setString("Listo");
                                                progreso.setIndeterminate(false);
                                                JOptionPane.showMessageDialog(null, "El UUID ya esta cancelado, pero no se pudo almacenar en la base de datos");
                                            }
                                            finally
                                            {
                                                if(session!=null)
                                                    if(session.isOpen())
                                                        session.close();
                                            }
                                            break;
                                        case "202"://UUID Previamente cancelado
                                        case "3027"://UUID Previamente cancelado
                                            Session session1 = HibernateUtil.getSessionFactory().openSession();
                                            try 
                                            {
                                                session1.beginTransaction().begin();
                                                Nota resp=(Nota) session1.createCriteria(Nota.class).add(Restrictions.eq("FFiscal", idBuscar)).uniqueResult();
                                                if(resp!=null)
                                                {
                                                    resp.setOrden(null);
                                                    resp.setEstadoFactura("Cancelado");
                                                    resp.setEstatus("CANCELADO");
                                                    session1.update(resp);
                                                    session1.beginTransaction().commit();
                                                    t_datos.setValueAt("Cancelado", t_datos.getSelectedRow(), 8);
                                                }
                                                habilita(true);
                                                progreso.setString("Listo");
                                                progreso.setIndeterminate(false);
                                                JOptionPane.showMessageDialog(null, "El UUID ya esta previamente cancelado");
                                            }catch(Exception e)
                                            {
                                                session1.beginTransaction().rollback();
                                                e.printStackTrace();
                                                habilita(true);
                                                progreso.setString("Listo");
                                                progreso.setIndeterminate(false);
                                                JOptionPane.showMessageDialog(null, "El UUID ya esta cancelado, pero no se pudo almacenar en la base de datos");
                                            }
                                            finally
                                            {
                                                if(session1!=null)
                                                    if(session1.isOpen())
                                                        session1.close();
                                            }
                                            break;
                                        case "203"://UUID No corresponde al emisor
                                            habilita(true);
                                            progreso.setString("Listo");
                                            progreso.setIndeterminate(false);
                                            JOptionPane.showMessageDialog(null, "No corresponde al emisor");
                                            break;
                                        case "204"://UUID No aplicable para cancelación
                                            habilita(true);
                                            progreso.setString("Listo");
                                            progreso.setIndeterminate(false);
                                            JOptionPane.showMessageDialog(null, "La nota no corresponde al emisor");
                                            break;
                                        case "205"://UUID No existe
                                            habilita(true);
                                            progreso.setString("Listo");
                                            progreso.setIndeterminate(false);
                                            JOptionPane.showMessageDialog(null, "La noota no existe en el SAT");
                                            break;
                                        case "3000"://UUID No corresponde al emisor
                                            habilita(true);
                                            progreso.setString("Listo");
                                            progreso.setIndeterminate(false);
                                            JOptionPane.showMessageDialog(null, "Intermitencia en el Servidor Sat intente mas tarde");
                                            break;
                                        default:
                                            habilita(true);
                                            progreso.setString("Listo");
                                            progreso.setIndeterminate(false);
                                            JOptionPane.showMessageDialog(null, "Error al cancelar en SAT Error: "+error+"\n"+desccripcion);
                                            break;
                                    }
                                    try
                                    {
                                        String fecha=rtr.getRequestTransactionResult().getResponse().getTimeStamp().toXMLFormat();
                                        File f = new File("errores/"+fecha+".txt");
                                        FileWriter w = new FileWriter(f);
                                        BufferedWriter bw = new BufferedWriter(w);
                                        PrintWriter wr = new PrintWriter(bw);  
                                        wr.write(rtr.getRequestTransactionResult().getResponse().getDescription());
                                        wr.write(rtr.getRequestTransactionResult().getResponse().getHint());
                                        wr.write(rtr.getRequestTransactionResult().getResponse().getData());
                                        wr.write(rtr.getRequestTransactionResult().getResponseData().getResponseData1());
                                        wr.write(rtr.getRequestTransactionResult().getResponseData().getResponseData2());
                                        wr.write(rtr.getRequestTransactionResult().getResponseData().getResponseData3());
                                        wr.close();
                                        bw.close();
                                    } catch (Exception e)
                                    {
                                        e.printStackTrace();
                                    }
                                }
                                else
                                {
                                    habilita(true);
                                    progreso.setString("Listo");
                                    progreso.setIndeterminate(false);
                                    JOptionPane.showMessageDialog(null, "Error al cancelar en SAT");
                                }
                            }
                        } catch(Exception ex) 
                        {
                            habilita(true);
                            progreso.setString("Listo");
                            progreso.setIndeterminate(false);
                            JOptionPane.showMessageDialog(null, "Error en la conexión con el SAP:"+ex);
                        }
                    }
                };
                java.util.concurrent.Future<? extends java.lang.Object> result = port.requestTransactionAsync(requestor, transaction, country, entity, userSAP, userName, data1, data2, data3, asyncHandler);
            }
        } catch (Exception ex) 
        {
            System.out.println("Error en la conexión con el SAP:"+ex);
            habilita(true);
            progreso.setString("Listo");
            progreso.setIndeterminate(false);
            JOptionPane.showMessageDialog(null, "Error en la conexión con el SAP:"+ex);
        }
    }
     
     public void habilita(boolean op)
     {
         c_filtro.setEnabled(op);
         t_busca.setEnabled(op);
         t_datos.setEnabled(op);
         b_cancelar.setEnabled(op);
         b_seleccionar.setEnabled(op);
     }
}
