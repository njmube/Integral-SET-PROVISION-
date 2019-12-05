/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Operaciones;

import Hibernate.Util.HibernateUtil;
import Hibernate.entidades.Configuracion;
import Hibernate.entidades.Orden;
import Integral.Render1;
import java.awt.Color;
import java.text.DecimalFormat;
import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author I.S.C.Salvador
 */
public class Material extends java.awt.Dialog {

    String num_orden;
    String especialidad;
    String trabajo;
    String empleado;
    /**
     * Creates new form Material
     */
    public Material(java.awt.Frame parent, boolean modal, String orden, String especialidad, String trabajo, String empleado) {
        super(parent, modal);
        this.num_orden=orden;
        this.especialidad=especialidad;
        this.trabajo=trabajo;
        this.empleado=empleado;
        initComponents();
        tabla_tamaños();
        lista_consumido_adicional();
    }
    
    void tabla_tamaños()
    {
        TableColumnModel col_model = t_datos.getColumnModel();
        for (int i=0; i<t_datos.getColumnCount(); i++)
        {
  	      TableColumn column = col_model.getColumn(i);
              switch(i)
              {
                  case 0:
                      column.setPreferredWidth(115);
                      break;
                  case 1:
                      column.setPreferredWidth(250);
                      break;
                  case 2:
                      column.setPreferredWidth(50);
                      break;    
                  case 3:
                      column.setPreferredWidth(90);
                      break;
              }
        }
        JTableHeader header = t_datos.getTableHeader();
        header.setForeground(Color.white);
        Color c1 = new java.awt.Color(2, 135, 242);
        for(int x=0; x<t_datos.getColumnModel().getColumnCount(); x++)
            t_datos.getColumnModel().getColumn(x).setHeaderRenderer(new Render1(c1));
        t_datos.setShowVerticalLines(true);
        t_datos.setShowHorizontalLines(true);
    }

