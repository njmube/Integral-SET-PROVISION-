/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Contabilidad;

import Hibernate.Util.HibernateUtil;
import Hibernate.entidades.ArchivoFactura;
import Hibernate.entidades.ArchivoNota;
import Hibernate.entidades.Asiento;
import Hibernate.entidades.Excel;
import Hibernate.entidades.Reclamo;
import Hibernate.entidades.Registro;
import Hibernate.entidades.Usuario;
import Hibernate.entidades.Cuentas;
import Hibernate.entidades.PagoReclamo;
import Integral.ExtensionFileFilter;
import Integral.FormatoTabla;
import Integral.Herramientas;
import Integral.Render1;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Vector;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Workbook;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author Sistemas
 */
public class Egresos extends javax.swing.JPanel {

    Usuario usr;
    int menu;
    String sessionPrograma="";
    Herramientas h;
    int i=0;
    String estado="";
    ArrayList lista=new ArrayList();
    FormatoTabla formato;
    DefaultTableModel model;
    String[] columnas = new String [] {
        "id","No Poliza","Fecha","Descripción","Reclamos"};
    /**
     * Creates new form provision
     */
    public Egresos(Usuario usuario, String ses, int op) {
        initComponents();
        menu=op;
        usr=usuario;
        sessionPrograma=ses;
        formato =new FormatoTabla();
        formatoTabla();
    }
    
