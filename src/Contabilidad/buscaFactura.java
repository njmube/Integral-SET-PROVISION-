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
import java.util.Vector;
import javax.swing.InputMap;
import javax.swing.JOptionPane;
import Hibernate.entidades.Configuracion;
import Hibernate.entidades.Factura;
import Hibernate.entidades.Orden;
import Hibernate.entidades.Usuario;
import Integral.HorizontalBarUI;
import Integral.VerticalBarUI;
import api.ApiTurbo;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JScrollBar;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import timbrar.ApiFinkok;
import timbrar.ApiMysuite;

/**
 *
 * @author ISC_SALVADOR
 */
public class buscaFactura extends javax.swing.JDialog {

    public static final Factura RET_CANCEL =null;
    InputMap map = new InputMap();
    DefaultTableModel model;
    String[] columnas = new String [] {"ID", "Fecha", "RFC Receptor", "Razon Social", "Folio Fiscal", "Serie", "Folio", "Monto sin IVA", "Estado", "Suc"};
    String sessionPrograma="";
    Herramientas h;
    Usuario usr;
    FormatoTabla formato;
    Factura factura=new Factura();
    String idBuscar="", ruta="";
    int opcion=1;
    boolean bandera=true;
    int inicio=0;
    boolean parar=false;
    final JScrollBar scrollBar;
    int configuracion=1;
    
    /** Creates new form acceso */
    public buscaFactura(java.awt.Frame parent, boolean modal, String ses, Usuario usuario, int op, int configuracion) {
        super(parent, modal);
        this.configuracion=configuracion;
        opcion=op;
        sessionPrograma=ses;
        usr=usuario;
        initComponents();
        try{
            FileReader fil = new FileReader("config.txt");
            BufferedReader b = new BufferedReader(fil);
            if((ruta = b.readLine())==null)
                ruta="";
            b.close();
            fil.close();
            fil=null;
            b=null;
        }catch(Exception e){}
        scrollBar = scroll.getVerticalScrollBar();
        scrollBar.addAdjustmentListener(new AdjustmentListener() {
            public void adjustmentValueChanged(AdjustmentEvent ae) {
                if(parar==false)
                {
                    int extent = scrollBar.getModel().getExtent();
                    extent+=scrollBar.getValue();
                    if(extent==scrollBar.getMaximum())
                        buscaDato();
                }
              //System.out.println("actual: " + extent+" maximo:"+scrollBar.getMaximum());
            }
          });
        getRootPane().setDefaultButton(b_seleccionar);
        formato =new FormatoTabla();
        t_datos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        t_datos.setModel(ModeloTablaReporte(0, columnas));
        formatoTabla();
        scroll.getVerticalScrollBar().setUI(new VerticalBarUI());
        scroll.getHorizontalScrollBar().setUI(new HorizontalBarUI());
        buscaDato();
        if(usr.getCancelarFactura()==true)
            b_cancelar.setEnabled(true);
        else
            b_cancelar.setEnabled(false);
    }
    
