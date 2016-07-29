/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Compras;

import Hibernate.Util.HibernateUtil;
import Hibernate.entidades.Configuracion;
import Hibernate.entidades.Orden;
import Hibernate.entidades.Partida;
import Hibernate.entidades.PartidaExterna;
import Hibernate.entidades.Pedido;
import Hibernate.entidades.Proveedor;
import Hibernate.entidades.Usuario;
import Proveedor.buscaProveedor;
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
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
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
import org.hibernate.Query;
import org.hibernate.Session;
import Integral.ExtensionFileFilter;
import Integral.FormatoTabla;
import Integral.Herramientas;
import Integral.HorizontalBarUI;
import Integral.PDF;
import Integral.Render1;
import Integral.VerticalBarUI;
import Integral.calendario;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author salvador
 */
public class reportePedidos extends javax.swing.JPanel {

    Usuario usr;
    String estado;
    String sessionPrograma;
    Herramientas h;
    FormatoTabla formato;
    //private Session session;
    String[] columnas = new String [] {"Pedido", "Fecha", "O. Taller", "Usuario", "Proveedor", "Nombre de Proveedor", "Facturar a", "Observaciones", "Autorizo1", "Monto tot."};
    DefaultTableModel model;
    /**
     * Creates new form reportePedidos
     */
    public reportePedidos(Usuario us, String ses) {
        initComponents();
        scroll.getVerticalScrollBar().setUI(new VerticalBarUI());
        scroll.getHorizontalScrollBar().setUI(new HorizontalBarUI());
        usr=us;
        sessionPrograma=ses;
        h=new Herramientas(usr, 0);
        formato = new FormatoTabla();
        titulos();
    }

    DefaultTableModel ModeloTablaReporte(int renglones, String columnas[])
        {
            model = new DefaultTableModel(new Object [renglones][10], columnas)
            {
                Class[] types = new Class [] {
                    java.lang.Integer.class, 
                    java.lang.String.class, 
                    java.lang.Integer.class, 
                    java.lang.String.class, 
                    java.lang.Integer.class, 
                    java.lang.String.class, 
                    java.lang.String.class, 
                    java.lang.String.class, 
                    java.lang.String.class,
                    java.lang.Double.class,
                };
                boolean[] canEdit = new boolean [] {
                    false, false, false, false, false, false, false, false, false, false
                };

                public void setValueAt(Object value, int row, int col)
                 {
                        Vector vector = (Vector)this.dataVector.elementAt(row);
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
            return model;
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
                    break;
                case 1:
                    column.setPreferredWidth(110);
                    break;
                case 2:
                    column.setPreferredWidth(30);
                    break;      
                case 3:
                    column.setPreferredWidth(50);
                    break; 
                case 4:
                    column.setPreferredWidth(10);
                    break; 
                case 5:
                    column.setPreferredWidth(200);
                    break; 
                case 6:
                    column.setPreferredWidth(200);
                    break; 
                case 7:
                    column.setPreferredWidth(50);
                    break; 
                case 8:
                    column.setPreferredWidth(50);
                    break;      
                case 9:
                    column.setPreferredWidth(50);
                    break; 
                default:
                    column.setPreferredWidth(40);
                    break; 
            }
        }
    }
    
