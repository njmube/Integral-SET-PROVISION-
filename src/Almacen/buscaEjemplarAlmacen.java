package Almacen;

import Integral.Herramientas;
import Integral.Render1;
import Hibernate.Util.HibernateUtil;
import java.util.List;
import javax.swing.InputMap;
import javax.swing.JOptionPane;
import Hibernate.entidades.Usuario;
import java.awt.Color;
import java.awt.event.KeyEvent;
import javax.swing.ImageIcon;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;

public class buscaEjemplarAlmacen extends javax.swing.JDialog {

    public static final Object[] RET_CANCEL =null;
    InputMap map = new InputMap();
    DefaultTableModel model;
    String sessionPrograma="";
    Herramientas h;
    Usuario usr;
    String orden;
    
    public buscaEjemplarAlmacen(java.awt.Frame parent, boolean modal, String ses, Usuario usuario, String orden) {
        super(parent, modal);
        this.orden=orden;
        sessionPrograma=ses;
        usr=usuario;
        initComponents();
        getRootPane().setDefaultButton(jButton1);
        t_datos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        formatoTabla();
        executeHQLQuery();
    }
    
    private void doClose(Object[] o) {
        returnStatus = o;
        setVisible(false);
        dispose();
    }
    
    public Object[] getReturnStatus() {
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

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        t_busca = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        cb_tipo = new javax.swing.JComboBox();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        t_datos = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Busqueda de Ejemplares");
        setResizable(false);

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "Numero de Parte", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Arial", 1, 11))); // NOI18N

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

        jLabel1.setText("Contiene:");

        cb_tipo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Catalogo", "clave", " " }));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(cb_tipo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(t_busca)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(t_busca, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1)
                    .addComponent(cb_tipo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 9, Short.MAX_VALUE))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "Resultados", javax.swing.border.TitledBorder.RIGHT, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Arial", 1, 11))); // NOI18N

