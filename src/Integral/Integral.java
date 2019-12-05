/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Integral;

import Agente.editaAgente;
import Ajustador.editaAjustador;
import Almacen.Existencias;
import Almacen.Reporte2;
import Almacen.Responsiva;
import Almacen.ajusteInventario;
import Catalogo.buscaCatalogo;
import Catalogo.editaCatalogo;
import Ciclo.buscaCiclo;
import Ciclo.editaCiclo;
import Clientes.buscaCliente;
import Clientes.editaCliente;
import Compania.NuevaCompania;
import Compania.buscaCompania;
import Compania.editaCompania;
import Compras.EliminaPedido;
import Compras.buscaPedido;
import Compras.consultaPedido;
import Compras.editaPedido;
import Compras.nuevoPedido;
import Compras.reportePedidos;
import Ejemplar.buscaEjemplar;
import Ejemplar.editaEjemplar;
import Especialidad.buscaEspecialidad;
import Especialidad.editaEspecialidad;
import Estatus.buscaEstatus;
import Estatus.editaEstatus;
import Hibernate.Util.HibernateUtil;
import Hibernate.entidades.Almacen;
import Hibernate.entidades.Catalogo;
import Hibernate.entidades.Ciclo;
import Hibernate.entidades.Clientes;
import Hibernate.entidades.Compania;
import Hibernate.entidades.Configuracion;
import Hibernate.entidades.Ejemplar;
import Hibernate.entidades.Empleado;
import Hibernate.entidades.Especialidad;
import Hibernate.entidades.Estatus;
import Hibernate.entidades.Orden;
import Hibernate.entidades.Pedido;
import Hibernate.entidades.Proveedor;
import Hibernate.entidades.Puestos;
import Hibernate.entidades.Reparacion;
import Hibernate.entidades.Tipo;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.List;
import javax.swing.ImageIcon;
import Hibernate.entidades.Usuario;
import Marca.editaMarca;
import Proveedor.buscaProveedor;
import Proveedor.editaProveedor;
import Proveedor.nuevoProveedor;
import Puestos.buscaPuestos;
import Puestos.editaPuestos;
import Reparacion.buscaReparacion;
import Reparacion.editaReparacion;
import Servicios.*;
import Tipo.buscaTipo;
import Tipo.editaTipo;
import Usuarios.altaUsuario;
import Usuarios.buscaUsuarios;
import Usuarios.editaUsuario;
import Almacen.buscaEntrada;
import Almacen.muestraAlmacen;
import Almacen.nuevoAlmacen;
import Contabilidad.Cuentas;
import Contabilidad.Egresos;
import Contabilidad.NuevoComprobante;
import Contabilidad.NuevoPago;
import Contabilidad.Provision;
import Contabilidad.RCuentas;
import Contabilidad.buscaFactura;
import Contabilidad.buscaNota;
import Contabilidad.buscaPago;
import Cuentas.buscaCuentas;
import Cuentas.editaCuentas;
import Empleados.buscaEmpleado;
import Empleados.modificaEmpleado;
import Grupo.editaGrupo;
import Herramientas.buscaHerramienta;
import Herramientas.editaHerramienta;
import Hibernate.entidades.Herramienta;
import KPI.Compras_Kpi;
import Operaciones.Adicionales;
import Operaciones.ResponsablesOP;
import Operaciones.Tiempos;
import Valuacion.Presupuestos;
import java.awt.AWTEvent;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.AWTEventListener;
import java.util.Random;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.plaf.basic.BasicMenuBarUI;
import org.hibernate.Session;
import Valuacion.Reportes;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author I.S.C Salvador
 */
public class Integral extends javax.swing.JFrame {
   
    private ModificarOrden Modificar_Orden;
    private Reportes reporteVal;
    private RCuentas Rcuenta;
    private reportePedidos reporteCom;
    private AperturaOrden Apertura_Orden;
    private ModificarOrden Modificar_Orden_Valuacion;
    private ModificarOrden Modificar_destajos;
    private Adicionales adicionales;
    private ModificarOrden generarCotizacion;
    private ModificarOrden generarPedidos;
    private ModificarOrden avancePedidos;
    private ModificarOrden preFac;
    private consultaPedido autoriza_pedidos;
    private editaPedido edita_pedidos;
    private EliminaPedido elimina_pedido;
    private muestraAlmacen muestra_almacen;
    private nuevoAlmacen nuevo_almacen;
    private Compras_Kpi kpi;
    private Provision provision;
    private ResponsablesOP panel_responsables;
    NuevoComprobante nuevaFactura;
    NuevoPago nuevoPago;
    private editaCuentas eCuentas;
    Cuentas cxc;
    
    private nuevoPedido npedido;
    private altaUsuario nuevoUsuario;

    private Reporte2 reporte2;
    private editaCliente eCliente;

    private editaUsuario editar_usuario;
    private editaPuestos ePuestos;
    
    private modificaEmpleado mEmpleado;
    private editaEstatus eEstatus;
    
    private editaMarca eMarca;
    private editaEspecialidad eEspecialidad;
    private editaCompania ecompania;
    private editaGrupo eServicios;
    private int pos=-1;
    private Usuario actor=null;
    private String sessionPrograma="";
    
    private editaReparacion eReparacion;
    private editaTipo eTipo;
    private editaCatalogo eCatalogo;
    private editaCiclo eCiclo;
    private Egresos egresos;
    private Existencias existencias;
    private ajusteInventario ajuste;
    
    private editaProveedor eproveedor;
    private configuracion panel_configuracion;
    private Responsiva responsiva;
    private respaldo panel_respaldo;
    private editaHerramienta eHerramienta;
    private Presupuestos presupuestos;
    editaEjemplar eEjemplar;
    final Herramientas h;
    acceso ventana;
    List lista;
    static Integral entrada;
    static Tiempo tiempo=new Tiempo();
    private String periodo;
    String ruta="";
    String rutaFTP="";
    int configuracion=2;
    
    private editaAjustador eAjustador;
    private editaAgente eAgente;
    