    DefaultTableModel ModeloTablaReporte(int renglones, String columnas[])
        {
            model = new DefaultTableModel(new Object [renglones][10], columnas)
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
                    java.lang.String.class,
                    java.lang.String.class
                };
                boolean[] canEdit = new boolean [] {
                    false, false, false, false, false, false, false, false, false, false
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

    
    private void doClose(Factura o) {
        returnStatus = o;
        //setVisible(false);
        this.dispose();
    }
    
    public Factura getReturnStatus() {
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

        addendas = new javax.swing.JDialog();
        jLabel24 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        t_busca = new javax.swing.JTextField();
        c_filtro = new javax.swing.JComboBox();
        jLabel1 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        scroll = new javax.swing.JScrollPane();
        t_datos = new javax.swing.JTable();
        b_seleccionar = new javax.swing.JButton();
        b_cancelar = new javax.swing.JButton();
        progreso = new javax.swing.JProgressBar();

        addendas.setTitle("Facturacion Electrónica");
        addendas.setModalityType(java.awt.Dialog.ModalityType.APPLICATION_MODAL);

        jLabel24.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel24.setText("Selecciona el tipo de Addenda a utilizar para facturar");

        jButton2.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        jButton2.setText("Qualitas");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton5.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        jButton5.setText("GNP");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jButton6.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        jButton6.setText("General");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jButton7.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        jButton7.setText("Alsea");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout addendasLayout = new javax.swing.GroupLayout(addendas.getContentPane());
        addendas.getContentPane().setLayout(addendasLayout);
        addendasLayout.setHorizontalGroup(
            addendasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(addendasLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(addendasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel24)
                    .addGroup(addendasLayout.createSequentialGroup()
                        .addComponent(jButton2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(addendasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(addendasLayout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(addendasLayout.createSequentialGroup()
                                .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        addendasLayout.setVerticalGroup(
            addendasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(addendasLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel24)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(addendasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton2)
                    .addComponent(jButton6)
                    .addComponent(jButton5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton7)
                .addContainerGap())
        );

        jButton3.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        jButton3.setText("Axa Talleres");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Consulta de Facturas");
        setModalityType(java.awt.Dialog.ModalityType.APPLICATION_MODAL);
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
                "ID", "Fecha", "RFC Receptor", "Razon Social", "Folio Fiscal", "Serie", "Folio", "Monto", "Estado", "Suc"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false
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
        scroll.setViewportView(t_datos);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(scroll, javax.swing.GroupLayout.DEFAULT_SIZE, 950, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(scroll, javax.swing.GroupLayout.DEFAULT_SIZE, 235, Short.MAX_VALUE)
                .addContainerGap())
        );

        b_seleccionar.setBackground(new java.awt.Color(2, 135, 242));
        b_seleccionar.setForeground(new java.awt.Color(255, 255, 255));
        b_seleccionar.setIcon(new ImageIcon("imagenes/seleccionar.png"));
        b_seleccionar.setText("Seleccionar");
        b_seleccionar.setToolTipText("Cargar factura seleccionada");
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
        b_cancelar.setToolTipText("Cancelar factura");
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
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(progreso, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(b_seleccionar, javax.swing.GroupLayout.DEFAULT_SIZE, 36, Short.MAX_VALUE)
                        .addComponent(b_cancelar, javax.swing.GroupLayout.DEFAULT_SIZE, 36, Short.MAX_VALUE))))
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
                    session.beginTransaction().begin();
                    factura=(Factura)session.get(Factura.class, Integer.parseInt(t_datos.getValueAt(t_datos.getSelectedRow(), 0).toString()));
                    session.beginTransaction().commit();
                    Dimension d;
                    if(opcion==1)
                    {
                        if(factura.getAddenda()!=null && factura.getEstadoFactura().compareTo("Facturado")==0)
                        {
                            switch(factura.getAddenda())
                            {
                                case "qualitas":
                                    QUALITAS genera=new QUALITAS(new Frame(), true, this.usr, sessionPrograma, factura,configuracion);
                                    d = Toolkit.getDefaultToolkit().getScreenSize();
                                    genera.setLocation((d.width/2)-(genera.getWidth()/2), (d.height/2)-(genera.getHeight()/2));
                                    genera.consulta();
                                    genera.setVisible(true);
                                    //factura=genera.getReturnStatus();
                                    genera.dispose();
                                    genera=null;
                                    break;
                                case "axa":
                                    AXA genera1=new AXA(new Frame(), true, this.usr, sessionPrograma, factura);
                                    d = Toolkit.getDefaultToolkit().getScreenSize();
                                    genera1.setLocation((d.width/2)-(genera1.getWidth()/2), (d.height/2)-(genera1.getHeight()/2));
                                    genera1.consulta();
                                    genera1.setVisible(true);
                                    //factura=genera1.getReturnStatus();
                                    genera1.dispose();
                                    genera1=null;
                                    break;
                                case "gnp":
                                    GNP genera2=new GNP(new Frame(), true, this.usr, sessionPrograma, factura,configuracion);
                                    d = Toolkit.getDefaultToolkit().getScreenSize();
                                    genera2.setLocation((d.width/2)-(genera2.getWidth()/2), (d.height/2)-(genera2.getHeight()/2));
                                    genera2.consulta();
                                    genera2.setVisible(true);
                                    //factura=genera2.getReturnStatus();
                                    genera2.dispose();
                                    genera1=null;
                                    break;
                                //case "mapfre":
    //                                            break;
                                case"general":
                                    General genera3=new General(new Frame(), true, this.usr, sessionPrograma, factura,configuracion);
                                    d = Toolkit.getDefaultToolkit().getScreenSize();
                                    genera3.setLocation((d.width/2)-(genera3.getWidth()/2), (d.height/2)-(genera3.getHeight()/2));
                                    genera3.consulta();
                                    genera3.setVisible(true);
                                    //factura=genera3.getReturnStatus();
                                    genera3.dispose();
                                    genera3=null;
                                    break;
                                default:
                                    addendas.setSize(323, 125);
                                    d = Toolkit.getDefaultToolkit().getScreenSize();
                                    addendas.setLocation((d.width/2)-(addendas.getWidth()/2), (d.height/2)-(addendas.getHeight()/2));
                                    addendas.setVisible(true);
                                    break;
                            }
                        }
                        else
                        {
                            addendas.setSize(323, 125);
                            d = Toolkit.getDefaultToolkit().getScreenSize();
                            addendas.setLocation((d.width/2)-(addendas.getWidth()/2), (d.height/2)-(addendas.getHeight()/2));
                            addendas.setVisible(true);
                        }
                        if(session!=null)
                            if(session.isOpen())
                                session.close();
                    }
                    doClose(factura);
                }
                else
                    JOptionPane.showMessageDialog(null, "¡No hay una factura seleccionada!");
            }
            else
                JOptionPane.showMessageDialog(null, "¡Acceso denegado!");
        }catch(Exception e)
        { 
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al consultar la factura");
        }
        finally
        {
            if(session!=null)
                if(session.isOpen())
                    session.close();
        }
    }//GEN-LAST:event_b_seleccionarActionPerformed

    private void t_buscaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_buscaKeyReleased
        inicio=0;
        parar=false;
        this.buscaDato();
    }//GEN-LAST:event_t_buscaKeyReleased

