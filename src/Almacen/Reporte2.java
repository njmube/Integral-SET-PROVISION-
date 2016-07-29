/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Almacen;

import Hibernate.Util.HibernateUtil;
import Hibernate.entidades.Almacen;
import Hibernate.entidades.Configuracion;
import Hibernate.entidades.Ejemplar;
import Hibernate.entidades.Movimiento;
import Hibernate.entidades.Orden;
import Hibernate.entidades.Partida;
import Hibernate.entidades.PartidaExterna;
import Hibernate.entidades.Usuario;
import Integral.ExtensionFileFilter;
import Integral.FormatoTabla;
import Integral.Herramientas;
import Integral.PDF;
import Integral.Render1;
import Integral.calendario;
import Servicios.buscaOrden;
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
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Vector;
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

/**
 *
 * @author salvador
 */
public class Reporte2 extends javax.swing.JPanel {

    DefaultTableModel model, model1,model2;
    Usuario usr;
    int menu, x=0;
    String sessionPrograma="";
    Herramientas h;
    muestraAlmacen muestra_almacen;
    String[] columnas = new String [] {"Pedido", "Proveedor", "Fecha De Mov.", "N° Mov.", "Tipo", "Operación", "Orden", "Monto", "Tipo Doc.", "N° Doc."};
    String[] columnas1 = new String [] {"Partida", "Descripción", "Cant", "Recibió/Entrego", "Fecha", "Movimiento", "T.Movimiento"};
    String[] columnas2 = new String [] {"Pedido", "Proveedor", "Fecha De Mov.", "N° Mov.", "Tipo", "Operación", "Orden", "Monto", "Tipo Doc.", "N° Doc."};
    String[] columnas3 = new String [] {"Pedido", "Proveedor", "Fecha De Mov.", "N° Mov.", "Operación", "Tipo Doc.", "N° Doc."};
    FormatoTabla formato;
    String valor;
    /**
     * Creates new form Reporte2
     */
    public Reporte2(Usuario usuario, String ses, int op) {
        initComponents();
        menu=op;
        usr=usuario;
        sessionPrograma=ses;
        Class[] t1 = new Class [] {
                java.lang.String.class, 
                java.lang.String.class, 
                java.lang.String.class, 
                java.lang.String.class, 
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class,
                java.lang.Double.class, 
                java.lang.String.class,
                java.lang.String.class
            };
        boolean[] e1 = new boolean [] {
                false, false, false, false, false, false, false, false, false, false
            };
        t_datos.setModel(ModeloTablaReporte(0, columnas, t1, e1, model));
        
        Class[] t2 = new Class [] {
                java.lang.String.class, 
                java.lang.String.class, 
                java.lang.String.class, 
                java.lang.String.class, 
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class,
                java.lang.Double.class, 
                java.lang.String.class,
                java.lang.String.class
            };
        boolean[] e2 = new boolean [] {
                false, false, false, false, false, false, false, false, false, false
            };
        t_datos2.setModel(ModeloTablaReporte(0,columnas3, t2,e2,model2));
        formato = new FormatoTabla();
        
        formatoTabla();
        formatoTabla1();
        formatoTabla2();
    }
    
