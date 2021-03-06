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
import java.io.FileWriter;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.MathContext;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
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
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author Sistemas
 */
public class Provision extends javax.swing.JPanel {

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
    public Provision(Usuario usuario, String ses, int op) {
        initComponents();
        menu=op;
        usr=usuario;
        sessionPrograma=ses;
        formato =new FormatoTabla();
        formatoTabla();
        Calendar fecha = Calendar.getInstance();
        int ano = fecha.get(Calendar.YEAR);
        System.out.println(ano);
        for(int a=2017; a<=ano; a++)
        {
            cb_ano.addItem(""+a);
            cb_ano1.addItem(""+a);
            cb_ano2.addItem(""+a);
        }
    }
    
    public void archivoExcel(String NoPoliza, String NoMes, String NoReclamo, String ano)
    {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try
        {
            Asiento[] Asientos= new Asiento[0];
            if(NoPoliza.compareTo("")!=0)
            {
                Query query1 = session.createQuery("SELECT DISTINCT reg FROM Asiento reg "
                    + "LEFT JOIN reg.excelProvision ex "
                    + "where ex.poliza="+NoPoliza+" AND MONTH(ex.fecha)='"+NoMes+"' AND year(ex.fecha)='"+ano+"' and ex.tipo='Dr' ORDER BY reg.idAsiento ASC");
                Asientos = (Asiento[])query1.list().toArray(new Asiento[0]);
            }
            else
            {
                Query query1 = session.createQuery("SELECT DISTINCT reg FROM Asiento reg "
                    + "where reg.reclamo.idReclamo="+NoReclamo+" ORDER BY reg.idAsiento ASC");
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
                        generaExcel(""+Asientos[0].getExcelProvision().getPoliza(), ""+(calendario.get(Calendar.MONTH)+1), ruta, cb_ano1.getSelectedItem().toString());
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
                        + "where MONTH(ex.fecha)="+cb_mes1.getSelectedItem()+" AND year(ex.fecha)='"+cb_ano2.getSelectedItem().toString()+"' and ex.tipo='Dr' ORDER BY poliza ASC");
            Excel[] listaPoliza = (Excel[])query1.list().toArray(new Excel[0]); 
            t_datos.setModel(ModeloTablaReporte(listaPoliza.length, columnas));
            for(int x=0; x<listaPoliza.length; x++)
            {
                t_datos.setValueAt(listaPoliza[x].getIdExcel(), x, 0);
                t_datos.setValueAt(listaPoliza[x].getPoliza(), x, 1);
                t_datos.setValueAt(listaPoliza[x].getFecha().toString(), x, 2);
                t_datos.setValueAt(listaPoliza[x].getConcepto(), x, 3);
                Asiento[] as=(Asiento[])listaPoliza[x].getAsientosForProvision().toArray(new Asiento[0]);
                String cadena="[";
                for(int a=0; a<as.length; a++)
                {
                    cadena+=""+as[a].getReclamo().getIdReclamo();
                    if(a+1<as.length)
                        cadena+="-";
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
    public void generaExcel(String noPoliza, String noMes, String ruta, String ano)
    {
        DecimalFormat formatoPorcentaje = new DecimalFormat("#,##0.00");
        formatoPorcentaje.setMinimumFractionDigits(2);
        File archivoXLS = new File(ruta+"/"+noPoliza+"-"+noMes+".xls");
        File plantilla = new File("imagenes/Diario.xls");
        Session session = HibernateUtil.getSessionFactory().openSession();
        try
        {
            Query query1 = session.createQuery("SELECT DISTINCT reg FROM Asiento reg "
                + "LEFT JOIN reg.excelProvision ex "
                + "where ex.poliza = "+noPoliza+" AND MONTH(ex.fecha)='"+noMes+"' AND year(ex.fecha)='"+ano+"'  and ex.tipo='Dr' ORDER BY reg.idAsiento ASC");
            Asiento[] Asientos = (Asiento[])query1.list().toArray(new Asiento[0]);

            Path FROM = Paths.get("imagenes/Diario.xls");
            Path TO = Paths.get(ruta+"/"+noPoliza+"-"+noMes+".xls");
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
            libro.getSheet("Hoja1").getRow(2).createCell(0).setCellValue("Dr");
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
                Registro[] registros = (Registro[])session.createCriteria(Registro.class).createAlias("asiento", "asc").add(Restrictions.eq("asc.idAsiento", Asientos[ren].getIdAsiento())).add(Restrictions.eq("tipoAsiento", "Dr")).addOrder(Order.desc("tipo")).addOrder(Order.asc("idRegistro")).list().toArray(new Registro[0]);
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
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        t_inicio = new javax.swing.JFormattedTextField();
        t_fin = new javax.swing.JFormattedTextField();
        jLabel12 = new javax.swing.JLabel();
        cb_ano = new javax.swing.JComboBox();
        jScrollPane1 = new javax.swing.JScrollPane();
        panel_provision = new javax.swing.JPanel();
        p_consulta = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        t_poliza1 = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        b_buscar1 = new javax.swing.JButton();
        b_xls1 = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        t_reclamo1 = new javax.swing.JTextField();
        cb_mes = new javax.swing.JComboBox();
        cb_ano1 = new javax.swing.JComboBox();
        jLabel13 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        panel_provision1 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jButton2 = new javax.swing.JButton();
        cb_mes1 = new javax.swing.JComboBox();
        jLabel6 = new javax.swing.JLabel();
        cb_ano2 = new javax.swing.JComboBox();
        jLabel14 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
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

        jButton1.setText("Generar Provision Pagos");
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

        jLabel10.setText("Reclamo Inicio:");

        jLabel11.setText("Fin:");

        t_inicio.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(java.text.NumberFormat.getIntegerInstance())));

        t_fin.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(java.text.NumberFormat.getIntegerInstance())));

        jLabel12.setText("Año");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jLabel10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(t_inicio, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12)
                .addComponent(jLabel11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(t_fin, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 46, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cb_poliza, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(t_mes, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel12)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cb_ano, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton3)
                .addContainerGap())
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
                    .addComponent(cb_poliza, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10)
                    .addComponent(jLabel11)
                    .addComponent(t_inicio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(t_fin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12)
                    .addComponent(cb_ano, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        p_genera.add(jPanel3, java.awt.BorderLayout.NORTH);

        jScrollPane1.setBorder(null);

        panel_provision.setBackground(new java.awt.Color(255, 255, 255));
        panel_provision.setLayout(new javax.swing.BoxLayout(panel_provision, javax.swing.BoxLayout.PAGE_AXIS));
        jScrollPane1.setViewportView(panel_provision);

        p_genera.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        jTabbedPane1.addTab("Generar", p_genera);

        p_consulta.setLayout(new java.awt.BorderLayout());

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

        jLabel13.setText("Año");

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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel13)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cb_ano1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(t_reclamo1, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(b_buscar1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(b_xls1)
                .addContainerGap(300, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(t_poliza1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel3)
                        .addComponent(jLabel4)
                        .addComponent(cb_mes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel13)
                        .addComponent(cb_ano1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(t_reclamo1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(b_buscar1)
                            .addComponent(b_xls1))))
                .addContainerGap(17, Short.MAX_VALUE))
        );

        p_consulta.add(jPanel4, java.awt.BorderLayout.NORTH);

        jScrollPane2.setBorder(null);

        panel_provision1.setBackground(new java.awt.Color(255, 255, 255));
        panel_provision1.setLayout(new javax.swing.BoxLayout(panel_provision1, javax.swing.BoxLayout.PAGE_AXIS));
        jScrollPane2.setViewportView(panel_provision1);

        p_consulta.add(jScrollPane2, java.awt.BorderLayout.CENTER);

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

        jLabel14.setText("Año");

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
                .addComponent(jLabel14)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cb_ano2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton2)
                .addContainerGap(593, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(cb_mes1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2)
                    .addComponent(jLabel14)
                    .addComponent(cb_ano2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
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
        t_datos.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        t_datos.getTableHeader().setReorderingAllowed(false);
        jScrollPane3.setViewportView(t_datos);

        jPanel1.add(jScrollPane3, java.awt.BorderLayout.CENTER);

        jTabbedPane1.addTab("Polizas de Diario", jPanel1);

        add(jTabbedPane1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
         Session session = HibernateUtil.getSessionFactory().openSession();
        try
        {
            if(t_inicio.getValue()!=null)
            {
                if(t_fin.getValue()!=null)
                {
                    String pendientes="";
                    ArrayList listaConsulta=new ArrayList();
                    //ArrayList listaProveedor=new ArrayList();
                    session.beginTransaction().begin();
                    int inicio=((Number)t_inicio.getValue()).intValue();
                    int fin=((Number)t_fin.getValue()).intValue();
                    //String consulta1 = "select distinct reclamo.id_proveedor as id FROM reclamo LEFT JOIN asiento on reclamo.id_reclamo=asiento.id_reclamo where reclamo.fecha_aceptado is not null and reclamo.estatus='A' AND asiento.id_reclamo is null and reclamo.id_reclamo>="+inicio+" and reclamo.id_reclamo<="+fin;
                    //Query query_proveedor = session.createSQLQuery(consulta1);
                    //query_proveedor.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
                    //listaProveedor=(ArrayList)query_proveedor.list();

                    lista = new ArrayList();
                    cb_poliza.removeAllItems();
                    cb_poliza.addItem("TODAS");
                    t_mes.setText("");

                    panel_provision.removeAll();
                    panel_provision.setAutoscrolls(true);
                    panel_provision.repaint();
                    panel_provision.updateUI();
                    Excel excel=null;

                    //for(int li=0; li<listaProveedor.size(); li++)
                    //{
                        Query qr=session.createSQLQuery("select if(max(poliza)+1 is null, 1, max(poliza)+1) as poliza from excel where year(fecha)=year(now()) and month(fecha)=month(now()) and tipo='Dr';");
                        qr.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
                        listaConsulta=(ArrayList)qr.list();
                        java.util.HashMap poliza=(java.util.HashMap)listaConsulta.get(0);

                        //java.util.HashMap proveedor=(java.util.HashMap)listaProveedor.get(li);
                        //Query query = session.createQuery("SELECT DISTINCT rec FROM Reclamo rec LEFT JOIN FETCH rec.asientos lista where rec.fechaAceptado is not null and rec.estatus='A' AND lista.reclamo is null and rec.proveedor.idProveedor="+proveedor.get("id").toString()+" AND rec.idReclamo>="+inicio+" AND rec.idReclamo<="+fin);
                        Query query = session.createQuery("SELECT DISTINCT rec FROM Reclamo rec LEFT JOIN FETCH rec.asientos lista where rec.fechaAceptado is not null and rec.estatus='A' AND lista.reclamo is null  AND rec.idReclamo>="+inicio+" AND rec.idReclamo<="+fin);
                        Reclamo[] rec = (Reclamo[])query.list().toArray(new Reclamo[0]);

                        if(rec.length>0)
                        {
                            cb_poliza.addItem(poliza.get("poliza").toString());
                            Date fecha = new Date();
                            Calendar calendario = Calendar.getInstance();
                            t_mes.setText(""+(calendario.get(Calendar.MONTH)+1));

                            excel=new Excel();
                            //if(i==0)//Registro del excel
                            //{
                                excel.setPoliza(Integer.parseInt(poliza.get("poliza").toString()));
                                excel.setFecha(fecha);
                                excel.setConcepto("Provision de pagos");
                                excel.setTipo("Dr");
                                session.saveOrUpdate(excel);
                            //}
                            for (int i=0; i<rec.length; i++) {
                                Reclamo rec1=rec[i];
                                usr=(Usuario)session.get(Usuario.class, usr.getIdUsuario());
                                Cuentas cuenta_prov=rec1.getProveedor().getCuentasByCtaProv();
                                Cuentas cuenta_gasto=rec1.getProveedor().getCuentasByCtaGasto();
                                if(cuenta_prov!=null && cuenta_gasto!=null)
                                {
                                    ArchivoFactura factura = (ArchivoFactura)session.createCriteria(ArchivoFactura.class).createAlias("reclamo", "rec").add(Restrictions.eq("rec.idReclamo", rec1.getIdReclamo())).add(Restrictions.eq("estatus", "v")).uniqueResult();
                                    ArchivoNota nota = (ArchivoNota)session.createCriteria(ArchivoNota.class).createAlias("reclamo", "rec").add(Restrictions.eq("rec.idReclamo", rec1.getIdReclamo())).add(Restrictions.eq("estatus", "v")).uniqueResult();
                                    Cuentas iva_x_acreditar = (Cuentas)session.createCriteria(Cuentas.class).add(Restrictions.eq("idCuentas", "1190-002-000")).uniqueResult();
                                    Cuentas iva_compras = (Cuentas)session.createCriteria(Cuentas.class).add(Restrictions.eq("idCuentas", "1191-004-000")).uniqueResult();

                                    //Registro de Asiento
                                    Asiento asiento=new Asiento();
                                    asiento.setReclamo(rec[i]);
                                    asiento.setFechaProvision(fecha);
                                    asiento.setUsuarioProvision(usr);
                                    asiento.setExcelProvision(excel);
                                    session.save(asiento);

                                    //***************Registro de Asientos************************
                                    //registro de compras
                                    Registro r1=new Registro();
                                    r1.setAsiento(asiento);
                                    r1.setCuentas(cuenta_gasto);
                                    r1.setDepto("0");
                                    r1.setConcepto("REC:"+factura.getReclamo().getIdReclamo()+" FAC:"+factura.getFolio()+" "+factura.getReclamo().getProveedor().getNombre());
                                    r1.setCantidad(BigDecimal.valueOf(factura.getSubTotal()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
                                    r1.setCambio(factura.getCambio());
                                    r1.setTipo("d");
                                    r1.setTipoAsiento("Dr");
                                    session.save(r1);

                                    //registro de iva
                                    Registro r2=new Registro();
                                    r2.setAsiento(asiento);
                                    r2.setCuentas(iva_x_acreditar);
                                    r2.setDepto("0");
                                    r2.setConcepto("REC:"+factura.getReclamo().getIdReclamo()+" FAC:"+factura.getFolio()+" "+factura.getReclamo().getProveedor().getNombre());
                                    r2.setCantidad(BigDecimal.valueOf(factura.getIva()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
                                    r2.setCambio(factura.getCambio());
                                    r2.setTipo("d");
                                    r2.setTipoAsiento("Dr");
                                    session.save(r2);

                                    //Registro de proveedores
                                    double total = factura.getIva() + factura.getSubTotal();
                                    Registro r3=new Registro();
                                    r3.setAsiento(asiento);
                                    r3.setCuentas(cuenta_prov);
                                    r3.setDepto("0");
                                    r3.setConcepto("REC:"+factura.getReclamo().getIdReclamo()+" FAC:"+factura.getFolio());
                                    r3.setCantidad(BigDecimal.valueOf(total).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
                                    r3.setCambio(factura.getCambio());
                                    r3.setTipo("a");
                                    r3.setTipoAsiento("Dr");
                                    session.save(r3);

                                    if(nota!=null)
                                    {
                                        //Registro de proveedores
                                        double total_nota = nota.getIva() + nota.getSubTotal();
                                        Registro r4=new Registro();
                                        r4.setAsiento(asiento);
                                        r4.setCuentas(cuenta_prov);
                                        r4.setDepto("0");
                                        r4.setConcepto("REC:"+factura.getReclamo().getIdReclamo()+" NOT:"+nota.getFolio());
                                        r4.setCantidad(BigDecimal.valueOf(total_nota).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
                                        r4.setCambio(factura.getCambio());
                                        r4.setTipo("d");
                                        r4.setTipoAsiento("Dr");
                                        session.save(r4);

                                        //registro de compras
                                        Cuentas dev_compra = (Cuentas)session.createCriteria(Cuentas.class).add(Restrictions.eq("idCuentas", "5400-001-000")).uniqueResult();
                                        Registro r5=new Registro();
                                        r5.setAsiento(asiento);
                                        r5.setCuentas(dev_compra);
                                        r5.setDepto("0");
                                        r5.setConcepto("REC:"+factura.getReclamo().getIdReclamo()+" NOT:"+nota.getFolio()+" "+factura.getReclamo().getProveedor().getNombre());
                                        r5.setCantidad(BigDecimal.valueOf(nota.getSubTotal()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
                                        r5.setCambio(nota.getCambio());
                                        r5.setTipo("a");
                                        r5.setTipoAsiento("Dr");
                                        session.save(r5);

                                        //registro de iva
                                        Registro r6=new Registro();
                                        r6.setAsiento(asiento);
                                        r6.setCuentas(iva_compras);
                                        r6.setDepto("0");
                                        r6.setConcepto("REC:"+factura.getReclamo().getIdReclamo()+" NOT:"+nota.getFolio()+" "+factura.getReclamo().getProveedor().getNombre());
                                        r6.setCantidad(BigDecimal.valueOf(nota.getIva()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
                                        r6.setCambio(nota.getCambio());
                                        r6.setTipo("a");
                                        r6.setTipoAsiento("Dr");
                                        session.save(r6);
                                    }
                                    lista.add(asiento);
                                }
                                else
                                {
                                    pendientes+=""+rec1.getIdReclamo()+", ";
                                }
                            }
                        }
                    //}
                    session.beginTransaction().commit();
                    for(int l=0; l<lista.size(); l++)
                    {
                        Asiento aux=(Asiento)lista.get(l);
                        asiento1 m=new asiento1(this.usr, aux, i, estado, this.sessionPrograma, "Dr");
                        panel_provision.add(m);
                        m.setVisible(true);
                    }
                    panel_provision.setAutoscrolls(true);
                    panel_provision.repaint();
                    panel_provision.updateUI();
                    if(pendientes.compareTo("")!=0)
                    {
                        FileWriter fichero = null;
                        PrintWriter pw = null;
                        try
                        {
                            Date fecha = new Date();
                            DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH-mm-ss");//YYYY-MM-DD HH:MM:SS
                            String valor=dateFormat.format(fecha);
                            File folder = new File("polizas");
                            folder.mkdirs();
                            fichero = new FileWriter("polizas/"+valor+".txt");
                            pw = new PrintWriter(fichero);
                            pw.println("Reclamos no generados: "+pendientes);
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                           try {
                           if (null != fichero)
                              fichero.close();
                           } catch (Exception e2) {
                              e2.printStackTrace();
                           }
                        }
                        JOptionPane.showMessageDialog(this, "Proceso completado pero algunos reclamos no se pudieron incluir por falta de cuenta de proveedor o gasto");
                    }
                    else
                        JOptionPane.showMessageDialog(this, "Proceso completado");
                }
                else
                    JOptionPane.showMessageDialog(this, "Falta el numero de reclamo de inicio");
            }
            else
                JOptionPane.showMessageDialog(this, "Falta el numero de reclamo de inicio");
        }catch(Exception e)
        {
            session.beginTransaction().rollback();
            cb_poliza.removeAllItems();
            t_mes.setText("");
            panel_provision.removeAll();
            panel_provision.setAutoscrolls(true);
            panel_provision.repaint();
            panel_provision.updateUI();
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
                        generaExcel(cb_poliza.getSelectedItem().toString(), t_mes.getText(), ruta, cb_ano.getSelectedItem().toString());
                    else
                    {
                        for(int op=1; op<cb_poliza.getItemCount(); op++)
                            generaExcel(cb_poliza.getItemAt(op).toString(), t_mes.getText(), ruta,cb_ano.getSelectedItem().toString());
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
                        + "LEFT JOIN reg.excelProvision ex "
                        + "where ex.poliza="+t_poliza1.getText()+" AND MONTH(ex.fecha)="+cb_mes.getSelectedItem()+" AND year(ex.fecha)='"+cb_ano1.getSelectedItem().toString()+"' and ex.tipo='Dr' ORDER BY reg.idAsiento ASC");
                    Asientos = (Asiento[])query1.list().toArray(new Asiento[0]);
                }
                else
                {
                    Query query1 = session.createQuery("SELECT DISTINCT reg FROM Asiento reg "
                        + "where reg.reclamo.idReclamo="+t_reclamo1.getText()+" ORDER BY reg.idAsiento ASC");
                    Asientos = (Asiento[])query1.list().toArray(new Asiento[0]);
                }
                panel_provision1.removeAll();
                panel_provision1.repaint();
                panel_provision1.updateUI();
                for(int e=0; e<Asientos.length; e++)
                {
                    asiento1 m=new asiento1(this.usr, Asientos[e], i, estado, this.sessionPrograma, "Dr");
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
            archivoExcel(t_poliza1.getText(), cb_mes.getSelectedItem().toString(), t_reclamo1.getText(), cb_ano1.getSelectedItem().toString());
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
                        + "LEFT JOIN reg.excelProvision ex "
                        + "where ex.idExcel="+t_datos.getValueAt(t_datos.getSelectedRow(), 0).toString()+" ORDER BY reg.idAsiento ASC");
                Asientos = (Asiento[])query1.list().toArray(new Asiento[0]);
                panel_aux.removeAll();
                panel_aux.setAutoscrolls(true);
                panel_aux.repaint();
                panel_aux.updateUI();
                for(int e=0; e<Asientos.length; e++)
                {
                    asiento1 m=new asiento1(this.usr, Asientos[e], i, estado, this.sessionPrograma, "Dr");
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
            String mi1=t_datos.getValueAt(t_datos.getSelectedRow(), 2).toString().split("-")[0];
            archivoExcel(t_datos.getValueAt(t_datos.getSelectedRow(), 1).toString(), mi, "", mi1);
        }
    }//GEN-LAST:event_archivoActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        String mi=t_fecha_aux.getText().split("-")[1];
        String mi1=t_fecha_aux.getText().split("-")[0];
        archivoExcel(t_poliza_aux.getText(), mi, "", mi1);
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
                 + "where ex.poliza="+t_poliza_aux.getText()+" AND MONTH(ex.fecha)="+mi+" and ex.tipo='Dr'");
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
                    Asiento[] lista=(Asiento[])poliza.getAsientosForProvision().toArray(new Asiento[0]);
                    int op=-1;
                    for(int a=0; a<lista.length; a++)
                    {
                        Excel rec=lista[a].getExcelPago();
                        if(rec!=null)
                        {
                            op=lista[a].getReclamo().getIdReclamo();
                            a=lista.length;
                        }
                    }
                    if(op==-1)
                    {
                        for(int p=0; p<lista.length; p++)
                        {
                            session.delete(lista[p]);
                        }
                        session.delete(poliza);
                        session.beginTransaction().commit();
                        buscaLista();
                        formatoTabla();
                    }
                    else
                    {
                        JOptionPane.showMessageDialog(this, "El reclamo NO:"+op+" ya tiene una poliza de pago, debe eliminarla primero");
                    }
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
    private javax.swing.JComboBox cb_ano;
    private javax.swing.JComboBox cb_ano1;
    private javax.swing.JComboBox cb_ano2;
    private javax.swing.JComboBox cb_mes;
    private javax.swing.JComboBox cb_mes1;
    private javax.swing.JComboBox cb_poliza;
    private javax.swing.JMenuItem elimina;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
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
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JPanel p_consulta;
    private javax.swing.JPanel p_genera;
    private javax.swing.JPanel panel_aux;
    private javax.swing.JPanel panel_provision;
    private javax.swing.JPanel panel_provision1;
    private javax.swing.JTextField t_concepto_aux;
    private javax.swing.JTable t_datos;
    private javax.swing.JTextField t_fecha_aux;
    private javax.swing.JFormattedTextField t_fin;
    private javax.swing.JFormattedTextField t_inicio;
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
