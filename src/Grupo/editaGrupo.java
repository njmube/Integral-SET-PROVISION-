/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Grupo;

import Catalogo.buscaCatalogo;
import Estatus.*;
import Hibernate.Util.HibernateUtil;
import Hibernate.entidades.Catalogo;
import Hibernate.entidades.Estatus;
import Hibernate.entidades.Item;
import Hibernate.entidades.Posicion;
import Hibernate.entidades.Servicio;
import Hibernate.entidades.Usuario;
import Integral.FormatoEditor;
import Integral.FormatoTabla;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.List;
import java.util.Vector;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import Integral.Herramientas;
import Integral.Render1;
import javax.swing.DefaultCellEditor;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
/**
 *
 * @author ESPECIALIZADO TOLUCA
 */
public class editaGrupo extends javax.swing.JPanel {   
    private Servicio servicio;
    //private Session session;
    DefaultTableModel model;
    DefaultTableModel mItem;
    String[] columnas = new String [] {"Nombre","Descripción"}; 
    String[] columnas1 = new String [] {"ID","Descripción", "Grupo Mecánico", "I.DM", "Cam", "Rep. Min", "Rep. Med", "Rep. Max", "Pin. Min", "Pin. Med", "Pin. Max","ubicacion"}; 
    public String nb = "";
    public String ds = "";
    Usuario usr;
    String sessionPrograma="";
    Herramientas h;
    FormatoTabla formato;

    public editaGrupo(Usuario usuario, String ses) {
        initComponents();
        formato = new FormatoTabla();
        sessionPrograma=ses;
        usr=usuario;
        ubicaciones();
        formatoTablas();
        buscaDato();
    }

