package Ejemplar;

import Integral.Herramientas;
import Integral.Render1;
import Hibernate.Util.HibernateUtil;
import Hibernate.entidades.Catalogo;
import java.util.List;
import javax.swing.InputMap;
import javax.swing.JOptionPane;
import Hibernate.entidades.Ejemplar;
import Hibernate.entidades.Usuario;
import Integral.HorizontalBarUI;
import Integral.VerticalBarUI;
import java.awt.Color;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Vector;
import javax.swing.ImageIcon;
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

public class buscaEjemplar extends javax.swing.JDialog {

    public static final Ejemplar RET_CANCEL =null;
    InputMap map = new InputMap();
    DefaultTableModel model;
    String[] columnas = new String [] {"No Parte","Marca", "Linea", "Descripcion", "Medida", "Ubicacion"};
    String sessionPrograma="";
    Herramientas h;
    Usuario usr;
    int tipo=0;
    int inicio=0;
    boolean parar=false;
    final JScrollBar scrollBar;
    ArrayList lista_id=new ArrayList();
    
    public buscaEjemplar(java.awt.Frame parent, boolean modal, String ses, Usuario usuario, int tipo) {
        super(parent, modal);
        this.tipo=tipo;
        sessionPrograma=ses;
        usr=usuario;
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
            }
          });
        scroll.getVerticalScrollBar().setUI(new VerticalBarUI());
        scroll.getHorizontalScrollBar().setUI(new HorizontalBarUI());
        getRootPane().setDefaultButton(jButton1);
        t_datos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        /*String consulta="from Ejemplar em where em.idParte like '%" + t_busca.getText() +"%'";        
        if(tipo<2)
            consulta+=" and inventario="+tipo;
        executeHQLQuery(consulta);*/
        buscaDato();
        this.formatoTabla();
    }
    
    void buscaDato()
    {
        String consulta;
        if(cb_tipo.getSelectedItem().toString().compareTo("Clave")==0)
            consulta="from Ejemplar em where em.idParte like '%" + t_busca.getText() +"%'";
        else
            consulta="from Ejemplar em where em.comentario like '%" + t_busca.getText() +"%'";
        switch(tipo)
        {
            case 0:
                consulta+=" and inventario="+tipo;
                break;
            case 1:
                consulta+=" and inventario="+tipo;
                break;
            case 2:
                break;
            case 3://Ejemplares que son consumibles no refacciones
                consulta+=" and inventario=1 and grupo.descripcion!='REFACCIONES'";
                break;
            case 4://
                consulta+=" and inventario=0 and grupo.descripcion!='REFACCIONES'";
                break;
        }
        executeHQLQuery(consulta);
    }
    DefaultTableModel ModeloTablaReporte(int renglones, String columnas[])
        {
            model = new DefaultTableModel(new Object [renglones][6], columnas)
            {
                Class[] types = new Class [] {
                    java.lang.String.class, 
                    java.lang.String.class,
                    java.lang.String.class,
                    java.lang.String.class,
                    java.lang.String.class,
                    java.lang.String.class
                };
                boolean[] canEdit = new boolean [] {
                    false, false, false, false, false, false
                };

                @Override
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
                                    break;

                            default:
                                    vector.setElementAt(value, col);
                                    this.dataVector.setElementAt(vector, row);
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

    
    private void doClose(Ejemplar o) {
        returnStatus = o;
        setVisible(false);
        dispose();
    }
    
    public Ejemplar getReturnStatus() {
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
        cb_tipo = new javax.swing.JComboBox();
        jLabel1 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        scroll = new javax.swing.JScrollPane();
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

        cb_tipo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Clave", "Catalogo" }));
        cb_tipo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cb_tipoActionPerformed(evt);
            }
        });

        jLabel1.setText("contiene:");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
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
                    .addComponent(cb_tipo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addGap(0, 9, Short.MAX_VALUE))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "Resultados", javax.swing.border.TitledBorder.RIGHT, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Arial", 1, 11))); // NOI18N

        t_datos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Clave", "Marca", "Linea", "Descripcion", "Medida"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
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
        scroll.setViewportView(t_datos);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(scroll, javax.swing.GroupLayout.DEFAULT_SIZE, 563, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(scroll, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
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
        inicio=0;
        parar=false;
        lista_id=new ArrayList();
        buscaDato();
    }//GEN-LAST:event_t_buscaKeyReleased

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        //h=new Herramientas(usr, 0);
        //h.session(sessionPrograma);
        Session session = HibernateUtil.getSessionFactory().openSession();
        //usr = (Usuario)session.get(Usuario.class, usr.getIdUsuario());
        /*if(usr.getEditarEjemplar()==true)
        {*/
            if(t_datos.getSelectedRow()>=0)
            {
                Ejemplar op= new Ejemplar();
                op.setIdParte(t_datos.getValueAt(t_datos.getSelectedRow(), 0).toString());
                Catalogo cat=null;
                //if(lista_id.get(t_datos.getSelectedRow())!=null)
                //{
                    ///*cat = new Catalogo();
                    //cat.setNombre(t_datos.getValueAt(t_datos.getSelectedRow(), 3).toString());
                    //cat.setIdCatalogo((int)lista_id.get(t_datos.getSelectedRow()));
                //}
                if(t_datos.getValueAt(t_datos.getSelectedRow(), 3)!=null)
                    op.setComentario(t_datos.getValueAt(t_datos.getSelectedRow(), 3).toString());
                else
                    op.setComentario("");
                op.setCatalogo(cat);
                
                if(t_datos.getValueAt(t_datos.getSelectedRow(), 4)!=null)
                    op.setMedida(t_datos.getValueAt(t_datos.getSelectedRow(), 4).toString());
                else
                    op.setMedida("");
                doClose(op);
            }
            else
                JOptionPane.showMessageDialog(null, "¡No hay un ejemplar seleccionado!");
        /*}
        else
            JOptionPane.showMessageDialog(null, "¡Acceso denegado!");*/
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

    private void cb_tipoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cb_tipoActionPerformed
        // TODO add your handling code here:
        inicio=0;
        parar=false;
        lista_id=new ArrayList();
        buscaDato();
    }//GEN-LAST:event_cb_tipoActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox cb_tipo;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane scroll;
    public javax.swing.JTextField t_busca;
    private javax.swing.JTable t_datos;
    // End of variables declaration//GEN-END:variables

        
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
                    Ejemplar actor = (Ejemplar) o;
                    Object[] renglon=new Object[9];
                    renglon[0]=actor.getIdParte();
                    if(actor.getMarca()!=null)
                        renglon[1]=actor.getMarca().getIdMarca();
                    else
                        renglon[1]=null;
                    if(actor.getGrupo()!=null)
                    renglon[2]=actor.getGrupo().getDescripcion();
                    else
                        renglon[2]=null;
                    /*if(actor.getCatalogo()!=null)
                    {
                        renglon[3]=actor.getCatalogo().getNombre();
                        lista_id.add(actor.getCatalogo().getIdCatalogo());
                    }
                    else
                    {*/
                        renglon[3]=actor.getComentario();
                        lista_id.add(null);
                    //}
                    renglon[4]=actor.getMedida();
                    renglon[5]=actor.getUbicacion();
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

    private Ejemplar returnStatus = RET_CANCEL;
    
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
                    column.setPreferredWidth(120);
                    break;
                case 3:
                    column.setPreferredWidth(300);
                    break; 
                case 4:
                    column.setPreferredWidth(50);
                    break; 
                default:
                    column.setPreferredWidth(0);
                    break; 
            }
        }
        JTableHeader header = t_datos.getTableHeader();
        header.setForeground(Color.white);
    }
}