/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Ejemplar;

import Hibernate.Util.HibernateUtil;
import Hibernate.entidades.Configuracion;
import Hibernate.entidades.CotConsumible;
import Integral.FormatoTabla;
import Integral.PDF;
import Integral.Render1;
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
import java.io.File;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author Sistemas
 */
public class MovEjemplar extends javax.swing.JDialog {

    String id_ejemplar;
    FormatoTabla formato;
    int configuracion=1;
    /**
     * Creates new form MovEjemplar
     */
    public MovEjemplar(java.awt.Frame parent, boolean modal, String id_ejemplar, int configuracion) {
        super(parent, modal);
        this.configuracion=configuracion;
        initComponents();
        formato = new FormatoTabla(); 
        this.id_ejemplar=id_ejemplar;
        formatoTabla();
        busca();
    }
    
    public void cabecera2(PDF reporte, BaseFont bf, PdfPTable tabla, String titulo1, int op)
   {
       Session session = HibernateUtil.getSessionFactory().openSession();
       try
       {
           reporte.contenido.setLineWidth(0.5f);
            reporte.contenido.setColorStroke(new GrayColor(0.2f));
            reporte.contenido.setColorFill(new GrayColor(0.9f));
            reporte.contenido.roundRectangle(35, 670, 540, 70, 5);

            Configuracion con= (Configuracion)session.get(Configuracion.class, configuracion);
            reporte.inicioTexto();
            reporte.contenido.setFontAndSize(bf, 15);
            reporte.contenido.setColorFill(BaseColor.BLACK);
            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, con.getEmpresa(), 45, 720, 0);
            
            
            reporte.contenido.setFontAndSize(bf, 10);
            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "NP: "+t_id.getText(), 45, 690, 0);
            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Descripcion: "+t_descripcion.getText(), 180, 690, 0);
            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Almacen: "+cb_almacen.getSelectedItem().toString(), 460, 750, 0);
            
            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Existencias: "+t_existencias.getText()+t_unidad.getText(), 45, 677, 0);
            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Maximo: "+t_maximo.getText()+t_unidad.getText(), 190, 677, 0);
            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Minimo: "+t_minimo.getText()+t_unidad.getText(), 310, 677, 0);
            
