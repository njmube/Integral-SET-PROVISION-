/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Valuacion;

import Hibernate.Util.HibernateUtil;
import Hibernate.entidades.Configuracion;
import Hibernate.entidades.Foto;
import Hibernate.entidades.Orden;
import Hibernate.entidades.Partida;
import Hibernate.entidades.Usuario;
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
import java.io.File;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
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
import Integral.VerticalBarUI;
import Integral.VerticalTableHeaderCellRenderer;
import java.awt.event.KeyEvent;

/**
 *
 * @author I.S.C Salvador
 */
public class Autorizacion extends javax.swing.JPanel {

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
    
    String[] columnas = new String [] {/*33*/
        "No","#",
        "Esp Hoj","Esp Mec","Esp Sus","Esp Ele",
        "Can","C.Aut","Med","Descripción","Fol","Codigo",
        "I. DM","Camb","Rep Min","Rep Med","Rep Max","Pin Min","Pin Med","Pin Max","Instrucción", 
        "I. DM","Camb","Rep Min","Rep Med","Rep Max","Pin Min","Pin Med","Pin Max",
        "Tipo", "Orden", "R. Cot", "Aut"};
    
    public Autorizacion(String ord, Usuario us, String edo, String ses) {
        initComponents();
        scroll.getVerticalScrollBar().setUI(new VerticalBarUI());
        scroll.getHorizontalScrollBar().setUI(new HorizontalBarUI());
        sessionPrograma=ses;
        orden=ord;
        user=us;
        model=new MyModel(0, columnas);
        t_datos.setModel(model);
        formatoTabla();
        t_datos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        formato = new FormatoTabla();
        buscaCuentas(-1, -1);
        if(edo.compareTo("")==0)
        {
            visualiza(true, true, true, true, true, true, true);
            
        }
        else
            visualiza(false, false, false, false, false, false, false);
        
        h=new Herramientas(user, 0);
        if(h.isCerrada(orden)==true)
            visualiza(false, false, false, true, true, true, false);
        entro=1;
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
                  
                  case 6:
                      column.setPreferredWidth(50);
                      //column.setCellRenderer(tcr);
                      break;
                      
                  case 7:
                      column.setPreferredWidth(50);
                      //column.setCellRenderer(tcr);
                      column.setCellEditor(new DefaultCellEditor(t_numero));
                      break;    
                      
                  case 8:
                      column.setPreferredWidth(50);
                      DefaultCellEditor editor = new DefaultCellEditor(medida);
                      column.setCellEditor(editor); 
                      editor.setClickCountToStart(2);
                      break;

                  case 9:
                      column.setPreferredWidth(300);
                      break;

                  case 10:
                      column.setPreferredWidth(50);
                      //column.setCellRenderer(tcr);
                      break;

                  case 11:
                      column.setPreferredWidth(100);
                      //column.setCellRenderer(tcr);
                      DefaultCellEditor miEditor = new DefaultCellEditor(numeros);
                      miEditor.setClickCountToStart(2);
                      column.setCellEditor(miEditor);
                      break;
                  case 20:
                      column.setPreferredWidth(100);
                      break;
                  
                  case 21:
                      column.setPreferredWidth(35);
                      break;
                      
                  case 22:
                      column.setPreferredWidth(35);
                      break;
                      
                  case 23:
                      column.setPreferredWidth(35);
                      break;
                      
                  case 24:
                      column.setPreferredWidth(35);
                      break;
                  
                  case 25:
                      column.setPreferredWidth(35);
                      break;
                  
                  case 26:
                      column.setPreferredWidth(35);
                      break;
                      
                  case 27:
                      column.setPreferredWidth(35);
                      break;
                      
                  case 28:
                      column.setPreferredWidth(35);
                      break;
                      
                  case 29:
                      column.setPreferredWidth(20);
                      break;
                  
                  case 30:
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
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        t_busca = new javax.swing.JTextField();
        b_busca = new javax.swing.JButton();
        b_exel = new javax.swing.JButton();
        b_pdfh = new javax.swing.JButton();
        c_filtro = new javax.swing.JComboBox();
        b_pdfx = new javax.swing.JButton();
        scroll = new javax.swing.JScrollPane();
        t_datos = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();

        medida.setFont(new java.awt.Font("Dialog", 0, 9)); // NOI18N
        medida.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "PZAS", "LTS", "MTS", "CMS", "MMS", "GRS", "MLS", "KGS", "HRS", "MIN", "KIT", "FT", "LB", "JGO", "NA" }));

        t_numero.setFont(new java.awt.Font("Dialog", 0, 10)); // NOI18N
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

        instruccion.setText("jTextField1");
        instruccion.setBorder(null);
        instruccion.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                instruccionKeyTyped(evt);
            }
        });

        numeros.setFont(new java.awt.Font("Dialog", 0, 9)); // NOI18N

        b_valuacion.setFont(new java.awt.Font("Tahoma", 0, 9)); // NOI18N
        b_valuacion.setText("A Valuación");
        b_valuacion.setToolTipText("envia a valuacion las partidas");
        b_valuacion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_valuacionActionPerformed(evt);
            }
        });

        setBackground(new java.awt.Color(255, 255, 255));
        setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        setLayout(new java.awt.BorderLayout());

        jPanel1.setBackground(new java.awt.Color(2, 135, 242));
        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Arial", 0, 9)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Buscar:");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, -1, -1));

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
        jPanel1.add(t_busca, new org.netbeans.lib.awtextra.AbsoluteConstraints(42, 2, 210, -1));

        b_busca.setIcon(new ImageIcon("imagenes/buscar1.png"));
        b_busca.setToolTipText("Busca una partida");
        b_busca.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_buscaActionPerformed(evt);
            }
        });
        jPanel1.add(b_busca, new org.netbeans.lib.awtextra.AbsoluteConstraints(253, 2, 23, 23));

        b_exel.setIcon(new ImageIcon("imagenes/xls_icon.png"));
        b_exel.setToolTipText("Exporta a EXCEL");
        b_exel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_exelActionPerformed(evt);
            }
        });
        jPanel1.add(b_exel, new org.netbeans.lib.awtextra.AbsoluteConstraints(452, 1, 23, 23));

        b_pdfh.setIcon(new ImageIcon("imagenes/pdf_h.png"));
        b_pdfh.setToolTipText("Exporta a PDF");
        b_pdfh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_pdfhActionPerformed(evt);
            }
        });
        jPanel1.add(b_pdfh, new org.netbeans.lib.awtextra.AbsoluteConstraints(427, 1, 23, 23));

        c_filtro.setFont(new java.awt.Font("Tahoma", 0, 9)); // NOI18N
        c_filtro.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Todos", "Hojalateria", "Mecanica", "Suspension", "Electricidad" }));
        jPanel1.add(c_filtro, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 4, 100, -1));

        b_pdfx.setIcon(new ImageIcon("imagenes/pdf_x.png"));
        b_pdfx.setToolTipText("Exporta a PDF");
        b_pdfx.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_pdfxActionPerformed(evt);
            }
        });
        jPanel1.add(b_pdfx, new org.netbeans.lib.awtextra.AbsoluteConstraints(402, 1, 23, 23));

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

        jLabel2.setBackground(new java.awt.Color(2, 135, 242));
        jLabel2.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Instrucción Final");
        jLabel2.setOpaque(true);

        jLabel3.setBackground(new java.awt.Color(2, 135, 242));
        jLabel3.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("Interno");
        jLabel3.setOpaque(true);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap(390, Short.MAX_VALUE)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 266, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 247, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(143, 143, 143))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3)))
        );

        add(jPanel2, java.awt.BorderLayout.PAGE_START);
    }// </editor-fold>//GEN-END:initComponents

    private void b_buscaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_buscaActionPerformed
        // TODO add your handling code here:
        h=new Herramientas(user, 0);
        h.session(sessionPrograma);
        if(this.t_busca.getText().compareToIgnoreCase("")!=0)
        {
            buscaCuentas(-1, -1);
            if(x>=t_datos.getRowCount())
            {
                x=0;
                java.awt.Rectangle r = t_datos.getCellRect( x, 3, true);
                t_datos.scrollRectToVisible(r);
            }
            for(; x<t_datos.getRowCount(); x++)
            {
                if(t_datos.getValueAt(x, 9).toString().indexOf(t_busca.getText()) != -1)
                {
                    t_datos.setRowSelectionInterval(x, x);
                    t_datos.setColumnSelectionInterval(9, 9);
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
                System.out.println(ruta);
                File archivoXLS = new File(ruta+".xls");
                try
                {
                    if(archivoXLS.exists()) 
                        archivoXLS.delete();
                    archivoXLS.createNewFile();
                    Workbook libro = new HSSFWorkbook();
                    FileOutputStream archivo = new FileOutputStream(archivoXLS);
                    Sheet hoja = libro.createSheet("Autorizacion");
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

    private void b_pdfhActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_pdfhActionPerformed
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
            reporte.Abrir(PageSize.LETTER.rotate(), "Valuación", "reportes/"+ord.getIdOrden()+"/"+valor+"-autorizacion.pdf");
            Font font = new Font(Font.FontFamily.HELVETICA, 6, Font.BOLD);
            BaseColor contenido=BaseColor.WHITE;
            int centro=Element.ALIGN_CENTER;
            int izquierda=Element.ALIGN_LEFT;
            int derecha=Element.ALIGN_RIGHT;
            float tam[]=new float[]{15,15,50,150,15,15, 15,15, 15,15,15, 15,15,15,100};
            PdfPTable tabla=reporte.crearTabla(15, tam, 100, Element.ALIGN_LEFT);
            
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
                }
                
                int ren=0;
                if(cuentas.size()>0)
                {
                    DecimalFormat formatoPorcentaje = new DecimalFormat("#,##0.00");
                    formatoPorcentaje.setMinimumFractionDigits(2);
                    for(int i=0; i<cuentas.size(); i++)
                    {
                        Partida Part = (Partida) cuentas.get(i);
                        tabla.addCell(reporte.celda(""+Part.getIdEvaluacion(), font, contenido, izquierda, 0,1,Rectangle.RECTANGLE));
                        tabla.addCell(reporte.celda(""+Part.getSubPartida(), font, contenido, izquierda, 0,1,Rectangle.RECTANGLE));
                        
                        if(Part.isEspEle()==true)
                                tabla.addCell(reporte.celda("Electricidad", font, contenido, izquierda, 0,0,Rectangle.RECTANGLE));
                            else
                            {
                                if(Part.isEspMec()==true)
                                    tabla.addCell(reporte.celda("Mecanica", font, contenido, izquierda, 0,0,Rectangle.RECTANGLE));
                                else
                                {
                                    if(Part.isEspSus()==true)
                                        tabla.addCell(reporte.celda("Suspension", font, contenido, izquierda, 0,0,Rectangle.RECTANGLE));
                                    else
                                    {
                                        if(Part.isEspHoj()==true)
                                            tabla.addCell(reporte.celda("Hojalateria", font, contenido, izquierda, 0,0,Rectangle.RECTANGLE));
                                        else
                                        {
                                            tabla.addCell(reporte.celda(" ", font, contenido, izquierda, 0,0,Rectangle.RECTANGLE));
                                        }
                                    }
                                }
                            }
                            
                            tabla.addCell(reporte.celda(Part.getCatalogo().getNombre(), font, contenido, izquierda, 0,0,Rectangle.RECTANGLE));

                            tabla.addCell(reporte.celda(""+Part.getCant(), font, contenido, izquierda, 0,1,Rectangle.RECTANGLE));
                            tabla.addCell(reporte.celda(Part.getMed(), font, contenido, izquierda, 0,1,Rectangle.RECTANGLE));

                            if(Part.getIntDesm()>-1)
                                tabla.addCell(reporte.celda(formatoPorcentaje.format(Part.getIntDesm()), font, contenido, izquierda, 0,1,Rectangle.RECTANGLE));
                            else
                                tabla.addCell(reporte.celda("", font, contenido, izquierda, 0,1,Rectangle.RECTANGLE));

                            if(Part.getIntCamb()>-1)
                                tabla.addCell(reporte.celda(formatoPorcentaje.format(Part.getIntCamb()), font, contenido, izquierda, 0,1,Rectangle.RECTANGLE));
                            else
                                tabla.addCell(reporte.celda(" ", font, contenido, izquierda, 0,1,Rectangle.RECTANGLE));

                            if(Part.getIntRepMin()>-1)
                                tabla.addCell(reporte.celda(formatoPorcentaje.format(Part.getIntRepMin()), font, contenido, izquierda, 0,1,Rectangle.RECTANGLE));
                            else
                                tabla.addCell(reporte.celda(" ", font, contenido, izquierda, 0,1,Rectangle.RECTANGLE));

                            if(Part.getIntRepMed()>-1)
                                tabla.addCell(reporte.celda(formatoPorcentaje.format(Part.getIntRepMed()), font, contenido, izquierda, 0,1,Rectangle.RECTANGLE));
                            else
                                tabla.addCell(reporte.celda(" ", font, contenido, izquierda, 0,1,Rectangle.RECTANGLE));

                            if(Part.getIntRepMax()>-1)
                                tabla.addCell(reporte.celda(formatoPorcentaje.format(Part.getIntRepMax()), font, contenido, izquierda, 0,1,Rectangle.RECTANGLE));
                            else
                                tabla.addCell(reporte.celda(" ", font, contenido, izquierda, 0,1,Rectangle.RECTANGLE));

                            if(Part.getIntPinMin()>-1)
                                tabla.addCell(reporte.celda(formatoPorcentaje.format(Part.getIntPinMin()), font, contenido, izquierda, 0,1,Rectangle.RECTANGLE));
                            else
                                tabla.addCell(reporte.celda(" ", font, contenido, izquierda, 0,1,Rectangle.RECTANGLE));

                            if(Part.getIntPinMed()>-1)
                                tabla.addCell(reporte.celda(formatoPorcentaje.format(Part.getIntPinMed()), font, contenido, izquierda, 0,1,Rectangle.RECTANGLE));
                            else
                                tabla.addCell(reporte.celda(" ", font, contenido, izquierda, 0,1,Rectangle.RECTANGLE));

                            if(Part.getIntPinMax()>-1)
                                tabla.addCell(reporte.celda(formatoPorcentaje.format(Part.getIntPinMax()), font, contenido, izquierda, 0,1,Rectangle.RECTANGLE));
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
                            }*/
                            ren++;
                    }
                    
                }
                session.beginTransaction().rollback();
            tabla.setHeaderRows(3);
            reporte.agregaObjeto(tabla);
            reporte.cerrar();
            reporte.visualizar("reportes/"+ord.getIdOrden()+"/"+valor+"-autorizacion.pdf");
            
        }catch(Exception e)
        {
            System.out.println(e);
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "No se pudo realizar el reporte");
        }
        if(session!=null)
            if(session.isOpen())
                session.close();
    }//GEN-LAST:event_b_pdfhActionPerformed

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
            buscaCuentas(-1, -1);
            if(x>=t_datos.getRowCount())
            {
                x=0;
                java.awt.Rectangle r = t_datos.getCellRect( x, 3, true);
                t_datos.scrollRectToVisible(r);
            }
            for(; x<t_datos.getRowCount(); x++)
            {
                if(t_datos.getValueAt(x, 9).toString().indexOf(t_busca.getText()) != -1)
                {
                    t_datos.setRowSelectionInterval(x, x);
                    t_datos.setColumnSelectionInterval(9, 9);
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
                if(session.isOpen())
                    session.close();
        }*/
    }//GEN-LAST:event_t_datosMouseClicked

    private void b_pdfxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_pdfxActionPerformed
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
            reporte.Abrir(PageSize.LETTER.rotate(), "Valuación", "reportes/"+ord.getIdOrden()+"/"+valor+"-autorizacion.pdf");
            Font font = new Font(Font.FontFamily.HELVETICA, 6, Font.BOLD);
            BaseColor contenido=BaseColor.WHITE;
            int centro=Element.ALIGN_CENTER;
            int izquierda=Element.ALIGN_LEFT;
            int derecha=Element.ALIGN_RIGHT;
            float tam[]=new float[]{15,15,50,150,15,15, 12,12, 12,12,12, 12,12,12,100};
            PdfPTable tabla=reporte.crearTabla(15, tam, 100, Element.ALIGN_LEFT);
            
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
                }
                
                int ren=0;
                if(cuentas.size()>0)
                {
                    for(int i=0; i<cuentas.size(); i++)
                    {
                        Partida Part = (Partida) cuentas.get(i);
                        /*if(Part.isRefCoti())
                        {*/
                            tabla.addCell(reporte.celda(""+Part.getIdEvaluacion(), font, contenido, izquierda, 0,1,Rectangle.RECTANGLE));
                            tabla.addCell(reporte.celda(""+Part.getSubPartida(), font, contenido, izquierda, 0,1,Rectangle.RECTANGLE));
                            if(Part.isEspEle()==true)
                                tabla.addCell(reporte.celda("Electricidad", font, contenido, izquierda, 0,0,Rectangle.RECTANGLE));
                            else
                            {
                                if(Part.isEspMec()==true)
                                    tabla.addCell(reporte.celda("Mecanica", font, contenido, izquierda, 0,0,Rectangle.RECTANGLE));
                                else
                                {
                                    if(Part.isEspSus()==true)
                                        tabla.addCell(reporte.celda("Suspension", font, contenido, izquierda, 0,0,Rectangle.RECTANGLE));
                                    else
                                    {
                                        if(Part.isEspHoj()==true)
                                            tabla.addCell(reporte.celda("Hojalateria", font, contenido, izquierda, 0,0,Rectangle.RECTANGLE));
                                        else
                                        {
                                            tabla.addCell(reporte.celda(" ", font, contenido, izquierda, 0,0,Rectangle.RECTANGLE));
                                        }
                                    }
                                }
                            }
                            tabla.addCell(reporte.celda(Part.getCatalogo().getNombre(), font, contenido, izquierda, 0,0,Rectangle.RECTANGLE));
                            
                            tabla.addCell(reporte.celda(""+Part.getCant(), font, contenido, izquierda, 0,1,Rectangle.RECTANGLE));
                            tabla.addCell(reporte.celda(Part.getMed(), font, contenido, izquierda, 0,1,Rectangle.RECTANGLE));
                            
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
                            }*/
                            ren++;
                        //}
                    }
                    
                }
                session.beginTransaction().rollback();
            tabla.setHeaderRows(3);
            reporte.agregaObjeto(tabla);
            reporte.cerrar();
            reporte.visualizar("reportes/"+ord.getIdOrden()+"/"+valor+"-autorizacion.pdf");
            
        }catch(Exception e)
        {
            System.out.println(e);
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "No se pudo realizar el reporte");
        }
        if(session!=null)
            if(session.isOpen())
                session.close();
    }//GEN-LAST:event_b_pdfxActionPerformed

    private void t_datosKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_datosKeyPressed
        // TODO add your handling code here:
        int code = evt.getKeyCode();
        if(code == KeyEvent.VK_ENTER)
        {
            if(t_datos.isCellEditable(t_datos.getSelectedRow(), t_datos.getSelectedColumn())==true)
            {
                //t_datos.getInputMap(t_datos.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER,0,false), "editColumnCell");
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


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton b_busca;
    private javax.swing.JButton b_exel;
    private javax.swing.JButton b_pdfh;
    private javax.swing.JButton b_pdfx;
    private javax.swing.JButton b_valuacion;
    private javax.swing.JComboBox c_filtro;
    private javax.swing.JTextField instruccion;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JComboBox medida;
    private javax.swing.JComboBox numeros;
    private javax.swing.JScrollPane scroll;
    private javax.swing.JTextField t_busca;
    private javax.swing.JTable t_datos;
    private javax.swing.JTextField t_numero;
    // End of variables declaration//GEN-END:variables

      
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

    private void buscaCuentas( int x, int y)
    {
        if(orden!=null)
        {
            Session session = HibernateUtil.getSessionFactory().openSession();
            try
            {
                session.beginTransaction().begin();
                ord = (Orden)session.get(Orden.class, Integer.parseInt(orden));
                Partida[] cuentas = (Partida[])session.createCriteria(Partida.class).add(Restrictions.eq("ordenByIdOrden.idOrden", ord.getIdOrden())).add(Restrictions.eq("autorizadoValuacion", true)).addOrder(Order.asc("idEvaluacion")).addOrder(Order.asc("subPartida")).list().toArray(new Partida[0]);
                if(cuentas.length>0)
                {
                    model=new MyModel(cuentas.length, columnas);
                    t_datos.setModel(model);
                    for(int i=0; i<cuentas.length; i++)
                    {
                        model.setValueAt(cuentas[i].getIdEvaluacion(), i, 0);
                        model.setCeldaEditable(i, 0, false);
                        model.setValueAt(cuentas[i].getSubPartida(), i, 1);
                        model.setCeldaEditable(i, 1, false);
                        model.setValueAt(cuentas[i].isEspHoj(), i, 2);
                        model.setCeldaEditable(i, 2, false);
                        model.setValueAt(cuentas[i].isEspMec(), i, 3);
                        model.setCeldaEditable(i, 3, false);
                        model.setValueAt(cuentas[i].isEspSus(), i, 4);
                        model.setCeldaEditable(i, 4, false);
                        model.setValueAt(cuentas[i].isEspEle(), i, 5);
                        model.setCeldaEditable(i, 5, false);
                        
                        model.setValueAt(cuentas[i].getCant(), i, 6);
                        model.setCeldaEditable(i, 6, false);
                        model.setValueAt(cuentas[i].getCantidadAut(), i, 7);
                        model.setValueAt(cuentas[i].getMed(), i, 8);
                        model.setCeldaEditable(i, 8, false);
                        model.setValueAt(cuentas[i].getCatalogo().getNombre(), i, 9);
                        model.setCeldaEditable(i, 9, false);
                        model.setValueAt(cuentas[i].getCatalogo().getIdCatalogo(), i, 10);
                        model.setCeldaEditable(i, 10, false);
                        try{
                        model.setValueAt(cuentas[i].getEjemplar().getIdParte(), i, 11);
                        }catch(Exception e){model.setValueAt("", i, 11);}
                        model.setCeldaEditable(i, 11, false);
                        
                        if(cuentas[i].getIntDesm()==-1)
                        {
                            model.setValueAt(false, i, 12);
                            model.setValueAt(null, i, 21);
                            model.setCeldaEditable(i, 21, false);
                        }
                        else
                        {
                            model.setValueAt(true, i, 12);
                            model.setValueAt(cuentas[i].getIntDesm(), i, 21);
                        }
                        model.setCeldaEditable(i, 12, false);
                        
                        
                        if(cuentas[i].getIntCamb()==-1)
                        {
                            model.setValueAt(false, i, 13);
                            model.setValueAt(null, i, 22);
                            model.setCeldaEditable(i, 22, false);
                        }
                        else
                        {
                            model.setValueAt(true, i, 13);
                            model.setValueAt(cuentas[i].getIntCamb(), i, 22);
                        }
                        model.setCeldaEditable(i, 13, false);
                        
                        
                        if(cuentas[i].getIntRepMin()==-1)
                        {
                            model.setValueAt(false, i, 14);
                            model.setValueAt(null, i, 23);
                            model.setCeldaEditable(i, 23, false);
                        }
                        else
                        {
                            model.setValueAt(true, i, 14);
                            model.setValueAt(cuentas[i].getIntRepMin(), i, 23);
                        }
                        model.setCeldaEditable(i, 14, false);
                        
                        
                        if(cuentas[i].getIntRepMed()==-1)
                        {
                            model.setValueAt(false, i, 15);
                            model.setValueAt(null, i, 24);
                            model.setCeldaEditable(i, 24, false);
                        }
                        else
                        {
                            model.setValueAt(true, i, 15);
                            model.setValueAt(cuentas[i].getIntRepMed(), i, 24);
                        }
                        model.setCeldaEditable(i, 15, false);
                        
                        
                        if(cuentas[i].getIntRepMax()==-1)
                        {
                            model.setValueAt(false, i, 16);
                            model.setValueAt(null, i, 25);
                            model.setCeldaEditable(i, 25, false);
                        }
                        else
                        {
                            model.setValueAt(true, i, 16);
                            model.setValueAt(cuentas[i].getIntRepMax(), i, 25);
                        }
                        model.setCeldaEditable(i, 16, false);
                        
                        
                        if(cuentas[i].getIntPinMin()==-1)
                        {
                            model.setValueAt(false, i, 17);
                            model.setValueAt(null, i, 26);
                            model.setCeldaEditable(i, 26, false);
                        }
                        else
                        {
                            model.setValueAt(true, i, 17);
                            model.setValueAt(cuentas[i].getIntPinMin(), i, 26);
                        }
                        model.setCeldaEditable(i, 17, false);
                        
                        
                        if(cuentas[i].getIntPinMed()==-1)
                        {
                            model.setValueAt(false, i, 18);
                            model.setValueAt(null, i, 27);
                            model.setCeldaEditable(i, 27, false);
                        }
                        else
                        {
                            model.setValueAt(true, i, 18);
                            model.setValueAt(cuentas[i].getIntPinMed(), i, 27);
                        }
                        model.setCeldaEditable(i, 18, false);
                        
                        
                        if(cuentas[i].getIntPinMax()==-1)
                        {
                            model.setValueAt(false, i, 19);
                            model.setValueAt(null, i, 28);
                            model.setCeldaEditable(i, 28, false);
                        }
                        else
                        {
                            model.setValueAt(true, i, 19);
                            model.setValueAt(cuentas[i].getIntPinMax(), i, 28);
                        }
                        model.setCeldaEditable(i, 19, false);
                        
                        
                        if(cuentas[i].getInstruccion()!=null)
                            model.setValueAt(cuentas[i].getInstruccion(), i, 20);
                        else
                            model.setValueAt("", i, 20);
                        model.setCeldaEditable(i, 20, false);
                        
                        //*********
                        
                        model.setValueAt(cuentas[i].getTipo(), i, 29);
                        model.setCeldaEditable(i, 29, false);
                        
                        if(cuentas[i].getOrdenByEnlazada()!=null)
                            model.setValueAt(cuentas[i].getOrdenByEnlazada().getIdOrden(), i, 30);
                        else
                            model.setValueAt("", i, 30);
                        model.setCeldaEditable(i, 30, false);
                        
                        model.setValueAt(cuentas[i].isRefCoti(), i, 31);
                        model.setCeldaEditable(i, 31, false);
                        
                        model.setValueAt(cuentas[i].isAutorizado(), i, 32);
                    }
                    
                    //**********cargamos las enlazadas
                    //for(int i=0; i<enlazadas.length; i++)
                    //{
                        //model.setValueAt(enlazadas[i].getIdEvaluacion(), cuentas.length+i, 0);
                    //}
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
                if(session.isOpen())
                    session.close();
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
            if((x>1 && x<6  ) || (x>11 && x<20) || (x>20 && x<29) || x==29 || x==32 || x==31)
                t_datos.getColumnModel().getColumn(x).setHeaderRenderer(textVertical);
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
    
    public void visualiza(boolean t_busca, boolean b_busca, boolean filtro, boolean pdfx, boolean pdfh, boolean exel, boolean tabla)
    {        
        this.t_busca.setEnabled(t_busca);
        this.b_busca.setEnabled(b_busca);
        
        this.c_filtro.setEnabled(filtro);
        this.b_pdfx.setEnabled(pdfx);
        this.b_pdfh.setVisible(pdfh);
        this.b_exel.setEnabled(exel);
        
        this.t_datos.setEnabled(tabla);
        if(tabla==false)
        {
            for(int x=0; x<t_datos.getColumnCount(); x++)
            {
                model.setColumnaEditable(x, tabla);
            }
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
            reporte.contenido.roundRectangle(160, 515, 210, 45, 5);
            reporte.contenido.roundRectangle(380, 515, 375, 45, 5);
            reporte.contenido.roundRectangle(35, 490, 720, 20, 5);

            Configuracion con= (Configuracion)session.get(Configuracion.class, 1);
            reporte.inicioTexto();
            reporte.contenido.setFontAndSize(bf, 14);
            reporte.contenido.setColorFill(BaseColor.BLACK);
            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, con.getEmpresa(), 160, 580, 0);
            reporte.contenido.setFontAndSize(bf, 8);
            reporte.contenido.setColorFill(BaseColor.BLACK);
            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Autorizacion de Operaciones ("+this.c_filtro.getSelectedItem().toString()+")", 160, 570, 0);
            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_RIGHT, "Fecha:"+new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date()), 760, 580, 0);

            ord = (Orden)session.get(Orden.class, Integer.parseInt(orden)); 
            Foto[] fotos = (Foto[]) ord.getFotos().toArray(new Foto[0]);
            for(int k=0;k<fotos.length-1;k++) 
            {
                for(int f=0;f<(fotos.length-1)-k;f++) 
                {
                    if (fotos[f].getFecha().after(fotos[f+1].getFecha())==true) 
                    {
                        Foto aux;
                        aux=fotos[f];
                        fotos[f]=fotos[f+1];
                        fotos[f+1]=aux;
                    }
                }
            }
            if(fotos.length>0)
                reporte.agregaObjeto(reporte.crearImagen("ordenes/"+ord.getIdOrden()+"/"+fotos[0].getDescripcion(), 0, -60, 120, 80, 0));
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
        
            tabla.addCell(reporte.celda("N°", font, cabecera, centro, 0, 3, Rectangle.RECTANGLE));
            tabla.addCell(reporte.celda("#", font, cabecera, centro, 0, 3,Rectangle.RECTANGLE));
            tabla.addCell(reporte.celda("Operación", font, cabecera, centro, 0,3, Rectangle.RECTANGLE));
            tabla.addCell(reporte.celda("Descripción", font, cabecera, centro, 0, 3,Rectangle.RECTANGLE));
            tabla.addCell(reporte.celda("Cant", font, cabecera, centro, 0, 3, Rectangle.RECTANGLE));
            tabla.addCell(reporte.celda("Med", font, cabecera, centro, 0, 3,Rectangle.RECTANGLE));
            tabla.addCell(reporte.celda("Instrucción Final", font, cabecera, centro, 8,1, Rectangle.RECTANGLE));
            tabla.addCell(reporte.celda("Descripcion", font, cabecera, centro, 0, 3, Rectangle.RECTANGLE));
            
            tabla.addCell(reporte.celda("D/M", font, cabecera, centro, 0,2, Rectangle.RECTANGLE));
            tabla.addCell(reporte.celda("Cam", font, cabecera, centro, 0,2, Rectangle.RECTANGLE));
            
            tabla.addCell(reporte.celda("Reparar", font, cabecera, centro, 3,1, Rectangle.RECTANGLE));
            tabla.addCell(reporte.celda("Pintar", font, cabecera, centro, 3,1, Rectangle.RECTANGLE));
            
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
                session.close();
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
                    
                    java.lang.Integer.class,
                    java.lang.Integer.class,
                    java.lang.String.class,
                    java.lang.String.class,
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
                    
                    java.lang.String.class,
                    
                    java.lang.Double.class,
                    java.lang.Double.class,
                    java.lang.Double.class,
                    java.lang.Double.class,
                    java.lang.Double.class,
                    java.lang.Double.class,
                    java.lang.Double.class,
                    java.lang.Double.class,

                    java.lang.String.class,
                    java.lang.String.class,
                    java.lang.Boolean.class,
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
                                        ord=(Orden)session.get(Orden.class, Integer.parseInt(orden));
                                        if(user.getEditaCantAut()==true)
                                        {
                                            if(ord.getRValuacionCierre()==null)
                                            {
                                                if(value.toString().compareTo("")!=0 && Double.parseDouble(value.toString())>=0) 
                                                {
                                                     Partida part=(Partida) session.createCriteria(Partida.class).add(Restrictions.eq("ordenByIdOrden.idOrden", Integer.parseInt(orden))).add(Restrictions.eq("idEvaluacion", Integer.parseInt(t_datos.getValueAt(t_datos.getSelectedRow(), 0).toString()))).add(Restrictions.eq("subPartida", Integer.parseInt(t_datos.getValueAt(t_datos.getSelectedRow(), 1).toString()))).setMaxResults(1).uniqueResult();
                                                     if(part!=null)
                                                     {
                                                         part.setCantidadAut(Double.parseDouble(value.toString()));
                                                         session.update(part);
                                                         session.getTransaction().commit();
                                                         vector.setElementAt(value, col);
                                                         this.dataVector.setElementAt(vector, row);
                                                         fireTableCellUpdated(row, col);
                                                         if(session.isOpen()==true)
                                                             session.close();
                                                         buscaCuentas(row, col);
                                                     }
                                                     else
                                                     {
                                                         JOptionPane.showMessageDialog(null, "La partida ya no existe");
                                                         buscaCuentas(-1,-1);
                                                     }
                                                }
                                                else
                                                {
                                                    buscaCuentas(row, col);
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
                                        System.out.println(e);
                                        JOptionPane.showMessageDialog(null, "Error al actualizar los datos"); 
                                    }
                                    if(session!=null)
                                        if(session.isOpen()==true)
                                            session.close();
                                }
                                break;
                                
                            case 21:
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
                                            ord=(Orden)session.get(Orden.class, Integer.parseInt(orden));
                                            if(user.getEditaHoras()==true)
                                            {
                                                if(ord.getRValuacionCierre()==null)
                                                {
                                                    if(value.toString().compareTo("")!=0 && Double.parseDouble(value.toString())>=0) 
                                                    {
                                                         Partida part=(Partida) session.createCriteria(Partida.class).add(Restrictions.eq("ordenByIdOrden.idOrden", Integer.parseInt(orden))).add(Restrictions.eq("idEvaluacion", Integer.parseInt(t_datos.getValueAt(t_datos.getSelectedRow(), 0).toString()))).add(Restrictions.eq("subPartida", Integer.parseInt(t_datos.getValueAt(t_datos.getSelectedRow(), 1).toString()))).setMaxResults(1).uniqueResult();
                                                         if(part!=null)
                                                         {
                                                             part.setIntDesm(Double.parseDouble(value.toString()));
                                                             session.update(part);
                                                             session.getTransaction().commit();
                                                             vector.setElementAt(value, col);
                                                             this.dataVector.setElementAt(vector, row);
                                                             fireTableCellUpdated(row, col);
                                                             if(session.isOpen()==true)
                                                                 session.close();
                                                             buscaCuentas(row, col);
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
                                                        buscaCuentas(row, col);
                                                    }
                                                }
                                                else
                                                {
                                                    JOptionPane.showMessageDialog(null, "La valuacion esta cerrada"); 
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
                                            System.out.println(e);
                                            JOptionPane.showMessageDialog(null, "Error al actualizar los datos"); 
                                        }
                                        if(session!=null)
                                            if(session.isOpen()==true)
                                                session.close();
                                    }
                                break;
                                
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
                                            session.beginTransaction().begin();
                                            user = (Usuario)session.get(Usuario.class, user.getIdUsuario());
                                            ord=(Orden)session.get(Orden.class, Integer.parseInt(orden));
                                            if(user.getEditaHoras()==true)
                                            {
                                                if(ord.getRValuacionCierre()==null)
                                                {
                                                    if(value.toString().compareTo("")!=0 && Double.parseDouble(value.toString())>=0) 
                                                    {
                                                         Partida part=(Partida) session.createCriteria(Partida.class).add(Restrictions.eq("ordenByIdOrden.idOrden", Integer.parseInt(orden))).add(Restrictions.eq("idEvaluacion", Integer.parseInt(t_datos.getValueAt(t_datos.getSelectedRow(), 0).toString()))).add(Restrictions.eq("subPartida", Integer.parseInt(t_datos.getValueAt(t_datos.getSelectedRow(), 1).toString()))).setMaxResults(1).uniqueResult();
                                                         if(part!=null)
                                                         {
                                                             part.setIntCamb(Double.parseDouble(value.toString()));
                                                             session.update(part);
                                                             session.getTransaction().commit();
                                                             vector.setElementAt(value, col);
                                                             this.dataVector.setElementAt(vector, row);
                                                             fireTableCellUpdated(row, col);
                                                             if(session.isOpen()==true)
                                                                 session.close();
                                                             buscaCuentas(row, col);
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
                                                        buscaCuentas(row, col);
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
                                            System.out.println(e);
                                            JOptionPane.showMessageDialog(null, "Error al actualizar los datos"); 
                                        }
                                        if(session!=null)
                                            if(session.isOpen()==true)
                                                session.close();
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
                                        Session session = HibernateUtil.getSessionFactory().openSession();
                                        try
                                        {
                                            session.beginTransaction().begin();
                                            user = (Usuario)session.get(Usuario.class, user.getIdUsuario());
                                            ord=(Orden)session.get(Orden.class, Integer.parseInt(orden));
                                            if(user.getEditaHoras()==true)
                                            {
                                                if(ord.getRValuacionCierre()==null)
                                                {
                                                    if(value.toString().compareTo("")!=0 && Double.parseDouble(value.toString())>=0) 
                                                    {
                                                         Partida part=(Partida) session.createCriteria(Partida.class).add(Restrictions.eq("ordenByIdOrden.idOrden", Integer.parseInt(orden))).add(Restrictions.eq("idEvaluacion", Integer.parseInt(t_datos.getValueAt(t_datos.getSelectedRow(), 0).toString()))).add(Restrictions.eq("subPartida", Integer.parseInt(t_datos.getValueAt(t_datos.getSelectedRow(), 1).toString()))).setMaxResults(1).uniqueResult();
                                                         if(part!=null)
                                                         {
                                                             part.setIntRepMin(Double.parseDouble(value.toString()));
                                                             session.update(part);
                                                             session.getTransaction().commit();
                                                             vector.setElementAt(value, col);
                                                             this.dataVector.setElementAt(vector, row);
                                                             fireTableCellUpdated(row, col);
                                                             if(session.isOpen()==true)
                                                                 session.close();
                                                             buscaCuentas(row, col);
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
                                                        buscaCuentas(row, col);
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
                                            System.out.println(e);
                                            JOptionPane.showMessageDialog(null, "Error al actualizar los datos"); 
                                        }
                                        if(session!=null)
                                            if(session.isOpen()==true)
                                                session.close();
                                    }
                                break;
                                
                            case 24:
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
                                            ord=(Orden)session.get(Orden.class, Integer.parseInt(orden));
                                            if(user.getEditaHoras()==true)
                                            {
                                                if(ord.getRValuacionCierre()==null)
                                                {
                                                    if(value.toString().compareTo("")!=0 && Double.parseDouble(value.toString())>=0) 
                                                    {
                                                         Partida part=(Partida) session.createCriteria(Partida.class).add(Restrictions.eq("ordenByIdOrden.idOrden", Integer.parseInt(orden))).add(Restrictions.eq("idEvaluacion", Integer.parseInt(t_datos.getValueAt(t_datos.getSelectedRow(), 0).toString()))).add(Restrictions.eq("subPartida", Integer.parseInt(t_datos.getValueAt(t_datos.getSelectedRow(), 1).toString()))).setMaxResults(1).uniqueResult();
                                                         if(part!=null)
                                                         {
                                                             part.setIntRepMed(Double.parseDouble(value.toString()));
                                                             session.update(part);
                                                             session.getTransaction().commit();
                                                             vector.setElementAt(value, col);
                                                             this.dataVector.setElementAt(vector, row);
                                                             fireTableCellUpdated(row, col);
                                                             if(session.isOpen()==true)
                                                                 session.close();
                                                             buscaCuentas(row, col);
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
                                                        buscaCuentas(row, col);
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
                                            System.out.println(e);
                                            JOptionPane.showMessageDialog(null, "Error al actualizar los datos"); 
                                        }
                                        if(session!=null)
                                            if(session.isOpen()==true)
                                                session.close();
                                    }
                                break;
                                
                            case 25:
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
                                            ord=(Orden)session.get(Orden.class, Integer.parseInt(orden));
                                            if(user.getEditaHoras()==true)
                                            {
                                                if(ord.getRValuacionCierre()==null)
                                                {
                                                    if(value.toString().compareTo("")!=0 && Double.parseDouble(value.toString())>=0) 
                                                    {
                                                         Partida part=(Partida) session.createCriteria(Partida.class).add(Restrictions.eq("ordenByIdOrden.idOrden", Integer.parseInt(orden))).add(Restrictions.eq("idEvaluacion", Integer.parseInt(t_datos.getValueAt(t_datos.getSelectedRow(), 0).toString()))).add(Restrictions.eq("subPartida", Integer.parseInt(t_datos.getValueAt(t_datos.getSelectedRow(), 1).toString()))).setMaxResults(1).uniqueResult();
                                                         if(part!=null)
                                                         {
                                                             part.setIntRepMax(Double.parseDouble(value.toString()));
                                                             session.update(part);
                                                             session.getTransaction().commit();
                                                             vector.setElementAt(value, col);
                                                             this.dataVector.setElementAt(vector, row);
                                                             fireTableCellUpdated(row, col);
                                                             if(session.isOpen()==true)
                                                                 session.close();
                                                             buscaCuentas(row, col);
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
                                                        buscaCuentas(row, col);
                                                    }
                                                }
                                                else
                                                {
                                                    JOptionPane.showMessageDialog(null, "La valuacion esta cerrada"); 
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
                                            System.out.println(e);
                                            JOptionPane.showMessageDialog(null, "Error al actualizar los datos"); 
                                        }
                                        if(session!=null)
                                            if(session.isOpen()==true)
                                                session.close();
                                    }
                                break;
                                
                            case 26:
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
                                            ord=(Orden)session.get(Orden.class, Integer.parseInt(orden));
                                            if(user.getEditaHoras()==true)
                                            {
                                                if(ord.getRValuacionCierre()==null)
                                                {
                                                    if(value.toString().compareTo("")!=0 && Double.parseDouble(value.toString())>=0) 
                                                    {
                                                         Partida part=(Partida) session.createCriteria(Partida.class).add(Restrictions.eq("ordenByIdOrden.idOrden", Integer.parseInt(orden))).add(Restrictions.eq("idEvaluacion", Integer.parseInt(t_datos.getValueAt(t_datos.getSelectedRow(), 0).toString()))).add(Restrictions.eq("subPartida", Integer.parseInt(t_datos.getValueAt(t_datos.getSelectedRow(), 1).toString()))).setMaxResults(1).uniqueResult();
                                                         if(part!=null)
                                                         {
                                                             part.setIntPinMin(Double.parseDouble(value.toString()));
                                                             session.update(part);
                                                             session.getTransaction().commit();
                                                             vector.setElementAt(value, col);
                                                             this.dataVector.setElementAt(vector, row);
                                                             fireTableCellUpdated(row, col);
                                                             if(session.isOpen()==true)
                                                                 session.close();
                                                             buscaCuentas(row, col);
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
                                                        buscaCuentas(row, col);
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
                                            System.out.println(e);
                                            JOptionPane.showMessageDialog(null, "Error al actualizar los datos"); 
                                        }
                                        if(session!=null)
                                            if(session.isOpen()==true)
                                                session.close();
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
                                        Session session = HibernateUtil.getSessionFactory().openSession();
                                        try
                                        {
                                            session.beginTransaction().begin();
                                            user = (Usuario)session.get(Usuario.class, user.getIdUsuario());
                                            ord=(Orden)session.get(Orden.class, Integer.parseInt(orden));
                                            if(user.getEditaHoras()==true)
                                            {
                                                if(ord.getRValuacionCierre()==null)
                                                {
                                                    if(value.toString().compareTo("")!=0 && Double.parseDouble(value.toString())>=0) 
                                                    {
                                                         Partida part=(Partida) session.createCriteria(Partida.class).add(Restrictions.eq("ordenByIdOrden.idOrden", Integer.parseInt(orden))).add(Restrictions.eq("idEvaluacion", Integer.parseInt(t_datos.getValueAt(t_datos.getSelectedRow(), 0).toString()))).add(Restrictions.eq("subPartida", Integer.parseInt(t_datos.getValueAt(t_datos.getSelectedRow(), 1).toString()))).setMaxResults(1).uniqueResult();
                                                         if(part!=null)
                                                         {
                                                             part.setIntPinMed(Double.parseDouble(value.toString()));
                                                             session.update(part);
                                                             session.getTransaction().commit();
                                                             vector.setElementAt(value, col);
                                                             this.dataVector.setElementAt(vector, row);
                                                             fireTableCellUpdated(row, col);
                                                             if(session.isOpen()==true)
                                                                 session.close();
                                                             buscaCuentas(row, col);
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
                                                        buscaCuentas(row, col);
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
                                            System.out.println(e);
                                            JOptionPane.showMessageDialog(null, "Error al actualizar los datos"); 
                                        }
                                        if(session!=null)
                                            if(session.isOpen())
                                                session.close();
                                    }
                                break;
                                
                            case 28:
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
                                            ord=(Orden)session.get(Orden.class, Integer.parseInt(orden));
                                            if(user.getEditaHoras()==true)
                                            {
                                                if(ord.getRValuacionCierre()==null)
                                                {
                                                    if(value.toString().compareTo("")!=0 && Double.parseDouble(value.toString())>=0) 
                                                    {
                                                         Partida part=(Partida) session.createCriteria(Partida.class).add(Restrictions.eq("ordenByIdOrden.idOrden", Integer.parseInt(orden))).add(Restrictions.eq("idEvaluacion", Integer.parseInt(t_datos.getValueAt(t_datos.getSelectedRow(), 0).toString()))).add(Restrictions.eq("subPartida", Integer.parseInt(t_datos.getValueAt(t_datos.getSelectedRow(), 1).toString()))).setMaxResults(1).uniqueResult();
                                                         if(part!=null)
                                                         {
                                                             part.setIntPinMax(Double.parseDouble(value.toString()));
                                                             session.update(part);
                                                             session.getTransaction().commit();
                                                             vector.setElementAt(value, col);
                                                             this.dataVector.setElementAt(vector, row);
                                                             fireTableCellUpdated(row, col);
                                                             if(session.isOpen()==true)
                                                                 session.close();
                                                             buscaCuentas(row, col);
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
                                                        buscaCuentas(row, col);
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
                                            System.out.println(e);
                                            JOptionPane.showMessageDialog(null, "Error al actualizar los datos"); 
                                        }
                                        if(session!=null)
                                            if(session.isOpen()==true)
                                                session.close();
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
                                    Session session = HibernateUtil.getSessionFactory().openSession();
                                    try
                                    {
                                        session.beginTransaction().begin();
                                        user = (Usuario)session.get(Usuario.class, user.getIdUsuario());
                                        ord=(Orden)session.get(Orden.class, Integer.parseInt(orden));
                                        if(user.getEditaAutorizaPartida()==true)
                                        {
                                            if(ord.getRValuacionCierre()==null)
                                            {
                                                try
                                                {
                                                    Partida part=(Partida) session.createCriteria(Partida.class).add(Restrictions.eq("ordenByIdOrden.idOrden", Integer.parseInt(orden))).add(Restrictions.eq("idEvaluacion", Integer.parseInt(t_datos.getValueAt(t_datos.getSelectedRow(), 0).toString()))).add(Restrictions.eq("subPartida", Integer.parseInt(t_datos.getValueAt(t_datos.getSelectedRow(), 1).toString()))).setMaxResults(1).uniqueResult();
                                                    if(part!=null)
                                                    {
                                                        if((boolean)value==true)
                                                            part.setAutorizado(true);
                                                        else
                                                            part.setAutorizado(false);
                                                        session.update(part);
                                                        session.getTransaction().commit();
                                                        vector.setElementAt(value, col);
                                                        this.dataVector.setElementAt(vector, row);
                                                        fireTableCellUpdated(row, col);
                                                        if(session.isOpen()==true)
                                                            session.close();
                                                        //buscaCuentas(row, col);
                                                    }
                                                    else
                                                    {
                                                        JOptionPane.showMessageDialog(null, "La partida ya no existe");
                                                        buscaCuentas(-1,-1);
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
                                            else
                                            {
                                                JOptionPane.showMessageDialog(null, "La valuación esta cerrada"); 
                                            }
                                        }
                                        else
                                        {
                                            JOptionPane.showMessageDialog(null, "Acceso denegado"); 
                                        }
                                    }catch(Exception e)
                                    {
                                        System.out.println(e);
                                    }
                                    if(session!=null)
                                        if(session.isOpen()==true)
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
}
