/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */                                          
package Servicios;
import Hibernate.entidades.Orden;
import Hibernate.entidades.Usuario;
import org.hibernate.Session;
import Integral.Herramientas;
import Hibernate.Util.HibernateUtil;
import java.awt.Color;
import javax.swing.InputMap;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import org.hibernate.Query;
import Integral.Imagen;
import Integral.Render1;
import java.util.ArrayList;
import java.util.Vector;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import org.hibernate.Criteria;
/**
 *
 * @author ESPECIALIZADO TOLUCA
 */
public class Inventarios extends javax.swing.JPanel {
    /**
     * Creates new form Inventarios
     */    
    InputMap map = new InputMap();
    Usuario usr;
    private String orden="";
    Orden ord;
    Herramientas h;
    String estado ="";
    String sessionPrograma="";
    String[] columnas = new String [] {"Incluye","Nombre","Observaciones"};
    String id_inventarios="";
    
    public Inventarios(String id, Usuario usuario,String edo, String ses) {
        usr=usuario;
        sessionPrograma=ses;
        orden=id;
        initComponents();  
        if(edo.compareTo("")==0)
        {
            if(usr.getEditaInventario()==true)
                visualiza(true);
            else
                visualiza(false);
            this.jComboBox1.setEnabled(true);
        }
        else
        {
            visualiza(false);
            this.jComboBox1.setEnabled(false);
        }
        jComboBox1.setSelectedIndex(-1);
        t_datos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        t_datos.setModel(new DefaultTableModel(new Object [0][3], columnas) {
            Class[] types = new Class [] {
                java.lang.Boolean.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                true, false, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
            
            public void setValueAt(Object value, int row, int col)
            {
                Vector vector = (Vector)this.dataVector.elementAt(row);
                switch(col)
                {
                    case 0:
                        if(vector.get(col)==null)
                        {
                            vector.setElementAt(value, col);
                            dataVector.setElementAt(vector, row);
                            fireTableCellUpdated(row, col);
                        }
                        else{
                            Session session = HibernateUtil.getSessionFactory().openSession();
                            try{
                                session.beginTransaction();
                                String valor="0&"+t_datos.getValueAt(row, 2).toString();
                                if((boolean)value==true)
                                    valor="1&"+t_datos.getValueAt(row, 2).toString();
                                Query query = session.createSQLQuery("UPDATE inventarios set `"+t_datos.getValueAt(row, 1).toString()+"` = '"+valor+"' where id_inventarios="+id_inventarios);
                                int respuesta = query.executeUpdate();
                                session.getTransaction().commit();
                                if(respuesta>0)
                                {
                                    vector.setElementAt(value, col);
                                    dataVector.setElementAt(vector, row);
                                    fireTableCellUpdated(row, col);
                                    if(session.isOpen()==true)
                                        session.close();
                                }
                                else
                                    JOptionPane.showMessageDialog(null, "Error al actualizar el campo");
                            }catch(Exception e){
                                session.getTransaction().rollback();
                                e.printStackTrace();
                                JOptionPane.showMessageDialog(null ,"error al Conectar con la Base de Datos");
                            }
                        }
                        break;
                    case 2:
                        if(vector.get(col)==null)
                        {
                            vector.setElementAt(value, col);
                            dataVector.setElementAt(vector, row);
                            fireTableCellUpdated(row, col);
                        }
                        else
                        {
                            Session session = HibernateUtil.getSessionFactory().openSession();
                            try{
                                session.beginTransaction();
                                String valor="0&"+(String)value;
                                if((boolean)t_datos.getValueAt(row, 0)==true)
                                    valor="1&"+(String)value;
                                Query query = session.createSQLQuery("UPDATE inventarios set `"+t_datos.getValueAt(row, 1).toString()+"`= '"+valor+"' where id_inventarios="+id_inventarios);
                                int respuesta = query.executeUpdate();
                                session.getTransaction().commit();
                                if(respuesta>0)
                                {
                                    vector.setElementAt(value, col);
                                    dataVector.setElementAt(vector, row);
                                    fireTableCellUpdated(row, col);
                                    if(session.isOpen()==true)
                                        session.close();
                                }
                            }
                            catch(Exception e){
                                session.getTransaction().rollback();
                                e.printStackTrace();
                            }
                        }
                        break;

                    default:
                        vector.setElementAt(value, col);
                        dataVector.setElementAt(vector, row);
                        fireTableCellUpdated(row, col);
                        break;
                }
            }
        });
        
        formatoTabla();
        
        h=new Herramientas(usr, 0);
        if(h.isCerrada(orden)==true)
        {
            visualiza(false);
            this.jComboBox1.setEnabled(false);
            t_datos.setEnabled(false);
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

        observacion = new javax.swing.JTextField();
        Tracto = new javax.swing.JPanel();
        cb_llanta1_tracto = new javax.swing.JCheckBox();
        cb_llanta2_tracto = new javax.swing.JCheckBox();
        cb_llanta3_tracto = new javax.swing.JCheckBox();
        cb_llanta4_tracto = new javax.swing.JCheckBox();
        cb_llanta5_tracto = new javax.swing.JCheckBox();
        cb_llanta6_tracto = new javax.swing.JCheckBox();
        cb_llanta7_tracto = new javax.swing.JCheckBox();
        cb_llanta8_tracto = new javax.swing.JCheckBox();
        cb_llanta9_tracto = new javax.swing.JCheckBox();
        cb_llanta10_tracto = new javax.swing.JCheckBox();
        jLabel5 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        ta_comentarios_tracto = new javax.swing.JTextArea();
        t_gas1_tracto = new javax.swing.JTextField();
        t_gas2_tracto = new javax.swing.JTextField();
        t_gas3_tracto = new javax.swing.JTextField();
        t_gas4_tracto = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        Caja = new javax.swing.JPanel();
        cb_llanta1_caja = new javax.swing.JCheckBox();
        cb_llanta2_caja = new javax.swing.JCheckBox();
        cb_llanta3_caja = new javax.swing.JCheckBox();
        cb_llanta4_caja = new javax.swing.JCheckBox();
        cb_llanta5_caja = new javax.swing.JCheckBox();
        cb_llanta6_caja = new javax.swing.JCheckBox();
        cb_llanta7_caja = new javax.swing.JCheckBox();
        cb_llanta8_caja = new javax.swing.JCheckBox();
        jLabel6 = new javax.swing.JLabel();
        t_gas_caja = new javax.swing.JTextField();
        jScrollPane3 = new javax.swing.JScrollPane();
        ta_comentarios_caja = new javax.swing.JTextArea();
        jLabel7 = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        inventariodeunidad = new javax.swing.JLabel();
        unidad = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox();
        listado = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        t_datos = new javax.swing.JTable();

        observacion.setText("jTextField1");
        observacion.setBorder(null);
        observacion.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                observacionKeyTyped(evt);
            }
        });

