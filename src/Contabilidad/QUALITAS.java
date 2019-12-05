/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Contabilidad;
import Compras.Formatos;
import static Contabilidad.GNP.acentos;
import static Contabilidad.General.acentos;
import Hibernate.Util.HibernateUtil;
import Hibernate.entidades.ClaveUnidad;
import Hibernate.entidades.Concepto;
import Hibernate.entidades.Configuracion;
import Hibernate.entidades.Factura;
import Hibernate.entidades.Nota;
import Hibernate.entidades.Orden;
import Hibernate.entidades.OrdenExterna;
import Hibernate.entidades.ProductoServicio;
import Hibernate.entidades.Relacion;
import Hibernate.entidades.UsoCfdi;
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
import mysuite.TDomicilioComercial;
import mysuite.TFactDocMX;
import mysuite.TImpuestos;
import mysuite.TMTECabecera;
import mysuite.TMTEContactoEmisor;
import mysuite.TMTEContactoReceptor;
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
import Integral.Comprimir;
import Integral.Conexion;
import Integral.FormatoTabla;
import Integral.Herramientas;
import Integral.Render1;
import Integral.numeroLetra;
import api.ApiTurbo;
import finkok.Comprobante;
import finkok.TUbicacion;
import finkok.TUbicacionFiscal;
import finkok33.CMetodoPago;
import finkok33.CMoneda;
import finkok33.CTipoDeComprobante;
import finkok33.CTipoFactor;
import finkok33.CUsoCFDI;
import java.awt.event.ItemEvent;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
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
import org.jdom2.Content;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.filter.Filters;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.jdom2.xpath.XPathExpression;
import org.jdom2.xpath.XPathFactory;
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
public class QUALITAS extends javax.swing.JDialog {

    String error="";
    private Usuario user;
    Date fecha_factura=new Date();
    String sessionPrograma=null;
    FormatoTabla formato;
    Herramientas h;
    String abrir="";
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
    Factura factura;
    boolean permiso=false;
    public static final Factura RET_CANCEL =null;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm-Ss");
    private Factura returnStatus = RET_CANCEL;
    String ruta="";
    finkok.Comprobante comprobante;
    finkok33.Comprobante comprobante33;
    String PAC="";
    boolean bandera=true;
    String anterior="";
    int configuracion=1;
    /**
     * Creates new form QUALITAS
     */
    public QUALITAS(java.awt.Frame parent, boolean modal, Usuario u, String ses, Factura fac, int configuracion) {
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
        jLabel88 = new javax.swing.JLabel();
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
        jLabel89 = new javax.swing.JLabel();
        cb_tipo_relacion = new javax.swing.JComboBox();
        l_tipo_relacion = new javax.swing.JLabel();
        b_menos1 = new javax.swing.JButton();
        b_mas1 = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jPanel10 = new javax.swing.JPanel();
        jLabel30 = new javax.swing.JLabel();
        c_area = new javax.swing.JComboBox();
        c_tipo = new javax.swing.JComboBox();
        jLabel45 = new javax.swing.JLabel();
        jLabel46 = new javax.swing.JLabel();
        jLabel47 = new javax.swing.JLabel();
        jLabel48 = new javax.swing.JLabel();
        t_codigo = new javax.swing.JTextField();
        jLabel66 = new javax.swing.JLabel();
        jLabel67 = new javax.swing.JLabel();
        t_inc = new javax.swing.JTextField();
        jLabel68 = new javax.swing.JLabel();
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
        jLabel90 = new javax.swing.JLabel();
        cb_no_proveedor = new javax.swing.JComboBox();
        jLabel91 = new javax.swing.JLabel();
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
        jLabel63 = new javax.swing.JLabel();
        t_oficina = new javax.swing.JTextField();
        t_electronico = new javax.swing.JTextField();
        jLabel64 = new javax.swing.JLabel();
        jLabel78 = new javax.swing.JLabel();
        jLabel87 = new javax.swing.JLabel();
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

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setModalityType(java.awt.Dialog.ModalityType.APPLICATION_MODAL);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(254, 254, 254));

        jLabel1.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel1.setText("Emisión de Facturas a Qualitas");

