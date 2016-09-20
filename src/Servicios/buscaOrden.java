/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * acceso.java
 *
 * Created on 19/01/2012, 02:01:25 PM
 */
package Servicios;

import Integral.Render1;
import Integral.PDF;
import Hibernate.Util.HibernateUtil;
import Hibernate.entidades.Configuracion;
import java.util.List;
import java.util.Vector;
import javax.swing.InputMap;
import javax.swing.JOptionPane;
import Hibernate.entidades.Orden;
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
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileOutputStream;
import javax.swing.ImageIcon;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.hibernate.Criteria;


/**
 *
 * @author ISC_SALVADOR
 */
public class buscaOrden extends javax.swing.JDialog {

    public static final Orden RET_CANCEL =null;
    InputMap map = new InputMap();
    DefaultTableModel model;
    int op=0;
    private Usuario actor=null;
    String[] columnas = new String [] {"No orden", "Ingreso","Aseguradora", "Siniestro", "Poliza", "Reporte", "Inciso", "Fecha sin.", "Cliente", "Email", "Tel", "Tipo", "Marca", "Placas", "Motor", "Año", "Serie", "Economico", "Estatus"};
    
    /** Creates new form acceso */
    public buscaOrden(java.awt.Frame parent, boolean modal, Usuario User, int op) {
        super(parent, modal);
        actor=User;
        this.op=op;
        initComponents();
        getRootPane().setDefaultButton(jButton1);
        t_datos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        t_datos.setModel(ModeloTablaReporte(0, columnas));
        formatoTabla();
        buscaDato();
    }
    
