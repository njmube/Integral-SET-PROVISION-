/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package sat;

import Hibernate.Util.HibernateUtil;
import Hibernate.entidades.UsoCfdi;
import Hibernate.entidades.Usuario;
import Integral.ExtensionFileFilter;
import Integral.Herramientas;
import Integral.Render1;
import Integral.calendario;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Vector;
import javax.swing.DefaultCellEditor;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author Sistemas
 */
public class ListaUsoCfdi extends javax.swing.JDialog {

    DefaultTableModel mod_todo;
    String[] columnas = new String [] {"ID","Descripción","Fisica", "Moral", "Ficha Ini. Vig", "Fecha Fin Vig."};
    Herramientas h;
    Usuario usr;
    String sessionPrograma="";
    int x=0;
    
    /**
     * Creates new form ListaMarca
     */
    public ListaUsoCfdi(java.awt.Frame parent, boolean modal, String session, Usuario usuario) {
        super(parent, modal);
        initComponents();
        usr=usuario;
        sessionPrograma=session;
        t_datos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        datos();
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
    
    public void tabla_tamaños()
    {
        TableColumnModel col_model = t_datos.getColumnModel();
        for (int i=0; i<t_datos.getColumnCount(); i++)
        {
  	      TableColumn column = col_model.getColumn(i);
              switch(i)
              {
                  case 0:
                      column.setPreferredWidth(30);
                      break;
                  case 1:
                      column.setPreferredWidth(200);
                      break;
                  case 2:
                      column.setPreferredWidth(40);
                      DefaultCellEditor editor = new DefaultCellEditor(medida);
                      column.setCellEditor(editor); 
                      editor.setClickCountToStart(2);
                      break;
                  case 3:
                      column.setPreferredWidth(40);
                      DefaultCellEditor editor1 = new DefaultCellEditor(medida);
                      column.setCellEditor(editor1); 
                      editor1.setClickCountToStart(2);
                      break;
                  case 4:
                      column.setPreferredWidth(80);
                      break;
                  case 5:
                      column.setPreferredWidth(80);
                      break;
                  default:
                      column.setPreferredWidth(40);
                      break;
                      
              }
        }
        JTableHeader header = t_datos.getTableHeader();
        header.setForeground(Color.white);
    }
    List HQLQuery(String consulta)
    {
        List lista = null;
        try {
            Session session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            Query q = session.createQuery(consulta);
            lista = q.list();
            session.getTransaction().commit();
            session.disconnect();
            return lista;
        } catch (HibernateException he) {
            he.printStackTrace();
            lista= null;
            return lista;
        }
    }
    
    void datos()
    {
        List lista = HQLQuery("FROM UsoCfdi");
        if(lista!=null && lista.size()>0)
        {
            t_datos.setModel(ModeloTablaReporte(lista.size(), columnas));
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            for (int x=0; x<lista.size(); x++) 
            {
                UsoCfdi actor = (UsoCfdi) lista.get(x);
                mod_todo.setValueAt(actor.getIdUsoCfdi(), x, 0);
                mod_todo.setValueAt(actor.getDescripcion(), x, 1);
                mod_todo.setValueAt(actor.getFisica(), x, 2);
                mod_todo.setValueAt(actor.getMoral(), x, 3);
                mod_todo.setValueAt(dateFormat.format(actor.getFechaInicioVigencia()), x, 4);
                mod_todo.setValueAt(dateFormat.format(actor.getFechaFinVigencia()), x, 5);
            }
        }
        else
            t_datos.setModel(ModeloTablaReporte(0, columnas));
        formatoTabla();
    }
    
    DefaultTableModel ModeloTablaReporte(int renglones, String columnas[])
    {
        mod_todo = new DefaultTableModel(new Object [renglones][6], columnas)
        {
            Class[] types = new Class [] {
                java.lang.String.class, 
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                true, true, true, true, true, true
            };

            public void setValueAt(Object value, int row, int col)
             {
                    Vector vector = (Vector)this.dataVector.elementAt(row);
                    Object celda = ((Vector)this.dataVector.elementAt(row)).elementAt(col);
                    switch(col)
                    {
                        case 0:
                            if(vector.get(col)==null)
                            {
                                vector.setElementAt(value, col);
                                this.dataVector.setElementAt(vector, row);
                                fireTableCellUpdated(row, col);
                            }
                            else
                            {
                                if(value.toString().length()>6)
                                    value=value.toString().substring(0, 6);
                                if(value.toString().compareTo(t_datos.getValueAt(row, col).toString())!=0)
                                {
                                    List lista = HQLQuery("FROM UsoCfdi WHERE idUsoCfdi='"+value.toString()+"'");
                                    if(lista!=null)
                                    {
                                        if(lista!=null && lista.size()==0)
                                        {
                                            try {
                                                Session session = HibernateUtil.getSessionFactory().openSession();
                                                session.beginTransaction();
                                                Query q = session.createQuery("update UsoCfdi obj set obj.idUsoCfdi='"+value.toString()+"' where obj.idUsoCfdi='"+t_datos.getValueAt(row, 0).toString()+"'");
                                                q.executeUpdate();
                                                session.getTransaction().commit();
                                                session.disconnect();
                                                vector.setElementAt(value, col);
                                                this.dataVector.setElementAt(vector, row);
                                                fireTableCellUpdated(row, col);
                                            } catch (HibernateException he) {
                                                he.printStackTrace();
                                                JOptionPane.showMessageDialog(null, "Error al guardar los datos");
                                            }
                                        }
                                        else
                                            JOptionPane.showMessageDialog(null, "¡Identificador en Uso!");
                                    }
                                    else
                                        JOptionPane.showMessageDialog(null, "Error al consultar la base de datos");
                                }
                            }
                                break;

                        case 1:
                            if(vector.get(col)==null)
                            {
                                vector.setElementAt(value, col);
                                this.dataVector.setElementAt(vector, row);
                                fireTableCellUpdated(row, col);
                            }
                            else
                            {
                                if(value.toString().length()>200)
                                    value=value.toString().substring(0, 200);
                                if(value.toString().compareTo(t_datos.getValueAt(row, col).toString())!=0)
                                {
                                    List lista = HQLQuery("FROM UsoCfdi WHERE descripcion='"+value.toString()+"'");
                                    if(lista!=null)
                                    {
                                        if(lista!=null && lista.size()==0)
                                        {
                                            try {
                                                Session session = HibernateUtil.getSessionFactory().openSession();
                                                session.beginTransaction();
                                                Query q = session.createQuery("update UsoCfdi obj set obj.descripcion='"+value.toString()+"' where obj.idUsoCfdi='"+t_datos.getValueAt(row, 0).toString()+"'");
                                                q.executeUpdate();
                                                session.getTransaction().commit();
                                                session.disconnect();
                                                vector.setElementAt(value, col);
                                                this.dataVector.setElementAt(vector, row);
                                                fireTableCellUpdated(row, col);
                                            } catch (HibernateException he) {
                                                he.printStackTrace();
                                                JOptionPane.showMessageDialog(null, "Error al guardar los datos");
                                            }
                                        }
                                        else
                                            JOptionPane.showMessageDialog(null, "¡Identificador en Uso!");
                                    }
                                    else
                                        JOptionPane.showMessageDialog(null, "Error al consultar la base de datos");
                                }
                            }
                                break;
                        
                        case 2:
                            if(vector.get(col)==null)
                            {
                                vector.setElementAt(value, col);
                                this.dataVector.setElementAt(vector, row);
                                fireTableCellUpdated(row, col);
                            }
                            else
                            {
                                if(value.toString().compareTo(t_datos.getValueAt(row, col).toString())!=0)
                                {
                                    try {
                                        Session session = HibernateUtil.getSessionFactory().openSession();
                                        session.beginTransaction();
                                        Query q = session.createQuery("update UsoCfdi obj set obj.fisica='"+value.toString()+"' where obj.idUsoCfdi='"+t_datos.getValueAt(row, 0).toString()+"'");
                                        q.executeUpdate();
                                        session.getTransaction().commit();
                                        session.disconnect();
                                        vector.setElementAt(value, col);
                                        this.dataVector.setElementAt(vector, row);
                                        fireTableCellUpdated(row, col);
                                    } catch (HibernateException he) {
                                        he.printStackTrace();
                                        JOptionPane.showMessageDialog(null, "Error al guardar los datos");
                                    }
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
                                if(value.toString().compareTo(t_datos.getValueAt(row, col).toString())!=0)
                                {
                                    try {
                                        Session session = HibernateUtil.getSessionFactory().openSession();
                                        session.beginTransaction();
                                        Query q = session.createQuery("update UsoCfdi obj set obj.moral='"+value.toString()+"' where obj.idUsoCfdi='"+t_datos.getValueAt(row, 0).toString()+"'");
                                        q.executeUpdate();
                                        session.getTransaction().commit();
                                        session.disconnect();
                                        vector.setElementAt(value, col);
                                        this.dataVector.setElementAt(vector, row);
                                        fireTableCellUpdated(row, col);
                                    } catch (HibernateException he) {
                                        he.printStackTrace();
                                        JOptionPane.showMessageDialog(null, "Error al guardar los datos");
                                    }
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
                                if(value.toString().compareTo(t_datos.getValueAt(row, col).toString())!=0)
                                {
                                    try {
                                        Session session = HibernateUtil.getSessionFactory().openSession();
                                        session.beginTransaction();
                                        Query q = session.createQuery("update UsoCfdi obj set obj.fechaInicioVigencia='"+value.toString()+"' where obj.idUsoCfdi='"+t_datos.getValueAt(row, 0).toString()+"'");
                                        q.executeUpdate();
                                        session.getTransaction().commit();
                                        session.disconnect();
                                        vector.setElementAt(value, col);
                                        this.dataVector.setElementAt(vector, row);
                                        fireTableCellUpdated(row, col);
                                    } catch (HibernateException he) {
                                        he.printStackTrace();
                                        JOptionPane.showMessageDialog(null, "Error al guardar los datos");
                                    }
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
                                if(value.toString().compareTo(t_datos.getValueAt(row, col).toString())!=0)
                                {
                                    try {
                                        Session session = HibernateUtil.getSessionFactory().openSession();
                                        session.beginTransaction();
                                        Query q = session.createQuery("update UsoCfdi obj set obj.fechaFinVigencia='"+value.toString()+"' where obj.idUsoCfdi='"+t_datos.getValueAt(row, 0).toString()+"'");
                                        q.executeUpdate();
                                        session.getTransaction().commit();
                                        session.disconnect();
                                        vector.setElementAt(value, col);
                                        this.dataVector.setElementAt(vector, row);
                                        fireTableCellUpdated(row, col);
                                    } catch (HibernateException he) {
                                        he.printStackTrace();
                                        JOptionPane.showMessageDialog(null, "Error al guardar los datos");
                                    }
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

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        };
        return mod_todo;
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
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        t_datos = new javax.swing.JTable();

        medida.setFont(new java.awt.Font("Dialog", 0, 9)); // NOI18N
        medida.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "si", "no" }));

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Administración de Marcas");

        jPanel1.setLayout(new java.awt.BorderLayout());

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        jButton1.setIcon(new ImageIcon("imagenes/agregar1.png"));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setIcon(new ImageIcon("imagenes/eliminar1.png"));
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setIcon(new ImageIcon("imagenes/consulta1.png"));
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton5.setIcon(new ImageIcon("imagenes/exel.png"));
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton5)
                .addContainerGap(223, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel2, java.awt.BorderLayout.PAGE_START);

        jScrollPane1.setBackground(new java.awt.Color(255, 255, 255));

        t_datos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "DESCRIPCION", "FISICA", "MORAL", "FECHA INI. VIG", "FECHA FIN VIG."
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        t_datos.setFillsViewportHeight(true);
        t_datos.getTableHeader().setReorderingAllowed(false);
        t_datos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                t_datosMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(t_datos);

        jPanel1.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 383, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        h=new Herramientas(usr, 0);
        h.session(sessionPrograma);
        altaUsoCfdi obj = new altaUsoCfdi(new javax.swing.JFrame(), true, usr, sessionPrograma);
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        obj.setLocation((d.width/2)-(obj.getWidth()/2), (d.height/2)-(obj.getHeight()/2));
        obj.setVisible(true);
        datos();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        h=new Herramientas(usr, 0);
        h.session(sessionPrograma);
        if(this.t_datos.getSelectedRow()>=0)
        {
            int opt=JOptionPane.showConfirmDialog(this, "¡Los datos capturados se eliminarán!");
            if (JOptionPane.YES_OPTION == opt)
            {
                Session session = HibernateUtil.getSessionFactory().openSession();
                try
                {
                    session.beginTransaction();
                    UsoCfdi actor = (UsoCfdi)session.get(UsoCfdi.class, t_datos.getValueAt(t_datos.getSelectedRow(), 0).toString());

                    if(actor.getClienteses().isEmpty()==false)
                    {
                        session.getTransaction().rollback();
                        JOptionPane.showMessageDialog(null, "¡El Uso CFDI esta en uso, no se puede eliminar!");
                    }
                    else
                    {
                        session.delete(actor);
                        session.getTransaction().commit();
                        if(session.isOpen())
                            session.close();
                        datos();
                        JOptionPane.showMessageDialog(null, "Registro eliminado");
                    }
                }catch(Exception e)
                {
                    e.printStackTrace();
                    if(session.isOpen())
                    {
                        session.getTransaction().rollback();
                        session.close();
                    }
                }
            } 
        }
        else
        {
            JOptionPane.showMessageDialog(null, "¡Selecciona el usp CFDI que desees eliminar!");
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        h=new Herramientas(usr, 0);
        h.session(sessionPrograma);

        buscaUsoCfdi obj = new buscaUsoCfdi(new javax.swing.JFrame(), true, this.sessionPrograma, this.usr);
        obj.t_busca.requestFocus();
        obj.formatoTabla();
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        obj.setLocation((d.width/2)-(obj.getWidth()/2), (d.height/2)-(obj.getHeight()/2));
        obj.setVisible(true);

        UsoCfdi actor=obj.getReturnStatus();
        if(actor!=null)
        {
            if(x>=t_datos.getRowCount())
            {
                x=0;
                java.awt.Rectangle r = t_datos.getCellRect( x, 3, true);
                t_datos.scrollRectToVisible(r);
            }
            for(; x<t_datos.getRowCount(); x++)
            {
                if(t_datos.getValueAt(x, 0).toString().indexOf(actor.getIdUsoCfdi()) != -1)
                {
                    t_datos.setRowSelectionInterval(x, x);
                    t_datos.setColumnSelectionInterval(0, 0);
                    java.awt.Rectangle r = t_datos.getCellRect( x, 3, true);
                    t_datos.scrollRectToVisible(r);
                    break;
                }
            }
            x++;
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:
        h=new Herramientas(usr, 0);
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
                    Sheet hoja = libro.createSheet("Uso CFDI");
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
                                celda.setCellValue(t_datos.getValueAt(ren-1, col).toString());
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
    }//GEN-LAST:event_jButton5ActionPerformed

    private void t_datosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_t_datosMouseClicked
        // TODO add your handling code here:
        if(t_datos.getSelectedColumn()>3 && t_datos.getSelectedColumn()<6)
        {
            calendario cal =new calendario(new javax.swing.JFrame(), true, false);
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
            }
            else
                t_datos.setValueAt("", t_datos.getSelectedRow(), t_datos.getSelectedColumn());
        }
    }//GEN-LAST:event_t_datosMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JComboBox medida;
    private javax.swing.JTable t_datos;
    // End of variables declaration//GEN-END:variables
}
