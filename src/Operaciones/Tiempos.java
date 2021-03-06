package Operaciones;

import Hibernate.Util.HibernateUtil;
import Integral.ExtensionFileFilter;
import Hibernate.entidades.Configuracion;
import Hibernate.entidades.Orden;
import Hibernate.entidades.Usuario;
import Integral.Render1;
import Integral.calendario;
import Servicios.buscaOrden;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import javax.swing.ImageIcon;
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

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author I.S.C.Salvador
 */
public class Tiempos extends javax.swing.JPanel {

    private Usuario usr;
    private int ventana;
    private String sessionPrograma;
    private int configuracion;
    private DefaultTableModel model3;
    private String[] columna = new String[] {"id", "Empleado", "Descripcion","Monto", "Aut"};
    /**
     * Creates new form Tiempos
     */
    public Tiempos(Usuario usr, int ventana, String sessionPrograma, int configuracion) {
        initComponents();
        this.usr=usr;
        this.ventana=ventana;
        this.sessionPrograma=sessionPrograma;
        this.configuracion=configuracion;
        DefaultTableModel modelo=(DefaultTableModel)t_datos.getModel();
        modelo.setRowCount(0);
        tabla_tamaños();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        t_fecha1 = new javax.swing.JTextField();
        b_fecha_siniestro = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        t_fecha2 = new javax.swing.JTextField();
        b_fecha_siniestro1 = new javax.swing.JButton();
        cb_supervisor = new javax.swing.JComboBox();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        t_datos = new javax.swing.JTable();
        b_buscar = new javax.swing.JButton();
        t_orden = new javax.swing.JTextField();
        b_busca_cliente1 = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();

        setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setText("Fecha Inicial:");

        t_fecha1.setEditable(false);
        t_fecha1.setBackground(new java.awt.Color(204, 255, 255));
        t_fecha1.setText("AAAA-MM-DD");
        t_fecha1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        t_fecha1.setEnabled(false);

        b_fecha_siniestro.setBackground(new java.awt.Color(2, 135, 242));
        b_fecha_siniestro.setIcon(new ImageIcon("imagenes/calendario.png"));
        b_fecha_siniestro.setToolTipText("Calendario");
        b_fecha_siniestro.setMaximumSize(new java.awt.Dimension(32, 8));
        b_fecha_siniestro.setMinimumSize(new java.awt.Dimension(32, 8));
        b_fecha_siniestro.setPreferredSize(new java.awt.Dimension(32, 8));
        b_fecha_siniestro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_fecha_siniestroActionPerformed(evt);
            }
        });

        jLabel2.setText("Fecha Final:");

        t_fecha2.setEditable(false);
        t_fecha2.setBackground(new java.awt.Color(204, 255, 255));
        t_fecha2.setText("AAAA-MM-DD");
        t_fecha2.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        t_fecha2.setEnabled(false);
        t_fecha2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                t_fecha2ActionPerformed(evt);
            }
        });

        b_fecha_siniestro1.setBackground(new java.awt.Color(2, 135, 242));
        b_fecha_siniestro1.setIcon(new ImageIcon("imagenes/calendario.png"));
        b_fecha_siniestro1.setToolTipText("Calendario");
        b_fecha_siniestro1.setMaximumSize(new java.awt.Dimension(32, 8));
        b_fecha_siniestro1.setMinimumSize(new java.awt.Dimension(32, 8));
        b_fecha_siniestro1.setPreferredSize(new java.awt.Dimension(32, 8));
        b_fecha_siniestro1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_fecha_siniestro1ActionPerformed(evt);
            }
        });

        cb_supervisor.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "TODOS", "LUIS", "GABRIEL", "OCTAVIO", "EDWIN", "MAURICIO" }));
        cb_supervisor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cb_supervisorActionPerformed(evt);
            }
        });

        jLabel3.setText("Supervisor:");

        t_datos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "EMPLEADO", "INICIO", "ASIGNADO", "ESTATUS", "ORDEN", "T.ADICIONAL", "TIEMPO", "ASIGNO", "ACTIVIDAD"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(t_datos);

        b_buscar.setText("Buscar");
        b_buscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_buscarActionPerformed(evt);
            }
        });

        t_orden.setEditable(false);
        t_orden.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                t_ordenActionPerformed(evt);
            }
        });

        b_busca_cliente1.setBackground(new java.awt.Color(2, 135, 242));
        b_busca_cliente1.setIcon(new ImageIcon("imagenes/buscar.png"));
        b_busca_cliente1.setToolTipText("Consultar clientes");
        b_busca_cliente1.setMaximumSize(new java.awt.Dimension(32, 8));
        b_busca_cliente1.setMinimumSize(new java.awt.Dimension(32, 8));
        b_busca_cliente1.setPreferredSize(new java.awt.Dimension(32, 8));
        b_busca_cliente1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_busca_cliente1ActionPerformed(evt);
            }
        });

        jButton1.setText("XLS");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(t_fecha1, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(1, 1, 1)
                        .addComponent(b_fecha_siniestro, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(t_fecha2, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(1, 1, 1)
                        .addComponent(b_fecha_siniestro1, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 51, Short.MAX_VALUE)
                        .addComponent(t_orden, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(b_busca_cliente1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(257, 257, 257)
                        .addComponent(b_buscar)
                        .addGap(19, 19, 19)
                        .addComponent(jButton1))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cb_supervisor, javax.swing.GroupLayout.PREFERRED_SIZE, 327, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(3, 3, 3)
                                        .addComponent(t_fecha1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(b_fecha_siniestro, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(5, 5, 5)
                                        .addComponent(jLabel2))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(3, 3, 3)
                                        .addComponent(t_fecha2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(b_fecha_siniestro1, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addComponent(jLabel1))
                            .addComponent(b_busca_cliente1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(b_buscar)
                                .addComponent(jButton1)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cb_supervisor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3)))
                    .addComponent(t_orden, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 434, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void b_fecha_siniestroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_fecha_siniestroActionPerformed
        // TODO add your handling code here
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
            t_fecha1.setText(anio+"-"+mes+"-"+dia);
            //b_busca_cliente.requestFocus();
        }
        else
        t_fecha1.setText("AAAA-MM-DD");
        DefaultTableModel modelo=(DefaultTableModel)t_datos.getModel();
        modelo.setRowCount(0);
        tabla_tamaños();
    }//GEN-LAST:event_b_fecha_siniestroActionPerformed

    private void t_fecha2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_t_fecha2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_t_fecha2ActionPerformed

    private void b_fecha_siniestro1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_fecha_siniestro1ActionPerformed
        // TODO add your handling code here:
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
            t_fecha2.setText(anio+"-"+mes+"-"+dia);
        }
        else
        t_fecha2.setText("AAAA-MM-DD");
        DefaultTableModel modelo=(DefaultTableModel)t_datos.getModel();
        modelo.setRowCount(0);
        tabla_tamaños();
    }//GEN-LAST:event_b_fecha_siniestro1ActionPerformed

    private void t_ordenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_t_ordenActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_t_ordenActionPerformed

    private void b_busca_cliente1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_busca_cliente1ActionPerformed
        // TODO add your handling code here:
        buscaOrden obj = new buscaOrden(new javax.swing.JFrame(), true, this.usr,0, configuracion);
        obj.t_busca.requestFocus();
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        obj.setLocation((d.width/2)-(obj.getWidth()/2), (d.height/2)-(obj.getHeight()/2));
        obj.setVisible(true);
        Orden orden_act=obj.getReturnStatus();
        if (orden_act!=null)
        this.t_orden.setText(""+orden_act.getIdOrden());
        else
        t_orden.setText("");
        DefaultTableModel modelo=(DefaultTableModel)t_datos.getModel();
        modelo.setRowCount(0);
        tabla_tamaños();
    }//GEN-LAST:event_b_busca_cliente1ActionPerformed

    private void b_buscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_buscarActionPerformed
        // TODO add your handling code here:
        DefaultTableModel modelo=(DefaultTableModel)t_datos.getModel();
        modelo.setRowCount(0);
        tabla_tamaños();
        
        String consulta="select id_asignacion as id, nombre, tiempo_inicio, STR_TO_DATE(tiempo_asignado, '%H:%i') as asignado, estatus, id_orden as orden, " +
                        "(select sec_to_time(SUM(time_to_sec(STR_TO_DATE(tiempo, '%H:%i')))) from tiempo_adicional where id_asignacion=id) as t_adicional, " +
                        "concat('',(select sec_to_time(SUM(time_to_sec(if(fin is not null, timediff(fin, inicio), timediff(now(), inicio))))) from tiempo where id_asignacion=id)) as tiempo, asignacion.id_usuario  \n" +
                        ", descripcion from asignacion inner join empleado on asignacion.id_empleado=empleado.id_empleado ";
        int entro=0;
        
        if(t_fecha1.getText().compareTo("AAAA-MM-DD")!=0)
        {
            consulta+="where asignacion.tiempo_inicio>='" +t_fecha1.getText() +"' ";
            entro=1;
        }
        if(t_fecha2.getText().compareTo("AAAA-MM-DD")!=0)
        {
            if(entro==1)
                consulta+="and ";
            else
                consulta+="where ";
            consulta+="asignacion.tiempo_inicio<='" +t_fecha2.getText() +" 23:59:59' ";
            entro=1;
        }
        if(t_orden.getText().compareTo("")!=0)
        {
            if(entro==1)
                consulta+="and ";
            else
                consulta+="where ";
            consulta+="asignacion.id_orden=" +t_orden.getText() +" ";
            entro=1;
        }
        if(cb_supervisor.getSelectedIndex()>0)
        {
            if(entro==1)
                consulta+="and ";
            else
                consulta+="where ";
            consulta+="asignacion.id_usuario='" +cb_supervisor.getSelectedItem().toString() +"' ";
            entro=1;
        }
                
        consulta+=" order by id desc;";
        
        Session session = HibernateUtil.getSessionFactory().openSession();
        try
        {
            System.out.println(consulta);
            session.beginTransaction().begin();
            Query query = session.createSQLQuery(consulta);
            query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
            ArrayList datos = (ArrayList) query.list();
            for(int z=0; z<datos.size(); z++){
                java.util.HashMap map = (java.util.HashMap) datos.get(z);
                String adicional="";
                if(map.get("t_adicional")!=null)
                    adicional=map.get("t_adicional").toString();
                modelo.addRow(new Object[]{
                    map.get("id").toString(),
                    map.get("nombre").toString(),
                    map.get("tiempo_inicio").toString(),
                    map.get("asignado").toString(),
                    map.get("estatus").toString(),
                    map.get("orden").toString(),
                    adicional,
                    map.get("tiempo").toString(),
                    map.get("id_usuario").toString(),map.get("descripcion").toString()});
            }
            session.beginTransaction().commit();
        }catch(Exception e){e.printStackTrace();}
        
        
    }//GEN-LAST:event_b_buscarActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        javax.swing.JFileChooser jF1= new javax.swing.JFileChooser(); 
        jF1.setFileFilter(new ExtensionFileFilter("Excel document (*.xls)", new String[] { "xls" }));
        String ruta = null; 
        /*List compradores=new ArrayList();
        compradores.lastIndexOf(h)*/
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
                    Sheet hoja = libro.createSheet("Tiempos");
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
    }//GEN-LAST:event_jButton1ActionPerformed

    private void cb_supervisorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cb_supervisorActionPerformed
        // TODO add your handling code here:
        DefaultTableModel modelo=(DefaultTableModel)t_datos.getModel();
        modelo.setRowCount(0);
        tabla_tamaños();
    }//GEN-LAST:event_cb_supervisorActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton b_busca_cliente1;
    public javax.swing.JButton b_buscar;
    private javax.swing.JButton b_fecha_siniestro;
    private javax.swing.JButton b_fecha_siniestro1;
    private javax.swing.JComboBox cb_supervisor;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable t_datos;
    private javax.swing.JTextField t_fecha1;
    private javax.swing.JTextField t_fecha2;
    private javax.swing.JTextField t_orden;
    // End of variables declaration//GEN-END:variables

void tabla_tamaños()
    {
        TableColumnModel col_model = t_datos.getColumnModel();
        for (int i=0; i<t_datos.getColumnCount(); i++)
        {
  	      TableColumn column = col_model.getColumn(i);
              switch(i)
              {
                  case 0:
                      column.setPreferredWidth(40);
                      break;
                  case 1:
                      column.setPreferredWidth(220);
                      break;
                  /*case 2:
                      column.setPreferredWidth(90);
                      break;    
                  case 3:
                      column.setPreferredWidth(90);
                      break;*/
                  default:
                      column.setPreferredWidth(90);
                      break;
              }
        }
        JTableHeader header = t_datos.getTableHeader();
        header.setForeground(Color.white);
        Color c1 = new java.awt.Color(2, 135, 242);
        for(int x=0; x<t_datos.getColumnModel().getColumnCount(); x++)
            t_datos.getColumnModel().getColumn(x).setHeaderRenderer(new Render1(c1));
        t_datos.setShowVerticalLines(true);
        t_datos.setShowHorizontalLines(true);
    }
}
