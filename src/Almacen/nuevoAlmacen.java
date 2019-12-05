package Almacen;

import Compras.buscaPedido;
import Empleados.buscaEmpleado;
import Hibernate.Util.HibernateUtil;
import Hibernate.entidades.Almacen;
import Hibernate.entidades.Catalogo;
import Hibernate.entidades.Configuracion;
import Hibernate.entidades.Ejemplar;
import Hibernate.entidades.Empleado;
import Hibernate.entidades.Marca;
import Hibernate.entidades.Movimiento;
import Hibernate.entidades.Orden;
import Hibernate.entidades.OrdenExterna;
import Hibernate.entidades.Partida;
import Hibernate.entidades.PartidaExterna;
import Hibernate.entidades.Pedido;
import Hibernate.entidades.Tipo;
import Hibernate.entidades.TrabajoExtra;
import Hibernate.entidades.Usuario;
import Hibernate.entidades.XCobrar;
import Servicios.buscaOrden;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Vector;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import Integral.FormatoEditor;
import Integral.FormatoTabla;
import Integral.Herramientas;
import Integral.Render1;
import Operaciones.buscaExtra;
import Operaciones.buscaResponsable;
import java.io.BufferedReader;
import java.io.FileReader;
import java.math.BigDecimal;
import java.util.Properties;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import org.hibernate.Criteria;
/**
 * @author ESPECIALIZADO TOLUCA
 */