    DefaultTableModel ModeloTablaReporte(int renglones, String columnas[])
        {
            model = new DefaultTableModel(new Object [renglones][11], columnas)
            {
                Class[] types = new Class [] {
                    java.lang.String.class, 
                    java.lang.String.class, 
                    java.lang.String.class, 
                    java.lang.String.class, 
                    java.lang.String.class, 
                    java.lang.String.class, 
                    java.lang.String.class, 
                    java.lang.String.class, 
                    java.lang.String.class, 
                    java.lang.String.class, 
                    java.lang.String.class, 
                    java.lang.String.class, 
                    java.lang.String.class, 
                    java.lang.String.class, 
                    java.lang.String.class, 
                    java.lang.String.class, 
                    java.lang.String.class, 
                    java.lang.String.class, 
                    java.lang.String.class
                };
                boolean[] canEdit = new boolean [] {
                    false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false
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

    
    private void doClose(Orden o) {
        returnStatus = o;
        setVisible(false);
        dispose();
    }
    
    public Orden getReturnStatus() {
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
        jLabel1 = new javax.swing.JLabel();
        c_filtro = new javax.swing.JComboBox();
        cb_todos = new javax.swing.JCheckBox();
        jScrollPane1 = new javax.swing.JScrollPane();
        t_datos = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(90, 66, 126), 1, true), "Busqueda de Ordenes", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Arial", 1, 12))); // NOI18N

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(90, 66, 126), 1, true), "Filtrar por:", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Arial", 1, 11))); // NOI18N

        t_busca.setBackground(new java.awt.Color(204, 255, 255));
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

        jLabel1.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel1.setText("Contiene:");

        c_filtro.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "No de orden", "No Aseguradora", "Nombre de Aseguradora", "No Siniestro", "No Poliza", "No Reporte", "No de Inciso", "Nombre del cliente", "Tipo", "Marca", "No motor", "No serie", "No Economico", "No placas", "Estatus", " " }));
        c_filtro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                c_filtroActionPerformed(evt);
            }
        });

        cb_todos.setText("Todos");
        cb_todos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cb_todosActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(c_filtro, javax.swing.GroupLayout.PREFERRED_SIZE, 239, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(t_busca, javax.swing.GroupLayout.PREFERRED_SIZE, 377, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cb_todos)
                .addContainerGap(159, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(t_busca, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1)
                    .addComponent(c_filtro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cb_todos))
                .addGap(0, 16, Short.MAX_VALUE))
        );

        t_datos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "No orden", "Aseguradora", "Siniestro", "Poliza", "Reporte", "Inciso", "Fecha sin.", "Cliente", "Email", "Tel", "Tipo", "Marca", "Placas", "Motor", "Año", "Serie", "Economico", "Estatus"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        t_datos.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
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
        jScrollPane1.setViewportView(t_datos);

        jButton1.setBackground(new java.awt.Color(2, 135, 242));
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setIcon(new ImageIcon("imagenes/seleccionar.png"));
        jButton1.setText("Seleccionar");
        jButton1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setBackground(new java.awt.Color(2, 135, 242));
        jButton2.setIcon(new ImageIcon("imagenes/exel.png"));
        jButton2.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setBackground(new java.awt.Color(2, 135, 242));
        jButton3.setIcon(new ImageIcon("imagenes/pdf.png"));
        jButton3.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton1)
                .addContainerGap())
            .addComponent(jScrollPane1)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 302, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
       if(t_datos.getRowCount()>0)
       {
           if(t_datos.getSelectedRow()>=0)
           {
                Session session = HibernateUtil.getSessionFactory().openSession();
                Query q = session.createQuery("SELECT ord from Orden ord where ord.idOrden="+t_datos.getValueAt(t_datos.getSelectedRow(), 0));
                List resultList = q.list();
                if(resultList.size()>0)
                {
                    Object o =resultList.get(0);
                    Orden ord = (Orden) o;
                    if(session!=null)
                        if(session.isOpen())
                            session.close();
                    doClose(ord);
                }
                else
                {
                    if(session!=null)
                        if(session.isOpen())
                            session.close();
                    doClose(null);
                }
           }
           else
               JOptionPane.showMessageDialog(null, "¡No hay una orden seleccionada!");
       }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void t_buscaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_buscaKeyReleased
        this.buscaDato();
    }//GEN-LAST:event_t_buscaKeyReleased

    private void c_filtroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_c_filtroActionPerformed
       this.buscaDato();
    }//GEN-LAST:event_c_filtroActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        File archivoXLS = new File("reportes/reporte.xls");
        try
        {
            if(archivoXLS.exists()) 
                archivoXLS.delete();
            archivoXLS.createNewFile();
            Workbook libro = new HSSFWorkbook();
            FileOutputStream archivo = new FileOutputStream(archivoXLS);
            Sheet hoja = libro.createSheet("Mi hoja de trabajo 1");
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
            JOptionPane.showMessageDialog(this, "No se pudo realizar el reporte si el archivo esta abierto");
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        Session session = HibernateUtil.getSessionFactory().openSession();
        try
        {
            BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.WINANSI, BaseFont.NOT_EMBEDDED);
            PDF reporte = new PDF();
            reporte.Abrir(PageSize.LEGAL.rotate(), "Consulta de ordenes", "reportes/consulta.pdf");
            reporte.agregaObjeto(reporte.crearImagen("imagenes/empresa300115.jpg", 00, -32, 17));
            reporte.contenido.setLineWidth(0.5f);
            reporte.contenido.setColorStroke(new GrayColor(0.2f));
            reporte.contenido.setColorFill(new GrayColor(0.9f));
            reporte.contenido.roundRectangle(30, 535, 945, 60, 5);
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
                reporte.contenido.showTextAligned(PdfContentByte.ALIGN_CENTER, "Consulta de Ordenes Filtrado por '"+this.c_filtro.getSelectedItem().toString()+"' ("+this.t_busca.getText()+")", 505, 537, 0);
            reporte.finTexto();
            reporte.agregaObjeto(new Paragraph(" "));
            reporte.agregaObjeto(new Paragraph(" "));
            reporte.agregaObjeto(new Paragraph(" "));
            float tam[]=new float[]{40,80, 150,50,50,50,50,65,180,100,80,130,130,50,80,30,80,50,90};
            Font font = new Font(Font.FontFamily.HELVETICA, 5, Font.BOLD);
            PdfPTable tabla=reporte.crearTabla(19, tam, 100, Element.ALIGN_LEFT);
            BaseColor cabecera=BaseColor.GRAY;
            BaseColor contenido=BaseColor.WHITE;
            int centro=Element.ALIGN_CENTER;
            int izquierda=Element.ALIGN_LEFT;
            int derecha=Element.ALIGN_RIGHT;
            tabla.addCell(reporte.celda("Orden", font, cabecera, centro, 0, 1,Rectangle.RECTANGLE));
            tabla.addCell(reporte.celda("Ingreso", font, cabecera, centro, 0, 1,Rectangle.RECTANGLE));
            tabla.addCell(reporte.celda("Aseguradora", font, cabecera, centro, 0,1, Rectangle.RECTANGLE));
            tabla.addCell(reporte.celda("Siniestro", font, cabecera, centro, 0,1, Rectangle.RECTANGLE));
            tabla.addCell(reporte.celda("Poliza", font, cabecera, centro, 0, 1,Rectangle.RECTANGLE));
            tabla.addCell(reporte.celda("Reporte", font, cabecera, centro, 0,1,Rectangle.RECTANGLE));
            tabla.addCell(reporte.celda("Insiso", font, cabecera, centro, 0, 1,Rectangle.RECTANGLE));
            tabla.addCell(reporte.celda("Fecha", font, cabecera, centro, 0, 1,Rectangle.RECTANGLE));
            tabla.addCell(reporte.celda("Cliente", font, cabecera, centro, 0, 1,Rectangle.RECTANGLE));
            tabla.addCell(reporte.celda("Email", font, cabecera, centro, 0, 1,Rectangle.RECTANGLE));
            tabla.addCell(reporte.celda("Tel.", font, cabecera, centro, 0, 1,Rectangle.RECTANGLE));
            tabla.addCell(reporte.celda("Tipo", font, cabecera, centro, 0, 1,Rectangle.RECTANGLE));
            tabla.addCell(reporte.celda("Marca", font, cabecera, centro, 0, 1,Rectangle.RECTANGLE));
            tabla.addCell(reporte.celda("Placas", font, cabecera, centro, 0, 1,Rectangle.RECTANGLE));
            tabla.addCell(reporte.celda("Motor", font, cabecera, centro, 0, 1,Rectangle.RECTANGLE));
            tabla.addCell(reporte.celda("Mod.", font, cabecera, centro, 0, 1,Rectangle.RECTANGLE));
            tabla.addCell(reporte.celda("Serie", font, cabecera, centro, 0, 1,Rectangle.RECTANGLE));
            tabla.addCell(reporte.celda("Econom.", font, cabecera, centro, 0, 1,Rectangle.RECTANGLE));
            tabla.addCell(reporte.celda("Estatus", font, cabecera, centro, 0, 1,Rectangle.RECTANGLE));
            //for de renflones
            for(int ren=0; ren<t_datos.getRowCount(); ren++)
            {
                for(int col=0; col<t_datos.getColumnCount(); col++)
                {
                    try
                    {
                        tabla.addCell(reporte.celda(t_datos.getValueAt(ren, col).toString(), font, contenido, izquierda, 0,1,Rectangle.RECTANGLE));
                    }catch(Exception e)
                    {
                        tabla.addCell(reporte.celda("", font, contenido, izquierda, 0,1,Rectangle.RECTANGLE));
                    }
                }
            }
            reporte.agregaObjeto(tabla);
            reporte.cerrar();
            reporte.visualizar("reportes/consulta.pdf");
        }catch(Exception e)
        {
            System.out.println(e);
            JOptionPane.showMessageDialog(this, "No se pudo realizar el reporte si el archivo esta abierto.");
        }
        if(session!=null)
            if(session.isOpen())
                session.close();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void t_datosKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_datosKeyPressed
        // TODO add your handling code here:
        int code = evt.getKeyCode();
        if(code == KeyEvent.VK_ENTER)
        {
            t_datos.getInputMap(t_datos.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER,0,false), "selectColumnCell");
            this.jButton1.requestFocus();
        }
    }//GEN-LAST:event_t_datosKeyPressed

    private void t_buscaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_t_buscaActionPerformed
        // TODO add your handling code here:
        t_datos.requestFocus();
        t_datos.getSelectionModel().setSelectionInterval(0,0);
    }//GEN-LAST:event_t_buscaActionPerformed

    private void t_datosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_t_datosMouseClicked
        // TODO add your handling code here:
        if (evt.getClickCount() == 2) {
            if(t_datos.getRowCount()>0)
           {
               if(t_datos.getSelectedRow()>=0)
               {
                    Session session = HibernateUtil.getSessionFactory().openSession();
                    Query q = session.createQuery("SELECT ord from Orden ord where ord.idOrden="+t_datos.getValueAt(t_datos.getSelectedRow(), 0));
                    List resultList = q.list();
                    if(resultList.size()>0)
                    {
                        Object o =resultList.get(0);
                        Orden ord = (Orden) o;
                        if(session!=null)
                            if(session.isOpen())
                                session.close();
                        doClose(ord);
                    }
                    else
                    {
                        if(session!=null)
                            if(session.isOpen())
                                session.close();
                        doClose(null);
                    }
               }
               else
                   JOptionPane.showMessageDialog(null, "¡No hay una orden seleccionada!");
           }
        }
    }//GEN-LAST:event_t_datosMouseClicked

    private void cb_todosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cb_todosActionPerformed
        // TODO add your handling code here:
        this.buscaDato();
    }//GEN-LAST:event_cb_todosActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox c_filtro;
    private javax.swing.JCheckBox cb_todos;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    public javax.swing.JTextField t_busca;
    private javax.swing.JTable t_datos;
    // End of variables declaration//GEN-END:variables

    
