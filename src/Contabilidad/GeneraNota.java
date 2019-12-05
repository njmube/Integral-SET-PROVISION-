/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Contabilidad;

import Compras.Formatos;
import static Contabilidad.General.acentos;
import Hibernate.Util.HibernateUtil;
import Hibernate.entidades.Concepto;
import Hibernate.entidades.Configuracion;
import Hibernate.entidades.Factura;
import Hibernate.entidades.Nota;
import Hibernate.entidades.Orden;
import Hibernate.entidades.OrdenExterna;
import Hibernate.entidades.ProductoServicio;
import Hibernate.entidades.Relacion;
import Hibernate.entidades.UsoCfdi;
import Hibernate.entidades.ClaveUnidad;
import Hibernate.entidades.Usuario;
import Servicios.EnviarCorreo;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import mysuite.TAllowanceChargeType;
import mysuite.TComprobanteEx;
import mysuite.TConceptoEx;
import mysuite.TCountryCode;
import mysuite.TCurrencyCode;
import mysuite.TDescuentosYRecargos;
import mysuite.TDictionaries;
import mysuite.TDictionary;
import mysuite.TDiscountOrRecharge;
import mysuite.TFactDocMX;
import mysuite.TImpuestos;
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
import Integral.Comprimir;
import Integral.FormatoTabla;
import Integral.Herramientas;
import Integral.Render1;
import Integral.numeroLetra;
import api.ApiTurbo;
import finkok.Comprobante;
import finkok.Comprobante.Emisor;
import finkok.Comprobante.Emisor.RegimenFiscal;
import finkok.Comprobante.Impuestos;
import finkok.Comprobante.Impuestos.Traslados;
import finkok.Comprobante.Impuestos.Traslados.Traslado;
import finkok.Comprobante.Receptor;
import finkok.TUbicacion;
import finkok.TUbicacionFiscal;
import finkok33.CMetodoPago;
import finkok33.CMoneda;
import finkok33.CTipoDeComprobante;
import finkok33.CTipoFactor;
import finkok33.CUsoCFDI;
import finkok33.Comprobante.CfdiRelacionados;
import finkok33.Comprobante.CfdiRelacionados.CfdiRelacionado;
import java.awt.event.ItemEvent;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.regex.Pattern;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.datatype.DatatypeFactory;
import org.apache.poi.hssf.record.crypto.Biff8EncryptionKey;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.hibernate.Criteria;
import org.hibernate.Query;
import sat.buscaUsoCfdi;
import sat.buscarProServicio;
import timbrar.ApiFinkok;
import timbrar.ApiMysuite;
import timbrar.Constantes;
import timbrar.Constantes33;
import timbrar.GeneradorSelloDigital;

/**
 *
 * @author salvador
 */
public class GeneraNota extends javax.swing.JDialog {

