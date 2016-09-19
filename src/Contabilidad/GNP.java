/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Contabilidad;

import Hibernate.Util.HibernateUtil;
import Hibernate.entidades.Concepto;
import Hibernate.entidades.Configuracion;
import Hibernate.entidades.Factura;
import Hibernate.entidades.Orden;
import Hibernate.entidades.OrdenExterna;
import Hibernate.entidades.Usuario;
import Servicios.EnviarCorreo;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Random;
import java.util.Vector;
import java.util.concurrent.ExecutionException;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import mx.com.fact.schema.ws.RequestTransaction;
import mx.com.fact.schema.ws.RequestTransactionResponse;
import mysuite.TAllowanceChargeType;
import mysuite.TComprobanteEx;
import mysuite.TConceptoEx;
import mysuite.TCountryCode;
import mysuite.TCurrencyCode;
import mysuite.TDescuentosYRecargos;
import mysuite.TDictionaries;
import mysuite.TDictionary;
import mysuite.TDiscountOrRecharge;
import mysuite.TDomicilioComercial;
import mysuite.TFactDocMX;
import mysuite.TFromTo;
import mysuite.TImpuestos;
import mysuite.TMTECabecera;
import mysuite.TMTEConcepto;
import mysuite.TMTEContactoEmisor;
import mysuite.TMTEContactoReceptor;
import mysuite.TMTEImportesAdicionales;
import mysuite.TMTEPoliza;
import mysuite.TMTEVehiculo;
import mysuite.TRelacionComercial;
import mysuite.TResumenDeDescuentosYRecargos;
import mysuite.TResumenDeImpuestos;
import mysuite.TSettlementType;
import mysuite.TSpecialServicesType;
import mysuite.TTax;
import mysuite.TTaxContext;
import mysuite.TTaxOperation;
import mysuite.TTextoLibre;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import Integral.Comprimir;
import Integral.FormatoTabla;
import Integral.Herramientas;
import Integral.Render1;
import Integral.calendario;
import Integral.numeroLetra;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.apache.poi.hssf.record.crypto.Biff8EncryptionKey;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

/**
 *
 * @author salvador
 */
public class GNP extends javax.swing.JDialog {