        Tracto.setBackground(new java.awt.Color(254, 254, 254));
        Tracto.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        cb_llanta1_tracto.setToolTipText("Llanta uno");
        cb_llanta1_tracto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cb_llanta1_tractoActionPerformed(evt);
            }
        });
        Tracto.add(cb_llanta1_tracto, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 10, -1, -1));

        cb_llanta2_tracto.setToolTipText("Llanta dos");
        cb_llanta2_tracto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cb_llanta2_tractoActionPerformed(evt);
            }
        });
        Tracto.add(cb_llanta2_tracto, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 30, -1, -1));

        cb_llanta3_tracto.setToolTipText("Llanta tres");
        cb_llanta3_tracto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cb_llanta3_tractoActionPerformed(evt);
            }
        });
        Tracto.add(cb_llanta3_tracto, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 120, -1, -1));

        cb_llanta4_tracto.setToolTipText("Llanta cuatro");
        cb_llanta4_tracto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cb_llanta4_tractoActionPerformed(evt);
            }
        });
        Tracto.add(cb_llanta4_tracto, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 140, -1, -1));

        cb_llanta5_tracto.setToolTipText("Llanta cinco");
        cb_llanta5_tracto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cb_llanta5_tractoActionPerformed(evt);
            }
        });
        Tracto.add(cb_llanta5_tracto, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 10, -1, -1));

        cb_llanta6_tracto.setToolTipText("Llanta seis");
        cb_llanta6_tracto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cb_llanta6_tractoActionPerformed(evt);
            }
        });
        Tracto.add(cb_llanta6_tracto, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 30, -1, -1));

        cb_llanta7_tracto.setToolTipText("Llanta siete");
        cb_llanta7_tracto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cb_llanta7_tractoActionPerformed(evt);
            }
        });
        Tracto.add(cb_llanta7_tracto, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 120, -1, -1));

        cb_llanta8_tracto.setToolTipText("Llanta ocho");
        cb_llanta8_tracto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cb_llanta8_tractoActionPerformed(evt);
            }
        });
        Tracto.add(cb_llanta8_tracto, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 140, -1, -1));

        cb_llanta9_tracto.setToolTipText("Llanta nueve");
        cb_llanta9_tracto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cb_llanta9_tractoActionPerformed(evt);
            }
        });
        Tracto.add(cb_llanta9_tracto, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 10, -1, -1));

        cb_llanta10_tracto.setToolTipText("Llanta diez");
        cb_llanta10_tracto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cb_llanta10_tractoActionPerformed(evt);
            }
        });
        Tracto.add(cb_llanta10_tracto, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 140, -1, -1));
        Tracto.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 420, 190));

        ta_comentarios_tracto.setColumns(20);
        ta_comentarios_tracto.setRows(5);
        ta_comentarios_tracto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                ta_comentarios_tractoKeyTyped(evt);
            }
        });
        jScrollPane2.setViewportView(ta_comentarios_tracto);

        Tracto.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 250, 310, 30));

        t_gas1_tracto.setNextFocusableComponent(ta_comentarios_tracto);
        t_gas1_tracto.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                t_gas1_tractoFocusLost(evt);
            }
        });
        t_gas1_tracto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                t_gas1_tractoActionPerformed(evt);
            }
        });
        t_gas1_tracto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                t_gas1_tractoKeyTyped(evt);
            }
        });
        Tracto.add(t_gas1_tracto, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 190, 60, -1));

        t_gas2_tracto.setNextFocusableComponent(ta_comentarios_tracto);
        t_gas2_tracto.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                t_gas2_tractoFocusLost(evt);
            }
        });
        t_gas2_tracto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                t_gas2_tractoActionPerformed(evt);
            }
        });
        t_gas2_tracto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                t_gas2_tractoKeyTyped(evt);
            }
        });
        Tracto.add(t_gas2_tracto, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 190, 60, -1));

        t_gas3_tracto.setNextFocusableComponent(ta_comentarios_tracto);
        t_gas3_tracto.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                t_gas3_tractoFocusLost(evt);
            }
        });
        t_gas3_tracto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                t_gas3_tractoActionPerformed(evt);
            }
        });
        t_gas3_tracto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                t_gas3_tractoKeyTyped(evt);
            }
        });
        Tracto.add(t_gas3_tracto, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 190, 60, -1));

        t_gas4_tracto.setNextFocusableComponent(ta_comentarios_tracto);
        t_gas4_tracto.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                t_gas4_tractoFocusLost(evt);
            }
        });
        t_gas4_tracto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                t_gas4_tractoActionPerformed(evt);
            }
        });
        t_gas4_tracto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                t_gas4_tractoKeyTyped(evt);
            }
        });
        Tracto.add(t_gas4_tracto, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 190, 60, -1));
        Tracto.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 140, 100, 110));
        Tracto.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 140, 110, 110));
        Tracto.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 140, 100, 110));
        Tracto.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 140, 100, 110));

        jButton2.setText("Guardar");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        Tracto.add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 250, -1, -1));

        Caja.setBackground(new java.awt.Color(254, 254, 254));
        Caja.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        cb_llanta1_caja.setToolTipText("Llanta uno");
        cb_llanta1_caja.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cb_llanta1_cajaActionPerformed(evt);
            }
        });
        Caja.add(cb_llanta1_caja, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 10, -1, -1));

        cb_llanta2_caja.setToolTipText("Llanta dos");
        cb_llanta2_caja.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cb_llanta2_cajaActionPerformed(evt);
            }
        });
        Caja.add(cb_llanta2_caja, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 30, -1, -1));

        cb_llanta3_caja.setToolTipText("Llanta tres");
        cb_llanta3_caja.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cb_llanta3_cajaActionPerformed(evt);
            }
        });
        Caja.add(cb_llanta3_caja, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 120, -1, -1));

        cb_llanta4_caja.setToolTipText("Llanta cuatro");
        cb_llanta4_caja.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cb_llanta4_cajaActionPerformed(evt);
            }
        });
        Caja.add(cb_llanta4_caja, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 140, -1, -1));

        cb_llanta5_caja.setToolTipText("Llanta cinco");
        cb_llanta5_caja.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cb_llanta5_cajaActionPerformed(evt);
            }
        });
        Caja.add(cb_llanta5_caja, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 10, -1, -1));

        cb_llanta6_caja.setToolTipText("Llanta seis");
        cb_llanta6_caja.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cb_llanta6_cajaActionPerformed(evt);
            }
        });
        Caja.add(cb_llanta6_caja, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 30, -1, -1));

        cb_llanta7_caja.setToolTipText("Llanta siete");
        cb_llanta7_caja.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cb_llanta7_cajaActionPerformed(evt);
            }
        });
        Caja.add(cb_llanta7_caja, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 120, -1, 20));

        cb_llanta8_caja.setToolTipText("Llanta ocho");
        cb_llanta8_caja.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cb_llanta8_cajaActionPerformed(evt);
            }
        });
        Caja.add(cb_llanta8_caja, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 140, -1, 20));
        Caja.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 480, 200));

        t_gas_caja.setToolTipText("");
        t_gas_caja.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                t_gas_cajaActionPerformed(evt);
            }
        });
        t_gas_caja.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                t_gas_cajaFocusLost(evt);
            }
        });
        t_gas_caja.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                t_gas_cajaKeyTyped(evt);
            }
        });
        Caja.add(t_gas_caja, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 190, 60, -1));

        ta_comentarios_caja.setColumns(20);
        ta_comentarios_caja.setRows(5);
        ta_comentarios_caja.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                ta_comentarios_cajaKeyTyped(evt);
            }
        });
        jScrollPane3.setViewportView(ta_comentarios_caja);

        Caja.add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 170, 200, 80));
        Caja.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 140, 120, 110));

        jButton3.setText("Guardar");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        Caja.add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 210, -1, 40));

        jButton1.setText("jButton1");

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane1.setViewportView(jTextArea1);

        setBackground(new java.awt.Color(255, 255, 255));

        inventariodeunidad.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        inventariodeunidad.setForeground(new java.awt.Color(0, 51, 255));
        inventariodeunidad.setText("Inventario de Unidad");

        unidad.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        unidad.setText("Unidad:");

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Tracto", "Caja" }));
        jComboBox1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox1ItemStateChanged(evt);
            }
        });
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });

        listado.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        listado.setText("Listado de inventario");

        jPanel2.setBackground(new java.awt.Color(254, 254, 254));
        jPanel2.setLayout(new java.awt.CardLayout());

        t_datos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Incluye", "Nombre", "Observaciones"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Boolean.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                true, false, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane4.setViewportView(t_datos);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(inventariodeunidad)
                        .addGap(161, 161, 161)
                        .addComponent(unidad)
                        .addGap(18, 18, 18)
                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 401, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addComponent(listado)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 452, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(unidad)
                            .addComponent(inventariodeunidad, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(listado)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 278, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 267, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void observacionKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_observacionKeyTyped
        // TODO add your handling code here:
        char car = evt.getKeyChar();
        evt.setKeyChar(Character.toUpperCase(evt.getKeyChar()));
        if(observacion.getText().length()>=50)
            evt.consume();
    }//GEN-LAST:event_observacionKeyTyped

    private void t_gas1_tractoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_gas1_tractoKeyTyped
        char car = evt.getKeyChar();
        if(t_gas1_tracto.getText().length()>=6) 
            evt.consume();
        if((car<'0' && car!='.') || (car>'9' && car!='.')) 
            evt.consume();
    }//GEN-LAST:event_t_gas1_tractoKeyTyped

    private void t_gas2_tractoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_gas2_tractoKeyTyped
        char car = evt.getKeyChar();
        if(t_gas2_tracto.getText().length()>=6) 
            evt.consume();
        if((car<'0' && car!='.') || (car>'9' && car!='.')) 
            evt.consume();
    }//GEN-LAST:event_t_gas2_tractoKeyTyped

    private void t_gas3_tractoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_gas3_tractoKeyTyped
        char car = evt.getKeyChar();
        if(t_gas3_tracto.getText().length()>=6) 
            evt.consume();
        if((car<'0' && car!='.') || (car>'9' && car!='.')) 
            evt.consume();
    }//GEN-LAST:event_t_gas3_tractoKeyTyped

    private void t_gas4_tractoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_gas4_tractoKeyTyped
        char car = evt.getKeyChar();
        if(t_gas4_tracto.getText().length()>=6) 
            evt.consume();
        if((car<'0' && car!='.') || (car>'9' && car!='.')) 
            evt.consume();
    }//GEN-LAST:event_t_gas4_tractoKeyTyped

    private void t_gas_cajaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_gas_cajaKeyTyped
        char car = evt.getKeyChar();
        if(t_gas_caja.getText().length()>=6) 
            evt.consume();
        if((car<'0' && car!='.') || (car>'9' && car!='.')) 
            evt.consume();
    }//GEN-LAST:event_t_gas_cajaKeyTyped

    private void jComboBox1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox1ItemStateChanged
        switch (jComboBox1.getSelectedIndex()) 
        {
            case 0:
                listado.setText("Inventario de Tracto");
                jPanel2.removeAll();
                jPanel2.add(Tracto);
                jLabel5.add(new Imagen("imagenes/llantas.jpg", 411, 170, 1, 0, 400, 220));
                jLabel1.add(new Imagen("imagenes/GAS.jpg", 100, 80, 1, 25, 400, 220));
                jLabel2.add(new Imagen("imagenes/GAS.jpg", 100, 80, 1, 25, 400, 220));
                jLabel3.add(new Imagen("imagenes/GAS.jpg", 100, 80, 1, 25, 400, 220));
                jLabel4.add(new Imagen("imagenes/GAS.jpg", 100, 80, 1, 25, 400, 220));
                jPanel2.repaint();
                buscaDato("T");
                break;
                    
            case 1:
                listado.setText("Inventario de Caja");
                jPanel2.removeAll();
                jPanel2.add(Caja);
                jLabel6.add(new Imagen("imagenes/llantas-caja.jpg", 399, 170, 1, 0, 400, 220));
                jLabel7.add(new Imagen("imagenes/GAS.jpg", 100, 80, 1, 25, 400, 220));
                jPanel2.repaint();
                buscaDato("C");
                break;
        }
    }//GEN-LAST:event_jComboBox1ItemStateChanged

    private void cb_llanta3_tractoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cb_llanta3_tractoActionPerformed
        Session session = HibernateUtil.getSessionFactory().openSession();
        try{
            session.beginTransaction();
            Query query = session.createSQLQuery("UPDATE inventarios set `llanta3`= "+cb_llanta3_tracto.isSelected()+" where id_inventarios="+id_inventarios);
            int respuesta = query.executeUpdate();
            session.getTransaction().commit();
        }
        catch(Exception e){
            session.getTransaction().rollback();
            e.printStackTrace();
        }
    }//GEN-LAST:event_cb_llanta3_tractoActionPerformed

    private void cb_llanta1_tractoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cb_llanta1_tractoActionPerformed
        Session session = HibernateUtil.getSessionFactory().openSession();
        try{
            session.beginTransaction();
            Query query = session.createSQLQuery("UPDATE inventarios set `llanta1`= "+cb_llanta1_tracto.isSelected()+" where id_inventarios="+id_inventarios);
            int respuesta = query.executeUpdate();
            session.getTransaction().commit();
        }
        catch(Exception e){
            session.getTransaction().rollback();
            e.printStackTrace();
        }
    }//GEN-LAST:event_cb_llanta1_tractoActionPerformed

    private void cb_llanta4_tractoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cb_llanta4_tractoActionPerformed
        Session session = HibernateUtil.getSessionFactory().openSession();
        try{
            session.beginTransaction();
            Query query = session.createSQLQuery("UPDATE inventarios set `llanta4`= "+cb_llanta4_tracto.isSelected()+" where id_inventarios="+id_inventarios);
            int respuesta = query.executeUpdate();
            session.getTransaction().commit();
        }
        catch(Exception e){
            session.getTransaction().rollback();
            e.printStackTrace();
        }
    }//GEN-LAST:event_cb_llanta4_tractoActionPerformed

    private void cb_llanta5_tractoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cb_llanta5_tractoActionPerformed
        Session session = HibernateUtil.getSessionFactory().openSession();
        try{
            session.beginTransaction();
            Query query = session.createSQLQuery("UPDATE inventarios set `llanta5`= "+cb_llanta5_tracto.isSelected()+" where id_inventarios="+id_inventarios);
            int respuesta = query.executeUpdate();
            session.getTransaction().commit();
        }
        catch(Exception e){
            session.getTransaction().rollback();
            e.printStackTrace();
        }
    }//GEN-LAST:event_cb_llanta5_tractoActionPerformed

    private void cb_llanta6_tractoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cb_llanta6_tractoActionPerformed
        Session session = HibernateUtil.getSessionFactory().openSession();
        try{
            session.beginTransaction();
            Query query = session.createSQLQuery("UPDATE inventarios set `llanta6`= "+cb_llanta6_tracto.isSelected()+" where id_inventarios="+id_inventarios);
            int respuesta = query.executeUpdate();
            session.getTransaction().commit();
        }
        catch(Exception e){
            session.getTransaction().rollback();
            e.printStackTrace();
        }
    }//GEN-LAST:event_cb_llanta6_tractoActionPerformed

    private void cb_llanta7_tractoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cb_llanta7_tractoActionPerformed
        Session session = HibernateUtil.getSessionFactory().openSession();
        try{
            session.beginTransaction();
            Query query = session.createSQLQuery("UPDATE inventarios set `llanta7`= "+cb_llanta7_tracto.isSelected()+" where id_inventarios="+id_inventarios);
            int respuesta = query.executeUpdate();
            session.getTransaction().commit();
        }
        catch(Exception e){
            session.getTransaction().rollback();
            e.printStackTrace();
        }
    }//GEN-LAST:event_cb_llanta7_tractoActionPerformed

    private void cb_llanta8_tractoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cb_llanta8_tractoActionPerformed
        Session session = HibernateUtil.getSessionFactory().openSession();
        try{
            session.beginTransaction();
            Query query = session.createSQLQuery("UPDATE inventarios set `llanta8`= "+cb_llanta8_tracto.isSelected()+" where id_inventarios="+id_inventarios);
            int respuesta = query.executeUpdate();
            session.getTransaction().commit();
        }
        catch(Exception e){
            session.getTransaction().rollback();
            e.printStackTrace();
        }
    }//GEN-LAST:event_cb_llanta8_tractoActionPerformed

    private void cb_llanta9_tractoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cb_llanta9_tractoActionPerformed
        Session session = HibernateUtil.getSessionFactory().openSession();
        try{
            session.beginTransaction();
            Query query = session.createSQLQuery("UPDATE inventarios set `llanta9`= "+cb_llanta9_tracto.isSelected()+" where id_inventarios="+id_inventarios);
            int respuesta = query.executeUpdate();
            session.getTransaction().commit();
        }
        catch(Exception e){
            session.getTransaction().rollback();
            e.printStackTrace();
        }
    }//GEN-LAST:event_cb_llanta9_tractoActionPerformed

    private void cb_llanta10_tractoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cb_llanta10_tractoActionPerformed
        Session session = HibernateUtil.getSessionFactory().openSession();
        try{
            session.beginTransaction();
            Query query = session.createSQLQuery("UPDATE inventarios set `llanta10`= "+cb_llanta10_tracto.isSelected()+" where id_inventarios="+id_inventarios);
            int respuesta = query.executeUpdate();
            session.getTransaction().commit();
        }
        catch(Exception e){
            session.getTransaction().rollback();
            e.printStackTrace();
        }
    }//GEN-LAST:event_cb_llanta10_tractoActionPerformed

    private void cb_llanta1_cajaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cb_llanta1_cajaActionPerformed
        Session session = HibernateUtil.getSessionFactory().openSession();
        try{
            session.beginTransaction();
            Query query = session.createSQLQuery("UPDATE inventarios set `llanta1`= "+cb_llanta1_caja.isSelected()+" where id_inventarios="+id_inventarios);
            int respuesta = query.executeUpdate();
            session.getTransaction().commit();
        }
        catch(Exception e){
            session.getTransaction().rollback();
            e.printStackTrace();
        }
    }//GEN-LAST:event_cb_llanta1_cajaActionPerformed

    private void cb_llanta2_cajaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cb_llanta2_cajaActionPerformed
        Session session = HibernateUtil.getSessionFactory().openSession();
        try{
            session.beginTransaction();
            Query query = session.createSQLQuery("UPDATE inventarios set `llanta2`= "+cb_llanta2_caja.isSelected()+" where id_inventarios="+id_inventarios);
            int respuesta = query.executeUpdate();
            session.getTransaction().commit();
        }
        catch(Exception e){
            session.getTransaction().rollback();
            e.printStackTrace();
        }
    }//GEN-LAST:event_cb_llanta2_cajaActionPerformed

    private void cb_llanta3_cajaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cb_llanta3_cajaActionPerformed
        Session session = HibernateUtil.getSessionFactory().openSession();
        try{
            session.beginTransaction();
            Query query = session.createSQLQuery("UPDATE inventarios set `llanta3`= "+cb_llanta3_caja.isSelected()+" where id_inventarios="+id_inventarios);
            int respuesta = query.executeUpdate();
            session.getTransaction().commit();
        }
        catch(Exception e){
            session.getTransaction().rollback();
            e.printStackTrace();
        }
    }//GEN-LAST:event_cb_llanta3_cajaActionPerformed

    private void cb_llanta4_cajaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cb_llanta4_cajaActionPerformed
        Session session = HibernateUtil.getSessionFactory().openSession();
        try{
            session.beginTransaction();
            Query query = session.createSQLQuery("UPDATE inventarios set `llanta4`= "+cb_llanta4_caja.isSelected()+" where id_inventarios="+id_inventarios);
            int respuesta = query.executeUpdate();
            session.getTransaction().commit();
        }
        catch(Exception e){
            session.getTransaction().rollback();
            e.printStackTrace();
        }
    }//GEN-LAST:event_cb_llanta4_cajaActionPerformed

    private void cb_llanta5_cajaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cb_llanta5_cajaActionPerformed
        Session session = HibernateUtil.getSessionFactory().openSession();
        try{
            session.beginTransaction();
            Query query = session.createSQLQuery("UPDATE inventarios set `llanta5`= "+cb_llanta5_caja.isSelected()+" where id_inventarios="+id_inventarios);
            int respuesta = query.executeUpdate();
            session.getTransaction().commit();
        }
        catch(Exception e){
            session.getTransaction().rollback();
            e.printStackTrace();
        }
    }//GEN-LAST:event_cb_llanta5_cajaActionPerformed

    private void cb_llanta6_cajaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cb_llanta6_cajaActionPerformed
        Session session = HibernateUtil.getSessionFactory().openSession();
        try{
            session.beginTransaction();
            Query query = session.createSQLQuery("UPDATE inventarios set `llanta6`= "+cb_llanta6_caja.isSelected()+" where id_inventarios="+id_inventarios);
            int respuesta = query.executeUpdate();
            session.getTransaction().commit();
        }
        catch(Exception e){
            session.getTransaction().rollback();
            e.printStackTrace();
        }
    }//GEN-LAST:event_cb_llanta6_cajaActionPerformed

    private void cb_llanta7_cajaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cb_llanta7_cajaActionPerformed
        Session session = HibernateUtil.getSessionFactory().openSession();
        try{
            session.beginTransaction();
            Query query = session.createSQLQuery("UPDATE inventarios set `llanta7`= "+cb_llanta7_caja.isSelected()+" where id_inventarios="+id_inventarios);
            int respuesta = query.executeUpdate();
            session.getTransaction().commit();
        }
        catch(Exception e){
            session.getTransaction().rollback();
            e.printStackTrace();
        }
    }//GEN-LAST:event_cb_llanta7_cajaActionPerformed

    private void cb_llanta2_tractoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cb_llanta2_tractoActionPerformed
        Session session = HibernateUtil.getSessionFactory().openSession();
        try{
            session.beginTransaction();
            Query query = session.createSQLQuery("UPDATE inventarios set `llanta2`= "+cb_llanta2_caja.isSelected()+" where id_inventarios="+id_inventarios);
            int respuesta = query.executeUpdate();
            session.getTransaction().commit();
        }
        catch(Exception e){
            session.getTransaction().rollback();
            e.printStackTrace();
        }
    }//GEN-LAST:event_cb_llanta2_tractoActionPerformed

    private void cb_llanta8_cajaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cb_llanta8_cajaActionPerformed
        Session session = HibernateUtil.getSessionFactory().openSession();
        try{
            session.beginTransaction();
            Query query = session.createSQLQuery("UPDATE inventarios set `llanta8`= "+cb_llanta8_caja.isSelected()+" where id_inventarios="+id_inventarios);
            int respuesta = query.executeUpdate();
            session.getTransaction().commit();
        }
        catch(Exception e){
            session.getTransaction().rollback();
            e.printStackTrace();
        }
    }//GEN-LAST:event_cb_llanta8_cajaActionPerformed

    private void t_gas1_tractoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_t_gas1_tractoFocusLost
        Session session = HibernateUtil.getSessionFactory().openSession();
        try{
            session.beginTransaction();
            Query query = session.createSQLQuery("UPDATE inventarios set `gas1`= '"+t_gas1_tracto.getText()+"' where id_inventarios="+id_inventarios);
            int respuesta = query.executeUpdate();
            session.getTransaction().commit();
        }
        catch(Exception e){
            session.getTransaction().rollback();
            e.printStackTrace();
        }
    }//GEN-LAST:event_t_gas1_tractoFocusLost

    private void t_gas2_tractoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_t_gas2_tractoFocusLost
        Session session = HibernateUtil.getSessionFactory().openSession();
        try{
            session.beginTransaction();
            Query query = session.createSQLQuery("UPDATE inventarios set `gas2`= '"+t_gas2_tracto.getText()+"' where id_inventarios="+id_inventarios);
            int respuesta = query.executeUpdate();
            session.getTransaction().commit();
        }
        catch(Exception e){
            session.getTransaction().rollback();
            e.printStackTrace();
        }
    }//GEN-LAST:event_t_gas2_tractoFocusLost

    private void t_gas3_tractoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_t_gas3_tractoFocusLost
        Session session = HibernateUtil.getSessionFactory().openSession();
        try{
            session.beginTransaction();
            Query query = session.createSQLQuery("UPDATE inventarios set `gas3`= '"+t_gas3_tracto.getText()+"' where id_inventarios="+id_inventarios);
            int respuesta = query.executeUpdate();
            session.getTransaction().commit();
        }
        catch(Exception e){
            session.getTransaction().rollback();
            e.printStackTrace();
        }
    }//GEN-LAST:event_t_gas3_tractoFocusLost

    private void t_gas4_tractoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_t_gas4_tractoFocusLost
        Session session = HibernateUtil.getSessionFactory().openSession();
        try{
            session.beginTransaction();
            Query query = session.createSQLQuery("UPDATE inventarios set `gas4`= '"+t_gas4_tracto.getText()+"' where id_inventarios="+id_inventarios);
            int respuesta = query.executeUpdate();
            session.getTransaction().commit();
        }
        catch(Exception e){
            session.getTransaction().rollback();
            e.printStackTrace();
        }
    }//GEN-LAST:event_t_gas4_tractoFocusLost

    private void t_gas_cajaFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_t_gas_cajaFocusLost
        Session session = HibernateUtil.getSessionFactory().openSession();
        try{
            session.beginTransaction();
            Query query = session.createSQLQuery("UPDATE inventarios set `gas1`= '"+t_gas_caja.getText()+"' where id_inventarios="+id_inventarios);
            int respuesta = query.executeUpdate();
            session.getTransaction().commit();
        }
        catch(Exception e){
            session.getTransaction().rollback();
            e.printStackTrace();
        }
    }//GEN-LAST:event_t_gas_cajaFocusLost

    private void ta_comentarios_tractoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ta_comentarios_tractoKeyTyped
        char car = evt.getKeyChar();
        evt.setKeyChar(Character.toUpperCase(evt.getKeyChar()));
        if(ta_comentarios_tracto.getText().length()>=45)
        evt.consume();
    }//GEN-LAST:event_ta_comentarios_tractoKeyTyped

    private void ta_comentarios_cajaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ta_comentarios_cajaKeyTyped
        char car = evt.getKeyChar();
        evt.setKeyChar(Character.toUpperCase(evt.getKeyChar()));
        if(ta_comentarios_caja.getText().length()>=45)
        evt.consume();
    }//GEN-LAST:event_ta_comentarios_cajaKeyTyped

    private void t_gas1_tractoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_t_gas1_tractoActionPerformed
        ta_comentarios_tracto.requestFocus();
    }//GEN-LAST:event_t_gas1_tractoActionPerformed

    private void t_gas2_tractoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_t_gas2_tractoActionPerformed
        ta_comentarios_tracto.requestFocus();
    }//GEN-LAST:event_t_gas2_tractoActionPerformed

    private void t_gas3_tractoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_t_gas3_tractoActionPerformed
        ta_comentarios_tracto.requestFocus();
    }//GEN-LAST:event_t_gas3_tractoActionPerformed

    private void t_gas4_tractoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_t_gas4_tractoActionPerformed
        ta_comentarios_tracto.requestFocus();
    }//GEN-LAST:event_t_gas4_tractoActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        Session session = HibernateUtil.getSessionFactory().openSession();
        try{
            session.beginTransaction();
            Query query = session.createSQLQuery("UPDATE inventarios set `comentarios`= '"+ta_comentarios_tracto.getText()+"' where id_inventarios="+id_inventarios);
            int respuesta = query.executeUpdate();
            session.getTransaction().commit();
        }
        catch(Exception e){
            session.getTransaction().rollback();
            e.printStackTrace();
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        Session session = HibernateUtil.getSessionFactory().openSession();
        try{
            session.beginTransaction();
            Query query = session.createSQLQuery("UPDATE inventarios set `comentarios`= '"+ta_comentarios_caja.getText()+"' where id_inventarios="+id_inventarios);
            int respuesta = query.executeUpdate();
            session.getTransaction().commit();
        }
        catch(Exception e){
            session.getTransaction().rollback();
            e.printStackTrace();
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void t_gas_cajaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_t_gas_cajaActionPerformed
        // TODO add your handling code here:
        ta_comentarios_caja.requestFocus();
    }//GEN-LAST:event_t_gas_cajaActionPerformed

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox1ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel Caja;
    private javax.swing.JPanel Tracto;
    private javax.swing.JCheckBox cb_llanta10_tracto;
    private javax.swing.JCheckBox cb_llanta1_caja;
    private javax.swing.JCheckBox cb_llanta1_tracto;
    private javax.swing.JCheckBox cb_llanta2_caja;
    private javax.swing.JCheckBox cb_llanta2_tracto;
    private javax.swing.JCheckBox cb_llanta3_caja;
    private javax.swing.JCheckBox cb_llanta3_tracto;
    private javax.swing.JCheckBox cb_llanta4_caja;
    private javax.swing.JCheckBox cb_llanta4_tracto;
    private javax.swing.JCheckBox cb_llanta5_caja;
    private javax.swing.JCheckBox cb_llanta5_tracto;
    private javax.swing.JCheckBox cb_llanta6_caja;
    private javax.swing.JCheckBox cb_llanta6_tracto;
    private javax.swing.JCheckBox cb_llanta7_caja;
    private javax.swing.JCheckBox cb_llanta7_tracto;
    private javax.swing.JCheckBox cb_llanta8_caja;
    private javax.swing.JCheckBox cb_llanta8_tracto;
    private javax.swing.JCheckBox cb_llanta9_tracto;
    private javax.swing.JLabel inventariodeunidad;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JLabel listado;
    private javax.swing.JTextField observacion;
    private javax.swing.JTable t_datos;
    private javax.swing.JTextField t_gas1_tracto;
    private javax.swing.JTextField t_gas2_tracto;
    private javax.swing.JTextField t_gas3_tracto;
    private javax.swing.JTextField t_gas4_tracto;
    private javax.swing.JTextField t_gas_caja;
    private javax.swing.JTextArea ta_comentarios_caja;
    private javax.swing.JTextArea ta_comentarios_tracto;
    private javax.swing.JLabel unidad;
    // End of variables declaration//GEN-END:variables
    public void visualiza(Boolean valor)
    {
        this.cb_llanta1_tracto.setEnabled(valor);
        this.cb_llanta2_tracto.setEnabled(valor);
        this.cb_llanta3_tracto.setEnabled(valor);
        this.cb_llanta4_tracto.setEnabled(valor);
        this.cb_llanta5_tracto.setEnabled(valor);
        this.cb_llanta6_tracto.setEnabled(valor);
        this.cb_llanta7_tracto.setEnabled(valor);
        this.cb_llanta8_tracto.setEnabled(valor);
        this.cb_llanta9_tracto.setEnabled(valor);
        this.cb_llanta10_tracto.setEnabled(valor);
        this.ta_comentarios_tracto.setEnabled(valor);
        this.t_gas1_tracto.setEnabled(valor);
        this.t_gas2_tracto.setEnabled(valor);
        this.t_gas3_tracto.setEnabled(valor);
        this.t_gas4_tracto.setEnabled(valor);
        this.jButton2.setEnabled(valor);
        this.t_datos.setEnabled(valor);
        
        this.cb_llanta1_caja.setEnabled(valor);
        this.cb_llanta2_caja.setEnabled(valor);
        this.cb_llanta3_caja.setEnabled(valor);
        this.cb_llanta4_caja.setEnabled(valor);
        this.cb_llanta5_caja.setEnabled(valor);
        this.cb_llanta6_caja.setEnabled(valor);
        this.cb_llanta7_caja.setEnabled(valor);
        this.cb_llanta8_caja.setEnabled(valor);
        this.ta_comentarios_caja.setEnabled(valor);
        this.t_gas_caja.setEnabled(valor);
        this.jButton3.setEnabled(valor);
        this.jButton1.setEnabled(valor);
    }
    
    private void buscaDato(String op)
    {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try
        {
            DefaultTableModel model = (DefaultTableModel)t_datos.getModel();
            model.setRowCount(0);
            Query query2 = session.createSQLQuery("select * from inventarios where id_orden="+orden+" and tipo='"+op+"'");
            query2.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
            ArrayList lista = (ArrayList)query2.list();
            int valor=0;
            if(lista.isEmpty()==true)
            {
                Query query = session.createSQLQuery("INSERT INTO inventarios (id_orden, tipo) VALUES ("+orden+", '"+op+"')");
                int respuesta = query.executeUpdate();
                if(respuesta>0)
                {
                    query2 = session.createSQLQuery("select * from inventarios where id_orden="+orden+" and tipo='"+op+"'");
                    query2.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
                    lista = (ArrayList)query2.list();
                }
            }
            if(lista.isEmpty()==false)
            {
                java.util.HashMap map=(java.util.HashMap)lista.get(0);
                id_inventarios=map.get("id_inventarios").toString();
                if(op.compareTo("T")==0)
                {
                    cb_llanta1_tracto.setSelected((boolean)map.get("llanta1"));
                    cb_llanta2_tracto.setSelected((boolean)map.get("llanta2"));
                    cb_llanta3_tracto.setSelected((boolean)map.get("llanta3"));
                    cb_llanta4_tracto.setSelected((boolean)map.get("llanta4"));
                    cb_llanta5_tracto.setSelected((boolean)map.get("llanta5"));
                    cb_llanta6_tracto.setSelected((boolean)map.get("llanta6"));
                    cb_llanta7_tracto.setSelected((boolean)map.get("llanta7"));
                    cb_llanta8_tracto.setSelected((boolean)map.get("llanta8"));
                    cb_llanta9_tracto.setSelected((boolean)map.get("llanta9"));
                    cb_llanta10_tracto.setSelected((boolean)map.get("llanta10"));
                    try{
                        t_gas1_tracto.setText(map.get("gas1").toString());
                    }catch(Exception e){}
                    try{
                    t_gas2_tracto.setText(map.get("gas2").toString());
                    }catch(Exception e){}
                    try{
                        t_gas3_tracto.setText(map.get("gas3").toString());
                    }catch(Exception e){}
                    try{
                        t_gas4_tracto.setText(map.get("gas4").toString());
                    }catch(Exception e){}
                    try{
                        ta_comentarios_tracto.setText(map.get("comentarios").toString());
                    }catch(Exception e){}
                    
                    model.addRow(renglon(map.get("defensa").toString(), "defensa"));
                    model.addRow(renglon(map.get("spoiler-de-defensa").toString(), "spoiler-de-defensa"));
                    model.addRow(renglon(map.get("faros-neblineros").toString(), "faros-neblineros"));
                    model.addRow(renglon(map.get("faros").toString(), "faros"));
                    model.addRow(renglon(map.get("cofre").toString(), "cofre"));
                    model.addRow(renglon(map.get("biceles-de-faro").toString(), "biceles-de-faro"));
                    model.addRow(renglon(map.get("emblemas").toString(), "emblemas"));
                    model.addRow(renglon(map.get("concavos-de-cofre").toString(), "concavos-de-cofre"));
                    model.addRow(renglon(map.get("accesorios").toString(), "accesorios"));
                    model.addRow(renglon(map.get("limpiadores").toString(), "limpiadores"));
                    model.addRow(renglon(map.get("parabrisas").toString(), "parabrisas"));
                    model.addRow(renglon(map.get("cristales-laterales").toString(), "cristales-laterales"));
                    model.addRow(renglon(map.get("cristales-de-toldo").toString(), "cristales-de-toldo"));
                    model.addRow(renglon(map.get("espejos-laterales").toString(), "espejos-laterales"));
                    model.addRow(renglon(map.get("puertas").toString(), "puertas"));
                    model.addRow(renglon(map.get("lienzos-de-costado").toString(), "lienzos-de-costado"));
                    model.addRow(renglon(map.get("puertas-de-dormitorio").toString(), "puertas-de-dormitorio"));
                    model.addRow(renglon(map.get("puertas-de-herramientas").toString(), "puertas-de-herramientas"));
                    model.addRow(renglon(map.get("pasamanos").toString(), "pasamanos"));
                    model.addRow(renglon(map.get("llaves").toString(), "llaves"));
                    model.addRow(renglon(map.get("loderas-delanteras").toString(), "loderas-delanteras"));
                    model.addRow(renglon(map.get("caja-de-baterias").toString(), "caja-de-baterias"));
                    model.addRow(renglon(map.get("loderas-intermedias").toString(), "loderas-intermedias"));
                    model.addRow(renglon(map.get("escalera-de-servicio").toString(), "escalera-de-servicio"));
                    model.addRow(renglon(map.get("plataforma-de-servicio").toString(), "plataforma-de-servicio"));
                    model.addRow(renglon(map.get("torre-de-manitas").toString(), "torre-de-manitas"));
                    model.addRow(renglon(map.get("manitas-de-aire-con-mangueras").toString(), "manitas-de-aire-con-mangueras"));
                    model.addRow(renglon(map.get("cable-macho-7-hilos").toString(), "cable-macho-7-hilos"));
                    model.addRow(renglon(map.get("loderas-traseras").toString(), "loderas-traseras"));
                    model.addRow(renglon(map.get("tapones-tanques-combustible").toString(), "tapones-tanques-combustible"));
                    model.addRow(renglon(map.get("antena-via-satelite").toString(), "antena-via-satelite"));
                    model.addRow(renglon(map.get("placas").toString(), "placas"));
                    model.addRow(renglon(map.get("gato").toString(), "gato"));
                    model.addRow(renglon(map.get("herramientas").toString(), "herramientas"));
                    model.addRow(renglon(map.get("señales-de-emergencia").toString(), "señales-de-emergencia"));
                    model.addRow(renglon(map.get("llave-de-ruedas").toString(), "llave-de-ruedas"));
                    model.addRow(renglon(map.get("extinguidor").toString(), "extinguidor"));
                    model.addRow(renglon(map.get("radiador").toString(), "radiador"));
                    model.addRow(renglon(map.get("ventilador").toString(), "ventilador"));
                    model.addRow(renglon(map.get("alternador").toString(), "alternador"));
                    model.addRow(renglon(map.get("turbo").toString(), "turbo"));
                    model.addRow(renglon(map.get("multiples").toString(), "multiples"));
                    model.addRow(renglon(map.get("modulo-(ecm)").toString(), "modulo-(ecm)"));
                    model.addRow(renglon(map.get("transmision").toString(), "transmision"));
                    model.addRow(renglon(map.get("marcha").toString(), "marcha"));
                    model.addRow(renglon(map.get("cornetas-de-aire").toString(), "cornetas-de-aire"));
                    model.addRow(renglon(map.get("caja-de-direccion").toString(), "caja-de-direccion"));
                    model.addRow(renglon(map.get("carter").toString(), "carter"));
                    model.addRow(renglon(map.get("bayoneta-aceite-de-motor").toString(), "bayoneta-aceite-de-motor"));
                    model.addRow(renglon(map.get("filtros").toString(), "filtros"));
                    model.addRow(renglon(map.get("cardan").toString(), "cardan"));
                    model.addRow(renglon(map.get("baterias").toString(), "baterias"));
                    model.addRow(renglon(map.get("escape").toString(), "escape"));
                    model.addRow(renglon(map.get("tanques-de-combustible").toString(), "tanques-de-combustible"));
                    model.addRow(renglon(map.get("tablero").toString(), "tablero"));
                    model.addRow(renglon(map.get("estereo").toString(), "estereo"));
                    model.addRow(renglon(map.get("cb").toString(), "cb"));
                    model.addRow(renglon(map.get("bocinas").toString(), "bocinas"));
                    model.addRow(renglon(map.get("encendedor").toString(), "encendedor"));
                    model.addRow(renglon(map.get("viceras").toString(), "viceras"));
                    model.addRow(renglon(map.get("tapetes").toString(), "tapetes"));
                    model.addRow(renglon(map.get("cortinas").toString(), "cortinas"));
                    model.addRow(renglon(map.get("luces-interiores-de-cabina").toString(), "luces-interiores-de-cabina"));
                    model.addRow(renglon(map.get("luces-interiores-dormitorio").toString(), "luces-interiores-dormitorio"));
                    model.addRow(renglon(map.get("cenicero").toString(), "cenicero"));
                    model.addRow(renglon(map.get("palanca-de-direccionales").toString(), "palanca-de-direccionales"));
                    model.addRow(renglon(map.get("consola-cabina").toString(), "consola-cabina"));
                    model.addRow(renglon(map.get("consola-dormitorio").toString(), "consola-dormitorio"));
                    model.addRow(renglon(map.get("colchon").toString(), "colchon"));
                    model.addRow(renglon(map.get("vestidura-cabina").toString(), "vestidura-cabina"));
                    model.addRow(renglon(map.get("asientos").toString(), "asientos"));
                    model.addRow(renglon(map.get("vestidura-dormitorio").toString(), "vestidura-dormitorio"));
                    model.addRow(renglon(map.get("palanca-de-velocidades").toString(), "palanca-de-velocidades"));
                    model.addRow(renglon(map.get("teclado-gps-via-satelite").toString(), "teclado-gps-via-satelite"));
                }
                
                else
                {
                    cb_llanta1_caja.setSelected((boolean)map.get("llanta1"));
                    cb_llanta2_caja.setSelected((boolean)map.get("llanta2"));
                    cb_llanta3_caja.setSelected((boolean)map.get("llanta3"));
                    cb_llanta4_caja.setSelected((boolean)map.get("llanta4"));
                    cb_llanta5_caja.setSelected((boolean)map.get("llanta5"));
                    cb_llanta6_caja.setSelected((boolean)map.get("llanta6"));
                    cb_llanta7_caja.setSelected((boolean)map.get("llanta7"));
                    cb_llanta8_caja.setSelected((boolean)map.get("llanta8"));
                    try{
                        t_gas_caja.setText(map.get("gas1").toString());
                    }catch(Exception e){}
                    try{
                        ta_comentarios_caja.setText(map.get("comentarios").toString());
                    }catch(Exception e){}
                    
                    model.addRow(renglon(map.get("frente").toString(), "frente"));
                    model.addRow(renglon(map.get("costado-izquierdo").toString(), "costado-izquierdo"));
                    model.addRow(renglon(map.get("costado-derecho").toString(), "costado-derecho"));
                    model.addRow(renglon(map.get("toldo").toString(), "toldo"));        
                    model.addRow(renglon(map.get("piso").toString(), "piso"));
                    model.addRow(renglon(map.get("puerta-lateral").toString(), "puerta-lateral"));
                    model.addRow(renglon(map.get("puertas-traseras").toString(), "puertas-traseras"));
                    model.addRow(renglon(map.get("bastidor-o-chasis").toString(), "bastidor-o-chasis"));
                    model.addRow(renglon(map.get("patines").toString(), "patines"));
                    model.addRow(renglon(map.get("portallantas").toString(), "portallantas"));
                    model.addRow(renglon(map.get("ejes").toString(), "ejes"));
                    model.addRow(renglon(map.get("rines").toString(), "rines"));
                    model.addRow(renglon(map.get("llantas").toString(), "llantas"));
                    model.addRow(renglon(map.get("plafonera").toString(), "plafonera"));
                    model.addRow(renglon(map.get("estribo").toString(), "estribo"));
                    model.addRow(renglon(map.get("borda-superior-izquierda").toString(), "borda-superior-izquierda"));
                    model.addRow(renglon(map.get("borda-superior-derecha").toString(), "borda-superior-derecha"));
                    model.addRow(renglon(map.get("borda-inferior-izquierda").toString(), "borda-inferior-izquierda"));
                    model.addRow(renglon(map.get("borda-inferior-derecha").toString(), "borda-inferior-derecha"));
                    model.addRow(renglon(map.get("suspension-trasera").toString(), "suspension-trasera"));
                    model.addRow(renglon(map.get("ventilador-").toString(), "ventilador-"));
                    model.addRow(renglon(map.get("alternador-").toString(), "alternador-"));
                    model.addRow(renglon(map.get("marcha-").toString(), "marcha-"));
                    model.addRow(renglon(map.get("bayoneta-aceite-motor").toString(), "bayoneta-aceite-motor"));
                    model.addRow(renglon(map.get("filtros-").toString(), "filtros-"));
                    model.addRow(renglon(map.get("baterias-").toString(), "baterias-"));
                }
            }
        }catch(Exception e)
        {
            e.printStackTrace();
        }
        if(session!=null)
            if(session.isOpen())
                session.close();
    }
    public void formatoTabla()
    {
        Color c1 = new java.awt.Color(2, 135, 242);   
        for(int x=0; x<t_datos.getColumnModel().getColumnCount(); x++)
        {
            t_datos.getColumnModel().getColumn(x).setHeaderRenderer(new Render1(c1));
        } 
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
                    column.setPreferredWidth(20);
                    break;
                case 1:               
                    column.setPreferredWidth(150);
                    //column.setCellEditor(new DefaultCellEditor(observacion));
                    break;
                case 2:
                    column.setPreferredWidth(40);
                    break; 
            }
        } 
    }
 
    Object[] renglon(String valor, String nombre){
        String v1[] = valor.toString().split("&");
        Object r1[] = new Object[]{false,nombre,""};
        if(v1[0].toString().compareTo("1")==0)
            r1[0]=true;
        if(v1.length>1)
            r1[2]=v1[1].toString();
        return r1;
    }
}