/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * acceso.java
 *
 * Created on 19/01/2012, 02:01:25 PM
 */
package Compras;

import Integral.FormatoTabla;
import Integral.Herramientas;
import Integral.Render1;
import Hibernate.Util.HibernateUtil;
import java.util.List;
import java.util.Vector;
import javax.swing.InputMap;
import javax.swing.JOptionPane;
import Hibernate.entidades.Partida;
import Hibernate.entidades.PartidaExterna;
import Hibernate.entidades.Pedido;
import Hibernate.entidades.Usuario;
import Integral.HorizontalBarUI;
import Integral.VerticalBarUI;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.ImageIcon;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author ISC_SALVADOR
 */
public class agregaFactura extends javax.swing.JDialog {

    public static final Pedido RET_CANCEL =null;
    InputMap map = new InputMap();
    DefaultTableModel model;
    String[] columnas = new String [] {"Pedido", "Fecha", "O. Taller", "Usuario", "Proveedor", "Nombre de Proveedor", "Facturar a", "Observaciones", "Monto tot.", "no° Factura"};
    FormatoTabla formato;
    int tipoVentana=0;
    String sessionPrograma="";
    Usuario user;
    editaPedido edita_pedidos;
    Herramientas h;
    String rutaFTP;
    int configuracion=1;
    
    /** Creates new form acceso */
    public agregaFactura(java.awt.Frame parent, boolean modal, int tipo, String sessionP, Usuario usr, int configuracion, String carpeta) {
        super(parent, modal);
        initComponents();
        rutaFTP=carpeta;
        this.configuracion=configuracion;
        scroll.getVerticalScrollBar().setUI(new VerticalBarUI());
        scroll.getHorizontalScrollBar().setUI(new HorizontalBarUI());
        getRootPane().setDefaultButton(jButton5);
        tipoVentana=tipo;
        t_datos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        formato=new FormatoTabla();
        titulos();
        buscaDato();
        sessionPrograma=sessionP;
        user=usr;
    }
    
   DefaultTableModel ModeloTablaReporte(int renglones, String columnas[])
        {
            model = new DefaultTableModel(new Object [renglones][9], columnas)
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
                    java.lang.Double.class,
                    java.lang.Integer.class
                };
                boolean[] canEdit = new boolean [] {
                    false, false, false, false, false, false, false, false, false, true
                };