        l_emisor.setText("jLabel88");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(355, 355, 355)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 393, Short.MAX_VALUE)
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 183, Short.MAX_VALUE)
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

        t_social.setToolTipText("Razon social del receptor");
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
                .addContainerGap(45, Short.MAX_VALUE))
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

        t_cp.setToolTipText("Codifo Postal del receptor");
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

        c_estado.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "AGUASCALIENTES", "BAJA CALIFORNIA", "BAJA CALIFORNIA SUR", "CAMPECHE", "CHIAPAS", "CHIHUAHUA", "COAHUILA", "COLIMA", "CIUDAD DE MEXICO", "DURANGO", "ESTADO DE MEXICO", "GUANAJUATO", "GUERRERO", "HIDALGO", "JALISCO", "MICHOACAN", "MORELOS", "NAYARIT", "NUEVO LEON", "OAXACA", "PUEBLA", "QUERETARO", "QUINTANA ROO", "SAN LUIS POTOSI", "SINALOA", "SONORA", "TABASCO", "TAMAULIPAS", "TLAXCALA", "VERACRUZ", "YUCATAN", "ZACATECAS" }));
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

        jLabel88.setText("*");

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
                                .addComponent(jLabel13)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(t_colonia, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(3, 3, 3)
                                .addComponent(jLabel12)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 126, Short.MAX_VALUE)
                                .addComponent(jLabel14)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(t_municipio, javax.swing.GroupLayout.PREFERRED_SIZE, 332, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(3, 3, 3)
                                .addComponent(jLabel15))
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
                                .addGap(168, 168, 168)
                                .addComponent(jButton7)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(t_id_cfdi, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(t_descripcion_cfdi)
                                .addGap(1, 1, 1)
                                .addComponent(jLabel88)))))
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
                            .addComponent(jLabel88))
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
        l_iva.setText("%  IVA:");
        jPanel9.add(l_iva, new org.netbeans.lib.awtextra.AbsoluteConstraints(43, 23, -1, -1));

        l_total.setFont(new java.awt.Font("Arial", 0, 9)); // NOI18N
        l_total.setForeground(new java.awt.Color(255, 255, 255));
        l_total.setText("Total:");
        jPanel9.add(l_total, new org.netbeans.lib.awtextra.AbsoluteConstraints(53, 45, -1, -1));

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
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
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
                        .addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(47, 47, 47)
                        .addComponent(l_iva1, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(15, 15, 15)
                        .addComponent(t_descuento, javax.swing.GroupLayout.DEFAULT_SIZE, 192, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel21)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(t_cuenta_pago, javax.swing.GroupLayout.PREFERRED_SIZE, 221, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel23)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(c_moneda, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel24)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(t_tipo_cambio, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel28)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
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
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(c_metodo_pago, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel20))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel25)
                            .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel26)
                                .addComponent(jLabel22)
                                .addComponent(c_forma_pago, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
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
                "id", "Cant", "Med", "Descripción", "Cve-Prod", "Costo c/u", "Descuento", "Total"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Object.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, true, true, true, true, false
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

        jLabel89.setText("Tipo de Relacion:");

        cb_tipo_relacion.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "BUSCAR", "03", "04", "07" }));
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
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 988, Short.MAX_VALUE)
                    .addGroup(jPanel16Layout.createSequentialGroup()
                        .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel16Layout.createSequentialGroup()
                                .addComponent(jLabel89)
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
                    .addComponent(jLabel89)
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

        jLabel30.setText("Área:");

        c_area.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Talleres", "Talleres de Equipo Pesado" }));
        c_area.setToolTipText("Sector de la cual proviene la factura");

        c_tipo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "DAÑOS", "AUTOS" }));
        c_tipo.setToolTipText("Tipo de poliza");

        jLabel45.setText("Tipo:");

        jLabel46.setText("*");

        jLabel47.setText("*");

        jLabel48.setText("Código Int. Emisor:");

        t_codigo.setToolTipText("Folio interno de la factura");
        t_codigo.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        t_codigo.setEnabled(false);
        t_codigo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                t_codigoKeyTyped(evt);
            }
        });

        jLabel66.setText("*");

        jLabel67.setText("Inc:");

        t_inc.setToolTipText("Número de inciso");
        t_inc.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        t_inc.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                t_incKeyTyped(evt);
            }
        });

        jLabel68.setText("*");

        jLabel69.setText("*");

        t_no_rep.setToolTipText("Numero de reporte");
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

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addComponent(jLabel70)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(t_no_rep, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel69)
                .addGap(18, 18, 18)
                .addComponent(jLabel73)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(c_tipo_cliente, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel74)
                .addGap(26, 26, 26)
                .addComponent(jLabel75)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(t_siniestro, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel76)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addComponent(jLabel30)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(c_area, javax.swing.GroupLayout.PREFERRED_SIZE, 182, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel46)
                .addGap(18, 18, 18)
                .addComponent(jLabel48)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(t_codigo, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel66)
                .addGap(18, 18, 18)
                .addComponent(jLabel45)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(c_tipo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel47)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel72)
                .addGap(5, 5, 5)
                .addComponent(t_numero, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel71)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 125, Short.MAX_VALUE)
                .addComponent(jLabel67)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(t_inc, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel68))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel72)
                            .addComponent(t_numero, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel71))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel30)
                            .addComponent(c_area, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel46)
                            .addComponent(jLabel48)
                            .addComponent(t_codigo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel45)
                            .addComponent(c_tipo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel47)
                            .addComponent(jLabel66)
                            .addComponent(jLabel67)
                            .addComponent(t_inc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel68))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel75)
                                .addComponent(t_siniestro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel76))
                            .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel73)
                                .addComponent(jLabel74))
                            .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel70)
                                .addComponent(t_no_rep, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel69))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(c_tipo_cliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
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

        t_tel_emisor.setToolTipText("Teléfono del contacto emisor");
        t_tel_emisor.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        t_tel_emisor.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                t_tel_emisorKeyTyped(evt);
            }
        });

        jLabel35.setText("*");

        jLabel36.setText("*");

        c_tipo_emisor.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "MATRIZ", "VENDEDOR", "EMPLEADO", "SUCURSAL", "AGENCIA", "DEPARTAMENTO", "TRANSPORTISTA", "DISTRUBUIDOR", "OTRO" }));
        c_tipo_emisor.setToolTipText("Tipo de contacto emisor");

        jLabel65.setText("*");

        jLabel85.setText("*");

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel33)
                    .addComponent(jLabel31))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addComponent(c_tipo_emisor, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(5, 5, 5)
                        .addComponent(jLabel65)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel32)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(t_nombre_emisor, javax.swing.GroupLayout.PREFERRED_SIZE, 498, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addComponent(t_correo_emisor, javax.swing.GroupLayout.PREFERRED_SIZE, 461, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel36)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel34)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(t_tel_emisor, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel35)
                    .addComponent(jLabel85))
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
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel33)
                    .addComponent(t_correo_emisor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel34)
                    .addComponent(t_tel_emisor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel36)
                    .addComponent(jLabel85))
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
        c_tipo_receptor.setToolTipText("Tipo de contacto receptor");

        jLabel84.setText("*");

        jLabel90.setText("No Proveedor:");

        jLabel91.setText("*");

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
                        .addComponent(c_tipo_receptor, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(6, 6, 6)
                        .addComponent(jLabel84)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel38)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(t_nombre_receptor, javax.swing.GroupLayout.PREFERRED_SIZE, 453, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addComponent(t_correo_receptor, javax.swing.GroupLayout.PREFERRED_SIZE, 411, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel42)
                        .addGap(34, 34, 34)
                        .addComponent(jLabel40)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(t_tel_receptor)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addComponent(jLabel41)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel90)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cb_no_proveedor, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel91, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel44))
                .addGap(0, 94, Short.MAX_VALUE))
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(cb_no_proveedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel90)
                        .addComponent(jLabel91))
                    .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel37)
                        .addComponent(jLabel38)
                        .addComponent(t_nombre_receptor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel41))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(c_tipo_receptor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel84)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel39)
                    .addComponent(t_correo_receptor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel40)
                    .addComponent(t_tel_receptor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel42)
                    .addComponent(jLabel44))
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

        t_motor_vehiculo.setToolTipText("Numero de motor de la unidad");
        t_motor_vehiculo.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        t_motor_vehiculo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                t_motor_vehiculoKeyTyped(evt);
            }
        });

        c_tipo_vehiculo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "PARTICULAR", "PUBLICO", "PUBLICO FEDERAL (CARGA)", " " }));
        c_tipo_vehiculo.setToolTipText("Tipo de unidad");

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
                        .addComponent(jLabel77)
                        .addContainerGap())
                    .addComponent(t_motor_vehiculo)))
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

        jLabel63.setText("Código Oficina:");

        t_oficina.setToolTipText("codigo de oficina");
        t_oficina.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        t_oficina.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                t_oficinaKeyTyped(evt);
            }
        });

        t_electronico.setToolTipText("En caso de ingresar varios folios separarlos con el caracter \";\" eje 1234;5678");
        t_electronico.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        t_electronico.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                t_electronicoKeyTyped(evt);
            }
        });

        jLabel64.setText("Folios Electrónicos:");

        jLabel78.setText("*");

        jLabel87.setForeground(new java.awt.Color(102, 0, 204));
        jLabel87.setText("Nota: En caso de ingresar varios folios separarlos con el caracter \";\" eje 1234;5678");

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
                        .addComponent(jLabel63)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(t_oficina, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(52, 52, 52)
                        .addComponent(jLabel64)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(jLabel87)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(t_electronico)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel78)))))
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel64)
                        .addComponent(t_electronico, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel78))
                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel63)
                        .addComponent(t_oficina, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel87)
                .addContainerGap(50, Short.MAX_VALUE))
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
                .addContainerGap(424, Short.MAX_VALUE))
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
                                                            if(c_area.getSelectedIndex()>=0)
                                                            {
                                                                if(t_codigo.getText().trim().compareTo("")!=0)
                                                                {
                                                                    if(c_tipo.getSelectedIndex()>=0)
                                                                    {
                                                                        if(t_numero.getText().trim().compareTo("")!=0)
                                                                        {
                                                                            if(t_inc.getText().trim().compareTo("")!=0)
                                                                            {
                                                                                if(t_no_rep.getText().trim().compareTo("")!=0)
                                                                                {
                                                                                    if(c_tipo_cliente.getSelectedIndex()>=0)
                                                                                    {
                                                                                        if(t_siniestro.getText().trim().compareTo("")!=0)
                                                                                        {
                                                                                            if(c_tipo_emisor.getSelectedIndex()>=0)
                                                                                            {
                                                                                                if(t_nombre_emisor.getText().trim().compareTo("")!=0)
                                                                                                {
                                                                                                    if(t_correo_emisor.getText().trim().compareTo("")!=0)
                                                                                                    {
                                                                                                        if(t_tel_emisor.getText().trim().compareTo("")!=0)
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
                                                                                                                                                        if(t_id_cfdi.getText().compareTo("")!=0)
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
                                                                                                                                                                    habilita(false, false);
                                                                                                                                                                    progreso.setIndeterminate(true);
                                                                                                                                                                    progreso.setString("Conectando al servidor SAT Espere");
                                                                                                                                                                    facturaElectronica();
                                                                                                                                                                }
                                                                                                                                                            }
                                                                                                                                                            else
                                                                                                                                                            {
                                                                                                                                                                contenedor.setSelectedIndex(2);
                                                                                                                                                                JOptionPane.showMessageDialog(null, "Debe ingresar los CFDI Relacionados\n"
                                                                                                                                                                        + "o quitar el campo relacion");
                                                                                                                                                            }
                                                                                                                                                        }
                                                                                                                                                        else
                                                                                                                                                        {
                                                                                                                                                            contenedor.setSelectedIndex(0);
                                                                                                                                                            JOptionPane.showMessageDialog(null, "Debe El Uso del CFDI");
                                                                                                                                                            t_id_cfdi.requestFocus();
                                                                                                                                                        }
                                                                                                                                                    }
                                                                                                                                                    else
                                                                                                                                                    {
                                                                                                                                                        contenedor.setSelectedIndex(3);
                                                                                                                                                        JOptionPane.showMessageDialog(null, "Debe ingresar las placas del vehículo");
                                                                                                                                                        t_placas_vehiculo.requestFocus();
                                                                                                                                                    }
                                                                                                                                                }
                                                                                                                                                else
                                                                                                                                                {
                                                                                                                                                    contenedor.setSelectedIndex(3);
                                                                                                                                                    JOptionPane.showMessageDialog(null, "Debe ingresar la serie del vehículo");
                                                                                                                                                    t_serie_vehiculo.requestFocus();
                                                                                                                                                }
                                                                                                                                            }
                                                                                                                                            else
                                                                                                                                            {
                                                                                                                                                contenedor.setSelectedIndex(3);
                                                                                                                                                JOptionPane.showMessageDialog(null, "Debe ingresar el año del vehículo");
                                                                                                                                                t_anio_vehiculo.requestFocus();
                                                                                                                                            }
                                                                                                                                        }
                                                                                                                                        else
                                                                                                                                        {
                                                                                                                                            contenedor.setSelectedIndex(3);
                                                                                                                                            JOptionPane.showMessageDialog(null, "Debe ingresar la marca del vehículo");
                                                                                                                                            t_marca_vehiculo.requestFocus();
                                                                                                                                        }
                                                                                                                                    }
                                                                                                                                    else
                                                                                                                                    {
                                                                                                                                        contenedor.setSelectedIndex(3);
                                                                                                                                        JOptionPane.showMessageDialog(null, "Debe ingresar el color del vehículo");
                                                                                                                                        t_color_vehiculo.requestFocus();
                                                                                                                                    }
                                                                                                                                }
                                                                                                                                else
                                                                                                                                {
                                                                                                                                    contenedor.setSelectedIndex(3);
                                                                                                                                    JOptionPane.showMessageDialog(null, "Debe ingresar el modelo del vehículo");
                                                                                                                                    t_modelo_vehiculo.requestFocus();
                                                                                                                                }
                                                                                                                            }
                                                                                                                            else
                                                                                                                            {
                                                                                                                                contenedor.setSelectedIndex(3);
                                                                                                                                JOptionPane.showMessageDialog(null, "Debe ingresar el tipo de propietario del vehículo");
                                                                                                                                c_tipo_vehiculo.requestFocus();
                                                                                                                            }
                                                                                                                        }
                                                                                                                        else
                                                                                                                        {
                                                                                                                            contenedor.setSelectedIndex(3);
                                                                                                                            JOptionPane.showMessageDialog(null, "Debes ingresar el teléfono del receptor");
                                                                                                                            t_tel_receptor.requestFocus();
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
                                                                                                                JOptionPane.showMessageDialog(null, "Debes ingresar el tipo del receptor");
                                                                                                                c_tipo_receptor.requestFocus();
                                                                                                            }
                                                                                                        }
                                                                                                        else
                                                                                                        {
                                                                                                            contenedor.setSelectedIndex(3);
                                                                                                            JOptionPane.showMessageDialog(null, "Debes ingrear el teléfono del emisor");
                                                                                                            t_tel_emisor.requestFocus();
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
                                                                                                contenedor.setSelectedIndex(3);
                                                                                                JOptionPane.showMessageDialog(null, "Debes ingresar el tipo del contacto emisor");
                                                                                                c_tipo_emisor.requestFocus();
                                                                                            }
                                                                                        }
                                                                                        else
                                                                                        {
                                                                                            contenedor.setSelectedIndex(3);
                                                                                            JOptionPane.showMessageDialog(null, "Debes ingresar el número de siniestro");
                                                                                            t_siniestro.requestFocus();
                                                                                        }
                                                                                    }
                                                                                    else
                                                                                    {
                                                                                        contenedor.setSelectedIndex(3);
                                                                                        JOptionPane.showMessageDialog(null, "Debes ingresar el tipo de cliente");
                                                                                        c_tipo_cliente.requestFocus();
                                                                                    }
                                                                                }
                                                                                else
                                                                                {
                                                                                    contenedor.setSelectedIndex(3);
                                                                                    JOptionPane.showMessageDialog(null, "Debes ingresar el número de reporte");
                                                                                    t_no_rep.requestFocus();
                                                                                }
                                                                            }
                                                                            else
                                                                            {
                                                                                contenedor.setSelectedIndex(3);
                                                                                JOptionPane.showMessageDialog(null, "Debes ingresar el inciso");
                                                                                t_inc.requestFocus();
                                                                            }
                                                                        }
                                                                        else
                                                                        {
                                                                            contenedor.setSelectedIndex(3);
                                                                            JOptionPane.showMessageDialog(null, "debes ingresar el no° de poliza");
                                                                            t_numero.requestFocus();
                                                                        }
                                                                    }
                                                                    else
                                                                    {
                                                                        contenedor.setSelectedIndex(3);
                                                                        JOptionPane.showMessageDialog(null, "Debes ingresar el tipo de poliza");
                                                                        c_tipo.requestFocus();
                                                                    }
                                                                }
                                                                else
                                                                {
                                                                    contenedor.setSelectedIndex(3);
                                                                    JOptionPane.showMessageDialog(null, "Debes ingresar el número interno de factura");
                                                                    t_codigo.requestFocus();
                                                                }
                                                            }
                                                            else
                                                            {
                                                                contenedor.setSelectedIndex(3);
                                                                JOptionPane.showMessageDialog(null, "Debes ingresar ingresar el area");
                                                                c_area.requestFocus();
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
                JOptionPane.showMessageDialog(null, "Debes ingresar el RFC receptor");
                t_rfc.requestFocus();
            }
        }
        else
        {
            contenedor.setSelectedIndex(0);
            JOptionPane.showMessageDialog(null, "Debes ingresar la razón social del receptor");
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
        if(t_cp.getText().length()>=5)
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

    private void t_codigoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_codigoKeyTyped
        // TODO add your handling code here:
        evt.setKeyChar(Character.toUpperCase(evt.getKeyChar()));
        if(t_codigo.getText().length()>=6)
            evt.consume();
    }//GEN-LAST:event_t_codigoKeyTyped

    private void t_incKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_incKeyTyped
        // TODO add your handling code here:
        evt.setKeyChar(Character.toUpperCase(evt.getKeyChar()));
        if(t_inc.getText().length()>=13)
            evt.consume();
    }//GEN-LAST:event_t_incKeyTyped

    private void t_no_repKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_no_repKeyTyped
        // TODO add your handling code here:
        evt.setKeyChar(Character.toUpperCase(evt.getKeyChar()));
        if(t_no_rep.getText().length()>=20)
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
        if(t_placas_vehiculo.getText().length()>=8)
            evt.consume();
    }//GEN-LAST:event_t_placas_vehiculoKeyTyped

    private void t_motor_vehiculoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_motor_vehiculoKeyTyped
        // TODO add your handling code here:
        evt.setKeyChar(Character.toUpperCase(evt.getKeyChar()));
        if(t_motor_vehiculo.getText().length()>=15)
            evt.consume();
    }//GEN-LAST:event_t_motor_vehiculoKeyTyped

    private void t_oficinaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_oficinaKeyTyped
        // TODO add your handling code here:
        evt.setKeyChar(Character.toUpperCase(evt.getKeyChar()));
        if(t_oficina.getText().length()>=20)
            evt.consume();
    }//GEN-LAST:event_t_oficinaKeyTyped

    private void t_electronicoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_electronicoKeyTyped
        // TODO add your handling code here:
        evt.setKeyChar(Character.toUpperCase(evt.getKeyChar()));
        if(t_electronico.getText().length()>=200)
            evt.consume();
    }//GEN-LAST:event_t_electronicoKeyTyped

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
                factura=(Factura)session.get(Factura.class, factura.getIdFactura());
                Configuracion config=(Configuracion)session.createCriteria(Configuracion.class).add(Restrictions.eq("nombre", factura.getNombreEmisor())).uniqueResult();
                if(t_xml.getText().compareTo("")==0)
                    t_xml.setText(config.getRfc()+"_"+t_serie_factura.getText()+"_"+t_folio_factura.getText()+"_"+t_rfc.getText()+".xml");
                File xml=new File(ruta+"xml-timbrados/"+t_xml.getText());
                if(xml.exists())//existe en nuestro directorio
                {
                    org.jdom2.Document documentJDOM = new SAXBuilder().build(ruta+"xml-timbrados/"+t_xml.getText());
                    XPathExpression<org.jdom2.Element> xPathExpression = XPathFactory.instance().compile("/hibernate-configuration/session-factory/property", Filters.element());
                    documentJDOM.getContent().remove("documentJDOM");
                    //generaAddenda(ruta+"xml-timbrados/"+t_xml.getText());
                    Desktop.getDesktop().open(xml);
                }
                else//descargar el archivo del SAP
                {
                    if(PAC.compareTo("M")==0)
                    {
                        factura=(Factura)session.get(Factura.class, factura.getIdFactura());
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
                        factura=(Factura)session.get(Factura.class, factura.getIdFactura());
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
                        //progreso.setIndeterminate(true);
                        //progreso.setString("Descargando archivo espere");
                        factura=(Factura)session.get(Factura.class, factura.getIdFactura());
                        Formatos formato = new Formatos(this.user, this.sessionPrograma, factura,configuracion);
                        formato.factura();
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
                factura=(Factura)session.get(Factura.class, factura.getIdFactura());
                Configuracion config=(Configuracion)session.createCriteria(Configuracion.class).add(Restrictions.eq("empresa", factura.getNombreEmisor())).setMaxResults(1).uniqueResult();
                
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
                factura=(Factura)session.get(Factura.class, factura.getIdFactura());
                Configuracion config=(Configuracion)session.createCriteria(Configuracion.class).add(Restrictions.eq("empresa", factura.getNombreEmisor())).setMaxResults(1).uniqueResult();
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
                            factura=(Factura) session.createCriteria(Factura.class).add(Restrictions.eq("FFiscal", idBuscar)).uniqueResult();
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
                    t_datos.setValueAt(((Number)t_descuento.getValue()).doubleValue(), a, 6);
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
            factura = (Factura)session.get(Factura.class, factura.getIdFactura());
            switch(cb_tipo_relacion.getSelectedItem().toString())
            {
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
                factura = (Factura)session.get(Factura.class, factura.getIdFactura());
                DefaultTableModel temp = (DefaultTableModel) t_relacionados.getModel();
                int [] renglones=t_relacionados.getSelectedRows();
                for(int x=0; x<renglones.length; x++)
                {
                    Relacion r_elimina=(Relacion)session.get(Relacion.class, Integer.parseInt(t_relacionados.getValueAt(renglones[x]-x, 3).toString()));
                    Factura actual = r_elimina.getFacturaByIdFactura();
                    Factura anterior =r_elimina.getFacturaByRelacionFactura();
                    if(actual.getOrden()!=null)
                    {
                        Orden ot=actual.getOrden();
                        ot.setNoFactura(anterior.getIdFactura());
                        session.update(ot);
                        
                        actual.setOrden(null);
                        session.update(actual);
                        
                        anterior.setEstatus("POR COBRAR");
                        session.update(anterior);
                    }
                    if(actual.getIdTultitlan()!=null)
                    {
                        Conexion mysql=new Conexion();
                        String respL=mysql.conectarMSQL("red", "04650077", "tbstultitlan.ddns.net", "3306", "mydb");
                        if(respL.compareTo("")==0)
                        {
                            try {
                                ArrayList resultado=mysql.Consulta("select id_orden from orden where no_factura="+actual.getIdFactura());
                                if(resultado.size()>0)
                                {
                                    ArrayList renglon=(ArrayList)resultado.get(0);
                                    int res=mysql.actualiza("update orden set no_factura="+anterior.getIdFactura()+" where id_orden="+renglon.get(0).toString());
                                    actual.setIdTultitlan(null);
                                    anterior.setEstatus("POR COBRAR");
                                    session.update(anterior);
                                    session.update(actual);
                                }
                            }catch(Exception e){
                                e.printStackTrace();
                            }
                            finally{
                                mysql.desconectarMSQL();
                            }
                        }
                    }
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
                int opcion =JOptionPane.showOptionDialog(null, "Selecciona el tipo de documento a ingresar", "Agregar Documento", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, new Object[] {"Ingreso", "Egreso", "Cancelar"}, "Cancelar");
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
                            int op=1;
                            aux=(Factura)session.get(Factura.class, aux.getIdFactura());
                            if(aux.getOrden()!=null)
                            {
                                Orden ot = aux.getOrden();
                                ot.setNoFactura(factura.getIdFactura());
                                session.update(ot);

                                factura = (Factura)session.get(Factura.class, factura.getIdFactura());
                                factura.setOrden(ot);
                                session.update(factura);

                                aux.setEstatus("SUSTITUIDA");
                                session.update(aux);
                            }
                            if(aux.getIdTultitlan()!=null)
                            {
                                Conexion mysql=new Conexion();
                                String respL=mysql.conectarMSQL("red", "04650077", "tbstultitlan.ddns.net", "3306", "mydb");
                                if(respL.compareTo("")==0)
                                {
                                    try {
                                        ArrayList resultado=mysql.Consulta("select id_orden from orden where no_factura="+aux.getIdFactura());
                                        if(resultado.size()>0)
                                        {
                                            ArrayList renglon=(ArrayList)resultado.get(0);
                                            int res=mysql.actualiza("update orden set no_factura="+factura.getIdFactura()+" where id_orden="+renglon.get(0).toString());
                                            factura = (Factura)session.get(Factura.class, factura.getIdFactura());
                                            factura.setIdTultitlan(Integer.parseInt(renglon.get(0).toString()));
                                            session.update(factura);

                                            aux.setEstatus("SUSTITUIDA");
                                            session.update(aux);
                                        }
                                    }catch(Exception e){
                                        e.printStackTrace();
                                    }
                                    finally{
                                        mysql.desconectarMSQL();
                                    }
                                }
                            }
                            Relacion r_nuevo=new Relacion();
                            r_nuevo.setFacturaByIdFactura(factura);
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
                            r_nuevo1.setFacturaByIdFactura(factura);
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
                factura = (Factura)session.get(Factura.class, factura.getIdFactura());

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
    private javax.swing.JComboBox c_area;
    private javax.swing.JComboBox c_estado;
    private javax.swing.JComboBox c_forma_pago;
    private javax.swing.JComboBox c_metodo_pago;
    private javax.swing.JComboBox c_moneda;
    private javax.swing.JComboBox c_pais;
    private javax.swing.JComboBox c_tipo;
    private javax.swing.JComboBox c_tipo_cliente;
    private javax.swing.JComboBox c_tipo_emisor;
    private javax.swing.JComboBox c_tipo_receptor;
    private javax.swing.JComboBox c_tipo_vehiculo;
    private javax.swing.JComboBox cb_no_proveedor;
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
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
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
    private javax.swing.JLabel jLabel78;
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
    private javax.swing.JTextField t_color_vehiculo;
    private javax.swing.JTextField t_correo_emisor;
    private javax.swing.JTextField t_correo_receptor;
    private javax.swing.JTextField t_cp;
    private javax.swing.JTextField t_cuenta_pago;
    private javax.swing.JTable t_datos;
    private javax.swing.JTextField t_descripcion_cfdi;
    private javax.swing.JFormattedTextField t_descuento;
    private javax.swing.JTextField t_electronico;
    private javax.swing.JTable t_facturas;
    private javax.swing.JTextField t_folio_factura;
    private javax.swing.JTextField t_id_cfdi;
    private javax.swing.JTextField t_inc;
    private javax.swing.JFormattedTextField t_iva;
    private javax.swing.JFormattedTextField t_iva1;
    private javax.swing.JTextField t_marca_vehiculo;
    private javax.swing.JTextField t_modelo_vehiculo;
    private javax.swing.JTextField t_motor_vehiculo;
    private javax.swing.JTextField t_municipio;
    private javax.swing.JTextField t_no_exterior;
    private javax.swing.JTextField t_no_rep;
    private javax.swing.JTextField t_nombre_emisor;
    private javax.swing.JTextField t_nombre_receptor;
    private javax.swing.JTextField t_numero;
    private javax.swing.JTextField t_oficina;
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
                factura = (Factura)session.get(Factura.class, factura.getIdFactura()); 
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
                t_descuento.setValue(0);
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
                if(factura.getArea()!=null)
                    c_area.setSelectedItem(factura.getArea());
                t_codigo.setText(""+factura.getIdFactura());
                if(factura.getOrden()!=null)
                {
                    if(factura.getOrden().getPoliza()!=null)
                        t_numero.setText(factura.getOrden().getPoliza().trim());
                    else
                        t_numero.setText("");
                    if(factura.getOrden().getInciso()!=null)
                        t_inc.setText(factura.getOrden().getInciso().trim());
                    else
                        t_inc.setText("");
                    if(factura.getOrden().getNoReporte()!=null)
                        t_no_rep.setText(factura.getOrden().getNoReporte().trim());
                    else
                        t_no_rep.setText("");
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
                        t_inc.setText(factura.getOrdenExterna().getInciso().trim());
                    else
                        t_inc.setText("");
                    if(factura.getOrdenExterna().getNoReporte()!=null)
                        t_no_rep.setText(factura.getOrdenExterna().getNoReporte().trim());
                    else
                        t_no_rep.setText("");
                    if(factura.getOrdenExterna().getSiniestro()!=null)
                        t_siniestro.setText(factura.getOrdenExterna().getSiniestro().trim());
                    else
                        t_siniestro.setText("");
                }
                Configuracion config=(Configuracion)session.createCriteria(Configuracion.class).add(Restrictions.eq("empresa", factura.getNombreEmisor())).setMaxResults(1).uniqueResult();
                c_tipo.setSelectedItem(factura.getTipoPoliza());
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
                c_tipo_receptor.setSelectedItem(factura.getTipoReceptor());
                t_nombre_receptor.setText(factura.getContactoReceptor().trim());
                String[] noProveedor = config.getNoProveedor().split(";");
                cb_no_proveedor.removeAllItems();
                for(int h=0; h<noProveedor.length; h++)
                {
                    cb_no_proveedor.addItem(noProveedor[h]);
                }
                cb_no_proveedor.setSelectedItem(factura.getNoProveedor());
                t_correo_receptor.setText(factura.getCorreoReceptor().trim());
                t_tel_receptor.setText(factura.getTelefonoReceptor().trim());
                if(factura.getOrden()!=null)
                {
                    t_modelo_vehiculo.setText(factura.getOrden().getTipo().getTipoNombre().trim());
                    if(factura.getOrden().getColor()!=null)
                        t_color_vehiculo.setText(factura.getOrden().getColor().trim());
                    else
                        t_color_vehiculo.setText("");
                    t_marca_vehiculo.setText(factura.getOrden().getMarca().getMarcaNombre().trim());
                    t_anio_vehiculo.setText(""+factura.getOrden().getModelo());
                    if(factura.getOrden().getNoSerie()!=null)
                        t_serie_vehiculo.setText(factura.getOrden().getNoSerie().trim());
                    if(factura.getOrden().getNoPlacas()!=null)
                        t_placas_vehiculo.setText(factura.getOrden().getNoPlacas().trim());
                    if(factura.getOrden().getNoMotor()!=null)
                        t_motor_vehiculo.setText(factura.getOrden().getNoMotor().trim());
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
                }
                if(factura.getOrdenExterna()!=null)
                {
                    if(factura.getOrdenExterna().getTipo()!=null)
                        t_modelo_vehiculo.setText(factura.getOrdenExterna().getTipo().getTipoNombre().trim());
                    if(factura.getOrdenExterna().getColor()!=null)
                        t_color_vehiculo.setText(factura.getOrdenExterna().getColor().trim());
                    else
                        t_color_vehiculo.setText("");
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
                }
                t_oficina.setText(factura.getCodigoOficina().trim());
                t_electronico.setText(factura.getFoliosElectronicos().trim());
                //cargar las partidas
                Concepto[] partidas = (Concepto[])session.createCriteria(Concepto.class).
                                        add(Restrictions.eq("factura.idFactura", factura.getIdFactura())).
                                        addOrder(Order.asc("idConcepto")).list().toArray(new Concepto[0]);
                for(int a=0; a<partidas.length; a++)
                {
                    BigDecimal cantidad_lista = new BigDecimal(partidas[a].getCantidad());
                    BigDecimal precio_lista = new BigDecimal(partidas[a].getPrecio());
                    BigDecimal total_lista = precio_lista.multiply(cantidad_lista).setScale(2, BigDecimal.ROUND_HALF_UP);
                    BigDecimal descuento = new BigDecimal(partidas[a].getDescuento()/100).setScale(2, BigDecimal.ROUND_HALF_UP);
                    BigDecimal total = total_lista.subtract(total_lista.multiply(descuento)).setScale(2, BigDecimal.ROUND_HALF_UP);
                    
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
                                cantidad_lista.doubleValue(), 
                                partidas[a].getMedida(), 
                                acentos(descripcion), 
                                cve_producto,
                                precio_lista.doubleValue(),
                                partidas[a].getDescuento(),
                               total.doubleValue()
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
                                        add(Restrictions.eq("facturaByIdFactura.idFactura", factura.getIdFactura())).
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
                    else
                    {
                        if(factura.getSerieExterno()!=null)
                            t_serie_factura.setText(factura.getSerieExterno());
                        else
                            t_serie_factura.setText("");
                    }
                    if(factura.getFolio()!=null)
                        t_folio_factura.setText(factura.getFolio());
                    else
                    {
                        if(factura.getFolioExterno()!=null)
                            t_folio_factura.setText(""+factura.getFolioExterno());
                        else
                            t_folio_factura.setText("");
                    }
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
                        this.habilita(false, false);
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
                habilita(true, false);
                JOptionPane.showMessageDialog(null, "Error al consultar la Factura");
            }
            if(session!=null)
                if(session.isOpen())
                    session.close();
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
        t_codigo.setText("");
        t_numero.setText("");
        t_inc.setText("");
        t_no_rep.setText("");
        t_siniestro.setText("");
        t_nombre_emisor.setText("");
        t_correo_emisor.setText("");
        t_tel_emisor.setText("");
        c_tipo_receptor.setSelectedItem("COORDINADOR");
        t_nombre_receptor.setText("");
        t_correo_receptor.setText("");
        t_tel_receptor.setText("");
        t_modelo_vehiculo.setText("");
        t_color_vehiculo.setText("");
        t_marca_vehiculo.setText("");
        t_anio_vehiculo.setText("");
        t_serie_vehiculo.setText("");
        t_placas_vehiculo.setText("");
        t_motor_vehiculo.setText("");
        t_oficina.setText("");
        t_electronico.setText("");
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
        c_area.setEnabled(edo);
        c_tipo.setEnabled(edo);
        c_tipo_cliente.setEnabled(edo);
        c_tipo_emisor.setEnabled(edo);
        c_tipo_vehiculo.setEnabled(edo);
        //t_codigo.setEnabled(edo);
        t_numero.setEnabled(edo);
        t_inc.setEnabled(edo);
        t_no_rep.setEnabled(edo);
        t_siniestro.setEnabled(edo);
        t_nombre_emisor.setEnabled(edo);
        t_correo_emisor.setEnabled(edo);
        t_tel_emisor.setEnabled(edo);
        c_tipo_receptor.setEnabled(edo);
        t_nombre_receptor.setEnabled(edo);
        t_correo_receptor.setEnabled(edo);
        t_tel_receptor.setEnabled(edo);
        cb_no_proveedor.setEnabled(edo);
        //t_modelo_vehiculo.setEnabled(edo);
        t_color_vehiculo.setEnabled(edo);
        //t_marca_vehiculo.setEnabled(edo);
        t_anio_vehiculo.setEnabled(edo);
        t_serie_vehiculo.setEnabled(edo);
        t_placas_vehiculo.setEnabled(edo);
        t_motor_vehiculo.setEnabled(edo);
        t_oficina.setEnabled(edo);
        t_electronico.setEnabled(edo);
        t_facturas.setEnabled(edo);
        t_descuento.setEnabled(edo);
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
        Session session=HibernateUtil.getSessionFactory().openSession();
        try
        {
            Factura[] fac=(Factura[])or.getFacturas().toArray(new Factura[0]);
            for(int r=0; r<fac.length; r++)
            {
                if(fac[r].getEstadoFactura().compareTo("Pendiente")==0 || fac[r].getEstadoFactura().compareTo("Facturado")==0)
                    return fac[r].getIdFactura();
            }
            return -1;
        }catch(Exception e)
        {
            System.out.println(e);
            return -1;
        }
        finally
        {
            if(session!=null)
                if(session.isOpen())
                    session.close();
        }
    }
    
    public boolean XML_MYSUITE(Orden ord, String nombre)
    {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try
        {
            error="";
            session.beginTransaction().begin();
            factura=(Factura)session.get(Factura.class, factura.getIdFactura());
            Configuracion config=(Configuracion)session.createCriteria(Configuracion.class).add(Restrictions.eq("empresa", factura.getNombreEmisor())).setMaxResults(1).uniqueResult();
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
                                    desc1.setBase(precio_lista);
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
                        if(t_datos.getValueAt(ren, 3).toString().toUpperCase().contains("MANO")==true && t_datos.getValueAt(ren, 3).toString().toUpperCase().contains("OBRA")==true)
                            concepto_ext.setCategoria("MANO DE OBRA");
                        else
                            concepto_ext.setCategoria("MATERIAL");
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
                    TComprobanteEx.DatosComerciales datos_comerciales = new TComprobanteEx.DatosComerciales();
                        datos_comerciales.setRelacionComercial(TRelacionComercial.PROVEEDOR);
                        datos_comerciales.setNumeroDeProveedor("22194");
                        datos_comerciales.setSubAddenda1("48");
                comprobante_ex.setDatosComerciales(datos_comerciales);
                TComprobanteEx.TerminosDePago terminos_de_pago= new TComprobanteEx.TerminosDePago();
                    terminos_de_pago.setMetodoDePago(c_metodo_pago.getSelectedItem().toString());
                comprobante_ex.setTerminosDePago(terminos_de_pago);
                    TComprobanteEx.DatosDeEmbarque datosEmbarque= new TComprobanteEx.DatosDeEmbarque();
                        TDomicilioComercial lugarEmbarque = new TDomicilioComercial();
                            lugarEmbarque.setCodigo("078");
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
                if(t_electronico.getText().compareTo("")!=0)
                {
                TTextoLibre pie= new TTextoLibre();
                String []aux = t_electronico.getText().split(";");
                for(int xp=0; xp<aux.length; xp++)
                    pie.getTexto().add(aux[xp]);
                comprobante_ex.setTextosDePie(pie);
                }
                //MAPFRE
                TMTECabecera mapfre = new TMTECabecera();
                    TMTEContactoEmisor contactoEmisor = new TMTEContactoEmisor();
                        contactoEmisor.setTipoDeContacto(c_tipo_emisor.getSelectedItem().toString().toLowerCase());
                        contactoEmisor.setNombreDePersona(t_nombre_emisor.getText().trim());
                        contactoEmisor.setEMail(t_correo_emisor.getText().trim());
                        contactoEmisor.setTelefono(t_tel_emisor.getText().trim());
                    mapfre.setContactoEmisor(contactoEmisor);
                    TMTEContactoReceptor contactoReceptor= new TMTEContactoReceptor();
                        contactoReceptor.setTipoDeContacto(c_tipo_receptor.getSelectedItem().toString());
                        contactoReceptor.setNombreDePersona(t_nombre_receptor.getText().trim());
                        contactoReceptor.setEMail(t_correo_receptor.getText().trim());
                        contactoReceptor.setTelefono(t_tel_receptor.getText().trim());
                    mapfre.setContactoReceptor(contactoReceptor);
                    TMTEPoliza poliza = new TMTEPoliza();
                        poliza.setTipo(c_tipo.getSelectedItem().toString());
                        poliza.setNumero(t_numero.getText().trim());
                        poliza.setInciso(t_inc.getText());
                        poliza.setNroReporte(t_no_rep.getText().trim());
                        poliza.setSiniestro(t_siniestro.getText().trim());
                    mapfre.setPoliza(poliza);
                    TMTEVehiculo vehiculo = new TMTEVehiculo();
                        vehiculo.setUso(c_tipo_vehiculo.getSelectedItem().toString());
                        vehiculo.setMarca(t_marca_vehiculo.getText().trim());
                        vehiculo.setSubMarca(t_anio_vehiculo.getText().trim());
                        vehiculo.setAnoDeProduccion(new BigInteger(t_anio_vehiculo.getText().trim()));
                        vehiculo.setColor(t_color_vehiculo.getText().trim());
                        vehiculo.setSerie(t_serie_vehiculo.getText().trim());
                        vehiculo.setPlacas(t_placas_vehiculo.getText().trim());
                    mapfre.setVehiculo(vehiculo);
                    mapfre.setOficina("078");
                    
                comprobante_ex.setMapfre(mapfre);
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
        try{
            session.beginTransaction().begin();
            factura=(Factura)session.get(Factura.class, factura.getIdFactura());
            Configuracion config=(Configuracion)session.createCriteria(Configuracion.class).add(Restrictions.eq("empresa", factura.getNombreEmisor())).setMaxResults(1).uniqueResult();
            String serie=config.getSerie();
            double version=config.getVersion();
            int numeroID=0;
            if(factura.getOrden()!=null)
                numeroID=factura.getOrden().getIdOrden();
            if(factura.getOrdenExterna()!=null)
                numeroID=factura.getOrdenExterna().getIdOrden();
            
            switch(config.getPac())
            {
                case "MYSUITE":
                    if(XML_MYSUITE(factura.getOrden(), ruta+"nativos/"+numeroID+"nativo.xml")==true)
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
                        aux.add("");
                        
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
                                        String cadenaCodificada=codificador.EncodeArchivo(new File(ruta+"nativos/"+numeroID+"nativo.xml"));
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
                                            factura = (Factura)session.get(Factura.class, factura.getIdFactura());

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
                                                factura.setAddenda("qualitas");
                                                session.update(factura);
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
                                    factura=(Factura)session.get(Factura.class, factura.getIdFactura());
                                    factura.setSerie(t_facturas.getValueAt(0, 3).toString());
                                    factura.setFolio(t_facturas.getValueAt(0, 4).toString());
                                    factura.setFFiscal(t_facturas.getValueAt(0, 1).toString());
                                    factura.setFechaFiscal(t_facturas.getValueAt(0, 5).toString());
                                    factura.setNombreDocumento(t_facturas.getValueAt(0, 0).toString()+"_"+t_facturas.getValueAt(0, 3).toString()+"_"+t_facturas.getValueAt(0, 4).toString()+"_"+t_rfc.getText());
                                    factura.setEstadoFactura("Facturado");
                                    factura.setEstatus("POR COBRAR");
                                    factura.setPac("M");
                                    factura.setAddenda("qualitas");
                                    session.update(factura);
                                    session.beginTransaction().commit();
                                    if(session.isOpen())
                                        session.close();
                                    habilita(true, false);
                                    progreso.setString("Listo");
                                    progreso.setIndeterminate(false);
                                    guarda();
                                    consulta();
                                    //generaAddenda(ruta+"/xml-timbrados/"+t_facturas.getValueAt(0, 0).toString()+"_"+t_facturas.getValueAt(0, 3).toString()+"_"+t_facturas.getValueAt(0, 4).toString()+"_"+t_rfc.getText()+".xml");
                                    JOptionPane.showMessageDialog(null, "se encontro un registro y se importo la información");
                                    break;
                                default:// se encontro mas de una factura
                                    modelo=(DefaultTableModel)resp.get(1);
                                    t_facturas.setModel(modelo);
                                    habilita(true, false);
                                    progreso.setString("Listo");
                                    progreso.setIndeterminate(false);
                                    formatoTablafactura();
                                    javax.swing.JDialog consulta1 = new javax.swing.JDialog(QUALITAS.this, true);
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
                            Query maximo = session.createSQLQuery("select if(max(folio_externo) is null, 1, max(folio_externo)+1) as folio from factura where PAC='F' and rfc_emisor='"+factura.getRfcEmisor()+"' and serie_externo='"+serie+"';");
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
                                                datos.add(factura.getRfcEmisor()+"_"+serie.toLowerCase()+"_"+folio+"_"+t_rfc.getText()+".xml");//nombre del archivo timbrado

                                                ArrayList guarda=api1.llamarSoapFimbra(datos);
                                                switch(guarda.get(0).toString())
                                                {
                                                    case "1"://Se timbro correcto
                                                        Factura factura1=(Factura)session.get(Factura.class, factura.getIdFactura());
                                                        factura1.setFecha(fecha_factura);
                                                        factura1.setEstadoFactura(guarda.get(1).toString());
                                                        factura1.setEstatus(guarda.get(2).toString());
                                                        factura1.setFFiscal(guarda.get(3).toString());
                                                        factura1.setFechaFiscal(guarda.get(4).toString());
                                                        factura1.setSerieExterno(serie);
                                                        factura1.setFolioExterno(Integer.parseInt(folio));
                                                        factura1.setNombreDocumento(factura.getRfcEmisor()+"_"+serie.toLowerCase()+"_"+folio+"_"+t_rfc.getText());
                                                        factura1.setPac("F");
                                                        factura1.setCertificadoEmisor(certificado[2]);
                                                        factura1.setCertificadoSat(guarda.get(9).toString());
                                                        factura1.setSelloSat(guarda.get(8).toString());
                                                        factura1.setSelloCfdi(sello);
                                                        factura1.setAddenda("qualitas");

                                                        session.update(factura1);
                                                        session.beginTransaction().commit();
                                                        session.beginTransaction().commit();

                                                        if(session.isOpen())
                                                            session.close();
                                                        Formatos formato_pdf = new Formatos(this.user, this.sessionPrograma, factura1,configuracion);
                                                        formato_pdf.factura();
                                                        habilita(true, false);
                                                        progreso.setString("Listo");
                                                        progreso.setIndeterminate(false);
                                                        guarda();
                                                        consulta();
                                                        generaAddenda(ruta+"xml-timbrados/"+factura.getRfcEmisor()+"_"+serie.toLowerCase()+"_"+folio+"_"+t_rfc.getText()+".xml");
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
                            Query maximo = session.createSQLQuery("select if(max(folio_externo) is null, 1, max(folio_externo)+1) as folio from factura where PAC='T' and rfc_emisor='"+factura.getRfcEmisor()+"' and serie_externo='"+serie+"';");
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
                                                datos.add(factura.getRfcEmisor()+"_"+serie.toLowerCase()+"_"+folio+"_"+t_rfc.getText()+".xml");//nombre del archivo timbrado

                                                ArrayList guarda=api1.llamarSoapTimbre(datos);
                                                switch(guarda.get(0).toString())
                                                {
                                                    case "1"://Se timbro correcto
                                                        Factura factura1=(Factura)session.get(Factura.class, factura.getIdFactura());
                                                        factura1.setFecha(fecha_factura);
                                                        factura1.setEstadoFactura(guarda.get(1).toString());
                                                        factura1.setEstatus(guarda.get(2).toString());
                                                        factura1.setFFiscal(guarda.get(3).toString());
                                                        factura1.setFechaFiscal(guarda.get(4).toString());
                                                        factura1.setSerieExterno(serie);
                                                        factura1.setFolioExterno(Integer.parseInt(folio));
                                                        factura1.setNombreDocumento(factura.getRfcEmisor()+"_"+serie.toLowerCase()+"_"+folio+"_"+t_rfc.getText());
                                                        factura1.setPac("T");
                                                        factura1.setCertificadoEmisor(certificado[2]);
                                                        factura1.setCertificadoSat(guarda.get(9).toString());
                                                        factura1.setSelloSat(guarda.get(8).toString());
                                                        factura1.setSelloCfdi(sello);
                                                        factura1.setAddenda("qualitas");

                                                        session.update(factura1);
                                                        session.beginTransaction().commit();
                                                        session.beginTransaction().commit();

                                                        if(session.isOpen())
                                                            session.close();
                                                        Formatos formato_pdf = new Formatos(this.user, this.sessionPrograma, factura1,configuracion);
                                                        formato_pdf.factura();
                                                        habilita(true, false);
                                                        progreso.setString("Listo");
                                                        progreso.setIndeterminate(false);
                                                        guarda();
                                                        consulta();
                                                        generaAddenda(ruta+"xml-timbrados/"+factura.getRfcEmisor()+"_"+serie.toLowerCase()+"_"+folio+"_"+t_rfc.getText()+".xml");
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
            habilita(true, false);
            progreso.setString("Listo");
            progreso.setIndeterminate(false);
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al abrir el archivo de configuración");
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
            factura.setMetodoPago(c_metodo_pago.getSelectedItem().toString());
            factura.setCuentaPago(t_cuenta_pago.getText().trim());
            factura.setFactorCambio(((Number)t_tipo_cambio.getValue()).doubleValue());
            if(c_moneda.getSelectedIndex()!=-1)
                factura.setMoneda(c_moneda.getSelectedItem().toString());

            //Addenda  
            if(c_area.getSelectedIndex()>-1)
                factura.setArea(c_area.getSelectedItem().toString());
            if(c_tipo.getSelectedIndex()!=-1)
                factura.setTipoPoliza(c_tipo.getSelectedItem().toString().toLowerCase());
            if(c_tipo_emisor.getSelectedIndex()!=-1)
                factura.setTipoEmisor(c_tipo_emisor.getSelectedItem().toString());
            factura.setContactoEmisor(t_nombre_emisor.getText().trim());
            factura.setCorreoEmisor(t_correo_emisor.getText().trim());
            factura.setTelefonoEmisor(t_tel_emisor.getText().trim());
            if(c_tipo_receptor.getSelectedIndex()!=-1)
                factura.setTipoReceptor(c_tipo_receptor.getSelectedItem().toString());
            if(cb_no_proveedor.getSelectedIndex()!=-1)
            factura.setNoProveedor(cb_no_proveedor.getSelectedItem().toString());
            factura.setContactoReceptor(t_nombre_receptor.getText().trim());
            factura.setCorreoReceptor(t_correo_receptor.getText().trim());
            factura.setTelefonoReceptor(t_tel_receptor.getText().trim());
            factura.setCodigoOficina(t_oficina.getText().trim());
            factura.setFoliosElectronicos(t_electronico.getText().trim());
            factura.setCondicionesPago(c_forma_pago.getSelectedItem().toString());
            factura.setAddenda("qualitas");
            factura.setIva(Integer.parseInt(t_iva1.getValue().toString()));
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
                ord.setInciso(t_inc.getText().trim());
                ord.setNoReporte(t_no_rep.getText().trim());
                ord.setSiniestro(t_siniestro.getText().trim());
                if(c_tipo_vehiculo.getSelectedIndex()!=-1)
                    ord.setPropietario(c_tipo_vehiculo.getSelectedItem().toString());
                ord.setColor(t_color_vehiculo.getText().trim());
                ord.setModelo(Integer.parseInt(t_anio_vehiculo.getText()));
                ord.setNoSerie(t_serie_vehiculo.getText().trim());
                ord.setNoPlacas(t_placas_vehiculo.getText().trim());
                ord.setNoMotor(t_motor_vehiculo.getText().trim());
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
                session.update(ord);
            }
            if(factura.getOrdenExterna()!=null)
            {
                OrdenExterna ordE=(OrdenExterna)session.get(OrdenExterna.class, factura.getOrdenExterna().getIdOrden());
                ordE.setPoliza(t_numero.getText().trim());
                ordE.setInciso(t_inc.getText().trim());
                ordE.setNoReporte(t_no_rep.getText().trim());
                ordE.setSiniestro(t_siniestro.getText().trim());
                if(c_tipo_vehiculo.getSelectedIndex()!=-1)
                    ordE.setPropietario(c_tipo_vehiculo.getSelectedItem().toString());
                ordE.setColor(t_color_vehiculo.getText().trim());
                if(t_anio_vehiculo.getText().trim().compareTo("")!=0)
                    ordE.setModelo(Integer.parseInt(t_anio_vehiculo.getText()));
                ordE.setNoSerie(t_serie_vehiculo.getText().trim());
                ordE.setNoPlacas(t_placas_vehiculo.getText().trim());
                ordE.setNoMotor(t_motor_vehiculo.getText().trim());
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
            factura=(Factura)session.get(Factura.class, factura.getIdFactura());
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
                comprobante.setTipoDeComprobante("ingreso");
                comprobante.setMetodoDePago(c_metodo_pago.getSelectedItem().toString());
                comprobante.setLugarExpedicion("TOLUCA, ESTADO DE MEXICO");
                comprobante.setSerie(serie);
                comprobante.setFolio(folio);//Falta
                    Comprobante.Emisor emisor= objeto.createComprobanteEmisor();
                    emisor.setRfc(factura.getRfcEmisor());//Cambiar por config.getRfc()
                    if(factura.getNombreEmisor().compareTo("VICENTE PEREZ VILLAR.")==0)
                        emisor.setNombre(acentos("VICENTE PEREZ VILLAR"));
                    else
                        emisor.setNombre(acentos(factura.getNombreEmisor()));
                        TUbicacionFiscal miUbicacion =objeto.createTUbicacionFiscal();
                        miUbicacion.setCalle(acentos(factura.getCalleEmisor()));
                        miUbicacion.setNoExterior(factura.getNumeroExteriorEmisor());
                        miUbicacion.setColonia(acentos(factura.getColoniaEmisor()));
                        miUbicacion.setMunicipio(acentos(factura.getMunicipioEmisor()));
                        miUbicacion.setEstado(acentos(factura.getEstadoEmisor()));
                        miUbicacion.setPais(acentos(factura.getPaisEmisor()));
                        miUbicacion.setCodigoPostal(factura.getCpEmisor());
                        Comprobante.Emisor.RegimenFiscal miRegimen= objeto.createComprobanteEmisorRegimenFiscal();
                        miRegimen.setRegimen("REGIMEN GENERAL DE LEY DE PERSONAS MORALES");
                    emisor.getRegimenFiscal().add(miRegimen);
                    emisor.setDomicilioFiscal(miUbicacion);
                comprobante.setEmisor(emisor);
                    Comprobante.Receptor receptor=objeto.createComprobanteReceptor();
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
                    Comprobante.Impuestos impuestos =objeto.createComprobanteImpuestos();
                    BigDecimal porc=valorIva.divide(new BigDecimal("100.0")).setScale(2, BigDecimal.ROUND_HALF_UP);
                    BigDecimal monto=big_sub_total.multiply(porc).setScale(2, BigDecimal.ROUND_HALF_UP);
                    impuestos.setTotalImpuestosTrasladados(monto.setScale(2, BigDecimal.ROUND_HALF_UP));
                        Comprobante.Impuestos.Traslados misTrasladados=objeto.createComprobanteImpuestosTraslados();
                            Comprobante.Impuestos.Traslados.Traslado traslado= objeto.createComprobanteImpuestosTrasladosTraslado();
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
                comprobante33.setTipoDeComprobante(CTipoDeComprobante.I);
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
                    finkok33.Comprobante.CfdiRelacionados relacion=new finkok33.Comprobante.CfdiRelacionados();
                    relacion.setTipoRelacion(cb_tipo_relacion.getSelectedItem().toString());
                    List <finkok33.Comprobante.CfdiRelacionados.CfdiRelacionado> lista_relacion=relacion.getCfdiRelacionado();
                    for(int w=0; w<t_relacionados.getRowCount(); w++)
                    {
                        finkok33.Comprobante.CfdiRelacionados.CfdiRelacionado elemento=new finkok33.Comprobante.CfdiRelacionados.CfdiRelacionado();
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
                            BigDecimal big_descuento=new BigDecimal(t_datos.getValueAt(ren, 6).toString());
                            BigDecimal big_porciento_dectuento=big_descuento.divide(new BigDecimal("100"));
                            //cantidades de lista
                            BigDecimal big_cantidad=new BigDecimal(t_datos.getValueAt(ren, 1).toString());
                            BigDecimal big_precio_lista=new BigDecimal(t_datos.getValueAt(ren, 5).toString());
                            BigDecimal big_total_lista=big_precio_lista.multiply(big_cantidad);
                            big_total_bruto = big_total_bruto.add(big_total_lista);
                            BigDecimal big_total_descuento=big_precio_lista.multiply(big_porciento_dectuento);
                            //cantidades netas
                            BigDecimal big_general_descuento=big_cantidad.multiply(big_total_descuento);
                            BigDecimal big_precio_neto=big_precio_lista.subtract(big_total_descuento);
                            BigDecimal big_total_neto=big_precio_neto.multiply(big_cantidad);
                            big_sub_total = big_sub_total.add(big_total_neto);

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
                BigDecimal tot=big_sub_total.add(big_iva_total.setScale(2, BigDecimal.ROUND_HALF_UP));
                comprobante33.setTotal(tot.setScale(2, BigDecimal.ROUND_HALF_UP));

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
    public boolean generaAddenda(String ruta){
        try
        {
            org.jdom2.Document doc = new SAXBuilder().build(ruta);
            Element rootNode = doc.getRootElement();
            List list = rootNode.getContent();
            for ( int i = 0; i < list.size(); i++ )
            {
                Content elementos = (Content) list.get(i);
                if(elementos.getCType()==Content.CType.Element)
                {
                    Element aux=(Element)elementos;
                    if(aux.getName().compareToIgnoreCase("Complemento")==0)
                    {
                        Element addenda = new Element("Addenda",Constantes.COMPROBANTE_PREFIX_NAMESPACE,Constantes.COMPROBANTE_NAMESPACE_URI);
                        Element ECFD = new Element("ECFD","","");
                        ECFD.setAttribute("version", "1.0");
                            Element documento = new Element("Documento","","");
                            documento.setAttribute("ID", "T33"+factura.getFolioExterno());
                                Element Encabezado = new Element("Encabezado","","");
                                    Element idDoc = new Element("IdDoc","","");
                                        Element NroAprob = new Element("NroAprob","","");
                                        NroAprob.setText("00000");
                                        idDoc.addContent((Content)NroAprob);
                                        Element AnoAprob = new Element("AnoAprob","","");
                                        AnoAprob.setText("00000");
                                        idDoc.addContent((Content)AnoAprob);
                                        Element Tipo = new Element("Tipo","","");
                                        Tipo.setText("33");
                                        idDoc.addContent((Content)Tipo);
                                        Element Folio = new Element("Folio","","");
                                        Folio.setText(""+factura.getFolioExterno());
                                        idDoc.addContent((Content)Folio);
                                        Element Estado = new Element("Estado","","");
                                        Estado.setText("ORIGINAL");
                                        idDoc.addContent((Content)Estado);
                                        Element NumeroInterno = new Element("NumeroInterno","","");
                                        if(factura.getRfcEmisor().compareToIgnoreCase("ITP150326MP1")==0)
                                            NumeroInterno.setText("50");
                                        else
                                            NumeroInterno.setText("48");
                                        idDoc.addContent((Content)NumeroInterno);
                                        Element FechaEmis = new Element("FechaEmis","","");
                                        FechaEmis.setText(factura.getFechaFiscal().substring(0, 19));
                                        idDoc.addContent((Content)FechaEmis);
                                        Element FormaPago = new Element("FormaPago","","");
                                        FormaPago.setText(c_forma_pago.getSelectedItem().toString());
                                        idDoc.addContent((Content)FormaPago);
                                        Element Area = new Element("Area","","");
                                            Element IdArea = new Element("IdArea","","");
                                            IdArea.setText("001");
                                            Area.addContent((Content)IdArea);
                                            Element IdRevision = new Element("IdRevision","","");
                                            IdRevision.setText("003");
                                            Area.addContent((Content)IdRevision);
                                        idDoc.addContent((Content)Area);
                                    Encabezado.addContent((Content)idDoc);
                                    Element ExEmisor = new Element("ExEmisor","","");
                                        Element RFCEmisor = new Element("RFCEmisor","","");
                                        RFCEmisor.setText(factura.getRfcEmisor());
                                        ExEmisor.addContent((Content)RFCEmisor);
                                        Element NmbEmisor = new Element("NmbEmisor","","");
                                        NmbEmisor.setText(factura.getNombreEmisor());
                                        ExEmisor.addContent((Content)NmbEmisor);
                                        Element CodigoExEmisor = new Element("CodigoExEmisor","","");
                                            Element TpoCdgIntEmisor = new Element("TpoCdgIntEmisor","","");
                                            TpoCdgIntEmisor.setText("EXT");
                                            CodigoExEmisor.addContent((Content)TpoCdgIntEmisor);
                                            Element CdgIntEmisor = new Element("CdgIntEmisor","","");
                                            CdgIntEmisor.setText(factura.getNoProveedor());
                                            /*switch(factura.getRfcEmisor()){
                                                case "SET0806255W2":
                                                    CdgIntEmisor.setText("22194");
                                                    break;
                                                case "TBS160622L39":
                                                    CdgIntEmisor.setText("28166");
                                                    break;
                                                case "ITP150326MP1":
                                                    CdgIntEmisor.setText("25367");
                                                    break;    
                                                default:
                                                    CdgIntEmisor.setText("");
                                                    break;
                                            }*/
                                            CodigoExEmisor.addContent((Content)CdgIntEmisor);
                                        ExEmisor.addContent((Content)CodigoExEmisor);
                                        Element DomFiscal = new Element("DomFiscal","","");
                                            Element Calle_emisor = new Element("Calle","","");
                                            Calle_emisor.setText(factura.getCalleEmisor());
                                            DomFiscal.addContent((Content)Calle_emisor);
                                            Element NroExterior_emisor = new Element("NroExterior","","");
                                            NroExterior_emisor.setText(factura.getNumeroExteriorEmisor());
                                            DomFiscal.addContent((Content)NroExterior_emisor);
                                            Element Colonia_emisor = new Element("Colonia","","");
                                            Colonia_emisor.setText(factura.getColoniaEmisor());
                                            DomFiscal.addContent((Content)Colonia_emisor);
                                            Element Localidad_emisor = new Element("Localidad","","");
                                            Localidad_emisor.setText(factura.getEstadoEmisor());
                                            DomFiscal.addContent((Content)Localidad_emisor);
                                            Element Municipio_emisor = new Element("Municipio","","");
                                            Municipio_emisor.setText(factura.getMunicipioEmisor());
                                            DomFiscal.addContent((Content)Municipio_emisor);
                                            Element Estado_emisor = new Element("Estado","","");
                                            Estado_emisor.setText(factura.getEstadoEmisor());
                                            DomFiscal.addContent((Content)Estado_emisor);
                                            Element Pais_emisor = new Element("Pais","","");
                                            Pais_emisor.setText(factura.getPaisEmisor());
                                            DomFiscal.addContent((Content)Pais_emisor);
                                            Element CodigoPostal_emisor = new Element("CodigoPostal","","");
                                            CodigoPostal_emisor.setText(factura.getCpEmisor());
                                            DomFiscal.addContent((Content)CodigoPostal_emisor);
                                        ExEmisor.addContent((Content)DomFiscal);
                                        Element ContactoEmisor = new Element("ContactoEmisor","","");
                                            Element Tipo_emisor = new Element("Tipo","","");
                                            Tipo_emisor.setText("matriz");
                                            ContactoEmisor.addContent((Content)Tipo_emisor);
                                            Element Nombre_emisor = new Element("Nombre","","");
                                            Nombre_emisor.setText(factura.getContactoEmisor());
                                            ContactoEmisor.addContent((Content)Nombre_emisor);
                                            Element eMail_emisor = new Element("eMail","","");
                                            eMail_emisor.setText(factura.getCorreoEmisor());
                                            ContactoEmisor.addContent((Content)eMail_emisor);
                                            Element Telefono_emisor = new Element("Telefono","","");
                                            Telefono_emisor.setText(factura.getTelefonoEmisor());
                                            ContactoEmisor.addContent((Content)Telefono_emisor);
                                        ExEmisor.addContent((Content)ContactoEmisor);
                                    Encabezado.addContent((Content)ExEmisor);
                                    Element ExReceptor = new Element("ExReceptor","","");
                                        Element RFCRecep = new Element("RFCRecep","","");
                                        RFCRecep.setText(factura.getRfcReceptor());
                                        ExReceptor.addContent((Content)RFCRecep);
                                        Element NmbRecep = new Element("NmbRecep","","");
                                        NmbRecep.setText(factura.getNombreReceptor());
                                        ExReceptor.addContent((Content)NmbRecep);
                                        Element DomFiscalRcp = new Element("DomFiscalRcp","","");
                                            Element Calle_receptor = new Element("Calle","","");
                                            Calle_receptor.setText(factura.getCalleReceptor());
                                            DomFiscalRcp.addContent((Content)Calle_receptor);
                                            Element NroExterior_receptor = new Element("NroExterior","","");
                                            NroExterior_receptor.setText(factura.getNumeroExteriorReceptor());
                                            DomFiscalRcp.addContent((Content)NroExterior_receptor);
                                            Element Colonia_receptor = new Element("Colonia","","");
                                            Colonia_receptor.setText(factura.getColoniaReceptor());
                                            DomFiscalRcp.addContent((Content)Colonia_receptor);
                                            Element Localidad_receptor = new Element("Localidad","","");
                                            Localidad_receptor.setText(factura.getEstadoReceptor());
                                            DomFiscalRcp.addContent((Content)Localidad_receptor);
                                            Element Municipio_receptor = new Element("Municipio","","");
                                            Municipio_receptor.setText(factura.getMunicipioReceptor());
                                            DomFiscalRcp.addContent((Content)Municipio_receptor);
                                            Element Estado_receptor = new Element("Estado","","");
                                            Estado_receptor.setText(factura.getEstadoReceptor());
                                            DomFiscalRcp.addContent((Content)Estado_receptor);
                                            Element Pais_receptor = new Element("Pais","","");
                                            Pais_receptor.setText(factura.getPaisReceptor());
                                            DomFiscalRcp.addContent((Content)Pais_receptor);
                                            Element CodigoPostal_receptor = new Element("CodigoPostal","","");
                                            CodigoPostal_receptor.setText(factura.getCpReceptor());
                                            DomFiscalRcp.addContent((Content)CodigoPostal_receptor);
                                        ExReceptor.addContent((Content)DomFiscalRcp);
                                        Element LugarRecep = new Element("LugarRecep","","");
                                            Element Calle_recep = new Element("Calle","","");
                                            Calle_recep.setText(factura.getCalleReceptor());
                                            LugarRecep.addContent((Content)Calle_recep);
                                            Element NroExterior_recep = new Element("NroExterior","","");
                                            NroExterior_recep.setText(factura.getNumeroExteriorReceptor());
                                            LugarRecep.addContent((Content)NroExterior_recep);
                                            Element Colonia_recep = new Element("Colonia","","");
                                            Colonia_recep.setText(factura.getColoniaReceptor());
                                            LugarRecep.addContent((Content)Colonia_recep);
                                            Element Localidad_recep = new Element("Localidad","","");
                                            Localidad_recep.setText(factura.getEstadoReceptor());
                                            LugarRecep.addContent((Content)Localidad_recep);
                                            Element Municipio_recep = new Element("Municipio","","");
                                            Municipio_recep.setText(factura.getMunicipioReceptor());
                                            LugarRecep.addContent((Content)Municipio_recep);
                                            Element Estado_recep = new Element("Estado","","");
                                            Estado_recep.setText(factura.getEstadoReceptor());
                                            LugarRecep.addContent((Content)Estado_recep);
                                            Element Pais_recep = new Element("Pais","","");
                                            Pais_recep.setText(factura.getPaisReceptor());
                                            LugarRecep.addContent((Content)Pais_recep);
                                            Element CodigoPostal_recep = new Element("CodigoPostal","","");
                                            CodigoPostal_recep.setText(factura.getCpReceptor());
                                            LugarRecep.addContent((Content)CodigoPostal_recep);
                                        ExReceptor.addContent((Content)LugarRecep);
                                        Element ContactoReceptor = new Element("ContactoReceptor","","");
                                            Element Tipo_receptor = new Element("Tipo","","");
                                            Tipo_receptor.setText("coordinador");
                                            ContactoReceptor.addContent((Content)Tipo_receptor);
                                            Element Nombre_receptor = new Element("Nombre","","");
                                            Nombre_receptor.setText(factura.getContactoReceptor());
                                            ContactoReceptor.addContent((Content)Nombre_receptor);
                                            Element eMail_receptor = new Element("eMail","","");
                                            eMail_receptor.setText(factura.getCorreoReceptor());
                                            ContactoReceptor.addContent((Content)eMail_receptor);
                                            Element Telefono_receptor = new Element("Telefono","","");
                                            Telefono_receptor.setText(factura.getTelefonoReceptor());
                                            ContactoReceptor.addContent((Content)Telefono_receptor);
                                        ExReceptor.addContent((Content)ContactoReceptor);
                                    Encabezado.addContent((Content)ExReceptor);
                                    Element Totales = new Element("Totales","","");
                                        Element Moneda = new Element("Moneda","","");
                                        Moneda.setText(factura.getMoneda());
                                        Totales.addContent((Content)Moneda);
                                        Element SubTotal = new Element("SubTotal","","");
                                        SubTotal.setText(t_subtotal.getValue().toString());
                                        Totales.addContent((Content)SubTotal);
                                        Element MntBase = new Element("MntBase","","");
                                        MntBase.setText(t_subtotal.getValue().toString());
                                        Totales.addContent((Content)MntBase);
                                        Element MntImp = new Element("MntImp","","");
                                        MntImp.setText(t_iva.getValue().toString());
                                        Totales.addContent((Content)MntImp);
                                        Element VlrPagar = new Element("VlrPagar","","");
                                        VlrPagar.setText(t_total.getValue().toString());
                                        Totales.addContent((Content)VlrPagar);
                                        Element VlrPalabras = new Element("VlrPalabras","","");
                                        DecimalFormat formatoPorcentaje = new DecimalFormat("#,##0.000");
                                        VlrPalabras.setText(numeroLetra.convertNumberToLetter(formatoPorcentaje.format(((Number)t_total.getValue()).doubleValue()))+" M.N.");
                                        Totales.addContent((Content)VlrPalabras);
                                    Encabezado.addContent((Content)Totales);
                                    Element ExImpuestos = new Element("ExImpuestos","","");
                                        Element TipoImp = new Element("TipoImp","","");
                                        TipoImp.setText("IVA");
                                        ExImpuestos.addContent((Content)TipoImp);
                                        Element TasaImp = new Element("TasaImp","","");
                                        TasaImp.setText(t_iva1.getValue().toString()+".00");
                                        ExImpuestos.addContent((Content)TasaImp);
                                        Element MontoImp = new Element("MontoImp","","");
                                        MontoImp.setText(t_iva.getValue().toString());
                                        ExImpuestos.addContent((Content)MontoImp);
                                    Encabezado.addContent((Content)ExImpuestos);
                                    Element Poliza = new Element("Poliza","","");
                                        Element Tipo_poliza = new Element("Tipo","","");
                                        Tipo_poliza.setText(c_tipo.getSelectedItem().toString());
                                        Poliza.addContent((Content)Tipo_poliza);
                                        Element Numero_poliza = new Element("Numero","","");
                                        Numero_poliza.setText(t_numero.getText());
                                        Poliza.addContent((Content)Numero_poliza);
                                        Element INC_poliza = new Element("INC","","");
                                        INC_poliza.setText(t_inc.getText());
                                        Poliza.addContent((Content)INC_poliza);
                                        Element TpoCliente_poliza = new Element("TpoCliente","","");
                                        switch(c_tipo_cliente.getSelectedIndex())
                                        {
                                            case 0:
                                                TpoCliente_poliza.setText("0");
                                                break;
                                            case 1:
                                                TpoCliente_poliza.setText("1");
                                                break;
                                        }
                                        Poliza.addContent((Content)TpoCliente_poliza);
                                        Element NroReporte_poliza = new Element("NroReporte","","");
                                        NroReporte_poliza.setText(t_no_rep.getText());
                                        Poliza.addContent((Content)NroReporte_poliza);
                                        Element NroSint_poliza = new Element("NroSint","","");
                                        NroSint_poliza.setText(t_siniestro.getText());
                                        Poliza.addContent((Content)NroSint_poliza);
                                    Encabezado.addContent((Content)Poliza);
                                    Element Vehiculo = new Element("Vehiculo","","");
                                        Element Tipo_vehiculo = new Element("Tipo","","");
                                        Tipo_vehiculo.setText(c_tipo_vehiculo.getSelectedItem().toString());
                                        Vehiculo.addContent((Content)Tipo_vehiculo);
                                        Element Marca_vehiculo = new Element("Marca","","");
                                        Marca_vehiculo.setText(t_marca_vehiculo.getText());
                                        Vehiculo.addContent((Content)Marca_vehiculo);
                                        Element Modelo_vehiculo = new Element("Modelo","","");
                                        Modelo_vehiculo.setText(t_modelo_vehiculo.getText());
                                        Vehiculo.addContent((Content)Modelo_vehiculo);
                                        Element Ano_vehiculo = new Element("Ano","","");
                                        Ano_vehiculo.setText(t_anio_vehiculo.getText());
                                        Vehiculo.addContent((Content)Ano_vehiculo);
                                        Element Color_vehiculo = new Element("Color","","");
                                        Color_vehiculo.setText(t_color_vehiculo.getText());
                                        Vehiculo.addContent((Content)Color_vehiculo);
                                        Element NroSerie_vehiculo = new Element("NroSerie","","");
                                        NroSerie_vehiculo.setText(t_serie_vehiculo.getText());
                                        Vehiculo.addContent((Content)NroSerie_vehiculo);
                                        Element Placa_vehiculo = new Element("Placa","","");
                                        Placa_vehiculo.setText(t_placas_vehiculo.getText());
                                        Vehiculo.addContent((Content)Placa_vehiculo);
                                    Encabezado.addContent((Content)Vehiculo);
                                documento.addContent((Content)Encabezado);
                             //BigDecimal big_total_bruto = new BigDecimal(0.0d);
                             BigDecimal big_sub_total = new BigDecimal(0);
                             BigDecimal big_mo_total = new BigDecimal(0);
                             BigDecimal big_ref_total = new BigDecimal(0);
                             for(int x=0; x<t_datos.getRowCount(); x++)
                             {
                                //descuento
                                BigDecimal big_descuento=new BigDecimal(t_datos.getValueAt(x, 6).toString());
                                BigDecimal big_porciento_dectuento=big_descuento.divide(new BigDecimal("100"));
                                //cantidades de lista
                                BigDecimal big_cantidad=new BigDecimal(t_datos.getValueAt(x, 1).toString());
                                BigDecimal big_precio_lista=new BigDecimal(t_datos.getValueAt(x, 5).toString());
                                //BigDecimal big_total_lista=big_precio_lista.multiply(big_cantidad);
                                //big_total_bruto = big_total_bruto.add(big_total_lista);
                                //cantidades netas
                                BigDecimal big_precio_neto=big_precio_lista.subtract(big_precio_lista.multiply(big_porciento_dectuento));
                                BigDecimal big_total_neto=big_precio_neto.multiply(big_cantidad);
                                
                                System.out.println("Precio Lista:"+t_datos.getValueAt(x, 5).toString());
                                
                                big_sub_total = big_sub_total.add(big_total_neto);
                                
                                if(t_datos.getValueAt(x, 4).toString().compareTo("2750")==0 || t_datos.getValueAt(x, 4).toString().compareTo("19171")==0)
                                    big_mo_total = big_mo_total.add(big_total_neto);
                                else
                                    big_ref_total = big_ref_total.add(big_total_neto);
                                
                                
                                Element Detalle = new Element("Detalle","","");
                                    Element NroLinDet = new Element("NroLinDet","","");
                                    int num=x+1;
                                    NroLinDet.setText(""+num);
                                    Detalle.addContent((Content)NroLinDet);
                                    Element DscLang = new Element("DscLang","","");
                                    DscLang.setText("ES");
                                    Detalle.addContent((Content)DscLang);
                                    Element DscItem = new Element("DscItem","","");
                                    DscItem.setText(t_datos.getValueAt(x, 3).toString());
                                    Detalle.addContent((Content)DscItem);
                                    Element QtyItem = new Element("QtyItem","","");
                                    QtyItem.setText(t_datos.getValueAt(x, 1).toString());
                                    Detalle.addContent((Content)QtyItem);
                                    Element UnmdItem = new Element("UnmdItem","","");
                                    UnmdItem.setText(t_datos.getValueAt(x, 2).toString());
                                    Detalle.addContent((Content)UnmdItem);
                                    Element PrcNetoItem = new Element("PrcNetoItem","","");
                                    PrcNetoItem.setText(""+big_precio_neto.doubleValue());
                                    Detalle.addContent((Content)PrcNetoItem);
                                    Element MontoNetoItem = new Element("MontoNetoItem","","");
                                    MontoNetoItem.setText(""+big_total_neto.doubleValue());
                                    Detalle.addContent((Content)MontoNetoItem);
                                documento.addContent((Content)Detalle);
                             }   
                                Element TimeStamp = new Element("TimeStamp","","");
                                    TimeStamp.setText(factura.getFechaFiscal().substring(0, 19));
                                documento.addContent((Content)TimeStamp);
                            Element personalizado = new Element("Personalizados","","");
                                Element campoString = new Element("campoString","","");
                                campoString.setAttribute("name", "montoManoObra");
                                campoString.setText(""+big_mo_total.doubleValue());
                                personalizado.addContent((Content)campoString);
                                Element campoString1 = new Element("campoString","","");
                                campoString1.setAttribute("name", "montoRefacciones");
                                campoString1.setText(""+big_ref_total.doubleValue());
                                personalizado.addContent((Content)campoString1);
                                Element campoString2 = new Element("campoString","","");
                                campoString2.setAttribute("name", "fechaFiniquito");
                                campoString2.setText(factura.getFechaFiscal().substring(0, 19));
                                personalizado.addContent((Content)campoString2);
                                Element campoString3 = new Element("campoString","","");
                                campoString3.setAttribute("name", "fechaEntregaRefacciones");
                                campoString3.setText(factura.getFechaFiscal().substring(0, 19));
                                personalizado.addContent((Content)campoString3);
                                Element campoString4 = new Element("campoString","","");
                                campoString4.setAttribute("name", "oficinaEntregaFactura");
                                campoString4.setText("078");
                                personalizado.addContent((Content)campoString4);
                                String[] campos = t_electronico.getText().split(";");
                                for(int r=0; r<campos.length; r++)
                                {
                                    Element campoString5 = new Element("campoString","","");
                                    campoString5.setAttribute("name", "folioElectronico");
                                    campoString5.setText(campos[r]);
                                    personalizado.addContent((Content)campoString5);
                                }
                                Element campoString6 = new Element("campoString","","");
                                campoString6.setAttribute("name", "montoDeducible");
                                campoString6.setText("0.00");
                                personalizado.addContent((Content)campoString6);
                                Element campoString7 = new Element("campoString","","");
                                campoString7.setAttribute("name", "bancoDepositoDeducible");
                                campoString7.setText("X");
                                personalizado.addContent((Content)campoString7);
                                Element campoString8 = new Element("campoString","","");
                                campoString8.setAttribute("name", "fechaDepositoDeducible");
                                campoString8.setText("000-00-00");
                                personalizado.addContent((Content)campoString8);
                                Element campoString9 = new Element("campoString","","");
                                campoString9.setAttribute("name", "folioFicha_ReferenciaDeducible");
                                personalizado.addContent((Content)campoString9);
                                Element campoString10 = new Element("campoString","","");
                                campoString10.setAttribute("name", "montoDemerito_Recupero");
                                campoString10.setText("0.00");
                                personalizado.addContent((Content)campoString10);
                                Element campoString11 = new Element("campoString","","");
                                campoString11.setAttribute("name", "bancoDepositoDemerito_Recupero");
                                campoString11.setText("X");
                                personalizado.addContent((Content)campoString11);
                                Element campoString12 = new Element("campoString","","");
                                campoString12.setAttribute("name", "fechaDepositoDemerito_Recupero");
                                campoString12.setText("0000-00-00");
                                personalizado.addContent((Content)campoString12);
                                Element campoString13 = new Element("campoString","","");
                                campoString13.setAttribute("name", "folioFicha_ReferenciaDemerito");
                                personalizado.addContent((Content)campoString13);
                                Element campoString14 = new Element("campoString","","");
                                campoString14.setAttribute("name", "UUID");
                                campoString14.setText(factura.getFFiscal());
                                personalizado.addContent((Content)campoString14);
                                Element campoString15 = new Element("campoString","","");
                                campoString15.setAttribute("name", "Default1");
                                campoString15.setText("string");
                                personalizado.addContent((Content)campoString15);
                                Element campoString16 = new Element("campoString","","");
                                campoString16.setAttribute("name", "Default2");
                                campoString16.setText("string");
                                personalizado.addContent((Content)campoString16);
                        ECFD.addContent((Content)documento);
                        ECFD.addContent((Content)personalizado);
                        addenda.addContent(ECFD);
                        rootNode.addContent((Content)addenda);
                        i = list.size();
                    }
                }
            }
            XMLOutputter outputter = new XMLOutputter( Format.getPrettyFormat() );
            FileOutputStream red=new FileOutputStream (ruta);
            outputter.output(doc, red);
            red.close();
        }catch ( IOException io ) {
            System.out.println( io.getMessage() );
            return false;
        }catch ( JDOMException jdomex ) {
            System.out.println( jdomex.getMessage() );
            return false;
        }
        return true;
    }
    public void consultaFacturaElectronica(File  arch){
        Session session = HibernateUtil.getSessionFactory().openSession();
        try{
            session.beginTransaction().begin();
            factura=(Factura)session.get(Factura.class, factura.getIdFactura());
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
                                            factura=(Factura)session.get(Factura.class, factura.getIdFactura());
                                            factura.setFecha(fecha_factura);
                                            factura.setEstadoFactura(guarda.get(1).toString());
                                            factura.setEstatus(guarda.get(2).toString());
                                            factura.setFFiscal(guarda.get(3).toString());
                                            factura.setFechaFiscal(guarda.get(4).toString());
                                            factura.setSerieExterno(serie);
                                            factura.setFolioExterno(Integer.parseInt(folio));
                                            factura.setNombreDocumento(factura.getRfcEmisor()+"_"+serie+"_"+folio+"_"+t_rfc.getText());
                                            factura.setPac("T");
                                            session.update(factura);
                                            session.beginTransaction().commit();

                                            progreso.setString("Listo");
                                            progreso.setIndeterminate(false);
                                            if(session.isOpen())
                                                session.close();
                                            guarda();
                                            consulta();
                                            generaAddenda(ruta+"xml-timbrados/"+factura.getRfcEmisor()+"_"+serie.toLowerCase()+"_"+folio+"_"+t_rfc.getText()+".xml");
                                            arch=new File(ruta+"xml-timbrados/"+factura.getRfcEmisor()+"_"+serie+"_"+folio+"_"+t_rfc.getText()+".xml");
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
