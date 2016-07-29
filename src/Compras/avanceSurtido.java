/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Compras;

import Hibernate.Util.HibernateUtil;
import Hibernate.entidades.Almacen;
import Hibernate.entidades.Configuracion;
import Hibernate.entidades.Movimiento;
import Hibernate.entidades.Orden;
import Hibernate.entidades.Partida;
import Hibernate.entidades.PartidaExterna;
import Hibernate.entidades.Pedido;
import Hibernate.entidades.Usuario;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Element;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.GrayColor;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPTable;
import java.awt.Color;
import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Vector;
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
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import Integral.DefaultTableHeaderCellRenderer;
import Integral.ExtensionFileFilter;
import Integral.FormatoTabla;
import Integral.Herramientas;
import Integral.HorizontalBarUI;
import Integral.PDF;
import Integral.VerticalBarUI;
import Integral.VerticalTableHeaderCellRenderer;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.Collection;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;

/**
 *
 * @author salvador
 */
public class avanceSurtido extends javax.swing.JPanel {

    /**
     * Creates new form avanceSurtido
     */
    private String orden;
    private Usuario user;
    //DefaultTableModel model;
    String sessionPrograma="";
    Herramientas h;
    private Usuario usrAut;
    int entro=0, x=0;
    
    String[] columnas = new String [] {
        "Id","No","#","Descripcion",
        "Esp Hoj","Esp Mec","Esp Sus","Esp Ele", 
        "Can","Med","Fol","Codigo","Ori","Prov.","Cant C.","$C/U Comp","Plazo","Pedido","F.Pedido","Alm.","Ope.","Pend.", "Factura", "OK"};
    //private Session session;
    Orden ord;
    FormatoTabla formato;
    MyModel model;
        Class[] types = new Class [] {
                    java.lang.String.class/*Id*/, 
                    java.lang.String.class/*No*/, 
                    java.lang.String.class/*#*/, 
                    java.lang.String.class/*Descripcion*/, 
                    
                    java.lang.Boolean.class/*Hoj*/, 
                    java.lang.Boolean.class/*Mec*/, 
                    java.lang.Boolean.class/*Susp*/, 
                    java.lang.Boolean.class/*Ele*/, 

                    java.lang.Double.class/*Can*/, 
                    java.lang.String.class/*Med*/, 
                    java.lang.String.class/*Fol*/, 
                    java.lang.String.class/*Cod*/, 
                    
                    java.lang.String.class/*Ori*/,
                    
                    java.lang.String.class/*Proveedor*/, 
                    java.lang.Double.class/*Cant C.*/, 
                    java.lang.Double.class/*c/u* Com*/,
                    java.lang.Integer.class/*Plazo*/, 
                    java.lang.Integer.class/*Pedido*/, 
                    java.lang.String.class/*fecha pedido*/,
                    java.lang.Double.class/*entradas*/, 
                    java.lang.Double.class/*dev*/, 
                    java.lang.Double.class/*pendiente*/, 
                    java.lang.Integer.class/*numero factura*/,
                    java.lang.Boolean.class/*OK*/
                };
    List noPartida;
    
