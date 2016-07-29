/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Compras;

import Ejemplar.altaEjemplar;
import Hibernate.Util.HibernateUtil;
import Hibernate.entidades.Almacen;
import Hibernate.entidades.Configuracion;
import Hibernate.entidades.Ejemplar;
import Hibernate.entidades.Orden;
import Hibernate.entidades.Partida;
import Hibernate.entidades.PartidaExterna;
import Hibernate.entidades.Pedido;
import Hibernate.entidades.Proveedor;
import Hibernate.entidades.Usuario;
import Integral.VerticalBarUI;
import Proveedor.buscaProveedor;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import org.hibernate.Criteria;
import org.hibernate.Query;
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
import Integral.Render1;
import Integral.VerticalTableHeaderCellRenderer;
import Integral.calendario;
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
import java.awt.Desktop;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Properties;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JViewport;
import javax.swing.KeyStroke;
import javax.swing.event.ListSelectionEvent;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellRangeAddress;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Picture;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.util.IOUtils;
import org.hibernate.HibernateException;

/**
 *
 * @author I.S.C Salvador
 */
public class altaCompras extends javax.swing.JPanel {

    JViewport viewport = new JViewport();
    private String orden;
    private Usuario user;
    private Usuario usrAut, usrAut1, usrAut2;
    DefaultTableModel modeloPedidos;
    String sessionPrograma="";
    Herramientas h;
    Formatos f1;
    editaPedido editaPedido;
    boolean habilita=false, boton=false;
    String[] columnas1 = new String [] {
        "No","#","Descripcion","M"
    };
    String[] columnas = new String [] {
        "Grupo",
        "Esp Hoj","Esp Mec","Esp Sus","Esp Ele", 
        "Camb","Pin","Can","Med","Fol","Codigo",
        "Costo c/u","%","$ Cia. Seg","Can Aut","$ Aut. c/u","$ Aut. Tot",
        /*"% Util",*/"Aut",
        
        "R.Cot","R.Com",
        
        "Ori","Inst",
        
        "Prov.","Cant C.","$C/U Comp","Entrega","Pedido","OK", "OP", "ASIGNO"};
    
    Class[] types = new Class [] {
                    java.lang.String.class/*Grupo*/, 
                    
                    java.lang.Boolean.class/*Hoj*/, 
                    java.lang.Boolean.class/*Mec*/, 
                    java.lang.Boolean.class/*Susp*/, 
                    java.lang.Boolean.class/*Ele*/, 

                    java.lang.Boolean.class/*Cam*/, 
                    java.lang.Boolean.class/*Pin*/, 
                    java.lang.Double.class/*Can*/, 
                    java.lang.String.class/*Med*/, 
                    java.lang.String.class/*Fol*/, 
                    java.lang.String.class/*Cod*/, 
                    
                    java.lang.Double.class/*Costo c/u*/,
                    java.lang.Double.class/*%*/, 
                    java.lang.Double.class/*cia seguroa*/, 
                    java.lang.Double.class/*cant autorizada*/, 
                    java.lang.Double.class/*$ aut c/u*/, 
                    java.lang.Double.class,/*$ aut tot*/
                    //java.lang.Double.class/*% de util*/, 
                    
                    java.lang.Boolean.class/*autorizado*/, 

                    java.lang.Boolean.class/*R cot*/,
                    java.lang.Boolean.class/*R com*/,
                    
                    java.lang.String.class/*Ori*/,
                    java.lang.String.class/*Ins*/,
                    
                    java.lang.String.class/*Proveedor*/, 
                    java.lang.Double.class/*Cant C.*/, 
                    java.lang.Double.class/*c/u* Com*/,
                    java.lang.String.class/*Plazo*/, 
                    java.lang.Integer.class/*Pedido*/, 
                    java.lang.Boolean.class/*OK*/,
                    java.lang.Boolean.class/*T*/,
                    java.lang.String.class/*Asigno*/, 
                };
    
    Class[] types1 = new Class [] {
                    java.lang.String.class/*No*/, 
                    java.lang.String.class/*#*/, 
                    java.lang.String.class/*Descripcion*/, 
                    java.lang.Boolean.class/*OP*/
                };
    //private Session session;
    
    String[] colPedidos = new String [] {
        "No°","Proveedor","Fecha","Tipo"};
    
