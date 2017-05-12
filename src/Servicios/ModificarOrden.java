/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Servicios;

import Agente.buscaAgentes;
import Ajustador.buscaAjustadores;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import Hibernate.entidades.Estatus;
import Hibernate.entidades.Compania;
import Hibernate.entidades.Clientes;
import Hibernate.entidades.Marca;
import Hibernate.entidades.Orden;
import Hibernate.entidades.Tipo;
import Hibernate.entidades.Usuario;
import Hibernate.entidades.Foto;
import Hibernate.Util.HibernateUtil;
import Compania.buscaCompania;
import Clientes.buscaCliente;
import Compras.ComparaCotizacion;
import Compras.Conciliacion;
import Compras.altaCompras;
import Compras.avanceSurtido;
import Compras.generaCotizacion;
import Empleados.buscaEmpleado;
import Hibernate.entidades.Acceso;
import Hibernate.entidades.Agente;
import Hibernate.entidades.Ajustador;
import Hibernate.entidades.Empleado;
import Hibernate.entidades.Notificacion;
import Hibernate.entidades.Reparacion;
import Marca.buscaMarca;
import Tipo.buscaTipo;
import com.sun.glass.events.KeyEvent;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import javax.imageio.ImageIO;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.swing.JFileChooser;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import Integral.calendario;
import Integral.Imagen;
import Integral.ExtensionFileFilter;
import java.awt.Graphics;
import java.util.Random;
import org.hibernate.criterion.Restrictions;
import Integral.Herramientas;
import Integral.HorizontalBarUI;
import Integral.PeticionPost;
import Integral.VerticalBarUI;
import Operaciones.Destajo;
import Valuacion.Autorizacion;
import Valuacion.PreFactura;
import Valuacion.valuacion;
import Valuacion.valuacionDirecta;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 *
 * @author I.S.C Salvador
 */
public class ModificarOrden extends javax.swing.JPanel {

    Pattern patronEntero=Pattern.compile("[☺☻♥♦.♣♠•◘○◙♂♀♪♫☼►◄↕‼¶§▬↨↑↓→←∟↔▲▼ !\"#$%&'()*+,--/:;<=>?@A-z{|}~⌂ÇüéâäàåçêëèïîìÄÅÉæÆôöòûùÿÖÜø£Ø×ƒáìóúñÑªº¿®¬½¼¡«»░▒▓│┤ÁÂÀ©╣║╗╝¢¥┐└┴┬├─┼ãÃ╚╔╩╦╠═╬¤ðÐÊËÈıÍÎÏ┘┌█▄¦Ì▀ÓßÔÒõÕµþÞÚÛÙýÝ¯´­±┬¾¶§÷¸°¨·¹³²■ ]*");
    Matcher encaja=patronEntero.matcher("1");
    Pattern patronFecha = Pattern.compile("^d{4}/d{2}/d{2}");
    String dia, mes, anio;
    Calendar cal = Calendar.getInstance();
    Usuario usr;
    String periodo=null;
    File archivo=null;
    Orden registro = null;
    int entro_foto=0;
    String existe_foto="";
    int menu=0;
    String sessionPrograma="";
    public Orden orden_act=null;
    Herramientas h;
    String estado="";
    int cerrada=0;
    int modifica=0;
    String ruta;
    Imagen im;
    valuacion r1;
    valuacionDirecta r2;
    
    String id_ajustador="";
    String id_agente="";
    String id_tecnico="";
    
