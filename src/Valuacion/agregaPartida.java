/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * acceso.java
 *
 * Created on 19/01/2012, 02:01:25 PM
 */
package Valuacion;

import Integral.FormatoTabla;
import Integral.Render1;
import Catalogo.altaCatalogo;
import Especialidad.altaEspecialidad;
import Hibernate.Util.HibernateUtil;
import Hibernate.entidades.Catalogo;
import Hibernate.entidades.Ejemplar;
import Hibernate.entidades.Especialidad;
import Hibernate.entidades.Partida;
import Hibernate.entidades.Posicion;
import Hibernate.entidades.Usuario;
import Integral.HorizontalBarUI;
import Integral.ListaEtiqueta;
import Integral.ListaObjeto;
import Integral.VerticalBarUI;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import javax.swing.DefaultCellEditor;
import javax.swing.DefaultListModel;
import javax.swing.InputMap;
import javax.swing.JOptionPane;
import javax.swing.ImageIcon;
import javax.swing.JScrollBar;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
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
 * @author ISC_SALVADOR
 */
public class agregaPartida extends javax.swing.JDialog {

    public static final ArrayList RET_CANCEL =null;
    InputMap map = new InputMap();
    DefaultTableModel model;
    String[] columnas = new String [] {"id","Descripcion", "Grupo Mecanico", "NoParte"};
    ArrayList ubicacionesId;
    String orden;
    FormatoTabla formato;
    Usuario user;
    String sessionPrograma;
    String plantilla="";
    int inicio=0;
    boolean parar=false;
    final JScrollBar scrollBar;
    
    /** Creates new form acceso */
    public agregaPartida(java.awt.Frame parent, boolean modal, String ord, Usuario u, String ses, String plantilla) {
        super(parent, modal);
        initComponents();
        scrollBar = scroll.getVerticalScrollBar();
        scrollBar.addAdjustmentListener(new AdjustmentListener() {
            public void adjustmentValueChanged(AdjustmentEvent ae) {
                if(parar==false)
                {
                    int extent = scrollBar.getModel().getExtent();
                    extent+=scrollBar.getValue();
                    if(extent==scrollBar.getMaximum())
                        buscaDato();
                }
              //System.out.println("actual: " + extent+" maximo:"+scrollBar.getMaximum());
            }
          });
        getRootPane().setDefaultButton(jButton1);
        this.plantilla=plantilla;
        user=u;
        sessionPrograma=ses;
        formato = new FormatoTabla();
        orden=ord;
        t_datos.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        listaGrupos();
        model=ModeloTablaReporte(0,columnas);
        t_datos.setModel(model);
        buscaDato();
        formatoTabla();
        scroll.getVerticalScrollBar().setUI(new VerticalBarUI());
        scroll.getHorizontalScrollBar().setUI(new HorizontalBarUI());
        if(plantilla.compareTo("PLANTILLAS")!=0)
        {
            l_plantilla.setText("Plantilla:"+plantilla);
            ubicacion.setEnabled(true);
        }
        else
        {
            l_plantilla.setText("Plantilla:Sin Plantilla");
            ubicacion.setEnabled(false);
        }
    }
    
