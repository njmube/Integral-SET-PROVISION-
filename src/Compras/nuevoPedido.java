/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Compras;

import Compania.buscaCompania;
import Ejemplar.altaEjemplar;
import Ejemplar.buscaEjemplar;
import Hibernate.Util.HibernateUtil;
import Hibernate.entidades.Compania;
import Hibernate.entidades.Marca;
import Marca.buscaMarca;
import Hibernate.entidades.Configuracion;
import Hibernate.entidades.Empleado;
import Hibernate.entidades.Orden;
import Hibernate.entidades.OrdenExterna;
import Hibernate.entidades.Partida;
import Hibernate.entidades.PartidaExterna;
import Hibernate.entidades.Pedido;
import Hibernate.entidades.Proveedor;
import Hibernate.entidades.Tipo;
import Hibernate.entidades.Usuario;
import Proveedor.buscaProveedor;
import Servicios.buscaOrden;
import Tipo.buscaTipo;
import Empleados.buscaEmpleado;
import Hibernate.entidades.Catalogo;
import Hibernate.entidades.Ejemplar;
import Hibernate.entidades.Item;
import Integral.EnviaCorreo;
import Integral.ExtensionFileFilter;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import Integral.FormatoTabla;
import Integral.Herramientas;
import Integral.calendario;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import Integral.FormatoEditor;
import Integral.FormatoEditorTexto;
import Integral.Ftp;
import Integral.HorizontalBarUI;
import Integral.Render1;
import Integral.VerticalBarUI;
import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.Properties;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import org.apache.commons.net.ftp.FTPFile;
/***
 * @author ESPECIALIZADO TOLUCA
 */
public class nuevoPedido extends javax.swing.JPanel {
    private Usuario usrAut, usrAut1, usrAut2; 
    private Usuario usrA=null;   
    private Usuario usrM=null;   
    private String orden;  
    Usuario usr;
    int entro=0, x=0;
    FormatoTabla formato;
    public Orden orden_act=null;
    public Proveedor prov_act=null;
    public Proveedor provf_act=null;
    public Partida [] part_act=null;
    MyModel model;
    String sessionPrograma="";
    Herramientas h;
    int menu;
    File archivo=null;
    int configuracion=1;
    JFileChooser selector;
    String ruta;
    EnviaCorreo miCorreo;
    String[] columnas = new String [] {"#","R. Valua","N° Parte","Folio","Descripción","Medida","Plazo","Cantidad","Costo c/u", "Tipo", "Total"};
    //false, false, false, false, false, false, true, true, true, false
    /**
    * Creates new form nuevoPedido
    */
    public nuevoPedido(String ord, Usuario usuario, String ses, int op, int configuracion, String carpeta){
        initComponents();
        p_centro.getVerticalScrollBar().setUI(new VerticalBarUI());
        ruta=carpeta;
        p_centro.getHorizontalScrollBar().setUI(new HorizontalBarUI());
        menu=op;
        this.configuracion=configuracion;
        orden=ord;
        usr=usuario;
        sessionPrograma=ses;
        formato = new FormatoTabla();
        model=new MyModel(0, columnas);
        t_datos.setModel(model);
        t_datos.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        formatoTabla();
        obtenTipo();
        agregaArchivo("Agregar Soporte", b_archivo);
        
        selector=new JFileChooser();
        selector.setFileFilter(new ExtensionFileFilter("Documentos(PDF)", new String[] { "PDF" }));
        selector.setAcceptAllFileFilterUsed(false);
        selector.setMultiSelectionEnabled(false);
    }
   