    Orden ord;
    FormatoTabla formato;
    MyModel model, model1;
    String estado;
    int entro=0, x=0;
    List noPartida;
    List pedidos;
    List id_pedido;
    /**
     * Creates ew form altaCompras
     */
    public altaCompras(String ord, Usuario us, String edo, String ses) {
        initComponents();
        viewport.setView(t_titulos);
        viewport.setPreferredSize(new Dimension(445, t_titulos.getPreferredSize().height));
        //scroll.setRowHeaderView(viewport);
        scroll.setCorner(JScrollPane.UPPER_LEFT_CORNER, t_titulos.getTableHeader());
        formatoTitulos();
        estado=edo;
        sessionPrograma=ses;
        orden=ord;
        user=us;
        model=new MyModel(0, columnas, this.types);
        t_datos.setModel(model);
        model1=new MyModel(0, columnas1, this.types1);
        t_titulos.setModel(model1);
        t_datos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        t_titulos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        t_datos.getSelectionModel().addListSelectionListener(
                new javax.swing.event.ListSelectionListener() 
                {
                    public void valueChanged(ListSelectionEvent e) 
                    {
                        t_datos.valueChanged(e);
                        checkSelection(false);
                    }
                }
        );
        t_titulos.getSelectionModel().addListSelectionListener(
                new javax.swing.event.ListSelectionListener() 
                {
                    public void valueChanged(ListSelectionEvent e) 
                    {
                        t_titulos.valueChanged(e);
                        checkSelection(true);
                    }
                }
        );
        t_titulos.getTableHeader().addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                viewport.setPreferredSize(t_titulos.getSize());
                viewport.updateUI();
            }
        });
        t_datos.getTableHeader().addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                viewport.setPreferredSize(t_titulos.getSize());
                viewport.updateUI();
            }
        });
        formatoTabla();
        formato = new FormatoTabla();
        buscaCuentas(-1, -1);
        if(edo.compareTo("")!=0)
        {
            h=new Herramientas(user, 0);
            if(h.isCerrada(orden)==true)
                visualiza(false, 1);
            else
                visualiza(false, 0);
        }
        else
        {
            h=new Herramientas(user, 0);
            if(h.isCerrada(orden)==true)
                visualiza(false, 1);
            else
                visualiza(true, 0);
        }
        formatoPedidos();
        scroll.getVerticalScrollBar().setUI(new VerticalBarUI());
        scroll.getHorizontalScrollBar().setUI(new HorizontalBarUI());
    }

    public void formatoTitulos()
    {
        TableCellRenderer textNormal = new DefaultTableHeaderCellRenderer();
        for(int x=0; x<t_titulos.getColumnModel().getColumnCount(); x++)
            t_titulos.getColumnModel().getColumn(x).setHeaderRenderer(textNormal);
        tabla_tamaños_titulos();
        t_titulos.setShowVerticalLines(true);
        t_titulos.setShowHorizontalLines(true);
        t_titulos.setDefaultRenderer(String.class, formato); 
        t_titulos.setDefaultRenderer(Integer.class, formato);
        t_titulos.setDefaultRenderer(Boolean.class, formato);
    }
    
    public void tabla_tamaños_titulos()
   {
        TableColumnModel col_model = t_titulos.getColumnModel();
        DefaultTableCellRenderer tcr = new DefaultTableCellRenderer();
        tcr.setHorizontalAlignment(SwingConstants.RIGHT);

        for (int i=0; i<t_titulos.getColumnCount(); i++)
        {
  	      TableColumn column = col_model.getColumn(i);
              switch(i)
              {
                  case 0://N°
                      column.setPreferredWidth(55);
                      break;
                  case 1://#
                      column.setPreferredWidth(40);
                      break;
                  case 2://descripcion
                      column.setPreferredWidth(335);
                      break;
                  case 3://Muestra
                      column.setPreferredWidth(10);
                      break;
                  default:
                      column.setPreferredWidth(17);
                      break;
              }
        }
        JTableHeader header = t_titulos.getTableHeader();
        header.setBackground(new java.awt.Color(2, 135, 242));//102,102,102
        header.setForeground(Color.white);
   }
    public void formatoTabla()
    {
        TableCellRenderer textNormal = new DefaultTableHeaderCellRenderer();        
        TableCellRenderer headerRenderer = new VerticalTableHeaderCellRenderer();
        Enumeration columns = t_datos.getColumnModel().getColumns();
        
        for(int x=0; x<t_datos.getColumnModel().getColumnCount(); x++)
        {
            switch(t_datos.getColumnName(x))
            {
                case "Esp Hoj":
                    t_datos.getColumnModel().getColumn(x).setHeaderRenderer(headerRenderer);
                    break;
                case "Esp Mec":
                    t_datos.getColumnModel().getColumn(x).setHeaderRenderer(headerRenderer);
                    break;
                case "Esp Sus":
                    t_datos.getColumnModel().getColumn(x).setHeaderRenderer(headerRenderer);
                    break;
                case "Esp Ele":
                    t_datos.getColumnModel().getColumn(x).setHeaderRenderer(headerRenderer);
                    break;
                case "Camb":
                    t_datos.getColumnModel().getColumn(x).setHeaderRenderer(headerRenderer);
                    break;
                case "Pin":
                    t_datos.getColumnModel().getColumn(x).setHeaderRenderer(headerRenderer);
                    break;
                case "Aut":
                    t_datos.getColumnModel().getColumn(x).setHeaderRenderer(headerRenderer);
                    break;
                case "R.Cot":
                    t_datos.getColumnModel().getColumn(x).setHeaderRenderer(headerRenderer);
                    break;
                case "R.Com":
                    t_datos.getColumnModel().getColumn(x).setHeaderRenderer(headerRenderer);
                    break;
                default:
                    t_datos.getColumnModel().getColumn(x).setHeaderRenderer(textNormal);
                    break;
            }
            /*if((x>0 && x<7) || (x>16 && x<20))
                t_datos.getColumnModel().getColumn(x).setHeaderRenderer(headerRenderer);
            else
                t_datos.getColumnModel().getColumn(x).setHeaderRenderer(textNormal);*/
        }
        tabla_tamaños();
        t_datos.setShowVerticalLines(true);
        t_datos.setShowHorizontalLines(true);
        
        t_datos.setDefaultRenderer(String.class, formato); 
        t_datos.setDefaultRenderer(Double.class, formato); 
        t_datos.setDefaultRenderer(Integer.class, formato);
        t_datos.setDefaultRenderer(Boolean.class, formato);
        
        FormatoEditor fe=new FormatoEditor();
        t_datos.setDefaultEditor(Double.class, fe);
    }
    
    public void formatoPedidos()
    {
        Color c1 = new java.awt.Color(2, 135, 242);
        for(int x=0; x<t_datos2.getColumnModel().getColumnCount(); x++)
        {
                t_datos2.getColumnModel().getColumn(x).setHeaderRenderer(new Render1(c1));
        }
        
        for(int x=0; x<t_pedidos.getColumnModel().getColumnCount(); x++)
        {
                t_pedidos.getColumnModel().getColumn(x).setHeaderRenderer(new Render1(c1));
        }
        tabla_tamañosPedidos();
        t_datos2.setShowVerticalLines(true);
        t_datos2.setShowHorizontalLines(true);
        t_pedidos.setShowVerticalLines(true);
        t_pedidos.setShowHorizontalLines(true);
        t_datos.setDefaultRenderer(String.class, formato); 
        t_datos2.setDefaultRenderer(Double.class, formato); 
        t_pedidos.setDefaultRenderer(Integer.class, formato);
        t_pedidos.setDefaultRenderer(Boolean.class, formato);
        //buscaCuentas(-1, -1);
    }
    public void tabla_tamañosPedidos()
   {
        TableColumnModel col_model = t_datos2.getColumnModel();
        TableColumnModel col_model1 = t_pedidos.getColumnModel();
        DefaultTableCellRenderer tcr = new DefaultTableCellRenderer();
        tcr.setHorizontalAlignment(SwingConstants.RIGHT);

        for (int i=0; i<t_datos2.getColumnCount(); i++)
        {
  	      TableColumn column = col_model.getColumn(i);
              switch(i)
              {
                  case 0://N°
                      column.setPreferredWidth(20);
                      //column.setCellRenderer(tcr);
                      break;

                  case 1:
                      column.setPreferredWidth(80);
                      break;
                  
                  case 2:
                      column.setPreferredWidth(80);;
                      break;
                      
                  default:
                      column.setPreferredWidth(17);
                      break;
              }
        }
        
        for (int i=0; i<t_pedidos.getColumnCount(); i++)
        {
              TableColumn column1 = col_model1.getColumn(i);
              switch(i)
              {
                  case 0://N°
                      column1.setPreferredWidth(20);
                      //column1.setCellRenderer(tcr);
                      break;

                  case 1:
                      column1.setPreferredWidth(80);
                      break;
                  
                  case 2:
                      column1.setPreferredWidth(80);
                      break;
                      
                  default:
                      column1.setPreferredWidth(17);
                      break;
              }
        }
        JTableHeader header = t_datos2.getTableHeader();
        header.setForeground(Color.white);
        
        JTableHeader header1 = t_pedidos.getTableHeader();
        header1.setForeground(Color.white);
   }
    public void tabla_tamaños()
   {
        TableColumnModel col_model = t_datos.getColumnModel();
        DefaultTableCellRenderer tcr = new DefaultTableCellRenderer();
        tcr.setHorizontalAlignment(SwingConstants.RIGHT);

        for (int i=0; i<t_datos.getColumnCount(); i++)
        {
  	      TableColumn column = col_model.getColumn(i);
              switch(t_datos.getColumnName(i))
              {
                  case "Grupo"://Grupo
                      column.setPreferredWidth(100);
                      break;
                  case "Can"://cant
                      column.setPreferredWidth(45);
                      break;
                  case "Med"://medida
                      column.setPreferredWidth(50);
                      DefaultCellEditor editor = new DefaultCellEditor(medida);
                      column.setCellEditor(editor); 
                      editor.setClickCountToStart(2);
                      break;
                  case "Fol"://folio
                      column.setPreferredWidth(40);
                      break;
                      
                  case "Codigo"://codigo
                      column.setPreferredWidth(100);
                      break;

                  case "Costo c/u"://Costo c/u
                      column.setPreferredWidth(75);
                      break;
                  
                  case "%"://%
                      column.setPreferredWidth(40);
                      break;
                      
                  case "$ Cia. Seg"://precio cia seg
                      column.setPreferredWidth(75);
                      break;

                  case "Can Aut"://cant autorizada
                      column.setPreferredWidth(50);
                      break;
                      
                  case "$ Aut. c/u"://precio aut c/u
                      column.setPreferredWidth(75);
                      break;
                      
                  case "$ Aut. Tot"://precio aut tot
                      column.setPreferredWidth(75);
                      break;

                  case "Ori"://ori
                      column.setPreferredWidth(50);
                      break;
                  
                  case "Inst"://inst
                      column.setPreferredWidth(150);
                      break;
                  case "Prov."://prov
                      column.setPreferredWidth(45);
                      break;    
                  case "Cant C."://cant c
                      column.setPreferredWidth(45);
                      break;
                  case "$C/U Comp"://c/u comp
                      column.setPreferredWidth(75);
                      break;
                  case "Entrega"://Plazo
                      column.setPreferredWidth(80);
                      break;
                 case "Pedido"://PEDIDO
                      column.setPreferredWidth(45);
                      break;
                 case "ASIGNO"://asigno
                      column.setPreferredWidth(80);
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
    
    private void buscaCuentas(int x, int y)
    {
        double imp=0.0;
        if(orden!=null)
        {
            Session session = HibernateUtil.getSessionFactory().openSession();
            try
            {
                session.beginTransaction().begin();
                ord = (Orden)session.get(Orden.class, Integer.parseInt(orden));
                
                if(ord.getCierreRefacciones()!=null)
                    this.r_cerrar.setSelected(true);
                else
                    this.r_cerrar.setSelected(false);
                
                imp=ord.getCompania().getImporteHora();
                t_importe1.setValue(ord.getVales());
                
                Query query=session.createSQLQuery("select partida.id_partida, partida.id_evaluacion, partida.sub_partida, catalogo.nombre, partida.muestra, especialidad.descripcion, partida.esp_hoj, partida.esp_mec, partida.esp_sus, partida.esp_ele, " +
                                                "if(partida.cam =-1, false, true) as cam, if(partida.pint=-1, false, true) as pint, partida.cant, partida.med, partida.id_catalogo, if(partida.id_parte is null, '', partida.id_parte) as id_parte, " +
                                                "partida.c_u, partida.porcentaje, if(partida.porcentaje=0, partida.c_u, partida.c_u/(1-(partida.porcentaje*0.01))) as cia, partida.Cantidad_aut, partida.Precio_aut_c_u, round((partida.Cantidad_aut*partida.Precio_aut_c_u),2) as aut_tot, " +
                                                "partida.autorizado, partida.ref_coti, partida.ref_comp, if(partida.ori=true, 'ORI', if(partida.nal=true, 'NAL', if(partida.desm=true, 'DES', if(partida.pd=true, 'RECON', '')))) AS ori, " +
                                                "if(partida.Instruccion is null, '', partida.Instruccion) as instruccion, if(partida.id_pedido is null, '', pedido.id_proveedor) as proveedor, partida.cant_pcp, partida.pcp, " +
                                                "if(partida.plazo is null, 0, partida.plazo) as plazo, if(partida.id_pedido is null, '', partida.id_pedido) as id_pedido, partida.surtido, partida.op, if(partida.mecanico is null, '', partida.mecanico) as mecanico " +
                                                "from partida left join catalogo on catalogo.id_catalogo=partida.id_catalogo " +
                                                "left join especialidad on especialidad.id_grupo_mecanico=catalogo.id_grupo_mecanico " +
                                                "left join pedido on pedido.id_pedido=partida.id_pedido " +
                                                "where partida.id_orden="+ord.getIdOrden()+" and partida.ref_comp=true and partida.autorizado_valuacion=true and partida.autorizado=true order by partida.id_evaluacion, partida.sub_partida asc;");
                query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
                ArrayList partidas=(ArrayList)query.list();
                if(partidas.size()>0)
                {
                    habilita=false;
                    model=new MyModel(partidas.size(), columnas, this.types);
                    t_datos.setModel(model);
                    model1=new MyModel(partidas.size(), columnas1, this.types1);
                    t_titulos.setModel(model1);
                    t_datos.getSelectionModel().addListSelectionListener(
                            new javax.swing.event.ListSelectionListener() 
                            {
                                public void valueChanged(ListSelectionEvent e) 
                                {
                                    checkSelection(false);
                                }
                            }
                    );
                    t_titulos.getSelectionModel().addListSelectionListener(
                            new javax.swing.event.ListSelectionListener() 
                            {
                                public void valueChanged(ListSelectionEvent e) 
                                {
                                    checkSelection(true);
                                }
                            }
                    );
                    noPartida=new ArrayList();
                    for(int i=0; i<partidas.size(); i++)
                    {
                        java.util.HashMap map=(java.util.HashMap)partidas.get(i);
                        noPartida.add(map.get("id_partida"));
                        model1.setValueAt(map.get("id_evaluacion"), i, 0);
                        model1.setCeldaEditable(i, 0, false);
                        model1.setValueAt(map.get("sub_partida"), i, 1);
                        model1.setCeldaEditable(i, 1, false);
                        model1.setValueAt(map.get("nombre"), i, 2);
                        model1.setCeldaEditable(i, 2, false);
                        model1.setValueAt(map.get("muestra"), i, 3);
                        model1.setCeldaEditable(i, 3, true);
                        
                        model.setValueAt(map.get("descripcion"), i, 0);
                        model.setCeldaEditable(i, 0, false);
                        model.setValueAt(map.get("esp_hoj"), i, 1);
                        model.setCeldaEditable(i, 1, false);
                        
                        model.setValueAt(map.get("esp_mec"), i, 2);
                        model.setCeldaEditable(i, 2, false);
                        
                        model.setValueAt(map.get("esp_sus"), i, 3);
                        model.setCeldaEditable(i, 3, false);
                        
                        model.setValueAt(map.get("esp_ele"), i, 4);
                        model.setCeldaEditable(i, 4, false);
                        if(Integer.parseInt(map.get("cam").toString())==1)
                            model.setValueAt(true, i, 5);
                        else
                            model.setValueAt(false, i, 5);
                        model.setCeldaEditable(i, 5, false);
                        
                        if(Integer.parseInt(map.get("pint").toString())==1)
                            model.setValueAt(true, i, 6);
                        else
                            model.setValueAt(false, i, 6);
                        model.setCeldaEditable(i, 6, false);
                        
                        model.setValueAt(map.get("cant"), i, 7);
                        model.setCeldaEditable(i, 7, false);
                        model.setValueAt(map.get("med"), i, 8);
                        model.setCeldaEditable(i, 8, false);
                        
                        model.setValueAt(map.get("id_catalogo"), i, 9);
                        model.setCeldaEditable(i, 9, false);
                        
                        model.setValueAt(map.get("id_parte"), i, 10);
                        
                        model.setValueAt(map.get("c_u"), i, 11);
                        model.setCeldaEditable(i, 11, false);
                        
                        model.setValueAt(map.get("porcentaje"), i, 12);
                        model.setCeldaEditable(i, 12, false);
                        
                        model.setValueAt(0.0+Math.round(Double.parseDouble(map.get("cia").toString())), i, 13);
                        //model.setValueAt(, i, 13);
                        model.setCeldaEditable(i, 13, false);
                        
                        model.setValueAt(map.get("Cantidad_aut"), i, 14);
                        model.setCeldaEditable(i, 14, false);
                        
                        model.setValueAt(map.get("Precio_aut_c_u"), i, 15);
                        model.setCeldaEditable(i, 15, false);
                        
                        model.setValueAt(map.get("aut_tot"), i, 16);
                        model.setCeldaEditable(i, 16, false);
                        
                        model.setValueAt(map.get("autorizado"), i, 17);
                        model.setCeldaEditable(i, 17, false);
                        
                        model.setValueAt(map.get("ref_coti"), i, 18);
                        model.setCeldaEditable(i, 18, false);
                        
                        model.setValueAt(map.get("ref_comp"), i, 19);
                        model.setCeldaEditable(i, 19, false);
                                                
                        model.setValueAt(map.get("ori"), i, 20);
                        model.setCeldaEditable(i, 20, false);
                        
                        model.setValueAt(map.get("instruccion"), i, 21);
                        model.setCeldaEditable(i, 21, false);
                        
                        model.setValueAt(map.get("proveedor"), i, 22);//proveedor
                        model.setValueAt(map.get("cant_pcp"), i, 23);//cant c
                        model.setValueAt(map.get("pcp"), i, 24);//C/U Com
                        
                        if(map.get("id_pedido").toString().compareTo("")!=0)
                        {
                            model.setCeldaEditable(i, 22, false);
                            model.setCeldaEditable(i, 23, false);
                            model.setCeldaEditable(i, 24, false);
                        }
                        
                        model.setValueAt(map.get("plazo"), i, 25);//plazo de entrega
                        model.setCeldaEditable(i, 25, false);
                        
                        model.setValueAt(map.get("id_pedido"), i, 26);//pedido
                        model.setCeldaEditable(i, 26, false);
                        
                        model.setValueAt(map.get("surtido"), i, 27);//surtido
                        model.setCeldaEditable(i, 27, true);
                        
                        model.setValueAt(map.get("op"), i, 28);//Operaciones
                        model.setCeldaEditable(i, 28, false);
                        
                        model.setValueAt(map.get("mecanico"), i, 29);
                        model.setCeldaEditable(i, 29, false);
                    }
                }
                else
                {
                    model=new MyModel(0, columnas, this.types);
                    t_datos.setModel(model);
                    model1=new MyModel(0, columnas1, this.types1);
                    t_titulos.setModel(model1);
                    t_datos.getSelectionModel().addListSelectionListener(
                        new javax.swing.event.ListSelectionListener() 
                        {
                            public void valueChanged(ListSelectionEvent e) 
                            {
                                checkSelection(false);
                            }
                        }
                    );
                    t_titulos.getSelectionModel().addListSelectionListener(
                        new javax.swing.event.ListSelectionListener() 
                        {
                            public void valueChanged(ListSelectionEvent e) 
                            {
                                checkSelection(true);
                            }
                        }
                    );
                }
                session.beginTransaction().rollback();
            }catch(Exception e)
            {
                e.printStackTrace();
            }
            finally
            {
                if(session.isOpen()==true)
                    session.close();
                if(this.user.getAutorizarPedidos()==false)
                {
                    t_datos.removeColumn(t_datos.getColumnModel().getColumn(16));
                    t_datos.removeColumn(t_datos.getColumnModel().getColumn(15));
                    t_datos.removeColumn(t_datos.getColumnModel().getColumn(13));
                    t_datos.removeColumn(t_datos.getColumnModel().getColumn(12));
                    t_datos.removeColumn(t_datos.getColumnModel().getColumn(11));
                }
            }
        }
        else
        {
            habilita=false;
            model=new MyModel(0, columnas, this.types);
            t_datos.setModel(model);
            model1=new MyModel(0, columnas1, this.types1);
            t_titulos.setModel(model1);
            t_datos.getSelectionModel().addListSelectionListener(
                new javax.swing.event.ListSelectionListener() 
                {
                    public void valueChanged(ListSelectionEvent e) 
                    {
                        //this.valueChanged(e);
                        checkSelection(false);
                    }
                }
            );
            t_titulos.getSelectionModel().addListSelectionListener(
                new javax.swing.event.ListSelectionListener() 
                {
                    public void valueChanged(ListSelectionEvent e) 
                    {
                        //this.valueChanged(e);
                        checkSelection(true);
                    }
                }
            );
        }
        formatoTabla();
        formatoTitulos();
        if(x>=0 && y>=0)
        {
            t_datos.setColumnSelectionInterval(y, y);
            t_datos.setRowSelectionInterval(x, x);
            
            t_titulos.setColumnSelectionInterval(y, y);
            t_titulos.setRowSelectionInterval(x, x);
        }
        habilita=true;
        sumaTotales();
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
        ventana = new javax.swing.JDialog();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        t_datos2 = new javax.swing.JTable();
        listaPedidos = new javax.swing.JDialog();
        jPanel4 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        t_pedidos = new javax.swing.JTable();
        jPanel5 = new javax.swing.JPanel();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton10 = new javax.swing.JButton();
        cb_tipo = new javax.swing.JComboBox();
        ventanaAdicional = new javax.swing.JDialog();
        jPanel6 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jButton9 = new javax.swing.JButton();
        jPanel7 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jPasswordField1 = new javax.swing.JPasswordField();
        jButton7 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jButton3 = new javax.swing.JButton();
        l4 = new javax.swing.JLabel();
        t_ref_adi = new javax.swing.JFormattedTextField();
        autorizarCosto = new javax.swing.JDialog();
        jPanel9 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        t_user = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        t_clave = new javax.swing.JPasswordField();
        b_1 = new javax.swing.JButton();
        jLabel14 = new javax.swing.JLabel();
        t_titulos = new javax.swing.JTable();
        autorizar1 = new javax.swing.JDialog();
        jPanel10 = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        t_user1 = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        t_clave1 = new javax.swing.JPasswordField();
        b2 = new javax.swing.JButton();
        jLabel17 = new javax.swing.JLabel();
        autorizar2 = new javax.swing.JDialog();
        jPanel11 = new javax.swing.JPanel();
        jLabel18 = new javax.swing.JLabel();
        t_user2 = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        t_clave2 = new javax.swing.JPasswordField();
        b3 = new javax.swing.JButton();
        jLabel20 = new javax.swing.JLabel();
        muestra = new javax.swing.JDialog();
        centro = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        t_costo_refacciones = new javax.swing.JFormattedTextField();
        jLabel5 = new javax.swing.JLabel();
        t_cia_seguros = new javax.swing.JFormattedTextField();
        jLabel6 = new javax.swing.JLabel();
        t_autorizado = new javax.swing.JFormattedTextField();
        jLabel13 = new javax.swing.JLabel();
        t_importe = new javax.swing.JFormattedTextField();
        jLabel1 = new javax.swing.JLabel();
        t_busca = new javax.swing.JTextField();
        b_busca = new javax.swing.JButton();
        b_tot = new javax.swing.JButton();
        r_cerrar = new javax.swing.JRadioButton();
        jLabel4 = new javax.swing.JLabel();
        t_empresa = new javax.swing.JTextField();
        b_busca1 = new javax.swing.JButton();
        b_muestra = new javax.swing.JButton();
        jButton11 = new javax.swing.JButton();
        jButton12 = new javax.swing.JButton();
        b_exel = new javax.swing.JButton();
        t_importe1 = new javax.swing.JFormattedTextField();
        jLabel21 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        scroll = new javax.swing.JScrollPane();
        t_datos = new javax.swing.JTable();

        numeros.setFont(new java.awt.Font("Dialog", 0, 9)); // NOI18N
        numeros.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                numerosMouseClicked(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                numerosMouseReleased(evt);
            }
        });
        numeros.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                numerosPopupMenuWillBecomeInvisible(evt);
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            }
        });
        numeros.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                numerosActionPerformed(evt);
            }
        });
        numeros.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                numerosFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                numerosFocusLost(evt);
            }
        });

        medida.setFont(new java.awt.Font("Dialog", 0, 9)); // NOI18N
        medida.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "PZAS", "LTS", "MTS", "CMS", "MMS", "GRS", "MLS", "KGS", "HRS", "MIN", "KIT", "FT", "LB", "JGO", "NA" }));

        ventana.setModal(true);
        ventana.setModalExclusionType(null);

        jPanel2.setBackground(new java.awt.Color(0, 153, 153));

        jLabel2.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(254, 254, 254));
        jLabel2.setText("Pedidos Generados");
        jPanel2.add(jLabel2);

        ventana.getContentPane().add(jPanel2, java.awt.BorderLayout.PAGE_START);

        jScrollPane1.setBackground(new java.awt.Color(255, 255, 255));

        t_datos2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "No°", "Proveedor", "Fecha"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        t_datos2.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(t_datos2);

        ventana.getContentPane().add(jScrollPane1, java.awt.BorderLayout.CENTER);

        listaPedidos.setModalExclusionType(null);
        listaPedidos.setModalityType(java.awt.Dialog.ModalityType.APPLICATION_MODAL);

        jPanel4.setBackground(new java.awt.Color(0, 153, 153));

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Relación de pedidos de la Unidad");
        jPanel4.add(jLabel7);

        listaPedidos.getContentPane().add(jPanel4, java.awt.BorderLayout.PAGE_START);

        t_pedidos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "No°", "Proveedor", "Fecha", "Tipo"
            }
        ));
        t_pedidos.getTableHeader().setReorderingAllowed(false);
        jScrollPane3.setViewportView(t_pedidos);

        listaPedidos.getContentPane().add(jScrollPane3, java.awt.BorderLayout.CENTER);

        jButton4.setBackground(new java.awt.Color(2, 135, 242));
        jButton4.setForeground(new java.awt.Color(255, 255, 255));
        jButton4.setIcon(new ImageIcon("imagenes/borra.png"));
        jButton4.setText("Cancelar");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        jPanel5.add(jButton4);

        jButton5.setBackground(new java.awt.Color(2, 135, 242));
        jButton5.setForeground(new java.awt.Color(255, 255, 255));
        jButton5.setIcon(new ImageIcon("imagenes/nuevo.png"));
        jButton5.setText("Pedido");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });
        jPanel5.add(jButton5);

        jButton6.setBackground(new java.awt.Color(2, 135, 242));
        jButton6.setForeground(new java.awt.Color(255, 255, 255));
        jButton6.setIcon(new ImageIcon("imagenes/nuevo.png"));
        jButton6.setText("Ord. Compra");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });
        jPanel5.add(jButton6);

        jButton10.setBackground(new java.awt.Color(2, 135, 242));
        jButton10.setForeground(new java.awt.Color(255, 255, 255));
        jButton10.setIcon(new ImageIcon("imagenes/nuevo.png"));
        jButton10.setText("DCG");
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });
        jPanel5.add(jButton10);

        cb_tipo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "ORDEN", "PEDIDO" }));
        jPanel5.add(cb_tipo);

        listaPedidos.getContentPane().add(jPanel5, java.awt.BorderLayout.PAGE_END);

        ventanaAdicional.setModalExclusionType(null);
        ventanaAdicional.setModalityType(java.awt.Dialog.ModalityType.APPLICATION_MODAL);

        jLabel8.setFont(new java.awt.Font("Arial", 3, 14)); // NOI18N
        jLabel8.setText("Agregar Partidas Adicionales");

        jButton9.setBackground(new java.awt.Color(2, 135, 242));
        jButton9.setForeground(new java.awt.Color(254, 254, 254));
        jButton9.setText("Agregar");
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 488, Short.MAX_VALUE)
                .addComponent(jButton9)
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel8)
                .addComponent(jButton9))
        );

        ventanaAdicional.getContentPane().add(jPanel6, java.awt.BorderLayout.PAGE_START);

        jPanel7.setBackground(new java.awt.Color(254, 254, 254));
        jPanel7.setLayout(new java.awt.BorderLayout());

        jLabel9.setText("Usuario:");

        jTextField1.setBackground(new java.awt.Color(204, 255, 255));

        jLabel10.setText("Contraseña:");

        jPasswordField1.setBackground(new java.awt.Color(204, 255, 255));

        jButton7.setBackground(new java.awt.Color(2, 135, 242));
        jButton7.setForeground(new java.awt.Color(254, 254, 254));
        jButton7.setText("Cancelar");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        jButton8.setBackground(new java.awt.Color(2, 135, 242));
        jButton8.setForeground(new java.awt.Color(254, 254, 254));
        jButton8.setText("Aceptar");
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(13, 13, 13)
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPasswordField1, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 242, Short.MAX_VALUE)
                .addComponent(jButton8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton7)
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jLabel9)
                .addComponent(jLabel10)
                .addComponent(jPasswordField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jButton7)
                .addComponent(jButton8))
        );

        jPanel7.add(jPanel8, java.awt.BorderLayout.PAGE_END);

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "folio", "Descripción", "Cantidad", "Precio C/U", "Precio Total", "Fecha"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.Double.class, java.lang.Double.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, true, true, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane4.setViewportView(jTable1);

        jPanel7.add(jScrollPane4, java.awt.BorderLayout.CENTER);

        ventanaAdicional.getContentPane().add(jPanel7, java.awt.BorderLayout.CENTER);

        jButton3.setBackground(new java.awt.Color(90, 66, 126));
        jButton3.setIcon(new ImageIcon("imagenes/agregar.png"));
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        l4.setFont(new java.awt.Font("Arial", 0, 9)); // NOI18N
        l4.setForeground(new java.awt.Color(255, 255, 255));
        l4.setText("C. Ref. Adi.");

        t_ref_adi.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        t_ref_adi.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.00"))));
        t_ref_adi.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        t_ref_adi.setText("0.00");
        t_ref_adi.setEnabled(false);
        t_ref_adi.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N

        autorizarCosto.setTitle("Autorización");
        autorizarCosto.setModalExclusionType(null);
        autorizarCosto.setModalityType(java.awt.Dialog.ModalityType.APPLICATION_MODAL);

        jPanel9.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel11.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel11.setText("Usuario:");

        t_user.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                t_userActionPerformed(evt);
            }
        });

        jLabel12.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel12.setText("contraseña:");

        t_clave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                t_claveActionPerformed(evt);
            }
        });

        b_1.setBackground(new java.awt.Color(2, 135, 242));
        b_1.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        b_1.setForeground(new java.awt.Color(254, 254, 254));
        b_1.setText("Autorizar");
        b_1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_1ActionPerformed(evt);
            }
        });

        jLabel14.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel14.setText("Autorizar un costo mayor al autorizado");

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(b_1)
                        .addGroup(jPanel9Layout.createSequentialGroup()
                            .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jLabel12)
                                .addComponent(jLabel11))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(t_user)
                                .addComponent(t_clave, javax.swing.GroupLayout.DEFAULT_SIZE, 173, Short.MAX_VALUE))))
                    .addComponent(jLabel14))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel14)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 41, Short.MAX_VALUE)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(t_user, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(t_clave, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(b_1)
                .addContainerGap())
        );

        autorizarCosto.getContentPane().add(jPanel9, java.awt.BorderLayout.CENTER);

        t_titulos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null}
            },
            new String [] {
                "No", "#", "Descripcion"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        t_titulos.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        t_titulos.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        t_titulos.getTableHeader().setReorderingAllowed(false);
        t_titulos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                t_titulosMouseClicked(evt);
            }
        });

        autorizar1.setTitle("Autorización");
        autorizar1.setModalExclusionType(null);
        autorizar1.setModalityType(java.awt.Dialog.ModalityType.APPLICATION_MODAL);

        jPanel10.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel15.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel15.setText("Usuario:");

        t_user1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                t_user1ActionPerformed(evt);
            }
        });

        jLabel16.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel16.setText("contraseña:");

        t_clave1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                t_clave1ActionPerformed(evt);
            }
        });

        b2.setBackground(new java.awt.Color(2, 135, 242));
        b2.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        b2.setForeground(new java.awt.Color(254, 254, 254));
        b2.setText("Autorizar");
        b2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b2ActionPerformed(evt);
            }
        });

        jLabel17.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel17.setText("Autorizar por encima del 70%");

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(b2)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel16)
                            .addComponent(jLabel15))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(t_user1)
                            .addComponent(t_clave1, javax.swing.GroupLayout.DEFAULT_SIZE, 173, Short.MAX_VALUE)))
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addComponent(jLabel17)
                        .addGap(47, 47, 47)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel17)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 41, Short.MAX_VALUE)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(t_user1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16)
                    .addComponent(t_clave1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(b2)
                .addContainerGap())
        );

        autorizar1.getContentPane().add(jPanel10, java.awt.BorderLayout.CENTER);

        autorizar2.setTitle("Autorización");
        autorizar2.setModalExclusionType(null);
        autorizar2.setModalityType(java.awt.Dialog.ModalityType.APPLICATION_MODAL);

        jPanel11.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel18.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel18.setText("Usuario:");

        t_user2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                t_user2ActionPerformed(evt);
            }
        });

        jLabel19.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel19.setText("contraseña:");

        t_clave2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                t_clave2ActionPerformed(evt);
            }
        });

        b3.setBackground(new java.awt.Color(2, 135, 242));
        b3.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        b3.setForeground(new java.awt.Color(254, 254, 254));
        b3.setText("Autorizar");
        b3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b3ActionPerformed(evt);
            }
        });

        jLabel20.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel20.setText("Autorizar por encima del 100%");

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(b3)
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel19)
                            .addComponent(jLabel18))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(t_user2)
                            .addComponent(t_clave2, javax.swing.GroupLayout.DEFAULT_SIZE, 173, Short.MAX_VALUE)))
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addComponent(jLabel20)
                        .addGap(47, 47, 47)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel20)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 41, Short.MAX_VALUE)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel18)
                    .addComponent(t_user2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel19)
                    .addComponent(t_clave2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(b3)
                .addContainerGap())
        );

        autorizar2.getContentPane().add(jPanel11, java.awt.BorderLayout.CENTER);

        muestra.setTitle("Consultar Movimiento");
        muestra.setModalityType(java.awt.Dialog.ModalityType.TOOLKIT_MODAL);

        centro.setLayout(new java.awt.BorderLayout());
        muestra.getContentPane().add(centro, java.awt.BorderLayout.CENTER);

        setLayout(new java.awt.BorderLayout());

        jPanel1.setBackground(new java.awt.Color(2, 135, 242));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel3.setFont(new java.awt.Font("Arial", 0, 9)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Costo:");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(462, 30, -1, -1));

        t_costo_refacciones.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        t_costo_refacciones.setForeground(new java.awt.Color(2, 38, 253));
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
        jPanel1.add(t_costo_refacciones, new org.netbeans.lib.awtextra.AbsoluteConstraints(496, 23, 88, -1));

        jLabel5.setFont(new java.awt.Font("Arial", 0, 9)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Cia/Seg:");
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(608, 30, -1, -1));

        t_cia_seguros.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        t_cia_seguros.setForeground(new java.awt.Color(2, 38, 253));
        t_cia_seguros.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.00"))));
        t_cia_seguros.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        t_cia_seguros.setText("0.00");
        t_cia_seguros.setDisabledTextColor(new java.awt.Color(2, 38, 253));
        t_cia_seguros.setEnabled(false);
        t_cia_seguros.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        jPanel1.add(t_cia_seguros, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 23, 88, -1));

        jLabel6.setFont(new java.awt.Font("Arial", 0, 9)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Autorizado:");
        jPanel1.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(750, 30, -1, -1));

        t_autorizado.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        t_autorizado.setForeground(new java.awt.Color(2, 38, 253));
        t_autorizado.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.00"))));
        t_autorizado.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        t_autorizado.setText("0.00");
        t_autorizado.setDisabledTextColor(new java.awt.Color(2, 38, 253));
        t_autorizado.setEnabled(false);
        t_autorizado.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        jPanel1.add(t_autorizado, new org.netbeans.lib.awtextra.AbsoluteConstraints(800, 25, 88, -1));

        jLabel13.setFont(new java.awt.Font("Arial", 0, 9)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setText("Compra:");
        jPanel1.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(900, 30, -1, -1));

        t_importe.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        t_importe.setForeground(new java.awt.Color(2, 38, 253));
        t_importe.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.00"))));
        t_importe.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        t_importe.setText("0.00");
        t_importe.setDisabledTextColor(new java.awt.Color(2, 38, 253));
        t_importe.setEnabled(false);
        t_importe.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        jPanel1.add(t_importe, new org.netbeans.lib.awtextra.AbsoluteConstraints(940, 25, 88, -1));

        jLabel1.setFont(new java.awt.Font("Arial", 0, 9)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Facturar a:");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, -1, 10));

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
        jPanel1.add(t_busca, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 25, 223, -1));

        b_busca.setIcon(new ImageIcon("imagenes/buscar1.png"));
        b_busca.setToolTipText("Busca una partida");
        b_busca.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_buscaActionPerformed(evt);
            }
        });
        jPanel1.add(b_busca, new org.netbeans.lib.awtextra.AbsoluteConstraints(82, 18, 23, 20));

        b_tot.setIcon(new ImageIcon("imagenes/boton_mas_PROV.png"));
        b_tot.setToolTipText("Trabajo en taller externo");
        b_tot.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_totActionPerformed(evt);
            }
        });
        jPanel1.add(b_tot, new org.netbeans.lib.awtextra.AbsoluteConstraints(860, 0, 23, 23));

        r_cerrar.setFont(new java.awt.Font("Tahoma", 0, 9)); // NOI18N
        r_cerrar.setForeground(new java.awt.Color(255, 255, 255));
        r_cerrar.setText("Cerrar Compras");
        r_cerrar.setToolTipText("Al marcar esta casilla las compras se cerraran");
        r_cerrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                r_cerrarActionPerformed(evt);
            }
        });
        jPanel1.add(r_cerrar, new org.netbeans.lib.awtextra.AbsoluteConstraints(930, 0, -1, -1));

        jLabel4.setFont(new java.awt.Font("Arial", 0, 9)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Buscar:");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 30, -1, -1));

        t_empresa.setFont(new java.awt.Font("Arial", 0, 9)); // NOI18N
        t_empresa.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        t_empresa.setEnabled(false);
        jPanel1.add(t_empresa, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 70, -1));

        b_busca1.setIcon(new ImageIcon("imagenes/buscar1.png"));
        b_busca1.setToolTipText("Busca una partida");
        b_busca1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_busca1ActionPerformed(evt);
            }
        });
        jPanel1.add(b_busca1, new org.netbeans.lib.awtextra.AbsoluteConstraints(406, 20, 23, 23));

        b_muestra.setIcon(new ImageIcon("imagenes/adjunto1.png"));
        b_muestra.setToolTipText("Formato Muestras");
        b_muestra.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_muestraActionPerformed(evt);
            }
        });
        jPanel1.add(b_muestra, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 0, 23, 23));

        jButton11.setBackground(new java.awt.Color(2, 135, 242));
        jButton11.setIcon(new ImageIcon("imagenes/pdf_icon.png"));
        jButton11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton11ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton11, new org.netbeans.lib.awtextra.AbsoluteConstraints(830, 0, 23, 23));

        jButton12.setIcon(new ImageIcon("imagenes/contacto.png"));
        jButton12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton12ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton12, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 0, 23, 23));

        b_exel.setIcon(new ImageIcon("imagenes/xls_icon.png"));
        b_exel.setToolTipText("Exporta a EXCEL");
        b_exel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_exelActionPerformed(evt);
            }
        });
        jPanel1.add(b_exel, new org.netbeans.lib.awtextra.AbsoluteConstraints(800, 0, 23, 23));

        t_importe1.setEditable(false);
        t_importe1.setBackground(new java.awt.Color(234, 254, 5));
        t_importe1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        t_importe1.setForeground(new java.awt.Color(2, 38, 253));
        t_importe1.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.00"))));
        t_importe1.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        t_importe1.setText("0.00");
        t_importe1.setDisabledTextColor(new java.awt.Color(2, 38, 253));
        t_importe1.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        t_importe1.setOpaque(false);
        jPanel1.add(t_importe1, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 0, 88, -1));

        jLabel21.setFont(new java.awt.Font("Arial", 0, 9)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(255, 255, 255));
        jLabel21.setText("Monto autorizado:");
        jPanel1.add(jLabel21, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 5, -1, -1));

        add(jPanel1, java.awt.BorderLayout.PAGE_END);

        jPanel3.setBackground(new java.awt.Color(254, 254, 254));
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jButton1.setBackground(new java.awt.Color(2, 135, 242));
        jButton1.setIcon(new ImageIcon("imagenes/carrito.png"));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel3.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 55, 50));

        jButton2.setBackground(new java.awt.Color(2, 135, 242));
        jButton2.setIcon(new ImageIcon("imagenes/pedido.png"));
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel3.add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 50, 55, 50));

        add(jPanel3, java.awt.BorderLayout.LINE_END);

        scroll.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        scroll.setAutoscrolls(true);
        scroll.setMaximumSize(new java.awt.Dimension(52767, 52767));
        scroll.setMinimumSize(new java.awt.Dimension(32, 32));
        scroll.setPreferredSize(new java.awt.Dimension(453, 150));
        scroll.setRowHeader(viewport);

        t_datos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "N°", "#", "Grupo", "D/M", "Rep Min ", "Title 6", "Title 7", "Title 8", "Title 9", "Title 10", "Title 11", "Title 12", "Title 13", "Title 14", "Title 15", "Title 16", "Title 17", "Title 18", "Title 19", "Title 20", "Title 21", "Title 22", "Title 23", "Title 24", "Title 25", "Title 26", "Title 27", "Title 28", "Title 29", "Title 30"
            }
        ));
        t_datos.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        t_datos.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        t_datos.getTableHeader().setReorderingAllowed(false);
        t_datos.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                t_datosMouseMoved(evt);
            }
        });
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

    private void t_datosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_t_datosMouseClicked
        // TODO add your handling code here:
        if (evt.getClickCount() == 2) {
            if(t_datos.getSelectedRow()>-1)
            {
                String valor=t_datos.getModel().getValueAt(t_datos.getSelectedRow(), 26).toString();
                if(valor.compareTo("")!=0)
                {
                    Pedido ped =new Pedido();
                    ped.setIdPedido(Integer.parseInt(valor));
                    editaPedido = new editaPedido(this.user, sessionPrograma, ped, 90);
                    editaPedido.busca();
                    Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
                    centro.removeAll();
                    centro.add(editaPedido);
                    muestra.setSize(1180,600);
                    muestra.setLocation((d.width/2)-(muestra.getWidth()/2), (d.height/2)-(muestra.getHeight()/2));
                    centro.repaint();
                    centro.updateUI();
                    muestra.setVisible(true);
                    h= new Herramientas(user, 90);
                    h.session(sessionPrograma);
                    h.desbloqueaOrden();
                    h.desbloqueaPedido();
                }
                else
                    JOptionPane.showMessageDialog(null, "La partida aun no esta pedida"); 
            }
        }
        else
        {
            if(t_datos.getSelectedRow()>=0 && this.b_tot.isEnabled()==true)
            {/*
                if(t_datos.getSelectedColumn()==13)
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
                    finally
                    {
                        if(session.isOpen()==true)
                        session.close();
                    }
                }*/
                if(t_datos.getSelectedColumn()==25)
                {
                    h=new Herramientas(user, 0);
                    h.session(sessionPrograma);
                    if(user.getGeneraPedidos()==true)
                    {
                        if(user.getEditaPedidos()==true)
                        {
                            if(r_cerrar.isSelected()==false)
                            {
                                    Session session = HibernateUtil.getSessionFactory().openSession();
                                    session.beginTransaction().begin();
                                    Partida part=(Partida) session.createCriteria(Partida.class).add(Restrictions.eq("ordenByIdOrden.idOrden", Integer.parseInt(orden))).add(Restrictions.eq("idEvaluacion", Integer.parseInt(t_titulos.getValueAt(t_datos.getSelectedRow(), 0).toString()))).add(Restrictions.eq("subPartida", Integer.parseInt(t_titulos.getValueAt(t_datos.getSelectedRow(), 1).toString()))).setMaxResults(1).uniqueResult();
                                    try
                                    {
                                        if(part!=null)
                                        {
                                            calendario cal =new calendario(new javax.swing.JFrame(), true);
                                            Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
                                            cal.setLocation((d.width/2)-(cal.getWidth()/2), (d.height/2)-(cal.getHeight()/2));
                                            cal.setVisible(true);

                                            Calendar miCalendario=cal.getReturnStatus();

                                            if(miCalendario!=null)
                                            {
                                                String dia=Integer.toString(miCalendario.get(Calendar.DATE));;
                                                String mes = Integer.toString(miCalendario.get(Calendar.MONTH)+1);
                                                String anio = Integer.toString(miCalendario.get(Calendar.YEAR));
                                                t_datos.setValueAt(anio+"-"+mes+"-"+dia, t_datos.getSelectedRow(), t_datos.getSelectedColumn());
                                                part.setPlazo(miCalendario.getTime());
                                            }
                                            else
                                            {
                                                t_datos.setValueAt("", t_datos.getSelectedRow(), t_datos.getSelectedColumn());
                                                part.setPlazo(null);
                                            }

                                            session.update(part);
                                            session.getTransaction().commit();

                                        }
                                        else
                                            JOptionPane.showMessageDialog(null, "La partida ya no existe"); 
                                    }
                                    catch(Exception e)
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
                            }
                            else
                                JOptionPane.showMessageDialog(null, "Las compras estan cerradas"); 
                        }
                        else
                            JOptionPane.showMessageDialog(null, "Acceso denegado no puedes modificar Pedidos"); 
                    }
                    else
                        JOptionPane.showMessageDialog(null, "Acceso denegado"); 
                }
            }
        }
    }//GEN-LAST:event_t_datosMouseClicked

    private void numerosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_numerosMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_numerosMouseClicked

    private void numerosMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_numerosMouseReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_numerosMouseReleased

    private void numerosPopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_numerosPopupMenuWillBecomeInvisible
        // TODO add your handling code here:
    }//GEN-LAST:event_numerosPopupMenuWillBecomeInvisible

    private void numerosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_numerosActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_numerosActionPerformed

    private void numerosFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_numerosFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_numerosFocusGained

    private void numerosFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_numerosFocusLost
        // TODO add your handling code here:
        entro=1;
    }//GEN-LAST:event_numerosFocusLost

    private void t_costo_refaccionesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_t_costo_refaccionesActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_t_costo_refaccionesActionPerformed

    private void t_costo_refaccionesFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_t_costo_refaccionesFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_t_costo_refaccionesFocusGained

    private void t_buscaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_t_buscaActionPerformed
        // TODO add your handling code here:
        h=new Herramientas(user, 0);
        h.session(sessionPrograma);
        if(this.t_busca.getText().compareToIgnoreCase("")!=0)
        {
            if(x>=t_titulos.getRowCount())
            {
                x=0;
                java.awt.Rectangle r = t_titulos.getCellRect( x, 2, true);
                t_titulos.scrollRectToVisible(r);
                t_datos.scrollRectToVisible(r);
            }
            for(; x<t_titulos.getRowCount(); x++)
            {
                if(t_titulos.getValueAt(x, 2).toString().indexOf(t_busca.getText()) != -1)
                {
                    t_titulos.setRowSelectionInterval(x, x);
                    t_titulos.setColumnSelectionInterval(2, 2);
                    java.awt.Rectangle r = t_titulos.getCellRect( x, 2, true);
                    t_titulos.scrollRectToVisible(r);
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

    private void b_buscaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_buscaActionPerformed
        // TODO add your handling code here:
        h=new Herramientas(user, 0);
        h.session(sessionPrograma);
        if(user.getGeneraPedidos()==true)
        {
            buscaProveedor obj = new buscaProveedor(new javax.swing.JFrame(), true, this.user, this.sessionPrograma);
            obj.t_busca.requestFocus();
            Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
            obj.setLocation((d.width/2)-(obj.getWidth()/2), (d.height/2)-(obj.getHeight()/2));
            obj.setVisible(true);

            Proveedor prov=obj.getReturnStatus();
            if (prov!=null)
            {
                t_empresa.setText(""+prov.getIdProveedor());
            }
            else
            {
                t_empresa.setText("");
            }
        }
        else
        {
            JOptionPane.showMessageDialog(null, "¡Acceso denegado!");
        }
    }//GEN-LAST:event_b_buscaActionPerformed

    private void r_cerrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_r_cerrarActionPerformed
        // TODO add your handling code here:
        Session session = HibernateUtil.getSessionFactory().openSession();
        if(r_cerrar.isSelected()==true)
        {
            user = (Usuario)session.get(Usuario.class, user.getIdUsuario());
            if(user.getCerrarCompras()==true)//permiso de cerrar compras
            {
                try
                {
                    h=new Herramientas(user, 0);
                    h.session(sessionPrograma);
                    int opt=JOptionPane.showConfirmDialog(this, "¡Confirma que deseas cerrar las compras!");
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
                            ord.setCierreRefacciones(calendario3.getTime());
                            session.update(ord);
                            session.getTransaction().commit();
                            JOptionPane.showMessageDialog(null, "Las compras fueron cerradas");
                            visualiza(false, 0);
                        //buscaCuentas(-1,-1);
                    }
                    else
                    r_cerrar.setSelected(false);
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                    session.getTransaction().rollback();
                    r_cerrar.setSelected(false);
                    JOptionPane.showMessageDialog(null, "Error no se pudo cerrar las compras");
                }
            }
            else
            {
                r_cerrar.setSelected(false);
                JOptionPane.showMessageDialog(null, "Acceso denegado");
            }
        }
        else
        {
            user = (Usuario)session.get(Usuario.class, user.getIdUsuario());
            if(user.getAutorizarPedidos()==true)
            {
                try
                {
                    h=new Herramientas(user, 0);
                    h.session(sessionPrograma);
                    int opt=JOptionPane.showConfirmDialog(this, "¡Confirma que deseas abrir las compras!");
                    if(opt==0)
                    {
                        session.beginTransaction().begin();
                        Orden ord=(Orden)session.get(Orden.class, Integer.parseInt(orden));
                        //***guardar el usuario que abrio el levantamiento*****
                        ord.setCierreRefacciones(null);
                        ord.setUsuarioByRRefaccionesAsigno(user);
                        session.update(ord);
                        session.getTransaction().commit();
                        JOptionPane.showMessageDialog(null, "Las compras fueron abierta");
                        visualiza(true, 0);
                    }
                    else
                    r_cerrar.setSelected(true);
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                    session.getTransaction().rollback();
                    r_cerrar.setSelected(true);
                    JOptionPane.showMessageDialog(null, "Error no se pudo abrir la valuación");
                }
            }
            else
            {
                r_cerrar.setSelected(true);
                JOptionPane.showMessageDialog(null, "Acceso denegado");
            }
        }
        if(session!=null)
            if(session.isOpen())
                session.close();
    }//GEN-LAST:event_r_cerrarActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        if(user.getGeneraPedidos()==true)
        {
            if(t_datos.getRowCount()>0)
            {
                if(t_empresa.getText().compareTo("")!=0)
                {
                    /*if(consultaLista()==true)
                    {*/
                    //separamos los pedidos por proveedores
                    pedidos=new ArrayList();
                    int entro=-1;
                    for(int ren=0; ren<t_datos.getRowCount(); ren++)
                    {
                        if(t_datos.getModel().getValueAt(ren, 22).toString().compareTo("")!=0 && t_datos.getModel().getValueAt(ren, 26).toString().compareTo("")==0)
                        {
                            if(Double.parseDouble(t_datos.getModel().getValueAt(ren, 23).toString())==0.00)
                            {
                                t_datos.setRowSelectionInterval(ren, ren);
                                if(t_datos.getColumnCount()==29)
                                    t_datos.setColumnSelectionInterval(22, 22);
                                else
                                    t_datos.setColumnSelectionInterval(17, 17);
                                ren=t_datos.getRowCount();
                                entro=ren;
                            }
                            else
                                exite(t_datos.getModel().getValueAt(ren, 22).toString(), ren); 
                        }
                    }
                    if(entro==-1)
                    {
                        //validamos que las compras sean menor al 70% del valor en vales
                        Session session1 = HibernateUtil.getSessionFactory().openSession();
                        session1.beginTransaction().begin();
                        this.ord=(Orden)session1.get(Orden.class, ord.getIdOrden());
                        if(session1!=null)
                            if(session1.isOpen())
                                session1.close();
                        if(ord!=null)
                        {
                            Double misVales=ord.getVales();
                            Double compras=((Number)t_importe.getValue()).doubleValue();
                            boolean permiso=true;
                            if(misVales>0.0d)
                            {
                                Double setenta = misVales*0.7;
                                Double total=totalActual();
                                Double totalNeto=(total+compras);
                                if(setenta <= totalNeto)
                                {
                                    if(misVales >= totalNeto)
                                    {
                                        usrAut1=null;
                                        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
                                        autorizar1.setSize(284, 177);
                                        autorizar1.setLocation((d.width/2)-(autorizar1.getWidth()/2), (d.height/2)-(autorizar1.getHeight()/2));
                                        t_user1.setText("");
                                        t_clave1.setText("");
                                        autorizar1.setVisible(true);
                                        if(usrAut1==null)
                                            permiso=false;//no se autoriza
                                    }
                                    else
                                    {
                                        usrAut2=null;
                                        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
                                        autorizar2.setSize(284, 177);
                                        autorizar2.setLocation((d.width/2)-(autorizar2.getWidth()/2), (d.height/2)-(autorizar2.getHeight()/2));
                                        t_user2.setText("");
                                        t_clave2.setText("");
                                        autorizar2.setVisible(true);
                                        if(usrAut2==null)
                                            permiso=false;//no se autoriza
                                    }
                                }
                            }
                            if(permiso==true)
                            {
                                //generamos las cotizaciones almacenandolas en la bd
                                Session session = HibernateUtil.getSessionFactory().openSession();
                                session.beginTransaction().begin();
                                id_pedido=new ArrayList();
                                int error=0;
                                Date fechaCotizacion = new Date();
                                DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                                            String valor=dateFormat.format(fechaCotizacion);
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

                                for(int cot=0; cot<pedidos.size(); cot++)
                                {
                                    List proveedores=(ArrayList)pedidos.get(cot);
                                    try
                                    {
                                        Pedido registroNuevo=new Pedido();
                                        Proveedor prov=(Proveedor)session.get(Proveedor.class, Integer.parseInt(proveedores.get(0).toString()));
                                        Proveedor emp=(Proveedor)session.get(Proveedor.class, Integer.parseInt(t_empresa.getText()));
                                        registroNuevo.setProveedorByIdProveedor(prov);
                                        registroNuevo.setProveedorByIdEmpresa(emp);
                                        registroNuevo.setUsuarioByIdUsuario(user);
                                        registroNuevo.setFechaPedido(calendario.getTime());
                                        registroNuevo.setTipoPedido("Interno");
                                        user=(Usuario)session.get(Usuario.class, user.getIdUsuario());
                                        registroNuevo.setEmpleado(user.getEmpleado());
                                        List vec =new ArrayList();

                                        vec.add((int)session.save(registroNuevo));
                                        vec.add(prov.getNombre());
                                        vec.add(calendario.getTime());
                                        id_pedido.add(vec);
                                        for(int h=1; h<proveedores.size(); h++)
                                        {
                                            Partida par=(Partida)session.get(Partida.class, Integer.parseInt(noPartida.get(Integer.parseInt(proveedores.get(h).toString())).toString()));
                                            par.setPedido(registroNuevo);
                                            session.update(par);
                                        }
                                    }
                                    catch(Exception e)
                                    {
                                        error=1;
                                        e.printStackTrace();
                                    }
                                }
                                if(error==0)
                                {
                                    session.beginTransaction().commit();
                                    session.close();
                                    DefaultTableModel temp = (DefaultTableModel) t_datos2.getModel();
                                    for(int y=temp.getRowCount()-1; y>-1; y--)
                                    {
                                        temp.removeRow(y);
                                    }
                                    for(int z=0; z<id_pedido.size(); z++)
                                    {
                                        List no=new ArrayList();
                                        no=(List)id_pedido.get(z);
                                        temp.addRow(new Object[]{no.get(0).toString(),no.get(1).toString(),no.get(2).toString()});
                                    }
                                    //System.out.println(t_titulos.getRowCount()-1);
                                    //System.out.println(t_datos.getRowCount()-1);
                                    habilita=false;
                                    t_titulos.removeRowSelectionInterval(0, t_titulos.getRowCount()-1);
                                    t_datos.removeRowSelectionInterval(0, t_datos.getRowCount()-1);
                                    habilita=true;
                                    this.buscaCuentas(-1, -1);
                                    try{
                                        envia();
                                    }catch(Exception e){
                                        e.printStackTrace();
                                    };
                                    Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
                                    ventana.setSize(400, 276);
                                    ventana.setLocation((d.width/2)-(ventana.getWidth()/2), (d.height/2)-(ventana.getHeight()/2));
                                    ventana.setVisible(true);
                                }
                                else
                                {
                                    session.beginTransaction().rollback();
                                    JOptionPane.showMessageDialog(null, "¡Algunos pedidos no se pudieron realizar!");
                                }
                                if(session!=null)
                                    if(session.isOpen())
                                        session.close();
                            }
                        }
                    }
                    else
                        JOptionPane.showMessageDialog(null, "No se puede almacenar ya que una partida contiene 0.00");
                }
                else
                {
                    JOptionPane.showMessageDialog(null, "¡Debes ingresar el proveedor al que se va a facturar!");
                    b_busca.requestFocus();
                }
            }
        }
        else
        {
            JOptionPane.showMessageDialog(null, "¡Acceso Denegado!");
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void b_busca1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_busca1ActionPerformed
        // TODO add your handling code here:
        if(this.t_busca.getText().compareToIgnoreCase("")!=0)
        {
            if(x>=t_titulos.getRowCount())
            {
                x=0;
                java.awt.Rectangle r = t_titulos.getCellRect( x, 2, true);
                t_titulos.scrollRectToVisible(r);
                t_datos.scrollRectToVisible(r);
            }
            for(; x<t_titulos.getRowCount(); x++)
            {
                if(t_titulos.getValueAt(x, 2).toString().indexOf(t_busca.getText()) != -1)
                {
                    t_titulos.setRowSelectionInterval(x, x);
                    t_titulos.setColumnSelectionInterval(2, 2);
                    java.awt.Rectangle r = t_titulos.getCellRect( x, 2, true);
                    t_titulos.scrollRectToVisible(r);
                    t_datos.scrollRectToVisible(r);
                    break;
                }
            }
            x++;
        }
    }//GEN-LAST:event_b_busca1ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        cargaPedidos();        
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        listaPedidos.setSize(550, 280);
        listaPedidos.setLocation((d.width/2)-(listaPedidos.getWidth()/2), (d.height/2)-(listaPedidos.getHeight()/2));
        listaPedidos.setVisible(true);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here: 
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        ventanaAdicional.setSize(629, 300);
        ventanaAdicional.setLocation((d.width/2)-(ventanaAdicional.getWidth()/2), (d.height/2)-(ventanaAdicional.getHeight()/2));
        ventanaAdicional.setVisible(true);
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:
        if(t_pedidos.getSelectedRow()> -1)
        {
            if(t_pedidos.getValueAt(t_pedidos.getSelectedRow(), 3).toString().compareTo("Interno")==0)
            {
                f1=new Formatos(this.user, this.sessionPrograma, this.ord, t_pedidos.getValueAt(t_pedidos.getSelectedRow(), 0).toString());
                f1.pedidos();
            }
            else
            {
                f1=new Formatos(this.user, this.sessionPrograma, this.ord, t_pedidos.getValueAt(t_pedidos.getSelectedRow(), 0).toString());
                f1.pedidosExternos(Integer.parseInt(t_pedidos.getValueAt(t_pedidos.getSelectedRow(), 0).toString()));
            }
        }
        else
        {
            JOptionPane.showMessageDialog(this, "¡Seleccione primero un pedido de la lista!");
        }
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        // TODO add your handling code here:
        if(t_pedidos.getSelectedRow()> -1)
        {
            if(t_pedidos.getValueAt(t_pedidos.getSelectedRow(), 3).toString().compareTo("Interno")==0)
            {
                f1=new Formatos(this.user, this.sessionPrograma, this.ord, t_pedidos.getValueAt(t_pedidos.getSelectedRow(), 0).toString());
                f1.ordenCompra();
            }
            else
            {
                f1=new Formatos(this.user, this.sessionPrograma, this.ord, t_pedidos.getValueAt(t_pedidos.getSelectedRow(), 0).toString());
                f1.ordenCompraExternos(Integer.parseInt(t_pedidos.getValueAt(t_pedidos.getSelectedRow(), 0).toString()));
            }
        }
        else
        {
            JOptionPane.showMessageDialog(this, "¡Seleccione primero un pedido de la lista!");
        }
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        int error=0;
        if(user.getEliminaPedidos()==true)
        {
            if(t_pedidos.getSelectedRow()> -1)
            {
                int opt=JOptionPane.showConfirmDialog(this, "¡Confirma que deseas eliminar el pedido!");
                if(opt==0)
                {
                    Session session = HibernateUtil.getSessionFactory().openSession();
                    try
                    {
                        session.beginTransaction().begin();
                        Pedido ped = (Pedido)session.get(Pedido.class, Integer.parseInt(t_pedidos.getValueAt(t_pedidos.getSelectedRow(), 0).toString()));
                        String consulta="select id_pedido as id, " +
                                            "if(tipo_pedido='Interno', " +
                                            "(select if( sum(movimiento.cantidad) is null, 0, sum(movimiento.cantidad)) as can from partida inner join movimiento on partida.id_partida=movimiento.id_partida inner join almacen on movimiento.id_almacen=almacen.id_almacen where partida.id_pedido=id and almacen.tipo_movimiento=1 and almacen.operacion=1) " +
                                            "-(select if( sum(movimiento.cantidad) is null, 0, sum(movimiento.cantidad)) as can from partida inner join movimiento on partida.id_partida=movimiento.id_partida inner join almacen on movimiento.id_almacen=almacen.id_almacen where partida.id_pedido=id and almacen.tipo_movimiento=2 and almacen.operacion=1), " +
                                            " (select if( sum(movimiento.cantidad) is null, 0, sum(movimiento.cantidad)) as can from partida_externa inner join movimiento on partida_externa.id_partida_externa=movimiento.id_externos inner join almacen on movimiento.id_almacen=almacen.id_almacen where partida_externa.id_pedido=id and almacen.tipo_movimiento=1 and almacen.operacion in (2, 3)) " +
                                            "-(select if( sum(movimiento.cantidad) is null, 0, sum(movimiento.cantidad)) as can from partida_externa inner join movimiento on partida_externa.id_partida_externa=movimiento.id_externos inner join almacen on movimiento.id_almacen=almacen.id_almacen where partida_externa.id_pedido=id and almacen.tipo_movimiento=2 and almacen.operacion in (2, 3)) ) as almacen2 " +
                                            "from pedido inner join empleado on pedido.comprador=empleado.id_empleado where pedido.id_pedido = '"+ped.getIdPedido()+"'";
                            Query query = session.createSQLQuery(consulta);
                            query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
                            ArrayList pedidos=(ArrayList)query.list();
                            double existencias=0.0d;
                            for(int a=0; a<pedidos.size(); a++)
                            {
                                java.util.HashMap map=(java.util.HashMap)pedidos.get(a);
                                existencias=Double.parseDouble(map.get("almacen2").toString());
                            }
                            if(existencias==0.0d)
                            {
                                if(ped.getUsuarioByAutorizo()==null && ped.getUsuarioByAutorizo2()==null)
                                {
                                    Almacen[] almacen= (Almacen[])ped.getAlmacens().toArray(new Almacen[0]);
                                    for(int alm=0; alm<ped.getAlmacens().size(); alm++)
                                    {
                                        session.delete(almacen[alm]);
                                    }
                                    Partida[] partidas =(Partida[]) session.createCriteria(Partida.class).add(Restrictions.eq("ordenByIdOrden.idOrden", Integer.parseInt(orden))).add(Restrictions.eq("pedido.idPedido", Integer.parseInt(t_pedidos.getValueAt(t_pedidos.getSelectedRow(), 0).toString()))).addOrder(Order.asc("idEvaluacion")).addOrder(Order.asc("subPartida")).list().toArray(new Partida[0]);
                                    for(int ren=0; ren<partidas.length; ren++)
                                    {
                                            partidas[ren].getMovimientos().removeAll(partidas[ren].getMovimientos());
                                            partidas[ren].setPedido(null);
                                            session.update(partidas[ren]);
                                    }
                                    if(error==0)
                                    {
                                        session.delete(ped);
                                        session.beginTransaction().commit();
                                        cargaPedidos();
                                        JOptionPane.showMessageDialog(this, "¡Pedido Eliminado!");
                                        this.buscaCuentas(-1, -1);
                                    }
                                }
                                else
                                    JOptionPane.showMessageDialog(this, "¡No se puede eliminar pedidos autorizados!");
                            }
                            else
                                JOptionPane.showMessageDialog(this, "¡El pedido tiene "+existencias+" en el almacen!");
                        
                    }catch(Exception e)
                    {
                        e.printStackTrace();
                        session.getTransaction().rollback();
                        cargaPedidos();
                        JOptionPane.showMessageDialog(this, "¡Error al Eliminar el pedido!");
                        this.buscaCuentas(-1, -1);
                    }
                    finally
                    {
                        if(session.isOpen())
                            session.close();
                    }
                }
            }
            else
                JOptionPane.showMessageDialog(this, "¡Seleccione primero un pedido de la lista!");
        }
        else
            JOptionPane.showMessageDialog(null, "¡Acceso denegado!");
    }//GEN-LAST:event_jButton4ActionPerformed

    private void t_userActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_t_userActionPerformed
        // TODO add your handling code here:
        t_clave.requestFocus();
    }//GEN-LAST:event_t_userActionPerformed

    private void b_1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_1ActionPerformed
        // TODO add your handling code here:
        if(t_user.getText().compareTo("")!=0)
        {
            if(t_clave.getPassword().toString().compareTo("")!=0)
            {
                Session session = HibernateUtil.getSessionFactory().openSession();
                try
                {
                    session.beginTransaction().begin();
                    Usuario autoriza = (Usuario)session.createCriteria(Usuario.class).add(Restrictions.eq("idUsuario", t_user.getText())).add(Restrictions.eq("clave", t_clave.getText())).setMaxResults(1).uniqueResult();
                    if(autoriza!=null)
                    {
                        if(autoriza.getAutorizarSobrecosto()==true)
                        {
                            usrAut=autoriza;
                            autorizarCosto.dispose();
                        }
                        else
                            JOptionPane.showMessageDialog(this, "¡el usuario no tiene permiso de autorizar!");    
                    }
                    else
                    {
                        session.beginTransaction().rollback();
                        JOptionPane.showMessageDialog(this, "¡Datos Incorrectos!");
                    }
                }catch(Exception e)
                {
                    session.beginTransaction().rollback();
                    JOptionPane.showMessageDialog(this, "¡Error al consultar los datos!");
                    e.printStackTrace();
                }
                finally
                {
                    if(session.isOpen()==true)
                        session.close();
                }
            }
            else
                JOptionPane.showMessageDialog(this, "¡Ingrese la contraseña!");
        }
        else
            JOptionPane.showMessageDialog(this, "¡Ingrese el Usuario!");
    }//GEN-LAST:event_b_1ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton9ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton8ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton7ActionPerformed

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

    private void b_muestraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_muestraActionPerformed
        // TODO add your handling code here:
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
                Sheet hoja = libro.createSheet("Muestras");
                
                org.apache.poi.ss.usermodel.Font font10 = libro.createFont();
                font10.setFontHeightInPoints((short)10);
                font10.setFontName("Arial");
                font10.setItalic(false);
                font10.setBold(false);
                font10.setColor(new HSSFColor.YELLOW().getIndex());
                
                
                CellStyle titulo = libro.createCellStyle();
                CellStyle contenido = libro.createCellStyle();
                CellStyle firma = libro.createCellStyle();
                CellStyle costado = libro.createCellStyle();
                CellStyle derecha = libro.createCellStyle();
                CellStyle derecha_borde = libro.createCellStyle();
                
                titulo.setFont(font10);
                titulo.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                titulo.setFillBackgroundColor(new HSSFColor.GREEN().getIndex());
                titulo.setAlignment(CellStyle.ALIGN_CENTER);
                
                contenido.setBorderBottom(HSSFCellStyle.BORDER_THIN);
                contenido.setBorderTop(HSSFCellStyle.BORDER_THIN);
                contenido.setBorderRight(HSSFCellStyle.BORDER_THIN);
                contenido.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                
                derecha_borde.setBorderBottom(HSSFCellStyle.BORDER_THIN);
                derecha_borde.setBorderTop(HSSFCellStyle.BORDER_THIN);
                derecha_borde.setBorderRight(HSSFCellStyle.BORDER_THIN);
                derecha_borde.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                derecha_borde.setAlignment(CellStyle.ALIGN_RIGHT);
                
                derecha.setAlignment(CellStyle.ALIGN_RIGHT);
                
                firma.setBorderTop(HSSFCellStyle.BORDER_THIN);
                firma.setAlignment(CellStyle.ALIGN_CENTER);
                
                //costado.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                
                hoja.setColumnWidth(0, 3000);
                hoja.setColumnWidth(2, 3000);
                hoja.setColumnWidth(3, 8000);
                hoja.setColumnWidth(4, 5000);
                try
                {
                    InputStream is = new FileInputStream("imagenes/grande300115.jpg");
                    byte[] bytes = IOUtils.toByteArray(is);
                    int pictureIdx = libro.addPicture(bytes, Workbook.PICTURE_TYPE_JPEG);
                    is.close();
                    CreationHelper helper = libro.getCreationHelper();
                    Drawing drawing = hoja.createDrawingPatriarch();
                    ClientAnchor anchor = helper.createClientAnchor();
                    anchor.setCol1(0);
                    anchor.setRow1(0);
                    Picture pict = drawing.createPicture(anchor, pictureIdx);
                    pict.resize();
                }catch(Exception e){e.printStackTrace();}
                Row r7 = hoja.createRow(7);
                r7.createCell(0).setCellValue("ORDEN:");
                r7.createCell(1).setCellValue(String.valueOf(ord.getIdOrden()));
                r7.createCell(2).setCellValue("Hrs. Entrega:");
                r7.createCell(3).setCellValue("");
                hoja.addMergedRegion(new CellRangeAddress(7,7,4,7));
                r7.createCell(4).setCellValue("ORDEN PARA SURTIR MUESTRAS");
                r7.getCell(4).setCellStyle(derecha);
                
                Date fecha = new Date();
                DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");//YYYY-MM-DD HH:MM:SS
                String valor=dateFormat.format(fecha);
                Row r8 = hoja.createRow(8);
                r8.createCell(0).setCellValue("Supervisor:");
                hoja.addMergedRegion(new CellRangeAddress(8,8,1,3));
                r8.createCell(1).setCellValue("");
                r8.createCell(4).setCellValue("F. Elaboración:");
                r8.createCell(5).setCellValue(valor);
                
                Row r9 = hoja.createRow(9);
                r9.createCell(0).setCellValue("Comprador:");
                hoja.addMergedRegion(new CellRangeAddress(9,9,1,3));
                r9.createCell(1).setCellValue("");
                r9.createCell(4).setCellValue("F. Entrega:");
                r9.createCell(5).setCellValue("");
                
                Row r10 = hoja.createRow(10);
                r10.createCell(0).setCellValue("Cantidad");
                r10.getCell(0).setCellStyle(titulo);
                hoja.addMergedRegion(new CellRangeAddress(10,10,1,7));
                r10.createCell(1).setCellValue("Descripción");
                r10.getCell(1).setCellStyle(titulo);
                
                int ren=11;
                for(int r=0;r<(t_datos.getRowCount());r++)
                {
                    if((boolean)t_titulos.getValueAt(r, 3)==true)
                    {
                        Row fila = hoja.createRow(ren);
                        Cell celda = fila.createCell(0);
                        celda.setCellStyle(derecha_borde);
                        Cell celda1 = fila.createCell(1);
                        celda1.setCellStyle(contenido);
                        fila.createCell(2).setCellStyle(contenido);
                        fila.createCell(3).setCellStyle(contenido);
                        fila.createCell(4).setCellStyle(contenido);
                        fila.createCell(5).setCellStyle(contenido);
                        fila.createCell(6).setCellStyle(contenido);
                        fila.createCell(7).setCellStyle(contenido);
                        //Cell celda8 = fila.createCell(8);
                        //celda8.setCellStyle(costado);
                        try{
                            celda.setCellValue(t_datos.getValueAt(r, 14).toString());
                            hoja.addMergedRegion(new CellRangeAddress(ren,ren,1,7));
                            celda1.setCellValue(t_titulos.getValueAt(r, 2).toString());
                            //celda8.setCellValue("");
                        }catch(Exception e){
                            celda.setCellValue("");
                        }
                        ren++;
                    }
                }
                
                Row rx = hoja.createRow(ren+5);
                hoja.addMergedRegion(new CellRangeAddress(ren+5,ren+5,0,2));
                rx.createCell(0).setCellValue("Recibe Muestras");
                rx.getCell(0).setCellStyle(firma);
                rx.createCell(1).setCellStyle(firma);
                rx.createCell(2).setCellStyle(firma);
                hoja.addMergedRegion(new CellRangeAddress(ren+5,ren+5,5,7));
                rx.createCell(5).setCellValue("Entrega Muestras");
                rx.getCell(5).setCellStyle(firma);
                rx.createCell(6).setCellStyle(firma);
                rx.createCell(7).setCellStyle(firma);
                
                libro.write(archivo);
                archivo.close();
                Desktop.getDesktop().open(archivoXLS);
            }catch(Exception e)
            {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "No se pudo realizar el reporte si el archivo esta abierto");
            }
        }
    }//GEN-LAST:event_b_muestraActionPerformed

    private void b_totActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_totActionPerformed
        // TODO add your handling code here:
        h=new Herramientas(user, 0);
        h.session(sessionPrograma);
        if(user.getGeneraPedidos()==true)
        {
            if(t_datos.getSelectedRow()>-1)
            {
                if((boolean)t_datos.getModel().getValueAt(t_datos.getSelectedRow(), 18)==true)
                {
                    if(t_datos.getModel().getValueAt(t_datos.getSelectedRow(), 26).toString().compareTo("")==0)
                    {
                        buscaProveedor obj = new buscaProveedor(new javax.swing.JFrame(), true, this.user, this.sessionPrograma);
                        obj.t_busca.requestFocus();
                        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
                        obj.setLocation((d.width/2)-(obj.getWidth()/2), (d.height/2)-(obj.getHeight()/2));
                        obj.setVisible(true);

                        Proveedor prov=obj.getReturnStatus();
                        Session session = HibernateUtil.getSessionFactory().openSession();
                        session.beginTransaction().begin();

                        if (prov!=null)
                        {
                            user = (Usuario)session.get(Usuario.class, user.getIdUsuario());
                            if(user.getEditaPedidos()==true || (r_cerrar.isSelected()==false))
                            {
                                t_datos.getModel().setValueAt(""+prov.getIdProveedor(), t_datos.getSelectedRow(), 22);
                            }
                            else
                            {
                                session.getTransaction().rollback();
                                if(session.isOpen()==true)
                                {
                                    session.close();
                                }
                                JOptionPane.showMessageDialog(null, "¡Acceso denegado!");
                            }
                        }
                        else
                        {
                            user = (Usuario)session.get(Usuario.class, user.getIdUsuario());
                            if(user.getEditaPedidos()==true || (r_cerrar.isSelected()==false ))
                            {
                                t_datos.getModel().setValueAt("", t_datos.getSelectedRow(), 22);
                            }
                            else
                            {
                                JOptionPane.showMessageDialog(null, "¡Acceso denegado!");
                            }
                        }
                        if(session!=null)
                        if(session.isOpen())
                        session.close();
                    }
                }
                else
                {
                    JOptionPane.showMessageDialog(null, "¡Aun no esta autorizada la compra!");
                }
            }
            else
            {
                JOptionPane.showMessageDialog(null, "¡Debes seleccionar primero una partida de la tabla!");
            }
        }
        else
        {
            JOptionPane.showMessageDialog(null, "¡Acceso denegado!");
        }
    }//GEN-LAST:event_b_totActionPerformed

    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton11ActionPerformed
        // TODO add your handling code here:
        if(t_datos.getRowCount()>0)
        {
            javax.swing.JFileChooser jF1= new javax.swing.JFileChooser();
            jF1.setFileFilter(new ExtensionFileFilter("Excel document (*.pdf)", new String[] { "pdf" }));
            String ruta = null;
            if(jF1.showSaveDialog(null)==jF1.APPROVE_OPTION)
            {
                ruta = jF1.getSelectedFile().getAbsolutePath();
                if(ruta!=null)
                {
                    Session session = HibernateUtil.getSessionFactory().openSession();
                    try
                    {
                        DecimalFormat formatoPorcentaje = new DecimalFormat("#,##0.00");
                        formatoPorcentaje.setMinimumFractionDigits(2);
                        session.beginTransaction().begin();
                        BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.WINANSI, BaseFont.NOT_EMBEDDED);
                        //Orden ord=buscaApertura();
                        PDF reporte = new PDF();
                        Date fecha = new Date();
                        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyyHH-mm-ss");//YYYY-MM-DD HH:MM:SS
                        String valor=dateFormat.format(fecha);

                        reporte.Abrir2(PageSize.LETTER.rotate(), "Valuación", ruta+".pdf");
                        Font font = new Font(Font.FontFamily.HELVETICA, 5, Font.BOLD);
                        BaseColor contenido=BaseColor.WHITE;
                        int centro=Element.ALIGN_CENTER;
                        int izquierda=Element.ALIGN_LEFT;
                        int derecha=Element.ALIGN_RIGHT;
                        float tam[]=new float[]{10,10,80,5,5,5,5,5,14,10,25,23,8,8,8,10,10,10};
                        

                        PdfPTable tabla=reporte.crearTabla(tam.length, tam, 100, Element.ALIGN_LEFT);

                        cabecera(reporte, bf, tabla);
                        int ren=0;
                        
                        //double dm=0d, cam=0d, min=0d, med=0d, max=0d, pin=0d, tot=0d;

                        for(int i=0; i<t_datos.getRowCount(); i++)
                        {
                            tabla.addCell(reporte.celda(t_titulos.getValueAt(i, 0).toString(), font, contenido, derecha, 0,1,Rectangle.RECTANGLE));
                            tabla.addCell(reporte.celda(t_titulos.getValueAt(i, 1).toString(), font, contenido, derecha, 0,1,Rectangle.RECTANGLE));
                            tabla.addCell(reporte.celda(t_titulos.getValueAt(i, 2).toString(), font, contenido, izquierda, 0,1,Rectangle.RECTANGLE));
                            if( ((boolean)t_datos.getValueAt(i, 1)) ==true)
                                tabla.addCell(reporte.celda("x", font, contenido, derecha, 0,1,Rectangle.RECTANGLE));
                            else
                                tabla.addCell(reporte.celda(" ", font, contenido, derecha, 0,1,Rectangle.RECTANGLE));
                            
                            if( ((boolean)t_datos.getValueAt(i, 2)) ==true)
                                tabla.addCell(reporte.celda("x", font, contenido, derecha, 0,1,Rectangle.RECTANGLE));
                            else
                                tabla.addCell(reporte.celda(" ", font, contenido, derecha, 0,1,Rectangle.RECTANGLE));
                            
                            if( ((boolean)t_datos.getValueAt(i, 3)) ==true)
                                tabla.addCell(reporte.celda("x", font, contenido, derecha, 0,1,Rectangle.RECTANGLE));
                            else
                                tabla.addCell(reporte.celda(" ", font, contenido, derecha, 0,1,Rectangle.RECTANGLE));
                            
                            if( ((boolean)t_datos.getValueAt(i, 4)) ==true)
                                tabla.addCell(reporte.celda("x", font, contenido, derecha, 0,1,Rectangle.RECTANGLE));
                            else
                                tabla.addCell(reporte.celda(" ", font, contenido, derecha, 0,1,Rectangle.RECTANGLE));
                            
                            if( (t_datos.getValueAt(i, 6)) != null)
                                tabla.addCell(reporte.celda("x", font, contenido, derecha, 0,1,Rectangle.RECTANGLE));
                            else
                                tabla.addCell(reporte.celda(" ", font, contenido, derecha, 0,1,Rectangle.RECTANGLE));
                            
                            tabla.addCell(reporte.celda(formatoPorcentaje.format(t_datos.getValueAt(i, 14)), font, contenido, derecha, 0,1,Rectangle.RECTANGLE));
                            
                            tabla.addCell(reporte.celda(t_datos.getValueAt(i, 8).toString(), font, contenido, derecha, 0,1,Rectangle.RECTANGLE));
                            
                            tabla.addCell(reporte.celda(t_datos.getValueAt(i, 10).toString(), font, contenido, derecha, 0,1,Rectangle.RECTANGLE));
                            
                            tabla.addCell(reporte.celda(formatoPorcentaje.format(t_datos.getValueAt(i, 15)), font, contenido, derecha, 0,1,Rectangle.RECTANGLE));
                            
                            if( ((boolean)t_datos.getValueAt(i, 17)) ==true)
                                tabla.addCell(reporte.celda("x", font, contenido, derecha, 0,1,Rectangle.RECTANGLE));
                            else
                                tabla.addCell(reporte.celda(" ", font, contenido, derecha, 0,1,Rectangle.RECTANGLE));
                            
                            if( ((boolean)t_datos.getValueAt(i, 18)) ==true)
                                tabla.addCell(reporte.celda("x", font, contenido, derecha, 0,1,Rectangle.RECTANGLE));
                            else
                                tabla.addCell(reporte.celda(" ", font, contenido, derecha, 0,1,Rectangle.RECTANGLE));
                            
                            if( ((boolean)t_datos.getValueAt(i, 19)) ==true)
                                tabla.addCell(reporte.celda("x", font, contenido, derecha, 0,1,Rectangle.RECTANGLE));
                            else
                                tabla.addCell(reporte.celda(" ", font, contenido, derecha, 0,1,Rectangle.RECTANGLE));
                            
                            if(t_datos.getValueAt(i, 20)!=null)
                                tabla.addCell(reporte.celda(t_datos.getValueAt(i, 20).toString(), font, contenido, derecha, 0,1,Rectangle.RECTANGLE));
                            else
                                tabla.addCell(reporte.celda(" ", font, contenido, derecha, 0,1,Rectangle.RECTANGLE));
                            
                            if(t_datos.getValueAt(i, 26)!=null)
                                tabla.addCell(reporte.celda(t_datos.getValueAt(i, 26).toString(), font, contenido, derecha, 0,1,Rectangle.RECTANGLE));
                            else
                                tabla.addCell(reporte.celda(" ", font, contenido, derecha, 0,1,Rectangle.RECTANGLE));
                            
                            if( ((boolean)t_datos.getValueAt(i, 27)) ==true)
                                tabla.addCell(reporte.celda("x", font, contenido, derecha, 0,1,Rectangle.RECTANGLE));
                            else
                                tabla.addCell(reporte.celda(" ", font, contenido, derecha, 0,1,Rectangle.RECTANGLE));
                            
                        }
                        
                        tabla.setHeaderRows(1);
                        reporte.agregaObjeto(tabla);
                        reporte.cerrar();
                        reporte.visualizar2(ruta+".pdf");
                    }catch(Exception e)
                    {
                        System.out.println(e);
                        e.printStackTrace();
                        JOptionPane.showMessageDialog(this, "No se pudo realizar el reporte si el archivo esta abierto.");
                    }
                    if(session!=null)
                    if(session.isOpen())
                    session.close();
                }
            }
        }
    }//GEN-LAST:event_jButton11ActionPerformed

    private void jButton12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton12ActionPerformed
        // TODO add your handling code here:
        h=new Herramientas(this.user, 0);
        h.session(sessionPrograma);
        Session session = HibernateUtil.getSessionFactory().openSession();
        try
        {
            session.beginTransaction().begin();
            user = (Usuario)session.get(Usuario.class, user.getIdUsuario());
            if(user.getEditaCodigo()==true)
            {
                altaEjemplar obj = new altaEjemplar(new javax.swing.JFrame(), true, user, sessionPrograma, 0);
                Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
                obj.setLocation((d.width/2)-(obj.getWidth()/2), 10);
                obj.setModal(false);
                obj.setAlwaysOnTop(true);
                obj.setVisible(true);
                obj.getReturnStatus();
            }
            else
            {
                JOptionPane.showMessageDialog(this, "Acceso denegado.");
            }
        }catch(Exception e)
        {
            System.out.println(e);
        }
        finally
        {
            if(session.isOpen())
                session.close();
        }
    }//GEN-LAST:event_jButton12ActionPerformed

    private void t_datosMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_t_datosMouseMoved
        // TODO add your handling code here:
        /*if(boton==true)
        {
            System.out.println("Hola");
            viewport.setPreferredSize(t_titulos.getSize());
            viewport.updateUI();
        }*/
    }//GEN-LAST:event_t_datosMouseMoved

    private void b_exelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_exelActionPerformed
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
                    Sheet hoja = libro.createSheet("reporte1");
                    for(int ren=0;ren<(t_datos.getRowCount()+1);ren++)
                    {
                        Row fila = hoja.createRow(ren);
                        for(int col=0; col<t_datos.getColumnCount()+(t_titulos.getColumnCount()-1); col++)
                        {
                            Cell celda = fila.createCell(col);
                            if(ren==0)
                            {
                                if(col<3)
                                    celda.setCellValue(t_titulos.getColumnName(col));
                                else
                                    celda.setCellValue(t_datos.getColumnName(col-3));
                            }
                            else
                            {
                                try
                                {
                                    if(col<3)
                                        celda.setCellValue(t_titulos.getValueAt(ren-1, col).toString());
                                    else
                                    {
                                        if(t_datos.getValueAt(ren-1, col-3).toString().compareToIgnoreCase("false")==0)
                                            celda.setCellValue("");
                                        else
                                        {
                                            if(t_datos.getValueAt(ren-1, col-3).toString().compareToIgnoreCase("true")==0)
                                                celda.setCellValue("x");
                                            else
                                                celda.setCellValue(t_datos.getValueAt(ren-1, col-3).toString());
                                        }
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

    private void t_user1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_t_user1ActionPerformed
        // TODO add your handling code here:
        t_clave1.requestFocus();
    }//GEN-LAST:event_t_user1ActionPerformed

    private void b2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b2ActionPerformed
        // TODO add your handling code here:
        if(t_user1.getText().compareTo("")!=0)
        {
            if(t_clave1.getPassword().toString().compareTo("")!=0)
            {
                Session session = HibernateUtil.getSessionFactory().openSession();
                try
                {
                    session.beginTransaction().begin();
                    Usuario autoriza = (Usuario)session.createCriteria(Usuario.class).add(Restrictions.eq("idUsuario", t_user1.getText())).add(Restrictions.eq("clave", t_clave1.getText())).setMaxResults(1).uniqueResult();
                    if(autoriza!=null)
                    {
                        if(autoriza.getAutorizarSobrecosto()==true)
                        {
                            usrAut1=autoriza;
                            autorizar1.dispose();
                        }
                        else
                            JOptionPane.showMessageDialog(this, "¡el usuario no tiene permiso de autorizar!");    
                    }
                    else
                    {
                        session.beginTransaction().rollback();
                        JOptionPane.showMessageDialog(this, "¡Datos Incorrectos!");
                    }
                }catch(Exception e)
                {
                    session.beginTransaction().rollback();
                    JOptionPane.showMessageDialog(this, "¡Error al consultar los datos!");
                    e.printStackTrace();
                }
                finally
                {
                    if(session.isOpen()==true)
                        session.close();
                }
            }
            else
                JOptionPane.showMessageDialog(this, "¡Ingrese la contraseña!");
        }
        else
            JOptionPane.showMessageDialog(this, "¡Ingrese el Usuario!");
    }//GEN-LAST:event_b2ActionPerformed

    private void t_claveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_t_claveActionPerformed
        // TODO add your handling code here:
        b_1.requestFocus();
    }//GEN-LAST:event_t_claveActionPerformed

    private void t_clave1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_t_clave1ActionPerformed
        // TODO add your handling code here:
        b2.requestFocus();
    }//GEN-LAST:event_t_clave1ActionPerformed

    private void t_user2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_t_user2ActionPerformed
        // TODO add your handling code here:
        t_clave2.requestFocus();
    }//GEN-LAST:event_t_user2ActionPerformed

    private void t_clave2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_t_clave2ActionPerformed
        // TODO add your handling code here:
        b3.requestFocus();
    }//GEN-LAST:event_t_clave2ActionPerformed

    private void b3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b3ActionPerformed
        // TODO add your handling code here:
        if(t_user2.getText().compareTo("")!=0)
        {
            if(t_clave2.getPassword().toString().compareTo("")!=0)
            {
                Session session = HibernateUtil.getSessionFactory().openSession();
                try
                {
                    session.beginTransaction().begin();
                    Usuario autoriza = (Usuario)session.createCriteria(Usuario.class).add(Restrictions.eq("idUsuario", t_user2.getText())).add(Restrictions.eq("clave", t_clave2.getText())).setMaxResults(1).uniqueResult();
                    if(autoriza!=null)
                    {
                        if(autoriza.getRespaldar()==true)
                        {
                            usrAut2=autoriza;
                            autorizar2.dispose();
                        }
                        else
                            JOptionPane.showMessageDialog(this, "¡el usuario no tiene permiso de autorizar!");    
                    }
                    else
                    {
                        session.beginTransaction().rollback();
                        JOptionPane.showMessageDialog(this, "¡Datos Incorrectos!");
                    }
                }catch(Exception e)
                {
                    session.beginTransaction().rollback();
                    JOptionPane.showMessageDialog(this, "¡Error al consultar los datos!");
                    e.printStackTrace();
                }
                finally
                {
                    if(session.isOpen()==true)
                        session.close();
                }
            }
            else
                JOptionPane.showMessageDialog(this, "¡Ingrese la contraseña!");
        }
        else
            JOptionPane.showMessageDialog(this, "¡Ingrese el Usuario!");
    }//GEN-LAST:event_b3ActionPerformed

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        // TODO add your handling code here:
        if(t_pedidos.getSelectedRow()> -1)
        {
            if(t_pedidos.getValueAt(t_pedidos.getSelectedRow(), 3).toString().compareTo("Interno")==0)
            {
                f1=new Formatos(this.user, this.sessionPrograma, this.ord, t_pedidos.getValueAt(t_pedidos.getSelectedRow(), 0).toString());
                f1.ordenCompraDCG(cb_tipo.getSelectedItem().toString());
            }
            else
            {
                f1=new Formatos(this.user, this.sessionPrograma, this.ord, t_pedidos.getValueAt(t_pedidos.getSelectedRow(), 0).toString());
                f1.ordenCompraExternosDCG(Integer.parseInt(t_pedidos.getValueAt(t_pedidos.getSelectedRow(), 0).toString()), cb_tipo.getSelectedItem().toString());
            }
        }
        else
        {
            JOptionPane.showMessageDialog(this, "¡Seleccione primero un pedido de la lista!");
        }
    }//GEN-LAST:event_jButton10ActionPerformed

    private void t_titulosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_t_titulosMouseClicked
        // TODO add your handling code here:
        if (evt.getClickCount() == 2) {
            if(t_datos.getSelectedRow()>-1)
            {
                String valor=t_datos.getModel().getValueAt(t_datos.getSelectedRow(), 26).toString();
                if(valor.compareTo("")!=0)
                {
                    Pedido ped =new Pedido();
                    ped.setIdPedido(Integer.parseInt(valor));
                    editaPedido = new editaPedido(this.user, sessionPrograma, ped, 90);
                    editaPedido.busca();
                    Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
                    centro.removeAll();
                    centro.add(editaPedido);
                    muestra.setSize(1180,600);
                    muestra.setLocation((d.width/2)-(muestra.getWidth()/2), (d.height/2)-(muestra.getHeight()/2));
                    centro.repaint();
                    centro.updateUI();
                    muestra.setVisible(true);
                    h= new Herramientas(user, 90);
                    h.session(sessionPrograma);
                    h.desbloqueaOrden();
                    h.desbloqueaPedido();
                }
                else
                    JOptionPane.showMessageDialog(null, "La partida aun no esta pedida"); 
            }
        }
    }//GEN-LAST:event_t_titulosMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JDialog autorizar1;
    private javax.swing.JDialog autorizar2;
    private javax.swing.JDialog autorizarCosto;
    private javax.swing.JButton b2;
    private javax.swing.JButton b3;
    private javax.swing.JButton b_1;
    private javax.swing.JButton b_busca;
    private javax.swing.JButton b_busca1;
    private javax.swing.JButton b_exel;
    private javax.swing.JButton b_muestra;
    private javax.swing.JButton b_tot;
    private javax.swing.JComboBox cb_tipo;
    private javax.swing.JPanel centro;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
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
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JPasswordField jPasswordField1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JLabel l4;
    private javax.swing.JDialog listaPedidos;
    private javax.swing.JComboBox medida;
    private javax.swing.JDialog muestra;
    private javax.swing.JComboBox numeros;
    private javax.swing.JRadioButton r_cerrar;
    private javax.swing.JScrollPane scroll;
    private javax.swing.JFormattedTextField t_autorizado;
    private javax.swing.JTextField t_busca;
    private javax.swing.JFormattedTextField t_cia_seguros;
    private javax.swing.JPasswordField t_clave;
    private javax.swing.JPasswordField t_clave1;
    private javax.swing.JPasswordField t_clave2;
    private javax.swing.JFormattedTextField t_costo_refacciones;
    private javax.swing.JTable t_datos;
    public javax.swing.JTable t_datos2;
    private javax.swing.JTextField t_empresa;
    private javax.swing.JFormattedTextField t_importe;
    private javax.swing.JFormattedTextField t_importe1;
    private javax.swing.JTable t_pedidos;
    private javax.swing.JFormattedTextField t_ref_adi;
    private javax.swing.JTable t_titulos;
    private javax.swing.JTextField t_user;
    private javax.swing.JTextField t_user1;
    private javax.swing.JTextField t_user2;
    private javax.swing.JDialog ventana;
    private javax.swing.JDialog ventanaAdicional;
    // End of variables declaration//GEN-END:variables