    public void titulos()
    {
        Color c1 = new java.awt.Color(2, 135, 242);
        for (int c=0; c<t_datos.getColumnCount(); c++)
            t_datos.getColumnModel().getColumn(c).setHeaderRenderer(new Render1(c1));
        JTableHeader header = t_datos.getTableHeader();
        header.setForeground(Color.white);
        t_datos.setShowVerticalLines(true);
        t_datos.setShowHorizontalLines(true);
        
        t_datos.setDefaultRenderer(Double.class, formato); 
        t_datos.setDefaultRenderer(Integer.class, formato);
        t_datos.setDefaultRenderer(String.class, formato); 
        t_datos.setDefaultRenderer(Boolean.class, formato);
        this.tabla_tamaños();
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
        jPanel2 = new javax.swing.JPanel();
        c_orden = new javax.swing.JComboBox();
        jPanel3 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        t_fecha1 = new javax.swing.JTextField();
        b_fecha_siniestro = new javax.swing.JButton();
        b_fecha_siniestro1 = new javax.swing.JButton();
        t_fecha2 = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        l_id_cliente = new javax.swing.JTextField();
        b_busca_cliente = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        b_busca_orden = new javax.swing.JButton();
        t_orden = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        autorizado = new javax.swing.JCheckBox();
        jButton5 = new javax.swing.JButton();
        scroll = new javax.swing.JScrollPane();
        t_datos = new javax.swing.JTable();

        setLayout(new java.awt.BorderLayout());

        jPanel1.setBackground(new java.awt.Color(254, 254, 254));

        jPanel2.setBackground(new java.awt.Color(254, 254, 254));
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "Ordenar por:", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Arial", 1, 11))); // NOI18N

        c_orden.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Pedido", "Fecha", "Proveedor" }));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(c_orden, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(c_orden, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel3.setBackground(new java.awt.Color(254, 254, 254));
        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "Filtrar por:", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Arial", 1, 11))); // NOI18N

        jPanel4.setBackground(new java.awt.Color(254, 254, 254));
        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "Fecha de pedido", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.BOTTOM, new java.awt.Font("Arial", 1, 10))); // NOI18N

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

        jLabel2.setText("Fin:");

        jLabel1.setText("Inicio:");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(t_fecha1, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(1, 1, 1)
                        .addComponent(b_fecha_siniestro, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(t_fecha2, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(1, 1, 1)
                        .addComponent(b_fecha_siniestro1, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(32, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addComponent(jLabel2))
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(t_fecha1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(b_fecha_siniestro, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(t_fecha2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(b_fecha_siniestro1, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        jPanel5.setBackground(new java.awt.Color(254, 254, 254));
        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "Proveedor", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.BOTTOM, new java.awt.Font("Arial", 1, 10))); // NOI18N

        jLabel8.setForeground(new java.awt.Color(51, 0, 255));
        jLabel8.setText("No°");

        l_id_cliente.setEnabled(false);

        b_busca_cliente.setBackground(new java.awt.Color(2, 135, 242));
        b_busca_cliente.setIcon(new ImageIcon("imagenes/buscar.png"));
        b_busca_cliente.setToolTipText("Consultar clientes");
        b_busca_cliente.setMaximumSize(new java.awt.Dimension(32, 8));
        b_busca_cliente.setMinimumSize(new java.awt.Dimension(32, 8));
        b_busca_cliente.setPreferredSize(new java.awt.Dimension(32, 8));
        b_busca_cliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_busca_clienteActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(l_id_cliente, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(b_busca_cliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(l_id_cliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(b_busca_cliente, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jPanel6.setBackground(new java.awt.Color(254, 254, 254));
        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "No de orden", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.BOTTOM, new java.awt.Font("Arial", 1, 10))); // NOI18N

        b_busca_orden.setBackground(new java.awt.Color(2, 135, 242));
        b_busca_orden.setIcon(new ImageIcon("imagenes/buscar.png"));
        b_busca_orden.setMaximumSize(new java.awt.Dimension(32, 8));
        b_busca_orden.setMinimumSize(new java.awt.Dimension(32, 8));
        b_busca_orden.setPreferredSize(new java.awt.Dimension(32, 8));
        b_busca_orden.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_busca_ordenActionPerformed(evt);
            }
        });

        t_orden.setBackground(new java.awt.Color(204, 255, 255));
        t_orden.setEnabled(false);

        jLabel9.setForeground(new java.awt.Color(51, 0, 255));
        jLabel9.setText("No°");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(t_orden, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(b_busca_orden, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(t_orden, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(b_busca_orden, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(17, 17, 17))
        );

        jButton1.setBackground(new java.awt.Color(2, 135, 242));
        jButton1.setIcon(new ImageIcon("imagenes/busca.png"));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setBackground(new java.awt.Color(2, 135, 242));
        jButton2.setIcon(new ImageIcon("imagenes/pdf.png"));
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setBackground(new java.awt.Color(2, 135, 242));
        jButton3.setIcon(new ImageIcon("imagenes/exel.png"));
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setBackground(new java.awt.Color(2, 135, 242));
        jButton4.setIcon(new ImageIcon("imagenes/pdf1.png"));
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        autorizado.setText("Autorizados");

        jButton5.setBackground(new java.awt.Color(2, 135, 242));
        jButton5.setIcon(new ImageIcon("imagenes/exel.png"));
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(autorizado))
                .addContainerGap(67, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(autorizado)
                .addContainerGap(16, Short.MAX_VALUE))
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jPanel6, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 2, Short.MAX_VALUE))
        );

        add(jPanel1, java.awt.BorderLayout.PAGE_START);

        t_datos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Pedido", "Fecha", "O. Taller", "Usuario", "Prov.", "Nombre de Proveedor", "Facturar a", "Observaciones", "Autorizo1", "Monto Tot."
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Double.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        t_datos.getTableHeader().setReorderingAllowed(false);
        scroll.setViewportView(t_datos);

        add(scroll, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

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
        }
        else
        t_fecha1.setText("AAAA-MM-DD");
    }//GEN-LAST:event_b_fecha_siniestroActionPerformed

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
        t_fecha2.setText("AAAA-MM-DD");
    }//GEN-LAST:event_b_fecha_siniestro1ActionPerformed

    private void t_fecha2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_t_fecha2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_t_fecha2ActionPerformed

    private void b_busca_clienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_busca_clienteActionPerformed
        // TODO add your handling code here:
        h=new Herramientas(usr, 0);
        h.session(sessionPrograma);

        buscaProveedor obj = new buscaProveedor(new javax.swing.JFrame(), true, this.usr, this.sessionPrograma);
        obj.t_busca.requestFocus();
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        obj.setLocation((d.width/2)-(obj.getWidth()/2), (d.height/2)-(obj.getHeight()/2));
        obj.setVisible(true);

        Proveedor actor=obj.getReturnStatus();
        if(actor!=null)
        l_id_cliente.setText(actor.getIdProveedor().toString());
        else
        l_id_cliente.setText("");
    }//GEN-LAST:event_b_busca_clienteActionPerformed

    private void b_busca_ordenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_busca_ordenActionPerformed
        // TODO add your handling code here:
        h=new Herramientas(usr, 0);
        h.session(sessionPrograma);

        buscaOrden obj = new buscaOrden(new javax.swing.JFrame(), true, this.usr,0);
        obj.t_busca.requestFocus();
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        obj.setLocation((d.width/2)-(obj.getWidth()/2), (d.height/2)-(obj.getHeight()/2));
        obj.setVisible(true);

        Orden orden_act=obj.getReturnStatus();

        if (orden_act!=null)
            this.t_orden.setText(""+orden_act.getIdOrden());
        else
            t_orden.setText("");
    }//GEN-LAST:event_b_busca_ordenActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        String consulta="SELECT DISTINCT ped from Pedido ped ";
        String aux="SELECT DISTINCT ped from Pedido ped ";
        int entro=0, otro=0;
        if(t_orden.getText().compareTo("")!=0)
        {
            consulta+="LEFT JOIN FETCH ped.partidas partP "
                    + "LEFT JOIN partP.ordenByIdOrden ord "
                    + "where ord.idOrden="+t_orden.getText();
            aux+= "where ped.orden.idOrden="+t_orden.getText();
            entro=1;
            otro=1;
        }
        if(t_fecha1.getText().compareTo("AAAA-MM-DD")!=0)
        {
            if(entro==0)
            {
                consulta+=" where ";
                aux +=" where ";
                entro=1;
            }
            else
            {
                consulta+=" and ";
                aux+=" and ";
            }
            consulta+="ped.fechaPedido>='"+t_fecha1.getText()+"' ";
            aux+="ped.fechaPedido>='"+t_fecha1.getText()+"' ";
        }
        
        if(t_fecha2.getText().compareTo("AAAA-MM-DD")!=0)
        {
            if(entro==0)
            {
                consulta+=" where ";
                aux+=" where ";
                entro=1;
            }
            else
            {
                consulta+=" and ";
                aux+=" and ";
            }
            consulta+=" ped.fechaPedido<='"+t_fecha2.getText()+"' ";
            aux+=" ped.fechaPedido<='"+t_fecha2.getText()+"' ";
            entro=1;
        }
        
        if(l_id_cliente.getText().compareTo("")!=0)
        {
            if(entro==0)
            {
                consulta+=" where ";
                aux+=" where ";
                entro=1;
            }
            else
            {
                consulta+=" and ";
                aux+=" and ";
            }
            consulta+=" ped.proveedorByIdProveedor="+l_id_cliente.getText();
            aux+=" ped.proveedorByIdProveedor="+l_id_cliente.getText();
        }
        
        if(c_orden.getSelectedItem().toString().compareTo("Pedido")==0)
        {
            consulta+=" order by ped.idPedido";
            aux+=" order by ped.idPedido";
        }
        
        if(c_orden.getSelectedItem().toString().compareTo("Fecha")==0)
        {
            consulta+=" order by ped.fechaPedido";
            aux+=" order by ped.fechaPedido";
        }
        
        if(c_orden.getSelectedItem().toString().compareTo("Proveedor")==0)
        {
            consulta+=" order by ped.proveedorByIdProveedor";
            aux+=" order by ped.proveedorByIdProveedor";
        }
        Session session = HibernateUtil.getSessionFactory().openSession();
        try
        {
            session.beginTransaction();
            Query q = session.createQuery(consulta);
            List resultList = q.list();
            if(otro==1)
            {
                Query q1 = session.createQuery(aux);
                resultList.addAll(q1.list());
            }
            if(resultList.size()>0)
            {
                t_datos.setModel(ModeloTablaReporte(resultList.size(), columnas));
                int i=0;
                for (Object o : resultList) 
                {
                    Pedido actor = (Pedido) o;
                    model.setValueAt(actor.getIdPedido(), i, 0);
                    model.setValueAt(actor.getFechaPedido().toString(), i, 1);
                    Partida[] part=(Partida[])actor.getPartidas().toArray(new Partida[0]);
                    PartidaExterna[] partEx=(PartidaExterna[])actor.getPartidaExternas().toArray(new PartidaExterna[0]);

                    if(part.length>0)
                        model.setValueAt(part[0].getOrdenByIdOrden().getIdOrden(), i, 2);
                    else
                    {
                        if(actor.getOrden()!=null)
                            model.setValueAt(actor.getOrden().getIdOrden(), i, 2);    
                        else
                            model.setValueAt("", i, 2);
                    }
                    model.setValueAt(actor.getUsuarioByIdUsuario().getIdUsuario(), i, 3);
                    model.setValueAt(actor.getProveedorByIdProveedor().getIdProveedor(), i, 4);
                    model.setValueAt(actor.getProveedorByIdProveedor().getNombre(), i, 5);
                    model.setValueAt(actor.getProveedorByIdEmpresa().getNombre(), i, 6);
                    model.setValueAt(actor.getNotas(), i, 7);
                    if(actor.getUsuarioByAutorizo()!=null)
                        model.setValueAt(actor.getUsuarioByAutorizo().getIdUsuario(), i, 8);
                    else
                        model.setValueAt("Sin autorizar", i, 8);
                    double tot=0.0d;
                    if(part.length>0)
                    {
                        for(int x=0; x<part.length; x++)
                            tot+=part[x].getCantPcp()*part[x].getPcp();
                    }
                    if(partEx.length>0)
                    {
                        for(int x=0; x<partEx.length; x++)
                            tot+=partEx[x].getCantidad()*partEx[x].getCosto();
                    }
                    model.setValueAt(tot, i, 9);
                    i++;
                }
            }
            else
                t_datos.setModel(ModeloTablaReporte(0, columnas));
            titulos();
        }catch(Exception e)
        {
            System.out.println(e);
        }
        finally
        {
            if(session!=null)
                if(session.isOpen())
                    session.close();
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        h=new Herramientas(usr, 0);
        h.session(sessionPrograma);
        if(t_datos.getRowCount()>0)
        {
            Session session = HibernateUtil.getSessionFactory().openSession();
            javax.swing.JFileChooser jF1= new javax.swing.JFileChooser();
            jF1.setFileFilter(new ExtensionFileFilter("Excel document (*.pdf)", new String[] { "pdf" }));
            String ruta = null;
            if(jF1.showSaveDialog(null)==jF1.APPROVE_OPTION)
            {
                ruta = jF1.getSelectedFile().getAbsolutePath();
                if(ruta!=null)
                {
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

                        reporte.Abrir2(PageSize.LETTER.rotate(), "Reporte", ruta+".pdf");
                        Font font = new Font(Font.FontFamily.HELVETICA, 5, Font.BOLD);
                        BaseColor contenido=BaseColor.WHITE;
                        int centro=Element.ALIGN_CENTER;
                        int izquierda=Element.ALIGN_LEFT;
                        int derecha=Element.ALIGN_RIGHT;
                        float[] tam_pdf = new float[]{10,25,13,18,10,50,50,60,18,15};

                        PdfPTable tabla=reporte.crearTabla(tam_pdf.length, tam_pdf, 100, Element.ALIGN_LEFT);

                        cabecera(reporte, bf, tabla);
                        int ren=0;
                        double dm=0d, cam=0d, min=0d, med=0d, max=0d, pin=0d, tot=0d;
                        for(int i=0; i<t_datos.getRowCount(); i++)
                        {
                            for(int j=0; j<t_datos.getColumnCount(); j++)
                            {
                                if(t_datos.getColumnName(j).compareTo("Monto tot.")==0)
                                {
                                    if(t_datos.getValueAt(i, j)!=null)
                                        tabla.addCell(reporte.celda(formatoPorcentaje.format(t_datos.getValueAt(i, j)), font, contenido, derecha, 0,1,Rectangle.RECTANGLE));
                                    else
                                        tabla.addCell(reporte.celda("0.00", font, contenido, derecha, 0,1,Rectangle.RECTANGLE));
                                }
                                else
                                {
                                    if(t_datos.getValueAt(i, j)!=null)
                                        tabla.addCell(reporte.celda(""+t_datos.getValueAt(i, j), font, contenido, izquierda, 0,1,Rectangle.RECTANGLE));
                                    else
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
                    finally
                    {
                        if(session!=null)
                            if(session.isOpen())
                                session.close();
                    }
                }
            }
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
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
                    Sheet hoja = libro.createSheet("Reporte");
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
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        h=new Herramientas(usr, 0);
        h.session(sessionPrograma);
        if(t_datos.getRowCount()>0)
        {
            Session session = HibernateUtil.getSessionFactory().openSession();
            javax.swing.JFileChooser jF1= new javax.swing.JFileChooser();
            jF1.setFileFilter(new ExtensionFileFilter("Excel document (*.pdf)", new String[] { "pdf" }));
            String ruta = null;
            if(jF1.showSaveDialog(null)==jF1.APPROVE_OPTION)
            {
                ruta = jF1.getSelectedFile().getAbsolutePath();
                if(ruta!=null)
                {
                    try
                    {
                        DecimalFormat formatoPorcentaje = new DecimalFormat("#,##0.00");
                        formatoPorcentaje.setMinimumFractionDigits(2);
                        session.beginTransaction().begin();
                        BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.WINANSI, BaseFont.NOT_EMBEDDED);
                        PDF reporte = new PDF();
                        Date fecha = new Date();
                        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyyHH-mm-ss");//YYYY-MM-DD HH:MM:SS
                        String valor=dateFormat.format(fecha);

                        reporte.Abrir2(PageSize.LETTER.rotate(), "Reporte", ruta+".pdf");
                        Font font = new Font(Font.FontFamily.HELVETICA, 8, Font.NORMAL);
                        BaseColor contenido=BaseColor.WHITE;
                        int centro=Element.ALIGN_CENTER;
                        int izquierda=Element.ALIGN_LEFT;
                        int derecha=Element.ALIGN_RIGHT;
                        float[] tam_pdf = new float[]{15,40,130,20,15,30,20,15};

                        PdfPTable tabla=reporte.crearTabla(tam_pdf.length, tam_pdf, 100, Element.ALIGN_LEFT);

                        cabeceraReporte(reporte, bf, tabla);
                        Object no[];
                        int ren[];
                        if(t_datos.getSelectedRows().length>0)
                        {
                            no=new  Object[t_datos.getSelectedRows().length];
                            ren =t_datos.getSelectedRows();
                            for(int x=0; x<t_datos.getSelectedRows().length; x++)
                            {
                                no[x]=(int)t_datos.getValueAt(ren[x], 0);
                            }
                        }
                        else
                        {
                            no=new  Object[t_datos.getRowCount()];
                            //ren =new int[t_datos.getRowCount()];
                            for(int x=0; x<t_datos.getRowCount(); x++)
                            {
                                no[x]=(int)t_datos.getValueAt(x, 0);
                            }
                        }
                        Pedido[] pedidos;
                        if(autorizado.isSelected()==true)
                        {
                            pedidos = (Pedido[]) session.createCriteria(Pedido.class)
                                .add(Restrictions.and(Restrictions.isNotNull("usuarioByAutorizo") , 
                                        Restrictions.in("idPedido", no)) 
                                ).list().toArray(new Pedido[0]);
                        }
                        else
                        {
                            pedidos = (Pedido[]) session.createCriteria(Pedido.class)
                                .add(Restrictions.in("idPedido", no)) 
                                .list().toArray(new Pedido[0]);
                        }
                        if(pedidos.length>0)
                        {
                            ArrayList ordena=new ArrayList();
                            for(int a=0; a<pedidos.length; a++)
                            {
                                Pedido aux=pedidos[a];
                                if(aux.getTipoPedido().compareTo("Interno")==0)
                                {
                                    Partida [] par=(Partida[])aux.getPartidas().toArray(new Partida[0]);
                                    for(int b=0; b<par.length; b++)
                                    {
                                        Partida ren1=par[b];
                                        Renglon nuevo;
                                        if(ren1.getEjemplar()!=null)
                                            nuevo=new Renglon(""+aux.getIdPedido(), ren1.getEjemplar().getIdParte(), ren1.getCatalogo().getNombre(), ren1.getCantPcp(),  ren1.getMed(),  ren1.getPcp(), ""+ren1.getOrdenByIdOrden().getIdOrden(), ""+ren1.getIdEvaluacion()+"-"+ren1.getSubPartida());
                                        else
                                            nuevo=new Renglon(""+aux.getIdPedido(), "", ren1.getCatalogo().getNombre(), ren1.getCantPcp(),  ren1.getMed(),  ren1.getPcp(), ""+ren1.getOrdenByIdOrden().getIdOrden(), ""+ren1.getIdEvaluacion()+"-"+ren1.getSubPartida());
                                        ordena.add(nuevo);
                                    }
                                }

                                if(aux.getTipoPedido().compareTo("Externo")==0)
                                {
                                    PartidaExterna [] par=(PartidaExterna[])aux.getPartidaExternas().toArray(new PartidaExterna[0]);
                                    for(int b=0; b<par.length; b++)
                                    {
                                        PartidaExterna ren2=par[b];
                                        Renglon nuevo;
                                        nuevo=new Renglon(""+aux.getIdPedido(), ren2.getNoParte(), ren2.getDescripcion(), ren2.getCantidad(),  ren2.getUnidad(),  ren2.getCosto(), "", ""+"EXT");
                                        ordena.add(nuevo);
                                    }
                                }

                                if(aux.getTipoPedido().compareTo("Adicional")==0)
                                {
                                    PartidaExterna [] par=(PartidaExterna[])aux.getPartidaExternas().toArray(new PartidaExterna[0]);
                                    for(int b=0; b<par.length; b++)
                                    {
                                        PartidaExterna ren2=par[b];
                                        Renglon nuevo;
                                        nuevo=new Renglon(""+aux.getIdPedido(), ren2.getNoParte(), ren2.getDescripcion(), ren2.getCantidad(),  ren2.getUnidad(),  ren2.getCosto(), ""+aux.getOrden().getIdOrden(), "DIR");
                                        ordena.add(nuevo);
                                    }
                                }
                                if(aux.getTipoPedido().compareTo("Inventario")==0)
                                {
                                    PartidaExterna [] par=(PartidaExterna[])aux.getPartidaExternas().toArray(new PartidaExterna[0]);
                                    for(int b=0; b<par.length; b++)
                                    {
                                        PartidaExterna ren2=par[b];
                                        Renglon nuevo;
                                        nuevo=new Renglon(""+aux.getIdPedido(), ren2.getEjemplar().getIdParte(), ren2.getDescripcion(), ren2.getCantidad(),  ren2.getUnidad(),  ren2.getCosto(), "", "INV");
                                        ordena.add(nuevo);
                                    }
                                }
                            }
                            
                            Collections.sort(ordena, new Comparator() {
                                @Override
                                public int compare(Object o1, Object o2) {
                                    Renglon p1=(Renglon) o1;
                                    Renglon p2=(Renglon) o2;
                                    return new String(p1.np+p1.descripcion).compareTo(new String(p2.np+p2.descripcion));
                                }
                            });
                            
                            for(int c=0; c<ordena.size(); c++)
                            {
                                Renglon r1=(Renglon)ordena.get(c);
                                tabla.addCell(reporte.celda(r1.pedido, font, contenido, derecha, 0,1,Rectangle.RECTANGLE));
                                tabla.addCell(reporte.celda(r1.np, font, contenido, derecha, 0,1,Rectangle.RECTANGLE));
                                tabla.addCell(reporte.celda(r1.descripcion, font, contenido, izquierda, 0,1,Rectangle.RECTANGLE));
                                tabla.addCell(reporte.celda(formatoPorcentaje.format(r1.cant), font, contenido, derecha, 0,1,Rectangle.RECTANGLE));
                                tabla.addCell(reporte.celda(r1.med, font, contenido, derecha, 0,1,Rectangle.RECTANGLE));
                                tabla.addCell(reporte.celda(formatoPorcentaje.format(r1.precio), font, contenido, derecha, 0,1,Rectangle.RECTANGLE));
                                tabla.addCell(reporte.celda(""+r1.orden, font, contenido, centro, 0,1,Rectangle.RECTANGLE));
                                tabla.addCell(reporte.celda(r1.partida, font, contenido, derecha, 0,1,Rectangle.RECTANGLE));
                            }
                        }
                        
                        tabla.setHeaderRows(2);
                        reporte.agregaObjeto(tabla);
                        reporte.cerrar();
                        reporte.visualizar2(ruta+".pdf");
                    }catch(Exception e)
                    {
                        System.out.println(e);
                        e.printStackTrace();
                        JOptionPane.showMessageDialog(this, "No se pudo realizar el reporte si el archivo esta abierto.");
                    }
                    finally
                    {
                        if(session!=null)
                            if(session.isOpen())
                                session.close();
                    }
                }
            }
        }
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:
        h=new Herramientas(usr, 0);
        h.session(sessionPrograma);
        if(t_datos.getRowCount()>0)
        {
            Session session = HibernateUtil.getSessionFactory().openSession();
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
                        DecimalFormat formatoPorcentaje = new DecimalFormat("#,##0.00");
                        formatoPorcentaje.setMinimumFractionDigits(2);
                        session.beginTransaction().begin();
                        if(archivoXLS.exists())
                            archivoXLS.delete();
                        archivoXLS.createNewFile();
                        Workbook libro = new HSSFWorkbook();
                        FileOutputStream archivo = new FileOutputStream(archivoXLS);
                        Sheet hoja = libro.createSheet("VALUACION");
                        
                        Date fecha = new Date();
                        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyyHH-mm-ss");//YYYY-MM-DD HH:MM:SS
                        String valor=dateFormat.format(fecha);

                        Object no[];
                        int ren[];
                        if(t_datos.getSelectedRows().length>0)
                        {
                            no=new  Object[t_datos.getSelectedRows().length];
                            ren =t_datos.getSelectedRows();
                            for(int x=0; x<t_datos.getSelectedRows().length; x++)
                            {
                                no[x]=(int)t_datos.getValueAt(ren[x], 0);
                            }
                        }
                        else
                        {
                            no=new  Object[t_datos.getRowCount()];
                            for(int x=0; x<t_datos.getRowCount(); x++)
                            {
                                no[x]=(int)t_datos.getValueAt(x, 0);
                            }
                        }
                        Pedido[] pedidos;
                        if(autorizado.isSelected()==true)
                        {
                            pedidos = (Pedido[]) session.createCriteria(Pedido.class)
                                .add(Restrictions.and(Restrictions.isNotNull("usuarioByAutorizo") , 
                                        Restrictions.in("idPedido", no)) 
                                ).list().toArray(new Pedido[0]);
                        }
                        else
                        {
                            pedidos = (Pedido[]) session.createCriteria(Pedido.class)
                                .add(Restrictions.in("idPedido", no)) 
                                .list().toArray(new Pedido[0]);
                        }
                        if(pedidos.length>0)
                        {
                            ArrayList ordena=new ArrayList();
                            for(int a=0; a<pedidos.length; a++)
                            {
                                Pedido aux=pedidos[a];
                                if(aux.getTipoPedido().compareTo("Interno")==0)
                                {
                                    Partida [] par=(Partida[])aux.getPartidas().toArray(new Partida[0]);
                                    for(int b=0; b<par.length; b++)
                                    {
                                        Partida ren1=par[b];
                                        Renglon nuevo;
                                        if(ren1.getEjemplar()!=null)
                                            nuevo=new Renglon(""+aux.getIdPedido(), ren1.getEjemplar().getIdParte(), ren1.getCatalogo().getNombre(), ren1.getCantPcp(),  ren1.getMed(),  ren1.getPcp(), ""+ren1.getOrdenByIdOrden().getIdOrden(), ""+ren1.getIdEvaluacion()+"-"+ren1.getSubPartida());
                                        else
                                            nuevo=new Renglon(""+aux.getIdPedido(), "", ren1.getCatalogo().getNombre(), ren1.getCantPcp(),  ren1.getMed(),  ren1.getPcp(), ""+ren1.getOrdenByIdOrden().getIdOrden(), ""+ren1.getIdEvaluacion()+"-"+ren1.getSubPartida());
                                        ordena.add(nuevo);
                                    }
                                }

                                if(aux.getTipoPedido().compareTo("Externo")==0)
                                {
                                    PartidaExterna [] par=(PartidaExterna[])aux.getPartidaExternas().toArray(new PartidaExterna[0]);
                                    for(int b=0; b<par.length; b++)
                                    {
                                        PartidaExterna ren2=par[b];
                                        Renglon nuevo;
                                        nuevo=new Renglon(""+aux.getIdPedido(), ren2.getNoParte(), ren2.getDescripcion(), ren2.getCantidad(),  ren2.getUnidad(),  ren2.getCosto(), "", ""+"Ext");
                                        ordena.add(nuevo);
                                    }
                                }

                                if(aux.getTipoPedido().compareTo("Adicional")==0)
                                {
                                    PartidaExterna [] par=(PartidaExterna[])aux.getPartidaExternas().toArray(new PartidaExterna[0]);
                                    for(int b=0; b<par.length; b++)
                                    {
                                        PartidaExterna ren2=par[b];
                                        Renglon nuevo;
                                        nuevo=new Renglon(""+aux.getIdPedido(), ren2.getNoParte(), ren2.getDescripcion(), ren2.getCantidad(),  ren2.getUnidad(),  ren2.getCosto(), ""+aux.getOrden().getIdOrden(), "DIR");
                                        ordena.add(nuevo);
                                    }
                                }
                                if(aux.getTipoPedido().compareTo("Inventario")==0)
                                {
                                    PartidaExterna [] par=(PartidaExterna[])aux.getPartidaExternas().toArray(new PartidaExterna[0]);
                                    for(int b=0; b<par.length; b++)
                                    {
                                        PartidaExterna ren2=par[b];
                                        Renglon nuevo;
                                        nuevo=new Renglon(""+aux.getIdPedido(), ren2.getEjemplar().getIdParte(), ren2.getDescripcion(), ren2.getCantidad(),  ren2.getUnidad(),  ren2.getCosto(), "", "INV");
                                        ordena.add(nuevo);
                                    }
                                }
                            }
                            
                            Collections.sort(ordena, new Comparator() {
                                @Override
                                public int compare(Object o1, Object o2) {
                                    Renglon p1=(Renglon) o1;
                                    Renglon p2=(Renglon) o2;
                                    return new String(p1.np+p1.descripcion).compareTo(new String(p2.np+p2.descripcion));
                                }
                            });
                            
                            
                            
                            for(int c=0; c<ordena.size()+1; c++)
                            {
                                Row fila = hoja.createRow(c);
                                
                                Cell celda0 = fila.createCell(0);
                                Cell celda1 = fila.createCell(1);
                                Cell celda2 = fila.createCell(2);
                                Cell celda3 = fila.createCell(3);
                                Cell celda4 = fila.createCell(4);
                                Cell celda5 = fila.createCell(5);
                                Cell celda6 = fila.createCell(6);
                                Cell celda7 = fila.createCell(7);
                                if(c==0)
                                {
                                    celda0.setCellValue("Pedido");
                                    celda1.setCellValue("NP");
                                    celda2.setCellValue("Descripcion");
                                    celda3.setCellValue("Cant");
                                    celda4.setCellValue("Med");
                                    celda5.setCellValue("Precio");
                                    celda6.setCellValue("Orden");
                                    celda7.setCellValue("Partida");
                                }
                                else
                                {
                                    Renglon r1=(Renglon)ordena.get(c-1);
                                    celda0.setCellValue(r1.pedido);
                                    celda1.setCellValue(r1.np);
                                    celda2.setCellValue(r1.descripcion);
                                    celda3.setCellValue(formatoPorcentaje.format(r1.cant));
                                    celda4.setCellValue(r1.med);
                                    celda5.setCellValue(formatoPorcentaje.format(r1.precio));
                                    celda6.setCellValue(""+r1.orden);
                                    celda7.setCellValue(r1.partida);
                                }
                            }
                            
                        }
                        libro.write(archivo);
                        archivo.close();
                        Desktop.getDesktop().open(archivoXLS);
                    }catch(Exception e)
                    {
                        System.out.println(e);
                        e.printStackTrace();
                        JOptionPane.showMessageDialog(this, "No se pudo realizar el reporte si el archivo esta abierto.");
                    }
                    finally
                    {
                        if(session!=null)
                            if(session.isOpen())
                                session.close();
                    }
                }
            }
        }
    }//GEN-LAST:event_jButton5ActionPerformed