    public void archivoExcel(String NoPoliza, String NoMes, String NoReclamo)
    {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try
        {
            Asiento[] Asientos= new Asiento[0];
            if(NoPoliza.compareTo("")!=0)
            {
                Query query1 = session.createQuery("SELECT DISTINCT reg FROM Asiento reg "
                    + "LEFT JOIN reg.excelPago ex "
                    + "where ex.poliza="+NoPoliza+" AND MONTH(ex.fecha)="+NoMes+" and ex.tipo='Eg' ORDER BY reg.idAsiento ASC");
                Asientos = (Asiento[])query1.list().toArray(new Asiento[0]);
            }
            else
            {
                Query query1 = session.createQuery("SELECT DISTINCT reg FROM Asiento reg "
                    + "LEFT JOIN reg.excelPago ex "
                    + "where reg.reclamo.idReclamo="+NoReclamo+"and ex.tipo='Eg' ORDER BY reg.idAsiento ASC");
                Asientos = (Asiento[])query1.list().toArray(new Asiento[0]);
            }
            if(Asientos.length>0)
            {
                Calendar calendario = Calendar.getInstance();
                calendario.setTime(Asientos[0].getExcelProvision().getFecha());
                javax.swing.JFileChooser jF1= new javax.swing.JFileChooser(); 
                jF1.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                String ruta = null; 
                if(jF1.showSaveDialog(null)==jF1.APPROVE_OPTION)
                { 
                    ruta = jF1.getSelectedFile().getAbsolutePath(); 
                    if(ruta!=null)
                    {
                        generaExcel(""+Asientos[0].getExcelProvision().getPoliza(), ""+(calendario.get(Calendar.MONTH)+1), ruta);
                        JOptionPane.showMessageDialog(this, "¡Listo!");
                    }
                }
            }
        }catch(Exception e)
        {
            session.beginTransaction().rollback();
            e.printStackTrace();
        }
        if(session!=null)
            if(session.isOpen())
                session.close();
    }
    public void buscaLista()
    {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try
        {
            Query query1 = session.createQuery("SELECT DISTINCT ex FROM Excel ex "
                        + "where MONTH(ex.fecha)="+cb_mes1.getSelectedItem()+" AND ex.tipo='Eg' ORDER BY poliza ASC");
            Excel[] listaPoliza = (Excel[])query1.list().toArray(new Excel[0]); 
            t_datos.setModel(ModeloTablaReporte(listaPoliza.length, columnas));
             for(int x=0; x<listaPoliza.length; x++)
            {
                t_datos.setValueAt(listaPoliza[x].getIdExcel(), x, 0);
                t_datos.setValueAt(listaPoliza[x].getPoliza(), x, 1);
                t_datos.setValueAt(listaPoliza[x].getFecha().toString(), x, 2);
                t_datos.setValueAt(listaPoliza[x].getConcepto(), x, 3);
                Asiento[] as=(Asiento[])listaPoliza[x].getAsientosForPago().toArray(new Asiento[0]);
                String cadena="";
                for(int a=0; a<as.length; a++)
                {
                    cadena+=""+as[a].getReclamo().getIdReclamo();
                }
                t_datos.setValueAt(cadena, x, 4);
            }
        }catch(Exception e)
        {
            session.beginTransaction().rollback();
            e.printStackTrace();
        }
        if(session!=null)
            if(session.isOpen())
                session.close();
    }
    public void generaExcel(String noPoliza, String noMes, String ruta)
    {
        DecimalFormat formatoPorcentaje = new DecimalFormat("#,##0.00");
        formatoPorcentaje.setMinimumFractionDigits(2);
        File archivoXLS = new File(ruta+"/"+noPoliza+"-"+noMes+"-Eg.xls");
        File plantilla = new File("imagenes/Diario.xls");
        Session session = HibernateUtil.getSessionFactory().openSession();
        try
        {
            Query query1 = session.createQuery("SELECT DISTINCT reg FROM Asiento reg "
                + "LEFT JOIN reg.excelPago ex "
                + "where ex.poliza="+noPoliza+" AND MONTH(ex.fecha)="+noMes+" AND ex.tipo='Eg' ORDER BY reg.idAsiento ASC");
            Asiento[] Asientos = (Asiento[])query1.list().toArray(new Asiento[0]);

            Path FROM = Paths.get("imagenes/Diario.xls");
            Path TO = Paths.get(ruta+"/"+noPoliza+"-"+noMes+"-Eg.xls");
            //sobreescribir el fichero de destino, si existe, y copiar los atributos, incluyendo los permisos rwx
            CopyOption[] options = new CopyOption[]
            {
                StandardCopyOption.REPLACE_EXISTING,
                StandardCopyOption.COPY_ATTRIBUTES
            }; 
            Files.copy(FROM, TO, options);

            FileInputStream miPlantilla = new FileInputStream(archivoXLS);
            POIFSFileSystem fsFileSystem = new POIFSFileSystem(miPlantilla);
            Workbook libro = new HSSFWorkbook(fsFileSystem);
            //Cargamos las cabeceras
            libro.getSheet("Hoja1").createRow(2);
            libro.getSheet("Hoja1").getRow(2).createCell(0).setCellValue("Eg");
            libro.getSheet("Hoja1").getRow(2).createCell(1).setCellValue(Integer.parseInt(noPoliza));
            if(Asientos.length>0)
            {
                libro.getSheet("Hoja1").getRow(2).createCell(2).setCellValue(Asientos[0].getExcelProvision().getConcepto());
                Calendar calendario = Calendar.getInstance();
                calendario.setTime(Asientos[0].getExcelProvision().getFecha());
                libro.getSheet("Hoja1").getRow(2).createCell(3).setCellValue(calendario.get(Calendar.DAY_OF_MONTH));
            }
            double total=0.0D;
            int renglon=3;
            for(int ren=0;ren<Asientos.length; ren++)
            {
                Registro[] registros = (Registro[])session.createCriteria(Registro.class).createAlias("asiento", "asc").add(Restrictions.eq("asc.idAsiento", Asientos[ren].getIdAsiento())).add(Restrictions.eq("tipoAsiento", "Eg")).addOrder(Order.desc("tipo")).addOrder(Order.asc("idRegistro")).list().toArray(new Registro[0]);
                for(int r=0; r<registros.length; r++)
                {
                    libro.getSheet("Hoja1").createRow(renglon);
                    libro.getSheet("Hoja1").getRow(renglon).createCell(1).setCellValue(registros[r].getCuentas().getIdCuentas());
                    libro.getSheet("Hoja1").getRow(renglon).createCell(2).setCellValue(Integer.parseInt(registros[r].getDepto()));
                    libro.getSheet("Hoja1").getRow(renglon).createCell(3).setCellValue(registros[r].getConcepto());
                    libro.getSheet("Hoja1").getRow(renglon).createCell(4).setCellValue(registros[r].getCambio());
                    if(registros[r].getTipo().compareTo("d")==0)
                        libro.getSheet("Hoja1").getRow(renglon).createCell(5).setCellValue(registros[r].getCantidad());
                    else
                        libro.getSheet("Hoja1").getRow(renglon).createCell(6).setCellValue(registros[r].getCantidad());
                    renglon++;
                }
            }
            int celda =renglon;
            libro.getSheet("Hoja1").createRow(renglon);
            libro.getSheet("Hoja1").getRow(renglon).createCell(1);//
            libro.getSheet("Hoja1").getRow(renglon).getCell(1).setCellValue("FIN_PARTIDAS");

            FileOutputStream archivo = new FileOutputStream(archivoXLS);
            libro.write(archivo);
            archivo.close();
            //Desktop.getDesktop().open(archivoXLS);
        }catch(Exception e)
        {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "No se pudo realizar el reporte");
        }
        if(session!=null)
            if(session.isOpen())
                session.close();
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
                      column.setPreferredWidth(20);
                      break;
                  case 1:
                      column.setPreferredWidth(20);
                      break;
                  case 3:
                      column.setPreferredWidth(200);
                      break;
                  case 4:
                      column.setPreferredWidth(300);
                      break;
                  default:
                      column.setPreferredWidth(100);
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
        t_datos.setDefaultRenderer(Double.class, formato); 
        t_datos.setDefaultRenderer(Integer.class, formato);
        t_datos.setDefaultRenderer(String.class, formato);
        t_datos.setDefaultRenderer(Boolean.class, formato);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        Menu = new javax.swing.JPopupMenu();
        Consultar = new javax.swing.JMenuItem();
        archivo = new javax.swing.JMenuItem();
        elimina = new javax.swing.JMenuItem();
        ventana_muestra = new javax.swing.JDialog();
        jScrollPane4 = new javax.swing.JScrollPane();
        panel_aux = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        t_poliza_aux = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        t_fecha_aux = new javax.swing.JTextField();
        jButton4 = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();
        t_concepto_aux = new javax.swing.JTextField();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        p_genera = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        t_mes = new javax.swing.JTextField();
        cb_poliza = new javax.swing.JComboBox();
        sp1 = new javax.swing.JScrollPane();
        panel_pago = new javax.swing.JPanel();
        p_consulta = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        panel_provision1 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        t_poliza1 = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        b_buscar1 = new javax.swing.JButton();
        b_xls1 = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        t_reclamo1 = new javax.swing.JTextField();
        cb_mes = new javax.swing.JComboBox();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jButton2 = new javax.swing.JButton();
        cb_mes1 = new javax.swing.JComboBox();
        jLabel6 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        t_datos = new javax.swing.JTable();

