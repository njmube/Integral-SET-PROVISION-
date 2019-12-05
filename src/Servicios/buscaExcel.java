package Servicios;

import Hibernate.entidades.Acceso;
import Hibernate.entidades.Configuracion;

import Hibernate.Util.HibernateUtil;
import Hibernate.entidades.CotizacionCliente;
import Hibernate.entidades.Orden;
import Hibernate.entidades.Usuario;
import Integral.ExtensionFileFilter;
import Integral.Herramientas;
import Integral.Render1;
import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

public class buscaExcel extends javax.swing.JDialog {

    Workbook libro1 = null;
    private int r = 0;
    DefaultTableModel model;
    String[] columnas = new String[]{"Autorizado", "Codigo", "Descripcion", "OP", "Cantidad", "Medida", "$ Aut. c/u", "$ Aut. Tot", "ID_COTIZACION"};
    Herramientas h;
    private Usuario user = null;
    String sessionPrograma = "";
    public Orden orden_act = null;
    private static javax.swing.JCheckBox ok;
    public static final String[] DATA = {"R", "C"};
    public static final String[] MEDIDA = {"PZAS", "GAL", "LTS", "MTS", "KGS", "KIT","HRA","NA"};
    JComboBox comboBox = new JComboBox(DATA);
    JComboBox medida = new JComboBox(MEDIDA);
    boolean respuesta = false;
    String valida = "";
    String mensaje = "";
    boolean check;
    boolean[] canEdit = new boolean[]{ check, true, true, true, true, true, true, false, false};
    public buscaExcel(java.awt.Frame parent, boolean modal, Usuario user, String ses, Orden id) {
        super(parent, modal);
        user = user;
        sessionPrograma = ses;
        orden_act = id;
        initComponents();
        getRootPane().setDefaultButton(jButton1);
        jTable1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jTable1.setModel(ModeloTablaReporte(0, columnas));
        formatoTabla();
        anadeListenerAlModelo(jTable1);
        Carga_Datos();
        jTable1.getColumnModel().getColumn(8).setMaxWidth(0);
        jTable1.getColumnModel().getColumn(8).setMinWidth(0);
        jTable1.getTableHeader().getColumnModel().getColumn(8).setMaxWidth(0);
        jTable1.getTableHeader().getColumnModel().getColumn(8).setMinWidth(0);
    }

