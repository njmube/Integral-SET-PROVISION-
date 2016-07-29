/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Valuacion;

import Hibernate.Util.HibernateUtil;
import Hibernate.entidades.Catalogo;
import Hibernate.entidades.Configuracion;
import Hibernate.entidades.Ejemplar;
import Hibernate.entidades.Foto;
import Hibernate.entidades.Item;
import Hibernate.entidades.Orden;
import Hibernate.entidades.Partida;
import Hibernate.entidades.Proveedor;
import Hibernate.entidades.Servicio;
import Hibernate.entidades.Usuario;
import Proveedor.buscaProveedor;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.GrayColor;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPTable;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Vector;
import javax.swing.DefaultCellEditor;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import Integral.DefaultTableHeaderCellRenderer;
import Integral.ExtensionFileFilter;
import Integral.FormatoEditor;
import Integral.FormatoTabla;
import Integral.Herramientas;
import Integral.HorizontalBarUI;
import Integral.PDF;
import Integral.VerticalBarUI;
import Integral.VerticalTableHeaderCellRenderer;
import java.util.ArrayList;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import org.hibernate.Criteria;
import org.hibernate.Query;

/**
 *
 * @author I.S.C Salvador
 */
public class valuacionDirecta extends javax.swing.JPanel {

    /**
     * Creates new form valuacionDirecta
     */
    private String orden;
    private Usuario user;
    MyModel model;
    private Partida aux;
    Orden ord;
    String sessionPrograma="";
    Herramientas h;
    int x=0, entro=0;
    FormatoTabla formato;
    boolean habilita=false;
    boolean inicio=false;
    
    String[] columnas = new String [] {
        "No","#",
        "Esp Hoj","Esp Mec","Esp Sus","Esp Ele",
        "DM","Cam",
        "Rep Min","Rep Med","Rep Max","Pin",
        "Can","Med","Descripción","Fol","Codigo","Freeze", "Ori","Nal","Desm",
        "Recon","TOT","I. DM","Camb","Rep Min","Rep Med","Rep Max","Pin Min","Pin Med","Pin Max","Instrucción", "Tipo", "Orden", "A. Val"};
    
    public valuacionDirecta(String ord, Usuario us, String edo, String ses) {
        initComponents();
        scroll.getVerticalScrollBar().setUI(new VerticalBarUI());
        scroll.getHorizontalScrollBar().setUI(new HorizontalBarUI());
        sessionPrograma=ses;
        orden=ord;
        user=us;
        t_datos.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        model=new MyModel(0, columnas);
        t_datos.setModel(model);
        formato = new FormatoTabla();
        formatoTabla();
        buscaCuentas(-1,-1);
        servicios();
        if(edo.compareTo("")==0)
        {
            if(r_cerrar.isSelected()==true)
                visualiza(false, false, false, false, false, false, false, false, false, false, false, false);
            else
            visualiza(true, true, true, true, true, true, true, true, true, true, true, true);
        }
        else
            visualiza(false, false, false, false, false, false, false, false, false, false, false, false);
        
        h=new Herramientas(user, 0);
        if(h.isCerrada(orden)==true)
            visualiza(false, false, false, false, false, false, false, false, false, false, false, false);

        entro=1;
        validaCerrado();
    }


   public void tabla_tamaños()
   {
        TableColumnModel col_model = t_datos.getColumnModel();
        DefaultTableCellRenderer tcr = new DefaultTableCellRenderer();
        tcr.setHorizontalAlignment(SwingConstants.RIGHT);
        FormatoEditor fe=new FormatoEditor();
        t_datos.setDefaultEditor(Double.class, fe);
        for (int i=0; i<t_datos.getColumnCount(); i++)
        {
  	      TableColumn column = col_model.getColumn(i);
              switch(i)
              {
                  case 0:
                      column.setPreferredWidth(56);
                      //column.setCellRenderer(tcr);
                      break;
                  case 1:
                      column.setPreferredWidth(40);
                      //column.setCellRenderer(tcr);
                      break;
                      
                  case 12:
                      column.setPreferredWidth(40);
                      //column.setCellRenderer(tcr);
                      //column.setCellEditor(new DefaultCellEditor(t_doble));
                      break;
                  case 13:
                      column.setPreferredWidth(53);
                      DefaultCellEditor editor = new DefaultCellEditor(medida);
                      column.setCellEditor(editor); 
                      editor.setClickCountToStart(2);
                      break;
                  case 14:
                      column.setPreferredWidth(300);
                      break;
                  case 15:
                      column.setPreferredWidth(50);
                      break;
                  case 16:
                      column.setPreferredWidth(100);
                      //DefaultCellEditor miEditor = new DefaultCellEditor(numeros);
                      //miEditor.setClickCountToStart(2);
                      //column.setCellEditor(miEditor);
                      break;
                  case 22:
                      column.setPreferredWidth(50);
                      break;
                  case 31:
                      column.setPreferredWidth(300);
                      column.setCellEditor(new DefaultCellEditor(instruccion)); 
                      break;
                  case 33:
                      column.setPreferredWidth(80);
                      break;    
                  default:
                      column.setPreferredWidth(20);
                      break;
              }
        }
        JTableHeader header = t_datos.getTableHeader();
        header.setBackground(new java.awt.Color(2, 135, 242));//102,102,102
        header.setForeground(Color.white);
   }  
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        medida = new javax.swing.JComboBox();
        t_numero = new javax.swing.JTextField();
        instruccion = new javax.swing.JTextField();
        numeros = new javax.swing.JComboBox();
        b_valuacion = new javax.swing.JButton();
        t_doble = new javax.swing.JFormattedTextField();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        t_busca = new javax.swing.JTextField();
        b_busca = new javax.swing.JButton();
        b_mas_sup_partida = new javax.swing.JButton();
        b_tot = new javax.swing.JButton();
        b_exel = new javax.swing.JButton();
        b_pdf = new javax.swing.JButton();
        b_mas_partida = new javax.swing.JButton();
        b_menos = new javax.swing.JButton();
        c_filtro = new javax.swing.JComboBox();
        r_cerrar = new javax.swing.JRadioButton();
        cb_tipo_partida = new javax.swing.JComboBox();
        c_servicio = new javax.swing.JComboBox();
        scroll = new javax.swing.JScrollPane();
        t_datos = new javax.swing.JTable();