    public avanceSurtido(String ord, Usuario us, String ses) {
        initComponents();
        scroll.getVerticalScrollBar().setUI(new VerticalBarUI());
        scroll.getHorizontalScrollBar().setUI(new HorizontalBarUI());
        sessionPrograma=ses;
        orden=ord;
        user=us;
        cargarPedidos();
        t_datos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        model=new MyModel(1, columnas, this.types);
        t_datos.setModel(model);
        formatoTabla();
        formato = new FormatoTabla();
        buscaCuentas();
    }

    
    public void formatoTabla()
    {
        TableCellRenderer textNormal = new DefaultTableHeaderCellRenderer();        
        TableCellRenderer headerRenderer = new VerticalTableHeaderCellRenderer();
        //TableCellRenderer headerRenderer = new RotatedTableCellRenderer(270);
        Enumeration columns = t_datos.getColumnModel().getColumns();
        
        for(int x=0; x<t_datos.getColumnModel().getColumnCount(); x++)
        {
            if(x>3 && x<9)
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
    
   public void tabla_tamaños()
   {
        TableColumnModel col_model = t_datos.getColumnModel();
        DefaultTableCellRenderer tcr = new DefaultTableCellRenderer();
        tcr.setHorizontalAlignment(SwingConstants.RIGHT);
        //FormatoEditor fe=new FormatoEditor();
        //t_datos.setDefaultEditor(Integer.class, fe);

        for (int i=0; i<t_datos.getColumnCount(); i++)
        {
  	      TableColumn column = col_model.getColumn(i);
              switch(i)
              {
                  case 0://Id
                      column.setPreferredWidth(56);
                      //column.setCellRenderer(tcr);
                      break;
                  case 1://N°
                      column.setPreferredWidth(56);
                      //column.setCellRenderer(tcr);
                      break;
                  case 2://#
                      column.setPreferredWidth(40);
                      //column.setCellRenderer(tcr);
                      break;
                  case 3://descripcion
                      column.setPreferredWidth(310);
                      break;
                  case 8://cant
                      column.setPreferredWidth(45);
                      break;
                  case 9://medida
                      column.setPreferredWidth(50);
                      break;
                  case 10://folio
                      column.setPreferredWidth(50);
                      break;
                      
                  case 11://codigo
                      column.setPreferredWidth(100);
                      //DefaultCellEditor miEditor = new DefaultCellEditor(numeros);
                      //miEditor.setClickCountToStart(2);
                      //column.setCellEditor(miEditor); 
                      //column.setCellRenderer(tcr);
                      break;

                  case 12://original
                      column.setPreferredWidth(50);
                      break;

                  case 13://proveedor
                      column.setPreferredWidth(50);
                      break;
                  
                  case 14://cant c
                      column.setPreferredWidth(45);
                      break;
                  case 15://cant tot
                      column.setPreferredWidth(85);
                      break;
                  case 16://Plazo
                      column.setPreferredWidth(35);
                      break;
                 case 17://PEDIDO
                      column.setPreferredWidth(50);
                      break;
                 case 18://f.PEDIDO
                      column.setPreferredWidth(100);
                      break;
                 case 19://Entradas
                      column.setPreferredWidth(45);
                      break;
                 case 20://Dev
                      column.setPreferredWidth(45);
                      break;
                 case 21://Pendiente
                      column.setPreferredWidth(45);
                      break;
                 case 22://N FACTURA
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
   
   
   private void buscaCuentas()
    {
        double imp=0.0;
        if(orden!=null)
        {
            Session session = HibernateUtil.getSessionFactory().openSession();
            try
            {   
                session.beginTransaction().begin();
                ord = (Orden)session.get(Orden.class, Integer.parseInt(orden));
                user=(Usuario)session.get(Usuario.class, user.getIdUsuario());
                imp=ord.getCompania().getImporteHora();
                List partidas=new ArrayList();
                Query query, query2;
                ArrayList partidas1=new ArrayList();
                if(c_filtro.getSelectedIndex()<=0)
                {   
                    query=session.createSQLQuery("select partida.id_partida as id, partida.id_evaluacion, partida.sub_partida, catalogo.nombre, partida.esp_hoj, partida.esp_mec, partida.esp_sus, partida.esp_ele, \n" +
"partida.cant, partida.med, partida.id_parte, if(partida.ori=1, 'ORI', if(partida.nal=1, 'NAL', if(partida.desm=1, 'DES', if(partida.pd=1, 'REC', '')))) as ori, pedido.id_proveedor, \n" +
"partida.cant_pcp, partida.pcp, partida.plazo, partida.id_pedido, pedido.fecha_pedido,\n" +
"(\n" +
"(select if( sum(movimiento.cantidad) is null, 0, sum(movimiento.cantidad)) as can\n" +
"from movimiento inner join almacen on movimiento.id_almacen=almacen.id_almacen where id_partida=id and almacen.tipo_movimiento=1 and almacen.operacion in (1, 4))\n" +
"-\n" +
"(select if( sum(movimiento.cantidad) is null, 0, sum(movimiento.cantidad)) as can\n" +
"from movimiento inner join almacen on movimiento.id_almacen=almacen.id_almacen where id_partida=id and almacen.tipo_movimiento=2 and almacen.operacion in (1, 4))) as almacen,\n" +
"(\n" +
"(select if( sum(movimiento.cantidad) is null, 0, sum(movimiento.cantidad)) as can\n" +
"from movimiento inner join almacen on movimiento.id_almacen=almacen.id_almacen where id_partida=id and almacen.tipo_movimiento=2 and almacen.operacion=5)\n" +
"-\n" +
"(select if( sum(movimiento.cantidad) is null, 0, sum(movimiento.cantidad)) as can\n" +
"from movimiento inner join almacen on movimiento.id_almacen=almacen.id_almacen where id_partida=id and almacen.tipo_movimiento=1 and almacen.operacion=5)) as operario, \n" +
"surtido   \n" +
"from partida inner join catalogo on partida.id_catalogo=catalogo.id_catalogo\n" +
"inner join pedido on pedido.id_pedido=partida.id_pedido where partida.id_orden="+ord.getIdOrden()+" order by partida.id_partida asc;");
                    query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
                    partidas1=(ArrayList)query.list();
                    query2 = session.createSQLQuery("select partida_externa.id_partida_externa as id, partida_externa.descripcion, partida_externa.cantidad, partida_externa.unidad, \n" +
"partida_externa.noParte, pedido.id_proveedor, partida_externa.costo, partida_externa.plazo, partida_externa.id_pedido, pedido.fecha_pedido,\n" +
"partida_externa.surtido, \n" +
"(select if( sum(movimiento.cantidad) is null, 0, sum(movimiento.cantidad)) as can\n" +
"from movimiento inner join almacen on movimiento.id_almacen=almacen.id_almacen where movimiento.id_externos=id and almacen.tipo_movimiento=1 and almacen.operacion = 3)\n" +
"-\n" +
"(select if( sum(movimiento.cantidad) is null, 0, sum(movimiento.cantidad)) as can\n" +
"from movimiento inner join almacen on movimiento.id_almacen=almacen.id_almacen where movimiento.id_externos=id and almacen.tipo_movimiento=2 and almacen.operacion = 3) as almacen,\n" +
"(\n" +
"(select if( sum(movimiento.cantidad) is null, 0, sum(movimiento.cantidad)) as can\n" +
"from movimiento inner join almacen on movimiento.id_almacen=almacen.id_almacen where movimiento.id_externos=id and almacen.tipo_movimiento=2 and almacen.operacion=5)\n" +
"- \n" +
"(select if( sum(movimiento.cantidad) is null, 0, sum(movimiento.cantidad)) as can\n" +
"from movimiento inner join almacen on movimiento.id_almacen=almacen.id_almacen where movimiento.id_externos=id and almacen.tipo_movimiento=1 and almacen.operacion=5)) as operario \n" +
"from partida_externa inner join pedido on partida_externa.id_pedido=pedido.id_pedido where id_orden="+ord.getIdOrden()+" order by partida_externa.id_externos;");
                    query2.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
                    partidas = (ArrayList)query2.list();
                }
                else
                {
                    query=session.createSQLQuery("select partida.id_partida as id, partida.id_evaluacion, partida.sub_partida, catalogo.nombre, partida.esp_hoj, partida.esp_mec, partida.esp_sus, partida.esp_ele, \n" +
"partida.cant, partida.med, partida.id_parte, if(partida.ori=1, 'ORI', if(partida.nal=1, 'NAL', if(partida.desm=1, 'DES', if(partida.pd=1, 'REC', '')))) as ori, pedido.id_proveedor, \n" +
"partida.cant_pcp, partida.pcp, partida.plazo, partida.id_pedido, pedido.fecha_pedido,\n" +
"(\n" +
"(select if( sum(movimiento.cantidad) is null, 0, sum(movimiento.cantidad)) as can\n" +
"from movimiento inner join almacen on movimiento.id_almacen=almacen.id_almacen where id_partida=id and almacen.tipo_movimiento=1 and almacen.operacion in (1, 4))\n" +
"-\n" +
"(select if( sum(movimiento.cantidad) is null, 0, sum(movimiento.cantidad)) as can\n" +
"from movimiento inner join almacen on movimiento.id_almacen=almacen.id_almacen where id_partida=id and almacen.tipo_movimiento=2 and almacen.operacion in (1, 4))) as almacen,\n" +
"(\n" +
"(select if( sum(movimiento.cantidad) is null, 0, sum(movimiento.cantidad)) as can\n" +
"from movimiento inner join almacen on movimiento.id_almacen=almacen.id_almacen where id_partida=id and almacen.tipo_movimiento=2 and almacen.operacion=5)\n" +
"-\n" +
"(select if( sum(movimiento.cantidad) is null, 0, sum(movimiento.cantidad)) as can\n" +
"from movimiento inner join almacen on movimiento.id_almacen=almacen.id_almacen where id_partida=id and almacen.tipo_movimiento=1 and almacen.operacion=5)) as operario, \n" +
"surtido   \n" +
"from partida inner join catalogo on partida.id_catalogo=catalogo.id_catalogo\n" +
"inner join pedido on pedido.id_pedido=partida.id_pedido where partida.id_pedido="+c_filtro.getSelectedItem().toString()+" order by partida.id_partida asc;");
                    query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
                    partidas1=(ArrayList)query.list();
                    query2 = session.createSQLQuery("select partida_externa.id_partida_externa as id, partida_externa.descripcion, partida_externa.cantidad, partida_externa.unidad, \n" +
"partida_externa.noParte, pedido.id_proveedor, partida_externa.costo, partida_externa.plazo, partida_externa.id_pedido, pedido.fecha_pedido,\n" +
"partida_externa.surtido, \n" +
"(select if( sum(movimiento.cantidad) is null, 0, sum(movimiento.cantidad)) as can\n" +
"from movimiento inner join almacen on movimiento.id_almacen=almacen.id_almacen where movimiento.id_externos=id and almacen.tipo_movimiento=1 and almacen.operacion = 3)\n" +
"-\n" +
"(select if( sum(movimiento.cantidad) is null, 0, sum(movimiento.cantidad)) as can\n" +
"from movimiento inner join almacen on movimiento.id_almacen=almacen.id_almacen where movimiento.id_externos=id and almacen.tipo_movimiento=2 and almacen.operacion = 3) as almacen,\n" +
"(\n" +
"(select if( sum(movimiento.cantidad) is null, 0, sum(movimiento.cantidad)) as can\n" +
"from movimiento inner join almacen on movimiento.id_almacen=almacen.id_almacen where movimiento.id_externos=id and almacen.tipo_movimiento=2 and almacen.operacion=5)\n" +
"- \n" +
"(select if( sum(movimiento.cantidad) is null, 0, sum(movimiento.cantidad)) as can\n" +
"from movimiento inner join almacen on movimiento.id_almacen=almacen.id_almacen where movimiento.id_externos=id and almacen.tipo_movimiento=1 and almacen.operacion=5)) as operario \n" +
"from partida_externa inner join pedido on partida_externa.id_pedido=pedido.id_pedido where partida_externa.id_pedido="+c_filtro.getSelectedItem().toString()+" order by partida_externa.id_externos;");
                    query2.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
                    partidas = (ArrayList)query2.list();
                }
                int renglones=partidas1.size()+partidas.size();
                if(renglones>0)
                {
                    model=new MyModel(renglones, columnas, this.types);
                    t_datos.setModel(model);
                }
                else
                {
                    model=new MyModel(0, columnas, this.types);
                    t_datos.setModel(model);
                }
                if(partidas1.size()>0)
                {                    
                    noPartida=new ArrayList();
                    for(int i=0; i<partidas1.size(); i++)
                    {
                        java.util.HashMap map=(java.util.HashMap)partidas1.get(i);
                        noPartida.add(map.get("id"));
                        model.setValueAt(map.get("id"), i, 0);
                        model.setValueAt(map.get("id_evaluacion"), i, 1);
                        model.setValueAt(map.get("sub_partida"), i, 2);
                        model.setValueAt(map.get("nombre"), i, 3);

                        model.setValueAt(map.get("esp_hoj"), i, 4);
                        model.setValueAt(map.get("esp_mec"), i, 5);
                        model.setValueAt(map.get("esp_sus"), i, 6);
                        model.setValueAt(map.get("esp_ele"), i, 7);
                        
                        model.setValueAt(map.get("cant"), i, 8);
                        model.setValueAt(map.get("med"), i, 9);
                        
                        model.setValueAt(map.get(""), i, 10);
                        model.setValueAt(map.get("id_parte"), i, 11);
                        model.setValueAt(map.get("ori"), i, 12);
                        model.setValueAt(map.get("id_proveedor"), i, 13);//proveedor
                        model.setValueAt(map.get("cant_pcp"), i, 14);//cant c
                        model.setValueAt(map.get("pcp"), i, 15);//C/U Com
                        model.setValueAt(map.get("plazo"), i, 16);//plazo de entrega
                        model.setValueAt(map.get("id_pedido"), i, 17);//pedido
                        model.setValueAt(map.get("fecha_pedido"), i, 18);//pedido
                        model.setValueAt(map.get("almacen"), i, 19);
                        model.setValueAt(map.get("operario"), i, 20);//pedido
                        
                        double pen=Double.parseDouble(map.get("cant_pcp").toString())-Double.parseDouble(map.get("almacen").toString());
                        model.setValueAt(pen, i, 21);//pedido
                        model.setValueAt("", i, 22);//surtido
                        model.setValueAt((boolean)map.get("surtido"), i, 23);//surtido
                        model.setCeldaEditable(i, 23, true);
                    }
                }
                                
                if(partidas.size()>0)
                {
                    //PartidaExterna[] parext = (PartidaExterna[]) partidas.toArray(new PartidaExterna[0]);
                    for(int i=0; i<partidas.size(); i++)
                    {
                        java.util.HashMap map1=(java.util.HashMap)partidas.get(i);
                       
                        model.setValueAt(map1.get("id"), i+partidas1.size(), 0);
                        model.setValueAt(0, i+partidas1.size(), 1);
                        model.setValueAt(0, i+partidas1.size(), 2);
                        model.setValueAt(map1.get("descripcion"), i+partidas1.size(), 3);
                        
                        model.setValueAt(false, i+partidas1.size(), 4);
                        model.setValueAt(false, i+partidas1.size(), 5);
                        model.setValueAt(false, i+partidas1.size(), 6);
                        model.setValueAt(false, i+partidas1.size(), 7);
                        model.setValueAt(map1.get("cantidad"), i+partidas1.size(), 8);
                        
                        model.setValueAt(map1.get("unidad"), i+partidas1.size(), 9);
                        model.setValueAt("", i+partidas1.size(), 10);
                        model.setValueAt(map1.get("noParte"), i+partidas1.size(), 11);
                        model.setValueAt("", i+partidas1.size(), 12);
                        model.setValueAt(map1.get("id_proveedor"), i+partidas1.size(), 13);
                        
                        model.setValueAt(map1.get("cantidad"), i+partidas1.size(), 14);
                        
                        model.setValueAt(map1.get("costo"), i+partidas1.size(), 15);
                        
                        model.setValueAt(map1.get("plazo"), i+partidas1.size(), 16);
                        model.setValueAt(map1.get("id_pedido"), i+partidas1.size(), 17);
                        model.setValueAt(map1.get("fecha_pedido"), i+partidas1.size(), 18);
                        model.setValueAt(map1.get("almacen"), i+partidas1.size(), 19);
                        model.setValueAt(map1.get("operario"), i+partidas1.size(), 20);

                        //double pen=entradas-devoluciones;
                        double pen=Double.parseDouble(map1.get("cantidad").toString())-Double.parseDouble(map1.get("almacen").toString());

                        model.setValueAt(pen, i+partidas1.size(), 21);
                        model.setValueAt("", i+partidas1.size(), 22);//no factura
                        
                        model.setValueAt(map1.get("surtido"), i+partidas1.size(), 23);//surtido
                        model.setCeldaEditable(i+partidas1.size(), 23, true);
                    }
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
            }
        }
        else
        {
            model=new MyModel(0, columnas, this.types);
            t_datos.setModel(model);
        }
        formatoTabla();
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        autorizarCosto = new javax.swing.JDialog();
        jPanel9 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        t_user = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        t_clave = new javax.swing.JPasswordField();
        jButton10 = new javax.swing.JButton();
        jLabel14 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        c_filtro = new javax.swing.JComboBox();
        jLabel1 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        t_busca = new javax.swing.JTextField();
        b_busca1 = new javax.swing.JButton();
        scroll = new javax.swing.JScrollPane();
        t_datos = new javax.swing.JTable();

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

        jButton10.setBackground(new java.awt.Color(2, 135, 242));
        jButton10.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jButton10.setForeground(new java.awt.Color(254, 254, 254));
        jButton10.setText("Autorizar");
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
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
                        .addComponent(jButton10)
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
                .addComponent(jButton10)
                .addContainerGap())
        );

        autorizarCosto.getContentPane().add(jPanel9, java.awt.BorderLayout.CENTER);

        setLayout(new java.awt.BorderLayout());

        jPanel1.setBackground(new java.awt.Color(2, 135, 242));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jButton1.setIcon(new ImageIcon("imagenes/exel.png"));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 0, 45, 40));

        jButton2.setIcon(new ImageIcon("imagenes/pdf.png"));
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, 45, 40));

        c_filtro.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Todos" }));
        c_filtro.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                c_filtroItemStateChanged(evt);
            }
        });
        jPanel1.add(c_filtro, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 7, 140, -1));

        jLabel1.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(254, 254, 254));
        jLabel1.setText("Filtrar por:");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 13, -1, -1));

        jLabel4.setFont(new java.awt.Font("Arial", 0, 9)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Buscar:");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 10, -1, -1));

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
        jPanel1.add(t_busca, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 10, 223, -1));

        b_busca1.setIcon(new ImageIcon("imagenes/buscar1.png"));
        b_busca1.setToolTipText("Busca una partida");
        b_busca1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_busca1ActionPerformed(evt);
            }
        });
        jPanel1.add(b_busca1, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 10, 23, 23));

        add(jPanel1, java.awt.BorderLayout.PAGE_END);

        scroll.setPreferredSize(new java.awt.Dimension(453, 150));

        t_datos.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        t_datos.setForeground(new java.awt.Color(102, 102, 102));
        t_datos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Id", "N°", "#", "Grupo", "D/M", "Rep Min ", "Title 7", "Title 8", "Title 9", "Title 10", "Title 11", "Title 12", "Title 13", "Title 14", "Title 15", "Title 16", "Title 17", "Title 18", "Title 19", "Title 20", "Title 21", "Title 22"
            }
        ));
        t_datos.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_LAST_COLUMN);
        t_datos.setGridColor(new java.awt.Color(102, 102, 102));
        t_datos.getTableHeader().setReorderingAllowed(false);
        scroll.setViewportView(t_datos);

        add(scroll, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void c_filtroItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_c_filtroItemStateChanged
        // TODO add your handling code here:
        buscaCuentas();
    }//GEN-LAST:event_c_filtroItemStateChanged

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        h=new Herramientas(this.user, 0);
        h.session(sessionPrograma);
        File archivoXLS=null;
        javax.swing.JFileChooser jF1= new javax.swing.JFileChooser(); 
        jF1.setFileFilter(new ExtensionFileFilter("Excel document (*.xls)", new String[] { "xls" }));
        String ruta = null; 
        if(jF1.showSaveDialog(null)==jF1.APPROVE_OPTION)
        { 
            ruta = jF1.getSelectedFile().getAbsolutePath(); 
            if(ruta!=null)
            {
                archivoXLS = new File(ruta+".xls");
                try
                {
                    if(archivoXLS.exists())
                        archivoXLS.delete();
                    archivoXLS.createNewFile();
                    Workbook libro = new HSSFWorkbook();
                    FileOutputStream archivo = new FileOutputStream(archivoXLS);
                    Sheet hoja = libro.createSheet("Avance de pedidos");
                    Font font = libro.createFont();
                    font.setFontHeightInPoints((short)24);
                    font.setFontName("Arial");
                    font.setItalic(false);
                    font.setBold(true);

                    // Fonts are set into a style so create a new one to use.
                    CellStyle style = libro.createCellStyle();
                    style.setFont(font);
                    
                    Session session = HibernateUtil.getSessionFactory().openSession();
                    session.beginTransaction().begin();
                    Orden ord=(Orden)session.get(Orden.class, Integer.parseInt(orden));

                    Configuracion con= (Configuracion)session.get(Configuracion.class, 1);
                    hoja.setColumnWidth(2, 15000);
                    Row r0=hoja.createRow(0);
                    Cell celdaTitulo=r0.createCell(0);
                    celdaTitulo.setCellValue(con.getEmpresa());
                    celdaTitulo.setCellStyle(style);
                    
                    Row r1=hoja.createRow(1);
                    r1.createCell(6).setCellValue("Orden de Taller:");
                    r1.createCell(7).setCellValue(orden);
                    
                    Row r2=hoja.createRow(2);
                    r2.createCell(0).setCellValue("Marca:");
                    r2.createCell(1).setCellValue(ord.getMarca().getMarcaNombre());
                    r2.createCell(6).setCellValue("Nº Serie:");
                    r2.createCell(7).setCellValue(ord.getNoSerie());
                    
                    Row r3=hoja.createRow(3);
                    r3.createCell(0).setCellValue("Tipo:");
                    r3.createCell(1).setCellValue(ord.getTipo().getTipoNombre());
                    r3.createCell(6).setCellValue("Nº Motor:");
                    r3.createCell(7).setCellValue(ord.getNoMotor());
                    
                    Row r4=hoja.createRow(4);
                    r4.createCell(0).setCellValue("Modelo:");
                    r4.createCell(1).setCellValue(""+ord.getModelo());
                    
                    hoja.createRow(5).createCell(0).setCellValue("**********************************************************************************************************************************************************************************************************************************************************************************************************************************************************");
                    
                    Row r6=hoja.createRow(6);
                    r6.createCell(0).setCellValue("Partida");
                    r6.createCell(1).setCellValue("sub");
                    r6.createCell(2).setCellValue("Descripcion");
                    r6.createCell(3).setCellValue("Hoj");
                    r6.createCell(4).setCellValue("Mec");
                    r6.createCell(5).setCellValue("Sus");
                    r6.createCell(6).setCellValue("Ele");
                    r6.createCell(7).setCellValue("Can");
                    r6.createCell(8).setCellValue("Med");
                    r6.createCell(9).setCellValue("Folio");
                    r6.createCell(10).setCellValue("Codigo");
                    r6.createCell(11).setCellValue("Origen");
                    r6.createCell(12).setCellValue("Proveedor");
                    r6.createCell(13).setCellValue("Cant C.");
                    r6.createCell(14).setCellValue("C/U Comprado");
                    r6.createCell(15).setCellValue("Plazo");
                    r6.createCell(16).setCellValue("Pedido");
                    r6.createCell(17).setCellValue("F. Pedido");
                    r6.createCell(18).setCellValue("Entradas");
                    r6.createCell(19).setCellValue("Devoluciones");
                    r6.createCell(20).setCellValue("Pendientes");
                    r6.createCell(21).setCellValue("No Factura");
                    
                    hoja.createRow(7).createCell(0).setCellValue("**********************************************************************************************************************************************************************************************************************************************************************************************************************************************************");

                    session.close();
                    if(t_datos.getRowCount()>0)
                    {
                        for(int i=0; i<t_datos.getRowCount(); i++)
                        {
                            
                            Row fila = hoja.createRow(i+8);
                            for(int j=1; j<t_datos.getColumnCount(); j++)
                            {
                                if(j>3 && j<8)
                                {
                                    if((boolean)t_datos.getValueAt(i, j)==true)
                                        fila.createCell(j-1).setCellValue("✓");
                                    else
                                        fila.createCell(j-1).setCellValue("");
                                }
                                else
                                {
                                    if(t_datos.getValueAt(i, j)!=null)
                                        fila.createCell(j-1).setCellValue(""+t_datos.getValueAt(i, j));
                                    else
                                        fila.createCell(j-1).setCellValue("");
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
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(this, "No se pudo realizar el reporte si el archivo esta abierto");
                }
            }
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        try
        {
            File archivoPDF=null;
            javax.swing.JFileChooser jF1= new javax.swing.JFileChooser(); 
            jF1.setFileFilter(new ExtensionFileFilter("Adobe document (*.pdf)", new String[] { "pdf" }));
            String ruta = null; 
            if(jF1.showSaveDialog(null)==jF1.APPROVE_OPTION)
            {
                ruta = jF1.getSelectedFile().getAbsolutePath(); 
                DecimalFormat formatoPorcentaje = new DecimalFormat("#,##0.00");
                formatoPorcentaje.setMinimumFractionDigits(2);
                if(ruta!=null)
                {
                    if(ruta.substring(ruta.length()-4, ruta.length()).compareTo(".pdf")==0)
                        archivoPDF = new File(ruta);
                    else
                        archivoPDF = new File(ruta+".pdf");
                    BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.WINANSI, BaseFont.NOT_EMBEDDED);
                    //Orden ord=buscaApertura();
                    PDF reporte = new PDF();
                    reporte.Abrir2(PageSize.LEGAL.rotate(), "Avance de Pedidos", archivoPDF.getAbsolutePath());

                    reporte.agregaObjeto(reporte.crearImagen("imagenes/empresa300115.jpg", 00, -32, 17));

                    reporte.contenido.setLineWidth(0.5f);
                    reporte.contenido.setColorStroke(new GrayColor(0.2f));
                    reporte.contenido.setColorFill(new GrayColor(0.9f));
                    reporte.contenido.roundRectangle(30, 535, 945, 60, 5);

                    Session session = HibernateUtil.getSessionFactory().openSession();
                    session.beginTransaction().begin();
                    Configuracion con= (Configuracion)session.get(Configuracion.class, 1);
                    
                    reporte.inicioTexto();
                        reporte.contenido.setFontAndSize(bf, 14);
                        reporte.contenido.setColorFill(BaseColor.BLACK);
                        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_CENTER, con.getEmpresa(), 505, 580, 0);
                        reporte.contenido.setFontAndSize(bf, 8);
                        reporte.contenido.setColorFill(BaseColor.BLACK);
                        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_CENTER, "HOLAJATERIA Y PINTURA EN GENERAL", 505, 567, 0);
                        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_CENTER, "COMPRA Y VENTA DE REFACCIONES", 505, 557, 0);
                        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_CENTER, "Avance de Pedidos de la orden: "+ord.getIdOrden(), 505, 537, 0);
                    reporte.finTexto();
                    //agregamos renglones vacios para dejar un espacio
                    reporte.agregaObjeto(new Paragraph(" "));
                    reporte.agregaObjeto(new Paragraph(" "));
                    reporte.agregaObjeto(new Paragraph(" "));

                    float tam[]=new float[]{25,25,190,7,7,7,7,20,30,20,100,20,30,20,60,50,30,70,35,30,30,30,7};
                    com.itextpdf.text.Font font = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 6, com.itextpdf.text.Font.BOLD);
                    PdfPTable tabla=reporte.crearTabla(tam.length, tam, 100, Element.ALIGN_LEFT);
                    BaseColor cabecera=BaseColor.GRAY;
                    BaseColor contenido=BaseColor.WHITE;
                    int centro=Element.ALIGN_CENTER;
                    int izquierda=Element.ALIGN_LEFT;
                    int derecha=Element.ALIGN_RIGHT;

                    tabla.addCell(reporte.celda("No", font, cabecera, centro, 0, 1,Rectangle.RECTANGLE));
                    tabla.addCell(reporte.celda("#", font, cabecera, centro, 0,1, Rectangle.RECTANGLE));
                    tabla.addCell(reporte.celda("Descripción", font, cabecera, centro, 0,1, Rectangle.RECTANGLE));
                    tabla.addCell(reporte.celda("Hoj", font, cabecera, centro, 0, 1,Rectangle.RECTANGLE));
                    tabla.addCell(reporte.celda("Mec", font, cabecera, centro, 0,1,Rectangle.RECTANGLE));
                    tabla.addCell(reporte.celda("Sus", font, cabecera, centro, 0, 1,Rectangle.RECTANGLE));
                    tabla.addCell(reporte.celda("Ele", font, cabecera, centro, 0, 1,Rectangle.RECTANGLE));
                    tabla.addCell(reporte.celda("Can", font, cabecera, centro, 0, 1,Rectangle.RECTANGLE));
                    tabla.addCell(reporte.celda("Med", font, cabecera, centro, 0, 1,Rectangle.RECTANGLE));
                    tabla.addCell(reporte.celda("Fol", font, cabecera, centro, 0, 1,Rectangle.RECTANGLE));
                    tabla.addCell(reporte.celda("Codigo", font, cabecera, centro, 0, 1,Rectangle.RECTANGLE));
                    tabla.addCell(reporte.celda("Ori", font, cabecera, centro, 0, 1,Rectangle.RECTANGLE));
                    tabla.addCell(reporte.celda("Prov", font, cabecera, centro, 0, 1,Rectangle.RECTANGLE));
                    tabla.addCell(reporte.celda("Cant. c.", font, cabecera, centro, 0, 1,Rectangle.RECTANGLE));
                    tabla.addCell(reporte.celda("$C/U Comp", font, cabecera, centro, 0, 1,Rectangle.RECTANGLE));
                    tabla.addCell(reporte.celda("Plazo", font, cabecera, centro, 0, 1,Rectangle.RECTANGLE));
                    tabla.addCell(reporte.celda("Pedido", font, cabecera, centro, 0, 1,Rectangle.RECTANGLE));
                    tabla.addCell(reporte.celda("F. Pedido", font, cabecera, centro, 0, 1,Rectangle.RECTANGLE));
                    tabla.addCell(reporte.celda("Ent.", font, cabecera, centro, 0, 1,Rectangle.RECTANGLE));
                    tabla.addCell(reporte.celda("Dev.", font, cabecera, centro, 0, 1,Rectangle.RECTANGLE));
                    tabla.addCell(reporte.celda("Pend.", font, cabecera, centro, 0, 1,Rectangle.RECTANGLE));
                    tabla.addCell(reporte.celda("No Factura", font, cabecera, centro, 0, 1,Rectangle.RECTANGLE));
                    tabla.addCell(reporte.celda("OK", font, cabecera, centro, 0, 1,Rectangle.RECTANGLE));
                    session.close();
                    //for de renflones
                    double sum=0.0d;
                    for(int ren=0; ren<t_datos.getRowCount(); ren++)
                    {
                        sum+=(Double.parseDouble(t_datos.getValueAt(ren, 14).toString())*Double.parseDouble(t_datos.getValueAt(ren, 15).toString()));
                            
                        for(int col=1; col<t_datos.getColumnCount(); col++)
                        {
                            if(col>3 && col<8 || col==23)
                            {
                                if((boolean)t_datos.getValueAt(ren, col)==true)
                                    tabla.addCell(reporte.celda("X", font, contenido, izquierda, 0,1,Rectangle.RECTANGLE));
                                else
                                    tabla.addCell(reporte.celda("", font, contenido, izquierda, 0,1,Rectangle.RECTANGLE));
                            }
                            else
                            {
                                if(t_datos.getValueAt(ren, col)!=null)
                                {
                                    if(col==14 || col==15 || col==19 || col==20 || col==21)
                                    {
                                        if(col==15)
                                        {
                                            tabla.addCell(reporte.celda(formatoPorcentaje.format(Double.parseDouble(t_datos.getValueAt(ren, col).toString())), font, contenido, derecha, 0,1,Rectangle.RECTANGLE));
                                        }
                                        else
                                            tabla.addCell(reporte.celda(t_datos.getValueAt(ren, col).toString(), font, contenido, derecha, 0,1,Rectangle.RECTANGLE));
                                    }
                                    else
                                    {
                                        tabla.addCell(reporte.celda(t_datos.getValueAt(ren, col).toString(), font, contenido, izquierda, 0,1,Rectangle.RECTANGLE));
                                    }
                                }
                                else
                                    tabla.addCell(reporte.celda("", font, contenido, izquierda, 0,1,Rectangle.RECTANGLE));
                            }
                        }
                    }
                    tabla.addCell(reporte.celda("Importe Total:", font, contenido, derecha, 14,1,Rectangle.NO_BORDER));
                    tabla.addCell(reporte.celda(formatoPorcentaje.format(sum), font, contenido, derecha, 0,1,Rectangle.RECTANGLE));
                    tabla.addCell(reporte.celda("", font, contenido, izquierda, 14,1,Rectangle.NO_BORDER));

                    reporte.agregaObjeto(tabla);
                    reporte.cerrar();
                    reporte.visualizar2(archivoPDF.getAbsolutePath());
                }
            }
        }catch(Exception e)
        {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "No se pudo realizar el reporte si el archivo esta abierto.");
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void t_userActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_t_userActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_t_userActionPerformed

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
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
        JOptionPane.showMessageDialog(this, "¡Ingrese el Usiario!");
    }//GEN-LAST:event_jButton10ActionPerformed

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
                java.awt.Rectangle r = t_datos.getCellRect( x, 3, true);
                t_datos.scrollRectToVisible(r);
            }
            for(; x<t_datos.getRowCount(); x++)
            {
                if(t_datos.getValueAt(x, 3).toString().indexOf(t_busca.getText()) != -1)
                {
                    t_datos.setRowSelectionInterval(x, x);
                    t_datos.setColumnSelectionInterval(3, 3);
                    java.awt.Rectangle r = t_datos.getCellRect( x, 3, true);
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

    private void b_busca1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_busca1ActionPerformed
        // TODO add your handling code here:
        h=new Herramientas(user, 0);
        h.session(sessionPrograma);
        if(this.t_busca.getText().compareToIgnoreCase("")!=0)
        {
            //buscaCuentas();
            if(x>=t_datos.getRowCount())
            {
                x=0;
                java.awt.Rectangle r = t_datos.getCellRect( x, 3, true);
                t_datos.scrollRectToVisible(r);
            }
            for(; x<t_datos.getRowCount(); x++)
            {
                if(t_datos.getValueAt(x, 3).toString().indexOf(t_busca.getText()) != -1)
                {
                    t_datos.setRowSelectionInterval(x, x);
                    t_datos.setColumnSelectionInterval(3, 3);
                    java.awt.Rectangle r = t_datos.getCellRect( x, 3, true);
                    t_datos.scrollRectToVisible(r);
                    break;
                }
            }
            x++;
        }
    }//GEN-LAST:event_b_busca1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JDialog autorizarCosto;
    private javax.swing.JButton b_busca1;
    private javax.swing.JComboBox c_filtro;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane scroll;
    private javax.swing.JTextField t_busca;
    private javax.swing.JPasswordField t_clave;
    private javax.swing.JTable t_datos;
    private javax.swing.JTextField t_user;
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
                celdaEditable[x][y]=false;
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
                            case 22:
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
                                        if(value!=null)
                                        {
                                            if((Integer)value>=0)
                                            {
                                                session.beginTransaction().begin();
                                                Pedido miPedido=(Pedido)session.get(Pedido.class, (Integer)t_datos.getValueAt(row, 17));
                                                if(miPedido!=null)
                                                {
                                                    if(miPedido.getTipoPedido().compareTo("Adicional")==0)
                                                    {
                                                        PartidaExterna parext=(PartidaExterna)session.get(PartidaExterna.class, (Integer)t_datos.getValueAt(row, 0));
                                                        if((Integer)value!=0)
                                                            parext.setFacturaPedido((Integer)value);
                                                        else
                                                            parext.setFacturaPedido(null);
                                                        session.update(parext);
                                                    }
                                                    else
                                                    {
                                                        Partida par=(Partida)session.get(Partida.class, (Integer)t_datos.getValueAt(row, 0));
                                                        if((Integer)value!=0)
                                                            par.setFacturaPedido((Integer)value);
                                                        else
                                                            par.setFacturaPedido(null);
                                                        session.update(par);
                                                    }
                                                    session.beginTransaction().commit();

                                                    if((Integer)value!=0)
                                                        vector.setElementAt(value, col);
                                                    else
                                                        vector.setElementAt(0, col);
                                                    this.dataVector.setElementAt(vector, row);
                                                    fireTableCellUpdated(row, col);
                                                }
                                            }
                                            else
                                                JOptionPane.showMessageDialog(null, "El campo no permite números menores a 1"); 
                                        }
                                        else
                                        {
                                            session.beginTransaction().begin();
                                            Partida par=(Partida)session.get(Partida.class, (Integer)t_datos.getValueAt(row, 0));
                                            par.setFacturaPedido(null);
                                            session.update(par);
                                            session.beginTransaction().commit();
                                            vector.setElementAt(0, col);
                                            this.dataVector.setElementAt(vector, row);
                                            fireTableCellUpdated(row, col);
                                        }
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
                                break;
                                
                            case 23:
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
                                                Partida part=null;
                                                PartidaExterna partEx=null;
                                                if( ((Integer)t_datos.getValueAt(row, 1)) > 0 )
                                                    part=(Partida)session.get(Partida.class, (Integer)t_datos.getValueAt(row, 0));
                                                else
                                                    partEx=(PartidaExterna)session.get(PartidaExterna.class, (Integer)t_datos.getValueAt(row, 0));
                                                try
                                                {
                                                    if(part!=null || partEx!=null)
                                                    {
                                                        if((Boolean)value==true)
                                                        {
                                                            if(part!=null)
                                                            {
                                                                part.setSurtido((Boolean)value);
                                                                session.update(part);
                                                            }
                                                            else
                                                            {
                                                                partEx.setSurtido((Boolean)value);
                                                                session.update(partEx);
                                                            }
                                                            session.getTransaction().commit();
                                                            vector.setElementAt(value, col);
                                                            this.dataVector.setElementAt(vector, row);
                                                            fireTableCellUpdated(row, col);
                                                        }
                                                        else
                                                        {
                                                            if(user.getAutorizarPedidos()==true)
                                                            {
                                                                if(part!=null)
                                                                {
                                                                    part.setSurtido((Boolean)value);
                                                                    session.update(part);
                                                                }
                                                                else
                                                                {
                                                                    partEx.setSurtido((Boolean)value);
                                                                    session.update(partEx);
                                                                }
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
    //not necessary
    }

    public void cargarPedidos()
    {
        if(orden!=null)
        {
            Session session = HibernateUtil.getSessionFactory().openSession();
            try
            {
                session.beginTransaction().begin();
                ord = (Orden)session.get(Orden.class, Integer.parseInt(orden));
                Pedido[] ped;
       
                Query query = session.createQuery("SELECT DISTINCT ped FROM Pedido ped "
                        + "LEFT JOIN FETCH ped.partidas partP "
                        + "LEFT JOIN partP.ordenByIdOrden ord "
                        + "where ord.idOrden="+orden
                        + " OR ped.orden.idOrden="+orden);
                ped = (Pedido[])query.list().toArray(new Pedido[0]);
                
                if(ped.length>0)
                {
                    this.c_filtro.removeAllItems();
                    c_filtro.addItem("Todos");
                    for(int g=0; g<ped.length; g++)
                        c_filtro.addItem(""+ped[g].getIdPedido());
                    c_filtro.setSelectedIndex(0);
                }
                else
                {
                    this.c_filtro.removeAllItems();
                    c_filtro.addItem("Todos");
                }
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
            finally
            {
                if(session.isOpen()==true)
                    session.close();
            }
        }
        else
        {
            this.c_filtro.removeAllItems();
            c_filtro.addItem("Todos");
        }
    }
    
}