    /**
     * Se añade el listener al modelo y se llama a actualizaSumas(), que es el
     * método encargado de actualizar las sumas de las celdas no editables.
     */
    private void anadeListenerAlModelo(JTable tabla) {
        tabla.getModel().addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent evento) {
                actualizaSumas(evento);
            }
        });
    }

    /**
     * Cuando cambie el valor de alguna celda, el JTable avisará a los listener
     * y nuestro listener llamará a este método.
     */
    protected void actualizaSumas(TableModelEvent evento) {
        // Solo se trata el evento UPDATE, correspondiente al cambio de valor
        // de una celda.
        if (evento.getType() == TableModelEvent.UPDATE) {
            // Se obtiene el modelo de la tabla y la fila/columna que han cambiado.
            TableModel modelo = ((TableModel) (evento.getSource()));
            int fila = evento.getFirstRow();
            int columna = evento.getColumn();
            if (columna == 4 || columna == 6) {
                try {
                    String valorPrimeraColumna = String.valueOf(modelo.getValueAt(fila, 4));
                    String valorSegundaColumna = String.valueOf(modelo.getValueAt(fila, 6));
                    modelo.setValueAt(Double.valueOf(valorPrimeraColumna) * Double.valueOf(valorSegundaColumna), fila, 7);
                } catch (NullPointerException e) {
                    // La celda que ha cambiado esta vacia.
                }
            }
        }
    }

    DefaultTableModel ModeloTablaReporte(int renglones, String columnas[]) {
        model = new DefaultTableModel(new Object[renglones][11], columnas) {
            Class[] types = new Class[]{
                java.lang.Boolean.class,
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class
            };
            

            public void setValueAt(Object value, int row, int col) {
                Vector vector = (Vector) this.dataVector.elementAt(row);
                Object celda = ((Vector) this.dataVector.elementAt(row)).elementAt(col);
                switch (col) {
                    case 0:
                        vector.setElementAt(value, col);
                        this.dataVector.setElementAt(vector, row);
                        fireTableCellUpdated(row, col);
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

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        t_busca = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jButton4 = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(90, 66, 126), 1, true), "Carga Excel", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Arial", 1, 12))); // NOI18N

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(90, 66, 126), 1, true), "Busca excel", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Arial", 1, 11))); // NOI18N

        t_busca.setEditable(false);
        t_busca.setBackground(new java.awt.Color(204, 255, 255));
        t_busca.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                t_buscaActionPerformed(evt);
            }
        });
        t_busca.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                t_buscaKeyReleased(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel1.setText("Nombre del archivo:");

        jButton4.setText("Buscar");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
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
                .addComponent(t_busca, javax.swing.GroupLayout.PREFERRED_SIZE, 377, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12)
                .addComponent(jButton4)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(t_busca, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1)
                    .addComponent(jButton4))
                .addGap(0, 17, Short.MAX_VALUE))
        );

        jButton1.setBackground(new java.awt.Color(2, 135, 242));
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText("Guardar");
        jButton1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jTable1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTable1FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTable1FocusLost(evt);
            }
        });
        jTable1.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jTable1PropertyChange(evt);
            }
        });
        jTable1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTable1KeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);

        jButton2.setBackground(new java.awt.Color(2, 135, 242));
        jButton2.setIcon(new javax.swing.ImageIcon("C:\\Users\\Angel\\Documents\\Proyectos\\Integral-SET-PROVISION-\\imagenes\\boton_mas.png")); // NOI18N
        jButton2.setToolTipText("Agrega un nuevo elemento");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setBackground(new java.awt.Color(2, 135, 242));
        jButton3.setIcon(new javax.swing.ImageIcon("C:\\Users\\Angel\\Documents\\Proyectos\\Integral-SET-PROVISION-\\imagenes\\boton_menos.png")); // NOI18N
        jButton3.setToolTipText("Elimina un elemento");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 795, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton1))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 294, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2)
                    .addComponent(jButton3))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void t_buscaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_t_buscaActionPerformed

    }//GEN-LAST:event_t_buscaActionPerformed

    private void t_buscaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_buscaKeyReleased

    }//GEN-LAST:event_t_buscaKeyReleased

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        if (t_busca.getText().compareTo("") != 0 || jTable1.getRowCount() != 0) {
            CotizacionCliente cot_cliente = new CotizacionCliente();
            Orden orden = new Orden();
            orden.setIdOrden(orden_act.getIdOrden());
            valida = valida();
            if (valida == "") {
                for (int ren = 0; ren <= jTable1.getRowCount(); ren++) {
                    try {
                        String val = jTable1.getValueAt(ren, 8).toString();
                        if (val.compareTo("") != 0) {
                            DecimalFormat formatea = new DecimalFormat("######.##");
                            cot_cliente.setOrden(orden);
                            cot_cliente.setDescripcion(jTable1.getValueAt(ren, 2).toString().toUpperCase());
                            cot_cliente.setCodigo(jTable1.getValueAt(ren, 1).toString().toUpperCase());
                            cot_cliente.setOp(jTable1.getValueAt(ren, 3).toString());
                            cot_cliente.setCantidad(Double.valueOf(jTable1.getValueAt(ren, 4).toString()));
                            cot_cliente.setMedida(jTable1.getValueAt(ren, 5).toString());
                            double costo = Double.valueOf(jTable1.getValueAt(ren, 4).toString()) * Double.valueOf(jTable1.getValueAt(ren, 6).toString());
                            String cos = formatea.format(costo);
                            cot_cliente.setCosto(Double.valueOf(cos));
                            cot_cliente.setAutorizado(0);
                            cot_cliente.setIdCotizacionCliente(Integer.valueOf(jTable1.getValueAt(ren, 8).toString()));
                            respuesta = actualizaDatosExcel(cot_cliente);

                        } else {
                            DecimalFormat formatea = new DecimalFormat("######.##");
                            cot_cliente.setOrden(orden);
                            cot_cliente.setDescripcion(jTable1.getValueAt(ren, 2).toString().toUpperCase());
                            cot_cliente.setCodigo(jTable1.getValueAt(ren, 1).toString().toUpperCase());
                            cot_cliente.setOp(jTable1.getValueAt(ren, 3).toString());
                            cot_cliente.setCantidad(Double.valueOf(jTable1.getValueAt(ren, 4).toString()));
                            cot_cliente.setMedida(jTable1.getValueAt(ren, 5).toString());
                            double costo = Double.valueOf(jTable1.getValueAt(ren, 4).toString()) * Double.valueOf(jTable1.getValueAt(ren, 6).toString());
                            String cos = formatea.format(costo);
                            cot_cliente.setCosto(Double.valueOf(cos));
                            cot_cliente.setAutorizado(0);
                            respuesta = guardarDatosExcel(cot_cliente);
                        }
                    } catch (Exception e) {
                        System.out.println("Error: " + e.toString());
                    }
                }
                if (respuesta == false) {
                    JOptionPane.showMessageDialog(null, "Error al guardar los datos");
                } else {
                    JOptionPane.showMessageDialog(null, "Registros almacenados correctamente");
                    t_busca.setText("");
                    this.dispose();
                    /*Nombre.requestFocus();*/
                }
            } else {
                JOptionPane.showMessageDialog(this, mensaje);
            }

        } else {
            JOptionPane.showMessageDialog(this, "Debes selecciomar un archivo o agregar registros", "Informacion", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        int valor = jTable1.getRowCount();
        javax.swing.JFileChooser jF1 = new javax.swing.JFileChooser();
        jF1.setFileFilter(new ExtensionFileFilter("Excel document (*.xls)", new String[]{"xls"}));

        String ruta = null;
        if (jF1.showOpenDialog(this) == jF1.APPROVE_OPTION) {
            ruta = jF1.getSelectedFile().getAbsolutePath();
            if (ruta != null) {
                File archivo1XLS = new File(ruta);
                try {
                    t_busca.setText(archivo1XLS.getName().toString());
                    FileInputStream archivo = new FileInputStream(archivo1XLS);
                    POIFSFileSystem fsFileSystem = new POIFSFileSystem(archivo);
                    libro1 = new HSSFWorkbook(fsFileSystem);
                    Sheet hoja = libro1.getSheetAt(0);
                    Iterator<Row> rowIter = hoja.rowIterator();
                    model.setRowCount(valor);
                    r=0;
                    while (rowIter.hasNext()) {
                        int i = 0;
                        HSSFRow myRow = (HSSFRow) rowIter.next();
                        if (r > 0) {
                            DefaultCellEditor defaultCellEditor = new DefaultCellEditor(comboBox);
                            jTable1.getColumnModel().getColumn(3).setCellEditor(defaultCellEditor);

                            DefaultCellEditor defaultCellEditor1 = new DefaultCellEditor(medida);
                            jTable1.getColumnModel().getColumn(5).setCellEditor(defaultCellEditor1);
                            DecimalFormat formatea = new DecimalFormat("######.##");
                            Object[] renglon = new Object[10];
                            renglon[0] = false;
                            renglon[1] = myRow.getCell(1).getStringCellValue();
                            renglon[2] = myRow.getCell(0).getStringCellValue();
                            renglon[3] = myRow.getCell(2).getStringCellValue();
                            renglon[4] = formatea.format(Double.valueOf(myRow.getCell(3).getStringCellValue()));
                            renglon[5] = myRow.getCell(4).getStringCellValue();
                            renglon[6] = formatea.format(Double.valueOf(myRow.getCell(5).getStringCellValue()));
                            renglon[7] = Double.valueOf(myRow.getCell(3).getStringCellValue()) * Double.valueOf(myRow.getCell(5).getStringCellValue());
                            renglon[8] = "";
                            model.addRow(renglon);
                        } else {
                            r = 1;
                        }
                    }
                } catch (Exception e) {
                    System.out.println(e);
                    e.printStackTrace();

                    libro1 = null;
                    JOptionPane.showMessageDialog(this, "No se pudo abrir el archivo");
                }
            }
        } else {
            libro1 = null;
        }
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed

        DefaultCellEditor defaultCellEditor = new DefaultCellEditor(comboBox);
        jTable1.getColumnModel().getColumn(3).setCellEditor(defaultCellEditor);

        DefaultCellEditor defaultCellEditor1 = new DefaultCellEditor(medida);
        jTable1.getColumnModel().getColumn(5).setCellEditor(defaultCellEditor1);

        Object[] renglon = new Object[10];
        renglon[0] = false;
        renglon[1] = "";
        renglon[2] = "";
        renglon[3] = "R";
        renglon[4] = "1.0";
        renglon[5] = "PZAS";
        renglon[6] = "0.00";
        renglon[7] = "0.00";
        renglon[8] = "";

        model.addRow(renglon);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        if (jTable1.getSelectedRow() == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona una fila", "Informacion", JOptionPane.INFORMATION_MESSAGE);
        } else {
            String val = jTable1.getValueAt(jTable1.getSelectedRow(), 8).toString();
            if (val != "") {
                int opt = JOptionPane.showConfirmDialog(this, "¡La fila se eliminara!");
                if (JOptionPane.YES_OPTION == opt) {

                    Session session = HibernateUtil.getSessionFactory().openSession();
                    try {
                        session.beginTransaction().begin();
                        Hibernate.entidades.CotizacionCliente resp = (Hibernate.entidades.CotizacionCliente) session.get(Hibernate.entidades.CotizacionCliente.class, Integer.parseInt(jTable1.getValueAt(jTable1.getSelectedRow(), 8).toString()));
                        session.delete(resp);
                        session.beginTransaction().commit();
                        DefaultTableModel temp = (DefaultTableModel) jTable1.getModel();
                        temp.removeRow(jTable1.getSelectedRow());
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        if (session.isOpen()) {
                            session.close();
                        }
                    }
                }
            } else {
                int opt1 = JOptionPane.showConfirmDialog(this, "¡La fila se eliminara!");
                if (JOptionPane.YES_OPTION == opt1) {
                    DefaultTableModel temp = (DefaultTableModel) jTable1.getModel();
                    temp.removeRow(jTable1.getSelectedRow());
                }
            }
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jTable1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTable1FocusGained
        int columna = jTable1.getSelectedColumn();
        int renglon = jTable1.getSelectedRow();
        switch (columna) {
            case 0:
                break;
            case 1:
                break;
            default:
                break;
        }
    }//GEN-LAST:event_jTable1FocusGained

    private void jTable1FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTable1FocusLost

    }//GEN-LAST:event_jTable1FocusLost

    private void jTable1PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jTable1PropertyChange

    }//GEN-LAST:event_jTable1PropertyChange

    private void jTable1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTable1KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTable1KeyPressed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    public javax.swing.JTextField t_busca;
    // End of variables declaration//GEN-END:variables

    public void tabla_tamaños() {
        TableColumnModel col_model = jTable1.getColumnModel();

        for (int i = 0; i < jTable1.getColumnCount(); i++) {
            TableColumn column = col_model.getColumn(i);
            switch (i) {
                case 0:
                    column.setPreferredWidth(100);
                    break;
                case 1:
                    column.setPreferredWidth(100);
                    break;
                case 2:
                    column.setPreferredWidth(400);
                    break;
                case 3:
                    column.setPreferredWidth(100);
                    break;
                case 4:
                    column.setPreferredWidth(100);
                    break;
                case 5:
                    column.setPreferredWidth(100);
                    break;
                case 6:
                    column.setPreferredWidth(100);
                    break;
                case 7:
                    column.setPreferredWidth(100);
                    break;
                case 8:
                    column.setPreferredWidth(0);
                    break;
                default:
                    column.setPreferredWidth(40);
                    break;

            }
        }
        JTableHeader header = jTable1.getTableHeader();
        header.setForeground(Color.white);
    }

    public void formatoTabla() {
        Color c1 = new java.awt.Color(2, 135, 242);
        for (int x = 0; x < jTable1.getColumnModel().getColumnCount(); x++) {
            jTable1.getColumnModel().getColumn(x).setHeaderRenderer(new Render1(c1));
        }
        tabla_tamaños();
        jTable1.setShowVerticalLines(true);
        jTable1.setShowHorizontalLines(true);
    }

    private boolean guardarDatosExcel(CotizacionCliente obj) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            session.beginTransaction();
            //IdOrden=(String) session.save(obj);
            session.save(obj);
            session.getTransaction().commit();
            return true;
        } catch (HibernateException he) {
            he.printStackTrace();
            session.getTransaction().rollback();
            return false;
        } finally {
            session.close();
            return true;
        }
    }

    private boolean actualizaDatosExcel(CotizacionCliente obj) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            session.beginTransaction();
            Query q = session.createQuery("update CotizacionCliente set descripcion = '" + obj.getDescripcion() + "'  where  id_cotizacion_cliente = " + obj.getIdCotizacionCliente() + "");
            q.executeUpdate();
            session.getTransaction().commit();
            return true;
        } catch (HibernateException he) {
            he.printStackTrace();
            System.out.println(he.hashCode());
            session.getTransaction().rollback();
            return false;
        } finally {
            if (session.isOpen()) {
                session.close();
            }
        }
    }

    private String valida() {
        for (int ren = 0; ren < jTable1.getRowCount(); ren++) {
            if (jTable1.getValueAt(ren, 2).toString() != "") {
                if (jTable1.getValueAt(ren, 1).toString() != "") {
                    mensaje = "";
                } else {
                    mensaje = "Completa los siguientes campos codigo";
                }
            } else {
                mensaje = "Completa los siguientes campos descripcion";
            }
        }
        return mensaje;
    }

    private void Carga_Datos() {
        String consulta = "from CotizacionCliente where id_orden = " + orden_act.getIdOrden();
        List<Object[]> resultList = executeHQLQuery(consulta);
        if (resultList.size() > 0) {
            int i = 0;
            for (Object o : resultList) {
                CotizacionCliente actor = (CotizacionCliente) o;

                DefaultCellEditor defaultCellEditor = new DefaultCellEditor(comboBox);
                jTable1.getColumnModel().getColumn(3).setCellEditor(defaultCellEditor);

                DefaultCellEditor defaultCellEditor1 = new DefaultCellEditor(medida);
                jTable1.getColumnModel().getColumn(5).setCellEditor(defaultCellEditor1);
                boolean autorizado;
                if (actor.getAutorizado() == 1) {
                    check = true;
                    autorizado = true;
                } else {
                    check = false;
                    autorizado = false;
                }
                Object[] renglon = new Object[10];
                renglon[0] = autorizado;
                renglon[1] = actor.getCodigo();
                renglon[2] = actor.getDescripcion();
                renglon[3] = actor.getOp();
                renglon[4] = actor.getCantidad();
                renglon[5] = actor.getMedida();
                renglon[6] = actor.getCosto() / actor.getCantidad();
                renglon[7] = actor.getCosto();
                renglon[8] = actor.getIdCotizacionCliente();

                model.addRow(renglon);
                model.isCellEditable(0, 0);
                i++;
            }
        } else {
        }
    }

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
            List lista = null;
            return lista;
        } finally {
            if (session.isOpen()) {
                session.close();
            }
        }
    }
}