private void buscaDato()
{
    //if(t_busca.getText().compareTo("")!=0)
    //{
        String consulta="select id_orden, fecha_siniestro, compania.nombre as nom, siniestro, poliza, no_reporte, inciso, fecha, clientes.nombre, clientes.email, clientes.telefono, tipo_nombre, marca.marca_nombre, no_placas, no_motor, modelo, no_serie, no_economico, estatus_nombre \n" +
"from orden left join compania on compania.id_compania=orden.id_compania \n" +
"left join clientes on clientes.id_clientes=orden.id_cliente \n" +
"left join marca on marca.id_marca=orden.id_marca";

        String busqueda=c_filtro.getSelectedItem().toString();
        switch (busqueda)
        {
            case "No de orden":
                consulta+=" where orden.id_orden like '" + t_busca.getText() +"%' ";
                break;

            case "No Aseguradora":
                consulta+=" where compania.id_compania like '" + t_busca.getText() +"%'";
                break;

            case "Nombre de Aseguradora":
                consulta+=" where compania.nombre like '%"+t_busca.getText()+"%'";
                break;

            case "No Siniestro":
                consulta+=" where siniestro like '" + t_busca.getText() +"%'";
                break;

            case "No Poliza":
                consulta+=" where poliza like '" + t_busca.getText() +"%'";
                break;

            case "No Reporte":
                consulta+=" where no_reporte like '" + t_busca.getText() +"%'";
                break;

            case "No de Inciso":
                consulta+=" where inciso like '" + t_busca.getText() +"%'";
                break;

            case "Nombre del cliente":
                consulta+=" where clientes.nombre like '%"+t_busca.getText()+"%'";
                break;

            case "Tipo":
                consulta+=" where tipo_nombre like '%"+t_busca.getText()+"%'";
                break;

            case "Marca":
                consulta+=" where marca.marca_nombre like '%"+t_busca.getText()+"%'";
                break;

            case "No motor":
                consulta+=" where no_motor like '" + t_busca.getText() +"%'";
                break;

            case "No serie":
                consulta+=" where no_serie like '" + t_busca.getText() +"%'";
                break;

            case "No Economico":
                consulta+=" where no_economico like '" + t_busca.getText() +"%'";
                break;

            case "No placas":
                consulta+=" where no_placas like '" + t_busca.getText() +"%'";
                break;

            case "Estatus":
                consulta+=" where estatus_nombre like '%"+t_busca.getText()+"%'";
                break;
        }
        if(op==1)
            consulta+=" and no_factura is null";
        consulta+=" order by orden.id_orden desc";

        Session session = HibernateUtil.getSessionFactory().openSession();
        try
        {
            if(cb_todos.isSelected()==false)
                consulta+=" limit 200";
            Query q = session.createSQLQuery(consulta);
            q.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
            List resultList = q.list();
            model.setRowCount(0);
            for (Object o : resultList) 
            {
                java.util.HashMap ord=(java.util.HashMap)o;
                Object[] renglon=new Object[19];
                //Orden actor = (Orden) o;
                renglon[0]=ord.get("id_orden");
                renglon[1]=ord.get("fecha_siniestro");
                renglon[2]=ord.get("nom");
                renglon[3]=ord.get("siniestro");
                renglon[4]=ord.get("poliza");
                renglon[5]=ord.get("no_reporte");
                renglon[6]=ord.get("inciso");
                renglon[7]=ord.get("fecha");
                renglon[8]=ord.get("nombre");
                renglon[9]=ord.get("email");
                renglon[10]=ord.get("telefono");
                renglon[11]=ord.get("tipo_nombre");
                renglon[12]=ord.get("marca_nombre");
                renglon[13]=ord.get("no_placas");
                renglon[14]=ord.get("no_motor");
                renglon[15]=ord.get("modelo");
                renglon[16]=ord.get("no_serie");
                renglon[17]=ord.get("no_economico");
                renglon[18]=ord.get("estatus_nombre");
                model.addRow(renglon);
            }
            t_busca.requestFocus();
            resultList=null;
        }catch(Exception e)
        {
            System.out.println(e);
        }
        if(session!=null)
            if(session.isOpen())
                session.close();
    //}
    //formatoTabla();
}
    private Orden returnStatus = RET_CANCEL;
    
    public void tabla_tamaños()
    {
        TableColumnModel col_model = t_datos.getColumnModel();

        for (int i=0; i<t_datos.getColumnCount(); i++)
        {
  	      TableColumn column = col_model.getColumn(i);
              switch(i)
              {
                  case 0:
                      column.setPreferredWidth(80);
                      break;
                  case 1:
                      column.setPreferredWidth(90);
                      break;    
                  case 2:
                      column.setPreferredWidth(200);
                      break;
                  case 3:
                      column.setPreferredWidth(150);
                      break;
                  case 4:
                      column.setPreferredWidth(170);
                      break;
                  case 5:
                      column.setPreferredWidth(110);
                      break;
                  case 6:
                      column.setPreferredWidth(80);
                      break;
                  case 7:
                      column.setPreferredWidth(100);
                      break;
                  case 8:
                      column.setPreferredWidth(200);
                      break;
                  case 9:
                      column.setPreferredWidth(100);
                      break;
                  case 10:
                      column.setPreferredWidth(80);
                      break;
                  case 11:
                      column.setPreferredWidth(130);
                      break;
                  case 12:
                      column.setPreferredWidth(130);
                      break;
                  case 13:
                      column.setPreferredWidth(80);
                      break;
                  case 14:
                      column.setPreferredWidth(150);
                      break;
                  case 15:
                      column.setPreferredWidth(80);
                      break;
                  case 16:
                      column.setPreferredWidth(150);
                      break;
                  case 17:
                      column.setPreferredWidth(80);
                      break;
                  case 18:
                      column.setPreferredWidth(120);
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
    }
}