public class nuevoAlmacen extends javax.swing.JPanel {
    Usuario usr;
    Usuario usrAut;
    FormatoTabla formato;
    String sessionPrograma="";
    Herramientas h;
    int menu;
    nuevoAlmacen.MyModel model;
    public Orden orden_act=null;
    public Partida part_act=null;
    public Pedido pedido=null;    
    Almacen miAlmacen;
    String miTitulo="ENTRADA DE MATERIAL DE PEDIDO";
    int iva=0;
    String id_empleado="", id_trabajo="";;
    int configuracion=1;
    /**0
     * Creates new form nuevAlmacen
     */
    public nuevoAlmacen(Usuario usuario, String ses, int op, int configuracion) {
        initComponents();
        Session session = HibernateUtil.getSessionFactory().openSession();
        Configuracion con = (Configuracion)session.get(Configuracion.class, 1);
        iva=con.getIva();
        this.configuracion=configuracion;
        if(session.isOpen())
            session.close();
       
        cb_especialidad.setVisible(false);
        menu=op;
        usr=usuario;
        if(usr.getMovimientoAlmacen()==true && usr.getMovimientoAlmacen2()==true)
            cb_almacen.setEnabled(true);
        else
        {
            if(usr.getMovimientoAlmacen()==true)
                cb_almacen.setSelectedIndex(0);
            else
                cb_almacen.setSelectedIndex(1);
            cb_almacen.setEnabled(false);
        }
        
        sessionPrograma=ses;
        formato = new FormatoTabla(); 
        String[] columnas = new String [] {"Id","N°","#","N° Parte","Descripción","Medida","Pedidos","X Surtir","Entrada","Costo c/u","Total"};
        Class[] types = new Class [] 
        {
            java.lang.String.class, java.lang.String.class, java.lang.String.class,
            java.lang.String.class, java.lang.String.class, java.lang.String.class,
            java.lang.Double.class, java.lang.Double.class, java.lang.Double.class,
            java.lang.Double.class, java.lang.Double.class
        };
        model=new MyModel(0, columnas, types);
        t_datos.setModel(model);
        t_datos.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        sumaTotales();
        formatoTabla();
        titulos();
        b_buscapedido.setEnabled(true);
        b_buscaorden.setEnabled(false);
        cb_sin_orden.setVisible(false);
        cb_sin_orden.setSelected(false);
    }
    public void formatoTabla()
    {
        Color c1 = new java.awt.Color(2, 135, 242); //2, 135, 242  //m 90,66,126
        for(int x=0; x<t_datos.getColumnModel().getColumnCount(); x++)
        {
            t_datos.getColumnModel().getColumn(x).setHeaderRenderer(new Render1(c1));
        }
        tabla_tamaños();
        t_datos.setShowVerticalLines(true);
        t_datos.setShowHorizontalLines(true);
        t_datos.setDefaultRenderer(Double.class, formato); 
        t_datos.setDefaultRenderer(Integer.class, formato);
        t_datos.setDefaultRenderer(String.class, formato);
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

        codigo = new javax.swing.JComboBox();
        autoriza = new javax.swing.JDialog();
        jPanel5 = new javax.swing.JPanel();
        t_contra = new javax.swing.JPasswordField();
        jLabel2 = new javax.swing.JLabel();
        t_user = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        b_autorizar = new javax.swing.JButton();
        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        c_tmovimiento = new javax.swing.JComboBox();
        c_toperacion = new javax.swing.JComboBox();
        datos = new javax.swing.JDialog();
        jPanel3 = new javax.swing.JPanel();
        t_serie = new javax.swing.JTextField();
        l_serie = new javax.swing.JLabel();
        l_tipo = new javax.swing.JLabel();
        t_tipo = new javax.swing.JTextField();
        t_marca = new javax.swing.JTextField();
        l_marca = new javax.swing.JLabel();
        l_modelo = new javax.swing.JLabel();
        t_modelo = new javax.swing.JTextField();
        l_siniestro = new javax.swing.JLabel();
        t_siniestro = new javax.swing.JTextField();
        l_asegurado = new javax.swing.JLabel();
        t_asegurado = new javax.swing.JTextField();
        l_compania = new javax.swing.JLabel();
        t_compania = new javax.swing.JTextField();
        l_nreferencia = new javax.swing.JLabel();
        t_nreferencia = new javax.swing.JTextField();
        reportes = new javax.swing.JDialog();
        cb_acondicionamiento = new javax.swing.JCheckBox();
        jPanel2 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jButton10 = new javax.swing.JButton();
        jButton11 = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        jPanelMalmacen = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jPanelProveedor = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        b_buscapedido = new javax.swing.JButton();
        t_pedido = new javax.swing.JTextField();
        l_tipo_pedido = new javax.swing.JLabel();
        b_buscaorden = new javax.swing.JButton();
        t_orden = new javax.swing.JTextField();
        cb_sin_orden = new javax.swing.JCheckBox();
        b_detalles = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        buscar = new javax.swing.JButton();
        cb_1 = new javax.swing.JComboBox();
        t_er = new javax.swing.JTextField();
        cb_especialidad = new javax.swing.JComboBox();
        jPanel7 = new javax.swing.JPanel();
        t_fecha = new javax.swing.JTextField();
        l_fecha = new javax.swing.JLabel();
        l_nmovimiento = new javax.swing.JLabel();
        t_nmovimiento = new javax.swing.JTextField();
        r2 = new javax.swing.JRadioButton();
        r1 = new javax.swing.JRadioButton();
        t_folio = new javax.swing.JTextField();
        jScrollPane4 = new javax.swing.JScrollPane();
        t_datos = new javax.swing.JTable();
        jPanelM = new javax.swing.JPanel();
        jPanelOperaciones = new javax.swing.JPanel();
        l_iva = new javax.swing.JLabel();
        t_IVA = new javax.swing.JFormattedTextField();
        t_subtotal = new javax.swing.JFormattedTextField();
        l_subtotal = new javax.swing.JLabel();
        l_total = new javax.swing.JLabel();
        t_total = new javax.swing.JFormattedTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        t_notas = new javax.swing.JTextArea();
        b_guardar = new javax.swing.JButton();
        b_mas = new javax.swing.JButton();
        b_menos = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        f1 = new javax.swing.JRadioButton();
        f2 = new javax.swing.JRadioButton();
        f3 = new javax.swing.JRadioButton();
        f4 = new javax.swing.JRadioButton();
        f5 = new javax.swing.JRadioButton();
        cb_almacen = new javax.swing.JComboBox();

        autoriza.setModalExclusionType(null);
        autoriza.setModalityType(java.awt.Dialog.ModalityType.APPLICATION_MODAL);

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "Autorizar entrega de material", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP));

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
        t_user.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                t_userKeyTyped(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Arial", 0, 10)); // NOI18N
        jLabel1.setText("Usuario:");

        b_autorizar.setFont(new java.awt.Font("Arial", 0, 10)); // NOI18N
        b_autorizar.setText("Autorizar");
        b_autorizar.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                b_autorizarFocusLost(evt);
            }
        });
        b_autorizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_autorizarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addGap(0, 208, Short.MAX_VALUE)
                .addComponent(b_autorizar))
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(t_contra)
                    .addComponent(t_user)))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(t_user, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(t_contra, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(b_autorizar)
                .addContainerGap())
        );

        javax.swing.GroupLayout autorizaLayout = new javax.swing.GroupLayout(autoriza.getContentPane());
        autoriza.getContentPane().setLayout(autorizaLayout);
        autorizaLayout.setHorizontalGroup(
            autorizaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        autorizaLayout.setVerticalGroup(
            autorizaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        c_tmovimiento.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Entrada", "Salida" }));
        c_tmovimiento.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                c_tmovimientoItemStateChanged(evt);
            }
        });

        c_toperacion.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Pedido", "Compañía", "Operarios", "Venta", "Inventario" }));
        c_toperacion.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                c_toperacionItemStateChanged(evt);
            }
        });

        datos.setTitle("Datos de la Orden de Taller");
        datos.setBackground(new java.awt.Color(255, 255, 255));
        datos.setModalExclusionType(null);
        datos.setModalityType(java.awt.Dialog.ModalityType.APPLICATION_MODAL);

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));

        t_serie.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        t_serie.setEnabled(false);

        l_serie.setFont(new java.awt.Font("Arial", 0, 10)); // NOI18N
        l_serie.setText("Serie:");

        l_tipo.setFont(new java.awt.Font("Arial", 0, 10)); // NOI18N
        l_tipo.setText("Tipo:");

        t_tipo.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        t_tipo.setEnabled(false);

        t_marca.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        t_marca.setEnabled(false);

        l_marca.setFont(new java.awt.Font("Arial", 0, 10)); // NOI18N
        l_marca.setText("Marca:");

        l_modelo.setFont(new java.awt.Font("Arial", 0, 10)); // NOI18N
        l_modelo.setText("Mod:");

        t_modelo.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        t_modelo.setEnabled(false);

        l_siniestro.setFont(new java.awt.Font("Arial", 0, 10)); // NOI18N
        l_siniestro.setText("Siniestro:");

        t_siniestro.setEditable(false);
        t_siniestro.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        t_siniestro.setEnabled(false);

        l_asegurado.setFont(new java.awt.Font("Arial", 0, 10)); // NOI18N
        l_asegurado.setText("Asegurado:");

        t_asegurado.setEditable(false);
        t_asegurado.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        t_asegurado.setEnabled(false);

        l_compania.setFont(new java.awt.Font("Arial", 0, 10)); // NOI18N
        l_compania.setText("Compañía:");

        t_compania.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        t_compania.setEnabled(false);

        l_nreferencia.setFont(new java.awt.Font("Arial", 0, 10)); // NOI18N
        l_nreferencia.setText("N° Ref:");

        t_nreferencia.setEditable(false);
        t_nreferencia.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        t_nreferencia.setEnabled(false);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(l_asegurado)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(t_asegurado))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(9, 9, 9)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(11, 11, 11)
                                .addComponent(l_marca))
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(l_siniestro)
                                .addGroup(jPanel3Layout.createSequentialGroup()
                                    .addGap(16, 16, 16)
                                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(l_serie)
                                        .addComponent(l_tipo)))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(t_tipo, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(l_modelo)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(t_modelo, javax.swing.GroupLayout.DEFAULT_SIZE, 91, Short.MAX_VALUE))
                            .addComponent(t_serie, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(t_siniestro)
                            .addComponent(t_marca)))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(l_nreferencia)
                            .addComponent(l_compania))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(t_compania)
                            .addComponent(t_nreferencia))))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(l_marca)
                    .addComponent(t_marca, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(l_tipo)
                    .addComponent(t_tipo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(l_modelo)
                    .addComponent(t_modelo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(l_serie)
                    .addComponent(t_serie, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(l_siniestro)
                    .addComponent(t_siniestro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(l_asegurado)
                    .addComponent(t_asegurado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(l_compania)
                    .addComponent(t_compania, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(l_nreferencia)
                    .addComponent(t_nreferencia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout datosLayout = new javax.swing.GroupLayout(datos.getContentPane());
        datos.getContentPane().setLayout(datosLayout);
        datosLayout.setHorizontalGroup(
            datosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        datosLayout.setVerticalGroup(
            datosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        reportes.setModalityType(java.awt.Dialog.ModalityType.APPLICATION_MODAL);

        cb_acondicionamiento.setText("acondicionamiento");
        cb_acondicionamiento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cb_acondicionamientoActionPerformed(evt);
            }
        });

        setLayout(new java.awt.BorderLayout());

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        jButton1.setBackground(Color.RED);
        jButton1.setIcon(new ImageIcon("imagenes/ent-ped.png"));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setBackground(new java.awt.Color(51, 51, 255));
        jButton2.setIcon(new ImageIcon("imagenes/sal-ped.png"));
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setBackground(new java.awt.Color(51, 51, 255));
        jButton3.setIcon(new ImageIcon("imagenes/ent-com.png"));
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setBackground(new java.awt.Color(51, 51, 255));
        jButton4.setIcon(new ImageIcon("imagenes/sal-com.png"));
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton5.setBackground(new java.awt.Color(51, 51, 255));
        jButton5.setIcon(new ImageIcon("imagenes/sal-op.png"));
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jButton6.setBackground(new java.awt.Color(51, 51, 255));
        jButton6.setIcon(new ImageIcon("imagenes/ent-op.png"));
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jButton7.setBackground(new java.awt.Color(51, 51, 255));
        jButton7.setIcon(new ImageIcon("imagenes/sal-ven.png"));
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        jButton8.setBackground(new java.awt.Color(51, 51, 255));
        jButton8.setIcon(new ImageIcon("imagenes/ent-ven.png"));
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        jButton10.setBackground(new java.awt.Color(51, 51, 255));
        jButton10.setIcon(new ImageIcon("imagenes/ent-int.png"));
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });

        jButton11.setBackground(new java.awt.Color(51, 51, 255));
        jButton11.setIcon(new ImageIcon("imagenes/sal-int.png"));
        jButton11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton11ActionPerformed(evt);
            }
        });

        jButton9.setBackground(new java.awt.Color(51, 51, 255));
        jButton9.setForeground(new java.awt.Color(204, 255, 0));
        jButton9.setText("Reportes");
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton11, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton10, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton9)
                .addGap(0, 5, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jButton9, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
                    .addComponent(jButton2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
                    .addComponent(jButton3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
                    .addComponent(jButton4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
                    .addComponent(jButton5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
                    .addComponent(jButton6, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
                    .addComponent(jButton7, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
                    .addComponent(jButton8, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
                    .addComponent(jButton11, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
                    .addComponent(jButton10, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE))
                .addGap(0, 5, Short.MAX_VALUE))
        );

        add(jPanel2, java.awt.BorderLayout.NORTH);

        jPanelMalmacen.setBackground(new java.awt.Color(254, 254, 254));
        jPanelMalmacen.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), miTitulo, javax.swing.border.TitledBorder.RIGHT, javax.swing.border.TitledBorder.ABOVE_TOP));

        jPanel8.setBackground(new java.awt.Color(51, 51, 255));

        jPanelProveedor.setBackground(new java.awt.Color(51, 51, 255));

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));

        b_buscapedido.setBackground(new java.awt.Color(51, 51, 255));
        b_buscapedido.setForeground(new java.awt.Color(255, 255, 255));
        b_buscapedido.setText("Pedido");
        b_buscapedido.setEnabled(false);
        b_buscapedido.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_buscapedidoActionPerformed(evt);
            }
        });

        t_pedido.setEditable(false);
        t_pedido.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(0, 0, 0)));
        t_pedido.setOpaque(false);

        l_tipo_pedido.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(0, 0, 0)));

        b_buscaorden.setBackground(new java.awt.Color(51, 51, 255));
        b_buscaorden.setForeground(new java.awt.Color(255, 255, 255));
        b_buscaorden.setText("Orden");
        b_buscaorden.setEnabled(false);
        b_buscaorden.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_buscaordenActionPerformed(evt);
            }
        });

        t_orden.setEditable(false);
        t_orden.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(0, 0, 0)));
        t_orden.setOpaque(false);

        cb_sin_orden.setText("Sin Orden");
        cb_sin_orden.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cb_sin_ordenActionPerformed(evt);
            }
        });

        b_detalles.setBackground(new java.awt.Color(51, 51, 255));
        b_detalles.setForeground(new java.awt.Color(255, 255, 255));
        b_detalles.setText("?");
        b_detalles.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_detallesActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(b_buscapedido)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(l_tipo_pedido, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(t_pedido, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(b_buscaorden)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(t_orden, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(b_detalles, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(cb_sin_orden))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(t_orden, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(b_detalles, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cb_sin_orden))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(t_pedido, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(8, 8, 8)
                        .addComponent(l_tipo_pedido, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(b_buscapedido, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(b_buscaorden, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(11, Short.MAX_VALUE))
        );

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));
        jPanel6.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));

        buscar.setBackground(new java.awt.Color(51, 51, 255));
        buscar.setForeground(new java.awt.Color(255, 255, 255));
        buscar.setText("Entrego");
        buscar.setPreferredSize(new java.awt.Dimension(32, 8));
        buscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buscarActionPerformed(evt);
            }
        });

        cb_1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "SINIESTRO", "ADICIONAL" }));
        cb_1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cb_1ItemStateChanged(evt);
            }
        });
        cb_1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cb_1ActionPerformed(evt);
            }
        });

        t_er.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        t_er.setNextFocusableComponent(b_guardar);
        t_er.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                t_erActionPerformed(evt);
            }
        });
        t_er.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                t_erKeyTyped(evt);
            }
        });

        cb_especialidad.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "SELECCIONAR", "HOJALATERIA", "MECANICA", "SUSPENSION", "ELECTRICO", "PINTURA", "ADICIONAL" }));
        cb_especialidad.setEnabled(false);
        cb_especialidad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cb_especialidadActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(buscar, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(t_er)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(cb_1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(cb_especialidad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cb_especialidad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cb_1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(t_er, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(buscar, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jPanel7.setBackground(new java.awt.Color(255, 255, 255));
        jPanel7.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));

        t_fecha.setEditable(false);
        t_fecha.setBackground(new java.awt.Color(255, 255, 255));
        t_fecha.setText("DD/MM/AAAA");
        t_fecha.setToolTipText("fecha para refacciones");
        t_fecha.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(0, 0, 0)));

        l_fecha.setFont(new java.awt.Font("Arial", 0, 10)); // NOI18N
        l_fecha.setText("Fecha:");

        l_nmovimiento.setFont(new java.awt.Font("Arial", 0, 10)); // NOI18N
        l_nmovimiento.setText("N° Mov.");

        t_nmovimiento.setEditable(false);
        t_nmovimiento.setBackground(new java.awt.Color(255, 255, 255));
        t_nmovimiento.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(0, 0, 0)));

        buttonGroup1.add(r2);
        r2.setSelected(true);
        r2.setText("Remisión");
        r2.setEnabled(false);

        buttonGroup1.add(r1);
        r1.setText("Factura");
        r1.setEnabled(false);

        t_folio.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        t_folio.setEnabled(false);
        t_folio.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                t_folioKeyTyped(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(r1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(r2))
                    .addComponent(t_folio, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(l_nmovimiento)
                    .addComponent(l_fecha))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(t_fecha)
                    .addComponent(t_nmovimiento, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(l_nmovimiento)
                    .addComponent(t_nmovimiento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(r1)
                    .addComponent(r2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(t_fecha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(l_fecha)
                    .addComponent(t_folio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(13, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanelProveedorLayout = new javax.swing.GroupLayout(jPanelProveedor);
        jPanelProveedor.setLayout(jPanelProveedorLayout);
        jPanelProveedorLayout.setHorizontalGroup(
            jPanelProveedorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelProveedorLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanelProveedorLayout.setVerticalGroup(
            jPanelProveedorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelProveedorLayout.createSequentialGroup()
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelProveedorLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        t_datos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "N°", "#", "N° Parte", "Descripción", "Medida", "Pedidos", "X Surtir", "Cantidad", "Costo c/u", "Total"
            }
        ));
        t_datos.setFillsViewportHeight(true);
        t_datos.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        t_datos.getTableHeader().setReorderingAllowed(false);
        jScrollPane4.setViewportView(t_datos);
        t_datos.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);

        jPanelM.setBackground(new java.awt.Color(51, 51, 255));

        jPanelOperaciones.setBackground(new java.awt.Color(51, 51, 255));
        jPanelOperaciones.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        l_iva.setFont(new java.awt.Font("Arial", 0, 9)); // NOI18N
        l_iva.setForeground(new java.awt.Color(255, 255, 255));
        l_iva.setText("I.V.A.:");
        jPanelOperaciones.add(l_iva, new org.netbeans.lib.awtextra.AbsoluteConstraints(21, 20, -1, -1));

        t_IVA.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        t_IVA.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.00"))));
        t_IVA.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        t_IVA.setText("0.00");
        t_IVA.setEnabled(false);
        t_IVA.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        jPanelOperaciones.add(t_IVA, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 20, 88, -1));

        t_subtotal.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        t_subtotal.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.00"))));
        t_subtotal.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        t_subtotal.setText("0.00");
        t_subtotal.setEnabled(false);
        t_subtotal.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        jPanelOperaciones.add(t_subtotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 0, 88, -1));

        l_subtotal.setFont(new java.awt.Font("Arial", 0, 9)); // NOI18N
        l_subtotal.setForeground(new java.awt.Color(255, 255, 255));
        l_subtotal.setText("Subtotal:");
        jPanelOperaciones.add(l_subtotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(12, 4, -1, -1));

        l_total.setFont(new java.awt.Font("Arial", 0, 9)); // NOI18N
        l_total.setForeground(new java.awt.Color(255, 255, 255));
        l_total.setText("Total:");
        jPanelOperaciones.add(l_total, new org.netbeans.lib.awtextra.AbsoluteConstraints(23, 40, -1, -1));

        t_total.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        t_total.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.00"))));
        t_total.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        t_total.setText("0.00");
        t_total.setEnabled(false);
        t_total.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        jPanelOperaciones.add(t_total, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 40, 88, -1));

        jScrollPane2.setBackground(new java.awt.Color(51, 51, 255));
        jScrollPane2.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        t_notas.setColumns(20);
        t_notas.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        t_notas.setRows(5);
        t_notas.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                t_notasKeyTyped(evt);
            }
        });
        jScrollPane2.setViewportView(t_notas);

        b_guardar.setFont(new java.awt.Font("Tahoma", 0, 9)); // NOI18N
        b_guardar.setText("Guardar");
        b_guardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_guardarActionPerformed(evt);
            }
        });

        b_mas.setIcon(new ImageIcon("imagenes/boton_mas.png"));
        b_mas.setToolTipText("Agrega una partida");
        b_mas.setEnabled(false);
        b_mas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_masActionPerformed(evt);
            }
        });

        b_menos.setIcon(new ImageIcon("imagenes/boton_menos.png"));
        b_menos.setMnemonic('x');
        b_menos.setToolTipText("Elimina la partida seleccionada");
        b_menos.setEnabled(false);
        b_menos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_menosActionPerformed(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(51, 51, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255), 1, true), "filtrar por:", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(254, 254, 254))); // NOI18N

        buttonGroup2.add(f1);
        f1.setForeground(new java.awt.Color(254, 254, 254));
        f1.setText("Hojalateria");
        f1.setBorder(null);
        f1.setEnabled(false);
        f1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                f1ActionPerformed(evt);
            }
        });

        buttonGroup2.add(f2);
        f2.setForeground(new java.awt.Color(254, 254, 254));
        f2.setText("Mecanica");
        f2.setBorder(null);
        f2.setEnabled(false);
        f2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                f2ActionPerformed(evt);
            }
        });

        buttonGroup2.add(f3);
        f3.setForeground(new java.awt.Color(254, 254, 254));
        f3.setText("Suspensión");
        f3.setBorder(null);
        f3.setEnabled(false);
        f3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                f3ActionPerformed(evt);
            }
        });

        buttonGroup2.add(f4);
        f4.setForeground(new java.awt.Color(254, 254, 254));
        f4.setText("Electricidad");
        f4.setBorder(null);
        f4.setEnabled(false);
        f4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                f4ActionPerformed(evt);
            }
        });

        buttonGroup2.add(f5);
        f5.setForeground(new java.awt.Color(254, 254, 254));
        f5.setSelected(true);
        f5.setText("Todos");
        f5.setBorder(null);
        f5.setEnabled(false);
        f5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                f5ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(f1)
                    .addComponent(f2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(f3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(f5))
                    .addComponent(f4))
                .addContainerGap(24, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(f1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(f2))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(f3)
                    .addComponent(f5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(f4))
        );

        cb_almacen.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "ALMACEN 1", "ALMACEN 2" }));
        cb_almacen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cb_almacenActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelMLayout = new javax.swing.GroupLayout(jPanelM);
        jPanelM.setLayout(jPanelMLayout);
        jPanelMLayout.setHorizontalGroup(
            jPanelMLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelMLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelMLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelMLayout.createSequentialGroup()
                        .addComponent(b_mas, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(b_menos, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(cb_almacen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 538, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(b_guardar, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanelOperaciones, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanelMLayout.setVerticalGroup(
            jPanelMLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelMLayout.createSequentialGroup()
                .addGroup(jPanelMLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelMLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanelMLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanelOperaciones, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelMLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(b_guardar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                    .addGroup(jPanelMLayout.createSequentialGroup()
                        .addGroup(jPanelMLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(b_mas, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(b_menos, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cb_almacen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanelProveedor, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jScrollPane4)
            .addComponent(jPanelM, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanelProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 273, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanelM, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout jPanelMalmacenLayout = new javax.swing.GroupLayout(jPanelMalmacen);
        jPanelMalmacen.setLayout(jPanelMalmacenLayout);
        jPanelMalmacenLayout.setHorizontalGroup(
            jPanelMalmacenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanelMalmacenLayout.setVerticalGroup(
            jPanelMalmacenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        add(jPanelMalmacen, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void t_erKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_erKeyTyped
        evt.setKeyChar(Character.toUpperCase(evt.getKeyChar()));
        if(t_er.getText().length()>=255)
        evt.consume();
    }//GEN-LAST:event_t_erKeyTyped

    private void t_notasKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_notasKeyTyped
        evt.setKeyChar(Character.toUpperCase(evt.getKeyChar()));
        if(t_notas.getText().length()>=255)
        evt.consume();
    }//GEN-LAST:event_t_notasKeyTyped

    private void b_masActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_masActionPerformed
        h=new Herramientas(usr, menu);
        h.session(sessionPrograma);
        Session session = HibernateUtil.getSessionFactory().openSession();
        List part_act, renglones;
        
        try
        {
            session.beginTransaction().begin();
            switch(c_toperacion.getSelectedItem().toString())
            {
                case "Pedido":
                    pedido =(Pedido)session.get(Pedido.class, Integer.parseInt(t_pedido.getText()));
                    if(t_pedido.getText().compareTo("")!=0)
                    {
                        consultaPartidaPedido obj = new consultaPartidaPedido(new javax.swing.JFrame(), true, pedido, usr);
                        obj.t_pedido.setText(t_pedido.getText());
                        obj.busca(c_toperacion.getSelectedItem().toString(),l_tipo_pedido.getText(), c_tmovimiento.getSelectedItem().toString());
                        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
                        obj.setLocation((d.width/2)-(obj.getWidth()/2), (d.height/2)-(obj.getHeight()/2));
                        obj.setVisible(true);
                        renglones=obj.getReturnStatus();
                        if(renglones!=null)
                        {
                            for(int x=0; x<renglones.size(); x++)
                            {
                                part_act=(List)renglones.get(x);
                                int r=buscapartida(part_act);
                                if(r==-1)
                                {
                                    Object[] vector=new Object[part_act.size()];
                                    for(int c=0; c<part_act.size(); c++)
                                    {
                                        vector[c]=part_act.get(c);
                                    }
                                    model.addRow(vector);
                                    if(l_tipo_pedido.getText().compareTo("Interno")==0)
                                    {
                                        model.setColumnaEditable(3, true);
                                        model.setColumnaEditable(8, true);
                                    }
                                    if(l_tipo_pedido.getText().compareTo("Externo")==0)
                                    {
                                        model.setColumnaEditable(6, true);
                                    }
                                    if(l_tipo_pedido.getText().compareTo("Adicional")==0)
                                    {
                                        model.setColumnaEditable(8, true);
                                    }
                                }
                            }
                            sumaTotales();
                        }
                    }
                    else
                    {
                        JOptionPane.showMessageDialog(null, "¡Selecciona una partida!");
                        b_buscapedido.requestFocus();
                    }
                break;
                
                case "Compañía":
                    orden_act =(Orden)session.get(Orden.class, Integer.parseInt(t_orden.getText()));
                    if(t_orden.getText().compareTo("")!=0)
                    {
                        consultaPartidaOrden obj = new consultaPartidaOrden(new javax.swing.JFrame(), true,this.orden_act, usr);
                        obj.t_orden.setText(t_orden.getText());
                        obj.busca(c_toperacion.getSelectedItem().toString(),l_tipo_pedido.getText(), c_tmovimiento.getSelectedItem().toString(), "", "","");
                        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
                        obj.setLocation((d.width/2)-(obj.getWidth()/2), (d.height/2)-(obj.getHeight()/2));
                        obj.setVisible(true);
                        renglones=obj.getReturnStatus();
                        if(renglones!=null)
                        {
                            for(int x=0; x<renglones.size(); x++)
                            {
                                part_act=(List)renglones.get(x);
                                int r=buscapartida(part_act);
                                if(r==-1)
                                {
                                    Object[] vector=new Object[10];
                                    for(int c=0; c<part_act.size(); c++)
                                    {
                                        vector[c]=part_act.get(c);
                                    }
                                    model.addRow(vector);
                                    model.setColumnaEditable(8, true);
                                    sumaTotales();
                                }
                            }
                        }               
                    }
                    else
                    {
                        JOptionPane.showMessageDialog(null, "¡Selecciona una partida!");
                        b_buscapedido.requestFocus();
                    }
                break;
                
                case "Operarios":
                    orden_act =(Orden)session.get(Orden.class, Integer.parseInt(t_orden.getText()));
                    if(t_orden.getText().compareTo("")!=0)
                    {
                        consultaPartidaOrden obj = new consultaPartidaOrden(new javax.swing.JFrame(), true, this.orden_act, usr);
                        String q1="";
                        String q2="";
                        String q3="";
                        if(f1.isSelected())
                        {
                            q1+=" and part.espHoj=true";
                            q2+=" and partEx.pedido.partida.espHoj=true";
                            q3+=" and part.espHoj=true";
                        }
                        if(f2.isSelected())
                        {
                            q1+=" and part.espMec=true";
                            q2+=" and partEx.pedido.partida.espMec=true";
                            q3+=" and part.espMec=true";
                        }
                        if(f3.isSelected())
                        {
                            q1+=" and part.espSus=true";
                            q2+=" and partEx.pedido.partida.espSus=true";
                            q3+=" and part.espSus=true";
                        }
                        if(f4.isSelected())
                        {
                            q1+=" and part.espEle=true";
                            q2+=" and partEx.pedido.partida.espEle=true";
                            q3+=" and part.espEle=true";
                        }
                        obj.t_orden.setText(t_orden.getText());
                        obj.busca(c_toperacion.getSelectedItem().toString(),l_tipo_pedido.getText(), c_tmovimiento.getSelectedItem().toString(), q1, q2, q3);
                        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
                        obj.setLocation((d.width/2)-(obj.getWidth()/2), (d.height/2)-(obj.getHeight()/2));
                        obj.setVisible(true);
                        renglones=obj.getReturnStatus();
                        if(renglones!=null)
                        {
                            for(int x=0; x<renglones.size(); x++)
                            {
                                part_act=(List)renglones.get(x);
                                int r=buscapartida(part_act);
                                if(r==-1)
                                {
                                    Object[] vector=new Object[11];
                                    for(int c=0; c<part_act.size(); c++)
                                    {
                                        vector[c]=part_act.get(c);
                                    }
                                    model.addRow(vector);
                                    if(c_tmovimiento.getSelectedItem().toString().compareTo("Entrada")==0)
                                        model.setColumnaEditable(7, true);
                                    else
                                        model.setColumnaEditable(9, true);
                                    model.setColumnaEditable(10, true);
                                    sumaTotales();
                                }
                                else
                                    JOptionPane.showMessageDialog(null, "¡No se pueden agregar partidas duplicadas!");
                            }
                        }               
                    }
                    else
                    {
                        JOptionPane.showMessageDialog(null, "¡Selecciona una partida!");
                        b_buscapedido.requestFocus();
                    }
                break;
                
                case "Inventario":
                    if(t_orden.getText().compareTo("")!=0)
                    {
                         buscaEjemplarAlmacen obj= new buscaEjemplarAlmacen(null, true, this.sessionPrograma, this.usr, t_orden.getText(), id_trabajo, cb_especialidad.getSelectedItem().toString(), cb_almacen.getSelectedIndex());
                         Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
                         obj.setLocation((d.width/2)-(obj.getWidth()/2), (d.height/2)-(obj.getHeight()/2));
                         obj.setVisible(true);
                         Object[] vector = obj.getReturnStatus();
                         if(vector!=null)
                         {
                             String valor=(String)vector[0];
                             int r= buscaEjemplar(valor);

                             if(r==-1)
                             {
                                 model.addRow(vector);
                                 model.setColumnaEditable(8, true);
                             }
                         } 
                    }
                    else
                    {
                        if(cb_sin_orden.isSelected()==true)
                        {
                            buscaEjemplarAlmacen obj= new buscaEjemplarAlmacen(null, true, this.sessionPrograma, this.usr, "", "", cb_especialidad.getSelectedItem().toString(), cb_almacen.getSelectedIndex());
                            Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
                            obj.setLocation((d.width/2)-(obj.getWidth()/2), (d.height/2)-(obj.getHeight()/2));
                            obj.setVisible(true);
                            Object[] vector = obj.getReturnStatus();
                            if(vector!=null)
                            {
                                String valor=(String)vector[0];
                                int r= buscaEjemplar(valor);

                                if(r==-1)
                                {
                                    model.addRow(vector);
                                    model.setColumnaEditable(7, true);
                                }
                            } 
                        }
                    }
                break;
                    
                case "Venta":
                    pedido =(Pedido)session.get(Pedido.class, Integer.parseInt(t_pedido.getText()));
                    if(t_pedido.getText().compareTo("")!=0)
                    {
                        consultaPartidaPedido obj = new consultaPartidaPedido(new javax.swing.JFrame(), true,pedido, usr);
                        obj.t_pedido.setText(t_pedido.getText());
                        obj.busca(c_toperacion.getSelectedItem().toString(),l_tipo_pedido.getText(), c_tmovimiento.getSelectedItem().toString());
                        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
                        obj.setLocation((d.width/2)-(obj.getWidth()/2), (d.height/2)-(obj.getHeight()/2));
                        obj.setVisible(true);
                        renglones=obj.getReturnStatus();
                        if(renglones!=null)
                        {
                            for(int x=0; x<renglones.size(); x++)
                            {
                                part_act=(List)renglones.get(x);
                                int r=buscapartida(part_act);
                                if(r==-1)
                                {
                                    Object[] vector=new Object[10];
                                    for(int c=0; c<part_act.size(); c++)
                                    {
                                        vector[c]=part_act.get(c);
                                    }
                                    model.addRow(vector);
                                    if(c_tmovimiento.getSelectedItem().toString().compareTo("Entrada")==0)
                                        model.setColumnaEditable(5, true);
                                    else
                                        model.setColumnaEditable(7, true);
                                    sumaTotales();
                                }
                            }
                        }               
                    }
                    else
                    {
                        JOptionPane.showMessageDialog(null, "¡Selecciona una partida!");
                        b_buscapedido.requestFocus();
                    }
                break;
            }
        }catch(Exception e)
        {
            System.out.println();
        }
        if(session!=null)
            if(session.isOpen())
                session.close();
    }//GEN-LAST:event_b_masActionPerformed

    private void c_toperacionItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_c_toperacionItemStateChanged

    }//GEN-LAST:event_c_toperacionItemStateChanged

    private void b_buscapedidoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_buscapedidoActionPerformed
        h=new Herramientas(usr, menu);
        h.session(sessionPrograma);
        buscaPedido obj;
        if(c_toperacion.getSelectedItem().toString().compareTo("Venta")==0)
            obj = new buscaPedido(new javax.swing.JFrame(), true, 1, "Externo");
        else
            obj = new buscaPedido(new javax.swing.JFrame(), true, 1, "");
        obj.t_busca.requestFocus();
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        obj.setLocation((d.width/2)-(obj.getWidth()/2), (d.height/2)-(obj.getHeight()/2));
        obj.setVisible(true);
        Pedido ped=obj.getReturnStatus();
        if(ped!=null)
        {
            operacion(true, true, false, true, true, true, true, true, true, true, false);
            limpiar_tabla();
            Session session = HibernateUtil.getSessionFactory().openSession();
            try
            {
                //session.beginTransaction().begin();
                ped=(Pedido)session.get(Pedido.class, ped.getIdPedido());
                h.desbloqueaOrden();
                h.desbloqueaPedido();
                String edo=h.estadoPedido(ped);
                if(edo.compareTo("")==0 || edo.compareTo("*bloqueada ok*")==0)
                {
                    switch(ped.getTipoPedido())
                    {
                        case "Interno":
                            Partida partida = (Partida)session.createCriteria(Partida.class).add(Restrictions.eq("pedido.idPedido", ped.getIdPedido())).setMaxResults(1).uniqueResult();
                            if(partida!=null)
                            {
                                orden_act=(Orden)session.get(Orden.class, partida.getOrdenByIdOrden().getIdOrden());
                                borra_cajas();
                                l_tipo_pedido.setText(ped.getTipoPedido());
                                t_pedido.setText(""+ped.getIdPedido());
                                t_orden.setText(""+orden_act.getIdOrden());
                                t_tipo.setText(orden_act.getTipo().getTipoNombre());
                                t_marca.setText(""+orden_act.getMarca().getIdMarca());//marcanombre
                                t_modelo.setText(Integer.toString(orden_act.getModelo()));
                                t_serie.setText(orden_act.getNoSerie());
                                t_compania.setText(""+orden_act.getCompania().getIdCompania());//nombre
                                t_siniestro.setText(orden_act.getSiniestro());
                                t_asegurado.setText(""+orden_act.getClientes().getIdClientes());//nombre
                            }
                            break;
                        case "Externo":
                                OrdenExterna orden_Ex=(OrdenExterna)session.get(OrdenExterna.class, ped.getOrdenExterna().getIdOrden());
                                borra_cajas();
                                l_tipo_pedido.setText(ped.getTipoPedido());
                                t_pedido.setText(""+ped.getIdPedido());
                                //t_orden.setText(""+orden_Ex.getIdOrden());
                                if(orden_Ex.getTipo()!=null)
                                    t_tipo.setText(orden_Ex.getTipo().getTipoNombre());
                                if(orden_Ex.getMarca()!=null)
                                    t_marca.setText(""+orden_Ex.getMarca().getIdMarca());//marcanombre
                                if(orden_Ex.getModelo()!=null)
                                    t_modelo.setText(Integer.toString(orden_Ex.getModelo()));
                                if(orden_Ex.getNoSerie()!=null)
                                    t_serie.setText(orden_Ex.getNoSerie());
                                if(orden_Ex.getCompania()!=null)
                                    t_compania.setText(""+orden_Ex.getCompania().getIdCompania());//nombre
                                if(orden_Ex.getSiniestro()!=null)
                                    t_siniestro.setText(orden_Ex.getSiniestro());
                            break;
                        case "Inventario":
                                borra_cajas();
                                l_tipo_pedido.setText(ped.getTipoPedido());
                                t_pedido.setText(""+ped.getIdPedido());
                            break;
                        case "Adicional":
                                orden_act=(Orden)session.get(Orden.class, ped.getOrden().getIdOrden());
                                borra_cajas();
                                l_tipo_pedido.setText(ped.getTipoPedido());
                                t_pedido.setText(""+ped.getIdPedido());
                                t_orden.setText(""+orden_act.getIdOrden());
                                t_tipo.setText(orden_act.getTipo().getTipoNombre());
                                t_marca.setText(""+orden_act.getMarca().getIdMarca());//marcanombre
                                t_modelo.setText(Integer.toString(orden_act.getModelo()));
                                t_serie.setText(orden_act.getNoSerie());
                                t_compania.setText(""+orden_act.getCompania().getIdCompania());//nombre
                                t_siniestro.setText(orden_act.getSiniestro());
                                t_asegurado.setText(""+orden_act.getClientes().getIdClientes());//nombre
                            break;
                    }
                    if(t_pedido.getText().compareTo("")!=0){
                        if(c_toperacion.getSelectedIndex()==0 || c_toperacion.getSelectedIndex()==4)
                        {
                            if(c_tmovimiento.getSelectedItem().toString().compareTo("Salida")==0)
                            {
                                XCobrar xp = (XCobrar)session.createCriteria(XCobrar.class).createAlias("reclamo", "rec").add(Restrictions.eq("idPedido", Integer.parseInt(t_pedido.getText()))).add(Restrictions.in("rec.estatus", new String[]{"a","P"})).setMaxResults(1).uniqueResult();
                                if(xp==null)
                                {
                                    t_pedido.setText(""+ped.getIdPedido());
                                    b_mas.setEnabled(true);
                                    b_menos.setEnabled(true);
                                    busca();
                                }
                                else
                                {
                                    JOptionPane.showMessageDialog(null, "El pedido "+t_pedido.getText()+" ya esta en proceso de cobro N° Reclamo:"+xp.getReclamo().getIdReclamo());
                                    borra_cajas();

                                }
                            }
                            else
                            {
                                t_pedido.setText(""+ped.getIdPedido());
                                b_mas.setEnabled(true);
                                b_menos.setEnabled(true);
                                busca();
                            }    
                        }
                        else
                        {
                            t_pedido.setText(""+ped.getIdPedido());
                            b_mas.setEnabled(true);
                            b_menos.setEnabled(true);
                            busca();
                        }
                    }
                }
                else
                {
                    JOptionPane.showMessageDialog(null, "¡Pedido bloqueado por:"+ped.getUsuarioByBloqueado().getIdUsuario());
                }
            }catch(Exception e)
            {
                e.printStackTrace();
            }
            finally
            {
                if(session.isOpen())
                    session.close();
            }
        }
        else
        {
            h= new Herramientas(usr, menu);
            h.desbloqueaOrden();
            limpiar_tabla();
            borra_cajas();
            sumaTotales();
            b_buscapedido.requestFocus();
        }
    }//GEN-LAST:event_b_buscapedidoActionPerformed

    private void c_tmovimientoItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_c_tmovimientoItemStateChanged
        
    }//GEN-LAST:event_c_tmovimientoItemStateChanged

    private void b_buscaordenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_buscaordenActionPerformed
        h=new Herramientas(usr, menu);
        h.session(sessionPrograma);
        buscaOrden obj = new buscaOrden(new javax.swing.JFrame(), true, usr, 0, configuracion);
        obj.t_busca.requestFocus();
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        obj.setLocation((d.width/2)-(obj.getWidth()/2), (d.height/2)-(obj.getHeight()/2));
        obj.setVisible(true);
        t_er.setText("");
        cb_especialidad.setSelectedIndex(0);
        cb_acondicionamiento.setSelected(false);
        orden_act=obj.getReturnStatus();
        try
        {
            if(orden_act!=null)
            {
                limpiar_tabla();
                /*if(orden_act.getInicioRefacciones()!=null)
                {*/
                    if(orden_act.getFechaCierre()==null)
                    {
                        h= new Herramientas(usr, menu);
                        h.desbloqueaOrden();
                        t_orden.setText(""+orden_act.getIdOrden());
                        if(t_orden.getText().compareTo("")!=0)
                        {
                            borra_cajas();
                            orden_act=buscarOrden(orden_act.getIdOrden());
                            t_orden.setText(""+orden_act.getIdOrden());
                            t_tipo.setText(orden_act.getTipo().getTipoNombre());
                            t_marca.setText(""+orden_act.getMarca().getIdMarca());//marcanombre
                            t_modelo.setText(Integer.toString(orden_act.getModelo()));
                            t_serie.setText(orden_act.getNoSerie());
                            t_compania.setText(""+orden_act.getCompania().getIdCompania());//nombre
                            t_siniestro.setText(orden_act.getSiniestro());
                            t_asegurado.setText(""+orden_act.getClientes().getIdClientes());//nombre
                            b_mas.setEnabled(true);
                            b_menos.setEnabled(true);
                            busca();
                        }                            
                        else
                        {
                            borra_cajas();
                            b_mas.setEnabled(false);
                            b_menos.setEnabled(false);
                            orden_act=null;
                        }
                    }
                    else
                        JOptionPane.showMessageDialog(null, "¡Orden cerrada!");
                /*}
                else
                    JOptionPane.showMessageDialog(null, "¡Aún no esta disponible!");*/
            }
            else
            {
                h= new Herramientas(usr, menu);
                h.desbloqueaOrden();
                limpiar_tabla();
                borra_cajas();
                sumaTotales();
                b_buscaorden.requestFocus();
            }
        }catch(Exception e){System.out.println(e);}
    }//GEN-LAST:event_b_buscaordenActionPerformed

    private void b_menosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_menosActionPerformed
        h=new Herramientas(usr, menu);
        h.session(sessionPrograma);
        if(t_datos.getSelectedRow()>=0)
        {   
            DefaultTableModel model = (DefaultTableModel) t_datos.getModel();
            int [] renglones = t_datos.getSelectedRows();
            int opt=JOptionPane.showConfirmDialog(this, "¡Las partidas se eliminará!");
            if (JOptionPane.YES_OPTION == opt)
            {
                for(int x=0; x<renglones.length; x++)
                    model.removeRow(t_datos.getSelectedRow());
                sumaTotales();
                JOptionPane.showMessageDialog(null, "¡Partida eliminada!");
            }
        }
        else
            JOptionPane.showMessageDialog(null, "¡Selecciona la partida que desees eliminar!");
    }//GEN-LAST:event_b_menosActionPerformed

    private void t_folioKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_folioKeyTyped
        evt.setKeyChar(Character.toUpperCase(evt.getKeyChar()));
        if(t_folio.getText().length()>=15)
        evt.consume();
    }//GEN-LAST:event_t_folioKeyTyped

    private void b_guardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_guardarActionPerformed
        h=new Herramientas(usr, menu);
        h.session(sessionPrograma);
        Session session = HibernateUtil.getSessionFactory().openSession();
        try
        {
            Almacen almacen = new Almacen();
            almacen.setUsuario(usr);
            Date fecha_almacen = new Date();
            DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            String valor=dateFormat.format(fecha_almacen);
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
            almacen.setFecha(calendario.getTime());
            almacen.setEntrego(t_er.getText());
            almacen.setDocumento(t_folio.getText());
            almacen.setNotas(t_notas.getText());
            almacen.setAlmacen(""+cb_almacen.getSelectedIndex());
            if (c_tmovimiento.getSelectedItem().toString().compareTo("Entrada")==0)
                almacen.setTipoMovimiento(1);
            else
                almacen.setTipoMovimiento(2);
            switch (c_toperacion.getSelectedIndex()) 
            {
                case 0://ped
                    if(l_tipo_pedido.getText().compareTo("Interno")==0)
                    {
                        if(t_er.getText().compareTo("")!=0)
                        {
                            if(t_datos.getRowCount()>0)
                            {
                                if(consultaLista(8)==true)
                                {
                                    if(almacen.getTipoMovimiento()==1)
                                    {
                                        if(t_folio.getText().compareTo("")!=0)
                                        {
                                            //consultar si estan todas autorizadas //para antes de entrada
                                            /*if(autorizaOperarios1()==true)
                                            {*/
                                                almacen.setOperacion(1); 
                                                if(r1.isSelected())
                                                    almacen.setTipoDocumento("F");
                                                else
                                                    almacen.setTipoDocumento("R");
                                                Integer respuesta=guardarAlmacen(almacen);
                                                if(respuesta!=null)
                                                {
                                                    //JOptionPane.showMessageDialog(null, "Registro almacenado con la clave:  " +respuesta);
                                                    int seleccion =JOptionPane.showOptionDialog(null, "Clave:  " +respuesta+"Numero de copias:","Registro almacenado",JOptionPane.YES_NO_CANCEL_OPTION,JOptionPane.QUESTION_MESSAGE,null,new Object[] { "1", "2" },   "1");
                                                    formatosPedido fa=new formatosPedido(this.usr, this.sessionPrograma, almacen,configuracion);
                                                    if (seleccion != -1)
                                                    {
                                                        if(seleccion==0)
                                                            fa.formato(false);
                                                        else
                                                            fa.formato(true);
                                                    }
                                                    else
                                                        fa.formato(false);
                                                    titulos();
                                                }
                                                else
                                                    b_guardar.requestFocus();
                                            //}
                                        }
                                        else
                                            JOptionPane.showMessageDialog(null, "Falta el numero de factura o remisión");
                                    }
                                    else
                                    {
                                        almacen.setOperacion(1); 
                                        if(r1.isSelected())
                                            almacen.setTipoDocumento("F");
                                        else
                                            almacen.setTipoDocumento("R");
                                        Integer respuesta=guardarAlmacen(almacen);
                                        if(respuesta!=null)
                                        {
                                            JOptionPane.showMessageDialog(null, "Registro almacenado con la clave:  " +respuesta);
                                            formatosPedido fa=new formatosPedido(this.usr, this.sessionPrograma, almacen,configuracion);
                                            fa.formato(false);//chava
                                            titulos();
                                        }
                                        else
                                            b_guardar.requestFocus();
                                    }
                                }
                                else
                                JOptionPane.showMessageDialog(null, "No se puede almacenar la cantidad ya que una partida contiene 0.00");
                            }
                            else
                            JOptionPane.showMessageDialog(null, "Es necesario seleccionar alguna partida");
                        }
                        else
                        {
                            JOptionPane.showMessageDialog(null, "Ingresa el nombre de quien "+buscar.getText());
                            t_er.requestFocus();
                        }
                    }
                    if(l_tipo_pedido.getText().compareTo("Externo")==0)
                    {
                        if(t_er.getText().compareTo("")!=0)
                        {
                            if(t_datos.getRowCount()>0)
                            {
                                if(consultaLista(6)==true)
                                {
                                    if(almacen.getTipoMovimiento()==1)
                                    {
                                        if(t_folio.getText().compareTo("")!=0)
                                        {
                                            almacen.setOperacion(2);
                                            if(r1.isSelected())
                                                almacen.setTipoDocumento("F");
                                            else
                                                almacen.setTipoDocumento("R");
                                            Integer respuesta=guardarAlmacen(almacen);
                                            if(respuesta!=null)
                                            {
                                                //JOptionPane.showMessageDialog(null, "Registro almacenado con la clave:  " +respuesta);
                                                int seleccion =JOptionPane.showOptionDialog(null, "Clave:  " +respuesta+"Numero de copias:","Registro almacenado",JOptionPane.YES_NO_CANCEL_OPTION,JOptionPane.QUESTION_MESSAGE,null,new Object[] { "1", "2" },   "1");
                                                    formatosPedido fa=new formatosPedido(this.usr, this.sessionPrograma, almacen,configuracion);
                                                    if (seleccion != -1)
                                                    {
                                                        if(seleccion==0)
                                                            fa.formato(false);
                                                        else
                                                            fa.formato(true);
                                                    }
                                                    else
                                                        fa.formato(false);
                                                titulos();
                                            }
                                            else
                                                b_guardar.requestFocus();
                                        }
                                        else
                                            JOptionPane.showMessageDialog(null, "Falta el numero de factura o remisión");
                                    }
                                    else
                                    {
                                        almacen.setOperacion(2);
                                        if(r1.isSelected())
                                            almacen.setTipoDocumento("F");
                                        else
                                            almacen.setTipoDocumento("R");
                                        Integer respuesta=guardarAlmacen(almacen);
                                        if(respuesta!=null)
                                        {
                                            int seleccion =JOptionPane.showOptionDialog(null, "Clave:  " +respuesta+"Numero de copias:","Registro almacenado",JOptionPane.YES_NO_CANCEL_OPTION,JOptionPane.QUESTION_MESSAGE,null,new Object[] { "1", "2" },   "1");
                                            formatosPedido fa=new formatosPedido(this.usr, this.sessionPrograma, almacen,configuracion);
                                            if (seleccion != -1)
                                            {
                                                if(seleccion==0)
                                                    fa.formato(false);
                                                else
                                                    fa.formato(true);
                                            }
                                            else
                                                fa.formato(false);
                                            titulos();
                                        }
                                        else
                                            b_guardar.requestFocus();
                                    }
                                }
                                else
                                JOptionPane.showMessageDialog(null, "No se puede almacenar la cantidad ya que una partida contiene 0.00");
                            }
                            else
                            JOptionPane.showMessageDialog(null, "Es necesario seleccionar alguna partida");
                        }
                        else
                        {
                            JOptionPane.showMessageDialog(null, "Ingresa el nombre de quien "+buscar.getText());
                            t_er.requestFocus();
                        }
                    }
                    if(l_tipo_pedido.getText().compareTo("Inventario")==0)
                    {
                        if(t_er.getText().compareTo("")!=0)
                        {
                            if(t_datos.getRowCount()>0)
                            {
                                if(consultaLista(6)==true)
                                {
                                    if(almacen.getTipoMovimiento()==1)
                                    {
                                        if(t_folio.getText().compareTo("")!=0)
                                        {
                                            almacen.setOperacion(7);
                                            if(r1.isSelected())
                                                almacen.setTipoDocumento("F");
                                            else
                                                almacen.setTipoDocumento("R");//se elimina
                                            boolean pasa=true;
                                            for(int r=0; r<t_datos.getRowCount(); r++)
                                            {
                                                Ejemplar art=(Ejemplar)session.get(Ejemplar.class, t_datos.getValueAt(r, 1).toString());
                                                if(cb_almacen.getSelectedItem().toString().compareToIgnoreCase("ALMACEN 1")==0)
                                                {
                                                    if(art.getExistencias()==null)
                                                    {
                                                        t_datos.setValueAt("-"+t_datos.getValueAt(r, 0).toString(), r, 0);
                                                        pasa=false;
                                                    }
                                                }
                                                else
                                                {
                                                    if(art.getExistencias2()==null)
                                                    {
                                                        t_datos.setValueAt("-"+t_datos.getValueAt(r, 0).toString(), r, 0);
                                                        pasa=false;
                                                    }
                                                }
                                            }
                                            formatoTabla();
                                            if(pasa==true)
                                            {
                                                Integer respuesta=guardarAlmacen(almacen);
                                                if(respuesta!=null)
                                                {
                                                    formatosOrden fa=new formatosOrden(this.usr, this.sessionPrograma, almacen,configuracion);
                                                    fa.formato(false);
                                                    titulos();
                                                }
                                                else
                                                    b_guardar.requestFocus();
                                            }
                                            else
                                            {
                                                JOptionPane.showMessageDialog(null, "Hay Articulos que no pertenecen a este almacen");
                                            }
                                        }
                                        else
                                            JOptionPane.showMessageDialog(null, "Falta el numero de factura o remisión");
                                    }
                                    else
                                    {
                                        almacen.setOperacion(7);
                                        if(r1.isSelected())
                                            almacen.setTipoDocumento("F");
                                        else
                                            almacen.setTipoDocumento("R");
                                        boolean pasa=true;
                                        for(int r=0; r<t_datos.getRowCount(); r++)
                                        {
                                            Ejemplar art=(Ejemplar)session.get(Ejemplar.class, t_datos.getValueAt(r, 1).toString());
                                            if(cb_almacen.getSelectedItem().toString().compareToIgnoreCase("ALMACEN 1")==0)
                                            {
                                                if(art.getExistencias()==null)
                                                {
                                                    t_datos.setValueAt("-"+t_datos.getValueAt(r, 0).toString(), r, 0);
                                                    pasa=false;
                                                }
                                            }
                                            else
                                            {
                                                if(art.getExistencias2()==null)
                                                {
                                                    t_datos.setValueAt("-"+t_datos.getValueAt(r, 0).toString(), r, 0);
                                                    pasa=false;
                                                }
                                            }
                                        }
                                        formatoTabla();
                                        if(pasa==true)
                                        {
                                            Integer respuesta=guardarAlmacen(almacen);
                                            if(respuesta!=null)
                                            {
                                                JOptionPane.showMessageDialog(null, "Registro almacenado con la clave:  " +respuesta);
                                                formatosOrden fa=new formatosOrden(this.usr, this.sessionPrograma, almacen,configuracion);
                                                fa.formato(false);
                                                titulos();
                                            }
                                            else
                                                b_guardar.requestFocus();
                                        }
                                            
                                    }
                                }
                                else
                                JOptionPane.showMessageDialog(null, "No se puede almacenar la cantidad ya que una partida contiene 0.00");
                            }
                            else
                            JOptionPane.showMessageDialog(null, "Es necesario seleccionar alguna partida");
                        }
                        else
                        {
                            JOptionPane.showMessageDialog(null, "Ingresa el nombre de quien "+buscar.getText());
                            t_er.requestFocus();
                        }
                    }
                    if(l_tipo_pedido.getText().compareTo("Adicional")==0)
                    {
                        if(t_er.getText().compareTo("")!=0)
                        {
                            if(t_datos.getRowCount()>0)
                            {
                                if(consultaLista(8)==true)
                                {
                                    if(almacen.getTipoMovimiento()==1)
                                    {
                                        if(t_folio.getText().compareTo("")!=0)
                                        {
                                            //consultar si estan todas autorizadas //para antes de entrada
                                            /*if(autorizaOperarios1()==true)
                                            {*/
                                                almacen.setOperacion(3);
                                                if(r1.isSelected())
                                                    almacen.setTipoDocumento("F");
                                                else
                                                    almacen.setTipoDocumento("R");
                                                Integer respuesta=guardarAlmacen(almacen);
                                                if(respuesta!=null)
                                                {
                                                    int seleccion =JOptionPane.showOptionDialog(null, "Clave:  " +respuesta+"Numero de copias:","Registro almacenado",JOptionPane.YES_NO_CANCEL_OPTION,JOptionPane.QUESTION_MESSAGE,null,new Object[] { "1", "2" },   "1");
                                                    formatosPedido fa=new formatosPedido(this.usr, this.sessionPrograma, almacen,configuracion);
                                                    if (seleccion != -1)
                                                    {
                                                        if(seleccion==0)
                                                            fa.formato(false);
                                                        else
                                                            fa.formato(true);
                                                    }
                                                    else
                                                        fa.formato(false);
                                                    titulos();
                                                }
                                                else
                                                    b_guardar.requestFocus();
                                            //}
                                        }
                                        else
                                            JOptionPane.showMessageDialog(null, "Falta el numero de factura o remisión");
                                    }
                                    else
                                    {
                                        almacen.setOperacion(3);
                                        if(r1.isSelected())
                                            almacen.setTipoDocumento("F");
                                        else
                                            almacen.setTipoDocumento("R");
                                        Integer respuesta=guardarAlmacen(almacen);
                                        if(respuesta!=null)
                                        {
                                            int seleccion =JOptionPane.showOptionDialog(null, "Clave:  " +respuesta+"Numero de copias:","Registro almacenado",JOptionPane.YES_NO_CANCEL_OPTION,JOptionPane.QUESTION_MESSAGE,null,new Object[] { "1", "2" },   "1");
                                            formatosPedido fa=new formatosPedido(this.usr, this.sessionPrograma, almacen,configuracion);
                                            if (seleccion != -1)
                                            {
                                                if(seleccion==0)
                                                    fa.formato(false);
                                                else
                                                    fa.formato(true);
                                            }
                                            else
                                                fa.formato(false);
                                            titulos();
                                        }
                                        else
                                            b_guardar.requestFocus();
                                    }
                                }
                                else
                                JOptionPane.showMessageDialog(null, "No se puede almacenar la cantidad ya que una partida contiene 0.00");
                            }
                            else
                            JOptionPane.showMessageDialog(null, "Es necesario seleccionar alguna partida");
                        }
                        else
                        {
                            JOptionPane.showMessageDialog(null, "Ingresa el nombre de quien "+buscar.getText());
                            t_er.requestFocus();
                        }
                    }
                    break;            
                case 1://compania
                    if(t_er.getText().compareTo("")!=0)
                    {
                        if(t_datos.getRowCount()>0)
                        {
                            if(consultaLista(8)==true)
                            {
                                if(t_folio.getText().compareTo("")!=0)
                                {
                                    almacen.setOperacion(4);
                                    if(r1.isSelected())
                                        almacen.setTipoDocumento("F");
                                    else
                                        almacen.setTipoDocumento("R");
                                    Integer respuesta=guardarAlmacenOrden(almacen);
                                    if(respuesta!=null)
                                    {
                                        JOptionPane.showMessageDialog(null, "Registro almacenado con la clave:  " +respuesta);
                                        formatosOrden fa=new formatosOrden(this.usr, this.sessionPrograma, almacen,configuracion);
                                        fa.formato(false);
                                        titulos();
                                    }
                                    else
                                        b_guardar.requestFocus();
                                }
                                else
                                    JOptionPane.showMessageDialog(null, "Falta el numero de factura o remisión");
                            }
                            else
                                JOptionPane.showMessageDialog(null, "No se puede almacenar la cantidad ya que una partida contiene 0.00");
                        }
                        else
                            JOptionPane.showMessageDialog(null, "Es necesario seleccionar alguna partida");
                    }
                    else
                    {
                        JOptionPane.showMessageDialog(null, "Ingresa el nombre de quien "+buscar.getText());
                        t_er.requestFocus();
                    }
                    break;                
                case 2://operario
                    if(c_tmovimiento.getSelectedItem().toString().compareTo("Entrada")==0)
                    {
                        if(t_er.getText().compareTo("")!=0)
                        {
                            if(t_datos.getRowCount()>0)
                            {
                                if(consultaLista(7)==true)
                                {
                                    almacen.setOperacion(5);                                                        
                                    Integer respuesta=guardarAlmacenOrden(almacen);
                                    if(respuesta!=null)
                                    {
                                        JOptionPane.showMessageDialog(null, "Registro almacenado con la clave:  " +respuesta);
                                        formatosOrden fa=new formatosOrden(this.usr, this.sessionPrograma, almacen,configuracion);
                                        fa.formato(false);
                                        titulos();
                                    }
                                    else
                                        b_guardar.requestFocus();
                                }
                                else
                                    JOptionPane.showMessageDialog(null, "No se puede almacenar la cantidad ya que una partida contiene 0.00");
                            }
                            else
                                JOptionPane.showMessageDialog(null, "Es necesario seleccionar alguna partida");
                        }
                        else
                        {
                            JOptionPane.showMessageDialog(null, "Ingresa el nombre de quien "+buscar.getText());
                            t_er.requestFocus();
                        }
                    }
                    if(c_tmovimiento.getSelectedItem().toString().compareTo("Salida")==0)
                    {
                        if(autorizaOperarios()==true)
                        {//para pedido op
                            if(t_er.getText().compareTo("")!=0)
                            {
                                if(t_datos.getRowCount()>0)
                                {
                                    if(consultaLista(9)==true)
                                    {
                                        //almacen.setAutorizo(usr);
                                        almacen.setOperacion(5);
                                        Integer respuesta=guardarAlmacenOrden(almacen);
                                        if(respuesta!=null)
                                        {
                                            JOptionPane.showMessageDialog(null, "Registro almacenado con la clave:  " +respuesta);
                                            formatosOrden fa=new formatosOrden(this.usr, this.sessionPrograma, almacen,configuracion);
                                            fa.formato(false);
                                            titulos();
                                        }
                                        else
                                            b_guardar.requestFocus();
                                    }
                                    else
                                        JOptionPane.showMessageDialog(null, "No se puede almacenar la cantidad ya que una partida contiene 0.00");
                                }
                                else
                                    JOptionPane.showMessageDialog(null, "Es necesario seleccionar alguna partida");
                            }
                            else
                            {
                                JOptionPane.showMessageDialog(null, "Ingresa el nombre de quien "+buscar.getText());
                                t_er.requestFocus();
                            }
                        }
                    }
                    break;

                case 3://venta
                    if(t_er.getText().compareTo("")!=0)
                    {
                        if(t_datos.getRowCount()>0)
                        {
                            int columna=5;
                            if(c_tmovimiento.getSelectedItem().toString().compareTo("Salida")==0)
                                columna=7;
                            if(consultaLista(columna)==true)
                            {
                                almacen.setOperacion(6);
                                Integer respuesta=guardarAlmacen(almacen);
                                if(respuesta!=null)
                                {
                                    JOptionPane.showMessageDialog(null, "Registro almacenado con la clave:  " +respuesta);
                                    formatosPedido fa=new formatosPedido(this.usr, this.sessionPrograma, almacen,configuracion);
                                    fa.formato(false);//chava
                                    titulos();
                                }
                                else
                                b_guardar.requestFocus();
                            }
                            else
                            JOptionPane.showMessageDialog(null, "No se puede almacenar la cantidad ya que una partida contiene 0.00");
                        }
                        else
                        JOptionPane.showMessageDialog(null, "Es necesario seleccionar alguna partida");
                    }
                    else
                    {
                        JOptionPane.showMessageDialog(null, "Ingresa el nombre de quien "+buscar.getText());
                        t_er.requestFocus();
                    }
                    break;
                case 4://Inventario
                    if(c_tmovimiento.getSelectedItem().toString().compareTo("Entrada")==0)
                    {
                        if(t_er.getText().compareTo("")!=0)
                        {
                            if(t_datos.getRowCount()>0)
                            {
                                boolean cero=false;
                                if(cb_sin_orden.isSelected()==true)
                                   cero=consultaLista(7);
                                else
                                    cero=consultaLista(8);
                                if(cero==true)
                                {
                                    if(orden_act!=null)
                                    {
                                        almacen.setOperacion(8); 
                                        almacen.setOrden(orden_act);

                                        Empleado em1=new Empleado();
                                        em1.setIdEmpleado(Integer.parseInt(id_empleado));
                                        almacen.setEmpleado(em1);

                                        if(t_orden.getText().compareTo("")!=0)
                                        {
                                            switch(cb_especialidad.getSelectedItem().toString()){
                                                case "HOJALATERIA":
                                                    almacen.setEspecialidad("H");
                                                    break;
                                                case "MECANICA":
                                                    almacen.setEspecialidad("M");
                                                    break;
                                                case "SUSPENSION":
                                                    almacen.setEspecialidad("S");
                                                    break;
                                                case "ELECTRICO":
                                                    almacen.setEspecialidad("E");
                                                    break;
                                                case "PINTURA":
                                                    almacen.setEspecialidad("P");
                                                    break;
                                                case "ADICIONAL":
                                                    almacen.setEspecialidad("A");
                                                    TrabajoExtra trabajo=(TrabajoExtra)session.get(TrabajoExtra.class, Integer.parseInt(id_trabajo));
                                                    almacen.setTrabajoExtra(trabajo);
                                                    break;
                                            }
                                        }
                                        Empleado em=new Empleado();
                                        em.setIdEmpleado(Integer.parseInt(id_empleado));
                                        almacen.setEmpleado(null);
                                        Integer respuesta=guardarAlmacenOrden(almacen);
                                        if(respuesta!=null)
                                        {
                                            JOptionPane.showMessageDialog(null, "Registro almacenado con la clave:  " +respuesta);
                                            formatosOrden fa=new formatosOrden(this.usr, this.sessionPrograma, almacen,configuracion);
                                            fa.formato1();
                                            titulos();
                                        }
                                        else
                                            b_guardar.requestFocus();
                                    }
                                    else
                                    {
                                        almacen.setOperacion(8);
                                        almacen.setEspecialidad("");
                                        Empleado em1=new Empleado();
                                        em1.setIdEmpleado(Integer.parseInt(id_empleado));
                                        almacen.setEmpleado(em1);
                                        Integer respuesta=guardarAlmacenOrden(almacen);
                                        if(respuesta!=null)
                                        {
                                            JOptionPane.showMessageDialog(null, "Registro almacenado con la clave:  " +respuesta);
                                            formatosOrden fa=new formatosOrden(this.usr, this.sessionPrograma, almacen,configuracion);
                                            fa.formato1();
                                            titulos();
                                        }
                                        else
                                            b_guardar.requestFocus();
                                    }
                                }
                                else
                                    JOptionPane.showMessageDialog(null, "No se puede almacenar la cantidad ya que una partida contiene 0.00");
                            }
                            else
                                JOptionPane.showMessageDialog(null, "Es necesario seleccionar alguna partida");
                        }
                        else
                        {
                            JOptionPane.showMessageDialog(null, "Ingresa el nombre de quien "+buscar.getText());
                            t_er.requestFocus();
                        }
                    }
                    if(c_tmovimiento.getSelectedItem().toString().compareTo("Salida")==0)
                    {
                        if(t_er.getText().compareTo("")!=0)
                        {
                            if(t_datos.getRowCount()>0)
                            {
                                boolean cero=false;
                                if(cb_sin_orden.isSelected()==true)
                                   cero=consultaLista(7);
                                else
                                    cero=consultaLista(8);
                                if(cero==true)
                                {
                                    session.beginTransaction();
                                    if(orden_act!=null)
                                    {
                                        orden_act= (Orden)session.get(Orden.class, orden_act.getIdOrden());
                                        String especialidad="";

                                        switch(this.cb_especialidad.getSelectedItem().toString())
                                        {
                                            case "HOJALATERIA":
                                                especialidad="H";
                                                break;

                                            case "MECANICA":
                                                especialidad="M";
                                                break;

                                            case "SUSPENSION":
                                                especialidad="S";
                                                break;

                                            case "ELECTRICO":
                                                especialidad="E";
                                                break;

                                            case "PINTURA":
                                                especialidad="P";
                                                break;

                                            case "ADICIONAL":
                                                especialidad="A";
                                                TrabajoExtra trabajo=(TrabajoExtra)session.get(TrabajoExtra.class, Integer.parseInt(id_trabajo));
                                                almacen.setTrabajoExtra(trabajo);
                                                break;
                                        }

                                        Empleado em1=new Empleado();
                                        em1.setIdEmpleado(Integer.parseInt(id_empleado));
                                        almacen.setOperacion(8);
                                        almacen.setEspecialidad(especialidad);
                                        almacen.setEmpleado(em1);
                                        Integer respuesta=guardarAlmacenOrden(almacen);
                                        if(respuesta!=null)
                                        {
                                            JOptionPane.showMessageDialog(null, "Registro almacenado con la clave:  " +respuesta);
                                            formatosOrden fa=new formatosOrden(this.usr, this.sessionPrograma, almacen,configuracion);
                                            fa.formato1();
                                            titulos();
                                        }
                                        else
                                            b_guardar.requestFocus();
                                    }
                                    else
                                    {
                                        almacen.setOperacion(8);
                                        almacen.setEspecialidad("");
                                        Empleado em1=new Empleado();
                                        em1.setIdEmpleado(Integer.parseInt(id_empleado));
                                        almacen.setEmpleado(em1);
                                        Integer respuesta=guardarAlmacenOrden(almacen);
                                        if(respuesta!=null)
                                        {
                                            JOptionPane.showMessageDialog(null, "Registro almacenado con la clave:  " +respuesta);
                                            formatosOrden fa=new formatosOrden(this.usr, this.sessionPrograma, almacen,configuracion);
                                            fa.formato1();
                                            titulos();
                                        }
                                        else
                                            b_guardar.requestFocus();
                                    }
                                    session.getTransaction().rollback();
                                    session.disconnect();
                                }
                                else
                                    JOptionPane.showMessageDialog(null, "No se puede almacenar la cantidad ya que una partida contiene 0.00");
                            }
                            else
                                JOptionPane.showMessageDialog(null, "Es necesario seleccionar alguna partida");
                        }
                        else
                        {
                            JOptionPane.showMessageDialog(null, "Ingresa el nombre de quien "+buscar.getText());
                            t_er.requestFocus();
                        }
                    }
                    break;
            }
        }catch(Exception e)
        {
            e.printStackTrace();
        }
        if(session!=null)
            if(session.isOpen())
                session.close();
    }//GEN-LAST:event_b_guardarActionPerformed

    private void t_erActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_t_erActionPerformed
        b_guardar.requestFocus();
    }//GEN-LAST:event_t_erActionPerformed

    private void t_contraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_t_contraActionPerformed
        b_autorizar.requestFocus();
    }//GEN-LAST:event_t_contraActionPerformed

    private void t_userActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_t_userActionPerformed
        t_contra.requestFocus();
    }//GEN-LAST:event_t_userActionPerformed

    private void b_autorizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_autorizarActionPerformed
        if(t_user.getText().compareTo("")!=0)
        {
            if(t_contra.getPassword().toString().compareTo("")!=0)
            {
                Session session = HibernateUtil.getSessionFactory().openSession();
                try
                {
                    session.beginTransaction().begin();
                    Usuario autorizar = (Usuario)session.createCriteria(Usuario.class).add(Restrictions.eq("idUsuario", t_user.getText())).add(Restrictions.eq("clave", t_contra.getText())).setMaxResults(1).uniqueResult();
                    if(autorizar!=null)
                    {
                        if(autorizar.getEditaTaller()==true)
                        {
                            usrAut=autorizar;
                            session.beginTransaction().rollback();
                            session.close();
                            autoriza.dispose();
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
                if(session!=null)
                    if(session.isOpen()==true)
                    session.close();
            }
            else
            {
                JOptionPane.showMessageDialog(this, "¡Ingrese la contraseña!");
                t_contra.requestFocus();
            }
        }
        else
        {
            JOptionPane.showMessageDialog(this, "¡Ingrese el usuario!");
            t_user.requestFocus();
        }
    }//GEN-LAST:event_b_autorizarActionPerformed

    private void b_autorizarFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_b_autorizarFocusLost
        sumaTotales();
    }//GEN-LAST:event_b_autorizarFocusLost

    private void t_userKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_userKeyTyped
        // TODO add your handling code here:
        evt.setKeyChar(Character.toUpperCase(evt.getKeyChar()));
        if(t_user.getText().length()>=10)
        evt.consume();
    }//GEN-LAST:event_t_userKeyTyped

    private void f1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_f1ActionPerformed
        // TODO add your handling code here:
        busca();
    }//GEN-LAST:event_f1ActionPerformed

    private void f2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_f2ActionPerformed
        // TODO add your handling code here:
        busca();
    }//GEN-LAST:event_f2ActionPerformed

    private void f3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_f3ActionPerformed
        // TODO add your handling code here:
        busca();
    }//GEN-LAST:event_f3ActionPerformed

    private void f4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_f4ActionPerformed
        // TODO add your handling code here:
        busca();
    }//GEN-LAST:event_f4ActionPerformed

    private void f5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_f5ActionPerformed
        // TODO add your handling code here:
        busca();
    }//GEN-LAST:event_f5ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        c_tmovimiento.setSelectedItem("Entrada");
        c_toperacion.setSelectedItem("Pedido");
        titulos();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        c_tmovimiento.setSelectedItem("Salida");
        c_toperacion.setSelectedItem("Pedido");
        titulos();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        c_tmovimiento.setSelectedItem("Entrada");
        c_toperacion.setSelectedItem("Compañía");
        titulos();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        c_tmovimiento.setSelectedItem("Salida");
        c_toperacion.setSelectedItem("Compañía");
        titulos();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:
        c_tmovimiento.setSelectedItem("Salida");
        c_toperacion.setSelectedItem("Operarios");
        titulos();
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        // TODO add your handling code here:
        c_tmovimiento.setSelectedItem("Entrada");
        c_toperacion.setSelectedItem("Operarios");
        titulos();
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        // TODO add your handling code here:
        c_tmovimiento.setSelectedItem("Salida");
        c_toperacion.setSelectedItem("Venta");
        titulos();
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        // TODO add your handling code here:
        c_tmovimiento.setSelectedItem("Entrada");
        c_toperacion.setSelectedItem("Venta");
        titulos();
    }//GEN-LAST:event_jButton8ActionPerformed

    private void b_detallesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_detallesActionPerformed
        // TODO add your handling code here:
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        datos.setSize(400, 200);
        datos.setLocation((d.width/2)-(datos.getWidth()/2), (d.height/2)-(datos.getHeight()/2));
        datos.setVisible(true);
    }//GEN-LAST:event_b_detallesActionPerformed

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        // TODO add your handling code here:
        c_tmovimiento.setSelectedItem("Entrada");
        c_toperacion.setSelectedItem("Inventario");
        titulos();
    }//GEN-LAST:event_jButton10ActionPerformed

    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton11ActionPerformed
        // TODO add your handling code here:
        c_tmovimiento.setSelectedItem("Salida");
        c_toperacion.setSelectedItem("Inventario");
        titulos();
    }//GEN-LAST:event_jButton11ActionPerformed

    private void cb_sin_ordenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cb_sin_ordenActionPerformed
        // TODO add your handling code here:
        t_er.setText("");
        orden_act=null;
        t_orden.setText("");
        t_tipo.setText("");
        t_marca.setText("");
        t_modelo.setText("");
        t_serie.setText("");
        t_compania.setText("");
        t_siniestro.setText("");
        t_asegurado.setText("");
        cb_acondicionamiento.setSelected(false);
        
        if(cb_sin_orden.isSelected()==true)
        {
            b_buscaorden.setEnabled(false);
            b_mas.setEnabled(true);
            b_menos.setEnabled(true);
            cb_especialidad.setSelectedIndex(0);
        }
        else
        {
            b_buscaorden.setEnabled(true);
            b_mas.setEnabled(false);
            b_menos.setEnabled(false);
            cb_especialidad.setSelectedIndex(0);
        }
        if(c_tmovimiento.getSelectedItem().toString().compareTo("Entrada")==0)//devolución de consumibles
        {
            Class[] types = new Class [] 
            {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class, 
                java.lang.String.class, java.lang.String.class, java.lang.Double.class, java.lang.Double.class,
                java.lang.Double.class, java.lang.Double.class
            };
            String[] columnas = new String [] {"No Parte","Modelo","Marca","Tipo","Catalogo","Medida","Existencias","Devoluciones"};
            model=new nuevoAlmacen.MyModel(0, columnas, types);
            model.setColumnaEditable(7, true);
            t_datos.setModel(model);
        }
        if(c_tmovimiento.getSelectedItem().toString().compareTo("Salida")==0)
        {
            Class[] types = new Class [] 
            {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class, 
                java.lang.String.class, java.lang.String.class, java.lang.Double.class, java.lang.Double.class,
                java.lang.Double.class, java.lang.Double.class
            };
            String[] columnas = new String [] {"No Parte","Modelo","Marca","Tipo","Catalogo","Medida","Existencias","Entregadas"}; 
            model=new nuevoAlmacen.MyModel(0, columnas, types);
            model.setColumnaEditable(7, true);
            t_datos.setModel(model);
        }
        busca();
    }//GEN-LAST:event_cb_sin_ordenActionPerformed

    private void buscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buscarActionPerformed
        // TODO add your handling code here:
        Session session = HibernateUtil.getSessionFactory().openSession();
        try
        {
            id_empleado="";
            t_er.setText("");
            cb_especialidad.setSelectedIndex(0);
            cb_acondicionamiento.setSelected(false);
            limpiar_tabla();
            sumaTotales();
            if(cb_sin_orden.isSelected()==true)
            {
                session.beginTransaction().begin();
                usr = (Usuario)session.get(Usuario.class, usr.getIdUsuario());
                if(usr.getConsultaEmpleados()==true || usr.getEditaEmpleados()==true)
                {
                    buscaEmpleado obj = new buscaEmpleado(new javax.swing.JFrame(), true, usr, this.sessionPrograma, false);
                    Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
                    obj.setLocation((d.width/2)-(obj.getWidth()/2), (d.height/2)-(obj.getHeight()/2));
                    obj.setVisible(true);
                    Empleado emp_act=obj.getReturnStatus();
                    if (emp_act!=null)
                    {
                        t_er.setText("");
                        emp_act=(Empleado)session.get(Empleado.class, emp_act.getIdEmpleado());
                        usr = (Usuario)session.get(Usuario.class, usr.getIdUsuario());
                        if(usr.getEditaEmpleados()==true)
                        {
                            id_empleado=""+emp_act.getIdEmpleado();
                            t_er.setText(emp_act.getNombre());
                        }
                        else
                        JOptionPane.showMessageDialog(null, "¡Acceso denegado!");
                    }
                    else
                    {
                        id_empleado="";
                        t_er.setText("");
                        cb_especialidad.setSelectedIndex(0);
                    }
                }
                else
                    JOptionPane.showMessageDialog(null, "¡Acceso denegado!");
            }
            else
            {
                if(orden_act!=null)
                {
                    if(cb_1.isVisible()==false || cb_1.getSelectedIndex()==0)
                    {
                        buscaResponsable resp = new buscaResponsable(new javax.swing.JFrame(), true, usr, this.sessionPrograma, orden_act);
                        Dimension d1 = Toolkit.getDefaultToolkit().getScreenSize();
                        resp.setLocation((d1.width/2)-(resp.getWidth()/2), (d1.height/2)-(resp.getHeight()/2));
                        resp.setVisible(true);
                        ArrayList aux = resp.getReturnStatus();
                        if (aux!=null)
                        {
                            id_empleado=aux.get(0).toString();
                            t_er.setText(aux.get(1).toString());
                            cb_especialidad.setSelectedItem(aux.get(2).toString());
                        }
                        else
                        {
                            id_empleado="";
                            t_er.setText("");
                            cb_especialidad.setSelectedIndex(0);
                        }
                    }
                    else
                    {
                        buscaExtra resp = new buscaExtra(new javax.swing.JFrame(), true, usr, this.sessionPrograma, orden_act);
                        Dimension d1 = Toolkit.getDefaultToolkit().getScreenSize();
                        resp.setLocation((d1.width/2)-(resp.getWidth()/2), (d1.height/2)-(resp.getHeight()/2));
                        resp.setVisible(true);
                        ArrayList aux = resp.getReturnStatus();
                        if (aux!=null)
                        {
                            id_trabajo=aux.get(0).toString();
                            id_empleado=aux.get(1).toString();
                            t_er.setText(aux.get(2).toString());
                            cb_especialidad.setSelectedItem("ADICIONAL");
                        }
                        else
                        {
                            id_trabajo="";
                            id_empleado="";
                            t_er.setText("");
                            cb_especialidad.setSelectedIndex(0);
                        }
                    }
                }
                else
                {
                    JOptionPane.showMessageDialog(null, "¡Debe seleccionar primero una orden!");
                }
            }
            
            
        }catch(Exception e)
        {
            e.printStackTrace();
        }
        if(session!=null)
        if(session.isOpen())
        session.close();
    }//GEN-LAST:event_buscarActionPerformed

    private void cb_1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cb_1ItemStateChanged
        // TODO add your handling code here:
        id_empleado="";
        t_er.setText("");
        t_er.setText("");
        cb_especialidad.setSelectedIndex(0);
        limpiar_tabla();
        sumaTotales();
    }//GEN-LAST:event_cb_1ItemStateChanged

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        // TODO add your handling code here:
        Session session = HibernateUtil.getSessionFactory().openSession();
        try
        {
            session.beginTransaction().begin();
            usr = (Usuario)session.get(Usuario.class, usr.getIdUsuario());
            if(usr.getConsultaMovimientoAlmacen()==true)
            {
                h.session(sessionPrograma);
                Reporte2 reporte2 = new Reporte2(usr, sessionPrograma, 26, configuracion);
                reporte2.jTabbedPane1.setSelectedIndex(4);
                reportes.setSize(1020, 570);
                reportes.getContentPane().removeAll();
                reportes.getContentPane().add(reporte2, java.awt.BorderLayout.CENTER);
                reportes.setVisible(true);
            }
            else
                JOptionPane.showMessageDialog(null, "¡Acceso denegado!");
        }catch(Exception e)
        {
            e.printStackTrace();
        }
        if(session!=null)
            if(session.isOpen())
                session.close();
    }//GEN-LAST:event_jButton9ActionPerformed

    private void cb_especialidadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cb_especialidadActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cb_especialidadActionPerformed

    private void cb_almacenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cb_almacenActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cb_almacenActionPerformed

    private void cb_acondicionamientoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cb_acondicionamientoActionPerformed
        // TODO add your handling code here:
        limpiar_tabla();
        if(cb_acondicionamiento.isSelected()==true)
        {
            if(t_orden.getText().compareTo("")!=0)
            {
                Session session = HibernateUtil.getSessionFactory().openSession();
                try
                {
                    session.beginTransaction().begin();
                    usr = (Usuario)session.get(Usuario.class, usr.getIdUsuario());
                    buscaEmpleado obj = new buscaEmpleado(new javax.swing.JFrame(), true, usr, this.sessionPrograma, false);
                    Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
                    obj.setLocation((d.width/2)-(obj.getWidth()/2), (d.height/2)-(obj.getHeight()/2));
                    obj.setVisible(true);
                    Empleado emp_act=obj.getReturnStatus();
                    if (emp_act!=null)
                    {
                        t_er.setText("");
                        emp_act=(Empleado)session.get(Empleado.class, emp_act.getIdEmpleado());
                        usr = (Usuario)session.get(Usuario.class, usr.getIdUsuario());
                        id_empleado=""+emp_act.getIdEmpleado();
                        t_er.setText(emp_act.getNombre());
                        cb_especialidad.setSelectedItem("ADICIONAL");
                        b_mas.setEnabled(true);
                        b_menos.setEnabled(true);
                    }
                    else
                    {
                        id_empleado="";
                        t_er.setText("");
                        cb_acondicionamiento.setSelected(false);
                        cb_especialidad.setSelectedIndex(0);
                        b_mas.setEnabled(false);
                        b_menos.setEnabled(false);
                    }
                }
                catch(Exception e)
                {
                    cb_acondicionamiento.setSelected(false);
                    cb_especialidad.setSelectedIndex(0);
                    b_mas.setEnabled(false);
                    b_menos.setEnabled(false);
                    e.printStackTrace();
                }
                if(session!=null)
                    if(session.isOpen())
                       session.close();
            }
            else
            {
                cb_acondicionamiento.setSelected(false);
                cb_especialidad.setSelectedIndex(0);
                JOptionPane.showMessageDialog(null, "¡Debe seleccionar primero una orden!");
            }
        }
        else
        {
            id_empleado="";
            t_er.setText("");
            cb_especialidad.setSelectedIndex(0);
            b_mas.setEnabled(false);
            b_menos.setEnabled(false);
            
        }
    }//GEN-LAST:event_cb_acondicionamientoActionPerformed

    private void cb_1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cb_1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cb_1ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JDialog autoriza;
    private javax.swing.JButton b_autorizar;
    private javax.swing.JButton b_buscaorden;
    private javax.swing.JButton b_buscapedido;
    private javax.swing.JButton b_detalles;
    private javax.swing.JButton b_guardar;
    private javax.swing.JButton b_mas;
    private javax.swing.JButton b_menos;
    private javax.swing.JButton buscar;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.JComboBox c_tmovimiento;
    private javax.swing.JComboBox c_toperacion;
    private javax.swing.JComboBox cb_1;
    private javax.swing.JCheckBox cb_acondicionamiento;
    private javax.swing.JComboBox cb_almacen;
    private javax.swing.JComboBox cb_especialidad;
    private javax.swing.JCheckBox cb_sin_orden;
    private javax.swing.JComboBox codigo;
    private javax.swing.JDialog datos;
    private javax.swing.JRadioButton f1;
    private javax.swing.JRadioButton f2;
    private javax.swing.JRadioButton f3;
    private javax.swing.JRadioButton f4;
    private javax.swing.JRadioButton f5;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanelM;
    private javax.swing.JPanel jPanelMalmacen;
    private javax.swing.JPanel jPanelOperaciones;
    private javax.swing.JPanel jPanelProveedor;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JLabel l_asegurado;
    private javax.swing.JLabel l_compania;
    private javax.swing.JLabel l_fecha;
    private javax.swing.JLabel l_iva;
    private javax.swing.JLabel l_marca;
    private javax.swing.JLabel l_modelo;
    private javax.swing.JLabel l_nmovimiento;
    private javax.swing.JLabel l_nreferencia;
    private javax.swing.JLabel l_serie;
    private javax.swing.JLabel l_siniestro;
    private javax.swing.JLabel l_subtotal;
    private javax.swing.JLabel l_tipo;
    private javax.swing.JLabel l_tipo_pedido;
    private javax.swing.JLabel l_total;
    private javax.swing.JRadioButton r1;
    private javax.swing.JRadioButton r2;
    private javax.swing.JDialog reportes;
    private javax.swing.JFormattedTextField t_IVA;
    private javax.swing.JTextField t_asegurado;
    private javax.swing.JTextField t_compania;
    private javax.swing.JPasswordField t_contra;
    private javax.swing.JTable t_datos;
    private javax.swing.JTextField t_er;
    private javax.swing.JTextField t_fecha;
    private javax.swing.JTextField t_folio;
    private javax.swing.JTextField t_marca;
    private javax.swing.JTextField t_modelo;
    private javax.swing.JTextField t_nmovimiento;
    private javax.swing.JTextArea t_notas;
    private javax.swing.JTextField t_nreferencia;
    private javax.swing.JTextField t_orden;
    private javax.swing.JTextField t_pedido;
    private javax.swing.JTextField t_serie;
    private javax.swing.JTextField t_siniestro;
    private javax.swing.JFormattedTextField t_subtotal;
    private javax.swing.JTextField t_tipo;
    private javax.swing.JFormattedTextField t_total;
    private javax.swing.JTextField t_user;
    // End of variables declaration//GEN-END:variables
    private Orden buscarOrden(int id)
    {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try 
        {
            session.beginTransaction().begin();
            Orden ord = (Orden)session.get(Orden.class, id);
            session.getTransaction().commit();
            return ord;
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
    
    public void busca()
    {
        h= new Herramientas(usr, menu);
        h.session(sessionPrograma);
        Session session = HibernateUtil.getSessionFactory().openSession();
        try
        {
            session.beginTransaction().begin();
            if(c_toperacion.getSelectedItem().toString().compareTo("Pedido")==0)
            {
                Pedido pedido=(Pedido)session.get(Pedido.class, Integer.parseInt(t_pedido.getText()));
                l_tipo_pedido.setText(pedido.getTipoPedido());
                if(this.l_tipo_pedido.getText().compareTo("Interno")==0)
                {
                    session.beginTransaction().begin();
                    if(c_tmovimiento.getSelectedItem().toString().compareTo("Entrada")==0)
                    {
                        String[] columnas = new String [] {"Id","N°","#","N° Parte","Descripción","Medida","Pedidos","X Surtir","Entrada","Costo c/u","Total"};//, "OK"};//para antes de entrada 
                        Class[] types = new Class [] 
                        {
                            java.lang.String.class, java.lang.String.class, java.lang.String.class,
                            java.lang.String.class, java.lang.String.class, java.lang.String.class,
                            java.lang.Double.class, java.lang.Double.class, java.lang.Double.class,
                            java.lang.Double.class, java.lang.Double.class
                        };
                        Query query = session.createQuery("SELECT DISTINCT par FROM Partida par "
                                + "LEFT JOIN FETCH par.movimientos movPart "
                                + "LEFT JOIN movPart.almacen alm "
                                + "where par.pedido.idPedido="+Integer.parseInt(t_pedido.getText()));
                        List partidas = query.list();
                        model=new nuevoAlmacen.MyModel(partidas.size(), columnas, types);
                        model.setColumnaEditable(8, true);
                        model.setColumnaEditable(3, true);
                        t_datos.setModel(model);
                        for(int a=0; a<partidas.size(); a++)
                        {
                            Partida par = (Partida) partidas.get(a);
                            Movimiento[] mov=(Movimiento[])par.getMovimientos().toArray(new Movimiento[0]);
                            double entradas=0, devoluciones=0;
                            for(int b=0; b<mov.length; b++)
                            {
                                Almacen alm=mov[b].getAlmacen();
                                if(alm.getTipoMovimiento()==1 && alm.getOperacion()==1)
                                    entradas+=mov[b].getCantidad();
                                if(alm.getTipoMovimiento()==2 && alm.getOperacion()==1)
                                    devoluciones+=mov[b].getCantidad();
                            }
                            double total_almacen=entradas-devoluciones;
                            double total=par.getCantPcp()-total_almacen;

                            model.setValueAt(par.getIdPartida(), a, 0);
                            model.setValueAt(par.getIdEvaluacion(), a, 1);
                            model.setValueAt(par.getSubPartida(), a, 2);
                            if(par.getEjemplar()!=null)
                                model.setValueAt(par.getEjemplar().getIdParte(), a, 3);
                            else
                                model.setValueAt("", a, 3);
                            String anotacion="";
                            if(par.getInstruccion()!=null)
                                anotacion=par.getInstruccion();
                            model.setValueAt(par.getCatalogo().getNombre()+" "+anotacion, a, 4);
                            model.setValueAt(par.getMed(), a, 5);
                            model.setValueAt(par.getCantPcp(), a, 6);
                            model.setValueAt(total, a, 7);
                            model.setValueAt(0.0d, a, 8);

                            if(par.getPcp()!=null)
                                model.setValueAt(par.getPcp(), a, 9);
                            else
                                model.setValueAt(par.getPcp(), a, 9);
                            System.out.println("Precio de compra"+par.getPcp());
                            model.setValueAt(0.0d, a, 10);
                        }
                        for(int m=model.getRowCount()-1; m>=0; m--)
                        {
                            if( ((double)model.getValueAt(m, 7)) ==0.0d )
                                model.removeRow(m);
                        }
                    }
                    if(c_tmovimiento.getSelectedItem().toString().compareTo("Salida")==0)
                    {
                        String[] columnas = new String [] {"Id","N°","#","N° Parte","Descripción","Medida","Pedidos","En almacen","Devolución","Costo c/u","Total"};
                        Class[] types = new Class [] 
                        {
                            java.lang.String.class, java.lang.String.class, java.lang.String.class,
                            java.lang.String.class, java.lang.String.class, java.lang.String.class,
                            java.lang.Double.class, java.lang.Double.class, java.lang.Double.class,
                            java.lang.Double.class, java.lang.Double.class
                        };
                        Query query = session.createQuery("SELECT DISTINCT par FROM Partida par "
                                + "LEFT JOIN FETCH par.movimientos movPart "
                                + "LEFT JOIN movPart.almacen alm "
                                + "where alm.operacion=1 and par.pedido.idPedido="+Integer.parseInt(t_pedido.getText()));
                                //+ "and par.pedido!="+null);
                        List partidas = query.list();
                        model=new nuevoAlmacen.MyModel(partidas.size(), columnas, types);
                        model.setColumnaEditable(8, true);
                        model.setColumnaEditable(3, true);
                        t_datos.setModel(model);
                        for(int a=0; a<partidas.size(); a++)
                        {
                            Partida par = (Partida) partidas.get(a);
                            Movimiento[] mov = (Movimiento[])session.createCriteria(Movimiento.class).add(Restrictions.eq("partida.idPartida", par.getIdPartida())).list().toArray(new Movimiento[0]);
                            double entradas=0.0d, devoluciones=0.0d, entregadas=0.0d, devueltas=0.0d;
                            for(int b=0; b<mov.length; b++)
                            {
                                Almacen alm=mov[b].getAlmacen();
                                if(alm.getTipoMovimiento()==1 && alm.getOperacion()==1)
                                    entradas+=mov[b].getCantidad();
                                if(alm.getTipoMovimiento()==2 && alm.getOperacion()==1)
                                    devoluciones+=mov[b].getCantidad();
                                if(alm.getTipoMovimiento()==1 && alm.getOperacion()==5)
                                    devueltas+=mov[b].getCantidad();
                                if(alm.getTipoMovimiento()==2 && alm.getOperacion()==5)
                                    entregadas+=mov[b].getCantidad();
                            }
                            double total_Pedido=entradas-devoluciones;
                            double total_operario=entregadas-devueltas;
                            double total=total_Pedido-total_operario;
                            model.setValueAt(par.getIdPartida(), a, 0);
                            model.setValueAt(par.getIdEvaluacion(), a, 1);
                            model.setValueAt(par.getSubPartida(), a, 2);
                            if(par.getEjemplar()!=null)
                                model.setValueAt(par.getEjemplar().getIdParte(), a, 3);
                            else
                                model.setValueAt("", a, 3);
                            String anotacion="";
                            if(par.getInstruccion()!=null)
                                anotacion=par.getInstruccion();
                            model.setValueAt(par.getCatalogo().getNombre()+" "+anotacion, a, 4);
                            model.setValueAt(par.getMed(), a, 5);
                            model.setValueAt(par.getCantPcp(), a, 6);
                            model.setValueAt(total, a, 7);
                            model.setValueAt(0.0d, a, 8);
                            if(par.getPcp()!=null)
                                model.setValueAt(par.getPcp(), a, 9);
                            else
                                model.setValueAt(par.getPcp(), a, 9);
                            model.setValueAt(0.0d, a, 10); 
                        }
                        for(int m=model.getRowCount()-1; m>=0; m--)
                        {
                            if( ((double)model.getValueAt(m, 7)) ==0.0d)
                                model.removeRow(m);
                        }
                    }
                    sumaTotales();
                    session.beginTransaction().commit();
                }
            
                if(this.l_tipo_pedido.getText().compareTo("Externo")==0)
                {
                    try
                    {
                        session.beginTransaction().begin();
                        if(c_tmovimiento.getSelectedItem().toString().compareTo("Entrada")==0)
                        {
                            String[] columnas = new String [] {"Id","N° Parte","Descripción","Medida","Pedidos","X Surtir","Entrada","Costo c/u","Total"};
                            Class[] types = new Class [] 
                            {
                                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class,
                                java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class,
                                java.lang.Double.class
                            };
                            Query query = session.createQuery("SELECT DISTINCT part FROM PartidaExterna part "
                                    + "where part.pedido.idPedido="+Integer.parseInt(t_pedido.getText()));
                                    //+ "and part.pedido!="+null);
                            List partidas = query.list();
                            model=new nuevoAlmacen.MyModel(partidas.size(), columnas, types);
                            model.setColumnaEditable(6, true);
                            t_datos.setModel(model);
                            for(int a=0; a<partidas.size(); a++)
                            {
                                PartidaExterna par = (PartidaExterna) partidas.get(a);
                                Movimiento[] mov=(Movimiento[])par.getMovimientos().toArray(new Movimiento[0]);
                                double entradas=0.0d, devoluciones=0.0d;
                                for(int b=0; b<mov.length; b++)
                                {
                                    Almacen alm=mov[b].getAlmacen();
                                    if(alm.getTipoMovimiento()==1 && alm.getOperacion()==2)
                                        entradas+=mov[b].getCantidad();
                                    if(alm.getTipoMovimiento()==2 && alm.getOperacion()==2)
                                        devoluciones+=mov[b].getCantidad();
                                }
                                double total_almacen=entradas-devoluciones;
                                double total=par.getCantidad()-total_almacen;
                                model.setValueAt(par.getIdPartidaExterna(), a, 0);
                                if(par.getNoParte()!=null)
                                    model.setValueAt(par.getNoParte(), a, 1);
                                else
                                    model.setValueAt("", a, 1);
                                model.setValueAt(par.getDescripcion(), a, 2);
                                model.setValueAt(par.getUnidad(), a, 3);
                                model.setValueAt(par.getCantidad(), a, 4);
                                model.setValueAt(total, a, 5);
                                model.setValueAt(total, a, 6);
                                model.setValueAt(par.getCosto(), a, 7);
                                model.setValueAt(total*par.getCosto(), a, 8);
                            }
                            for(int m=model.getRowCount()-1; m>=0; m--)
                            {
                                if( ((double)model.getValueAt(m, 5)) == 0.0d)
                                    model.removeRow(m);
                            }
                        }
                        if(c_tmovimiento.getSelectedItem().toString().compareTo("Salida")==0)
                        {
                            String[] columnas = new String [] {"Id","N° Parte","Descripción","Medida","Pedidos","En almacen","Devolución","Costo c/u","Total"};
                            Class[] types = new Class [] 
                            {
                                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class,
                                java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class,
                                java.lang.Double.class
                            };
                            Query query = session.createQuery("SELECT DISTINCT part FROM PartidaExterna part "
                                    + "LEFT JOIN FETCH part.movimientos movPart "
                                    + "LEFT JOIN movPart.almacen alm "
                                    + "where alm.operacion=2 and part.pedido.idPedido="+Integer.parseInt(t_pedido.getText()));
                            List partidas = query.list();
                            model=new nuevoAlmacen.MyModel(partidas.size(), columnas, types);
                            model.setColumnaEditable(6, true);
                            t_datos.setModel(model);
                            for(int a=0; a<partidas.size(); a++)
                            {
                                PartidaExterna par = (PartidaExterna) partidas.get(a);
                                Movimiento[] mov = (Movimiento[])session.createCriteria(Movimiento.class).add(Restrictions.eq("partidaExterna.idPartidaExterna", par.getIdPartidaExterna())).list().toArray(new Movimiento[0]);
                                double entradas=0, devoluciones=0, entregadas=0, devueltas=0;;
                                for(int b=0; b<mov.length; b++)
                                {
                                    Almacen alm=mov[b].getAlmacen();
                                    if(alm.getTipoMovimiento()==1 && alm.getOperacion()==2)
                                        entradas+=mov[b].getCantidad();
                                    if(alm.getTipoMovimiento()==2 && alm.getOperacion()==2)
                                        devoluciones+=mov[b].getCantidad();
                                    if(alm.getTipoMovimiento()==1 && alm.getOperacion()==6)
                                        devueltas+=mov[b].getCantidad();
                                    if(alm.getTipoMovimiento()==2 && alm.getOperacion()==6)
                                        entregadas+=mov[b].getCantidad();
                                }
                                double total_Pedido=entradas-devoluciones;
                                double total_operario=entregadas-devueltas;
                                double total=total_Pedido-total_operario;
                                model.setValueAt(par.getIdPartidaExterna(), a, 0);
                                if(par.getNoParte()!=null)
                                    model.setValueAt(par.getNoParte(), a, 1);
                                else
                                    model.setValueAt("", a, 1);
                                model.setValueAt(par.getDescripcion(), a, 2);
                                model.setValueAt(par.getUnidad(), a, 3);
                                model.setValueAt(par.getCantidad(), a, 4);
                                model.setValueAt(total, a, 5);
                                model.setValueAt(0.0d, a, 6);

                                if(par.getCosto()!=null)
                                    model.setValueAt(par.getCosto(), a, 7);
                                else
                                    model.setValueAt(0.0d, a, 7);
                                model.setValueAt(0.0d, a, 8);
                            }
                            for(int m=model.getRowCount()-1; m>=0; m--)
                            {
                                if( ((double)model.getValueAt(m, 5)) ==0.0d)
                                    model.removeRow(m);
                            }
                        }
                        sumaTotales();
                        session.beginTransaction().commit();
                    }catch(Exception e)
                    {
                        e.printStackTrace();
                        session.beginTransaction().rollback();
                    }
                    finally
                    {
                        if(session.isOpen()==true)
                            session.close();
                    }
                }
            
                if(this.l_tipo_pedido.getText().compareTo("Inventario")==0)
                {
                    try
                    {
                        session.beginTransaction().begin();
                        if(c_tmovimiento.getSelectedItem().toString().compareTo("Entrada")==0)
                        {
                            String[] columnas = new String [] {"Id","N° Parte","Descripción","Medida","Pedidos","X Surtir","Entrada","Costo c/u","Total"};
                            Class[] types = new Class [] 
                            {
                                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class,
                                java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class,
                                java.lang.Double.class
                            };
                            Query query = session.createQuery("SELECT DISTINCT part FROM PartidaExterna part "
                                    + "where part.pedido.idPedido="+Integer.parseInt(t_pedido.getText()));
                            List partidas = query.list();
                            model=new nuevoAlmacen.MyModel(partidas.size(), columnas, types);
                            model.setColumnaEditable(6, true);
                            t_datos.setModel(model);
                            for(int a=0; a<partidas.size(); a++)
                            {
                                PartidaExterna par = (PartidaExterna) partidas.get(a);
                                Movimiento[] mov=(Movimiento[])par.getMovimientos().toArray(new Movimiento[0]);
                                double entradas=0.0d, devoluciones=0.0d;
                                for(int b=0; b<mov.length; b++)
                                {
                                    Almacen alm=mov[b].getAlmacen();
                                    if(alm.getTipoMovimiento()==1 && alm.getOperacion()==7)
                                        entradas+=mov[b].getCantidad();
                                    if(alm.getTipoMovimiento()==2 && alm.getOperacion()==7)
                                        devoluciones+=mov[b].getCantidad();
                                }
                                double total_almacen=entradas-devoluciones;
                                double total=par.getCantidad()-total_almacen;
                                model.setValueAt(par.getIdPartidaExterna(), a, 0);
                                model.setValueAt(par.getEjemplar().getIdParte(), a, 1);
                                model.setValueAt(par.getDescripcion(), a, 2);
                                model.setValueAt(par.getUnidad(), a, 3);
                                model.setValueAt(par.getCantidad(), a, 4);
                                model.setValueAt(total, a, 5);
                                model.setValueAt(total, a, 6);
                                model.setValueAt(par.getCosto(), a, 7);
                                model.setValueAt(total*par.getCosto(), a, 8);
                            }
                            for(int m=model.getRowCount()-1; m>=0; m--)
                            {
                                if( ((double)model.getValueAt(m, 5)) == 0.0d)
                                    model.removeRow(m);
                            }
                        }
                        if(c_tmovimiento.getSelectedItem().toString().compareTo("Salida")==0)
                        {
                            String[] columnas = new String [] {"Id","N° Parte","Descripción","Medida","Pedidos","En almacen","Devolución","Costo c/u","Total"};
                            Class[] types = new Class [] 
                            {
                                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class,
                                java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class,
                                java.lang.Double.class
                            };
                            Query query = session.createQuery("SELECT DISTINCT part FROM PartidaExterna part "
                                    + "LEFT JOIN FETCH part.movimientos movPart "
                                    + "LEFT JOIN movPart.almacen alm "
                                    + "where alm.operacion=7 and part.pedido.idPedido="+Integer.parseInt(t_pedido.getText()));
                            List partidas = query.list();
                            model=new nuevoAlmacen.MyModel(partidas.size(), columnas, types);
                            model.setColumnaEditable(6, true);
                            t_datos.setModel(model);
                            for(int a=0; a<partidas.size(); a++)
                            {
                                PartidaExterna par = (PartidaExterna) partidas.get(a);
                                double existencas= par.getEjemplar().getExistencias();
                                Movimiento[] mov = (Movimiento[])session.createCriteria(Movimiento.class).add(Restrictions.eq("partidaExterna.idPartidaExterna", par.getIdPartidaExterna())).list().toArray(new Movimiento[0]);
                                double entradas=0, devoluciones=0;
                                for(int b=0; b<mov.length; b++)
                                {
                                    Almacen alm=mov[b].getAlmacen();
                                    if(alm.getTipoMovimiento()==1 && alm.getOperacion()==7)
                                        entradas+=mov[b].getCantidad();
                                    if(alm.getTipoMovimiento()==2 && alm.getOperacion()==7)
                                        devoluciones+=mov[b].getCantidad();
                                }
                                double total_Pedido=entradas-devoluciones;
                                double total=existencas;
                                if(existencas>=total_Pedido)
                                    total=total_Pedido;
                                model.setValueAt(par.getIdPartidaExterna(), a, 0);
                                model.setValueAt(par.getEjemplar().getIdParte(), a, 1);
                                /*if(par.getNoParte()!=null)
                                    model.setValueAt(par.getNoParte(), a, 1);
                                else
                                    model.setValueAt("", a, 1);*/
                                model.setValueAt(par.getDescripcion(), a, 2);
                                model.setValueAt(par.getUnidad(), a, 3);
                                model.setValueAt(par.getCantidad(), a, 4);
                                model.setValueAt(total, a, 5);
                                model.setValueAt(0.0d, a, 6);

                                if(par.getCosto()!=null)
                                    model.setValueAt(par.getCosto(), a, 7);
                                else
                                    model.setValueAt(0.0d, a, 7);
                                model.setValueAt(0.0d, a, 8);
                            }
                            for(int m=model.getRowCount()-1; m>=0; m--)
                            {
                                if( ((double)model.getValueAt(m, 5)) ==0.0d)
                                    model.removeRow(m);
                            }
                        }
                        sumaTotales();
                        session.beginTransaction().commit();
                    }catch(Exception e)
                    {
                        e.printStackTrace();
                        session.beginTransaction().rollback();
                    }
                    finally
                    {
                        if(session.isOpen()==true)
                            session.close();
                    }
                }
                
                if(this.l_tipo_pedido.getText().compareTo("Adicional")==0)
                {
                    try
                    {
                        session.beginTransaction().begin();
                        if(c_tmovimiento.getSelectedItem().toString().compareTo("Entrada")==0)
                        {
                            String[] columnas = new String [] {"Id","N°","#","N° Parte","Descripción","Medida","Pedidos","X Surtir","Entrada","Costo c/u","Total"};//, "Ok"}; //para antes de entrada
                            Class[] types = new Class [] 
                            {
                                java.lang.String.class, java.lang.String.class, java.lang.String.class,
                                java.lang.String.class, java.lang.String.class, java.lang.String.class,
                                java.lang.Double.class, java.lang.Double.class, java.lang.Double.class,
                                java.lang.Double.class, java.lang.Double.class
                                //, java.lang.Boolean.class //para antes de entrada
                            };
                            Query query = session.createQuery("SELECT DISTINCT part FROM PartidaExterna part "
                                    + "where part.pedido.idPedido="+Integer.parseInt(t_pedido.getText()));
                            List partidas = query.list();
                            model=new nuevoAlmacen.MyModel(partidas.size(), columnas, types);
                            model.setColumnaEditable(8, true);
                            t_datos.setModel(model);
                            for(int a=0; a<partidas.size(); a++)
                            {
                                PartidaExterna par = (PartidaExterna) partidas.get(a);
                                Movimiento[] mov=(Movimiento[])par.getMovimientos().toArray(new Movimiento[0]);
                                double entradas=0, devoluciones=0;
                                for(int b=0; b<mov.length; b++)
                                {
                                    Almacen alm=mov[b].getAlmacen();
                                    if(alm.getTipoMovimiento()==1 && alm.getOperacion()==3)
                                        entradas+=mov[b].getCantidad();
                                    if(alm.getTipoMovimiento()==2 && alm.getOperacion()==3)
                                        devoluciones+=mov[b].getCantidad();
                                }
                                double total_almacen=entradas-devoluciones;
                                double total=par.getCantidad()-total_almacen;
                                model.setValueAt(par.getIdPartidaExterna(), a, 0);
                                model.setValueAt(0, a, 1);
                                model.setValueAt(0, a, 2);
                                if(par.getNoParte()!=null)
                                    model.setValueAt(par.getNoParte(), a, 3);
                                else
                                    model.setValueAt("", a, 3);
                                model.setValueAt(par.getDescripcion(), a, 4);
                                model.setValueAt(par.getUnidad(), a, 5);
                                model.setValueAt(par.getCantidad(), a, 6);
                                model.setValueAt(total, a, 7);
                                model.setValueAt(0.0d, a, 8);
                                model.setValueAt(par.getCosto(), a, 9);
                                model.setValueAt(0.0d, a, 10);
                            }
                            for(int m=model.getRowCount()-1; m>=0; m--)
                            {
                                if( ((double)model.getValueAt(m, 7)) ==0.0d)
                                    model.removeRow(m);
                            }
                        }
                        if(c_tmovimiento.getSelectedItem().toString().compareTo("Salida")==0)
                        {
                            String[] columnas = new String [] {"Id","N°","#","N° Parte","Descripción","Medida","Pedidos","En almacen","Devolución","Costo c/u","Total"};
                            Class[] types = new Class [] 
                            {
                                java.lang.String.class, java.lang.String.class, java.lang.String.class,
                                java.lang.String.class, java.lang.String.class, java.lang.String.class,
                                java.lang.Double.class, java.lang.Double.class, java.lang.Double.class,
                                java.lang.Double.class, java.lang.Double.class
                            };
                            Query query = session.createQuery("SELECT DISTINCT part FROM PartidaExterna part "
                                    + "LEFT JOIN FETCH part.movimientos movPart "
                                    + "LEFT JOIN movPart.almacen alm "
                                    + "where alm.operacion=3 and part.pedido.idPedido="+Integer.parseInt(t_pedido.getText())
                                    + "and part.pedido!="+null);
                            List partidas = query.list();
                            model=new nuevoAlmacen.MyModel(partidas.size(), columnas, types);
                            model.setColumnaEditable(8, true);
                            t_datos.setModel(model);
                            for(int a=0; a<partidas.size(); a++)
                            {
                                PartidaExterna par = (PartidaExterna) partidas.get(a);
                                Movimiento[] mov = (Movimiento[])session.createCriteria(Movimiento.class).add(Restrictions.eq("partidaExterna.idPartidaExterna", par.getIdPartidaExterna())).list().toArray(new Movimiento[0]);
                                double entradas=0, devoluciones=0, entregadas=0, devueltas=0;;
                                for(int b=0; b<mov.length; b++)
                                {
                                    Almacen alm=mov[b].getAlmacen();
                                    if(alm.getTipoMovimiento()==1 && alm.getOperacion()==3)
                                        entradas+=mov[b].getCantidad();
                                    if(alm.getTipoMovimiento()==2 && alm.getOperacion()==3)
                                        devoluciones+=mov[b].getCantidad();
                                    if(alm.getTipoMovimiento()==1 && alm.getOperacion()==5)
                                        devueltas+=mov[b].getCantidad();
                                    if(alm.getTipoMovimiento()==2 && alm.getOperacion()==5)
                                        entregadas+=mov[b].getCantidad();
                                }
                                double total_Pedido=entradas-devoluciones;
                                double total_operario=entregadas-devueltas;
                                double total=total_Pedido-total_operario;
                                model.setValueAt(par.getIdPartidaExterna(), a, 0);
                                model.setValueAt(0, a, 1);
                                model.setValueAt(0, a, 2);
                                if(par.getNoParte()!=null)
                                    model.setValueAt(par.getNoParte(), a, 3);
                                else
                                    model.setValueAt("", a, 3);
                                model.setValueAt(par.getDescripcion(), a, 4);
                                model.setValueAt(par.getUnidad(), a, 5);
                                model.setValueAt(par.getCantidad(), a, 6);
                                model.setValueAt(total, a, 7);
                                model.setValueAt(0.0d, a, 8);
                                model.setValueAt(par.getCosto(), a, 9);
                                model.setValueAt(0.0d, a, 10);
                            }
                            for(int m=model.getRowCount()-1; m>=0; m--)
                            {
                                if( ((double)model.getValueAt(m, 7)) ==0.0d)
                                    model.removeRow(m);
                            }
                        }
                        sumaTotales();
                        session.beginTransaction().commit();
                    }catch(Exception e)
                    {
                        e.printStackTrace();
                        session.beginTransaction().rollback();
                    }
                    finally
                    {
                        if(session.isOpen()==true)
                            session.close();
                    }
                }
            }
            
            if(c_toperacion.getSelectedItem().toString().compareTo("Compañía")==0)
            {
                Class[] types = new Class [] 
                {
                    java.lang.String.class, java.lang.String.class, java.lang.String.class, 
                    java.lang.String.class, java.lang.String.class, java.lang.String.class,
                    java.lang.Double.class, java.lang.Double.class, java.lang.Double.class
                };
                if(t_orden.getText().compareTo("")!=0)
                {
                    if(c_tmovimiento.getSelectedItem().toString().compareTo("Entrada")==0)
                    {
                        String[] columnas = new String [] {"Id","N°","#","N° Parte","Descripción","Medida","Autorizados","X Surtir","Entrada"};
                        Query query = session.createQuery("SELECT DISTINCT part FROM Partida part "
                                + "where part.ordenByIdOrden.idOrden="+Integer.parseInt(t_orden.getText())
                                + " and part.so="+true);
                        List partidas = query.list();
                        
                        model=new nuevoAlmacen.MyModel(partidas.size(), columnas, types);
                        model.setColumnaEditable(8, true);
                        t_datos.setModel(model);
                        for(int a=0; a<partidas.size(); a++)
                        {
                            Partida par = (Partida) partidas.get(a);
                            Movimiento[] mov=(Movimiento[])par.getMovimientos().toArray(new Movimiento[0]);
                            double entradas=0, devoluciones=0;
                            for(int b=0; b<mov.length; b++)
                            {
                                Almacen alm=mov[b].getAlmacen();
                                if(alm.getTipoMovimiento()==1 && alm.getOperacion()==4)
                                    entradas+=mov[b].getCantidad();
                                if(alm.getTipoMovimiento()==2 && alm.getOperacion()==4)
                                    devoluciones+=mov[b].getCantidad();
                            }
                            double total_almacen=entradas-devoluciones;
                            double total=par.getCantidadAut()-total_almacen;
                            model.setValueAt(par.getIdPartida(), a, 0);
                            model.setValueAt(par.getIdEvaluacion(), a, 1);
                            model.setValueAt(par.getSubPartida(), a, 2);
                            if(par.getEjemplar()!=null)
                                model.setValueAt(par.getEjemplar().getIdParte(), a, 3);
                            else
                                model.setValueAt("", a, 3);
                            String anotacion="";
                            if(par.getInstruccion()!=null)
                                anotacion=par.getInstruccion();
                            model.setValueAt(par.getCatalogo().getNombre()+" "+anotacion, a, 4);
                            model.setValueAt(par.getMed(), a, 5);
                            model.setValueAt(par.getCantidadAut(), a, 6);
                            model.setValueAt(total, a, 7);
                            model.setValueAt(0.0d, a, 8);
                        } 
                        for(int m=model.getRowCount()-1; m>=0; m--)
                        {
                            if( ((double)model.getValueAt(m, 7)) == 0.0d)
                                model.removeRow(m);
                        }
                    }
                    if(c_tmovimiento.getSelectedItem().toString().compareTo("Salida")==0)
                    {
                        String[] columnas = new String [] {"Id","N°","#","N° Parte","Descripción","Medida","Autorizados","En almacen","Devolución"};
                        Query query = session.createQuery("SELECT DISTINCT part FROM Partida part "
                                + "LEFT JOIN FETCH part.movimientos movPart "
                                + "LEFT JOIN movPart.almacen alm "
                                + "where alm.operacion=4 and part.ordenByIdOrden.idOrden="+Integer.parseInt(t_orden.getText()));
                        List partidas = query.list();
                        model=new nuevoAlmacen.MyModel(partidas.size(), columnas, types);
                        model.setColumnaEditable(8, true);
                        t_datos.setModel(model);
                        for(int a=0; a<partidas.size(); a++)
                        {
                            Partida par = (Partida) partidas.get(a);
                            Movimiento[] mov = (Movimiento[])session.createCriteria(Movimiento.class).add(Restrictions.eq("partida.idPartida", par.getIdPartida())).list().toArray(new Movimiento[0]);
                            double entradas=0, devoluciones=0, entregadas=0, devueltas=0;;
                            for(int b=0; b<mov.length; b++)
                            {
                                Almacen alm=mov[b].getAlmacen();
                                if(alm.getTipoMovimiento()==1 && alm.getOperacion()==4)
                                    entradas+=mov[b].getCantidad();
                                if(alm.getTipoMovimiento()==2 && alm.getOperacion()==4)
                                    devoluciones+=mov[b].getCantidad();
                                if(alm.getTipoMovimiento()==1 && alm.getOperacion()==5)
                                    devueltas+=mov[b].getCantidad();
                                if(alm.getTipoMovimiento()==2 && alm.getOperacion()==5)
                                    entregadas+=mov[b].getCantidad();
                            }
                            double total_Pedido=entradas-devoluciones;
                            double total_operario=entregadas-devueltas;
                            double total=total_Pedido-total_operario;
                            model.setValueAt(par.getIdPartida(), a, 0);
                            model.setValueAt(par.getIdEvaluacion(), a, 1);
                            model.setValueAt(par.getSubPartida(), a, 2);
                            if(par.getEjemplar()!=null)
                                model.setValueAt(par.getEjemplar().getIdParte(), a, 3);
                            else
                                model.setValueAt("", a, 3);
                            String anotacion="";
                            if(par.getInstruccion()!=null)
                                anotacion=par.getInstruccion();
                            model.setValueAt(par.getCatalogo().getNombre()+" "+anotacion, a, 4);
                            model.setValueAt(par.getMed(), a, 5);
                            model.setValueAt(par.getCantidadAut(), a, 6);
                            model.setValueAt(total, a, 7);
                            model.setValueAt(0.0d, a, 8);
                        } 
                        for(int m=model.getRowCount()-1; m>=0; m--)
                        {
                            if( ((double)model.getValueAt(m, 7)) == 0.0d)
                                model.removeRow(m);
                        }
                    }
                }
            }
            if(c_toperacion.getSelectedItem().toString().compareTo("Operarios")==0)
            {
                if(t_orden.getText().compareTo("")!=0)
                {
                    if(c_tmovimiento.getSelectedItem().toString().compareTo("Entrada")==0)
                    {
                        Class[] types = new Class [] 
                        {
                            java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, 
                            java.lang.String.class, java.lang.String.class, java.lang.String.class, 
                            java.lang.Double.class, java.lang.Double.class
                        };
                        String[] columnas = new String [] {"Id","Partida","Tipo","N° Parte","Descripción","Medida","Operario","Devoluciones"};
                        
                        String q1="SELECT DISTINCT part FROM Partida part "
                                + "LEFT JOIN FETCH part.movimientos movPart "
                                + "LEFT JOIN movPart.almacen alm "
                                + "where alm.operacion=5 and part.ordenByIdOrden.idOrden="+Integer.parseInt(t_orden.getText());
                        
                        String q2="SELECT DISTINCT partEx FROM PartidaExterna partEx "
                                + "LEFT JOIN FETCH partEx.movimientos movEx "
                                + "LEFT JOIN movEx.almacen alm "
                                + "where alm.operacion=5 and partEx.pedido.orden.idOrden="+Integer.parseInt(t_orden.getText());
                        
                        if(f1.isSelected())
                        {
                            q1+=" and part.espHoj=true";
                            q2+=" and partEx.pedido.partida.espHoj=true";
                        }
                        if(f2.isSelected())
                        {
                            q1+=" and part.espMec=true";
                            q2+=" and partEx.pedido.partida.espMec=true";
                        }
                        if(f3.isSelected())
                        {
                            q1+=" and part.espSus=true";
                            q2+=" and partEx.pedido.partida.espSus=true";
                        }
                        if(f4.isSelected())
                        {
                            q1+=" and part.espEle=true";
                            q2+=" and partEx.pedido.partida.espEle=true";
                        }
                        
                        Query query = session.createQuery(q1);
                        List partidas = query.list();
                        
                        Query query1 = session.createQuery(q2);
                        List partidasExternas = query1.list();
                        
                        model=new nuevoAlmacen.MyModel(partidas.size()+partidasExternas.size(), columnas, types);
                        model.setColumnaEditable(7, true);
                        t_datos.setModel(model);
                        
                        for(int a=0; a<partidas.size(); a++)
                        {
                            Partida par = (Partida) partidas.get(a);
                            Movimiento[] mov=(Movimiento[])par.getMovimientos().toArray(new Movimiento[0]);
                            double entregadas=0, devueltas=0;
                            for(int b=0; b<mov.length; b++)
                            {
                                Almacen alm=mov[b].getAlmacen();
                                if(alm.getTipoMovimiento()==1 && alm.getOperacion()==5)
                                    devueltas+=mov[b].getCantidad();
                                if(alm.getTipoMovimiento()==2 && alm.getOperacion()==5)
                                    entregadas+=mov[b].getCantidad();
                            }
                            double total=entregadas-devueltas;
                            model.setValueAt(par.getIdPartida(), a, 0);
                            String gm="";
                            if(par.isEspEle()==true)
                                gm="E";
                            if(par.isEspHoj()==true)
                                gm="H";
                            if(par.isEspMec()==true)
                                gm="M";
                            if(par.isEspSus()==true)
                                gm="S";
                            model.setValueAt(par.getIdEvaluacion()+"-"+par.getSubPartida()+"-"+gm, a, 1);
                            if(par.getPedido()!=null)
                                model.setValueAt("PED.", a, 2);
                            else
                            {
                                if(par.isSurteAlmacen()==true)
                                    model.setValueAt("ALM.", a, 2);
                                else
                                    model.setValueAt("COM.", a, 2);
                            }
                            if(par.getEjemplar()!=null)
                                model.setValueAt(par.getEjemplar().getIdParte(), a, 3);
                            else
                                model.setValueAt("", a, 3);
                            String anotacion="";
                            if(par.getInstruccion()!=null)
                                anotacion=par.getInstruccion();
                            model.setValueAt(par.getCatalogo().getNombre()+" "+anotacion, a, 4);
                            model.setValueAt(par.getMed(), a, 5);
                            model.setValueAt(total, a, 6);
                            model.setValueAt(0.0d, a, 7);
                        }
                        
                        for(int a=0; a<partidasExternas.size(); a++)
                        {
                            PartidaExterna par = (PartidaExterna) partidasExternas.get(a);
                            Movimiento[] mov=(Movimiento[])par.getMovimientos().toArray(new Movimiento[0]);
                            double entregadas=0, devueltas=0;
                            for(int b=0; b<mov.length; b++)
                            {
                                Almacen alm=mov[b].getAlmacen();
                                if(alm.getTipoMovimiento()==1 && alm.getOperacion()==5)
                                    devueltas+=mov[b].getCantidad();
                                if(alm.getTipoMovimiento()==2 && alm.getOperacion()==5)
                                    entregadas+=mov[b].getCantidad();
                            }
                            double total=entregadas-devueltas;
                            model.setValueAt(par.getIdPartidaExterna(), a+partidas.size(), 0);
                            String gm="";
                            model.setValueAt("-"+gm, a+partidas.size(), 1);
                            model.setValueAt("ADI.", a+partidas.size(), 2);
                            if(par.getNoParte()!=null)
                                model.setValueAt(par.getNoParte(), a+partidas.size(), 3);
                            else
                                model.setValueAt("", a+partidas.size(), 3);
                            model.setValueAt(par.getDescripcion(), a+partidas.size(), 4);
                            model.setValueAt(par.getUnidad(), a+partidas.size(), 5);
                            model.setValueAt(total, a+partidas.size(), 6);
                            model.setValueAt(0.0d, a+partidas.size(), 7);
                        }
                        for(int m=model.getRowCount()-1; m>=0; m--)
                        {
                            if( ((double)model.getValueAt(m, 6)) == 0.0d)
                                model.removeRow(m);
                        }
                    }
                    if(c_tmovimiento.getSelectedItem().toString().compareTo("Salida")==0)
                    {
                        Class[] types = new Class [] 
                        {
                            java.lang.String.class, java.lang.String.class, java.lang.String.class, 
                            java.lang.String.class, java.lang.String.class, java.lang.String.class, 
                            java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, 
                            java.lang.Double.class, java.lang.Boolean.class //para pedido op
                        };
                        String[] columnas = new String [] {"Id","Partida","Tipo","N° Parte","Descripción","Medida","Solicitado","Existencias","Operario","Entregadas","Solicita"}; //para pedido op
                        String q1="SELECT DISTINCT part FROM Partida part "
                                + "LEFT JOIN FETCH part.movimientos movPart "
                                + "LEFT JOIN movPart.almacen alm "
                                + "where part.ordenByIdOrden.idOrden="+Integer.parseInt(t_orden.getText())
                                + " and alm!="+null+ " and part.surteAlmacen=false";
                        
                        String q2="SELECT DISTINCT partEx FROM PartidaExterna partEx "
                                + "LEFT JOIN FETCH partEx.movimientos movEx "
                                + "LEFT JOIN movEx.almacen alm "
                                + "where alm.operacion in (3, 5) and partEx.pedido.orden.idOrden="+Integer.parseInt(t_orden.getText());
                        
                        String q3="SELECT DISTINCT part FROM Partida part "
                                + "where part.ordenByIdOrden.idOrden="+Integer.parseInt(t_orden.getText())
                                + " and part.surteAlmacen=true";
                        
                        if(f1.isSelected())
                        {
                            q1+=" and part.espHoj=true";
                            q2+=" and partEx.pedido.partida.espHoj=true";
                            q3+=" and part.espHoj=true";
                        }
                        if(f2.isSelected())
                        {
                            q1+=" and part.espMec=true";
                            q2+=" and partEx.pedido.partida.espMec=true";
                            q3+=" and part.espMec=true";
                        }
                        if(f3.isSelected())
                        {
                            q1+=" and part.espSus=true";
                            q2+=" and partEx.pedido.partida.espSus=true";
                            q3+=" and part.espSus=true";
                        }
                        if(f4.isSelected())
                        {
                            q1+=" and part.espEle=true";
                            q2+=" and partEx.pedido.partida.espEle=true";
                            q3+=" and part.espEle=true";
                        }
                        
                        Query query = session.createQuery(q1);
                        List partidas = query.list();
                        
                        Query query1 = session.createQuery(q2);
                        List partidasExternas = query1.list();
                        
                        Query query2 = session.createQuery(q3);
                        List partidasAlmacen = query2.list();
                        
                        System.out.println("Partidas:"+partidas.size()+"PartidasExternas:"+partidasExternas.size()+"PartidasAlmacen:"+partidasAlmacen.size());
                        model=new nuevoAlmacen.MyModel(partidas.size()+partidasExternas.size()+partidasAlmacen.size(), columnas, types);
                        model.setColumnaEditable(9, true);
                        model.setColumnaEditable(10, true);
                        t_datos.setModel(model);
                        for(int a=0; a<partidas.size(); a++)//Partidas con estradas por pedido
                        {
                            Partida par = (Partida) partidas.get(a);
                            Movimiento[] mov=(Movimiento[])par.getMovimientos().toArray(new Movimiento[0]);
                            double entradas=0, devoluciones=0, entregadas=0, devueltas=0;
                            for(int b=0; b<mov.length; b++)
                            {
                                Almacen alm=mov[b].getAlmacen();
                                //Entradad por pedido interno
                                if(alm.getTipoMovimiento()==1 && alm.getOperacion()==1)
                                    entradas+=mov[b].getCantidad();
                                if(alm.getTipoMovimiento()==2 && alm.getOperacion()==1)
                                    devoluciones+=mov[b].getCantidad();
                                //entrada por compañia
                                if(alm.getTipoMovimiento()==1 && alm.getOperacion()==4)
                                    entradas+=mov[b].getCantidad();
                                if(alm.getTipoMovimiento()==2 && alm.getOperacion()==4)
                                    devoluciones+=mov[b].getCantidad();
                                if(alm.getTipoMovimiento()==1 && alm.getOperacion()==5)
                                    devueltas+=mov[b].getCantidad();
                                if(alm.getTipoMovimiento()==2 && alm.getOperacion()==5)
                                    entregadas+=mov[b].getCantidad();
                            }
                            double total_Pedido=entradas-devoluciones;
                            double total_operario=entregadas-devueltas;
                            double total=total_Pedido-total_operario;
                            model.setValueAt(par.getIdPartida(), a, 0);
                            model.setValueAt(par.getIdEvaluacion()+"-"+par.getSubPartida(), a, 1);
                            if(par.getPedido()==null)
                                model.setValueAt("COM.", a, 2);
                            else
                                model.setValueAt("PED.", a, 2);
                            if(par.getEjemplar()!=null)
                                model.setValueAt(par.getEjemplar().getIdParte(), a, 3);
                            else
                                model.setValueAt("", a, 3);
                            String anotacion="";
                            if(par.getInstruccion()!=null)
                                anotacion=par.getInstruccion();
                            model.setValueAt(par.getCatalogo().getNombre()+" "+anotacion, a, 4);
                            model.setValueAt(par.getMed(), a, 5);
                            if(par.getPedido()!=null)
                                model.setValueAt(par.getCantPcp(), a, 6);
                            else
                                model.setValueAt(par.getCantidadAut(), a, 6);
                            model.setValueAt(total, a, 7);
                            model.setValueAt(total_operario, a, 8);
                            model.setValueAt(0.0d, a, 9);
                            model.setValueAt(par.getOp(), a, 10); //para pedido op
                        }
                        for(int a=0; a<partidasAlmacen.size(); a++)//Partidas marcadas para entrega por almacen
                        {
                            Partida par = (Partida) partidasAlmacen.get(a);
                            Movimiento[] mov=(Movimiento[])par.getMovimientos().toArray(new Movimiento[0]);
                            double entregadas=0, devueltas=0;
                            for(int b=0; b<mov.length; b++)
                            {
                                Almacen alm=mov[b].getAlmacen();
                                if(alm.getTipoMovimiento()==1 && alm.getOperacion()==5)
                                    devueltas+=mov[b].getCantidad();
                                if(alm.getTipoMovimiento()==2 && alm.getOperacion()==5)
                                    entregadas+=mov[b].getCantidad();
                            }
                            double total_operario=entregadas-devueltas;
                            
                            double total=par.getCantidadAut()-total_operario;
                            model.setValueAt(par.getIdPartida(), a+partidas.size(), 0);
                            model.setValueAt(par.getIdEvaluacion()+"-"+par.getSubPartida(), a+partidas.size(), 1);
                            model.setValueAt("ALM.", a+partidas.size(), 2);
                            
                            if(par.getEjemplar()!=null)
                                model.setValueAt(par.getEjemplar().getIdParte(), a+partidas.size(), 3);
                            else
                                model.setValueAt("", a+partidas.size(), 3);
                            String anotacion="";
                            if(par.getInstruccion()!=null)
                                anotacion=par.getInstruccion();
                            model.setValueAt(par.getCatalogo().getNombre()+" "+anotacion, a+partidas.size(), 4);
                            model.setValueAt(par.getMed(), a+partidas.size(), 5);
                            model.setValueAt(par.getCantidadAut(), a+partidas.size(), 6);
                            if(par.getEjemplar()!=null)
                            {
                                if(par.getEjemplar().getExistencias()!=null)
                                    model.setValueAt(par.getEjemplar().getExistencias(), a+partidas.size(), 7);
                                else
                                    model.setValueAt(0.0d, a+partidas.size(), 7);
                            }
                            else
                                model.setValueAt(0.0d, a+partidas.size(), 7);
                            model.setValueAt(total_operario, a+partidas.size(), 8);
                            model.setValueAt(0.0d, a+partidas.size(), 9);
                            //model.setValueAt(par.getPerdidases().size(), a, 9);// para pedido op
                            model.setValueAt(par.getOp(), a+partidas.size(), 10); //para pedido op
                        }
                        for(int a=0; a<partidasExternas.size(); a++)
                        {
                            PartidaExterna par = (PartidaExterna) partidasExternas.get(a);
                            Movimiento[] mov=(Movimiento[])par.getMovimientos().toArray(new Movimiento[0]);
                            double entradas=0, devoluciones=0, entregadas=0, devueltas=0;
                            for(int b=0; b<mov.length; b++)
                            {
                                Almacen alm=mov[b].getAlmacen();
                                if(alm.getTipoMovimiento()==1 && alm.getOperacion()==3)
                                    entradas+=mov[b].getCantidad();
                                if(alm.getTipoMovimiento()==2 && alm.getOperacion()==3)
                                    devoluciones+=mov[b].getCantidad();
                                if(alm.getTipoMovimiento()==1 && alm.getOperacion()==5)
                                    devueltas+=mov[b].getCantidad();
                                if(alm.getTipoMovimiento()==2 && alm.getOperacion()==5)
                                    entregadas+=mov[b].getCantidad();
                            }
                            double total_Pedido=entradas-devoluciones;
                            double total_operario=entregadas-devueltas;
                            double total=total_Pedido-total_operario;
                            model.setValueAt(par.getIdPartidaExterna(), a+partidas.size()+partidasAlmacen.size(), 0);
                            model.setValueAt("-", a+partidas.size()+partidasAlmacen.size(), 1);
                            model.setValueAt("ADI.", a+partidas.size()+partidasAlmacen.size(), 2);
                            if(par.getNoParte()!=null)
                                model.setValueAt(par.getNoParte(), a+partidas.size()+partidasAlmacen.size(), 3);
                            else
                                model.setValueAt("", a+partidas.size()+partidasAlmacen.size(), 3);
                            model.setValueAt(par.getDescripcion(), a+partidas.size()+partidasAlmacen.size(), 4);
                            model.setValueAt(par.getUnidad(), a+partidas.size()+partidasAlmacen.size(), 5);
                            model.setValueAt(par.getCantidad(), a+partidas.size()+partidasAlmacen.size(), 6);
                            model.setValueAt(total, a+partidas.size()+partidasAlmacen.size(), 7);
                            model.setValueAt(total_operario, a+partidas.size()+partidasAlmacen.size(), 8);
                            model.setValueAt(0.0d, a+partidas.size()+partidasAlmacen.size(), 9);
                            //model.setValueAt(0, a+partidas.size(), 9);//para op
                            model.setValueAt(par.getOp(), a+partidas.size()+partidasAlmacen.size(), 10); //para pedido op
                        }
                        for(int m=model.getRowCount()-1; m>=0; m--)
                        {
                            if( ((double)model.getValueAt(m, 7)) == 0.0d || ((double)model.getValueAt(m, 6) - (double)model.getValueAt(m, 8))==0)
                                model.removeRow(m);
                        }
                    }
                }
            }
            if(c_toperacion.getSelectedItem().toString().compareTo("Inventario")==0)
            {
                if(t_orden.getText().compareTo("")!=0)
                {
                    if(c_tmovimiento.getSelectedItem().toString().compareTo("Entrada")==0)//devolución de consumibles
                    {
                        Class[] types = new Class [] 
                        {
                            java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Double.class, 
                            java.lang.String.class, java.lang.String.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class,
                            java.lang.Double.class, java.lang.Double.class
                        };
                        String[] columnas = new String [] {"No Parte","Modelo","Marca","Autorizados","Catalogo","Medida","Existencias","Operario","Devoluciones", "Costo c/u","Total"};
                        model=new nuevoAlmacen.MyModel(0, columnas, types);
                        model.setColumnaEditable(8, true);
                        t_datos.setModel(model);
                    }
                    if(c_tmovimiento.getSelectedItem().toString().compareTo("Salida")==0)
                    {
                        Class[] types = new Class [] 
                        {
                            java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Double.class, 
                            java.lang.String.class, java.lang.String.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class,
                            java.lang.Double.class, java.lang.Double.class
                        };
                        String[] columnas = new String [] {"No Parte","Modelo","Marca","Autorizados","Catalogo","Medida","Existencias","Operario","Entregadas", "Costo c/u","Total"}; 
                        model=new nuevoAlmacen.MyModel(0, columnas, types);
                        model.setColumnaEditable(8, true);
                        t_datos.setModel(model);
                    }
                }
            }
            if(c_toperacion.getSelectedItem().toString().compareTo("Venta")==0)
            {
                Pedido pedido=(Pedido)session.get(Pedido.class, Integer.parseInt(t_pedido.getText()));
                l_tipo_pedido.setText(pedido.getTipoPedido());
                session.beginTransaction().begin();
                if(c_tmovimiento.getSelectedItem().toString().compareTo("Entrada")==0)
                {
                    Class[] types = new Class [] 
                    {
                        java.lang.String.class, 
                        java.lang.String.class, 
                        java.lang.String.class, 
                        java.lang.String.class, 
                        java.lang.Double.class,
                        java.lang.Double.class
                    };
                    String[] columnas = new String [] {"Id","N° Parte","Descripción","Medida","Entregados","Devueltos"};
                    Query query = session.createQuery("SELECT DISTINCT part FROM PartidaExterna part "
                            + "LEFT JOIN FETCH part.movimientos movPart "
                            + "LEFT JOIN movPart.almacen alm "
                            + "where alm.operacion=2 and part.pedido.idPedido="+Integer.parseInt(t_pedido.getText())
                            + "and part.pedido!="+null);
                    List partidas = query.list();
                    model=new nuevoAlmacen.MyModel(partidas.size(), columnas, types);
                    model.setColumnaEditable(5, true);
                    t_datos.setModel(model);
                    for(int a=0; a<partidas.size(); a++)
                    {
                        PartidaExterna par = (PartidaExterna) partidas.get(a);
                        Movimiento[] mov = (Movimiento[])session.createCriteria(Movimiento.class).add(Restrictions.eq("partidaExterna.idPartidaExterna", par.getIdPartidaExterna())).list().toArray(new Movimiento[0]);
                        double entregados=0.0d, devoluciones=0.0d;
                        for(int b=0; b<mov.length; b++)
                        {
                            Almacen alm=mov[b].getAlmacen();
                            if(alm.getTipoMovimiento()==1 && alm.getOperacion()==6)
                                devoluciones+=mov[b].getCantidad();
                            if(alm.getTipoMovimiento()==2 && alm.getOperacion()==6)
                                entregados+=mov[b].getCantidad();
                        }
                        double total=entregados-devoluciones;
                        model.setValueAt(par.getIdPartidaExterna(), a, 0);
                        if(par.getNoParte()!=null)
                            model.setValueAt(par.getNoParte(), a, 1);
                        else
                            model.setValueAt("", a, 1);
                        model.setValueAt(par.getDescripcion(), a, 2);
                        model.setValueAt(par.getUnidad(), a, 3);
                        model.setValueAt(total, a, 4);
                        model.setValueAt(0.0d, a, 5);
                    }
                    for(int m=model.getRowCount()-1; m>=0; m--)
                    {
                        if( ((double)model.getValueAt(m, 4)) == 0.0d)
                            model.removeRow(m);
                    }
                }
                if(c_tmovimiento.getSelectedItem().toString().compareTo("Salida")==0)
                {
                    Class[] types = new Class [] 
                    {
                        java.lang.String.class, 
                        java.lang.String.class, 
                        java.lang.String.class, 
                        java.lang.String.class, 
                        java.lang.Double.class,
                        java.lang.Double.class,
                        java.lang.Double.class,
                        java.lang.Double.class
                    };
                    String[] columnas = new String [] {"Id","N° Parte","Descripción","Medida","Pedidos","Entregadas","Existencias","Salida"};
                    Query query = session.createQuery("SELECT DISTINCT part FROM PartidaExterna part "
                            + "LEFT JOIN FETCH part.movimientos movPart "
                            + "LEFT JOIN movPart.almacen alm "
                            + "where alm.operacion=2 and part.pedido.idPedido="+Integer.parseInt(t_pedido.getText())
                            + "and part.pedido!="+null);
                    List partidas = query.list();
                    model=new nuevoAlmacen.MyModel(partidas.size(), columnas, types);
                    model.setColumnaEditable(7, true);
                    t_datos.setModel(model);
                    for(int a=0; a<partidas.size(); a++)
                    {
                        PartidaExterna par = (PartidaExterna) partidas.get(a);
                        Movimiento[] mov = (Movimiento[])session.createCriteria(Movimiento.class).add(Restrictions.eq("partidaExterna.idPartidaExterna", par.getIdPartidaExterna())).list().toArray(new Movimiento[0]);
                        double entradas=00.d, devoluciones=0.0d, entregadas=0.0d, devueltas=0.0d;
                        for(int b=0; b<mov.length; b++)
                        {
                            Almacen alm=mov[b].getAlmacen();
                            if(alm.getTipoMovimiento()==1 && alm.getOperacion()==2)
                                entradas+=mov[b].getCantidad();
                            if(alm.getTipoMovimiento()==2 && alm.getOperacion()==2)
                                devoluciones+=mov[b].getCantidad();
                            if(alm.getTipoMovimiento()==1 && alm.getOperacion()==6)
                                devueltas+=mov[b].getCantidad();
                            if(alm.getTipoMovimiento()==2 && alm.getOperacion()==6)
                                entregadas+=mov[b].getCantidad();
                        }
                        double total_Existencias=entradas-devoluciones;
                        double total_cliente=entregadas-devueltas;
                        double total=total_Existencias-total_cliente;
                        model.setValueAt(par.getIdPartidaExterna(), a, 0);
                        if(par.getNoParte()!=null)
                            model.setValueAt(par.getNoParte(), a, 1);
                        else
                            model.setValueAt("", a, 1);
                        model.setValueAt(par.getDescripcion(), a, 2);
                        model.setValueAt(par.getUnidad(), a, 3);
                        model.setValueAt(par.getCantidad(), a, 4);
                        model.setValueAt(total_cliente, a, 5);
                        model.setValueAt(total, a, 6);
                        model.setValueAt(total, a, 7);
                    }
                    for(int m=model.getRowCount()-1; m>=0; m--)
                    {
                        if( ((double)model.getValueAt(m, 6)) == 0.0d)
                            model.removeRow(m);
                    }
                }
                sumaTotales();
                session.beginTransaction().commit();
            }
        }catch(Exception e)
        {
            e.printStackTrace();
        }
        if(session!=null)
            if(session.isOpen())
                session.close();
        formatoTabla();
    }
    
    public class MyModel extends DefaultTableModel
    {        
        Class [] types;
        int ren=0;
        int col=0;
        private List celdaEditable;
        
        public MyModel(int renglones, String columnas[], Class[] tipos)
        {
            types=tipos;
            ren=renglones;
            col=columnas.length;
            celdaEditable=new ArrayList();
            for(int x=0; x<renglones; x++)
            {
                List aux=new ArrayList();
                for(int y=0; y<types.length; y++)
                    aux.add(false);
                celdaEditable.add(aux);
            }
            setDataVector(new Object [renglones][columnas.length], columnas);
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
            if(t_datos.getRowCount()>0)
            {
                Vector vector = (Vector)dataVector.elementAt(row);
                Object celda = ((Vector)dataVector.elementAt(row)).elementAt(col);
                switch(col)
                {
                    case 3:
                        if(vector.get(col)==null)
                        {
                            vector.setElementAt(value, col);
                            dataVector.setElementAt(vector, row);
                            fireTableCellUpdated(row, col);
                        }
                        else
                        {
                            if(c_toperacion.getSelectedItem().toString().compareTo("Pedido")==0)
                            {
                                Session session = HibernateUtil.getSessionFactory().openSession();
                                try
                                {
                                    session.beginTransaction().begin();
                                    usr = (Usuario)session.get(Usuario.class, usr.getIdUsuario());
                                    Partida part=(Partida) session.createCriteria(Partida.class).add(Restrictions.eq("pedido.idPedido", Integer.parseInt(t_pedido.getText()))).add(Restrictions.eq("idEvaluacion", Integer.parseInt(t_datos.getValueAt(t_datos.getSelectedRow(), 1).toString()))).add(Restrictions.eq("subPartida", Integer.parseInt(t_datos.getValueAt(t_datos.getSelectedRow(), 2).toString()))).setMaxResults(1).uniqueResult();
                                    if(part!=null)
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
                                                dataVector.setElementAt(vector, row);
                                                fireTableCellUpdated(row, col);
                                                if(session.isOpen()==true)
                                                    session.close();
                                            }
                                            else{
                                                Ejemplar e_nuevo=new Ejemplar();
                                                e_nuevo.setIdParte(value.toString());
                                                if(orden_act!=null){
                                                    if(orden_act.getMarca()!=null)
                                                    {
                                                        Marca mar=(Marca)session.get(Marca.class, orden_act.getMarca().getIdMarca());
                                                        e_nuevo.setMarca(mar);
                                                    }
                                                    else
                                                        e_nuevo.setMarca(null);
                                                }
                                                Catalogo cat_actual=(Catalogo)session.get(Catalogo.class, part.getCatalogo().getIdCatalogo());
                                                e_nuevo.setCatalogo(cat_actual);
                                                e_nuevo.setComentario("");
                                                e_nuevo.setInventario(0);
                                                session.save(e_nuevo);
                                                part.setEjemplar(e_nuevo);
                                                session.update(part);
                                                session.getTransaction().commit();
                                                vector.setElementAt(value, col);
                                                dataVector.setElementAt(vector, row);
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
                                            dataVector.setElementAt(vector, row);
                                            fireTableCellUpdated(row, col);
                                            if(session.isOpen()==true)
                                                session.close();
                                        }
                                    }
                                    else
                                        JOptionPane.showMessageDialog(null, "La partida ya no existe");
                                }
                                catch(Exception e)
                                {
                                    session.getTransaction().rollback();
                                    JOptionPane.showMessageDialog(null, "¡Error al cargar la información!");
                                    e.printStackTrace();
                                }
                                finally
                                {
                                    if(session!=null)
                                        if(session.isOpen()==true)
                                            session.close();
                                }
                            }
                            else
                            {
                                vector.setElementAt(value, col);
                                dataVector.setElementAt(vector, row);
                                fireTableCellUpdated(row, col);                            
                            }
                        }
                        break;

                        case 5:
                            if(vector.get(col)==null)
                            {
                                vector.setElementAt(value, col);
                                dataVector.setElementAt(vector, row);
                                fireTableCellUpdated(row, col);
                            }
                            else
                            {
                                if(c_toperacion.getSelectedItem().toString().compareTo("Venta")==0)
                                {
                                    if((double)value<=(double)t_datos.getValueAt(row, 4))
                                    {
                                        vector.setElementAt(value, col);
                                        dataVector.setElementAt(vector, row);
                                        fireTableCellUpdated(row, col);
                                    }
                                    else
                                        JOptionPane.showMessageDialog(null, "La cantidad máxima a devolver es: "+t_datos.getValueAt(row, 4)+t_datos.getValueAt(row, 3).toString());
                                }
                                else
                                {
                                    vector.setElementAt(value, col);
                                    dataVector.setElementAt(vector, row);
                                    fireTableCellUpdated(row, col);
                                }
                            }
                        break;

                        case 6:
                            double entrada=0.0, salida=0.0;
                            if(c_toperacion.getSelectedItem().toString().compareTo("Pedido")==0 && (l_tipo_pedido.getText().compareTo("Externo")==0 || l_tipo_pedido.getText().compareTo("Inventario")==0) )
                            {
                                if(vector.get(col)==null)
                                {
                                    vector.setElementAt(value, col);
                                    dataVector.setElementAt(vector, row);
                                    fireTableCellUpdated(row, col);
                                }
                                else
                                {
                                    Session session = HibernateUtil.getSessionFactory().openSession();
                                    try
                                    {
                                        if((double)value>=0)
                                        {
                                            if(c_tmovimiento.getSelectedItem().toString().compareTo("Entrada")==0)
                                            {
                                                if((double)value<=(double)t_datos.getValueAt(row, 5))
                                                {
                                                    vector.setElementAt(value, col);
                                                    dataVector.setElementAt(vector, row);
                                                    fireTableCellUpdated(row, col);
                                                    sumaTotales();
                                                }
                                                else
                                                    JOptionPane.showMessageDialog(null, "La cantidad máxima por entrar es: "+t_datos.getValueAt(row, 5)+t_datos.getValueAt(row, 3).toString()); 
                                            }
                                            else
                                            {
                                                Query query = session.createQuery("SELECT sum(mov.cantidad) from Movimiento mov "
                                                        +" where mov.partidaExterna.pedido.idPedido="+t_pedido.getText()
                                                        +" AND mov.partidaExterna.idPartidaExterna="+t_datos.getValueAt(row, 0)
                                                        +" and mov.almacen.tipoMovimiento=1"
                                                        +" and mov.almacen.operacion=6");
                                                Object  ent = query.uniqueResult();
                                                if(ent!=null)
                                                    entrada=Double.parseDouble(ent.toString());
                                                query = session.createQuery("SELECT sum(mov.cantidad) from Movimiento mov "
                                                        +" where mov.partidaExterna.pedido.idPedido="+t_pedido.getText()
                                                        +" AND mov.partidaExterna.idPartidaExterna="+t_datos.getValueAt(row, 0)
                                                        +" and mov.almacen.tipoMovimiento=2"
                                                        +" and mov.almacen.operacion=6");
                                                Object  sal = query.uniqueResult();
                                                if(sal!=null)
                                                    salida=Double.parseDouble(sal.toString());
                                                double totalOperario=salida-entrada;
                                                if((double)value<= (double)t_datos.getValueAt(row, 5))
                                                {
                                                    vector.setElementAt(value, col);
                                                    dataVector.setElementAt(vector, row);
                                                    fireTableCellUpdated(row, col);
                                                    sumaTotales();
                                                }
                                                else
                                                {
                                                    if(totalOperario>0)
                                                        JOptionPane.showMessageDialog(null, "La cantidad máxima por devolver es: "+t_datos.getValueAt(row, 5).toString()+ t_datos.getValueAt(row, 3).toString() + " debido a que ya se entregaron al cliente " +totalOperario+ t_datos.getValueAt(row, 3).toString());
                                                    else
                                                        JOptionPane.showMessageDialog(null, "La cantidad máxima por devolver es: "+t_datos.getValueAt(row, 5).toString()+t_datos.getValueAt(row, 3).toString());
                                                }
                                            }
                                        }
                                        else
                                            JOptionPane.showMessageDialog(null, "El campo no permite números negativos"); 
                                        t_datos.setColumnSelectionInterval(col, col);
                                        t_datos.setRowSelectionInterval(row, row);
                                    }catch(Exception e)
                                    {
                                        System.out.println(e);
                                    }
                                    if(session!=null)
                                        if(session.isOpen())
                                            session.close();
                                }
                            }
                            else
                            {
                                vector.setElementAt(value, col);
                                dataVector.setElementAt(vector, row);
                                fireTableCellUpdated(row, col);
                            }
                            break;

                        case 7://Entrada operario y Salida Venta
                            if(c_toperacion.getSelectedItem().toString().compareTo("Operarios")==0 && c_tmovimiento.getSelectedItem().toString().compareTo("Entrada")==0)
                            {
                                if(vector.get(col)==null)
                                {
                                    vector.setElementAt(value, col);
                                    dataVector.setElementAt(vector, row);
                                    fireTableCellUpdated(row, col);
                                }
                                else
                                {
                                    Session session = HibernateUtil.getSessionFactory().openSession();
                                    try
                                    {
                                        if((double)value>=0)
                                        {
                                            if((double)value<=(double)t_datos.getValueAt(row, 6))
                                            {
                                                vector.setElementAt(value, col);
                                                dataVector.setElementAt(vector, row);
                                                fireTableCellUpdated(row, col);
                                            }
                                            else
                                                JOptionPane.showMessageDialog(null, "La cantidad máxima por entregar es: "+t_datos.getValueAt(row, 6).toString()+t_datos.getValueAt(row, 5).toString());
                                        }
                                        else
                                            JOptionPane.showMessageDialog(null, "El campo no permite números negativos");
                                        t_datos.setColumnSelectionInterval(col, col);
                                        t_datos.setRowSelectionInterval(row, row);
                                    }
                                    catch(Exception e)
                                    {
                                        System.out.println(e);
                                    }
                                    if(session!=null)
                                        if(session.isOpen())
                                            session.close();
                                }
                            }
                            else
                            {
                                if(c_toperacion.getSelectedItem().toString().compareTo("Venta")==0 && c_tmovimiento.getSelectedItem().toString().compareTo("Salida")==0)
                                {
                                    if((double)value>=0)
                                    {
                                        if((double)value<=(double)t_datos.getValueAt(row, 6))
                                        {
                                            vector.setElementAt(value, col);
                                            dataVector.setElementAt(vector, row);
                                            fireTableCellUpdated(row, col);
                                        }
                                        else
                                            JOptionPane.showMessageDialog(null, "La cantidad máxima por entregar es: "+t_datos.getValueAt(row, 6).toString()+t_datos.getValueAt(row, 3).toString());
                                    }
                                    else
                                        JOptionPane.showMessageDialog(null, "El campo no permite números negativos");
                                }
                                else
                                {
                                    if(c_toperacion.getSelectedItem().toString().compareTo("Inventario")==0 && cb_sin_orden.isSelected()==true)
                                    {
                                        if(vector.get(col)==null)
                                        {
                                            vector.setElementAt(value, col);
                                            dataVector.setElementAt(vector, row);
                                            fireTableCellUpdated(row, col);
                                        }
                                        else
                                        {
                                            Session session = HibernateUtil.getSessionFactory().openSession();
                                            try
                                            {
                                                if(c_tmovimiento.getSelectedItem().toString().compareTo("Salida")==0)
                                                {
                                                    if((double)value>=0)
                                                    {
                                                        if((double)value<=(double)t_datos.getValueAt(row, 6))
                                                        {
                                                            vector.setElementAt(value, col);
                                                            dataVector.setElementAt(vector, row);
                                                            fireTableCellUpdated(row, col);
                                                        }
                                                        else
                                                            JOptionPane.showMessageDialog(null, "La cantidad máxima por entregar es: "+t_datos.getValueAt(row, 6).toString()+t_datos.getValueAt(row, 5).toString());
                                                    }
                                                    else
                                                        JOptionPane.showMessageDialog(null, "El campo no permite números negativos");
                                                    t_datos.setColumnSelectionInterval(col, col);
                                                    t_datos.setRowSelectionInterval(row, row);
                                                }
                                                else
                                                {
                                                    if((double)value>=0)
                                                    {
                                                        vector.setElementAt(value, col);
                                                        dataVector.setElementAt(vector, row);
                                                        fireTableCellUpdated(row, col);
                                                    }
                                                    else
                                                        JOptionPane.showMessageDialog(null, "El campo no permite números negativos");
                                                    t_datos.setColumnSelectionInterval(col, col);
                                                    t_datos.setRowSelectionInterval(row, row);
                                                }
                                            }
                                            catch(Exception e)
                                            {
                                                System.out.println(e);
                                            }
                                            if(session!=null)
                                                if(session.isOpen())
                                                    session.close();
                                        }
                                    }
                                    else
                                    {
                                        vector.setElementAt(value, col);
                                        dataVector.setElementAt(vector, row);
                                        fireTableCellUpdated(row, col);
                                    }
                                }
                            }
                            break;

                        case 8://Pedido Interno(Ent, Sal), Pedido Adicional(Ent, Sal), Compañia(Ent, Sal) y Salida Operarios
                            if(c_toperacion.getSelectedItem().toString().compareTo("Pedido")==0 && (l_tipo_pedido.getText().compareTo("Interno")==0 || l_tipo_pedido.getText().compareTo("Adicional")==0))
                            {
                                if(vector.get(col)==null)
                                {
                                    vector.setElementAt(value, col);
                                    dataVector.setElementAt(vector, row);
                                    fireTableCellUpdated(row, col);
                                }
                                else
                                {
                                    Session session = HibernateUtil.getSessionFactory().openSession();
                                    try
                                    {
                                        if((double)value>=0)
                                        {
                                            if(c_tmovimiento.getSelectedItem().toString().compareTo("Entrada")==0)
                                            {
                                                if((double)value<=(double)t_datos.getValueAt(row, 7))
                                                {
                                                    vector.setElementAt(value, col);
                                                    dataVector.setElementAt(vector, row);
                                                    fireTableCellUpdated(row, col);
                                                    sumaTotales();
                                                }
                                                else
                                                    JOptionPane.showMessageDialog(null, "La cantidad máxima por entrar es: "+t_datos.getValueAt(row, 7)+t_datos.getValueAt(row, 5).toString()); 
                                            }
                                            else
                                            {
                                                double entrada1=0.0, salida1=0.0;
                                                Query query = session.createQuery("SELECT sum(mov.cantidad) from Movimiento mov "
                                                        +" where mov.partida.pedido.idPedido="+t_pedido.getText()
                                                        +" AND mov.partida.idPartida="+t_datos.getValueAt(row, 0)
                                                        +" and mov.almacen.tipoMovimiento=1"
                                                        +" and mov.almacen.operacion=5");
                                                Object  ent = query.uniqueResult();
                                                if(ent!=null)
                                                    entrada1=Double.parseDouble(ent.toString());
                                                query = session.createQuery("SELECT sum(mov.cantidad) from Movimiento mov "
                                                        +" where mov.partida.pedido.idPedido="+t_pedido.getText()
                                                        +" AND mov.partida.idPartida="+t_datos.getValueAt(row, 0)
                                                        +" and mov.almacen.tipoMovimiento=2"
                                                        +" and mov.almacen.operacion=5");
                                                Object  sal = query.uniqueResult();
                                                if(sal!=null)
                                                    salida1=Double.parseDouble(sal.toString());
                                                double totalOperario=salida1-entrada1;
                                                if((double)value <= (double)t_datos.getValueAt(row, 7))
                                                {
                                                    vector.setElementAt(value, col);
                                                    dataVector.setElementAt(vector, row);
                                                    fireTableCellUpdated(row, col);
                                                    sumaTotales();
                                                }
                                                else
                                                {
                                                    if(totalOperario>0)
                                                        JOptionPane.showMessageDialog(null, "La cantidad máxima por devolver es: "+t_datos.getValueAt(row, 7).toString()+ t_datos.getValueAt(row, 5).toString() + " debido a que ya se entregaron al operario " +totalOperario+ t_datos.getValueAt(row, 5).toString());
                                                    else
                                                        JOptionPane.showMessageDialog(null, "La cantidad máxima por devolver es: "+t_datos.getValueAt(row, 7).toString()+t_datos.getValueAt(row, 5).toString());
                                                }
                                            }
                                        }
                                        else
                                            JOptionPane.showMessageDialog(null, "El campo no permite números negativos"); 
                                        sumaTotales();
                                    }catch(Exception e)
                                    {
                                        System.out.println(e);
                                    }
                                    if(session!=null)
                                        if(session.isOpen())
                                            session.close();
                                }
                            }
                            else
                            {
                                if(c_toperacion.getSelectedItem().toString().compareTo("Compañía")==0)
                                {
                                    if(vector.get(col)==null)
                                    {
                                        vector.setElementAt(value, col);
                                        dataVector.setElementAt(vector, row);
                                        fireTableCellUpdated(row, col);
                                    }
                                    else
                                    {
                                        Session session = HibernateUtil.getSessionFactory().openSession();
                                        try
                                        {
                                            if((double)value>=0)
                                            {
                                                if(c_tmovimiento.getSelectedItem().toString().compareTo("Entrada")==0)
                                                {
                                                    if((double)value<=(double)t_datos.getValueAt(row, 7))
                                                    {
                                                        vector.setElementAt(value, col);
                                                        dataVector.setElementAt(vector, row);
                                                        fireTableCellUpdated(row, col);
                                                        sumaTotales();
                                                    }
                                                    else
                                                        JOptionPane.showMessageDialog(null, "La cantidad máxima por entrar es: "+t_datos.getValueAt(row, 7)+t_datos.getValueAt(row, 5).toString()); 
                                                }
                                                else
                                                {
                                                    double entrada1=0.0, salida1=0.0;
                                                    Query query = session.createQuery("SELECT sum(mov.cantidad) from Movimiento mov "
                                                            +" where mov.partida.ordenByIdOrden.idOrden="+t_orden.getText()
                                                            +" AND mov.partida.idPartida="+t_datos.getValueAt(row, 0)
                                                            +" and mov.almacen.tipoMovimiento=1"
                                                            +" and mov.almacen.operacion=5");
                                                    Object  ent = query.uniqueResult();
                                                    if(ent!=null)
                                                        entrada1=Double.parseDouble(ent.toString());
                                                    query = session.createQuery("SELECT sum(mov.cantidad) from Movimiento mov "
                                                            +" where mov.partida.ordenByIdOrden.idOrden="+t_orden.getText()
                                                            +" AND mov.partida.idPartida="+t_datos.getValueAt(row, 0)
                                                            +" and mov.almacen.tipoMovimiento=2"
                                                            +" and mov.almacen.operacion=5");
                                                    Object  sal = query.uniqueResult();
                                                    if(sal!=null)
                                                        salida1=Double.parseDouble(sal.toString());
                                                    double totalOperario=salida1-entrada1;
                                                    if((double)value<= (double)t_datos.getValueAt(row, 7))
                                                    {
                                                        vector.setElementAt(value, col);
                                                        dataVector.setElementAt(vector, row);
                                                        fireTableCellUpdated(row, col);
                                                        sumaTotales();
                                                    }
                                                    else
                                                    {
                                                        if(totalOperario>0)
                                                            JOptionPane.showMessageDialog(null, "La cantidad máxima por devolver es: "+t_datos.getValueAt(row, 7).toString()+ t_datos.getValueAt(row, 5).toString() + " debido a que ya se entregaron al operario " +totalOperario+ t_datos.getValueAt(row, 5).toString());
                                                        else
                                                            JOptionPane.showMessageDialog(null, "La cantidad máxima por devolver es: "+t_datos.getValueAt(row, 7).toString()+t_datos.getValueAt(row, 5).toString());
                                                    }
                                                }
                                            }
                                            else
                                                JOptionPane.showMessageDialog(null, "El campo no permite números negativos"); 
                                            sumaTotales();
                                        }catch(Exception e)
                                        {
                                            System.out.println(e);
                                        }
                                        if(session!=null)
                                            if(session.isOpen())
                                                session.close();
                                    }
                                }
                                else
                                {
                                    if(c_toperacion.getSelectedItem().toString().compareTo("Inventario")==0)
                                    {
                                        if(vector.get(col)==null)
                                        {
                                            vector.setElementAt(value, col);
                                            dataVector.setElementAt(vector, row);
                                            fireTableCellUpdated(row, col);
                                        }
                                        else
                                        {
                                            Session session = HibernateUtil.getSessionFactory().openSession();
                                            try
                                            {
                                                if((double)value>=0)
                                                {
                                                    if(c_tmovimiento.getSelectedItem().toString().compareTo("Entrada")==0)
                                                    {
                                                        if((double)value<=(double)t_datos.getValueAt(row, 7))
                                                        {
                                                            vector.setElementAt(value, col);
                                                            dataVector.setElementAt(vector, row);
                                                            fireTableCellUpdated(row, col);
                                                            sumaTotales();
                                                        }
                                                        else
                                                            JOptionPane.showMessageDialog(null, "La cantidad máxima por devolver es: "+t_datos.getValueAt(row, 7)); 
                                                    }
                                                    else
                                                    {
                                                        if((double)value<=(double)t_datos.getValueAt(row, 6))
                                                        {
                                                            double ref=(double)value+(double)t_datos.getValueAt(row, 7);
                                                            if((double)t_datos.getValueAt(row, 3)>=ref)
                                                            {
                                                                vector.setElementAt(value, col);
                                                                dataVector.setElementAt(vector, row);
                                                                fireTableCellUpdated(row, col);
                                                                sumaTotales();
                                                            }
                                                            else
                                                                JOptionPane.showMessageDialog(null, "La cantidad Autorizada es de "+t_datos.getValueAt(row, 3)); 
                                                        }
                                                        else
                                                            JOptionPane.showMessageDialog(null, "La cantidad máxima a entregar es: "+t_datos.getValueAt(row, 6)+t_datos.getValueAt(row, 5).toString()); 
                                                    }
                                                }
                                                else
                                                    JOptionPane.showMessageDialog(null, "El campo no permite números negativos"); 
                                                sumaTotales();
                                            }catch(Exception e)
                                            {
                                                System.out.println(e);
                                            }
                                            if(session!=null)
                                                if(session.isOpen())
                                                    session.close();
                                        }
                                    }
                                    else
                                    {
                                        vector.setElementAt(value, col);
                                        dataVector.setElementAt(vector, row);
                                        fireTableCellUpdated(row, col);
                                    }
                                }
                            }
                            break;

                        case 9:
                            if(c_toperacion.getSelectedItem().toString().compareTo("Operarios")==0 && c_tmovimiento.getSelectedItem().toString().compareTo("Salida")==0)
                            {//Operario Salida Entregados 
                                if(vector.get(col)==null)
                                {
                                    vector.setElementAt(value, col);
                                    dataVector.setElementAt(vector, row);
                                    fireTableCellUpdated(row, col);
                                }
                                else
                                {
                                    Session session = HibernateUtil.getSessionFactory().openSession();
                                    try
                                    {
                                        if((double)value>=0)
                                        {
                                            /*if(c_tmovimiento.getSelectedItem().toString().compareTo("Entrada")==0)
                                            {
                                                if((double)value<=(double)t_datos.getValueAt(row, 6))
                                                {
                                                    vector.setElementAt(value, col);
                                                    dataVector.setElementAt(vector, row);
                                                    fireTableCellUpdated(row, col);
                                                    sumaTotales();
                                                }
                                                else
                                                    JOptionPane.showMessageDialog(null, "La cantidad máxima por devolver es: "+t_datos.getValueAt(row, 6)+t_datos.getValueAt(row, 5).toString()); 
                                            }
                                            else
                                            {*/
                                                if(((double)value + (double)t_datos.getValueAt(row, 8))<=(double)t_datos.getValueAt(row, 6))
                                                {
                                                    if((double)value<=(double)t_datos.getValueAt(row, 7))
                                                    {
                                                        vector.setElementAt(value, col);
                                                        dataVector.setElementAt(vector, row);
                                                        fireTableCellUpdated(row, col);
                                                        sumaTotales();
                                                    }
                                                    else
                                                        JOptionPane.showMessageDialog(null, "No hay suficientes existencias para entregar esa cantidas"); 
                                                }
                                                else
                                                {
                                                    double cant=((double)t_datos.getValueAt(row, 6) - (double)t_datos.getValueAt(row, 8));
                                                    JOptionPane.showMessageDialog(null, "La cantidad máxima a entregar es: "+cant+t_datos.getValueAt(row, 5).toString()); 
                                                }
                                            //}
                                        }
                                        else
                                            JOptionPane.showMessageDialog(null, "El campo no permite números negativos"); 
                                        sumaTotales();
                                    }catch(Exception e)
                                    {
                                        System.out.println(e);
                                    }
                                    if(session!=null)
                                        if(session.isOpen())
                                            session.close();
                                }
                            }
                            else
                            {
                                vector.setElementAt(value, col);
                                dataVector.setElementAt(vector, row);
                                fireTableCellUpdated(row, col);
                            }
                            break;
                            
                     case 10:
                            if(c_toperacion.getSelectedItem().toString().compareTo("Operarios")==0 && c_tmovimiento.getSelectedItem().toString().compareTo("Salida")==0)
                            {//Operario Salida Entregados 
                                if(vector.get(col)==null)
                                {
                                    vector.setElementAt(value, col);
                                    dataVector.setElementAt(vector, row);
                                    fireTableCellUpdated(row, col);
                                }
                                else
                                {
                                    usrAut=null;
                                    Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
                                    autoriza.setSize(318, 200);
                                    autoriza.setLocation((d.width/2)-(autoriza.getWidth()/2), (d.height/2)-(autoriza.getHeight()/2));
                                    t_user.setText("");
                                    t_contra.setText("");
                                    autoriza.setVisible(true);
                                    if(usrAut!=null)
                                    {
                                        Session session = HibernateUtil.getSessionFactory().openSession();
                                        session.beginTransaction().begin();
                                        try
                                        {
                                            String op_compra=t_datos.getValueAt(row, 2).toString();
                                            switch(op_compra)
                                            {
                                                case "ALM.":
                                                    usrAut=(Usuario)session.get(Usuario.class, usrAut.getIdUsuario());
                                                    Partida par=(Partida)session.get(Partida.class, Integer.parseInt(t_datos.getValueAt(row, 0).toString()));
                                                    par.setOp((boolean)value);
                                                    par.setMecanico(usrAut);
                                                    session.update(par);
                                                    session.beginTransaction().commit();
                                                    vector.setElementAt(value, col);
                                                    dataVector.setElementAt(vector, row);
                                                    fireTableCellUpdated(row, col);
                                                    break;
                                                case "PED.":
                                                    usrAut=(Usuario)session.get(Usuario.class, usrAut.getIdUsuario());
                                                    Partida par1=(Partida)session.get(Partida.class, Integer.parseInt(t_datos.getValueAt(row, 0).toString()));
                                                    par1.setOp((boolean)value);
                                                    par1.setMecanico(usrAut);
                                                    session.update(par1);
                                                    session.beginTransaction().commit();
                                                    vector.setElementAt(value, col);
                                                    dataVector.setElementAt(vector, row);
                                                    fireTableCellUpdated(row, col);
                                                    break;
                                                case "COM.":
                                                    usrAut=(Usuario)session.get(Usuario.class, usrAut.getIdUsuario());
                                                    Partida par0=(Partida)session.get(Partida.class, Integer.parseInt(t_datos.getValueAt(row, 0).toString()));
                                                    par0.setOp((boolean)value);
                                                    par0.setMecanico(usrAut);
                                                    session.update(par0);
                                                    session.beginTransaction().commit();
                                                    vector.setElementAt(value, col);
                                                    dataVector.setElementAt(vector, row);
                                                    fireTableCellUpdated(row, col);
                                                    break;
                                                case "ADI.":
                                                    usrAut=(Usuario)session.get(Usuario.class, usrAut.getIdUsuario());
                                                    PartidaExterna par2=(PartidaExterna)session.get(PartidaExterna.class, Integer.parseInt(t_datos.getValueAt(row, 0).toString()));
                                                    par2.setOp((boolean)value);
                                                    par2.setMecanico(usrAut);
                                                    session.update(par2);
                                                    session.beginTransaction().commit();
                                                    vector.setElementAt(value, col);
                                                    dataVector.setElementAt(vector, row);
                                                    fireTableCellUpdated(row, col);
                                                    break;
                                            }   
                                        }catch(Exception e)
                                        {
                                            e.printStackTrace();
                                            JOptionPane.showMessageDialog(null, "Error al actualizar la partida"); 
                                        }
                                        if(session!=null)
                                            if(session.isOpen())
                                                session.close();
                                    }
                                }
                            }
                            else
                            {
                                vector.setElementAt(value, col);
                                dataVector.setElementAt(vector, row);
                                fireTableCellUpdated(row, col);
                            }
                            break;
                    default:
                        vector.setElementAt(value, col);
                        dataVector.setElementAt(vector, row);
                        fireTableCellUpdated(row, col);
                        break;
                }
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
            ren=ren-1;
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
                aux.add(false);
            celdaEditable.add(ren, aux);
            fireTableRowsInserted(row, row);
            ren=ren+1;
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
    
    private int buscapartida(List partida)
    {
        int x=-1;
        for(int ren=0; ren<t_datos.getRowCount(); ren++)
        {
            if(t_datos.getValueAt(ren, 0).toString().compareTo(partida.get(0).toString())==0)
                x=ren;
        }
        return x;
    }
    private int buscaEjemplar(String partida)
    {
        int x=-1;
        for(int ren=0; ren<t_datos.getRowCount(); ren++)
        {
            if(t_datos.getValueAt(ren, 0).toString().compareTo(partida)==0)
                x=ren;
        }
        return x;
    }
    
    private void limpiar_tabla(){
        for (int i = 0; i < t_datos.getRowCount(); i++) 
        {
            model.removeRow(i);
            i-=1;
        }
        //if(t_orden.getText().compareTo("")==0)
        //{
            b_mas.setEnabled(true);
            b_menos.setEnabled(true);
        //}
        formatoTabla();
    }
    
    private void borra_cajas()
    {
        if (c_tmovimiento.getSelectedItem().toString().compareTo("Entrada")==0)
            buscar.setText("Entrego");
        else
            buscar.setText("Recibio");
        t_er.setText("");
        t_notas.setText("");
        t_fecha.setText("DD/MM/AAAA");
        t_tipo.setText("");
        t_marca.setText("");
        t_modelo.setText("");
        t_serie.setText("");
        t_compania.setText("");
        t_siniestro.setText("");
        t_asegurado.setText("");
        t_orden.setText("");
        t_pedido.setText("");
        t_nmovimiento.setText("");
        t_folio.setText("");
        t_nreferencia.setText("");
        this.l_tipo_pedido.setText("");
    }
    
    private void tabla_tamaños()
    {
        TableColumnModel col_model = t_datos.getColumnModel();
        DefaultTableCellRenderer tcr = new DefaultTableCellRenderer();
        tcr.setHorizontalAlignment(SwingConstants.RIGHT);
        FormatoEditor fe=new FormatoEditor();
        t_datos.setDefaultEditor(Double.class, fe);
        for (int i=0; i<t_datos.getColumnCount(); i++)
        {
            TableColumn column = col_model.getColumn(i);
            switch(i)
            {
                case 0:
                    column.setPreferredWidth(10);
                    break;
                case 1:
                    if( (c_toperacion.getSelectedItem().toString().compareTo("Pedido")==0 && (l_tipo_pedido.getText().compareTo("Externo")==0 || l_tipo_pedido.getText().compareTo("Inventario")==0) ) || (c_toperacion.getSelectedItem().toString().compareTo("Venta")==0) )
                        column.setPreferredWidth(100);
                    else
                        column.setPreferredWidth(10);
                    break;
                case 2:
                    if( (c_toperacion.getSelectedItem().toString().compareTo("Pedido")==0 && (l_tipo_pedido.getText().compareTo("Externo")==0 || l_tipo_pedido.getText().compareTo("Inventario")==0) ) || (c_toperacion.getSelectedItem().toString().compareTo("Venta")==0) )
                        column.setPreferredWidth(350);
                    else
                        column.setPreferredWidth(10);
                    break;
                case 3:
                    if( (c_toperacion.getSelectedItem().toString().compareTo("Pedido")==0 && (l_tipo_pedido.getText().compareTo("Externo")==0 || l_tipo_pedido.getText().compareTo("Inventario")==0) ) || (c_toperacion.getSelectedItem().toString().compareTo("Venta")==0) )
                        column.setPreferredWidth(20);
                    else
                    {
                        column.setPreferredWidth(100);
                        if(c_toperacion.getSelectedItem().toString().compareTo("Pedido")==0 && (l_tipo_pedido.getText().compareTo("Externo")==0 || l_tipo_pedido.getText().compareTo("Inventario")==0) )
                        {
                            //DefaultCellEditor miEditor = new DefaultCellEditor(codigo);
                            //miEditor.setClickCountToStart(2);
                            //column.setCellEditor(miEditor); 
                        }
                    }
                    break;
                case 4:               
                    if( (c_toperacion.getSelectedItem().toString().compareTo("Pedido")==0 && (l_tipo_pedido.getText().compareTo("Externo")==0 || l_tipo_pedido.getText().compareTo("Inventario")==0) ) || (c_toperacion.getSelectedItem().toString().compareTo("Venta")==0) )
                        column.setPreferredWidth(50);
                    else
                        column.setPreferredWidth(350);
                    break;
                case 5:               
                    column.setPreferredWidth(50);
                    break;
                case 6:               
                    column.setPreferredWidth(50);
                    break;
                case 7:               
                    column.setPreferredWidth(50);
                    break;
                case 8:               
                    column.setPreferredWidth(50);
                    break;
                default:
                    column.setPreferredWidth(50);
                    break; 
            }
        }
        JTableHeader header = t_datos.getTableHeader();
        header.setBackground(new java.awt.Color(90,66,126));
        header.setForeground(Color.white);
        sumaTotales();
    }        
    
    private boolean consultaLista(int columna)
    {
        for(int ren=0; ren<t_datos.getRowCount(); ren++)
        {
            if((double)t_datos.getValueAt(ren, columna)==0.00)
                return false;
        }
        return true;
    } 
    
    private boolean autorizaOperarios()
    {
        for(int ren=0; ren<t_datos.getRowCount(); ren++)
        {
            if((boolean)t_datos.getValueAt(ren, 10)==false)
            {
                JOptionPane.showMessageDialog(this, "La partida "+t_datos.getValueAt(ren, 1).toString()+" no esta autorizada por operaciones para ser entregada aun");
                return false;
            }
        }
        return true;
    }
    
    private Integer guardarAlmacen(Almacen obj) 
    {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Integer IdAlmacen = null;
        try 
        {
            session.beginTransaction().begin();
            
            if(obj.getTipoMovimiento()==2 && (obj.getOperacion()==1 || obj.getOperacion()==2 || obj.getOperacion()==3|| obj.getOperacion()==7))
            {
                XCobrar xp = (XCobrar)session.createCriteria(XCobrar.class).createAlias("reclamo", "rec").add(Restrictions.eq("idPedido", Integer.parseInt(t_pedido.getText()))).add(Restrictions.in("rec.estatus", new String[]{"a","P"})).setMaxResults(1).uniqueResult();
                if(xp==null)
                {
                    pedido =(Pedido)session.get(Pedido.class, Integer.parseInt(t_pedido.getText()));
                    obj.setPedido(pedido);
                    obj.setMovimientos(new HashSet(0));
                    IdAlmacen=(Integer) session.save(obj);
                    Almacen alm = (Almacen)session.get(Almacen.class, IdAlmacen);
                    for(int ren=0; ren<t_datos.getRowCount(); ren++)
                    {
                        Movimiento mov = new Movimiento();
                        mov.setAlmacen(alm);
                        if(c_toperacion.getSelectedItem().toString().compareTo("Pedido")==0)
                        {
                            if(l_tipo_pedido.getText().compareTo("Interno")==0 || l_tipo_pedido.getText().compareTo("Adicional")==0)
                                mov.setCantidad((double)t_datos.getValueAt(ren, 8));
                            else
                                mov.setCantidad((double)t_datos.getValueAt(ren, 6));
                        }
                        if(c_toperacion.getSelectedItem().toString().compareTo("Venta")==0)
                        {
                            if(c_tmovimiento.getSelectedItem().toString().compareTo("Entrada")==0)
                                mov.setCantidad((double)t_datos.getValueAt(ren, 5));
                            else
                                mov.setCantidad((double)t_datos.getValueAt(ren, 7));
                        }
                        if(l_tipo_pedido.getText().compareTo("Interno")==0)// || l_tipo_pedido.getText().compareTo("Adicional")==0)
                        {
                            Partida part=(Partida)session.get(Partida.class, (Integer)t_datos.getValueAt(ren, 0));
                            mov.setPartida(part);
                        }
                        else
                        {
                            PartidaExterna part=(PartidaExterna)session.get(PartidaExterna.class, (Integer)t_datos.getValueAt(ren, 0));
                            if(l_tipo_pedido.getText().compareTo("Inventario")==0)
                            {
                                Ejemplar ejemplar=part.getEjemplar();
                                if(c_tmovimiento.getSelectedItem().toString().compareToIgnoreCase("Entrada")==0)
                                {
                                    if(cb_almacen.getSelectedIndex()==0)
                                        ejemplar.setExistencias(ejemplar.getExistencias()+mov.getCantidad());
                                    else
                                        ejemplar.setExistencias2(ejemplar.getExistencias2()+mov.getCantidad());
                                }
                                if(c_tmovimiento.getSelectedItem().toString().compareToIgnoreCase("Salida")==0)
                                {
                                    if(cb_almacen.getSelectedIndex()==0)
                                        ejemplar.setExistencias(ejemplar.getExistencias()-mov.getCantidad());
                                    else
                                        ejemplar.setExistencias2(ejemplar.getExistencias2()-mov.getCantidad());
                                }
                                session.update(ejemplar);
                            }
                            mov.setPartidaExterna(part);
                        }

                        alm.addMovimiento(mov);
                    }
                    session.update(alm);
                    session.beginTransaction().commit();
                    t_nmovimiento.setText(alm.getIdAlmacen().toString());
                    t_fecha.setText(alm.getFecha().toLocaleString());
                    t_notas.setText(alm.getNotas());
                    t_pedido.setText(alm.getPedido().getIdPedido().toString());
                }
                else
                {
                    IdAlmacen = null;
                    session.beginTransaction().rollback();
                    JOptionPane.showMessageDialog(null, "El pedido "+t_pedido.getText()+" ya esta en proceso de cobro N° Reclamo:"+xp.getReclamo().getIdReclamo());
                }
            }
            else
            {
                pedido =(Pedido)session.get(Pedido.class, Integer.parseInt(t_pedido.getText()));
                obj.setPedido(pedido);
                obj.setMovimientos(new HashSet(0));
                IdAlmacen=(Integer) session.save(obj);
                Almacen alm = (Almacen)session.get(Almacen.class, IdAlmacen);
                for(int ren=0; ren<t_datos.getRowCount(); ren++)
                {
                    Movimiento mov = new Movimiento();
                    mov.setAlmacen(alm);
                    if(c_toperacion.getSelectedItem().toString().compareTo("Pedido")==0)
                    {
                        if(l_tipo_pedido.getText().compareTo("Interno")==0 || l_tipo_pedido.getText().compareTo("Adicional")==0)
                            mov.setCantidad((double)t_datos.getValueAt(ren, 8));
                        else
                            mov.setCantidad((double)t_datos.getValueAt(ren, 6));
                    }
                    if(c_toperacion.getSelectedItem().toString().compareTo("Venta")==0)
                    {
                        if(c_tmovimiento.getSelectedItem().toString().compareTo("Entrada")==0)
                            mov.setCantidad((double)t_datos.getValueAt(ren, 5));
                        else
                            mov.setCantidad((double)t_datos.getValueAt(ren, 7));
                    }
                    if(l_tipo_pedido.getText().compareTo("Interno")==0)// || l_tipo_pedido.getText().compareTo("Adicional")==0)
                    {
                        Partida part=(Partida)session.get(Partida.class, (Integer)t_datos.getValueAt(ren, 0));
                        mov.setPartida(part);
                    }
                    else
                    {
                        PartidaExterna part=(PartidaExterna)session.get(PartidaExterna.class, (Integer)t_datos.getValueAt(ren, 0));
                        if(l_tipo_pedido.getText().compareTo("Inventario")==0)
                        {
                            Ejemplar ejemplar=part.getEjemplar();
                            if(c_tmovimiento.getSelectedItem().toString().compareToIgnoreCase("Entrada")==0)
                            {
                                if(cb_almacen.getSelectedIndex()==0)
                                    ejemplar.setExistencias(ejemplar.getExistencias()+mov.getCantidad());
                                else
                                    ejemplar.setExistencias2(ejemplar.getExistencias2()+mov.getCantidad());
                            }
                            if(c_tmovimiento.getSelectedItem().toString().compareToIgnoreCase("Salida")==0)
                            {
                                if(cb_almacen.getSelectedIndex()==0)
                                    ejemplar.setExistencias(ejemplar.getExistencias()-mov.getCantidad());
                                else
                                    ejemplar.setExistencias2(ejemplar.getExistencias2()-mov.getCantidad());
                            }
                            session.update(ejemplar);
                        }
                        mov.setPartidaExterna(part);
                    }

                    alm.addMovimiento(mov);
                }
                session.update(alm);
                session.beginTransaction().commit();
                int tipo=alm.getTipoMovimiento();
                int operacion= alm.getOperacion();
                if( (operacion==1 || operacion==3 || operacion==4) && tipo==1)
                {
                    String mensaje="<p>Confirmaci&oacute;n de material recibido en almacen <strong>No de pedido "+t_pedido.getText()+"</strong></p>"+
                                    "<p>Lista de material</p>"+
                                    "<table width='100%' border='1' cellspacing='0' cellpadding='0'>"+
                                      "<tr>"+
                                        "<td width='7%'>No</td>"+
                                        "<td width='8%'>#</td>"+
                                        "<td width='21%'>No&deg; Parte </td>"+
                                        "<td width='45%'>Descripcion</td>"+
                                        "<td width='7%'>Medida</td>"+
                                        "<td width='12%'>Cantidad</td>"+
                                      "</tr>";
                    for(int x=0; x<t_datos.getRowCount(); x++)
                    {
                        mensaje+="<tr>"+
                                        "<td>"+t_datos.getValueAt(x, 1).toString()+"</td>"+
                                        "<td>"+t_datos.getValueAt(x, 2).toString()+"</td>"+
                                        "<td>"+t_datos.getValueAt(x, 3).toString()+"</td>"+
                                        "<td>"+t_datos.getValueAt(x, 4).toString()+"</td>"+
                                        "<td>"+t_datos.getValueAt(x, 5).toString()+"</td>"+
                                        "<td>"+t_datos.getValueAt(x, 8).toString()+"</td>"+
                                      "</tr>";
                    }
                    mensaje+="</table><p>"+t_notas.getText()+"</p> <p>Saludos. </p>";
                    Configuracion con = (Configuracion)session.get(Configuracion.class, 1);
                    switch(con.getRfc())
                    {
                        case "XTR190129S67":
                        enviaCorreo("Recepción de material en Xell, OT("+t_orden.getText()+")", mensaje, "servicio.especializado.toluca@gmail.com;omarortega@xelltrucks.com;juanguzman@xelltrucks.com"); 
                        break;
                        
                        case "SET0806255W2":
                        enviaCorreo("Recepción de material en Sucursal, OT("+t_orden.getText()+")", mensaje, "angeldavila@tractoservicio.com;servicio.especializado.toluca@gmail.com"); 
                        break;
                            
                        case "TBS160622L39":
                        enviaCorreo("Recepción de material en Matriz, OT("+t_orden.getText()+")", mensaje, "jorgerios@tractoservicio.com;edwinaldair@tractoservicio.com;luiscarrillo@tractoservicio.com;servicio.especializado.toluca@gmail.com;octavio.castaneda@tractoservicio.com"); 
                        break;
                            
                    }
                }
                t_nmovimiento.setText(alm.getIdAlmacen().toString());
                t_fecha.setText(alm.getFecha().toLocaleString());
                t_notas.setText(alm.getNotas());
                t_pedido.setText(alm.getPedido().getIdPedido().toString());
            }
        } 
        catch (HibernateException he) 
        {
            he.printStackTrace();
            session.getTransaction().rollback();
            JOptionPane.showMessageDialog(null, "¡Error al guardar!"); 
            IdAlmacen = null;
        }
        finally
        {
            if(session!=null)
                if(session.isOpen())
                    session.close();
            return IdAlmacen;
        }   
    }    
    
    private Integer guardarAlmacenOrden(Almacen obj) 
    {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Integer IdAlmacen = null;
        try 
        {
            session.beginTransaction().begin();
            if(t_orden.getText().compareTo("")!=0)
            {
                orden_act =(Orden)session.get(Orden.class, Integer.parseInt(t_orden.getText()));
                obj.setOrden(orden_act);   
            }
            obj.setMovimientos(new HashSet(0));
            IdAlmacen=(Integer) session.save(obj);
            Almacen alm = (Almacen)session.get(Almacen.class, IdAlmacen);
            String msg="";
            for(int ren=0; ren<t_datos.getRowCount(); ren++)
            {
                Partida part=null;
                PartidaExterna parEx=null;
                Ejemplar ejem = null;
                Movimiento mov = new Movimiento();
                mov.setAlmacen(alm);
                switch(c_toperacion.getSelectedItem().toString())
                {
                    case "Compañía":
                        mov.setCantidad((double)t_datos.getValueAt(ren, 8));
                        part=(Partida)session.createCriteria(Partida.class).add(Restrictions.eq("ordenByIdOrden.idOrden", Integer.parseInt(t_orden.getText()))).add(Restrictions.eq("idPartida", Integer.parseInt(t_datos.getValueAt(ren, 0).toString()))).setMaxResults(1).uniqueResult();
                        break;
                        
                    case "Operarios":
                        if(c_tmovimiento.getSelectedItem().toString().compareTo("Entrada")==0)
                            mov.setCantidad((double)t_datos.getValueAt(ren, 7));
                        else
                            mov.setCantidad((double)t_datos.getValueAt(ren, 9));
                        
                        if(t_datos.getValueAt(ren, 2).toString().compareTo("PED.")==0 || t_datos.getValueAt(ren, 2).toString().compareTo("COM.")==0 || t_datos.getValueAt(ren, 2).toString().compareTo("ALM.")==0)
                            part=(Partida)session.createCriteria(Partida.class).add(Restrictions.eq("ordenByIdOrden.idOrden", Integer.parseInt(t_orden.getText()))).add(Restrictions.eq("idPartida", Integer.parseInt(t_datos.getValueAt(ren, 0).toString()))).setMaxResults(1).uniqueResult();
                        if(t_datos.getValueAt(ren, 2).toString().compareTo("ADI.")==0)
                            parEx=(PartidaExterna)session.get(PartidaExterna.class, Integer.parseInt(t_datos.getValueAt(ren, 0).toString()));
                        break;
                        
                    case "Inventario":
                        ejem=(Ejemplar)session.get(Ejemplar.class, t_datos.getValueAt(ren, 0).toString());
                        if(t_orden.getText().compareTo("")!=0)
                        {
                            if(c_tmovimiento.getSelectedItem().toString().compareTo("Salida")==0)
                            {
                                if(cb_almacen.getSelectedIndex()==0)
                                {
                                    if(ejem.getExistencias()>=(double)t_datos.getValueAt(ren, 8))
                                        mov.setCantidad((double)t_datos.getValueAt(ren, 8));
                                    else
                                    {
                                        ren=t_datos.getRowCount();
                                        msg="Solo hay "+ejem.getExistencias()+" existencias y no son suficientes para surtir el pedido";
                                    }
                                }
                                else
                                {
                                    if(ejem.getExistencias2()>=(double)t_datos.getValueAt(ren, 8))
                                        mov.setCantidad((double)t_datos.getValueAt(ren, 8));
                                    else
                                    {
                                        ren=t_datos.getRowCount();
                                        msg="Solo hay "+ejem.getExistencias2()+" existencias en Almacen 2, y no son suficientes para surtir el pedido";
                                    }
                                }
                            }
                            else
                            {
                                mov.setCantidad((double)t_datos.getValueAt(ren, 8));
                            }
                        }
                        else
                        {
                            mov.setCantidad((double)t_datos.getValueAt(ren, 7));
                        }
                        if(c_tmovimiento.getSelectedItem().toString().compareTo("Salida")==0)
                        {
                            if(cb_almacen.getSelectedIndex()==0)
                                ejem.setExistencias(ejem.getExistencias()-mov.getCantidad());
                            else
                                ejem.setExistencias2(ejem.getExistencias2()-mov.getCantidad());
                        }
                        else
                        {
                            if(cb_almacen.getSelectedIndex()==0)
                                ejem.setExistencias(ejem.getExistencias()+mov.getCantidad());
                            else
                                ejem.setExistencias2(ejem.getExistencias2()+mov.getCantidad());
                        }
                        session.update(ejem);
                        mov.setValor(ejem.getPrecio());
                        break;
                        
                    case "pedido":
                        mov.setCantidad((double)t_datos.getValueAt(ren, 8));
                        break;
                    case "Venta":
                        if(c_tmovimiento.getSelectedItem().toString().compareTo("Salida")==0)
                            mov.setCantidad((double)t_datos.getValueAt(ren, 7));
                        else
                            mov.setCantidad((double)t_datos.getValueAt(ren, 5));
                        break;
                }
                if(msg.compareTo("")==0)
                {
                    if(part!=null)
                    {
                       if(alm.getOperacion()==5 && part.isSurteAlmacen()==true)
                       {
                            Ejemplar aux=(Ejemplar)session.get(Ejemplar.class, part.getEjemplar().getIdParte());
                            if(alm.getTipoMovimiento()==1)//entrada
                                aux.setExistencias(aux.getExistencias()+mov.getCantidad());
                            else
                                aux.setExistencias(aux.getExistencias()-mov.getCantidad());
                            mov.setValor(aux.getPrecio());
                            session.update(aux);
                        }
                       /*part.setOp(false);
                       part.setMecanico(null);
                       part.setFechaMecanico(null);*/
                       //session.update(part);
                    }
                    mov.setPartida(part);
                    mov.setPartidaExterna(parEx);
                    mov.setEjemplar(ejem);
                    alm.addMovimiento(mov);
                }
                else
                {
                    session.getTransaction().rollback();
                    JOptionPane.showMessageDialog(null, msg); 
                    IdAlmacen = null;
                }
            }
            alm.setNotas(t_notas.getText());
            session.update(alm);
            session.beginTransaction().commit();
            t_nmovimiento.setText(alm.getIdAlmacen().toString());
            t_fecha.setText(alm.getFecha().toLocaleString());
            t_notas.setText(alm.getNotas());
        } 
        catch (Exception he) 
        {
            he.printStackTrace();
            session.getTransaction().rollback();
            JOptionPane.showMessageDialog(null, "¡Error al guardar!"); 
            IdAlmacen = null;
        }
        finally
        {
            if(session!=null)
                if(session.isOpen())
                    session.close();
            return IdAlmacen;
        }
    }  
    
    public void sumaTotales()
    {
        double subtotal=0.0d, iva1;        
        if(c_toperacion.getSelectedItem().toString().compareTo("Pedido")==0)
        {
            if(l_tipo_pedido.getText().compareTo("Interno")==0 || l_tipo_pedido.getText().compareTo("Adicional")==0)
            {
                for(int ren=0; ren<t_datos.getRowCount(); ren++)
                {
                    double multi= Double.parseDouble(String.valueOf(t_datos.getValueAt(ren,8)))*Double.parseDouble(String.valueOf(t_datos.getValueAt(ren,9)));
                    t_datos.setValueAt(multi,ren,10);
                    subtotal+=multi;
                }
            }
            if(l_tipo_pedido.getText().compareTo("Externo")==0 || l_tipo_pedido.getText().compareTo("Inventario")==0)
            {
                for(int ren=0; ren<t_datos.getRowCount(); ren++)
                {
                    double multi= Double.parseDouble(String.valueOf(t_datos.getValueAt(ren,6)))*Double.parseDouble(String.valueOf(t_datos.getValueAt(ren,7)));
                    t_datos.setValueAt(multi,ren,8);
                    subtotal+=multi;
                }
            }
            
        }
        
        if(c_toperacion.getSelectedItem().toString().compareTo("Inventario")==0 && t_orden.getText().compareTo("")!=0)
        {
            for(int ren=0; ren<t_datos.getRowCount(); ren++)
            {
                double multi= Double.parseDouble(String.valueOf(t_datos.getValueAt(ren,8)))*Double.parseDouble(String.valueOf(t_datos.getValueAt(ren,9)));
                t_datos.setValueAt(multi,ren,10);
                subtotal+=multi;
            }
        }
        t_subtotal.setValue(subtotal);
        t_IVA.setValue(iva1=subtotal*iva/100);
        t_total.setValue(subtotal+iva1);
    }
    
    public void operacion(boolean buscap, boolean nreferencia, boolean buscao, boolean folio, boolean lsubtotal, boolean tsubtotal, boolean liva, boolean tiva, boolean ltotal, boolean ttotal, boolean bbusca)
    {
        if(c_toperacion.getSelectedItem().toString().compareTo("Inventario")==0)
        {
            cb_acondicionamiento.setVisible(true);
            cb_sin_orden.setVisible(true);
            cb_sin_orden.setSelected(false);
            cb_especialidad.setVisible(true);
            cb_1.setVisible(true);
        }
        else
        {
            cb_acondicionamiento.setVisible(false);
            cb_sin_orden.setVisible(false);
            cb_sin_orden.setSelected(false);
            cb_especialidad.setVisible(false);
            cb_1.setVisible(false);
        }
        
        b_buscapedido.setEnabled(buscap);
        t_nreferencia.setEditable(nreferencia);
        b_buscaorden.setEnabled(buscao); 
        buscar.setEnabled(bbusca);
        if(c_toperacion.getSelectedItem().toString().compareTo("Operarios")==0)
        {
            
            f1.setEnabled(buscao);
            f2.setEnabled(buscao);
            f3.setEnabled(buscao);
            f4.setEnabled(buscao);
            f5.setEnabled(buscao);
            f5.setSelected(true);
        }
        else
        {
            f1.setEnabled(false);
            f2.setEnabled(false);
            f3.setEnabled(false);
            f4.setEnabled(false);
            f5.setEnabled(false);
            f5.setSelected(true);
        }
            
        t_folio.setEnabled(folio);
        r1.setEnabled(folio);
        r2.setEnabled(folio);
        l_subtotal.setVisible(lsubtotal);
        t_subtotal.setVisible(tsubtotal);
        l_iva.setVisible(liva);
        t_IVA.setVisible(tiva);
        l_total.setVisible(ltotal);
        t_total.setVisible(ttotal);
    }
    public void titulos()
    {
        h= new Herramientas(usr, menu);
        h.desbloqueaOrden();
        h.desbloqueaPedido();
        orden_act=null;
        id_empleado="";
        id_trabajo="";
        
        t_datos.setModel(model);
        formatoTabla();
        borra_cajas();
        limpiar_tabla();
        sumaTotales();
        cb_especialidad.setSelectedIndex(0);
        cb_1.setSelectedIndex(0);
        t_er.setText("");
        
        if(c_toperacion.getSelectedItem().toString().compareTo("Pedido")==0 && c_tmovimiento.getSelectedItem().toString().compareTo("Entrada")==0)
        {
            miTitulo="ENTRADA DE MATERIAL DE PEDIDO";
            jButton1.setBackground(Color.RED);
            t_er.setEnabled(true);
            operacion(true, true, false, true, true, true, true, true, true, true, false);
        }
        else
            jButton1.setBackground(new java.awt.Color(51, 51, 255));

        if(c_toperacion.getSelectedItem().toString().compareTo("Compañía")==0 && c_tmovimiento.getSelectedItem().toString().compareTo("Entrada")==0)
        {
            miTitulo="ENTRADA DE MATERIAL DE LA COMPAÑIA";
            jButton3.setBackground(Color.RED);
            t_er.setEnabled(true);
            operacion(false, true, true, true, false, false, false, false, false, false, false);
        }
        else
            jButton3.setBackground(new java.awt.Color(51, 51, 255));

        if(c_toperacion.getSelectedItem().toString().compareTo("Operarios")==0 && c_tmovimiento.getSelectedItem().toString().compareTo("Entrada")==0)
        {
            miTitulo="DEVOLUCION DE MATERIAL OPERARIOS";
            jButton6.setBackground(Color.RED);
            t_er.setEnabled(true);
            operacion(false, false, true, false, false, false, false, false, false, false, true);
        }
        else
            jButton6.setBackground(new java.awt.Color(51, 51, 255));

        if(c_toperacion.getSelectedItem().toString().compareTo("Venta")==0 && c_tmovimiento.getSelectedItem().toString().compareTo("Entrada")==0)
        {
            miTitulo="DEVOLUCION DE MATERIAL SALIDA EXTERNA";
            jButton8.setBackground(Color.RED);
            t_er.setEnabled(true);
            operacion(true, true, false, true, true, true, true, true, true, true, false);
        }
        else
            jButton8.setBackground(new java.awt.Color(51, 51, 255));

        if(c_toperacion.getSelectedItem().toString().compareTo("Inventario")==0 && c_tmovimiento.getSelectedItem().toString().compareTo("Entrada")==0)
        {
            miTitulo="DEVOLUCION DE CONSUMIBLES";
            jButton10.setBackground(Color.RED);
            t_er.setEnabled(false);
            operacion(false, false, true, false, true, true, true, true, true, true, true);
        }
        else
            jButton10.setBackground(new java.awt.Color(51, 51, 255));

        if(c_toperacion.getSelectedItem().toString().compareTo("Pedido")==0 && c_tmovimiento.getSelectedItem().toString().compareTo("Salida")==0)
        {
            miTitulo="DEVOLUCION DE MATERIAL DE PEDIDO";
            jButton2.setBackground(Color.RED);
            t_er.setEnabled(true);
            operacion(true, true, false, true, true, true, true, true, true, true, false);
        }
        else
            jButton2.setBackground(new java.awt.Color(51, 51, 255));

        if(c_toperacion.getSelectedItem().toString().compareTo("Compañía")==0 && c_tmovimiento.getSelectedItem().toString().compareTo("Salida")==0)
        {
            miTitulo="DEVOLUCION DE MATERIAL A LA COMPAÑIA";
            jButton4.setBackground(Color.RED);
            t_er.setEnabled(true);
            operacion(false, true, true, true, false, false, false, false, false, false, false);
        }
        else
            jButton4.setBackground(new java.awt.Color(51, 51, 255));

        if(c_toperacion.getSelectedItem().toString().compareTo("Operarios")==0 && c_tmovimiento.getSelectedItem().toString().compareTo("Salida")==0)
        {
            miTitulo="ENTREGA DE MATERIAL A OPERARIOS";
            jButton5.setBackground(Color.RED);
            t_er.setEnabled(true);
            operacion(false, false, true, false, false, false, false, false, false, false, true);
        }
        else
            jButton5.setBackground(new java.awt.Color(51, 51, 255));

        if(c_toperacion.getSelectedItem().toString().compareTo("Venta")==0 && c_tmovimiento.getSelectedItem().toString().compareTo("Salida")==0)
        {
            miTitulo="SALIDA DE MATERIAL EXTERNO";
            jButton7.setBackground(Color.RED);
            t_er.setEnabled(true);
            operacion(true, true, false, true, true, true, true, true, true, true, false);
        }
        else
            jButton7.setBackground(new java.awt.Color(51, 51, 255));    

        if(c_toperacion.getSelectedItem().toString().compareTo("Inventario")==0 && c_tmovimiento.getSelectedItem().toString().compareTo("Salida")==0)
        {
            miTitulo="SALIDA DE CONSUMIBLES";
            jButton11.setBackground(Color.RED);
            t_er.setEnabled(false);
            operacion(false, false, true, false, true, true, true, true, true, true, true);
        }
        else
            jButton11.setBackground(new java.awt.Color(51, 51, 255));
        jPanelMalmacen.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), miTitulo, javax.swing.border.TitledBorder.RIGHT, javax.swing.border.TitledBorder.ABOVE_TOP));
    }
    
    public void enviaCorreo(String asunto, String mensaje, String from)
    {
        String smtp="";
        boolean ttl=false;
        String puerto="";
        String envia="";
        String clave="";
        //String from="";
        String cc="";
        String texto = null;
        
        try
        {
            FileReader f = new FileReader("correo.ml");
            BufferedReader b = new BufferedReader(f);
            int renglon=0;
            while((texto = b.readLine())!=null)
            {
                switch(renglon)
                {
                    case 1://smtp
                        smtp=texto.trim();
                        break;
                    case 2://ttl
                        if(texto.compareToIgnoreCase("true")==0)
                            ttl=true;
                        else
                            ttl=false;
                        break;
                    case 3://puerto
                        puerto=texto.trim();
                        break;
                    case 4://cuenta
                        envia=texto.trim();
                        break;
                    case 5://contraseña
                        clave=texto.trim();
                        break;
                }
                renglon+=1;
            }
            b.close();
        }catch(Exception e){e.printStackTrace();}
        
        try
        {
            // se obtiene el objeto Session.
            Properties props = new Properties();
            props.put("mail.smtp.host", smtp);
            props.setProperty("mail.smtp.starttls.enable", ""+ttl);
            props.setProperty("mail.smtp.port", puerto);
            props.setProperty("mail.smtp.user", envia);
            props.setProperty("mail.smtp.auth", "true");

            System.out.println(smtp+"-"+ttl+"-"+puerto+"-"+envia+"-"+clave);
            javax.mail.Session session = javax.mail.Session.getDefaultInstance(props,null);
            // session.setDebug(true);

            // Se compone la parte del texto
            BodyPart texto_mensaje = new MimeBodyPart();
            texto_mensaje.setContent(mensaje, "text/html");

            // Una MultiParte para agrupar texto e imagen.
            MimeMultipart multiParte = new MimeMultipart();
            multiParte.addBodyPart(texto_mensaje);

            // Se compone el correo, dando to, from, subject y el contenido.
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(envia));

            String [] direcciones=from.split(";");
            for(int x=0; x<direcciones.length; x++)
            {
                if(direcciones[x].compareTo("")!=0)
                    message.addRecipient(Message.RecipientType.TO, new InternetAddress(direcciones[x].replace(" ","")));
            }

            String [] dirCC=cc.split(";");
            for(int y=0; y<dirCC.length; y++)
            {
                if(dirCC[y].compareTo("")!=0)
                    message.addRecipient(Message.RecipientType.CC, new InternetAddress(dirCC[y].replace(" ","")));
            }

            message.setSubject(asunto);
            message.setContent(multiParte);

            Transport t = session.getTransport("smtp");
            t.connect(envia, clave);
            t.sendMessage(message, message.getAllRecipients());
            t.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "¡No se pudo enviar el correo error de red!"); 
        }
    }
}