    public void lista_consumido_adicional(){
        DecimalFormat formatoPorcentaje = new DecimalFormat("#,##0.00");
        formatoPorcentaje.setMinimumFractionDigits(2);   
        double total = 0.0d, iva=0.0d;
        Session session = HibernateUtil.getSessionFactory().openSession();
        Orden orden = (Orden)session.get(Orden.class, Integer.parseInt(num_orden));
        Configuracion config=(Configuracion)session.get(Configuracion.class, 1);
        iva = config.getIva();
        try{
            String consulta ="select distinct movimiento.id_Parte as id, movimiento.valor,ejemplar.comentario,\n" +
            "(select (select if(sum(cantidad) is null, 0, sum(cantidad)) \n" +
            "from movimiento left join almacen on movimiento.id_almacen=almacen.id_almacen \n" +
            "left join orden on almacen.id_orden=orden.id_orden where orden.id_orden="+num_orden+" \n" +
            "and almacen.operacion=8 and almacen.tipo_movimiento=2 and movimiento.id_Parte=id ";
            
            if(especialidad!=null)
                consulta += "and almacen.especialidad='"+especialidad+"' \n";
            if(trabajo!=null)
                consulta += "and almacen.id_trabajo="+trabajo;
            consulta += ") - \n" +
            "(select if(sum(cantidad) is null, 0, sum(cantidad)) \n" +
            "from movimiento left join almacen on movimiento.id_almacen=almacen.id_almacen \n" +
            "left join orden on almacen.id_orden=orden.id_orden where orden.id_orden="+num_orden+" \n" +
            "and almacen.operacion=8 and almacen.tipo_movimiento=1 and movimiento.id_Parte=id ";
            if(especialidad!=null)
                consulta += "and almacen.especialidad='"+especialidad+"' \n";
            if(trabajo!=null)
                consulta += "and almacen.id_trabajo="+trabajo;
            consulta += "))as monto_consumible \n" +
            "from almacen\n" +
            "right join movimiento on almacen.id_almacen= movimiento.id_almacen\n" +
            "inner join ejemplar on movimiento.id_Parte = ejemplar.id_Parte\n"+
            "where almacen.id_orden="+num_orden;
            if(trabajo!=null)
                consulta += " and almacen.id_trabajo="+trabajo;
            consulta += " and almacen.tipo_movimiento=2 ";
            if(especialidad!=null)
                consulta += "and almacen.especialidad='"+especialidad+"' \n";
            consulta += "and almacen.operacion=8;";
            
            System.out.println(consulta);
            t_orden.setText(num_orden);
            t_empleado.setText(empleado);
            DefaultTableModel modelo=(DefaultTableModel)t_datos.getModel();
            modelo.setRowCount(0);
            Query query = session.createSQLQuery(consulta);
            query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
            ArrayList consumido = (ArrayList)query.list();
            if(consumido.size()>0){
                for(int i =0; i<consumido.size(); i++){
                    java.util.HashMap map=(java.util.HashMap)consumido.get(i);
                    Object[] obj = new Object[4];
                    if(Double.parseDouble(map.get("monto_consumible").toString())>0){
                        obj[0] = map.get("id");
                        obj[1] = map.get("comentario");
                        Double niva = Double.parseDouble(map.get("valor").toString())*iva/100;
                        obj[2] = Double.parseDouble(map.get("monto_consumible").toString());
                        obj[3] = Double.parseDouble(map.get("valor").toString())+niva;
                        modelo.addRow(obj);
                        total += Double.parseDouble(map.get("monto_consumible").toString())* (Double.parseDouble(map.get("valor").toString())+niva);
                    }
                }

                total_consumido.setText(""+formatoPorcentaje.format(total));
                total_consumido.setValue(total);
            }else{
                total_consumido.setText("");
                total_consumido.setValue(0.0d);
            }
        }catch(Exception e)
        {
            e.printStackTrace();
        }
        if(session!=null)
            if(session.isOpen())
            {
                session.flush();
                session.clear();
                session.close();
            }
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel20 = new javax.swing.JLabel();
        jScrollPane6 = new javax.swing.JScrollPane();
        t_datos = new javax.swing.JTable();
        jLabel21 = new javax.swing.JLabel();
        total_consumido = new javax.swing.JFormattedTextField();
        jLabel1 = new javax.swing.JLabel();
        t_orden = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        t_empleado = new javax.swing.JTextField();

        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeDialog(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel20.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel20.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel20.setText("Lista de Material Consumido");

        t_datos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "#Parte", "Descripcion", "Cantidad", "Costo"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.Double.class, java.lang.Double.class
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
        t_datos.getTableHeader().setReorderingAllowed(false);
        jScrollPane6.setViewportView(t_datos);

        jLabel21.setText("Total: $");

        total_consumido.setEditable(false);
        total_consumido.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.00"))));
        total_consumido.setValue(0.0d);
        total_consumido.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                total_consumidoActionPerformed(evt);
            }
        });

        jLabel1.setText("OT:");

        t_orden.setEditable(false);
        t_orden.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                t_ordenActionPerformed(evt);
            }
        });

        jLabel2.setText("Empleado:");

        t_empleado.setEditable(false);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane6)
                    .addComponent(jLabel20, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(0, 583, Short.MAX_VALUE)
                        .addComponent(jLabel21)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(total_consumido, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(t_orden, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(t_empleado)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel20)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(t_orden, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(t_empleado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 208, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(total_consumido, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel21))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        add(jPanel1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Closes the dialog
     */
    private void closeDialog(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_closeDialog
        setVisible(false);
        dispose();
    }//GEN-LAST:event_closeDialog

    private void total_consumidoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_total_consumidoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_total_consumidoActionPerformed

    private void t_ordenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_t_ordenActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_t_ordenActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JTable t_datos;
    private javax.swing.JTextField t_empleado;
    private javax.swing.JTextField t_orden;
    private javax.swing.JFormattedTextField total_consumido;
    // End of variables declaration//GEN-END:variables
}
