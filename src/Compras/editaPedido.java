/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Compras;

import Compania.buscaCompania;
import Ejemplar.buscaEjemplar;
import Hibernate.Util.HibernateUtil;
import Hibernate.entidades.Compania;
import Marca.buscaMarca;
import Hibernate.entidades.Configuracion;
import Hibernate.entidades.Empleado;
import Hibernate.entidades.Marca;
import Hibernate.entidades.Orden;
import Hibernate.entidades.OrdenExterna;
import Hibernate.entidades.Partida;
import Hibernate.entidades.PartidaExterna;
import Hibernate.entidades.Pedido;
import Hibernate.entidades.Proveedor;   
import Hibernate.entidades.Tipo;
import Hibernate.entidades.Usuario;
import Proveedor.buscaProveedor;
import Tipo.buscaTipo;
import Empleados.buscaEmpleado;
import Hibernate.entidades.Ejemplar;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Vector;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import Integral.FormatoEditor;
import Integral.FormatoEditorTexto;
import Integral.FormatoTabla;
import Integral.Herramientas;
import Integral.HorizontalBarUI;
import Integral.Render1;
import Integral.VerticalBarUI;
import Integral.calendario;
import Servicios.buscaOrden;
import java.text.DateFormat;
import java.util.Date;
import javax.swing.DefaultCellEditor;
import org.hibernate.Criteria;
/**
 *
 * @author ESPECIALIZADO TOLUCA
 */
public class editaPedido extends javax.swing.JPanel {
    private Usuario usrAut;
    Usuario usr;
    int entro=0, x=0;
    double imp=0.0;
    static ResultSet rs;
    static Statement st;
    static Connection conn;
    DefaultTableModel temp;
    public Orden orden_act=null;
    public Proveedor prov_act=null;
    public Proveedor provf_act=null;
    public Partida [] part_act=null;
    MyModel model;
    Proveedor registro = null;
    Partida registropar = null;
    String sessionPrograma="";
    Herramientas h;
    Formatos f1;
    int menu=0;
    List elimina=new ArrayList();
    public Pedido pedido=null;
    String[] columnas = new String [] {
        "Interno","N0","#","N° Parte","Folio","Descripción","Med","Plazo","Cant","Costo c/u","Total"
    };
    FormatoTabla formato;
    /**
     * Crea una ventana para editar un Pedido existente
     * @param usuario Usuario del sistema.
     * @param ses Session con la que inicio el usuario.
     * @param ped Pedido a modificar.
     * @param ventana ventana en la que abrio el pedido.
     */
    public editaPedido(Usuario usuario, String ses, Pedido ped, int ventana) {
        usr=usuario;
        sessionPrograma=ses;
        pedido=ped;
        menu=ventana;
        initComponents();
        scroll.getVerticalScrollBar().setUI(new VerticalBarUI());
        scroll.getHorizontalScrollBar().setUI(new HorizontalBarUI());
        formato = new FormatoTabla();
        model=new MyModel(0, columnas);
        t_datos.setModel(model);
        t_datos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        r_autorizar.setEnabled(false);
        bloquea(false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false);
        titulos();
    }
    
    void estado()
    {
        String consulta="SELECT DISTINCT obj from Orden obj "
                      + "LEFT JOIN FETCH obj.partidasForIdOrden part "
                      + "LEFT JOIN partP.pedido ped "
                      + "where ped.idPedido = "+pedido.getIdPedido();
        Session session = HibernateUtil.getSessionFactory().openSession();
        try
        {
            session.beginTransaction();
            Query q = session.createQuery(consulta);
            List resultList = q.list();
        if(resultList.size()>0)
        {
            Orden[] actor = (Orden[]) resultList.get(0);
            actor[0].getIdOrden();
            if(actor[0].getFechaCierre()!=null)
            {
                this.t_proveedor.setEnabled(false);
                this.t_notas.setEnabled(false);
                JOptionPane.showMessageDialog(null, "¡Orden cerrada!");
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
                    session.close();                       
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

        autorizarCosto = new javax.swing.JDialog();
        jPanel7 = new javax.swing.JPanel();
        t_contra = new javax.swing.JPasswordField();
        jLabel2 = new javax.swing.JLabel();
        t_user = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        b_autorizar = new javax.swing.JButton();
        autorizarECosto = new javax.swing.JDialog();
        jPanel11 = new javax.swing.JPanel();
        t_contra1 = new javax.swing.JPasswordField();
        jLabel5 = new javax.swing.JLabel();
        t_user1 = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        b_autorizar1 = new javax.swing.JButton();
        medida = new javax.swing.JComboBox();
        numeros = new javax.swing.JComboBox();
        t_id_aseguradora = new javax.swing.JTextField();
        t_clave = new javax.swing.JTextField();
        t_proveedor = new javax.swing.JTextField();
        t_id_comprador = new javax.swing.JTextField();
        r_autorizar = new javax.swing.JRadioButton();
        jPanel4 = new javax.swing.JPanel();
        l_busca = new javax.swing.JLabel();
        b_busca = new javax.swing.JButton();
        t_busca = new javax.swing.JTextField();
        b_pedidos = new javax.swing.JButton();
        b_compra = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        l_iva = new javax.swing.JLabel();
        t_IVA = new javax.swing.JFormattedTextField();
        t_subtotal = new javax.swing.JFormattedTextField();
        l_subtotal = new javax.swing.JLabel();
        l_total = new javax.swing.JLabel();
        t_total = new javax.swing.JFormattedTextField();
        b_menos = new javax.swing.JButton();
        b_mas = new javax.swing.JButton();
        b_guardar = new javax.swing.JButton();
        b_recargar = new javax.swing.JButton();
        b_edita = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        t_notas = new javax.swing.JTextArea();
        l_notas = new javax.swing.JLabel();
        b_dcg = new javax.swing.JButton();
        cb_tipo = new javax.swing.JComboBox();
        r_autorizar2 = new javax.swing.JRadioButton();
        scroll = new javax.swing.JScrollPane();
        t_datos = new javax.swing.JTable();
        p_arriba = new javax.swing.JPanel();
        p_interno_centro = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jPanel12 = new javax.swing.JPanel();
        c_tipo = new javax.swing.JComboBox();
        t_pedido = new javax.swing.JTextField();
        t_fecha = new javax.swing.JTextField();
        t_folio_externo = new javax.swing.JTextField();
        l_pedido1 = new javax.swing.JLabel();
        b_calendario = new javax.swing.JButton();
        t_plazo = new javax.swing.JTextField();
        t_nombre_comprador = new javax.swing.JTextField();
        b_comprador = new javax.swing.JButton();
        b_busca_pedido = new javax.swing.JButton();
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
        jPanel5 = new javax.swing.JPanel();
        b_proveedor = new javax.swing.JButton();
        l_proveedor = new javax.swing.JLabel();
        jPanel13 = new javax.swing.JPanel();
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

        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "Autorización de costos mayores", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP));

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

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(b_autorizar)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(t_user)
                            .addComponent(t_contra, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(18, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(t_user, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
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
            .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        autorizarCostoLayout.setVerticalGroup(
            autorizarCostoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        autorizarECosto.setModalExclusionType(null);
        autorizarECosto.setModalityType(java.awt.Dialog.ModalityType.APPLICATION_MODAL);

        jPanel11.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "Autorización para modificar costos", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP));

        jLabel5.setFont(new java.awt.Font("Arial", 0, 10)); // NOI18N
        jLabel5.setText("Contraseña:");

        jLabel6.setFont(new java.awt.Font("Arial", 0, 10)); // NOI18N
        jLabel6.setText("Usuario:");

        b_autorizar1.setFont(new java.awt.Font("Arial", 0, 10)); // NOI18N
        b_autorizar1.setText("Autorizar");
        b_autorizar1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_autorizar1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(b_autorizar1)
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(t_user1)
                            .addComponent(t_contra1, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(18, Short.MAX_VALUE))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(t_user1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(t_contra1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(b_autorizar1)
                .addGap(23, 23, 23))
        );

        javax.swing.GroupLayout autorizarECostoLayout = new javax.swing.GroupLayout(autorizarECosto.getContentPane());
        autorizarECosto.getContentPane().setLayout(autorizarECostoLayout);
        autorizarECostoLayout.setHorizontalGroup(
            autorizarECostoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        autorizarECostoLayout.setVerticalGroup(
            autorizarECostoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        medida.setFont(new java.awt.Font("Dialog", 0, 9)); // NOI18N
        medida.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "PZAS", "GAL", "LTS", "MTS", "CMS", "MMS", "GRS", "MLS", "KGS", "HRS", "MIN", "KIT", "FT", "LB", "JGO", "NA" }));

        numeros.setFont(new java.awt.Font("Dialog", 0, 9)); // NOI18N
        numeros.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                numerosFocusLost(evt);
            }
        });