public class MyModel extends DefaultTableModel
{

    Class[] types;
    int ren=0;
    int col=0;
    private boolean[][] celdaEditable;
    
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
            switch(this.getColumnName(col))
            {
                case "M":
                        if(vector.get(col)==null)
                        {
                            vector.setElementAt(value, col);
                            this.dataVector.setElementAt(vector, row);
                            fireTableCellUpdated(row, col);
                        }
                        else
                        {
                            Session session = HibernateUtil.getSessionFactory().openSession();
                            session.beginTransaction().begin();
                            user = (Usuario)session.get(Usuario.class, user.getIdUsuario());
                            if(user.getGeneraPedidos())
                            {
                                Partida part=(Partida) session.createCriteria(Partida.class).add(Restrictions.eq("ordenByIdOrden.idOrden", Integer.parseInt(orden))).add(Restrictions.eq("idEvaluacion", Integer.parseInt(t_titulos.getValueAt(t_datos.getSelectedRow(), 0).toString()))).add(Restrictions.eq("subPartida", Integer.parseInt(t_titulos.getValueAt(t_datos.getSelectedRow(), 1).toString()))).setMaxResults(1).uniqueResult();
                                try
                                {
                                    if(part!=null)
                                    {
                                        part.setMuestra((boolean)value);
                                        session.update(part);
                                        session.getTransaction().commit();
                                        vector.setElementAt(value, col);
                                        this.dataVector.setElementAt(vector, row);
                                        fireTableCellUpdated(row, col);
                                        if(session.isOpen()==true)
                                            session.close();
                                    }
                                    else
                                    {
                                        buscaCuentas(-1,-1);
                                        JOptionPane.showMessageDialog(null, "La partida ya no existe");
                                    }
                                }
                                catch(Exception e)
                                {
                                    session.getTransaction().rollback();
                                    System.out.println(e);
                                }
                                finally
                                {
                                    if(session.isOpen()==true)
                                        session.close();
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
                        break;
                    
                case "Codigo":
                        if(vector.get(col)==null)
                        {
                            vector.setElementAt(value, col);
                            this.dataVector.setElementAt(vector, row);
                            fireTableCellUpdated(row, col);
                        }
                        else
                        {
                            Session session = HibernateUtil.getSessionFactory().openSession();
                            session.beginTransaction().begin();
                            user = (Usuario)session.get(Usuario.class, user.getIdUsuario());
                            if(user.getEditaCodigo()==true)
                            {
                                Partida part=(Partida) session.createCriteria(Partida.class).add(Restrictions.eq("ordenByIdOrden.idOrden", Integer.parseInt(orden))).add(Restrictions.eq("idEvaluacion", Integer.parseInt(t_titulos.getValueAt(t_datos.getSelectedRow(), 0).toString()))).add(Restrictions.eq("subPartida", Integer.parseInt(t_titulos.getValueAt(t_datos.getSelectedRow(), 1).toString()))).setMaxResults(1).uniqueResult();
                                try
                                {
                                    if(part!=null)
                                    {
                                        if(value.toString().compareToIgnoreCase("")!=0)
                                        {
                                            Ejemplar ejem = (Ejemplar)session.get(Ejemplar.class, value.toString());
                                            if(ejem!=null)
                                            {
                                                /*Criteria crit = session.createCriteria(Partida.class);
                                                crit.add(Restrictions.eq("ejemplar.idParte", ejem.getIdParte()));
                                                crit = crit.createAlias("pedido", "ped");
                                                //crit=crit.addOrder(Order.asc("ped.fechaPedido"));
                                                crit=crit.addOrder(Order.desc("pcp"));
                                                crit.add(Restrictions.isNotNull("pedido"));
                                                Partida partidaPrecio=(Partida) crit.setMaxResults(1).uniqueResult();*/
                                                part.setEjemplar(ejem);
                                                /*if(partidaPrecio!=null)
                                                {
                                                    if(partidaPrecio.getPcp()>part.getCU())
                                                    {
                                                        part.setCU(partidaPrecio.getPcp());
                                                        part.setPrecioAutCU(0.0+Math.round(part.getCU()/(1-(part.getPorcentaje()*0.01))));
                                                        part.setPrecioCiaSegurosCU(part.getPrecioAutCU());

                                                        vector.setElementAt(part.getCU(), 11);
                                                        fireTableCellUpdated(row, 11);
                                                        if(part.getPorcentaje()==0.0)
                                                        {
                                                            vector.setElementAt(part.getCU(), 13);
                                                        }
                                                        else
                                                        {
                                                            vector.setElementAt(0.0+Math.round(part.getCU()/(1-(part.getPorcentaje()*0.01))), 13);
                                                        }
                                                        fireTableCellUpdated(row, 13);
                                                        vector.setElementAt(part.getPrecioAutCU(), 15);
                                                        fireTableCellUpdated(row, 15);
                                                        vector.setElementAt(part.getPrecioAutCU()*part.getCantidadAut(), 16);
                                                        fireTableCellUpdated(row, 16);
                                                    }
                                                }*/
                                                session.update(part);
                                                session.getTransaction().commit();
                                                vector.setElementAt(value, col);
                                                this.dataVector.setElementAt(vector, row);
                                                fireTableCellUpdated(row, col);
                                                if(session.isOpen()==true)
                                                    session.close();
                                            }
                                        }
                                        else
                                        {
                                            part.setEjemplar(null);
                                            //part.setCU(0.0d);
                                            //part.setPrecioAutCU(0.0+Math.round(part.getCU()/(1-(part.getPorcentaje()*0.01))));
                                            session.update(part);
                                            session.getTransaction().commit();
                                            vector.setElementAt(value, col);
                                            this.dataVector.setElementAt(vector, row);
                                            fireTableCellUpdated(row, col);
                                            if(session.isOpen()==true)
                                                session.close();
                                        }
                                    }
                                    else
                                    {
                                        buscaCuentas(-1,-1);
                                        JOptionPane.showMessageDialog(null, "La partida ya no existe");
                                    }
                                }
                                catch(Exception e)
                                {
                                    session.getTransaction().rollback();
                                    System.out.println(e);
                                }
                                finally
                                {
                                    if(session.isOpen()==true)
                                        session.close();
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
                        break;
                case "Prov.":
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
                                    if(r_cerrar.isSelected()==false)
                                    {
                                        Session session = HibernateUtil.getSessionFactory().openSession();
                                        session.beginTransaction().begin();
                                        String aux=(String)value;
                                        if(aux.compareTo("")!=0)
                                        {
                                            try
                                            {
                                                Proveedor prov=(Proveedor)session.get(Proveedor.class, Integer.parseInt(aux));
                                                if(prov==null)
                                                    value="";
                                            }catch(Exception e){value="";}
                                        }
                                        vector.setElementAt(value, col);
                                        this.dataVector.setElementAt(vector, row);
                                        fireTableCellUpdated(row, col);

                                        if(session!=null)
                                            if(session.isOpen())
                                                session.close();
                                    }
                                    else
                                        JOptionPane.showMessageDialog(null, "Las compras estan cerradas"); 
                                }
                                else
                                    JOptionPane.showMessageDialog(null, "Acceso denegado no puedes modificar Pedidos"); 
                            }
                            else
                                JOptionPane.showMessageDialog(null, "Acceso denegado"); 
                        }
                        break;
                case "Cant C."://CANT COMPRADA
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
                                    if(r_cerrar.isSelected()==false)
                                    {
                                        if(value.toString().compareTo("")!=0)
                                        {
                                            if((double)value>=0)
                                            {
                                                if((double)value<=(double)t_datos.getModel().getValueAt(row, 14))
                                                {
                                                    Session session = HibernateUtil.getSessionFactory().openSession();
                                                    session.beginTransaction().begin();
                                                    Partida part=(Partida) session.createCriteria(Partida.class).add(Restrictions.eq("ordenByIdOrden.idOrden", Integer.parseInt(orden))).add(Restrictions.eq("idEvaluacion", Integer.parseInt(t_titulos.getValueAt(t_datos.getSelectedRow(), 0).toString()))).add(Restrictions.eq("subPartida", Integer.parseInt(t_titulos.getValueAt(t_datos.getSelectedRow(), 1).toString()))).setMaxResults(1).uniqueResult();
                                                    try
                                                    {
                                                        if(part!=null)
                                                        {
                                                            part.setCantPcp(Double.parseDouble(value.toString()));
                                                            session.update(part);
                                                            session.getTransaction().commit();
                                                            vector.setElementAt(value, col);
                                                            this.dataVector.setElementAt(vector, row);
                                                            fireTableCellUpdated(row, col);
                                                        }
                                                        else
                                                            JOptionPane.showMessageDialog(null, "La partida ya no existe"); 
                                                    }
                                                    catch(Exception e)
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
                                                }
                                                else
                                                    JOptionPane.showMessageDialog(null, "La cantidad mayor a compras es:"+t_datos.getModel().getValueAt(row, 14).toString()); 
                                            }
                                            else
                                                JOptionPane.showMessageDialog(null, "El campo no permite numeros negativos"); 
                                        }
                                    }
                                    else
                                        JOptionPane.showMessageDialog(null, "Las compras estan cerradas"); 
                                }
                                else
                                    JOptionPane.showMessageDialog(null, "Acceso denegado no puedes modificar Pedidos"); 
                            }
                            else
                                JOptionPane.showMessageDialog(null, "Acceso denegado"); 
                        }
                        break;

                case "$C/U Comp":
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
                                    if(r_cerrar.isSelected()==false)
                                    {
                                        if(value.toString().compareTo("")==0)
                                        {
                                            value=0.0;
                                        }
                                            if((double)value>=0)
                                            {
                                                if((double)value<=(double)t_datos.getModel().getValueAt(row, 11))
                                                {
                                                    Session session = HibernateUtil.getSessionFactory().openSession();
                                                    session.beginTransaction().begin();
                                                    Partida part=(Partida) session.createCriteria(Partida.class).add(Restrictions.eq("ordenByIdOrden.idOrden", Integer.parseInt(orden))).add(Restrictions.eq("idEvaluacion", Integer.parseInt(t_titulos.getValueAt(t_datos.getSelectedRow(), 0).toString()))).add(Restrictions.eq("subPartida", Integer.parseInt(t_titulos.getValueAt(t_datos.getSelectedRow(), 1).toString()))).setMaxResults(1).uniqueResult();
                                                    try
                                                    {
                                                        if(part!=null)
                                                        {
                                                            part.setPcp((double)value);
                                                            part.setUsuario(null);
                                                            session.update(part);
                                                            session.getTransaction().commit();
                                                            vector.setElementAt(value, col);
                                                            this.dataVector.setElementAt(vector, row);
                                                            fireTableCellUpdated(row, col);
                                                        }
                                                        else
                                                            JOptionPane.showMessageDialog(null, "La partida ya no existe"); 
                                                    }
                                                    catch(Exception e)
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
                                                }
                                                else
                                                {
                                                    usrAut=null;
                                                    Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
                                                    autorizarCosto.setSize(284, 177);
                                                    autorizarCosto.setLocation((d.width/2)-(autorizarCosto.getWidth()/2), (d.height/2)-(autorizarCosto.getHeight()/2));
                                                    t_user.setText("");
                                                    t_clave.setText("");
                                                    autorizarCosto.setVisible(true);
                                                    if(usrAut!=null)
                                                    {
                                                        Session session = HibernateUtil.getSessionFactory().openSession();
                                                        session.beginTransaction().begin();
                                                        Partida part=(Partida) session.createCriteria(Partida.class).add(Restrictions.eq("ordenByIdOrden.idOrden", Integer.parseInt(orden))).add(Restrictions.eq("idEvaluacion", Integer.parseInt(t_titulos.getValueAt(t_datos.getSelectedRow(), 0).toString()))).add(Restrictions.eq("subPartida", Integer.parseInt(t_titulos.getValueAt(t_datos.getSelectedRow(), 1).toString()))).setMaxResults(1).uniqueResult();
                                                        try
                                                        {
                                                            if(part!=null)
                                                            {
                                                                part.setPcp((double)value);
                                                                part.setUsuario(usrAut);
                                                                session.update(part);
                                                                session.getTransaction().commit();
                                                                vector.setElementAt(value, col);
                                                                this.dataVector.setElementAt(vector, row);
                                                                fireTableCellUpdated(row, col);
                                                            }
                                                            else
                                                                JOptionPane.showMessageDialog(null, "La partida ya no existe"); 
                                                        }
                                                        catch(Exception e)
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
                                                    }
                                                }
                                            }
                                            else
                                                JOptionPane.showMessageDialog(null, "El campo no permite numeros negativos"); 
                                    }
                                    else
                                        JOptionPane.showMessageDialog(null, "Las compras estan cerradas"); 
                                }
                                else
                                    JOptionPane.showMessageDialog(null, "Acceso denegado no puedes modificar Pedidos"); 
                            }
                            else
                                JOptionPane.showMessageDialog(null, "Acceso denegado"); 
                        }
                        break;

                case "Entrega":
                            vector.setElementAt(value, col);
                            this.dataVector.setElementAt(vector, row);
                            fireTableCellUpdated(row, col);
                        break;
                case "OK":
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
                                    Session session = HibernateUtil.getSessionFactory().openSession();
                                    session.beginTransaction().begin();

                                    Partida part=(Partida) session.createCriteria(Partida.class).add(Restrictions.eq("ordenByIdOrden.idOrden", Integer.parseInt(orden))).add(Restrictions.eq("idEvaluacion", Integer.parseInt(t_titulos.getValueAt(t_datos.getSelectedRow(), 0).toString()))).add(Restrictions.eq("subPartida", Integer.parseInt(t_titulos.getValueAt(t_datos.getSelectedRow(), 1).toString()))).setMaxResults(1).uniqueResult();
                                    try
                                    {
                                        if(part!=null)
                                        {
                                            if((Boolean)value==true)
                                            {
                                                part.setSurtido((Boolean)value);
                                                session.update(part);
                                                session.getTransaction().commit();
                                                vector.setElementAt(value, col);
                                                this.dataVector.setElementAt(vector, row);
                                                fireTableCellUpdated(row, col);
                                            }
                                            else
                                            {
                                                if(user.getAutorizarPedidos()==true)
                                                {
                                                    part.setSurtido((Boolean)value);
                                                    session.update(part);
                                                    session.getTransaction().commit();
                                                    vector.setElementAt(value, col);
                                                    this.dataVector.setElementAt(vector, row);
                                                    fireTableCellUpdated(row, col);
                                                }
                                                else
                                                    JOptionPane.showMessageDialog(null, "¡Acceso denegado!"); 
                                            }
                                        }
                                        else
                                            JOptionPane.showMessageDialog(null, "La partida ya no existe"); 
                                    }
                                    catch(Exception e)
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
    }

    void exite(String busca, int par)
    {
        int entro=0;
        for(int a=0; a< pedidos.size(); a++)
        {
            List proveedores=(ArrayList)pedidos.get(a);
            if(proveedores.get(0).toString().compareToIgnoreCase(busca)==0)
            {
                entro=1;
                proveedores.add(par);
                pedidos.set(a, proveedores);
                break;
            }
        }
        if(entro==0)
        {
            List proveedores=new ArrayList();
            proveedores.add(busca);
            proveedores.add(par);
            pedidos.add(proveedores);
        }
    }
    
    DefaultTableModel ModeloTablaPedidos(int renglones, String columnas[])
    {
            modeloPedidos = new DefaultTableModel(new Object [renglones][2], columnas)
            {
                Class[] types = new Class [] {
                    java.lang.String.class, 
                    java.lang.String.class, 
                    java.lang.String.class, 
                    java.lang.String.class
                };
                boolean[] canEdit = new boolean [] {
                    false, false, false, false
                };

                public void setValueAt(Object value, int row, int col)
                 {
                        Vector vector = (Vector)this.dataVector.elementAt(row);
                        Object celda = ((Vector)this.dataVector.elementAt(row)).elementAt(col);
                        vector.setElementAt(value, col);
                        this.dataVector.setElementAt(vector, row);
                        fireTableCellUpdated(row, col);
                 }
                
                public Class getColumnClass(int columnIndex) {
                    return types [columnIndex];
                }

                public boolean isCellEditable(int rowIndex, int columnIndex) {
                    return canEdit [columnIndex];
                }
            };
            return modeloPedidos;
        }
    
    public void visualiza(boolean ver, int op)
    {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try
        {
            session.beginTransaction().begin();
            ord=(Orden)session.get(Orden.class, Integer.parseInt(orden));    
            if(ord.getCierreRefacciones()!=null && op==0)
            {
                b_busca.setEnabled(false);
                //t_datos.setEnabled(false);
                /*for(int x=0; x<t_datos.getColumnCount(); x++)
                {
                    model.setColumnaEditable(x, ver);
                }*/
                r_cerrar.setEnabled(true);
                b_tot.setEnabled(false);
                this.jButton4.setEnabled(false);
                this.jButton2.setEnabled(false);
                this.jButton3.setEnabled(false);
            }
            else
            {
                b_busca.setEnabled(ver);
                t_datos.setEnabled(ver);
                //for(int x=0; x<t_datos.getColumnCount(); x++)
                //{
                    //model.setColumnaEditable(x, ver);
                //}
                //model.setColumnaEditable(26, ver);
                r_cerrar.setEnabled(ver);
                b_tot.setEnabled(ver);
                this.jButton4.setEnabled(ver);
                this.jButton2.setEnabled(ver);
                this.jButton3.setEnabled(ver);
            }
        }catch(Exception e)
        {
            e.printStackTrace();
            session.getTransaction().rollback();
        }
        finally
        {
            if(session.isOpen())
                session.close();
        }
    }
    public void cargaPedidos()
    {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction().begin();
        List misPedidos=null;
        List misPedidosEx=null;
        
         Query query = session.createQuery("SELECT DISTINCT ped FROM Pedido ped "
                 + "LEFT JOIN FETCH ped.partidas part "
                 + "LEFT JOIN part.ordenByIdOrden ord "
                 + "where ord.idOrden="+orden);
         misPedidos = query.list();
         
         Query query1 = session.createQuery("SELECT ped FROM Pedido ped where orden.idOrden="+orden);
         misPedidosEx = query1.list();
         
         if(misPedidos.size()>0 || misPedidosEx.size()>0)
         {
             t_pedidos.setModel(ModeloTablaPedidos(misPedidos.size()+misPedidosEx.size(), colPedidos));
             int i=0;
            if(misPedidos.size()>0)
            {
               for(i=0; i<misPedidos.size(); i++)
               {
                   Pedido Part = (Pedido) misPedidos.get(i);
                   modeloPedidos.setValueAt(Part.getIdPedido(), i, 0);
                   modeloPedidos.setValueAt(Part.getProveedorByIdProveedor().getNombre(), i, 1);
                   modeloPedidos.setValueAt(Part.getFechaPedido(), i, 2);
                   modeloPedidos.setValueAt(Part.getTipoPedido(), i, 3);
               }
            }
            if(misPedidosEx.size()>0)
            {
               for(int j=0; j<misPedidosEx.size(); j++)
               {
                   Pedido Part = (Pedido) misPedidosEx.get(j);
                   modeloPedidos.setValueAt(Part.getIdPedido(), j+i, 0);
                   modeloPedidos.setValueAt(Part.getProveedorByIdProveedor().getNombre(), j+i, 1);
                   modeloPedidos.setValueAt(Part.getFechaPedido(), j+i, 2);
                   modeloPedidos.setValueAt(Part.getTipoPedido(), j+i, 3);
               }
            }
         }
         else
             t_pedidos.setModel(ModeloTablaPedidos(0, colPedidos));
         this.formatoPedidos();
         if(session!=null)
             if(session.isOpen())
                 session.close();
    }
    
    public void sumaTotales()
    {
        double refacciones_costo=0.0;
        double cia_seg=0.0;
        double autorizado=0.0;
        BigDecimal compra=new BigDecimal("0.00");
        
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction().begin();
        Configuracion con=(Configuracion)session.get(Configuracion.class, 1);
        this.ord=(Orden)session.get(Orden.class, ord.getIdOrden());
        if(ord!=null)
        {
            Partida[] partidas=(Partida[])ord.getPartidasForIdOrden().toArray(new Partida[0]);
            double suma=0.0d;
            for(int p=0; p<partidas.length; p++)
            {
                if(partidas[p].getPedido()!=null)
                {
                    suma+=/*Math.round(*/partidas[p].getCantPcp()*partidas[p].getPcp()/*)*/;
                }
            }
            if(ord.getPedidos().size()>0)
            {
                Pedido [] adicionales=(Pedido[])ord.getPedidos().toArray(new Pedido[0]);
                if(adicionales.length>0)
                {
                    for(int a=0; a<adicionales.length; a++)
                    {
                        PartidaExterna[] pe = (PartidaExterna[])adicionales[a].getPartidaExternas().toArray(new PartidaExterna[0]);
                        for(int b=0; b<pe.length; b++)
                        {
                            suma+=/*Math.round(*/pe[b].getCantidad()*pe[b].getCosto()/*)*/;
                        }
                    }
                }
            }
            compra=new BigDecimal(suma);
        }
        
        for(int ren=0; ren<t_datos.getRowCount(); ren++)
        {
            refacciones_costo+=Double.parseDouble(t_datos.getModel().getValueAt(ren, 11).toString())*Double.parseDouble(t_datos.getValueAt(ren, 7).toString());
            cia_seg+=Double.parseDouble(t_datos.getModel().getValueAt(ren, 13).toString())*Double.parseDouble(t_datos.getValueAt(ren, 7).toString());
            autorizado+=Double.parseDouble(t_datos.getModel().getValueAt(ren, 16).toString());
            /*if(t_datos.getValueAt(ren, 27).toString().compareTo("")!=0)
            {
                compra+=Double.parseDouble(t_datos.getValueAt(ren, 24).toString())*Double.parseDouble(t_datos.getValueAt(ren, 25).toString());
            }*/
        }

        if(this.user.getAutorizarPedidos()==true)
        {
            t_costo_refacciones.setValue(refacciones_costo);
            t_cia_seguros.setValue(cia_seg);
            t_autorizado.setValue(autorizado);
        }
        else
        {
            t_costo_refacciones.setValue(0.0);
            t_cia_seguros.setValue(0.0);
            t_autorizado.setValue(0.0);
        }
        t_importe.setValue(compra.doubleValue());
        if(session!=null)
             if(session.isOpen())
                 session.close();
    }
    
    private boolean consultaLista()
    {
        for(int ren=0; ren<t_datos.getRowCount(); ren++)
        {
            if((Double.parseDouble(t_datos.getValueAt(ren, 24).toString()))==0.00 && t_datos.getValueAt(ren, 22).toString().compareTo("")!=0)
            {
                t_datos.setRowSelectionInterval(ren, ren);
                t_datos.setColumnSelectionInterval(24, 24);
                return false;
            }
            if(Double.parseDouble(t_datos.getValueAt(ren, 23).toString())==0.00 && t_datos.getValueAt(ren, 22).toString().compareTo("")!=0)
            {
                t_datos.setRowSelectionInterval(ren, ren);
                t_datos.setColumnSelectionInterval(22, 22);
                return false;
            }
        }
        return true;
    }

    
    void envia()
    {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try
        {
            session.beginTransaction().begin();
            Usuario[] autoriza = (Usuario[])session.createCriteria(Usuario.class).add(Restrictions.eq("autorizarPedidos", true)).list().toArray(new Usuario[0]);
            if(autoriza!=null)
            {
                String correos="";
                for(int y=0; y<autoriza.length; y++)
                {
                    correos+=autoriza[y].getEmpleado().getEmail()+";";
                }
                for(int x=0; x< id_pedido.size(); x++)
                {
                    List vec = (ArrayList)id_pedido.get(x);
                    enviaCorreo("Nuevo Pedido ("+vec.get(0).toString()+")", "Hola buen dia, se te comunica que se genero el pedido No:"+vec.get(0).toString()+" para que sea revisado y autorizado saludos.", correos);
                }
            }
            session.beginTransaction().rollback();
        }catch(HibernateException e)
        {
            e.printStackTrace();
            session.getTransaction().rollback();
        }
        finally
        {
            if(session.isOpen())
                session.close();
        }
    }
    
    public void enviaCorreo(String asunto, String mensaje, String from)
    {
        String smtp="";
        boolean ttl=false;
        String puerto="";
        String envia="";
        String clave="";
        //String from="";
        String cc="";
        String texto = null;
        
        try
        {
            FileReader f = new FileReader("correo.ml");
            BufferedReader b = new BufferedReader(f);
            int renglon=0;
            while((texto = b.readLine())!=null)
            {
                switch(renglon)
                {
                    case 1://smtp
                        smtp=texto.trim();
                        break;
                    case 2://ttl
                        if(texto.compareToIgnoreCase("true")==0)
                            ttl=true;
                        else
                            ttl=false;
                        break;
                    case 3://puerto
                        puerto=texto.trim();
                        break;
                    case 4://cuenta
                        envia=texto.trim();
                        break;
                    case 5://contraseña
                        clave=texto.trim();
                        break;
                }
                renglon+=1;
            }
            b.close();
        }catch(Exception e){e.printStackTrace();}
        
        try
        {
            // se obtiene el objeto Session.
            Properties props = new Properties();
            props.put("mail.smtp.host", smtp);
            props.setProperty("mail.smtp.starttls.enable", ""+ttl);
            props.setProperty("mail.smtp.port", puerto);
            props.setProperty("mail.smtp.user", envia);
            props.setProperty("mail.smtp.auth", "true");

            javax.mail.Session session = javax.mail.Session.getDefaultInstance(props, null);
            // session.setDebug(true);

            // Se compone la parte del texto
            BodyPart texto_mensaje = new MimeBodyPart();
            texto_mensaje.setText(mensaje);

            // Una MultiParte para agrupar texto e imagen.
            MimeMultipart multiParte = new MimeMultipart();
            multiParte.addBodyPart(texto_mensaje);

            // Se compone el correo, dando to, from, subject y el contenido.
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(envia));

            String [] direcciones=from.split(";");
            for(int x=0; x<direcciones.length; x++)
            {
                if(direcciones[x].compareTo("")!=0)
                    message.addRecipient(Message.RecipientType.TO, new InternetAddress(direcciones[x].replace(" ","")));
            }

            String [] dirCC=cc.split(";");
            for(int y=0; y<dirCC.length; y++)
            {
                if(dirCC[y].compareTo("")!=0)
                    message.addRecipient(Message.RecipientType.CC, new InternetAddress(dirCC[y].replace(" ","")));
            }

            message.setSubject(asunto);
            message.setContent(multiParte);

            Transport t = session.getTransport("smtp");
            t.connect(envia, clave);
            t.sendMessage(message, message.getAllRecipients());
            t.close();
        }
        catch (MessagingException e)
        {
            e.printStackTrace();
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

            Configuracion con= (Configuracion)session.get(Configuracion.class, 1);
            reporte.inicioTexto();
            reporte.contenido.setFontAndSize(bf, 14);
            reporte.contenido.setColorFill(BaseColor.BLACK);
            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, con.getEmpresa(), 35, 575, 0);
            reporte.contenido.setFontAndSize(bf, 8);
            reporte.contenido.setColorFill(BaseColor.BLACK);
            String titulo="Reporte";
            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, titulo, 35, 565, 0);
            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_RIGHT, "Fecha:"+new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date()), 750, 575, 0);
                
            reporte.finTexto();
            //agregamos renglones vacios para dejar un espacio
            reporte.agregaObjeto(new Paragraph(" "));
            /*reporte.agregaObjeto(new Paragraph(" "));
            reporte.agregaObjeto(new Paragraph(" "));
            reporte.agregaObjeto(new Paragraph(" "));*/
            //reporte.agregaObjeto(new Paragraph(" "));
            
            Font font = new Font(Font.FontFamily.HELVETICA, 6, Font.BOLD);
            
            BaseColor cabecera=BaseColor.GRAY;
            BaseColor contenido=BaseColor.WHITE;
            int centro=Element.ALIGN_CENTER;
            int izquierda=Element.ALIGN_LEFT;
            int derecha=Element.ALIGN_RIGHT;
            
            tabla.addCell(reporte.celda("NO", font, cabecera, centro, 0, 1, Rectangle.RECTANGLE));
            tabla.addCell(reporte.celda("#", font, cabecera, centro, 0, 1, Rectangle.RECTANGLE));
            tabla.addCell(reporte.celda("DESCRIPCION", font, cabecera, centro, 0, 1, Rectangle.RECTANGLE));
            tabla.addCell(reporte.celda("H", font, cabecera, centro, 0, 1, Rectangle.RECTANGLE));
            tabla.addCell(reporte.celda("M", font, cabecera, centro, 0, 1, Rectangle.RECTANGLE));
            tabla.addCell(reporte.celda("S", font, cabecera, centro, 0, 1, Rectangle.RECTANGLE));
            tabla.addCell(reporte.celda("E", font, cabecera, centro, 0, 1, Rectangle.RECTANGLE));
            tabla.addCell(reporte.celda("P", font, cabecera, centro, 0, 1, Rectangle.RECTANGLE));
            tabla.addCell(reporte.celda("C. AUT", font, cabecera, centro, 0, 1, Rectangle.RECTANGLE));
            tabla.addCell(reporte.celda("MED", font, cabecera, centro, 0, 1, Rectangle.RECTANGLE));
            tabla.addCell(reporte.celda("NO° PART", font, cabecera, centro, 0, 1, Rectangle.RECTANGLE));
            tabla.addCell(reporte.celda("$ AUT", font, cabecera, centro, 0, 1, Rectangle.RECTANGLE));
            tabla.addCell(reporte.celda("AUT", font, cabecera, centro, 0, 1, Rectangle.RECTANGLE));
            tabla.addCell(reporte.celda("COT", font, cabecera, centro, 0, 1, Rectangle.RECTANGLE));
            tabla.addCell(reporte.celda("COM", font, cabecera, centro, 0, 1, Rectangle.RECTANGLE));
            tabla.addCell(reporte.celda("ORI", font, cabecera, centro, 0, 1, Rectangle.RECTANGLE));
            tabla.addCell(reporte.celda("NO PED", font, cabecera, centro, 0, 1, Rectangle.RECTANGLE));
            tabla.addCell(reporte.celda("OK", font, cabecera, centro, 0, 1, Rectangle.RECTANGLE));
       }catch(Exception e)
       {
           System.out.println(e);
       }
       if(session!=null)
            if(session.isOpen())
                session.close();
    }
   
   private void checkSelection(boolean isFixedTable) 
   {
       if(habilita==true)
       {
            int t_titulosSelectedIndex = t_titulos.getSelectedRow();
            int t_datosSelectedIndex = t_datos.getSelectedRow();
            if(t_titulosSelectedIndex != t_datosSelectedIndex) 
            {
                if (isFixedTable) 
                {
                    t_datos.setRowSelectionInterval(t_titulosSelectedIndex,t_titulosSelectedIndex);
                    java.awt.Rectangle r = t_datos.getCellRect( t_titulosSelectedIndex, -1, true);
                    t_datos.scrollRectToVisible(r);
                }
                else 
                {
                    t_titulos.setRowSelectionInterval(t_datosSelectedIndex, t_datosSelectedIndex);
                    java.awt.Rectangle r = t_titulos.getCellRect( t_datosSelectedIndex, 2, true);
                    t_titulos.scrollRectToVisible(r);
                }
            }
       }
   }
   
   public double totalActual()
   {
       double total=0.0d;
       for(int x=0; x<t_datos.getRowCount(); x++)
       {
           if(t_datos.getModel().getValueAt(x, 22).toString().compareTo("")!=0 && t_datos.getModel().getValueAt(x, 26).toString().compareTo("")==0)
           {
               total+=Double.parseDouble(t_datos.getModel().getValueAt(x, 23).toString())*Double.parseDouble(t_datos.getModel().getValueAt(x, 24).toString());
           }
       }
       return total;
   }
}
