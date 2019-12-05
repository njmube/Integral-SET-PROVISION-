/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Valuacion;

import Ejemplar.altaEjemplar;
import Ejemplar.buscaEjemplar;
import Hibernate.Util.HibernateUtil;
import Hibernate.entidades.Almacen;
import Hibernate.entidades.Catalogo;
import Hibernate.entidades.Configuracion;
import Hibernate.entidades.Ejemplar;
import Hibernate.entidades.Foto;
import Hibernate.entidades.Item;
import Hibernate.entidades.Movimiento;
import Hibernate.entidades.Orden;
import Hibernate.entidades.Partida;
import Hibernate.entidades.Proveedor;
import Hibernate.entidades.Reparacion;
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
import Integral.Ftp;
import Integral.Herramientas;
import Integral.HorizontalBarUI;
import Integral.PDF;
import Integral.VerticalBarUI;
import Integral.VerticalTableHeaderCellRenderer;
import java.awt.Point;
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
    private String edo="";
    
    String[] columnas = new String [] {
        "No","#",
        "Esp Hoj","Esp Mec","Esp Sus","Esp Ele",
        "DM","Cam",
        "Rep Min","Rep Med","Rep Max","Pin",
        "Can","Med","Descripción","Fol","Codigo","Freeze", "PD","TOT","I. DM","Camb","Rep Min","Rep Med","Rep Max","Pin Min","Pin Med","Pin Max","Instrucción", "Tipo", "Orden", "☺", "A. Val"};
    int configuracion=1;
    String rutaFTP;
    
    public valuacionDirecta(String ord, Usuario us, String edo, String ses, int configuracion, String rutaFTP) {
        initComponents();
        this.rutaFTP = rutaFTP;
        this.edo=edo;
        scroll.getVerticalScrollBar().setUI(new VerticalBarUI());
        scroll.getHorizontalScrollBar().setUI(new HorizontalBarUI());
        sessionPrograma=ses;
        this.configuracion=configuracion;
        orden=ord;
        user=us;
        t_datos.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        model=new MyModel(0, columnas);
        t_datos.setModel(model);
        formato = new FormatoTabla();
        formatoTabla();
        servicios();
        buscaCuentas(-1,-1);
        SumaTotal();
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
                      DefaultCellEditor miEditor = new DefaultCellEditor(numeros);
                      miEditor.setClickCountToStart(2);
                      column.setCellEditor(miEditor);
                      //column.setCellRenderer(tcr);
                      break;
                  case 19:
                      column.setPreferredWidth(50);
                      break;
                  case 28:
                      column.setPreferredWidth(300);
                      column.setCellEditor(new DefaultCellEditor(instruccion)); 
                      break;
                  case 30:
                      column.setPreferredWidth(80);
                      break; 
                  case 31:
                      column.setPreferredWidth(20);
                      DefaultCellEditor miEditor1 = new DefaultCellEditor(cb_prioridad);
                      miEditor1.setClickCountToStart(2);
                      column.setCellEditor(miEditor1);
                      //column.setCellRenderer(tcr);
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
        cb_prioridad = new javax.swing.JComboBox();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        t_busca = new javax.swing.JTextField();
        b_busca = new javax.swing.JButton();
        b_mas_sup_partida = new javax.swing.JButton();
        b_tot = new javax.swing.JButton();
        b_mas_partida = new javax.swing.JButton();
        b_menos = new javax.swing.JButton();
        cb_tipo_partida = new javax.swing.JComboBox();
        t_total_consumible = new javax.swing.JFormattedTextField();
        b_consumibles = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        scroll = new javax.swing.JScrollPane();
        t_datos = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        c_plantilla = new javax.swing.JComboBox();
        jLabel2 = new javax.swing.JLabel();
        c_filtro = new javax.swing.JComboBox();
        b_pdf = new javax.swing.JButton();
        b_exel = new javax.swing.JButton();
        r_cerrar = new javax.swing.JRadioButton();

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
        numeros.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Cancelar", "Eliminar", "Buscar", "Nuevo" }));

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

        cb_prioridad.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "1", "2", "3", "4" }));

        setBackground(new java.awt.Color(255, 255, 255));
        setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        setLayout(new java.awt.BorderLayout());

        jPanel1.setBackground(new java.awt.Color(2, 135, 242));
        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Arial", 0, 9)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Buscar:");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(235, 8, -1, -1));

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
        jPanel1.add(t_busca, new org.netbeans.lib.awtextra.AbsoluteConstraints(275, 2, 210, -1));

        b_busca.setIcon(new ImageIcon("imagenes/buscar1.png"));
        b_busca.setToolTipText("Busca una partida");
        b_busca.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_buscaActionPerformed(evt);
            }
        });
        jPanel1.add(b_busca, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 2, 23, 23));

        b_mas_sup_partida.setIcon(new ImageIcon("imagenes/boton_mas_#.png"));
        b_mas_sup_partida.setMnemonic('2');
        b_mas_sup_partida.setToolTipText("Agrega una complementaria (ALT+2)");
        b_mas_sup_partida.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_mas_sup_partidaActionPerformed(evt);
            }
        });
        jPanel1.add(b_mas_sup_partida, new org.netbeans.lib.awtextra.AbsoluteConstraints(29, 2, 23, 23));

        b_tot.setIcon(new ImageIcon("imagenes/boton_mas_PROV.png"));
        b_tot.setToolTipText("Trabajo en taller externo");
        b_tot.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_totActionPerformed(evt);
            }
        });
        jPanel1.add(b_tot, new org.netbeans.lib.awtextra.AbsoluteConstraints(840, 0, 23, 23));

        b_mas_partida.setIcon(new ImageIcon("imagenes/boton_mas_n.png"));
        b_mas_partida.setMnemonic('1');
        b_mas_partida.setToolTipText("Agrega una partida (ALT+1)");
        b_mas_partida.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_mas_partidaActionPerformed(evt);
            }
        });
        jPanel1.add(b_mas_partida, new org.netbeans.lib.awtextra.AbsoluteConstraints(2, 2, 23, 23));

        b_menos.setIcon(new ImageIcon("imagenes/boton_menos.png"));
        b_menos.setToolTipText("Elimina la partida seleccionada");
        b_menos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_menosActionPerformed(evt);
            }
        });
        jPanel1.add(b_menos, new org.netbeans.lib.awtextra.AbsoluteConstraints(185, 2, 23, 23));

        cb_tipo_partida.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        cb_tipo_partida.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Ordinaria" }));
        cb_tipo_partida.setBorder(null);
        jPanel1.add(cb_tipo_partida, new org.netbeans.lib.awtextra.AbsoluteConstraints(58, 4, 120, -1));

        t_total_consumible.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        t_total_consumible.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.00"))));
        t_total_consumible.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        t_total_consumible.setText("0.00");
        t_total_consumible.setDisabledTextColor(new java.awt.Color(2, 38, 253));
        t_total_consumible.setEnabled(false);
        t_total_consumible.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        jPanel1.add(t_total_consumible, new org.netbeans.lib.awtextra.AbsoluteConstraints(625, 3, 70, -1));

        b_consumibles.setBackground(new java.awt.Color(2, 135, 242));
        b_consumibles.setIcon(new ImageIcon("imagenes/calendario.png"));
        b_consumibles.setToolTipText("Calendario");
        b_consumibles.setMaximumSize(new java.awt.Dimension(32, 8));
        b_consumibles.setMinimumSize(new java.awt.Dimension(32, 8));
        b_consumibles.setPreferredSize(new java.awt.Dimension(32, 8));
        b_consumibles.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_consumiblesActionPerformed(evt);
            }
        });
        jPanel1.add(b_consumibles, new org.netbeans.lib.awtextra.AbsoluteConstraints(710, 2, -1, 20));

        jLabel3.setText("Consumibles");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 5, -1, -1));

        add(jPanel1, java.awt.BorderLayout.PAGE_END);

        scroll.setBorder(null);
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
            public void keyReleased(java.awt.event.KeyEvent evt) {
                t_datosKeyReleased(evt);
            }
        });
        scroll.setViewportView(t_datos);

        add(scroll, java.awt.BorderLayout.CENTER);

        jPanel2.setBackground(new java.awt.Color(2, 135, 242));
        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jPanel3.setBackground(new java.awt.Color(2, 135, 242));
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        c_plantilla.setFont(new java.awt.Font("Arial", 0, 10)); // NOI18N
        c_plantilla.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "PLANTILLAS" }));
        c_plantilla.setBorder(null);
        c_plantilla.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                c_plantillaItemStateChanged(evt);
            }
        });
        c_plantilla.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                c_plantillaActionPerformed(evt);
            }
        });
        jPanel3.add(c_plantilla, new org.netbeans.lib.awtextra.AbsoluteConstraints(2, 2, 186, -1));

        jLabel2.setFont(new java.awt.Font("Arial", 0, 9)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Reporte:");
        jPanel3.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 5, -1, -1));

        c_filtro.setFont(new java.awt.Font("Tahoma", 0, 9)); // NOI18N
        c_filtro.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Todos", "Hojalateria", "Mecanica", "Suspension", "Electricidad", "Pintura" }));
        jPanel3.add(c_filtro, new org.netbeans.lib.awtextra.AbsoluteConstraints(265, 2, 100, -1));

        b_pdf.setIcon(new ImageIcon("imagenes/pdf_icon.png"));
        b_pdf.setToolTipText("Exporta a PDF");
        b_pdf.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_pdfActionPerformed(evt);
            }
        });
        jPanel3.add(b_pdf, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 0, 23, 23));

        b_exel.setIcon(new ImageIcon("imagenes/xls_icon.png"));
        b_exel.setToolTipText("Exporta a EXCEL");
        b_exel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_exelActionPerformed(evt);
            }
        });
        jPanel3.add(b_exel, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 0, 23, 23));

        r_cerrar.setFont(new java.awt.Font("Tahoma", 0, 9)); // NOI18N
        r_cerrar.setForeground(new java.awt.Color(255, 255, 255));
        r_cerrar.setText("Cerrar y enviar a valuación");
        r_cerrar.setToolTipText("Al marcar esta casilla el levantamiento se cerrara.");
        r_cerrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                r_cerrarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 449, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 454, Short.MAX_VALUE)
                .addComponent(r_cerrar)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(r_cerrar)
        );

        add(jPanel2, java.awt.BorderLayout.PAGE_START);
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
            buscaProveedor obj = new buscaProveedor(new javax.swing.JFrame(), true, this.user, this.sessionPrograma, 0);
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
                        if(user.getEditarLevantamiento()==true || (r_cerrar.isSelected()==false && (boolean)t_datos.getValueAt(t_datos.getSelectedRow(), 32)==false))
                        {
                            if(t_datos.getValueAt(t_datos.getSelectedRow(), 29).toString().compareTo("e")!=0)
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
                    if(user.getEditarLevantamiento()==true || (r_cerrar.isSelected()==false && (boolean)t_datos.getValueAt(t_datos.getSelectedRow(), 32)==false))
                    {
                        if(t_datos.getValueAt(t_datos.getSelectedRow(), 29).toString().compareTo("e")!=0)
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
                    Orden aux_orden=null;
                    try
                    {
                        user = (Usuario)session.get(Usuario.class, user.getIdUsuario());
                        if(this.cb_tipo_partida.getSelectedItem().toString().compareTo("Ordinaria")==0)
                        {
                            if(user.getOrdinariaLevantamiento()==true  && (user.getEditarLevantamiento()==true || r_cerrar.isSelected()==false))
                            {
                                nuevaPartida(1, "o", -1);
                                aux_orden=(Orden)session.get(Orden.class, Integer.parseInt(orden));
                                String opcion=aux_orden.getReparacion().getNombre();
                                if(opcion.compareToIgnoreCase("EXPRESS")==0 || opcion.compareToIgnoreCase("CHICO")==0 || opcion.compareToIgnoreCase("MEDIANO")==0 || opcion.compareToIgnoreCase("GRANDE")==0)
                                {
                                    int num_partidas=0;
                                    if(aux_orden.getPartidasForIdOrden()!=null)
                                        num_partidas=aux_orden.getPartidasForIdOrden().size();
                                    if(num_partidas<=10){//EXPRESS
                                        Reparacion re = (Reparacion)session.createCriteria(Reparacion.class).add(Restrictions.eq("nombre", "EXPRESS")).setMaxResults(1).uniqueResult();
                                        aux_orden.setReparacion(re);
                                    }
                                    else{
                                        if(num_partidas<=40){//CHICO
                                            Reparacion re = (Reparacion)session.createCriteria(Reparacion.class).add(Restrictions.eq("nombre", "CHICO")).setMaxResults(1).uniqueResult();
                                            aux_orden.setReparacion(re);
                                        }
                                        else{
                                            if(num_partidas<=110){//MEDIANO
                                                Reparacion re = (Reparacion)session.createCriteria(Reparacion.class).add(Restrictions.eq("nombre", "MEDIANO")).setMaxResults(1).uniqueResult();
                                                aux_orden.setReparacion(re);
                                            }
                                            else{//GRANDE
                                                Reparacion re = (Reparacion)session.createCriteria(Reparacion.class).add(Restrictions.eq("nombre", "GRANDE")).setMaxResults(1).uniqueResult();
                                                aux_orden.setReparacion(re);
                                            }
                                        }
                                    }
                                    session.update(aux_orden);
                                }
                            }
                            else
                                JOptionPane.showMessageDialog(null, "¡Acceso denegado!");
                        }

                        if(this.cb_tipo_partida.getSelectedItem().toString().compareTo("Complementaria")==0)
                        {
                            if(user.getComplementariaLevantamiento()==true)
                            {
                                nuevaPartida(1, "c", -1);
                                aux_orden=(Orden)session.get(Orden.class, Integer.parseInt(orden));
                                String opcion=aux_orden.getReparacion().getNombre();
                                if(opcion.compareToIgnoreCase("EXPRESS")==0 || opcion.compareToIgnoreCase("CHICO")==0 || opcion.compareToIgnoreCase("MEDIANO")==0 || opcion.compareToIgnoreCase("GRANDE")==0)
                                {
                                    int num_partidas=0;
                                    if(aux_orden.getPartidasForIdOrden()!=null)
                                        num_partidas=aux_orden.getPartidasForIdOrden().size();
                                    if(num_partidas<=10){//EXPRESS
                                        Reparacion re = (Reparacion)session.createCriteria(Reparacion.class).add(Restrictions.eq("nombre", "EXPRESS")).setMaxResults(1).uniqueResult();
                                        aux_orden.setReparacion(re);
                                    }
                                    else{
                                        if(num_partidas<=40){//CHICO
                                            Reparacion re = (Reparacion)session.createCriteria(Reparacion.class).add(Restrictions.eq("nombre", "CHICO")).setMaxResults(1).uniqueResult();
                                            aux_orden.setReparacion(re);
                                        }
                                        else{
                                            if(num_partidas<=110){//MEDIANO
                                                Reparacion re = (Reparacion)session.createCriteria(Reparacion.class).add(Restrictions.eq("nombre", "MEDIANO")).setMaxResults(1).uniqueResult();
                                                aux_orden.setReparacion(re);
                                            }
                                            else{//GRANDE
                                                Reparacion re = (Reparacion)session.createCriteria(Reparacion.class).add(Restrictions.eq("nombre", "GRANDE")).setMaxResults(1).uniqueResult();
                                                aux_orden.setReparacion(re);
                                            }
                                        }
                                    }
                                    session.update(aux_orden);
                                }
                            }
                            else
                                JOptionPane.showMessageDialog(null, "¡Acceso denegado!");
                        }

                        if(this.cb_tipo_partida.getSelectedItem().toString().compareTo("Adicional")==0)
                        {
                            if(user.getAdicionalLevantamiento()==true)
                            {
                                nuevaPartida(1, "a", -1);
                                aux_orden=(Orden)session.get(Orden.class, Integer.parseInt(orden));
                                String opcion=aux_orden.getReparacion().getNombre();
                                if(opcion.compareToIgnoreCase("EXPRESS")==0 || opcion.compareToIgnoreCase("CHICO")==0 || opcion.compareToIgnoreCase("MEDIANO")==0 || opcion.compareToIgnoreCase("GRANDE")==0)
                                {
                                    int num_partidas=0;
                                    if(aux_orden.getPartidasForIdOrden()!=null)
                                        num_partidas=aux_orden.getPartidasForIdOrden().size();
                                    if(num_partidas<=10){//EXPRESS
                                        Reparacion re = (Reparacion)session.createCriteria(Reparacion.class).add(Restrictions.eq("nombre", "EXPRESS")).setMaxResults(1).uniqueResult();
                                        aux_orden.setReparacion(re);
                                    }
                                    else{
                                        if(num_partidas<=40){//CHICO
                                            Reparacion re = (Reparacion)session.createCriteria(Reparacion.class).add(Restrictions.eq("nombre", "CHICO")).setMaxResults(1).uniqueResult();
                                            aux_orden.setReparacion(re);
                                        }
                                        else{
                                            if(num_partidas<=110){//MEDIANO
                                                Reparacion re = (Reparacion)session.createCriteria(Reparacion.class).add(Restrictions.eq("nombre", "MEDIANO")).setMaxResults(1).uniqueResult();
                                                aux_orden.setReparacion(re);
                                            }
                                            else{//GRANDE
                                                Reparacion re = (Reparacion)session.createCriteria(Reparacion.class).add(Restrictions.eq("nombre", "GRANDE")).setMaxResults(1).uniqueResult();
                                                aux_orden.setReparacion(re);
                                            }
                                        }
                                    }
                                    session.update(aux_orden);
                                }
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
            reporte.Abrir2(PageSize.LETTER.rotate(), "Valuación", "reportes/"+ord.getIdOrden()+"/"+valor+"-levantamiento.pdf");
            Font font = new Font(Font.FontFamily.HELVETICA, 6, Font.BOLD);
            BaseColor contenido=BaseColor.WHITE;
            int centro=Element.ALIGN_CENTER;
            int izquierda=Element.ALIGN_LEFT;
            int derecha=Element.ALIGN_RIGHT;
            float tam[]=new float[]{15,15,7,7,7,7,7,59,125,16,16,16, 12,12, 12,12,12, 12,12,12,100};
            PdfPTable tabla=reporte.crearTabla(21, tam, 100, Element.ALIGN_LEFT);
            
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
                            
                            tabla.addCell(reporte.celda(Part.getOriCon(), font, contenido, izquierda, 0,1,Rectangle.RECTANGLE));

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
            reporte.visualizar2("reportes/"+ord.getIdOrden()+"/"+valor+"-levantamiento.pdf");
            
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
        Orden aux_orden=null;
        try
        {
            user = (Usuario)session.get(Usuario.class, user.getIdUsuario());
            if(this.cb_tipo_partida.getSelectedItem().toString().compareTo("Ordinaria")==0)
            {
                if(user.getOrdinariaLevantamiento()==true && (user.getEditarLevantamiento()==true || r_cerrar.isSelected()==false))
                {
                    nuevaPartida(0, "o", -1);
                    aux_orden=(Orden)session.get(Orden.class, Integer.parseInt(orden));
                    String opcion=aux_orden.getReparacion().getNombre();
                    if(opcion.compareToIgnoreCase("EXPRESS")==0 || opcion.compareToIgnoreCase("CHICO")==0 || opcion.compareToIgnoreCase("MEDIANO")==0 || opcion.compareToIgnoreCase("GRANDE")==0)
                    {
                        int num_partidas=0;
                        if(aux_orden.getPartidasForIdOrden()!=null)
                            num_partidas=aux_orden.getPartidasForIdOrden().size();
                        if(num_partidas<=10){//EXPRESS
                            Reparacion re = (Reparacion)session.createCriteria(Reparacion.class).add(Restrictions.eq("nombre", "EXPRESS")).setMaxResults(1).uniqueResult();
                            aux_orden.setReparacion(re);
                        }
                        else{
                            if(num_partidas<=40){//CHICO
                                Reparacion re = (Reparacion)session.createCriteria(Reparacion.class).add(Restrictions.eq("nombre", "CHICO")).setMaxResults(1).uniqueResult();
                                aux_orden.setReparacion(re);
                            }
                            else{
                                if(num_partidas<=110){//MEDIANO
                                    Reparacion re = (Reparacion)session.createCriteria(Reparacion.class).add(Restrictions.eq("nombre", "MEDIANO")).setMaxResults(1).uniqueResult();
                                    aux_orden.setReparacion(re);
                                }
                                else{//GRANDE
                                    Reparacion re = (Reparacion)session.createCriteria(Reparacion.class).add(Restrictions.eq("nombre", "GRANDE")).setMaxResults(1).uniqueResult();
                                    aux_orden.setReparacion(re);
                                }
                            }
                        }
                        session.update(aux_orden);
                    }
                }
                else
                    JOptionPane.showMessageDialog(null, "¡Acceso denegado!");
            }

            if(this.cb_tipo_partida.getSelectedItem().toString().compareTo("Complementaria")==0)
            {
                if(user.getComplementariaLevantamiento()==true)
                {
                    nuevaPartida(0, "c", -1);
                    aux_orden=(Orden)session.get(Orden.class, Integer.parseInt(orden));
                    String opcion=aux_orden.getReparacion().getNombre();
                    if(opcion.compareToIgnoreCase("EXPRESS")==0 || opcion.compareToIgnoreCase("CHICO")==0 || opcion.compareToIgnoreCase("MEDIANO")==0 || opcion.compareToIgnoreCase("GRANDE")==0)
                    {
                        int num_partidas=0;
                        if(aux_orden.getPartidasForIdOrden()!=null)
                            num_partidas=aux_orden.getPartidasForIdOrden().size();
                        if(num_partidas<=10){//EXPRESS
                            Reparacion re = (Reparacion)session.createCriteria(Reparacion.class).add(Restrictions.eq("nombre", "EXPRESS")).setMaxResults(1).uniqueResult();
                            aux_orden.setReparacion(re);
                        }
                        else{
                            if(num_partidas<=40){//CHICO
                                Reparacion re = (Reparacion)session.createCriteria(Reparacion.class).add(Restrictions.eq("nombre", "CHICO")).setMaxResults(1).uniqueResult();
                                aux_orden.setReparacion(re);
                            }
                            else{
                                if(num_partidas<=110){//MEDIANO
                                    Reparacion re = (Reparacion)session.createCriteria(Reparacion.class).add(Restrictions.eq("nombre", "MEDIANO")).setMaxResults(1).uniqueResult();
                                    aux_orden.setReparacion(re);
                                }
                                else{//GRANDE
                                    Reparacion re = (Reparacion)session.createCriteria(Reparacion.class).add(Restrictions.eq("nombre", "GRANDE")).setMaxResults(1).uniqueResult();
                                    aux_orden.setReparacion(re);
                                }
                            }
                        }
                        session.update(aux_orden);
                    }
                }
                else
                    JOptionPane.showMessageDialog(null, "¡Acceso denegado!");
            }

            if(this.cb_tipo_partida.getSelectedItem().toString().compareTo("Adicional")==0)
            {
                if(user.getAdicionalLevantamiento()==true)
                {
                    nuevaPartida(0, "a", -1);
                    aux_orden=(Orden)session.get(Orden.class, Integer.parseInt(orden));
                    String opcion=aux_orden.getReparacion().getNombre();
                    if(opcion.compareToIgnoreCase("EXPRESS")==0 || opcion.compareToIgnoreCase("CHICO")==0 || opcion.compareToIgnoreCase("MEDIANO")==0 || opcion.compareToIgnoreCase("GRANDE")==0)
                    {
                        int num_partidas=0;
                        if(aux_orden.getPartidasForIdOrden()!=null)
                            num_partidas=aux_orden.getPartidasForIdOrden().size();
                        if(num_partidas<=10){//EXPRESS
                            Reparacion re = (Reparacion)session.createCriteria(Reparacion.class).add(Restrictions.eq("nombre", "EXPRESS")).setMaxResults(1).uniqueResult();
                            aux_orden.setReparacion(re);
                        }
                        else{
                            if(num_partidas<=40){//CHICO
                                Reparacion re = (Reparacion)session.createCriteria(Reparacion.class).add(Restrictions.eq("nombre", "CHICO")).setMaxResults(1).uniqueResult();
                                aux_orden.setReparacion(re);
                            }
                            else{
                                if(num_partidas<=110){//MEDIANO
                                    Reparacion re = (Reparacion)session.createCriteria(Reparacion.class).add(Restrictions.eq("nombre", "MEDIANO")).setMaxResults(1).uniqueResult();
                                    aux_orden.setReparacion(re);
                                }
                                else{//GRANDE
                                    Reparacion re = (Reparacion)session.createCriteria(Reparacion.class).add(Restrictions.eq("nombre", "GRANDE")).setMaxResults(1).uniqueResult();
                                    aux_orden.setReparacion(re);
                                }
                            }
                        }
                        session.update(aux_orden);
                    }
                }
                else
                    JOptionPane.showMessageDialog(null, "¡Acceso denegado!");
            }

            if(this.cb_tipo_partida.getSelectedItem().toString().compareTo("Enlazada")==0)
            {
                if(user.getEnlazadaLevantamiento()==true)
                {
                    nuevaEnlazada();
                    aux_orden=(Orden)session.get(Orden.class, Integer.parseInt(orden));
                    String opcion=aux_orden.getReparacion().getNombre();
                    if(opcion.compareToIgnoreCase("EXPRESS")==0 || opcion.compareToIgnoreCase("CHICO")==0 || opcion.compareToIgnoreCase("MEDIANO")==0 || opcion.compareToIgnoreCase("GRANDE")==0)
                    {
                        int num_partidas=0;
                        if(aux_orden.getPartidasForIdOrden()!=null)
                            num_partidas=aux_orden.getPartidasForIdOrden().size();
                        if(num_partidas<=10){//EXPRESS
                            Reparacion re = (Reparacion)session.createCriteria(Reparacion.class).add(Restrictions.eq("nombre", "EXPRESS")).setMaxResults(1).uniqueResult();
                            aux_orden.setReparacion(re);
                        }
                        else{
                            if(num_partidas<=40){//CHICO
                                Reparacion re = (Reparacion)session.createCriteria(Reparacion.class).add(Restrictions.eq("nombre", "CHICO")).setMaxResults(1).uniqueResult();
                                aux_orden.setReparacion(re);
                            }
                            else{
                                if(num_partidas<=110){//MEDIANO
                                    Reparacion re = (Reparacion)session.createCriteria(Reparacion.class).add(Restrictions.eq("nombre", "MEDIANO")).setMaxResults(1).uniqueResult();
                                    aux_orden.setReparacion(re);
                                }
                                else{//GRANDE
                                    Reparacion re = (Reparacion)session.createCriteria(Reparacion.class).add(Restrictions.eq("nombre", "GRANDE")).setMaxResults(1).uniqueResult();
                                    aux_orden.setReparacion(re);
                                }
                            }
                        }
                        session.update(aux_orden);
                    }
                }
                else
                    JOptionPane.showMessageDialog(null, "¡Acceso denegado!");
            }
            t_datos.requestFocus();
        }catch(Exception e)
        {
            e.printStackTrace();
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
                                if(t_datos.getValueAt(renglones[x], 28).toString().compareTo("e")!=0)
                                {
                                    Partida part=(Partida) session.createCriteria(Partida.class).add(Restrictions.eq("ordenByIdOrden.idOrden", ord.getIdOrden())).add(Restrictions.eq("idEvaluacion", Integer.parseInt(t_datos.getValueAt(renglones[x], 0).toString()))).add(Restrictions.eq("subPartida", Integer.parseInt(t_datos.getValueAt(renglones[x], 1).toString()))).setMaxResults(1).uniqueResult();
                                    Orden ord=(Orden)session.get(Orden.class, Integer.parseInt(orden));
                                    if(part.getPedido()==null)
                                    {
                                        boolean permiso=true;
                                        if(part.isSurteAlmacen()==true)
                                        {
                                            int entradas=0, salidas=0;
                                            Movimiento[] lista_movimientos=(Movimiento[])part.getMovimientos().toArray(new Movimiento[0]);
                                            for(int mo=0; mo<lista_movimientos.length; mo++)
                                            {
                                                Almacen almacen=lista_movimientos[mo].getAlmacen();
                                                if(almacen.getTipoMovimiento()==1)
                                                    entradas++;
                                                else
                                                    salidas++;
                                            }
                                            if(entradas!=salidas)
                                                permiso=false;
                                        }
                                        if(permiso==true)
                                        {
                                            ord.eliminaPartidaOrden(part);
                                            String opcion=ord.getReparacion().getNombre();
                                            if(opcion.compareToIgnoreCase("EXPRESS")==0 || opcion.compareToIgnoreCase("CHICO")==0 || opcion.compareToIgnoreCase("MEDIANO")==0 || opcion.compareToIgnoreCase("GRANDE")==0)
                                            {
                                                int num_partidas=0;
                                                if(ord.getPartidasForIdOrden()!=null)
                                                    num_partidas=ord.getPartidasForIdOrden().size()-1;
                                                if(num_partidas<=10){//EXPRESS
                                                    Reparacion re = (Reparacion)session.createCriteria(Reparacion.class).add(Restrictions.eq("nombre", "EXPRESS")).setMaxResults(1).uniqueResult();
                                                    ord.setReparacion(re);
                                                }
                                                else{
                                                    if(num_partidas<=40){//CHICO
                                                        Reparacion re = (Reparacion)session.createCriteria(Reparacion.class).add(Restrictions.eq("nombre", "CHICO")).setMaxResults(1).uniqueResult();
                                                        ord.setReparacion(re);
                                                    }
                                                    else{
                                                        if(num_partidas<=110){//MEDIANO
                                                            Reparacion re = (Reparacion)session.createCriteria(Reparacion.class).add(Restrictions.eq("nombre", "MEDIANO")).setMaxResults(1).uniqueResult();
                                                            ord.setReparacion(re);
                                                        }
                                                        else{//GRANDE
                                                            Reparacion re = (Reparacion)session.createCriteria(Reparacion.class).add(Restrictions.eq("nombre", "GRANDE")).setMaxResults(1).uniqueResult();
                                                            ord.setReparacion(re);
                                                        }
                                                    }
                                                }
                                            }
                                            session.update(ord);
                                            session.delete(part);
                                            session.getTransaction().commit();
                                        }
                                        else
                                        {
                                            session.getTransaction().rollback();
                                            JOptionPane.showMessageDialog(null, "La partida ya tiene entregas");    
                                        }
                                            
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
                                    Orden ord=(Orden)session.get(Orden.class, Integer.parseInt(t_datos.getValueAt(renglones[x], 29).toString()));
                                    part.setOrdenByEnlazada(null);
                                    ord.eliminaPartidaenlazada(part);
                                    String opcion=ord.getReparacion().getNombre();
                                    if(opcion.compareToIgnoreCase("EXPRESS")==0 || opcion.compareToIgnoreCase("CHICO")==0 || opcion.compareToIgnoreCase("MEDIANO")==0 || opcion.compareToIgnoreCase("GRANDE")==0)
                                    {
                                        int num_partidas=0;
                                        if(ord.getPartidasForIdOrden()!=null)
                                            num_partidas=ord.getPartidasForIdOrden().size()-1;
                                        if(num_partidas<=10){//EXPRESS
                                            Reparacion re = (Reparacion)session.createCriteria(Reparacion.class).add(Restrictions.eq("nombre", "EXPRESS")).setMaxResults(1).uniqueResult();
                                            ord.setReparacion(re);
                                        }
                                        else{
                                            if(num_partidas<=40){//CHICO
                                                Reparacion re = (Reparacion)session.createCriteria(Reparacion.class).add(Restrictions.eq("nombre", "CHICO")).setMaxResults(1).uniqueResult();
                                                ord.setReparacion(re);
                                            }
                                            else{
                                                if(num_partidas<=110){//MEDIANO
                                                    Reparacion re = (Reparacion)session.createCriteria(Reparacion.class).add(Restrictions.eq("nombre", "MEDIANO")).setMaxResults(1).uniqueResult();
                                                    ord.setReparacion(re);
                                                }
                                                else{//GRANDE
                                                    Reparacion re = (Reparacion)session.createCriteria(Reparacion.class).add(Restrictions.eq("nombre", "GRANDE")).setMaxResults(1).uniqueResult();
                                                    ord.setReparacion(re);
                                                }
                                            }
                                        }
                                    }
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
                                    if(t_datos.getValueAt(renglones[x], 28).toString().compareTo("e")!=0)
                                    {
                                        Partida part=(Partida) session.createCriteria(Partida.class).add(Restrictions.eq("ordenByIdOrden.idOrden", ord.getIdOrden())).add(Restrictions.eq("idEvaluacion", Integer.parseInt(t_datos.getValueAt(renglones[x], 0).toString()))).add(Restrictions.eq("subPartida", Integer.parseInt(t_datos.getValueAt(renglones[x], 1).toString()))).setMaxResults(1).uniqueResult();
                                        Orden ord=(Orden)session.get(Orden.class, Integer.parseInt(orden));
                                        if(part.getPedido()==null)
                                        {
                                            boolean permiso=true;
                                            if(part.isSurteAlmacen()==true)
                                            {
                                                int entradas=0, salidas=0;
                                                Movimiento[] lista_movimientos=(Movimiento[])part.getMovimientos().toArray(new Movimiento[0]);
                                                for(int mo=0; mo<lista_movimientos.length; mo++)
                                                {
                                                    Almacen almacen=lista_movimientos[mo].getAlmacen();
                                                    if(almacen.getTipoMovimiento()==1)
                                                        entradas++;
                                                    else
                                                        salidas++;
                                                }
                                                if(entradas!=salidas)
                                                    permiso=false;
                                            }
                                            if(permiso==true)
                                            {
                                                ord.eliminaPartidaOrden(part);
                                                session.update(ord);
                                                session.delete(part);
                                                session.getTransaction().commit();
                                            }else
                                            {
                                                session.getTransaction().rollback();
                                                JOptionPane.showMessageDialog(null, "La partida ya tiene entregas");    
                                            }
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
                                        Orden ord=(Orden)session.get(Orden.class, Integer.parseInt(t_datos.getValueAt(renglones[x], 29).toString()));
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
        if(evt!=null)
        {
            Point p = evt.getPoint(); 
            int row = t_datos.rowAtPoint(p); 
            int column = t_datos.columnAtPoint(p); 
            t_datos.setRowSelectionInterval(row, row);
            t_datos.setColumnSelectionInterval(column, column);
        }
        /*if(t_datos.getSelectedRow()>=0)
        {
            if(t_datos.getSelectedColumn()==16)
            {
                numeros.removeAllItems();
                numeros.addItem("Cancelar");
                numeros.addItem("Eliminar");
                numeros.addItem("Nuevo");
                numeros.setSelectedItem("Cancelar");
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
                     e.printStackTrace();
                }
                if(session!=null)
                     if(session.isOpen()==true)
                         session.close();
            }
        }*/
    }//GEN-LAST:event_t_datosMouseClicked

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
                        //Partida extiste = (Partida) session.createCriteria(Partida.class).add(Restrictions.and(Restrictions.eq("ordenByIdOrden.idOrden", ord.getIdOrden()), Restrictions.or(Restrictions.gt("intDesm", 0), Restrictions.or(Restrictions.gt("intCamb", 0), Restrictions.or(Restrictions.gt("intRepMin", 0), Restrictions.or(Restrictions.gt("intRepMed", 0), Restrictions.or(Restrictions.gt("intRepMax", 0), Restrictions.or(Restrictions.gt("intPinMin", 0), Restrictions.or(Restrictions.gt("intPinMed", 0), Restrictions.gt("intPinMax", 0)))))))))).setMaxResults(1).uniqueResult();
                        
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

    private void c_plantillaItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_c_plantillaItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_c_plantillaItemStateChanged

    private void c_plantillaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_c_plantillaActionPerformed
        // TODO add your handling code here:
        if(inicio==true)
        {
            Session session = HibernateUtil.getSessionFactory().openSession();
            try 
            {
                session.getTransaction().begin();
                Orden ord = (Orden)session.get(Orden.class, Integer.parseInt(orden));
                if(c_plantilla.getSelectedItem().toString().compareTo("PLANTILLAS")!=0)
                {
                    Servicio ser=new Servicio();
                    ser.setIdServicio(c_plantilla.getSelectedItem().toString());
                    ord.setServicio(ser);
                }
                else
                {
                    ord.setServicio(null);
                }
                session.update(ord);
                session.getTransaction().commit();
            }catch(Exception he) {
                he.printStackTrace();
                evt=null;
                JOptionPane.showMessageDialog(null, "¡Error al asignar la plantilla!");
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
    }//GEN-LAST:event_c_plantillaActionPerformed

    private void t_datosKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_datosKeyReleased
        // TODO add your handling code here:
        int code = evt.getKeyCode();
        if(code == KeyEvent.VK_SPACE)
        {
            if(t_datos.getSelectedColumn()==34)
            {
                cb_prioridad.requestFocus();
                cb_prioridad.setPopupVisible(true);
            }
            else
            {
                numeros.requestFocus();
                numeros.setPopupVisible(true);
            }
        }
    }//GEN-LAST:event_t_datosKeyReleased

    private void b_consumiblesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_consumiblesActionPerformed
        // TODO add your handling code here:
        if(user.getConsultaConsumible()==false && user.getEditaConsumible()==false){
            JOptionPane.showMessageDialog(this, "¡Acceso Denegado!");
        }else{
            /*if(user.getEditaConsumible()==false){
                bloquea_consumible();
            }
            consulta_consumible();

            Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
            ventanaConsumible.setLocation((d.width/2)-(375/2), (d.height/2)-(250/2));
            ventanaConsumible.setSize(375, 250);
            ventanaConsumible.setVisible(true);   */
            String cerrada="";
            if(edo.compareTo("")!=0)
                cerrada=edo;
            else{
                h=new Herramientas(user, 0);
                if(h.isCerrada(orden)==true)
                    cerrada="cerrada";
            }
            RegistraConsumibles reg_con=new RegistraConsumibles(new javax.swing.JFrame(), true, orden, user, sessionPrograma, cerrada);
            Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
            reg_con.setLocation((d.width/2)-(880/2), (d.height/2)-(405/2));
            reg_con.setSize(880, 405);
            reg_con.setVisible(true);
            SumaTotal();
        }
    }//GEN-LAST:event_b_consumiblesActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton b_busca;
    private javax.swing.JButton b_consumibles;
    private javax.swing.JButton b_exel;
    private javax.swing.JButton b_mas_partida;
    private javax.swing.JButton b_mas_sup_partida;
    private javax.swing.JButton b_menos;
    private javax.swing.JButton b_pdf;
    private javax.swing.JButton b_tot;
    private javax.swing.JButton b_valuacion;
    private javax.swing.JComboBox c_filtro;
    private javax.swing.JComboBox c_plantilla;
    private javax.swing.JComboBox cb_prioridad;
    private javax.swing.JComboBox cb_tipo_partida;
    private javax.swing.JTextField instruccion;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JComboBox medida;
    private javax.swing.JComboBox numeros;
    private javax.swing.JRadioButton r_cerrar;
    private javax.swing.JScrollPane scroll;
    private javax.swing.JTextField t_busca;
    private javax.swing.JTable t_datos;
    private javax.swing.JFormattedTextField t_doble;
    private javax.swing.JTextField t_numero;
    private javax.swing.JFormattedTextField t_total_consumible;
    // End of variables declaration//GEN-END:variables

    public void nuevaPartida(int a, String op, int tipo)
    {
        if(a==0)
        {
            agregaPartida obj = null;
            ArrayList registro = null;
            if(tipo==-1)
            {
                obj = new agregaPartida(new javax.swing.JFrame(), true, orden, this.user, this.sessionPrograma, c_plantilla.getSelectedItem().toString());
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
                    registro=new ArrayList();
                    Catalogo cat=(Catalogo)session.get(Catalogo.class, tipo);
                    ArrayList aux=new ArrayList();
                    aux.add(cat.getIdCatalogo());
                    aux.add("BUSCAR");
                    registro.add(aux);
                }
                if(registro!=null)
                {
                    for(int x=0; x<registro.size(); x++)
                    {
                        ArrayList arr=(ArrayList)registro.get(x);
                        int id_catalogo=Integer.parseInt(arr.get(0).toString());
                        int r=busca(id_catalogo, op);
                        //if(r==-1)
                        //{
                            Partida resp=(Partida) session.createCriteria(Partida.class).add(Restrictions.eq("ordenByIdOrden.idOrden", Integer.parseInt(orden))).addOrder(Order.desc("idEvaluacion")).setMaxResults(1).uniqueResult();
                            if(resp!=null)
                            {
                                ord = (Orden)session.get(Orden.class, Integer.parseInt(orden));
                                Catalogo cat1=(Catalogo)session.get(Catalogo.class, id_catalogo);
                                Ejemplar eje1=null;
                                int id=resp.getIdEvaluacion()+1;
                                Orden ord = (Orden)session.get(Orden.class, Integer.parseInt(orden)); 
                                Partida part=new Partida();
                                part.setIdEvaluacion(id);
                                part.setSubPartida(0);
                                part.setCant(1.0d);
                                part.setMed("PZAS");
                                part.setPrioridad(4);
                                part.setCatalogo(cat1);
                                if(arr.get(1).toString().compareTo("BUSCAR")!=0)
                                {
                                    eje1=(Ejemplar)session.get(Ejemplar.class, arr.get(1).toString());
                                    part.setEjemplar(eje1);
                                    Criteria crit = session.createCriteria(Partida.class);
                                    crit.add(Restrictions.eq("ejemplar.idParte", eje1.getIdParte()));
                                    crit = crit.createAlias("pedido", "ped");
                                    //crit=crit.addOrder(Order.asc("ped.fechaPedido"));
                                    crit=crit.addOrder(Order.desc("pcp"));
                                    crit.add(Restrictions.isNotNull("pedido"));
                                    Partida partidaPrecio=(Partida) crit.setMaxResults(1).uniqueResult();

                                    if(partidaPrecio!=null)
                                    {
                                            part.setCU(partidaPrecio.getPcp());
                                            part.setPrecioCiaSegurosCU(partidaPrecio.getPcp());
                                    }
                                }
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
                                part.setComprador(1);
                                part.setOp(false);
                                part.setD(0.0);
                                part.setR(0.0);
                                part.setM(0.0);
                                part.setOriCon("-");
                                part.setComprador(1);
                                part.setCuValuacion(0.0);
                                session.saveOrUpdate(part);
                                session.beginTransaction().commit();
                            }
                            else
                            {
                                int id=1;
                                Orden ord = (Orden)session.get(Orden.class, Integer.parseInt(orden)); 
                                Catalogo cat1=(Catalogo)session.get(Catalogo.class, id_catalogo);
                                Ejemplar eje1=null;
                                Partida part=new Partida();
                                part.setIdEvaluacion(id);
                                part.setSubPartida(0);
                                part.setCant(1.0d);
                                part.setMed("PZAS");
                                part.setPrioridad(4);
                                part.setCatalogo(cat1);
                                if(arr.get(1).toString().compareTo("BUSCAR")!=0)
                                {
                                    eje1=(Ejemplar)session.get(Ejemplar.class, arr.get(1).toString());
                                    part.setEjemplar(eje1);
                                                
                                    Criteria crit = session.createCriteria(Partida.class);
                                    crit.add(Restrictions.eq("ejemplar.idParte", eje1.getIdParte()));
                                    crit = crit.createAlias("pedido", "ped");
                                    //crit=crit.addOrder(Order.asc("ped.fechaPedido"));
                                    crit=crit.addOrder(Order.desc("pcp"));
                                    crit.add(Restrictions.isNotNull("pedido"));
                                    Partida partidaPrecio=(Partida) crit.setMaxResults(1).uniqueResult();

                                    if(partidaPrecio!=null)
                                    {
                                            part.setCU(partidaPrecio.getPcp());
                                            part.setPrecioCiaSegurosCU(partidaPrecio.getPcp());
                                    }
                                }
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
                                part.setComprador(1);
                                part.setCuValuacion(0.0);
                                session.saveOrUpdate(part);
                                session.beginTransaction().commit();
                            }
                        /*}
                        else
                        {
                            JOptionPane.showMessageDialog(null, "No se puede agregar partidas duplicadas!");
                            t_datos.setRowSelectionInterval(r, r);
                        }*/
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
                agregaPartida obj = new agregaPartida(new javax.swing.JFrame(), true, orden, this.user, this.sessionPrograma, c_plantilla.getSelectedItem().toString());
                Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
                obj.setLocation((d.width/2)-(obj.getWidth()/2), (d.height/2)-(obj.getHeight()/2));
                obj.setVisible(true);
                Session session = HibernateUtil.getSessionFactory().openSession();
                try
                {
                    session.beginTransaction().begin();
                    ArrayList registro=obj.getReturnStatus();
                    if(registro!=null)
                    {
                        for(int x=0; x<registro.size(); x++)
                        {
                            ArrayList arr=(ArrayList)registro.get(x);
                            int id_catalogo=Integer.parseInt(arr.get(0).toString());
                            int r=busca(id_catalogo, op);
                            if(r==-1)
                            {
                                Partida resp=(Partida) session.createCriteria(Partida.class).add(Restrictions.eq("ordenByIdOrden.idOrden", Integer.parseInt(orden))).add(Restrictions.eq("idEvaluacion", Integer.parseInt(t_datos.getValueAt(t_datos.getSelectedRow(), 0).toString()))).addOrder(Order.desc("subPartida")).setMaxResults(1).uniqueResult();
                                if(resp!=null)
                                {
                                    int id=resp.getSubPartida()+1;
                                    ord = (Orden)session.get(Orden.class, Integer.parseInt(orden)); 
                                    Catalogo cat1=(Catalogo)session.get(Catalogo.class, id_catalogo);
                                    Ejemplar eje1=null;
                                    Partida part=new Partida();
                                    part.setIdEvaluacion(resp.getIdEvaluacion());
                                    part.setSubPartida(id);
                                    part.setCant(1.0d);
                                    part.setMed("PZAS");
                                    part.setPrioridad(4);
                                    part.setCatalogo(cat1);
                                    if(arr.get(1).toString().compareTo("BUSCAR")!=0)
                                    {
                                        eje1=(Ejemplar)session.get(Ejemplar.class, arr.get(1).toString());
                                        part.setEjemplar(eje1);
                                                
                                        Criteria crit = session.createCriteria(Partida.class);
                                        crit.add(Restrictions.eq("ejemplar.idParte", eje1.getIdParte()));
                                        crit = crit.createAlias("pedido", "ped");
                                        //crit=crit.addOrder(Order.asc("ped.fechaPedido"));
                                        crit=crit.addOrder(Order.desc("pcp"));
                                        crit.add(Restrictions.isNotNull("pedido"));
                                        Partida partidaPrecio=(Partida) crit.setMaxResults(1).uniqueResult();

                                        if(partidaPrecio!=null)
                                        {
                                                part.setCU(partidaPrecio.getPcp());
                                                part.setPrecioCiaSegurosCU(partidaPrecio.getPcp());
                                        }
                                    }
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
                                    part.setCuValuacion(0.0);
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
            if(Integer.parseInt(t_datos.getValueAt(ren, 15).toString())==articulo && t_datos.getValueAt(ren, 29).toString().compareTo(op)==0)
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
                if(ord.getServicio()!=null)
                    c_plantilla.setSelectedItem(ord.getServicio().getIdServicio());
                Query query = session.createSQLQuery("select id_evaluacion, sub_partida, esp_hoj, esp_mec, esp_sus, esp_ele, if(dm=-1, 'false', 'true')as desmontar, if(cam=-1, 'false', 'true') as cambiar, if(rep_min=-1, 'false', 'true') as r_min, \n" +
"if(rep_med=-1, 'false', 'true')as r_med,  if(rep_max=-1, 'false', 'true') as r_max, if(pint=-1, 'false', 'true') as pintura, cant, med, catalogo.nombre as miNombre, partida.id_catalogo, \n" +
"id_Parte, incluida, ori, nal, desm, pd, tot, if(int_desm=-1, 'false', 'true') as i_desm, if(Int_camb=-1, 'false', 'true') as i_cam, if(Int_rep_min=-1, 'false', 'true') as i_r_min, \n" +
"if(Int_rep_med=-1, 'false', 'true') as i_r_med, if(Int_rep_max=-1, 'false', 'true') as i_r_max, if(int_pin_min=-1, 'false', 'true') as i_p_min, if(int_pin_med=-1, 'false', 'true') as i_p_med, \n" +
"if(int_pin_max=-1, 'false', 'true')as i_p_max, if(Instruccion is null, '', Instruccion) as instruccion_f,  tipo, if(enlazada is null, '', enlazada)as freeze, autorizado_valuacion, prioridad \n" +
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
                        //model.setValueAt(map.get("id_parte"), i, 16);
                        if(map.get("id_Parte")!=null)
                            model.setValueAt(map.get("id_Parte"), i, 16);
                        else
                            model.setValueAt("", i, 16);
                        //model.setCeldaEditable(i, 16, false);
                        model.setValueAt(map.get("incluida"), i, 17);
                        model.setValueAt(map.get("pd"), i, 18);
                        
                        model.setValueAt(map.get("tot"), i, 19);
                        model.setCeldaEditable(i, 19, true);
                        
                        model.setValueAt(map.get("i_desm").toString().contentEquals("true"), i, 20);
                        model.setValueAt(map.get("i_cam").toString().contentEquals("true"), i, 21);
                        model.setValueAt(map.get("i_r_min").toString().contentEquals("true"), i, 22);
                        model.setValueAt(map.get("i_r_med").toString().contentEquals("true"), i, 23);
                        model.setValueAt(map.get("i_r_max").toString().contentEquals("true"), i, 24);
                        model.setValueAt(map.get("i_p_min").toString().contentEquals("true"), i, 25);
                        model.setValueAt(map.get("i_p_med").toString().contentEquals("true"), i, 26);
                        model.setValueAt(map.get("i_p_max").toString().contentEquals("true"), i, 27);
                        
                        model.setValueAt(map.get("instruccion_f"), i, 28);
                        
                        model.setValueAt(map.get("tipo"), i, 29);
                        model.setCeldaEditable(i, 29, false);
                        
                        model.setValueAt(map.get("freeze"), i, 30);
                        model.setCeldaEditable(i, 30, false);
                        
                        model.setValueAt(map.get("prioridad"), i, 31);
                        model.setValueAt(map.get("autorizado_valuacion"), i, 32);
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
                        
                        model.setValueAt(enlazadas[i].getPrioridad(), partidas.size()+i, 34);
                        model.setCeldaEditable(partidas.size()+i, 34, false);
                        
                        model.setValueAt(enlazadas[i].isAutorizadoValuacion(), partidas.size()+i, 35);
                        model.setCeldaEditable(partidas.size()+i, 35, false);
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
            if((x>1 && x<12) || (x>16 && x<31) || x==32 || x==35)
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
        this.c_plantilla.setEnabled(tabla);
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
            Configuracion con= (Configuracion)session.get(Configuracion.class, configuracion);
            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, con.getEmpresa(), 160, 580, 0);
            reporte.contenido.setFontAndSize(bf, 8);
            reporte.contenido.setColorFill(BaseColor.BLACK);
            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Autorizacion de Operaciones ("+this.c_filtro.getSelectedItem().toString()+")", 160, 570, 0);
            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_RIGHT, "Fecha:"+new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date()), 760, 580, 0);

                    ord = (Orden)session.get(Orden.class, Integer.parseInt(orden)); 
                    Foto foto = (Foto)session.createCriteria(Foto.class).add(Restrictions.eq("orden.idOrden", Integer.parseInt(orden))).addOrder(Order.desc("fecha")).setMaxResults(1).uniqueResult();
                    if(foto!=null)
                    {
                        Ftp miFtp=new Ftp();
                        boolean respuesta=true;
                        respuesta=miFtp.conectar(rutaFTP, "compras", "04650077", 3310);
                        if(respuesta==true)
                        {
                            miFtp.cambiarDirectorio("ordenes/"+ord.getIdOrden()+"/miniatura");
                            String temporal=miFtp.descargaTemporal(foto.getDescripcion());
                            miFtp.desconectar();
                            reporte.agregaObjeto(reporte.crearImagen(temporal, 15, -50, 25, true));
                        }
                    }
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
                tabla.addCell(reporte.celda("Refacción", font, cabecera, centro, 3,1,Rectangle.RECTANGLE));
                tabla.addCell(reporte.celda("Instrucción Final", font, cabecera, centro, 8,1, Rectangle.RECTANGLE));
                tabla.addCell(reporte.celda("Descripcion", font, cabecera, centro, 0, 3, Rectangle.RECTANGLE));

                tabla.addCell(reporte.celda("Ori", font, cabecera, centro, 0,2, Rectangle.RECTANGLE));
                tabla.addCell(reporte.celda("PD", font, cabecera, centro, 0,2, Rectangle.RECTANGLE));
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
                    javax.swing.ImageIcon.class,/*Prioridad*/
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
                                            if(user.getEditarLevantamiento()==true || (((String)t_datos.getValueAt(t_datos.getSelectedRow(), 29)).compareToIgnoreCase("o")==0 && user.getAValuacionLevantamiento()==true && r_cerrar.isSelected()==false) || (((String)t_datos.getValueAt(t_datos.getSelectedRow(), 29)).compareToIgnoreCase("o")!=0 && user.getAValuacionLevantamiento()==true) )
                                            {
                                                if(t_datos.getValueAt(row, 29).toString().compareTo("e")!=0)
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
                                            if(user.getEditarLevantamiento()==true || (((String)t_datos.getValueAt(t_datos.getSelectedRow(), 29)).compareToIgnoreCase("o")==0 && user.getAValuacionLevantamiento()==true && r_cerrar.isSelected()==false) || (((String)t_datos.getValueAt(t_datos.getSelectedRow(), 29)).compareToIgnoreCase("o")!=0 && user.getAValuacionLevantamiento()==true) )
                                            {
                                                if(t_datos.getValueAt(row, 29).toString().compareTo("e")!=0)
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
                                            if(user.getEditarLevantamiento()==true || (((String)t_datos.getValueAt(t_datos.getSelectedRow(), 29)).compareToIgnoreCase("o")==0 && user.getAValuacionLevantamiento()==true && r_cerrar.isSelected()==false) || (((String)t_datos.getValueAt(t_datos.getSelectedRow(), 29)).compareToIgnoreCase("o")!=0 && user.getAValuacionLevantamiento()==true) )
                                            {
                                                if(t_datos.getValueAt(row, 29).toString().compareTo("e")!=0)
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
                                            if(user.getEditarLevantamiento()==true || (((String)t_datos.getValueAt(t_datos.getSelectedRow(), 29)).compareToIgnoreCase("o")==0 && user.getAValuacionLevantamiento()==true && r_cerrar.isSelected()==false) || (((String)t_datos.getValueAt(t_datos.getSelectedRow(), 29)).compareToIgnoreCase("o")!=0 && user.getAValuacionLevantamiento()==true) )
                                            {
                                                if(t_datos.getValueAt(row, 29).toString().compareTo("e")!=0)
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
                                            if(user.getEditarLevantamiento()==true || (((String)t_datos.getValueAt(t_datos.getSelectedRow(), 29)).compareToIgnoreCase("o")==0 && user.getAValuacionLevantamiento()==true && r_cerrar.isSelected()==false) || (((String)t_datos.getValueAt(t_datos.getSelectedRow(), 29)).compareToIgnoreCase("o")!=0 && user.getAValuacionLevantamiento()==true) )
                                            {
                                                if(t_datos.getValueAt(row, 29).toString().compareTo("e")!=0)
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
                                            if(user.getEditarLevantamiento()==true || (((String)t_datos.getValueAt(t_datos.getSelectedRow(), 29)).compareToIgnoreCase("o")==0 && user.getAValuacionLevantamiento()==true && r_cerrar.isSelected()==false) || (((String)t_datos.getValueAt(t_datos.getSelectedRow(), 29)).compareToIgnoreCase("o")!=0 && user.getAValuacionLevantamiento()==true) )
                                            {
                                                if(t_datos.getValueAt(row, 29).toString().compareTo("e")!=0)
                                                {
                                                    Partida part=(Partida) session.createCriteria(Partida.class).add(Restrictions.eq("ordenByIdOrden.idOrden", Integer.parseInt(orden))).add(Restrictions.eq("idEvaluacion", Integer.parseInt(t_datos.getValueAt(t_datos.getSelectedRow(), 0).toString()))).add(Restrictions.eq("subPartida", Integer.parseInt(t_datos.getValueAt(t_datos.getSelectedRow(), 1).toString()))).setMaxResults(1).uniqueResult();
                                                    if(part!=null)
                                                    {
                                                        if(value.toString().compareTo("true")==0)
                                                        {
                                                            /*if(part.getOrdenByIdOrden().getRCotizaCierre()==null)
                                                            {
                                                                if(part.getOrdenByIdOrden().getCierreRefacciones()==null)
                                                                {*/
                                                                    part.setCam(0);
                                                                    part.setRefCoti(true);
                                                                    part.setDm(-1);
                                                                    if(part.getEjemplar()!=null && part.getEjemplar().getInventario()==1 && (boolean)t_datos.getValueAt(row, 21)==true)
                                                                    {
                                                                        part.setAutorizado(true);
                                                                        part.setSurteAlmacen(true);
                                                                        part.setRefComp(false);
                                                                        if(part.getTipo().compareTo("o")==0 && part.getOrdenByIdOrden().getRLevantamientoCierre()!=null){
                                                                            part.setTipo("o");
                                                                        }
                                                                    }
                                                                    else
                                                                    {
                                                                        if((boolean)t_datos.getValueAt(row, 21)==true)
                                                                        {
                                                                            part.setSurteAlmacen(false);
                                                                            part.setRefComp(true);
                                                                            part.setAutorizado(true);
                                                                            if(part.getTipo().compareTo("o")==0 && part.getOrdenByIdOrden().getRLevantamientoCierre()!=null){
                                                                                part.setTipo("o");
                                                                            }
                                                                        }
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
                                                                /*}
                                                                else
                                                                {
                                                                    JOptionPane.showMessageDialog(null, "Las Compras ya estan cerradas, solicitar la Apertura al Depto de Compras");
                                                                }
                                                            }
                                                            else
                                                            {
                                                                JOptionPane.showMessageDialog(null, "Las Cotizaciones ya estan cerradas, solicitar la Apertura al Depto de Compras");
                                                            }*/
                                                        }
                                                        else
                                                        {
                                                            if(part.getIntCamb()==-1)
                                                            {
                                                                if(part.getPedido()==null)
                                                                {
                                                                    part.setCam(-1);
                                                                    part.setAutorizado(false);
                                                                    part.setRefCoti(false);
                                                                    part.setRefComp(false);
                                                                    part.setSurteAlmacen(false);
                                                                    
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
                                                            else
                                                                JOptionPane.showMessageDialog(null, "No se puede modicar si vas a cambiar en interno");
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
                                            if(user.getEditarLevantamiento()==true || (((String)t_datos.getValueAt(t_datos.getSelectedRow(), 29)).compareToIgnoreCase("o")==0 && user.getAValuacionLevantamiento()==true && r_cerrar.isSelected()==false) || (((String)t_datos.getValueAt(t_datos.getSelectedRow(), 29)).compareToIgnoreCase("o")!=0 && user.getAValuacionLevantamiento()==true) )
                                            {
                                                if(t_datos.getValueAt(row, 29).toString().compareTo("e")!=0)
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
                                            if(user.getEditarLevantamiento()==true || (((String)t_datos.getValueAt(t_datos.getSelectedRow(), 29)).compareToIgnoreCase("o")==0 && user.getAValuacionLevantamiento()==true && r_cerrar.isSelected()==false) || (((String)t_datos.getValueAt(t_datos.getSelectedRow(), 29)).compareToIgnoreCase("o")!=0 && user.getAValuacionLevantamiento()==true) )
                                            {
                                                if(t_datos.getValueAt(row, 29).toString().compareTo("e")!=0)
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
                                            if(user.getEditarLevantamiento()==true || (((String)t_datos.getValueAt(t_datos.getSelectedRow(), 29)).compareToIgnoreCase("o")==0 && user.getAValuacionLevantamiento()==true && r_cerrar.isSelected()==false) || (((String)t_datos.getValueAt(t_datos.getSelectedRow(), 29)).compareToIgnoreCase("o")!=0 && user.getAValuacionLevantamiento()==true) )
                                            {
                                                if(t_datos.getValueAt(row, 29).toString().compareTo("e")!=0)
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
                                
                            case 11://Pin
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
                                            if(user.getEditarLevantamiento()==true || (((String)t_datos.getValueAt(t_datos.getSelectedRow(), 29)).compareToIgnoreCase("o")==0 && user.getAValuacionLevantamiento()==true && r_cerrar.isSelected()==false) || (((String)t_datos.getValueAt(t_datos.getSelectedRow(), 29)).compareToIgnoreCase("o")!=0 && user.getAValuacionLevantamiento()==true) )
                                            {
                                                if(t_datos.getValueAt(row, 29).toString().compareTo("e")!=0)
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
                                            if(user.getEditarLevantamiento()==true || (((String)t_datos.getValueAt(t_datos.getSelectedRow(), 29)).compareToIgnoreCase("o")==0 && user.getAValuacionLevantamiento()==true && r_cerrar.isSelected()==false) || (((String)t_datos.getValueAt(t_datos.getSelectedRow(), 29)).compareToIgnoreCase("o")!=0 && user.getAValuacionLevantamiento()==true) )
                                            {
                                                if(t_datos.getValueAt(row, 29).toString().compareTo("e")!=0)
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
                                            if(user.getEditarLevantamiento()==true || (((String)t_datos.getValueAt(t_datos.getSelectedRow(), 29)).compareToIgnoreCase("o")==0 && user.getAValuacionLevantamiento()==true && r_cerrar.isSelected()==false) || (((String)t_datos.getValueAt(t_datos.getSelectedRow(), 29)).compareToIgnoreCase("o")!=0 && user.getAValuacionLevantamiento()==true) )
                                            {
                                                if(t_datos.getValueAt(row, 29).toString().compareTo("e")!=0)
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
                                        Session session = null;
                                        try
                                        {
                                            if(t_datos.getValueAt(row, 29).toString().compareTo("e")!=0)
                                            {
                                                if(value!=null)
                                                {
                                                    switch(value.toString())
                                                    {
                                                        case "Eliminar":
                                                            session = HibernateUtil.getSessionFactory().openSession();
                                                            session.beginTransaction().begin();
                                                            user = (Usuario)session.get(Usuario.class, user.getIdUsuario());
                                                            if(user.getEditarLevantamiento()==true || (((String)t_datos.getValueAt(t_datos.getSelectedRow(), 29)).compareToIgnoreCase("o")==0 && user.getAValuacionLevantamiento()==true && r_cerrar.isSelected()==false) || (((String)t_datos.getValueAt(t_datos.getSelectedRow(), 29)).compareToIgnoreCase("o")!=0 && user.getAValuacionLevantamiento()==true) )
                                                            {
                                                                Partida part=(Partida) session.createCriteria(Partida.class).add(Restrictions.eq("ordenByIdOrden.idOrden", Integer.parseInt(orden))).add(Restrictions.eq("idEvaluacion", Integer.parseInt(t_datos.getValueAt(t_datos.getSelectedRow(), 0).toString()))).add(Restrictions.eq("subPartida", Integer.parseInt(t_datos.getValueAt(t_datos.getSelectedRow(), 1).toString()))).setMaxResults(1).uniqueResult();
                                                                if(part!=null)
                                                                {
                                                                    boolean permiso=true;
                                                                    if(part.isSurteAlmacen()==true)
                                                                    {
                                                                        int entradas=0, salidas=0;
                                                                        Movimiento[] lista_movimientos=(Movimiento[])part.getMovimientos().toArray(new Movimiento[0]);
                                                                        for(int mo=0; mo<lista_movimientos.length; mo++)
                                                                        {
                                                                            Almacen almacen=lista_movimientos[mo].getAlmacen();
                                                                            if(almacen.getTipoMovimiento()==1)
                                                                                entradas++;
                                                                            else
                                                                                salidas++;
                                                                        }
                                                                        if(entradas!=salidas)
                                                                            permiso=false;
                                                                    }
                                                                    if(permiso==true)
                                                                    {
                                                                        part.setEjemplar(null);
                                                                        part.setSurteAlmacen(false);
                                                                        if(part.getCam()!=-1 && part.getIntCamb()!=-1)
                                                                            part.setRefComp(true);
                                                                        session.update(part);
                                                                        session.getTransaction().commit();
                                                                        vector.setElementAt("", col);
                                                                        this.dataVector.setElementAt(vector, row);
                                                                        fireTableCellUpdated(row, col);
                                                                    }
                                                                    else
                                                                        JOptionPane.showMessageDialog(null, "La partida ya Tiene Entregas no se puede modificar");
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
                                                            break;
                                                        case "Cancelar":
                                                            break;
                                                        case "Nuevo":
                                                            //Mostrar lista de Ejemplares
                                                            altaEjemplar obj1 = new altaEjemplar(new javax.swing.JFrame(), true, user, sessionPrograma, 0);
                                                            obj1.t_id_catalogo.setText(t_datos.getValueAt(t_datos.getSelectedRow(), 15).toString());
                                                            obj1.t_catalogo.setText(t_datos.getValueAt(t_datos.getSelectedRow(), 14).toString());
                                                            obj1.t_numero.requestFocus();
                                                            Dimension d1 = Toolkit.getDefaultToolkit().getScreenSize();
                                                            obj1.setLocation((d1.width/2)-(obj1.getWidth()/2), (d1.height/2)-(obj1.getHeight()/2));
                                                            obj1.setVisible(true);
                                                            Ejemplar ejem1=obj1.getReturnStatus();
                                                            if (ejem1!=null)
                                                            {
                                                                session = HibernateUtil.getSessionFactory().openSession();
                                                                session.beginTransaction().begin();
                                                                Ejemplar ejem = (Ejemplar)session.get(Ejemplar.class, ejem1.getIdParte());
                                                                if(ejem!=null)
                                                                {
                                                                    user = (Usuario)session.get(Usuario.class, user.getIdUsuario());
                                                                    if(user.getEditarLevantamiento()==true || (((String)t_datos.getValueAt(t_datos.getSelectedRow(), 29)).compareToIgnoreCase("o")==0 && user.getAValuacionLevantamiento()==true && r_cerrar.isSelected()==false) || (((String)t_datos.getValueAt(t_datos.getSelectedRow(), 29)).compareToIgnoreCase("o")!=0 && user.getAValuacionLevantamiento()==true) )
                                                                    {
                                                                        Partida part=(Partida) session.createCriteria(Partida.class).add(Restrictions.eq("ordenByIdOrden.idOrden", Integer.parseInt(orden))).add(Restrictions.eq("idEvaluacion", Integer.parseInt(t_datos.getValueAt(t_datos.getSelectedRow(), 0).toString()))).add(Restrictions.eq("subPartida", Integer.parseInt(t_datos.getValueAt(t_datos.getSelectedRow(), 1).toString()))).setMaxResults(1).uniqueResult();
                                                                        if(part!=null)
                                                                        {
                                                                            boolean permiso=true;
                                                                            if(part.isSurteAlmacen()==true)
                                                                            {
                                                                                int entradas=0, salidas=0;
                                                                                Movimiento[] lista_movimientos=(Movimiento[])part.getMovimientos().toArray(new Movimiento[0]);
                                                                                for(int mo=0; mo<lista_movimientos.length; mo++)
                                                                                {
                                                                                    Almacen almacen=lista_movimientos[mo].getAlmacen();
                                                                                    if(almacen.getTipoMovimiento()==1)
                                                                                        entradas++;
                                                                                    else
                                                                                        salidas++;
                                                                                }
                                                                                if(entradas!=salidas)
                                                                                    permiso=false;
                                                                            }
                                                                            if(permiso==true)
                                                                            {
                                                                                Criteria crit = session.createCriteria(Partida.class);
                                                                                crit.add(Restrictions.eq("ejemplar.idParte", ejem.getIdParte()));
                                                                                crit = crit.createAlias("pedido", "ped");
                                                                                //crit=crit.addOrder(Order.asc("ped.fechaPedido"));
                                                                                crit=crit.addOrder(Order.desc("pcp"));
                                                                                crit.add(Restrictions.isNotNull("pedido"));
                                                                                Partida partidaPrecio=(Partida) crit.setMaxResults(1).uniqueResult();
                                                                                part.setEjemplar(ejem);
                                                                                if(ejem.getInventario()==1)
                                                                                {
                                                                                    part.setRefComp(false);
                                                                                    part.setSurteAlmacen(true);
                                                                                }
                                                                                else
                                                                                {
                                                                                    if(part.getCam()!=-1 && part.getIntCamb()!=-1)
                                                                                        part.setRefComp(true);
                                                                                    part.setSurteAlmacen(false);
                                                                                }
                                                                                if(partidaPrecio!=null)
                                                                                {
                                                                                    if(partidaPrecio.getPcp()>part.getCU())
                                                                                    {
                                                                                        part.setCU(partidaPrecio.getPcp());
                                                                                        part.setPrecioCiaSegurosCU(0.0+Math.round(part.getCU()/(1-(part.getPorcentaje()*0.01))));
                                                                                    }
                                                                                }
                                                                                session.update(part);
                                                                                session.getTransaction().commit();
                                                                                vector.setElementAt(ejem.getIdParte(), col);
                                                                                this.dataVector.setElementAt(vector, row);
                                                                                fireTableCellUpdated(row, col);
                                                                            }
                                                                            else
                                                                                JOptionPane.showMessageDialog(null, "La partida ya Tiene Entregas no se puede modificar");

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
                                                            }
                                                            break;
                                                        case "Buscar":
                                                            session = HibernateUtil.getSessionFactory().openSession();
                                                            session.beginTransaction().begin();
                                                            Partida part=(Partida) session.createCriteria(Partida.class).add(Restrictions.eq("ordenByIdOrden.idOrden", Integer.parseInt(orden))).add(Restrictions.eq("idEvaluacion", Integer.parseInt(t_datos.getValueAt(t_datos.getSelectedRow(), 0).toString()))).add(Restrictions.eq("subPartida", Integer.parseInt(t_datos.getValueAt(t_datos.getSelectedRow(), 1).toString()))).setMaxResults(1).uniqueResult();
                                                            if(part!=null)
                                                            {
                                                                buscaEjemplar obj = new buscaEjemplar(new javax.swing.JFrame(), true, sessionPrograma, null, 2);
                                                                obj.t_busca.requestFocus();
                                                                Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
                                                                obj.setLocation((d.width/2)-(obj.getWidth()/2), (d.height/2)-(obj.getHeight()/2));
                                                                obj.setVisible(true);
                                                                Ejemplar ejem=obj.getReturnStatus();
                                                                if (ejem!=null)
                                                                {
                                                                    boolean permiso=true;
                                                                    if(part.isSurteAlmacen()==true)
                                                                    {
                                                                        int entradas=0, salidas=0;
                                                                        Movimiento[] lista_movimientos=(Movimiento[])part.getMovimientos().toArray(new Movimiento[0]);
                                                                        for(int mo=0; mo<lista_movimientos.length; mo++)
                                                                        {
                                                                            Almacen almacen=lista_movimientos[mo].getAlmacen();
                                                                            if(almacen.getTipoMovimiento()==1)
                                                                                entradas++;
                                                                            else
                                                                                salidas++;
                                                                        }
                                                                        if(entradas!=salidas)
                                                                            permiso=false;
                                                                    }
                                                                    if(permiso==true)
                                                                    {
                                                                        ejem = (Ejemplar)session.get(Ejemplar.class, ejem.getIdParte());
                                                                        //part.setEjemplar(ejem);
                                                                        Criteria crit = session.createCriteria(Partida.class);
                                                                        crit.add(Restrictions.eq("ejemplar.idParte", ejem.getIdParte()));
                                                                        crit = crit.createAlias("pedido", "ped");
                                                                        //crit=crit.addOrder(Order.asc("ped.fechaPedido"));
                                                                        crit=crit.addOrder(Order.desc("pcp"));
                                                                        crit.add(Restrictions.isNotNull("pedido"));
                                                                        Partida partidaPrecio=(Partida) crit.setMaxResults(1).uniqueResult();
                                                                        part.setEjemplar(ejem);
                                                                        boolean permiso2=true;
                                                                        if(ejem.getInventario()==1)
                                                                        {
                                                                            if(part.getPedido()==null)
                                                                            {
                                                                                if(part.getCam()!=-1 && part.getIntCamb()!=-1)
                                                                                    part.setSurteAlmacen(true);
                                                                                part.setRefComp(false);
                                                                            }
                                                                            else
                                                                                permiso2=false;
                                                                        }
                                                                        else
                                                                        {
                                                                            if(part.getCam()!=-1 && part.getIntCamb()!=-1)
                                                                                part.setRefComp(true);
                                                                            part.setSurteAlmacen(false);
                                                                        }
                                                                        if(permiso2==true)
                                                                        {
                                                                            if(partidaPrecio!=null)
                                                                            {
                                                                                if(partidaPrecio.getPcp()>part.getCU())
                                                                                {
                                                                                    part.setCU(partidaPrecio.getPcp());
                                                                                    part.setPrecioCiaSegurosCU(0.0+Math.round(part.getCU()/(1-(part.getPorcentaje()*0.01))));
                                                                                }
                                                                            }
                                                                            session.update(part);
                                                                            session.getTransaction().commit();
                                                                            vector.setElementAt(ejem.getIdParte(), col);
                                                                            this.dataVector.setElementAt(vector, row);
                                                                            fireTableCellUpdated(row, col);
                                                                        }
                                                                        else
                                                                            JOptionPane.showMessageDialog(null, "No se puede cambiar ya que la partida ya fue pedida");
                                                                    }
                                                                    else
                                                                        JOptionPane.showMessageDialog(null, "La partida ya Tiene Entregas no se puede modificar");

                                                                    if(session.isOpen()==true)
                                                                    {
                                                                        session.flush();
                                                                        session.clear();
                                                                        session.close();
                                                                    }
                                                                }
                                                            }
                                                            break;
                                                    }
                                                }
                                            }
                                            else
                                            {
                                               JOptionPane.showMessageDialog(null, "No se pueden modificar partidas enlazadas de otra orden");
                                            }
                                        }
                                        catch(Exception e)
                                        {
                                            e.printStackTrace();
                                            if(session.isOpen()==true)
                                                session.getTransaction().rollback();
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
                                            if(user.getEditarLevantamiento()==true || (((String)t_datos.getValueAt(t_datos.getSelectedRow(), 29)).compareToIgnoreCase("o")==0 && user.getAValuacionLevantamiento()==true && r_cerrar.isSelected()==false) || (((String)t_datos.getValueAt(t_datos.getSelectedRow(), 29)).compareToIgnoreCase("o")!=0 && user.getAValuacionLevantamiento()==true) )
                                            {
                                                if(t_datos.getValueAt(row, 29).toString().compareTo("e")!=0)
                                                {
                                                    Partida part=(Partida) session.createCriteria(Partida.class).add(Restrictions.eq("ordenByIdOrden.idOrden", Integer.parseInt(orden))).add(Restrictions.eq("idEvaluacion", Integer.parseInt(t_datos.getValueAt(t_datos.getSelectedRow(), 0).toString()))).add(Restrictions.eq("subPartida", Integer.parseInt(t_datos.getValueAt(t_datos.getSelectedRow(), 1).toString()))).setMaxResults(1).uniqueResult();
                                                    if(part!=null)
                                                    {
                                                        if(t_datos.getValueAt(row, 33).toString().compareTo("c")==0)
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
                            case 18://PD
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
                                            if(user.getEditarLevantamiento()==true || (((String)t_datos.getValueAt(t_datos.getSelectedRow(), 29)).compareToIgnoreCase("o")==0 && user.getAValuacionLevantamiento()==true && r_cerrar.isSelected()==false) || (((String)t_datos.getValueAt(t_datos.getSelectedRow(), 29)).compareToIgnoreCase("o")!=0 && user.getAValuacionLevantamiento()==true) )
                                            {
                                                if(t_datos.getValueAt(row, 29).toString().compareTo("e")!=0)
                                                {
                                                    Partida part=(Partida) session.createCriteria(Partida.class).add(Restrictions.eq("ordenByIdOrden.idOrden", Integer.parseInt(orden))).add(Restrictions.eq("idEvaluacion", Integer.parseInt(t_datos.getValueAt(t_datos.getSelectedRow(), 0).toString()))).add(Restrictions.eq("subPartida", Integer.parseInt(t_datos.getValueAt(t_datos.getSelectedRow(), 1).toString()))).setMaxResults(1).uniqueResult();
                                                    if(part!=null)
                                                    {
                                                        if(value.toString().compareTo("true")==0)
                                                        {
                                                            part.setPd(true);
                                                            session.update(part);
                                                            session.getTransaction().commit();
                                                            vector.setElementAt(value, col);
                                                            this.dataVector.setElementAt(vector, row);
                                                            fireTableCellUpdated(row, col);;
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
                            case 20://Int Desm
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
                                            if(user.getEditarLevantamiento()==true || (((String)t_datos.getValueAt(t_datos.getSelectedRow(), 29)).compareToIgnoreCase("o")==0 && user.getAValuacionLevantamiento()==true && r_cerrar.isSelected()==false) || (((String)t_datos.getValueAt(t_datos.getSelectedRow(), 29)).compareToIgnoreCase("o")!=0 && user.getAValuacionLevantamiento()==true) )
                                            {
                                                if(t_datos.getValueAt(row, 29).toString().compareTo("e")!=0)
                                                {
                                                    Partida part=(Partida) session.createCriteria(Partida.class).add(Restrictions.eq("ordenByIdOrden.idOrden", Integer.parseInt(orden))).add(Restrictions.eq("idEvaluacion", Integer.parseInt(t_datos.getValueAt(t_datos.getSelectedRow(), 0).toString()))).add(Restrictions.eq("subPartida", Integer.parseInt(t_datos.getValueAt(t_datos.getSelectedRow(), 1).toString()))).setMaxResults(1).uniqueResult();
                                                    if(part!=null)
                                                    {
                                                        if(value.toString().compareTo("true")==0)
                                                        {
                                                            Item item=null;
                                                            if(ord.getServicio()!=null)
                                                                item = (Item)session.createCriteria(Item.class).add(Restrictions.and(Restrictions.eq("catalogo.idCatalogo", part.getCatalogo().getIdCatalogo()), Restrictions.eq("servicio.idServicio", ord.getServicio().getIdServicio()))).setMaxResults(1).uniqueResult();
                                                            if(part.getPedido()==null)
                                                            {
                                                                boolean permiso=true;
                                                                if(part.isSurteAlmacen()==true)
                                                                {
                                                                    int entradas=0, salidas=0;
                                                                    Movimiento[] lista_movimientos=(Movimiento[])part.getMovimientos().toArray(new Movimiento[0]);
                                                                    for(int mo=0; mo<lista_movimientos.length; mo++)
                                                                    {
                                                                        Almacen almacen=lista_movimientos[mo].getAlmacen();
                                                                        if(almacen.getTipoMovimiento()==1)
                                                                            entradas++;
                                                                        else
                                                                            salidas++;
                                                                    }
                                                                    if(entradas!=salidas)
                                                                        permiso=false;
                                                                }
                                                                if(permiso==true)
                                                                {
                                                                    if(item!=null)
                                                                        part.setIntDesm(item.getIntDesm());
                                                                    else
                                                                    {
                                                                        if(part.getCatalogo().getCDesm()>0)
                                                                            part.setIntDesm(part.getCatalogo().getCDesm());
                                                                        else
                                                                            part.setIntDesm(0);
                                                                    }
                                                                    part.setIntCamb(-1);
                                                                    part.setRefComp(false);
                                                                    part.setAutorizado(false);
                                                                    part.setSurteAlmacen(false);

                                                                    session.update(part);
                                                                    session.getTransaction().commit();
                                                                    vector.setElementAt(value, col);
                                                                    this.dataVector.setElementAt(vector, row);
                                                                    fireTableCellUpdated(row, col);

                                                                    habilita=true;
                                                                    t_datos.setValueAt(false, row, 21);
                                                                    habilita=false;
                                                                }
                                                                else
                                                                    JOptionPane.showMessageDialog(null, "La partida ya Tiene Entregas no se puede modificar");
                                                                
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
                                
                            case 21://Int cam
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
                                            if(user.getEditarLevantamiento()==true || (((String)t_datos.getValueAt(t_datos.getSelectedRow(), 29)).compareToIgnoreCase("o")==0 && user.getAValuacionLevantamiento()==true && r_cerrar.isSelected()==false) || (((String)t_datos.getValueAt(t_datos.getSelectedRow(), 29)).compareToIgnoreCase("o")!=0 && user.getAValuacionLevantamiento()==true) )
                                            {
                                                if(t_datos.getValueAt(row, 29).toString().compareTo("e")!=0)
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
                                                                    if(part.getCam()>-1)
                                                                    {
                                                                        if(part.getOrdenByIdOrden().getRCotizaCierre()==null)
                                                                        {
                                                                            if(part.getOrdenByIdOrden().getCierreRefacciones()==null)
                                                                            {
                                                                                Item item=null;
                                                                                if(ord.getServicio()!=null)
                                                                                    item= (Item)session.createCriteria(Item.class).add(Restrictions.and(Restrictions.eq("catalogo.idCatalogo", part.getCatalogo().getIdCatalogo()), Restrictions.eq("servicio.idServicio", ord.getServicio().getIdServicio()))).setMaxResults(1).uniqueResult();
                                                                                if(item!=null)
                                                                                {
                                                                                    part.setIntCamb(item.getIntCamb());
                                                                                }
                                                                                else
                                                                                {
                                                                                    if(part.getCatalogo().getCCamb()>0)
                                                                                    {
                                                                                        part.setIntCamb(part.getCatalogo().getCCamb());
                                                                                    }
                                                                                    else
                                                                                    {
                                                                                        part.setIntCamb(0);
                                                                                    }
                                                                                }
                                                                                part.setIntDesm(-1);
                                                                                if(part.getEjemplar()!=null && part.getEjemplar().getInventario()==1 && (boolean)t_datos.getValueAt(row, 7)==true)
                                                                                {
                                                                                    part.setSurteAlmacen(true);
                                                                                    part.setRefComp(false);
                                                                                    part.setAutorizado(true);
                                                                                    if(part.getTipo().compareTo("o")==0 && part.getOrdenByIdOrden().getRLevantamientoCierre()!=null){
                                                                                        part.setTipo("o");
                                                                                    }
                                                                                }
                                                                                else
                                                                                {
                                                                                    if((boolean)t_datos.getValueAt(row, 7)==true)
                                                                                    {
                                                                                        part.setSurteAlmacen(false);
                                                                                        part.setRefComp(true);
                                                                                        part.setAutorizado(true);
                                                                                        if(part.getTipo().compareTo("o")==0 && part.getOrdenByIdOrden().getRLevantamientoCierre()!=null){
                                                                                            part.setTipo("o");
                                                                                        }
                                                                                    }
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
                                                                                t_datos.setValueAt(false, row, 20);//Int desm
                                                                                t_datos.setValueAt(false, row, 22);//Int Rep min
                                                                                t_datos.setValueAt(false, row, 23);//Int Rep med
                                                                                t_datos.setValueAt(false, row, 24);//Int Rep Max
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
                                                                                JOptionPane.showMessageDialog(null, "Las Compras ya estan cerradas, solicitar la Apertura al Depto de Compras");
                                                                            }
                                                                        }
                                                                        else
                                                                        {
                                                                            JOptionPane.showMessageDialog(null, "Las Cotizaciones ya estan cerradas, solicitar la Apertura al Depto de Compras");
                                                                        }
                                                                    }
                                                                    else
                                                                        JOptionPane.showMessageDialog(null, "No puedes cambiar sin cambiar en aseguradora");
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
                                                                boolean permiso=true;
                                                                if(part.isSurteAlmacen()==true)
                                                                {
                                                                    int entradas=0, salidas=0;
                                                                    Movimiento[] lista_movimientos=(Movimiento[])part.getMovimientos().toArray(new Movimiento[0]);
                                                                    for(int mo=0; mo<lista_movimientos.length; mo++)
                                                                    {
                                                                        Almacen almacen=lista_movimientos[mo].getAlmacen();
                                                                        if(almacen.getTipoMovimiento()==1)
                                                                            entradas++;
                                                                        else
                                                                            salidas++;
                                                                    }
                                                                    System.out.println("Entradas:"+entradas+" Salidad:"+salidas);
                                                                    if(entradas!=salidas)
                                                                        permiso=false;
                                                                }
                                                                if(permiso==true)
                                                                {
                                                                    part.setIntCamb(-1);
                                                                    part.setSurteAlmacen(false);
                                                                    part.setRefComp(false);
                                                                    part.setAutorizado(false);

                                                                    session.update(part);
                                                                    session.getTransaction().commit();
                                                                    vector.setElementAt(value, col);
                                                                    this.dataVector.setElementAt(vector, row);
                                                                    fireTableCellUpdated(row, col);
                                                                }
                                                                else
                                                                    JOptionPane.showMessageDialog(null, "La partida ya Tiene Entregas no se puede modificar");
                                                                
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
                                    break;
                                
                            case 22://Int Rep Mim
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
                                            if(user.getEditarLevantamiento()==true || (((String)t_datos.getValueAt(t_datos.getSelectedRow(), 29)).compareToIgnoreCase("o")==0 && user.getAValuacionLevantamiento()==true && r_cerrar.isSelected()==false) || (((String)t_datos.getValueAt(t_datos.getSelectedRow(), 29)).compareToIgnoreCase("o")!=0 && user.getAValuacionLevantamiento()==true) )
                                            {
                                                if(t_datos.getValueAt(row, 29).toString().compareTo("e")!=0)
                                                {
                                                    Partida part=(Partida) session.createCriteria(Partida.class).add(Restrictions.eq("ordenByIdOrden.idOrden", Integer.parseInt(orden))).add(Restrictions.eq("idEvaluacion", Integer.parseInt(t_datos.getValueAt(t_datos.getSelectedRow(), 0).toString()))).add(Restrictions.eq("subPartida", Integer.parseInt(t_datos.getValueAt(t_datos.getSelectedRow(), 1).toString()))).setMaxResults(1).uniqueResult();
                                                    if(part!=null)
                                                    {
                                                        Item item=null;
                                                        if(ord.getServicio()!=null)
                                                            item= (Item)session.createCriteria(Item.class).add(Restrictions.and(Restrictions.eq("catalogo.idCatalogo", part.getCatalogo().getIdCatalogo()), Restrictions.eq("servicio.idServicio", ord.getServicio().getIdServicio()))).setMaxResults(1).uniqueResult();
                                                        if(value.toString().compareTo("true")==0)
                                                        {
                                                            if(part.getPedido()==null)
                                                            {
                                                                boolean permiso=true;
                                                                if(part.isSurteAlmacen()==true)
                                                                {
                                                                    int entradas=0, salidas=0;
                                                                    Movimiento[] lista_movimientos=(Movimiento[])part.getMovimientos().toArray(new Movimiento[0]);
                                                                    for(int mo=0; mo<lista_movimientos.length; mo++)
                                                                    {
                                                                        Almacen almacen=lista_movimientos[mo].getAlmacen();
                                                                        if(almacen.getTipoMovimiento()==1)
                                                                            entradas++;
                                                                        else
                                                                            salidas++;
                                                                    }
                                                                    if(entradas!=salidas)
                                                                        permiso=false;
                                                                }
                                                                if(permiso==true)
                                                                {
                                                                    if(item!=null)
                                                                        part.setIntRepMin(item.getIntRepMin());
                                                                    else
                                                                    {
                                                                        if(part.getCatalogo().getCRepMin()>0)
                                                                            part.setIntRepMin(part.getCatalogo().getCRepMin());
                                                                        else
                                                                            part.setIntRepMin(0);
                                                                    }
                                                                    part.setIntRepMed(-1);
                                                                    part.setIntRepMax(-1);

                                                                    part.setIntCamb(-1);
                                                                    part.setRefComp(false);
                                                                    part.setAutorizado(false);
                                                                    part.setSurteAlmacen(false);

                                                                    session.update(part);
                                                                    session.getTransaction().commit();
                                                                    vector.setElementAt(value, col);
                                                                    this.dataVector.setElementAt(vector, row);
                                                                    fireTableCellUpdated(row, col);

                                                                    habilita=true;
                                                                    t_datos.setValueAt(false, row, 23);//Int Rep med
                                                                    t_datos.setValueAt(false, row, 24);//Int Rep Max
                                                                    t_datos.setValueAt(false, row, 21);//Int cam
                                                                    habilita=false;
                                                                }
                                                                else
                                                                    JOptionPane.showMessageDialog(null, "La partida ya Tiene Entregas no se puede modificar");
                                                                
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
                                
                            case 23://Int Rep Med
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
                                            if(user.getEditarLevantamiento()==true || (((String)t_datos.getValueAt(t_datos.getSelectedRow(), 29)).compareToIgnoreCase("o")==0 && user.getAValuacionLevantamiento()==true && r_cerrar.isSelected()==false) || (((String)t_datos.getValueAt(t_datos.getSelectedRow(), 29)).compareToIgnoreCase("o")!=0 && user.getAValuacionLevantamiento()==true) )
                                            {
                                                if(t_datos.getValueAt(row, 29).toString().compareTo("e")!=0)
                                                {
                                                    Partida part=(Partida) session.createCriteria(Partida.class).add(Restrictions.eq("ordenByIdOrden.idOrden", Integer.parseInt(orden))).add(Restrictions.eq("idEvaluacion", Integer.parseInt(t_datos.getValueAt(t_datos.getSelectedRow(), 0).toString()))).add(Restrictions.eq("subPartida", Integer.parseInt(t_datos.getValueAt(t_datos.getSelectedRow(), 1).toString()))).setMaxResults(1).uniqueResult();
                                                    if(part!=null)
                                                    {
                                                        Item item=null;
                                                        if(ord.getServicio()!=null)
                                                            item= (Item)session.createCriteria(Item.class).add(Restrictions.and(Restrictions.eq("catalogo.idCatalogo", part.getCatalogo().getIdCatalogo()), Restrictions.eq("servicio.idServicio", ord.getServicio().getIdServicio()))).setMaxResults(1).uniqueResult();
                                                        if(value.toString().compareTo("true")==0)
                                                        {
                                                            if(part.getPedido()==null)
                                                            {
                                                                boolean permiso=true;
                                                                if(part.isSurteAlmacen()==true)
                                                                {
                                                                    int entradas=0, salidas=0;
                                                                    Movimiento[] lista_movimientos=(Movimiento[])part.getMovimientos().toArray(new Movimiento[0]);
                                                                    for(int mo=0; mo<lista_movimientos.length; mo++)
                                                                    {
                                                                        Almacen almacen=lista_movimientos[mo].getAlmacen();
                                                                        if(almacen.getTipoMovimiento()==1)
                                                                            entradas++;
                                                                        else
                                                                            salidas++;
                                                                    }
                                                                    if(entradas!=salidas)
                                                                        permiso=false;
                                                                }
                                                                if(permiso==true)
                                                                {
                                                                    part.setIntRepMin(-1);
                                                                    if(item!=null)
                                                                        part.setIntRepMed(item.getIntRepMed());
                                                                    else
                                                                    {
                                                                        if(part.getCatalogo().getCRepMed()>0)
                                                                            part.setIntRepMed(part.getCatalogo().getCRepMed());
                                                                        else
                                                                            part.setIntRepMed(0);
                                                                    }
                                                                    part.setIntRepMax(-1);

                                                                    part.setIntCamb(-1);
                                                                    part.setRefComp(false);
                                                                    part.setAutorizado(false);
                                                                    part.setSurteAlmacen(false);

                                                                    session.update(part);
                                                                    session.getTransaction().commit();
                                                                    vector.setElementAt(value, col);
                                                                    this.dataVector.setElementAt(vector, row);
                                                                    fireTableCellUpdated(row, col);

                                                                    habilita=true;
                                                                    t_datos.setValueAt(false, row, 22);//Int Rep min
                                                                    t_datos.setValueAt(false, row, 24);//Int Rep Max
                                                                    t_datos.setValueAt(false, row, 21);//Int cam
                                                                    habilita=false;
                                                                }
                                                                else
                                                                    JOptionPane.showMessageDialog(null, "La partida ya Tiene Entregas no se puede modificar");
                                                                
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
                                
                            case 24://Int Rep Max
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
                                            if(user.getEditarLevantamiento()==true || (((String)t_datos.getValueAt(t_datos.getSelectedRow(), 29)).compareToIgnoreCase("o")==0 && user.getAValuacionLevantamiento()==true && r_cerrar.isSelected()==false) || (((String)t_datos.getValueAt(t_datos.getSelectedRow(), 29)).compareToIgnoreCase("o")!=0 && user.getAValuacionLevantamiento()==true) )
                                            {
                                                if(t_datos.getValueAt(row, 29).toString().compareTo("e")!=0)
                                                {
                                                    Partida part=(Partida) session.createCriteria(Partida.class).add(Restrictions.eq("ordenByIdOrden.idOrden", Integer.parseInt(orden))).add(Restrictions.eq("idEvaluacion", Integer.parseInt(t_datos.getValueAt(t_datos.getSelectedRow(), 0).toString()))).add(Restrictions.eq("subPartida", Integer.parseInt(t_datos.getValueAt(t_datos.getSelectedRow(), 1).toString()))).setMaxResults(1).uniqueResult();
                                                    if(part!=null)
                                                    {
                                                        Item item=null;
                                                        if(ord.getServicio()!=null)
                                                            item = (Item)session.createCriteria(Item.class).add(Restrictions.and(Restrictions.eq("catalogo.idCatalogo", part.getCatalogo().getIdCatalogo()), Restrictions.eq("servicio.idServicio", ord.getServicio().getIdServicio()))).setMaxResults(1).uniqueResult();
                                                        if(value.toString().compareTo("true")==0)
                                                        {   
                                                            if(part.getPedido()==null)
                                                            {
                                                                boolean permiso=true;
                                                                if(part.isSurteAlmacen()==true)
                                                                {
                                                                    int entradas=0, salidas=0;
                                                                    Movimiento[] lista_movimientos=(Movimiento[])part.getMovimientos().toArray(new Movimiento[0]);
                                                                    for(int mo=0; mo<lista_movimientos.length; mo++)
                                                                    {
                                                                        Almacen almacen=lista_movimientos[mo].getAlmacen();
                                                                        if(almacen.getTipoMovimiento()==1)
                                                                            entradas++;
                                                                        else
                                                                            salidas++;
                                                                    }
                                                                    if(entradas!=salidas)
                                                                        permiso=false;
                                                                }
                                                                if(permiso==true)
                                                                {
                                                                    part.setIntRepMin(-1);
                                                                    part.setIntRepMed(-1);
                                                                    if(item!=null)
                                                                        part.setIntRepMax(item.getIntRepMax());
                                                                    else
                                                                    {
                                                                        if(part.getCatalogo().getCRepMax()>0)
                                                                            part.setIntRepMax(part.getCatalogo().getCRepMax());
                                                                        else
                                                                            part.setIntRepMax(0);
                                                                    }

                                                                    part.setIntCamb(-1);
                                                                    part.setRefComp(false);
                                                                    part.setAutorizado(false);
                                                                    part.setSurteAlmacen(false);

                                                                    session.update(part);
                                                                    session.getTransaction().commit();
                                                                    vector.setElementAt(value, col);
                                                                    this.dataVector.setElementAt(vector, row);
                                                                    fireTableCellUpdated(row, col);

                                                                    habilita=true;
                                                                    t_datos.setValueAt(false, row, 22);//Int Rep min
                                                                    t_datos.setValueAt(false, row, 23);//Int Rep Med
                                                                    t_datos.setValueAt(false, row, 21);//Int cam
                                                                    habilita=false;
                                                                }
                                                                else
                                                                    JOptionPane.showMessageDialog(null, "La partida ya Tiene Entregas no se puede modificar");
                                                                
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
                                
                            case 25://Pin min
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
                                            if(user.getEditarLevantamiento()==true || (((String)t_datos.getValueAt(t_datos.getSelectedRow(), 29)).compareToIgnoreCase("o")==0 && user.getAValuacionLevantamiento()==true && r_cerrar.isSelected()==false) || (((String)t_datos.getValueAt(t_datos.getSelectedRow(), 29)).compareToIgnoreCase("o")!=0 && user.getAValuacionLevantamiento()==true) )
                                            {
                                                if(t_datos.getValueAt(row, 29).toString().compareTo("e")!=0)
                                                {
                                                    Partida part=(Partida) session.createCriteria(Partida.class).add(Restrictions.eq("ordenByIdOrden.idOrden", Integer.parseInt(orden))).add(Restrictions.eq("idEvaluacion", Integer.parseInt(t_datos.getValueAt(t_datos.getSelectedRow(), 0).toString()))).add(Restrictions.eq("subPartida", Integer.parseInt(t_datos.getValueAt(t_datos.getSelectedRow(), 1).toString()))).setMaxResults(1).uniqueResult();
                                                    if(part!=null)
                                                    {
                                                        Item item=null;
                                                        if(ord.getServicio()!=null)
                                                            item= (Item)session.createCriteria(Item.class).add(Restrictions.and(Restrictions.eq("catalogo.idCatalogo", part.getCatalogo().getIdCatalogo()), Restrictions.eq("servicio.idServicio", ord.getServicio().getIdServicio()))).setMaxResults(1).uniqueResult();
                                                        if(value.toString().compareTo("true")==0)
                                                        {
                                                            if(item!=null)
                                                                part.setIntPinMin(item.getIntPinMin());
                                                            else
                                                            {
                                                                if(part.getCatalogo().getCPinMin()>0)
                                                                    part.setIntPinMin(part.getCatalogo().getCPinMin());
                                                                else
                                                                    part.setIntPinMin(0);
                                                            }
                                                            part.setIntPinMed(-1);
                                                            part.setIntPinMax(-1);
                                                            
                                                            session.update(part);
                                                            session.getTransaction().commit();
                                                            vector.setElementAt(value, col);
                                                            this.dataVector.setElementAt(vector, row);
                                                            fireTableCellUpdated(row, col);
                                                            
                                                            habilita=true;
                                                            t_datos.setValueAt(false, row, 26);//Pin med
                                                            t_datos.setValueAt(false, row, 27);//Pin max
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
                                
                            case 26://Pin med
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
                                            if(user.getEditarLevantamiento()==true || (((String)t_datos.getValueAt(t_datos.getSelectedRow(), 29)).compareToIgnoreCase("o")==0 && user.getAValuacionLevantamiento()==true && r_cerrar.isSelected()==false) || (((String)t_datos.getValueAt(t_datos.getSelectedRow(), 29)).compareToIgnoreCase("o")!=0 && user.getAValuacionLevantamiento()==true) )
                                            {
                                                if(t_datos.getValueAt(row, 29).toString().compareTo("e")!=0)
                                                {
                                                    Partida part=(Partida) session.createCriteria(Partida.class).add(Restrictions.eq("ordenByIdOrden.idOrden", Integer.parseInt(orden))).add(Restrictions.eq("idEvaluacion", Integer.parseInt(t_datos.getValueAt(t_datos.getSelectedRow(), 0).toString()))).add(Restrictions.eq("subPartida", Integer.parseInt(t_datos.getValueAt(t_datos.getSelectedRow(), 1).toString()))).setMaxResults(1).uniqueResult();
                                                    if(part!=null)
                                                    {
                                                        Item item=null;
                                                        if(ord.getServicio()!=null)
                                                            item= (Item)session.createCriteria(Item.class).add(Restrictions.and(Restrictions.eq("catalogo.idCatalogo", part.getCatalogo().getIdCatalogo()), Restrictions.eq("servicio.idServicio", ord.getServicio().getIdServicio()))).setMaxResults(1).uniqueResult();
                                                        if(value.toString().compareTo("true")==0)
                                                        {
                                                            part.setIntPinMin(-1);
                                                            if(item!=null)
                                                                part.setIntPinMed(item.getIntPinMed());
                                                            else
                                                            {
                                                                if(part.getCatalogo().getCPinMed()>0)
                                                                    part.setIntPinMed(part.getCatalogo().getCPinMed());
                                                                else
                                                                    part.setIntPinMed(0);
                                                            }
                                                            part.setIntPinMax(-1);
                                                            session.update(part);
                                                            session.getTransaction().commit();
                                                            vector.setElementAt(value, col);
                                                            this.dataVector.setElementAt(vector, row);
                                                            fireTableCellUpdated(row, col);
                                                            
                                                            habilita=true;
                                                            t_datos.setValueAt(false, row, 25);//Pin min
                                                            t_datos.setValueAt(false, row, 27);//Pin max
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
                                
                            case 27://Pin max
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
                                            if(user.getEditarLevantamiento()==true || (((String)t_datos.getValueAt(t_datos.getSelectedRow(), 29)).compareToIgnoreCase("o")==0 && user.getAValuacionLevantamiento()==true && r_cerrar.isSelected()==false) || (((String)t_datos.getValueAt(t_datos.getSelectedRow(), 29)).compareToIgnoreCase("o")!=0 && user.getAValuacionLevantamiento()==true) )
                                            {
                                                if(t_datos.getValueAt(row, 29).toString().compareTo("e")!=0)
                                                {
                                                    Partida part=(Partida) session.createCriteria(Partida.class).add(Restrictions.eq("ordenByIdOrden.idOrden", Integer.parseInt(orden))).add(Restrictions.eq("idEvaluacion", Integer.parseInt(t_datos.getValueAt(t_datos.getSelectedRow(), 0).toString()))).add(Restrictions.eq("subPartida", Integer.parseInt(t_datos.getValueAt(t_datos.getSelectedRow(), 1).toString()))).setMaxResults(1).uniqueResult();
                                                    if(part!=null)
                                                    {
                                                        Item item=null;
                                                        if(ord.getServicio()!=null)
                                                            item = (Item)session.createCriteria(Item.class).add(Restrictions.and(Restrictions.eq("catalogo.idCatalogo", part.getCatalogo().getIdCatalogo()), Restrictions.eq("servicio.idServicio", ord.getServicio().getIdServicio()))).setMaxResults(1).uniqueResult();
                                                        if(value.toString().compareTo("true")==0)
                                                        {
                                                            part.setIntPinMin(-1);
                                                            part.setIntPinMed(-1);
                                                            if(item!=null)
                                                                part.setIntPinMax(item.getIntPinMax());
                                                            else
                                                            {
                                                                if(part.getCatalogo().getCPinMax()>0)
                                                                    part.setIntPinMax(part.getCatalogo().getCPinMax());
                                                                else
                                                                    part.setIntPinMax(0);
                                                            }
                                                            
                                                            session.update(part);
                                                            session.getTransaction().commit();
                                                            vector.setElementAt(value, col);
                                                            this.dataVector.setElementAt(vector, row);
                                                            fireTableCellUpdated(row, col);
                                                            
                                                            habilita=true;
                                                            t_datos.setValueAt(false, row, 25);//Pin min
                                                            t_datos.setValueAt(false, row, 26);//Pin med
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
                                
                            case 28://instruccion
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
                                            if(user.getEditarLevantamiento()==true || (((String)t_datos.getValueAt(t_datos.getSelectedRow(), 29)).compareToIgnoreCase("o")==0 && user.getAValuacionLevantamiento()==true && r_cerrar.isSelected()==false) || (((String)t_datos.getValueAt(t_datos.getSelectedRow(), 29)).compareToIgnoreCase("o")!=0 && user.getAValuacionLevantamiento()==true) )
                                            {
                                                if(t_datos.getValueAt(row, 29).toString().compareTo("e")!=0)
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

                            case 31://Prioridad
                                    if(vector.get(col)==null)
                                    {
                                        switch((Integer)value)
                                        {
                                            case 1:
                                                vector.setElementAt(new ImageIcon("imagenes/1.png"), col);
                                                this.dataVector.setElementAt(vector, row);
                                                fireTableCellUpdated(row, col);
                                                break;
                                            case 2:
                                                vector.setElementAt(new ImageIcon("imagenes/2.png"), col);
                                                this.dataVector.setElementAt(vector, row);
                                                fireTableCellUpdated(row, col);
                                                break;
                                            case 3:
                                                vector.setElementAt(new ImageIcon("imagenes/3.png"), col);
                                                this.dataVector.setElementAt(vector, row);
                                                fireTableCellUpdated(row, col);
                                                break;
                                            case 4:
                                                vector.setElementAt(new ImageIcon("imagenes/4.png"), col);
                                                this.dataVector.setElementAt(vector, row);
                                                fireTableCellUpdated(row, col);
                                                break;
                                        }
                                    }
                                    else
                                    {
                                        Session session = HibernateUtil.getSessionFactory().openSession();
                                        try
                                        {
                                            session.beginTransaction().begin();
                                            user = (Usuario)session.get(Usuario.class, user.getIdUsuario());
                                            if(user.getEditarLevantamiento()==true || (((String)t_datos.getValueAt(t_datos.getSelectedRow(), 29)).compareToIgnoreCase("o")==0 && user.getAValuacionLevantamiento()==true && r_cerrar.isSelected()==false) || (((String)t_datos.getValueAt(t_datos.getSelectedRow(), 29)).compareToIgnoreCase("o")!=0 && user.getAValuacionLevantamiento()==true) )
                                            {
                                                if(t_datos.getValueAt(row, 29).toString().compareTo("e")!=0)
                                                {
                                                    if(vector!=null)
                                                    {
                                                        Partida part=(Partida) session.createCriteria(Partida.class).add(Restrictions.eq("ordenByIdOrden.idOrden", Integer.parseInt(orden))).add(Restrictions.eq("idEvaluacion", Integer.parseInt(t_datos.getValueAt(t_datos.getSelectedRow(), 0).toString()))).add(Restrictions.eq("subPartida", Integer.parseInt(t_datos.getValueAt(t_datos.getSelectedRow(), 1).toString()))).setMaxResults(1).uniqueResult();
                                                        if(part!=null)
                                                        {
                                                            part.setPrioridad(Integer.parseInt(value.toString()));
                                                            session.update(part);
                                                            session.getTransaction().commit();
                                                            switch(Integer.parseInt(value.toString()))
                                                            {
                                                                case 1:
                                                                    vector.setElementAt(new ImageIcon("imagenes/1.png"), col);
                                                                    this.dataVector.setElementAt(vector, row);
                                                                    fireTableCellUpdated(row, col);
                                                                    break;
                                                                case 2:
                                                                    vector.setElementAt(new ImageIcon("imagenes/2.png"), col);
                                                                    this.dataVector.setElementAt(vector, row);
                                                                    fireTableCellUpdated(row, col);
                                                                    break;
                                                                case 3:
                                                                    vector.setElementAt(new ImageIcon("imagenes/3.png"), col);
                                                                    this.dataVector.setElementAt(vector, row);
                                                                    fireTableCellUpdated(row, col);
                                                                    break;
                                                                case 4:
                                                                    vector.setElementAt(new ImageIcon("imagenes/4.png"), col);
                                                                    this.dataVector.setElementAt(vector, row);
                                                                    fireTableCellUpdated(row, col);
                                                                    break;
                                                            }
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
                                            e.printStackTrace();
                                            session.getTransaction().rollback();
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
                                
                            case 32:
                                    if(vector.get(col)==null)
                                    {
                                        vector.setElementAt(value, col);
                                        this.dataVector.setElementAt(vector, row);
                                        fireTableCellUpdated(row, col);
                                    }
                                    else
                                    {
                                        //if((user.getEditarLevantamiento()==true || (((String)t_datos.getValueAt(t_datos.getSelectedRow(), 32)).compareToIgnoreCase("o")==0 && user.getAValuacionLevantamiento()==true && r_cerrar.isSelected()==false) || (((String)t_datos.getValueAt(t_datos.getSelectedRow(), 32)).compareToIgnoreCase("o")!=0 && user.getAValuacionLevantamiento()==true) ) && (t_datos.getValueAt(row, 32).toString().compareTo("e")!=0))
                                        if((user.getAValuacionLevantamiento()==true && r_cerrar.isSelected()==false) || user.getEditarLevantamiento()==true)
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
                                                            if(part.getTipo().compareTo("o")==0 && part.getOrdenByIdOrden().getRLevantamientoCierre()!=null){
                                                                part.setTipo("c");
                                                                t_datos.setValueAt("c", row, 29);
                                                            }
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
                                                                boolean permiso=true;
                                                                if(part.isSurteAlmacen()==true)
                                                                {
                                                                    int entradas=0, salidas=0;
                                                                    Movimiento[] lista_movimientos=(Movimiento[])part.getMovimientos().toArray(new Movimiento[0]);
                                                                    for(int mo=0; mo<lista_movimientos.length; mo++)
                                                                    {
                                                                        Almacen almacen=lista_movimientos[mo].getAlmacen();
                                                                        if(almacen.getTipoMovimiento()==1)
                                                                            entradas++;
                                                                        else
                                                                            salidas++;
                                                                    }
                                                                    if(entradas!=salidas)
                                                                        permiso=false;
                                                                }
                                                                if(permiso==true)
                                                                {
                                                                    part.setAutorizadoValuacion(false);
                                                                    session.update(part);
                                                                    session.getTransaction().commit();
                                                                    vector.setElementAt(value, col);
                                                                    this.dataVector.setElementAt(vector, row);
                                                                    fireTableCellUpdated(row, col);
                                                                }
                                                                else
                                                                    JOptionPane.showMessageDialog(null, "La partida ya tiene entregas");
                                                                    
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
            c_plantilla.removeAllItems();
            c_plantilla.addItem("PLANTILLAS");
            for(int p=0; p<servicios.size(); p++)
            {
                Servicio ser=(Servicio)servicios.get(p);
                c_plantilla.addItem(ser.getIdServicio());
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
    
    private void SumaTotal()
    {
        if(orden!=null)
        {
            double total=0.0;
            Session session = HibernateUtil.getSessionFactory().openSession();
            try
            {
                //Orden ord = (Orden)session.get(Orden.class, Integer.parseInt(orden));
                Query query = session.createSQLQuery("select sum(cantidad*consumible.precio)as tot from consumible \n" +
"left join ejemplar on consumible.id_Parte=ejemplar.id_Parte where id_orden="+orden+" order by id_consumible;");  
                query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
                ArrayList partidas=(ArrayList)query.list();
                if(partidas.size()>0)
                {
                    java.util.HashMap map=(java.util.HashMap)partidas.get(0);
                    try{
                    total = Double.parseDouble(map.get("tot").toString());
                    }catch(Exception e){total =0.0d;}
                    t_total_consumible.setValue(total);
                }
                else
                    t_total_consumible.setValue(0.0d);
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
            t_total_consumible.setValue(0.0d);
    }
}