    DefaultTableModel ModeloTablaReporte(int renglones, String columnas[])
        {
            model = new DefaultTableModel(new Object [renglones][4], columnas)
            {
                Class[] types = new Class [] {
                    java.lang.String.class, 
                    java.lang.String.class, 
                    java.lang.String.class,
                    java.lang.String.class
                };
                boolean[] canEdit = new boolean [] {
                    false, false, false, true
                };

                public void setValueAt(Object value, int row, int col)
                 {
                        Vector vector = (Vector)this.dataVector.elementAt(row);
                        Object celda = ((Vector)this.dataVector.elementAt(row)).elementAt(col);
                        switch(col)
                        {
                            case 3:
                                    if(vector.get(col)==null)
                                    {
                                        vector.setElementAt(value, col);
                                        this.dataVector.setElementAt(vector, row);
                                        fireTableCellUpdated(row, col);
                                    }
                                    else
                                    {
                                        if(value!=null)
                                        {
                                            switch(value.toString())
                                            {
                                                case "Eliminar":
                                                    vector.setElementAt("BUSCAR", col);
                                                    this.dataVector.setElementAt(vector, row);
                                                    fireTableCellUpdated(row, col);
                                                    break;
                                                case "Cancelar":
                                                    break;
                                                default:
                                                        vector.setElementAt(value, col);
                                                        this.dataVector.setElementAt(vector, row);
                                                        fireTableCellUpdated(row, col);
                                                    break;
                                            }
                                        }
                                    }
                                    break;
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

    
    private void doClose(ArrayList o) {
        returnStatus = o;
        setVisible(false);
        dispose();
    }
    
    public ArrayList getReturnStatus() {
        return returnStatus;
    }
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        numeros = new javax.swing.JComboBox();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        t_busca = new javax.swing.JTextField();
        c_filtro = new javax.swing.JComboBox();
        jLabel1 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        scroll = new javax.swing.JScrollPane();
        t_datos = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        l_grupo = new javax.swing.JList();
        jLabel2 = new javax.swing.JLabel();
        ubicacion = new javax.swing.JComboBox();
        l_plantilla = new javax.swing.JLabel();

        numeros.setFont(new java.awt.Font("Dialog", 0, 9)); // NOI18N
        numeros.setForeground(new java.awt.Color(102, 102, 102));
        numeros.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Buscar", "Eliminar", "Cancelar", "Nuevo" }));

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Consulta de descripciones");
        setResizable(false);

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(90, 66, 126), 1, true), "Filtrar por:", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Arial", 1, 11))); // NOI18N

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

        c_filtro.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Descripcion", "id", "G. Mecanico", "N° Parte" }));
        c_filtro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                c_filtroActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel1.setText("contiene:");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(c_filtro, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(t_busca)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(t_busca, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(c_filtro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(90, 66, 126), 1, true), "Resultados", javax.swing.border.TitledBorder.RIGHT, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Arial", 1, 11))); // NOI18N

        t_datos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Id", "Descripcion", "Grupo Mecanico", "N° Parte"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        t_datos.setSelectionMode(javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        t_datos.getTableHeader().setReorderingAllowed(false);
        t_datos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                t_datosMouseClicked(evt);
            }
        });
        t_datos.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                t_datosKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                t_datosKeyReleased(evt);
            }
        });
        scroll.setViewportView(t_datos);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(scroll, javax.swing.GroupLayout.DEFAULT_SIZE, 478, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(scroll, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addContainerGap())
        );

        jButton1.setBackground(new java.awt.Color(2, 135, 242));
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setIcon(new ImageIcon("imagenes/seleccionar.png"));
        jButton1.setText("Seleccionar");
        jButton1.setToolTipText("Cargar registro seleccionado");
        jButton1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton3.setBackground(new java.awt.Color(2, 135, 242));
        jButton3.setForeground(new java.awt.Color(254, 254, 254));
        jButton3.setIcon(new ImageIcon("imagenes/nuevo.png"));
        jButton3.setMnemonic('c');
        jButton3.setText("Catalogo (ALT+c)");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "Grupo Mecanico", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 1, 11))); // NOI18N

        l_grupo.setForeground(new java.awt.Color(0, 51, 204));
        l_grupo.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        l_grupo.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                l_grupoValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(l_grupo);

        jLabel2.setText("Posición:");

        ubicacion.setFont(new java.awt.Font("Dialog", 0, 9)); // NOI18N
        ubicacion.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "NA", "IZQ", "DER" }));
        ubicacion.setEnabled(false);
        ubicacion.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                ubicacionItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 242, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(ubicacion, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 190, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ubicacion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addContainerGap())
        );

        l_plantilla.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        l_plantilla.setText("Plantilla:");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jButton3)))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(22, 427, Short.MAX_VALUE)
                                .addComponent(jButton1))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))))
                .addContainerGap())
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(l_plantilla)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addComponent(l_plantilla)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)))
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

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
       if(t_datos.getRowCount()>0)
       {
           if(t_datos.getSelectedRowCount()>=0)
           {
               int[] renglones = t_datos.getSelectedRows();
               Session session = HibernateUtil.getSessionFactory().openSession();
               ArrayList registro =new ArrayList();
               for(int x=0; x<renglones.length; x++)
               {
                   //registro = (Catalogo)session.get(Catalogo.class, Integer.parseInt(t_datos.getValueAt(renglones[x], 0).toString()));
                   ArrayList aux=new ArrayList();
                   aux.add(t_datos.getValueAt(renglones[x], 0).toString());
                   aux.add(t_datos.getValueAt(renglones[x], 3).toString());
                   registro.add(aux);
               }
               if(session!=null)
                   if(session.isOpen())
                       session.close();
               doClose(registro);
           }
           else
               JOptionPane.showMessageDialog(null, "¡No hay una Concepto seleccionado!");
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void t_buscaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_buscaKeyReleased
        // TODO add your handling code here:
        inicio=0;
        parar=false;
        buscaDato();
    }//GEN-LAST:event_t_buscaKeyReleased

    private void c_filtroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_c_filtroActionPerformed
        // TODO add your handling code here:
        inicio=0;
        parar=false;
        buscaDato();
    }//GEN-LAST:event_c_filtroActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        altaCatalogo obj = new altaCatalogo(new javax.swing.JFrame(), true, user, sessionPrograma);
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        obj.setLocation((d.width/2)-(obj.getWidth()/2), (d.height/2)-(obj.getHeight()/2));
        obj.setVisible(true);
        Catalogo c=obj.getReturnStatus();
        if(c!=null)
        {
            this.t_busca.setText(""+c.getIdCatalogo());
            this.c_filtro.setSelectedItem("id");
        }
        buscaDato();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void t_datosKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_datosKeyPressed
        // TODO add your handling code here:
        int code = evt.getKeyCode(); 
        if(code == KeyEvent.VK_ENTER)
        {
            t_datos.getInputMap(t_datos.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER,0,false), "selectColumnCell");
            this.jButton1.requestFocus();
        }
    }//GEN-LAST:event_t_datosKeyPressed

    private void t_buscaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_t_buscaActionPerformed
        // TODO add your handling code here:
        t_datos.requestFocus();
        t_datos.getSelectionModel().setSelectionInterval(0,0);
    }//GEN-LAST:event_t_buscaActionPerformed

    private void l_grupoValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_l_grupoValueChanged
        // TODO add your handling code here:
        inicio=0;
        parar=false;
        this.buscaDato();
    }//GEN-LAST:event_l_grupoValueChanged

    private void ubicacionItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_ubicacionItemStateChanged
        // TODO add your handling code here:
        inicio=0;
        parar=false;
        this.buscaDato();
    }//GEN-LAST:event_ubicacionItemStateChanged

    private void t_datosKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_datosKeyReleased
        // TODO add your handling code here:
        int code = evt.getKeyCode();
        if(code != KeyEvent.VK_SPACE)
        {
            if(t_datos.getSelectedColumn()==3)
                t_datosMouseClicked(null);
        }
        else
        {
            numeros.requestFocus();
            numeros.setPopupVisible(true);
        }
    }//GEN-LAST:event_t_datosKeyReleased

    private void t_datosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_t_datosMouseClicked
        // TODO add your handling code here:
        if(evt!=null)
        {
            Point p = evt.getPoint(); 
            int row = t_datos.rowAtPoint(p); 
            int column = t_datos.columnAtPoint(p); 
            t_datos.setRowSelectionInterval(row, row);
            t_datos.setColumnSelectionInterval(column, column);
        }
        if(t_datos.getSelectedRow()>=0)
        {
            if(t_datos.getSelectedColumn()==3)
            {
                numeros.removeAllItems();
                numeros.addItem("Cancelar");
                numeros.addItem("Eliminar");
                numeros.setSelectedItem("Cancelar");
                Session session = HibernateUtil.getSessionFactory().openSession();
                try
                {
                    session.beginTransaction().begin();
                    Catalogo cat=(Catalogo) session.get(Catalogo.class, Integer.parseInt(t_datos.getValueAt(t_datos.getSelectedRow(), 0).toString()));
                    Ejemplar[] codigos = (Ejemplar[]) cat.getEjemplars().toArray(new Ejemplar[0]);
                    Ejemplar codigoAux= new Ejemplar();
                    for(int k=0;k<codigos.length;k++) 
                    {
                        for(int f=0;f<(codigos.length-1)-k;f++) 
                        {
                           if (codigos[f].getIdParte().compareTo(codigos[f+1].getIdParte())==1) 
                           {
                               codigoAux=codigos[f];
                               codigos[f]=codigos[f+1];
                               codigos[f+1]=codigoAux;
                           }
                        }
                    }

                    if(codigos.length>0)
                    {
                        for(int i=0; i<codigos.length; i++)
                        {
                            if(codigos[i].getTipoEjemplar().compareTo("R")==0)
                                numeros.addItem(codigos[i].getIdParte());
                        }
                    }
                    session.beginTransaction().commit();
                }catch(Exception e)
                {
                     e.printStackTrace();
                }
                if(session!=null)
                     if(session.isOpen()==true)
                         session.close();
            }
        }
    }//GEN-LAST:event_t_datosMouseClicked

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox c_filtro;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JList l_grupo;
    private javax.swing.JLabel l_plantilla;
    private javax.swing.JComboBox numeros;
    private javax.swing.JScrollPane scroll;
    public javax.swing.JTextField t_busca;
    private javax.swing.JTable t_datos;
    private javax.swing.JComboBox ubicacion;
    // End of variables declaration//GEN-END:variables

    