    String error="";
    private Usuario user;
    String sessionPrograma=null;
    FormatoTabla formato;
    Herramientas h;
    String abrir="";
    String idBuscar="";
    DefaultTableModel model;
    DefaultTableModel modeloFactura;
    int iva=0;
    String[] columnas = new String [] {
        "Id","Can","Med","Descripción","Costo c/u","Descuento","Total"
    };
    String[] columnas1 = new String [] {
        "RFC","UUID","SUCURSAL","SERIE","FOLIO","FECHA","MONTO","MONEDA"
    };
    boolean permiso=false;
    Factura factura;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm-Ss");
    public static final Factura RET_CANCEL =null;
    private Factura returnStatus = RET_CANCEL;
    String ruta="";
    /**
     * Creates new form QUALITAS
     */
    public GNP(java.awt.Frame parent, boolean modal, Usuario u, String ses, Factura fac) {
        super(parent, modal);
        initComponents();
        try{
            FileReader fil = new FileReader("config.txt");
            BufferedReader b = new BufferedReader(fil);
            if((ruta = b.readLine())==null)
                ruta="";
            b.close();
            fil.close();
            fil=null;
            b=null;
        }catch(Exception e){};
        factura = fac;
        user=u;
        sessionPrograma=ses;
        formato =new FormatoTabla();
        borra_cajas();
        habilita(true, false);
        //consulta();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel27 = new javax.swing.JLabel();
        consulta = new javax.swing.JDialog();
        jLabel43 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        t_facturas = new javax.swing.JTable();
        jPanel15 = new javax.swing.JPanel();
        b_cancelar = new javax.swing.JButton();
        b_descargar = new javax.swing.JButton();
        aviso = new javax.swing.JFileChooser();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        b_salir = new javax.swing.JButton();
        b_generar = new javax.swing.JButton();
        b_actualizar = new javax.swing.JButton();
        jLabel29 = new javax.swing.JLabel();
        progreso = new javax.swing.JProgressBar();
        contenedor = new javax.swing.JTabbedPane();
        jPanel3 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        t_social = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        t_rfc = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        t_calle = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        t_no_exterior = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        t_cp = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        t_colonia = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        t_municipio = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        c_estado = new javax.swing.JComboBox();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        c_pais = new javax.swing.JComboBox();
        jLabel19 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        l_subtotal = new javax.swing.JLabel();
        t_subtotal = new javax.swing.JFormattedTextField();
        t_iva = new javax.swing.JFormattedTextField();
        l_iva = new javax.swing.JLabel();
        l_total = new javax.swing.JLabel();
        t_total = new javax.swing.JFormattedTextField();
        t_iva1 = new javax.swing.JFormattedTextField();
        jLabel20 = new javax.swing.JLabel();
        t_metodo_pago = new javax.swing.JTextField();
        jLabel21 = new javax.swing.JLabel();
        t_cuenta_pago = new javax.swing.JTextField();
        jLabel22 = new javax.swing.JLabel();
        t_forma_pago = new javax.swing.JTextField();
        jLabel23 = new javax.swing.JLabel();
        c_moneda = new javax.swing.JComboBox();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        t_tipo_cambio = new javax.swing.JFormattedTextField();
        t_descuento = new javax.swing.JFormattedTextField();
        l_iva1 = new javax.swing.JLabel();
        b_menos = new javax.swing.JButton();
        b_mas = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        t_datos = new javax.swing.JTable();
        jPanel5 = new javax.swing.JPanel();
        jPanel10 = new javax.swing.JPanel();
        c_tipo = new javax.swing.JComboBox();
        jLabel45 = new javax.swing.JLabel();
        jLabel47 = new javax.swing.JLabel();
        jLabel48 = new javax.swing.JLabel();
        t_codigo = new javax.swing.JTextField();
        jLabel66 = new javax.swing.JLabel();
        jLabel69 = new javax.swing.JLabel();
        t_no_rep = new javax.swing.JTextField();
        jLabel70 = new javax.swing.JLabel();
        jLabel71 = new javax.swing.JLabel();
        t_numero = new javax.swing.JTextField();
        jLabel72 = new javax.swing.JLabel();
        jLabel73 = new javax.swing.JLabel();
        jLabel74 = new javax.swing.JLabel();
        jLabel75 = new javax.swing.JLabel();
        t_siniestro = new javax.swing.JTextField();
        jLabel76 = new javax.swing.JLabel();
        c_tipo_cliente = new javax.swing.JComboBox();
        jLabel88 = new javax.swing.JLabel();
        t_contratante = new javax.swing.JTextField();
        jLabel89 = new javax.swing.JLabel();
        jLabel90 = new javax.swing.JLabel();
        t_codigo_asegurado = new javax.swing.JTextField();
        jLabel91 = new javax.swing.JLabel();
        jPanel11 = new javax.swing.JPanel();
        jLabel31 = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        t_nombre_emisor = new javax.swing.JTextField();
        jLabel33 = new javax.swing.JLabel();
        t_correo_emisor = new javax.swing.JTextField();
        jLabel34 = new javax.swing.JLabel();
        t_tel_emisor = new javax.swing.JTextField();
        jLabel35 = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();
        c_tipo_emisor = new javax.swing.JComboBox();
        jLabel65 = new javax.swing.JLabel();
        jLabel85 = new javax.swing.JLabel();
        t_descripcion = new javax.swing.JTextField();
        jLabel64 = new javax.swing.JLabel();
        jLabel87 = new javax.swing.JLabel();
        jPanel12 = new javax.swing.JPanel();
        jLabel37 = new javax.swing.JLabel();
        jLabel38 = new javax.swing.JLabel();
        t_nombre_receptor = new javax.swing.JTextField();
        jLabel39 = new javax.swing.JLabel();
        t_correo_receptor = new javax.swing.JTextField();
        jLabel40 = new javax.swing.JLabel();
        t_tel_receptor = new javax.swing.JTextField();
        jLabel41 = new javax.swing.JLabel();
        jLabel42 = new javax.swing.JLabel();
        jLabel44 = new javax.swing.JLabel();
        c_tipo_receptor = new javax.swing.JComboBox();
        jLabel84 = new javax.swing.JLabel();
        t_proveedor = new javax.swing.JTextField();
        jLabel63 = new javax.swing.JLabel();
        jLabel67 = new javax.swing.JLabel();
        t_deducible = new javax.swing.JFormattedTextField();
        jPanel13 = new javax.swing.JPanel();
        jLabel49 = new javax.swing.JLabel();
        jLabel50 = new javax.swing.JLabel();
        t_modelo_vehiculo = new javax.swing.JTextField();
        jLabel51 = new javax.swing.JLabel();
        t_color_vehiculo = new javax.swing.JTextField();
        t_marca_vehiculo = new javax.swing.JTextField();
        jLabel52 = new javax.swing.JLabel();
        jLabel53 = new javax.swing.JLabel();
        jLabel54 = new javax.swing.JLabel();
        jLabel55 = new javax.swing.JLabel();
        t_anio_vehiculo = new javax.swing.JTextField();
        jLabel56 = new javax.swing.JLabel();
        t_serie_vehiculo = new javax.swing.JTextField();
        jLabel57 = new javax.swing.JLabel();
        jLabel58 = new javax.swing.JLabel();
        t_placas_vehiculo = new javax.swing.JTextField();
        jLabel59 = new javax.swing.JLabel();
        jLabel60 = new javax.swing.JLabel();
        jLabel61 = new javax.swing.JLabel();
        jLabel62 = new javax.swing.JLabel();
        t_motor_vehiculo = new javax.swing.JTextField();
        c_tipo_vehiculo = new javax.swing.JComboBox();
        jLabel77 = new javax.swing.JLabel();
        jLabel68 = new javax.swing.JLabel();
        t_medio_pago = new javax.swing.JTextField();
        jLabel92 = new javax.swing.JLabel();
        jLabel93 = new javax.swing.JLabel();
        jLabel94 = new javax.swing.JLabel();
        jLabel95 = new javax.swing.JLabel();
        jLabel96 = new javax.swing.JLabel();
        jLabel97 = new javax.swing.JLabel();
        t_condiciones_pago = new javax.swing.JTextField();
        jLabel98 = new javax.swing.JLabel();
        b_fecha_1 = new javax.swing.JButton();
        t_fecha_1 = new javax.swing.JTextField();
        b_fecha_2 = new javax.swing.JButton();
        t_fecha_2 = new javax.swing.JTextField();
        jPanel14 = new javax.swing.JPanel();
        jLabel79 = new javax.swing.JLabel();
        jLabel80 = new javax.swing.JLabel();
        jLabel81 = new javax.swing.JLabel();
        t_serie_factura = new javax.swing.JTextField();
        t_folio_factura = new javax.swing.JTextField();
        jLabel82 = new javax.swing.JLabel();
        jLabel83 = new javax.swing.JLabel();
        t_xml = new javax.swing.JTextField();
        t_pdf = new javax.swing.JTextField();
        b_xml = new javax.swing.JButton();
        b_pdf = new javax.swing.JButton();
        b_email = new javax.swing.JButton();
        jLabel86 = new javax.swing.JLabel();
        t_uuid_factura = new javax.swing.JTextField();

        jLabel27.setText("*");

        consulta.setAlwaysOnTop(true);
        consulta.setModalityType(java.awt.Dialog.ModalityType.DOCUMENT_MODAL);

        jLabel43.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel43.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel43.setText("Se encontraron las siguentes facturas timbradas y activas en el servidor SAT");
        consulta.getContentPane().add(jLabel43, java.awt.BorderLayout.PAGE_START);

        t_facturas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "RFC", "UUID", "SUCURSAL", "SERIE", "FOLIO", "FECHA", "MONTO", "MONEDA"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Double.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        t_facturas.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        t_facturas.getTableHeader().setReorderingAllowed(false);
        jScrollPane2.setViewportView(t_facturas);

        consulta.getContentPane().add(jScrollPane2, java.awt.BorderLayout.CENTER);

        b_cancelar.setText("Cancelar factura");
        b_cancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_cancelarActionPerformed(evt);
            }
        });

        b_descargar.setText("Descargar PDF");
        b_descargar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_descargarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel15Layout.createSequentialGroup()
                .addGap(0, 579, Short.MAX_VALUE)
                .addComponent(b_cancelar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(b_descargar))
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(b_cancelar)
                .addComponent(b_descargar))
        );

        consulta.getContentPane().add(jPanel15, java.awt.BorderLayout.PAGE_END);

        aviso.setDialogType(javax.swing.JFileChooser.SAVE_DIALOG);
        aviso.setDialogTitle("Examinar");

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setModalityType(java.awt.Dialog.ModalityType.APPLICATION_MODAL);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(254, 254, 254));

        jLabel1.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel1.setText("Emisión de Facturas a GNP");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(355, 355, 355)
                .addComponent(jLabel1)
                .addContainerGap(441, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING)
        );

        getContentPane().add(jPanel1, java.awt.BorderLayout.PAGE_START);

        jPanel2.setBackground(new java.awt.Color(254, 254, 254));

        b_salir.setBackground(new java.awt.Color(2, 135, 242));
        b_salir.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        b_salir.setForeground(new java.awt.Color(254, 254, 254));
        b_salir.setIcon(new ImageIcon("imagenes/salir1.png"));
        b_salir.setText("Salir");
        b_salir.setToolTipText("Cerrar la ventana");
        b_salir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_salirActionPerformed(evt);
            }
        });

        b_generar.setBackground(new java.awt.Color(2, 135, 242));
        b_generar.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        b_generar.setForeground(new java.awt.Color(254, 254, 254));
        b_generar.setIcon(new ImageIcon("imagenes/nube.png"));
        b_generar.setText("Generar comprobante");
        b_generar.setToolTipText("Genera el timbre fiscal");
        b_generar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_generarActionPerformed(evt);
            }
        });

        b_actualizar.setBackground(new java.awt.Color(2, 135, 242));
        b_actualizar.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        b_actualizar.setForeground(new java.awt.Color(254, 254, 254));
        b_actualizar.setIcon(new ImageIcon("imagenes/guardar-documento.png"));
        b_actualizar.setText("Actualizar");
        b_actualizar.setToolTipText("Actualizar la información de la factura en la base de datos");
        b_actualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_actualizarActionPerformed(evt);
            }
        });

        jLabel29.setText("Los campos marcados con \"*\" son obligatorios");

        progreso.setString("Listo");
        progreso.setStringPainted(true);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(progreso, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel29)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 144, Short.MAX_VALUE)
                .addComponent(b_actualizar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(b_generar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(b_salir))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(progreso, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jLabel29))
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(b_salir)
                .addComponent(b_generar)
                .addComponent(b_actualizar))
        );

        getContentPane().add(jPanel2, java.awt.BorderLayout.PAGE_END);

        contenedor.setBackground(new java.awt.Color(254, 254, 254));
        contenedor.setOpaque(true);

        jPanel3.setBackground(new java.awt.Color(254, 254, 254));
        jPanel3.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));

        jPanel6.setBackground(new java.awt.Color(254, 254, 254));
        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 1, 0, new java.awt.Color(0, 0, 0)), "Identificación", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.ABOVE_TOP, new java.awt.Font("Arial", 1, 12))); // NOI18N

        jLabel2.setText("Razón Social:");

        t_social.setToolTipText("Razón social del receptor");
        t_social.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        t_social.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                t_socialKeyTyped(evt);
            }
        });

        jLabel3.setText("RFC:");

        t_rfc.setToolTipText("RFC del receptor");
        t_rfc.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        t_rfc.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                t_rfcKeyTyped(evt);
            }
        });

        jLabel4.setText("*");

        jLabel5.setText("*");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(t_social, javax.swing.GroupLayout.PREFERRED_SIZE, 620, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(7, 7, 7)
                .addComponent(jLabel5)
                .addGap(18, 18, 18)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(t_rfc, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(4, 4, 4)
                .addComponent(jLabel4)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap(20, Short.MAX_VALUE)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(t_social, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(t_rfc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5))
                .addContainerGap())
        );

        jPanel7.setBackground(new java.awt.Color(254, 254, 254));
        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 1, 0, new java.awt.Color(0, 0, 0)), "Domicilio Fiscal", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.ABOVE_TOP, new java.awt.Font("Arial", 1, 12))); // NOI18N

        jLabel6.setText("Calle:");

        t_calle.setToolTipText("Calle del receptor");
        t_calle.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        t_calle.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                t_calleKeyTyped(evt);
            }
        });

        jLabel7.setText("*");

        jLabel8.setText("Número Ext:");

        t_no_exterior.setToolTipText("Número exterior del receptor");
        t_no_exterior.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        t_no_exterior.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                t_no_exteriorKeyTyped(evt);
            }
        });

        jLabel9.setText("*");

        jLabel10.setText("CP:");

        t_cp.setToolTipText("Codigo postal del receptor");
        t_cp.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        t_cp.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                t_cpKeyTyped(evt);
            }
        });

        jLabel11.setText("*");

        t_colonia.setToolTipText("Colonia del receptor");
        t_colonia.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        t_colonia.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                t_coloniaKeyTyped(evt);
            }
        });

        jLabel12.setText("*");

        jLabel13.setText("Col:");

        jLabel14.setText("Municipio:");

        t_municipio.setToolTipText("Municipio del receptor");
        t_municipio.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        t_municipio.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                t_municipioKeyTyped(evt);
            }
        });

        jLabel15.setText("*");

        jLabel16.setText("Edo:");

        c_estado.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "AGUASCALIENTES", "BAJA CALIFORNIA", "BAJA CALIFORNIA SUR", "CAMPECHE", "CHIAPAS", "CHIHUAHUA", "COAHUILA", "COLIMA", "DISTRITO FEDERAL", "DURANGO", "ESTADO DE MEXICO", "GUANAJUATO", "GUERRERO", "HIDALGO", "JALISCO", "MICHOACAN", "MORELOS", "NAYARIT", "NUEVO LEON", "OAXACA", "PUEBLA", "QUERETARO", "QUINTANA ROO", "SAN LUIS POTOSI", "SINALOA", "SONORA", "TABASCO", "TAMAULIPAS", "TLAXCALA", "VERACRUZ", "YUCATAN", "ZACATECAS" }));
        c_estado.setToolTipText("Estado del receptor");

        jLabel17.setText("Pais");

        jLabel18.setText("*");

        c_pais.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "MEXICO" }));
        c_pais.setSelectedItem("MX");
        c_pais.setToolTipText("Pais dewl receptor");

        jLabel19.setText("*");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGap(9, 9, 9)
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(t_calle, javax.swing.GroupLayout.PREFERRED_SIZE, 478, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(3, 3, 3)
                        .addComponent(jLabel7)
                        .addGap(37, 37, 37)
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(t_no_exterior, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(3, 3, 3)
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(t_cp, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(1, 1, 1)
                        .addComponent(jLabel11))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addComponent(jLabel16)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(c_estado, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(2, 2, 2)
                                .addComponent(jLabel18)
                                .addGap(36, 36, 36)
                                .addComponent(jLabel17)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(c_pais, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(1, 1, 1)
                                .addComponent(jLabel19)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addComponent(jLabel13)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(t_colonia, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(3, 3, 3)
                                .addComponent(jLabel12)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel14)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(t_municipio, javax.swing.GroupLayout.PREFERRED_SIZE, 332, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(3, 3, 3)
                                .addComponent(jLabel15)))))
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(t_calle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7)
                    .addComponent(jLabel8)
                    .addComponent(t_no_exterior, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9)
                    .addComponent(jLabel10)
                    .addComponent(t_cp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(t_colonia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12)
                    .addComponent(jLabel14)
                    .addComponent(t_municipio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel15))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16)
                    .addComponent(c_estado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel17)
                    .addComponent(jLabel18)
                    .addComponent(c_pais, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel19))
                .addContainerGap(28, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(329, Short.MAX_VALUE))
        );

        contenedor.addTab("Receptor", jPanel3);

        jPanel4.setBackground(new java.awt.Color(254, 254, 254));
        jPanel4.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        jPanel4.setLayout(new java.awt.BorderLayout());

        jPanel8.setBackground(new java.awt.Color(2, 135, 242));

        jPanel9.setBackground(new java.awt.Color(2, 135, 242));
        jPanel9.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        l_subtotal.setFont(new java.awt.Font("Arial", 0, 9)); // NOI18N
        l_subtotal.setForeground(new java.awt.Color(255, 255, 255));
        l_subtotal.setText("Subtotal:");
        jPanel9.add(l_subtotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 4, -1, -1));

        t_subtotal.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        t_subtotal.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.00"))));
        t_subtotal.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        t_subtotal.setText("0.00");
        t_subtotal.setToolTipText("Subtotal antes de iva");
        t_subtotal.setEnabled(false);
        t_subtotal.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        jPanel9.add(t_subtotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(85, 0, 88, -1));

        t_iva.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        t_iva.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.00"))));
        t_iva.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        t_iva.setText("0.00");
        t_iva.setToolTipText("Monto de iva");
        t_iva.setEnabled(false);
        t_iva.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        jPanel9.add(t_iva, new org.netbeans.lib.awtextra.AbsoluteConstraints(85, 20, 88, -1));

        l_iva.setFont(new java.awt.Font("Arial", 0, 9)); // NOI18N
        l_iva.setForeground(new java.awt.Color(255, 255, 255));
        l_iva.setText("%  IVA:");
        jPanel9.add(l_iva, new org.netbeans.lib.awtextra.AbsoluteConstraints(43, 23, -1, -1));

        l_total.setFont(new java.awt.Font("Arial", 0, 9)); // NOI18N
        l_total.setForeground(new java.awt.Color(255, 255, 255));
        l_total.setText("Total:");
        jPanel9.add(l_total, new org.netbeans.lib.awtextra.AbsoluteConstraints(47, 45, -1, -1));

        t_total.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        t_total.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.00"))));
        t_total.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        t_total.setText("0.00");
        t_total.setToolTipText("Total Neto");
        t_total.setEnabled(false);
        t_total.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        jPanel9.add(t_total, new org.netbeans.lib.awtextra.AbsoluteConstraints(85, 41, 88, -1));

        t_iva1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        t_iva1.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0"))));
        t_iva1.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        t_iva1.setToolTipText("Total de IVA");
        t_iva1.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        t_iva1.setValue(16);
        t_iva1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                t_iva1FocusLost(evt);
            }
        });
        t_iva1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                t_iva1ActionPerformed(evt);
            }
        });
        t_iva1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                t_iva1KeyTyped(evt);
            }
        });
        jPanel9.add(t_iva1, new org.netbeans.lib.awtextra.AbsoluteConstraints(1, 20, 40, -1));

        jLabel20.setForeground(new java.awt.Color(254, 254, 254));
        jLabel20.setText("Metodo de Pago:");

        t_metodo_pago.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        t_metodo_pago.setText("03");
        t_metodo_pago.setToolTipText("Metodo de pago del monto de la factura");
        t_metodo_pago.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        t_metodo_pago.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                t_metodo_pagoKeyTyped(evt);
            }
        });

        jLabel21.setForeground(new java.awt.Color(254, 254, 254));
        jLabel21.setText("Cuenta de Pago:");

        t_cuenta_pago.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        t_cuenta_pago.setToolTipText("Número de cuenta en caso de que el pago sea por depósito bancario");
        t_cuenta_pago.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        t_cuenta_pago.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                t_cuenta_pagoKeyTyped(evt);
            }
        });

        jLabel22.setForeground(new java.awt.Color(254, 254, 254));
        jLabel22.setText("Forma de Pago:");

        t_forma_pago.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        t_forma_pago.setText("PAGO EN UNA SOLA EXHIBICION");
        t_forma_pago.setToolTipText("Forama de pago de la factura");
        t_forma_pago.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        t_forma_pago.setEnabled(false);

        jLabel23.setForeground(new java.awt.Color(254, 254, 254));
        jLabel23.setText("Moneda:");

        c_moneda.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        c_moneda.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "MXN", "USD" }));
        c_moneda.setToolTipText("Tipo de moneda del monto");

        jLabel24.setForeground(new java.awt.Color(254, 254, 254));
        jLabel24.setText("Factor de Cambio");

        jLabel25.setForeground(new java.awt.Color(254, 254, 254));
        jLabel25.setText("*");

        jLabel26.setForeground(new java.awt.Color(254, 254, 254));
        jLabel26.setText("*");

        jLabel28.setForeground(new java.awt.Color(254, 254, 254));
        jLabel28.setText("*");

        t_tipo_cambio.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        t_tipo_cambio.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        t_tipo_cambio.setToolTipText("Factor de cambio en caso de ser moneda extranjera");
        t_tipo_cambio.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N

        t_descuento.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        t_descuento.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("0.00"))));
        t_descuento.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        t_descuento.setToolTipText("Agregar descuento global");
        t_descuento.setEnabled(false);
        t_descuento.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        t_descuento.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                t_descuentoFocusLost(evt);
            }
        });

        l_iva1.setFont(new java.awt.Font("Arial", 0, 9)); // NOI18N
        l_iva1.setForeground(new java.awt.Color(255, 255, 255));
        l_iva1.setText("Agregar Descuento:");

        b_menos.setBackground(new java.awt.Color(2, 135, 242));
        b_menos.setForeground(new java.awt.Color(255, 255, 255));
        b_menos.setIcon(new ImageIcon("imagenes/boton_menos.png"));
        b_menos.setToolTipText("Eliminar un concepto");
        b_menos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_menosActionPerformed(evt);
            }
        });

        b_mas.setBackground(new java.awt.Color(2, 135, 242));
        b_mas.setForeground(new java.awt.Color(255, 255, 255));
        b_mas.setIcon(new ImageIcon("imagenes/boton_mas.png"));
        b_mas.setToolTipText("Agrega un concepto");
        b_mas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_masActionPerformed(evt);
            }
        });

        jButton1.setText("Exportar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(b_mas, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(b_menos, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel20)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(t_metodo_pago, javax.swing.GroupLayout.DEFAULT_SIZE, 221, Short.MAX_VALUE)
                        .addGap(2, 2, 2)
                        .addComponent(jLabel25)
                        .addGap(45, 45, 45)
                        .addComponent(jLabel22)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 9, Short.MAX_VALUE)
                        .addComponent(t_forma_pago, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(284, 284, 284))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(jLabel21)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(t_cuenta_pago, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel23)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(c_moneda, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel24)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(t_tipo_cambio, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel28)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(l_iva1, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(t_descuento, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(b_mas, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(b_menos, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel20)
                        .addComponent(t_metodo_pago, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel25))
                    .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(t_forma_pago, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel26))
                    .addComponent(jLabel22))
                .addGap(7, 7, 7)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(c_moneda, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel23))
                    .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel24)
                        .addComponent(jLabel28)
                        .addComponent(t_tipo_cambio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(l_iva1)
                        .addComponent(t_descuento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(t_cuenta_pago, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel21)
                        .addComponent(jButton1)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel4.add(jPanel8, java.awt.BorderLayout.PAGE_END);

        t_datos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "id", "Cant", "Med", "Descripción", "Costo c/u", "Descuento", "Total"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Object.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, true, true, true, false
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
        jScrollPane1.setViewportView(t_datos);

        jPanel4.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        contenedor.addTab("Productos y servicios", jPanel4);

        jPanel5.setBackground(new java.awt.Color(254, 254, 254));
        jPanel5.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        jPanel5.setForeground(new java.awt.Color(254, 254, 254));

        jPanel10.setBackground(new java.awt.Color(254, 254, 254));
        jPanel10.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 1, 0, new java.awt.Color(0, 0, 0)), "Poliza"), "Poliza", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 1, 10))); // NOI18N

        c_tipo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "DAÑOS", "AUTOS" }));
        c_tipo.setToolTipText("Tipo de poliza");

        jLabel45.setText("Tipo:");

        jLabel47.setText("*");

        jLabel48.setText("Código Int. Emisor:");

        t_codigo.setToolTipText("Campo de 5 posiciones");
        t_codigo.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        t_codigo.setEnabled(false);
        t_codigo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                t_codigoKeyTyped(evt);
            }
        });

        jLabel66.setText("*");

        jLabel69.setText("*");

        t_no_rep.setToolTipText("Número de reporte");
        t_no_rep.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        t_no_rep.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                t_no_repKeyTyped(evt);
            }
        });

        jLabel70.setText("no Rep.");

        jLabel71.setText("*");

        t_numero.setToolTipText("Número de poliza");
        t_numero.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        t_numero.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                t_numeroKeyTyped(evt);
            }
        });

        jLabel72.setText("Número:");

        jLabel73.setText("Tipo cliente:");

        jLabel74.setText("*");

        jLabel75.setText("Siniestro:");

        t_siniestro.setToolTipText("Número de siniestro");
        t_siniestro.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        t_siniestro.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                t_siniestroKeyTyped(evt);
            }
        });

        jLabel76.setText("*");

        c_tipo_cliente.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "ASEGURADO", "TERCERO" }));
        c_tipo_cliente.setToolTipText("Tipo de cliente");

        jLabel88.setText("contratante:");

        t_contratante.setToolTipText("Nombre del contratante");
        t_contratante.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        t_contratante.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                t_contratanteKeyTyped(evt);
            }
        });

        jLabel89.setText("*");

        jLabel90.setText("codigo asegurado:");

        t_codigo_asegurado.setToolTipText("Codigo del asegurado");
        t_codigo_asegurado.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        t_codigo_asegurado.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                t_codigo_aseguradoKeyTyped(evt);
            }
        });

        jLabel91.setText("*");

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addComponent(jLabel45)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(c_tipo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel47)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel72)
                        .addGap(5, 5, 5)
                        .addComponent(t_numero, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel71)
                        .addGap(26, 26, 26)
                        .addComponent(jLabel73)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(c_tipo_cliente, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel74)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel70)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(t_no_rep, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel69))
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addComponent(jLabel75)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(t_siniestro, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel76)
                        .addGap(5, 5, 5)
                        .addComponent(jLabel88)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(t_contratante, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel89)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel90)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(t_codigo_asegurado, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel91)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel48)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(t_codigo, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel66)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel45)
                        .addComponent(c_tipo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel47)
                        .addComponent(jLabel72)
                        .addComponent(t_numero, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel71))
                    .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel10Layout.createSequentialGroup()
                            .addGap(6, 6, 6)
                            .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel73)
                                .addComponent(jLabel74)
                                .addComponent(jLabel70)
                                .addComponent(t_no_rep, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel69)))
                        .addComponent(c_tipo_cliente, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel48)
                        .addComponent(t_codigo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel66))
                    .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel75)
                        .addComponent(t_siniestro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel76)
                        .addComponent(jLabel88)
                        .addComponent(t_contratante, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel89)
                        .addComponent(jLabel90)
                        .addComponent(t_codigo_asegurado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel91)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel11.setBackground(new java.awt.Color(254, 254, 254));
        jPanel11.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 1, 0, new java.awt.Color(0, 0, 0)), "Contacto Emisor", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.ABOVE_TOP, new java.awt.Font("Arial", 1, 10))); // NOI18N

        jLabel31.setText("Tipo:");

        jLabel32.setText("Nombre:");

        t_nombre_emisor.setToolTipText("Nombre del contacto emisor");
        t_nombre_emisor.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        t_nombre_emisor.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                t_nombre_emisorKeyTyped(evt);
            }
        });

        jLabel33.setText("Correo:");

        t_correo_emisor.setToolTipText("Correo del contacto emisor");
        t_correo_emisor.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        t_correo_emisor.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                t_correo_emisorKeyTyped(evt);
            }
        });

        jLabel34.setText("Tel:");

        t_tel_emisor.setToolTipText("Telefono del contacto emisor");
        t_tel_emisor.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        t_tel_emisor.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                t_tel_emisorKeyTyped(evt);
            }
        });

        jLabel35.setText("*");

        jLabel36.setText("*");

        c_tipo_emisor.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "MATRIZ", "VENDEDOR", "EMPLEADO", "SUCURSAL", "AGENCIA", "DEPARTAMENTO", "TRANSPORTISTA", "DISTRUBUIDOR", "OTRO" }));
        c_tipo_emisor.setToolTipText("Tipo de contacto del emisor");

        jLabel65.setText("*");

        jLabel85.setText("*");

        t_descripcion.setToolTipText("Descripción corta del contacto emisor");
        t_descripcion.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        t_descripcion.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                t_descripcionKeyTyped(evt);
            }
        });

        jLabel64.setText("Descripción");

        jLabel87.setText("*");

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel33)
                    .addComponent(jLabel31))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addComponent(c_tipo_emisor, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(5, 5, 5)
                        .addComponent(jLabel65)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel32)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(t_nombre_emisor, javax.swing.GroupLayout.PREFERRED_SIZE, 498, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel35))
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addComponent(t_correo_emisor, javax.swing.GroupLayout.PREFERRED_SIZE, 351, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel36)
                        .addGap(31, 31, 31)
                        .addComponent(jLabel34)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(t_tel_emisor, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel85)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel64)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(t_descripcion, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel87)))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel31)
                    .addComponent(jLabel32)
                    .addComponent(t_nombre_emisor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel35)
                    .addComponent(c_tipo_emisor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel65))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel64)
                        .addComponent(t_descripcion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel87))
                    .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel33)
                        .addComponent(t_correo_emisor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel34)
                        .addComponent(t_tel_emisor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel36)
                        .addComponent(jLabel85)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel12.setBackground(new java.awt.Color(254, 254, 254));
        jPanel12.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 1, 0, new java.awt.Color(0, 0, 0)), "Contacto Receptor", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.ABOVE_TOP, new java.awt.Font("Arial", 1, 10))); // NOI18N

        jLabel37.setText("Tipo:");

        jLabel38.setText("Nombre:");

        t_nombre_receptor.setToolTipText("Nombre del contacto receptor");
        t_nombre_receptor.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        t_nombre_receptor.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                t_nombre_receptorKeyTyped(evt);
            }
        });

        jLabel39.setText("Correo:");

        t_correo_receptor.setToolTipText("Correo del contacto receptor");
        t_correo_receptor.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        t_correo_receptor.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                t_correo_receptorKeyTyped(evt);
            }
        });

        jLabel40.setText("Tel:");

        t_tel_receptor.setToolTipText("Teléfono del contacto receptor");
        t_tel_receptor.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        t_tel_receptor.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                t_tel_receptorKeyTyped(evt);
            }
        });

        jLabel41.setText("*");

        jLabel42.setText("*");

        jLabel44.setText("*");

        c_tipo_receptor.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "COORDINADOR" }));
        c_tipo_receptor.setToolTipText("Tipo de contacto del receptor");

        jLabel84.setText("*");

        t_proveedor.setToolTipText("Número que nos fue asignado por el receptor");
        t_proveedor.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        t_proveedor.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                t_proveedorKeyTyped(evt);
            }
        });

        jLabel63.setText("No Proveedor");

        jLabel67.setText("Deducible:");

        t_deducible.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        t_deducible.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        t_deducible.setToolTipText("Monto del deducible");
        t_deducible.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel39)
                    .addComponent(jLabel37))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addComponent(c_tipo_receptor, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(6, 6, 6)
                        .addComponent(jLabel84)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel38)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(t_nombre_receptor, javax.swing.GroupLayout.PREFERRED_SIZE, 363, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addComponent(t_correo_receptor, javax.swing.GroupLayout.PREFERRED_SIZE, 331, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel42)
                        .addGap(24, 24, 24)
                        .addComponent(jLabel40)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(t_tel_receptor)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addComponent(jLabel44)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel67)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(t_deducible, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addComponent(jLabel41)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel63)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(t_proveedor, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(33, 33, 33))
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel63)
                        .addComponent(t_proveedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel37)
                        .addComponent(jLabel38)
                        .addComponent(t_nombre_receptor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel41))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(c_tipo_receptor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel84)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel67)
                        .addComponent(t_deducible, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel39)
                        .addComponent(t_correo_receptor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel40)
                        .addComponent(t_tel_receptor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel42)
                        .addComponent(jLabel44)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel13.setBackground(new java.awt.Color(254, 254, 254));
        jPanel13.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 1, 0, new java.awt.Color(0, 0, 0)), "Vehículo", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.ABOVE_TOP, new java.awt.Font("Arial", 1, 10))); // NOI18N

        jLabel49.setText("Tipo:");

        jLabel50.setText("Modelo:");

        t_modelo_vehiculo.setToolTipText("Modelo de la unidad");
        t_modelo_vehiculo.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        t_modelo_vehiculo.setEnabled(false);

        jLabel51.setText("Color:");

        t_color_vehiculo.setToolTipText("Color de la unidad");
        t_color_vehiculo.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        t_color_vehiculo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                t_color_vehiculoKeyTyped(evt);
            }
        });

        t_marca_vehiculo.setToolTipText("Marca de la unidad");
        t_marca_vehiculo.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        t_marca_vehiculo.setEnabled(false);

        jLabel52.setText("Marca:");

        jLabel53.setText("*");

        jLabel54.setText("*");

        jLabel55.setText("*");

        t_anio_vehiculo.setToolTipText("Año de fabricación de la unidad");
        t_anio_vehiculo.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        t_anio_vehiculo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                t_anio_vehiculoKeyTyped(evt);
            }
        });

        jLabel56.setText("Año:");

        t_serie_vehiculo.setToolTipText("Número de serie de la unidad");
        t_serie_vehiculo.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        t_serie_vehiculo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                t_serie_vehiculoKeyTyped(evt);
            }
        });

        jLabel57.setText("Serie:");

        jLabel58.setText("Placas:");

        t_placas_vehiculo.setToolTipText("Número de placas de la unidad");
        t_placas_vehiculo.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        t_placas_vehiculo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                t_placas_vehiculoKeyTyped(evt);
            }
        });

        jLabel59.setText("*");

        jLabel60.setText("*");

        jLabel61.setText("*");

        jLabel62.setText("No. Motor:");

        t_motor_vehiculo.setToolTipText("Número de motor de la unidad");
        t_motor_vehiculo.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        t_motor_vehiculo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                t_motor_vehiculoKeyTyped(evt);
            }
        });

        c_tipo_vehiculo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "PARTICULAR", "PUBLICO", "PUBLICO FEDERAL (CARGA)", " " }));
        c_tipo_vehiculo.setToolTipText("Tipo de la unidad");

        jLabel77.setText("*");

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel51)
                    .addComponent(jLabel49)
                    .addComponent(jLabel50))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(t_modelo_vehiculo)
                    .addComponent(t_color_vehiculo)
                    .addComponent(c_tipo_vehiculo, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addComponent(jLabel55)
                        .addGap(61, 61, 61)
                        .addComponent(t_serie_vehiculo, javax.swing.GroupLayout.DEFAULT_SIZE, 198, Short.MAX_VALUE))
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel53)
                            .addComponent(jLabel54))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel52)
                            .addGroup(jPanel13Layout.createSequentialGroup()
                                .addGap(5, 5, 5)
                                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel57)
                                    .addComponent(jLabel56))))
                        .addGap(5, 5, 5)
                        .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(t_marca_vehiculo, javax.swing.GroupLayout.DEFAULT_SIZE, 198, Short.MAX_VALUE)
                            .addComponent(t_anio_vehiculo))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel59)
                    .addComponent(jLabel60)
                    .addComponent(jLabel61))
                .addGap(72, 72, 72)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel58)
                    .addComponent(jLabel62))
                .addGap(18, 18, 18)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addComponent(t_placas_vehiculo, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel77))
                    .addComponent(t_motor_vehiculo, javax.swing.GroupLayout.PREFERRED_SIZE, 221, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel58)
                        .addComponent(t_placas_vehiculo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel77))
                    .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel52)
                        .addComponent(t_marca_vehiculo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel59))
                    .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel49)
                        .addComponent(jLabel53))
                    .addComponent(c_tipo_vehiculo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel56)
                        .addComponent(t_anio_vehiculo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel60)
                        .addComponent(jLabel62)
                        .addComponent(t_motor_vehiculo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel50)
                        .addComponent(t_modelo_vehiculo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel54)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel57)
                        .addComponent(t_serie_vehiculo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel61))
                    .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(t_color_vehiculo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel55))
                    .addComponent(jLabel51))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel68.setText("Medio de pago:");

        t_medio_pago.setToolTipText("Medio de pago de la factura");
        t_medio_pago.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        t_medio_pago.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                t_medio_pagoKeyTyped(evt);
            }
        });

        jLabel92.setText("*");

        jLabel93.setText("Desde:");

        jLabel94.setText("*");

        jLabel95.setText("*");

        jLabel96.setText("Hasta");

        jLabel97.setText("*");

        t_condiciones_pago.setToolTipText("Condiciones de pago de la factura");
        t_condiciones_pago.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        t_condiciones_pago.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                t_condiciones_pagoKeyTyped(evt);
            }
        });

        jLabel98.setText("Condiciones de pago:");

        b_fecha_1.setBackground(new java.awt.Color(2, 135, 242));
        b_fecha_1.setIcon(new ImageIcon("imagenes/calendario.png"));
        b_fecha_1.setToolTipText("Calendario");
        b_fecha_1.setMaximumSize(new java.awt.Dimension(32, 8));
        b_fecha_1.setMinimumSize(new java.awt.Dimension(32, 8));
        b_fecha_1.setPreferredSize(new java.awt.Dimension(32, 8));
        b_fecha_1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_fecha_1ActionPerformed(evt);
            }
        });

        t_fecha_1.setEditable(false);
        t_fecha_1.setBackground(new java.awt.Color(204, 255, 255));
        t_fecha_1.setText("AAAA-MM-DD");
        t_fecha_1.setToolTipText("Fecha desde");
        t_fecha_1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        t_fecha_1.setEnabled(false);

        b_fecha_2.setBackground(new java.awt.Color(2, 135, 242));
        b_fecha_2.setIcon(new ImageIcon("imagenes/calendario.png"));
        b_fecha_2.setToolTipText("Calendario");
        b_fecha_2.setMaximumSize(new java.awt.Dimension(32, 8));
        b_fecha_2.setMinimumSize(new java.awt.Dimension(32, 8));
        b_fecha_2.setPreferredSize(new java.awt.Dimension(32, 8));
        b_fecha_2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_fecha_2ActionPerformed(evt);
            }
        });

        t_fecha_2.setEditable(false);
        t_fecha_2.setBackground(new java.awt.Color(204, 255, 255));
        t_fecha_2.setText("AAAA-MM-DD");
        t_fecha_2.setToolTipText("Fecha hasta");
        t_fecha_2.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        t_fecha_2.setEnabled(false);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel68)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(t_medio_pago, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel92)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel93)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(t_fecha_1, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(1, 1, 1)
                        .addComponent(b_fecha_1, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel94)
                        .addGap(32, 32, 32)
                        .addComponent(jLabel98)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(t_condiciones_pago, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel97)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel96)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(t_fecha_2, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(1, 1, 1)
                        .addComponent(b_fecha_2, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(3, 3, 3)
                        .addComponent(jLabel95)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel68)
                        .addComponent(t_medio_pago, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel92)
                        .addComponent(jLabel93)
                        .addComponent(jLabel98)
                        .addComponent(t_condiciones_pago, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel97)
                        .addComponent(jLabel96)
                        .addComponent(jLabel94))
                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel5Layout.createSequentialGroup()
                            .addGap(3, 3, 3)
                            .addComponent(t_fecha_1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(b_fecha_1, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel5Layout.createSequentialGroup()
                            .addGap(3, 3, 3)
                            .addComponent(t_fecha_2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(b_fecha_2, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel95))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        contenedor.addTab("Datos de addenda", jPanel5);

        jPanel14.setBackground(new java.awt.Color(254, 254, 254));
        jPanel14.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));

        jLabel79.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel79.setText("Datos de la Factura Electrónica");

        jLabel80.setText("Serie:");

        jLabel81.setText("Folio:");

        t_serie_factura.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        t_serie_factura.setToolTipText("Número de serie asignado por el PAC");
        t_serie_factura.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        t_serie_factura.setEnabled(false);

        t_folio_factura.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        t_folio_factura.setToolTipText("Folio asignado por el PAC");
        t_folio_factura.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        t_folio_factura.setEnabled(false);

        jLabel82.setText("XML:");

        jLabel83.setText("PDF:");

        t_xml.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        t_xml.setToolTipText("Factura en formato XML");
        t_xml.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        t_xml.setEnabled(false);

        t_pdf.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        t_pdf.setToolTipText("Factura en formato PDF");
        t_pdf.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        t_pdf.setEnabled(false);

        b_xml.setBackground(new java.awt.Color(2, 135, 242));
        b_xml.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        b_xml.setForeground(new java.awt.Color(254, 254, 254));
        b_xml.setIcon(new ImageIcon("imagenes/xml_icon.png"));
        b_xml.setText("Abrir");
        b_xml.setToolTipText("Abrir factura en formato XML");
        b_xml.setEnabled(false);
        b_xml.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_xmlActionPerformed(evt);
            }
        });

        b_pdf.setBackground(new java.awt.Color(2, 135, 242));
        b_pdf.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        b_pdf.setForeground(new java.awt.Color(254, 254, 254));
        b_pdf.setIcon(new ImageIcon("imagenes/pdf_icon.png"));
        b_pdf.setText("Abrir");
        b_pdf.setToolTipText("Abrir factura en formato PDF");
        b_pdf.setEnabled(false);
        b_pdf.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_pdfActionPerformed(evt);
            }
        });

        b_email.setBackground(new java.awt.Color(2, 135, 242));
        b_email.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        b_email.setForeground(new java.awt.Color(254, 254, 254));
        b_email.setIcon(new ImageIcon("imagenes/send2.png"));
        b_email.setText("Enviar al Cliente");
        b_email.setToolTipText("comprime el XML y el PDF para enviarlo por correo electrónico");
        b_email.setEnabled(false);
        b_email.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_emailActionPerformed(evt);
            }
        });

        jLabel86.setText("UUID:");

        t_uuid_factura.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        t_uuid_factura.setToolTipText("Folio fiscal asignado por el SAT");
        t_uuid_factura.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        t_uuid_factura.setEnabled(false);

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addGap(325, 325, 325)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jLabel79)
                        .addGroup(jPanel14Layout.createSequentialGroup()
                            .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(jPanel14Layout.createSequentialGroup()
                                    .addComponent(jLabel86)
                                    .addGap(18, 18, 18)
                                    .addComponent(t_uuid_factura, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel14Layout.createSequentialGroup()
                                    .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel80)
                                        .addComponent(jLabel81))
                                    .addGap(18, 18, 18)
                                    .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(t_folio_factura, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(t_serie_factura, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGap(45, 45, 45)))
                    .addGroup(jPanel14Layout.createSequentialGroup()
                        .addGap(4, 4, 4)
                        .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel82)
                            .addComponent(jLabel83))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel14Layout.createSequentialGroup()
                                .addComponent(t_pdf, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(b_pdf))
                            .addGroup(jPanel14Layout.createSequentialGroup()
                                .addComponent(t_xml, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(b_xml))
                            .addComponent(b_email))))
                .addContainerGap(380, Short.MAX_VALUE))
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addGap(97, 97, 97)
                .addComponent(jLabel79)
                .addGap(18, 18, 18)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel80)
                    .addComponent(t_serie_factura, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel81)
                    .addComponent(t_folio_factura, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel86)
                    .addComponent(t_uuid_factura, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel82)
                    .addComponent(t_xml, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(b_xml))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel83)
                    .addComponent(t_pdf, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(b_pdf))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(b_email)
                .addContainerGap(266, Short.MAX_VALUE))
        );

        contenedor.addTab("Factura Electrónica", jPanel14);

        getContentPane().add(contenedor, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void b_generarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_generarActionPerformed
        // TODO add your handling code here:
        h= new Herramientas(this.user, 0);
        
        if(t_social.getText().trim().compareTo("")!=0)
        {
            if(t_rfc.getText().trim().compareTo("")!=0)
            {
                if(t_calle.getText().trim().compareTo("")!=0)
                {
                    if(t_colonia.getText().trim().compareTo("")!=0)
                    {
                        if(c_estado.getSelectedIndex()>=0)
                        {
                            if(c_pais.getSelectedIndex()>=0)
                            {
                                if(t_no_exterior.getText().trim().compareTo("")!=0)
                                {
                                    if(t_cp.getText().trim().compareTo("")!=0)
                                    {
                                        if(t_municipio.getText().trim().compareTo("")!=0)
                                        {
                                            if(t_metodo_pago.getText().trim().compareTo("")!=0)
                                            {
                                                if(c_moneda.getSelectedIndex()>=0)
                                                {
                                                    if(t_tipo_cambio.getText().trim().compareTo("")!=0)
                                                    {
                                                        if(t_datos.getRowCount()>0)
                                                        {
                                                            if(c_tipo.getSelectedIndex()>=0)
                                                            {
                                                                if(t_numero.getText().trim().compareTo("")!=0)
                                                                {
                                                                    if(c_tipo_cliente.getSelectedIndex()>=0)
                                                                    {
                                                                        if(t_no_rep.getText().trim().compareTo("")!=0)
                                                                        {
                                                                            if(t_siniestro.getText().trim().compareTo("")!=0)
                                                                            {
                                                                                if(t_contratante.getText().trim().compareTo("")!=0)
                                                                                {
                                                                                    if(t_codigo_asegurado.getText().trim().compareTo("")!=0)
                                                                                    {
                                                                                        if(c_tipo_emisor.getSelectedIndex()>=0)
                                                                                        {
                                                                                            if(t_nombre_emisor.getText().trim().compareTo("")!=0)
                                                                                            {
                                                                                                if(t_correo_emisor.getText().trim().compareTo("")!=0)
                                                                                                {
                                                                                                    if(t_tel_emisor.getText().trim().compareTo("")!=0)
                                                                                                    {
                                                                                                        if(t_descripcion.getText().trim().compareTo("")!=0)
                                                                                                        {
                                                                                                            if(c_tipo_receptor.getSelectedIndex()>=0)
                                                                                                            {
                                                                                                                if(t_nombre_receptor.getText().trim().compareTo("")!=0)
                                                                                                                {
                                                                                                                    if(t_correo_receptor.getText().trim().compareTo("")!=0)
                                                                                                                    {
                                                                                                                        if(t_tel_receptor.getText().trim().compareTo("")!=0)
                                                                                                                        {
                                                                                                                            if(c_tipo_vehiculo.getSelectedIndex()>=0)
                                                                                                                            {
                                                                                                                                if(t_modelo_vehiculo.getText().trim().compareTo("")!=0)
                                                                                                                                {
                                                                                                                                    if(t_color_vehiculo.getText().trim().compareTo("")!=0)
                                                                                                                                    {
                                                                                                                                        if(t_marca_vehiculo.getText().trim().compareTo("")!=0)
                                                                                                                                        {
                                                                                                                                            if(t_anio_vehiculo.getText().trim().compareTo("")!=0)
                                                                                                                                            {
                                                                                                                                                if(t_serie_vehiculo.getText().trim().compareTo("")!=0)
                                                                                                                                                {
                                                                                                                                                    if(t_placas_vehiculo.getText().trim().compareTo("")!=0)
                                                                                                                                                    {
                                                                                                                                                        if(t_medio_pago.getText().trim().compareTo("")!=0)
                                                                                                                                                        {
                                                                                                                                                            if(t_fecha_1.getText().trim().compareTo("AAAA-MM-DD")!=0)
                                                                                                                                                            {
                                                                                                                                                                if(t_condiciones_pago.getText().trim().compareTo("")!=0)
                                                                                                                                                                {
                                                                                                                                                                    if(t_fecha_2.getText().trim().compareTo("AAAA-MM-DD")!=0)
                                                                                                                                                                    {
                                                                                                                                                                        habilita(false, false);
                                                                                                                                                                        progreso.setIndeterminate(true);
                                                                                                                                                                        progreso.setString("Conectando al servidor SAT Espere");
                                                                                                                                                                        facturaElectronica();

                                                                                                                                                                    }
                                                                                                                                                                    else
                                                                                                                                                                    {
                                                                                                                                                                        contenedor.setSelectedIndex(2);
                                                                                                                                                                        JOptionPane.showMessageDialog(null, "Debe ingresar la fecha hasta");
                                                                                                                                                                        t_fecha_2.requestFocus();
                                                                                                                                                                    }
                                                                                                                                                                }
                                                                                                                                                                else
                                                                                                                                                                {
                                                                                                                                                                    contenedor.setSelectedIndex(2);
                                                                                                                                                                    JOptionPane.showMessageDialog(null, "Debe ingresar la condición de pago");
                                                                                                                                                                    t_condiciones_pago.requestFocus();
                                                                                                                                                                }
                                                                                                                                                            }
                                                                                                                                                            else
                                                                                                                                                            {
                                                                                                                                                                contenedor.setSelectedIndex(2);
                                                                                                                                                                JOptionPane.showMessageDialog(null, "Debe ingresar la fecha desde");
                                                                                                                                                                t_fecha_1.requestFocus();
                                                                                                                                                            }
                                                                                                                                                        }
                                                                                                                                                        else
                                                                                                                                                        {
                                                                                                                                                            contenedor.setSelectedIndex(2);
                                                                                                                                                            JOptionPane.showMessageDialog(null, "Debe ingresar el medio de pago");
                                                                                                                                                            t_medio_pago.requestFocus();
                                                                                                                                                        }
                                                                                                                                                    }
                                                                                                                                                    else
                                                                                                                                                    {
                                                                                                                                                        contenedor.setSelectedIndex(2);
                                                                                                                                                        JOptionPane.showMessageDialog(null, "Debe ingresar las placas del vehículo");
                                                                                                                                                        t_placas_vehiculo.requestFocus();
                                                                                                                                                    }
                                                                                                                                                }
                                                                                                                                                else
                                                                                                                                                {
                                                                                                                                                    contenedor.setSelectedIndex(2);
                                                                                                                                                    JOptionPane.showMessageDialog(null, "Debe ingresar la serie del vehículo");
                                                                                                                                                    t_serie_vehiculo.requestFocus();
                                                                                                                                                }
                                                                                                                                            }
                                                                                                                                            else
                                                                                                                                            {
                                                                                                                                                contenedor.setSelectedIndex(2);
                                                                                                                                                JOptionPane.showMessageDialog(null, "Debe ingresar el año del vehículo");
                                                                                                                                                t_anio_vehiculo.requestFocus();
                                                                                                                                            }
                                                                                                                                        }
                                                                                                                                        else
                                                                                                                                        {
                                                                                                                                            contenedor.setSelectedIndex(2);
                                                                                                                                            JOptionPane.showMessageDialog(null, "Debe ingresar la marca del vehículo");
                                                                                                                                            t_marca_vehiculo.requestFocus();
                                                                                                                                        }
                                                                                                                                    }
                                                                                                                                    else
                                                                                                                                    {
                                                                                                                                        contenedor.setSelectedIndex(2);
                                                                                                                                        JOptionPane.showMessageDialog(null, "Debe ingresar el color del vehículo");
                                                                                                                                        t_color_vehiculo.requestFocus();
                                                                                                                                    }
                                                                                                                                }
                                                                                                                                else
                                                                                                                                {
                                                                                                                                    contenedor.setSelectedIndex(2);
                                                                                                                                    JOptionPane.showMessageDialog(null, "Debe ingresar el modelo del vehículo");
                                                                                                                                    t_modelo_vehiculo.requestFocus();
                                                                                                                                }
                                                                                                                            }
                                                                                                                            else
                                                                                                                            {
                                                                                                                                contenedor.setSelectedIndex(2);
                                                                                                                                JOptionPane.showMessageDialog(null, "Debe ingresar el tipo de propietario del vehículo");
                                                                                                                                c_tipo_vehiculo.requestFocus();
                                                                                                                            }
                                                                                                                        }
                                                                                                                        else
                                                                                                                        {
                                                                                                                            contenedor.setSelectedIndex(2);
                                                                                                                            JOptionPane.showMessageDialog(null, "Debes ingresar el teléfono del receptor");
                                                                                                                            t_tel_receptor.requestFocus();
                                                                                                                        }
                                                                                                                    }
                                                                                                                    else
                                                                                                                    {
                                                                                                                        contenedor.setSelectedIndex(2);
                                                                                                                        JOptionPane.showMessageDialog(null, "Debes ingresar el correo del receptor");
                                                                                                                        t_correo_receptor.requestFocus();
                                                                                                                    }
                                                                                                                }
                                                                                                                else
                                                                                                                {
                                                                                                                    contenedor.setSelectedIndex(2);
                                                                                                                    JOptionPane.showMessageDialog(null, "Debes ingresar el nombre del receptor");
                                                                                                                    t_nombre_receptor.requestFocus();
                                                                                                                }
                                                                                                            }
                                                                                                            else
                                                                                                            {
                                                                                                                contenedor.setSelectedIndex(2);
                                                                                                                JOptionPane.showMessageDialog(null, "Debes ingresar el tipo del receptor");
                                                                                                                c_tipo_receptor.requestFocus();
                                                                                                            }
                                                                                                        }
                                                                                                        else
                                                                                                        {
                                                                                                            contenedor.setSelectedIndex(2);
                                                                                                            JOptionPane.showMessageDialog(null, "Debes ingresar una desccripción del emisor");
                                                                                                            c_tipo_receptor.requestFocus();
                                                                                                        }
                                                                                                    }
                                                                                                    else
                                                                                                    {
                                                                                                        contenedor.setSelectedIndex(2);
                                                                                                        JOptionPane.showMessageDialog(null, "Debes ingrear el teléfono del emisor");
                                                                                                        t_descripcion.requestFocus();
                                                                                                    }
                                                                                                }
                                                                                                else
                                                                                                {
                                                                                                    contenedor.setSelectedIndex(2);
                                                                                                    JOptionPane.showMessageDialog(null, "Debes ingresar el correo del emisor");
                                                                                                    t_correo_emisor.requestFocus();
                                                                                                }
                                                                                            }
                                                                                            else
                                                                                            {
                                                                                                contenedor.setSelectedIndex(2);
                                                                                                JOptionPane.showMessageDialog(null, "Debes ingresar el nombre del contacto emisor");
                                                                                                t_nombre_emisor.requestFocus();
                                                                                            }
                                                                                        }
                                                                                        else
                                                                                        {
                                                                                            contenedor.setSelectedIndex(2);
                                                                                            JOptionPane.showMessageDialog(null, "Debes ingresar el tipo del contacto emisor");
                                                                                            c_tipo_emisor.requestFocus();
                                                                                        }
                                                                                    }
                                                                                    else
                                                                                    {
                                                                                        contenedor.setSelectedIndex(2);
                                                                                        JOptionPane.showMessageDialog(null, "Debes ingresar el codigo del asegurado");
                                                                                        t_codigo.requestFocus();
                                                                                    }
                                                                                }
                                                                                else
                                                                                {
                                                                                    contenedor.setSelectedIndex(2);
                                                                                    JOptionPane.showMessageDialog(null, "Debes ingresar el nombre del contratante");
                                                                                    t_contratante.requestFocus();
                                                                                }
                                                                            }
                                                                            else
                                                                            {
                                                                                contenedor.setSelectedIndex(2);
                                                                                JOptionPane.showMessageDialog(null, "Debes ingresar el no° de siniestro");
                                                                                t_siniestro.requestFocus();
                                                                            }
                                                                        }
                                                                        else
                                                                        {
                                                                            contenedor.setSelectedIndex(2);
                                                                            JOptionPane.showMessageDialog(null, "Debes ingresar no de reporte");
                                                                            t_no_rep.requestFocus();
                                                                        }
                                                                    }
                                                                    else
                                                                    {
                                                                        contenedor.setSelectedIndex(2);
                                                                        JOptionPane.showMessageDialog(null, "Debes ingresar el tipo de cliente");
                                                                        c_tipo_cliente.requestFocus();
                                                                    }
                                                                }
                                                                else
                                                                {
                                                                    contenedor.setSelectedIndex(2);
                                                                    JOptionPane.showMessageDialog(null, "Debes ingresar el número de poliza");
                                                                    t_numero.requestFocus();
                                                                }
                                                            }
                                                            else
                                                            {
                                                                contenedor.setSelectedIndex(0);
                                                                JOptionPane.showMessageDialog(null, "Debes ingresar el tipo de poliza");
                                                                c_tipo.requestFocus();
                                                            }
                                                        }
                                                        else
                                                        {
                                                            contenedor.setSelectedIndex(1);
                                                            JOptionPane.showMessageDialog(null, "Debes ingresar conceptos para la factura");
                                                        }
                                                    }
                                                    else
                                                    {
                                                        contenedor.setSelectedIndex(1);
                                                        JOptionPane.showMessageDialog(null, "Debes ingresar el tipo de combio");
                                                        t_tipo_cambio.requestFocus();
                                                    }
                                                }
                                                else
                                                {
                                                    contenedor.setSelectedIndex(1);
                                                    JOptionPane.showMessageDialog(null, "Debes ingresar el tipo de moneda");
                                                    c_moneda.requestFocus();
                                                }
                                            }
                                            else
                                            {
                                                contenedor.setSelectedIndex(1);
                                                JOptionPane.showMessageDialog(null, "Debes ingresar el metodo de pago");
                                                t_metodo_pago.requestFocus();
                                            }
                                        }
                                        else
                                        {
                                            contenedor.setSelectedIndex(0);
                                            JOptionPane.showMessageDialog(null, "Debes ingresar el municipio del receptor");
                                            t_municipio.requestFocus();
                                        }
                                    }
                                    else
                                    {
                                        contenedor.setSelectedIndex(0);
                                        JOptionPane.showMessageDialog(null, "Debes ingresar el cp del receptor");
                                        t_cp.requestFocus();
                                    }
                                }
                                else
                                {
                                    contenedor.setSelectedIndex(0);
                                    JOptionPane.showMessageDialog(null, "Debes ingresar el no° exterior del receptor");
                                    t_no_exterior.requestFocus();
                                }
                            }
                            else
                            {
                                contenedor.setSelectedIndex(0);
                                JOptionPane.showMessageDialog(null, "Debes ingresar el pais del receptor");
                                c_pais.requestFocus();
                            }
                        }
                        else
                        {
                            contenedor.setSelectedIndex(0);
                            JOptionPane.showMessageDialog(null, "Debes ingresar el estado del del receptor");
                            c_estado.requestFocus();
                        }
                    }
                    else
                    {
                        contenedor.setSelectedIndex(0);
                        JOptionPane.showMessageDialog(null, "Debes ingresar la colonia del receptor");
                        t_colonia.requestFocus();
                    }
                }
                else
                {
                    contenedor.setSelectedIndex(0);
                    JOptionPane.showMessageDialog(null, "Debes ingresar la calle del receptor");
                    t_calle.requestFocus();
                }
            }
            else
            {
                contenedor.setSelectedIndex(0);
                JOptionPane.showMessageDialog(null, "Debes ingresar el rfc receptor");
                t_rfc.requestFocus();
            }
        }
        else
        {
            contenedor.setSelectedIndex(0);
            JOptionPane.showMessageDialog(null, "Debes ingresar la razon social del receptor");
            t_social.requestFocus();
        }
    }//GEN-LAST:event_b_generarActionPerformed

    private void b_actualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_actualizarActionPerformed
        // TODO add your handling code here:
        h=new Herramientas(user, 0);
        h.session(sessionPrograma);
        if (factura!=null)
        {
            String res=guarda();
            if(res.compareTo("")==0)
                JOptionPane.showMessageDialog(null, "Informacion actualizada");
            else
                JOptionPane.showMessageDialog(null, res);
        }
    }//GEN-LAST:event_b_actualizarActionPerformed

    private void t_socialKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_socialKeyTyped
        // TODO add your handling code here:
        evt.setKeyChar(Character.toUpperCase(evt.getKeyChar()));
        if(t_social.getText().length()>=200)
        evt.consume();
    }//GEN-LAST:event_t_socialKeyTyped

    private void t_rfcKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_rfcKeyTyped
        // TODO add your handling code here:
        evt.setKeyChar(Character.toUpperCase(evt.getKeyChar()));
        if(t_rfc.getText().length()>=13)
        evt.consume();
    }//GEN-LAST:event_t_rfcKeyTyped

    private void t_calleKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_calleKeyTyped
        // TODO add your handling code here:
        evt.setKeyChar(Character.toUpperCase(evt.getKeyChar()));
        if(t_calle.getText().length()>=200)
        evt.consume();
    }//GEN-LAST:event_t_calleKeyTyped

    private void t_no_exteriorKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_no_exteriorKeyTyped
        // TODO add your handling code here:
        evt.setKeyChar(Character.toUpperCase(evt.getKeyChar()));
        if(t_no_exterior.getText().length()>=10)
        evt.consume();
    }//GEN-LAST:event_t_no_exteriorKeyTyped

    private void t_cpKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_cpKeyTyped
        // TODO add your handling code here:
        char car = evt.getKeyChar();
        evt.setKeyChar(Character.toUpperCase(evt.getKeyChar()));
        if(t_cp.getText().length()>=5)
            evt.consume();
        if((car<'0' || car>'9'))
            evt.consume();
    }//GEN-LAST:event_t_cpKeyTyped

    private void t_coloniaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_coloniaKeyTyped
        // TODO add your handling code here:
        evt.setKeyChar(Character.toUpperCase(evt.getKeyChar()));
        if(t_colonia.getText().length()>=150)
        evt.consume();
    }//GEN-LAST:event_t_coloniaKeyTyped

    private void t_municipioKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_municipioKeyTyped
        // TODO add your handling code here:
        evt.setKeyChar(Character.toUpperCase(evt.getKeyChar()));
        if(t_municipio.getText().length()>=150)
        evt.consume();
    }//GEN-LAST:event_t_municipioKeyTyped

    private void t_codigoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_codigoKeyTyped
        // TODO add your handling code here:
        evt.setKeyChar(Character.toUpperCase(evt.getKeyChar()));
        if(t_codigo.getText().length()>=6)
        evt.consume();
    }//GEN-LAST:event_t_codigoKeyTyped

    private void t_no_repKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_no_repKeyTyped
        // TODO add your handling code here:
        evt.setKeyChar(Character.toUpperCase(evt.getKeyChar()));
        if(t_no_rep.getText().length()>=19)
        evt.consume();
    }//GEN-LAST:event_t_no_repKeyTyped

    private void t_siniestroKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_siniestroKeyTyped
        // TODO add your handling code here:
        evt.setKeyChar(Character.toUpperCase(evt.getKeyChar()));
        if(t_siniestro.getText().length()>=20)
        evt.consume();
    }//GEN-LAST:event_t_siniestroKeyTyped

    private void t_nombre_emisorKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_nombre_emisorKeyTyped
        // TODO add your handling code here:
        evt.setKeyChar(Character.toUpperCase(evt.getKeyChar()));
        if(t_nombre_emisor.getText().length()>=100)
        evt.consume();
    }//GEN-LAST:event_t_nombre_emisorKeyTyped

    private void t_correo_emisorKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_correo_emisorKeyTyped
        // TODO add your handling code here:
        //evt.setKeyChar(Character.toUpperCase(evt.getKeyChar()));
        if(t_correo_emisor.getText().length()>=100)
        evt.consume();
    }//GEN-LAST:event_t_correo_emisorKeyTyped

    private void t_tel_emisorKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_tel_emisorKeyTyped
        // TODO add your handling code here:
        char car = evt.getKeyChar();
        if(t_tel_emisor.getText().length()>=13)
        evt.consume();
        if((car<'0' || car>'9'))
        evt.consume();
    }//GEN-LAST:event_t_tel_emisorKeyTyped

    private void t_nombre_receptorKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_nombre_receptorKeyTyped
        // TODO add your handling code here:
        evt.setKeyChar(Character.toUpperCase(evt.getKeyChar()));
        if(t_nombre_receptor.getText().length()>=100)
            evt.consume();
    }//GEN-LAST:event_t_nombre_receptorKeyTyped

    private void t_correo_receptorKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_correo_receptorKeyTyped
        // TODO add your handling code here:
        //evt.setKeyChar(Character.toUpperCase(evt.getKeyChar()));
        if(t_correo_receptor.getText().length()>=100)
        evt.consume();
    }//GEN-LAST:event_t_correo_receptorKeyTyped

    private void t_tel_receptorKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_tel_receptorKeyTyped
        // TODO add your handling code here:
        char car = evt.getKeyChar();
        if(t_tel_receptor.getText().length()>=13)
        evt.consume();
        if((car<'0' || car>'9'))
        evt.consume();
    }//GEN-LAST:event_t_tel_receptorKeyTyped

    private void b_emailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_emailActionPerformed
        // TODO add your handling code here:
        String ruta="";
        try
        {
            ruta="";
            FileReader f = new FileReader("config.txt");
            BufferedReader b = new BufferedReader(f);
            if((ruta = b.readLine())==null)
                ruta="";
            b.close();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        File xml= new File(ruta+"xml-timbrados/"+t_xml.getText());
        File pdf=new File(ruta+"xml-timbrados/"+t_pdf.getText());
        
        if(xml.exists()==true && pdf.exists()==true)
        {
            Comprimir zip=new Comprimir();
            List lista=new ArrayList();
            lista.add(ruta+"xml-timbrados/"+t_xml.getText());
            lista.add(ruta+"xml-timbrados/"+t_pdf.getText());
            h=new Herramientas(user, 0);
            h.session(sessionPrograma);
            Session session = HibernateUtil.getSessionFactory().openSession();
            try
            {
                session.beginTransaction().begin();
                factura=(Factura)session.get(Factura.class, factura.getIdFactura());
                int numeroID=0;
                if(factura.getOrden()!=null)
                    numeroID=factura.getOrden().getIdOrden();
                if(factura.getOrdenExterna()!=null)
                    numeroID=factura.getOrdenExterna().getIdOrden();
                zip.zipArchivos(lista, ruta+"xml-timbrados/"+numeroID, Comprimir.Extension.ZIP);
                File envio=new File(ruta+"xml-timbrados/"+numeroID+".zip");
                if(envio.exists())
                {
                    String mail=factura.getCorreoReceptor();
                    EnviarCorreo en=null;
                    if(factura.getOrden()!=null)
                    {
                        if(factura.getOrden().getSiniestro()!=null)
                            en= new EnviarCorreo(new javax.swing.JFrame(), true, mail,"Envío Factura", "Hola buen día, envío XML y PDF correspondiente al siniestro:."+factura.getOrden().getSiniestro(), envio, this.user, this.sessionPrograma);
                        else
                            en= new EnviarCorreo(new javax.swing.JFrame(), true, mail,"Envío Factura", "Hola buen día, envío XML y PDF", envio, this.user, this.sessionPrograma);
                    }
                    if(factura.getOrdenExterna()!=null)
                    {
                        if(factura.getOrdenExterna().getSiniestro()!=null)
                            en= new EnviarCorreo(new javax.swing.JFrame(), true, mail,"Envío Factura", "Hola buen día, envío XML y PDF correspondiente al siniestro:."+factura.getOrdenExterna().getSiniestro(), envio, this.user, this.sessionPrograma);
                        else
                            en= new EnviarCorreo(new javax.swing.JFrame(), true, mail,"Envío Factura", "Hola buen día, envío XML y PDF", envio, this.user, this.sessionPrograma);
                    }
                    Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
                    en.setLocation((d.width/2)-(en.getWidth()/2), (d.height/2)-(en.getHeight()/2));
                    en.setVisible(true);
                }
                else
                    JOptionPane.showMessageDialog(null, "Error al comprimir.");
            }catch(Exception e)
            {
                JOptionPane.showMessageDialog(null, "Error al comprimir.");
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
            JOptionPane.showMessageDialog(null, "No se encontraron los archivos");
        }
    }//GEN-LAST:event_b_emailActionPerformed

    private void b_salirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_salirActionPerformed
        // TODO add your handling code here:
        doClose(factura);
    }//GEN-LAST:event_b_salirActionPerformed

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        // TODO add your handling code here:
        doClose(factura);
    }//GEN-LAST:event_formWindowClosed

    private void t_metodo_pagoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_metodo_pagoKeyTyped
        // TODO add your handling code here:
        evt.setKeyChar(Character.toUpperCase(evt.getKeyChar()));
        if(t_metodo_pago.getText().length()>35)
        evt.consume();
    }//GEN-LAST:event_t_metodo_pagoKeyTyped

    private void t_cuenta_pagoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_cuenta_pagoKeyTyped
        // TODO add your handling code here:
        evt.setKeyChar(Character.toUpperCase(evt.getKeyChar()));
        if(t_cuenta_pago.getText().length()>=30)
        evt.consume();
    }//GEN-LAST:event_t_cuenta_pagoKeyTyped

    private void t_numeroKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_numeroKeyTyped
        // TODO add your handling code here:
        evt.setKeyChar(Character.toUpperCase(evt.getKeyChar()));
        if(t_numero.getText().length()>=20)
        evt.consume();
    }//GEN-LAST:event_t_numeroKeyTyped

    private void t_color_vehiculoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_color_vehiculoKeyTyped
        // TODO add your handling code here:
        evt.setKeyChar(Character.toUpperCase(evt.getKeyChar()));
        if(t_color_vehiculo.getText().length()>=20)
        evt.consume();
    }//GEN-LAST:event_t_color_vehiculoKeyTyped

    private void t_anio_vehiculoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_anio_vehiculoKeyTyped
        // TODO add your handling code here:
        char car = evt.getKeyChar();
        if(t_anio_vehiculo.getText().length()>=4)
            evt.consume();
        if((car<'0' || car>'9'))
            evt.consume();
    }//GEN-LAST:event_t_anio_vehiculoKeyTyped

    private void t_serie_vehiculoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_serie_vehiculoKeyTyped
        // TODO add your handling code here:
        evt.setKeyChar(Character.toUpperCase(evt.getKeyChar()));
        if(t_serie_vehiculo.getText().length()>=20)
            evt.consume();
    }//GEN-LAST:event_t_serie_vehiculoKeyTyped

    private void t_placas_vehiculoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_placas_vehiculoKeyTyped
        // TODO add your handling code here:
        evt.setKeyChar(Character.toUpperCase(evt.getKeyChar()));
        if(t_placas_vehiculo.getText().length()>=9)
            evt.consume();
    }//GEN-LAST:event_t_placas_vehiculoKeyTyped

    private void t_motor_vehiculoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_motor_vehiculoKeyTyped
        // TODO add your handling code here:
        evt.setKeyChar(Character.toUpperCase(evt.getKeyChar()));
        if(t_motor_vehiculo.getText().length()>=15)
            evt.consume();
    }//GEN-LAST:event_t_motor_vehiculoKeyTyped

    private void t_proveedorKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_proveedorKeyTyped
        // TODO add your handling code here:
        evt.setKeyChar(Character.toUpperCase(evt.getKeyChar()));
        if(t_proveedor.getText().length()>=20)
            evt.consume();
    }//GEN-LAST:event_t_proveedorKeyTyped

    private void b_xmlActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_xmlActionPerformed
        // TODO add your handling code here:
        if(t_folio_factura.getText().compareTo("")!=0 && t_uuid_factura.getText().compareTo("")!=0)
        {
            Session session = HibernateUtil.getSessionFactory().openSession();
            try
            {
                session.beginTransaction().begin();
                String ruta="";
                try
                {
                    ruta="";
                    FileReader f = new FileReader("config.txt");
                    BufferedReader b = new BufferedReader(f);
                    if((ruta = b.readLine())==null)
                        ruta="";
                    b.close();
                }
                catch(IOException e)
                {
                    e.printStackTrace();
                }
                Configuracion config=(Configuracion)session.get(Configuracion.class, 1);
                if(t_xml.getText().compareTo("")==0)
                    t_xml.setText(config.getRfc()+"_"+t_serie_factura.getText()+"_"+t_folio_factura.getText()+"_"+t_rfc.getText()+".xml");
                File xml=new File(ruta+"xml-timbrados/"+t_xml.getText());
                if(xml.exists())//existe en nuestro directorio
                    Desktop.getDesktop().open(xml);
                else//descargar el archivo del SAP
                {
                    factura=(Factura)session.get(Factura.class, factura.getIdFactura());
                    RequestTransaction rq=new RequestTransaction();
                    rq.setRequestor(config.getRequestor());//Lo proporcionará MySuite
                    rq.setTransaction("GET_DOCUMENT");//Tipo de Transaccion
                    rq.setCountry("MX");//Codigo de pais
                    rq.setUser(config.getRequestor());//igual que Requestor
                    rq.setUserName(config.getUsuario_1());//Country.Entity.nombre_usuario
                    rq.setEntity(config.getRfc());
                    rq.setData1(t_serie_factura.getText());//serie del documento.
                    rq.setData2(t_folio_factura.getText());//folio
                    rq.setData3("PDF XML");//documentos
                    abrir="xml";
                    llamarSoapDocumento(rq);
                }
            }catch(Exception e)
            {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error al consultar la base de datos");
            }
            finally
            {
                if(session!=null)
                    if(session.isConnected())
                        session.close();
            }
        }
        else
        {
            JOptionPane.showMessageDialog(null, "Debes generar comprobante primero para tratar de recuperar los datos del SAP");
        }
    }//GEN-LAST:event_b_xmlActionPerformed

    private void b_pdfActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_pdfActionPerformed
        // TODO add your handling code here:
        if(t_folio_factura.getText().compareTo("")!=0 && t_uuid_factura.getText().compareTo("")!=0)
        {
            Session session = HibernateUtil.getSessionFactory().openSession();
            try
            {
                session.beginTransaction().begin();
                String ruta="";
                try
                {
                    ruta="";
                    FileReader f = new FileReader("config.txt");
                    BufferedReader b = new BufferedReader(f);
                    if((ruta = b.readLine())==null)
                        ruta="";
                    b.close();
                }
                catch(IOException e)
                {
                    e.printStackTrace();
                }
                Configuracion config=(Configuracion)session.get(Configuracion.class, 1);
                if(t_pdf.getText().compareTo("")==0)
                    t_pdf.setText(config.getRfc()+"_"+t_serie_factura.getText()+"_"+t_folio_factura.getText()+"_"+t_rfc.getText()+".pdf");
                File xml=new File(ruta+"xml-timbrados/"+t_pdf.getText());
                if(xml.exists())//existe en nuestro directorio
                    Desktop.getDesktop().open(xml);
                else//descargar el archivo del SAP
                {
                    factura=(Factura)session.get(Factura.class, factura.getIdFactura());
                    RequestTransaction rq=new RequestTransaction();
                    rq.setRequestor(config.getRequestor());//Lo proporcionará MySuite
                    rq.setTransaction("GET_DOCUMENT");//Tipo de Transaccion
                    rq.setCountry("MX");//Codigo de pais
                    rq.setUser(config.getRequestor());//igual que Requestor
                    rq.setUserName(config.getUsuario_1());//Country.Entity.nombre_usuario
                    rq.setEntity(config.getRfc());
                    rq.setData1(t_serie_factura.getText());//serie del documento.
                    rq.setData2(t_folio_factura.getText());//folio
                    rq.setData3("PDF XML");//documentos
                    abrir="pdf";
                    llamarSoapDocumento(rq);
                }
            }catch(Exception e)
            {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error al consultar la base de datos");
            }
            finally
            {
                if(session!=null)
                    if(session.isConnected())
                        session.close();
            }
        }
        else
        {
            JOptionPane.showMessageDialog(null, "Debes generar comprobante primero para tratar de recuperar los datos del SAP");
        }
    }//GEN-LAST:event_b_pdfActionPerformed

    private void b_descargarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_descargarActionPerformed
        // TODO add your handling code here:
        if(t_facturas.getSelectedRow()>-1)
        {
            habilita2(false);
            progreso.setString("Conectando al servidor SAT Espere");
            progreso.setIndeterminate(true);
            Session session = HibernateUtil.getSessionFactory().openSession();
            try
            {
                session.beginTransaction().begin();
                Configuracion config=(Configuracion)session.get(Configuracion.class, 1);
                factura=(Factura)session.get(Factura.class, factura.getIdFactura());
                RequestTransaction rq=new RequestTransaction();
                rq.setRequestor(config.getRequestor());//Lo proporcionará MySuite
                rq.setTransaction("GET_DOCUMENT");//Tipo de Transaccion
                rq.setCountry("MX");//Codigo de pais
                rq.setUser(config.getRequestor());//igual que Requestor
                rq.setUserName(config.getUsuario_1());//Country.Entity.nombre_usuario
                rq.setEntity(config.getRfc());
                rq.setData1(t_facturas.getValueAt(t_facturas.getSelectedRow(), 3).toString());//serie del documento.
                rq.setData2(t_facturas.getValueAt(t_facturas.getSelectedRow(), 4).toString());//folio
                rq.setData3("PDF XML");//documentos
                abrir="pdf";
                llamarSoapDocumento1(rq);
            }catch(Exception e)
            {
                habilita2(true);
                progreso.setString("Listo");
                progreso.setIndeterminate(false);
                JOptionPane.showMessageDialog(null, "Error al consultar la base de datos");
            }
            finally
            {
                if(session!=null)
                    if(session.isConnected())
                        session.close();
            }
        }
        else
            JOptionPane.showMessageDialog(null, "Debes seleccionar una factura de la lista primero");
    }//GEN-LAST:event_b_descargarActionPerformed

    private void b_cancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_cancelarActionPerformed
        // TODO add your handling code here:
        if(t_facturas.getSelectedRow()>-1)
        {
            habilita2(false);
            progreso.setString("Conectando al servidor SAT Espere");
            progreso.setIndeterminate(true);
            Session session = HibernateUtil.getSessionFactory().openSession();
            try
            {
                idBuscar=t_facturas.getValueAt(t_facturas.getSelectedRow(), 1).toString();
                session.beginTransaction().begin();
                Configuracion config=(Configuracion)session.get(Configuracion.class, 1);
                factura=(Factura)session.get(Factura.class, factura.getIdFactura());
                RequestTransaction rq=new RequestTransaction();
                rq.setRequestor(config.getRequestor());//Lo proporcionará MySuite
                rq.setTransaction("CANCEL_XML");//Tipo de Transaccion
                rq.setCountry("MX");//Codigo de pais
                rq.setUser(config.getRequestor());//igual que Requestor
                rq.setUserName(config.getUsuario_1());//Country.Entity.nombre_usuario
                rq.setEntity(config.getRfc());
                rq.setData1(idBuscar);//GUIID.
                rq.setData2("");
                rq.setData3("");
                llamarSoapCancela(rq);
            }catch(Exception e)
            {
                habilita2(true);
                progreso.setString("Listo");
                progreso.setIndeterminate(false);
                JOptionPane.showMessageDialog(null, "Error al consultar la base de datos");
            }
            finally
            {
                if(session!=null)
                    if(session.isConnected())
                        session.close();
            }
        }
        else
            JOptionPane.showMessageDialog(null, "Debes seleccionar una factura de la lista primero");
    }//GEN-LAST:event_b_cancelarActionPerformed

    private void t_descuentoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_t_descuentoFocusLost
        // TODO add your handling code here:
        try
        {
            t_descuento.commitEdit();
            if(((Number)t_descuento.getValue()).doubleValue()<=100.0d && ((Number)t_descuento.getValue()).doubleValue()>=0.0d)
            {
                for(int a=0; a<t_datos.getRowCount(); a++)
                {
                    t_datos.setValueAt(((Number)t_descuento.getValue()).doubleValue(), a, 5);
                }
            }
            else
            t_descuento.setValue(0);
            this.sumaTotales();
        }
        catch(Exception e)
        {

        }
    }//GEN-LAST:event_t_descuentoFocusLost

    private void t_descripcionKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_descripcionKeyTyped
        // TODO add your handling code here:
        evt.setKeyChar(Character.toUpperCase(evt.getKeyChar()));
        if(t_contratante.getText().length()>20)
            evt.consume();
    }//GEN-LAST:event_t_descripcionKeyTyped

    private void t_contratanteKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_contratanteKeyTyped
        // TODO add your handling code here:
        evt.setKeyChar(Character.toUpperCase(evt.getKeyChar()));
        if(t_contratante.getText().length()>=150)
            evt.consume();
    }//GEN-LAST:event_t_contratanteKeyTyped

    private void t_codigo_aseguradoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_codigo_aseguradoKeyTyped
        // TODO add your handling code here:
        evt.setKeyChar(Character.toUpperCase(evt.getKeyChar()));
        if(t_codigo_asegurado.getText().length()>=30)
            evt.consume();
    }//GEN-LAST:event_t_codigo_aseguradoKeyTyped

    private void t_medio_pagoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_medio_pagoKeyTyped
        // TODO add your handling code here:
        evt.setKeyChar(Character.toUpperCase(evt.getKeyChar()));
        if(t_medio_pago.getText().length()>=20)
            evt.consume();
    }//GEN-LAST:event_t_medio_pagoKeyTyped

    private void t_condiciones_pagoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_condiciones_pagoKeyTyped
        // TODO add your handling code here:
        evt.setKeyChar(Character.toUpperCase(evt.getKeyChar()));
        if(t_condiciones_pago.getText().length()>=20)
            evt.consume();
    }//GEN-LAST:event_t_condiciones_pagoKeyTyped

    private void b_fecha_1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_fecha_1ActionPerformed
        // TODO add your handling code here:
        h=new Herramientas(this.user, 0);
        h.session(sessionPrograma);

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
            t_fecha_1.setText(anio+"-"+mes+"-"+dia);
        }
        else
            t_fecha_1.setText("AAAA-MM-DD");
    }//GEN-LAST:event_b_fecha_1ActionPerformed

    private void b_fecha_2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_fecha_2ActionPerformed
        // TODO add your handling code here:
        h=new Herramientas(this.user, 0);
        h.session(sessionPrograma);

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
            t_fecha_2.setText(anio+"-"+mes+"-"+dia);
        }
        else
            t_fecha_2.setText("AAAA-MM-DD");
    }//GEN-LAST:event_b_fecha_2ActionPerformed

    private void t_iva1FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_t_iva1FocusLost
        // TODO add your handling code here:
        try
        {
            t_iva1.commitEdit();
        }catch(Exception e){}
        this.sumaTotales();
    }//GEN-LAST:event_t_iva1FocusLost

    private void t_iva1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_t_iva1ActionPerformed
        // TODO add your handling code here:
        try
        {
            t_iva1.commitEdit();
        }catch(Exception e){}
        this.sumaTotales();
    }//GEN-LAST:event_t_iva1ActionPerformed

    private void t_iva1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_iva1KeyTyped
        // TODO add your handling code here:
        char car = evt.getKeyChar();
        if(t_iva1.getText().length()>=3)
        evt.consume();
        if((car<'0' || car>'9'))
        evt.consume();
    }//GEN-LAST:event_t_iva1KeyTyped

    private void b_menosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_menosActionPerformed
        // TODO add your handling code here:
        if(t_datos.getSelectedRow()>-1)
        {
            h=new Herramientas(user, 0);
            h.session(sessionPrograma);
            Session session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction().begin();
            Concepto con = (Concepto)session.get(Concepto.class, Integer.parseInt(t_datos.getValueAt(t_datos.getSelectedRow(), 0).toString()));
            session.delete(con);
            session.beginTransaction().commit();

            DefaultTableModel temp = (DefaultTableModel) t_datos.getModel();
            temp.removeRow(t_datos.getSelectedRow());
            formatoTabla();
        }
    }//GEN-LAST:event_b_menosActionPerformed

    private void b_masActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_masActionPerformed
        // TODO add your handling code here:
        h=new Herramientas(user, 0);
        h.session(sessionPrograma);
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction().begin();
        factura=(Factura)session.get(Factura.class, factura.getIdFactura());
        Concepto con = new Concepto();
        con.setCantidad(0.0);
        con.setMedida("PZAS");
        con.setDescripcion("");
        con.setPrecio(0.0);
        con.setDescuento(0.0);
        con.setFactura(factura);
        int dato=(int)session.save(con);
        session.beginTransaction().commit();

        DefaultTableModel temp = (DefaultTableModel) t_datos.getModel();
        Object nuevo[]= {dato,0.0d,"PZAS","",0.0d, 0.0d, 0.0d};
        temp.addRow(nuevo);
        formatoTabla();
        t_datos.setRowSelectionInterval(t_datos.getRowCount()-1, t_datos.getRowCount()-1);
        t_datos.setColumnSelectionInterval(0, 0);
        t_datos.requestFocus();
    }//GEN-LAST:event_b_masActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        FileNameExtensionFilter filtroImagen=new FileNameExtensionFilter("XLS","xls");
        aviso.setFileFilter(filtroImagen);
        int r=aviso.showSaveDialog(null);
        if(r==aviso.APPROVE_OPTION)
        {
            File a=aviso.getSelectedFile();
            File archivoXLS=null;
            if(a.getName().indexOf(".xls")==-1)
            a= new File(a.getAbsoluteFile()+".xls");
            archivoXLS=a;
            try
            {
                if(archivoXLS.exists())
                archivoXLS.delete();
                archivoXLS.createNewFile();
                Workbook libro = new HSSFWorkbook();
                FileOutputStream archivo = new FileOutputStream(archivoXLS);
                Sheet hoja = libro.createSheet("datos");
                Row h1r0=hoja.createRow(0);
                h1r0.createCell(0).setCellValue("Cant");
                h1r0.createCell(1).setCellValue("Med");
                h1r0.createCell(2).setCellValue("Descripcion");
                h1r0.createCell(3).setCellValue("c/u");
                h1r0.createCell(4).setCellValue("Descuento");
                for(int x=0; x<t_datos.getRowCount(); x++)
                {
                    Row h1=hoja.createRow(x+1);
                    h1.createCell(0).setCellValue((double)t_datos.getValueAt(x, 1));
                    h1.createCell(1).setCellValue((String)t_datos.getValueAt(x, 2));
                    h1.createCell(2).setCellValue((String)t_datos.getValueAt(x, 3));
                    h1.createCell(3).setCellValue((double)t_datos.getValueAt(x, 4));
                    h1.createCell(4).setCellValue((double)t_datos.getValueAt(x, 5));
                }
                libro.write(archivo);
                Biff8EncryptionKey.setCurrentUserPassword(null);
                archivo.close();
                JOptionPane.showMessageDialog(null, "Archivo guardado!");
            }catch(Exception e){e.printStackTrace();};
        }
    }//GEN-LAST:event_jButton1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JFileChooser aviso;
    private javax.swing.JButton b_actualizar;
    private javax.swing.JButton b_cancelar;
    private javax.swing.JButton b_descargar;
    private javax.swing.JButton b_email;
    private javax.swing.JButton b_fecha_1;
    private javax.swing.JButton b_fecha_2;
    private javax.swing.JButton b_generar;
    private javax.swing.JButton b_mas;
    private javax.swing.JButton b_menos;
    private javax.swing.JButton b_pdf;
    private javax.swing.JButton b_salir;
    private javax.swing.JButton b_xml;
    private javax.swing.JComboBox c_estado;
    private javax.swing.JComboBox c_moneda;
    private javax.swing.JComboBox c_pais;
    private javax.swing.JComboBox c_tipo;
    private javax.swing.JComboBox c_tipo_cliente;
    private javax.swing.JComboBox c_tipo_emisor;
    private javax.swing.JComboBox c_tipo_receptor;
    private javax.swing.JComboBox c_tipo_vehiculo;
    private javax.swing.JDialog consulta;
    private javax.swing.JTabbedPane contenedor;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel51;
    private javax.swing.JLabel jLabel52;
    private javax.swing.JLabel jLabel53;
    private javax.swing.JLabel jLabel54;
    private javax.swing.JLabel jLabel55;
    private javax.swing.JLabel jLabel56;
    private javax.swing.JLabel jLabel57;
    private javax.swing.JLabel jLabel58;
    private javax.swing.JLabel jLabel59;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel60;
    private javax.swing.JLabel jLabel61;
    private javax.swing.JLabel jLabel62;
    private javax.swing.JLabel jLabel63;
    private javax.swing.JLabel jLabel64;
    private javax.swing.JLabel jLabel65;
    private javax.swing.JLabel jLabel66;
    private javax.swing.JLabel jLabel67;
    private javax.swing.JLabel jLabel68;
    private javax.swing.JLabel jLabel69;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel70;
    private javax.swing.JLabel jLabel71;
    private javax.swing.JLabel jLabel72;
    private javax.swing.JLabel jLabel73;
    private javax.swing.JLabel jLabel74;
    private javax.swing.JLabel jLabel75;
    private javax.swing.JLabel jLabel76;
    private javax.swing.JLabel jLabel77;
    private javax.swing.JLabel jLabel79;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel80;
    private javax.swing.JLabel jLabel81;
    private javax.swing.JLabel jLabel82;
    private javax.swing.JLabel jLabel83;
    private javax.swing.JLabel jLabel84;
    private javax.swing.JLabel jLabel85;
    private javax.swing.JLabel jLabel86;
    private javax.swing.JLabel jLabel87;
    private javax.swing.JLabel jLabel88;
    private javax.swing.JLabel jLabel89;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabel90;
    private javax.swing.JLabel jLabel91;
    private javax.swing.JLabel jLabel92;
    private javax.swing.JLabel jLabel93;
    private javax.swing.JLabel jLabel94;
    private javax.swing.JLabel jLabel95;
    private javax.swing.JLabel jLabel96;
    private javax.swing.JLabel jLabel97;
    private javax.swing.JLabel jLabel98;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel l_iva;
    private javax.swing.JLabel l_iva1;
    private javax.swing.JLabel l_subtotal;
    private javax.swing.JLabel l_total;
    private javax.swing.JProgressBar progreso;
    private javax.swing.JTextField t_anio_vehiculo;
    private javax.swing.JTextField t_calle;
    private javax.swing.JTextField t_codigo;
    private javax.swing.JTextField t_codigo_asegurado;
    private javax.swing.JTextField t_colonia;
    private javax.swing.JTextField t_color_vehiculo;
    private javax.swing.JTextField t_condiciones_pago;
    private javax.swing.JTextField t_contratante;
    private javax.swing.JTextField t_correo_emisor;
    private javax.swing.JTextField t_correo_receptor;
    private javax.swing.JTextField t_cp;
    private javax.swing.JTextField t_cuenta_pago;
    private javax.swing.JTable t_datos;
    private javax.swing.JFormattedTextField t_deducible;
    private javax.swing.JTextField t_descripcion;
    private javax.swing.JFormattedTextField t_descuento;
    private javax.swing.JTable t_facturas;
    private javax.swing.JTextField t_fecha_1;
    private javax.swing.JTextField t_fecha_2;
    private javax.swing.JTextField t_folio_factura;
    private javax.swing.JTextField t_forma_pago;
    private javax.swing.JFormattedTextField t_iva;
    private javax.swing.JFormattedTextField t_iva1;
    private javax.swing.JTextField t_marca_vehiculo;
    private javax.swing.JTextField t_medio_pago;
    private javax.swing.JTextField t_metodo_pago;
    private javax.swing.JTextField t_modelo_vehiculo;
    private javax.swing.JTextField t_motor_vehiculo;
    private javax.swing.JTextField t_municipio;
    private javax.swing.JTextField t_no_exterior;
    private javax.swing.JTextField t_no_rep;
    private javax.swing.JTextField t_nombre_emisor;
    private javax.swing.JTextField t_nombre_receptor;
    private javax.swing.JTextField t_numero;
    private javax.swing.JTextField t_pdf;
    private javax.swing.JTextField t_placas_vehiculo;
    private javax.swing.JTextField t_proveedor;
    private javax.swing.JTextField t_rfc;
    private javax.swing.JTextField t_serie_factura;
    private javax.swing.JTextField t_serie_vehiculo;
    private javax.swing.JTextField t_siniestro;
    private javax.swing.JTextField t_social;
    private javax.swing.JFormattedTextField t_subtotal;
    private javax.swing.JTextField t_tel_emisor;
    private javax.swing.JTextField t_tel_receptor;
    private javax.swing.JFormattedTextField t_tipo_cambio;
    private javax.swing.JFormattedTextField t_total;
    private javax.swing.JTextField t_uuid_factura;
    private javax.swing.JTextField t_xml;
    // End of variables declaration//GEN-END:variables