    /**
     * Creates new form altaServicios
     */
    public ModificarOrden(Usuario usuario, String envio_periodo, int opcion, String programa, String carpeta) {
        sessionPrograma=programa;
        usr=usuario;
        periodo=envio_periodo;
        initComponents();
        p_responsables.getVerticalScrollBar().setUI(new VerticalBarUI());
        p_responsables.getHorizontalScrollBar().setUI(new HorizontalBarUI());
        ruta=carpeta;
        cargaEstatus();
        this.cargaSiniestro();
        l_id_cliente.setVisible(false);
        im=new Imagen("imagenes/foto.png", 83, 43, 1, 25, 83, 88);
        p_foto.add(im);
        p_foto.repaint();
        menu=opcion;
        if(menu==3)
        {
            p_ventanas.addTab("Levantamiento", p_levantamiento);
            p_ventanas.addTab("Valuacion", p_valuacion);
        }
        if(menu==4)
        {
            p_ventanas.addTab("Valuacion", p_valuacion);
        }
        if(menu==5)
        {
            p_ventanas.addTab("Autorización", p_autoriza_valuacion);
        }
        if(menu==6)
        {
            p_ventanas.addTab("Genera Cotización", p_genera_cotizacion);
        }
        if(menu==7)
        {
            p_ventanas.addTab("Pedidos", p_genera_pedidos);
            p_ventanas.addTab("Avance de Pedidos", p_avance_pedidos);
            p_ventanas.addTab("Compara Cotizaciones", p_compara_cotizaciones);
            p_ventanas.addTab("Conciliacion", p_conciliacion);
        }
        if(menu==8)
        {
            p_ventanas.addTab("Avance de Pedidos", p_avance_pedidos);
        }
        if(menu==15)
        {
            p_ventanas.addTab("Pre-Factura", p_preFacura);
        }
        if(menu==16)
        {
            p_ventanas.addTab("Destajo", p_destajo);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        p_levantamiento = new javax.swing.JScrollPane();
        p_valuacion = new javax.swing.JScrollPane();
        p_autoriza_valuacion = new javax.swing.JScrollPane();
        p_genera_cotizacion = new javax.swing.JScrollPane();
        p_genera_pedidos = new javax.swing.JScrollPane();
        p_avance_pedidos = new javax.swing.JScrollPane();
        p_compara_cotizaciones = new javax.swing.JScrollPane();
        p_preFacura = new javax.swing.JScrollPane();
        p_conciliacion = new javax.swing.JScrollPane();
        p_destajo = new javax.swing.JScrollPane();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        t_aseguradora = new javax.swing.JTextField();
        b_buscar_aseguradora = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        t_siniestro = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        b_fecha_siniestro = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        t_poliza = new javax.swing.JTextField();
        t_inciso = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        t_reporte = new javax.swing.JTextField();
        l_nombre_aseguradora = new javax.swing.JLabel();
        t_fecha_siniestro = new javax.swing.JTextField();
        p_foto = new javax.swing.JPanel();
        t_orden = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        b_busca_orden = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        t_nombre_cliente = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        t_direccion_cliente = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        t_colonia_cliente = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        t_rfc_cliente = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        t_poblacion_cliente = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        c_estado_cliente = new javax.swing.JComboBox();
        jLabel15 = new javax.swing.JLabel();
        t_email_cliente = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        l_id_cliente = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        cb_tercero = new javax.swing.JCheckBox();
        cb_tercero_asegurado = new javax.swing.JCheckBox();
        cb_asegurado = new javax.swing.JCheckBox();
        b_busca_cliente = new javax.swing.JButton();
        cb_particular = new javax.swing.JCheckBox();
        b_guardar_cliente = new javax.swing.JButton();
        b_nuevo_cliente = new javax.swing.JButton();
        t_contacto_cliente = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        t_nextel_cliente = new javax.swing.JTextField();
        t_telefono_cliente = new javax.swing.JFormattedTextField();
        t_cp_cliente = new javax.swing.JFormattedTextField();
        p_ventanas = new javax.swing.JTabbedPane();
        p_unidad = new javax.swing.JPanel();
        b_cancelar = new javax.swing.JButton();
        b_guardar = new javax.swing.JButton();
        jLabel25 = new javax.swing.JLabel();
        id_modificado = new javax.swing.JTextField();
        l_nombre_modificado = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        t_tipo = new javax.swing.JTextField();
        b_tipo = new javax.swing.JButton();
        l_nombre_marca = new javax.swing.JLabel();
        t_marca = new javax.swing.JTextField();
        b_marca = new javax.swing.JButton();
        jLabel23 = new javax.swing.JLabel();
        t_placas = new javax.swing.JTextField();
        t_motor = new javax.swing.JTextField();
        jLabel22 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        t_serie = new javax.swing.JTextField();
        t_economico = new javax.swing.JTextField();
        jLabel28 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        c_estatus = new javax.swing.JComboBox();
        t_fecha_estatus = new javax.swing.JTextField();
        b_fecha_cliente = new javax.swing.JButton();
        t_fecha_cliente = new javax.swing.JTextField();
        b_fecha_interna = new javax.swing.JButton();
        t_fecha_interna = new javax.swing.JTextField();
        t_demerito = new javax.swing.JFormattedTextField();
        jLabel30 = new javax.swing.JLabel();
        t_deducible = new javax.swing.JFormattedTextField();
        jLabel29 = new javax.swing.JLabel();
        c_siniestro = new javax.swing.JComboBox();
        jLabel32 = new javax.swing.JLabel();
        t_modelo = new javax.swing.JFormattedTextField();
        jLabel33 = new javax.swing.JLabel();
        t_km = new javax.swing.JTextField();
        t_color = new javax.swing.JTextField();
        jLabel34 = new javax.swing.JLabel();
        t_agente = new javax.swing.JTextField();
        b_agente = new javax.swing.JButton();
        b_ajustador = new javax.swing.JButton();
        t_ajustador = new javax.swing.JTextField();
        b_buscarh = new javax.swing.JButton();
        t_tecnico = new javax.swing.JTextField();
        p_responsables = new javax.swing.JScrollPane();
        p_galeria = new javax.swing.JPanel();
        p_documentos = new javax.swing.JPanel();
        p_formatos = new javax.swing.JPanel();
        p_observaciones = new javax.swing.JPanel();
        p_agenda = new javax.swing.JPanel();
        p_inventario = new javax.swing.JScrollPane();
        p_sm = new javax.swing.JPanel();

        p_levantamiento.setAutoscrolls(true);

        p_valuacion.setAutoscrolls(true);

        p_autoriza_valuacion.setAutoscrolls(true);

        p_genera_cotizacion.setAutoscrolls(true);

        p_genera_pedidos.setAutoscrolls(true);

        p_avance_pedidos.setAutoscrolls(true);

        p_compara_cotizaciones.setAutoscrolls(true);

        p_preFacura.setAutoscrolls(true);

        p_conciliacion.setAutoscrolls(true);

        p_destajo.setAutoscrolls(true);

        setBackground(new java.awt.Color(255, 255, 255));
        setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(90, 66, 126), 1, true), "Actualización de Ordenes", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Arial", 1, 10))); // NOI18N
        setAlignmentX(0.0F);
        setAlignmentY(0.0F);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(90, 66, 126), 1, true), "Datos del Seguro", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Arial", 1, 8))); // NOI18N
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(51, 0, 255));
        jLabel1.setText("Aseguradora:");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(16, 59, -1, 20));

        t_aseguradora.setEditable(false);
        t_aseguradora.setBackground(new java.awt.Color(204, 255, 255));
        t_aseguradora.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        t_aseguradora.setNextFocusableComponent(t_siniestro);
        t_aseguradora.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                t_aseguradoraFocusLost(evt);
            }
        });
        t_aseguradora.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                t_aseguradoraActionPerformed(evt);
            }
        });
        t_aseguradora.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                t_aseguradoraKeyTyped(evt);
            }
        });
        jPanel1.add(t_aseguradora, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 59, 53, -1));

        b_buscar_aseguradora.setBackground(new java.awt.Color(2, 135, 242));
        b_buscar_aseguradora.setIcon(new ImageIcon("imagenes/buscar.png"));
        b_buscar_aseguradora.setToolTipText("Consultar aseguradoras");
        b_buscar_aseguradora.setEnabled(false);
        b_buscar_aseguradora.setMaximumSize(new java.awt.Dimension(32, 8));
        b_buscar_aseguradora.setMinimumSize(new java.awt.Dimension(32, 8));
        b_buscar_aseguradora.setNextFocusableComponent(t_siniestro);
        b_buscar_aseguradora.setPreferredSize(new java.awt.Dimension(32, 8));
        b_buscar_aseguradora.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_buscar_aseguradoraActionPerformed(evt);
            }
        });
        jPanel1.add(b_buscar_aseguradora, new org.netbeans.lib.awtextra.AbsoluteConstraints(145, 55, 28, 24));

        jLabel3.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel3.setText("Siniestro:");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 85, -1, -1));

        t_siniestro.setEditable(false);
        t_siniestro.setBackground(new java.awt.Color(204, 255, 255));
        t_siniestro.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        t_siniestro.setNextFocusableComponent(t_poliza);
        t_siniestro.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                t_siniestroFocusLost(evt);
            }
        });
        t_siniestro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                t_siniestroActionPerformed(evt);
            }
        });
        t_siniestro.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                t_siniestroKeyTyped(evt);
            }
        });
        jPanel1.add(t_siniestro, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 100, 90, -1));

        jLabel4.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel4.setText("F.Reporte:");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 130, -1, -1));

        b_fecha_siniestro.setBackground(new java.awt.Color(2, 135, 242));
        b_fecha_siniestro.setIcon(new ImageIcon("imagenes/calendario.png"));
        b_fecha_siniestro.setToolTipText("Calendario");
        b_fecha_siniestro.setEnabled(false);
        b_fecha_siniestro.setMaximumSize(new java.awt.Dimension(32, 8));
        b_fecha_siniestro.setMinimumSize(new java.awt.Dimension(32, 8));
        b_fecha_siniestro.setNextFocusableComponent(cb_asegurado);
        b_fecha_siniestro.setPreferredSize(new java.awt.Dimension(32, 8));
        b_fecha_siniestro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_fecha_siniestroActionPerformed(evt);
            }
        });
        jPanel1.add(b_fecha_siniestro, new org.netbeans.lib.awtextra.AbsoluteConstraints(295, 126, 28, 24));

        jLabel5.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel5.setText("Poliza:");
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(17, 85, -1, -1));

        t_poliza.setEditable(false);
        t_poliza.setBackground(new java.awt.Color(204, 255, 255));
        t_poliza.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        t_poliza.setNextFocusableComponent(t_reporte);
        t_poliza.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                t_polizaFocusLost(evt);
            }
        });
        t_poliza.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                t_polizaActionPerformed(evt);
            }
        });
        t_poliza.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                t_polizaKeyTyped(evt);
            }
        });
        jPanel1.add(t_poliza, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 100, 90, -1));

        t_inciso.setEditable(false);
        t_inciso.setBackground(new java.awt.Color(204, 255, 255));
        t_inciso.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        t_inciso.setNextFocusableComponent(b_fecha_siniestro);
        t_inciso.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                t_incisoFocusLost(evt);
            }
        });
        t_inciso.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                t_incisoActionPerformed(evt);
            }
        });
        t_inciso.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                t_incisoKeyTyped(evt);
            }
        });
        jPanel1.add(t_inciso, new org.netbeans.lib.awtextra.AbsoluteConstraints(205, 100, 30, -1));

        jLabel6.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel6.setText("Inciso");
        jPanel1.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(205, 85, -1, -1));

        jLabel7.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel7.setText("Reporte:");
        jPanel1.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 130, -1, -1));

        t_reporte.setEditable(false);
        t_reporte.setBackground(new java.awt.Color(204, 255, 255));
        t_reporte.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        t_reporte.setNextFocusableComponent(t_inciso);
        t_reporte.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                t_reporteFocusLost(evt);
            }
        });
        t_reporte.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                t_reporteActionPerformed(evt);
            }
        });
        t_reporte.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                t_reporteKeyTyped(evt);
            }
        });
        jPanel1.add(t_reporte, new org.netbeans.lib.awtextra.AbsoluteConstraints(58, 130, 90, -1));

        l_nombre_aseguradora.setFont(new java.awt.Font("Arial", 0, 10)); // NOI18N
        l_nombre_aseguradora.setText("Seleccione una aseguradora");
        jPanel1.add(l_nombre_aseguradora, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 36, 210, 20));

        t_fecha_siniestro.setEditable(false);
        t_fecha_siniestro.setBackground(new java.awt.Color(204, 255, 255));
        t_fecha_siniestro.setText("DD-MM-AAAA");
        t_fecha_siniestro.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel1.add(t_fecha_siniestro, new org.netbeans.lib.awtextra.AbsoluteConstraints(218, 130, 76, -1));

        p_foto.setBackground(new java.awt.Color(2, 135, 242));
        p_foto.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        p_foto.setToolTipText("Agregar imagen de la unidad");
        p_foto.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        p_foto.setPreferredSize(new java.awt.Dimension(84, 94));
        p_foto.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                p_fotoMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout p_fotoLayout = new javax.swing.GroupLayout(p_foto);
        p_foto.setLayout(p_fotoLayout);
        p_fotoLayout.setHorizontalGroup(
            p_fotoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 82, Short.MAX_VALUE)
        );
        p_fotoLayout.setVerticalGroup(
            p_fotoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 92, Short.MAX_VALUE)
        );

        jPanel1.add(p_foto, new org.netbeans.lib.awtextra.AbsoluteConstraints(241, 15, -1, -1));

        t_orden.setBackground(new java.awt.Color(204, 255, 255));
        t_orden.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        t_orden.setNextFocusableComponent(b_busca_orden);
        t_orden.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                t_ordenActionPerformed(evt);
            }
        });
        t_orden.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                t_ordenKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                t_ordenKeyTyped(evt);
            }
        });
        jPanel1.add(t_orden, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 12, 110, -1));

        jLabel2.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(51, 0, 255));
        jLabel2.setText("No Orden:");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(16, 14, -1, -1));

        b_busca_orden.setBackground(new java.awt.Color(2, 135, 242));
        b_busca_orden.setIcon(new ImageIcon("imagenes/buscar.png"));
        b_busca_orden.setMnemonic(KeyEvent.VK_F1);
        b_busca_orden.setMaximumSize(new java.awt.Dimension(32, 8));
        b_busca_orden.setMinimumSize(new java.awt.Dimension(32, 8));
        b_busca_orden.setNextFocusableComponent(t_aseguradora);
        b_busca_orden.setPreferredSize(new java.awt.Dimension(32, 8));
        b_busca_orden.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_busca_ordenActionPerformed(evt);
            }
        });
        jPanel1.add(b_busca_orden, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 10, 28, 24));

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(90, 66, 126), 1, true), "Datos del Cliente", javax.swing.border.TitledBorder.RIGHT, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Arial", 1, 8))); // NOI18N

        jLabel8.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(51, 0, 255));
        jLabel8.setText("Nombre:");

        t_nombre_cliente.setEditable(false);
        t_nombre_cliente.setBackground(new java.awt.Color(204, 255, 255));
        t_nombre_cliente.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        t_nombre_cliente.setNextFocusableComponent(t_direccion_cliente);
        t_nombre_cliente.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                t_nombre_clienteFocusLost(evt);
            }
        });
        t_nombre_cliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                t_nombre_clienteActionPerformed(evt);
            }
        });
        t_nombre_cliente.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                t_nombre_clienteKeyTyped(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel9.setText("Dir:");

        t_direccion_cliente.setEditable(false);
        t_direccion_cliente.setBackground(new java.awt.Color(204, 255, 255));
        t_direccion_cliente.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        t_direccion_cliente.setNextFocusableComponent(t_cp_cliente);
        t_direccion_cliente.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                t_direccion_clienteFocusLost(evt);
            }
        });
        t_direccion_cliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                t_direccion_clienteActionPerformed(evt);
            }
        });
        t_direccion_cliente.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                t_direccion_clienteKeyTyped(evt);
            }
        });

        jLabel10.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel10.setText("Col:");

        t_colonia_cliente.setEditable(false);
        t_colonia_cliente.setBackground(new java.awt.Color(204, 255, 255));
        t_colonia_cliente.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        t_colonia_cliente.setNextFocusableComponent(t_rfc_cliente);
        t_colonia_cliente.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                t_colonia_clienteFocusLost(evt);
            }
        });
        t_colonia_cliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                t_colonia_clienteActionPerformed(evt);
            }
        });
        t_colonia_cliente.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                t_colonia_clienteKeyTyped(evt);
            }
        });

        jLabel11.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel11.setText("CP:");

        jLabel12.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel12.setText("RFC:");

        t_rfc_cliente.setEditable(false);
        t_rfc_cliente.setBackground(new java.awt.Color(204, 255, 255));
        t_rfc_cliente.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        t_rfc_cliente.setNextFocusableComponent(t_email_cliente);
        t_rfc_cliente.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                t_rfc_clienteFocusLost(evt);
            }
        });
        t_rfc_cliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                t_rfc_clienteActionPerformed(evt);
            }
        });
        t_rfc_cliente.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                t_rfc_clienteKeyTyped(evt);
            }
        });

        jLabel13.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel13.setText("Pob:");

        t_poblacion_cliente.setEditable(false);
        t_poblacion_cliente.setBackground(new java.awt.Color(204, 255, 255));
        t_poblacion_cliente.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        t_poblacion_cliente.setNextFocusableComponent(c_estado_cliente);
        t_poblacion_cliente.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                t_poblacion_clienteFocusLost(evt);
            }
        });
        t_poblacion_cliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                t_poblacion_clienteActionPerformed(evt);
            }
        });
        t_poblacion_cliente.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                t_poblacion_clienteKeyTyped(evt);
            }
        });

        jLabel14.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel14.setText("Edo:");

        c_estado_cliente.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "AGUASCALIENTES", "BAJA CALIFORNIA", "BAJA CALIFORNIA SUR", "CAMPECHE", "CHIAPAS", "CHIHUAHUA", "COAHUILA", "COLIMA", "CIUDAD DE MÉXICO", "DURANGO", "ESTADO DE MÉXICO", "GUANAJUATO", "GUERRERO", "HIDALGO", "JALISCO", "MICHOACAN", "MORELOS", "NAYARIT", "NUEVO LEON", "OAXACA", "PUEBLA", "QUERETARO", "QUINTANA ROO", "SAN LUIS POTOSI", "SINALOA", "SONORA", "TABASCO", "TAMAULIPAS", "TLAXCALA", "VERACRUZ", "YUCATAN", "ZACATECAS" }));
        c_estado_cliente.setEnabled(false);
        c_estado_cliente.setNextFocusableComponent(b_guardar_cliente);
        c_estado_cliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                c_estado_clienteActionPerformed(evt);
            }
        });

        jLabel15.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel15.setText("Tel:");

        t_email_cliente.setEditable(false);
        t_email_cliente.setBackground(new java.awt.Color(204, 255, 255));
        t_email_cliente.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        t_email_cliente.setNextFocusableComponent(t_contacto_cliente);
        t_email_cliente.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                t_email_clienteFocusLost(evt);
            }
        });
        t_email_cliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                t_email_clienteActionPerformed(evt);
            }
        });
        t_email_cliente.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                t_email_clienteKeyTyped(evt);
            }
        });

        jLabel16.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(51, 0, 255));
        jLabel16.setText("email:");

        jPanel5.setOpaque(false);
        jPanel5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        cb_tercero.setText("Tercero           ");
        cb_tercero.setEnabled(false);
        cb_tercero.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cb_terceroActionPerformed(evt);
            }
        });
        jPanel5.add(cb_tercero, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 20, -1, -1));

        cb_tercero_asegurado.setText("Tercero Aseg.");
        cb_tercero_asegurado.setEnabled(false);
        cb_tercero_asegurado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cb_tercero_aseguradoActionPerformed(evt);
            }
        });
        jPanel5.add(cb_tercero_asegurado, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 40, -1, -1));

        cb_asegurado.setText("Asegurado     ");
        cb_asegurado.setEnabled(false);
        cb_asegurado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cb_aseguradoActionPerformed(evt);
            }
        });
        jPanel5.add(cb_asegurado, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        b_busca_cliente.setBackground(new java.awt.Color(2, 135, 242));
        b_busca_cliente.setIcon(new ImageIcon("imagenes/buscar.png"));
        b_busca_cliente.setToolTipText("Consultar clientes");
        b_busca_cliente.setEnabled(false);
        b_busca_cliente.setMaximumSize(new java.awt.Dimension(32, 8));
        b_busca_cliente.setMinimumSize(new java.awt.Dimension(32, 8));
        b_busca_cliente.setPreferredSize(new java.awt.Dimension(32, 8));
        b_busca_cliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_busca_clienteActionPerformed(evt);
            }
        });
        jPanel5.add(b_busca_cliente, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 90, -1, 30));

        cb_particular.setText("Particular        ");
        cb_particular.setEnabled(false);
        cb_particular.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cb_particularActionPerformed(evt);
            }
        });
        jPanel5.add(cb_particular, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 60, -1, -1));

        b_guardar_cliente.setBackground(new java.awt.Color(2, 135, 242));
        b_guardar_cliente.setIcon(new ImageIcon("imagenes/guardar.png"));
        b_guardar_cliente.setToolTipText("Guardar Cliente");
        b_guardar_cliente.setEnabled(false);
        b_guardar_cliente.setMaximumSize(new java.awt.Dimension(32, 8));
        b_guardar_cliente.setMinimumSize(new java.awt.Dimension(32, 8));
        b_guardar_cliente.setPreferredSize(new java.awt.Dimension(32, 8));
        b_guardar_cliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_guardar_clienteActionPerformed(evt);
            }
        });
        jPanel5.add(b_guardar_cliente, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 90, -1, 30));

        b_nuevo_cliente.setBackground(new java.awt.Color(2, 135, 242));
        b_nuevo_cliente.setIcon(new ImageIcon("imagenes/nuevo.png"));
        b_nuevo_cliente.setToolTipText("Dar de alta un cliente");
        b_nuevo_cliente.setEnabled(false);
        b_nuevo_cliente.setMaximumSize(new java.awt.Dimension(32, 8));
        b_nuevo_cliente.setMinimumSize(new java.awt.Dimension(32, 8));
        b_nuevo_cliente.setPreferredSize(new java.awt.Dimension(32, 8));
        b_nuevo_cliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_nuevo_clienteActionPerformed(evt);
            }
        });
        jPanel5.add(b_nuevo_cliente, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 90, -1, 30));

        t_contacto_cliente.setEditable(false);
        t_contacto_cliente.setBackground(new java.awt.Color(204, 255, 255));
        t_contacto_cliente.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        t_contacto_cliente.setNextFocusableComponent(t_telefono_cliente);
        t_contacto_cliente.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                t_contacto_clienteFocusLost(evt);
            }
        });
        t_contacto_cliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                t_contacto_clienteActionPerformed(evt);
            }
        });
        t_contacto_cliente.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                t_contacto_clienteKeyTyped(evt);
            }
        });

        jLabel18.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel18.setText("Contacto:");

        jLabel31.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel31.setText("ID:");

        t_nextel_cliente.setEditable(false);
        t_nextel_cliente.setBackground(new java.awt.Color(204, 255, 255));
        t_nextel_cliente.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        t_nextel_cliente.setNextFocusableComponent(t_poblacion_cliente);
        t_nextel_cliente.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                t_nextel_clienteFocusLost(evt);
            }
        });
        t_nextel_cliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                t_nextel_clienteActionPerformed(evt);
            }
        });
        t_nextel_cliente.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                t_nextel_clienteKeyTyped(evt);
            }
        });

        t_telefono_cliente.setEditable(false);
        t_telefono_cliente.setBackground(new java.awt.Color(204, 255, 255));
        t_telefono_cliente.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        t_telefono_cliente.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("##0"))));
        t_telefono_cliente.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                t_telefono_clienteFocusLost(evt);
            }
        });
        t_telefono_cliente.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                t_telefono_clienteKeyTyped(evt);
            }
        });

        t_cp_cliente.setEditable(false);
        t_cp_cliente.setBackground(new java.awt.Color(204, 255, 255));
        t_cp_cliente.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        t_cp_cliente.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("##0"))));
        t_cp_cliente.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                t_cp_clienteFocusLost(evt);
            }
        });
        t_cp_cliente.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                t_cp_clienteKeyTyped(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(t_nombre_cliente))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel9)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(t_direccion_cliente))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(t_contacto_cliente))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel10)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(t_colonia_cliente)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel12)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(t_rfc_cliente, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(8, 8, 8)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel11, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel16, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel15, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(t_email_cliente, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(t_cp_cliente, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(t_telefono_cliente, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel31)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(t_nextel_cliente, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel13)
                        .addGap(6, 6, 6)
                        .addComponent(t_poblacion_cliente)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(l_id_cliente, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(9, 9, 9)
                        .addComponent(jLabel14)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(c_estado_cliente, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 23, Short.MAX_VALUE)
                    .addComponent(t_nombre_cliente, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jLabel9, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 23, Short.MAX_VALUE)
                        .addComponent(t_direccion_cliente, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(t_cp_cliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jLabel10, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(t_colonia_cliente, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(t_email_cliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(t_rfc_cliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(t_telefono_cliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(9, 9, 9))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(t_contacto_cliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(c_estado_cliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel14))
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(t_poblacion_cliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(t_nextel_cliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel31)
                            .addComponent(jLabel13))
                        .addComponent(l_id_cliente, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))))
            .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        p_ventanas.setFont(new java.awt.Font("Dialog", 0, 10)); // NOI18N
        p_ventanas.setInheritsPopupMenu(true);
        p_ventanas.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                p_ventanasStateChanged(evt);
            }
        });

        p_unidad.setBackground(new java.awt.Color(255, 255, 255));
        p_unidad.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(90, 66, 126), 1, true), "Datos de la Unidad", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Arial", 1, 11))); // NOI18N

        b_cancelar.setBackground(new java.awt.Color(2, 135, 242));
        b_cancelar.setForeground(new java.awt.Color(255, 255, 255));
        b_cancelar.setIcon(new ImageIcon("imagenes/cancelar.png"));
        b_cancelar.setText("Cancelar");
        b_cancelar.setToolTipText("Cancelar orden actual");
        b_cancelar.setEnabled(false);
        b_cancelar.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        b_cancelar.setNextFocusableComponent(t_orden);
        b_cancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_cancelarActionPerformed(evt);
            }
        });

        b_guardar.setBackground(new java.awt.Color(2, 135, 242));
        b_guardar.setForeground(new java.awt.Color(255, 255, 255));
        b_guardar.setIcon(new ImageIcon("imagenes/guardar.png"));
        b_guardar.setText("Actualizar");
        b_guardar.setToolTipText("Actualizar orden actual");
        b_guardar.setEnabled(false);
        b_guardar.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        b_guardar.setNextFocusableComponent(b_cancelar);
        b_guardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_guardarActionPerformed(evt);
            }
        });

        jLabel25.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel25.setForeground(new java.awt.Color(51, 0, 255));
        jLabel25.setText("Modificado por:");

        id_modificado.setEditable(false);
        id_modificado.setBackground(new java.awt.Color(204, 255, 255));
        id_modificado.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel17.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(51, 0, 255));
        jLabel17.setText("Nota: Los campos en azul son obligatorios");

        t_tipo.setEditable(false);
        t_tipo.setBackground(new java.awt.Color(204, 255, 255));
        t_tipo.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        t_tipo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                t_tipoFocusLost(evt);
            }
        });
        t_tipo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                t_tipoActionPerformed(evt);
            }
        });
        t_tipo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                t_tipoKeyTyped(evt);
            }
        });

        b_tipo.setBackground(new java.awt.Color(2, 135, 242));
        b_tipo.setForeground(new java.awt.Color(255, 255, 255));
        b_tipo.setText("Tipo");
        b_tipo.setToolTipText("Consultar Tipos de vehículo");
        b_tipo.setEnabled(false);
        b_tipo.setMargin(new java.awt.Insets(1, 14, 1, 14));
        b_tipo.setMaximumSize(new java.awt.Dimension(28, 24));
        b_tipo.setMinimumSize(new java.awt.Dimension(28, 24));
        b_tipo.setPreferredSize(new java.awt.Dimension(28, 24));
        b_tipo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_tipoActionPerformed(evt);
            }
        });

        l_nombre_marca.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        l_nombre_marca.setText("Seleccione una marca");

        t_marca.setEditable(false);
        t_marca.setBackground(new java.awt.Color(204, 255, 255));
        t_marca.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        t_marca.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                t_marcaFocusLost(evt);
            }
        });
        t_marca.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                t_marcaActionPerformed(evt);
            }
        });
        t_marca.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                t_marcaKeyTyped(evt);
            }
        });

        b_marca.setBackground(new java.awt.Color(2, 135, 242));
        b_marca.setForeground(new java.awt.Color(255, 255, 255));
        b_marca.setText("Marca");
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

        jLabel23.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel23.setText("Placas:");

        t_placas.setEditable(false);
        t_placas.setBackground(new java.awt.Color(204, 255, 255));
        t_placas.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        t_placas.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                t_placasFocusLost(evt);
            }
        });
        t_placas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                t_placasActionPerformed(evt);
            }
        });
        t_placas.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                t_placasKeyTyped(evt);
            }
        });

        t_motor.setEditable(false);
        t_motor.setBackground(new java.awt.Color(204, 255, 255));
        t_motor.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        t_motor.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                t_motorFocusLost(evt);
            }
        });
        t_motor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                t_motorActionPerformed(evt);
            }
        });
        t_motor.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                t_motorKeyTyped(evt);
            }
        });

        jLabel22.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel22.setText("Motor:");

        jLabel26.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel26.setForeground(new java.awt.Color(51, 0, 255));
        jLabel26.setText("Año:");

        jLabel27.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel27.setText("Serie:");

        t_serie.setEditable(false);
        t_serie.setBackground(new java.awt.Color(204, 255, 255));
        t_serie.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        t_serie.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                t_serieFocusLost(evt);
            }
        });
        t_serie.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                t_serieActionPerformed(evt);
            }
        });
        t_serie.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                t_serieKeyTyped(evt);
            }
        });

        t_economico.setEditable(false);
        t_economico.setBackground(new java.awt.Color(204, 255, 255));
        t_economico.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        t_economico.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                t_economicoFocusLost(evt);
            }
        });
        t_economico.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                t_economicoActionPerformed(evt);
            }
        });
        t_economico.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                t_economicoKeyTyped(evt);
            }
        });

        jLabel28.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel28.setText("Económico:");

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(90, 66, 126), 1, true), "Estatus", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.TOP));

        c_estatus.setForeground(new java.awt.Color(51, 0, 255));
        c_estatus.setEnabled(false);
        c_estatus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                c_estatusActionPerformed(evt);
            }
        });

        t_fecha_estatus.setBackground(new java.awt.Color(204, 255, 255));
        t_fecha_estatus.setForeground(new java.awt.Color(51, 0, 255));
        t_fecha_estatus.setText("DD-MM-AAAA");
        t_fecha_estatus.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        t_fecha_estatus.setEnabled(false);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(c_estatus, javax.swing.GroupLayout.PREFERRED_SIZE, 227, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(t_fecha_estatus, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(c_estatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(t_fecha_estatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        b_fecha_cliente.setBackground(new java.awt.Color(2, 135, 242));
        b_fecha_cliente.setForeground(new java.awt.Color(255, 255, 255));
        b_fecha_cliente.setText("F.Cliente");
        b_fecha_cliente.setToolTipText("Calendario");
        b_fecha_cliente.setEnabled(false);
        b_fecha_cliente.setMaximumSize(new java.awt.Dimension(32, 8));
        b_fecha_cliente.setMinimumSize(new java.awt.Dimension(32, 8));
        b_fecha_cliente.setPreferredSize(new java.awt.Dimension(28, 24));
        b_fecha_cliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_fecha_clienteActionPerformed(evt);
            }
        });

        t_fecha_cliente.setEditable(false);
        t_fecha_cliente.setBackground(new java.awt.Color(204, 255, 255));
        t_fecha_cliente.setText("DD-MM-AAAA");
        t_fecha_cliente.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        b_fecha_interna.setBackground(new java.awt.Color(2, 135, 242));
        b_fecha_interna.setForeground(new java.awt.Color(255, 255, 255));
        b_fecha_interna.setText("F.Interna");
        b_fecha_interna.setToolTipText("Calendario");
        b_fecha_interna.setEnabled(false);
        b_fecha_interna.setMaximumSize(new java.awt.Dimension(32, 8));
        b_fecha_interna.setMinimumSize(new java.awt.Dimension(32, 8));
        b_fecha_interna.setPreferredSize(new java.awt.Dimension(28, 24));
        b_fecha_interna.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_fecha_internaActionPerformed(evt);
            }
        });

        t_fecha_interna.setEditable(false);
        t_fecha_interna.setBackground(new java.awt.Color(204, 255, 255));
        t_fecha_interna.setText("DD-MM-AAAA");
        t_fecha_interna.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        t_demerito.setEditable(false);
        t_demerito.setBackground(new java.awt.Color(204, 255, 255));
        t_demerito.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        t_demerito.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.00"))));
        t_demerito.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        t_demerito.setText("0.00");
        t_demerito.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                t_demeritoActionPerformed(evt);
            }
        });

        jLabel30.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel30.setText("Demerito:");

        t_deducible.setEditable(false);
        t_deducible.setBackground(new java.awt.Color(204, 255, 255));
        t_deducible.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        t_deducible.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.00"))));
        t_deducible.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        t_deducible.setText("0.00");
        t_deducible.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                t_deducibleActionPerformed(evt);
            }
        });

        jLabel29.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel29.setText("Deducible:");

        c_siniestro.setBackground(new java.awt.Color(204, 255, 255));
        c_siniestro.setForeground(new java.awt.Color(51, 0, 255));
        c_siniestro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                c_siniestroActionPerformed(evt);
            }
        });

        jLabel32.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel32.setText("tipo Siniestro:");

        t_modelo.setEditable(false);
        t_modelo.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        t_modelo.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("##0"))));
        t_modelo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                t_modeloFocusLost(evt);
            }
        });

        jLabel33.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel33.setText("Km:");

        t_km.setBackground(new java.awt.Color(204, 255, 255));
        t_km.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        t_km.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                t_kmFocusLost(evt);
            }
        });
        t_km.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                t_kmActionPerformed(evt);
            }
        });
        t_km.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                t_kmKeyTyped(evt);
            }
        });

        t_color.setBackground(new java.awt.Color(204, 255, 255));
        t_color.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        t_color.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                t_colorFocusLost(evt);
            }
        });
        t_color.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                t_colorActionPerformed(evt);
            }
        });
        t_color.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                t_colorKeyTyped(evt);
            }
        });

        jLabel34.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel34.setText("Color:");

        t_agente.setEditable(false);
        t_agente.setBackground(new java.awt.Color(204, 255, 255));
        t_agente.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        t_agente.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                t_agenteFocusLost(evt);
            }
        });
        t_agente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                t_agenteActionPerformed(evt);
            }
        });
        t_agente.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                t_agenteKeyTyped(evt);
            }
        });

        b_agente.setBackground(new java.awt.Color(2, 135, 242));
        b_agente.setForeground(new java.awt.Color(255, 255, 255));
        b_agente.setText("Agente");
        b_agente.setToolTipText("Calendario");
        b_agente.setEnabled(false);
        b_agente.setMaximumSize(new java.awt.Dimension(32, 8));
        b_agente.setMinimumSize(new java.awt.Dimension(32, 8));
        b_agente.setPreferredSize(new java.awt.Dimension(28, 24));
        b_agente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_agenteActionPerformed(evt);
            }
        });

        b_ajustador.setBackground(new java.awt.Color(2, 135, 242));
        b_ajustador.setForeground(new java.awt.Color(255, 255, 255));
        b_ajustador.setText("Ajustador");
        b_ajustador.setToolTipText("Calendario");
        b_ajustador.setEnabled(false);
        b_ajustador.setMaximumSize(new java.awt.Dimension(32, 8));
        b_ajustador.setMinimumSize(new java.awt.Dimension(32, 8));
        b_ajustador.setPreferredSize(new java.awt.Dimension(28, 24));
        b_ajustador.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_ajustadorActionPerformed(evt);
            }
        });

        t_ajustador.setEditable(false);
        t_ajustador.setBackground(new java.awt.Color(204, 255, 255));
        t_ajustador.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        t_ajustador.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                t_ajustadorFocusLost(evt);
            }
        });
        t_ajustador.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                t_ajustadorActionPerformed(evt);
            }
        });
        t_ajustador.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                t_ajustadorKeyTyped(evt);
            }
        });

        b_buscarh.setBackground(new java.awt.Color(2, 135, 242));
        b_buscarh.setForeground(new java.awt.Color(255, 255, 255));
        b_buscarh.setText("Supervisor");
        b_buscarh.setToolTipText("Consultar empleados");
        b_buscarh.setEnabled(false);
        b_buscarh.setMaximumSize(new java.awt.Dimension(32, 8));
        b_buscarh.setMinimumSize(new java.awt.Dimension(32, 8));
        b_buscarh.setPreferredSize(new java.awt.Dimension(32, 8));
        b_buscarh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_buscarhActionPerformed(evt);
            }
        });

        t_tecnico.setBackground(new java.awt.Color(204, 255, 255));
        t_tecnico.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        t_tecnico.setEnabled(false);

        javax.swing.GroupLayout p_unidadLayout = new javax.swing.GroupLayout(p_unidad);
        p_unidad.setLayout(p_unidadLayout);
        p_unidadLayout.setHorizontalGroup(
            p_unidadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(p_unidadLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(p_unidadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel17, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, p_unidadLayout.createSequentialGroup()
                        .addGroup(p_unidadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, p_unidadLayout.createSequentialGroup()
                                .addGroup(p_unidadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(b_ajustador, javax.swing.GroupLayout.DEFAULT_SIZE, 85, Short.MAX_VALUE)
                                    .addComponent(b_agente, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(6, 6, 6)
                                .addGroup(p_unidadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(t_ajustador)
                                    .addComponent(t_agente)))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, p_unidadLayout.createSequentialGroup()
                                .addGroup(p_unidadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(b_marca, javax.swing.GroupLayout.DEFAULT_SIZE, 85, Short.MAX_VALUE)
                                    .addComponent(b_tipo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(p_unidadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(p_unidadLayout.createSequentialGroup()
                                        .addComponent(t_marca, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(l_nombre_marca, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addGroup(p_unidadLayout.createSequentialGroup()
                                        .addComponent(t_tipo, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 0, Short.MAX_VALUE))))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, p_unidadLayout.createSequentialGroup()
                                .addGroup(p_unidadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addGroup(p_unidadLayout.createSequentialGroup()
                                        .addComponent(b_buscarh, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(t_tecnico, javax.swing.GroupLayout.PREFERRED_SIZE, 265, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, p_unidadLayout.createSequentialGroup()
                                        .addComponent(b_fecha_interna, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(t_fecha_interna, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(b_fecha_cliente, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(t_fecha_cliente, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addGap(99, 99, 99)
                        .addGroup(p_unidadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(p_unidadLayout.createSequentialGroup()
                                .addGroup(p_unidadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(p_unidadLayout.createSequentialGroup()
                                        .addComponent(jLabel34)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(t_color))
                                    .addGroup(p_unidadLayout.createSequentialGroup()
                                        .addComponent(jLabel23)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(t_placas, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel22)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(t_motor))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, p_unidadLayout.createSequentialGroup()
                                        .addComponent(jLabel32)
                                        .addGap(18, 18, 18)
                                        .addComponent(c_siniestro, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, p_unidadLayout.createSequentialGroup()
                                        .addComponent(jLabel26)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(t_modelo, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel27)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(t_serie, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(p_unidadLayout.createSequentialGroup()
                                        .addComponent(jLabel28)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(t_economico, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel33)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(t_km, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jPanel4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(56, 56, 56)
                                .addGroup(p_unidadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(b_guardar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(b_cancelar, javax.swing.GroupLayout.DEFAULT_SIZE, 114, Short.MAX_VALUE)))
                            .addGroup(p_unidadLayout.createSequentialGroup()
                                .addComponent(jLabel29)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(t_deducible, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(43, 43, 43)
                                .addComponent(jLabel30)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(t_demerito, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(p_unidadLayout.createSequentialGroup()
                        .addComponent(jLabel25)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(id_modificado, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(l_nombre_modificado, javax.swing.GroupLayout.PREFERRED_SIZE, 295, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        p_unidadLayout.setVerticalGroup(
            p_unidadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(p_unidadLayout.createSequentialGroup()
                .addGroup(p_unidadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(p_unidadLayout.createSequentialGroup()
                        .addComponent(b_cancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(b_guardar, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(p_unidadLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(p_unidadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(p_unidadLayout.createSequentialGroup()
                                .addGroup(p_unidadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(b_tipo, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(t_tipo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(p_unidadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(b_marca, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(t_marca, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(l_nombre_marca, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(p_unidadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(b_fecha_cliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(p_unidadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(b_fecha_interna, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(t_fecha_interna, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(t_fecha_cliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(p_unidadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(b_ajustador, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(t_ajustador, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(p_unidadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(b_agente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(t_agente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(p_unidadLayout.createSequentialGroup()
                                .addGroup(p_unidadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel27)
                                    .addComponent(t_serie, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel26)
                                    .addComponent(t_modelo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(p_unidadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel23)
                                    .addComponent(t_placas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel22)
                                    .addComponent(t_motor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(p_unidadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel34)
                                    .addComponent(t_color, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(p_unidadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(p_unidadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel33)
                                        .addComponent(t_km, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(p_unidadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel28)
                                        .addComponent(t_economico, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(p_unidadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(p_unidadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(b_buscarh, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(t_tecnico, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(p_unidadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(c_siniestro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel32)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(p_unidadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel29)
                    .addComponent(jLabel30)
                    .addComponent(t_deducible, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(t_demerito, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(p_unidadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(p_unidadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel25)
                        .addComponent(id_modificado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(l_nombre_modificado, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel17)
                .addGap(5, 5, 5))
        );

        p_ventanas.addTab("Unidad", p_unidad);

        p_responsables.setAutoscrolls(true);
        p_ventanas.addTab("Seguimiento", p_responsables);

        p_galeria.setLayout(new java.awt.BorderLayout());
        p_ventanas.addTab("Galeria", p_galeria);

        p_documentos.setLayout(new java.awt.BorderLayout());
        p_ventanas.addTab("Documentos", p_documentos);

        p_formatos.setLayout(new java.awt.BorderLayout());
        p_ventanas.addTab("Formatos", p_formatos);

        p_observaciones.setLayout(new java.awt.BorderLayout());
        p_ventanas.addTab("Observaciones", p_observaciones);

        p_agenda.setLayout(new java.awt.BorderLayout());
        p_ventanas.addTab("Agenda de Cliente", p_agenda);
        p_ventanas.addTab("Inventario", p_inventario);

        p_sm.setLayout(new java.awt.BorderLayout());
        p_ventanas.addTab("SM Logistics", new javax.swing.ImageIcon(getClass().getResource("/Servicios/icono-1.png")), p_sm); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 335, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(p_ventanas)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 157, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(p_ventanas))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void t_siniestroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_t_siniestroActionPerformed
        t_poliza.requestFocus();
    }//GEN-LAST:event_t_siniestroActionPerformed

    private void t_polizaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_t_polizaActionPerformed
        t_reporte.requestFocus();
    }//GEN-LAST:event_t_polizaActionPerformed

    private void t_incisoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_t_incisoActionPerformed
        this.b_fecha_siniestro.requestFocus();
    }//GEN-LAST:event_t_incisoActionPerformed

    private void t_reporteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_t_reporteActionPerformed
        t_inciso.requestFocus();
    }//GEN-LAST:event_t_reporteActionPerformed

    private void b_fecha_siniestroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_fecha_siniestroActionPerformed
        h=new Herramientas(usr, menu);
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
            t_fecha_siniestro.setText(dia+"-"+mes+"-"+anio);
            b_busca_cliente.requestFocus();
        }
        else
            t_fecha_siniestro.setText("DD-MM-AAAA");
    }//GEN-LAST:event_b_fecha_siniestroActionPerformed

    private void t_aseguradoraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_t_aseguradoraActionPerformed
            t_siniestro.requestFocus();
    }//GEN-LAST:event_t_aseguradoraActionPerformed

    private void t_aseguradoraFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_t_aseguradoraFocusLost
        if(t_aseguradora.getText().compareTo("")!=0)
            buscaCompania();
        else
            this.l_nombre_aseguradora.setText("Seleccione una aseguradora");
    }//GEN-LAST:event_t_aseguradoraFocusLost

    private void t_nombre_clienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_t_nombre_clienteActionPerformed
        t_direccion_cliente.requestFocus();
    }//GEN-LAST:event_t_nombre_clienteActionPerformed

    private void t_direccion_clienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_t_direccion_clienteActionPerformed
        t_colonia_cliente.requestFocus();
    }//GEN-LAST:event_t_direccion_clienteActionPerformed

    private void t_colonia_clienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_t_colonia_clienteActionPerformed
        t_cp_cliente.requestFocus();
    }//GEN-LAST:event_t_colonia_clienteActionPerformed

    private void t_rfc_clienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_t_rfc_clienteActionPerformed
        t_poblacion_cliente.requestFocus();
    }//GEN-LAST:event_t_rfc_clienteActionPerformed

    private void t_poblacion_clienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_t_poblacion_clienteActionPerformed
        c_estado_cliente.requestFocus();
    }//GEN-LAST:event_t_poblacion_clienteActionPerformed

    private void c_estado_clienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_c_estado_clienteActionPerformed

    }//GEN-LAST:event_c_estado_clienteActionPerformed

    private void t_email_clienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_t_email_clienteActionPerformed
        b_guardar_cliente.requestFocus();
    }//GEN-LAST:event_t_email_clienteActionPerformed

    private void b_guardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_guardarActionPerformed
        h= new Herramientas(usr, menu);
        h.desbloqueaOrden();
        if(t_orden.getText().compareTo("")!=0)
        {
        if(t_aseguradora.getText().compareTo("")!=0)
        {
            if(t_nombre_cliente.getText().compareTo("")!=0)
            {
                if(l_id_cliente.getText().compareTo("")!=0)
                {
                    if(t_tipo.getText().compareTo("")!=0)
                    {
                        if(t_marca.getText().compareTo("")!=0)
                        {
                            if(t_modelo.getText().compareTo("")!=0)
                            {
                                boolean respuesta=guardarOrden();
                                if(respuesta==true)
                                {
                                    Date fecha = new Date();
                                    DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");//YYYY-MM-DD HH:MM:SS
                                    String valor=dateFormat.format(fecha);
                                    boolean resp=false;
                                    if(c_estatus.getSelectedItem().toString().compareToIgnoreCase("FACTURADO")==0)
                                        resp=guardaEmpleado(usr.getEmpleado(), "cierra_orden", valor);
                                    //this.borra_cajas();
                                    //this.t_orden.setText(dia);
                                    //estado_campos(true, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false);
                                    JOptionPane.showMessageDialog(null, "La orden se ha guardado");
                                    if(c_estatus.getSelectedItem().toString().compareToIgnoreCase("FACTURADO")==0)
                                    {
                                        orden_act=buscarOrden(Integer.parseInt(t_orden.getText()));
                                        consultaOrden();
                                    }
                                }
                                else
                                {
                                    JOptionPane.showMessageDialog(null, "¡Error al Guardar!");
                                    t_tipo.requestFocus();
                                }
                            }
                            else
                            {
                                JOptionPane.showMessageDialog(null, "¡Debe ingresar un modelo!");
                                t_modelo.requestFocus();
                            }
                        }
                        else
                        {
                            JOptionPane.showMessageDialog(null, "¡Debe seleccionar una marca!");
                            t_marca.requestFocus();
                        }
                    }
                    else
                    {
                        JOptionPane.showMessageDialog(null, "¡Debe seleccionar un tipo de unidad!");
                        t_tipo.requestFocus();
                    }
                }
                else
                {
                    JOptionPane.showMessageDialog(null, "¡Debe guardar primero el cliente!");
                    b_guardar_cliente.requestFocus();
                }
            }
            else
            {
                JOptionPane.showMessageDialog(null, "¡Debe introducir un cliente!");
                b_busca_cliente.requestFocus();
            }   
        }
        else
        {
            JOptionPane.showMessageDialog(null, "¡Debe introducir numero de aseguradora!");
            t_aseguradora.requestFocus();
        }
    }
    else
            consultaOrden();
    }//GEN-LAST:event_b_guardarActionPerformed

    private void b_buscar_aseguradoraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_buscar_aseguradoraActionPerformed
        // TODO add your handling code here:
        h=new Herramientas(usr, menu);
        h.session(sessionPrograma);
        
        buscaCompania obj = new buscaCompania(new javax.swing.JFrame(), true, this.sessionPrograma, this.usr);
        obj.t_busca.requestFocus();
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        obj.setLocation((d.width/2)-(obj.getWidth()/2), (d.height/2)-(obj.getHeight()/2));
        obj.setVisible(true);
        
        Compania actor=obj.getReturnStatus();
        if(actor!=null)
        {
            t_aseguradora.setText(actor.getIdCompania().toString());
            l_nombre_aseguradora.setText(actor.getNombre());
        }
        else
        {
            t_aseguradora.setText("");
            l_nombre_aseguradora.setText("Seleccione una aseguradora");
        }
    }//GEN-LAST:event_b_buscar_aseguradoraActionPerformed

    private void b_cancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_cancelarActionPerformed
       h= new Herramientas(usr, menu);
       h.desbloqueaOrden();
       int opt=JOptionPane.showConfirmDialog(this, "¡Los datos capturados se eliminaran!");
       if(opt==0)
       {
           borra_cajas();
            estado_campos(true, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false);
            l_nombre_modificado.setText("");
            id_modificado.setText("");
            t_orden.setText("");
            p_ventanas.setSelectedIndex(0);
       }
    }//GEN-LAST:event_b_cancelarActionPerformed

    private void p_fotoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_p_fotoMouseClicked
        if(orden_act!=null && cerrada==0 && modifica==1)
        {
            JFileChooser selector=new JFileChooser();
            selector.setFileFilter(new ExtensionFileFilter("JPG and JPEG", new String[] { "JPG", "JPEG" }));
            int estado=1;
            estado=selector.showOpenDialog(null);
            if(estado==0)
            {
                archivo=selector.getSelectedFile();
                try
                {
                    if(archivo.exists())
                    {
                        String ruta=archivo.getPath();
                        p_foto.removeAll();
                        p_foto.add(new Imagen(ruta, 83, 93, 1, 1, 83, 93));
                        p_foto.repaint();
                        entro_foto=1;
                    }
                    else
                        javax.swing.JOptionPane.showMessageDialog(null, "No se pudo cargar la imagen");
                }catch(Exception e){}
            }
        }
    }//GEN-LAST:event_p_fotoMouseClicked

    private void t_aseguradoraKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_aseguradoraKeyTyped
        char car = evt.getKeyChar();
        if(t_aseguradora.getText().length()>=4) 
            evt.consume();
        if((car<'0' || car>'9')) 
            evt.consume();
    }//GEN-LAST:event_t_aseguradoraKeyTyped

    private void t_siniestroKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_siniestroKeyTyped
        char car = evt.getKeyChar();
        if(t_siniestro.getText().length()>=10) 
            evt.consume();
    }//GEN-LAST:event_t_siniestroKeyTyped

    private void t_polizaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_polizaKeyTyped
        char car = evt.getKeyChar();
        if(t_poliza.getText().length()>=15) 
            evt.consume();
    }//GEN-LAST:event_t_polizaKeyTyped

    private void t_reporteKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_reporteKeyTyped
        char car = evt.getKeyChar();
        if(t_reporte.getText().length()>=19) 
            evt.consume();
    }//GEN-LAST:event_t_reporteKeyTyped

    private void t_incisoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_incisoKeyTyped
        char car = evt.getKeyChar();
        if(t_inciso.getText().length()>=10) 
            evt.consume();
    }//GEN-LAST:event_t_incisoKeyTyped

    private void t_nombre_clienteKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_nombre_clienteKeyTyped
        evt.setKeyChar(Character.toUpperCase(evt.getKeyChar()));
        char car = evt.getKeyChar();
        if(t_nombre_cliente.getText().length()>=150) 
            evt.consume();
    }//GEN-LAST:event_t_nombre_clienteKeyTyped

    private void t_direccion_clienteKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_direccion_clienteKeyTyped
        evt.setKeyChar(Character.toUpperCase(evt.getKeyChar()));
        char car = evt.getKeyChar();
        if(t_direccion_cliente.getText().length()>=150) 
            evt.consume();
    }//GEN-LAST:event_t_direccion_clienteKeyTyped

    private void t_colonia_clienteKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_colonia_clienteKeyTyped
        evt.setKeyChar(Character.toUpperCase(evt.getKeyChar()));
        char car = evt.getKeyChar();
        if(t_colonia_cliente.getText().length()>=150) 
            evt.consume();
    }//GEN-LAST:event_t_colonia_clienteKeyTyped

    private void t_rfc_clienteKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_rfc_clienteKeyTyped
        evt.setKeyChar(Character.toUpperCase(evt.getKeyChar()));
        char car = evt.getKeyChar();
        if(t_rfc_cliente.getText().length()>=13) 
            evt.consume();
    }//GEN-LAST:event_t_rfc_clienteKeyTyped

    private void t_poblacion_clienteKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_poblacion_clienteKeyTyped
        evt.setKeyChar(Character.toUpperCase(evt.getKeyChar()));
        char car = evt.getKeyChar();
        if(t_poblacion_cliente.getText().length()>=150) 
            evt.consume();
    }//GEN-LAST:event_t_poblacion_clienteKeyTyped

    private void t_email_clienteKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_email_clienteKeyTyped
        char car = evt.getKeyChar();
        if(t_email_cliente.getText().length()>=100) 
            evt.consume();
    }//GEN-LAST:event_t_email_clienteKeyTyped

    private void t_ordenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_t_ordenActionPerformed
        if(t_orden.getText().compareTo("")!=0)
        {
            orden_act=buscarOrden(Integer.parseInt(t_orden.getText()));
            if(this.p_ventanas.getSelectedIndex()==0)
                consultaOrden();
            else
            {
                if(orden_act==null)
                {
                    t_orden.setText("");
                    borra_cajas();
                    estado_campos(true, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false);
                    l_nombre_modificado.setText("");
                    id_modificado.setText("");
                    JOptionPane.showMessageDialog(null, "¡El numero de orden no existe!");
                    t_orden.requestFocus();
                    modifica=0;
                    cerrada=0;
                    this.orden_act=null;
                }
                p_ventanas.setSelectedIndex(0);
            }
        }
        else
        {
            h= new Herramientas(usr, menu);
            h.desbloqueaOrden();
            t_orden.setText("");
            borra_cajas();
            estado_campos(true, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false);
            l_nombre_modificado.setText("");
            id_modificado.setText("");
            t_orden.requestFocus();
            this.cerrada=0;
            this.modifica=0;
            this.orden_act=null;
            p_ventanas.setSelectedIndex(0);
        }
    }//GEN-LAST:event_t_ordenActionPerformed

    private void t_ordenKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_ordenKeyTyped
        char car = evt.getKeyChar();
        if(t_orden.getSelectedText()!=null)
            t_orden.setText(t_orden.getText().replace(t_orden.getSelectedText(), ""));
        
        if(t_orden.getText().length()>=6)
        {
            evt.consume();
        }
        if((car<'0' || car>'9')) 
            evt.consume();
    }//GEN-LAST:event_t_ordenKeyTyped

    private void p_ventanasStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_p_ventanasStateChanged
        h=new Herramientas(usr, menu);
        h.session(sessionPrograma);
        if(orden_act!=null)
        {
            t_orden.setText(""+orden_act.getIdOrden());
            String ventana=p_ventanas.getTitleAt(p_ventanas.getSelectedIndex());          
            if(ventana.compareTo("Unidad")==0)
                consultaOrden(); 
            if(ventana.compareTo("Seguimiento")==0)
            {
                estado_campos(true, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false);
                p_responsables.getViewport().removeAll();
                p_responsables.updateUI();
                try
                {
                    if(usr.getConsultarSeguimiento()==true)
                    {
                        Responsables r1=new Responsables(t_orden.getText(), usr, estado, this.sessionPrograma);
                        p_responsables.setViewportView(r1);
                        p_responsables.updateUI();
                        r1=null;
                    }
                    else
                        JOptionPane.showMessageDialog(null, "¡Acceso denegado!");
                }catch(Exception e)
                {
                    System.out.println(e);
                }
            }
            if(ventana.compareTo("Formatos")==0)
            {
                estado_campos(true, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false);
                p_formatos.removeAll();
                p_formatos.repaint();
                p_formatos.updateUI();
                try
                {
                    if(usr.getConsultaFormatos()==true)
                    {
                        formatos r1=new formatos(t_orden.getText(), usr, t_aseguradora.getText(), estado, this.sessionPrograma);
                        p_formatos.add(r1);
                        r1.setVisible(true);
                        p_formatos.repaint();
                        p_formatos.updateUI();
                        r1=null;
                    }
                    else
                        JOptionPane.showMessageDialog(null, "¡Acceso denegado!");
                }catch(Exception e)
                {
                    System.out.println(e);
                }
            }
            if(ventana.compareTo("Agenda de Cliente")==0)
            {
                estado_campos(true, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false);
                p_agenda.removeAll();
                p_agenda.repaint();
                p_agenda.updateUI();
                try
                {
                    if(usr.getConsultaAgenda()==true)
                    {
                        AgendaCliente r1=new AgendaCliente(t_orden.getText(), this.sessionPrograma, usr, estado);
                        p_agenda.removeAll();
                        p_agenda.add(r1);
                        r1.setVisible(true);
                        p_agenda.repaint();
                        p_agenda.updateUI();
                        r1=null;
                    }
                    else
                        JOptionPane.showMessageDialog(null, "¡Acceso denegado!");
                }catch(Exception e)
                {
                    System.out.println(e);
                }
            }
            
            if(ventana.compareTo("Inventario")==0)
            {
                estado_campos(true, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false);
                p_inventario.getViewport().removeAll();
                p_inventario.updateUI();
                try
                {
                    if(usr.getConsultaInventario()==true)
                    {
                        Inventarios r1=new Inventarios(t_orden.getText(), usr, estado, sessionPrograma);
                        p_inventario.getViewport().removeAll();
                        p_inventario.setViewportView(r1);
                        p_inventario.updateUI();
                        r1=null;
                    }
                    else
                        JOptionPane.showMessageDialog(null, "¡Acceso denegado!");
                }catch(Exception e)
                {
                    System.out.println(e);
                }
            }
            
            if(ventana.compareTo("Galeria")==0)
            {
                estado_campos(true, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false);
                p_galeria.removeAll();
                p_galeria.repaint();
                p_galeria.updateUI();
                try
                {
                    if(usr.getConsultaGaleria()==true)
                    {
                        Galeria r1;
                        if(cerrada==1)
                            r1=new Galeria(t_orden.getText(), usr, "cerrada", this.sessionPrograma);
                        else
                            r1=new Galeria(t_orden.getText(), usr, estado, this.sessionPrograma);
                        p_galeria.add(r1);
                        r1.setVisible(true);
                        p_galeria.repaint();
                        p_galeria.updateUI();
                        r1=null;
                    }
                    else
                        JOptionPane.showMessageDialog(null, "¡Acceso denegado!");
                }catch(Exception e)
                {
                    System.out.println(e);
                }
            }
            if(ventana.compareTo("Documentos")==0)
            {
                estado_campos(true, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false);
                p_documentos.removeAll();
                p_documentos.repaint();
                p_documentos.updateUI();
                try
                {
                    if(usr.getConsultaDocumentos()==true)
                    {
                        ArchivoOrden r1;
                        if(cerrada==1)
                            r1=new ArchivoOrden(t_orden.getText(), usr, "cerrada", this.sessionPrograma, ruta);
                        else
                            r1=new ArchivoOrden(t_orden.getText(), usr, estado, this.sessionPrograma, ruta);

                        p_documentos.removeAll();
                        p_documentos.add(r1);
                        r1.setVisible(true);
                        p_documentos.repaint();
                        p_documentos.updateUI();
                        r1=null;
                    }
                    else
                        JOptionPane.showMessageDialog(null, "¡Acceso denegado!");
                }catch(Exception e)
                {
                    System.out.println(e);
                }
            }
            if(ventana.compareTo("Observaciones")==0)
            {
                estado_campos(true, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false);
                p_observaciones.removeAll();
                p_observaciones.repaint();
                p_observaciones.updateUI();
                Session session = HibernateUtil.getSessionFactory().openSession();
                try
                {
                    session.beginTransaction().begin();
                    usr = (Usuario)session.get(Usuario.class, usr.getIdUsuario());
                    if(usr.getConsultaObservaciones()==true)
                    {
                        Observacion r1=new Observacion(t_orden.getText(), usr, estado, this.sessionPrograma);
                        p_observaciones.removeAll();
                        p_observaciones.add(r1);
                        r1.setVisible(true);
                        p_observaciones.repaint();
                        p_observaciones.updateUI();
                        r1=null;
                    }
                    else
                        JOptionPane.showMessageDialog(null, "¡Acceso denegado!");
                }catch(Exception e)
                {
                    System.out.println(e);
                }
                if(session!=null)
                    if(session.isOpen())
                    {
                        session.flush();
                        session.clear();
                        session.close();
                    }
            }
            if(ventana.compareTo("Levantamiento")==0)
            {
                estado_campos(true, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false);
                p_levantamiento.getViewport().removeAll();
                p_levantamiento.updateUI();
                Session session = HibernateUtil.getSessionFactory().openSession();
                try
                {
                    session.beginTransaction().begin();
                    usr = (Usuario)session.get(Usuario.class, usr.getIdUsuario());
                    orden_act=(Orden)session.get(Orden.class, orden_act.getIdOrden());
                    if(usr.getConsultaLevantamiento()==true)
                    {
                        if(orden_act.getRLevantamientoInicio()!=null)
                        {
                            p_levantamiento.getViewport().removeAll();
                            r2=null;
                            r2=new valuacionDirecta(t_orden.getText(), usr, estado, this.sessionPrograma);
                            p_levantamiento.setViewportView(r2);
                            p_levantamiento.updateUI();
                            r1=null;
                        }
                        else
                        {
                            p_levantamiento.getViewport().removeAll();
                            p_levantamiento.updateUI();
                            JOptionPane.showMessageDialog(null, "¡Aun no esta disponible!");
                        }
                    }
                    else
                        JOptionPane.showMessageDialog(null, "¡Acceso denegado!");
                    }catch(Exception e)
                    {
                        System.out.println(e);
                    }
                    if(session!=null)
                        if(session.isOpen())
                        {
                            session.flush();
                            session.clear();
                            session.close();
                        }
            }
            
            if(ventana.compareTo("Valuacion")==0)
            {
                estado_campos(true, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false);
                p_valuacion.getViewport().removeAll();
                p_valuacion.updateUI();
                Session session = HibernateUtil.getSessionFactory().openSession();
                try
                {
                    session.beginTransaction().begin();
                    usr = (Usuario)session.get(Usuario.class, usr.getIdUsuario());
                    orden_act=(Orden)session.get(Orden.class, orden_act.getIdOrden());
                    if(usr.getConsultaValuacion()==true)
                    {
                        if(orden_act.getRLevantamientoInicio()!=null)
                        {
                            p_valuacion.getViewport().removeAll();
                            r1=null;
                            r1=new valuacion(t_orden.getText(), usr, estado, this.sessionPrograma, menu);
                            p_valuacion.setViewportView(r1);
                            p_valuacion.updateUI();
                            r1=null;
                        }
                        else
                        {
                            p_valuacion.getViewport().removeAll();
                            p_valuacion.updateUI();
                            JOptionPane.showMessageDialog(null, "¡Aun no esta disponible!");
                        }
                    }
                    else
                        JOptionPane.showMessageDialog(null, "¡Acceso denegado!");
                }catch(Exception e)
                {
                    System.out.println(e);
                }
                if(session!=null)
                    if(session.isOpen())
                    {
                        session.flush();
                            session.clear();
                        session.close();
                    }
            }
            
            if(ventana.compareTo("Autorización")==0)
            {
                estado_campos(true, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false);
                p_autoriza_valuacion.getViewport().removeAll();
                p_autoriza_valuacion.updateUI();
                try
                {
                    if(usr.getEditaAutorizaPartida()==true || usr.getEditaHoras())
                    {
                        if(orden_act.getRLevantamientoInicio()!=null)
                        {
                            Autorizacion r1=new Autorizacion(t_orden.getText(), usr, estado, this.sessionPrograma);
                            p_autoriza_valuacion.setViewportView(r1);
                            p_autoriza_valuacion.updateUI();
                            r1=null;
                        }
                        else
                        {
                            p_autoriza_valuacion.getViewport().removeAll();
                            p_autoriza_valuacion.updateUI();
                            JOptionPane.showMessageDialog(null, "¡Aun no esta disponible!");
                        }
                    }
                    else
                        JOptionPane.showMessageDialog(null, "¡Acceso denegado!");
                }catch(Exception e)
                {
                    System.out.println(e);
                }
            }
            
            if(ventana.compareTo("Genera Cotización")==0)
            {
                estado_campos(true, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false);
                p_genera_cotizacion.getViewport().removeAll();
                p_genera_cotizacion.updateUI();
                try
                {
                    if(usr.getEditarCotizaciones()==true)
                    {
                        if(orden_act.getRCotizaInicio()!=null)
                        {
                            generaCotizacion r1=new generaCotizacion(t_orden.getText(), usr, estado, this.sessionPrograma);
                            p_genera_cotizacion.setViewportView(r1);
                            p_genera_cotizacion.updateUI();
                            r1=null;
                        }
                        else
                        {
                            p_genera_cotizacion.getViewport().removeAll();
                            p_genera_cotizacion.updateUI();
                            JOptionPane.showMessageDialog(null, "¡Aun no esta disponible!");
                        }
                    }
                    else
                        JOptionPane.showMessageDialog(null, "¡Acceso denegado!");
                }catch(Exception e)
                {
                    System.out.println(e);
                }
            }
            if(ventana.compareTo("Pedidos")==0)
            {
                estado_campos(true, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false);
                p_genera_pedidos.getViewport().removeAll();
                p_genera_pedidos.updateUI();
                try
                {
                    if(usr.getGeneraPedidos()==true || usr.getEditaPedidos()==true | usr.getEliminaPedidos()==true || usr.getAutorizarPedidos()==true ||usr.getGeneraPedidos()==true ||usr.getCerrarCompras()==true)
                    {
                        if(orden_act.getInicioRefacciones()!=null)
                        {
                            altaCompras r1=new altaCompras(t_orden.getText(), usr, estado, this.sessionPrograma);
                            p_genera_pedidos.setViewportView(r1);
                            p_genera_pedidos.updateUI();
                            r1=null;
                        }
                        else
                        {
                            p_genera_pedidos.getViewport().removeAll();
                            p_genera_pedidos.updateUI();
                            JOptionPane.showMessageDialog(null, "¡Aun no esta disponible!");
                        }
                    }
                    else
                        JOptionPane.showMessageDialog(null, "¡Acceso denegado!");
                }catch(Exception e)
                {
                    e.printStackTrace();
                }
            }
            if(ventana.compareTo("Avance de Pedidos")==0)
            {
                estado_campos(true, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false);
                p_avance_pedidos.getViewport().removeAll();
                p_avance_pedidos.updateUI();
                try
                {
                    if(usr.getGeneraPedidos()==true || usr.getEditaPedidos()==true | usr.getEliminaPedidos()==true || usr.getAutorizarPedidos()==true ||usr.getGeneraPedidos()==true ||usr.getCerrarCompras()==true || usr.getConsultaPedidos())
                    {
                        if(orden_act.getInicioRefacciones()!=null)
                        {
                            avanceSurtido r1=new avanceSurtido(t_orden.getText(), usr, this.sessionPrograma);
                            p_avance_pedidos.setViewportView(r1);
                            p_avance_pedidos.updateUI();
                            r1=null;
                        }
                        else
                        {
                            p_avance_pedidos.getViewport().removeAll();
                            p_avance_pedidos.updateUI();
                            JOptionPane.showMessageDialog(null, "¡Aun no esta disponible!");
                        }
                    }
                    else
                        JOptionPane.showMessageDialog(null, "¡Acceso denegado!");
                }catch(Exception e)
                {
                    System.out.println(e);
                }
            }
            if(ventana.compareTo("Compara Cotizaciones")==0)
            {
                estado_campos(true, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false);
                p_compara_cotizaciones.getViewport().removeAll();
                p_compara_cotizaciones.updateUI();
                try
                {
                    if(usr.getGeneraPedidos()==true)
                    {
                        if(orden_act.getRCotizaInicio()!=null)
                        {
                            ComparaCotizacion r1=new ComparaCotizacion(t_orden.getText(), usr, this.sessionPrograma, this.orden_act, this.estado);
                            p_compara_cotizaciones.setViewportView(r1);
                            p_compara_cotizaciones.updateUI();
                            r1=null;
                        }
                        else
                        {
                            p_compara_cotizaciones.getViewport().removeAll();
                            p_compara_cotizaciones.updateUI();
                            JOptionPane.showMessageDialog(null, "¡Aun no esta disponible!");
                        }
                    }
                    else
                        JOptionPane.showMessageDialog(null, "¡Acceso denegado!");
                }catch(Exception e)
                {
                    System.out.println(e);
                }
            }
            if(ventana.compareTo("Pre-Factura")==0)
            {
                estado_campos(true, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false);
                p_preFacura.getViewport().removeAll();
                p_preFacura.updateUI();
                try
                {
                        PreFactura r1=new PreFactura(t_orden.getText(), this.usr, this.estado, this.sessionPrograma, this.menu);
                        p_preFacura.setViewportView(r1);
                        p_preFacura.updateUI();
                        r1=null;
                }catch(Exception e)
                {
                    System.out.println(e);
                }
            }
            if(ventana.compareTo("Destajo")==0)
            {
                estado_campos(true, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false);
                p_destajo.getViewport().removeAll();
                p_destajo.updateUI();
                try
                {
                    if(usr.getAltaMetas()==true)
                    {
                        Destajo r1=new Destajo(t_orden.getText(), usr, estado, this.sessionPrograma);
                        p_destajo.setViewportView(r1);
                        p_destajo.updateUI();
                        r1=null;
                    }
                    else
                        JOptionPane.showMessageDialog(null, "¡Acceso denegado!");
                }catch(Exception e)
                {
                    System.out.println(e);
                }
            }
            
            if(ventana.compareTo("Conciliacion")==0)
            {
                estado_campos(true, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false);
                p_conciliacion.getViewport().removeAll();
                p_conciliacion.updateUI();
                try
                {
                    if(usr.getGeneraPedidos()==true || usr.getEditaPedidos()==true | usr.getEliminaPedidos()==true || usr.getAutorizarPedidos()==true ||usr.getGeneraPedidos()==true ||usr.getCerrarCompras()==true)
                    {
                        if(orden_act.getInicioRefacciones()!=null)
                        {
                            Conciliacion r1=new Conciliacion(t_orden.getText(), usr, this.sessionPrograma);
                            p_conciliacion.setViewportView(r1);
                            p_conciliacion.updateUI();
                            r1=null;
                        }
                        else
                        {
                            p_conciliacion.getViewport().removeAll();
                            p_conciliacion.updateUI();
                            JOptionPane.showMessageDialog(null, "¡Aun no esta disponible!");
                        }
                    }
                    else
                        JOptionPane.showMessageDialog(null, "¡Acceso denegado!");
                }catch(Exception e)
                {
                    System.out.println(e);
                }
            }
            if(ventana.compareTo("SM Logistics")==0)
            {
                estado_campos(true, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false);
                p_sm.removeAll();
                p_sm.repaint();
                p_sm.updateUI();
                try
                {
                    //if(usr.getConsultaFormatos()==true)
                    //{
                        SmLogistics r1=new SmLogistics(t_orden.getText(), usr, estado, this.sessionPrograma, ruta);
                        p_sm.add(r1);
                        r1.setVisible(true);
                        p_sm.repaint();
                        p_sm.updateUI();
                        r1=null;
                    //}
                    //else
                        //JOptionPane.showMessageDialog(null, "¡Acceso denegado!");
                }catch(Exception e)
                {
                    System.out.println(e);
                }
            }
        }
        else
        {
            p_responsables.getViewport().removeAll();
            p_responsables.updateUI();
            
            p_formatos.removeAll();
            p_formatos.repaint();
            p_formatos.updateUI();
            
            p_galeria.removeAll();
            p_galeria.repaint();
            p_galeria.updateUI();
            
            p_documentos.removeAll();
            p_documentos.repaint();
            p_documentos.updateUI();
                
            p_observaciones.removeAll();
            p_observaciones.repaint();
            p_observaciones.updateUI();
            
            p_agenda.removeAll();
            p_agenda.repaint();
            p_agenda.updateUI();
            
            p_levantamiento.getViewport().removeAll();
            p_levantamiento.updateUI();
            
            p_valuacion.getViewport().removeAll();
            p_valuacion.updateUI();
            
            p_autoriza_valuacion.getViewport().removeAll();
            p_autoriza_valuacion.updateUI();
            
            p_genera_cotizacion.getViewport().removeAll();
            p_genera_cotizacion.updateUI();
            
            p_genera_pedidos.getViewport().removeAll();
            p_genera_pedidos.updateUI();
            
            p_avance_pedidos.getViewport().removeAll();
            p_avance_pedidos.updateUI();
            
            p_inventario.getViewport().removeAll();
            p_inventario.updateUI();            
            
            p_compara_cotizaciones.getViewport().removeAll();
            p_compara_cotizaciones.updateUI();
            
            p_preFacura.getViewport().removeAll();
            p_preFacura.updateUI();
        }
    }//GEN-LAST:event_p_ventanasStateChanged

    private void b_busca_ordenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_busca_ordenActionPerformed
        h=new Herramientas(usr, menu);
        h.session(sessionPrograma);
        buscaOrden obj = new buscaOrden(new javax.swing.JFrame(), true, this.usr,0);
        obj.t_busca.requestFocus();
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        obj.setLocation((d.width/2)-(obj.getWidth()/2), (d.height/2)-(obj.getHeight()/2));
        obj.setVisible(true);        
        orden_act=obj.getReturnStatus();
        if (orden_act!=null)
        {
            this.t_orden.setText(""+orden_act.getIdOrden());
            if(this.p_ventanas.getSelectedIndex()==0)
                consultaOrden();
            else
                p_ventanas.setSelectedIndex(0);
        }
        else
        {
             h.desbloqueaOrden();
            t_orden.setText("");
            borra_cajas();
            estado_campos(true, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false);
            l_nombre_modificado.setText("");
            id_modificado.setText("");
            t_orden.requestFocus();      
            p_ventanas.setSelectedIndex(0);
        }
    }//GEN-LAST:event_b_busca_ordenActionPerformed

    private void cb_terceroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cb_terceroActionPerformed
        // TODO add your handling code here:
        if(cb_tercero.isSelected())
        {
            cb_asegurado.setSelected(false);
            cb_tercero_asegurado.setSelected(false);
            cb_particular.setSelected(false);
        }
        else
            cb_tercero.setSelected(true);
        this.b_busca_cliente.requestFocus();
    }//GEN-LAST:event_cb_terceroActionPerformed

    private void cb_tercero_aseguradoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cb_tercero_aseguradoActionPerformed
        // TODO add your handling code here:
        if(cb_tercero_asegurado.isSelected())
        {
            cb_tercero.setSelected(false);
            cb_asegurado.setSelected(false);
            cb_particular.setSelected(false);
        }
        else
            cb_tercero_asegurado.setSelected(true);
        this.b_busca_cliente.requestFocus();
    }//GEN-LAST:event_cb_tercero_aseguradoActionPerformed

    private void cb_aseguradoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cb_aseguradoActionPerformed
        // TODO add your handling code here:
        if(cb_asegurado.isSelected())
        {
            cb_tercero.setSelected(false);
            cb_tercero_asegurado.setSelected(false);
            cb_particular.setSelected(false);
        }
        else
            cb_asegurado.setSelected(true);
        this.b_busca_cliente.requestFocus();
    }//GEN-LAST:event_cb_aseguradoActionPerformed

    private void b_busca_clienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_busca_clienteActionPerformed
        // TODO add your handling code here:
        h=new Herramientas(usr, 0);
        h.session(sessionPrograma);

        buscaCliente obj = new buscaCliente(new javax.swing.JFrame(), true);
        obj.t_busca.requestFocus();
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        obj.setLocation((d.width/2)-(obj.getWidth()/2), (d.height/2)-(obj.getHeight()/2));
        obj.setVisible(true);

        Clientes actor=obj.getReturnStatus();
        if(actor!=null)
        {
            t_nombre_cliente.setText("");
            t_direccion_cliente.setText("");
            t_colonia_cliente.setText("");;
            t_cp_cliente.setText("");;
            t_cp_cliente.setValue(null);
            t_rfc_cliente.setText("");;
            t_poblacion_cliente.setText("");;
            c_estado_cliente.setSelectedIndex(0);
            t_telefono_cliente.setText("");
            t_telefono_cliente.setValue(null);
            t_email_cliente.setText("");
            l_id_cliente.setText("");
            t_contacto_cliente.setText("");
            t_nextel_cliente.setText("");
        
            l_id_cliente.setText(actor.getIdClientes().toString());
            t_nombre_cliente.setText(actor.getNombre());
            try{
                t_direccion_cliente.setText(actor.getDireccion());
            }catch(Exception e){}

            try{
                t_colonia_cliente.setText(actor.getColonia());
            }catch(Exception e){}

            try{
                t_cp_cliente.setText(actor.getCp().toString());
            }catch(Exception e){}

            try{
                t_rfc_cliente.setText(actor.getRfc());
            }catch(Exception e){}

            try{
                t_poblacion_cliente.setText(actor.getPoblacion());
            }catch(Exception e){}

            try{
                t_telefono_cliente.setText(actor.getTelefono());
            }catch(Exception e){}

            try{
                t_email_cliente.setText(actor.getEmail());
            }catch(Exception e){}
            
            try{
                t_contacto_cliente.setText(actor.getContacto());
            }catch(Exception e){}
            
            try{
                t_nextel_cliente.setText(actor.getNextel());
            }catch(Exception e){}

            c_estado_cliente.setSelectedItem(actor.getEstado());
            t_nombre_cliente.setEditable(false);
            t_direccion_cliente.setEditable(false);
            t_colonia_cliente.setEditable(false);
            t_cp_cliente.setEditable(false);
            t_rfc_cliente.setEditable(false);
            t_poblacion_cliente.setEditable(false);
            c_estado_cliente.setEnabled(false);
            t_telefono_cliente.setEditable(false);
            t_email_cliente.setEditable(false);
            t_contacto_cliente.setEditable(false);
            t_nextel_cliente.setEditable(false);
            b_nuevo_cliente.setEnabled(true);
            b_guardar_cliente.setEnabled(false);
        }

    }//GEN-LAST:event_b_busca_clienteActionPerformed

    private void cb_particularActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cb_particularActionPerformed
        // TODO add your handling code here:
        if(cb_particular.isSelected())
        {
            cb_asegurado.setSelected(false);
            cb_tercero_asegurado.setSelected(false);
            cb_tercero.setSelected(false);
        }
        else
            cb_particular.setSelected(true);
        this.b_busca_cliente.requestFocus();
    }//GEN-LAST:event_cb_particularActionPerformed

    private void b_guardar_clienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_guardar_clienteActionPerformed
        // TODO add your handling code here:
        h=new Herramientas(usr, 0);
        h.session(sessionPrograma);

        if(t_nombre_cliente.getText().compareTo("")==0)
            JOptionPane.showMessageDialog(null, "¡Debe introducir el nombre del cliente!");
        else
        {
            if(this.t_email_cliente.getText().compareTo("")==0)
            {
                JOptionPane.showMessageDialog(null, "¡Debe introducir el email del cliente!");
                t_email_cliente.requestFocus();
            }
            else
            {
                if(consultaCliente(t_nombre_cliente.getText())==false)
                {
                    Clientes nuevoCliente = new Clientes();
                    if(t_nombre_cliente.getText().compareTo("")!=0)
                        nuevoCliente.setNombre(t_nombre_cliente.getText());
                    if(t_direccion_cliente.getText().compareTo("")!=0)
                        nuevoCliente.setDireccion(t_direccion_cliente.getText());
                    if(t_colonia_cliente.getText().compareTo("")!=0)
                        nuevoCliente.setColonia(t_colonia_cliente.getText());
                    if(t_cp_cliente.getText().compareTo("")!=0)
                        nuevoCliente.setCp(Integer.parseInt(t_cp_cliente.getText()));
                    if(t_rfc_cliente.getText().compareTo("")!=0)
                        nuevoCliente.setRfc(t_rfc_cliente.getText());
                    if(t_poblacion_cliente.getText().compareTo("")!=0)
                        nuevoCliente.setPoblacion(t_poblacion_cliente.getText());
                    if(c_estado_cliente.getSelectedItem().toString().compareTo("")!=0)
                        nuevoCliente.setEstado(c_estado_cliente.getSelectedItem().toString());
                    if(t_telefono_cliente.getText().compareTo("")!=0)
                        nuevoCliente.setTelefono(t_telefono_cliente.getText());
                    if(t_email_cliente.getText().compareTo("")!=0)
                        nuevoCliente.setEmail(t_email_cliente.getText());
                    if(t_contacto_cliente.getText().compareTo("")!=0)
                        nuevoCliente.setContacto(t_contacto_cliente.getText());
                    if(t_nextel_cliente.getText().compareTo("")!=0)
                        nuevoCliente.setNextel(t_nextel_cliente.getText());
                    boolean respuesta=guardarCliente(nuevoCliente);
                    if(respuesta==true)
                    {
                        t_nombre_cliente.setEditable(false);
                        t_direccion_cliente.setEditable(false);
                        t_colonia_cliente.setEditable(false);
                        t_cp_cliente.setEditable(false);
                        t_rfc_cliente.setEditable(false);
                        t_poblacion_cliente.setEditable(false);
                        c_estado_cliente.setEnabled(false);
                        t_telefono_cliente.setEditable(false);
                        t_email_cliente.setEditable(false);
                        t_contacto_cliente.setEditable(false);
                        t_nextel_cliente.setEditable(false);
                        b_nuevo_cliente.setEnabled(true);
                        b_guardar_cliente.setEnabled(false);
                        JOptionPane.showMessageDialog(null, "¡El cliente se ha guardado!");
                        cb_asegurado.requestFocus();
                    }
                    else
                    {
                        JOptionPane.showMessageDialog(null, "¡Error al guardar!");
                        t_tipo.requestFocus();
                    }
                }
                else
                {
                    JOptionPane.showMessageDialog(null, "¡En nombre del cliente ya existe!");
                    t_nombre_cliente.requestFocus();
                }
            }
        }
    }//GEN-LAST:event_b_guardar_clienteActionPerformed

    private void b_nuevo_clienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_nuevo_clienteActionPerformed
        // TODO add your handling code here:
        h=new Herramientas(usr, 0);
        h.session(sessionPrograma);
        t_nombre_cliente.setText("");
        t_direccion_cliente.setText("");
        t_colonia_cliente.setText("");;
        t_cp_cliente.setText("");;
        t_cp_cliente.setValue(null);
        t_rfc_cliente.setText("");;
        t_poblacion_cliente.setText("");;
        c_estado_cliente.setSelectedIndex(0);
        t_telefono_cliente.setText("");
        t_telefono_cliente.setValue(null);
        t_email_cliente.setText("");
        l_id_cliente.setText("");
        t_contacto_cliente.setText("");
        t_nextel_cliente.setText("");
        t_nombre_cliente.setEditable(true);
        t_direccion_cliente.setEditable(true);
        t_colonia_cliente.setEditable(true);
        t_cp_cliente.setEditable(true);
        t_rfc_cliente.setEditable(true);
        t_poblacion_cliente.setEditable(true);
        c_estado_cliente.setEnabled(true);
        t_telefono_cliente.setEditable(true);
        t_email_cliente.setEditable(true);
        t_contacto_cliente.setEditable(true);
        t_nextel_cliente.setEditable(true);
        b_nuevo_cliente.setEnabled(false);
        b_guardar_cliente.setEnabled(true);
        t_nombre_cliente.requestFocus();
    }//GEN-LAST:event_b_nuevo_clienteActionPerformed

    private void t_contacto_clienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_t_contacto_clienteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_t_contacto_clienteActionPerformed

    private void t_contacto_clienteKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_contacto_clienteKeyTyped
        // TODO add your handling code here:
        evt.setKeyChar(Character.toUpperCase(evt.getKeyChar()));
        if(t_contacto_cliente.getText().length()>=150) 
            evt.consume();
    }//GEN-LAST:event_t_contacto_clienteKeyTyped

    private void t_nextel_clienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_t_nextel_clienteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_t_nextel_clienteActionPerformed

    private void t_nextel_clienteKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_nextel_clienteKeyTyped
        // TODO add your handling code here:
        evt.setKeyChar(Character.toUpperCase(evt.getKeyChar()));
        if(t_nextel_cliente.getText().length()>=13) 
            evt.consume();
    }//GEN-LAST:event_t_nextel_clienteKeyTyped

    private void t_tipoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_t_tipoActionPerformed
        // TODO add your handling code here:
        t_tipo.setText(t_tipo.getText().toUpperCase());
        b_tipo.requestFocus();
    }//GEN-LAST:event_t_tipoActionPerformed

    private void t_tipoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_t_tipoFocusLost
        // TODO add your handling code here:
        if(t_tipo.getText().length()>10)
        {
            t_tipo.setText(t_tipo.getText().substring(0, 10));
        }
        buscaTipo();
    }//GEN-LAST:event_t_tipoFocusLost

    private void t_tipoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_tipoKeyTyped
        // TODO add your handling code here:
        char car = evt.getKeyChar();
        if(t_tipo.getText().length()>=10)
        evt.consume();
    }//GEN-LAST:event_t_tipoKeyTyped

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
            t_tipo.setText(actor);
        else
            t_tipo.setText("");
    }//GEN-LAST:event_b_tipoActionPerformed

    private void t_marcaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_t_marcaActionPerformed
        t_marca.setText(t_marca.getText().toUpperCase());
        t_motor.requestFocus();
    }//GEN-LAST:event_t_marcaActionPerformed

    private void t_marcaFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_t_marcaFocusLost
        if(t_marca.getText().length()>4)
        {
            t_marca.setText(t_marca.getText().substring(0, 4));
        }
        buscaMarca();
    }//GEN-LAST:event_t_marcaFocusLost

    private void t_marcaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_marcaKeyTyped
        char car = evt.getKeyChar();
        if(t_marca.getText().length()>=4)
            evt.consume();
    }//GEN-LAST:event_t_marcaKeyTyped

    private void b_marcaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_marcaActionPerformed
        h=new Herramientas(usr, 0);
        h.session(sessionPrograma);
        buscaMarca obj = new buscaMarca(new javax.swing.JFrame(), true, this.sessionPrograma, this.usr);
        obj.t_busca.requestFocus();
        obj.formatoTabla();
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        obj.setLocation((d.width/2)-(obj.getWidth()/2), (d.height/2)-(obj.getHeight()/2));
        obj.setVisible(true);

        Marca actor=obj.getReturnStatus();
        if(actor!=null)
        {
            l_nombre_marca.setText(actor.getMarcaNombre());
            t_marca.setText(actor.getIdMarca());
        }
        else
        {
            l_nombre_marca.setText("Seleccione una marca");
            t_marca.setText("");
        }
        t_motor.requestFocus();
    }//GEN-LAST:event_b_marcaActionPerformed

    private void t_placasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_t_placasActionPerformed
        t_modelo.requestFocus();
    }//GEN-LAST:event_t_placasActionPerformed

    private void t_placasKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_placasKeyTyped
        char car = evt.getKeyChar();
        if(t_placas.getText().length()>=8)
            evt.consume();
    }//GEN-LAST:event_t_placasKeyTyped

    private void t_motorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_t_motorActionPerformed
        t_placas.requestFocus();
    }//GEN-LAST:event_t_motorActionPerformed

    private void t_motorKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_motorKeyTyped
        evt.setKeyChar(Character.toUpperCase(evt.getKeyChar()));
        if(t_motor.getText().length()>=15)
            evt.consume();
    }//GEN-LAST:event_t_motorKeyTyped

    private void t_serieActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_t_serieActionPerformed
        t_economico.requestFocus();
    }//GEN-LAST:event_t_serieActionPerformed

    private void t_serieKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_serieKeyTyped
        evt.setKeyChar(Character.toUpperCase(evt.getKeyChar()));
        if(t_serie.getText().length()>=20)
            evt.consume();
    }//GEN-LAST:event_t_serieKeyTyped

    private void t_economicoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_t_economicoActionPerformed
        this.b_guardar.requestFocus();
    }//GEN-LAST:event_t_economicoActionPerformed

    private void t_economicoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_economicoKeyTyped
        if(t_economico.getText().length()>=6)
            evt.consume();
    }//GEN-LAST:event_t_economicoKeyTyped

    private void c_estatusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_c_estatusActionPerformed
        Date fecha = new Date();
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        t_fecha_estatus.setText(dateFormat.format(fecha));
        b_fecha_interna.requestFocus();
    }//GEN-LAST:event_c_estatusActionPerformed

    private void b_fecha_clienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_fecha_clienteActionPerformed
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
            t_fecha_cliente.setText(dia+"-"+mes+"-"+anio);
            if(guardaFecha()==true)
            {
                JOptionPane.showMessageDialog(null, "¡Se envió notificación al cliente!");
            }
        }
        else
            t_fecha_cliente.setText("DD-MM-AAAA");
        b_guardar.requestFocus();
    }//GEN-LAST:event_b_fecha_clienteActionPerformed

    private void b_fecha_internaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_fecha_internaActionPerformed
        h=new Herramientas(usr, 0);
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
            t_fecha_interna.setText(dia+"-"+mes+"-"+anio);
        }
        else
        t_fecha_interna.setText("DD-MM-AAAA");
        b_fecha_cliente.requestFocus();
    }//GEN-LAST:event_b_fecha_internaActionPerformed

    private void t_demeritoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_t_demeritoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_t_demeritoActionPerformed

    private void t_deducibleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_t_deducibleActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_t_deducibleActionPerformed

    private void c_siniestroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_c_siniestroActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_c_siniestroActionPerformed

    private void t_siniestroFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_t_siniestroFocusLost
        // TODO add your handling code here:
        if(t_siniestro.getText().length()>20)
        {
            t_siniestro.setText(t_siniestro.getText().substring(0, 20));
        }
    }//GEN-LAST:event_t_siniestroFocusLost

    private void t_polizaFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_t_polizaFocusLost
        // TODO add your handling code here:
        if(t_poliza.getText().length()>20)
        {
            t_poliza.setText(t_poliza.getText().substring(0, 20));
        }
    }//GEN-LAST:event_t_polizaFocusLost

    private void t_reporteFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_t_reporteFocusLost
        // TODO add your handling code here:
        if(t_reporte.getText().length()>19)
        {
            t_reporte.setText(t_reporte.getText().substring(0, 19));
        }
    }//GEN-LAST:event_t_reporteFocusLost

    private void t_incisoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_t_incisoFocusLost
        // TODO add your handling code here:
        if(t_inciso.getText().length()>10)
        {
            t_inciso.setText(t_inciso.getText().substring(0, 10));
        }
    }//GEN-LAST:event_t_incisoFocusLost

    private void t_placasFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_t_placasFocusLost
        // TODO add your handling code here:
        if(t_placas.getText().length()>8)
        {
            t_placas.setText(t_placas.getText().substring(0, 8));
        }
    }//GEN-LAST:event_t_placasFocusLost

    private void t_motorFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_t_motorFocusLost
        // TODO add your handling code here:
        if(t_motor.getText().length()>15)
        {
            t_motor.setText(t_motor.getText().substring(0, 15));
        }
    }//GEN-LAST:event_t_motorFocusLost

    private void t_serieFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_t_serieFocusLost
        // TODO add your handling code here:
        if(t_serie.getText().length()>20)
        {
            t_serie.setText(t_serie.getText().substring(0, 20));
        }
    }//GEN-LAST:event_t_serieFocusLost

    private void t_economicoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_t_economicoFocusLost
        // TODO add your handling code here:
        if(t_economico.getText().length()>6)
        {
            t_economico.setText(t_economico.getText().substring(0, 6));
        }
    }//GEN-LAST:event_t_economicoFocusLost

    private void t_nombre_clienteFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_t_nombre_clienteFocusLost
        // TODO add your handling code here:
        if(t_nombre_cliente.getText().length()>150)
        {
            t_nombre_cliente.setText(t_nombre_cliente.getText().substring(0, 150));
        }
    }//GEN-LAST:event_t_nombre_clienteFocusLost

    private void t_direccion_clienteFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_t_direccion_clienteFocusLost
        // TODO add your handling code here:
        if(t_direccion_cliente.getText().length()>150)
        {
            t_direccion_cliente.setText(t_direccion_cliente.getText().substring(0, 150));
        }
    }//GEN-LAST:event_t_direccion_clienteFocusLost

    private void t_colonia_clienteFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_t_colonia_clienteFocusLost
        // TODO add your handling code here:
        if(t_colonia_cliente.getText().length()>150)
        {
            t_colonia_cliente.setText(t_colonia_cliente.getText().substring(0, 150));
        }
    }//GEN-LAST:event_t_colonia_clienteFocusLost

    private void t_rfc_clienteFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_t_rfc_clienteFocusLost
        // TODO add your handling code here:
        if(t_rfc_cliente.getText().length()>13)
        {
            t_rfc_cliente.setText(t_rfc_cliente.getText().substring(0, 13));
        }
    }//GEN-LAST:event_t_rfc_clienteFocusLost

    private void t_email_clienteFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_t_email_clienteFocusLost
        // TODO add your handling code here:
        if(t_email_cliente.getText().length()>100)
        {
            t_email_cliente.setText(t_email_cliente.getText().substring(0, 100));
        }
    }//GEN-LAST:event_t_email_clienteFocusLost

    private void t_contacto_clienteFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_t_contacto_clienteFocusLost
        // TODO add your handling code here:
        if(t_contacto_cliente.getText().length()>150)
        {
            t_contacto_cliente.setText(t_contacto_cliente.getText().substring(0, 150));
        }
    }//GEN-LAST:event_t_contacto_clienteFocusLost

    private void t_nextel_clienteFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_t_nextel_clienteFocusLost
        // TODO add your handling code here:
        if(t_nextel_cliente.getText().length()>13)
        {
            t_nextel_cliente.setText(t_nextel_cliente.getText().substring(0, 13));
        }
    }//GEN-LAST:event_t_nextel_clienteFocusLost

    private void t_poblacion_clienteFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_t_poblacion_clienteFocusLost
        // TODO add your handling code here:
        if(t_poblacion_cliente.getText().length()>150)
        {
            t_poblacion_cliente.setText(t_poblacion_cliente.getText().substring(0, 150));
        }
    }//GEN-LAST:event_t_poblacion_clienteFocusLost

    private void t_cp_clienteKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_cp_clienteKeyTyped
        // TODO add your handling code here:
        char car = evt.getKeyChar();
        if(t_cp_cliente.getText().length()>=5)
        evt.consume();
        if((car<'0' || car>'9'))
        evt.consume();
    }//GEN-LAST:event_t_cp_clienteKeyTyped

    private void t_telefono_clienteKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_telefono_clienteKeyTyped
        // TODO add your handling code here:
        char car = evt.getKeyChar();
        if(t_telefono_cliente.getText().length()>=13)
        evt.consume();
        if((car<'0' || car>'9'))
        evt.consume();
    }//GEN-LAST:event_t_telefono_clienteKeyTyped

    private void t_cp_clienteFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_t_cp_clienteFocusLost
        // TODO add your handling code here:
        if(t_cp_cliente.getText().length()>5)
        {
            t_cp_cliente.setText(t_cp_cliente.getText().substring(0, 5));
            try{
                t_cp_cliente.commitEdit();
            }catch(Exception e){}
        }
    }//GEN-LAST:event_t_cp_clienteFocusLost

    private void t_telefono_clienteFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_t_telefono_clienteFocusLost
        // TODO add your handling code here:
        if(t_telefono_cliente.getText().length()>13)
        {
            t_telefono_cliente.setText(t_telefono_cliente.getText().substring(0, 13));
            try{
                t_telefono_cliente.commitEdit();
            }catch(Exception e){}
        }
    }//GEN-LAST:event_t_telefono_clienteFocusLost

    private void t_modeloFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_t_modeloFocusLost
        // TODO add your handling code here:
        if(t_modelo.getText().length()>4)
        {
            t_modelo.setText(t_modelo.getText().substring(0, 4));
            try{
                t_modelo.commitEdit();
            }catch(Exception e){}
        }
    }//GEN-LAST:event_t_modeloFocusLost

    private void t_ordenKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_ordenKeyReleased
        // TODO add your handling code here:
        if(t_orden.getText().length()>=6)
        {
            if(t_orden.getText().compareTo("")!=0)
            {
                orden_act=buscarOrden(Integer.parseInt(t_orden.getText()));
                if(this.p_ventanas.getSelectedIndex()==0)
                    consultaOrden();
                else
                    p_ventanas.setSelectedIndex(0);
            }
            else
            {
                h= new Herramientas(usr, menu);
                h.desbloqueaOrden();
                t_orden.setText("");
                borra_cajas();
                estado_campos(true, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false);
                l_nombre_modificado.setText("");
                id_modificado.setText("");
                t_orden.requestFocus();
                this.cerrada=0;
                this.modifica=0;
                this.orden_act=null;
                p_ventanas.setSelectedIndex(0);
            }
        }
    }//GEN-LAST:event_t_ordenKeyReleased

    private void t_kmFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_t_kmFocusLost
        // TODO add your handling code here:
        if(t_km.getText().length()>10)
        {
            t_km.setText(t_km.getText().substring(0, 10));
        }
    }//GEN-LAST:event_t_kmFocusLost

    private void t_kmActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_t_kmActionPerformed
        // TODO add your handling code here:
        t_color.requestFocus();
    }//GEN-LAST:event_t_kmActionPerformed

    private void t_kmKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_kmKeyTyped
        // TODO add your handling code here:
        evt.setKeyChar(Character.toUpperCase(evt.getKeyChar()));
        if(t_km.getText().length()>=10)
        evt.consume();
    }//GEN-LAST:event_t_kmKeyTyped

    private void t_colorFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_t_colorFocusLost
        // TODO add your handling code here:
        if(t_color.getText().length()>20)
        {
            t_color.setText(t_color.getText().substring(0, 19));
        }
    }//GEN-LAST:event_t_colorFocusLost

    private void t_colorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_t_colorActionPerformed
        // TODO add your handling code here:
        b_guardar.requestFocus();
    }//GEN-LAST:event_t_colorActionPerformed

    private void t_colorKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_colorKeyTyped
        // TODO add your handling code here:
        evt.setKeyChar(Character.toUpperCase(evt.getKeyChar()));
        if(t_color.getText().length()>=20)
        evt.consume();
    }//GEN-LAST:event_t_colorKeyTyped

    private void t_agenteFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_t_agenteFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_t_agenteFocusLost

    private void t_agenteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_t_agenteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_t_agenteActionPerformed

    private void t_agenteKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_agenteKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_t_agenteKeyTyped

    private void b_agenteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_agenteActionPerformed
        // TODO add your handling code here:
        buscaAgentes obj = new buscaAgentes(new javax.swing.JFrame(), true, this.sessionPrograma, this.usr);
        obj.t_busca.requestFocus();
        obj.formatoTabla();
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        obj.setLocation((d.width/2)-(obj.getWidth()/2), (d.height/2)-(obj.getHeight()/2));
        obj.setVisible(true);

        List actor=obj.getReturnStatus();
        if(actor!=null)
        {
            id_agente=actor.get(0).toString();
            t_agente.setText(actor.get(1).toString());
        }
        else
        {
            id_agente="";
            t_agente.setText("");
        }
        t_motor.requestFocus();
    }//GEN-LAST:event_b_agenteActionPerformed

    private void b_ajustadorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_ajustadorActionPerformed
        // TODO add your handling code here:
        buscaAjustadores obj = new buscaAjustadores(new javax.swing.JFrame(), true, this.sessionPrograma, this.usr);
        obj.t_busca.requestFocus();
        obj.formatoTabla();
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        obj.setLocation((d.width/2)-(obj.getWidth()/2), (d.height/2)-(obj.getHeight()/2));
        obj.setVisible(true);

        List actor=obj.getReturnStatus();
        if(actor!=null)
        {
            id_ajustador=actor.get(0).toString();
            t_ajustador.setText(actor.get(1).toString());
        }
        else
        {
            id_ajustador="";
            t_ajustador.setText("");
        }
        t_motor.requestFocus();
    }//GEN-LAST:event_b_ajustadorActionPerformed

    private void t_ajustadorFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_t_ajustadorFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_t_ajustadorFocusLost

    private void t_ajustadorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_t_ajustadorActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_t_ajustadorActionPerformed

    private void t_ajustadorKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_ajustadorKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_t_ajustadorKeyTyped

    private void b_buscarhActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_buscarhActionPerformed
        h=new Herramientas(usr, 0);
        h.session(sessionPrograma);
        buscaEmpleado obj = new buscaEmpleado(new javax.swing.JFrame(), true, usr, this.sessionPrograma, false);
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        obj.setLocation((d.width/2)-(obj.getWidth()/2), (d.height/2)-(obj.getHeight()/2));
        obj.setVisible(true);
        Empleado actor=obj.getReturnStatus();
        if(actor!=null)
        {
            id_tecnico=""+actor.getIdEmpleado();
            t_tecnico.setText(actor.getNombre());
        }
        else
        {
            id_tecnico="";
            t_tecnico.setText("");
        }
    }//GEN-LAST:event_b_buscarhActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton b_agente;
    private javax.swing.JButton b_ajustador;
    private javax.swing.JButton b_busca_cliente;
    public javax.swing.JButton b_busca_orden;
    private javax.swing.JButton b_buscar_aseguradora;
    private javax.swing.JButton b_buscarh;
    private javax.swing.JButton b_cancelar;
    private javax.swing.JButton b_fecha_cliente;
    private javax.swing.JButton b_fecha_interna;
    private javax.swing.JButton b_fecha_siniestro;
    private javax.swing.JButton b_guardar;
    private javax.swing.JButton b_guardar_cliente;
    private javax.swing.JButton b_marca;
    private javax.swing.JButton b_nuevo_cliente;
    private javax.swing.JButton b_tipo;
    private javax.swing.JComboBox c_estado_cliente;
    private javax.swing.JComboBox c_estatus;
    private javax.swing.JComboBox c_siniestro;
    private javax.swing.JCheckBox cb_asegurado;
    private javax.swing.JCheckBox cb_particular;
    private javax.swing.JCheckBox cb_tercero;
    private javax.swing.JCheckBox cb_tercero_asegurado;
    private javax.swing.JTextField id_modificado;
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
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
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
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JLabel l_id_cliente;
    private javax.swing.JLabel l_nombre_aseguradora;
    private javax.swing.JLabel l_nombre_marca;
    private javax.swing.JLabel l_nombre_modificado;
    private javax.swing.JPanel p_agenda;
    private javax.swing.JScrollPane p_autoriza_valuacion;
    private javax.swing.JScrollPane p_avance_pedidos;
    private javax.swing.JScrollPane p_compara_cotizaciones;
    private javax.swing.JScrollPane p_conciliacion;
    private javax.swing.JScrollPane p_destajo;
    private javax.swing.JPanel p_documentos;
    private javax.swing.JPanel p_formatos;
    private javax.swing.JPanel p_foto;
    private javax.swing.JPanel p_galeria;
    private javax.swing.JScrollPane p_genera_cotizacion;
    private javax.swing.JScrollPane p_genera_pedidos;
    private javax.swing.JScrollPane p_inventario;
    private javax.swing.JScrollPane p_levantamiento;
    private javax.swing.JPanel p_observaciones;
    private javax.swing.JScrollPane p_preFacura;
    private javax.swing.JScrollPane p_responsables;
    private javax.swing.JPanel p_sm;
    private javax.swing.JPanel p_unidad;
    private javax.swing.JScrollPane p_valuacion;
    public javax.swing.JTabbedPane p_ventanas;
    private javax.swing.JTextField t_agente;
    private javax.swing.JTextField t_ajustador;
    private javax.swing.JTextField t_aseguradora;
    private javax.swing.JTextField t_colonia_cliente;
    private javax.swing.JTextField t_color;
    private javax.swing.JTextField t_contacto_cliente;
    private javax.swing.JFormattedTextField t_cp_cliente;
    private javax.swing.JFormattedTextField t_deducible;
    private javax.swing.JFormattedTextField t_demerito;
    private javax.swing.JTextField t_direccion_cliente;
    private javax.swing.JTextField t_economico;
    private javax.swing.JTextField t_email_cliente;
    private javax.swing.JTextField t_fecha_cliente;
    private javax.swing.JTextField t_fecha_estatus;
    private javax.swing.JTextField t_fecha_interna;
    private javax.swing.JTextField t_fecha_siniestro;
    private javax.swing.JTextField t_inciso;
    private javax.swing.JTextField t_km;
    private javax.swing.JTextField t_marca;
    private javax.swing.JFormattedTextField t_modelo;
    private javax.swing.JTextField t_motor;
    private javax.swing.JTextField t_nextel_cliente;
    private javax.swing.JTextField t_nombre_cliente;
    public javax.swing.JTextField t_orden;
    private javax.swing.JTextField t_placas;
    private javax.swing.JTextField t_poblacion_cliente;
    private javax.swing.JTextField t_poliza;
    private javax.swing.JTextField t_reporte;
    private javax.swing.JTextField t_rfc_cliente;
    private javax.swing.JTextField t_serie;
    private javax.swing.JTextField t_siniestro;
    private javax.swing.JTextField t_tecnico;
    private javax.swing.JFormattedTextField t_telefono_cliente;
    private javax.swing.JTextField t_tipo;
    // End of variables declaration//GEN-END:variables

    private boolean guardaFecha()
    {
        Session session = HibernateUtil.getSessionFactory().openSession();
        boolean ID = false;
        try 
        {
            session.beginTransaction().begin();
            registro = (Orden)session.get(Orden.class, Integer.parseInt(t_orden.getText()));
            if(t_fecha_cliente.getText().compareTo("DD-MM-AAAA")!=0)
            {
                String [] campos = t_fecha_cliente.getText().split("-");

                Calendar calendario2 = Calendar.getInstance();
                calendario2.set(Calendar.MONTH, Integer.parseInt(campos[1])-1);
                calendario2.set(Calendar.YEAR, Integer.parseInt(campos[2]));
                calendario2.set(Calendar.DAY_OF_MONTH, Integer.parseInt(campos[0]));

                registro.setFechaCliente(calendario2.getTime());
            }
            else
                registro.setFechaCliente(null);
            session.update(registro);
            //*********Enviar la notificación***********
            orden_act=(Orden)session.get(Orden.class, orden_act.getIdOrden());
            ArrayList<Acceso> accesos=new ArrayList();
            Acceso [] aux1 = (Acceso[])orden_act.getClientes().getAccesos().toArray(new Acceso[0]);
            accesos.addAll(Arrays.asList(aux1));
            aux1 = (Acceso[])orden_act.getCompania().getAccesos().toArray(new Acceso[0]);
            accesos.addAll(Arrays.asList(aux1));
            aux1 = (Acceso[])orden_act.getAgente().getAccesos().toArray(new Acceso[0]);
            accesos.addAll(Arrays.asList(aux1));
            
            String notificaciones="{\"NOTIFICACIONES\":[";
            for(int a=0; a<accesos.size(); a++)
            {
                Notificacion nueva=new Notificacion();
                nueva.setMensaje("OT:"+orden_act.getIdOrden()+" Fecha Promesa");
                nueva.setExtra("");
                nueva.setIntentos(0);
                nueva.setVisto(false);
                nueva.setAcceso(accesos.get(a));
                Integer id = (Integer)session.save(nueva);
                nueva=(Notificacion)session.get(Notificacion.class, id);
                nueva.setExtra("{\"VENTANA\":\"Solicitudes_des\",\"ID_ORDEN\":\""+orden_act.getIdOrden()+"\",\"ID_NOTIFICACION\":\""+id+"\",\"ID_USUARIO\":\""+accesos.get(a).getIdAcceso()+"\"}");
                session.update(nueva);
                notificaciones+="{\"ID\":\""+id+"\"}";
                if(a+1 < accesos.size())
                    notificaciones+=",";
            }
            notificaciones+="]}";
            session.beginTransaction().commit();
            
            PeticionPost service=new PeticionPost("http://tracto.ddns.net:8085/integral/service/api.php");
            service.add("METODO", "NOTIFICACION.MENSAJE");
            service.add("NOTIFICACIONES", notificaciones);
            System.out.println(service.getRespueta());
            //******************************************
            ID=true;
        }catch (Exception he) 
        {
            he.printStackTrace();
            session.getTransaction().rollback();
            ID=false;
        }
        if(session!=null)
            if(session.isOpen())
            {
                session.flush();
                session.clear();
                session.close();    
            }
        return ID;
    }
    
    private boolean guardarOrden()
    {
        Session session = HibernateUtil.getSessionFactory().openSession();
        boolean ID = false;
        try 
        {
            session.beginTransaction().begin();
            registro = (Orden)session.get(Orden.class, Integer.parseInt(t_orden.getText()));
            //**********Almacenamos el Registro*******************
            Compania aseguradora = new Compania();
            Clientes cliente =new Clientes();
            Tipo unidad_tipo = new Tipo();
            Marca mr =new Marca();
            aseguradora.setIdCompania(Integer.parseInt(t_aseguradora.getText()));
            registro.setCompania(aseguradora);

            if(t_siniestro.getText().compareTo("")!=0)
                registro.setSiniestro(t_siniestro.getText());
            else
                registro.setSiniestro(null);

            if(t_poliza.getText().compareTo("")!=0)
                registro.setPoliza(t_poliza.getText());
            else
                registro.setPoliza(null);

            if(t_reporte.getText().compareTo("")!=0)
                registro.setNoReporte(t_reporte.getText());
            else
                registro.setNoReporte(null);

            if(t_inciso.getText().compareTo("")!=0)
                registro.setInciso(t_inciso.getText());
            else
                registro.setInciso(null);

            if(t_fecha_siniestro.getText().compareTo("DD-MM-AAAA")!=0)
            {
                String [] campos = t_fecha_siniestro.getText().split("-");
                Calendar calendario = Calendar.getInstance();
                calendario.set(Calendar.MONTH, Integer.parseInt(campos[1])-1);
                calendario.set(Calendar.YEAR, Integer.parseInt(campos[2]));
                calendario.set(Calendar.DAY_OF_MONTH, Integer.parseInt(campos[0]));

                registro.setFechaSiniestro(calendario.getTime());
            }
            else
                registro.setFechaSiniestro(null);

            if(t_tipo.getText().compareTo("")!=0)
            {
                unidad_tipo.setTipoNombre(t_tipo.getText());
                registro.setTipo(unidad_tipo);
            }

            if(t_marca.getText().compareTo("")!=0)
            {
                mr.setIdMarca(t_marca.getText());
                registro.setMarca(mr);
            }

            if(t_motor.getText().compareTo("")!=0)
                registro.setNoMotor(t_motor.getText());
            else
                registro.setNoMotor(null);

            if(t_placas.getText().compareTo("")!=0)
                registro.setNoPlacas(t_placas.getText());
            else
                registro.setNoPlacas(null);

            if(t_modelo.getText().compareTo("")!=0)
                registro.setModelo(Integer.parseInt(t_modelo.getText()));

            if(t_serie.getText().compareTo("")!=0)
                registro.setNoSerie(t_serie.getText());
            else
                registro.setNoSerie(null);

            if(t_economico.getText().compareTo("")!=0)
                registro.setNoEconomico(t_economico.getText());
            else
                registro.setNoEconomico(null);

            if(t_nombre_cliente.getText().compareTo("")!=0)
            {
                cliente.setIdClientes(Integer.parseInt(l_id_cliente.getText()));
                registro.setClientes(cliente);
            }

            if(t_fecha_interna.getText().compareTo("DD-MM-AAAA")!=0)
            {
                String [] campos = t_fecha_interna.getText().split("-");

                Calendar calendario1 = Calendar.getInstance();
                calendario1.set(Calendar.MONTH, Integer.parseInt(campos[1])-1);
                calendario1.set(Calendar.YEAR, Integer.parseInt(campos[2]));
                calendario1.set(Calendar.DAY_OF_MONTH, Integer.parseInt(campos[0]));

                registro.setFechaTaller(calendario1.getTime());
            }
            else
                registro.setFechaTaller(null);

            if(t_fecha_cliente.getText().compareTo("DD-MM-AAAA")!=0)
            {
                String [] campos = t_fecha_cliente.getText().split("-");

                Calendar calendario2 = Calendar.getInstance();
                calendario2.set(Calendar.MONTH, Integer.parseInt(campos[1])-1);
                calendario2.set(Calendar.YEAR, Integer.parseInt(campos[2]));
                calendario2.set(Calendar.DAY_OF_MONTH, Integer.parseInt(campos[0]));

                registro.setFechaCliente(calendario2.getTime());
            }
            else
                registro.setFechaCliente(null);

            //**********guardamos en estatus de la orden******************
            String [] campos = t_fecha_estatus.getText().split("-");                                
            Calendar calendario3 = Calendar.getInstance();
            calendario3.set(Calendar.MONTH, Integer.parseInt(campos[1])-1);
            calendario3.set(Calendar.YEAR, Integer.parseInt(campos[2]));
            calendario3.set(Calendar.DAY_OF_MONTH, Integer.parseInt(campos[0]));

            registro.setFechaEstatus(calendario3.getTime());

            Estatus est = new Estatus();
            est.setEstatusNombre(c_estatus.getSelectedItem().toString());
            registro.setEstatus(est);
            registro.setKm(t_km.getText());
            registro.setColor(t_color.getText());

            //***************************************************************
            int op=0;
            if(cb_asegurado.isSelected())
            {
                registro.setTipoCliente("1");
                op=1;
            }

            if(cb_tercero.isSelected())
            {
                registro.setTipoCliente("2");
                op=1;
            }

            if(cb_tercero_asegurado.isSelected())
            {
                registro.setTipoCliente("3");
                op=1;
            }

            if(cb_particular.isSelected())
            {
                registro.setTipoCliente("4");
                op=1;
            }

            if(op==0)
                registro.setTipoCliente(null);

            if(id_tecnico.compareTo("")!=0)
            {
                Empleado emp=new Empleado();
                emp.setIdEmpleado(Integer.parseInt(id_tecnico));
                registro.setEmpleadoByRTecnico(emp);
            }
            else
            {
                registro.setEmpleadoByRTecnico(null);
            }
            
            if(id_agente.compareTo("")!=0)
            {
                Agente age=new Agente();
                age.setIdAgente(Integer.parseInt(id_agente));
                registro.setAgente(age);
            }
            else
            {
                registro.setAgente(null);
            }
            
            if(id_ajustador.compareTo("")!=0)
            {
                Ajustador aju=new Ajustador();
                aju.setIdAjustador(Integer.parseInt(id_ajustador));
                registro.setAjustador(aju);
            }
            else
            {
                registro.setAjustador(null);
            }
            //registro.setCiclo(new Ciclo(Integer.parseInt(periodo)));
            //************Guardamos el usuario que modifica*****************
            registro.setUsuarioByIdModificado(usr);
            //**********si hay foto la guardamos
            if(entro_foto==1)
            {
                if(guardaFoto(registro.getIdOrden()))
                {
                    if(existe_foto.compareTo("")==0)
                    {
                        Date fecha_orden = new Date();
                        Foto img=new Foto(registro, ""+registro.getIdOrden()+".jpg", fecha_orden);
                        registro.addFoto(img);
                    }
                    if(c_siniestro.getSelectedIndex()!=-1)
                    {
                        if(this.c_siniestro.getItemCount()>0)
                        {
                            Reparacion re = (Reparacion)session.createCriteria(Reparacion.class).add(Restrictions.eq("nombre", this.c_siniestro.getSelectedItem().toString())).setMaxResults(1).uniqueResult();
                            registro.setReparacion(re);
                        }
                        else
                            registro.setReparacion(null);
                    }
                    else
                        registro.setReparacion(null);

                    session.update(registro);
                    session.getTransaction().commit();
                    int valor= orden_act.getIdOrden();
                    orden_act=null;
                    Orden aux=(Orden)session.get(Orden.class, ID);
                    Empleado emp1=aux.getEmpleadoByRTecnico();
                    if(emp1!=null)
                    {
                        emp1=(Empleado)session.get(Empleado.class, emp1.getIdEmpleado());
                        if(emp1.getEmail()!=null)
                        {
                            String mensaje="<p>Asignaci&oacute;n de Orden De Taller <strong>"+valor+"</strong></p><p>Saludos. </p>";
                            enviaCorreo("Asignaci&oacute;n OT("+valor+")", mensaje, emp1.getEmail()); 
                        }
                    }
                    ID=true;
                }
            }
            else
            {
                if(c_siniestro.getSelectedIndex()!=-1)
                {
                    if(this.c_siniestro.getItemCount()>0)
                    {
                       Reparacion re = (Reparacion)session.createCriteria(Reparacion.class).add(Restrictions.eq("nombre", this.c_siniestro.getSelectedItem().toString())).setMaxResults(1).uniqueResult();
                       registro.setReparacion(re);           
                    }
                    else
                       registro.setReparacion(null);
                }
                else
                    registro.setReparacion(null);
                session.update(registro);
                session.getTransaction().commit();
                int valor= orden_act.getIdOrden();
                Orden aux=(Orden)session.get(Orden.class, valor);
                Empleado emp1=aux.getEmpleadoByRTecnico();
                if(emp1!=null)
                {
                    emp1=(Empleado)session.get(Empleado.class, emp1.getIdEmpleado());
                    if(emp1.getEmail()!=null)
                    {
                        String mensaje="<p>Asignaci&oacute;n de Orden De Taller <strong>"+valor+"</strong></p><p>Saludos. </p>";
                        enviaCorreo("Asignaci&oacute;n OT("+valor+")", mensaje, emp1.getEmail()); 
                    }
                }
                ID=true;
            }
            //return ID;   
        } 
        catch (HibernateException he) 
        {
            he.printStackTrace();
            session.getTransaction().rollback();
            ID=false;
        }
        if(session!=null)
            if(session.isOpen())
            {
                session.flush();
                session.clear();
                session.close();    
            }
        return ID;
    }

    private boolean guardaFoto(int no)
    {
        try 
        {
            if(archivo.exists()==true)
            {
                File folder = new File(ruta+"ordenes/"+no);
                folder.mkdirs();
                folder = new File(ruta+"ordenes/"+no+"/miniatura");
                folder.mkdirs();
                Random rng=new Random();
                long  dig8 = rng.nextInt(90000000)+10000000;
                String nombre_img="";
                if(existe_foto.compareTo("")!=0)
                    nombre_img=existe_foto;
                else
                    nombre_img=""+no+".jpg";
                File destino = new File(ruta+"ordenes/"+no+"/"+nombre_img);
                File miniatura = new File(ruta+"ordenes/"+no+"/miniatura/"+nombre_img);
                String ruta=archivo.getPath();
                javax.swing.JPanel p=new Imagen(ruta, 385, 250, 0, 0, 385, 250);
                BufferedImage dibujo =new BufferedImage(385, 250, BufferedImage.TYPE_INT_RGB);
                Graphics g = dibujo.getGraphics();
                p.paint(g);
                ImageIO.write((RenderedImage)dibujo, "jpg", miniatura); // Salvar la imagen en el fichero
                BufferedImage dibujoGrande =ImageIO.read(archivo);
                ImageIO.write((RenderedImage)dibujoGrande, "jpg", destino); // Salvar la imagen en el fichero*/
                return true;
            }
            return false;
        } 
        catch (Exception ioe)
        {
            ioe.printStackTrace();
            return false;
        }
    }
    private boolean guardarCliente(Clientes obj)
    {
        boolean var=false;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try 
        {
            session.beginTransaction().begin();
            session.save(obj);
            l_id_cliente.setText(obj.getIdClientes().toString());
            session.getTransaction().commit();
            var= true;
        } 
        catch (HibernateException he) 
        {
            he.printStackTrace();
            session.getTransaction().rollback();
            var = false;
        }
        if(session!=null)
            if(session.isOpen())
            {
                session.flush();
                session.clear();
                session.close(); 
            }
        return var;
    }
    private void buscaCompania()
    {
        if(t_aseguradora.getText().length()>4)
                t_aseguradora.setText(t_aseguradora.getText().substring(0, 4));

            if(t_aseguradora.getText().compareTo("")==0)
                b_buscar_aseguradora.requestFocus();
            else
            {
                Session session = HibernateUtil.getSessionFactory().openSession();
                try
                {
                    session.beginTransaction().begin();
                    Query q = session.createQuery("from Compania com where com.idCompania='" + t_aseguradora.getText() + "'");
                    List resultList = q.list();
                    if(resultList.size()>0)
                    {
                        for (Object o : resultList) 
                        {
                            Compania actor = (Compania) o;
                            l_nombre_aseguradora.setText(actor.getNombre());
                        }
                    }
                    else
                    {
                        l_nombre_aseguradora.setText("Seleccione una aseguradora");
                        t_aseguradora.setText("");
                    }
                }catch(Exception e)
                {
                    System.out.println(e);
                }
                if(session!=null)
                    if(session.isOpen())
                    {
                        session.flush();
                        session.clear();
                        session.close();
                    }
            }
    }

    private void buscaTipo()
    {
        if(t_tipo.getText().length()>150)
            t_tipo.setText(t_tipo.getText().substring(0, 150));

        if(t_tipo.getText().compareTo("")!=0)
        {
            Session session = HibernateUtil.getSessionFactory().openSession();
            try
            {
                session.beginTransaction().begin();
                Query q = session.createQuery("from Tipo com where com.tipoNombre='" + t_tipo.getText() + "'");
                List resultList = q.list();
                if(resultList.size()<=0)
                    t_tipo.setText("");
            }catch(Exception e)
            {
                System.out.println(e);
            }
            if(session!=null)
                if(session.isOpen())
                {
                    session.flush();
                    session.clear();
                    session.close();
                }
        }
    }

    private void buscaMarca()
    {
        if(t_marca.getText().length()>20)
                t_marca.setText(t_marca.getText().substring(0, 20));

            if(t_marca.getText().compareTo("")!=0)
            {
                Session session = HibernateUtil.getSessionFactory().openSession();
                try
                {
                    session.beginTransaction().begin();
                    Query q = session.createQuery("from Marca com where com.idMarca='" + t_marca.getText() + "'");
                    List resultList = q.list();
                    if(resultList.size()>0)
                    {
                        for (Object o : resultList) 
                        {
                            Marca actor = (Marca) o;
                            l_nombre_marca.setText(actor.getMarcaNombre());
                        }
                    }
                    else
                    {
                        l_nombre_marca.setText("Seleccione una marca");
                        t_marca.setText("");
                    }
                }catch(Exception e)
                {
                    System.out.println(e);
                }
                if(session!=null)
                    if(session.isOpen())
                    {
                        session.flush();
                        session.clear();
                        session.close();
                    }
            }
            else
                l_nombre_marca.setText("Seleccione una marca");
    }

    public void cargaEstatus()
    {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try
        {
            Query q = session.createQuery("from Estatus");
            List resultList = q.list();
            if(resultList.size()>0)
            {
                c_estatus.removeAllItems();
                for (Object o : resultList)
                {
                    Estatus actor = (Estatus) o;
                    c_estatus.addItem(actor.getEstatusNombre());
                }
            }
        }catch(Exception e)
        {
            System.out.println(e);
        }
        finally
        {
            if(session!=null)
                if(session.isOpen())
                {
                    session.flush();
                    session.clear();
                    session.close();
                }
        }
    }


    public void cargaSiniestro()
    {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try
        {
            Query q = session.createQuery("from Reparacion es");
            List resultList = q.list();
            if(resultList.size()>0)
            {
                this.c_siniestro.removeAllItems();
                for (Object o : resultList)
                {
                    Reparacion actor = (Reparacion) o;
                    this.c_siniestro.addItem(actor.getNombre());
                }
            }
        }catch(Exception e)
        {
            System.out.println(e);
        }
        finally
        {
            if(session!=null)
                if(session.isOpen())
                {
                    session.flush();
                    session.clear();
                    session.close();
                }
        }
    }

    public boolean consultaCliente(String nombre)
    {
        boolean var=false;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try
        {
            session.beginTransaction().begin();
            Query q = session.createQuery("from Clientes obj where obj.nombre='"+nombre+"'");
            List resultList = q.list();
            if(resultList.size()>0)
                var=true;
            else
                var=false;
        }catch(Exception e)
        {
            System.out.println(e);
        }
        if(session!=null)
            if(session.isOpen())
            {
                session.flush();
                session.clear();
                session.close();
            }
        return var;
    }

    private Orden buscarOrden(int id)
    {
        Orden ord=null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try 
        {
            ord = (Orden)session.get(Orden.class, id); 
        } 
        catch (HibernateException he) 
        {
            he.printStackTrace();
            session.getTransaction().rollback();
            ord=null;
        }
        if(session!=null)
            if(session.isOpen())
            {
                session.flush();
                session.clear();
                session.close();
            }
        return ord;
    }

    private void borra_cajas()
    {
        this.cargaSiniestro();
        this.cargaEstatus();
        t_aseguradora.setText("");
        t_siniestro.setText("");
        t_poliza.setText("");
        t_reporte.setText("");
        t_inciso.setText("");
        t_fecha_siniestro.setText("DD-MM-AAAA");
        t_tipo.setText("");
        l_nombre_marca.setText("Seleccione una marca");
        t_marca.setText("");
        t_motor.setText("");
        t_placas.setText("");
        t_tecnico.setText("");
        id_tecnico="";
        t_agente.setText("");
        id_agente="";
        t_ajustador.setText("");
        id_ajustador="";
        t_nombre_cliente.setText("");
        t_direccion_cliente.setText("");
        t_colonia_cliente.setText("");
        t_cp_cliente.setText("");
        t_rfc_cliente.setText("");
        t_poblacion_cliente.setText("");
        t_telefono_cliente.setText("");
        t_email_cliente.setText("");
        t_modelo.setText("");
        t_serie.setText("");
        t_economico.setText("");
        t_deducible.setValue(null);
        t_deducible.setText("");
        t_demerito.setValue(null);
        t_demerito.setText("");
        c_estatus.setSelectedItem(0);
        t_fecha_interna.setText("DD-MM-AAAA");
        t_fecha_cliente.setText("DD-MM-AAAA");
        cb_asegurado.setSelected(false);
        cb_tercero.setSelected(false);
        cb_tercero_asegurado.setSelected(false);
        cb_particular.setSelected(false);
        p_foto.removeAll();
        p_foto.add(new Imagen("imagenes/foto.png", 83, 43, 1, 25, 83, 88));
        p_foto.repaint();

        t_nombre_cliente.setEditable(false);
        t_direccion_cliente.setEditable(false);
        t_colonia_cliente.setEditable(false);
        t_cp_cliente.setEditable(false);
        t_rfc_cliente.setEditable(false);
        t_poblacion_cliente.setEditable(false);
        c_estado_cliente.setEnabled(false);
        t_telefono_cliente.setEditable(false);
        t_email_cliente.setEditable(false);

        b_nuevo_cliente.setEnabled(false);
        b_guardar_cliente.setEnabled(false);

        entro_foto=0;
        existe_foto="";

        l_nombre_aseguradora.setText("Seleccione una aseguradora");

        Date fecha = new Date();
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        t_fecha_estatus.setText(dateFormat.format(fecha));
        
        t_km.setText("");
        t_color.setText("");
    }

    public void consultaOrden()
    {
        //h=new Herramientas(usr, 0);
        if(t_orden.getText().compareTo("")!=0)
        {
            cargaEstatus();
            borra_cajas();
            h= new Herramientas(usr, menu);
            h.session(sessionPrograma);
            h.desbloqueaOrden();
            if (orden_act!=null)
            {
                //*******************Datos del Seguro***************************
                //orden_act=buscarOrden(orden_act.getIdOrden());
                Session session = HibernateUtil.getSessionFactory().openSession();
                orden_act=(Orden)session.get(Orden.class, orden_act.getIdOrden());
                this.l_nombre_aseguradora.setText(orden_act.getCompania().getNombre());
                this.t_aseguradora.setText(""+orden_act.getCompania().getIdCompania());
                this.t_siniestro.setText(orden_act.getSiniestro());
                this.t_poliza.setText(orden_act.getPoliza());
                this.t_reporte.setText(orden_act.getNoReporte());
                this.t_inciso.setText(orden_act.getInciso());

                Date fecha;
                DateFormat dateFormat;
                try
                {
                    fecha=orden_act.getFechaSiniestro();
                    dateFormat = new SimpleDateFormat("dd-MM-yyyy");//YYYY-MM-DD HH:MM:SS
                    this.t_fecha_siniestro.setText(dateFormat.format(fecha));
                }catch(Exception E){}
                //**************************************************************

                if(orden_act.getEmpleadoByRTecnico()!=null)
                {
                    id_tecnico=""+orden_act.getEmpleadoByRTecnico().getIdEmpleado();
                    t_tecnico.setText(orden_act.getEmpleadoByRTecnico().getNombre());
                }
                
                if(orden_act.getAgente()!=null)
                {
                    id_agente=""+orden_act.getAgente().getIdAgente();
                    t_agente.setText(orden_act.getAgente().getNombre());
                }
                
                if(orden_act.getAjustador()!=null)
                {
                    id_ajustador=""+orden_act.getAjustador().getIdAjustador();
                    t_ajustador.setText(orden_act.getAjustador().getNombre());
                }
                //*********Datos de la Unidad***********************************
                this.t_tipo.setText(orden_act.getTipo().getTipoNombre());
                this.l_nombre_marca.setText(orden_act.getMarca().getMarcaNombre());
                this.t_marca.setText(orden_act.getMarca().getIdMarca());
                this.t_placas.setText(orden_act.getNoPlacas());
                this.t_motor.setText(orden_act.getNoMotor());
                try
                {
                    fecha=orden_act.getFechaTaller();
                    dateFormat = new SimpleDateFormat("dd-MM-yyyy");//YYYY-MM-DD HH:MM:SS
                    this.t_fecha_interna.setText(dateFormat.format(fecha));
                }catch(Exception E){}

                try
                {
                    fecha=orden_act.getFechaCliente();
                    dateFormat = new SimpleDateFormat("dd-MM-yyyy");//YYYY-MM-DD HH:MM:SS
                    this.t_fecha_cliente.setText(dateFormat.format(fecha));
                }catch(Exception E){}

                try
                {
                    int mod=orden_act.getModelo();
                    this.t_modelo.setText(""+mod);
                }catch(Exception e){}

                this.t_serie.setText(orden_act.getNoSerie());
                this.t_economico.setText(orden_act.getNoEconomico());

                this.t_deducible.setText(""+orden_act.getDeducible());
                this.t_deducible.setValue(orden_act.getDeducible());
                this.t_demerito.setText(""+orden_act.getDemerito());
                this.t_demerito.setValue(orden_act.getDemerito());
                

                this.c_estatus.setSelectedItem(orden_act.getEstatus().getEstatusNombre());

                if(orden_act.getReparacion()!=null)
                    this.c_siniestro.setSelectedItem(orden_act.getReparacion().getNombre());
                else
                    this.c_siniestro.setSelectedIndex(-1);

                try
                {
                    fecha=orden_act.getFechaEstatus();
                    dateFormat = new SimpleDateFormat("dd-MM-yyyy");//YYYY-MM-DD HH:MM:SS
                    this.t_fecha_estatus.setText(dateFormat.format(fecha));
                }catch(Exception e){}
                
                if(orden_act.getKm()!=null)
                    t_km.setText(orden_act.getKm());
                if(orden_act.getColor()!=null)
                    t_color.setText(orden_act.getColor());
                //**************************************************************

                //***********cargamos los datos del cliente********************
                this.l_id_cliente.setText(""+orden_act.getClientes().getIdClientes());
                this.t_nombre_cliente.setText(orden_act.getClientes().getNombre());
                this.t_direccion_cliente.setText(orden_act.getClientes().getDireccion());
                this.t_colonia_cliente.setText(orden_act.getClientes().getColonia());
                try{
                   int cp=orden_act.getClientes().getCp();
                   this.t_cp_cliente.setText(""+cp);
                }catch(Exception E){}
                this.t_rfc_cliente.setText(orden_act.getClientes().getRfc());
                this.t_poblacion_cliente.setText(orden_act.getClientes().getPoblacion());
                this.c_estado_cliente.setSelectedItem(orden_act.getClientes().getEstado());
                this.t_telefono_cliente.setText(orden_act.getClientes().getTelefono());
                this.t_email_cliente.setText(orden_act.getClientes().getEmail());

                String tipo_cliente=orden_act.getTipoCliente();
                if(tipo_cliente!=null)
                {
                    switch(tipo_cliente)
                    {
                        case "1": 
                            this.cb_asegurado.setSelected(true); 
                            break;

                        case "2": 
                            this.cb_tercero.setSelected(true);
                            break;

                        case "3": 
                            this.cb_tercero_asegurado.setSelected(true);
                            break;

                        case "4": 
                            this.cb_particular.setSelected(true);
                            break;
                    }
                }
                //**************************************************************

                //**********************cargamos la imagen**********************
                Query q = session.createSQLQuery("select descripcion from foto where id_orden="+orden_act.getIdOrden()+" order by id_foto asc limit 1;");
                Object foto = q.uniqueResult();
                if(foto!=null)
                {
                    existe_foto=foto.toString();
                    p_foto.removeAll();
                    im=new Imagen(ruta+"ordenes/"+orden_act.getIdOrden()+"/miniatura/"+existe_foto, 81, 91, 1, 1, 83, 93 );
                    p_foto.add(im);
                    p_foto.repaint();
                }
                else
                {
                    existe_foto="";
                    p_foto.removeAll();
                    im=new Imagen("imagenes/foto.png", 83, 43, 1, 25, 83, 88);
                    p_foto.add(im);
                    p_foto.repaint();
                }
                //**************************************************************

                //************cargamos quien modifico***************************
                try
                {
                    this.id_modificado.setText(orden_act.getUsuarioByIdModificado().getIdUsuario());
                }catch(Exception e){}
                try
                {
                    this.l_nombre_modificado.setText(orden_act.getUsuarioByIdModificado().getEmpleado().getNombre());
                }catch(Exception e){}
                //**************************************************************

                //******consultamos los permosos
                String resp=h.estadoOrden(orden_act);
                try
                {
                    this.usr = (Usuario)session.get(Usuario.class, usr.getIdUsuario());

                    if(usr.getVerFechaCliente()==false)
                    {
                        this.t_fecha_cliente.setVisible(false);
                        this.b_fecha_cliente.setVisible(false);
                    }
                    else
                    {
                        this.t_fecha_cliente.setVisible(true);
                        this.b_fecha_cliente.setVisible(true);
                    }
                    if(resp.compareTo("")==0 || resp.compareTo("*bloqueada ok*")==0)
                    {
                        estado="";
                        if(usr.getEditarAperturaOrden()==true)
                        {
                            estado_campos(true, true, true, true, true, true, true, false, false, false, false, false, false, false, false, false, true, false, usr.getCrearClientes(), true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, false, false);
                            modifica=1;
                        }
                        else
                        {
                            estado_campos(true, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false);
                            modifica=0;
                        }
                    }
                    else
                    {
                        estado="bloqueado";
                        estado_campos(true, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false);
                        modifica=0;
                        JOptionPane.showMessageDialog(null, "¡Orden Bloqueada por:"+orden_act.getUsuarioByBloqueada().getIdUsuario());
                    }
                    if(orden_act.getFechaCierre()!=null)
                    {
                        estado_campos(true, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false);
                        modifica=0;
                        JOptionPane.showMessageDialog(null, "¡Orden cerrada!");
                        cerrada=1;
                    }
                    else
                        cerrada=0;
                }catch(Exception e)
                {
                    System.out.println(e);
                }
                finally
                {
                    if(session!=null)
                        if(session.isOpen())
                        {
                            session.flush();
                            session.clear();
                            session.close();
                        }
                }
            }
            else
            {
                t_orden.setText("");
                borra_cajas();
                estado_campos(true, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false);
                l_nombre_modificado.setText("");
                id_modificado.setText("");
                JOptionPane.showMessageDialog(null, "¡El numero de orden no existe!");
                t_orden.requestFocus();
                modifica=0;
                cerrada=0;
                this.orden_act=null;
            }
        }
        else
        {
            orden_act=null;
            borra_cajas();
            estado_campos(true, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false);
            l_nombre_modificado.setText("");
            id_modificado.setText("");
            p_ventanas.setSelectedIndex(0);
            this.orden_act=null;
            modifica=0;
            cerrada=0;
        }
        this.updateUI();
    }

    private void estado_campos(boolean orden, boolean aseguradora, boolean siniestro, boolean poliza, boolean reporte, boolean inciso, boolean fecha_siniestro, boolean nombre_cliente, boolean direccion_cleinte, boolean clonia_cliente, boolean cp_cliente, boolean rfc_cliente, boolean poblacion_cliente, boolean estado_cliente, boolean telefono_cliente, boolean email_cliente, boolean tipo_cliente, boolean guarda_cliente, boolean edita_cliente, boolean busca_cliente, boolean tipo, boolean marca, boolean placas, boolean motor, boolean fecha_interna, boolean fecha_cliente, boolean modelo, boolean serie, boolean economico, boolean deducible, boolean demerito, boolean estatus, boolean cancelar, boolean guardar, boolean contacto, boolean nextel)
    {
        this.t_orden.setEditable(orden);
        this.b_busca_orden.setEnabled(orden);
        this.t_aseguradora.setEditable(aseguradora);
        this.b_buscar_aseguradora.setEnabled(aseguradora);
        this.t_siniestro.setEditable(siniestro);
        this.t_poliza.setEditable(poliza);
        this.t_reporte.setEditable(reporte);
        this.t_inciso.setEditable(inciso);
        this.b_fecha_siniestro.setEnabled(fecha_siniestro);
        this.t_nombre_cliente.setEditable(nombre_cliente);
        this.t_direccion_cliente.setEditable(direccion_cleinte);
        this.t_colonia_cliente.setEditable(clonia_cliente);
        this.t_cp_cliente.setEditable(cp_cliente);
        this.t_rfc_cliente.setEditable(rfc_cliente);
        this.t_poblacion_cliente.setEditable(poblacion_cliente);
        this.c_estado_cliente.setEnabled(estado_cliente);
        this.t_telefono_cliente.setEditable(telefono_cliente);
        this.t_email_cliente.setEditable(email_cliente);
        this.t_contacto_cliente.setEditable(contacto);
        this.t_nextel_cliente.setEditable(nextel);
        this.cb_asegurado.setEnabled(tipo_cliente);
        this.cb_particular.setEnabled(tipo_cliente);
        this.cb_tercero.setEnabled(tipo_cliente);
        this.cb_tercero_asegurado.setEnabled(tipo_cliente);
        this.b_guardar_cliente.setEnabled(guarda_cliente);
        this.b_nuevo_cliente.setEnabled(edita_cliente);
        this.b_busca_cliente.setEnabled(busca_cliente);
        this.t_tipo.setEditable(tipo);
        this.b_tipo.setEnabled(tipo);
        this.t_marca.setEditable(marca);
        this.b_marca.setEnabled(marca);
        this.t_placas.setEditable(placas);
        this.t_motor.setEditable(motor);
        this.b_fecha_interna.setEnabled(fecha_interna);
        this.b_fecha_cliente.setEnabled(fecha_cliente);
        this.t_modelo.setEditable(modelo);
        this.t_serie.setEditable(serie);
        this.t_economico.setEditable(economico);
        this.t_deducible.setEditable(false);
        this.t_demerito.setEditable(false);
        this.c_estatus.setEnabled(estatus);
        this.c_siniestro.setEnabled(estatus);
        this.b_cancelar.setEnabled(cancelar);
        this.b_guardar.setEnabled(guardar);
        this.b_buscarh.setEnabled(guardar);
        this.b_ajustador.setEnabled(guardar);
        this.b_agente.setEnabled(guardar);
    }
    
    
    private boolean guardaEmpleado(Empleado obj, String campo, String valor)
    {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try 
        {
            session.beginTransaction().begin();
            Orden objeto = (Orden)session.get(Orden.class, Integer.parseInt(t_orden.getText()));
            String [] fecha ;
            String [] hora ;
            String [] aux ;
            int op=0;
            Calendar calendario3;
            switch(campo)
            {
                case "cierra_orden":
                    //dd-MM-yyyy HH:mm:mm
                    fecha = valor.split("-");
                    hora=fecha[2].split(":");
                    aux=hora[0].split(" ");
                    fecha[2]=aux[0];
                    hora[0]=aux[1];

                    calendario3 = Calendar.getInstance();
                    calendario3.set(
                            Integer.parseInt(fecha[2]), 
                            Integer.parseInt(fecha[1])-1, 
                            Integer.parseInt(fecha[0]), 
                            Integer.parseInt(hora[0]), 
                            Integer.parseInt(hora[1]), 
                            Integer.parseInt(hora[2]));
                    objeto.setFechaCierre(calendario3.getTime());
                    objeto.setUsuarioByRCierre(this.usr);
                    session.update(objeto);
                    session.getTransaction().commit();
                    return true;
            }
            return false;
        } 
        catch (HibernateException he) 
        {
            he.printStackTrace();
            session.getTransaction().rollback();
            return false;
        }
        finally
        {
        if(session!=null)
            if(session.isOpen())
            {
                session.flush();
                session.clear();
                session.close();
            }
        }
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

            javax.mail.Session session = javax.mail.Session.getDefaultInstance(props, null);
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
            JOptionPane.showMessageDialog(null, "¡No se pudo enviar el correo error de red!"); 
        }
    }
}