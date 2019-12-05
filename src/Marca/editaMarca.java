/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Marca;

import Hibernate.Util.HibernateUtil;
import Hibernate.entidades.Marca;
import Hibernate.entidades.Usuario;
import Integral.ExtensionFileFilter;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.List;
import java.util.Vector;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import Integral.Herramientas;
import Integral.Render1;
import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import javax.swing.DefaultCellEditor;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
/**
 *
 * @author ESPECIALIZADO TOLUCA
 */
public class editaMarca extends javax.swing.JPanel {
    
    private Marca actor;
    DefaultTableModel model;
    String[] columnas = new String [] {"Prefijo","Nombre", "Inventario"}; 
    public String ic = "";
    public String m = "";
    Usuario usr;
    String sessionPrograma="";
    Herramientas h;
    Marca registro = null;
    int x=0;

    public editaMarca(Usuario usuario, String ses) {
        initComponents();
        t_datos.setModel(ModeloTablaReporte(0, columnas));
        sessionPrograma=ses;
        usr=usuario;
        formatoTabla();
        buscaDato();
    }

    DefaultTableModel ModeloTablaReporte(int renglones, String columnas[])
    {
        model = new DefaultTableModel(new Object [renglones][2], columnas)
        {
            Class[] types = new Class [] {
                java.lang.String.class, 
                java.lang.String.class,
                java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                true,true,true
            };
            
            public void setValueAt(Object value, int row, int col)
            {
                Vector vector = (Vector)this.dataVector.elementAt(row);
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
                            Session session = HibernateUtil.getSessionFactory().openSession();
                            String valor = (String)value;
                            String valorAnterior = t_datos.getValueAt(row, col).toString();
                            if(valorAnterior.compareToIgnoreCase(valor)!=0)
                            {
                                try   
                                {
                                    session.beginTransaction();
                                    Object resp=session.createQuery("from Marca obj where obj.idMarca='"+valor+"'").uniqueResult();
                                    if(resp==null)
                                    {
                                        Query q = session.createQuery("update Marca obj set obj.idMarca='"+valor+"' where obj.idMarca='"+valorAnterior+"'");
                                        q.executeUpdate();
                                        session.getTransaction().commit();
                                        vector.setElementAt(value, col);
                                        this.dataVector.setElementAt(vector, row);
                                        fireTableCellUpdated(row, col);
                                    }
                                    else
                                        JOptionPane.showMessageDialog(null, "No se pueden guardar claves duplicadas");  
                                }
                                catch (HibernateException he)
                                {
                                    he.printStackTrace(); 
                                    System.out.println(he.hashCode());
                                    session.getTransaction().rollback();
                                    JOptionPane.showMessageDialog(null, "Error al guardar los datos");  
                                }
                                finally
                                {
                                    if(session.isOpen())
                                        session.close();
                                }
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
                            Session session = HibernateUtil.getSessionFactory().openSession();
                            String valor = (String)value;
                            String valorAnterior = t_datos.getValueAt(row, col).toString();
                            if(valorAnterior.compareToIgnoreCase(valor)!=0)
                            {
                                try   
                                {                
                                    session.beginTransaction();
                                    Object resp=session.createQuery("from Marca obj where obj.marcaNombre='"+valor+"'").uniqueResult();
                                    if(resp==null)
                                    {
                                        ic = t_datos.getValueAt(row, 0).toString();
                                        Query q = session.createQuery("update Marca obj set obj.marcaNombre='"+valor+"' where obj.idMarca='"+ic+"'");
                                        q.executeUpdate();
                                        session.getTransaction().commit();
                                        vector.setElementAt(value, col);
                                        this.dataVector.setElementAt(vector, row);
                                        fireTableCellUpdated(row, col);
                                    }
                                    else
                                        JOptionPane.showMessageDialog(null, "No se pueden guardar claves duplicadas");
                                }
                                catch (HibernateException he)
                                {
                                    he.printStackTrace(); 
                                    System.out.println(he.hashCode());
                                    session.getTransaction().rollback();
                                    JOptionPane.showMessageDialog(null, "Error al guardar los datos");  
                                }
                                finally
                                {
                                    if(session.isOpen())
                                        session.close();
                                }
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
                            Session session = HibernateUtil.getSessionFactory().openSession();
                            String valor = (String)value;
                            String valorAnterior = t_datos.getValueAt(row, col).toString();
                            if(valorAnterior.compareToIgnoreCase(valor)!=0)
                            {
                                try   
                                {                
                                    session.beginTransaction();
                                    Object resp=session.createQuery("from Marca obj where obj.idMarca='"+valor+"'").uniqueResult();
                                    if(resp==null)
                                    {
                                        ic = t_datos.getValueAt(row, 0).toString();
                                        switch(valor)
                                        {
                                            case "Unidad":
                                                valor="0";
                                                break;
                                            case "Inventario":
                                                valor="1";
                                                break;
                                            case "Ambos":
                                                valor="2";
                                                break;
                                        }
                                        Query q = session.createQuery("update Marca obj  set obj.ejemplar="+valor+" where obj.idMarca='"+ic+"'");
                                        q.executeUpdate();
                                        session.getTransaction().commit();
                                        vector.setElementAt(value, col);
                                        this.dataVector.setElementAt(vector, row);
                                        fireTableCellUpdated(row, col);
                                    }
                                    else
                                        JOptionPane.showMessageDialog(null, "No se pueden guardar claves duplicadas");  
                                }
                                catch (HibernateException he)
                                {
                                    he.printStackTrace(); 
                                    System.out.println(he.hashCode());
                                    session.getTransaction().rollback();
                                    JOptionPane.showMessageDialog(null, "Error al guardar los datos");  
                                }
                                finally
                                {
                                    if(session.isOpen())
                                        session.close();
                                }
                            }
                        }
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
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        cb_inventario = new javax.swing.JComboBox();
        t_prefijo = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        t_datos = new javax.swing.JTable();
        t_busca = new javax.swing.JTextField();
        b_busca = new javax.swing.JButton();
        Selecciona3 = new javax.swing.JButton();
        Eliminar1 = new javax.swing.JButton();
        bt_actualiza1 = new javax.swing.JButton();
        bt_actualiza2 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();

        cb_inventario.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Unidad", "Inventario", "Ambos" }));

        t_prefijo.setText("jTextField1");
        t_prefijo.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        t_prefijo.setCaretPosition(10);
        t_prefijo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                t_prefijoKeyTyped(evt);
            }
        });

        setBackground(new java.awt.Color(255, 255, 255));

        t_datos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Prefijo", "Nombre", "Inventario"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        t_datos.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        t_datos.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(t_datos);

        t_busca.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                t_buscaActionPerformed(evt);
            }
        });

        b_busca.setIcon(new ImageIcon("imagenes/buscar1.png"));
        b_busca.setToolTipText("Busca una partida");
        b_busca.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_buscaActionPerformed(evt);
            }
        });

        Selecciona3.setBackground(new java.awt.Color(2, 135, 242));
        Selecciona3.setForeground(new java.awt.Color(254, 254, 254));
        Selecciona3.setIcon(new ImageIcon("imagenes/add-user.png"));
        Selecciona3.setText("Nuevo");
        Selecciona3.setToolTipText("Agregar un registo actual");
        Selecciona3.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        Selecciona3.setMaximumSize(new java.awt.Dimension(87, 23));
        Selecciona3.setMinimumSize(new java.awt.Dimension(87, 23));
        Selecciona3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Selecciona3ActionPerformed(evt);
            }
        });

        Eliminar1.setBackground(new java.awt.Color(2, 135, 242));
        Eliminar1.setForeground(new java.awt.Color(254, 254, 254));
        Eliminar1.setIcon(new ImageIcon("imagenes/del-user.png"));
        Eliminar1.setText("Eliminar");
        Eliminar1.setToolTipText("Eliminar el registro actual");
        Eliminar1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        Eliminar1.setMaximumSize(new java.awt.Dimension(87, 23));
        Eliminar1.setMinimumSize(new java.awt.Dimension(87, 23));
        Eliminar1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Eliminar1ActionPerformed(evt);
            }
        });

        bt_actualiza1.setBackground(new java.awt.Color(2, 135, 242));
        bt_actualiza1.setForeground(new java.awt.Color(254, 254, 254));
        bt_actualiza1.setIcon(new ImageIcon("imagenes/tabla.png"));
        bt_actualiza1.setText("Actualizar");
        bt_actualiza1.setToolTipText("Actualizar los datos de la tabla");
        bt_actualiza1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        bt_actualiza1.setMaximumSize(new java.awt.Dimension(87, 23));
        bt_actualiza1.setMinimumSize(new java.awt.Dimension(87, 23));
        bt_actualiza1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bt_actualiza1ActionPerformed(evt);
            }
        });

        bt_actualiza2.setBackground(new java.awt.Color(2, 135, 242));
        bt_actualiza2.setForeground(new java.awt.Color(254, 254, 254));
        bt_actualiza2.setIcon(new ImageIcon("imagenes/guardar-documento.png"));
        bt_actualiza2.setText("Exportar");
        bt_actualiza2.setToolTipText("Actualizar los datos de la tabla");
        bt_actualiza2.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        bt_actualiza2.setMaximumSize(new java.awt.Dimension(87, 23));
        bt_actualiza2.setMinimumSize(new java.awt.Dimension(87, 23));
        bt_actualiza2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bt_actualiza2ActionPerformed(evt);
            }
        });

        jLabel1.setText("Buscar:");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(t_busca, javax.swing.GroupLayout.PREFERRED_SIZE, 319, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(b_busca, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 235, Short.MAX_VALUE)
                        .addComponent(Selecciona3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(bt_actualiza1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(bt_actualiza2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Eliminar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(b_busca, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel1)
                        .addComponent(t_busca, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(Eliminar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(bt_actualiza1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(bt_actualiza2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(Selecciona3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 431, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void bt_actualiza1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bt_actualiza1ActionPerformed
        buscaDato();
    }//GEN-LAST:event_bt_actualiza1ActionPerformed

    private void Eliminar1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Eliminar1ActionPerformed
        h=new Herramientas(usr, 0);
        h.session(sessionPrograma);
        if(this.t_datos.getSelectedRow()>=0)
        {
            DefaultTableModel model = (DefaultTableModel) t_datos.getModel();
            int a = t_datos.getSelectedRow();
            int opt=JOptionPane.showConfirmDialog(this, "¡Los datos capturados se eliminarán!");
            if (JOptionPane.YES_OPTION == opt)
            {
                boolean respuesta=elimina(t_datos.getValueAt(t_datos.getSelectedRow(), 0).toString());
                if(respuesta==true)
                { 
                    JOptionPane.showMessageDialog(null, "Registro eliminado");
                    buscaDato();
                }
            }
            else
            {
                if (JOptionPane.NO_OPTION== opt)
                    JOptionPane.showMessageDialog(null, "¡Selecciona la marca correcta para eliminar!");  
            }  
        }
        else
        {
            JOptionPane.showMessageDialog(null, "¡Selecciona la marca que desees eliminar!");
        }
    }//GEN-LAST:event_Eliminar1ActionPerformed

    private void Selecciona3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Selecciona3ActionPerformed
        h=new Herramientas(usr, 0);
        h.session(sessionPrograma);
        altaMarca obj = new altaMarca(new javax.swing.JFrame(), true, usr, sessionPrograma);
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        obj.setLocation((d.width/2)-(obj.getWidth()/2), (d.height/2)-(obj.getHeight()/2));
        obj.setVisible(true);
        buscaDato();
    }//GEN-LAST:event_Selecciona3ActionPerformed

    private void bt_actualiza2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bt_actualiza2ActionPerformed
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
                    Sheet hoja = libro.createSheet("Clientes");
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
                    JOptionPane.showMessageDialog(this, "No se pudo realizar el reporte");
                }
            }
        }
    }//GEN-LAST:event_bt_actualiza2ActionPerformed

    private void t_buscaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_t_buscaActionPerformed
        // TODO add your handling code here:
        if(this.t_busca.getText().compareToIgnoreCase("")!=0)
        {
            //buscaCuentas();
            if(x>=t_datos.getRowCount())
            {
                x=0;
                java.awt.Rectangle r = t_datos.getCellRect( x, 1, true);
                t_datos.scrollRectToVisible(r);
            }
            for(; x<t_datos.getRowCount(); x++)
            {
                if(t_datos.getValueAt(x, 1).toString().indexOf(t_busca.getText().toUpperCase()) != -1)
                {
                    t_datos.setRowSelectionInterval(x, x);
                    t_datos.setColumnSelectionInterval(1, 1);
                    java.awt.Rectangle r = t_datos.getCellRect( x, 1, true);
                    t_datos.scrollRectToVisible(r);
                    break;
                }
            }
            x++;
        }
    }//GEN-LAST:event_t_buscaActionPerformed

    private void b_buscaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_buscaActionPerformed
        // TODO add your handling code here:
        if(this.t_busca.getText().compareToIgnoreCase("")!=0)
        {
            //buscaCuentas();
            if(x>=t_datos.getRowCount())
            {
                x=0;
                java.awt.Rectangle r = t_datos.getCellRect( x, 1, true);
                t_datos.scrollRectToVisible(r);
            }
            for(; x<t_datos.getRowCount(); x++)
            {
                if(t_datos.getValueAt(x, 1).toString().indexOf(t_busca.getText().toUpperCase()) != -1)
                {
                    t_datos.setRowSelectionInterval(x, x);
                    t_datos.setColumnSelectionInterval(1, 1);
                    java.awt.Rectangle r = t_datos.getCellRect( x, 1, true);
                    t_datos.scrollRectToVisible(r);
                    break;
                }
            }
            x++;
        }
    }//GEN-LAST:event_b_buscaActionPerformed

    private void t_prefijoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_prefijoKeyTyped
        // TODO add your handling code here:
        char car = evt.getKeyChar();
        evt.setKeyChar(Character.toUpperCase(evt.getKeyChar()));
        if(t_prefijo.getText().length()>=3)
            evt.consume();
    }//GEN-LAST:event_t_prefijoKeyTyped

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Eliminar1;
    private javax.swing.JButton Selecciona3;
    private javax.swing.JButton b_busca;
    private javax.swing.JButton bt_actualiza1;
    private javax.swing.JButton bt_actualiza2;
    public javax.swing.JComboBox cb_inventario;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField t_busca;
    private javax.swing.JTable t_datos;
    private javax.swing.JTextField t_prefijo;
    // End of variables declaration//GEN-END:variables

    private List<Object[]> executeHQLQuery(String hql) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            session.beginTransaction();
            Query q = session.createQuery(hql);
            List resultList = q.list();
            session.getTransaction().commit();
            session.disconnect();
            return resultList;
        } catch (HibernateException he) {
            he.printStackTrace();
            List lista= null;
            return lista;
        }
        finally
        {
            if(session.isOpen())
            session.close();
        }
    }
    
    private void buscaDato()
    {
        String consulta="from Marca";
        List <Object[]> resultList=executeHQLQuery(consulta);
        if(resultList.size()>0)
        {
            t_datos.setModel(ModeloTablaReporte(resultList.size(), columnas));
            int i=0;
            for (Object o : resultList)
            {
                Marca actor = (Marca) o;
                model.setValueAt(actor.getIdMarca(), i, 0);
                model.setValueAt(actor.getMarcaNombre(), i, 1);
                String valor = "";
                switch(actor.getEjemplar())
                {
                    case 0:
                        valor="Unidad";
                        break;
                    case 1:
                        valor="Inventario";
                        break;
                    case 2:
                        valor="Ambos";
                        break;
                }
                model.setValueAt(valor, i, 2);
                i++;
            }
        }else
            t_datos.setModel(ModeloTablaReporte(0, columnas));
        formatoTabla();
    }
    
    private boolean elimina(String idMarca)
    {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try
        {
            session.beginTransaction();
            actor = (Marca)session.get(Marca.class, idMarca);
            
            if(actor.getOrdens().isEmpty()==false || 
            actor.getEjemplars().isEmpty()==false)
            {
                session.getTransaction().rollback();
                JOptionPane.showMessageDialog(null, "¡La marca esta en uso en una orden, no se puede eliminar!");
                return false;
            }
            else
            {
                session.delete(actor);
                session.getTransaction().commit();
                return true;
            }
        }catch(Exception e)
        {
            e.printStackTrace();
            session.getTransaction().rollback();
            return false;
        }
        finally
        {
            if(session.isOpen())
                session.close();
        }
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
                      column.setPreferredWidth(10);
                      DefaultCellEditor miEditor1 = new DefaultCellEditor(t_prefijo);
                      miEditor1.setClickCountToStart(2);
                      column.setCellEditor(miEditor1);
                      break;
                  case 1:
                      column.setPreferredWidth(150);
                      break;                 
                  case 2:
                      column.setPreferredWidth(40);
                      DefaultCellEditor miEditor = new DefaultCellEditor(cb_inventario);
                      miEditor.setClickCountToStart(2);
                      column.setCellEditor(miEditor);
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