private void buscaDato()
{
        ListaObjeto obj;
        String anexo=", 'BUSCAR' as parte";
        if(c_filtro.getSelectedItem().toString().compareTo("N° Parte")==0)
            anexo=", ejemp.id_Parte as parte";
        String consulta="select obj.id_catalogo, obj.nombre, especialidad.descripcion"+anexo+" from catalogo as obj ", ordenacion=" obj.id_catalogo", complemento="";
        consulta+="inner join especialidad on obj.id_grupo_mecanico=especialidad.id_grupo_mecanico ";
        
        if(c_filtro.getSelectedItem().toString().compareTo("N° Parte")==0)
            consulta+="left join ejemplar as ejemp on obj.id_catalogo=ejemp.id_catalogo ";

        if(plantilla.compareToIgnoreCase("PLANTILLAS")!=0)
        {
            obj = (ListaObjeto)l_grupo.getSelectedValue();
            if(obj.getTexto().compareToIgnoreCase("TODOS")!=0)
            {
                consulta+="inner join item  as dato on obj.id_catalogo=dato.id_catalogo where ";
                ordenacion=" dato.id_reparacion";
                ubicacion.setEnabled(true);
            }
            else
            {
                ubicacion.setEnabled(false);
                consulta+="where ";
            }
        }
        else 
            consulta+="where ";
        if(c_filtro.getSelectedItem().toString().compareTo("Descripcion")==0)
            consulta+="obj.nombre like '%" + t_busca.getText() +"%'";

        if(c_filtro.getSelectedItem().toString().compareTo("id")==0)
            consulta+="obj.id_catalogo like '%" + t_busca.getText() +"%'";

        if(c_filtro.getSelectedItem().toString().compareTo("G. Mecanico")==0)
            consulta+="especialidad.descripcion like '%" + t_busca.getText() +"%' ";
        
        if(c_filtro.getSelectedItem().toString().compareTo("N° Parte")==0)
            consulta+="ejemp.id_Parte like '%" + t_busca.getText() +"%' and ejemp.tipo_ejemplar='R '";
        
        if(l_grupo.getSelectedValue()!=null)
        {
            obj = (ListaObjeto)l_grupo.getSelectedValue();
            if(obj.getTexto().compareToIgnoreCase("TODOS")!=0)
                consulta += " and especialidad.id_grupo_mecanico = " + obj.getId();
        }
        if(plantilla.compareToIgnoreCase("PLANTILLAS")!=0)
        {
            obj = (ListaObjeto)l_grupo.getSelectedValue();
            if(obj.getTexto().compareToIgnoreCase("TODOS")!=0)
            {
                consulta += " and dato.id_servicio='"+plantilla+"'";
                if(ubicacion.getSelectedItem().toString().compareTo("NA")!=0)
                    complemento=" and dato.id_posicion="+ubicacionesId.get(ubicacion.getSelectedIndex()).toString()+" ";
            }
        }
        consulta+= complemento+" and actual=1 ORDER BY especialidad.orden, "+ordenacion;

        Session session = HibernateUtil.getSessionFactory().openSession();
        try
        {
            int siguente=inicio+20;
            System.out.println(consulta);
            Query q = session.createSQLQuery(consulta);
            q.setFirstResult(inicio);
            q.setMaxResults(20);
            
            q.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
            ArrayList renglones = (ArrayList)q.list();
            if(renglones.size()>0)
            {
                DefaultTableModel modelo= (DefaultTableModel)t_datos.getModel();
                if(inicio==0)
                    modelo.setNumRows(0);
                for(int i =0; i<renglones.size(); i++){
                    java.util.HashMap map=(java.util.HashMap)renglones.get(i);
                    modelo.addRow(new Object[]{map.get("id_catalogo").toString(), map.get("nombre").toString(), map.get("descripcion").toString(), map.get("parte").toString()});
                }
                inicio=siguente;
            }
            else
            {
                parar=true;
                if(inicio==0)
                {
                    DefaultTableModel modelo= (DefaultTableModel)t_datos.getModel();
                    modelo.setNumRows(0);
                }
            }
            t_busca.requestFocus();
        }catch(Exception e)
        {
            e.printStackTrace();
        }
        if(session!=null)
            if(session.isOpen())
                session.close();
    //formatoTabla();
}
    private ArrayList returnStatus = RET_CANCEL;
    
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
                      column.setPreferredWidth(250);
                      break;
                  case 2:
                      column.setPreferredWidth(40);
                      break;
                  case 3:
                      column.setPreferredWidth(60);
                      DefaultCellEditor miEditor = new DefaultCellEditor(numeros);
                      miEditor.setClickCountToStart(2);
                      column.setCellEditor(miEditor);
                      break;
                  case 4:
                      column.setPreferredWidth(250);
                      break;
                  case 5:
                      column.setPreferredWidth(250);
                      break;
                  default:
                      column.setPreferredWidth(40);
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
        
        t_datos.setDefaultRenderer(String.class, formato); 
        t_datos.setDefaultRenderer(Double.class, formato); 
        t_datos.setDefaultRenderer(Integer.class, formato);
    }
     
     public void listaGrupos()
     {
         DefaultListModel dlm = new DefaultListModel();
         Session session = HibernateUtil.getSessionFactory().openSession();
         try
         {
             String consulta="";
             if(plantilla.compareToIgnoreCase("PLANTILLAS")==0)
                 consulta="select distinct id_grupo_mecanico, descripcion  from especialidad where plantilla=1 order by orden asc;";
             else
             {
                 consulta="select distinct especialidad.id_grupo_mecanico, especialidad.descripcion  from servicio " +
                            "inner join item on servicio.id_servicio=item.id_servicio " +
                            "inner join catalogo on item.id_catalogo=catalogo.id_catalogo " +
                            "inner join especialidad on catalogo.id_grupo_mecanico=especialidad.id_grupo_mecanico " +
                            "where servicio.id_servicio='"+plantilla+"' order by especialidad.orden asc";
             }
             System.out.println(consulta);
             Query q = session.createSQLQuery(consulta);
             q.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
             ArrayList especilidad=(ArrayList)q.list();
             for(int i=0; i<especilidad.size(); i++)
             {
                 java.util.HashMap map=(java.util.HashMap)especilidad.get(i);
                 dlm.addElement(new ListaObjeto(Integer.parseInt(map.get("id_grupo_mecanico").toString()), map.get("descripcion").toString(), new ImageIcon("imagenes/simbolo.png")));
             }
             dlm.addElement(new ListaObjeto(-1, "TODOS", new ImageIcon("imagenes/simbolo.png")));
        }catch(Exception e)
        {
            e.printStackTrace();
        }
        if(session!=null)
            if(session.isOpen())
                session.close();
         l_grupo.setModel(dlm);
         l_grupo.setSelectedIndex(l_grupo.getModel().getSize()-1);
         l_grupo.setCellRenderer(new ListaEtiqueta());
         ubicaciones();
     }
     
     private void ubicaciones()
    {
        ubicacion.removeAllItems();
        String consulta="from Posicion";
        ubicacion.addItem("NA");
        Session session = HibernateUtil.getSessionFactory().openSession();
        try
        {
            session.beginTransaction().begin();
            Posicion[] pos=(Posicion[]) session.createCriteria(Posicion.class).addOrder(Order.desc("idPosicion")).list().toArray(new Posicion[0]);
            ubicacionesId=new ArrayList();
            for (int x=0; x<pos.length; x++)
            {
                ubicacion.addItem(pos[x].getDescripcion());
                ubicacionesId.add(pos[x].getIdPosicion());
            }
        }catch(Exception e)
        {
            e.printStackTrace();
        }
        if(session!=null)
            if(session.isOpen()==true)
                session.close();
    }
}
