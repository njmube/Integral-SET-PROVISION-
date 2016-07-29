/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Almacen;

import Hibernate.Util.HibernateUtil;
import Hibernate.entidades.Usuario;
import Integral.ExtensionFileFilter;
import Integral.Herramientas;
import Integral.PDF;
import Integral.Render1;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
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
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.CriteriaSpecification;

/**
 *
 * @author Angel
 */
public class Existencias extends javax.swing.JPanel {

    /**
     * Creates new form Existencias
     */
    Usuario usr;
    String sessionPrograma="";
    Herramientas h;
    DefaultTableModel model;
    int aux=0;
    private Usuario user;
    public Existencias(Usuario usuario, String ses) {
        initComponents();
        usr=usuario;
        sessionPrograma=ses;
        formatotabla();
        tabla_tamaños();
        cargaDatos();
    }

    public String[] getColumnas() {
        String columnas[] = new String[]{"ID PARTE", "MARCA", "TIPO", "MODELO", "CATALOGO", "MEDIDA", "EXISTENCIAS", "COMENTARIOS"};
        return columnas;
    }
    public void cargaDatos(){
        Session session = HibernateUtil.getSessionFactory().openSession();
        try{
            model = new DefaultTableModel(null, getColumnas());
            t_datos.setModel(model);
            tabla_tamaños();
            formatotabla();
            ArrayList datos = new ArrayList();
            Query query = session.createSQLQuery("select ejemplar.id_Parte, id_marca as mar, if(id_marca is not null, (select marca_nombre from marca where id_marca=mar),'')as marca_nombre, ejemplar.tipo_nombre, ejemplar.modelo, ejemplar.id_catalogo, ejemplar.comentario, ejemplar.medida, ejemplar.existencias from ejemplar \n" +
                            "where ejemplar.inventario=1");
            query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
            datos = (ArrayList)query.list();
             Object[] objeto = new Object[t_datos.getColumnCount()];
            for(int i=0; i<datos.size();i++){
                java.util.HashMap map = (java.util.HashMap)datos.get(i);
                if(map.get("id_Parte")!=null){
                    objeto[0]=(map.get("id_Parte"));
                }else{
                    objeto[0]=" ";
                }
                objeto[1]=(map.get("marca_nombre"));
                if(map.get("tipo_nombre")!=null){
                    objeto[2]=(map.get("tipo_nombre"));
                }else{
                    objeto[2]=" ";
                }
                if(map.get("modelo")!=null){
                    objeto[3]=(map.get("modelo"));
                }else{
                    objeto[3]=" ";
                }
                if(map.get("id_catalogo")!=null){
                    objeto[4]=(map.get("id_catalogo"));
                }else{
                    objeto[4]=" ";
                }
                if(map.get("medida")!=null){
                    objeto[5]=(map.get("medida"));
                }else{
                    objeto[5]=" ";
                }
                if(map.get("existencias")!=null){
                    objeto[6]=(map.get("existencias"));
                }else{
                    objeto[6]=" ";
                }
                if(map.get("comentario")!=null){
                    objeto[7]=(map.get("comentario"));
                }else{
                    objeto[7]=" ";
                }
                model.addRow(objeto);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        finally{
            if(session.isOpen()==true){
                session.close();
            }
        }
    }
    public void formatotabla(){
        Color c1 = new java.awt.Color(2, 135, 242);
        for(int x=0; x<t_datos.getColumnModel().getColumnCount(); x++)
            t_datos.getColumnModel().getColumn(x).setHeaderRenderer(new Render1(c1));
        tabla_tamaños();
        t_datos.setShowVerticalLines(true);
        t_datos.setShowHorizontalLines(true);
    }
    public void tabla_tamaños(){
        TableColumnModel col_model = t_datos.getColumnModel();
        
        for(int i=0; i<t_datos.getColumnCount(); i++){
            TableColumn column = col_model.getColumn(i);
            switch(i){
                case 0:
                    column.setPreferredWidth(30);
                    break;
                case 1:
                    column.setPreferredWidth(100);
                    break;
                case 2:
                    column.setPreferredWidth(100);
                    break;
                case 3:
                    column.setPreferredWidth(20);
                    break;
                case 4:
                    column.setPreferredWidth(300);
                    break;
                case 5:
                    column.setPreferredWidth(20);
                    break;
                case 6:
                    column.setPreferredWidth(30);
                    break;
                case 7:
                    column.setPreferredWidth(100);
                    break;
                default:
                    column.setPreferredWidth(30);
                    break;
            }
            JTableHeader header = t_datos.getTableHeader();
            header.setForeground(Color.BLACK);
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
        jLabel1 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        t_datos = new javax.swing.JTable(){
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        jPanel2 = new javax.swing.JPanel();
        jButton4 = new javax.swing.JButton();

        setBackground(new java.awt.Color(255, 255, 255));
        setPreferredSize(new java.awt.Dimension(800, 500));
        setLayout(new java.awt.BorderLayout());

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setPreferredSize(new java.awt.Dimension(800, 50));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel1.setText("EXISTENCIAS DE ARTICULOS.");

        jButton1.setBackground(new java.awt.Color(2, 135, 242));
        jButton1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText("PDF");
        jButton1.setToolTipText("Generar PDF");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setBackground(new java.awt.Color(2, 135, 242));
        jButton2.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jButton2.setForeground(new java.awt.Color(255, 255, 255));
        jButton2.setText("EXCEL");
        jButton2.setToolTipText("Generar EXCEL");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel2.setText("EXPORTAR:");

        jButton3.setBackground(new java.awt.Color(2, 135, 242));
        jButton3.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jButton3.setForeground(new java.awt.Color(255, 255, 255));
        jButton3.setText("CONTEO");
        jButton3.setToolTipText("Generar PDF");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 207, Short.MAX_VALUE)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton2)
                .addGap(31, 31, 31)
                .addComponent(jButton3)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton1)
                            .addComponent(jButton2)
                            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton3))
                        .addContainerGap())))
        );

        add(jPanel1, java.awt.BorderLayout.PAGE_START);

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));

        t_datos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID PARTE", "MARCA", "TIPO", "MODELO", "CATALOGO", "MEDIDA", "EXISTENCIAS", "COMENTARIO"
            }
        ));
        t_datos.setGridColor(new java.awt.Color(0, 0, 0));
        t_datos.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(t_datos);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 780, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 489, Short.MAX_VALUE))
        );

        add(jPanel4, java.awt.BorderLayout.CENTER);

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setMinimumSize(new java.awt.Dimension(100, 20));

        jButton4.setBackground(new java.awt.Color(2, 135, 242));
        jButton4.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jButton4.setForeground(new java.awt.Color(255, 255, 255));
        jButton4.setText("ACTUALIZAR");
        jButton4.setToolTipText("Actualizar Datos");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(679, Short.MAX_VALUE)
                .addComponent(jButton4)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton4, javax.swing.GroupLayout.DEFAULT_SIZE, 28, Short.MAX_VALUE)
                .addContainerGap())
        );

        add(jPanel2, java.awt.BorderLayout.PAGE_END);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        aux=0;
        try {
            javax.swing.JFileChooser archivo = new javax.swing.JFileChooser();
            archivo.setFileFilter(new ExtensionFileFilter("pdf document (*.pdf)", new String[]{"PDF"}));
            String ruta = null;
             if (archivo.showSaveDialog(null) == archivo.APPROVE_OPTION) {
                ruta = archivo.getSelectedFile().getAbsolutePath();
                if (ruta != null) {
                     try {
                         PDF reporte = new PDF();
                        reporte.Abrir2(PageSize.LEGAL.rotate(), "Existencias", ruta + ".pdf");
                        BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.WINANSI, BaseFont.NOT_EMBEDDED);
                        float[] tamanio = new float[]{90, 120, 120, 55, 230, 55, 90, 90};
                        Font font = new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL);
                        BaseColor contenido = BaseColor.WHITE;
                        int centro = com.itextpdf.text.Element.ALIGN_CENTER;
                        int izquierda = com.itextpdf.text.Element.ALIGN_LEFT;
                        int derecha = com.itextpdf.text.Element.ALIGN_RIGHT;
                        
                        PdfPTable tabla = reporte.crearTabla(tamanio.length, tamanio, 100, Element.ALIGN_CENTER);
                        cabecera(reporte, bf, tabla, "EXISTENCIAS DE ARTICULOS.", 2);
                        
                        for(int i=0; i<t_datos.getRowCount(); i++){
                            tabla.addCell(reporte.celda(t_datos.getValueAt(i, 0).toString(),font, contenido, centro, 0, 1, Rectangle.RECTANGLE));
                            tabla.addCell(reporte.celda(t_datos.getValueAt(i, 1).toString(),font, contenido, centro, 0, 1, Rectangle.RECTANGLE));
                            tabla.addCell(reporte.celda(t_datos.getValueAt(i, 2).toString(),font, contenido, centro, 0, 1, Rectangle.RECTANGLE));
                            tabla.addCell(reporte.celda(t_datos.getValueAt(i, 3).toString(),font, contenido, centro, 0, 1, Rectangle.RECTANGLE));
                            tabla.addCell(reporte.celda(t_datos.getValueAt(i, 4).toString(),font, contenido, centro, 0, 1, Rectangle.RECTANGLE));
                            tabla.addCell(reporte.celda(t_datos.getValueAt(i, 5).toString(),font, contenido, centro, 0, 1, Rectangle.RECTANGLE));
                            tabla.addCell(reporte.celda(t_datos.getValueAt(i, 6).toString(),font, contenido, centro, 0, 1, Rectangle.RECTANGLE));
                            tabla.addCell(reporte.celda(t_datos.getValueAt(i, 7).toString(),font, contenido, centro, 0, 1, Rectangle.RECTANGLE));
                        }
                        reporte.agregaObjeto(tabla);
                        reporte.cerrar();
                        reporte.visualizar2(ruta + ".pdf");
                     }catch(Exception e){
                         JOptionPane.showMessageDialog(this, "No se pudo realizar el reporte si el archivo esta abierto.");
                     }
                }
             }
        }catch(Exception e){
            e.printStackTrace();
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        h=new Herramientas(this.usr, 0);
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
                    Sheet hoja = libro.createSheet("Existencias Articulos");
                    for(int ren=0;ren<(t_datos.getRowCount()+1);ren++)
                    {
                        Row fila = hoja.createRow(ren);
                        for(int col=0; col<t_datos.getColumnCount(); col++)
                        {
                            Cell celda = fila.createCell(col);
                            if(ren==0)
                            {
                                celda.setCellValue(t_datos.getColumnName(col));
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
            }
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        aux=1;
        try {
            javax.swing.JFileChooser archivo = new javax.swing.JFileChooser();
            archivo.setFileFilter(new ExtensionFileFilter("pdf document (*.pdf)", new String[]{"PDF"}));
            String ruta = null;
             if (archivo.showSaveDialog(null) == archivo.APPROVE_OPTION) {
                ruta = archivo.getSelectedFile().getAbsolutePath();
                if (ruta != null) {
                     try {
                         PDF reporte = new PDF();
                        reporte.Abrir2(PageSize.LEGAL.rotate(), "Existencias", ruta + ".pdf");
                        BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.WINANSI, BaseFont.NOT_EMBEDDED);
                        float[] tamanio = new float[]{90, 120, 120, 55, 230, 55, 100, 100};
                        Font font = new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL);
                        BaseColor contenido = BaseColor.WHITE;
                        int centro = com.itextpdf.text.Element.ALIGN_CENTER;
                        int izquierda = com.itextpdf.text.Element.ALIGN_LEFT;
                        int derecha = com.itextpdf.text.Element.ALIGN_RIGHT;
                        
                        PdfPTable tabla = reporte.crearTabla(tamanio.length, tamanio, 100, Element.ALIGN_CENTER);
                        cabecera(reporte, bf, tabla, "EXISTENCIAS DE ARTICULOS.", 2);
                        
                        for(int i=0; i<t_datos.getRowCount(); i++){
                            tabla.addCell(reporte.celda(t_datos.getValueAt(i, 0).toString(),font, contenido, centro, 0, 1, Rectangle.RECTANGLE));
                            tabla.addCell(reporte.celda(t_datos.getValueAt(i, 1).toString(),font, contenido, centro, 0, 1, Rectangle.RECTANGLE));
                            tabla.addCell(reporte.celda(t_datos.getValueAt(i, 2).toString(),font, contenido, centro, 0, 1, Rectangle.RECTANGLE));
                            tabla.addCell(reporte.celda(t_datos.getValueAt(i, 3).toString(),font, contenido, centro, 0, 1, Rectangle.RECTANGLE));
                            tabla.addCell(reporte.celda(t_datos.getValueAt(i, 4).toString(),font, contenido, centro, 0, 1, Rectangle.RECTANGLE));
                            tabla.addCell(reporte.celda(t_datos.getValueAt(i, 5).toString(),font, contenido, centro, 0, 1, Rectangle.RECTANGLE));
                            tabla.addCell(reporte.celda(t_datos.getValueAt(i, 6).toString(),font, contenido, centro, 0, 1, Rectangle.RECTANGLE));
                            tabla.addCell(reporte.celda(" ",font, contenido, centro, 0, 1, Rectangle.RECTANGLE));
                        }
                        reporte.agregaObjeto(tabla);
                        reporte.cerrar();
                        reporte.visualizar2(ruta + ".pdf");
                     }catch(Exception e){
                         JOptionPane.showMessageDialog(this, "No se pudo realizar el reporte si el archivo esta abierto.");
                     }
                }
             }
        }catch(Exception e){
            e.printStackTrace();
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        cargaDatos();
    }//GEN-LAST:event_jButton4ActionPerformed
public void cabecera(PDF reporte, BaseFont bf, PdfPTable tabla, String titulo, int op) {
        reporte.inicioTexto();
        reporte.contenido.setFontAndSize(bf, 9);
        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, titulo, 35, 495, 0);
        reporte.finTexto();

        reporte.agregaObjeto(reporte.crearImagen("imagenes/factura300115.jpg", 250, -52, 60));
        reporte.agregaObjeto(new Paragraph(" "));
        reporte.agregaObjeto(new Paragraph(" "));
        reporte.agregaObjeto(new Paragraph(" "));
        reporte.agregaObjeto(new Paragraph(" "));
        reporte.agregaObjeto(new Paragraph(" "));
        Font font = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD);

        BaseColor cabecera = BaseColor.GRAY;
        BaseColor contenido = BaseColor.WHITE;
        int centro = com.itextpdf.text.Element.ALIGN_CENTER;
        int izquierda = com.itextpdf.text.Element.ALIGN_LEFT;
        int derecha = com.itextpdf.text.Element.ALIGN_RIGHT;
        tabla.addCell(reporte.celda("ID PARTE", font, cabecera, centro, 0, 1, Rectangle.RECTANGLE));
        tabla.addCell(reporte.celda("MARCA", font, cabecera, centro, 0, 1, Rectangle.RECTANGLE));
        tabla.addCell(reporte.celda("TIPO", font, cabecera, centro, 0, 1, Rectangle.RECTANGLE));
        tabla.addCell(reporte.celda("MODELO", font, cabecera, centro, 0, 1, Rectangle.RECTANGLE));
        tabla.addCell(reporte.celda("CATALOGO", font, cabecera, centro, 0, 1, Rectangle.RECTANGLE));
        tabla.addCell(reporte.celda("MEDIDA", font, cabecera, centro, 0, 1, Rectangle.RECTANGLE));
        tabla.addCell(reporte.celda("EXIST. SISTEMA", font, cabecera, centro, 0, 1, Rectangle.RECTANGLE));
        if(aux!=0)
            tabla.addCell(reporte.celda("EXIST. FISICO", font, cabecera, centro, 0, 1, Rectangle.RECTANGLE));
        else
            tabla.addCell(reporte.celda("COMENTARIO", font, cabecera, centro, 0, 1, Rectangle.RECTANGLE));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable t_datos;
    // End of variables declaration//GEN-END:variables
}