        t_id_aseguradora.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(0, 0, 0)));
        t_id_aseguradora.setDisabledTextColor(new java.awt.Color(2, 38, 253));
        t_id_aseguradora.setEnabled(false);

        t_clave.setEditable(false);
        t_clave.setBackground(new java.awt.Color(255, 255, 255));
        t_clave.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(0, 0, 0)));
        t_clave.setDisabledTextColor(new java.awt.Color(2, 38, 253));
        t_clave.setEnabled(false);

        t_proveedor.setEditable(false);
        t_proveedor.setBackground(new java.awt.Color(255, 255, 255));
        t_proveedor.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        t_proveedor.setDisabledTextColor(new java.awt.Color(2, 38, 253));
        t_proveedor.setEnabled(false);

        t_id_comprador.setEditable(false);
        t_id_comprador.setBackground(new java.awt.Color(255, 255, 255));
        t_id_comprador.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(0, 0, 0)));
        t_id_comprador.setDisabledTextColor(new java.awt.Color(2, 38, 253));

        r_autorizar.setFont(new java.awt.Font("Arial", 0, 10)); // NOI18N
        r_autorizar.setForeground(new java.awt.Color(2, 38, 253));
        r_autorizar.setText("Autorizacion 1");
        r_autorizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                r_autorizarActionPerformed(evt);
            }
        });

        setBackground(new java.awt.Color(255, 255, 255));
        setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "Modificación de Pedidos", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Arial", 1, 12))); // NOI18N
        setLayout(new java.awt.BorderLayout());

        l_busca.setFont(new java.awt.Font("Arial", 0, 9)); // NOI18N
        l_busca.setText("Buscar:");

        b_busca.setIcon(new ImageIcon("imagenes/buscar1.png"));
        b_busca.setToolTipText("Busca una partida");
        b_busca.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_buscaActionPerformed(evt);
            }
        });

        t_busca.setFont(new java.awt.Font("Arial", 0, 10)); // NOI18N
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

        b_pedidos.setBackground(new java.awt.Color(90, 66, 126));
        b_pedidos.setFont(new java.awt.Font("Arial", 0, 9)); // NOI18N
        b_pedidos.setText("Pedido");
        b_pedidos.setEnabled(false);
        b_pedidos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_pedidosActionPerformed(evt);
            }
        });

        b_compra.setBackground(new java.awt.Color(90, 66, 126));
        b_compra.setFont(new java.awt.Font("Arial", 0, 9)); // NOI18N
        b_compra.setText("Ord. de Compra");
        b_compra.setEnabled(false);
        b_compra.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_compraActionPerformed(evt);
            }
        });

        jPanel6.setBackground(new java.awt.Color(2, 135, 242));
        jPanel6.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        l_iva.setFont(new java.awt.Font("Arial", 0, 9)); // NOI18N
        l_iva.setForeground(new java.awt.Color(255, 255, 255));
        l_iva.setText("I.V.A.:");
        jPanel6.add(l_iva, new org.netbeans.lib.awtextra.AbsoluteConstraints(21, 20, -1, -1));

        t_IVA.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(0, 0, 0)));
        t_IVA.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.00"))));
        t_IVA.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        t_IVA.setText("0.00");
        t_IVA.setDisabledTextColor(new java.awt.Color(2, 38, 253));
        t_IVA.setEnabled(false);
        t_IVA.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        jPanel6.add(t_IVA, new org.netbeans.lib.awtextra.AbsoluteConstraints(55, 20, 80, -1));

        t_subtotal.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(0, 0, 0)));
        t_subtotal.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.00"))));
        t_subtotal.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        t_subtotal.setText("0.00");
        t_subtotal.setDisabledTextColor(new java.awt.Color(2, 38, 253));
        t_subtotal.setEnabled(false);
        t_subtotal.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        jPanel6.add(t_subtotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(55, 0, 80, -1));

        l_subtotal.setFont(new java.awt.Font("Arial", 0, 9)); // NOI18N
        l_subtotal.setForeground(new java.awt.Color(255, 255, 255));
        l_subtotal.setText("Subtotal:");
        jPanel6.add(l_subtotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(12, 4, -1, -1));

        l_total.setFont(new java.awt.Font("Arial", 0, 9)); // NOI18N
        l_total.setForeground(new java.awt.Color(255, 255, 255));
        l_total.setText("Total:");
        jPanel6.add(l_total, new org.netbeans.lib.awtextra.AbsoluteConstraints(23, 40, -1, -1));

        t_total.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(0, 0, 0)));
        t_total.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.00"))));
        t_total.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        t_total.setText("0.00");
        t_total.setDisabledTextColor(new java.awt.Color(2, 38, 253));
        t_total.setEnabled(false);
        t_total.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        jPanel6.add(t_total, new org.netbeans.lib.awtextra.AbsoluteConstraints(55, 40, 80, -1));

        b_menos.setBackground(new java.awt.Color(90, 66, 126));
        b_menos.setIcon(new ImageIcon("imagenes/boton_menos.png"));
        b_menos.setToolTipText("Elimina la partida seleccionada");
        b_menos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_menosActionPerformed(evt);
            }
        });

        b_mas.setBackground(new java.awt.Color(90, 66, 126));
        b_mas.setIcon(new ImageIcon("imagenes/boton_mas.png"));
        b_mas.setToolTipText("Agrega una partida");
        b_mas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_masActionPerformed(evt);
            }
        });

        b_guardar.setBackground(new java.awt.Color(90, 66, 126));
        b_guardar.setFont(new java.awt.Font("Arial", 0, 9)); // NOI18N
        b_guardar.setText("Guardar");
        b_guardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_guardarActionPerformed(evt);
            }
        });

        b_recargar.setBackground(new java.awt.Color(90, 66, 126));
        b_recargar.setFont(new java.awt.Font("Arial", 0, 9)); // NOI18N
        b_recargar.setText("Recargar");
        b_recargar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_recargarActionPerformed(evt);
            }
        });

        b_edita.setBackground(new java.awt.Color(90, 66, 126));
        b_edita.setFont(new java.awt.Font("Arial", 0, 9)); // NOI18N
        b_edita.setText("Editar");
        b_edita.setEnabled(false);
        b_edita.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_editaActionPerformed(evt);
            }
        });

        t_notas.setColumns(20);
        t_notas.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        t_notas.setRows(3);
        jScrollPane2.setViewportView(t_notas);

        l_notas.setBackground(new java.awt.Color(254, 254, 254));
        l_notas.setFont(new java.awt.Font("Arial", 0, 10)); // NOI18N
        l_notas.setText("Notas:");

        b_dcg.setBackground(new java.awt.Color(90, 66, 126));
        b_dcg.setFont(new java.awt.Font("Arial", 0, 9)); // NOI18N
        b_dcg.setText("DCG");
        b_dcg.setEnabled(false);
        b_dcg.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_dcgActionPerformed(evt);
            }
        });

        cb_tipo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "ORDEN", "PEDIDO" }));

        r_autorizar2.setFont(new java.awt.Font("Arial", 0, 10)); // NOI18N
        r_autorizar2.setForeground(new java.awt.Color(2, 38, 253));
        r_autorizar2.setText("Autorizacion 2");
        r_autorizar2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                r_autorizar2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(b_mas, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(b_menos, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(l_busca)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(t_busca, javax.swing.GroupLayout.PREFERRED_SIZE, 193, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(b_busca, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(r_autorizar2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(b_pedidos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(b_edita, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(b_compra)
                    .addComponent(b_guardar, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(b_dcg, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(b_recargar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(l_notas)
                        .addGap(6, 6, 6))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cb_tipo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 71, Short.MAX_VALUE)))
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 227, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(l_notas)
                                .addComponent(b_edita)
                                .addComponent(b_guardar)
                                .addComponent(b_recargar))
                            .addComponent(b_mas, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(b_menos, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(b_busca, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(t_busca, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(l_busca))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(b_pedidos)
                                .addComponent(b_compra)
                                .addComponent(b_dcg)
                                .addComponent(cb_tipo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(r_autorizar2)))
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addComponent(jPanel6, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGap(1, 1, 1))
        );

        add(jPanel4, java.awt.BorderLayout.SOUTH);

        scroll.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(2, 135, 242)));

        t_datos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "interno", "#", "R. Valua", "N° Parte", "Folio", "Descripción", "Medida", "Plazo", "Cantidad", "Costo c/u", "Total"
            }
        ));
        t_datos.setAutoscrolls(false);
        t_datos.getTableHeader().setReorderingAllowed(false);
        t_datos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                t_datosMouseClicked(evt);
            }
        });
        scroll.setViewportView(t_datos);
        t_datos.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);

        add(scroll, java.awt.BorderLayout.CENTER);

        p_arriba.setBackground(new java.awt.Color(254, 254, 254));
        p_arriba.setLayout(new java.awt.BorderLayout());

        p_interno_centro.setBackground(new java.awt.Color(254, 254, 254));
        p_interno_centro.setLayout(new java.awt.GridLayout(1, 0));

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setLayout(new java.awt.BorderLayout());

        jPanel12.setBackground(new java.awt.Color(254, 254, 254));
        jPanel12.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "Pedido", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Arial", 1, 11))); // NOI18N

        c_tipo.setFont(new java.awt.Font("Droid Sans", 0, 10)); // NOI18N
        c_tipo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Interno", "Externo", "Adicional", "Inventario" }));
        c_tipo.setEnabled(false);

        t_pedido.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        t_pedido.setDisabledTextColor(new java.awt.Color(2, 38, 253));
        t_pedido.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                t_pedidoActionPerformed(evt);
            }
        });

        t_fecha.setEditable(false);
        t_fecha.setBackground(new java.awt.Color(255, 255, 255));
        t_fecha.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        t_fecha.setText("DD-MM-YYYY HH:MM:SS");
        t_fecha.setToolTipText("fecha de pedido");
        t_fecha.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(0, 0, 0)));
        t_fecha.setDisabledTextColor(new java.awt.Color(2, 38, 253));

        t_folio_externo.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        t_folio_externo.setDisabledTextColor(new java.awt.Color(2, 38, 253));

        l_pedido1.setFont(new java.awt.Font("Arial", 0, 10)); // NOI18N
        l_pedido1.setText("N° externo:");

        b_calendario.setBackground(new java.awt.Color(90, 66, 126));
        b_calendario.setText("Plazo");
        b_calendario.setToolTipText("Calendario");
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

        b_comprador.setBackground(new java.awt.Color(90, 66, 126));
        b_comprador.setText("Comprador");
        b_comprador.setToolTipText("Busca un proveedor");
        b_comprador.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_compradorActionPerformed(evt);
            }
        });

        b_busca_pedido.setBackground(new java.awt.Color(90, 66, 126));
        b_busca_pedido.setForeground(new java.awt.Color(255, 255, 0));
        b_busca_pedido.setText("Pedido");
        b_busca_pedido.setToolTipText("Calendario");
        b_busca_pedido.setMaximumSize(new java.awt.Dimension(32, 8));
        b_busca_pedido.setMinimumSize(new java.awt.Dimension(32, 8));
        b_busca_pedido.setPreferredSize(new java.awt.Dimension(32, 8));
        b_busca_pedido.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_busca_pedidoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(b_busca_pedido, javax.swing.GroupLayout.DEFAULT_SIZE, 69, Short.MAX_VALUE)
                    .addComponent(b_calendario, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(10, 10, 10)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(t_plazo)
                    .addComponent(t_pedido, javax.swing.GroupLayout.DEFAULT_SIZE, 85, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addComponent(c_tipo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(l_pedido1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(t_folio_externo, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(t_fecha, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addComponent(b_comprador)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(t_nombre_comprador)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(c_tipo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(b_busca_pedido, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(t_pedido, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(l_pedido1)
                    .addComponent(t_folio_externo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(t_fecha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(3, 3, 3)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(b_calendario, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(t_plazo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(b_comprador, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(t_nombre_comprador, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.add(jPanel12, java.awt.BorderLayout.NORTH);

        jPanel8.setBackground(new java.awt.Color(255, 255, 255));
        jPanel8.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "Orden", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Arial", 1, 11))); // NOI18N

        t_orden.setEditable(false);
        t_orden.setBackground(new java.awt.Color(255, 255, 255));
        t_orden.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(0, 0, 0)));
        t_orden.setDisabledTextColor(new java.awt.Color(2, 38, 253));

        b_orden.setBackground(new java.awt.Color(90, 66, 126));
        b_orden.setText("Orden");
        b_orden.setToolTipText("Busca una orden");
        b_orden.setEnabled(false);
        b_orden.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_ordenActionPerformed(evt);
            }
        });

        t_tipo.setEditable(false);
        t_tipo.setBackground(new java.awt.Color(255, 255, 255));
        t_tipo.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(0, 0, 0)));
        t_tipo.setDisabledTextColor(new java.awt.Color(2, 38, 253));

        b_tipo.setBackground(new java.awt.Color(90, 66, 126));
        b_tipo.setText("Tipo");
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

        b_marca.setBackground(new java.awt.Color(90, 66, 126));
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

        b_aseguradora.setBackground(new java.awt.Color(90, 66, 126));
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
                        .addComponent(b_tipo)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(t_tipo, javax.swing.GroupLayout.DEFAULT_SIZE, 52, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(b_marca, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(t_marca, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(l_modelo)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(t_modelo, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE))
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
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(b_tipo, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(t_tipo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(t_orden, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(b_orden, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(b_marca, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(t_marca, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(l_modelo)
                        .addComponent(t_modelo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(10, 10, 10)
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

        jPanel5.setBackground(new java.awt.Color(254, 254, 254));
        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "Proveedor", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Arial", 1, 11))); // NOI18N

        b_proveedor.setIcon(new ImageIcon("imagenes/buscar1.png"));
        b_proveedor.setToolTipText("Busca proveedor");
        b_proveedor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_proveedorActionPerformed(evt);
            }
        });

        l_proveedor.setBackground(new java.awt.Color(255, 255, 255));
        l_proveedor.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        l_proveedor.setText("SELECCIONE UN PROVEEDOR");
        l_proveedor.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(0, 0, 0)));

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(l_proveedor, javax.swing.GroupLayout.DEFAULT_SIZE, 493, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(b_proveedor, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(b_proveedor, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(3, 3, 3)
                .addComponent(l_proveedor, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel5, java.awt.BorderLayout.NORTH);

        jPanel13.setBackground(new java.awt.Color(254, 254, 254));
        jPanel13.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Arial", 1, 11)), "Facturar a", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Arial", 1, 11))); // NOI18N

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

        b_proveedorf.setIcon(new ImageIcon("imagenes/buscar1.png"));
        b_proveedorf.setToolTipText("Busca un proveedor");
        b_proveedorf.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_proveedorfActionPerformed(evt);
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
                        .addComponent(l_poblacion)
                        .addGap(9, 9, 9)
                        .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(t_colonia)
                            .addGroup(jPanel13Layout.createSequentialGroup()
                                .addComponent(t_poblacion, javax.swing.GroupLayout.DEFAULT_SIZE, 312, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(l_rfc)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(t_rfc, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addComponent(l_colonia)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel13Layout.createSequentialGroup()
                                .addComponent(l_direccion)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(t_direccion)
                                .addGap(8, 8, 8)
                                .addComponent(l_cp)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(t_cp, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel13Layout.createSequentialGroup()
                                .addComponent(l_nombre)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(t_nombre)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(b_proveedorf, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(4, 4, 4)))
                .addContainerGap())
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(b_proveedorf, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(l_nombre)
                        .addComponent(t_nombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(l_direccion, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(t_direccion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(l_cp)
                        .addComponent(t_cp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(l_colonia)
                    .addComponent(t_colonia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(l_rfc)
                        .addComponent(t_rfc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(l_poblacion)
                        .addComponent(t_poblacion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(22, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel13, java.awt.BorderLayout.CENTER);

        p_interno_centro.add(jPanel1);

        p_arriba.add(p_interno_centro, java.awt.BorderLayout.CENTER);

        add(p_arriba, java.awt.BorderLayout.NORTH);
    }// </editor-fold>//GEN-END:initComponents

    private void b_buscaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_buscaActionPerformed
        consulta();
    }//GEN-LAST:event_b_buscaActionPerformed

    private void t_buscaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_t_buscaActionPerformed
        consulta();
    }//GEN-LAST:event_t_buscaActionPerformed

    private void t_buscaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_buscaKeyTyped
        evt.setKeyChar(Character.toUpperCase(evt.getKeyChar()));
        if(t_busca.getText().length()>=30)
            evt.consume();
    }//GEN-LAST:event_t_buscaKeyTyped

    private void b_pedidosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_pedidosActionPerformed
        Formatos f1;
        if(this.c_tipo.getSelectedItem().toString().compareTo("Externo")==0 || this.c_tipo.getSelectedItem().toString().compareTo("Inventario")==0)
            f1=new Formatos(this.usr, this.sessionPrograma, null, t_pedido.getText());
        else
            f1=new Formatos(this.usr, this.sessionPrograma, this.orden_act, t_pedido.getText());
            
        if(this.c_tipo.getSelectedItem().toString().compareTo("Interno")==0)
            f1.pedidos();
        else
            f1.pedidosExternos(Integer.parseInt(this.t_pedido.getText()));
    }//GEN-LAST:event_b_pedidosActionPerformed

    private void b_compraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_compraActionPerformed
        Formatos f1;
        if(this.c_tipo.getSelectedItem().toString().compareTo("Interno")==0)
        {
            f1=new Formatos(this.usr, this.sessionPrograma, this.orden_act, t_pedido.getText());
            f1.ordenCompra();
        }
        else
        {
            if(this.c_tipo.getSelectedItem().toString().compareTo("Externo")==0)
                f1=new Formatos(this.usr, this.sessionPrograma, null, t_pedido.getText());
            else
                f1=new Formatos(this.usr, this.sessionPrograma, this.orden_act, t_pedido.getText());
            f1.ordenCompraExternos(Integer.parseInt(this.t_pedido.getText()));
        }
    }//GEN-LAST:event_b_compraActionPerformed

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

    private void b_guardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_guardarActionPerformed
        // TODO add your handling code here:
        if(t_clave.getText().compareTo("")!=0)
        {
            if(t_proveedor.getText().compareTo("")!=0)
            {
                if(t_datos.getRowCount()!=0)
                {
                    if(actualizaPedido()==true)
                    {
                        bloquea(false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, true, true, false, true);
                        model.setColumnaEditable(0, false);
                        model.setColumnaEditable(1, false);
                        model.setColumnaEditable(2, false);
                        model.setColumnaEditable(3, false);
                        model.setColumnaEditable(4, false);
                        model.setColumnaEditable(5, false);
                        model.setColumnaEditable(6, false);
                        model.setColumnaEditable(7, false);
                        model.setColumnaEditable(8, false);
                        model.setColumnaEditable(9, false);
                        JOptionPane.showMessageDialog(null, "Pedido actualizado");
                    }
                }
                else
                        JOptionPane.showMessageDialog(null, "Selecciona almenos una partida");
            }
            else
                JOptionPane.showMessageDialog(null, "Selecciona un proveedor");
        }
        else
            JOptionPane.showMessageDialog(null, "Selecciona una empresa a la que se va a facturar");
    }//GEN-LAST:event_b_guardarActionPerformed

    private void b_recargarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_recargarActionPerformed
        // TODO add your handling code here:
        elimina=new ArrayList();
        model=new MyModel(0, columnas);
        t_datos.setModel(model);
        busca();
    }//GEN-LAST:event_b_recargarActionPerformed

    private void t_datosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_t_datosMouseClicked
        // TODO add your handling code here:
        h=new Herramientas(usr, 0);
        h.session(sessionPrograma);
        if(t_datos.getSelectedRow()>=0)
        {
            if(t_datos.getSelectedColumn()==7) 
            {
                if(this.r_autorizar.isSelected()==false)
                {
                    if(b_guardar.isEnabled())
                    {
                        calendario cal =new calendario(new javax.swing.JFrame(), true);
                        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
                        cal.setLocation((d.width/2)-(cal.getWidth()/2), (d.height/2)-(cal.getHeight()/2));
                        cal.setVisible(true);
                        Calendar miCalendario=cal.getReturnStatus();
                        if(miCalendario!=null)
                        {
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                            t_datos.setValueAt(sdf.format(miCalendario.getTime()),t_datos.getSelectedRow(),7);
                        }
                        else
                            t_datos.setValueAt("0",t_datos.getSelectedRow(),7);
                    }
                }
            }
        }
    }//GEN-LAST:event_t_datosMouseClicked

    private void b_editaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_editaActionPerformed
        // TODO add your handling code here:
        usrAut=null;
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        autorizarECosto.setSize(284, 192);//284, 177
        t_user1.setText("");
        t_contra1.setText("");
        autorizarECosto.setLocation((d.width/2)-(autorizarECosto.getWidth()/2), (d.height/2)-(autorizarECosto.getHeight()/2));
        autorizarECosto.setVisible(true);
        if(usrAut!=null)
        {
            Session session = HibernateUtil.getSessionFactory().openSession();
            try
            {
                session.beginTransaction().begin();
                Query query2 = session.createSQLQuery("select ( (select if( sum(movimiento.cantidad) is null, 0, sum(movimiento.cantidad)) as can " +
                        "from movimiento inner join almacen on movimiento.id_almacen=almacen.id_almacen where id_pedido="+pedido.getIdPedido()+" and almacen.tipo_movimiento=1 and almacen.operacion in (1, 2, 3)) " +
                        "- " +
                        "(select if( sum(movimiento.cantidad) is null, 0, sum(movimiento.cantidad)) as can " +
                        "from movimiento inner join almacen on movimiento.id_almacen=almacen.id_almacen where id_pedido="+pedido.getIdPedido()+" and almacen.tipo_movimiento=2 and almacen.operacion in (1, 2, 3))) as almacen;");
                query2.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
                ArrayList resp = (ArrayList)query2.list();
                java.util.HashMap map=(java.util.HashMap)resp.get(0);
                if(Double.parseDouble(map.get("almacen").toString())==0.0d)
                {
                    bloquea(true, true, true, true, false, false, false, false, false, false, false, false, false, false, true, true, false, false, false, false);
                    model=(MyModel)t_datos.getModel();
                    model.setColumnaEditable(8, true);
                    model.setColumnaEditable(9, true);
                }
                else
                {
                    JOptionPane.showMessageDialog(this, "El pedido ya tiene movimientos en el almacen");
                }
            }catch(Exception e)
            {
                e.printStackTrace();
                r_autorizar2.setSelected(true);
                session.beginTransaction().rollback();
                JOptionPane.showMessageDialog(this, "Error al autorizar edición.");
            }
            finally
            {
                if(session.isOpen()==true)
                session.close();
            }
        }
        
    }//GEN-LAST:event_b_editaActionPerformed

    private void b_autorizar1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_autorizar1ActionPerformed
        // TODO add your handling code here:
        if(t_user1.getText().compareTo("")!=0)
        {
            if(t_contra1.getPassword().toString().compareTo("")!=0)
            {
                Session session = HibernateUtil.getSessionFactory().openSession();
                try
                {
                    session.beginTransaction().begin();
                    Usuario autoriza = (Usuario)session.createCriteria(Usuario.class).add(Restrictions.eq("idUsuario", t_user1.getText())).add(Restrictions.eq("clave", t_contra1.getText())).setMaxResults(1).uniqueResult();
                    if(autoriza!=null)
                    {
                        if(autoriza.getEditaPrecioAutorizado()==true)
                        {
                            usrAut=autoriza;
                            autorizarECosto.dispose();
                        }
                        else
                            JOptionPane.showMessageDialog(this, "¡El usuario no tiene permiso de autorizar!");
                    }
                    else
                    {
                        session.beginTransaction().rollback();
                        JOptionPane.showMessageDialog(this, "¡Datos Incorrectos!");
                        t_user1.requestFocus();
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
            {
                JOptionPane.showMessageDialog(this, "¡Ingrese la contraseña!");
                t_contra1.requestFocus();
            }
        }
        else
        {
            JOptionPane.showMessageDialog(this, "¡Ingrese el usuario!");
            t_user1.requestFocus();
        }
    }//GEN-LAST:event_b_autorizar1ActionPerformed

    private void b_calendarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_calendarioActionPerformed
        h=new Herramientas(usr, 0);
        h.session(sessionPrograma);
        calendario cal =new calendario(new javax.swing.JFrame(), true);
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
                    Partida part=(Partida)session.createCriteria(Partida.class).add(Restrictions.eq("ordenByIdOrden.idOrden", Integer.parseInt(t_orden.getText()))).add(Restrictions.eq("idEvaluacion", Integer.parseInt(t_datos.getValueAt(ren, 1).toString()))).add(Restrictions.eq("subPartida", Integer.parseInt(t_datos.getValueAt(ren, 2).toString()))).setMaxResults(1).uniqueResult();
                    if(part!=null)
                    {
                        session.beginTransaction().begin();
                        try{
                            String [] fecha;
                            Calendar calendario;
                            String dia=Integer.toString(miCalendario.get(Calendar.DATE));
                            String mes = Integer.toString(miCalendario.get(Calendar.MONTH)+1);
                            String anio = Integer.toString(miCalendario.get(Calendar.YEAR));
                            String valor=anio+"-"+mes+"-"+dia;
                            fecha = valor.split("-");
                            calendario = Calendar.getInstance();
                            calendario.set(
                                Integer.parseInt(fecha[0]),
                                Integer.parseInt(fecha[1])-1,
                                Integer.parseInt(fecha[2]));
                            part.setPlazo(calendario.getTime());
                            session.update(part);
                            session.getTransaction().commit();
                            t_plazo.setText(anio+"-"+mes+"-"+dia);
                            t_datos.setValueAt(valor,ren,7);
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
                    t_datos.setValueAt(sdf.format(miCalendario.getTime()),r,7);
                }
            }
        }
    }//GEN-LAST:event_b_calendarioActionPerformed

    private void t_plazoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_t_plazoActionPerformed
        b_orden.requestFocus();
    }//GEN-LAST:event_t_plazoActionPerformed

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

    private void b_ordenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_ordenActionPerformed
        h=new Herramientas(usr, menu);
        h.session(sessionPrograma);
        buscaOrden obj = new buscaOrden(new javax.swing.JFrame(), true, this.usr,0);
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
                if(orden_act.getInicioRefacciones()!=null)
                {
                    if(orden_act.getFechaCierre()==null)
                    {
                        h= new Herramientas(usr, menu);
                        h.desbloqueaOrden();
                        String resp=h.estadoOrden(orden_act);
                        if(resp.compareTo("")==0 || resp.compareTo("*bloqueada ok*")==0)
                        {
                            t_orden.setText(""+orden_act.getIdOrden());
                            if(t_orden.getText().compareTo("")!=0)
                            {
                                datos_unidad("", "", "", "", "", "","", "","");
                                if (orden_act!=null)
                                {
                                    Session session = HibernateUtil.getSessionFactory().openSession();
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
                        {
                            JOptionPane.showMessageDialog(null, "¡Orden Bloqueada por:"+orden_act.getUsuarioByBloqueada().getIdUsuario());
                        }
                    }
                    else
                    JOptionPane.showMessageDialog(null, "¡Orden cerrada!");
                }
                else
                JOptionPane.showMessageDialog(null, "¡Aún no esta disponible!");
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
        }catch(Exception e){}
    }//GEN-LAST:event_b_ordenActionPerformed

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

    private void b_marcaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_marcaActionPerformed
        // TODO add your handling code here:
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
        t_marca.setText(actor.getIdMarca());
        else
        t_marca.setText("");
    }//GEN-LAST:event_b_marcaActionPerformed

    private void t_modeloKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_modeloKeyTyped
        // TODO add your handling code here:
        char car = evt.getKeyChar();
        if(t_modelo.getText().length()>=4)
        evt.consume();
        if((car<'0' || car>'9'))
        evt.consume();
    }//GEN-LAST:event_t_modeloKeyTyped

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

    private void b_proveedorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_proveedorActionPerformed
        h=new Herramientas(usr, 0);
        h.session(sessionPrograma);
        buscaProveedor obj = new buscaProveedor(new javax.swing.JFrame(), true, this.usr, this.sessionPrograma);
        obj.t_busca.requestFocus();
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        obj.setLocation((d.width/2)-(obj.getWidth()/2), (d.height/2)-(obj.getHeight()/2));
        obj.setVisible(true);
        prov_act=obj.getReturnStatus();
        if (prov_act!=null)
        {
            t_proveedor.setText(""+prov_act.getIdProveedor());
            prov_act=buscarProveedor(prov_act.getIdProveedor());
            l_proveedor.setText(prov_act.getNombre());
        }
        else
        {
            t_proveedor.setText("");
            l_proveedor.setText("SELECCIONE UN PROVEEDOR");
            t_proveedor.requestFocus();
        }
    }//GEN-LAST:event_b_proveedorActionPerformed

    private void b_proveedorfActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_proveedorfActionPerformed
        h=new Herramientas(usr, 0);
        h.session(sessionPrograma);
        buscaProveedor obj = new buscaProveedor(new javax.swing.JFrame(), true, this.usr, this.sessionPrograma);
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

    private void b_menosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_menosActionPerformed
        h=new Herramientas(usr, 0);
        h.session(sessionPrograma);
        int [] renglones = t_datos.getSelectedRows();
        if(renglones.length>=0)
        {
            int opt=JOptionPane.showConfirmDialog(this, "¡La partida se eliminará!");
            if (JOptionPane.YES_OPTION == opt)
            {
                for(int x=0; x<renglones.length; x++)
                {
                    if(t_datos.getValueAt(t_datos.getSelectedRow(), 0).toString().compareTo("")!=0)
                        elimina.add(Integer.parseInt(t_datos.getValueAt(t_datos.getSelectedRow(), 0).toString()));
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
        JOptionPane.showMessageDialog(null, "¡Selecciona la partida a eliminar!");
    }//GEN-LAST:event_b_menosActionPerformed

    private void b_masActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_masActionPerformed
        h=new Herramientas(usr, 0);
        h.session(sessionPrograma);

        if(this.c_tipo.getSelectedItem().toString().compareTo("Interno")==0)
        {
            Session session = HibernateUtil.getSessionFactory().openSession();
            if(t_orden.getText().compareTo("")!=0)
            {
                buscaPartida obj = new buscaPartida(this.t_orden.getText(), new javax.swing.JFrame(), true, 1, ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
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
                            part_act[x]=(Partida)session.get(Partida.class, part_act[x].getIdPartida());
                            if(buscaTabla(part_act[x].getIdEvaluacion())==-1)
                            {
                                int plazo= 0;
                                String codigo = "";
                                if(part_act[x].getEjemplar()!=null)
                                    codigo=part_act[x].getEjemplar().getIdParte();
                                try
                                {
                                    if(part_act[x].getPlazo()!=null)
                                        plazo=Integer.parseInt(part_act[x].getPlazo().toString());
                                }
                                catch(Exception e){}
                                double cantidad = 0.0d;
                                double costo = 0.0d;
                                if(part_act[x].getCantPcp()!=null)
                                    cantidad=part_act[x].getCantPcp();
                                else
                                    cantidad=part_act[x].getCantidadAut();
                                if(part_act[x].getPcp()!=null)
                                    costo=part_act[x].getPcp();

                                //DefaultTableModel model = (DefaultTableModel) t_datos.getModel();
                                String anotacion="";
                                if(part_act[x].getInstruccion()!=null)
                                    anotacion=part_act[x].getInstruccion();
                                Object[] vector=new Object[]{""+part_act[x].getIdPartida(),""+part_act[x].getIdEvaluacion()/*#*/,""+part_act[x].getSubPartida()/*R_valua*/,codigo/*codigo*/,""+part_act[x].getCatalogo().getIdCatalogo()/*folio*/,""+part_act[x].getCatalogo().getNombre()+" "+anotacion/*descripción*/,""+part_act[x].getMed()/*medida*/,plazo/*plazo*/,cantidad/*cantidad*/,costo/*costo c/u*/,cantidad*costo/*total*/};
                                model.addRow(vector);
                                model.setCeldaEditable(t_datos.getRowCount()-1, 3, true);
                            }
                        }
                        sumaTotales();
                                titulos();
                                if(t_datos.getRowCount()!=0)
                                b_calendario.setEnabled(true);
                                else
                                b_calendario.setEnabled(false);
                    }
                }
                catch(Exception e)
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
            JOptionPane.showMessageDialog(null, "¡Selecciona una orden de taller!");
        }
        if(this.c_tipo.getSelectedItem().toString().compareTo("Externo")==0)
        {
            int pos=t_datos.getRowCount()+1;
            Object[] vector=new Object[]{""/**Interno*/,""/*#*/,""/*R_valua*/,""/*codigo*/,""+"s/f"/*folio*/,""/*descripción*/,""+""/*medida*/,"0"/*plazo*/,1.0/*cantidad*/,0.0/*costo c/u*/,0.0/*total*/};
            model.addRow(vector);
            for(int x=0; x<t_datos.getColumnCount(); x++)
            {
                if(x==0 || x==4 || x==7 || x==10)
                model.setCeldaEditable(t_datos.getRowCount()-1, x, false);
                else
                model.setCeldaEditable(t_datos.getRowCount()-1, x, true);
            }
            sumaTotales();
            if(t_datos.getRowCount()!=0)
            b_calendario.setEnabled(true);
            else
            b_calendario.setEnabled(false);
        }
        if(this.c_tipo.getSelectedItem().toString().compareTo("Adicional")==0)
        {
            int pos=t_datos.getRowCount()+1;
            Object[] vector=new Object[]{""/**Interno*/,""/*#*/,""/*R_valua*/,""/*codigo*/,""+"s/f"/*folio*/,""/*descripción*/,""+""/*medida*/,"0"/*plazo*/,1.0/*cantidad*/,0.0/*costo c/u*/,0.0/*total*/};
            model.addRow(vector);
            for(int x=0; x<t_datos.getColumnCount(); x++)
            {
                if(x==0 || x==1 || x==4 || x==7 || x==10)
                    model.setCeldaEditable(t_datos.getRowCount()-1, x, false);
                else
                    model.setCeldaEditable(t_datos.getRowCount()-1, x, true);
            }
            sumaTotales();
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
                Object[] vector=new Object[]{""/**Interno*/,""/*#*/,""/*R_valua*/,eje.getIdParte()/*codigo*/,""+"s/f"/*folio*/,eje.getCatalogo()/*descripción*/,""+eje.getMedida()/*medida*/,""/*plazo*/,1.0/*cantidad*/,0.0/*costo c/u*/,0.0/*total*/};
                model.addRow(vector);
                for(int x=0; x<t_datos.getColumnCount(); x++)
                {
                    if(x==8 || x==9)
                        model.setCeldaEditable(t_datos.getRowCount()-1, x, true);
                    else
                        model.setCeldaEditable(t_datos.getRowCount()-1, x, false);
                }
                sumaTotales();
                if(t_datos.getRowCount()!=0)
                    b_calendario.setEnabled(true);
                else
                    b_calendario.setEnabled(false);
            }
        }
    }//GEN-LAST:event_b_masActionPerformed

    private void numerosFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_numerosFocusLost
        // TODO add your handling code here:
        entro=1;
    }//GEN-LAST:event_numerosFocusLost

    private void b_busca_pedidoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_busca_pedidoActionPerformed
        // TODO add your handling code here:
        try
        {
            buscaPedido obj = new buscaPedido(new javax.swing.JFrame(), true, 0, "");
            obj.t_busca.requestFocus();
            Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
            obj.setLocation((d.width/2)-(obj.getWidth()/2), (d.height/2)-(obj.getHeight()/2));
            obj.setVisible(true);

            Pedido ped=obj.getReturnStatus();
            if(ped!=null)
            {
                this.pedido=ped;
                busca();
            }
        }catch(Exception e)
        {
            System.out.println(e);
        }
    }//GEN-LAST:event_b_busca_pedidoActionPerformed

    private void b_dcgActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_dcgActionPerformed
        // TODO add your handling code here:
        Formatos f1;
        if(this.c_tipo.getSelectedItem().toString().compareTo("Interno")==0)
        {
            f1=new Formatos(this.usr, this.sessionPrograma, this.orden_act, t_pedido.getText());
            f1.ordenCompraDCG(cb_tipo.getSelectedItem().toString());
        }
        else
        {
            if(this.c_tipo.getSelectedItem().toString().compareTo("Externo")==0)
                f1=new Formatos(this.usr, this.sessionPrograma, null, t_pedido.getText());
            else
                f1=new Formatos(this.usr, this.sessionPrograma, this.orden_act, t_pedido.getText());
            f1.ordenCompraExternosDCG(Integer.parseInt(this.t_pedido.getText()), cb_tipo.getSelectedItem().toString());
        }
    }//GEN-LAST:event_b_dcgActionPerformed

    private void t_pedidoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_t_pedidoActionPerformed
        // TODO add your handling code here:
        h= new Herramientas(usr, menu);
        h.session(sessionPrograma);
        h.desbloqueaOrden();
        h.desbloqueaPedido();
        Session session = HibernateUtil.getSessionFactory().openSession();
        try
        {
            session.beginTransaction().begin();
            pedido = (Pedido)session.get(Pedido.class, Integer.parseInt(t_pedido.getText()));
            session.beginTransaction().commit();
        }catch(Exception e){e.printStackTrace();}
        finally
        {
            if(session!=null)
                if(session.isOpen())
                    session.close();
        }
        busca();
    }//GEN-LAST:event_t_pedidoActionPerformed

    private void r_autorizar2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_r_autorizar2ActionPerformed
        // TODO add your handling code here:
        if(this.r_autorizar2.isSelected()==true)
        {
            Session session = HibernateUtil.getSessionFactory().openSession();
            try
            {
                session.beginTransaction().begin();
                usr=(Usuario)session.get(Usuario.class, usr.getIdUsuario());
                pedido = (Pedido)session.get(Pedido.class, Integer.parseInt(t_pedido.getText()));
                if(usr.getAutorizarPedidos()==true)
                {
                    //if(pedido.getUsuarioByAutorizo()==null || pedido.getUsuarioByAutorizo().getIdUsuario().compareTo(usr.getIdUsuario())!=0)
                    //{
                        pedido.setUsuarioByAutorizo2(usr);
                        Date fecha_autorizo = new Date();
                        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                        String valor=dateFormat.format(fecha_autorizo);
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
                        pedido.setFechaAutorizo2(calendario.getTime());
                        session.update(pedido);
                        session.beginTransaction().commit();
                        bloquea(false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false);
                        model.setColumnaEditable(0, false);
                        model.setColumnaEditable(1, false);
                        model.setColumnaEditable(2, false);
                        model.setColumnaEditable(4, false);
                        model.setColumnaEditable(5, false);
                        model.setColumnaEditable(7, false);
                        model.setColumnaEditable(8, false);
                        model.setColumnaEditable(9, false);
                        this.b_compra.setEnabled(true);
                        this.b_pedidos.setEnabled(true);
                        this.b_dcg.setEnabled(true);
                        this.b_edita.setEnabled(true);
                        r_autorizar2.setText(usr.getEmpleado().getNombre());
                        JOptionPane.showMessageDialog(this, "El pedido fue autorizado con exito");
                    /*}
                    else
                    {
                        r_autorizar2.setSelected(false);
                        JOptionPane.showMessageDialog(this, "El mismo usuario no puede autorizar 2 veces");
                    }*/
                }
                else
                {
                    r_autorizar2.setSelected(false);
                    JOptionPane.showMessageDialog(this, "Acceso denegado");
                }
            }catch(Exception e)
            {
                e.printStackTrace();
                r_autorizar2.setSelected(false);
                session.beginTransaction().rollback();
                JOptionPane.showMessageDialog(this, "Error al autorizar el pedido.");
            }
            finally
            {
                if(session.isOpen()==true)
                session.close();
            }
        }
        else//chacar si ya se recibio productos no quitar*******************
        {
            Session session = HibernateUtil.getSessionFactory().openSession();
            try
            {
                session.beginTransaction().begin();
                pedido = (Pedido)session.get(Pedido.class, pedido.getIdPedido());
                usr = (Usuario)session.get(Usuario.class, usr.getIdUsuario());
                Query query2 = session.createSQLQuery("select ( (select if( sum(movimiento.cantidad) is null, 0, sum(movimiento.cantidad)) as can " +
                    "from movimiento inner join almacen on movimiento.id_almacen=almacen.id_almacen where id_pedido="+pedido.getIdPedido()+" and almacen.tipo_movimiento=1 and almacen.operacion in (1, 2, 3)) " +
                    "- " +
                    "(select if( sum(movimiento.cantidad) is null, 0, sum(movimiento.cantidad)) as can " +
                    "from movimiento inner join almacen on movimiento.id_almacen=almacen.id_almacen where id_pedido="+pedido.getIdPedido()+" and almacen.tipo_movimiento=2 and almacen.operacion in (1, 2, 3))) as almacen;");
                query2.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
                ArrayList resp = (ArrayList)query2.list();
                java.util.HashMap map=(java.util.HashMap)resp.get(0);
                if(Double.parseDouble(map.get("almacen").toString())==0.0d)
                {
                    if(usr.getAutorizarPedidos()==true && pedido.getUsuarioByAutorizo2().getIdUsuario().compareTo(usr.getIdUsuario())==0)
                    {
                        pedido.setUsuarioByAutorizo2(null);
                        pedido.setFechaAutorizo2(null);
                        session.update(pedido);
                        session.beginTransaction().commit();
                        r_autorizar2.setText("Autorizacion 2");
                        busca();
                        JOptionPane.showMessageDialog(this, "Se eliminó la autorización del pedido con exito");
                    }
                    else
                    {
                        r_autorizar2.setSelected(true);
                        JOptionPane.showMessageDialog(this, "Acceso denegado");
                    }
                }
                else
                {
                    r_autorizar2.setSelected(true);
                    JOptionPane.showMessageDialog(this, "El pedido ya tiene movimientos en el almacen");
                }
            }catch(Exception e)
            {
                e.printStackTrace();
                r_autorizar2.setSelected(true);
                session.beginTransaction().rollback();
                JOptionPane.showMessageDialog(this, "Error al al quitar la autorizacion del pedido.");
            }
            finally
            {
                if(session.isOpen()==true)
                session.close();
            }
        }
    }//GEN-LAST:event_r_autorizar2ActionPerformed

    private void r_autorizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_r_autorizarActionPerformed
        if(this.r_autorizar.isSelected()==true)
        {
            Session session = HibernateUtil.getSessionFactory().openSession();
            try
            {
                session.beginTransaction().begin();
                pedido = (Pedido)session.get(Pedido.class, Integer.parseInt(t_pedido.getText()));
                usr=(Usuario)session.get(Usuario.class, usr.getIdUsuario());
                if(usr.getAutorizarPedidos()==true)
                {
                    if(pedido.getUsuarioByAutorizo2()==null || pedido.getUsuarioByAutorizo2().getIdUsuario().compareTo(usr.getIdUsuario())!=0)
                    {
                        pedido.setUsuarioByAutorizo(usr);
                        Date fecha_autorizo2 = new Date();
                        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                        String valor=dateFormat.format(fecha_autorizo2);
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
                        pedido.setFechaAutorizo(calendario.getTime());
                        session.update(pedido);
                        session.beginTransaction().commit();
                        bloquea(false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false);
                        model.setColumnaEditable(0, false);
                        model.setColumnaEditable(1, false);
                        model.setColumnaEditable(2, false);
                        model.setColumnaEditable(4, false);
                        model.setColumnaEditable(5, false);
                        model.setColumnaEditable(7, false);
                        model.setColumnaEditable(8, false);
                        model.setColumnaEditable(9, false);
                        this.b_compra.setEnabled(true);
                        this.b_pedidos.setEnabled(true);
                        this.b_dcg.setEnabled(true);
                        this.b_edita.setEnabled(true);
                        r_autorizar.setText(usr.getEmpleado().getNombre());
                        JOptionPane.showMessageDialog(this, "El pedido fue autorizado con exito.");
                    }
                    else
                    {
                        this.r_autorizar.setSelected(false);
                        JOptionPane.showMessageDialog(this, "El mismo usuario no puede autorizar 2 veces");
                    }
                }
                else
                {
                    this.r_autorizar.setSelected(false);
                    JOptionPane.showMessageDialog(this, "Acceso denegado!");
                }
            }catch(Exception e)
            {
                e.printStackTrace();
                r_autorizar.setSelected(false);
                session.beginTransaction().rollback();
                JOptionPane.showMessageDialog(this, "Error al autorizar el pedido.");
            }
            finally
            {
                if(session.isOpen()==true)
                session.close();
            }
        }
        else//chacar si ya se recibio productos no quitar*******************
        {
            Session session = HibernateUtil.getSessionFactory().openSession();
            try
            {
                if(usr.getAutorizarPedidos()==true && pedido.getUsuarioByAutorizo().getIdUsuario().compareTo(usr.getIdUsuario())==0)
                {
                    session.beginTransaction().begin();
                    pedido = (Pedido)session.get(Pedido.class, Integer.parseInt(t_pedido.getText()));
                    usr = (Usuario)session.get(Usuario.class, usr.getIdUsuario());
                    Query query2 = session.createSQLQuery("select ( (select if( sum(movimiento.cantidad) is null, 0, sum(movimiento.cantidad)) as can " +
                    "from movimiento inner join almacen on movimiento.id_almacen=almacen.id_almacen where id_pedido="+pedido.getIdPedido()+" and almacen.tipo_movimiento=1 and almacen.operacion in (1, 2, 3)) " +
                    "- " +
                    "(select if( sum(movimiento.cantidad) is null, 0, sum(movimiento.cantidad)) as can " +
                    "from movimiento inner join almacen on movimiento.id_almacen=almacen.id_almacen where id_pedido="+pedido.getIdPedido()+" and almacen.tipo_movimiento=2 and almacen.operacion in (1, 2, 3))) as almacen;");
                    query2.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
                    ArrayList resp = (ArrayList)query2.list();
                    java.util.HashMap map=(java.util.HashMap)resp.get(0);
                    if(Double.parseDouble(map.get("almacen").toString())==0.0d)
                    {
                        pedido.setUsuarioByAutorizo(null);
                        pedido.setFechaAutorizo(null);
                        session.update(pedido);
                        session.beginTransaction().commit();
                        r_autorizar.setText("Autorizacion 1");
                        busca();
                        JOptionPane.showMessageDialog(this, "Se eliminó la autorización del pedido con exito");
                    }
                    else
                    {
                        this.r_autorizar.setSelected(true);
                        JOptionPane.showMessageDialog(this, "El pedido ya tiene movimientos en el almacen");
                    }
                }
                else
                {
                    this.r_autorizar.setSelected(true);
                    JOptionPane.showMessageDialog(this, "Acceso denegado");
                }
            }catch(Exception e)
            {
                e.printStackTrace();
                this.r_autorizar.setSelected(true);
                session.beginTransaction().rollback();
                JOptionPane.showMessageDialog(this, "Error al al quitar la autorizacion del pedido.");
            }
            finally
            {
                if(session.isOpen()==true)
                session.close();
            }
        }
    }//GEN-LAST:event_r_autorizarActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JDialog autorizarCosto;
    private javax.swing.JDialog autorizarECosto;
    private javax.swing.JButton b_aseguradora;
    private javax.swing.JButton b_autorizar;
    private javax.swing.JButton b_autorizar1;
    private javax.swing.JButton b_busca;
    private javax.swing.JButton b_busca_pedido;
    private javax.swing.JButton b_calendario;
    private javax.swing.JButton b_compra;
    private javax.swing.JButton b_comprador;
    private javax.swing.JButton b_dcg;
    private javax.swing.JButton b_edita;
    private javax.swing.JButton b_guardar;
    private javax.swing.JButton b_marca;
    private javax.swing.JButton b_mas;
    private javax.swing.JButton b_menos;
    private javax.swing.JButton b_orden;
    private javax.swing.JButton b_pedidos;
    private javax.swing.JButton b_proveedor;
    private javax.swing.JButton b_proveedorf;
    private javax.swing.JButton b_recargar;
    private javax.swing.JButton b_tipo;
    private javax.swing.JComboBox c_tipo;
    private javax.swing.JComboBox cb_tipo;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel l_asegurado;
    private javax.swing.JLabel l_busca;
    private javax.swing.JLabel l_colonia;
    private javax.swing.JLabel l_cp;
    private javax.swing.JLabel l_direccion;
    private javax.swing.JLabel l_iva;
    private javax.swing.JLabel l_modelo;
    private javax.swing.JLabel l_nombre;
    private javax.swing.JLabel l_notas;
    private javax.swing.JLabel l_pedido1;
    private javax.swing.JLabel l_poblacion;
    private javax.swing.JLabel l_proveedor;
    private javax.swing.JLabel l_rfc;
    private javax.swing.JLabel l_subtotal;
    private javax.swing.JLabel l_total;
    private javax.swing.JComboBox medida;
    private javax.swing.JComboBox numeros;
    private javax.swing.JPanel p_arriba;
    private javax.swing.JPanel p_interno_centro;
    private javax.swing.JRadioButton r_autorizar;
    private javax.swing.JRadioButton r_autorizar2;
    private javax.swing.JScrollPane scroll;
    private javax.swing.JFormattedTextField t_IVA;
    private javax.swing.JTextField t_asegurado;
    private javax.swing.JTextField t_aseguradora;
    private javax.swing.JTextField t_busca;
    private javax.swing.JTextField t_clave;
    private javax.swing.JTextField t_colonia;
    private javax.swing.JPasswordField t_contra;
    private javax.swing.JPasswordField t_contra1;
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
    // End of variables declaration//GEN-END:variables
   
    public void tabla_tamaños()
    {
        FormatoEditor fe=new FormatoEditor();
        t_datos.setDefaultEditor(Double.class, fe);
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
                    column.setPreferredWidth(10);
                    break;
                case 2:                       
                    column.setPreferredWidth(10);
                    break;
                case 3:               
                    column.setPreferredWidth(100);
                    /*if(c_tipo.getSelectedItem().toString().compareTo("Interno")==0)
                    {
                        DefaultCellEditor miEditor = new DefaultCellEditor(numeros);
                        miEditor.setClickCountToStart(2);
                        column.setCellEditor(miEditor); 
                    }*/
                    break;
                case 4:
                    column.setPreferredWidth(10);
                    break;
                case 5:               
                    column.setPreferredWidth(400);
                    break;
                case 6:               
                    column.setPreferredWidth(10);
                    DefaultCellEditor editor = new DefaultCellEditor(medida);
                    column.setCellEditor(editor); 
                    editor.setClickCountToStart(2);
                    break;
                case 7:               
                    column.setPreferredWidth(10);
                    break;
                case 8:               
                    column.setPreferredWidth(10);
                    break;
                case 9:               
                    column.setPreferredWidth(30);
                    break;
                case 10:               
                    column.setPreferredWidth(30);
                    break;
                default:
                    column.setPreferredWidth(40);
                    break; 
            }
        } 
        JTableHeader header = t_datos.getTableHeader();
        header.setForeground(Color.white);
    }
    
    public void titulos()
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
        
        t_datos.setDefaultRenderer(Double.class, formato); 
        t_datos.setDefaultRenderer(Integer.class, formato);
        t_datos.setDefaultRenderer(String.class, formato);
        t_datos.setDefaultRenderer(Boolean.class, formato);
    }
    
    public void busca()
    {
        this.t_pedido.setText("");
        this.t_proveedor.setText("");
        this.t_nombre_comprador.setText("");
        this.t_plazo.setText("");
        this.t_orden.setText("");
        this.t_fecha.setText("DD/MM/AAAA");
        this.l_proveedor.setText("SELECCIONE UN PROVEEDOR");
        this.t_notas.setText("");
        this.t_nombre.setText("");
        this.t_direccion.setText("");
        this.t_poblacion.setText("");
        this.t_colonia.setText("");
        this.t_cp.setText("");
        this.t_rfc.setText("");
        this.t_tipo.setText("");
        this.t_marca.setText("");
        this.t_modelo.setText("");
        this.t_id_aseguradora.setText("");
        this.t_aseguradora.setText("");
        this.t_siniestro.setText("");
        this.t_asegurado.setText("");
        this.t_folio_externo.setText("");
        model=new MyModel(0, columnas);
        t_datos.setModel(model);
                    
        if(pedido!=null)
        {
            elimina=new ArrayList();
            h= new Herramientas(usr, menu);
            h.session(sessionPrograma);
            h.desbloqueaOrden();
            h.desbloqueaPedido();
            Session session = HibernateUtil.getSessionFactory().openSession();
            try
            {
                session.beginTransaction().begin();
                pedido = (Pedido)session.get(Pedido.class, pedido.getIdPedido());
                String resp="";
                
                //*********cargamos datos de la factura*************************
                this.t_pedido.setText(""+pedido.getIdPedido());
                this.t_proveedor.setText(""+pedido.getProveedorByIdProveedor().getIdProveedor());
                l_proveedor.setText(pedido.getProveedorByIdProveedor().getNombre());
                this.t_plazo.setText("");
                Partida[] part=(Partida[])pedido.getPartidas().toArray(new Partida[0]);
                PartidaExterna[] partEx=(PartidaExterna[])pedido.getPartidaExternas().toArray(new PartidaExterna[0]);
                if(part.length>0)
                    t_orden.setText(""+part[0].getOrdenByIdOrden().getIdOrden());
                if(pedido.getNotas()!=null)
                    this.t_notas.setText(pedido.getNotas());
                else
                    this.t_notas.setText("");
                t_fecha.setText(""+pedido.getFechaPedido());
                if(pedido.getIdExterno()!=null)
                    t_folio_externo.setText(pedido.getIdExterno());
                else
                    t_folio_externo.setText("");
                
                //************cargamos dator de facturacion*********************
                this.t_clave.setText(""+pedido.getProveedorByIdEmpresa().getIdProveedor());
                this.t_nombre.setText(pedido.getProveedorByIdEmpresa().getNombre());
                if(pedido.getProveedorByIdEmpresa()!=null)
                    this.t_direccion.setText(pedido.getProveedorByIdEmpresa().getDireccion());
                else
                    this.t_direccion.setText("");
                if(pedido.getProveedorByIdEmpresa()!=null)
                    this.t_colonia.setText(pedido.getProveedorByIdEmpresa().getColonia());
                else
                    this.t_colonia.setText("");
                if(pedido.getProveedorByIdEmpresa()!=null)
                    this.t_poblacion.setText(pedido.getProveedorByIdEmpresa().getPoblacion());
                else
                    this.t_poblacion.setText("");
                this.t_rfc.setText(pedido.getProveedorByIdEmpresa().getRfc());
                if(pedido.getProveedorByIdEmpresa()!=null)
                    this.t_cp.setText(pedido.getProveedorByIdEmpresa().getCp());
                else
                    this.t_cp.setText("");
                
                //************cargamos datos de la orden***********************
                if(part.length>0)
                {
                    c_tipo.setSelectedItem("Interno");
                    resp=h.estadoOrden(part[0].getOrdenByIdOrden());
                    orden_act=part[0].getOrdenByIdOrden();
                    t_tipo.setText(part[0].getOrdenByIdOrden().getTipo().getTipoNombre());
                    t_marca.setText(part[0].getOrdenByIdOrden().getMarca().getIdMarca());
                    t_modelo.setText(""+part[0].getOrdenByIdOrden().getModelo());
                    t_id_aseguradora.setText(""+part[0].getOrdenByIdOrden().getCompania().getIdCompania());
                    t_aseguradora.setText(part[0].getOrdenByIdOrden().getCompania().getNombre());
                    if(part[0].getOrdenByIdOrden().getSiniestro()!=null)
                        t_siniestro.setText(part[0].getOrdenByIdOrden().getSiniestro());
                    else
                        t_siniestro.setText("");
                    t_asegurado.setText(part[0].getOrdenByIdOrden().getClientes().getNombre());
                    this.t_id_comprador.setText(""+pedido.getEmpleado().getIdEmpleado());
                    this.t_nombre_comprador.setText(pedido.getEmpleado().getNombre());
                }
                else
                {
                    if(partEx.length>0 && pedido.getOrdenExterna()!=null)
                    {
                        c_tipo.setSelectedItem("Externo");
                        if(pedido.getOrdenExterna().getTipo()!=null)
                            t_tipo.setText(pedido.getOrdenExterna().getTipo().getTipoNombre());
                        else
                            t_tipo.setText("");
                        if(pedido.getOrdenExterna().getMarca()!=null)
                            t_marca.setText(pedido.getOrdenExterna().getMarca().getIdMarca());
                        else
                            t_marca.setText("");
                        if(pedido.getOrdenExterna().getModelo()!=null)
                            t_modelo.setText(""+pedido.getOrdenExterna().getModelo());
                        else
                            t_modelo.setText("");
                        if(pedido.getOrdenExterna().getCompania()!=null)
                        {
                            t_id_aseguradora.setText(""+pedido.getOrdenExterna().getCompania().getIdCompania());
                            t_aseguradora.setText(pedido.getOrdenExterna().getCompania().getNombre());
                        }
                        else
                        {
                            t_id_aseguradora.setText("");
                            t_aseguradora.setText("");
                        }

                        if(pedido.getOrdenExterna().getAsegurado()!=null)
                            t_asegurado.setText(pedido.getOrdenExterna().getAsegurado());
                        else
                            t_asegurado.setText("");
                        if(pedido.getOrdenExterna().getSiniestro()!=null)
                            this.t_siniestro.setText(pedido.getOrdenExterna().getSiniestro());
                        else
                            this.t_siniestro.setText("");
                        //agregar el commprador
                        this.t_id_comprador.setText(""+pedido.getEmpleado().getIdEmpleado());
                        this.t_nombre_comprador.setText(pedido.getEmpleado().getNombre());
                    }
                    else
                    {
                        if(pedido.getTipoPedido().compareTo("Adicional")==0)
                        {
                            c_tipo.setSelectedItem("Adicional");
                            orden_act=pedido.getOrden();
                            orden_act=(Orden)session.get(Orden.class, orden_act.getIdOrden());
                            resp=h.estadoOrden(orden_act);
                            t_orden.setText(""+orden_act.getIdOrden());
                            t_tipo.setText(orden_act.getTipo().getTipoNombre());
                            t_marca.setText(orden_act.getMarca().getIdMarca());
                            t_modelo.setText(""+orden_act.getModelo());
                            t_id_aseguradora.setText(""+orden_act.getCompania().getIdCompania());
                            t_aseguradora.setText(orden_act.getCompania().getNombre());
                            if(orden_act.getSiniestro()!=null)
                                t_siniestro.setText(orden_act.getSiniestro());
                            else
                                t_siniestro.setText("");
                            t_asegurado.setText(orden_act.getClientes().getNombre());

                            this.t_id_comprador.setText(""+pedido.getEmpleado().getIdEmpleado());
                            this.t_nombre_comprador.setText(pedido.getEmpleado().getNombre());
                        }
                        else
                        {
                            c_tipo.setSelectedItem("Inventario");
                            this.t_id_comprador.setText(""+pedido.getEmpleado().getIdEmpleado());
                            this.t_nombre_comprador.setText(pedido.getEmpleado().getNombre());
                        }
                    }
                }
                
                //***ordenamos las partidas************
                Partida aux;
                for(int k=0;k<part.length;k++)
                {
                    for(int f=0;f<(part.length-1)-k;f++) 
                    {
                        if (part[f].getIdEvaluacion()>part[f+1].getIdEvaluacion()) 
                        {
                            aux=part[f];
                            part[f]=part[f+1];
                            part[f+1]=aux;
                        }
                        if (part[f].getIdEvaluacion()==part[f+1].getIdEvaluacion() && part[f].getSubPartida()>part[f+1].getSubPartida()) 
                        {
                            aux=part[f];
                            part[f]=part[f+1];
                            part[f+1]=aux;
                        }
                    }
                }
                
                for(int k=0; k<partEx.length; k++)
                {
                    for(int f=0; f<(partEx.length-1)-k; f++) 
                    {
                        if (partEx[f].getIdPartidaExterna() > partEx[f+1].getIdPartidaExterna()) 
                        {
                            PartidaExterna auxEx=partEx[f];
                            partEx[f]=partEx[f+1];
                            partEx[f+1]=auxEx;
                        }
                    }
                }
                //**********
                double tot=0.0d;
                if(c_tipo.getSelectedItem().toString().compareTo("Interno")==0)
                {
                    model=new MyModel(part.length, columnas);
                    t_datos.setModel(model);
                    for(int r=0; r<part.length; r++)
                    {
                        model.setValueAt(part[r].getIdPartida(), r, 0);
                        model.setValueAt(part[r].getIdEvaluacion(), r, 1);
                        model.setValueAt(part[r].getSubPartida(), r, 2);
                        if(part[r].getEjemplar()!=null)
                            model.setValueAt(part[r].getEjemplar().getIdParte(), r, 3);
                        else
                            model.setValueAt("", r, 3);
                        model.setValueAt(part[r].getCatalogo().getIdCatalogo(), r, 4);
                        String anotacion="";
                        if(part[r].getInstruccion()!=null)
                            anotacion=part[r].getInstruccion();
                        model.setValueAt(part[r].getCatalogo().getNombre()+" "+anotacion, r, 5);
                        model.setValueAt(part[r].getMed(), r, 6);
                        if(part[r].getPlazo()!=null)
                        {
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                            model.setValueAt(sdf.format(part[r].getPlazo()), r, 7);
                        }
                        else
                            model.setValueAt("0", r, 7);
                        if(part[r].getCantPcp()!=0.0d)
                            model.setValueAt(part[r].getCantPcp(), r, 8);
                        else
                            model.setValueAt(0, r, 8);
                        if(part[r].getPcp()!=null)
                            model.setValueAt(part[r].getPcp(), r, 9);
                        else
                            model.setValueAt(0, r, 9);
                        double sum=part[r].getCantPcp()*part[r].getPcp();
                        tot+=sum;
                        model.setValueAt(sum, r, 10);
                    }
                }
                if(c_tipo.getSelectedItem().toString().compareTo("Inventario")==0)
                {
                    model=new MyModel(partEx.length, columnas);
                    model.setColumnaEditable(8, true);
                    model.setColumnaEditable(9, true);
                    t_datos.setModel(model);
                    for(int r=0; r<partEx.length; r++)
                    {
                        model.setValueAt(partEx[r].getIdPartidaExterna(), r, 0);
                        model.setValueAt("", r, 1);
                        model.setValueAt("", r, 2);
                        model.setValueAt(partEx[r].getEjemplar().getIdParte(), r, 3);
                        model.setValueAt("", r, 4);
                        model.setValueAt(partEx[r].getDescripcion(), r, 5);
                        model.setValueAt(partEx[r].getUnidad(), r, 6);
                        if(partEx[r].getPlazo()!=null)
                        {
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                            model.setValueAt(sdf.format(partEx[r].getPlazo()), r, 7);
                        }
                        else
                            model.setValueAt("0", r, 7);
                        if(partEx[r].getCantidad()!=null)
                            model.setValueAt(partEx[r].getCantidad(), r, 8);
                        else
                            model.setValueAt(0, r, 8);
                        if(partEx[r].getCosto()!=null)
                            model.setValueAt(partEx[r].getCosto(), r, 9);
                        else
                            model.setValueAt(0, r, 9);
                        double sum=partEx[r].getCantidad()*partEx[r].getCosto();
                        tot+=sum;
                        model.setValueAt(sum, r, 10);
                    }
                }
                if(c_tipo.getSelectedItem().toString().compareTo("Externo")==0 || c_tipo.getSelectedItem().toString().compareTo("Adicional")==0)
                {
                    model=new MyModel(partEx.length, columnas);
                    model.setColumnaEditable(3, true);
                    model.setColumnaEditable(5, true);
                    model.setColumnaEditable(6, true);
                    t_datos.setModel(model);
                    for(int r=0; r<partEx.length; r++)
                    {
                        model.setValueAt(partEx[r].getIdPartidaExterna(), r, 0);
                        model.setValueAt(partEx[r].getPartida(), r, 1);
                        model.setValueAt(partEx[r].getIdValuacion(), r, 2);
                        if(partEx[r].getNoParte()!=null)
                            model.setValueAt(partEx[r].getNoParte(), r, 3);
                        else
                            model.setValueAt("", r, 3);
                        model.setValueAt("", r, 4);
                        model.setValueAt(partEx[r].getDescripcion(), r, 5);
                        model.setValueAt(partEx[r].getUnidad(), r, 6);
                        if(partEx[r].getPlazo()!=null)
                        {
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                            model.setValueAt(sdf.format(partEx[r].getPlazo()), r, 7);
                        }
                        else
                            model.setValueAt("0", r, 7);
                        if(partEx[r].getCantidad()!=null)
                            model.setValueAt(partEx[r].getCantidad(), r, 8);
                        else
                            model.setValueAt(0, r, 8);
                        if(partEx[r].getCosto()!=null)
                            model.setValueAt(partEx[r].getCosto(), r, 9);
                        else
                            model.setValueAt(0, r, 9);
                        double sum=partEx[r].getCantidad()*partEx[r].getCosto();
                        tot+=sum;
                        model.setValueAt(sum, r, 10);
                    }
                }
                
                t_subtotal.setValue(tot);
                double iva=tot*.16;
                t_IVA.setValue(iva);
                t_total.setValue(tot+iva);
                
                //checar si la orden ya fue autorizada
                 r_autorizar.setEnabled(true);
                r_autorizar2.setEnabled(true);
                if(/*pedido.getUsuarioByAutorizo()!=null ||*/ pedido.getUsuarioByAutorizo2()!=null)
                {
                    /*if(pedido.getUsuarioByAutorizo()!=null)
                    {
                        r_autorizar.setSelected(true);
                        r_autorizar.setText(pedido.getUsuarioByAutorizo().getEmpleado().getNombre());
                    }
                    else
                    {
                        r_autorizar.setSelected(false);
                        r_autorizar.setText("Autorizacion 1");
                    }
                    if(pedido.getUsuarioByAutorizo2()!=null)
                    {*/
                        r_autorizar2.setSelected(true);
                        r_autorizar2.setText(pedido.getUsuarioByAutorizo2().getEmpleado().getNombre());
                    /*}
                    else
                    {
                        r_autorizar2.setSelected(false);
                        r_autorizar2.setText("Autorizacion 2");
                    }*/
                    bloquea(false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false);
                    model.setColumnaEditable(0, false);
                    model.setColumnaEditable(1, false);
                    model.setColumnaEditable(2, false);
                    model.setColumnaEditable(3, false);
                    model.setColumnaEditable(4, false);
                    model.setColumnaEditable(5, false);
                    model.setColumnaEditable(6, false);
                    model.setColumnaEditable(7, false);
                    model.setColumnaEditable(8, false);
                    model.setColumnaEditable(9, false);
                    b_pedidos.setEnabled(true);
                    b_compra.setEnabled(true);
                    b_edita.setEnabled(true);
                    b_dcg.setEnabled(true);
                    this.b_guardar.setEnabled(true);
                    //this.t_folio_externo.setEnabled(true);
                }
                else
                {
                    //r_autorizar.setSelected(false);
                    r_autorizar2.setSelected(false);
                    //r_autorizar.setText("Autorizacion 1");
                    r_autorizar2.setText("Autorizacion 2");
                    if(c_tipo.getSelectedItem().toString().compareTo("Interno")==0)
                    {
                        bloquea(true, true, true, true, false, false, false, false, false, false, false, false, true, true, true, true, false, false, false, false);
                        model.setColumnaEditable(0, false);
                        model.setColumnaEditable(1, false);
                        model.setColumnaEditable(2, false);
                        model.setColumnaEditable(3, true);
                        model.setColumnaEditable(4, false);
                        model.setColumnaEditable(5, false);
                        model.setColumnaEditable(6, false);
                        model.setColumnaEditable(7, false);
                        model.setColumnaEditable(8, true);
                        model.setColumnaEditable(9, true);
                        model.setColumnaEditable(10, false);
                    }
                    if(c_tipo.getSelectedItem().toString().compareTo("Adicional")==0)
                    {
                        bloquea(true, true, true, true, false, false, false, false, false, false, false, false, true, true, true, true, false, false, false, false);
                        model.setColumnaEditable(0, false);
                        model.setColumnaEditable(1, false);
                        model.setColumnaEditable(2, false);
                        model.setColumnaEditable(3, true);
                        model.setColumnaEditable(4, false);
                        model.setColumnaEditable(5, true);
                        model.setColumnaEditable(6, true);
                        model.setColumnaEditable(7, false);
                        model.setColumnaEditable(8, true);
                        model.setColumnaEditable(9, true);
                        model.setColumnaEditable(10, false);
                    }
                    if(c_tipo.getSelectedItem().toString().compareTo("Externo")==0)
                    {
                        bloquea(true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, false, false, true, false);
                        model.setColumnaEditable(0, false);
                        model.setColumnaEditable(1, true);
                        model.setColumnaEditable(2, true);
                        model.setColumnaEditable(3, true);
                        model.setColumnaEditable(4, false);
                        model.setColumnaEditable(5, true);
                        model.setColumnaEditable(6, true);
                        model.setColumnaEditable(7, false);
                        model.setColumnaEditable(8, true);
                        model.setColumnaEditable(9, true);
                        model.setColumnaEditable(10, false);
                    }
                    if(c_tipo.getSelectedItem().toString().compareTo("Inventario")==0)
                    {
                        bloquea(true, true, true, true, false, false, false, false, false, false, false, true, true, true, true, true, false, false, true, false);
                        model.setColumnaEditable(0, false);
                        model.setColumnaEditable(1, false);
                        model.setColumnaEditable(2, false);
                        model.setColumnaEditable(3, false);
                        model.setColumnaEditable(4, false);
                        model.setColumnaEditable(5, false);
                        model.setColumnaEditable(6, false);
                        model.setColumnaEditable(7, false);
                        model.setColumnaEditable(8, true);
                        model.setColumnaEditable(9, true);
                        model.setColumnaEditable(10, false);
                    }
                    b_pedidos.setEnabled(true);
                    b_compra.setEnabled(true);
                    b_edita.setEnabled(false);
                    b_dcg.setEnabled(true);
                }
                
                if(c_tipo.getSelectedItem().toString().compareTo("Interno")==0 || c_tipo.getSelectedItem().toString().compareTo("Adicional")==0)
                {
                    if(resp.compareTo("")==0 || resp.compareTo("*bloqueada ok*")==0)
                    {
                        resp=h.estadoPedido(pedido);
                        if(resp.compareTo("")==0 || resp.compareTo("*bloqueada ok*")==0)
                        {
                            if(usr.getAutorizarPedidos()==true)
                            {
                                r_autorizar.setEnabled(true);
                                r_autorizar2.setEnabled(true);
                            }
                            else
                            {
                                r_autorizar.setEnabled(false);
                                r_autorizar2.setEnabled(false);
                            }
                        }
                        else
                        {
                            r_autorizar.setEnabled(false);
                            r_autorizar2.setEnabled(false);
                            bloquea(false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false);
                            model.setColumnaEditable(0, false);
                            model.setColumnaEditable(1, false);
                            model.setColumnaEditable(2, false);
                            model.setColumnaEditable(3, false);
                            model.setColumnaEditable(4, false);
                            model.setColumnaEditable(5, false);
                            model.setColumnaEditable(6, false);
                            model.setColumnaEditable(7, false);
                            model.setColumnaEditable(8, false);
                            model.setColumnaEditable(9, false);
                            model.setColumnaEditable(10, false);
                            if(menu!=90)
                            JOptionPane.showMessageDialog(null, "¡Pedido bloqueada por:"+pedido.getUsuarioByBloqueado().getIdUsuario());
                        }
                    }
                    else
                    {
                        r_autorizar.setEnabled(false);
                        r_autorizar2.setEnabled(false);
                        bloquea(false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false);
                        model.setColumnaEditable(0, false);
                        model.setColumnaEditable(1, false);
                        model.setColumnaEditable(2, false);
                        model.setColumnaEditable(3, false);
                        model.setColumnaEditable(4, false);
                        model.setColumnaEditable(5, false);
                        model.setColumnaEditable(6, false);
                        model.setColumnaEditable(7, false);
                        model.setColumnaEditable(8, false);
                        model.setColumnaEditable(9, false);
                        model.setColumnaEditable(10, false);
                        b_pedidos.setEnabled(true);
                        b_compra.setEnabled(true);
                        b_edita.setEnabled(false);
                        b_dcg.setEnabled(true);
                        if(menu!=90)
                        JOptionPane.showMessageDialog(null, "¡Orden bloqueada por:"+orden_act.getUsuarioByBloqueada().getIdUsuario());
                    }
                    if(orden_act!=null && orden_act.getFechaCierre()!=null)
                    {
                        r_autorizar.setEnabled(false);
                        r_autorizar2.setEnabled(false);
                        bloquea(false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false);
                        model.setColumnaEditable(0, false);
                        model.setColumnaEditable(1, false);
                        model.setColumnaEditable(2, false);
                        model.setColumnaEditable(3, false);
                        model.setColumnaEditable(4, false);
                        model.setColumnaEditable(5, false);
                        model.setColumnaEditable(6, false);
                        model.setColumnaEditable(7, false);
                        model.setColumnaEditable(8, false);
                        model.setColumnaEditable(9, false);
                        model.setColumnaEditable(10, false);
                        if(menu!=90)
                        JOptionPane.showMessageDialog(null, "Orden cerrada");
                    }
                }
                else
                {
                    resp=h.estadoPedido(pedido);
                    if(resp.compareTo("")==0 || resp.compareTo("*bloqueada ok*")==0)
                    {
                        if(usr.getAutorizarPedidos()==true)
                        {
                            r_autorizar.setEnabled(true);
                            r_autorizar2.setEnabled(true);
                        }
                        else
                        {
                            r_autorizar.setEnabled(false);
                            r_autorizar2.setEnabled(false);
                        }
                    }
                    else
                    {
                        r_autorizar.setEnabled(false);
                        r_autorizar2.setEnabled(false);
                        bloquea(false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false);
                        model.setColumnaEditable(0, false);
                        model.setColumnaEditable(1, false);
                        model.setColumnaEditable(2, false);
                        model.setColumnaEditable(3, false);
                        model.setColumnaEditable(4, false);
                        model.setColumnaEditable(5, false);
                        model.setColumnaEditable(6, false);
                        model.setColumnaEditable(7, false);
                        model.setColumnaEditable(8, false);
                        model.setColumnaEditable(9, false);
                        model.setColumnaEditable(10, false);
                        if(menu!=90)
                        JOptionPane.showMessageDialog(null, "¡Pedido bloqueado por:"+pedido.getUsuarioByBloqueado().getIdUsuario());
                    }
                }
                session.beginTransaction().commit();
            }catch(Exception e)
            {
                e.printStackTrace();
                this.t_pedido.setText("");
                this.t_proveedor.setText("");
                this.t_plazo.setText("");
                this.t_orden.setText("");
                this.t_fecha.setText("DD/MM/AAAA");
                this.l_proveedor.setText("SELECCIONE UN PROVEEDOR");
                this.t_notas.setText("");
                this.t_nombre.setText("");
                this.t_direccion.setText("");
                this.t_poblacion.setText("");
                this.t_colonia.setText("");
                this.t_cp.setText("");
                this.t_rfc.setText("");
                this.t_tipo.setText("");
                this.t_marca.setText("");
                this.t_modelo.setText("");
                this.t_id_aseguradora.setText("");
                this.t_aseguradora.setText("");
                this.t_siniestro.setText("");
                this.t_asegurado.setText("");
                this.t_folio_externo.setText("");
                this.r_autorizar.setEnabled(false);
                this.r_autorizar2.setEnabled(false);
                bloquea(false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false);
                model.setColumnaEditable(0, false);
                model.setColumnaEditable(1, false);
                model.setColumnaEditable(2, false);
                model.setColumnaEditable(3, false);
                model.setColumnaEditable(4, false);
                model.setColumnaEditable(5, false);
                model.setColumnaEditable(6, false);
                model.setColumnaEditable(7, false);
                model.setColumnaEditable(8, false);
                model.setColumnaEditable(9, false);
                model.setColumnaEditable(10, false);
                b_pedidos.setEnabled(true);
                b_compra.setEnabled(true);
                b_edita.setEnabled(false);
                b_dcg.setEnabled(true);
                session.beginTransaction().rollback();
            }
            finally
            {
                if(session.isOpen()==true)
                    session.close();
            }
        }
        else
        {
            r_autorizar.setEnabled(false);
            r_autorizar2.setEnabled(false);
            bloquea(false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false);
        }
        titulos();
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
            if(session.isOpen())
                session.close(); 
        }
    }
    
    void bloquea(boolean proveedor, boolean cal, boolean notas, boolean facturar, boolean tipo, boolean marca, boolean modelo, boolean serie, boolean aseg, boolean siniestro, boolean asegurado, boolean externo, boolean mas, boolean menos, boolean guarda, boolean recarga, boolean pedidos, boolean compra, boolean comprador, boolean edita)
    {
        //t_folio_externo.setEditable(proveedor);
        b_proveedor.setEnabled(proveedor);
        b_calendario.setEnabled(cal);
        t_notas.setEditable(notas);
        b_proveedorf.setEnabled(facturar);
        b_tipo.setEnabled(tipo);
        b_marca.setEnabled(marca);
        t_modelo.setEditable(modelo);
        b_aseguradora.setEnabled(aseg);
        t_siniestro.setEditable(siniestro);
        t_asegurado.setEditable(aseg);
        b_mas.setEnabled(mas);
        b_menos.setEnabled(menos);
        b_guardar.setEnabled(guarda);
        b_recargar.setEnabled(recarga);
        b_pedidos.setEnabled(pedidos);
        b_compra.setEnabled(compra);
        b_comprador.setEnabled(comprador);
        b_edita.setEnabled(edita);
        b_dcg.setEnabled(pedidos);
    }
    
    private void sumaTotales()
    {
        Integer IdCon =null;
        double subtotal=0.0;
        double iva=0.0;
        for(int ren=0; ren<t_datos.getRowCount(); ren++)
        {
            double multi= Double.parseDouble(String.valueOf(t_datos.getValueAt(ren,8)))*Double.parseDouble(String.valueOf(t_datos.getValueAt(ren,9)));
            t_datos.setValueAt(multi,ren,10);
            subtotal+=multi;
        }
        t_subtotal.setValue(subtotal);
        Session session = HibernateUtil.getSessionFactory().openSession();
        Configuracion con = (Configuracion)session.get(Configuracion.class, 1);
        t_IVA.setValue(iva=subtotal*con.getIva()/100);
        t_total.setValue(subtotal+iva);
        if(session.isOpen())
            session.close();
    }
    
    private int buscaTabla(int partida)
    {
        int x=-1;
        for(int ren=0; ren<t_datos.getRowCount(); ren++)
            if(Integer.parseInt(t_datos.getValueAt(ren, 0).toString())==partida)
                x=ren;
        return x;
    }
    
    private boolean actualizaPedido()
    {
        if(t_clave.getText().compareTo("")!=0)
        {
            if(t_proveedor.getText().compareTo("")!=0)
            {
                Session session = HibernateUtil.getSessionFactory().openSession();
                try
                {
                    session.beginTransaction().begin();
                    pedido = (Pedido)session.get(Pedido.class, Integer.parseInt(t_pedido.getText()));
                    if(pedido!=null)
                    {
                        if(c_tipo.getSelectedItem().toString().compareTo("Interno")==0)
                        {
                            Partida[] partidas=(Partida[])pedido.getPartidas().toArray(new Partida[0]);
                            if(partidas.length>0 && model.isCellEditable(0, 9)==true)
                            {
                                for(int r=0; r<partidas.length; r++)
                                {
                                    partidas[r].setPedido(null);
                                    session.update(partidas[r]);
                                }
                                pedido.setPartidas(new HashSet(0));
                                session.update(pedido);

                                for(int r=0; r<t_datos.getRowCount(); r++)
                                {
                                    Partida part=(Partida) session.get(Partida.class, Integer.parseInt(t_datos.getValueAt(r, 0).toString()));
                                    if(part!=null)
                                    {
                                        part.setCantPcp(Double.parseDouble(t_datos.getValueAt(r, 8).toString()));
                                        if((Double)t_datos.getValueAt(r, 9)>part.getPrecioAutCU())
                                        {
                                            if(part.getUsuario()==null &&  usrAut!=null)
                                            {
                                                usrAut=(Usuario)session.get(Usuario.class, usrAut.getIdUsuario());
                                                part.setUsuario(usrAut);
                                            }
                                        }
                                        part.setPcp((Double)t_datos.getValueAt(r, 9));
                                        part.setPedido(pedido);
                                        if(t_datos.getValueAt(r, 7).toString().compareTo("0")==0 || t_datos.getValueAt(r, 7)==null)
                                                part.setPlazo(null);
                                        else
                                        {
                                            String [] fecha =t_datos.getValueAt(r, 7).toString().split("-");
                                            Calendar calendario = Calendar.getInstance();
                                            calendario.set(
                                                    Integer.parseInt(fecha[0]), 
                                                    Integer.parseInt(fecha[1])-1, 
                                                    Integer.parseInt(fecha[2]));
                                            part.setPlazo(calendario.getTime());
                                        }
                                        pedido.agregaPartida(part);
                                        session.update(part);
                                    }
                                    else
                                    {
                                        session.beginTransaction().rollback();
                                        JOptionPane.showMessageDialog(null, "¡Error al actualizar los datos!");
                                        return false;
                                    }
                                }
                            }
                            prov_act=(Proveedor)session.get(Proveedor.class, Integer.parseInt(t_proveedor.getText()));
                            provf_act=(Proveedor)session.get(Proveedor.class, Integer.parseInt(t_clave.getText()));
                            pedido.setProveedorByIdProveedor(prov_act);
                            pedido.setProveedorByIdEmpresa(provf_act);
                            pedido.setNotas(t_notas.getText());
                            pedido.setIdExterno(this.t_folio_externo.getText());
                            session.update(pedido);
                            session.beginTransaction().commit();
                        }
                        if(c_tipo.getSelectedItem().toString().compareTo("Externo")==0)
                        {
                            if(t_id_comprador.getText().compareTo("")!=0)
                            {
                                Empleado emp=(Empleado)session.get(Empleado.class, Integer.parseInt(this.t_id_comprador.getText()));
                                if(emp!=null)
                                {
                                    for(int ren=0; ren<t_datos.getRowCount(); ren++)//*** eliminamos los renglones sin informacion
                                    {
                                        if(t_datos.getValueAt(ren, 5).toString().compareTo("")==0)
                                        {
                                            DefaultTableModel model = (DefaultTableModel) t_datos.getModel();
                                            model.removeRow(ren);
                                            ren--;
                                        }
                                    }
                                    if(t_datos.getRowCount()>0)
                                    {
                                        pedido=(Pedido)session.get(Pedido.class, pedido.getIdPedido());

                                        provf_act=(Proveedor)session.get(Proveedor.class, Integer.parseInt(t_clave.getText()));
                                        pedido.setProveedorByIdEmpresa(provf_act);

                                        prov_act=(Proveedor)session.get(Proveedor.class, Integer.parseInt(t_proveedor.getText()));
                                        pedido.setProveedorByIdProveedor(prov_act);

                                        pedido.setUsuarioByIdUsuario(usr);
                                        pedido.setIdExterno(this.t_folio_externo.getText());
                                        
                                        if(t_notas.getText().compareTo("")!=0)
                                            pedido.setNotas(t_notas.getText());
                                        else
                                            pedido.setNotas("");

                                        //********cargamos los datos de la unidad
                                        OrdenExterna ordenx=pedido.getOrdenExterna();
                                        if(this.t_tipo.getText().compareTo("")!=0)//agregamos el tipo
                                        {
                                            Tipo tip=(Tipo)session.get(Tipo.class, t_tipo.getText());
                                            if(tip!=null)
                                                ordenx.setTipo(tip);
                                        }
                                        else
                                            ordenx.setTipo(null);

                                        if(this.t_marca.getText().compareTo("")!=0)//agregamos la marca
                                        {
                                            Marca mar=(Marca)session.get(Marca.class, t_marca.getText());
                                            if(mar!=null)
                                                ordenx.setMarca(mar);
                                        }
                                        else
                                            ordenx.setMarca(null);

                                        if(this.t_modelo.getText().compareTo("")!=0)//agregamos el modelo
                                            ordenx.setModelo(Integer.parseInt(this.t_modelo.getText()));
                                        else
                                            ordenx.setModelo(null);

                                        if(this.t_id_aseguradora.getText().compareTo("")!=0)//agregamos la aseguradora
                                        {
                                            Compania com=(Compania) session.get(Compania.class, Integer.parseInt(this.t_id_aseguradora.getText()));
                                            if(com!=null)
                                                ordenx.setCompania(com);
                                        }
                                        else
                                            ordenx.setCompania(null);

                                        ordenx.setSiniestro(t_siniestro.getText());
                                        ordenx.setAsegurado(this.t_asegurado.getText());

                                        session.update(ordenx);
                                        //********************************
                                        for(int ren=0; ren<t_datos.getRowCount(); ren++)
                                        {
                                            if(t_datos.getValueAt(ren, 0).toString().compareTo("")==0)//incertar renglon
                                            {
                                                PartidaExterna npx=new PartidaExterna();
                                                npx.setPartida(t_datos.getValueAt(ren, 1).toString());
                                                npx.setIdValuacion(t_datos.getValueAt(ren, 2).toString());
                                                npx.setNoParte(t_datos.getValueAt(ren, 3).toString());
                                                npx.setDescripcion(t_datos.getValueAt(ren, 5).toString());
                                                npx.setFacturado(false);
                                                npx.setUnidad(t_datos.getValueAt(ren, 6).toString());
                                                if(t_datos.getValueAt(ren, 7).toString().compareTo("0")!=0)
                                                {
                                                    String[] fecha1 = t_datos.getValueAt(ren, 7).toString().split("-");
                                                    Calendar calendario1 = Calendar.getInstance();
                                                    calendario1.set(
                                                            Integer.parseInt(fecha1[0]), 
                                                            Integer.parseInt(fecha1[1])-1, 
                                                            Integer.parseInt(fecha1[2]));
                                                    npx.setPlazo(calendario1.getTime());
                                                }
                                                npx.setCantidad((double) t_datos.getValueAt(ren, 8));
                                                npx.setCosto((double) t_datos.getValueAt(ren, 9));
                                                npx.setPedido(pedido);
                                                npx.setD(0.0);
                                                npx.setR(0.0);
                                                npx.setM(0.0);
                                                pedido.agregaPartidaExterna(npx);
                                            }
                                            else
                                            {
                                                PartidaExterna px=(PartidaExterna)session.get(PartidaExterna.class, Integer.parseInt(t_datos.getValueAt(ren, 0).toString()));
                                                px.setPartida(t_datos.getValueAt(ren, 1).toString());
                                                px.setIdValuacion(t_datos.getValueAt(ren, 2).toString());
                                                px.setNoParte(t_datos.getValueAt(ren, 3).toString());
                                                px.setDescripcion(t_datos.getValueAt(ren, 5).toString());
                                                px.setUnidad(t_datos.getValueAt(ren, 6).toString());
                                                if(t_datos.getValueAt(ren, 7).toString().compareTo("0")!=0)
                                                {
                                                    String[] fecha1 = t_datos.getValueAt(ren, 7).toString().split("-");
                                                    Calendar calendario1 = Calendar.getInstance();
                                                    calendario1.set(
                                                            Integer.parseInt(fecha1[0]), 
                                                            Integer.parseInt(fecha1[1])-1, 
                                                            Integer.parseInt(fecha1[2]));
                                                    px.setPlazo(calendario1.getTime());
                                                }
                                                px.setCantidad((double) t_datos.getValueAt(ren, 8));
                                                px.setCosto((double) t_datos.getValueAt(ren, 9));
                                                session.update(px);
                                            }
                                        }
                                        for(int d=0; d<elimina.size(); d++)
                                        {
                                            PartidaExterna pe=(PartidaExterna)session.get(PartidaExterna.class, (int)elimina.get(d));
                                            pedido.getPartidaExternas().remove(pe);
                                            session.delete(pe);
                                        }
                                        pedido.setIdExterno(this.t_folio_externo.getText());
                                        pedido.setEmpleado(emp);
                                        session.update(pedido);
                                        session.beginTransaction().commit();
                                        t_fecha.setText(pedido.getFechaPedido().toLocaleString());
                                    }
                                    else
                                    {
                                        session.beginTransaction().rollback();
                                        JOptionPane.showMessageDialog(null, "Selecciona algunos pedidos");
                                        return false;
                                    }
                                }
                                else
                                {
                                    session.beginTransaction().rollback();
                                    JOptionPane.showMessageDialog(null, "El empleado ya no existe"); 
                                    return false;
                                }
                            }
                            else
                            {
                                session.beginTransaction().rollback();
                                JOptionPane.showMessageDialog(null, "Selecciona el nombre del comprador");
                                return false;
                            }
                        }
                        if(c_tipo.getSelectedItem().toString().compareTo("Adicional")==0)
                        {
                                if(t_id_comprador.getText().compareTo("")!=0)
                                {
                                    Empleado emp=(Empleado)session.get(Empleado.class, Integer.parseInt(this.t_id_comprador.getText()));
                                    if(emp!=null)
                                    {
                                        for(int ren=0; ren<t_datos.getRowCount(); ren++)//*** eliminamos los renglones sin informacion
                                        {
                                            if(t_datos.getValueAt(ren, 5).toString().compareTo("")==0)
                                            {
                                                DefaultTableModel model = (DefaultTableModel) t_datos.getModel();
                                                model.removeRow(ren);
                                                ren--;
                                            }
                                        }
                                        if(t_datos.getRowCount()>0)
                                        {
                                            pedido=(Pedido)session.get(Pedido.class, pedido.getIdPedido());

                                            provf_act=(Proveedor)session.get(Proveedor.class, Integer.parseInt(t_clave.getText()));
                                            pedido.setProveedorByIdEmpresa(provf_act);

                                            prov_act=(Proveedor)session.get(Proveedor.class, Integer.parseInt(t_proveedor.getText()));
                                            pedido.setProveedorByIdProveedor(prov_act);

                                            pedido.setUsuarioByIdUsuario(usr);
                                            pedido.setIdExterno(this.t_folio_externo.getText());

                                            if(t_notas.getText().compareTo("")!=0)
                                                pedido.setNotas(t_notas.getText());
                                            else
                                                pedido.setNotas("");

                                            //********************************
                                            for(int ren=0; ren<t_datos.getRowCount(); ren++)
                                            {
                                                if(t_datos.getValueAt(ren, 0).toString().compareTo("")==0)//incertar renglon
                                                {
                                                    PartidaExterna npx=new PartidaExterna();
                                                    npx.setNoParte(t_datos.getValueAt(ren, 3).toString());
                                                    npx.setDescripcion(t_datos.getValueAt(ren, 5).toString());
                                                    npx.setFacturado(false);
                                                    npx.setUnidad(t_datos.getValueAt(ren, 6).toString());
                                                    if(t_datos.getValueAt(ren, 7).toString().compareTo("0")!=0)
                                                    {
                                                        String[] fecha1 = t_datos.getValueAt(ren, 7).toString().split("-");
                                                        Calendar calendario1 = Calendar.getInstance();
                                                        calendario1.set(
                                                                Integer.parseInt(fecha1[0]), 
                                                                Integer.parseInt(fecha1[1])-1, 
                                                                Integer.parseInt(fecha1[2]));
                                                        npx.setPlazo(calendario1.getTime());
                                                    }
                                                    npx.setCantidad((double) t_datos.getValueAt(ren, 8));
                                                    npx.setCosto((double) t_datos.getValueAt(ren, 9));
                                                    npx.setPedido(pedido);
                                                    npx.setD(0.0);
                                                    npx.setR(0.0);
                                                    npx.setM(0.0);
                                                    pedido.agregaPartidaExterna(npx);
                                                }
                                                else
                                                {
                                                    PartidaExterna px=(PartidaExterna)session.get(PartidaExterna.class, Integer.parseInt(t_datos.getValueAt(ren, 0).toString()));
                                                    px.setNoParte(t_datos.getValueAt(ren, 3).toString());
                                                    px.setDescripcion(t_datos.getValueAt(ren, 5).toString());
                                                    px.setUnidad(t_datos.getValueAt(ren, 6).toString());
                                                    if(t_datos.getValueAt(ren, 7).toString().compareTo("0")!=0)
                                                    {
                                                        String[] fecha1 = t_datos.getValueAt(ren, 7).toString().split("-");
                                                        Calendar calendario1 = Calendar.getInstance();
                                                        calendario1.set(
                                                                Integer.parseInt(fecha1[0]), 
                                                                Integer.parseInt(fecha1[1])-1, 
                                                                Integer.parseInt(fecha1[2]));
                                                        px.setPlazo(calendario1.getTime());
                                                    }
                                                    px.setCantidad((double) t_datos.getValueAt(ren, 8));
                                                    px.setCosto((double) t_datos.getValueAt(ren, 9));
                                                    session.update(px);
                                                }
                                            }
                                            for(int d=0; d<elimina.size(); d++)
                                            {
                                                PartidaExterna pe=(PartidaExterna)session.get(PartidaExterna.class, (int)elimina.get(d));
                                                pedido.getPartidaExternas().remove(pe);
                                                session.delete(pe);
                                            }
                                            pedido.setEmpleado(emp);
                                            pedido.setOrden(orden_act);
                                            session.update(pedido);
                                            session.beginTransaction().commit();
                                            t_fecha.setText(pedido.getFechaPedido().toLocaleString());
                                        }
                                        else
                                        {
                                            session.beginTransaction().rollback();
                                            JOptionPane.showMessageDialog(null, "Selecciona algunos pedidos");
                                            return false;
                                        }
                                    }
                                    else
                                    {
                                        session.beginTransaction().rollback();
                                        JOptionPane.showMessageDialog(null, "El empleado ya no existe"); 
                                        return false;
                                    }
                                }
                                else
                                {
                                    session.beginTransaction().rollback();
                                    JOptionPane.showMessageDialog(null, "Selecciona el nombre del comprador");
                                    return false;
                                }
                        }
                        if(this.c_tipo.getSelectedItem().toString().compareTo("Inventario")==0)
                        {
                            if(t_datos.getRowCount()>0)
                            {
                                pedido=(Pedido)session.get(Pedido.class, pedido.getIdPedido());

                                provf_act=(Proveedor)session.get(Proveedor.class, Integer.parseInt(t_clave.getText()));
                                pedido.setProveedorByIdEmpresa(provf_act);

                                prov_act=(Proveedor)session.get(Proveedor.class, Integer.parseInt(t_proveedor.getText()));
                                pedido.setProveedorByIdProveedor(prov_act);

                                pedido.setUsuarioByIdUsuario(usr);
                                pedido.setIdExterno(this.t_folio_externo.getText());

                                if(t_notas.getText().compareTo("")!=0)
                                    pedido.setNotas(t_notas.getText());
                                else
                                    pedido.setNotas("");
                                for(int ren=0; ren<t_datos.getRowCount(); ren++)
                                {
                                    if(t_datos.getValueAt(ren, 0).toString().compareTo("")==0)//incertar renglon
                                    {
                                        PartidaExterna px=new PartidaExterna();
                                        //px.setPartida(t_datos.getValueAt(ren, 0).toString());
                                        //px.setIdValuacion(t_datos.getValueAt(ren, 1).toString());
                                        Ejemplar ej=(Ejemplar)session.get(Ejemplar.class, t_datos.getValueAt(ren, 3).toString());
                                        px.setEjemplar(ej);
                                        px.setDescripcion(t_datos.getValueAt(ren, 5).toString());
                                        px.setFacturado(false);
                                        px.setUnidad(t_datos.getValueAt(ren, 6).toString());
                                        px.setOp(false);
                                        System.out.println("ok "+t_datos.getValueAt(ren, 7).toString());
                                        if(t_datos.getValueAt(ren, 7).toString().compareTo("")!=0 || t_datos.getValueAt(ren, 6).toString().compareTo("0")!=0)
                                        {
                                            String[] fecha = t_datos.getValueAt(ren, 7).toString().split("-");
                                            Calendar calendario = Calendar.getInstance();
                                            calendario.set(
                                                    Integer.parseInt(fecha[0]), 
                                                    Integer.parseInt(fecha[1])-1, 
                                                    Integer.parseInt(fecha[2]));
                                            px.setPlazo(calendario.getTime());
                                        }
                                        px.setCantidad((double) t_datos.getValueAt(ren, 8));
                                        px.setCosto((double) t_datos.getValueAt(ren, 9));
                                        px.setPedido(pedido);
                                        px.setOriCon("-");
                                        px.setD(0.0);
                                        px.setR(0.0);
                                        px.setM(0.0);
                                        pedido.agregaPartidaExterna(px);
                                    }
                                    else
                                    {
                                        PartidaExterna px=(PartidaExterna)session.get(PartidaExterna.class, Integer.parseInt(t_datos.getValueAt(ren, 0).toString()));
                                        if(t_datos.getValueAt(ren, 7).toString().compareTo("0")!=0)
                                        {
                                            String[] fecha1 = t_datos.getValueAt(ren, 7).toString().split("-");
                                            Calendar calendario1 = Calendar.getInstance();
                                            calendario1.set(
                                                    Integer.parseInt(fecha1[0]), 
                                                    Integer.parseInt(fecha1[1])-1, 
                                                    Integer.parseInt(fecha1[2]));
                                            px.setPlazo(calendario1.getTime());
                                        }
                                        px.setCantidad((double) t_datos.getValueAt(ren, 8));
                                        px.setCosto((double) t_datos.getValueAt(ren, 9));
                                        session.update(px);
                                    }
                                }
                                for(int d=0; d<elimina.size(); d++)
                                {
                                    PartidaExterna pe=(PartidaExterna)session.get(PartidaExterna.class, (int)elimina.get(d));
                                    pedido.getPartidaExternas().remove(pe);
                                    session.delete(pe);
                                }
                                session.update(pedido);
                                session.beginTransaction().commit();
                                t_fecha.setText(pedido.getFechaPedido().toLocaleString());
                            }
                        }
                    }
                    else
                    {
                        JOptionPane.showMessageDialog(null, "¡El pedido ya no existe!");
                        return false;
                    }
                }
                catch(Exception e)
                {
                    session.beginTransaction().rollback();
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(null, "¡Error al actualizar los datos!");
                    return false;
                }
                finally
                {
                    if(session.isOpen())
                        session.close();
                }
                return true;
            }
            else
            {
                JOptionPane.showMessageDialog(null, "Selecciona un proveedor");
                return false;
            }
        }
        else
        {
            JOptionPane.showMessageDialog(null, "Selecciona una empresa a la que se va a facturar");
            return false;
        }
    }
    
    
    
    public class MyModel extends DefaultTableModel
    {
        Class[] types = new Class [] 
        {
            java.lang.String.class/*interno*/, 
            java.lang.String.class/*#*/, 
            java.lang.String.class/*R.Valua*/, 
            java.lang.String.class/*N° Parte*/, 
            java.lang.String.class/*Folio*/, 
            java.lang.String.class/*Descripción*/, 
            java.lang.String.class/*Medida*/, 
            java.lang.String.class/*Plazo*/, 
            java.lang.Double.class/*Cantidad*/, 
            java.lang.Double.class/*Costo c/u*/, 
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
            Vector vector = (Vector)this.dataVector.elementAt(row);
            Object celda = ((Vector)this.dataVector.elementAt(row)).elementAt(col);
            switch(col)
            {
                case 3:
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
                                    Partida part=(Partida) session.createCriteria(Partida.class).add(Restrictions.eq("ordenByIdOrden.idOrden", Integer.parseInt(t_orden.getText()))).add(Restrictions.eq("idEvaluacion", Integer.parseInt(t_datos.getValueAt(t_datos.getSelectedRow(), 1).toString()))).add(Restrictions.eq("subPartida", Integer.parseInt(t_datos.getValueAt(t_datos.getSelectedRow(), 2).toString()))).setMaxResults(1).uniqueResult();
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
                case 8://cantidad
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
                            if((double)value>0)
                            {
                                Partida part=(Partida) session.get(Partida.class, Integer.parseInt(t_datos.getValueAt(row, 0).toString()));
                                if((double)value<=part.getCantidadAut())
                                {
                                    vector.setElementAt(value, col);
                                    this.dataVector.setElementAt(vector, row);
                                    fireTableCellUpdated(row, col);
                                }
                                else
                                    JOptionPane.showMessageDialog(null, "La cantidad mayor es: "+part.getCantidadAut()); 
                            }
                            else
                                JOptionPane.showMessageDialog(null, "El campo no permite números menorea a 1"); 
                            t_datos.setColumnSelectionInterval(col, col);
                            t_datos.setRowSelectionInterval(row, row);
                            if(session!=null)
                                if(session.isOpen())
                                    session.close();
                        }
                        else
                        {
                            vector.setElementAt(value, col);
                            this.dataVector.setElementAt(vector, row);
                            fireTableCellUpdated(row, col);
                        }
                        sumaTotales();
                    }
                    break;
                case 9://Costo c/u
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
                            if((double)value>=0)
                            {
                                Session session = HibernateUtil.getSessionFactory().openSession();
                                //Partida part=(Partida) session.createCriteria(Partida.class).add(Restrictions.eq("ordenByIdOrden.idOrden", Integer.parseInt(t_orden.getText()))).add(Restrictions.eq("idEvaluacion", Integer.parseInt(t_datos.getValueAt(t_datos.getSelectedRow(), 0).toString()))).add(Restrictions.eq("subPartida", Integer.parseInt(t_datos.getValueAt(t_datos.getSelectedRow(), 1).toString()))).setMaxResults(1).uniqueResult();
                                Partida part=(Partida) session.get(Partida.class, Integer.parseInt(t_datos.getValueAt(row, 0).toString()));
                                
                                if(Double.parseDouble(value.toString())<=part.getPrecioAutCU())
                                {
                                    vector.setElementAt(value, col);
                                    this.dataVector.setElementAt(vector, row);
                                    fireTableCellUpdated(row, col);
                                }
                                else
                                {
                                    usrAut=null;
                                    Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
                                    autorizarCosto.setSize(284, 192);//284, 177
                                    t_user.setText("");
                                    t_contra.setText("");
                                    autorizarCosto.setLocation((d.width/2)-(autorizarCosto.getWidth()/2), (d.height/2)-(autorizarCosto.getHeight()/2));
                                    autorizarCosto.setVisible(true);
                                    if(usrAut!=null)
                                    {
                                        session = HibernateUtil.getSessionFactory().openSession();
                                        session.beginTransaction().begin();
                                        vector.setElementAt(value, col);
                                        this.dataVector.setElementAt(vector, row);
                                        fireTableCellUpdated(row, col);
                                    }
                                }
                                if(session!=null)
                                    if(session.isOpen())
                                        session.close();
                            }
                            else
                                JOptionPane.showMessageDialog(null, "El campo no permite números negativos"); 
                            sumaTotales();
                            t_datos.setColumnSelectionInterval(col, col);
                            t_datos.setRowSelectionInterval(row, row);
                            t_datos.requestFocus();
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
                default:
                    vector.setElementAt(value, col);
                    dataVector.setElementAt(vector, row);
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
            //Vector vector = (Vector)dataVector.elementAt(0);
            dataVector.remove(row);
            celdaEditable.remove(row);
            //vector.remove(row);
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
                if(x==8 || x==9)
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
            //this.celdaEditable[ columna ][ fila ] = editable;
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
    void consulta()
    {
        h=new Herramientas(usr, 0);
        h.session(sessionPrograma);
        if(t_busca.getText().compareToIgnoreCase("")!=0)
        {
            if(x>=t_datos.getRowCount())
                x=0;
            for(; x<t_datos.getRowCount(); x++)
            {
                if(t_datos.getValueAt(x, 5).toString().indexOf(t_busca.getText()) != -1)
                {
                    t_datos.setRowSelectionInterval(x, x);
                    t_datos.setColumnSelectionInterval(4, 5);
                    break;
                }
            }
            x++;
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
    
    private void datos_unidad(String tipo, String marca, String modelo, String serie, String id_aseguradora, String aseguradora, String siniestro, String asegurado, String plazo)
    {
        t_tipo.setText(tipo);
        t_marca.setText(marca);
        t_modelo.setText(modelo);
        t_id_aseguradora.setText(id_aseguradora);
        this.t_aseguradora.setText(aseguradora);
        t_siniestro.setText(siniestro);
        t_asegurado.setText(asegurado);    
        t_plazo.setText(plazo);
    }
    
    public void formatoTabla()
    {
        Color c1 = new java.awt.Color(2, 135, 242);   
        for(int x=0; x<t_datos.getColumnModel().getColumnCount(); x++)
        {
            t_datos.getColumnModel().getColumn(x).setHeaderRenderer(new Render1(c1));
        }
        tabla_tamaños();
        t_datos.setShowVerticalLines(true);
        t_datos.setShowHorizontalLines(true);
        
        FormatoEditor fe=new FormatoEditor();
        t_datos.setDefaultEditor(Double.class, fe);
        
        FormatoEditorTexto fs=new FormatoEditorTexto();
        t_datos.setDefaultEditor(String.class, fs);
        
        t_datos.setDefaultRenderer(Double.class, formato); 
        t_datos.setDefaultRenderer(Integer.class, formato);
        t_datos.setDefaultRenderer(String.class, formato);
    }
}