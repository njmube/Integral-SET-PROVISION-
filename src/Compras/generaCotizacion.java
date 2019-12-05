/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Compras;

import Hibernate.Util.HibernateUtil;
import Hibernate.entidades.Configuracion;
import Hibernate.entidades.Cotizacion;
import Hibernate.entidades.Ejemplar;
import Hibernate.entidades.Orden;
import Hibernate.entidades.Partida;
import Hibernate.entidades.PartidaCotizacion;
import Hibernate.entidades.Proveedor;
import Hibernate.entidades.Usuario;
import Proveedor.buscaProveedor;
import Servicios.EnviarCorreo;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import Integral.ExtensionFileFilter;
import Integral.Herramientas;
import Integral.HorizontalBarUI;
import Integral.Render1;
import Integral.VerticalBarUI;

/**
 *
 * @author I.S.C Salvador
 */
public class generaCotizacion extends javax.swing.JPanel {

    Herramientas h;
    private Usuario user;
    //private Session session;
    String estado;
    String sessionPrograma="";
    int x=0, entro=0;
    private String orden;
    DefaultTableModel model;
    DefaultTableModel model1;
    String[] columnas = new String [] {"N°", "#", "Cant", "Codigo", "Descripción", "Prov1", "Prov2", "Prov3"};
    String[] columnas1 = new String [] {"Pedido","NP", "Proveedor", "Fecha"};
    List noPartida;
    List cotizaciones;
    File archivoXLS=null;
    int configuracion=1;
    /**
     * Creates new form generaCotizacion
     */
    public generaCotizacion(String ord, Usuario us, String edo, String ses, int configuracion) {
        initComponents();
        scroll.getVerticalScrollBar().setUI(new VerticalBarUI());
        scroll.getHorizontalScrollBar().setUI(new HorizontalBarUI());
        scroll1.getVerticalScrollBar().setUI(new VerticalBarUI());
        scroll1.getHorizontalScrollBar().setUI(new HorizontalBarUI());
        noPartida= new ArrayList();
        this.configuracion=configuracion;
        estado=edo;
        sessionPrograma=ses;
        orden=ord;
        user=us;
        formatoTabla();
        buscaDato();
        formatoTabla1();
        cargaCotizaciones();
        if(edo.compareTo("")!=0)
            bloquea(false);
        h=new Herramientas(user, 0);
        if(h.isCerrada(orden)==true)
            bloquea(false);
    }

     public void formatoTabla()
    {
        Color c1 = new java.awt.Color(2, 135, 242);
        for(int x=0; x<t_datos.getColumnModel().getColumnCount(); x++)
            t_datos.getColumnModel().getColumn(x).setHeaderRenderer(new Render1(c1));
        tabla_tamaños();
        t_datos.setShowVerticalLines(true);
        t_datos.setShowHorizontalLines(true);
        t_datos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }
     
     public void formatoTabla1()
    {
        Color c1 = new java.awt.Color(2, 135, 242);
        for(int x=0; x<t_datos1.getColumnModel().getColumnCount(); x++)
            t_datos1.getColumnModel().getColumn(x).setHeaderRenderer(new Render1(c1));
        tabla_tamaños1();
        t_datos1.setShowVerticalLines(true);
        t_datos1.setShowHorizontalLines(true);
        t_datos1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }
     
    DefaultTableModel ModeloTablaReporte(int renglones, String columnas[])
    {
            model = new DefaultTableModel(new Object [renglones][2], columnas)
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
                    false, false, false, true, false, true, true, true 
                };