    DefaultTableModel ModeloTablaReporte(int renglones, String columnas[])
    {
        model = new DefaultTableModel(new Object [renglones][2], columnas)
        {
            Class[] types = new Class [] {
                java.lang.String.class, 
                java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false,false
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
        return model;
    }
    DefaultTableModel ModeloTablaConcepto(int renglones, String columnas[])
    {
        mItem = new DefaultTableModel(new Object [renglones][12], columnas1)
        {
            Class[] types = new Class [] {
                java.lang.Integer.class,
                java.lang.String.class,
                java.lang.String.class,
                java.lang.Double.class,
                java.lang.Double.class,
                java.lang.Double.class,
                java.lang.Double.class,
                java.lang.Double.class,
                java.lang.Double.class,
                java.lang.Double.class,
                java.lang.Double.class,
                java.lang.String.class,
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, true, true, true, true, true, true, true, true, true
            };
            
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
                            Session session = HibernateUtil.getSessionFactory().openSession();
                            try
                            {
                                session.beginTransaction().begin();
                                usr = (Usuario)session.get(Usuario.class, usr.getIdUsuario());
                                if(usr.getRespaldar()==true)
                                {
                                    try
                                    {
                                        Item part=(Item) session.get(Item.class, Integer.parseInt(t_conceptos.getValueAt(row, 0).toString()));
                                        if(part!=null)
                                        {
                                            Item[] partidas=(Item[]) session.createCriteria(Item.class).add(Restrictions.eq("catalogo.idCatalogo", part.getCatalogo().getIdCatalogo())).list().toArray(new Item[0]);
                                            for(int a=0; a< partidas.length; a++)
                                            {
                                                partidas[a].setIntDesm((double)value);
                                                session.update(partidas[a]);
                                            }
                                            session.getTransaction().commit();
                                            vector.setElementAt(value, col);
                                            this.dataVector.setElementAt(vector, row);
                                            fireTableCellUpdated(row, col);
                                            if(session.isOpen()==true)
                                                session.close();
                                        }
                                        else
                                        {
                                            JOptionPane.showMessageDialog(null, "La partida ya no existe");
                                            buscaItems();
                                        }
                                    }
                                    catch(Exception e)
                                    {
                                        session.getTransaction().rollback();
                                        System.out.println(e);
                                        JOptionPane.showMessageDialog(null, "Error al actualizar los datos"); 
                                    }
                                    finally
                                    {
                                        if(session.isOpen()==true)
                                            session.close();
                                    }
                                }
                                else
                                {
                                    JOptionPane.showMessageDialog(null, "Acceso denegado"); 
                                }
                            }catch(Exception e)
                            {
                                System.out.println(e);
                            }
                            if(session!=null)
                                if(session.isOpen()==true)
                                    session.close();
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
                            Session session = HibernateUtil.getSessionFactory().openSession();
                            try
                            {
                                session.beginTransaction().begin();
                                usr = (Usuario)session.get(Usuario.class, usr.getIdUsuario());
                                if(usr.getRespaldar()==true)
                                {
                                    try
                                    {
                                        Item part=(Item) session.get(Item.class, Integer.parseInt(t_conceptos.getValueAt(row, 0).toString()));
                                        if(part!=null)
                                        {
                                            Item[] partidas=(Item[]) session.createCriteria(Item.class).add(Restrictions.eq("catalogo.idCatalogo", part.getCatalogo().getIdCatalogo())).list().toArray(new Item[0]);
                                            for(int a=0; a< partidas.length; a++)
                                            {
                                                partidas[a].setIntCamb((double)value);
                                                session.update(partidas[a]);
                                            }
                                            session.getTransaction().commit();
                                            vector.setElementAt(value, col);
                                            this.dataVector.setElementAt(vector, row);
                                            fireTableCellUpdated(row, col);
                                            if(session.isOpen()==true)
                                                session.close();
                                        }
                                        else
                                        {
                                            JOptionPane.showMessageDialog(null, "La partida ya no existe");
                                            buscaItems();
                                        }
                                    }
                                    catch(Exception e)
                                    {
                                        session.getTransaction().rollback();
                                        System.out.println(e);
                                        JOptionPane.showMessageDialog(null, "Error al actualizar los datos"); 
                                    }
                                    finally
                                    {
                                        if(session.isOpen()==true)
                                            session.close();
                                    }
                                }
                                else
                                {
                                    JOptionPane.showMessageDialog(null, "Acceso denegado"); 
                                }
                            }catch(Exception e)
                            {
                                System.out.println(e);
                            }
                            if(session!=null)
                                if(session.isOpen()==true)
                                    session.close();
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
                                usr = (Usuario)session.get(Usuario.class, usr.getIdUsuario());
                                if(usr.getRespaldar()==true)
                                {
                                    try
                                    {
                                        Item part=(Item) session.get(Item.class, Integer.parseInt(t_conceptos.getValueAt(row, 0).toString()));
                                        if(part!=null)
                                        {
                                            Item[] partidas=(Item[]) session.createCriteria(Item.class).add(Restrictions.eq("catalogo.idCatalogo", part.getCatalogo().getIdCatalogo())).list().toArray(new Item[0]);
                                            for(int a=0; a< partidas.length; a++)
                                            {
                                                partidas[a].setIntRepMin((double)value);
                                                session.update(partidas[a]);
                                            }
                                            session.getTransaction().commit();
                                            vector.setElementAt(value, col);
                                            this.dataVector.setElementAt(vector, row);
                                            fireTableCellUpdated(row, col);
                                            if(session.isOpen()==true)
                                                session.close();
                                        }
                                        else
                                        {
                                            JOptionPane.showMessageDialog(null, "La partida ya no existe");
                                            buscaItems();
                                        }
                                    }
                                    catch(Exception e)
                                    {
                                        session.getTransaction().rollback();
                                        System.out.println(e);
                                        JOptionPane.showMessageDialog(null, "Error al actualizar los datos"); 
                                    }
                                    finally
                                    {
                                        if(session.isOpen()==true)
                                            session.close();
                                    }
                                }
                                else
                                {
                                    JOptionPane.showMessageDialog(null, "Acceso denegado"); 
                                }
                            }catch(Exception e)
                            {
                                System.out.println(e);
                            }
                            if(session!=null)
                                if(session.isOpen()==true)
                                    session.close();
                        }
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
                            Session session = HibernateUtil.getSessionFactory().openSession();
                            try
                            {
                                session.beginTransaction().begin();
                                usr = (Usuario)session.get(Usuario.class, usr.getIdUsuario());
                                if(usr.getRespaldar()==true)
                                {
                                    try
                                    {
                                        Item part=(Item) session.get(Item.class, Integer.parseInt(t_conceptos.getValueAt(row, 0).toString()));
                                        if(part!=null)
                                        {
                                            Item[] partidas=(Item[]) session.createCriteria(Item.class).add(Restrictions.eq("catalogo.idCatalogo", part.getCatalogo().getIdCatalogo())).list().toArray(new Item[0]);
                                            for(int a=0; a< partidas.length; a++)
                                            {
                                                partidas[a].setIntRepMed((double)value);
                                                session.update(partidas[a]);
                                            }
                                            session.getTransaction().commit();
                                            vector.setElementAt(value, col);
                                            this.dataVector.setElementAt(vector, row);
                                            fireTableCellUpdated(row, col);
                                            if(session.isOpen()==true)
                                                session.close();
                                        }
                                        else
                                        {
                                            JOptionPane.showMessageDialog(null, "La partida ya no existe");
                                            buscaItems();
                                        }
                                    }
                                    catch(Exception e)
                                    {
                                        session.getTransaction().rollback();
                                        System.out.println(e);
                                        JOptionPane.showMessageDialog(null, "Error al actualizar los datos"); 
                                    }
                                    finally
                                    {
                                        if(session.isOpen()==true)
                                            session.close();
                                    }
                                }
                                else
                                {
                                    JOptionPane.showMessageDialog(null, "Acceso denegado"); 
                                }
                            }catch(Exception e)
                            {
                                System.out.println(e);
                            }
                            if(session!=null)
                                if(session.isOpen()==true)
                                    session.close();
                        }
                        break;
                     
                    case 7:
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
                                usr = (Usuario)session.get(Usuario.class, usr.getIdUsuario());
                                if(usr.getRespaldar()==true)
                                {
                                    try
                                    {
                                        Item part=(Item) session.get(Item.class, Integer.parseInt(t_conceptos.getValueAt(row, 0).toString()));
                                        if(part!=null)
                                        {
                                            Item[] partidas=(Item[]) session.createCriteria(Item.class).add(Restrictions.eq("catalogo.idCatalogo", part.getCatalogo().getIdCatalogo())).list().toArray(new Item[0]);
                                            for(int a=0; a< partidas.length; a++)
                                            {
                                                partidas[a].setIntRepMax((double)value);
                                                session.update(partidas[a]);
                                            }
                                            session.getTransaction().commit();
                                            vector.setElementAt(value, col);
                                            this.dataVector.setElementAt(vector, row);
                                            fireTableCellUpdated(row, col);
                                            if(session.isOpen()==true)
                                                session.close();
                                        }
                                        else
                                        {
                                            JOptionPane.showMessageDialog(null, "La partida ya no existe");
                                            buscaItems();
                                        }
                                    }
                                    catch(Exception e)
                                    {
                                        session.getTransaction().rollback();
                                        System.out.println(e);
                                        JOptionPane.showMessageDialog(null, "Error al actualizar los datos"); 
                                    }
                                    finally
                                    {
                                        if(session.isOpen()==true)
                                            session.close();
                                    }
                                }
                                else
                                {
                                    JOptionPane.showMessageDialog(null, "Acceso denegado"); 
                                }
                            }catch(Exception e)
                            {
                                System.out.println(e);
                            }
                            if(session!=null)
                                if(session.isOpen()==true)
                                    session.close();
                        }
                        break;
                        
                    case 8:
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
                                usr = (Usuario)session.get(Usuario.class, usr.getIdUsuario());
                                if(usr.getRespaldar()==true)
                                {
                                    try
                                    {
                                        Item part=(Item) session.get(Item.class, Integer.parseInt(t_conceptos.getValueAt(row, 0).toString()));
                                        if(part!=null)
                                        {
                                            Item[] partidas=(Item[]) session.createCriteria(Item.class).add(Restrictions.eq("catalogo.idCatalogo", part.getCatalogo().getIdCatalogo())).list().toArray(new Item[0]);
                                            for(int a=0; a< partidas.length; a++)
                                            {
                                                partidas[a].setIntPinMin((double)value);
                                                session.update(partidas[a]);
                                            }
                                            session.getTransaction().commit();
                                            vector.setElementAt(value, col);
                                            this.dataVector.setElementAt(vector, row);
                                            fireTableCellUpdated(row, col);
                                            if(session.isOpen()==true)
                                                session.close();
                                        }
                                        else
                                        {
                                            JOptionPane.showMessageDialog(null, "La partida ya no existe");
                                            buscaItems();
                                        }
                                    }
                                    catch(Exception e)
                                    {
                                        session.getTransaction().rollback();
                                        System.out.println(e);
                                        JOptionPane.showMessageDialog(null, "Error al actualizar los datos"); 
                                    }
                                    finally
                                    {
                                        if(session.isOpen()==true)
                                            session.close();
                                    }
                                }
                                else
                                {
                                    JOptionPane.showMessageDialog(null, "Acceso denegado"); 
                                }
                            }catch(Exception e)
                            {
                                System.out.println(e);
                            }
                            if(session!=null)
                                if(session.isOpen()==true)
                                    session.close();
                        }
                        break;
                        
                    case 9:
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
                                usr = (Usuario)session.get(Usuario.class, usr.getIdUsuario());
                                if(usr.getRespaldar()==true)
                                {
                                    try
                                    {
                                        Item part=(Item) session.get(Item.class, Integer.parseInt(t_conceptos.getValueAt(row, 0).toString()));
                                        if(part!=null)
                                        {
                                            Item[] partidas=(Item[]) session.createCriteria(Item.class).add(Restrictions.eq("catalogo.idCatalogo", part.getCatalogo().getIdCatalogo())).list().toArray(new Item[0]);
                                            for(int a=0; a< partidas.length; a++)
                                            {
                                                partidas[a].setIntPinMed((double)value);
                                                session.update(partidas[a]);
                                            }
                                            session.getTransaction().commit();
                                            vector.setElementAt(value, col);
                                            this.dataVector.setElementAt(vector, row);
                                            fireTableCellUpdated(row, col);
                                            if(session.isOpen()==true)
                                                session.close();
                                        }
                                        else
                                        {
                                            JOptionPane.showMessageDialog(null, "La partida ya no existe");
                                            buscaItems();
                                        }
                                    }
                                    catch(Exception e)
                                    {
                                        session.getTransaction().rollback();
                                        System.out.println(e);
                                        JOptionPane.showMessageDialog(null, "Error al actualizar los datos"); 
                                    }
                                    finally
                                    {
                                        if(session.isOpen()==true)
                                            session.close();
                                    }
                                }
                                else
                                {
                                    JOptionPane.showMessageDialog(null, "Acceso denegado"); 
                                }
                            }catch(Exception e)
                            {
                                System.out.println(e);
                            }
                            if(session!=null)
                                if(session.isOpen()==true)
                                    session.close();
                        }
                        break;
                        
                    case 10:
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
                                usr = (Usuario)session.get(Usuario.class, usr.getIdUsuario());
                                if(usr.getRespaldar()==true)
                                {
                                    try
                                    {
                                        Item part=(Item) session.get(Item.class, Integer.parseInt(t_conceptos.getValueAt(row, 0).toString()));
                                        if(part!=null)
                                        {
                                            Item[] partidas=(Item[]) session.createCriteria(Item.class).add(Restrictions.eq("catalogo.idCatalogo", part.getCatalogo().getIdCatalogo())).list().toArray(new Item[0]);
                                            for(int a=0; a< partidas.length; a++)
                                            {
                                                partidas[a].setIntPinMax((double)value);
                                                session.update(partidas[a]);
                                            }
                                            session.update(part);
                                            session.getTransaction().commit();
                                            vector.setElementAt(value, col);
                                            this.dataVector.setElementAt(vector, row);
                                            fireTableCellUpdated(row, col);
                                            if(session.isOpen()==true)
                                                session.close();
                                        }
                                        else
                                        {
                                            JOptionPane.showMessageDialog(null, "La partida ya no existe");
                                            buscaItems();
                                        }
                                    }
                                    catch(Exception e)
                                    {
                                        session.getTransaction().rollback();
                                        System.out.println(e);
                                        JOptionPane.showMessageDialog(null, "Error al actualizar los datos"); 
                                    }
                                    finally
                                    {
                                        if(session.isOpen()==true)
                                            session.close();
                                    }
                                }
                                else
                                {
                                    JOptionPane.showMessageDialog(null, "Acceso denegado"); 
                                }
                            }catch(Exception e)
                            {
                                System.out.println(e);
                            }
                            if(session!=null)
                                if(session.isOpen()==true)
                                    session.close();
                        }
                        break;
                       
                    case 11:
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
                                usr = (Usuario)session.get(Usuario.class, usr.getIdUsuario());
                                if(usr.getRespaldar()==true)
                                {
                                    try
                                    {
                                        Item part=(Item) session.get(Item.class, Integer.parseInt(t_conceptos.getValueAt(row, 0).toString()));
                                        if(part!=null)
                                        {
                                            Item[] partidas=(Item[]) session.createCriteria(Item.class).add(Restrictions.eq("catalogo.idCatalogo", part.getCatalogo().getIdCatalogo())).list().toArray(new Item[0]);
                                            Posicion pos=null;
                                            if(((String)value).compareTo("NA")!=0)
                                                 pos = (Posicion) session.createCriteria(Posicion.class).add(Restrictions.eq("descripcion", ubicacion.getSelectedItem().toString())).uniqueResult();
                                            for(int a=0; a< partidas.length; a++)
                                            {
                                                partidas[a].setPosicion(pos);
                                                session.update(partidas[a]);
                                            }
                                            session.update(part);
                                            session.getTransaction().commit();
                                            vector.setElementAt(value, col);
                                            this.dataVector.setElementAt(vector, row);
                                            fireTableCellUpdated(row, col);
                                            if(session.isOpen()==true)
                                                session.close();
                                        }
                                        else
                                        {
                                            JOptionPane.showMessageDialog(null, "La partida ya no existe");
                                            buscaItems();
                                        }
                                    }
                                    catch(Exception e)
                                    {
                                        session.getTransaction().rollback();
                                        System.out.println(e);
                                        JOptionPane.showMessageDialog(null, "Error al actualizar los datos"); 
                                    }
                                    finally
                                    {
                                        if(session.isOpen()==true)
                                            session.close();
                                    }
                                }
                                else
                                {
                                    JOptionPane.showMessageDialog(null, "Acceso denegado"); 
                                }
                            }catch(Exception e)
                            {
                                System.out.println(e);
                            }
                            if(session!=null)
                                if(session.isOpen()==true)
                                    session.close();
                        }
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
        return mItem;
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        ubicacion = new javax.swing.JComboBox();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        t_datos = new javax.swing.JTable();
        bt_actualiza1 = new javax.swing.JButton();
        Selecciona2 = new javax.swing.JButton();
        Eliminar1 = new javax.swing.JButton();
        Selecciona3 = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        nombre = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        descripcion = new javax.swing.JTextField();
        b_cancelar1 = new javax.swing.JButton();
        b_guardar = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        t_conceptos = new javax.swing.JTable();
        jButton2 = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();

        ubicacion.setFont(new java.awt.Font("Dialog", 0, 9)); // NOI18N
        ubicacion.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "NA", "IZQ", "DER" }));

        setBackground(new java.awt.Color(255, 255, 255));
        setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "Edita Servicios", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Arial", 1, 12))); // NOI18N

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "Lista", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Arial", 1, 11))); // NOI18N

        t_datos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Nombre", "Descripción"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false
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
        t_datos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                t_datosMouseClicked(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                t_datosMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                t_datosMousePressed(evt);
            }
        });
        jScrollPane1.setViewportView(t_datos);

        bt_actualiza1.setBackground(new java.awt.Color(2, 135, 242));
        bt_actualiza1.setForeground(new java.awt.Color(254, 254, 254));
        bt_actualiza1.setIcon(new ImageIcon("imagenes/tabla.png"));
        bt_actualiza1.setText("Actualizar");
        bt_actualiza1.setToolTipText("Actualizar los datos de la tabla");
        bt_actualiza1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        bt_actualiza1.setMaximumSize(new java.awt.Dimension(87, 23));
        bt_actualiza1.setMinimumSize(new java.awt.Dimension(87, 23));
        bt_actualiza1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bt_actualiza1ActionPerformed(evt);
            }
        });

        Selecciona2.setBackground(new java.awt.Color(2, 135, 242));
        Selecciona2.setForeground(new java.awt.Color(254, 254, 254));
        Selecciona2.setIcon(new ImageIcon("imagenes/update-user.png"));
        Selecciona2.setText("Seleccionar");
        Selecciona2.setToolTipText("Seleccionar un registro de la tabla para editar");
        Selecciona2.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        Selecciona2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Selecciona2ActionPerformed(evt);
            }
        });

        Eliminar1.setBackground(new java.awt.Color(2, 135, 242));
        Eliminar1.setForeground(new java.awt.Color(254, 254, 254));
        Eliminar1.setIcon(new ImageIcon("imagenes/del-user.png"));
        Eliminar1.setText("Eliminar");
        Eliminar1.setToolTipText("Eliminar el registro actual");
        Eliminar1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        Eliminar1.setMaximumSize(new java.awt.Dimension(87, 23));
        Eliminar1.setMinimumSize(new java.awt.Dimension(87, 23));
        Eliminar1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Eliminar1ActionPerformed(evt);
            }
        });

        Selecciona3.setBackground(new java.awt.Color(2, 135, 242));
        Selecciona3.setForeground(new java.awt.Color(254, 254, 254));
        Selecciona3.setIcon(new ImageIcon("imagenes/add-user.png"));
        Selecciona3.setText("Nuevo");
        Selecciona3.setToolTipText("Agregar un registo actual");
        Selecciona3.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        Selecciona3.setMaximumSize(new java.awt.Dimension(87, 23));
        Selecciona3.setMinimumSize(new java.awt.Dimension(87, 23));
        Selecciona3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Selecciona3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(bt_actualiza1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(Selecciona2)
                        .addGap(18, 18, 18)
                        .addComponent(Eliminar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(Selecciona3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Selecciona3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Eliminar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Selecciona2)
                    .addComponent(bt_actualiza1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel4.setBackground(new java.awt.Color(254, 254, 254));
        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "Actualizar", javax.swing.border.TitledBorder.RIGHT, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Arial", 1, 11))); // NOI18N

        nombre.setEnabled(false);
        nombre.setNextFocusableComponent(descripcion);
        nombre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nombreActionPerformed(evt);
            }
        });
        nombre.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                nombreKeyTyped(evt);
            }
        });

        jLabel2.setForeground(new java.awt.Color(51, 0, 255));
        jLabel2.setText("Nombre");

        jLabel3.setText("Descripción");

        descripcion.setEnabled(false);
        descripcion.setNextFocusableComponent(b_guardar);
        descripcion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                descripcionActionPerformed(evt);
            }
        });
        descripcion.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                descripcionKeyTyped(evt);
            }
        });

        b_cancelar1.setBackground(new java.awt.Color(2, 135, 242));
        b_cancelar1.setForeground(new java.awt.Color(254, 254, 254));
        b_cancelar1.setIcon(new ImageIcon("imagenes/cancelar.png"));
        b_cancelar1.setText("Cancelar");
        b_cancelar1.setEnabled(false);
        b_cancelar1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        b_cancelar1.setNextFocusableComponent(nombre);
        b_cancelar1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_cancelar1ActionPerformed(evt);
            }
        });

        b_guardar.setBackground(new java.awt.Color(2, 135, 242));
        b_guardar.setForeground(new java.awt.Color(254, 254, 254));
        b_guardar.setIcon(new ImageIcon("imagenes/guardar.png"));
        b_guardar.setText("Actualizar");
        b_guardar.setEnabled(false);
        b_guardar.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        b_guardar.setName(""); // NOI18N
        b_guardar.setNextFocusableComponent(b_cancelar1);
        b_guardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_guardarActionPerformed(evt);
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
                        .addGap(0, 284, Short.MAX_VALUE)
                        .addComponent(b_cancelar1)
                        .addGap(18, 18, 18)
                        .addComponent(b_guardar))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(jLabel2))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(nombre)
                            .addComponent(descripcion))))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel2)
                    .addComponent(nombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel3)
                    .addComponent(descripcion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(b_cancelar1)
                    .addComponent(b_guardar))
                .addContainerGap(91, Short.MAX_VALUE))
        );

        jPanel1.setBackground(new java.awt.Color(254, 254, 254));
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "Conceptos del Servicio", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Arial", 1, 11))); // NOI18N

        t_conceptos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Descripción", "Grupo Mecánico", "I. DM", "Camb", "Rep. Min", "Rep. Med", "Rep. Max", "Pin. Min", "Pin. Med", "Pin. Max", "ubicacion"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.Object.class, java.lang.Object.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, true, true, true, true, true, true, true, true, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        t_conceptos.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        t_conceptos.getTableHeader().setReorderingAllowed(false);
        jScrollPane2.setViewportView(t_conceptos);

        jButton2.setText("-");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton1.setText("+");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 888, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 166, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void nombreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nombreActionPerformed
        descripcion.requestFocus();
    }//GEN-LAST:event_nombreActionPerformed

    private void descripcionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_descripcionActionPerformed
        b_guardar.requestFocus();
    }//GEN-LAST:event_descripcionActionPerformed

    private void nombreKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_nombreKeyTyped
        evt.setKeyChar(Character.toUpperCase(evt.getKeyChar()));
        if(nombre.getText().length()>=20)
            evt.consume();
    }//GEN-LAST:event_nombreKeyTyped

    private void descripcionKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_descripcionKeyTyped
        evt.setKeyChar(Character.toUpperCase(evt.getKeyChar()));
        if(descripcion.getText().length()>=100)
            evt.consume();
    }//GEN-LAST:event_descripcionKeyTyped

    private void b_guardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_guardarActionPerformed
        h=new Herramientas(usr, 0);
        h.session(sessionPrograma);
        Session session = HibernateUtil.getSessionFactory().openSession();
        try   
        {
            session.beginTransaction();
            usr = (Usuario)session.get(Usuario.class, usr.getIdUsuario());
            if(usr.getEditaCatalogo()==true)
            {
                if(nombre.getText().compareTo("")!=0)
                {
                    Object resp=session.createQuery("from Servicio obj where obj.idServicio='"+nombre.getText()+"' and obj.idServicio!='"+nb+"'").uniqueResult();
                    if(resp==null)
                    {
                        Query r = session.createQuery("update Servicio obj set obj.idServicio='"+nombre.getText()+"',obj.descripcion='"+descripcion.getText()+"' where obj.idServicio='"+nb+"'");
                        r.executeUpdate();
                        session.getTransaction().commit();
                        cajas(false, false, false, false);
                        borra_cajas();
                        buscaDato();
                        JOptionPane.showMessageDialog(null, "Registro modificado");
                    }
                    else
                        JOptionPane.showMessageDialog(null, "¡No se pueden guardar claves duplicadas!");  
                }
                else
                {
                    JOptionPane.showMessageDialog(null, "¡El nombre no puede estar vacio!!");
                    nombre.requestFocus();
                }
            }
            else
                JOptionPane.showMessageDialog(null, "¡Acceso denegado!");
        }catch (HibernateException he)
        {
            he.printStackTrace(); 
            session.getTransaction().rollback();
            JOptionPane.showMessageDialog(null, "Error al guardar los datos");
        }
        finally
        {
            if(session.isOpen())
                session.close();
        }
    }//GEN-LAST:event_b_guardarActionPerformed

    private void b_cancelar1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_cancelar1ActionPerformed
        int opt=JOptionPane.showConfirmDialog(this, "¡Los datos capturados se eliminarán!");
        if(opt==0)
        {
            borra_cajas();
            cajas(false, false, false, false);
        }
    }//GEN-LAST:event_b_cancelar1ActionPerformed

    private void bt_actualiza1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bt_actualiza1ActionPerformed
        buscaDato();
        this.borra_cajas();
        cajas(false, false, false, false);
    }//GEN-LAST:event_bt_actualiza1ActionPerformed

    private void Selecciona2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Selecciona2ActionPerformed
        h=new Herramientas(usr, 0);
        h.session(sessionPrograma);
        Session session = HibernateUtil.getSessionFactory().openSession();
        usr = (Usuario)session.get(Usuario.class, usr.getIdUsuario());
        if(usr.getEditaCatalogo()==true)
        {
            this.borra_cajas();
            if(t_datos.getSelectedRow()>=0)
            {
                nombre.setText(t_datos.getValueAt(t_datos.getSelectedRow(), 0).toString());
                nb=t_datos.getValueAt(t_datos.getSelectedRow(), 0).toString();
                descripcion.setText(t_datos.getValueAt(t_datos.getSelectedRow(), 1).toString());            
                ds=t_datos.getValueAt(t_datos.getSelectedRow(), 1).toString();
                cajas(true, true, true, true);
            }
            else
            JOptionPane.showMessageDialog(null, "¡No hay un servicio seleccionado!");
        }
        else
            JOptionPane.showMessageDialog(null, "¡Acceso denegado!");
        if(session!=null)
            if(session.isOpen())
                session.close();
    }//GEN-LAST:event_Selecciona2ActionPerformed

    private void Eliminar1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Eliminar1ActionPerformed
        h=new Herramientas(usr, 0);
        h.session(sessionPrograma);
        Session session = HibernateUtil.getSessionFactory().openSession();
        try
        {
            session.beginTransaction();
            usr = (Usuario)session.get(Usuario.class, usr.getIdUsuario());
            if(usr.getEditaCatalogo()==true)
            {
                if(this.t_datos.getSelectedRow()>=0)
                {
                    DefaultTableModel model = (DefaultTableModel) t_datos.getModel();
                    int opt=JOptionPane.showConfirmDialog(this, "¡Los datos capturados se eliminarán!");
                    if (JOptionPane.YES_OPTION == opt)
                    {
                        servicio = (Servicio)session.get(Servicio.class,t_datos.getValueAt(t_datos.getSelectedRow(), 0).toString());
                        if(servicio.getItems().isEmpty()==false)
                        {
                            session.getTransaction().rollback();
                            JOptionPane.showMessageDialog(null, "¡Debe eliminar primero los conceptos del servicio!");
                        }
                        else
                        {
                            session.delete(servicio);
                            session.getTransaction().commit();
                            model.removeRow(t_datos.getSelectedRow());
                            JOptionPane.showMessageDialog(null, "¡Registro eliminado!");
                            this.borra_cajas();
                            cajas(false, false, false, false);
                            buscaDato();
                        }
                    }
                }
                else
                {
                    JOptionPane.showMessageDialog(null, "¡Selecciona el servicio que desees eliminar!");
                    nombre.requestFocus();
                }
            }
            else
                JOptionPane.showMessageDialog(null, "¡Acceso denegado!");
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
    }//GEN-LAST:event_Eliminar1ActionPerformed

    private void Selecciona3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Selecciona3ActionPerformed
        h=new Herramientas(usr, 0);
        h.session(sessionPrograma);
        Session session = HibernateUtil.getSessionFactory().openSession();
        usr = (Usuario)session.get(Usuario.class, usr.getIdUsuario());
        if(usr.getEditaCatalogo()==true)
        {
            altaGrupo obj = new altaGrupo(new javax.swing.JFrame(), true, usr, sessionPrograma);
            Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
            obj.setLocation((d.width/2)-(obj.getWidth()/2), (d.height/2)-(obj.getHeight()/2));
            obj.setVisible(true);
            borra_cajas();
            cajas(false, false, false, false);
            buscaDato();
        }else
            JOptionPane.showMessageDialog(null, "¡Acceso denegado!");
        if(session!=null)
            if(session.isOpen())
                session.close();
    }//GEN-LAST:event_Selecciona3ActionPerformed

    private void t_datosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_t_datosMouseClicked
        // TODO add your handling code here:
        buscaItems();
    }//GEN-LAST:event_t_datosMouseClicked

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        if(t_datos.getSelectedRow()>-1)
        {
            Session session = HibernateUtil.getSessionFactory().openSession();
            try
            {
                session.beginTransaction();
                usr = (Usuario)session.get(Usuario.class, usr.getIdUsuario());
                if(usr.getEditaCatalogo()==true)
                {
                    buscaCatalogo obj = new buscaCatalogo(new javax.swing.JFrame(), true, sessionPrograma, this.usr);
                    Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
                    obj.setLocation((d.width/2)-(obj.getWidth()/2), (d.height/2)-(obj.getHeight()/2));
                    obj.setVisible(true);
                    Catalogo cat=obj.getReturnStatus();
                    if (cat!=null)
                    {
                        cat=(Catalogo)session.get(Catalogo.class, cat.getIdCatalogo());
                        servicio=(Servicio)session.get(Servicio.class, t_datos.getValueAt(t_datos.getSelectedRow(), 0).toString());
                        Object resp=session.createQuery("from Item obj where obj.catalogo.idCatalogo="+cat.getIdCatalogo()+" and obj.servicio.idServicio='"+servicio.getIdServicio()+"'").uniqueResult();
                        if(resp==null)
                        {
                            Item nuevo=new Item();
                            nuevo.setCatalogo(cat);
                            nuevo.setServicio(servicio);
                            session.save(nuevo);
                            session.getTransaction().commit();
                        }
                        else
                        {
                            session.getTransaction().rollback();
                            JOptionPane.showMessageDialog(null, "¡No se permiten conceptos repetidos!");
                        }
                    }
                }
                else
                    JOptionPane.showMessageDialog(null, "¡Acceso denegado!");
            }catch(Exception e)
            {
                e.printStackTrace();
                session.getTransaction().rollback();
            }
            finally
            {
                if(session.isOpen())
                    session.close();
                buscaItems();
            }
        }
        else
            JOptionPane.showMessageDialog(null, "¡Debes seleccionar primero un servicio de la lista!");
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        if(t_conceptos.getSelectedRow()>-1)
        {
            Session session = HibernateUtil.getSessionFactory().openSession();
            try
            {
                session.beginTransaction();
                usr = (Usuario)session.get(Usuario.class, usr.getIdUsuario());
                if(usr.getEditaCatalogo()==true)
                {
                    Item elimina=(Item)session.get(Item.class, Integer.parseInt(t_conceptos.getValueAt(t_conceptos.getSelectedRow(), 0).toString()));
                    if(elimina!=null)
                    {
                        session.delete(elimina);
                        session.getTransaction().commit();
                    }
                    else
                        session.getTransaction().rollback();
                }
                else
                    JOptionPane.showMessageDialog(null, "¡Acceso denegado!");
            }catch(Exception e)
            {
                e.printStackTrace();
                session.getTransaction().rollback();
            }
            finally
            {
                if(session.isOpen())
                    session.close();
                buscaItems();
            }
        }
        else
            JOptionPane.showMessageDialog(null, "¡Debes seleccionar primero un servicio de la lista!");
    }//GEN-LAST:event_jButton2ActionPerformed

    private void t_datosMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_t_datosMousePressed
        // TODO add your handling code here:
        
    }//GEN-LAST:event_t_datosMousePressed

    private void t_datosMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_t_datosMouseExited
        // TODO add your handling code here:
        
    }//GEN-LAST:event_t_datosMouseExited

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Eliminar1;
    private javax.swing.JButton Selecciona2;
    private javax.swing.JButton Selecciona3;
    private javax.swing.JButton b_cancelar1;
    private javax.swing.JButton b_guardar;
    private javax.swing.JButton bt_actualiza1;
    public javax.swing.JTextField descripcion;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    public javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    public javax.swing.JTextField nombre;
    private javax.swing.JTable t_conceptos;
    private javax.swing.JTable t_datos;
    private javax.swing.JComboBox ubicacion;
    // End of variables declaration//GEN-END:variables

    private List<Object[]> executeHQLQuery(String hql) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            session.beginTransaction();
            Query q = session.createQuery(hql);
            List resultList = q.list();
            session.getTransaction().commit();
            session.disconnect();
            return resultList;
        } catch (HibernateException he) {
            he.printStackTrace();
            List lista= null;
            return lista;
        }
        finally
        {
            if(session.isOpen())
            session.close();
        }
    }
    
    private void buscaDato()
    {
        String consulta="from Servicio";
        List <Object[]> resultList=executeHQLQuery(consulta);
        if(resultList.size()>0)
        {
            t_datos.setModel(ModeloTablaReporte(resultList.size(), columnas));
            int i=0;
            for (Object o : resultList)
            {
                Servicio actor = (Servicio) o;
                model.setValueAt(actor.getIdServicio(), i, 0);
                model.setValueAt(actor.getDescripcion(), i, 1);
                i++;
            }
        }else
            t_datos.setModel(ModeloTablaReporte(0, columnas));
        formatoTablas();
        buscaItems();
    }
    
    private void buscaItems()
    {
        if(t_datos.getSelectedRow()>-1)
        {
            Session session = HibernateUtil.getSessionFactory().openSession();
            try
            {
                session.beginTransaction();
                //servicio =(Servicio)session.get(Servicio.class, t_datos.getValueAt(t_datos.getSelectedRow(), 0).toString());
                Item [] renglones =(Item[])session.createCriteria(Item.class).add(Restrictions.eq("servicio.idServicio", t_datos.getValueAt(t_datos.getSelectedRow(), 0).toString())).createAlias("catalogo", "cat").createAlias("cat.especialidad", "esp").addOrder(Order.asc("esp.descripcion")).addOrder(Order.asc("cat.nombre")).list().toArray(new Item[0]);
                if(renglones!=null)
                {
                    //Item [] renglones=(Item[])servicio.getItems().toArray(new Item[0]);
                    t_conceptos.setModel(ModeloTablaConcepto(renglones.length, columnas1));
                    for(int i=0; i<renglones.length; i++)
                    {
                        mItem.setValueAt(renglones[i].getIdReparacion(), i, 0);
                        mItem.setValueAt(renglones[i].getCatalogo().getNombre(), i, 1);
                        mItem.setValueAt(renglones[i].getCatalogo().getEspecialidad().getDescripcion(), i, 2);
                        mItem.setValueAt(renglones[i].getIntDesm(), i, 3);
                        mItem.setValueAt(renglones[i].getIntCamb(), i, 4);
                        mItem.setValueAt(renglones[i].getIntRepMin(), i, 5);
                        mItem.setValueAt(renglones[i].getIntRepMed(), i, 6);
                        mItem.setValueAt(renglones[i].getIntRepMax(), i, 7);
                        mItem.setValueAt(renglones[i].getIntPinMin(), i, 8);
                        mItem.setValueAt(renglones[i].getIntPinMed(), i, 9);
                        mItem.setValueAt(renglones[i].getIntPinMax(), i, 10);
                        if(renglones[i].getPosicion()!=null)
                        {
                            mItem.setValueAt(renglones[i].getPosicion().getDescripcion(), i, 11);
                        }
                        else
                        {
                            mItem.setValueAt("NA", i, 11);
                        }
                    }
                }
                else
                {
                    t_conceptos.setModel(ModeloTablaConcepto(0, columnas));
                }
                session.getTransaction().commit();
            } catch (HibernateException he)
            {
                he.printStackTrace();
                session.getTransaction().rollback();
            }
            finally
            {
                if(session.isOpen())
                session.close();
            }
        }
        else
        {
            t_conceptos.setModel(ModeloTablaConcepto(0, columnas));
            System.out.println("1");
        }
        formatoTablas();
    }
    
    public void cajas( boolean nombre, boolean descripcion, boolean guardar, boolean cancelar)
    {
        this.nombre.setEnabled(nombre);
        this.descripcion.setEnabled(descripcion);
        this.b_guardar.setEnabled(guardar);
        this.b_cancelar1.setEnabled(cancelar);        
    }
    
    public void borra_cajas()
    {
        this.nombre.setText("");
        this.descripcion.setText("");        
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
                      column.setPreferredWidth(20);
                      break;
                  case 1:
                      column.setPreferredWidth(150);
                      break; 
                  default:
                      column.setPreferredWidth(40);
                      break;   
              }
        }
        JTableHeader header = t_datos.getTableHeader();
        header.setForeground(Color.white);
    }
    
    public void tabla_tamaños2()
    {
        TableColumnModel col_model = t_conceptos.getColumnModel();
        FormatoEditor fe=new FormatoEditor();
        t_conceptos.setDefaultEditor(Double.class, fe);
        for (int i=0; i<t_conceptos.getColumnCount(); i++)
        {
  	      TableColumn column = col_model.getColumn(i);
              switch(i)
              {
                  case 0:
                      column.setPreferredWidth(50);
                      break;
                  case 1:
                      column.setPreferredWidth(550);
                      break; 
                  case 2:
                      column.setPreferredWidth(250);
                      break; 
                  case 11:
                      column.setPreferredWidth(250);
                      DefaultCellEditor editor = new DefaultCellEditor(ubicacion);
                      column.setCellEditor(editor); 
                      editor.setClickCountToStart(2);
                      break; 
                  default:
                      column.setPreferredWidth(80);
                      break;   
              }
        }
        JTableHeader header = t_conceptos.getTableHeader();
        header.setForeground(Color.white);
    }
    public void formatoTablas()
    {
        Color c1 = new java.awt.Color(2, 135, 242);
        for(int x=0; x<t_datos.getColumnModel().getColumnCount(); x++)
            t_datos.getColumnModel().getColumn(x).setHeaderRenderer(new Render1(c1));
        for(int x=0; x<t_conceptos.getColumnModel().getColumnCount(); x++)
            t_conceptos.getColumnModel().getColumn(x).setHeaderRenderer(new Render1(c1));
        tabla_tamaños();
        tabla_tamaños2();
        t_conceptos.setDefaultRenderer(Double.class, formato); 
        
        t_datos.setShowVerticalLines(true);
        t_datos.setShowHorizontalLines(true);
        
        t_conceptos.setShowVerticalLines(true);
        t_conceptos.setShowHorizontalLines(true);
    }
    
    private void ubicaciones()
    {
        ubicacion.removeAllItems();
        String consulta="from Posicion";
        ubicacion.addItem("NA");
        List <Object[]> resultList=executeHQLQuery(consulta);
        for (Object o : resultList)
        {
            Posicion actor = (Posicion) o;
            ubicacion.addItem(actor.getDescripcion());
        }
    }
}