        t_datos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "No Parte", "Modelo", "Marca", "Tipo", "Catálogo", "Medida", "Existencia", "Operario", "-"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Object.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        t_datos.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        t_datos.getTableHeader().setReorderingAllowed(false);
        t_datos.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                t_datosKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(t_datos);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 563, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jButton1.setBackground(new java.awt.Color(2, 135, 242));
        jButton1.setForeground(new java.awt.Color(254, 254, 254));
        jButton1.setIcon(new ImageIcon("imagenes/seleccionar.png"));
        jButton1.setText("Seleccionar");
        jButton1.setToolTipText("Cargar registro seleccionado");
        jButton1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(jButton1)))))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 229, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, 36, Short.MAX_VALUE)
                .addGap(15, 15, 15))
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

    private void t_buscaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_buscaKeyReleased
        executeHQLQuery();
    }//GEN-LAST:event_t_buscaKeyReleased

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        h=new Herramientas(usr, 0);
        h.session(sessionPrograma);
        Session session = HibernateUtil.getSessionFactory().openSession();
        usr = (Usuario)session.get(Usuario.class, usr.getIdUsuario());
        if(usr.getEditarEjemplar()==true)
        {
            if(t_datos.getSelectedRow()>=0)
            {
                Object[] op;
                if(orden.compareTo("")==0)
                {
                    op= new Object[]{
                        t_datos.getValueAt(t_datos.getSelectedRow(), 0).toString(),
                        t_datos.getValueAt(t_datos.getSelectedRow(), 1).toString(),
                        t_datos.getValueAt(t_datos.getSelectedRow(), 2).toString(),
                        t_datos.getValueAt(t_datos.getSelectedRow(), 3).toString(),
                        t_datos.getValueAt(t_datos.getSelectedRow(), 4).toString(),
                        t_datos.getValueAt(t_datos.getSelectedRow(), 5).toString(),
                        Double.parseDouble(t_datos.getValueAt(t_datos.getSelectedRow(), 6).toString()),
                        Double.parseDouble(t_datos.getValueAt(t_datos.getSelectedRow(), 8).toString())
                    };
                }
                else
                {
                    op= new Object[]{
                        t_datos.getValueAt(t_datos.getSelectedRow(), 0).toString(),
                        t_datos.getValueAt(t_datos.getSelectedRow(), 1).toString(),
                        t_datos.getValueAt(t_datos.getSelectedRow(), 2).toString(),
                        t_datos.getValueAt(t_datos.getSelectedRow(), 3).toString(),
                        t_datos.getValueAt(t_datos.getSelectedRow(), 4).toString(),
                        t_datos.getValueAt(t_datos.getSelectedRow(), 5).toString(),
                        Double.parseDouble(t_datos.getValueAt(t_datos.getSelectedRow(), 6).toString()),
                        Double.parseDouble(t_datos.getValueAt(t_datos.getSelectedRow(), 7).toString()),
                        Double.parseDouble(t_datos.getValueAt(t_datos.getSelectedRow(), 8).toString())
                    };
                }
                doClose(op);
            }
            else
                JOptionPane.showMessageDialog(null, "¡No hay un ejemplar seleccionado!");
        }
        else
            JOptionPane.showMessageDialog(null, "¡Acceso denegado!");
        if(session!=null)
            if(session.isOpen())
                session.close();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void t_buscaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_t_buscaActionPerformed
        t_datos.requestFocus();
        t_datos.getSelectionModel().setSelectionInterval(0,0);
    }//GEN-LAST:event_t_buscaActionPerformed

    private void t_datosKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_datosKeyPressed
        // TODO add your handling code here:
        int code = evt.getKeyCode(); 
        if(code == KeyEvent.VK_ENTER)
        {
            t_datos.getInputMap(t_datos.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER,0,false), "selectColumnCell");
            this.jButton1.requestFocus();
        }
    }//GEN-LAST:event_t_datosKeyPressed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox cb_tipo;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    public javax.swing.JTextField t_busca;
    private javax.swing.JTable t_datos;
    // End of variables declaration//GEN-END:variables

        
    private void executeHQLQuery() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            Query q;
            String texto="";
            if(orden.compareTo("")==0)
            {
                texto="select id_parte as id, if(modelo is null,'', modelo) as modelo, if(id_marca is null, '', id_marca) as marca, if(tipo_nombre is null,'', tipo_nombre) as tipo, id_catalogo, medida, existencias, 0.0 as cero " +
                      "from ejemplar where inventario=1 and ";
            }
            else
            {
                texto="select ejemplar.id_Parte as id, if(modelo is null,'', modelo) as modelo, if(id_marca is null, '', id_marca) as marca, if(tipo_nombre is null,'', tipo_nombre) as tipo, id_catalogo, medida, 0.0 as cero, " +
                      "( (select if(sum(movimiento.cantidad) is null, 0, sum(movimiento.cantidad)) from ejemplar left join movimiento on ejemplar.id_Parte=movimiento.id_Parte " +
                      "left join almacen on movimiento.id_almacen=almacen.id_almacen where ejemplar.id_Parte=id and almacen.id_orden="+orden+" and almacen.tipo_movimiento=2 and almacen.operacion=8) - " +
                      "(select if(sum(movimiento.cantidad) is null, 0, sum(movimiento.cantidad)) from ejemplar left join movimiento on ejemplar.id_Parte=movimiento.id_Parte " +
                      "left join almacen on movimiento.id_almacen=almacen.id_almacen where ejemplar.id_Parte=id and almacen.id_orden="+orden+" and almacen.tipo_movimiento=1 and almacen.operacion=8) )as operario, existencias " +
                      "from ejemplar where inventario=1 and ";
            }
            if(cb_tipo.getSelectedItem().toString().compareTo("clave")==0)
                texto+="ejemplar.id_Parte like '"+t_busca.getText()+"%'";
            else
                texto+="id_catalogo like '"+t_busca.getText()+"%'";
            q = session.createSQLQuery(texto);
            q.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
            List resultList = q.list();
            model=(DefaultTableModel)t_datos.getModel();
            model.setRowCount(0);
            int i=0;
            for (Object o : resultList) 
            {
                java.util.HashMap actor=(java.util.HashMap)o;
                Object[] vector=new Object[]{actor.get("id"), actor.get("modelo"),actor.get("marca"),actor.get("tipo"), actor.get("id_catalogo"),actor.get("medida"),actor.get("existencias"),actor.get("operario"),actor.get("cero")};
                model.addRow(vector);
            }
            session.disconnect();
        } catch (Exception he) {
            he.printStackTrace();
            List lista= null;
        }
        finally
        {
            if(session!=null)
                if(session.isOpen())
                    session.close();
        }
    }

    private Object[] returnStatus = RET_CANCEL;
    
    public void formatoTabla()
    {
        Color c1 = new java.awt.Color(2, 135, 242);
        for(int x=0; x<t_datos.getColumnModel().getColumnCount(); x++)
            t_datos.getColumnModel().getColumn(x).setHeaderRenderer(new Render1(c1));
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
                    column.setPreferredWidth(100);
                    break;
                case 1:
                    column.setPreferredWidth(10);
                    break;
                case 2:
                    column.setPreferredWidth(10);
                    break;      
                case 3:
                    column.setPreferredWidth(10);
                    break; 
                case 4:
                    column.setPreferredWidth(150);
                    break; 
                case 5:
                    column.setPreferredWidth(50);
                    break; 
                default:
                    column.setPreferredWidth(40);
                    break; 
            }
        }
        JTableHeader header = t_datos.getTableHeader();
        header.setForeground(Color.white);
    }
}