                public void setValueAt(Object value, int row, int col)
                {
                    Vector vector = (Vector)this.dataVector.elementAt(row);
                    Object celda = ((Vector)this.dataVector.elementAt(row)).elementAt(col);
                    switch(col)
                    {

                        case 9:
                            if(vector.get(col)==null)
                            {
                                vector.setElementAt(value, col);
                                this.dataVector.setElementAt(vector, row);
                                fireTableCellUpdated(row, col);
                            }
                            else
                            {
                                Session session = HibernateUtil.getSessionFactory().openSession();
                                try
                                {
                                    if((Integer)value>=0)
                                    {
                                        session.beginTransaction().begin();
                                        Pedido ped=(Pedido)session.get(Pedido.class, (Integer)t_datos.getValueAt(row, 0));
                                        if((Integer)value!=0)
                                            ped.setNoFactura((Integer)value);
                                        else
                                            ped.setNoFactura(null);
                                        session.update(ped);
                                        session.beginTransaction().commit();

                                        if((Integer)value!=0)
                                            vector.setElementAt(value, col);
                                        else
                                            vector.setElementAt(0, col);
                                        this.dataVector.setElementAt(vector, row);
                                        fireTableCellUpdated(row, col);
                                        JOptionPane.showMessageDialog(null, "Factura guardada"); 
                                    }
                                    else
                                        JOptionPane.showMessageDialog(null, "El campo no permite números menorea a 1"); 
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

    
    private void doClose(Pedido o) {
        returnStatus = o;
        setVisible(false);
        dispose();
    }
    
    public Pedido getReturnStatus() {
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

        ventana = new javax.swing.JDialog();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        t_busca = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        c_filtro = new javax.swing.JComboBox();
        jPanel3 = new javax.swing.JPanel();
        scroll = new javax.swing.JScrollPane();
        t_datos = new javax.swing.JTable();
        jButton5 = new javax.swing.JButton();

        ventana.setModalityType(java.awt.Dialog.ModalityType.APPLICATION_MODAL);
        ventana.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                ventanaWindowClosing(evt);
            }
        });

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Busqueda de Pedidos");
        setResizable(false);

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Filtrar por:", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 1, 11))); // NOI18N

        t_busca.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                t_buscaActionPerformed(evt);
            }
        });
        t_busca.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                t_buscaKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                t_buscaKeyTyped(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel1.setText("Contiene:");

        c_filtro.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Seleccione un filtro", "Pedido", "O. Taller", "No Proveedor", "Nombre de Proveedor" }));
        c_filtro.setNextFocusableComponent(t_busca);
        c_filtro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                c_filtroActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(c_filtro, javax.swing.GroupLayout.PREFERRED_SIZE, 239, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
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
                    .addComponent(jLabel1)
                    .addComponent(c_filtro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 15, Short.MAX_VALUE))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Resultados", javax.swing.border.TitledBorder.RIGHT, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 1, 11))); // NOI18N

        t_datos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Pedido", "Fecha", "O. Taller", "Usuario", "Prov.", "Nombre de Proveedor", "Facturar a", "Observaciones", "Monto Tot.", "no° Factura"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Double.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
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
            .addGap(0, 1076, Short.MAX_VALUE)
            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel3Layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(scroll, javax.swing.GroupLayout.PREFERRED_SIZE, 1076, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 423, Short.MAX_VALUE)
            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel3Layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(scroll, javax.swing.GroupLayout.PREFERRED_SIZE, 423, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );

        jButton5.setBackground(new java.awt.Color(2, 135, 242));
        jButton5.setForeground(new java.awt.Color(254, 254, 254));
        jButton5.setIcon(new ImageIcon("imagenes/seleccionar.png"));
        jButton5.setText("Seleccionar");
        jButton5.setToolTipText("Cargar registro seleccionado");
        jButton5.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButton5)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton5)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void t_buscaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_buscaKeyReleased
        // TODO add your handling code here:
        this.buscaDato();
    }//GEN-LAST:event_t_buscaKeyReleased

    private void c_filtroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_c_filtroActionPerformed
        // TODO add your handling code here:
        t_busca.setText("");
       this.buscaDato();
    }//GEN-LAST:event_c_filtroActionPerformed

    private void t_buscaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_t_buscaActionPerformed
        // TODO add your handling code here:
        t_datos.requestFocus();
        t_datos.getSelectionModel().setSelectionInterval(0,0);
    }//GEN-LAST:event_t_buscaActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:
        t_busca.requestFocus();
        
        if(t_datos.getSelectedRow()>=0)
        {
            Pedido ped = new Pedido();
            ped.setIdPedido(Integer.parseInt(t_datos.getValueAt(t_datos.getSelectedRow(), 0).toString()));
            if(ped!=null)
            {
                h=new Herramientas(user, 20);
                h.session(sessionPrograma);
                edita_pedidos = new editaPedido(user, sessionPrograma, ped, 20, configuracion, rutaFTP);
                ventana.setSize(1108, 530);
                ventana.add(edita_pedidos);
                ventana.repaint();
                Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
                ventana.setLocation((d.width/2)-(ventana.getWidth()/2), (d.height/2)-(ventana.getHeight()/2));
                ventana.setVisible(true);
            }
        }
        else
            JOptionPane.showMessageDialog(null, "¡No hay un cliente seleccionado!");
    }//GEN-LAST:event_jButton5ActionPerformed

    private void t_buscaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_buscaKeyTyped
        // TODO add your handling code here:
        char car = evt.getKeyChar();
        if(c_filtro.getSelectedItem().toString().compareTo("Pedido")==0 || c_filtro.getSelectedItem().toString().compareTo("O. Taller")==0 || c_filtro.getSelectedItem().toString().compareTo("No Proveedor")==0)
        {
            if((car<'0' || car>'9'))
                evt.consume();
        }
    }//GEN-LAST:event_t_buscaKeyTyped

    private void t_datosKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_datosKeyPressed
        // TODO add your handling code here:
        /*int code = evt.getKeyCode(); 
        if(code == KeyEvent.VK_ENTER)
        {
            t_datos.getInputMap(t_datos.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER,0,false), "selectColumnCell");
            this.jButton5.requestFocus();
        }*/
    }//GEN-LAST:event_t_datosKeyPressed

    private void ventanaWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_ventanaWindowClosing
        // TODO add your handling code here:
        h=new Herramientas(user, 20);
        h.session(sessionPrograma);
        h.desbloqueaOrden();
        h.desbloqueaPedido();
    }//GEN-LAST:event_ventanaWindowClosing

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox c_filtro;
    private javax.swing.JButton jButton5;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane scroll;
    public javax.swing.JTextField t_busca;
    private javax.swing.JTable t_datos;
    private javax.swing.JDialog ventana;
    // End of variables declaration//GEN-END:variables

    private void buscaDato()
    {
        /*if(t_busca.getText().compareTo("")!=0)
        {*/
            String consulta="SELECT DISTINCT obj from Pedido obj ";
            String aux="";
            if(c_filtro.getSelectedItem().toString().compareTo("Pedido")==0)
                if(t_busca.getText().compareTo("")!=0)
                    consulta+=" where obj.idPedido =" + t_busca.getText();
            
            if(c_filtro.getSelectedItem().toString().compareTo("O. Taller")==0)
            {
                if(t_busca.getText().compareTo("")!=0)
                {
                    aux="SELECT DISTINCT obj from Pedido obj "
                      + "WHERE obj.partida.ordenByIdOrden.idOrden like '%"+t_busca.getText()+"%'";
                    
                    consulta+="LEFT JOIN FETCH obj.partidas partP "
                            + "LEFT JOIN partP.ordenByIdOrden ord "
                            + "WHERE ord.idOrden like'%"+t_busca.getText()+"%'";
                }
            }
            
            if(c_filtro.getSelectedItem().toString().compareTo("No Proveedor")==0)
                if(t_busca.getText().compareTo("")!=0)
                    consulta+=" where obj.proveedorByIdProveedor.idProveedor=" + t_busca.getText();
            
            if(c_filtro.getSelectedItem().toString().compareTo("Nombre de Proveedor")==0)
                if(t_busca.getText().compareTo("")!=0)
                    consulta+=" where obj.proveedorByIdProveedor.nombre like '%" + t_busca.getText() +"%'";
            
            if(tipoVentana==1)
            {
                if(c_filtro.getSelectedItem().toString().compareTo("Seleccione un filtro")!=0)
                {
                    if(t_busca.getText().compareTo("")==0)
                    {
                        //consulta+=" where obj.usuarioByAutorizo!=null and obj.usuarioByAutorizo2!=null";
                        consulta+=" where obj.usuarioByAutorizo2!=null";
                    }
                    else
                    {
                        if(c_filtro.getSelectedItem().toString().compareTo("O. Taller")==0)
                            aux+=" and obj.usuarioByAutorizo2!=null";
                            //aux+=" and obj.usuarioByAutorizo!=null and obj.usuarioByAutorizo2!=null";
                        consulta+=" and obj.usuarioByAutorizo2!=null";
                        //consulta+=" and obj.usuarioByAutorizo!=null and obj.usuarioByAutorizo2!=null";
                    }
                }
                else
                    consulta+=" where obj.usuarioByAutorizo2!=null";
                    //consulta+=" where obj.usuarioByAutorizo!=null and obj.usuarioByAutorizo2!=null";
            }
            Session session = HibernateUtil.getSessionFactory().openSession();
            try
            {
                session.beginTransaction();
                Query q = session.createQuery(consulta);
                List resultList = q.list();

                if(aux.compareTo("")!=0)
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
                        model.setValueAt(actor.getFechaPedido(), i, 1);
                        Partida[] part=(Partida[])actor.getPartidas().toArray(new Partida[0]);
                        PartidaExterna[] partEx=(PartidaExterna[])actor.getPartidaExternas().toArray(new PartidaExterna[0]);
                        double tot=0.0d;
                        if(part.length>0)
                        {
                            model.setValueAt(part[0].getOrdenByIdOrden().getIdOrden(), i, 2);
                            for(int x=0; x<part.length; x++)
                                tot+=part[x].getCantPcp()*part[x].getPcp();
                            model.setValueAt(tot, i , 8);
                        }
                        else
                        {
                            model.setValueAt("", i, 2);
                            if(partEx.length>0)
                            {
                                for(int x=0; x<partEx.length; x++)
                                    tot+=partEx[x].getCantidad()*partEx[x].getCosto();
                                model.setValueAt(tot, i , 8);
                            }
                            else
                                model.setValueAt(0.0, i , 8);
                        }
                        model.setValueAt(actor.getUsuarioByIdUsuario().getIdUsuario(), i, 3);
                        model.setValueAt(actor.getProveedorByIdProveedor().getIdProveedor(), i, 4);
                        model.setValueAt(actor.getProveedorByIdProveedor().getNombre(), i, 5);
                        model.setValueAt(actor.getProveedorByIdEmpresa().getNombre(), i, 6);
                        model.setValueAt(actor.getNotas(), i, 7);
                        if(actor.getNoFactura()!=null)
                            model.setValueAt(actor.getNoFactura(), i, 9);
                        else
                            model.setValueAt(0, i, 9);
                        i++;
                    }
                }
                else
                    t_datos.setModel(ModeloTablaReporte(0, columnas));
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
        //}
        titulos();
    }
    private Pedido returnStatus = RET_CANCEL;
    
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
                    column.setPreferredWidth(60);
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
}