    public void consultaOrden()
    {
        DefaultTableModel modelo=(DefaultTableModel)t_unidad.getModel();
        modelo.setNumRows(0);
        valor=t_orden.getText();
        if(valor.compareTo("")!=0)
        {
            ArrayList datos = new ArrayList();
            Session session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction().begin();
            Query query = session.createSQLQuery("select if(almacen.tipo_movimiento=1, 'DEVOLUCION', 'SALIDA')as tipo, DATE_FORMAT(almacen.fecha, '%Y-%d-%m')as fecha, almacen.entrego, movimiento.id_Parte, ejemplar.id_catalogo, movimiento.cantidad, ejemplar.medida, movimiento.valor, (movimiento.cantidad*movimiento.valor) as total " +
            "from movimiento left join ejemplar on ejemplar.id_Parte=movimiento.id_Parte left join almacen on almacen.id_almacen=movimiento.id_almacen where almacen.id_orden="+valor+" and almacen.operacion=8");
            query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
            datos = (ArrayList) query.list();
            for(int c=0; c<datos.size(); c++)
            {
                java.util.HashMap map = (java.util.HashMap) datos.get(c);
                modelo.addRow(new Object[]{map.get("tipo"),map.get("fecha"),map.get("entrego"),map.get("id_Parte"),map.get("id_catalogo"),map.get("cantidad"),map.get("medida"),map.get("valor"),map.get("total")});
            }
            if(session.isOpen())
                session.close();
            session=null;
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

        muestra = new javax.swing.JDialog();
        centro = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        t_datos3 = new javax.swing.JTable();
        jButton10 = new javax.swing.JButton();
        jButton11 = new javax.swing.JButton();
        jButton12 = new javax.swing.JButton();
        jPanel10 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        t_fecha5 = new javax.swing.JTextField();
        b_fecha_siniestro4 = new javax.swing.JButton();
        t_fecha6 = new javax.swing.JTextField();
        b_fecha_siniestro5 = new javax.swing.JButton();
        jLabel11 = new javax.swing.JLabel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        t_datos = new javax.swing.JTable();
        jButton4 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        t_fecha1 = new javax.swing.JTextField();
        b_fecha_siniestro = new javax.swing.JButton();
        t_fecha2 = new javax.swing.JTextField();
        b_fecha_siniestro1 = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        t_busca = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        t_datos1 = new javax.swing.JTable();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        t_busca1 = new javax.swing.JTextField();
        b_busca = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        cb_tipo = new javax.swing.JComboBox();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        t_datos2 = new javax.swing.JTable();
        jButton7 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        orden = new javax.swing.JTextField();
        jButton9 = new javax.swing.JButton();
        jComboBox1 = new javax.swing.JComboBox();
        jLabel8 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jButton13 = new javax.swing.JButton();
        jButton14 = new javax.swing.JButton();
        jScrollPane5 = new javax.swing.JScrollPane();
        t_surtir = new javax.swing.JTable();
        jButton15 = new javax.swing.JButton();
        jPanel8 = new javax.swing.JPanel();
        t_orden = new javax.swing.JTextField();
        jButton16 = new javax.swing.JButton();
        jButton17 = new javax.swing.JButton();
        jScrollPane6 = new javax.swing.JScrollPane();
        t_unidad = new javax.swing.JTable();
        jButton18 = new javax.swing.JButton();
        jButton19 = new javax.swing.JButton();

        muestra.setTitle("Consultar Movimiento");
        muestra.setModalityType(java.awt.Dialog.ModalityType.APPLICATION_MODAL);

        centro.setLayout(new java.awt.BorderLayout());
        muestra.getContentPane().add(centro, java.awt.BorderLayout.CENTER);

        jPanel9.setBackground(new java.awt.Color(254, 254, 254));

        t_datos3.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Pedido", "Proveedor", "Fecha De Mov.", "N° Mov.", "Tipo", "Operación", "Orden", "Monto", "Tipo Doc.", "N° doc."
            }
        ));
        t_datos3.getTableHeader().setReorderingAllowed(false);
        jScrollPane4.setViewportView(t_datos3);

        jButton10.setBackground(new java.awt.Color(2, 135, 242));
        jButton10.setIcon(new ImageIcon("imagenes/exel.png"));
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });

        jButton11.setBackground(new java.awt.Color(2, 135, 242));
        jButton11.setIcon(new ImageIcon("imagenes/pdf.png"));
        jButton11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton11ActionPerformed(evt);
            }
        });

        jButton12.setText("Buscar");
        jButton12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton12ActionPerformed(evt);
            }
        });

        jPanel10.setBackground(new java.awt.Color(255, 255, 255));
        jPanel10.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "Fecha", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Arial", 1, 11))); // NOI18N

        jLabel9.setText("Inicio:");

        jLabel10.setText("Fin:");

        t_fecha5.setEditable(false);
        t_fecha5.setBackground(new java.awt.Color(204, 255, 255));
        t_fecha5.setText("AAAA-MM-DD");
        t_fecha5.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        t_fecha5.setEnabled(false);

        b_fecha_siniestro4.setBackground(new java.awt.Color(2, 135, 242));
        b_fecha_siniestro4.setIcon(new ImageIcon("imagenes/calendario.png"));
        b_fecha_siniestro4.setToolTipText("Calendario");
        b_fecha_siniestro4.setMaximumSize(new java.awt.Dimension(32, 8));
        b_fecha_siniestro4.setMinimumSize(new java.awt.Dimension(32, 8));
        b_fecha_siniestro4.setPreferredSize(new java.awt.Dimension(32, 8));
        b_fecha_siniestro4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_fecha_siniestro4ActionPerformed(evt);
            }
        });

        t_fecha6.setEditable(false);
        t_fecha6.setBackground(new java.awt.Color(204, 255, 255));
        t_fecha6.setText("AAAA-MM-DD");
        t_fecha6.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        t_fecha6.setEnabled(false);
        t_fecha6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                t_fecha6ActionPerformed(evt);
            }
        });

        b_fecha_siniestro5.setBackground(new java.awt.Color(2, 135, 242));
        b_fecha_siniestro5.setIcon(new ImageIcon("imagenes/calendario.png"));
        b_fecha_siniestro5.setToolTipText("Calendario");
        b_fecha_siniestro5.setMaximumSize(new java.awt.Dimension(32, 8));
        b_fecha_siniestro5.setMinimumSize(new java.awt.Dimension(32, 8));
        b_fecha_siniestro5.setPreferredSize(new java.awt.Dimension(32, 8));
        b_fecha_siniestro5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_fecha_siniestro5ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(t_fecha5, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(b_fecha_siniestro4, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(t_fecha6, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(b_fecha_siniestro5, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel9)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGap(3, 3, 3)
                .addComponent(t_fecha5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addComponent(b_fecha_siniestro4, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addComponent(jLabel10))
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGap(3, 3, 3)
                .addComponent(t_fecha6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addComponent(b_fecha_siniestro5, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        jLabel11.setText("Notas: P.INTERNO=PEDIDO INTERNO,   P.ADICIONAL= PEDIDOD ADICIONAL P.EXTERNO=PEDIDO EXTERNO");

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane4)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel9Layout.createSequentialGroup()
                                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jButton12)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jButton11, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton10, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel11))
                        .addGap(0, 453, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton12, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton11, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton10, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 366, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel11)
                .addContainerGap())
        );

        setBackground(new java.awt.Color(254, 254, 254));
        setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "Reportes de Almacen", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP));

        jPanel1.setBackground(new java.awt.Color(254, 254, 254));

        t_datos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Pedido", "Proveedor", "Fecha De Mov.", "N° Mov.", "Tipo", "Operación", "Orden", "Monto", "Tipo Doc.", "N° doc."
            }
        ));
        t_datos.getTableHeader().setReorderingAllowed(false);
        t_datos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                t_datosMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(t_datos);

        jButton4.setBackground(new java.awt.Color(2, 135, 242));
        jButton4.setIcon(new ImageIcon("imagenes/exel.png"));
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton3.setBackground(new java.awt.Color(2, 135, 242));
        jButton3.setIcon(new ImageIcon("imagenes/pdf.png"));
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton1.setText("Buscar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "Fecha", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Arial", 1, 11))); // NOI18N

        jLabel1.setText("Inicio:");

        jLabel2.setText("Fin:");

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

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
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
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(3, 3, 3)
                .addComponent(t_fecha1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addComponent(b_fecha_siniestro, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addComponent(jLabel2))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(3, 3, 3)
                .addComponent(t_fecha2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addComponent(b_fecha_siniestro1, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        jLabel4.setText("Notas: P.INTERNO=PEDIDO VALUACIÓN,   P.ADICIONAL= PEDIDOD ADICIONAL P.EXTERNO=PEDIDO EXTERNO");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jButton1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel4))
                        .addGap(0, 440, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 395, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel4)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Entradas/salidas de Pedidos", jPanel1);

        jPanel3.setBackground(new java.awt.Color(254, 254, 254));

        jPanel5.setBackground(new java.awt.Color(254, 254, 254));
        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "No Orden:"));

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(t_busca, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(t_busca, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jButton2.setText("Buscar");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        t_datos1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Partida", "Descripción", "Cant", "Recibió/Entrego", "Fecha", "No Mov", "T. Movimiento"
            }
        ));
        t_datos1.getTableHeader().setReorderingAllowed(false);
        t_datos1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                t_datos1MouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(t_datos1);

        jButton5.setBackground(new java.awt.Color(2, 135, 242));
        jButton5.setIcon(new ImageIcon("imagenes/pdf.png"));
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jButton6.setBackground(new java.awt.Color(2, 135, 242));
        jButton6.setIcon(new ImageIcon("imagenes/exel.png"));
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Arial", 0, 9)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(1, 1, 1));
        jLabel3.setText("Buscar:");

        t_busca1.setFont(new java.awt.Font("Arial", 0, 9)); // NOI18N
        t_busca1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        t_busca1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                t_busca1ActionPerformed(evt);
            }
        });
        t_busca1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                t_busca1KeyTyped(evt);
            }
        });

        b_busca.setIcon(new ImageIcon("imagenes/buscar1.png"));
        b_busca.setToolTipText("Busca una partida");
        b_busca.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_buscaActionPerformed(evt);
            }
        });

        jLabel5.setText("Nota: S.OPERARIOS=Entrega a Operarios    D.OPERARIOS=Devolucion Operarios  E.Proveedor=Entrada Almacen    S.Proveedor: Devolucion a Proveedor");

        cb_tipo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "OPERARIOS", "PROVEEDORES" }));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 985, Short.MAX_VALUE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addGap(8, 8, 8)
                                .addComponent(t_busca1, javax.swing.GroupLayout.PREFERRED_SIZE, 223, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, 0)
                                .addComponent(b_busca, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(cb_tipo, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel5))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jButton2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cb_tipo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 362, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(t_busca1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3)))
                    .addComponent(b_busca, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel5)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Entrega/Devolución", jPanel3);

        jPanel4.setBackground(new java.awt.Color(254, 254, 254));

        t_datos2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Pedido", "Proveedor", "Fecha De Mov.", "N° Mov.", "Operación", "Tipo Doc.", "N° doc."
            }
        ));
        t_datos2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                t_datos2MouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(t_datos2);

        jButton7.setBackground(new java.awt.Color(2, 135, 242));
        jButton7.setIcon(new ImageIcon("imagenes/exel.png"));
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        jButton8.setBackground(new java.awt.Color(2, 135, 242));
        jButton8.setIcon(new ImageIcon("imagenes/pdf.png"));
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));
        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "Ordenes", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Arial", 1, 11))); // NOI18N

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel6.setText("N° Orden:");

        orden.setBackground(new java.awt.Color(204, 255, 255));
        orden.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ordenActionPerformed(evt);
            }
        });
        orden.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                ordenKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                ordenKeyTyped(evt);
            }
        });

        jButton9.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jButton9.setText("Buscar");
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });

        jComboBox1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Remision", "Factura", "Ambas" }));

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(orden, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(20, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(orden, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton9)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(7, 7, 7))
        );

        jLabel8.setText("Notas: P.INTERNO=PEDIDO VALUACION,   P.DIRECTO= PEDIDO DIRECTO,  P.COMPAÑIA=SURTE COMPAÑIA.");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 985, Short.MAX_VALUE)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel8)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(9, 9, 9)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 389, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel8)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Consultar Pedidos", jPanel4);

        jPanel7.setBackground(new java.awt.Color(255, 255, 255));

        jButton13.setText("Consultar");
        jButton13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton13ActionPerformed(evt);
            }
        });

        jButton14.setIcon(new ImageIcon("imagenes/exel.png"));
        jButton14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton14ActionPerformed(evt);
            }
        });

        t_surtir.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "NO° PARTE", "DESCRIPCION", "MAXIMO", "MINIMO", "EXISTENCIAS", "MEDIDA", "X SURTIR"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.String.class, java.lang.Double.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        t_surtir.setFillsViewportHeight(true);
        jScrollPane5.setViewportView(t_surtir);

        jButton15.setIcon(new ImageIcon("imagenes/pdf.png"));
        jButton15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton15ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 985, Short.MAX_VALUE)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jButton13)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton14, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton15, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton13, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton14, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton15, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Consumibles x surtir", jPanel7);

        jPanel8.setBackground(new java.awt.Color(255, 255, 255));

        t_orden.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                t_ordenActionPerformed(evt);
            }
        });

        jButton16.setText("Ok");
        jButton16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton16ActionPerformed(evt);
            }
        });

        jButton17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton17ActionPerformed(evt);
            }
        });

        t_unidad.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "TIPO", "FECHA", "RECIBIÓ", "N/P", "DESCRIPCIÓN", "CANTIDAD", "MED", "C/U", "TOTAL"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Double.class, java.lang.String.class, java.lang.Double.class, java.lang.Double.class
            };
            boolean[] canEdit = new boolean [] {
                true, true, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        t_unidad.setFillsViewportHeight(true);
        jScrollPane6.setViewportView(t_unidad);

        jButton18.setIcon(new ImageIcon("imagenes/pdf.png"));
        jButton18.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton18ActionPerformed(evt);
            }
        });

        jButton19.setIcon(new ImageIcon("imagenes/exel.png"));
        jButton19.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton19ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 985, Short.MAX_VALUE)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(t_orden, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton16)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton17, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton19, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton18, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(t_orden, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton16)
                            .addComponent(jButton17, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButton19, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton18, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 438, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Consumible  x unidad", jPanel8);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.Alignment.TRAILING)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void b_buscaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_buscaActionPerformed
        // TODO add your handling code here:
        if(this.t_busca1.getText().compareToIgnoreCase("")!=0)
        {
            //buscaCuentas();
            if(x>=t_datos1.getRowCount())
            {
                x=0;
                java.awt.Rectangle r = t_datos1.getCellRect( x, 1, true);
                t_datos1.scrollRectToVisible(r);
            }
            for(; x<t_datos1.getRowCount(); x++)
            {
                if(t_datos1.getValueAt(x, 1).toString().indexOf(t_busca1.getText()) != -1)
                {
                    t_datos1.setRowSelectionInterval(x, x);
                    t_datos1.setColumnSelectionInterval(3, 3);
                    java.awt.Rectangle r = t_datos1.getCellRect( x, 1, true);
                    t_datos1.scrollRectToVisible(r);
                    break;
                }
            }
            x++;
        }
    }//GEN-LAST:event_b_buscaActionPerformed

    private void t_busca1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_busca1KeyTyped
        // TODO add your handling code here:
        char car = evt.getKeyChar();
        evt.setKeyChar(Character.toUpperCase(evt.getKeyChar()));
    }//GEN-LAST:event_t_busca1KeyTyped

    private void t_busca1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_t_busca1ActionPerformed
        // TODO add your handling code here:
        if(this.t_busca1.getText().compareToIgnoreCase("")!=0)
        {
            //buscaCuentas();
            if(x>=t_datos1.getRowCount())
            {
                x=0;
                java.awt.Rectangle r = t_datos1.getCellRect( x, 1, true);
                t_datos1.scrollRectToVisible(r);
            }
            for(; x<t_datos1.getRowCount(); x++)
            {
                if(t_datos1.getValueAt(x, 1).toString().indexOf(t_busca1.getText()) != -1)
                {
                    t_datos1.setRowSelectionInterval(x, x);
                    t_datos1.setColumnSelectionInterval(3, 3);
                    java.awt.Rectangle r = t_datos1.getCellRect( x, 1, true);
                    t_datos1.scrollRectToVisible(r);
                    break;
                }
            }
            x++;
        }
    }//GEN-LAST:event_t_busca1ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
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
                    Sheet hoja = libro.createSheet("reporte2");
                    for(int ren=0;ren<(t_datos1.getRowCount()+1);ren++)
                    {
                        Row fila = hoja.createRow(ren);
                        for(int col=0; col<t_datos1.getColumnCount(); col++)
                        {
                            Cell celda = fila.createCell(col);
                            if(ren==0)
                            {
                                celda.setCellValue(t_datos1.getColumnName(col));
                            }
                            else
                            {
                                try
                                {
                                    celda.setCellValue(t_datos1.getValueAt(ren-1, col).toString());
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
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:
        h=new Herramientas(usr, 0);
        h.session(sessionPrograma);
        if(t_datos1.getRowCount()>0)
        {
            javax.swing.JFileChooser jF1= new javax.swing.JFileChooser();
            jF1.setFileFilter(new ExtensionFileFilter("Excel document (*.pdf)", new String[] { "pdf" }));
            String ruta = null;
            if(jF1.showSaveDialog(null)==jF1.APPROVE_OPTION)
            {
                ruta = jF1.getSelectedFile().getAbsolutePath();
                if(ruta!=null)
                {
                    Session session = HibernateUtil.getSessionFactory().openSession();
                    try
                    {
                        DecimalFormat formatoPorcentaje = new DecimalFormat("#,##0.00");
                        formatoPorcentaje.setMinimumFractionDigits(2);
                        session.beginTransaction().begin();
                        BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.WINANSI, BaseFont.NOT_EMBEDDED);
                        //Orden ord=buscaApertura();
                        PDF reporte = new PDF();
                        Date fecha = new Date();
                        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyyHH-mm-ss");//YYYY-MM-DD HH:MM:SS
                        String valor=dateFormat.format(fecha);

                        reporte.Abrir2(PageSize.LETTER, "reporte almacen", ruta+".pdf");
                        Font font = new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL);
                        BaseColor contenido=BaseColor.WHITE;
                        int centro=Element.ALIGN_CENTER;
                        int izquierda=Element.ALIGN_LEFT;
                        int derecha=Element.ALIGN_RIGHT;
                        float[] nuevos=new float[]{20,150,20,100,70,20,60};

                        PdfPTable tabla=reporte.crearTabla(nuevos.length, nuevos, 100, Element.ALIGN_LEFT);

                        cabecera(reporte, bf, tabla,"Reporte de material a "+cb_tipo.getSelectedItem().toString()+" orden: "+t_busca.getText(),2);
                        for(int ren=0; ren<t_datos1.getRowCount(); ren++)
                        {
                            for(int col=0; col<t_datos1.getColumnCount(); col++)
                            {
                                try
                                {
                                    tabla.addCell(reporte.celda(t_datos1.getValueAt(ren, col).toString(), font, contenido, izquierda, 0,1,Rectangle.RECTANGLE));
                                }catch(Exception e)
                                {
                                    tabla.addCell(reporte.celda("", font, contenido, izquierda, 0,1,Rectangle.RECTANGLE));
                                }
                            }
                        }
                        tabla.setHeaderRows(1);
                        reporte.agregaObjeto(tabla);
                        reporte.cerrar();
                        reporte.visualizar2(ruta+".pdf");
                    }catch(Exception e)
                    {
                        System.out.println(e);
                        e.printStackTrace();
                        JOptionPane.showMessageDialog(this, "No se pudo realizar el reporte si el archivo esta abierto.");
                    }
                    if(session!=null)
                    if(session.isOpen())
                    session.close();
                }
            }
        }
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        Class[] t1 = new Class [] {
            java.lang.String.class,
            java.lang.String.class,
            java.lang.String.class,
            java.lang.String.class,
            java.lang.String.class,
            java.lang.String.class,
            java.lang.String.class
        };
        boolean[] e1 = new boolean [] {
            false, false, false, false, false, false, false
        };
        if(t_busca.getText().compareTo("")!=0)
        {
            String consulta="SELECT mov from Movimiento mov LEFT JOIN mov.partida par LEFT JOIN par.ordenByIdOrden ord "
                    + " where ord.idOrden='"+t_busca.getText();
            
            String consultaEx="SELECT mov from Movimiento mov LEFT JOIN mov.partidaExterna pex LEFT JOIN pex.pedido ped LEFT JOIN ped.orden ord "
            + "where (ord.idOrden='"+t_busca.getText()+"' OR mov.almacen.orden.idOrden='"+t_busca.getText()+"')";
            
            if(cb_tipo.getSelectedItem().toString().compareTo("PROVEEDORES")==0)
            {
                consulta+="' and mov.almacen.operacion in (1,3) order By mov.partida.idEvaluacion, mov.partida.subPartida asc";
                consultaEx+=" and mov.almacen.operacion in (1,3)";
            }
            else
            {
                consulta+="' and mov.almacen.operacion=5 order By mov.partida.idEvaluacion, mov.partida.subPartida asc";
                consultaEx+=" and mov.almacen.operacion in (5,8)";
            }


            Session session = HibernateUtil.getSessionFactory().openSession();
            try
            {
                session.beginTransaction();
                Query q = session.createQuery(consulta);
                Query qEx = session.createQuery(consultaEx);
                List resultList = q.list();
                List resultListEx = qEx.list();

                if(resultList.size()>0 || resultListEx.size()>0)
                {
                    t_datos1.setModel(ModeloTablaReporte(resultList.size()+resultListEx.size(), columnas1, t1, e1, model1));
                    model1=(DefaultTableModel)t_datos1.getModel();
                    int i=0;
                    for (int a=0; a<resultList.size(); a++)
                    {
                        Movimiento mov = (Movimiento)resultList.get(a);
                        Almacen alm =mov.getAlmacen();
                        Partida par=mov.getPartida();
                        model1.setValueAt(""+par.getIdEvaluacion()+"-"+par.getSubPartida(), i, 0);
                        model1.setValueAt(par.getCatalogo().getNombre(), i, 1);
                        model1.setValueAt(mov.getCantidad(), i, 2);
                        model1.setValueAt(alm.getEntrego(), i, 3);
                        model1.setValueAt(alm.getFecha().toString(), i, 4);
                        model1.setValueAt(""+alm.getIdAlmacen(), i, 5);
                        //model1.setValueAt(""+alm.getTipoMovimiento(), i, 6);
                        if(alm.getOperacion()==5)
                        {
                            if(alm.getTipoMovimiento()==1)
                            {
                                model1.setValueAt("D.Operarios", i, 6);
                            }
                            else
                            {
                                model1.setValueAt("S.Operarios", i, 6);
                            }
                        }
                        else
                        {
                            if(alm.getTipoMovimiento()==1)
                            {
                                model1.setValueAt("E.Proveedor", i, 6);
                            }
                            else
                            {
                                model1.setValueAt("S.Proveedor", i, 6);
                            }
                        }
                        i++;
                    }

                    for (int b=0; b<resultListEx.size(); b++)
                    {
                        Movimiento mov = (Movimiento)resultListEx.get(b);
                        Almacen alm =mov.getAlmacen();
                        model1.setValueAt("-", i, 0);
                        if(mov.getPartidaExterna()!=null)
                        {
                            PartidaExterna par=mov.getPartidaExterna();
                            model1.setValueAt(par.getDescripcion(), i, 1);
                        }
                        else
                        {
                            Ejemplar par=mov.getEjemplar();
                            model1.setValueAt(par.getCatalogo(), i, 1);
                        }
                        model1.setValueAt(mov.getCantidad(), i, 2);
                        model1.setValueAt(alm.getEntrego(), i, 3);
                        model1.setValueAt(alm.getFecha().toString(), i, 4);
                        model1.setValueAt(""+alm.getIdAlmacen(), i, 5);
                        //model.setValueAt(""+alm.getTipoMovimiento(), i, 6);
                        if(alm.getOperacion()==5 || alm.getOperacion()==8)
                        {
                            if(alm.getTipoMovimiento()==1)
                            {
                                model1.setValueAt("D.Operarios", i, 6);
                            }
                            else
                            {
                                model1.setValueAt("S.Operarios", i, 6);
                            }
                        }
                        else
                        {
                            if(alm.getTipoMovimiento()==1)
                            {
                                model1.setValueAt("E.Proveedor", i, 6);
                            }
                            else
                            {
                                model1.setValueAt("S.Proveedor", i, 6);
                            }
                        }
                        i++;
                    }
                }
                else
                {
                    t_datos1.setModel(ModeloTablaReporte(0, columnas1, t1, e1, model1));
                }
                t_busca.requestFocus();
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
            t_datos1.setModel(ModeloTablaReporte(0, columnas1, t1, e1, model1));
        }
        formatoTabla1();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void b_fecha_siniestro1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_fecha_siniestro1ActionPerformed
        // TODO add your handling code here:
        h=new Herramientas(usr, 0);
        h.session(sessionPrograma);

        calendario cal =new calendario(new javax.swing.JFrame(), true);
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
        {
            t_fecha2.setText("AAAA-MM-DD");
            model=(DefaultTableModel)t_datos.getModel();
            model.getDataVector().removeAllElements();
            t_datos.revalidate();
        }
    }//GEN-LAST:event_b_fecha_siniestro1ActionPerformed

    private void t_fecha2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_t_fecha2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_t_fecha2ActionPerformed

    private void b_fecha_siniestroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_fecha_siniestroActionPerformed
        // TODO add your handling code here:
        h=new Herramientas(usr, 0);
        h.session(sessionPrograma);

        calendario cal =new calendario(new javax.swing.JFrame(), true);
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
        {
            t_fecha1.setText("AAAA-MM-DD");
            model=(DefaultTableModel)t_datos.getModel();
            model.getDataVector().removeAllElements();
            t_datos.revalidate();
        }
    }//GEN-LAST:event_b_fecha_siniestroActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        String consulta="SELECT DISTINCT obj from Almacen obj where obj.operacion in(1,2,3,4,7) ";
        if(t_fecha1.getText().compareTo("AAAA-MM-DD")!=0)
        consulta+="and DATE(obj.fecha) >= '"+t_fecha1.getText()+"' ";
        if(t_fecha2.getText().compareTo("AAAA-MM-DD")!=0)
        consulta+="and DATE(obj.fecha) <= '"+t_fecha2.getText()+"'";

        Session session = HibernateUtil.getSessionFactory().openSession();
        try
        {
            session.beginTransaction();
            Query q = session.createQuery(consulta);
            List resultList = q.list();
            model=(DefaultTableModel)t_datos.getModel();
            if(resultList.size()>0)
            {
                model.getDataVector().removeAllElements();
                for (Object o : resultList)
                {
                    Almacen actor = (Almacen) o;
                    Object[] renglon=new Object[10];

                    if(actor.getPedido()!=null)
                    {
                        renglon[0]=""+actor.getPedido().getIdPedido();
                        renglon[1]=""+actor.getPedido().getProveedorByIdProveedor().getNombre();
                    }
                    else
                    {
                        renglon[0]="-";
                        renglon[1]=""+actor.getEntrego();
                    }
                    renglon[2]=actor.getFecha().toString();
                    renglon[3]=actor.getIdAlmacen();
                    if(actor.getTipoMovimiento()==1)
                    {
                        if(actor.getOperacion()==1)
                        {
                            renglon[4]="Entrada";
                            renglon[5]="P.Interno";
                            Movimiento [] mov=(Movimiento[])actor.getMovimientos().toArray(new Movimiento[0]);
                            if(mov.length>0)
                            {
                                renglon[6]=""+mov[0].getPartida().getOrdenByIdOrden().getIdOrden();
                                double tot=0.0d;
                                for(int x=0; x<mov.length; x++)
                                {
                                    tot+=mov[x].getPartida().getCantPcp()*mov[x].getPartida().getPcp();
                                }
                                renglon[7]=BigDecimal.valueOf(tot).setScale(2, RoundingMode.HALF_UP).doubleValue();
                            }
                        }
                        if(actor.getOperacion()==2)
                        {
                            renglon[4]="Entrada";
                            renglon[5]="P.Externo";
                            //renglon[5]=actor.getPedido().getOrden().getIdOrden();
                            renglon[6]="Ext";
                            Movimiento [] mov=(Movimiento[])actor.getMovimientos().toArray(new Movimiento[0]);
                            if(mov.length>0)
                            {
                                double tot=0.0d;
                                for(int x=0; x<mov.length; x++)
                                {
                                    tot+=mov[x].getPartidaExterna().getCantidad()*mov[x].getPartidaExterna().getCosto();
                                }
                                renglon[7]=BigDecimal.valueOf(tot).setScale(2, RoundingMode.HALF_UP).doubleValue();
                            }
                        }
                        if(actor.getOperacion()==3)
                        {
                            renglon[4]="Entrada";
                            renglon[5]="P.Adicional";
                            renglon[6]=""+actor.getPedido().getOrden().getIdOrden();
                            Movimiento [] mov=(Movimiento[])actor.getMovimientos().toArray(new Movimiento[0]);
                            if(mov.length>0)
                            {
                                double tot=0.0d;
                                for(int x=0; x<mov.length; x++)
                                {
                                    tot+=mov[x].getPartidaExterna().getCantidad()*mov[x].getPartidaExterna().getCosto();
                                }
                                renglon[7]=BigDecimal.valueOf(tot).setScale(2, RoundingMode.HALF_UP).doubleValue();
                            }
                        }
                        if(actor.getOperacion()==4)
                        {
                            renglon[4]="Entrada";
                            renglon[5]="Compañia";
                            Movimiento [] mov=(Movimiento[])actor.getMovimientos().toArray(new Movimiento[0]);
                            if(mov.length>0)
                            {
                                renglon[6]=""+mov[0].getPartida().getOrdenByIdOrden().getIdOrden();
                                double tot=0.0d;
                                renglon[7]=BigDecimal.valueOf(tot).setScale(2, RoundingMode.HALF_UP).doubleValue();
                            }
                        }
                        if(actor.getOperacion()==7)
                        {
                            renglon[4]="Entrada";
                            renglon[5]="P.Inventario";
                            renglon[6]="INV";
                            Movimiento [] mov=(Movimiento[])actor.getMovimientos().toArray(new Movimiento[0]);
                            if(mov.length>0)
                            {
                                double tot=0.0d;
                                for(int x=0; x<mov.length; x++)
                                {
                                    tot+=mov[x].getPartidaExterna().getCantidad()*mov[x].getPartidaExterna().getCosto();
                                }
                                renglon[7]=BigDecimal.valueOf(tot).setScale(2, RoundingMode.HALF_UP).doubleValue();
                            }
                        }
                    }
                    else
                    {

                        if(actor.getOperacion()==1)
                        {
                            renglon[4]="Devolución";
                            renglon[5]="P.Interno";
                            Movimiento [] mov=(Movimiento[])actor.getMovimientos().toArray(new Movimiento[0]);
                            if(mov.length>0)
                            {
                                renglon[6]=""+mov[0].getPartida().getOrdenByIdOrden().getIdOrden();
                                double tot=0.0d;
                                for(int x=0; x<mov.length; x++)
                                {
                                    tot+=mov[x].getPartida().getCantPcp()*mov[x].getPartida().getPcp();
                                }
                                renglon[7]=BigDecimal.valueOf(tot).setScale(2, RoundingMode.HALF_UP).doubleValue();
                            }
                        }
                        if(actor.getOperacion()==2)
                        {
                            renglon[4]="Devolución";
                            renglon[5]="P.Externo";
                            renglon[6]="Ext";
                            Movimiento [] mov=(Movimiento[])actor.getMovimientos().toArray(new Movimiento[0]);
                            if(mov.length>0)
                            {
                                double tot=0.0d;
                                for(int x=0; x<mov.length; x++)
                                {
                                    tot+=mov[x].getPartidaExterna().getCantidad()*mov[x].getPartidaExterna().getCosto();
                                }
                                renglon[7]=BigDecimal.valueOf(tot).setScale(2, RoundingMode.HALF_UP).doubleValue();
                            }
                        }
                        if(actor.getOperacion()==3)
                        {
                            renglon[4]="Devolución";
                            renglon[5]="P.Adicional";
                            renglon[6]=""+actor.getPedido().getOrden().getIdOrden();
                            Movimiento [] mov=(Movimiento[])actor.getMovimientos().toArray(new Movimiento[0]);
                            if(mov.length>0)
                            {
                                double tot=0.0d;
                                for(int x=0; x<mov.length; x++)
                                {
                                    tot+=mov[x].getPartidaExterna().getCantidad()*mov[x].getPartidaExterna().getCosto();
                                }
                                renglon[7]=BigDecimal.valueOf(tot).setScale(2, RoundingMode.HALF_UP).doubleValue();
                            }
                        }
                        if(actor.getOperacion()==4)
                        {
                            renglon[4]="Devolución";
                            renglon[5]="Compañia";
                            Movimiento [] mov=(Movimiento[])actor.getMovimientos().toArray(new Movimiento[0]);
                            if(mov.length>0)
                            {
                                renglon[6]=""+mov[0].getPartida().getOrdenByIdOrden().getIdOrden();
                                double tot=0.0d;
                                renglon[7]=BigDecimal.valueOf(tot).setScale(2, RoundingMode.HALF_UP).doubleValue();
                            }
                        }
                        if(actor.getOperacion()==7)
                        {
                            renglon[4]="Devolución";
                            renglon[5]="P.Inventario";
                            renglon[6]="INV";
                            Movimiento [] mov=(Movimiento[])actor.getMovimientos().toArray(new Movimiento[0]);
                            if(mov.length>0)
                            {
                                double tot=0.0d;
                                for(int x=0; x<mov.length; x++)
                                {
                                    tot+=mov[x].getPartidaExterna().getCantidad()*mov[x].getPartidaExterna().getCosto();
                                }
                                renglon[7]=BigDecimal.valueOf(tot).setScale(2, RoundingMode.HALF_UP).doubleValue();
                            }
                        }
                    }
                    if(actor.getDocumento()!=null)
                    {
                        if(actor.getTipoDocumento().compareTo("R")==0)
                        renglon[8]="Remisión";
                        else
                        renglon[8]="Factura";
                        renglon[9]=actor.getDocumento();
                    }
                    else
                    {
                        renglon[8]="";
                        renglon[9]="";
                    }
                    model.addRow(renglon);
                }
            }
            else
            model.getDataVector().removeAllElements();
            t_datos.revalidate();
        }catch(Exception e)
        {
            e.printStackTrace();
        }
        if(session!=null)
        if(session.isOpen())
        session.close();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        h=new Herramientas(usr, 0);
        h.session(sessionPrograma);
        if(t_datos.getRowCount()>0)
        {
            javax.swing.JFileChooser jF1= new javax.swing.JFileChooser();
            jF1.setFileFilter(new ExtensionFileFilter("Excel document (*.pdf)", new String[] { "pdf" }));
            String ruta = null;
            if(jF1.showSaveDialog(null)==jF1.APPROVE_OPTION)
            {
                ruta = jF1.getSelectedFile().getAbsolutePath();
                if(ruta!=null)
                {
                    Session session = HibernateUtil.getSessionFactory().openSession();
                    try
                    {
                        DecimalFormat formatoPorcentaje = new DecimalFormat("#,##0.00");
                        formatoPorcentaje.setMinimumFractionDigits(2);
                        session.beginTransaction().begin();
                        BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.WINANSI, BaseFont.NOT_EMBEDDED);
                        //Orden ord=buscaApertura();
                        PDF reporte = new PDF();
                        Date fecha = new Date();
                        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyyHH-mm-ss");//YYYY-MM-DD HH:MM:SS
                        String valor=dateFormat.format(fecha);

                        reporte.Abrir2(PageSize.LETTER, "reporte almacen", ruta+".pdf");
                        Font font = new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL);
                        BaseColor contenido=BaseColor.WHITE;
                        int centro=Element.ALIGN_CENTER;
                        int izquierda=Element.ALIGN_LEFT;
                        int derecha=Element.ALIGN_RIGHT;
                        float[] nuevos=new float[]{36,140,86,36,40,45,37,50,42,60};

                        PdfPTable tabla=reporte.crearTabla(nuevos.length, nuevos, 100, Element.ALIGN_LEFT);

                        cabecera(reporte, bf, tabla, "Reporte de movimientos de pedidos en almacen", 1);
                        for(int ren=0; ren<t_datos.getRowCount(); ren++)
                        {
                            for(int col=0; col<t_datos.getColumnCount(); col++)
                            {
                                try
                                {
                                    if(col==7)
                                    tabla.addCell(reporte.celda(formatoPorcentaje.format(t_datos.getValueAt(ren, col)), font, contenido, derecha, 0,1,Rectangle.RECTANGLE));
                                    else
                                    tabla.addCell(reporte.celda(t_datos.getValueAt(ren, col).toString(), font, contenido, izquierda, 0,1,Rectangle.RECTANGLE));
                                }catch(Exception e)
                                {
                                    tabla.addCell(reporte.celda("", font, contenido, izquierda, 0,1,Rectangle.RECTANGLE));
                                }
                            }
                        }
                        tabla.setHeaderRows(1);
                        reporte.agregaObjeto(tabla);
                        reporte.cerrar();
                        reporte.visualizar2(ruta+".pdf");
                    }catch(Exception e)
                    {
                        System.out.println(e);
                        e.printStackTrace();
                        JOptionPane.showMessageDialog(this, "No se pudo realizar el reporte si el archivo esta abierto.");
                    }
                    if(session!=null)
                    if(session.isOpen())
                    session.close();
                }
            }
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
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
                    Sheet hoja = libro.createSheet("reporte1");
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
    }//GEN-LAST:event_jButton4ActionPerformed

    private void t_datos1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_t_datos1MouseClicked
        // TODO add your handling code here:
        if (evt.getClickCount() == 2) {
            if(t_datos1.getSelectedRow()>-1)
            {
                Almacen alm=new Almacen();
                alm.setIdAlmacen(Integer.parseInt(t_datos1.getValueAt(t_datos1.getSelectedRow(), 5).toString()));
                muestra_almacen = new muestraAlmacen(this.usr, sessionPrograma, alm);
                muestra_almacen.busca();
                Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
                centro.removeAll();
                centro.add(muestra_almacen);
                muestra.setSize(900,600);
                muestra.setLocation((d.width/2)-(muestra.getWidth()/2), (d.height/2)-(muestra.getHeight()/2));
                centro.repaint();
                centro.updateUI();
                muestra.setVisible(true);
            }
        }
    }//GEN-LAST:event_t_datos1MouseClicked

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton10ActionPerformed

    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton11ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton11ActionPerformed

    private void jButton12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton12ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton12ActionPerformed

    private void b_fecha_siniestro4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_fecha_siniestro4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_b_fecha_siniestro4ActionPerformed

    private void t_fecha6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_t_fecha6ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_t_fecha6ActionPerformed

    private void b_fecha_siniestro5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_fecha_siniestro5ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_b_fecha_siniestro5ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
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
                    Sheet hoja = libro.createSheet("reporte1");
                    for(int ren=0;ren<(t_datos2.getRowCount()+1);ren++)
                    {
                        Row fila = hoja.createRow(ren);
                        for(int col=0; col<t_datos2.getColumnCount(); col++)
                        {
                            Cell celda = fila.createCell(col);
                            if(ren==0)
                            {
                                celda.setCellValue(t_datos2.getColumnName(col));
                            }
                            else
                            {
                                try
                                {
                                    celda.setCellValue(t_datos2.getValueAt(ren-1, col).toString());
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
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        // TODO add your handling code here:
        h=new Herramientas(usr, 0);
        h.session(sessionPrograma);
        if(t_datos2.getRowCount()>0)
        {
            javax.swing.JFileChooser jF1= new javax.swing.JFileChooser();
            jF1.setFileFilter(new ExtensionFileFilter("Excel document (*.pdf)", new String[] { "pdf" }));
            String ruta = null;
            if(jF1.showSaveDialog(null)==jF1.APPROVE_OPTION)
            {
                ruta = jF1.getSelectedFile().getAbsolutePath();
                if(ruta!=null)
                {
                    Session session = HibernateUtil.getSessionFactory().openSession();
                    try
                    {
                        session.beginTransaction().begin();
                        BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.WINANSI, BaseFont.NOT_EMBEDDED);
                        
                        PDF reporte = new PDF();
                        Date fecha = new Date();
                        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyyHH-mm-ss");//YYYY-MM-DD HH:MM:SS
                        String valor=dateFormat.format(fecha);

                        reporte.Abrir2(PageSize.LETTER, "reporte pedidos", ruta+".pdf");
                        Font font = new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL);
                        BaseColor contenido=BaseColor.WHITE;
                        int centro=Element.ALIGN_CENTER;
                        int izquierda=Element.ALIGN_LEFT;
                        int derecha=Element.ALIGN_RIGHT;
                        float[] nuevos=new float[]{60,340,170,60,90,90,90};

                        PdfPTable tabla=reporte.crearTabla(nuevos.length, nuevos, 100, Element.ALIGN_CENTER);
                        cabecera1(reporte, bf, tabla, "Reporte de Consultar Pedidos.", 1);
                        for(int ren=0; ren<t_datos2.getRowCount(); ren++)
                        {
                            for(int col=0; col<t_datos2.getColumnCount(); col++)
                            {
                                try
                                {
                                    tabla.addCell(reporte.celda(t_datos2.getValueAt(ren, col).toString(), font, contenido, derecha, 0,1,Rectangle.RECTANGLE));
                                 }catch(Exception e)
                                {
                                    tabla.addCell(reporte.celda("", font, contenido, centro, 0,1,Rectangle.RECTANGLE));
                                }
                            }
                        }
                        tabla.setHeaderRows(1);
                        reporte.agregaObjeto(tabla);
                        reporte.cerrar();
                        reporte.visualizar2(ruta+".pdf");
                    }catch(Exception e)
                    {
                        System.out.println(e);
                        e.printStackTrace();
                        JOptionPane.showMessageDialog(this, "No se pudo realizar el reporte si el archivo esta abierto.");
                    }
                    if(session!=null)
                    if(session.isOpen())
                    session.close();
                }
            }
        }
    }//GEN-LAST:event_jButton8ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        // TODO add your handling code here:
        model2=(DefaultTableModel)t_datos2.getModel();
        if (orden.getText().compareTo("") != 0) {
            String filtro=""; 
            if(jComboBox1.getSelectedItem()=="Remision"){
            filtro=" and tipo_documento='R'";
            }else if(jComboBox1.getSelectedItem()=="Factura"){
                filtro=" and tipo_documento='F'";
            }else{
                filtro="";
            }
            String consultar="select distinct almacen.id_pedido as id, almacen.fecha, almacen.id_almacen, almacen.documento, if(tipo_documento='R', 'REMISIÓN', 'FACTURA') as tipo_documento, if(almacen.operacion=1, 'P.INTERNO', if(almacen.operacion=2, 'P.EXTERNA', if(almacen.operacion=3, 'P.ADICIONAL', if(almacen.operacion=4, 'P.COMPAÑIA', 'P.INVENTARIO')))) as operacion, \n" +
"(select distinct proveedor.nombre from proveedor inner join pedido on pedido.id_proveedor=proveedor.id_proveedor inner join almacen on almacen.id_pedido=pedido.id_pedido where almacen.id_pedido=id) as proveedor \n" +
"from almacen left join pedido on almacen.id_pedido=pedido.id_pedido left join partida on partida.id_pedido=almacen.id_pedido\n" +
"where almacen.operacion in(1,3,4) and almacen.tipo_movimiento=1"+filtro+" and (almacen.id_orden="+orden.getText()+" or pedido.id_orden="+orden.getText()+" or partida.id_orden="+orden.getText()+");";
            Session session = HibernateUtil.getSessionFactory().openSession();
            try
            {
             ArrayList datos = new ArrayList();
             Query query = session.createSQLQuery(consultar);
             query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
             datos = (ArrayList) query.list();
              if(datos.size()>0)
                    {
                      model2.getDataVector().removeAllElements();
                    
                      Object[] objeto = new Object[7];

                        for (int a=0; a<datos.size(); a++)
                        {
                            java.util.HashMap map = (java.util.HashMap) datos.get(a);
                            
                            objeto[0]=map.get("id");
                            objeto[1]=map.get("proveedor");
                            objeto[2]=map.get("fecha");
                            objeto[3]=map.get("id_almacen");
                            objeto[4]=map.get("operacion");
                            objeto[5]=map.get("tipo_documento");
                            objeto[6]=map.get("documento");
                            model2.addRow(objeto);
                        }
                       
                    }else{
                    model2.getDataVector().removeAllElements();
                    t_datos2.removeAll();
                    JOptionPane.showMessageDialog(null, "No se Encontraron Pedidos.");

              }

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
            model2.getDataVector().removeAllElements();
            t_datos2.removeAll();
            JOptionPane.showMessageDialog(null, "Ingrese un N° de Orden.");
        }
    }//GEN-LAST:event_jButton9ActionPerformed

    private void ordenKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ordenKeyTyped
        // TODO add your handling code here:
       char car = evt.getKeyChar();
        if(orden.getSelectedText()!=null)
            orden.setText(orden.getText().replace(orden.getSelectedText(), ""));
        
        if(orden.getText().length()>=6)
        {
            evt.consume();
        }
        if((car<'0' || car>'9')) 
            evt.consume();
    }//GEN-LAST:event_ordenKeyTyped

    private void ordenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ordenActionPerformed
        // TODO add your handling code here:
        
    }//GEN-LAST:event_ordenActionPerformed

    private void ordenKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ordenKeyPressed
        // TODO add your handling code here:
       if (orden.getText().length() >= 6) {
                this.jButton9ActionPerformed(null);
          
        }
    }//GEN-LAST:event_ordenKeyPressed

    private void t_datosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_t_datosMouseClicked
        // TODO add your handling code here:
        if (evt.getClickCount() == 2) {
            if(t_datos.getSelectedRow()>-1)
            {
                Almacen alm=new Almacen();
                alm.setIdAlmacen(Integer.parseInt(t_datos.getValueAt(t_datos.getSelectedRow(), 3).toString()));
                muestra_almacen = new muestraAlmacen(this.usr, sessionPrograma, alm);
                muestra_almacen.busca();
                Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
                centro.removeAll();
                centro.add(muestra_almacen);
                muestra.setSize(900,600);
                muestra.setLocation((d.width/2)-(muestra.getWidth()/2), (d.height/2)-(muestra.getHeight()/2));
                centro.repaint();
                centro.updateUI();
                muestra.setVisible(true);
            }
        }
    }//GEN-LAST:event_t_datosMouseClicked

    private void t_datos2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_t_datos2MouseClicked
        // TODO add your handling code here:
        if (evt.getClickCount() == 2) {
            if(t_datos2.getSelectedRow()>-1)
            {
                Almacen alm=new Almacen();
                alm.setIdAlmacen(Integer.parseInt(t_datos2.getValueAt(t_datos2.getSelectedRow(), 3).toString()));
                muestra_almacen = new muestraAlmacen(this.usr, sessionPrograma, alm);
                muestra_almacen.busca();
                Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
                centro.removeAll();
                centro.add(muestra_almacen);
                muestra.setSize(900,600);
                muestra.setLocation((d.width/2)-(muestra.getWidth()/2), (d.height/2)-(muestra.getHeight()/2));
                centro.repaint();
                centro.updateUI();
                muestra.setVisible(true);
            }
        }
    }//GEN-LAST:event_t_datos2MouseClicked

    private void jButton13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton13ActionPerformed
        // TODO add your handling code here:
        ArrayList datos = new ArrayList();
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction().begin();
        Query query = session.createSQLQuery("select id_Parte, id_catalogo, maximo, minimo, existencias, (maximo-existencias)surtir, medida  from ejemplar where existencias<=minimo and minimo>0");
        query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
        datos = (ArrayList) query.list();
        DefaultTableModel modelo=(DefaultTableModel)t_surtir.getModel();
        modelo.setNumRows(0);
        for(int c=0; c<datos.size(); c++)
        {
            java.util.HashMap map = (java.util.HashMap) datos.get(c);
            modelo.addRow(new Object[]{map.get("id_Parte"),map.get("id_catalogo"),map.get("maximo"),map.get("minimo"),map.get("existencias"),map.get("medida"),map.get("surtir")});
        }
        if(session.isOpen())
            session.close();
        session=null;
    }//GEN-LAST:event_jButton13ActionPerformed

    private void jButton14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton14ActionPerformed
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
                    Sheet hoja = libro.createSheet("Consumibles por surtir");
                    for(int ren=0;ren<(t_surtir.getRowCount()+1);ren++)
                    {
                        Row fila = hoja.createRow(ren);
                        for(int col=0; col<t_surtir.getColumnCount(); col++)
                        {
                            Cell celda = fila.createCell(col);
                            if(ren==0)
                            {
                                celda.setCellValue(t_surtir.getColumnName(col));
                            }
                            else
                            {
                                try
                                {
                                    celda.setCellValue(t_surtir.getValueAt(ren-1, col).toString());
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
    }//GEN-LAST:event_jButton14ActionPerformed

    private void jButton15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton15ActionPerformed
        // TODO add your handling code here:
        h=new Herramientas(usr, 0);
        h.session(sessionPrograma);
        if(t_surtir.getRowCount()>0)
        {
            javax.swing.JFileChooser jF1= new javax.swing.JFileChooser();
            jF1.setFileFilter(new ExtensionFileFilter("Excel document (*.pdf)", new String[] { "pdf" }));
            String ruta = null;
            if(jF1.showSaveDialog(null)==jF1.APPROVE_OPTION)
            {
                ruta = jF1.getSelectedFile().getAbsolutePath();
                if(ruta!=null)
                {
                    Session session = HibernateUtil.getSessionFactory().openSession();
                    try
                    {
                        session.beginTransaction().begin();
                        BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.WINANSI, BaseFont.NOT_EMBEDDED);
                        
                        PDF reporte = new PDF();
                        Date fecha = new Date();
                        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyyHH-mm-ss");//YYYY-MM-DD HH:MM:SS
                        String valor=dateFormat.format(fecha);

                        reporte.Abrir2(PageSize.LETTER, "Reporte Pedidos Consumibles", ruta+".pdf");
                        Font font = new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL);
                        BaseColor contenido=BaseColor.WHITE;
                        int centro=Element.ALIGN_CENTER;
                        int izquierda=Element.ALIGN_LEFT;
                        int derecha=Element.ALIGN_RIGHT;
                        float[] nuevos=new float[]{100,310,80,80,80,50,80};

                        PdfPTable tabla=reporte.crearTabla(nuevos.length, nuevos, 100, Element.ALIGN_CENTER);
                        cabecera1(reporte, bf, tabla, "Consumibles por Surtir", 2);
                        for(int ren=0; ren<t_surtir.getRowCount(); ren++)
                        {
                            for(int col=0; col<t_surtir.getColumnCount(); col++)
                            {
                                try
                                {
                                    tabla.addCell(reporte.celda(t_surtir.getValueAt(ren, col).toString(), font, contenido, derecha, 0,1,Rectangle.RECTANGLE));
                                }catch(Exception e)
                                {
                                    tabla.addCell(reporte.celda("", font, contenido, centro, 0,1,Rectangle.RECTANGLE));
                                }
                            }
                        }
                        tabla.setHeaderRows(1);
                        reporte.agregaObjeto(tabla);
                        reporte.cerrar();
                        reporte.visualizar2(ruta+".pdf");
                    }catch(Exception e)
                    {
                        System.out.println(e);
                        e.printStackTrace();
                        JOptionPane.showMessageDialog(this, "No se pudo realizar el reporte si el archivo esta abierto.");
                    }
                    if(session!=null)
                    if(session.isOpen())
                    session.close();
                }
            }
        }
    }//GEN-LAST:event_jButton15ActionPerformed

    private void jButton17ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton17ActionPerformed
        // TODO add your handling code here:
        h=new Herramientas(usr, menu);
        h.session(sessionPrograma);
        buscaOrden obj = new buscaOrden(new javax.swing.JFrame(), true, this.usr,0);
        obj.t_busca.requestFocus();
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        obj.setLocation((d.width/2)-(obj.getWidth()/2), (d.height/2)-(obj.getHeight()/2));
        obj.setVisible(true);        
        Orden orden_act=obj.getReturnStatus();
        if (orden_act!=null)
        {
            this.t_orden.setText(""+orden_act.getIdOrden());
            consultaOrden();
        }
        else
        {
             h.desbloqueaOrden();
            t_orden.setText("");
            t_orden.requestFocus();
        }
    }//GEN-LAST:event_jButton17ActionPerformed

    private void jButton16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton16ActionPerformed
        // TODO add your handling code here:
        consultaOrden();
    }//GEN-LAST:event_jButton16ActionPerformed

    private void t_ordenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_t_ordenActionPerformed
        // TODO add your handling code here:
        consultaOrden();
    }//GEN-LAST:event_t_ordenActionPerformed

    private void jButton18ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton18ActionPerformed
        // TODO add your handling code here:
        h=new Herramientas(usr, 0);
        h.session(sessionPrograma);
        if(t_unidad.getRowCount()>0)
        {
            javax.swing.JFileChooser jF1= new javax.swing.JFileChooser();
            jF1.setFileFilter(new ExtensionFileFilter("PDF document (*.pdf)", new String[] { "pdf" }));
            String ruta = null;
            if(jF1.showSaveDialog(null)==jF1.APPROVE_OPTION)
            {
                ruta = jF1.getSelectedFile().getAbsolutePath();
                if(ruta!=null)
                {
                    Session session = HibernateUtil.getSessionFactory().openSession();
                    try
                    {
                        session.beginTransaction().begin();
                        BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.WINANSI, BaseFont.NOT_EMBEDDED);
                        
                        PDF reporte = new PDF();
                        Date fecha = new Date();
                        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyyHH-mm-ss");//YYYY-MM-DD HH:MM:SS
                        String valor=dateFormat.format(fecha);

                        reporte.Abrir2(PageSize.LETTER, "Reporte Pedidos Consumibles", ruta+".pdf");
                        Font font = new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL);
                        BaseColor contenido=BaseColor.WHITE;
                        int centro=Element.ALIGN_CENTER;
                        int izquierda=Element.ALIGN_LEFT;
                        int derecha=Element.ALIGN_RIGHT;
                        float[] nuevos=new float[]{45,55,130,80,150,50,40,70,70};

                        PdfPTable tabla=reporte.crearTabla(nuevos.length, nuevos, 100, Element.ALIGN_CENTER);
                        cabecera1(reporte, bf, tabla, "Consumibles de la Unidad: "+valor, 3);
                        for(int ren=0; ren<t_unidad.getRowCount(); ren++)
                        {
                            for(int col=0; col<t_unidad.getColumnCount(); col++)
                            {
                                try
                                {
                                    tabla.addCell(reporte.celda(t_unidad.getValueAt(ren, col).toString(), font, contenido, derecha, 0,1,Rectangle.RECTANGLE));
                                }catch(Exception e)
                                {
                                    tabla.addCell(reporte.celda("", font, contenido, centro, 0,1,Rectangle.RECTANGLE));
                                }
                            }
                        }
                        tabla.setHeaderRows(1);
                        reporte.agregaObjeto(tabla);
                        reporte.cerrar();
                        reporte.visualizar2(ruta+".pdf");
                    }catch(Exception e)
                    {
                        System.out.println(e);
                        e.printStackTrace();
                        JOptionPane.showMessageDialog(this, "No se pudo realizar el reporte si el archivo esta abierto.");
                    }
                    if(session!=null)
                    if(session.isOpen())
                    session.close();
                }
            }
        }
    }//GEN-LAST:event_jButton18ActionPerformed

    private void jButton19ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton19ActionPerformed
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
                    Sheet hoja = libro.createSheet("Consumibles de la unidad "+valor);
                    for(int ren=0;ren<(t_unidad.getRowCount()+1);ren++)
                    {
                        Row fila = hoja.createRow(ren);
                        for(int col=0; col<t_unidad.getColumnCount(); col++)
                        {
                            Cell celda = fila.createCell(col);
                            if(ren==0)
                            {
                                celda.setCellValue(t_unidad.getColumnName(col));
                            }
                            else
                            {
                                try
                                {
                                    celda.setCellValue(t_unidad.getValueAt(ren-1, col).toString());
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
    }//GEN-LAST:event_jButton19ActionPerformed

    DefaultTableModel ModeloTablaReporte(int renglones, String columnas[], final Class[] tipos, final boolean[] edo, DefaultTableModel modelo)
    {
        modelo = new DefaultTableModel(new Object [renglones][0], columnas)
        {
            Class[] types = tipos;
            boolean[] canEdit = edo;

            public void setValueAt(Object value, int row, int col)
             {
                 Vector vector = (Vector)this.dataVector.elementAt(row);
                 Object celda = ((Vector)this.dataVector.elementAt(row)).elementAt(col);
                 vector.setElementAt(value, col);
                 this.dataVector.setElementAt(vector, row);
                 fireTableCellUpdated(row, col);
             }

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        };
        return modelo;
    }
    
    public void cabecera(PDF reporte, BaseFont bf, PdfPTable tabla, String titulo1, int op)
   {
       Session session = HibernateUtil.getSessionFactory().openSession();
       try
       {
            reporte.contenido.setLineWidth(0.5f);
            reporte.contenido.setColorStroke(new GrayColor(0.2f));
            reporte.contenido.setColorFill(new GrayColor(0.9f));

            Configuracion con= (Configuracion)session.get(Configuracion.class, 1);
            reporte.inicioTexto();
            reporte.contenido.setFontAndSize(bf, 14);
            reporte.contenido.setColorFill(BaseColor.BLACK);
            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, con.getEmpresa(), 35, 755, 0);
            reporte.contenido.setFontAndSize(bf, 8);
            reporte.contenido.setColorFill(BaseColor.BLACK);
            String titulo=titulo1;
            if(op==1)
            {
                if(t_fecha1.getText().compareTo("AAAA-MM-DD")!=0)
                    titulo+=" del "+t_fecha1.getText();
                if(t_fecha2.getText().compareTo("AAAA-MM-DD")!=0)
                    titulo+=" al "+t_fecha2.getText();
            }
            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, titulo, 35, 745, 0);
            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_RIGHT, "Fecha:"+new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date()), 570, 745, 0);
                
            reporte.finTexto();
            //agregamos renglones vacios para dejar un espacio
            reporte.agregaObjeto(new Paragraph(" "));
            
            Font font = new Font(Font.FontFamily.HELVETICA, 6, Font.BOLD);
            
            BaseColor cabecera=BaseColor.GRAY;
            BaseColor contenido=BaseColor.WHITE;
            int centro=Element.ALIGN_CENTER;
            int izquierda=Element.ALIGN_LEFT;
            int derecha=Element.ALIGN_RIGHT;
            if(op==1)
            {
                for(int a=0; a<tabla.getNumberOfColumns(); a++)
                {
                    tabla.addCell(reporte.celda(t_datos.getColumnName(a), font, cabecera, centro, 0, 1, Rectangle.RECTANGLE));
                }
            }
            if(op==2)
            {
                for(int a=0; a<tabla.getNumberOfColumns(); a++)
                {
                    tabla.addCell(reporte.celda(t_datos1.getColumnName(a), font, cabecera, centro, 0, 1, Rectangle.RECTANGLE));
                }
            }
       }catch(Exception e)
       {
           System.out.println(e);
       }
       if(session!=null)
            if(session.isOpen())
                session.close();
    }