        Consultar.setText("Consultar");
        Consultar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ConsultarActionPerformed(evt);
            }
        });
        Menu.add(Consultar);

        archivo.setText("Archivo Excel");
        archivo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                archivoActionPerformed(evt);
            }
        });
        Menu.add(archivo);

        elimina.setText("Eliminar");
        elimina.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                eliminaActionPerformed(evt);
            }
        });
        Menu.add(elimina);

        ventana_muestra.setTitle("Consulta de Poliza");
        ventana_muestra.setModalityType(java.awt.Dialog.ModalityType.APPLICATION_MODAL);

        panel_aux.setBackground(new java.awt.Color(255, 255, 255));
        panel_aux.setLayout(new javax.swing.BoxLayout(panel_aux, javax.swing.BoxLayout.PAGE_AXIS));
        jScrollPane4.setViewportView(panel_aux);

        ventana_muestra.getContentPane().add(jScrollPane4, java.awt.BorderLayout.CENTER);

        jPanel5.setBackground(new java.awt.Color(2, 135, 242));

        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Poliza:");

        t_poliza_aux.setEditable(false);
        t_poliza_aux.setDisabledTextColor(new java.awt.Color(0, 51, 255));

        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("Fecha:");

        t_fecha_aux.setEditable(false);
        t_fecha_aux.setDisabledTextColor(new java.awt.Color(0, 51, 255));

        jButton4.setText("xls");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("Concepto Poliza:");

        t_concepto_aux.setNextFocusableComponent(jButton4);
        t_concepto_aux.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                t_concepto_auxFocusLost(evt);
            }
        });
        t_concepto_aux.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                t_concepto_auxActionPerformed(evt);
            }
        });
        t_concepto_aux.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                t_concepto_auxKeyTyped(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(t_poliza_aux, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(t_fecha_aux, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(t_concepto_aux, javax.swing.GroupLayout.DEFAULT_SIZE, 404, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton4)
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel7)
                .addComponent(t_poliza_aux, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jLabel8)
                .addComponent(t_fecha_aux, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jButton4)
                .addComponent(jLabel9)
                .addComponent(t_concepto_aux, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        ventana_muestra.getContentPane().add(jPanel5, java.awt.BorderLayout.NORTH);

        setBackground(new java.awt.Color(255, 255, 255));
        setLayout(new java.awt.BorderLayout());

        jTabbedPane1.setBackground(new java.awt.Color(255, 255, 255));

        p_genera.setLayout(new java.awt.BorderLayout());

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));

        jButton1.setText("Generar Polizas de Egresos");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton3.setText("xls");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jLabel1.setText("Poliza:");

        jLabel2.setText("Mes:");

        t_mes.setEditable(false);
        t_mes.setDisabledTextColor(new java.awt.Color(0, 0, 0));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton1)
                .addGap(142, 142, 142)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cb_poliza, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(t_mes, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton3)
                .addContainerGap(166, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton3)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2)
                    .addComponent(t_mes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cb_poliza, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        p_genera.add(jPanel3, java.awt.BorderLayout.NORTH);

        sp1.setBorder(null);

        panel_pago.setBackground(new java.awt.Color(255, 255, 255));
        panel_pago.setLayout(new javax.swing.BoxLayout(panel_pago, javax.swing.BoxLayout.PAGE_AXIS));
        sp1.setViewportView(panel_pago);

        p_genera.add(sp1, java.awt.BorderLayout.CENTER);

        jTabbedPane1.addTab("Generar", p_genera);

        p_consulta.setLayout(new java.awt.BorderLayout());

        jScrollPane2.setBorder(null);

        panel_provision1.setBackground(new java.awt.Color(255, 255, 255));
        panel_provision1.setLayout(new javax.swing.BoxLayout(panel_provision1, javax.swing.BoxLayout.PAGE_AXIS));
        jScrollPane2.setViewportView(panel_provision1);

        p_consulta.add(jScrollPane2, java.awt.BorderLayout.CENTER);

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));

        jLabel3.setText("Poliza:");

        t_poliza1.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        t_poliza1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                t_poliza1FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                t_poliza1FocusLost(evt);
            }
        });

        jLabel4.setText("Mes:");

        b_buscar1.setText("Buscar");
        b_buscar1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_buscar1ActionPerformed(evt);
            }
        });

        b_xls1.setText("xls");
        b_xls1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_xls1ActionPerformed(evt);
            }
        });

        jLabel5.setText("Reclamo:");

        t_reclamo1.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        t_reclamo1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                t_reclamo1FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                t_reclamo1FocusLost(evt);
            }
        });

        cb_mes.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12" }));

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(t_poliza1, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cb_mes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(19, 19, 19)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(t_reclamo1, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(b_buscar1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(b_xls1)
                .addContainerGap(321, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel5)
                        .addComponent(t_reclamo1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(b_buscar1)
                        .addComponent(b_xls1))
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(t_poliza1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel3)
                        .addComponent(jLabel4)
                        .addComponent(cb_mes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(14, Short.MAX_VALUE))
        );

        p_consulta.add(jPanel4, java.awt.BorderLayout.NORTH);

        jTabbedPane1.addTab("Buscar", p_consulta);

        jPanel1.setLayout(new java.awt.BorderLayout());

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        jButton2.setText("Buscar");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        cb_mes1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12" }));

        jLabel6.setText("Mes:");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cb_mes1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton2)
                .addContainerGap(623, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(cb_mes1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2))
                .addContainerGap(23, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel2, java.awt.BorderLayout.NORTH);

        t_datos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "id", "No Poliza", "Fecha", "Descripción", "Reclamos"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                true, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        t_datos.setComponentPopupMenu(Menu);
        t_datos.setFillsViewportHeight(true);
        t_datos.setRowHeight(15);
        t_datos.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        t_datos.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(t_datos);

        jPanel1.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        jTabbedPane1.addTab("Polizas de Egresos", jPanel1);

        add(jTabbedPane1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
         Session session = HibernateUtil.getSessionFactory().openSession();
        try
        {
            ArrayList listaConsulta=new ArrayList();
            ArrayList listaProveedor=new ArrayList();
            session.beginTransaction().begin();
            Query query_proveedor = session.createSQLQuery("select distinct reclamo.id_proveedor as id, proveedor.cta_prov as cta FROM reclamo LEFT JOIN asiento on reclamo.id_reclamo=asiento.id_reclamo left join proveedor on proveedor.id_proveedor=reclamo.id_proveedor where asiento.excel_pago is null;");
            query_proveedor.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
            listaProveedor=(ArrayList)query_proveedor.list();

            lista = new ArrayList();
            cb_poliza.removeAllItems();
            cb_poliza.addItem("TODAS");
            t_mes.setText("");

            panel_pago.removeAll();
            panel_pago.repaint();
            panel_pago.updateUI();
                
            for(int li=0; li<listaProveedor.size(); li++)
            {
                Query qr=session.createSQLQuery("select if(max(poliza)+1 is null, 1, max(poliza)+1) as poliza from excel where year(fecha)=year(now()) and month(fecha)=month(now()) and tipo='Eg';");
                qr.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
                listaConsulta=(ArrayList)qr.list();
                java.util.HashMap poliza=(java.util.HashMap)listaConsulta.get(0);
                
                java.util.HashMap proveedor=(java.util.HashMap)listaProveedor.get(li);
                Query query = session.createQuery("SELECT DISTINCT asi FROM Asiento asi LEFT JOIN asi.reclamo rec where asi.usuarioPago is null AND rec.pagoReclamo is not null AND rec.proveedor.idProveedor="+proveedor.get("id").toString());
                Asiento[] lista_asientos = (Asiento[])query.list().toArray(new Asiento[0]);
                System.out.println(lista_asientos.length);
                if(lista_asientos.length>0)
                {
                    cb_poliza.addItem(poliza.get("poliza").toString());
                    Date fecha = new Date();
                    Calendar calendario = Calendar.getInstance();
                    t_mes.setText(""+(calendario.get(Calendar.MONTH)+1));
                        
                    Excel excel=new Excel();
                    for (i=0; i<lista_asientos.length; i++) 
                    {
                        Registro[] registros_d = (Registro[])session.createCriteria(Registro.class).createAlias("asiento", "asc").add(Restrictions.eq("asc.idAsiento", lista_asientos[i].getIdAsiento())).add(Restrictions.eq("tipoAsiento", "Dr")).add(Restrictions.eq("tipo", "d")).addOrder(Order.desc("tipo")).addOrder(Order.asc("idRegistro")).list().toArray(new Registro[0]);
                        Registro[] registros_a = (Registro[])session.createCriteria(Registro.class).createAlias("asiento", "asc").add(Restrictions.eq("asc.idAsiento", lista_asientos[i].getIdAsiento())).add(Restrictions.eq("tipoAsiento", "Dr")).add(Restrictions.eq("tipo", "a")).add(Restrictions.like("cuentas.idCuentas", "2110-", MatchMode.START)).addOrder(Order.desc("tipo")).addOrder(Order.asc("idRegistro")).list().toArray(new Registro[0]);
                        usr=(Usuario)session.get(Usuario.class, usr.getIdUsuario());
                        
                        ArchivoFactura factura = (ArchivoFactura)session.createCriteria(ArchivoFactura.class).createAlias("reclamo", "rec").add(Restrictions.eq("rec.idReclamo", lista_asientos[i].getReclamo().getIdReclamo())).add(Restrictions.eq("estatus", "v")).uniqueResult();
                        ArchivoNota nota = (ArchivoNota)session.createCriteria(ArchivoNota.class).createAlias("reclamo", "rec").add(Restrictions.eq("rec.idReclamo", lista_asientos[i].getReclamo().getIdReclamo())).add(Restrictions.eq("estatus", "v")).uniqueResult();

                        Cuentas gastos_pagados = (Cuentas)session.createCriteria(Cuentas.class).add(Restrictions.eq("idCuentas", "6100-000-000")).uniqueResult();
                        Cuentas iva_gastos_16 = (Cuentas)session.createCriteria(Cuentas.class).add(Restrictions.eq("idCuentas", "1191-005-000")).uniqueResult();
                        Cuentas comp_pag_16 = (Cuentas)session.createCriteria(Cuentas.class).add(Restrictions.eq("idCuentas", "5200-002-000")).uniqueResult();
                        Cuentas iva_comp_16 = (Cuentas)session.createCriteria(Cuentas.class).add(Restrictions.eq("idCuentas", "1191-004-000")).uniqueResult();
                        Cuentas cuenta_proveedor=(Cuentas)session.get(Cuentas.class, proveedor.get("cta").toString());
                        PagoReclamo pagoReclamo=lista_asientos[i].getReclamo().getPagoReclamo();
                        //sacar la cuenta de pago reclamo agregar liga
                        Cuentas bancos = (Cuentas)session.createCriteria(Cuentas.class).add(Restrictions.eq("idCuentas", "1120-001-000")).uniqueResult();
                        

                        if(i==0)//Registro del excel
                        {
                            excel.setPoliza(Integer.parseInt(poliza.get("poliza").toString()));
                            excel.setFecha(fecha);
                            excel.setConcepto("Pago a proveedores");
                            excel.setTipo("Eg");
                            session.save(excel);
                        }
                        //Registro de Asiento
                        Asiento asiento=(Asiento)session.get(Asiento.class, lista_asientos[i].getIdAsiento());
                        asiento.setFechaPago(fecha);
                        asiento.setUsuarioPago(usr);
                        asiento.setExcelPago(excel);
                        session.update(asiento);

                        //***************Registro de Asientos************************
                        double suma=0.0d;
                        for(int h=0; h<registros_d.length; h++)
                        {
                            Cuentas aux=(Cuentas)session.get(Cuentas.class, registros_d[h].getCuentas().getIdCuentas());
                            if(aux.getIdCuentas().compareTo(cuenta_proveedor.getIdCuentas())!=0)
                            {
                                Registro registro=new Registro();
                                registro.setAsiento(asiento);
                                registro.setCuentas(aux);
                                registro.setDepto("0");
                                registro.setConcepto(aux.getNombre());
                                registro.setCantidad(BigDecimal.valueOf(registros_d[h].getCantidad()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
                                registro.setCambio(registros_d[h].getCambio());
                                registro.setTipo("a");
                                registro.setTipoAsiento("Eg");
                                session.save(registro);
                                
                                if(aux.getIdCuentas().compareTo("5300-002-000")==0)
                                {
                                    Registro reg_compra_pago_16=new Registro();
                                    reg_compra_pago_16.setAsiento(asiento);
                                    reg_compra_pago_16.setCuentas(comp_pag_16);
                                    reg_compra_pago_16.setDepto("0");
                                    reg_compra_pago_16.setConcepto(comp_pag_16.getNombre());
                                    reg_compra_pago_16.setCantidad(BigDecimal.valueOf(registros_d[h].getCantidad()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
                                    reg_compra_pago_16.setCambio(registros_d[h].getCambio());
                                    reg_compra_pago_16.setTipo("d");
                                    reg_compra_pago_16.setTipoAsiento("Eg");
                                    session.save(reg_compra_pago_16);
                                    
                                    Registro reg_iva_compra_pago_16=new Registro();
                                    reg_iva_compra_pago_16.setAsiento(asiento);
                                    reg_iva_compra_pago_16.setCuentas(iva_comp_16);
                                    reg_iva_compra_pago_16.setDepto("0");
                                    reg_iva_compra_pago_16.setConcepto(iva_comp_16.getNombre());
                                    reg_iva_compra_pago_16.setCantidad(BigDecimal.valueOf(registros_d[h].getCantidad()*0.16d).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
                                    reg_iva_compra_pago_16.setCambio(registros_d[h].getCambio());
                                    reg_iva_compra_pago_16.setTipo("d");
                                    reg_iva_compra_pago_16.setTipoAsiento("Eg");
                                    session.save(reg_iva_compra_pago_16);
                                }
                                else
                                {
                                    if(aux.getIdCuentas().contains("1190-")==false){
                                        suma+=registros_d[h].getCantidad();
                                    }
                                }
                            }
                        }
                        for(int g=0; g<registros_a.length; g++)
                        {
                            System.out.println(registros_a[g].getConcepto());
                            Cuentas prov=(Cuentas)session.get(Cuentas.class, registros_a[g].getCuentas().getIdCuentas());
                            Registro reg_proveedor=new Registro();
                            reg_proveedor.setAsiento(asiento);
                            reg_proveedor.setCuentas(prov);
                            reg_proveedor.setDepto("0");
                            reg_proveedor.setConcepto(prov.getNombre());
                            reg_proveedor.setCantidad(BigDecimal.valueOf(pagoReclamo.getCantidad()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
                            reg_proveedor.setCambio(1.00d);
                            reg_proveedor.setTipo("d");
                            reg_proveedor.setTipoAsiento("Eg");
                            session.save(reg_proveedor);

                            Registro reg_bancos=new Registro();
                            reg_bancos.setAsiento(asiento);
                            reg_bancos.setCuentas(bancos);
                            reg_bancos.setDepto("0");
                            reg_bancos.setConcepto("BANCOS");
                            reg_bancos.setCantidad(BigDecimal.valueOf(pagoReclamo.getCantidad()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
                            reg_bancos.setCambio(1.00d);
                            reg_bancos.setTipo("a");
                            reg_bancos.setTipoAsiento("Eg");
                            session.save(reg_bancos);
                        }
                        if(suma>0.0d)
                        {
                            Registro reg_gasto=new Registro();
                            reg_gasto.setAsiento(asiento);
                            reg_gasto.setCuentas(gastos_pagados);
                            reg_gasto.setDepto("0");
                            reg_gasto.setConcepto(gastos_pagados.getNombre());
                            reg_gasto.setCantidad(BigDecimal.valueOf(suma).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
                            reg_gasto.setCambio(1.00d);
                            reg_gasto.setTipo("d");
                            reg_gasto.setTipoAsiento("Eg");
                            session.save(reg_gasto);

                            Registro reg_iva_gasto=new Registro();
                            reg_iva_gasto.setAsiento(asiento);
                            reg_iva_gasto.setCuentas(iva_gastos_16);
                            reg_iva_gasto.setDepto("0");
                            reg_iva_gasto.setConcepto(iva_gastos_16.getNombre());
                            reg_iva_gasto.setCantidad(BigDecimal.valueOf(suma*0.16d).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
                            reg_iva_gasto.setCambio(1.00d);
                            reg_iva_gasto.setTipo("d");
                            reg_iva_gasto.setTipoAsiento("Eg");
                            session.save(reg_iva_gasto);
                        }
                        lista.add(asiento);
                    }
                }
            }
            session.beginTransaction().commit();
            for(int l=0; l<lista.size(); l++)
            {
                Asiento aux=(Asiento)lista.get(l);
                asiento1 m=new asiento1(this.usr, aux, i, estado, this.sessionPrograma, "Eg");
                panel_pago.add(m);
                m.setVisible(true);
            }
            panel_pago.setAutoscrolls(true);
            panel_pago.repaint();
            panel_pago.updateUI();
        }catch(Exception e)
        {
            session.beginTransaction().rollback();
            cb_poliza.removeAllItems();
            t_mes.setText("");
            panel_pago.removeAll();
            panel_pago.setAutoscrolls(true);
            panel_pago.repaint();
            panel_pago.updateUI();
            e.printStackTrace();
        }
        if(session!=null)
            if(session.isOpen())
                session.close();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        if(cb_poliza.getItemCount()>1 && t_mes.getText().compareTo("")!=0)
        {
            javax.swing.JFileChooser jF1= new javax.swing.JFileChooser(); 
            jF1.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            String ruta = null; 
            DecimalFormat formatoPorcentaje = new DecimalFormat("#,##0.00");
            formatoPorcentaje.setMinimumFractionDigits(2);
            if(jF1.showSaveDialog(null)==jF1.APPROVE_OPTION)
            { 
                ruta = jF1.getSelectedFile().getAbsolutePath(); 
                if(ruta!=null)
                {
                    if(cb_poliza.getSelectedItem().toString().compareTo("TODAS")!=0)
                        generaExcel(cb_poliza.getSelectedItem().toString(), t_mes.getText(), ruta);
                    else
                    {
                        for(int op=1; op<cb_poliza.getItemCount(); op++)
                            generaExcel(cb_poliza.getItemAt(op).toString(), t_mes.getText(), ruta);
                    }
                    JOptionPane.showMessageDialog(this, "¡Listo!");
                }
            }
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void b_buscar1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_buscar1ActionPerformed
        if(t_poliza1.getText().compareTo("")!=0 || t_reclamo1.getText().compareTo("")!=0)
        {
            Session session = HibernateUtil.getSessionFactory().openSession();
            try
            {
                Asiento[] Asientos= new Asiento[0];
                if(t_poliza1.getText().compareTo("")!=0)
                {
                    Query query1 = session.createQuery("SELECT DISTINCT reg FROM Asiento reg "
                        + "LEFT JOIN reg.excelPago ex "
                        + "where ex.poliza="+t_poliza1.getText()+" AND MONTH(ex.fecha)="+cb_mes.getSelectedItem()+" AND ex.tipo='Eg' ORDER BY reg.idAsiento ASC");
                    Asientos = (Asiento[])query1.list().toArray(new Asiento[0]);
                }
                else
                {
                    Query query1 = session.createQuery("SELECT DISTINCT reg FROM Asiento reg "
                            + "LEFT JOIN reg.excelPago ex "
                        + "where reg.reclamo.idReclamo="+t_reclamo1.getText()+" AND ex.tipo='Eg' ORDER BY reg.idAsiento ASC");
                    Asientos = (Asiento[])query1.list().toArray(new Asiento[0]);
                }
                panel_provision1.removeAll();
                panel_provision1.repaint();
                panel_provision1.updateUI();
                for(int e=0; e<Asientos.length; e++)
                {
                    asiento1 m=new asiento1(this.usr, Asientos[e], i, estado, this.sessionPrograma,"Eg");
                    panel_provision1.add(m);
                    m.setVisible(true);
                }
                panel_provision1.setAutoscrolls(true);
                panel_provision1.repaint();
                panel_provision1.updateUI();
            }catch(Exception e)
            {
                session.beginTransaction().rollback();
                cb_poliza.removeAllItems();
                t_mes.setText("");
                panel_provision1.removeAll();
                panel_provision1.setAutoscrolls(true);
                panel_provision1.repaint();
                panel_provision1.updateUI();
                e.printStackTrace();
            }
            if(session!=null)
                if(session.isOpen())
                    session.close();
        }
    }//GEN-LAST:event_b_buscar1ActionPerformed

    private void b_xls1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_xls1ActionPerformed
        // TODO add your handling code here:
        if(t_poliza1.getText().compareTo("")!=0 || t_reclamo1.getText().compareTo("")!=0)
        {
            archivoExcel(t_poliza1.getText(), cb_mes.getSelectedItem().toString(), t_reclamo1.getText());
        }
    }//GEN-LAST:event_b_xls1ActionPerformed

    private void t_poliza1FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_t_poliza1FocusLost
        // TODO add your handling code here:
        try{
            Integer.parseInt(t_poliza1.getText());
        }catch(Exception e){
            t_poliza1.setText("");
        }
    }//GEN-LAST:event_t_poliza1FocusLost

    private void t_reclamo1FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_t_reclamo1FocusLost
        // TODO add your handling code here:
        try{
            Integer.parseInt(t_reclamo1.getText());
        }catch(Exception e){
            t_reclamo1.setText("");
        }
    }//GEN-LAST:event_t_reclamo1FocusLost

    private void t_poliza1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_t_poliza1FocusGained
        // TODO add your handling code here:
        t_reclamo1.setText("");
    }//GEN-LAST:event_t_poliza1FocusGained

    private void t_reclamo1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_t_reclamo1FocusGained
        // TODO add your handling code here:
        t_poliza1.setText("");
    }//GEN-LAST:event_t_reclamo1FocusGained

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        buscaLista();
        formatoTabla();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void ConsultarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ConsultarActionPerformed
        // TODO add your handling code here:
        if(t_datos.getSelectedRow()>-1)
        {
            t_poliza_aux.setText(t_datos.getValueAt(t_datos.getSelectedRow(), 1).toString());
            t_fecha_aux.setText(t_datos.getValueAt(t_datos.getSelectedRow(), 2).toString());
            t_concepto_aux.setText(t_datos.getValueAt(t_datos.getSelectedRow(), 3).toString());
            Session session = HibernateUtil.getSessionFactory().openSession();
            try
            {
                Asiento[] Asientos= new Asiento[0];
                Query query1 = session.createQuery("SELECT DISTINCT reg FROM Asiento reg "
                        + "LEFT JOIN reg.excelPago ex "
                        + "where ex.idExcel="+t_datos.getValueAt(t_datos.getSelectedRow(), 0).toString()+" ORDER BY reg.idAsiento ASC");
                Asientos = (Asiento[])query1.list().toArray(new Asiento[0]);
                panel_aux.removeAll();
                panel_aux.setAutoscrolls(true);
                panel_aux.repaint();
                panel_aux.updateUI();
                for(int e=0; e<Asientos.length; e++)
                {
                    asiento1 m=new asiento1(this.usr, Asientos[e], i, estado, this.sessionPrograma,"Eg");
                    m.setVisible(true);
                    panel_aux.add(m);
                }
                panel_aux.setAutoscrolls(true);
                panel_aux.repaint();
                panel_aux.updateUI();
            }catch(Exception e)
            {
                session.beginTransaction().rollback();
                panel_aux.removeAll();
                panel_aux.setAutoscrolls(true);
                panel_aux.repaint();
                panel_aux.updateUI();
                e.printStackTrace();
            }
            if(session!=null)
                if(session.isOpen())
                    session.close();
            Dimension d = Toolkit.getDefaultToolkit().getScreenSize(); 
            ventana_muestra.setSize(800, 500);
            ventana_muestra.setLocation((d.width/2)-400, (d.height/2)-250);
            ventana_muestra.setVisible(true);
            buscaLista();
            formatoTabla();
        }
    }//GEN-LAST:event_ConsultarActionPerformed

    private void archivoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_archivoActionPerformed
        // TODO add your handling code here:
        if(t_datos.getSelectedRow()>-1)
        {
            String mi=t_datos.getValueAt(t_datos.getSelectedRow(), 2).toString().split("-")[1];
            archivoExcel(t_datos.getValueAt(t_datos.getSelectedRow(), 1).toString(), mi, "");
        }
    }//GEN-LAST:event_archivoActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        String mi=t_fecha_aux.getText().split("-")[1];
        archivoExcel(t_poliza_aux.getText(), mi, "");
    }//GEN-LAST:event_jButton4ActionPerformed

    private void t_concepto_auxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_t_concepto_auxActionPerformed
        // TODO add your handling code here:
        this.jButton4.requestFocus();
    }//GEN-LAST:event_t_concepto_auxActionPerformed

    private void t_concepto_auxKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_concepto_auxKeyTyped
        // TODO add your handling code here:
        char car = evt.getKeyChar();
        evt.setKeyChar(Character.toUpperCase(evt.getKeyChar()));
        if(t_concepto_aux.getText().length()>=119) 
            evt.consume();
    }//GEN-LAST:event_t_concepto_auxKeyTyped

    private void t_concepto_auxFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_t_concepto_auxFocusLost
        // TODO add your handling code here:
        Session session = HibernateUtil.getSessionFactory().openSession();
        try
        {
            session.beginTransaction().begin();
            String mi=t_fecha_aux.getText().split("-")[1];
            Query query1 = session.createQuery("SELECT DISTINCT ex FROM Excel ex "
                 + "where ex.poliza="+t_poliza_aux.getText()+" AND MONTH(ex.fecha)="+mi);
            Excel poliza = (Excel)query1.uniqueResult();
            poliza.setConcepto(t_concepto_aux.getText());
            session.update(poliza);
            session.beginTransaction().commit();
        }catch(Exception e)
        {
            session.beginTransaction().rollback();
            e.printStackTrace();
        }
        finally{
        if(session!=null)
            if(session.isOpen())
                session.close();
            JOptionPane.showMessageDialog(this, "Concepto Actualizado");
        }
    }//GEN-LAST:event_t_concepto_auxFocusLost

    private void eliminaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_eliminaActionPerformed
        // TODO add your handling code here:
        if(t_datos.getSelectedRow()>-1)
        {
            int opt=JOptionPane.showConfirmDialog(this, "¡Confirma que deseas eliminar la Poliza!");
            if(opt==0)
            {
                Session session = HibernateUtil.getSessionFactory().openSession();
                try
                {
                    session.beginTransaction().begin();
                    Excel poliza = (Excel)session.get(Excel.class, Integer.parseInt(t_datos.getValueAt(t_datos.getSelectedRow(), 0).toString()));
                    Asiento[] lista=(Asiento[])poliza.getAsientosForPago().toArray(new Asiento[0]);
                    for(int p=0; p<lista.length; p++)
                    {
                        Asiento aux=(Asiento)session.get(Asiento.class, lista[p].getIdAsiento());
                        Registro[] registros = (Registro[])session.createCriteria(Registro.class).createAlias("asiento", "asc").add(Restrictions.eq("asc.idAsiento", aux.getIdAsiento())).add(Restrictions.eq("tipoAsiento", "Eg")).addOrder(Order.desc("tipo")).addOrder(Order.asc("idRegistro")).list().toArray(new Registro[0]);
                        for(int y=0; y<registros.length; y++)
                        {
                            session.delete(registros[y]);
                        }
                        aux.setFechaPago(null);
                        aux.setUsuarioPago(null);
                        aux.setExcelPago(null);
                        session.update(aux);
                    }
                    session.delete(poliza);
                    session.beginTransaction().commit();
                    buscaLista();
                    formatoTabla();
                }catch(Exception e)
                {
                    session.beginTransaction().rollback();
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Error al eliminar la poliza");
                }
                finally{
                if(session!=null)
                    if(session.isOpen())
                        session.close();
                }
            }
        }
        else
        {
            JOptionPane.showMessageDialog(this, "Debes seleccionar primero una poliza de la lista");
        }
    }//GEN-LAST:event_eliminaActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem Consultar;
    private javax.swing.JPopupMenu Menu;
    private javax.swing.JMenuItem archivo;
    private javax.swing.JButton b_buscar1;
    private javax.swing.JButton b_xls1;
    private javax.swing.JComboBox cb_mes;
    private javax.swing.JComboBox cb_mes1;
    private javax.swing.JComboBox cb_poliza;
    private javax.swing.JMenuItem elimina;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JPanel p_consulta;
    private javax.swing.JPanel p_genera;
    private javax.swing.JPanel panel_aux;
    private javax.swing.JPanel panel_pago;
    private javax.swing.JPanel panel_provision1;
    private javax.swing.JScrollPane sp1;
    private javax.swing.JTextField t_concepto_aux;
    private javax.swing.JTable t_datos;
    private javax.swing.JTextField t_fecha_aux;
    private javax.swing.JTextField t_mes;
    private javax.swing.JTextField t_poliza1;
    private javax.swing.JTextField t_poliza_aux;
    private javax.swing.JTextField t_reclamo1;
    private javax.swing.JDialog ventana_muestra;
    // End of variables declaration//GEN-END:variables


    DefaultTableModel ModeloTablaReporte(int renglones, String columnas[])
        {
            model = new DefaultTableModel(new Object [renglones][6], columnas)
            {
                Class[] types = new Class [] {
                    java.lang.String.class,
                    java.lang.String.class,
                    java.lang.String.class, 
                    java.lang.String.class, 
                    java.lang.String.class
                };
                boolean[] canEdit = new boolean [] {
                    false, false, false, false, false
                };

                public void setValueAt(Object value, int row, int col)
                 {
                        Vector vector = (Vector)this.dataVector.elementAt(row);
                        Object celda = ((Vector)this.dataVector.elementAt(row)).elementAt(col);
                        switch(col)
                        {
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
}
