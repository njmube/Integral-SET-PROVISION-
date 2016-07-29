/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Contabilidad;

import Hibernate.Util.HibernateUtil;
import Hibernate.entidades.Concepto;
import Hibernate.entidades.Factura;
import Hibernate.entidades.Nota;
import Hibernate.entidades.Usuario;
import Integral.calendario;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author salvador
 */
public class Cuentas extends javax.swing.JPanel {

    Usuario usr;
    String sessionPrograma;
    Factura factura=null;
    Nota nota=null;
    String edo="", edo_factura="";
    /**
     * Creates new form Cuentas
     */
    public Cuentas(Usuario usr, String sess) {
        initComponents();
        this.usr=usr;
        sessionPrograma=sess;
        
        t_factura.setText("");
        borra_cajas();
        estado(false);
        t_factura.requestFocus();
        factura=null;
    }

    public void buscaDato()
    {
        if(t_factura.getText().compareTo("")!=0)
        {
            Session session = HibernateUtil.getSessionFactory().openSession();
            try 
            {
                session.beginTransaction().begin();
                //factura = (Factura)session.get(Factura.class, Integer.parseInt(t_factura.getText()));
                if(cb.getSelectedItem().toString().compareTo("FACTURA")==0)
                {
                    factura = (Factura)session.createCriteria(Factura.class).add(Restrictions.eq("folio", t_factura.getText())).uniqueResult();
                    if(factura!=null)
                    {
                        this.tf.setText(this.cb.getSelectedItem().toString());
                        this.t_id.setText(""+factura.getIdFactura());
                        this.t_fecha.setText(factura.getFechaFiscal());
                        this.t_rfc.setText(factura.getRfcReceptor());
                        this.t_social.setText(factura.getNombreReceptor());
                        this.t_fiscal.setText(factura.getFFiscal());
                        this.t_serie.setText(factura.getSerie());
                        this.t_folio.setText(factura.getFolio());
                        edo_factura=factura.getEstadoFactura();
                        try{
                            Concepto[] renglon=(Concepto[])factura.getConceptos().toArray(new Concepto[0]);
                            BigDecimal total=new BigDecimal("0.0");
                            for(int w=0; w<factura.getConceptos().size(); w++)
                            {
                                BigDecimal cantidad=new BigDecimal(""+renglon[w].getCantidad());
                                BigDecimal precio=new BigDecimal(""+renglon[w].getPrecio());
                                BigDecimal descuento=new BigDecimal(""+renglon[w].getDescuento());
                                descuento=descuento.divide(new BigDecimal("100"));
                                BigDecimal subtotal=cantidad.multiply(precio).setScale(2, BigDecimal.ROUND_HALF_UP);
                                BigDecimal resta=subtotal.multiply(descuento).setScale(2, BigDecimal.ROUND_HALF_UP);
                                total = total.add(subtotal.subtract(resta)).setScale(2, BigDecimal.ROUND_HALF_UP);
                            }
                            BigDecimal iva=new BigDecimal(""+factura.getIva());
                            iva=iva.divide(new BigDecimal("100"));
                            total=total.add(total.multiply(iva));
                            this.t_monto.setValue(Double.parseDouble(total.setScale(2, BigDecimal.ROUND_HALF_UP).toString()));
                            t_monto.commitEdit();
                        }catch(Exception e){}
                        this.c_estatus.setSelectedItem(factura.getEstatus());
                        edo=factura.getEstatus();
                        if(factura.getfEstatus()!=null)
                        {
                            DateFormat dateFormat;
                            try
                            {
                                Date fecha=factura.getfEstatus();
                                dateFormat = new SimpleDateFormat("dd-MM-yyyy");//YYYY-MM-DD HH:MM:SS
                                t_fecha_estatus.setText(dateFormat.format(fecha));
                            }catch(Exception E){this.t_fecha_estatus.setText("");}
                        }
                        else
                            this.t_fecha_estatus.setText("");
                        if(factura.getTexto()!=null)
                            this.t_estatus.setText(factura.getTexto());
                        else
                            this.t_estatus.setText("");
                        session.beginTransaction().commit();
                        estado(true);
                    }
                    else
                    {
                        t_factura.setText("");
                        borra_cajas();
                        estado(false);
                        t_factura.requestFocus();
                        factura=null;
                        JOptionPane.showMessageDialog(null, "La cuenta no existe!");
                    }
                }
                else
                {
                    nota = (Nota)session.createCriteria(Nota.class).add(Restrictions.eq("folio", t_factura.getText())).uniqueResult();
                    if(nota!=null)
                    {
                        this.tf.setText(this.cb.getSelectedItem().toString());
                        this.t_id.setText(""+nota.getIdNota());
                        this.t_fecha.setText(nota.getFechaFiscal());
                        this.t_rfc.setText(nota.getRfcReceptor());
                        this.t_social.setText(nota.getNombreReceptor());
                        this.t_fiscal.setText(nota.getFFiscal());
                        this.t_serie.setText(nota.getSerie());
                        this.t_folio.setText(nota.getFolio());
                        edo_factura=nota.getEstadoFactura();
                        try{
                            Concepto[] renglon=(Concepto[])nota.getConceptos().toArray(new Concepto[0]);
                            BigDecimal total=new BigDecimal("0.0");
                            for(int w=0; w<nota.getConceptos().size(); w++)
                            {
                                BigDecimal cantidad=new BigDecimal(""+renglon[w].getCantidad());
                                BigDecimal precio=new BigDecimal(""+renglon[w].getPrecio());
                                BigDecimal descuento=new BigDecimal(""+renglon[w].getDescuento());
                                descuento=descuento.divide(new BigDecimal("100"));
                                BigDecimal subtotal=cantidad.multiply(precio).setScale(2, BigDecimal.ROUND_HALF_UP);
                                BigDecimal resta=subtotal.multiply(descuento).setScale(2, BigDecimal.ROUND_HALF_UP);
                                total = total.add(subtotal.subtract(resta)).setScale(2, BigDecimal.ROUND_HALF_UP);
                            }
                            BigDecimal iva=new BigDecimal(""+nota.getIva());
                            iva=iva.divide(new BigDecimal("100"));
                            total=total.add(total.multiply(iva));
                            this.t_monto.setValue(Double.parseDouble(total.setScale(2, BigDecimal.ROUND_HALF_UP).toString()));
                            t_monto.commitEdit();
                        }catch(Exception e){}
                        this.c_estatus.setSelectedItem(nota.getEstatus());
                        edo=nota.getEstatus();
                        if(nota.getfEstatus()!=null)
                        {
                            DateFormat dateFormat;
                            try
                            {
                                Date fecha=nota.getfEstatus();
                                dateFormat = new SimpleDateFormat("dd-MM-yyyy");//YYYY-MM-DD HH:MM:SS
                                t_fecha_estatus.setText(dateFormat.format(fecha));
                            }catch(Exception E){this.t_fecha_estatus.setText("");}
                        }
                        else
                            this.t_fecha_estatus.setText("");
                        if(nota.getTexto()!=null)
                            this.t_estatus.setText(nota.getTexto());
                        else
                            this.t_estatus.setText("");
                        session.beginTransaction().commit();
                        estado(true);
                    }
                    else
                    {
                        t_factura.setText("");
                        borra_cajas();
                        estado(false);
                        t_factura.requestFocus();
                        nota=null;
                        JOptionPane.showMessageDialog(null, "La cuenta no existe!");
                    }
                }
            }catch(Exception e)
            {
                e.printStackTrace();
            }
            finally
            {
                if(session!=null)
                    if(session.isOpen())
                        session.close();
            }
        }
        else
        {
            t_factura.setText("");
            borra_cajas();
            estado(false);
            t_factura.requestFocus();
            factura=null;
        }
    }
    