public void cabecera(PDF reporte, BaseFont bf, PdfPTable tabla)
   {
        reporte.contenido.setLineWidth(0.5f);
        reporte.contenido.setColorStroke(new GrayColor(0.2f));
        reporte.contenido.setColorFill(new GrayColor(0.9f));
        reporte.contenido.roundRectangle(30, 700, 210, 45, 5);
        reporte.contenido.roundRectangle(250, 700, 325, 45, 5);
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction().begin();
        Configuracion con= (Configuracion)session.get(Configuracion.class, 1);
        reporte.inicioTexto();
        reporte.contenido.setFontAndSize(bf, 14);
        reporte.contenido.setColorFill(BaseColor.BLACK);
        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, con.getEmpresa(), 30, 585, 0);
        reporte.contenido.setFontAndSize(bf, 8);
        reporte.contenido.setColorFill(BaseColor.BLACK);
        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Reporte de pedidos", 30, 575, 0);
        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_RIGHT, "Fecha:"+new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date()), 750, 585, 0);
        reporte.finTexto();
        if(session!=null)
            if(session.isOpen())
                session.close();
        
            //agregamos renglones vacios para dejar un espacio
            reporte.agregaObjeto(new Paragraph(" "));
            
            Font font = new Font(Font.FontFamily.HELVETICA, 6, Font.BOLD);
            
            BaseColor cabecera=BaseColor.GRAY;
            BaseColor contenido=BaseColor.WHITE;
            int centro=Element.ALIGN_CENTER;
            int izquierda=Element.ALIGN_LEFT;
            int derecha=Element.ALIGN_RIGHT;
        
            tabla.addCell(reporte.celda("Pedido", font, cabecera, centro, 0, 1, Rectangle.RECTANGLE));
            tabla.addCell(reporte.celda("Fecha", font, cabecera, centro, 0, 1,Rectangle.RECTANGLE));
            tabla.addCell(reporte.celda("O. Taller", font, cabecera, centro, 0,1, Rectangle.RECTANGLE));
            tabla.addCell(reporte.celda("Usuario", font, cabecera, centro, 0, 1,Rectangle.RECTANGLE));
            tabla.addCell(reporte.celda("Prov.", font, cabecera, centro, 0,1, Rectangle.RECTANGLE));
            tabla.addCell(reporte.celda("Nombre de Proveedor", font, cabecera, centro, 0,1, Rectangle.RECTANGLE));
            tabla.addCell(reporte.celda("Facturar a", font, cabecera, centro, 0,1,Rectangle.RECTANGLE));
            tabla.addCell(reporte.celda("Observaciones", font, cabecera, centro, 0,1, Rectangle.RECTANGLE));
            tabla.addCell(reporte.celda("Autorizo1", font, cabecera, centro, 0, 1, Rectangle.RECTANGLE));
            tabla.addCell(reporte.celda("Monto Tot", font, cabecera, centro, 0,1, Rectangle.RECTANGLE));
    }