                public void setValueAt(Object value, int row, int col)
                 {
                        Vector vector = (Vector)this.dataVector.elementAt(row);
                        Object celda = ((Vector)this.dataVector.elementAt(row)).elementAt(col);
                        Session session;
                        switch(col)
                        {
                            case 0:
                                    vector.setElementAt(value, col);
                                    this.dataVector.setElementAt(vector, row);
                                    fireTableCellUpdated(row, col);
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
                                        session = HibernateUtil.getSessionFactory().openSession();
                                        try
                                        {
                                            session.beginTransaction().begin();
                                            user = (Usuario)session.get(Usuario.class, user.getIdUsuario());
                                            if(user.getEditaCodigo()==true)
                                            {
                                                Partida part=(Partida) session.createCriteria(Partida.class).add(Restrictions.eq("ordenByIdOrden.idOrden", Integer.parseInt(orden))).add(Restrictions.eq("idEvaluacion", Integer.parseInt(t_datos.getValueAt(t_datos.getSelectedRow(), 0).toString()))).add(Restrictions.eq("subPartida", Integer.parseInt(t_datos.getValueAt(t_datos.getSelectedRow(), 1).toString()))).setMaxResults(1).uniqueResult();
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
                                    break;

                            case 5:
                                    session = HibernateUtil.getSessionFactory().openSession();
                                    try
                                    {
                                        session.beginTransaction().begin();
                                        Proveedor prov=new Proveedor();
                                        if(value.toString().compareTo("")!=0)
                                            prov=(Proveedor)session.get(Proveedor.class, Integer.parseInt(value.toString()));
                                        if(prov!=null)
                                        {
                                            vector.setElementAt(value, col);
                                            this.dataVector.setElementAt(vector, row);
                                            fireTableCellUpdated(row, col);
                                            session.getTransaction().commit();
                                        }
                                        else
                                            JOptionPane.showMessageDialog(null, "el numero de proveedor no existe");
                                    }
                                    catch(Exception e)
                                    {
                                        session.getTransaction().rollback();
                                        System.out.println(e);
                                    }
                                    if(session!=null)
                                        if(session.isOpen()==true)
                                            session.close();
                                    break;
                            
                            case 6:
                                    session = HibernateUtil.getSessionFactory().openSession();
                                    try
                                    {
                                        session.beginTransaction().begin();
                                        Proveedor prov=new Proveedor();
                                        if(value.toString().compareTo("")!=0)
                                            prov=(Proveedor)session.get(Proveedor.class, Integer.parseInt(value.toString()));
                                        if(prov!=null)
                                        {
                                            vector.setElementAt(value, col);
                                            this.dataVector.setElementAt(vector, row);
                                            fireTableCellUpdated(row, col);
                                            session.getTransaction().commit();
                                        }
                                        else
                                            JOptionPane.showMessageDialog(null, "el numero de proveedor no existe");
                                    }
                                    catch(Exception e)
                                    {
                                        session.getTransaction().rollback();
                                        System.out.println(e);
                                    }
                                    if(session!=null)
                                        if(session.isOpen()==true)
                                            session.close();
                                    break;
                            
                            case 7:
                                    session = HibernateUtil.getSessionFactory().openSession();
                                    try
                                    {
                                        session.beginTransaction().begin();
                                        Proveedor prov=new Proveedor();
                                        if(value.toString().compareTo("")!=0)
                                            prov=(Proveedor)session.get(Proveedor.class, Integer.parseInt(value.toString()));
                                        if(prov!=null)
                                        {
                                            vector.setElementAt(value, col);
                                            this.dataVector.setElementAt(vector, row);
                                            fireTableCellUpdated(row, col);
                                            session.getTransaction().commit();
                                        }
                                        else
                                            JOptionPane.showMessageDialog(null, "el numero de proveedor no existe");
                                    }
                                    catch(Exception e)
                                    {
                                        session.getTransaction().rollback();
                                        System.out.println(e);
                                    }
                                    if(session!=null)
                                        if(session.isOpen()==true)
                                            session.close();
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
    
    DefaultTableModel ModeloTablaReporte1(int renglones, String columnas[])
    {
            model1 = new DefaultTableModel(new Object [renglones][2], columnas)
            {
                Class[] types = new Class [] {
                    java.lang.String.class, 
                    java.lang.String.class, 
                    java.lang.String.class, 
                    java.lang.String.class, 
                };
                boolean[] canEdit = new boolean [] {
                    false, false, false, false
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
            return model1;
        }
    
   public void tabla_tamaños()
   {
        TableColumnModel col_model = t_datos.getColumnModel();
        DefaultTableCellRenderer tcr = new DefaultTableCellRenderer();
        tcr.setHorizontalAlignment(SwingConstants.RIGHT);

        for (int i=0; i<t_datos.getColumnCount(); i++)
        {
  	      TableColumn column = col_model.getColumn(i);
              switch(i)
              {
                  case 0://N°
                      column.setPreferredWidth(15);
                      column.setCellRenderer(tcr);
                      break;
                  case 1://#
                      column.setPreferredWidth(15);
                      column.setCellRenderer(tcr);
                      break;
                  case 2://Cant
                      column.setPreferredWidth(15);
                      column.setCellRenderer(tcr);
                      break;
                  case 3://codigo
                      column.setPreferredWidth(100);
                      //DefaultCellEditor miEditor = new DefaultCellEditor(numeros);
                      //miEditor.setClickCountToStart(2);
                      //column.setCellEditor(miEditor); 
                      break;
                  case 4://descripcion
                      column.setPreferredWidth(250);
                      break;
                  case 5://prov1
                      column.setPreferredWidth(20);
                      break;
                  case 6://prov2
                      column.setPreferredWidth(20);
                      break;
                  case 7://prov3
                      column.setPreferredWidth(20);
                      break;
                      
                  default:
                      column.setPreferredWidth(20);
                      break;
              }
        }
        JTableHeader header = t_datos.getTableHeader();
        header.setForeground(Color.white);
   }
    
       public void tabla_tamaños1()
   {
        TableColumnModel col_model = t_datos1.getColumnModel();
        DefaultTableCellRenderer tcr = new DefaultTableCellRenderer();
        tcr.setHorizontalAlignment(SwingConstants.RIGHT);

        for (int i=0; i<t_datos1.getColumnCount(); i++)
        {
  	      TableColumn column = col_model.getColumn(i);
              switch(i)
              {
                  case 0://pedido
                      column.setPreferredWidth(15);
                      column.setCellRenderer(tcr);
                      break;
                  case 1://NP
                      column.setPreferredWidth(15);
                      column.setCellRenderer(tcr);
                      break;
                  case 2://proveedor
                      column.setPreferredWidth(200);
                      column.setCellRenderer(tcr);
                      break;
                      
                  case 3://Cant
                      column.setPreferredWidth(70);
                      break;
                      
                  default:
                      column.setPreferredWidth(20);
                      break;
              }
        }
        JTableHeader header = t_datos1.getTableHeader();
        header.setForeground(Color.white);
   }
    
      
    private void cargaCotizaciones()
    {
        h=new Herramientas(user, 0);
        h.session(sessionPrograma);
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction().begin();
        List misCotizaciones=null;
        
         Query query = session.createQuery("SELECT DISTINCT cot FROM Cotizacion cot "
                 + "LEFT JOIN FETCH cot.partidaCotizacions partC "
                 + "LEFT JOIN partC.partida part "
                 + "LEFT JOIN part.ordenByIdOrden ord "
                 + "where ord.idOrden="+orden);
         misCotizaciones = query.list();
        //misCotizaciones=c.addOrder(Order.asc("idCotizacion")).list();
        
        if(misCotizaciones.size()>0)
        {
            t_datos1.setModel(ModeloTablaReporte1(misCotizaciones.size(), columnas1));
            for(int i=0; i<misCotizaciones.size(); i++)
            {
                Cotizacion Part = (Cotizacion) misCotizaciones.get(i);
                model1.setValueAt(Part.getIdCotizacion(), i, 0);
                model1.setValueAt(Part.getProveedor().getIdProveedor(), i, 1);
                model1.setValueAt(Part.getProveedor().getNombre(), i, 2);
                model1.setValueAt(Part.getFechaEnvio(), i, 3);
            }
        }
        else
            t_datos1.setModel(ModeloTablaReporte1(0, columnas1));
        formatoTabla1();
        if(session!=null)
            if(session.isOpen())
                session.close();
    }
       
    private void buscaDato()
    {
        h=new Herramientas(user, 0);
        h.session(sessionPrograma);
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction().begin();
        List cuentas=null;
        switch(c_filtro.getSelectedItem().toString())
        {
            case "Todos":
                cuentas= session.createCriteria(Partida.class).add(Restrictions.eq("ordenByIdOrden.idOrden", Integer.parseInt(orden))).add(Restrictions.eq("autorizadoValuacion", true)).add(Restrictions.eq("refCoti", true)).addOrder(Order.asc("idEvaluacion")).addOrder(Order.asc("subPartida")).list();
                break;
                        
            case "Hojalateria":
                cuentas= session.createCriteria(Partida.class).add(Restrictions.eq("ordenByIdOrden.idOrden", Integer.parseInt(orden))).add(Restrictions.eq("espHoj", true)).add(Restrictions.eq("autorizadoValuacion", true)).add(Restrictions.eq("refCoti", true)).addOrder(Order.asc("idEvaluacion")).addOrder(Order.asc("subPartida")).list();
                break;
                        
            case "Mecanica":
                cuentas= session.createCriteria(Partida.class).add(Restrictions.eq("ordenByIdOrden.idOrden", Integer.parseInt(orden))).add(Restrictions.eq("espMec", true)).add(Restrictions.eq("autorizadoValuacion", true)).add(Restrictions.eq("refCoti", true)).addOrder(Order.asc("idEvaluacion")).addOrder(Order.asc("subPartida")).list();
                break;
                        
            case "Suspension":
                cuentas= session.createCriteria(Partida.class).add(Restrictions.eq("ordenByIdOrden.idOrden", Integer.parseInt(orden))).add(Restrictions.eq("espSus", true)).add(Restrictions.eq("autorizadoValuacion", true)).add(Restrictions.eq("refCoti", true)).addOrder(Order.asc("idEvaluacion")).addOrder(Order.asc("subPartida")).list();
                break;
                        
            case "Electricidad":
                cuentas= session.createCriteria(Partida.class).add(Restrictions.eq("ordenByIdOrden.idOrden", Integer.parseInt(orden))).add(Restrictions.eq("espEle", true)).add(Restrictions.eq("autorizadoValuacion", true)).add(Restrictions.eq("refCoti", true)).addOrder(Order.asc("idEvaluacion")).addOrder(Order.asc("subPartida")).list();
                break;
        }
        
        if(cuentas.size()>0)
        {
            t_datos.setModel(ModeloTablaReporte(cuentas.size(), columnas));
            for(int i=0; i<cuentas.size(); i++)
            {
                Partida Part = (Partida) cuentas.get(i);
                noPartida.add(Part.getIdPartida());
                model.setValueAt(Part.getIdEvaluacion(), i, 0);
                model.setValueAt(Part.getSubPartida(), i, 1);
                model.setValueAt(Part.getCant(), i, 2);
                if(Part.getEjemplar()!=null)
                    model.setValueAt(Part.getEjemplar().getIdParte(), i, 3);
                else
                    model.setValueAt("", i, 3);
                model.setValueAt(Part.getCatalogo().getNombre(), i, 4);
                model.setValueAt("", i, 5);
                model.setValueAt("", i, 6);
                model.setValueAt("", i, 7);
            }
        }
        else
            t_datos.setModel(ModeloTablaReporte(0, columnas));
        this.formatoTabla();
        if(session!=null)
            if(session.isOpen())
                session.close();
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        numeros = new javax.swing.JComboBox();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        c_filtro = new javax.swing.JComboBox();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        t_busca = new javax.swing.JTextField();
        b_mail = new javax.swing.JButton();
        b_busca = new javax.swing.JButton();
        b_proveedor = new javax.swing.JButton();
        b_duplica = new javax.swing.JButton();
        b_busca4 = new javax.swing.JButton();
        b_busca5 = new javax.swing.JButton();
        b_elimina = new javax.swing.JButton();
        b_busca7 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        scroll1 = new javax.swing.JScrollPane();
        t_datos1 = new javax.swing.JTable();
        scroll = new javax.swing.JScrollPane();
        t_datos = new javax.swing.JTable();

        numeros.setFont(new java.awt.Font("Dialog", 0, 9)); // NOI18N
        numeros.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                numerosFocusLost(evt);
            }
        });

        setLayout(new java.awt.BorderLayout());

        jPanel2.setLayout(new java.awt.BorderLayout());

        c_filtro.setFont(new java.awt.Font("Arial", 0, 9)); // NOI18N
        c_filtro.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Todos", "Hojalateria", "Mecanica", "Suspension", "Electricidad" }));
        c_filtro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                c_filtroActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Arial", 0, 9)); // NOI18N
        jLabel1.setText("Especialidad:");

        jLabel2.setFont(new java.awt.Font("Arial", 0, 9)); // NOI18N
        jLabel2.setText("Buscar:");

        t_busca.setFont(new java.awt.Font("Arial", 0, 9)); // NOI18N
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

        b_mail.setBackground(new java.awt.Color(2, 135, 242));
        b_mail.setIcon(new ImageIcon("imagenes/mail.png"));
        b_mail.setToolTipText("Enviar cotizacion por email");
        b_mail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_mailActionPerformed(evt);
            }
        });

        b_busca.setBackground(new java.awt.Color(2, 135, 242));
        b_busca.setIcon(new ImageIcon("imagenes/buscar1.png"));
        b_busca.setToolTipText("Busca una partida");
        b_busca.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_buscaActionPerformed(evt);
            }
        });

        b_proveedor.setBackground(new java.awt.Color(2, 135, 242));
        b_proveedor.setIcon(new ImageIcon("imagenes/boton_mas_PROV.png"));
        b_proveedor.setToolTipText("Agrega un proveedor");
        b_proveedor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_proveedorActionPerformed(evt);
            }
        });

        b_duplica.setBackground(new java.awt.Color(2, 135, 242));
        b_duplica.setIcon(new ImageIcon("imagenes/REP.png"));
        b_duplica.setToolTipText("Duplica proveedor");
        b_duplica.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_duplicaActionPerformed(evt);
            }
        });

        b_busca4.setBackground(new java.awt.Color(2, 135, 242));
        b_busca4.setIcon(new ImageIcon("imagenes/boton_menos.png"));
        b_busca4.setToolTipText("Eliminar cotización");
        b_busca4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_busca4ActionPerformed(evt);
            }
        });

        b_busca5.setBackground(new java.awt.Color(2, 135, 242));
        b_busca5.setIcon(new ImageIcon("imagenes/xls_icon.png"));
        b_busca5.setToolTipText("Exportar a exel");
        b_busca5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_busca5ActionPerformed(evt);
            }
        });

        b_elimina.setBackground(new java.awt.Color(2, 135, 242));
        b_elimina.setIcon(new ImageIcon("imagenes/boton_menos.png"));
        b_elimina.setToolTipText("Elimina un proveedor");
        b_elimina.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_eliminaActionPerformed(evt);
            }
        });

        b_busca7.setBackground(new java.awt.Color(2, 135, 242));
        b_busca7.setForeground(new java.awt.Color(255, 255, 255));
        b_busca7.setText("Generar Cotización");
        b_busca7.setToolTipText("Generar cotizaciones");
        b_busca7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_busca7ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(jLabel1)
                .addGap(15, 15, 15)
                .addComponent(c_filtro, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(jLabel2)
                .addGap(8, 8, 8)
                .addComponent(t_busca, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(b_busca, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(65, 65, 65)
                .addComponent(b_proveedor, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(b_duplica, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(b_elimina, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(b_busca7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 190, Short.MAX_VALUE)
                .addComponent(b_mail, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(b_busca5, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(8, 8, 8)
                .addComponent(b_busca4, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(b_busca4, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(5, 5, 5)
                        .addComponent(jLabel1))
                    .addComponent(c_filtro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(5, 5, 5)
                        .addComponent(jLabel2))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(t_busca, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(b_busca, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(b_proveedor, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(b_duplica, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(b_mail, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(b_busca5, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(b_elimina, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(b_busca7, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jPanel2.add(jPanel3, java.awt.BorderLayout.PAGE_START);

        t_datos1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "NP", "Proveedor", "Fecha"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        t_datos1.setCellSelectionEnabled(true);
        t_datos1.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        t_datos1.getTableHeader().setReorderingAllowed(false);
        scroll1.setViewportView(t_datos1);

        scroll.setOpaque(false);

        t_datos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "No", "#", "Cant", "Codigo", "Descripción", "Prov1", "Prov2", "Prov3"
            }
        ));
        t_datos.setCellSelectionEnabled(true);
        t_datos.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        t_datos.getTableHeader().setReorderingAllowed(false);
        t_datos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                t_datosMouseClicked(evt);
            }
        });
        scroll.setViewportView(t_datos);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(scroll, javax.swing.GroupLayout.DEFAULT_SIZE, 599, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(scroll1, javax.swing.GroupLayout.DEFAULT_SIZE, 423, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(scroll, javax.swing.GroupLayout.DEFAULT_SIZE, 206, Short.MAX_VALUE)
                    .addComponent(scroll1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addGap(1, 1, 1))
        );

        jPanel2.add(jPanel1, java.awt.BorderLayout.CENTER);

        add(jPanel2, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void t_buscaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_t_buscaActionPerformed
        // TODO add your handling code here:
        h=new Herramientas(user, 0);
        h.session(sessionPrograma);
        if(this.t_busca.getText().compareToIgnoreCase("")!=0)
        {
            if(x>=t_datos.getRowCount())
                x=0;
            for(; x<t_datos.getRowCount(); x++)
            {
                if(t_datos.getValueAt(x, 4).toString().indexOf(t_busca.getText()) != -1)
                {
                    t_datos.setRowSelectionInterval(x, x);
                    t_datos.setColumnSelectionInterval(4, 4);
                    break;
                }
            }
            x++;
        }
    }//GEN-LAST:event_t_buscaActionPerformed

    private void t_buscaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_buscaKeyTyped
        // TODO add your handling code here:
        char car = evt.getKeyChar();
        evt.setKeyChar(Character.toUpperCase(evt.getKeyChar()));
    }//GEN-LAST:event_t_buscaKeyTyped

    private void b_mailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_mailActionPerformed
        // TODO add your handling code here:
        h=new Herramientas(user, 0);
        h.session(sessionPrograma);
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction().begin();
        
        if(t_datos1.getSelectedRow()>-1)
        {
            exel();
            Cotizacion miCotizacion=(Cotizacion)session.get(Cotizacion.class, Integer.parseInt(t_datos1.getValueAt(t_datos1.getSelectedRow(), 0).toString()));
            String mail="";
            if(miCotizacion.getProveedor().getEmail()!=null)
                mail=miCotizacion.getProveedor().getEmail();
            EnviarCorreo en= new EnviarCorreo(new javax.swing.JFrame(), true, mail,"Envío información para cotizacion", "Hola buen dia, envío esta lista para cotizacion de precios y existencias gracias.", this.archivoXLS, this.user, this.sessionPrograma);
            Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
            en.setLocation((d.width/2)-(en.getWidth()/2), (d.height/2)-(en.getHeight()/2));
            en.setVisible(true);
        }
        else
            JOptionPane.showMessageDialog(null, "¡Debes seleccionar primero una cotización de la tabla!");
        if(session!=null)
            if(session.isOpen())
                session.close();
    }//GEN-LAST:event_b_mailActionPerformed

    private void b_buscaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_buscaActionPerformed
        // TODO add your handling code here:
        h=new Herramientas(user, 0);
        h.session(sessionPrograma);
        if(this.t_busca.getText().compareToIgnoreCase("")!=0)
        {
            if(x>=t_datos.getRowCount())
                x=0;
            for(; x<t_datos.getRowCount(); x++)
            {
                if(t_datos.getValueAt(x, 4).toString().indexOf(t_busca.getText()) != -1)
                {
                    t_datos.setRowSelectionInterval(x, x);
                    t_datos.setColumnSelectionInterval(4, 4);
                    break;
                }
            }
            x++;
        }
    }//GEN-LAST:event_b_buscaActionPerformed

    private void b_proveedorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_proveedorActionPerformed
        // TODO add your handling code here:
        h=new Herramientas(user, 0);
        h.session(sessionPrograma);

        if(t_datos.getSelectedRow()>-1)
        {
            if(t_datos.getSelectedColumn()>4 && t_datos.getSelectedColumn()<8)
            {
                buscaProveedor obj = new buscaProveedor(new javax.swing.JFrame(), true, this.user, this.sessionPrograma,0);
                obj.t_busca.requestFocus();
                Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
                obj.setLocation((d.width/2)-(obj.getWidth()/2), (d.height/2)-(obj.getHeight()/2));
                obj.setVisible(true);

                Proveedor prov=obj.getReturnStatus();
                if (prov!=null)
                    t_datos.setValueAt(""+prov.getIdProveedor(), t_datos.getSelectedRow(), t_datos.getSelectedColumn());
                else
                    t_datos.setValueAt("", t_datos.getSelectedRow(), t_datos.getSelectedColumn());
            }
            else
            {
                JOptionPane.showMessageDialog(null, "¡Debes seleccionar una columna de algun proveedor(1,2,3) primero!");
            }
        }
        else
        {
            JOptionPane.showMessageDialog(null, "¡Debes seleccionar primero una partida de la tabla!");
        }
    }//GEN-LAST:event_b_proveedorActionPerformed

    private void b_duplicaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_duplicaActionPerformed
        // TODO add your handling code here:
        if(t_datos.getSelectedRow()>-1)
        {
            if(t_datos.getSelectedColumn()>4 && t_datos.getSelectedColumn()<8)
            {
                if(t_datos.getValueAt(t_datos.getSelectedRow(), t_datos.getSelectedColumn()).toString().compareTo("")!=0)
                {
                    for(int x=0; x<t_datos.getRowCount(); x++)
                    {
                        t_datos.setValueAt(t_datos.getValueAt(t_datos.getSelectedRow(), t_datos.getSelectedColumn()), x, t_datos.getSelectedColumn());
                    }
                }
                else
                    JOptionPane.showMessageDialog(null, "¡No se puede duplicar un proveedor vacío selecciono otro!");
            }
            else
            {
                JOptionPane.showMessageDialog(null, "¡Debes seleccionar una columna de algun proveedor(1,2,3) primero!");
            }
        }
        else
        {
            JOptionPane.showMessageDialog(null, "¡Debes seleccionar primero una partida de la tabla!");
        }
    }//GEN-LAST:event_b_duplicaActionPerformed

    private void c_filtroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_c_filtroActionPerformed
        // TODO add your handling code here:
        buscaDato();
    }//GEN-LAST:event_c_filtroActionPerformed

    private void b_busca4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_busca4ActionPerformed
        // TODO add your handling code here:
        if(t_datos1.getSelectedRow()>-1)
        {
            Session session = HibernateUtil.getSessionFactory().openSession();
            try
            {
                
                session.beginTransaction().begin();
                Cotizacion cot=(Cotizacion)session.get(Cotizacion.class, Integer.parseInt(t_datos1.getValueAt(t_datos1.getSelectedRow(), 0).toString()));
                session.delete(cot);
                session.beginTransaction().commit();
                this.cargaCotizaciones();
            }
            catch(Exception e)
            {
                session.beginTransaction().rollback();
                e.printStackTrace();
                if(session.isOpen())
                    session.close();
                JOptionPane.showMessageDialog(null, "¡Error al eliminar el pedido!");
            }
            if(session!=null)
            if(session.isOpen())
                session.close();
        }
        else
            JOptionPane.showMessageDialog(null, "¡Debes seleccionar primero una cotizacion de la tabla!");
    }//GEN-LAST:event_b_busca4ActionPerformed

    private void b_eliminaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_eliminaActionPerformed
        // TODO add your handling code here:
        if(t_datos.getSelectedRow()>-1)
        {
            if(t_datos.getSelectedColumn()>4 && t_datos.getSelectedColumn()<8)
            {
                t_datos.setValueAt("", t_datos.getSelectedRow(), t_datos.getSelectedColumn());
            }
            else
            {
                JOptionPane.showMessageDialog(null, "¡Debes seleccionar una columna de algun proveedor(1,2,3) primero!");
            }
        }
        else
        {
            JOptionPane.showMessageDialog(null, "¡Debes seleccionar primero una partida de la tabla!");
        }
    }//GEN-LAST:event_b_eliminaActionPerformed

    private void b_busca7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_busca7ActionPerformed
        // TODO add your handling code here:
        if(t_datos.getRowCount()>0)
        {
            //**************buscamos los diferentes proveedores
            cotizaciones=new ArrayList();
            for(int ren = 0; ren < t_datos.getRowCount(); ren++)
            {
                for(int col=5; col<t_datos.getColumnCount(); col++)
                {
                    if(t_datos.getValueAt(ren, col).toString().compareTo("")!=0)
                    {
                        exite(t_datos.getValueAt(ren, col).toString(), noPartida.get(ren).toString());
                    }
                }
            }
            
            Session session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction().begin();
            int error=0;
            Date fechaCotizacion = new Date();
            DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                        String valor=dateFormat.format(fechaCotizacion);
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
                        
            for(int cot=0; cot<cotizaciones.size(); cot++)
            {
                List proveedores=(ArrayList)cotizaciones.get(cot);
                try
                {
                    Cotizacion registro=new Cotizacion();
                    Proveedor prov=(Proveedor)session.get(Proveedor.class, Integer.parseInt(proveedores.get(0).toString()));
                    registro.setProveedor(prov);
                    registro.setUsuarioByIdUsuario(user);
                    registro.setFechaEnvio(calendario.getTime());
                    for(int h=1; h<proveedores.size(); h++)
                    {
                        Partida par=(Partida)session.get(Partida.class, Integer.parseInt(proveedores.get(h).toString()));
                        PartidaCotizacion parCot=new PartidaCotizacion(par, registro, 0f, false);
                        registro.agregaPartida(parCot);
                    }
                    int num=(int)session.save(registro);
                }
                catch(Exception e)
                {
                    error=1;
                    e.printStackTrace();
                }
            }
            if(error==0)
            {
                session.beginTransaction().commit();
                JOptionPane.showMessageDialog(null, "¡Listo!");
            }
            else
            {
                session.beginTransaction().rollback();
                JOptionPane.showMessageDialog(null, "¡Algunas cotizaciones no se pudieron realizar!");
            }
            if(session!=null)
            if(session.isOpen())
                session.close();
        }
        else
        {
            JOptionPane.showMessageDialog(null, "¡No hay partidas para generar cotizacioines!");
        }
         formatoTabla1();
         cargaCotizaciones();
    }//GEN-LAST:event_b_busca7ActionPerformed

    private void b_busca5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_busca5ActionPerformed
        // TODO add your handling code here:
        if(t_datos1.getSelectedRow()>-1)
        {
            exel();
        }
        else
            JOptionPane.showMessageDialog(null, "¡No hay una cotización seleccionada!");
    }//GEN-LAST:event_b_busca5ActionPerformed

    private void t_datosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_t_datosMouseClicked
        // TODO add your handling code here:
        /*if(t_datos.getSelectedRow()>=0)
        {
            if(t_datos.getSelectedColumn()==3)
            {
                numeros.removeAllItems();
                numeros.addItem("S/C");
                numeros.setSelectedItem("S/C");
                Session session = HibernateUtil.getSessionFactory().openSession();
                try
                {
                    session.beginTransaction().begin();
                    Partida partida=(Partida) session.createCriteria(Partida.class).add(Restrictions.eq("ordenByIdOrden.idOrden", Integer.parseInt(orden))).add(Restrictions.eq("idEvaluacion", Integer.parseInt(t_datos.getValueAt(t_datos.getSelectedRow(), 0).toString()))).add(Restrictions.eq("subPartida", Integer.parseInt(t_datos.getValueAt(t_datos.getSelectedRow(), 1).toString()))).setMaxResults(1).uniqueResult();
                    Ejemplar[] codigos = (Ejemplar[]) partida.getCatalogo().getEjemplars().toArray(new Ejemplar[0]);
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
                     System.out.println(e);
                }
                if(session!=null)
                     if(session.isOpen()==true)
                         session.close();
            }
        }*/
    }//GEN-LAST:event_t_datosMouseClicked

    private void numerosFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_numerosFocusLost
        // TODO add your handling code here:
        entro=1;
    }//GEN-LAST:event_numerosFocusLost


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton b_busca;
    private javax.swing.JButton b_busca4;
    private javax.swing.JButton b_busca5;
    private javax.swing.JButton b_busca7;
    private javax.swing.JButton b_duplica;
    private javax.swing.JButton b_elimina;
    private javax.swing.JButton b_mail;
    private javax.swing.JButton b_proveedor;
    private javax.swing.JComboBox c_filtro;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JComboBox numeros;
    private javax.swing.JScrollPane scroll;
    private javax.swing.JScrollPane scroll1;
    private javax.swing.JTextField t_busca;
    private javax.swing.JTable t_datos;
    private javax.swing.JTable t_datos1;
    // End of variables declaration//GEN-END:variables

    void exite(String busca, String par)
    {
        int entro=0;
        for(int a=0; a<cotizaciones.size(); a++)
        {
            List proveedores=(ArrayList)cotizaciones.get(a);
            if(proveedores.get(0).toString().compareToIgnoreCase(busca)==0)
            {
                entro=1;
                int encontrado=0;
                for(int f=1; f<proveedores.size(); f++)
                {
                    if(proveedores.get(f).toString().compareTo(par)==0)
                    {
                        encontrado=1;
                        break;
                    }
                }
                if(encontrado==0)
                    proveedores.add(par);
            }
        }
        if(entro==0)
        {
            List proveedores=new ArrayList();
            proveedores.add(busca);
            proveedores.add(par);
            cotizaciones.add(proveedores);
        }
    }
    
    void exel()
    {
        h=new Herramientas(this.user, 0);
        h.session(sessionPrograma);
        javax.swing.JFileChooser jF1= new javax.swing.JFileChooser(); 
        jF1.setFileFilter(new ExtensionFileFilter("Excel document (*.xls)", new String[] { "xls" }));
        String ruta = null; 
        if(jF1.showSaveDialog(null)==jF1.APPROVE_OPTION)
        { 
            ruta = jF1.getSelectedFile().getAbsolutePath(); 
            if(ruta!=null)
            {
                if(ruta.endsWith(".xls")==true)
                    archivoXLS = new File(ruta);
                else
                    archivoXLS = new File(ruta+".xls");
                try
                {
                    if(archivoXLS.exists())
                        archivoXLS.delete();
                    archivoXLS.createNewFile();
                    Workbook libro = new HSSFWorkbook();
                    FileOutputStream archivo = new FileOutputStream(archivoXLS);
                    Sheet hoja = libro.createSheet("Cotizacion");
                    Font font = libro.createFont();
                    font.setFontHeightInPoints((short)24);
                    font.setFontName("Arial");
                    font.setItalic(false);
                    font.setBold(true);

                    Font font10 = libro.createFont();
                    font10.setFontHeightInPoints((short)10);
                    font10.setFontName("Arial");
                    font10.setItalic(false);
                    font10.setBold(false);
                    font10.setColor(new HSSFColor.YELLOW().getIndex());
                    
                    Font font11 = libro.createFont();
                    font11.setFontHeightInPoints((short)10);
                    font11.setFontName("Arial");
                    font10.setItalic(false);
                    font10.setBold(false);
                    font11.setColor(new HSSFColor.BLACK().getIndex());
                    
                    // Fonts are set into a style so create a new one to use.
                    CellStyle style = libro.createCellStyle();
                    CellStyle desBloqueo = libro.createCellStyle();
                    CellStyle desBloqueo1 = libro.createCellStyle();
                    CellStyle desBloqueoFecha = libro.createCellStyle();
                    
                    style.setFont(font);
                    
                    desBloqueo.setFont(font10);
                    desBloqueo.setLocked(false);
                    desBloqueo.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                    desBloqueo.setFillBackgroundColor(new HSSFColor.GREEN().getIndex());
                    
                    desBloqueo1.setFont(font11);
                    desBloqueo1.setLocked(false);
                    desBloqueo1.setFillBackgroundColor(new HSSFColor.WHITE().getIndex());
                    
                    desBloqueoFecha.setFont(font10);
                    desBloqueoFecha.setLocked(false);
                    desBloqueoFecha.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                    desBloqueoFecha.setFillBackgroundColor(new HSSFColor.GREEN().getIndex());
                    desBloqueoFecha.setDataFormat(HSSFDataFormat.getBuiltinFormat("m/d/yy"));
                    
                    Session session = HibernateUtil.getSessionFactory().openSession();
                    session.beginTransaction().begin();
                    Orden ord=(Orden)session.get(Orden.class, Integer.parseInt(orden));
                    Configuracion con= (Configuracion)session.get(Configuracion.class, configuracion);
                    
                    hoja.setColumnWidth(5, 15000);
                    Row r0=hoja.createRow(0);
                    Cell celdaTitulo=r0.createCell(0);
                    celdaTitulo.setCellValue(con.getEmpresa());
                    celdaTitulo.setCellStyle(style);
                    
                    Row r1=hoja.createRow(1);
                    r1.createCell(6).setCellValue("Orden de Taller:");
                    r1.createCell(7).setCellValue(orden);
                    
                    Row r2=hoja.createRow(2);
                    r2.createCell(0).setCellValue("Marca:");
                    r2.createCell(1).setCellValue(ord.getMarca().getMarcaNombre());
                    r2.createCell(6).setCellValue("Nº Serie:");
                    r2.createCell(7).setCellValue(ord.getNoSerie());
                    
                    Row r3=hoja.createRow(3);
                    r3.createCell(0).setCellValue("Tipo:");
                    r3.createCell(1).setCellValue(ord.getTipo().getTipoNombre());
                    r3.createCell(6).setCellValue("Nº Motor:");
                    r3.createCell(7).setCellValue(ord.getNoMotor());
                    
                    Row r4=hoja.createRow(4);
                    r4.createCell(0).setCellValue("NP:");
                    r4.createCell(1).setCellValue(t_datos1.getValueAt(t_datos1.getSelectedRow(), 1).toString());
                    r4.createCell(2).setCellValue("Proveedor:");
                    r4.createCell(3).setCellValue(t_datos1.getValueAt(t_datos1.getSelectedRow(), 2).toString());
                    r4.createCell(6).setCellValue("Modelo:");
                    r4.createCell(7).setCellValue(""+ord.getModelo());

                    Row r5=hoja.createRow(5);
                    r5.createCell(0).setCellValue("**********************************************************************************[Nota:  Solo puedes editar las celdas de color]*******************************************************************************");
                    
                    Row r6=hoja.createRow(6);
                    r6.createCell(0).setCellValue("Partida");
                    r6.createCell(1).setCellValue("sub");
                    r6.createCell(2).setCellValue("Cantidad");
                    r6.createCell(3).setCellValue("U/Medida");
                    r6.createCell(4).setCellValue("Nº Parte");
                    r6.createCell(5).setCellValue("Descripcion");
                    r6.createCell(6).setCellValue("Instrucción");
                    r6.createCell(7).setCellValue("Precio c/u");
                    r6.createCell(8).setCellValue("T o t a l");
                    r6.createCell(9).setCellValue("Origen");
                    r6.createCell(10).setCellValue("Pazo");
                    
                    Row r7=hoja.createRow(7);
                    r7.createCell(0).setCellValue("**********************************************************************************************************************************************************************************************************************");
                    
                    List misCotizaciones=null;

                     Query query = session.createQuery("SELECT DISTINCT par FROM Partida par "
                             + "RIGHT JOIN FETCH par.partidaCotizacions partC "
                             + "RIGHT JOIN partC.cotizacion cot "
                             + "where cot.idCotizacion="+t_datos1.getValueAt(t_datos1.getSelectedRow(), 0).toString()+" order by par.idEvaluacion asc, par.subPartida asc");
                     //misCotizaciones=c.addOrder(Order.asc("idCotizacion")).list();
                     misCotizaciones = query.list();

                    if(misCotizaciones.size()>0)
                    {
                        
                        for(int i=0; i<misCotizaciones.size(); i++)
                        {
                            Partida Part = (Partida) misCotizaciones.get(i);
                            Row fila = hoja.createRow(i+8);
                            
                            fila.createCell(0).setCellValue(Part.getIdEvaluacion());
                            fila.createCell(1).setCellValue(Part.getSubPartida());
                            fila.createCell(2).setCellValue(Part.getCant());
                            fila.createCell(3).setCellValue(Part.getMed());
                            
                            Cell aux=fila.createCell(4);
                            aux.setCellStyle(desBloqueo);
                            if(Part.getEjemplar()!=null)
                                aux.setCellValue(Part.getEjemplar().getIdParte());
                            else
                                aux.setCellValue("");
                                    
                            fila.createCell(5).setCellValue(Part.getCatalogo().getNombre());
                            int fil=i+9;
                            Cell a10 = fila.createCell(6);
                            a10.setCellStyle(desBloqueo1);
                            a10.setCellValue(Part.getInstruccion());
                            
                            Cell a6=fila.createCell(7);
                            a6.setCellStyle(desBloqueo);
                            a6.setCellValue("");
                            Cell celForm= fila.createCell(8);
                            celForm.setCellType(HSSFCell.CELL_TYPE_FORMULA);
                            celForm.setCellFormula("H"+fil+"*C"+fil);
                            
                            if(Part.isOri()==true)
                                fila.createCell(9).setCellValue("Ori");
                            else
                                if(Part.isNal()==true)
                                    fila.createCell(9).setCellValue("Nal");
                                else
                                    if(Part.isDesm()==true)
                                        fila.createCell(9).setCellValue("Des");
                                    else
                                        fila.createCell(9).setCellValue("");
                            Cell a9=fila.createCell(10);
                            a9.setCellValue("");
                            a9.setCellStyle(desBloqueo);
                        }
                    }
                    
                    hoja.protectSheet("04650077");
                    libro.write(archivo);
                    archivo.close();
                    Desktop.getDesktop().open(archivoXLS);
                    if(session!=null)
                        if(session.isOpen())
                            session.close();
                }catch(Exception e)
                {
                    System.out.println(e);
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(this, "No se pudo realizar el reporte si el archivo esta abierto");
                }
            }
        }
    }
    void bloquea(boolean edo)
    {
        c_filtro.setEnabled(edo);
        t_busca.setEnabled(edo);
        b_busca.setEnabled(edo);
        b_proveedor.setEnabled(edo);
        b_duplica.setEnabled(edo);
        b_elimina.setEnabled(edo);
        b_busca7.setEnabled(edo);
        b_mail.setEnabled(edo);
        b_busca5.setEnabled(edo);
        b_busca4.setEnabled(edo);
    }
}