            if(t_pedido.getText().compareTo("")!=0){
                reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Ultima Compra", 470, 690, 0);
                reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, t_pedido.getText()+"["+t_fecha.getText()+"]", 460, 677, 0);
            }
            
            reporte.contenido.setFontAndSize(bf, 8);
            reporte.contenido.setColorFill(BaseColor.BLACK);
            String titulo=titulo1;
          
            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, titulo, 45, 705, 0);
            
            reporte.finTexto();
            //agregamos renglones vacios para dejar un espacio
            reporte.agregaObjeto(new Paragraph(" "));
            reporte.agregaObjeto(new Paragraph(" "));
            reporte.agregaObjeto(new Paragraph(" "));
            reporte.agregaObjeto(new Paragraph(" "));
            reporte.agregaObjeto(new Paragraph(" "));
            reporte.agregaObjeto(new Paragraph(" "));
            
            Font font = new Font(Font.FontFamily.HELVETICA, 8, Font.BOLD);
            
            BaseColor cabecera=BaseColor.GRAY;
            BaseColor contenido=BaseColor.WHITE;
            int centro=Element.ALIGN_CENTER;
           
            tabla.addCell(reporte.celda("Np", font, cabecera, centro, 0, 1, Rectangle.RECTANGLE));
            tabla.addCell(reporte.celda("OT", font, cabecera, centro, 0, 1, Rectangle.RECTANGLE));
            tabla.addCell(reporte.celda("OP", font, cabecera, centro, 0, 1, Rectangle.RECTANGLE));
            tabla.addCell(reporte.celda("Recibió", font, cabecera, centro, 0, 1, Rectangle.RECTANGLE));
            tabla.addCell(reporte.celda("Cantidad", font, cabecera, centro, 0, 1, Rectangle.RECTANGLE));
              
       }catch(Exception e)
       {
           System.out.println(e);
       }
       if(session!=null)
            if(session.isOpen())
                session.close();
    }
    
    void busca(){
        Session session = HibernateUtil.getSessionFactory().openSession();
        try{
            String almacen="existencias as ex", no_almacen="0";
            if(cb_almacen.getSelectedItem().toString().compareToIgnoreCase("ALMACEN 2")==0)
            {
                almacen="existencias2 as ex";
                no_almacen="1";
            }

            Query query = session.createSQLQuery("select id_Parte as id, comentario as nombre, "+almacen+", maximo, minimo, medida, "
                    + "(select partida_externa.id_pedido from partida_externa inner join pedido on partida_externa.id_pedido=pedido.id_pedido where id_parte=id and pedido.autorizo2 is not null order by id_pedido desc limit 1) as pedido, "
                    + "(select date_format(fecha_pedido, '%Y-%m-%d') from pedido where id_pedido=pedido) as fecha1 from ejemplar where id_Parte='"+id_ejemplar+"'");  
            query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
            ArrayList ejemplar=(ArrayList)query.list();
            java.util.HashMap map=(java.util.HashMap)ejemplar.get(0);
            t_id.setText(map.get("id").toString());
            t_descripcion.setText(map.get("nombre").toString());
            if(map.get("ex")!=null)
                t_existencias.setValue((double)map.get("ex"));
            else
                t_existencias.setValue(0.0d);
            if(map.get("maximo")!=null)
                t_maximo.setValue((double)map.get("maximo"));
            else
                t_maximo.setValue(0.0d);
            if(map.get("minimo")!=null)
                t_minimo.setValue((double)map.get("minimo"));
            else
                t_minimo.setValue(0.0d);
            if(map.get("medida")!=null)
                t_unidad.setText(map.get("medida").toString());
            else
                t_unidad.setText("N/A");
            if(map.get("pedido")!=null){
                t_pedido.setText(map.get("pedido").toString());
                t_fecha.setText(map.get("fecha1").toString());
            }
            else{
                t_pedido.setText("");
                t_fecha.setText("");
            }
            
            String consulta="select almacen.id_almacen as id, almacen.id_orden as ot, entrego, cantidad, if(almacen.tipo_movimiento=2, 'sal', 'dev')as operacion " +
                            "from movimiento left JOIN almacen on movimiento.id_almacen=almacen.id_almacen " +
                            "left join ejemplar on ejemplar.id_Parte=movimiento.id_Parte " +
                            "left join partida on movimiento.id_partida=partida.id_partida " +
                            "where (movimiento.id_Parte='"+t_id.getText()+"' or partida.id_parte='"+t_id.getText()+"') and operacion in(8, 5) and almacen=0 order by almacen.id_almacen;";
            /*String consulta="select almacen.id_almacen as id, almacen.id_orden as ot, entrego, cantidad " +
                    "from movimiento join almacen on movimiento.id_almacen=almacen.id_almacen join ejemplar on ejemplar.id_Parte=movimiento.id_Parte " +
                    "where movimiento.id_Parte='"+t_id.getText()+"' and operacion=8 and almacen="+no_almacen+" order by almacen.id_almacen;";*/
            query = session.createSQLQuery(consulta);
            query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
            ArrayList registros=(ArrayList)query.list();
            
            DefaultTableModel modelo=(DefaultTableModel)t_datos.getModel();
            modelo.setNumRows(0);
            for(int x=0; x<registros.size(); x++)
            {
                java.util.HashMap map1=(java.util.HashMap)registros.get(x);
                modelo.addRow(new Object[]{map1.get("id").toString(), map1.get("ot"), map1.get("operacion").toString(),map1.get("entrego").toString(), map1.get("cantidad").toString()});
            }
        }
        catch(Exception e){
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "No se pudo realizar el reporte si el archivo esta abierto.");
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        t_datos = new javax.swing.JTable();
        t_existencias = new javax.swing.JFormattedTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        t_maximo = new javax.swing.JFormattedTextField();
        t_minimo = new javax.swing.JFormattedTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        t_unidad = new javax.swing.JTextField();
        t_pedido = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        t_fecha = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        t_id = new javax.swing.JTextField();
        t_descripcion = new javax.swing.JTextField();
        cb_almacen = new javax.swing.JComboBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setBackground(new java.awt.Color(255, 255, 255));
        setResizable(false);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jButton1.setText("Imprimir");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        t_datos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Mov", "OT", "Op", "Operario", "Cant"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        t_datos.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(t_datos);

        t_existencias.setEditable(false);
        t_existencias.setBackground(new java.awt.Color(255, 255, 255));
        t_existencias.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.00"))));
        t_existencias.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        jLabel3.setText("Existencias");

        jLabel4.setText("Maximo");

        t_maximo.setEditable(false);
        t_maximo.setBackground(new java.awt.Color(255, 255, 255));
        t_maximo.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.00"))));
        t_maximo.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        t_minimo.setEditable(false);
        t_minimo.setBackground(new java.awt.Color(255, 255, 255));
        t_minimo.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.00"))));
        t_minimo.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        jLabel5.setText("Minimo");

        jLabel6.setText("U. Medida");

        t_unidad.setEditable(false);
        t_unidad.setBackground(new java.awt.Color(255, 255, 255));
        t_unidad.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        t_pedido.setEditable(false);
        t_pedido.setBackground(new java.awt.Color(255, 255, 255));

        jLabel7.setText("Ultima Compra");

        t_fecha.setEditable(false);
        t_fecha.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setText("NP:");

        t_id.setEditable(false);
        t_id.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(0, 0, 0)));

        t_descripcion.setEditable(false);
        t_descripcion.setBackground(new java.awt.Color(255, 255, 255));
        t_descripcion.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(0, 0, 0)));
        t_descripcion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                t_descripcionActionPerformed(evt);
            }
        });

        cb_almacen.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "ALMACEN 1", "ALMACEN 2" }));
        cb_almacen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cb_almacenActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButton1))
                    .addComponent(jScrollPane1)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(t_existencias, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(jLabel3)))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(t_maximo, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(22, 22, 22)
                                .addComponent(jLabel4)))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(25, 25, 25)
                                .addComponent(jLabel5)
                                .addGap(38, 38, 38)
                                .addComponent(jLabel6))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addComponent(t_minimo, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(t_unidad, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(39, 39, 39)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(t_pedido)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(t_fecha, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel7)
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(t_id, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(t_descripcion, javax.swing.GroupLayout.PREFERRED_SIZE, 319, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cb_almacen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(t_id, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(t_descripcion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cb_almacen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5)
                    .addComponent(jLabel6)
                    .addComponent(jLabel7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(t_unidad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(t_pedido, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(t_fecha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(t_existencias, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(t_maximo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(t_minimo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void t_descripcionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_t_descripcionActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_t_descripcionActionPerformed

    private void cb_almacenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cb_almacenActionPerformed
        // TODO add your handling code here:
        busca();
    }//GEN-LAST:event_cb_almacenActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        Session session = HibernateUtil.getSessionFactory().openSession();
        try
        {
            Configuracion con= (Configuracion)session.get(Configuracion.class, 1);
            BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.WINANSI, BaseFont.NOT_EMBEDDED);
            PDF reporte = new PDF();
            Date fecha = new Date();
            DateFormat dateFormat1 = new SimpleDateFormat("dd-MM-yyyyHH-mm-ss");//YYYY-MM-DD HH:MM:SS
            String valor=dateFormat1.format(fecha);
            File folder = new File("reportes/");
            folder.mkdirs();
            reporte.Abrir2(PageSize.LETTER, "almacen", "reportes/"+valor+"-Ejemplar.pdf");

            Font font = new Font(Font.FontFamily.HELVETICA, 8, Font.NORMAL);
            DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");//YYYY-MM-DD HH:MM:SS
            DecimalFormat formatoPorcentaje = new DecimalFormat("#,##0.00");
            BaseColor contenido=BaseColor.WHITE;
            int centro=Element.ALIGN_CENTER;
            int izquierda = Element.ALIGN_LEFT;
            int derecha=Element.ALIGN_RIGHT;
            float[] nuevos=new float[]{100,70,70,350,80};

            PdfPTable tabla=reporte.crearTabla(nuevos.length, nuevos, 100, Element.ALIGN_CENTER);
            reporte.estatusAutoriza("ok", "NO AUTORIZADO");
            cabecera2(reporte, bf, tabla, "ALMACEN DE CONSUMIBLES (Movimientos de Articulo)", 1);
            double subtotal=0.0d;
            double iva=0.0d;


            for(int i=0; i<t_datos.getRowCount();i++){
                tabla.addCell(reporte.celda(""+t_datos.getValueAt(i, 0).toString(), font, contenido, izquierda, 0,1,Rectangle.RECTANGLE));
                if(t_datos.getValueAt(i, 1)!=null)
                    tabla.addCell(reporte.celda(""+t_datos.getValueAt(i, 1).toString(), font, contenido, izquierda, 0,1,Rectangle.RECTANGLE));
                else
                    tabla.addCell(reporte.celda("-", font, contenido, izquierda, 0,1,Rectangle.RECTANGLE));
                tabla.addCell(reporte.celda(""+t_datos.getValueAt(i, 2).toString(), font, contenido, izquierda, 0,1,Rectangle.RECTANGLE));
                tabla.addCell(reporte.celda(""+t_datos.getValueAt(i, 3).toString(), font, contenido, izquierda, 0,1,Rectangle.RECTANGLE));
                tabla.addCell(reporte.celda(""+t_datos.getValueAt(i, 4).toString()+t_unidad.getText(), font, contenido, centro, 0,1,Rectangle.RECTANGLE));
            }

            tabla.setHeaderRows(1);
            reporte.agregaObjeto(tabla);
            reporte.cerrar();
            reporte.visualizar2("reportes/"+valor+"-Ejemplar.pdf");
        }
        catch(Exception e)
        {
            System.out.println(e);
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "No se pudo realizar el reporte si el archivo esta abierto.");
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    public void formatoTabla()
    {
        Color c1 = new java.awt.Color(2, 135, 242); //2, 135, 242  //m 90,66,126
        for(int x=0; x<t_datos.getColumnModel().getColumnCount(); x++)
        {
            t_datos.getColumnModel().getColumn(x).setHeaderRenderer(new Render1(c1));
        }
        tabla_tamaños();
        t_datos.setShowVerticalLines(true);
        t_datos.setShowHorizontalLines(true);
        t_datos.setDefaultRenderer(Double.class, formato); 
        t_datos.setDefaultRenderer(Integer.class, formato);
        t_datos.setDefaultRenderer(String.class, formato);
        t_datos.setDefaultRenderer(Boolean.class, formato);
    }
    
    public void tabla_tamaños()
    {
        TableColumnModel col_model = t_datos.getColumnModel();
        for (int i=0; i<t_datos.getColumnCount(); i++)
        {
            TableColumn column = col_model.getColumn(i);
            switch(i)
            {
                case 0:
                    column.setPreferredWidth(50);
                    break;
                case 1:
                    column.setPreferredWidth(70);
                    break;
                case 2:
                    column.setPreferredWidth(50);
                    break;      
                case 3:
                    column.setPreferredWidth(190);
                    break; 
                case 4:
                    column.setPreferredWidth(70);
                    break; 
            }
        }
        JTableHeader header = t_datos.getTableHeader();
        header.setForeground(Color.white);
    }
  

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox cb_almacen;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable t_datos;
    private javax.swing.JTextField t_descripcion;
    private javax.swing.JFormattedTextField t_existencias;
    private javax.swing.JTextField t_fecha;
    private javax.swing.JTextField t_id;
    private javax.swing.JFormattedTextField t_maximo;
    private javax.swing.JFormattedTextField t_minimo;
    private javax.swing.JTextField t_pedido;
    private javax.swing.JTextField t_unidad;
    // End of variables declaration//GEN-END:variables
}