        medida.setFont(new java.awt.Font("Dialog", 0, 9)); // NOI18N
        medida.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "PZAS", "LTS", "MTS", "CMS", "MMS", "GRS", "MLS", "KGS", "HRS", "MIN", "KIT", "FT", "LB", "JGO", "NA" }));

        t_numero.setFont(new java.awt.Font("Dialog", 0, 10)); // NOI18N
        t_numero.setForeground(new java.awt.Color(102, 102, 102));
        t_numero.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        t_numero.setAlignmentX(0.0F);
        t_numero.setAlignmentY(0.0F);
        t_numero.setBorder(null);
        t_numero.setMinimumSize(new java.awt.Dimension(3, 14));
        t_numero.setPreferredSize(new java.awt.Dimension(3, 14));
        t_numero.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                t_numeroKeyTyped(evt);
            }
        });

        instruccion.setForeground(new java.awt.Color(102, 102, 102));
        instruccion.setText("jTextField1");
        instruccion.setBorder(null);
        instruccion.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                instruccionKeyTyped(evt);
            }
        });

        numeros.setFont(new java.awt.Font("Dialog", 0, 9)); // NOI18N
        numeros.setForeground(new java.awt.Color(102, 102, 102));
        numeros.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                numerosFocusLost(evt);
            }
        });

        b_valuacion.setFont(new java.awt.Font("Tahoma", 0, 9)); // NOI18N
        b_valuacion.setText("A Valuación");
        b_valuacion.setToolTipText("envia a valuacion las partidas");
        b_valuacion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_valuacionActionPerformed(evt);
            }
        });

        t_doble.setBorder(null);
        t_doble.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.0"))));
        t_doble.setFont(new java.awt.Font("Dialog", 0, 10)); // NOI18N

        setBackground(new java.awt.Color(255, 255, 255));
        setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        setLayout(new java.awt.BorderLayout());

        jPanel1.setBackground(new java.awt.Color(2, 135, 242));
        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Arial", 0, 9)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Buscar:");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(205, 10, -1, -1));

        t_busca.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        t_busca.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                t_buscaActionPerformed(evt);
            }
        });
        t_busca.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                t_buscaKeyTyped(evt);
            }
        });
        jPanel1.add(t_busca, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 3, 210, -1));

        b_busca.setIcon(new ImageIcon("imagenes/buscar1.png"));
        b_busca.setToolTipText("Busca una partida");
        b_busca.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_buscaActionPerformed(evt);
            }
        });
        jPanel1.add(b_busca, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 2, 23, 23));

        b_mas_sup_partida.setIcon(new ImageIcon("imagenes/boton_mas_#.png"));
        b_mas_sup_partida.setMnemonic('2');
        b_mas_sup_partida.setToolTipText("Agrega una complementaria (ALT+2)");
        b_mas_sup_partida.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_mas_sup_partidaActionPerformed(evt);
            }
        });
        jPanel1.add(b_mas_sup_partida, new org.netbeans.lib.awtextra.AbsoluteConstraints(23, 2, 23, 23));

        b_tot.setIcon(new ImageIcon("imagenes/boton_mas_PROV.png"));
        b_tot.setToolTipText("Trabajo en taller externo");
        b_tot.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_totActionPerformed(evt);
            }
        });
        jPanel1.add(b_tot, new org.netbeans.lib.awtextra.AbsoluteConstraints(860, 0, 23, 23));

        b_exel.setIcon(new ImageIcon("imagenes/xls_icon.png"));
        b_exel.setToolTipText("Exporta a EXCEL");
        b_exel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_exelActionPerformed(evt);
            }
        });
        jPanel1.add(b_exel, new org.netbeans.lib.awtextra.AbsoluteConstraints(624, 2, 23, 23));

        b_pdf.setIcon(new ImageIcon("imagenes/pdf_icon.png"));
        b_pdf.setToolTipText("Exporta a PDF");
        b_pdf.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_pdfActionPerformed(evt);
            }
        });
        jPanel1.add(b_pdf, new org.netbeans.lib.awtextra.AbsoluteConstraints(601, 2, 23, 23));

        b_mas_partida.setIcon(new ImageIcon("imagenes/boton_mas_n.png"));
        b_mas_partida.setMnemonic('1');
        b_mas_partida.setToolTipText("Agrega una partida (ALT+1)");
        b_mas_partida.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_mas_partidaActionPerformed(evt);
            }
        });
        jPanel1.add(b_mas_partida, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 2, 23, 23));

        b_menos.setIcon(new ImageIcon("imagenes/boton_menos.png"));
        b_menos.setToolTipText("Elimina la partida seleccionada");
        b_menos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_menosActionPerformed(evt);
            }
        });
        jPanel1.add(b_menos, new org.netbeans.lib.awtextra.AbsoluteConstraints(167, 2, 23, 23));

        c_filtro.setFont(new java.awt.Font("Tahoma", 0, 9)); // NOI18N
        c_filtro.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Todos", "Hojalateria", "Mecanica", "Suspension", "Electricidad", "Pintura" }));
        jPanel1.add(c_filtro, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 3, 100, -1));

        r_cerrar.setFont(new java.awt.Font("Tahoma", 0, 9)); // NOI18N
        r_cerrar.setForeground(new java.awt.Color(255, 255, 255));
        r_cerrar.setText("Cerrar y enviar a valuación");
        r_cerrar.setToolTipText("Al marcar esta casilla el levantamiento se cerrara.");
        r_cerrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                r_cerrarActionPerformed(evt);
            }
        });
        jPanel1.add(r_cerrar, new org.netbeans.lib.awtextra.AbsoluteConstraints(890, 0, -1, -1));

        cb_tipo_partida.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        cb_tipo_partida.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Ordinaria" }));
        cb_tipo_partida.setBorder(null);
        jPanel1.add(cb_tipo_partida, new org.netbeans.lib.awtextra.AbsoluteConstraints(46, 3, 120, -1));

        c_servicio.setFont(new java.awt.Font("Arial", 0, 10)); // NOI18N
        c_servicio.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "SERVICIOS" }));
        c_servicio.setBorder(null);
        c_servicio.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                c_servicioItemStateChanged(evt);
            }
        });
        c_servicio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                c_servicioActionPerformed(evt);
            }
        });
        jPanel1.add(c_servicio, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 2, 170, -1));

        add(jPanel1, java.awt.BorderLayout.PAGE_END);

        scroll.setPreferredSize(new java.awt.Dimension(453, 150));

        t_datos.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        t_datos.setForeground(new java.awt.Color(102, 102, 102));
        t_datos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "N°", "#", "Grupo", "D/M", "Rep Min ", "Title 6", "Title 7", "Title 8", "Title 9", "Title 10", "Title 11", "Title 12", "Title 13", "Title 14", "Title 15", "Title 16", "Title 17", "Title 18", "Title 19", "Title 20", "Title 21", "Title 22", "Title 23", "Title 24", "Title 25", "Title 26", "Title 27", "Title 28", "Title 29", "Title 30"
            }
        ));
        t_datos.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        t_datos.setGridColor(new java.awt.Color(102, 102, 102));
        t_datos.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        t_datos.getTableHeader().setReorderingAllowed(false);
        t_datos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                t_datosMouseClicked(evt);
            }
        });
        t_datos.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                t_datosKeyPressed(evt);
            }
        });
        scroll.setViewportView(t_datos);

        add(scroll, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void b_buscaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_buscaActionPerformed
        // TODO add your handling code here:
        h=new Herramientas(user, 0);
        h.session(sessionPrograma);
        if(this.t_busca.getText().compareToIgnoreCase("")!=0)
        {
            buscaCuentas(-1,-1);
            if(x>=t_datos.getRowCount())
            {
                x=0;
                java.awt.Rectangle r = t_datos.getCellRect( x, 3, true);
                t_datos.scrollRectToVisible(r);
            }
            for(; x<t_datos.getRowCount(); x++)
            {
                if(t_datos.getValueAt(x, 14).toString().indexOf(t_busca.getText()) != -1)
                {
                    t_datos.setRowSelectionInterval(x, x);
                    t_datos.setColumnSelectionInterval(14, 14);
                    java.awt.Rectangle r = t_datos.getCellRect( x, 3, true);
                    t_datos.scrollRectToVisible(r);
                    break;
                }
            }
            x++;
        }
    }//GEN-LAST:event_b_buscaActionPerformed

    private void b_valuacionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_valuacionActionPerformed
        // TODO add your handling code here:
        
    }//GEN-LAST:event_b_valuacionActionPerformed

    private void b_totActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_totActionPerformed
        // TODO add your handling code here:
        h=new Herramientas(user, 0);
        h.session(sessionPrograma);

        if(t_datos.getSelectedRow()>-1)
        {
            buscaProveedor obj = new buscaProveedor(new javax.swing.JFrame(), true, this.user, this.sessionPrograma);
            obj.t_busca.requestFocus();
            Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
            obj.setLocation((d.width/2)-(obj.getWidth()/2), (d.height/2)-(obj.getHeight()/2));
            obj.setVisible(true);

            Proveedor prov=obj.getReturnStatus();

            if (prov!=null)
                {
                    Session session = HibernateUtil.getSessionFactory().openSession();
                    try
                    {
                        session.beginTransaction().begin();
                        user = (Usuario)session.get(Usuario.class, user.getIdUsuario());
                        if(user.getEditarLevantamiento()==true || (r_cerrar.isSelected()==false && (boolean)t_datos.getValueAt(t_datos.getSelectedRow(), 34)==false))
                        {
                            if(t_datos.getValueAt(t_datos.getSelectedRow(), 32).toString().compareTo("e")!=0)
                            {
                                Partida part=(Partida) session.createCriteria(Partida.class).add(Restrictions.eq("ordenByIdOrden.idOrden", Integer.parseInt(orden))).add(Restrictions.eq("idEvaluacion", Integer.parseInt(t_datos.getValueAt(t_datos.getSelectedRow(), 0).toString()))).add(Restrictions.eq("subPartida", Integer.parseInt(t_datos.getValueAt(t_datos.getSelectedRow(), 1).toString()))).setMaxResults(1).uniqueResult();
                                if(part!=null)
                                {
                                        prov = (Proveedor) session.get(Proveedor.class, prov.getIdProveedor());
                                        part.setProveedor(prov);
                                        session.update(part);
                                        session.getTransaction().commit();
                                        if(session.isOpen()==true)
                                        {
                                            session.flush();
                                            session.clear();
                                            session.close();
                                        }
                                        buscaCuentas(-1,-1);
                                }
                                else
                                {
                                    buscaCuentas(-1,-1);
                                    JOptionPane.showMessageDialog(null, "La partida ya no existe");
                                }
                            }
                            else
                            {
                                JOptionPane.showMessageDialog(null, "No se pueden modificar partidas enlazadas de otra orden");
                            }
                        }
                        else
                        {
                            session.getTransaction().rollback();
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
                        session.getTransaction().rollback();
                        System.out.println(e);
                    }
                    if(session!=null)
                        if(session.isOpen()==true)
                        {
                            session.flush();
                            session.clear();
                            session.close();
                        }
                }
            else
            {
                Session session = HibernateUtil.getSessionFactory().openSession();
                try
                {
                    session.beginTransaction().begin();
                    user = (Usuario)session.get(Usuario.class, user.getIdUsuario());
                    if(user.getEditarLevantamiento()==true || (r_cerrar.isSelected()==false && (boolean)t_datos.getValueAt(t_datos.getSelectedRow(), 34)==false))
                    {
                        if(t_datos.getValueAt(t_datos.getSelectedRow(), 32).toString().compareTo("e")!=0)
                        {
                            Partida part=(Partida) session.createCriteria(Partida.class).add(Restrictions.eq("ordenByIdOrden.idOrden", Integer.parseInt(orden))).add(Restrictions.eq("idEvaluacion", Integer.parseInt(t_datos.getValueAt(t_datos.getSelectedRow(), 0).toString()))).add(Restrictions.eq("subPartida", Integer.parseInt(t_datos.getValueAt(t_datos.getSelectedRow(), 1).toString()))).setMaxResults(1).uniqueResult();
                            if(part!=null)
                            {
                                    part.setProveedor(null);
                                    session.update(part);
                                    session.getTransaction().commit();
                                    if(session.isOpen()==true)
                                    {
                                        session.flush();
                                        session.clear();
                                        session.close();
                                    }
                                    buscaCuentas(-1,-1);
                                    JOptionPane.showMessageDialog(null, "La partida fue actualizada");
                            }
                            else
                            {
                                buscaCuentas(-1,-1);
                                JOptionPane.showMessageDialog(null, "La partida ya no existe");
                            }
                        }
                        else
                        {
                            JOptionPane.showMessageDialog(null, "No se pueden modificar partidas enlazadas de otra orden");
                        }
                    }
                    else
                    {
                        session.getTransaction().rollback();
                        if(session.isOpen()==true)
                        {
                            session.flush();
                            session.clear();
                            session.close();
                        }
                        JOptionPane.showMessageDialog(null, "¡Acceso denegado!");
                    }
                }catch(Exception e)
                {
                    session.getTransaction().rollback();
                    System.out.println(e);
                }
                if(session!=null)
                    if(session.isOpen()==true)
                    {
                        session.flush();
                        session.clear();
                        session.close();
                    }
            }
        }
        else
        {
            JOptionPane.showMessageDialog(null, "¡Debes seleccionar primero una partida de la tabla!");
        }
    }//GEN-LAST:event_b_totActionPerformed

    private void b_mas_sup_partidaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_mas_sup_partidaActionPerformed
        // TODO add your handling code here:
        h=new Herramientas(user, 0);
        h.session(sessionPrograma);
        if(t_datos.getRowCount()>0)
        {
            if(t_datos.getSelectedRow()>-1)
            {
                if(t_datos.getValueAt(t_datos.getSelectedRow(), 32).toString().compareTo("e")!=0)
                {
                    Session session = HibernateUtil.getSessionFactory().openSession();
                    try
                    {
                        user = (Usuario)session.get(Usuario.class, user.getIdUsuario());
                        if(this.cb_tipo_partida.getSelectedItem().toString().compareTo("Ordinaria")==0)
                        {
                            if(user.getOrdinariaLevantamiento()==true  && (user.getEditarLevantamiento()==true || r_cerrar.isSelected()==false))
                            {
                                nuevaPartida(1, "o", -1);
                            }
                            else
                                JOptionPane.showMessageDialog(null, "¡Acceso denegado!");
                        }

                        if(this.cb_tipo_partida.getSelectedItem().toString().compareTo("Complementaria")==0)
                        {
                            if(user.getComplementariaLevantamiento()==true)
                            {
                                nuevaPartida(1, "c", -1);
                            }
                            else
                                JOptionPane.showMessageDialog(null, "¡Acceso denegado!");
                        }

                        if(this.cb_tipo_partida.getSelectedItem().toString().compareTo("Adicional")==0)
                        {
                            if(user.getAdicionalLevantamiento()==true)
                            {
                                nuevaPartida(1, "a", -1);
                            }
                            else
                                JOptionPane.showMessageDialog(null, "¡Acceso denegado!");
                        }
                    }catch(Exception e)
                    {
                        System.out.println(e);
                    }
                    if(session!=null)
                        if(session.isOpen()==true)
                        {
                            session.flush();
                            session.clear();
                            session.close();
                        }
                }
                else
                {
                    JOptionPane.showMessageDialog(null, "No se puede agregar sub-partidas a partidas enlazadas");
                }
            }
            else
            {
                JOptionPane.showMessageDialog(null, "Debes seleccionar una partida primero");
            }
        }
    }//GEN-LAST:event_b_mas_sup_partidaActionPerformed

    private void b_exelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_exelActionPerformed
        // TODO add your handling code here:
        h=new Herramientas(user, 0);
        h.session(sessionPrograma);
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
                    Sheet hoja = libro.createSheet("Prevaluacion");
                    for(int ren=0;ren<(t_datos.getRowCount()+1);ren++)
                    {
                        Row fila = hoja.createRow(ren);
                        for(int col=0; col<t_datos.getColumnCount(); col++)
                        {
                            Cell celda = fila.createCell(col);
                            if(ren==0)
                            {
                                celda.setCellValue(columnas[col]);
                            }
                            else
                            {
                                try
                                {
                                    if(t_datos.getValueAt(ren-1, col).toString().compareToIgnoreCase("false")==0)
                                        celda.setCellValue(0.0);
                                    else
                                    {
                                        if(t_datos.getValueAt(ren-1, col).toString().compareToIgnoreCase("true")==0)
                                            celda.setCellValue(1.0);
                                        else
                                            celda.setCellValue(t_datos.getValueAt(ren-1, col).toString());
                                    }
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
                    JOptionPane.showMessageDialog(this, "No se pudo realizar el reporte si el archivo esta abierto");
                }
            }
        } 
        
        
    }//GEN-LAST:event_b_exelActionPerformed

    private void b_pdfActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_pdfActionPerformed
        // TODO add your handling code here:
        h=new Herramientas(user, 0);
        h.session(sessionPrograma);
        Session session = HibernateUtil.getSessionFactory().openSession();
        try
        {
            session.beginTransaction().begin();
            BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.WINANSI, BaseFont.NOT_EMBEDDED);
            //Orden ord=buscaApertura();
            PDF reporte = new PDF();
            Date fecha = new Date();
            DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyyHH-mm-ss");//YYYY-MM-DD HH:MM:SS
            String valor=dateFormat.format(fecha);
            File folder = new File("reportes/"+ord.getIdOrden());
            folder.mkdirs();
            reporte.Abrir(PageSize.LETTER.rotate(), "Valuación", "reportes/"+ord.getIdOrden()+"/"+valor+"-levantamiento.pdf");
            Font font = new Font(Font.FontFamily.HELVETICA, 6, Font.BOLD);
            BaseColor contenido=BaseColor.WHITE;
            int centro=Element.ALIGN_CENTER;
            int izquierda=Element.ALIGN_LEFT;
            int derecha=Element.ALIGN_RIGHT;
            float tam[]=new float[]{15,15,7,7,7,7,7,59,125,11,11,11,11,16, 12,12, 12,12,12, 12,12,12,100};
            PdfPTable tabla=reporte.crearTabla(23, tam, 100, Element.ALIGN_LEFT);
            
            cabecera(reporte, bf, tabla);
            
                session.beginTransaction().begin();
                Orden ord = (Orden)session.get(Orden.class, Integer.parseInt(orden));
                //Partida resp=(Partida[]) session.createCriteria(Partida.class).add(Restrictions.eq("ordenByIdOrden.idOrden", Integer.parseInt(orden))).addOrder(Order.desc("idEvaluacion")).addOrder(Order.desc("subPartida"));
                //Partida[] cuentas = (Partida[]) ord.getPartidas().toArray(new Partida[0]);
                List cuentas=null;
                switch(c_filtro.getSelectedItem().toString())
                {
                    case "Todos":
                        cuentas= session.createCriteria(Partida.class).add(Restrictions.eq("ordenByIdOrden.idOrden", Integer.parseInt(orden))).addOrder(Order.asc("idEvaluacion")).addOrder(Order.asc("subPartida")).list();
                        break;
                        
                    case "Hojalateria":
                        cuentas= session.createCriteria(Partida.class).add(Restrictions.eq("ordenByIdOrden.idOrden", Integer.parseInt(orden))).add(Restrictions.eq("espHoj", true)).addOrder(Order.asc("idEvaluacion")).addOrder(Order.asc("subPartida")).list();
                        break;
                        
                    case "Mecanica":
                        cuentas= session.createCriteria(Partida.class).add(Restrictions.eq("ordenByIdOrden.idOrden", Integer.parseInt(orden))).add(Restrictions.eq("espMec", true)).addOrder(Order.asc("idEvaluacion")).addOrder(Order.asc("subPartida")).list();
                        break;
                        
                    case "Suspension":
                        cuentas= session.createCriteria(Partida.class).add(Restrictions.eq("ordenByIdOrden.idOrden", Integer.parseInt(orden))).add(Restrictions.eq("espSus", true)).addOrder(Order.asc("idEvaluacion")).addOrder(Order.asc("subPartida")).list();
                        break;
                        
                    case "Electricidad":
                        cuentas= session.createCriteria(Partida.class).add(Restrictions.eq("ordenByIdOrden.idOrden", Integer.parseInt(orden))).add(Restrictions.eq("espEle", true)).addOrder(Order.asc("idEvaluacion")).addOrder(Order.asc("subPartida")).list();
                        break;
                    case "Pintura":
                        cuentas= session.createCriteria(Partida.class).add(Restrictions.eq("ordenByIdOrden.idOrden", Integer.parseInt(orden))).add(Restrictions.ne("pint", -1.0d)).addOrder(Order.asc("idEvaluacion")).addOrder(Order.asc("subPartida")).list();
                        break;
                }
                
                int ren=0;
                if(cuentas.size()>0)
                {
                    for(int i=0; i<cuentas.size(); i++)
                    {
                        Partida Part = (Partida) cuentas.get(i);
                        /*if(Part.isRefCoti())
                        {*/
                            tabla.addCell(reporte.celda(""+Part.getCant(), font, contenido, izquierda, 0,1,Rectangle.RECTANGLE));
                            tabla.addCell(reporte.celda(Part.getMed(), font, contenido, izquierda, 0,1,Rectangle.RECTANGLE));
                            
                            tabla.addCell(reporte.celda((Part.isEspHoj()==true ? "x" : ""), font, contenido, izquierda, 0,0,Rectangle.RECTANGLE));
                            tabla.addCell(reporte.celda((Part.isEspMec()==true ? "x" : ""), font, contenido, izquierda, 0,0,Rectangle.RECTANGLE));
                            tabla.addCell(reporte.celda((Part.isEspSus()==true ? "x" : ""), font, contenido, izquierda, 0,0,Rectangle.RECTANGLE));
                            tabla.addCell(reporte.celda((Part.isEspEle()==true ? "x" : ""), font, contenido, izquierda, 0,0,Rectangle.RECTANGLE));
                            tabla.addCell(reporte.celda((Part.getPint()>-1 ? "x" : ""), font, contenido, izquierda, 0,0,Rectangle.RECTANGLE));
                            
                            tabla.addCell(reporte.celda(Part.getCatalogo().getEspecialidad().getDescripcion(), font, contenido, izquierda, 0,0,Rectangle.RECTANGLE));
                            if(Part.getEjemplar()!=null)
                                tabla.addCell(reporte.celda(Part.getCatalogo().getNombre()+" [NP:"+Part.getEjemplar().getIdParte()+"]", font, contenido, izquierda, 0,0,Rectangle.RECTANGLE));
                            else
                                tabla.addCell(reporte.celda(Part.getCatalogo().getNombre(), font, contenido, izquierda, 0,0,Rectangle.RECTANGLE));
                            if(Part.isOri()==true)
                                tabla.addCell(reporte.celda("X", font, contenido, izquierda, 0,1,Rectangle.RECTANGLE));
                            else
                                tabla.addCell(reporte.celda(" ", font, contenido, izquierda, 0,1,Rectangle.RECTANGLE));

                            if(Part.isNal()==true)
                                tabla.addCell(reporte.celda("X", font, contenido, izquierda, 0,1,Rectangle.RECTANGLE));
                            else
                                tabla.addCell(reporte.celda(" ", font, contenido, izquierda, 0,1,Rectangle.RECTANGLE));

                            if(Part.isDesm()==true)
                                tabla.addCell(reporte.celda("X", font, contenido, izquierda, 0,1,Rectangle.RECTANGLE));
                            else
                                tabla.addCell(reporte.celda(" ", font, contenido, izquierda, 0,1,Rectangle.RECTANGLE));

                            if(Part.isPd()==true)
                                tabla.addCell(reporte.celda("X", font, contenido, izquierda, 0,1,Rectangle.RECTANGLE));
                            else
                                tabla.addCell(reporte.celda(" ", font, contenido, izquierda, 0,1,Rectangle.RECTANGLE));
                            
                            if(Part.getProveedor()!=null)
                                tabla.addCell(reporte.celda(""+Part.getProveedor().getIdProveedor(), font, contenido, izquierda, 0,1,Rectangle.RECTANGLE));
                            else
                                tabla.addCell(reporte.celda(" ", font, contenido, izquierda, 0,1,Rectangle.RECTANGLE));

                            if(Part.getIntDesm()>-1)
                                tabla.addCell(reporte.celda("X", font, contenido, izquierda, 0,1,Rectangle.RECTANGLE));
                            else
                                tabla.addCell(reporte.celda(" ", font, contenido, izquierda, 0,1,Rectangle.RECTANGLE));

                            if(Part.getIntCamb()>-1)
                                tabla.addCell(reporte.celda("X", font, contenido, izquierda, 0,1,Rectangle.RECTANGLE));
                            else
                                tabla.addCell(reporte.celda(" ", font, contenido, izquierda, 0,1,Rectangle.RECTANGLE));

                            if(Part.getIntRepMin()>-1)
                                tabla.addCell(reporte.celda("X", font, contenido, izquierda, 0,1,Rectangle.RECTANGLE));
                            else
                                tabla.addCell(reporte.celda(" ", font, contenido, izquierda, 0,1,Rectangle.RECTANGLE));

                            if(Part.getIntRepMed()>-1)
                                tabla.addCell(reporte.celda("X", font, contenido, izquierda, 0,1,Rectangle.RECTANGLE));
                            else
                                tabla.addCell(reporte.celda(" ", font, contenido, izquierda, 0,1,Rectangle.RECTANGLE));

                            if(Part.getIntRepMax()>-1)
                                tabla.addCell(reporte.celda("X", font, contenido, izquierda, 0,1,Rectangle.RECTANGLE));
                            else
                                tabla.addCell(reporte.celda(" ", font, contenido, izquierda, 0,1,Rectangle.RECTANGLE));

                            if(Part.getIntPinMin()>-1)
                                tabla.addCell(reporte.celda("X", font, contenido, izquierda, 0,1,Rectangle.RECTANGLE));
                            else
                                tabla.addCell(reporte.celda(" ", font, contenido, izquierda, 0,1,Rectangle.RECTANGLE));

                            if(Part.getIntPinMed()>-1)
                                tabla.addCell(reporte.celda("X", font, contenido, izquierda, 0,1,Rectangle.RECTANGLE));
                            else
                                tabla.addCell(reporte.celda(" ", font, contenido, izquierda, 0,1,Rectangle.RECTANGLE));

                            if(Part.getIntPinMax()>-1)
                                tabla.addCell(reporte.celda("X", font, contenido, izquierda, 0,1,Rectangle.RECTANGLE));
                            else
                                tabla.addCell(reporte.celda(" ", font, contenido, izquierda, 0,1,Rectangle.RECTANGLE));
                            if(Part.getInstruccion()!=null)
                                tabla.addCell(reporte.celda(Part.getInstruccion(), font, contenido, izquierda, 0,1,Rectangle.RECTANGLE));
                            else
                                tabla.addCell(reporte.celda("", font, contenido, izquierda, 0,1,Rectangle.RECTANGLE));
                            /*if(ren==38)
                            {
                                reporte.agregaObjeto(tabla);
                                reporte.writer.newPage();
                                tabla=reporte.crearTabla(17, tam, 100, Element.ALIGN_LEFT);
                                cabecera(reporte, bf, tabla);
                                ren=-1;
                            }
                            ren++;*/
                        //}
                    }
                    
                }
                session.beginTransaction().rollback();
            
            tabla.setHeaderRows(3);
            reporte.agregaObjeto(tabla);
            reporte.cerrar();
            reporte.visualizar("reportes/"+ord.getIdOrden()+"/"+valor+"-levantamiento.pdf");
            
        }catch(Exception e)
        {
            System.out.println(e);
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "No se pudo realizar el reporte si el archivo esta abierto.");
        }
        if(session!=null)
            if(session.isOpen())
            {
                session.flush();
                session.clear();
                session.close();
            }
    }//GEN-LAST:event_b_pdfActionPerformed

    private void b_mas_partidaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_mas_partidaActionPerformed
        // TODO add your handling code here:
        h=new Herramientas(user, 0);
        h.session(sessionPrograma);
        
        Session session = HibernateUtil.getSessionFactory().openSession();
        try
        {
            user = (Usuario)session.get(Usuario.class, user.getIdUsuario());
            if(this.cb_tipo_partida.getSelectedItem().toString().compareTo("Ordinaria")==0)
            {
                if(user.getOrdinariaLevantamiento()==true && (user.getEditarLevantamiento()==true || r_cerrar.isSelected()==false))
                {
                    nuevaPartida(0, "o", -1);
                }
                else
                    JOptionPane.showMessageDialog(null, "¡Acceso denegado!");
            }

            if(this.cb_tipo_partida.getSelectedItem().toString().compareTo("Complementaria")==0)
            {
                if(user.getComplementariaLevantamiento()==true)
                    nuevaPartida(0, "c", -1);
                else
                    JOptionPane.showMessageDialog(null, "¡Acceso denegado!");
            }

            if(this.cb_tipo_partida.getSelectedItem().toString().compareTo("Adicional")==0)
            {
                if(user.getAdicionalLevantamiento()==true)
                    nuevaPartida(0, "a", -1);
                else
                    JOptionPane.showMessageDialog(null, "¡Acceso denegado!");
            }

            if(this.cb_tipo_partida.getSelectedItem().toString().compareTo("Enlazada")==0)
            {
                if(user.getEnlazadaLevantamiento()==true)
                    nuevaEnlazada();
                else
                    JOptionPane.showMessageDialog(null, "¡Acceso denegado!");
            }
            t_datos.requestFocus();
        }catch(Exception e)
        {
            System.out.println(e);
        }
        if(session!=null)
            if(session.isOpen())
            {
                session.flush();
                session.clear();
                session.close();
            }
    }//GEN-LAST:event_b_mas_partidaActionPerformed

    private void b_menosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_menosActionPerformed
        // TODO add your handling code here:
        h=new Herramientas(user, 0);
        h.session(sessionPrograma);
        Session session = HibernateUtil.getSessionFactory().openSession();
        try
        {
            user = (Usuario)session.get(Usuario.class, user.getIdUsuario());
            //if(user.getEditarLevantamiento()==true)
            if( (user.getOrdinariaLevantamiento()==true || user.getComplementariaLevantamiento()==true || user.getAdicionalLevantamiento()==true || user.getEnlazadaLevantamiento()==true) && (user.getEditarLevantamiento()==true || r_cerrar.isSelected()==false))
            {
                if(r_cerrar.isSelected()==false)
                {
                    if(t_datos.getSelectedRow()>-1)
                    {
                        int opt=JOptionPane.showConfirmDialog(this, "¡Confirma que deseas eliminar la partidas seleccionadas");
                        if(opt==0)
                        {
                            int [] renglones=t_datos.getSelectedRows();
                            for(int x=0; x<renglones.length;x++)
                            {
                                session.beginTransaction().begin();
                                if(t_datos.getValueAt(renglones[x], 32).toString().compareTo("e")!=0)
                                {
                                    Partida part=(Partida) session.createCriteria(Partida.class).add(Restrictions.eq("ordenByIdOrden.idOrden", ord.getIdOrden())).add(Restrictions.eq("idEvaluacion", Integer.parseInt(t_datos.getValueAt(renglones[x], 0).toString()))).add(Restrictions.eq("subPartida", Integer.parseInt(t_datos.getValueAt(renglones[x], 1).toString()))).setMaxResults(1).uniqueResult();
                                    Orden ord=(Orden)session.get(Orden.class, Integer.parseInt(orden));
                                    if(part.getPedido()==null)
                                    {
                                        ord.eliminaPartidaOrden(part);
                                        session.update(ord);
                                        session.delete(part);
                                        session.getTransaction().commit();
                                    }
                                    else
                                    {
                                        session.getTransaction().rollback();
                                        JOptionPane.showMessageDialog(null, "La partida ya fue pedida");    
                                    }
                                }
                                else
                                {
                                    Partida part=(Partida) session.createCriteria(Partida.class).add(Restrictions.eq("ordenByIdOrden.idOrden", Integer.parseInt(t_datos.getValueAt(renglones[x], 33).toString()))).add(Restrictions.eq("idEvaluacion", Integer.parseInt(t_datos.getValueAt(renglones[x], 0).toString()))).add(Restrictions.eq("subPartida", Integer.parseInt(t_datos.getValueAt(renglones[x], 1).toString()))).setMaxResults(1).uniqueResult();
                                    Orden ord=(Orden)session.get(Orden.class, Integer.parseInt(t_datos.getValueAt(renglones[x], 33).toString()));
                                    part.setOrdenByEnlazada(null);
                                    ord.eliminaPartidaenlazada(part);
                                    session.update(ord);
                                    session.getTransaction().commit();
                                }
                            }
                            buscaCuentas(-1,-1);
                        }
                    }
                    else
                    {
                        javax.swing.JOptionPane.showMessageDialog(null, "Debes seleccionar una partida de la tabla para porde eliminarla");
                    }
                }
                else
                {
                    if(user.getEditarLevantamiento()==true)
                    {
                        if(t_datos.getSelectedRow()>-1)
                        {
                            int opt=JOptionPane.showConfirmDialog(this, "¡Confirma que deseas eliminar las partidas seleccionadas");
                            if(opt==0)
                            {
                                int [] renglones=t_datos.getSelectedRows();
                                for(int x=0; x<renglones.length;x++)
                                {
                                    session.beginTransaction().begin();
                                    if(t_datos.getValueAt(renglones[x], 32).toString().compareTo("e")!=0)
                                    {
                                        Partida part=(Partida) session.createCriteria(Partida.class).add(Restrictions.eq("ordenByIdOrden.idOrden", ord.getIdOrden())).add(Restrictions.eq("idEvaluacion", Integer.parseInt(t_datos.getValueAt(renglones[x], 0).toString()))).add(Restrictions.eq("subPartida", Integer.parseInt(t_datos.getValueAt(renglones[x], 1).toString()))).setMaxResults(1).uniqueResult();
                                        Orden ord=(Orden)session.get(Orden.class, Integer.parseInt(orden));
                                        if(part.getPedido()==null)
                                        {
                                            ord.eliminaPartidaOrden(part);
                                            session.update(ord);
                                            session.delete(part);
                                            session.getTransaction().commit();
                                        }
                                        else
                                        {
                                            session.getTransaction().rollback();
                                            JOptionPane.showMessageDialog(null, "La partida ya fue pedida");    
                                        }
                                    }
                                    else
                                    {
                                        Partida part=(Partida) session.createCriteria(Partida.class).add(Restrictions.eq("ordenByIdOrden.idOrden", Integer.parseInt(t_datos.getValueAt(renglones[x], 33).toString()))).add(Restrictions.eq("idEvaluacion", Integer.parseInt(t_datos.getValueAt(renglones[x], 0).toString()))).add(Restrictions.eq("subPartida", Integer.parseInt(t_datos.getValueAt(renglones[x], 1).toString()))).setMaxResults(1).uniqueResult();
                                        Orden ord=(Orden)session.get(Orden.class, Integer.parseInt(t_datos.getValueAt(renglones[x], 33).toString()));
                                        part.setOrdenByEnlazada(null);
                                        ord.eliminaPartidaenlazada(part);
                                        session.update(ord);
                                        session.getTransaction().commit();
                                    }
                                }
                                buscaCuentas(-1,-1);
                            }
                        }
                        else
                        {
                            javax.swing.JOptionPane.showMessageDialog(null, "Debes seleccionar una partida de la tabla para porde eliminarla");
                        }
                    }
                }
            }
            else
            {
                session.getTransaction().rollback();
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
            session.getTransaction().rollback();
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

    private void t_buscaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_buscaKeyTyped
        // TODO add your handling code here:
        char car = evt.getKeyChar();
        evt.setKeyChar(Character.toUpperCase(evt.getKeyChar()));
    }//GEN-LAST:event_t_buscaKeyTyped

    private void t_buscaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_t_buscaActionPerformed
        // TODO add your handling code here:
        h=new Herramientas(user, 0);
        h.session(sessionPrograma);
        if(this.t_busca.getText().compareToIgnoreCase("")!=0)
        {
            //buscaCuentas(-1,-1);
            if(x>=t_datos.getRowCount())
            {
                x=0;
                java.awt.Rectangle r = t_datos.getCellRect( x, 3, true);
                t_datos.scrollRectToVisible(r);
            }
            for(; x<t_datos.getRowCount(); x++)
            {
                if(t_datos.getValueAt(x, 14).toString().indexOf(t_busca.getText()) != -1)
                {
                    t_datos.setRowSelectionInterval(x, x);
                    t_datos.setColumnSelectionInterval(14, 14);
                    java.awt.Rectangle r = t_datos.getCellRect( x, 3, true);
                    t_datos.scrollRectToVisible(r);
                    break;
                }
            }
            x++;
        }
    }//GEN-LAST:event_t_buscaActionPerformed

    private void t_numeroKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_numeroKeyTyped
        // TODO add your handling code here:
        char car = evt.getKeyChar();
        if(t_numero.getText().length()>=4) 
            evt.consume();
        if((car<'0' || car>'9')) 
            evt.consume();
    }//GEN-LAST:event_t_numeroKeyTyped

    private void instruccionKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_instruccionKeyTyped
        // TODO add your handling code here:
        char car = evt.getKeyChar();
        evt.setKeyChar(Character.toUpperCase(evt.getKeyChar()));
        if(instruccion.getText().length()>=100) 
            evt.consume();
    }//GEN-LAST:event_instruccionKeyTyped

    private void t_datosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_t_datosMouseClicked
        // TODO add your handling code here:
        /*if(t_datos.getSelectedRow()>=0)
        {
            if(t_datos.getSelectedColumn()==16)
            {
                numeros.removeAllItems();
                numeros.addItem("S/C");
                numeros.setSelectedItem("S/C");
                Session session = HibernateUtil.getSessionFactory().openSession();
                try
                {
                    session.beginTransaction().begin();
                    Partida partida=(Partida) session.createCriteria(Partida.class).add(Restrictions.eq("ordenByIdOrden.idOrden", Integer.parseInt(orden))).add(Restrictions.eq("idEvaluacion", Integer.parseInt(t_datos.getValueAt(t_datos.getSelectedRow(), 0).toString()))).add(Restrictions.eq("subPartida", Integer.parseInt(t_datos.getValueAt(t_datos.getSelectedRow(), 1).toString()))).setMaxResults(1).uniqueResult();
                    Ejemplar[] codigos = (Ejemplar[]) partida.getCatalogo().getEjemplars().toArray(new Ejemplar[0]);
                    Ejemplar codigoAux= new Ejemplar();
                    for(int k=0;k<codigos.length;k++) 
                    {
                        for(int f=0;f<(codigos.length-1)-k;f++) 
                        {
                           if (codigos[f].getIdParte().compareTo(codigos[f+1].getIdParte())==1) 
                           {
                               codigoAux=codigos[f];
                               codigos[f]=codigos[f+1];
                               codigos[f+1]=codigoAux;
                           }
                        }
                    }

                    if(codigos.length>0)
                    {
                        for(int i=0; i<codigos.length; i++)
                        {
                            numeros.addItem(codigos[i].getIdParte());
                        }
                    }
                    session.beginTransaction().commit();
                }catch(Exception e)
                {
                     System.out.println(e);
                }
                if(session!=null)
                     if(session.isOpen()==true)
                         session.close();
            }
        }*/
    }//GEN-LAST:event_t_datosMouseClicked

    private void numerosFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_numerosFocusLost
        // TODO add your handling code here:
        entro=1;
    }//GEN-LAST:event_numerosFocusLost

    private void r_cerrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_r_cerrarActionPerformed
        // TODO add your handling code here:
        if(r_cerrar.isSelected()==true)
        {
            Session session = HibernateUtil.getSessionFactory().openSession();
            try
            {
                user = (Usuario)session.get(Usuario.class, user.getIdUsuario());
                if(user.getCarrarLevantamiento()==true)
                {
                    h=new Herramientas(user, 0);
                    h.session(sessionPrograma);
                    int opt=JOptionPane.showConfirmDialog(this, "¡Confirma que deseas pasar a valuación, esta operación cerrará el levantamiento!");
                    if(opt==0)
                    {
                        session.beginTransaction().begin();
                        Orden ord=(Orden)session.get(Orden.class, Integer.parseInt(orden));
                        Partida[] par = (Partida[]) ord.getPartidasForIdOrden().toArray(new Partida[0]);
                        int op=0;
                        for(int x=0; x<par.length; x++)
                        {
                            par[x].setAutorizadoValuacion(true);
                            session.update(par[x]);
                        }
                        //***guardar la fecha de cierre del levantamiento*****
                        Date fecha = new Date();
                        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");//YYYY-MM-DD HH:MM:SS
                        String valor=dateFormat.format(fecha);
                        String [] fech = valor.split("-");
                        String [] hora=fech[2].split(":");
                        String [] aux=hora[0].split(" ");
                        fech[2]=aux[0];
                        hora[0]=aux[1];
                        Calendar calendario3 = Calendar.getInstance();
                        calendario3.set(
                                    Integer.parseInt(fech[2]), 
                                    Integer.parseInt(fech[1])-1, 
                                    Integer.parseInt(fech[0]), 
                                    Integer.parseInt(hora[0]), 
                                    Integer.parseInt(hora[1]), 
                                    Integer.parseInt(hora[2]));
                        ord.setRLevantamientoCierre(calendario3.getTime());
                        session.update(ord);
                    //************
                        session.getTransaction().commit();
                        JOptionPane.showMessageDialog(null, "Las partidas fueron autorizadas para valuación, el levantamiento fue cerrado");
                        buscaCuentas(-1,-1);
                    }
                }
                else
                {
                    r_cerrar.setSelected(false);
                    JOptionPane.showMessageDialog(null, "¡No tienes permiso para cerrar el levantamiento!");
                    session.beginTransaction().rollback();
                }
            }
            catch(Exception e)
            {
                e.printStackTrace();
                session.getTransaction().rollback();
            }
            if(session!=null)
                if(session.isOpen())
                {
                    session.flush();
                    session.clear();
                    session.close(); 
                }
        }
        else
            {
                Session session = HibernateUtil.getSessionFactory().openSession();
                try
                {
                    session.beginTransaction().begin();
                    user = (Usuario)session.get(Usuario.class, user.getIdUsuario());
                    if(user.getAbrirLevantamiento()==true)
                    {
                        session.beginTransaction().begin();
                        h=new Herramientas(user, 0);
                        h.session(sessionPrograma);
                        ord = (Orden)session.get(Orden.class, Integer.parseInt(orden)); 
                        ord.setRLevantamientoCierre(null);
                        session.save(ord);
                        session.getTransaction().commit();
                        buscaCuentas(-1,-1);
                        JOptionPane.showMessageDialog(null, "¡El levantamiento fue abierto!");
                    }
                    else
                    {
                        r_cerrar.setSelected(true);
                        JOptionPane.showMessageDialog(null, "¡No tienes permiso para abrir el levantamiento!");
                        session.beginTransaction().rollback();
                    }
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                    session.getTransaction().rollback();
                }
                if(session!=null)
                    if(session.isOpen())
                    {
                        session.flush();
                        session.clear();
                        session.close(); 
                    }
          }
          validaCerrado();
    }//GEN-LAST:event_r_cerrarActionPerformed

    private void t_datosKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_datosKeyPressed
        // TODO add your handling code here:
        int code = evt.getKeyCode();
        if(code == KeyEvent.VK_ENTER)
        {
            if(t_datos.isCellEditable(t_datos.getSelectedRow(), t_datos.getSelectedColumn())==true)
            {
                if(t_datos.getSelectedColumn()>-1)
                {
                    Class edo=t_datos.getColumnClass(t_datos.getSelectedColumn());
                    if(edo==Boolean.class)
                    {
                        boolean val=(boolean)t_datos.getValueAt(t_datos.getSelectedRow(), t_datos.getSelectedColumn());
                        if(val==false)
                            t_datos.setValueAt(true, t_datos.getSelectedRow(), t_datos.getSelectedColumn());
                        else
                            t_datos.setValueAt(false, t_datos.getSelectedRow(), t_datos.getSelectedColumn());
                    }
                }
            }
            t_datos.getInputMap(JTable.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER,0), "selectNextColumnCell");
        }
    }//GEN-LAST:event_t_datosKeyPressed

    private void c_servicioItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_c_servicioItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_c_servicioItemStateChanged

    private void c_servicioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_c_servicioActionPerformed
        // TODO add your handling code here:
        if(inicio==true)
        {
            if(c_servicio.getSelectedItem().toString().compareTo("SERVICIOS")!=0)
            {
                String valor=c_servicio.getSelectedItem().toString();
                c_servicio.setSelectedItem("SERVICIOS");
                Session session = HibernateUtil.getSessionFactory().openSession();
                try 
                {
                    Item [] renglones =(Item[])session.createCriteria(Item.class).add(Restrictions.eq("servicio.idServicio", valor)).addOrder(Order.asc("idReparacion")).list().toArray(new Item[0]);
                    System.out.println(renglones.length);
                    if(renglones!=null)
                    {
                        user = (Usuario)session.get(Usuario.class, user.getIdUsuario());
                        for(int r=0; r<renglones.length; r++)
                        {
                            if(buscaTabla(renglones[r].getCatalogo().getIdCatalogo())==-1)
                            {
                                if(this.cb_tipo_partida.getSelectedItem().toString().compareTo("Ordinaria")==0)
                                {
                                    if(user.getOrdinariaLevantamiento()==true)
                                    {
                                        nuevaPartida(0, "o", renglones[r].getCatalogo().getIdCatalogo());
                                    }
                                    else
                                    {
                                        r=renglones.length;
                                        JOptionPane.showMessageDialog(null, "¡No tienes permiso de crear partidas ordinarias!");
                                    }
                                }

                                if(this.cb_tipo_partida.getSelectedItem().toString().compareTo("Complementaria")==0)
                                {
                                    if(user.getComplementariaLevantamiento()==true)
                                        nuevaPartida(0, "c", renglones[r].getCatalogo().getIdCatalogo());
                                    else
                                    {
                                        r=renglones.length;
                                        JOptionPane.showMessageDialog(null, "¡No tienes permiso de crear partidas complementarias!");
                                    }
                                }

                                if(this.cb_tipo_partida.getSelectedItem().toString().compareTo("Adicional")==0)
                                {
                                    if(user.getAdicionalLevantamiento()==true)
                                        nuevaPartida(0, "a", renglones[r].getCatalogo().getIdCatalogo());
                                    else
                                    {
                                        r=renglones.length;
                                        JOptionPane.showMessageDialog(null, "¡No tienes permiso de crear partidas  adicionales!");
                                    }
                                }
                            }
                        }
                        
                    }
                }catch(Exception he) 
                {
                    he.printStackTrace();
                }
                finally
                {
                    if(session.isOpen())
                    {
                        session.flush();
                        session.clear();
                        session.close();
                    }
                }
            }
        }
    }//GEN-LAST:event_c_servicioActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton b_busca;
    private javax.swing.JButton b_exel;
    private javax.swing.JButton b_mas_partida;
    private javax.swing.JButton b_mas_sup_partida;
    private javax.swing.JButton b_menos;
    private javax.swing.JButton b_pdf;
    private javax.swing.JButton b_tot;
    private javax.swing.JButton b_valuacion;
    private javax.swing.JComboBox c_filtro;
    private javax.swing.JComboBox c_servicio;
    private javax.swing.JComboBox cb_tipo_partida;
    private javax.swing.JTextField instruccion;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JComboBox medida;
    private javax.swing.JComboBox numeros;
    private javax.swing.JRadioButton r_cerrar;
    private javax.swing.JScrollPane scroll;
    private javax.swing.JTextField t_busca;
    private javax.swing.JTable t_datos;
    private javax.swing.JFormattedTextField t_doble;
    private javax.swing.JTextField t_numero;
    // End of variables declaration//GEN-END:variables

    public void nuevaPartida(int a, String op, int tipo)
    {
        if(a==0)
        {
            agregaPartida obj = null;
            Catalogo registro = null;
            if(tipo==-1)
            {
                obj = new agregaPartida(new javax.swing.JFrame(), true, orden, this.user, this.sessionPrograma);
                Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
                obj.setLocation((d.width/2)-(obj.getWidth()/2), (d.height/2)-(obj.getHeight()/2));
                obj.setVisible(true);
            }
            Session session = HibernateUtil.getSessionFactory().openSession();
            try
            {
                session.beginTransaction().begin();
                if(tipo==-1)
                {
                    registro=obj.getReturnStatus();
                }
                else
                {
                    registro=(Catalogo)session.get(Catalogo.class, tipo);
                }
                if(registro!=null)
                {
                    int r=busca(registro.getIdCatalogo(), op);
                    if(r==-1)
                    {
                        Partida resp=(Partida) session.createCriteria(Partida.class).add(Restrictions.eq("ordenByIdOrden.idOrden", Integer.parseInt(orden))).addOrder(Order.desc("idEvaluacion")).setMaxResults(1).uniqueResult();
                        if(resp!=null)
                        {
                            ord = (Orden)session.get(Orden.class, Integer.parseInt(orden));                                    
                            int id=resp.getIdEvaluacion()+1;
                            Orden ord = (Orden)session.get(Orden.class, Integer.parseInt(orden)); 
                            Partida part=new Partida();
                            part.setIdEvaluacion(id);
                            part.setSubPartida(0);
                            part.setCant(1.0d);
                            part.setMed("PZAS");
                            part.setCatalogo(registro);
                            part.setFacturado(false);
                            part.setOrdenByIdOrden(ord);
                            part.setTipo(op);
                            part.setDm(-1);
                            part.setCam(-1);
                            part.setRepMin(-1);
                            part.setRepMed(-1);
                            part.setRepMax(-1);
                            part.setPint(-1);
                            part.setCantidadAut(1);
                            part.setIntCamb(-1);
                            part.setIntDesm(-1);
                            part.setIntPinMax(-1);
                            part.setIntPinMed(-1);
                            part.setIntPinMin(-1);
                            part.setIntRepMax(-1);
                            part.setIntRepMed(-1);
                            part.setIntRepMin(-1);
                            part.setPcp(0.0d);
                            part.setCantPcp(0.0d);
                            part.setIncluida(false);
                            part.setOp(false);
                            part.setD(0.0);
                            part.setR(0.0);
                            part.setM(0.0);
                            part.setOriCon("-");
                            session.saveOrUpdate(part);
                            session.beginTransaction().commit();
                        }
                        else
                        {
                            int id=1;
                            Orden ord = (Orden)session.get(Orden.class, Integer.parseInt(orden)); 
                            Partida part=new Partida();
                            part.setIdEvaluacion(id);
                            part.setSubPartida(0);
                            part.setCant(1.0d);
                            part.setMed("PZAS");
                            part.setCatalogo(registro);
                            part.setFacturado(false);
                            part.setOrdenByIdOrden(ord);
                            part.setTipo(op);
                            part.setDm(-1);
                            part.setCam(-1);
                            part.setRepMin(-1);
                            part.setRepMed(-1);
                            part.setRepMax(-1);
                            part.setPint(-1);
                            part.setCantidadAut(1);
                            part.setIntCamb(-1);
                            part.setIntDesm(-1);
                            part.setIntPinMax(-1);
                            part.setIntPinMed(-1);
                            part.setIntPinMin(-1);
                            part.setIntRepMax(-1);
                            part.setIntRepMed(-1);
                            part.setIntRepMin(-1);
                            part.setPcp(0.0d);
                            part.setCantPcp(0.0d);
                            part.setIncluida(false);
                            part.setOp(false);
                            part.setD(0.0);
                            part.setR(0.0);
                            part.setM(0.0);
                            part.setOriCon("-");
                            session.saveOrUpdate(part);
                            session.beginTransaction().commit();
                        }
                    }
                    else
                    {
                        JOptionPane.showMessageDialog(null, "No se puede agregar partidas duplicadas!");
                        t_datos.setRowSelectionInterval(r, r);
                    }
                }
            }
            catch(Exception e)
            {
                e.printStackTrace();
                session.getTransaction().rollback();
            }
            if(session!=null)
                if(session.isOpen())
                {
                    session.flush();
                    session.clear();
                    session.close(); 
                }
            buscaCuentas(-1,-1);
        }
        else
        {
            if(t_datos.getSelectedRow()!=-1)
            {
                agregaPartida obj = new agregaPartida(new javax.swing.JFrame(), true, orden, this.user, this.sessionPrograma);
                Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
                obj.setLocation((d.width/2)-(obj.getWidth()/2), (d.height/2)-(obj.getHeight()/2));
                obj.setVisible(true);
                Session session = HibernateUtil.getSessionFactory().openSession();
                try
                {
                    session.beginTransaction().begin();
                    Catalogo registro=obj.getReturnStatus();
                    if(registro!=null)
                    {
                        int r=busca(registro.getIdCatalogo(), op);
                        if(r==-1)
                        {
                            Partida resp=(Partida) session.createCriteria(Partida.class).add(Restrictions.eq("ordenByIdOrden.idOrden", Integer.parseInt(orden))).add(Restrictions.eq("idEvaluacion", Integer.parseInt(t_datos.getValueAt(t_datos.getSelectedRow(), 0).toString()))).addOrder(Order.desc("subPartida")).setMaxResults(1).uniqueResult();
                            if(resp!=null)
                            {
                                int id=resp.getSubPartida()+1;
                                ord = (Orden)session.get(Orden.class, Integer.parseInt(orden)); 
                                    Partida part=new Partida();
                                    part.setIdEvaluacion(resp.getIdEvaluacion());
                                    part.setSubPartida(id);
                                    part.setCant(1.0d);
                                    part.setMed("PZAS");
                                    part.setCatalogo(registro);
                                    part.setFacturado(false);
                                    part.setOrdenByIdOrden(ord);
                                    part.setTipo(op);
                                    part.setDm(-1);
                                    part.setCam(-1);
                                    part.setRepMin(-1);
                                    part.setRepMed(-1);
                                    part.setRepMax(-1);
                                    part.setPint(-1);
                                    part.setCantidadAut(1);
                                    part.setIntCamb(-1);
                                    part.setIntDesm(-1);
                                    part.setIntPinMax(-1);
                                    part.setIntPinMed(-1);
                                    part.setIntPinMin(-1);
                                    part.setIntRepMax(-1);
                                    part.setIntRepMed(-1);
                                    part.setIntRepMin(-1);
                                    part.setPcp(0.0d);
                                    part.setCantPcp(0.0d);
                                    part.setIncluida(false);
                                    part.setOp(false);
                                    part.setD(0.0);
                                    part.setR(0.0);
                                    part.setM(0.0);
                                    part.setOriCon("-");
                                    session.saveOrUpdate(part);
                                    session.beginTransaction().commit();
                            }
                        }
                        else
                        {
                            JOptionPane.showMessageDialog(null, "No se puede agregar partidas duplicadas!");
                            t_datos.setRowSelectionInterval(r, r);
                        }
                    }
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                    session.getTransaction().rollback();
                }
                if(session!=null)
                    if(session.isOpen())
                    {
                        session.flush();
                        session.clear();
                        session.close(); 
                    }
                buscaCuentas(-1,-1);
            }
            else
            {
                JOptionPane.showMessageDialog(null, "Selecciona un registro apartir del cual quiere incertar una sub-partida");
            }
        }
    }
    
    public int busca(int articulo, String op)
    {
        int x=-1;
        for(int ren=0; ren<t_datos.getRowCount(); ren++)
        {
            if(Integer.parseInt(t_datos.getValueAt(ren, 15).toString())==articulo && t_datos.getValueAt(ren, 33).toString().compareTo(op)==0)
                x=ren;
        }
        return x;
    }
    

    private void buscaCuentas(int x, int y)
    {
        if(orden!=null)
        {
            Session session = HibernateUtil.getSessionFactory().openSession();
            try
            {
                Orden ord = (Orden)session.get(Orden.class, Integer.parseInt(orden));
                Query query = session.createSQLQuery("select id_evaluacion, sub_partida, esp_hoj, esp_mec, esp_sus, esp_ele, if(dm=-1, 'false', 'true')as desmontar, if(cam=-1, 'false', 'true') as cambiar, if(rep_min=-1, 'false', 'true') as r_min, \n" +
"if(rep_med=-1, 'false', 'true')as r_med,  if(rep_max=-1, 'false', 'true') as r_max, if(pint=-1, 'false', 'true') as pintura, cant, med, catalogo.nombre as miNombre, partida.id_catalogo, \n" +
"id_Parte, incluida, ori, nal, desm, pd, tot, if(int_desm=-1, 'false', 'true') as i_desm, if(Int_camb=-1, 'false', 'true') as i_cam, if(Int_rep_min=-1, 'false', 'true') as i_r_min, \n" +
"if(Int_rep_med=-1, 'false', 'true') as i_r_med, if(Int_rep_max=-1, 'false', 'true') as i_r_max, if(int_pin_min=-1, 'false', 'true') as i_p_min, if(int_pin_med=-1, 'false', 'true') as i_p_med, \n" +
"if(int_pin_max=-1, 'false', 'true')as i_p_max, if(Instruccion is null, '', Instruccion) as instruccion_f,  tipo, if(enlazada is null, '', enlazada)as freeze, autorizado_valuacion \n" +
"from partida inner join catalogo on partida.id_catalogo=catalogo.id_catalogo where partida.id_orden="+orden+" order by id_evaluacion, sub_partida asc;");  
                query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
                ArrayList partidas=(ArrayList)query.list();
                Partida[] enlazadas = (Partida[])session.createCriteria(Partida.class).add(Restrictions.eq("ordenByEnlazada.idOrden", ord.getIdOrden())).addOrder(Order.asc("idEvaluacion")).addOrder(Order.asc("subPartida")).list().toArray(new Partida[0]);                
                if(partidas.size()>0 || enlazadas.length>0)
                {
                    model=new MyModel(partidas.size()+enlazadas.length, columnas);
                    t_datos.setModel(model);
                    for(int i=0; i<partidas.size(); i++)
                    {
                        java.util.HashMap map=(java.util.HashMap)partidas.get(i);
                        model.setValueAt(map.get("id_evaluacion"), i, 0);
                        model.setCeldaEditable(i, 0, false);
                        model.setValueAt(map.get("sub_partida"), i, 1);
                        model.setCeldaEditable(i, 1, false);
                        model.setValueAt(map.get("esp_hoj"), i, 2);
                        model.setValueAt(map.get("esp_mec"), i, 3);
                        model.setValueAt(map.get("esp_sus"), i, 4);
                        model.setValueAt(map.get("esp_ele"), i, 5);
                        model.setValueAt(map.get("desmontar").toString().contentEquals("true"), i, 6);
                        model.setValueAt(map.get("cambiar").toString().contentEquals("true"), i, 7);
                        model.setValueAt(map.get("r_min").toString().contentEquals("true"), i, 8);
                        model.setValueAt(map.get("r_med").toString().contentEquals("true"), i, 9);
                        model.setValueAt(map.get("r_max").toString().contentEquals("true"), i, 10);
                        model.setValueAt(map.get("pintura").toString().contentEquals("true"), i, 11);
                        
                        model.setValueAt(map.get("cant"), i, 12);
                        model.setValueAt(map.get("med"), i, 13);
                        model.setValueAt(map.get("miNombre"), i, 14);
                        model.setCeldaEditable(i, 14, false);
                        model.setValueAt(map.get("id_catalogo"), i, 15);
                        model.setCeldaEditable(i, 15, false);
                        model.setValueAt(map.get("id_parte"), i, 16);
                        model.setValueAt(map.get("incluida"), i, 17);
                        model.setValueAt(map.get("ori"), i, 18);
                        model.setValueAt(map.get("nal"), i, 19);
                        model.setValueAt(map.get("desm"), i, 20);
                        model.setValueAt(map.get("pd"), i, 21);
                        
                        model.setValueAt(map.get("tot"), i, 22);
                        model.setCeldaEditable(i, 22, true);
                        
                        model.setValueAt(map.get("i_desm").toString().contentEquals("true"), i, 23);
                        model.setValueAt(map.get("i_cam").toString().contentEquals("true"), i, 24);
                        model.setValueAt(map.get("i_r_min").toString().contentEquals("true"), i, 25);
                        model.setValueAt(map.get("i_r_med").toString().contentEquals("true"), i, 26);
                        model.setValueAt(map.get("i_r_max").toString().contentEquals("true"), i, 27);
                        model.setValueAt(map.get("i_p_min").toString().contentEquals("true"), i, 28);
                        model.setValueAt(map.get("i_p_med").toString().contentEquals("true"), i, 29);
                        model.setValueAt(map.get("i_p_max").toString().contentEquals("true"), i, 30);
                        
                        model.setValueAt(map.get("instruccion_f"), i, 31);
                        
                        model.setValueAt(map.get("tipo"), i, 32);
                        model.setCeldaEditable(i, 32, false);
                        
                        model.setValueAt(map.get("freeze"), i, 33);
                        model.setCeldaEditable(i, 33, false);
                        model.setValueAt(map.get("autorizado_valuacion"), i, 34);
                    }
                    
                    //**********cargamos las enlazadas
                    //cuentas.length+enlazadas.length
                    for(int i=0; i<enlazadas.length; i++)
                    {
                        model.setValueAt(enlazadas[i].getIdEvaluacion(), partidas.size()+i, 0);
                        model.setCeldaEditable(partidas.size()+i, 0, false);
                        model.setValueAt(enlazadas[i].getSubPartida(), partidas.size()+i, 1);
                        model.setCeldaEditable(partidas.size()+i, 1, false);
                        model.setValueAt(enlazadas[i].isEspHoj(), partidas.size()+i, 2);
                        model.setCeldaEditable(partidas.size()+i, 2, false);
                        model.setValueAt(enlazadas[i].isEspMec(), partidas.size()+i, 3);
                        model.setCeldaEditable(partidas.size()+i, 3, false);
                        model.setValueAt(enlazadas[i].isEspSus(), partidas.size()+i, 4);
                        model.setCeldaEditable(partidas.size()+i, 4, false);
                        model.setValueAt(enlazadas[i].isEspEle(), partidas.size()+i, 5);
                        model.setCeldaEditable(partidas.size()+i, 5, false);
                        if(enlazadas[i].getDm()==-1)
                            model.setValueAt(false, partidas.size()+i, 6);
                        else
                            model.setValueAt(true, partidas.size()+i, 6);
                        model.setCeldaEditable(partidas.size()+i, 6, false);
                        
                        if(enlazadas[i].getCam()==-1)
                            model.setValueAt(false, partidas.size()+i, 7);
                        else
                            model.setValueAt(true, partidas.size()+i, 7);
                        model.setCeldaEditable(partidas.size()+i, 7, false);
                        
                        if(enlazadas[i].getRepMin()==-1)
                            model.setValueAt(false, partidas.size()+i, 8);
                        else
                            model.setValueAt(true, partidas.size()+i, 8);
                        model.setCeldaEditable(partidas.size()+i, 8, false);
                        
                        if(enlazadas[i].getRepMed()==-1)
                            model.setValueAt(false, partidas.size()+i, 9);
                        else
                            model.setValueAt(true, partidas.size()+i, 9);
                        model.setCeldaEditable(partidas.size()+i, 9, false);
                        
                        if(enlazadas[i].getRepMax()==-1)
                            model.setValueAt(false, partidas.size()+i, 10);
                        else
                            model.setValueAt(true, partidas.size()+i, 10);
                        model.setCeldaEditable(partidas.size()+i, 10, false);
                        
                        if(enlazadas[i].getPint()==-1)
                            model.setValueAt(false, partidas.size()+i, 11);
                        else
                            model.setValueAt(true, partidas.size()+i, 11);
                        model.setCeldaEditable(partidas.size()+i, 11, false);
                        
                        model.setValueAt(enlazadas[i].getCant(), partidas.size()+i, 12);
                        model.setCeldaEditable(partidas.size()+i, 12, false);
                        
                        model.setValueAt(enlazadas[i].getMed(), partidas.size()+i, 13);
                        model.setCeldaEditable(partidas.size()+i,13, false);
                        
                        model.setValueAt(enlazadas[i].getCatalogo().getNombre(), partidas.size()+i, 14);
                        model.setCeldaEditable(partidas.size()+i, 14, false);
                        
                        model.setValueAt(enlazadas[i].getCatalogo().getIdCatalogo(), partidas.size()+i, 15);
                        model.setCeldaEditable(partidas.size()+i, 15, false);
                        
                        try{
                        model.setValueAt(enlazadas[i].getEjemplar().getIdParte(), partidas.size()+i, 16);
                        }catch(Exception e){model.setValueAt("", partidas.size()+i, 16);}
                        model.setCeldaEditable(partidas.size()+i, 16, false);
                        
                        //model.setValueAt(enlazadas[i].isIncluida(), i, 17);
                        model.setValueAt(false, partidas.size()+i, 17);
                        model.setCeldaEditable(partidas.size()+i, 17, false);
                        
                        model.setValueAt(enlazadas[i].isOri(), partidas.size()+i, 18);
                        model.setCeldaEditable(partidas.size()+i, 18, false);
                        
                        model.setValueAt(enlazadas[i].isNal(), partidas.size()+i, 19);
                        model.setCeldaEditable(partidas.size()+i, 19, false);
                        
                        model.setValueAt(enlazadas[i].isDesm(), partidas.size()+i, 20);
                        model.setCeldaEditable(partidas.size()+i, 20, false);
                        
                        model.setValueAt(enlazadas[i].isPd(), partidas.size()+i, 21);
                        model.setCeldaEditable(partidas.size()+i, 21, false);
                        
                        if(enlazadas[i].getProveedor()!=null)
                            model.setValueAt(enlazadas[i].getProveedor().getIdProveedor(), partidas.size()+i, 22);
                        else
                            model.setValueAt("", partidas.size()+i, 22);
                        model.setCeldaEditable(partidas.size()+i, 22, false);
                        
                        if(enlazadas[i].getIntDesm()==-1)
                            model.setValueAt(false, partidas.size()+i, 23);
                        else
                            model.setValueAt(true, partidas.size()+i, 23);
                        model.setCeldaEditable(partidas.size()+i, 23, false);
                        
                        if(enlazadas[i].getIntCamb()==-1)
                            model.setValueAt(false, partidas.size()+i, 24);
                        else
                            model.setValueAt(true, partidas.size()+i, 24);
                        model.setCeldaEditable(partidas.size()+i, 24, false);
                        
                        if(enlazadas[i].getIntRepMin()==-1)
                            model.setValueAt(false, partidas.size()+i, 25);
                        else
                            model.setValueAt(true, partidas.size()+i, 25);
                        model.setCeldaEditable(partidas.size()+i, 25, false);
                        
                        if(enlazadas[i].getIntRepMed()==-1)
                            model.setValueAt(false, partidas.size()+i, 26);
                        else
                            model.setValueAt(true, partidas.size()+i, 26);
                        model.setCeldaEditable(partidas.size()+i, 26, false);
                        
                        if(enlazadas[i].getIntRepMax()==-1)
                            model.setValueAt(false, partidas.size()+i, 27);
                        else
                            model.setValueAt(true, partidas.size()+i, 27);
                        model.setCeldaEditable(partidas.size()+i, 27, false);
                        
                        if(enlazadas[i].getIntPinMin()==-1)
                            model.setValueAt(false, partidas.size()+i, 28);
                        else
                            model.setValueAt(true, partidas.size()+i, 28);
                        model.setCeldaEditable(partidas.size()+i, 28, false);
                        
                        if(enlazadas[i].getIntPinMed()==-1)
                            model.setValueAt(false, partidas.size()+i, 29);
                        else
                            model.setValueAt(true, partidas.size()+i, 29);
                        model.setCeldaEditable(partidas.size()+i, 29, false);
                        
                        if(enlazadas[i].getIntPinMax()==-1)
                            model.setValueAt(false, partidas.size()+i, 30);
                        else
                            model.setValueAt(true, partidas.size()+i, 30);
                        model.setCeldaEditable(partidas.size()+i, 30, false);
                        
                        if(enlazadas[i].getInstruccion()!=null)
                            model.setValueAt(enlazadas[i].getInstruccion(), partidas.size()+i, 31);
                        else
                            model.setValueAt("", partidas.size()+i, 31);
                        model.setCeldaEditable(partidas.size()+i, 31, false);
                        
                        //model.setValueAt(enlazadas[i].getTipo(), cuentas.length+i, 33);
                        model.setValueAt("e", partidas.size()+i, 32);
                        model.setCeldaEditable(partidas.size()+i, 32, false);
                        
                        if(enlazadas[i].getOrdenByEnlazada()!=null)
                            model.setValueAt(enlazadas[i].getOrdenByIdOrden().getIdOrden(), partidas.size()+i, 33);
                        else
                            model.setValueAt("", partidas.size()+i, 33);
                        model.setCeldaEditable(partidas.size()+i, 33, false);
                        
                        model.setValueAt(enlazadas[i].isAutorizadoValuacion(), partidas.size()+i, 34);
                        model.setCeldaEditable(partidas.size()+i, 34, false);
                    }
                }
                else
                {
                    model=new MyModel(0, columnas);
                    t_datos.setModel(model);    
                }
                session.beginTransaction().rollback();
            }catch(Exception e)
            {
                e.printStackTrace();
            }
            if(session!=null)
                if(session.isOpen()==true)
                {
                    session.flush();
                    session.clear();
                    session.close();
                }
        }
        else
        {
            model=new MyModel(0, columnas);
            t_datos.setModel(model);
        }
        formatoTabla();
        if(x>=0 && y>=0)
        {
            t_datos.setColumnSelectionInterval(y, y);
            t_datos.setRowSelectionInterval(x, x);
        }
    }
    
    public void formatoTabla()
    {
        TableCellRenderer textVertical = new VerticalTableHeaderCellRenderer();
        TableCellRenderer textNormal = new DefaultTableHeaderCellRenderer();
        Enumeration columns = t_datos.getColumnModel().getColumns();
        
        
        for(int x=0; x<t_datos.getColumnModel().getColumnCount(); x++)
        {
            if((x>1 && x<12) || (x>16 && x<31) || x==32 || x==34)
                t_datos.getColumnModel().getColumn(x).setHeaderRenderer(textVertical);
            else
                t_datos.getColumnModel().getColumn(x).setHeaderRenderer(textNormal);
        } 
        tabla_tamaños();
        //t_datos.setShowVerticalLines(true);
        t_datos.setShowHorizontalLines(true);
        t_datos.setDefaultRenderer(String.class, formato); 
        t_datos.setDefaultRenderer(Boolean.class, formato); 
        t_datos.setDefaultRenderer(Double.class, formato); 
    }
    
    public void visualiza(boolean partida, boolean sub_partida, boolean tipo, boolean menos, boolean t_busca, boolean b_busca, boolean filtro, boolean pdf, boolean exel, boolean valor, boolean cerrar, boolean tabla)
    {
        this.b_mas_partida.setEnabled(partida);
        this.b_mas_sup_partida.setEnabled(sub_partida);
        this.cb_tipo_partida.setEnabled(tipo);
        this.b_menos.setEnabled(menos);
        
        //this.t_busca.setEnabled(t_busca);
        //this.b_busca.setEnabled(b_busca);
        
        //this.c_filtro.setEnabled(filtro);
        //this.b_pdf.setEnabled(pdf);
        //this.b_exel.setEnabled(exel);
        
        this.b_tot.setEnabled(valor);
        this.r_cerrar.setEnabled(cerrar);
        //this.t_datos.setEnabled(tabla);
        if(valor==false)
        {
            for(int x=0; x<t_datos.getColumnCount(); x++)
            {
                model.setColumnaEditable(x, valor);
            }
        }
        this.c_servicio.setEnabled(tabla);
        //this.b_valuacion.setEnabled(valor);
        //this.b_elimina.setEnabled(valor);
    }
    
    public void cabecera(PDF reporte, BaseFont bf, PdfPTable tabla)
    {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try
        {
            reporte.contenido.setLineWidth(0.5f);
            reporte.contenido.setColorStroke(new GrayColor(0.2f));
            reporte.contenido.setColorFill(new GrayColor(0.9f));
            reporte.contenido.roundRectangle(160, 515, 210, 45, 5);
            reporte.contenido.roundRectangle(380, 515, 375, 45, 5);
            reporte.contenido.roundRectangle(35, 490, 720, 20, 5);


            reporte.inicioTexto();
            reporte.contenido.setFontAndSize(bf, 14);
            reporte.contenido.setColorFill(BaseColor.BLACK);
            Configuracion con= (Configuracion)session.get(Configuracion.class, 1);
            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, con.getEmpresa(), 160, 580, 0);
            reporte.contenido.setFontAndSize(bf, 8);
            reporte.contenido.setColorFill(BaseColor.BLACK);
            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Autorizacion de Operaciones ("+this.c_filtro.getSelectedItem().toString()+")", 160, 570, 0);
            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_RIGHT, "Fecha:"+new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date()), 760, 580, 0);

                    ord = (Orden)session.get(Orden.class, Integer.parseInt(orden)); 
                    Foto foto = (Foto)session.createCriteria(Foto.class).add(Restrictions.eq("orden.idOrden", Integer.parseInt(orden))).addOrder(Order.desc("fecha")).setMaxResults(1).uniqueResult();
                    if(foto!=null)
                        reporte.agregaObjeto(reporte.crearImagen("ordenes/"+ord.getIdOrden()+"/"+foto.getDescripcion(), 0, -60, 120, 80, 0));
                    else{}
                    //************************datos de la orden****************************
                    reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Orden:"+ord.getIdOrden(), 164, 550, 0);

                    if(ord.getFecha()!=null)
                        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Apertura:"+ord.getFecha(), 285, 550, 0);
                    else
                        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Apertura:", 285, 550, 0);

                    reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Compañia:"+ord.getCompania().getIdCompania()+" "+ord.getCompania().getNombre(), 164, 540, 0);

                    if(ord.getSiniestro()!=null)
                        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Siniestro:"+ord.getSiniestro(), 164, 530, 0);
                    else
                        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Siniestro:", 164, 530, 0);

                    if(ord.getFechaSiniestro()!=null)
                        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "F. Siniestro:"+ord.getFechaSiniestro(), 285, 530, 0);
                    else
                        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "F.Siniestro:", 285, 530, 0);

                    if(ord.getPoliza()!=null)
                        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Poliza:"+ord.getPoliza(), 164, 520, 0);
                    else
                        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Poliza:", 164, 520, 0);

                    if(ord.getInciso()!=null)
                        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Inciso:"+ord.getInciso(), 285, 520, 0);
                    else
                        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Inciso:", 285, 520, 0);
                    //**********************************************************

                    //************datos de la unidad
                    reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Unidad:"+ord.getTipo().getTipoNombre(), 385, 550, 0);
                    reporte.contenido.showTextAligned(PdfContentByte.ALIGN_RIGHT, "Modelo:"+ord.getModelo(), 664, 550, 0);

                    reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Marca:"+ord.getMarca().getMarcaNombre(), 385, 540, 0);
                    if(ord.getNoEconomico()!=null)
                        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_RIGHT, "Economico:"+ord.getNoEconomico(), 664, 540, 0);
                    else
                        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_RIGHT, "Economico:", 664, 540, 0);

                    if(ord.getNoMotor()!=null)
                        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "N° Motor:"+ord.getNoMotor(), 385, 530, 0);
                    else
                        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "N° Motor:", 385, 530, 0);

                    if(ord.getNoSerie()!=null)
                        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "N° Serie:"+ord.getNoSerie(), 385, 520, 0);
                    else
                        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "N° Serie:", 385, 520, 0);
                    //*************************************************************

                    //****************Datos del valuador
                    reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Valuador:", 40, 495, 0);
                    if(ord.getEmpleadoByRLevantamiento()!=null)
                        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, ord.getEmpleadoByRLevantamiento().getNombre(), 75, 495, 0);
                    if(ord.getFechaTaller()!=null)
                        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Fecha Entrega:"+ord.getFechaTaller(), 320, 495, 0);
                    else
                        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Fecha Entrega:  //", 320, 495, 0);
                    if(ord.getFechaCierre()!=null)
                        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Cerrado:"+ord.getFechaCierre(), 500, 495, 0);
                    else
                        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Cerrado:  //", 500, 495, 0);

                reporte.finTexto();
                //agregamos renglones vacios para dejar un espacio
                reporte.agregaObjeto(new Paragraph(" "));
                reporte.agregaObjeto(new Paragraph(" "));
                reporte.agregaObjeto(new Paragraph(" "));
                reporte.agregaObjeto(new Paragraph(" "));
                reporte.agregaObjeto(new Paragraph(" "));

                Font font = new Font(Font.FontFamily.HELVETICA, 6, Font.BOLD);

                BaseColor cabecera=BaseColor.GRAY;
                BaseColor contenido=BaseColor.WHITE;
                int centro=Element.ALIGN_CENTER;
                int izquierda=Element.ALIGN_LEFT;
                int derecha=Element.ALIGN_RIGHT;

                tabla.addCell(reporte.celda("Cant", font, cabecera, centro, 0, 3, Rectangle.RECTANGLE));
                tabla.addCell(reporte.celda("Med", font, cabecera, centro, 0, 3,Rectangle.RECTANGLE));
                tabla.addCell(reporte.celda("Especialidad", font, cabecera, centro, 5, 2, Rectangle.RECTANGLE));
                tabla.addCell(reporte.celda("Grupo", font, cabecera, centro, 0,3, Rectangle.RECTANGLE));
                tabla.addCell(reporte.celda("Descripción", font, cabecera, centro, 0, 3,Rectangle.RECTANGLE));
                tabla.addCell(reporte.celda("Origen Refacción", font, cabecera, centro, 5,1,Rectangle.RECTANGLE));
                tabla.addCell(reporte.celda("Instrucción Final", font, cabecera, centro, 8,1, Rectangle.RECTANGLE));
                tabla.addCell(reporte.celda("Descripcion", font, cabecera, centro, 0, 3, Rectangle.RECTANGLE));

                tabla.addCell(reporte.celda("Ori", font, cabecera, centro, 0,2, Rectangle.RECTANGLE));
                tabla.addCell(reporte.celda("Nal", font, cabecera, centro, 0,2, Rectangle.RECTANGLE));
                tabla.addCell(reporte.celda("Des", font, cabecera, centro, 0,2, Rectangle.RECTANGLE));
                tabla.addCell(reporte.celda("Rec", font, cabecera, centro, 0,2, Rectangle.RECTANGLE));
                tabla.addCell(reporte.celda("TOT", font, cabecera, centro, 0,2, Rectangle.RECTANGLE));

                tabla.addCell(reporte.celda("D/M", font, cabecera, centro, 0,2, Rectangle.RECTANGLE));
                tabla.addCell(reporte.celda("Cam", font, cabecera, centro, 0,2, Rectangle.RECTANGLE));

                tabla.addCell(reporte.celda("Reparar", font, cabecera, centro, 3,1, Rectangle.RECTANGLE));
                tabla.addCell(reporte.celda("Pintar", font, cabecera, centro, 3,1, Rectangle.RECTANGLE));

                tabla.addCell(reporte.celda("H", font, cabecera, centro, 0, 1, Rectangle.RECTANGLE));
                tabla.addCell(reporte.celda("M", font, cabecera, centro, 0, 1, Rectangle.RECTANGLE));
                tabla.addCell(reporte.celda("S", font, cabecera, centro, 0, 1, Rectangle.RECTANGLE));
                tabla.addCell(reporte.celda("E", font, cabecera, centro, 0, 1, Rectangle.RECTANGLE));
                tabla.addCell(reporte.celda("P", font, cabecera, centro, 0, 1, Rectangle.RECTANGLE));
            
                tabla.addCell(reporte.celda("Min", font, cabecera, centro, 0,1, Rectangle.RECTANGLE));
                tabla.addCell(reporte.celda("Med", font, cabecera, centro, 0,1, Rectangle.RECTANGLE));
                tabla.addCell(reporte.celda("Max", font, cabecera, centro, 0,1, Rectangle.RECTANGLE));

                tabla.addCell(reporte.celda("Min", font, cabecera, centro, 0,1, Rectangle.RECTANGLE));
                tabla.addCell(reporte.celda("Med", font, cabecera, centro, 0,1, Rectangle.RECTANGLE));
                tabla.addCell(reporte.celda("Max", font, cabecera, centro, 0,1, Rectangle.RECTANGLE));
        }catch(Exception e)
        {
            System.out.println(e);
        }
        if(session!=null)
            if(session.isOpen())
            {
                session.flush();
                session.clear();
                session.close();
            }
    }
    
    public void validaCerrado()
    {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try
        {
              ord = (Orden)session.get(Orden.class, Integer.parseInt(orden));
             if(ord.getRLevantamientoCierre()==null)
             {
                  r_cerrar.setSelected(false);
                  cb_tipo_partida.removeAllItems();
                  cb_tipo_partida.addItem("Ordinaria");
                  cb_tipo_partida.addItem("Enlazada");
             }
             else
             {
                  r_cerrar.setSelected(true);
                  cb_tipo_partida.removeAllItems();
                  cb_tipo_partida.addItem("Complementaria");
                  cb_tipo_partida.addItem("Adicional");
                  cb_tipo_partida.addItem("Enlazada");
             }
        }catch(Exception e)
        {
             e.printStackTrace();
        }
        if(session!=null)
             if(session.isOpen()==true)
             {
                 session.flush();
                 session.clear();
                 session.close();
             }
    }
    
    public void nuevaEnlazada()
    {
        agregaEnlazada obj = new agregaEnlazada(new javax.swing.JFrame(), true, orden);
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        obj.setLocation((d.width/2)-(obj.getWidth()/2), (d.height/2)-(obj.getHeight()/2));
        obj.setVisible(true);
        Session session = HibernateUtil.getSessionFactory().openSession();
        try
        {
            session.beginTransaction().begin();
            Partida registro=obj.getReturnStatus();
            if(registro!=null)
            {
                h=new Herramientas(user, 0);
                h.session(sessionPrograma);
                Partida resp=(Partida) session.createCriteria(Partida.class).add(Restrictions.eq("idPartida", registro.getIdPartida())).setMaxResults(1).uniqueResult();
                if(resp!=null)
                {
                    ord = (Orden)session.get(Orden.class, Integer.parseInt(orden));
                    resp.setOrdenByEnlazada(ord);
                    ord.getPartidasForEnlazada().add(resp);
                    session.update(resp);
                    session.update(ord);
                    session.beginTransaction().commit();
                    session.persist(ord);
                    session.flush();
                    session.clear();
                    session.close();
                    buscaCuentas(-1,-1);
                }
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
            session.beginTransaction().rollback();
        }
        if(session!=null)
            if(session.isOpen())
            {
                session.flush();
                session.clear();
                session.close(); 
            }
    }
    
    public void permisos_tabla()
    {
        DefaultTableModel mod= (DefaultTableModel)t_datos.getModel();
    }
    
    public class MyModel extends DefaultTableModel
    {
        Class[] types = new Class [] {
                    java.lang.String.class, 
                    java.lang.String.class, 
                    java.lang.Boolean.class,
                    java.lang.Boolean.class,
                    java.lang.Boolean.class,
                    java.lang.Boolean.class,
                    java.lang.Boolean.class,
                    java.lang.Boolean.class,
                    java.lang.Boolean.class,
                    java.lang.Boolean.class,
                    java.lang.Boolean.class,
                    java.lang.Boolean.class,
                    java.lang.Double.class, 
                    java.lang.String.class, 
                    //datos del articulo
                    java.lang.String.class, 
                    java.lang.String.class, 
                    java.lang.String.class, 
                    //freeze
                    java.lang.Boolean.class,
                    
                    java.lang.Boolean.class,
                    java.lang.Boolean.class,
                    java.lang.Boolean.class,
                    java.lang.Boolean.class,
                    //tot trabajo en otro taller
                    java.lang.String.class, 
                    //intrucciones max med min
                    java.lang.Boolean.class,
                    java.lang.Boolean.class,
                    java.lang.Boolean.class,
                    java.lang.Boolean.class,
                    java.lang.Boolean.class,
                    java.lang.Boolean.class,
                    java.lang.Boolean.class,
                    java.lang.Boolean.class,
                    
                    java.lang.String.class,
                    java.lang.String.class,
                    java.lang.String.class,
                    java.lang.Boolean.class
                };
        int ren=0;
        int col=0;
        private boolean[][] celdaEditable;
        
        public MyModel(int renglones, String columnas[])
        {
            ren=renglones;
            col=columnas.length;
            celdaEditable=new boolean[types.length][renglones];
            for(int x=0; x<types.length; x++)
            {
                for(int y=0; y<renglones; y++)
                {
                    celdaEditable[x][y]=true;
                }
            }
            this.setDataVector(new Object [renglones][columnas.length], columnas);
        }
        
        @Override
        public int getRowCount() {
            return ren;
        }

        @Override
        public int getColumnCount() {
            return col;
        }
        
        @Override
        public void setValueAt(Object value, int row, int col)
        {
                        Vector vector = (Vector)this.dataVector.elementAt(row);
                        Object celda = ((Vector)this.dataVector.elementAt(row)).elementAt(col);
                        String consulta="";
                        switch(col)
                        {
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
                                            user = (Usuario)session.get(Usuario.class, user.getIdUsuario());
                                            //if(user.getEditarLevantamiento()==true || (r_cerrar.isSelected()==false && (boolean)t_datos.getValueAt(t_datos.getSelectedRow(), 34)==false) || (r_cerrar.isSelected()==true && ((String)t_datos.getValueAt(t_datos.getSelectedRow(), 32)).compareToIgnoreCase("o")!=0))
                                            if(user.getEditarLevantamiento()==true || (((String)t_datos.getValueAt(t_datos.getSelectedRow(), 32)).compareToIgnoreCase("o")==0 && user.getAValuacionLevantamiento()==true && r_cerrar.isSelected()==false) || (((String)t_datos.getValueAt(t_datos.getSelectedRow(), 32)).compareToIgnoreCase("o")!=0 && user.getAValuacionLevantamiento()==true) )
                                            {
                                                if(t_datos.getValueAt(row, 32).toString().compareTo("e")!=0)
                                                {
                                                    Partida part=(Partida) session.createCriteria(Partida.class).add(Restrictions.eq("ordenByIdOrden.idOrden", Integer.parseInt(orden))).add(Restrictions.eq("idEvaluacion", Integer.parseInt(t_datos.getValueAt(t_datos.getSelectedRow(), 0).toString()))).add(Restrictions.eq("subPartida", Integer.parseInt(t_datos.getValueAt(t_datos.getSelectedRow(), 1).toString()))).setMaxResults(1).uniqueResult();
                                                    if(part!=null)
                                                    {
                                                        if(value.toString().compareTo("true")==0)
                                                            part.setEspHoj(true);
                                                        else
                                                            part.setEspHoj(false);
                                                        session.update(part);
                                                        session.getTransaction().commit();
                                                        vector.setElementAt(value, col);
                                                        this.dataVector.setElementAt(vector, row);
                                                        fireTableCellUpdated(row, col);
                                                        if(session.isOpen()==true)
                                                        {
                                                            session.flush();
                                                            session.clear();
                                                            session.close();
                                                        }
                                                        //buscaCuentas(row,col);
                                                    }
                                                    else
                                                    {
                                                        buscaCuentas(-1,-1);
                                                        JOptionPane.showMessageDialog(null, "La partida ya no existe");
                                                    }
                                                }
                                                else
                                                {
                                                    JOptionPane.showMessageDialog(null, "No se pueden modificar partidas enlazadas de otra orden");
                                                }
                                            }
                                            else
                                            {
                                                session.getTransaction().rollback();
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
                                            session.getTransaction().rollback();
                                            System.out.println(e);
                                        }
                                        if(session!=null)
                                            if(session.isOpen()==true)
                                            {
                                                session.flush();
                                                session.clear();
                                                session.close();
                                            }
                                    }
                                    break;

                            case 3:
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
                                            user = (Usuario)session.get(Usuario.class, user.getIdUsuario());
                                            if(user.getEditarLevantamiento()==true || (((String)t_datos.getValueAt(t_datos.getSelectedRow(), 32)).compareToIgnoreCase("o")==0 && user.getAValuacionLevantamiento()==true && r_cerrar.isSelected()==false) || (((String)t_datos.getValueAt(t_datos.getSelectedRow(), 32)).compareToIgnoreCase("o")!=0 && user.getAValuacionLevantamiento()==true) )
                                            {
                                                if(t_datos.getValueAt(row, 32).toString().compareTo("e")!=0)
                                                {
                                                    Partida part=(Partida) session.createCriteria(Partida.class).add(Restrictions.eq("ordenByIdOrden.idOrden", Integer.parseInt(orden))).add(Restrictions.eq("idEvaluacion", Integer.parseInt(t_datos.getValueAt(t_datos.getSelectedRow(), 0).toString()))).add(Restrictions.eq("subPartida", Integer.parseInt(t_datos.getValueAt(t_datos.getSelectedRow(), 1).toString()))).setMaxResults(1).uniqueResult();
                                                    if(part!=null)
                                                    {
                                                        if(value.toString().compareTo("true")==0)
                                                            part.setEspMec(true);
                                                        else
                                                            part.setEspMec(false);
                                                        session.update(part);
                                                        session.getTransaction().commit();
                                                        vector.setElementAt(value, col);
                                                        this.dataVector.setElementAt(vector, row);
                                                        fireTableCellUpdated(row, col);
                                                        if(session.isOpen()==true)
                                                        {
                                                            session.flush();
                                                            session.clear();
                                                            session.close();
                                                        }
                                                        //buscaCuentas(row,col);
                                                    }
                                                    else
                                                    {
                                                        buscaCuentas(-1,-1);
                                                        JOptionPane.showMessageDialog(null, "La partida ya no existe");
                                                    }
                                                }
                                                else
                                                {
                                                   JOptionPane.showMessageDialog(null, "No se pueden modificar partidas enlazadas de otra orden");
                                                }
                                            }
                                            else
                                            {
                                                session.getTransaction().rollback();
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
                                            session.getTransaction().rollback();
                                            System.out.println(e);
                                        }
                                        if(session!=null)
                                            if(session.isOpen()==true)
                                            {
                                                session.flush();
                                                session.clear();
                                                session.close();
                                            }
                                    }
                                    break;
                                
                            case 4:
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
                                            user = (Usuario)session.get(Usuario.class, user.getIdUsuario());
                                            if(user.getEditarLevantamiento()==true || (((String)t_datos.getValueAt(t_datos.getSelectedRow(), 32)).compareToIgnoreCase("o")==0 && user.getAValuacionLevantamiento()==true && r_cerrar.isSelected()==false) || (((String)t_datos.getValueAt(t_datos.getSelectedRow(), 32)).compareToIgnoreCase("o")!=0 && user.getAValuacionLevantamiento()==true) )
                                            {
                                                if(t_datos.getValueAt(row, 32).toString().compareTo("e")!=0)
                                                {
                                                    Partida part=(Partida) session.createCriteria(Partida.class).add(Restrictions.eq("ordenByIdOrden.idOrden", Integer.parseInt(orden))).add(Restrictions.eq("idEvaluacion", Integer.parseInt(t_datos.getValueAt(t_datos.getSelectedRow(), 0).toString()))).add(Restrictions.eq("subPartida", Integer.parseInt(t_datos.getValueAt(t_datos.getSelectedRow(), 1).toString()))).setMaxResults(1).uniqueResult();
                                                    if(part!=null)
                                                    {
                                                        if(value.toString().compareTo("true")==0)
                                                            part.setEspSus(true);
                                                        else
                                                            part.setEspSus(false);
                                                        session.update(part);
                                                        session.getTransaction().commit();
                                                        vector.setElementAt(value, col);
                                                        this.dataVector.setElementAt(vector, row);
                                                        fireTableCellUpdated(row, col);
                                                        if(session.isOpen()==true)
                                                        {
                                                            session.flush();
                                                            session.clear();
                                                            session.close();
                                                        }
                                                        //buscaCuentas(row,col);
                                                    }
                                                    else
                                                    {
                                                        buscaCuentas(-1,-1);
                                                        JOptionPane.showMessageDialog(null, "La partida ya no existe");
                                                    }
                                                }
                                                else
                                                {
                                                   JOptionPane.showMessageDialog(null, "No se pueden modificar partidas enlazadas de otra orden");
                                                }
                                            }
                                            else
                                            {
                                                session.getTransaction().rollback();
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
                                            session.getTransaction().rollback();
                                            System.out.println(e);
                                        }
                                        if(session!=null)
                                            if(session.isOpen()==true)
                                            {
                                                session.flush();
                                                session.clear();
                                                session.close();
                                            }
                                    }
                                    break;
                                
                            case 5:
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
                                            user = (Usuario)session.get(Usuario.class, user.getIdUsuario());
                                            if(user.getEditarLevantamiento()==true || (((String)t_datos.getValueAt(t_datos.getSelectedRow(), 32)).compareToIgnoreCase("o")==0 && user.getAValuacionLevantamiento()==true && r_cerrar.isSelected()==false) || (((String)t_datos.getValueAt(t_datos.getSelectedRow(), 32)).compareToIgnoreCase("o")!=0 && user.getAValuacionLevantamiento()==true) )
                                            {
                                                if(t_datos.getValueAt(row, 32).toString().compareTo("e")!=0)
                                                {
                                                    Partida part=(Partida) session.createCriteria(Partida.class).add(Restrictions.eq("ordenByIdOrden.idOrden", Integer.parseInt(orden))).add(Restrictions.eq("idEvaluacion", Integer.parseInt(t_datos.getValueAt(t_datos.getSelectedRow(), 0).toString()))).add(Restrictions.eq("subPartida", Integer.parseInt(t_datos.getValueAt(t_datos.getSelectedRow(), 1).toString()))).setMaxResults(1).uniqueResult();
                                                    if(part!=null)
                                                    {
                                                        if(value.toString().compareTo("true")==0)
                                                            part.setEspEle(true);
                                                        else
                                                            part.setEspEle(false);
                                                        session.update(part);
                                                        session.getTransaction().commit();
                                                        vector.setElementAt(value, col);
                                                        this.dataVector.setElementAt(vector, row);
                                                        fireTableCellUpdated(row, col);
                                                        if(session.isOpen()==true)
                                                        {
                                                            session.flush();
                                                            session.clear();
                                                            session.close();
                                                        }
                                                    }
                                                    else
                                                    {
                                                        buscaCuentas(-1,-1);
                                                        JOptionPane.showMessageDialog(null, "La partida ya no existe");
                                                    }
                                                }
                                                else
                                                {
                                                   JOptionPane.showMessageDialog(null, "No se pueden modificar partidas enlazadas de otra orden");
                                                }
                                            }
                                            else
                                            {
                                                session.getTransaction().rollback();
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
                                            session.getTransaction().rollback();
                                            System.out.println(e);
                                        }
                                        if(session!=null)
                                            if(session.isOpen()==true)
                                            {
                                                session.flush();
                                                session.clear();
                                                session.close();
                                            }
                                    }
                                    break;
                                
                            case 6://DM
                                    if(vector.get(col)==null || habilita==true)
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
                                            user = (Usuario)session.get(Usuario.class, user.getIdUsuario());
                                            if(user.getEditarLevantamiento()==true || (((String)t_datos.getValueAt(t_datos.getSelectedRow(), 32)).compareToIgnoreCase("o")==0 && user.getAValuacionLevantamiento()==true && r_cerrar.isSelected()==false) || (((String)t_datos.getValueAt(t_datos.getSelectedRow(), 32)).compareToIgnoreCase("o")!=0 && user.getAValuacionLevantamiento()==true) )
                                            {
                                                if(t_datos.getValueAt(row, 32).toString().compareTo("e")!=0)
                                                {
                                                    Partida part=(Partida) session.createCriteria(Partida.class).add(Restrictions.eq("ordenByIdOrden.idOrden", Integer.parseInt(orden))).add(Restrictions.eq("idEvaluacion", Integer.parseInt(t_datos.getValueAt(t_datos.getSelectedRow(), 0).toString()))).add(Restrictions.eq("subPartida", Integer.parseInt(t_datos.getValueAt(t_datos.getSelectedRow(), 1).toString()))).setMaxResults(1).uniqueResult();
                                                    if(part!=null)
                                                    {
                                                        if(value.toString().compareTo("true")==0)
                                                        {
                                                            if(part.getIntCamb()==-1)
                                                            {
                                                                if(part.getPedido()==null)
                                                                {
                                                                    part.setDm(0);
                                                                    if(part.getCam()>-1 && (boolean)t_datos.getValueAt(row, 24)==true)
                                                                    {
                                                                        part.setRefComp(false);
                                                                        part.setAutorizado(false);
                                                                    }
                                                                    part.setCam(-1);
                                                                    part.setRefComp(false);
                                                                    session.update(part);
                                                                    session.getTransaction().commit();
                                                                    vector.setElementAt(value, col);
                                                                    this.dataVector.setElementAt(vector, row);
                                                                    fireTableCellUpdated(row, col);
                                                                    
                                                                    habilita=true;
                                                                    t_datos.setValueAt(false, row, 7);
                                                                    habilita=false;
                                                                    
                                                                    if(session.isOpen()==true)
                                                                    {
                                                                        session.flush();
                                                                        session.clear();
                                                                        session.close();
                                                                    }
                                                                }
                                                                else
                                                                    JOptionPane.showMessageDialog(null, "La partida ya fue pedida");
                                                            }
                                                            else
                                                                JOptionPane.showMessageDialog(null, "No puedes desmontar montar si vas a cambiar en interno");
                                                        }
                                                        else
                                                        {
                                                            part.setDm(-1);
                                                            session.update(part);
                                                            session.getTransaction().commit();
                                                            vector.setElementAt(value, col);
                                                            this.dataVector.setElementAt(vector, row);
                                                            fireTableCellUpdated(row, col);
                                                            if(session.isOpen()==true)
                                                            {
                                                                session.flush();
                                                                session.clear();
                                                                session.close();
                                                            }
                                                        }
                                                    }
                                                    else
                                                    {
                                                        buscaCuentas(-1,-1);
                                                        JOptionPane.showMessageDialog(null, "La partida ya no existe");
                                                    }
                                                }
                                                else
                                                {
                                                   JOptionPane.showMessageDialog(null, "No se pueden modificar partidas enlazadas de otra orden");
                                                }
                                            }
                                            else
                                            {
                                                session.getTransaction().rollback();
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
                                            session.getTransaction().rollback();
                                            System.out.println(e);
                                        }
                                        if(session!=null)
                                            if(session.isOpen()==true)
                                            {
                                                session.flush();
                                                session.clear();
                                                session.close();
                                            }
                                    }
                                    break;
                                
                            case 7://Cam
                                    if(vector.get(col)==null || habilita==true)
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
                                            user = (Usuario)session.get(Usuario.class, user.getIdUsuario());
                                            if(user.getEditarLevantamiento()==true || (((String)t_datos.getValueAt(t_datos.getSelectedRow(), 32)).compareToIgnoreCase("o")==0 && user.getAValuacionLevantamiento()==true && r_cerrar.isSelected()==false) || (((String)t_datos.getValueAt(t_datos.getSelectedRow(), 32)).compareToIgnoreCase("o")!=0 && user.getAValuacionLevantamiento()==true) )
                                            {
                                                if(t_datos.getValueAt(row, 32).toString().compareTo("e")!=0)
                                                {
                                                    Partida part=(Partida) session.createCriteria(Partida.class).add(Restrictions.eq("ordenByIdOrden.idOrden", Integer.parseInt(orden))).add(Restrictions.eq("idEvaluacion", Integer.parseInt(t_datos.getValueAt(t_datos.getSelectedRow(), 0).toString()))).add(Restrictions.eq("subPartida", Integer.parseInt(t_datos.getValueAt(t_datos.getSelectedRow(), 1).toString()))).setMaxResults(1).uniqueResult();
                                                    if(part!=null)
                                                    {
                                                        if(value.toString().compareTo("true")==0)
                                                        {
                                                            part.setCam(0);
                                                            part.setRefCoti(true);
                                                            part.setDm(-1);
                                                            if((boolean)t_datos.getValueAt(row, 24)==true)
                                                            {
                                                                part.setRefComp(true);
                                                                part.setAutorizado(true);
                                                            }
                                                            part.setRepMin(-1);
                                                            part.setRepMed(-1);
                                                            part.setRepMax(-1);
                                                            
                                                            session.update(part);
                                                            session.getTransaction().commit();
                                                            vector.setElementAt(value, col);
                                                            this.dataVector.setElementAt(vector, row);
                                                            fireTableCellUpdated(row, col);
                                                            
                                                            habilita=true;
                                                            t_datos.setValueAt(false, row, 6);
                                                            t_datos.setValueAt(false, row, 8);
                                                            t_datos.setValueAt(false, row, 9);
                                                            t_datos.setValueAt(false, row, 10);
                                                            habilita=false;

                                                            if(session.isOpen()==true)
                                                            {
                                                                session.flush();
                                                                session.clear();
                                                                session.close();
                                                            }
                                                        }
                                                        else
                                                        {
                                                            if(part.getPedido()==null)
                                                            {
                                                                if(part.getIntCamb()==-1)
                                                                {
                                                                    part.setCam(-1);
                                                                    part.setRefCoti(false);
                                                                    if((boolean)t_datos.getValueAt(row, 24)==true)
                                                                    {
                                                                        part.setAutorizado(false);
                                                                    }
                                                                    if((boolean)t_datos.getValueAt(row, 24)==false)
                                                                    {
                                                                        part.setRefComp(false);
                                                                        part.setAutorizado(false);
                                                                    }
                                                                    session.update(part);
                                                                    session.getTransaction().commit();
                                                                    vector.setElementAt(value, col);
                                                                    this.dataVector.setElementAt(vector, row);
                                                                    fireTableCellUpdated(row, col);
                                                                    if(session.isOpen()==true)
                                                                    {
                                                                        session.flush();
                                                                        session.clear();
                                                                        session.close();
                                                                    }
                                                                }
                                                                else
                                                                    JOptionPane.showMessageDialog(null, "No se puede modicar si vas a cambiar en interno");
                                                            }
                                                            else
                                                                JOptionPane.showMessageDialog(null, "La partida ya fue pedida");
                                                        }
                                                    }
                                                    else
                                                    {
                                                        buscaCuentas(-1,-1);
                                                        JOptionPane.showMessageDialog(null, "La partida ya no existe");
                                                    }
                                                }
                                                else
                                                {
                                                   JOptionPane.showMessageDialog(null, "No se pueden modificar partidas enlazadas de otra orden");
                                                }
                                            }
                                            else
                                            {
                                                session.getTransaction().rollback();
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
                                            session.getTransaction().rollback();
                                            System.out.println(e);
                                        }
                                        if(session!=null)
                                            if(session.isOpen()==true)
                                            {
                                                session.flush();
                                                session.clear();
                                                session.close();
                                            }
                                    }
                                    break;
                                
                            case 8://Rep Min
                                    if(vector.get(col)==null || habilita==true)
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
                                            user = (Usuario)session.get(Usuario.class, user.getIdUsuario());
                                            if(user.getEditarLevantamiento()==true || (((String)t_datos.getValueAt(t_datos.getSelectedRow(), 32)).compareToIgnoreCase("o")==0 && user.getAValuacionLevantamiento()==true && r_cerrar.isSelected()==false) || (((String)t_datos.getValueAt(t_datos.getSelectedRow(), 32)).compareToIgnoreCase("o")!=0 && user.getAValuacionLevantamiento()==true) )
                                            {
                                                if(t_datos.getValueAt(row, 32).toString().compareTo("e")!=0)
                                                {
                                                    Partida part=(Partida) session.createCriteria(Partida.class).add(Restrictions.eq("ordenByIdOrden.idOrden", Integer.parseInt(orden))).add(Restrictions.eq("idEvaluacion", Integer.parseInt(t_datos.getValueAt(t_datos.getSelectedRow(), 0).toString()))).add(Restrictions.eq("subPartida", Integer.parseInt(t_datos.getValueAt(t_datos.getSelectedRow(), 1).toString()))).setMaxResults(1).uniqueResult();
                                                    if(part!=null)
                                                    {
                                                        if(value.toString().compareTo("true")==0)
                                                        {
                                                            if(part.getIntCamb()==-1)
                                                            {
                                                                if(part.getPedido()==null)
                                                                {
                                                                    part.setRepMin(0);
                                                                    part.setRepMed(-1);
                                                                    part.setRepMax(-1);
                                                                    if(part.getCam()>-1 && (boolean)t_datos.getValueAt(row, 24)==true)
                                                                    {
                                                                        part.setRefComp(false);
                                                                        part.setAutorizado(false);
                                                                    }
                                                                    part.setCam(-1);
                                                                    part.setRefComp(false);
                                                                    
                                                                    session.update(part);
                                                                    session.getTransaction().commit();
                                                                    vector.setElementAt(value, col);
                                                                    this.dataVector.setElementAt(vector, row);
                                                                    fireTableCellUpdated(row, col);
                                                                    
                                                                    habilita=true;
                                                                    t_datos.setValueAt(false, row, 9);//Rep med
                                                                    t_datos.setValueAt(false, row, 10);//Rep max
                                                                    t_datos.setValueAt(false, row, 7);//Cam
                                                                    habilita=false;
                                                                    
                                                                    if(session.isOpen()==true)
                                                                    {
                                                                        session.flush();
                                                                        session.clear();
                                                                        session.close();
                                                                    }
                                                                }
                                                                else
                                                                    JOptionPane.showMessageDialog(null, "La partida ya fue pedida");
                                                            }
                                                            else
                                                                JOptionPane.showMessageDialog(null, "no puedes reparar si vas a cambiar en interno");
                                                        }
                                                        else
                                                        {
                                                            part.setRepMin(-1);
                                                            session.update(part);
                                                            session.getTransaction().commit();
                                                            vector.setElementAt(value, col);
                                                            this.dataVector.setElementAt(vector, row);
                                                            fireTableCellUpdated(row, col);
                                                            if(session.isOpen()==true)
                                                            {
                                                                session.flush();
                                                                session.clear();
                                                                session.close();
                                                            }
                                                        }
                                                    }
                                                    else
                                                    {
                                                        buscaCuentas(-1,-1);
                                                        JOptionPane.showMessageDialog(null, "La partida ya no existe");
                                                    }
                                                }
                                                else
                                                {
                                                   JOptionPane.showMessageDialog(null, "No se pueden modificar partidas enlazadas de otra orden");
                                                }
                                            }
                                            else
                                            {
                                                session.getTransaction().rollback();
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
                                            session.getTransaction().rollback();
                                            System.out.println(e);
                                        }
                                        if(session!=null)
                                            if(session.isOpen()==true)
                                            {
                                                session.flush();
                                                session.clear();
                                                session.close();
                                            }
                                    }
                                    break;
                                
                            case 9://Rep Med
                                    if(vector.get(col)==null || habilita==true)
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
                                            user = (Usuario)session.get(Usuario.class, user.getIdUsuario());
                                            if(user.getEditarLevantamiento()==true || (((String)t_datos.getValueAt(t_datos.getSelectedRow(), 32)).compareToIgnoreCase("o")==0 && user.getAValuacionLevantamiento()==true && r_cerrar.isSelected()==false) || (((String)t_datos.getValueAt(t_datos.getSelectedRow(), 32)).compareToIgnoreCase("o")!=0 && user.getAValuacionLevantamiento()==true) )
                                            {
                                                if(t_datos.getValueAt(row, 32).toString().compareTo("e")!=0)
                                                {
                                                    Partida part=(Partida) session.createCriteria(Partida.class).add(Restrictions.eq("ordenByIdOrden.idOrden", Integer.parseInt(orden))).add(Restrictions.eq("idEvaluacion", Integer.parseInt(t_datos.getValueAt(t_datos.getSelectedRow(), 0).toString()))).add(Restrictions.eq("subPartida", Integer.parseInt(t_datos.getValueAt(t_datos.getSelectedRow(), 1).toString()))).setMaxResults(1).uniqueResult();
                                                    if(part!=null)
                                                    {
                                                        if(value.toString().compareTo("true")==0)
                                                        {
                                                            if(part.getIntCamb()==-1)
                                                            {
                                                                if(part.getPedido()==null)
                                                                {
                                                                    part.setRepMin(-1);
                                                                    part.setRepMed(0);
                                                                    part.setRepMax(-1);
                                                                    if(part.getCam()>-1 && (boolean)t_datos.getValueAt(row, 24)==true)
                                                                    {
                                                                        part.setRefComp(false);
                                                                        part.setAutorizado(false);
                                                                    }
                                                                    part.setCam(-1);
                                                                    part.setRefComp(false);
                                                                    
                                                                    session.update(part);
                                                                    session.getTransaction().commit();
                                                                    vector.setElementAt(value, col);
                                                                    this.dataVector.setElementAt(vector, row);
                                                                    fireTableCellUpdated(row, col);
                                                                    
                                                                    habilita=true;
                                                                    t_datos.setValueAt(false, row, 8);//Rep mim
                                                                    t_datos.setValueAt(false, row, 10);//Rep max
                                                                    t_datos.setValueAt(false, row, 7);//Cam
                                                                    habilita=false;
                                                                    
                                                                    if(session.isOpen()==true)
                                                                    {
                                                                        session.flush();
                                                                        session.clear();
                                                                        session.close();
                                                                    }
                                                                }
                                                                else
                                                                    JOptionPane.showMessageDialog(null, "La partida ya fue pedida");

                                                            }
                                                            else
                                                                JOptionPane.showMessageDialog(null, "no puedes reparar si vas a cambiar en interno");
                                                        }
                                                        else
                                                        {
                                                            part.setRepMed(-1);
                                                            session.update(part);
                                                            session.getTransaction().commit();
                                                            vector.setElementAt(value, col);
                                                            this.dataVector.setElementAt(vector, row);
                                                            fireTableCellUpdated(row, col);
                                                            if(session.isOpen()==true)
                                                            {
                                                                session.flush();
                                                                session.clear();
                                                                session.close();
                                                            }
                                                        }
                                                    }
                                                    else
                                                    {
                                                        buscaCuentas(-1,-1);
                                                        JOptionPane.showMessageDialog(null, "La partida ya no existe");
                                                    }
                                                }
                                                else
                                                {
                                                   JOptionPane.showMessageDialog(null, "No se pueden modificar Partidas enlazadas de otra orden");
                                                }
                                            }
                                            else
                                            {
                                                session.getTransaction().rollback();
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
                                            session.getTransaction().rollback();
                                            System.out.println(e);
                                        }
                                        if(session!=null)
                                            if(session.isOpen()==true)
                                            {
                                                session.flush();
                                                session.clear();
                                                session.close();
                                            }
                                    }
                                    break;
                                
                            case 10://Rep MAx
                                    if(vector.get(col)==null || habilita==true)
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
                                            user = (Usuario)session.get(Usuario.class, user.getIdUsuario());
                                            if(user.getEditarLevantamiento()==true || (((String)t_datos.getValueAt(t_datos.getSelectedRow(), 32)).compareToIgnoreCase("o")==0 && user.getAValuacionLevantamiento()==true && r_cerrar.isSelected()==false) || (((String)t_datos.getValueAt(t_datos.getSelectedRow(), 32)).compareToIgnoreCase("o")!=0 && user.getAValuacionLevantamiento()==true) )
                                            {
                                                if(t_datos.getValueAt(row, 32).toString().compareTo("e")!=0)
                                                {
                                                    Partida part=(Partida) session.createCriteria(Partida.class).add(Restrictions.eq("ordenByIdOrden.idOrden", Integer.parseInt(orden))).add(Restrictions.eq("idEvaluacion", Integer.parseInt(t_datos.getValueAt(t_datos.getSelectedRow(), 0).toString()))).add(Restrictions.eq("subPartida", Integer.parseInt(t_datos.getValueAt(t_datos.getSelectedRow(), 1).toString()))).setMaxResults(1).uniqueResult();
                                                    if(part!=null)
                                                    {
                                                        if(value.toString().compareTo("true")==0)
                                                        {
                                                            if(part.getIntCamb()==-1)
                                                            {
                                                                if(part.getPedido()==null)
                                                                {
                                                                    part.setRepMin(-1);
                                                                    part.setRepMed(-1);
                                                                    part.setRepMax(0);
                                                                    if(part.getCam()>-1 && (boolean)t_datos.getValueAt(row, 24)==true)
                                                                    {
                                                                        part.setRefComp(false);
                                                                        part.setAutorizado(false);
                                                                    }
                                                                    part.setCam(-1);
                                                                    part.setRefComp(false);
                                                                    
                                                                    session.update(part);
                                                                    session.getTransaction().commit();
                                                                    vector.setElementAt(value, col);
                                                                    this.dataVector.setElementAt(vector, row);
                                                                    fireTableCellUpdated(row, col);
                                                                    
                                                                    habilita=true;
                                                                    t_datos.setValueAt(false, row, 8);//Rep min
                                                                    t_datos.setValueAt(false, row, 9);//Rep med
                                                                    t_datos.setValueAt(false, row, 7);//Cam
                                                                    habilita=false;
                                                                    
                                                                    if(session.isOpen()==true)
                                                                    {
                                                                        session.flush();
                                                                        session.clear();
                                                                        session.close();
                                                                    }
                                                                }
                                                                else
                                                                    JOptionPane.showMessageDialog(null, "La partida ya fue pedida");
                                                            }
                                                            else
                                                                JOptionPane.showMessageDialog(null, "no puedes reparar si vas a cambiar en interno");
                                                        }
                                                        else
                                                        {
                                                            part.setRepMax(-1);
                                                            session.update(part);
                                                            session.getTransaction().commit();
                                                            vector.setElementAt(value, col);
                                                            this.dataVector.setElementAt(vector, row);
                                                            fireTableCellUpdated(row, col);
                                                            if(session.isOpen()==true)
                                                            {
                                                                session.flush();
                                                                session.clear();
                                                                session.close();
                                                            }
                                                        }
                                                    }
                                                    else
                                                    {
                                                        buscaCuentas(-1,-1);
                                                        JOptionPane.showMessageDialog(null, "La partida ya no existe");
                                                    }
                                                }
                                                else
                                                {
                                                   JOptionPane.showMessageDialog(null, "No se pueden modificar partidas enlazadas de otra orden");
                                                }
                                            }
                                            else
                                            {
                                                session.getTransaction().rollback();
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
                                            session.getTransaction().rollback();
                                            System.out.println(e);
                                        }
                                        if(session!=null)
                                            if(session.isOpen()==true)
                                            {
                                                session.flush();
                                                session.clear();
                                                session.close();
                                            }
                                    }
                                    break;
                                
                            case 11:
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
                                            user = (Usuario)session.get(Usuario.class, user.getIdUsuario());
                                            if(user.getEditarLevantamiento()==true || (((String)t_datos.getValueAt(t_datos.getSelectedRow(), 32)).compareToIgnoreCase("o")==0 && user.getAValuacionLevantamiento()==true && r_cerrar.isSelected()==false) || (((String)t_datos.getValueAt(t_datos.getSelectedRow(), 32)).compareToIgnoreCase("o")!=0 && user.getAValuacionLevantamiento()==true) )
                                            {
                                                if(t_datos.getValueAt(row, 32).toString().compareTo("e")!=0)
                                                {
                                                    Partida part=(Partida) session.createCriteria(Partida.class).add(Restrictions.eq("ordenByIdOrden.idOrden", Integer.parseInt(orden))).add(Restrictions.eq("idEvaluacion", Integer.parseInt(t_datos.getValueAt(t_datos.getSelectedRow(), 0).toString()))).add(Restrictions.eq("subPartida", Integer.parseInt(t_datos.getValueAt(t_datos.getSelectedRow(), 1).toString()))).setMaxResults(1).uniqueResult();
                                                    if(part!=null)
                                                    {
                                                        if(value.toString().compareTo("true")==0)
                                                            part.setPint(0);
                                                        else
                                                            part.setPint(-1);
                                                        session.update(part);
                                                        session.getTransaction().commit();
                                                        vector.setElementAt(value, col);
                                                        this.dataVector.setElementAt(vector, row);
                                                        fireTableCellUpdated(row, col);
                                                        if(session.isOpen()==true)
                                                        {
                                                            session.flush();
                                                            session.clear();
                                                            session.close();
                                                        }
                                                        //buscaCuentas(row,col);
                                                    }
                                                    else
                                                    {
                                                        buscaCuentas(-1,-1);
                                                        JOptionPane.showMessageDialog(null, "La partida ya no existe");
                                                    }
                                                }
                                                else
                                                {
                                                   JOptionPane.showMessageDialog(null, "No se pueden modificar partidas enlazadas de otra orden");
                                                }
                                            }
                                            else
                                            {
                                                session.getTransaction().rollback();
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
                                            session.getTransaction().rollback();
                                            System.out.println(e);
                                        }
                                        if(session!=null)
                                            if(session.isOpen()==true)
                                            {
                                                session.flush();
                                                session.clear();
                                                session.close();
                                            }
                                    }
                                    break;
                                
                            case 12:
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
                                            user = (Usuario)session.get(Usuario.class, user.getIdUsuario());
                                            if(user.getEditarLevantamiento()==true || (((String)t_datos.getValueAt(t_datos.getSelectedRow(), 32)).compareToIgnoreCase("o")==0 && user.getAValuacionLevantamiento()==true && r_cerrar.isSelected()==false) || (((String)t_datos.getValueAt(t_datos.getSelectedRow(), 32)).compareToIgnoreCase("o")!=0 && user.getAValuacionLevantamiento()==true) )
                                            {
                                                if(t_datos.getValueAt(row, 32).toString().compareTo("e")!=0)
                                                {
                                                    Partida part=(Partida) session.createCriteria(Partida.class).add(Restrictions.eq("ordenByIdOrden.idOrden", Integer.parseInt(orden))).add(Restrictions.eq("idEvaluacion", Integer.parseInt(t_datos.getValueAt(row, 0).toString()))).add(Restrictions.eq("subPartida", Integer.parseInt(t_datos.getValueAt(row, 1).toString()))).setMaxResults(1).uniqueResult();
                                                    if(part!=null)
                                                    {
                                                        double val;
                                                        if(value.toString().compareTo("")==0)
                                                            val=1.0d;
                                                        else
                                                            val=Double.parseDouble(value.toString());
                                                        part.setCant(val);
                                                        part.setCantidadAut(val);
                                                        session.update(part);
                                                        session.getTransaction().commit();
                                                        vector.setElementAt(value, col);
                                                        this.dataVector.setElementAt(vector, row);
                                                        fireTableCellUpdated(row, col);
                                                        if(session.isOpen()==true)
                                                        {
                                                            session.flush();
                                                            session.clear();
                                                            session.close();
                                                        }
                                                        //buscaCuentas(row,col);
                                                    }
                                                    else
                                                    {
                                                        buscaCuentas(-1,-1);
                                                        JOptionPane.showMessageDialog(null, "La partida ya no existe");
                                                    }
                                                }
                                                else
                                                {
                                                   JOptionPane.showMessageDialog(null, "No se pueden modificar partidas enlazadas de otra orden");
                                                }
                                            }
                                            else
                                            {
                                                session.getTransaction().rollback();
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
                                            session.getTransaction().rollback();
                                            System.out.println(e);
                                        }
                                        if(session!=null)
                                            if(session.isOpen()==true)
                                            {
                                                session.flush();
                                                session.clear();
                                                session.close();
                                            }
                                    }
                                    break;
                                
                            case 13:
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
                                            user = (Usuario)session.get(Usuario.class, user.getIdUsuario());
                                            if(user.getEditarLevantamiento()==true || (((String)t_datos.getValueAt(t_datos.getSelectedRow(), 32)).compareToIgnoreCase("o")==0 && user.getAValuacionLevantamiento()==true && r_cerrar.isSelected()==false) || (((String)t_datos.getValueAt(t_datos.getSelectedRow(), 32)).compareToIgnoreCase("o")!=0 && user.getAValuacionLevantamiento()==true) )
                                            {
                                                if(t_datos.getValueAt(row, 32).toString().compareTo("e")!=0)
                                                {
                                                    Partida part=(Partida) session.createCriteria(Partida.class).add(Restrictions.eq("ordenByIdOrden.idOrden", Integer.parseInt(orden))).add(Restrictions.eq("idEvaluacion", Integer.parseInt(t_datos.getValueAt(t_datos.getSelectedRow(), 0).toString()))).add(Restrictions.eq("subPartida", Integer.parseInt(t_datos.getValueAt(t_datos.getSelectedRow(), 1).toString()))).setMaxResults(1).uniqueResult();
                                                    if(part!=null)
                                                    {
                                                        part.setMed(value.toString());
                                                        session.update(part);
                                                        session.getTransaction().commit();
                                                        vector.setElementAt(value, col);
                                                        this.dataVector.setElementAt(vector, row);
                                                        fireTableCellUpdated(row, col);
                                                        if(session.isOpen()==true)
                                                        {
                                                            session.flush();
                                                            session.clear();
                                                            session.close();
                                                        }
                                                        //buscaCuentas(row,col);
                                                    }
                                                    else
                                                    {
                                                        buscaCuentas(-1,-1);
                                                        JOptionPane.showMessageDialog(null, "La partida ya no existe");
                                                    }
                                                }
                                                else
                                                {
                                                   JOptionPane.showMessageDialog(null, "No se pueden modificar partidas enlazadas de otra orden");
                                                }
                                            }
                                            else
                                            {
                                                session.getTransaction().rollback();
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
                                            session.getTransaction().rollback();
                                            System.out.println(e);
                                        }
                                        if(session!=null)
                                            if(session.isOpen()==true)
                                            {
                                                session.flush();
                                                session.clear();
                                                session.close();
                                            }
                                    }
                                    break;
                            case 16://codigo
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
                                            user = (Usuario)session.get(Usuario.class, user.getIdUsuario());
                                            if(user.getEditarLevantamiento()==true || (((String)t_datos.getValueAt(t_datos.getSelectedRow(), 32)).compareToIgnoreCase("o")==0 && user.getAValuacionLevantamiento()==true && r_cerrar.isSelected()==false) || (((String)t_datos.getValueAt(t_datos.getSelectedRow(), 32)).compareToIgnoreCase("o")!=0 && user.getAValuacionLevantamiento()==true) )
                                            {
                                                if(t_datos.getValueAt(row, 32).toString().compareTo("e")!=0)
                                                {
                                                    Partida part=(Partida) session.createCriteria(Partida.class).add(Restrictions.eq("ordenByIdOrden.idOrden", Integer.parseInt(orden))).add(Restrictions.eq("idEvaluacion", Integer.parseInt(t_datos.getValueAt(t_datos.getSelectedRow(), 0).toString()))).add(Restrictions.eq("subPartida", Integer.parseInt(t_datos.getValueAt(t_datos.getSelectedRow(), 1).toString()))).setMaxResults(1).uniqueResult();
                                                    if(part!=null)
                                                    {
                                                        if(value.toString().compareTo("")!=0)
                                                        {
                                                            Ejemplar ejem = (Ejemplar)session.get(Ejemplar.class, value.toString());
                                                            if(ejem!=null)
                                                            {
                                                                //part.setEjemplar(ejem);
                                                                Criteria crit = session.createCriteria(Partida.class);
                                                                crit.add(Restrictions.eq("ejemplar.idParte", ejem.getIdParte()));
                                                                crit = crit.createAlias("pedido", "ped");
                                                                //crit=crit.addOrder(Order.asc("ped.fechaPedido"));
                                                                crit=crit.addOrder(Order.desc("pcp"));
                                                                crit.add(Restrictions.isNotNull("pedido"));
                                                                Partida partidaPrecio=(Partida) crit.setMaxResults(1).uniqueResult();
                                                                part.setEjemplar(ejem);
                                                                if(partidaPrecio!=null)
                                                                {
                                                                    if(partidaPrecio.getPcp()>part.getCU())
                                                                    {
                                                                        part.setCU(partidaPrecio.getPcp());
                                                                        part.setPrecioAutCU(0.0+Math.round(part.getCU()/(1-(part.getPorcentaje()*0.01))));
                                                                        part.setPrecioCiaSegurosCU(part.getPrecioAutCU());
                                                                    }
                                                                }
                                                                session.update(part);
                                                                session.getTransaction().commit();
                                                                vector.setElementAt(value, col);
                                                                this.dataVector.setElementAt(vector, row);
                                                                fireTableCellUpdated(row, col);
                                                                if(session.isOpen()==true)
                                                                {
                                                                    session.flush();
                                                                    session.clear();
                                                                    session.close();
                                                                }
                                                                //buscaCuentas(row,col);
                                                            }
                                                        }
                                                        else
                                                        {
                                                            part.setEjemplar(null);
                                                            session.update(part);
                                                            session.getTransaction().commit();
                                                            vector.setElementAt("", col);
                                                            this.dataVector.setElementAt(vector, row);
                                                            fireTableCellUpdated(row, col);
                                                            if(session.isOpen()==true)
                                                            {
                                                                session.flush();
                                                                session.clear();
                                                                session.close();
                                                            }
                                                            //buscaCuentas(row,col);
                                                        }
                                                    }
                                                    else
                                                    {
                                                        buscaCuentas(-1,-1);
                                                        JOptionPane.showMessageDialog(null, "La partida ya no existe");
                                                    }
                                                }
                                                else
                                                {
                                                   JOptionPane.showMessageDialog(null, "No se pueden modificar partidas enlazadas de otra orden");
                                                }
                                            }
                                            else
                                            {
                                                session.getTransaction().rollback();
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
                                            session.getTransaction().rollback();
                                            System.out.println(e);
                                        }
                                        if(session!=null)
                                            if(session.isOpen()==true)
                                            {
                                                session.flush();
                                                session.clear();
                                                session.close();
                                            }
                                    }
                                    break;

                            case 17://Freeze
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
                                            user = (Usuario)session.get(Usuario.class, user.getIdUsuario());
                                            if(user.getEditarLevantamiento()==true || (((String)t_datos.getValueAt(t_datos.getSelectedRow(), 32)).compareToIgnoreCase("o")==0 && user.getAValuacionLevantamiento()==true && r_cerrar.isSelected()==false) || (((String)t_datos.getValueAt(t_datos.getSelectedRow(), 32)).compareToIgnoreCase("o")!=0 && user.getAValuacionLevantamiento()==true) )
                                            {
                                                if(t_datos.getValueAt(row, 32).toString().compareTo("e")!=0)
                                                {
                                                    Partida part=(Partida) session.createCriteria(Partida.class).add(Restrictions.eq("ordenByIdOrden.idOrden", Integer.parseInt(orden))).add(Restrictions.eq("idEvaluacion", Integer.parseInt(t_datos.getValueAt(t_datos.getSelectedRow(), 0).toString()))).add(Restrictions.eq("subPartida", Integer.parseInt(t_datos.getValueAt(t_datos.getSelectedRow(), 1).toString()))).setMaxResults(1).uniqueResult();
                                                    if(part!=null)
                                                    {
                                                        if(t_datos.getValueAt(row, 32).toString().compareTo("c")==0)
                                                        {
                                                            if(value.toString().compareTo("true")==0)
                                                                part.setIncluida(true);
                                                            else
                                                                part.setIncluida(false);
                                                            session.update(part);
                                                            session.getTransaction().commit();
                                                            vector.setElementAt(value, col);
                                                            this.dataVector.setElementAt(vector, row);
                                                            fireTableCellUpdated(row, col);
                                                            if(session.isOpen()==true)
                                                            {
                                                                session.flush();
                                                                session.clear();
                                                                session.close();
                                                            }
                                                            //buscaCuentas(row,col);
                                                        }
                                                        else
                                                        {
                                                            JOptionPane.showMessageDialog(null, "solo puedes congelar las partidas complementarias");
                                                        }
                                                    }
                                                    else
                                                    {
                                                        buscaCuentas(-1,-1);
                                                        JOptionPane.showMessageDialog(null, "La partida ya no existe");
                                                    }
                                                }
                                                else
                                                {
                                                   JOptionPane.showMessageDialog(null, "No se pueden modificar partidas enlazadas de otra orden");
                                                }
                                            }
                                            else
                                            {
                                                session.getTransaction().rollback();
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
                                            session.getTransaction().rollback();
                                            System.out.println(e);
                                        }
                                        if(session!=null)
                                            if(session.isOpen()==true)
                                            {
                                                session.flush();
                                                session.clear();
                                                session.close();
                                            }
                                    }
                                    break;
                                
                            case 18://ori
                                    if(vector.get(col)==null || habilita==true)
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
                                            user = (Usuario)session.get(Usuario.class, user.getIdUsuario());
                                            if(user.getEditarLevantamiento()==true || (((String)t_datos.getValueAt(t_datos.getSelectedRow(), 32)).compareToIgnoreCase("o")==0 && user.getAValuacionLevantamiento()==true && r_cerrar.isSelected()==false) || (((String)t_datos.getValueAt(t_datos.getSelectedRow(), 32)).compareToIgnoreCase("o")!=0 && user.getAValuacionLevantamiento()==true) )
                                            {
                                                if(t_datos.getValueAt(row, 32).toString().compareTo("e")!=0)
                                                {
                                                    Partida part=(Partida) session.createCriteria(Partida.class).add(Restrictions.eq("ordenByIdOrden.idOrden", Integer.parseInt(orden))).add(Restrictions.eq("idEvaluacion", Integer.parseInt(t_datos.getValueAt(t_datos.getSelectedRow(), 0).toString()))).add(Restrictions.eq("subPartida", Integer.parseInt(t_datos.getValueAt(t_datos.getSelectedRow(), 1).toString()))).setMaxResults(1).uniqueResult();
                                                    if(part!=null)
                                                    {
                                                        if(value.toString().compareTo("true")==0)
                                                        {
                                                            part.setOri(true);
                                                            part.setNal(false);
                                                            part.setDesm(false);
                                                            part.setPd(false);
                                                            
                                                            session.update(part);
                                                            session.getTransaction().commit();
                                                            vector.setElementAt(value, col);
                                                            this.dataVector.setElementAt(vector, row);
                                                            fireTableCellUpdated(row, col);
                                                            
                                                            habilita=true;
                                                            t_datos.setValueAt(false, row, 19);
                                                            t_datos.setValueAt(false, row, 20);
                                                            t_datos.setValueAt(false, row, 21);
                                                            habilita=false;
                                                            
                                                            if(session.isOpen()==true)
                                                            {
                                                                session.flush();
                                                                session.clear();
                                                                session.close();
                                                            }
                                                        }
                                                        else
                                                        {
                                                            part.setOri(false);
                                                            session.update(part);
                                                            session.getTransaction().commit();
                                                            vector.setElementAt(value, col);
                                                            this.dataVector.setElementAt(vector, row);
                                                            fireTableCellUpdated(row, col);
                                                            if(session.isOpen()==true)
                                                            {
                                                                session.flush();
                                                                session.clear();
                                                                session.close();
                                                            }
                                                        }
                                                    }
                                                    else
                                                    {
                                                        buscaCuentas(-1,-1);
                                                        JOptionPane.showMessageDialog(null, "La partida ya no existe");
                                                    }
                                                }
                                                else
                                                {
                                                   JOptionPane.showMessageDialog(null, "No se pueden modificar partidas enlazadas de otra orden");
                                                }
                                            }
                                            else
                                            {
                                                session.getTransaction().rollback();
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
                                            session.getTransaction().rollback();
                                            System.out.println(e);
                                        }
                                        if(session!=null)
                                            if(session.isOpen()==true)
                                            {
                                                session.flush();
                                                session.clear();
                                                session.close();
                                            }
                                    }
                                    break;
                                
                            case 19://Nal
                                    if(vector.get(col)==null || habilita==true)
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
                                            user = (Usuario)session.get(Usuario.class, user.getIdUsuario());
                                            if(user.getEditarLevantamiento()==true || (((String)t_datos.getValueAt(t_datos.getSelectedRow(), 32)).compareToIgnoreCase("o")==0 && user.getAValuacionLevantamiento()==true && r_cerrar.isSelected()==false) || (((String)t_datos.getValueAt(t_datos.getSelectedRow(), 32)).compareToIgnoreCase("o")!=0 && user.getAValuacionLevantamiento()==true) )
                                            {
                                                if(t_datos.getValueAt(row, 32).toString().compareTo("e")!=0)
                                                {
                                                    Partida part=(Partida) session.createCriteria(Partida.class).add(Restrictions.eq("ordenByIdOrden.idOrden", Integer.parseInt(orden))).add(Restrictions.eq("idEvaluacion", Integer.parseInt(t_datos.getValueAt(t_datos.getSelectedRow(), 0).toString()))).add(Restrictions.eq("subPartida", Integer.parseInt(t_datos.getValueAt(t_datos.getSelectedRow(), 1).toString()))).setMaxResults(1).uniqueResult();
                                                    if(part!=null)
                                                    {
                                                        if(value.toString().compareTo("true")==0)
                                                        {
                                                            part.setOri(false);
                                                            part.setNal(true);
                                                            part.setDesm(false);
                                                            part.setPd(false);

                                                            session.update(part);
                                                            session.getTransaction().commit();
                                                            vector.setElementAt(value, col);
                                                            this.dataVector.setElementAt(vector, row);
                                                            fireTableCellUpdated(row, col);
                                                            
                                                            habilita=true;
                                                            t_datos.setValueAt(false, row, 18);
                                                            t_datos.setValueAt(false, row, 20);
                                                            t_datos.setValueAt(false, row, 21);
                                                            habilita=false;
                                                            
                                                            if(session.isOpen()==true)
                                                            {
                                                                session.flush();
                                                                session.clear();
                                                                session.close();
                                                            }
                                                        }
                                                        else
                                                        {
                                                            part.setNal(false);
                                                            session.update(part);
                                                            session.getTransaction().commit();
                                                            vector.setElementAt(value, col);
                                                            this.dataVector.setElementAt(vector, row);
                                                            fireTableCellUpdated(row, col);
                                                            if(session.isOpen()==true)
                                                            {
                                                                session.flush();
                                                                session.clear();
                                                                session.close();
                                                            }
                                                        }
                                                    }
                                                    else
                                                    {
                                                        buscaCuentas(-1,-1);
                                                        JOptionPane.showMessageDialog(null, "La partida ya no existe");
                                                    }
                                                }
                                                else
                                                {
                                                   JOptionPane.showMessageDialog(null, "No se pueden modificar partidas enlazadas de otra orden");
                                                }
                                            }
                                            else
                                            {
                                                session.getTransaction().rollback();
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
                                            session.getTransaction().rollback();
                                            System.out.println(e);
                                        }
                                        if(session!=null)
                                            if(session.isOpen()==true)
                                            {
                                                session.flush();
                                                session.clear();
                                                session.close();
                                            }
                                    }
                                    break;
                                
                            case 20://Desm
                                    if(vector.get(col)==null || habilita==true)
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
                                            user = (Usuario)session.get(Usuario.class, user.getIdUsuario());
                                            if(user.getEditarLevantamiento()==true || (((String)t_datos.getValueAt(t_datos.getSelectedRow(), 32)).compareToIgnoreCase("o")==0 && user.getAValuacionLevantamiento()==true && r_cerrar.isSelected()==false) || (((String)t_datos.getValueAt(t_datos.getSelectedRow(), 32)).compareToIgnoreCase("o")!=0 && user.getAValuacionLevantamiento()==true) )
                                            {
                                                if(t_datos.getValueAt(row, 32).toString().compareTo("e")!=0)
                                                {
                                                    Partida part=(Partida) session.createCriteria(Partida.class).add(Restrictions.eq("ordenByIdOrden.idOrden", Integer.parseInt(orden))).add(Restrictions.eq("idEvaluacion", Integer.parseInt(t_datos.getValueAt(t_datos.getSelectedRow(), 0).toString()))).add(Restrictions.eq("subPartida", Integer.parseInt(t_datos.getValueAt(t_datos.getSelectedRow(), 1).toString()))).setMaxResults(1).uniqueResult();
                                                    if(part!=null)
                                                    {
                                                        if(value.toString().compareTo("true")==0)
                                                        {
                                                            part.setOri(false);
                                                            part.setNal(false);
                                                            part.setDesm(true);
                                                            part.setPd(false);
                                                            
                                                            session.update(part);
                                                            session.getTransaction().commit();
                                                            vector.setElementAt(value, col);
                                                            this.dataVector.setElementAt(vector, row);
                                                            fireTableCellUpdated(row, col);
                                                            
                                                            habilita=true;
                                                            t_datos.setValueAt(false, row, 18);
                                                            t_datos.setValueAt(false, row, 19);
                                                            t_datos.setValueAt(false, row, 21);
                                                            habilita=false;
                                                            
                                                            if(session.isOpen()==true)
                                                            {
                                                                session.flush();
                                                                session.clear();
                                                                session.close();
                                                            }
                                                        }
                                                        else
                                                        {
                                                            part.setDesm(false);
                                                            session.update(part);
                                                            session.getTransaction().commit();
                                                            vector.setElementAt(value, col);
                                                            this.dataVector.setElementAt(vector, row);
                                                            fireTableCellUpdated(row, col);
                                                            if(session.isOpen()==true)
                                                            {
                                                                session.flush();
                                                                session.clear();
                                                                session.close();
                                                            }
                                                        }
                                                    }
                                                    else
                                                    {
                                                        buscaCuentas(-1,-1);
                                                        JOptionPane.showMessageDialog(null, "La partida ya no existe");
                                                    }
                                                }
                                                else
                                                {
                                                   JOptionPane.showMessageDialog(null, "No se pueden modificar partidas enlazadas de otra orden");
                                                }
                                            }
                                            else
                                            {
                                                session.getTransaction().rollback();
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
                                            session.getTransaction().rollback();
                                            System.out.println(e);
                                        }
                                        if(session!=null)
                                            if(session.isOpen()==true)
                                            {
                                                session.flush();
                                                session.clear();
                                                session.close();
                                            }
                                    }
                                    break;
                                
                            case 21:
                                    if(vector.get(col)==null || habilita==true)
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
                                            user = (Usuario)session.get(Usuario.class, user.getIdUsuario());
                                            if(user.getEditarLevantamiento()==true || (((String)t_datos.getValueAt(t_datos.getSelectedRow(), 32)).compareToIgnoreCase("o")==0 && user.getAValuacionLevantamiento()==true && r_cerrar.isSelected()==false) || (((String)t_datos.getValueAt(t_datos.getSelectedRow(), 32)).compareToIgnoreCase("o")!=0 && user.getAValuacionLevantamiento()==true) )
                                            {
                                                if(t_datos.getValueAt(row, 32).toString().compareTo("e")!=0)
                                                {
                                                    Partida part=(Partida) session.createCriteria(Partida.class).add(Restrictions.eq("ordenByIdOrden.idOrden", Integer.parseInt(orden))).add(Restrictions.eq("idEvaluacion", Integer.parseInt(t_datos.getValueAt(t_datos.getSelectedRow(), 0).toString()))).add(Restrictions.eq("subPartida", Integer.parseInt(t_datos.getValueAt(t_datos.getSelectedRow(), 1).toString()))).setMaxResults(1).uniqueResult();
                                                    if(part!=null)
                                                    {
                                                        if(value.toString().compareTo("true")==0)
                                                        {
                                                            part.setOri(false);
                                                            part.setNal(false);
                                                            part.setDesm(false);
                                                            part.setPd(true);
                                                            
                                                            session.update(part);
                                                            session.getTransaction().commit();
                                                            vector.setElementAt(value, col);
                                                            this.dataVector.setElementAt(vector, row);
                                                            fireTableCellUpdated(row, col);
                                                            
                                                            habilita=true;
                                                            t_datos.setValueAt(false, row, 18);
                                                            t_datos.setValueAt(false, row, 19);
                                                            t_datos.setValueAt(false, row, 20);
                                                            habilita=false;
                                                            
                                                            if(session.isOpen()==true)
                                                            {
                                                                session.flush();
                                                                session.clear();
                                                                session.close();
                                                            }
                                                        }
                                                        else
                                                        {
                                                            part.setPd(false);
                                                            session.update(part);
                                                            session.getTransaction().commit();
                                                            vector.setElementAt(value, col);
                                                            this.dataVector.setElementAt(vector, row);
                                                            fireTableCellUpdated(row, col);
                                                            if(session.isOpen()==true)
                                                            {
                                                                session.flush();
                                                                session.clear();
                                                                session.close();
                                                            }
                                                        }
                                                    }
                                                    else
                                                    {
                                                        buscaCuentas(-1,-1);
                                                        JOptionPane.showMessageDialog(null, "La partida ya no existe");
                                                    }
                                                }
                                                else
                                                {
                                                   JOptionPane.showMessageDialog(null, "No se pueden modificar partidas enlazadas de otra orden");
                                                }
                                            }
                                            else
                                            {
                                                session.getTransaction().rollback();
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
                                            session.getTransaction().rollback();
                                            System.out.println(e);
                                        }
                                        if(session!=null)
                                            if(session.isOpen()==true)
                                            {
                                                session.flush();
                                                session.clear();
                                                session.close();
                                            }
                                    }
                                    break;                                

                                //falta meter TOT 
                            case 23://Int Desm
                                    if(vector.get(col)==null || habilita==true)
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
                                            user = (Usuario)session.get(Usuario.class, user.getIdUsuario());
                                            if(user.getEditarLevantamiento()==true || (((String)t_datos.getValueAt(t_datos.getSelectedRow(), 32)).compareToIgnoreCase("o")==0 && user.getAValuacionLevantamiento()==true && r_cerrar.isSelected()==false) || (((String)t_datos.getValueAt(t_datos.getSelectedRow(), 32)).compareToIgnoreCase("o")!=0 && user.getAValuacionLevantamiento()==true) )
                                            {
                                                if(t_datos.getValueAt(row, 32).toString().compareTo("e")!=0)
                                                {
                                                    Partida part=(Partida) session.createCriteria(Partida.class).add(Restrictions.eq("ordenByIdOrden.idOrden", Integer.parseInt(orden))).add(Restrictions.eq("idEvaluacion", Integer.parseInt(t_datos.getValueAt(t_datos.getSelectedRow(), 0).toString()))).add(Restrictions.eq("subPartida", Integer.parseInt(t_datos.getValueAt(t_datos.getSelectedRow(), 1).toString()))).setMaxResults(1).uniqueResult();
                                                    if(part!=null)
                                                    {
                                                        if(value.toString().compareTo("true")==0)
                                                        {
                                                            if(part.getPedido()==null)
                                                            {
                                                                part.setIntDesm(0);
                                                                part.setIntCamb(-1);
                                                                part.setRefComp(false);
                                                                part.setAutorizado(false);
                                                                
                                                                session.update(part);
                                                                session.getTransaction().commit();
                                                                vector.setElementAt(value, col);
                                                                this.dataVector.setElementAt(vector, row);
                                                                fireTableCellUpdated(row, col);
                                                                
                                                                habilita=true;
                                                                t_datos.setValueAt(false, row, 24);
                                                                habilita=false;
                                                                
                                                                if(session.isOpen()==true)
                                                                {
                                                                    session.flush();
                                                                    session.clear();
                                                                    session.close();
                                                                }
                                                            }
                                                            else
                                                                JOptionPane.showMessageDialog(null, "La partida ya fue pedida");
                                                        }
                                                        else
                                                        {
                                                            part.setIntDesm(-1);
                                                            session.update(part);
                                                            session.getTransaction().commit();
                                                            vector.setElementAt(value, col);
                                                            this.dataVector.setElementAt(vector, row);
                                                            fireTableCellUpdated(row, col);
                                                            if(session.isOpen()==true)
                                                            {
                                                                session.flush();
                                                                session.clear();
                                                                session.close();
                                                            }
                                                        }
                                                    }
                                                    else
                                                    {
                                                        buscaCuentas(-1,-1);
                                                        JOptionPane.showMessageDialog(null, "La partida ya no existe");
                                                    }
                                                }
                                                else
                                                {
                                                   JOptionPane.showMessageDialog(null, "No se pueden modificar partidas enlazadas de otra orden");
                                                }
                                            }
                                            else
                                            {
                                                session.getTransaction().rollback();
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
                                            session.getTransaction().rollback();
                                            System.out.println(e);
                                        }
                                        if(session!=null)
                                            if(session.isOpen()==true)
                                            {
                                                session.flush();
                                                session.clear();
                                                session.close();
                                            }
                                    }
                                    break;
                                
                            case 24://Int cam
                                    if(vector.get(col)==null || habilita==true)
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
                                            user = (Usuario)session.get(Usuario.class, user.getIdUsuario());
                                            if(user.getEditarLevantamiento()==true || (((String)t_datos.getValueAt(t_datos.getSelectedRow(), 32)).compareToIgnoreCase("o")==0 && user.getAValuacionLevantamiento()==true && r_cerrar.isSelected()==false) || (((String)t_datos.getValueAt(t_datos.getSelectedRow(), 32)).compareToIgnoreCase("o")!=0 && user.getAValuacionLevantamiento()==true) )
                                            {
                                                if(t_datos.getValueAt(row, 32).toString().compareTo("e")!=0)
                                                {
                                                    Partida part=(Partida) session.createCriteria(Partida.class).add(Restrictions.eq("ordenByIdOrden.idOrden", Integer.parseInt(orden))).add(Restrictions.eq("idEvaluacion", Integer.parseInt(t_datos.getValueAt(t_datos.getSelectedRow(), 0).toString()))).add(Restrictions.eq("subPartida", Integer.parseInt(t_datos.getValueAt(t_datos.getSelectedRow(), 1).toString()))).setMaxResults(1).uniqueResult();
                                                    if(part!=null)
                                                    {
                                                        if(value.toString().compareTo("true")==0)
                                                        {
                                                            if(part.getRepMin()==-1 && part.getRepMed()==-1 && part.getRepMax()==-1)
                                                            {
                                                                if(part.getDm()==-1)
                                                                {
                                                                    part.setIntCamb(0);
                                                                    part.setIntDesm(-1);
                                                                    if((boolean)t_datos.getValueAt(row, 7)==true)
                                                                    {
                                                                        part.setRefComp(true);
                                                                        part.setAutorizado(true);
                                                                    }
                                                                    part.setIntRepMin(-1);
                                                                    part.setIntRepMed(-1);
                                                                    part.setIntRepMax(-1);
                                                                    
                                                                    session.update(part);
                                                                    session.getTransaction().commit();
                                                                    vector.setElementAt(value, col);
                                                                    this.dataVector.setElementAt(vector, row);
                                                                    fireTableCellUpdated(row, col);
                                                                    
                                                                    habilita=true;
                                                                    t_datos.setValueAt(false, row, 23);//Int desm
                                                                    t_datos.setValueAt(false, row, 25);//Int Rep min
                                                                    t_datos.setValueAt(false, row, 26);//Int Rep med
                                                                    t_datos.setValueAt(false, row, 27);//Int Rep Max
                                                                    habilita=false;
                                                                    
                                                                    if(session.isOpen()==true)
                                                                    {
                                                                        session.flush();
                                                                        session.clear();
                                                                        session.close();
                                                                    }
                                                                }
                                                                else
                                                                    JOptionPane.showMessageDialog(null, "No puedes cambiar si vas a desmontar montar en aseguradora");
                                                            }
                                                            else
                                                                JOptionPane.showMessageDialog(null, "No puedes cambiar si vas a reparar en aseguradora");
                                                        }
                                                        else
                                                        {
                                                            if(part.getPedido()==null)
                                                            {
                                                                part.setIntCamb(-1);
                                                                if((boolean)t_datos.getValueAt(row, 7)==true)
                                                                {
                                                                    part.setAutorizado(false);
                                                                }
                                                                if((boolean)t_datos.getValueAt(row, 7)==false)
                                                                {
                                                                    part.setRefComp(false);
                                                                    part.setAutorizado(false);
                                                                }
                                                                
                                                                session.update(part);
                                                                session.getTransaction().commit();
                                                                vector.setElementAt(value, col);
                                                                this.dataVector.setElementAt(vector, row);
                                                                fireTableCellUpdated(row, col);
                                                                if(session.isOpen()==true)
                                                                {
                                                                    session.flush();
                                                                    session.clear();
                                                                    session.close();
                                                                }
                                                            }
                                                            else
                                                                JOptionPane.showMessageDialog(null, "La partida ya fue pedida");
                                                        }
                                                    }
                                                    else
                                                    {
                                                        buscaCuentas(-1,-1);
                                                        JOptionPane.showMessageDialog(null, "La partida ya no existe");
                                                    }
                                                }
                                                else
                                                {
                                                   JOptionPane.showMessageDialog(null, "No se pueden modificar partidas enlazadas de otra orden");
                                                }
                                            }
                                            else
                                            {
                                                session.getTransaction().rollback();
                                                if(session.isOpen()==true)
                                                    session.close();
                                                JOptionPane.showMessageDialog(null, "¡Acceso denegado!");
                                            }
                                        }
                                        catch(Exception e)
                                        {
                                            session.getTransaction().rollback();
                                            System.out.println(e);
                                        }
                                        if(session!=null)
                                            if(session.isOpen()==true)
                                            {
                                                session.flush();
                                                session.clear();
                                                session.close();
                                            }
                                    }
                                    break;
                                
                            case 25://Int Rep Mim
                                    if(vector.get(col)==null || habilita==true)
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
                                            user = (Usuario)session.get(Usuario.class, user.getIdUsuario());
                                            if(user.getEditarLevantamiento()==true || (((String)t_datos.getValueAt(t_datos.getSelectedRow(), 32)).compareToIgnoreCase("o")==0 && user.getAValuacionLevantamiento()==true && r_cerrar.isSelected()==false) || (((String)t_datos.getValueAt(t_datos.getSelectedRow(), 32)).compareToIgnoreCase("o")!=0 && user.getAValuacionLevantamiento()==true) )
                                            {
                                                if(t_datos.getValueAt(row, 32).toString().compareTo("e")!=0)
                                                {
                                                    Partida part=(Partida) session.createCriteria(Partida.class).add(Restrictions.eq("ordenByIdOrden.idOrden", Integer.parseInt(orden))).add(Restrictions.eq("idEvaluacion", Integer.parseInt(t_datos.getValueAt(t_datos.getSelectedRow(), 0).toString()))).add(Restrictions.eq("subPartida", Integer.parseInt(t_datos.getValueAt(t_datos.getSelectedRow(), 1).toString()))).setMaxResults(1).uniqueResult();
                                                    if(part!=null)
                                                    {
                                                        if(value.toString().compareTo("true")==0)
                                                        {
                                                            if(part.getPedido()==null)
                                                            {
                                                                part.setIntRepMin(0);
                                                                part.setIntRepMed(-1);
                                                                part.setIntRepMax(-1);

                                                                part.setIntCamb(-1);
                                                                part.setRefComp(false);
                                                                part.setAutorizado(false);
                                                                
                                                                session.update(part);
                                                                session.getTransaction().commit();
                                                                vector.setElementAt(value, col);
                                                                this.dataVector.setElementAt(vector, row);
                                                                fireTableCellUpdated(row, col);
                                                                
                                                                habilita=true;
                                                                t_datos.setValueAt(false, row, 26);//Int Rep med
                                                                t_datos.setValueAt(false, row, 27);//Int Rep Max
                                                                t_datos.setValueAt(false, row, 24);//Int cam
                                                                habilita=false;
                                                                
                                                                if(session.isOpen()==true)
                                                                {
                                                                    session.flush();
                                                                    session.clear();
                                                                    session.close();
                                                                }
                                                            }
                                                            else
                                                                JOptionPane.showMessageDialog(null, "La partida ya fue pedida");
                                                        }
                                                        else
                                                        {
                                                            part.setIntRepMin(-1);
                                                            session.update(part);
                                                            session.getTransaction().commit();
                                                            vector.setElementAt(value, col);
                                                            this.dataVector.setElementAt(vector, row);
                                                            fireTableCellUpdated(row, col);
                                                            if(session.isOpen()==true)
                                                            {
                                                                session.flush();
                                                                session.clear();
                                                                session.close();
                                                            }
                                                        }
                                                    }
                                                    else
                                                    {
                                                        buscaCuentas(-1,-1);
                                                        JOptionPane.showMessageDialog(null, "La partida ya no existe");
                                                    }
                                                }
                                                else
                                                {
                                                   JOptionPane.showMessageDialog(null, "No se pueden modificar partidas enlazadas de otra orden");
                                                }
                                            }
                                            else
                                            {
                                                session.getTransaction().rollback();
                                                JOptionPane.showMessageDialog(null, "¡Acceso denegado!");
                                            }
                                        }
                                        catch(Exception e)
                                        {
                                            session.getTransaction().rollback();
                                            System.out.println(e);
                                        }
                                        if(session!=null)
                                            if(session.isOpen()==true)
                                            {
                                                session.flush();
                                                session.clear();
                                                session.close();
                                            }
                                    }
                                    break;
                                
                            case 26://Int Rep Med
                                    if(vector.get(col)==null || habilita==true)
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
                                            user = (Usuario)session.get(Usuario.class, user.getIdUsuario());
                                            if(user.getEditarLevantamiento()==true || (((String)t_datos.getValueAt(t_datos.getSelectedRow(), 32)).compareToIgnoreCase("o")==0 && user.getAValuacionLevantamiento()==true && r_cerrar.isSelected()==false) || (((String)t_datos.getValueAt(t_datos.getSelectedRow(), 32)).compareToIgnoreCase("o")!=0 && user.getAValuacionLevantamiento()==true) )
                                            {
                                                if(t_datos.getValueAt(row, 32).toString().compareTo("e")!=0)
                                                {
                                                    Partida part=(Partida) session.createCriteria(Partida.class).add(Restrictions.eq("ordenByIdOrden.idOrden", Integer.parseInt(orden))).add(Restrictions.eq("idEvaluacion", Integer.parseInt(t_datos.getValueAt(t_datos.getSelectedRow(), 0).toString()))).add(Restrictions.eq("subPartida", Integer.parseInt(t_datos.getValueAt(t_datos.getSelectedRow(), 1).toString()))).setMaxResults(1).uniqueResult();
                                                    if(part!=null)
                                                    {
                                                        if(value.toString().compareTo("true")==0)
                                                        {
                                                            if(part.getPedido()==null)
                                                            {
                                                                part.setIntRepMin(-1);
                                                                part.setIntRepMed(0);
                                                                part.setIntRepMax(-1);

                                                                part.setIntCamb(-1);
                                                                part.setRefComp(false);
                                                                part.setAutorizado(false);
                                                                
                                                                session.update(part);
                                                                session.getTransaction().commit();
                                                                vector.setElementAt(value, col);
                                                                this.dataVector.setElementAt(vector, row);
                                                                fireTableCellUpdated(row, col);
                                                                
                                                                habilita=true;
                                                                t_datos.setValueAt(false, row, 25);//Int Rep min
                                                                t_datos.setValueAt(false, row, 27);//Int Rep Max
                                                                t_datos.setValueAt(false, row, 24);//Int cam
                                                                habilita=false;
                                                                
                                                                if(session.isOpen()==true)
                                                                {
                                                                    session.flush();
                                                                    session.clear();
                                                                    session.close();
                                                                }
                                                            }
                                                            else
                                                                JOptionPane.showMessageDialog(null, "La partida ya fue pedida");
                                                        }
                                                        else
                                                        {
                                                            part.setIntRepMed(-1);
                                                            session.update(part);
                                                            session.getTransaction().commit();
                                                            vector.setElementAt(value, col);
                                                            this.dataVector.setElementAt(vector, row);
                                                            fireTableCellUpdated(row, col);
                                                            if(session.isOpen()==true)
                                                            {
                                                                session.flush();
                                                                session.clear();
                                                                session.close();
                                                            }
                                                        }
                                                    }
                                                    else
                                                    {
                                                        buscaCuentas(-1,-1);
                                                        JOptionPane.showMessageDialog(null, "La partida ya no existe");
                                                    }
                                                }
                                                else
                                                {
                                                   JOptionPane.showMessageDialog(null, "No se pueden modificar partidas enlazadas de otra orden");
                                                }
                                            }
                                            else
                                            {
                                                session.getTransaction().rollback();
                                                JOptionPane.showMessageDialog(null, "¡Acceso denegado!");
                                            }
                                        }
                                        catch(Exception e)
                                        {
                                            session.getTransaction().rollback();
                                            System.out.println(e);
                                        }
                                        if(session != null)
                                            if(session.isOpen()==true)
                                            {
                                                session.flush();
                                                session.clear();
                                                session.close();
                                            }
                                    }
                                    break;
                                
                            case 27://Int Rep Max
                                    if(vector.get(col)==null || habilita==true)
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
                                            user = (Usuario)session.get(Usuario.class, user.getIdUsuario());
                                            if(user.getEditarLevantamiento()==true || (((String)t_datos.getValueAt(t_datos.getSelectedRow(), 32)).compareToIgnoreCase("o")==0 && user.getAValuacionLevantamiento()==true && r_cerrar.isSelected()==false) || (((String)t_datos.getValueAt(t_datos.getSelectedRow(), 32)).compareToIgnoreCase("o")!=0 && user.getAValuacionLevantamiento()==true) )
                                            {
                                                if(t_datos.getValueAt(row, 32).toString().compareTo("e")!=0)
                                                {
                                                    Partida part=(Partida) session.createCriteria(Partida.class).add(Restrictions.eq("ordenByIdOrden.idOrden", Integer.parseInt(orden))).add(Restrictions.eq("idEvaluacion", Integer.parseInt(t_datos.getValueAt(t_datos.getSelectedRow(), 0).toString()))).add(Restrictions.eq("subPartida", Integer.parseInt(t_datos.getValueAt(t_datos.getSelectedRow(), 1).toString()))).setMaxResults(1).uniqueResult();
                                                    if(part!=null)
                                                    {
                                                        if(value.toString().compareTo("true")==0)
                                                        {   
                                                            if(part.getPedido()==null)
                                                            {
                                                                part.setIntRepMin(-1);
                                                                part.setIntRepMed(-1);
                                                                part.setIntRepMax(0);

                                                                part.setIntCamb(-1);
                                                                part.setRefComp(false);
                                                                part.setAutorizado(false);
                                                                
                                                                session.update(part);
                                                                session.getTransaction().commit();
                                                                vector.setElementAt(value, col);
                                                                this.dataVector.setElementAt(vector, row);
                                                                fireTableCellUpdated(row, col);
                                                                
                                                                habilita=true;
                                                                t_datos.setValueAt(false, row, 25);//Int Rep min
                                                                t_datos.setValueAt(false, row, 26);//Int Rep Med
                                                                t_datos.setValueAt(false, row, 24);//Int cam
                                                                habilita=false;
                                                                
                                                                if(session.isOpen()==true)
                                                                    session.close();
                                                            }
                                                            else
                                                                JOptionPane.showMessageDialog(null, "La partida ya fue pedida");
                                                        }
                                                        else
                                                        {
                                                            part.setIntRepMax(-1);
                                                            session.update(part);
                                                            session.getTransaction().commit();
                                                            vector.setElementAt(value, col);
                                                            this.dataVector.setElementAt(vector, row);
                                                            fireTableCellUpdated(row, col);
                                                            if(session.isOpen()==true)
                                                            {
                                                                session.flush();
                                                                session.clear();
                                                                session.close();
                                                            }
                                                        }
                                                    }
                                                    else
                                                    {
                                                        buscaCuentas(-1,-1);
                                                        JOptionPane.showMessageDialog(null, "La partida ya no existe");
                                                    }
                                                }
                                                else
                                                {
                                                   JOptionPane.showMessageDialog(null, "No se pueden modificar partidas enlazadas de otra orden");
                                                }
                                            }
                                            else
                                            {
                                                session.getTransaction().rollback();
                                                JOptionPane.showMessageDialog(null, "¡Acceso denegado!");
                                            }
                                        }
                                        catch(Exception e)
                                        {
                                            session.getTransaction().rollback();
                                            System.out.println(e);
                                        }
                                        if(session!=null)
                                            if(session.isOpen()==true)
                                            {
                                                session.flush();
                                                session.clear();
                                                session.close();
                                            }
                                    }
                                    break;
                                
                            case 28://Pin min
                                    if(vector.get(col)==null || habilita==true)
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
                                            user = (Usuario)session.get(Usuario.class, user.getIdUsuario());
                                            if(user.getEditarLevantamiento()==true || (((String)t_datos.getValueAt(t_datos.getSelectedRow(), 32)).compareToIgnoreCase("o")==0 && user.getAValuacionLevantamiento()==true && r_cerrar.isSelected()==false) || (((String)t_datos.getValueAt(t_datos.getSelectedRow(), 32)).compareToIgnoreCase("o")!=0 && user.getAValuacionLevantamiento()==true) )
                                            {
                                                if(t_datos.getValueAt(row, 32).toString().compareTo("e")!=0)
                                                {
                                                    Partida part=(Partida) session.createCriteria(Partida.class).add(Restrictions.eq("ordenByIdOrden.idOrden", Integer.parseInt(orden))).add(Restrictions.eq("idEvaluacion", Integer.parseInt(t_datos.getValueAt(t_datos.getSelectedRow(), 0).toString()))).add(Restrictions.eq("subPartida", Integer.parseInt(t_datos.getValueAt(t_datos.getSelectedRow(), 1).toString()))).setMaxResults(1).uniqueResult();
                                                    if(part!=null)
                                                    {
                                                        if(value.toString().compareTo("true")==0)
                                                        {
                                                            part.setIntPinMin(0);
                                                            part.setIntPinMed(-1);
                                                            part.setIntPinMax(-1);
                                                            
                                                            session.update(part);
                                                            session.getTransaction().commit();
                                                            vector.setElementAt(value, col);
                                                            this.dataVector.setElementAt(vector, row);
                                                            fireTableCellUpdated(row, col);
                                                            
                                                            habilita=true;
                                                            t_datos.setValueAt(false, row, 29);//Pin med
                                                            t_datos.setValueAt(false, row, 30);//Pin max
                                                            habilita=false;
                                                            
                                                            if(session.isOpen()==true)
                                                            {
                                                                session.flush();
                                                                session.clear();
                                                                session.close();
                                                            }
                                                        }
                                                        else
                                                        {
                                                            part.setIntPinMin(-1);
                                                            session.update(part);
                                                            session.getTransaction().commit();
                                                            vector.setElementAt(value, col);
                                                            this.dataVector.setElementAt(vector, row);
                                                            fireTableCellUpdated(row, col);
                                                            if(session.isOpen()==true)
                                                            {
                                                                session.flush();
                                                                session.clear();
                                                                session.close();
                                                            }
                                                        }
                                                    }
                                                    else
                                                    {
                                                        buscaCuentas(-1,-1);
                                                        JOptionPane.showMessageDialog(null, "La partida ya no existe");
                                                    }
                                                }
                                                else
                                                {
                                                   JOptionPane.showMessageDialog(null, "No se pueden modificar partidas enlazadas de otra orden");
                                                }
                                            }
                                            else
                                            {
                                                session.getTransaction().rollback();
                                                JOptionPane.showMessageDialog(null, "¡Acceso denegado!");
                                            }
                                        }
                                        catch(Exception e)
                                        {
                                            session.getTransaction().rollback();
                                            System.out.println(e);
                                        }
                                        if(session!=null)
                                            if(session.isOpen()==true)
                                            {
                                                session.flush();
                                                session.clear();
                                                session.close();
                                            }
                                    }
                                    break;
                                
                            case 29://Pin med
                                    if(vector.get(col)==null || habilita==true)
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
                                            user = (Usuario)session.get(Usuario.class, user.getIdUsuario());
                                            if(user.getEditarLevantamiento()==true || (((String)t_datos.getValueAt(t_datos.getSelectedRow(), 32)).compareToIgnoreCase("o")==0 && user.getAValuacionLevantamiento()==true && r_cerrar.isSelected()==false) || (((String)t_datos.getValueAt(t_datos.getSelectedRow(), 32)).compareToIgnoreCase("o")!=0 && user.getAValuacionLevantamiento()==true) )
                                            {
                                                if(t_datos.getValueAt(row, 32).toString().compareTo("e")!=0)
                                                {
                                                    Partida part=(Partida) session.createCriteria(Partida.class).add(Restrictions.eq("ordenByIdOrden.idOrden", Integer.parseInt(orden))).add(Restrictions.eq("idEvaluacion", Integer.parseInt(t_datos.getValueAt(t_datos.getSelectedRow(), 0).toString()))).add(Restrictions.eq("subPartida", Integer.parseInt(t_datos.getValueAt(t_datos.getSelectedRow(), 1).toString()))).setMaxResults(1).uniqueResult();
                                                    if(part!=null)
                                                    {
                                                        if(value.toString().compareTo("true")==0)
                                                        {
                                                            part.setIntPinMin(-1);
                                                            part.setIntPinMed(0);
                                                            part.setIntPinMax(-1);
                                                            session.update(part);
                                                            session.getTransaction().commit();
                                                            vector.setElementAt(value, col);
                                                            this.dataVector.setElementAt(vector, row);
                                                            fireTableCellUpdated(row, col);
                                                            
                                                            habilita=true;
                                                            t_datos.setValueAt(false, row, 28);//Pin min
                                                            t_datos.setValueAt(false, row, 30);//Pin max
                                                            habilita=false;
                                                            
                                                            if(session.isOpen()==true)
                                                                session.close();
                                                        }
                                                        else
                                                        {
                                                            part.setIntPinMed(-1);
                                                            session.update(part);
                                                            session.getTransaction().commit();
                                                            vector.setElementAt(value, col);
                                                            this.dataVector.setElementAt(vector, row);
                                                            fireTableCellUpdated(row, col);
                                                            if(session.isOpen()==true)
                                                            {
                                                                session.flush();
                                                                session.clear();
                                                                session.close();
                                                            }
                                                        }
                                                    }
                                                    else
                                                    {
                                                        buscaCuentas(-1,-1);
                                                        JOptionPane.showMessageDialog(null, "La partida ya no existe");
                                                    }
                                                }
                                                else
                                                {
                                                   JOptionPane.showMessageDialog(null, "No se pueden modificar partidas enlazadas de otra orden");
                                                }
                                            }
                                            else
                                            {
                                                session.getTransaction().rollback();
                                                JOptionPane.showMessageDialog(null, "¡Acceso denegado!");
                                            }
                                        }
                                        catch(Exception e)
                                        {
                                            session.getTransaction().rollback();
                                            System.out.println(e);
                                        }
                                        if(session!=null)
                                            if(session.isOpen()==true)
                                            {
                                                session.flush();
                                                session.clear();
                                                session.close();
                                            }
                                    }
                                    break;
                                
                            case 30://Pin max
                                    if(vector.get(col)==null || habilita==true)
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
                                            user = (Usuario)session.get(Usuario.class, user.getIdUsuario());
                                            if(user.getEditarLevantamiento()==true || (((String)t_datos.getValueAt(t_datos.getSelectedRow(), 32)).compareToIgnoreCase("o")==0 && user.getAValuacionLevantamiento()==true && r_cerrar.isSelected()==false) || (((String)t_datos.getValueAt(t_datos.getSelectedRow(), 32)).compareToIgnoreCase("o")!=0 && user.getAValuacionLevantamiento()==true) )
                                            {
                                                if(t_datos.getValueAt(row, 32).toString().compareTo("e")!=0)
                                                {
                                                    Partida part=(Partida) session.createCriteria(Partida.class).add(Restrictions.eq("ordenByIdOrden.idOrden", Integer.parseInt(orden))).add(Restrictions.eq("idEvaluacion", Integer.parseInt(t_datos.getValueAt(t_datos.getSelectedRow(), 0).toString()))).add(Restrictions.eq("subPartida", Integer.parseInt(t_datos.getValueAt(t_datos.getSelectedRow(), 1).toString()))).setMaxResults(1).uniqueResult();
                                                    if(part!=null)
                                                    {
                                                        if(value.toString().compareTo("true")==0)
                                                        {
                                                            part.setIntPinMin(-1);
                                                            part.setIntPinMed(-1);
                                                            part.setIntPinMax(0);
                                                            
                                                            session.update(part);
                                                            session.getTransaction().commit();
                                                            vector.setElementAt(value, col);
                                                            this.dataVector.setElementAt(vector, row);
                                                            fireTableCellUpdated(row, col);
                                                            
                                                            habilita=true;
                                                            t_datos.setValueAt(false, row, 28);//Pin min
                                                            t_datos.setValueAt(false, row, 29);//Pin med
                                                            habilita=false;
                                                            
                                                            if(session.isOpen()==true)
                                                            {
                                                                session.flush();
                                                                session.clear();
                                                                session.close();
                                                            }
                                                        }
                                                        else
                                                        {
                                                            part.setIntPinMax(-1);
                                                            session.update(part);
                                                            session.getTransaction().commit();
                                                            vector.setElementAt(value, col);
                                                            this.dataVector.setElementAt(vector, row);
                                                            fireTableCellUpdated(row, col);
                                                            if(session.isOpen()==true)
                                                            {
                                                                session.flush();
                                                                session.clear();
                                                                session.close();
                                                            }
                                                        }
                                                    }
                                                    else
                                                    {
                                                        buscaCuentas(-1,-1);
                                                        JOptionPane.showMessageDialog(null, "La partida ya no existe");
                                                    }
                                                }
                                                else
                                                {
                                                   JOptionPane.showMessageDialog(null, "No se pueden modificar partidas enlazadas de otra orden");
                                                }
                                            }
                                            else
                                            {
                                                session.getTransaction().rollback();
                                                JOptionPane.showMessageDialog(null, "¡Acceso denegado!");
                                            }
                                        }
                                        catch(Exception e)
                                        {
                                            session.getTransaction().rollback();
                                            System.out.println(e);
                                        }
                                        if(session!=null)
                                            if(session.isOpen()==true)
                                            {
                                                session.flush();
                                                session.clear();
                                                session.close();
                                            }
                                    }
                                    break;
                                
                            case 31://instruccion
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
                                            user = (Usuario)session.get(Usuario.class, user.getIdUsuario());
                                            if(user.getEditarLevantamiento()==true || (((String)t_datos.getValueAt(t_datos.getSelectedRow(), 32)).compareToIgnoreCase("o")==0 && user.getAValuacionLevantamiento()==true && r_cerrar.isSelected()==false) || (((String)t_datos.getValueAt(t_datos.getSelectedRow(), 32)).compareToIgnoreCase("o")!=0 && user.getAValuacionLevantamiento()==true) )
                                            {
                                                if(t_datos.getValueAt(row, 32).toString().compareTo("e")!=0)
                                                {
                                                    Partida part=(Partida) session.createCriteria(Partida.class).add(Restrictions.eq("ordenByIdOrden.idOrden", Integer.parseInt(orden))).add(Restrictions.eq("idEvaluacion", Integer.parseInt(t_datos.getValueAt(t_datos.getSelectedRow(), 0).toString()))).add(Restrictions.eq("subPartida", Integer.parseInt(t_datos.getValueAt(t_datos.getSelectedRow(), 1).toString()))).setMaxResults(1).uniqueResult();
                                                    if(part!=null)
                                                    {
                                                        if(value.toString().compareTo("")==0)
                                                            part.setInstruccion(null);
                                                        else
                                                            part.setInstruccion(value.toString());
                                                        session.update(part);
                                                        session.getTransaction().commit();
                                                        vector.setElementAt(value, col);
                                                        this.dataVector.setElementAt(vector, row);
                                                        fireTableCellUpdated(row, col);
                                                        if(session.isOpen()==true)
                                                        {
                                                            session.flush();
                                                            session.clear();
                                                            session.close();
                                                        }
                                                    }
                                                    else
                                                    {
                                                        buscaCuentas(-1,-1);
                                                        JOptionPane.showMessageDialog(null, "La partida ya no existe");
                                                    }
                                                }
                                                else
                                                {
                                                   JOptionPane.showMessageDialog(null, "No se pueden modificar Partidas enlazadas de otra orden");
                                                }
                                            }
                                            else
                                            {
                                                session.getTransaction().rollback();
                                                JOptionPane.showMessageDialog(null, "¡Acceso denegado!");
                                            }
                                        }
                                        catch(Exception e)
                                        {
                                            session.getTransaction().rollback();
                                            System.out.println(e);
                                        }
                                        if(session!=null)
                                            if(session.isOpen()==true)
                                            {
                                                session.flush();
                                                session.clear();
                                                session.close();
                                            }
                                    }
                                    break;

                            case 34:
                                    if(vector.get(col)==null)
                                    {
                                        vector.setElementAt(value, col);
                                        this.dataVector.setElementAt(vector, row);
                                        fireTableCellUpdated(row, col);
                                    }
                                    else
                                    {
                                        if((user.getEditarLevantamiento()==true || (((String)t_datos.getValueAt(t_datos.getSelectedRow(), 32)).compareToIgnoreCase("o")==0 && user.getAValuacionLevantamiento()==true && r_cerrar.isSelected()==false) || (((String)t_datos.getValueAt(t_datos.getSelectedRow(), 32)).compareToIgnoreCase("o")!=0 && user.getAValuacionLevantamiento()==true) ) && (t_datos.getValueAt(row, 32).toString().compareTo("e")!=0))
                                        {
                                            Session session = HibernateUtil.getSessionFactory().openSession();
                                            try
                                            {
                                                session.beginTransaction().begin();
                                                user = (Usuario)session.get(Usuario.class, user.getIdUsuario());
                                                if(user.getAValuacionLevantamiento()==true)
                                                {
                                                    Partida part=(Partida) session.createCriteria(Partida.class).add(Restrictions.eq("ordenByIdOrden.idOrden", Integer.parseInt(orden))).add(Restrictions.eq("idEvaluacion", Integer.parseInt(t_datos.getValueAt(t_datos.getSelectedRow(), 0).toString()))).add(Restrictions.eq("subPartida", Integer.parseInt(t_datos.getValueAt(t_datos.getSelectedRow(), 1).toString()))).setMaxResults(1).uniqueResult();
                                                    if(part!=null)
                                                    {
                                                        if(value.toString().compareTo("true")==0)
                                                        {
                                                            part.setAutorizadoValuacion(true);
                                                            session.update(part);
                                                            session.getTransaction().commit();
                                                            vector.setElementAt(value, col);
                                                            this.dataVector.setElementAt(vector, row);
                                                            fireTableCellUpdated(row, col);
                                                            if(session.isOpen()==true)
                                                            {
                                                                session.flush();
                                                                session.clear();
                                                                session.close();
                                                            }
                                                        }
                                                        else
                                                        {
                                                            if(part.getPedido()==null)
                                                            {
                                                                part.setAutorizadoValuacion(false);
                                                                session.update(part);
                                                                session.getTransaction().commit();
                                                                vector.setElementAt(value, col);
                                                                this.dataVector.setElementAt(vector, row);
                                                                fireTableCellUpdated(row, col);
                                                            }
                                                            else
                                                                JOptionPane.showMessageDialog(null, "La partida ya fue pedida");
                                                            if(session.isOpen()==true)
                                                            {
                                                                session.flush();
                                                                session.clear();
                                                                session.close();
                                                            }
                                                        }
                                                    }
                                                    else
                                                    {
                                                        buscaCuentas(-1,-1);
                                                        JOptionPane.showMessageDialog(null, "La partida ya no existe");
                                                    }
                                                }
                                                else
                                                {
                                                    JOptionPane.showMessageDialog(null, "¡Acceso denegado!");
                                                }
                                            }
                                            catch(Exception e)
                                            {
                                                session.getTransaction().rollback();
                                                System.out.println(e);
                                            }
                                            if(session!=null)
                                                if(session.isOpen()==true)
                                                {
                                                    session.flush();
                                                    session.clear();
                                                    session.close();
                                                }
                                        }
                                        else
                                        {
                                           JOptionPane.showMessageDialog(null, "¡Acceso denegado!");
                                        }
                                    }
                                    break;
                                
                            default:
                                    vector.setElementAt(value, col);
                                    this.dataVector.setElementAt(vector, row);
                                    fireTableCellUpdated(row, col);
                                    break;
                        }
        }
        
        public Class getColumnClass(int columnIndex) 
        {
            return types [columnIndex];
        }

        public boolean isCellEditable(int rowIndex, int columnIndex) 
        {
            return this.celdaEditable[ columnIndex ] [ rowIndex ];
        }
                
        public void setCeldaEditable(int fila, int columna,  boolean editable)
        {
            this.celdaEditable[ columna ][ fila ] = editable;
        }

    
        public void setColumnaEditable(int columna, boolean editable)
        {
            int i = 0;
            int cantidadFilas = this.getRowCount();
            for(i=0; i<celdaEditable[columna].length; i++)
                this.celdaEditable[ columna ][ i ] = editable;
        }
        
    }
    
    public void servicios()
    {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try
        {
            inicio=false;
            session.beginTransaction();
            Query q = session.createQuery("from Servicio");
            List servicios = q.list();
            c_servicio.removeAllItems();
            c_servicio.addItem("SERVICIOS");
            for(int p=0; p<servicios.size(); p++)
            {
                Servicio ser=(Servicio)servicios.get(p);
                c_servicio.addItem(ser.getIdServicio());
            }
            session.getTransaction().commit();
            inicio=true;
        }catch(Exception e)
        {
            e.printStackTrace();
            session.getTransaction().rollback();
        }
        finally
        {
            if(session.isOpen()==true)
            {
                session.flush();
                session.clear();
                session.close();
            }
        }
    }
    int buscaTabla(int id)
    {
        for(int r=0; r<t_datos.getRowCount(); r++)
        {
            if(t_datos.getValueAt(r, 15).toString().compareTo(""+id)==0)
                return r;
        }
        return -1;
    }
}