    public Integral() {
        
        Version ver=new Version();
        boolean respuesta=ver.version();
        if(respuesta==true)
        {
            try{
                Runtime runtime = Runtime.getRuntime();
                Process child = runtime.exec("java -jar Descarga.jar");
            }catch(Exception e){}
            System.exit(0);
        }
        ventana = new acceso(new javax.swing.JFrame(), true, configuracion);
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension medida=ventana.getSize();
        ventana.setLocation((d.width/2)-(medida.width/2), (d.height/2)-(medida.height/2));
        ventana.setVisible(true);
        lista=ventana.getReturnStatus();
        ruta=ventana.ruta;
        rutaFTP=ventana.rutaFTP;
        medida=null;
        d=null;
        if(lista!=null)
        {
            initComponents();
            eliminaBloqueos();
            jMenuBar1.setUI ( new BasicMenuBarUI ()
            {
                public void paint ( Graphics g, JComponent c )
                {
                    g.setColor ( new Color(1, 42, 102) );// 51,51,51
                    g.fillRect ( 0, 0, c.getWidth (), c.getHeight () );
                }
            } );
            
            this.P_pestana.setUI (new TabbedPaneUI());
            //**********************************
            setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
 
            addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosing(java.awt.event.WindowEvent evt) {
                    close();
                }
            });
            //**********************************
            Session session = HibernateUtil.getSessionFactory().openSession();
            try
            {
                actor=(Usuario)lista.get(0);
                periodo=(String)lista.get(1);
                //*****guardamos el acceso en la BD
                Random rng=new Random();
                long  dig8 = rng.nextInt(90000000)+10000000;
                sessionPrograma=""+dig8;
                rng=null;
                tiempo.Contar(actor, sessionPrograma);
                session.beginTransaction().begin();
                actor=(Usuario)session.get(Usuario.class, actor.getIdUsuario());
                Configuracion con=(Configuracion)session.get(Configuracion.class, 2);
                this.l_nombre.setText(con.getEmpresa());
                con=null;
                actor.setSession(sessionPrograma);
                session.update(actor);
                session.getTransaction().commit();
                t_usuario.setText(actor.getEmpleado().getNombre());
                t_periodo.setText(periodo);
                this.setExtendedState(this.MAXIMIZED_BOTH);
                this.p_img.add(new Imagen("imagenes/empresa300116.jpg", 451, 210, 0, 0, 451, 210));
                if(session!=null)
                    if(session.isOpen())
                        session.close();
            }catch(Exception e)
            {
                System.out.println(e);
            }
            if(session!=null)
                if(session.isOpen())
                    session.close();
            session=null;
        }
        else
        {
            System.exit(0);
        }
        lista=null;
        h=new Herramientas(this.actor, 0);
        long eventMask = AWTEvent.MOUSE_MOTION_EVENT_MASK + AWTEvent.MOUSE_EVENT_MASK + AWTEvent.KEY_EVENT_MASK;
        Toolkit.getDefaultToolkit().addAWTEventListener( 
                new AWTEventListener()
                {
                    public void eventDispatched(AWTEvent e)
                    {
                        tiempo.Reiniciar();
                    }
                }, eventMask);
    }

    private void cerrarSession(){
        if (JOptionPane.showConfirmDialog(rootPane, "¿Confime que desea cerrar sessión?",
                "Cerrar sessión", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
        {
            eliminaBloqueos();
            tiempo.Detener();
            this.dispose();
            Integral.main(null);
        }
    }   
    
    private boolean eliminaBloqueos()
    {
        boolean val=false;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try
        {
            if(session.connection().isValid(15))
            {
                session.beginTransaction().begin();
                actor=(Usuario)session.get(Usuario.class, actor.getIdUsuario());
                session.createQuery("Update Orden Set ventana=null, usuarioByBloqueada=null where usuarioByBloqueada.idUsuario='"+actor.getIdUsuario()+"'").executeUpdate();
                session.createQuery("Update Pedido Set ventana=null, usuarioByBloqueado=null where usuarioByBloqueado.idUsuario='"+actor.getIdUsuario()+"'").executeUpdate();
                actor.setSession(sessionPrograma);
                session.update(actor);
                session.getTransaction().commit();
                val=true;
            }
            else
                return false;
        }catch(Exception e)
        {
            val = false;
        }
        if(session.isOpen())
            session.close(); 
        session=null;
        return val;
    }
    
     private void close(){
        if (JOptionPane.showConfirmDialog(rootPane, "¿Desea realmente salir del sistema?",
                "Salir del sistema", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
        {
            eliminaBloqueos();
            System.exit(0);
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

        t_bienvenido1 = new javax.swing.JLabel();
        t_periodo = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        p_tilulo = new javax.swing.JPanel();
        l_nombre = new javax.swing.JLabel();
        t_bienvenido = new javax.swing.JLabel();
        t_usuario = new javax.swing.JLabel();
        p_centro = new javax.swing.JPanel();
        P_pestana = new javax.swing.JTabbedPane();
        p_inicio = new javax.swing.JPanel();
        p_logo = new javax.swing.JPanel();
        p_img = new javax.swing.JPanel();
        jMenuBar1 = new javax.swing.JMenuBar();
        m_servicios = new javax.swing.JMenu();
        m_apertura = new javax.swing.JMenuItem();
        m_consultar = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();
        m_valuacion = new javax.swing.JMenu();
        jMenuItem9 = new javax.swing.JMenuItem();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem10 = new javax.swing.JMenuItem();
        jMenuItem20 = new javax.swing.JMenuItem();
        jMenuItem11 = new javax.swing.JMenuItem();
        m_operaciones = new javax.swing.JMenu();
        jMenuItem31 = new javax.swing.JMenuItem();
        jMenuItem27 = new javax.swing.JMenuItem();
        jMenuItem26 = new javax.swing.JMenuItem();
        jMenuItem45 = new javax.swing.JMenuItem();
        m_compras = new javax.swing.JMenu();
        jMenuItem13 = new javax.swing.JMenuItem();
        jMenuItem14 = new javax.swing.JMenuItem();
        jMenu17 = new javax.swing.JMenu();
        jMenuItem34 = new javax.swing.JMenuItem();
        jMenuItem16 = new javax.swing.JMenuItem();
        jMenuItem18 = new javax.swing.JMenuItem();
        jMenuItem25 = new javax.swing.JMenuItem();
        jMenuItem37 = new javax.swing.JMenuItem();
        jMenuItem23 = new javax.swing.JMenuItem();
        jMenuItem17 = new javax.swing.JMenuItem();
        jMenu20 = new javax.swing.JMenu();
        jMenuItem19 = new javax.swing.JMenuItem();
        jMenuItem21 = new javax.swing.JMenuItem();
        jMenu18 = new javax.swing.JMenu();
        m_consultar_articulo1 = new javax.swing.JMenuItem();
        m_editar_articulo1 = new javax.swing.JMenuItem();
        jMenuItem46 = new javax.swing.JMenuItem();
        jMenuItem53 = new javax.swing.JMenuItem();
        jMenu26 = new javax.swing.JMenu();
        jMenuItem40 = new javax.swing.JMenuItem();
        jMenuItem39 = new javax.swing.JMenuItem();
        jMenuItem41 = new javax.swing.JMenuItem();
        jMenuItem54 = new javax.swing.JMenuItem();
        jMenuItem38 = new javax.swing.JMenuItem();
        jMenu21 = new javax.swing.JMenu();
        jMenu22 = new javax.swing.JMenu();
        jMenuItem32 = new javax.swing.JMenuItem();
        jMenuItem29 = new javax.swing.JMenuItem();
        jMenuItem33 = new javax.swing.JMenuItem();
        jMenuItem30 = new javax.swing.JMenuItem();
        jMenu24 = new javax.swing.JMenu();
        jMenu25 = new javax.swing.JMenu();
        jMenuItem50 = new javax.swing.JMenuItem();
        jMenuItem51 = new javax.swing.JMenuItem();
        jMenuItem52 = new javax.swing.JMenuItem();
        jMenuItem49 = new javax.swing.JMenuItem();
        jMenuItem48 = new javax.swing.JMenuItem();
        jMenuItem47 = new javax.swing.JMenuItem();
        m_administracion = new javax.swing.JMenu();
        jMenu27 = new javax.swing.JMenu();
        jMenuItem60 = new javax.swing.JMenuItem();
        m_catalogos = new javax.swing.JMenu();
        m_edita_ciclo = new javax.swing.JMenu();
        jMenuItem35 = new javax.swing.JMenuItem();
        jMenuItem22 = new javax.swing.JMenuItem();
        jMenu16 = new javax.swing.JMenu();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem4 = new javax.swing.JMenuItem();
        jMenuItem6 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        m_consultar_puestos = new javax.swing.JMenuItem();
        m_editar_puestos = new javax.swing.JMenuItem();
        m_clientes = new javax.swing.JMenu();
        m_consulta_clientes = new javax.swing.JMenuItem();
        m_edita_clientes = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
        jMenuItem7 = new javax.swing.JMenuItem();
        jMenuItem8 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenu7 = new javax.swing.JMenu();
        m_consultar_estatus = new javax.swing.JMenuItem();
        m_editar_estatus = new javax.swing.JMenuItem();
        jMenu14 = new javax.swing.JMenu();
        jMenu13 = new javax.swing.JMenu();
        m_consultar_articulo = new javax.swing.JMenuItem();
        m_editar_articulo = new javax.swing.JMenuItem();
        jMenu5 = new javax.swing.JMenu();
        m_consultar_catalogo = new javax.swing.JMenuItem();
        m_editar_catalogo = new javax.swing.JMenuItem();
        jMenu6 = new javax.swing.JMenu();
        m_consultar_especialidad = new javax.swing.JMenuItem();
        m_editar_especialidad = new javax.swing.JMenuItem();
        jMenu19 = new javax.swing.JMenu();
        jMenuItem44 = new javax.swing.JMenuItem();
        jMenu11 = new javax.swing.JMenu();
        nuevo_proveedor = new javax.swing.JMenuItem();
        busca_proveedor = new javax.swing.JMenuItem();
        edita_proveedor = new javax.swing.JMenuItem();
        jMenu8 = new javax.swing.JMenu();
        m_consultar_reparacion = new javax.swing.JMenuItem();
        m_editar_reparacion = new javax.swing.JMenuItem();
        jMenu15 = new javax.swing.JMenu();
        jMenu10 = new javax.swing.JMenu();
        m_consultar_tipo = new javax.swing.JMenuItem();
        m_editar_tipo = new javax.swing.JMenuItem();
        m_editar_marca = new javax.swing.JMenuItem();
        jMenuItem57 = new javax.swing.JMenuItem();
        jMenuItem59 = new javax.swing.JMenuItem();
        jMenu9 = new javax.swing.JMenu();
        jMenuItem5 = new javax.swing.JMenuItem();
        jMenuItem12 = new javax.swing.JMenuItem();
        jMenuItem24 = new javax.swing.JMenuItem();
        jMenuItem28 = new javax.swing.JMenuItem();
        m_itilerias = new javax.swing.JMenu();
        jMenuItem42 = new javax.swing.JMenuItem();
        jMenuItem15 = new javax.swing.JMenuItem();
        jMenuItem36 = new javax.swing.JMenuItem();
        jMenuItem43 = new javax.swing.JMenuItem();
        jCheckBoxMenuItem1 = new javax.swing.JCheckBoxMenuItem();
        jMenuItem55 = new javax.swing.JMenuItem();

        t_bienvenido1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        t_bienvenido1.setForeground(new java.awt.Color(255, 255, 255));
        t_bienvenido1.setText("Periodo:");

        t_periodo.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        t_periodo.setForeground(new java.awt.Color(255, 255, 255));
        t_periodo.setText("jLabel2");

        jLabel1.setFont(new java.awt.Font("Tahoma", 3, 18)); // NOI18N
        jLabel1.setIcon(new ImageIcon("imagenes/procesos1.png"));

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Integral Administración de Taller Automotriz v.3.1");
        setIconImage(new ImageIcon("imagenes/procesos.png").getImage());
        setMinimumSize(new java.awt.Dimension(1050, 700));
        setName("Sisitema de Administración de Ordenes"); // NOI18N
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        p_tilulo.setBackground(new java.awt.Color(255, 255, 255));

        l_nombre.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        l_nombre.setForeground(new java.awt.Color(90, 66, 126));
        l_nombre.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        l_nombre.setText("Servicio Especializado Toluca S.A. de C.V.");

        t_bienvenido.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        t_bienvenido.setForeground(new java.awt.Color(90, 66, 126));
        t_bienvenido.setText("Usuario:");

        t_usuario.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        t_usuario.setForeground(new java.awt.Color(90, 66, 126));
        t_usuario.setText("jLabel2");

        javax.swing.GroupLayout p_tiluloLayout = new javax.swing.GroupLayout(p_tilulo);
        p_tilulo.setLayout(p_tiluloLayout);
        p_tiluloLayout.setHorizontalGroup(
            p_tiluloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(p_tiluloLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(t_bienvenido)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(t_usuario, javax.swing.GroupLayout.DEFAULT_SIZE, 451, Short.MAX_VALUE)
                .addGap(10, 10, 10)
                .addComponent(l_nombre, javax.swing.GroupLayout.DEFAULT_SIZE, 521, Short.MAX_VALUE)
                .addContainerGap())
        );
        p_tiluloLayout.setVerticalGroup(
            p_tiluloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, p_tiluloLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(p_tiluloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(l_nombre)
                    .addComponent(t_bienvenido)
                    .addComponent(t_usuario))
                .addContainerGap())
        );

        getContentPane().add(p_tilulo, java.awt.BorderLayout.PAGE_START);

        p_centro.setLayout(new java.awt.BorderLayout());

        P_pestana.setBackground(new java.awt.Color(1, 42, 102));
        P_pestana.setTabPlacement(javax.swing.JTabbedPane.BOTTOM);
        P_pestana.setOpaque(true);

        p_inicio.setBackground(new java.awt.Color(255, 255, 255));

        p_logo.setOpaque(false);
        p_logo.setPreferredSize(new java.awt.Dimension(451, 210));

        p_img.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout p_imgLayout = new javax.swing.GroupLayout(p_img);
        p_img.setLayout(p_imgLayout);
        p_imgLayout.setHorizontalGroup(
            p_imgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 514, Short.MAX_VALUE)
        );
        p_imgLayout.setVerticalGroup(
            p_imgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 223, Short.MAX_VALUE)
        );

        p_logo.add(p_img);

        javax.swing.GroupLayout p_inicioLayout = new javax.swing.GroupLayout(p_inicio);
        p_inicio.setLayout(p_inicioLayout);
        p_inicioLayout.setHorizontalGroup(
            p_inicioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(p_inicioLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(p_logo, javax.swing.GroupLayout.DEFAULT_SIZE, 1029, Short.MAX_VALUE)
                .addContainerGap())
        );
        p_inicioLayout.setVerticalGroup(
            p_inicioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(p_inicioLayout.createSequentialGroup()
                .addGap(129, 129, 129)
                .addComponent(p_logo, javax.swing.GroupLayout.PREFERRED_SIZE, 229, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(148, Short.MAX_VALUE))
        );

        P_pestana.addTab("Inicio", p_inicio);

        p_centro.add(P_pestana, java.awt.BorderLayout.CENTER);

        getContentPane().add(p_centro, java.awt.BorderLayout.CENTER);

        jMenuBar1.setBackground(new java.awt.Color(51, 51, 51));
        jMenuBar1.setBorderPainted(false);

        m_servicios.setForeground(new java.awt.Color(255, 255, 255));
        m_servicios.setIcon(new javax.swing.ImageIcon(getClass().getResource("/recursos/servicio.png"))); // NOI18N
        m_servicios.setMnemonic('s');
        m_servicios.setText("Servicio");
        m_servicios.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N

        m_apertura.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_A, java.awt.event.InputEvent.CTRL_MASK));
        m_apertura.setText("Aperturar");
        m_apertura.setBorderPainted(true);
        m_apertura.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_aperturaActionPerformed(evt);
            }
        });
        m_servicios.add(m_apertura);

        m_consultar.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_E, java.awt.event.InputEvent.CTRL_MASK));
        m_consultar.setText("Editar");
        m_consultar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_consultarActionPerformed(evt);
            }
        });
        m_servicios.add(m_consultar);

        jMenuItem3.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem3.setText("Consulta");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        m_servicios.add(jMenuItem3);

        jMenuBar1.add(m_servicios);

        m_valuacion.setForeground(new java.awt.Color(255, 255, 255));
        m_valuacion.setIcon(new javax.swing.ImageIcon(getClass().getResource("/recursos/valuacion.png"))); // NOI18N
        m_valuacion.setMnemonic('v');
        m_valuacion.setText("Valuación");
        m_valuacion.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N

        jMenuItem9.setText("Abrir");
        jMenuItem9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem9ActionPerformed(evt);
            }
        });
        m_valuacion.add(jMenuItem9);

        jMenuItem1.setText("Presupuestos");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        m_valuacion.add(jMenuItem1);

        jMenuItem10.setText("Tiempos de reparación");
        jMenuItem10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem10ActionPerformed(evt);
            }
        });
        m_valuacion.add(jMenuItem10);

        jMenuItem20.setText("Pre-factura");
        jMenuItem20.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem20ActionPerformed(evt);
            }
        });
        m_valuacion.add(jMenuItem20);

        jMenuItem11.setText("Reportes");
        jMenuItem11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem11ActionPerformed(evt);
            }
        });
        m_valuacion.add(jMenuItem11);

        jMenuBar1.add(m_valuacion);

        m_operaciones.setForeground(new java.awt.Color(255, 255, 255));
        m_operaciones.setIcon(new javax.swing.ImageIcon(getClass().getResource("/recursos/operaciones.png"))); // NOI18N
        m_operaciones.setMnemonic('o');
        m_operaciones.setText("Operaciones");
        m_operaciones.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N

        jMenuItem31.setText("Asignación responsables");
        jMenuItem31.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem31ActionPerformed(evt);
            }
        });
        m_operaciones.add(jMenuItem31);

        jMenuItem27.setText("Destajos");
        jMenuItem27.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem27ActionPerformed(evt);
            }
        });
        m_operaciones.add(jMenuItem27);

        jMenuItem26.setText("Trabajos Adicionales");
        jMenuItem26.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem26ActionPerformed(evt);
            }
        });
        m_operaciones.add(jMenuItem26);

        jMenuItem45.setText("Tiempos");
        jMenuItem45.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem45ActionPerformed(evt);
            }
        });
        m_operaciones.add(jMenuItem45);

        jMenuBar1.add(m_operaciones);

        m_compras.setForeground(new java.awt.Color(255, 255, 255));
        m_compras.setIcon(new javax.swing.ImageIcon(getClass().getResource("/recursos/compras.png"))); // NOI18N
        m_compras.setMnemonic('c');
        m_compras.setText("Compras");
        m_compras.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N

        jMenuItem13.setText("Refacciones Cotiza");
        jMenuItem13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem13ActionPerformed(evt);
            }
        });
        m_compras.add(jMenuItem13);

        jMenuItem14.setText("Genera Cotizaciones");
        jMenuItem14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem14ActionPerformed(evt);
            }
        });
        m_compras.add(jMenuItem14);

        jMenu17.setText("Pedidos");

        jMenuItem34.setText("Nuevo");
        jMenuItem34.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem34ActionPerformed(evt);
            }
        });
        jMenu17.add(jMenuItem34);

        jMenuItem16.setText("Pedidos y Avances");
        jMenuItem16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem16ActionPerformed(evt);
            }
        });
        jMenu17.add(jMenuItem16);

        jMenuItem18.setText("Avance de Pedidos");
        jMenuItem18.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem18ActionPerformed(evt);
            }
        });
        jMenu17.add(jMenuItem18);

        jMenuItem25.setText("Modificar Pedido");
        jMenuItem25.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem25ActionPerformed(evt);
            }
        });
        jMenu17.add(jMenuItem25);

        jMenuItem37.setText("Eliminar Pedido");
        jMenuItem37.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem37ActionPerformed(evt);
            }
        });
        jMenu17.add(jMenuItem37);

        jMenuItem23.setText("Autorizar Pedidos");
        jMenuItem23.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem23ActionPerformed(evt);
            }
        });
        jMenu17.add(jMenuItem23);

        jMenuItem17.setText("Reporte");
        jMenuItem17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem17ActionPerformed(evt);
            }
        });
        jMenu17.add(jMenuItem17);

        m_compras.add(jMenu17);

        jMenuBar1.add(m_compras);

        jMenu20.setForeground(new java.awt.Color(255, 255, 255));
        jMenu20.setIcon(new javax.swing.ImageIcon(getClass().getResource("/recursos/almacen.png"))); // NOI18N
        jMenu20.setText("Almacen");
        jMenu20.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N

        jMenuItem19.setText("Nuevo Movimiento");
        jMenuItem19.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem19ActionPerformed(evt);
            }
        });
        jMenu20.add(jMenuItem19);

        jMenuItem21.setText("Consultar Movimiento");
        jMenuItem21.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem21ActionPerformed(evt);
            }
        });
        jMenu20.add(jMenuItem21);

        jMenu18.setText("Ejemplares");

        m_consultar_articulo1.setText("Consultar");
        m_consultar_articulo1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_consultar_articulo1ActionPerformed(evt);
            }
        });
        jMenu18.add(m_consultar_articulo1);

        m_editar_articulo1.setText("Editar");
        m_editar_articulo1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_editar_articulo1ActionPerformed(evt);
            }
        });
        jMenu18.add(m_editar_articulo1);

        jMenu20.add(jMenu18);

        jMenuItem46.setText("Reportes");
        jMenuItem46.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem46ActionPerformed(evt);
            }
        });
        jMenu20.add(jMenuItem46);

        jMenuItem53.setText("Existencias");
        jMenuItem53.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem53ActionPerformed(evt);
            }
        });
        jMenu20.add(jMenuItem53);

        jMenu26.setText("Herramientas");

        jMenuItem40.setText("Consultar");
        jMenuItem40.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem40ActionPerformed(evt);
            }
        });
        jMenu26.add(jMenuItem40);

        jMenuItem39.setText("Editar");
        jMenuItem39.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem39ActionPerformed(evt);
            }
        });
        jMenu26.add(jMenuItem39);

        jMenuItem41.setText("Responsiva");
        jMenuItem41.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem41ActionPerformed(evt);
            }
        });
        jMenu26.add(jMenuItem41);

        jMenu20.add(jMenu26);

        jMenuItem54.setText("Ajuste Inventario");
        jMenuItem54.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem54ActionPerformed(evt);
            }
        });
        jMenu20.add(jMenuItem54);

        jMenuItem38.setText("Eliminar Entradas");
        jMenuItem38.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem38ActionPerformed(evt);
            }
        });
        jMenu20.add(jMenuItem38);

        jMenuBar1.add(jMenu20);

        jMenu21.setForeground(new java.awt.Color(254, 254, 254));
        jMenu21.setIcon(new javax.swing.ImageIcon(getClass().getResource("/recursos/contabilidad.png"))); // NOI18N
        jMenu21.setMnemonic('n');
        jMenu21.setText("Contabilidad");
        jMenu21.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N

        jMenu22.setText("Facturación");

        jMenuItem32.setText("Nuevo Documento");
        jMenuItem32.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem32ActionPerformed(evt);
            }
        });
        jMenu22.add(jMenuItem32);

        jMenuItem29.setText("Consultar Factura");
        jMenuItem29.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem29ActionPerformed(evt);
            }
        });
        jMenu22.add(jMenuItem29);

        jMenuItem33.setText("Consultar  nota de crédito");
        jMenuItem33.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem33ActionPerformed(evt);
            }
        });
        jMenu22.add(jMenuItem33);

        jMenuItem30.setText("Consultar Pago");
        jMenuItem30.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem30ActionPerformed(evt);
            }
        });
        jMenu22.add(jMenuItem30);

        jMenu21.add(jMenu22);

        jMenu24.setText("COI");

        jMenu25.setText("Cuentas");

        jMenuItem50.setText("Consultar");
        jMenuItem50.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem50ActionPerformed(evt);
            }
        });
        jMenu25.add(jMenuItem50);

        jMenuItem51.setText("Editar");
        jMenuItem51.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem51ActionPerformed(evt);
            }
        });
        jMenu25.add(jMenuItem51);

        jMenu24.add(jMenu25);

        jMenuItem52.setText("Poliza de pagos");
        jMenuItem52.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem52ActionPerformed(evt);
            }
        });
        jMenu24.add(jMenuItem52);

        jMenuItem49.setText("Poliza de Provisiones");
        jMenuItem49.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem49ActionPerformed(evt);
            }
        });
        jMenu24.add(jMenuItem49);

        jMenu21.add(jMenu24);

        jMenuItem48.setText("Cuentas por Cobrar");
        jMenuItem48.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem48ActionPerformed(evt);
            }
        });
        jMenu21.add(jMenuItem48);

        jMenuItem47.setText("Reportes");
        jMenuItem47.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem47ActionPerformed(evt);
            }
        });
        jMenu21.add(jMenuItem47);

        jMenuBar1.add(jMenu21);

        m_administracion.setForeground(new java.awt.Color(255, 255, 255));
        m_administracion.setIcon(new javax.swing.ImageIcon(getClass().getResource("/recursos/administracion.png"))); // NOI18N
        m_administracion.setMnemonic('a');
        m_administracion.setText("Administración");
        m_administracion.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N

        jMenu27.setText("Indice de Rendimiento Individual");

        jMenuItem60.setText("Compas y Almacen");
        jMenuItem60.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem60ActionPerformed(evt);
            }
        });
        jMenu27.add(jMenuItem60);

        m_administracion.add(jMenu27);

        m_catalogos.setText("Catalogos");

        m_edita_ciclo.setText("Ciclo");

        jMenuItem35.setText("Consulta");
        jMenuItem35.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem35ActionPerformed(evt);
            }
        });
        m_edita_ciclo.add(jMenuItem35);

        jMenuItem22.setText("Edita");
        jMenuItem22.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem22ActionPerformed(evt);
            }
        });
        m_edita_ciclo.add(jMenuItem22);

        m_catalogos.add(m_edita_ciclo);

        jMenu16.setText("Capital Humano");

        jMenu1.setText("Empleados");

        jMenuItem4.setText("Consultar");
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem4);

        jMenuItem6.setText("Editar");
        jMenuItem6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem6ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem6);

        jMenu16.add(jMenu1);

        jMenu2.setText("Puestos");

        m_consultar_puestos.setText("Consultar");
        m_consultar_puestos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_consultar_puestosActionPerformed(evt);
            }
        });
        jMenu2.add(m_consultar_puestos);

        m_editar_puestos.setText("Editar");
        m_editar_puestos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_editar_puestosActionPerformed(evt);
            }
        });
        jMenu2.add(m_editar_puestos);

        jMenu16.add(jMenu2);

        m_catalogos.add(jMenu16);

        m_clientes.setText("Clientes");

        m_consulta_clientes.setText("Consultar");
        m_consulta_clientes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_consulta_clientesActionPerformed(evt);
            }
        });
        m_clientes.add(m_consulta_clientes);

        m_edita_clientes.setText("Editar");
        m_edita_clientes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_edita_clientesActionPerformed(evt);
            }
        });
        m_clientes.add(m_edita_clientes);

        m_catalogos.add(m_clientes);

        jMenu3.setText("Compañias");

        jMenuItem7.setText("Nueva");
        jMenuItem7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem7ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem7);

        jMenuItem8.setText("Consulta");
        jMenuItem8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem8ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem8);

        jMenuItem2.setText("Editar");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem2);

        m_catalogos.add(jMenu3);

        jMenu7.setText("Estatus");

        m_consultar_estatus.setText("Consultar");
        m_consultar_estatus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_consultar_estatusActionPerformed(evt);
            }
        });
        jMenu7.add(m_consultar_estatus);

        m_editar_estatus.setText("Editar");
        m_editar_estatus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_editar_estatusActionPerformed(evt);
            }
        });
        jMenu7.add(m_editar_estatus);

        m_catalogos.add(jMenu7);

        jMenu14.setText("Partidas");

        jMenu13.setText("Ejemplares");

        m_consultar_articulo.setText("Consultar");
        m_consultar_articulo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_consultar_articuloActionPerformed(evt);
            }
        });
        jMenu13.add(m_consultar_articulo);

        m_editar_articulo.setText("Editar");
        m_editar_articulo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_editar_articuloActionPerformed(evt);
            }
        });
        jMenu13.add(m_editar_articulo);

        jMenu14.add(jMenu13);

        jMenu5.setText("Descripciones");

        m_consultar_catalogo.setText("Consultar");
        m_consultar_catalogo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_consultar_catalogoActionPerformed(evt);
            }
        });
        jMenu5.add(m_consultar_catalogo);

        m_editar_catalogo.setText("Editar");
        m_editar_catalogo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_editar_catalogoActionPerformed(evt);
            }
        });
        jMenu5.add(m_editar_catalogo);

        jMenu14.add(jMenu5);

        jMenu6.setText("Grupo Mecánico");

        m_consultar_especialidad.setText("Consultar");
        m_consultar_especialidad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_consultar_especialidadActionPerformed(evt);
            }
        });
        jMenu6.add(m_consultar_especialidad);

        m_editar_especialidad.setText("Editar");
        m_editar_especialidad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_editar_especialidadActionPerformed(evt);
            }
        });
        jMenu6.add(m_editar_especialidad);

        jMenu14.add(jMenu6);

        jMenu19.setText("Plantillas");

        jMenuItem44.setText("Editar");
        jMenuItem44.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem44ActionPerformed(evt);
            }
        });
        jMenu19.add(jMenuItem44);

        jMenu14.add(jMenu19);

        m_catalogos.add(jMenu14);

        jMenu11.setText("Proveedores");

        nuevo_proveedor.setText("Nuevo");
        nuevo_proveedor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nuevo_proveedorActionPerformed(evt);
            }
        });
        jMenu11.add(nuevo_proveedor);

        busca_proveedor.setText("Consulta");
        busca_proveedor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                busca_proveedorActionPerformed(evt);
            }
        });
        jMenu11.add(busca_proveedor);

        edita_proveedor.setText("Editar");
        edita_proveedor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                edita_proveedorActionPerformed(evt);
            }
        });
        jMenu11.add(edita_proveedor);

        m_catalogos.add(jMenu11);

        jMenu8.setText("Reparación");

        m_consultar_reparacion.setText("Consultar");
        m_consultar_reparacion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_consultar_reparacionActionPerformed(evt);
            }
        });
        jMenu8.add(m_consultar_reparacion);

        m_editar_reparacion.setText("Editar");
        m_editar_reparacion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_editar_reparacionActionPerformed(evt);
            }
        });
        jMenu8.add(m_editar_reparacion);

        m_catalogos.add(jMenu8);

        jMenu15.setText("Unidades");

        jMenu10.setText("Tipo");

        m_consultar_tipo.setText("Consultar");
        m_consultar_tipo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_consultar_tipoActionPerformed(evt);
            }
        });
        jMenu10.add(m_consultar_tipo);

        m_editar_tipo.setText("Editar");
        m_editar_tipo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_editar_tipoActionPerformed(evt);
            }
        });
        jMenu10.add(m_editar_tipo);

        jMenu15.add(jMenu10);

        m_catalogos.add(jMenu15);

        m_editar_marca.setText("Marca");
        m_editar_marca.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_editar_marcaActionPerformed(evt);
            }
        });
        m_catalogos.add(m_editar_marca);

        jMenuItem57.setText("Ajustador");
        jMenuItem57.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem57ActionPerformed(evt);
            }
        });
        m_catalogos.add(jMenuItem57);

        jMenuItem59.setText("Agente");
        jMenuItem59.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem59ActionPerformed(evt);
            }
        });
        m_catalogos.add(jMenuItem59);

        m_administracion.add(m_catalogos);

        jMenu9.setText("Usuarios");

        jMenuItem5.setText("Alta de Usuarios");
        jMenuItem5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem5ActionPerformed(evt);
            }
        });
        jMenu9.add(jMenuItem5);

        jMenuItem12.setText("Consulta de Usuarios");
        jMenuItem12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem12ActionPerformed(evt);
            }
        });
        jMenu9.add(jMenuItem12);

        m_administracion.add(jMenu9);

        jMenuItem24.setText("Configuración");
        jMenuItem24.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem24ActionPerformed(evt);
            }
        });
        m_administracion.add(jMenuItem24);

        jMenuItem28.setText("Respaldo");
        jMenuItem28.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem28ActionPerformed(evt);
            }
        });
        m_administracion.add(jMenuItem28);

        jMenuBar1.add(m_administracion);

        m_itilerias.setForeground(new java.awt.Color(255, 255, 255));
        m_itilerias.setIcon(new javax.swing.ImageIcon(getClass().getResource("/recursos/utilerias.png"))); // NOI18N
        m_itilerias.setMnemonic('u');
        m_itilerias.setText("Utilerias");
        m_itilerias.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N

        jMenuItem42.setText("Manual de usuario");
        jMenuItem42.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem42ActionPerformed(evt);
            }
        });
        m_itilerias.add(jMenuItem42);

        jMenuItem15.setText("Correo");
        jMenuItem15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem15ActionPerformed(evt);
            }
        });
        m_itilerias.add(jMenuItem15);

        jMenuItem36.setText("Cambiar de usuario");
        jMenuItem36.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem36ActionPerformed(evt);
            }
        });
        m_itilerias.add(jMenuItem36);

        jMenuItem43.setText("Cambiar contraseña");
        jMenuItem43.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem43ActionPerformed(evt);
            }
        });
        m_itilerias.add(jMenuItem43);

        jCheckBoxMenuItem1.setText("Cancelar  cierre automático");
        jCheckBoxMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxMenuItem1ActionPerformed(evt);
            }
        });
        m_itilerias.add(jCheckBoxMenuItem1);

        jMenuItem55.setText("Buscar Actualizaciones");
        jMenuItem55.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem55ActionPerformed(evt);
            }
        });
        m_itilerias.add(jMenuItem55);

        jMenuBar1.add(m_itilerias);

        setJMenuBar(jMenuBar1);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void m_consultarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_consultarActionPerformed
        // TODO add your handling code here:
        h.menu=2;
        try
        {
            if(actor.getEditarAperturaOrden()==true)
            {
                pos = P_pestana.indexOfTab("E. Orden");
                if(pos>=0)
                {
                    P_pestana.setSelectedIndex(pos);
                }
                else
                {
                    Modificar_Orden = new ModificarOrden(actor, t_periodo.getText().toString(), h.menu, sessionPrograma, rutaFTP, configuracion);
                    PanelPestanas btc=new PanelPestanas(P_pestana, h.menu, actor);
                    P_pestana.addTab("E. Orden", Modificar_Orden);
                    P_pestana.setSelectedComponent(Modificar_Orden);
                    P_pestana.setTabComponentAt(P_pestana.getSelectedIndex(), btc);
                    Modificar_Orden.t_orden.requestFocus();
                    btc=null;
                }
            }
            else
            {
                JOptionPane.showMessageDialog(null, "¡Acceso denegado!");
            }
        }catch(Exception e)
        {
            System.out.println(e);
            JOptionPane.showMessageDialog(null, "¡Se perdió la conexion con el servidor intente nuevamente!");
        }
    }//GEN-LAST:event_m_consultarActionPerformed

    private void m_aperturaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_aperturaActionPerformed
        // TODO add your handling code here:
        h.menu=1;
        try
        {
            if(actor.getAperturaOrden()==true)
            {
                pos = P_pestana.indexOfTab("Apertura");
                if(pos>=0)
                    P_pestana.setSelectedIndex(pos);
                else
                {
                    Apertura_Orden = new AperturaOrden(actor, t_periodo.getText().toString(), sessionPrograma, rutaFTP, configuracion);
                    P_pestana.addTab("Apertura", Apertura_Orden);
                    P_pestana.setSelectedComponent(Apertura_Orden);
                    P_pestana.setTabComponentAt(P_pestana.getSelectedIndex(), new PanelPestanas(P_pestana,h.menu,actor));
                    Apertura_Orden.t_aseguradora.requestFocus();
                    Apertura_Orden=null;
                }
            }
            else
                JOptionPane.showMessageDialog(null, "¡Acceso denegado!");
        }catch(Exception e)
        {
            System.out.println(e);
            JOptionPane.showMessageDialog(null, "¡Se perdió la conexion con el servidor intente nuevamente!");
        }
    }//GEN-LAST:event_m_aperturaActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        // TODO add your handling code here:
        System.out.println("cerrado");
    }//GEN-LAST:event_formWindowClosing

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        // TODO add your handling code here:
        h.menu=3;
        h.session(sessionPrograma);
        Session session = HibernateUtil.getSessionFactory().openSession();
        try
        {
            buscaOrden obj = new buscaOrden(new javax.swing.JFrame(), true, this.actor,0,configuracion);
            obj.t_busca.requestFocus();
            Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
            obj.setLocation((d.width/2)-(obj.getWidth()/2), (d.height/2)-(obj.getHeight()/2));
            obj.setVisible(true);
            Orden orden_act=obj.getReturnStatus();  
            obj=null;
            if (orden_act!=null)
            {
                session.beginTransaction().begin();
                orden_act=(Orden)session.get(Orden.class, orden_act.getIdOrden());
                pos = P_pestana.indexOfTab("E. Orden");
                if(pos>=0)
                {
                    P_pestana.setSelectedIndex(pos);
                }
                else
                {
                    Modificar_Orden = new ModificarOrden(actor, t_periodo.getText().toString(), h.menu, sessionPrograma, rutaFTP,configuracion);
                    PanelPestanas btc=new PanelPestanas(P_pestana, h.menu, actor);
                    P_pestana.addTab("E. Orden", Modificar_Orden);
                    P_pestana.setSelectedComponent(Modificar_Orden);
                    P_pestana.setTabComponentAt(P_pestana.getSelectedIndex(), btc);
                    Modificar_Orden.t_orden.requestFocus();
                    btc=null;
                }
                Modificar_Orden.t_orden.setText(""+orden_act.getIdOrden());
                Modificar_Orden.orden_act=orden_act;
                Modificar_Orden.consultaOrden();
                Modificar_Orden.b_busca_orden.requestFocus();
                Modificar_Orden.p_ventanas.setSelectedIndex(0);
            }
        }catch(Exception e)
        {
            System.out.println(e);
        }
        if(session!=null)
            if(session.isOpen())
                session.close();
        session=null;
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        // TODO add your handling code here:
        this.repaint();
    }//GEN-LAST:event_formWindowActivated

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed
        // TODO add your handling code here:
        h.menu=0;
        h.session(sessionPrograma);
        Session session = HibernateUtil.getSessionFactory().openSession();
        try
        {
            session.beginTransaction().begin();
            actor = (Usuario)session.get(Usuario.class, actor.getIdUsuario());
            if(actor.getConsultaEmpleados()==true || actor.getEditaEmpleados()==true)
            {
                buscaEmpleado obj = new buscaEmpleado(new javax.swing.JFrame(), true, actor, this.sessionPrograma, true);
                Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
                obj.setLocation((d.width/2)-(obj.getWidth()/2), (d.height/2)-(obj.getHeight()/2));
                obj.setVisible(true);
                Empleado emp_act=obj.getReturnStatus();
                if (emp_act!=null)
                {
                    emp_act=(Empleado)session.get(Empleado.class, emp_act.getIdEmpleado());
                    actor = (Usuario)session.get(Usuario.class, actor.getIdUsuario());
                    if(actor.getEditaEmpleados()==true)
                    {
                        pos=-1;
                        for(int a=0; a<P_pestana.getTabCount(); a++)
                        {
                            if(P_pestana.getTitleAt(a)=="E. Empleado")
                                pos=a;
                        }
                        if(pos>=0)
                        {
                            P_pestana.setSelectedIndex(pos);
                            mEmpleado.t_nombre.requestFocus();
                        }
                        else
                        {
                            mEmpleado = new modificaEmpleado(actor, this.sessionPrograma);
                            PanelPestanas btc=new PanelPestanas(P_pestana,-1, actor);
                            P_pestana.addTab("E. Empleado", mEmpleado);
                            P_pestana.setSelectedComponent(mEmpleado);
                            P_pestana.setTabComponentAt(P_pestana.getSelectedIndex(), btc);
                        }


                        String pago="";
                        switch(emp_act.getFomaPago())
                        {
                            case 0:
                                pago="Día";
                                break;
                            case 1:
                                pago="Hora";
                                break;
                            case 2:
                                pago="Comision";
                                break;
                            case 3:
                                pago="Semana";
                                break;
                            case 4:
                                pago="Catorcena";
                                break;
                            case 5:
                                pago="Mes";
                                break;
                        }
                        mEmpleado.t_numero.setText(""+emp_act.getIdEmpleado());
                        mEmpleado.t_nombre.setText(emp_act.getNombre());
                        mEmpleado.t_direccion.setText(emp_act.getDireccion());
                        if(emp_act.getTelefono()!=null)
                            mEmpleado.t_telefono.setText(emp_act.getTelefono());
                        else
                            mEmpleado.t_telefono.setText("");
                        mEmpleado.c_puesto.setSelectedItem(emp_act.getPuestos().getNombre());
                        if(emp_act.getEmail()!=null)
                            mEmpleado.t_email.setText(emp_act.getEmail());
                        else
                            mEmpleado.t_email.setText("");
                        mEmpleado.c_pago.setSelectedItem(pago);
                        mEmpleado.t_importe.setText(""+emp_act.getImporte());
                    }
                    else
                        JOptionPane.showMessageDialog(null, "¡Acceso denegado!");
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
                session.close();
    }//GEN-LAST:event_jMenuItem4ActionPerformed

    private void jMenuItem6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem6ActionPerformed
        // TODO add your handling code here:
        h.menu=0;
        h.session(sessionPrograma);
        try
        {
            if(actor.getConsultaEmpleados()==true || actor.getEditaEmpleados()==true)
            {
                pos = P_pestana.indexOfTab("E. Empleado");
                if(pos>=0)
                {
                    P_pestana.setSelectedIndex(pos);
                }
                else
                {
                    mEmpleado = new modificaEmpleado(actor, this.sessionPrograma);
                    PanelPestanas btc=new PanelPestanas(P_pestana,-1, actor);
                    P_pestana.addTab("E. Empleado", mEmpleado);
                    P_pestana.setSelectedComponent(mEmpleado);
                    P_pestana.setTabComponentAt(P_pestana.getSelectedIndex(), btc);
                }
            }
            else
            {
                JOptionPane.showMessageDialog(null, "¡Acceso denegado!");
            }
        }catch(Exception e)
        {
            System.out.println(e);
        }
    }//GEN-LAST:event_jMenuItem6ActionPerformed

    private void jMenuItem7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem7ActionPerformed
        // TODO add your handling code here:
        h.menu=0;
        h.session(sessionPrograma);
        try
        {
            if(actor.getEditaCompania()==true)
            {
                pos = P_pestana.indexOfTab("Nueva Compañia");
                if(pos>=0)
                {
                    P_pestana.setSelectedIndex(pos);
                }
                else
                {
                    NuevaCompania ncompania= new NuevaCompania(actor, t_periodo.getText().toString(), this.sessionPrograma);
                    PanelPestanas btc=new PanelPestanas(P_pestana,-1, actor);
                    P_pestana.addTab("Nueva Compañia", ncompania);
                    P_pestana.setSelectedComponent(ncompania);
                    P_pestana.setTabComponentAt(P_pestana.getSelectedIndex(), btc);
                }
            }
            else
                JOptionPane.showMessageDialog(null, "¡Acceso denegado!");
        }catch(Exception e)
        {
            System.out.println(e);
        }
    }//GEN-LAST:event_jMenuItem7ActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        // TODO add your handling code here:
        h.menu=0;
        h.session(sessionPrograma);
        try
        {
            if(actor.getEditaCompania()==true)
            {
                pos = P_pestana.indexOfTab("Edita Compañia");
                if(pos>=0)
                {
                    P_pestana.setSelectedIndex(pos);
                }
                else
                {
                    ecompania= new editaCompania(actor, t_periodo.getText().toString(), this.sessionPrograma);
                    PanelPestanas btc=new PanelPestanas(P_pestana,-1, actor);
                    P_pestana.addTab("Edita Compañia", ecompania);
                    P_pestana.setSelectedComponent(ecompania);
                    P_pestana.setTabComponentAt(P_pestana.getSelectedIndex(), btc);
                    ecompania.t_compania.requestFocus();
                }
            }
            else
                JOptionPane.showMessageDialog(null, "¡Acceso denegado!");
        }catch(Exception e)
        {
            System.out.println(e);
        }
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jMenuItem8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem8ActionPerformed
        // TODO add your handling code here:
        h.menu=0;
        h.session(sessionPrograma);
        Session session = HibernateUtil.getSessionFactory().openSession();
        try
        {
            session.beginTransaction().begin();
            actor = (Usuario)session.get(Usuario.class, actor.getIdUsuario());
            if(actor.getConsultaCompania()==true)
            {
                buscaCompania obj = new buscaCompania(new javax.swing.JFrame(), true, this.sessionPrograma, this.actor);
                obj.t_busca.requestFocus();
                Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
                obj.setLocation((d.width/2)-(obj.getWidth()/2), (d.height/2)-(obj.getHeight()/2));
                obj.setVisible(true);
                Compania orden_act=obj.getReturnStatus();

                if (orden_act!=null)
                {
                    orden_act=(Compania)session.get(Compania.class, orden_act.getIdCompania());
                    pos=-1;
                    for(int a=0; a<P_pestana.getTabCount(); a++)
                    {
                        if(P_pestana.getTitleAt(a)=="Edita Compañia")
                            pos=a;
                    }
                    if(pos>=0)
                    {
                        P_pestana.setSelectedIndex(pos);
                        ecompania.t_compania.requestFocus();
                    }
                    else
                    {
                        ecompania = new editaCompania(actor, t_periodo.getText().toString(), this.sessionPrograma);
                        PanelPestanas btc=new PanelPestanas(P_pestana,-1, actor);
                        P_pestana.addTab("Edita Compañia", ecompania);
                        P_pestana.setSelectedComponent(ecompania);
                        P_pestana.setTabComponentAt(P_pestana.getSelectedIndex(), btc);
                        ecompania.t_compania.requestFocus();
                    }
                    ecompania.t_compania.setText(""+orden_act.getIdCompania());
                    ecompania.cargaCompania();
                    ecompania.b_busca_orden.requestFocus();
                }
            }
            else
            {
                JOptionPane.showMessageDialog(null, "¡Acceso denegado!");
            }
        }catch(Exception e)
        {
            System.out.println(e);
        }
        if(session!=null)
            if(session.isOpen())
                session.close();
    }//GEN-LAST:event_jMenuItem8ActionPerformed

    private void jMenuItem9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem9ActionPerformed
        // TODO add your handling code here
        h.menu=4;
        h.session(sessionPrograma);
        try
        {
            pos = P_pestana.indexOfTab("A. Valuacion");
            if(pos>=0)
            {
                P_pestana.setSelectedIndex(pos);    
            }
            else
            {
                Modificar_Orden_Valuacion = null;
                Modificar_Orden_Valuacion = new ModificarOrden(actor, t_periodo.getText().toString(), h.menu, sessionPrograma, rutaFTP,configuracion);
                PanelPestanas btc=new PanelPestanas(P_pestana,h.menu,actor);
                P_pestana.addTab("A. Valuacion", Modificar_Orden_Valuacion);
                P_pestana.setSelectedComponent(Modificar_Orden_Valuacion);
                P_pestana.setTabComponentAt(P_pestana.getSelectedIndex(), btc);
                Modificar_Orden_Valuacion.t_orden.requestFocus();
                btc=null;
                Modificar_Orden_Valuacion=null;
            }
        }catch(Exception e)
        {
            System.out.println(e);
        }
    }//GEN-LAST:event_jMenuItem9ActionPerformed

    private void jMenuItem5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem5ActionPerformed
        // TODO add your handling code here:
        try
        {
            if(actor.getEditaUsuario()==true)
            {
                h.menu=0;
                h.session(sessionPrograma);
                pos = P_pestana.indexOfTab("Alta de Usuarios");
                if(pos>=0)
                {
                    P_pestana.setSelectedIndex(pos);    
                    nuevoUsuario.t_usuario.requestFocus();
                }
                else
                {
                    nuevoUsuario = new altaUsuario(actor, t_periodo.getText().toString(), sessionPrograma);
                    PanelPestanas btc=new PanelPestanas(P_pestana,3,actor);
                    P_pestana.addTab("Alta de Usuarios", nuevoUsuario);
                    P_pestana.setSelectedComponent(nuevoUsuario);
                    P_pestana.setTabComponentAt(P_pestana.getSelectedIndex(), btc);
                    nuevoUsuario.t_usuario.requestFocus();
                }
            }
            else
            {
                JOptionPane.showMessageDialog(null, "¡Acceso denegado!");
            }
        }catch(Exception e)
        {
            System.out.println(e);
        }
    }//GEN-LAST:event_jMenuItem5ActionPerformed

    private void jMenuItem12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem12ActionPerformed
        // TODO add your handling code here:
        Session session = HibernateUtil.getSessionFactory().openSession();
        try
        {
            session.beginTransaction().begin();
            actor = (Usuario)session.get(Usuario.class, actor.getIdUsuario());
            if(actor.getEditaUsuario()==true)
            {
                h.menu=0;
                h.session(sessionPrograma);

                buscaUsuarios obj = new buscaUsuarios(new javax.swing.JFrame(), true);
                obj.t_busca.requestFocus();
                Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
                obj.setLocation((d.width/2)-(obj.getWidth()/2), (d.height/2)-(obj.getHeight()/2));
                obj.setVisible(true);
                String no_usr=obj.getReturnStatus();
                if (no_usr!=null)
                {
                    pos = P_pestana.indexOfTab("E. Usuario");
                    if(pos>=0)
                    {
                        P_pestana.setSelectedIndex(pos);
                        editar_usuario.numero_usuario=no_usr;
                    }
                    else
                    {
                        editar_usuario = new editaUsuario(actor, t_periodo.getText().toString(), sessionPrograma, no_usr);
                        PanelPestanas btc=new PanelPestanas(P_pestana,4, actor);
                        P_pestana.addTab("E. Usuario", editar_usuario);
                        P_pestana.setSelectedComponent(editar_usuario);
                        P_pestana.setTabComponentAt(P_pestana.getSelectedIndex(), btc);
                        editar_usuario.t_usuario.requestFocus();
                    }
                    editar_usuario.t_usuario.setText(no_usr);
                    editar_usuario.consulta_usuario();
                    editar_usuario.t_usuario.requestFocus();
                }
            }
            else
            {
                JOptionPane.showMessageDialog(null, "¡Acceso denegado!");
            }
        }catch(Exception e)
        {
            System.out.println(e);
        }
        if(session!=null)
            if(session.isOpen())
                session.close();
    }//GEN-LAST:event_jMenuItem12ActionPerformed

    private void m_consultar_catalogoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_consultar_catalogoActionPerformed
        // TODO add your handling code here:
        h.menu=0;
        h.session(sessionPrograma);
        Session session = HibernateUtil.getSessionFactory().openSession();
        try
        {
            session.beginTransaction().begin();
            actor = (Usuario)session.get(Usuario.class, actor.getIdUsuario());
            if(actor.getConsultaCatalogo()==true)
            {
                buscaCatalogo obj = new buscaCatalogo(new javax.swing.JFrame(), true, sessionPrograma, actor);
                Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
                obj.setLocation((d.width/2)-(obj.getWidth()/2), (d.height/2)-(obj.getHeight()/2));
                obj.setVisible(true);
                Catalogo orden_act=obj.getReturnStatus();
                if (orden_act!=null)
                {
                    orden_act=(Catalogo)session.get(Catalogo.class, orden_act.getIdCatalogo());
                    pos=-1;
                    for(int a=0; a<P_pestana.getTabCount(); a++)
                    {
                        if(P_pestana.getTitleAt(a)=="E. Catalogo")
                        pos=a;
                    }
                    if(pos>=0)
                    {
                        P_pestana.setSelectedIndex(pos);
                    }
                    else
                    {
                        eCatalogo = new editaCatalogo(actor, sessionPrograma);
                        PanelPestanas btc=new PanelPestanas(P_pestana,-1, actor);
                        P_pestana.addTab("E. Catalogo", eCatalogo);
                        P_pestana.setSelectedComponent(eCatalogo);
                        P_pestana.setTabComponentAt(P_pestana.getSelectedIndex(), btc);
                        eCatalogo.t_nombre.requestFocus();
                    }
                    eCatalogo.borra_cajas();
                    eCatalogo.cajas(false, true, true, true, true, true);
                    eCatalogo.t_numero.setText(""+orden_act.getIdCatalogo());
                    eCatalogo.t_nombre.setText(""+orden_act.getNombre());
                    eCatalogo.c_catalogo.setSelectedItem(""+orden_act.getEspecialidad().getIdGrupoMecanico());
                }
            }
            else
            {
                JOptionPane.showMessageDialog(null, "¡Acceso denegado!");
            }
        }catch(Exception e)
        {
            System.out.println(e);
        }
        if(session!=null)
            if(session.isOpen())
                session.close();
    }//GEN-LAST:event_m_consultar_catalogoActionPerformed

    private void m_editar_catalogoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_editar_catalogoActionPerformed
        // TODO add your handling code here:
        h.menu=0;
        h.session(sessionPrograma);
        try
        {
            if(actor.getEditaCatalogo()==true)
            {
                pos = P_pestana.indexOfTab("E. Catalogo");
                if(pos>=0)
                {
                    P_pestana.setSelectedIndex(pos);
                    eCatalogo.t_nombre.requestFocus();
                }
                else
                {
                    eCatalogo = new editaCatalogo(actor,sessionPrograma);
                    PanelPestanas btc=new PanelPestanas(P_pestana,-1, actor);
                    P_pestana.addTab("E. Catalogo", eCatalogo);
                    P_pestana.setSelectedComponent(eCatalogo);
                    P_pestana.setTabComponentAt(P_pestana.getSelectedIndex(), btc);
                    eCatalogo.t_nombre.requestFocus();
                }
            }
            else
            {
                JOptionPane.showMessageDialog(null, "¡Acceso denegado!");
            }
        }catch(Exception e)
        {
            System.out.println(e);
        }
    }//GEN-LAST:event_m_editar_catalogoActionPerformed

    private void m_consulta_clientesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_consulta_clientesActionPerformed
        // TODO add your handling code here:
        h.menu=0;
        h.session(sessionPrograma);
        Session session = HibernateUtil.getSessionFactory().openSession();
        try
        {
            session.beginTransaction().begin();
            actor = (Usuario)session.get(Usuario.class, actor.getIdUsuario());
            if(actor.getCrearClientes()==true || actor.getModificarClientes()==true)
            {
                buscaCliente obj = new buscaCliente(new javax.swing.JFrame(), true);
                //obj.t_busca.requestFocus();
                Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
                obj.setLocation((d.width/2)-(obj.getWidth()/2), (d.height/2)-(obj.getHeight()/2));
                obj.setVisible(true);
                Clientes orden_act=obj.getReturnStatus();

                if (orden_act!=null)
                {
                    orden_act=(Clientes)session.get(Clientes.class, orden_act.getIdClientes());
                    pos=-1;
                    for(int a=0; a<P_pestana.getTabCount(); a++)
                    {
                        if(P_pestana.getTitleAt(a)=="E. Clientes")
                        pos=a;
                    }
                    if(pos>=0)
                    {
                        P_pestana.setSelectedIndex(pos);
                        //alta_cliente.t_compania.requestFocus();
                    }
                    else
                    {
                        eCliente = new editaCliente(actor,sessionPrograma);
                        PanelPestanas btc=new PanelPestanas(P_pestana,-1, actor);
                        P_pestana.addTab("E. Clientes", eCliente);
                        P_pestana.setSelectedComponent(eCliente);
                        P_pestana.setTabComponentAt(P_pestana.getSelectedIndex(), btc);
                        //eCliente.nombre.requestFocus();
                    }
                    eCliente.borra_cajas();
                    eCliente.IdClientes.setText(""+orden_act.getIdClientes());
                    eCliente.nombre.setText(""+orden_act.getNombre());
                    if(orden_act.getDireccion()!=null)
                        eCliente.Direccion.setText(""+orden_act.getDireccion());
                    else
                        eCliente.Direccion.setText("");

                    if(orden_act.getColonia()!=null)
                        eCliente.Colonia.setText(""+orden_act.getColonia());
                    else
                        eCliente.Colonia.setText("");

                    if(orden_act.getCp()!=null)
                        eCliente.Cp.setText(""+orden_act.getCp());
                    else
                        eCliente.Cp.setText("");

                    if(orden_act.getRfc()!=null)
                        eCliente.Rfc.setText(""+orden_act.getRfc());
                    else
                        eCliente.Rfc.setText("");

                    if(orden_act.getPoblacion()!=null)
                        eCliente.Poblacion.setText(""+orden_act.getPoblacion());
                    else
                        eCliente.Poblacion.setText("");

                    eCliente.Estado.setSelectedItem(""+orden_act.getEstado());
                    if(orden_act.getTelefono()!=null)
                        eCliente.Telefono.setText(""+orden_act.getTelefono());
                    else
                        eCliente.Telefono.setText("");

                    if(orden_act.getEmail()!=null)
                        eCliente.Email.setText(""+orden_act.getEmail());
                    else
                        eCliente.Email.setText("");
                    
                    if(orden_act.getEmail()!=null)
                        eCliente.contacto.setText(orden_act.getContacto());
                    else
                        eCliente.contacto.setText("");

                    if(orden_act.getNextel()!=null)
                        eCliente.nextel.setText(orden_act.getNextel());
                    else
                        eCliente.nextel.setText("");
                    
                    if(orden_act.getReceptor()!=null)
                        eCliente.t_receptor.setText(orden_act.getReceptor());
                    else
                        eCliente.t_receptor.setText("");
                    
                    if(orden_act.getEmailReceptor()!=null)
                        eCliente.t_email_receptor.setText(orden_act.getEmailReceptor());
                    else
                        eCliente.t_email_receptor.setText("");
                    eCliente.cajas(true);
                   
                }
            }
            else
            {
                JOptionPane.showMessageDialog(null, "¡Acceso denegado!");
            }
        }catch(Exception e)
        {
            System.out.println(e);
        }
        if(session!=null)
            if(session.isOpen())
                session.close();
    }//GEN-LAST:event_m_consulta_clientesActionPerformed

    private void m_edita_clientesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_edita_clientesActionPerformed
        // TODO add your handling code here
        h.menu=0;
        h.session(sessionPrograma);
        try
        {
            if(actor.getModificarClientes()==true)
            {
                pos = P_pestana.indexOfTab("E. Clientes");
                if(pos>=0)
                {
                    P_pestana.setSelectedIndex(pos);
                }
                else
                {
                    eCliente = new editaCliente(actor, sessionPrograma);
                    PanelPestanas btc=new PanelPestanas(P_pestana,-1, actor);
                    P_pestana.addTab("E. Clientes", eCliente);
                    P_pestana.setSelectedComponent(eCliente);
                    P_pestana.setTabComponentAt(P_pestana.getSelectedIndex(), btc);
                }
            }
            else
            {
                JOptionPane.showMessageDialog(null, "¡Acceso denegado!");
            }
        }catch(Exception e)
        {
            System.out.println(e);
        }
    }//GEN-LAST:event_m_edita_clientesActionPerformed

    private void m_consultar_especialidadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_consultar_especialidadActionPerformed
        // TODO add your handling code here:
        h.menu=0;
        h.session(sessionPrograma);
        Session session = HibernateUtil.getSessionFactory().openSession();
        try
        {
            session.beginTransaction().begin();
            actor = (Usuario)session.get(Usuario.class, actor.getIdUsuario());
            if(actor.getConsultaMecanico()==true)
            {
                buscaEspecialidad obj = new buscaEspecialidad(new javax.swing.JFrame(), true, sessionPrograma, actor);
                //obj.t_busca.requestFocus();
                Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
                obj.setLocation((d.width/2)-(obj.getWidth()/2), (d.height/2)-(obj.getHeight()/2));
                obj.setVisible(true);
                Especialidad orden_act=obj.getReturnStatus();

                if (orden_act!=null)
                {
                    orden_act=(Especialidad)session.get(Especialidad.class, orden_act.getIdGrupoMecanico());
                    pos=-1;
                    for(int a=0; a<P_pestana.getTabCount(); a++)
                    {
                        if(P_pestana.getTitleAt(a)=="E. Especialidad")
                        pos=a;
                    }
                    if(pos>=0)
                    {
                        P_pestana.setSelectedIndex(pos);
                        eEspecialidad.borra_cajas();
                        eEspecialidad.cajas(true, true, true);
                        //ecompania.t_compania.requestFocus();
                    }
                    else
                    {
                        eEspecialidad = new editaEspecialidad(actor, sessionPrograma);
                        PanelPestanas btc=new PanelPestanas(P_pestana,-1, actor);
                        P_pestana.addTab("E. Especialidad", eEspecialidad);
                        P_pestana.setSelectedComponent(eEspecialidad);
                        P_pestana.setTabComponentAt(P_pestana.getSelectedIndex(), btc);
                        eEspecialidad.grupomecanico.requestFocus();
                    }
                    eEspecialidad.borra_cajas();
                    eEspecialidad.cajas(true, true, true);
                    eEspecialidad.IdEspecialidad.setText(""+orden_act.getIdGrupoMecanico());
                    eEspecialidad.grupomecanico.setText(""+orden_act.getDescripcion());
                }
            }
            else
            {
                JOptionPane.showMessageDialog(null, "¡Acceso denegado!");
            }
        }catch(Exception e)
        {
            System.out.println(e);
        }
        if(session!=null)
            if(session.isOpen())
                session.close();
    }//GEN-LAST:event_m_consultar_especialidadActionPerformed

    private void m_editar_especialidadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_editar_especialidadActionPerformed
        // TODO add your handling code here:
        h.menu=0;
        h.session(sessionPrograma);
        try
        {
            if(actor.getEditaMecanico()==true)
            {
                pos = P_pestana.indexOfTab("E. Especialidad");
                if(pos>=0)
                {
                    P_pestana.setSelectedIndex(pos);
                }
                else
                {
                    eEspecialidad = new editaEspecialidad(actor, sessionPrograma);
                    PanelPestanas btc=new PanelPestanas(P_pestana,-1, actor);
                    P_pestana.addTab("E. Especialidad", eEspecialidad);
                    P_pestana.setSelectedComponent(eEspecialidad);
                    P_pestana.setTabComponentAt(P_pestana.getSelectedIndex(), btc);
                }
            }
            else
            {
                JOptionPane.showMessageDialog(null, "¡Acceso denegado!");
            }
        }catch(Exception e)
        {
            System.out.println(e);
        }
    }//GEN-LAST:event_m_editar_especialidadActionPerformed

    private void m_consultar_estatusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_consultar_estatusActionPerformed
        // TODO add your handling code here:
        h.menu=0;
        h.session(sessionPrograma);
        Session session = HibernateUtil.getSessionFactory().openSession();
        try
        {
            session.beginTransaction().begin();
            actor = (Usuario)session.get(Usuario.class, actor.getIdUsuario());
            if(actor.getConsultaEstatus()==true)
            {
                buscaEstatus obj = new buscaEstatus(new javax.swing.JFrame(), true, sessionPrograma, actor);
                //obj.t_busca.requestFocus();
                Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
                obj.setLocation((d.width/2)-(obj.getWidth()/2), (d.height/2)-(obj.getHeight()/2));
                obj.setVisible(true);
                Estatus orden_act=obj.getReturnStatus();
                if (orden_act!=null)
                {
                    orden_act=(Estatus)session.get(Estatus.class, orden_act.getEstatusNombre());
                    pos=-1;
                    for(int a=0; a<P_pestana.getTabCount(); a++)
                    {
                        if(P_pestana.getTitleAt(a)=="Edita Estatus")
                        pos=a;
                    }
                    if(pos>=0)
                    {
                        P_pestana.setSelectedIndex(pos);
                        //eEstatus.t_compania.requestFocus();
                    }
                    else
                    {
                        eEstatus = new editaEstatus(actor, sessionPrograma);

                        PanelPestanas btc=new PanelPestanas(P_pestana,-1, actor);
                        P_pestana.addTab("Edita Estatus", eEstatus);
                        P_pestana.setSelectedComponent(eEstatus);
                        P_pestana.setTabComponentAt(P_pestana.getSelectedIndex(), btc);
                        eEstatus.nombre.requestFocus();
                    }
                    eEstatus.borra_cajas();
                    if(actor.getEditaEstatus()==true)
                        eEstatus.cajas(true, true, true, true);
                    else
                        eEstatus.cajas(false, false, false, false);
                    eEstatus.nombre.setText(""+orden_act.getEstatusNombre());
                    eEstatus.descripcion.setText(""+orden_act.getDescripcion());
                    eEstatus.nb=""+orden_act.getEstatusNombre();
                    eEstatus.ds=""+orden_act.getDescripcion();
                }
            }
            else
            {
                JOptionPane.showMessageDialog(null, "¡Acceso denegado!");
            }
        }catch(Exception e)
        {
            System.out.println(e);
        }
        if(session!=null)
            if(session.isOpen())
                session.close();
    }//GEN-LAST:event_m_consultar_estatusActionPerformed

    private void m_editar_estatusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_editar_estatusActionPerformed
        // TODO add your handling code here:
        h.menu=0;
        h.session(sessionPrograma);
        try
        {
            if(actor.getEditaEstatus()==true)
            {
                pos = P_pestana.indexOfTab("E. Estatus");
                if(pos>=0)
                {
                    P_pestana.setSelectedIndex(pos);
                    eEstatus.nombre.requestFocus();
                }
                else
                {
                    eEstatus = new editaEstatus(actor,sessionPrograma);
                    PanelPestanas btc=new PanelPestanas(P_pestana,-1, actor);
                    P_pestana.addTab("E. Estatus", eEstatus);
                    P_pestana.setSelectedComponent(eEstatus);
                    P_pestana.setTabComponentAt(P_pestana.getSelectedIndex(), btc);
                    eEstatus.nombre.requestFocus();
                }
            }
            else
            {
                JOptionPane.showMessageDialog(null, "¡Acceso denegado!");
            }
        }catch(Exception e)
        {
            System.out.println(e);
        }
    }//GEN-LAST:event_m_editar_estatusActionPerformed

    private void m_editar_marcaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_editar_marcaActionPerformed
        // TODO add your handling code here:
        h.menu=0;
        h.session(sessionPrograma);
        try
        {
            if(actor.getEditaMarca()==true)
            {
                pos = P_pestana.indexOfTab("E. Marca");
                if(pos>=0)
                    P_pestana.setSelectedIndex(pos);
                else
                {
                    eMarca = new editaMarca(actor,sessionPrograma );
                    PanelPestanas btc=new PanelPestanas(P_pestana,-1, actor);
                    P_pestana.addTab("E. Marca", eMarca);
                    P_pestana.setSelectedComponent(eMarca);
                    P_pestana.setTabComponentAt(P_pestana.getSelectedIndex(), btc);
                }
            }
            else
                JOptionPane.showMessageDialog(null, "¡Acceso denegado!");
        }catch(Exception e)
        {
            System.out.println(e);
        }
    }//GEN-LAST:event_m_editar_marcaActionPerformed

    private void m_consultar_puestosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_consultar_puestosActionPerformed
        // TODO add your handling code here:
        h.menu=0;
        h.session(sessionPrograma);
        Session session = HibernateUtil.getSessionFactory().openSession();
        try
        {
            session.beginTransaction().begin();
            actor = (Usuario)session.get(Usuario.class, actor.getIdUsuario());
            if(actor.getConsultaPuestos()==true || actor.getEditaPuestos()==true)
            {
                buscaPuestos obj = new buscaPuestos(new javax.swing.JFrame(), true, sessionPrograma, actor);
                Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
                obj.setLocation((d.width/2)-(obj.getWidth()/2), (d.height/2)-(obj.getHeight()/2));
                obj.setVisible(true);
                Puestos orden_act=obj.getReturnStatus();

                if (orden_act!=null)
                {
                    orden_act=(Puestos)session.get(Puestos.class, orden_act.getIdPuestos());
                    pos=-1;
                    for(int a=0; a<P_pestana.getTabCount(); a++)
                    {
                        if(P_pestana.getTitleAt(a)=="E. Puestos")
                        pos=a;
                    }
                    if(pos>=0)
                        P_pestana.setSelectedIndex(pos);
                    else
                    {
                        ePuestos = new editaPuestos(actor, sessionPrograma);
                        PanelPestanas btc=new PanelPestanas(P_pestana,-1, actor);
                        P_pestana.addTab("E. Puestos", ePuestos);
                        P_pestana.setSelectedComponent(ePuestos);
                        P_pestana.setTabComponentAt(P_pestana.getSelectedIndex(), btc);
                        ePuestos.Nombre.requestFocus();
                    }
                    ePuestos.borra_cajas();
                    if(actor.getEditaPuestos()==true)
                        ePuestos.cajas(true, true, true, true);
                    else
                        ePuestos.cajas(false, false, false, false);
                    ePuestos.IdPuestos.setText(""+orden_act.getIdPuestos());
                    ePuestos.Nombre.setText(""+orden_act.getNombre());
                    ePuestos.Descripcion.setText(""+orden_act.getDescripcion());
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
                session.close();
    }//GEN-LAST:event_m_consultar_puestosActionPerformed

    private void m_editar_puestosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_editar_puestosActionPerformed
        // TODO add your handling code here:
        h.menu=0;
        h.session(sessionPrograma);
        try
        {
            if(actor.getEditaPuestos()==true || actor.getConsultaPuestos()==true)
            {
                pos = P_pestana.indexOfTab("E. Puestos");
                if(pos>=0)
                {
                    P_pestana.setSelectedIndex(pos);
                    ePuestos.Nombre.requestFocus();
                }
                else
                {
                    ePuestos = new editaPuestos(actor, sessionPrograma);
                    PanelPestanas btc=new PanelPestanas(P_pestana,-1, actor);
                    P_pestana.addTab("E. Puestos", ePuestos);
                    P_pestana.setSelectedComponent(ePuestos);
                    P_pestana.setTabComponentAt(P_pestana.getSelectedIndex(), btc);
                    ePuestos.Nombre.requestFocus();
                }
            }
            else
                JOptionPane.showMessageDialog(null, "¡Acceso denegado!");
        }catch(Exception e)
        {
            System.out.println(e);
        }
    }//GEN-LAST:event_m_editar_puestosActionPerformed

    private void m_consultar_reparacionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_consultar_reparacionActionPerformed
        // TODO add your handling code here:
        h.menu=0;
        h.session(sessionPrograma);
        Session session = HibernateUtil.getSessionFactory().openSession();
        try
        {
            session.beginTransaction().begin();
            actor = (Usuario)session.get(Usuario.class, actor.getIdUsuario());
            if(actor.getConsultaReparacion()==true)
            {
                buscaReparacion obj = new buscaReparacion(new javax.swing.JFrame(), true, sessionPrograma, actor);
                Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
                obj.setLocation((d.width/2)-(obj.getWidth()/2), (d.height/2)-(obj.getHeight()/2));
                obj.setVisible(true);
                Reparacion orden_act=obj.getReturnStatus();

                if (orden_act!=null)
                {
                    orden_act=(Reparacion)session.get(Reparacion.class, orden_act.getIdReparacion());
                    pos = P_pestana.indexOfTab("E. Reparación");
                    if(pos>=0)
                    {
                        P_pestana.setSelectedIndex(pos);
                        //ePuestos.t_compania.requestFocus();
                    }
                    else
                    {
                        eReparacion = new editaReparacion(actor,sessionPrograma);
                        PanelPestanas btc=new PanelPestanas(P_pestana,-1, actor);
                        P_pestana.addTab("E. Reparación", eReparacion);
                        P_pestana.setSelectedComponent(eReparacion);
                        P_pestana.setTabComponentAt(P_pestana.getSelectedIndex(), btc);
                        eReparacion.Nombre.requestFocus();
                    }
                    eReparacion.cajas(true, true, true);
                    eReparacion.borra_cajas();
                    eReparacion.IdReparacion.setText(""+orden_act.getIdReparacion());
                    eReparacion.Nombre.setText(""+orden_act.getNombre());
                }
            }
            else
            {
                JOptionPane.showMessageDialog(null, "¡Acceso denegado!");
            }
        }catch(Exception e)
        {
            System.out.println(e);
        }
        if(session!=null)
            if(session.isOpen())
                session.close();
    }//GEN-LAST:event_m_consultar_reparacionActionPerformed

    private void m_editar_reparacionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_editar_reparacionActionPerformed
        // TODO add your handling code here:
        h.menu=0;
        h.session(sessionPrograma);
        try
        {
            if(actor.getEditaReparacion()==true)
            {
                pos = P_pestana.indexOfTab("E. Reparación");
                if(pos>=0)
                {
                    P_pestana.setSelectedIndex(pos);
                }
                else
                {
                    eReparacion = new editaReparacion(actor, sessionPrograma);
                    PanelPestanas btc=new PanelPestanas(P_pestana,-1, actor);
                    P_pestana.addTab("E. Reparación", eReparacion);
                    P_pestana.setSelectedComponent(eReparacion);
                    P_pestana.setTabComponentAt(P_pestana.getSelectedIndex(), btc);
                }
            }
            else
            {
                JOptionPane.showMessageDialog(null, "¡Acceso denegado!");
            }
        }catch(Exception e)
        {
            System.out.println(e);
        }
    }//GEN-LAST:event_m_editar_reparacionActionPerformed

    private void m_consultar_tipoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_consultar_tipoActionPerformed
        // TODO add your handling code here:
        h.menu=0;
        h.session(sessionPrograma);
        Session session = HibernateUtil.getSessionFactory().openSession();
        try
        {
            session.beginTransaction().begin();
            actor = (Usuario)session.get(Usuario.class, actor.getIdUsuario());
            if(actor.getConsultaTipo()==true)
            {
                buscaTipo obj = new buscaTipo(new javax.swing.JFrame(), true,sessionPrograma,actor);
                Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
                obj.setLocation((d.width/2)-(obj.getWidth()/2), (d.height/2)-(obj.getHeight()/2));
                obj.setVisible(true);
                String orden_act=obj.getReturnStatus();
                if (orden_act!=null)
                {
                    pos=-1;
                    for(int a=0; a<P_pestana.getTabCount(); a++)
                    {
                        if(P_pestana.getTitleAt(a)=="E. Tipo")
                        pos=a;
                    }
                    if(pos>=0)
                    {
                        P_pestana.setSelectedIndex(pos);
                    }
                    else
                    {
                        eTipo = new editaTipo(actor, sessionPrograma);
                        PanelPestanas btc=new PanelPestanas(P_pestana,-1, actor);
                        P_pestana.addTab("E. Tipo", eTipo);
                        P_pestana.setSelectedComponent(eTipo);
                        P_pestana.setTabComponentAt(P_pestana.getSelectedIndex(), btc);
                        eTipo.Nombre.requestFocus();
                    }
                    Tipo tip=(Tipo)session.get(Tipo.class, orden_act);
                    eTipo.cajas(true, true, true, true);
                    eTipo.borra_cajas();
                    eTipo.Nombre.setText(""+tip.getTipoNombre());
                    eTipo.ic=""+tip.getTipoNombre();
                    if(tip.getEPesado()==1)
                        eTipo.cb_ep.setSelected(true);
                    else
                        eTipo.cb_ep.setSelected(false);
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
                session.close();
    }//GEN-LAST:event_m_consultar_tipoActionPerformed

    private void jMenuItem13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem13ActionPerformed
        // TODO add your handling code here:
        h.menu=11;
        h.session(sessionPrograma);
        Session session = HibernateUtil.getSessionFactory().openSession();
        try
        {
            session.beginTransaction().begin();
            actor = (Usuario)session.get(Usuario.class, actor.getIdUsuario());
            if(actor.getEditaPorcentaje()==true || actor.getEditaPrecioCu()==true)
            { 
                pos = P_pestana.indexOfTab("Ref. cotiza");
                if(pos>=0)
                {
                    P_pestana.setSelectedIndex(pos);    
                    Modificar_Orden_Valuacion.t_orden.requestFocus();
                }
                else
                {
                    Modificar_Orden_Valuacion = new ModificarOrden(actor, t_periodo.getText().toString(), h.menu, sessionPrograma,rutaFTP,configuracion);
                    PanelPestanas btc=new PanelPestanas(P_pestana,h.menu,actor);
                    P_pestana.addTab("Ref. cotiza", Modificar_Orden_Valuacion);
                    P_pestana.setSelectedComponent(Modificar_Orden_Valuacion);
                    P_pestana.setTabComponentAt(P_pestana.getSelectedIndex(), btc);
                    Modificar_Orden_Valuacion.t_orden.requestFocus();
                }
            }
            else
            {
                JOptionPane.showMessageDialog(null, "¡Acceso denegado!");
            }
        }catch(Exception e)
        {
            System.out.println(e);
        }
        if(session!=null)
            if(session.isOpen())
                session.close();
    }//GEN-LAST:event_jMenuItem13ActionPerformed

    private void jMenuItem10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem10ActionPerformed
        // TODO add your handling code here:
        h.menu=5;
        h.session(sessionPrograma);
        try
        {
            if(actor.getEditaAutorizaPartida()==true)
            { 
                pos = P_pestana.indexOfTab("Autorización");
                if(pos>=0)
                {
                    P_pestana.setSelectedIndex(pos);    
                    Modificar_Orden_Valuacion.t_orden.requestFocus();
                }
                else
                {
                    Modificar_Orden_Valuacion = new ModificarOrden(actor, t_periodo.getText().toString(), h.menu, sessionPrograma,rutaFTP,configuracion);
                    PanelPestanas btc=new PanelPestanas(P_pestana,h.menu,actor);
                    P_pestana.addTab("Autorización", Modificar_Orden_Valuacion);
                    P_pestana.setSelectedComponent(Modificar_Orden_Valuacion);
                    P_pestana.setTabComponentAt(P_pestana.getSelectedIndex(), btc);
                    Modificar_Orden_Valuacion.t_orden.requestFocus();
                }
            }
            else
            {
                JOptionPane.showMessageDialog(null, "¡Acceso denegado!");
            }
        }catch(Exception e)
        {
            System.out.println(e);
        }
    }//GEN-LAST:event_jMenuItem10ActionPerformed

    private void jMenuItem11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem11ActionPerformed
        // TODO add your handling code here:
        h.menu=7;
        h.session(sessionPrograma);
        try
        {
            if(actor.getReportes()==true)
            { 
                pos = P_pestana.indexOfTab("Reportes Valuacion");
                if(pos>=0)
                    P_pestana.setSelectedIndex(pos);
                else
                {
                    reporteVal = new Reportes(actor, sessionPrograma, periodo, P_pestana, Modificar_Orden, ruta,configuracion);
                    PanelPestanas btc=new PanelPestanas(P_pestana,h.menu,actor);
                    P_pestana.addTab("Reportes Valuacion", reporteVal);
                    P_pestana.setSelectedComponent(reporteVal);
                    P_pestana.setTabComponentAt(P_pestana.getSelectedIndex(), btc);
                }
            }
            else
            {
                JOptionPane.showMessageDialog(null, "¡Acceso denegado!");
            }
        }catch(Exception e)
        {
            System.out.println(e);
        }
    }//GEN-LAST:event_jMenuItem11ActionPerformed

    private void nuevo_proveedorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nuevo_proveedorActionPerformed
        // TODO add your handling code here:
        h.menu=0;
        h.session(sessionPrograma);
        try
        {
            if(actor.getEditarProveedores()==true)
            {
                pos = P_pestana.indexOfTab("Nuevo Proveedor");
                if(pos>=0)
                {
                    P_pestana.setSelectedIndex(pos);
                }
                else
                {
                    nuevoProveedor nproveedor= new nuevoProveedor(actor, t_periodo.getText().toString(), sessionPrograma);
                    PanelPestanas btc=new PanelPestanas(P_pestana,-1, actor);
                    P_pestana.addTab("Nuevo Proveedor", nproveedor);
                    P_pestana.setSelectedComponent(nproveedor);
                    P_pestana.setTabComponentAt(P_pestana.getSelectedIndex(), btc);
                }
            }
            else
                JOptionPane.showMessageDialog(null, "¡Acceso denegado!");
        }catch(Exception e)
        {
            System.out.println(e);
        }
    }//GEN-LAST:event_nuevo_proveedorActionPerformed

    private void busca_proveedorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_busca_proveedorActionPerformed
        // TODO add your handling code here:
        h.menu=0;
        h.session(sessionPrograma);
        Session session = HibernateUtil.getSessionFactory().openSession();
        try
        {
            session.beginTransaction().begin();
            actor = (Usuario)session.get(Usuario.class, actor.getIdUsuario());
            if(actor.getConsultarProveedores()==true)
            {
                buscaProveedor obj = new buscaProveedor(new javax.swing.JFrame(), true, this.actor, this.sessionPrograma,0);
                obj.t_busca.requestFocus();
                Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
                obj.setLocation((d.width/2)-(obj.getWidth()/2), (d.height/2)-(obj.getHeight()/2));
                obj.setVisible(true);
                Proveedor orden_act=obj.getReturnStatus();

                if (orden_act!=null)
                {
                    orden_act=(Proveedor)session.get(Proveedor.class, orden_act.getIdProveedor());
                    pos=-1;
                    for(int a=0; a<P_pestana.getTabCount(); a++)
                    {
                        if(P_pestana.getTitleAt(a)=="Edita Proveedor")
                        pos=a;
                    }
                    if(pos>=0)
                    {
                        P_pestana.setSelectedIndex(pos);
                        eproveedor.t_proveedor.requestFocus();
                    }
                    else
                    {
                        eproveedor = new editaProveedor(actor, t_periodo.getText().toString(),sessionPrograma);
                        PanelPestanas btc=new PanelPestanas(P_pestana,-1, actor);
                        P_pestana.addTab("Edita Proveedor", eproveedor);
                        P_pestana.setSelectedComponent(eproveedor);
                        P_pestana.setTabComponentAt(P_pestana.getSelectedIndex(), btc);
                        eproveedor.t_proveedor.requestFocus();
                    }
                    eproveedor.t_proveedor.setText(""+orden_act.getIdProveedor());
                    eproveedor.cargaProveedor();
                    eproveedor.b_busca.requestFocus();
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
                session.close();
    }//GEN-LAST:event_busca_proveedorActionPerformed

    private void edita_proveedorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_edita_proveedorActionPerformed
        // TODO add your handling code here:
        h.menu=0;
        h.session(sessionPrograma);
        try
        {
            if(actor.getEditarProveedores()==true)
            {
                pos = P_pestana.indexOfTab("Edita Proveedor");
                if(pos>=0)
                {
                    P_pestana.setSelectedIndex(pos);
                }
                else
                {
                    eproveedor= new editaProveedor(actor, t_periodo.getText().toString(), sessionPrograma);
                    PanelPestanas btc=new PanelPestanas(P_pestana,-1, actor);
                    P_pestana.addTab("Edita Proveedor", eproveedor);
                    P_pestana.setSelectedComponent(eproveedor);
                    P_pestana.setTabComponentAt(P_pestana.getSelectedIndex(), btc);
                    eproveedor.t_proveedor.requestFocus();
                }
            }
            else
                JOptionPane.showMessageDialog(null, "¡Acceso denegado!");
        }catch(Exception e)
        {
            System.out.println(e);
        }
    }//GEN-LAST:event_edita_proveedorActionPerformed

    private void m_consultar_articuloActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_consultar_articuloActionPerformed
        // TODO add your handling code here:
        h.menu=0;
        h.session(sessionPrograma);
        Session session = HibernateUtil.getSessionFactory().openSession();
        try
        {
            session.beginTransaction().begin();
            actor = (Usuario)session.get(Usuario.class, actor.getIdUsuario());
            if(actor.getConsultarEjemplar()==true)
             {
                buscaEjemplar obj = new buscaEjemplar(new javax.swing.JFrame(), true, sessionPrograma, actor, 0);
                obj.t_busca.requestFocus();
                Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
                obj.setLocation((d.width/2)-(obj.getWidth()/2), (d.height/2)-(obj.getHeight()/2));
                obj.setVisible(true);
                Ejemplar orden_act=obj.getReturnStatus();

                if (orden_act!=null)
                {
                    orden_act=(Ejemplar)session.get(Ejemplar.class, orden_act.getIdParte());
                    pos=-1;
                    for(int a=0; a<P_pestana.getTabCount(); a++)
                    {
                        if(P_pestana.getTitleAt(a)=="Edita Ejemplar")
                        pos=a;
                    }
                    if(pos>=0)
                    {
                        P_pestana.setSelectedIndex(pos);
                    }
                    else
                    {
                        eEjemplar = new editaEjemplar(actor, sessionPrograma, 0, configuracion);
                        PanelPestanas btc=new PanelPestanas(P_pestana,-1, actor);
                        P_pestana.addTab("Edita Ejemplar", eEjemplar);
                        P_pestana.setSelectedComponent(eEjemplar);
                        P_pestana.setTabComponentAt(P_pestana.getSelectedIndex(), btc);
                        eEjemplar.t_numero.requestFocus();
                    }
                    eEjemplar.borra_cajas();
                    eEjemplar.habilita_cajas(true);
                    eEjemplar.t_numero.setText(orden_act.getIdParte());
                    eEjemplar.NS=""+orden_act.getIdParte();
                    if(orden_act.getMarca()!=null)
                        eEjemplar.c_marca.setSelectedItem(""+orden_act.getMarca().getIdMarca());
                    if(orden_act.getGrupo()!=null)
                        eEjemplar.c_grupo.setSelectedItem(""+orden_act.getGrupo().getDescripcion());
                    if(orden_act.getCatalogo()!=null)
                    {
                        eEjemplar.t_id_catalogo.setText(""+orden_act.getCatalogo().getIdCatalogo());
                        eEjemplar.t_catalogo.setText(orden_act.getCatalogo().getNombre());
                    }
                    else
                    {
                        eEjemplar.t_id_catalogo.setText("");
                        eEjemplar.t_catalogo.setText("");
                    }
                    if(orden_act.getComentario()!=null)
                        eEjemplar.t_comentario.setText(orden_act.getComentario());
                    else
                        eEjemplar.t_comentario.setText("");
                    eEjemplar.medida.setSelectedItem(orden_act.getMedida());
                    eEjemplar.t_precio.setText(orden_act.getPrecio().toString());
                    eEjemplar.t_maximo.setText(orden_act.getMaximo().toString());
                    eEjemplar.t_minimo.setText(orden_act.getMinimo().toString());
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
                session.close();
    }//GEN-LAST:event_m_consultar_articuloActionPerformed

    private void m_editar_articuloActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_editar_articuloActionPerformed
        // TODO add your handling code here:
        h.menu=0;
        h.session(sessionPrograma);
        try
        {
            if(actor.getEditarEjemplar()==true)
            {
                pos=-1;
                for(int a=0; a<P_pestana.getTabCount(); a++)
                {
                    if(P_pestana.getTitleAt(a)=="Edita Ejemplar")
                    pos=a;
                }
                if(pos>=0)
                {
                    P_pestana.setSelectedIndex(pos);
                    eEjemplar.t_numero.requestFocus();
                }
                else
                {
                    eEjemplar = new editaEjemplar(actor,sessionPrograma, 0, configuracion);
                    PanelPestanas btc=new PanelPestanas(P_pestana,-1, actor);
                    P_pestana.addTab("Edita Ejemplar", eEjemplar);
                    P_pestana.setSelectedComponent(eEjemplar);
                    P_pestana.setTabComponentAt(P_pestana.getSelectedIndex(), btc);
                    eEjemplar.t_numero.requestFocus();
                }
            }
            else
                JOptionPane.showMessageDialog(null, "¡Acceso denegado!");
        }catch(Exception e)
        {
            System.out.println(e);
        }
    }//GEN-LAST:event_m_editar_articuloActionPerformed

    private void jMenuItem14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem14ActionPerformed
        // TODO add your handling code here:
        h.menu=12;
        h.session(sessionPrograma);
        Session session = HibernateUtil.getSessionFactory().openSession();
        try
        {
            session.beginTransaction().begin();
            actor = (Usuario)session.get(Usuario.class, actor.getIdUsuario());
            if(actor.getEditarCotizaciones()==true)
            { 
                pos = P_pestana.indexOfTab("Genera Cotizaciones");
                if(pos>=0)
                {
                    P_pestana.setSelectedIndex(pos);    
                    generarCotizacion.t_orden.requestFocus();
                }
                else
                {
                    generarCotizacion = new ModificarOrden(actor, t_periodo.getText().toString(), h.menu, sessionPrograma,rutaFTP,configuracion);
                    PanelPestanas btc=new PanelPestanas(P_pestana, h.menu, actor);
                    P_pestana.addTab("Genera Cotizaciones", generarCotizacion);
                    P_pestana.setSelectedComponent(generarCotizacion);
                    P_pestana.setTabComponentAt(P_pestana.getSelectedIndex(), btc);
                    generarCotizacion.t_orden.requestFocus();
                }
            }
            else
            {
                JOptionPane.showMessageDialog(null, "¡Acceso denegado!");
            }
        }catch(Exception e)
        {
            System.out.println(e);
        }
        if(session!=null)
            if(session.isOpen())
                session.close();
    }//GEN-LAST:event_jMenuItem14ActionPerformed

    private void jMenuItem15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem15ActionPerformed
        // TODO add your handling code here:
        h.menu=0;
        h.session(sessionPrograma);        
        try
        {
            pos = P_pestana.indexOfTab("Correo");
            if(pos>=0)
                P_pestana.setSelectedIndex(pos);    
            else
            {
                bandejaSalida bandeja= new bandejaSalida(this.actor, this.sessionPrograma);
                PanelPestanas btc=new PanelPestanas(P_pestana,3,actor);
                P_pestana.addTab("Correo", bandeja);
                P_pestana.setSelectedComponent(bandeja);
                P_pestana.setTabComponentAt(P_pestana.getSelectedIndex(), btc);
            }
        }catch(Exception e)
        {
            System.out.println(e);
        }
    }//GEN-LAST:event_jMenuItem15ActionPerformed

    private void jMenuItem16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem16ActionPerformed
        // TODO add your handling code here:
        h.menu=14;
        h.session(sessionPrograma);
        try
        {
            if(actor.getEditaPedidos()==true || actor.getEliminaPedidos()==true || actor.getGeneraPedidos())
            {
                pos = P_pestana.indexOfTab("Genera Pedidos");
                if(pos>=0)
                {
                    P_pestana.setSelectedIndex(pos);
                    generarPedidos.t_orden.requestFocus();
                }
                else
                {
                    generarPedidos = new ModificarOrden(actor, t_periodo.getText().toString(), h.menu, sessionPrograma,rutaFTP,configuracion);
                    PanelPestanas btc=new PanelPestanas(P_pestana, h.menu, actor);
                    P_pestana.addTab("Genera Pedidos", generarPedidos);
                    P_pestana.setSelectedComponent(generarPedidos);
                    P_pestana.setTabComponentAt(P_pestana.getSelectedIndex(), btc);
                    generarPedidos.t_orden.requestFocus();
                }
            }
            else
            {
                JOptionPane.showMessageDialog(null, "¡Acceso denegado!");
            }
        }catch(Exception e)
        {
            System.out.println(e);
        }
    }//GEN-LAST:event_jMenuItem16ActionPerformed

    private void jMenuItem18ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem18ActionPerformed
        // TODO add your handling code here:
        h.menu=15;
        h.session(sessionPrograma);
        try
        {
            if(actor.getConsultaPedidos()==true)
            {
                pos = P_pestana.indexOfTab("Avance de Pedidos");
                if(pos>=0)
                {
                    P_pestana.setSelectedIndex(pos);    
                    avancePedidos.t_orden.requestFocus();
                }
                else
                {
                    avancePedidos = new ModificarOrden(actor, t_periodo.getText().toString(), h.menu, sessionPrograma,rutaFTP,configuracion);
                    PanelPestanas btc=new PanelPestanas(P_pestana, h.menu, actor);
                    P_pestana.addTab("Avance de Pedidos", avancePedidos);
                    P_pestana.setSelectedComponent(avancePedidos);
                    P_pestana.setTabComponentAt(P_pestana.getSelectedIndex(), btc);
                    avancePedidos.t_orden.requestFocus();
                }
            }
            else
                JOptionPane.showMessageDialog(null, "¡Acceso denegado!");
        }catch(Exception e)
        {
            System.out.println(e);
        }
    }//GEN-LAST:event_jMenuItem18ActionPerformed

    private void jMenuItem17ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem17ActionPerformed
        // TODO add your handling code here:
        h.menu=19;
        h.session(sessionPrograma);
        try
        {
            if(actor.getReportesPedidos()==true)
            {
                pos = P_pestana.indexOfTab("Reportes Compras");
                if(pos>=0)
                {
                    P_pestana.setSelectedIndex(pos);    
                }
                else
                {
                    reporteCom = new reportePedidos(actor, sessionPrograma,configuracion);
                    PanelPestanas btc=new PanelPestanas(P_pestana,h.menu,actor);
                    P_pestana.addTab("Reportes Compras", reporteCom);
                    P_pestana.setSelectedComponent(reporteCom);
                    P_pestana.setTabComponentAt(P_pestana.getSelectedIndex(), btc);
                }
            }
            else
                JOptionPane.showMessageDialog(null, "¡Acceso denegado!");
        }catch(Exception e)
        {
            System.out.println(e);
        }
    }//GEN-LAST:event_jMenuItem17ActionPerformed

    private void jMenuItem23ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem23ActionPerformed
        // TODO add your handling code here:
        h.menu=18;
        h.session(sessionPrograma);
        try
        {
            if(actor.getAutorizarPedidos()==true)
            {
                    pos = P_pestana.indexOfTab("Autorizar Pedidos");
                    if(pos>=0)
                    {
                        P_pestana.setSelectedIndex(pos);    
                    }
                    else
                    {
                        autoriza_pedidos = new consultaPedido(actor, sessionPrograma, null, h.menu,configuracion, rutaFTP);
                        PanelPestanas btc=new PanelPestanas(P_pestana,h.menu,actor);
                        P_pestana.addTab("Autorizar Pedidos", autoriza_pedidos);
                        P_pestana.setSelectedComponent(autoriza_pedidos);
                        P_pestana.setTabComponentAt(P_pestana.getSelectedIndex(), btc);
                    }
            }
            else
                JOptionPane.showMessageDialog(null, "¡Acceso denegado!");    
        }catch(Exception e)
        {
            System.out.println(e);
        }
    }//GEN-LAST:event_jMenuItem23ActionPerformed

    private void jMenuItem24ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem24ActionPerformed
        // TODO add your handling code here:
        h.menu=0;
        h.session(sessionPrograma);
        try
        {
            if(actor.getConfiguracion()==true)
            {
                pos = P_pestana.indexOfTab("Configuración");
                if(pos>=0)
                {
                    P_pestana.setSelectedIndex(pos);
                }
                else
                {
                    this.panel_configuracion = new configuracion(actor, this.sessionPrograma, l_nombre, p_logo);
                    PanelPestanas btc=new PanelPestanas(P_pestana,-1, actor);
                    P_pestana.addTab("Configuración", panel_configuracion);
                    P_pestana.setSelectedComponent(panel_configuracion);
                    P_pestana.setTabComponentAt(P_pestana.getSelectedIndex(), btc);
                }
            }
            else
            {
                JOptionPane.showMessageDialog(null, "¡Acceso denegado!");
            }
        }catch(Exception e)
        {
            System.out.println(e);
        }
    }//GEN-LAST:event_jMenuItem24ActionPerformed

    private void jMenuItem34ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem34ActionPerformed
        // TODO add your handling code here:
        h.menu=13;
        h.session(sessionPrograma);
        try
        {
            if(actor.getGeneraPedidos()==true)
            {
                pos = P_pestana.indexOfTab("Nuevo Pedido");
                if(pos>=0)
                    P_pestana.setSelectedIndex(pos);
                else
                {
                    npedido= new nuevoPedido(t_periodo.getText().toString(),actor, sessionPrograma, h.menu,configuracion, rutaFTP);
                    PanelPestanas btc=new PanelPestanas(P_pestana,h.menu, actor);
                    P_pestana.addTab("Nuevo Pedido", npedido);
                    P_pestana.setSelectedComponent(npedido);
                    P_pestana.setTabComponentAt(P_pestana.getSelectedIndex(), btc);
                }
            }
            else
            {
                JOptionPane.showMessageDialog(null, "¡Acceso denegado!");
            }
        }catch(Exception e)
        {
            System.out.println(e);
        }
    }//GEN-LAST:event_jMenuItem34ActionPerformed

    private void jMenuItem25ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem25ActionPerformed
        h.menu=16;
        h.session(sessionPrograma);
        try
        {
            if(actor.getEditaPedidos()==true)
            {
                h.session(sessionPrograma);
                pos = P_pestana.indexOfTab("Modificar Pedido");
                if(pos>=0)
                {
                    P_pestana.setSelectedIndex(pos);    
                }
                else
                {
                    edita_pedidos = new editaPedido(actor, sessionPrograma, null, h.menu,configuracion, rutaFTP);
                    PanelPestanas btc=new PanelPestanas(P_pestana,h.menu,actor);
                    P_pestana.addTab("Modificar Pedido", edita_pedidos);
                    P_pestana.setSelectedComponent(edita_pedidos);
                    P_pestana.setTabComponentAt(P_pestana.getSelectedIndex(), btc);
                }
            }
            else
                JOptionPane.showMessageDialog(null, "¡Acceso denegado!");    
        }catch(Exception e)
        {
            System.out.println(e);
        }
    }//GEN-LAST:event_jMenuItem25ActionPerformed

    private void jMenuItem21ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem21ActionPerformed
        // TODO add your handling code here:
        h.menu=21;
        h.session(sessionPrograma);
        try
        {
            if(actor.getConsultaMovimientoAlmacen()==true)
            {
                pos = P_pestana.indexOfTab("Consulta Almacen");
                if(pos>=0)
                {
                    P_pestana.setSelectedIndex(pos);    
                }
                else
                {
                    muestra_almacen = new muestraAlmacen(actor, sessionPrograma, null,configuracion);
                    PanelPestanas btc=new PanelPestanas(P_pestana,h.menu,actor);
                    P_pestana.addTab("Consulta Almacen", muestra_almacen);
                    P_pestana.setSelectedComponent(muestra_almacen);
                    P_pestana.setTabComponentAt(P_pestana.getSelectedIndex(), btc);
                }
                System.gc();
            }
            else
                JOptionPane.showMessageDialog(null, "¡Acceso denegado!");    
        }catch(Exception e)
        {
            System.out.println(e);
            e.printStackTrace();
        }
    }//GEN-LAST:event_jMenuItem21ActionPerformed

    private void jMenuItem20ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem20ActionPerformed
        // TODO add your handling code here:
        h.menu=6;
        h.session(sessionPrograma);
        try
        {
            if(actor.getEnviarConta()==true || actor.getCancelarEnvio()==true)
            {
                pos = P_pestana.indexOfTab("Pre-factura");
                if(pos>=0)
                {
                    P_pestana.setSelectedIndex(pos);    
                }
                else
                {
                    preFac = new ModificarOrden(actor, t_periodo.getText().toString(), h.menu, sessionPrograma,rutaFTP,configuracion);
                    PanelPestanas btc=new PanelPestanas(P_pestana,h.menu,actor);
                    P_pestana.addTab("Pre-factura", preFac);
                    P_pestana.setSelectedComponent(preFac);
                    P_pestana.setTabComponentAt(P_pestana.getSelectedIndex(), btc);
                    preFac.t_orden.requestFocus();
                    preFac=null;
                }
            }
            else
                JOptionPane.showMessageDialog(null, "¡Acceso denegado!");
        }catch(Exception e)
        {
            System.out.println(e);
        }
    }//GEN-LAST:event_jMenuItem20ActionPerformed

    private void jMenuItem19ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem19ActionPerformed
        // TODO add your handling code here:
        h.menu=20;
        h.session(sessionPrograma);
        try
        {
            if(actor.getMovimientoAlmacen()==true || actor.getMovimientoAlmacen2()==true)
            {
                h.session(sessionPrograma);
                pos = P_pestana.indexOfTab("Nuevo Almacén");
                if(pos>=0)
                {
                    P_pestana.setSelectedIndex(pos);    
                }
                else
                {
                    nuevo_almacen = new nuevoAlmacen(actor, sessionPrograma, h.menu,configuracion);
                    PanelPestanas btc=new PanelPestanas(P_pestana,h.menu,actor);
                    P_pestana.addTab("Nuevo Almacén", nuevo_almacen);
                    P_pestana.setSelectedComponent(nuevo_almacen);
                    P_pestana.setTabComponentAt(P_pestana.getSelectedIndex(), btc);
                }
            }
            else
                JOptionPane.showMessageDialog(null, "¡Acceso denegado!");
        }catch(Exception e)
        {
            System.out.println(e);
        }
    }//GEN-LAST:event_jMenuItem19ActionPerformed

    private void jMenuItem22ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem22ActionPerformed
        // TODO add your handling code here:
        h.menu=0;
        h.session(sessionPrograma);
        Session session = HibernateUtil.getSessionFactory().openSession();
        try
        {
            session.beginTransaction().begin();
            actor = (Usuario)session.get(Usuario.class, actor.getIdUsuario());
            if(actor.getEditaPeriodo()==true)
            {
                pos=-1;
                for(int a=0; a<P_pestana.getTabCount(); a++)
                {
                    if(P_pestana.getTitleAt(a)=="E. Ciclo")
                    pos=a;
                }
                if(pos>=0)
                {
                    P_pestana.setSelectedIndex(pos);
                    eCiclo.t_ciclo.requestFocus();
                }
                else
                {
                    eCiclo = new editaCiclo(actor,sessionPrograma);
                    PanelPestanas btc=new PanelPestanas(P_pestana,-1, actor);
                    P_pestana.addTab("E. Ciclo", eCiclo);
                    P_pestana.setSelectedComponent(eCiclo);
                    P_pestana.setTabComponentAt(P_pestana.getSelectedIndex(), btc);
                    eCiclo.t_ciclo.requestFocus();
                    eCiclo.cajas(false, false, false, false);
                }
            }
            else
            {
                JOptionPane.showMessageDialog(null, "¡Acceso denegado!");
            }
        }catch(Exception e)
        {
            System.out.println(e);
        }
        if(session!=null)
            if(session.isOpen())
                session.close();
    }//GEN-LAST:event_jMenuItem22ActionPerformed

    private void jMenuItem35ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem35ActionPerformed
        // TODO add your handling code here:
        h.menu=0;
        h.session(sessionPrograma);
        Session session = HibernateUtil.getSessionFactory().openSession();
        try
        {
            session.beginTransaction().begin();
            actor = (Usuario)session.get(Usuario.class, actor.getIdUsuario());
            if(actor.getEditaPeriodo()==true)
             {
                buscaCiclo obj = new buscaCiclo(new javax.swing.JFrame(), true, sessionPrograma, actor);
                obj.t_busca.requestFocus();
                Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
                obj.setLocation((d.width/2)-(obj.getWidth()/2), (d.height/2)-(obj.getHeight()/2));
                obj.setVisible(true);
                Ciclo orden_act=obj.getReturnStatus();

                if (orden_act!=null)
                {
                    orden_act=(Ciclo)session.get(Ciclo.class, orden_act.getIdCiclo());
                    pos=-1;
                    for(int a=0; a<P_pestana.getTabCount(); a++)
                    {
                        if(P_pestana.getTitleAt(a)=="Edita Ciclo")
                        pos=a;
                    }
                    if(pos>=0)
                    {
                        P_pestana.setSelectedIndex(pos);
                    }
                    else
                    {
                        eCiclo = new editaCiclo(actor, sessionPrograma);
                        PanelPestanas btc=new PanelPestanas(P_pestana,-1, actor);
                        P_pestana.addTab("Edita Ciclo", eCiclo);
                        P_pestana.setSelectedComponent(eCiclo);
                        P_pestana.setTabComponentAt(P_pestana.getSelectedIndex(), btc);
                        eCiclo.t_ciclo.requestFocus();
                    }
                    eCiclo.borra_cajas();
                    eCiclo.cajas(false, false, false, false);
                    eCiclo.t_ciclo.setText(""+orden_act.getIdCiclo());
                    eCiclo.t_descripcion.setText(""+orden_act.getDescripcion());
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
                session.close();
    }//GEN-LAST:event_jMenuItem35ActionPerformed

    private void jMenuItem36ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem36ActionPerformed
        // TODO add your handling code here:
        cerrarSession();
    }//GEN-LAST:event_jMenuItem36ActionPerformed

    private void jMenuItem37ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem37ActionPerformed
        // TODO add your handling code here:
        h.menu=17;
        h.session(sessionPrograma);
        try
        {
            if(actor.getEliminaPedidos()==true)
            {
                buscaPedido obj = new buscaPedido(new javax.swing.JFrame(), true, 0, "");
                obj.t_busca.requestFocus();
                Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
                obj.setLocation((d.width/2)-(obj.getWidth()/2), (d.height/2)-(obj.getHeight()/2));
                obj.setVisible(true);

                Pedido ped=obj.getReturnStatus();
                if(ped!=null)
                {
                    pos = P_pestana.indexOfTab("Eliminar Pedido");
                    if(pos>=0)
                    {
                        P_pestana.setSelectedIndex(pos);    
                        elimina_pedido.pedido=ped;
                        elimina_pedido.busca();
                    }
                    else
                    {
                        elimina_pedido = new EliminaPedido(actor, sessionPrograma, ped, h.menu);
                        PanelPestanas btc=new PanelPestanas(P_pestana,h.menu,actor);
                        P_pestana.addTab("Eliminar Pedido", elimina_pedido);
                        P_pestana.setSelectedComponent(elimina_pedido);
                        P_pestana.setTabComponentAt(P_pestana.getSelectedIndex(), btc);
                    }
                }
            }
            else
                JOptionPane.showMessageDialog(null, "¡Acceso denegado!");    
        }catch(Exception e)
        {
            System.out.println(e);
        }
    }//GEN-LAST:event_jMenuItem37ActionPerformed

    private void jMenuItem32ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem32ActionPerformed
        // TODO add your handling code here:
        h.menu=31;
        h.session(sessionPrograma);
        try
        {
            if(actor.getGenerarFactura()==true)
            {
                pos = P_pestana.indexOfTab("Nueva Factura");
                if(pos>=0)
                    P_pestana.setSelectedIndex(pos);
                else
                {
                    nuevaFactura= new NuevoComprobante(actor, sessionPrograma,configuracion);
                    PanelPestanas btc=new PanelPestanas(P_pestana,h.menu, actor);
                    P_pestana.addTab("Nueva Factura", nuevaFactura);
                    P_pestana.setSelectedComponent(nuevaFactura);
                    P_pestana.setTabComponentAt(P_pestana.getSelectedIndex(), btc);
                }
            }
            else
            {
                JOptionPane.showMessageDialog(null, "¡Acceso denegado!");
            }
        }catch(Exception e)
        {
            System.out.println(e);
        }
    }//GEN-LAST:event_jMenuItem32ActionPerformed

    private void jMenuItem29ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem29ActionPerformed
        // TODO add your handling code here:
        h.menu=32;
        h.session(sessionPrograma);
        try
        {
            if(actor.getGenerarFactura()==true || actor.getAutorizarFactura()==true)
            {
                buscaFactura obj = new buscaFactura(new javax.swing.JFrame(), true, this.sessionPrograma, this.actor, 1,configuracion);
                obj.t_busca.requestFocus();
                Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
                obj.setLocation((d.width/2)-(obj.getWidth()/2), (d.height/2)-(obj.getHeight()/2));
                obj.setVisible(true);
                obj=null;
            }
            else
            {
                JOptionPane.showMessageDialog(null, "¡Acceso denegado!");
            }
        }catch(Exception e)
        {
            System.out.println(e);
        }
    }//GEN-LAST:event_jMenuItem29ActionPerformed

    private void jMenuItem33ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem33ActionPerformed
        // TODO add your handling code here:
        h.menu=33;
        h.session(sessionPrograma);
        try
        {
            if(actor.getGenerarFactura()==true || actor.getAutorizarFactura()==true)
            {
                buscaNota obj = new buscaNota(new javax.swing.JFrame(), true, this.sessionPrograma, this.actor,1,configuracion);
                obj.t_busca.requestFocus();
                Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
                obj.setLocation((d.width/2)-(obj.getWidth()/2), (d.height/2)-(obj.getHeight()/2));
                obj.setVisible(true);
            }
            else
            {
                JOptionPane.showMessageDialog(null, "¡Acceso denegado!");
            }
        }catch(Exception e)
        {
            System.out.println(e);
        }
    }//GEN-LAST:event_jMenuItem33ActionPerformed

    private void jMenuItem38ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem38ActionPerformed
        // TODO add your handling code here:
        h.menu=30;
        h.session(sessionPrograma);
        try
        {
            if(actor.getCancelaMovimientoAlmacen()==true)
            {
                buscaEntrada obj = new buscaEntrada(new javax.swing.JFrame(), true, actor);
                obj.t_busca.requestFocus();
                Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
                obj.setLocation((d.width/2)-(obj.getWidth()/2), (d.height/2)-(obj.getHeight()/2));
                obj.setVisible(true);

                Almacen ped=obj.getReturnStatus();
                if(ped!=null)
                {
                    pos = P_pestana.indexOfTab("Consulta Almacen");
                    if(pos>=0)
                    {
                        P_pestana.setSelectedIndex(pos);    
                        muestra_almacen.actual=ped;
                        muestra_almacen.busca();
                    }
                    else
                    {
                        muestra_almacen = new muestraAlmacen(actor, sessionPrograma,ped,configuracion);
                        PanelPestanas btc=new PanelPestanas(P_pestana,h.menu,actor);
                        P_pestana.addTab("Consulta Almacen", muestra_almacen);
                        P_pestana.setSelectedComponent(muestra_almacen);
                        P_pestana.setTabComponentAt(P_pestana.getSelectedIndex(), btc);
                    }
                }
            }
            else
                JOptionPane.showMessageDialog(null, "¡Acceso denegado!");  
        }catch(Exception e)
        {
            System.out.println(e);
        }
    }//GEN-LAST:event_jMenuItem38ActionPerformed

    private void jCheckBoxMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxMenuItem1ActionPerformed
        // TODO add your handling code here:
        if(jCheckBoxMenuItem1.isSelected()==true)
            tiempo.Detener();
        else
            tiempo.Contar(actor, sessionPrograma);
    }//GEN-LAST:event_jCheckBoxMenuItem1ActionPerformed

    private void jMenuItem28ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem28ActionPerformed
        // TODO add your handling code here:
        h.menu=0;
        h.session(sessionPrograma);
        try
        {
            if(actor.getRespaldar()==true || actor.getRestaurar()==true)
            {
                pos = P_pestana.indexOfTab("Respaldo");
                if(pos>=0)
                {

                    P_pestana.setSelectedIndex(pos);
                }
                else
                {
                    this.panel_respaldo = new respaldo(actor, this.sessionPrograma);
                    PanelPestanas btc=new PanelPestanas(P_pestana,-1, actor);
                    P_pestana.addTab("Respaldo", panel_respaldo);
                    P_pestana.setSelectedComponent(panel_respaldo);
                    P_pestana.setTabComponentAt(P_pestana.getSelectedIndex(), btc);
                }
            }
            else
            {
                JOptionPane.showMessageDialog(null, "¡Acceso denegado!");
            }
        }catch(Exception e)
        {
            System.out.println(e);
        }
    }//GEN-LAST:event_jMenuItem28ActionPerformed

    private void jMenuItem31ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem31ActionPerformed
        // TODO add your handling code here:
        h.menu=8;
        h.session(sessionPrograma);
        Session session = HibernateUtil.getSessionFactory().openSession();
        try
        {
            session.beginTransaction().begin();
            actor = (Usuario)session.get(Usuario.class, actor.getIdUsuario());
            pos = P_pestana.indexOfTab("Operaciones");
            if(pos>=0)
            {
                P_pestana.setSelectedIndex(pos);    
                Modificar_Orden_Valuacion.t_orden.requestFocus();
            }
            else
            {
                panel_responsables = new ResponsablesOP(sessionPrograma, actor,configuracion);
                PanelPestanas btc=new PanelPestanas(P_pestana, h.menu, actor);
                P_pestana.addTab("Operaciones", panel_responsables);
                P_pestana.setSelectedComponent(panel_responsables);
                P_pestana.setTabComponentAt(P_pestana.getSelectedIndex(), btc);
                panel_responsables.t_orden.requestFocus();
            }
        }catch(Exception e)
        {
            System.out.println(e);
        }
        if(session!=null)
            if(session.isOpen())
                session.close();
    }//GEN-LAST:event_jMenuItem31ActionPerformed

    private void jMenuItem42ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem42ActionPerformed
        // TODO add your handling code here:
        try
        {
            Desktop.getDesktop().open(new File("ManualUsuarioFinal.pdf"));
        }
        catch(IOException e)
        {
            System.out.println(e);
        }
    }//GEN-LAST:event_jMenuItem42ActionPerformed

    private void jMenuItem43ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem43ActionPerformed
        // TODO add your handling code here:
        try
        {            
            Clave obj = new Clave(new javax.swing.JFrame(), true, this.actor, this.sessionPrograma);
            Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
            obj.setLocation((d.width/2)-(obj.getWidth()/2), (d.height/2)-(obj.getHeight()/2));
            obj.setVisible(true);
        }catch(Exception e)
        {
            System.out.println(e);
        }
    }//GEN-LAST:event_jMenuItem43ActionPerformed

    private void jMenuItem44ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem44ActionPerformed
        // TODO add your handling code here:
        h.menu=0;
        h.session(sessionPrograma);
        try
        {
            if(actor.getEditaCatalogo()==true)
            {
                pos = P_pestana.indexOfTab("E. Servicios");
                if(pos>=0)
                {
                    P_pestana.setSelectedIndex(pos);
                }
                else
                {
                    eServicios = new editaGrupo(actor, sessionPrograma);
                    PanelPestanas btc=new PanelPestanas(P_pestana,-1, actor);
                    P_pestana.addTab("E. Servicios", eServicios);
                    P_pestana.setSelectedComponent(eServicios);
                    P_pestana.setTabComponentAt(P_pestana.getSelectedIndex(), btc);
                }
            }
            else
            {
                JOptionPane.showMessageDialog(null, "¡Acceso denegado!");
            }
        }catch(Exception e)
        {
            System.out.println(e);
        }
    }//GEN-LAST:event_jMenuItem44ActionPerformed

    private void jMenuItem46ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem46ActionPerformed
        // TODO add your handling code here:
        h.menu=24;
        h.session(sessionPrograma);
        try
        {
            if(actor.getConsultaMovimientoAlmacen()==true)
            {
                pos = P_pestana.indexOfTab("Reportes");
                if(pos>=0)
                {
                    P_pestana.setSelectedIndex(pos);    
                }
                else
                {
                    reporte2 = new Reporte2(actor, sessionPrograma, h.menu,configuracion);
                    PanelPestanas btc=new PanelPestanas(P_pestana, h.menu,actor);
                    P_pestana.addTab("Reportes", reporte2);
                    P_pestana.setSelectedComponent(reporte2);
                    P_pestana.setTabComponentAt(P_pestana.getSelectedIndex(), btc);
                }
            }
            else
                JOptionPane.showMessageDialog(null, "¡Acceso denegado!");
        }catch(Exception e)
        {
            e.printStackTrace();
        }
        //JOptionPane.showMessageDialog(null, "¡Aun no disponible!");
    }//GEN-LAST:event_jMenuItem46ActionPerformed

    private void jMenuItem48ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem48ActionPerformed
        // TODO add your handling code here:
        h.menu=39;
        h.session(sessionPrograma);
        try
        {
            if(actor.getGenerarFactura()==true)
            {
                pos = P_pestana.indexOfTab("Cuentas por Cobrar");
                if(pos>=0)
                    P_pestana.setSelectedIndex(pos);
                else
                {
                    cxc= new Cuentas(actor, sessionPrograma,configuracion);
                    PanelPestanas btc=new PanelPestanas(P_pestana,h.menu, actor);
                    P_pestana.addTab("Cuentas por Cobrar", cxc);
                    P_pestana.setSelectedComponent(cxc);
                    P_pestana.setTabComponentAt(P_pestana.getSelectedIndex(), btc);
                }
            }
            else
            {
                JOptionPane.showMessageDialog(null, "¡Acceso denegado!");
            }
        }catch(Exception e)
        {
            System.out.println(e);
        }
    }//GEN-LAST:event_jMenuItem48ActionPerformed

    private void jMenuItem47ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem47ActionPerformed
        // TODO add your handling code here:
        h.menu=40;
        h.session(sessionPrograma);
        try
        {
            if(actor.getGenerarFactura()==true)
            {
                pos = P_pestana.indexOfTab("R. Contabilidad");
                if(pos>=0)
                {
                    P_pestana.setSelectedIndex(pos);    
                }
                else
                {
                    Rcuenta = new RCuentas(actor, sessionPrograma, h.menu);
                    PanelPestanas btc=new PanelPestanas(P_pestana, h.menu,actor);
                    P_pestana.addTab("R. Contabilidad", Rcuenta);
                    P_pestana.setSelectedComponent(Rcuenta);
                    P_pestana.setTabComponentAt(P_pestana.getSelectedIndex(), btc);
                }
            }
            else
                JOptionPane.showMessageDialog(null, "¡Acceso denegado!");
        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }//GEN-LAST:event_jMenuItem47ActionPerformed

    private void jMenuItem27ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem27ActionPerformed
        // TODO add your handling code here:
        h.menu = 9;
        h.session(sessionPrograma);
        Session session = HibernateUtil.getSessionFactory().openSession();
        try
        {
            session.beginTransaction().begin();
            actor = (Usuario)session.get(Usuario.class, actor.getIdUsuario());
            if(actor.getRespaldar()==true)
            {
                pos = P_pestana.indexOfTab("Destajos");
                if(pos>=0)
                {
                    P_pestana.setSelectedIndex(pos);    
                }
                else
                {
                    Modificar_destajos = null;
                    Modificar_destajos = new ModificarOrden(actor, t_periodo.getText().toString(), h.menu, sessionPrograma, rutaFTP,configuracion);
                    PanelPestanas btc=new PanelPestanas(P_pestana,h.menu,actor);
                    P_pestana.addTab("Destajos", Modificar_destajos);
                    P_pestana.setSelectedComponent(Modificar_destajos);
                    P_pestana.setTabComponentAt(P_pestana.getSelectedIndex(), btc);
                    Modificar_destajos.t_orden.requestFocus();
                    btc=null;
                    Modificar_destajos=null;
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
                session.close();
    }//GEN-LAST:event_jMenuItem27ActionPerformed

    private void m_consultar_articulo1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_consultar_articulo1ActionPerformed
        // TODO add your handling code here:
        h.menu=22;
        h.session(sessionPrograma);
        Session session = HibernateUtil.getSessionFactory().openSession();
        try
        {
            session.beginTransaction().begin();
            if(actor.getConsultarEjemplar()==true)
            {
                buscaEjemplar obj = new buscaEjemplar(new javax.swing.JFrame(), true, sessionPrograma, actor, 1);
                obj.t_busca.requestFocus();
                Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
                obj.setLocation((d.width/2)-(obj.getWidth()/2), (d.height/2)-(obj.getHeight()/2));
                obj.setVisible(true);
                Ejemplar orden_act=obj.getReturnStatus();

                if (orden_act!=null)
                {
                    orden_act=(Ejemplar)session.get(Ejemplar.class, orden_act.getIdParte());
                    pos=-1;
                    for(int a=0; a<P_pestana.getTabCount(); a++)
                    {
                        if(P_pestana.getTitleAt(a)=="Edita Ejemplar")
                        pos=a;
                    }
                    if(pos>=0)
                    {
                        P_pestana.setSelectedIndex(pos);
                    }
                    else
                    {
                        eEjemplar = new editaEjemplar(actor, sessionPrograma, 1, configuracion);
                        PanelPestanas btc=new PanelPestanas(P_pestana,-1, actor);
                        P_pestana.addTab("Edita Ejemplar", eEjemplar);
                        P_pestana.setSelectedComponent(eEjemplar);
                        P_pestana.setTabComponentAt(P_pestana.getSelectedIndex(), btc);
                        eEjemplar.t_numero.requestFocus();
                    }
                    eEjemplar.borra_cajas();
                    eEjemplar.habilita_cajas(true);
                    eEjemplar.t_numero.setText(orden_act.getIdParte());
                    eEjemplar.NS=""+orden_act.getIdParte();
                    if(orden_act.getMarca()!=null)
                        eEjemplar.c_marca.setSelectedItem(""+orden_act.getMarca().getIdMarca());
                    if(orden_act.getMarca()!=null)
                        eEjemplar.c_grupo.setSelectedItem(""+orden_act.getGrupo().getDescripcion());
                    if(orden_act.getCatalogo()!=null)
                    {
                        eEjemplar.t_id_catalogo.setText(""+orden_act.getCatalogo().getIdCatalogo());
                        eEjemplar.t_catalogo.setText(""+orden_act.getCatalogo().getNombre());
                    }
                    if(orden_act.getComentario()!=null)
                        eEjemplar.t_comentario.setText(orden_act.getComentario());
                    else
                        eEjemplar.t_comentario.setText("");
                    eEjemplar.medida.setSelectedItem(orden_act.getMedida());
                    eEjemplar.t_precio.setText(orden_act.getPrecio().toString());
                    eEjemplar.t_maximo.setText(orden_act.getMaximo().toString());
                    eEjemplar.t_minimo.setText(orden_act.getMinimo().toString());
                    eEjemplar.t_existencia.setText(orden_act.getExistencias().toString());
                    eEjemplar.cb_moneda.setEnabled(orden_act.getMoneda());
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
        session.close();
    }//GEN-LAST:event_m_consultar_articulo1ActionPerformed

    private void m_editar_articulo1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_editar_articulo1ActionPerformed
        // TODO add your handling code here:
        h.menu=23;
        h.session(sessionPrograma);
        try
        {
            if(actor.getEditarEjemplar()==true)
            {
                pos = P_pestana.indexOfTab("Edita Ejemplar");
                if(pos>=0)
                {
                    P_pestana.setSelectedIndex(pos);
                    eEjemplar.t_numero.requestFocus();
                }
                else
                {
                    eEjemplar = new editaEjemplar(actor,sessionPrograma, 1, configuracion);
                    PanelPestanas btc=new PanelPestanas(P_pestana,-1, actor);
                    P_pestana.addTab("Edita Ejemplar", eEjemplar);
                    P_pestana.setSelectedComponent(eEjemplar);
                    P_pestana.setTabComponentAt(P_pestana.getSelectedIndex(), btc);
                    eEjemplar.t_numero.requestFocus();
                }
            }
            else
            JOptionPane.showMessageDialog(null, "¡Acceso denegado!");
        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }//GEN-LAST:event_m_editar_articulo1ActionPerformed

    private void jMenuItem53ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem53ActionPerformed
        // TODO add your handling code here:
        h.menu=25;
        try
        {
            pos=P_pestana.indexOfTab("Existencias");
            if(pos>=0)
            {
                P_pestana.setSelectedIndex(pos);
            }
            else
            {
                existencias = null;
                existencias = new Existencias(actor, sessionPrograma);
                PanelPestanas btc=new PanelPestanas(P_pestana, h.menu,actor);
                P_pestana.addTab("Existencias", existencias);
                P_pestana.setSelectedComponent(existencias);
                P_pestana.setTabComponentAt(P_pestana.getSelectedIndex(), btc);
            }
            System.gc();
        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }//GEN-LAST:event_jMenuItem53ActionPerformed

    private void jMenuItem40ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem40ActionPerformed
        h.menu=26;
        h.session(sessionPrograma);
        Session session = HibernateUtil.getSessionFactory().openSession();
        try
        {
            session.beginTransaction().begin();
            actor = (Usuario)session.get(Usuario.class, actor.getIdUsuario());
            if(actor!=null && actor.getHerramienta()==true)
            {
                buscaHerramienta obj = new buscaHerramienta(new javax.swing.JFrame(), true, sessionPrograma, actor);
                obj.t_busca.requestFocus();
                Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
                obj.setLocation((d.width/2)-(obj.getWidth()/2), (d.height/2)-(obj.getHeight()/2));
                obj.setVisible(true);
                Herramienta orden_act=obj.getReturnStatus();

                if (orden_act!=null)
                {
                    orden_act=(Herramienta)session.get(Herramienta.class, orden_act.getIdHerramienta());
                    pos=-1;
                    for(int a=0; a<P_pestana.getTabCount(); a++)
                    {
                        if(P_pestana.getTitleAt(a)=="Edita Herramientas")
                        pos=a;
                    }
                    if(pos>=0)
                    {
                        P_pestana.setSelectedIndex(pos);
                    }
                    else
                    {
                        eHerramienta = new editaHerramienta(actor,sessionPrograma);
                        PanelPestanas btc=new PanelPestanas(P_pestana,-1, actor);
                        P_pestana.addTab("Edita Herrmienta", eHerramienta);
                        P_pestana.setSelectedComponent(eHerramienta);
                        P_pestana.setTabComponentAt(P_pestana.getSelectedIndex(), btc);
                        eHerramienta.t_nombre.requestFocus();

                    }
                    eHerramienta.borra_cajas();
                    eHerramienta.cajas(true, true, true, true, true, true, true);
                    eHerramienta.t_numero.setText(orden_act.getIdHerramienta());
                    eHerramienta.NS=""+orden_act.getIdHerramienta();
                    eHerramienta.t_nombre.setText(orden_act.getNombre());
                    if(orden_act.getUbicacion()!=null)
                    eHerramienta.t_ubicacion.setText(orden_act.getUbicacion());

                    if(orden_act.getExistencias()!=null)
                    eHerramienta.t_existencia.setText(String.valueOf(orden_act.getExistencias()));

                    if(orden_act.getComentario()!=null)
                    eHerramienta.t_comentario.setText(orden_act.getComentario());

                    try
                    {
                        if(orden_act.getImagen()!=null)
                        eHerramienta.p_foto.removeAll();
                        Imagen op;
                        eHerramienta.nombreFoto=orden_act.getImagen();
                        op=new Imagen("herramienta/miniatura/"+eHerramienta.nombreFoto, 144, 135, 1, 1, 144, 135);
                        eHerramienta.p_foto.add(op);
                        eHerramienta.p_foto.repaint();

                    }catch(Exception e){
                        eHerramienta.p_foto.removeAll();
                        eHerramienta.p_foto.repaint();
                    }
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
        session.close();
    }//GEN-LAST:event_jMenuItem40ActionPerformed

    private void jMenuItem39ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem39ActionPerformed
        // TODO add your handling code here:
        h.menu=27;
        h.session(sessionPrograma);
        try
        {
            if(actor!=null && actor.getHerramienta()==true)
            {
                pos = P_pestana.indexOfTab("Edita Herramienta");
                if(pos>=0)
                {
                    P_pestana.setSelectedIndex(pos);
                }
                else
                {
                    eHerramienta = new editaHerramienta(actor,sessionPrograma);
                    PanelPestanas btc=new PanelPestanas(P_pestana,-1, actor);
                    P_pestana.addTab("Edita Herrmienta", eHerramienta);
                    P_pestana.setSelectedComponent(eHerramienta);
                    P_pestana.setTabComponentAt(P_pestana.getSelectedIndex(), btc);
                    eHerramienta.t_nombre.requestFocus();
                }
            }
            else
            JOptionPane.showMessageDialog(null, "¡Acceso denegado!");
        }catch(Exception e)
        {
            System.out.println(e);
        }
    }//GEN-LAST:event_jMenuItem39ActionPerformed

    private void jMenuItem41ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem41ActionPerformed
        // TODO add your handling code here:
        h.menu=28;
        try
        {
            if(actor.getResponsiva()==true)
            {
                pos=P_pestana.indexOfTab("Responsivas");
                if(pos>=0)
                {
                    P_pestana.setSelectedIndex(pos);
                }
                else
                {
                    responsiva = null;
                    responsiva = new Responsiva(actor, sessionPrograma, configuracion);
                    PanelPestanas btc=new PanelPestanas(P_pestana, h.menu,actor);
                    P_pestana.addTab("Responsivas", responsiva);
                    P_pestana.setSelectedComponent(responsiva);
                    P_pestana.setTabComponentAt(P_pestana.getSelectedIndex(), btc);
                }
                System.gc();
            }
            else
            JOptionPane.showMessageDialog(null, "¡Acceso denegado!");
        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }//GEN-LAST:event_jMenuItem41ActionPerformed

    private void jMenuItem54ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem54ActionPerformed
        // TODO add your handling code here:
        h.menu=29;
        h.session(sessionPrograma);
        try
        {
            if(actor.getMovimientoAlmacen()==true)
            {
                pos=P_pestana.indexOfTab("Ajuste Inventario");
                if(pos>=0)
                {
                    P_pestana.setSelectedIndex(pos);
                }
                else
                {
                    ajuste = null;
                    ajuste = new ajusteInventario(actor, sessionPrograma,configuracion);
                    PanelPestanas btc=new PanelPestanas(P_pestana, h.menu,actor);
                    P_pestana.addTab("Ajuste Inventario", ajuste);
                    P_pestana.setSelectedComponent(ajuste);
                    P_pestana.setTabComponentAt(P_pestana.getSelectedIndex(), btc);
                }
                System.gc();
            }
            else
            JOptionPane.showMessageDialog(null, "¡Acceso denegado!");
        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }//GEN-LAST:event_jMenuItem54ActionPerformed

    private void jMenuItem50ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem50ActionPerformed
        // TODO add your handling code here:
        h.menu=35;
        h.session(sessionPrograma);
         try{
            if(actor.getConsultaTipo()==true)
            {
                buscaCuentas obj1 = new buscaCuentas(new javax.swing.JFrame(), true, sessionPrograma,actor, "");
                Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
                obj1.setLocation((d.width/2)-(obj1.getWidth()/2), (d.height/2)-(obj1.getHeight()/2));
                obj1.setVisible(true);
                ArrayList lista=obj1.getReturnStatus();

                if(lista.size()>0){
                    pos = P_pestana.indexOfTab("E. Cuentas");
                   if(pos>=0)
                   {
                       P_pestana.setSelectedIndex(pos);
                   }
                   else
                   {
                       eCuentas = new editaCuentas(actor, sessionPrograma);
                       PanelPestanas btc=new PanelPestanas(P_pestana,-1, actor);
                       P_pestana.addTab("E. Cuentas", eCuentas);
                       P_pestana.setSelectedComponent(eCuentas);
                       P_pestana.setTabComponentAt(P_pestana.getSelectedIndex(), btc);
                       eCuentas.Cuenta.requestFocus();
                   }
                    eCuentas.Cuenta.setText(""+lista.get(0).toString());
                    eCuentas.cu=lista.get(0).toString();
                    if(lista.get(2).toString().equals("A")){
                        eCuentas.jComboBox1.setSelectedIndex(1);
                    }else{
                        eCuentas.jComboBox1.setSelectedIndex(2);
                    }
                    
                    eCuentas.nombre.setText(""+lista.get(1).toString());
                    eCuentas.cajas(true, true, true, true, true , true);
                }
            }else
                JOptionPane.showMessageDialog(null, "Â¡Acceso denegado!");  
        }catch(Exception e){
            System.out.println(e);
        }
    }//GEN-LAST:event_jMenuItem50ActionPerformed

    private void jMenuItem51ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem51ActionPerformed
        // TODO add your handling code here:
        h.menu=36;
        h.session(sessionPrograma);
        try
        {
            if(actor.getEditaTipo()==true)
            {
                pos = P_pestana.indexOfTab("E. Cuentas");
                if(pos>=0)
                {
                    P_pestana.setSelectedIndex(pos);
                    eCuentas.Cuenta.requestFocus();
                }
                else
                {
                    eCuentas = new editaCuentas(actor, sessionPrograma);
                    PanelPestanas btc=new PanelPestanas(P_pestana,-1, actor);
                    P_pestana.addTab("E. Cuentas", eCuentas);
                    P_pestana.setSelectedComponent(eCuentas);
                    P_pestana.setTabComponentAt(P_pestana.getSelectedIndex(), btc);
                    eCuentas.Cuenta.requestFocus();
                     eCuentas.cajas(false, false, false, false, false, false);
                }
            }
            else
            {
                JOptionPane.showMessageDialog(null, "Â¡Acceso denegado!");
            }
        }catch(Exception e)
        {
            System.out.println(e);
        }
    }//GEN-LAST:event_jMenuItem51ActionPerformed

    private void jMenuItem52ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem52ActionPerformed
        // TODO add your handling code here:
        h.menu=37;
        try
        {
            if(actor.getGenerarFactura()==true)
            {
                pos=P_pestana.indexOfTab("Egresos");
                if(pos>=0)
                {
                    P_pestana.setSelectedIndex(pos);
                }
                else
                {
                    egresos = null;
                    egresos = new Egresos(actor, sessionPrograma, h.menu);
                    PanelPestanas btc=new PanelPestanas(P_pestana, h.menu,actor);
                    P_pestana.addTab("Egresos", egresos);
                    P_pestana.setSelectedComponent(egresos);
                    P_pestana.setTabComponentAt(P_pestana.getSelectedIndex(), btc);
                }
                System.gc();
            }
            else
            JOptionPane.showMessageDialog(null, "¡Acceso denegado!");
        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }//GEN-LAST:event_jMenuItem52ActionPerformed

    private void jMenuItem49ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem49ActionPerformed
        // TODO add your handling code here:
        h.menu=38;
        try
        {
            if(actor.getGenerarFactura()==true)
            {
                pos=P_pestana.indexOfTab("Provisiones");
                if(pos>=0)
                {
                    P_pestana.setSelectedIndex(pos);
                }
                else
                {
                    provision = null;
                    provision = new Provision(actor, sessionPrograma, h.menu);
                    PanelPestanas btc=new PanelPestanas(P_pestana, h.menu,actor);
                    P_pestana.addTab("Provisiones", provision);
                    P_pestana.setSelectedComponent(provision);
                    P_pestana.setTabComponentAt(P_pestana.getSelectedIndex(), btc);
                }
                System.gc();
            }
            else
            JOptionPane.showMessageDialog(null, "¡Acceso denegado!");
        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }//GEN-LAST:event_jMenuItem49ActionPerformed

    private void jMenuItem55ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem55ActionPerformed
        // TODO add your handling code here:
        Version ver=new Version();
        boolean respuesta=ver.version();
        if(respuesta==true)
        {
            try{
                Runtime runtime = Runtime.getRuntime();
                Process child = runtime.exec("java -jar Descarga.jar");
            }catch(Exception e){}
            System.exit(0);
        }
        else
        {
            JOptionPane.showMessageDialog(this, "No existen Actualizaciones Disponibles");
        }
    }//GEN-LAST:event_jMenuItem55ActionPerformed

    private void jMenuItem57ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem57ActionPerformed
        // TODO add your handling code here:
        h.menu=0;
        h.session(sessionPrograma);
        try
        {
            if(actor.getEditaEmpleados()==true)
            {
                pos = P_pestana.indexOfTab("E. Ajustador");
                if(pos>=0)
                {
                    P_pestana.setSelectedIndex(pos);
                }
                else
                {
                    eAjustador = new editaAjustador(actor,sessionPrograma);
                    PanelPestanas btc=new PanelPestanas(P_pestana,-1, actor);
                    P_pestana.addTab("E. Ajustador", eAjustador);
                    P_pestana.setSelectedComponent(eAjustador);
                    P_pestana.setTabComponentAt(P_pestana.getSelectedIndex(), btc);
                }
            }
            else
            {
                JOptionPane.showMessageDialog(null, "¡Acceso denegado!");
            }
        }catch(Exception e)
        {
            System.out.println(e);
        }
    }//GEN-LAST:event_jMenuItem57ActionPerformed

    private void jMenuItem60ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem60ActionPerformed
        // TODO add your handling code here:
        h.menu=41;
        try
        {
            pos = P_pestana.indexOfTab("KPI");
            if(pos>=0)
            {
                P_pestana.setSelectedIndex(pos);    
            }
            else
            {
                kpi = new Compras_Kpi(actor,configuracion);
                PanelPestanas btc=new PanelPestanas(P_pestana,h.menu,actor);
                P_pestana.addTab("KPI", kpi);
                P_pestana.setSelectedComponent(kpi);
                P_pestana.setTabComponentAt(P_pestana.getSelectedIndex(), btc);
            }
        }catch(Exception e)
        {
            System.out.println(e);
        }
    }//GEN-LAST:event_jMenuItem60ActionPerformed

    private void jMenuItem30ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem30ActionPerformed
        // TODO add your handling code here:
        h.menu=34;
        h.session(sessionPrograma);
        try
        {
            if(actor.getGenerarFactura()==true || actor.getAutorizarFactura()==true)
            {
                buscaPago obj = new buscaPago(new javax.swing.JFrame(), true, this.sessionPrograma, this.actor, 1,configuracion);
                obj.t_busca.requestFocus();
                Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
                obj.setLocation((d.width/2)-(obj.getWidth()/2), (d.height/2)-(obj.getHeight()/2));
                obj.setVisible(true);
                obj=null;
            }
            else
            {
                JOptionPane.showMessageDialog(null, "¡Acceso denegado!");
            }
        }catch(Exception e)
        {
            System.out.println(e);
        }
    }//GEN-LAST:event_jMenuItem30ActionPerformed

    private void jMenuItem26ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem26ActionPerformed
        // TODO add your handling code here:
        h.menu=10;
        h.session(sessionPrograma);
        Session session = HibernateUtil.getSessionFactory().openSession();
        try
        {
            session.beginTransaction().begin();
            actor = (Usuario)session.get(Usuario.class, actor.getIdUsuario());
            if(actor.getGeneraAdicional()==true)
            {
                pos = P_pestana.indexOfTab("T.Adicionales");
                if(pos>=0)
                {
                    P_pestana.setSelectedIndex(pos);    
                }
                else
                {
                    adicionales = null;
                    adicionales = new Adicionales(actor, h.menu, sessionPrograma, configuracion);
                    PanelPestanas btc=new PanelPestanas(P_pestana,h.menu,actor);
                    P_pestana.addTab("T.Adicionales", adicionales);
                    P_pestana.setSelectedComponent(adicionales);
                    P_pestana.setTabComponentAt(P_pestana.getSelectedIndex(), btc);
                    adicionales.t_orden.requestFocus();
                    btc=null;
                    Modificar_destajos=null;
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
                session.close();
    }//GEN-LAST:event_jMenuItem26ActionPerformed

    private void jMenuItem59ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem59ActionPerformed
        // TODO add your handling code here:
        h.menu=0;
        h.session(sessionPrograma);
        try
        {
            if(actor.getEditaEmpleados()==true)
            {
                pos = P_pestana.indexOfTab("E. Agente");
                if(pos>=0)
                {
                    P_pestana.setSelectedIndex(pos);
                }
                else
                {
                    eAgente = new editaAgente(actor,sessionPrograma);
                    PanelPestanas btc=new PanelPestanas(P_pestana,-1, actor);
                    P_pestana.addTab("E. Agente", eAgente);
                    P_pestana.setSelectedComponent(eAgente);
                    P_pestana.setTabComponentAt(P_pestana.getSelectedIndex(), btc);

                }
            }
            else
            {
                JOptionPane.showMessageDialog(null, "¡Acceso denegado!");
            }
        }catch(Exception e)
        {
            System.out.println(e);
        }
    }//GEN-LAST:event_jMenuItem59ActionPerformed

    private void m_editar_tipoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_editar_tipoActionPerformed
        // TODO add your handling code here:
        h.menu=0;
        h.session(sessionPrograma);
        try
        {
            if(actor.getEditaTipo()==true)
            {
                pos = P_pestana.indexOfTab("E. Tipo");
                if(pos>=0)
                {
                    P_pestana.setSelectedIndex(pos);
                    eTipo.Nombre.requestFocus();
                }
                else
                {
                    eTipo = new editaTipo(actor, sessionPrograma);
                    PanelPestanas btc=new PanelPestanas(P_pestana,-1, actor);
                    P_pestana.addTab("E. Tipo", eTipo);
                    P_pestana.setSelectedComponent(eTipo);
                    P_pestana.setTabComponentAt(P_pestana.getSelectedIndex(), btc);
                    eTipo.Nombre.requestFocus();
                }
            }
            else
            {
                JOptionPane.showMessageDialog(null, "¡Acceso denegado!");
            }
        }catch(Exception e)
        {
            System.out.println(e);
        }
    }//GEN-LAST:event_m_editar_tipoActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        // TODO add your handling code here:
        h.menu=45;
        h.session(sessionPrograma);
        try
        {
            if(actor.getRestaurar()==true)
            {
                Presupuestos pre = new Presupuestos(this, true, this.sessionPrograma, this.actor, configuracion, h.menu);
                pre.t_orden.requestFocus();
                Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
                pre.setLocation((d.width/2)-(pre.getWidth()/2), (d.height/2)-(pre.getHeight()/2));
                pre.setVisible(true);
                pre=null;
            }
            else
            {
                JOptionPane.showMessageDialog(null, "¡Acceso denegado!");
            }
        }catch(Exception e)
        {
            System.out.println(e);
        }
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jMenuItem45ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem45ActionPerformed
        // TODO add your handling code here:
        h.menu=10;
        h.session(sessionPrograma);
        Session session = HibernateUtil.getSessionFactory().openSession();
        try
        {
            session.beginTransaction().begin();
            actor = (Usuario)session.get(Usuario.class, actor.getIdUsuario());
            if(actor.getReportes()==true)
            {
                pos = P_pestana.indexOfTab("Tiempos");
                if(pos>=0)
                {
                    P_pestana.setSelectedIndex(pos);    
                }
                else
                {
                    adicionales = null;
                    Tiempos tiempos = new Tiempos(actor, h.menu, sessionPrograma, configuracion);
                    PanelPestanas btc=new PanelPestanas(P_pestana,h.menu,actor);
                    P_pestana.addTab("Tiempos", tiempos);
                    P_pestana.setSelectedComponent(tiempos);
                    P_pestana.setTabComponentAt(P_pestana.getSelectedIndex(), btc);
                    tiempos.b_buscar.requestFocus();
                    btc=null;
                    tiempos=null;
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
                session.close();
    }//GEN-LAST:event_jMenuItem45ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Integral.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Integral.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Integral.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Integral.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                entrada=new Integral();
                entrada.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JTabbedPane P_pestana;
    private javax.swing.JMenuItem busca_proveedor;
    private javax.swing.JMenuItem edita_proveedor;
    private javax.swing.JCheckBoxMenuItem jCheckBoxMenuItem1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu10;
    private javax.swing.JMenu jMenu11;
    private javax.swing.JMenu jMenu13;
    private javax.swing.JMenu jMenu14;
    private javax.swing.JMenu jMenu15;
    private javax.swing.JMenu jMenu16;
    private javax.swing.JMenu jMenu17;
    private javax.swing.JMenu jMenu18;
    private javax.swing.JMenu jMenu19;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu20;
    private javax.swing.JMenu jMenu21;
    private javax.swing.JMenu jMenu22;
    private javax.swing.JMenu jMenu24;
    private javax.swing.JMenu jMenu25;
    private javax.swing.JMenu jMenu26;
    private javax.swing.JMenu jMenu27;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu5;
    private javax.swing.JMenu jMenu6;
    private javax.swing.JMenu jMenu7;
    private javax.swing.JMenu jMenu8;
    private javax.swing.JMenu jMenu9;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem10;
    private javax.swing.JMenuItem jMenuItem11;
    private javax.swing.JMenuItem jMenuItem12;
    private javax.swing.JMenuItem jMenuItem13;
    private javax.swing.JMenuItem jMenuItem14;
    private javax.swing.JMenuItem jMenuItem15;
    private javax.swing.JMenuItem jMenuItem16;
    private javax.swing.JMenuItem jMenuItem17;
    private javax.swing.JMenuItem jMenuItem18;
    private javax.swing.JMenuItem jMenuItem19;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem20;
    private javax.swing.JMenuItem jMenuItem21;
    private javax.swing.JMenuItem jMenuItem22;
    private javax.swing.JMenuItem jMenuItem23;
    private javax.swing.JMenuItem jMenuItem24;
    private javax.swing.JMenuItem jMenuItem25;
    private javax.swing.JMenuItem jMenuItem26;
    private javax.swing.JMenuItem jMenuItem27;
    private javax.swing.JMenuItem jMenuItem28;
    private javax.swing.JMenuItem jMenuItem29;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem30;
    private javax.swing.JMenuItem jMenuItem31;
    private javax.swing.JMenuItem jMenuItem32;
    private javax.swing.JMenuItem jMenuItem33;
    private javax.swing.JMenuItem jMenuItem34;
    private javax.swing.JMenuItem jMenuItem35;
    private javax.swing.JMenuItem jMenuItem36;
    private javax.swing.JMenuItem jMenuItem37;
    private javax.swing.JMenuItem jMenuItem38;
    private javax.swing.JMenuItem jMenuItem39;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem40;
    private javax.swing.JMenuItem jMenuItem41;
    private javax.swing.JMenuItem jMenuItem42;
    private javax.swing.JMenuItem jMenuItem43;
    private javax.swing.JMenuItem jMenuItem44;
    private javax.swing.JMenuItem jMenuItem45;
    private javax.swing.JMenuItem jMenuItem46;
    private javax.swing.JMenuItem jMenuItem47;
    private javax.swing.JMenuItem jMenuItem48;
    private javax.swing.JMenuItem jMenuItem49;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JMenuItem jMenuItem50;
    private javax.swing.JMenuItem jMenuItem51;
    private javax.swing.JMenuItem jMenuItem52;
    private javax.swing.JMenuItem jMenuItem53;
    private javax.swing.JMenuItem jMenuItem54;
    private javax.swing.JMenuItem jMenuItem55;
    private javax.swing.JMenuItem jMenuItem57;
    private javax.swing.JMenuItem jMenuItem59;
    private javax.swing.JMenuItem jMenuItem6;
    private javax.swing.JMenuItem jMenuItem60;
    private javax.swing.JMenuItem jMenuItem7;
    private javax.swing.JMenuItem jMenuItem8;
    private javax.swing.JMenuItem jMenuItem9;
    private javax.swing.JLabel l_nombre;
    private javax.swing.JMenu m_administracion;
    private javax.swing.JMenuItem m_apertura;
    private javax.swing.JMenu m_catalogos;
    private javax.swing.JMenu m_clientes;
    private javax.swing.JMenu m_compras;
    private javax.swing.JMenuItem m_consulta_clientes;
    private javax.swing.JMenuItem m_consultar;
    private javax.swing.JMenuItem m_consultar_articulo;
    private javax.swing.JMenuItem m_consultar_articulo1;
    private javax.swing.JMenuItem m_consultar_catalogo;
    private javax.swing.JMenuItem m_consultar_especialidad;
    private javax.swing.JMenuItem m_consultar_estatus;
    private javax.swing.JMenuItem m_consultar_puestos;
    private javax.swing.JMenuItem m_consultar_reparacion;
    private javax.swing.JMenuItem m_consultar_tipo;
    private javax.swing.JMenu m_edita_ciclo;
    private javax.swing.JMenuItem m_edita_clientes;
    private javax.swing.JMenuItem m_editar_articulo;
    private javax.swing.JMenuItem m_editar_articulo1;
    private javax.swing.JMenuItem m_editar_catalogo;
    private javax.swing.JMenuItem m_editar_especialidad;
    private javax.swing.JMenuItem m_editar_estatus;
    private javax.swing.JMenuItem m_editar_marca;
    private javax.swing.JMenuItem m_editar_puestos;
    private javax.swing.JMenuItem m_editar_reparacion;
    private javax.swing.JMenuItem m_editar_tipo;
    private javax.swing.JMenu m_itilerias;
    private javax.swing.JMenu m_operaciones;
    private javax.swing.JMenu m_servicios;
    private javax.swing.JMenu m_valuacion;
    private javax.swing.JMenuItem nuevo_proveedor;
    private javax.swing.JPanel p_centro;
    private javax.swing.JPanel p_img;
    private javax.swing.JPanel p_inicio;
    private javax.swing.JPanel p_logo;
    private javax.swing.JPanel p_tilulo;
    private javax.swing.JLabel t_bienvenido;
    private javax.swing.JLabel t_bienvenido1;
    private javax.swing.JLabel t_periodo;
    private javax.swing.JLabel t_usuario;
    // End of variables declaration//GEN-END:variables
}
