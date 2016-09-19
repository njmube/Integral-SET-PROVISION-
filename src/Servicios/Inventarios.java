/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */                                          
package Servicios;
import Hibernate.entidades.Orden;
import Hibernate.entidades.Usuario;
import Hibernate.entidades.Inventario;
import org.hibernate.Session;
import Integral.Herramientas;
import Hibernate.Util.HibernateUtil;
import Hibernate.entidades.Conceptos;
import java.awt.Color;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.util.Vector;
import javax.swing.DefaultCellEditor;
import javax.swing.InputMap;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import Integral.Imagen;
import Integral.Render1;
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
    DefaultTableModel model;
    String[] columnas = new String [] {"Id","Nombre","Incluye","Observaciones"};
    Conceptos registro = null;
    
    public Inventarios(String id, Usuario usuario,String edo, String ses) {
        usr=usuario;
        sessionPrograma=ses;
        orden=id;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {            
            //session.beginTransaction().begin();
            Connection conn = (Connection) session.connection();
            CallableStatement call = conn.prepareCall("{ call actualiza() }");
            call.execute();
            //session.beginTransaction().commit();
        }catch(Exception e)
        {
            e.printStackTrace();
            //session.beginTransaction().rollback();
        }
        finally{
            if(session.isOpen()==true)
                session.close();
        }
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
        t_datos2.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        t_datos2.setModel(ModeloTablaReporte(0, columnas));
        formatoTabla();
        
        h=new Herramientas(usr, 0);
        if(h.isCerrada(orden)==true)
        {
            visualiza(false);
            this.jComboBox1.setEnabled(false);
            t_datos2.setEnabled(false);
        }
    }    
    DefaultTableModel ModeloTablaReporte(int renglones, String columnas[])
    {
        model = new DefaultTableModel(new Object [renglones][4], columnas)
        {
            Class[] types = new Class [] {
                java.lang.String.class,
                java.lang.String.class,
                java.lang.Boolean.class,
                java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, true, true
            };
            public void setValueAt(Object value, int row, int col)
            {
                Vector vector = (Vector)dataVector.elementAt(row);
                Object celda = ((Vector)dataVector.elementAt(row)).elementAt(col);
                switch(col)
                {
                    case 0:
                        if(vector.get(col)==null)
                        {
                            vector.setElementAt(value, col);
                            dataVector.setElementAt(vector, row);
                            fireTableCellUpdated(row, col);
                        }
                        break;
                        
                    case 1:
                        if(vector.get(col)==null)
                        {
                            vector.setElementAt(value, col);
                            dataVector.setElementAt(vector, row);
                            fireTableCellUpdated(row, col);
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
                            try
                            {
                                session.beginTransaction();
                                Inventario inv=(Inventario)session.createCriteria(Inventario.class).add(Restrictions. eq("idInventario", Integer.parseInt(t_datos2.getValueAt(row, 0).toString()))).setMaxResults(1).uniqueResult();
                                if(inv!=null)
                                {
                                    inv.setContiene((boolean)value);
                                    session.update(inv);
                                    session.getTransaction().commit();
                                    vector.setElementAt(value, col);
                                    dataVector.setElementAt(vector, row);
                                    fireTableCellUpdated(row, col);
                                    if(session.isOpen()==true)
                                        session.close();
                                    buscaDato(jComboBox1.getSelectedIndex());
                                }
                                else
                                {
                                    buscaDato(jComboBox1.getSelectedIndex());
                                    JOptionPane.showMessageDialog(null, "¡El registro no existe!");
                                }
                            }
                            catch(Exception e)
                            {
                                session.getTransaction().rollback();
                                System.out.println(e);
                                buscaDato(jComboBox1.getSelectedIndex());
                            }
                            if(session!=null)
                                if(session.isOpen()==true)
                                    session.close();
                        }
                        break;
                    
                    case 3:
                        if(vector.get(col)==null)
                        {
                            vector.setElementAt(value, col);
                            dataVector.setElementAt(vector, row);
                            fireTableCellUpdated(row, col);
                        }
                        else
                        {
                            Session session = HibernateUtil.getSessionFactory().openSession();
                            try
                            {
                                session.beginTransaction();
                                Inventario inv=(Inventario) session.createCriteria(Inventario.class).add(Restrictions. eq("idInventario", Integer.parseInt(t_datos2.getValueAt(row, 0).toString()))).setMaxResults(1).uniqueResult();
                                if(inv!=null)
                                {
                                    if(value.toString().compareTo("")==0)
                                        inv.setObservacion(null);
                                    else
                                        inv.setObservacion(value.toString());
                                    session.update(inv);
                                    session.getTransaction().commit();
                                    vector.setElementAt(value, col);
                                    dataVector.setElementAt(vector, row);
                                    fireTableCellUpdated(row, col);
                                    if(session.isOpen()==true)
                                        session.close();
                                    buscaDato(jComboBox1.getSelectedIndex());
                                }
                                else
                                {
                                    buscaDato(jComboBox1.getSelectedIndex());
                                    JOptionPane.showMessageDialog(null, "¡El registro no existe!");
                                }
                            }
                            catch(Exception e)
                            {
                                session.getTransaction().rollback();
                                System.out.println(e);
                                buscaDato(jComboBox1.getSelectedIndex());
                            }
                            if(session!=null)
                                if(session.isOpen()==true)
                                    session.close();
                        }
                        break;                        
                    default:
                            vector.setElementAt(value, col);
                            dataVector.setElementAt(vector, row);
                            fireTableCellUpdated(row, col);
                            break;
                        }
                 }
                @Override
                public Class getColumnClass(int columnIndex) {
                    return types [columnIndex];
                }

                @Override
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

        observacion = new javax.swing.JTextField();
        Tracto = new javax.swing.JPanel();
        uno1 = new javax.swing.JCheckBox();
        dos1 = new javax.swing.JCheckBox();
        tres1 = new javax.swing.JCheckBox();
        cuatro1 = new javax.swing.JCheckBox();
        diez1 = new javax.swing.JCheckBox();
        nueve1 = new javax.swing.JCheckBox();
        ocho1 = new javax.swing.JCheckBox();
        cinco1 = new javax.swing.JCheckBox();
        seis1 = new javax.swing.JCheckBox();
        siete1 = new javax.swing.JCheckBox();
        jLabel5 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        l = new javax.swing.JTextArea();
        l_g4 = new javax.swing.JTextField();
        l_g1 = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        l_g3 = new javax.swing.JTextField();
        l_g2 = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        Caja = new javax.swing.JPanel();
        uno2 = new javax.swing.JCheckBox();
        dos2 = new javax.swing.JCheckBox();
        tres2 = new javax.swing.JCheckBox();
        cuatro2 = new javax.swing.JCheckBox();
        ocho2 = new javax.swing.JCheckBox();
        cinco2 = new javax.swing.JCheckBox();
        seis2 = new javax.swing.JCheckBox();
        siete2 = new javax.swing.JCheckBox();
        jLabel6 = new javax.swing.JLabel();
        l_g1c = new javax.swing.JTextField();
        jScrollPane3 = new javax.swing.JScrollPane();
        l1 = new javax.swing.JTextArea();
        jLabel7 = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        inventariodeunidad = new javax.swing.JLabel();
        unidad = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox();
        listado = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        t_datos2 = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();

        observacion.setText("jTextField1");
        observacion.setBorder(null);
        observacion.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                observacionKeyTyped(evt);
            }
        });

        Tracto.setBackground(new java.awt.Color(254, 254, 254));
        Tracto.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        uno1.setToolTipText("Llanta uno");
        uno1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                uno1ActionPerformed(evt);
            }
        });
        Tracto.add(uno1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 10, -1, -1));

        dos1.setToolTipText("Llanta dos");
        dos1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dos1ActionPerformed(evt);
            }
        });
        Tracto.add(dos1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 30, -1, -1));

        tres1.setToolTipText("Llanta tres");
        tres1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tres1ActionPerformed(evt);
            }
        });
        Tracto.add(tres1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 120, -1, -1));

        cuatro1.setToolTipText("Llanta cuatro");
        cuatro1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cuatro1ActionPerformed(evt);
            }
        });
        Tracto.add(cuatro1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 140, -1, -1));

        diez1.setToolTipText("Llanta diez");
        diez1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                diez1ActionPerformed(evt);
            }
        });
        Tracto.add(diez1, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 140, -1, -1));

        nueve1.setToolTipText("Llanta nueve");
        nueve1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nueve1ActionPerformed(evt);
            }
        });
        Tracto.add(nueve1, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 10, -1, -1));

        ocho1.setToolTipText("Llanta ocho");
        ocho1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ocho1ActionPerformed(evt);
            }
        });
        Tracto.add(ocho1, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 140, -1, -1));

        cinco1.setToolTipText("Llanta cinco");
        cinco1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cinco1ActionPerformed(evt);
            }
        });
        Tracto.add(cinco1, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 10, -1, -1));

        seis1.setToolTipText("Llanta seis");
        seis1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seis1ActionPerformed(evt);
            }
        });
        Tracto.add(seis1, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 30, -1, -1));

        siete1.setToolTipText("Llanta siete");
        siete1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                siete1ActionPerformed(evt);
            }
        });
        Tracto.add(siete1, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 120, -1, -1));
        Tracto.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 420, 190));

        l.setColumns(20);
        l.setRows(5);
        l.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                lKeyTyped(evt);
            }
        });
        jScrollPane2.setViewportView(l);

        Tracto.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 250, 310, 30));

        l_g4.setNextFocusableComponent(l);
        l_g4.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                l_g4FocusLost(evt);
            }
        });
        l_g4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                l_g4ActionPerformed(evt);
            }
        });
        l_g4.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                l_g4KeyTyped(evt);
            }
        });
        Tracto.add(l_g4, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 190, 60, -1));

        l_g1.setNextFocusableComponent(l);
        l_g1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                l_g1FocusLost(evt);
            }
        });
        l_g1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                l_g1ActionPerformed(evt);
            }
        });
        l_g1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                l_g1KeyTyped(evt);
            }
        });
        Tracto.add(l_g1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 190, 60, -1));
        Tracto.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 140, 100, 110));
        Tracto.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 140, 110, 110));

        l_g3.setNextFocusableComponent(l);
        l_g3.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                l_g3FocusLost(evt);
            }
        });
        l_g3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                l_g3ActionPerformed(evt);
            }
        });
        l_g3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                l_g3KeyTyped(evt);
            }
        });
        Tracto.add(l_g3, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 190, 60, -1));

        l_g2.setNextFocusableComponent(l);
        l_g2.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                l_g2FocusLost(evt);
            }
        });
        l_g2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                l_g2ActionPerformed(evt);
            }
        });
        l_g2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                l_g2KeyTyped(evt);
            }
        });
        Tracto.add(l_g2, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 190, 60, -1));
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

        uno2.setToolTipText("Llanta uno");
        uno2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                uno2ActionPerformed(evt);
            }
        });
        Caja.add(uno2, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 10, -1, -1));

        dos2.setToolTipText("Llanta dos");
        dos2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dos2ActionPerformed(evt);
            }
        });
        Caja.add(dos2, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 30, -1, -1));

        tres2.setToolTipText("Llanta tres");
        tres2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tres2ActionPerformed(evt);
            }
        });
        Caja.add(tres2, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 120, -1, -1));

        cuatro2.setToolTipText("Llanta cuatro");
        cuatro2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cuatro2ActionPerformed(evt);
            }
        });
        Caja.add(cuatro2, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 140, -1, -1));

        ocho2.setToolTipText("Llanta ocho");
        ocho2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ocho2ActionPerformed(evt);
            }
        });
        Caja.add(ocho2, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 140, -1, 20));

        cinco2.setToolTipText("Llanta cinco");
        cinco2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cinco2ActionPerformed(evt);
            }
        });
        Caja.add(cinco2, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 10, -1, -1));

        seis2.setToolTipText("Llanta seis");
        seis2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seis2ActionPerformed(evt);
            }
        });
        Caja.add(seis2, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 30, -1, -1));

        siete2.setToolTipText("Llanta siete");
        siete2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                siete2ActionPerformed(evt);
            }
        });
        Caja.add(siete2, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 120, -1, 20));
        Caja.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 480, 200));

        l_g1c.setToolTipText("");
        l_g1c.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                l_g1cActionPerformed(evt);
            }
        });
        l_g1c.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                l_g1cFocusLost(evt);
            }
        });
        l_g1c.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                l_g1cKeyTyped(evt);
            }
        });
        Caja.add(l_g1c, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 190, 60, -1));

        l1.setColumns(20);
        l1.setRows(5);
        l1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                l1KeyTyped(evt);
            }
        });
        jScrollPane3.setViewportView(l1);

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

        listado.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        listado.setText("Listado de inventario");

        t_datos2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Id", "Nombre", "Incluye", "Observaciones"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                true, false, true, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        t_datos2.setColumnSelectionAllowed(true);
        t_datos2.getTableHeader().setReorderingAllowed(false);
        jScrollPane4.setViewportView(t_datos2);
        t_datos2.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);

        jPanel2.setBackground(new java.awt.Color(254, 254, 254));
        jPanel2.setLayout(new java.awt.CardLayout());

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
                .addGap(102, 102, 102)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(listado)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 411, Short.MAX_VALUE)))
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
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 241, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(31, Short.MAX_VALUE))
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void observacionKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_observacionKeyTyped
        // TODO add your handling code here:
        char car = evt.getKeyChar();
        evt.setKeyChar(Character.toUpperCase(evt.getKeyChar()));
        if(observacion.getText().length()>=50)
            evt.consume();
    }//GEN-LAST:event_observacionKeyTyped

    private void l_g1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_l_g1KeyTyped
        char car = evt.getKeyChar();
        if(l_g1.getText().length()>=6) 
            evt.consume();
        if((car<'0' && car!='.') || (car>'9' && car!='.')) 
            evt.consume();
    }//GEN-LAST:event_l_g1KeyTyped

    private void l_g2KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_l_g2KeyTyped
        char car = evt.getKeyChar();
        if(l_g2.getText().length()>=6) 
            evt.consume();
        if((car<'0' && car!='.') || (car>'9' && car!='.')) 
            evt.consume();
    }//GEN-LAST:event_l_g2KeyTyped

    private void l_g3KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_l_g3KeyTyped
        char car = evt.getKeyChar();
        if(l_g3.getText().length()>=6) 
            evt.consume();
        if((car<'0' && car!='.') || (car>'9' && car!='.')) 
            evt.consume();
    }//GEN-LAST:event_l_g3KeyTyped

    private void l_g4KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_l_g4KeyTyped
        char car = evt.getKeyChar();
        if(l_g4.getText().length()>=6) 
            evt.consume();
        if((car<'0' && car!='.') || (car>'9' && car!='.')) 
            evt.consume();
    }//GEN-LAST:event_l_g4KeyTyped

    private void l_g1cKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_l_g1cKeyTyped
        char car = evt.getKeyChar();
        if(l_g1c.getText().length()>=6) 
            evt.consume();
        if((car<'0' && car!='.') || (car>'9' && car!='.')) 
            evt.consume();
    }//GEN-LAST:event_l_g1cKeyTyped

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
                buscaDato(0);
                break;
                    
            case 1:
                listado.setText("Inventario de Caja");
                jPanel2.removeAll();
                jPanel2.add(Caja);
                jLabel6.add(new Imagen("imagenes/llantas-caja.jpg", 399, 170, 1, 0, 400, 220));
                jLabel7.add(new Imagen("imagenes/GAS.jpg", 100, 80, 1, 25, 400, 220));
                jPanel2.repaint();
                buscaDato(1);
                break;
        }
    }//GEN-LAST:event_jComboBox1ItemStateChanged

    private void tres1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tres1ActionPerformed
        Session session = HibernateUtil.getSessionFactory().openSession();
        try
        {
            Inventario inv=(Inventario)session.createCriteria(Inventario.class).add(Restrictions.eq("orden.idOrden", Integer.parseInt(orden))).add(Restrictions.eq("conceptos.idConcepto",3)).setMaxResults(1).uniqueResult();
            if(inv!=null)
            {
                session.beginTransaction();
                inv.setContiene(tres1.isSelected());
                session.update(inv);
                session.getTransaction().commit();
                buscaDato(jComboBox1.getSelectedIndex());
            }
            else
            {
                JOptionPane.showMessageDialog(null, "¡El registro no se encontró!");
            }
        }
        catch(Exception e)
        {
            session.getTransaction().rollback();
            System.out.println(e);
            buscaDato(jComboBox1.getSelectedIndex());
            JOptionPane.showMessageDialog(null, "¡El registro no existe!");
        }
        if(session!=null)
            if(session.isOpen()==true)
                session.close();
    }//GEN-LAST:event_tres1ActionPerformed

    private void uno1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_uno1ActionPerformed
        Session session = HibernateUtil.getSessionFactory().openSession();
        try
        {
            Inventario inv=(Inventario)session.createCriteria(Inventario.class).add(Restrictions.eq("orden.idOrden", Integer.parseInt(orden))).add(Restrictions.eq("conceptos.idConcepto",1)).setMaxResults(1).uniqueResult();
            if(inv!=null)
            {
                session.beginTransaction();
                inv.setContiene(uno1.isSelected());
                session.update(inv);
                session.getTransaction().commit();
                buscaDato(jComboBox1.getSelectedIndex());
            }
            else
            {
                JOptionPane.showMessageDialog(null, "¡El registro no se encontró!");
            }
        }
        catch(Exception e)
        {
            session.getTransaction().rollback();
            System.out.println(e);
            buscaDato(jComboBox1.getSelectedIndex());
            JOptionPane.showMessageDialog(null, "¡El registro no existe!");
        }
        if(session!=null)
            if(session.isOpen()==true)
                session.close();
    }//GEN-LAST:event_uno1ActionPerformed

    private void cuatro1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cuatro1ActionPerformed
        Session session = HibernateUtil.getSessionFactory().openSession();
        try
        {
            Inventario inv=(Inventario)session.createCriteria(Inventario.class).add(Restrictions.eq("orden.idOrden", Integer.parseInt(orden))).add(Restrictions.eq("conceptos.idConcepto",4)).setMaxResults(1).uniqueResult();
            if(inv!=null)
            {
            session.beginTransaction();
            inv.setContiene(cuatro1.isSelected());
            session.update(inv);
            session.getTransaction().commit();
            buscaDato(jComboBox1.getSelectedIndex());
            }
            else
            {
                JOptionPane.showMessageDialog(null, "¡El registro no se encontró!");
            }
        }
        catch(Exception e)
        {
            session.getTransaction().rollback();
            System.out.println(e);
            buscaDato(jComboBox1.getSelectedIndex());
            JOptionPane.showMessageDialog(null, "¡El registro no existe!");
        }
        if(session!=null)
            if(session.isOpen()==true)
                session.close();
    }//GEN-LAST:event_cuatro1ActionPerformed

    private void cinco1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cinco1ActionPerformed
        Session session = HibernateUtil.getSessionFactory().openSession();
        try
        {
            Inventario inv=(Inventario)session.createCriteria(Inventario.class).add(Restrictions.eq("orden.idOrden", Integer.parseInt(orden))).add(Restrictions.eq("conceptos.idConcepto",5)).setMaxResults(1).uniqueResult();
            if(inv!=null)
            {
            session.beginTransaction();
            inv.setContiene(cinco1.isSelected());
            session.update(inv);
            session.getTransaction().commit();
            buscaDato(jComboBox1.getSelectedIndex());
            }
            else
            {
                JOptionPane.showMessageDialog(null, "¡El registro no se encontró!");
            }
        }
        catch(Exception e)
        {
            session.getTransaction().rollback();
            System.out.println(e);
            buscaDato(jComboBox1.getSelectedIndex());
            JOptionPane.showMessageDialog(null, "¡El registro no existe!");
        }
        if(session!=null)
            if(session.isOpen()==true)
                session.close();
    }//GEN-LAST:event_cinco1ActionPerformed

    private void seis1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_seis1ActionPerformed
        Session session = HibernateUtil.getSessionFactory().openSession();
        try
        {
            Inventario inv=(Inventario)session.createCriteria(Inventario.class).add(Restrictions.eq("orden.idOrden", Integer.parseInt(orden))).add(Restrictions.eq("conceptos.idConcepto",6)).setMaxResults(1).uniqueResult();
            if(inv!=null)
            {
            session.beginTransaction();
            inv.setContiene(seis1.isSelected());
            session.update(inv);
            session.getTransaction().commit();
            buscaDato(jComboBox1.getSelectedIndex());
            }
            else
            {
                JOptionPane.showMessageDialog(null, "¡El registro no se encontró!");
            }
        }
        catch(Exception e)
        {
            session.getTransaction().rollback();
            System.out.println(e);
            buscaDato(jComboBox1.getSelectedIndex());
            JOptionPane.showMessageDialog(null, "¡El registro no existe!");
        }
        if(session!=null)
            if(session.isOpen()==true)
                session.close();
    }//GEN-LAST:event_seis1ActionPerformed

    private void siete1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_siete1ActionPerformed
        Session session = HibernateUtil.getSessionFactory().openSession();
        try
        {
            Inventario inv=(Inventario)session.createCriteria(Inventario.class).add(Restrictions.eq("orden.idOrden", Integer.parseInt(orden))).add(Restrictions.eq("conceptos.idConcepto",7)).setMaxResults(1).uniqueResult();
            if(inv!=null)
            {
            session.beginTransaction();
            inv.setContiene(siete1.isSelected());
            session.update(inv);
            session.getTransaction().commit();
            buscaDato(jComboBox1.getSelectedIndex());
            }
            else
            {
                JOptionPane.showMessageDialog(null, "¡El registro no se encontró!");
            }
        }
        catch(Exception e)
        {
            session.getTransaction().rollback();
            System.out.println(e);
            buscaDato(jComboBox1.getSelectedIndex());
            JOptionPane.showMessageDialog(null, "¡El registro no existe!");
        }
        if(session!=null)
            if(session.isOpen()==true)
                session.close();
    }//GEN-LAST:event_siete1ActionPerformed

    private void ocho1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ocho1ActionPerformed
        Session session = HibernateUtil.getSessionFactory().openSession();
        try
        {
            Inventario inv=(Inventario)session.createCriteria(Inventario.class).add(Restrictions.eq("orden.idOrden", Integer.parseInt(orden))).add(Restrictions.eq("conceptos.idConcepto",8)).setMaxResults(1).uniqueResult();
            if(inv!=null)
            {
            session.beginTransaction();
            inv.setContiene(ocho1.isSelected());
            session.update(inv);
            session.getTransaction().commit();
            buscaDato(jComboBox1.getSelectedIndex());
            }
            else
            {
                JOptionPane.showMessageDialog(null, "¡El registro no se encontró!");
            }
        }
        catch(Exception e)
        {
            session.getTransaction().rollback();
            System.out.println(e);
            buscaDato(jComboBox1.getSelectedIndex());
            JOptionPane.showMessageDialog(null, "¡El registro no existe!");
        }
        if(session!=null)
            if(session.isOpen()==true)
                session.close();
    }//GEN-LAST:event_ocho1ActionPerformed

    private void nueve1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nueve1ActionPerformed
        Session session = HibernateUtil.getSessionFactory().openSession();
        try
        {
            Inventario inv=(Inventario)session.createCriteria(Inventario.class).add(Restrictions.eq("orden.idOrden", Integer.parseInt(orden))).add(Restrictions.eq("conceptos.idConcepto",9)).setMaxResults(1).uniqueResult();
            if(inv!=null)
            {
                session.beginTransaction();
               inv.setContiene(nueve1.isSelected());
                session.update(inv);
                session.getTransaction().commit();
                buscaDato(jComboBox1.getSelectedIndex());
            }
            else
            {
                JOptionPane.showMessageDialog(null, "¡El registro no se encontró!");
            }
        }
        catch(Exception e)
        {
            session.getTransaction().rollback();
            System.out.println(e);
            buscaDato(jComboBox1.getSelectedIndex());
            JOptionPane.showMessageDialog(null, "¡El registro no existe!");
        }
        if(session!=null)
            if(session.isOpen()==true)
                session.close();
    }//GEN-LAST:event_nueve1ActionPerformed

    private void diez1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_diez1ActionPerformed
        Session session = HibernateUtil.getSessionFactory().openSession();
        try
        {
            Inventario inv=(Inventario)session.createCriteria(Inventario.class).add(Restrictions.eq("orden.idOrden", Integer.parseInt(orden))).add(Restrictions.eq("conceptos.idConcepto",10)).setMaxResults(1).uniqueResult();
            if(inv!=null)
            {
            session.beginTransaction();
            inv.setContiene(diez1.isSelected());
            session.update(inv);
            session.getTransaction().commit();
            buscaDato(jComboBox1.getSelectedIndex());
            }
            else
            {
                JOptionPane.showMessageDialog(null, "¡El registro no se encontró!");
            }
        }
        catch(Exception e)
        {
            session.getTransaction().rollback();
            System.out.println(e);
            buscaDato(jComboBox1.getSelectedIndex());
            JOptionPane.showMessageDialog(null, "¡El registro no existe!");
        }
        if(session!=null)
            if(session.isOpen()==true)
                session.close();
    }//GEN-LAST:event_diez1ActionPerformed

    private void uno2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_uno2ActionPerformed
        Session session = HibernateUtil.getSessionFactory().openSession();
        try
        {
            Inventario inv=(Inventario)session.createCriteria(Inventario.class).add(Restrictions.eq("orden.idOrden", Integer.parseInt(orden))).add(Restrictions.eq("conceptos.idConcepto",15)).setMaxResults(1).uniqueResult();
            if(inv!=null)
            {
            session.beginTransaction();
            inv.setContiene(uno2.isSelected());
            session.update(inv);
            session.getTransaction().commit();
            buscaDato(jComboBox1.getSelectedIndex());
            }
            else
            {
                JOptionPane.showMessageDialog(null, "¡El registro no se encontró!");
            }
        }
        catch(Exception e)
        {
            session.getTransaction().rollback();
            System.out.println(e);
            buscaDato(jComboBox1.getSelectedIndex());
            JOptionPane.showMessageDialog(null, "¡El registro no existe!");
        }
        if(session!=null)
            if(session.isOpen()==true)
                session.close();
    }//GEN-LAST:event_uno2ActionPerformed

    private void dos2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dos2ActionPerformed
        Session session = HibernateUtil.getSessionFactory().openSession();
        try
        {
            Inventario inv=(Inventario)session.createCriteria(Inventario.class).add(Restrictions.eq("orden.idOrden", Integer.parseInt(orden))).add(Restrictions.eq("conceptos.idConcepto",16)).setMaxResults(1).uniqueResult();
            if(inv!=null)
            {
            session.beginTransaction();
            inv.setContiene(dos2.isSelected());
            session.update(inv);
            session.getTransaction().commit();
            buscaDato(jComboBox1.getSelectedIndex());
            }
            else
            {
                JOptionPane.showMessageDialog(null, "¡El registro no se encontró!");
            }
        }
        catch(Exception e)
        {
            session.getTransaction().rollback();
            System.out.println(e);
            buscaDato(jComboBox1.getSelectedIndex());
            JOptionPane.showMessageDialog(null, "¡El registro no existe!");
        }
        if(session!=null)
            if(session.isOpen()==true)
                session.close();
    }//GEN-LAST:event_dos2ActionPerformed

    private void tres2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tres2ActionPerformed
        Session session = HibernateUtil.getSessionFactory().openSession();
        try
        {
            Inventario inv=(Inventario)session.createCriteria(Inventario.class).add(Restrictions.eq("orden.idOrden", Integer.parseInt(orden))).add(Restrictions.eq("conceptos.idConcepto",17)).setMaxResults(1).uniqueResult();
            if(inv!=null)
            {
            session.beginTransaction();
            inv.setContiene(tres2.isSelected());
            session.update(inv);
            session.getTransaction().commit();
            buscaDato(jComboBox1.getSelectedIndex());
            }
            else
            {
                JOptionPane.showMessageDialog(null, "¡El registro no se encontró!");
            }
        }
        catch(Exception e)
        {
            session.getTransaction().rollback();
            System.out.println(e);
            buscaDato(jComboBox1.getSelectedIndex());
            JOptionPane.showMessageDialog(null, "¡El registro no existe!");
        }
        if(session!=null)
            if(session.isOpen()==true)
                session.close();
    }//GEN-LAST:event_tres2ActionPerformed

    private void cuatro2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cuatro2ActionPerformed
        Session session = HibernateUtil.getSessionFactory().openSession();
        try
        {
            Inventario inv=(Inventario)session.createCriteria(Inventario.class).add(Restrictions.eq("orden.idOrden", Integer.parseInt(orden))).add(Restrictions.eq("conceptos.idConcepto",18)).setMaxResults(1).uniqueResult();
            if(inv!=null)
            {
                session.beginTransaction();
                inv.setContiene(cuatro2.isSelected());
                session.update(inv);
                session.getTransaction().commit();
                buscaDato(jComboBox1.getSelectedIndex());
            }
            else
            {
                JOptionPane.showMessageDialog(null, "¡El registro no se encontró!");
            }
        }
        catch(Exception e)
        {
            session.getTransaction().rollback();
            System.out.println(e);
            buscaDato(jComboBox1.getSelectedIndex());
            JOptionPane.showMessageDialog(null, "¡El registro no existe!");
        }
        if(session!=null)
            if(session.isOpen()==true)
                session.close();
    }//GEN-LAST:event_cuatro2ActionPerformed

    private void cinco2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cinco2ActionPerformed
        Session session = HibernateUtil.getSessionFactory().openSession();
        try
        {
            Inventario inv=(Inventario)session.createCriteria(Inventario.class).add(Restrictions.eq("orden.idOrden", Integer.parseInt(orden))).add(Restrictions.eq("conceptos.idConcepto",19)).setMaxResults(1).uniqueResult();
            if(inv!=null)
            {
                session.beginTransaction();
                inv.setContiene(cinco2.isSelected());
                session.update(inv);
                session.getTransaction().commit();
                buscaDato(jComboBox1.getSelectedIndex());
            }
            else
            {
                JOptionPane.showMessageDialog(null, "¡El registro no se encontró!");
            }
        }
        catch(Exception e)
        {
            session.getTransaction().rollback();
            System.out.println(e);
            buscaDato(jComboBox1.getSelectedIndex());
            JOptionPane.showMessageDialog(null, "¡El registro no existe!");
        }
        if(session!=null)
            if(session.isOpen()==true)
                session.close();
    }//GEN-LAST:event_cinco2ActionPerformed

    private void seis2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_seis2ActionPerformed
        Session session = HibernateUtil.getSessionFactory().openSession();
        try
        {
            Inventario inv=(Inventario)session.createCriteria(Inventario.class).add(Restrictions.eq("orden.idOrden", Integer.parseInt(orden))).add(Restrictions.eq("conceptos.idConcepto",20)).setMaxResults(1).uniqueResult();
            if(inv!=null)
            {
                session.beginTransaction();
                inv.setContiene(seis2.isSelected());
                session.update(inv);
                session.getTransaction().commit();
                buscaDato(jComboBox1.getSelectedIndex());
            }
            else
            {
                JOptionPane.showMessageDialog(null, "¡El registro no se encontró!");
            }
        }
        catch(Exception e)
        {
            session.getTransaction().rollback();
            System.out.println(e);
            buscaDato(jComboBox1.getSelectedIndex());
            JOptionPane.showMessageDialog(null, "¡El registro no existe!");
        }
        if(session!=null)
            if(session.isOpen()==true)
                session.close();
    }//GEN-LAST:event_seis2ActionPerformed

    private void siete2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_siete2ActionPerformed
        Session session = HibernateUtil.getSessionFactory().openSession();
        try
        {
            Inventario inv=(Inventario)session.createCriteria(Inventario.class).add(Restrictions.eq("orden.idOrden", Integer.parseInt(orden))).add(Restrictions.eq("conceptos.idConcepto",21)).setMaxResults(1).uniqueResult();
            if(inv!=null)
            {
                session.beginTransaction();
                inv.setContiene(siete2.isSelected());
                session.update(inv);
                session.getTransaction().commit();
                buscaDato(jComboBox1.getSelectedIndex());
            }
            else
            {
                JOptionPane.showMessageDialog(null, "¡El registro no se encontró!");
            }
        }
        catch(Exception e)
        {
            session.getTransaction().rollback();
            System.out.println(e);
            buscaDato(jComboBox1.getSelectedIndex());
            JOptionPane.showMessageDialog(null, "¡El registro no existe!");
        }
        if(session!=null)
            if(session.isOpen()==true)
                session.close();
    }//GEN-LAST:event_siete2ActionPerformed

    private void dos1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dos1ActionPerformed
        Session session = HibernateUtil.getSessionFactory().openSession();
        try
        {
            Inventario inv=(Inventario)session.createCriteria(Inventario.class).add(Restrictions.eq("orden.idOrden", Integer.parseInt(orden))).add(Restrictions.eq("conceptos.idConcepto",2)).setMaxResults(1).uniqueResult();        
            if(inv!=null)
            {
                session.beginTransaction();
                inv.setContiene(dos1.isSelected());
                session.update(inv);
                session.getTransaction().commit();
                buscaDato(jComboBox1.getSelectedIndex());
            }
            else
            {
                JOptionPane.showMessageDialog(null, "¡El registro no se encontró!");
            }
        }
        catch(Exception e)
        {
            session.getTransaction().rollback();
            System.out.println(e);
            buscaDato(jComboBox1.getSelectedIndex());
            JOptionPane.showMessageDialog(null, "¡El registro no existe!");
        }
        if(session!=null)
            if(session.isOpen()==true)
            session.close();
    }//GEN-LAST:event_dos1ActionPerformed

    private void ocho2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ocho2ActionPerformed
        Session session = HibernateUtil.getSessionFactory().openSession();
        try
        {
            Inventario inv=(Inventario)session.createCriteria(Inventario.class).add(Restrictions.eq("orden.idOrden", Integer.parseInt(orden))).add(Restrictions.eq("conceptos.idConcepto",22)).setMaxResults(1).uniqueResult();
            if(inv!=null)
            {
                session.beginTransaction();
                inv.setContiene(ocho2.isSelected());
                session.update(inv);
                session.getTransaction().commit();
                buscaDato(jComboBox1.getSelectedIndex());
            }
            else
            {
                JOptionPane.showMessageDialog(null, "¡El registro no se encontró!");
            }
        }
        catch(Exception e)
        {
            session.getTransaction().rollback();
            System.out.println(e);
            buscaDato(jComboBox1.getSelectedIndex());
            JOptionPane.showMessageDialog(null, "¡El registro no existe!");
        }
        if(session!=null)
            if(session.isOpen()==true)
                session.close();
    }//GEN-LAST:event_ocho2ActionPerformed

    private void l_g1FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_l_g1FocusLost
        Session session = HibernateUtil.getSessionFactory().openSession();
        try
        {
            Inventario inv=(Inventario)session.createCriteria(Inventario.class).add(Restrictions.eq("orden.idOrden", Integer.parseInt(orden))).add(Restrictions.eq("conceptos.idConcepto",11)).setMaxResults(1).uniqueResult();
            if(inv!=null)
            {
                session.beginTransaction();
                inv.setObservacion(l_g1.getText());
                session.update(inv);
                session.getTransaction().commit();
                buscaDato(jComboBox1.getSelectedIndex());
            }
            else
            {
                JOptionPane.showMessageDialog(null, "¡El registro no se encontró!");
            }
        }
        catch(Exception e)
        {
            session.getTransaction().rollback();
            System.out.println(e);
            buscaDato(jComboBox1.getSelectedIndex());
            JOptionPane.showMessageDialog(null, "¡El registro no existe!");
        }
        if(session!=null)
            if(session.isOpen()==true)
                session.close();
    }//GEN-LAST:event_l_g1FocusLost

    private void l_g2FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_l_g2FocusLost
        Session session = HibernateUtil.getSessionFactory().openSession();
        try
        {
            Inventario inv=(Inventario)session.createCriteria(Inventario.class).add(Restrictions.eq("orden.idOrden", Integer.parseInt(orden))).add(Restrictions.eq("conceptos.idConcepto",12)).setMaxResults(1).uniqueResult();
            if(inv!=null)
            {
                session.beginTransaction();
                inv.setObservacion(l_g2.getText());
                session.update(inv);
                session.getTransaction().commit();
                buscaDato(jComboBox1.getSelectedIndex());
            }
            else
            {
                JOptionPane.showMessageDialog(null, "¡El registro no se encontró!");
            }
        }
        catch(Exception e)
        {
            session.getTransaction().rollback();
            System.out.println(e);
            buscaDato(jComboBox1.getSelectedIndex());
            JOptionPane.showMessageDialog(null, "¡El registro no existe!");
        }
        if(session!=null)
            if(session.isOpen()==true)
                session.close();
    }//GEN-LAST:event_l_g2FocusLost

    private void l_g3FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_l_g3FocusLost
        Session session = HibernateUtil.getSessionFactory().openSession();
        try
        {
            Inventario inv=(Inventario)session.createCriteria(Inventario.class).add(Restrictions.eq("orden.idOrden", Integer.parseInt(orden))).add(Restrictions.eq("conceptos.idConcepto",13)).setMaxResults(1).uniqueResult();
            if(inv!=null)
            {
                session.beginTransaction();
                inv.setObservacion(l_g3.getText());
                session.update(inv);
                session.getTransaction().commit();
                buscaDato(jComboBox1.getSelectedIndex());
            }
            else
            {
                JOptionPane.showMessageDialog(null, "¡El registro no se encontró!");
            }
        }
        catch(Exception e)
        {
            session.getTransaction().rollback();
            System.out.println(e);
            buscaDato(jComboBox1.getSelectedIndex());
            JOptionPane.showMessageDialog(null, "¡El registro no existe!");
        }
        if(session!=null)
            if(session.isOpen()==true)
                session.close();
    }//GEN-LAST:event_l_g3FocusLost

    private void l_g4FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_l_g4FocusLost
        Session session = HibernateUtil.getSessionFactory().openSession();
        try
        {
            Inventario inv=(Inventario)session.createCriteria(Inventario.class).add(Restrictions.eq("orden.idOrden", Integer.parseInt(orden))).add(Restrictions.eq("conceptos.idConcepto",14)).setMaxResults(1).uniqueResult();
            if(inv!=null)
            {
                session.beginTransaction();
                inv.setObservacion(l_g4.getText());
                session.update(inv);
                session.getTransaction().commit();
                buscaDato(jComboBox1.getSelectedIndex());
            }
            else
            {
                JOptionPane.showMessageDialog(null, "¡El registro no se encontró!");
            }
        }
        catch(Exception e)
        {
            session.getTransaction().rollback();
            System.out.println(e);
            buscaDato(jComboBox1.getSelectedIndex());
            JOptionPane.showMessageDialog(null, "¡El registro no existe!");
        }
        if(session!=null)
            if(session.isOpen()==true)
                session.close();
    }//GEN-LAST:event_l_g4FocusLost

    private void l_g1cFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_l_g1cFocusLost
        Session session = HibernateUtil.getSessionFactory().openSession();
        try
        {
            Inventario inv=(Inventario)session.createCriteria(Inventario.class).add(Restrictions.eq("orden.idOrden", Integer.parseInt(orden))).add(Restrictions.eq("conceptos.idConcepto",23)).setMaxResults(1).uniqueResult();
            if(inv!=null)
            {
                session.beginTransaction();
                inv.setObservacion(l_g1c.getText());
                session.update(inv);
                session.getTransaction().commit();
                buscaDato(jComboBox1.getSelectedIndex());
            }
            else
            {
                JOptionPane.showMessageDialog(null, "¡El registro no se encontró!");
            }
        }
        catch(Exception e)
        {
            session.getTransaction().rollback();
            System.out.println(e);
            buscaDato(jComboBox1.getSelectedIndex());
            JOptionPane.showMessageDialog(null, "¡El registro no existe!");
        }
        if(session!=null)
            if(session.isOpen()==true)
                session.close();
    }//GEN-LAST:event_l_g1cFocusLost

    private void lKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_lKeyTyped
        char car = evt.getKeyChar();
        evt.setKeyChar(Character.toUpperCase(evt.getKeyChar()));
        if(l.getText().length()>=45)
        evt.consume();
    }//GEN-LAST:event_lKeyTyped

    private void l1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_l1KeyTyped
        char car = evt.getKeyChar();
        evt.setKeyChar(Character.toUpperCase(evt.getKeyChar()));
        if(l1.getText().length()>=45)
        evt.consume();
    }//GEN-LAST:event_l1KeyTyped

    private void l_g1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_l_g1ActionPerformed
        l.requestFocus();
    }//GEN-LAST:event_l_g1ActionPerformed

    private void l_g2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_l_g2ActionPerformed
        l.requestFocus();
    }//GEN-LAST:event_l_g2ActionPerformed

    private void l_g3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_l_g3ActionPerformed
        l.requestFocus();
    }//GEN-LAST:event_l_g3ActionPerformed

    private void l_g4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_l_g4ActionPerformed
        l.requestFocus();
    }//GEN-LAST:event_l_g4ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        Session session = HibernateUtil.getSessionFactory().openSession();
        try
        {
            Inventario inv=(Inventario)session.createCriteria(Inventario.class).add(Restrictions.eq("orden.idOrden", Integer.parseInt(orden))).add(Restrictions.eq("conceptos.idConcepto",1)).setMaxResults(1).uniqueResult();
            if(inv!=null)
            {
                session.beginTransaction();
                inv.setObservacion(l.getText());
                session.update(inv);
                session.getTransaction().commit();
                buscaDato(jComboBox1.getSelectedIndex());
            }
            else
            {
                JOptionPane.showMessageDialog(null, "¡El registro no se encontró!");
            }
        }
        catch(Exception e)
        {
            session.getTransaction().rollback();
            System.out.println(e);
            buscaDato(jComboBox1.getSelectedIndex());
            JOptionPane.showMessageDialog(null, "¡El registro no existe!");
        }
        if(session!=null)
            if(session.isOpen()==true)
            session.close();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        Session session = HibernateUtil.getSessionFactory().openSession();
        try
        {
            Inventario inv=(Inventario)session.createCriteria(Inventario.class).add(Restrictions.eq("orden.idOrden", Integer.parseInt(orden))).add(Restrictions.eq("conceptos.idConcepto",15)).setMaxResults(1).uniqueResult();
            if(inv!=null)
            {
                session.beginTransaction();
                inv.setObservacion(l1.getText());
                session.update(inv);
                session.getTransaction().commit();
                buscaDato(jComboBox1.getSelectedIndex());
            }
            else
            {
                JOptionPane.showMessageDialog(null, "¡El registro no se encontró!");
            }
        }
        catch(Exception e)
        {
            session.getTransaction().rollback();
            System.out.println(e);
            buscaDato(jComboBox1.getSelectedIndex());
            JOptionPane.showMessageDialog(null, "¡El registro no existe!");
        }
        if(session!=null)
            if(session.isOpen()==true)
                session.close();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void l_g1cActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_l_g1cActionPerformed
        // TODO add your handling code here:
        l1.requestFocus();
    }//GEN-LAST:event_l_g1cActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel Caja;
    private javax.swing.JPanel Tracto;
    private javax.swing.JCheckBox cinco1;
    private javax.swing.JCheckBox cinco2;
    private javax.swing.JCheckBox cuatro1;
    private javax.swing.JCheckBox cuatro2;
    private javax.swing.JCheckBox diez1;
    private javax.swing.JCheckBox dos1;
    private javax.swing.JCheckBox dos2;
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
    private javax.swing.JTextArea l;
    private javax.swing.JTextArea l1;
    private javax.swing.JTextField l_g1;
    private javax.swing.JTextField l_g1c;
    private javax.swing.JTextField l_g2;
    private javax.swing.JTextField l_g3;
    private javax.swing.JTextField l_g4;
    private javax.swing.JLabel listado;
    private javax.swing.JCheckBox nueve1;
    private javax.swing.JTextField observacion;
    private javax.swing.JCheckBox ocho1;
    private javax.swing.JCheckBox ocho2;
    private javax.swing.JCheckBox seis1;
    private javax.swing.JCheckBox seis2;
    private javax.swing.JCheckBox siete1;
    private javax.swing.JCheckBox siete2;
    private javax.swing.JTable t_datos2;
    private javax.swing.JCheckBox tres1;
    private javax.swing.JCheckBox tres2;
    private javax.swing.JLabel unidad;
    private javax.swing.JCheckBox uno1;
    private javax.swing.JCheckBox uno2;
    // End of variables declaration//GEN-END:variables
    public void visualiza(Boolean valor)
    {
        this.uno1.setEnabled(valor);
        this.dos1.setEnabled(valor);
        this.tres1.setEnabled(valor);
        this.cuatro1.setEnabled(valor);
        this.cinco1.setEnabled(valor);
        this.seis1.setEnabled(valor);
        this.siete1.setEnabled(valor);
        this.ocho1.setEnabled(valor);
        this.nueve1.setEnabled(valor);
        this.diez1.setEnabled(valor);
        this.l.setEnabled(valor);
        this.l_g1.setEnabled(valor);
        this.l_g2.setEnabled(valor);
        this.l_g3.setEnabled(valor);
        this.l_g4.setEnabled(valor);
        this.jButton2.setEnabled(valor);
        this.t_datos2.setEnabled(valor);
        
        this.uno2.setEnabled(valor);
        this.dos2.setEnabled(valor);
        this.tres2.setEnabled(valor);
        this.cuatro2.setEnabled(valor);
        this.cinco2.setEnabled(valor);
        this.seis2.setEnabled(valor);
        this.siete2.setEnabled(valor);
        this.ocho2.setEnabled(valor);
        this.l1.setEnabled(valor);
        this.l_g1c.setEnabled(valor);
        this.jButton3.setEnabled(valor);
        this.jButton1.setEnabled(valor);
    }
    
    private void buscaDato(int op)
    {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try
        {
            Inventario[] inv=null;
            int valor=0;
            if(op==0)
            {
                Query query = session.createQuery("SELECT inv FROM Inventario inv where orden.idOrden="+orden+ "AND conceptos.vehiculo="+1);
                inv = (Inventario[])query.list().toArray(new Inventario[0]);
                if((inv.length-14)>=0)
                {
                    if(inv[0].getConceptos().getNombre().compareTo("LLANTAS1T")==0)
                    {
                        if (inv[0].isContiene()==false)
                            uno1.setSelected(false);
                        else
                            uno1.setSelected(true);

                        try{
                            if (inv[0].getObservacion().compareTo("")==0)
                                l.setText(null);
                            else
                                l.setText(inv[0].getObservacion());
                        }
                        catch(Exception e){
                        }
                    }

                    if(inv[1].getConceptos().getNombre().compareTo("LLANTAS2T")==0)
                    {
                        if (inv[1].isContiene()==false)
                            dos1.setSelected(false);
                        else
                            dos1.setSelected(true);
                    }
                    if(inv[2].getConceptos().getNombre().compareTo("LLANTAS3T")==0)
                    {
                        if (inv[2].isContiene()==false)
                            tres1.setSelected(false);
                        else
                            tres1.setSelected(true);
                    }
                    if(inv[3].getConceptos().getNombre().compareTo("LLANTAS4T")==0)
                    {
                        if (inv[3].isContiene()==false)
                            cuatro1.setSelected(false);
                        else
                            cuatro1.setSelected(true);
                    }
                    if(inv[4].getConceptos().getNombre().compareTo("LLANTAS5T")==0)
                    {
                        if (inv[4].isContiene()==false)
                            cinco1.setSelected(false);
                        else
                            cinco1.setSelected(true);
                    }
                    if(inv[5].getConceptos().getNombre().compareTo("LLANTAS6T")==0)
                    {
                        if (inv[5].isContiene()==false)
                            seis1.setSelected(false);
                        else
                            seis1.setSelected(true);
                    }
                    if(inv[6].getConceptos().getNombre().compareTo("LLANTAS7T")==0)
                    {
                        if (inv[6].isContiene()==false)
                            siete1.setSelected(false);
                        else
                            siete1.setSelected(true);
                    }
                    if(inv[7].getConceptos().getNombre().compareTo("LLANTAS8T")==0)
                    {
                        if (inv[7].isContiene()==false)
                            ocho1.setSelected(false);
                        else
                            ocho1.setSelected(true);
                    }

                    if(inv[8].getConceptos().getNombre().compareTo("LLANTAS9T")==0)
                    {
                        if (inv[8].isContiene()==false)
                            nueve1.setSelected(false);
                        else
                            nueve1.setSelected(true);
                    }
                    if(inv[9].getConceptos().getNombre().compareTo("LLANTAS10T")==0)
                   {
                        if (inv[9].isContiene()==false)
                            diez1.setSelected(false);
                        else
                            diez1.setSelected(true);
                    }
                    if(inv[10].getConceptos().getNombre().compareTo("GAS1T")==0)
                    {
                        try{
                            if(inv[10].getObservacion().compareTo("")==0)
                                l_g1.setText(null);
                            else
                                l_g1.setText(inv[10].getObservacion());
                        }
                        catch(Exception e){
                        }
                    }
                    if(inv[11].getConceptos().getNombre().compareTo("GAS2T")==0)
                    {
                        try{
                            if(inv[11].getObservacion().compareTo("")==0)
                                l_g2.setText(null);
                            else
                                l_g2.setText(inv[11].getObservacion());
                        }
                        catch(Exception e){
                        }
                    }
                    if(inv[12].getConceptos().getNombre().compareTo("GAS3T")==0)
                    {
                        try{
                            if(inv[12].getObservacion().compareTo("")==0)
                                l_g3.setText(null);
                            else
                                l_g3.setText(inv[12].getObservacion());
                        }
                        catch(Exception e){
                        }
                    }
                    if(inv[13].getConceptos().getNombre().compareTo("GAS4T")==0)
                    {
                        try{
                            if(inv[13].getObservacion().compareTo("")==0)
                                l_g4.setText(null);
                            else
                                l_g4.setText(inv[13].getObservacion());
                        }
                        catch(Exception e){
                        }
                    }
                    t_datos2.setModel(ModeloTablaReporte(inv.length-14, columnas));
                    valor=14;
                }
                else
                    t_datos2.setModel(ModeloTablaReporte(0, columnas));
            }
            if(op==1)
            {
                Query query = session.createQuery("SELECT inv FROM Inventario inv where orden.idOrden="+orden+ "AND conceptos.vehiculo="+2);
                inv = (Inventario[])query.list().toArray(new Inventario[0]);
                if((inv.length-9)>=0)
                {
                    if(inv[0].getConceptos().getNombre().compareTo("LLANTAS1C")==0)
                    {
                        if (inv[0].isContiene()==false)
                            uno2.setSelected(false);
                        else
                            uno2.setSelected(true);
                    }
                    if(inv[0].getConceptos().getNombre().compareTo("LLANTAS1C")==0)
                    {
                        try{
                            if (inv[0].getObservacion().compareTo("")==0)
                                l1.setText(null);
                            else
                                l1.setText(inv[0].getObservacion());
                        }
                        catch(Exception e){
                        }
                    }
                    if(inv[1].getConceptos().getNombre().compareTo("LLANTAS2C")==0)
                    {
                        if (inv[1].isContiene()==false)
                            dos2.setSelected(false);
                        else
                            dos2.setSelected(true);
                    }
                    if(inv[2].getConceptos().getNombre().compareTo("LLANTAS3C")==0)
                    {
                        if (inv[2].isContiene()==false)
                            tres2.setSelected(false);
                        else
                            tres2.setSelected(true);
                    }
                    if(inv[3].getConceptos().getNombre().compareTo("LLANTAS4C")==0)
                    {
                        if (inv[3].isContiene()==false)
                            cuatro2.setSelected(false);
                        else
                            cuatro2.setSelected(true);
                    }
                    if(inv[4].getConceptos().getNombre().compareTo("LLANTAS5C")==0)
                    {
                        if (inv[4].isContiene()==false)
                            cinco2.setSelected(false);
                        else
                            cinco2.setSelected(true);
                    }
                    if(inv[5].getConceptos().getNombre().compareTo("LLANTAS6C")==0)
                    {
                        if (inv[5].isContiene()==false)
                            seis2.setSelected(false);
                        else
                            seis2.setSelected(true);
                    }
                    if(inv[6].getConceptos().getNombre().compareTo("LLANTAS7C")==0)
                    {
                        if (inv[6].isContiene()==false)
                            siete2.setSelected(false);
                        else
                            siete2.setSelected(true);
                    }
                    if(inv[7].getConceptos().getNombre().compareTo("LLANTAS8C")==0)
                    {
                        if (inv[7].isContiene()==false)
                            ocho2.setSelected(false);
                        else
                            ocho2.setSelected(true);
                    }
                    if(inv[8].getConceptos().getNombre().compareTo("GAS1C")==0)
                    {
                        try{
                            if(inv[8].getObservacion().compareTo("")==0)
                                l_g1c.setText(null);
                            else
                                l_g1c.setText(inv[8].getObservacion());
                            }
                        catch(Exception e){
                        }
                    }
                    t_datos2.setModel(ModeloTablaReporte(inv.length-9, columnas));
                    valor=9;
                }
                else
                    t_datos2.setModel(ModeloTablaReporte(0, columnas));
            }
            if(op==-1)
            {
                t_datos2.setModel(ModeloTablaReporte(0, columnas));
            }
            formatoTabla();
            if(inv!=null)
            {
                for(int i=valor; i<inv.length; i++)
                {
                    model.setValueAt(inv[i].getIdInventario(), i-valor, 0);
                    model.setValueAt(inv[i].getConceptos().getNombre(), i-valor, 1);
                    model.setValueAt(inv[i].isContiene(), i-valor, 2);
                    if(inv[i].getObservacion()!=null)
                        model.setValueAt(inv[i].getObservacion(), i-valor, 3);
                    else
                        model.setValueAt("", i-valor, 3);    
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
    public void formatoTabla()
    {
        Color c1 = new java.awt.Color(2, 135, 242);   
        for(int x=0; x<t_datos2.getColumnModel().getColumnCount(); x++)
        {
            t_datos2.getColumnModel().getColumn(x).setHeaderRenderer(new Render1(c1));
        } 
        tabla_tamaños();
        t_datos2.setShowVerticalLines(true);
        t_datos2.setShowHorizontalLines(true);
    }
    public void tabla_tamaños()
    {
        TableColumnModel col_model = t_datos2.getColumnModel();
        for (int i=0; i<t_datos2.getColumnCount(); i++)
        {
            TableColumn column = col_model.getColumn(i);
            switch(i)
            {
                case 0:                       
                    column.setPreferredWidth(20);
                    break;
                case 1:                       
                    column.setPreferredWidth(200);
                    break;
                case 2:               
                    column.setPreferredWidth(20);
                    break;                 
                case 3:
                    column.setPreferredWidth(150);
                    column.setCellEditor(new DefaultCellEditor(observacion));
                    break;
                default:
                    column.setPreferredWidth(40);
                    break; 
            }
        } 
    }    
}