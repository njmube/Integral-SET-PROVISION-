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
import Hibernate.entidades.Movimiento;
import Hibernate.entidades.Orden;
import Hibernate.entidades.Partida;
import Hibernate.entidades.Usuario;
import Servicios.EnviarCorreo;
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
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
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
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellRangeAddress;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.hibernate.Criteria;
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
import Integral.PeticionPost;
import Integral.VerticalBarUI;
import Integral.VerticalTableHeaderCellRenderer;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.math.RoundingMode;
import java.util.ArrayList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableModel;
import org.hibernate.Query;
import org.json.JSONObject;

/**
 *
 * @author I.S.C Salvador
 */
public class valuacion extends javax.swing.JPanel implements	ListSelectionListener{

    /**
     * Creates new form valuacionDirecta
     */
    //private javax.swing.JDialog ventana;
    private String orden;
    private Usuario user;
    String sessionPrograma="";
    ListSelectionModel selectionModel;
    Herramientas h;
    int x=0, entro=0;
    String[] columnas = new String [] {
        "No","#","Descripcion","D/M","Rep Min","Rep Med","Rep Max","Cam","Pin",
        "Can","Med","Inst","Codigo","Costo c/u","%","$ Cia. Seg","Can Aut","$ Aut. c/u","$ Aut. Tot","OK",
        "Aut","Hrs.","R.Cot","R.Com","S.Alm","SO","PD","Ori","Pedido",
        "I. DM","Camb","Rep Min","Rep Med","Rep Max","Pin Min","Pin Med","Pin Max", "Tipo", "Orden"};
    String[] columnas2 = new String [] {
        "No","#","Descripcion"};
    private Partida aux;
    Orden ord;
    FormatoTabla formato;
    MyModel model;
    MyModel model2;
    int menu;
    int foto=0;
    boolean [] edita_columnas;
    String estado, rutaFtp;
    Class[] types;
    int configuracion=1;
    

    
    public valuacion(String ord, Usuario us, String edo, String ses, int op, int configuracion, String rutaFtp) {
        initComponents();
        this.rutaFtp=rutaFtp;
        scroll.getVerticalScrollBar().setUI(new VerticalBarUI());
        scroll.getHorizontalScrollBar().setUI(new HorizontalBarUI());
        estado=edo;
        menu=op;
        this.configuracion=configuracion;
        sessionPrograma=ses;
        orden=ord;
        user=us;
        /*cb_hojalateria.setEnabled(true);
        cb_mecanica.setEnabled(true);
        cb_suspension.setEnabled(true);
        cb_electrico.setEnabled(true);
        cb_pintura.setEnabled(true);*/
        

        if(menu==4)
        {
            edita_columnas=new boolean[]{true/*horas mo*/,false/*costo c/u*/, false/*%*/, true/*cant aut*/, true/*$ autorizado*/, true/*autorizar partida*/,true/*r cot*/, true/*r com*/, true/*tipo de surtido*/, false /*surte almacen*/, true /*Origende pieza*/};
            t_ok.setVisible(false);
            b_ok.setVisible(false);
            bt.setVisible(true);
            r_cerrar_cotizacion.setVisible(false);
            r_cerrar_valuacion.setVisible(true);
        }
        else
        {
            edita_columnas=new boolean[]{false/*horas mo*/,true/*costo c/u*/, true/*%*/, false/*cant aut*/, false/*$ autorizado*/, false/*autorizar partida*/,false/*r cot*/, false/*r com*/, false/*tipo de surtido*/, false /*surte almacen*/, false /*Origende pieza*/};
            t_ok.setVisible(true);
            b_ok.setVisible(true);
            bt.setVisible(true);
            r_cerrar_cotizacion.setVisible(true);
            r_cerrar_valuacion.setVisible(false);
        }
            types = new Class [] {
                    java.lang.String.class/*No*/, 
                    java.lang.String.class/*#*/, 
                    java.lang.String.class/*Descripcion*/, 
                    java.lang.Double.class/*D/M*/, 
                    java.lang.Double.class/*Rep Min*/, 
                    java.lang.Double.class/*Rep Med*/, 
                    java.lang.Double.class/*Rep Max*/, 
                    java.lang.Double.class/*Cam*/, 
                    java.lang.Double.class/*Pin*/, 
                    java.lang.Double.class/*Can*/, 
                    java.lang.String.class/*Med*/, 
                    java.lang.String.class/*Ins*/,
                    java.lang.String.class/*Cod*/, 
                    java.lang.Double.class/*Costo c/u*/,
                    java.lang.Double.class/*%*/, 
                    java.lang.Double.class/*cia seguroa*/, 
                    java.lang.Double.class/*cant autorizada*/, 
                    java.lang.Double.class/*$ aut c/u*/, 
                    java.lang.Double.class,/*$ aut tot*/
                    java.lang.Boolean.class,/*$ vales*/ 
                    java.lang.Boolean.class/*autorizado*/, 
                    java.lang.Double.class/*Horas*/,
                    java.lang.Boolean.class/*R cot*/,
                    java.lang.Boolean.class/*R com*/,
                    java.lang.Boolean.class/*S. Almacen*/,
                    java.lang.Boolean.class/*SO*/,
                    java.lang.Boolean.class/*PD*/,                    
                    java.lang.String.class/*Ori*/,
                    java.lang.String.class/*Pedido*/,
                    java.lang.Boolean.class/*I. DM*/,
                    java.lang.Boolean.class/*Camb*/,
                    java.lang.Boolean.class/*Rep Min*/,
                    java.lang.Boolean.class/**Rep Med*/,
                    java.lang.Boolean.class/*Rep Max*/,
                    java.lang.Boolean.class/*Pin Min*/,
                    java.lang.Boolean.class/*Pin Med*/,
                    java.lang.Boolean.class/*Pin Max*/,
                    java.lang.String.class/*Tipo*/,
                    java.lang.String.class/*Orden*/
                };
            this.l5.setVisible(false);
            this.t_porcentaje.setVisible(false);

        this.repaint();
        t_datos.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        formato = new FormatoTabla();
        buscaCuentas(-1,-1);
        SumaTotal();
        verEstado();
        h=new Herramientas(user, 0);
        if(h.isCerrada(orden)==true)
        {
            visualiza(false);
            t_deducible.setEnabled(false);
            t_demerito.setEnabled(false);
            b_ok.setEnabled(false);
            b_ac.setEnabled(false);
            b_consumibles.setEnabled(false);
            t_ref_autorizadas_directo.setEnabled(false);
            t_mo_directa.setEnabled(false);
            t_mo_directa1.setEnabled(false);
            r_cerrar_valuacion.setEnabled(false);
            this.r_cerrar_cotizacion.setEnabled(false);
        }
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
                  case 0://N°
                      column.setPreferredWidth(54);
                      //column.setCellRenderer(tcr);
                      break;
                  case 1://#
                      column.setPreferredWidth(30);
                      //column.setCellRenderer(tcr);
                      break;
                  case 2://descripcion
                      column.setPreferredWidth(290);
                      break;
                  case 3://D/M
                      column.setPreferredWidth(40);
                      break;
                  case 4://rep min
                      column.setPreferredWidth(40);
                      break;
                  case 5://rep med
                      column.setPreferredWidth(40);
                      break;
                  case 6://rep max
                      column.setPreferredWidth(40);
                      break;
                  case 7://camb
                      column.setPreferredWidth(40);
                      break;
                  case 8://pintar
                      column.setPreferredWidth(40);
                      break;
                  case 9://cant
                      column.setPreferredWidth(45);
                      break;
                  case 10://medida
                      column.setPreferredWidth(50);
                      DefaultCellEditor editor = new DefaultCellEditor(medida);
                      column.setCellEditor(editor); 
                      editor.setClickCountToStart(2);
                      break;
                  case 11://foliof
                      column.setPreferredWidth(100);
                      break;
                      
                  case 12://codigo
                      column.setPreferredWidth(100);
                      DefaultCellEditor miEditor = new DefaultCellEditor(numeros);
                      miEditor.setClickCountToStart(2);
                      column.setCellEditor(miEditor);
                      break;

                  case 13://Costo c/u
                      column.setPreferredWidth(75);
                      break;
                  
                  case 14://%
                      column.setPreferredWidth(35);
                      break;
                      
                  case 15://precio cia seg
                      column.setPreferredWidth(75);
                      break;

                  case 16://cant autorizada
                      column.setPreferredWidth(50);
                      break;
                      
                  case 17://precio aut c/u
                      column.setPreferredWidth(75);
                      break;
                      
                  case 18://precio aut tot
                      column.setPreferredWidth(75);
                      break;
                      
                  case 21://horas
                      column.setPreferredWidth(50);
                      break;

                  case 27://origen
                      column.setPreferredWidth(40);
                      DefaultCellEditor miEditor1 = new DefaultCellEditor(tipo);
                      miEditor1.setClickCountToStart(2);
                      column.setCellEditor(miEditor1);
                      break;
                      
                  case 28://Pedido
                      column.setPreferredWidth(50);
                      break;
                  
                  case 38://enlazada
                          column.setPreferredWidth(70);
                      break;
                      
                  default:
                      column.setPreferredWidth(17);
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

        numeros = new javax.swing.JComboBox();
        medida = new javax.swing.JComboBox();
        instruccion = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        t_cantidad = new javax.swing.JFormattedTextField();
        ventanaReportes = new javax.swing.JDialog();
        jPanel5 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        cb_precio = new javax.swing.JComboBox();
        jButton2 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        r_parte = new javax.swing.JRadioButton();
        jButton5 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        cb_partidas = new javax.swing.JComboBox();
        jLabel7 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        cb_tipo = new javax.swing.JComboBox();
        jButton1 = new javax.swing.JButton();
        b_exel1 = new javax.swing.JButton();
        t_num = new javax.swing.JFormattedTextField();
        l3 = new javax.swing.JLabel();
        t_ref_presupuesto = new javax.swing.JFormattedTextField();
        l1 = new javax.swing.JLabel();
        l5 = new javax.swing.JLabel();
        t_porcentaje = new javax.swing.JTextField();
        ventanaConsumible = new javax.swing.JDialog();
        jPanel9 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        cb_hojalateria = new javax.swing.JCheckBox();
        cb_mecanica = new javax.swing.JCheckBox();
        cb_suspension = new javax.swing.JCheckBox();
        cb_electrico = new javax.swing.JCheckBox();
        cb_pintura = new javax.swing.JCheckBox();
        jLabel18 = new javax.swing.JLabel();
        jButton7 = new javax.swing.JButton();
        t_hojalateria = new javax.swing.JFormattedTextField();
        t_mecanica = new javax.swing.JFormattedTextField();
        t_suspension = new javax.swing.JFormattedTextField();
        t_electrico = new javax.swing.JFormattedTextField();
        t_pintura = new javax.swing.JFormattedTextField();
        b_exel2 = new javax.swing.JButton();
        b_consumibles = new javax.swing.JButton();
        tipo = new javax.swing.JComboBox();
        jPanel1 = new javax.swing.JPanel();
        b_pdf = new javax.swing.JButton();
        b_exel = new javax.swing.JButton();
        b_enviar = new javax.swing.JButton();
        b_enviar.setContentAreaFilled(false);
        r_cerrar_cotizacion = new javax.swing.JRadioButton();
        r_cerrar_valuacion = new javax.swing.JRadioButton();
        jPanel6 = new javax.swing.JPanel();
        t_costo_refacciones = new javax.swing.JFormattedTextField();
        jLabel3 = new javax.swing.JLabel();
        l2 = new javax.swing.JLabel();
        t_ref_autorizadas_directo = new javax.swing.JFormattedTextField();
        jLabel5 = new javax.swing.JLabel();
        t_cia_seguros = new javax.swing.JFormattedTextField();
        jLabel6 = new javax.swing.JLabel();
        t_autorizado = new javax.swing.JFormattedTextField();
        t_autorizado1 = new javax.swing.JFormattedTextField();
        jLabel8 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        t_horas = new javax.swing.JFormattedTextField();
        jLabel13 = new javax.swing.JLabel();
        t_importe = new javax.swing.JFormattedTextField();
        l4 = new javax.swing.JLabel();
        t_mo_directa = new javax.swing.JFormattedTextField();
        t_mo_directa1 = new javax.swing.JFormattedTextField();
        l6 = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        t_total_consumible = new javax.swing.JFormattedTextField();
        scroll = new javax.swing.JScrollPane();
        t_datos = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        t_ok = new javax.swing.JTextField();
        b_ok = new javax.swing.JButton();
        bt = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        t_busca = new javax.swing.JTextField();
        b_busca = new javax.swing.JButton();
        jLabel29 = new javax.swing.JLabel();
        t_deducible = new javax.swing.JFormattedTextField();
        jLabel30 = new javax.swing.JLabel();
        t_demerito = new javax.swing.JFormattedTextField();
        b_ac = new javax.swing.JButton();
        bt1 = new javax.swing.JButton();

        numeros.setFont(new java.awt.Font("Dialog", 0, 9)); // NOI18N
        numeros.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Cancelar", "Eliminar", "Buscar", "Nuevo" }));
        numeros.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                numerosFocusLost(evt);
            }
        });

        medida.setFont(new java.awt.Font("Dialog", 0, 9)); // NOI18N
        medida.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "PZAS", "LTS", "MTS", "CMS", "MMS", "GRS", "MLS", "KGS", "HRS", "MIN", "KIT", "FT", "LB", "JGO", "NA" }));

        instruccion.setText("jTextField1");
        instruccion.setBorder(null);
        instruccion.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                instruccionKeyTyped(evt);
            }
        });

        t_cantidad.setBorder(null);
        t_cantidad.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0"))));
        t_cantidad.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        t_cantidad.setEnabled(false);
        t_cantidad.setFont(new java.awt.Font("Dialog", 0, 10)); // NOI18N

        ventanaReportes.setMinimumSize(new java.awt.Dimension(270, 160));
        ventanaReportes.setModal(true);
        ventanaReportes.setResizable(false);

        jPanel5.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));

        jPanel4.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));

        cb_precio.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Seleccionar", "Compañia", "Autorizado", "Cotizado", "Compra" }));
        cb_precio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cb_precioActionPerformed(evt);
            }
        });

        jButton2.setBackground(new java.awt.Color(2, 135, 242));
        jButton2.setText("Ref.precio   ");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton4.setBackground(new java.awt.Color(2, 135, 242));
        jButton4.setText("Solo compra");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        r_parte.setText("Agregar NP");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton4, javax.swing.GroupLayout.DEFAULT_SIZE, 98, Short.MAX_VALUE)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(r_parte)
                            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jButton2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 98, Short.MAX_VALUE)
                                .addComponent(cb_precio, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton4)
                .addGap(8, 8, 8)
                .addComponent(jButton2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cb_precio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(r_parte))
        );

        jButton5.setBackground(new java.awt.Color(2, 135, 242));
        jButton5.setText("Reparaciones");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jButton3.setBackground(new java.awt.Color(2, 135, 242));
        jButton3.setText("Ref. sin precio");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        cb_partidas.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Marcadas", "Todas", "Autorizadas", "Sin Autorizar" }));

        jLabel7.setText("Partidas:");

        jLabel2.setText("Especialidad");

        cb_tipo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Todas", "Hojalateria", "Mecanica", "Suspension", "Electrico", "Pintura" }));
        cb_tipo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cb_tipoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton5)
                    .addComponent(jButton3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(cb_tipo, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel7)
                    .addComponent(cb_partidas, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(9, 9, 9)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cb_tipo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cb_partidas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton3))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jButton1.setBackground(new java.awt.Color(2, 135, 242));
        jButton1.setText("Valuación");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        b_exel1.setIcon(new ImageIcon("imagenes/xls_icon.png"));
        b_exel1.setToolTipText("Exporta a EXCEL");
        b_exel1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_exel1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout ventanaReportesLayout = new javax.swing.GroupLayout(ventanaReportes.getContentPane());
        ventanaReportes.getContentPane().setLayout(ventanaReportesLayout);
        ventanaReportesLayout.setHorizontalGroup(
            ventanaReportesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ventanaReportesLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(ventanaReportesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(ventanaReportesLayout.createSequentialGroup()
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(b_exel1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        ventanaReportesLayout.setVerticalGroup(
            ventanaReportesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ventanaReportesLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(ventanaReportesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(b_exel1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 9, Short.MAX_VALUE))
        );

        t_num.setBorder(null);
        t_num.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        t_num.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        t_num.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                t_numFocusGained(evt);
            }
        });

        l3.setFont(new java.awt.Font("Arial", 0, 9)); // NOI18N
        l3.setForeground(new java.awt.Color(255, 255, 255));
        l3.setText("M.O. Presupuestada:");

        t_ref_presupuesto.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        t_ref_presupuesto.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.00"))));
        t_ref_presupuesto.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        t_ref_presupuesto.setText("0.00");
        t_ref_presupuesto.setDisabledTextColor(new java.awt.Color(2, 38, 253));
        t_ref_presupuesto.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        t_ref_presupuesto.setNextFocusableComponent(t_busca);
        t_ref_presupuesto.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                t_ref_presupuestoFocusLost(evt);
            }
        });
        t_ref_presupuesto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                t_ref_presupuestoActionPerformed(evt);
            }
        });

        l1.setFont(new java.awt.Font("Arial", 0, 9)); // NOI18N
        l1.setForeground(new java.awt.Color(255, 255, 255));
        l1.setText("Ref.Presupuesto:");

        l5.setFont(new java.awt.Font("Arial", 0, 9)); // NOI18N
        l5.setForeground(new java.awt.Color(255, 255, 255));
        l5.setText("% de utilidad:");

        t_porcentaje.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        t_porcentaje.setEnabled(false);
        t_porcentaje.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                t_porcentajeActionPerformed(evt);
            }
        });
        t_porcentaje.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                t_porcentajeKeyTyped(evt);
            }
        });

        ventanaConsumible.setTitle("ASIGNACIÓN PRESUPUESTO DE CONSUMIBLES");
        ventanaConsumible.setBackground(new java.awt.Color(255, 255, 255));
        ventanaConsumible.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        ventanaConsumible.setModalityType(java.awt.Dialog.ModalityType.APPLICATION_MODAL);

        jPanel9.setBackground(new java.awt.Color(255, 255, 255));
        jPanel9.setPreferredSize(new java.awt.Dimension(375, 250));

        jLabel9.setFont(new java.awt.Font("Times New Roman", 0, 11)); // NOI18N
        jLabel9.setText("HOJALATERÍA:");

        jLabel10.setFont(new java.awt.Font("Times New Roman", 0, 11)); // NOI18N
        jLabel10.setText("MECÁNICA:");

        jLabel12.setFont(new java.awt.Font("Times New Roman", 0, 11)); // NOI18N
        jLabel12.setText("SUSPENSIÓN:");

        jLabel14.setFont(new java.awt.Font("Times New Roman", 0, 11)); // NOI18N
        jLabel14.setText("ELÉCTRICO:");

        jLabel15.setFont(new java.awt.Font("Times New Roman", 0, 11)); // NOI18N
        jLabel15.setText("PINTURA:");

        jLabel16.setFont(new java.awt.Font("Times New Roman", 1, 11)); // NOI18N
        jLabel16.setText("ESPECIALIDAD");

        jLabel17.setFont(new java.awt.Font("Times New Roman", 1, 11)); // NOI18N
        jLabel17.setText("DESTAJO");

        cb_hojalateria.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cb_hojalateriaActionPerformed(evt);
            }
        });

        cb_mecanica.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cb_mecanicaActionPerformed(evt);
            }
        });

        cb_suspension.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cb_suspensionActionPerformed(evt);
            }
        });

        cb_electrico.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cb_electricoActionPerformed(evt);
            }
        });

        cb_pintura.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cb_pinturaActionPerformed(evt);
            }
        });

        jLabel18.setFont(new java.awt.Font("Times New Roman", 1, 11)); // NOI18N
        jLabel18.setText("PRESUPUESTO");

        jButton7.setBackground(new java.awt.Color(2, 135, 242));
        jButton7.setForeground(new java.awt.Color(255, 255, 255));
        jButton7.setText("Guardar");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        t_hojalateria.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        t_hojalateria.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.00"))));
        t_hojalateria.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        t_hojalateria.setText("0.00");
        t_hojalateria.setDisabledTextColor(new java.awt.Color(2, 38, 253));
        t_hojalateria.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        t_hojalateria.setNextFocusableComponent(t_busca);
        t_hojalateria.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                t_hojalateriaFocusLost(evt);
            }
        });

        t_mecanica.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        t_mecanica.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.00"))));
        t_mecanica.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        t_mecanica.setText("0.00");
        t_mecanica.setDisabledTextColor(new java.awt.Color(2, 38, 253));
        t_mecanica.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        t_mecanica.setNextFocusableComponent(t_busca);
        t_mecanica.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                t_mecanicaFocusLost(evt);
            }
        });

        t_suspension.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        t_suspension.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.00"))));
        t_suspension.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        t_suspension.setText("0.00");
        t_suspension.setDisabledTextColor(new java.awt.Color(2, 38, 253));
        t_suspension.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        t_suspension.setNextFocusableComponent(t_busca);
        t_suspension.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                t_suspensionFocusLost(evt);
            }
        });

        t_electrico.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        t_electrico.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.00"))));
        t_electrico.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        t_electrico.setText("0.00");
        t_electrico.setDisabledTextColor(new java.awt.Color(2, 38, 253));
        t_electrico.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        t_electrico.setNextFocusableComponent(t_busca);
        t_electrico.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                t_electricoFocusLost(evt);
            }
        });

        t_pintura.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        t_pintura.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.00"))));
        t_pintura.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        t_pintura.setText("0.00");
        t_pintura.setDisabledTextColor(new java.awt.Color(2, 38, 253));
        t_pintura.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        t_pintura.setNextFocusableComponent(t_busca);
        t_pintura.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                t_pinturaFocusLost(evt);
            }
        });

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel16)
                    .addComponent(jLabel9)
                    .addComponent(jLabel10)
                    .addComponent(jLabel12)
                    .addComponent(jLabel14)
                    .addComponent(jLabel15))
                .addGap(29, 29, 29)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(t_hojalateria)
                            .addComponent(t_mecanica)
                            .addComponent(t_suspension, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(t_electrico, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(t_pintura, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel18))
                .addGap(47, 47, 47)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel17, javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                            .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(cb_mecanica)
                                .addComponent(cb_hojalateria)
                                .addComponent(cb_suspension)
                                .addComponent(cb_electrico)
                                .addComponent(cb_pintura))
                            .addGap(20, 20, 20)))
                    .addComponent(jButton7))
                .addContainerGap(30, Short.MAX_VALUE))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel9Layout.createSequentialGroup()
                                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel16)
                                    .addComponent(jLabel18))
                                .addGap(18, 18, 18)
                                .addComponent(jLabel9))
                            .addGroup(jPanel9Layout.createSequentialGroup()
                                .addComponent(jLabel17)
                                .addGap(18, 18, 18)
                                .addComponent(cb_hojalateria)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cb_mecanica)
                            .addComponent(jLabel10))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel9Layout.createSequentialGroup()
                                .addComponent(jLabel12)
                                .addGap(10, 10, 10))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                                .addComponent(cb_suspension)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cb_electrico)
                            .addComponent(jLabel14))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel15)
                            .addComponent(cb_pintura)))
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGap(31, 31, 31)
                        .addComponent(t_hojalateria, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(t_mecanica, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(t_suspension, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(t_electrico, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(t_pintura, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addComponent(jButton7)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout ventanaConsumibleLayout = new javax.swing.GroupLayout(ventanaConsumible.getContentPane());
        ventanaConsumible.getContentPane().setLayout(ventanaConsumibleLayout);
        ventanaConsumibleLayout.setHorizontalGroup(
            ventanaConsumibleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        ventanaConsumibleLayout.setVerticalGroup(
            ventanaConsumibleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        b_exel2.setIcon(new ImageIcon("imagenes/xls_icon.png"));
        b_exel2.setToolTipText("Exporta a EXCEL");
        b_exel2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_exel2ActionPerformed(evt);
            }
        });

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

        tipo.setFont(new java.awt.Font("Dialog", 0, 9)); // NOI18N
        tipo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "-", "ori", "nal", "des" }));
        tipo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tipoMouseClicked(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                tipoMouseReleased(evt);
            }
        });
        tipo.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                tipoPopupMenuWillBecomeInvisible(evt);
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            }
        });
        tipo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tipoActionPerformed(evt);
            }
        });
        tipo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                tipoFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                tipoFocusLost(evt);
            }
        });

        setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        setLayout(new java.awt.BorderLayout());

        jPanel1.setBackground(new java.awt.Color(2, 135, 242));
        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        b_pdf.setIcon(new ImageIcon("imagenes/pdf_icon.png"));
        b_pdf.setToolTipText("Exporta a PDF");
        b_pdf.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_pdfActionPerformed(evt);
            }
        });
        jPanel1.add(b_pdf, new org.netbeans.lib.awtextra.AbsoluteConstraints(940, 10, 23, 23));

        b_exel.setIcon(new ImageIcon("imagenes/xls_icon.png"));
        b_exel.setToolTipText("Exporta a EXCEL");
        b_exel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_exelActionPerformed(evt);
            }
        });
        jPanel1.add(b_exel, new org.netbeans.lib.awtextra.AbsoluteConstraints(970, 10, 23, 23));

        b_enviar.setIcon(new ImageIcon("imagenes/send.png"));
        b_enviar.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        b_enviar.setPressedIcon(new ImageIcon("imagenes/send2.png"));
        b_enviar.setRolloverIcon(new ImageIcon("imagenes/send1.png"));
        b_enviar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                b_enviarMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                b_enviarMouseExited(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                b_enviarMouseReleased(evt);
            }
        });
        b_enviar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_enviarActionPerformed(evt);
            }
        });
        jPanel1.add(b_enviar, new org.netbeans.lib.awtextra.AbsoluteConstraints(1000, 10, 23, 23));

        r_cerrar_cotizacion.setFont(new java.awt.Font("Tahoma", 0, 9)); // NOI18N
        r_cerrar_cotizacion.setForeground(new java.awt.Color(255, 255, 255));
        r_cerrar_cotizacion.setText("Cerrar cotizacion");
        r_cerrar_cotizacion.setToolTipText("Al marcar esta casilla el levantamiento se cerrara.");
        r_cerrar_cotizacion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                r_cerrar_cotizacionActionPerformed(evt);
            }
        });
        jPanel1.add(r_cerrar_cotizacion, new org.netbeans.lib.awtextra.AbsoluteConstraints(790, 30, -1, -1));

        r_cerrar_valuacion.setFont(new java.awt.Font("Tahoma", 0, 9)); // NOI18N
        r_cerrar_valuacion.setForeground(new java.awt.Color(255, 255, 255));
        r_cerrar_valuacion.setText("Autorizar valuación");
        r_cerrar_valuacion.setToolTipText("Al marcar esta casilla la valuacion  se cerrara.");
        r_cerrar_valuacion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                r_cerrar_valuacionActionPerformed(evt);
            }
        });
        jPanel1.add(r_cerrar_valuacion, new org.netbeans.lib.awtextra.AbsoluteConstraints(790, 10, -1, -1));

        jPanel6.setBackground(new java.awt.Color(2, 135, 242));
        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "Refacciones", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Tahoma", 1, 10))); // NOI18N
        jPanel6.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        t_costo_refacciones.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        t_costo_refacciones.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.00"))));
        t_costo_refacciones.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        t_costo_refacciones.setText("0.00");
        t_costo_refacciones.setDisabledTextColor(new java.awt.Color(2, 38, 253));
        t_costo_refacciones.setEnabled(false);
        t_costo_refacciones.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        t_costo_refacciones.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                t_costo_refaccionesFocusGained(evt);
            }
        });
        t_costo_refacciones.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                t_costo_refaccionesActionPerformed(evt);
            }
        });
        jPanel6.add(t_costo_refacciones, new org.netbeans.lib.awtextra.AbsoluteConstraints(47, 13, 88, -1));

        jLabel3.setFont(new java.awt.Font("Arial", 0, 9)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Cotizado:");
        jPanel6.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(7, 16, -1, -1));

        l2.setFont(new java.awt.Font("Arial", 0, 9)); // NOI18N
        l2.setForeground(new java.awt.Color(255, 255, 255));
        l2.setText("Cot. Final:");
        jPanel6.add(l2, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 15, -1, -1));

        t_ref_autorizadas_directo.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        t_ref_autorizadas_directo.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.00"))));
        t_ref_autorizadas_directo.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        t_ref_autorizadas_directo.setText("0.00");
        t_ref_autorizadas_directo.setDisabledTextColor(new java.awt.Color(2, 38, 253));
        t_ref_autorizadas_directo.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        t_ref_autorizadas_directo.setNextFocusableComponent(t_busca);
        t_ref_autorizadas_directo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                t_ref_autorizadas_directoFocusLost(evt);
            }
        });
        t_ref_autorizadas_directo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                t_ref_autorizadas_directoActionPerformed(evt);
            }
        });
        jPanel6.add(t_ref_autorizadas_directo, new org.netbeans.lib.awtextra.AbsoluteConstraints(188, 13, 88, -1));

        jLabel5.setFont(new java.awt.Font("Arial", 0, 9)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Cia/Seg:");
        jPanel6.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(7, 37, -1, -1));

        t_cia_seguros.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        t_cia_seguros.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.00"))));
        t_cia_seguros.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        t_cia_seguros.setText("0.00");
        t_cia_seguros.setDisabledTextColor(new java.awt.Color(2, 38, 253));
        t_cia_seguros.setEnabled(false);
        t_cia_seguros.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        jPanel6.add(t_cia_seguros, new org.netbeans.lib.awtextra.AbsoluteConstraints(47, 35, 88, -1));

        jLabel6.setFont(new java.awt.Font("Arial", 0, 9)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Autorizado:");
        jPanel6.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 38, -1, -1));

        t_autorizado.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        t_autorizado.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.00"))));
        t_autorizado.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        t_autorizado.setText("0.00");
        t_autorizado.setDisabledTextColor(new java.awt.Color(2, 38, 253));
        t_autorizado.setEnabled(false);
        t_autorizado.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        jPanel6.add(t_autorizado, new org.netbeans.lib.awtextra.AbsoluteConstraints(188, 35, 88, -1));

        t_autorizado1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        t_autorizado1.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.00"))));
        t_autorizado1.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        t_autorizado1.setText("0.00");
        t_autorizado1.setDisabledTextColor(new java.awt.Color(2, 38, 253));
        t_autorizado1.setEnabled(false);
        t_autorizado1.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        jPanel6.add(t_autorizado1, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 35, 88, -1));

        jLabel8.setFont(new java.awt.Font("Arial", 0, 9)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("Total en Vales");
        jPanel6.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(293, 20, -1, -1));

        jPanel1.add(jPanel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(2, 2, 375, 60));

        jPanel7.setBackground(new java.awt.Color(2, 135, 242));
        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "Mano de Obra", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Tahoma", 1, 10))); // NOI18N
        jPanel7.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel11.setFont(new java.awt.Font("Arial", 0, 9)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setText("Horas:");
        jPanel7.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(7, 16, -1, -1));

        t_horas.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        t_horas.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.00"))));
        t_horas.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        t_horas.setText("0.00");
        t_horas.setDisabledTextColor(new java.awt.Color(2, 38, 253));
        t_horas.setEnabled(false);
        t_horas.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        jPanel7.add(t_horas, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 13, 88, -1));

        jLabel13.setFont(new java.awt.Font("Arial", 0, 9)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setText("Cot.:");
        jPanel7.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(7, 38, -1, -1));

        t_importe.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        t_importe.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.00"))));
        t_importe.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        t_importe.setText("0.00");
        t_importe.setDisabledTextColor(new java.awt.Color(2, 38, 253));
        t_importe.setEnabled(false);
        t_importe.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        jPanel7.add(t_importe, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 35, 88, -1));

        l4.setFont(new java.awt.Font("Arial", 0, 9)); // NOI18N
        l4.setForeground(new java.awt.Color(255, 255, 255));
        l4.setText("Directa:");
        jPanel7.add(l4, new org.netbeans.lib.awtextra.AbsoluteConstraints(132, 40, -1, -1));

        t_mo_directa.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        t_mo_directa.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.00"))));
        t_mo_directa.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        t_mo_directa.setText("0.00");
        t_mo_directa.setDisabledTextColor(new java.awt.Color(2, 38, 253));
        t_mo_directa.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        t_mo_directa.setNextFocusableComponent(t_busca);
        t_mo_directa.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                t_mo_directaFocusLost(evt);
            }
        });
        t_mo_directa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                t_mo_directaActionPerformed(evt);
            }
        });
        jPanel7.add(t_mo_directa, new org.netbeans.lib.awtextra.AbsoluteConstraints(178, 35, 88, -1));

        t_mo_directa1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        t_mo_directa1.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.00"))));
        t_mo_directa1.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        t_mo_directa1.setText("0.00");
        t_mo_directa1.setDisabledTextColor(new java.awt.Color(2, 38, 253));
        t_mo_directa1.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        t_mo_directa1.setNextFocusableComponent(t_busca);
        t_mo_directa1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                t_mo_directa1FocusLost(evt);
            }
        });
        t_mo_directa1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                t_mo_directa1ActionPerformed(evt);
            }
        });
        jPanel7.add(t_mo_directa1, new org.netbeans.lib.awtextra.AbsoluteConstraints(178, 10, 88, -1));

        l6.setFont(new java.awt.Font("Arial", 0, 9)); // NOI18N
        l6.setForeground(new java.awt.Color(255, 255, 255));
        l6.setText("Cot: Final:");
        jPanel7.add(l6, new org.netbeans.lib.awtextra.AbsoluteConstraints(132, 16, -1, -1));

        jPanel1.add(jPanel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 2, 275, 60));

        jPanel8.setBackground(new java.awt.Color(2, 135, 242));
        jPanel8.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "Consumibles", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Tahoma", 1, 10))); // NOI18N
        jPanel8.setLayout(null);

        t_total_consumible.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        t_total_consumible.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.00"))));
        t_total_consumible.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        t_total_consumible.setText("0.00");
        t_total_consumible.setDisabledTextColor(new java.awt.Color(2, 38, 253));
        t_total_consumible.setEnabled(false);
        t_total_consumible.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        jPanel8.add(t_total_consumible);
        t_total_consumible.setBounds(10, 20, 69, 18);

        jPanel1.add(jPanel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(660, 10, 100, 50));

        add(jPanel1, java.awt.BorderLayout.PAGE_END);

        scroll.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        scroll.setPreferredSize(new java.awt.Dimension(453, 150));

        t_datos.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        t_datos.setForeground(new java.awt.Color(102, 102, 102));
        t_datos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "N°", "#", "D/M", "Rep Min ", "Title 5", "Title 6", "Title 7", "Title 8", "Title 9", "Title 10", "Title 11", "Title 12", "Title 13", "Title 14", "Title 15", "Title 16", "Title 17", "Title 18", "Title 19", "Title 20", "Title 21", "Title 22", "Title 23", "Title 24", "Title 25", "Title 26", "Title 27", "Title 28", "Title 29"
            }
        ));
        t_datos.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        t_datos.setGridColor(new java.awt.Color(102, 102, 102));
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

        jPanel3.setBackground(new java.awt.Color(2, 135, 242));

        t_ok.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        t_ok.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                t_okKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                t_okKeyTyped(evt);
            }
        });

        b_ok.setText("Ok");
        b_ok.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_okActionPerformed(evt);
            }
        });

        bt.setText("cotizacion");
        bt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btActionPerformed(evt);
            }
        });

        jButton6.setText("Imperia");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Arial", 0, 9)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Buscar:");

        t_busca.setFont(new java.awt.Font("Arial", 0, 9)); // NOI18N
        t_busca.setBorder(javax.swing.BorderFactory.createEtchedBorder());
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

        b_busca.setIcon(new ImageIcon("imagenes/buscar1.png"));
        b_busca.setToolTipText("Busca una partida");
        b_busca.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_buscaActionPerformed(evt);
            }
        });

        jLabel29.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel29.setForeground(new java.awt.Color(254, 254, 254));
        jLabel29.setText("Deducible:");

        t_deducible.setBackground(new java.awt.Color(204, 255, 255));
        t_deducible.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        t_deducible.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.00"))));
        t_deducible.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        t_deducible.setText("0.00");
        t_deducible.setDisabledTextColor(new java.awt.Color(2, 38, 253));

        jLabel30.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel30.setForeground(new java.awt.Color(254, 254, 254));
        jLabel30.setText("Demerito:");

        t_demerito.setBackground(new java.awt.Color(204, 255, 255));
        t_demerito.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        t_demerito.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.00"))));
        t_demerito.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        t_demerito.setText("0.00");
        t_demerito.setDisabledTextColor(new java.awt.Color(2, 38, 253));

        b_ac.setText("Guardar");
        b_ac.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_acActionPerformed(evt);
            }
        });

        bt1.setText("Particular");
        bt1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bt1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(8, 8, 8)
                .addComponent(t_busca, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(b_busca, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel29)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(t_deducible, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel30)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(t_demerito, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(b_ac)
                .addGap(18, 18, 18)
                .addComponent(bt)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(bt1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 25, Short.MAX_VALUE)
                .addComponent(t_ok, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(b_ok)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(t_ok, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(b_ok)
                        .addComponent(bt)
                        .addComponent(jButton6)
                        .addComponent(jLabel29)
                        .addComponent(t_deducible, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel30)
                        .addComponent(t_demerito, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(b_ac)
                        .addComponent(bt1))
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(t_busca, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(b_busca, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );

        add(jPanel3, java.awt.BorderLayout.PAGE_START);
    }// </editor-fold>//GEN-END:initComponents

    private void b_buscaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_buscaActionPerformed
        // TODO add your handling code here:
        h=new Herramientas(user, 0);
        h.session(sessionPrograma);
        if(this.t_busca.getText().compareToIgnoreCase("")!=0)
        {
            if(x>=t_datos.getRowCount())
            {
                x=0;
                java.awt.Rectangle r = t_datos.getCellRect( x, 3, true);
                t_datos.scrollRectToVisible(r);
            }
            for(; x<t_datos.getRowCount(); x++)
            {
                if(t_datos.getValueAt(x, 2).toString().indexOf(t_busca.getText()) != -1)
                {
                    t_datos.setRowSelectionInterval(x, x);
                    t_datos.setColumnSelectionInterval(2, 2);
                    java.awt.Rectangle r = t_datos.getCellRect( x, 2, true);
                    t_datos.scrollRectToVisible(r);
                    break;
                }
            }
            x++;
        }
    }//GEN-LAST:event_b_buscaActionPerformed

    private void b_pdfActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_pdfActionPerformed
        // TODO add your handling code here:
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize(); 
        ventanaReportes.setSize(404, 220);
        ventanaReportes.setLocation((d.width/2)-202, (d.height/2)-110);
        ventanaReportes.setVisible(true);
    }//GEN-LAST:event_b_pdfActionPerformed

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
            File archivoXLS = new File(ruta+".xls");
            try
            {
                if(archivoXLS.exists())
                archivoXLS.delete();
                archivoXLS.createNewFile();
                Workbook libro = new HSSFWorkbook();
                FileOutputStream archivo = new FileOutputStream(archivoXLS);
                Sheet hoja = libro.createSheet("VALUACION");
                //hoja.protectSheet("04650077");
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
                                if(t_datos.getValueAt(ren-1, col).toString().compareToIgnoreCase("true")==0)
                                    celda.setCellValue("✓");
                                else
                                {
                                    if(t_datos.getValueAt(ren-1, col).toString().compareToIgnoreCase("false")==0)
                                        celda.setCellValue("");
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
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "No se pudo realizar el reporte si el archivo esta abierto");
            }
        }
    }//GEN-LAST:event_b_exelActionPerformed

    private void numerosFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_numerosFocusLost
        // TODO add your handling code here:
        entro=1;
    }//GEN-LAST:event_numerosFocusLost

    private void instruccionKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_instruccionKeyTyped
        // TODO add your handling code here:
        char car = evt.getKeyChar();
        evt.setKeyChar(Character.toUpperCase(evt.getKeyChar()));
        if(instruccion.getText().length()>=100)
        evt.consume();
    }//GEN-LAST:event_instruccionKeyTyped

    private void t_costo_refaccionesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_t_costo_refaccionesActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_t_costo_refaccionesActionPerformed

    private void t_costo_refaccionesFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_t_costo_refaccionesFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_t_costo_refaccionesFocusGained

    private void r_cerrar_cotizacionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_r_cerrar_cotizacionActionPerformed
        // TODO add your handling code here:
        if(r_cerrar_cotizacion.isSelected()==true)
        {
            Session session = HibernateUtil.getSessionFactory().openSession();
            try
            {
                user = (Usuario)session.get(Usuario.class, user.getIdUsuario());
                if(user.getCerrarCotizaciones()==true)
                {
                    h=new Herramientas(user, 0);
                    h.session(sessionPrograma);
                    int opt=JOptionPane.showConfirmDialog(this, "¡Confirma que deseas cerrar la cotización!");
                    if(opt==0)
                    {
                        session.beginTransaction().begin();
                        Orden ord=(Orden)session.get(Orden.class, Integer.parseInt(orden));
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
                            ord.setRCotizaCierre(calendario3.getTime());
                            session.update(ord);
                            session.getTransaction().commit();
                            JOptionPane.showMessageDialog(null, "La cotizacion fue cerrada");
                        buscaCuentas(-1,-1);
                    }
                    else
                        r_cerrar_cotizacion.setSelected(false);
                }
                else
                {
                    r_cerrar_cotizacion.setSelected(false);
                    JOptionPane.showMessageDialog(null, "Acceso denegado");
                }
            }
            catch(Exception e)
            {
                e.printStackTrace();
                session.getTransaction().rollback();
                r_cerrar_cotizacion.setSelected(false);
                JOptionPane.showMessageDialog(null, "Error no se pudo cerrar la cotización");
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
                user = (Usuario)session.get(Usuario.class, user.getIdUsuario());
                if(user.getAbrirCotizaciones()==true)
                {
                    h=new Herramientas(user, 0);
                    h.session(sessionPrograma);
                    int opt=JOptionPane.showConfirmDialog(this, "¡Confirma que deseas abrir la cotización!");
                    if(opt==0)
                    {
                        session.beginTransaction().begin();
                        Orden ord=(Orden)session.get(Orden.class, Integer.parseInt(orden));
                            //***guardar el usuario que abrio el levantamiento*****
                            ord.setRCotizaCierre(null);
                            session.update(ord);
                            session.beginTransaction().commit();
                            JOptionPane.showMessageDialog(null, "La cotización fue abierta");
                        buscaCuentas(-1,-1);
                    }
                    else
                        r_cerrar_cotizacion.setSelected(true);
                }
                else
                {
                    r_cerrar_cotizacion.setSelected(true);
                    JOptionPane.showMessageDialog(null, "Acceso denegado");
                }
            }
            catch(Exception e)
            {
                e.printStackTrace();
                session.beginTransaction().rollback();
                r_cerrar_valuacion.setSelected(true);
                JOptionPane.showMessageDialog(null, "Error no se pudo abrir la cotizacion");
            }
            if(session!=null)
                if(session.isOpen())
                {
                    session.flush();
                    session.clear();
                    session.close();
                }
        }
    }//GEN-LAST:event_r_cerrar_cotizacionActionPerformed

    private void r_cerrar_valuacionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_r_cerrar_valuacionActionPerformed
        // TODO add your handling code here:
        if(r_cerrar_valuacion.isSelected()==true)
        {
            Session session = HibernateUtil.getSessionFactory().openSession();
            try
            {
                user = (Usuario)session.get(Usuario.class, user.getIdUsuario());
                if(user.getCerrarValuacion()==true)
                {
                    h=new Herramientas(user, 0);
                    h.session(sessionPrograma);
                    int opt=JOptionPane.showConfirmDialog(this, "¡Confirma que deseas autorizar la valuación!");
                    if(opt==0)
                    {
                        session.beginTransaction().begin();
                        Orden ord=(Orden)session.get(Orden.class, Integer.parseInt(orden));
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
                            ord.setRValuacionCierre(calendario3.getTime());
                            ord.setUsuarioByRValuacionCierreAsigno(user);
                            session.update(ord);
                            session.beginTransaction().commit();
                            JOptionPane.showMessageDialog(null, "La valuación fue autorizada");
                        buscaCuentas(-1,-1);
                    }
                    else
                        r_cerrar_valuacion.setSelected(false);
                }
                else
                {
                    r_cerrar_valuacion.setSelected(false);
                    JOptionPane.showMessageDialog(null, "Acceso denegado");
                }
            }
            catch(Exception e)
            {
                e.printStackTrace();
                session.getTransaction().rollback();
                r_cerrar_valuacion.setSelected(false);
                JOptionPane.showMessageDialog(null, "Error no se pudo cerrar la valuación");
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
                user = (Usuario)session.get(Usuario.class, user.getIdUsuario());
                if(user.getAbrirValuacion()==true)
                {
                    h=new Herramientas(user, 0);
                    h.session(sessionPrograma);
                    int opt=JOptionPane.showConfirmDialog(this, "¡Confirma que deseas abrir la valuación!");
                    if(opt==0)
                    {
                        session.beginTransaction().begin();
                        Orden ord=(Orden)session.get(Orden.class, Integer.parseInt(orden));
                            //***guardar el usuario que abrio el levantamiento*****
                            ord.setRValuacionCierre(null);
                            ord.setUsuarioByRValuacionCierreAsigno(user);
                            session.update(ord);
                            session.getTransaction().commit();
                            JOptionPane.showMessageDialog(null, "La valuación fue abierta");
                        buscaCuentas(-1,-1);
                    }
                    else
                        r_cerrar_valuacion.setSelected(true);
                }
                else
                {
                    r_cerrar_valuacion.setSelected(true);
                    JOptionPane.showMessageDialog(null, "Acceso denegado");
                }
            }
            catch(Exception e)
            {
                e.printStackTrace();
                session.getTransaction().rollback();
                r_cerrar_valuacion.setSelected(true);
                JOptionPane.showMessageDialog(null, "Error no se pudo abrir la valuación");
            }
            if(session!=null)
                if(session.isOpen())
                {
                    session.flush();
                    session.clear();
                    session.close();
                }
        }
    }//GEN-LAST:event_r_cerrar_valuacionActionPerformed

    private void t_porcentajeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_t_porcentajeActionPerformed
        // TODO add your handling code here:
        if(r_cerrar_valuacion.isSelected()==false)
        {
            Session session = HibernateUtil.getSessionFactory().openSession();
            try
            {
                session.beginTransaction().begin();
                if(t_porcentaje.getText().compareTo("")!=0)
                {
                    ord = (Orden)session.get(Orden.class, Integer.parseInt(orden));
                    Partida[] cuentas = (Partida[])session.createCriteria(Partida.class).add(Restrictions.eq("ordenByIdOrden.idOrden", ord.getIdOrden())).add(Restrictions.eq("autorizadoValuacion", true)).list().toArray(new Partida[0]);
                    if(cuentas!=null)
                    {
                        for(int p=0; p<cuentas.length; p++)
                        {
                            cuentas[p].setPorcentaje(Double.parseDouble(t_porcentaje.getText()));
                            session.update(cuentas[p]);
                        }
                        session.getTransaction().commit();
                        if(session.isOpen()==true)
                        {
                            session.flush();
                            session.clear();
                            session.close();
                        }
                        JOptionPane.showMessageDialog(null, "Las partidas fueron actualizadas");
                    }
                }
                else
                {
                    JOptionPane.showMessageDialog(null, "no se puede introducir datos vacios");
                }
            }
            catch(Exception e)
            {
                session.getTransaction().rollback();
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error al actualizar los datos"); 
            }
            if(session!=null)
                if(session.isOpen()==true)
                {
                    session.flush();
                    session.clear();
                    session.close();
                }
        }
        buscaCuentas(-1,-1);
    }//GEN-LAST:event_t_porcentajeActionPerformed

    private void t_porcentajeKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_porcentajeKeyTyped
        // TODO add your handling code here:
        char car = evt.getKeyChar();
        if(t_porcentaje.getText().length()>=3)
        evt.consume();
        if((car<'0' || car>'9'))
        evt.consume();
    }//GEN-LAST:event_t_porcentajeKeyTyped

    private void t_ref_presupuestoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_t_ref_presupuestoActionPerformed
        // TODO add your handling code here:
        t_busca.requestFocus();
    }//GEN-LAST:event_t_ref_presupuestoActionPerformed

    private void t_ref_presupuestoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_t_ref_presupuestoFocusLost
        // TODO add your handling code here:
        if(t_ref_presupuesto.getText().compareTo("")==0)
        {
            t_ref_presupuesto.setText("0");
            t_ref_presupuesto.setValue(0.00);
        }
        Session session = HibernateUtil.getSessionFactory().openSession();
        try
        {
            session.beginTransaction().begin();
            user = (Usuario)session.get(Usuario.class, user.getIdUsuario());
            //if(user.getEditaRefPresupuesto()==true)
            //{
            h=new Herramientas(user, 0);
            h.session(sessionPrograma);
            ord = (Orden)session.get(Orden.class, Integer.parseInt(orden));
            double valor=Double.parseDouble(t_ref_presupuesto.getText());
            ord.setRefPresupuesto(valor);
            session.update(ord);
            session.getTransaction().commit();
            /*}
            else
                JOptionPane.showMessageDialog(null, "¡Acceso denegado!");*/
        }
        catch(Exception e)
        {
            session.getTransaction().rollback();
            JOptionPane.showMessageDialog(null, "¡Error al actualizar los datos!");
            e.printStackTrace();
        }
        if(session!=null)
            if(session.isOpen()==true)
            {
                session.flush();
                session.clear();
                session.close();
            }
    }//GEN-LAST:event_t_ref_presupuestoFocusLost

    private void t_ref_autorizadas_directoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_t_ref_autorizadas_directoActionPerformed
        // TODO add your handling code here:
        t_busca.requestFocus();
    }//GEN-LAST:event_t_ref_autorizadas_directoActionPerformed

    private void t_ref_autorizadas_directoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_t_ref_autorizadas_directoFocusLost
        // TODO add your handling code here:
        if(t_mo_directa.getText().compareTo("")==0)
        {
            t_mo_directa.setText("0");
            t_mo_directa.setValue(0.00);
        }
        try
        {
            t_mo_directa.commitEdit();
        }catch(Exception e){e.printStackTrace();}
        if(t_mo_directa1.getText().compareTo("")==0)
        {
            t_mo_directa1.setText("0");
            t_mo_directa1.setValue(0.00);
        }
        try
        {
            t_mo_directa1.commitEdit();
        }catch(Exception e){e.printStackTrace();}
        if(t_ref_autorizadas_directo.getText().compareTo("")==0)
        {
            t_ref_autorizadas_directo.setText("0");
            t_ref_autorizadas_directo.setValue(0.00);
        }
        try
        {
            t_ref_autorizadas_directo.commitEdit();
        }catch(Exception e){e.printStackTrace();}
        Session session = HibernateUtil.getSessionFactory().openSession();
        try
        {
            session.getTransaction().begin();
            user = (Usuario)session.get(Usuario.class, user.getIdUsuario());
            h=new Herramientas(user, 0);
            h.session(sessionPrograma);
            ord=(Orden)session.load(Orden.class, ord.getIdOrden());
            ord.setMoDirecta(((Number)t_mo_directa.getValue()).doubleValue());
            ord.setRefAutorizadas(((Number)t_ref_autorizadas_directo.getValue()).doubleValue());
            ord.setMoPresupuestada(((Number)t_mo_directa1.getValue()).doubleValue());
            session.update(ord);
            session.getTransaction().commit();
        }
        catch(Exception e)
        {
            session.getTransaction().rollback();
            JOptionPane.showMessageDialog(null, "¡Error al actualizar los datos!");
            e.printStackTrace();
        }
        if(session!=null)
            if(session.isOpen()==true)
            {
                session.flush();
                session.clear();
                session.close();
            }
    }//GEN-LAST:event_t_ref_autorizadas_directoFocusLost

    private void t_mo_directaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_t_mo_directaActionPerformed
        // TODO add your handling code here:
        t_busca.requestFocus();
    }//GEN-LAST:event_t_mo_directaActionPerformed

    private void t_mo_directaFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_t_mo_directaFocusLost
        // TODO add your handling code here:
        if(t_mo_directa.getText().compareTo("")==0)
        {
            t_mo_directa.setText("0");
            t_mo_directa.setValue(0.00);
        }
        try
        {
            t_mo_directa.commitEdit();
        }catch(Exception e){e.printStackTrace();}
        if(t_mo_directa1.getText().compareTo("")==0)
        {
            t_mo_directa1.setText("0");
            t_mo_directa1.setValue(0.00);
        }
        try
        {
            t_mo_directa1.commitEdit();
        }catch(Exception e){e.printStackTrace();}
        if(t_ref_autorizadas_directo.getText().compareTo("")==0)
        {
            t_ref_autorizadas_directo.setText("0");
            t_ref_autorizadas_directo.setValue(0.00);
        }
        try
        {
            t_ref_autorizadas_directo.commitEdit();
        }catch(Exception e){e.printStackTrace();}
        Session session = HibernateUtil.getSessionFactory().openSession();
        try
        {
            session.getTransaction().begin();
            user = (Usuario)session.get(Usuario.class, user.getIdUsuario());
            h=new Herramientas(user, 0);
            h.session(sessionPrograma);
            ord=(Orden)session.load(Orden.class, ord.getIdOrden());
            ord.setMoDirecta(((Number)t_mo_directa.getValue()).doubleValue());
            ord.setRefAutorizadas(((Number)t_ref_autorizadas_directo.getValue()).doubleValue());
            ord.setMoPresupuestada(((Number)t_mo_directa1.getValue()).doubleValue());
            session.update(ord);
            session.getTransaction().commit();
        }
        catch(Exception e)
        {
            session.getTransaction().rollback();
            JOptionPane.showMessageDialog(null, "¡Error al actualizar los datos!");
            e.printStackTrace();
        }
        if(session!=null)
            if(session.isOpen()==true)
            {
                session.flush();
                session.clear();
                session.close();
            }
    }//GEN-LAST:event_t_mo_directaFocusLost

    private void t_datosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_t_datosMouseClicked
        // TODO add your handling code here:
        /*if(evt!=null)
        {
            Point p = evt.getPoint(); 
            int row = t_datos.rowAtPoint(p); 
            int column = t_datos.columnAtPoint(p); 
            t_datos.setRowSelectionInterval(row, row);
            t_datos.setColumnSelectionInterval(column, column);
        }*/
        /*if(t_datos.getSelectedRow()>=0)
        {
            if(t_datos.getSelectedColumn()==13)
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
                     System.out.println(e);
                }
                if(session!=null)
                     if(session.isOpen()==true)
                         session.close();
            }
        }*/
    }//GEN-LAST:event_t_datosMouseClicked

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        //h=new Herramientas(user, 0);
        //h.session(sessionPrograma);
        Session session = HibernateUtil.getSessionFactory().openSession();
        try
        {
            DecimalFormat formatoPorcentaje = new DecimalFormat("#,##0.00");
            DecimalFormat formatoDecimal = new DecimalFormat("####0.0");
            formatoPorcentaje.setMinimumFractionDigits(2);
            session.beginTransaction().begin();
            BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.WINANSI, BaseFont.NOT_EMBEDDED);
            //Orden ord=buscaApertura();
            PDF reporte = new PDF();
            Date fecha = new Date();
            DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyyHH-mm-ss");//YYYY-MM-DD HH:MM:SS
            String valor=dateFormat.format(fecha);
            File folder = new File("reportes/"+ord.getIdOrden());
            folder.mkdirs();
            reporte.Abrir2(PageSize.LETTER, "Valuación", "reportes/"+ord.getIdOrden()+"/"+valor+"-valuacion.pdf");
            Font font = new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL);
            BaseColor contenido=BaseColor.WHITE;
            int centro=Element.ALIGN_CENTER;
            int izquierda=Element.ALIGN_LEFT;
            int derecha=Element.ALIGN_RIGHT;
            float tam[]=new float[]{15,15,180,14,14,14,14,14,14,9,25};
            PdfPTable tabla=reporte.crearTabla(11, tam, 100, Element.ALIGN_LEFT);
            
            cabecera(reporte, bf, tabla);
            int ren=0;
            double dm=0d, cam=0d, min=0d, med=0d, max=0d, pin=0d, tot=0d;
            for(int i=0; i<t_datos.getRowCount(); i++)
            {
                if(t_datos.getValueAt(i, 37).toString().compareTo("e")!=0)
                {
                    double suma = 0d;
                    double v = 0.0d;
                    tabla.addCell(reporte.celda(""+t_datos.getValueAt(i, 9).toString(), font, contenido, izquierda, 0,1,Rectangle.RECTANGLE));
                    tabla.addCell(reporte.celda(t_datos.getValueAt(i, 10).toString(), font, contenido, izquierda, 0,1,Rectangle.RECTANGLE));
                    tabla.addCell(reporte.celda(t_datos.getValueAt(i, 2).toString(), font, contenido, izquierda, 0,0,Rectangle.RECTANGLE));

                    if(t_datos.getValueAt(i, 3)!=null)
                    {
                        v = Double.parseDouble(t_datos.getValueAt(i, 3).toString()) * Double.parseDouble(t_datos.getValueAt(i, 9).toString());
                        tabla.addCell(reporte.celda(new BigDecimal(v).setScale(2, BigDecimal.ROUND_HALF_UP).toString(), font, contenido, derecha, 0,1,Rectangle.RECTANGLE));
                        suma+=Double.parseDouble(t_datos.getValueAt(i, 3).toString());
                        dm+=v;
                    }
                    else
                        tabla.addCell(reporte.celda("", font, contenido, derecha, 0,1,Rectangle.RECTANGLE));

                    if(t_datos.getValueAt(i, 7)!=null)
                    {
                        v = Double.parseDouble(t_datos.getValueAt(i, 7).toString()) * Double.parseDouble(t_datos.getValueAt(i, 9).toString());
                        tabla.addCell(reporte.celda(new BigDecimal(v).setScale(2, BigDecimal.ROUND_HALF_UP).toString(), font, contenido, derecha, 0,1,Rectangle.RECTANGLE));
                        suma+=Double.parseDouble(t_datos.getValueAt(i, 7).toString());
                        cam+=v;
                    }
                    else
                        tabla.addCell(reporte.celda("", font, contenido, derecha, 0,1,Rectangle.RECTANGLE));

                    if(t_datos.getValueAt(i, 4)!=null)
                    {
                        v = Double.parseDouble(t_datos.getValueAt(i, 4).toString()) * Double.parseDouble(t_datos.getValueAt(i, 9).toString());
                        tabla.addCell(reporte.celda(new BigDecimal(v).setScale(2, BigDecimal.ROUND_HALF_UP).toString(), font, contenido, derecha, 0,1,Rectangle.RECTANGLE));
                        suma+=Double.parseDouble(t_datos.getValueAt(i, 4).toString());
                        min+=v;
                    }
                    else
                        tabla.addCell(reporte.celda("", font, contenido, derecha, 0,1,Rectangle.RECTANGLE));

                    if(t_datos.getValueAt(i, 5)!=null)
                    {
                        v = Double.parseDouble(t_datos.getValueAt(i, 5).toString()) * Double.parseDouble(t_datos.getValueAt(i, 9).toString());
                        tabla.addCell(reporte.celda(new BigDecimal(v).setScale(2, BigDecimal.ROUND_HALF_UP).toString(), font, contenido, derecha, 0,1,Rectangle.RECTANGLE));
                        suma+=Double.parseDouble(t_datos.getValueAt(i, 5).toString());
                        med+=v;
                    }
                    else
                        tabla.addCell(reporte.celda("", font, contenido, derecha, 0,1,Rectangle.RECTANGLE));

                    if(t_datos.getValueAt(i, 6)!=null)
                    {
                        v = Double.parseDouble(t_datos.getValueAt(i, 6).toString()) * Double.parseDouble(t_datos.getValueAt(i, 9).toString());
                        tabla.addCell(reporte.celda(new BigDecimal(v).setScale(2, BigDecimal.ROUND_HALF_UP).toString(), font, contenido, derecha, 0,1,Rectangle.RECTANGLE));
                        suma+=Double.parseDouble(t_datos.getValueAt(i, 6).toString());
                        max+=v;
                    }
                    else
                        tabla.addCell(reporte.celda("", font, contenido, derecha, 0,1,Rectangle.RECTANGLE));
                    if(t_datos.getValueAt(i, 8)!=null)
                    {
                        v = Double.parseDouble(t_datos.getValueAt(i, 8).toString()) * Double.parseDouble(t_datos.getValueAt(i, 9).toString());
                        tabla.addCell(reporte.celda(new BigDecimal(v).setScale(2, BigDecimal.ROUND_HALF_UP).toString(), font, contenido, derecha, 0,1,Rectangle.RECTANGLE));
                        suma+=Double.parseDouble(t_datos.getValueAt(i, 8).toString());
                        pin+=v;
                    }
                    else
                        tabla.addCell(reporte.celda("", font, contenido, derecha, 0,1,Rectangle.RECTANGLE));

                    suma*=ord.getCompania().getImporteHora();
                    suma*=Double.parseDouble(t_datos.getValueAt(i, 9).toString());
                    tabla.addCell(reporte.celda("", font, contenido, derecha, 0,1,Rectangle.RECTANGLE));
                    tabla.addCell(reporte.celda(formatoPorcentaje.format(suma), font, contenido, derecha, 0,1,Rectangle.RECTANGLE));
                    tot+=suma;
                    /*if(ren==38)
                    {
                        reporte.agregaObjeto(tabla);
                        reporte.writer.newPage();
                        
                        tabla=reporte.crearTabla(11, tam, 100, Element.ALIGN_LEFT);
                        cabecera(reporte, bf, tabla);
                        ren=-1;
                    }
                    ren++;*/
                }
            }
            Double tot_horas=dm+cam+min+med+max+pin;
            tabla.addCell(reporte.celda("Costo M.O:$"+formatoPorcentaje.format(ord.getCompania().getImporteHora())+"     Total de Horas:"+formatoDecimal.format(tot_horas), font, contenido, derecha, 4,1,Rectangle.RIGHT));
            tabla.addCell(reporte.celda(""+formatoDecimal.format(dm), font, contenido, derecha, 0,1,Rectangle.RECTANGLE));
            tabla.addCell(reporte.celda(""+formatoDecimal.format(cam), font, contenido, derecha, 0,1,Rectangle.RECTANGLE));
            tabla.addCell(reporte.celda(""+formatoDecimal.format(min), font, contenido, derecha, 0,1,Rectangle.RECTANGLE));
            tabla.addCell(reporte.celda(""+formatoDecimal.format(med), font, contenido, derecha, 0,1,Rectangle.RECTANGLE));
            tabla.addCell(reporte.celda(""+formatoDecimal.format(max), font, contenido, derecha, 0,1,Rectangle.RECTANGLE));
            tabla.addCell(reporte.celda(""+formatoDecimal.format(pin), font, contenido, derecha, 0,1,Rectangle.RECTANGLE));
            tabla.addCell(reporte.celda(formatoPorcentaje.format(tot), font, contenido, derecha, 0,1,Rectangle.RECTANGLE));
            tabla.addCell(reporte.celda(" ", font, contenido, izquierda, 4,1,Rectangle.NO_BORDER));
            tabla.addCell(reporte.celda(" ", font, contenido, izquierda, 7,1,Rectangle.TOP));
            tabla.setHeaderRows(2);
            
            PdfPTable tabla1=reporte.crearTabla(11, tam, 100, Element.ALIGN_LEFT);
            tabla1.addCell(reporte.celda("OBSERVACIONES", font, contenido, izquierda, 11,1,Rectangle.NO_BORDER));
            tabla1.addCell(reporte.celda(" ", font, contenido, izquierda, 11,1,Rectangle.BOTTOM));
            tabla1.addCell(reporte.celda(" ", font, contenido, izquierda, 11,1,Rectangle.BOTTOM));
            tabla1.addCell(reporte.celda(" ", font, contenido, izquierda, 11,1,Rectangle.BOTTOM));
            tabla1.addCell(reporte.celda(" ", font, contenido, izquierda, 10,1,Rectangle.BOTTOM));
            
            reporte.agregaObjeto(tabla);
            reporte.agregaObjeto(tabla1);
            reporte.cerrar();
            reporte.visualizar2("reportes/"+ord.getIdOrden()+"/"+valor+"-valuacion.pdf");
        }catch(Exception e)
        {
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
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        //h=new Herramientas(user, 0);
        //h.session(sessionPrograma);
        Session session = HibernateUtil.getSessionFactory().openSession();
        try
        {
            DecimalFormat formatoPorcentaje = new DecimalFormat("#,##0.00");
            formatoPorcentaje.setMinimumFractionDigits(2);   
            BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.WINANSI, BaseFont.NOT_EMBEDDED);
            PDF reporte = new PDF();
            Date fecha = new Date();
            DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyyHH-mm-ss");//YYYY-MM-DD HH:MM:SS
            String valor=dateFormat.format(fecha);
            File folder = new File("reportes/"+ord.getIdOrden());
            folder.mkdirs();
            reporte.Abrir2(PageSize.LETTER.rotate(), "Valuación", "reportes/"+ord.getIdOrden()+"/"+valor+"-ref_precio.pdf");
            Font font = new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL);
            BaseColor contenido=BaseColor.WHITE;
            int centro=Element.ALIGN_CENTER;
            int izquierda=Element.ALIGN_LEFT;
            int derecha=Element.ALIGN_RIGHT;
            int ren=0;
            double dm=0d, cam=0d, min=0d, med=0d, max=0d, pin=0d, tot=0d;
            session.beginTransaction().begin();
            Orden ord = (Orden)session.get(Orden.class, Integer.parseInt(orden));
            boolean redondeo = ord.getCompania().isRedondeo();
            Configuracion config=(Configuracion)session.get(Configuracion.class, configuracion);
            /*Todas Autorizadas Sin Autorizar*/
            Partida[] cuentas= new Partida[]{};
            
            switch(cb_partidas.getSelectedItem().toString())
            {
                case "Todas":
                    switch(cb_tipo.getSelectedItem().toString())
                    {
                        case "Hojalateria":
                            cuentas= (Partida[]) session.createCriteria(Partida.class).add(Restrictions.eq("ordenByIdOrden.idOrden", Integer.parseInt(orden))).add(Restrictions.eq("espHoj", true)).addOrder(Order.asc("idEvaluacion")).addOrder(Order.asc("subPartida")).list().toArray(new Partida[0]);
                            break;
                        case "Mecanica":
                            cuentas= (Partida[]) session.createCriteria(Partida.class).add(Restrictions.eq("ordenByIdOrden.idOrden", Integer.parseInt(orden))).add(Restrictions.eq("espMec", true)).addOrder(Order.asc("idEvaluacion")).addOrder(Order.asc("subPartida")).list().toArray(new Partida[0]);
                            break;
                        case "Suspension":
                            cuentas= (Partida[]) session.createCriteria(Partida.class).add(Restrictions.eq("ordenByIdOrden.idOrden", Integer.parseInt(orden))).add(Restrictions.eq("espSus", true)).addOrder(Order.asc("idEvaluacion")).addOrder(Order.asc("subPartida")).list().toArray(new Partida[0]);
                            break;
                        case "Electrico":
                            cuentas= (Partida[]) session.createCriteria(Partida.class).add(Restrictions.eq("ordenByIdOrden.idOrden", Integer.parseInt(orden))).add(Restrictions.eq("espEle", true)).addOrder(Order.asc("idEvaluacion")).addOrder(Order.asc("subPartida")).list().toArray(new Partida[0]);
                            break;
                        case "Pintura":
                            cuentas= (Partida[]) session.createCriteria(Partida.class).add(Restrictions.eq("ordenByIdOrden.idOrden", Integer.parseInt(orden))).add(Restrictions.or(Restrictions.or(Restrictions.ne("intPinMin", -1.0d), Restrictions.ne("intPinMed", -1.0d)), Restrictions.ne("intPinMax", -1.0d))).addOrder(Order.asc("idEvaluacion")).addOrder(Order.asc("subPartida")).list().toArray(new Partida[0]);
                            break;
                            
                        case "Todas":
                            cuentas= (Partida[]) session.createCriteria(Partida.class).add(Restrictions.eq("ordenByIdOrden.idOrden", Integer.parseInt(orden))).addOrder(Order.asc("idEvaluacion")).addOrder(Order.asc("subPartida")).list().toArray(new Partida[0]);
                            break;
                    }
                    break;
                    
                case "Autorizadas":
                    cuentas= (Partida[]) session.createCriteria(Partida.class).add(Restrictions.eq("ordenByIdOrden.idOrden", Integer.parseInt(orden))).add(Restrictions.eq("autorizado", true)).addOrder(Order.asc("idEvaluacion")).addOrder(Order.asc("subPartida")).list().toArray(new Partida[0]);
                    switch(cb_tipo.getSelectedItem().toString())
                    {
                        case "Hojalateria":
                            cuentas= (Partida[]) session.createCriteria(Partida.class).add(Restrictions.eq("ordenByIdOrden.idOrden", Integer.parseInt(orden))).add(Restrictions.eq("autorizado", true)).add(Restrictions.eq("espHoj", true)).addOrder(Order.asc("idEvaluacion")).addOrder(Order.asc("subPartida")).list().toArray(new Partida[0]);
                            break;
                        case "Mecanica":
                            cuentas= (Partida[]) session.createCriteria(Partida.class).add(Restrictions.eq("ordenByIdOrden.idOrden", Integer.parseInt(orden))).add(Restrictions.eq("autorizado", true)).add(Restrictions.eq("espMec", true)).addOrder(Order.asc("idEvaluacion")).addOrder(Order.asc("subPartida")).list().toArray(new Partida[0]);
                            break;
                        case "Suspension":
                            cuentas= (Partida[]) session.createCriteria(Partida.class).add(Restrictions.eq("ordenByIdOrden.idOrden", Integer.parseInt(orden))).add(Restrictions.eq("autorizado", true)).add(Restrictions.eq("espSus", true)).addOrder(Order.asc("idEvaluacion")).addOrder(Order.asc("subPartida")).list().toArray(new Partida[0]);
                            break;
                        case "Electrico":
                            cuentas= (Partida[]) session.createCriteria(Partida.class).add(Restrictions.eq("ordenByIdOrden.idOrden", Integer.parseInt(orden))).add(Restrictions.eq("autorizado", true)).add(Restrictions.eq("espEle", true)).addOrder(Order.asc("idEvaluacion")).addOrder(Order.asc("subPartida")).list().toArray(new Partida[0]);
                            break;
                        case "Pintura":
                            cuentas= (Partida[]) session.createCriteria(Partida.class).add(Restrictions.eq("ordenByIdOrden.idOrden", Integer.parseInt(orden))).add(Restrictions.eq("autorizado", true)).add(Restrictions.or(Restrictions.or(Restrictions.ne("intPinMin", -1.0d), Restrictions.ne("intPinMed", -1.0d)), Restrictions.ne("intPinMax", -1.0d))).addOrder(Order.asc("idEvaluacion")).addOrder(Order.asc("subPartida")).list().toArray(new Partida[0]);
                            break;
                            
                        case "Todas":
                            cuentas= (Partida[]) session.createCriteria(Partida.class).add(Restrictions.eq("ordenByIdOrden.idOrden", Integer.parseInt(orden))).add(Restrictions.eq("autorizado", true)).addOrder(Order.asc("idEvaluacion")).addOrder(Order.asc("subPartida")).list().toArray(new Partida[0]);
                            break;
                    }
                    break;
                case "Sin Autorizar":
                    switch(cb_tipo.getSelectedItem().toString())
                    {
                        case "Hojalateria":
                            cuentas= (Partida[]) session.createCriteria(Partida.class).add(Restrictions.eq("ordenByIdOrden.idOrden", Integer.parseInt(orden))).add(Restrictions.eq("autorizado", false)).add(Restrictions.eq("espHoj", true)).addOrder(Order.asc("idEvaluacion")).addOrder(Order.asc("subPartida")).list().toArray(new Partida[0]);
                            break;
                        case "Mecanica":
                            cuentas= (Partida[]) session.createCriteria(Partida.class).add(Restrictions.eq("ordenByIdOrden.idOrden", Integer.parseInt(orden))).add(Restrictions.eq("autorizado", false)).add(Restrictions.eq("espMec", true)).addOrder(Order.asc("idEvaluacion")).addOrder(Order.asc("subPartida")).list().toArray(new Partida[0]);
                            break;
                        case "Suspension":
                            cuentas= (Partida[]) session.createCriteria(Partida.class).add(Restrictions.eq("ordenByIdOrden.idOrden", Integer.parseInt(orden))).add(Restrictions.eq("autorizado", false)).add(Restrictions.eq("espSus", true)).addOrder(Order.asc("idEvaluacion")).addOrder(Order.asc("subPartida")).list().toArray(new Partida[0]);
                            break;
                        case "Electrico":
                            cuentas= (Partida[]) session.createCriteria(Partida.class).add(Restrictions.eq("ordenByIdOrden.idOrden", Integer.parseInt(orden))).add(Restrictions.eq("autorizado", false)).add(Restrictions.eq("espEle", true)).addOrder(Order.asc("idEvaluacion")).addOrder(Order.asc("subPartida")).list().toArray(new Partida[0]);
                            break;
                        case "Pintura":
                            cuentas= (Partida[]) session.createCriteria(Partida.class).add(Restrictions.eq("ordenByIdOrden.idOrden", Integer.parseInt(orden))).add(Restrictions.eq("autorizado", false)).add(Restrictions.or(Restrictions.or(Restrictions.ne("intPinMin", -1.0d), Restrictions.ne("intPinMed", -1.0d)), Restrictions.ne("intPinMax", -1.0d))).addOrder(Order.asc("idEvaluacion")).addOrder(Order.asc("subPartida")).list().toArray(new Partida[0]);
                            break;
                        case "Todas":
                            cuentas= (Partida[]) session.createCriteria(Partida.class).add(Restrictions.eq("ordenByIdOrden.idOrden", Integer.parseInt(orden))).add(Restrictions.eq("autorizado", false)).addOrder(Order.asc("idEvaluacion")).addOrder(Order.asc("subPartida")).list().toArray(new Partida[0]);
                            break;
                    }
                    break;
                    
                case "Marcadas":
                    if(t_datos.getSelectedRows().length>0)
                    {
                        String consulta="from Partida where concat(idEvaluacion, subPartida) in (";
                        int [] selec=t_datos.getSelectedRows();
                        for(int x=0; x<selec.length; x++)
                        {
                            if(x>0)
                                consulta+=", ";
                            consulta+=t_datos.getValueAt(selec[x], 0).toString()+t_datos.getValueAt(selec[x], 1).toString();
                        }
                        switch(cb_tipo.getSelectedItem().toString())
                        {
                            case "Hojalateria":
                                consulta+=") AND(espHoj=true) ";
                                break;
                            case "Mecanica":
                                consulta+=") AND(espMec=true) ";
                                break;
                            case "Suspension":
                                consulta+=") AND(espSus=true) ";
                                break;
                            case "Electrico":
                                consulta+=") AND(espEle=true) ";
                                break;
                            case "Pintura":
                                    consulta+=")) AND(intPinMin>-1 OR intPinMed>-1 OR intPinMax>-1) ";
                                    break;
                            case "Todas":
                                consulta+=") ";
                                break;
                        }
                        consulta+="AND (ordenByIdOrden.idOrden="+orden+") order by idEvaluacion asc, subPartida asc";
                        Query q = session.createQuery(consulta);
                        cuentas=(Partida[]) q.list().toArray(new Partida[0]);
                    }
                    else
                        cuentas= new Partida[0];
                    break;
            }
            /*if(this.cb_precio.getSelectedItem().toString().compareToIgnoreCase("Compra")!=0)
            {*/
                float tam[];
                if(this.cb_precio.getSelectedItem().toString().compareToIgnoreCase("Compra")==0)
                    tam=new float[]{12,12,50,110,15,15,25,25,25,25};
                else
                    tam=new float[]{12,12,50,110,15,15,25,25,35,12};
                
                PdfPTable tabla=reporte.crearTabla(10, tam, 100, Element.ALIGN_LEFT);

                cabecera1(reporte, bf, tabla, config.getCinta());

                for(int i=0; i<cuentas.length; i++)
                {    
                    double suma=0d;
                    tabla.addCell(reporte.celda(""+cuentas[i].getIdEvaluacion(), font, contenido, derecha, 0,1,Rectangle.RECTANGLE));
                    tabla.addCell(reporte.celda(""+cuentas[i].getSubPartida(), font, contenido, derecha, 0,1,Rectangle.RECTANGLE));
                    if(r_parte.isSelected()==true)
                    {
                        if(cuentas[i].getEjemplar()!=null)
                            tabla.addCell(reporte.celda(cuentas[i].getEjemplar().getIdParte(), font, contenido, centro, 0,1,Rectangle.RECTANGLE));
                        else
                            tabla.addCell(reporte.celda("S/N", font, contenido, centro, 0,0,Rectangle.RECTANGLE));
                    }
                    else
                        tabla.addCell(reporte.celda("S/N", font, contenido, centro, 0,0,Rectangle.RECTANGLE));

                    tabla.addCell(reporte.celda(cuentas[i].getCatalogo().getNombre(), font, contenido, izquierda, 0,1,Rectangle.RECTANGLE));
                    tabla.addCell(reporte.celda(""+cuentas[i].getCant(), font, contenido, derecha, 0,1,Rectangle.RECTANGLE));
                    tabla.addCell(reporte.celda(cuentas[i].getMed(), font, contenido, derecha, 0,1,Rectangle.RECTANGLE));
                    Double precio=0.0d;
                    switch(this.cb_precio.getSelectedItem().toString())
                    {
                        case "Seleccionar":
                            tabla.addCell(reporte.celda(formatoPorcentaje.format(0.0d), font, contenido, derecha, 0,1,Rectangle.RECTANGLE));
                            tabla.addCell(reporte.celda(formatoPorcentaje.format(0.0), font, contenido, derecha, 0,1,Rectangle.RECTANGLE));
                            tabla.addCell(reporte.celda("", font, contenido, derecha, 0,1,Rectangle.RECTANGLE));
                            break;

                        case "Cotizado":
                            tabla.addCell(reporte.celda(formatoPorcentaje.format(cuentas[i].getCU()), font, contenido, derecha, 0,1,Rectangle.RECTANGLE));
                            precio=cuentas[i].getCU()*cuentas[i].getCant();
                            tot+=precio;
                            tabla.addCell(reporte.celda(formatoPorcentaje.format(precio), font, contenido, derecha, 0,1,Rectangle.RECTANGLE));
                            tabla.addCell(reporte.celda("", font, contenido, derecha, 0,1,Rectangle.RECTANGLE));
                            break;

                        case "Autorizado":
                            tabla.addCell(reporte.celda(formatoPorcentaje.format(cuentas[i].getPrecioAutCU()), font, contenido, derecha, 0,1,Rectangle.RECTANGLE));
                            precio=cuentas[i].getPrecioAutCU()*cuentas[i].getCant();
                            tot+=precio;
                            tabla.addCell(reporte.celda(formatoPorcentaje.format(precio), font, contenido, derecha, 0,1,Rectangle.RECTANGLE));
                            tabla.addCell(reporte.celda("", font, contenido, derecha, 0,1,Rectangle.RECTANGLE));
                            break;

                        case "Compañia":
                            Double costo=0.0d;
                            if(redondeo==false)
                                costo=0.0+(cuentas[i].getCU()/(1-(cuentas[i].getPorcentaje()*0.01)));
                             else
                                costo=0.0+Math.round(cuentas[i].getCU()/(1-(cuentas[i].getPorcentaje()*0.01)));
                            
                            tabla.addCell(reporte.celda(formatoPorcentaje.format(new BigDecimal(costo).setScale(2, RoundingMode.HALF_UP).doubleValue()), font, contenido, derecha, 0,1,Rectangle.RECTANGLE));
                            precio=costo*cuentas[i].getCant();
                            tot+=precio;
                            tabla.addCell(reporte.celda(formatoPorcentaje.format(precio), font, contenido, derecha, 0,1,Rectangle.RECTANGLE));
                            tabla.addCell(reporte.celda("", font, contenido, derecha, 0,1,Rectangle.RECTANGLE));
                            break;

                        case "Compra"://costo de cotizacion
                            tabla.addCell(reporte.celda(formatoPorcentaje.format(cuentas[i].getCU()), font, contenido, derecha, 0,1,Rectangle.RECTANGLE));
                            //costo de compra si ya fue comprado
                            double pcom=0d;
                            if(cuentas[i].getPedido()!=null)
                            {
                                pcom=cuentas[i].getPcp();
                                tabla.addCell(reporte.celda(formatoPorcentaje.format(pcom), font, contenido, derecha, 0,1,Rectangle.RECTANGLE));
                                precio=pcom*cuentas[i].getCantPcp();
                                tot+=precio;
                            }
                            else
                                tabla.addCell(reporte.celda("0.00", font, contenido, derecha, 0,1,Rectangle.RECTANGLE));
                            //cosoto autorizado
                            tabla.addCell(reporte.celda(formatoPorcentaje.format(cuentas[i].getPrecioAutCU()), font, contenido, derecha, 0,1,Rectangle.RECTANGLE));
                            //Utlidad
                            double util=cuentas[i].getPrecioAutCU()-pcom;
                            tabla.addCell(reporte.celda(formatoPorcentaje.format(util), font, contenido, derecha, 0,1,Rectangle.RECTANGLE));
                            break;
                    }
                    if(this.cb_precio.getSelectedItem().toString().compareToIgnoreCase("Compra")!=0){
                        if(cuentas[i].isOri()==true)
                            tabla.addCell(reporte.celda("Ori", font, contenido, centro, 0,1,Rectangle.RECTANGLE));
                        else{
                            if(cuentas[i].isNal()==true)
                                tabla.addCell(reporte.celda("Nal", font, contenido, centro, 0,1,Rectangle.RECTANGLE));
                            else{
                                if(cuentas[i].isDesm()==true)
                                    tabla.addCell(reporte.celda("Des", font, contenido, centro, 0,1,Rectangle.RECTANGLE));
                                else{
                                    if(cuentas[i].isPd()==true)
                                        tabla.addCell(reporte.celda("Recon", font, contenido, centro, 0,1,Rectangle.RECTANGLE));
                                    else
                                        tabla.addCell(reporte.celda(" ", font, contenido, centro, 0,1,Rectangle.RECTANGLE));
                                }
                            }
                        }
                    }
                }
                if(this.cb_precio.getSelectedItem().toString().compareToIgnoreCase("Compra")!=0)
                {
                    tabla.addCell(reporte.celda(" ", font, contenido, derecha, 7,1,Rectangle.TOP));
                    tabla.addCell(reporte.celda(formatoPorcentaje.format(tot), font, contenido, derecha, 0,1,Rectangle.RECTANGLE));
                    tabla.addCell(reporte.celda(" ", font, contenido, derecha, 2,1,Rectangle.TOP));
                }
                else
                {
                    tabla.addCell(reporte.celda(" ", font, contenido, derecha, 7,1,Rectangle.TOP));
                    tabla.addCell(reporte.celda(formatoPorcentaje.format(tot), font, contenido, derecha, 0,1,Rectangle.RECTANGLE));
                    tabla.addCell(reporte.celda(" ", font, contenido, derecha, 2,1,Rectangle.TOP));
                }
                tabla.setHeaderRows(1);
                reporte.agregaObjeto(tabla);
            //}
            reporte.cerrar();
            reporte.visualizar2("reportes/"+ord.getIdOrden()+"/"+valor+"-ref_precio.pdf");
        }catch(Exception e)
        {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "No se pudo realizar el reporte");
        }
        if(session!=null)
            if(session.isOpen())
            {
                session.flush();
                session.clear();
                session.close();
            }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void cb_precioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cb_precioActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cb_precioActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
                h=new Herramientas(user, 0);
        h.session(sessionPrograma);
        Session session = HibernateUtil.getSessionFactory().openSession();
        try
        {
            //NumberFormat formatoPorcentaje = NumberFormat.getCurrencyInstance();
            DecimalFormat formatoPorcentaje = new DecimalFormat("#,###.00");
            formatoPorcentaje.setMinimumFractionDigits(2);
            
            session.beginTransaction().begin();
            BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.WINANSI, BaseFont.NOT_EMBEDDED);
            //Orden ord=buscaApertura();
            PDF reporte = new PDF();
            Date fecha = new Date();
            DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyyHH-mm-ss");//YYYY-MM-DD HH:MM:SS
            String valor=dateFormat.format(fecha);
            File folder = new File("reportes/"+ord.getIdOrden());
            folder.mkdirs();
            reporte.Abrir2(PageSize.LETTER.rotate(), "Valuación", "reportes/"+ord.getIdOrden()+"/"+valor+"-ref_sin_precio.pdf");
            Font font = new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL);
            BaseColor contenido=BaseColor.WHITE;
            int centro=Element.ALIGN_CENTER;
            int izquierda=Element.ALIGN_LEFT;
            int derecha=Element.ALIGN_RIGHT;
            int ren=0;
            double dm=0d, cam=0d, min=0d, med=0d, max=0d, pin=0d, tot=0d;
            session.beginTransaction().begin();
            Orden ord = (Orden)session.get(Orden.class, Integer.parseInt(orden));
            Configuracion config=(Configuracion)session.get(Configuracion.class, configuracion);
            /*Todas Autorizadas Sin Autorizar*/
            Partida[] cuentas= new Partida[]{};
            
            switch(cb_partidas.getSelectedItem().toString())
            {
                case "Todas":
                    switch(cb_tipo.getSelectedItem().toString())
                    {
                        case "Hojalateria":
                            cuentas= (Partida[]) session.createCriteria(Partida.class).add(Restrictions.eq("ordenByIdOrden.idOrden", Integer.parseInt(orden))).add(Restrictions.eq("espHoj", true)).add(Restrictions.eq("refCoti", true)).addOrder(Order.asc("idEvaluacion")).addOrder(Order.asc("subPartida")).list().toArray(new Partida[0]);
                            break;
                        case "Mecanica":
                            cuentas= (Partida[]) session.createCriteria(Partida.class).add(Restrictions.eq("ordenByIdOrden.idOrden", Integer.parseInt(orden))).add(Restrictions.eq("espMec", true)).add(Restrictions.eq("refCoti", true)).addOrder(Order.asc("idEvaluacion")).addOrder(Order.asc("subPartida")).list().toArray(new Partida[0]);
                            break;
                        case "Suspension":
                            cuentas= (Partida[]) session.createCriteria(Partida.class).add(Restrictions.eq("ordenByIdOrden.idOrden", Integer.parseInt(orden))).add(Restrictions.eq("espSus", true)).add(Restrictions.eq("refCoti", true)).addOrder(Order.asc("idEvaluacion")).addOrder(Order.asc("subPartida")).list().toArray(new Partida[0]);
                            break;
                        case "Electrico":
                            cuentas= (Partida[]) session.createCriteria(Partida.class).add(Restrictions.eq("ordenByIdOrden.idOrden", Integer.parseInt(orden))).add(Restrictions.eq("espEle", true)).add(Restrictions.eq("refCoti", true)).addOrder(Order.asc("idEvaluacion")).addOrder(Order.asc("subPartida")).list().toArray(new Partida[0]);
                            break;
                        case "Pintura":
                            cuentas= (Partida[]) session.createCriteria(Partida.class).add(Restrictions.eq("ordenByIdOrden.idOrden", Integer.parseInt(orden))).add(Restrictions.eq("refCoti", true)).add(Restrictions.or(Restrictions.or(Restrictions.ne("intPinMin", -1.0d), Restrictions.ne("intPinMed", -1.0d)), Restrictions.ne("intPinMax", -1.0d))).addOrder(Order.asc("idEvaluacion")).addOrder(Order.asc("subPartida")).list().toArray(new Partida[0]);
                            break;
                        case "Todas":
                            cuentas= (Partida[]) session.createCriteria(Partida.class).add(Restrictions.eq("ordenByIdOrden.idOrden", Integer.parseInt(orden))).add(Restrictions.eq("refCoti", true)).addOrder(Order.asc("idEvaluacion")).addOrder(Order.asc("subPartida")).list().toArray(new Partida[0]);
                            break;
                    }
                    break;
                    
                case "Autorizadas":
                    switch(cb_tipo.getSelectedItem().toString())
                    {
                        case "Hojalateria":
                            cuentas= (Partida[]) session.createCriteria(Partida.class).add(Restrictions.eq("ordenByIdOrden.idOrden", Integer.parseInt(orden))).add(Restrictions.eq("espHoj", true)).add(Restrictions.eq("refCoti", true)).add(Restrictions.eq("autorizado", true)).addOrder(Order.asc("idEvaluacion")).addOrder(Order.asc("subPartida")).list().toArray(new Partida[0]);
                            break;
                        case "Mecanica":
                            cuentas= (Partida[]) session.createCriteria(Partida.class).add(Restrictions.eq("ordenByIdOrden.idOrden", Integer.parseInt(orden))).add(Restrictions.eq("espMec", true)).add(Restrictions.eq("refCoti", true)).add(Restrictions.eq("autorizado", true)).addOrder(Order.asc("idEvaluacion")).addOrder(Order.asc("subPartida")).list().toArray(new Partida[0]);
                            break;
                        case "Suspension":
                            cuentas= (Partida[]) session.createCriteria(Partida.class).add(Restrictions.eq("ordenByIdOrden.idOrden", Integer.parseInt(orden))).add(Restrictions.eq("espSus", true)).add(Restrictions.eq("refCoti", true)).add(Restrictions.eq("autorizado", true)).addOrder(Order.asc("idEvaluacion")).addOrder(Order.asc("subPartida")).list().toArray(new Partida[0]);
                            break;
                        case "Electrico":
                            cuentas= (Partida[]) session.createCriteria(Partida.class).add(Restrictions.eq("ordenByIdOrden.idOrden", Integer.parseInt(orden))).add(Restrictions.eq("espEle", true)).add(Restrictions.eq("refCoti", true)).add(Restrictions.eq("autorizado", true)).addOrder(Order.asc("idEvaluacion")).addOrder(Order.asc("subPartida")).list().toArray(new Partida[0]);
                            break;
                        case "Pintura":
                            cuentas= (Partida[]) session.createCriteria(Partida.class).add(Restrictions.eq("ordenByIdOrden.idOrden", Integer.parseInt(orden))).add(Restrictions.eq("refCoti", true)).add(Restrictions.eq("autorizado", true)).add(Restrictions.or(Restrictions.or(Restrictions.ne("intPinMin", -1.0d), Restrictions.ne("intPinMed", -1.0d)), Restrictions.ne("intPinMax", -1.0d))).addOrder(Order.asc("idEvaluacion")).addOrder(Order.asc("subPartida")).list().toArray(new Partida[0]);
                            break;    
                        case "Todas":
                            cuentas= (Partida[]) session.createCriteria(Partida.class).add(Restrictions.eq("ordenByIdOrden.idOrden", Integer.parseInt(orden))).add(Restrictions.eq("refCoti", true)).addOrder(Order.asc("idEvaluacion")).add(Restrictions.eq("autorizado", true)).addOrder(Order.asc("subPartida")).list().toArray(new Partida[0]);
                            break;
                    }
                    break;
                    
                case "Sin Autorizar":
                    switch(cb_tipo.getSelectedItem().toString())
                    {
                        case "Hojalateria":
                            cuentas= (Partida[]) session.createCriteria(Partida.class).add(Restrictions.eq("ordenByIdOrden.idOrden", Integer.parseInt(orden))).add(Restrictions.eq("espHoj", true)).add(Restrictions.eq("refCoti", true)).add(Restrictions.eq("autorizado", false)).addOrder(Order.asc("idEvaluacion")).addOrder(Order.asc("subPartida")).list().toArray(new Partida[0]);
                            break;
                        case "Mecanica":
                            cuentas= (Partida[]) session.createCriteria(Partida.class).add(Restrictions.eq("ordenByIdOrden.idOrden", Integer.parseInt(orden))).add(Restrictions.eq("espMec", true)).add(Restrictions.eq("refCoti", true)).add(Restrictions.eq("autorizado", false)).addOrder(Order.asc("idEvaluacion")).addOrder(Order.asc("subPartida")).list().toArray(new Partida[0]);
                            break;
                        case "Suspension":
                            cuentas= (Partida[]) session.createCriteria(Partida.class).add(Restrictions.eq("ordenByIdOrden.idOrden", Integer.parseInt(orden))).add(Restrictions.eq("espSus", true)).add(Restrictions.eq("refCoti", true)).add(Restrictions.eq("autorizado", false)).addOrder(Order.asc("idEvaluacion")).addOrder(Order.asc("subPartida")).list().toArray(new Partida[0]);
                            break;
                        case "Electrico":
                            cuentas= (Partida[]) session.createCriteria(Partida.class).add(Restrictions.eq("ordenByIdOrden.idOrden", Integer.parseInt(orden))).add(Restrictions.eq("espEle", true)).add(Restrictions.eq("refCoti", true)).add(Restrictions.eq("autorizado", false)).addOrder(Order.asc("idEvaluacion")).addOrder(Order.asc("subPartida")).list().toArray(new Partida[0]);
                            break;
                        case "Pintura":
                            cuentas= (Partida[]) session.createCriteria(Partida.class).add(Restrictions.eq("ordenByIdOrden.idOrden", Integer.parseInt(orden))).add(Restrictions.eq("espEle", true)).add(Restrictions.eq("refCoti", true)).add(Restrictions.eq("autorizado", false)).add(Restrictions.or(Restrictions.or(Restrictions.ne("intPinMin", -1.0d), Restrictions.ne("intPinMed", -1.0d)), Restrictions.ne("intPinMax", -1.0d))).addOrder(Order.asc("idEvaluacion")).addOrder(Order.asc("subPartida")).list().toArray(new Partida[0]);
                            break;    
                        case "Todas":
                            cuentas= (Partida[]) session.createCriteria(Partida.class).add(Restrictions.eq("ordenByIdOrden.idOrden", Integer.parseInt(orden))).add(Restrictions.eq("refCoti", true)).addOrder(Order.asc("idEvaluacion")).add(Restrictions.eq("autorizado", false)).addOrder(Order.asc("subPartida")).list().toArray(new Partida[0]);
                            break;
                    }
                    break;
                    
                case "Marcadas":
                    if(t_datos.getSelectedRows().length>0)
                    {
                        //cuentas= (Partida[]) session.createCriteria(Partida.class).add(Restrictions.eq("ordenByIdOrden.idOrden", Integer.parseInt(orden))).add(Restrictions.and(Restrictions.in("subPartida", seleccion(1)), Restrictions.in("idEvaluacion", seleccion(0)))).addOrder(Order.asc("idEvaluacion")).addOrder(Order.asc("subPartida")).list().toArray(new Partida[0]);
                        String consulta="from Partida where concat(idEvaluacion, subPartida) in (";
                        int [] selec=t_datos.getSelectedRows();
                        for(int x=0; x<selec.length; x++)
                        {
                            if(x>0)
                                consulta+=", ";
                            consulta+=t_datos.getValueAt(selec[x], 0).toString()+t_datos.getValueAt(selec[x], 1).toString();
                        }
                        switch(cb_tipo.getSelectedItem().toString())
                        {
                            case "Hojalateria":
                                consulta+=") AND(espHoj=true) ";
                                break;
                            case "Mecanica":
                                consulta+=") AND(espMec=true) ";
                                break;
                            case "Suspension":
                                consulta+=") AND(espSus=true) ";
                                break;
                            case "Electrico":
                                consulta+=") AND(espEle=true) ";
                                break;
                            case "Pintura":
                                    consulta+=")) AND(intPinMin>-1 OR intPinMed>-1 OR intPinMax>-1) ";
                                    break;
                            case "Todas":
                                consulta+=") ";
                                break;
                        }
                        consulta+="AND (ordenByIdOrden.idOrden="+orden+") order by idEvaluacion asc, subPartida asc";
                        Query q = session.createQuery(consulta);
                        cuentas=(Partida[]) q.list().toArray(new Partida[0]);
                    }
                    else
                        cuentas= new Partida[0];
                    break;
            }
            float tam[]=new float[]{12,12,50,110,15,15,25,25,35,12};
            PdfPTable tabla=reporte.crearTabla(10, tam, 100, Element.ALIGN_LEFT);

            cabecera1(reporte, bf, tabla, config.getCinta());

            for(int i=0; i<cuentas.length; i++)
            {    
                        double suma=0d;
                        tabla.addCell(reporte.celda(""+cuentas[i].getIdEvaluacion(), font, contenido, derecha, 0,1,Rectangle.RECTANGLE));
                        tabla.addCell(reporte.celda(""+cuentas[i].getSubPartida(), font, contenido, derecha, 0,1,Rectangle.RECTANGLE));
                        if(r_parte.isSelected()==true)
                        {
                            if(cuentas[i].getEjemplar()!=null)
                                tabla.addCell(reporte.celda(cuentas[i].getEjemplar().getIdParte(), font, contenido, centro, 0,1,Rectangle.RECTANGLE));
                            else
                                tabla.addCell(reporte.celda("S/N", font, contenido, centro, 0,0,Rectangle.RECTANGLE));
                        }
                        else
                            tabla.addCell(reporte.celda("S/N", font, contenido, centro, 0,0,Rectangle.RECTANGLE));

                        tabla.addCell(reporte.celda(cuentas[i].getCatalogo().getNombre(), font, contenido, izquierda, 0,1,Rectangle.RECTANGLE));
                        tabla.addCell(reporte.celda(""+cuentas[i].getCant(), font, contenido, derecha, 0,1,Rectangle.RECTANGLE));
                        tabla.addCell(reporte.celda(cuentas[i].getMed(), font, contenido, derecha, 0,1,Rectangle.RECTANGLE));
                        tabla.addCell(reporte.celda("", font, contenido, derecha, 0,1,Rectangle.RECTANGLE));
                        
                        tabla.addCell(reporte.celda("", font, contenido, derecha, 0,1,Rectangle.RECTANGLE));
                        tabla.addCell(reporte.celda("", font, contenido, derecha, 0,1,Rectangle.RECTANGLE));
                        if(cuentas[i].isOri()==true)
                        {
                            tabla.addCell(reporte.celda("Ori", font, contenido, centro, 0,1,Rectangle.RECTANGLE));
                        }
                        else
                        {
                            if(cuentas[i].isNal()==true)
                            {
                                tabla.addCell(reporte.celda("Nal", font, contenido, centro, 0,1,Rectangle.RECTANGLE));
                            }
                            else
                            {
                                if(cuentas[i].isDesm()==true)
                                    tabla.addCell(reporte.celda("Des", font, contenido, centro, 0,1,Rectangle.RECTANGLE));
                                else
                                {
                                    if(cuentas[i].isPd()==true)
                                        tabla.addCell(reporte.celda("Rec", font, contenido, centro, 0,1,Rectangle.RECTANGLE));
                                    else
                                        tabla.addCell(reporte.celda(" ", font, contenido, centro, 0,1,Rectangle.RECTANGLE));
                                }
                            }
                        }

                        /*if(ren==37)
                        {
                            reporte.agregaObjeto(tabla);
                            reporte.writer.newPage();
                            tabla=reporte.crearTabla(10, tam, 100, Element.ALIGN_LEFT);
                            cabecera1(reporte, bf, tabla);
                            ren=-1;
                        }
                        ren++;*/
            }
            tabla.setHeaderRows(1);
            reporte.agregaObjeto(tabla);
            
            reporte.cerrar();
            reporte.visualizar2("reportes/"+ord.getIdOrden()+"/"+valor+"-ref_sin_precio.pdf");
        }catch(Exception e)
        {
            e.printStackTrace();
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
    }//GEN-LAST:event_jButton3ActionPerformed

    private void t_buscaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_t_buscaActionPerformed
        // TODO add your handling code here:
        h=new Herramientas(user, 0);
        h.session(sessionPrograma);
        if(this.t_busca.getText().compareToIgnoreCase("")!=0)
        {
            //buscaCuentas();
            if(x>=t_datos.getRowCount())
            {
                x=0;
                java.awt.Rectangle r = t_datos.getCellRect( x, 2, true);
                t_datos.scrollRectToVisible(r);
            }
            for(; x<t_datos.getRowCount(); x++)
            {
                if(t_datos.getValueAt(x, 2).toString().indexOf(t_busca.getText()) != -1)
                {
                    t_datos.setRowSelectionInterval(x, x);
                    t_datos.setColumnSelectionInterval(2, 2);
                    java.awt.Rectangle r = t_datos.getCellRect( x, 2, true);
                    t_datos.scrollRectToVisible(r);
                    break;
                }
            }
            x++;
        }
    }//GEN-LAST:event_t_buscaActionPerformed

    private void t_buscaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_buscaKeyTyped
        // TODO add your handling code here:
        char car = evt.getKeyChar();
        evt.setKeyChar(Character.toUpperCase(evt.getKeyChar()));
    }//GEN-LAST:event_t_buscaKeyTyped

    private void b_acActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_acActionPerformed
        // TODO add your handling code here:
        h= new Herramientas(this.user, menu);
        Session session = HibernateUtil.getSessionFactory().openSession();
        try
        {
            session.beginTransaction().begin();
            ord = (Orden)session.get(Orden.class, Integer.parseInt(this.orden));

            if(t_deducible.getText().compareTo("")!=0)
                ord.setDeducible(((Number)t_deducible.getValue()).doubleValue());
            else
                ord.setDeducible(null);
            
            if(t_demerito.getText().compareTo("")!=0)
                ord.setDemerito(((Number)t_demerito.getValue()).doubleValue());
            else
                ord.setDemerito(null);
            
            session.update(ord);
            session.beginTransaction().commit();
            
            try{
                if(ord.getIdSm()!=null)
                {
                    PeticionPost service=new PeticionPost("http://tbstoluca.ddns.net/sm-l/service/api.php");
                    service.add("METODO", "REPARACION.GUARDA_MONTOS");
                    service.add("ID_REPARACION", ord.getIdSm());
                    service.add("DEDUCIBLE", ""+((Number)t_deducible.getValue()).doubleValue());
                    service.add("DEMERITO", ""+((Number)t_demerito.getValue()).doubleValue());
                    String resp=service.getRespueta();
                    System.out.println(resp);
                    JSONObject respuesta = new JSONObject(resp);
                    if(respuesta.getInt("ESTADO")==1)
                        JOptionPane.showMessageDialog(this, "LOS MONTOS FUERON ACTUALIZADOS Y SE NOTIFICO AL CLIENTE");
                    else
                        JOptionPane.showMessageDialog(this, respuesta.getString("MENSAJE"));
                }
                else
                    JOptionPane.showMessageDialog(this, "LOS MONTOS FUERON ACTUALIZADOS");
            }catch(Exception e){
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "LOS MONTOS FUERON ACTUALIZADOS");
            }
            
        }
        catch(Exception e)
        {
            session.beginTransaction().rollback();
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "No se pudo realizar la actualizacion.");
        }
        if(session!=null)
            if(session.isOpen())
            {
                session.flush();
                session.clear();
                session.close();
            }
    }//GEN-LAST:event_b_acActionPerformed

    private void t_okKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_okKeyTyped
        // TODO add your handling code here:
        char car = evt.getKeyChar();
        if(t_ok.getText().length()>=3)
            evt.consume();
        if((car<'0' || car>'9'))
            evt.consume();
    }//GEN-LAST:event_t_okKeyTyped

    private void t_okKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_okKeyReleased
        // TODO add your handling code here:
        if(t_ok.getText().compareTo("")!=0)
        {
            if(Integer.parseInt(t_ok.getText())>100 || Integer.parseInt(t_ok.getText())<0)
                t_ok.setText("0");
        }
    }//GEN-LAST:event_t_okKeyReleased

    private void b_okActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_okActionPerformed
        // TODO add your handling code here:
        if(t_ok.getText().compareTo("")!=0)
        {
            Session session = HibernateUtil.getSessionFactory().openSession();
            try
            {
                session.beginTransaction().begin();
                ord=(Orden)session.get(Orden.class, ord.getIdOrden());
                Partida[] cuentas = (Partida[])session.createCriteria(Partida.class).add(Restrictions.eq("ordenByIdOrden.idOrden", ord.getIdOrden())).add(Restrictions.eq("refCoti", true)).add(Restrictions.eq("autorizadoValuacion", true)).list().toArray(new Partida[0]);
                for(int g=0; g<cuentas.length; g++)
                {
                    cuentas[g].setPorcentaje(Integer.parseInt(this.t_ok.getText()));
                    cuentas[g].setPrecioAutCU(0.0+Math.round(cuentas[g].getCU()/(1-(cuentas[g].getPorcentaje()*0.01))));
                    //cuentas[g].setPrecioFactura(cuentas[g].getPrecioAutCU());
                    session.update(cuentas[g]);
                }
                session.beginTransaction().commit();
                this.buscaCuentas(-1, -1);
                JOptionPane.showMessageDialog(this, "¡Se actualizaron todos los porcentajes!");
            }
            catch(Exception e)
            {
                session.beginTransaction().rollback();
                JOptionPane.showMessageDialog(this, "¡Error al actualizar los porcentajes!");
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
            JOptionPane.showMessageDialog(this, "¡Debes ingresar el porcentaje!");
    }//GEN-LAST:event_b_okActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        //h=new Herramientas(user, 0);
        //h.session(sessionPrograma);
        Session session = HibernateUtil.getSessionFactory().openSession();
        try{
            boolean entro=false;
            session.beginTransaction().begin();
            if(r_cerrar_valuacion.isSelected()==false)
            {
                entro=true;
                Orden ord=(Orden)session.get(Orden.class, Integer.parseInt(orden));
                user=(Usuario)session.get(Usuario.class,user.getIdUsuario());
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
                ord.setRValuacionCierre(calendario3.getTime());
                ord.setUsuarioByRValuacionCierreAsigno(user);
                Criteria cr = session.createCriteria(Partida.class).
                        add(Restrictions.eq("ordenByIdOrden.idOrden", ord.getIdOrden())).
                        add(Restrictions.in("tipo", new Object[]{"c","a"}));
                Partida[] renglones = (Partida[])cr.list().toArray(new Partida[0]);
                for(int e=0; e<renglones.length; e++)
                {
                    renglones[e].setTipo("o");
                    session.update(renglones[e]);
                }
                session.update(ord);
                r_cerrar_valuacion.setSelected(true);
            }
            
            DecimalFormat formatoPorcentaje = new DecimalFormat("#,##0.00");
            DecimalFormat formatoDecimal = new DecimalFormat("####0.0");
            formatoPorcentaje.setMinimumFractionDigits(2);
            BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.WINANSI, BaseFont.NOT_EMBEDDED);
            //Orden ord=buscaApertura();
            PDF reporte = new PDF();
            Date fecha = new Date();
            DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyyHH-mm-ss");//YYYY-MM-DD HH:MM:SS
            String valor=dateFormat.format(fecha);
            File folder = new File("reportes/"+ord.getIdOrden());
            folder.mkdirs();
            if(r_cerrar_valuacion.isSelected()==false)
                reporte.Abrir2(PageSize.LETTER.rotate(), "Operaciones-No-Autorizado", "reportes/"+ord.getIdOrden()+"/"+valor+"-operaciones.pdf");
            else
                reporte.Abrir2(PageSize.LETTER.rotate(), "Operaciones-Autorizado", "reportes/"+ord.getIdOrden()+"/"+valor+"-operaciones.pdf");
            Font font = new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL);
            BaseColor contenido=BaseColor.WHITE;
            int centro=Element.ALIGN_CENTER;
            int izquierda=Element.ALIGN_LEFT;
            int derecha=Element.ALIGN_RIGHT;
            float tam[];
            PdfPTable tabla;
            if(cb_precio.getSelectedItem().toString().compareTo("Seleccionar")!=0)
            {
                tam=new float[]{15,12,10,10,10,10,15,15,15,10,10,10,10,10,10,10,10,130,75,10,25};
                tabla=reporte.crearTabla(21, tam, 100, Element.ALIGN_LEFT);
            }
            else
            {
                tam=new float[]{15,12,10,10,10,10,15,15,15,10,10,10,10,10,10,10,10,140,90,10};
                tabla=reporte.crearTabla(20, tam, 100, Element.ALIGN_LEFT);
            }
            
            
            cabecera2(reporte, bf, tabla, " Cambios ");
            Partida[] cuentas=new Partida[0];;
            Criteria cr;
            switch(cb_partidas.getSelectedItem().toString())
            {
                case "Marcadas":
                    if(t_datos.getSelectedRows().length>0)
                    {   
                        String consulta="from Partida where (concat(idEvaluacion, subPartida) in (";
                            int [] selec=t_datos.getSelectedRows();
                            for(int x=0; x<selec.length; x++)
                            {
                                if(x>0)
                                    consulta+=", ";
                                consulta+=t_datos.getValueAt(selec[x], 0).toString()+t_datos.getValueAt(selec[x], 1).toString();
                            }
                            switch(cb_tipo.getSelectedItem().toString())
                            {
                                case "Hojalateria":
                                    consulta+=")) AND(espHoj=true) ";
                                    break;
                                case "Mecanica":
                                    consulta+=")) AND(espMec=true) ";
                                    break;
                                case "Suspension":
                                    consulta+=")) AND(espSus=true) ";
                                    break;
                                case "Electrico":
                                    consulta+=")) AND(espEle=true) ";
                                    break;
                                case "Pintura":
                                    consulta+=")) AND(intPinMin>-1 OR intPinMed>-1 OR intPinMax>-1) ";
                                    break;
                                case "Todas":
                                    consulta+=")) ";
                                    break;
                            }
                            consulta+="AND (ordenByIdOrden.idOrden="+orden+") AND (autorizadoValuacion=true)  AND (intCamb>-1)  order by idEvaluacion asc, subPartida asc";
                            Query q = session.createQuery(consulta);
                            cuentas=(Partida[]) q.list().toArray(new Partida[0]);
                    }
                    else
                        cuentas= new Partida[0];
                    break;
                case "Sin Autorizar":
                    cr=session.createCriteria(Partida.class).
                            add(Restrictions.eq("ordenByIdOrden.idOrden", ord.getIdOrden())).
                            add(Restrictions.eq("autorizadoValuacion", true)).
                            add(Restrictions.eq("autorizado", false)).
                            add(Restrictions.ne("intCamb", -1.0d));
                    switch(cb_tipo.getSelectedItem().toString())
                    {
                        case "Hojalateria":
                            cr.add(Restrictions.eq("espHoj", true));
                            break;
                        case "Mecanica":
                            cr.add(Restrictions.eq("espMec", true));
                            break;
                        case "Suspension":
                            cr.add(Restrictions.eq("espSus", true));
                            break;
                        case "Electrico":
                            cr.add(Restrictions.eq("espEle", true));
                            break;
                        case "Pintura":
                            cr.add(Restrictions.or(Restrictions.or(Restrictions.ne("intPinMin", -1.0d), Restrictions.ne("intPinMed", -1.0d)), Restrictions.ne("intPinMax", -1.0d)));
                            break;
                    }
                    cr.addOrder(Order.asc("idEvaluacion"));
                    cr.addOrder(Order.asc("subPartida"));
                    cuentas = (Partida[])cr.list().toArray(new Partida[0]);;
                    break;
                default:
                    cr=session.createCriteria(Partida.class).
                            add(Restrictions.eq("ordenByIdOrden.idOrden", ord.getIdOrden())).
                            add(Restrictions.eq("autorizadoValuacion", true)).
                            add(Restrictions.ne("intCamb", -1.0d));
                    switch(cb_tipo.getSelectedItem().toString())
                    {
                        case "Hojalateria":
                            cr.add(Restrictions.eq("espHoj", true));
                            
                            break;
                        case "Mecanica":
                            cr.add(Restrictions.eq("espMec", true));
                            break;
                        case "Suspension":
                            cr.add(Restrictions.eq("espSus", true));
                            break;
                        case "Electrico":
                            cr.add(Restrictions.eq("espEle", true));
                            break;
                        case "Pintura":
                            cr.add(Restrictions.or(Restrictions.or(Restrictions.ne("intPinMin", -1.0d), Restrictions.ne("intPinMed", -1.0d)), Restrictions.ne("intPinMax", -1.0d)));
                            break;
                    }
                    cr.addOrder(Order.asc("idEvaluacion"));
                    cr.addOrder(Order.asc("subPartida"));
                    cuentas = (Partida[])cr.list().toArray(new Partida[0]);;
                    break;
            }
            int ren=0;
            Double tot=0.0d;
            for(int i=0; i<cuentas.length; i++)
            {   
                double suma=0d;
                tabla.addCell(reporte.celda(""+cuentas[i].getIdEvaluacion(), font, contenido, izquierda, 0,1,Rectangle.RECTANGLE));
                tabla.addCell(reporte.celda(""+cuentas[i].getSubPartida(), font, contenido, izquierda, 0,1,Rectangle.RECTANGLE));
                tabla.addCell(reporte.celda((cuentas[i].isEspHoj()==true ? "x" : ""), font, contenido, izquierda, 0,0,Rectangle.RECTANGLE));
                tabla.addCell(reporte.celda((cuentas[i].isEspMec()==true ? "x" : ""), font, contenido, izquierda, 0,0,Rectangle.RECTANGLE));
                tabla.addCell(reporte.celda((cuentas[i].isEspSus()==true ? "x" : ""), font, contenido, izquierda, 0,0,Rectangle.RECTANGLE));
                tabla.addCell(reporte.celda((cuentas[i].isEspEle()==true ? "x" : ""), font, contenido, izquierda, 0,0,Rectangle.RECTANGLE));
                tabla.addCell(reporte.celda(""+cuentas[i].getCant(), font, contenido, izquierda, 0,1,Rectangle.RECTANGLE));
                tabla.addCell(reporte.celda(""+cuentas[i].getCantidadAut(), font, contenido, izquierda, 0,1,Rectangle.RECTANGLE));
                tabla.addCell(reporte.celda(cuentas[i].getMed(), font, contenido, izquierda, 0,1,Rectangle.RECTANGLE));
                tabla.addCell(reporte.celda((cuentas[i].getDm()!=-1 ? "x" : ""), font, contenido, izquierda, 0,0,Rectangle.RECTANGLE));
                tabla.addCell(reporte.celda((cuentas[i].getIntCamb()!=-1 ? "x" : ""), font, contenido, izquierda, 0,0,Rectangle.RECTANGLE));
                tabla.addCell(reporte.celda((cuentas[i].getIntRepMin()!=-1 ? "x" : ""), font, contenido, izquierda, 0,0,Rectangle.RECTANGLE));
                tabla.addCell(reporte.celda((cuentas[i].getIntRepMed()!=-1 ? "x" : ""), font, contenido, izquierda, 0,0,Rectangle.RECTANGLE));
                tabla.addCell(reporte.celda((cuentas[i].getIntRepMax()!=-1 ? "x" : ""), font, contenido, izquierda, 0,0,Rectangle.RECTANGLE));
                tabla.addCell(reporte.celda((cuentas[i].getIntPinMin()!=-1 ? "x" : ""), font, contenido, izquierda, 0,0,Rectangle.RECTANGLE));
                tabla.addCell(reporte.celda((cuentas[i].getIntPinMed()!=-1 ? "x" : ""), font, contenido, izquierda, 0,0,Rectangle.RECTANGLE));
                tabla.addCell(reporte.celda((cuentas[i].getIntPinMax()!=-1 ? "x" : ""), font, contenido, izquierda, 0,0,Rectangle.RECTANGLE));
                tabla.addCell(reporte.celda(cuentas[i].getCatalogo().getNombre(), font, contenido, izquierda, 0,1,Rectangle.RECTANGLE));
                Double precio=0.0d;
                
                tabla.addCell(reporte.celda((cuentas[i].getInstruccion()!=null ? cuentas[i].getInstruccion() : ""), font, contenido, izquierda, 0,1,Rectangle.RECTANGLE));
                tabla.addCell(reporte.celda((cuentas[i].getPrioridad()!=4 ? ""+cuentas[i].getPrioridad() : ""), font, contenido, izquierda, 0,1,Rectangle.RECTANGLE));
                switch(this.cb_precio.getSelectedItem().toString())
                {
                    case "Cotizado":
                        //tabla.addCell(reporte.celda(formatoPorcentaje.format(cuentas[i].getCU()), font, contenido, derecha, 0,1,Rectangle.RECTANGLE));
                        precio=cuentas[i].getCU()*cuentas[i].getCant();
                        tot+=precio;
                        tabla.addCell(reporte.celda(formatoPorcentaje.format(precio), font, contenido, derecha, 0,1,Rectangle.RECTANGLE));
                        break;

                    case "Autorizado":
                        //tabla.addCell(reporte.celda(formatoPorcentaje.format(cuentas[i].getPrecioAutCU()), font, contenido, derecha, 0,1,Rectangle.RECTANGLE));
                        precio=cuentas[i].getPrecioAutCU()*cuentas[i].getCant();
                        tot+=precio;
                        tabla.addCell(reporte.celda(formatoPorcentaje.format(precio), font, contenido, derecha, 0,1,Rectangle.RECTANGLE));
                        break;

                    case "Compañia":
                        Double costo=0.0+Math.round(cuentas[i].getCU()/(1-(cuentas[i].getPorcentaje()*0.01)));
                        //tabla.addCell(reporte.celda(formatoPorcentaje.format(costo), font, contenido, derecha, 0,1,Rectangle.RECTANGLE));
                        precio=costo*cuentas[i].getCant();
                        tot+=precio;
                        tabla.addCell(reporte.celda(formatoPorcentaje.format(precio), font, contenido, derecha, 0,1,Rectangle.RECTANGLE));
                        break;

                    case "Compra"://costo de cotizacion
                        //tabla.addCell(reporte.celda(formatoPorcentaje.format(cuentas[i].getCU()), font, contenido, derecha, 0,1,Rectangle.RECTANGLE));
                        //costo de compra si ya fue comprado
                        double pcom=0d;
                        if(cuentas[i].getPedido()!=null)
                        {
                            pcom=cuentas[i].getPcp();
                            precio=pcom*cuentas[i].getCantPcp();
                            tabla.addCell(reporte.celda(formatoPorcentaje.format(precio), font, contenido, derecha, 0,1,Rectangle.RECTANGLE));
                            tot+=precio;
                        }
                        else
                            tabla.addCell(reporte.celda("0.00", font, contenido, derecha, 0,1,Rectangle.RECTANGLE));
                        break;
                }
            }
            if(cb_precio.getSelectedItem().toString().compareTo("Seleccionar")!=0)
            {
                tabla.addCell(reporte.celda("", font, contenido, izquierda, 20,1,Rectangle.NO_BORDER));
                tabla.addCell(reporte.celda(formatoPorcentaje.format(tot), font, contenido, derecha, 0,1,Rectangle.RECTANGLE));
            }
            
            session.getTransaction().commit();
            session.beginTransaction().commit();
            tabla.setHeaderRows(3);
            reporte.agregaObjeto(tabla);
            Paragraph parrafo=new Paragraph("P(Prioridad de la Partida)= 1(Alta), 2(Media), 3(Baja)", font);
            parrafo.setAlignment(centro);
            reporte.agregaObjeto(parrafo);
            reporte.cerrar();
            reporte.visualizar2("reportes/"+ord.getIdOrden()+"/"+valor+"-operaciones.pdf");
            
            if(entro==true)
                buscaCuentas(-1,-1);
        }catch(Exception e)
        {
            e.printStackTrace();
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "No se pudo realizar el reporte si el archivo esta abierto.");
        }
        if(session!=null)
            if(session.isOpen())
                    session.close();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void t_numFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_t_numFocusGained
        // TODO add your handling code here:
        t_num.selectAll();
    }//GEN-LAST:event_t_numFocusGained

    private void b_exel1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_exel1ActionPerformed
        // TODO add your handling code here:
        //h=new Herramientas(user, 0);
        //h.session(sessionPrograma);
        javax.swing.JFileChooser jF1= new javax.swing.JFileChooser(); 
        jF1.setFileFilter(new ExtensionFileFilter("Excel document (*.xls)", new String[] { "xls" }));
        String ruta = null; 
        DecimalFormat formatoPorcentaje = new DecimalFormat("#,##0.00");
        formatoPorcentaje.setMinimumFractionDigits(2);
        if(jF1.showSaveDialog(null)==jF1.APPROVE_OPTION)
        { 
            ruta = jF1.getSelectedFile().getAbsolutePath(); 
            if(ruta!=null)
            {
                File archivoXLS = new File(ruta+".xls");
                File plantilla = new File("imagenes/plantillaValuacion.xls");
                Session session = HibernateUtil.getSessionFactory().openSession();
                try
                {
                    Path FROM = Paths.get("imagenes/plantillaValuacion.xls");
                    Path TO = Paths.get(ruta+".xls");
                    //sobreescribir el fichero de destino, si existe, y copiar
                    // los atributos, incluyendo los permisos rwx
                    CopyOption[] options = new CopyOption[]
                    {
                        StandardCopyOption.REPLACE_EXISTING,
                        StandardCopyOption.COPY_ATTRIBUTES
                    }; 
                    Files.copy(FROM, TO, options);
                    
                    FileInputStream miPlantilla = new FileInputStream(archivoXLS);
                    POIFSFileSystem fsFileSystem = new POIFSFileSystem(miPlantilla);
                    Workbook libro = new HSSFWorkbook(fsFileSystem);
                    //Sheet hoja = libro.getSheet("valuacion");
                    //Cargamos las cabeceras
                    Configuracion con=(Configuracion)session.get(Configuracion.class, configuracion);
                    ord = (Orden)session.get(Orden.class, Integer.parseInt(orden));
                    
                    libro.getSheet("valuacion").getRow(0).getCell(10).setCellValue(con.getEmpresa());
                    
                    libro.getSheet("valuacion").getRow(1).getCell(2).setCellValue(ord.getIdOrden());
                    libro.getSheet("valuacion").getRow(1).getCell(6).setCellValue(ord.getFecha().toString());
                    libro.getSheet("valuacion").getRow(1).getCell(10).setCellValue(ord.getTipo().getTipoNombre());
                    
                    libro.getSheet("valuacion").getRow(2).getCell(2).setCellValue(ord.getClientes().getNombre());
                    libro.getSheet("valuacion").getRow(2).getCell(10).setCellValue(ord.getMarca().getMarcaNombre());
                    
                    if(ord.getSiniestro()!=null)
                        libro.getSheet("valuacion").getRow(3).getCell(2).setCellValue(ord.getSiniestro());
                    if(ord.getFechaSiniestro()!=null)
                        libro.getSheet("valuacion").getRow(3).getCell(6).setCellValue(ord.getFechaSiniestro().toString());
                    if(ord.getNoMotor()!=null)
                        libro.getSheet("valuacion").getRow(3).getCell(10).setCellValue(ord.getNoMotor());
                    libro.getSheet("valuacion").getRow(3).getCell(16).setCellValue(ord.getModelo());
                    
                    if(ord.getPoliza()!=null)
                        libro.getSheet("valuacion").getRow(4).getCell(2).setCellValue(ord.getPoliza());
                    if(ord.getInciso()!=null)
                        libro.getSheet("valuacion").getRow(4).getCell(6).setCellValue(ord.getInciso());
                    if(ord.getNoSerie()!=null)
                        libro.getSheet("valuacion").getRow(4).getCell(10).setCellValue(ord.getNoSerie());
                    if(ord.getNoEconomico()!=null)
                        libro.getSheet("valuacion").getRow(4).getCell(16).setCellValue(ord.getNoEconomico());
                    
                    CellStyle borde_d = libro.createCellStyle();
                    borde_d.setBorderBottom(CellStyle.BORDER_THIN);
                    borde_d.setBorderTop(CellStyle.BORDER_THIN);
                    borde_d.setBorderRight(CellStyle.BORDER_THIN);
                    borde_d.setBorderLeft(CellStyle.BORDER_THIN);
                    borde_d.setAlignment(CellStyle.ALIGN_RIGHT);
                    
                    CellStyle borde_i = libro.createCellStyle();
                    borde_i.setBorderBottom(CellStyle.BORDER_THIN);
                    borde_i.setBorderTop(CellStyle.BORDER_THIN);
                    borde_i.setBorderRight(CellStyle.BORDER_THIN);
                    borde_i.setBorderLeft(CellStyle.BORDER_THIN);
                    borde_i.setAlignment(CellStyle.ALIGN_LEFT);
                    
                    CellStyle borde_c = libro.createCellStyle();
                    borde_c.setBorderBottom(CellStyle.BORDER_THIN);
                    borde_c.setBorderTop(CellStyle.BORDER_THIN);
                    borde_c.setBorderRight(CellStyle.BORDER_THIN);
                    borde_c.setBorderLeft(CellStyle.BORDER_THIN);
                    borde_c.setAlignment(CellStyle.ALIGN_CENTER);
                    double total=0.0D;
                    for(int ren=0;ren<t_datos.getRowCount();ren++)
                    {
                        double v = 0.0d;
                        libro.getSheet("valuacion").createRow(ren+8);
                        libro.getSheet("valuacion").getRow(ren+8).createCell(1).setCellValue(t_datos.getValueAt(ren, 9).toString());//cant
                        libro.getSheet("valuacion").getRow(ren+8).getCell(1).setCellStyle(borde_d);
                        
                        libro.getSheet("valuacion").getRow(ren+8).createCell(2).setCellValue(t_datos.getValueAt(ren, 10).toString());//Med
                        libro.getSheet("valuacion").getRow(ren+8).getCell(2).setCellStyle(borde_c);
                        
                        libro.getSheet("valuacion").getRow(ren+8).createCell(3).setCellValue("");//Grupo
                        libro.getSheet("valuacion").getRow(ren+8).getCell(3).setCellStyle(borde_i);
                        libro.getSheet("valuacion").getRow(ren+8).createCell(4);
                        libro.getSheet("valuacion").getRow(ren+8).getCell(4).setCellStyle(borde_i);
                        libro.getSheet("valuacion").getRow(ren+8).createCell(5);
                        libro.getSheet("valuacion").getRow(ren+8).getCell(5).setCellStyle(borde_i);
                        libro.getSheet("valuacion").addMergedRegion(new CellRangeAddress(ren+8, ren+8, 3, 5));
                        
                        libro.getSheet("valuacion").getRow(ren+8).createCell(6).setCellValue(t_datos.getValueAt(ren, 2).toString());//Descripción
                        libro.getSheet("valuacion").getRow(ren+8).getCell(6).setCellStyle(borde_i);
                        libro.getSheet("valuacion").getRow(ren+8).createCell(7);
                        libro.getSheet("valuacion").getRow(ren+8).getCell(7).setCellStyle(borde_i);
                        libro.getSheet("valuacion").getRow(ren+8).createCell(8);
                        libro.getSheet("valuacion").getRow(ren+8).getCell(8).setCellStyle(borde_i);
                        libro.getSheet("valuacion").getRow(ren+8).createCell(9);
                        libro.getSheet("valuacion").getRow(ren+8).getCell(9).setCellStyle(borde_i);
                        libro.getSheet("valuacion").getRow(ren+8).createCell(10);
                        libro.getSheet("valuacion").getRow(ren+8).getCell(10).setCellStyle(borde_i);
                        libro.getSheet("valuacion").addMergedRegion(new CellRangeAddress(ren+8, ren+8, 6, 10));
                        
                        if(t_datos.getValueAt(ren, 3)!=null)
                        {
                            v = Double.parseDouble(t_datos.getValueAt(ren, 3).toString()) * Double.parseDouble(t_datos.getValueAt(ren, 9).toString());
                            libro.getSheet("valuacion").getRow(ren+8).createCell(11).setCellValue(new BigDecimal(v).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());//D/M
                        }
                        else
                            libro.getSheet("valuacion").getRow(ren+8).createCell(11).setCellValue("");
                        libro.getSheet("valuacion").getRow(ren+8).getCell(11).setCellStyle(borde_d);
                        
                        if(t_datos.getValueAt(ren, 7)!=null)
                        {
                            v = Double.parseDouble(t_datos.getValueAt(ren, 7).toString()) * Double.parseDouble(t_datos.getValueAt(ren, 9).toString());
                            libro.getSheet("valuacion").getRow(ren+8).createCell(12).setCellValue(new BigDecimal(v).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());//Cam
                        }
                        else
                            libro.getSheet("valuacion").getRow(ren+8).createCell(12).setCellValue("");
                        libro.getSheet("valuacion").getRow(ren+8).getCell(12).setCellStyle(borde_d);
                        
                        if(t_datos.getValueAt(ren, 4)!=null)
                        {
                            v = Double.parseDouble(t_datos.getValueAt(ren, 4).toString()) * Double.parseDouble(t_datos.getValueAt(ren, 9).toString());
                            libro.getSheet("valuacion").getRow(ren+8).createCell(13).setCellValue(new BigDecimal(v).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());//R. min
                        }
                        else
                            libro.getSheet("valuacion").getRow(ren+8).createCell(13).setCellValue("");
                        libro.getSheet("valuacion").getRow(ren+8).getCell(13).setCellStyle(borde_d);
                        
                        if(t_datos.getValueAt(ren, 5)!=null)
                        {
                            v = Double.parseDouble(t_datos.getValueAt(ren, 5).toString()) * Double.parseDouble(t_datos.getValueAt(ren, 9).toString());
                            libro.getSheet("valuacion").getRow(ren+8).createCell(14).setCellValue(new BigDecimal(v).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());//R. med
                        }
                        else
                            libro.getSheet("valuacion").getRow(ren+8).createCell(14).setCellValue("");
                        libro.getSheet("valuacion").getRow(ren+8).getCell(14).setCellStyle(borde_d);
                        
                        if(t_datos.getValueAt(ren, 6)!=null)
                        {
                            v = Double.parseDouble(t_datos.getValueAt(ren, 6).toString()) * Double.parseDouble(t_datos.getValueAt(ren, 9).toString());
                            libro.getSheet("valuacion").getRow(ren+8).createCell(15).setCellValue(new BigDecimal(v).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());//R. max
                        }
                        else
                            libro.getSheet("valuacion").getRow(ren+8).createCell(15).setCellValue("");
                        libro.getSheet("valuacion").getRow(ren+8).getCell(15).setCellStyle(borde_d);
                        
                        if(t_datos.getValueAt(ren, 8)!=null)
                        {
                            v = Double.parseDouble(t_datos.getValueAt(ren, 8).toString()) * Double.parseDouble(t_datos.getValueAt(ren, 9).toString());
                            libro.getSheet("valuacion").getRow(ren+8).createCell(16).setCellValue(new BigDecimal(v).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());//Pin
                        }
                        else
                            libro.getSheet("valuacion").getRow(ren+8).createCell(16).setCellValue("");
                        libro.getSheet("valuacion").getRow(ren+8).getCell(16).setCellStyle(borde_d);
                        
                        Double suma =Double.parseDouble(t_datos.getValueAt(ren, 21).toString());
                        suma*=ord.getCompania().getImporteHora();
                        total+=suma;
                        if(t_datos.getValueAt(ren, 21)!=null)
                            libro.getSheet("valuacion").getRow(ren+8).createCell(17).setCellValue(formatoPorcentaje.format(suma));//Costo M.O
                        else
                            libro.getSheet("valuacion").getRow(ren+8).createCell(17).setCellValue("");
                        libro.getSheet("valuacion").getRow(ren+8).getCell(17).setCellStyle(borde_d);
                    }
                    int renglon=t_datos.getRowCount()+8;
                    int celda =renglon;
                    libro.getSheet("valuacion").createRow(renglon);
                    libro.getSheet("valuacion").getRow(renglon).createCell(6);//
                    libro.getSheet("valuacion").getRow(renglon).getCell(6).setCellValue("Costo M.O:$"+formatoPorcentaje.format(ord.getCompania().getImporteHora())+" Total de Horas:"+t_horas.getText());
                    libro.getSheet("valuacion").getRow(renglon).getCell(6).setCellStyle(borde_c);
                    libro.getSheet("valuacion").getRow(renglon).createCell(7);
                    libro.getSheet("valuacion").getRow(renglon).getCell(7).setCellStyle(borde_i);
                    libro.getSheet("valuacion").getRow(renglon).createCell(8);
                    libro.getSheet("valuacion").getRow(renglon).getCell(8).setCellStyle(borde_i);
                    libro.getSheet("valuacion").getRow(renglon).createCell(9);
                    libro.getSheet("valuacion").getRow(renglon).getCell(9).setCellStyle(borde_i);
                    libro.getSheet("valuacion").getRow(renglon).createCell(10);
                    libro.getSheet("valuacion").getRow(renglon).getCell(10).setCellStyle(borde_i);
                    libro.getSheet("valuacion").addMergedRegion(new CellRangeAddress(renglon, renglon, 6, 10));
                    
                    
                    libro.getSheet("valuacion").getRow(renglon).createCell(11);//
                    libro.getSheet("valuacion").getRow(renglon).getCell(11).setCellType(HSSFCell.CELL_TYPE_FORMULA);
                    libro.getSheet("valuacion").getRow(renglon).getCell(11).setCellFormula("SUM(L9:L"+celda+")");
                    libro.getSheet("valuacion").getRow(renglon).getCell(11).setCellStyle(borde_d);
                    
                    libro.getSheet("valuacion").getRow(renglon).createCell(12);//
                    libro.getSheet("valuacion").getRow(renglon).getCell(12).setCellType(HSSFCell.CELL_TYPE_FORMULA);
                    libro.getSheet("valuacion").getRow(renglon).getCell(12).setCellFormula("SUM(M9:M"+celda+")");
                    libro.getSheet("valuacion").getRow(renglon).getCell(12).setCellStyle(borde_d);
                    
                    libro.getSheet("valuacion").getRow(renglon).createCell(13);//
                    libro.getSheet("valuacion").getRow(renglon).getCell(13).setCellType(HSSFCell.CELL_TYPE_FORMULA);
                    libro.getSheet("valuacion").getRow(renglon).getCell(13).setCellFormula("SUM(N9:N"+celda+")");
                    libro.getSheet("valuacion").getRow(renglon).getCell(13).setCellStyle(borde_d);
                    
                    libro.getSheet("valuacion").getRow(renglon).createCell(14);//
                    libro.getSheet("valuacion").getRow(renglon).getCell(14).setCellType(HSSFCell.CELL_TYPE_FORMULA);
                    libro.getSheet("valuacion").getRow(renglon).getCell(14).setCellFormula("SUM(O9:O"+celda+")");
                    libro.getSheet("valuacion").getRow(renglon).getCell(14).setCellStyle(borde_d);
                    
                    libro.getSheet("valuacion").getRow(renglon).createCell(15);//
                    libro.getSheet("valuacion").getRow(renglon).getCell(15).setCellType(HSSFCell.CELL_TYPE_FORMULA);
                    libro.getSheet("valuacion").getRow(renglon).getCell(15).setCellFormula("SUM(P9:P"+celda+")");
                    libro.getSheet("valuacion").getRow(renglon).getCell(15).setCellStyle(borde_d);
                    
                    libro.getSheet("valuacion").getRow(renglon).createCell(16);//
                    libro.getSheet("valuacion").getRow(renglon).getCell(16).setCellType(HSSFCell.CELL_TYPE_FORMULA);
                    libro.getSheet("valuacion").getRow(renglon).getCell(16).setCellFormula("SUM(Q9:Q"+celda+")");
                    libro.getSheet("valuacion").getRow(renglon).getCell(16).setCellStyle(borde_d);
                    
                    libro.getSheet("valuacion").getRow(renglon).createCell(17);//
                    libro.getSheet("valuacion").getRow(renglon).getCell(17).setCellValue(formatoPorcentaje.format(total));
                    libro.getSheet("valuacion").getRow(renglon).getCell(17).setCellStyle(borde_d);
                    FileOutputStream archivo = new FileOutputStream(archivoXLS);
                    
                    libro.write(archivo);
                    archivo.close();
                    //miPlantilla.close();
                    Desktop.getDesktop().open(archivoXLS);
                }catch(Exception e)
                {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(this, "No se pudo realizar el reporte si el archivo esta abierto");
                }
                if(session!=null)
                    if(session.isOpen())
                    {
                        session.flush();
                        session.clear();
                        session.close();
                    }
            }
        }
    }//GEN-LAST:event_b_exel1ActionPerformed

    private void b_enviarMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_b_enviarMouseReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_b_enviarMouseReleased

    private void b_enviarMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_b_enviarMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_b_enviarMouseExited

    private void b_enviarMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_b_enviarMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_b_enviarMouseEntered

    private void b_enviarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_enviarActionPerformed
        // TODO add your handling code here:
        EnviarCorreo en= new EnviarCorreo(new javax.swing.JFrame(), true, "","", "", null, this.user, this.sessionPrograma);
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        en.setLocation((d.width/2)-(en.getWidth()/2), (d.height/2)-(en.getHeight()/2));
        en.setVisible(true);
    }//GEN-LAST:event_b_enviarActionPerformed

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
        }
    }//GEN-LAST:event_t_datosKeyPressed

    private void b_exel2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_exel2ActionPerformed
        // TODO add your handling code here:
        //h=new Herramientas(user, 0);
        //h.session(sessionPrograma);
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
                    Sheet hoja = libro.createSheet("compra");
                    //hoja.protectSheet("04650077");
                    for(int ren=0;ren<(t_datos.getRowCount()+1);ren++)
                    {
                        Row fila = hoja.createRow(ren);
                        if(ren==0 || ((boolean)t_datos.getValueAt(ren-1, 23))==true)
                        {
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
                                        celda.setCellValue("✓");
                                        else
                                        {
                                            if(t_datos.getValueAt(ren-1, col).toString().compareToIgnoreCase("true")==0)
                                            celda.setCellValue("");
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
                    }
                    libro.write(archivo);
                    archivo.close();
                    Desktop.getDesktop().open(archivoXLS);
                }catch(Exception e)
                {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(this, "No se pudo realizar el reporte si el archivo esta abierto");
                }
            }
        }
    }//GEN-LAST:event_b_exel2ActionPerformed

    private void btActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btActionPerformed
        // TODO add your handling code here:
        //h=new Herramientas(user, 0);
        //h.session(sessionPrograma);
        Session session = HibernateUtil.getSessionFactory().openSession();
        try
        {
            DecimalFormat formatoPorcentaje = new DecimalFormat("#,##0.00");
            formatoPorcentaje.setMinimumFractionDigits(2);   
            session.beginTransaction().begin();
            BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.WINANSI, BaseFont.NOT_EMBEDDED);
            PDF reporte = new PDF();
            Date fecha = new Date();
            DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyyHH-mm-ss");//YYYY-MM-DD HH:MM:SS
            String valor=dateFormat.format(fecha);
            File folder = new File("reportes/"+ord.getIdOrden());
            folder.mkdirs();
            reporte.Abrir2(PageSize.LETTER.rotate(), "Valuación", "reportes/"+ord.getIdOrden()+"/"+valor+"-ref_precio.pdf");
            Font font = new Font(Font.FontFamily.HELVETICA, 6, Font.BOLD);
            BaseColor contenido=BaseColor.WHITE;
            int centro=Element.ALIGN_CENTER;
            int izquierda=Element.ALIGN_LEFT;
            int derecha=Element.ALIGN_RIGHT;
            int ren=0;
            double dm=0d, cam=0d, min=0d, med=0d, max=0d, pin=0d, tot=0d;
            session.beginTransaction().begin();
            Orden ord = (Orden)session.get(Orden.class, Integer.parseInt(orden));
            Configuracion config=(Configuracion)session.get(Configuracion.class, configuracion);

            float tam[];
            if(this.cb_precio.getSelectedItem().toString().compareToIgnoreCase("Compra")==0)
                tam=new float[]{12,12,50,110,15,15,25,25,25,25};
            else
                tam=new float[]{12,12,50,110,15,15,25,25,35,12};

            PdfPTable tabla=reporte.crearTabla(10, tam, 100, Element.ALIGN_LEFT);

            cabecera1(reporte, bf, tabla, config.getCinta());

            for(int i=0; i<t_datos.getRowCount(); i++)
            {    
                double suma=0d;
                tabla.addCell(reporte.celda(""+t_datos.getValueAt(i, 0).toString(), font, contenido, derecha, 0,1,Rectangle.RECTANGLE));
                tabla.addCell(reporte.celda(""+t_datos.getValueAt(i, 1).toString(), font, contenido, derecha, 0,1,Rectangle.RECTANGLE));
                if(t_datos.getValueAt(i, 12)!=null)
                    tabla.addCell(reporte.celda(t_datos.getValueAt(i, 12).toString(), font, contenido, centro, 0,1,Rectangle.RECTANGLE));
                else
                    tabla.addCell(reporte.celda("S/N", font, contenido, centro, 0,0,Rectangle.RECTANGLE));

                tabla.addCell(reporte.celda(t_datos.getValueAt(i, 2).toString(), font, contenido, izquierda, 0,1,Rectangle.RECTANGLE));
                tabla.addCell(reporte.celda(""+t_datos.getValueAt(i, 9).toString(), font, contenido, derecha, 0,1,Rectangle.RECTANGLE));
                tabla.addCell(reporte.celda(t_datos.getValueAt(i, 10).toString(), font, contenido, derecha, 0,1,Rectangle.RECTANGLE));

                //Double costo=0.0+Math.round(cuentas[i].getCU()/(1-(cuentas[i].getPorcentaje()*0.01)));
                Double costo=0.0;
                if(t_datos.getValueAt(i, 15)!=null)
                {
                    costo=(Double)t_datos.getValueAt(i, 15);
                    tabla.addCell(reporte.celda(formatoPorcentaje.format(costo), font, contenido, derecha, 0,1,Rectangle.RECTANGLE));
                }
                else
                    tabla.addCell(reporte.celda(formatoPorcentaje.format(0.0), font, contenido, derecha, 0,1,Rectangle.RECTANGLE));
                //Double precio=costo*cuentas[i].getCant();
                Double precio=costo*(Double)t_datos.getValueAt(i, 9);
                tot+=precio;
                tabla.addCell(reporte.celda(formatoPorcentaje.format(precio), font, contenido, derecha, 0,1,Rectangle.RECTANGLE));

                tabla.addCell(reporte.celda("", font, contenido, derecha, 0,1,Rectangle.RECTANGLE));
                if(t_datos.getValueAt(i, 25)!=null)
                    tabla.addCell(reporte.celda(t_datos.getValueAt(i, 25).toString(), font, contenido, centro, 0,1,Rectangle.RECTANGLE));
                else
                    tabla.addCell(reporte.celda("", font, contenido, centro, 0,1,Rectangle.RECTANGLE));
            }
            //if(this.cb_precio.getSelectedItem().toString().compareToIgnoreCase("Compra")!=0)
            //{
                tabla.addCell(reporte.celda(" ", font, contenido, derecha, 7,1,Rectangle.TOP));
                tabla.addCell(reporte.celda(formatoPorcentaje.format(tot), font, contenido, derecha, 0,1,Rectangle.RECTANGLE));
                tabla.addCell(reporte.celda(" ", font, contenido, derecha, 2,1,Rectangle.TOP));
            //}
            tabla.setHeaderRows(1);
            reporte.agregaObjeto(tabla);

            reporte.cerrar();
            reporte.visualizar2("reportes/"+ord.getIdOrden()+"/"+valor+"-ref_precio.pdf");
        }catch(Exception e)
        {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "No se pudo realizar el reporte");
        }
        if(session!=null)
            if(session.isOpen())
            {
                session.flush();
                session.clear();
                session.close();
            }
    }//GEN-LAST:event_btActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:
        Session session = HibernateUtil.getSessionFactory().openSession();
        try
        {
            session.beginTransaction().begin();
            DecimalFormat formatoPorcentaje = new DecimalFormat("#,##0.00");
            DecimalFormat formatoDecimal = new DecimalFormat("####0.0");
            formatoPorcentaje.setMinimumFractionDigits(2);
            BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.WINANSI, BaseFont.NOT_EMBEDDED);
            //Orden ord=buscaApertura();
            PDF reporte = new PDF();
            Date fecha = new Date();
            DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyyHH-mm-ss");//YYYY-MM-DD HH:MM:SS
            String valor=dateFormat.format(fecha);
            File folder = new File("reportes/"+ord.getIdOrden());
            folder.mkdirs();
            if(r_cerrar_valuacion.isSelected()==false)
                reporte.Abrir2(PageSize.LETTER.rotate(), "Operaciones-No-Autorizado", "reportes/"+ord.getIdOrden()+"/"+valor+"-operaciones1.pdf");
            else
                reporte.Abrir2(PageSize.LETTER.rotate(), "Operaciones-Autorizado", "reportes/"+ord.getIdOrden()+"/"+valor+"-operaciones1.pdf");
            Font font = new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL);
            BaseColor contenido=BaseColor.WHITE;
            int centro=Element.ALIGN_CENTER;
            int izquierda=Element.ALIGN_LEFT;
            int derecha=Element.ALIGN_RIGHT;
            float tam[]=new float[]{15,12,10,10,10,10,15,15,15,10,10,10,10,10,10,10,10,140,90,10};
            PdfPTable tabla=reporte.crearTabla(20, tam, 100, Element.ALIGN_LEFT);
            
            cabecera2(reporte, bf, tabla, " Reparaciones ");
            Partida[] cuentas=new Partida[0];;
            
            Criteria cr;
            switch(cb_partidas.getSelectedItem().toString())
            {
                case "Marcadas":
                    if(t_datos.getSelectedRows().length>0)
                    {   
                        String consulta="from Partida where (concat(idEvaluacion, subPartida) in (";
                            int [] selec=t_datos.getSelectedRows();
                            for(int x=0; x<selec.length; x++)
                            {
                                if(x>0)
                                    consulta+=", ";
                                consulta+=t_datos.getValueAt(selec[x], 0).toString()+t_datos.getValueAt(selec[x], 1).toString();
                            }
                            switch(cb_tipo.getSelectedItem().toString())
                            {
                                case "Hojalateria":
                                    consulta+=")) AND(espHoj=true) ";
                                    break;
                                case "Mecanica":
                                    consulta+=")) AND(espMec=true) ";
                                    break;
                                case "Suspension":
                                    consulta+=")) AND(espSus=true) ";
                                    break;
                                case "Electrico":
                                    consulta+=")) AND(espEle=true) ";
                                    break;
                                case "Pintura":
                                    consulta+=")) AND(intPinMin>-1 OR intPinMed>-1 OR intPinMax>-1) ";
                                    break;
                                case "Todas":
                                    consulta+=")) ";
                                    break;
                            }
                            consulta+="AND (ordenByIdOrden.idOrden="+orden+") AND (autorizadoValuacion=true) AND (intCamb=-1)  order by idEvaluacion asc, subPartida asc";
                            Query q = session.createQuery(consulta);
                            cuentas=(Partida[]) q.list().toArray(new Partida[0]);
                    }
                    else
                        cuentas= new Partida[0];
                    break;
                case "Sin Autorizar":
                    cr=session.createCriteria(Partida.class).
                                add(Restrictions.eq("ordenByIdOrden.idOrden", ord.getIdOrden())).
                                add(Restrictions.eq("autorizadoValuacion", true)).
                                add(Restrictions.eq("autorizado", false)).
                                add(Restrictions.eq("intCamb", -1.0d));
                    switch(cb_tipo.getSelectedItem().toString())
                    {
                        case "Hojalateria":                    
                            cr.add(Restrictions.eq("espHoj", true));
                            break;
                        case "Mecanica":
                            cr.add(Restrictions.eq("espMec", true));
                            break;
                        case "Suspension":
                            cr.add(Restrictions.eq("espSus", true));
                            break;
                        case "Electrico":
                            cr.add(Restrictions.eq("espEle", true));
                            break;
                        case "Pintura":
                            cr.add(Restrictions.or(Restrictions.or(Restrictions.ne("intPinMin", -1.0d), Restrictions.ne("intPinMed", -1.0d)), Restrictions.ne("intPinMax", -1.0d)));
                            //cr.add(Restrictions.sqlRestriction(" AND(intPinMin>-1 OR intPinMed>-1 OR intPinMax>-1) "));
                            break;
                    }
                    cr.addOrder(Order.asc("idEvaluacion"));
                    cr.addOrder(Order.asc("subPartida"));
                    cuentas = (Partida[])cr.list().toArray(new Partida[0]);
                    break;
                case "Autorizadas":
                    cr=session.createCriteria(Partida.class).
                                add(Restrictions.eq("ordenByIdOrden.idOrden", ord.getIdOrden())).
                                add(Restrictions.eq("autorizadoValuacion", true)).
                                add(Restrictions.eq("autorizado", true)).
                                add(Restrictions.eq("intCamb", -1.0d));
                    switch(cb_tipo.getSelectedItem().toString())
                    {
                        case "Hojalateria":                    
                            cr.add(Restrictions.eq("espHoj", true));
                            break;
                        case "Mecanica":
                            cr.add(Restrictions.eq("espMec", true));
                            break;
                        case "Suspension":
                            cr.add(Restrictions.eq("espSus", true));
                            break;
                        case "Electrico":
                            cr.add(Restrictions.eq("espEle", true));
                            break;
                        case "Pintura":
                            cr.add(Restrictions.or(Restrictions.or(Restrictions.ne("intPinMin", -1.0d), Restrictions.ne("intPinMed", -1.0d)), Restrictions.ne("intPinMax", -1.0d)));
                            //cr.add(Restrictions.sqlRestriction(" AND(intPinMin>-1 OR intPinMed>-1 OR intPinMax>-1) "));
                            break;
                    }
                    cr.addOrder(Order.asc("idEvaluacion"));
                    cr.addOrder(Order.asc("subPartida"));
                    cuentas = (Partida[])cr.list().toArray(new Partida[0]);
                    break;
                default:
                    cr=session.createCriteria(Partida.class).
                                add(Restrictions.eq("ordenByIdOrden.idOrden", ord.getIdOrden())).
                                add(Restrictions.eq("autorizadoValuacion", true)).
                                add(Restrictions.eq("intCamb", -1.0d));
                    switch(cb_tipo.getSelectedItem().toString())
                    {
                        case "Hojalateria":                    
                            cr.add(Restrictions.eq("espHoj", true));
                            break;
                        case "Mecanica":
                            cr.add(Restrictions.eq("espMec", true));
                            break;
                        case "Suspension":
                            cr.add(Restrictions.eq("espSus", true));
                            break;
                        case "Electrico":
                            cr.add(Restrictions.eq("espEle", true));
                            break;
                        case "Pintura":
                            cr.add(Restrictions.or(Restrictions.or(Restrictions.ne("intPinMin", -1.0d), Restrictions.ne("intPinMed", -1.0d)), Restrictions.ne("intPinMax", -1.0d)));
                            break;
                    }
                    cr.addOrder(Order.asc("idEvaluacion"));
                    cr.addOrder(Order.asc("subPartida"));
                    cuentas = (Partida[])cr.list().toArray(new Partida[0]);
                    break;
            }
            
            int ren=0;
            for(int i=0; i<cuentas.length; i++)
            {   
                double suma=0d;
                tabla.addCell(reporte.celda(""+cuentas[i].getIdEvaluacion(), font, contenido, izquierda, 0,1,Rectangle.RECTANGLE));
                tabla.addCell(reporte.celda(""+cuentas[i].getSubPartida(), font, contenido, izquierda, 0,1,Rectangle.RECTANGLE));
                tabla.addCell(reporte.celda((cuentas[i].isEspHoj()==true ? "x" : ""), font, contenido, izquierda, 0,0,Rectangle.RECTANGLE));
                tabla.addCell(reporte.celda((cuentas[i].isEspMec()==true ? "x" : ""), font, contenido, izquierda, 0,0,Rectangle.RECTANGLE));
                tabla.addCell(reporte.celda((cuentas[i].isEspSus()==true ? "x" : ""), font, contenido, izquierda, 0,0,Rectangle.RECTANGLE));
                tabla.addCell(reporte.celda((cuentas[i].isEspEle()==true ? "x" : ""), font, contenido, izquierda, 0,0,Rectangle.RECTANGLE));
                tabla.addCell(reporte.celda(""+cuentas[i].getCant(), font, contenido, izquierda, 0,1,Rectangle.RECTANGLE));
                tabla.addCell(reporte.celda(""+cuentas[i].getCantidadAut(), font, contenido, izquierda, 0,1,Rectangle.RECTANGLE));
                tabla.addCell(reporte.celda(cuentas[i].getMed(), font, contenido, izquierda, 0,1,Rectangle.RECTANGLE));
                tabla.addCell(reporte.celda((cuentas[i].getDm()!=-1 ? "x" : ""), font, contenido, izquierda, 0,0,Rectangle.RECTANGLE));
                tabla.addCell(reporte.celda((cuentas[i].getIntCamb()!=-1 ? "x" : ""), font, contenido, izquierda, 0,0,Rectangle.RECTANGLE));
                tabla.addCell(reporte.celda((cuentas[i].getIntRepMin()!=-1 ? "x" : ""), font, contenido, izquierda, 0,0,Rectangle.RECTANGLE));
                tabla.addCell(reporte.celda((cuentas[i].getIntRepMed()!=-1 ? "x" : ""), font, contenido, izquierda, 0,0,Rectangle.RECTANGLE));
                tabla.addCell(reporte.celda((cuentas[i].getIntRepMax()!=-1 ? "x" : ""), font, contenido, izquierda, 0,0,Rectangle.RECTANGLE));
                tabla.addCell(reporte.celda((cuentas[i].getIntPinMin()!=-1 ? "x" : ""), font, contenido, izquierda, 0,0,Rectangle.RECTANGLE));
                tabla.addCell(reporte.celda((cuentas[i].getIntPinMed()!=-1 ? "x" : ""), font, contenido, izquierda, 0,0,Rectangle.RECTANGLE));
                tabla.addCell(reporte.celda((cuentas[i].getIntPinMax()!=-1 ? "x" : ""), font, contenido, izquierda, 0,0,Rectangle.RECTANGLE));
                tabla.addCell(reporte.celda(cuentas[i].getCatalogo().getNombre(), font, contenido, izquierda, 0,1,Rectangle.RECTANGLE));
                tabla.addCell(reporte.celda((cuentas[i].getInstruccion()!=null ? cuentas[i].getInstruccion() : ""), font, contenido, izquierda, 0,1,Rectangle.RECTANGLE));
                tabla.addCell(reporte.celda((cuentas[i].getPrioridad()!=4 ? ""+cuentas[i].getPrioridad() : ""), font, contenido, izquierda, 0,1,Rectangle.RECTANGLE));
            }
            tabla.setHeaderRows(3);
            reporte.agregaObjeto(tabla);
            Paragraph parrafo=new Paragraph("P(Prioridad de la Partida)= 1(Alta), 2(Media), 3(Baja)", font);
            parrafo.setAlignment(centro);
            reporte.agregaObjeto(parrafo);
            reporte.cerrar();
            reporte.visualizar2("reportes/"+ord.getIdOrden()+"/"+valor+"-operaciones1.pdf");
        }catch(Exception e)
        {
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
    }//GEN-LAST:event_jButton5ActionPerformed

    private void t_mo_directa1FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_t_mo_directa1FocusLost
        // TODO add your handling code here:
        if(t_mo_directa.getText().compareTo("")==0)
        {
            t_mo_directa.setText("0");
            t_mo_directa.setValue(0.00);
        }
        try
        {
            t_mo_directa.commitEdit();
        }catch(Exception e){e.printStackTrace();}
        if(t_mo_directa1.getText().compareTo("")==0)
        {
            t_mo_directa1.setText("0");
            t_mo_directa1.setValue(0.00);
        }
        try
        {
            t_mo_directa1.commitEdit();
        }catch(Exception e){e.printStackTrace();}
        if(t_ref_autorizadas_directo.getText().compareTo("")==0)
        {
            t_ref_autorizadas_directo.setText("0");
            t_ref_autorizadas_directo.setValue(0.00);
        }
        try
        {
            t_ref_autorizadas_directo.commitEdit();
        }catch(Exception e){e.printStackTrace();}
        Session session = HibernateUtil.getSessionFactory().openSession();
        try
        {
            session.getTransaction().begin();
            user = (Usuario)session.get(Usuario.class, user.getIdUsuario());
            h=new Herramientas(user, 0);
            h.session(sessionPrograma);
            ord=(Orden)session.load(Orden.class, ord.getIdOrden());
            ord.setMoDirecta(((Number)t_mo_directa.getValue()).doubleValue());
            ord.setRefAutorizadas(((Number)t_ref_autorizadas_directo.getValue()).doubleValue());
            ord.setMoPresupuestada(((Number)t_mo_directa1.getValue()).doubleValue());
            session.update(ord);
            session.getTransaction().commit();
        }
        catch(Exception e)
        {
            session.getTransaction().rollback();
            JOptionPane.showMessageDialog(null, "¡Error al actualizar los datos!");
            e.printStackTrace();
        }
        if(session!=null)
            if(session.isOpen()==true)
            {
                session.flush();
                session.clear();
                session.close();
            }
    }//GEN-LAST:event_t_mo_directa1FocusLost

    private void t_mo_directa1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_t_mo_directa1ActionPerformed
        // TODO add your handling code here:
        t_busca.requestFocus();
    }//GEN-LAST:event_t_mo_directa1ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        // TODO add your handling code here:s
       cb_partidas.setSelectedIndex(0);
       cb_tipo.setSelectedIndex(0);
       cb_precio.setSelectedItem("Compañia");
       foto=1;
       this.jButton2ActionPerformed(null);
       foto=0;
    }//GEN-LAST:event_jButton6ActionPerformed

    private void cb_tipoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cb_tipoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cb_tipoActionPerformed

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
           /*RegistraConsumibles reg_con=new RegistraConsumibles(new javax.swing.JFrame(), true, orden, user, sessionPrograma, r_cerrar_valuacion.isSelected());
           Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
           reg_con.setLocation((d.width/2)-(880/2), (d.height/2)-(405/2));
           reg_con.setSize(880, 405);
           reg_con.setVisible(true);
           SumaTotal();*/
       }
    }//GEN-LAST:event_b_consumiblesActionPerformed

    public void bloquea_consumible(){
        cb_hojalateria.setEnabled(false);
        cb_mecanica.setEnabled(false);
        cb_suspension.setEnabled(false);
        cb_electrico.setEnabled(false);
        cb_pintura.setEnabled(false);
        t_hojalateria.setEnabled(false);
        t_mecanica.setEnabled(false);
        t_suspension.setEnabled(false);
        t_electrico.setEnabled(false);
        t_pintura.setEnabled(false);
        jButton7.setEnabled(false);
    }
    private void t_pinturaFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_t_pinturaFocusLost
        // TODO add your handling code here:
        if(t_pintura.getText().compareTo("")==0)
        {
            t_pintura.setText("0");
            t_pintura.setValue(0.00);
        }
    }//GEN-LAST:event_t_pinturaFocusLost

    private void t_electricoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_t_electricoFocusLost
        // TODO add your handling code here:
        if(t_electrico.getText().compareTo("")==0)
        {
            t_electrico.setText("0");
            t_electrico.setValue(0.00);
        }
    }//GEN-LAST:event_t_electricoFocusLost

    private void t_suspensionFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_t_suspensionFocusLost
        // TODO add your handling code here:
        if(t_suspension.getText().compareTo("")==0)
        {
            t_suspension.setText("0");
            t_suspension.setValue(0.00);
        }
    }//GEN-LAST:event_t_suspensionFocusLost

    private void t_mecanicaFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_t_mecanicaFocusLost
        // TODO add your handling code here:
        if(t_mecanica.getText().compareTo("")==0)
        {
            t_mecanica.setText("0");
            t_mecanica.setValue(0.00);
        }
    }//GEN-LAST:event_t_mecanicaFocusLost

    private void t_hojalateriaFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_t_hojalateriaFocusLost
        // TODO add your handling code here:
        if(t_hojalateria.getText().compareTo("")==0)
        {
            t_hojalateria.setText("0");
            t_hojalateria.setValue(0.00);
        }
    }//GEN-LAST:event_t_hojalateriaFocusLost

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        // TODO add your handling code here:
        double total=0.00, t_h = 0.00, t_m = 0.00, t_s = 0.00, t_e = 0.00, t_p = 0.00;
        Session session = HibernateUtil.getSessionFactory().openSession();        
        session.beginTransaction().begin();
        Orden ord1=(Orden)session.get(Orden.class, Integer.parseInt(this.orden));
        Configuracion con= (Configuracion)session.get(Configuracion.class, configuracion);
        double iva = con.getIva(); 
        
        //***guardar la fecha de cierre del levantamiento*****
        try{
            //HOJALATERIA
            if(cb_hojalateria.isSelected()==true){
                ord1.setDHojalateria(true);
                t_hojalateria.setValue(ord1.getPHojalateria());
            }else{
                Query query2 = session.createSQLQuery("select (select if(sum(cantidad*valor) is null, 0, sum(cantidad*valor)) from movimiento left join almacen on movimiento.id_almacen=almacen.id_almacen left join orden on almacen.id_orden=orden.id_orden where orden.id_orden="+ord.getIdOrden()+" and almacen.operacion=5 and almacen.tipo_movimiento=2 and almacen.especialidad='h') - (select if(sum(cantidad*valor) is null, 0, sum(cantidad*valor)) from movimiento left join almacen on movimiento.id_almacen=almacen.id_almacen left join orden on almacen.id_orden=orden.id_orden where orden.id_orden="+ord1.getIdOrden()+" and almacen.operacion=5 and almacen.tipo_movimiento=1 and almacen.especialidad='h')as monto_consumible");
                query2.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
                ArrayList consumido = (ArrayList)query2.list();
                for(int i =0; i<consumido.size(); i++){
                    java.util.HashMap map=(java.util.HashMap)consumido.get(i);
                    t_h = Double.parseDouble(map.get("monto_consumible").toString());
                }
                Double iva_h = t_h*(iva /100);
                iva_h = t_h+iva_h;
                if(((Number)t_hojalateria.getValue()).doubleValue()>=iva_h){
                    ord1.setPHojalateria(((Number)t_hojalateria.getValue()).doubleValue());
                    ord1.setDHojalateria(false);
                }else{
                    t_hojalateria.setValue(ord1.getPHojalateria());
                    JOptionPane.showMessageDialog(this, "El Monto Hojalateria es menor al Consumido");
                }
            }
            //MECANICA
            if(cb_mecanica.isSelected()==true){
                ord1.setDMecanica(true);
                t_mecanica.setValue(ord1.getPMecanica());
            }else{
                Query query2 = session.createSQLQuery("select (select if(sum(cantidad*valor) is null, 0, sum(cantidad*valor)) from movimiento left join almacen on movimiento.id_almacen=almacen.id_almacen left join orden on almacen.id_orden=orden.id_orden where orden.id_orden="+ord.getIdOrden()+" and almacen.operacion=5 and almacen.tipo_movimiento=2 and almacen.especialidad='m') - (select if(sum(cantidad*valor) is null, 0, sum(cantidad*valor)) from movimiento left join almacen on movimiento.id_almacen=almacen.id_almacen left join orden on almacen.id_orden=orden.id_orden where orden.id_orden="+ord1.getIdOrden()+" and almacen.operacion=5 and almacen.tipo_movimiento=1 and almacen.especialidad='m')as monto_consumible");
                query2.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
                ArrayList consumido = (ArrayList)query2.list();
                for(int i =0; i<consumido.size(); i++){
                    java.util.HashMap map=(java.util.HashMap)consumido.get(i);
                    t_m = Double.parseDouble(map.get("monto_consumible").toString());
                }
                Double iva_m = t_m*(iva /100);
                iva_m = t_m+iva_m;
                if(((Number)t_mecanica.getValue()).doubleValue()>=iva_m){
                    ord1.setPMecanica(((Number)t_mecanica.getValue()).doubleValue());
                    ord1.setDMecanica(false);
                }else{
                    t_mecanica.setValue(ord1.getPMecanica());
                    JOptionPane.showMessageDialog(this, "El Monto Mecanica es menor al Consumido");
                }
            }
            //SUSPENSION
            if(cb_suspension.isSelected()==true){
                ord1.setDSuspension(true);
                t_suspension.setValue(ord1.getPSuspension());
            }else{
                Query query2 = session.createSQLQuery("select (select if(sum(cantidad*valor) is null, 0, sum(cantidad*valor)) from movimiento left join almacen on movimiento.id_almacen=almacen.id_almacen left join orden on almacen.id_orden=orden.id_orden where orden.id_orden="+ord.getIdOrden()+" and almacen.operacion=5 and almacen.tipo_movimiento=2 and almacen.especialidad='s') - (select if(sum(cantidad*valor) is null, 0, sum(cantidad*valor)) from movimiento left join almacen on movimiento.id_almacen=almacen.id_almacen left join orden on almacen.id_orden=orden.id_orden where orden.id_orden="+ord1.getIdOrden()+" and almacen.operacion=5 and almacen.tipo_movimiento=1 and almacen.especialidad='s')as monto_consumible");
                query2.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
                ArrayList consumido = (ArrayList)query2.list();
                for(int i =0; i<consumido.size(); i++){
                    java.util.HashMap map=(java.util.HashMap)consumido.get(i);
                    t_s = Double.parseDouble(map.get("monto_consumible").toString());
                }
                Double iva_s = t_s*(iva /100);
                iva_s = t_s+iva_s;
                if(((Number)t_suspension.getValue()).doubleValue()>=iva_s){
                    ord1.setPSuspension(((Number)t_suspension.getValue()).doubleValue());
                    ord1.setDSuspension(false);
                }else{
                    t_suspension.setValue(ord1.getPSuspension());
                    JOptionPane.showMessageDialog(this, "El Monto Suspension es menor al Consumido");
                }
            }
            //ELECTRICO
            if(cb_electrico.isSelected()==true){
                ord1.setDElectrico(true);
                t_electrico.setValue(ord1.getPElectrico());
            }else{
                Query query2 = session.createSQLQuery("select (select if(sum(cantidad*valor) is null, 0, sum(cantidad*valor)) from movimiento left join almacen on movimiento.id_almacen=almacen.id_almacen left join orden on almacen.id_orden=orden.id_orden where orden.id_orden="+ord.getIdOrden()+" and almacen.operacion=5 and almacen.tipo_movimiento=2 and almacen.especialidad='e') - (select if(sum(cantidad*valor) is null, 0, sum(cantidad*valor)) from movimiento left join almacen on movimiento.id_almacen=almacen.id_almacen left join orden on almacen.id_orden=orden.id_orden where orden.id_orden="+ord1.getIdOrden()+" and almacen.operacion=5 and almacen.tipo_movimiento=1 and almacen.especialidad='e')as monto_consumible");
                query2.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
                ArrayList consumido = (ArrayList)query2.list();
                for(int i =0; i<consumido.size(); i++){
                    java.util.HashMap map=(java.util.HashMap)consumido.get(i);
                    t_e = Double.parseDouble(map.get("monto_consumible").toString());
                }
                Double iva_e = t_e*(iva /100);
                iva_e = t_e+iva_e;
                if(((Number)t_electrico.getValue()).doubleValue()>=iva_e){
                    ord1.setPElectrico(((Number)t_electrico.getValue()).doubleValue());
                    ord1.setDElectrico(false);
                }else{
                    t_electrico.setValue(ord1.getPElectrico());
                    JOptionPane.showMessageDialog(this, "El Monto Electrico es menor al Consumido");
                }
            }
            //PINTURA
            if(cb_pintura.isSelected()==true){
                ord1.setDPintura(true);
                t_pintura.setValue(ord1.getPPintura());
            }else{
                Query query2 = session.createSQLQuery("select (select if(sum(cantidad*valor) is null, 0, sum(cantidad*valor)) from movimiento left join almacen on movimiento.id_almacen=almacen.id_almacen left join orden on almacen.id_orden=orden.id_orden where orden.id_orden="+ord.getIdOrden()+" and almacen.operacion=5 and almacen.tipo_movimiento=2 and almacen.especialidad='p') - (select if(sum(cantidad*valor) is null, 0, sum(cantidad*valor)) from movimiento left join almacen on movimiento.id_almacen=almacen.id_almacen left join orden on almacen.id_orden=orden.id_orden where orden.id_orden="+ord1.getIdOrden()+" and almacen.operacion=5 and almacen.tipo_movimiento=1 and almacen.especialidad='p')as monto_consumible");
                query2.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
                ArrayList consumido = (ArrayList)query2.list();
                for(int i =0; i<consumido.size(); i++){
                    java.util.HashMap map=(java.util.HashMap)consumido.get(i);
                    t_p = Double.parseDouble(map.get("monto_consumible").toString());
                }
                Double iva_p = t_p*(iva /100);
                iva_p = t_p+iva_p;
                if(((Number)t_pintura.getValue()).doubleValue()>=iva_p){
                    ord1.setPPintura(((Number)t_pintura.getValue()).doubleValue());
                    ord1.setDPintura(false);
                }else{
                    t_pintura.setValue(ord1.getPPintura());
                    JOptionPane.showMessageDialog(this, "El Monto Electrico es Menor al Consumido");
                }
            }
            
            session.update(ord1);
            session.getTransaction().commit();
            
            t_total_consumible.setText("0");
            t_total_consumible.setValue(0.00);

            if(ord1.getDHojalateria()==false){
                total += ord1.getPHojalateria().doubleValue();
            }

            if(ord1.getDMecanica()==false){
                total += ord1.getPMecanica().doubleValue();
            }

            if(ord1.getDSuspension()==false){
                total += ord1.getPSuspension().doubleValue();
            }

            if(ord1.getDElectrico()==false){
                total += ord1.getPElectrico().doubleValue();
            }

            if(ord1.getDPintura()==false){
                total += ord1.getPPintura().doubleValue();
            }
            t_total_consumible.setValue(total);
            
        }catch(Exception e)
        {
            session.beginTransaction().rollback();
            e.printStackTrace();
        }
        if(session!=null)
            if(session.isOpen())
            {
                session.flush();
                session.clear();
                session.close();
            }
    }//GEN-LAST:event_jButton7ActionPerformed

    private void cb_pinturaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cb_pinturaActionPerformed
        // TODO add your handling code here:
        if(cb_pintura.isSelected()){
            t_pintura.setEnabled(false);
        }else{
            t_pintura.setEnabled(true);
        }
    }//GEN-LAST:event_cb_pinturaActionPerformed

    private void cb_electricoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cb_electricoActionPerformed
        // TODO add your handling code here:
        if(cb_electrico.isSelected()){
            t_electrico.setEnabled(false);
        }else{
            t_electrico.setEnabled(true);
        }
    }//GEN-LAST:event_cb_electricoActionPerformed

    private void cb_suspensionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cb_suspensionActionPerformed
        // TODO add your handling code here:
        if(cb_suspension.isSelected()){
            t_suspension.setEnabled(false);
        }else{
            t_suspension.setEnabled(true);
        }
    }//GEN-LAST:event_cb_suspensionActionPerformed

    private void cb_mecanicaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cb_mecanicaActionPerformed
        // TODO add your handling code here:
        if(cb_mecanica.isSelected()){
            t_mecanica.setEnabled(false);
        }else{
            t_mecanica.setEnabled(true);
        }
    }//GEN-LAST:event_cb_mecanicaActionPerformed

    private void cb_hojalateriaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cb_hojalateriaActionPerformed
        // TODO add your handling code here:
        if(cb_hojalateria.isSelected()==true){
            t_hojalateria.setEnabled(false);
        }else{
            t_hojalateria.setEnabled(true);
        }
    }//GEN-LAST:event_cb_hojalateriaActionPerformed

    private void t_datosKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_datosKeyReleased
        // TODO add your handling code here:
        int code = evt.getKeyCode();
        if(code == KeyEvent.VK_SPACE)
        {
        /*    if(t_datos.getSelectedColumn()==13)
                t_datosMouseClicked(null);
        }
        else
        {*/
            try{
            numeros.requestFocus();
            numeros.setPopupVisible(true);
            }catch(Exception e){}
        }
    }//GEN-LAST:event_t_datosKeyReleased

    private void tipoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tipoMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_tipoMouseClicked

    private void tipoMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tipoMouseReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_tipoMouseReleased

    private void tipoPopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_tipoPopupMenuWillBecomeInvisible
        // TODO add your handling code here:
    }//GEN-LAST:event_tipoPopupMenuWillBecomeInvisible

    private void tipoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tipoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tipoActionPerformed

    private void tipoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tipoFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_tipoFocusGained

    private void tipoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tipoFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_tipoFocusLost

    private void bt1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bt1ActionPerformed
        // TODO add your handling code here:
        Session session = HibernateUtil.getSessionFactory().openSession();
        try
        {
            DecimalFormat formatoPorcentaje = new DecimalFormat("#,##0.00");
            formatoPorcentaje.setMinimumFractionDigits(2);   
            session.beginTransaction().begin();
            BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.WINANSI, BaseFont.NOT_EMBEDDED);
            PDF reporte = new PDF();
            Date fecha = new Date();
            DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyyHH-mm-ss");//YYYY-MM-DD HH:MM:SS
            String valor=dateFormat.format(fecha);
            File folder = new File("reportes/"+ord.getIdOrden());
            folder.mkdirs();
            reporte.Abrir2(PageSize.LETTER, "Cotizacion", "reportes/"+ord.getIdOrden()+"/"+valor+"-ref_cot.pdf");
            Font font = new Font(Font.FontFamily.HELVETICA, 6, Font.BOLD);
            BaseColor contenido=BaseColor.WHITE;
            int centro=Element.ALIGN_CENTER;
            int izquierda=Element.ALIGN_LEFT;
            int derecha=Element.ALIGN_RIGHT;
            int ren=0;
            double dm=0d, cam=0d, min=0d, med=0d, max=0d, pin=0d, tot=0d;
            session.beginTransaction().begin();
            Orden ord = (Orden)session.get(Orden.class, Integer.parseInt(orden));
            Configuracion config=(Configuracion)session.get(Configuracion.class, configuracion);

            float tam[];
            tam=new float[]{400,80,80,80,80};

            PdfPTable tabla=reporte.crearTabla(5, tam, 100, Element.ALIGN_LEFT);

            cabecera3(reporte, bf, tabla, config.getCinta());

            int[] partidas_reporte = t_datos.getSelectedRows();
            for(int i=0; i<partidas_reporte.length; i++)
            {    
                double suma=0d;
                tabla.addCell(reporte.celda(t_datos.getValueAt(partidas_reporte[i], 2).toString(), font, contenido, izquierda, 0,1,Rectangle.RECTANGLE));//descripcion
                tabla.addCell(reporte.celda(""+t_datos.getValueAt(partidas_reporte[i], 9).toString(), font, contenido, derecha, 0,1,Rectangle.RECTANGLE));//cant
                tabla.addCell(reporte.celda(t_datos.getValueAt(partidas_reporte[i], 10).toString(), font, contenido, derecha, 0,1,Rectangle.RECTANGLE));//Med

                Double costo=0.0;
                if(t_datos.getValueAt(partidas_reporte[i], 15)!=null)
                {
                    costo=(Double)t_datos.getValueAt(partidas_reporte[i], 15);
                    tabla.addCell(reporte.celda(formatoPorcentaje.format(costo), font, contenido, derecha, 0,1,Rectangle.RECTANGLE));
                }
                else
                    tabla.addCell(reporte.celda(formatoPorcentaje.format(0.0), font, contenido, derecha, 0,1,Rectangle.RECTANGLE));
                Double precio=costo*(Double)t_datos.getValueAt(partidas_reporte[i], 9);
                tot+=precio;
                System.out.println("Precio:"+precio);
                tabla.addCell(reporte.celda(formatoPorcentaje.format(precio), font, contenido, derecha, 0,1,Rectangle.RECTANGLE));
            }
            
            tabla.addCell(reporte.celda("MANO DE OBRA", font, contenido, izquierda, 0,1,Rectangle.RECTANGLE));
            tabla.addCell(reporte.celda("1", font, contenido, derecha, 0,1,Rectangle.RECTANGLE));
            tabla.addCell(reporte.celda("N/A", font, contenido, derecha, 0,1,Rectangle.RECTANGLE));
            tabla.addCell(reporte.celda("$"+t_mo_directa.getText(), font, contenido, derecha, 0,1,Rectangle.RECTANGLE));
            tabla.addCell(reporte.celda("$"+t_mo_directa.getText(), font, contenido, derecha, 0,1,Rectangle.RECTANGLE));
            
            double total_mo = ((Number)t_mo_directa.getValue()).doubleValue();
            double sub_tot_cot = total_mo+ tot;
            double iva_tot_cot = sub_tot_cot*0.16;
            double tot_cot = sub_tot_cot + iva_tot_cot;
            
            tabla.addCell(reporte.celda("SUB TOTAL", font, contenido, izquierda, 4,1,Rectangle.RECTANGLE));
            tabla.addCell(reporte.celda("$"+formatoPorcentaje.format(sub_tot_cot), font, contenido, derecha, 0,1,Rectangle.RECTANGLE));
            
            tabla.addCell(reporte.celda("IVA", font, contenido, izquierda, 4,1,Rectangle.RECTANGLE));
            tabla.addCell(reporte.celda("$"+formatoPorcentaje.format(iva_tot_cot), font, contenido, derecha, 0,1,Rectangle.RECTANGLE));
            
            tabla.addCell(reporte.celda("TOTAL", font, contenido, izquierda, 4,1,Rectangle.RECTANGLE));
            tabla.addCell(reporte.celda("$"+formatoPorcentaje.format(tot_cot), font, contenido, derecha, 0,1,Rectangle.RECTANGLE));
            
            tabla.addCell(reporte.celda("", font, contenido, centro, 5,1,Rectangle.TOP));
            tabla.addCell(reporte.celda("Vigencia 15 días apartir de la fecha de emisión", font, contenido, centro, 5,1,Rectangle.NO_BORDER));
            
            tabla.setHeaderRows(1);
            reporte.agregaObjeto(tabla);

            reporte.cerrar();
            reporte.visualizar2("reportes/"+ord.getIdOrden()+"/"+valor+"-ref_cot.pdf");
        }catch(Exception e)
        {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "No se pudo realizar el reporte");
        }
        if(session!=null)
            if(session.isOpen())
            {
                session.flush();
                session.clear();
                session.close();
            }
    }//GEN-LAST:event_bt1ActionPerformed

    public void consulta_consumible(){
        Session session = HibernateUtil.getSessionFactory().openSession();
        try{
            ord=(Orden)session.get(Orden.class, ord.getIdOrden());
            if(ord!=null){
                //HOJALATERIA
                t_hojalateria.setText(""+ord.getPHojalateria());
                t_hojalateria.setValue(ord.getPHojalateria());
                if(ord.getDHojalateria()==true){
                    t_hojalateria.setEnabled(false);
                    cb_hojalateria.setSelected(true);
                }else{
                    t_hojalateria.setEnabled(true);
                    cb_hojalateria.setSelected(false);
                }
                //MECANICA
                t_mecanica.setText(""+ord.getPMecanica());
                t_mecanica.setValue(ord.getPMecanica());
                if(ord.getDMecanica()==true){
                    t_mecanica.setEnabled(false);
                    cb_mecanica.setSelected(true);
                }else{
                    t_mecanica.setEnabled(true);
                    cb_mecanica.setSelected(false);
                }
                //SUSPENSION
                t_suspension.setText(""+ord.getPSuspension());
                t_suspension.setValue(ord.getPSuspension());
                if(ord.getDSuspension()==true){
                    t_suspension.setEnabled(false);
                    cb_suspension.setSelected(true);
                }else{
                    t_suspension.setEnabled(true);
                    cb_suspension.setSelected(false);
                }
                //ELECTRICO
                t_electrico.setText(""+ord.getPElectrico());
                t_electrico.setValue(ord.getPElectrico());
                if(ord.getDElectrico()==true){
                    t_electrico.setEnabled(false);
                    cb_electrico.setSelected(true);
                }else{
                    t_electrico.setEnabled(true);
                    cb_electrico.setSelected(false);
                }
                //PINTURA
                t_pintura.setText(""+ord.getPPintura());
                t_pintura.setValue(ord.getPPintura());
                if(ord.getDPintura()==true){
                    t_pintura.setEnabled(false);
                    cb_pintura.setSelected(true);
                }else{
                    t_pintura.setEnabled(true);
                    cb_pintura.setSelected(false);
                }
            }
        }
        catch(Exception e)
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
    }
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton b_ac;
    private javax.swing.JButton b_busca;
    private javax.swing.JButton b_consumibles;
    private javax.swing.JButton b_enviar;
    private javax.swing.JButton b_exel;
    private javax.swing.JButton b_exel1;
    private javax.swing.JButton b_exel2;
    private javax.swing.JButton b_ok;
    private javax.swing.JButton b_pdf;
    private javax.swing.JButton bt;
    private javax.swing.JButton bt1;
    private javax.swing.JCheckBox cb_electrico;
    private javax.swing.JCheckBox cb_hojalateria;
    private javax.swing.JCheckBox cb_mecanica;
    private javax.swing.JComboBox cb_partidas;
    private javax.swing.JCheckBox cb_pintura;
    private javax.swing.JComboBox cb_precio;
    private javax.swing.JCheckBox cb_suspension;
    private javax.swing.JComboBox cb_tipo;
    private javax.swing.JTextField instruccion;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel l1;
    private javax.swing.JLabel l2;
    private javax.swing.JLabel l3;
    private javax.swing.JLabel l4;
    private javax.swing.JLabel l5;
    private javax.swing.JLabel l6;
    private javax.swing.JComboBox medida;
    private javax.swing.JComboBox numeros;
    private javax.swing.JRadioButton r_cerrar_cotizacion;
    public javax.swing.JRadioButton r_cerrar_valuacion;
    private javax.swing.JRadioButton r_parte;
    private javax.swing.JScrollPane scroll;
    private javax.swing.JFormattedTextField t_autorizado;
    private javax.swing.JFormattedTextField t_autorizado1;
    private javax.swing.JTextField t_busca;
    private javax.swing.JFormattedTextField t_cantidad;
    private javax.swing.JFormattedTextField t_cia_seguros;
    private javax.swing.JFormattedTextField t_costo_refacciones;
    private javax.swing.JTable t_datos;
    private javax.swing.JFormattedTextField t_deducible;
    private javax.swing.JFormattedTextField t_demerito;
    private javax.swing.JFormattedTextField t_electrico;
    private javax.swing.JFormattedTextField t_hojalateria;
    private javax.swing.JFormattedTextField t_horas;
    private javax.swing.JFormattedTextField t_importe;
    private javax.swing.JFormattedTextField t_mecanica;
    private javax.swing.JFormattedTextField t_mo_directa;
    private javax.swing.JFormattedTextField t_mo_directa1;
    private javax.swing.JFormattedTextField t_num;
    private javax.swing.JTextField t_ok;
    private javax.swing.JFormattedTextField t_pintura;
    private javax.swing.JTextField t_porcentaje;
    private javax.swing.JFormattedTextField t_ref_autorizadas_directo;
    private javax.swing.JFormattedTextField t_ref_presupuesto;
    private javax.swing.JFormattedTextField t_suspension;
    private javax.swing.JFormattedTextField t_total_consumible;
    private javax.swing.JComboBox tipo;
    private javax.swing.JDialog ventanaConsumible;
    private javax.swing.JDialog ventanaReportes;
    // End of variables declaration//GEN-END:variables

    public void formatoTabla()
    {
        TableCellRenderer textNormal = new DefaultTableHeaderCellRenderer();        
        TableCellRenderer headerRenderer = new VerticalTableHeaderCellRenderer();
        Enumeration columns = t_datos.getColumnModel().getColumns();
        
        for(int x=0; x<t_datos.getColumnModel().getColumnCount(); x++)
        {
            if((x>3 && x<7) || (x>20 && x<25) || (x>28 && x<38))
                t_datos.getColumnModel().getColumn(x).setHeaderRenderer(headerRenderer);
            else
                t_datos.getColumnModel().getColumn(x).setHeaderRenderer(textNormal);
        } 
        tabla_tamaños();
        t_datos.setShowVerticalLines(true);
        t_datos.setShowHorizontalLines(true);
        
        t_datos.setDefaultRenderer(String.class, formato); 
        t_datos.setDefaultRenderer(Double.class, formato); 
        t_datos.setDefaultRenderer(Integer.class, formato);
        t_datos.setDefaultRenderer(Boolean.class, formato);
        
    }

    private void buscaCuentas(int x, int y)
    {
        double imp=0.0;
        double suma=0.00;
        if(orden!=null)
        {
            Session session = HibernateUtil.getSessionFactory().openSession();
            try
            {
                session.beginTransaction().begin();
                ord = (Orden)session.get(Orden.class, Integer.parseInt(orden));
                boolean redondeo = ord.getCompania().isRedondeo();
                
                if(ord.getRValuacionCierre()!=null)
                    this.r_cerrar_valuacion.setSelected(true);
                else
                    this.r_cerrar_valuacion.setSelected(false);
                
                if(ord.getRCotizaCierre()!=null)
                    this.r_cerrar_cotizacion.setSelected(true);
                else
                    this.r_cerrar_cotizacion.setSelected(false);
                if(ord.getDeducible()!=null)
                {
                    this.t_deducible.setText(""+ord.getDeducible());
                    this.t_deducible.setValue(ord.getDeducible());
                }
                else
                {
                    this.t_deducible.setText("0.00");
                    this.t_deducible.setValue(0);
                }
                if(ord.getPHojalateria()!=null)
                    
                    if(ord.getDHojalateria()==false){
                        suma +=ord.getPHojalateria().doubleValue();
                    }
                    
                    if(ord.getDMecanica()==false){
                        suma +=ord.getPMecanica().doubleValue();
                    }
                    
                    if(ord.getDSuspension()==false){
                        suma += ord.getPSuspension().doubleValue();
                    }
                    
                    if(ord.getDElectrico()==false){
                        suma += ord.getPElectrico().doubleValue();
                    }
                    
                    if(ord.getDPintura()==false){
                        suma +=ord.getPPintura().doubleValue();
                    }
                
                    t_total_consumible.setValue(suma);
                
                if(ord.getDemerito()!=null)
                {
                    this.t_demerito.setText(""+ord.getDemerito());
                    t_demerito.setValue(ord.getDemerito());
                }
                else
                {
                    this.t_demerito.setText("0.00");
                    this.t_demerito.setValue(0);
                }
                
                this.t_ref_autorizadas_directo.setValue(ord.getRefAutorizadas());
                this.t_ref_presupuesto.setValue(ord.getRefPresupuesto());
                this.t_mo_directa.setValue(ord.getMoDirecta());
                this.t_mo_directa1.setValue(ord.getMoPresupuestada());
                
                imp=ord.getCompania().getImporteHora();
                //Partida[] cuentas=new Partida[0];
                Query query;
                ArrayList lista=new ArrayList();
                        
                if(menu==4)
                {
                    query=session.createSQLQuery("select id_evaluacion, sub_partida, especialidad.descripcion, catalogo.nombre, dm, rep_min, rep_med, rep_max, cam, pint, cant, med, if(Instruccion is null, '', Instruccion)as dato,  \n" +
"if(id_Parte is null, '', id_Parte) as parte, c_u, porcentaje, Precio_cia_seguros_c_u, Cantidad_aut, Precio_aut_c_u, (Cantidad_aut*Precio_aut_c_u) as total, \n" +
"autorizado, ref_coti, ref_comp, surte_almacen, tot, so, pd, tipo_pieza as orig, id_pedido, if(int_desm=-1, 'false', 'true') as i_desm, if(Int_camb=-1, 'false', 'true') as i_cam, if(Int_rep_min=-1, 'false', 'true') as i_r_min, \n" +
"if(Int_rep_med=-1, 'false', 'true') as i_r_med, if(Int_rep_max=-1, 'false', 'true') as i_r_max, if(int_pin_min=-1, 'false', 'true') as i_p_min, if(int_pin_med=-1, 'false', 'true') as i_p_med, \n" +
"if(int_pin_max=-1, 'false', 'true')as i_p_max, \n" +
"tipo, if(enlazada is null, '', enlazada) as ext, if(facturado is null, 'false', if(facturado=true, 'true', 'false'))as fac \n" +
"from partida inner join catalogo on partida.id_catalogo=catalogo.id_catalogo \n" +
"inner join especialidad on catalogo.id_grupo_mecanico=especialidad.id_grupo_mecanico where id_orden="+ord.getIdOrden()+" and autorizado_valuacion=true order by id_evaluacion, sub_partida asc;");
                    query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
                    lista=(ArrayList)query.list();
                }
                else
                {
                    query=session.createSQLQuery("select id_evaluacion, sub_partida, especialidad.descripcion, catalogo.nombre, dm, rep_min, rep_med, rep_max, cam, pint, cant, med, if(Instruccion is null, '', Instruccion)as dato,  \n" +
"if(id_Parte is null, '', id_Parte) as parte, c_u, porcentaje, Precio_cia_seguros_c_u, Cantidad_aut, Precio_aut_c_u, (Cantidad_aut*Precio_aut_c_u) as total, \n" +
"autorizado, ref_coti, ref_comp, surte_almacen,tot, so, pd, tipo_pieza as orig, id_pedido, if(int_desm=-1, 'false', 'true') as i_desm, if(Int_camb=-1, 'false', 'true') as i_cam, if(Int_rep_min=-1, 'false', 'true') as i_r_min, \n" +
"if(Int_rep_med=-1, 'false', 'true') as i_r_med, if(Int_rep_max=-1, 'false', 'true') as i_r_max, if(int_pin_min=-1, 'false', 'true') as i_p_min, if(int_pin_med=-1, 'false', 'true') as i_p_med, \n" +
"if(int_pin_max=-1, 'false', 'true')as i_p_max, \n" +
"tipo, if(enlazada is null, '', enlazada) as ext, if(facturado is null, 'false', if(facturado=true, 'true', 'false'))as fac \n" +
"from partida inner join catalogo on partida.id_catalogo=catalogo.id_catalogo \n" +
"inner join especialidad on catalogo.id_grupo_mecanico=especialidad.id_grupo_mecanico where id_orden="+ord.getIdOrden()+" and autorizado_valuacion=true and ref_coti=true  order by id_evaluacion, sub_partida asc;");
                    query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
                    lista=(ArrayList)query.list();
                }
                
                Partida[] enlazadas=new Partida[0];
                if(menu==4)
                    enlazadas = (Partida[])session.createCriteria(Partida.class).add(Restrictions.eq("ordenByEnlazada.idOrden", ord.getIdOrden())).add(Restrictions.eq("autorizadoValuacion", true)).addOrder(Order.asc("idEvaluacion")).addOrder(Order.asc("subPartida")).list().toArray(new Partida[0]);
                //else
                    //enlazadas = (Partida[])session.createCriteria(Partida.class).add(Restrictions.eq("ordenByEnlazada.idOrden", ord.getIdOrden())).add(Restrictions.eq("refCoti", true)).add(Restrictions.eq("autorizadoValuacion", true)).list().toArray(new Partida[0]);
                
                if(lista.size()>0 || enlazadas.length>0)
                {
                    model=new MyModel(lista.size()+enlazadas.length, columnas, this.types);
                    t_datos.setModel(model);
                    formatoTabla();
                    for(int i=0; i<lista.size(); i++)
                    {
                        java.util.HashMap map=(java.util.HashMap)lista.get(i);
                        model.setValueAt(map.get("id_evaluacion"), i, 0);
                        model.setCeldaEditable(i, 0, false);
                        model.setValueAt(map.get("sub_partida"), i, 1);
                        model.setCeldaEditable(i, 1, false);
                        model.setValueAt(map.get("nombre"), i, 2);
                        model.setCeldaEditable(i, 2, false);
                        
                        double dm=(double)map.get("dm");
                        if(dm==-1)
                        {
                            model.setValueAt(null, i, 3);
                            model.setCeldaEditable(i, 3, false);
                        }
                        else
                        {
                            model.setValueAt(dm, i, 3);
                            model.setCeldaEditable(i, 3, this.edita_columnas[0]);
                        }
                        
                        double repMin=(double)map.get("rep_min");
                        if(repMin==-1)
                        {
                            model.setCeldaEditable(i, 4, false);
                            model.setValueAt(null, i, 4);
                        }
                        else
                        {
                            model.setValueAt(repMin, i, 4);
                            model.setCeldaEditable(i, 4, this.edita_columnas[0]);
                        }
                        
                        double repMed=(double)map.get("rep_med");
                        if(repMed==-1)
                        {
                            model.setCeldaEditable(i, 5, false);
                            model.setValueAt(null, i, 5);
                        }
                        else
                        {
                            model.setValueAt(repMed, i, 5);
                            model.setCeldaEditable(i, 5, this.edita_columnas[0]);
                        }
                        
                        double repMax=(double)map.get("rep_max");
                        if(repMax==-1)
                        {
                            model.setValueAt(null, i, 6);
                            model.setCeldaEditable(i, 6, false);
                        }
                        else
                        {
                            model.setValueAt(repMax, i, 6);
                            model.setCeldaEditable(i, 6, this.edita_columnas[0]);
                        }
                        double cam=(double)map.get("cam");
                        if(cam==-1)
                        {
                            model.setValueAt(null, i, 7);
                            model.setCeldaEditable(i, 7, false);
                        }
                        else
                        {
                            model.setValueAt(cam, i, 7);
                            model.setCeldaEditable(i, 7, this.edita_columnas[0]);
                        }
                        double pint=(double)map.get("pint");
                        if(pint==-1)
                        {
                            model.setValueAt(null, i, 8);
                            model.setCeldaEditable(i, 8, false);
                        }
                        else
                        {
                            model.setValueAt(pint, i, 8);
                            model.setCeldaEditable(i, 8, this.edita_columnas[0]);
                        }
                        
                        model.setValueAt(map.get("cant"), i, 9);
                        model.setCeldaEditable(i, 9, false);
                        model.setValueAt(map.get("med"), i, 10);
                        model.setCeldaEditable(i, 10, false);
                        
                        model.setValueAt(map.get("dato"), i, 11);
                        model.setCeldaEditable(i, 11, false);
                        
                        if(map.get("parte")!=null)
                            model.setValueAt(map.get("parte"), i, 12);
                        else
                            model.setValueAt("1", i, 12);
                        
                        
                        model.setValueAt(map.get("c_u"), i, 13);
                        model.setCeldaEditable(i, 13, this.edita_columnas[1]);
                        
                        double porcentaje=(double)map.get("porcentaje");
                        model.setValueAt(porcentaje, i, 14);
                        model.setCeldaEditable(i, 14, this.edita_columnas[2]);
                        double cu=(double)map.get("c_u");
                        if(porcentaje==0.0)
                        {
                            model.setValueAt(cu, i, 15);
                        }
                        else
                        {
                            double valor1=0.0d;
                            if(redondeo==false)
                                valor1 = 0.0+(cu/(1-(porcentaje*0.01)));
                            else
                                valor1 = 0.0+Math.round(cu/(1-(porcentaje*0.01)));
                            
                            model.setValueAt(new BigDecimal(valor1).setScale(2, RoundingMode.HALF_UP).doubleValue(), i, 15);
                        }
                        model.setCeldaEditable(i, 15, false);
                        
                        model.setValueAt(map.get("Cantidad_aut"), i, 16);
                        model.setCeldaEditable(i, 16, this.edita_columnas[3]);
                        
                        model.setValueAt(map.get("Precio_aut_c_u"), i, 17);
                        model.setCeldaEditable(i, 17, this.edita_columnas[4]);
                        
                        model.setValueAt(map.get("total"), i, 18);
                        model.setCeldaEditable(i, 18, false);
                        
                        model.setValueAt(map.get("fac").toString().contentEquals("true"), i, 19);//vales
                        model.setCeldaEditable(i, 19, false);
                        
                        model.setValueAt(map.get("autorizado"), i, 20);
                        model.setCeldaEditable(i, 20, this.edita_columnas[5]);
                        
                        double horas=0.0;
                        for(int r=4; r<10; r++)
                        {
                            if(model.getValueAt(i, r)!=null)
                            {
                                double val=Double.parseDouble(model.getValueAt(i, r).toString());
                                horas+=val;
                            }
                        }
                        horas=horas*cu;
                        model.setValueAt(horas, i, 21);
                        model.setCeldaEditable(i, 21, false);
                        
                        model.setValueAt(map.get("ref_coti"), i, 22);
                        model.setCeldaEditable(i, 22, this.edita_columnas[6]);
                        
                        model.setValueAt(map.get("ref_comp"), i, 23);
                        model.setCeldaEditable(i, 23, this.edita_columnas[7]);
                        
                        model.setValueAt(map.get("surte_almacen"), i, 24);//s.almacen
                        model.setCeldaEditable(i, 24, this.edita_columnas[9]);
                        
                        model.setValueAt(map.get("so"), i, 25);
                        model.setCeldaEditable(i, 25, this.edita_columnas[8]);
                        model.setValueAt(map.get("pd"), i, 26);
                        model.setCeldaEditable(i, 26, this.edita_columnas[8]);
                        
                        
                        model.setValueAt(map.get("orig"), i, 27);
                        model.setCeldaEditable(i, 27, this.edita_columnas[10]);
                        
                        //recorree
                        model.setValueAt(map.get("id_pedido"), i, 28);//pedido
                        model.setCeldaEditable(i, 28, false);
                            
                        model.setValueAt(map.get("i_desm").toString().contentEquals("true"), i, 29);
                        model.setCeldaEditable(i, 29, false);
                        model.setValueAt(map.get("i_cam").toString().contentEquals("true"), i, 30);
                        model.setCeldaEditable(i, 30, false);
                        model.setValueAt(map.get("i_r_min").toString().contentEquals("true"), i, 31);
                        model.setCeldaEditable(i, 31, false);
                        model.setValueAt(map.get("i_r_med").toString().contentEquals("true"), i, 31);
                        model.setCeldaEditable(i, 32, false);
                        model.setValueAt(map.get("i_r_max").toString().contentEquals("true"), i, 33);
                        model.setCeldaEditable(i, 33, false);
                        model.setValueAt(map.get("i_p_min").toString().contentEquals("true"), i, 34);
                        model.setCeldaEditable(i, 34, false);
                        model.setValueAt(map.get("i_p_med").toString().contentEquals("true"), i, 35);
                        model.setCeldaEditable(i, 35, false);
                        model.setValueAt(map.get("i_p_max").toString().contentEquals("true"), i, 36);
                        model.setCeldaEditable(i, 36, false);
                            
                        
                        model.setValueAt(map.get("tipo"), i, 37);
                        model.setCeldaEditable(i, 37, false);
                        
                        model.setValueAt(map.get("ext"), i, 38);
                        model.setCeldaEditable(i, 38, false);
                    }
                    
                    //**********cargamos las enlazadas
                    //lista.size()+enlazadas.length
                    for(int i=0; i<enlazadas.length; i++)
                    {
                        model.setValueAt(enlazadas[i].getIdEvaluacion(), i+lista.size(), 0);
                        model.setCeldaEditable(i+lista.size(), 0, false);
                        model.setValueAt(enlazadas[i].getSubPartida(), i+lista.size(), 1);
                        model.setCeldaEditable(i+lista.size(), 1, false);
                        model.setValueAt(enlazadas[i].getCatalogo().getNombre(), i+lista.size(), 2);
                        model.setCeldaEditable(i+lista.size(), 2, false);
                        
                        if(enlazadas[i].getDm()==-1)
                        {
                            model.setValueAt(null, i+lista.size(), 3);
                            model.setCeldaEditable(i+lista.size(), 3, false);
                        }
                        else
                        {
                            model.setValueAt(enlazadas[i].getDm(), i+lista.size(), 3);
                            model.setCeldaEditable(i+lista.size(), 3, false);
                        }
                        
                        
                        if(enlazadas[i].getRepMin()==-1)
                        {
                            model.setCeldaEditable(i+lista.size(), 4, false);
                            model.setValueAt(null, i+lista.size(), 4);
                        }
                        else
                        {
                            model.setValueAt(enlazadas[i].getRepMin(), i+lista.size(), 4);
                            model.setCeldaEditable(i+lista.size(), 4, false);
                        }
                        
                        if(enlazadas[i].getRepMed()==-1)
                        {
                            model.setCeldaEditable(i+lista.size(), 5, false);
                            model.setValueAt(null, i+lista.size(), 5);
                        }
                        else
                        {
                            model.setValueAt(enlazadas[i].getRepMed(), i+lista.size(), 5);
                            model.setCeldaEditable(i+lista.size(), 5, false);
                        }
                        
                        if(enlazadas[i].getRepMax()==-1)
                        {
                            model.setValueAt(null, i+lista.size(), 6);
                            model.setCeldaEditable(i+lista.size(), 6, false);
                        }
                        else
                        {
                            model.setValueAt(enlazadas[i].getRepMax(), i+lista.size(), 6);
                            model.setCeldaEditable(i+lista.size(), 6, false);
                        }
                        
                        if(enlazadas[i].getCam()==-1)
                        {
                            model.setValueAt(null, i+lista.size(), 7);
                            model.setCeldaEditable(i+lista.size(), 7, false);
                        }
                        else
                        {
                            model.setValueAt(enlazadas[i].getCam(), i+lista.size(), 7);
                            model.setCeldaEditable(i+lista.size(), 7, false);
                        }
                        
                        if(enlazadas[i].getPint()==-1)
                        {
                            model.setValueAt(null, i+lista.size(), 8);
                            model.setCeldaEditable(i+lista.size(), 8, false);
                        }
                        else
                        {
                            model.setValueAt(enlazadas[i].getPint(), i+lista.size(), 8);
                            model.setCeldaEditable(i+lista.size(), 8, false);
                        }
                        
                        model.setValueAt(enlazadas[i].getCant(), i+lista.size(), 9);
                        model.setCeldaEditable(i+lista.size(), 9, false);
                        model.setValueAt(enlazadas[i].getMed(), i+lista.size(), 10);
                        model.setCeldaEditable(i+lista.size(), 10, false);
                        
                        //model.setValueAt(enlazadas[i].getCatalogo().getIdCatalogo(), i+lista.size(), 12);
                        //model.setCeldaEditable(i+lista.size(), 12, false);
                        if(enlazadas[i].getInstruccion()!=null)
                            model.setValueAt(enlazadas[i].getInstruccion(), i+lista.size(), 11);
                        else
                            model.setValueAt("", i+lista.size(), 11);
                        model.setCeldaEditable(i+lista.size(), 11, false);
                        
                        try{
                        model.setValueAt(enlazadas[i].getEjemplar().getIdParte(), i+lista.size(), 12);
                        }catch(Exception e){model.setValueAt("", i+lista.size(), 12);}
                        
                        model.setValueAt(enlazadas[i].getCU(), i+lista.size(), 13);
                        model.setCeldaEditable(i+lista.size(), 13, false);
                        
                        model.setValueAt(enlazadas[i].getPorcentaje(), i+lista.size(), 14);
                        model.setCeldaEditable(i+lista.size(), 14, false);
                        if(enlazadas[i].getPorcentaje()==0.0)
                        {
                            model.setValueAt(enlazadas[i].getCU(), i+lista.size(), 15);
                        }
                        else
                        {
                            model.setValueAt(0.0+Math.round(enlazadas[i].getCU()/(1-(enlazadas[i].getPorcentaje()*0.01))), i+lista.size(), 15);
                        }
                        model.setCeldaEditable(i+lista.size(), 15, false);
                        
                        model.setValueAt(enlazadas[i].getCantidadAut(), i+lista.size(), 16);
                        model.setCeldaEditable(i+lista.size(), 16, false);
                        
                        model.setValueAt(enlazadas[i].getPrecioAutCU(), i+lista.size(), 17);
                        model.setCeldaEditable(i+lista.size(), 17, false);
                        
                        model.setValueAt((enlazadas[i].getCantidadAut()*enlazadas[i].getPrecioAutCU()), i+lista.size(), 18);
                        model.setCeldaEditable(i+lista.size(), 18, false);
                        
                        model.setValueAt(enlazadas[i].getFacturado(), i+lista.size(), 19);//vales
                        model.setCeldaEditable(i+lista.size(), 19, false);
                        
                        model.setValueAt(enlazadas[i].isAutorizado(), i+lista.size(), 20);
                        model.setCeldaEditable(i+lista.size(), 20, false);
                        
                        double horas=0.0;
                        for(int r=4; r<10; r++)
                        {
                            if(model.getValueAt(i+lista.size(), r)!=null)
                            {
                                double val=Double.parseDouble(model.getValueAt(i+lista.size(), r).toString());
                                horas+=val;
                            }
                        }
                        model.setValueAt(horas, i+lista.size(), 21);
                        model.setCeldaEditable(i+lista.size(), 21, false);
                        
                        model.setValueAt(enlazadas[i].isRefCoti(), i+lista.size(), 22);
                        model.setCeldaEditable(i+lista.size(), 22, false);
                        
                        model.setValueAt(enlazadas[i].isRefComp(), i+lista.size(), 23);
                        model.setCeldaEditable(i+lista.size(), 23, false);
                        
                        model.setValueAt(enlazadas[i].isSurteAlmacen(), i+lista.size(), 24);
                        model.setCeldaEditable(i+lista.size(), 24, false);
                        
                        model.setValueAt(enlazadas[i].isSo(), i+lista.size(), 25);
                        model.setCeldaEditable(i+lista.size(), 25, false);
                        model.setValueAt(enlazadas[i].isPd(), i+lista.size(), 26);
                        model.setCeldaEditable(i+lista.size(), 26, false);
                        
                        if(enlazadas[i].isOri()==true)
                            model.setValueAt("ORI", i+lista.size(), 29);
                        if(enlazadas[i].isNal()==true)
                            model.setValueAt("NAL", i+lista.size(), 29);
                        if(enlazadas[i].isDesm()==true)
                            model.setValueAt("DES", i+lista.size(), 29);
                        if(enlazadas[i].isPd()==true)
                            model.setValueAt("RECON", i, 29);
                        model.setCeldaEditable(i+lista.size(), 29, false);
                        
                        
                        if(enlazadas[i].getPedido()!=null)
                            model.setValueAt(enlazadas[i].getPedido().getProveedorByIdProveedor().getIdProveedor(), i+lista.size(), 30);//pedido
                        else
                            model.setValueAt("", i+lista.size(), 30);//pedido
                        model.setCeldaEditable(i+lista.size(), 30, false);
                        
                            if(enlazadas[i].getIntDesm()==-1)
                                model.setValueAt(false, i+lista.size(), 31);
                            else
                                model.setValueAt(true, i+lista.size(), 31);
                            model.setCeldaEditable(i+lista.size(), 31, false);

                            if(enlazadas[i].getIntCamb()==-1)
                                model.setValueAt(false, i+lista.size(), 32);
                            else
                                model.setValueAt(true, i+lista.size(), 32);
                            model.setCeldaEditable(i+lista.size(), 32, false);

                            if(enlazadas[i].getIntRepMin()==-1)
                                model.setValueAt(false, i+lista.size(), 33);
                            else
                                model.setValueAt(true, i+lista.size(), 33);
                            model.setCeldaEditable(i+lista.size(), 33, false);

                            if(enlazadas[i].getIntRepMed()==-1)
                                model.setValueAt(false, i+lista.size(), 34);
                            else
                                model.setValueAt(true, i+lista.size(), 34);
                            model.setCeldaEditable(i+lista.size(), 34, false);

                            if(enlazadas[i].getIntRepMax()==-1)
                                model.setValueAt(false, i+lista.size(), 35);
                            else
                                model.setValueAt(true, i+lista.size(), 35);
                            model.setCeldaEditable(i+lista.size(), 35, false);

                            if(enlazadas[i].getIntPinMin()==-1)
                                model.setValueAt(false, i+lista.size(), 36);
                            else
                                model.setValueAt(true, i+lista.size(), 36);
                            model.setCeldaEditable(i+lista.size(), 36, false);

                            if(enlazadas[i].getIntPinMed()==-1)
                                model.setValueAt(false, i+lista.size(), 37);
                            else
                                model.setValueAt(true, i+lista.size(), 37);
                            model.setCeldaEditable(i+lista.size(), 37, false);

                            if(enlazadas[i].getIntPinMax()==-1)
                                model.setValueAt(false, i+lista.size(), 38);
                            else
                                model.setValueAt(true, i+lista.size(), 38);
                            model.setCeldaEditable(i+lista.size(), 38, false);
                        
                        model.setValueAt("e", i+lista.size(), 39);
                        model.setCeldaEditable(i+lista.size(), 39, false);
                        
                        if(enlazadas[i].getOrdenByEnlazada()!=null)
                        {
                            if(enlazadas[i].getOrdenByEnlazada().getIdOrden()==ord.getIdOrden())
                                model.setValueAt(enlazadas[i].getOrdenByIdOrden().getIdOrden(), i+lista.size(), 40);
                            else
                                model.setValueAt(enlazadas[i].getOrdenByEnlazada().getIdOrden(), i+lista.size(), 40);
                        }
                        else
                            model.setValueAt("", i, 40);
                        model.setCeldaEditable(i+lista.size(), 40, false);
                    }
                }
                else
                {
                    model=new MyModel(0, columnas, this.types);
                    t_datos.setModel(model);
                    formatoTabla();
                }
                //session.beginTransaction().rollback();
            }catch(Exception e)
            {
                e.printStackTrace();
            }
            if(session!=null)
                if(session.isOpen()==true)
                {
                    session.beginTransaction().commit();
                    session.flush();
                    session.clear();
                    session.close();
                }
        }
        else
        {
            model=new MyModel(0, columnas, this.types);
            t_datos.setModel(model);
            formatoTabla();
        }
        sumaTotales(imp);
        verEstado();
        if(x>=0 && y>=0)
        {
            t_datos.setColumnSelectionInterval(y, y);
            t_datos.setRowSelectionInterval(x, x);
        }
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        // See if this is a valid table selection
        if(e.getSource() == t_datos.getSelectionModel() && e.getFirstIndex() >= 0 && this.r_cerrar_valuacion.isSelected()==false)
        {
            TableModel model = (TableModel)t_datos.getModel();
            Double valor = (Double)model.getValueAt(t_datos.getSelectedRow(),t_datos.getSelectedColumn() );
            if(valor==0)
                if(t_datos.getSelectedColumn()==17)
                {
                    t_datos.setValueAt(t_datos.getValueAt(t_datos.getSelectedRow(), 15), t_datos.getSelectedRow(),t_datos.getSelectedColumn());
                }
        }
    }

