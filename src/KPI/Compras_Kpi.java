/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package KPI;

import Hibernate.Util.HibernateUtil;
import Hibernate.entidades.Empleado;
import Hibernate.entidades.Orden;
import Hibernate.entidades.Usuario;
import Integral.ExtensionFileFilter;
import Integral.FormatoTabla;
import Integral.Render1;
import Integral.calendario;
import Servicios.buscaOrden;
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
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author Sistemas
 */
public class Compras_Kpi extends javax.swing.JPanel {

    Usuario usr;
    FormatoTabla formato;
    int configuracion=1;
    /**
     * Creates new form Consulta_Kpi
     */
    public Compras_Kpi(Usuario usr, int configuracion) {
        initComponents();
        this.usr = usr;
        this.configuracion=configuracion;
        carga_compradores();
        formato = new FormatoTabla();
        formatoTabla();
    }

    public void formatoTabla()
    {
        Color c1 = new java.awt.Color(2, 135, 242);
        for(int x=0; x<t_unidades.getColumnModel().getColumnCount(); x++)
            t_unidades.getColumnModel().getColumn(x).setHeaderRenderer(new Render1(c1));
        tabla_tamaños();
        t_unidades.setShowVerticalLines(true);
        t_unidades.setShowHorizontalLines(true);
        
        t_unidades.setDefaultRenderer(String.class, formato); 
        t_unidades.setDefaultRenderer(Double.class, formato); 
        t_unidades.setDefaultRenderer(Integer.class, formato);
        t_unidades.setDefaultRenderer(Boolean.class, formato);
    }
    
    public void tabla_tamaños()
    {
        TableColumnModel col_model = t_unidades.getColumnModel();

        for (int i=0; i<t_unidades.getColumnCount(); i++)
        {
  	      TableColumn column = col_model.getColumn(i);
              switch(i)
              {
                  case 0:
                      column.setPreferredWidth(60);
                      break;
                  case 1:
                      column.setPreferredWidth(300);
                      break;
                  case 2:
                      column.setPreferredWidth(70);
                      break;
                  case 3:
                      column.setPreferredWidth(80);
                      break;
                  case 4:
                      column.setPreferredWidth(250);
                      break;
                  case 5:
                      column.setPreferredWidth(90);
                      break;
                  case 6:
                      column.setPreferredWidth(90);
                      break;
                  case 12:
                      column.setPreferredWidth(90);
                      break;
                  default:
                      column.setPreferredWidth(60);
                      break;
                      
              }
        }
        JTableHeader header = t_unidades.getTableHeader();
        header.setForeground(Color.white);
    }
    