public void cabecera1(PDF reporte, BaseFont bf, PdfPTable tabla, String titulo1, int op)
   {
       Session session = HibernateUtil.getSessionFactory().openSession();
       try
       {
            reporte.contenido.setLineWidth(0.5f);
            reporte.contenido.setColorStroke(new GrayColor(0.2f));
            reporte.contenido.setColorFill(new GrayColor(0.9f));

            Configuracion con= (Configuracion)session.get(Configuracion.class, 1);
            reporte.inicioTexto();
            reporte.contenido.setFontAndSize(bf, 14);
            reporte.contenido.setColorFill(BaseColor.BLACK);
            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, con.getEmpresa(), 35, 755, 0);
            reporte.contenido.setFontAndSize(bf, 8);
            reporte.contenido.setColorFill(BaseColor.BLACK);
            String titulo=titulo1;
          
            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, titulo, 35, 745, 0);
            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_RIGHT, "Fecha:"+new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date()), 570, 745, 0);
                
            reporte.finTexto();
            //agregamos renglones vacios para dejar un espacio
            reporte.agregaObjeto(new Paragraph(" "));
            
            Font font = new Font(Font.FontFamily.HELVETICA, 6, Font.BOLD);
            
            BaseColor cabecera=BaseColor.GRAY;
            BaseColor contenido=BaseColor.WHITE;
            int centro=Element.ALIGN_CENTER;
            
                for(int a=0; a<tabla.getNumberOfColumns(); a++)
                {
                    if(op==1)
                        tabla.addCell(reporte.celda(t_datos2.getColumnName(a), font, cabecera, centro, 0, 1, Rectangle.RECTANGLE));
                    if(op==2)
                        tabla.addCell(reporte.celda(t_surtir.getColumnName(a), font, cabecera, centro, 0, 1, Rectangle.RECTANGLE));
                    if(op==3)
                        tabla.addCell(reporte.celda(t_unidad.getColumnName(a), font, cabecera, centro, 0, 1, Rectangle.RECTANGLE));
                }
       }catch(Exception e)
       {
           System.out.println(e);
       }
       if(session!=null)
            if(session.isOpen())
                session.close();
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
        t_datos.setDefaultRenderer(String.class, formato); 
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
                    column.setPreferredWidth(25);
                    break;
                case 1:
                    column.setPreferredWidth(140);
                    break;    
                case 2:
                    column.setPreferredWidth(100);
                    break;
                case 3:
                    column.setPreferredWidth(25);
                    break;      
                case 4:
                    column.setPreferredWidth(50);
                    break; 
                case 5:
                    column.setPreferredWidth(50);
                    break; 
                case 6:
                    column.setPreferredWidth(40);
                    break; 
                case 7:
                    column.setPreferredWidth(50);
                    break; 
                case 8:
                    column.setPreferredWidth(40);
                    break; 
                default:
                    column.setPreferredWidth(60);
                    break; 
            }
        }
        JTableHeader header = t_datos.getTableHeader();
        header.setForeground(Color.white);
    }
    
    public void formatoTabla1()
    {
        Color c1 = new java.awt.Color(2, 135, 242);
        for(int x=0; x<t_datos1.getColumnModel().getColumnCount(); x++)
            t_datos1.getColumnModel().getColumn(x).setHeaderRenderer(new Render1(c1));
        tabla_tamaños1();
        t_datos1.setShowVerticalLines(true);
        t_datos1.setShowHorizontalLines(true);
        
        t_datos1.setDefaultRenderer(Double.class, formato); 
        t_datos1.setDefaultRenderer(String.class, formato); 
    }
    
    public void tabla_tamaños1()
    {
        TableColumnModel col_model = t_datos1.getColumnModel();
        for (int i=0; i<t_datos1.getColumnCount(); i++)
        {
            TableColumn column = col_model.getColumn(i);
            switch(i)
            {
                case 0:
                    column.setPreferredWidth(60);
                    break;
                case 1:
                    column.setPreferredWidth(130);
                    break;
                case 2:
                    column.setPreferredWidth(40);
                    break;      
                case 3:
                    column.setPreferredWidth(90);
                    break; 
                case 4:
                    column.setPreferredWidth(90);
                    break; 
                case 5:
                    column.setPreferredWidth(60);
                    break; 
                case 6:
                    column.setPreferredWidth(80);
                    break; 
            }
        }
        JTableHeader header = t_datos1.getTableHeader();
        header.setForeground(Color.white);
    }
    public void formatoTabla2()
    {
        Color c1 = new java.awt.Color(2, 135, 242);
        for(int x=0; x<t_datos2.getColumnModel().getColumnCount(); x++)
            t_datos.getColumnModel().getColumn(x).setHeaderRenderer(new Render1(c1));
        tabla_tamaños2();
        t_datos2.setShowVerticalLines(true);
        t_datos2.setShowHorizontalLines(true);
            
    }
    
    public void tabla_tamaños2()
    {
        TableColumnModel col_model = t_datos2.getColumnModel();
        
        for (int i=0; i<t_datos2.getColumnCount(); i++)
        {
            TableColumn column = col_model.getColumn(i);
            switch(i)
            {
                case 0:
                    column.setPreferredWidth(30);
                    break;
                case 1:
                    column.setPreferredWidth(340);
                    break;
                case 2:
                    column.setPreferredWidth(170);
                    break;      
                case 3:
                    column.setPreferredWidth(30);
                    break; 
                case 4:
                    column.setPreferredWidth(90);
                    break; 
                case 5:
                    column.setPreferredWidth(90);
                    break; 
                case 6:
                    column.setPreferredWidth(90);
                    break; 
            }
        }
        JTableHeader header = t_datos2.getTableHeader();
        header.setForeground(Color.black);
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton b_busca;
    private javax.swing.JButton b_fecha_siniestro;
    private javax.swing.JButton b_fecha_siniestro1;
    private javax.swing.JButton b_fecha_siniestro4;
    private javax.swing.JButton b_fecha_siniestro5;
    private javax.swing.JComboBox cb_tipo;
    private javax.swing.JPanel centro;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton13;
    private javax.swing.JButton jButton14;
    private javax.swing.JButton jButton15;
    private javax.swing.JButton jButton16;
    private javax.swing.JButton jButton17;
    private javax.swing.JButton jButton18;
    private javax.swing.JButton jButton19;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JDialog muestra;
    private javax.swing.JTextField orden;
    private javax.swing.JTextField t_busca;
    private javax.swing.JTextField t_busca1;
    private javax.swing.JTable t_datos;
    private javax.swing.JTable t_datos1;
    private javax.swing.JTable t_datos2;
    private javax.swing.JTable t_datos3;
    private javax.swing.JTextField t_fecha1;
    private javax.swing.JTextField t_fecha2;
    private javax.swing.JTextField t_fecha5;
    private javax.swing.JTextField t_fecha6;
    private javax.swing.JTextField t_orden;
    private javax.swing.JTable t_surtir;
    private javax.swing.JTable t_unidad;
    // End of variables declaration//GEN-END:variables
}