public class MyModel extends DefaultTableModel
{
    Class[] types;
    int ren=0;
    int col=0;
    private boolean[][] celdaEditable;
    double imp=0.0;
    
    public MyModel(int renglones, String columnas[], Class[] tipo)
    {
        types=tipo;
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
                        Vector vector = (Vector) this.dataVector.elementAt(row);
                        Object celda = ((Vector)this.dataVector.elementAt(row)).elementAt(col);
                        switch(col)
                        {
                            case 3://DM
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
                                            if(user.getEditaHoras()==true)
                                            {
                                                if(r_cerrar_valuacion.isSelected()==false)
                                                {
                                                    if(value.toString().compareTo("")!=0 && Double.parseDouble(value.toString())>=0) 
                                                    {       
                                                         Partida part=(Partida) session.createCriteria(Partida.class).add(Restrictions.eq("ordenByIdOrden.idOrden", Integer.parseInt(orden))).add(Restrictions.eq("idEvaluacion", Integer.parseInt(t_datos.getValueAt(t_datos.getSelectedRow(), 0).toString()))).add(Restrictions.eq("subPartida", Integer.parseInt(t_datos.getValueAt(t_datos.getSelectedRow(), 1).toString()))).setMaxResults(1).uniqueResult();
                                                         imp=part.getOrdenByIdOrden().getCompania().getImporteHora();
                                                         if(part!=null)
                                                         {
                                                             part.setDm(BigDecimal.valueOf(Double.parseDouble(value.toString())).setScale(2, RoundingMode.HALF_UP).doubleValue());
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
                                                             //buscaCuentas(row, col);
                                                             sumaTotales(imp);
                                                             verEstado();
                                                         }
                                                         else
                                                         {
                                                             JOptionPane.showMessageDialog(null, "La partida ya no existe");
                                                             buscaCuentas(-1,-1);
                                                         }
                                                    }
                                                    else
                                                    {
                                                        if(Double.parseDouble(value.toString()) < 0) 
                                                            JOptionPane.showMessageDialog(null, "El campo no permite numeros negativos"); 
                                                        buscaCuentas(-1,-1);
                                                    }
                                                }
                                                else
                                                {
                                                    JOptionPane.showMessageDialog(null, "La valuación esta cerrada"); 
                                                }
                                            }
                                            else
                                            {
                                                JOptionPane.showMessageDialog(null, "Acceso denegado"); 
                                            }
                                        }
                                        catch(Exception e)
                                        {
                                            session.getTransaction().rollback();
                                            e.printStackTrace();
                                            JOptionPane.showMessageDialog(null, "Error al actualizar los datos"); 
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

                            case 4://Rep Min
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
                                            if(user.getEditaHoras()==true)
                                            {
                                                if(r_cerrar_valuacion.isSelected()==false)
                                                {
                                                    if(value.toString().compareTo("")!=0 && Double.parseDouble(value.toString())>=0) 
                                                    {
                                                         Partida part=(Partida) session.createCriteria(Partida.class).add(Restrictions.eq("ordenByIdOrden.idOrden", Integer.parseInt(orden))).add(Restrictions.eq("idEvaluacion", Integer.parseInt(t_datos.getValueAt(t_datos.getSelectedRow(), 0).toString()))).add(Restrictions.eq("subPartida", Integer.parseInt(t_datos.getValueAt(t_datos.getSelectedRow(), 1).toString()))).setMaxResults(1).uniqueResult();
                                                         if(part!=null)
                                                         {
                                                             imp=part.getOrdenByIdOrden().getCompania().getImporteHora();
                                                             part.setRepMin(BigDecimal.valueOf(Double.parseDouble(value.toString())).setScale(2, RoundingMode.HALF_UP).doubleValue());
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
                                                             sumaTotales(imp);
                                                             verEstado();
                                                         }
                                                         else
                                                         {
                                                             JOptionPane.showMessageDialog(null, "La partida ya no existe");
                                                             buscaCuentas(-1,-1);
                                                         }
                                                    }
                                                    else
                                                    {
                                                        if(Double.parseDouble(value.toString()) < 0) 
                                                            JOptionPane.showMessageDialog(null, "El campo no permite numeros negativos"); 
                                                    }
                                                }
                                                else
                                                {
                                                    JOptionPane.showMessageDialog(null, "La valuación esta cerrada"); 
                                                }
                                            }
                                            else
                                            {
                                                JOptionPane.showMessageDialog(null, "Acceso denegado"); 
                                            }
                                        }
                                        catch(Exception e)
                                        {
                                            session.getTransaction().rollback();
                                            e.printStackTrace();
                                            JOptionPane.showMessageDialog(null, "Error al actualizar los datos"); 
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
                                            if(user.getEditaHoras()==true)
                                            {
                                                if(r_cerrar_valuacion.isSelected()==false)
                                                {
                                                    if(value.toString().compareTo("")!=0 && Double.parseDouble(value.toString())>=0) 
                                                    {
                                                         Partida part=(Partida) session.createCriteria(Partida.class).add(Restrictions.eq("ordenByIdOrden.idOrden", Integer.parseInt(orden))).add(Restrictions.eq("idEvaluacion", Integer.parseInt(t_datos.getValueAt(t_datos.getSelectedRow(), 0).toString()))).add(Restrictions.eq("subPartida", Integer.parseInt(t_datos.getValueAt(t_datos.getSelectedRow(), 1).toString()))).setMaxResults(1).uniqueResult();
                                                         if(part!=null)
                                                         {
                                                             imp=part.getOrdenByIdOrden().getCompania().getImporteHora();
                                                             part.setRepMed(BigDecimal.valueOf(Double.parseDouble(value.toString())).setScale(2, RoundingMode.HALF_UP).doubleValue());
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
                                                             //buscaCuentas(row, col);
                                                             sumaTotales(imp);
                                                             verEstado();
                                                         }
                                                         else
                                                         {
                                                             JOptionPane.showMessageDialog(null, "La partida ya no existe");
                                                             buscaCuentas(-1,-1);
                                                         }
                                                    }
                                                    else
                                                    {
                                                        if(Double.parseDouble(value.toString()) < 0) 
                                                            JOptionPane.showMessageDialog(null, "El campo no permite numeros negativos"); 
                                                        //buscaCuentas(row, col);
                                                    }
                                                }
                                                else
                                                {
                                                    JOptionPane.showMessageDialog(null, "La valuación esta cerrada"); 
                                                }
                                            }
                                            else
                                            {
                                                JOptionPane.showMessageDialog(null, "Acceso denegado"); 
                                            }
                                        }
                                        catch(Exception e)
                                        {
                                            session.getTransaction().rollback();
                                            e.printStackTrace();
                                            JOptionPane.showMessageDialog(null, "Error al actualizar los datos"); 
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
                                
                            case 6:
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
                                            if(user.getEditaHoras()==true)
                                            {
                                                if(r_cerrar_valuacion.isSelected()==false)
                                                {
                                                    if(value.toString().compareTo("")!=0 && Double.parseDouble(value.toString())>=0) 
                                                    {
                                                         Partida part=(Partida) session.createCriteria(Partida.class).add(Restrictions.eq("ordenByIdOrden.idOrden", Integer.parseInt(orden))).add(Restrictions.eq("idEvaluacion", Integer.parseInt(t_datos.getValueAt(t_datos.getSelectedRow(), 0).toString()))).add(Restrictions.eq("subPartida", Integer.parseInt(t_datos.getValueAt(t_datos.getSelectedRow(), 1).toString()))).setMaxResults(1).uniqueResult();
                                                         if(part!=null)
                                                         {
                                                             imp=part.getOrdenByIdOrden().getCompania().getImporteHora();
                                                             part.setRepMax(BigDecimal.valueOf(Double.parseDouble(value.toString())).setScale(2, RoundingMode.HALF_UP).doubleValue());
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
                                                             //buscaCuentas(row, col);
                                                             sumaTotales(imp);
                                                             verEstado();
                                                         }
                                                         else
                                                         {
                                                             buscaCuentas(-1,-1);
                                                         }
                                                    }
                                                    else
                                                    {
                                                        if(Double.parseDouble(value.toString()) < 0) 
                                                            JOptionPane.showMessageDialog(null, "El campo no permite numeros negativos"); 
                                                        //buscaCuentas(row, col);
                                                    }
                                                }
                                                else
                                                {
                                                    JOptionPane.showMessageDialog(null, "La valuación esta cerrada"); 
                                                }
                                            }
                                            else
                                            {
                                                JOptionPane.showMessageDialog(null, "Acceso denegado"); 
                                            }
                                        }
                                        catch(Exception e)
                                        {
                                            session.getTransaction().rollback();
                                            e.printStackTrace();
                                            JOptionPane.showMessageDialog(null, "Error al actualizar los datos"); 
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
                                
                            case 7:
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
                                            if(user.getEditaHoras()==true)
                                            {
                                                if(r_cerrar_valuacion.isSelected()==false)
                                                {
                                                    if(value.toString().compareTo("")!=0 && Double.parseDouble(value.toString())>=0) 
                                                    {
                                                         Partida part=(Partida) session.createCriteria(Partida.class).add(Restrictions.eq("ordenByIdOrden.idOrden", Integer.parseInt(orden))).add(Restrictions.eq("idEvaluacion", Integer.parseInt(t_datos.getValueAt(t_datos.getSelectedRow(), 0).toString()))).add(Restrictions.eq("subPartida", Integer.parseInt(t_datos.getValueAt(t_datos.getSelectedRow(), 1).toString()))).setMaxResults(1).uniqueResult();
                                                         if(part!=null)
                                                         {
                                                             imp=part.getOrdenByIdOrden().getCompania().getImporteHora();
                                                             part.setCam(BigDecimal.valueOf(Double.parseDouble(value.toString())).setScale(2, RoundingMode.HALF_UP).doubleValue());
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
                                                             //buscaCuentas(row, col);
                                                             sumaTotales(imp);
                                                             verEstado();
                                                         }
                                                         else
                                                         {
                                                             JOptionPane.showMessageDialog(null, "La partida ya no existe");
                                                             buscaCuentas(-1,-1);
                                                         }
                                                    }
                                                    else
                                                    {
                                                        if(Double.parseDouble(value.toString()) < 0) 
                                                            JOptionPane.showMessageDialog(null, "El campo no permite numeros negativos"); 
                                                        //buscaCuentas(row, col);
                                                    }
                                                }
                                                else
                                                {
                                                    JOptionPane.showMessageDialog(null, "La valuación esta cerrada"); 
                                                }
                                            }
                                            else
                                            {
                                                JOptionPane.showMessageDialog(null, "Acceso denegado"); 
                                            }
                                        }
                                        catch(Exception e)
                                        {
                                            session.getTransaction().rollback();
                                            e.printStackTrace();
                                            JOptionPane.showMessageDialog(null, "Error al actualizar los datos"); 
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
                                
                            case 8:
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
                                            if(user.getEditaHoras()==true)
                                            {
                                                if(r_cerrar_valuacion.isSelected()==false)
                                                {
                                                    if(value.toString().compareTo("")!=0 && Double.parseDouble(value.toString())>=0) 
                                                    {
                                                         Partida part=(Partida) session.createCriteria(Partida.class).add(Restrictions.eq("ordenByIdOrden.idOrden", Integer.parseInt(orden))).add(Restrictions.eq("idEvaluacion", Integer.parseInt(t_datos.getValueAt(t_datos.getSelectedRow(), 0).toString()))).add(Restrictions.eq("subPartida", Integer.parseInt(t_datos.getValueAt(t_datos.getSelectedRow(), 1).toString()))).setMaxResults(1).uniqueResult();
                                                         if(part!=null)
                                                         {
                                                             imp=part.getOrdenByIdOrden().getCompania().getImporteHora();
                                                             part.setPint(BigDecimal.valueOf(Double.parseDouble(value.toString())).setScale(2, RoundingMode.HALF_UP).doubleValue());
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
                                                             //buscaCuentas(row, col);
                                                             sumaTotales(imp);
                                                             verEstado();
                                                         }
                                                         else
                                                         {
                                                             JOptionPane.showMessageDialog(null, "La partida ya no existe");
                                                             buscaCuentas(-1,-1);
                                                         }
                                                    }
                                                    else
                                                    {
                                                        if(Double.parseDouble(value.toString()) < 0) 
                                                            JOptionPane.showMessageDialog(null, "El campo no permite numeros negativos"); 
                                                        //buscaCuentas(row, col);
                                                    }
                                                }
                                                else
                                                {
                                                    JOptionPane.showMessageDialog(null, "La valuación esta cerrada"); 
                                                }
                                            }
                                            else
                                            {
                                                JOptionPane.showMessageDialog(null, "Acceso denegado"); 
                                            }
                                        }
                                        catch(Exception e)
                                        {
                                            session.getTransaction().rollback();
                                            e.printStackTrace();
                                            JOptionPane.showMessageDialog(null, "Error al actualizar los datos"); 
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

                            case 12://codigo
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
                                            if(t_datos.getValueAt(row, 37).toString().compareTo("e")!=0)
                                            {
                                                if(value!=null)
                                                {
                                                    switch(value.toString())
                                                    {
                                                        case "Eliminar":
                                                            session = HibernateUtil.getSessionFactory().openSession();
                                                            session.beginTransaction().begin();
                                                            user = (Usuario)session.get(Usuario.class, user.getIdUsuario());
                                                            if(user.getEditaCodigo()==true)
                                                            {
                                                                Partida part=(Partida) session.createCriteria(Partida.class).add(Restrictions.eq("ordenByIdOrden.idOrden", Integer.parseInt(orden))).add(Restrictions.eq("idEvaluacion", Integer.parseInt(t_datos.getValueAt(t_datos.getSelectedRow(), 0).toString()))).add(Restrictions.eq("subPartida", Integer.parseInt(t_datos.getValueAt(t_datos.getSelectedRow(), 1).toString()))).setMaxResults(1).uniqueResult();
                                                                imp=part.getOrdenByIdOrden().getCompania().getImporteHora();
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
                                                                if(permiso=true)
                                                                {
                                                                    part.setEjemplar(null);
                                                                    part.setSurteAlmacen(false);
                                                                    if(part.getCam()!=-1 && part.getIntCamb()!=-1)
                                                                        part.setRefComp(true);
                                                                    session.update(part);
                                                                    session.getTransaction().commit();
                                                                    vector.setElementAt("", col);
                                                                    dataVector.setElementAt(vector, row);
                                                                    fireTableCellUpdated(row, col);
                                                                    if(session.isOpen()==true)
                                                                    {
                                                                        session.flush();
                                                                        session.clear();
                                                                        session.close();
                                                                    }
                                                                    buscaCuentas(row, col);
                                                                    sumaTotales(imp);
                                                                    verEstado();
                                                                }
                                                                else
                                                                    JOptionPane.showMessageDialog(null, "La partida ya Tiene Entregas no se puede modificar");
                                                            }
                                                            break;
                                                        case "Cancelar":
                                                            break;
                                                        case "Nuevo":
                                                            session = HibernateUtil.getSessionFactory().openSession();
                                                            session.beginTransaction().begin();
                                                            user = (Usuario)session.get(Usuario.class, user.getIdUsuario());
                                                            if(user.getEditaCodigo()==true)
                                                            {
                                                                Partida part=(Partida) session.createCriteria(Partida.class).add(Restrictions.eq("ordenByIdOrden.idOrden", Integer.parseInt(orden))).add(Restrictions.eq("idEvaluacion", Integer.parseInt(t_datos.getValueAt(t_datos.getSelectedRow(), 0).toString()))).add(Restrictions.eq("subPartida", Integer.parseInt(t_datos.getValueAt(t_datos.getSelectedRow(), 1).toString()))).setMaxResults(1).uniqueResult();
                                                                Catalogo cat = part.getCatalogo();
                                                                altaEjemplar obj1 = new altaEjemplar(new javax.swing.JFrame(), true, user, sessionPrograma, 0);
                                                                obj1.t_id_catalogo.setText(""+cat.getIdCatalogo());
                                                                obj1.t_catalogo.setText(cat.getNombre());
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
                                                                        part=(Partida) session.createCriteria(Partida.class).add(Restrictions.eq("ordenByIdOrden.idOrden", Integer.parseInt(orden))).add(Restrictions.eq("idEvaluacion", Integer.parseInt(t_datos.getValueAt(t_datos.getSelectedRow(), 0).toString()))).add(Restrictions.eq("subPartida", Integer.parseInt(t_datos.getValueAt(t_datos.getSelectedRow(), 1).toString()))).setMaxResults(1).uniqueResult();
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
                                                                            if(permiso=true)
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
                                                                                    part.setSurteAlmacen(true);
                                                                                    part.setRefComp(false);
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

                                                                                        vector.setElementAt(part.getCU(), 14);
                                                                                        vector.setElementAt(part.getPrecioCiaSegurosCU(), 16);
                                                                                    }
                                                                                }
                                                                                session.update(part);
                                                                                session.getTransaction().commit();
                                                                                vector.setElementAt(ejem.getIdParte(), col);
                                                                                this.dataVector.setElementAt(vector, row);
                                                                                fireTableCellUpdated(row, col);
                                                                                fireTableCellUpdated(row, 14);
                                                                                fireTableCellUpdated(row, 16);
                                                                                buscaCuentas(row,col);
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
                                                                }
                                                            }
                                                            break;
                                                        case "Buscar":
                                                            session = HibernateUtil.getSessionFactory().openSession();
                                                            session.beginTransaction().begin();
                                                            user = (Usuario)session.get(Usuario.class, user.getIdUsuario());
                                                            session.beginTransaction().begin();
                                                            Partida part=(Partida) session.createCriteria(Partida.class).add(Restrictions.eq("ordenByIdOrden.idOrden", Integer.parseInt(orden))).add(Restrictions.eq("idEvaluacion", Integer.parseInt(t_datos.getValueAt(t_datos.getSelectedRow(), 0).toString()))).add(Restrictions.eq("subPartida", Integer.parseInt(t_datos.getValueAt(t_datos.getSelectedRow(), 1).toString()))).setMaxResults(1).uniqueResult();
                                                            if(part!=null)
                                                            {
                                                                if(user.getEditaCodigo()==true)
                                                                {
                                                                    buscaEjemplar obj = new buscaEjemplar(new javax.swing.JFrame(), true, sessionPrograma, null, 2);
                                                                    obj.t_busca.requestFocus();
                                                                    Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
                                                                    obj.setLocation((d.width/2)-(obj.getWidth()/2), (d.height/2)-(obj.getHeight()/2));
                                                                    obj.setVisible(true);
                                                                    Ejemplar ejem=obj.getReturnStatus();
                                                                    if (ejem!=null)
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
                                                                        if(permiso=true)
                                                                        {
                                                                            boolean permiso2=true;
                                                                            part.setEjemplar(ejem);
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

                                                                                        vector.setElementAt(part.getCU(), 14);
                                                                                        vector.setElementAt(part.getPrecioCiaSegurosCU(), 16);
                                                                                    }
                                                                                }
                                                                                session.update(part);
                                                                                session.getTransaction().commit();
                                                                                vector.setElementAt(ejem.getIdParte(), col);

                                                                                this.dataVector.setElementAt(vector, row);
                                                                                fireTableCellUpdated(row, col);
                                                                                fireTableCellUpdated(row, 14);
                                                                                fireTableCellUpdated(row, 16);
                                                                                buscaCuentas(row,col);
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
                                                                else
                                                                    JOptionPane.showMessageDialog(null, "Acceso Denegado");
                                                            }
                                                            break;
                                                        /*default:
                                                            session = HibernateUtil.getSessionFactory().openSession();
                                                            session.beginTransaction().begin();
                                                            Partida part=(Partida) session.createCriteria(Partida.class).add(Restrictions.eq("ordenByIdOrden.idOrden", Integer.parseInt(orden))).add(Restrictions.eq("idEvaluacion", Integer.parseInt(t_datos.getValueAt(t_datos.getSelectedRow(), 0).toString()))).add(Restrictions.eq("subPartida", Integer.parseInt(t_datos.getValueAt(t_datos.getSelectedRow(), 1).toString()))).setMaxResults(1).uniqueResult();
                                                            if(part!=null)
                                                            {
                                                                Ejemplar ejem = (Ejemplar)session.get(Ejemplar.class, value.toString());
                                                                //part.setEjemplar(ejem);
                                                                Criteria crit = session.createCriteria(Partida.class);
                                                                crit.add(Restrictions.eq("ejemplar.idParte", ejem.getIdParte()));
                                                                crit = crit.createAlias("pedido", "ped");
                                                                //crit=crit.addOrder(Order.asc("ped.fechaPedido"));
                                                                crit=crit.addOrder(Order.desc("pcp"));
                                                                crit.add(Restrictions.isNotNull("pedido"));
                                                                Partida partidaPrecio=(Partida) crit.setMaxResults(1).uniqueResult();
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
                                                                if(permiso=true)
                                                                {
                                                                    boolean permiso2=true;
                                                                    part.setEjemplar(ejem);
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

                                                                                vector.setElementAt(part.getCU(), 14);
                                                                                vector.setElementAt(part.getPrecioCiaSegurosCU(), 16);
                                                                            }
                                                                        }
                                                                        session.update(part);
                                                                        session.getTransaction().commit();
                                                                        vector.setElementAt(value, col);

                                                                        this.dataVector.setElementAt(vector, row);
                                                                        fireTableCellUpdated(row, col);
                                                                        fireTableCellUpdated(row, 14);
                                                                        fireTableCellUpdated(row, 16);
                                                                        buscaCuentas(row,col);
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
                                                        break;*/
                                                    }
                                                }
                                            }
                                            else
                                            {
                                               JOptionPane.showMessageDialog(null, "No se pueden modificar Partidas enlazadas de otra orden");
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
                            case 13://cu
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
                                            if(user.getEditaPrecioCu()==true)
                                            {
                                                    if(value.toString().compareTo("")!=0 && Double.parseDouble(value.toString())>=0) 
                                                    {
                                                         Partida part=(Partida) session.createCriteria(Partida.class).add(Restrictions.eq("ordenByIdOrden.idOrden", Integer.parseInt(orden))).add(Restrictions.eq("idEvaluacion", Integer.parseInt(t_datos.getValueAt(t_datos.getSelectedRow(), 0).toString()))).add(Restrictions.eq("subPartida", Integer.parseInt(t_datos.getValueAt(t_datos.getSelectedRow(), 1).toString()))).setMaxResults(1).uniqueResult();
                                                         if(part!=null)
                                                         {
                                                             boolean redondeo = part.getOrdenByIdOrden().getCompania().isRedondeo();
                                                             imp=part.getOrdenByIdOrden().getCompania().getImporteHora();
                                                             part.setCU(BigDecimal.valueOf(Double.parseDouble(value.toString())).setScale(2, RoundingMode.HALF_UP).doubleValue());
                                                             double precio=0.0d;
                                                             if(redondeo==false)
                                                                precio=0.0+part.getCU()/(1-(part.getPorcentaje()*0.01));
                                                             else
                                                                precio=0.0+Math.round(part.getCU()/(1-(part.getPorcentaje()*0.01)));
                                                             
                                                             part.setPrecioCiaSegurosCU(new BigDecimal(precio).setScale(2, RoundingMode.HALF_UP).doubleValue());
                                                             session.update(part);
                                                             session.getTransaction().commit();
                                                             vector.setElementAt(value, col);
                                                             vector.setElementAt(part.getPrecioCiaSegurosCU(), 15);
                                                             this.dataVector.setElementAt(vector, row);
                                                             fireTableCellUpdated(row, col);
                                                             fireTableCellUpdated(row, 15);
                                                             if(session.isOpen()==true)
                                                             {
                                                                 session.flush();
                                                                 session.clear();
                                                                 session.close();
                                                             }
                                                             sumaTotales(imp);
                                                             verEstado();
                                                         }
                                                         else
                                                         {
                                                             JOptionPane.showMessageDialog(null, "La partida ya no existe");
                                                             buscaCuentas(-1,-1);
                                                         }
                                                    }
                                                    else
                                                    {
                                                        if(Double.parseDouble(value.toString()) < 0) 
                                                            JOptionPane.showMessageDialog(null, "El campo no permite numeros negativos"); 
                                                    }
                                            }
                                            else
                                            {
                                                JOptionPane.showMessageDialog(null, "Acceso denegado"); 
                                            }
                                        }
                                        catch(Exception e)
                                        {
                                            session.getTransaction().rollback();
                                            e.printStackTrace();
                                            JOptionPane.showMessageDialog(null, "Error al actualizar los datos"); 
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
                                
                            case 14://%
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
                                            if(user.getEditaPorcentaje()==true)
                                            {
                                                    if(value.toString().compareTo("")!=0 && Double.parseDouble(value.toString())>=0) 
                                                    {
                                                         Partida part=(Partida) session.createCriteria(Partida.class).add(Restrictions.eq("ordenByIdOrden.idOrden", Integer.parseInt(orden))).add(Restrictions.eq("idEvaluacion", Integer.parseInt(t_datos.getValueAt(t_datos.getSelectedRow(), 0).toString()))).add(Restrictions.eq("subPartida", Integer.parseInt(t_datos.getValueAt(t_datos.getSelectedRow(), 1).toString()))).setMaxResults(1).uniqueResult();
                                                         if(part!=null)
                                                         {
                                                             boolean redondeo = part.getOrdenByIdOrden().getCompania().isRedondeo();
                                                             imp=part.getOrdenByIdOrden().getCompania().getImporteHora();
                                                             part.setPorcentaje(BigDecimal.valueOf(Double.parseDouble(value.toString())).setScale(2, RoundingMode.HALF_UP).doubleValue());
                                                             double precio=0.0d;
                                                             if(redondeo==false)
                                                                precio=0.0+part.getCU()/(1-(part.getPorcentaje()*0.01));
                                                             else
                                                                precio=0.0+Math.round(part.getCU()/(1-(part.getPorcentaje()*0.01)));
                                                             part.setPrecioCiaSegurosCU(new BigDecimal(precio).setScale(2, RoundingMode.HALF_UP).doubleValue());
                                                             session.update(part);
                                                             session.getTransaction().commit();
                                                             vector.setElementAt(value, col);
                                                             vector.setElementAt(part.getPrecioCiaSegurosCU(), 15);
                                                             this.dataVector.setElementAt(vector, row);
                                                             fireTableCellUpdated(row, col);
                                                             fireTableCellUpdated(row, 15);
                                                             if(session.isOpen()==true)
                                                             {
                                                                 session.flush();
                                                                 session.clear();
                                                                 session.close();
                                                             }
                                                             sumaTotales(imp);
                                                             verEstado();
                                                         }
                                                         else
                                                         {
                                                             JOptionPane.showMessageDialog(null, "La partida ya no existe");
                                                             buscaCuentas(-1,-1);
                                                         }
                                                    }
                                                    else
                                                    {
                                                        if(Double.parseDouble(value.toString()) < 0) 
                                                            JOptionPane.showMessageDialog(null, "El campo no permite numeros negativos"); 
                                                    }
                                            }
                                            else
                                            {
                                                JOptionPane.showMessageDialog(null, "Acceso denegado"); 
                                            }
                                        }
                                        catch(Exception e)
                                        {
                                            session.getTransaction().rollback();
                                            e.printStackTrace();
                                            JOptionPane.showMessageDialog(null, "Error al actualizar los datos"); 
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
                                
                            case 16://can aut
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
                                            if(user.getEditaCantAut()==true)
                                            {
                                                if(r_cerrar_valuacion.isSelected()==false)
                                                {
                                                    if(value.toString().compareTo("")!=0 && Double.parseDouble(value.toString())>=0) 
                                                    {
                                                         Partida part=(Partida) session.createCriteria(Partida.class).add(Restrictions.eq("ordenByIdOrden.idOrden", Integer.parseInt(orden))).add(Restrictions.eq("idEvaluacion", Integer.parseInt(t_datos.getValueAt(t_datos.getSelectedRow(), 0).toString()))).add(Restrictions.eq("subPartida", Integer.parseInt(t_datos.getValueAt(t_datos.getSelectedRow(), 1).toString()))).setMaxResults(1).uniqueResult();
                                                         if(part!=null)
                                                         {
                                                             //if(part.getOrdenByIdOrden().getAutorizadoFacturar()!=true)
                                                             //{  
                                                                imp=part.getOrdenByIdOrden().getCompania().getImporteHora();
                                                                part.setCantidadAut(BigDecimal.valueOf(Double.parseDouble(value.toString())).setScale(2, RoundingMode.HALF_UP).doubleValue());
                                                                //part.setCantidadFactura(part.getCantidadAut());
                                                                session.update(part);
                                                                session.getTransaction().commit();
                                                                vector.setElementAt(value, col);
                                                                vector.setElementAt(part.getPrecioAutCU()*BigDecimal.valueOf(Double.parseDouble(value.toString())).setScale(2, RoundingMode.HALF_UP).doubleValue(), 18);
                                                                this.dataVector.setElementAt(vector, row);
                                                                fireTableCellUpdated(row, col);
                                                                fireTableCellUpdated(row, 18);
                                                                if(session.isOpen()==true)
                                                                {
                                                                    session.flush();
                                                                    session.clear();
                                                                    session.close();
                                                                }
                                                                sumaTotales(imp);
                                                                verEstado();
                                                             //}
                                                         }
                                                         else
                                                         {
                                                             JOptionPane.showMessageDialog(null, "La partida ya no existe");
                                                             buscaCuentas(-1,-1);
                                                         }
                                                    }
                                                    else
                                                    {
                                                        if(Double.parseDouble(value.toString()) < 0) 
                                                            JOptionPane.showMessageDialog(null, "El campo no permite numeros negativos"); 
                                                    }
                                                }
                                                else
                                                {
                                                    JOptionPane.showMessageDialog(null, "La valuación esta cerrada"); 
                                                }
                                            }
                                        }
                                        catch(Exception e)
                                        {
                                            session.getTransaction().rollback();
                                            e.printStackTrace();
                                            JOptionPane.showMessageDialog(null, "Error al actualizar los datos"); 
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
                                
                            case 17://aut c/u
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
                                            if(user.getEditaCostoAut()==true)
                                            {
                                                if(r_cerrar_valuacion.isSelected()==false)
                                                {
                                                    if(value.toString().compareTo("")!=0 && Double.parseDouble(value.toString())>=0) 
                                                    {
                                                         Partida part=(Partida) session.createCriteria(Partida.class).add(Restrictions.eq("ordenByIdOrden.idOrden", Integer.parseInt(orden))).add(Restrictions.eq("idEvaluacion", Integer.parseInt(t_datos.getValueAt(t_datos.getSelectedRow(), 0).toString()))).add(Restrictions.eq("subPartida", Integer.parseInt(t_datos.getValueAt(t_datos.getSelectedRow(), 1).toString()))).setMaxResults(1).uniqueResult();
                                                         if(part!=null)
                                                         {
                                                             /*if(part.getOrdenByIdOrden().getAutorizadoFacturar()!=true)
                                                             {*/
                                                                imp=part.getOrdenByIdOrden().getCompania().getImporteHora();
                                                                part.setPrecioAutCU(BigDecimal.valueOf(Double.parseDouble(value.toString())).setScale(2, RoundingMode.HALF_UP).doubleValue());
                                                                //part.setPrecioFactura(part.getPrecioAutCU());
                                                                session.update(part);
                                                                session.getTransaction().commit();
                                                                vector.setElementAt(value, col);
                                                                vector.setElementAt(part.getCantidadAut()*BigDecimal.valueOf(Double.parseDouble(value.toString())).setScale(2, RoundingMode.HALF_UP).doubleValue(), 18);
                                                                this.dataVector.setElementAt(vector, row);
                                                                fireTableCellUpdated(row, col);
                                                                fireTableCellUpdated(row, 18);
                                                                if(session.isOpen()==true)
                                                                {
                                                                    session.flush();
                                                                    session.clear();
                                                                    session.close();
                                                                }
                                                                sumaTotales(imp);
                                                                verEstado();
                                                             //}
                                                         }
                                                         else
                                                         {
                                                             JOptionPane.showMessageDialog(null, "La partida ya no existe");
                                                             buscaCuentas(-1,-1);
                                                         }
                                                    }
                                                    else
                                                    {
                                                        if(Double.parseDouble(value.toString()) < 0) 
                                                            JOptionPane.showMessageDialog(null, "El campo no permite numeros negativos"); 
                                                    }
                                                }
                                                else
                                                {
                                                    JOptionPane.showMessageDialog(null, "La valuación esta cerrada"); 
                                                }
                                            }
                                        }
                                        catch(Exception e)
                                        {
                                            session.getTransaction().rollback();
                                            e.printStackTrace();
                                            JOptionPane.showMessageDialog(null, "Error al actualizar los datos"); 
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
                            case 19://vales
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
                                            if(user.getEditaCostoAut()==true)
                                            {
                                                if(r_cerrar_valuacion.isSelected()==false)
                                                {
                                                    Partida part=(Partida) session.createCriteria(Partida.class).add(Restrictions.eq("ordenByIdOrden.idOrden", Integer.parseInt(orden))).add(Restrictions.eq("idEvaluacion", Integer.parseInt(t_datos.getValueAt(t_datos.getSelectedRow(), 0).toString()))).add(Restrictions.eq("subPartida", Integer.parseInt(t_datos.getValueAt(t_datos.getSelectedRow(), 1).toString()))).setMaxResults(1).uniqueResult();
                                                    imp=part.getOrdenByIdOrden().getCompania().getImporteHora();
                                                    if(part!=null)
                                                    {
                                                        if(part.getOrdenByIdOrden().getAutorizadoFacturar()!=true)
                                                        {
                                                            if((boolean)value==true)
                                                            {
                                                                part.setFacturado(true);
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
                                                                verEstado();
                                                            }
                                                            else
                                                            {
                                                                part.setFacturado(false);
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
                                                                verEstado();
                                                            }
                                                         }
                                                         else
                                                         {
                                                             JOptionPane.showMessageDialog(null, "La Orden ya se envío a facturar");
                                                         }
                                                    }
                                                    else
                                                    {
                                                        JOptionPane.showMessageDialog(null, "La partida ya no existe");
                                                        buscaCuentas(-1,-1);
                                                    }
                                                    sumaTotales(imp);
                                                }
                                                else
                                                {
                                                    JOptionPane.showMessageDialog(null, "La valuación esta cerrada"); 
                                                }
                                            }
                                            else
                                            {
                                                JOptionPane.showMessageDialog(null, "Acceso denegado"); 
                                            }
                                        }
                                        catch(Exception e)
                                        {
                                            session.getTransaction().rollback();
                                            e.printStackTrace();
                                            JOptionPane.showMessageDialog(null, "Error al actualizar los datos"); 
                                        }
                                        if(session!=null)
                                            if(session.isOpen())
                                            {
                                                session.flush();
                                                session.clear();
                                                session.close();
                                            }
                                    }
                                break;
                                        
                            case 20://aut
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
                                            if(user.getEditaAutorizaPartida()==true)
                                            {
                                                Partida part=(Partida) session.createCriteria(Partida.class).add(Restrictions.eq("ordenByIdOrden.idOrden", Integer.parseInt(orden))).add(Restrictions.eq("idEvaluacion", Integer.parseInt(t_datos.getValueAt(t_datos.getSelectedRow(), 0).toString()))).add(Restrictions.eq("subPartida", Integer.parseInt(t_datos.getValueAt(t_datos.getSelectedRow(), 1).toString()))).setMaxResults(1).uniqueResult();
                                                imp=part.getOrdenByIdOrden().getCompania().getImporteHora();
                                                if(part!=null)
                                                {
                                                    if((boolean)value==true)
                                                    {
                                                        part.setAutorizado(true);
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
                                                        verEstado();
                                                    }
                                                    else
                                                    {
                                                        if(part.getPedido()==null)
                                                        {
                                                            part.setAutorizado(false);
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
                                                            verEstado();
                                                        }
                                                        else
                                                            JOptionPane.showMessageDialog(null, "La partida ya fue pedida");
                                                    }
                                                }
                                                else
                                                {
                                                    JOptionPane.showMessageDialog(null, "La partida ya no existe");
                                                    buscaCuentas(-1,-1);
                                                }
                                                sumaTotales(imp);
                                            }
                                            else
                                            {
                                                JOptionPane.showMessageDialog(null, "Acceso denegado"); 
                                            }
                                        }
                                        catch(Exception e)
                                        {
                                            session.getTransaction().rollback();
                                            e.printStackTrace();
                                            JOptionPane.showMessageDialog(null, "Error al actualizar los datos"); 
                                        }
                                        if(session!=null)
                                            if(session.isOpen())
                                            {
                                                session.flush();
                                                session.clear();
                                                session.close();
                                            }
                                    }
                                    break;
                                
                            case 22://r cotiza
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
                                            if(user.getEditaAutorizaCompra()==true)
                                            {
                                                Partida part=(Partida) session.createCriteria(Partida.class).add(Restrictions.eq("ordenByIdOrden.idOrden", Integer.parseInt(orden))).add(Restrictions.eq("idEvaluacion", Integer.parseInt(t_datos.getValueAt(t_datos.getSelectedRow(), 0).toString()))).add(Restrictions.eq("subPartida", Integer.parseInt(t_datos.getValueAt(t_datos.getSelectedRow(), 1).toString()))).setMaxResults(1).uniqueResult();
                                                if(part!=null)
                                                {
                                                    if((boolean)value==true)
                                                    {
                                                        if(part.getOrdenByIdOrden().getRCotizaCierre()==null)
                                                        {
                                                            part.setRefCoti(true);
                                                        }
                                                        else
                                                        {
                                                            JOptionPane.showMessageDialog(null, "Las Cotizaciones ya estan cerradas, solicitar la Apertura al Depto de Compras");
                                                            buscaCuentas(-1,-1);
                                                        }
                                                    }
                                                    else
                                                        part.setRefCoti(false);
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
                                                    verEstado();
                                                }
                                                else
                                                {
                                                    JOptionPane.showMessageDialog(null, "La partida ya no existe");
                                                    buscaCuentas(-1,-1);
                                                }
                                            }
                                            else
                                            {
                                                JOptionPane.showMessageDialog(null, "Acceso denegado"); 
                                            }
                                        }
                                        catch(Exception e)
                                        {
                                            session.getTransaction().rollback();
                                            e.printStackTrace();
                                            JOptionPane.showMessageDialog(null, "Error al actualizar los datos"); 
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
                                
                            case 23://r compra
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
                                            if(user.getEditaAutorizaCompra()==true)
                                            {
                                                Partida part=(Partida) session.createCriteria(Partida.class).add(Restrictions.eq("ordenByIdOrden.idOrden", Integer.parseInt(orden))).add(Restrictions.eq("idEvaluacion", Integer.parseInt(t_datos.getValueAt(t_datos.getSelectedRow(), 0).toString()))).add(Restrictions.eq("subPartida", Integer.parseInt(t_datos.getValueAt(t_datos.getSelectedRow(), 1).toString()))).setMaxResults(1).uniqueResult();
                                                if(part!=null)
                                                {
                                                    Orden ot=part.getOrdenByIdOrden();
                                                    if((boolean)value==true)
                                                    {
                                                        if(ot.getCierreRefacciones()==null)
                                                        {
                                                            if(part.getCam()!=-1 && part.getIntCamb()!=-1)
                                                            {
                                                                if(part.isSurteAlmacen()==false)
                                                                {
                                                                    if(part.getTipo().compareTo("o")==0 && part.getOrdenByIdOrden().getRLevantamientoCierre()!=null){
                                                                        part.setTipo("c");
                                                                        t_datos.setValueAt("c", row, 37);
                                                                    }
                                                                    part.setRefComp(true);
                                                                    session.update(part);
                                                                    session.getTransaction().commit();
                                                                    vector.setElementAt(value, col);
                                                                    this.dataVector.setElementAt(vector, row);
                                                                    fireTableCellUpdated(row, col);
                                                                }
                                                                else
                                                                    JOptionPane.showMessageDialog(null, "No se puede compra Material que se maneja de stock en almacen");   
                                                                if(session.isOpen()==true)
                                                                {
                                                                    session.flush();
                                                                    session.clear();
                                                                    session.close();
                                                                }
                                                                verEstado();
                                                            }
                                                            else
                                                                JOptionPane.showMessageDialog(null, "No se puede compra sin cambiar en aseguradora");   
                                                        }
                                                        else
                                                        {
                                                            JOptionPane.showMessageDialog(null, "Las compras ya estan cerradas, solicitar la Apertura al Depto de Compras");
                                                            buscaCuentas(-1,-1);
                                                        }
                                                    }
                                                    else
                                                    {
                                                        if(part.isRefComp()==true)
                                                        {
                                                            if(part.getPedido()==null)
                                                            {
                                                                part.setRefComp(false);
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
                                                                verEstado();
                                                            }
                                                            else
                                                                JOptionPane.showMessageDialog(null, "La partida ya fue pedida");
                                                        }
                                                    }
                                                }
                                                else
                                                {
                                                    JOptionPane.showMessageDialog(null, "La partida ya no existe");
                                                    buscaCuentas(-1,-1);
                                                }
                                            }
                                            else
                                            {
                                                JOptionPane.showMessageDialog(null, "Acceso denegado"); 
                                            }
                                        }
                                        catch(Exception e)
                                        {
                                            session.getTransaction().rollback();
                                            e.printStackTrace();
                                            JOptionPane.showMessageDialog(null, "Error al actualizar los datos"); 
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
        
                            case 25://so
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
                                            if(user.getEditaTipoSustido()==true)
                                            {
                                                /*if(r_cerrar_valuacion.isSelected()==false)
                                                {*/
                                                    Partida part=(Partida) session.createCriteria(Partida.class).add(Restrictions.eq("ordenByIdOrden.idOrden", Integer.parseInt(orden))).add(Restrictions.eq("idEvaluacion", Integer.parseInt(t_datos.getValueAt(t_datos.getSelectedRow(), 0).toString()))).add(Restrictions.eq("subPartida", Integer.parseInt(t_datos.getValueAt(t_datos.getSelectedRow(), 1).toString()))).setMaxResults(1).uniqueResult();
                                                    if(part!=null)
                                                    {
                                                        if(part.getPedido()==null)
                                                        {
                                                            if((boolean)value==true)
                                                                part.setSo(true);
                                                            else
                                                                part.setSo(false);
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
                                                            verEstado();
                                                        }
                                                        else
                                                            JOptionPane.showMessageDialog(null, "La partida ya tiene un pedido y se surtiria 2 veces");
                                                    }
                                                    else
                                                    {
                                                        JOptionPane.showMessageDialog(null, "La partida ya no existe");
                                                        buscaCuentas(-1,-1);
                                                    }
                                                /*}
                                                else
                                                {
                                                    JOptionPane.showMessageDialog(null, "La valuación esta cerrada"); 
                                                }*/
                                            }
                                            else
                                            {
                                                JOptionPane.showMessageDialog(null, "Acceso denegado"); 
                                            }
                                        }
                                        catch(Exception e)
                                        {
                                            session.getTransaction().rollback();
                                            e.printStackTrace();
                                            JOptionPane.showMessageDialog(null, "Error al actualizar los datos"); 
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
                                
                            case 26://pd
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
                                            if(user.getEditaTipoSustido()==true)
                                            {
                                                if(r_cerrar_valuacion.isSelected()==false)
                                                {
                                                    Partida part=(Partida) session.createCriteria(Partida.class).add(Restrictions.eq("ordenByIdOrden.idOrden", Integer.parseInt(orden))).add(Restrictions.eq("idEvaluacion", Integer.parseInt(t_datos.getValueAt(t_datos.getSelectedRow(), 0).toString()))).add(Restrictions.eq("subPartida", Integer.parseInt(t_datos.getValueAt(t_datos.getSelectedRow(), 1).toString()))).setMaxResults(1).uniqueResult();
                                                    if(part!=null)
                                                    {
                                                        if((boolean)value==true)
                                                            part.setPd(true);
                                                        else
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
                                                        verEstado();
                                                    }
                                                    else
                                                    {
                                                        JOptionPane.showMessageDialog(null, "La partida ya no existe");
                                                        buscaCuentas(-1,-1);
                                                    }
                                                }
                                                else
                                                {
                                                    JOptionPane.showMessageDialog(null, "La valuación esta cerrada"); 
                                                }
                                            }
                                            else
                                            {
                                                JOptionPane.showMessageDialog(null, "Acceso denegado"); 
                                            }
                                        }
                                        catch(Exception e)
                                        {
                                            session.getTransaction().rollback();
                                            e.printStackTrace();
                                            JOptionPane.showMessageDialog(null, "Error al actualizar los datos"); 
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
                            case 27:
                                    if(vector.get(col)==null)
                                    {
                                        vector.setElementAt(value, col);
                                        this.dataVector.setElementAt(vector, row);
                                        fireTableCellUpdated(row, col);
                                    }
                                    else
                                    {
                                        if(user.getGeneraPedidos()==true)
                                        {
                                            if(user.getEditaPedidos()==true)
                                            {
                                                 if((r_cerrar_valuacion.isVisible()==true && r_cerrar_valuacion.isSelected()==false) ||(r_cerrar_cotizacion.isVisible()==true && r_cerrar_cotizacion.isSelected()==false))
                                                {
                                                    Session session = HibernateUtil.getSessionFactory().openSession();
                                                    session.beginTransaction().begin();

                                                    Partida part=(Partida) session.createCriteria(Partida.class).add(Restrictions.eq("ordenByIdOrden.idOrden", Integer.parseInt(orden))).add(Restrictions.eq("idEvaluacion", Integer.parseInt(t_datos.getValueAt(t_datos.getSelectedRow(), 0).toString()))).add(Restrictions.eq("subPartida", Integer.parseInt(t_datos.getValueAt(t_datos.getSelectedRow(), 1).toString()))).setMaxResults(1).uniqueResult();
                                                    try
                                                    {
                                                        if(part!=null)
                                                        {
                                                            String valor = value.toString().toLowerCase();
                                                            if(valor.compareTo("-")!=0)
                                                                part.setTipoPieza(valor);
                                                            else
                                                                part.setTipoPieza(null);
                                                            System.out.println("Valor:" + valor);
                                                            session.update(part);
                                                            session.getTransaction().commit();
                                                            vector.setElementAt(value, col);
                                                            this.dataVector.setElementAt(vector, row);
                                                            fireTableCellUpdated(row, col);
                                                        }
                                                        else
                                                            JOptionPane.showMessageDialog(null, "La partida ya no existe"); 
                                                    }catch(Exception e)
                                                    {
                                                        session.getTransaction().rollback();
                                                        System.out.println(e);
                                                        JOptionPane.showMessageDialog(null, "Error al actualizar"); 
                                                    }
                                                    finally
                                                    {
                                                        if(session.isOpen()==true)
                                                            session.close();
                                                    }
                                                }else
                                                    JOptionPane.showMessageDialog(null, "El modulo ya esta cerrado"); 
                                            }
                                            else
                                                JOptionPane.showMessageDialog(null, "Acceso denegado no puedes modificar Pedidos"); 
                                        }
                                        else
                                            JOptionPane.showMessageDialog(null, "Acceso denegado"); 
                                    }
                                    break;
                            default:
                                    vector.setElementAt(value, col);
                                    this.dataVector.setElementAt(vector, row);
                                    fireTableCellUpdated(row, col);
                                    break;
                        }
                        t_datos.requestFocus();
                }
                
                public Class getColumnClass(int columnIndex) {
                    return types [columnIndex];
                }

                public boolean isCellEditable(int rowIndex, int columnIndex) {
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
    //not necessary
    }
    public void sumaTotales(double importe)
    {
        double refacciones_costo=0.0;
        double cia_seg=0.0;
        double autorizado=0.0;
        double vales=0.0;
        double mano_obra_horas=0.0;
        double mano_obra_cash=0.0;
        for(int ren=0; ren<t_datos.getRowCount(); ren++)
        {
            if(t_datos.getValueAt(ren, 38).toString().compareTo("e")!=0)
            {
                double res=0.0;
                for(int r=3; r<9; r++)
                {
                    if(t_datos.getValueAt(ren, r)!=null)
                        res+=BigDecimal.valueOf(Double.parseDouble(t_datos.getValueAt(ren, r).toString())).setScale(2, RoundingMode.HALF_UP).doubleValue();
                }
                BigDecimal BigCantidad=BigDecimal.valueOf(Double.parseDouble(t_datos.getValueAt(ren, 9).toString())).setScale(2, RoundingMode.HALF_UP);
                BigDecimal BigCU=BigDecimal.valueOf(Double.parseDouble(t_datos.getValueAt(ren, 13).toString())).setScale(2, RoundingMode.HALF_UP);
                BigDecimal BigCIA=BigDecimal.valueOf(Double.parseDouble(t_datos.getValueAt(ren, 15).toString())).setScale(2, RoundingMode.HALF_UP);
                BigDecimal BigAUT=BigDecimal.valueOf(Double.parseDouble(t_datos.getValueAt(ren, 18).toString())).setScale(2, RoundingMode.HALF_UP);
                
                res*=BigCantidad.doubleValue();
                t_datos.setValueAt(res, ren, 21);
                
                refacciones_costo+=BigCU.doubleValue()*BigCantidad.doubleValue();
                cia_seg+=BigCIA.doubleValue()*BigCantidad.doubleValue();
                if(((boolean)t_datos.getValueAt(ren, 20))==true)
                    autorizado+=BigAUT.doubleValue();
                if(((boolean)t_datos.getValueAt(ren, 19))==true)
                    vales+=BigAUT.doubleValue();
                mano_obra_horas+=Double.parseDouble(t_datos.getValueAt(ren, 21).toString());
            }
        }

        t_costo_refacciones.setValue(refacciones_costo);
        t_cia_seguros.setValue(cia_seg);
        t_autorizado.setValue(autorizado);
        t_autorizado1.setValue(vales);
        t_horas.setValue(mano_obra_horas);
        t_importe.setValue(importe*mano_obra_horas);
    }
    
    public void visualiza(boolean valor)
    {
        if(valor==true)
        {
            t_ref_presupuesto.setEnabled(user.getEditaRefPresupuesto());
            t_ref_autorizadas_directo.setEnabled(user.getEditaRefAutorizadoDirecto());
            t_mo_directa.setEnabled(user.getEditaMoDirecta());
            t_mo_directa1.setEnabled(user.getEditaMoDirecta());
            
        }
        else
        {
            t_ref_presupuesto.setEnabled(valor);
            t_ref_autorizadas_directo.setEnabled(valor);
            t_mo_directa.setEnabled(valor);
            t_mo_directa1.setEnabled(valor);
            
            for(int x=0; x<t_datos.getColumnCount(); x++)
            {
                    if(x!=20 && x!=22 && x!=23 && x!=25 && x!=26 && x!=27)//chava
                        model.setColumnaEditable(x, valor);
                    else
                        model.setColumnaEditable(x, true);
            }
        }
        t_ref_presupuesto.setVisible(false);
        l1.setVisible(false);
        l3.setVisible(false);
            
        this.t_porcentaje.setEnabled(valor);
        this.t_porcentaje.setEnabled(valor);
        if(valor==true)
        {
            if(this.user.getEditaPorcentaje()==false)
            {
                this.t_ok.setEnabled(false);
                this.b_ok.setEnabled(false);
            }
            if(this.user.getEditaDd()==false)
            {
                this.t_deducible.setEnabled(false);
                this.t_demerito.setEnabled(false);
                this.b_ac.setEnabled(false);
            }
        }
        else
        {
            this.t_ok.setEnabled(valor);
            this.b_ok.setEnabled(valor);
        }
        
    }
    
    public void verEstado()
    {
        if(estado.compareTo("")==0)
        {
            this.t_deducible.setEnabled(true);
            this.t_demerito.setEnabled(true);
            this.b_ac.setEnabled(true);
            Session session = HibernateUtil.getSessionFactory().openSession();
            try 
            {
                h=new Herramientas(user, 0);
                h.session(sessionPrograma);
                session.beginTransaction().begin();
                ord=(Orden)session.get(Orden.class, Integer.parseInt(orden));
                if(menu==4)
                {
                    if(ord.getRValuacionCierre()!=null)
                        visualiza(false);
                    else
                        visualiza(true);
                }
                if(menu==11)
                {
                    if(ord.getRCotizaCierre()!=null)
                        visualiza(false);
                    else
                        visualiza(true);
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
        else{
            this.t_deducible.setEnabled(false);
            this.t_demerito.setEnabled(false);
            this.b_ac.setEnabled(false);
            visualiza(false);
        }
            
    }
    
   public void cabecera(PDF reporte, BaseFont bf, PdfPTable tabla)
   {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try
        {
            reporte.contenido.setLineWidth(0.5f);
            reporte.contenido.setColorStroke(new GrayColor(0.2f));
            reporte.contenido.setColorFill(new GrayColor(0.9f));
            reporte.contenido.roundRectangle(30, 700, 210, 45, 5);
            reporte.contenido.roundRectangle(250, 700, 325, 45, 5);
            //reporte.contenido.roundRectangle(550, 700, 210, 45, 5);

            Configuracion con=(Configuracion)session.get(Configuracion.class, configuracion);
            reporte.inicioTexto();
            reporte.contenido.setFontAndSize(bf, 14);
            reporte.contenido.setColorFill(BaseColor.BLACK);
            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, con.getEmpresa(), 30, 760, 0);
            reporte.contenido.setFontAndSize(bf, 8);
            reporte.contenido.setColorFill(BaseColor.BLACK);
            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Tabulador de Mano de Obra", 30, 750, 0);
            
            //reporte.contenido.showTextAligned(PdfContentByte.ALIGN_CENTER, "Consulta de Ordenes Filtrado por '"+this.c_filtro.getSelectedItem().toString()+"' ("+this.t_busca.getText()+")", 505, 537, 0);
                
                ord = (Orden)session.get(Orden.class, Integer.parseInt(orden)); 
                //************************datos de la orden****************************
                reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Orden:"+ord.getIdOrden(), 34, 735, 0);
                
                if(ord.getFecha()!=null)
                    reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Apertura:"+ord.getFecha(), 155, 735, 0);
                else
                    reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Apertura:", 155, 735, 0);

                String clien=ord.getClientes().getNombre();
                if(clien.length()>30)
                    clien=ord.getClientes().getNombre().substring(0, 30);
                reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Cliente:"+clien, 34, 725, 0);
                
                if(ord.getSiniestro()!=null)
                    reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Siniestro:"+ord.getSiniestro(), 34, 715, 0);
                else
                    reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Siniestro:", 34, 715, 0);
                
                if(ord.getFechaSiniestro()!=null)
                    reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "F. Siniestro:"+ord.getFechaSiniestro(), 155, 715, 0);
                else
                    reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "F.Siniestro:", 155, 715, 0);
                
                if(ord.getPoliza()!=null)
                    reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Poliza:"+ord.getPoliza(), 34, 705, 0);
                else
                    reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Poliza:", 34, 705, 0);
                
                if(ord.getInciso()!=null)
                    reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Inciso:"+ord.getInciso(), 155, 705, 0);
                else
                    reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Inciso:", 155, 705, 0);
                //**********************************************************
                
                //************datos de la unidad
                reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Unidad:"+ord.getTipo().getTipoNombre(), 255, 735, 0);
                reporte.contenido.showTextAligned(PdfContentByte.ALIGN_RIGHT, "Modelo:"+ord.getModelo(), 534, 735, 0);
                
                reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Marca:"+ord.getMarca().getMarcaNombre(), 255, 725, 0);
                if(ord.getNoEconomico()!=null)
                    reporte.contenido.showTextAligned(PdfContentByte.ALIGN_RIGHT, "Economico:"+ord.getNoEconomico(), 534, 725, 0);
                else
                    reporte.contenido.showTextAligned(PdfContentByte.ALIGN_RIGHT, "Economico:", 534, 725, 0);
                
                if(ord.getNoMotor()!=null)
                    reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "N° Motor:"+ord.getNoMotor(), 255, 715, 0);
                else
                    reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "N° Motor:", 255, 715, 0);
                
                if(ord.getNoSerie()!=null)
                    reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "N° Serie:"+ord.getNoSerie(), 255, 705, 0);
                else
                    reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "N° Serie:", 255, 705, 0);
                reporte.contenido.showTextAligned(PdfContentByte.ALIGN_RIGHT, "Fecha:"+new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date()), 564, 705, 0);
                //*************************************************************
                
                /*try
                {*/
                    if(ord.getCompania().getFoto()!=null)
                    {
                        Ftp miFtp=new Ftp();
                        boolean respuesta=true;
                        respuesta=miFtp.conectar(rutaFtp, "compras", "04650077", 3310);
                        if(respuesta==true)
                        {
                            miFtp.cambiarDirectorio("/compania");
                            String temporal=miFtp.descargaTemporal(ord.getCompania().getFoto());
                            miFtp.desconectar();
                            reporte.agregaObjeto(reporte.crearImagen(temporal, 455, -10, 13, true));
                        }
                    }
                //}catch(Exception e){}
                
            reporte.finTexto();
            //agregamos renglones vacios para dejar un espacio
            reporte.agregaObjeto(new Paragraph(" "));
            reporte.agregaObjeto(new Paragraph(" "));
            reporte.agregaObjeto(new Paragraph(" "));
            reporte.agregaObjeto(new Paragraph(" "));
            //reporte.agregaObjeto(new Paragraph(" "));
            
            Font font = new Font(Font.FontFamily.HELVETICA, 6, Font.BOLD);
            
            BaseColor cabecera=BaseColor.GRAY;
            BaseColor contenido=BaseColor.WHITE;
            int centro=Element.ALIGN_CENTER;
            int izquierda=Element.ALIGN_LEFT;
            int derecha=Element.ALIGN_RIGHT;
        
            tabla.addCell(reporte.celda("Cant", font, cabecera, centro, 0, 2, Rectangle.RECTANGLE));
            tabla.addCell(reporte.celda("Med", font, cabecera, centro, 0, 2,Rectangle.RECTANGLE));
            tabla.addCell(reporte.celda("Descripción", font, cabecera, centro, 0, 2,Rectangle.RECTANGLE));
            tabla.addCell(reporte.celda("D/M", font, cabecera, centro, 0,2, Rectangle.RECTANGLE));
            tabla.addCell(reporte.celda("Cam", font, cabecera, centro, 0,2, Rectangle.RECTANGLE));
            tabla.addCell(reporte.celda("Reparar(hr)", font, cabecera, centro, 3,1,Rectangle.RECTANGLE));
            tabla.addCell(reporte.celda("Pin", font, cabecera, centro, 0,2, Rectangle.RECTANGLE));
            tabla.addCell(reporte.celda("P", font, cabecera, centro, 0, 2, Rectangle.RECTANGLE));
            tabla.addCell(reporte.celda("Costo M.O.", font, cabecera, centro, 0, 2, Rectangle.RECTANGLE));
            
            tabla.addCell(reporte.celda("Min", font, cabecera, centro, 0,1, Rectangle.RECTANGLE));
            tabla.addCell(reporte.celda("Med", font, cabecera, centro, 0,1, Rectangle.RECTANGLE));
            tabla.addCell(reporte.celda("Max", font, cabecera, centro, 0,1, Rectangle.RECTANGLE));
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
    }
   
   public void cabecera1(PDF reporte, BaseFont bf, PdfPTable tabla, String cinta)
   {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try
        {
            reporte.contenido.setLineWidth(0.5f);
            reporte.contenido.setColorStroke(new GrayColor(0.2f));
            reporte.contenido.setColorFill(new GrayColor(0.9f));
            reporte.contenido.roundRectangle(30, 495, 210, 45, 5);
            reporte.contenido.roundRectangle(250, 495, 290, 45, 5);
            reporte.contenido.roundRectangle(550, 495, 210, 45, 5);
            
            if(foto==0)
            {
                reporte.agregaObjeto(reporte.crearImagen("imagenes/"+cinta, 27, -32, 50, true));
            }
            else
                reporte.agregaObjeto(reporte.crearImagen("imagenes/imperia1.jpg", 30, -40, 60, true));

            reporte.inicioTexto();
            reporte.contenido.setFontAndSize(bf, 8);
            reporte.contenido.setColorFill(BaseColor.BLACK);
            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "COTIZACIÓN DE REFACCIONES", 595, 552, 0);
            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Especialidad: "+this.cb_tipo.getSelectedItem().toString(), 595, 542, 0);
            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_RIGHT, "Fecha:"+new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date()), 760, 590, 0);
        
                
            ord = (Orden)session.get(Orden.class, Integer.parseInt(orden)); 
            //************************datos de la orden****************************
            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Orden:"+ord.getIdOrden(), 34, 530, 0);

            if(ord.getFecha()!=null)
                reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Apertura:"+ord.getFecha(), 155, 530, 0);
            else
                reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Apertura:", 155, 530, 0);

            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Compañia:"+ord.getCompania().getIdCompania()+" "+ord.getCompania().getNombre(), 34, 520, 0);

            if(ord.getSiniestro()!=null)
                reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Siniestro:"+ord.getSiniestro(), 34, 510, 0);
            else
                reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Siniestro:", 34, 510, 0);

            if(ord.getFechaSiniestro()!=null)
                reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "F. Siniestro:"+ord.getFechaSiniestro(), 155, 510, 0);
            else
                reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "F.Siniestro:", 155, 510, 0);

            if(ord.getPoliza()!=null)
                reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Poliza:"+ord.getPoliza(), 34, 500, 0);
            else
                reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Poliza:", 34, 500, 0);

            if(ord.getInciso()!=null)
                reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Inciso:"+ord.getInciso(), 155, 500, 0);
            else
                reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Inciso:", 155, 500, 0);
            //**********************************************************

            //************datos de la unidad
            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Unidad:"+ord.getTipo().getTipoNombre(), 255, 530, 0);
            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_RIGHT, "Modelo:"+ord.getModelo(), 534, 530, 0);

            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Marca:"+ord.getMarca().getMarcaNombre(), 255, 520, 0);
            if(ord.getNoEconomico()!=null)
                reporte.contenido.showTextAligned(PdfContentByte.ALIGN_RIGHT, "Economico:"+ord.getNoEconomico(), 534, 520, 0);
            else
                reporte.contenido.showTextAligned(PdfContentByte.ALIGN_RIGHT, "Economico:", 534, 520, 0);

            if(ord.getNoMotor()!=null)
                reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "N° Motor:"+ord.getNoMotor(), 255, 510, 0);
            else
                reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "N° Motor:", 255, 510, 0);

            if(ord.getNoSerie()!=null)
                reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "N° Serie:"+ord.getNoSerie(), 255, 500, 0);
            else
                reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "N° Serie:", 255, 500, 0);
            //*************************************************************

            //****************Datos del valuador
            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Valuador:", 555, 530, 0);
            if(ord.getEmpleadoByRLevantamiento()!=null)
                reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, ord.getEmpleadoByRLevantamiento().getNombre(), 555, 520, 0);
            if(ord.getFechaTaller()!=null)
                reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Fecha Entrega:"+ord.getFechaTaller(), 555, 510, 0);
            else
                reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Fecha Entrega:  //", 555, 510, 0);
            if(ord.getFechaCierre()!=null)
                reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Cerrado:"+ord.getFechaCierre(), 555, 500, 0);
            else
                reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Cerrado:  //", 555, 500, 0);
                
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
        
            tabla.addCell(reporte.celda("N°", font, cabecera, centro, 0, 1, Rectangle.RECTANGLE));
            tabla.addCell(reporte.celda("#", font, cabecera, centro, 0, 1,Rectangle.RECTANGLE));
            tabla.addCell(reporte.celda("Codigo", font, cabecera, centro, 0,1, Rectangle.RECTANGLE));
            tabla.addCell(reporte.celda("Descripción", font, cabecera, centro, 0, 1,Rectangle.RECTANGLE));
            tabla.addCell(reporte.celda("Cant", font, cabecera, centro, 0,1, Rectangle.RECTANGLE));
            tabla.addCell(reporte.celda("Med", font, cabecera, centro, 0,1, Rectangle.RECTANGLE));
            if(this.cb_precio.getSelectedItem().toString().compareToIgnoreCase("Compra")==0)
            {
                tabla.addCell(reporte.celda("Cotizado", font, cabecera, centro, 0,1, Rectangle.RECTANGLE));
                tabla.addCell(reporte.celda("Costo compra", font, cabecera, centro, 0,1, Rectangle.RECTANGLE));
                tabla.addCell(reporte.celda("Autorizado", font, cabecera, centro, 0, 1, Rectangle.RECTANGLE));
                tabla.addCell(reporte.celda("Utilidad", font, cabecera, centro, 0,1, Rectangle.RECTANGLE));
            }
            else
            {
                tabla.addCell(reporte.celda("Precio c/u", font, cabecera, centro, 0,1, Rectangle.RECTANGLE));
                tabla.addCell(reporte.celda("Total", font, cabecera, centro, 0,1, Rectangle.RECTANGLE));
                tabla.addCell(reporte.celda("Autorizado", font, cabecera, centro, 0, 1, Rectangle.RECTANGLE));
                tabla.addCell(reporte.celda("Ori", font, cabecera, centro, 0,1, Rectangle.RECTANGLE));
            }
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
    }
   
   public void cabecera2(PDF reporte, BaseFont bf, PdfPTable tabla, String tipo)
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
            //reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Refacciones de Operaciones", 160, 570, 0);
            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Autorizacion de Operaciones ("+this.cb_tipo.getSelectedItem().toString()+tipo+")", 160, 570, 0);
            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_RIGHT, "Fecha:"+new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date()), 760, 580, 0);

                    ord = (Orden)session.get(Orden.class, Integer.parseInt(orden)); 
                    Foto foto = (Foto)session.createCriteria(Foto.class).add(Restrictions.eq("orden.idOrden", Integer.parseInt(orden))).addOrder(Order.desc("fecha")).setMaxResults(1).uniqueResult();
                    if(foto!=null)
                    {
                        Ftp miFtp=new Ftp();
                        boolean respuesta=true;
                        respuesta=miFtp.conectar(rutaFtp, "compras", "04650077", 3310);
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
                    
                    if(ord.getClientes()!=null)
                    {
                        String val=ord.getClientes().getNombre();
                        if(ord.getClientes().getNombre().length()>38)
                            val=ord.getClientes().getNombre().substring(0, 37);
                        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Cliente:"+val, 525, 520, 0);
                    }
                    else
                        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Cliente:", 525, 520, 0);
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

            tabla.addCell(reporte.celda("No", font, cabecera, centro, 1, 3, Rectangle.RECTANGLE));
            tabla.addCell(reporte.celda("#", font, cabecera, centro, 1, 3, Rectangle.RECTANGLE));
            tabla.addCell(reporte.celda("Especialidad", font, cabecera, centro, 4, 1, Rectangle.RECTANGLE));
            tabla.addCell(reporte.celda("Cantidad", font, cabecera, centro, 2, 1,Rectangle.RECTANGLE));
            tabla.addCell(reporte.celda("Med", font, cabecera, centro, 1,3, Rectangle.RECTANGLE));
            tabla.addCell(reporte.celda("DM", font, cabecera, centro, 1,3, Rectangle.RECTANGLE));
            tabla.addCell(reporte.celda("Cam", font, cabecera, centro, 1,3,Rectangle.RECTANGLE));
            tabla.addCell(reporte.celda("Instruccion Final", font, cabecera, centro, 6,1, Rectangle.RECTANGLE));
            tabla.addCell(reporte.celda("D e s c r i p c i o n", font, cabecera, centro, 1, 3, Rectangle.RECTANGLE));
            if(cb_precio.getSelectedItem().toString().compareTo("Seleccionar")!=0)
            {
                tabla.addCell(reporte.celda("Inscruccion", font, cabecera, centro, 1, 3, Rectangle.RECTANGLE));
                tabla.addCell(reporte.celda("P", font, cabecera, centro, 1, 3, Rectangle.RECTANGLE));
                if(tipo.contains("Reparaciones")!=true)
                    tabla.addCell(reporte.celda(cb_precio.getSelectedItem().toString(), font, cabecera, centro, 1, 3, Rectangle.RECTANGLE));
            }
            else
            {
                tabla.addCell(reporte.celda("Inscruccion", font, cabecera, centro, 1, 3, Rectangle.RECTANGLE));
                tabla.addCell(reporte.celda("P", font, cabecera, centro, 1, 3, Rectangle.RECTANGLE));
            }

            tabla.addCell(reporte.celda("Hoj", font, cabecera, centro, 1, 2, Rectangle.RECTANGLE));
            tabla.addCell(reporte.celda("Mec", font, cabecera, centro, 1, 2, Rectangle.RECTANGLE));
            tabla.addCell(reporte.celda("Sus", font, cabecera, centro, 1, 2, Rectangle.RECTANGLE));
            tabla.addCell(reporte.celda("Ele", font, cabecera, centro, 1, 2, Rectangle.RECTANGLE));
            tabla.addCell(reporte.celda("Val", font, cabecera, centro, 1, 2, Rectangle.RECTANGLE));
            tabla.addCell(reporte.celda("Aut", font, cabecera, centro, 1, 2, Rectangle.RECTANGLE));
            tabla.addCell(reporte.celda("Reparacion", font, cabecera, centro, 3, 1, Rectangle.RECTANGLE));
            tabla.addCell(reporte.celda("Pintura", font, cabecera, centro, 3, 1, Rectangle.RECTANGLE));

            tabla.addCell(reporte.celda("Min", font, cabecera, centro, 1, 1, Rectangle.RECTANGLE));
            tabla.addCell(reporte.celda("Men", font, cabecera, centro, 1, 1, Rectangle.RECTANGLE));
            tabla.addCell(reporte.celda("Max", font, cabecera, centro, 1, 1, Rectangle.RECTANGLE));
            tabla.addCell(reporte.celda("Min", font, cabecera, centro, 1, 1, Rectangle.RECTANGLE));
            tabla.addCell(reporte.celda("Men", font, cabecera, centro, 1, 1, Rectangle.RECTANGLE));
            tabla.addCell(reporte.celda("Max", font, cabecera, centro, 1, 1, Rectangle.RECTANGLE));
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
    }
   
   public void cabecera3(PDF reporte, BaseFont bf, PdfPTable tabla, String cinta)
   {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try
        {
            reporte.contenido.setLineWidth(0.5f);
            reporte.contenido.setColorStroke(new GrayColor(0.2f));
            reporte.contenido.setColorFill(new GrayColor(0.9f));
            reporte.contenido.roundRectangle(30, 675, 220, 45, 5);
            reporte.contenido.roundRectangle(260, 675, 315, 45, 5);
            //reporte.contenido.roundRectangle(550, 495, 210, 45, 5);
            
            if(foto==0)
            {
                reporte.agregaObjeto(reporte.crearImagen("imagenes/"+cinta, 10, -32, 50, true));
            }
            else
                reporte.agregaObjeto(reporte.crearImagen("imagenes/imperia1.jpg", 10, -40, 60, true));

            reporte.inicioTexto();
            reporte.contenido.setFontAndSize(bf, 8);
            reporte.contenido.setColorFill(BaseColor.BLACK);
            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_RIGHT, "Fecha:"+new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date()), 570, 760, 0);
        
                
            ord = (Orden)session.get(Orden.class, Integer.parseInt(orden)); 
            //************************datos de la orden****************************
            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Orden:"+ord.getIdOrden(), 34, 710, 0);

            if(ord.getFecha()!=null)
                reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Apertura:"+ord.getFecha(), 155, 710, 0);
            else
                reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Apertura:", 155, 710, 0);

            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Cliente:"+ord.getClientes().getNombre(), 34, 700, 0);

            if(ord.getSiniestro()!=null)
                reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Siniestro:"+ord.getSiniestro(), 34, 690, 0);
            else
                reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Siniestro:", 34, 690, 0);

            if(ord.getFechaSiniestro()!=null)
                reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "F. Siniestro:"+ord.getFechaSiniestro(), 155, 690, 0);
            else
                reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "F.Siniestro:", 155, 690, 0);

            if(ord.getPoliza()!=null)
                reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Poliza:"+ord.getPoliza(), 34, 680, 0);
            else
                reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Poliza:", 34, 680, 0);

            if(ord.getInciso()!=null)
                reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Inciso:"+ord.getInciso(), 155, 680, 0);
            else
                reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Inciso:", 155, 680, 0);
            //**********************************************************

            //************datos de la unidad
            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Unidad:"+ord.getTipo().getTipoNombre(), 263, 710, 0);
            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_RIGHT, "Modelo:"+ord.getModelo(), 534, 710, 0);

            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Marca:"+ord.getMarca().getMarcaNombre(), 263, 700, 0);
            if(ord.getNoEconomico()!=null)
                reporte.contenido.showTextAligned(PdfContentByte.ALIGN_RIGHT, "Economico:"+ord.getNoEconomico(), 534, 700, 0);
            else
                reporte.contenido.showTextAligned(PdfContentByte.ALIGN_RIGHT, "Economico:", 534, 700, 0);

            if(ord.getNoMotor()!=null)
                reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "N° Motor:"+ord.getNoMotor(), 263, 690, 0);
            else
                reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "N° Motor:", 263, 690, 0);

            if(ord.getNoSerie()!=null)
                reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "N° Serie:"+ord.getNoSerie(), 263, 680, 0);
            else
                reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "N° Serie:", 263, 680, 0);
            //*************************************************************

                
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
        
            tabla.addCell(reporte.celda("Descripción", font, cabecera, centro, 0, 1, Rectangle.RECTANGLE));
            tabla.addCell(reporte.celda("Cant", font, cabecera, centro, 0, 1,Rectangle.RECTANGLE));
            tabla.addCell(reporte.celda("Med", font, cabecera, centro, 0, 1,Rectangle.RECTANGLE));
            tabla.addCell(reporte.celda("Precio C/U", font, cabecera, centro, 0, 1,Rectangle.RECTANGLE));
            tabla.addCell(reporte.celda("Total", font, cabecera, centro, 0, 1,Rectangle.RECTANGLE));
            
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
    }
   Object [] seleccion(int columna)
   {
       int [] selec=t_datos.getSelectedRows();
       Object [] seleccion =new Object[selec.length];
       for(int x=0; x<seleccion.length; x++)
       {
           seleccion[x]=Integer.parseInt(t_datos.getValueAt(selec[x], columna).toString());
       }
       return seleccion;
   }
   Object [] concatena()
   {
       int [] selec=t_datos.getSelectedRows();
       Object [] seleccion =new Object[selec.length];
       for(int x=0; x<seleccion.length; x++)
       {
           seleccion[x]=t_datos.getValueAt(selec[x], 0).toString()+t_datos.getValueAt(selec[x], 0).toString();
       }
       return seleccion;
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