    private List<Object[]> executeHQLQuery(String hql) 
    {
        try {
            Session session = HibernateUtil.getSessionFactory().openSession();
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
    }
    void carga_compradores()
    {
        try {
            List <Object[]> resultList = executeHQLQuery("FROM Empleado em WHERE em.puestos.nombre='COMPRADOR'");
            DefaultListModel<String> modelo_unidades = new DefaultListModel<>();
            JList<String> list = new JList<>( modelo_unidades );
            modelo_unidades.removeAllElements();
            modelo_unidades.addElement("TODOS");
            l_comprador_unidades.setModel(modelo_unidades);
            
            if(resultList.size()>0)
            {
                for (Object o : resultList){
                    Empleado obj = (Empleado) o;
                    modelo_unidades.addElement(obj.getNombre());
                }
            }
        } catch (HibernateException he) {
            he.printStackTrace();
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

        jPanel2 = new javax.swing.JPanel();
        jPanel15 = new javax.swing.JPanel();
        jPanel17 = new javax.swing.JPanel();
        b_fecha_1_pedidos = new javax.swing.JButton();
        t_fecha_1_pedidos = new javax.swing.JTextField();
        b_fecha_2_pedidos = new javax.swing.JButton();
        t_fecha_2_pedidos = new javax.swing.JTextField();
        jPanel18 = new javax.swing.JPanel();
        jScrollPane7 = new javax.swing.JScrollPane();
        l_comprador_pedidos = new javax.swing.JList();
        b_buscar_pedidos = new javax.swing.JButton();
        jPanel16 = new javax.swing.JPanel();
        b_excel_pedidos = new javax.swing.JButton();
        b_pdf_pedidos = new javax.swing.JButton();
        jScrollPane6 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jPanel11 = new javax.swing.JPanel();
        jPanel12 = new javax.swing.JPanel();
        jPanel14 = new javax.swing.JPanel();
        b_fecha_1_material = new javax.swing.JButton();
        t_fecha_1_material = new javax.swing.JTextField();
        b_fecha_2_material = new javax.swing.JButton();
        t_fecha_2_material = new javax.swing.JTextField();
        b_ot = new javax.swing.JButton();
        t_orden = new javax.swing.JTextField();
        b_buscar_material = new javax.swing.JButton();
        jPanel13 = new javax.swing.JPanel();
        b_excel_material = new javax.swing.JButton();
        b_pdf_material = new javax.swing.JButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        t_material = new javax.swing.JTable();
        b_pdf_unidades = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        p_unidades = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        b_fecha_1_unidades = new javax.swing.JButton();
        t_fecha_1_unidades = new javax.swing.JTextField();
        b_fecha_2_unidades = new javax.swing.JButton();
        t_fecha_2_unidades = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        t_ot = new javax.swing.JTextField();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        l_comprador_unidades = new javax.swing.JList();
        b_buscar_unidades = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        t_unidades = new javax.swing.JTable();
        jPanel6 = new javax.swing.JPanel();
        b_exel_unidades = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        t_surtir = new javax.swing.JFormattedTextField();
        t_reales = new javax.swing.JFormattedTextField();
        t_promedio = new javax.swing.JFormattedTextField();

        jPanel2.setLayout(new java.awt.BorderLayout());

        jPanel15.setBackground(new java.awt.Color(255, 255, 255));

        jPanel17.setBackground(new java.awt.Color(255, 255, 255));
        jPanel17.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "fecha Asignación", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP));

        b_fecha_1_pedidos.setBackground(new java.awt.Color(2, 135, 242));
        b_fecha_1_pedidos.setText("Desde");
        b_fecha_1_pedidos.setToolTipText("Calendario");
        b_fecha_1_pedidos.setMaximumSize(new java.awt.Dimension(32, 8));
        b_fecha_1_pedidos.setMinimumSize(new java.awt.Dimension(32, 8));
        b_fecha_1_pedidos.setPreferredSize(new java.awt.Dimension(32, 8));
        b_fecha_1_pedidos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_fecha_1_pedidosActionPerformed(evt);
            }
        });

        t_fecha_1_pedidos.setEditable(false);
        t_fecha_1_pedidos.setBackground(new java.awt.Color(204, 255, 255));
        t_fecha_1_pedidos.setText("AAAA-MM-DD");
        t_fecha_1_pedidos.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        t_fecha_1_pedidos.setEnabled(false);

        b_fecha_2_pedidos.setBackground(new java.awt.Color(2, 135, 242));
        b_fecha_2_pedidos.setText("hasta");
        b_fecha_2_pedidos.setToolTipText("Calendario");
        b_fecha_2_pedidos.setMaximumSize(new java.awt.Dimension(32, 8));
        b_fecha_2_pedidos.setMinimumSize(new java.awt.Dimension(32, 8));
        b_fecha_2_pedidos.setPreferredSize(new java.awt.Dimension(32, 8));
        b_fecha_2_pedidos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_fecha_2_pedidosActionPerformed(evt);
            }
        });

        t_fecha_2_pedidos.setEditable(false);
        t_fecha_2_pedidos.setBackground(new java.awt.Color(204, 255, 255));
        t_fecha_2_pedidos.setText("AAAA-MM-DD");
        t_fecha_2_pedidos.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        t_fecha_2_pedidos.setEnabled(false);
        t_fecha_2_pedidos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                t_fecha_2_pedidosActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel17Layout = new javax.swing.GroupLayout(jPanel17);
        jPanel17.setLayout(jPanel17Layout);
        jPanel17Layout.setHorizontalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(b_fecha_2_pedidos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(b_fecha_1_pedidos, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(t_fecha_1_pedidos, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(t_fecha_2_pedidos, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel17Layout.setVerticalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(b_fecha_1_pedidos, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(t_fecha_1_pedidos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(b_fecha_2_pedidos, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(t_fecha_2_pedidos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel18.setBackground(new java.awt.Color(255, 255, 255));
        jPanel18.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "Comprador", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP));

        l_comprador_pedidos.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "TODOS" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane7.setViewportView(l_comprador_pedidos);

        javax.swing.GroupLayout jPanel18Layout = new javax.swing.GroupLayout(jPanel18);
        jPanel18.setLayout(jPanel18Layout);
        jPanel18Layout.setHorizontalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane7, javax.swing.GroupLayout.DEFAULT_SIZE, 833, Short.MAX_VALUE)
        );
        jPanel18Layout.setVerticalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        b_buscar_pedidos.setText("jButton3");

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addComponent(jPanel17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(b_buscar_pedidos)
                .addContainerGap())
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel17, javax.swing.GroupLayout.DEFAULT_SIZE, 0, Short.MAX_VALUE)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(b_buscar_pedidos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel2.add(jPanel15, java.awt.BorderLayout.PAGE_START);

        jPanel16.setBackground(new java.awt.Color(255, 255, 255));

        b_excel_pedidos.setText("Excel");

        b_pdf_pedidos.setText("PDF");

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addComponent(b_pdf_pedidos)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(b_excel_pedidos)
                .addGap(0, 1015, Short.MAX_VALUE))
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(b_pdf_pedidos)
                .addComponent(b_excel_pedidos))
        );

        jPanel2.add(jPanel16, java.awt.BorderLayout.PAGE_END);

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "OT", "NO", "#", "TIPO", "DESCRIPCIÓN", "H", "M", "S", "E", "P", "CANT", "MED", "CODIGO", "ORI", "PROVEEDOR", "PLAZO", "# PEDIDO", "SOLICITADO", "ALM", "OP", "PENDIENTE", "FACTURA"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        jTable1.getTableHeader().setReorderingAllowed(false);
        jScrollPane6.setViewportView(jTable1);

        jPanel2.add(jScrollPane6, java.awt.BorderLayout.CENTER);

        jPanel11.setLayout(new java.awt.BorderLayout());

        jPanel12.setBackground(new java.awt.Color(255, 255, 255));

        jPanel14.setBackground(new java.awt.Color(255, 255, 255));
        jPanel14.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "fecha Asignación", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP));

        b_fecha_1_material.setBackground(new java.awt.Color(2, 135, 242));
        b_fecha_1_material.setText("Desde");
        b_fecha_1_material.setToolTipText("Calendario");
        b_fecha_1_material.setMaximumSize(new java.awt.Dimension(32, 8));
        b_fecha_1_material.setMinimumSize(new java.awt.Dimension(32, 8));
        b_fecha_1_material.setPreferredSize(new java.awt.Dimension(32, 8));
        b_fecha_1_material.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_fecha_1_materialActionPerformed(evt);
            }
        });

        t_fecha_1_material.setEditable(false);
        t_fecha_1_material.setBackground(new java.awt.Color(204, 255, 255));
        t_fecha_1_material.setText("AAAA-MM-DD");
        t_fecha_1_material.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        t_fecha_1_material.setEnabled(false);

        b_fecha_2_material.setBackground(new java.awt.Color(2, 135, 242));
        b_fecha_2_material.setText("hasta");
        b_fecha_2_material.setToolTipText("Calendario");
        b_fecha_2_material.setMaximumSize(new java.awt.Dimension(32, 8));
        b_fecha_2_material.setMinimumSize(new java.awt.Dimension(32, 8));
        b_fecha_2_material.setPreferredSize(new java.awt.Dimension(32, 8));
        b_fecha_2_material.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_fecha_2_materialActionPerformed(evt);
            }
        });

        t_fecha_2_material.setEditable(false);
        t_fecha_2_material.setBackground(new java.awt.Color(204, 255, 255));
        t_fecha_2_material.setText("AAAA-MM-DD");
        t_fecha_2_material.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        t_fecha_2_material.setEnabled(false);
        t_fecha_2_material.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                t_fecha_2_materialActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addComponent(b_fecha_1_material, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(t_fecha_1_material, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(b_fecha_2_material, javax.swing.GroupLayout.DEFAULT_SIZE, 85, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(t_fecha_2_material, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(b_fecha_1_material, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(t_fecha_1_material, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(b_fecha_2_material, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(t_fecha_2_material, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        b_ot.setBackground(new java.awt.Color(2, 135, 242));
        b_ot.setText("OT:");
        b_ot.setToolTipText("Consultar clientes");
        b_ot.setMaximumSize(new java.awt.Dimension(32, 8));
        b_ot.setMinimumSize(new java.awt.Dimension(32, 8));
        b_ot.setPreferredSize(new java.awt.Dimension(32, 8));
        b_ot.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_otActionPerformed(evt);
            }
        });

        t_orden.setEditable(false);
        t_orden.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                t_ordenActionPerformed(evt);
            }
        });

        b_buscar_material.setText("jButton9");

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(b_ot, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(t_orden, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 505, Short.MAX_VALUE)
                .addComponent(b_buscar_material)
                .addContainerGap())
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(b_ot, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(t_orden, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(b_buscar_material)))
        );

        jPanel11.add(jPanel12, java.awt.BorderLayout.PAGE_START);

        jPanel13.setBackground(new java.awt.Color(255, 255, 255));

        b_excel_material.setText("Excel");

        b_pdf_material.setText("PDF");

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addComponent(b_pdf_material)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(b_excel_material)
                .addGap(0, 1015, Short.MAX_VALUE))
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(b_pdf_material)
                .addComponent(b_excel_material))
        );

        jPanel11.add(jPanel13, java.awt.BorderLayout.PAGE_END);

        t_material.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "OT", "MARCA", "TIPO", "PLACAS", "N.E.", "T. SINIESTRO", "ASIGNADA", "COMPRADOR", "ALM", "OP", "% ENTREGADO"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        t_material.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        t_material.getTableHeader().setReorderingAllowed(false);
        jScrollPane4.setViewportView(t_material);

        jPanel11.add(jScrollPane4, java.awt.BorderLayout.CENTER);

        b_pdf_unidades.setText("PDF");
        b_pdf_unidades.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_pdf_unidadesActionPerformed(evt);
            }
        });

        setBackground(new java.awt.Color(255, 255, 255));
        setLayout(new java.awt.BorderLayout());

        jPanel1.setLayout(new java.awt.BorderLayout());

        jLabel1.setBackground(new java.awt.Color(255, 255, 255));
        jLabel1.setFont(new java.awt.Font("Arial Black", 0, 11)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Indice de Rendimiento Individual de Compras y Almacen");
        jLabel1.setOpaque(true);
        jPanel1.add(jLabel1, java.awt.BorderLayout.NORTH);

        p_unidades.setLayout(new java.awt.BorderLayout());

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "fecha Asignación", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP));

        b_fecha_1_unidades.setBackground(new java.awt.Color(2, 135, 242));
        b_fecha_1_unidades.setText("Desde");
        b_fecha_1_unidades.setToolTipText("Calendario");
        b_fecha_1_unidades.setMaximumSize(new java.awt.Dimension(32, 8));
        b_fecha_1_unidades.setMinimumSize(new java.awt.Dimension(32, 8));
        b_fecha_1_unidades.setPreferredSize(new java.awt.Dimension(32, 8));
        b_fecha_1_unidades.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_fecha_1_unidadesActionPerformed(evt);
            }
        });

        t_fecha_1_unidades.setEditable(false);
        t_fecha_1_unidades.setBackground(new java.awt.Color(204, 255, 255));
        t_fecha_1_unidades.setText("AAAA-MM-DD");
        t_fecha_1_unidades.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        t_fecha_1_unidades.setEnabled(false);

        b_fecha_2_unidades.setBackground(new java.awt.Color(2, 135, 242));
        b_fecha_2_unidades.setText("hasta");
        b_fecha_2_unidades.setToolTipText("Calendario");
        b_fecha_2_unidades.setMaximumSize(new java.awt.Dimension(32, 8));
        b_fecha_2_unidades.setMinimumSize(new java.awt.Dimension(32, 8));
        b_fecha_2_unidades.setPreferredSize(new java.awt.Dimension(32, 8));
        b_fecha_2_unidades.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_fecha_2_unidadesActionPerformed(evt);
            }
        });

        t_fecha_2_unidades.setEditable(false);
        t_fecha_2_unidades.setBackground(new java.awt.Color(204, 255, 255));
        t_fecha_2_unidades.setText("AAAA-MM-DD");
        t_fecha_2_unidades.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        t_fecha_2_unidades.setEnabled(false);
        t_fecha_2_unidades.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                t_fecha_2_unidadesActionPerformed(evt);
            }
        });

        jLabel5.setText("OT:");

        t_ot.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                t_otFocusLost(evt);
            }
        });
        t_ot.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                t_otKeyTyped(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(b_fecha_2_unidades, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(b_fecha_1_unidades, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel5))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(t_fecha_1_unidades, javax.swing.GroupLayout.DEFAULT_SIZE, 88, Short.MAX_VALUE)
                    .addComponent(t_fecha_2_unidades, javax.swing.GroupLayout.DEFAULT_SIZE, 88, Short.MAX_VALUE)
                    .addComponent(t_ot))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(b_fecha_1_unidades, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(t_fecha_1_unidades, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(b_fecha_2_unidades, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(t_fecha_2_unidades, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(t_ot, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));
        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "Comprador", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP));

        l_comprador_unidades.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "TODOS" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        l_comprador_unidades.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                l_comprador_unidadesValueChanged(evt);
            }
        });
        jScrollPane3.setViewportView(l_comprador_unidades);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 834, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        b_buscar_unidades.setText("Buscar");
        b_buscar_unidades.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_buscar_unidadesActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(b_buscar_unidades)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(b_buscar_unidades, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        p_unidades.add(jPanel4, java.awt.BorderLayout.PAGE_START);

        t_unidades.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Ot", "Cliente", "Siniestro", "Tipo", "Comprador", "Asignacion", "Meta", "Partidas", "Surtidas", "Pendientes", "Reales", "Avance(%)", "Promedio", "F. Termino", "Retraso", "Dias Meta", "Dias Real", "Promedio", "Complementos", "Monto"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Double.class, java.lang.Double.class, java.lang.String.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        t_unidades.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        t_unidades.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(t_unidades);

        p_unidades.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));

        b_exel_unidades.setText("Excel");
        b_exel_unidades.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_exel_unidadesActionPerformed(evt);
            }
        });

        jLabel2.setText("Cant. de Items a surtir:");

        jLabel3.setText("Cant. de Items Surtidos:");

        jLabel4.setText("Promedio Gral:");

        t_surtir.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        t_surtir.setText("0.00");
        t_surtir.setEnabled(false);

        t_reales.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        t_reales.setText("0.00");
        t_reales.setEnabled(false);

        t_promedio.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        t_promedio.setText("0.00");
        t_promedio.setEnabled(false);

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(t_surtir, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(28, 28, 28)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(t_reales, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(t_promedio, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 457, Short.MAX_VALUE)
                .addComponent(b_exel_unidades)
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(b_exel_unidades)
                .addComponent(jLabel2)
                .addComponent(jLabel3)
                .addComponent(jLabel4)
                .addComponent(t_surtir, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(t_reales, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(t_promedio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        p_unidades.add(jPanel6, java.awt.BorderLayout.PAGE_END);

        jPanel1.add(p_unidades, java.awt.BorderLayout.CENTER);

        add(jPanel1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void t_ordenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_t_ordenActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_t_ordenActionPerformed

    private void b_otActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_otActionPerformed
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
    }//GEN-LAST:event_b_otActionPerformed

    private void t_fecha_2_materialActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_t_fecha_2_materialActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_t_fecha_2_materialActionPerformed

    private void b_fecha_2_materialActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_fecha_2_materialActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_b_fecha_2_materialActionPerformed

    private void b_fecha_1_materialActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_fecha_1_materialActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_b_fecha_1_materialActionPerformed

    private void t_fecha_2_pedidosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_t_fecha_2_pedidosActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_t_fecha_2_pedidosActionPerformed

    private void b_fecha_2_pedidosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_fecha_2_pedidosActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_b_fecha_2_pedidosActionPerformed

    private void b_fecha_1_pedidosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_fecha_1_pedidosActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_b_fecha_1_pedidosActionPerformed

    private void b_buscar_unidadesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_buscar_unidadesActionPerformed
        // TODO add your handling code here:
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            if(t_ot.getText().compareTo("")!=0 || (t_fecha_1_unidades.getText().compareTo("AAAA-MM-DD")!=0 && t_fecha_2_unidades.getText().compareTo("AAAA-MM-DD")!=0) )
            {
                String fecha="";
                if(t_fecha_1_unidades.getText().compareTo("AAAA-MM-DD")!=0)
                {
                    if(t_fecha_2_unidades.getText().compareTo("AAAA-MM-DD")!=0)
                    {
                        fecha="WHERE inicio_refacciones>='"+t_fecha_1_unidades.getText()+"' AND inicio_refacciones<='"+t_fecha_2_unidades.getText()+"'";
                    }
                }
                if(t_ot.getText().compareTo("")!=0)
                    fecha="WHERE orden.id_orden="+t_ot.getText();
                session.beginTransaction().begin();
                String consulta="select orden.id_orden as ot, clientes.nombre as cliente, reparacion.nombre as reparacion, tipo_nombre, empleado.nombre, DATE_FORMAT(inicio_refacciones,'%Y-%m-%d') as asignacion, meta_refacciones as meta, (select if(sum(1) is null, 0, sum(1)) from partida where id_orden=ot and partida.ref_comp=true and partida.autorizado_valuacion=true and partida.autorizado=true and partida.tipo='o') as total_partidas, partidas_surtidas(orden.id_orden, 'o') as surtidas, (select DATE_FORMAT(fecha,'%Y-%m-%d') from almacen \n" +
                "inner join movimiento on almacen.id_almacen=movimiento.id_almacen " +
                "inner join partida on movimiento.id_partida=partida.id_partida where almacen.tipo_movimiento=1 and operacion=1 and partida.id_orden=ot order by almacen.id_almacen desc limit 1) as f_termino, workdaydiff(meta_refacciones, DATE_FORMAT(inicio_refacciones,'%Y-%m-%d')) as t_surtimiento, partidas_reales(orden.id_orden, meta_refacciones, 'o') as partidas_reales, " +
                "(select if(sum(1) is null, 0, sum(1)) from partida where id_orden=ot and partida.ref_comp=true and partida.autorizado_valuacion=true and partida.autorizado=true and partida.tipo in ('c', 'a')) as complemento, " +
                "(select if(sum(cant_pcp*pcp) is null, 0, sum(cant_pcp*pcp)) from partida where id_orden=ot and partida.ref_comp=true and partida.autorizado_valuacion=true and partida.autorizado=true and partida.tipo in ('c', 'a')) as monto_complemento\n" +
                "from orden left join clientes on orden.id_cliente=clientes.id_clientes " +
                "left join reparacion on orden.id_reparacion=reparacion.id_reparacion " +
                "left join empleado on orden.r_refacciones=empleado.id_empleado ";
                consulta+=fecha;

                if(l_comprador_unidades.getSelectedValuesList()!=null)
                {
                    if(l_comprador_unidades.getSelectedValuesList().size()>0)
                    {
                        if(l_comprador_unidades.getSelectedValue().toString().compareTo("TODOS")!=0)
                        {
                            for(int x=0; x<l_comprador_unidades.getSelectedValuesList().size(); x++)
                            {
                                consulta+="AND ";
                                consulta+="empleado.nombre='"+l_comprador_unidades.getSelectedValuesList().get(x).toString()+"' ";
                            }
                        }
                    }
                }
                Query query = session.createSQLQuery(consulta);
                query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
                ArrayList resultList=(ArrayList)query.list();
                DefaultTableModel modelo=(DefaultTableModel)t_unidades.getModel();
                modelo.setNumRows(0);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                if(resultList.size()>0)
                {
                    for (int i=0; i<resultList.size(); i++) {
                        java.util.HashMap obj=(java.util.HashMap)resultList.get(i);
                        double total_partidas=Double.parseDouble(obj.get("total_partidas").toString());
                        double surtidas=Double.parseDouble(obj.get("surtidas").toString());
                        double pendientes=total_partidas - surtidas;
                        Date asignacion=simpleDateFormat.parse(obj.get("asignacion").toString());
                        Date meta=null;
                        if(obj.get("meta")!=null)
                            meta=dateFormat.parse(obj.get("meta").toString());
                        Date f_termino=null;
                        if(pendientes==0 && obj.get("f_termino")!=null)
                            f_termino=dateFormat.parse(obj.get("f_termino").toString());
                        double promedio=0, avance=-1, dato=0;
                        if(total_partidas!=0)
                            avance=surtidas*100/total_partidas;
                        if(f_termino!=null && pendientes==0 && meta!=null && Integer.parseInt(comparaFecha(asignacion,f_termino).toString())>0)
                            promedio=Integer.parseInt(comparaFecha(asignacion,meta).toString())*100/Integer.parseInt(comparaFecha(asignacion,f_termino).toString());
                        if(total_partidas!=0)
                            dato=Integer.parseInt(obj.get("partidas_reales").toString())*100/total_partidas;

                        modelo.addRow(new Object[]{
                            obj.get("ot").toString(),
                            obj.get("cliente"),
                            obj.get("reparacion"),
                            obj.get("tipo_nombre"),
                            obj.get("nombre"),
                            obj.get("asignacion"),
                            (obj.get("meta")==null ?null:obj.get("meta").toString()),
                            total_partidas,
                            surtidas,
                            pendientes,
                            obj.get("partidas_reales"),
                            (avance>-1 ? avance:null),
                            (dato==-1 ?null:dato),
                            (f_termino!=null ? dateFormat.format(f_termino):null),
                            (f_termino!=null && meta!=null ? comparaFecha(meta,f_termino):null),
                            (meta!=null ? comparaFecha(asignacion,meta):null),
                            (f_termino!=null && pendientes==0 ? comparaFecha(asignacion,f_termino):null),
                            promedio,
                            Integer.parseInt(obj.get("complemento").toString()),
                            obj.get("monto_complemento")
                        });
                    }
                }
                Query query1 = session.createSQLQuery("select (select if(sum(1) is null, 0, sum(1)) from partida inner join orden on orden.id_orden=partida.id_orden where partida.ref_comp=true and partida.autorizado_valuacion=true and partida.autorizado=true and partida.tipo='o' and orden.meta_refacciones>='"+t_fecha_1_unidades.getText()+"' and orden.meta_refacciones<='"+t_fecha_2_unidades.getText()+"' and orden.r_refacciones is not null) as surtir, " +
                "(select sum(partidas_reales(id_orden, meta_refacciones, 'o')) from orden where orden.meta_refacciones>='"+t_fecha_1_unidades.getText()+"' and orden.meta_refacciones<='"+t_fecha_2_unidades.getText()+"' and orden.r_refacciones is not null) as reales;");
                query1.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
                ArrayList resultList1=(ArrayList)query1.list();
                for (int i=0; i<resultList1.size(); i++) {
                    java.util.HashMap obj1=(java.util.HashMap)resultList1.get(i);
                    double surtir=0.0, reales=0.0;
                    if(obj1.get("surtir")!=null)
                        surtir=Double.parseDouble(obj1.get("surtir").toString());
                    if(obj1.get("reales")!=null)
                        reales=Double.parseDouble(obj1.get("reales").toString());

                    t_surtir.setValue(surtir);
                    t_reales.setValue(reales);
                    if(surtir>0)
                        t_promedio.setValue(reales*100/surtir);
                    else
                        t_promedio.setValue(0);
                }
            }
            else
            {
                JOptionPane.showMessageDialog(this, "Faltan Datos");
            }
        }catch(Exception e)
        {
            session.beginTransaction().rollback();
            e.printStackTrace();
        }
        finally
        {
            if(session.isOpen()==true)
            session.close();
        }
    }//GEN-LAST:event_b_buscar_unidadesActionPerformed

    private void l_comprador_unidadesValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_l_comprador_unidadesValueChanged
        // TODO add your handling code here:
        if (!evt.getValueIsAdjusting()) {
            if(l_comprador_unidades.getSelectedValue()!=null)
            {
                String item = l_comprador_unidades.getSelectedValue().toString();
                System.out.println(item);
                if (item.equals("TODOS")==true) {
                    //l_comprador_unidades.removeSelectionInterval(0, l_comprador_unidades.getMaxSelectionIndex());
                    l_comprador_unidades.setSelectedIndex(0);
                    DefaultTableModel modelo = (DefaultTableModel)t_unidades.getModel();
                    modelo.setNumRows(0);
                }
            }
        }
    }//GEN-LAST:event_l_comprador_unidadesValueChanged

    private void t_fecha_2_unidadesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_t_fecha_2_unidadesActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_t_fecha_2_unidadesActionPerformed

    private void b_fecha_2_unidadesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_fecha_2_unidadesActionPerformed
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
            t_fecha_2_unidades.setText(anio+"-"+mes+"-"+dia);
            t_ot.setText("");
        }
        else
        t_fecha_2_unidades.setText("AAAA-MM-DD");
        DefaultTableModel mode = (DefaultTableModel)t_unidades.getModel();
        mode.setNumRows(0);
    }//GEN-LAST:event_b_fecha_2_unidadesActionPerformed

    private void b_fecha_1_unidadesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_fecha_1_unidadesActionPerformed
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
            t_fecha_1_unidades.setText(anio+"-"+mes+"-"+dia);
            t_ot.setText("");
        }
        else
        t_fecha_1_unidades.setText("AAAA-MM-DD");
        DefaultTableModel modelo = (DefaultTableModel)t_unidades.getModel();
        modelo.setNumRows(0);
    }//GEN-LAST:event_b_fecha_1_unidadesActionPerformed

    private void b_exel_unidadesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_exel_unidadesActionPerformed
        // TODO add your handling code here:
        javax.swing.JFileChooser jF1= new javax.swing.JFileChooser(); 
        jF1.setFileFilter(new ExtensionFileFilter("Excel document (*.xls)", new String[] { "xls" }));
        String ruta = null; 
        DecimalFormat formatoPorcentaje = new DecimalFormat("#,##0.00");
        formatoPorcentaje.setMinimumFractionDigits(2);
        if(jF1.showSaveDialog(null)==jF1.APPROVE_OPTION)
        { 
            ruta = jF1.getSelectedFile().getAbsolutePath(); 
            if(ruta!=null)
            {
                File archivoXLS = new File(ruta+".xls");
                File plantilla = new File("imagenes/plantillaKPI.xls");
                Session session = HibernateUtil.getSessionFactory().openSession();
                try
                {
                    Path FROM = Paths.get("imagenes/plantillaKPI.xls");
                    Path TO = Paths.get(ruta+".xls");
                    //sobreescribir el fichero de destino, si existe, y copiar
                    // los atributos, incluyendo los permisos rwx
                    CopyOption[] options = new CopyOption[]
                    {
                        StandardCopyOption.REPLACE_EXISTING,
                        StandardCopyOption.COPY_ATTRIBUTES
                    }; 
                    Files.copy(FROM, TO, options);
                    FileInputStream miPlantilla = new FileInputStream(archivoXLS);
                    POIFSFileSystem fsFileSystem = new POIFSFileSystem(miPlantilla);
                    Workbook libro = new HSSFWorkbook(fsFileSystem);
                    
                    CellStyle borde_d = libro.createCellStyle();
                    borde_d.setBorderBottom(CellStyle.BORDER_THIN);
                    borde_d.setBorderTop(CellStyle.BORDER_THIN);
                    borde_d.setBorderRight(CellStyle.BORDER_THIN);
                    borde_d.setBorderLeft(CellStyle.BORDER_THIN);
                    borde_d.setAlignment(CellStyle.ALIGN_RIGHT);
                    
                    CellStyle borde_i = libro.createCellStyle();
                    borde_i.setBorderBottom(CellStyle.BORDER_THIN);
                    borde_i.setBorderTop(CellStyle.BORDER_THIN);
                    borde_i.setBorderRight(CellStyle.BORDER_THIN);
                    borde_i.setBorderLeft(CellStyle.BORDER_THIN);
                    borde_i.setAlignment(CellStyle.ALIGN_LEFT);
                    
                    Sheet hoja = libro.getSheet("KPI");
                    Sheet grafica = libro.getSheet("GRAFICA");
                    CellStyle verde=grafica.getRow(0).getCell(0).getCellStyle();
                    CellStyle azul=grafica.getRow(0).getCell(1).getCellStyle();
                    CellStyle rojo=grafica.getRow(0).getCell(2).getCellStyle();
                    CellStyle amarillo=grafica.getRow(0).getCell(3).getCellStyle();
                    
                    CellStyle borde_c = libro.createCellStyle();
                    borde_c.setAlignment(CellStyle.ALIGN_LEFT);
                    
                    Row fila0 = hoja.getRow(1);
                    Cell celda0 = fila0.getCell(2);
                    celda0.setCellValue("del "+t_fecha_1_unidades.getText()+" al "+t_fecha_2_unidades.getText());
                    
                    for(int ren=0;ren<(t_unidades.getRowCount());ren++)
                    {
                        Row fila = hoja.createRow(ren+4);
                        Cell celda1 = fila.createCell(0);
                        celda1.setCellValue(t_unidades.getValueAt(ren, 0).toString());
                        celda1.setCellStyle(borde_i);
                        Cell celda2 = fila.createCell(1);
                        celda2.setCellValue(t_unidades.getValueAt(ren, 1).toString());
                        celda2.setCellStyle(borde_i);
                        Cell celda3 = fila.createCell(2);
                        celda3.setCellValue(t_unidades.getValueAt(ren, 2).toString());
                        celda3.setCellStyle(borde_i);
                        Cell celda4 = fila.createCell(3);
                        celda4.setCellValue(t_unidades.getValueAt(ren, 3).toString());
                        celda4.setCellStyle(borde_i);
                        Cell celda5 = fila.createCell(4);
                        celda5.setCellValue(t_unidades.getValueAt(ren, 4).toString());
                        celda5.setCellStyle(borde_i);
                        Cell celda6 = fila.createCell(5);
                        celda6.setCellValue(t_unidades.getValueAt(ren, 5).toString());
                        celda6.setCellStyle(borde_i);
                        Cell celda7 = fila.createCell(6);
                        celda7.setCellValue((t_unidades.getValueAt(ren, 6)!=null ? t_unidades.getValueAt(ren, 6).toString():""));
                        celda7.setCellStyle(borde_i);
                        Cell celda8 = fila.createCell(7);
                        celda8.setCellValue(new BigDecimal(t_unidades.getValueAt(ren, 7).toString()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
                        celda8.setCellStyle(amarillo);
                        Cell celda9 = fila.createCell(8);
                        celda9.setCellValue(new BigDecimal(t_unidades.getValueAt(ren, 8).toString()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
                        celda9.setCellStyle(borde_d);
                        Cell celda10 = fila.createCell(9);
                        celda10.setCellValue(new BigDecimal(t_unidades.getValueAt(ren, 9).toString()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
                        celda10.setCellStyle(borde_d);
                        Cell celda11 = fila.createCell(10);
                        if(t_unidades.getValueAt(ren, 11)!=null)
                        {
                            celda11.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                            celda11.setCellValue(new BigDecimal(t_unidades.getValueAt(ren, 11).toString()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
                        }
                        else
                            celda11.setCellValue("");
                        celda11.setCellStyle(borde_d);
                        Cell celda12 = fila.createCell(11);
                        celda12.setCellValue((t_unidades.getValueAt(ren, 13)!=null ? t_unidades.getValueAt(ren, 13).toString():""));
                        celda12.setCellStyle(borde_i);
                        Cell celda13 = fila.createCell(12);
                        if(t_unidades.getValueAt(ren, 14)!=null)
                        {
                            celda13.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                            celda13.setCellValue(new BigDecimal(t_unidades.getValueAt(ren, 14).toString()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
                        }
                        else
                            celda13.setCellValue("");
                        celda13.setCellStyle(borde_d);
                        Cell celda14 = fila.createCell(13);
                        celda14.setCellValue("");
                        Cell celda15 = fila.createCell(14);
                        celda15.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                        celda15.setCellValue((t_unidades.getValueAt(ren, 10)!=null ? new BigDecimal(t_unidades.getValueAt(ren, 10).toString()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue():null));
                        celda15.setCellStyle(amarillo);
                        Cell celda16 = fila.createCell(15);
                        celda16.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                        BigDecimal c16=new BigDecimal(t_unidades.getValueAt(ren, 12).toString());
                        celda16.setCellValue(c16.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
                        //if(t_unidades.getValueAt(ren, 16)!=null)
                        //{
                            if(c16.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue()>=90)
                            {
                                celda16.setCellStyle(verde);
                                celda14.setCellStyle(azul);
                            }
                            else
                            {
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                                if(t_unidades.getValueAt(ren, 6)!=null)
                                {
                                    Date f_meta=simpleDateFormat.parse(t_unidades.getValueAt(ren, 6).toString());
                                    Date f_reporte=new Date();
                                    if(t_fecha_2_unidades.getText().compareTo("AAAA-MM-DD")!=0)
                                        f_reporte= simpleDateFormat.parse(t_fecha_2_unidades.getText());
                                    //Date f_reporte= new Date();
                                    if(f_meta.before(f_reporte) || f_meta.equals(f_reporte))
                                        celda16.setCellStyle(rojo);
                                    else
                                        celda16.setCellStyle(borde_d);
                                    celda14.setCellStyle(borde_d);
                                }
                                else
                                {
                                    celda16.setCellStyle(borde_d);
                                    celda14.setCellStyle(borde_d);
                                }
                            }
                        /*}
                        else
                        {
                            celda16.setCellStyle(borde_d);
                            celda14.setCellStyle(borde_d);
                        }*/
                            
                        Cell celda17 = fila.createCell(16);
                        if(t_unidades.getValueAt(ren, 15)!=null)
                        {
                            celda17.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                            celda17.setCellValue(new BigDecimal(t_unidades.getValueAt(ren, 15).toString()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
                        }
                        else
                            celda17.setCellValue("");
                        celda17.setCellStyle(borde_d);
                        
                        Cell celda18 = fila.createCell(17);
                        if(t_unidades.getValueAt(ren, 16)!=null)
                        {
                            celda18.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                            celda18.setCellValue(new BigDecimal(t_unidades.getValueAt(ren, 16).toString()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
                        }
                        else
                            celda18.setCellValue("");
                        celda18.setCellStyle(borde_d);
                        
                        Cell celda19 = fila.createCell(18);
                        celda19.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                        if(t_unidades.getValueAt(ren, 17)!=null)
                            celda19.setCellValue(new BigDecimal(t_unidades.getValueAt(ren, 17).toString()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
                        else
                            celda19.setCellValue(0.00d);
                            
                        celda19.setCellStyle(borde_d);
                        
                        Cell celda20 = fila.createCell(19);
                        celda20.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                        BigDecimal promedio2=new BigDecimal(0.0), promedio3=null;
                        if(t_unidades.getValueAt(ren, 12)!=null || t_unidades.getValueAt(ren, 17)!=null)
                        {
                            promedio2 = new BigDecimal(t_unidades.getValueAt(ren, 17).toString()).setScale(2, BigDecimal.ROUND_HALF_UP);
                            promedio3 = new BigDecimal(t_unidades.getValueAt(ren, 12).toString()).setScale(2, BigDecimal.ROUND_HALF_UP);
                            if(promedio2.doubleValue()>0 && promedio3.doubleValue()>0)
                                promedio2 = promedio2.multiply(new BigDecimal(100)).divide(promedio3, 2, BigDecimal.ROUND_HALF_UP);
                        }
                        celda20.setCellValue(promedio2.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
                        celda20.setCellStyle(borde_d);
                        Cell celda21 = fila.createCell(20);
                        celda21.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                        celda21.setCellValue(new BigDecimal(t_unidades.getValueAt(ren, 18).toString()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
                        celda21.setCellStyle(borde_d);
                        Cell celda22 = fila.createCell(21);
                        celda22.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                        celda22.setCellValue(new BigDecimal(t_unidades.getValueAt(ren, 19).toString()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
                        celda22.setCellStyle(borde_d);
                    }
                    Row fila_1 = hoja.createRow(t_unidades.getRowCount()+5);
                    Row fila_2 = hoja.createRow(t_unidades.getRowCount()+6);
                    Row fila_3 = hoja.createRow(t_unidades.getRowCount()+7);
                    Cell c_1 = fila_1.createCell(1);
                    c_1.setCellValue("Items a Surtir:");
                    c_1.setCellStyle(borde_d);
                    
                    Cell c_2 = fila_1.createCell(2);
                    c_2.setCellValue(t_surtir.getText());
                    c_2.setCellStyle(borde_d);
                    
                    Cell ce_1 = fila_2.createCell(1);
                    ce_1.setCellValue("Items Surtidos:");
                    ce_1.setCellStyle(borde_d);
                    
                    Cell ce_2 = fila_2.createCell(2);
                    ce_2.setCellValue(t_reales.getText());
                    ce_2.setCellStyle(borde_d);
                    
                    Cell cel_1 = fila_3.createCell(1);
                    cel_1.setCellValue("Promedio Depto:");
                    cel_1.setCellStyle(borde_d);
                    
                    Cell cel_2 = fila_3.createCell(2);
                    cel_2.setCellValue(t_promedio.getText());
                    cel_2.setCellStyle(borde_d);
                            
                    FileOutputStream archivo = new FileOutputStream(archivoXLS);
                    libro.write(archivo);
                    archivo.close();
                    Desktop.getDesktop().open(archivoXLS);
                }catch(Exception e)
                {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(this, "No se pudo realizar el reporte si el archivo esta abierto");
                }
            }
        }
    }//GEN-LAST:event_b_exel_unidadesActionPerformed

    private void b_pdf_unidadesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_pdf_unidadesActionPerformed
        // TODO add your handling code here:
        
    }//GEN-LAST:event_b_pdf_unidadesActionPerformed

    private void t_otFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_t_otFocusLost
        // TODO add your handling code here:
        if(t_ot.getText().compareTo("")!=0)
        {
            t_fecha_1_unidades.setText("AAAA-MM-DD");
            t_fecha_2_unidades.setText("AAAA-MM-DD");
        }
    }//GEN-LAST:event_t_otFocusLost

    private void t_otKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_otKeyTyped
        // TODO add your handling code here:
        char car = evt.getKeyChar();
        if(t_ot.getSelectedText()!=null)
            t_ot.setText(t_ot.getText().replace(t_ot.getSelectedText(), ""));
        
        if(t_ot.getText().length()>=6)
        {
            evt.consume();
        }
        if((car<'0' || car>'9')) 
            evt.consume();
    }//GEN-LAST:event_t_otKeyTyped


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton b_buscar_material;
    private javax.swing.JButton b_buscar_pedidos;
    private javax.swing.JButton b_buscar_unidades;
    private javax.swing.JButton b_excel_material;
    private javax.swing.JButton b_excel_pedidos;
    private javax.swing.JButton b_exel_unidades;
    private javax.swing.JButton b_fecha_1_material;
    private javax.swing.JButton b_fecha_1_pedidos;
    private javax.swing.JButton b_fecha_1_unidades;
    private javax.swing.JButton b_fecha_2_material;
    private javax.swing.JButton b_fecha_2_pedidos;
    private javax.swing.JButton b_fecha_2_unidades;
    private javax.swing.JButton b_ot;
    private javax.swing.JButton b_pdf_material;
    private javax.swing.JButton b_pdf_pedidos;
    private javax.swing.JButton b_pdf_unidades;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JTable jTable1;
    private javax.swing.JList l_comprador_pedidos;
    private javax.swing.JList l_comprador_unidades;
    private javax.swing.JPanel p_unidades;
    private javax.swing.JTextField t_fecha_1_material;
    private javax.swing.JTextField t_fecha_1_pedidos;
    private javax.swing.JTextField t_fecha_1_unidades;
    private javax.swing.JTextField t_fecha_2_material;
    private javax.swing.JTextField t_fecha_2_pedidos;
    private javax.swing.JTextField t_fecha_2_unidades;
    private javax.swing.JTable t_material;
    private javax.swing.JTextField t_orden;
    private javax.swing.JTextField t_ot;
    private javax.swing.JFormattedTextField t_promedio;
    private javax.swing.JFormattedTextField t_reales;
    private javax.swing.JFormattedTextField t_surtir;
    private javax.swing.JTable t_unidades;
    // End of variables declaration//GEN-END:variables


    Object comparaFecha(Date fechaInicial ,Date fechaFinal)
    {
        if(fechaInicial!=null && fechaFinal!=null)
        {
            Calendar calInicial = Calendar.getInstance();
            Calendar calFinal = Calendar.getInstance();
            Date aux;
            int modificador=1;
            if(fechaInicial.after(fechaFinal))
            {
                aux=fechaInicial;
                fechaInicial=fechaFinal;
                fechaFinal=aux;
                modificador=-1;
            }
            calInicial.setTime(fechaInicial);
            calFinal.setTime(fechaFinal);
            int diffDays= 0;
            while (fechaInicial.before(fechaFinal)) {
                if (calInicial.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY && calInicial.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY) {
                    diffDays++;
                }
                calInicial.add(Calendar.DATE, 1);
                fechaInicial=calInicial.getTime();
            }
            return diffDays*modificador;
        }
        else
        {
            return null;
        }
    }
}