    public void estado(boolean edo)
    {
        this.t_rfc.setEnabled(edo);
        this.t_social.setEnabled(edo);
        this.t_fiscal.setEnabled(edo);
        this.t_serie.setEnabled(edo);
        this.t_folio.setEnabled(edo);
        this.t_monto.setEnabled(edo);
        this.c_estatus.setEnabled(edo);
        this.t_fecha_estatus.setEnabled(edo);
        this.t_estatus.setEnabled(edo);
        this.b_guardar.setEnabled(edo);
    }
    public void borra_cajas()
    {
        this.tf.setText("");
        this.t_id.setText("");
        this.t_fecha.setText("");
        this.t_rfc.setText("");
        this.t_social.setText("");
        this.t_fiscal.setText("");
        this.t_serie.setText("");
        this.t_folio.setText("");
        this.t_monto.setText("");
        this.t_monto.setValue(0.0d);
        try{
            t_monto.commitEdit();
        }catch(Exception e){}
        this.c_estatus.setSelectedItem("POR COBRAR");
        this.t_fecha_estatus.setText("");
        this.t_estatus.setText("");
        
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        t_factura = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        t_id = new javax.swing.JLabel();
        t_fecha = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        t_fiscal = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        t_serie = new javax.swing.JTextField();
        t_folio = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        t_monto = new javax.swing.JFormattedTextField();
        jPanel2 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        t_rfc = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        t_social = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        c_estatus = new javax.swing.JComboBox();
        jScrollPane1 = new javax.swing.JScrollPane();
        t_estatus = new javax.swing.JTextArea();
        t_fecha_estatus = new javax.swing.JLabel();
        b_guardar = new javax.swing.JButton();
        tf = new javax.swing.JTextField();
        b_buscar = new javax.swing.JButton();
        cb = new javax.swing.JComboBox();

        setBackground(new java.awt.Color(254, 254, 254));
        setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "Estatus de Cuentas por Cobrar", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Arial", 1, 12))); // NOI18N

        jLabel1.setText("Folio:");

        t_factura.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                t_facturaActionPerformed(evt);
            }
        });
        t_factura.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                t_facturaKeyTyped(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(254, 254, 254));
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "Datos Generales", javax.swing.border.TitledBorder.RIGHT, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Arial", 1, 12))); // NOI18N

        jLabel2.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(61, 70, 191));
        jLabel2.setText("Id interno:");

        t_id.setForeground(new java.awt.Color(61, 70, 191));
        t_id.setText(" ");

        t_fecha.setForeground(new java.awt.Color(61, 70, 191));

        jLabel5.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(61, 70, 191));
        jLabel5.setText("Fecha:");

        jPanel4.setBackground(new java.awt.Color(254, 254, 254));
        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "Datos Fiscales", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 1, 12))); // NOI18N

        jLabel10.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(61, 70, 191));
        jLabel10.setText("Folio fiscal:");

        t_fiscal.setEditable(false);
        t_fiscal.setForeground(new java.awt.Color(61, 70, 191));
        t_fiscal.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        t_fiscal.setDisabledTextColor(new java.awt.Color(61, 70, 191));

        jLabel11.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(61, 70, 191));
        jLabel11.setText("Serie:");

        t_serie.setEditable(false);
        t_serie.setForeground(new java.awt.Color(61, 70, 191));
        t_serie.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        t_serie.setDisabledTextColor(new java.awt.Color(61, 70, 191));

        t_folio.setEditable(false);
        t_folio.setForeground(new java.awt.Color(61, 70, 191));
        t_folio.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        t_folio.setDisabledTextColor(new java.awt.Color(61, 70, 191));

        jLabel12.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(61, 70, 191));
        jLabel12.setText("Folio:");

        jLabel13.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(61, 70, 191));
        jLabel13.setText("Monto: $");

        t_monto.setEditable(false);
        t_monto.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        t_monto.setForeground(new java.awt.Color(61, 70, 191));
        t_monto.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.00"))));
        t_monto.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        t_monto.setText("0.00");
        t_monto.setToolTipText("subtotal antes de IVA");
        t_monto.setDisabledTextColor(new java.awt.Color(61, 70, 191));
        t_monto.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel10)
                        .addGap(18, 18, 18)
                        .addComponent(t_fiscal))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel11)
                        .addGap(18, 18, 18)
                        .addComponent(t_serie, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel12)
                        .addGap(18, 18, 18)
                        .addComponent(t_folio, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 215, Short.MAX_VALUE)
                        .addComponent(jLabel13)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(t_monto, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(t_fiscal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel13)
                        .addComponent(t_monto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel12)
                        .addComponent(t_folio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel11)
                        .addComponent(t_serie, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.setBackground(new java.awt.Color(254, 254, 254));
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "Receptor:", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Arial", 1, 12))); // NOI18N

        jLabel7.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(61, 70, 191));
        jLabel7.setText("RFC:");

        t_rfc.setEditable(false);
        t_rfc.setForeground(new java.awt.Color(61, 70, 191));
        t_rfc.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        t_rfc.setDisabledTextColor(new java.awt.Color(61, 70, 191));

        jLabel8.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(61, 70, 191));
        jLabel8.setText("Razon Social:");

        t_social.setEditable(false);
        t_social.setForeground(new java.awt.Color(61, 70, 191));
        t_social.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        t_social.setDisabledTextColor(new java.awt.Color(61, 70, 191));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel7)
                .addGap(18, 18, 18)
                .addComponent(t_rfc, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(t_social)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(t_rfc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8)
                    .addComponent(t_social, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel3.setBackground(new java.awt.Color(254, 254, 254));
        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "Estatus", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Arial", 1, 12))); // NOI18N

        c_estatus.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "POR COBRAR", "COBRADA", "INCOBRABLE", "CANCELADO", "PENDIENTE" }));
        c_estatus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                c_estatusActionPerformed(evt);
            }
        });

        t_estatus.setColumns(20);
        t_estatus.setRows(5);
        jScrollPane1.setViewportView(t_estatus);

        t_fecha_estatus.setForeground(new java.awt.Color(61, 70, 191));
        t_fecha_estatus.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        t_fecha_estatus.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        t_fecha_estatus.setMinimumSize(new java.awt.Dimension(0, 15));
        t_fecha_estatus.setOpaque(true);
        t_fecha_estatus.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                t_fecha_estatusMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(c_estatus, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(t_fecha_estatus, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(c_estatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(t_fecha_estatus, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        b_guardar.setBackground(new java.awt.Color(2, 135, 242));
        b_guardar.setIcon(new ImageIcon("imagenes/guardar.png"));
        b_guardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_guardarActionPerformed(evt);
            }
        });

        tf.setEditable(false);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(t_fecha)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(b_guardar, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap())
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(t_id)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(tf, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel2)
                        .addComponent(t_id))
                    .addComponent(tf, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(t_fecha)
                    .addComponent(jLabel5))
                .addGap(18, 18, 18)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(36, 36, 36)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(b_guardar, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(25, Short.MAX_VALUE))
        );

        b_buscar.setText("Buscar");
        b_buscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_buscarActionPerformed(evt);
            }
        });

        cb.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "FACTURA", "NOTA" }));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(t_factura, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cb, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(b_buscar)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(t_factura, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(b_buscar)
                    .addComponent(cb, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(21, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void b_guardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_guardarActionPerformed
        // TODO add your handling code here:
        Session session = HibernateUtil.getSessionFactory().openSession();
        try 
        {
            session.beginTransaction().begin();
            if(tf.getText().compareTo("FACTURA")==0)
            {
                factura = (Factura)session.get(Factura.class, Integer.parseInt(t_id.getText()));
                if(factura!=null)
                {
                    factura.setEstatus(c_estatus.getSelectedItem().toString());
                    if(t_fecha_estatus.getText().compareTo("")!=0)
                    {
                        String [] campos = t_fecha_estatus.getText().split("-");
                        Calendar calendario = Calendar.getInstance();
                        calendario.set(Calendar.MONTH, Integer.parseInt(campos[1])-1);
                        calendario.set(Calendar.YEAR, Integer.parseInt(campos[2]));
                        calendario.set(Calendar.DAY_OF_MONTH, Integer.parseInt(campos[0]));
                        factura.setfEstatus(calendario.getTime());
                    }
                    factura.setTexto(t_estatus.getText());
                    session.beginTransaction().commit();
                    estado(true);
                    JOptionPane.showMessageDialog(null, "Registro actualizado");
                }
                else
                {
                    t_factura.setText("");
                    borra_cajas();
                    estado(false);
                    t_factura.requestFocus();
                    factura=null;
                    JOptionPane.showMessageDialog(null, "La cuenta no existe!");
                }
            }
            else
            {
                nota = (Nota)session.get(Nota.class, Integer.parseInt(t_id.getText()));
                if(nota!=null)
                {
                    nota.setEstatus(c_estatus.getSelectedItem().toString());
                    if(t_fecha_estatus.getText().compareTo("")!=0)
                    {
                        String [] campos = t_fecha_estatus.getText().split("-");
                        Calendar calendario = Calendar.getInstance();
                        calendario.set(Calendar.MONTH, Integer.parseInt(campos[1])-1);
                        calendario.set(Calendar.YEAR, Integer.parseInt(campos[2]));
                        calendario.set(Calendar.DAY_OF_MONTH, Integer.parseInt(campos[0]));
                        nota.setfEstatus(calendario.getTime());
                    }
                    nota.setTexto(t_estatus.getText());
                    session.beginTransaction().commit();
                    estado(true);
                    JOptionPane.showMessageDialog(null, "Registro actualizado");
                }
                else
                {
                    t_factura.setText("");
                    borra_cajas();
                    estado(false);
                    t_factura.requestFocus();
                    factura=null;
                    JOptionPane.showMessageDialog(null, "La cuenta no existe!");
                }
            }
        }catch(Exception e)
        {
            JOptionPane.showMessageDialog(null, "error al actualizar los datos");
            e.printStackTrace();
        }
        finally
        {
            if(session!=null)
                if(session.isOpen())
                    session.close();
        }
    }//GEN-LAST:event_b_guardarActionPerformed

    private void t_facturaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_facturaKeyTyped
        // TODO add your handling code here:
        char car = evt.getKeyChar();
        if(t_factura.getSelectedText()!=null)
            t_factura.setText(t_factura.getText().replace(t_factura.getSelectedText(), ""));
        
        if(t_factura.getText().length()>=6)
        {
            evt.consume();
        }
        if((car<'0' || car>'9')) 
            evt.consume();
    }//GEN-LAST:event_t_facturaKeyTyped

    private void t_facturaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_t_facturaActionPerformed
        // TODO add your handling code here:
        buscaDato();
    }//GEN-LAST:event_t_facturaActionPerformed

    private void t_fecha_estatusMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_t_fecha_estatusMouseClicked
        // TODO add your handling code here:
        if(t_fecha_estatus.isEnabled()==true)
        {
            calendario cal =new calendario(new javax.swing.JFrame(), true);
            Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
            cal.setLocation((d.width/2)-(cal.getWidth()/2), (d.height/2)-(cal.getHeight()/2));
            cal.setVisible(true);
            Calendar miCalendario=cal.getReturnStatus();
            if(miCalendario!=null)
            {
                String dia=Integer.toString(miCalendario.get(Calendar.DATE));;
                String mes = Integer.toString(miCalendario.get(Calendar.MONTH)+1);
                String anio = Integer.toString(miCalendario.get(Calendar.YEAR));
                t_fecha_estatus.setText(dia+"-"+mes+"-"+anio);
                t_estatus.requestFocus();
            }
            else
                t_fecha_estatus.setText("");
        }
    }//GEN-LAST:event_t_fecha_estatusMouseClicked

    private void b_buscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_buscarActionPerformed
        // TODO add your handling code here:
        if(cb.getSelectedItem().toString().compareTo("FACTURA")==0)
        {
            buscaFactura obj = new buscaFactura(new javax.swing.JFrame(), true, this.sessionPrograma, this.usr, 0);
            obj.t_busca.requestFocus();
            Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
            obj.setLocation((d.width/2)-(obj.getWidth()/2), (d.height/2)-(obj.getHeight()/2));
            obj.setVisible(true);
            Factura fac=obj.getReturnStatus();
            if(fac!=null)
                t_factura.setText(""+fac.getFolio());
            obj=null;
            buscaDato();
        }
        else
        {
            buscaNota obj1 = new buscaNota(new javax.swing.JFrame(), true, this.sessionPrograma, this.usr, 0);
            obj1.t_busca.requestFocus();
            Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
            obj1.setLocation((d.width/2)-(obj1.getWidth()/2), (d.height/2)-(obj1.getHeight()/2));
            obj1.setVisible(true);
            Nota not=obj1.getReturnStatus();
            if(not!=null)
                t_factura.setText(""+not.getFolio());
            obj1=null;
            buscaDato();
        }
    }//GEN-LAST:event_b_buscarActionPerformed

    private void c_estatusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_c_estatusActionPerformed
        // TODO add your handling code here:
        if(t_id.getText().compareTo("")!=0)
        {
            if(this.t_fiscal.getText().compareTo("")!=0)
            {
                if(c_estatus.getSelectedItem().toString().compareTo("PENDIENTE")==0)
                {
                    JOptionPane.showMessageDialog(null, "El Estatus 'PENDIENTE' solo aplica a documentos sin timbrar!");
                    c_estatus.setSelectedItem(edo);
                }
                else
                {
                    if(c_estatus.getSelectedItem().toString().compareTo("CANCELADO")==0 && edo_factura.compareToIgnoreCase("FACTURADO")==0)
                    {
                        JOptionPane.showMessageDialog(null, "Debes cancelar el documentos antes de marcarlo como 'CANCELADO'!");
                        c_estatus.setSelectedItem(edo);
                    }
                }
            }
            else
            {
                if(c_estatus.getSelectedItem().toString().compareTo("PENDIENTE")!=0 && c_estatus.getSelectedItem().toString().compareTo("CANCELADO")!=0)
                {
                    JOptionPane.showMessageDialog(null, "El estatus '"+c_estatus.getSelectedItem().toString()+"' solo aplica a documentos Timbradas!");
                    c_estatus.setSelectedItem(edo);
                }
                else
                {
                    if(c_estatus.getSelectedItem().toString().compareTo("CANCELADO")==0 && edo_factura.compareToIgnoreCase("PENDIENTE")==0)
                    {
                        JOptionPane.showMessageDialog(null, "Debes cancelar el documentos antes de marcarlo como 'CANCELADO'!");
                        c_estatus.setSelectedItem(edo);
                    }
                }
            }
        }
    }//GEN-LAST:event_c_estatusActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton b_buscar;
    private javax.swing.JButton b_guardar;
    private javax.swing.JComboBox c_estatus;
    private javax.swing.JComboBox cb;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea t_estatus;
    private javax.swing.JTextField t_factura;
    private javax.swing.JLabel t_fecha;
    private javax.swing.JLabel t_fecha_estatus;
    private javax.swing.JTextField t_fiscal;
    private javax.swing.JTextField t_folio;
    private javax.swing.JLabel t_id;
    private javax.swing.JFormattedTextField t_monto;
    private javax.swing.JTextField t_rfc;
    private javax.swing.JTextField t_serie;
    private javax.swing.JTextField t_social;
    private javax.swing.JTextField tf;
    // End of variables declaration//GEN-END:variables
}