    String error="";
    private Usuario user;
    String sessionPrograma=null;
    FormatoTabla formato;
    Herramientas h;
    String abrir="";
    Date fecha_factura=new Date();
    finkok.Comprobante comprobante;
    finkok33.Comprobante comprobante33;
    String idBuscar="";
    DefaultTableModel model;
    DefaultTableModel modeloFactura;
    int iva=0;
    String[] columnas = new String [] {
        "Id","Can","Med","Descripción","Cve-Prod","Costo c/u","Descuento","Total"
    };
    String[] columnas1 = new String [] {
        "RFC","UUID","SUCURSAL","SERIE","FOLIO","FECHA","MONTO","MONEDA"
    };
    Nota factura;
    boolean permiso=false;
    public static final Nota RET_CANCEL =null;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm-Ss");
    private Nota returnStatus = RET_CANCEL;
    String ruta="";
    boolean bandera=true;
    String PAC="";
    String anterior="";
    int configuracion=1;
    /**
     * Creates new form QUALITAS
     */
    public GeneraNota(java.awt.Frame parent, boolean modal, Usuario u, String ses, Nota fac, int configuracion) {
        super(parent, modal);
        initComponents();
        this.configuracion=configuracion;
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
        cfdi = new javax.swing.JComboBox();
        medida = new javax.swing.JComboBox();
        t_iva1 = new javax.swing.JFormattedTextField();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        l_emisor = new javax.swing.JLabel();
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
        jButton7 = new javax.swing.JButton();
        t_id_cfdi = new javax.swing.JTextField();
        t_descripcion_cfdi = new javax.swing.JTextField();
        jLabel31 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        l_subtotal = new javax.swing.JLabel();
        t_subtotal = new javax.swing.JFormattedTextField();
        t_iva = new javax.swing.JFormattedTextField();
        l_iva = new javax.swing.JLabel();
        l_total = new javax.swing.JLabel();
        t_total = new javax.swing.JFormattedTextField();
        jLabel21 = new javax.swing.JLabel();
        t_cuenta_pago = new javax.swing.JTextField();
        jLabel23 = new javax.swing.JLabel();
        c_moneda = new javax.swing.JComboBox();
        jLabel24 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        t_tipo_cambio = new javax.swing.JFormattedTextField();
        t_descuento = new javax.swing.JFormattedTextField();
        l_iva1 = new javax.swing.JLabel();
        b_menos = new javax.swing.JButton();
        b_mas = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jLabel20 = new javax.swing.JLabel();
        c_metodo_pago = new javax.swing.JComboBox();
        jLabel25 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        c_forma_pago = new javax.swing.JComboBox();
        jLabel26 = new javax.swing.JLabel();
        jButton6 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        t_datos = new javax.swing.JTable();
        jPanel16 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        t_relacionados = new javax.swing.JTable();
        jLabel30 = new javax.swing.JLabel();
        cb_tipo_relacion = new javax.swing.JComboBox();
        l_tipo_relacion = new javax.swing.JLabel();
        b_menos1 = new javax.swing.JButton();
        b_mas1 = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jPanel10 = new javax.swing.JPanel();
        t_inciso = new javax.swing.JTextField();
        jLabel70 = new javax.swing.JLabel();
        t_numero = new javax.swing.JTextField();
        jLabel72 = new javax.swing.JLabel();
        jLabel75 = new javax.swing.JLabel();
        t_siniestro = new javax.swing.JTextField();
        jLabel53 = new javax.swing.JLabel();
        t_deducible = new javax.swing.JFormattedTextField();
        t_contratante = new javax.swing.JTextField();
        jLabel89 = new javax.swing.JLabel();
        jPanel11 = new javax.swing.JPanel();
        jLabel32 = new javax.swing.JLabel();
        t_nombre_emisor = new javax.swing.JTextField();
        jLabel33 = new javax.swing.JLabel();
        t_correo_emisor = new javax.swing.JTextField();
        jLabel34 = new javax.swing.JLabel();
        t_tel_emisor = new javax.swing.JTextField();
        jLabel35 = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();
        jPanel12 = new javax.swing.JPanel();
        jLabel38 = new javax.swing.JLabel();
        t_nombre_receptor = new javax.swing.JTextField();
        jLabel39 = new javax.swing.JLabel();
        t_correo_receptor = new javax.swing.JTextField();
        jLabel40 = new javax.swing.JLabel();
        t_tel_receptor = new javax.swing.JTextField();
        jLabel41 = new javax.swing.JLabel();
        jLabel42 = new javax.swing.JLabel();
        jPanel13 = new javax.swing.JPanel();
        jLabel50 = new javax.swing.JLabel();
        t_modelo_vehiculo = new javax.swing.JTextField();
        t_marca_vehiculo = new javax.swing.JTextField();
        jLabel52 = new javax.swing.JLabel();
        t_anio_vehiculo = new javax.swing.JTextField();
        jLabel56 = new javax.swing.JLabel();
        t_serie_vehiculo = new javax.swing.JTextField();
        jLabel57 = new javax.swing.JLabel();
        jLabel58 = new javax.swing.JLabel();
        t_placas_vehiculo = new javax.swing.JTextField();
        jLabel62 = new javax.swing.JLabel();
        t_motor_vehiculo = new javax.swing.JTextField();
        jLabel87 = new javax.swing.JLabel();
        t_condiciones = new javax.swing.JTextField();
        jLabel88 = new javax.swing.JLabel();
        t_codigo = new javax.swing.JTextField();
        jLabel48 = new javax.swing.JLabel();
        jLabel37 = new javax.swing.JLabel();
        jLabel49 = new javax.swing.JLabel();
        t_orden = new javax.swing.JTextField();
        t_extra = new javax.swing.JTextField();
        jLabel51 = new javax.swing.JLabel();
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

        cfdi.setFont(new java.awt.Font("Dialog", 0, 9)); // NOI18N
        cfdi.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Buscar", "Cancelar" }));

        medida.setFont(new java.awt.Font("Dialog", 0, 9)); // NOI18N
        medida.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "PZAS", "LTS", "MTS", "CMS", "MMS", "GRS", "MLS", "KGS", "HRS", "MIN", "KIT", "FT", "LB", "JGO", "NA", "SERV" }));

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

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setModalityType(java.awt.Dialog.ModalityType.APPLICATION_MODAL);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(254, 254, 254));

        jLabel1.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel1.setText("Emisión de Notas de Crédito");

        l_emisor.setText("jLabel30");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(355, 355, 355)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 499, Short.MAX_VALUE)
                .addComponent(l_emisor)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel1)
                .addComponent(l_emisor))
        );

        getContentPane().add(jPanel1, java.awt.BorderLayout.PAGE_START);

        jPanel2.setBackground(new java.awt.Color(254, 254, 254));

        b_salir.setBackground(new java.awt.Color(2, 135, 242));
        b_salir.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        b_salir.setForeground(new java.awt.Color(254, 254, 254));
        b_salir.setIcon(new ImageIcon("imagenes/salir1.png"));
        b_salir.setText("Salir");
        b_salir.setToolTipText("Cierra la ventana");
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
        b_actualizar.setToolTipText("Actualiza el registro en la base de datos");
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 263, Short.MAX_VALUE)
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
                .addContainerGap(165, Short.MAX_VALUE))
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

        c_estado.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "AGUASCALIENTES", "BAJA CALIFORNIA", "BAJA CALIFORNIA SUR", "CAMPECHE", "CHIAPAS", "CHIHUAHUA", "COAHUILA", "COLIMA", "CIUDAD DE MÉXICO", "DURANGO", "ESTADO DE MÉXICO", "GUANAJUATO", "GUERRERO", "HIDALGO", "JALISCO", "MICHOACAN", "MORELOS", "NAYARIT", "NUEVO LEON", "OAXACA", "PUEBLA", "QUERETARO", "QUINTANA ROO", "SAN LUIS POTOSI", "SINALOA", "SONORA", "TABASCO", "TAMAULIPAS", "TLAXCALA", "VERACRUZ", "YUCATAN", "ZACATECAS" }));
        c_estado.setToolTipText("Estado del receptor");

        jLabel17.setText("Pais");

        jLabel18.setText("*");

        c_pais.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "MEXICO" }));
        c_pais.setSelectedItem("MX");
        c_pais.setToolTipText("Pais del receptor");

        jLabel19.setText("*");

        jButton7.setBackground(new java.awt.Color(2, 135, 242));
        jButton7.setForeground(new java.awt.Color(255, 255, 255));
        jButton7.setText("Uso CFDI");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        t_id_cfdi.setBackground(new java.awt.Color(204, 255, 255));
        t_id_cfdi.setEnabled(false);

        t_descripcion_cfdi.setBackground(new java.awt.Color(204, 255, 255));
        t_descripcion_cfdi.setEnabled(false);

        jLabel31.setText("*");

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
                                .addGap(287, 287, 287)
                                .addComponent(jButton7)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(t_id_cfdi, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(t_descripcion_cfdi)
                                .addGap(1, 1, 1)
                                .addComponent(jLabel31))
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addComponent(jLabel13)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(t_colonia, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(3, 3, 3)
                                .addComponent(jLabel12)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 216, Short.MAX_VALUE)
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
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel16)
                        .addComponent(c_estado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel17)
                        .addComponent(jLabel18)
                        .addComponent(c_pais, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel19))
                    .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(t_id_cfdi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(t_descripcion_cfdi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel31))
                        .addComponent(jButton7)))
                .addContainerGap(25, Short.MAX_VALUE))
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
                .addContainerGap(266, Short.MAX_VALUE))
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
        l_iva.setText("    IVA:");
        jPanel9.add(l_iva, new org.netbeans.lib.awtextra.AbsoluteConstraints(43, 23, -1, -1));

        l_total.setFont(new java.awt.Font("Arial", 0, 9)); // NOI18N
        l_total.setForeground(new java.awt.Color(255, 255, 255));
        l_total.setText("Total:");
        jPanel9.add(l_total, new org.netbeans.lib.awtextra.AbsoluteConstraints(52, 45, -1, -1));

        t_total.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        t_total.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.00"))));
        t_total.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        t_total.setText("0.00");
        t_total.setToolTipText("Total Neto");
        t_total.setEnabled(false);
        t_total.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        jPanel9.add(t_total, new org.netbeans.lib.awtextra.AbsoluteConstraints(85, 41, 88, -1));

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

        jLabel23.setForeground(new java.awt.Color(254, 254, 254));
        jLabel23.setText("Moneda:");

        c_moneda.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        c_moneda.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "MXN", "USD" }));
        c_moneda.setToolTipText("Tipo de moneda del monto");

        jLabel24.setForeground(new java.awt.Color(254, 254, 254));
        jLabel24.setText("Factor de Cambio");

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

        jLabel20.setForeground(new java.awt.Color(254, 254, 254));
        jLabel20.setText("Metodo de Pago:");

        c_metodo_pago.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "01", "02", "03", "04", "05", "06", "08", "12", "13", "14", "15", "17", "23", "24", "25", "26", "27", "28", "29", "30", "99" }));

        jLabel25.setForeground(new java.awt.Color(254, 254, 254));
        jLabel25.setText("*");

        jLabel22.setForeground(new java.awt.Color(254, 254, 254));
        jLabel22.setText("F. Pago:");

        c_forma_pago.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "PAGO EN UNA SOLA EXHIBICION", "PAGO EN PARCIALIDADES O DIFERIDO" }));

        jLabel26.setForeground(new java.awt.Color(254, 254, 254));
        jLabel26.setText("*");

        jButton6.setText("Claves");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(b_mas, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(b_menos, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel20)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(c_metodo_pago, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel25)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel22)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(c_forma_pago, javax.swing.GroupLayout.PREFERRED_SIZE, 223, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel21)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(t_cuenta_pago, javax.swing.GroupLayout.PREFERRED_SIZE, 221, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel23)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(c_moneda, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel24)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(t_tipo_cambio, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel28)))
                .addGap(42, 42, 42)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addComponent(l_iva1, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(t_descuento, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(104, 104, 104))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(jButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(b_mas, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(b_menos, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(t_descuento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(l_iva1))
                    .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(c_metodo_pago, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel20))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel25)
                            .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel26)
                                .addComponent(jLabel22)
                                .addComponent(c_forma_pago, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(t_cuenta_pago, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel21)
                    .addComponent(c_moneda, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel24)
                    .addComponent(jLabel28)
                    .addComponent(jLabel23)
                    .addComponent(t_tipo_cambio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1)
                    .addComponent(jButton6)))
        );

        jPanel4.add(jPanel8, java.awt.BorderLayout.PAGE_END);

        t_datos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "id", "Cant", "Med", "Descripción", "Cve-Prod", "Costo c/u", "Descuento", "iva", "Total"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Object.class, java.lang.String.class, java.lang.Object.class, java.lang.Object.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, true, true, true, true, true, false
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

        t_relacionados.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "UUID", "Tipo", "relacion"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
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
        jScrollPane3.setViewportView(t_relacionados);

        jLabel30.setText("Tipo de Relacion:");

        cb_tipo_relacion.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "BUSCAR", "01", "03", "04", "07" }));
        cb_tipo_relacion.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cb_tipo_relacionItemStateChanged(evt);
            }
        });
        cb_tipo_relacion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cb_tipo_relacionActionPerformed(evt);
            }
        });

        l_tipo_relacion.setText("Texto");

        b_menos1.setBackground(new java.awt.Color(2, 135, 242));
        b_menos1.setForeground(new java.awt.Color(255, 255, 255));
        b_menos1.setIcon(new ImageIcon("imagenes/boton_menos.png"));
        b_menos1.setToolTipText("Eliminar un concepto");
        b_menos1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_menos1ActionPerformed(evt);
            }
        });

        b_mas1.setBackground(new java.awt.Color(2, 135, 242));
        b_mas1.setForeground(new java.awt.Color(255, 255, 255));
        b_mas1.setIcon(new ImageIcon("imagenes/boton_mas.png"));
        b_mas1.setToolTipText("Agrega un concepto");
        b_mas1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_mas1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 1078, Short.MAX_VALUE)
                    .addGroup(jPanel16Layout.createSequentialGroup()
                        .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel16Layout.createSequentialGroup()
                                .addComponent(jLabel30)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cb_tipo_relacion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(l_tipo_relacion))
                            .addGroup(jPanel16Layout.createSequentialGroup()
                                .addComponent(b_mas1, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(2, 2, 2)
                                .addComponent(b_menos1, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addGap(11, 11, 11)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel30)
                    .addComponent(cb_tipo_relacion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(l_tipo_relacion))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 386, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(b_mas1, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(b_menos1, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(50, Short.MAX_VALUE))
        );

        contenedor.addTab("CFDI Relacionados", jPanel16);

        jPanel5.setBackground(new java.awt.Color(254, 254, 254));
        jPanel5.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        jPanel5.setForeground(new java.awt.Color(254, 254, 254));

        jPanel10.setBackground(new java.awt.Color(254, 254, 254));
        jPanel10.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 1, 0, new java.awt.Color(0, 0, 0)), "Poliza"), "Poliza", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 1, 10))); // NOI18N

        t_inciso.setToolTipText("Número de inciso");
        t_inciso.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        t_inciso.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                t_incisoKeyTyped(evt);
            }
        });

        jLabel70.setText("t_inciso");

        t_numero.setToolTipText("Número de poliza");
        t_numero.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        t_numero.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                t_numeroKeyTyped(evt);
            }
        });

        jLabel72.setText("No Poliza:");

        jLabel75.setText("Siniestro:");

        t_siniestro.setToolTipText("Número de siniestro");
        t_siniestro.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        t_siniestro.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                t_siniestroKeyTyped(evt);
            }
        });

        jLabel53.setText("Deducible:");

        t_deducible.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        t_deducible.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        t_deducible.setToolTipText("Monto del deducible");
        t_deducible.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N

        t_contratante.setToolTipText("Nombre del contratante");
        t_contratante.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        t_contratante.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                t_contratanteKeyTyped(evt);
            }
        });

        jLabel89.setText("contratante:");

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addComponent(jLabel72)
                .addGap(5, 5, 5)
                .addComponent(t_numero, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel75)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(t_siniestro, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel70)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(t_inciso, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel53)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(t_deducible, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26)
                .addComponent(jLabel89)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(t_contratante, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(156, Short.MAX_VALUE))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel89)
                        .addComponent(t_contratante, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel53)
                        .addComponent(t_deducible, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel72)
                        .addComponent(t_numero, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel70)
                        .addComponent(t_inciso, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel75)
                        .addComponent(t_siniestro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(21, Short.MAX_VALUE))
        );

        jPanel11.setBackground(new java.awt.Color(254, 254, 254));
        jPanel11.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 1, 0, new java.awt.Color(0, 0, 0)), "Contacto Emisor", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.ABOVE_TOP, new java.awt.Font("Arial", 1, 10))); // NOI18N

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

        t_tel_emisor.setToolTipText("Teléfono del contacto emisor");
        t_tel_emisor.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        t_tel_emisor.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                t_tel_emisorKeyTyped(evt);
            }
        });

        jLabel35.setText("*");

        jLabel36.setText("*");

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addComponent(jLabel33)
                        .addGap(24, 24, 24)
                        .addComponent(t_correo_emisor, javax.swing.GroupLayout.PREFERRED_SIZE, 461, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel36)
                        .addGap(88, 88, 88)
                        .addComponent(jLabel34)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(t_tel_emisor, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addComponent(jLabel32)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(t_nombre_emisor, javax.swing.GroupLayout.PREFERRED_SIZE, 498, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel35)))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel32)
                    .addComponent(t_nombre_emisor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel35))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel33)
                    .addComponent(t_correo_emisor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel34)
                    .addComponent(t_tel_emisor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel36))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel12.setBackground(new java.awt.Color(254, 254, 254));
        jPanel12.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 1, 0, new java.awt.Color(0, 0, 0)), "Contacto Receptor", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.ABOVE_TOP, new java.awt.Font("Arial", 1, 10))); // NOI18N

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

        t_tel_receptor.setToolTipText("Telefono del contacto receptor");
        t_tel_receptor.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        t_tel_receptor.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                t_tel_receptorKeyTyped(evt);
            }
        });

        jLabel41.setText("*");

        jLabel42.setText("*");

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addComponent(jLabel39)
                        .addGap(36, 36, 36)
                        .addComponent(t_correo_receptor, javax.swing.GroupLayout.PREFERRED_SIZE, 411, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel42)
                        .addGap(82, 82, 82)
                        .addComponent(jLabel40)
                        .addGap(18, 18, 18)
                        .addComponent(t_tel_receptor, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel38)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(t_nombre_receptor, javax.swing.GroupLayout.PREFERRED_SIZE, 453, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel41)))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel38)
                    .addComponent(t_nombre_receptor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel41))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel39)
                        .addComponent(t_correo_receptor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(t_tel_receptor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel42))
                    .addComponent(jLabel40))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel13.setBackground(new java.awt.Color(254, 254, 254));
        jPanel13.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 1, 0, new java.awt.Color(0, 0, 0)), "Vehículo", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.ABOVE_TOP, new java.awt.Font("Arial", 1, 10))); // NOI18N

        jLabel50.setText("Modelo:");

        t_modelo_vehiculo.setToolTipText("Modelo de la unidad");
        t_modelo_vehiculo.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        t_modelo_vehiculo.setEnabled(false);

        t_marca_vehiculo.setToolTipText("Marca de la unidad");
        t_marca_vehiculo.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        t_marca_vehiculo.setEnabled(false);

        jLabel52.setText("Marca:");

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

        jLabel62.setText("No. Motor:");

        t_motor_vehiculo.setToolTipText("Número de motor de la unidad");
        t_motor_vehiculo.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        t_motor_vehiculo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                t_motor_vehiculoKeyTyped(evt);
            }
        });

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addComponent(jLabel50)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(t_modelo_vehiculo, javax.swing.GroupLayout.DEFAULT_SIZE, 419, Short.MAX_VALUE))
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addComponent(jLabel52)
                        .addGap(5, 5, 5)
                        .addComponent(t_marca_vehiculo)))
                .addGap(18, 18, 18)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addGap(46, 46, 46)
                        .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel13Layout.createSequentialGroup()
                                .addGap(38, 38, 38)
                                .addComponent(t_serie_vehiculo, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel57))
                        .addGap(45, 45, 45)
                        .addComponent(jLabel62)
                        .addGap(18, 18, 18)
                        .addComponent(t_motor_vehiculo))
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addGap(71, 71, 71)
                        .addComponent(jLabel58)
                        .addGap(18, 18, 18)
                        .addComponent(t_placas_vehiculo, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 15, Short.MAX_VALUE)
                        .addComponent(jLabel56)
                        .addGap(7, 7, 7)
                        .addComponent(t_anio_vehiculo, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18))))
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel50)
                    .addComponent(t_modelo_vehiculo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel58)
                    .addComponent(t_placas_vehiculo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel56)
                    .addComponent(t_anio_vehiculo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(14, 14, 14)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel57)
                        .addComponent(t_serie_vehiculo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel62)
                        .addComponent(t_motor_vehiculo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel52)
                        .addComponent(t_marca_vehiculo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel87.setText("condiciones de Pago:");

        t_condiciones.setToolTipText("Condiciones de pago de la nota");
        t_condiciones.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        t_condiciones.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                t_condicionesKeyTyped(evt);
            }
        });

        jLabel88.setText("*");

        t_codigo.setToolTipText("Numero de factura interna");
        t_codigo.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        t_codigo.setEnabled(false);
        t_codigo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                t_codigoKeyTyped(evt);
            }
        });

        jLabel48.setText("Folio interno:");

        jLabel37.setText("*");

        jLabel49.setText("Orden:");

        t_orden.setToolTipText("Numero de orden");
        t_orden.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        t_orden.setEnabled(false);
        t_orden.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                t_ordenKeyTyped(evt);
            }
        });

        t_extra.setToolTipText("Nombre del contacto receptor");
        t_extra.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        t_extra.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                t_extraKeyTyped(evt);
            }
        });

        jLabel51.setText("Extra(max 100 car):");

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
                        .addGap(12, 12, 12)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(jLabel51)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(t_extra, javax.swing.GroupLayout.PREFERRED_SIZE, 453, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(jLabel48)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(t_codigo, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel37)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel49)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(t_orden, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel87)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(t_condiciones, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(4, 4, 4)
                                .addComponent(jLabel88)))))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel49)
                        .addComponent(t_orden, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel48)
                        .addComponent(t_codigo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel37))
                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel87)
                        .addComponent(t_condiciones, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel88)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(t_extra, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel51))
                .addContainerGap(89, Short.MAX_VALUE))
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
        t_xml.setToolTipText("Nota en formato XML");
        t_xml.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        t_xml.setEnabled(false);

        t_pdf.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        t_pdf.setToolTipText("Nota en formato XML");
        t_pdf.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        t_pdf.setEnabled(false);

        b_xml.setBackground(new java.awt.Color(2, 135, 242));
        b_xml.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        b_xml.setForeground(new java.awt.Color(254, 254, 254));
        b_xml.setIcon(new ImageIcon("imagenes/xml_icon.png"));
        b_xml.setText("Abrir");
        b_xml.setToolTipText("Abrir Nota en formato XML");
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
        b_pdf.setToolTipText("Abrir Nota en formato XML");
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
        b_email.setToolTipText("Comprime el XML y el PDF para enviarlo por correo electrónico");
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
                .addContainerGap(499, Short.MAX_VALUE))
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
                .addContainerGap(205, Short.MAX_VALUE))
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
                                            if(c_metodo_pago.getSelectedItem().toString().compareTo("")!=0)
                                            {
                                                if(c_moneda.getSelectedIndex()>=0)
                                                {
                                                    if(t_tipo_cambio.getText().trim().compareTo("")!=0)
                                                    {
                                                        if(t_datos.getRowCount()>0)
                                                        {
                                                            if(t_nombre_emisor.getText().trim().compareTo("")!=0)
                                                            {
                                                                if(t_correo_emisor.getText().trim().compareTo("")!=0)
                                                                {
                                                                    if(t_nombre_receptor.getText().trim().compareTo("")!=0)
                                                                    {
                                                                        if(t_correo_receptor.getText().trim().compareTo("")!=0)
                                                                        {
                                                                            if(t_condiciones.getText().trim().compareTo("")!=0)
                                                                            {
                                                                                boolean entra=true;
                                                                                if(cb_tipo_relacion.getSelectedItem().toString().compareTo("BUSCAR")!=0)
                                                                                {
                                                                                    if(t_relacionados.getRowCount()<=0)
                                                                                        entra=false;
                                                                                }
                                                                                if(entra==true)
                                                                                {
                                                                                    if(bandera==true)
                                                                                    {
                                                                                        bandera=false;
                                                                                        habilita(false, false);
                                                                                        progreso.setIndeterminate(true);
                                                                                        progreso.setString("Conectando al servidor SAT Espere");
                                                                                        facturaElectronica();
                                                                                    }
                                                                                }
                                                                                else
                                                                                {
                                                                                    JOptionPane.showMessageDialog(null, "Debe ingresar los CFDI Relacionados\n"
                                                                                            + "o quitar el campo relacion");
                                                                                }
                                                                            }
                                                                            else
                                                                            {
                                                                                contenedor.setSelectedIndex(3);
                                                                                JOptionPane.showMessageDialog(null, "Debe ingresar las condiciones de pago");
                                                                                t_condiciones.requestFocus();
                                                                            }
                                                                        }
                                                                        else
                                                                        {
                                                                            contenedor.setSelectedIndex(3);
                                                                            JOptionPane.showMessageDialog(null, "Debes ingresar el correo del receptor");
                                                                            t_correo_receptor.requestFocus();
                                                                        }
                                                                    }
                                                                    else
                                                                    {
                                                                        contenedor.setSelectedIndex(3);
                                                                        JOptionPane.showMessageDialog(null, "Debes ingresar el nombre del receptor");
                                                                        t_nombre_receptor.requestFocus();
                                                                    }
                                                                }
                                                                else
                                                                {
                                                                    contenedor.setSelectedIndex(3);
                                                                    JOptionPane.showMessageDialog(null, "Debes ingresar el correo del emisor");
                                                                    t_correo_emisor.requestFocus();
                                                                }
                                                            }
                                                            else
                                                            {
                                                                contenedor.setSelectedIndex(3);
                                                                JOptionPane.showMessageDialog(null, "Debes ingresar el nombre del contacto emisor");
                                                                t_nombre_emisor.requestFocus();
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
                                                c_metodo_pago.requestFocus();
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
                JOptionPane.showMessageDialog(null, "Información actualizada");
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
        if(t_calle.getText().length()>=150)
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
        evt.setKeyChar(Character.toUpperCase(evt.getKeyChar()));
        if(t_cp.getText().length()>=6)
        evt.consume();
    }//GEN-LAST:event_t_cpKeyTyped

    private void t_coloniaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_coloniaKeyTyped
        // TODO add your handling code here:
        evt.setKeyChar(Character.toUpperCase(evt.getKeyChar()));
        if(t_colonia.getText().length()>=100)
            evt.consume();
    }//GEN-LAST:event_t_coloniaKeyTyped

    private void t_municipioKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_municipioKeyTyped
        // TODO add your handling code here:
        evt.setKeyChar(Character.toUpperCase(evt.getKeyChar()));
        if(t_municipio.getText().length()>=100)
            evt.consume();
    }//GEN-LAST:event_t_municipioKeyTyped

    private void t_incisoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_incisoKeyTyped
        // TODO add your handling code here:
        evt.setKeyChar(Character.toUpperCase(evt.getKeyChar()));
        if(t_inciso.getText().length()>=13)
            evt.consume(); 
    }//GEN-LAST:event_t_incisoKeyTyped

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
                factura=(Nota)session.get(Nota.class, factura.getIdNota());
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
                            en= new EnviarCorreo(new javax.swing.JFrame(), true, mail,"Envío Nota de crédito", "Hola buen dia, envío XML y PDF correspondiente al siniestro:."+factura.getOrden().getSiniestro(), envio, this.user, this.sessionPrograma);
                        else
                            en= new EnviarCorreo(new javax.swing.JFrame(), true, mail,"Envío Nota de crédito", "Hola buen dia, envío XML y PDF", envio, this.user, this.sessionPrograma);
                    }
                    if(factura.getOrdenExterna()!=null)
                    {
                        if(factura.getOrdenExterna().getSiniestro()!=null)
                            en= new EnviarCorreo(new javax.swing.JFrame(), true, mail,"Envío Nota de crédito", "Hola buen dia, envío XML y PDF correspondiente al siniestro:."+factura.getOrdenExterna().getSiniestro(), envio, this.user, this.sessionPrograma);
                        else
                            en= new EnviarCorreo(new javax.swing.JFrame(), true, mail,"Envío Nota de crédito", "Hola buen dia, envío XML y PDF", envio, this.user, this.sessionPrograma);
                    }
                    Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
                    en.setLocation((d.width/2)-(en.getWidth()/2), (d.height/2)-(en.getHeight()/2));
                    en.setVisible(true);
                }
                else
                    JOptionPane.showMessageDialog(null, "error al comprimir.");
            }catch(Exception e)
            {
                JOptionPane.showMessageDialog(null, "error al comprimir.");
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
                Configuracion config=(Configuracion)session.createCriteria(Configuracion.class).add(Restrictions.eq("nombre", factura.getNombreEmisor())).uniqueResult();
                
                if(t_xml.getText().compareTo("")==0)
                    t_xml.setText(config.getRfc()+"_"+t_serie_factura.getText()+"_"+t_folio_factura.getText()+"_"+t_rfc.getText()+".xml");
                File xml=new File(ruta+"xml-timbrados/"+t_xml.getText());
                if(xml.exists())//existe en nuestro directorio
                    Desktop.getDesktop().open(xml);
                else//descargar el archivo del SAP
                {
                    if(PAC.compareTo("M")==0)
                    {
                        factura=(Nota)session.get(Nota.class, factura.getIdNota());
                        ArrayList aux=new ArrayList();
                        aux.add(config.getRequestor());//Lo proporcionará MySuite
                        aux.add("GET_DOCUMENT");//Tipo de Transaccion
                        aux.add("MX");//Codigo de pais
                        aux.add(config.getRequestor());//igual que Requestor
                        aux.add(config.getUsuario_1());//Country.Entity.nombre_usuario
                        aux.add(config.getRfc());
                        aux.add(t_serie_factura.getText());//serie del documento.
                        aux.add(t_folio_factura.getText());//folio
                        aux.add("PDF XML");//documentos
                        abrir="xml";

                        progreso.setIndeterminate(true);
                        progreso.setString("Descargando archivo espere");
                        ApiMysuite timbrar=new ApiMysuite(ruta);
                        ArrayList resp=timbrar.llamarSoapDocumento(aux);
                        switch(resp.get(0).toString())
                        {
                            case "1"://Se descargo el documento
                                Desktop.getDesktop().open(xml);
                                break;
                            case "-1"://Error al descargar
                                JOptionPane.showMessageDialog(null, resp.get(1).toString());
                                break;
                        }
                        progreso.setString("Listo");
                        progreso.setIndeterminate(false);
                    }
                    else
                    {
                        progreso.setIndeterminate(true);
                        progreso.setString("Descargando archivo espere");
                        consultaFacturaElectronica(xml);
                    }
                }
            }catch(Exception e)
            {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error al consultar la base de datos");
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
                Configuracion config=(Configuracion)session.createCriteria(Configuracion.class).add(Restrictions.eq("nombre", factura.getNombreEmisor())).uniqueResult();
                if(t_pdf.getText().compareTo("")==0)
                    t_pdf.setText(config.getRfc()+"_"+t_serie_factura.getText()+"_"+t_folio_factura.getText()+"_"+t_rfc.getText()+".pdf");
                File xml=new File(ruta+"xml-timbrados/"+t_pdf.getText());
                if(xml.exists())//existe en nuestro directorio
                    Desktop.getDesktop().open(xml);
                else//descargar el archivo del SAP
                {
                    if(PAC.compareTo("M")==0)
                    {
                        factura=(Nota)session.get(Nota.class, factura.getIdNota());
                        ArrayList aux=new ArrayList();
                        aux.add(config.getRequestor());//Lo proporcionará MySuite
                        aux.add("GET_DOCUMENT");//Tipo de Transaccion
                        aux.add("MX");//Codigo de pais
                        aux.add(config.getRequestor());//igual que Requestor
                        aux.add(config.getUsuario_1());//Country.Entity.nombre_usuario
                        aux.add(config.getRfc());
                        aux.add(t_serie_factura.getText());//serie del documento.
                        aux.add(t_folio_factura.getText());//folio
                        aux.add("PDF XML");//documentos
                        abrir="pdf";

                        progreso.setIndeterminate(true);
                        progreso.setString("Descargando archivo espere");
                        ApiMysuite timbrar=new ApiMysuite(ruta);
                        ArrayList resp=timbrar.llamarSoapDocumento(aux);
                        switch(resp.get(0).toString())
                        {
                            case "1"://Se descargo el documento
                                Desktop.getDesktop().open(xml);
                                break;
                            case "-1"://Error al descargar
                                JOptionPane.showMessageDialog(null, resp.get(1).toString());
                                break;
                        }
                        progreso.setString("Listo");
                        progreso.setIndeterminate(false);
                    }
                    else
                    {
                        factura=(Nota)session.get(Nota.class, factura.getIdNota());
                        Formatos formato = new Formatos(this.user, this.sessionPrograma, factura,configuracion);
                        formato.nota();
                    }
                }
            }catch(Exception e)
            {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error al consultar la base de datos");
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
            JOptionPane.showMessageDialog(null, "Debes generar comprobante primero para tratar de recuperar los datos del SAP");
        }
    }//GEN-LAST:event_b_pdfActionPerformed

    private void b_descargarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_descargarActionPerformed
        // TODO add your handling code here:
        if(t_facturas.getSelectedRow()>-1)
        {
            Session session = HibernateUtil.getSessionFactory().openSession();
            try
            {
                session.beginTransaction().begin();
                Configuracion config=(Configuracion)session.get(Configuracion.class, 1);
                factura=(Nota)session.get(Nota.class, factura.getIdNota());
                
                ArrayList aux=new ArrayList();
                aux.add(config.getRequestor());//Lo proporcionará MySuite
                aux.add("GET_DOCUMENT");//Tipo de Transaccion
                aux.add("MX");//Codigo de pais
                aux.add(config.getRequestor());//igual que Requestor
                aux.add(config.getUsuario_1());//Country.Entity.nombre_usuario
                aux.add(config.getRfc());
                aux.add(t_facturas.getValueAt(t_facturas.getSelectedRow(), 3).toString());//serie del documento.
                aux.add(t_facturas.getValueAt(t_facturas.getSelectedRow(), 4).toString());//folio
                aux.add("PDF XML");//documentos
                abrir="pdf";
                ApiMysuite timbrar=new ApiMysuite(ruta);
                ArrayList resp=timbrar.llamarSoapDocumento(aux);
                switch(resp.get(0).toString())
                {
                    case "1"://Se descargo el documento
                        System.out.println(resp.get(6).toString());
                        File xml=new File(ruta+"xml-timbrados/"+config.getRfc()+"_"+t_serie_factura.getText()+"_"+t_folio_factura.getText()+"_"+t_rfc.getText()+".pdf");
                        Desktop.getDesktop().open(xml);
                        break;
                    case "-1"://Error al descargar
                        JOptionPane.showMessageDialog(null, resp.get(1).toString());
                        break;
                }
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
                    if(session.isOpen())
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
            idBuscar=t_facturas.getValueAt(t_facturas.getSelectedRow(), 1).toString();
            Session session = HibernateUtil.getSessionFactory().openSession();
            try
            {
                session.beginTransaction().begin();
                Configuracion config=(Configuracion)session.get(Configuracion.class, 1);
                factura=(Nota)session.get(Nota.class, factura.getIdNota());
                ArrayList rq=new ArrayList();
                rq.add(config.getRequestor());//Lo proporcionará MySuite
                rq.add("CANCEL_XML");//Tipo de Transaccion
                rq.add("MX");//Codigo de pais
                rq.add(config.getRequestor());//igual que Requestor
                rq.add(config.getUsuario_1());//Country.Entity.nombre_usuario
                rq.add(config.getRfc());
                rq.add(idBuscar);//GUIID.
                rq.add("");
                rq.add("");
                ApiMysuite timbrar=new ApiMysuite(ruta);
                ArrayList respuesta=timbrar.llamarSoapCancela(rq);
                if(respuesta.size()>0)
                {
                    switch(respuesta.get(0).toString())
                    {
                        case "1"://Se Canceló
                            factura=(Nota) session.createCriteria(Nota.class).add(Restrictions.eq("FFiscal", idBuscar)).uniqueResult();
                            if(factura!=null)
                            {
                                factura.setOrden(null);
                                factura.setEstadoFactura("Cancelado");
                                session.update(factura);
                                session.beginTransaction().commit();
                                DefaultTableModel modelElimina=(DefaultTableModel)t_facturas.getModel();
                                modelElimina.removeRow(t_facturas.getSelectedRow());
                                t_facturas.setModel(modelElimina);
                                formatoTablafactura();
                                habilita2(true);
                                progreso.setString("Listo");
                                progreso.setIndeterminate(false);
                                JOptionPane.showMessageDialog(null, "El UUID ya esta cancelado");
                            }
                            else
                            {
                                habilita2(true);
                                progreso.setString("Listo");
                                progreso.setIndeterminate(false);
                                JOptionPane.showMessageDialog(null, "El UUID ya esta cancelado pero no pudo actualizarse la base de datos");
                            }
                            break;
                            
                        case "-1":
                            habilita2(true);
                            progreso.setString("Listo");
                            progreso.setIndeterminate(false);
                            JOptionPane.showMessageDialog(null, respuesta.get(1).toString());
                            break;
                    }
                }
                else
                {
                    habilita2(true);
                    progreso.setString("Listo");
                    progreso.setIndeterminate(false);
                    JOptionPane.showMessageDialog(null, "Error al consultar la base de datos");
                }
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
                    if(session.isOpen())
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

    private void t_condicionesKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_condicionesKeyTyped
        // TODO add your handling code here:
        evt.setKeyChar(Character.toUpperCase(evt.getKeyChar()));
        if(t_condiciones.getText().length()>=20)
            evt.consume();
    }//GEN-LAST:event_t_condicionesKeyTyped

    private void t_codigoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_codigoKeyTyped
        // TODO add your handling code here:
        evt.setKeyChar(Character.toUpperCase(evt.getKeyChar()));
        if(t_codigo.getText().length()>=6)
            evt.consume();
    }//GEN-LAST:event_t_codigoKeyTyped

    private void t_ordenKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_ordenKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_t_ordenKeyTyped

    private void t_contratanteKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_contratanteKeyTyped
        // TODO add your handling code here:
        evt.setKeyChar(Character.toUpperCase(evt.getKeyChar()));
        if(t_codigo.getText().length()>=150)
            evt.consume();
    }//GEN-LAST:event_t_contratanteKeyTyped

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

    private void t_extraKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_extraKeyTyped
        // TODO add your handling code here:
        evt.setKeyChar(Character.toUpperCase(evt.getKeyChar()));
        if(t_extra.getText().length()>=100)
        evt.consume();
    }//GEN-LAST:event_t_extraKeyTyped

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
        factura=(Nota)session.get(Nota.class, factura.getIdNota());
        Concepto con = new Concepto();
        con.setCantidad(0.0);
        con.setMedida("PZAS");
        con.setDescripcion("");
        con.setPrecio(0.0);
        con.setDescuento(0.0);
        con.setNota(factura);
        int dato=(int)session.save(con);
        session.beginTransaction().commit();

        DefaultTableModel temp = (DefaultTableModel) t_datos.getModel();
        Object nuevo[]= {dato,0.0d,"PZAS","","",0.0d, 0.0d, 0.0d};
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
                h1r0.createCell(3).setCellValue("Clave");
                h1r0.createCell(4).setCellValue("c/u");
                h1r0.createCell(5).setCellValue("Descuento");
                for(int x=0; x<t_datos.getRowCount(); x++)
                {
                    Row h1=hoja.createRow(x+1);
                    h1.createCell(0).setCellValue((double)t_datos.getValueAt(x, 1));
                    h1.createCell(1).setCellValue((String)t_datos.getValueAt(x, 2));
                    h1.createCell(2).setCellValue((String)t_datos.getValueAt(x, 3));
                    h1.createCell(3).setCellValue((String)t_datos.getValueAt(x, 4));
                    h1.createCell(4).setCellValue((double)t_datos.getValueAt(x, 5));
                    h1.createCell(5).setCellValue((double)t_datos.getValueAt(x, 6));
                }
                libro.write(archivo);
                Biff8EncryptionKey.setCurrentUserPassword(null);
                archivo.close();
                JOptionPane.showMessageDialog(null, "Archivo guardado!");
            }catch(Exception e){e.printStackTrace();};
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        // TODO add your handling code here:
        buscaUsoCfdi obj = new buscaUsoCfdi(new javax.swing.JFrame(), true, this.sessionPrograma, this.user);
        obj.t_busca.requestFocus();
        obj.formatoTabla();
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        obj.setLocation((d.width/2)-(obj.getWidth()/2), (d.height/2)-(obj.getHeight()/2));
        obj.setVisible(true);

        UsoCfdi actor=obj.getReturnStatus();
        if(actor!=null)
        {
            t_id_cfdi.setText(actor.getIdUsoCfdi());
            t_descripcion_cfdi.setText(actor.getDescripcion());
        }
        else
        {
            t_id_cfdi.setText("");
            t_descripcion_cfdi.setText("");
        }
    }//GEN-LAST:event_jButton7ActionPerformed

    private void cb_tipo_relacionItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cb_tipo_relacionItemStateChanged
        // TODO add your handling code here:
        if(evt.getStateChange() == ItemEvent.DESELECTED){
            anterior=evt.getItem().toString();
        }
    }//GEN-LAST:event_cb_tipo_relacionItemStateChanged

    private void cb_tipo_relacionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cb_tipo_relacionActionPerformed
        // TODO add your handling code here:
        Session session = HibernateUtil.getSessionFactory().openSession();
        String error="";
        boolean valido=true;
        try
        {
            session.beginTransaction().begin();
            factura = (Nota)session.get(Nota.class, factura.getIdNota());
            switch(cb_tipo_relacion.getSelectedItem().toString())
            {
                case "01":
                    for(int x=0; x<t_relacionados.getRowCount(); x++)
                    {
                        if(t_relacionados.getValueAt(x, 2).toString().compareToIgnoreCase("Ingreso")!=0 && t_relacionados.getValueAt(x, 2).toString().compareToIgnoreCase("Egreso")!=0)
                        {
                            t_relacionados.setRowSelectionInterval(x, x);
                            valido=false;
                        }
                    }
                    if(valido==true)
                    {
                        l_tipo_relacion.setText("Nota de credito de los documentos relacionados");
                        factura.setTipoRelacion("01");
                    }
                    else
                    {
                        JComboBox cn =(JComboBox)evt.getSource();
                        String aux_anterior=anterior;
                        cn.setSelectedItem(anterior);
                        anterior=aux_anterior;
                        error="Hay documentos en la lista que no son de Tipo Ingreso o Egreso.";
                    }
                    break;
                case "03":
                for(int x=0; x<t_relacionados.getRowCount(); x++)
                {
                    if(t_relacionados.getValueAt(x, 2).toString().compareToIgnoreCase("Ingreso")!=0)
                    {
                        t_relacionados.setRowSelectionInterval(x, x);
                        valido=false;
                    }
                }
                if(valido==true)
                {
                    l_tipo_relacion.setText("Devolución de mercancía sobre facturas o traslados previos");
                    factura.setTipoRelacion("03");
                }
                else
                {
                    JComboBox cn =(JComboBox)evt.getSource();
                    String aux_anterior=anterior;
                    cn.setSelectedItem(anterior);
                    anterior=aux_anterior;
                    error="Hay documentos en la lista que no son de Tipo Ingreso";
                }
                break;

                case "04":
                for(int x=0; x<t_relacionados.getRowCount(); x++)
                {
                    if(t_relacionados.getValueAt(x, 2).toString().compareToIgnoreCase("Ingreso")!=0 && t_relacionados.getValueAt(x, 2).toString().compareToIgnoreCase("Egreso")!=0)
                    {
                        t_relacionados.setRowSelectionInterval(x, x);
                        valido=false;
                    }
                }
                if(valido==true)
                {
                    l_tipo_relacion.setText("Sustitución de los CFDI previos");
                    factura.setTipoRelacion("04");
                }
                else
                {
                    JComboBox cn =(JComboBox)evt.getSource();
                    String aux_anterior=anterior;
                    cn.setSelectedItem(anterior);
                    anterior=aux_anterior;
                    error="Hay documentos en la lista que no son de Tipo Ingreso o Egreso.";
                }
                break;

                case "07":
                for(int x=0; x<t_relacionados.getRowCount(); x++)
                {
                    if(t_relacionados.getValueAt(x, 2).toString().compareToIgnoreCase("Ingreso")!=0 && t_relacionados.getValueAt(x, 2).toString().compareToIgnoreCase("Egreso")!=0)
                    {
                        t_relacionados.setRowSelectionInterval(x, x);
                        valido=false;
                    }
                }
                if(valido==true)
                {
                    l_tipo_relacion.setText("CFDI por aplicación de anticipo");
                    factura.setTipoRelacion("07");
                }
                else
                {
                    JComboBox cn =(JComboBox)evt.getSource();
                    String aux_anterior=anterior;
                    cn.setSelectedItem(anterior);
                    anterior=aux_anterior;
                    error="Hay documentos en la lista que no son de Tipo Ingreso o Egreso..";
                }
                break;

                default:
                if(t_relacionados.getRowCount()==0)
                {
                    l_tipo_relacion.setText("");
                    factura.setTipoRelacion(null);
                }
                else
                {
                    JComboBox cn =(JComboBox)evt.getSource();
                    String aux_anterior=anterior;
                    cn.setSelectedItem(anterior);
                    anterior=aux_anterior;
                    error="Debes eleminar primero los documentos de la lista";
                }
                break;
            }
            session.beginTransaction().commit();
        }catch(Exception e)
        {
            session.beginTransaction().rollback();
            e.printStackTrace();
        }
        finally
        {
            if(session!=null)
            if(session.isOpen())
            session.close();
            if(error.compareTo("")!=0)
            JOptionPane.showMessageDialog(null, error);
        }
    }//GEN-LAST:event_cb_tipo_relacionActionPerformed

    private void b_menos1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_menos1ActionPerformed
        h=new Herramientas(user, 0);
        h.session(sessionPrograma);
        if(t_relacionados.getRowCount()>0 && t_relacionados.getSelectedRowCount()>=0)
        {
            Session session = HibernateUtil.getSessionFactory().openSession();
            try
            {
                session.beginTransaction().begin();
                factura = (Nota)session.get(Nota.class, factura.getIdNota());
                DefaultTableModel temp = (DefaultTableModel) t_relacionados.getModel();
                int [] renglones=t_relacionados.getSelectedRows();
                for(int x=0; x<renglones.length; x++)
                {
                    Relacion r_elimina=(Relacion)session.get(Relacion.class, Integer.parseInt(t_relacionados.getValueAt(renglones[x]-x, 3).toString()));
                    session.delete(r_elimina);
                    temp.removeRow(renglones[x]-x);
                }
                session.beginTransaction().commit();
            }catch(Exception e)
            {
                session.beginTransaction().rollback();
                e.printStackTrace();
            }
            finally
            {
                if(session!=null)
                if(session.isOpen())
                session.close();
            }
        }
    }//GEN-LAST:event_b_menos1ActionPerformed

    private void b_mas1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_mas1ActionPerformed
        if(cb_tipo_relacion.getSelectedItem().toString().compareToIgnoreCase("BUSCAR")!=0)
        {
            Session session = HibernateUtil.getSessionFactory().openSession();
            try
            {
                int opcion=-1;
                if(cb_tipo_relacion.getSelectedItem().toString().compareToIgnoreCase("03")!=0)
                    opcion =JOptionPane.showOptionDialog(null, "Selecciona el tipo de documento a ingresar", "Agregar Documento", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, new Object[] {"Ingreso", "Egreso", "Cancelar"}, "Cancelar");
                else
                {
                    opcion=0;
                }
                switch(opcion)
                {
                    case 0:
                        buscaFactura obj = new buscaFactura(new javax.swing.JFrame(), true, this.sessionPrograma, this.user, 0, configuracion);
                        obj.t_busca.requestFocus();
                        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
                        obj.setLocation((d.width/2)-(obj.getWidth()/2), (d.height/2)-(obj.getHeight()/2));
                        obj.setVisible(true);
                        Factura aux=obj.getReturnStatus();
                        if(aux!=null && busca_relacion(aux.getFFiscal())==false)
                        {
                            if(aux.getFFiscal()!=null || aux.getFFiscal().compareTo("")!=0)
                            {
                                aux=(Factura)session.get(Factura.class, aux.getIdFactura());
                                Relacion r_nuevo=new Relacion();
                                r_nuevo.setNotaByIdNota(factura);
                                r_nuevo.setFacturaByRelacionFactura(aux);
                                session.save(r_nuevo);
                                DefaultTableModel temp = (DefaultTableModel) t_relacionados.getModel();
                                Object nuevo[]= {aux.getIdFactura(),aux.getFFiscal(),"Ingreso", r_nuevo.getIdRelacion()};
                                temp.addRow(nuevo);
                            }
                            else
                            {
                                JOptionPane.showMessageDialog(null, "El documento no esta timbrado aun!");
                            }
                        }
                        break;
                    case 1:
                        buscaNota obj1 = new buscaNota(new javax.swing.JFrame(), true, this.sessionPrograma, this.user,0, configuracion);
                        obj1.t_busca.requestFocus();
                        Dimension d1 = Toolkit.getDefaultToolkit().getScreenSize();
                        obj1.setLocation((d1.width/2)-(obj1.getWidth()/2), (d1.height/2)-(obj1.getHeight()/2));
                        obj1.setVisible(true);
                        Nota aux1=obj1.getReturnStatus();
                        if(aux1!=null && busca_relacion(aux1.getFFiscal())==false)
                        {
                            if(aux1.getFFiscal()!=null || aux1.getFFiscal().compareTo("")!=0)
                            {
                                aux1=(Nota)session.get(Nota.class, aux1.getIdNota());
                                Relacion r_nuevo1=new Relacion();
                                r_nuevo1.setNotaByIdNota(factura);
                                r_nuevo1.setNotaByRelacionNota(aux1);
                                session.save(r_nuevo1);
                                DefaultTableModel temp1 = (DefaultTableModel) t_relacionados.getModel();
                                Object nuevo1[]= {aux1.getIdNota(),aux1.getFFiscal(),"Egreso", r_nuevo1.getIdRelacion()};
                                temp1.addRow(nuevo1);
                            }
                            else
                            {
                                JOptionPane.showMessageDialog(null, "El documento no esta timbrado aun!");
                            }
                        }
                        break;
                }
                session.beginTransaction().begin();
                factura = (Nota)session.get(Nota.class, factura.getIdNota());

                session.beginTransaction().commit();
            }catch(Exception e)
            {
                session.beginTransaction().rollback();
                e.printStackTrace();
                evt=null;
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
            JOptionPane.showMessageDialog(null, "Debe seleccionar primero un tipo de relación");
        }
    }//GEN-LAST:event_b_mas1ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        // TODO add your handling code here:
        if(t_datos.getSelectedRow()>=0)
        {
            buscarProServicio buscaServicio;
            buscaServicio = new buscarProServicio(null, true, user, sessionPrograma);
            Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
            buscaServicio.setLocation((d.width / 2) - (buscaServicio.getWidth() / 2), (d.height / 2) - (buscaServicio.getHeight() / 2));
            buscaServicio.setVisible(true);
            if(buscaServicio.getReturnStatus()!=null){
                int [] renglones=t_datos.getSelectedRows();
                if(renglones!=null)
                {
                    String valor=buscaServicio.getReturnStatus().getIdProductoServicio();
                    for(int x=0; x<renglones.length; x++)
                    {
                        t_datos.setValueAt(valor, renglones[x], 4);
                    }
                }
            }
        }
        else
        {
            JOptionPane.showMessageDialog(null, "Debes seleccionar almenos un concepto de la tabla.");
        }
    }//GEN-LAST:event_jButton6ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JFileChooser aviso;
    private javax.swing.JButton b_actualizar;
    private javax.swing.JButton b_cancelar;
    private javax.swing.JButton b_descargar;
    private javax.swing.JButton b_email;
    private javax.swing.JButton b_generar;
    private javax.swing.JButton b_mas;
    private javax.swing.JButton b_mas1;
    private javax.swing.JButton b_menos;
    private javax.swing.JButton b_menos1;
    private javax.swing.JButton b_pdf;
    private javax.swing.JButton b_salir;
    private javax.swing.JButton b_xml;
    private javax.swing.JComboBox c_estado;
    private javax.swing.JComboBox c_forma_pago;
    private javax.swing.JComboBox c_metodo_pago;
    private javax.swing.JComboBox c_moneda;
    private javax.swing.JComboBox c_pais;
    private javax.swing.JComboBox cb_tipo_relacion;
    private javax.swing.JComboBox cfdi;
    private javax.swing.JDialog consulta;
    private javax.swing.JTabbedPane contenedor;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
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
    private javax.swing.JLabel jLabel30;
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
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel51;
    private javax.swing.JLabel jLabel52;
    private javax.swing.JLabel jLabel53;
    private javax.swing.JLabel jLabel56;
    private javax.swing.JLabel jLabel57;
    private javax.swing.JLabel jLabel58;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel62;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel70;
    private javax.swing.JLabel jLabel72;
    private javax.swing.JLabel jLabel75;
    private javax.swing.JLabel jLabel79;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel80;
    private javax.swing.JLabel jLabel81;
    private javax.swing.JLabel jLabel82;
    private javax.swing.JLabel jLabel83;
    private javax.swing.JLabel jLabel86;
    private javax.swing.JLabel jLabel87;
    private javax.swing.JLabel jLabel88;
    private javax.swing.JLabel jLabel89;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
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
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JLabel l_emisor;
    private javax.swing.JLabel l_iva;
    private javax.swing.JLabel l_iva1;
    private javax.swing.JLabel l_subtotal;
    private javax.swing.JLabel l_tipo_relacion;
    private javax.swing.JLabel l_total;
    private javax.swing.JComboBox medida;
    private javax.swing.JProgressBar progreso;
    private javax.swing.JTextField t_anio_vehiculo;
    private javax.swing.JTextField t_calle;
    private javax.swing.JTextField t_codigo;
    private javax.swing.JTextField t_colonia;
    private javax.swing.JTextField t_condiciones;
    private javax.swing.JTextField t_contratante;
    private javax.swing.JTextField t_correo_emisor;
    private javax.swing.JTextField t_correo_receptor;
    private javax.swing.JTextField t_cp;
    private javax.swing.JTextField t_cuenta_pago;
    private javax.swing.JTable t_datos;
    private javax.swing.JFormattedTextField t_deducible;
    private javax.swing.JTextField t_descripcion_cfdi;
    private javax.swing.JFormattedTextField t_descuento;
    private javax.swing.JTextField t_extra;
    private javax.swing.JTable t_facturas;
    private javax.swing.JTextField t_folio_factura;
    private javax.swing.JTextField t_id_cfdi;
    private javax.swing.JTextField t_inciso;
    private javax.swing.JFormattedTextField t_iva;
    private javax.swing.JFormattedTextField t_iva1;
    private javax.swing.JTextField t_marca_vehiculo;
    private javax.swing.JTextField t_modelo_vehiculo;
    private javax.swing.JTextField t_motor_vehiculo;
    private javax.swing.JTextField t_municipio;
    private javax.swing.JTextField t_no_exterior;
    private javax.swing.JTextField t_nombre_emisor;
    private javax.swing.JTextField t_nombre_receptor;
    private javax.swing.JTextField t_numero;
    private javax.swing.JTextField t_orden;
    private javax.swing.JTextField t_pdf;
    private javax.swing.JTextField t_placas_vehiculo;
    private javax.swing.JTable t_relacionados;
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
                factura=(Nota)session.get(Nota.class, factura.getIdNota());
                //Emisor
                l_emisor.setText(factura.getNombreEmisor());
                //Receptor
                t_iva1.setValue(factura.getIva());
                try{
                t_iva1.commitEdit();}catch(Exception e){}
                t_social.setText(acentos(factura.getNombreReceptor().trim()));
                t_rfc.setText(factura.getRfcReceptor().trim());
                t_calle.setText(acentos(factura.getCalleReceptor().trim()));
                t_no_exterior.setText(factura.getNumeroExteriorReceptor().trim());
                t_cp.setText(factura.getCpReceptor().trim());
                t_colonia.setText(acentos(factura.getColoniaReceptor().trim()));
                t_municipio.setText(acentos(factura.getMunicipioReceptor().trim()));
                c_estado.setSelectedItem(acentos(factura.getEstadoReceptor()));
                c_pais.setSelectedItem(acentos(factura.getPaisReceptor()));
                if(factura.getUsoCfdi()!=null)
                {
                    t_id_cfdi.setText(factura.getUsoCfdi().getIdUsoCfdi());
                    t_descripcion_cfdi.setText(factura.getUsoCfdi().getDescripcion());
                }
                else
                {
                    t_id_cfdi.setText("");
                    t_descripcion_cfdi.setText("");
                }
                
                
                //Productos y servicios
                c_metodo_pago.setSelectedItem(factura.getMetodoPago().trim());
                t_cuenta_pago.setText(factura.getCuentaPago().trim());
                t_tipo_cambio.setText(""+factura.getFactorCambio());
                t_tipo_cambio.setValue(factura.getFactorCambio());
                c_moneda.setSelectedItem(factura.getMoneda().trim());
                if(factura.getCondicionesPago().compareTo("UNA SOLA EXHIBICION")==0)
                    c_forma_pago.setSelectedIndex(0);
                else
                    c_forma_pago.setSelectedItem(factura.getCondicionesPago());
                //PAC
                PAC=factura.getPac();
                //Fecha
                if(factura.getFFiscal()==null)
                {
                    long mFactura, mActual, diff;
                    Date actual=new Date();
                    Calendar cFactura = Calendar.getInstance();
                    Calendar cActual = Calendar.getInstance();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

                    cFactura.setTime(factura.getFecha());
                    cActual.setTime(actual);
                    mFactura = cFactura.getTimeInMillis();
                    mActual = cActual.getTimeInMillis();
                    diff = mActual-mFactura;
                    long diffMinutos =  Math.abs (diff / (60 * 1000));
                    if(diffMinutos > 4320)
                        fecha_factura=new Date();
                    else
                        fecha_factura=factura.getFecha();
                }
                else
                    fecha_factura=factura.getFecha();
                //Addenda
                t_codigo.setText(""+factura.getIdNota());
                if(factura.getOrden()!=null)
                {
                    if(factura.getOrden().getPoliza()!=null)
                        t_numero.setText(factura.getOrden().getPoliza().trim());
                    else
                        t_numero.setText("");
                    if(factura.getOrden().getInciso()!=null)
                        t_inciso.setText(factura.getOrden().getInciso().trim());
                    else
                        t_inciso.setText("");
                    if(factura.getOrden().getSiniestro()!=null)
                        t_siniestro.setText(factura.getOrden().getSiniestro().trim());
                    else
                        t_siniestro.setText("");
                }
                if(factura.getOrdenExterna()!=null)
                {
                    if(factura.getOrdenExterna().getPoliza()!=null)
                        t_numero.setText(factura.getOrdenExterna().getPoliza().trim());
                    else
                        t_numero.setText("");
                    if(factura.getOrdenExterna().getInciso()!=null)
                        t_inciso.setText(factura.getOrdenExterna().getInciso().trim());
                    else
                        t_inciso.setText("");
                    if(factura.getOrdenExterna().getSiniestro()!=null)
                        t_siniestro.setText(factura.getOrdenExterna().getSiniestro().trim());
                    else
                        t_siniestro.setText("");
                }
                Configuracion config=(Configuracion)session.get(Configuracion.class, 1);
                
                if(factura.getContactoEmisor()!=null)
                    t_nombre_emisor.setText(config.getContacto().trim());
                else
                    t_nombre_emisor.setText(factura.getContactoEmisor().trim());
                if(factura.getCorreoEmisor()!=null)
                    t_correo_emisor.setText(config.getMail().trim());
                else
                    t_correo_emisor.setText(factura.getCorreoEmisor().trim());
                if(factura.getTelefonoEmisor()!=null)
                    t_tel_emisor.setText(factura.getTelefonoEmisor().trim());
                else
                    t_tel_emisor.setText("");
                
                if(factura.getContactoReceptor()!=null)
                    t_nombre_receptor.setText(factura.getContactoReceptor().trim());
                else
                    t_nombre_receptor.setText("");
                if(factura.getCorreoReceptor()!=null)
                    t_correo_receptor.setText(factura.getCorreoReceptor().trim());
                else
                    t_correo_receptor.setText("");
                if(factura.getTelefonoReceptor()!=null)
                    t_tel_receptor.setText(factura.getTelefonoReceptor().trim());
                else
                    t_tel_receptor.setText("");
                
                if(factura.getOrden()!=null)
                {
                    t_modelo_vehiculo.setText(factura.getOrden().getTipo().getTipoNombre().trim());
                    t_marca_vehiculo.setText(factura.getOrden().getMarca().getMarcaNombre().trim());
                    t_anio_vehiculo.setText(""+factura.getOrden().getModelo());
                    if(factura.getOrden().getNoSerie()!=null)
                        t_serie_vehiculo.setText(factura.getOrden().getNoSerie().trim());
                    if(factura.getOrden().getNoPlacas()!=null)
                        t_placas_vehiculo.setText(factura.getOrden().getNoPlacas().trim());
                    if(factura.getOrden().getNoMotor()!=null)
                        t_motor_vehiculo.setText(factura.getOrden().getNoMotor().trim());
                    this.t_deducible.setText(""+factura.getOrden().getDeducible());
                    t_deducible.setValue(factura.getOrden().getDeducible());
                    t_orden.setText(""+factura.getOrden().getIdOrden());
                    if(factura.getOrden().getContratante()!=null)
                        t_contratante.setText(factura.getOrden().getContratante().trim());
                }
                if(factura.getOrdenExterna()!=null)
                {
                    if(factura.getOrdenExterna().getTipo()!=null)
                        t_modelo_vehiculo.setText(factura.getOrdenExterna().getTipo().getTipoNombre().trim());
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
                    //t_orden.setText(""+factura.getOrdenExterna().getIdOrden());
                    if(factura.getOrdenExterna().getContratante()!=null)
                        t_contratante.setText(factura.getOrdenExterna().getContratante().trim());
                }
                if(factura.getCondicionesPago()!=null)
                    this.t_condiciones.setText(factura.getCondicionesPago().trim());
                else
                    this.t_condiciones.setText("");
                if(factura.getExtra()!=null)
                    t_extra.setText(factura.getExtra());
                else
                    t_extra.setText("");

                //cargar las partidas
                Concepto[] partidas = (Concepto[])session.createCriteria(Concepto.class).
                                        add(Restrictions.eq("nota.idNota", factura.getIdNota())).
                                        addOrder(Order.asc("idConcepto")).list().toArray(new Concepto[0]);
                for(int a=0; a<partidas.length; a++)
                {
                    double total_lista=partidas[a].getPrecio()*partidas[a].getCantidad();
                    double descuento=partidas[a].getDescuento()/100;
                    double total=total_lista-(total_lista*descuento);
                    String descripcion=partidas[a].getDescripcion();
                    descripcion=descripcion.replaceAll(" +", " ");
                    descripcion=descripcion.trim();
                    
                    String cve_producto="";
                    if(partidas[a].getProductoServicio()!=null)
                        cve_producto=partidas[a].getProductoServicio().getIdProductoServicio();
                    model.addRow(
                            new Object[]
                            {
                                partidas[a].getIdConcepto(),
                                partidas[a].getCantidad(), 
                                partidas[a].getMedida(), 
                                acentos(descripcion), 
                                cve_producto,
                                partidas[a].getPrecio(),
                                partidas[a].getDescuento(),
                               total
                            });
                }
                iva=config.getIva();
                t_datos.setModel(model);
                formatoTabla();
                sumaTotales();
                
                DefaultTableModel temp = (DefaultTableModel) t_relacionados.getModel();
                if(factura.getTipoRelacion()!=null)
                {
                    cb_tipo_relacion.setSelectedItem(factura.getTipoRelacion());
                    Relacion[] r_factura=(Relacion[])session.createCriteria(Relacion.class).
                                        add(Restrictions.eq("notaByIdNota.idNota", factura.getIdNota())).
                                        addOrder(Order.asc("idRelacion")).list().toArray(new Relacion[0]);
                    temp.setRowCount(0);
                    for(int ren=0; ren<r_factura.length; ren++)
                    {
                        Factura f_relacion= r_factura[ren].getFacturaByRelacionFactura();
                        Nota n_relacion= r_factura[ren].getNotaByRelacionNota();
                        if(f_relacion!=null)
                        {
                            Object nuevo[]= {f_relacion.getIdFactura(),f_relacion.getFFiscal(),"Ingreso", r_factura[ren].getIdRelacion()};
                            temp.addRow(nuevo);
                        }
                        if(n_relacion!=null)
                        {
                            Object nuevo1[]= {n_relacion.getIdNota(),n_relacion.getFFiscal(),"Egreso",r_factura[ren].getIdRelacion()};
                            temp.addRow(nuevo1);
                        }
                    }
                }
                else
                    cb_tipo_relacion.setSelectedItem("BUSCAR");
                
                if(factura.getEstadoFactura().compareTo("Facturado")==0)
                {
                    if(factura.getSerie()!=null)
                        t_serie_factura.setText(factura.getSerie());
                    if(factura.getSerieExterno()!=null)
                        t_serie_factura.setText(factura.getSerieExterno());
                    if(factura.getFolio()!=null)
                        t_folio_factura.setText(factura.getFolio());
                    if(factura.getFolioExterno()!=null)
                        t_folio_factura.setText(""+factura.getFolioExterno());
                    if(factura.getNombreDocumento()!=null)
                    {
                        t_xml.setText(factura.getNombreDocumento()+".xml");
                        t_pdf.setText(factura.getNombreDocumento()+".pdf");
                    }
                    if(factura.getFFiscal()!=null)
                        this.t_uuid_factura.setText(factura.getFFiscal());
                    contenedor.setSelectedIndex(4);
                    if(this.user.getAutorizarFactura()==true)
                        permiso=true;
                    else
                        permiso=false;
                    habilita(false, true); 
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
                permiso=false;
                this.habilita(false, false);
                JOptionPane.showMessageDialog(null, "Error al consultar la Factura");
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
        c_metodo_pago.setSelectedIndex(20);
        t_cuenta_pago.setText("");
        t_tipo_cambio.setText("1.0000");
        t_tipo_cambio.setValue(1.0d);
        t_datos.setModel(ModeloTablaReporte(0, columnas));
        formatoTabla();
        //Addenda
        t_inciso.setText("");
        t_numero.setText("");
        t_siniestro.setText("");
        t_deducible.setText("0.00");
        t_deducible.setValue(0.00d);
        t_nombre_emisor.setText("");
        t_correo_emisor.setText("");
        t_tel_emisor.setText("");
        t_nombre_receptor.setText("");
        t_correo_receptor.setText("");
        t_tel_receptor.setText("");
        t_modelo_vehiculo.setText("");
        t_marca_vehiculo.setText("");
        t_anio_vehiculo.setText("");
        t_serie_vehiculo.setText("");
        t_placas_vehiculo.setText("");
        t_motor_vehiculo.setText("");
        t_condiciones.setText("");
        t_contratante.setText("");
        t_orden.setText("");
        t_extra.setText("");
        
        t_serie_factura.setText("");
        t_folio_factura.setText("");
        t_uuid_factura.setText("");
        t_xml.setText("");
        t_pdf.setText("");
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
        c_metodo_pago.setEnabled(edo);
        t_cuenta_pago.setEnabled(edo);
        t_tipo_cambio.setEnabled(edo);
        c_moneda.setEnabled(edo);
        t_datos.setEnabled(edo);
        t_iva1.setEnabled(edo);
        b_mas.setEnabled(edo);
        b_menos.setEnabled(edo);
        //Addenda
        t_inciso.setEnabled(edo);
        t_numero.setEnabled(edo);
        t_siniestro.setEnabled(edo);
        t_deducible.setEnabled(edo);;
        t_nombre_emisor.setEnabled(edo);
        t_correo_emisor.setEnabled(edo);
        t_tel_emisor.setEnabled(edo);
        t_nombre_receptor.setEnabled(edo);
        t_correo_receptor.setEnabled(edo);
        t_tel_receptor.setEnabled(edo);
        //t_modelo_vehiculo.setEnabled(edo);
        //t_marca_vehiculo.setEnabled(edo);
        t_anio_vehiculo.setEnabled(edo);
        t_serie_vehiculo.setEnabled(edo);
        t_placas_vehiculo.setEnabled(edo);
        t_motor_vehiculo.setEnabled(edo);
        t_condiciones.setEnabled(edo);
        t_contratante.setEnabled(edo);
        t_descuento.setEnabled(edo);
        t_extra.setEnabled(edo);
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
                total = total.add(new BigDecimal(t_datos.getValueAt(ren, 7).toString()));
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
                    java.lang.String.class, 
                    java.lang.Double.class,
                    java.lang.Double.class,
                    java.lang.Double.class
                };
                boolean[] canEdit = new boolean [] {
                    false, true, true, true, true, true, true, false
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
                                                double suma=(((double)t_datos.getValueAt(row, 5))*((double)value));
                                                double desc=((double)t_datos.getValueAt(row, 6))/100;
                                                double total=suma-(suma*desc);
                                                t_datos.setValueAt(total, row, 7);
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
                                                    if(session.isOpen())
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
                                        Session session = null;
                                        try
                                        {
                                            if(value.toString().compareToIgnoreCase("")!=0)
                                            {
                                                session = HibernateUtil.getSessionFactory().openSession();
                                                session.beginTransaction().begin();
                                                ProductoServicio p_servicio = (ProductoServicio) session.createCriteria(ProductoServicio.class).add(Restrictions.eq("idProductoServicio", value.toString())).setMaxResults(1).uniqueResult();
                                                if(p_servicio!=null)
                                                {
                                                    Concepto con = (Concepto)session.get(Concepto.class, Integer.parseInt(t_datos.getValueAt(row, 0).toString()));
                                                    con.setProductoServicio(p_servicio);
                                                    session.update(con);
                                                    session.beginTransaction().commit();
                                                    
                                                    vector.setElementAt(value.toString(), col);
                                                    this.dataVector.setElementAt(vector, row);
                                                    fireTableCellUpdated(row, col);
                                                }
                                                else{
                                                    JOptionPane.showMessageDialog(null, "La clave no esta en el catalogo");
                                                }
                                            }
                                        }
                                        catch(Exception e)
                                        {
                                            e.printStackTrace();
                                            if(session.isOpen()==true)
                                                session.getTransaction().rollback();
                                        }
                                        if(session!=null)
                                            if(session.isOpen()==true)
                                            {
                                                session.flush();
                                                session.clear();
                                                session.close();
                                            }
                                    }
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
                                                double desc=((double)t_datos.getValueAt(row, 6))/100;
                                                double total=suma-(suma*desc);
                                                t_datos.setValueAt(total, row, 7);
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
                            case 6:
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
                                                double suma=(((double)t_datos.getValueAt(row, 5))*((double)t_datos.getValueAt(row, 1)));
                                                double desc=((double)value)/100;
                                                double total=suma-(suma*desc);
                                                t_datos.setValueAt(total, row, 7);
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
                      DefaultCellEditor editor2 = new DefaultCellEditor(medida);
                      column.setCellEditor(editor2); 
                      editor2.setClickCountToStart(2);
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
        
        for(int x=0; x<t_relacionados.getColumnModel().getColumnCount(); x++)
            t_relacionados.getColumnModel().getColumn(x).setHeaderRenderer(new Render1(c1));
        t_relacionados.setShowVerticalLines(true);
        t_relacionados.setShowHorizontalLines(true);
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
        int res=-1;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try
        {
            or=(Orden)session.get(Orden.class, or.getIdOrden());
            Factura[] fac=(Factura[])or.getFacturas().toArray(new Factura[0]);
            for(int r=0; r<fac.length; r++)
            {
                if(fac[r].getEstadoFactura().compareTo("Pendiente")==0 || fac[r].getEstadoFactura().compareTo("Facturado")==0)
                    res= fac[r].getIdFactura();
            }
            res= -1;
        }catch(Exception e)
        {
            System.out.println(e);
        }
        if(session!=null)
            if(session.isOpen())
                session.close();
        return res;
    }
    
    public boolean XML_MYSUITE(Orden ord, String nombre)
    {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try
        {
            error="";
            session.beginTransaction().begin();
            factura=(Nota)session.get(Nota.class, factura.getIdNota());
            Configuracion config=(Configuracion)session.get(Configuracion.class, 1);
            BigDecimal valorIva=new BigDecimal(""+t_iva1.getValue().toString());
            javaToXML miXML= new javaToXML();
            TFactDocMX documento=new TFactDocMX();
            documento.setVersion(BigInteger.valueOf(5));
                TFactDocMX.Identificacion id = new TFactDocMX.Identificacion();
                id.setCdgPaisEmisor(mysuite.TSenderCountryCode.MX);
                    id.setTipoDeComprobante(mysuite.TTipoDeDocumento.NOTA_DE_CREDITO);
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
                    renglon.setUnidadDeMedida(t_datos.getValueAt(ren, 2).toString().trim());
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
                        concepto_ext.setPrecioLista(precio_lista);
                            mysuite.TNonNegativeAmount importe_lista= new mysuite.TNonNegativeAmount();
                            importe_lista.setValue(big_total_lista.setScale(2, BigDecimal.ROUND_HALF_UP));
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
                                    desc1.setBase( importe_lista);
                                    desc1.setTasa(BigDecimal.valueOf( (double)t_datos.getValueAt(ren, 5) ).setScale(2, BigDecimal.ROUND_HALF_UP));
                                        mysuite.TNonNegativeAmount monto1 = new mysuite.TNonNegativeAmount();
                                        monto1.setValue( big_total_lista.multiply(big_porciento_dectuento).setScale(2, BigDecimal.ROUND_HALF_UP));
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
                        datos_del_negocio.setSucursal(config.getSucursal());
                comprobante_ex.setDatosDeNegocio(datos_del_negocio);
                TComprobanteEx.TerminosDePago terminos_de_pago= new TComprobanteEx.TerminosDePago();
                    terminos_de_pago.setMetodoDePago(c_metodo_pago.getSelectedItem().toString());
                    terminos_de_pago.setCondicionesDePago(t_condiciones.getText().trim());
                comprobante_ex.setTerminosDePago(terminos_de_pago);
                
                String linea1="";
                if(t_orden.getText().trim().compareTo("")!=0)
                    linea1+=" ORDEN: "+t_orden.getText().trim()+"        ";
                if(t_numero.getText().trim().compareTo("")!=0)
                    linea1+=" POLIZA: "+t_numero.getText().trim()+"        ";
                if(t_siniestro.getText().trim().compareTo("")!=0)
                    linea1+=" SINIESTRO: "+t_siniestro.getText().trim()+"        ";
                
                TTextoLibre cabecera= new TTextoLibre();
                    if(linea1.compareTo("")!=0)
                        cabecera.getTexto().add(linea1);
                    cabecera.getTexto().add("MARCA: "+t_marca_vehiculo.getText().trim()+"   SERIE: "+t_serie_vehiculo.getText().trim()+"    INSISO: "+t_inciso.getText().trim());
                    cabecera.getTexto().add("MODELO: "+t_modelo_vehiculo.getText().trim()+" PLACA: "+t_placas_vehiculo.getText().trim());
                    String texto="";
                    if(t_contratante.getText().trim().compareTo("")!=0)
                        texto=" ASEGURADO: "+t_contratante.getText().trim()+"     ";
                    if(t_extra.getText().compareTo("")!=0)
                        texto+=" "+t_extra.getText().trim();
                    if(texto.compareTo("")!=0)
                        cabecera.getTexto().add(texto);
                comprobante_ex.setTextosDeCabecera(cabecera);
                //MAPFRE
           documento.setComprobanteEx(comprobante_ex);

            if(miXML.creaAndValidaXML(documento, nombre)==true)
                return true;
            else
            {
                error=miXML.error;
                return false;
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return false;
        }
        finally
        {
            if(session!=null)
                if(session.isOpen())
                    session.close();
        }
    }
    
    /*public void llamarSoapCancela(RequestTransaction rq)
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
                                        resp.setEstatus("CANCELADO");
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
                                    JOptionPane.showMessageDialog(null, "El UUID ya esta Cancelado");
                                }catch(Exception e)
                                {
                                    session.beginTransaction().rollback();
                                    e.printStackTrace();
                                    habilita2(true);
                                    progreso.setString("Listo");
                                    progreso.setIndeterminate(false);
                                    JOptionPane.showMessageDialog(null, "El UUID ya esta Cancelado, pero no se pudo almacenar en la base de datos");
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
                                                    resp.setEstatus("CANCELADO");
                                                    session.update(resp);
                                                    session.beginTransaction().commit();
                                                }
                                                habilita2(true);
                                                progreso.setString("Listo");
                                                progreso.setIndeterminate(false);
                                                JOptionPane.showMessageDialog(null, "El UUID ya esta Cancelado");
                                            }catch(Exception e)
                                            {
                                                session.beginTransaction().rollback();
                                                e.printStackTrace();
                                                habilita2(true);
                                                progreso.setString("Listo");
                                                progreso.setIndeterminate(false);
                                                JOptionPane.showMessageDialog(null, "El UUID ya esta Cancelado, pero no se pudo almacenar en la base de datos");
                                            }
                                            finally
                                            {
                                                if(session!=null)
                                                    if(session.isOpen())
                                                        session.close();
                                            }
                                            break;
                                        case "202"://UUID Previamente cancelado
                                            Session session1 = HibernateUtil.getSessionFactory().openSession();
                                            try 
                                            {
                                                session1.beginTransaction().begin();
                                                Factura resp=(Factura) session1.createCriteria(Factura.class).add(Restrictions.eq("FFiscal", idBuscar)).uniqueResult();
                                                if(resp!=null)
                                                {
                                                    resp.setOrden(null);
                                                    resp.setEstadoFactura("Cancelado");
                                                    resp.setEstatus("CANCELADO");
                                                    session1.update(resp);
                                                    session1.beginTransaction().commit();
                                                }
                                                habilita2(true);
                                                progreso.setString("Listo");
                                                progreso.setIndeterminate(false);
                                                JOptionPane.showMessageDialog(null, "El UUID ya esta Cancelado");
                                            }catch(Exception e)
                                            {
                                                session1.beginTransaction().rollback();
                                                e.printStackTrace();
                                                habilita2(true);
                                                progreso.setString("Listo");
                                                progreso.setIndeterminate(false);
                                                JOptionPane.showMessageDialog(null, "El UUID ya esta Cancelado, pero no se pudo almacenar en la base de datos");
                                            }
                                            finally
                                            {
                                                if(session1!=null)
                                                    if(session1.isOpen())
                                                        session1.close();
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
                                            JOptionPane.showMessageDialog(null, "error al cancelar en sat");
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
                            System.out.println("Error en la conexion con el SAP:"+ex);
                            habilita2(true);
                            progreso.setString("Listo");
                            progreso.setIndeterminate(false);
                            JOptionPane.showMessageDialog(null, "Error en la conexion con el SAP:"+ex);
                        }
                    }
                };
                java.util.concurrent.Future<? extends java.lang.Object> result = port.requestTransactionAsync(requestor, transaction, country, entity, userSAP, userName, data1, data2, data3, asyncHandler);
            }
            else
            {
                habilita2(true);
                progreso.setIndeterminate(false);
                progreso.setString("Listo");
            }
        } catch (Exception ex) 
        {
            System.out.println("Error en la conexion con el SAP:"+ex);
            habilita2(true);
            progreso.setString("Listo");
            progreso.setIndeterminate(false);
            JOptionPane.showMessageDialog(null, "Error en la conexion con el SAP:"+ex);
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
                                        factura=(Nota)session.get(Nota.class, factura.getIdNota());

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
                                            JOptionPane.showMessageDialog(null, "Error al abrir el archivo xml");
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
                            System.out.println("Error en la conexion con el SAP:"+ex);
                        }
                    }
                };
                java.util.concurrent.Future<? extends java.lang.Object> result = port.requestTransactionAsync(requestor, transaction, country, entity, userSAP, userName, data1, data2, data3, asyncHandler);
            }
        } catch (Exception ex) 
        {
            System.out.println("Error en la conexion con el SAP:"+ex);
            JOptionPane.showMessageDialog(null, "Error en la conexion con el SAP:"+ex);
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
                                        factura=(Nota)session.get(Nota.class, factura.getIdNota());

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
                            System.out.println("Error en la conexion con el SAP:"+ex);
                            habilita2(true);
                            progreso.setString("Listo");
                            progreso.setIndeterminate(false);
                            JOptionPane.showMessageDialog(null, "Error en la conexion con el SAP:"+ex);
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
            System.out.println("Error en la conexion con el SAP:"+ex);
            habilita2(true);
            progreso.setString("Listo");
            progreso.setIndeterminate(false);
            JOptionPane.showMessageDialog(null, "Error en la conexion con el SAP:"+ex);
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
                                        factura=(Nota)session.get(Nota.class, factura.getIdNota());

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
                            JOptionPane.showMessageDialog(null, "Error en la conexion con el SAP:"+ex);
                        }
                    }
                };
                java.util.concurrent.Future<? extends java.lang.Object> result = port.requestTransactionAsync(requestor, transaction, country, entity, userSAP, userName, data1, data2, data3, asyncHandler);
            }
            else
            {
                habilita(true, false);
                progreso.setString("Listo");
                progreso.setIndeterminate(false);
            }
        } catch (Exception ex) 
        {
            System.out.println("Error en la conexion con el SAP:"+ex);
            habilita(true, false);
            progreso.setString("Listo");
            progreso.setIndeterminate(false);
            JOptionPane.showMessageDialog(null, "Error en la conexion con el SAP:"+ex);
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
                    Session session = HibernateUtil.getSessionFactory().openSession();
                    try
                    {
                        // TODO process asynchronous response here                        
                        session.beginTransaction().begin();
                        factura=(Nota)session.get(Nota.class, factura.getIdNota());
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
                                            //optional, but recommended
                                            //read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
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
                                                javax.swing.JDialog consulta1 = new javax.swing.JDialog(GeneraNota.this, true);
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
                                            System.out.println("Se encontraron "+numero+" facturas pero no fue posible leer el xml "+e);
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
                                String cadenaCodificada=codificador.EncodeArchivo(new File(ruta+"nativos/N"+numeroID+"nativo.xml"));
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
                                    rq1.setData1(cadenaCodificada);//XML del CFDI a timbrar (no puede contener addendas ni timbre, y CFDI codificado en base 64).
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
                    finally
                    {
                        if(session!=null)
                            if(session.isOpen())
                                session.close();
                    }
                }
            };
            java.util.concurrent.Future<? extends java.lang.Object> result = port.requestTransactionAsync(requestor, transaction, country, entity, userSAP, userName, data1, data2, data3, asyncHandler);
        }
        else
        {
            habilita(true, false);
            progreso.setString("Listo");
            progreso.setIndeterminate(false);
        }
        return true;
    }
    */
    private void doClose(Nota o) {
        returnStatus = o;
        setVisible(false);
        //dispose();
    }
    
    public Nota getReturnStatus() {
        return returnStatus;
    }
    
    public void facturaElectronica()
    {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try{
            session.beginTransaction().begin();
            factura=(Nota)session.get(Nota.class, factura.getIdNota());
            Configuracion config=(Configuracion)session.createCriteria(Configuracion.class).add(Restrictions.eq("empresa", factura.getNombreEmisor())).setMaxResults(1).uniqueResult();
            String serie=config.getSerie1();
            double version=config.getVersion();
            int numeroID=0;
            if(factura.getOrden()!=null)
                numeroID=factura.getOrden().getIdOrden();
            if(factura.getOrdenExterna()!=null)
                numeroID=factura.getOrdenExterna().getIdOrden();
            
            switch(config.getPac())
            {
                case "MYSUITE":
                    if(XML_MYSUITE(factura.getOrden(), ruta+"nativos/N"+numeroID+"nativo.xml")==true)
                    {   
                        ArrayList aux=new ArrayList();
                        aux.add(config.getRequestor());
                        aux.add("LOOKUP_ISSUED_INTERNAL_ID");
                        aux.add("MX");
                        aux.add(config.getRequestor());
                        aux.add(config.getUsuario_1());
                        aux.add(config.getRfc());
                        aux.add(t_codigo.getText());
                        aux.add(config.getSucursal());
                        
                        ApiMysuite timbrar=new ApiMysuite(ruta);
                        DefaultTableModel modelo=ModeloTablaFactura(0, columnas1);
                        ArrayList resp=timbrar.llamarSoapConsulta(aux,modelo);
                        if(resp.size()>0)
                        {
                            switch(resp.get(0).toString())
                            {
                                case "-1"://Error
                                    habilita(true, false);
                                    progreso.setString("Listo");
                                    progreso.setIndeterminate(false);
                                    JOptionPane.showMessageDialog(null, resp.get(1).toString());
                                    break;
                                case "0"://No se encontro la factura
                                    try 
                                    {
                                        CodeBase64 codificador=new CodeBase64();
                                        String cadenaCodificada=codificador.EncodeArchivo(new File(ruta+"nativos/N"+numeroID+"nativo.xml"));
                                        if(cadenaCodificada.compareTo("")!=0)
                                        {
                                            aux=new ArrayList();
                                            aux.add(config.getRequestor());//Lo proporcionará MySuite
                                            aux.add("CONVERT_NATIVE_XML");//Tipo de Transaccion
                                            aux.add("MX");//Codigo de pais
                                            aux.add(config.getRequestor());//igual que Requestor
                                            aux.add(config.getUsuario_1());//Country.Entity.nombre_usuario
                                            aux.add(config.getRfc());
                                            aux.add(cadenaCodificada);//XML del CFDI a timbrar (no puede contener addendas ni timbre, y CFDI codificado en base 64).
                                            aux.add("PDF XML");//Documentos Requeridos

                                            ArrayList guarda=timbrar.llamarSoapTimbra(aux);
                                            factura = (Nota)session.get(Nota.class, factura.getIdNota());
                                            
                                            if(guarda.get(0).toString().compareTo("-1")!=0 && guarda.get(0).toString().compareTo("0")!=0)
                                            {
                                                //Datos de timbrado
                                                factura.setEstadoFactura(guarda.get(1).toString());
                                                factura.setEstatus(guarda.get(2).toString());
                                                factura.setFFiscal(guarda.get(3).toString());
                                                factura.setFechaFiscal(guarda.get(4).toString());
                                                factura.setSerie(guarda.get(5).toString());
                                                factura.setFolio(guarda.get(6).toString());
                                                factura.setNombreDocumento(guarda.get(7).toString());
                                                factura.setPac("M");
                                                session.update(factura);
                                                session.beginTransaction().commit();
                                                session.beginTransaction().commit();

                                                habilita(true, false);
                                                progreso.setString("Listo");
                                                progreso.setIndeterminate(false);
                                                if(session.isOpen())
                                                    session.close();
                                                guarda();
                                                consulta();
                                            }
                                            else{
                                                habilita(true, false);
                                                progreso.setString("Listo");
                                                progreso.setIndeterminate(false);
                                                JOptionPane.showMessageDialog(null, aux.get(1));
                                            }
                                        }
                                        else
                                        {
                                            habilita(true, false);
                                            progreso.setString("Listo");
                                            progreso.setIndeterminate(false);
                                            JOptionPane.showMessageDialog(null, "Error al codificar la cadena a Base 64");
                                        }
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
                                    
                                    
                                    
                                    break;
                                case "1"://se encontro una sola factura
                                    modelo=(DefaultTableModel)resp.get(1);
                                    t_facturas.setModel(modelo);
                                    factura=(Nota)session.get(Nota.class, factura.getIdNota());
                                    factura.setSerie(t_facturas.getValueAt(0, 3).toString());
                                    factura.setFolio(t_facturas.getValueAt(0, 4).toString());
                                    factura.setFFiscal(t_facturas.getValueAt(0, 1).toString());
                                    factura.setFechaFiscal(t_facturas.getValueAt(0, 5).toString());
                                    factura.setNombreDocumento(t_facturas.getValueAt(0, 0).toString()+"_"+t_facturas.getValueAt(0, 3).toString()+"_"+t_facturas.getValueAt(0, 4).toString()+"_"+t_rfc.getText());
                                    factura.setEstadoFactura("Facturado");
                                    factura.setEstatus("POR COBRAR");
                                    factura.setPac("M");
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
                                    break;
                                default:// se encontro mas de una factura
                                    modelo=(DefaultTableModel)resp.get(1);
                                    t_facturas.setModel(modelo);
                                    habilita(true, false);
                                    progreso.setString("Listo");
                                    progreso.setIndeterminate(false);
                                    formatoTablafactura();
                                    javax.swing.JDialog consulta1 = new javax.swing.JDialog(GeneraNota.this, true);
                                    consulta1.setModalityType(java.awt.Dialog.ModalityType.DOCUMENT_MODAL);
                                    consulta1.getContentPane().add(jLabel43, java.awt.BorderLayout.PAGE_START);
                                    consulta1.getContentPane().add(jScrollPane2, java.awt.BorderLayout.CENTER);
                                    consulta1.getContentPane().add(jPanel15, java.awt.BorderLayout.PAGE_END);
                                    consulta1.setSize(861, 326);
                                    Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
                                    consulta1.setLocation((d.width/2)-(consulta1.getWidth()/2), (d.height/2)-(consulta1.getHeight()/2));
                                    consulta1.setVisible(true);
                                    break;
                            }
                        }
                        else
                            JOptionPane.showMessageDialog(null, "Error al obtener respuesta");
                    }
                    else
                    {
                        habilita(true, false);
                        progreso.setString("Listo");
                        progreso.setIndeterminate(false);
                        JOptionPane.showMessageDialog(null, "Error al generar el XML.-"+error);
                    }
                    bandera=true;
                    break;
                    
                case "FINKOK":
                    boolean todos=true;
                    if(factura.getVersion()>3.2)
                    {
                        for(int m=0; m<t_datos.getRowCount(); m++)
                        {
                            if(t_datos.getValueAt(m, 4)!=null)
                            {
                                if(t_datos.getValueAt(m, 4).toString().compareTo("")==0)
                                    todos=false;
                            }
                            else
                                todos=false;
                        }
                    }
                    if(todos==true)
                    {
                        String folio="1";
                        if(t_codigo.getText().compareTo("")!=0)
                        {
                            Query maximo = session.createSQLQuery("select if(max(folio_externo) is null, 1, max(folio_externo)+1) as folio from nota where PAC='F' and rfc_emisor='"+factura.getRfcEmisor()+"' and serie_externo='"+serie+"';");
                            maximo.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
                            ArrayList list_maximo=(ArrayList)maximo.list();
                            if(list_maximo.size()>0)
                            {
                                java.util.HashMap map=(java.util.HashMap)list_maximo.get(0);
                                folio = map.get("folio").toString();
                            }
                        }
                        else{
                            folio=t_codigo.getText();
                        }
                        if(XML_FINKOK(factura.getOrden(), folio, serie, version)==true)
                        {
                            GeneradorSelloDigital generadorsello = new GeneradorSelloDigital();
                            if(config.getCer()!=null && config.getCer().compareTo("")!=0)
                            {
                                if(config.getClave()!=null && config.getClave().compareTo("")!=0)
                                {
                                    String cadena="";
                                    String[] certificado=generadorsello.getCertificado(ruta+"config/"+config.getCer());
                                    if(certificado[0].compareTo("1")==0)
                                    {
                                        if(version==3.2d)
                                        {
                                            comprobante.setNoCertificado(certificado[2]);
                                            comprobante.setCertificado(certificado[3]);
                                        }
                                        else
                                        {
                                            comprobante33.setNoCertificado(certificado[2]);
                                            comprobante33.setCertificado(certificado[3]);
                                        }
                                        String[] getSello;
                                        if(version==3.2d)
                                        {
                                            cadena = comprobante.getCadenaOriginal();
                                            getSello = generadorsello.getSelloDigital(cadena, config.getClave(), ruta+"config/"+config.getLlave());
                                        }
                                        else
                                        {
                                            timbrar.CadenaOriginal33 cOriginal= new timbrar.CadenaOriginal33(comprobante33); 
                                            cadena = cOriginal.getCadena();
                                            getSello = generadorsello.getSelloDigital33(cadena, config.getClave(), ruta+"config/"+config.getLlave());
                                        }
                                        if(getSello[0].compareTo("1")==0)
                                        {
                                            String sello=getSello[1];
                                            FinkokJavaToXML xml= new FinkokJavaToXML();
                                            boolean genero=false;
                                            if(version==3.2d)
                                            {
                                                comprobante.setSello(sello);
                                                genero = xml.creaAndValidaXML(comprobante, ruta+"nativos/"+numeroID+"nativo.xml");
                                            }
                                            else
                                            {
                                                comprobante33.setSello(sello);
                                                genero = xml.convierteXML(comprobante33, ruta+"nativos/"+numeroID+"nativo.xml");
                                            }

                                            if(genero==true)
                                            {
                                                ApiFinkok api1=new ApiFinkok(ruta);
                                                ArrayList datos=new ArrayList();
                                                datos.add(config.getEmailFinkok());
                                                datos.add(config.getClaveFinkok());
                                                datos.add(numeroID+"nativo.xml");//nombre del archivo nativo
                                                datos.add(factura.getRfcEmisor()+"_"+serie+"_"+folio+"_"+t_rfc.getText()+".xml");//nombre del archivo timbrado

                                                ArrayList guarda=api1.llamarSoapFimbra(datos);
                                                switch(guarda.get(0).toString())
                                                {
                                                    case "1"://Se timbro correcto
                                                        Nota factura1=(Nota)session.get(Nota.class, factura.getIdNota());
                                                        factura1.setFecha(fecha_factura);
                                                        factura1.setEstadoFactura(guarda.get(1).toString());
                                                        factura1.setEstatus(guarda.get(2).toString());
                                                        factura1.setFFiscal(guarda.get(3).toString());
                                                        factura1.setFechaFiscal(guarda.get(4).toString());
                                                        factura1.setSerieExterno(serie);
                                                        factura1.setFolioExterno(Integer.parseInt(folio));
                                                        factura1.setNombreDocumento(factura.getRfcEmisor()+"_"+serie+"_"+folio+"_"+t_rfc.getText());
                                                        factura1.setPac("F");
                                                        factura1.setCertificadoEmisor(certificado[2]);
                                                        factura1.setCertificadoSat(guarda.get(9).toString());
                                                        factura1.setSelloSat(guarda.get(8).toString());
                                                        factura1.setSelloCfdi(sello);
                                                        factura1.setAddenda("general");

                                                        session.update(factura1);
                                                        session.beginTransaction().commit();
                                                        session.beginTransaction().commit();

                                                        if(session.isOpen())
                                                            session.close();
                                                        Formatos formato_pdf = new Formatos(this.user, this.sessionPrograma, factura1,configuracion);
                                                        formato_pdf.nota();
                                                        habilita(true, false);
                                                        progreso.setString("Listo");
                                                        progreso.setIndeterminate(false);
                                                        guarda();
                                                        consulta();
                                                        break;

                                                    case "0"://Hay error local
                                                        ArrayList lista=(ArrayList)guarda.get(1);
                                                        String error="Error";
                                                        for(int x=0; x<lista.size(); x++)
                                                        {
                                                            ArrayList inc=(ArrayList)lista.get(x);
                                                            error+="Error: ";
                                                            for(int y=0; y<inc.size(); y++)
                                                            {
                                                                error+=lista.get(0)+" ";
                                                            }
                                                            error+="\n";
                                                        }
                                                        habilita(true, false);
                                                        progreso.setString("Listo");
                                                        progreso.setIndeterminate(false);
                                                        JOptionPane.showMessageDialog(null, error);
                                                        break;

                                                    case "-1"://Hay error en el SAP
                                                        habilita(true, false);
                                                        progreso.setString("Listo");
                                                        progreso.setIndeterminate(false);
                                                        JOptionPane.showMessageDialog(null, guarda.get(1).toString());
                                                        break;
                                                }
                                            }
                                            else
                                            {
                                                habilita(true, false);
                                                progreso.setString("Listo");
                                                progreso.setIndeterminate(false);
                                                JOptionPane.showMessageDialog(null, "Error al generar el XML.-"+error);
                                            }
                                        }
                                        else
                                        {
                                            habilita(true, false);
                                            progreso.setString("Listo");
                                            progreso.setIndeterminate(false);
                                            JOptionPane.showMessageDialog(null, getSello[1]);
                                        }
                                    }
                                    else
                                    {
                                        habilita(true, false);
                                        progreso.setString("Listo");
                                        progreso.setIndeterminate(false);
                                        JOptionPane.showMessageDialog(null, certificado[1]);
                                    }
                                }
                                else
                                {
                                    habilita(true, false);
                                    progreso.setString("Listo");
                                    progreso.setIndeterminate(false);
                                    JOptionPane.showMessageDialog(null, "Falta la clave del Archivo CER");
                                }
                            }
                            else
                            {
                                habilita(true, false);
                                progreso.setString("Listo");
                                progreso.setIndeterminate(false);
                                JOptionPane.showMessageDialog(null, "Falta agrerar el archivo CER");
                            }
                        }
                        else
                        {
                            habilita(true, false);
                            progreso.setString("Listo");
                            progreso.setIndeterminate(false);
                            JOptionPane.showMessageDialog(null, "Error al generar el XML.-"+error);
                        }
                    }
                    else
                    {
                        habilita(true, false);
                        progreso.setString("Listo");
                        progreso.setIndeterminate(false);
                        JOptionPane.showMessageDialog(null, "Falta la clave de Producto a lagunos conceptos");
                    }
                    bandera=true;
                    break;
                    
                case "TURBO":
                    boolean todos1=true;
                    if(factura.getVersion()>3.2)
                    {
                        for(int m=0; m<t_datos.getRowCount(); m++)
                        {
                            if(t_datos.getValueAt(m, 4)!=null)
                            {
                                if(t_datos.getValueAt(m, 4).toString().compareTo("")==0)
                                    todos1=false;
                            }
                            else
                                todos1=false;
                        }
                    }
                    if(todos1==true)
                    {
                        String folio="1";
                        if(t_codigo.getText().compareTo("")!=0)
                        {
                            Query maximo = session.createSQLQuery("select if(max(folio_externo) is null, 1, max(folio_externo)+1) as folio from nota where PAC='T' and rfc_emisor='"+factura.getRfcEmisor()+"' and serie_externo='"+serie+"';");
                            maximo.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
                            ArrayList list_maximo=(ArrayList)maximo.list();
                            if(list_maximo.size()>0)
                            {
                                java.util.HashMap map=(java.util.HashMap)list_maximo.get(0);
                                folio = map.get("folio").toString();
                            }
                        }
                        else{
                            folio=t_codigo.getText();
                        }
                        if(XML_FINKOK(factura.getOrden(), folio, serie, version)==true)
                        {
                            GeneradorSelloDigital generadorsello = new GeneradorSelloDigital();
                            if(config.getCer()!=null && config.getCer().compareTo("")!=0)
                            {
                                if(config.getClave()!=null && config.getClave().compareTo("")!=0)
                                {
                                    String cadena="";
                                    String[] certificado=generadorsello.getCertificado(ruta+"config/"+config.getCer());
                                    if(certificado[0].compareTo("1")==0)
                                    {
                                        if(version==3.2d)
                                        {
                                            comprobante.setNoCertificado(certificado[2]);
                                            comprobante.setCertificado(certificado[3]);
                                        }
                                        else
                                        {
                                            comprobante33.setNoCertificado(certificado[2]);
                                            comprobante33.setCertificado(certificado[3]);
                                        }
                                        String[] getSello;
                                        if(version==3.2d)
                                        {
                                            cadena = comprobante.getCadenaOriginal();
                                            getSello = generadorsello.getSelloDigital(cadena, config.getClave(), ruta+"config/"+config.getLlave());
                                        }
                                        else
                                        {
                                            timbrar.CadenaOriginal33 cOriginal= new timbrar.CadenaOriginal33(comprobante33); 
                                            cadena = cOriginal.getCadena();
                                            getSello = generadorsello.getSelloDigital33(cadena, config.getClave(), ruta+"config/"+config.getLlave());
                                        }
                                        if(getSello[0].compareTo("1")==0)
                                        {
                                            String sello=getSello[1];
                                            FinkokJavaToXML xml= new FinkokJavaToXML();
                                            boolean genero=false;
                                            if(version==3.2d)
                                            {
                                                comprobante.setSello(sello);
                                                genero = xml.creaAndValidaXML(comprobante, ruta+"nativos/"+numeroID+"nativo.xml");
                                            }
                                            else
                                            {
                                                comprobante33.setSello(sello);
                                                genero = xml.convierteXML(comprobante33, ruta+"nativos/"+numeroID+"nativo.xml");
                                            }

                                            if(genero==true)
                                            {
                                                ApiTurbo api1=new ApiTurbo(ruta);
                                                ArrayList datos=new ArrayList();
                                                datos.add(config.getEmailFinkok());
                                                datos.add(config.getClaveFinkok());
                                                datos.add(numeroID+"nativo.xml");//nombre del archivo nativo
                                                datos.add(factura.getRfcEmisor()+"_"+serie+"_"+folio+"_"+t_rfc.getText()+".xml");//nombre del archivo timbrado

                                                ArrayList guarda=api1.llamarSoapTimbre(datos);
                                                switch(guarda.get(0).toString())
                                                {
                                                    case "1"://Se timbro correcto
                                                        Nota factura1=(Nota)session.get(Nota.class, factura.getIdNota());
                                                        factura1.setFecha(fecha_factura);
                                                        factura1.setEstadoFactura(guarda.get(1).toString());
                                                        factura1.setEstatus(guarda.get(2).toString());
                                                        factura1.setFFiscal(guarda.get(3).toString());
                                                        factura1.setFechaFiscal(guarda.get(4).toString());
                                                        factura1.setSerieExterno(serie);
                                                        factura1.setFolioExterno(Integer.parseInt(folio));
                                                        factura1.setNombreDocumento(factura.getRfcEmisor()+"_"+serie+"_"+folio+"_"+t_rfc.getText());
                                                        factura1.setPac("T");
                                                        factura1.setCertificadoEmisor(certificado[2]);
                                                        factura1.setCertificadoSat(guarda.get(9).toString());
                                                        factura1.setSelloSat(guarda.get(8).toString());
                                                        factura1.setSelloCfdi(sello);
                                                        factura1.setAddenda("general");

                                                        session.update(factura1);
                                                        session.beginTransaction().commit();
                                                        session.beginTransaction().commit();

                                                        if(session.isOpen())
                                                            session.close();
                                                        Formatos formato_pdf = new Formatos(this.user, this.sessionPrograma, factura1,configuracion);
                                                        formato_pdf.nota();
                                                        habilita(true, false);
                                                        progreso.setString("Listo");
                                                        progreso.setIndeterminate(false);
                                                        guarda();
                                                        consulta();
                                                        break;

                                                    case "0"://Hay error local
                                                         ArrayList lista=(ArrayList)guarda.get(1);
                                                        String error="Error:";
                                                        for(int x=0; x<lista.size(); x++)
                                                        {
                                                            error+=lista.get(x).toString()+" ";
                                                            error+="\n";
                                                        }
                                                        habilita(true, false);
                                                        progreso.setString("Listo");
                                                        progreso.setIndeterminate(false);
                                                        JOptionPane.showMessageDialog(null, error);
                                                        break;

                                                    case "-1"://Hay error en el SAP
                                                        habilita(true, false);
                                                        progreso.setString("Listo");
                                                        progreso.setIndeterminate(false);
                                                        JOptionPane.showMessageDialog(null, guarda.get(1).toString());
                                                        break;
                                                }
                                            }
                                            else
                                            {
                                                habilita(true, false);
                                                progreso.setString("Listo");
                                                progreso.setIndeterminate(false);
                                                JOptionPane.showMessageDialog(null, "Error al generar el XML.-"+error);
                                            }
                                        }
                                        else
                                        {
                                            habilita(true, false);
                                            progreso.setString("Listo");
                                            progreso.setIndeterminate(false);
                                            JOptionPane.showMessageDialog(null, getSello[1]);
                                        }
                                    }
                                    else
                                    {
                                        habilita(true, false);
                                        progreso.setString("Listo");
                                        progreso.setIndeterminate(false);
                                        JOptionPane.showMessageDialog(null, certificado[1]);
                                    }
                                }
                                else
                                {
                                    habilita(true, false);
                                    progreso.setString("Listo");
                                    progreso.setIndeterminate(false);
                                    JOptionPane.showMessageDialog(null, "Falta la clave del Archivo CER");
                                }
                            }
                            else
                            {
                                habilita(true, false);
                                progreso.setString("Listo");
                                progreso.setIndeterminate(false);
                                JOptionPane.showMessageDialog(null, "Falta agrerar el archivo CER");
                            }
                        }
                        else
                        {
                            habilita(true, false);
                            progreso.setString("Listo");
                            progreso.setIndeterminate(false);
                            JOptionPane.showMessageDialog(null, "Error al generar el XML.-"+error);
                        }
                    }
                    else
                    {
                        habilita(true, false);
                        progreso.setString("Listo");
                        progreso.setIndeterminate(false);
                        JOptionPane.showMessageDialog(null, "Falta la clave de Producto a lagunos conceptos");
                    }
                    bandera=true;
                    break;
            }
        }
        catch(Exception e)
        {
            bandera=true;
            habilita(true, false);
            progreso.setString("Listo");
            progreso.setIndeterminate(false);
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al Abrir el archivo de configuración..");
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
            factura = (Nota)session.get(Nota.class, factura.getIdNota());

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
            factura.setMetodoPago(c_metodo_pago.getSelectedItem().toString());
            factura.setCuentaPago(t_cuenta_pago.getText().trim());
            factura.setFactorCambio(((Number)t_tipo_cambio.getValue()).doubleValue());
            if(c_moneda.getSelectedIndex()!=-1)
                factura.setMoneda(c_moneda.getSelectedItem().toString());

            //Addenda  
            factura.setContactoEmisor(t_nombre_emisor.getText().trim());
            factura.setCorreoEmisor(t_correo_emisor.getText().trim());
            factura.setTelefonoEmisor(t_tel_emisor.getText().trim());
            factura.setContactoReceptor(t_nombre_receptor.getText().trim());
            factura.setCorreoReceptor(t_correo_receptor.getText().trim());
            factura.setTelefonoReceptor(t_tel_receptor.getText().trim());
            factura.setCondicionesPago(c_forma_pago.getSelectedItem().toString());
            factura.setAddenda("general");
            factura.setIva(Integer.parseInt(t_iva1.getValue().toString()));
            factura.setExtra(t_extra.getText());
            UsoCfdi uso=(UsoCfdi)session.get(UsoCfdi.class, t_id_cfdi.getText());
            if(uso!=null)
            {
                factura.setUsoCfdi(uso);
            }
            session.update(factura);
            if(factura.getOrden()!=null)
            {
                Orden ord=(Orden)session.get(Orden.class, factura.getOrden().getIdOrden());
                ord.setPoliza(t_numero.getText().trim());
                ord.setSiniestro(t_siniestro.getText().trim());
                ord.setDeducible(((Number)t_deducible.getValue()).doubleValue());
                ord.setModelo(Integer.parseInt(t_anio_vehiculo.getText()));
                ord.setNoSerie(t_serie_vehiculo.getText().trim());
                ord.setNoPlacas(t_placas_vehiculo.getText().trim());
                ord.setNoMotor(t_motor_vehiculo.getText().trim());
                ord.setInciso(t_inciso.getText().trim());
                session.update(ord);
            }
            if(factura.getOrdenExterna()!=null)
            {
                OrdenExterna ordE=(OrdenExterna)session.get(OrdenExterna.class, factura.getOrdenExterna().getIdOrden());
                ordE.setPoliza(t_numero.getText().trim());
                ordE.setSiniestro(t_siniestro.getText().trim());
                ordE.setDeducible(((Number)t_deducible.getValue()).doubleValue());
                if(t_anio_vehiculo.getText().trim().compareTo("")!=0)
                    ordE.setModelo(Integer.parseInt(t_anio_vehiculo.getText()));
                ordE.setNoSerie(t_serie_vehiculo.getText().trim());
                ordE.setNoPlacas(t_placas_vehiculo.getText().trim());
                ordE.setNoMotor(t_motor_vehiculo.getText().trim());
                ordE.setInciso(t_inciso.getText().trim());
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
    
    public boolean XML_FINKOK(Orden ord, String folio, String serie, double version)
    {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try
        {
            error="";
            GregorianCalendar GC = new GregorianCalendar();
            GC.setTime(fecha_factura);
            
            session.beginTransaction().begin();
            factura=(Nota)session.get(Nota.class, factura.getIdNota());
            //Configuracion config=(Configuracion)session.get(Configuracion.class, 2);
            BigDecimal valorIva=new BigDecimal(""+t_iva1.getValue().toString());
            
            //************************ V3.2
            if(version==3.2d)
            {
                finkok.ObjectFactory objeto = new finkok.ObjectFactory();
                comprobante = objeto.createComprobante();

                comprobante.setVersion(Constantes.VERSION_COMPROBANTE_TRES);
                comprobante.setFecha(DatatypeFactory.newInstance().newXMLGregorianCalendar(GC));
                comprobante.setSello(null);//Falta
                comprobante.setFormaDePago(c_forma_pago.getSelectedItem().toString());
                comprobante.setNoCertificado(null);//Falta
                comprobante.setCertificado(null);//Falta
                comprobante.setCondicionesDePago(c_forma_pago.getSelectedItem().toString());
                comprobante.setTipoCambio(BigDecimal.valueOf(((Number)t_tipo_cambio.getValue()).doubleValue() ).setScale(3, BigDecimal.ROUND_HALF_UP).toString());
                comprobante.setMoneda(c_moneda.getSelectedItem().toString());
                comprobante.setTipoDeComprobante("egreso");
                comprobante.setMetodoDePago(c_metodo_pago.getSelectedItem().toString());
                comprobante.setLugarExpedicion("TOLUCA, ESTADO DE MEXICO");
                comprobante.setSerie(serie);
                comprobante.setFolio(folio);//Falta
                    Emisor emisor= objeto.createComprobanteEmisor();
                    emisor.setRfc(factura.getRfcEmisor());//Cambiar por config.getRfc()
                    emisor.setNombre(acentos(factura.getNombreEmisor()));
                        TUbicacionFiscal miUbicacion =objeto.createTUbicacionFiscal();
                        miUbicacion.setCalle(acentos(factura.getCalleEmisor()));
                        miUbicacion.setNoExterior(factura.getNumeroExteriorEmisor());
                        miUbicacion.setColonia(acentos(factura.getColoniaEmisor()));
                        miUbicacion.setMunicipio(acentos(factura.getMunicipioEmisor()));
                        miUbicacion.setEstado(acentos(factura.getEstadoEmisor()));
                        miUbicacion.setPais(acentos(factura.getPaisEmisor()));
                        miUbicacion.setCodigoPostal(factura.getCpEmisor());
                        RegimenFiscal miRegimen= objeto.createComprobanteEmisorRegimenFiscal();
                        miRegimen.setRegimen("REGIMEN GENERAL DE LEY DE PERSONAS MORALES");
                    emisor.getRegimenFiscal().add(miRegimen);
                    emisor.setDomicilioFiscal(miUbicacion);
                comprobante.setEmisor(emisor);
                    Receptor receptor=objeto.createComprobanteReceptor();
                    receptor.setRfc(t_rfc.getText());
                    receptor.setNombre(acentos(t_social.getText()));
                        TUbicacion sUbicacion = objeto.createTUbicacion();
                        sUbicacion.setCalle(acentos(t_calle.getText()));
                        sUbicacion.setColonia(acentos(t_colonia.getText()));
                        sUbicacion.setMunicipio(acentos(t_municipio.getText()));
                        sUbicacion.setEstado(acentos(c_estado.getSelectedItem().toString()));
                        sUbicacion.setPais(c_pais.getSelectedItem().toString());
                        sUbicacion.setCodigoPostal(t_cp.getText());
                    receptor.setDomicilio(sUbicacion);
                comprobante.setReceptor(receptor);
                    finkok.Comprobante.Conceptos misConceptos = objeto.createComprobanteConceptos();
                        BigDecimal big_total_bruto= new BigDecimal("0.0");
                        BigDecimal big_sub_total=new BigDecimal("0.0");

                        for(int ren=0; ren<t_datos.getRowCount(); ren++)
                        {
                            //descuento
                            BigDecimal big_descuento=new BigDecimal(t_datos.getValueAt(ren, 6).toString());
                            BigDecimal big_porciento_dectuento=big_descuento.divide(new BigDecimal("100"));
                            //cantidades de lista
                            BigDecimal big_cantidad=new BigDecimal(t_datos.getValueAt(ren, 1).toString());
                            BigDecimal big_precio_lista=new BigDecimal(t_datos.getValueAt(ren, 5).toString());
                            BigDecimal big_total_lista=big_precio_lista.multiply(big_cantidad);
                            big_total_bruto = big_total_bruto.add(big_total_lista);
                            //cantidades netas
                            BigDecimal big_precio_neto=big_precio_lista.subtract(big_precio_lista.multiply(big_porciento_dectuento));
                            BigDecimal big_total_neto=big_precio_neto.multiply(big_cantidad);
                            big_sub_total = big_sub_total.add(big_total_neto);

                            finkok.Comprobante.Conceptos.Concepto renglon= objeto.createComprobanteConceptosConcepto();
                            renglon.setCantidad(big_cantidad.setScale(2, BigDecimal.ROUND_HALF_UP));
                            renglon.setUnidad(t_datos.getValueAt(ren, 2).toString().trim());
                            renglon.setDescripcion(t_datos.getValueAt(ren, 3).toString().trim());

                            renglon.setValorUnitario(big_precio_lista.setScale(2, BigDecimal.ROUND_HALF_UP));
                            renglon.setImporte(big_total_neto.setScale(2, BigDecimal.ROUND_HALF_UP));
                            misConceptos.getConcepto().add(renglon);
                        }

                comprobante.setConceptos(misConceptos);
                    Impuestos impuestos =objeto.createComprobanteImpuestos();
                    BigDecimal porc=valorIva.divide(new BigDecimal("100.0")).setScale(2, BigDecimal.ROUND_HALF_UP);
                    BigDecimal monto=big_sub_total.multiply(porc).setScale(2, BigDecimal.ROUND_HALF_UP);
                    impuestos.setTotalImpuestosTrasladados(monto.setScale(2, BigDecimal.ROUND_HALF_UP));
                        Traslados misTrasladados=objeto.createComprobanteImpuestosTraslados();
                            Traslado traslado= objeto.createComprobanteImpuestosTrasladosTraslado();
                            traslado.setImpuesto("IVA");
                            traslado.setTasa(valorIva.setScale(0, BigDecimal.ROUND_HALF_UP));
                            traslado.setImporte(monto.setScale(6, BigDecimal.ROUND_HALF_UP));
                        misTrasladados.getTraslado().add(traslado);
                    impuestos.setTraslados(misTrasladados);
                comprobante.setImpuestos(impuestos);

                comprobante.setSubTotal(big_total_bruto.setScale(2, BigDecimal.ROUND_HALF_UP));
                BigDecimal big_descuento=big_total_bruto.subtract(big_sub_total);
                if(big_descuento.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue()>0)
                {
                    comprobante.setDescuento(big_descuento.setScale(2, BigDecimal.ROUND_HALF_UP));//Falta
                    comprobante.setMotivoDescuento("PRONTO PAGO");
                }
                comprobante.setTotal(big_sub_total.add(monto.setScale(2, BigDecimal.ROUND_HALF_UP)));

                    //Addenda adenda = objeto.createComprobanteAddenda();
                    //adenda.getAny().add("<H><H>");//agregar cada adenda
                //comprobante.setAddenda(adenda);
                //************************
            }
            else{
                finkok33.ObjectFactory objeto = new finkok33.ObjectFactory();
                comprobante33 = objeto.createComprobante();

                comprobante33.setVersion(Constantes33.VERSION_COMPROBANTE_TRES);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss");
                comprobante33.setFecha(sdf.format(fecha_factura));
                comprobante33.setSello(null);//Falta
                comprobante33.setFormaPago(c_metodo_pago.getSelectedItem().toString());
                comprobante33.setNoCertificado(null);//Falta
                comprobante33.setCertificado(null);//Falta
                comprobante33.setCondicionesDePago(c_forma_pago.getSelectedItem().toString());
                comprobante33.setTipoCambio(BigDecimal.valueOf(((Number)t_tipo_cambio.getValue()).doubleValue() ).setScale(0, BigDecimal.ROUND_HALF_UP));
                if(c_moneda.getSelectedItem().toString().compareTo("MXN")==0)
                    comprobante33.setMoneda(CMoneda.MXN);
                else
                    comprobante33.setMoneda(CMoneda.USD);
                comprobante33.setTipoDeComprobante(CTipoDeComprobante.E);
                switch(c_forma_pago.getSelectedItem().toString())
                {
                    case "PAGO EN UNA SOLA EXHIBICION":
                        comprobante33.setMetodoPago(CMetodoPago.PUE);
                        break;
                    case "PAGO EN PARCIALIDADES O DIFERIDO":
                        comprobante33.setMetodoPago(CMetodoPago.PPD);
                        break;    
                }
                
                comprobante33.setLugarExpedicion(factura.getCpEmisor());
                comprobante33.setSerie(serie);
                comprobante33.setFolio(folio);//Falta
                
                //agregamos CFDI Enlazados
                if(cb_tipo_relacion.getSelectedItem().toString().compareToIgnoreCase("BUSCAR")!=0 && t_relacionados.getRowCount()>0 )
                {
                    CfdiRelacionados relacion=new CfdiRelacionados();
                    relacion.setTipoRelacion(cb_tipo_relacion.getSelectedItem().toString());
                    List <CfdiRelacionado> lista_relacion=relacion.getCfdiRelacionado();
                    for(int w=0; w<t_relacionados.getRowCount(); w++)
                    {
                        CfdiRelacionado elemento=new CfdiRelacionado();
                        elemento.setUUID(t_relacionados.getValueAt(w, 1).toString());
                        lista_relacion.add(elemento);
                    }
                    comprobante33.setCfdiRelacionados(relacion);
                }
                
                    finkok33.Comprobante.Emisor emisor= objeto.createComprobanteEmisor();
                    emisor.setRfc(factura.getRfcEmisor());//Cambiar por config.getRfc()
                    if(factura.getNombreEmisor().compareTo("VICENTE PEREZ VILLAR.")==0)
                    {
                        emisor.setNombre(acentos("VICENTE PEREZ VILLAR"));
                        emisor.setRegimenFiscal("612");
                    }
                    else
                    {
                        emisor.setNombre(acentos(factura.getNombreEmisor()));
                        emisor.setRegimenFiscal("601");
                    }
                comprobante33.setEmisor(emisor);
                    finkok33.Comprobante.Receptor receptor=objeto.createComprobanteReceptor();
                    receptor.setRfc(t_rfc.getText());
                    receptor.setNombre(acentos(t_social.getText()));
                    receptor.setUsoCFDI(CUsoCFDI.fromValue(t_id_cfdi.getText()));
                comprobante33.setReceptor(receptor);
                    finkok33.Comprobante.Conceptos misConceptos = objeto.createComprobanteConceptos();
                        BigDecimal big_total_bruto= new BigDecimal("0.0");
                        BigDecimal big_sub_total=new BigDecimal("0.0");
                        BigDecimal big_iva_total=new BigDecimal("0.0");

                        for(int ren=0; ren<t_datos.getRowCount(); ren++)
                        {
                            //descuento
                            BigDecimal big_descuento=new BigDecimal(t_datos.getValueAt(ren, 6).toString()).setScale(2, BigDecimal.ROUND_HALF_UP);
                            BigDecimal big_porciento_dectuento=big_descuento.divide(new BigDecimal("100")).setScale(2, BigDecimal.ROUND_HALF_UP);
                            //cantidades de lista
                            BigDecimal big_cantidad=new BigDecimal(t_datos.getValueAt(ren, 1).toString()).setScale(2, BigDecimal.ROUND_HALF_UP);
                            BigDecimal big_precio_lista=new BigDecimal(t_datos.getValueAt(ren, 5).toString()).setScale(2, BigDecimal.ROUND_HALF_UP);
                            BigDecimal big_total_lista=big_precio_lista.multiply(big_cantidad);
                            big_total_bruto = big_total_bruto.add(big_total_lista.setScale(2, BigDecimal.ROUND_HALF_UP));
                            BigDecimal big_total_descuento=big_precio_lista.multiply(big_porciento_dectuento.setScale(2, BigDecimal.ROUND_HALF_UP)).setScale(2, BigDecimal.ROUND_HALF_UP);
                            //cantidades netas
                            BigDecimal big_general_descuento=big_cantidad.multiply(big_total_descuento);
                            BigDecimal big_precio_neto=big_precio_lista.subtract(big_total_descuento);
                            BigDecimal big_total_neto=big_precio_neto.multiply(big_cantidad);
                            big_sub_total = big_sub_total.add(big_total_neto.setScale(2, BigDecimal.ROUND_HALF_UP));

                            finkok33.Comprobante.Conceptos.Concepto renglon= objeto.createComprobanteConceptosConcepto();
                            renglon.setCantidad(big_cantidad.setScale(2, BigDecimal.ROUND_HALF_UP));
                            renglon.setClaveProdServ(t_datos.getValueAt(ren, 4).toString().trim());
                            ClaveUnidad cve_unidad=(ClaveUnidad)session.createCriteria(ClaveUnidad.class).add(Restrictions.eq("simbolo", t_datos.getValueAt(ren, 2).toString().trim())).uniqueResult();
                            renglon.setClaveUnidad(cve_unidad.getIdClaveUnidad());
                            renglon.setUnidad(t_datos.getValueAt(ren, 2).toString().trim());
                            renglon.setDescripcion(t_datos.getValueAt(ren, 3).toString().trim());
                            renglon.setImporte(big_total_lista.setScale(2, BigDecimal.ROUND_HALF_UP));
                            renglon.setValorUnitario(big_precio_lista.setScale(2, BigDecimal.ROUND_HALF_UP));
                            if(big_general_descuento.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue()>0)
                                renglon.setDescuento(big_general_descuento.setScale(2, BigDecimal.ROUND_HALF_UP));
                            
                            finkok33.Comprobante.Conceptos.Concepto.Impuestos impuestos =objeto.createComprobanteConceptosConceptoImpuestos();
                            BigDecimal porc=valorIva.divide(new BigDecimal("100.0")).setScale(2, BigDecimal.ROUND_HALF_UP);
                            BigDecimal monto=big_total_neto.multiply(porc).setScale(2, BigDecimal.ROUND_HALF_UP);
                                finkok33.Comprobante.Conceptos.Concepto.Impuestos.Traslados misTrasladados=objeto.createComprobanteConceptosConceptoImpuestosTraslados();
                                    finkok33.Comprobante.Conceptos.Concepto.Impuestos.Traslados.Traslado traslado= objeto.createComprobanteConceptosConceptoImpuestosTrasladosTraslado();
                                    traslado.setBase(big_total_neto);
                                    traslado.setImpuesto("002");
                                    traslado.setTipoFactor(CTipoFactor.TASA);
                                    traslado.setTasaOCuota(porc.setScale(6, BigDecimal.ROUND_HALF_UP));
                                    traslado.setImporte(monto.setScale(2, BigDecimal.ROUND_HALF_UP));
                                    big_iva_total=big_iva_total.add(monto);
                                misTrasladados.getTraslado().add(traslado);
                            impuestos.setTraslados(misTrasladados);
                            renglon.setImpuestos(impuestos);
                            misConceptos.getConcepto().add(renglon);
                        }

                comprobante33.setConceptos(misConceptos);
                    finkok33.Comprobante.Impuestos impuestos =objeto.createComprobanteImpuestos();
                    BigDecimal porc=(valorIva.divide(new BigDecimal("100.0"))).setScale(2, BigDecimal.ROUND_HALF_UP);
                    //BigDecimal monto=(big_sub_total.multiply(porc)).setScale(2, BigDecimal.ROUND_HALF_UP);
                    impuestos.setTotalImpuestosTrasladados(big_iva_total.setScale(2, BigDecimal.ROUND_HALF_UP));
                        finkok33.Comprobante.Impuestos.Traslados misTrasladados=objeto.createComprobanteImpuestosTraslados();
                            finkok33.Comprobante.Impuestos.Traslados.Traslado traslado= objeto.createComprobanteImpuestosTrasladosTraslado();
                            traslado.setImpuesto("002");
                            traslado.setTipoFactor(CTipoFactor.TASA);
                            traslado.setTasaOCuota(porc.setScale(6, BigDecimal.ROUND_HALF_UP));
                            traslado.setImporte(big_iva_total.setScale(2, BigDecimal.ROUND_HALF_UP));
                        misTrasladados.getTraslado().add(traslado);
                    impuestos.setTraslados(misTrasladados);
                comprobante33.setImpuestos(impuestos);

                comprobante33.setSubTotal(big_total_bruto.setScale(2, BigDecimal.ROUND_HALF_UP));
                BigDecimal big_descuento=big_total_bruto.subtract(big_sub_total);
                if(big_descuento.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue()>0)
                {
                    comprobante33.setDescuento(big_descuento.setScale(2, BigDecimal.ROUND_HALF_UP));//Falta
                }
                comprobante33.setTotal(big_sub_total.add(big_iva_total.setScale(2, BigDecimal.ROUND_HALF_UP)));

                    //Addenda adenda = objeto.createComprobanteAddenda();
                    //adenda.getAny().add("<H><H>");//agregar cada adenda
                //comprobante.setAddenda(adenda);
                //************************
            }
            return true;
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return false;
        }
        finally
        {
            if(session!=null)
                if(session.isOpen())
                    session.close();
        }
    }
    
    public void consultaFacturaElectronica(File  arch){
        Session session = HibernateUtil.getSessionFactory().openSession();
        try{
            session.beginTransaction().begin();
            factura=(Nota)session.get(Nota.class, factura.getIdNota());
            int numeroID=0;
            if(factura.getOrden()!=null)
                numeroID=factura.getOrden().getIdOrden();
            if(factura.getOrdenExterna()!=null)
                numeroID=factura.getOrdenExterna().getIdOrden();
            Configuracion config=(Configuracion)session.createCriteria(Configuracion.class).add(Restrictions.eq("empresa", factura.getNombreEmisor())).setMaxResults(1).uniqueResult();
            
            String folio=""+factura.getFolioExterno();
            String serie=factura.getSerieExterno();
            double version=config.getVersion();
            if(XML_FINKOK(factura.getOrden(), folio, serie, config.getVersion())==true)
            {
                GeneradorSelloDigital generadorsello = new GeneradorSelloDigital();
                if(config.getCer()!=null && config.getCer().compareTo("")!=0)
                {
                    if(config.getClave()!=null && config.getClave().compareTo("")!=0)
                    {
                        String cadena="";
                        String[] certificado=generadorsello.getCertificado(ruta+"config/"+config.getCer());
                        if(certificado[0].compareTo("1")==0)
                        {
                            if(version==3.2d)
                            {
                                comprobante.setNoCertificado(certificado[2]);
                                comprobante.setCertificado(certificado[3]);
                            }
                            else
                            {
                                comprobante33.setNoCertificado(certificado[2]);
                                comprobante33.setCertificado(certificado[3]);
                            }
                            String[] getSello;
                            if(version==3.2d)
                            {
                                cadena = comprobante.getCadenaOriginal();
                                getSello = generadorsello.getSelloDigital(cadena, config.getClave(), ruta+"config/"+config.getLlave());
                            }
                            else
                            {
                                timbrar.CadenaOriginal33 cOriginal= new timbrar.CadenaOriginal33(comprobante33); 
                                cadena = cOriginal.getCadena();
                                getSello = generadorsello.getSelloDigital33(cadena, config.getClave(), ruta+"config/"+config.getLlave());
                            }
                            if(getSello[0].compareTo("1")==0)
                            {
                                String sello=getSello[1];
                                FinkokJavaToXML xml= new FinkokJavaToXML();
                                boolean genero=false;
                                if(version==3.2d)
                                {
                                    comprobante.setSello(sello);
                                    genero = xml.creaAndValidaXML(comprobante, ruta+"nativos/"+numeroID+"nativo.xml");
                                }
                                else
                                {
                                    comprobante33.setSello(sello);
                                    genero = xml.convierteXML(comprobante33, ruta+"nativos/"+numeroID+"nativo.xml");
                                }
                                if(genero==true)
                                {
                                    ArrayList datos=new ArrayList();
                                    ArrayList guarda;
                                    if(factura.getPac().compareTo("F")==0)
                                    {
                                        ApiFinkok api1=new ApiFinkok(ruta);
                                        datos.add(config.getEmailFinkok());
                                        datos.add(config.getClaveFinkok());
                                        datos.add(numeroID+"nativo.xml");//nombre del archivo nativo
                                        datos.add(factura.getRfcEmisor()+"_"+serie+"_"+folio+"_"+t_rfc.getText()+".xml");//nombre del archivo timbrado
                                        guarda=api1.llamarSoapConsulta(datos);
                                    }
                                    else
                                    {
                                        ApiTurbo api1=new ApiTurbo(ruta);
                                        
                                        datos.add(config.getEmailFinkok());
                                        datos.add(config.getClaveFinkok());
                                        datos.add(factura.getFFiscal());//nombre del archivo nativo
                                        datos.add(factura.getRfcEmisor()+"_"+serie+"_"+folio+"_"+t_rfc.getText()+".xml");//nombre del archivo timbrado
                                        guarda=api1.llamarSoapConsulta(datos);
                                    }
                                    switch(guarda.get(0).toString())
                                    {
                                        case "1"://Se timbro correcto
                                            factura=(Nota)session.get(Nota.class, factura.getIdNota());
                                            factura.setFecha(fecha_factura);
                                            factura.setEstadoFactura(guarda.get(1).toString());
                                            factura.setEstatus(guarda.get(2).toString());
                                            factura.setFFiscal(guarda.get(3).toString());
                                            factura.setFechaFiscal(guarda.get(4).toString());
                                            factura.setSerieExterno(serie);
                                            factura.setFolioExterno(Integer.parseInt(folio));
                                            factura.setNombreDocumento(config.getRfc()+"_"+serie+"_"+folio+"_"+t_rfc.getText());
                                            factura.setPac("T");
                                            session.update(factura);
                                            session.beginTransaction().commit();

                                            progreso.setString("Listo");
                                            progreso.setIndeterminate(false);
                                            if(session.isOpen())
                                                session.close();
                                            guarda();
                                            consulta();
                                            Desktop.getDesktop().open(arch);
                                            break;

                                        case "0"://Hay error local
                                            ArrayList lista=(ArrayList)guarda.get(1);
                                            String error="Error";
                                            for(int x=0; x<lista.size(); x++)
                                            {
                                                ArrayList inc=(ArrayList)lista.get(x);
                                                error+="Error: ";
                                                for(int y=0; y<inc.size(); y++)
                                                {
                                                    error+=lista.get(0)+" ";
                                                }
                                                error+="\n";
                                            }
                                            progreso.setString("Listo");
                                            progreso.setIndeterminate(false);
                                            JOptionPane.showMessageDialog(null, error);
                                            break;

                                        case "-1"://Hay error en el SAP
                                            progreso.setString("Listo");
                                            progreso.setIndeterminate(false);
                                            JOptionPane.showMessageDialog(null, guarda.get(1).toString());
                                            break;
                                    }
                                }
                                else
                                {
                                    progreso.setString("Listo");
                                    progreso.setIndeterminate(false);
                                    JOptionPane.showMessageDialog(null, "Error al generar el XML.-"+error);
                                }
                            }
                            else
                            {
                                progreso.setString("Listo");
                                progreso.setIndeterminate(false);
                                JOptionPane.showMessageDialog(null, getSello[1]);
                            }
                        }
                        else
                        {
                            progreso.setString("Listo");
                            progreso.setIndeterminate(false);
                            JOptionPane.showMessageDialog(null, certificado[1]);
                        }
                    }
                    else
                    {
                        progreso.setString("Listo");
                        progreso.setIndeterminate(false);
                        JOptionPane.showMessageDialog(null, "Falta la clave del Archivo CER");
                    }
                }
                else
                {
                    progreso.setString("Listo");
                    progreso.setIndeterminate(false);
                    JOptionPane.showMessageDialog(null, "Falta agrerar el archivo CER");
                }
            }
            else
            {
                progreso.setString("Listo");
                progreso.setIndeterminate(false);
                JOptionPane.showMessageDialog(null, "Error al generar el XML.-"+error);
            }
        }catch(Exception e)
        {
            habilita(true, false);
            progreso.setString("Listo");
            progreso.setIndeterminate(false);
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al abrir el archivo de configuración..");
        }
        finally
        {
            if(session!=null)
                if(session.isOpen())
                    session.close();
        }
    }
    
    public static String acentos(String input) {
        // Descomposición canónica
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
        // Nos quedamos únicamente con los caracteres ASCII
        Pattern pattern = Pattern.compile("\\P{ASCII}");
        return pattern.matcher(normalized).replaceAll("");
    }
    boolean busca_relacion(String aux)
    {
        if(aux!=null)
        {
            for(int x=0; x<t_relacionados.getRowCount(); x++)
            {
                if(t_relacionados.getValueAt(x, 1).toString().compareTo(aux)==0)
                    return true;
            }
            return false;
        }
        else
            return false;
        
    }
}