    private void c_filtroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_c_filtroActionPerformed
        inicio=0;
        parar=false;
        this.buscaDato();
    }//GEN-LAST:event_c_filtroActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        Session session = HibernateUtil.getSessionFactory().openSession();
        try
        {
            session.beginTransaction().begin();
            factura=(Factura)session.get(Factura.class, factura.getIdFactura());
            session.beginTransaction().commit();
            addendas.setVisible(false);
            QUALITAS genera=new QUALITAS(new javax.swing.JFrame(), true, usr, sessionPrograma, factura,configuracion);
            Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
            genera.setLocation((d.width/2)-(genera.getWidth()/2), (d.height/2)-(genera.getHeight()/2));
            genera.consulta();
            genera.setVisible(true);
        }catch(Exception e)
        {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "error al consultar la factura");
        }
        finally
        {
            if(session!=null)
                if(session.isOpen())
                    session.close();
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        Session session = HibernateUtil.getSessionFactory().openSession();
        try
        {
            session.beginTransaction().begin();
            factura=(Factura)session.get(Factura.class, factura.getIdFactura());
            session.beginTransaction().commit();
            this.doClose(factura);
            addendas.setVisible(false);
            AXA genera=new AXA(new javax.swing.JFrame(), true, usr, sessionPrograma, factura);
            Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
            genera.setLocation((d.width/2)-(genera.getWidth()/2), (d.height/2)-(genera.getHeight()/2));
            genera.consulta();
            genera.setVisible(true);
        }catch(Exception e)
        {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "error al consultar la factura");
        }
        finally
        {
            if(session!=null)
                if(session.isOpen())
                    session.close();
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:
        Session session = HibernateUtil.getSessionFactory().openSession();
        try
        {
            factura=(Factura)session.get(Factura.class, factura.getIdFactura());
            session.beginTransaction().commit();
            addendas.setVisible(false);
            GNP genera=new GNP(new javax.swing.JFrame(), true, usr, sessionPrograma, factura,configuracion);
            Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
            genera.setLocation((d.width/2)-(genera.getWidth()/2), (d.height/2)-(genera.getHeight()/2));
            genera.consulta();
            genera.setVisible(true);
        }catch(Exception e)
        {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "error al consultar la factura");
        }
        finally
        {
            if(session!=null)
                if(session.isOpen())
                    session.close();
        }
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        // TODO add your handling code here:
        Session session = HibernateUtil.getSessionFactory().openSession();
        try
        {
            session.beginTransaction().begin();
            factura=(Factura)session.get(Factura.class, factura.getIdFactura());
            session.beginTransaction().commit();
            addendas.setVisible(false);
            General genera=new General(new javax.swing.JFrame(), true, usr, sessionPrograma, factura,configuracion);
            Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
            genera.setLocation((d.width/2)-(genera.getWidth()/2), (d.height/2)-(genera.getHeight()/2));
            genera.consulta();
            genera.setVisible(true);
        }catch(Exception e)
        {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "error al consultar la factura");
        }
        finally
        {
            if(session!=null)
                if(session.isOpen())
                    session.close();
        }
    }//GEN-LAST:event_jButton6ActionPerformed

    private void b_cancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_cancelarActionPerformed
        // TODO add your handling code here:
        if(t_datos.getSelectedRow()>-1)
        {
            if(usr.getCancelarFactura()==true)
            {
                if(t_datos.getValueAt(t_datos.getSelectedRow(), 8).toString().compareTo("Cancelado")!=0)
                {
                    int opt=JOptionPane.showConfirmDialog(this, "¡Confirma que desea cancelar la factura seleccionada (Esto cancelará en SAT)");
                    if(opt==0)
                    {
                        Session session = HibernateUtil.getSessionFactory().openSession();
                        try
                        {
                            if(bandera==true)
                            {
                                bandera=false;
                                if(t_datos.getValueAt(t_datos.getSelectedRow(), 4)!=null)
                                    idBuscar=t_datos.getValueAt(t_datos.getSelectedRow(), 4).toString();
                                else
                                    idBuscar="";
                                habilita(false);
                                progreso.setString("Conectando al servidor SAT Espere");
                                progreso.setIndeterminate(true);

                                if(idBuscar.compareTo("")!=0)
                                {
                                    session.beginTransaction().begin();
                                    factura=(Factura) session.get(Factura.class, Integer.parseInt(t_datos.getValueAt(t_datos.getSelectedRow(), 0).toString()));
                                    switch(factura.getPac())
                                    {
                                        case "M":
                                            Configuracion config=(Configuracion)session.createCriteria(Configuracion.class).add(Restrictions.eq("empresa", factura.getNombreEmisor())).setMaxResults(1).uniqueResult();
                                            ArrayList rq=new ArrayList();
                                            rq.add(config.getRequestor());//Lo proporcionará MySuite
                                            rq.add("CANCEL_XML");//Tipo de Transaccion
                                            rq.add("MX");//Codigo de pais
                                            rq.add(config.getRequestor());//igual que Requestor
                                            rq.add("MX."+config.getRfc()+"."+config.getUsuario_1());//Country.Entity.nombre_usuario
                                            rq.add(config.getRfc());
                                            rq.add(idBuscar);//GUIID.
                                            rq.add("");
                                            rq.add("");
                                            ApiMysuite timbrar=new ApiMysuite(ruta);
                                            ArrayList respuesta=timbrar.llamarSoapCancela(rq);
                                            for(int x=0; x<respuesta.size(); x++)
                                            {
                                                System.out.println(respuesta.get(x).toString());
                                            }
                                            if(respuesta.size()>0)
                                            {
                                                switch(respuesta.get(0).toString())
                                                {
                                                    case "1"://Se Canceló
                                                        factura=(Factura) session.createCriteria(Factura.class).add(Restrictions.eq("FFiscal", idBuscar)).uniqueResult();
                                                        if(factura!=null)
                                                        {
                                                            Orden aux=factura.getOrden();
                                                            if(aux!=null)
                                                            {
                                                                aux.setNoFactura(null);
                                                                session.update(aux);
                                                            }
                                                            factura.setOrden(null);
                                                            factura.setEstadoFactura("Cancelado");
                                                            factura.setEstatus("CANCELADO");
                                                            session.update(factura);
                                                            session.beginTransaction().commit();
                                                            t_datos.setValueAt("Cancelado", t_datos.getSelectedRow(), 8);
                                                            habilita(true);
                                                            progreso.setString("Listo");
                                                            progreso.setIndeterminate(false);
                                                            bandera=true;
                                                            JOptionPane.showMessageDialog(null, "El UUID ya esta cancelado");
                                                        }
                                                        else
                                                        {
                                                            habilita(true);
                                                            progreso.setString("Listo");
                                                            progreso.setIndeterminate(false);
                                                            bandera=true;
                                                            JOptionPane.showMessageDialog(null, "El UUID ya esta cancelado pero no pudo actualizarse la base de datos");
                                                        }
                                                        break;

                                                    case "-1":
                                                        habilita(true);
                                                        progreso.setString("Listo");
                                                        progreso.setIndeterminate(false);
                                                        bandera=true;
                                                        JOptionPane.showMessageDialog(null, respuesta.get(1).toString());
                                                        break;
                                                }
                                            }
                                            else
                                            {
                                                habilita(true);
                                                progreso.setString("Listo");
                                                progreso.setIndeterminate(false);
                                                bandera=true;
                                                JOptionPane.showMessageDialog(null, "Error al consultar la base de datos");
                                            }
                                        break;

                                        case "F":
                                            factura=(Factura)session.get(Factura.class, factura.getIdFactura());
                                            Configuracion config1=(Configuracion)session.createCriteria(Configuracion.class).add(Restrictions.eq("empresa", factura.getNombreEmisor())).setMaxResults(1).uniqueResult();
                                            ApiFinkok api1=new ApiFinkok(ruta);
                                            factura=(Factura) session.createCriteria(Factura.class).add(Restrictions.eq("FFiscal", idBuscar)).uniqueResult();
                                            //"clave", "key", "cer", "rita destino", "claveFinkok", "uuid a cancelar", "usuario finkok"
                                            ArrayList datos=new ArrayList();
                                            datos.add(config1.getClave());//0
                                            datos.add(ruta+"config/"+config1.getLlave());//1
                                            datos.add(ruta+"config/"+config1.getCer());//2
                                            datos.add("pom/");//3
                                            datos.add(config1.getClaveFinkok());//4
                                            datos.add(idBuscar);//5
                                            datos.add(config1.getEmailFinkok());//6
                                            datos.add(factura.getRfcEmisor());//7
                                            ArrayList guarda=api1.llamarSoapElimina(datos);
                                            switch(guarda.get(0).toString())
                                            {
                                                case "0":
                                                    habilita(true);
                                                    progreso.setString("Listo");
                                                    progreso.setIndeterminate(false);
                                                    bandera=true;
                                                    JOptionPane.showMessageDialog(null, "Error: "+guarda.get(1).toString());
                                                    break;

                                                case "1"://se cancelo correcto
                                                    ArrayList resp=(ArrayList)guarda.get(1);
                                                    ArrayList arr=(ArrayList)guarda.get(1);
                                                    ArrayList aq=(ArrayList)arr.get(0);
                                                    switch(aq.get(1).toString())
                                                    {
                                                        case "201":
                                                        factura=(Factura) session.createCriteria(Factura.class).add(Restrictions.eq("FFiscal", idBuscar)).uniqueResult();
                                                        if(factura!=null)
                                                        {
                                                            Orden aux=factura.getOrden();
                                                            if(aux!=null)
                                                            {
                                                                aux.setNoFactura(null);
                                                                session.update(aux);
                                                            }
                                                            factura.setOrden(null);
                                                            factura.setEstadoFactura("Cancelado");
                                                            factura.setEstatus("CANCELADO");
                                                            session.update(factura);
                                                            session.beginTransaction().commit();
                                                            t_datos.setValueAt("Cancelado", t_datos.getSelectedRow(), 8);
                                                            habilita(true);
                                                            progreso.setString("Listo");
                                                            progreso.setIndeterminate(false);
                                                            bandera=true;
                                                            JOptionPane.showMessageDialog(null, "El UUID ya esta cancelado");
                                                        }
                                                        else
                                                        {
                                                            habilita(true);
                                                            progreso.setString("Listo");
                                                            progreso.setIndeterminate(false);
                                                            bandera=true;
                                                            JOptionPane.showMessageDialog(null, "El UUID ya esta cancelado pero no pudo actualizarse la base de datos");
                                                        }
                                                        break;

                                                        case "202":
                                                            factura=(Factura) session.createCriteria(Factura.class).add(Restrictions.eq("FFiscal", idBuscar)).uniqueResult();
                                                            if(factura!=null)
                                                            {
                                                                factura.setOrden(null);
                                                                factura.setEstadoFactura("Cancelado");
                                                                factura.setEstatus("CANCELADO");
                                                                session.update(factura);
                                                                session.beginTransaction().commit();
                                                                t_datos.setValueAt("Cancelado", t_datos.getSelectedRow(), 8);
                                                                habilita(true);
                                                                progreso.setString("Listo");
                                                                progreso.setIndeterminate(false);
                                                                bandera=true;
                                                                JOptionPane.showMessageDialog(null, "El UUID ya esta cancelado");
                                                            }
                                                            else
                                                            {
                                                                habilita(true);
                                                                progreso.setString("Listo");
                                                                progreso.setIndeterminate(false);
                                                                bandera=true;
                                                                JOptionPane.showMessageDialog(null, "El UUID ya esta cancelado pero no pudo actualizarse la base de datos");
                                                            }
                                                            break;
                                                        case "300":
                                                            habilita(true);
                                                            progreso.setString("Listo");
                                                            progreso.setIndeterminate(false);
                                                            bandera=true;
                                                            JOptionPane.showMessageDialog(null, "Usuario y contraseña inválidos ");
                                                            break;
                                                        case "203":
                                                            habilita(true);
                                                            progreso.setString("Listo");
                                                            progreso.setIndeterminate(false);
                                                            bandera=true;
                                                            JOptionPane.showMessageDialog(null, "No corresponde el RFC del Emisor y de quien solicita la cancelación");
                                                            break;
                                                        case "205":
                                                            habilita(true);
                                                            progreso.setString("Listo");
                                                            progreso.setIndeterminate(false);
                                                            bandera=true;
                                                            JOptionPane.showMessageDialog(null, "UUID No existe");
                                                            break;
                                                        case "704":
                                                            habilita(true);
                                                            progreso.setString("Listo");
                                                            progreso.setIndeterminate(false);
                                                            bandera=true;
                                                            JOptionPane.showMessageDialog(null, "Error con la contraseña de la llave Privada");
                                                        case "708":
                                                            habilita(true);
                                                            progreso.setString("Listo");
                                                            progreso.setIndeterminate(false);
                                                            bandera=true;
                                                            JOptionPane.showMessageDialog(null, "No se pudo conectar al SAT");
                                                        case "711":
                                                            habilita(true);
                                                            progreso.setString("Listo");
                                                            progreso.setIndeterminate(false);
                                                            bandera=true;
                                                            JOptionPane.showMessageDialog(null, "Error con el certificado al cancelar");
                                                        case "712":
                                                            habilita(true);
                                                            progreso.setString("Listo");
                                                            progreso.setIndeterminate(false);
                                                            bandera=true;
                                                            JOptionPane.showMessageDialog(null, "El número de 'noCertificado' es diferente al del número de certificado del atributo 'certificado'");
                                                        default:
                                                            habilita(true);
                                                            progreso.setString("Listo");
                                                            progreso.setIndeterminate(false);
                                                            bandera=true;
                                                            JOptionPane.showMessageDialog(null, "Error.- "+aq.get(1).toString());
                                                            break;
                                                    }
                                                    break;
                                            }
                                            break;
                                        case "T":
                                            factura=(Factura)session.get(Factura.class, factura.getIdFactura());
                                            Configuracion config2=(Configuracion)session.createCriteria(Configuracion.class).add(Restrictions.eq("empresa", factura.getNombreEmisor())).setMaxResults(1).uniqueResult();
                                            ApiTurbo api2=new ApiTurbo(ruta);
                                            factura=(Factura) session.createCriteria(Factura.class).add(Restrictions.eq("FFiscal", idBuscar)).uniqueResult();
                                            //"clave", "key", "cer", "rita destino", "claveFinkok", "uuid a cancelar", "usuario finkok"
                                            ArrayList datos2=new ArrayList();
                                            datos2.add(config2.getClave());
                                            datos2.add(ruta+"config/"+config2.getLlave());
                                            datos2.add(ruta+"config/"+config2.getCer());
                                            datos2.add("pom/");
                                            datos2.add(config2.getClaveFinkok());
                                            datos2.add(idBuscar);
                                            datos2.add(config2.getEmailFinkok());
                                            datos2.add(factura.getRfcEmisor());
                                                                                        
                                            ArrayList guarda2=api2.llamarSoapElimina(datos2);
                                            switch(guarda2.get(0).toString())
                                            {
                                                case "0":
                                                    habilita(true);
                                                    progreso.setString("Listo");
                                                    progreso.setIndeterminate(false);
                                                    bandera=true;
                                                    JOptionPane.showMessageDialog(null, "Error: "+guarda2.get(1).toString());
                                                    break;

                                                case "1"://se cancelo correcto
                                                    ArrayList lista_estatus=(ArrayList)guarda2.get(2);
                                                    ArrayList estatus_recibidos=(ArrayList)lista_estatus.get(0);
                                                    switch(estatus_recibidos.get(1).toString())
                                                    {
                                                        case "201":
                                                            factura=(Factura) session.createCriteria(Factura.class).add(Restrictions.eq("FFiscal", idBuscar)).uniqueResult();
                                                            if(factura!=null)
                                                            {
                                                                Orden aux=factura.getOrden();
                                                                if(aux!=null)
                                                                {
                                                                    aux.setNoFactura(null);
                                                                    session.update(aux);
                                                                }
                                                                factura.setOrden(null);
                                                                factura.setEstadoFactura("Cancelado");
                                                                factura.setEstatus("CANCELADO");
                                                                session.update(factura);
                                                                session.beginTransaction().commit();
                                                                t_datos.setValueAt("Cancelado", t_datos.getSelectedRow(), 8);
                                                                habilita(true);
                                                                progreso.setString("Listo");
                                                                progreso.setIndeterminate(false);
                                                                bandera=true;
                                                                JOptionPane.showMessageDialog(null, "El UUID ya esta cancelado");
                                                            }
                                                            else
                                                            {
                                                                habilita(true);
                                                                progreso.setString("Listo");
                                                                progreso.setIndeterminate(false);
                                                                bandera=true;
                                                                JOptionPane.showMessageDialog(null, "El UUID ya esta cancelado pero no pudo actualizarse la base de datos");
                                                            }
                                                            break;

                                                        case "202":
                                                            factura=(Factura) session.createCriteria(Factura.class).add(Restrictions.eq("FFiscal", idBuscar)).uniqueResult();
                                                            if(factura!=null)
                                                            {
                                                                factura.setOrden(null);
                                                                factura.setEstadoFactura("Cancelado");
                                                                factura.setEstatus("CANCELADO");
                                                                session.update(factura);
                                                                session.beginTransaction().commit();
                                                                t_datos.setValueAt("Cancelado", t_datos.getSelectedRow(), 8);
                                                                habilita(true);
                                                                progreso.setString("Listo");
                                                                progreso.setIndeterminate(false);
                                                                bandera=true;
                                                                JOptionPane.showMessageDialog(null, "El UUID ya esta cancelado");
                                                            }
                                                            else
                                                            {
                                                                habilita(true);
                                                                progreso.setString("Listo");
                                                                progreso.setIndeterminate(false);
                                                                bandera=true;
                                                                JOptionPane.showMessageDialog(null, "El UUID ya esta cancelado pero no pudo actualizarse la base de datos");
                                                            }
                                                            break;
                                                        case "203":
                                                            habilita(true);
                                                            progreso.setString("Listo");
                                                            progreso.setIndeterminate(false);
                                                            bandera=true;
                                                            JOptionPane.showMessageDialog(null, "UUID no corresponde al emisor");
                                                            break;
                                                        case "204":
                                                            habilita(true);
                                                            progreso.setString("Listo");
                                                            progreso.setIndeterminate(false);
                                                            bandera=true;
                                                            JOptionPane.showMessageDialog(null, "El CFDI no aplica para cancelación");
                                                            break;
                                                        case "205":
                                                            habilita(true);
                                                            progreso.setString("Listo");
                                                            progreso.setIndeterminate(false);
                                                            bandera=true;
                                                            JOptionPane.showMessageDialog(null, "El UUID no existe o no ha sido procesado por el SAT.");
                                                            break;
                                                        case "704":
                                                            habilita(true);
                                                            progreso.setString("Listo");
                                                            progreso.setIndeterminate(false);
                                                            bandera=true;
                                                            JOptionPane.showMessageDialog(null, "Error con la contraseña de la llave Privada");
                                                        case "708":
                                                            habilita(true);
                                                            progreso.setString("Listo");
                                                            progreso.setIndeterminate(false);
                                                            bandera=true;
                                                            JOptionPane.showMessageDialog(null, "No se pudo conectar al SAT");
                                                        case "711":
                                                            habilita(true);
                                                            progreso.setString("Listo");
                                                            progreso.setIndeterminate(false);
                                                            bandera=true;
                                                            JOptionPane.showMessageDialog(null, "Error con el certificado al cancelar");
                                                        case "712":
                                                            habilita(true);
                                                            progreso.setString("Listo");
                                                            progreso.setIndeterminate(false);
                                                            bandera=true;
                                                            JOptionPane.showMessageDialog(null, "El número de 'noCertificado' es diferente al del número de certificado del atributo 'certificado'");
                                                        default:
                                                            habilita(true);
                                                            progreso.setString("Listo");
                                                            progreso.setIndeterminate(false);
                                                            bandera=true;
                                                            JOptionPane.showMessageDialog(null, "Error.- "+estatus_recibidos.get(1).toString()+guarda2.get(1));
                                                            break;
                                                    }
                                                    break;
                                            }
                                            break;
                                    }
                                }
                                else
                                {
                                    try 
                                    {
                                        session.beginTransaction().begin();
                                        Factura resp=(Factura) session.get(Factura.class, Integer.parseInt(t_datos.getValueAt(t_datos.getSelectedRow(), 0).toString()));
                                        if(resp!=null)
                                        {
                                            Orden aux=resp.getOrden();
                                            if(aux!=null)
                                            {
                                                aux.setNoFactura(null);
                                                session.update(aux);
                                            }
                                            resp.setOrden(null);
                                            resp.setEstadoFactura("Cancelado");
                                            resp.setEstatus("CANCELADO");
                                            session.update(resp);
                                            session.beginTransaction().commit();
                                            t_datos.setValueAt("Cancelado", t_datos.getSelectedRow(), 8);
                                            habilita(true);
                                            progreso.setString("Listo");
                                            progreso.setIndeterminate(false);
                                            bandera=true;
                                            JOptionPane.showMessageDialog(null, "Factura cancelada");
                                        }
                                    }catch(Exception e)
                                    {
                                        session.beginTransaction().rollback();
                                        e.printStackTrace();
                                        habilita(true);
                                        progreso.setString("Listo");
                                        progreso.setIndeterminate(false);
                                        bandera=true;
                                        JOptionPane.showMessageDialog(null, "Error al almacenar los datos");
                                    }
                                }
                            }
                        }catch(Exception e)
                        {
                            e.printStackTrace();
                            bandera=true;
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
            JOptionPane.showMessageDialog(null, "Debes seleccionar una factura de la lista primero");
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

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        // TODO add your handling code here:
        Session session = HibernateUtil.getSessionFactory().openSession();
        try
        {
            session.beginTransaction().begin();
            factura=(Factura)session.get(Factura.class, factura.getIdFactura());
            session.beginTransaction().commit();
            addendas.setVisible(false);
            Alsea genera=new Alsea(new javax.swing.JFrame(), true, usr, sessionPrograma, factura,configuracion);
            Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
            genera.setLocation((d.width/2)-(genera.getWidth()/2), (d.height/2)-(genera.getHeight()/2));
            genera.consulta();
            genera.setVisible(true);
        }catch(Exception e)
        {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "error al consultar la factura");
        }
        finally
        {
            if(session!=null)
                if(session.isOpen())
                    session.close();
        }
    }//GEN-LAST:event_jButton7ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JDialog addendas;
    private javax.swing.JButton b_cancelar;
    private javax.swing.JButton b_seleccionar;
    private javax.swing.JComboBox c_filtro;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JProgressBar progreso;
    private javax.swing.JScrollPane scroll;
    public javax.swing.JTextField t_busca;
    private javax.swing.JTable t_datos;
    // End of variables declaration//GEN-END:variables

    
    private void buscaDato()
    {
        //ID, RFC, Razon Social, Folio Fiscal, Serie, Folio, Estado
        String consulta="select id_factura as id, rfc_receptor, nombre_receptor, f_fiscal, serie, folio, estado_factura, (select sum(if(descuento=0, (precio*cantidad), (precio-(precio*(descuento/100)))*cantidad )) as total from concepto where id_factura=id) as tot, fecha, folio_externo, serie_externo, id_tultitlan from factura ";
        if(c_filtro.getSelectedItem().toString().compareTo("ID")==0)
            consulta+="where id_factura like '%" + t_busca.getText() +"%'";
        if(c_filtro.getSelectedItem().toString().compareTo("RFC")==0)
            consulta+="where rfc_receptor like '%" + t_busca.getText() +"%'";
        if(c_filtro.getSelectedItem().toString().compareTo("Razon Social")==0)
            consulta+="where nombre_receptor like '%" + t_busca.getText() +"%'";
        if(c_filtro.getSelectedItem().toString().compareTo("Folio Fiscal")==0)
            consulta+="where f_fiscal like '%" + t_busca.getText() +"%'";
        if(c_filtro.getSelectedItem().toString().compareTo("Serie")==0)
            consulta+="where serie like '%" + t_busca.getText() +"%'";
        if(c_filtro.getSelectedItem().toString().compareTo("Folio")==0)
        {
            consulta+="where folio like '%" + t_busca.getText() +"%' OR folio_externo like '%" + t_busca.getText() +"%'";
        }
        if(c_filtro.getSelectedItem().toString().compareTo("Estado")==0)
            consulta+="where estado_factura like '%" + t_busca.getText() +"%'";
        consulta+=" order by id_factura desc";
        Session session = HibernateUtil.getSessionFactory().openSession();
        try
        {
            int siguente=inicio+20;
            consulta+=" limit "+inicio+", 20";
            Query query = session.createSQLQuery(consulta);
            query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
            ArrayList resultList=(ArrayList)query.list();
            
            if(resultList.size()>0)
            {
                DefaultTableModel modelo= (DefaultTableModel)t_datos.getModel();
                if(inicio==0)
                    modelo.setNumRows(0);
                for (int x=0; x<resultList.size(); x++) 
                {
                    java.util.HashMap map=(java.util.HashMap)resultList.get(x);
                    if(map.get("folio_externo")==null)
                    {
                        String sucursal="";
                        if(map.get("id_tultitlan")!=null)
                            sucursal="Tul";
                        Object[] renglon=new Object[]{map.get("id"),map.get("fecha").toString(),map.get("rfc_receptor"),map.get("nombre_receptor"),map.get("f_fiscal"),map.get("serie"),map.get("folio"),map.get("tot"),map.get("estado_factura"),sucursal};
                        modelo.addRow(renglon);
                    }
                    else
                    {
                        String sucursal="";
                        if(map.get("id_tultitlan")!=null)
                            sucursal="Tul";
                        Object[] renglon1=new Object[]{map.get("id"),map.get("fecha").toString(),map.get("rfc_receptor"),map.get("nombre_receptor"),map.get("f_fiscal"),map.get("serie_externo"),map.get("folio_externo"),map.get("tot"),map.get("estado_factura"), sucursal};
                        modelo.addRow(renglon1);
                    }
                }
                inicio=siguente;
            }
            else
            {
                parar=true;
                if(inicio==0)
                {
                    DefaultTableModel modelo= (DefaultTableModel)t_datos.getModel();
                    modelo.setNumRows(0);
                }
            }
            t_datos.revalidate();
        }catch(Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            if(session!=null)
                if(session.isOpen())
                    session.close();
        }
}
    private Factura returnStatus = RET_CANCEL;
    
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
                      column.setPreferredWidth(30);
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
     
     public void habilita(boolean op)
     {
         c_filtro.setEnabled(op);
         t_busca.setEnabled(op);
         t_datos.setEnabled(op);
         b_cancelar.setEnabled(op);
         b_seleccionar.setEnabled(op);
     }
}
