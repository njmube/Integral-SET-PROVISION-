/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package sat;

import Hibernate.Util.HibernateUtil;
import Hibernate.entidades.ProductoServicio;
import Hibernate.entidades.Usuario;
import Integral.Herramientas;
import Integral.HorizontalBarUI;
import Integral.Render1;
import Integral.VerticalBarUI;
import java.awt.Color;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.Vector;
import javax.swing.InputMap;
import javax.swing.JOptionPane;
import javax.swing.JScrollBar;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author Hp Sistemas
 */
public class buscarProServicio extends javax.swing.JDialog {
    
    public static final ProductoServicio  RET_CANCEL = null;
    String[] columnas = new String[]{"Clave", "Descripción"};
    InputMap map = new InputMap();
    DefaultTableModel model;
    String sessionPrograma = "";
    Herramientas h;
    Usuario usr;
    int inicio=0;
    boolean parar=false;
    final JScrollBar scrollBar;
    
    public buscarProServicio(java.awt.Frame parent, boolean modal, Usuario usr, String ses) {
        super(parent, modal);
        sessionPrograma=ses;
        this.usr=usr;
        initComponents();
        scrollBar = scroll.getVerticalScrollBar();
        scrollBar.addAdjustmentListener(new AdjustmentListener() {
            public void adjustmentValueChanged(AdjustmentEvent ae) {
                if(parar==false)
                {
                    int extent = scrollBar.getModel().getExtent();
                    extent+=scrollBar.getValue();
                    if(extent==scrollBar.getMaximum())
                        buscarDato();
                }
            }
          });
        scroll.getVerticalScrollBar().setUI(new VerticalBarUI());
        scroll.getHorizontalScrollBar().setUI(new HorizontalBarUI());
        getRootPane().setDefaultButton(botonSeleccionar);
        formatoTabla();
        t_datos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        buscarDato();
    }
    
