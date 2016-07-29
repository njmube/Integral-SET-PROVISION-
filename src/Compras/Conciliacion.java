/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Compras;

import Hibernate.Util.HibernateUtil;
import Hibernate.entidades.Orden;
import Hibernate.entidades.Partida;
import Hibernate.entidades.PartidaExterna;
import Hibernate.entidades.Usuario;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Element;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPTable;
import java.awt.Color;
import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
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
import org.apache.poi.ss.usermodel.CellStyle;
import org.hibernate.Query;
import org.hibernate.Session;
import Integral.DefaultTableHeaderCellRenderer;
import Integral.ExtensionFileFilter;
import Integral.FormatoEditor;
import Integral.FormatoTabla;
import Integral.Herramientas;
import Integral.HorizontalBarUI;
import Integral.PDF;
import Integral.VerticalBarUI;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import javax.swing.DefaultCellEditor;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.util.CellRangeAddress;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.hibernate.Criteria;


/**
 *
 * @author salvador
 */
public class Conciliacion extends javax.swing.JPanel {

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
        "Id","Tipo","Descripcion","Aut","Val","N° SET","N° Ext.", "C.Aut","N","Ori","D","R","M","Med","Codigo","$C/U Aut",
        "N $C/U","Factura Tot","Prov","N° Fact."};
    //private Session session;
    Orden ord;
    FormatoTabla formato;
    MyModel model;
        Class[] types = new Class [] {
                    java.lang.String.class/*Id*/,  
                    java.lang.String.class/*Tipo*/,  
                    java.lang.String.class/*Descripcion*/,
                    java.lang.Boolean.class/*Aut*/,
                    java.lang.Boolean.class/*Vales*/,
                    java.lang.Integer.class/*N° SET*/, 
                    java.lang.String.class/*N° Ext*/, 
                    java.lang.Double.class/*C. Aut*/, 
                    java.lang.Double.class/*C. N*/,  
                    java.lang.String.class/*Ori*/,
                    java.lang.Double.class/*C. D*/,  
                    java.lang.Double.class/*C. R*/,  
                    java.lang.Double.class/*C. M*/,  
                    java.lang.String.class/*Uni*/,  
                    java.lang.String.class/*Cod*/, 
                    
                    java.lang.Double.class/*C/U Aut*/, 
                    java.lang.Double.class/*N $C/U*/, 
                    java.lang.Double.class/*$Factura Tot*/, 
                    java.lang.Integer.class/*Prov*/, 
                    java.lang.String.class/*N° Fact.*/
                };
    List noPartida;
    
    public Conciliacion(String ord, Usuario us, String ses) {
        initComponents();
        scroll.getVerticalScrollBar().setUI(new VerticalBarUI());
        scroll.getHorizontalScrollBar().setUI(new HorizontalBarUI());
        sessionPrograma=ses;
        orden=ord;
        user=us;
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
        for(int x=0; x<t_datos.getColumnModel().getColumnCount(); x++)
        {
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
        FormatoEditor fe=new FormatoEditor();
        t_datos.setDefaultEditor(Double.class, fe);

        for (int i=0; i<t_datos.getColumnCount(); i++)
        {
  	      TableColumn column = col_model.getColumn(i);
              switch(i)
              {
                  case 0://Id
                      column.setPreferredWidth(40);
                      break;
                  case 1://tipo
                      column.setPreferredWidth(10);
                      break;
                  case 2://descripcion
                      column.setPreferredWidth(310);
                      break;
                  case 3://Aut
                      column.setPreferredWidth(10);
                      break;
                  case 4://Vales
                      column.setPreferredWidth(10);
                      break;
                  case 5://No SET
                      column.setPreferredWidth(50);
                      break;
                  case 6://No ext
                      column.setPreferredWidth(50);
                      break;
                  case 7://Cant Aut
                      column.setPreferredWidth(40);
                      break;
                  case 8://Cant N
                      column.setPreferredWidth(40);
                      break;
                  case 9://origen
                      column.setPreferredWidth(10);
                      DefaultCellEditor editor = new DefaultCellEditor(tipo);
                      column.setCellEditor(editor); 
                      editor.setClickCountToStart(2);
                      break;
                  case 10://Cant D
                      column.setPreferredWidth(40);
                      break;
                  case 11://Cant R
                      column.setPreferredWidth(40);
                      break;
                  case 12://Cant M
                      column.setPreferredWidth(40);
                      break;
                  case 13://Uni
                      column.setPreferredWidth(30);
                      break;
                  case 14://codigo
                      column.setPreferredWidth(80);
                      break;

                  case 15://Aut C/U
                      column.setPreferredWidth(70);
                      break;
                  
                  case 16://Compra C/U 
                      column.setPreferredWidth(70);
                      break;
                      
                  case 17://Factura C/U
                      column.setPreferredWidth(70);
                      break;
                  case 18://Prov
                      column.setPreferredWidth(100);
                      break;
                  case 19://N° Factura
                      column.setPreferredWidth(60);
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
                
                Query query = session.createSQLQuery("select id_partida, partida.tipo, catalogo.nombre, autorizado, facturado, partida.id_pedido as id, pedido.id_externo, Cantidad_aut, cant_pcp, d, r, m, med, id_parte, ori_con, \n" +
                "Precio_aut_c_u, pcp, proveedor.nombre as prov, (select concat('[',tipo_documento,']',documento) from almacen where tipo_movimiento=1 and operacion in(1, 4) and id_pedido=id order by fecha limit 1) as factura  \n" +
                "from partida inner join catalogo on partida.id_catalogo=catalogo.id_catalogo " +
                "left join pedido on pedido.id_pedido=partida.id_pedido left join proveedor on pedido.id_proveedor = proveedor.id_proveedor " +
                "where partida.id_orden="+ord.getIdOrden()+" and (partida.autorizado=true or partida.facturado=true or partida.id_pedido is not null) order by partida.id_evaluacion, partida.sub_partida desc;");
                query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
                ArrayList cuentas=(ArrayList)query.list();

                Query query2 = session.createSQLQuery("select id_partida_externa, 'AC'as tipo, descripcion, false, facturado, partida_externa.id_pedido as id, id_externo, 0.0, cantidad, d, r, m, unidad, noParte, ori_con, \n" +
"0.0, costo, proveedor.nombre as prov, (select concat('[',tipo_documento,']',documento) from almacen where tipo_movimiento=1 and operacion=3 and id_pedido=id order by fecha limit 1) as factura  \n" +
"from partida_externa left join pedido on partida_externa.id_pedido=pedido.id_pedido \n" +
"inner join proveedor on pedido.id_proveedor= proveedor.id_proveedor where pedido.id_orden="+ord.getIdOrden()+" order by partida_externa.id_partida_externa");
                query2.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
                ArrayList cuentas2=(ArrayList)query2.list();
                
                int renglones=cuentas.size()+cuentas2.size();
                model=new MyModel(renglones, columnas, this.types);
                t_datos.setModel(model);
                
                if(cuentas.size()>0)
                {                    
                    noPartida=new ArrayList();
                    for(int i=0; i<cuentas.size(); i++)
                    {
                        java.util.HashMap map=(java.util.HashMap)cuentas.get(i);
                        noPartida.add(map.get("id_partida"));
                        model.setValueAt(map.get("id_partida"), i, 0);
                        model.setValueAt(map.get("tipo"), i, 1);
                        model.setValueAt(map.get("nombre"), i, 2);
                        model.setValueAt(map.get("autorizado"), i, 3);
                        model.setValueAt(map.get("facturado"), i, 4);
                        model.setValueAt(map.get("id"), i, 5);
                        model.setValueAt(map.get("id_externo"), i, 6);
                        model.setValueAt(map.get("Cantidad_aut"), i, 7);//cant Aut
                        if(map.get("cant_pcp")!=null)
                            model.setValueAt(map.get("cant_pcp"), i, 8);//cant Com
                        else
                            model.setValueAt(0.00d, i, 8);//cant Com
                        model.setValueAt(map.get("ori_con"), i, 9);//origen
                        model.setCeldaEditable(i, 9, true);
                        model.setValueAt(map.get("d"), i, 10);//origen
                        model.setCeldaEditable(i, 10, true);
                        model.setValueAt(map.get("r"), i, 11);//origen
                        model.setCeldaEditable(i, 11, true);
                        model.setValueAt(map.get("m"), i, 12);//origen
                        model.setCeldaEditable(i, 12, true);
                        model.setValueAt(map.get("med"), i, 13);
                        model.setValueAt(map.get("id_parte"), i, 14);
                        
                        model.setValueAt(map.get("Precio_aut_c_u"), i, 15);
                        if(map.get("pcp")!=null)
                            model.setValueAt(map.get("pcp"), i, 16);//C/U Com
                        else
                            model.setValueAt(0.00d, i, 16);//C/U Com
                        
                        double suma=0.0d;
                        if(map.get("ori_con").toString().compareToIgnoreCase("-")!=0)
                        {
                            if(map.get("cant_pcp")!=null && map.get("pcp")!=null)
                                suma=Double.parseDouble(map.get("cant_pcp").toString())*(Double.parseDouble(map.get("pcp").toString())/0.9d);
                        }
                        if(Double.parseDouble(map.get("d").toString())>0)
                        {
                            if(map.get("Precio_aut_c_u")!=null)
                               suma+=Double.parseDouble(map.get("d").toString())*(Double.parseDouble(map.get("Precio_aut_c_u").toString())*0.72d);
                        }
                        if(Double.parseDouble(map.get("r").toString())>0)
                        {
                            if(map.get("Precio_aut_c_u")!=null)
                                suma+=Double.parseDouble(map.get("r").toString())*(Double.parseDouble(map.get("Precio_aut_c_u").toString())*0.65d);
                        }
                        if(Double.parseDouble(map.get("m").toString())>0)
                        {
                            if(map.get("Precio_aut_c_u")!=null)
                                suma+=Double.parseDouble(map.get("m").toString())*(Double.parseDouble(map.get("Precio_aut_c_u").toString())*0.65d);
                        }
                        model.setValueAt(suma, i, 17);
                        
                        model.setValueAt(map.get("prov"), i, 18);//proveedor
                        
                        model.setValueAt(map.get("factura"), i, 19);// no de factura
                       
                    }
                    if(user.getGeneraPedidos()==false)
                        t_datos.setEnabled(false);
                    else
                        t_datos.setEnabled(true);
                        
                }
                                
                if(cuentas2.size()>0)
                {
                    for(int i=0; i<cuentas2.size(); i++)
                    {
                        java.util.HashMap map=(java.util.HashMap)cuentas2.get(i);
                        model.setValueAt(map.get("id_partida_externa"), i+cuentas.size(), 0);
                        model.setValueAt(map.get("tipo"), i+cuentas.size(), 1);
                        model.setValueAt(map.get("descripcion"), i+cuentas.size(), 2);
                        model.setValueAt(false, i+cuentas.size(), 3);
                        model.setValueAt(map.get("facturado"), i+cuentas.size(), 4);
                        model.setCeldaEditable(i+cuentas.size(), 4, true);
                        model.setValueAt(map.get("id"), i+cuentas.size(), 5);
                        model.setValueAt(map.get("id_externo"), i+cuentas.size(), 6);
                        model.setValueAt(map.get("cantidad"), i+cuentas.size(), 7);
                        model.setValueAt(map.get("cantidad"), i+cuentas.size(), 8);
                        model.setValueAt(map.get("ori_con"), i+cuentas.size(), 9);//Origen
                        model.setCeldaEditable(i+cuentas.size(), 9, true);
                        model.setValueAt(map.get("d"), i+cuentas.size(), 10);//origen
                        model.setCeldaEditable(i+cuentas.size(),10, true);
                        model.setValueAt(map.get("r"), i+cuentas.size(), 11);//origen
                        model.setCeldaEditable(i+cuentas.size(), 11, true);
                        model.setValueAt(map.get("m"), i+cuentas.size(), 12);//origen
                        model.setCeldaEditable(i+cuentas.size(), 12, true);
                        model.setValueAt(map.get("unidad"), i+cuentas.size(), 13);
                        model.setValueAt(map.get("noParte"), i+cuentas.size(), 14);
                        model.setValueAt(0.0, i+cuentas.size(), 15);
                        model.setValueAt(map.get("costo"), i+cuentas.size(), 16);
                        double suma=0.0d;
                        if(map.get("ori_con").toString().compareToIgnoreCase("-")!=0)
                            suma=Double.parseDouble(map.get("costo").toString())/0.9;
                        model.setValueAt(suma, i+cuentas.size(), 17);
                        model.setValueAt(map.get("prov"), i+cuentas.size(), 18);
                        model.setValueAt(map.get("factura"), i+cuentas.size(), 19);// N° factura
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
        suma();
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        tipo = new javax.swing.JComboBox();
        jPanel1 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        t_busca = new javax.swing.JTextField();
        b_busca1 = new javax.swing.JButton();
        total = new javax.swing.JFormattedTextField();
        scroll = new javax.swing.JScrollPane();
        t_datos = new javax.swing.JTable();

        tipo.setFont(new java.awt.Font("Dialog", 0, 9)); // NOI18N
        tipo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "-", "N" }));

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

        total.setEditable(false);
        total.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.00"))));
        total.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        total.setDisabledTextColor(new java.awt.Color(0, 0, 153));
        jPanel1.add(total, new org.netbeans.lib.awtextra.AbsoluteConstraints(790, 0, 110, -1));

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
        t_datos.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                t_datosKeyPressed(evt);
            }
        });
        scroll.setViewportView(t_datos);

        add(scroll, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        try {
            javax.swing.JFileChooser archivo = new javax.swing.JFileChooser();
            archivo.setFileFilter(new ExtensionFileFilter("Excel document (*.xls)", new String[]{"xls"}));
            String ruta = null;

            if (archivo.showSaveDialog(null) == archivo.APPROVE_OPTION) {
                ruta = archivo.getSelectedFile().getAbsolutePath();
                if (ruta != null) {
                    File archivoXLS = new File(ruta + ".xls");
                    File plantilla = new File("imagenes/plantillaConciliacion.xls");
                    Session session = HibernateUtil.getSessionFactory().openSession();
                    ArrayList datos = new ArrayList();
                    Query query = session.createSQLQuery("select compania.nombre, orden.tipo_nombre, orden.modelo, orden.no_serie, clientes.nombre as nombres,orden.id_orden \n"
                            + "from orden inner join compania on compania.id_compania=orden.id_compania inner join clientes on clientes.id_clientes=orden.id_cliente\n"
                            + "where orden.id_orden="+Integer.parseInt(orden)+"");
                    query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
                    datos = (ArrayList) query.list();

                    //
                    Path FROM = Paths.get("imagenes/plantillaConciliacion.xls");
                    Path TO = Paths.get(ruta + ".xls");
                    //sobreescribir el fichero de destino, si existe, y copiar
                    // los atributos, incluyendo los permisos rwx
                    CopyOption[] options = new CopyOption[]{
                        StandardCopyOption.REPLACE_EXISTING,
                        StandardCopyOption.COPY_ATTRIBUTES
                    };
                    Files.copy(FROM, TO, options);

                    FileInputStream miPlantilla = new FileInputStream(archivoXLS);
                    POIFSFileSystem fsFileSystem = new POIFSFileSystem(miPlantilla);
                    HSSFWorkbook  libro = new HSSFWorkbook(fsFileSystem);
                    libro.getSheet("Conciliacion").getRow(0).getCell(6).setCellValue("CONCILIACIÓN PARA FACTURACIÓN");

                    for (int i = 0; i < datos.size(); i++) {
                        java.util.HashMap map = (java.util.HashMap) datos.get(i);

                        libro.getSheet("Conciliacion").getRow(1).getCell(2).setCellValue(map.get("nombre").toString());
                        libro.getSheet("Conciliacion").getRow(2).getCell(2).setCellValue(map.get("tipo_nombre").toString());
                        libro.getSheet("Conciliacion").getRow(3).getCell(2).setCellValue(map.get("modelo").toString());
                        libro.getSheet("Conciliacion").getRow(4).getCell(2).setCellValue(map.get("no_serie").toString());
                        libro.getSheet("Conciliacion").getRow(5).getCell(2).setCellValue(map.get("nombres").toString());
                        libro.getSheet("Conciliacion").getRow(2).getCell(12).setCellValue(map.get("id_orden").toString());
                    }
                    HSSFCellStyle  borde_d = libro.createCellStyle();
                    borde_d.setBorderBottom(CellStyle.BORDER_THIN);
                    borde_d.setBorderTop(CellStyle.BORDER_THIN);
                    borde_d.setBorderRight(CellStyle.BORDER_THIN);
                    borde_d.setBorderLeft(CellStyle.BORDER_THIN);
                    borde_d.setAlignment(CellStyle.ALIGN_RIGHT);

                    HSSFCellStyle borde_i = libro.createCellStyle();
                    borde_i.setBorderBottom(CellStyle.BORDER_THIN);
                    borde_i.setBorderTop(CellStyle.BORDER_THIN);
                    borde_i.setBorderRight(CellStyle.BORDER_THIN);
                    borde_i.setBorderLeft(CellStyle.BORDER_THIN);
                    borde_i.setAlignment(CellStyle.ALIGN_LEFT);

                    HSSFCellStyle borde_c = libro.createCellStyle();
                    borde_c.setBorderBottom(CellStyle.BORDER_THIN);
                    borde_c.setBorderTop(CellStyle.BORDER_THIN);
                    borde_c.setBorderRight(CellStyle.BORDER_THIN);
                    borde_c.setBorderLeft(CellStyle.BORDER_THIN);
                    borde_c.setAlignment(CellStyle.ALIGN_CENTER);
                    
                    
                    HSSFCellStyle  borde_dr = libro.createCellStyle();
                    borde_dr.setBorderBottom(CellStyle.BORDER_THIN);
                    borde_dr.setBorderTop(CellStyle.BORDER_THIN);
                    borde_dr.setBorderRight(CellStyle.BORDER_THIN);
                    borde_dr.setBorderLeft(CellStyle.BORDER_THIN);
                    borde_dr.setAlignment(CellStyle.ALIGN_RIGHT);
                    borde_dr.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                    borde_dr.setFillBackgroundColor(HSSFColor.LIGHT_BLUE.index);
                    borde_dr.setFillForegroundColor(HSSFColor.YELLOW.index);

                    HSSFCellStyle borde_ir = libro.createCellStyle();
                    borde_ir.setBorderBottom(CellStyle.BORDER_THIN);
                    borde_ir.setBorderTop(CellStyle.BORDER_THIN);
                    borde_ir.setBorderRight(CellStyle.BORDER_THIN);
                    borde_ir.setBorderLeft(CellStyle.BORDER_THIN);
                    borde_ir.setAlignment(CellStyle.ALIGN_LEFT);
                    borde_ir.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                    borde_ir.setFillBackgroundColor(HSSFColor.LIGHT_BLUE.index);
                    borde_ir.setFillForegroundColor(HSSFColor.YELLOW.index);
                    
                    HSSFCellStyle borde_cr = libro.createCellStyle();
                    borde_cr.setBorderBottom(CellStyle.BORDER_THIN);
                    borde_cr.setBorderTop(CellStyle.BORDER_THIN);
                    borde_cr.setBorderRight(CellStyle.BORDER_THIN);
                    borde_cr.setBorderLeft(CellStyle.BORDER_THIN);
                    borde_cr.setAlignment(CellStyle.ALIGN_CENTER);
                    borde_cr.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                    borde_cr.setFillBackgroundColor(HSSFColor.LIGHT_BLUE.index);
                    borde_cr.setFillForegroundColor(HSSFColor.YELLOW.index);
                    
                    DecimalFormat formatoDecimal = new DecimalFormat("####0.0");
                    DecimalFormat formatoPorcentaje = new DecimalFormat("#,##0.00");

                    int miRenglon=9;
                    for (int i = 0; i < t_datos.getRowCount(); i++) 
                    {
                        for(int j=0; j<4; j++)
                        {
                            int renglon=0;
                            switch(j)
                            {
                                case 0:
                                    renglon=8;
                                    break;
                                case 1:
                                    renglon=10;
                                    break;
                                case 2:
                                    renglon=11;
                                    break;
                                case 3:
                                    renglon=12;
                                    break;
                            }
                            if( (Double.parseDouble(t_datos.getValueAt(i,renglon).toString())>0 && t_datos.getValueAt(i,9).toString().compareTo("N")==0) || 
                                    ((Double.parseDouble(t_datos.getValueAt(i,renglon).toString())>0 && renglon>=10)) ||
                                    (renglon==8 && Double.parseDouble(t_datos.getValueAt(i,10).toString())<=0 && Double.parseDouble(t_datos.getValueAt(i,11).toString())<=0 &&  Double.parseDouble(t_datos.getValueAt(i,12).toString())<=0) )
                            {
                                if((boolean)t_datos.getValueAt(i,3)==true || (boolean)t_datos.getValueAt(i, 4)==true)
                                {
                                    libro.getSheet("Conciliacion").createRow(miRenglon);
                                    //columna0
                                    if(t_datos.getValueAt(i,5)==null){
                                        libro.getSheet("Conciliacion").getRow(miRenglon).createCell(0).setCellValue("");
                                    }else{
                                        libro.getSheet("Conciliacion").getRow(miRenglon).createCell(0).setCellValue(t_datos.getValueAt(i,5).toString());
                                    }
                                    
                                    //columna1
                                    if(t_datos.getValueAt(i,6)==null){
                                        libro.getSheet("Conciliacion").getRow(miRenglon).createCell(1).setCellValue("");
                                    }else{
                                        libro.getSheet("Conciliacion").getRow(miRenglon).createCell(1).setCellValue(t_datos.getValueAt(i,6).toString());
                                    }
                                    
                                    //columna2
                                    libro.getSheet("Conciliacion").getRow(miRenglon).createCell(2).setCellValue(t_datos.getValueAt(i,renglon).toString());
                                    
                                    //columna3
                                    if(t_datos.getValueAt(i,14)==null){
                                        libro.getSheet("Conciliacion").getRow(miRenglon).createCell(3).setCellValue("");
                                    }else{
                                        libro.getSheet("Conciliacion").getRow(miRenglon).createCell(3).setCellValue(t_datos.getValueAt(i,14).toString());
                                    }
                                    
                                    //columna4
                                    libro.getSheet("Conciliacion").getRow(miRenglon).createCell(4).setCellValue(t_datos.getValueAt(i,2).toString());
                                    
                                    //columna5
                                    if(renglon==8 && t_datos.getValueAt(i,9).toString().compareTo("-")==0)
                                        libro.getSheet("Conciliacion").getRow(miRenglon).createCell(5).setCellValue("");
                                    else
                                    {
                                        switch(renglon)
                                        {
                                            case 8:
                                                libro.getSheet("Conciliacion").getRow(miRenglon).createCell(5).setCellValue("N");
                                                break;
                                            case 10:
                                                libro.getSheet("Conciliacion").getRow(miRenglon).createCell(5).setCellValue("D");
                                                break;
                                            case 11:
                                                libro.getSheet("Conciliacion").getRow(miRenglon).createCell(5).setCellValue("R");
                                                break;
                                            case 12:
                                                libro.getSheet("Conciliacion").getRow(miRenglon).createCell(5).setCellValue("M");
                                                break;
                                        }
                                    }
                                    
                                    //columna6
                                    libro.getSheet("Conciliacion").getRow(miRenglon).createCell(6).setCellValue(formatoPorcentaje.format(t_datos.getValueAt(i,15)));
                                    
                                    
                                    //columna7 $tot aut.
                                    double n;
                                    n= BigDecimal.valueOf(Double.parseDouble(t_datos.getValueAt(i,renglon).toString())*Double.parseDouble(t_datos.getValueAt(i,15).toString())).setScale(2, RoundingMode.UP).doubleValue();
                                    libro.getSheet("Conciliacion").getRow(miRenglon).createCell(7).setCellValue(formatoPorcentaje.format(n));
                                    
                                    //columna8
                                    libro.getSheet("Conciliacion").getRow(miRenglon).createCell(8).setCellValue(formatoPorcentaje.format(t_datos.getValueAt(i,16)));
                                    
                                    //columna9 $tot com
                                    n= BigDecimal.valueOf(Double.parseDouble(t_datos.getValueAt(i,renglon).toString())*Double.parseDouble(t_datos.getValueAt(i,16).toString())).setScale(2, RoundingMode.UP).doubleValue();
                                    libro.getSheet("Conciliacion").getRow(miRenglon).createCell(9).setCellValue(formatoPorcentaje.format(n));
                                    
                                    //columna10 11
                                    if(renglon==8 && t_datos.getValueAt(i,9).toString().compareTo("-")==0)
                                    {
                                        libro.getSheet("Conciliacion").getRow(miRenglon).createCell(10).setCellValue("");
                                        libro.getSheet("Conciliacion").getRow(miRenglon).createCell(11).setCellValue("");
                                    }
                                    else
                                    {
                                        switch(renglon)
                                        {
                                            case 8:
                                                n= BigDecimal.valueOf(Double.parseDouble(t_datos.getValueAt(i, 16).toString())/0.9d).setScale(2, RoundingMode.UP).doubleValue();
                                                libro.getSheet("Conciliacion").getRow(miRenglon).createCell(11).setCellValue(formatoPorcentaje.format(n*Double.parseDouble(t_datos.getValueAt(i,renglon).toString())));
                                                libro.getSheet("Conciliacion").getRow(miRenglon).createCell(10).setCellValue(formatoPorcentaje.format(n));
                                                break;
                                            case 10:
                                                n= BigDecimal.valueOf(Double.parseDouble(t_datos.getValueAt(i, 15).toString())*0.72d).setScale(2, RoundingMode.UP).doubleValue();
                                                libro.getSheet("Conciliacion").getRow(miRenglon).createCell(11).setCellValue(formatoPorcentaje.format(n*Double.parseDouble(t_datos.getValueAt(i,renglon).toString())));
                                                libro.getSheet("Conciliacion").getRow(miRenglon).createCell(10).setCellValue(formatoPorcentaje.format(n));
                                                break;
                                            case 11:
                                                n= BigDecimal.valueOf(Double.parseDouble(t_datos.getValueAt(i, 15).toString())*0.65d).setScale(2, RoundingMode.UP).doubleValue();
                                                libro.getSheet("Conciliacion").getRow(miRenglon).createCell(11).setCellValue(formatoPorcentaje.format(n*Double.parseDouble(t_datos.getValueAt(i,renglon).toString())));
                                                libro.getSheet("Conciliacion").getRow(miRenglon).createCell(10).setCellValue(formatoPorcentaje.format(n));
                                                break;
                                            case 12:
                                                n= BigDecimal.valueOf(Double.parseDouble(t_datos.getValueAt(i, 15).toString())*0.65d).setScale(2, RoundingMode.UP).doubleValue();
                                                libro.getSheet("Conciliacion").getRow(miRenglon).createCell(11).setCellValue(formatoPorcentaje.format(n*Double.parseDouble(t_datos.getValueAt(i,renglon).toString())));
                                                libro.getSheet("Conciliacion").getRow(miRenglon).createCell(10).setCellValue(formatoPorcentaje.format(n));
                                                break;
                                        }
                                    }
                                    
                                    //columna12
                                    if(t_datos.getValueAt(i,18)==null){
                                        libro.getSheet("Conciliacion").getRow(miRenglon).createCell(12).setCellValue("");
                                    }else{
                                        libro.getSheet("Conciliacion").getRow(miRenglon).createCell(12).setCellValue(t_datos.getValueAt(i,18).toString());
                                    }
                                    
                                    //columna13
                                    if(t_datos.getValueAt(i,19)==null){
                                        libro.getSheet("Conciliacion").getRow(miRenglon).createCell(13).setCellValue("");
                                    }else{
                                        libro.getSheet("Conciliacion").getRow(miRenglon).createCell(13).setCellValue(t_datos.getValueAt(i,19).toString());
                                    }
                                    
                                    //columna14
                                    libro.getSheet("Conciliacion").getRow(miRenglon).createCell(14).setCellValue("V");
                                    
                                    if(renglon==8 && t_datos.getValueAt(i,9).toString().compareTo("-")==0)
                                    {
                                        libro.getSheet("Conciliacion").getRow(miRenglon).getCell(0).setCellStyle(borde_d);
                                        libro.getSheet("Conciliacion").getRow(miRenglon).getCell(1).setCellStyle(borde_d);
                                        libro.getSheet("Conciliacion").getRow(miRenglon).getCell(2).setCellStyle(borde_d);
                                        libro.getSheet("Conciliacion").getRow(miRenglon).getCell(3).setCellStyle(borde_d);
                                        libro.getSheet("Conciliacion").getRow(miRenglon).getCell(4).setCellStyle(borde_i);
                                        libro.getSheet("Conciliacion").getRow(miRenglon).getCell(5).setCellStyle(borde_c);
                                        libro.getSheet("Conciliacion").getRow(miRenglon).getCell(6).setCellStyle(borde_d);
                                        libro.getSheet("Conciliacion").getRow(miRenglon).getCell(7).setCellStyle(borde_d);
                                        libro.getSheet("Conciliacion").getRow(miRenglon).getCell(8).setCellStyle(borde_d);
                                        libro.getSheet("Conciliacion").getRow(miRenglon).getCell(9).setCellStyle(borde_d);
                                        libro.getSheet("Conciliacion").getRow(miRenglon).getCell(10).setCellStyle(borde_d);
                                        libro.getSheet("Conciliacion").getRow(miRenglon).getCell(11).setCellStyle(borde_d);
                                        libro.getSheet("Conciliacion").getRow(miRenglon).getCell(12).setCellStyle(borde_i);
                                        libro.getSheet("Conciliacion").getRow(miRenglon).getCell(13).setCellStyle(borde_i);
                                        libro.getSheet("Conciliacion").getRow(miRenglon).getCell(14).setCellStyle(borde_d);
                                    }
                                    else
                                    {
                                        libro.getSheet("Conciliacion").getRow(miRenglon).getCell(0).setCellStyle(borde_dr);
                                        libro.getSheet("Conciliacion").getRow(miRenglon).getCell(1).setCellStyle(borde_dr);
                                        libro.getSheet("Conciliacion").getRow(miRenglon).getCell(2).setCellStyle(borde_dr);
                                        libro.getSheet("Conciliacion").getRow(miRenglon).getCell(3).setCellStyle(borde_dr);
                                        libro.getSheet("Conciliacion").getRow(miRenglon).getCell(4).setCellStyle(borde_ir);
                                        libro.getSheet("Conciliacion").getRow(miRenglon).getCell(5).setCellStyle(borde_cr);
                                        libro.getSheet("Conciliacion").getRow(miRenglon).getCell(6).setCellStyle(borde_dr);
                                        libro.getSheet("Conciliacion").getRow(miRenglon).getCell(7).setCellStyle(borde_dr);
                                        libro.getSheet("Conciliacion").getRow(miRenglon).getCell(8).setCellStyle(borde_dr);
                                        libro.getSheet("Conciliacion").getRow(miRenglon).getCell(9).setCellStyle(borde_dr);
                                        libro.getSheet("Conciliacion").getRow(miRenglon).getCell(10).setCellStyle(borde_dr);
                                        libro.getSheet("Conciliacion").getRow(miRenglon).getCell(11).setCellStyle(borde_dr);
                                        libro.getSheet("Conciliacion").getRow(miRenglon).getCell(12).setCellStyle(borde_ir);
                                        libro.getSheet("Conciliacion").getRow(miRenglon).getCell(13).setCellStyle(borde_ir);
                                        libro.getSheet("Conciliacion").getRow(miRenglon).getCell(14).setCellStyle(borde_dr);
                                    } 
                                    miRenglon++;
                                }
                            }
                        }
                    }
                    //font1.setColor(BaseColor.WHITE);
                    libro.getSheet("Conciliacion").createRow(miRenglon);
                    libro.getSheet("Conciliacion").addMergedRegion(new CellRangeAddress(miRenglon, miRenglon, 0, 14));
                    libro.getSheet("Conciliacion").getRow(miRenglon).createCell(0).setCellValue("Faltante en Vales");
                    libro.getSheet("Conciliacion").getRow(miRenglon).getCell(0).setCellStyle(borde_c);
                    miRenglon++;
                    
                    for (int i = 0; i < t_datos.getRowCount(); i++) 
                    {  
                        for(int j=0; j<4; j++)
                        {
                            int renglon=0;
                            switch(j)
                            {
                                case 0:
                                    renglon=8;
                                    break;
                                case 1:
                                    renglon=10;
                                    break;
                                case 2:
                                    renglon=11;
                                    break;
                                case 3:
                                    renglon=12;
                                    break;
                            }
                            if( (Double.parseDouble(t_datos.getValueAt(i,renglon).toString())>0 && t_datos.getValueAt(i,9).toString().compareTo("N")==0) || 
                                    ((Double.parseDouble(t_datos.getValueAt(i,renglon).toString())>0 && renglon>=10)) ||
                                    (renglon==8 && Double.parseDouble(t_datos.getValueAt(i,10).toString())<=0 && Double.parseDouble(t_datos.getValueAt(i,11).toString())<=0 &&  Double.parseDouble(t_datos.getValueAt(i,12).toString())<=0) )
                            {
                                if((boolean)t_datos.getValueAt(i,3)==false && (boolean)t_datos.getValueAt(i, 4)==false && t_datos.getValueAt(i,5)!=null)
                                {
                                    libro.getSheet("Conciliacion").createRow(miRenglon);
                                    //columna0
                                    if(t_datos.getValueAt(i,5)==null){
                                        libro.getSheet("Conciliacion").getRow(miRenglon).createCell(0).setCellValue("");
                                    }else{
                                        libro.getSheet("Conciliacion").getRow(miRenglon).createCell(0).setCellValue(t_datos.getValueAt(i,5).toString());
                                    }
                                    
                                    //columna1
                                    if(t_datos.getValueAt(i,6)==null){
                                        libro.getSheet("Conciliacion").getRow(miRenglon).createCell(1).setCellValue("");
                                    }else{
                                        libro.getSheet("Conciliacion").getRow(miRenglon).createCell(1).setCellValue(t_datos.getValueAt(i,6).toString());
                                    }
                                    
                                    //columna2
                                    libro.getSheet("Conciliacion").getRow(miRenglon).createCell(2).setCellValue(t_datos.getValueAt(i,renglon).toString());
                                    
                                    //columna3
                                    if(t_datos.getValueAt(i,14)==null){
                                        libro.getSheet("Conciliacion").getRow(miRenglon).createCell(3).setCellValue("");
                                    }else{
                                        libro.getSheet("Conciliacion").getRow(miRenglon).createCell(3).setCellValue(t_datos.getValueAt(i,14).toString());
                                    }
                                    
                                    //columna4
                                    libro.getSheet("Conciliacion").getRow(miRenglon).createCell(4).setCellValue(t_datos.getValueAt(i,2).toString());
                                    
                                    //columna5
                                    if(renglon==8 && t_datos.getValueAt(i,9).toString().compareTo("-")==0)
                                        libro.getSheet("Conciliacion").getRow(miRenglon).createCell(5).setCellValue("");
                                    else
                                    {
                                        switch(renglon)
                                        {
                                            case 8:
                                                libro.getSheet("Conciliacion").getRow(miRenglon).createCell(5).setCellValue("N");
                                                break;
                                            case 10:
                                                libro.getSheet("Conciliacion").getRow(miRenglon).createCell(5).setCellValue("D");
                                                break;
                                            case 11:
                                                libro.getSheet("Conciliacion").getRow(miRenglon).createCell(5).setCellValue("R");
                                                break;
                                            case 12:
                                                libro.getSheet("Conciliacion").getRow(miRenglon).createCell(5).setCellValue("M");
                                                break;
                                        }
                                    }
                                    //columna6
                                    libro.getSheet("Conciliacion").getRow(miRenglon).createCell(6).setCellValue(formatoPorcentaje.format(t_datos.getValueAt(i,15)));
                                    
                                    //columna7 $tot aut.
                                    double n;
                                    n= BigDecimal.valueOf(Double.parseDouble(t_datos.getValueAt(i,renglon).toString())*Double.parseDouble(t_datos.getValueAt(i,15).toString())).setScale(2, RoundingMode.UP).doubleValue();
                                    libro.getSheet("Conciliacion").getRow(miRenglon).createCell(7).setCellValue(formatoPorcentaje.format(n));
                                    
                                    //columna8
                                    libro.getSheet("Conciliacion").getRow(miRenglon).createCell(8).setCellValue(formatoPorcentaje.format(t_datos.getValueAt(i,16)));
                                    
                                    //columna9 $tot com
                                    n= BigDecimal.valueOf(Double.parseDouble(t_datos.getValueAt(i,renglon).toString())*Double.parseDouble(t_datos.getValueAt(i,16).toString())).setScale(2, RoundingMode.UP).doubleValue();
                                    libro.getSheet("Conciliacion").getRow(miRenglon).createCell(9).setCellValue(formatoPorcentaje.format(n));
                                    
                                    //columna10 11
                                    if(renglon==8 && t_datos.getValueAt(i,9).toString().compareTo("-")==0)
                                    {
                                        libro.getSheet("Conciliacion").getRow(miRenglon).createCell(10).setCellValue("");
                                        libro.getSheet("Conciliacion").getRow(miRenglon).createCell(11).setCellValue("");
                                    }
                                    else
                                    {
                                        switch(renglon)
                                        {
                                            case 8:
                                                n= BigDecimal.valueOf(Double.parseDouble(t_datos.getValueAt(i, 16).toString())/0.9d).setScale(2, RoundingMode.UP).doubleValue();
                                                libro.getSheet("Conciliacion").getRow(miRenglon).createCell(10).setCellValue(formatoPorcentaje.format(n*Double.parseDouble(t_datos.getValueAt(i,renglon).toString())));
                                                libro.getSheet("Conciliacion").getRow(miRenglon).createCell(11).setCellValue(formatoPorcentaje.format(n));
                                                break;
                                            case 10:
                                                n= BigDecimal.valueOf(Double.parseDouble(t_datos.getValueAt(i, 15).toString())*0.72d).setScale(2, RoundingMode.UP).doubleValue();
                                                libro.getSheet("Conciliacion").getRow(miRenglon).createCell(10).setCellValue(formatoPorcentaje.format(n*Double.parseDouble(t_datos.getValueAt(i,renglon).toString())));
                                                libro.getSheet("Conciliacion").getRow(miRenglon).createCell(11).setCellValue(formatoPorcentaje.format(n));
                                                break;
                                            case 11:
                                                n= BigDecimal.valueOf(Double.parseDouble(t_datos.getValueAt(i, 15).toString())*0.65d).setScale(2, RoundingMode.UP).doubleValue();
                                                libro.getSheet("Conciliacion").getRow(miRenglon).createCell(10).setCellValue(formatoPorcentaje.format(n*Double.parseDouble(t_datos.getValueAt(i,renglon).toString())));
                                                libro.getSheet("Conciliacion").getRow(miRenglon).createCell(11).setCellValue(formatoPorcentaje.format(n));
                                                break;
                                            case 12:
                                                n= BigDecimal.valueOf(Double.parseDouble(t_datos.getValueAt(i, 15).toString())*0.65d).setScale(2, RoundingMode.UP).doubleValue();
                                                libro.getSheet("Conciliacion").getRow(miRenglon).createCell(10).setCellValue(formatoPorcentaje.format(n*Double.parseDouble(t_datos.getValueAt(i,renglon).toString())));
                                                libro.getSheet("Conciliacion").getRow(miRenglon).createCell(11).setCellValue(formatoPorcentaje.format(n));
                                                break;
                                        }
                                    }
                                    
                                    //columna12
                                    if(t_datos.getValueAt(i,18)==null){
                                        libro.getSheet("Conciliacion").getRow(miRenglon).createCell(12).setCellValue("");
                                    }else{
                                        libro.getSheet("Conciliacion").getRow(miRenglon).createCell(12).setCellValue(t_datos.getValueAt(i,18).toString());
                                    }
                                    
                                    //columna13
                                    if(t_datos.getValueAt(i,19)==null){
                                        libro.getSheet("Conciliacion").getRow(miRenglon).createCell(13).setCellValue("");
                                    }else{
                                        libro.getSheet("Conciliacion").getRow(miRenglon).createCell(13).setCellValue(t_datos.getValueAt(i,19).toString());
                                    }
                                    //columna14
                                    libro.getSheet("Conciliacion").getRow(miRenglon).createCell(14).setCellValue("");
                                    if(renglon==8 && t_datos.getValueAt(i,9).toString().compareTo("-")==0)
                                    {
                                        libro.getSheet("Conciliacion").getRow(miRenglon).getCell(0).setCellStyle(borde_d);
                                        libro.getSheet("Conciliacion").getRow(miRenglon).getCell(1).setCellStyle(borde_d);
                                        libro.getSheet("Conciliacion").getRow(miRenglon).getCell(2).setCellStyle(borde_d);
                                        libro.getSheet("Conciliacion").getRow(miRenglon).getCell(3).setCellStyle(borde_d);
                                        libro.getSheet("Conciliacion").getRow(miRenglon).getCell(4).setCellStyle(borde_i);
                                        libro.getSheet("Conciliacion").getRow(miRenglon).getCell(5).setCellStyle(borde_c);
                                        libro.getSheet("Conciliacion").getRow(miRenglon).getCell(6).setCellStyle(borde_d);
                                        libro.getSheet("Conciliacion").getRow(miRenglon).getCell(7).setCellStyle(borde_d);
                                        libro.getSheet("Conciliacion").getRow(miRenglon).getCell(8).setCellStyle(borde_d);
                                        libro.getSheet("Conciliacion").getRow(miRenglon).getCell(9).setCellStyle(borde_d);
                                        libro.getSheet("Conciliacion").getRow(miRenglon).getCell(10).setCellStyle(borde_d);
                                        libro.getSheet("Conciliacion").getRow(miRenglon).getCell(11).setCellStyle(borde_d);
                                        libro.getSheet("Conciliacion").getRow(miRenglon).getCell(12).setCellStyle(borde_i);
                                        libro.getSheet("Conciliacion").getRow(miRenglon).getCell(13).setCellStyle(borde_i);
                                        libro.getSheet("Conciliacion").getRow(miRenglon).getCell(14).setCellStyle(borde_d);
                                    }
                                    else
                                    {
                                        libro.getSheet("Conciliacion").getRow(miRenglon).getCell(0).setCellStyle(borde_dr);
                                        libro.getSheet("Conciliacion").getRow(miRenglon).getCell(1).setCellStyle(borde_dr);
                                        libro.getSheet("Conciliacion").getRow(miRenglon).getCell(2).setCellStyle(borde_dr);
                                        libro.getSheet("Conciliacion").getRow(miRenglon).getCell(3).setCellStyle(borde_dr);
                                        libro.getSheet("Conciliacion").getRow(miRenglon).getCell(4).setCellStyle(borde_ir);
                                        libro.getSheet("Conciliacion").getRow(miRenglon).getCell(5).setCellStyle(borde_cr);
                                        libro.getSheet("Conciliacion").getRow(miRenglon).getCell(6).setCellStyle(borde_dr);
                                        libro.getSheet("Conciliacion").getRow(miRenglon).getCell(7).setCellStyle(borde_dr);
                                        libro.getSheet("Conciliacion").getRow(miRenglon).getCell(8).setCellStyle(borde_dr);
                                        libro.getSheet("Conciliacion").getRow(miRenglon).getCell(9).setCellStyle(borde_dr);
                                        libro.getSheet("Conciliacion").getRow(miRenglon).getCell(10).setCellStyle(borde_dr);
                                        libro.getSheet("Conciliacion").getRow(miRenglon).getCell(11).setCellStyle(borde_dr);
                                        libro.getSheet("Conciliacion").getRow(miRenglon).getCell(12).setCellStyle(borde_ir);
                                        libro.getSheet("Conciliacion").getRow(miRenglon).getCell(13).setCellStyle(borde_ir);
                                        libro.getSheet("Conciliacion").getRow(miRenglon).getCell(14).setCellStyle(borde_dr);
                                    }   
                                    miRenglon++;
                                }
                            }
                        }
                    }      
                    FileOutputStream archivo1 = new FileOutputStream(archivoXLS);
                    libro.write(archivo1);
                    archivo1.close();
                    Desktop.getDesktop().open(archivoXLS);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        try {
            javax.swing.JFileChooser archivo = new javax.swing.JFileChooser();
            archivo.setFileFilter(new ExtensionFileFilter("pdf document (*.pdf)", new String[]{"PDF"}));
            String ruta = null;

            if (archivo.showSaveDialog(null) == archivo.APPROVE_OPTION) {
                ruta = archivo.getSelectedFile().getAbsolutePath();
                if (ruta != null) {
                    Session session = HibernateUtil.getSessionFactory().openSession();
                    ArrayList datos = new ArrayList();
                    Query query = session.createSQLQuery("select compania.nombre, orden.tipo_nombre, orden.modelo, orden.no_serie, clientes.nombre as nombres,orden.id_orden \n"
                            + "from orden inner join compania on compania.id_compania=orden.id_compania inner join clientes on clientes.id_clientes=orden.id_cliente\n"
                            + "where orden.id_orden="+Integer.parseInt(orden)+"");
                    query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
                    datos = (ArrayList) query.list();

                    //
                    PDF reporte = new PDF();
                    reporte.Abrir2(PageSize.LEGAL.rotate(), "CONCILIACIÓN", ruta + ".pdf");
                    BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.WINANSI, BaseFont.NOT_EMBEDDED);
                    float[] nuevos = new float[]{40, 40, 25, 40, 145, 20, 50, 50, 50, 50, 50, 50, 100, 50, 20};
                    com.itextpdf.text.Font font = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 7, com.itextpdf.text.Font.NORMAL);
                    com.itextpdf.text.Font font1 = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 7, com.itextpdf.text.Font.NORMAL);
                    BaseColor contenido = BaseColor.WHITE;
                    BaseColor contenido1 = BaseColor.DARK_GRAY;
                    int centro = com.itextpdf.text.Element.ALIGN_CENTER;
                    int izquierda = com.itextpdf.text.Element.ALIGN_LEFT;
                    int derecha = com.itextpdf.text.Element.ALIGN_RIGHT;

                    reporte.inicioTexto();
                    reporte.agregaObjeto(reporte.crearImagen("imagenes/empresa300115.jpg", 730, -90, 45));
                    reporte.contenido.setFontAndSize(bf, 20);
                    reporte.contenido.setColorFill(BaseColor.BLACK);
                    reporte.contenido.showTextAligned(PdfContentByte.ALIGN_CENTER, "CONCILIACIÓN PARA FACTURACIÓN", 395, 577, 0);

                    reporte.contenido.setFontAndSize(bf, 12);
                    reporte.contenido.setColorFill(BaseColor.BLACK);
                    reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "COMPAÑIA:", 38, 540, 0);
                    reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "TIPO DE UNIDAD:", 38, 520, 0);
                    reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "MODELO:", 38, 500, 0);
                    reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "SERIE VIN:", 38, 480, 0);
                    reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "CLIENTE:", 38, 460, 0);

                    reporte.contenido.roundRectangle(170, 540, 410, 0, 0);
                    reporte.contenido.roundRectangle(170, 520, 410, 0, 0);
                    reporte.contenido.roundRectangle(170, 500, 410, 0, 0);
                    reporte.contenido.roundRectangle(170, 480, 410, 0, 0);
                    reporte.contenido.roundRectangle(170, 460, 410, 0, 0);

                    reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "ORDEN DE TRABAJO SET.", 605, 540, 0);
                    reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "ORDEN DE SURTIDO", 620, 480, 0);
                    
                    for (int i = 0; i < datos.size(); i++) {
                        java.util.HashMap map = (java.util.HashMap) datos.get(i);

                        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, map.get("nombre").toString(), 171, 540, 0);
                        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, map.get("tipo_nombre").toString(), 171, 520, 0);
                        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, map.get("modelo").toString(), 171, 500, 0);
                        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, map.get("no_serie").toString(), 171, 480, 0);
                        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, map.get("nombres").toString(), 171, 460, 0);
                        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, map.get("id_orden").toString(), 657, 520, 0);
                        //reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, " ", 645, 460, 0);

                    }
                    reporte.contenido.roundRectangle(640, 520, 80, 0, 0);
                    reporte.contenido.roundRectangle(640, 460, 80, 0, 0);

                    reporte.finTexto();

                    reporte.agregaObjeto(new Paragraph(" "));
                    reporte.agregaObjeto(new Paragraph(" "));
                    reporte.agregaObjeto(new Paragraph(" "));

                    reporte.agregaObjeto(new Paragraph(" "));
                    reporte.agregaObjeto(new Paragraph(" "));

                    reporte.agregaObjeto(new Paragraph(" "));
                    reporte.agregaObjeto(new Paragraph(" "));

                    PdfPTable tabla = reporte.crearTabla(nuevos.length, nuevos, 100, Element.ALIGN_CENTER);
                    cabecera(reporte, bf, tabla, "", 2);
                    //
                    DecimalFormat formatoDecimal = new DecimalFormat("####0.0");
                    DecimalFormat formatoPorcentaje = new DecimalFormat("#,##0.00");

                    for (int i = 0; i < t_datos.getRowCount(); i++) 
                    {
                        for(int j=0; j<4; j++)
                        {
                            int renglon=0;
                            switch(j)
                            {
                                case 0:
                                    renglon=8;
                                    break;
                                case 1:
                                    renglon=10;
                                    break;
                                case 2:
                                    renglon=11;
                                    break;
                                case 3:
                                    renglon=12;
                                    break;
                            }
                            if( (Double.parseDouble(t_datos.getValueAt(i,renglon).toString())>0 && t_datos.getValueAt(i,9).toString().compareTo("N")==0) || 
                                    ((Double.parseDouble(t_datos.getValueAt(i,renglon).toString())>0 && renglon>=10)) ||
                                    (renglon==8 && Double.parseDouble(t_datos.getValueAt(i,10).toString())<=0 && Double.parseDouble(t_datos.getValueAt(i,11).toString())<=0 &&  Double.parseDouble(t_datos.getValueAt(i,12).toString())<=0) )
                            {
                                if(renglon==8 && t_datos.getValueAt(i,9).toString().compareTo("-")==0)
                                    contenido = BaseColor.WHITE;
                                else
                                    contenido = BaseColor.LIGHT_GRAY;

                                if((boolean)t_datos.getValueAt(i,3)==true || (boolean)t_datos.getValueAt(i, 4)==true)
                                {
                                    //columna0
                                    if(t_datos.getValueAt(i,5)==null){
                                        tabla.addCell(reporte.celda("",font,contenido,centro,0,1, Rectangle.RECTANGLE));
                                    }else{
                                        tabla.addCell(reporte.celda(t_datos.getValueAt(i,5).toString(),font,contenido,centro,0,1, Rectangle.RECTANGLE));
                                    }
                                    //columna1
                                    if(t_datos.getValueAt(i,6)==null){
                                        tabla.addCell(reporte.celda("",font,contenido,centro,0,1, Rectangle.RECTANGLE));
                                    }else{
                                        tabla.addCell(reporte.celda(t_datos.getValueAt(i,6).toString(),font,contenido,centro,0,1, Rectangle.RECTANGLE));
                                    }
                                    //columna2
                                    tabla.addCell(reporte.celda(t_datos.getValueAt(i,renglon).toString(),font,contenido,centro,0,1, Rectangle.RECTANGLE));
                                  
                                    //columna3
                                    if(t_datos.getValueAt(i,14)==null){
                                        tabla.addCell(reporte.celda("",font,contenido,centro,0,1, Rectangle.RECTANGLE));
                                    }else{
                                        tabla.addCell(reporte.celda(t_datos.getValueAt(i,14).toString(),font,contenido,centro,0,1, Rectangle.RECTANGLE));
                                    }
                                    //columna4
                                    tabla.addCell(reporte.celda(t_datos.getValueAt(i,2).toString(),font,contenido,izquierda,0,1, Rectangle.RECTANGLE));
                                    //columna5
                                    if(renglon==8 && t_datos.getValueAt(i,9).toString().compareTo("-")==0)
                                        tabla.addCell(reporte.celda("",font,contenido,centro,0,1, Rectangle.RECTANGLE));
                                    else
                                    {
                                        switch(renglon)
                                        {
                                            case 8:
                                                tabla.addCell(reporte.celda("N",font,contenido,centro,0,1, Rectangle.RECTANGLE));
                                                break;
                                            case 10:
                                                tabla.addCell(reporte.celda("D",font,contenido,centro,0,1, Rectangle.RECTANGLE));
                                                break;
                                            case 11:
                                                tabla.addCell(reporte.celda("R",font,contenido,centro,0,1, Rectangle.RECTANGLE));
                                                break;
                                            case 12:
                                                tabla.addCell(reporte.celda("M",font,contenido,centro,0,1, Rectangle.RECTANGLE));
                                                break;
                                        }
                                    }
                                    //columna6
                                    tabla.addCell(reporte.celda(formatoPorcentaje.format(t_datos.getValueAt(i,15)),font,contenido,derecha,0,1, Rectangle.RECTANGLE));
                                    //columna7 $tot aut.
                                    double n;
                                    n= BigDecimal.valueOf(Double.parseDouble(t_datos.getValueAt(i,renglon).toString())*Double.parseDouble(t_datos.getValueAt(i,15).toString())).setScale(2, RoundingMode.UP).doubleValue();
                                    tabla.addCell(reporte.celda(formatoPorcentaje.format(n),font,contenido,derecha,0,1, Rectangle.RECTANGLE));
                                    //columna8
                                    tabla.addCell(reporte.celda(formatoPorcentaje.format(t_datos.getValueAt(i,16)),font,contenido,derecha,0,1, Rectangle.RECTANGLE));
                                    
                                    //columna9 $tot com
                                    n= BigDecimal.valueOf(Double.parseDouble(t_datos.getValueAt(i,renglon).toString())*Double.parseDouble(t_datos.getValueAt(i,16).toString())).setScale(2, RoundingMode.UP).doubleValue();
                                    tabla.addCell(reporte.celda(formatoPorcentaje.format(n),font,contenido,derecha,0,1, Rectangle.RECTANGLE));
                                    
                                    //columna10 11
                                    if(renglon==8 && t_datos.getValueAt(i,9).toString().compareTo("-")==0)
                                    {
                                        tabla.addCell(reporte.celda("",font,contenido,centro,0,1, Rectangle.RECTANGLE));
                                        tabla.addCell(reporte.celda("",font,contenido,centro,0,1, Rectangle.RECTANGLE));
                                    }
                                    else
                                    {
                                        switch(renglon)
                                        {
                                            case 8:
                                                n= BigDecimal.valueOf(Double.parseDouble(t_datos.getValueAt(i, 16).toString())/0.9d).setScale(2, RoundingMode.UP).doubleValue();
                                                tabla.addCell(reporte.celda(formatoPorcentaje.format(n*Double.parseDouble(t_datos.getValueAt(i,renglon).toString())),font,contenido,derecha,0,1, Rectangle.RECTANGLE));
                                                tabla.addCell(reporte.celda(formatoPorcentaje.format(n),font,contenido,derecha,0,1, Rectangle.RECTANGLE));
                                                break;
                                            case 10:
                                                n= BigDecimal.valueOf(Double.parseDouble(t_datos.getValueAt(i, 15).toString())*0.72d).setScale(2, RoundingMode.UP).doubleValue();
                                                tabla.addCell(reporte.celda(formatoPorcentaje.format(n*Double.parseDouble(t_datos.getValueAt(i,renglon).toString())),font,contenido,derecha,0,1, Rectangle.RECTANGLE));
                                                tabla.addCell(reporte.celda(formatoPorcentaje.format(n),font,contenido,derecha,0,1, Rectangle.RECTANGLE));
                                                break;
                                            case 11:
                                                n= BigDecimal.valueOf(Double.parseDouble(t_datos.getValueAt(i, 15).toString())*0.65d).setScale(2, RoundingMode.UP).doubleValue();
                                                tabla.addCell(reporte.celda(formatoPorcentaje.format(n*Double.parseDouble(t_datos.getValueAt(i,renglon).toString())),font,contenido,derecha,0,1, Rectangle.RECTANGLE));
                                                tabla.addCell(reporte.celda(formatoPorcentaje.format(n),font,contenido,derecha,0,1, Rectangle.RECTANGLE));
                                                break;
                                            case 12:
                                                n= BigDecimal.valueOf(Double.parseDouble(t_datos.getValueAt(i, 15).toString())*0.65d).setScale(2, RoundingMode.UP).doubleValue();
                                                tabla.addCell(reporte.celda(formatoPorcentaje.format(n*Double.parseDouble(t_datos.getValueAt(i,renglon).toString())),font,contenido,derecha,0,1, Rectangle.RECTANGLE));
                                                tabla.addCell(reporte.celda(formatoPorcentaje.format(n),font,contenido,derecha,0,1, Rectangle.RECTANGLE));
                                                break;
                                        }
                                    }
                                    //columna12
                                    if(t_datos.getValueAt(i,18)==null){
                                    tabla.addCell(reporte.celda("",font,contenido,izquierda,0,1, Rectangle.RECTANGLE));
                                    }else{
                                        tabla.addCell(reporte.celda(t_datos.getValueAt(i,18).toString(),font,contenido,izquierda,0,1, Rectangle.RECTANGLE));
                                    }
                                    
                                    //columna13
                                    if(t_datos.getValueAt(i,19)==null){
                                    tabla.addCell(reporte.celda("",font,contenido,centro,0,1, Rectangle.RECTANGLE));
                                    }else{
                                    tabla.addCell(reporte.celda(t_datos.getValueAt(i,19).toString(),font,contenido,centro,0,1, Rectangle.RECTANGLE));
                                    }
                                    //columna14
                                    tabla.addCell(reporte.celda("V",font,contenido,centro,0,1, Rectangle.RECTANGLE));
                                }
                            }
                        }
                    }
                    font1.setColor(BaseColor.WHITE);
                    tabla.addCell(reporte.celda("Faltante en Vales",font1,contenido1,centro,15,1, Rectangle.RECTANGLE));
                    
                    for (int i = 0; i < t_datos.getRowCount(); i++) 
                    {  
                        for(int j=0; j<4; j++)
                        {
                            int renglon=0;
                            switch(j)
                            {
                                case 0:
                                    renglon=8;
                                    break;
                                case 1:
                                    renglon=10;
                                    break;
                                case 2:
                                    renglon=11;
                                    break;
                                case 3:
                                    renglon=12;
                                    break;
                            }
                            if( (Double.parseDouble(t_datos.getValueAt(i,renglon).toString())>0 && t_datos.getValueAt(i,9).toString().compareTo("N")==0) || 
                                    ((Double.parseDouble(t_datos.getValueAt(i,renglon).toString())>0 && renglon>=10)) ||
                                    (renglon==8 && Double.parseDouble(t_datos.getValueAt(i,10).toString())<=0 && Double.parseDouble(t_datos.getValueAt(i,11).toString())<=0 &&  Double.parseDouble(t_datos.getValueAt(i,12).toString())<=0) )
                            {
                                System.out.println("entro");
                                if(renglon==8 && t_datos.getValueAt(i,9).toString().compareTo("-")==0)
                                    contenido = BaseColor.WHITE;
                                else
                                    contenido = BaseColor.LIGHT_GRAY;

                                if((boolean)t_datos.getValueAt(i,3)==false && (boolean)t_datos.getValueAt(i, 4)==false && t_datos.getValueAt(i,5)!=null)
                                {
                                    //columna0
                                    if(t_datos.getValueAt(i,5)==null){
                                        tabla.addCell(reporte.celda("",font,contenido,centro,0,1, Rectangle.RECTANGLE));
                                    }else{
                                        tabla.addCell(reporte.celda(t_datos.getValueAt(i,5).toString(),font,contenido,centro,0,1, Rectangle.RECTANGLE));
                                    }
                                    //columna1
                                    if(t_datos.getValueAt(i,6)==null){
                                        tabla.addCell(reporte.celda("",font,contenido,centro,0,1, Rectangle.RECTANGLE));
                                    }else{
                                        tabla.addCell(reporte.celda(t_datos.getValueAt(i,6).toString(),font,contenido,centro,0,1, Rectangle.RECTANGLE));
                                    }
                                    //columna2
                                    tabla.addCell(reporte.celda(t_datos.getValueAt(i,renglon).toString(),font,contenido,centro,0,1, Rectangle.RECTANGLE));
                                  
                                    //columna3
                                    if(t_datos.getValueAt(i,14)==null){
                                        tabla.addCell(reporte.celda("",font,contenido,centro,0,1, Rectangle.RECTANGLE));
                                    }else{
                                        tabla.addCell(reporte.celda(t_datos.getValueAt(i,14).toString(),font,contenido,centro,0,1, Rectangle.RECTANGLE));
                                    }
                                    //columna4
                                    tabla.addCell(reporte.celda(t_datos.getValueAt(i,2).toString(),font,contenido,izquierda,0,1, Rectangle.RECTANGLE));
                                    //columna5
                                    if(renglon==8 && t_datos.getValueAt(i,9).toString().compareTo("-")==0)
                                        tabla.addCell(reporte.celda("",font,contenido,centro,0,1, Rectangle.RECTANGLE));
                                    else
                                    {
                                        switch(renglon)
                                        {
                                            case 8:
                                                tabla.addCell(reporte.celda("N",font,contenido,centro,0,1, Rectangle.RECTANGLE));
                                                break;
                                            case 10:
                                                tabla.addCell(reporte.celda("D",font,contenido,centro,0,1, Rectangle.RECTANGLE));
                                                break;
                                            case 11:
                                                tabla.addCell(reporte.celda("R",font,contenido,centro,0,1, Rectangle.RECTANGLE));
                                                break;
                                            case 12:
                                                tabla.addCell(reporte.celda("M",font,contenido,centro,0,1, Rectangle.RECTANGLE));
                                                break;
                                        }
                                    }
                                    //columna6
                                    tabla.addCell(reporte.celda(formatoPorcentaje.format(t_datos.getValueAt(i,15)),font,contenido,derecha,0,1, Rectangle.RECTANGLE));
                                    //columna7 $tot aut.
                                    double n;
                                    n= BigDecimal.valueOf(Double.parseDouble(t_datos.getValueAt(i,renglon).toString())*Double.parseDouble(t_datos.getValueAt(i,15).toString())).setScale(2, RoundingMode.UP).doubleValue();
                                    tabla.addCell(reporte.celda(formatoPorcentaje.format(n),font,contenido,derecha,0,1, Rectangle.RECTANGLE));
                                    //columna8
                                    tabla.addCell(reporte.celda(formatoPorcentaje.format(t_datos.getValueAt(i,16)),font,contenido,derecha,0,1, Rectangle.RECTANGLE));
                                    
                                    //columna9 $tot com
                                    n= BigDecimal.valueOf(Double.parseDouble(t_datos.getValueAt(i,renglon).toString())*Double.parseDouble(t_datos.getValueAt(i,16).toString())).setScale(2, RoundingMode.UP).doubleValue();
                                    tabla.addCell(reporte.celda(formatoPorcentaje.format(n),font,contenido,derecha,0,1, Rectangle.RECTANGLE));
                                    
                                    //columna10 11
                                    if(renglon==8 && t_datos.getValueAt(i,9).toString().compareTo("-")==0)
                                    {
                                        tabla.addCell(reporte.celda("",font,contenido,centro,0,1, Rectangle.RECTANGLE));
                                        tabla.addCell(reporte.celda("",font,contenido,centro,0,1, Rectangle.RECTANGLE));
                                    }
                                    else
                                    {
                                        switch(renglon)
                                        {
                                            case 8:
                                                n= BigDecimal.valueOf(Double.parseDouble(t_datos.getValueAt(i, 16).toString())/0.9d).setScale(2, RoundingMode.UP).doubleValue();
                                                tabla.addCell(reporte.celda(formatoPorcentaje.format(n*Double.parseDouble(t_datos.getValueAt(i,renglon).toString())),font,contenido,derecha,0,1, Rectangle.RECTANGLE));
                                                tabla.addCell(reporte.celda(formatoPorcentaje.format(n),font,contenido,derecha,0,1, Rectangle.RECTANGLE));
                                                break;
                                            case 10:
                                                n= BigDecimal.valueOf(Double.parseDouble(t_datos.getValueAt(i, 15).toString())*0.72d).setScale(2, RoundingMode.UP).doubleValue();
                                                tabla.addCell(reporte.celda(formatoPorcentaje.format(n*Double.parseDouble(t_datos.getValueAt(i,renglon).toString())),font,contenido,derecha,0,1, Rectangle.RECTANGLE));
                                                tabla.addCell(reporte.celda(formatoPorcentaje.format(n),font,contenido,derecha,0,1, Rectangle.RECTANGLE));
                                                break;
                                            case 11:
                                                n= BigDecimal.valueOf(Double.parseDouble(t_datos.getValueAt(i, 15).toString())*0.65d).setScale(2, RoundingMode.UP).doubleValue();
                                                tabla.addCell(reporte.celda(formatoPorcentaje.format(n*Double.parseDouble(t_datos.getValueAt(i,renglon).toString())),font,contenido,derecha,0,1, Rectangle.RECTANGLE));
                                                tabla.addCell(reporte.celda(formatoPorcentaje.format(n),font,contenido,derecha,0,1, Rectangle.RECTANGLE));
                                                break;
                                            case 12:
                                                n= BigDecimal.valueOf(Double.parseDouble(t_datos.getValueAt(i, 15).toString())*0.65d).setScale(2, RoundingMode.UP).doubleValue();
                                                tabla.addCell(reporte.celda(formatoPorcentaje.format(n*Double.parseDouble(t_datos.getValueAt(i,renglon).toString())),font,contenido,derecha,0,1, Rectangle.RECTANGLE));
                                                tabla.addCell(reporte.celda(formatoPorcentaje.format(n),font,contenido,derecha,0,1, Rectangle.RECTANGLE));
                                                break;
                                        }
                                    }
                                    //columna12
                                    if(t_datos.getValueAt(i,18)==null){
                                    tabla.addCell(reporte.celda("",font,contenido,izquierda,0,1, Rectangle.RECTANGLE));
                                    }else{
                                        tabla.addCell(reporte.celda(t_datos.getValueAt(i,18).toString(),font,contenido,izquierda,0,1, Rectangle.RECTANGLE));
                                    }
                                    
                                    //columna13
                                    if(t_datos.getValueAt(i,19)==null){
                                    tabla.addCell(reporte.celda("",font,contenido,centro,0,1, Rectangle.RECTANGLE));
                                    }else{
                                    tabla.addCell(reporte.celda(t_datos.getValueAt(i,19).toString(),font,contenido,centro,0,1, Rectangle.RECTANGLE));
                                    }
                                    //columna14
                                    tabla.addCell(reporte.celda("",font,contenido,centro,0,1, Rectangle.RECTANGLE));
                                }
                            }
                        }
                    }    
                    tabla.addCell(reporte.celda("Total",font,contenido,derecha,11,1, Rectangle.NO_BORDER));
                    tabla.addCell(reporte.celda(formatoPorcentaje.format(Double.parseDouble(total.getValue().toString())),font,contenido,derecha,0,1, Rectangle.RECTANGLE));
                    tabla.addCell(reporte.celda(" ",font,contenido,centro,3,1, Rectangle.NO_BORDER));
                    tabla.setHeaderRows(1);
                    reporte.agregaObjeto(tabla);
                    reporte.cerrar();
                    reporte.visualizar2(ruta + ".pdf");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_jButton2ActionPerformed

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
    }//GEN-LAST:event_b_busca1ActionPerformed

    private void t_datosKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_datosKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_t_datosKeyPressed

    private void suma()
    {
        double suma=0.00d;
        for(int x=0; x<t_datos.getRowCount(); x++)
        {
            if( (t_datos.getValueAt(x, 1).toString().compareTo("AC")!=0 &&( ((boolean)t_datos.getValueAt(x, 3))==true ||  ((boolean)t_datos.getValueAt(x, 4))==true)) || (t_datos.getValueAt(x, 1).toString().compareTo("AC")==0 && (t_datos.getValueAt(x, 5)!=null)))
                suma+=(double)t_datos.getValueAt(x, 17);
        }
        total.setValue(suma);
    }
    private static void cabecera(PDF reporte, BaseFont bf, PdfPTable tabla, String titulo, int op) {
        
        com.itextpdf.text.Font font = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 7, com.itextpdf.text.Font.BOLD);
        font.setColor(BaseColor.WHITE);
        BaseColor cabecera = BaseColor.DARK_GRAY;
        BaseColor contenido = BaseColor.WHITE;
        int centro = com.itextpdf.text.Element.ALIGN_CENTER;
        int izquierda = com.itextpdf.text.Element.ALIGN_LEFT;
        int derecha = com.itextpdf.text.Element.ALIGN_RIGHT;
        tabla.addCell(reporte.celda("N°.SET", font, cabecera, centro, 0, 1, Rectangle.RECTANGLE));
        tabla.addCell(reporte.celda("N°.EXT", font, cabecera, centro, 0, 1, Rectangle.RECTANGLE));
        tabla.addCell(reporte.celda("CNT", font, cabecera, centro, 0, 1, Rectangle.RECTANGLE));
        tabla.addCell(reporte.celda("N°PARTE", font, cabecera, centro, 0, 1, Rectangle.RECTANGLE));
        tabla.addCell(reporte.celda("DESCRIPCIÓN", font, cabecera, centro, 0, 1, Rectangle.RECTANGLE));
        tabla.addCell(reporte.celda("ORI", font, cabecera, centro, 0, 1, Rectangle.RECTANGLE));
        tabla.addCell(reporte.celda("$C/U AUT", font, cabecera, centro, 0, 1, Rectangle.RECTANGLE));
        tabla.addCell(reporte.celda("$TOT AUT", font, cabecera, centro, 0, 1, Rectangle.RECTANGLE));
        tabla.addCell(reporte.celda("$C/U COM", font, cabecera, centro, 0, 1, Rectangle.RECTANGLE));
        tabla.addCell(reporte.celda("$TOT COM", font, cabecera, centro, 0, 1, Rectangle.RECTANGLE));
        tabla.addCell(reporte.celda("$C/U FAC", font, cabecera, centro, 0, 1, Rectangle.RECTANGLE)); 
        tabla.addCell(reporte.celda("$TOT FAC", font, cabecera, centro, 0, 1, Rectangle.RECTANGLE));
        tabla.addCell(reporte.celda("PROVEEDOR", font, cabecera, centro, 0, 1, Rectangle.RECTANGLE));
        tabla.addCell(reporte.celda("#FACTURA", font, cabecera, centro, 0, 1, Rectangle.RECTANGLE));
        tabla.addCell(reporte.celda("VAL", font, cabecera, centro, 0, 1, Rectangle.RECTANGLE));
     }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton b_busca1;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane scroll;
    private javax.swing.JTextField t_busca;
    private javax.swing.JTable t_datos;
    private javax.swing.JComboBox tipo;
    private javax.swing.JFormattedTextField total;
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
                                        if(t_datos.getValueAt(row, 1).toString().compareTo("I")==0)
                                        {
                                            Partida par=(Partida)session.get(Partida.class, (Integer)t_datos.getValueAt(row, 0));
                                            par.setFacturado((boolean)value);
                                            session.update(par);
                                            session.beginTransaction().commit();
                                            vector.setElementAt(value, col);
                                            this.dataVector.setElementAt(vector, row);
                                            fireTableCellUpdated(row, col);
                                        }
                                        else
                                        {
                                            PartidaExterna par=(PartidaExterna)session.get(PartidaExterna.class, (Integer)t_datos.getValueAt(row, 0));
                                            System.out.println(value);
                                            par.setFacturado((boolean)value);
                                            session.update(par);
                                            session.beginTransaction().commit();
                                            vector.setElementAt(value, col);
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
                                
                            case 9:
                                if(vector.get(col)==null)
                                {
                                    vector.setElementAt(value, col);
                                    this.dataVector.setElementAt(vector, row);
                                    fireTableCellUpdated(row, col);
                                }
                                else
                                {
                                    double tot=0.0;
                                    if(((String)value).compareTo("N")==0)
                                        tot=Double.parseDouble(t_datos.getValueAt(row, 8).toString());
                                    tot+=Double.parseDouble(t_datos.getValueAt(row, 10).toString());
                                    tot+=Double.parseDouble(t_datos.getValueAt(row, 11).toString());
                                    tot+=Double.parseDouble(t_datos.getValueAt(row, 12).toString());
                                    if(tot<=Double.parseDouble(t_datos.getValueAt(row, 7).toString()))
                                    {
                                        Session session = HibernateUtil.getSessionFactory().openSession();
                                        try
                                        {
                                            session.beginTransaction().begin();
                                            String dato=t_datos.getValueAt(row, 1).toString();
                                            if(dato.compareToIgnoreCase("o")==0 || dato.compareToIgnoreCase("c")==0 || dato.compareToIgnoreCase("a")==0 || dato.compareToIgnoreCase("e")==0)
                                            {
                                                Partida par=(Partida)session.get(Partida.class, (Integer)t_datos.getValueAt(row, 0));
                                                par.setOriCon((String)value);
                                                session.update(par);
                                                session.beginTransaction().commit();
                                            }
                                            else
                                            {
                                                PartidaExterna par=(PartidaExterna)session.get(PartidaExterna.class, (Integer)t_datos.getValueAt(row, 0));
                                                par.setOriCon((String)value);
                                                session.update(par);
                                                session.beginTransaction().commit();
                                            }
                                            double suma=0.0d;//Double.parseDouble(t_datos.getValueAt(row, 12).toString())
                                            if(((String)value).compareToIgnoreCase("-")!=0)
                                                suma=Double.parseDouble(t_datos.getValueAt(row, 8).toString())*(Double.parseDouble(t_datos.getValueAt(row, 16).toString())/0.9);
                                            if(Double.parseDouble(t_datos.getValueAt(row, 10).toString())>0)
                                                suma+=Double.parseDouble(t_datos.getValueAt(row, 10).toString())*(Double.parseDouble(t_datos.getValueAt(row, 15).toString())*0.72);
                                            if(Double.parseDouble(t_datos.getValueAt(row, 11).toString())>0)
                                                suma+=Double.parseDouble(t_datos.getValueAt(row, 11).toString())*(Double.parseDouble(t_datos.getValueAt(row, 15).toString())*0.65);
                                            if(Double.parseDouble(t_datos.getValueAt(row, 12).toString())>0)
                                                suma+=Double.parseDouble(t_datos.getValueAt(row, 12).toString())*(Double.parseDouble(t_datos.getValueAt(row, 15).toString())*0.65);
                                            vector.setElementAt(value, col);
                                            vector.setElementAt(suma, 17);
                                            this.dataVector.setElementAt(vector, row);
                                            fireTableCellUpdated(row, col);
                                            fireTableCellUpdated(row, 17);
                                            suma();
                                        
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
                                    else
                                        JOptionPane.showMessageDialog(null, "La suma de N, D, R, M excede del total Autorizado");
                                }
                                break;
                            
                            case 10:
                                if(vector.get(col)==null)
                                {
                                    vector.setElementAt(value, col);
                                    this.dataVector.setElementAt(vector, row);
                                    fireTableCellUpdated(row, col);
                                }
                                else
                                {
                                    double tot=0.0;
                                    if(t_datos.getValueAt(row, 9).toString().compareTo("N")==0)
                                        tot=Double.parseDouble(t_datos.getValueAt(row, 8).toString());
                                    tot+=(double)value;
                                    tot+=Double.parseDouble(t_datos.getValueAt(row, 11).toString());
                                    tot+=Double.parseDouble(t_datos.getValueAt(row, 12).toString());
                                    if(tot<=Double.parseDouble(t_datos.getValueAt(row, 7).toString()))
                                    {
                                        Session session = HibernateUtil.getSessionFactory().openSession();
                                        try
                                        {
                                            session.beginTransaction().begin();
                                            String dato=t_datos.getValueAt(row, 1).toString();
                                            if(dato.compareToIgnoreCase("o")==0 || dato.compareToIgnoreCase("c")==0 || dato.compareToIgnoreCase("a")==0 || dato.compareToIgnoreCase("e")==0)
                                            {
                                                Partida par=(Partida)session.get(Partida.class, (Integer)t_datos.getValueAt(row, 0));
                                                par.setD((double)value);
                                                session.update(par);
                                                session.beginTransaction().commit();
                                            }
                                            else
                                            {
                                                PartidaExterna par=(PartidaExterna)session.get(PartidaExterna.class, (Integer)t_datos.getValueAt(row, 0));
                                                par.setD((double)value);
                                                session.update(par);
                                                session.beginTransaction().commit();
                                            }
                                            double suma=0.0d;//Double.parseDouble(t_datos.getValueAt(row, 12).toString())
                                            if(t_datos.getValueAt(row, 9).toString().compareToIgnoreCase("-")!=0)
                                                suma=Double.parseDouble(t_datos.getValueAt(row, 8).toString())*(Double.parseDouble(t_datos.getValueAt(row, 16).toString())/0.9);
                                            if((double)value>0)
                                                suma+=((double)value)*(Double.parseDouble(t_datos.getValueAt(row, 15).toString())*0.72);
                                            if(Double.parseDouble(t_datos.getValueAt(row, 11).toString())>0)
                                                suma+=Double.parseDouble(t_datos.getValueAt(row, 11).toString())*(Double.parseDouble(t_datos.getValueAt(row, 15).toString())*0.65);
                                            if(Double.parseDouble(t_datos.getValueAt(row, 12).toString())>0)
                                                suma+=Double.parseDouble(t_datos.getValueAt(row, 12).toString())*(Double.parseDouble(t_datos.getValueAt(row, 15).toString())*0.65);
                                            vector.setElementAt(value, col);
                                            vector.setElementAt(suma, 17);
                                            this.dataVector.setElementAt(vector, row);
                                            fireTableCellUpdated(row, col);
                                            fireTableCellUpdated(row, 17);
                                            suma();

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
                                    else
                                        JOptionPane.showMessageDialog(null, "La suma de N, D, R, M excede del total Autorizado");
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
                                    double tot=0.0;
                                    if(t_datos.getValueAt(row, 9).toString().compareTo("N")==0)
                                        tot=Double.parseDouble(t_datos.getValueAt(row, 8).toString());
                                    tot+=Double.parseDouble(t_datos.getValueAt(row, 10).toString());
                                    tot+=(double)value;
                                    tot+=Double.parseDouble(t_datos.getValueAt(row, 12).toString());
                                    if(tot<=Double.parseDouble(t_datos.getValueAt(row, 7).toString()))
                                    {
                                        Session session = HibernateUtil.getSessionFactory().openSession();
                                        try
                                        {
                                            session.beginTransaction().begin();
                                            String dato=t_datos.getValueAt(row, 1).toString();
                                            if(dato.compareToIgnoreCase("o")==0 || dato.compareToIgnoreCase("c")==0 || dato.compareToIgnoreCase("a")==0 || dato.compareToIgnoreCase("e")==0)
                                            {
                                                Partida par=(Partida)session.get(Partida.class, (Integer)t_datos.getValueAt(row, 0));
                                                par.setR((double)value);
                                                session.update(par);
                                                session.beginTransaction().commit();
                                            }
                                            else
                                            {
                                                PartidaExterna par=(PartidaExterna)session.get(PartidaExterna.class, (Integer)t_datos.getValueAt(row, 0));
                                                par.setR((double)value);
                                                session.update(par);
                                                session.beginTransaction().commit();
                                            }
                                            double suma=0.0d;//Double.parseDouble(t_datos.getValueAt(row, 12).toString())
                                            if(t_datos.getValueAt(row, 9).toString().compareToIgnoreCase("-")!=0)
                                                suma=Double.parseDouble(t_datos.getValueAt(row, 8).toString())*(Double.parseDouble(t_datos.getValueAt(row, 16).toString())/0.9);
                                            if(Double.parseDouble(t_datos.getValueAt(row, 10).toString())>0)
                                                suma+=Double.parseDouble(t_datos.getValueAt(row, 10).toString())*(Double.parseDouble(t_datos.getValueAt(row, 15).toString())*0.72);
                                            if(((double)value)>0)
                                                suma+=((double)value)*(Double.parseDouble(t_datos.getValueAt(row, 15).toString())*0.65);
                                            if(Double.parseDouble(t_datos.getValueAt(row, 12).toString())>0)
                                                suma+=Double.parseDouble(t_datos.getValueAt(row, 12).toString())*(Double.parseDouble(t_datos.getValueAt(row, 15).toString())*0.65);
                                            vector.setElementAt(value, col);
                                            vector.setElementAt(suma, 17);
                                            this.dataVector.setElementAt(vector, row);
                                            fireTableCellUpdated(row, col);
                                            fireTableCellUpdated(row, 17);
                                            suma();

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
                                    else
                                        JOptionPane.showMessageDialog(null, "La suma de N, D, R, M excede del total Autorizado");
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
                                    double tot=0.0;
                                    if(t_datos.getValueAt(row, 9).toString().compareTo("N")==0)
                                        tot=Double.parseDouble(t_datos.getValueAt(row, 8).toString());
                                    tot+=Double.parseDouble(t_datos.getValueAt(row, 10).toString());
                                    tot+=Double.parseDouble(t_datos.getValueAt(row, 11).toString());
                                    tot+=(double)value;
                                    if(tot<=Double.parseDouble(t_datos.getValueAt(row, 7).toString()))
                                    {
                                        Session session = HibernateUtil.getSessionFactory().openSession();
                                        try
                                        {
                                            session.beginTransaction().begin();
                                            String dato=t_datos.getValueAt(row, 1).toString();
                                            if(dato.compareToIgnoreCase("o")==0 || dato.compareToIgnoreCase("c")==0 || dato.compareToIgnoreCase("a")==0 || dato.compareToIgnoreCase("e")==0)
                                            {
                                                Partida par=(Partida)session.get(Partida.class, (Integer)t_datos.getValueAt(row, 0));
                                                par.setM((double)value);
                                                session.update(par);
                                                session.beginTransaction().commit();
                                            }
                                            else
                                            {
                                                PartidaExterna par=(PartidaExterna)session.get(PartidaExterna.class, (Integer)t_datos.getValueAt(row, 0));
                                                par.setM((double)value);
                                                session.update(par);
                                                session.beginTransaction().commit();
                                            }
                                            double suma=0.0d;//Double.parseDouble(t_datos.getValueAt(row, 12).toString())
                                            if(t_datos.getValueAt(row, 9).toString().compareToIgnoreCase("-")!=0)
                                                suma=Double.parseDouble(t_datos.getValueAt(row, 8).toString())*(Double.parseDouble(t_datos.getValueAt(row, 16).toString())/0.9);
                                            if(Double.parseDouble(t_datos.getValueAt(row, 10).toString())>0)
                                                suma+=Double.parseDouble(t_datos.getValueAt(row, 10).toString())*(Double.parseDouble(t_datos.getValueAt(row, 15).toString())*0.72);
                                            if(Double.parseDouble(t_datos.getValueAt(row, 11).toString())>0)
                                                suma+=Double.parseDouble(t_datos.getValueAt(row, 11).toString())*(Double.parseDouble(t_datos.getValueAt(row, 15).toString())*0.65);
                                            if(((double)value)>0)
                                                suma+=((double)value)*(Double.parseDouble(t_datos.getValueAt(row, 15).toString())*0.65);
                                            vector.setElementAt(value, col);
                                            vector.setElementAt(suma, 17);
                                            this.dataVector.setElementAt(vector, row);
                                            fireTableCellUpdated(row, col);
                                            fireTableCellUpdated(row, 17);
                                            suma();

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
                                    else
                                        JOptionPane.showMessageDialog(null, "La suma de N, D, R, M excede del total Autorizado");
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
    
}