public void consulta()
    {
        h=new Herramientas(user, 0);
        h.session(sessionPrograma);
        borra_cajas();
        if (factura!=null)
        {
            Session session = HibernateUtil.getSessionFactory().openSession();
            try 
            {
                session.beginTransaction().begin();
                factura = (Factura)session.get(Factura.class, factura.getIdFactura()); 
                //Receptor
                t_iva1.setValue(factura.getIva());
                try{
                t_iva1.commitEdit();}catch(Exception e){}
                t_social.setText(factura.getNombreReceptor().trim());
                t_rfc.setText(factura.getRfcReceptor().trim());
                t_calle.setText(factura.getCalleReceptor().trim());
                t_no_exterior.setText(factura.getNumeroExteriorReceptor().trim());
                t_cp.setText(factura.getCpReceptor().trim());
                t_colonia.setText(factura.getColoniaReceptor().trim());
                t_municipio.setText(factura.getMunicipioReceptor().trim());
                c_estado.setSelectedItem(factura.getEstadoReceptor());
                c_pais.setSelectedItem(factura.getPaisReceptor());
                //Productos y servicios
                t_metodo_pago.setText(factura.getMetodoPago().trim());
                t_cuenta_pago.setText(factura.getCuentaPago().trim());
                t_tipo_cambio.setText(""+factura.getFactorCambio());
                t_tipo_cambio.setValue(factura.getFactorCambio());
                c_moneda.setSelectedItem(factura.getMoneda().trim());
                //Addenda
                c_tipo.setSelectedItem(factura.getTipoPoliza());
                if(factura.getOrden()!=null)
                {
                    if(factura.getOrden().getPoliza()!=null)
                        t_numero.setText(factura.getOrden().getPoliza().trim());
                    else
                        t_numero.setText("");
                    String tipo_cliente=factura.getOrden().getTipoCliente();
                    if(tipo_cliente!=null)
                    {
                        switch(tipo_cliente)
                        {
                            case "1": 
                                c_tipo_cliente.setSelectedItem("ASEGURADO");
                                break;
                            case "2": 
                                c_tipo_cliente.setSelectedItem("TERCERO");
                                break;
                            default:
                                c_tipo_cliente.setSelectedItem("ASEGURADO");
                                break;
                        }
                    }
                    if(factura.getOrden().getNoReporte()!=null)
                        t_no_rep.setText(factura.getOrden().getNoReporte().trim());
                    else
                        t_no_rep.setText("");
                    if(factura.getOrden().getSiniestro()!=null)
                        t_siniestro.setText(factura.getOrden().getSiniestro().trim());
                    else
                        t_siniestro.setText("");
                    if(factura.getOrden().getContratante()!=null)
                        t_contratante.setText(factura.getOrden().getContratante().trim());
                    else
                        t_contratante.setText("");
                    if(factura.getOrden().getCodigoAsegurado()!=null)
                        t_codigo_asegurado.setText(factura.getOrden().getCodigoAsegurado().trim());
                    else
                        t_codigo_asegurado.setText("");
                }
                if(factura.getOrdenExterna()!=null)
                {
                    if(factura.getOrdenExterna().getPoliza()!=null)
                        t_numero.setText(factura.getOrdenExterna().getPoliza().trim());
                    else
                        t_numero.setText("");
                    String tipo_cliente=factura.getOrdenExterna().getTipoCliente();
                    if(tipo_cliente!=null)
                    {
                        switch(tipo_cliente)
                        {
                            case "1": 
                                c_tipo_cliente.setSelectedItem("ASEGURADO");
                                break;
                            case "2": 
                                c_tipo_cliente.setSelectedItem("TERCERO");
                                break;
                            default:
                                c_tipo_cliente.setSelectedItem("ASEGURADO");
                                break;
                        }
                    }
                    if(factura.getOrdenExterna().getNoReporte()!=null)
                        t_no_rep.setText(factura.getOrdenExterna().getNoReporte().trim());
                    else
                        t_no_rep.setText("");
                    if(factura.getOrdenExterna().getSiniestro()!=null)
                        t_siniestro.setText(factura.getOrdenExterna().getSiniestro().trim());
                    else
                        t_siniestro.setText("");
                    if(factura.getOrdenExterna().getContratante()!=null)
                        t_contratante.setText(factura.getOrdenExterna().getContratante().trim());
                    else
                        t_contratante.setText("");
                    if(factura.getOrdenExterna().getCodigoAsegurado()!=null)
                        t_codigo_asegurado.setText(factura.getOrdenExterna().getCodigoAsegurado().trim());
                    else
                        t_codigo_asegurado.setText("");
                }
                t_codigo.setText(""+factura.getIdFactura());
                
                Configuracion config=(Configuracion)session.get(Configuracion.class, 1);
                c_tipo_emisor.setSelectedItem(factura.getTipoEmisor().trim());
                if(factura.getContactoEmisor().compareTo("")==0)
                    t_nombre_emisor.setText(config.getContacto().trim());
                else
                    t_nombre_emisor.setText(factura.getContactoEmisor().trim());
                if(factura.getCorreoEmisor().compareTo("")==0)
                    t_correo_emisor.setText(config.getMail().trim());
                else
                    t_correo_emisor.setText(factura.getCorreoEmisor().trim());
                t_tel_emisor.setText(factura.getTelefonoEmisor().trim());
                if(factura.getDescripcionEmisor()!=null)
                    t_descripcion.setText(factura.getDescripcionEmisor().trim());
                else
                    t_descripcion.setText("");
                
                c_tipo_receptor.setSelectedItem(factura.getTipoReceptor());
                t_nombre_receptor.setText(factura.getContactoReceptor().trim());
                t_correo_receptor.setText(factura.getCorreoReceptor().trim());
                t_tel_receptor.setText(factura.getTelefonoReceptor().trim());
                if(factura.getOrden()!=null)
                {
                    if(factura.getOrden().getTipoVehiculo()!=null)
                        c_tipo_vehiculo.setSelectedItem(factura.getOrden().getTipoVehiculo());
                    if(factura.getOrden().getTipo()!=null)
                        t_modelo_vehiculo.setText(factura.getOrden().getTipo().getTipoNombre().trim());
                    if(factura.getOrden().getColor()!=null)
                        t_color_vehiculo.setText(factura.getOrden().getColor().trim());
                    if(factura.getOrden().getMarca()!=null)
                        t_marca_vehiculo.setText(factura.getOrden().getMarca().getMarcaNombre().trim());
                    if(factura.getOrden().getModelo()!=null)
                        t_anio_vehiculo.setText(""+factura.getOrden().getModelo());
                    if(factura.getOrden().getNoSerie()!=null)
                        t_serie_vehiculo.setText(factura.getOrden().getNoSerie().trim());
                    if(factura.getOrden().getNoPlacas()!=null)
                        t_placas_vehiculo.setText(factura.getOrden().getNoPlacas().trim());
                    if(factura.getOrden().getNoMotor()!=null)
                        t_motor_vehiculo.setText(factura.getOrden().getNoMotor().trim());                
                    if(factura.getOrden().getDeducible()!=null)
                    {
                        t_deducible.setText(""+factura.getOrden().getDeducible());
                        t_deducible.setValue(factura.getOrden().getDeducible());
                    }
                }
                if(factura.getOrdenExterna()!=null)
                {
                    if(factura.getOrdenExterna().getTipoVehiculo()!=null)
                        c_tipo_vehiculo.setSelectedItem(factura.getOrdenExterna().getTipoVehiculo());
                    if(factura.getOrdenExterna().getTipo()!=null)
                        t_modelo_vehiculo.setText(factura.getOrdenExterna().getTipo().getTipoNombre().trim());
                    if(factura.getOrdenExterna().getColor()!=null)
                        t_color_vehiculo.setText(factura.getOrdenExterna().getColor().trim());
                    if(factura.getOrdenExterna().getMarca()!=null)
                        t_marca_vehiculo.setText(factura.getOrdenExterna().getMarca().getMarcaNombre().trim());
                    if(factura.getOrdenExterna().getModelo()!=null)
                        t_anio_vehiculo.setText(""+factura.getOrdenExterna().getModelo());
                    if(factura.getOrdenExterna().getNoSerie()!=null)
                        t_serie_vehiculo.setText(factura.getOrdenExterna().getNoSerie().trim());
                    if(factura.getOrdenExterna().getNoPlacas()!=null)
                        t_placas_vehiculo.setText(factura.getOrdenExterna().getNoPlacas().trim());
                    if(factura.getOrdenExterna().getNoMotor()!=null)
                        t_motor_vehiculo.setText(factura.getOrdenExterna().getNoMotor().trim());                
                    if(factura.getOrdenExterna().getDeducible()!=null)
                    {
                        t_deducible.setText(""+factura.getOrdenExterna().getDeducible());
                        t_deducible.setValue(factura.getOrdenExterna().getDeducible());
                    }
                }
                if(factura.getProveedor()!=null)
                    t_proveedor.setText(factura.getProveedor().trim());
                t_medio_pago.setText(factura.getMedioPago());
                SimpleDateFormat form = new SimpleDateFormat("yyyy-MM-dd");
                if(factura.getFechaMedioPago()!=null)
                    t_fecha_1.setText(form.format(factura.getFechaMedioPago()));
                t_condiciones_pago.setText(factura.getCondicionesPago());
                if(factura.getFechaCondicionesPago()!=null)
                    t_fecha_2.setText(form.format(factura.getFechaCondicionesPago()));
                
                //cargar las partidas
                Concepto[] partidas = (Concepto[])session.createCriteria(Concepto.class).
                                        add(Restrictions.eq("factura.idFactura", factura.getIdFactura())).
                                        addOrder(Order.asc("idConcepto")).list().toArray(new Concepto[0]);
                for(int a=0; a<partidas.length; a++)
                {
                    double total_lista=partidas[a].getPrecio()*partidas[a].getCantidad();
                    double descuento=partidas[a].getDescuento()/100;
                    double total=total_lista-(total_lista*descuento);
                    model.addRow(
                            new Object[]
                            {
                                partidas[a].getIdConcepto(),
                                partidas[a].getCantidad(), 
                                partidas[a].getMedida(), 
                                partidas[a].getDescripcion(), 
                                partidas[a].getPrecio(),
                                partidas[a].getDescuento(),
                               total
                            });
                }
                iva=config.getIva();
                t_datos.setModel(model);
                formatoTabla();
                sumaTotales();
                
                if(factura.getEstadoFactura().compareTo("Facturado")==0)
                {
                    if(factura.getSerie()!=null)
                        t_serie_factura.setText(factura.getSerie());
                    if(factura.getFolio()!=null)
                        t_folio_factura.setText(factura.getFolio());
                    if(factura.getNombreDocumento()!=null)
                    {
                        t_xml.setText(factura.getNombreDocumento()+".xml");
                        t_pdf.setText(factura.getNombreDocumento()+".pdf");
                    }
                    if(factura.getFFiscal()!=null)
                        this.t_uuid_factura.setText(factura.getFFiscal());
                    contenedor.setSelectedIndex(3);
                    if(this.user.getAutorizarFactura()==true)
                        permiso=true;
                    else
                        permiso=false;
                    this.habilita(false, true); 
                }
                else
                {
                    if(factura.getEstadoFactura().compareTo("Cancelado")==0)
                    {
                        if(factura.getSerie()!=null)
                            t_serie_factura.setText(factura.getSerie());
                        if(factura.getFolio()!=null)
                            t_folio_factura.setText(factura.getFolio());
                        if(factura.getNombreDocumento()!=null)
                        {
                            t_xml.setText(factura.getNombreDocumento()+".xml");
                            t_pdf.setText(factura.getNombreDocumento()+".pdf");
                        }
                        if(factura.getFFiscal()!=null)
                            this.t_uuid_factura.setText(factura.getFFiscal());
                        permiso=false;
                        habilita(false, false);
                    }
                    else
                    {
                        if(this.user.getAutorizarFactura()==true)
                            permiso=true;
                        else
                            permiso=false;
                        this.habilita(true, false);
                    }
                }
                session.beginTransaction().commit();
            }
            catch (HibernateException he) 
            {
                he.printStackTrace();
                session.getTransaction().rollback();
                borra_cajas();
                habilita(false, false);
                b_generar.setEnabled(false);
                JOptionPane.showMessageDialog(null, "Error al consultar la factura");
            }
            finally
            {
                if(session!=null)
                    if(session.isConnected())
                        session.close();
            }
        }
        else
        {
            borra_cajas();
            this.dispose();
        }
    }

    void borra_cajas()
    {
        //Receptor
        t_social.setText("");
        t_rfc.setText("");
        t_calle.setText("");
        t_no_exterior.setText("");
        t_cp.setText("");
        t_colonia.setText("");
        t_municipio.setText("");
        //Productos y servicios
        t_metodo_pago.setText("03");
        t_cuenta_pago.setText("");
        t_tipo_cambio.setText("1.0000");
        t_tipo_cambio.setValue(1.0d);
        t_datos.setModel(ModeloTablaReporte(0, columnas));
        formatoTabla();
        //Addenda
        c_tipo.setSelectedItem("AUTOS");
        t_numero.setText("");
        c_tipo_cliente.setSelectedItem("ASEGURADO");
        t_no_rep.setText("");
        t_siniestro.setText("");
        t_contratante.setText("");
        t_codigo_asegurado.setText("");
        t_codigo.setText("");
        c_tipo_emisor.setSelectedItem("MATRIZ");
        t_nombre_emisor.setText("");
        t_correo_emisor.setText("");
        t_tel_emisor.setText("");
        t_descripcion.setText("");
        
        c_tipo_receptor.setSelectedItem("COORDINADOR");
        t_nombre_receptor.setText("");
        t_correo_receptor.setText("");
        t_tel_receptor.setText("");
        c_tipo_vehiculo.setSelectedItem("PARTICULAR");
        t_modelo_vehiculo.setText("");
        t_color_vehiculo.setText("");
        t_marca_vehiculo.setText("");
        t_anio_vehiculo.setText("");
        t_serie_vehiculo.setText("");
        t_placas_vehiculo.setText("");
        t_motor_vehiculo.setText("");
        t_proveedor.setText("");
        t_deducible.setText("0.00");
        t_deducible.setValue(0.00d);
        
        t_medio_pago.setText("");
        t_fecha_1.setText("AAAA-MM-DD");
        t_condiciones_pago.setText("");
        t_fecha_2.setText("AAAA-MM-DD");
        iva=0;
    }
    
    void habilita(boolean edo, boolean edo2)
    {
        //Receptor
        t_social.setEnabled(edo);
        t_rfc.setEnabled(edo);
        t_calle.setEnabled(edo);
        t_no_exterior.setEnabled(edo);
        t_cp.setEnabled(edo);
        t_colonia.setEnabled(edo);
        t_municipio.setEnabled(edo);
        c_estado.setEnabled(edo);
        c_pais.setEnabled(edo);
        //Productos y servicios
        t_metodo_pago.setEnabled(edo);
        t_cuenta_pago.setEnabled(edo);
        t_tipo_cambio.setEnabled(edo);
        c_moneda.setEnabled(edo);
        t_datos.setEnabled(edo);
        t_descuento.setEnabled(edo);
        t_iva1.setEnabled(edo);
        b_mas.setEnabled(edo);
        b_menos.setEnabled(edo);
        //Addenda
        c_tipo.setEnabled(edo);
        t_numero.setEnabled(edo);
        c_tipo_cliente.setEnabled(edo);
        t_no_rep.setEnabled(edo);
        t_siniestro.setEnabled(edo);
        t_contratante.setEnabled(edo);
        t_codigo_asegurado.setEnabled(edo);
        t_codigo.setEnabled(false);
        c_tipo_emisor.setEnabled(edo);
        t_nombre_emisor.setEnabled(edo);
        t_correo_emisor.setEnabled(edo);
        t_tel_emisor.setEnabled(edo);
        t_descripcion.setEnabled(edo);
        
        c_tipo_receptor.setEnabled(edo);
        t_nombre_receptor.setEnabled(edo);
        t_correo_receptor.setEnabled(edo);
        t_tel_receptor.setEnabled(edo);
        c_tipo_vehiculo.setEnabled(edo);
        t_modelo_vehiculo.setEnabled(false);
        t_color_vehiculo.setEnabled(edo);
        t_marca_vehiculo.setEnabled(false);
        t_anio_vehiculo.setEnabled(edo);
        t_serie_vehiculo.setEnabled(edo);
        t_placas_vehiculo.setEnabled(edo);
        t_motor_vehiculo.setEnabled(edo);
        t_proveedor.setEnabled(edo);
        t_deducible.setEnabled(edo);
        
        t_medio_pago.setEnabled(edo);
        t_fecha_1.setEnabled(edo);
        b_fecha_1.setEnabled(edo);
        t_condiciones_pago.setEnabled(edo);
        t_fecha_2.setEnabled(edo);
        b_fecha_2.setEnabled(edo);
        b_actualizar.setEnabled(edo);
        if(edo==false)
            b_generar.setEnabled(edo);
        else
            b_generar.setEnabled(permiso);
        b_xml.setEnabled(edo2);
        b_pdf.setEnabled(edo2);
        b_email.setEnabled(edo2);
    }
    
    public void sumaTotales()
    {
        try
        {
            iva=Integer.parseInt(t_iva1.getValue().toString());
            BigDecimal total=new BigDecimal("0.0");
            for(int ren=0; ren<t_datos.getRowCount(); ren++)
                total = total.add(new BigDecimal(t_datos.getValueAt(ren, 6).toString()));
            t_subtotal.setValue(new Double(total.toString()));
            BigDecimal valor_iva=new BigDecimal(""+iva);
            valor_iva=valor_iva.divide(new BigDecimal("100"));
            valor_iva=total.multiply(valor_iva);
            System.out.println("iva:"+iva+"valor:"+valor_iva.toString());
            t_iva.setValue(new Double(valor_iva.toString()));
            total = total.add(valor_iva);
            t_total.setValue(new Double(total.toString()));
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    
    DefaultTableModel ModeloTablaReporte(int renglones, String columnas[])
        {
            model = new DefaultTableModel(new Object [renglones][7], columnas)
            {
                Class[] types = new Class [] {
                    java.lang.String.class,
                    java.lang.Double.class, 
                    java.lang.String.class, 
                    java.lang.String.class, 
                    java.lang.Double.class,
                    java.lang.Double.class,
                    java.lang.Double.class
                };
                boolean[] canEdit = new boolean [] {
                    false, true, true, true, true, true, false
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

                            case 1:
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
                                                session.beginTransaction().begin();
                                                Concepto con = (Concepto)session.get(Concepto.class, Integer.parseInt(t_datos.getValueAt(row, 0).toString()));
                                                con.setCantidad((double)value);
                                                session.update(con);
                                                session.beginTransaction().commit();
                                                
                                                vector.setElementAt(value, col);
                                                this.dataVector.setElementAt(vector, row);
                                                fireTableCellUpdated(row, col);
                                                double suma=(((double)t_datos.getValueAt(row, 4))*((double)value));
                                                double desc=((double)t_datos.getValueAt(row, 5))/100;
                                                double total=suma-(suma*desc);
                                                t_datos.setValueAt(total, row, 6);
                                            }catch(Exception e)
                                            {
                                                session.beginTransaction().rollback();
                                                e.printStackTrace();
                                                JOptionPane.showMessageDialog(null, "Error al almacenar los datos");
                                            }
                                            finally
                                            {
                                                if(session!=null)
                                                    if(session.isOpen())
                                                        session.close();
                                            }
                                    }
                                    sumaTotales();
                                    break;
                            case 2:
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
                                                session.beginTransaction().begin();
                                                Concepto con = (Concepto)session.get(Concepto.class, Integer.parseInt(t_datos.getValueAt(row, 0).toString()));
                                                con.setMedida((String)value);
                                                session.update(con);
                                                session.beginTransaction().commit();
                                                
                                                vector.setElementAt(value, col);
                                                this.dataVector.setElementAt(vector, row);
                                                fireTableCellUpdated(row, col);
                                            }catch(Exception e)
                                            {
                                                session.beginTransaction().rollback();
                                                e.printStackTrace();
                                                JOptionPane.showMessageDialog(null, "Error al almacenar los datos");
                                            }
                                            finally
                                            {
                                                if(session!=null)
                                                    if(session.isOpen())
                                                        session.close();
                                            }
                                    }
                                    sumaTotales();
                                    break;
                            case 3:
                                    if(vector.get(col)==null)
                                    {
                                        vector.setElementAt(value, col);
                                        this.dataVector.setElementAt(vector, row);
                                        fireTableCellUpdated(row, col);
                                    }
                                    else
                                    {
                                        if(value.toString().compareTo("")!=0)
                                        {
                                            Session session = HibernateUtil.getSessionFactory().openSession();
                                            try 
                                            {
                                                session.beginTransaction().begin();
                                                Concepto con = (Concepto)session.get(Concepto.class, Integer.parseInt(t_datos.getValueAt(row, 0).toString()));
                                                con.setDescripcion(value.toString());
                                                session.update(con);
                                                session.beginTransaction().commit();
                                                
                                                vector.setElementAt(value, col);
                                                this.dataVector.setElementAt(vector, row);
                                                fireTableCellUpdated(row, col);
                                            }catch(Exception e)
                                            {
                                                session.beginTransaction().rollback();
                                                e.printStackTrace();
                                                JOptionPane.showMessageDialog(null, "Error al almacenar los datos");
                                            }
                                            finally
                                            {
                                                if(session!=null)
                                                    if(session.isConnected())
                                                        session.close();
                                            }
                                        }
                                    }
                                    sumaTotales();
                                    break;
                            case 4:
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
                                                session.beginTransaction().begin();
                                                Concepto con = (Concepto)session.get(Concepto.class, Integer.parseInt(t_datos.getValueAt(row, 0).toString()));
                                                con.setPrecio((double)value);
                                                session.update(con);
                                                session.beginTransaction().commit();
                                                
                                                vector.setElementAt(value, col);
                                                this.dataVector.setElementAt(vector, row);
                                                fireTableCellUpdated(row, col);
                                                double suma=(((double)value)*((double)t_datos.getValueAt(row, 1)));
                                                double desc=((double)t_datos.getValueAt(row, 5))/100;
                                                double total=suma-(suma*desc);
                                                t_datos.setValueAt(total, row, 6);
                                            }catch(Exception e)
                                            {
                                                session.beginTransaction().rollback();
                                                e.printStackTrace();
                                                JOptionPane.showMessageDialog(null, "Error al almacenar los datos");
                                            }
                                            finally
                                            {
                                                if(session!=null)
                                                    if(session.isOpen())
                                                        session.close();
                                            }
                                    }
                                    sumaTotales();
                                    break;    
                            case 5:
                                    if(vector.get(col)==null)
                                    {
                                        vector.setElementAt(value, col);
                                        this.dataVector.setElementAt(vector, row);
                                        fireTableCellUpdated(row, col);
                                    }
                                    else
                                    {
                                        if((double)value<=100.0d && (double)value>=0.0d)
                                        {
                                             Session session = HibernateUtil.getSessionFactory().openSession();
                                            try 
                                            {
                                                session.beginTransaction().begin();
                                                Concepto con = (Concepto)session.get(Concepto.class, Integer.parseInt(t_datos.getValueAt(row, 0).toString()));
                                                con.setDescuento((double)value);
                                                session.update(con);
                                                session.beginTransaction().commit();
                                                
                                                vector.setElementAt(value, col);
                                                this.dataVector.setElementAt(vector, row);
                                                fireTableCellUpdated(row, col);
                                                double suma=(((double)t_datos.getValueAt(row, 4))*((double)t_datos.getValueAt(row, 1)));
                                                double desc=((double)value)/100;
                                                double total=suma-(suma*desc);
                                                t_datos.setValueAt(total, row, 6);
                                            }catch(Exception e)
                                            {
                                                session.beginTransaction().rollback();
                                                e.printStackTrace();
                                                JOptionPane.showMessageDialog(null, "Error al almacenar los datos");
                                            }
                                            finally
                                            {
                                                if(session!=null)
                                                    if(session.isConnected())
                                                        session.close();
                                            }
                                        }
                                    }
                                    sumaTotales();
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
    
    DefaultTableModel ModeloTablaFactura(int renglones, String columnas[])
        {
            modeloFactura = new DefaultTableModel(new Object [renglones][6], columnas)
            {
                Class[] types = new Class [] {
                    java.lang.String.class, 
                    java.lang.String.class, 
                    java.lang.String.class, 
                    java.lang.String.class,
                    java.lang.String.class,
                    java.lang.String.class,
                    java.lang.String.class,
                    java.lang.String.class
                };
                boolean[] canEdit = new boolean [] {
                    false, false, false, false, false, false, false, false
                };

                public void setValueAt(Object value, int row, int col)
                 {
                        Vector vector = (Vector)this.dataVector.elementAt(row);
                        Object celda = ((Vector)this.dataVector.elementAt(row)).elementAt(col);
                        switch(col)
                        {
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
            return modeloFactura;
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
                      column.setPreferredWidth(5);
                      break;
                  case 1:
                      column.setPreferredWidth(20);
                      break;
                  case 2:
                      column.setPreferredWidth(20);
                      break;
                  case 3:
                      column.setPreferredWidth(350);
                      break;
                  case 4:
                      column.setPreferredWidth(40);
                      break;
                  case 5:
                      column.setPreferredWidth(40);
                      break;
                  case 6:
                      column.setPreferredWidth(40);
                      break;
                  default:
                      column.setPreferredWidth(5);
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
        t_datos.setDefaultRenderer(Double.class, formato); 
        t_datos.setDefaultRenderer(Integer.class, formato);
        t_datos.setDefaultRenderer(String.class, formato);
        t_datos.setDefaultRenderer(Boolean.class, formato);
    }
    
    public void tabla_tamanios_factura()
    {
        TableColumnModel col_model = t_facturas.getColumnModel();
        for (int i=0; i<t_facturas.getColumnCount(); i++)
        {
  	      TableColumn column = col_model.getColumn(i);
              switch(i)
              {
                  case 0:
                      column.setPreferredWidth(90);
                      break;
                  case 1:
                      column.setPreferredWidth(140);
                      break;
                  case 2:
                      column.setPreferredWidth(90);
                      break;
                  case 3:
                      column.setPreferredWidth(30);
                      break;
                  case 4:
                      column.setPreferredWidth(30);
                      break;
                  case 5:
                      column.setPreferredWidth(100);
                      break;
                  case 6:
                      column.setPreferredWidth(90);
                      break;
                  case 7:
                      column.setPreferredWidth(40);
                      break;
                  default:
                      column.setPreferredWidth(30);
                      break;
              }
        }
        JTableHeader header = t_facturas.getTableHeader();
        header.setForeground(Color.white);
    }
    
    public void formatoTablafactura()
    {
        Color c1 = new java.awt.Color(2, 135, 242);   
        for(int x=0; x<t_facturas.getColumnModel().getColumnCount(); x++)
            t_facturas.getColumnModel().getColumn(x).setHeaderRenderer(new Render1(c1));
        tabla_tamanios_factura();
        t_facturas.setShowVerticalLines(true);
        t_facturas.setShowHorizontalLines(true);
        t_facturas.setDefaultRenderer(Double.class, formato); 
        t_facturas.setDefaultRenderer(Integer.class, formato);
    }
    
    public int OrdenFacturado(Orden or)
    {
        Factura[] fac=(Factura[])or.getFacturas().toArray(new Factura[0]);
        for(int r=0; r<fac.length; r++)
        {
            if(fac[r].getEstadoFactura().compareTo("Pendiente")==0 || fac[r].getEstadoFactura().compareTo("Facturado")==0)
                return fac[r].getIdFactura();
        }
        return -1;
    }
    
    public boolean generarXML(Orden ord, String nombre)
    {
        boolean edo=false;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try
        {
            error="";
            session.beginTransaction().begin();
            Configuracion config=(Configuracion)session.get(Configuracion.class, 1);
            BigDecimal valorIva=new BigDecimal(""+t_iva1.getValue().toString());
            javaToXML miXML= new javaToXML();
            TFactDocMX documento=new TFactDocMX();
            documento.setVersion(BigInteger.valueOf(5));
                TFactDocMX.Identificacion id = new TFactDocMX.Identificacion();
                id.setCdgPaisEmisor(mysuite.TSenderCountryCode.MX);
                    id.setTipoDeComprobante(mysuite.TTipoDeDocumento.FACTURA);
                id.setRFCEmisor(config.getRfc());
                id.setRazonSocialEmisor(config.getNombre());
                id.setUsuario(config.getUsuario_1());
                id.setNumeroInterno(t_codigo.getText());//numero de factura interna
                if(config.getMunicipio().trim().compareTo("")!=0)
                {
                    if(config.getEstado().compareTo("")!=0)
                        id.setLugarExpedicion(config.getMunicipio()+", "+ config.getEstado());
                    else
                        id.setLugarExpedicion(config.getMunicipio());
                }
                else
                {
                    if(config.getEstado().compareTo("")!=0)
                        id.setLugarExpedicion(config.getEstado());
                    else
                        id.setLugarExpedicion("TOLUCA, ESTADO DE MEXICO");
                }
                if(t_cuenta_pago.getText().compareTo("")!=0)
                    id.setNumCtaPago(t_cuenta_pago.getText());
            documento.setIdentificacion(id);
                TDictionaries diccionarios = new TDictionaries();
                    TDictionary direccion = new TDictionary();
                    direccion.setName("email");
                        TDictionary.Entry de= new TDictionary.Entry();
                        de.setK("from");
                        de.setV("ACCOUNT_OWNER");
                    direccion.getEntry().add(de);
                    TDictionary.Entry para= new TDictionary.Entry();
                        para.setK("to");
                        para.setV(t_correo_emisor.getText().trim());
                    direccion.getEntry().add(para);
                diccionarios.getDictionary().add(direccion);
            documento.setProcesamiento(diccionarios);
                TFactDocMX.Emisor envia=new TFactDocMX.Emisor();
                    mysuite.TDomicilioMexicano dFiscal= new mysuite.TDomicilioMexicano();
                    dFiscal.setCalle(config.getDireccion().trim());
                    dFiscal.setNumeroExterior(config.getNo().trim());
                    //dFiscal.setNumeroInterior("");
                    //dFiscal.setLocalidad("");
                    //dFiscal.setReferencia("");
                    dFiscal.setColonia(config.getColonia().trim());
                    dFiscal.setMunicipio(config.getMunicipio().trim());
                    dFiscal.setEstado(config.getEstado());
                    dFiscal.setPais(config.getPais());
                    dFiscal.setCodigoPostal(config.getCp().trim());
                    //dFiscal.setNomContacto("");
                    //dFiscal.setTelContacto("");
                envia.setDomicilioFiscal(dFiscal);
                //si es diferente al domicilio fiscal usar este camposen blanco no obligatorios
                    //mysuite.TDomicilioMexicano dEmision= new mysuite.TDomicilioMexicano();
                    //dEmision.setCalle("TDomicilioMexicano");
                    //dEmision.setNumeroExterior("S/N");
                    //dEmision.setNumeroInterior("");
                    //dEmision.setLocalidad("");
                    //dEmision.setReferencia("");
                    //dEmision.setColonia("SAN PEDRO TOTOLTEPE");
                    //dEmision.setMunicipio("SAN PEDRO TOTOLTEPEC");
                    //dEmision.setEstado("MEXICO");
                    //dEmision.setPais("MEXICO");
                    //dEmision.setCodigoPostal("50200");
                    //dEmision.setNomContacto("");
                    //dEmision.setTelContacto("");
                //envia.setDomicilioDeEmision(dEmision);
                    TFactDocMX.Emisor.RegimenFiscal enviaRegimen = new TFactDocMX.Emisor.RegimenFiscal();
                    enviaRegimen.getRegimen().add("REGIMEN GENERAL DE LEY DE PERSONAS MORALES");
                envia.setRegimenFiscal(enviaRegimen);
            documento.setEmisor(envia);
                TFactDocMX.Receptor recibe = new TFactDocMX.Receptor();
                recibe.setCdgPaisReceptor(TCountryCode.MX);
                recibe.setRFCReceptor(t_rfc.getText().trim());
                recibe.setNombreReceptor(t_social.getText().trim()); 
                    TFactDocMX.Receptor.Domicilio domicilio= new TFactDocMX.Receptor.Domicilio();
                        mysuite.TDomicilioMexicano dFiscalRecibe= new mysuite.TDomicilioMexicano();
                        dFiscalRecibe.setCalle(t_calle.getText().trim());
                        dFiscalRecibe.setNumeroExterior(t_no_exterior.getText().trim());
                        //dFiscalRecibe.setNumeroInterior("");
                        //dFiscalRecibe.setLocalidad("");
                        //dFiscalRecibe.setReferencia("");
                        dFiscalRecibe.setColonia(t_colonia.getText().trim());
                        dFiscalRecibe.setMunicipio(t_municipio.getText().trim());
                        dFiscalRecibe.setEstado(c_estado.getSelectedItem().toString());
                        dFiscalRecibe.setPais(c_pais.getSelectedItem().toString());
                        dFiscalRecibe.setCodigoPostal(t_cp.getText().trim());
                        //dFiscalRecibe.setNomContacto("");
                        //dFiscalRecibe.setTelContacto("");
                    domicilio.setDomicilioFiscalMexicano(dFiscalRecibe);
                    domicilio.setOtroDomicilio(null);
                recibe.setDomicilio(domicilio);
                recibe.setDomicilioDeRecepcion(null);
           documento.setReceptor(recibe);
                TFactDocMX.Conceptos conceptos = new TFactDocMX.Conceptos();
                BigDecimal big_total_bruto= new BigDecimal("0.0");
                BigDecimal big_sub_total=new BigDecimal("0.0");
                TDescuentosYRecargos descuentosyRecargos =new TDescuentosYRecargos(); //agrupa los descuentos y recargos
                for(int ren=0; ren<t_datos.getRowCount(); ren++)
                {
                    //descuento
                    BigDecimal big_descuento=new BigDecimal(t_datos.getValueAt(ren, 5).toString());
                    BigDecimal big_porciento_dectuento=big_descuento.divide(new BigDecimal("100"));
                    //cantidades de lista
                    BigDecimal big_cantidad=new BigDecimal(t_datos.getValueAt(ren, 1).toString());
                    BigDecimal big_precio_lista=new BigDecimal(t_datos.getValueAt(ren, 4).toString());
                    BigDecimal big_total_lista=big_precio_lista.multiply(big_cantidad);
                    big_total_bruto = big_total_bruto.add(big_total_lista);
                    //cantidades netas
                    BigDecimal big_precio_neto=big_precio_lista.subtract(big_precio_lista.multiply(big_porciento_dectuento));
                    BigDecimal big_total_neto=big_precio_neto.multiply(big_cantidad);
                    big_sub_total = big_sub_total.add(big_total_neto);
                    
                    TFactDocMX.Conceptos.Concepto renglon = new TFactDocMX.Conceptos.Concepto();
                    renglon.setCantidad(big_cantidad.setScale(2, BigDecimal.ROUND_HALF_UP));
                    renglon.setUnidadDeMedida("EA"/*t_datos.getValueAt(ren, 2).toString().trim()*/);
                    renglon.setCodigo("1");
                    renglon.setDescripcion(t_datos.getValueAt(ren, 3).toString().trim());
                    
                        mysuite.TNonNegativeAmount unitario= new mysuite.TNonNegativeAmount();
                        unitario.setValue(big_precio_neto.setScale(2, BigDecimal.ROUND_HALF_UP));
                    renglon.setValorUnitario(unitario);
                        mysuite.TNonNegativeAmount importe= new mysuite.TNonNegativeAmount();
                        importe.setValue(big_total_neto.setScale(2, BigDecimal.ROUND_HALF_UP));
                    renglon.setImporte(importe);
                        TConceptoEx  concepto_ext= new TConceptoEx();
                            mysuite.TNonNegativeAmount precio_lista= new mysuite.TNonNegativeAmount();
                            precio_lista.setValue(big_precio_lista.setScale(2, BigDecimal.ROUND_HALF_UP));
                        concepto_ext.setPrecioLista( precio_lista);
                            mysuite.TNonNegativeAmount importe_lista= new mysuite.TNonNegativeAmount();
                            importe_lista.setValue( big_total_lista.setScale(2, BigDecimal.ROUND_HALF_UP));
                        concepto_ext.setImporteLista(importe_lista);
                        if( ((double)t_datos.getValueAt(ren, 5))>0 )
                        {
                            TDescuentosYRecargos descuentosRecargos =new TDescuentosYRecargos();//sirve para totales
                                TDiscountOrRecharge desc= new TDiscountOrRecharge();
                                desc.setOperacion(TAllowanceChargeType.DESCUENTO);
                                desc.setImputacion(TSettlementType.FUERA_DE_FACTURA);
                                desc.setServicio(TSpecialServicesType.REBAJA);
                                desc.setDescripcion("Descuento");
                                desc.setBase( importe_lista);
                                desc.setTasa(BigDecimal.valueOf( (double)t_datos.getValueAt(ren, 5) ).setScale(2, BigDecimal.ROUND_HALF_UP));
                                    mysuite.TNonNegativeAmount monto = new mysuite.TNonNegativeAmount();
                                    monto.setValue( big_total_lista.multiply(big_porciento_dectuento).setScale(2, BigDecimal.ROUND_HALF_UP));
                                desc.setMonto(monto);
                                descuentosRecargos.getDescuentoORecargo().add(desc);
                       concepto_ext.setDescuentosYRecargos(descuentosRecargos);
                                int posicion=existe(descuentosyRecargos, desc);
                                if(posicion==-1)
                                {
                                    TDiscountOrRecharge desc1= new TDiscountOrRecharge();
                                    desc1.setOperacion(TAllowanceChargeType.DESCUENTO);
                                    desc1.setImputacion(TSettlementType.FUERA_DE_FACTURA);
                                    desc1.setServicio(TSpecialServicesType.REBAJA);
                                    desc1.setDescripcion("Descuento");
                                    desc1.setBase(importe_lista);
                                    desc1.setTasa(BigDecimal.valueOf( (double)t_datos.getValueAt(ren, 5) ).setScale(2, BigDecimal.ROUND_HALF_UP));
                                        mysuite.TNonNegativeAmount monto1 = new mysuite.TNonNegativeAmount();
                                        monto1.setValue(big_total_lista.multiply(big_porciento_dectuento).setScale(2, BigDecimal.ROUND_HALF_UP));
                                    desc1.setMonto(monto1);
                                    descuentosyRecargos.getDescuentoORecargo().add(desc1);
                                }
                                else
                                {
                                    TDiscountOrRecharge aux=descuentosyRecargos.getDescuentoORecargo().get(posicion);
                                    mysuite.TNonNegativeAmount baseAux = new mysuite.TNonNegativeAmount();
                                    BigDecimal n1 = aux.getBase().getValue();
                                    BigDecimal n2 = desc.getBase().getValue();
                                    BigDecimal n3 = n1.add(n2).setScale(2, BigDecimal.ROUND_HALF_UP);
                                    baseAux.setValue(n3);
                                    aux.setBase(baseAux);
                                    mysuite.TNonNegativeAmount montoAux = new mysuite.TNonNegativeAmount();
                                    BigDecimal a1=aux.getBase().getValue();
                                    BigDecimal a2=aux.getTasa().divide(new BigDecimal("100")).setScale(2, BigDecimal.ROUND_HALF_UP);
                                    BigDecimal a3=a1.multiply(a2).setScale(2, BigDecimal.ROUND_HALF_UP);
                                    montoAux.setValue(a3);
                                    aux.setMonto(montoAux);
                                    descuentosyRecargos.getDescuentoORecargo().set(posicion, aux);
                                }
                        }
                                TImpuestos impuestos = new TImpuestos();//sirve para totales
                                TTax imp= new TTax();
                                imp.setContexto(TTaxContext.FEDERAL);
                                imp.setOperacion(TTaxOperation.TRASLADO);
                                imp.setCodigo("IVA");
                                    mysuite.TNonNegativeAmount baseIMP = new mysuite.TNonNegativeAmount();
                                    baseIMP.setValue(big_total_neto.setScale(2, BigDecimal.ROUND_HALF_UP));
                                imp.setBase(baseIMP);
                                imp.setTasa(valorIva.setScale(2, BigDecimal.ROUND_HALF_UP));
                                    mysuite.TNonNegativeAmount montoIMP = new mysuite.TNonNegativeAmount();
                                    BigDecimal porc=valorIva.divide(new BigDecimal("100.0")).setScale(2, BigDecimal.ROUND_HALF_UP);
                                    BigDecimal monto=big_total_neto.multiply(porc).setScale(2, BigDecimal.ROUND_HALF_UP);
                                    montoIMP.setValue(monto.setScale(2, BigDecimal.ROUND_HALF_UP));
                                imp.setMonto(montoIMP);
                            impuestos.getImpuesto().add(imp);
                        concepto_ext.setImpuestos(impuestos);
                        mysuite.TNonNegativeAmount impTot = new mysuite.TNonNegativeAmount();
                            impTot.setValue(big_total_neto.add(monto.setScale(2, BigDecimal.ROUND_HALF_UP)).setScale(2, BigDecimal.ROUND_HALF_UP));
                        concepto_ext.setImporteTotal(impTot);
                            TMTEConcepto map=new TMTEConcepto();
                            map.setIndiceListaDePrecios("1");
                            map.setTipoListaDePrecios("0");
                        concepto_ext.setMapfre(map);
                    renglon.setConceptoEx(concepto_ext);
                    renglon.setOpciones(null);
                conceptos.getConcepto().add(renglon);//ciclo por el total de conceptos
                }
           documento.setConceptos(conceptos);
                TFactDocMX.Totales totales = new TFactDocMX.Totales();
                    totales.setMoneda(TCurrencyCode.MXN);
                    totales.setTipoDeCambioVenta(BigDecimal.valueOf( ((Number)t_tipo_cambio.getValue()).doubleValue() ).setScale(3, BigDecimal.ROUND_HALF_UP));
                        mysuite.TNonNegativeAmount sub_total_bruto = new mysuite.TNonNegativeAmount();
                        sub_total_bruto.setValue(big_total_bruto.setScale(2, BigDecimal.ROUND_HALF_UP));
                    totales.setSubTotalBruto(sub_total_bruto);
                        mysuite.TNonNegativeAmount sub_total = new mysuite.TNonNegativeAmount();
                        sub_total.setValue(big_sub_total.setScale(2, BigDecimal.ROUND_HALF_UP));
                    totales.setSubTotal(sub_total);

                    totales.setDescuentosYRecargos(descuentosyRecargos); 
                    TResumenDeDescuentosYRecargos resumenDescuentos= new TResumenDeDescuentosYRecargos();
                        mysuite.TNonNegativeAmount totalDescuentos = new mysuite.TNonNegativeAmount();
                        BigDecimal sumaDescuentos=new BigDecimal(0.0).setScale(2, BigDecimal.ROUND_HALF_UP);
                        for(int r=0; r<descuentosyRecargos.getDescuentoORecargo().size(); r++)
                        {
                            if(descuentosyRecargos.getDescuentoORecargo().get(r).getOperacion()==TAllowanceChargeType.DESCUENTO)
                                sumaDescuentos = sumaDescuentos.add(descuentosyRecargos.getDescuentoORecargo().get(r).getMonto().getValue()).setScale(2, BigDecimal.ROUND_HALF_UP);
                        }
                        totalDescuentos.setValue(sumaDescuentos.setScale(2, BigDecimal.ROUND_HALF_UP));
                    resumenDescuentos.setTotalDescuentos(totalDescuentos);
                        mysuite.TNonNegativeAmount totalRecargos = new mysuite.TNonNegativeAmount();
                        totalRecargos.setValue(BigDecimal.valueOf(0));
                    resumenDescuentos.setTotalRecargos(totalRecargos);
                    totales.setResumenDeDescuentosYRecargos(resumenDescuentos);
                        TImpuestos impuestos = new TImpuestos();
                            TTax imp= new TTax();
                            imp.setContexto(TTaxContext.FEDERAL);
                            imp.setOperacion(TTaxOperation.TRASLADO);
                            imp.setCodigo("IVA");
                                mysuite.TNonNegativeAmount baseIMP = new mysuite.TNonNegativeAmount();
                                baseIMP.setValue(big_sub_total.setScale(2, BigDecimal.ROUND_HALF_UP));
                            imp.setBase(baseIMP);
                            imp.setTasa(valorIva);
                                mysuite.TNonNegativeAmount montoIMP = new mysuite.TNonNegativeAmount();
                                BigDecimal big_total_iva=big_sub_total.multiply(valorIva.divide(new BigDecimal("100.0"))).setScale(2, BigDecimal.ROUND_HALF_UP);
                                montoIMP.setValue(big_total_iva.setScale(2, BigDecimal.ROUND_HALF_UP));
                            imp.setMonto(montoIMP);
                        impuestos.getImpuesto().add(imp);
                    totales.setImpuestos(impuestos); 
                        TResumenDeImpuestos resumenImpuestos =new TResumenDeImpuestos();
                            mysuite.TNonNegativeAmount total_traslados_federales = new mysuite.TNonNegativeAmount();
                            total_traslados_federales.setValue(big_total_iva.setScale(2, BigDecimal.ROUND_HALF_UP));
                        resumenImpuestos.setTotalTrasladosFederales(total_traslados_federales);
                            mysuite.TNonNegativeAmount total_iva_traslado = new mysuite.TNonNegativeAmount();
                            total_iva_traslado.setValue(big_total_iva.setScale(2, BigDecimal.ROUND_HALF_UP));
                        resumenImpuestos.setTotalIVATrasladado(total_iva_traslado);
                            mysuite.TNonNegativeAmount total_ieps_traslado = new mysuite.TNonNegativeAmount();
                            total_ieps_traslado.setValue(BigDecimal.valueOf(0));
                        resumenImpuestos.setTotalIEPSTrasladado(total_ieps_traslado);
                            mysuite.TNonNegativeAmount total_retenciones_federales = new mysuite.TNonNegativeAmount();
                            total_retenciones_federales.setValue(BigDecimal.valueOf(0));
                        resumenImpuestos.setTotalRetencionesFederales(total_retenciones_federales);
                            mysuite.TNonNegativeAmount total_isr_retenido = new mysuite.TNonNegativeAmount();
                            total_isr_retenido.setValue(BigDecimal.valueOf(0));
                        resumenImpuestos.setTotalISRRetenido(total_isr_retenido);
                            mysuite.TNonNegativeAmount total_iva_retenido = new mysuite.TNonNegativeAmount();
                            total_iva_retenido.setValue(BigDecimal.valueOf(0));
                        resumenImpuestos.setTotalIVARetenido(total_iva_retenido);
                            mysuite.TNonNegativeAmount total_traslados_locales = new mysuite.TNonNegativeAmount();
                            total_traslados_locales.setValue(BigDecimal.valueOf(0));
                        resumenImpuestos.setTotalTrasladosLocales(total_traslados_locales);
                            mysuite.TNonNegativeAmount total_retenciones_locales = new mysuite.TNonNegativeAmount();
                            total_retenciones_locales.setValue(BigDecimal.valueOf(0));
                        resumenImpuestos.setTotalRetencionesLocales(total_retenciones_locales);
                    totales.setResumenDeImpuestos(resumenImpuestos);
                        mysuite.TNonNegativeAmount total = new mysuite.TNonNegativeAmount();
                        BigDecimal big_total_final=big_sub_total.add(big_total_iva).setScale(2, BigDecimal.ROUND_HALF_UP);
                        total.setValue(big_total_final.setScale(2, BigDecimal.ROUND_HALF_UP));
                    totales.setTotal(total);
                    //totales.setTotalEnLetra("OCHOCIENTOS TREINTA Y CINCO PESOS 20/100 M.N.");
                    totales.setTotalEnLetra(numeroLetra.convertNumberToLetter(big_total_final.setScale(2, BigDecimal.ROUND_HALF_UP).toString())+" M.N.");
                    totales.setFormaDePago("PAGO EN UNA SOLA EXHIBICION");
           documento.setTotales(totales);
           TComprobanteEx comprobante_ex=new TComprobanteEx();
                    TComprobanteEx.DatosDeNegocio datos_del_negocio = new TComprobanteEx.DatosDeNegocio();
                        datos_del_negocio.setRegion("CONTABILIDAD");
                        datos_del_negocio.setSucursal(config.getSucursal());
                comprobante_ex.setDatosDeNegocio(datos_del_negocio);
                    TComprobanteEx.DatosComerciales datos_comerciales = new TComprobanteEx.DatosComerciales();
                        datos_comerciales.setRelacionComercial(TRelacionComercial.PROVEEDOR);
                        if(t_proveedor.getText().trim().compareTo("")!=0)
                            datos_comerciales.setNumeroDeProveedor(t_proveedor.getText().trim());
                        datos_comerciales.setSubAddenda1("001");
                comprobante_ex.setDatosComerciales(datos_comerciales);
                TComprobanteEx.TerminosDePago terminos_de_pago= new TComprobanteEx.TerminosDePago();
                    terminos_de_pago.setMetodoDePago(t_metodo_pago.getText().trim());
                    terminos_de_pago.setMedioDePago(t_medio_pago.getText().trim());
                    terminos_de_pago.setCondicionesDePago(t_condiciones_pago.getText().trim());
                comprobante_ex.setTerminosDePago(terminos_de_pago);
                    TComprobanteEx.DatosDeEmbarque datosEmbarque= new TComprobanteEx.DatosDeEmbarque();
                        TDomicilioComercial lugarEmbarque = new TDomicilioComercial();
                            lugarEmbarque.setCodigo("NA");
                        datosEmbarque.setLugarDeEntrega(lugarEmbarque);
                comprobante_ex.setDatosDeEmbarque(datosEmbarque);
                /*TComprobanteEx.ReferenciasBancarias refBanco=new TComprobanteEx.ReferenciasBancarias();
                    TReferenciaBancaria rBancaria=new TReferenciaBancaria();
                        rBancaria.setMoneda(TCurrencyCode.MXN);
                        rBancaria.setBanco("BANCO TEST");
                        rBancaria.setTitular("TITULAR TEST");
                        rBancaria.setCuenta("2222222222");
                        rBancaria.setCLABE("22222222222222222");
                        rBancaria.setRefCliente("TITULAR TEST");
                    refBanco.getReferenciaBancaria().add(rBancaria);
                comprobante_ex.setReferenciasBancarias(refBanco);*/
                TTextoLibre pie= new TTextoLibre();
                    pie.getTexto().add("NA");
                comprobante_ex.setTextosDePie(pie);
                //MAPFRE
                TMTECabecera mapfre = new TMTECabecera();
                mapfre.setIdArea("001");
                mapfre.setIdRevision("003");
                    TMTEContactoEmisor contactoEmisor = new TMTEContactoEmisor();
                        contactoEmisor.setTipoDeContacto(c_tipo_emisor.getSelectedItem().toString());
                        contactoEmisor.setNombreDePersona(t_nombre_emisor.getText().trim());
                        contactoEmisor.setEMail(t_correo_emisor.getText().trim());
                        contactoEmisor.setTelefono(t_tel_emisor.getText().trim());
                    mapfre.setContactoEmisor(contactoEmisor);
                    TMTEContactoReceptor contactoReceptor= new TMTEContactoReceptor();
                        contactoReceptor.setTipoDeContacto(c_tipo_receptor.getSelectedItem().toString());
                        contactoReceptor.setNombreDePersona(t_nombre_emisor.getText().trim());
                        contactoReceptor.setEMail(t_correo_emisor.getText().trim());
                        contactoReceptor.setTelefono(t_tel_receptor.getText().trim());
                    mapfre.setContactoReceptor(contactoReceptor);
                    TMTEPoliza poliza = new TMTEPoliza();
                        poliza.setTipo(c_tipo.getSelectedItem().toString());
                        poliza.setNumero(t_numero.getText().trim());
                        poliza.setInciso("0");
                        poliza.setNroReporte(t_no_rep.getText().trim());
                        poliza.setSiniestro(t_siniestro.getText().trim());
                        poliza.setTramitador(t_contratante.getText().trim());
                        poliza.setAsegurado(t_codigo_asegurado.getText().trim());
                        TFromTo vigencia= new TFromTo();
                            SimpleDateFormat formaFecha = new SimpleDateFormat("yyyy-MM-dd");
                            Date desde = formaFecha.parse(t_fecha_1.getText());
                            GregorianCalendar c = new GregorianCalendar();
                            c.setTime(desde);
                            vigencia.setDesde(DatatypeFactory.newInstance().newXMLGregorianCalendar(c));
                            Date hasta = formaFecha.parse(t_fecha_2.getText());
                            GregorianCalendar cc = new GregorianCalendar();
                            cc.setTime(hasta);
                            vigencia.setHasta(DatatypeFactory.newInstance().newXMLGregorianCalendar(cc));
                        poliza.setVigencia(vigencia);
                    mapfre.setPoliza(poliza);
                    TMTEVehiculo vehiculo = new TMTEVehiculo();
                        vehiculo.setUso(c_tipo_vehiculo.getSelectedItem().toString());
                        vehiculo.setMarca(t_marca_vehiculo.getText().trim());
                        vehiculo.setSubMarca(t_anio_vehiculo.getText().trim());
                        vehiculo.setAnoDeProduccion(new BigInteger(t_anio_vehiculo.getText().trim()));
                        vehiculo.setColor(t_color_vehiculo.getText().trim());
                        vehiculo.setSerie(t_serie_vehiculo.getText().trim());
                        if(t_motor_vehiculo.getText().compareTo("")!=0)
                            vehiculo.setMotor(t_motor_vehiculo.getText().toString());
                        vehiculo.setPlacas(t_placas_vehiculo.getText().trim());
                    mapfre.setVehiculo(vehiculo);
                    if(t_deducible.getText().trim().compareTo("")!=0)
                    {
                    TMTEImportesAdicionales importesAdicionales =new TMTEImportesAdicionales();
                        mysuite.TNonNegativeAmount tot_deducible = new mysuite.TNonNegativeAmount();
                        BigDecimal ded=new BigDecimal(((Number)t_deducible.getValue()).doubleValue());
                            tot_deducible.setValue(ded.setScale(2, BigDecimal.ROUND_HALF_UP));
                        importesAdicionales.setDeducible(tot_deducible);
                        mapfre.setImportesAdicionales(importesAdicionales);
                    }
                comprobante_ex.setMapfre(mapfre);
           documento.setComprobanteEx(comprobante_ex);

            if(miXML.creaAndValidaXML(documento, nombre)==true)
                edo = true;
            else
            {
                error=miXML.error;
                edo= false;
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
            edo = false;
        }
        if(session!=null)
            if(session.isConnected())
                session.close();
        return edo;
    }
    
    public void llamarSoapCancela(RequestTransaction rq)
    {
        System.setProperty("javax.net.ssl.keyStore", "cacerts");
        System.setProperty("javax.net.ssl.keyStorePassword", "changeit");
        System.setProperty("javax.net.ssl.trustStore", "cacerts");
        try 
        { // Call Web Service Operation(async. callback)
            if(rq!=null)
            {
                mx.com.fact.schema.ws.FactWSFront service = new mx.com.fact.schema.ws.FactWSFront();
                mx.com.fact.schema.ws.FactWSFrontSoap port = service.getFactWSFrontSoap();
                
                // TODO initialize WS operation arguments here
                java.lang.String requestor = rq.getRequestor();
                java.lang.String transaction = rq.getTransaction();
                java.lang.String country = rq.getCountry();
                java.lang.String entity = rq.getEntity();
                java.lang.String userSAP = rq.getUser();
                java.lang.String userName = rq.getUserName();
                java.lang.String data1 = rq.getData1();
                java.lang.String data2 = rq.getData2();
                java.lang.String data3 = rq.getData3();
                javax.xml.ws.AsyncHandler<mx.com.fact.schema.ws.RequestTransactionResponse> asyncHandler = new javax.xml.ws.AsyncHandler<mx.com.fact.schema.ws.RequestTransactionResponse>() {
                    public void handleResponse(final javax.xml.ws.Response<mx.com.fact.schema.ws.RequestTransactionResponse> response) 
                    {
                        try 
                        {
                            // TODO process asynchronous response here
                            RequestTransactionResponse rtr=response.get();
                            if(rtr.getRequestTransactionResult().getResponse().isResult()==true)//la transaccion se genero
                            {
                                XMLGregorianCalendar fecha_ingreso=rtr.getRequestTransactionResult().getResponse().getTimeStamp();
                                Session session = HibernateUtil.getSessionFactory().openSession();
                                try 
                                {
                                    session.beginTransaction().begin();
                                    Factura resp=(Factura) session.createCriteria(Factura.class).add(Restrictions.eq("FFiscal", idBuscar)).uniqueResult();
                                    if(resp!=null)
                                    {
                                        resp.setOrden(null);
                                        resp.setEstadoFactura("Cancelado");
                                        session.update(resp);
                                        session.beginTransaction().commit();
                                        DefaultTableModel modelElimina=(DefaultTableModel)t_facturas.getModel();
                                        modelElimina.removeRow(t_facturas.getSelectedRow());
                                        t_facturas.setModel(modelElimina);
                                        formatoTablafactura();
                                    }
                                    habilita2(true);
                                    progreso.setString("Listo");
                                    progreso.setIndeterminate(false);
                                    JOptionPane.showMessageDialog(null, "El UUID ya esta cancelado");
                                }catch(Exception e)
                                {
                                    session.beginTransaction().rollback();
                                    e.printStackTrace();
                                    habilita2(true);
                                    progreso.setString("Listo");
                                    progreso.setIndeterminate(false);
                                    JOptionPane.showMessageDialog(null, "El UUID ya esta cancelado, pero no se pudo almacenar en la base de datos");
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
                                String error=rtr.getRequestTransactionResult().getResponseData().getResponseData2();
                                String aux="";
                                String numeros="0123456789";
                                for(int pal=0; pal<error.length(); pal++)
                                {
                                    if(numeros.contains(""+error.charAt(pal)))
                                        aux+=""+error.charAt(pal);
                                }
                                if(aux.length()>0)
                                {
                                    switch(aux)
                                    {
                                        case "201"://UUID Cancelado 
                                            Session session = HibernateUtil.getSessionFactory().openSession();
                                            try 
                                            {
                                                session.beginTransaction().begin();
                                                Factura resp=(Factura) session.createCriteria(Factura.class).add(Restrictions.eq("FFiscal", idBuscar)).uniqueResult();
                                                if(resp!=null)
                                                {
                                                    resp.setOrden(null);
                                                    resp.setEstadoFactura("Cancelado");
                                                    session.update(resp);
                                                    session.beginTransaction().commit();
                                                }
                                                habilita2(true);
                                                progreso.setString("Listo");
                                                progreso.setIndeterminate(false);
                                                JOptionPane.showMessageDialog(null, "El UUID ya esta cancelado");
                                            }catch(Exception e)
                                            {
                                                session.beginTransaction().rollback();
                                                e.printStackTrace();
                                                habilita2(true);
                                                progreso.setString("Listo");
                                                progreso.setIndeterminate(false);
                                                JOptionPane.showMessageDialog(null, "El UUID ya esta cancelado, pero no se pudo almacenar en la base de datos");
                                            }
                                            finally
                                            {
                                                if(session!=null)
                                                    if(session.isOpen())
                                                        session.close();
                                            }
                                            break;
                                        case "202"://UUID Previamente cancelado
                                            session = HibernateUtil.getSessionFactory().openSession();
                                            try 
                                            {
                                                session.beginTransaction().begin();
                                                Factura resp=(Factura) session.createCriteria(Factura.class).add(Restrictions.eq("FFiscal", idBuscar)).uniqueResult();
                                                if(resp!=null)
                                                {
                                                    resp.setOrden(null);
                                                    resp.setEstadoFactura("Cancelado");
                                                    session.update(resp);
                                                    session.beginTransaction().commit();
                                                }
                                                habilita2(true);
                                                progreso.setString("Listo");
                                                progreso.setIndeterminate(false);
                                                JOptionPane.showMessageDialog(null, "El UUID ya esta cancelado");
                                            }catch(Exception e)
                                            {
                                                session.beginTransaction().rollback();
                                                e.printStackTrace();
                                                habilita2(true);
                                                progreso.setString("Listo");
                                                progreso.setIndeterminate(false);
                                                JOptionPane.showMessageDialog(null, "El UUID ya esta cancelado, pero no se pudo almacenar en la base de datos");
                                            }
                                            finally
                                            {
                                                if(session!=null)
                                                    if(session.isOpen())
                                                        session.close();
                                            }
                                            break;
                                        case "203"://UUID No corresponde al emisor
                                            habilita2(true);
                                            progreso.setString("Listo");
                                            progreso.setIndeterminate(false);
                                            JOptionPane.showMessageDialog(null, "no corresponde al emisor");
                                            break;
                                        case "204"://UUID No aplicable para cancelación
                                            habilita2(true);
                                            progreso.setString("Listo");
                                            progreso.setIndeterminate(false);
                                            JOptionPane.showMessageDialog(null, "La factura no corresponde al emisor");
                                            break;
                                        case "205"://UUID No existe
                                            habilita2(true);
                                            progreso.setString("Listo");
                                            progreso.setIndeterminate(false);
                                            JOptionPane.showMessageDialog(null, "La factura no existe en el SAT");
                                            break;
                                        default:
                                            habilita2(true);
                                            progreso.setString("Listo");
                                            progreso.setIndeterminate(false);
                                            JOptionPane.showMessageDialog(null, "error al cancelar en SAT");
                                            break;
                                    }
                                    try
                                    {
                                        String fecha=rtr.getRequestTransactionResult().getResponse().getTimeStamp().toXMLFormat();
                                        Calendar calendario = Calendar.getInstance();
                                        File f = new File(ruta+"errores/"+sdf.format(calendario.getTime())+".txt");
                                        FileWriter w = new FileWriter(f);
                                        BufferedWriter bw = new BufferedWriter(w);
                                        PrintWriter wr = new PrintWriter(bw);  
                                        wr.write(rtr.getRequestTransactionResult().getResponse().getDescription());
                                        wr.write(rtr.getRequestTransactionResult().getResponse().getHint());
                                        wr.write(rtr.getRequestTransactionResult().getResponse().getData());
                                        wr.write(rtr.getRequestTransactionResult().getResponseData().getResponseData1());
                                        wr.write(rtr.getRequestTransactionResult().getResponseData().getResponseData2());
                                        wr.write(rtr.getRequestTransactionResult().getResponseData().getResponseData3());
                                        wr.close();
                                        bw.close();
                                    } catch (Exception e)
                                    {
                                        e.printStackTrace();
                                    }
                                }
                                else
                                {
                                    habilita2(true);
                                    progreso.setString("Listo");
                                    progreso.setIndeterminate(false);
                                    JOptionPane.showMessageDialog(null, "error al cancelar en sat");
                                }
                            }
                        } catch(Exception ex) 
                        {
                            System.out.println("Error en la conexión con el SAP:"+ex);
                            habilita2(true);
                            progreso.setString("Listo");
                            progreso.setIndeterminate(false);
                            JOptionPane.showMessageDialog(null, "Error en la conexión con el SAP:"+ex);
                        }
                    }
                };
                java.util.concurrent.Future<? extends java.lang.Object> result = port.requestTransactionAsync(requestor, transaction, country, entity, userSAP, userName, data1, data2, data3, asyncHandler);
            }
            else
            {
                habilita2(true);
                progreso.setString("Listo");
                progreso.setIndeterminate(false);
            }
        } catch (Exception ex) 
        {
            System.out.println("Error en la conexión con el SAP:"+ex);
            habilita2(true);
            progreso.setString("Listo");
            progreso.setIndeterminate(false);
            JOptionPane.showMessageDialog(null, "Error en la conexión con el SAP:"+ex);
        }
    }
    
    public void llamarSoapDocumento(RequestTransaction rq)
    {
        System.setProperty("javax.net.ssl.keyStore", "cacerts");
        System.setProperty("javax.net.ssl.keyStorePassword", "changeit");
        System.setProperty("javax.net.ssl.trustStore", "cacerts");
        try 
        { // Call Web Service Operation(async. callback)
            if(rq!=null)
            {
                mx.com.fact.schema.ws.FactWSFront service = new mx.com.fact.schema.ws.FactWSFront();
                mx.com.fact.schema.ws.FactWSFrontSoap port = service.getFactWSFrontSoap();
                
                // TODO initialize WS operation arguments here
                java.lang.String requestor = rq.getRequestor();
                java.lang.String transaction = rq.getTransaction();
                java.lang.String country = rq.getCountry();
                java.lang.String entity = rq.getEntity();
                java.lang.String userSAP = rq.getUser();
                java.lang.String userName = rq.getUserName();
                java.lang.String data1 = rq.getData1();
                java.lang.String data2 = rq.getData2();
                java.lang.String data3 = rq.getData3();
                javax.xml.ws.AsyncHandler<mx.com.fact.schema.ws.RequestTransactionResponse> asyncHandler = new javax.xml.ws.AsyncHandler<mx.com.fact.schema.ws.RequestTransactionResponse>() {
                    public void handleResponse(final javax.xml.ws.Response<mx.com.fact.schema.ws.RequestTransactionResponse> response) 
                    {
                        try 
                        {
                            // TODO process asynchronous response here
                            RequestTransactionResponse rtr=response.get();
                            if(rtr.getRequestTransactionResult().getResponse().isResult()==true)//la transaccion se genero
                            {
                                String serie = rtr.getRequestTransactionResult().getResponse().getIdentifier().getBatch();
                                String folio = rtr.getRequestTransactionResult().getResponse().getIdentifier().getSerial();
                                String UUID = rtr.getRequestTransactionResult().getResponse().getIdentifier().getDocumentGUID();
                                XMLGregorianCalendar fecha_ingreso=rtr.getRequestTransactionResult().getResponse().getTimeStamp();
                                String nombre = rtr.getRequestTransactionResult().getResponse().getIdentifier().getSuggestedFileName();
                                h=new Herramientas(user, 0);
                                h.session(sessionPrograma);
                                if (factura!=null)
                                {
                                    Session session = HibernateUtil.getSessionFactory().openSession();
                                    try 
                                    {
                                        session.beginTransaction().begin();
                                        factura = (Factura)session.get(Factura.class, factura.getIdFactura());

                                        //Datos de timbrado
                                        factura.setEstadoFactura("Facturado");
                                        factura.setFFiscal(UUID);
                                        factura.setFechaFiscal(fecha_ingreso.toXMLFormat());
                                        factura.setSerie(serie);
                                        factura.setFolio(folio);
                                        factura.setNombreDocumento(nombre);
                                        session.update(factura);
                                        session.beginTransaction().commit();

                                        if(rtr.getRequestTransactionResult().getResponseData().getResponseData1().compareTo("")!=0)
                                        {
                                            rtr.getRequestTransactionResult().getResponse().getIdentifier();
                                            CodeBase64 codificador = new CodeBase64();
                                            codificador.DecodeBase64(rtr.getRequestTransactionResult().getResponseData().getResponseData1(), "xml-timbrados/"+nombre+".xml");
                                        }
                                        if(rtr.getRequestTransactionResult().getResponseData().getResponseData3().compareTo("")!=0)
                                        {
                                            rtr.getRequestTransactionResult().getResponse().getIdentifier();
                                            CodeBase64 codificador = new CodeBase64();
                                            codificador.DecodeBase64(rtr.getRequestTransactionResult().getResponseData().getResponseData3(), "xml-timbrados/"+nombre+".pdf");
                                        }
                                        consulta();
                                        try
                                        {
                                            File xml;
                                            if(abrir.compareTo("xml")==0)
                                                xml=new File("xml-timbrados/"+t_xml.getText());
                                            else
                                                xml=new File("xml-timbrados/"+t_pdf.getText());
                                            Desktop.getDesktop().open(xml);
                                        }catch(Exception e)
                                        {
                                            e.printStackTrace();
                                            JOptionPane.showMessageDialog(null, "Error al abrir el archivo XML");
                                        }
                                    }catch(Exception e)
                                    {
                                        session.beginTransaction().rollback();
                                        e.printStackTrace();
                                        JOptionPane.showMessageDialog(null, "Error al almacenar los datos");
                                    }
                                    finally
                                    {
                                        if(session!=null)
                                            if(session.isOpen())
                                                session.close();
                                    }
                                }
                            }
                            else
                            {
                                try
                                {
                                    String fecha=rtr.getRequestTransactionResult().getResponse().getTimeStamp().toXMLFormat();
                                        Calendar calendario = Calendar.getInstance();
                                        File f = new File(ruta+"errores/"+sdf.format(calendario.getTime())+".txt");
                                    FileWriter w = new FileWriter(f);
                                    BufferedWriter bw = new BufferedWriter(w);
                                    PrintWriter wr = new PrintWriter(bw);  
                                    wr.write(rtr.getRequestTransactionResult().getResponse().getDescription());
                                    wr.write(rtr.getRequestTransactionResult().getResponse().getHint());
                                    wr.write(rtr.getRequestTransactionResult().getResponse().getData());
                                    wr.close();
                                    bw.close();
                                } catch (Exception e)
                                {
                                    e.printStackTrace();
                                }
                                JOptionPane.showMessageDialog(null, "Error al timbrar la factura:"+rtr.getRequestTransactionResult().getResponse().getDescription());
                            }
                        } catch(Exception ex) 
                        {
                            System.out.println("Error en la conexión con el SAP:"+ex);
                        }
                    }
                };
                java.util.concurrent.Future<? extends java.lang.Object> result = port.requestTransactionAsync(requestor, transaction, country, entity, userSAP, userName, data1, data2, data3, asyncHandler);
                /*while(!result.isDone())
                {
                    Thread.sleep(100);
                }*/
            }
        } catch (Exception ex) 
        {
            System.out.println("Error en la conexión con el SAP:"+ex);
            JOptionPane.showMessageDialog(null, "Error en la conexión con el SAP:"+ex);
        }
    }
    
    public void llamarSoapDocumento1(RequestTransaction rq)
    {
        System.setProperty("javax.net.ssl.keyStore", "cacerts");
        System.setProperty("javax.net.ssl.keyStorePassword", "changeit");
        System.setProperty("javax.net.ssl.trustStore", "cacerts");
        try 
        { // Call Web Service Operation(async. callback)
            if(rq!=null)
            {
                mx.com.fact.schema.ws.FactWSFront service = new mx.com.fact.schema.ws.FactWSFront();
                mx.com.fact.schema.ws.FactWSFrontSoap port = service.getFactWSFrontSoap();
                
                // TODO initialize WS operation arguments here
                java.lang.String requestor = rq.getRequestor();
                java.lang.String transaction = rq.getTransaction();
                java.lang.String country = rq.getCountry();
                java.lang.String entity = rq.getEntity();
                java.lang.String userSAP = rq.getUser();
                java.lang.String userName = rq.getUserName();
                java.lang.String data1 = rq.getData1();
                java.lang.String data2 = rq.getData2();
                java.lang.String data3 = rq.getData3();
                javax.xml.ws.AsyncHandler<mx.com.fact.schema.ws.RequestTransactionResponse> asyncHandler = new javax.xml.ws.AsyncHandler<mx.com.fact.schema.ws.RequestTransactionResponse>() {
                    public void handleResponse(final javax.xml.ws.Response<mx.com.fact.schema.ws.RequestTransactionResponse> response) 
                    {
                        try 
                        {
                            // TODO process asynchronous response here
                            RequestTransactionResponse rtr=response.get();
                            if(rtr.getRequestTransactionResult().getResponse().isResult()==true)//la transaccion se genero
                            {
                                String serie = rtr.getRequestTransactionResult().getResponse().getIdentifier().getBatch();
                                String folio = rtr.getRequestTransactionResult().getResponse().getIdentifier().getSerial();
                                String UUID = rtr.getRequestTransactionResult().getResponse().getIdentifier().getDocumentGUID();
                                XMLGregorianCalendar fecha_ingreso=rtr.getRequestTransactionResult().getResponse().getTimeStamp();
                                String nombre = rtr.getRequestTransactionResult().getResponse().getIdentifier().getSuggestedFileName2();
                                h=new Herramientas(user, 0);
                                h.session(sessionPrograma);
                                if (factura!=null)
                                {
                                    Session session = HibernateUtil.getSessionFactory().openSession();
                                    try 
                                    {
                                        session.beginTransaction().begin();
                                        factura = (Factura)session.get(Factura.class, factura.getIdFactura());

                                        //Datos de timbrado
                                        factura.setEstadoFactura("Facturado");
                                        factura.setFFiscal(UUID);
                                        factura.setFechaFiscal(fecha_ingreso.toXMLFormat());
                                        factura.setSerie(serie);
                                        factura.setFolio(folio);
                                        factura.setNombreDocumento(nombre);
                                        session.update(factura);
                                        session.beginTransaction().commit();

                                        if(rtr.getRequestTransactionResult().getResponseData().getResponseData1().compareTo("")!=0)
                                        {
                                            rtr.getRequestTransactionResult().getResponse().getIdentifier();
                                            CodeBase64 codificador = new CodeBase64();
                                            codificador.DecodeBase64(rtr.getRequestTransactionResult().getResponseData().getResponseData1(), "xml-timbrados/"+nombre+".xml");
                                        }
                                        if(rtr.getRequestTransactionResult().getResponseData().getResponseData3().compareTo("")!=0)
                                        {
                                            rtr.getRequestTransactionResult().getResponse().getIdentifier();
                                            CodeBase64 codificador = new CodeBase64();
                                            codificador.DecodeBase64(rtr.getRequestTransactionResult().getResponseData().getResponseData3(), "xml-timbrados/"+nombre+".pdf");
                                        }
                                        try
                                        {
                                            File xml;
                                            if(abrir.compareTo("xml")==0)
                                                xml=new File("xml-timbrados/"+nombre+".xml");
                                            else
                                                xml=new File("xml-timbrados/"+nombre+".pdf");
                                            Desktop.getDesktop().open(xml);
                                            habilita2(true);
                                            progreso.setString("Listo");
                                            progreso.setIndeterminate(false);
                                        }catch(Exception e)
                                        {
                                            e.printStackTrace();
                                            habilita2(true);
                                            progreso.setString("Listo");
                                            progreso.setIndeterminate(false);
                                            JOptionPane.showMessageDialog(null, "Error al abrir el archivo xml");
                                        }
                                    }catch(Exception e)
                                    {
                                        session.beginTransaction().rollback();
                                        e.printStackTrace();
                                        habilita2(true);
                                        progreso.setString("Listo");
                                        progreso.setIndeterminate(false);
                                        JOptionPane.showMessageDialog(null, "Error al almacenar los datos");
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
                                    habilita2(true);
                                    progreso.setString("Listo");
                                    progreso.setIndeterminate(false);
                                }
                            }
                            else
                            {
                                try
                                {
                                    String fecha=rtr.getRequestTransactionResult().getResponse().getTimeStamp().toXMLFormat();
                                        Calendar calendario = Calendar.getInstance();
                                        File f = new File(ruta+"errores/"+sdf.format(calendario.getTime())+".txt");
                                    FileWriter w = new FileWriter(f);
                                    BufferedWriter bw = new BufferedWriter(w);
                                    PrintWriter wr = new PrintWriter(bw);  
                                    wr.write(rtr.getRequestTransactionResult().getResponse().getDescription());
                                    wr.write(rtr.getRequestTransactionResult().getResponse().getHint());
                                    wr.write(rtr.getRequestTransactionResult().getResponse().getData());
                                    wr.close();
                                    bw.close();
                                } catch (Exception e)
                                {
                                    e.printStackTrace();
                                }
                                habilita2(true);
                                progreso.setString("Listo");
                                progreso.setIndeterminate(false);
                                JOptionPane.showMessageDialog(null, "Error al consultar la factura:"+rtr.getRequestTransactionResult().getResponse().getDescription());
                            }
                        } catch(Exception ex) 
                        {
                            System.out.println("Error en la conexión con el SAP:"+ex);
                            habilita2(true);
                            progreso.setString("Listo");
                            progreso.setIndeterminate(false);
                            JOptionPane.showMessageDialog(null, "Error en la conexión con el SAP:"+ex);
                        }
                    }
                };
                java.util.concurrent.Future<? extends java.lang.Object> result = port.requestTransactionAsync(requestor, transaction, country, entity, userSAP, userName, data1, data2, data3, asyncHandler);
                /*while(!result.isDone())
                {
                    Thread.sleep(100);
                }*/
            }
            else
            {
                habilita2(true);
                progreso.setString("Listo");
                progreso.setIndeterminate(false);
            }
        } catch (Exception ex) 
        {
            System.out.println("Error en la conexión con el SAP:"+ex);
            habilita2(true);
            progreso.setString("Listo");
            progreso.setIndeterminate(false);
            JOptionPane.showMessageDialog(null, "Error en la conexión con el SAP:"+ex);
        }
    }
    
    
    public void llamarSoapTimbra(RequestTransaction rq)
    {
        System.setProperty("javax.net.ssl.keyStore", "cacerts");
        System.setProperty("javax.net.ssl.keyStorePassword", "changeit");
        System.setProperty("javax.net.ssl.trustStore", "cacerts");
        try 
        { // Call Web Service Operation(async. callback)
            if(rq!=null)
            {
                mx.com.fact.schema.ws.FactWSFront service = new mx.com.fact.schema.ws.FactWSFront();
                mx.com.fact.schema.ws.FactWSFrontSoap port = service.getFactWSFrontSoap();
                
                // TODO initialize WS operation arguments here
                java.lang.String requestor = rq.getRequestor();
                java.lang.String transaction = rq.getTransaction();
                java.lang.String country = rq.getCountry();
                java.lang.String entity = rq.getEntity();
                java.lang.String userSAP = rq.getUser();
                java.lang.String userName = rq.getUserName();
                java.lang.String data1 = rq.getData1();
                java.lang.String data2 = rq.getData2();
                java.lang.String data3 = rq.getData3();
                javax.xml.ws.AsyncHandler<mx.com.fact.schema.ws.RequestTransactionResponse> asyncHandler = new javax.xml.ws.AsyncHandler<mx.com.fact.schema.ws.RequestTransactionResponse>() {
                    public void handleResponse(final javax.xml.ws.Response<mx.com.fact.schema.ws.RequestTransactionResponse> response) 
                    {
                        try 
                        {
                            // TODO process asynchronous response here
                            RequestTransactionResponse rtr=response.get();
                            if(rtr.getRequestTransactionResult().getResponse().isResult()==true)//la transaccion se genero
                            {
                                String serie = rtr.getRequestTransactionResult().getResponse().getIdentifier().getBatch();
                                String folio = rtr.getRequestTransactionResult().getResponse().getIdentifier().getSerial();
                                String UUID = rtr.getRequestTransactionResult().getResponse().getIdentifier().getDocumentGUID();
                                XMLGregorianCalendar fecha_ingreso=rtr.getRequestTransactionResult().getResponse().getTimeStamp();
                                String nombre = rtr.getRequestTransactionResult().getResponse().getIdentifier().getSuggestedFileName();
                                if (factura!=null)
                                {
                                    
                                    Session session = HibernateUtil.getSessionFactory().openSession();
                                    try 
                                    {
                                        session.beginTransaction().begin();
                                        factura = (Factura)session.get(Factura.class, factura.getIdFactura());

                                        //Datos de timbrado
                                        factura.setEstadoFactura("Facturado");
                                        factura.setEstatus("POR COBRAR");
                                        factura.setFFiscal(UUID);
                                        factura.setFechaFiscal(fecha_ingreso.toXMLFormat());
                                        factura.setSerie(serie);
                                        factura.setFolio(folio);
                                        factura.setNombreDocumento(nombre);
                                        session.update(factura);
                                        session.beginTransaction().commit();
                                        
                                        if(rtr.getRequestTransactionResult().getResponseData().getResponseData1().compareTo("")!=0)
                                        {
                                            rtr.getRequestTransactionResult().getResponse().getIdentifier();
                                            CodeBase64 codificador = new CodeBase64();
                                            codificador.DecodeBase64(rtr.getRequestTransactionResult().getResponseData().getResponseData1(), ruta+"xml-timbrados/"+nombre+".xml");
                                        }
                                        if(rtr.getRequestTransactionResult().getResponseData().getResponseData3().compareTo("")!=0)
                                        {
                                            rtr.getRequestTransactionResult().getResponse().getIdentifier();
                                            CodeBase64 codificador = new CodeBase64();
                                            codificador.DecodeBase64(rtr.getRequestTransactionResult().getResponseData().getResponseData3(), ruta+"xml-timbrados/"+nombre+".pdf");
                                        }
                                        habilita(true, false);
                                        progreso.setString("Listo");
                                        progreso.setIndeterminate(false);
                                        if(session.isOpen())
                                            session.close();
                                        guarda();
                                        consulta();
                                    }catch(Exception e)
                                    {
                                        session.beginTransaction().rollback();
                                        e.printStackTrace();
                                        habilita(true, false);
                                        progreso.setString("Listo");
                                        progreso.setIndeterminate(false);
                                        JOptionPane.showMessageDialog(null, "Error al almacenar los datos");
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
                                    habilita(true, false);
                                    progreso.setString("Listo");
                                    progreso.setIndeterminate(false);
                                }
                            }
                            else
                            {
                                try
                                {
                                    String fecha=rtr.getRequestTransactionResult().getResponse().getTimeStamp().toXMLFormat();
                                        Calendar calendario = Calendar.getInstance();
                                        File f = new File(ruta+"errores/"+sdf.format(calendario.getTime())+".txt");
                                    FileWriter w = new FileWriter(f);
                                    BufferedWriter bw = new BufferedWriter(w);
                                    PrintWriter wr = new PrintWriter(bw);  
                                    wr.write(rtr.getRequestTransactionResult().getResponse().getDescription());
                                    wr.write(rtr.getRequestTransactionResult().getResponse().getHint());
                                    wr.write(rtr.getRequestTransactionResult().getResponse().getData());
                                    wr.close();
                                    bw.close();
                                } catch (Exception e)
                                {
                                    e.printStackTrace();
                                }
                                habilita(true, false);
                                progreso.setString("Listo");
                                progreso.setIndeterminate(false);
                                JOptionPane.showMessageDialog(null, "Error al timbrar la factura:"+rtr.getRequestTransactionResult().getResponse().getDescription());
                            }
                        } catch(Exception ex) 
                        {
                            habilita(true, false);
                            progreso.setString("Listo");
                            progreso.setIndeterminate(false);
                            JOptionPane.showMessageDialog(null, "Error en la conexión con el SAP:"+ex);
                        }
                    }
                };
                java.util.concurrent.Future<? extends java.lang.Object> result = port.requestTransactionAsync(requestor, transaction, country, entity, userSAP, userName, data1, data2, data3, asyncHandler);
                /*while(!result.isDone())
                {
                    Thread.sleep(100);
                }*/
            }
            else
            {
                habilita(true, false);
                progreso.setString("Listo");
                progreso.setIndeterminate(false);
            }
        } catch (Exception ex) 
        {
            System.out.println("Error en la conexión con el SAP:"+ex);
            habilita(true, false);
            progreso.setString("Listo");
            progreso.setIndeterminate(false);
            JOptionPane.showMessageDialog(null, "Error en la conexión con el SAP:"+ex);
        }
    }
    
    public boolean llamarSoapConsulta(final RequestTransaction rq)
    {
        System.setProperty("javax.net.ssl.keyStore", "cacerts");
        System.setProperty("javax.net.ssl.keyStorePassword", "changeit");
        System.setProperty("javax.net.ssl.trustStore", "cacerts");
        if(rq!=null)
        {
            mx.com.fact.schema.ws.FactWSFront service = new mx.com.fact.schema.ws.FactWSFront();
            mx.com.fact.schema.ws.FactWSFrontSoap port = service.getFactWSFrontSoap();
            
            // TODO initialize WS operation arguments here
            java.lang.String requestor = rq.getRequestor();
            java.lang.String transaction = rq.getTransaction();
            java.lang.String country = rq.getCountry();
            java.lang.String entity = rq.getEntity();
            java.lang.String userSAP = rq.getUser();
            java.lang.String userName = rq.getUserName();
            java.lang.String data1 = rq.getData1();
            java.lang.String data2 = rq.getData2();
            java.lang.String data3 = rq.getData3();
            javax.xml.ws.AsyncHandler<mx.com.fact.schema.ws.RequestTransactionResponse> asyncHandler = new javax.xml.ws.AsyncHandler<mx.com.fact.schema.ws.RequestTransactionResponse>()
            {
                public final List error=new ArrayList();
                @Override
                public void handleResponse(final javax.xml.ws.Response<mx.com.fact.schema.ws.RequestTransactionResponse> response) {
                    try
                    {
                        // TODO process asynchronous response here
                        Session session = HibernateUtil.getSessionFactory().openSession();
                        session.beginTransaction().begin();
                        factura = (Factura)session.get(Factura.class, factura.getIdFactura());
                        RequestTransactionResponse rtr=response.get();
                        String numero=rtr.getRequestTransactionResult().getResponseData().getResponseData1();
                        if(numero.compareTo("")!=0) 
                        {
                            if(Integer.parseInt(numero)>0)
                            {
                                if(rtr.getRequestTransactionResult().getResponseData().getResponseData1().compareTo("")!=0)
                                {
                                    Random rng=new Random();
                                    long  dig8 = rng.nextInt(90000000)+10000000;
                                    rtr.getRequestTransactionResult().getResponse().getIdentifier();
                                    CodeBase64 codificador = new CodeBase64();
                                    boolean respuesta=codificador.DecodeBase64(rtr.getRequestTransactionResult().getResponseData().getResponseData2(), "xml-consulta/"+dig8+".xml");
                                    if(respuesta==true)
                                    {
                                        try
                                        {
                                            File fXmlFile = new File(ruta+"xml-consulta/"+dig8+".xml"); 
                                            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                                            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                                            Document doc = dBuilder.parse(fXmlFile);
                                            doc.getDocumentElement().normalize();
                                            NodeList nList = doc.getElementsByTagName("doc");
                                            DefaultTableModel modelo=ModeloTablaFactura(0, columnas1);
                                            for (int temp = 0; temp < nList.getLength(); temp++)
                                            {
                                                Node nNode = nList.item(temp);
                                                if (nNode.getNodeType() == Node.ELEMENT_NODE) 
                                                {
                                                    Element eElement = (Element) nNode;
                                                    if(eElement.getElementsByTagName("cancelled").item(0).getTextContent().compareTo("")==0) 
                                                    {
                                                        modelo.addRow(
                                                                new Object[]
                                                                {
                                                                    eElement.getElementsByTagName("taxId").item(0).getTextContent(),
                                                                    eElement.getElementsByTagName("uuid").item(0).getTextContent(),
                                                                    eElement.getElementsByTagName("branch").item(0).getTextContent(),
                                                                    eElement.getElementsByTagName("batch").item(0).getTextContent(),
                                                                    eElement.getElementsByTagName("serial").item(0).getTextContent(),
                                                                    eElement.getElementsByTagName("issued").item(0).getTextContent(),
                                                                    Double.parseDouble(eElement.getElementsByTagName("total").item(0).getTextContent()),
                                                                    eElement.getElementsByTagName("currency").item(0).getTextContent()
                                                                });
                                                    }
                                                }
                                            }
                                            t_facturas.setModel(modelo);
                                            if(t_facturas.getRowCount()==1)//hay una factura ya generada guardamos los datos en la orden
                                            {
                                                factura.setSerie(t_facturas.getValueAt(0, 3).toString());
                                                factura.setFolio(t_facturas.getValueAt(0, 4).toString());
                                                factura.setFFiscal(t_facturas.getValueAt(0, 1).toString());
                                                factura.setFechaFiscal(t_facturas.getValueAt(0, 5).toString());
                                                factura.setNombreDocumento(t_facturas.getValueAt(0, 0).toString()+"_"+t_facturas.getValueAt(0, 3).toString()+"_"+t_facturas.getValueAt(0, 4).toString()+"_"+t_rfc.getText());
                                                factura.setEstadoFactura("Facturado");
                                                factura.setEstatus("POR COBRAR");
                                                session.update(factura);
                                                session.beginTransaction().commit();
                                                if(session.isOpen())
                                                    session.close();
                                                habilita(true, false);
                                                progreso.setString("Listo");
                                                progreso.setIndeterminate(false);
                                                JOptionPane.showMessageDialog(null, "se encontro un registro y se importo la información");
                                                guarda();
                                                consulta();
                                            }
                                            else//hay mas de 1 factura para el folio enterno
                                            {
                                                habilita(true, false);
                                                progreso.setString("Listo");
                                                progreso.setIndeterminate(false);
                                                formatoTablafactura();
                                                javax.swing.JDialog consulta1 = new javax.swing.JDialog(GNP.this, true);
                                                consulta1.setModalityType(java.awt.Dialog.ModalityType.DOCUMENT_MODAL);
                                                consulta1.getContentPane().add(jLabel43, java.awt.BorderLayout.PAGE_START);
                                                consulta1.getContentPane().add(jScrollPane2, java.awt.BorderLayout.CENTER);
                                                consulta1.getContentPane().add(jPanel15, java.awt.BorderLayout.PAGE_END);
                                                consulta1.setSize(861, 326);
                                                Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
                                                consulta1.setLocation((d.width/2)-(consulta1.getWidth()/2), (d.height/2)-(consulta1.getHeight()/2));
                                                consulta1.setVisible(true);
                                            }
                                        }
                                        catch (Exception e)
                                        {
                                            habilita(true, false);
                                            progreso.setString("Listo");
                                            progreso.setIndeterminate(false);
                                            JOptionPane.showMessageDialog(null, "Se encontraron "+numero+" facturas pero no fue posible leer el xml "+e);
                                        }
                                    }
                                    else
                                    {
                                        habilita(true, false);
                                        progreso.setString("Listo");
                                        progreso.setIndeterminate(false);
                                        JOptionPane.showMessageDialog(null, "Se encontraron "+numero+" facturas pero no fue posible decodificar el xml");
                                    }
                                }
                                else
                                {
                                    habilita(true, false);
                                    progreso.setString("Listo");
                                    progreso.setIndeterminate(false);
                                    JOptionPane.showMessageDialog(null, "Se encontraron "+numero+" facturas pero no fue posible descargar el xml");
                                }
                            }
                            else
                            {
                                CodeBase64 codificador=new CodeBase64();
                                int numeroID=0;
                                if(factura.getOrden()!=null)
                                    numeroID=factura.getOrden().getIdOrden();
                                if(factura.getOrdenExterna()!=null)
                                    numeroID=factura.getOrdenExterna().getIdOrden();
                                String cadenaCodificada=codificador.EncodeArchivo(new File(ruta+"nativos/"+numeroID+"nativo.xml"));
                                if(cadenaCodificada.compareTo("")!=0)
                                {
                                    //Configuracion config=(Configuracion)session.get(Configuracion.class, 1);
                                    RequestTransaction rq1=new RequestTransaction();
                                    rq1.setRequestor(rq.getRequestor());//Lo proporcionará MySuite
                                    rq1.setTransaction("CONVERT_NATIVE_XML");//Tipo de Transaccion
                                    rq1.setCountry("MX");//Codigo de pais
                                    rq1.setUser(rq.getUser());//igual que Requestor
                                    rq1.setUserName(rq.getUserName());//Country.Entity.nombre_usuario
                                    rq1.setEntity(rq.getEntity());
                                    rq1.setData1(cadenaCodificada);
                                    rq1.setData2("PDF XML");//Vacío
                                    rq1.setData3("");//Vacío
                                    if(session.isOpen())
                                        session.close();
                                    llamarSoapTimbra(rq1);
                                }
                                else
                                {
                                    habilita(true, false);
                                    progreso.setString("Listo");
                                    progreso.setIndeterminate(false);
                                    JOptionPane.showMessageDialog(null, "Error al codificar la cadena a Base 64");
                                }
                            }
                        } 
                        else
                        {
                            habilita(true, false);
                            progreso.setString("Listo");
                            progreso.setIndeterminate(false);
                            JOptionPane.showMessageDialog(null, "¡Error en el SAP:"+rtr.getRequestTransactionResult().getResponse().getData());
                        }
                    } 
                    catch(InterruptedException ex)
                    {
                        habilita(true, false);
                        progreso.setString("Listo");
                        progreso.setIndeterminate(false);
                        JOptionPane.showMessageDialog(null, "Error en la conexion con el SAP:"+ex);
                    }
                    catch(NumberFormatException ex)
                    {
                        habilita(true, false);
                        progreso.setString("Listo");
                        progreso.setIndeterminate(false);
                        JOptionPane.showMessageDialog(null, "Error al generar el archivo de error");
                    }
                    catch(ExecutionException ex)
                    {
                        habilita(true, false);
                        progreso.setString("Listo");
                        progreso.setIndeterminate(false);
                        JOptionPane.showMessageDialog(null, "Error al obtener la respuesta del SAP:"+ex);
                    }
                    catch(HibernateException ex)
                    {
                        habilita(true, false);
                        progreso.setString("Listo");
                        progreso.setIndeterminate(false);
                        JOptionPane.showMessageDialog(null, "Error al leer la base de datos");
                    }
                }
            };
            java.util.concurrent.Future<? extends java.lang.Object> result = port.requestTransactionAsync(requestor, transaction, country, entity, userSAP, userName, data1, data2, data3, asyncHandler);
            /*while(!result.isDone())
            {
            Thread.sleep(100);
            }*/
        }
        else
        {
            habilita(true, false);
            progreso.setString("Listo");
            progreso.setIndeterminate(false);
        }
        return true;
    }
    
    private void doClose(Factura o) {
        returnStatus = o;
        setVisible(false);
        //dispose();
    }
    
    public Factura getReturnStatus() {
        return returnStatus;
    }
    
    public void facturaElectronica()
    {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try
        {
            session.beginTransaction().begin();
            factura=(Factura)session.get(Factura.class, factura.getIdFactura());
            int numero=0;
            if(factura.getOrden()!=null)
                numero=factura.getOrden().getIdOrden();
            if(factura.getOrdenExterna()!=null)
                numero=factura.getOrdenExterna().getIdOrden();
            if(generarXML(factura.getOrden(), ruta+"nativos/"+numero+"nativo.xml")==true)
            {
                Configuracion config=(Configuracion)session.get(Configuracion.class, 1);
                RequestTransaction rq=new RequestTransaction();
                rq.setRequestor(config.getRequestor());//Lo proporcionará MySuite
                rq.setTransaction("LOOKUP_ISSUED_INTERNAL_ID");//Tipo de Transaccion
                rq.setCountry("MX");//Codigo de pais
                rq.setUser(config.getRequestor());//igual que Requestor
                rq.setUserName(config.getUsuario_1());//Country.Entity.nombre_usuario
                rq.setEntity(config.getRfc());
                rq.setData1(t_codigo.getText());//XML del CFDI a timbrar (no puede contener addendas ni timbre, y CFDI codificado en base 64).
                rq.setData2(config.getSucursal());//Vacío
                rq.setData3("");//Vacío
                if(session.isOpen())
                    session.close();
                llamarSoapConsulta(rq);
            }
            else
            {
                habilita(true, false);
                progreso.setString("Listo");
                progreso.setIndeterminate(false);
                JOptionPane.showMessageDialog(null, "Error al generar el XML");
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
            habilita(true, false);
            progreso.setString("Listo");
            progreso.setIndeterminate(false);
            JOptionPane.showMessageDialog(null, "Error al generar el XML.-"+error);
        }
        finally
        {
            if(session!=null)
                if(session.isOpen())
                    session.close();
        }
    }
    
    public int existe(TDescuentosYRecargos descuentos, TDiscountOrRecharge aux)
    {
        for(int rec=0; rec<descuentos.getDescuentoORecargo().size(); rec++)
        {
            if(descuentos.getDescuentoORecargo().get(rec).getOperacion().value().compareTo(aux.getOperacion().value())==0 && descuentos.getDescuentoORecargo().get(rec).getTasa().setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue()==aux.getTasa().setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue())
            {
                return rec;
            }
        }
        return -1;
    }
    
    public void habilita2(boolean edo)
    {
        t_facturas.setEnabled(edo);
        b_cancelar.setEnabled(edo);
        b_descargar.setEnabled(edo);
    }
    
    public String guarda()
    {
        String respuesta="";
        Session session = HibernateUtil.getSessionFactory().openSession();
        try 
        {
            session.beginTransaction().begin();
            factura = (Factura)session.get(Factura.class, factura.getIdFactura());

            //Receptor
            factura.setNombreReceptor(t_social.getText().trim());
            factura.setRfcReceptor(t_rfc.getText().trim());
            factura.setCalleReceptor(t_calle.getText().trim());
            factura.setNumeroExteriorReceptor(t_no_exterior.getText().trim());
            factura.setCpReceptor(t_cp.getText().trim());
            factura.setColoniaReceptor(t_colonia.getText().trim());
            factura.setMunicipioReceptor(t_municipio.getText().trim());
            if(c_estado.getSelectedIndex()!=-1)
                factura.setEstadoReceptor(c_estado.getSelectedItem().toString());
            if(c_pais.getSelectedIndex()!=-1)
                factura.setPaisReceptor(c_pais.getSelectedItem().toString());
            //Productos y servicios
            factura.setMetodoPago(t_metodo_pago.getText().trim());
            factura.setCuentaPago(t_cuenta_pago.getText().trim());
            factura.setFactorCambio(((Number)t_tipo_cambio.getValue()).doubleValue());
            if(c_moneda.getSelectedIndex()!=-1)
                factura.setMoneda(c_moneda.getSelectedItem().toString());

            //Addenda  
            if(c_tipo.getSelectedIndex()>-1)
                factura.setTipoPoliza(c_tipo.getSelectedItem().toString());
            if(c_tipo_emisor.getSelectedIndex()>-1)
                factura.setTipoEmisor(c_tipo_emisor.getSelectedItem().toString());
            factura.setContactoEmisor(t_nombre_emisor.getText().trim());
            factura.setCorreoEmisor(t_correo_emisor.getText().trim());
            factura.setTelefonoEmisor(t_tel_emisor.getText().trim());
            factura.setDescripcionEmisor(t_descripcion.getText().trim());
            if(c_tipo_receptor.getSelectedIndex()>-1)
                factura.setTipoReceptor(c_tipo_receptor.getSelectedItem().toString());
            factura.setContactoReceptor(t_nombre_receptor.getText().trim());
            factura.setCorreoReceptor(t_correo_receptor.getText().trim());
            factura.setTelefonoReceptor(t_tel_receptor.getText().trim());
            factura.setProveedor(t_proveedor.getText());
            factura.setMedioPago(t_medio_pago.getText().trim());
            String [] campos;
            if(t_fecha_1.getText().compareTo("AAAA-MM-DD")!=0)
            {
                campos = t_fecha_1.getText().split("-");
                Calendar calendario = Calendar.getInstance();
                calendario.set(Calendar.MONTH, Integer.parseInt(campos[1])-1);
                calendario.set(Calendar.YEAR, Integer.parseInt(campos[0]));
                calendario.set(Calendar.DAY_OF_MONTH, Integer.parseInt(campos[2]));
                factura.setFechaMedioPago(calendario.getTime());
            }
            factura.setCondicionesPago(t_condiciones_pago.getText().trim());
            if(t_fecha_2.getText().compareTo("AAAA-MM-DD")!=0)
            {
                campos = t_fecha_2.getText().split("-");
                Calendar calendario = Calendar.getInstance();
                calendario.set(Calendar.MONTH, Integer.parseInt(campos[1])-1);
                calendario.set(Calendar.YEAR, Integer.parseInt(campos[0]));
                calendario.set(Calendar.DAY_OF_MONTH, Integer.parseInt(campos[2]));
                factura.setFechaCondicionesPago(calendario.getTime());
            }
            factura.setAddenda("gnp");
            factura.setIva(Integer.parseInt(t_iva1.getValue().toString()));
            session.update(factura);
            if(factura.getOrden()!=null)
            {
                Orden ord=(Orden)session.get(Orden.class, factura.getOrden().getIdOrden());
                ord.setPoliza(t_numero.getText().trim());
                String tipo_cliente=c_tipo_cliente.getSelectedItem().toString();
                if(tipo_cliente!=null)
                {
                    switch(tipo_cliente)
                    {
                        case "ASEGURADO": 
                            ord.setTipoCliente("1");
                            break;
                        case "TERCERO": 
                            ord.setTipoCliente("2");
                            break;
                    }
                }
                ord.setNoReporte(t_no_rep.getText().trim());
                ord.setSiniestro(t_siniestro.getText().trim());
                ord.setContratante(t_contratante.getText().trim());
                ord.setCodigoAsegurado(t_codigo_asegurado.getText().trim());
                if(c_tipo_vehiculo.getSelectedIndex()>-1)
                    ord.setTipoVehiculo(c_tipo_vehiculo.getSelectedItem().toString());
                ord.setColor(t_color_vehiculo.getText().trim());
                ord.setModelo(Integer.parseInt(t_anio_vehiculo.getText()));
                ord.setNoSerie(t_serie_vehiculo.getText().trim());
                ord.setNoPlacas(t_placas_vehiculo.getText().trim());
                ord.setNoMotor(t_motor_vehiculo.getText().trim());
                ord.setDeducible(((Number)t_deducible.getValue()).doubleValue());
                session.update(ord);
            }
            if(factura.getOrdenExterna()!=null)
            {
                OrdenExterna ordE=(OrdenExterna)session.get(OrdenExterna.class, factura.getOrdenExterna().getIdOrden());
                ordE.setPoliza(t_numero.getText().trim());
                String tipo_cliente=c_tipo_cliente.getSelectedItem().toString();
                if(tipo_cliente!=null)
                {
                    switch(tipo_cliente)
                    {
                        case "ASEGURADO": 
                            ordE.setTipoCliente("1");
                            break;
                        case "TERCERO": 
                            ordE.setTipoCliente("2");
                            break;
                    }
                }
                ordE.setNoReporte(t_no_rep.getText().trim());
                ordE.setSiniestro(t_siniestro.getText().trim());
                ordE.setContratante(t_contratante.getText().trim());
                ordE.setCodigoAsegurado(t_codigo_asegurado.getText().trim());
                if(c_tipo_vehiculo.getSelectedIndex()>-1)
                    ordE.setTipoVehiculo(c_tipo_vehiculo.getSelectedItem().toString());
                ordE.setColor(t_color_vehiculo.getText().trim());
                if(t_anio_vehiculo.getText().trim().compareTo("")!=0)
                    ordE.setModelo(Integer.parseInt(t_anio_vehiculo.getText()));
                ordE.setNoSerie(t_serie_vehiculo.getText().trim());
                ordE.setNoPlacas(t_placas_vehiculo.getText().trim());
                ordE.setNoMotor(t_motor_vehiculo.getText().trim());
                ordE.setDeducible(((Number)t_deducible.getValue()).doubleValue());
                session.update(ordE);
            }
            session.beginTransaction().commit();
        }catch(Exception e)
        {
            session.beginTransaction().rollback();
            e.printStackTrace();
            respuesta="Error al almacenar los datos";
        }
        finally
        {
            if(session!=null)
                if(session.isOpen())
                    session.close();
        }
        return respuesta;
    }
}