    DefaultTableModel ModeloTablaReporte(int renglones, String columnas[])
        {
            model = new DefaultTableModel(new Object [renglones][3], columnas)
            {
                Class[] types = new Class [] {
                    java.lang.String.class, 
                    java.lang.String.class,
                    java.lang.String.class
                };
                boolean[] canEdit = new boolean [] {
                    false, false
                };

                public void setValueAt(Object value, int row, int col)
                 {
                        Vector vector = (Vector)this.dataVector.elementAt(row);
                        Object celda = ((Vector)this.dataVector.elementAt(row)).elementAt(col);
                        switch(col)
                        {
                            case 0:
                                    vector.setElementAt(value, col);
                                    this.dataVector.setElementAt(vector, row);
                                    fireTableCellUpdated(row, col);
                                    //calcula_totales();
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

     private void buscarDato() {
        String consulta;
        if(cb_tipo.getSelectedIndex()==0)
            consulta="from ProductoServicio obj where obj.descripcion like '%" + textBuscarServicio.getText() + "%'";
        else
            consulta="from ProductoServicio obj where obj.idProductoServicio like '%" + textBuscarServicio.getText() + "%'";
        executeHQLQuery(consulta);
    }
     
     private List<Object[]> executeHQLQuery(String hql) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            session.beginTransaction();
            int siguente=inicio+20;
            Query q = session.createQuery(hql);
            q.setFirstResult(inicio);
            q.setMaxResults(20);
            List resultList = q.list();
            if(resultList.size()>0)
            {
                DefaultTableModel modelo= (DefaultTableModel)t_datos.getModel();
                if(inicio==0)
                    modelo.setNumRows(0);
                for (Object o : resultList) 
                {
                    ProductoServicio actor = (ProductoServicio) o;
                    Object[] renglon=new Object[2];
                    renglon[0]=actor.getIdProductoServicio();
                    renglon[1]=actor.getDescripcion();
                    modelo.addRow(renglon);
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
                session.getTransaction().commit();
                session.disconnect();
                return resultList;
        } catch (Exception he) {
            he.printStackTrace();
            List lista= null;
            return lista;
        }
        finally
        {
            if(session!=null)
                if(session.isOpen())
                    session.close();
        }
    }
     
    public void formatoTabla() {
        Color c1 = new java.awt.Color(2, 135, 242);
        for (int x = 0; x < t_datos.getColumnModel().getColumnCount(); x++) {
            t_datos.getColumnModel().getColumn(x).setHeaderRenderer(new Render1(c1));
        }
        tabla_tamaños();
        t_datos.setShowVerticalLines(true);
        t_datos.setShowHorizontalLines(true);
    }
    
    public void tabla_tamaños(){
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
                      column.setPreferredWidth(150);
                      break;
                  default:
                      column.setPreferredWidth(40);
                      break;
                      
              }
        }
        JTableHeader header = t_datos.getTableHeader();
        header.setForeground(Color.white);
    }  
      
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        textBuscarServicio = new javax.swing.JTextField();
        cb_tipo = new javax.swing.JComboBox();
        jPanel2 = new javax.swing.JPanel();
        scroll = new javax.swing.JScrollPane();
        t_datos = new javax.swing.JTable();
        botonSeleccionar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Buscar Servicio", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.TOP));

        jLabel1.setText("Buscar:");

        textBuscarServicio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                textBuscarServicioActionPerformed(evt);
            }
        });
        textBuscarServicio.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                textBuscarServicioKeyReleased(evt);
            }
        });

        cb_tipo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "DESCRIPCION", "ID" }));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cb_tipo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(textBuscarServicio, javax.swing.GroupLayout.PREFERRED_SIZE, 364, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(textBuscarServicio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cb_tipo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Resultados", javax.swing.border.TitledBorder.RIGHT, javax.swing.border.TitledBorder.TOP));

        t_datos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Clave", "Descripción"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        t_datos.getTableHeader().setReorderingAllowed(false);
        t_datos.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                t_datosKeyPressed(evt);
            }
        });
        scroll.setViewportView(t_datos);

        botonSeleccionar.setBackground(new java.awt.Color(2, 135, 242));
        botonSeleccionar.setForeground(new java.awt.Color(254, 254, 254));
        botonSeleccionar.setText("Seleccionar");
        botonSeleccionar.setToolTipText("Cargar registro seleccionado");
        botonSeleccionar.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        botonSeleccionar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonSeleccionarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(scroll)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(botonSeleccionar)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(scroll, javax.swing.GroupLayout.PREFERRED_SIZE, 237, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(botonSeleccionar, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void textBuscarServicioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_textBuscarServicioActionPerformed
        // TODO add your handling code here:
        t_datos.requestFocus();
        t_datos.getSelectionModel().setSelectionInterval(0,0);
    }//GEN-LAST:event_textBuscarServicioActionPerformed

    private void botonSeleccionarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonSeleccionarActionPerformed
        h = new Herramientas(usr, 0);
        h.session(sessionPrograma);
        Session session = HibernateUtil.getSessionFactory().openSession();
        usr = (Usuario) session.get(Usuario.class, usr.getIdUsuario());
        if (t_datos.getRowCount() > 0) {
            if (t_datos.getSelectedRow() >= 0) {
                ProductoServicio opc = new ProductoServicio();
                opc.setIdProductoServicio(t_datos.getValueAt(t_datos.getSelectedRow(), 0).toString());
                opc.setDescripcion(t_datos.getValueAt(t_datos.getSelectedRow(), 1).toString());
                if (session != null) {
                    if (session.isOpen()) {
                        session.close();
                    }
                }
                doClose(opc);
            } else {
                JOptionPane.showMessageDialog(null, "¡No ha seleccionado una clave!");
            }
        }
        if (session != null) {
            if (session.isOpen()) {
                session.close();
            }
        }
    }//GEN-LAST:event_botonSeleccionarActionPerformed

    private void textBuscarServicioKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_textBuscarServicioKeyReleased
        // TODO add your handling code here:
        inicio=0;
        parar=false;
        this.buscarDato();
    }//GEN-LAST:event_textBuscarServicioKeyReleased

    private void t_datosKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_datosKeyPressed
        // TODO add your handling code here:
        int code = evt.getKeyCode(); 
        if(code == KeyEvent.VK_ENTER)
        {
            t_datos.getInputMap(t_datos.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER,0,false), "selectColumnCell");
            this.botonSeleccionar.requestFocus();
        }
    }//GEN-LAST:event_t_datosKeyPressed

    private ProductoServicio returnStatus = RET_CANCEL;

    private void doClose(ProductoServicio o) {
        returnStatus = o;
        setVisible(false);
        dispose();

    }

    public ProductoServicio getReturnStatus() {
        return returnStatus;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton botonSeleccionar;
    private javax.swing.JComboBox cb_tipo;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane scroll;
    private javax.swing.JTable t_datos;
    private javax.swing.JTextField textBuscarServicio;
    // End of variables declaration//GEN-END:variables
}