    public void obtenTipo(){
        Session session = HibernateUtil.getSessionFactory().openSession();
        try{
            //configuracion tipo de cambio
            DecimalFormat df = new DecimalFormat("#.0000");
            Configuracion config = (Configuracion)session.get(Configuracion.class, configuracion);
            if(config.getTipoCambio()>0.0){
                t_cambio.setEditable(false);
                t_cambio.setText(""+df.format(config.getTipoCambio()));
            }else
                t_cambio.setEditable(true);
        }catch(Exception e)
            {
                e.printStackTrace();
            }
            finally
            {
                if(session.isOpen()==true)
                    session.close();
            }
    }
    public void formatoTabla()
    {
        Color c1 = new java.awt.Color(2, 135, 242);   
        for(int x=0; x<t_datos.getColumnModel().getColumnCount(); x++)
            t_datos.getColumnModel().getColumn(x).setHeaderRenderer(new Render1(c1));
        tabla_tamaños();
        t_datos.setShowVerticalLines(true);
        t_datos.setShowHorizontalLines(true);
        
        FormatoEditor fe=new FormatoEditor();
        t_datos.setDefaultEditor(Double.class, fe);
        
        FormatoEditorTexto fs=new FormatoEditorTexto();
        t_datos.setDefaultEditor(String.class, fs);
        
        t_datos.setDefaultRenderer(String.class, formato); 
        t_datos.setDefaultRenderer(Double.class, formato); 
        t_datos.setDefaultRenderer(Integer.class, formato);
        t_datos.setDefaultRenderer(Boolean.class, formato);
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        autorizarCosto = new javax.swing.JDialog();
        jPanel5 = new javax.swing.JPanel();
        t_contra = new javax.swing.JPasswordField();
        jLabel2 = new javax.swing.JLabel();
        t_user = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        b_autorizar = new javax.swing.JButton();
        jLabel14 = new javax.swing.JLabel();
        medida = new javax.swing.JComboBox();
        numeros = new javax.swing.JComboBox();
        numeros1 = new javax.swing.JComboBox();
        autorizar1 = new javax.swing.JDialog();
        jPanel10 = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        t_user1 = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        t_clave1 = new javax.swing.JPasswordField();
        b2 = new javax.swing.JButton();
        jLabel17 = new javax.swing.JLabel();
        autorizar2 = new javax.swing.JDialog();
        jPanel11 = new javax.swing.JPanel();
        jLabel18 = new javax.swing.JLabel();
        t_user2 = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        t_clave2 = new javax.swing.JPasswordField();
        b3 = new javax.swing.JButton();
        jLabel20 = new javax.swing.JLabel();
        autorizar3 = new javax.swing.JDialog();
        jPanel12 = new javax.swing.JPanel();
        jLabel22 = new javax.swing.JLabel();
        t_user3 = new javax.swing.JTextField();
        jLabel23 = new javax.swing.JLabel();
        t_clave3 = new javax.swing.JPasswordField();
        b4 = new javax.swing.JButton();
        jLabel24 = new javax.swing.JLabel();
        t_id_comprador = new javax.swing.JTextField();
        t_id_aseguradora = new javax.swing.JTextField();
        t_clave = new javax.swing.JTextField();
        t_proveedor = new javax.swing.JTextField();
        autorizar4 = new javax.swing.JDialog();
        jPanel13 = new javax.swing.JPanel();
        jLabel21 = new javax.swing.JLabel();
        t_user4 = new javax.swing.JTextField();
        jLabel25 = new javax.swing.JLabel();
        t_clave4 = new javax.swing.JPasswordField();
        b5 = new javax.swing.JButton();
        jLabel26 = new javax.swing.JLabel();
        tipo = new javax.swing.JComboBox();
        centro = new javax.swing.JPanel();
        p_centro = new javax.swing.JScrollPane();
        t_datos = new javax.swing.JTable();
        jPanel9 = new javax.swing.JPanel();
        t_busca = new javax.swing.JTextField();
        b_busca = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        t_cambio = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        p_abajo = new javax.swing.JPanel();
        b_guardar = new javax.swing.JButton();
        b_pedido = new javax.swing.JButton();
        b_nuevo_pedido = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        l_total = new javax.swing.JLabel();
        t_total = new javax.swing.JFormattedTextField();
        t_IVA = new javax.swing.JFormattedTextField();
        l_iva = new javax.swing.JLabel();
        l_subtotal = new javax.swing.JLabel();
        t_subtotal = new javax.swing.JFormattedTextField();
        l_notas = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        t_notas = new javax.swing.JTextArea();
        l_busca1 = new javax.swing.JLabel();
        cb_pago = new javax.swing.JComboBox();
        b_menos = new javax.swing.JButton();
        b_mas = new javax.swing.JButton();
        b_archivo = new javax.swing.JButton();
        p_arriba = new javax.swing.JPanel();
        p_interno_centro = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        c_tipo = new javax.swing.JComboBox();
        t_pedido = new javax.swing.JTextField();
        l_fecha = new javax.swing.JLabel();
        t_fecha = new javax.swing.JTextField();
        t_folio_externo = new javax.swing.JTextField();
        l_pedido1 = new javax.swing.JLabel();
        b_calendario = new javax.swing.JButton();
        t_plazo = new javax.swing.JTextField();
        t_nombre_comprador = new javax.swing.JTextField();
        b_comprador = new javax.swing.JButton();
        jPanel8 = new javax.swing.JPanel();
        t_orden = new javax.swing.JTextField();
        b_orden = new javax.swing.JButton();
        t_tipo = new javax.swing.JTextField();
        b_tipo = new javax.swing.JButton();
        t_marca = new javax.swing.JTextField();
        b_marca = new javax.swing.JButton();
        l_modelo = new javax.swing.JLabel();
        t_modelo = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        t_siniestro = new javax.swing.JTextField();
        l_asegurado = new javax.swing.JLabel();
        t_asegurado = new javax.swing.JTextField();
        t_aseguradora = new javax.swing.JTextField();
        b_aseguradora = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        b_proveedor = new javax.swing.JButton();
        l_nombre_aseguradora = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        l_nombre = new javax.swing.JLabel();
        t_nombre = new javax.swing.JTextField();
        t_direccion = new javax.swing.JTextField();
        l_direccion = new javax.swing.JLabel();
        l_colonia = new javax.swing.JLabel();
        t_colonia = new javax.swing.JTextField();
        l_poblacion = new javax.swing.JLabel();
        t_poblacion = new javax.swing.JTextField();
        t_cp = new javax.swing.JTextField();
        l_cp = new javax.swing.JLabel();
        l_rfc = new javax.swing.JLabel();
        t_rfc = new javax.swing.JTextField();
        b_proveedorf = new javax.swing.JButton();

        autorizarCosto.setModalExclusionType(null);
        autorizarCosto.setModalityType(java.awt.Dialog.ModalityType.APPLICATION_MODAL);

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "Autorización de costos mayores"));

        t_contra.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                t_contraActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Arial", 0, 10)); // NOI18N
        jLabel2.setText("Contraseña:");

        t_user.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                t_userActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Arial", 0, 10)); // NOI18N
        jLabel1.setText("Usuario:");

        b_autorizar.setFont(new java.awt.Font("Arial", 0, 10)); // NOI18N
        b_autorizar.setText("Autorizar");
        b_autorizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_autorizarActionPerformed(evt);
            }
        });
        b_autorizar.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                b_autorizarFocusLost(evt);
            }
        });

        jLabel14.setText("Autorizar un costo mayor al autorizado");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(b_autorizar)
                        .addGroup(jPanel5Layout.createSequentialGroup()
                            .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(t_user)
                                .addComponent(t_contra, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addComponent(jLabel14))
                .addContainerGap(18, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel14)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(t_user, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(t_contra, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(b_autorizar)
                .addGap(23, 23, 23))
        );

        javax.swing.GroupLayout autorizarCostoLayout = new javax.swing.GroupLayout(autorizarCosto.getContentPane());
        autorizarCosto.getContentPane().setLayout(autorizarCostoLayout);
        autorizarCostoLayout.setHorizontalGroup(
            autorizarCostoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        autorizarCostoLayout.setVerticalGroup(
            autorizarCostoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        medida.setFont(new java.awt.Font("Dialog", 0, 9)); // NOI18N
        medida.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "PZAS", "GAL", "LTS", "MTS", "CMS", "MMS", "GRS", "MLS", "KGS", "HRS", "MIN", "KIT", "FT", "LB", "JGO", "NA" }));

        numeros.setFont(new java.awt.Font("Dialog", 0, 9)); // NOI18N
        numeros.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Cancelar", "Eliminar", "Buscar", "Nuevo" }));
        numeros.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                numerosFocusLost(evt);
            }
        });

        numeros1.setFont(new java.awt.Font("Dialog", 0, 9)); // NOI18N
        numeros1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                numeros1FocusLost(evt);
            }
        });

        autorizar1.setTitle("Autorización");
        autorizar1.setModalExclusionType(null);
        autorizar1.setModalityType(java.awt.Dialog.ModalityType.APPLICATION_MODAL);

        jPanel10.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel15.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel15.setText("Usuario:");

        t_user1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                t_user1ActionPerformed(evt);
            }
        });

        jLabel16.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel16.setText("contraseña:");

        t_clave1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                t_clave1ActionPerformed(evt);
            }
        });

        b2.setBackground(new java.awt.Color(2, 135, 242));
        b2.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        b2.setForeground(new java.awt.Color(254, 254, 254));
        b2.setText("Autorizar");
        b2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b2ActionPerformed(evt);
            }
        });

        jLabel17.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel17.setText("Autorizar por encima del 70%");

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(b2)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel16)
                            .addComponent(jLabel15))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(t_user1)
                            .addComponent(t_clave1, javax.swing.GroupLayout.DEFAULT_SIZE, 173, Short.MAX_VALUE)))
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addComponent(jLabel17)
                        .addGap(47, 47, 47)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel17)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 41, Short.MAX_VALUE)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(t_user1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16)
                    .addComponent(t_clave1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(b2)
                .addContainerGap())
        );

        autorizar1.getContentPane().add(jPanel10, java.awt.BorderLayout.CENTER);

        autorizar2.setTitle("Autorización");
        autorizar2.setModalExclusionType(null);
        autorizar2.setModalityType(java.awt.Dialog.ModalityType.APPLICATION_MODAL);

        jPanel11.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel18.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel18.setText("Usuario:");

        t_user2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                t_user2ActionPerformed(evt);
            }
        });

        jLabel19.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel19.setText("contraseña:");

        t_clave2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                t_clave2ActionPerformed(evt);
            }
        });

        b3.setBackground(new java.awt.Color(2, 135, 242));
        b3.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        b3.setForeground(new java.awt.Color(254, 254, 254));
        b3.setText("Autorizar");
        b3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b3ActionPerformed(evt);
            }
        });

        jLabel20.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel20.setText("Autorizar por encima del 100%");

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(b3)
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel19)
                            .addComponent(jLabel18))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(t_user2)
                            .addComponent(t_clave2, javax.swing.GroupLayout.DEFAULT_SIZE, 173, Short.MAX_VALUE)))
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addComponent(jLabel20)
                        .addGap(47, 47, 47)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel20)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 41, Short.MAX_VALUE)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel18)
                    .addComponent(t_user2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel19)
                    .addComponent(t_clave2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(b3)
                .addContainerGap())
        );

        autorizar2.getContentPane().add(jPanel11, java.awt.BorderLayout.CENTER);

        autorizar3.setTitle("Autorización");
        autorizar3.setModalExclusionType(null);
        autorizar3.setModalityType(java.awt.Dialog.ModalityType.APPLICATION_MODAL);

        jPanel12.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel22.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel22.setText("Usuario:");

        t_user3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                t_user3ActionPerformed(evt);
            }
        });

        jLabel23.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel23.setText("contraseña:");

        t_clave3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                t_clave3ActionPerformed(evt);
            }
        });

        b4.setBackground(new java.awt.Color(2, 135, 242));
        b4.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        b4.setForeground(new java.awt.Color(254, 254, 254));
        b4.setText("Autorizar");
        b4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b4ActionPerformed(evt);
            }
        });

        jLabel24.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel24.setText("Autorizar Adicionales");

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(b4)
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel23)
                            .addComponent(jLabel22))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(t_user3)
                            .addComponent(t_clave3, javax.swing.GroupLayout.DEFAULT_SIZE, 173, Short.MAX_VALUE)))
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addComponent(jLabel24)
                        .addGap(47, 47, 47)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel24)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 41, Short.MAX_VALUE)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel22)
                    .addComponent(t_user3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel23)
                    .addComponent(t_clave3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(b4)
                .addContainerGap())
        );

        autorizar3.getContentPane().add(jPanel12, java.awt.BorderLayout.CENTER);

        t_id_comprador.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        t_id_comprador.setDisabledTextColor(new java.awt.Color(2, 38, 253));
        t_id_comprador.setEnabled(false);

        t_id_aseguradora.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        t_id_aseguradora.setDisabledTextColor(new java.awt.Color(2, 38, 253));
        t_id_aseguradora.setEnabled(false);

        t_clave.setEditable(false);
        t_clave.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(0, 0, 0)));
        t_clave.setDisabledTextColor(new java.awt.Color(2, 38, 253));
        t_clave.setEnabled(false);

        t_proveedor.setEditable(false);
        t_proveedor.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        t_proveedor.setDisabledTextColor(new java.awt.Color(2, 38, 253));
        t_proveedor.setEnabled(false);

        autorizar4.setTitle("Autorización");
        autorizar4.setModalExclusionType(null);
        autorizar4.setModalityType(java.awt.Dialog.ModalityType.APPLICATION_MODAL);

        jPanel13.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel21.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel21.setText("Usuario:");

        t_user4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                t_user4ActionPerformed(evt);
            }
        });

        jLabel25.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel25.setText("contraseña:");

        t_clave4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                t_clave4ActionPerformed(evt);
            }
        });

        b5.setBackground(new java.awt.Color(2, 135, 242));
        b5.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        b5.setForeground(new java.awt.Color(254, 254, 254));
        b5.setText("Autorizar");
        b5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b5ActionPerformed(evt);
            }
        });

        jLabel26.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel26.setText("Autorizar Compras Fuera de Tiempo");

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(b5)
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel25)
                            .addComponent(jLabel21))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(t_user4)
                            .addComponent(t_clave4, javax.swing.GroupLayout.DEFAULT_SIZE, 173, Short.MAX_VALUE)))
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addComponent(jLabel26)
                        .addGap(23, 23, 23)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel26)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 41, Short.MAX_VALUE)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel21)
                    .addComponent(t_user4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel25)
                    .addComponent(t_clave4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(b5)
                .addContainerGap())
        );

        autorizar4.getContentPane().add(jPanel13, java.awt.BorderLayout.CENTER);

        tipo.setFont(new java.awt.Font("Dialog", 0, 9)); // NOI18N
        tipo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "-", "ori", "nal", "des" }));

        setBackground(new java.awt.Color(255, 255, 255));
        setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "Nuevo Pedido", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Arial", 1, 12))); // NOI18N
        setLayout(new java.awt.BorderLayout());

        centro.setLayout(new java.awt.BorderLayout());

        p_centro.setBackground(new java.awt.Color(255, 255, 255));
        p_centro.setBorder(null);

        t_datos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "#", "R. Valua", "N° Parte", "Folio", "Descripción", "Medida", "Plazo", "Cantidad", "Costo c/u", "Tipo", "Total"
            }
        ));
        t_datos.setColumnSelectionAllowed(true);
        t_datos.setFillsViewportHeight(true);
        t_datos.setSelectionMode(javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        t_datos.getTableHeader().setReorderingAllowed(false);
        t_datos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                t_datosMouseClicked(evt);
            }
        });
        p_centro.setViewportView(t_datos);
        t_datos.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);

        centro.add(p_centro, java.awt.BorderLayout.CENTER);

        jPanel9.setBackground(new java.awt.Color(2, 135, 242));

        t_busca.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        t_busca.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                t_buscaActionPerformed(evt);
            }
        });
        t_busca.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                t_buscaKeyTyped(evt);
            }
        });

        b_busca.setBackground(new java.awt.Color(2, 135, 242));
        b_busca.setToolTipText("Busca un pedido");
        b_busca.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_buscaActionPerformed(evt);
            }
        });

        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Tipo Cambio:");

        t_cambio.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        t_cambio.setText("0.0");
        t_cambio.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(0, 0, 0)));
        t_cambio.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                t_cambioFocusLost(evt);
            }
        });
        t_cambio.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                t_cambioKeyTyped(evt);
            }
        });

        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Buscar:");

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(t_busca, javax.swing.GroupLayout.PREFERRED_SIZE, 193, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(b_busca, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 681, Short.MAX_VALUE)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(t_cambio, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(7, 7, 7)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel5)
                        .addComponent(t_cambio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(b_busca, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(t_busca, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel6)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        centro.add(jPanel9, java.awt.BorderLayout.NORTH);

        add(centro, java.awt.BorderLayout.CENTER);

        p_abajo.setBackground(new java.awt.Color(2, 135, 242));

        b_guardar.setText("Guardar");
        b_guardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_guardarActionPerformed(evt);
            }
        });

        b_pedido.setText("Pedido");
        b_pedido.setEnabled(false);
        b_pedido.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_pedidoActionPerformed(evt);
            }
        });

        b_nuevo_pedido.setText("Nuevo");
        b_nuevo_pedido.setToolTipText("Nuevo Pedido");
        b_nuevo_pedido.setEnabled(false);
        b_nuevo_pedido.setMaximumSize(new java.awt.Dimension(32, 8));
        b_nuevo_pedido.setMinimumSize(new java.awt.Dimension(32, 8));
        b_nuevo_pedido.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_nuevo_pedidoActionPerformed(evt);
            }
        });

        jPanel3.setBackground(new java.awt.Color(2, 135, 242));
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        l_total.setFont(new java.awt.Font("Arial", 0, 9)); // NOI18N
        l_total.setForeground(new java.awt.Color(255, 255, 255));
        l_total.setText("Total:");
        jPanel3.add(l_total, new org.netbeans.lib.awtextra.AbsoluteConstraints(31, 40, -1, -1));

        t_total.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        t_total.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.00"))));
        t_total.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        t_total.setText("0.00");
        t_total.setDisabledTextColor(new java.awt.Color(2, 38, 253));
        t_total.setEnabled(false);
        t_total.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        jPanel3.add(t_total, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 40, 88, -1));

        t_IVA.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        t_IVA.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.00"))));
        t_IVA.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        t_IVA.setText("0.00");
        t_IVA.setDisabledTextColor(new java.awt.Color(2, 38, 253));
        t_IVA.setEnabled(false);
        t_IVA.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        jPanel3.add(t_IVA, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 20, 88, -1));

        l_iva.setFont(new java.awt.Font("Arial", 0, 9)); // NOI18N
        l_iva.setForeground(new java.awt.Color(255, 255, 255));
        l_iva.setText("I.V.A.:");
        jPanel3.add(l_iva, new org.netbeans.lib.awtextra.AbsoluteConstraints(28, 20, -1, -1));

        l_subtotal.setFont(new java.awt.Font("Arial", 0, 9)); // NOI18N
        l_subtotal.setForeground(new java.awt.Color(255, 255, 255));
        l_subtotal.setText("Subtotal:");
        jPanel3.add(l_subtotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 5, -1, -1));

        t_subtotal.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        t_subtotal.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.00"))));
        t_subtotal.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        t_subtotal.setText("0.00");
        t_subtotal.setDisabledTextColor(new java.awt.Color(2, 38, 253));
        t_subtotal.setEnabled(false);
        t_subtotal.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        jPanel3.add(t_subtotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 0, 88, -1));

        l_notas.setBackground(new java.awt.Color(254, 254, 254));
        l_notas.setFont(new java.awt.Font("Arial", 0, 10)); // NOI18N
        l_notas.setForeground(new java.awt.Color(255, 255, 255));
        l_notas.setText("Notas:");

        t_notas.setColumns(20);
        t_notas.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        t_notas.setRows(3);
        jScrollPane2.setViewportView(t_notas);

        l_busca1.setBackground(new java.awt.Color(255, 255, 255));
        l_busca1.setFont(new java.awt.Font("Arial", 0, 10)); // NOI18N
        l_busca1.setForeground(new java.awt.Color(255, 255, 255));
        l_busca1.setText("Tipo de Pago:");

        cb_pago.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "SELECCIONAR", "CREDITO", "CONTADO", "EFECTIVO", "CHEQUE" }));

        b_menos.setIcon(new ImageIcon("imagenes/boton_menos.png"));
        b_menos.setMnemonic('2');
        b_menos.setToolTipText("Elimina la partida seleccionada (ALT+2)");
        b_menos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_menosActionPerformed(evt);
            }
        });

        b_mas.setIcon(new ImageIcon("imagenes/boton_mas.png"));
        b_mas.setMnemonic('1');
        b_mas.setToolTipText("Agrega una partida (ALT+1)");
        b_mas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_masActionPerformed(evt);
            }
        });

        b_archivo.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        b_archivo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_archivoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout p_abajoLayout = new javax.swing.GroupLayout(p_abajo);
        p_abajo.setLayout(p_abajoLayout);
        p_abajoLayout.setHorizontalGroup(
            p_abajoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(p_abajoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(p_abajoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(p_abajoLayout.createSequentialGroup()
                        .addComponent(b_guardar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(b_nuevo_pedido, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(b_pedido))
                    .addGroup(p_abajoLayout.createSequentialGroup()
                        .addComponent(b_mas, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(b_menos, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(l_busca1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cb_pago, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 168, Short.MAX_VALUE)
                .addComponent(b_archivo, javax.swing.GroupLayout.PREFERRED_SIZE, 193, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(l_notas)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 305, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        p_abajoLayout.setVerticalGroup(
            p_abajoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(p_abajoLayout.createSequentialGroup()
                .addGroup(p_abajoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(p_abajoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(l_busca1)
                        .addComponent(cb_pago, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(p_abajoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(b_mas, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(b_menos, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(p_abajoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(b_guardar)
                    .addComponent(b_nuevo_pedido, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(b_pedido))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(p_abajoLayout.createSequentialGroup()
                .addGroup(p_abajoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(p_abajoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(b_archivo, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(l_notas))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        add(p_abajo, java.awt.BorderLayout.SOUTH);

        p_arriba.setBackground(new java.awt.Color(254, 254, 254));
        p_arriba.setLayout(new java.awt.BorderLayout());

        p_interno_centro.setBackground(new java.awt.Color(254, 254, 254));
        p_interno_centro.setLayout(new java.awt.GridLayout(1, 0));

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setLayout(new java.awt.BorderLayout());

        jPanel7.setBackground(new java.awt.Color(254, 254, 254));
        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "Pedido", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Arial", 1, 11))); // NOI18N

        jLabel3.setFont(new java.awt.Font("Arial", 0, 10)); // NOI18N
        jLabel3.setText("Pedido:");

        c_tipo.setFont(new java.awt.Font("Droid Sans", 0, 10)); // NOI18N
        c_tipo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Interno", "Externo", "Adicional", "Inventario" }));
        c_tipo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                c_tipoActionPerformed(evt);
            }
        });

        t_pedido.setEditable(false);
        t_pedido.setBackground(new java.awt.Color(255, 255, 255));
        t_pedido.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        t_pedido.setDisabledTextColor(new java.awt.Color(2, 38, 253));
        t_pedido.setEnabled(false);

        l_fecha.setFont(new java.awt.Font("Arial", 0, 10)); // NOI18N
        l_fecha.setText("Fecha:");

        t_fecha.setEditable(false);
        t_fecha.setText("DD-MM-YYYY HH:MM:SS");
        t_fecha.setToolTipText("fecha de pedido");
        t_fecha.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        t_fecha.setDisabledTextColor(new java.awt.Color(2, 38, 253));
        t_fecha.setEnabled(false);

        t_folio_externo.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        t_folio_externo.setDisabledTextColor(new java.awt.Color(2, 38, 253));
        t_folio_externo.setEnabled(false);

        l_pedido1.setFont(new java.awt.Font("Arial", 0, 10)); // NOI18N
        l_pedido1.setText("Folio externo:");

        b_calendario.setBackground(new java.awt.Color(2, 135, 242));
        b_calendario.setText("Plazo:");
        b_calendario.setToolTipText("Calendario");
        b_calendario.setEnabled(false);
        b_calendario.setMaximumSize(new java.awt.Dimension(32, 8));
        b_calendario.setMinimumSize(new java.awt.Dimension(32, 8));
        b_calendario.setPreferredSize(new java.awt.Dimension(32, 8));
        b_calendario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_calendarioActionPerformed(evt);
            }
        });

        t_plazo.setEditable(false);
        t_plazo.setBackground(new java.awt.Color(255, 255, 255));
        t_plazo.setText("DD-MM-YYYY");
        t_plazo.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(0, 0, 0)));
        t_plazo.setDisabledTextColor(new java.awt.Color(2, 38, 253));
        t_plazo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                t_plazoActionPerformed(evt);
            }
        });

        t_nombre_comprador.setEditable(false);
        t_nombre_comprador.setBackground(new java.awt.Color(255, 255, 255));
        t_nombre_comprador.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(0, 0, 0)));
        t_nombre_comprador.setDisabledTextColor(new java.awt.Color(2, 38, 253));

        b_comprador.setBackground(new java.awt.Color(2, 135, 242));
        b_comprador.setText("Empleado:");
        b_comprador.setToolTipText("Busca un proveedor");
        b_comprador.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_compradorActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(t_pedido, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(c_tipo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(l_pedido1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(t_folio_externo, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(l_fecha)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(t_fecha, javax.swing.GroupLayout.DEFAULT_SIZE, 115, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                        .addComponent(b_calendario, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(t_plazo, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(b_comprador)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(t_nombre_comprador)))
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(t_pedido, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(25, 25, 25))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(c_tipo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(l_pedido1)
                                .addComponent(t_folio_externo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(l_fecha)
                                .addComponent(t_fecha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(b_calendario, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(t_plazo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(b_comprador, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(t_nombre_comprador, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.add(jPanel7, java.awt.BorderLayout.NORTH);

        jPanel8.setBackground(new java.awt.Color(254, 254, 254));
        jPanel8.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "Orden", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Arial", 1, 11))); // NOI18N

        t_orden.setEditable(false);
        t_orden.setBackground(new java.awt.Color(255, 255, 255));
        t_orden.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(0, 0, 0)));
        t_orden.setDisabledTextColor(new java.awt.Color(2, 38, 253));

        b_orden.setBackground(new java.awt.Color(2, 135, 242));
        b_orden.setText("Orden:");
        b_orden.setToolTipText("Busca una orden");
        b_orden.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_ordenActionPerformed(evt);
            }
        });

        t_tipo.setEditable(false);
        t_tipo.setBackground(new java.awt.Color(255, 255, 255));
        t_tipo.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(0, 0, 0)));
        t_tipo.setDisabledTextColor(new java.awt.Color(2, 38, 253));

        b_tipo.setBackground(new java.awt.Color(2, 135, 242));
        b_tipo.setText("Tipo:");
        b_tipo.setToolTipText("Busca un tipo");
        b_tipo.setEnabled(false);
        b_tipo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_tipoActionPerformed(evt);
            }
        });

        t_marca.setEditable(false);
        t_marca.setBackground(new java.awt.Color(255, 255, 255));
        t_marca.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(0, 0, 0)));
        t_marca.setDisabledTextColor(new java.awt.Color(2, 38, 253));

        b_marca.setBackground(new java.awt.Color(2, 135, 242));
        b_marca.setText("Marca:");
        b_marca.setToolTipText("Consultar marcas");
        b_marca.setEnabled(false);
        b_marca.setMaximumSize(new java.awt.Dimension(32, 8));
        b_marca.setMinimumSize(new java.awt.Dimension(32, 8));
        b_marca.setPreferredSize(new java.awt.Dimension(28, 24));
        b_marca.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_marcaActionPerformed(evt);
            }
        });

        l_modelo.setFont(new java.awt.Font("Arial", 0, 10)); // NOI18N
        l_modelo.setText("Modelo:");

        t_modelo.setEditable(false);
        t_modelo.setBackground(new java.awt.Color(255, 255, 255));
        t_modelo.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(0, 0, 0)));
        t_modelo.setDisabledTextColor(new java.awt.Color(2, 38, 253));
        t_modelo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                t_modeloKeyTyped(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Arial", 0, 10)); // NOI18N
        jLabel4.setText("Siniestro:");

        t_siniestro.setEditable(false);
        t_siniestro.setBackground(new java.awt.Color(255, 255, 255));
        t_siniestro.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(0, 0, 0)));
        t_siniestro.setDisabledTextColor(new java.awt.Color(2, 38, 253));
        t_siniestro.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                t_siniestroKeyTyped(evt);
            }
        });

        l_asegurado.setFont(new java.awt.Font("Arial", 0, 10)); // NOI18N
        l_asegurado.setText("Asegurado:");

        t_asegurado.setEditable(false);
        t_asegurado.setBackground(new java.awt.Color(255, 255, 255));
        t_asegurado.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(0, 0, 0)));
        t_asegurado.setDisabledTextColor(new java.awt.Color(2, 38, 253));

        t_aseguradora.setEditable(false);
        t_aseguradora.setBackground(new java.awt.Color(255, 255, 255));
        t_aseguradora.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(0, 0, 0)));
        t_aseguradora.setDisabledTextColor(new java.awt.Color(2, 38, 253));

        b_aseguradora.setBackground(new java.awt.Color(2, 135, 242));
        b_aseguradora.setText("Compañia:");
        b_aseguradora.setToolTipText("Busca un tipo");
        b_aseguradora.setEnabled(false);
        b_aseguradora.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_aseguradoraActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(b_orden)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(t_orden, javax.swing.GroupLayout.DEFAULT_SIZE, 83, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(b_tipo, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(t_tipo, javax.swing.GroupLayout.DEFAULT_SIZE, 43, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(b_marca, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(t_marca, javax.swing.GroupLayout.DEFAULT_SIZE, 46, Short.MAX_VALUE)
                        .addGap(38, 38, 38)
                        .addComponent(l_modelo)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(t_modelo, javax.swing.GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addGap(3, 3, 3)
                        .addComponent(t_siniestro, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(l_asegurado)
                        .addGap(3, 3, 3)
                        .addComponent(t_asegurado))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(b_aseguradora)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(t_aseguradora)))
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(b_tipo, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(t_tipo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(b_orden, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(t_orden, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(l_modelo)
                            .addComponent(t_modelo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(b_marca, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(t_marca, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(t_siniestro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(l_asegurado)
                    .addComponent(t_asegurado, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(b_aseguradora, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(t_aseguradora, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.add(jPanel8, java.awt.BorderLayout.CENTER);

        p_interno_centro.add(jPanel2);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setLayout(new java.awt.BorderLayout());

        jPanel4.setBackground(new java.awt.Color(254, 254, 254));
        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "Proveedor", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Arial", 1, 11))); // NOI18N

        b_proveedor.setBackground(new java.awt.Color(2, 135, 242));
        b_proveedor.setIcon(new ImageIcon("imagenes/buscar.png"));
        b_proveedor.setToolTipText("Busca proveedor");
        b_proveedor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_proveedorActionPerformed(evt);
            }
        });

        l_nombre_aseguradora.setFont(new java.awt.Font("Arial", 0, 10)); // NOI18N
        l_nombre_aseguradora.setText("SELECCIONE UN PROVEEDOR");
        l_nombre_aseguradora.setEnabled(false);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(l_nombre_aseguradora, javax.swing.GroupLayout.DEFAULT_SIZE, 497, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(b_proveedor, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(b_proveedor, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(3, 3, 3)
                .addComponent(l_nombre_aseguradora, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel4, java.awt.BorderLayout.NORTH);

        jPanel6.setBackground(new java.awt.Color(254, 254, 254));
        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Arial", 1, 11)), "Facturar a", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Arial", 1, 11))); // NOI18N

        l_nombre.setFont(new java.awt.Font("Arial", 0, 10)); // NOI18N
        l_nombre.setText("Nombre:");

        t_nombre.setEditable(false);
        t_nombre.setBackground(new java.awt.Color(255, 255, 255));
        t_nombre.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(0, 0, 0)));
        t_nombre.setDisabledTextColor(new java.awt.Color(2, 38, 253));

        t_direccion.setEditable(false);
        t_direccion.setBackground(new java.awt.Color(255, 255, 255));
        t_direccion.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(0, 0, 0)));
        t_direccion.setDisabledTextColor(new java.awt.Color(2, 38, 253));

        l_direccion.setFont(new java.awt.Font("Arial", 0, 10)); // NOI18N
        l_direccion.setText("Dirección:");

        l_colonia.setFont(new java.awt.Font("Arial", 0, 10)); // NOI18N
        l_colonia.setText("Colonia:");

        t_colonia.setEditable(false);
        t_colonia.setBackground(new java.awt.Color(255, 255, 255));
        t_colonia.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(0, 0, 0)));
        t_colonia.setDisabledTextColor(new java.awt.Color(2, 38, 253));

        l_poblacion.setFont(new java.awt.Font("Arial", 0, 10)); // NOI18N
        l_poblacion.setText("Población:");

        t_poblacion.setEditable(false);
        t_poblacion.setBackground(new java.awt.Color(255, 255, 255));
        t_poblacion.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(0, 0, 0)));
        t_poblacion.setDisabledTextColor(new java.awt.Color(2, 38, 253));

        t_cp.setEditable(false);
        t_cp.setBackground(new java.awt.Color(255, 255, 255));
        t_cp.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(0, 0, 0)));
        t_cp.setDisabledTextColor(new java.awt.Color(2, 38, 253));

        l_cp.setFont(new java.awt.Font("Arial", 0, 10)); // NOI18N
        l_cp.setText("C.P.:");

        l_rfc.setFont(new java.awt.Font("Arial", 0, 10)); // NOI18N
        l_rfc.setText("R.F.C.:");

        t_rfc.setEditable(false);
        t_rfc.setBackground(new java.awt.Color(255, 255, 255));
        t_rfc.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(0, 0, 0)));
        t_rfc.setDisabledTextColor(new java.awt.Color(2, 38, 253));

        b_proveedorf.setBackground(new java.awt.Color(2, 135, 242));
        b_proveedorf.setIcon(new ImageIcon("imagenes/buscar.png"));
        b_proveedorf.setToolTipText("Busca un proveedor");
        b_proveedorf.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_proveedorfActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(l_poblacion)
                        .addGap(9, 9, 9)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(t_colonia)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addComponent(t_poblacion)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(l_rfc)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(t_rfc, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(l_nombre)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(t_nombre, javax.swing.GroupLayout.PREFERRED_SIZE, 451, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(b_proveedorf, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(l_colonia)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(l_direccion)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(t_direccion)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(l_cp)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(t_cp, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(l_nombre)
                        .addComponent(t_nombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(b_proveedorf, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(l_direccion, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(t_direccion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(l_cp)
                        .addComponent(t_cp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(l_colonia)
                    .addComponent(t_colonia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(l_rfc)
                        .addComponent(t_rfc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(l_poblacion)
                        .addComponent(t_poblacion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel6, java.awt.BorderLayout.CENTER);

        p_interno_centro.add(jPanel1);

        p_arriba.add(p_interno_centro, java.awt.BorderLayout.CENTER);

        add(p_arriba, java.awt.BorderLayout.NORTH);
    }// </editor-fold>//GEN-END:initComponents

    private void b_buscaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_buscaActionPerformed
        h=new Herramientas(usr, 0);
        h.session(sessionPrograma);
        if(t_busca.getText().compareToIgnoreCase("")!=0)
        {
            if(x>=t_datos.getRowCount())
            x=0;
            for(; x<t_datos.getRowCount(); x++)
            {
                if(t_datos.getValueAt(x, 4).toString().indexOf(t_busca.getText()) != -1)
                {
                    t_datos.setRowSelectionInterval(x, x);
                    t_datos.setColumnSelectionInterval(3, 4);
                    break;
                }
            }
            x++;
        }
    }//GEN-LAST:event_b_buscaActionPerformed

    private void t_buscaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_t_buscaActionPerformed
        h=new Herramientas(usr, 0);
        h.session(sessionPrograma);
        if(t_busca.getText().compareToIgnoreCase("")!=0)
        {
            if(x>=t_datos.getRowCount())
            x=0;
            for(; x<t_datos.getRowCount(); x++)
            {
                if(t_datos.getValueAt(x, 4).toString().indexOf(t_busca.getText()) != -1)
                {
                    t_datos.setRowSelectionInterval(x, x);
                    t_datos.setColumnSelectionInterval(3, 4);
                    break;
                }
            }
            x++;
        }
    }//GEN-LAST:event_t_buscaActionPerformed

    private void b_masActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_masActionPerformed
        h=new Herramientas(usr, 0);
        h.session(sessionPrograma);
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction().begin();
        if(usr.getGeneraPedidos()==true)
        {
            if(this.c_tipo.getSelectedItem().toString().compareTo("Interno")==0)
            {
                if(t_orden.getText().compareTo("")!=0)
                {
                    buscaPartida obj = new buscaPartida(this.t_orden.getText(), new javax.swing.JFrame(), true, 1, ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
                    obj.t_busca.requestFocus();
                    Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
                    obj.setLocation((d.width/2)-(obj.getWidth()/2), (d.height/2)-(obj.getHeight()/2));
                    obj.setVisible(true);
                    part_act=obj.getReturnStatus();
                    try
                    {  
                        if(part_act!=null)
                        {
                            for(int x=0; x<part_act.length; x++)
                            {
                                int r=buscaInterno(part_act[x].getIdEvaluacion(), part_act[x].getSubPartida());
                                if(r==-1)
                                {
                                    int plazo= 0;
                                    String codigo = "";
                                    if(part_act[x].getEjemplar()!=null)
                                        codigo=part_act[x].getEjemplar().getIdParte();
                                    try{
                                        if(part_act[x].getPlazo()!=null) 
                                            plazo=Integer.parseInt(part_act[x].getPlazo().toString());
                                    }
                                    catch(Exception e){}
                                    double cantidad = 0.0d;
                                    double costo = 0.0d;
                                    Partida parid=part_act[x];
                                    parid = (Partida)session.get(Partida.class, parid.getIdPartida());
                                    
                                    if(part_act[x].getCantPcp()!=null && part_act[x].getCantPcp()!= 0)
                                        cantidad=part_act[x].getCantPcp();
                                    else
                                    {
                                        cantidad=part_act[x].getCantidadAut();
                                        parid.setCantPcp(cantidad);
                                    }
                                    if(part_act[x].getPcp()!=null && part_act[x].getPcp()!=0)
                                        costo=part_act[x].getPcp();
                                    else
                                    {
                                        costo=part_act[x].getCU();
                                        parid.setPcp(costo);
                                    }
                                    session.update(parid);
                                    session.beginTransaction().commit();
                                    
                                    //*****buscar que se este comprando en el orden deseado******
                                    boolean acceso=true;
                                    int prioridad = parid.getPrioridad();
                                    if(prioridad==4 || prioridad==3)
                                    {
                                        Partida[] cuentas = (Partida[])session.createCriteria(Partida.class).add(Restrictions.eq("ordenByIdOrden.idOrden", parid.getOrdenByIdOrden().getIdOrden())).add(Restrictions.in("prioridad", new Object[]{1,2})).add(Restrictions.isNull("pedido")).add(Restrictions.eq("refComp", true)).add(Restrictions.eq("autorizadoValuacion", true)).add(Restrictions.eq("autorizado", true)).list().toArray(new Partida[0]);
                                        if(cuentas.length>0)
                                            acceso=false;
                                    }
                                    if(acceso==true)
                                    {
                                    //********
                                    String anotacion="";
                                    if(part_act[x].getInstruccion()!=null)
                                        anotacion=part_act[x].getInstruccion();
                                    
                                    String tipo_pieza="";
                                    if(part_act[x].getTipoPieza()!=null){
                                        tipo_pieza = part_act[x].getTipoPieza().toString().toLowerCase();
                                    }else
                                        tipo_pieza="-";
                                        
                                    Object[] vector=new Object[]{""+part_act[x].getIdEvaluacion()/*#*/,""+part_act[x].getSubPartida()/*R_valua*/,codigo/*codigo*/,""+parid.getCatalogo().getIdCatalogo()/*folio*/,""+parid.getCatalogo().getNombre()+" "+anotacion/*descripción*/,""+part_act[x].getMed()/*medida*/,plazo/*plazo*/,cantidad/*cantidad*/,costo/*costo c/u*/,""+tipo_pieza /*tipo pieza*/,cantidad*costo/*total*/};
                                    this.model.addRow(vector);
                                    model.setCeldaEditable(t_datos.getRowCount()-1, 2, true);
                                    model.setCeldaEditable(t_datos.getRowCount()-1, 9, true);
                                    //***********
                                    }
                                    else
                                    {
                                        JOptionPane.showMessageDialog(null, "¡Recuerda que debes comprar primero las partidas de prioridad 1,2 y 3!");
                                    }
                                }
                            }
                            sumaTotales();
                            formatoTabla();
                            if(t_datos.getRowCount()!=0)
                                b_calendario.setEnabled(true);
                            else
                                b_calendario.setEnabled(false);
                        }
                    }
                    catch(Exception e)
                    {
                        e.printStackTrace();
                        session.getTransaction().rollback();
                    }
                    finally 
                    {
                        if(session.isOpen())
                            session.close();
                    }
                }
                else
                    JOptionPane.showMessageDialog(null, "¡Selecciona una orden de taller!");
            }
            if(this.c_tipo.getSelectedItem().toString().compareTo("Externo")==0)
            {
                int pos=t_datos.getRowCount()+1;
                Object[] vector=new Object[]{""/*#*/,""/*R_valua*/,""/*codigo*/,""+"s/f"/*folio*/,""/*descripción*/,""+""/*medida*/,""/*plazo*/,1.0/*cantidad*/,0.0/*costo c/u*/, "" /*tipo*/,0.0/*total*/};
                model.addRow(vector);
                for(int x=0; x<t_datos.getColumnCount(); x++)
                {
                    if(x==0 || x==1|| x==3 || x==6 || x==9 || x==10)
                        model.setCeldaEditable(t_datos.getRowCount()-1, x, false);
                    else
                        model.setCeldaEditable(t_datos.getRowCount()-1, x, true);
                }
                sumaTotales();
                formatoTabla();
                if(t_datos.getRowCount()!=0)
                    b_calendario.setEnabled(true);
                else
                    b_calendario.setEnabled(false);
            }
            if(this.c_tipo.getSelectedItem().toString().compareTo("Inventario")==0)
            {
                buscaEjemplar obj = new buscaEjemplar(new javax.swing.JFrame(), true, sessionPrograma, this.usr, 1);
                obj.t_busca.requestFocus();
                Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
                obj.setLocation((d.width/2)-(obj.getWidth()/2), (d.height/2)-(obj.getHeight()/2));
                obj.setVisible(true);
                Ejemplar eje=obj.getReturnStatus();
                if (eje!=null)
                {
                    int pos=t_datos.getRowCount()+1;
                    Object[] vector=new Object[]{""/*#*/,""/*R_valua*/,eje.getIdParte()/*codigo*/,""+"s/f"/*folio*/,eje.getComentario()/*descripción*/,""+eje.getMedida()/*medida*/,""/*plazo*/,1.0/*cantidad*/,0.0/*costo c/u*/, "" /*tipo pieza*/,0.0/*total*/};
                    model.addRow(vector);
                    for(int x=0; x<t_datos.getColumnCount(); x++)
                    {
                        if(x==7 || x==8)
                            model.setCeldaEditable(t_datos.getRowCount()-1, x, true);
                        else
                            model.setCeldaEditable(t_datos.getRowCount()-1, x, false);
                    }
                    sumaTotales();
                    formatoTabla();
                    if(t_datos.getRowCount()!=0)
                        b_calendario.setEnabled(true);
                    else
                        b_calendario.setEnabled(false);
                }
            }
            if(this.c_tipo.getSelectedItem().toString().compareTo("Adicional")==0)
            {
                int pos=t_datos.getRowCount()+1;
                Object[] vector=new Object[]{""/*#*/,""/*R_valua*/,""/*codigo*/,""+"s/f"/*folio*/,""/*descripción*/,""+""/*medida*/,""/*plazo*/,1.0/*cantidad*/,0.0/*costo c/u*/, "" /*tipo pieza*/,0.0/*total*/};
                model.addRow(vector);
                for(int x=0; x<t_datos.getColumnCount(); x++)
                {
                    if(x==0 || x==1|| x==3 || x==6 || x==9 || x==10)
                        model.setCeldaEditable(t_datos.getRowCount()-1, x, false);
                    else
                        model.setCeldaEditable(t_datos.getRowCount()-1, x, true);
                }
                sumaTotales();
                formatoTabla();
                if(t_datos.getRowCount()!=0)
                    b_calendario.setEnabled(true);
                else
                    b_calendario.setEnabled(false);
            }
        }
        else
            JOptionPane.showMessageDialog(null, "¡No tienes permiso de generar pedidos!");
    }//GEN-LAST:event_b_masActionPerformed

    private void b_menosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_menosActionPerformed
        h=new Herramientas(usr, 0);
        h.session(sessionPrograma);
        int [] renglones;
        if(c_tipo.getSelectedItem().toString().compareTo("Interno")==0)
        {
            if(t_orden.getText().compareTo("")!=0)
            {
                renglones = t_datos.getSelectedRows();
                if(renglones.length>=0)
                {
                    DefaultTableModel model = (DefaultTableModel) t_datos.getModel();
                    int opt=JOptionPane.showConfirmDialog(this, "¡Las partidas seleccionanas se eliminarán!");
                    if(JOptionPane.YES_OPTION == opt)
                    {
                        for(int x=0; x<renglones.length; x++)
                        {
                            //JOptionPane.showMessageDialog(null, "¡Pedido eliminado!");
                            model.removeRow(t_datos.getSelectedRow());
                        }
                        sumaTotales();
                        if(t_datos.getRowCount()!=0)
                            b_calendario.setEnabled(true);
                        else
                            b_calendario.setEnabled(false);
                        t_plazo.setText("");
                    }
                }
                else
                    JOptionPane.showMessageDialog(null, "¡Selecciona las partidas que desees eliminar!");
            }
            else
                JOptionPane.showMessageDialog(null, "¡Selecciona una orden de taller!");
        }
        if(c_tipo.getSelectedItem().toString().compareTo("Externo")==0)
        {
            renglones = t_datos.getSelectedRows();
            if(renglones.length>=0)
            {
                DefaultTableModel model = (DefaultTableModel) t_datos.getModel();
                int opt=JOptionPane.showConfirmDialog(this, "¡El pedido se eliminará!");
                if (JOptionPane.YES_OPTION == opt)
                {
                    for(int x=0; x<renglones.length; x++)
                    {
                        model.removeRow(t_datos.getSelectedRow());
                    }
                    sumaTotales();
                    //JOptionPane.showMessageDialog(null, "¡Pedido eliminado!");
                    if(t_datos.getRowCount()!=0)
                        b_calendario.setEnabled(true);
                    else
                        b_calendario.setEnabled(false);
                    t_plazo.setText("");
                }
            }
            else
                JOptionPane.showMessageDialog(null, "¡Selecciona las partidas que desees eliminar!");
        }
        if(c_tipo.getSelectedItem().toString().compareTo("Adicional")==0)
        {
            renglones = t_datos.getSelectedRows();
            if(renglones.length>=0)
            {
                int opt=JOptionPane.showConfirmDialog(this, "¡El pedido se eliminará!");
                if (JOptionPane.YES_OPTION == opt)
                {
                    for(int x=0; x<renglones.length; x++)
                    {
                        model.removeRow(t_datos.getSelectedRow());
                    }
                    sumaTotales();
                    //JOptionPane.showMessageDialog(null, "¡Pedido eliminado!");
                    if(t_datos.getRowCount()!=0)
                        b_calendario.setEnabled(true);
                    else
                        b_calendario.setEnabled(false);
                    t_plazo.setText("");
                }
            }
            else
                JOptionPane.showMessageDialog(null, "¡Selecciona las partidas que desees eliminar!");
        }
        if(c_tipo.getSelectedItem().toString().compareTo("Inventario")==0)
        {
            renglones = t_datos.getSelectedRows();
            if(renglones.length>=0)
            {
                int opt=JOptionPane.showConfirmDialog(this, "¡El pedido se eliminará!");
                if (JOptionPane.YES_OPTION == opt)
                {
                    for(int x=0; x<renglones.length; x++)
                    {
                        model.removeRow(t_datos.getSelectedRow());
                    }
                    sumaTotales();
                    //JOptionPane.showMessageDialog(null, "¡Pedido eliminado!");
                    if(t_datos.getRowCount()!=0)
                        b_calendario.setEnabled(true);
                    else
                        b_calendario.setEnabled(false);
                    t_plazo.setText("");
                }
            }
            else
                JOptionPane.showMessageDialog(null, "¡Selecciona las partidas que desees eliminar!");
        }
    }//GEN-LAST:event_b_menosActionPerformed

    private void t_buscaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_buscaKeyTyped
        /*evt.setKeyChar(Character.toUpperCase(evt.getKeyChar()));
        if(t_busca.getText().length()>=30)
        evt.consume();*/
        char car = evt.getKeyChar();
        evt.setKeyChar(Character.toUpperCase(evt.getKeyChar()));
    }//GEN-LAST:event_t_buscaKeyTyped
    public boolean validaTipos(){
        boolean entro=true;
        for(int i=0; i<t_datos.getRowCount(); i++){
            if(t_datos.getValueAt(i, 9).toString().compareTo("-")==0)
                entro=false;
        }
        return entro;
    }
    private void b_guardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_guardarActionPerformed
        BigDecimal compra=new BigDecimal("0.00");
        if(t_clave.getText().compareTo("")!=0)
        {
            if(t_proveedor.getText().compareTo("")!=0)
            {
                if(cb_pago.getSelectedIndex()>0)
                {
                   if(Double.parseDouble(t_cambio.getText())>0.0)
                   {    
                        if(c_tipo.getSelectedItem().toString().compareTo("Interno")==0)
                        {
                            if(t_id_comprador.getText().compareTo("")!=0)
                            {
                                if(t_orden.getText().compareTo("")!=0)
                                {
                                    if(t_datos.getRowCount()!=0)
                                    {
                                        boolean resp = validaTipos();
                                        if(resp==true)
                                        {
                                            //validamos que las compras sean menor al 70% del valor en vales
                                            Session session1 = HibernateUtil.getSessionFactory().openSession();
                                            session1.beginTransaction().begin();
                                            this.orden_act=(Orden)session1.get(Orden.class, orden_act.getIdOrden());
                                            if(orden_act!=null)
                                            {
                                                Partida[] partidas=(Partida[])orden_act.getPartidasForIdOrden().toArray(new Partida[0]);
                                                double suma=0d;
                                                for(int p=0; p<partidas.length; p++)
                                                {
                                                    if(partidas[p].getPedido()!=null)
                                                    {
                                                        BigDecimal bigSuma=new BigDecimal(partidas[p].getCantPcp()*partidas[p].getPcp());
                                                        suma+=bigSuma.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
                                                    }
                                                }
                                                if(orden_act.getPedidos().size()>0)
                                                {
                                                    Pedido [] adicionales=(Pedido[])orden_act.getPedidos().toArray(new Pedido[0]);
                                                    if(adicionales.length>0)
                                                    {
                                                        for(int a=0; a<adicionales.length; a++)
                                                        {
                                                            PartidaExterna[] pe = (PartidaExterna[])adicionales[a].getPartidaExternas().toArray(new PartidaExterna[0]);
                                                            for(int b=0; b<pe.length; b++)
                                                            {
                                                                BigDecimal bigSuma=new BigDecimal(pe[b].getCantidad()*pe[b].getCosto());
                                                                suma+=bigSuma.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
                                                            }
                                                        }
                                                    }
                                                }
                                                compra=new BigDecimal(suma);
                                            }

                                            if(orden_act!=null)
                                            {
                                                Double misVales=orden_act.getVales();
                                                Double compras=compra.doubleValue();
                                                boolean permiso=true;
                                                if(misVales>0.0d)
                                                {
                                                    Double setenta = misVales*0.7;
                                                    Double total=((Number)t_subtotal.getValue()).doubleValue();
                                                    Double totalNeto=(total+compras);
                                                    System.out.println("en vales:"+misVales+"neto:"+totalNeto);
                                                    if(setenta <= totalNeto)
                                                    {
                                                        if(misVales >= totalNeto)
                                                        {
                                                            /*usrAut1=null;
                                                            Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
                                                            autorizar1.setSize(284, 177);
                                                            autorizar1.setLocation((d.width/2)-(autorizar1.getWidth()/2), (d.height/2)-(autorizar1.getHeight()/2));
                                                            t_user1.setText("");
                                                            t_clave1.setText("");
                                                            autorizar1.setVisible(true);
                                                            if(usrAut1==null)
                                                                permiso=false;//no se autoriza*/
                                                            Usuario[] autoriza = (Usuario[])session1.createCriteria(Usuario.class).add(Restrictions.eq("autorizarPedidos", true)).list().toArray(new Usuario[0]);
                                                            usr = (Usuario) session1.get(Usuario.class, usr.getIdUsuario());
                                                            if(autoriza!=null)
                                                            {
                                                                String correos="";
                                                                for(int y=0; y<autoriza.length; y++)
                                                                {
                                                                    correos+=autoriza[y].getEmpleado().getEmail()+";";
                                                                }
                                                                String suc="Toluca";
                                                                if(ruta.compareToIgnoreCase("tbstultitlan.ddns.net")==0)
                                                                    suc="Tultitlan";
                                                                else{
                                                                    if(ruta.compareToIgnoreCase("set-toluca.ddns.net")==0)
                                                                        suc="Merida";
                                                                }
                                                                miCorreo= new EnviaCorreo("La OT"+orden_act.getIdOrden()+" arriba del 70%", "Hola buen dia, se te comunica que la OT "+orden_act.getIdOrden()+" ha sobre pasado el 70% del presupuesto asignado", correos, usr.getEmpleado().getEmail());
                                                            }
                                                        }
                                                        else
                                                        {
                                                            if(session1!=null)
                                                                if(session1.isOpen())
                                                                    session1.close();
                                                            usrAut2=null;
                                                            Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
                                                            autorizar2.setSize(284, 177);
                                                            autorizar2.setLocation((d.width/2)-(autorizar2.getWidth()/2), (d.height/2)-(autorizar2.getHeight()/2));
                                                            t_user2.setText("");
                                                            t_clave2.setText("");
                                                            autorizar2.setVisible(true);
                                                            if(usrAut2==null)
                                                                permiso=false;//no se autoriza
                                                        }
                                                    }
                                                }
                                                if(permiso==true)
                                                {
                                                    Pedido pedido = new Pedido();
                                                    pedido.setFormaPago(cb_pago.getSelectedIndex());
                                                    if(t_clave.getText().compareTo("")!=0)
                                                        pedido.setProveedorByIdEmpresa(provf_act);
                                                    if(t_proveedor.getText().compareTo("")!=0)
                                                        pedido.setProveedorByIdProveedor(prov_act);
                                                    pedido.setUsuarioByIdUsuario(usr);
                                                    Date fecha_pedido = new Date();
                                                    DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                                                    String valor=dateFormat.format(fecha_pedido);
                                                    String [] fecha = valor.split("-");
                                                    String [] hora=fecha[2].split(":");
                                                    String [] aux=hora[0].split(" ");
                                                    fecha[2]=aux[0];
                                                    hora[0]=aux[1];
                                                    Calendar calendario = Calendar.getInstance();
                                                    calendario.set(
                                                            Integer.parseInt(fecha[2]), 
                                                            Integer.parseInt(fecha[1])-1, 
                                                            Integer.parseInt(fecha[0]), 
                                                            Integer.parseInt(hora[0]), 
                                                            Integer.parseInt(hora[1]), 
                                                            Integer.parseInt(hora[2]));
                                                    pedido.setFechaPedido(calendario.getTime());
                                                    if(t_notas.getText().compareTo("")!=0)
                                                        pedido.setNotas(t_notas.getText());
                                                    pedido.setTipoPedido(c_tipo.getSelectedItem().toString());
                                                    Integer respuesta=guardarPedido(pedido);
                                                    if(respuesta!=null)
                                                    {
                                                        t_pedido.setText(""+respuesta);
                                                        if(archivo!=null)
                                                            if(subeArchivo(""+respuesta)==true)
                                                                envia(""+respuesta);
                                                            estadoBotones();
                                                        JOptionPane.showMessageDialog(null, "Pedido almacenado con la clave:  " +respuesta);
                                                    }
                                                    else
                                                        b_guardar.requestFocus();
                                                }
                                            }
                                            if(session1!=null)
                                                if(session1.isOpen())
                                                    session1.close();
                                        }else
                                            JOptionPane.showMessageDialog(null, "Existen partidas sin Tipo de pieza.");
                                    }
                                    else
                                        JOptionPane.showMessageDialog(null, "Selecciona algunos pedidos");
                                }
                                else
                                    JOptionPane.showMessageDialog(null, "Selecciona una orden de taller");
                            }
                            else
                                JOptionPane.showMessageDialog(null, "Selecciona el nombre del comprador");
                        }
                        if(c_tipo.getSelectedItem().toString().compareTo("Externo")==0)
                        {
                            if(t_id_comprador.getText().compareTo("")!=0)
                            {
                                for(int ren=0; ren<t_datos.getRowCount(); ren++)
                                {
                                    if(t_datos.getValueAt(ren, 4).toString().compareTo("")==0)
                                    {
                                        DefaultTableModel model = (DefaultTableModel) t_datos.getModel();
                                        model.removeRow(ren);
                                        ren--;
                                    }
                                }
                                if(t_datos.getRowCount()!=0)
                                {
                                    Pedido pedido = new Pedido();
                                    pedido.setFormaPago(cb_pago.getSelectedIndex());
                                    if(t_clave.getText().compareTo("")!=0)
                                        pedido.setProveedorByIdEmpresa(provf_act);
                                    if(t_proveedor.getText().compareTo("")!=0)
                                        pedido.setProveedorByIdProveedor(prov_act);
                                    pedido.setUsuarioByIdUsuario(usr);
                                    Date fecha_pedido = new Date();
                                    DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                                    String valor=dateFormat.format(fecha_pedido);
                                    String [] fecha = valor.split("-");
                                    String [] hora=fecha[2].split(":");
                                    String [] aux=hora[0].split(" ");
                                    fecha[2]=aux[0];
                                    hora[0]=aux[1];
                                    Calendar calendario = Calendar.getInstance();
                                    calendario.set(
                                            Integer.parseInt(fecha[2]), 
                                            Integer.parseInt(fecha[1])-1, 
                                            Integer.parseInt(fecha[0]), 
                                            Integer.parseInt(hora[0]), 
                                            Integer.parseInt(hora[1]), 
                                            Integer.parseInt(hora[2]));
                                    pedido.setFechaPedido(calendario.getTime());
                                    if(this.t_folio_externo.getText().compareTo("")!=0)
                                        pedido.setIdExterno(this.t_folio_externo.getText());
                                    if(t_notas.getText().compareTo("")!=0)
                                        pedido.setNotas(t_notas.getText());
                                    pedido.setTipoPedido(c_tipo.getSelectedItem().toString());
                                    Integer respuesta=guardarPedido(pedido);
                                    if(respuesta!=null)
                                    {
                                        t_pedido.setText(""+respuesta);
                                        if(archivo!=null)
                                            if(subeArchivo(""+respuesta)==true)
                                                envia(""+respuesta);
                                            estadoBotones();
                                        JOptionPane.showMessageDialog(null, "Pedido almacenado con la clave:  " +respuesta);
                                    }
                                    else
                                        b_guardar.requestFocus();
                                }
                                else
                                    JOptionPane.showMessageDialog(null, "Selecciona algunos pedidos");
                            }
                            else
                                JOptionPane.showMessageDialog(null, "Selecciona el nombre del comprador");
                        }
                        if(c_tipo.getSelectedItem().toString().compareTo("Inventario")==0)
                        {
                            if(t_id_comprador.getText().compareTo("")!=0)
                            {
                                if(t_datos.getRowCount()!=0)
                                {
                                    Pedido pedido = new Pedido();
                                    pedido.setFormaPago(cb_pago.getSelectedIndex());
                                    if(t_clave.getText().compareTo("")!=0)
                                        pedido.setProveedorByIdEmpresa(provf_act);
                                    if(t_proveedor.getText().compareTo("")!=0)
                                        pedido.setProveedorByIdProveedor(prov_act);
                                    pedido.setUsuarioByIdUsuario(usr);
                                    Date fecha_pedido = new Date();
                                    DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                                    String valor=dateFormat.format(fecha_pedido);
                                    String [] fecha = valor.split("-");
                                    String [] hora=fecha[2].split(":");
                                    String [] aux=hora[0].split(" ");
                                    fecha[2]=aux[0];
                                    hora[0]=aux[1];
                                    Calendar calendario = Calendar.getInstance();
                                    calendario.set(
                                            Integer.parseInt(fecha[2]), 
                                            Integer.parseInt(fecha[1])-1, 
                                            Integer.parseInt(fecha[0]), 
                                            Integer.parseInt(hora[0]), 
                                            Integer.parseInt(hora[1]), 
                                            Integer.parseInt(hora[2]));
                                    pedido.setFechaPedido(calendario.getTime());
                                    if(this.t_folio_externo.getText().compareTo("")!=0)
                                        pedido.setIdExterno(this.t_folio_externo.getText());
                                    if(t_notas.getText().compareTo("")!=0)
                                        pedido.setNotas(t_notas.getText());
                                    pedido.setTipoPedido(c_tipo.getSelectedItem().toString());
                                    Integer respuesta=guardarPedido(pedido);
                                    if(respuesta!=null)
                                    {
                                        t_pedido.setText(""+respuesta);
                                        if(archivo!=null)
                                            if(subeArchivo(""+respuesta)==true)
                                                envia(""+respuesta);
                                            estadoBotones();
                                        JOptionPane.showMessageDialog(null, "Pedido almacenado con la clave:  " +respuesta);
                                    }
                                    else
                                        b_guardar.requestFocus();
                                }
                                else
                                    JOptionPane.showMessageDialog(null, "Selecciona algunos pedidos");
                            }
                            else
                                JOptionPane.showMessageDialog(null, "Selecciona el nombre del comprador");
                        }
                        if(c_tipo.getSelectedItem().toString().compareTo("Adicional")==0)
                        {
                            usrA=null;
                            Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
                            autorizar3.setSize(284, 177);
                            autorizar3.setLocation((d.width/2)-(autorizar3.getWidth()/2), (d.height/3)-(autorizar3.getHeight()/3));
                            t_user3.setText("");
                            t_clave3.setText("");
                            autorizar3.setVisible(true);
                            if(usrA!=null)
                            {
                                //validamos que las compras sean menor al 70% del valor en vales
                                Session session1 = HibernateUtil.getSessionFactory().openSession();
                                session1.beginTransaction().begin();
                                this.orden_act=(Orden)session1.get(Orden.class, orden_act.getIdOrden());
                                if(orden_act!=null)
                                {
                                    Partida[] partidas=(Partida[])orden_act.getPartidasForIdOrden().toArray(new Partida[0]);
                                    double suma=0d;
                                    for(int p=0; p<partidas.length; p++)
                                    {
                                        if(partidas[p].getPedido()!=null)
                                        {
                                            BigDecimal bigSuma=new BigDecimal(partidas[p].getCantPcp()*partidas[p].getPcp());
                                            suma+=bigSuma.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
                                        }
                                    }
                                    if(orden_act.getPedidos().size()>0)
                                    {
                                        Pedido [] adicionales=(Pedido[])orden_act.getPedidos().toArray(new Pedido[0]);
                                        if(adicionales.length>0)
                                        {
                                            for(int a=0; a<adicionales.length; a++)
                                            {
                                                PartidaExterna[] pe = (PartidaExterna[])adicionales[a].getPartidaExternas().toArray(new PartidaExterna[0]);
                                                for(int b=0; b<pe.length; b++)
                                                {
                                                    BigDecimal bigSuma=new BigDecimal(pe[b].getCantidad()*pe[b].getCosto());
                                                    suma+=bigSuma.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
                                                }
                                            }
                                        }
                                    }
                                    compra=new BigDecimal(suma);
                                }

                                /*if(session1!=null)
                                    if(session1.isOpen())
                                        session1.close();*/
                                if(orden_act!=null)
                                {
                                    Double misVales=orden_act.getVales();
                                    Double compras=compra.doubleValue();
                                    boolean permiso=true;
                                    if(misVales>0.0d)
                                    {
                                        Double setenta = misVales*0.7;
                                        Double total=((Number)t_subtotal.getValue()).doubleValue();
                                        Double totalNeto=(total+compras);
                                        System.out.println("en vales:"+misVales+"total:"+total+" compras:"+compras);
                                        if(setenta <= totalNeto)
                                        {
                                            if(misVales >= totalNeto)
                                            {
                                                /*usrAut1=null;
                                                d = Toolkit.getDefaultToolkit().getScreenSize();
                                                autorizar1.setSize(284, 177);
                                                autorizar1.setLocation((d.width/2)-(autorizar1.getWidth()/2), (d.height/2)-(autorizar1.getHeight()/2));
                                                t_user1.setText("");
                                                t_clave1.setText("");
                                                autorizar1.setVisible(true);
                                                if(usrAut1==null)
                                                    permiso=false;//no se autoriza*/
                                                Usuario[] autoriza = (Usuario[])session1.createCriteria(Usuario.class).add(Restrictions.eq("autorizarPedidos", true)).list().toArray(new Usuario[0]);
                                                usr = (Usuario) session1.get(Usuario.class, usr.getIdUsuario());
                                                if(autoriza!=null)
                                                {
                                                    String correos="";
                                                    for(int y=0; y<autoriza.length; y++)
                                                    {
                                                        correos+=autoriza[y].getEmpleado().getEmail()+";";
                                                    }
                                                    String suc="Toluca";
                                                    if(ruta.compareToIgnoreCase("tbstultitlan.ddns.net")==0)
                                                        suc="Tultitlan";
                                                    else{
                                                        if(ruta.compareToIgnoreCase("set-toluca.ddns.net")==0)
                                                            suc="Merida";
                                                    }
                                                    miCorreo= new EnviaCorreo("La OT"+orden_act.getIdOrden()+" arriba del 70%", "Hola buen dia, se te comunica que la OT "+orden_act.getIdOrden()+" ha sobre pasado el 70% del presupuesto asignado", correos, usr.getEmpleado().getEmail());
                                                }
                                            }
                                            else
                                            {
                                                if(session1!=null)
                                                    if(session1.isOpen())
                                                        session1.close();
                                                usrAut2=null;
                                                d = Toolkit.getDefaultToolkit().getScreenSize();
                                                autorizar2.setSize(284, 177);
                                                autorizar2.setLocation((d.width/2)-(autorizar2.getWidth()/2), (d.height/2)-(autorizar2.getHeight()/2));
                                                t_user2.setText("");
                                                t_clave2.setText("");
                                                autorizar2.setVisible(true);
                                                if(usrAut2==null)
                                                    permiso=false;//no se autoriza
                                            }
                                        }
                                    }
                                    if(permiso==true)
                                    {
                                        if(t_id_comprador.getText().compareTo("")!=0)
                                        {
                                            for(int ren=0; ren<t_datos.getRowCount(); ren++)
                                            {
                                                if(t_datos.getValueAt(ren, 4).toString().compareTo("")==0 )
                                                {
                                                    DefaultTableModel model = (DefaultTableModel) t_datos.getModel();
                                                    model.removeRow(ren);
                                                    ren--;
                                                }
                                            }
                                            if(t_datos.getRowCount()!=0)
                                            {
                                                Pedido pedido = new Pedido();
                                                pedido.setFormaPago(cb_pago.getSelectedIndex());
                                                if(t_clave.getText().compareTo("")!=0)
                                                    pedido.setProveedorByIdEmpresa(provf_act);
                                                if(t_proveedor.getText().compareTo("")!=0)
                                                    pedido.setProveedorByIdProveedor(prov_act);
                                                pedido.setUsuarioByIdUsuario(usr);
                                                Date fecha_pedido = new Date();
                                                DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                                                String valor=dateFormat.format(fecha_pedido);
                                                String [] fecha = valor.split("-");
                                                String [] hora=fecha[2].split(":");
                                                String [] aux=hora[0].split(" ");
                                                fecha[2]=aux[0];
                                                hora[0]=aux[1];
                                                Calendar calendario = Calendar.getInstance();
                                                calendario.set(
                                                        Integer.parseInt(fecha[2]), 
                                                        Integer.parseInt(fecha[1])-1, 
                                                        Integer.parseInt(fecha[0]), 
                                                        Integer.parseInt(hora[0]), 
                                                        Integer.parseInt(hora[1]), 
                                                        Integer.parseInt(hora[2]));
                                                pedido.setFechaPedido(calendario.getTime());
                                                if(this.t_folio_externo.getText().compareTo("")!=0)
                                                    pedido.setIdExterno(this.t_folio_externo.getText());
                                                if(t_notas.getText().compareTo("")!=0)
                                                    pedido.setNotas(t_notas.getText());
                                                pedido.setTipoPedido(c_tipo.getSelectedItem().toString());
                                                Integer respuesta=guardarPedido(pedido);
                                                if(respuesta!=null)
                                                {
                                                    t_pedido.setText(""+respuesta);
                                                    if(archivo!=null)
                                                        if(subeArchivo(""+respuesta)==true)
                                                            envia(""+respuesta);
                                                        estadoBotones();
                                                    JOptionPane.showMessageDialog(null, "Pedido almacenado con la clave:  " +respuesta);
                                                }
                                                else
                                                    b_guardar.requestFocus();
                                            }
                                            else
                                                JOptionPane.showMessageDialog(null, "Selecciona algunos pedidos");
                                        }
                                        else
                                            JOptionPane.showMessageDialog(null, "Selecciona el nombre del comprador");
                                    }
                                }
                            }
                        }
                   }else
                       JOptionPane.showMessageDialog(null, "Ingrese un tipo de cambio.");
                }
                else
                    JOptionPane.showMessageDialog(null, "Selecciona el tipo de pago");
            }
            else
                JOptionPane.showMessageDialog(null, "Selecciona un proveedor");
        }
        else
            JOptionPane.showMessageDialog(null, "Selecciona una empresa a la que se va a facturar");
    }//GEN-LAST:event_b_guardarActionPerformed

    private void b_pedidoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_pedidoActionPerformed
        Formatos f1;
        if(this.c_tipo.getSelectedItem().toString().compareTo("Externo")==0 || this.c_tipo.getSelectedItem().toString().compareTo("Inventario")==0)
            f1=new Formatos(this.usr, this.sessionPrograma, null, t_pedido.getText(),configuracion);
        else
            f1=new Formatos(this.usr, this.sessionPrograma, this.orden_act, t_pedido.getText(),configuracion);
            
        if(this.c_tipo.getSelectedItem().toString().compareTo("Interno")==0)
            f1.pedidos();
        else
            f1.pedidosExternos(Integer.parseInt(this.t_pedido.getText()));
    }//GEN-LAST:event_b_pedidoActionPerformed

    private void b_nuevo_pedidoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_nuevo_pedidoActionPerformed
        h=new Herramientas(usr, menu);
        h.session(sessionPrograma);
        h= new Herramientas(usr, menu);
        h.desbloqueaOrden();
        this.c_tipo.setSelectedIndex(0);
        datos_unidad("", "", "", "", "", "","", "", "");
        cajasprovf("", "", "", "", "", "");
        model=new MyModel(0, columnas);
        t_datos.setModel(model);
        formatoTabla();
        this.t_folio_externo.setText("");
        t_clave.setText("");
        t_fecha.setText("");
        t_pedido.setText("");
        t_proveedor.setText("");
        t_orden.setText("");
        t_notas.setText("");
        this.t_id_comprador.setText("");
        this.t_nombre_comprador.setText("");
        l_nombre_aseguradora.setText("SELECCIONE UN PROVEEDOR");
        b_guardar.setEnabled(true);
        b_proveedor.setEnabled(true);
        b_orden.setEnabled(true);
        b_proveedorf.setEnabled(true);
        b_mas.setEnabled(true);
        b_menos.setEnabled(true);
        t_notas.setEditable(true);
        t_datos.setEnabled(true);
        b_pedido.setEnabled(false);
        b_archivo.setEnabled(true);
        agregaArchivo("Agregar Soporte", b_archivo);
        this.c_tipo.setEnabled(true);
        cb_pago.setSelectedIndex(0);
        sumaTotales();
        this.orden_act=null;
    }//GEN-LAST:event_b_nuevo_pedidoActionPerformed

    private void t_datosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_t_datosMouseClicked
        //h=new Herramientas(usr, 0);
        //h.session(sessionPrograma);
        if(t_datos.getSelectedRow()>=0)
        {
            /*if(t_datos.getSelectedColumn()==2 && c_tipo.getSelectedItem().toString().compareTo("Interno")==0)
            {
                numeros.removeAllItems();
                numeros.addItem("Cancelar");
                numeros.addItem("Eliminar");
                numeros.addItem("Buscar");
                numeros.addItem("Nuevo");
                numeros.setSelectedItem("Cancelar");
                Session session = HibernateUtil.getSessionFactory().openSession();
                try
                {
                    session.beginTransaction().begin();
                    Catalogo cat=(Catalogo) session.get(Catalogo.class, Integer.parseInt(t_datos.getValueAt(t_datos.getSelectedRow(), 3).toString()));
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
            }*/
            if(t_datos.getSelectedColumn()==6) 
            { 
                if(b_guardar.isEnabled())
                {
                    if(c_tipo.getSelectedItem().toString().compareTo("Interno")==0)
                    {
                        calendario cal =new calendario(new javax.swing.JFrame(), true, false);
                        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
                        cal.setLocation((d.width/2)-(cal.getWidth()/2), (d.height/2)-(cal.getHeight()/2));
                        cal.setVisible(true);
                        Calendar miCalendario=cal.getReturnStatus();
                        if(miCalendario!=null)
                        {
                            Session session = HibernateUtil.getSessionFactory().openSession();
                            Partida part=(Partida)session.createCriteria(Partida.class).add(Restrictions.eq("ordenByIdOrden.idOrden", Integer.parseInt(t_orden.getText()))).add(Restrictions.eq("idEvaluacion", Integer.parseInt(t_datos.getValueAt(t_datos.getSelectedRow(), 0).toString()))).add(Restrictions.eq("subPartida", Integer.parseInt(t_datos.getValueAt(t_datos.getSelectedRow(), 1).toString()))).setMaxResults(1).uniqueResult();
                            if(part!=null)
                            {
                                session.beginTransaction().begin();
                                try{
                                    String [] fecha ;
                                    Calendar calendario;
                                    String dia=Integer.toString(miCalendario.get(Calendar.DATE));
                                    String mes = Integer.toString(miCalendario.get(Calendar.MONTH)+1);
                                    String anio = Integer.toString(miCalendario.get(Calendar.YEAR));
                                    String valor=dia+"-"+mes+"-"+anio;
                                    fecha = valor.split("-");
                                    calendario = Calendar.getInstance();
                                    calendario.set(
                                            Integer.parseInt(fecha[2]), 
                                            Integer.parseInt(fecha[1])-1, 
                                            Integer.parseInt(fecha[0]));
                                    part.setPlazo(calendario.getTime());
                                    session.update(part);
                                    session.getTransaction().commit();
                                    t_datos.setName(dia+"-"+mes+"-"+anio);
                                    int reng = t_datos.getSelectedRow();
                                    String dato = (t_datos.getName());
                                    t_datos.setValueAt(dato,reng,6);
                                }
                                catch(Exception e)
                                {
                                    session.getTransaction().rollback();
                                    System.out.println(e);
                                }
                            }
                            else
                                JOptionPane.showMessageDialog(null, "¡El registro no se encontró!");
                            if(session.isOpen())
                                session.close();
                        }
                    }
                    else
                    {
                        calendario cal =new calendario(new javax.swing.JFrame(), true, false);
                        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
                        cal.setLocation((d.width/2)-(cal.getWidth()/2), (d.height/2)-(cal.getHeight()/2));
                        cal.setVisible(true);
                        Calendar miCalendario=cal.getReturnStatus();
                        if(miCalendario!=null)
                        {
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                            t_datos.setValueAt(sdf.format(miCalendario.getTime()),t_datos.getSelectedRow(),6);
                        }
                        else
                            t_datos.setValueAt("",t_datos.getSelectedRow(),6);
                    }
                }
            }
        }
    }//GEN-LAST:event_t_datosMouseClicked

    private void t_userActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_t_userActionPerformed
        t_contra.requestFocus();
    }//GEN-LAST:event_t_userActionPerformed

    private void t_contraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_t_contraActionPerformed
        b_autorizar.requestFocus();
    }//GEN-LAST:event_t_contraActionPerformed

    private void b_autorizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_autorizarActionPerformed
        if(t_user.getText().compareTo("")!=0)
        {
            if(t_contra.getPassword().toString().compareTo("")!=0)
            {
                Session session = HibernateUtil.getSessionFactory().openSession();
                try
                {
                    session.beginTransaction().begin();
                    Usuario autoriza = (Usuario)session.createCriteria(Usuario.class).add(Restrictions.eq("idUsuario", t_user.getText())).add(Restrictions.eq("clave", t_contra.getText())).setMaxResults(1).uniqueResult();
                    if(autoriza!=null)
                    {
                        if(autoriza.getAutorizarSobrecosto()==true)
                        {
                            usrAut=autoriza;
                            autorizarCosto.dispose();
                        }
                        else
                            JOptionPane.showMessageDialog(this, "¡El usuario no tiene permiso de autorizar!");    
                    }
                    else
                    {
                        session.beginTransaction().rollback();
                        JOptionPane.showMessageDialog(this, "¡Datos Incorrectos!");
                        t_user.requestFocus();
                    }
                }catch(Exception e)
                {
                    session.beginTransaction().rollback();
                    JOptionPane.showMessageDialog(this, "¡Error al consultar los datos!");
                    e.printStackTrace();
                }
                finally
                {
                    if(session.isOpen()==true)
                        session.close();
                }
            }
            else
                JOptionPane.showMessageDialog(this, "¡Ingrese la contraseña!");
            t_contra.requestFocus();
        }
        else
            JOptionPane.showMessageDialog(this, "¡Ingrese el usuario!");
        t_user.requestFocus();
    }//GEN-LAST:event_b_autorizarActionPerformed

    private void b_autorizarFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_b_autorizarFocusLost
        sumaTotales();
    }//GEN-LAST:event_b_autorizarFocusLost

    private void t_siniestroKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_siniestroKeyTyped
        // TODO add your handling code here:
        char car = evt.getKeyChar();
        if(t_siniestro.getText().length()>=18)
        evt.consume();
    }//GEN-LAST:event_t_siniestroKeyTyped

    private void b_aseguradoraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_aseguradoraActionPerformed
        // TODO add your handling code here:
        h=new Herramientas(usr, 0);
        h.session(sessionPrograma);

        buscaCompania obj = new buscaCompania(new javax.swing.JFrame(), true, this.sessionPrograma, this.usr);
        obj.t_busca.requestFocus();
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        obj.setLocation((d.width/2)-(obj.getWidth()/2), (d.height/2)-(obj.getHeight()/2));
        obj.setVisible(true);

        Compania actor=obj.getReturnStatus();
        if(actor!=null)
        {
            t_id_aseguradora.setText(actor.getIdCompania().toString());
            t_aseguradora.setText(actor.getNombre());
        }
        else
        {
            t_id_aseguradora.setText("");
            t_aseguradora.setText("");
        }
    }//GEN-LAST:event_b_aseguradoraActionPerformed

    private void t_modeloKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_modeloKeyTyped
        // TODO add your handling code here:
        char car = evt.getKeyChar();
        if(t_modelo.getText().length()>=4)
        evt.consume();
        if((car<'0' || car>'9'))
        evt.consume();
    }//GEN-LAST:event_t_modeloKeyTyped

    private void b_marcaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_marcaActionPerformed
        // TODO add your handling code here:
        h=new Herramientas(usr, 0);
        h.session(sessionPrograma);

        buscaMarca obj = new buscaMarca(new javax.swing.JFrame(), true, this.sessionPrograma, this.usr, false);
        obj.t_busca.requestFocus();
        obj.formatoTabla();
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        obj.setLocation((d.width/2)-(obj.getWidth()/2), (d.height/2)-(obj.getHeight()/2));
        obj.setVisible(true);

        Marca actor=obj.getReturnStatus();
        if(actor!=null)
        t_marca.setText(actor.getIdMarca());
        else
        t_marca.setText("");
    }//GEN-LAST:event_b_marcaActionPerformed

    private void b_tipoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_tipoActionPerformed
        // TODO add your handling code here:
        h=new Herramientas(usr, 0);
        h.session(sessionPrograma);

        buscaTipo obj = new buscaTipo(new javax.swing.JFrame(), true, this.sessionPrograma, this.usr);
        obj.t_busca.requestFocus();
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        obj.setLocation((d.width/2)-(obj.getWidth()/2), (d.height/2)-(obj.getHeight()/2));
        obj.setVisible(true);

        String actor=obj.getReturnStatus();
        if(actor!=null)
        {
            t_tipo.setText(actor);
        }
        else
        {
            t_tipo.setText("");
        }
    }//GEN-LAST:event_b_tipoActionPerformed

    private void c_tipoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_c_tipoActionPerformed
        // TODO add your handling code here:
        if(c_tipo.getSelectedItem().toString().compareTo("Interno")==0)
        {
            datos_unidad("", "", "", "", "", "","","", "");
            this.t_folio_externo.setEditable(false);
            this.t_id_comprador.setText("");
            this.t_nombre_comprador.setText("");
            this.b_comprador.setEnabled(true);
            this.t_orden.setText("");
            this.b_orden.setEnabled(true);
            this.b_tipo.setEnabled(false);
            this.b_marca.setEnabled(false);
            this.t_modelo.setEditable(false);
            
            this.b_aseguradora.setEnabled(false);
            this.t_siniestro.setEditable(false);
            this.t_asegurado.setEditable(false);
            this.t_folio_externo.setEditable(false);
            this.t_folio_externo.setText("");
            this.b_calendario.setEnabled(false);
        }
        if(c_tipo.getSelectedItem().toString().compareTo("Externo")==0)
        {
            datos_unidad("", "", "", "", "", "","","", "");
            this.t_folio_externo.setEditable(true);
            this.t_id_comprador.setText("");
            this.t_nombre_comprador.setText("");
            this.b_comprador.setEnabled(true);
            this.t_orden.setText("");
            this.orden_act=null;
            this.b_orden.setEnabled(false);
            this.b_tipo.setEnabled(true);
            this.b_marca.setEnabled(true);
            this.t_modelo.setEditable(true);
            this.b_aseguradora.setEnabled(true);
            this.t_siniestro.setEditable(true);
            this.t_asegurado.setEditable(true);
            this.t_folio_externo.setEditable(true);
            this.t_folio_externo.setText("");
            this.b_calendario.setEnabled(false);
            
        }
        if(c_tipo.getSelectedItem().toString().compareTo("Adicional")==0)
        {
                datos_unidad("", "", "", "", "", "","","", "");
                this.t_folio_externo.setEditable(false);
                this.t_id_comprador.setText("");
                this.t_nombre_comprador.setText("");
                this.b_comprador.setEnabled(true);
                this.t_orden.setText("");
                this.b_orden.setEnabled(true);
                this.b_tipo.setEnabled(false);
                this.b_marca.setEnabled(false);
                this.t_modelo.setEditable(false);
                this.b_aseguradora.setEnabled(false);
                this.t_siniestro.setEditable(false);
                this.t_asegurado.setEditable(false);
                this.t_folio_externo.setEditable(false);
                this.t_folio_externo.setText("");
                this.b_calendario.setEnabled(false);

        }
        if(c_tipo.getSelectedItem().toString().compareTo("Inventario")==0)
        {
            datos_unidad("", "", "", "", "", "","","", "");
            this.t_id_comprador.setText("");
            this.t_nombre_comprador.setText("");
            this.b_comprador.setEnabled(true);
            this.t_orden.setText("");
            this.orden_act=null;
            this.b_orden.setEnabled(false);
            this.b_tipo.setEnabled(false);
            this.b_marca.setEnabled(false);
            this.t_modelo.setEditable(false);
            this.b_aseguradora.setEnabled(false);
            this.t_siniestro.setEditable(false);
            this.t_asegurado.setEditable(false);
            this.t_folio_externo.setEditable(true);
            this.t_folio_externo.setText("");
            this.b_calendario.setEnabled(false);
            
        }
        model=new MyModel(0, columnas);
        t_datos.setModel(model);
        formatoTabla();
    }//GEN-LAST:event_c_tipoActionPerformed

    private void b_compradorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_compradorActionPerformed
        // TODO add your handling code here:
        h=new Herramientas(usr, 0);
        h.session(sessionPrograma);

        buscaEmpleado obj = new buscaEmpleado(new javax.swing.JFrame(), true, usr, sessionPrograma, false);
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        obj.setLocation((d.width/2)-(obj.getWidth()/2), (d.height/2)-(obj.getHeight()/2));
        obj.setVisible(true);
        Empleado actor=obj.getReturnStatus();
        if(actor!=null)
        {
            this.t_id_comprador.setText(""+actor.getIdEmpleado());
            this.t_nombre_comprador.setText(actor.getNombre());
        }
    }//GEN-LAST:event_b_compradorActionPerformed

    private void b_proveedorfActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_proveedorfActionPerformed
        h=new Herramientas(usr, 0);
        h.session(sessionPrograma);
        buscaProveedor obj = new buscaProveedor(new javax.swing.JFrame(), true, this.usr, this.sessionPrograma, 1);
        obj.t_busca.requestFocus();
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        obj.setLocation((d.width/2)-(obj.getWidth()/2), (d.height/2)-(obj.getHeight()/2));
        obj.setVisible(true);
        provf_act=obj.getReturnStatus();
        if (provf_act!=null)
        {
            provf_act=buscarProveedor(provf_act.getIdProveedor());
            t_clave.setText(""+provf_act.getIdProveedor());
            this.cajasprovf(provf_act.getNombre(), provf_act.getDireccion(), provf_act.getPoblacion(), provf_act.getColonia(), provf_act.getCp(), provf_act.getRfc());
        }
        else
        {
            t_clave.setText("");
            cajasprovf("", "", "", "", "", "");
            t_clave.requestFocus();
        }
    }//GEN-LAST:event_b_proveedorfActionPerformed

    private void b_calendarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_calendarioActionPerformed
        h=new Herramientas(usr, 0);
        h.session(sessionPrograma);
        calendario cal =new calendario(new javax.swing.JFrame(), true, false);
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        cal.setLocation((d.width/2)-(cal.getWidth()/2), (d.height/2)-(cal.getHeight()/2));
        cal.setVisible(true);
        Calendar miCalendario=cal.getReturnStatus();
        if(miCalendario!=null)
        {
            if(this.c_tipo.getSelectedItem().toString().compareTo("Interno")==0)
            {
                Session session = HibernateUtil.getSessionFactory().openSession();
                for(int ren=0; ren<t_datos.getRowCount(); ren++)
                {
                    Partida part=(Partida)session.createCriteria(Partida.class).add(Restrictions.eq("ordenByIdOrden.idOrden", Integer.parseInt(t_orden.getText()))).add(Restrictions.eq("idEvaluacion", Integer.parseInt(t_datos.getValueAt(ren, 0).toString()))).add(Restrictions.eq("subPartida", Integer.parseInt(t_datos.getValueAt(ren, 1).toString()))).setMaxResults(1).uniqueResult();
                    if(part!=null)
                    {
                        session.beginTransaction().begin();
                        try{
                            String [] fecha;
                            Calendar calendario;
                            String dia=Integer.toString(miCalendario.get(Calendar.DATE));
                            String mes = Integer.toString(miCalendario.get(Calendar.MONTH)+1);
                            String anio = Integer.toString(miCalendario.get(Calendar.YEAR));
                            String valor=dia+"-"+mes+"-"+anio;
                            fecha = valor.split("-");
                            calendario = Calendar.getInstance();
                            calendario.set(
                                Integer.parseInt(fecha[2]),
                                Integer.parseInt(fecha[1])-1,
                                Integer.parseInt(fecha[0]));
                            part.setPlazo(calendario.getTime());
                            session.update(part);
                            session.getTransaction().commit();
                            t_plazo.setText(dia+"-"+mes+"-"+anio);
                            String dato = (t_plazo.getText());
                            t_datos.setValueAt(dato,ren,6);
                        }
                        catch(Exception e)
                        {
                            session.getTransaction().rollback();
                            System.out.println(e);
                        }
                    }
                    else
                    JOptionPane.showMessageDialog(null, "¡El registro no se encontró!");
                }
                if(session.isOpen())
                    session.close();
            }
            else
            {
                for(int r=0; r<t_datos.getRowCount(); r++)
                {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    t_datos.setValueAt(sdf.format(miCalendario.getTime()),r,6);
                }
            }
        }
    }//GEN-LAST:event_b_calendarioActionPerformed

    private void t_plazoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_t_plazoActionPerformed
        b_orden.requestFocus();
    }//GEN-LAST:event_t_plazoActionPerformed

    private void b_ordenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_ordenActionPerformed
        h=new Herramientas(usr, menu);
        h.session(sessionPrograma);
        buscaOrden obj = new buscaOrden(new javax.swing.JFrame(), true, this.usr, 0, configuracion);
        obj.t_busca.requestFocus();
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        obj.setLocation((d.width/2)-(obj.getWidth()/2), (d.height/2)-(obj.getHeight()/2));
        obj.setVisible(true);
        orden_act=obj.getReturnStatus();
        try{
            if (orden_act!=null)
            {
                model=new MyModel(0, columnas);
                t_datos.setModel(model);
                formatoTabla();
                boolean permiso_nuevo=true;
                if(orden_act.getMetaRefacciones()==null)
                {
                    usrM=null;
                    autorizar4.setSize(284, 177);
                    autorizar4.setLocation((d.width/2)-(autorizar4.getWidth()/2), (d.height/2)-(autorizar4.getHeight()/2));
                    t_user4.setText("");
                    t_clave4.setText("");
                    autorizar4.setVisible(true);
                    if(usrM==null)
                        permiso_nuevo=false;//no se autoriza
                }
                if(permiso_nuevo==true)
                {
                    Session session = HibernateUtil.getSessionFactory().openSession();
                    if(orden_act.getFechaCierre()==null)
                    {
                        Date hoy=new Date();
                        int dias=0;
                        if(orden_act.getMetaRefacciones()!=null)
                            dias=(int) ((orden_act.getMetaRefacciones().getTime()-hoy.getTime())/86400000);
                        boolean permiso=true;
                        if(dias<0 || orden_act.getCierreRefacciones()!=null)
                        {
                            usrM=null;
                            autorizar4.setSize(284, 177);
                            autorizar4.setLocation((d.width/2)-(autorizar4.getWidth()/2), (d.height/2)-(autorizar4.getHeight()/2));
                            t_user4.setText("");
                            t_clave4.setText("");
                            autorizar4.setVisible(true);
                            if(usrM==null)
                                permiso=false;//no se autoriza
                        }
                        if(permiso==true)
                        {
                        
                            h= new Herramientas(usr, menu);
                            h.desbloqueaOrden();
                            String resp=h.estadoOrden(orden_act);
                            if(resp.compareTo("")==0 || resp.compareTo("*bloqueada ok*")==0)
                            {
                                if(orden_act.getVales()!=null && orden_act.getVales()>0)
                                {
                                    t_orden.setText(""+orden_act.getIdOrden());
                                    if(t_orden.getText().compareTo("")!=0)
                                    {
                                        datos_unidad("", "", "", "", "", "","", "","");
                                        try
                                        {
                                            session.beginTransaction().begin();
                                            orden_act = (Orden)session.get(Orden.class, Integer.parseInt(t_orden.getText()));
                                            session.getTransaction().commit();

                                            t_tipo.setText(orden_act.getTipo().getTipoNombre());
                                            t_modelo.setText(Integer.toString(orden_act.getModelo()));
                                            t_id_aseguradora.setText(""+orden_act.getCompania().getIdCompania());
                                            t_aseguradora.setText(orden_act.getCompania().getNombre());
                                            t_asegurado.setText(orden_act.getClientes().getNombre());
                                        }
                                        catch (HibernateException he)
                                        {
                                            he.printStackTrace();
                                            session.getTransaction().rollback();
                                            orden_act=null;
                                            t_tipo.setText("");
                                            t_modelo.setText("");
                                            t_id_aseguradora.setText("");
                                            t_asegurado.setText("");
                                        }
                                        finally
                                        {
                                            session.close();
                                        }
                                    }
                                    else
                                    {
                                        orden_act=null;
                                        datos_unidad("", "", "", "", "", "","","", "");
                                        t_orden.setEnabled(false);
                                        orden_act=null;
                                    }
                                    model=new MyModel(0, columnas);
                                    t_datos.setModel(model);
                                    formatoTabla();
                                }
                                else
                                    JOptionPane.showMessageDialog(null, "Aun no se ha asignado presupuesto para surtimiento!");
                            }
                            else
                            JOptionPane.showMessageDialog(null, "¡Orden Bloqueada por:"+orden_act.getUsuarioByBloqueada().getIdUsuario());
                        }else
                        {
                            if(orden_act.getCierreRefacciones()==null)
                            {
                                session.beginTransaction().begin();
                                orden_act = (Orden)session.get(Orden.class, orden_act.getIdOrden());
                                orden_act.setCierreRefacciones(hoy);
                                session.getTransaction().commit();
                                session.close();
                            }
                            JOptionPane.showMessageDialog(null, "¡El tiempo para surtir la orden ha terminado, comunicate con tus superiores!");

                        }
                    }
                    else
                        JOptionPane.showMessageDialog(null, "¡Orden cerrada!");
                }
                else
                    JOptionPane.showMessageDialog(null, "¡Falta asignar meta de refacciones!");
            }
            else
            {
                h= new Herramientas(usr, menu);
                h.desbloqueaOrden();
                model=new MyModel(0, columnas);
                t_datos.setModel(model);
                formatoTabla();
                sumaTotales();
                t_orden.setText("");
                datos_unidad("", "", "", "", "", "","","","");
                t_orden.setEnabled(false);
                t_orden.requestFocus();
            }
        }catch(Exception e){e.printStackTrace();}
    }//GEN-LAST:event_b_ordenActionPerformed

    private void b_proveedorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_proveedorActionPerformed
        h=new Herramientas(usr, 0);
        h.session(sessionPrograma);
        buscaProveedor obj = new buscaProveedor(new javax.swing.JFrame(), true, this.usr, this.sessionPrograma, 0);
        obj.t_busca.requestFocus();
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        obj.setLocation((d.width/2)-(obj.getWidth()/2), (d.height/2)-(obj.getHeight()/2));
        obj.setVisible(true);
        prov_act=obj.getReturnStatus();
        if (prov_act!=null)
        {
            t_proveedor.setText(""+prov_act.getIdProveedor());
            prov_act=buscarProveedor(prov_act.getIdProveedor());
            l_nombre_aseguradora.setText(prov_act.getNombre());
        }
        else
        {
            t_proveedor.setText("");
            l_nombre_aseguradora.setText("SELECCIONE UN PROVEEDOR");
            l_nombre_aseguradora.setEnabled(false);
            t_proveedor.requestFocus();
        }
    }//GEN-LAST:event_b_proveedorActionPerformed

    private void numerosFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_numerosFocusLost
        // TODO add your handling code here:
        entro=1;
    }//GEN-LAST:event_numerosFocusLost

    private void numeros1FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_numeros1FocusLost
        // TODO add your handling code here:
        entro=1;
    }//GEN-LAST:event_numeros1FocusLost

    private void t_user1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_t_user1ActionPerformed
        // TODO add your handling code here:
        t_clave1.requestFocus();
    }//GEN-LAST:event_t_user1ActionPerformed

    private void t_clave1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_t_clave1ActionPerformed
        // TODO add your handling code here:
        b2.requestFocus();
    }//GEN-LAST:event_t_clave1ActionPerformed

    private void b2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b2ActionPerformed
        // TODO add your handling code here:
        if(t_user1.getText().compareTo("")!=0)
        {
            if(t_clave1.getPassword().toString().compareTo("")!=0)
            {
                Session session = HibernateUtil.getSessionFactory().openSession();
                try
                {
                    session.beginTransaction().begin();
                    Usuario autoriza = (Usuario)session.createCriteria(Usuario.class).add(Restrictions.eq("idUsuario", t_user1.getText())).add(Restrictions.eq("clave", t_clave1.getText())).setMaxResults(1).uniqueResult();
                    if(autoriza!=null)
                    {
                        if(autoriza.getAutorizarSobrecosto()==true)
                        {
                            usrAut1=autoriza;
                            autorizar1.dispose();
                        }
                        else
                        JOptionPane.showMessageDialog(this, "¡el usuario no tiene permiso de autorizar!");
                    }
                    else
                    {
                        session.beginTransaction().rollback();
                        JOptionPane.showMessageDialog(this, "¡Datos Incorrectos!");
                    }
                }catch(Exception e)
                {
                    session.beginTransaction().rollback();
                    JOptionPane.showMessageDialog(this, "¡Error al consultar los datos!");
                    e.printStackTrace();
                }
                finally
                {
                    if(session.isOpen()==true)
                    session.close();
                }
            }
            else
            JOptionPane.showMessageDialog(this, "¡Ingrese la contraseña!");
        }
        else
        JOptionPane.showMessageDialog(this, "¡Ingrese el Usuario!");
    }//GEN-LAST:event_b2ActionPerformed

    private void t_user2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_t_user2ActionPerformed
        // TODO add your handling code here:
        t_clave2.requestFocus();
    }//GEN-LAST:event_t_user2ActionPerformed

    private void t_clave2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_t_clave2ActionPerformed
        // TODO add your handling code here:
        b3.requestFocus();
    }//GEN-LAST:event_t_clave2ActionPerformed

    private void b3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b3ActionPerformed
        // TODO add your handling code here:
        if(t_user2.getText().compareTo("")!=0)
        {
            if(t_clave2.getPassword().toString().compareTo("")!=0)
            {
                Session session = HibernateUtil.getSessionFactory().openSession();
                try
                {
                    session.beginTransaction().begin();
                    Usuario autoriza = (Usuario)session.createCriteria(Usuario.class).add(Restrictions.eq("idUsuario", t_user2.getText())).add(Restrictions.eq("clave", t_clave2.getText())).setMaxResults(1).uniqueResult();
                    if(autoriza!=null)
                    {
                        if(autoriza.getRespaldar()==true)
                        {
                            usrAut2=autoriza;
                            autorizar2.dispose();
                        }
                        else
                        JOptionPane.showMessageDialog(this, "¡el usuario no tiene permiso de autorizar!");
                    }
                    else
                    {
                        session.beginTransaction().rollback();
                        JOptionPane.showMessageDialog(this, "¡Datos Incorrectos!");
                    }
                }catch(Exception e)
                {
                    session.beginTransaction().rollback();
                    JOptionPane.showMessageDialog(this, "¡Error al consultar los datos!");
                    e.printStackTrace();
                }
                finally
                {
                    if(session.isOpen()==true)
                    session.close();
                }
            }
            else
            JOptionPane.showMessageDialog(this, "¡Ingrese la contraseña!");
        }
        else
        JOptionPane.showMessageDialog(this, "¡Ingrese el Usuario!");
    }//GEN-LAST:event_b3ActionPerformed

    private void t_user3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_t_user3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_t_user3ActionPerformed

    private void t_clave3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_t_clave3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_t_clave3ActionPerformed

    private void b4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b4ActionPerformed
        // TODO add your handling code here:
        if(t_user3.getText().compareTo("")!=0)
        {
            if(t_clave3.getPassword().toString().compareTo("")!=0)
            {
                Session session = HibernateUtil.getSessionFactory().openSession();
                try
                {
                    session.beginTransaction().begin();
                    Usuario autoriza = (Usuario)session.createCriteria(Usuario.class).add(Restrictions.eq("idUsuario", t_user3.getText())).add(Restrictions.eq("clave", t_clave3.getText())).setMaxResults(1).uniqueResult();
                    if(autoriza!=null)
                    {
                        if(autoriza.getAutorizaAdicional()==true)
                        {
                            usrA=autoriza;
                            autorizar3.dispose();
                        }
                        else
                        JOptionPane.showMessageDialog(this, "¡el usuario no tiene permiso de autorizar!");
                    }
                    else
                    {
                        session.beginTransaction().rollback();
                        JOptionPane.showMessageDialog(this, "¡Datos Incorrectos!");
                    }
                }catch(Exception e)
                {
                    session.beginTransaction().rollback();
                    JOptionPane.showMessageDialog(this, "¡Error al consultar los datos!");
                    e.printStackTrace();
                }
                finally
                {
                    if(session.isOpen()==true)
                    session.close();
                }
            }
            else
            JOptionPane.showMessageDialog(this, "¡Ingrese la contraseña!");
        }
        else
        JOptionPane.showMessageDialog(this, "¡Ingrese el Usuario!");
    }//GEN-LAST:event_b4ActionPerformed

    private void t_user4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_t_user4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_t_user4ActionPerformed

    private void t_clave4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_t_clave4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_t_clave4ActionPerformed

    private void b5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b5ActionPerformed
        // TODO add your handling code here:
         if(t_user4.getText().compareTo("")!=0)
        {
            if(t_clave4.getPassword().toString().compareTo("")!=0)
            {
                Session session = HibernateUtil.getSessionFactory().openSession();
                try
                {
                    session.beginTransaction().begin();
                    Usuario autoriza = (Usuario)session.createCriteria(Usuario.class).add(Restrictions.eq("idUsuario", t_user4.getText())).add(Restrictions.eq("clave", t_clave4.getText())).setMaxResults(1).uniqueResult();
                    if(autoriza!=null)
                    {
                        if(autoriza.getAutorizarSobrecosto()==true)
                        {
                            usrM=autoriza;
                            autorizar4.dispose();
                        }
                        else
                        JOptionPane.showMessageDialog(this, "¡el usuario no tiene permiso de autorizar!");
                    }
                    else
                    {
                        session.beginTransaction().rollback();
                        JOptionPane.showMessageDialog(this, "¡Datos Incorrectos!");
                    }
                }catch(Exception e)
                {
                    session.beginTransaction().rollback();
                    JOptionPane.showMessageDialog(this, "¡Error al consultar los datos!");
                    e.printStackTrace();
                }
                finally
                {
                    if(session.isOpen()==true)
                    session.close();
                }
            }
            else
            JOptionPane.showMessageDialog(this, "¡Ingrese la contraseña!");
        }
        else
        JOptionPane.showMessageDialog(this, "¡Ingrese el Usuario!");
    }//GEN-LAST:event_b5ActionPerformed

    private void t_cambioFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_t_cambioFocusLost
        // TODO add your handling code here:
        Session session = HibernateUtil.getSessionFactory().openSession();
        try
        {
            session.beginTransaction().begin();
            Configuracion config=(Configuracion)session.get(Configuracion.class, configuracion);
            Date fecha_hoy = new Date();
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String fecha = dateFormat.format(fecha_hoy);
            DecimalFormat df = new DecimalFormat("#.0000");
            
            double valor = Double.parseDouble(t_cambio.getText());
            if(valor > 0.0){
                config.setTipoCambio(valor);
                config.setFechaCambio(fecha_hoy);
                session.update(config);
                session.beginTransaction().commit();
                
                t_cambio.setText(""+df.format(valor));
                t_cambio.setEditable(false);
            }else{
                t_cambio.setText("0.0");
                t_cambio.setEditable(true);
            }
            
        }catch(Exception e)
        {
            session.beginTransaction().rollback();
            e.printStackTrace();
            //JOptionPane.showMessageDialog(null, "Error al consultar la base de datos");
        }
        finally
        {
            if(session!=null)
                if(session.isOpen())
                    session.close();
        }
    }//GEN-LAST:event_t_cambioFocusLost

    private void t_cambioKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_cambioKeyTyped
        // TODO add your handling code here:
        char caracter = evt.getKeyChar();
        // Verificar si la tecla pulsada no es un digito
        if(((caracter < '0') ||
            (caracter > '9')) &&
            (caracter != '\b' /*corresponde a BACK_SPACE*/) && caracter!='.')
         {
            evt.consume();  // ignorar el evento de teclado
         }
    }//GEN-LAST:event_t_cambioKeyTyped

    private void b_archivoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_archivoActionPerformed
        // TODO add your handling code here:
        int estado=selector.showOpenDialog(null);
        if(estado==0)
        {
            archivo = selector.getSelectedFile();
            if(archivo.exists())
                agregaArchivo(archivo.getName(), b_archivo);
        }
        else{
            archivo=null;
            agregaArchivo("Agregar Soporte", b_archivo);
        }
    }//GEN-LAST:event_b_archivoActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JDialog autorizar1;
    private javax.swing.JDialog autorizar2;
    private javax.swing.JDialog autorizar3;
    private javax.swing.JDialog autorizar4;
    private javax.swing.JDialog autorizarCosto;
    private javax.swing.JButton b2;
    private javax.swing.JButton b3;
    private javax.swing.JButton b4;
    private javax.swing.JButton b5;
    private javax.swing.JButton b_archivo;
    private javax.swing.JButton b_aseguradora;
    private javax.swing.JButton b_autorizar;
    private javax.swing.JButton b_busca;
    private javax.swing.JButton b_calendario;
    private javax.swing.JButton b_comprador;
    private javax.swing.JButton b_guardar;
    private javax.swing.JButton b_marca;
    private javax.swing.JButton b_mas;
    private javax.swing.JButton b_menos;
    private javax.swing.JButton b_nuevo_pedido;
    private javax.swing.JButton b_orden;
    private javax.swing.JButton b_pedido;
    private javax.swing.JButton b_proveedor;
    private javax.swing.JButton b_proveedorf;
    private javax.swing.JButton b_tipo;
    private javax.swing.JComboBox c_tipo;
    private javax.swing.JComboBox cb_pago;
    private javax.swing.JPanel centro;
    private javax.swing.JLabel jLabel1;
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
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel l_asegurado;
    private javax.swing.JLabel l_busca1;
    private javax.swing.JLabel l_colonia;
    private javax.swing.JLabel l_cp;
    private javax.swing.JLabel l_direccion;
    private javax.swing.JLabel l_fecha;
    private javax.swing.JLabel l_iva;
    private javax.swing.JLabel l_modelo;
    private javax.swing.JLabel l_nombre;
    private javax.swing.JLabel l_nombre_aseguradora;
    private javax.swing.JLabel l_notas;
    private javax.swing.JLabel l_pedido1;
    private javax.swing.JLabel l_poblacion;
    private javax.swing.JLabel l_rfc;
    private javax.swing.JLabel l_subtotal;
    private javax.swing.JLabel l_total;
    private javax.swing.JComboBox medida;
    private javax.swing.JComboBox numeros;
    private javax.swing.JComboBox numeros1;
    private javax.swing.JPanel p_abajo;
    private javax.swing.JPanel p_arriba;
    private javax.swing.JScrollPane p_centro;
    private javax.swing.JPanel p_interno_centro;
    private javax.swing.JFormattedTextField t_IVA;
    private javax.swing.JTextField t_asegurado;
    private javax.swing.JTextField t_aseguradora;
    private javax.swing.JTextField t_busca;
    private javax.swing.JTextField t_cambio;
    private javax.swing.JTextField t_clave;
    private javax.swing.JPasswordField t_clave1;
    private javax.swing.JPasswordField t_clave2;
    private javax.swing.JPasswordField t_clave3;
    private javax.swing.JPasswordField t_clave4;
    private javax.swing.JTextField t_colonia;
    private javax.swing.JPasswordField t_contra;
    private javax.swing.JTextField t_cp;
    private javax.swing.JTable t_datos;
    private javax.swing.JTextField t_direccion;
    private javax.swing.JTextField t_fecha;
    private javax.swing.JTextField t_folio_externo;
    private javax.swing.JTextField t_id_aseguradora;
    private javax.swing.JTextField t_id_comprador;
    private javax.swing.JTextField t_marca;
    private javax.swing.JTextField t_modelo;
    private javax.swing.JTextField t_nombre;
    private javax.swing.JTextField t_nombre_comprador;
    private javax.swing.JTextArea t_notas;
    private javax.swing.JTextField t_orden;
    private javax.swing.JTextField t_pedido;
    private javax.swing.JTextField t_plazo;
    private javax.swing.JTextField t_poblacion;
    private javax.swing.JTextField t_proveedor;
    private javax.swing.JTextField t_rfc;
    private javax.swing.JTextField t_siniestro;
    private javax.swing.JFormattedTextField t_subtotal;
    private javax.swing.JTextField t_tipo;
    private javax.swing.JFormattedTextField t_total;
    private javax.swing.JTextField t_user;
    private javax.swing.JTextField t_user1;
    private javax.swing.JTextField t_user2;
    private javax.swing.JTextField t_user3;
    private javax.swing.JTextField t_user4;
    private javax.swing.JComboBox tipo;
    // End of variables declaration//GEN-END:variables

    
    private void datos_unidad(String tipo, String marca, String modelo, String serie, String id_aseguradora, String aseguradora, String siniestro, String asegurado, String plazo)
    {
        t_tipo.setText(tipo);
        t_marca.setText(marca);
        t_modelo.setText(modelo);
        //t_serie.setText(serie);
        t_id_aseguradora.setText(id_aseguradora);
        this.t_aseguradora.setText(aseguradora);
        t_siniestro.setText(siniestro);
        t_asegurado.setText(asegurado);    
        t_plazo.setText(plazo);
    }
    private Proveedor buscarProveedor(int id)
    {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try 
        {
            session.beginTransaction().begin();
            Proveedor prov= (Proveedor)session.get(Proveedor.class, id); 
            session.getTransaction().commit();
            return prov;
        } 
        catch (HibernateException he) 
        {
            he.printStackTrace();
            session.getTransaction().rollback();
            return null;
        }
        finally 
        {
            session.close(); 
        }
    }
    

    private void cajasprovf(String nombre, String direccion, String poblacion, String colonia, String cp, String rfc)
    {
        t_nombre.setText(nombre);
        t_direccion.setText(direccion);
        t_poblacion.setText(poblacion);
        t_colonia.setText(colonia);
        t_cp.setText(cp);
        t_rfc.setText(rfc);
    }
    private void sumaTotales()
    {
        double subtotal=0.0;
        double iva;
        for(int ren=0; ren<t_datos.getRowCount(); ren++)
        {
            double multi= Double.parseDouble(String.valueOf(t_datos.getValueAt(ren,7)))*Double.parseDouble(String.valueOf(t_datos.getValueAt(ren,8)));
            t_datos.setValueAt(multi,ren,10);
            double subtotal1= Double.parseDouble(String.valueOf(t_datos.getValueAt(ren,10)));
            subtotal+=subtotal1;
        }
        t_subtotal.setValue(subtotal);
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction().begin();
        Configuracion con = (Configuracion)session.get(Configuracion.class, configuracion);
        if(session.isOpen())
            session.close();
        t_IVA.setValue(iva=subtotal*con.getIva()/100);
        t_total.setValue(subtotal+iva);
    }

    private void tabla_tamaños()
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
                    column.setPreferredWidth(20);
                    break;
                case 2://no parte
                    column.setPreferredWidth(100);
                    if(c_tipo.getSelectedItem().toString().compareTo("Interno")==0)
                    {
                        DefaultCellEditor miEditor = new DefaultCellEditor(numeros);
                        miEditor.setClickCountToStart(2);
                        column.setCellEditor(miEditor); 
                    }
                    break;
                case 3:
                    column.setPreferredWidth(20);
                    break;
                case 4:               
                    column.setPreferredWidth(350);
                    break;
                case 5:               
                    column.setPreferredWidth(10);
                    DefaultCellEditor editor = new DefaultCellEditor(medida);
                    column.setCellEditor(editor); 
                    editor.setClickCountToStart(2);
                    break;
                case 6:               
                    column.setPreferredWidth(10);
                    break;
                case 7:               
                    column.setPreferredWidth(10);
                    break;
                case 8:               
                    column.setPreferredWidth(20);
                    break;
                case 9:               
                    column.setPreferredWidth(10);
                    DefaultCellEditor editor2 = new DefaultCellEditor(tipo);
                    column.setCellEditor(editor2); 
                    editor2.setClickCountToStart(0);
                    break;
                case 10:
                    column.setPreferredWidth(20);
                    break;
                default:
                    column.setPreferredWidth(40);
                    break; 
            }
        }
        JTableHeader header = t_datos.getTableHeader();
        header.setForeground(Color.white);
    }
    
    private int busca(int partida)
    {
        int x=-1;
        for(int ren=0; ren<t_datos.getRowCount(); ren++)
        {
            if(Integer.parseInt(t_datos.getValueAt(ren, 0).toString())==partida)
            {
                x=ren;
                ren=t_datos.getRowCount();
            }
        }
        return x;
    }
    private int buscaInterno(int partida, int sub)
    {
        int x=-1;
        for(int ren=0; ren<t_datos.getRowCount(); ren++)
        {
            if(Integer.parseInt(t_datos.getValueAt(ren, 0).toString())==partida && Integer.parseInt(t_datos.getValueAt(ren, 1).toString())==sub)
            {
                x=ren;
                ren=t_datos.getRowCount();
            }
        }
        return x;
    }
    private Integer guardarPedido(Pedido obj) 
    {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Integer IdPedido = null;
        try 
        {
            session.beginTransaction().begin();
            obj.setPartidaExternas(new HashSet(0));
            Empleado emp=(Empleado)session.get(Empleado.class, Integer.parseInt(this.t_id_comprador.getText()));
            if(emp!=null)
            {
                obj.setEmpleado(emp);
                obj.setTipoCambio(Double.parseDouble(t_cambio.getText()));
                IdPedido=(Integer) session.save(obj);
                obj = (Pedido)session.get(Pedido.class, IdPedido);
                if(this.c_tipo.getSelectedItem().toString().compareTo("Interno")==0)
                {
                    for(int ren=0; ren<t_datos.getRowCount(); ren++)
                    {
                        Partida part=(Partida)session.createCriteria(Partida.class).add(Restrictions.eq("ordenByIdOrden.idOrden", Integer.parseInt(t_orden.getText()))).add(Restrictions.eq("idEvaluacion", Integer.parseInt(t_datos.getValueAt(ren, 0).toString()))).add(Restrictions.eq("subPartida", Integer.parseInt(t_datos.getValueAt(ren, 1).toString()))).setMaxResults(1).uniqueResult();
                        part.setPedido(obj);
                        
                        if(orden_act.getServicio()!=null)
                        {
                            Item reparacion = (Item)session.createCriteria(Item.class).add(Restrictions.eq("catalogo.idCatalogo", part.getCatalogo().getIdCatalogo())).add(Restrictions.eq("servicio.idServicio", orden_act.getServicio().getIdServicio())).setMaxResults(1).uniqueResult();
                            //Item reparacion = (Item)session.get(Item.class, part.getReparacion().getIdReparacion());
                            if(reparacion!=null)
                            {
                                String tipo = part.getTipoPieza().toString().toLowerCase();
                                switch(tipo){
                                    case "ori":
                                        //original
                                        if(part.getPcp()>reparacion.getCostoOri())
                                            reparacion.setCostoOri((double)part.getPcp());
                                        break;
                                    case "nal":
                                        //nacional
                                        if(part.getPcp()>reparacion.getCostoNal())
                                            reparacion.setCostoNal((double)part.getPcp());
                                        break;
                                    case "des":
                                        //desmontado
                                        if(part.getPcp()>reparacion.getCostoDesm())
                                            reparacion.setCostoDesm((double)part.getPcp());
                                        break;
                                }
                                session.update(reparacion);
                            }
                        }
                        session.update(part);
                    }
                    t_fecha.setText(obj.getFechaPedido().toLocaleString());
                    session.beginTransaction().commit();
                }
                if(this.c_tipo.getSelectedItem().toString().compareTo("Inventario")==0)
                {
                    for(int ren=0; ren<t_datos.getRowCount(); ren++)
                    {
                        PartidaExterna px=new PartidaExterna();
                        Ejemplar ej=(Ejemplar)session.get(Ejemplar.class, t_datos.getValueAt(ren, 2).toString());
                        px.setEjemplar(ej);
                        px.setDescripcion(t_datos.getValueAt(ren, 4).toString());
                        px.setFacturado(false);
                        px.setUnidad(t_datos.getValueAt(ren, 5).toString());
                        px.setOp(false);
                        if(t_datos.getValueAt(ren, 6).toString().compareTo("")!=0)
                        {
                            String[] fecha = t_datos.getValueAt(ren, 6).toString().split("-");
                            Calendar calendario = Calendar.getInstance();
                            calendario.set(
                                    Integer.parseInt(fecha[0]), 
                                    Integer.parseInt(fecha[1])-1, 
                                    Integer.parseInt(fecha[2]));
                            px.setPlazo(calendario.getTime());
                        }
                        px.setCantidad((double) t_datos.getValueAt(ren, 7));
                        px.setCosto((double) t_datos.getValueAt(ren, 8));
                        /*if(ej.getPrecio()>0)
                            ej.setPrecio(px.getCosto());
                        else*/
                            ej.setPrecio(px.getCosto());
                        session.update(ej);
                        px.setPedido(obj);
                        px.setOriCon("-");
                        px.setD(0.0);
                        px.setR(0.0);
                        px.setM(0.0);
                        //obj.agregaPartidaExterna(px);
                        session.save(px);
                    }
                    obj.setEmpleado(emp);
                    session.update(obj);
                    session.beginTransaction().commit();
                    t_fecha.setText(obj.getFechaPedido().toLocaleString());
                }
                if(this.c_tipo.getSelectedItem().toString().compareTo("Externo")==0)
                {
                    OrdenExterna ordenx=new OrdenExterna();
                    if(this.t_tipo.getText().compareTo("")!=0)//agregamos el tipo
                    {
                        Tipo tip=(Tipo)session.get(Tipo.class, t_tipo.getText());
                        if(tip!=null)
                            ordenx.setTipo(tip);
                    }
                    if(this.t_marca.getText().compareTo("")!=0)//agregamos la marca
                    {
                        Marca mar=(Marca)session.get(Marca.class, t_marca.getText());
                        if(mar!=null)
                            ordenx.setMarca(mar);
                    }
                    if(this.t_modelo.getText().compareTo("")!=0)//agregamos el modelo
                        ordenx.setModelo(Integer.parseInt(this.t_modelo.getText()));

                    if(this.t_id_aseguradora.getText().compareTo("")!=0)//agregamos la aseguradora
                    {
                        Compania com=(Compania) session.get(Compania.class, Integer.parseInt(this.t_id_aseguradora.getText()));
                        if(com!=null)
                            ordenx.setCompania(com);
                    }
                    if(t_siniestro.getText().compareTo("")!=0)
                        ordenx.setSiniestro(t_siniestro.getText());
                    if(this.t_asegurado.getText().compareTo("")!=0)
                        ordenx.setAsegurado(this.t_asegurado.getText());
                    ordenx.setPedidos(new HashSet(0));
                    ordenx.addPedido(obj);
                    int nOrd=(int)session.save(ordenx);
                    ordenx=(OrdenExterna)session.get(OrdenExterna.class, nOrd);
                    if(ordenx!=null)
                        obj.setOrdenExterna(ordenx);
                    for(int ren=0; ren<t_datos.getRowCount(); ren++)
                    {
                        PartidaExterna px=new PartidaExterna();
                        px.setPartida(t_datos.getValueAt(ren, 0).toString());
                        px.setIdValuacion(t_datos.getValueAt(ren, 1).toString());
                        px.setNoParte(t_datos.getValueAt(ren, 2).toString());
                        px.setDescripcion(t_datos.getValueAt(ren, 4).toString());
                        px.setFacturado(false);
                        px.setUnidad(t_datos.getValueAt(ren, 5).toString());
                        px.setOp(false);
                        if(t_datos.getValueAt(ren, 6).toString().compareTo("")!=0)
                        {
                            String[] fecha = t_datos.getValueAt(ren, 6).toString().split("-");
                            Calendar calendario = Calendar.getInstance();
                            calendario.set(
                                    Integer.parseInt(fecha[0]), 
                                    Integer.parseInt(fecha[1])-1, 
                                    Integer.parseInt(fecha[2]));
                            px.setPlazo(calendario.getTime());
                        }
                        px.setCantidad((double) t_datos.getValueAt(ren, 7));
                        px.setCosto((double) t_datos.getValueAt(ren, 8));
                        px.setPedido(obj);
                        px.setOriCon("-");
                        px.setD(0.0);
                        px.setR(0.0);
                        px.setM(0.0);
                        obj.agregaPartidaExterna(px);
                    }

                    obj.setEmpleado(emp);
                    session.update(obj);
                    session.beginTransaction().commit();
                    t_fecha.setText(obj.getFechaPedido().toLocaleString());
                }
                if(this.c_tipo.getSelectedItem().toString().compareTo("Adicional")==0)
                {
                    for(int ren=0; ren<t_datos.getRowCount(); ren++)
                    {
                        PartidaExterna px=new PartidaExterna();
                        px.setNoParte(t_datos.getValueAt(ren, 2).toString());
                        px.setDescripcion(t_datos.getValueAt(ren, 4).toString());
                        px.setFacturado(false);
                        px.setUnidad(t_datos.getValueAt(ren, 5).toString());
                        px.setOp(false);
                        if(t_datos.getValueAt(ren, 6).toString().compareTo("")!=0)
                        {
                            String[] fecha = t_datos.getValueAt(ren, 6).toString().split("-");
                            Calendar calendario = Calendar.getInstance();
                            calendario.set(
                                    Integer.parseInt(fecha[0]), 
                                    Integer.parseInt(fecha[1])-1, 
                                    Integer.parseInt(fecha[2]));
                            px.setPlazo(calendario.getTime());
                        }
                        px.setCantidad((double) t_datos.getValueAt(ren, 7));
                        px.setCosto((double) t_datos.getValueAt(ren, 8));
                        px.setPedido(obj);
                        px.setOriCon("-");
                        px.setD(0.0);
                        px.setR(0.0);
                        px.setM(0.0);
                        obj.agregaPartidaExterna(px);
                    }

                    obj.setOrden(orden_act);
                    obj.setEmpleado(emp);
                    session.update(obj);
                    session.beginTransaction().commit();
                    t_fecha.setText(obj.getFechaPedido().toLocaleString());
                }
            }
            else
            {
                JOptionPane.showMessageDialog(null, "El empleado ya no existe"); 
                IdPedido = null;
            }
        }
        catch (HibernateException he)
        {
            he.printStackTrace();
            session.getTransaction().rollback();
            IdPedido = null;
        }
        finally
        {
            session.close();
            return IdPedido;
        }
    } 
    
    public class MyModel extends DefaultTableModel
    {
        Class[] types = new Class [] 
        {
                java.lang.String.class/*#*/,
                java.lang.String.class/*R.Valua*/,
                java.lang.String.class/*N° Parte*/,
                java.lang.String.class/*Folio*/,
                java.lang.String.class/*Descripción*/,
                java.lang.String.class/*Medida*/,
                java.lang.Integer.class/*Plazo*/,
                java.lang.Double.class/*Cantidad*/,
                java.lang.Double.class/*Costo c/u*/,
                java.lang.String.class/*Tipo*/,
                java.lang.Double.class/*Total*/
        };
        int ren=0;
        int col=0;
        private List celdaEditable;
        
        public MyModel(int renglones, String columnas[])
        {
            ren=renglones;
            col=columnas.length;
            celdaEditable=new ArrayList();
            //celdaEditable=new boolean[types.length][renglones];
            for(int x=0; x<renglones; x++)
            {
                List aux=new ArrayList();
                for(int y=0; y<types.length; y++)
                    aux.add(false);
                celdaEditable.add(aux);
            }
            this.setDataVector(new Object [renglones][columnas.length], columnas);
        }
        
        @Override
        public int getRowCount() {
            return ren;
        }

        @Override
        public int getColumnCount() {
            return col;
        }
        
        @Override
        public void setValueAt(Object value, int row, int col)
        {
            Vector vector = (Vector)dataVector.elementAt(row);
            //Object celda = ((Vector)dataVector.elementAt(row)).elementAt(col);
            switch(col)
            {
                case 2://no parte
                    if(vector.get(col)==null)
                    {
                        vector.setElementAt(value, col);
                        this.dataVector.setElementAt(vector, row);
                        fireTableCellUpdated(row, col);
                    }
                    else
                    {
                        if(c_tipo.getSelectedItem().toString().compareTo("Interno")==0)
                        {
                            Session session = HibernateUtil.getSessionFactory().openSession();
                            try
                            {
                                session.beginTransaction().begin();
                                usr = (Usuario)session.get(Usuario.class, usr.getIdUsuario());
                                if(usr.getEditaCodigo()==true)
                                {
                                    Partida part=(Partida) session.createCriteria(Partida.class).add(Restrictions.eq("ordenByIdOrden.idOrden", Integer.parseInt(t_orden.getText()))).add(Restrictions.eq("idEvaluacion", Integer.parseInt(t_datos.getValueAt(t_datos.getSelectedRow(), 0).toString()))).add(Restrictions.eq("subPartida", Integer.parseInt(t_datos.getValueAt(t_datos.getSelectedRow(), 1).toString()))).setMaxResults(1).uniqueResult();
                                    if(part!=null)
                                    {
                                        if(c_tipo.getSelectedItem().toString().compareTo("Interno")==0)
                                        {
                                            switch(value.toString())
                                            {
                                                case "Eliminar":
                                                    part.setEjemplar(null);
                                                    session.update(part);
                                                    session.getTransaction().commit();
                                                    vector.setElementAt("", col);
                                                    this.dataVector.setElementAt(vector, row);
                                                    fireTableCellUpdated(row, col);
                                                    if(session.isOpen()==true)
                                                        session.close();
                                                    break;
                                                case "Cancelar":
                                                    break;
                                                case "Nuevo":
                                                    altaEjemplar obj1 = new altaEjemplar(new javax.swing.JFrame(), true, usr, sessionPrograma, 0);
                                                    obj1.t_id_catalogo.setText(t_datos.getValueAt(t_datos.getSelectedRow(), 3).toString());
                                                    obj1.t_catalogo.setText(t_datos.getValueAt(t_datos.getSelectedRow(), 4).toString());
                                                    obj1.t_numero.requestFocus();
                                                    Dimension d1 = Toolkit.getDefaultToolkit().getScreenSize();
                                                    obj1.setLocation((d1.width/2)-(obj1.getWidth()/2), (d1.height/2)-(obj1.getHeight()/2));
                                                    obj1.setVisible(true);
                                                    Ejemplar ejem1=obj1.getReturnStatus();
                                                    if (ejem1!=null)
                                                    {
                                                        Ejemplar ejem = (Ejemplar)session.get(Ejemplar.class, ejem1.getIdParte());
                                                        if(ejem!=null)
                                                        {
                                                            part.setEjemplar(ejem);
                                                            session.update(part);
                                                            session.getTransaction().commit();
                                                            vector.setElementAt(ejem1.getIdParte(), col);
                                                            this.dataVector.setElementAt(vector, row);
                                                            fireTableCellUpdated(row, col);
                                                            if(session.isOpen()==true)
                                                                session.close();
                                                        }
                                                    }
                                                    break;
                                                case "Buscar":
                                                    buscaEjemplar obj = new buscaEjemplar(new javax.swing.JFrame(), true, sessionPrograma, null, 0);
                                                    obj.t_busca.requestFocus();
                                                    Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
                                                    obj.setLocation((d.width/2)-(obj.getWidth()/2), (d.height/2)-(obj.getHeight()/2));
                                                    obj.setVisible(true);
                                                    Ejemplar ejem=obj.getReturnStatus();
                                                    if (ejem!=null)
                                                    {
                                                        ejem = (Ejemplar)session.get(Ejemplar.class, ejem.getIdParte());
                                                        part.setEjemplar(ejem);
                                                        session.update(part);
                                                        session.getTransaction().commit();
                                                        vector.setElementAt(ejem.getIdParte(), col);
                                                        this.dataVector.setElementAt(vector, row);
                                                        fireTableCellUpdated(row, col);
                                                        if(session.isOpen()==true)
                                                            session.close();
                                                    }
                                                    break;
                                                /*default:
                                                    Ejemplar ejem = (Ejemplar)session.get(Ejemplar.class, value.toString());
                                                    if(ejem!=null)
                                                    {
                                                        part.setEjemplar(ejem);
                                                        session.update(part);
                                                        session.getTransaction().commit();
                                                        vector.setElementAt(value, col);
                                                        this.dataVector.setElementAt(vector, row);
                                                        fireTableCellUpdated(row, col);
                                                        if(session.isOpen()==true)
                                                            session.close();
                                                    }
                                                    break;*/
                                            }
                                        }
                                        else
                                        {
                                            if(value.toString().compareTo("")!=0)
                                            {
                                                Ejemplar ejem = (Ejemplar)session.get(Ejemplar.class, value.toString());
                                                if(ejem!=null)
                                                {
                                                    part.setEjemplar(ejem);
                                                    session.update(part);
                                                    session.getTransaction().commit();
                                                    vector.setElementAt(value, col);
                                                    this.dataVector.setElementAt(vector, row);
                                                    fireTableCellUpdated(row, col);
                                                    if(session.isOpen()==true)
                                                        session.close();
                                                }
                                            }
                                            else
                                            {
                                                part.setEjemplar(null);
                                                session.update(part);
                                                session.getTransaction().commit();
                                                vector.setElementAt("", col);
                                                this.dataVector.setElementAt(vector, row);
                                                fireTableCellUpdated(row, col);
                                                if(session.isOpen()==true)
                                                    session.close();
                                            }
                                        }
                                    }
                                    else
                                    {
                                        JOptionPane.showMessageDialog(null, "La partida ya no existe");
                                    }
                                }
                                else
                                {
                                    session.getTransaction().rollback();
                                    if(session.isOpen()==true)
                                        session.close();
                                    JOptionPane.showMessageDialog(null, "¡Acceso denegado!");
                                }
                            }
                            catch(Exception e)
                            {
                                session.getTransaction().rollback();
                                System.out.println(e);
                            }
                            if(session!=null)
                                if(session.isOpen()==true)
                                    session.close();
                        }
                        else
                        {
                            if(value!=null)
                                vector.setElementAt(((String)value).toUpperCase(), col);
                            else
                                vector.setElementAt("", col);
                            this.dataVector.setElementAt(vector, row);
                            fireTableCellUpdated(row, col);
                        }
                    }
                    break;
                case 4://descripcion
                    if(value!=null)
                        vector.setElementAt(((String)value).toUpperCase(), col);
                    else
                        vector.setElementAt("", col);
                    this.dataVector.setElementAt(vector, row);
                    fireTableCellUpdated(row, col);
                    break;
                case 6://plazo
                    vector.setElementAt(value, col);
                    this.dataVector.setElementAt(vector, row);
                    fireTableCellUpdated(row, col);
                    break;
                case 7://cantidad
                    if(vector.get(col)==null)
                    {
                        vector.setElementAt(value, col);
                        this.dataVector.setElementAt(vector, row);
                        fireTableCellUpdated(row, col);
                    }
                    else
                    {
                        if(c_tipo.getSelectedItem().toString().compareTo("Interno")==0)
                        {
                            Session session = HibernateUtil.getSessionFactory().openSession();
                            session.beginTransaction().begin();
                            Partida part=(Partida) session.createCriteria(Partida.class).add(Restrictions.eq("ordenByIdOrden.idOrden", Integer.parseInt(t_orden.getText()))).add(Restrictions.eq("idEvaluacion", Integer.parseInt(t_datos.getValueAt(t_datos.getSelectedRow(), 0).toString()))).add(Restrictions.eq("subPartida", Integer.parseInt(t_datos.getValueAt(t_datos.getSelectedRow(), 1).toString()))).setMaxResults(1).uniqueResult();
                            if((double)value>=0)
                            {
                                if((double)value<=part.getCantidadAut())
                                {
                                    try
                                    {
                                        if(part!=null)
                                        {
                                            part.setCantPcp((double)value);
                                            session.update(part);
                                            session.getTransaction().commit();
                                            vector.setElementAt(value, col);
                                            this.dataVector.setElementAt(vector, row);
                                            fireTableCellUpdated(row, col);
                                            sumaTotales();
                                        }
                                        else
                                            JOptionPane.showMessageDialog(null, "¡Error: El registro no existe!");
                                    }
                                    catch(Exception e)
                                    {
                                        session.getTransaction().rollback();
                                        System.out.println(e);
                                    }
                                    finally
                                    {
                                        if(session.isOpen()==true)
                                            session.close();
                                    }
                                }
                                else
                                    JOptionPane.showMessageDialog(null, "La cantidad mayor es: "+part.getCantidadAut()); 
                            }
                            else
                                JOptionPane.showMessageDialog(null, "El campo no permite números negativos");
                            if(session.isOpen())
                                session.close();
                        }
                        else
                        {
                            vector.setElementAt(value, col);
                            this.dataVector.setElementAt(vector, row);
                            fireTableCellUpdated(row, col);
                        }
                    }
                    sumaTotales();
                    break;
                case 8://Costo c/u
                    if(vector.get(col)==null)
                    {
                        vector.setElementAt(value, col);
                        this.dataVector.setElementAt(vector, row);
                        fireTableCellUpdated(row, col);
                    }
                    else
                    {
                        if(c_tipo.getSelectedItem().toString().compareTo("Interno")==0)
                        {
                            Session session = HibernateUtil.getSessionFactory().openSession();
                            session.beginTransaction().begin();
                            Partida part=(Partida) session.createCriteria(Partida.class).add(Restrictions.eq("ordenByIdOrden.idOrden", Integer.parseInt(t_orden.getText()))).add(Restrictions.eq("idEvaluacion", Integer.parseInt(t_datos.getValueAt(t_datos.getSelectedRow(), 0).toString()))).add(Restrictions.eq("subPartida", Integer.parseInt(t_datos.getValueAt(t_datos.getSelectedRow(), 1).toString()))).setMaxResults(1).uniqueResult();
                            if((double)value>=0)
                            {
                                if((double)value<=part.getCU())//if(Double.parseDouble(value.toString())<=part.getPrecioAutCU())
                                {
                                    try
                                    {
                                        if(part!=null)
                                        {
                                            part.setPcp((double)value);
                                            session.update(part);
                                            session.getTransaction().commit();
                                            vector.setElementAt(value, col);
                                            this.dataVector.setElementAt(vector, row);
                                            fireTableCellUpdated(row, col);
                                        }
                                        else
                                            JOptionPane.showMessageDialog(null, "¡Error: El registro no existe!");
                                    }
                                    catch(Exception e)
                                    {
                                        session.getTransaction().rollback();
                                        System.out.println(e);
                                        JOptionPane.showMessageDialog(null, "Error al actualizar");
                                    }
                                    sumaTotales();
                                }
                                else
                                {
                                    t_user.setText("");
                                    t_contra.setText("");
                                    usrAut=null;
                                    Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
                                    autorizarCosto.setSize(284, 192);//284, 177
                                    autorizarCosto.setLocation((d.width/2)-(autorizarCosto.getWidth()/2), (d.height/2)-(autorizarCosto.getHeight()/2));
                                    t_user.setText("");
                                    t_contra.setText("");
                                    autorizarCosto.setVisible(true);
                                    if(usrAut!=null)
                                    {
                                        Partida par=(Partida) session.createCriteria(Partida.class).add(Restrictions.eq("ordenByIdOrden.idOrden", Integer.parseInt(t_orden.getText()))).add(Restrictions.eq("idEvaluacion", Integer.parseInt(t_datos.getValueAt(t_datos.getSelectedRow(), 0).toString()))).add(Restrictions.eq("subPartida", Integer.parseInt(t_datos.getValueAt(t_datos.getSelectedRow(), 1).toString()))).setMaxResults(1).uniqueResult();
                                        try
                                        {
                                            if(par!=null)
                                            {
                                                par.setUsuario(usr);
                                                par.setPcp((double)value);
                                                session.update(par);
                                                session.getTransaction().commit();
                                                vector.setElementAt(value, col);
                                                this.dataVector.setElementAt(vector, row);
                                                fireTableCellUpdated(row, col);
                                            }
                                            else
                                                JOptionPane.showMessageDialog(null, "¡Error: El pedido no existe!"); 
                                        }
                                        catch(Exception e)
                                        {
                                            session.getTransaction().rollback();
                                            System.out.println(e);
                                            JOptionPane.showMessageDialog(null, "Error al actualizar"); 
                                        }
                                        finally
                                        {
                                            if(session.isOpen()==true)
                                                session.close();
                                        }
                                    }
                                }
                                sumaTotales();
                            }
                            else
                                JOptionPane.showMessageDialog(null, "El campo no permite números negativos");
                            if(session.isOpen())
                                session.close();
                        }
                        else
                        {
                            vector.setElementAt(value, col);
                            this.dataVector.setElementAt(vector, row);
                            fireTableCellUpdated(row, col);
                            sumaTotales();
                        }
                    }
                    break;
                case 9://Tipo
                    if(vector.get(col)==null)
                    {
                        vector.setElementAt(value, col);
                        this.dataVector.setElementAt(vector, row);
                        fireTableCellUpdated(row, col);
                    }
                    else
                    {
                        //tipo
                        if(c_tipo.getSelectedItem().toString().compareTo("Interno")==0)
                        {
                            Session session = HibernateUtil.getSessionFactory().openSession();
                            session.beginTransaction().begin();
                            Partida part=(Partida) session.createCriteria(Partida.class).add(Restrictions.eq("ordenByIdOrden.idOrden", Integer.parseInt(t_orden.getText()))).add(Restrictions.eq("idEvaluacion", Integer.parseInt(t_datos.getValueAt(t_datos.getSelectedRow(), 0).toString()))).add(Restrictions.eq("subPartida", Integer.parseInt(t_datos.getValueAt(t_datos.getSelectedRow(), 1).toString()))).setMaxResults(1).uniqueResult();
                            try{
                                if(part!=null){
                                    String tipo_pieza =value.toString().toLowerCase();
                                    if(tipo_pieza.toString().compareTo("-")!=0)
                                        part.setTipoPieza(tipo_pieza);
                                    else
                                        part.setTipoPieza(null);
                                    
                                    session.update(part);
                                    session.getTransaction().commit();
                                    vector.setElementAt(value, col);
                                    this.dataVector.setElementAt(vector, row);
                                    fireTableCellUpdated(row, col);
                                }
                            }catch(Exception e)
                            {
                                session.getTransaction().rollback();
                                System.out.println(e);
                                JOptionPane.showMessageDialog(null, "Error al actualizar");
                            }
                            sumaTotales();
                            
                            if(session.isOpen())
                                session.close();
                        }else
                        {
                            vector.setElementAt(value, col);
                            this.dataVector.setElementAt(vector, row);
                            fireTableCellUpdated(row, col);
                            sumaTotales();
                        }
                            
                    }
                    break;
                case 10://total
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
        
        public Class getColumnClass(int columnIndex) 
        {
            return types [columnIndex];
        }

        public boolean isCellEditable(int rowIndex, int columnIndex) 
        {
            List aux=(List)celdaEditable.get(rowIndex);
            return (boolean)aux.get(columnIndex);
        }
        
        @Override
        public void removeRow(int row) 
        {
            dataVector.remove(row);
            celdaEditable.remove(row);
            fireTableRowsDeleted(row, row);
            this.ren=ren-1;
        }
        
        @Override
        public void addRow(Object[] vec) 
        {
            insertRow(getRowCount(), vec);
        }
        
        @Override
        public void insertRow(int row, Object[] rowData) {
            Vector vector=new Vector();
            for(int x=0; x<rowData.length; x++)
                vector.add(rowData[x]);
            dataVector.insertElementAt(vector, row);
            List aux=new ArrayList();
            for(int x=0; x<rowData.length; x++)
            {
                if(x==7 || x==8)
                    aux.add(true);
                else
                    aux.add(false);
            }
            celdaEditable.add(ren, aux);
            fireTableRowsInserted(row, row);
            this.ren=ren+1;
            
        }
        
        public void setCeldaEditable(int fila, int columna,  boolean editable)
        {
            List aux=(List)celdaEditable.get(fila);
            aux.set(columna, editable);
        }

    
        public void setColumnaEditable(int columna, boolean editable)
        {
            for(int i=0; i<celdaEditable.size(); i++)
            {
                List aux=(List)celdaEditable.get(i);
                aux.set(columna, editable);
            }
        }
        
    }
    void estadoBotones()
    {
        this.c_tipo.setEnabled(false);
        t_plazo.setEnabled(false);
        b_calendario.setEnabled(false); 
        t_folio_externo.setEditable(false);
        b_comprador.setEnabled(false);
        b_orden.setEnabled(false);
        b_tipo.setEnabled(false);
        b_marca.setEnabled(false);
        t_modelo.setEditable(false);
        t_siniestro.setEditable(false);
        t_asegurado.setEditable(false);
        b_aseguradora.setEnabled(false);
        b_proveedor.setEnabled(false);
        b_proveedorf.setEnabled(false);
        t_datos.setEnabled(false);
        b_mas.setEnabled(false);
        b_menos.setEnabled(false);
        t_notas.setEditable(false);
        
        b_guardar.setEnabled(false);
        b_nuevo_pedido.setEnabled(true);
        b_pedido.setEnabled(true);
        b_archivo.setEnabled(false);
    }
    
    void envia(String no)
    {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try
        {
            session.beginTransaction().begin();
            Usuario[] autoriza = (Usuario[])session.createCriteria(Usuario.class).add(Restrictions.eq("autorizarPedidos", true)).list().toArray(new Usuario[0]);
            usr = (Usuario) session.get(Usuario.class, usr.getIdUsuario());
            if(autoriza!=null)
            {
                String correos="";
                for(int y=0; y<autoriza.length; y++)
                {
                    correos+=autoriza[y].getEmpleado().getEmail()+";";
                }
                String suc="Toluca";
                if(ruta.compareToIgnoreCase("tbstultitlan.ddns.net")==0)
                    suc="Tultitlan";
                else{
                    if(ruta.compareToIgnoreCase("set-toluca.ddns.net")==0)
                        suc="Merida";
                }
                miCorreo= new EnviaCorreo("Nuevo Pedido de "+suc+"("+no+")", "Hola buen dia, se te comunica que se genero el pedido No:"+no+" para que sea revisado y autorizado saludos.", correos, usr.getEmpleado().getEmail());
            }
            session.beginTransaction().rollback();
        }catch(Exception e)
        {
            e.printStackTrace();
            session.getTransaction().rollback();
        }
        finally
        {
            if(session.isOpen())
                session.close();
        }
    }
    
    
    void agregaArchivo(String nombre, JButton bt)
    {
        if(nombre.contains(".pdf") || nombre.contains(".PDF"))
            bt.setIcon(new ImageIcon("imagenes/pdf.png"));
        else
        {
            if(nombre.contains(".docx") || nombre.contains(".DOCX"))
                bt.setIcon(new ImageIcon("imagenes/word.png"));
            else
                bt.setIcon(new ImageIcon("imagenes/desconocido.png"));
        }
        bt.setText(nombre);
   }
    boolean subeArchivo(String pedido){
        Ftp miFtp=new Ftp();
        boolean respuesta=true;
        respuesta=miFtp.conectar(ruta, "compras", "04650077", 3310);
        if(respuesta==true){
            if(miFtp.cambiarDirectorio("/soporte/")!=true)
            {
                if(miFtp.crearDirectorio("/soporte/")==true)
                    miFtp.cambiarDirectorio("/soporte/");
            }
            String aux = archivo.getName().substring(archivo.getName().lastIndexOf(".") + 1);
            respuesta=miFtp.subirArchivo(archivo.getPath(), pedido+"."+aux);
            if(respuesta==true)
            {
                //FTPFile[] lista=miFtp.listarArchivos();
                ///for(int x=0; x<lista.length; x++)
                //{
                    //miFtp.AbrirArchivo(archivo.getName());
                //}
                
                Session session = HibernateUtil.getSessionFactory().openSession();
                try
                {
                    session.beginTransaction().begin();
                    Pedido ped = (Pedido)session.get(Pedido.class, Integer.parseInt(pedido));
                    if(ped!=null){
                        ped.setSoporte(true);
                        session.update(ped);
                    }
                    session.beginTransaction().commit();
                    respuesta=true;
                }catch(Exception e)
                {
                    e.printStackTrace();
                    session.getTransaction().rollback();
                    respuesta=false;
                }
                finally
                {
                    if(session.isOpen())
                        session.close();
                }
                miFtp.desconectar();
            }
            else
            {
                agregaArchivo("Agregar Soporte", b_archivo);
                JOptionPane.showMessageDialog(null, "No fue posible subir el archivo");
            }
        }
        else
        {
            agregaArchivo("Agregar Soporte", b_archivo);
            JOptionPane.showMessageDialog(null, "No fue posible conectar al servidor FTP");
        }
        return respuesta;
    }
}