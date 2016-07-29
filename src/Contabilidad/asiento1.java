/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Contabilidad;

import Hibernate.Util.HibernateUtil;
import Hibernate.entidades.ArchivoFactura;
import Hibernate.entidades.Asiento;
import Hibernate.entidades.Cuentas;
import Hibernate.entidades.Orden;
import Hibernate.entidades.Registro;
import Hibernate.entidades.Usuario;
import Integral.FormatoTabla;
import Integral.Herramientas;
import Integral.Render1;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.Vector;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author Sistemas
 */
public class asiento1 extends javax.swing.JPanel {

    Usuario user;
    Asiento asiento;
    String titulo = "";
    Orden ord;
    String sessionPrograma = "";
    Herramientas h;
    public String nombre;
    public int aux = 0;
    ArrayList elimina = new ArrayList();
    DefaultTableModel model, cuenta;
    FormatoTabla formato;
    String tipo="";

    String[] columnas = new String[]{
        "Registro", "Cuenta", "Depto.", "Concepto", "T. Cambio", "Debe", "Haber"
    };

    /**
     * Creates new form asiento1
     */
    public asiento1(Usuario u, Asiento asc, int index, String edo, String ses, String tipo) {
        initComponents();
        user = u;
        this.tipo=tipo;
        sessionPrograma = ses;
        asiento = asc;
        t_datos.setModel(ModeloTablaReporte(0, columnas));
        formato = new FormatoTabla();
        consultaAsiento();
        t_datos1.setValueAt("Total Factura:", 0, 0);
        t_datos1.setValueAt(t_datos1.getValueAt(0, 5), 0, 1);
        formatoTabla();
        formatoTabla1();
    }

    public void consultaAsiento() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            Registro[] registros = (Registro[]) session.createCriteria(Registro.class).createAlias("asiento", "asc").add(Restrictions.eq("asc.idAsiento", asiento.getIdAsiento())).add(Restrictions.eq("tipoAsiento", tipo)).addOrder(Order.desc("tipo")).addOrder(Order.asc("idRegistro")).list().toArray(new Registro[0]);
            ArchivoFactura factura = (ArchivoFactura) session.createCriteria(ArchivoFactura.class).createAlias("reclamo", "rec").add(Restrictions.eq("rec.idReclamo", asiento.getReclamo().getIdReclamo())).add(Restrictions.eq("estatus", "v")).uniqueResult();

            asiento = (Asiento) session.get(Asiento.class, asiento.getIdAsiento());
            t_reclamo.setText("" + asiento.getReclamo().getIdReclamo());
            t_proveedor.setText(asiento.getReclamo().getProveedor().getNombre());
            t_folio.setText(factura.getFolio());