public void cabeceraReporte(PDF reporte, BaseFont bf, PdfPTable tabla)
   {
        reporte.contenido.setLineWidth(0.5f);
        reporte.contenido.setColorStroke(new GrayColor(0.2f));
        reporte.contenido.setColorFill(new GrayColor(0.9f));
        reporte.contenido.roundRectangle(30, 700, 210, 45, 5);
        reporte.contenido.roundRectangle(250, 700, 325, 45, 5);
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction().begin();
        Configuracion con= (Configuracion)session.get(Configuracion.class, 1);
        reporte.inicioTexto();
        reporte.contenido.setFontAndSize(bf, 14);
        reporte.contenido.setColorFill(BaseColor.BLACK);
        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, con.getEmpresa(), 30, 585, 0);
        reporte.contenido.setFontAndSize(bf, 8);
        reporte.contenido.setColorFill(BaseColor.BLACK);
        String cabeza="Reporte";
        if(l_id_cliente.getText().compareTo("")!=0 && t_datos.getRowCount()>0)
        {
            cabeza+=" Proveedor: "+t_datos.getValueAt(0, 5).toString();
        }
        if(t_fecha1.getText().compareTo("AAAA-MM-DD")!=0)
            cabeza+=" del: "+t_fecha1.getText();
        if(t_fecha2.getText().compareTo("AAAA-MM-DD")!=0)
            cabeza+="  al: "+t_fecha2.getText();
        if(t_orden.getText().compareTo("")!=0)
            cabeza+="  Orden: "+t_orden.getText();
        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, cabeza, 30, 575, 0);
        String texto="";
        int [] lista = t_datos.getSelectedRows();
        if(t_datos.getSelectedRows().length>0)
        {
            for(int x=0; x<t_datos.getSelectedRows().length; x++)
            {
                texto += t_datos.getValueAt(lista[x], 0)+", ";
            }
        }
        else
        {
            if(t_datos.getRowCount()>0)
            {
                for(int x=0; x<t_datos.getRowCount(); x++)
                {
                    texto += t_datos.getValueAt(x, 0)+", ";
                }
            }
        }
        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_RIGHT, "Fecha:"+new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date()), 750, 585, 0);
        reporte.finTexto();
        if(session!=null)
            if(session.isOpen())
                session.close();
        
            //agregamos renglones vacios para dejar un espacio
            reporte.agregaObjeto(new Paragraph(" "));
            
            Font font = new Font(Font.FontFamily.HELVETICA, 7, Font.BOLD);
            
            BaseColor cabecera=BaseColor.GRAY;
            BaseColor contenido=BaseColor.WHITE;
            int centro=Element.ALIGN_CENTER;
            int izquierda=Element.ALIGN_LEFT;
            int derecha=Element.ALIGN_RIGHT;
        
            tabla.addCell(reporte.celda("MATERIAL PEDIDOS:"+texto, font, cabecera, centro, 8, 1, Rectangle.RECTANGLE));
            tabla.addCell(reporte.celda("PEDIDO", font, cabecera, centro, 0, 1, Rectangle.RECTANGLE));
            tabla.addCell(reporte.celda("NP", font, cabecera, centro, 0, 1, Rectangle.RECTANGLE));
            tabla.addCell(reporte.celda("DESCRIPCIÓN", font, cabecera, centro, 0, 1,Rectangle.RECTANGLE));
            tabla.addCell(reporte.celda("CANT", font, cabecera, centro, 0,1, Rectangle.RECTANGLE));
            tabla.addCell(reporte.celda("MED", font, cabecera, centro, 0, 1,Rectangle.RECTANGLE));
            tabla.addCell(reporte.celda("PRECIO", font, cabecera, centro, 0,1, Rectangle.RECTANGLE));
            tabla.addCell(reporte.celda("ORDEN", font, cabecera, centro, 0,1, Rectangle.RECTANGLE));
            tabla.addCell(reporte.celda("PARTIDA", font, cabecera, centro, 0,1, Rectangle.RECTANGLE));
    }

    public class Renglon
    {
        String pedido="";
        String np="";
        String descripcion="";
        double cant=0.0d;
        String med="";
        double precio=0.0d;
        String orden="";
        String partida="";
        
        public Renglon(String pedido, String np, String descripcion,  double cant,  String med,  double precio, String orden, String partida)
        {
            this.pedido=pedido;
            this.np=np;
            this.descripcion=descripcion;
            this.cant=cant;
            this.med=med;
            this.precio=precio;
            this.orden=orden;
            this.partida=partida;
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox autorizado;
    private javax.swing.JButton b_busca_cliente;
    public javax.swing.JButton b_busca_orden;
    private javax.swing.JButton b_fecha_siniestro;
    private javax.swing.JButton b_fecha_siniestro1;
    private javax.swing.JComboBox c_orden;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JTextField l_id_cliente;
    private javax.swing.JScrollPane scroll;
    private javax.swing.JTable t_datos;
    private javax.swing.JTextField t_fecha1;
    private javax.swing.JTextField t_fecha2;
    public javax.swing.JTextField t_orden;
    // End of variables declaration//GEN-END:variables
}