            t_datos.setModel(ModeloTablaReporte(registros.length, columnas));
            double d = 0.00d;
            double a = 0.00d;
            for (int x = 0; x < registros.length; x++) {
                t_datos.setValueAt(registros[x].getIdRegistro(), x, 0);
                t_datos.setValueAt(registros[x].getCuentas().getIdCuentas(), x, 1);
                t_datos.setValueAt(registros[x].getDepto(), x, 2);
                t_datos.setValueAt(registros[x].getConcepto(), x, 3);
                t_datos.setValueAt(registros[x].getCambio(), x, 4);
                if (registros[x].getTipo().compareTo("d") == 0) {
                    t_datos.setValueAt(registros[x].getCantidad(), x, 5);
                    d += registros[x].getCantidad();
                } else {
                    t_datos.setValueAt(registros[x].getCantidad(), x, 6);
                    a += registros[x].getCantidad();
                }
            }
            t_datos1.setValueAt(d, 0, 5);
            t_datos1.setValueAt(a, 0, 6);
        } catch (Exception e) {
            session.beginTransaction().rollback();
            e.printStackTrace();
        }
        if (session != null) {
            if (session.isOpen()) {
                session.close();
            }
        }
    }

    public void tabla_tamaños() {
        TableColumnModel col_model = t_datos.getColumnModel();
        for (int i = 0; i < t_datos.getColumnCount(); i++) {
            TableColumn column = col_model.getColumn(i);
            switch (i) {
                case 0:
                    column.setPreferredWidth(40);
                    break;
                case 1:
                    column.setPreferredWidth(100);
                    break;
                case 2:
                    column.setPreferredWidth(40);
                    break;
                case 3:
                    column.setPreferredWidth(300);
                    break;
                case 4:
                    column.setPreferredWidth(40);
                    break;
                default:
                    column.setPreferredWidth(100);
                    break;
            }
        }
        JTableHeader header = t_datos.getTableHeader();
        header.setForeground(Color.white);
    }

    public void tabla_tamaños1() {
        TableColumnModel col_model = t_datos1.getColumnModel();
        for (int i = 0; i < t_datos1.getColumnCount(); i++) {
            TableColumn column = col_model.getColumn(i);
            switch (i) {
                case 0:
                    column.setPreferredWidth(40);
                    break;
                case 1:
                    column.setPreferredWidth(100);
                    break;
                case 2:
                    column.setPreferredWidth(40);
                    break;
                case 3:
                    column.setPreferredWidth(300);
                    break;
                case 4:
                    column.setPreferredWidth(40);
                    break;
                default:
                    column.setPreferredWidth(100);
                    break;
            }
        }
        JTableHeader header = t_datos.getTableHeader();
        header.setForeground(Color.white);
    }

    public void tabla_tamaños2() {
        TableColumnModel col_model = tabla_cuentas.getColumnModel();
        for (int i = 0; i < tabla_cuentas.getColumnCount(); i++) {
            TableColumn column = col_model.getColumn(i);
            switch (i) {
                case 0:
                    column.setPreferredWidth(90);
                    break;
                case 1:
                    column.setPreferredWidth(30);
                    break;
                case 2:
                    column.setPreferredWidth(320);
                    break;
            }
        }
        JTableHeader header = tabla_cuentas.getTableHeader();
        header.setForeground(Color.black);
    }

    public String[] getColumnas_Cuentas() {
        String columnas_cuentas[] = new String[]{"N° de Cuenta", "Saldo", "Nombres"};
        return columnas_cuentas;
    }

    public void formatoTabla() {
        Color c1 = new java.awt.Color(2, 135, 242);
        for (int x = 0; x < t_datos.getColumnModel().getColumnCount(); x++) {
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

    public void formatoTabla1() {
        Color c1 = new java.awt.Color(2, 135, 242);
        for (int x = 0; x < t_datos1.getColumnModel().getColumnCount(); x++) {
            t_datos1.getColumnModel().getColumn(x).setHeaderRenderer(new Render1(c1));
        }
        tabla_tamaños1();
        t_datos1.setShowVerticalLines(true);
        t_datos1.setShowHorizontalLines(true);
        t_datos1.setDefaultRenderer(Double.class, formato);
        t_datos1.setDefaultRenderer(Integer.class, formato);
        t_datos1.setDefaultRenderer(String.class, formato);
        t_datos1.setDefaultRenderer(Boolean.class, formato);
    }

    DefaultTableModel ModeloTablaReporte(int renglones, String columnas[]) {
        model = new DefaultTableModel(new Object[renglones][6], columnas) {
            Class[] types = new Class[]{
                java.lang.Integer.class,
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class,
                java.lang.Double.class,
                java.lang.Double.class,
                java.lang.Double.class
            };
            boolean[] canEdit = new boolean[]{
                false, false, true, true, true, true, true
            };

            public void setValueAt(Object value, int row, int col) {
                Vector vector = (Vector) this.dataVector.elementAt(row);
                Object celda = ((Vector) this.dataVector.elementAt(row)).elementAt(col);
                switch (col) {
                    case 2:
                        if (vector.get(col) == null) {
                            vector.setElementAt(value, col);
                            this.dataVector.setElementAt(vector, row);
                            fireTableCellUpdated(row, col);
                        } else {
                            if (((String) value).length() <= 3) {
                                vector.setElementAt(value, col);
                                this.dataVector.setElementAt(vector, row);
                                fireTableCellUpdated(row, col);
                            } else {
                                String aux = (String) value;
                                aux = aux.substring(0, 3);
                                vector.setElementAt(aux, col);
                                this.dataVector.setElementAt(vector, row);
                                fireTableCellUpdated(row, col);
                            }
                        }
                        break;
                     case 3:
                        if (vector.get(col) == null) {
                            vector.setElementAt(value, col);
                            this.dataVector.setElementAt(vector, row);
                            fireTableCellUpdated(row, col);
                        } else {
                            if (((String) value).length() <= 119) {
                                vector.setElementAt(value, col);
                                this.dataVector.setElementAt(vector, row);
                                fireTableCellUpdated(row, col);
                            } else {
                                String aux = (String) value;
                                aux = aux.substring(0, 119);
                                vector.setElementAt(aux, col);
                                this.dataVector.setElementAt(vector, row);
                                fireTableCellUpdated(row, col);
                            }
                        }
                        break;
                    case 5:
                        vector.setElementAt(value, col);
                        vector.setElementAt(null, 6);
                        this.dataVector.setElementAt(vector, row);
                        fireTableCellUpdated(row, col);
                        fireTableCellUpdated(row, 6);
                        totales();
                        break;
                    case 6:
                        vector.setElementAt(value, col);
                        vector.setElementAt(null, 5);
                        this.dataVector.setElementAt(vector, row);
                        fireTableCellUpdated(row, col);
                        fireTableCellUpdated(row, 5);
                        totales();
                        break;
                    default:
                        vector.setElementAt(value, col);
                        this.dataVector.setElementAt(vector, row);
                        fireTableCellUpdated(row, col);
                        break;
                }
            }

            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
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

        cuentas = new javax.swing.JDialog();
        jLabel2 = new javax.swing.JLabel();
        tipo_cuenta = new javax.swing.JComboBox();
        texto_cuenta = new javax.swing.JTextField();
        jButton5 = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        tabla_cuentas = new javax.swing.JTable(){
            public boolean isCellEditable(int row, int column){
                return false;
            }
        };
        jButton6 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        t_datos1 = new javax.swing.JTable();
        jScrollPane1 = new javax.swing.JScrollPane();
        t_datos = new javax.swing.JTable();
        jButton3 = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        t_folio = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        t_proveedor = new javax.swing.JTextField();
        t_reclamo = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jButton4 = new javax.swing.JButton();

        cuentas.setBackground(new java.awt.Color(255, 255, 255));
        cuentas.setModalityType(java.awt.Dialog.ModalityType.TOOLKIT_MODAL);

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel2.setText("Buscar por:");

        tipo_cuenta.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        tipo_cuenta.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "N° Cuenta", "Nombre" }));
        tipo_cuenta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tipo_cuentaActionPerformed(evt);
            }
        });

        texto_cuenta.setBackground(new java.awt.Color(204, 255, 255));
        texto_cuenta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                texto_cuentaActionPerformed(evt);
            }
        });
        texto_cuenta.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                texto_cuentaKeyTyped(evt);
            }
        });

        jButton5.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jButton5.setText("Buscar");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        tabla_cuentas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "N° de Cuenta", "Saldo", "Nombre"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class
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
        tabla_cuentas.setFillsViewportHeight(true);
        tabla_cuentas.getTableHeader().setResizingAllowed(false);
        tabla_cuentas.getTableHeader().setReorderingAllowed(false);
        tabla_cuentas.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabla_cuentasMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(tabla_cuentas);

        jButton6.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jButton6.setText("Agregar");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout cuentasLayout = new javax.swing.GroupLayout(cuentas.getContentPane());
        cuentas.getContentPane().setLayout(cuentasLayout);
        cuentasLayout.setHorizontalGroup(
            cuentasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cuentasLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(cuentasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 580, Short.MAX_VALUE)
                    .addGroup(cuentasLayout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tipo_cuenta, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(texto_cuenta)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton5))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, cuentasLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButton6)))
                .addContainerGap())
        );
        cuentasLayout.setVerticalGroup(
            cuentasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cuentasLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(cuentasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(tipo_cuenta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(texto_cuenta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 410, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton6)
                .addGap(12, 12, 12))
        );

        setBackground(new java.awt.Color(255, 255, 255));

        jPanel1.setBackground(new java.awt.Color(2, 135, 242));

        t_datos1.setFont(new java.awt.Font("Arial", 0, 10)); // NOI18N
        t_datos1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "", "", "", "", "", "", ""
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        t_datos1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        t_datos1.setEnabled(false);
        t_datos1.setFillsViewportHeight(true);
        t_datos1.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        t_datos1.getTableHeader().setReorderingAllowed(false);
        jScrollPane2.setViewportView(t_datos1);

        t_datos.setFont(new java.awt.Font("Arial", 0, 10)); // NOI18N
        t_datos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "Registro", "Cuenta", "Depto.", "Concepto", "T. Cambio", "Debe", "Haber"
            }
        ));
        t_datos.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        t_datos.setFillsViewportHeight(true);
        t_datos.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        t_datos.getTableHeader().setReorderingAllowed(false);
        t_datos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                t_datosMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(t_datos);

        jButton3.setFont(new java.awt.Font("Arial", 0, 9)); // NOI18N
        jButton3.setText("Actualizar");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton1.setFont(new java.awt.Font("Arial", 0, 9)); // NOI18N
        jButton1.setIcon(new ImageIcon("imagenes/boton_menos.png"));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setFont(new java.awt.Font("Arial", 0, 9)); // NOI18N
        jButton2.setIcon(new ImageIcon("imagenes/boton_mas.png"));
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        t_folio.setEditable(false);
        t_folio.setFont(new java.awt.Font("Arial", 0, 10)); // NOI18N
        t_folio.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(2, 135, 242)));
        t_folio.setDisabledTextColor(new java.awt.Color(0, 0, 0));

        jLabel3.setFont(new java.awt.Font("Arial", 0, 10)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Folio:");

        t_proveedor.setEditable(false);
        t_proveedor.setFont(new java.awt.Font("Arial", 0, 10)); // NOI18N
        t_proveedor.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(2, 135, 242)));
        t_proveedor.setDisabledTextColor(new java.awt.Color(0, 0, 0));

        t_reclamo.setEditable(false);
        t_reclamo.setFont(new java.awt.Font("Arial", 0, 10)); // NOI18N
        t_reclamo.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(2, 135, 242)));
        t_reclamo.setDisabledTextColor(new java.awt.Color(0, 0, 0));

        jLabel1.setFont(new java.awt.Font("Arial", 0, 10)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Reclamo:");

        jButton4.setFont(new java.awt.Font("Arial", 0, 9)); // NOI18N
        jButton4.setText("Restaurar");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(t_reclamo, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(t_proveedor, javax.swing.GroupLayout.DEFAULT_SIZE, 229, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(t_folio, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton4)
                .addContainerGap())
            .addComponent(jScrollPane1)
            .addComponent(jScrollPane2)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButton3)
                        .addComponent(jButton4))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(4, 4, 4)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(t_reclamo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(t_proveedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3)
                            .addComponent(t_folio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        aux = 2;
        cuenta = new DefaultTableModel(null, getColumnas_Cuentas());
        tabla_cuentas.setModel(cuenta);
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        cuentas.setSize(600, 500);
        cuentas.setTitle("Consultar Cuentas");
        cuentas.setLocation((d.width / 2) - 269, (d.height / 2) - 250);
        llena_cuentas();
        tabla_tamaños2();
        texto_cuenta.setText("");
        cuentas.setVisible(true);

    }//GEN-LAST:event_jButton2ActionPerformed
    public void asigna() {
        if (t_datos.getSelectedRow() >= 0) {

            if (t_datos.getValueAt(t_datos.getSelectedRow(), 5) != null) {

                model.insertRow(t_datos.getSelectedRow() + 1, new Object[]{null, tabla_cuentas.getValueAt(tabla_cuentas.getSelectedRow(), 0), "0", tabla_cuentas.getValueAt(tabla_cuentas.getSelectedRow(),2), 1.0d, 0.0d, null});
            } else {
                model.insertRow(t_datos.getSelectedRow() + 1, new Object[]{null, tabla_cuentas.getValueAt(tabla_cuentas.getSelectedRow(), 0), "0", tabla_cuentas.getValueAt(tabla_cuentas.getSelectedRow(),2), 1.0d, null, 0.0});
            }
        } else {

            if (t_datos.getRowCount() > 0) {

                if (t_datos.getValueAt(t_datos.getRowCount() - 1, 5) != null) {
                    model.addRow(new Object[]{null, tabla_cuentas.getValueAt(tabla_cuentas.getSelectedRow(), 0), "0", tabla_cuentas.getValueAt(tabla_cuentas.getSelectedRow(),2), 1.0d, 0.0d, null});
                } else {
                    model.addRow(new Object[]{null, tabla_cuentas.getValueAt(tabla_cuentas.getSelectedRow(), 0), "0", tabla_cuentas.getValueAt(tabla_cuentas.getSelectedRow(),2), 1.0d, null, 0.0d});
                }
            } else {
                model.addRow(new Object[]{null, tabla_cuentas.getValueAt(tabla_cuentas.getSelectedRow(), 0), "0", tabla_cuentas.getValueAt(tabla_cuentas.getSelectedRow(),2), 1.0d, 0.0d, null});
            }
        }

    }

    public void llena_cuentas() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            Query query = session.createSQLQuery("select id_cuentas, saldo, nombre from cuentas;");
            query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
            ArrayList datos = (ArrayList) query.list();
            cuenta = new DefaultTableModel(null, getColumnas_Cuentas());
            tabla_cuentas.setModel(cuenta);
            if (datos.size() > 0) {
                cuenta.getDataVector().removeAllElements();
                Object[] objeto = new Object[3];

                for (int i = 0; i < datos.size(); i++) {
                    java.util.HashMap map = (java.util.HashMap) datos.get(i);
                    objeto[0] = map.get("id_cuentas");
                    objeto[1] = map.get("saldo");
                    objeto[2] = map.get("nombre");
                    cuenta.addRow(objeto);
                }
            } else {
                cuenta = new DefaultTableModel(null, getColumnas_Cuentas());
                tabla_cuentas.setModel(cuenta);

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session.isOpen() == true) {
                session.close();
            }
        }
    }
    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        elimina = new ArrayList();
        t_datos.setModel(ModeloTablaReporte(0, columnas));
        formato = new FormatoTabla();
        consultaAsiento();
        t_datos1.setValueAt("Total Factura:", 0, 0);
        t_datos1.setValueAt(t_datos1.getValueAt(0, 5), 0, 1);
        formatoTabla();
        formatoTabla1();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        if ((double) t_datos1.getValueAt(0, 5) == (double) t_datos1.getValueAt(0, 6)) {
            if ((double) t_datos1.getValueAt(0, 1) == (double) t_datos1.getValueAt(0, 5)) {
                Session session = HibernateUtil.getSessionFactory().openSession();
                try {
                    session.beginTransaction().begin();
                    //eliminas for recorre lista de eliminados
                    for (int a = 0; a < elimina.size(); a++) {
                        Registro elimina1 = (Registro) session.get(Registro.class, Integer.parseInt(elimina.get(a).toString()));
                        session.delete(elimina1);
                    }
                    for (int w = 0; w < t_datos.getRowCount(); w++) {
                        if (t_datos.getValueAt(w, 0) == null)//incertas
                        {
                            asiento = (Asiento) session.get(Asiento.class, asiento.getIdAsiento());
                            Cuentas cuenta = (Cuentas) session.get(Cuentas.class, t_datos.getValueAt(w, 1).toString());
                            Registro r = new Registro();
                            r.setAsiento(asiento);
                            r.setCuentas(cuenta);
                            r.setDepto(t_datos.getValueAt(w, 2).toString());
                            r.setConcepto(t_datos.getValueAt(w, 3).toString());
                            r.setCambio((double) t_datos.getValueAt(w, 4));
                            if (t_datos.getValueAt(w, 5) != null) {
                                r.setCantidad((double) t_datos.getValueAt(w, 5));
                                r.setTipo("d");//d, a
                            } else {
                                r.setCantidad((double) t_datos.getValueAt(w, 6));
                                r.setTipo("a");//d, a
                            }
                            session.save(r);
                        } else//actualizas
                        {
                            Cuentas cuenta = (Cuentas) session.get(Cuentas.class, t_datos.getValueAt(w, 1).toString());
                            Registro r = (Registro) session.get(Registro.class, Integer.parseInt(t_datos.getValueAt(w, 0).toString()));
                            r.setAsiento(asiento);
                            r.setCuentas(cuenta);
                            r.setDepto(t_datos.getValueAt(w, 2).toString());
                            r.setConcepto(t_datos.getValueAt(w, 3).toString());
                            r.setCambio((double) t_datos.getValueAt(w, 4));
                            if (t_datos.getValueAt(w, 5) != null) {
                                r.setCantidad((double) t_datos.getValueAt(w, 5));
                                r.setTipo("d");//d, a
                            } else {
                                r.setCantidad((double) t_datos.getValueAt(w, 6));
                                r.setTipo("a");//d, a
                            }
                            session.update(r);
                        }
                    }
                    session.beginTransaction().commit();
                } catch (Exception e) {
                    session.beginTransaction().rollback();
                    e.printStackTrace();
                }
                finally{
                if (session != null) {
                    if (session.isOpen()) {
                        session.close();
                    }
                }
                JOptionPane.showMessageDialog(null, "Datos Actualizados Correctamente.");
                }
            } else {
                JOptionPane.showMessageDialog(null, "El total Debe y Haber debe ser Igual a Total Factura.");
            }
        } else {
            JOptionPane.showMessageDialog(null, "El total Debe y Haber debe ser Igual.");
        }

    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:

        if (t_datos.getSelectedRow() >= 0) {
            if(t_datos.getValueAt(t_datos.getSelectedRow(), 0)!=null)
                elimina.add(t_datos.getValueAt(t_datos.getSelectedRow(), 0));
            DefaultTableModel model = (DefaultTableModel) t_datos.getModel();
            model.removeRow(t_datos.getSelectedRow());
            t_datos.setModel(model);
            totales();
        } else {
            JOptionPane.showMessageDialog(null, "Seleccione un Registro a Eliminar.");
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:
        String consulta;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            consulta = "select id_cuentas, saldo, nombre from cuentas where";
            if (tipo_cuenta.getSelectedItem().toString().compareTo("N° Cuenta") == 0) {
                consulta += " id_cuentas='" + texto_cuenta.getText().toString() + "';";
            } else {
                consulta += " nombre like '%" + texto_cuenta.getText().toString() + "%';";
            }

            Query query = session.createSQLQuery(consulta);
            query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
            ArrayList datos = (ArrayList) query.list();
            cuenta = new DefaultTableModel(null, getColumnas_Cuentas());
            tabla_cuentas.setModel(cuenta);
            tabla_tamaños2();
            if (datos.size() > 0) {
                cuenta.getDataVector().removeAllElements();
                Object[] objeto = new Object[3];

                for (int i = 0; i < datos.size(); i++) {
                    java.util.HashMap map = (java.util.HashMap) datos.get(i);
                    objeto[0] = map.get("id_cuentas");
                    objeto[1] = map.get("saldo");
                    objeto[2] = map.get("nombre");
                    cuenta.addRow(objeto);
                }
            } else {
                cuenta = new DefaultTableModel(null, getColumnas_Cuentas());
                tabla_cuentas.setModel(cuenta);
                tabla_tamaños2();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session.isOpen() == true) {
                session.close();
            }
        }
    }//GEN-LAST:event_jButton5ActionPerformed

    private void texto_cuentaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_texto_cuentaKeyTyped
        // TODO add your handling code here:
        char car = evt.getKeyChar();
        evt.setKeyChar(Character.toUpperCase(evt.getKeyChar()));
    }//GEN-LAST:event_texto_cuentaKeyTyped

    private void texto_cuentaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_texto_cuentaActionPerformed
        // TODO add your handling code here:
        this.jButton5ActionPerformed(null);
    }//GEN-LAST:event_texto_cuentaActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        // TODO add your handling code here:

        if (tabla_cuentas.getRowCount() > 0) {
            System.out.println(tabla_cuentas.getSelectedRow());
            if (tabla_cuentas.getSelectedRow() >= 0) {
                nombre = tabla_cuentas.getValueAt(tabla_cuentas.getSelectedRow(), 0).toString();
                concepto=tabla_cuentas.getValueAt(tabla_cuentas.getSelectedRow(),2).toString();
                cuentas.dispose();
                if (aux == 2) {
                    asigna();
                }
            }
        }

    }//GEN-LAST:event_jButton6ActionPerformed
    public String concepto;
    private void t_datosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_t_datosMouseClicked
        // TODO add your handling code here:
        if (evt.getClickCount() == 2) {
            aux = 1;
            cuenta = new DefaultTableModel(null, getColumnas_Cuentas());
            tabla_cuentas.setModel(cuenta);
            Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
            cuentas.setSize(600, 500);
            cuentas.setTitle("Consultar Cuentas");
            cuentas.setLocation((d.width / 2) - 269, (d.height / 2) - 250);
            llena_cuentas();
            tabla_tamaños2();
            texto_cuenta.setText("");
            cuentas.setVisible(true);
            if (nombre != null) {
                t_datos.setValueAt(nombre, t_datos.getSelectedRow(), 1);
                t_datos.setValueAt(concepto, t_datos.getSelectedRow(), 3);
            }
            nombre = "";
            concepto="";
        }
    }//GEN-LAST:event_t_datosMouseClicked

    private void tipo_cuentaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tipo_cuentaActionPerformed
        // TODO add your handling code here:
        texto_cuenta.setText("");
        if (tipo_cuenta.getSelectedItem().equals("N° Cuenta")) {
            llena_cuentas();
            tabla_tamaños2();
        } else {
            llena_cuentas();
            tabla_tamaños2();
        }
    }//GEN-LAST:event_tipo_cuentaActionPerformed

    private void tabla_cuentasMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabla_cuentasMouseClicked
        // TODO add your handling code here:
        if(evt.getClickCount()==2){
            this.jButton6ActionPerformed(null);
        }
    }//GEN-LAST:event_tabla_cuentasMouseClicked

    public void totales() {
        double suma = 0.0d, suma1 = 0.0d;
        for (int i = 0; i < t_datos.getRowCount(); i++) {
            if (t_datos.getValueAt(i, 5) != null) {

                suma += Double.parseDouble(t_datos.getValueAt(i, 5).toString());

            }
            if (t_datos.getValueAt(i, 6) != null) {
                suma1 += Double.parseDouble(t_datos.getValueAt(i, 6).toString());
            }
        }
        t_datos1.setValueAt(suma, 0, 5);
        t_datos1.setValueAt(suma1, 0, 6);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JDialog cuentas;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable t_datos;
    private javax.swing.JTable t_datos1;
    private javax.swing.JTextField t_folio;
    private javax.swing.JTextField t_proveedor;
    public javax.swing.JTextField t_reclamo;
    private javax.swing.JTable tabla_cuentas;
    private javax.swing.JTextField texto_cuenta;
    private javax.swing.JComboBox tipo_cuenta;
    // End of variables declaration//GEN-END:variables
}
