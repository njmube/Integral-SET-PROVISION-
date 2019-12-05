/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Operaciones;

import Hibernate.Util.HibernateUtil;
import Hibernate.entidades.Configuracion;
import Hibernate.entidades.Empleado;
import Hibernate.entidades.Foto;
import Hibernate.entidades.Orden;
import Hibernate.entidades.TrabajoExtra;
import Hibernate.entidades.Usuario;
import Integral.Herramientas;
import Integral.PDF;
import Integral.Render1;
import Integral.calendario;
import Servicios.buscaOrden;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.GrayColor;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPTable;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author Sistemas
 */
public class Destajo extends javax.swing.JPanel {

    String estado="";
    String sessionPrograma="";
    Usuario usr;
    String ord="";
    Herramientas h;
    String global="h";
    double anterior=0.0d;
    double anterior1=0.0d;
    DefaultTableModel model, model1, model2, model3, model4;
    String id_empleado="";
    Empleado emp;
    Orden orden_act=null;
    String[] columnas = new String [] {"Id", "Fecha de Avance", "% de avance", "Importe Pagado", "Notas"};
    String[] columnas1 = new String [] {"Id", "Orden", "Fecha Asignación", "Imp. a Pagar", "Avance", "Concepto", "Aut."};
    String[] columnas2 = new String [] {"#Parte", "Descripción", "Cant.", "Costo"};
    String[] columnas3 = new String [] {"Id", "F. Avance", "% de avance", "Imp. Pagado", "Nota"};
    String[] columnas4 = new String [] {"No. Parte", "Descripción", "Cantidad", "Costo"};
    int configuracion=1;
    /**
     * Creates new form destajo
     */
    public Destajo(String ord, Usuario usr, String estado, String sessionPrograma, int configuracion) {
        initComponents();
        this.ord=ord;
        this.configuracion=configuracion;
        this.usr=usr;
        this.estado=estado;
        this.sessionPrograma=sessionPrograma;
        t_datos.setModel(ModeloTablaReporte(0, columnas));
        t_adicionales.setModel(ModeloTablaReporte1(0, columnas1));
        t_datos1.setModel(ModeloTablaReporte3(0, columnas3));
        //jButton6.setIcon(new ImageIcon("imagenes/busca.png"));
        b_pdf1.setIcon(new ImageIcon("imagenes/pdf.png"));
        jButton3.setIcon(new ImageIcon("imagenes/pdf.png"));
        formatoTabla();
        formatoTabla1();
        consulta();
        buscaPagosAdicionales();
    }
    
    DefaultTableModel ModeloTablaReporte3(int renglones, String columnas[])
        {
            model3 = new DefaultTableModel(new Object [renglones][5], columnas)
            {
                Class[] types = new Class [] {
                    java.lang.String.class, 
                    java.lang.String.class, 
                    java.lang.Double.class, 
                    java.lang.Double.class, 
                    java.lang.String.class
                };
                boolean[] canEdit = new boolean [] {
                    false, false, false, false, false
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
            return model3;
        }

    DefaultTableModel ModeloTablaReporte2(int renglones, String columnas[])
        {
            model2 = new DefaultTableModel(new Object [renglones][5], columnas)
            {
                Class[] types = new Class [] {
                    java.lang.String.class, 
                    java.lang.String.class, 
                    java.lang.Double.class, 
                    java.lang.Double.class, 
                    java.lang.String.class
                };
                boolean[] canEdit = new boolean [] {
                    false, false, false, false, false
                };
                
                public void setValueAt(Object value, int row, int col)
                {
                        Vector vector = (Vector)this.dataVector.elementAt(row);
                        Object celda = ((Vector)this.dataVector.elementAt(row)).elementAt(col);
                        switch(col)
                        {
                            case 6:
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
        return model2;
    }
    
    DefaultTableModel ModeloTablaReporte4(int renglones, String columnas[])
        {
            model4 = new DefaultTableModel(new Object [renglones][5], columnas)
            {
                Class[] types = new Class [] {
                    java.lang.String.class, 
                    java.lang.String.class, 
                    java.lang.Double.class, 
                    java.lang.Double.class, 
                    java.lang.String.class
                };
                boolean[] canEdit = new boolean [] {
                    false, false, false, false, false
                };
                
                public void setValueAt(Object value, int row, int col)
                {
                        Vector vector = (Vector)this.dataVector.elementAt(row);
                        Object celda = ((Vector)this.dataVector.elementAt(row)).elementAt(col);
                        switch(col)
                        {
                            case 6:
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
        return model4;
    }
                
    DefaultTableModel ModeloTablaReporte(int renglones, String columnas[])
        {
            model = new DefaultTableModel(new Object [renglones][5], columnas)
            {
                Class[] types = new Class [] {
                    java.lang.String.class, 
                    java.lang.String.class, 
                    java.lang.Double.class, 
                    java.lang.Double.class, 
                    java.lang.String.class
                };
                boolean[] canEdit = new boolean [] {
                    false, false, false, false, false
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
            model1 = new DefaultTableModel(new Object [renglones][7], columnas)
            {
                Class[] types = new Class [] {
                    java.lang.Integer.class, 
                    java.lang.Integer.class, 
                    java.lang.String.class, 
                    java.lang.Double.class, 
                    java.lang.Double.class, 
                    java.lang.String.class,
                    java.lang.Boolean.class 
                };
                boolean[] canEdit = new boolean [] {
                    false, false, false, false, false, false, true
                };

                public void setValueAt(Object value, int row, int col)
                {
                        Vector vector = (Vector)this.dataVector.elementAt(row);
                        Object celda = ((Vector)this.dataVector.elementAt(row)).elementAt(col);
                        switch(col)
                        {
                            case 6:
                                Session session = HibernateUtil.getSessionFactory().openSession();
                                session.beginTransaction().begin();
                                
                                try{
                                    TrabajoExtra extra=(TrabajoExtra)session.get(TrabajoExtra.class, Integer.parseInt(t_adicionales.getValueAt(t_adicionales.getSelectedRow(), 0).toString())); 
                                    usr = (Usuario)session.get(Usuario.class, usr.getIdUsuario());
                                    
                                    if(usr.getAutorizaTrabajo()==true){
                                        if((Boolean)t_adicionales.getValueAt(t_adicionales.getSelectedRow(), 6)==false){
                                            extra.setAutorizado(true);
                                            extra.setUsuario(usr);
                                            session.beginTransaction().commit();
                                            buscaPagosAdicionales();
                                        }else{
                                            Query query = session.createSQLQuery("select * from pago_adicional where id_trabajo_extra = "+Integer.parseInt(t_adicionales.getValueAt(t_adicionales.getSelectedRow(), 0).toString()));
                                            query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
                                            ArrayList consumido = (ArrayList)query.list();
                                            if(consumido.size()>0){
                                                session.beginTransaction().rollback();
                                                JOptionPane.showMessageDialog(null, "!La partida contiene pagos adicionales¡");
                                            }else{
                                                extra.setAutorizado(false);
                                                extra.setUsuario(null);
                                                session.beginTransaction().commit();
                                                buscaPagosAdicionales();
                                            }
                                        }
                                    }else{
                                        session.beginTransaction().rollback();
                                        JOptionPane.showMessageDialog(null, "!Acceso denegado¡");
                                    }
                                    
                                }catch(Exception e)
                                {
                                    session.beginTransaction().rollback();
                                    System.out.println(e);
                                    e.printStackTrace();
                                    JOptionPane.showMessageDialog(null, "Error al autorizar el trabajo adicional");
                                }
                                if(session!=null)
                                    if(session.isOpen())
                                    {
                                        session.flush();
                                        session.clear();
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
            return model1;
        }
    public void consulta()
    {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try
        {
            session.beginTransaction().begin();
            Orden orden = (Orden)session.get(Orden.class, Integer.parseInt(ord));
            t_responsable.setText("");
            t_asignacion.setText("");
            t_limite.setText("");
            t_importe.setValue(0.0d);
            t_monto.setValue(0.0d);
            t_horas.setValue(0.0d);
            int f_pago=0;//
            double importe=0.0d, total=0.0d;
            DecimalFormat formatoPorcentaje = new DecimalFormat("#,##0.00");
            formatoPorcentaje.setMinimumFractionDigits(2);   
            emp=null;
            id_empleado="";
            switch(global)
            {
                case "h":
                    emp=orden.getEmpleadoByRHojalateria();
                    if(emp!=null)
                    {
                        id_empleado=""+emp.getIdEmpleado();
                        t_responsable.setText(emp.getNombre());
                        if(orden.getImporteHojalateria()!=null)
                        {
                            total=orden.getImporteHojalateria();
                            t_importe.setValue(total);
                            anterior=total;
                        }
                        f_pago=emp.getFomaPago();//
                        importe=emp.getImporte();
                        double valor=0.0d;
                        switch(f_pago)
                        {
                            case 0:
                                valor=new BigDecimal(importe/8.0d).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                                t_monto.setValue(valor);
                                if(total==0.0d || valor==0.0d)
                                    t_horas.setValue(0.0d);
                                else
                                    t_horas.setValue(new BigDecimal(total/valor).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
                                break;
                            case 1:
                                valor=importe;
                                t_monto.setValue(valor);
                                if(total==0.0d || valor==0.0d)
                                    t_horas.setValue(0.0d);
                                else
                                    t_horas.setValue(new BigDecimal(total/valor).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
                                break;
                            case 2:
                                valor=0.0d;
                                t_monto.setValue(valor);
                                t_horas.setValue(valor);
                                break;
                            case 3:
                                valor=new BigDecimal(importe/45.0d).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                                t_monto.setValue(valor);
                                if(total==0.0d || valor==0.0d)
                                    t_horas.setValue(0.0d);
                                else
                                    t_horas.setValue(new BigDecimal(total/valor).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
                                break;
                            case 4:
                                valor=new BigDecimal(importe/90.0d).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                                t_monto.setValue(valor);
                                if(total==0.0d || valor==0.0d)
                                    t_horas.setValue(0.0d);
                                else
                                    t_horas.setValue(new BigDecimal(total/valor).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
                                break;
                            case 5:
                                valor=new BigDecimal(importe/98.0d).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                                t_monto.setValue(valor);
                                if(total==0.0d || valor==0.0d)
                                    t_horas.setValue(0.0d);
                                else
                                    t_horas.setValue(new BigDecimal(total/valor).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
                                break;
                            case 6:
                                valor=new BigDecimal(importe/196.0d).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                                t_monto.setValue(valor);
                                if(total==0.0d || valor==0.0d)
                                    t_horas.setValue(0.0d);
                                else
                                    t_horas.setValue(new BigDecimal(total/valor).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
                                break;
                        }
                        b_adicionales.setEnabled(true);
                    }
                    else
                    {
                        b_adicionales.setEnabled(false);
                    }
                    if(orden.getRHojalateriaFecha()!=null)
                        t_asignacion.setText(orden.getRHojalateriaFecha().toString());
                    if(orden.getHojalateriaLimite()!=null)
                        t_limite.setText(orden.getHojalateriaLimite().toString());
                   
                    break;
                    
                case "m":
                    emp=orden.getEmpleadoByRMecanica();
                    if(emp!=null)
                    {
                        id_empleado=""+emp.getIdEmpleado();
                        t_responsable.setText(emp.getNombre());
                        if(orden.getImporteMecanica()!=null)
                        {
                            total=orden.getImporteMecanica();
                            t_importe.setValue(total);
                            anterior=total;
                        }
                        f_pago=emp.getFomaPago();//
                        importe=emp.getImporte();
                        double valor=0.0d;
                        switch(f_pago)
                        {
                            case 0:
                                valor=importe/8.0d;
                                t_monto.setValue(valor);
                                if(total==0.0d || valor==0.0d)
                                    t_horas.setValue(0.0d);
                                else
                                    t_horas.setValue(new BigDecimal(total/valor).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
                                break;
                            case 1:
                                valor=importe;
                                t_monto.setValue(valor);
                                if(total==0.0d || valor==0.0d)
                                    t_horas.setValue(0.0d);
                                else
                                    t_horas.setValue(new BigDecimal(total/valor).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
                                break;
                            case 2:
                                valor=0.0d;
                                t_monto.setValue(valor);
                                t_horas.setValue(valor);
                                break;
                            case 3:
                                valor=importe/45.0d;
                                t_monto.setValue(valor);
                                if(total==0.0d || valor==0.0d)
                                    t_horas.setValue(0.0d);
                                else
                                    t_horas.setValue(new BigDecimal(total/valor).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
                                break;
                            case 4:
                                valor=importe/90.0d;
                                t_monto.setValue(valor);
                                if(total==0.0d || valor==0.0d)
                                    t_horas.setValue(0.0d);
                                else
                                    t_horas.setValue(new BigDecimal(total/valor).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
                                break;
                            case 5:
                                valor=importe/98.0d;
                                t_monto.setValue(valor);
                                if(total==0.0d || valor==0.0d)
                                    t_horas.setValue(0.0d);
                                else
                                    t_horas.setValue(new BigDecimal(total/valor).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
                                break;
                            case 6:
                                valor=importe/196.0d;
                                t_monto.setValue(valor);
                                if(total==0.0d || valor==0.0d)
                                    t_horas.setValue(0.0d);
                                else
                                    t_horas.setValue(new BigDecimal(total/valor).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
                                break;
                        }
                        b_adicionales.setEnabled(true);
                    }
                    else
                    {
                        b_adicionales.setEnabled(false);
                    }
                    if(orden.getRMecanicaFecha()!=null)
                        t_asignacion.setText(orden.getRMecanicaFecha().toString());
                    if(orden.getMecanicaLimite()!=null)
                        t_limite.setText(orden.getMecanicaLimite().toString());
                    break;
                    
                case "s":
                    emp=orden.getEmpleadoByRSuspension();
                    if(emp!=null)
                    {
                        id_empleado=""+emp.getIdEmpleado();
                        t_responsable.setText(emp.getNombre());
                        if(orden.getImporteSuspension()!=null)
                        {
                            total=orden.getImporteSuspension();
                            t_importe.setValue(total);
                            anterior=total;
                        }
                        f_pago=emp.getFomaPago();//
                        importe=emp.getImporte();
                        double valor=0.0d;
                        switch(f_pago)
                        {
                            case 0:
                                valor=importe/8.0d;
                                t_monto.setValue(valor);
                                if(total==0.0d || valor==0.0d)
                                    t_horas.setValue(0.0d);
                                else
                                    t_horas.setValue(new BigDecimal(total/valor).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
                                break;
                            case 1:
                                valor=importe;
                                t_monto.setValue(valor);
                                if(total==0.0d || valor==0.0d)
                                    t_horas.setValue(0.0d);
                                else
                                    t_horas.setValue(new BigDecimal(total/valor).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
                                break;
                            case 2:
                                valor=0.0d;
                                t_monto.setValue(valor);
                                t_horas.setValue(valor);
                                break;
                            case 3:
                                valor=importe/45.0d;
                                t_monto.setValue(valor);
                                if(total==0.0d || valor==0.0d)
                                    t_horas.setValue(0.0d);
                                else
                                    t_horas.setValue(new BigDecimal(total/valor).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
                                break;
                            case 4:
                                valor=importe/90.0d;
                                t_monto.setValue(valor);
                                if(total==0.0d || valor==0.0d)
                                    t_horas.setValue(0.0d);
                                else
                                    t_horas.setValue(new BigDecimal(total/valor).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
                                break;
                            case 5:
                                valor=importe/98.0d;
                                t_monto.setValue(valor);
                                if(total==0.0d || valor==0.0d)
                                    t_horas.setValue(0.0d);
                                else
                                    t_horas.setValue(new BigDecimal(total/valor).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
                                break;
                            case 6:
                                valor=importe/196.0d;
                                t_monto.setValue(valor);
                                if(total==0.0d || valor==0.0d)
                                    t_horas.setValue(0.0d);
                                else
                                    t_horas.setValue(new BigDecimal(total/valor).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
                                break;
                        }
                        b_adicionales.setEnabled(true);
                    }
                    else
                    {
                        b_adicionales.setEnabled(false);
                    }
                    if(orden.getRSuspensionFecha()!=null)
                        t_asignacion.setText(orden.getRSuspensionFecha().toString());
                    if(orden.getSuspensionLimite()!=null)
                        t_limite.setText(orden.getSuspensionLimite().toString());
                    break;
                case "e":
                    emp=orden.getEmpleadoByRElectrico();
                    if(emp!=null)
                    {
                        id_empleado=""+emp.getIdEmpleado();
                        t_responsable.setText(emp.getNombre());
                        if(orden.getImporteElectrico()!=null)
                        {
                            total=orden.getImporteElectrico();
                            t_importe.setValue(total);
                            anterior=total;
                        }
                        f_pago=emp.getFomaPago();//
                        importe=emp.getImporte();
                        double valor=0.0d;
                        switch(f_pago)
                        {
                            case 0:
                                valor=importe/8.0d;
                                t_monto.setValue(valor);
                                if(total==0.0d || valor==0.0d)
                                    t_horas.setValue(0.0d);
                                else
                                    t_horas.setValue(new BigDecimal(total/valor).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
                                break;
                            case 1:
                                valor=importe;
                                t_monto.setValue(valor);
                                if(total==0.0d || valor==0.0d)
                                    t_horas.setValue(0.0d);
                                else
                                    t_horas.setValue(new BigDecimal(total/valor).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
                                break;
                            case 2:
                                valor=0.0d;
                                t_monto.setValue(valor);
                                t_horas.setValue(total/valor);
                                break;
                            case 3:
                                valor=importe/45.0d;
                                t_monto.setValue(valor);
                                if(total==0.0d || valor==0.0d)
                                    t_horas.setValue(0.0d);
                                else
                                    t_horas.setValue(new BigDecimal(total/valor).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
                                break;
                            case 4:
                                valor=importe/90.0d;
                                t_monto.setValue(valor);
                                if(total==0.0d || valor==0.0d)
                                    t_horas.setValue(0.0d);
                                else
                                    t_horas.setValue(new BigDecimal(total/valor).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
                                break;
                            case 5:
                                valor=importe/98.0d;
                                t_monto.setValue(valor);
                                if(total==0.0d || valor==0.0d)
                                    t_horas.setValue(0.0d);
                                else
                                    t_horas.setValue(new BigDecimal(total/valor).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
                                break;
                            case 6:
                                valor=importe/196.0d;
                                t_monto.setValue(valor);
                                if(total==0.0d || valor==0.0d)
                                    t_horas.setValue(0.0d);
                                else
                                    t_horas.setValue(new BigDecimal(total/valor).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
                                break;
                        }
                        b_adicionales.setEnabled(true);
                    }
                    else
                    {
                        b_adicionales.setEnabled(false);
                    }
                    if(orden.getRElectricoFecha()!=null)
                        t_asignacion.setText(orden.getRElectricoFecha().toString());
                    if(orden.getElectricoLimite()!=null)
                        t_limite.setText(orden.getElectricoLimite().toString());
                    break;
                    
                case "p":
                    emp=orden.getEmpleadoByRPintura();
                    if(emp!=null)
                    {
                        id_empleado=""+emp.getIdEmpleado();
                        t_responsable.setText(emp.getNombre());
                        if(orden.getImportePintura()!=null)
                        {
                            total=orden.getImportePintura();
                            t_importe.setValue(total);
                            anterior=total;
                        }
                        f_pago=emp.getFomaPago();//
                        importe=emp.getImporte();
                        double valor=0.0d;
                        switch(f_pago)
                        {
                            case 0:
                                valor=importe/8.0d;
                                t_monto.setValue(valor);
                                if(total==0.0d || valor==0.0d)
                                    t_horas.setValue(0.0d);
                                else
                                    t_horas.setValue(new BigDecimal(total/valor).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
                                break;
                            case 1:
                                valor=importe;
                                t_monto.setValue(valor);
                                if(total==0.0d || valor==0.0d)
                                    t_horas.setValue(0.0d);
                                else
                                    t_horas.setValue(new BigDecimal(total/valor).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
                                break;
                            case 2:
                                valor=0.0d;
                                t_monto.setValue(valor);
                                t_horas.setValue(total/valor);
                                break;
                            case 3:
                                valor=importe/45.0d;
                                t_monto.setValue(valor);
                                if(total==0.0d || valor==0.0d)
                                    t_horas.setValue(0.0d);
                                else
                                    t_horas.setValue(new BigDecimal(total/valor).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
                                break;
                            case 4:
                                valor=importe/90.0d;
                                t_monto.setValue(valor);
                                if(total==0.0d || valor==0.0d)
                                    t_horas.setValue(0.0d);
                                else
                                    t_horas.setValue(new BigDecimal(total/valor).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
                                break;
                            case 5:
                                valor=importe/98.0d;
                                t_monto.setValue(valor);
                                if(total==0.0d || valor==0.0d)
                                    t_horas.setValue(0.0d);
                                else
                                    t_horas.setValue(new BigDecimal(total/valor).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
                                break;
                            case 6:
                                valor=importe/196.0d;
                                t_monto.setValue(valor);
                                if(total==0.0d || valor==0.0d)
                                    t_horas.setValue(0.0d);
                                else
                                    t_horas.setValue(new BigDecimal(total/valor).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
                                break;
                        }
                        b_adicionales.setEnabled(true);
                    }
                    else
                    {
                        b_adicionales.setEnabled(false);
                    }
                    if(orden.getRPinturaFecha()!=null)
                        t_asignacion.setText(orden.getRPinturaFecha().toString());
                    if(orden.getPinturaLimite()!=null)
                        t_limite.setText(orden.getPinturaLimite().toString());
                    break;
            }
            h=new Herramientas(usr, 0);
            if(estado.compareTo("")==0 && h.isCerrada(ord)==false)
            {
                t_datos.setEnabled(true);
                t_importe.setEditable(true);
                b_mas.setEnabled(true);
                b_menos.setEnabled(true);
                b_mas1.setEnabled(true);
                b_menos1.setEnabled(true);
                t_adicionales.setEnabled(true);
            }
            else
            {
                t_datos.setEnabled(false);
                t_importe.setEditable(false);
                b_mas.setEnabled(false);
                b_menos.setEnabled(false);
                b_mas1.setEnabled(false);
                b_menos1.setEnabled(false);
                t_adicionales.setEnabled(false);
            }
        }catch(Exception e)
        {
            e.printStackTrace();
        }
        if(session!=null)
            if(session.isOpen())
                session.close();
        buscaDestajos();
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        grupo = new javax.swing.ButtonGroup();
        d_nuevo = new javax.swing.JDialog();
        jPanel3 = new javax.swing.JPanel();
        t_fecha_pago = new javax.swing.JTextField();
        b_calendario_pago = new javax.swing.JButton();
        l_porcentaje_pago = new javax.swing.JLabel();
        l_nota_pago = new javax.swing.JLabel();
        l_importe_pago = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        b_guardar_pago = new javax.swing.JButton();
        b_cancelar_pago = new javax.swing.JButton();
        t_porcentaje_pago = new javax.swing.JFormattedTextField();
        t_importe_pago = new javax.swing.JFormattedTextField();
        t_notas_pago = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jButton4 = new javax.swing.JButton();
        d_pagos = new javax.swing.JDialog();
        jPanel4 = new javax.swing.JPanel();
        b_menos1 = new javax.swing.JButton();
        b_mas1 = new javax.swing.JButton();
        cb = new javax.swing.JCheckBox();
        jScrollPane3 = new javax.swing.JScrollPane();
        t_adicionales = new javax.swing.JTable();
        t_monto1 = new javax.swing.JFormattedTextField();
        jLabel9 = new javax.swing.JLabel();
        t_responsable1 = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        d_nuevo1 = new javax.swing.JDialog();
        jPanel5 = new javax.swing.JPanel();
        l_importe_pago1 = new javax.swing.JLabel();
        b_guardar_pago1 = new javax.swing.JButton();
        b_cancelar_pago1 = new javax.swing.JButton();
        t_importe_pago1 = new javax.swing.JFormattedTextField();
        t_notas_pago1 = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        t_orden = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        pago_adicional = new javax.swing.JDialog();
        jPanel8 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        t_responsable2 = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        t_asignacion1 = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        t_importe1 = new javax.swing.JFormattedTextField();
        jLabel17 = new javax.swing.JLabel();
        t_trabajo = new javax.swing.JTextField();
        jLabel22 = new javax.swing.JLabel();
        t_orden_trabajo = new javax.swing.JTextField();
        jLabel24 = new javax.swing.JLabel();
        t_trabajo1 = new javax.swing.JTextField();
        jButton8 = new javax.swing.JButton();
        jScrollPane5 = new javax.swing.JScrollPane();
        t_datos1 = new javax.swing.JTable();
        b_menos3 = new javax.swing.JButton();
        b_mas3 = new javax.swing.JButton();
        b_pdf1 = new javax.swing.JButton();
        d_nuevo2 = new javax.swing.JDialog();
        jPanel9 = new javax.swing.JPanel();
        t_fecha_pago1 = new javax.swing.JTextField();
        b_calendario_pago1 = new javax.swing.JButton();
        l_porcentaje_pago1 = new javax.swing.JLabel();
        l_nota_pago1 = new javax.swing.JLabel();
        l_importe_pago2 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        b_guardar_pago2 = new javax.swing.JButton();
        b_cancelar_pago2 = new javax.swing.JButton();
        t_porcentaje_pago1 = new javax.swing.JFormattedTextField();
        t_importe_pago2 = new javax.swing.JFormattedTextField();
        t_notas_pago2 = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        b_mecanica = new javax.swing.JRadioButton();
        b_hojalateria = new javax.swing.JRadioButton();
        b_suspencion = new javax.swing.JRadioButton();
        b_electrico = new javax.swing.JRadioButton();
        b_pintura = new javax.swing.JRadioButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        t_responsable = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        t_asignacion = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        t_importe = new javax.swing.JFormattedTextField();
        jLabel4 = new javax.swing.JLabel();
        t_limite = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        t_monto = new javax.swing.JFormattedTextField();
        t_horas = new javax.swing.JFormattedTextField();
        jLabel23 = new javax.swing.JLabel();
        t_consumibles = new javax.swing.JTextField();
        jButton7 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        t_datos = new javax.swing.JTable();
        b_mas = new javax.swing.JButton();
        b_menos = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        b_adicionales = new javax.swing.JButton();
        b_general = new javax.swing.JButton();
        t_fecha = new javax.swing.JTextField();
        b_fecha_siniestro = new javax.swing.JButton();

        d_nuevo.setTitle("Nuevo Pago");
        d_nuevo.setModalityType(java.awt.Dialog.ModalityType.APPLICATION_MODAL);

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "Nuevo Importe pagado"));
        jPanel3.setPreferredSize(new java.awt.Dimension(486, 150));

        t_fecha_pago.setEditable(false);

        b_calendario_pago.setIcon(new ImageIcon("imagenes/calendario.png"));
        b_calendario_pago.setText("Fecha de Avance:");
        b_calendario_pago.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_calendario_pagoActionPerformed(evt);
            }
        });

        l_porcentaje_pago.setText("avance:");

        l_nota_pago.setText("Nota: El porcentaje se acumula con los anteriores y no debe ser mayor al 100%.");

        l_importe_pago.setText("Importe Pagado: $");

        jLabel12.setText("%");

        b_guardar_pago.setText("Guardar");
        b_guardar_pago.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_guardar_pagoActionPerformed(evt);
            }
        });

        b_cancelar_pago.setText("Cancelar");
        b_cancelar_pago.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_cancelar_pagoActionPerformed(evt);
            }
        });

        t_porcentaje_pago.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        t_porcentaje_pago.setValue(0.0d);
        t_porcentaje_pago.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                t_porcentaje_pagoFocusLost(evt);
            }
        });
        t_porcentaje_pago.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                t_porcentaje_pagoActionPerformed(evt);
            }
        });

        t_importe_pago.setEditable(false);
        t_importe_pago.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.00"))));
        t_importe_pago.setValue(0.0d);

        jLabel7.setText("Notas:");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(t_notas_pago))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(l_nota_pago)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel3Layout.createSequentialGroup()
                                .addComponent(l_porcentaje_pago)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(t_porcentaje_pago))
                            .addComponent(b_calendario_pago))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(0, 6, Short.MAX_VALUE)
                                .addComponent(t_fecha_pago, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(214, 214, 214))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel12)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(l_importe_pago)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(t_importe_pago, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(b_cancelar_pago)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(b_guardar_pago, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(19, 19, 19))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(t_fecha_pago, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(b_calendario_pago))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(l_porcentaje_pago)
                    .addComponent(jLabel12)
                    .addComponent(t_porcentaje_pago, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(l_importe_pago)
                    .addComponent(t_importe_pago, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel7)
                    .addComponent(t_notas_pago, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(b_cancelar_pago)
                    .addComponent(b_guardar_pago))
                .addGap(18, 18, 18)
                .addComponent(l_nota_pago)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout d_nuevoLayout = new javax.swing.GroupLayout(d_nuevo.getContentPane());
        d_nuevo.getContentPane().setLayout(d_nuevoLayout);
        d_nuevoLayout.setHorizontalGroup(
            d_nuevoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        d_nuevoLayout.setVerticalGroup(
            d_nuevoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, 178, Short.MAX_VALUE)
        );

        jButton4.setText("excel");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        d_pagos.setTitle("Trabajos Adicionales");
        d_pagos.setBackground(new java.awt.Color(255, 255, 255));
        d_pagos.setModalityType(java.awt.Dialog.ModalityType.APPLICATION_MODAL);

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));

        b_menos1.setIcon(new ImageIcon("imagenes/boton_menos.png"));
        b_menos1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_menos1ActionPerformed(evt);
            }
        });

        b_mas1.setIcon(new ImageIcon("imagenes/boton_mas.png"));
        b_mas1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_mas1ActionPerformed(evt);
            }
        });

        cb.setText("Todos los pagos.");
        cb.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbActionPerformed(evt);
            }
        });

        jScrollPane3.setBackground(new java.awt.Color(255, 255, 255));
        jScrollPane3.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "Pagos Adicionales", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.TOP));

        t_adicionales.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Id", "Orden", "Fecha Asignación", "Imp. a Pagar", "Avance", "Concepto", "Aut."
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.Integer.class, java.lang.String.class, java.lang.Double.class, java.lang.Double.class, java.lang.String.class, java.lang.Boolean.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        t_adicionales.setFillsViewportHeight(true);
        t_adicionales.getTableHeader().setReorderingAllowed(false);
        t_adicionales.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                t_adicionalesMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(t_adicionales);

        t_monto1.setEditable(false);
        t_monto1.setBackground(new java.awt.Color(255, 255, 255));
        t_monto1.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(0, 0, 0)));
        t_monto1.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.00"))));
        t_monto1.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        jLabel9.setText("Monto x Hora:");

        t_responsable1.setEditable(false);
        t_responsable1.setBackground(new java.awt.Color(255, 255, 255));
        t_responsable1.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(0, 0, 0)));

        jLabel8.setText("Empleado:");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 704, Short.MAX_VALUE)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(b_mas1, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(b_menos1, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(cb))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(t_responsable1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(t_monto1, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(t_responsable1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9)
                    .addComponent(t_monto1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 270, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(b_mas1, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(b_menos1, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(cb, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
        );

        javax.swing.GroupLayout d_pagosLayout = new javax.swing.GroupLayout(d_pagos.getContentPane());
        d_pagos.getContentPane().setLayout(d_pagosLayout);
        d_pagosLayout.setHorizontalGroup(
            d_pagosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        d_pagosLayout.setVerticalGroup(
            d_pagosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        d_nuevo1.setModalityType(java.awt.Dialog.ModalityType.APPLICATION_MODAL);

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));
        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "Nuevo Importe pagado adicional"));

        l_importe_pago1.setText("Importe Pagado: $");

        b_guardar_pago1.setText("Guardar");
        b_guardar_pago1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_guardar_pago1ActionPerformed(evt);
            }
        });

        b_cancelar_pago1.setText("Cancelar");
        b_cancelar_pago1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_cancelar_pago1ActionPerformed(evt);
            }
        });

        t_importe_pago1.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.00"))));
        t_importe_pago1.setValue(0.0d);

        jLabel10.setText("Notas:");

        jButton2.setText("Orden:");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        t_orden.setEditable(false);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(t_notas_pago1))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(b_cancelar_pago1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(b_guardar_pago1, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(1, 1, 1))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jButton2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(t_orden, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 32, Short.MAX_VALUE)
                        .addComponent(l_importe_pago1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(t_importe_pago1, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton2)
                    .addComponent(t_orden, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(l_importe_pago1)
                    .addComponent(t_importe_pago1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(t_notas_pago1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(b_cancelar_pago1)
                    .addComponent(b_guardar_pago1))
                .addContainerGap(26, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout d_nuevo1Layout = new javax.swing.GroupLayout(d_nuevo1.getContentPane());
        d_nuevo1.getContentPane().setLayout(d_nuevo1Layout);
        d_nuevo1Layout.setHorizontalGroup(
            d_nuevo1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        d_nuevo1Layout.setVerticalGroup(
            d_nuevo1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane2.setViewportView(jTable1);

        pago_adicional.setTitle("Pagos Adicionales");
        pago_adicional.setModalityType(java.awt.Dialog.ModalityType.APPLICATION_MODAL);

        jPanel8.setBackground(new java.awt.Color(255, 255, 255));

        jPanel7.setBackground(new java.awt.Color(255, 255, 255));
        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "Información"));

        jLabel13.setText("Responsable:");

        t_responsable2.setEditable(false);
        t_responsable2.setBackground(new java.awt.Color(255, 255, 255));
        t_responsable2.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(0, 0, 0)));

        jLabel15.setText("Asignación:");

        t_asignacion1.setEditable(false);
        t_asignacion1.setBackground(new java.awt.Color(255, 255, 255));
        t_asignacion1.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(0, 0, 0)));

        jLabel16.setText("Importa a Pagar:");

        t_importe1.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        t_importe1.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.00"))));
        t_importe1.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        t_importe1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                t_importe1FocusLost(evt);
            }
        });
        t_importe1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                t_importe1ActionPerformed(evt);
            }
        });

        jLabel17.setText("T. Adicional:");

        t_trabajo.setEditable(false);
        t_trabajo.setBackground(new java.awt.Color(255, 255, 255));
        t_trabajo.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(0, 0, 0)));

        jLabel22.setText("O.T.");

        t_orden_trabajo.setEditable(false);
        t_orden_trabajo.setBackground(new java.awt.Color(255, 255, 255));
        t_orden_trabajo.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(0, 0, 0)));

        jLabel24.setText("Consumible: $");

        t_trabajo1.setEditable(false);
        t_trabajo1.setBackground(new java.awt.Color(255, 255, 255));
        t_trabajo1.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(0, 0, 0)));

        jButton8.setBackground(new java.awt.Color(2, 135, 242));
        jButton8.setForeground(new java.awt.Color(255, 255, 255));
        jButton8.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jButton8.setMaximumSize(new java.awt.Dimension(35, 18));
        jButton8.setMinimumSize(new java.awt.Dimension(35, 18));
        jButton8.setPreferredSize(new java.awt.Dimension(35, 15));
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
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
                        .addComponent(jLabel16)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(t_importe1, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel24)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(t_trabajo1, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel15)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(t_asignacion1, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jLabel13)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(t_responsable2, javax.swing.GroupLayout.PREFERRED_SIZE, 297, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel22)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(t_orden_trabajo, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel17)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(t_trabajo, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(t_responsable2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel22)
                    .addComponent(t_orden_trabajo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel17)
                    .addComponent(t_trabajo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 8, Short.MAX_VALUE)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel16)
                        .addComponent(t_importe1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel15)
                        .addComponent(t_asignacion1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel24)
                        .addComponent(t_trabajo1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jButton8, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        t_datos1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Id", "F. Avance", "% avance", "Imp. Pagado", "Notas"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Double.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, true, false, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        t_datos1.getTableHeader().setReorderingAllowed(false);
        jScrollPane5.setViewportView(t_datos1);

        b_menos3.setIcon(new ImageIcon("imagenes/boton_menos.png"));
        b_menos3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_menos3ActionPerformed(evt);
            }
        });

        b_mas3.setIcon(new ImageIcon("imagenes/boton_mas.png"));
        b_mas3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_mas3ActionPerformed(evt);
            }
        });

        b_pdf1.setBackground(new java.awt.Color(2, 135, 242));
        b_pdf1.setToolTipText("Imprimir PDF");
        b_pdf1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_pdf1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(b_mas3, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(b_menos3, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(b_pdf1, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jScrollPane5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 675, Short.MAX_VALUE)
                            .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(30, 30, 30))))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(4, 4, 4)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 243, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(b_pdf1, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(b_menos3, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(b_mas3, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(11, 11, 11))
        );

        javax.swing.GroupLayout pago_adicionalLayout = new javax.swing.GroupLayout(pago_adicional.getContentPane());
        pago_adicional.getContentPane().setLayout(pago_adicionalLayout);
        pago_adicionalLayout.setHorizontalGroup(
            pago_adicionalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        pago_adicionalLayout.setVerticalGroup(
            pago_adicionalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        d_nuevo2.setTitle("Pago de Trabajos Adicionales");
        d_nuevo2.setModalityType(java.awt.Dialog.ModalityType.APPLICATION_MODAL);

        jPanel9.setBackground(new java.awt.Color(255, 255, 255));
        jPanel9.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "Nuevo Importe pagado"));

        t_fecha_pago1.setEditable(false);

        b_calendario_pago1.setIcon(new ImageIcon("imagenes/calendario.png"));
        b_calendario_pago1.setText("Fecha de Avance:");
        b_calendario_pago1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_calendario_pago1ActionPerformed(evt);
            }
        });

        l_porcentaje_pago1.setText("avance:");

        l_nota_pago1.setText("Nota: El porcentaje se acumula con los anteriores y no debe ser mayor al 100%.");

        l_importe_pago2.setText("Importe Pagado: $");

        jLabel18.setText("%");

        b_guardar_pago2.setText("Guardar");
        b_guardar_pago2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_guardar_pago2ActionPerformed(evt);
            }
        });

        b_cancelar_pago2.setText("Cancelar");
        b_cancelar_pago2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_cancelar_pago2ActionPerformed(evt);
            }
        });

        t_porcentaje_pago1.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        t_porcentaje_pago1.setValue(0.0d);
        t_porcentaje_pago1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                t_porcentaje_pago1FocusLost(evt);
            }
        });
        t_porcentaje_pago1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                t_porcentaje_pago1ActionPerformed(evt);
            }
        });

        t_importe_pago2.setEditable(false);
        t_importe_pago2.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.00"))));
        t_importe_pago2.setValue(0.0d);

        jLabel19.setText("Notas:");

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addComponent(jLabel19)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(t_notas_pago2))
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addComponent(l_nota_pago1)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel9Layout.createSequentialGroup()
                                .addComponent(l_porcentaje_pago1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(t_porcentaje_pago1))
                            .addComponent(b_calendario_pago1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel9Layout.createSequentialGroup()
                                .addGap(0, 6, Short.MAX_VALUE)
                                .addComponent(t_fecha_pago1, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(214, 214, 214))
                            .addGroup(jPanel9Layout.createSequentialGroup()
                                .addComponent(jLabel18)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(l_importe_pago2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(t_importe_pago2, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(b_cancelar_pago2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(b_guardar_pago2, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(t_fecha_pago1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(b_calendario_pago1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(t_importe_pago2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(l_porcentaje_pago1)
                        .addComponent(jLabel18)
                        .addComponent(t_porcentaje_pago1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(l_importe_pago2)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel19)
                    .addComponent(t_notas_pago2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(b_cancelar_pago2)
                    .addComponent(b_guardar_pago2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(l_nota_pago1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout d_nuevo2Layout = new javax.swing.GroupLayout(d_nuevo2.getContentPane());
        d_nuevo2.getContentPane().setLayout(d_nuevo2Layout);
        d_nuevo2Layout.setHorizontalGroup(
            d_nuevo2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        d_nuevo2Layout.setVerticalGroup(
            d_nuevo2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        setBackground(new java.awt.Color(255, 255, 255));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "Especialidad"));

        grupo.add(b_mecanica);
        b_mecanica.setText("Mecánica");
        b_mecanica.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_mecanicaActionPerformed(evt);
            }
        });

        grupo.add(b_hojalateria);
        b_hojalateria.setSelected(true);
        b_hojalateria.setText("Hojalateria");
        b_hojalateria.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_hojalateriaActionPerformed(evt);
            }
        });

        grupo.add(b_suspencion);
        b_suspencion.setText("Suspensión");
        b_suspencion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_suspencionActionPerformed(evt);
            }
        });

        grupo.add(b_electrico);
        b_electrico.setText("Electrico");
        b_electrico.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_electricoActionPerformed(evt);
            }
        });

        grupo.add(b_pintura);
        b_pintura.setText("Pintura");
        b_pintura.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_pinturaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(b_electrico)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(b_pintura)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(b_suspencion))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(b_mecanica)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(b_hojalateria))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(b_suspencion)
                    .addComponent(b_pintura)
                    .addComponent(b_electrico))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(b_mecanica)
                    .addComponent(b_hojalateria)))
        );

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "Información"));

        jLabel1.setText("Responsable:");

        t_responsable.setEditable(false);
        t_responsable.setBackground(new java.awt.Color(255, 255, 255));
        t_responsable.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(0, 0, 0)));

        jLabel2.setText("Asignación:");

        t_asignacion.setEditable(false);
        t_asignacion.setBackground(new java.awt.Color(255, 255, 255));
        t_asignacion.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(0, 0, 0)));

        jLabel3.setText("Importa a Pagar: $");

        t_importe.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        t_importe.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.00"))));
        t_importe.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        t_importe.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                t_importeFocusLost(evt);
            }
        });
        t_importe.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                t_importeActionPerformed(evt);
            }
        });

        jLabel4.setText("Fecha Límite:");

        t_limite.setEditable(false);
        t_limite.setBackground(new java.awt.Color(255, 255, 255));
        t_limite.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(0, 0, 0)));

        jLabel5.setText("Monto x Hora:");

        jLabel6.setText("Horas:");

        t_monto.setEditable(false);
        t_monto.setBackground(new java.awt.Color(255, 255, 255));
        t_monto.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(0, 0, 0)));
        t_monto.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.00"))));
        t_monto.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        t_horas.setEditable(false);
        t_horas.setBackground(new java.awt.Color(255, 255, 255));
        t_horas.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(0, 0, 0)));
        t_horas.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        t_horas.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        jLabel23.setText("Consumible: $");

        t_consumibles.setEditable(false);
        t_consumibles.setBackground(new java.awt.Color(255, 255, 255));
        t_consumibles.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(0, 0, 0)));

        jButton7.setBackground(new java.awt.Color(2, 135, 242));
        jButton7.setForeground(new java.awt.Color(255, 255, 255));
        jButton7.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jButton7.setMaximumSize(new java.awt.Dimension(35, 18));
        jButton7.setMinimumSize(new java.awt.Dimension(35, 18));
        jButton7.setPreferredSize(new java.awt.Dimension(35, 15));
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
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
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(t_responsable)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(t_asignacion, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(t_limite, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(t_monto, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(t_horas, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(t_importe, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel23)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(t_consumibles, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 2, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(t_responsable, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(t_limite, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(t_asignacion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(t_monto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6)
                            .addComponent(t_horas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(t_importe, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel23)
                            .addComponent(t_consumibles, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        t_datos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Id", "Fecha de Avance", "% avance", "Importe Pagado", "Notas"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.Double.class, java.lang.Double.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, true, false, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        t_datos.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(t_datos);

        b_mas.setIcon(new ImageIcon("imagenes/boton_mas.png"));
        b_mas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_masActionPerformed(evt);
            }
        });

        b_menos.setIcon(new ImageIcon("imagenes/boton_menos.png"));
        b_menos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_menosActionPerformed(evt);
            }
        });

        jButton3.setToolTipText("Imprimir PDF");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton5.setBackground(new java.awt.Color(2, 135, 242));
        jButton5.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jButton5.setForeground(new java.awt.Color(255, 255, 255));
        jButton5.setText("Hoja Empleado");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        b_adicionales.setBackground(new java.awt.Color(2, 135, 242));
        b_adicionales.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        b_adicionales.setForeground(new java.awt.Color(255, 255, 255));
        b_adicionales.setText("Pagos Adicionales");
        b_adicionales.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_adicionalesActionPerformed(evt);
            }
        });

        b_general.setBackground(new java.awt.Color(2, 135, 242));
        b_general.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        b_general.setForeground(new java.awt.Color(255, 255, 255));
        b_general.setText("Avance General");
        b_general.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_generalActionPerformed(evt);
            }
        });

        t_fecha.setEditable(false);
        t_fecha.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        t_fecha.setText("DD-MM-AAAA");
        t_fecha.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                t_fechaActionPerformed(evt);
            }
        });

        b_fecha_siniestro.setBackground(new java.awt.Color(2, 135, 242));
        b_fecha_siniestro.setIcon(new ImageIcon("imagenes/calendario.png"));
        b_fecha_siniestro.setToolTipText("Calendario");
        b_fecha_siniestro.setMaximumSize(new java.awt.Dimension(32, 8));
        b_fecha_siniestro.setMinimumSize(new java.awt.Dimension(32, 8));
        b_fecha_siniestro.setPreferredSize(new java.awt.Dimension(32, 8));
        b_fecha_siniestro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_fecha_siniestroActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(b_mas, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(b_menos, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(b_adicionales)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(b_fecha_siniestro, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(t_fecha, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(b_general)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton5))
            .addComponent(jScrollPane1)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(8, 8, 8)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 142, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(b_menos, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(b_mas, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(b_adicionales)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton5)
                            .addComponent(b_general)
                            .addComponent(t_fecha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(b_fecha_siniestro, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void b_hojalateriaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_hojalateriaActionPerformed
        // TODO add your handling code here:
        if(b_hojalateria.isSelected())
            global="h";
        consulta();
    }//GEN-LAST:event_b_hojalateriaActionPerformed

    private void b_mecanicaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_mecanicaActionPerformed
        // TODO add your handling code here:
        if(b_mecanica.isSelected())
            global="m";
        consulta();
    }//GEN-LAST:event_b_mecanicaActionPerformed

    private void b_suspencionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_suspencionActionPerformed
        // TODO add your handling code here:
        if(b_suspencion.isSelected())
            global="s";
        consulta();
    }//GEN-LAST:event_b_suspencionActionPerformed

    private void b_electricoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_electricoActionPerformed
        if(b_electrico.isSelected())
            global="e";
        consulta();
    }//GEN-LAST:event_b_electricoActionPerformed

    private void b_pinturaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_pinturaActionPerformed
        if(b_pintura.isSelected())
            global="p";
        consulta();
    }//GEN-LAST:event_b_pinturaActionPerformed

    private void t_importeFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_t_importeFocusLost
        // TODO add your handling code here:
        try{
            t_importe.commitEdit();
            t_importe.commitEdit();
        }catch(Exception e){}
        double suma=0.0d, porcentaje=0.0d;
        double nuevo=((Number)t_importe.getValue()).doubleValue();
        
        for(int x=0; x<t_datos.getRowCount(); x++)
        {
            porcentaje+=(double)t_datos.getValueAt(x, 2);
            suma+=(double)t_datos.getValueAt(x, 3);
        }
        if(porcentaje<100.0d)
        {
            if(suma<nuevo)
            {
                Session session = HibernateUtil.getSessionFactory().openSession();
                try
                {
                    session.beginTransaction().begin();
                    Orden orden = (Orden)session.get(Orden.class, Integer.parseInt(ord));
                    switch(global)
                    {
                        case "h":
                            orden.setImporteHojalateria(((Number)t_importe.getValue()).doubleValue());
                            break;
                        case "m":
                            orden.setImporteMecanica(((Number)t_importe.getValue()).doubleValue());
                            break;
                        case "s":
                            orden.setImporteSuspension(((Number)t_importe.getValue()).doubleValue());
                            break;
                        case "e":
                            orden.setImporteElectrico(((Number)t_importe.getValue()).doubleValue());
                            break;
                        case"p":
                            orden.setImportePintura(((Number)t_importe.getValue()).doubleValue());
                            break;
                    }
                    session.save(orden);
                    session.beginTransaction().commit();
                }catch(Exception e)
                {
                    session.beginTransaction().rollback();
                    System.out.println(e);
                }
                if(session!=null)
                    if(session.isOpen())
                        session.close();
                consulta();
            }
            else
            {
                t_importe.setValue(anterior);
                if(suma==nuevo)
                    JOptionPane.showMessageDialog(this, "La cantidad es igual a los pagos realizados y el porcentaje de avance es de "+porcentaje+"%");
                else
                    JOptionPane.showMessageDialog(this, "La cantidad es menor a los pagos realizados");
            }
        }
        else
        {
            t_importe.setValue(anterior);
            JOptionPane.showMessageDialog(this, "Ya se pago el 100% de la reparación");
        }
    }//GEN-LAST:event_t_importeFocusLost

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
       h=new Herramientas(usr, 0);
       h.session(sessionPrograma);
       Session session = HibernateUtil.getSessionFactory().openSession();
       try
       {
           session.beginTransaction().begin();
           BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.WINANSI, BaseFont.NOT_EMBEDDED);
           
           PDF reporte = new PDF();
           Date fecha = new Date();
           DecimalFormat formatoPorcentaje = new DecimalFormat("#,##0.00");
           formatoPorcentaje.setMinimumFractionDigits(2);
           
           DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyyHH-mm-ss");//YYYY-MM-DD HH:MM:SS
           String valor=dateFormat.format(fecha);
           File folder = new File("reportes/"+ord);
           folder.mkdirs();
           reporte.Abrir(PageSize.LETTER.rotate(), "Destajos", "reportes/"+ord+"/"+valor+"-detalle-destajo.pdf");
           Font font = new Font(Font.FontFamily.HELVETICA, 6, Font.BOLD);
           BaseColor contenido=BaseColor.WHITE;
           int centro=Element.ALIGN_CENTER;
           int izquierda=Element.ALIGN_LEFT;
           int derecha=Element.ALIGN_RIGHT;
           float tam[]=new float[]{150,80,90,70,250};
           PdfPTable tabla=reporte.crearTabla(5, tam, 100, Element.ALIGN_LEFT);
           
           cabecera2(reporte, bf, tabla);
           Double sum=0.0d, avance=0.0d;
           for(int x=0; x<t_datos.getRowCount(); x++)
           {
               tabla.addCell(reporte.celda(t_datos.getValueAt(x, 1).toString(), font, contenido, centro, 0,1,Rectangle.RECTANGLE));
               tabla.addCell(reporte.celda(formatoPorcentaje.format(Double.parseDouble(t_datos.getValueAt(x, 2).toString()))+"%", font, contenido, centro, 0,1,Rectangle.RECTANGLE));
               tabla.addCell(reporte.celda("$"+formatoPorcentaje.format(Double.parseDouble(t_datos.getValueAt(x, 3).toString())), font, contenido, derecha, 2,1,Rectangle.RECTANGLE));
               tabla.addCell(reporte.celda(t_datos.getValueAt(x, 4).toString(), font, contenido, izquierda, 0,1,Rectangle.RECTANGLE));
               sum =sum+Double.parseDouble(t_datos.getValueAt(x, 3).toString());
               avance = avance+Double.parseDouble(t_datos.getValueAt(x, 2).toString());
           }
           tabla.addCell(reporte.celda("Avance General:", font, contenido, derecha, 0,1,Rectangle.NO_BORDER));
           tabla.addCell(reporte.celda(""+formatoPorcentaje.format(avance)+"%", font, contenido, centro, 0,1,Rectangle.RECTANGLE));
           tabla.addCell(reporte.celda("Importe Pagado al día de Hoy:", font, contenido, derecha, 0,1,Rectangle.NO_BORDER));
           tabla.addCell(reporte.celda("$"+formatoPorcentaje.format(sum), font, contenido, derecha, 0,1,Rectangle.RECTANGLE));
           tabla.addCell(reporte.celda("", font, contenido, izquierda, 0,1,Rectangle.NO_BORDER));
           
           Double rest=Double.parseDouble(t_importe.getValue().toString())-sum;
           tabla.addCell(reporte.celda("", font, contenido, derecha, 2,1,Rectangle.NO_BORDER));
           tabla.addCell(reporte.celda("Importe Restante por Pagar:", font, contenido, derecha, 0,1,Rectangle.NO_BORDER));
           tabla.addCell(reporte.celda("$"+formatoPorcentaje.format(rest), font, contenido, derecha, 0,1,Rectangle.RECTANGLE));
           tabla.addCell(reporte.celda("", font, contenido, izquierda, 0,1,Rectangle.NO_BORDER));
            
           tabla.setHeaderRows(1);
           reporte.agregaObjeto(tabla);
           reporte.cerrar();
           reporte.visualizar("reportes/"+ord+"/"+valor+"-detalle-destajo.pdf");
           
       }catch(Exception e){
           e.printStackTrace();
           JOptionPane.showMessageDialog(this, "No se pudo realizar el reporte si el archivo esta abierto.");
       }
       if(session!=null)
            if(session.isOpen())
            {
                session.flush();
                session.clear();
                session.close();
            }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:
        h=new Herramientas(usr, 0);
        h.session(sessionPrograma);
        Session session = HibernateUtil.getSessionFactory().openSession();
        try
        {
            session.beginTransaction().begin();
            BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.WINANSI, BaseFont.NOT_EMBEDDED);
            //Orden ord=buscaApertura();
            PDF reporte = new PDF();
            Date fecha = new Date();
            DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyyHH-mm-ss");//YYYY-MM-DD HH:MM:SS
            String valor=dateFormat.format(fecha);
            File folder = new File("reportes/"+ord);
            folder.mkdirs();
            reporte.Abrir(PageSize.LETTER.rotate(), "Valuación", "reportes/"+ord+"/"+valor+"-destajo.pdf");
            Font font = new Font(Font.FontFamily.HELVETICA, 6, Font.BOLD);
            BaseColor contenido=BaseColor.WHITE;
            int centro=Element.ALIGN_CENTER;
            int izquierda=Element.ALIGN_LEFT;
            int derecha=Element.ALIGN_RIGHT;
            float tam[]=new float[]{150,50,100,300};
            PdfPTable tabla=reporte.crearTabla(4, tam, 100, Element.ALIGN_LEFT);
            
            cabecera(reporte, bf, tabla);
            
                session.beginTransaction().begin();
               //Orden dato = (Orden)session.get(Orden.class, Integer.parseInt(this.ord));
               //List cuentas=null;
                for(int x=0; x<21; x++)
                {
                    tabla.addCell(reporte.celda(" \n ", font, contenido, izquierda, 0,1,Rectangle.RECTANGLE));
                    tabla.addCell(reporte.celda(" \n ", font, contenido, derecha, 0,1,Rectangle.RECTANGLE));
                    tabla.addCell(reporte.celda(" \n ", font, contenido, derecha, 0,1,Rectangle.RECTANGLE));
                    tabla.addCell(reporte.celda(" \n ", font, contenido, derecha, 0,1,Rectangle.RECTANGLE));
                }
                session.beginTransaction().rollback();
            
            tabla.setHeaderRows(1);
            reporte.agregaObjeto(tabla);
            reporte.cerrar();
            reporte.visualizar("reportes/"+ord+"/"+valor+"-destajo.pdf");
            
        }catch(Exception e)
        {
            System.out.println(e);
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "No se pudo realizar el reporte si el archivo esta abierto.");
        }
        if(session!=null)
            if(session.isOpen())
            {
                session.flush();
                session.clear();
                session.close();
            }
    }//GEN-LAST:event_jButton5ActionPerformed

    private void b_masActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_masActionPerformed
        // TODO add your handling code here:
        Session session = HibernateUtil.getSessionFactory().openSession();
        try
        {
            Dimension d = Toolkit.getDefaultToolkit().getScreenSize(); 
            d_nuevo.setSize(530, 280);
            d_nuevo.setLocation((d.width/2)-265, (d.height/2)-140);
            d_nuevo.setVisible(true);
        }catch(Exception e)
        {
            System.out.println(e);
        }
        if(session!=null)
        {
            if(session.isOpen())
            {
                session.flush();
                session.clear();
                session.close();
            }
        }
        buscaDestajos();
    }//GEN-LAST:event_b_masActionPerformed

    private void b_menosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_menosActionPerformed
        // TODO add your handling code here:
        Session session = HibernateUtil.getSessionFactory().openSession();
        try
        {
            usr = (Usuario)session.get(Usuario.class, usr.getIdUsuario());
            if(usr.getRespaldar()==true)
            {
                if(t_datos.getSelectedRow()>-1)
                {
                    int opt=JOptionPane.showConfirmDialog(this, "¡Confirma que deseas eliminar la partidas seleccionadas");
                    if(opt==0)
                    {
                        int [] renglones=t_datos.getSelectedRows();
                        for(int x=0; x<renglones.length;x++)
                        {
                            session.beginTransaction().begin();
                            Hibernate.entidades.Destajo elimina=(Hibernate.entidades.Destajo) session.get(Hibernate.entidades.Destajo.class, Integer.parseInt(t_datos.getValueAt(renglones[x], 0).toString()));
                            if(elimina!=null)
                            {
                                session.delete(elimina);
                                session.getTransaction().commit();
                            }
                            else
                            {
                                session.getTransaction().rollback();
                                JOptionPane.showMessageDialog(null, "La partida ya fue eliminada");    
                            }
                        }
                        buscaDestajos();
                    }
                }
                else
                {
                    javax.swing.JOptionPane.showMessageDialog(null, "Debes seleccionar una partida de la tabla para porde eliminarla");
                }
            }
            else
            {
                if(session.isOpen()==true)
                {
                    session.flush();
                    session.clear();
                    session.close();
                }
                JOptionPane.showMessageDialog(null, "¡Acceso denegado!");
            
            }
        }
        catch(Exception e)
        {
            System.out.println(e);
            JOptionPane.showMessageDialog(null, "¡No se pudo eliminar la partida seleccionada!");
        }
        if(session!=null)
            if(session.isOpen())
            {
                session.flush();
                session.clear();
                session.close();
            }
    }//GEN-LAST:event_b_menosActionPerformed

    private void b_guardar_pagoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_guardar_pagoActionPerformed
        // TODO add your handling code here:
        Double importe=0.0d, total=0.0d;
        if(t_fecha_pago.getText().compareTo("")!=0)
        {
            if(((Number)t_porcentaje_pago.getValue()).doubleValue()>0.0d)
            {
                Session session = HibernateUtil.getSessionFactory().openSession();
                try
                {
                    session.beginTransaction().begin();
                    usr = (Usuario)session.get(Usuario.class, usr.getIdUsuario());
                    Orden trabajo=(Orden)session.get(Orden.class, Integer.parseInt(ord));
                    Hibernate.entidades.Destajo nuevo= new Hibernate.entidades.Destajo();
                    
                    String [] campos = t_fecha_pago.getText().split("-");
                    Calendar calendario = Calendar.getInstance();
                    calendario.set(Calendar.MONTH, Integer.parseInt(campos[1])-1);
                    calendario.set(Calendar.YEAR, Integer.parseInt(campos[2]));
                    calendario.set(Calendar.DAY_OF_MONTH, Integer.parseInt(campos[0]));
                    nuevo.setFechaDestajo(calendario.getTime());
                    nuevo.setAvance(((Number)t_porcentaje_pago.getValue()).doubleValue());
                    nuevo.setNotas(t_notas_pago.getText());
                    nuevo.setOrden(trabajo);
                    nuevo.setEspecialidad(global);
                    nuevo.setUsuario(usr);
                    calendario = Calendar.getInstance();
                    nuevo.setFechaModifico(calendario.getTime());
                    
                    nuevo.setImporte(((Number)t_importe_pago.getValue()).doubleValue());
                    nuevo.setTotal(((Number)t_importe_pago.getValue()).doubleValue());
                    session.save(nuevo);
                    session.beginTransaction().commit();
                    t_fecha_pago.setText("");
                    t_porcentaje_pago.setValue(0.0d);
                    t_importe_pago.setValue(0.0d);
                    t_notas_pago.setText("");
                    d_nuevo.setVisible(false);
                    
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                    System.out.println(e);
                    session.getTransaction().rollback();
                    JOptionPane.showMessageDialog(null, "¡No se pudo Agregar el destajo!");
                }
                if(session!=null)
                {
                    if(session.isOpen())
                    {
                        session.flush();
                        session.clear();
                        session.close();
                    }
                
                }
            }else
            {
                JOptionPane.showMessageDialog(null, "¡El porcentaje de valor debe ser mayor a 0.00!");
            }
        }else
        {
            JOptionPane.showMessageDialog(null, "¡Debe ingresar una fecha de avance!");
        }
    }//GEN-LAST:event_b_guardar_pagoActionPerformed

    private void b_calendario_pagoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_calendario_pagoActionPerformed
        // TODO add your handling code here:
        calendario cal =new calendario(new javax.swing.JFrame(), true, false);
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        cal.setLocation((d.width/2)-(cal.getWidth()/2), (d.height/2)-(cal.getHeight()/2));
        cal.setVisible(true);
        Calendar miCalendario=cal.getReturnStatus();
        if(miCalendario!=null)
        {
            String dia=Integer.toString(miCalendario.get(Calendar.DATE));
            String mes = Integer.toString(miCalendario.get(Calendar.MONTH)+1);
            String anio = Integer.toString(miCalendario.get(Calendar.YEAR));
            t_fecha_pago.setText(dia+"-"+mes+"-"+anio);
        }
        else
        t_fecha_pago.setText("");
    }//GEN-LAST:event_b_calendario_pagoActionPerformed

    private void t_porcentaje_pagoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_t_porcentaje_pagoActionPerformed
        // TODO add your handling code here:
        try
        {
            t_porcentaje_pago.commitEdit();
        }catch(Exception e){}
        t_notas_pago.requestFocus();
    }//GEN-LAST:event_t_porcentaje_pagoActionPerformed

    private void t_porcentaje_pagoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_t_porcentaje_pagoFocusLost
        // TODO add your handling code here:
        try
        {
            t_porcentaje_pago.commitEdit();
        }catch(Exception e){}
        
        double nuevo_porcentaje=((Number)t_porcentaje_pago.getValue()).doubleValue();
        double total_importe=((Number)t_importe.getValue()).doubleValue();
        double suma_porcentaje=0.0d, suma_importe=0.0d;
        for(int x=0; x<t_datos.getRowCount(); x++)
        {
            suma_porcentaje+=(double)t_datos.getValueAt(x, 2);
            suma_importe+=(double)t_datos.getValueAt(x, 3);
        }
        double total_porcentaje=suma_porcentaje+nuevo_porcentaje;
        
        if(total_porcentaje<=100.0d)
        {   
            double importe_porcentaje=(total_importe/100.0d)*total_porcentaje;
            t_importe_pago.setValue(importe_porcentaje-suma_importe);
        }
        else
        {
            t_porcentaje_pago.setValue(0.0d);
            t_importe_pago.setValue(0.0d);
            JOptionPane.showMessageDialog(null, "¡el porcentaje acumulado supera el 100%!");
        }
    }//GEN-LAST:event_t_porcentaje_pagoFocusLost

    private void t_importeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_t_importeActionPerformed
        // TODO add your handling code here:
       b_mas.requestFocus();
    }//GEN-LAST:event_t_importeActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton4ActionPerformed

    private void cbActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbActionPerformed
        // TODO add your handling code here:
        buscaPagosAdicionales();
    }//GEN-LAST:event_cbActionPerformed

    private void b_generalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_generalActionPerformed
        // TODO add your handling code here:
        h=new Herramientas(usr, 0);
        h.session(sessionPrograma);
        Session session = HibernateUtil.getSessionFactory().openSession();
        try
        {
            session.beginTransaction().begin();
            BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.WINANSI, BaseFont.NOT_EMBEDDED);
            
            PDF reporte = new PDF();
            Date fecha = new Date();
            DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyyHH-mm-ss");//YYYY-MM-DD HH:MM:SS
            String valor=dateFormat.format(fecha);
            File folder = new File("reportes/"+ord);
            folder.mkdirs();
            reporte.Abrir(PageSize.LETTER.rotate(), "Valuación", "reportes/"+ord+"/"+valor+"-destajo.pdf");
            Font font = new Font(Font.FontFamily.HELVETICA, 6, Font.BOLD);
            BaseColor contenido=BaseColor.WHITE;
            int centro=Element.ALIGN_CENTER;
            int izquierda=Element.ALIGN_LEFT;
            int derecha=Element.ALIGN_RIGHT;
            float tam[]=new float[]{70,100,100,50,90,250};
            PdfPTable tabla=reporte.crearTabla(6, tam, 100, Element.ALIGN_LEFT);
            
            DecimalFormat formatoPorcentaje = new DecimalFormat("#,##0.00");
            formatoPorcentaje.setMinimumFractionDigits(2);   
            
            cabecera1(reporte, bf, tabla);
            
                session.beginTransaction().begin();
                String consulta="select destajo.id_orden, "
                        + "if(especialidad='h', 'Hojalateria', if(especialidad='m', 'Mecanica', if(especialidad='s', 'Suspensión', if(especialidad='e', 'Electrico', 'Pintura')))) as especialidad, "
                        + "fecha_destajo, avance, importe, total,notas "
                        + "from destajo left join orden "
                        + "on destajo.id_orden=orden.id_orden where ( (orden.r_hojalateria="+id_empleado+" and especialidad='h') or (orden.r_mecanica="+id_empleado+" and especialidad='m') or (orden.r_suspension="+id_empleado+" and especialidad='s') or (orden.r_electrico="+id_empleado+" and especialidad='e') or (orden.r_pintura="+id_empleado+" and especialidad='p')) ";
                
                String consulta1="select id_orden, fecha, porcentaje, pago_adicional.importe, total,nota from pago_adicional\n" +
"inner join trabajo_extra on trabajo_extra.id_adicional=pago_adicional.id_trabajo_extra  where id_empleado="+emp.getIdEmpleado();
                
                if(t_fecha.getText().compareTo("DD-MM-AAAA")!=0)
                {
                    String [] campos = t_fecha.getText().split("-");
                    String aux="";
                    aux+=campos[2]+"-"+campos[1]+"-"+campos[0];//Calendar.MONTH, Integer.parseInt(campos[1])-1);
                    consulta+=" and fecha_destajo='"+aux+"' order by destajo.id_orden";
                    
                    consulta1+=" and fecha='"+aux+"' order by trabajo_extra.id_orden";
                }
                else
                {
                    consulta+=" order by destajo.id_orden";
                    consulta1+=" order by trabajo_extra.id_orden";
                }
                
                try
                {
                    Query q = session.createSQLQuery(consulta);
                    q.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
                    List resultList = q.list();
                    
                    Query q1 = session.createSQLQuery(consulta1);
                    q1.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
                    List resultList1 = q1.list();
                    double suma=0.0d;
                    for (Object o : resultList) 
                    {
                        java.util.HashMap respuesta=(java.util.HashMap)o;
                        
                        tabla.addCell(reporte.celda(respuesta.get("id_orden").toString(), font, contenido, izquierda, 0,1,Rectangle.RECTANGLE));
                        tabla.addCell(reporte.celda(respuesta.get("especialidad").toString(), font, contenido, izquierda, 0,1,Rectangle.RECTANGLE));
                        tabla.addCell(reporte.celda(respuesta.get("fecha_destajo").toString(), font, contenido, derecha, 0,1,Rectangle.RECTANGLE));
                        
                        //% avance general
                        String [] campos2 = t_fecha.getText().split("-");
                        String aux2="";
                        aux2+=campos2[2]+"-"+campos2[1]+"-"+campos2[0];//Calendar.MONTH, Integer.parseInt(campos[1])-1);
                        
                        Query query = session.createSQLQuery("select sum(avance) as avance from destajo where id_orden="+respuesta.get("id_orden")+" and especialidad='"+global+"' and fecha_destajo<='"+aux2+"'");
                        query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
                        ArrayList avance=(ArrayList)query.list();
                        java.util.HashMap map=(java.util.HashMap)avance.get(0);
                        
                        tabla.addCell(reporte.celda(formatoPorcentaje.format(Double.parseDouble(map.get("avance").toString())), font, contenido, derecha, 0,1,Rectangle.RECTANGLE));
                        suma +=(Double)respuesta.get("importe");
                        tabla.addCell(reporte.celda(formatoPorcentaje.format((Double)respuesta.get("importe")), font, contenido, derecha, 0,1,Rectangle.RECTANGLE));
                        tabla.addCell(reporte.celda(respuesta.get("notas").toString(), font, contenido, izquierda, 0,1,Rectangle.RECTANGLE));
                    }
                    resultList=null;
                    
                    for (Object o : resultList1) 
                    {
                        java.util.HashMap respuesta1=(java.util.HashMap)o;
                        if(respuesta1.get("id_orden")!=null)
                            tabla.addCell(reporte.celda(respuesta1.get("id_orden").toString(), font, contenido, izquierda, 0,1,Rectangle.RECTANGLE));
                        else
                            tabla.addCell(reporte.celda("", font, contenido, izquierda, 0,1,Rectangle.RECTANGLE));
                        tabla.addCell(reporte.celda("ADICIONALES", font, contenido, izquierda, 0,1,Rectangle.RECTANGLE));
                        tabla.addCell(reporte.celda(respuesta1.get("fecha").toString(), font, contenido, derecha, 0,1,Rectangle.RECTANGLE));
                        tabla.addCell(reporte.celda(formatoPorcentaje.format((Double)respuesta1.get("porcentaje")), font, contenido, derecha, 0,1,Rectangle.RECTANGLE));
                        suma +=(Double)respuesta1.get("importe");
                        tabla.addCell(reporte.celda(formatoPorcentaje.format((Double)respuesta1.get("importe")), font, contenido, derecha, 0,1,Rectangle.RECTANGLE));
                        tabla.addCell(reporte.celda(respuesta1.get("nota").toString(), font, contenido, izquierda, 0,1,Rectangle.RECTANGLE));
                    }
                    tabla.addCell(reporte.celda(" ", font, contenido, izquierda, 4,1,Rectangle.NO_BORDER));
                    tabla.addCell(reporte.celda(formatoPorcentaje.format(suma), font, contenido, derecha, 0,1,Rectangle.RECTANGLE));
                    tabla.addCell(reporte.celda(" ", font, contenido, izquierda, 0,1,Rectangle.NO_BORDER));
                   resultList1=null;
                }catch(Exception e)
                {
                    System.out.println(e);
                }
                if(session!=null)
                    if(session.isOpen())
                        session.close();
            
            tabla.setHeaderRows(1);
            reporte.agregaObjeto(tabla);
            reporte.cerrar();
            reporte.visualizar("reportes/"+ord+"/"+valor+"-destajo.pdf");
            
        }catch(Exception e)
        {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "No se pudo realizar el reporte si el archivo esta abierto.");
        }
        if(session!=null)
            if(session.isOpen())
            {
                session.flush();
                session.clear();
                session.close();
            }
        
    }//GEN-LAST:event_b_generalActionPerformed

    private void b_fecha_siniestroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_fecha_siniestroActionPerformed
        calendario cal =new calendario(new javax.swing.JFrame(), true, false);
        cal.setTitle("Fecha");
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        cal.setLocation((d.width/2)-(cal.getWidth()/2), (d.height/2)-(cal.getHeight()/2));
        cal.setVisible(true);
        Calendar miCalendario=cal.getReturnStatus();
        if(miCalendario!=null)
        {
            String dia=Integer.toString(miCalendario.get(Calendar.DATE));;
            String mes = Integer.toString(miCalendario.get(Calendar.MONTH)+1);
            String anio = Integer.toString(miCalendario.get(Calendar.YEAR));
            t_fecha.setText(dia+"-"+mes+"-"+anio);
            b_general.requestFocus();
        }
        else
        t_fecha.setText("DD-MM-AAAA");
    }//GEN-LAST:event_b_fecha_siniestroActionPerformed

    private void b_guardar_pago1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_guardar_pago1ActionPerformed
        // TODO add your handling code here:
        
        if(((Number)t_importe_pago1.getValue()).doubleValue()>0.0d){
            Session session = HibernateUtil.getSessionFactory().openSession();
            try
            {
                session.beginTransaction().begin();
               //usr = (Usuario)session.get(Usuario.class, usr.getIdUsuario());
                TrabajoExtra nuevo= new TrabajoExtra();

                Calendar calendario = Calendar.getInstance();
                calendario = Calendar.getInstance();
                nuevo.setFechaDestajo(calendario.getTime());
               // nuevo.setAvance(((Number)t_porcentaje_pago1.getValue()).doubleValue());
                nuevo.setImporte(((Number)t_importe_pago1.getValue()).doubleValue());
                nuevo.setNotas(t_notas_pago1.getText());
                if(t_orden.getText().compareTo("")!=0)
                {
                    Orden trabajo=(Orden)session.get(Orden.class, Integer.parseInt(t_orden.getText()));
                    nuevo.setOrden(trabajo);
                }
                emp=(Empleado)session.get(Empleado.class, emp.getIdEmpleado());
                nuevo.setEmpleado(emp);
                nuevo.setAutorizado(false);
                //calendario = Calendar.getInstance();
                //nuevo.setFechaModifico(calendario.getTime());

                session.save(nuevo);
                session.beginTransaction().commit();

               // t_porcentaje_pago1.setValue(0.0d);
                t_importe_pago1.setValue(0.0d);
                t_notas_pago1.setText("");
                t_orden.setText("");
                d_nuevo1.setVisible(false);
            }
            catch(Exception e)
            {
                System.out.println(e);
                session.getTransaction().rollback();
                JOptionPane.showMessageDialog(null, "¡No se pudo Agregar el destajo!");
            }
            if(session!=null)
            {
                if(session.isOpen())
                {
                    session.flush();
                    session.clear();
                    session.close();
                }

            }
        }else{
            JOptionPane.showMessageDialog(null, "¡Debe ingresar una importe!");
        }
        
    }//GEN-LAST:event_b_guardar_pago1ActionPerformed

    private void b_cancelar_pago1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_cancelar_pago1ActionPerformed
        // TODO add your handling code here:
       // t_porcentaje_pago1.setValue(0.0d);
        t_importe_pago1.setValue(0.0d);
        t_notas_pago1.setText("");
        t_orden.setText("");
        d_nuevo1.setVisible(false);
    }//GEN-LAST:event_b_cancelar_pago1ActionPerformed

    private void b_cancelar_pagoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_cancelar_pagoActionPerformed
        // TODO add your handling code here:
        t_fecha_pago.setText("");
        t_porcentaje_pago.setValue(0.0d);
        t_importe_pago.setValue(0.0d);
        t_notas_pago.setText("");
        d_nuevo.setVisible(false);
    }//GEN-LAST:event_b_cancelar_pagoActionPerformed

    private void b_adicionalesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_adicionalesActionPerformed
        // TODO add your handling code here:
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize(); 
        d_pagos.setSize(726, 362);
        d_pagos.setLocation((d.width/2)-363, (d.height/2)-181);
        t_responsable1.setText(t_responsable.getText());
        t_monto1.setText(t_monto.getText());
        buscaPagosAdicionales();
        d_pagos.setVisible(true);
    }//GEN-LAST:event_b_adicionalesActionPerformed

    private void b_mas1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_mas1ActionPerformed
        // TODO add your handling code here:
        Session session = HibernateUtil.getSessionFactory().openSession();
        usr = (Usuario)session.get(Usuario.class, usr.getIdUsuario());
        try
        {
            if(usr.getGeneraAdicional()==true){
                t_importe_pago1.setValue(0.0d);
                t_notas_pago1.setText("");
                t_orden.setText("");

                Dimension d = Toolkit.getDefaultToolkit().getScreenSize(); 
                d_nuevo1.setSize(530, 200);
                d_nuevo1.setLocation((d.width/2)-265, (d.height/2)-140);
                d_nuevo1.setVisible(true);
            }else{
                JOptionPane.showMessageDialog(null, "!Acceso Denegado¡");
            }
        }catch(Exception e)
        {
            System.out.println(e);
        }
        if(session!=null)
        {
            if(session.isOpen())
            {
                session.flush();
                session.clear();
                session.close();
            }
        }
        buscaPagosAdicionales();
    }//GEN-LAST:event_b_mas1ActionPerformed

    private void b_menos1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_menos1ActionPerformed
        // TODO add your handling code here:
        Session session = HibernateUtil.getSessionFactory().openSession();
        try
        {
            usr = (Usuario)session.get(Usuario.class, usr.getIdUsuario());
            
            if(usr.getRespaldar()==true)
            {
                if(t_adicionales.getSelectedRow()>-1)
                {
                    int opt=JOptionPane.showConfirmDialog(null, "¡Confirma que deseas eliminar ep pago seleccionado");
                    if(opt==0)
                    {
                        if(usr.getGeneraAdicional()==true){
                        int [] renglones=t_adicionales.getSelectedRows();
                            for(int x=0; x<renglones.length;x++)
                            {
                                session.beginTransaction().begin();
                                TrabajoExtra elimina=(TrabajoExtra) session.get(TrabajoExtra.class, Integer.parseInt(t_adicionales.getValueAt(renglones[x], 0).toString()));
                                if(elimina!=null)
                                {
                                    if(elimina.getAutorizado()==false){

                                        session.delete(elimina);
                                        session.getTransaction().commit();

                                    }else{
                                        session.getTransaction().rollback();
                                        JOptionPane.showMessageDialog(null, "La partida esta autorizada");    
                                    }                            
                                }
                                else
                                {
                                    session.getTransaction().rollback();
                                    JOptionPane.showMessageDialog(null, "La partida ya fue eliminada");    
                                }
                            }
                            buscaPagosAdicionales();
                        }else{
                            JOptionPane.showMessageDialog(null, "¡Acceso Denegado!");    
                        }
                    }
                }
                else
                {
                    javax.swing.JOptionPane.showMessageDialog(null, "Debes seleccionar una partida de la tabla para porde eliminarla");
                }
            }
            else
            {
                if(session.isOpen()==true)
                {
                    session.flush();
                    session.clear();
                    session.close();
                }
                JOptionPane.showMessageDialog(null, "¡Acceso denegado!");
            
            }
        }
        catch(Exception e)
        {
            System.out.println(e);
            JOptionPane.showMessageDialog(null, "¡No se pudo eliminar la partida seleccionada!");
        }
        if(session!=null)
            if(session.isOpen())
            {
                session.flush();
                session.clear();
                session.close();
            }
    }//GEN-LAST:event_b_menos1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
         buscaOrden obj = new buscaOrden(new javax.swing.JFrame(), true, this.usr,0, configuracion);
        obj.t_busca.requestFocus();
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        obj.setLocation((d.width/2)-(obj.getWidth()/2), (d.height/2)-(obj.getHeight()/2));
        obj.setVisible(true);        
        orden_act=obj.getReturnStatus();
        if (orden_act!=null)
        {
            this.t_orden.setText(""+orden_act.getIdOrden());
        }
        else
        {
            t_orden.setText("");
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void t_adicionalesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_t_adicionalesMouseClicked
        // TODO add your handling code here:+
        if(evt.getClickCount()==2){
            if(usr.getGeneraAdicional()==true){
                if((Boolean)t_adicionales.getValueAt(t_adicionales.getSelectedRow(), 6)==true){
                    busca_pago_adicional(String.valueOf(t_adicionales.getValueAt(t_adicionales.getSelectedRow(), 0).toString()));
                    Dimension d = Toolkit.getDefaultToolkit().getScreenSize(); 
                    pago_adicional.setSize(726, 362);
                    pago_adicional.setLocation((d.width/2)-363, (d.height/2)-181);
                    pago_adicional.setVisible(true);
                }else{
                    JOptionPane.showMessageDialog(null, "El trabajo adicional no esta autorizado");
                }
            }else{
                JOptionPane.showMessageDialog(null, "!Acceso Denegado¡");
            }
        }
    }//GEN-LAST:event_t_adicionalesMouseClicked

    private void t_importe1FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_t_importe1FocusLost
        // TODO add your handling code here:
        try{
            t_importe1.commitEdit();
            t_importe1.commitEdit();
        }catch(Exception e){}
        double suma=0.0d, porcentaje=0.0d;
        double nuevo=((Number)t_importe1.getValue()).doubleValue();
        
        for(int x=0; x<t_datos1.getRowCount(); x++)
        {
            porcentaje+=(double)t_datos1.getValueAt(x, 2);
            suma+=(double)t_datos1.getValueAt(x, 3);
        }
        if(porcentaje<100.0d)
        {
            Session session = HibernateUtil.getSessionFactory().openSession();
            TrabajoExtra trabajo = (TrabajoExtra)session.get(TrabajoExtra.class, Integer.parseInt(t_trabajo.getText()));
            
            if(suma<nuevo)
            {
                try
                {
                    session.beginTransaction().begin();
                    trabajo.setImporte(((Number)t_importe1.getValue()).doubleValue());
                    session.update(trabajo);
                    session.beginTransaction().commit();
                }catch(Exception e)
                {
                    session.beginTransaction().rollback();
                    System.out.println(e);
                }
                if(session!=null)
                    if(session.isOpen())
                        session.close();
                consulta();
            }
            else
            {
                t_importe1.setValue(anterior1);
                if(suma==nuevo)
                    JOptionPane.showMessageDialog(null, "La cantidad es igual a los pagos realizados y el porcentaje de avance es de "+porcentaje+"%");
                else
                    JOptionPane.showMessageDialog(null, "La cantidad es menor a los pagos realizados");
            }
        }
        else
        {
            t_importe1.setValue(anterior1);
            JOptionPane.showMessageDialog(null, "Ya se pago el 100% de la reparación");
        }
    }//GEN-LAST:event_t_importe1FocusLost

    private void t_importe1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_t_importe1ActionPerformed
        // TODO add your handling code here:
        b_mas3.requestFocus();
    }//GEN-LAST:event_t_importe1ActionPerformed

    private void b_calendario_pago1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_calendario_pago1ActionPerformed
        // TODO add your handling code here:
        calendario cal =new calendario(new javax.swing.JFrame(), true, false);
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        cal.setLocation((d.width/2)-(cal.getWidth()/2), (d.height/2)-(cal.getHeight()/2));
        cal.setVisible(true);
        Calendar miCalendario=cal.getReturnStatus();
        if(miCalendario!=null)
        {
            String dia=Integer.toString(miCalendario.get(Calendar.DATE));
            String mes = Integer.toString(miCalendario.get(Calendar.MONTH)+1);
            String anio = Integer.toString(miCalendario.get(Calendar.YEAR));
            t_fecha_pago1.setText(dia+"-"+mes+"-"+anio);
        }
        else
        t_fecha_pago1.setText("");
    }//GEN-LAST:event_b_calendario_pago1ActionPerformed

    private void b_guardar_pago2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_guardar_pago2ActionPerformed
        // TODO add your handling code here:
        if(t_fecha_pago1.getText().compareTo("")!=0)
        {
            if(((Number)t_porcentaje_pago1.getValue()).doubleValue()>0.0d)
            {
                Session session = HibernateUtil.getSessionFactory().openSession();
                TrabajoExtra te=(TrabajoExtra)session.get(TrabajoExtra.class, Integer.parseInt(t_trabajo.getText()));
                try
                {
                    session.beginTransaction().begin();
                    usr = (Usuario)session.get(Usuario.class, usr.getIdUsuario());
                    Hibernate.entidades.PagoAdicional nuevo= new Hibernate.entidades.PagoAdicional();
                    
                    String [] campos = t_fecha_pago1.getText().split("-");
                    Calendar calendario = Calendar.getInstance();
                    calendario.set(Calendar.MONTH, Integer.parseInt(campos[1])-1);
                    calendario.set(Calendar.YEAR, Integer.parseInt(campos[2]));
                    calendario.set(Calendar.DAY_OF_MONTH, Integer.parseInt(campos[0]));
                    nuevo.setFecha(calendario.getTime());
                    nuevo.setPorcentaje(((Number)t_porcentaje_pago1.getValue()).doubleValue());
                    nuevo.setImporte(((Number)t_importe_pago2.getValue()).doubleValue());
                    nuevo.setNota(t_notas_pago2.getText());
                    nuevo.setTrabajoExtra(te);
                    nuevo.setImporte(((Number)t_importe_pago2.getValue()).doubleValue());
                    nuevo.setTotal(((Number)t_importe_pago2.getValue()).doubleValue());
                    session.save(nuevo);
                    session.beginTransaction().commit();
                    t_fecha_pago1.setText("");
                    t_porcentaje_pago1.setValue(0.0d);
                    t_importe_pago2.setValue(0.0d);
                    t_notas_pago2.setText("");
                    d_nuevo2.setVisible(false);
                    
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                    System.out.println(e);
                    session.getTransaction().rollback();
                    JOptionPane.showMessageDialog(null, "¡No se pudo Agregar el destajo!");
                }
                if(session!=null)
                {
                    if(session.isOpen())
                    {
                        session.flush();
                        session.clear();
                        session.close();
                    }
                
                }
                busca_pago_adicional(String.valueOf(te.getIdAdicional()));
                buscaPagosAdicionales();
            }else
            {
                JOptionPane.showMessageDialog(null, "¡El porcentaje de valor debe ser mayor a 0.00!");
            }
        }else
        {
            JOptionPane.showMessageDialog(null, "¡Debe ingresar una fecha de avance!");
        }
    }//GEN-LAST:event_b_guardar_pago2ActionPerformed

    private void b_cancelar_pago2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_cancelar_pago2ActionPerformed
        // TODO add your handling code here:
        t_fecha_pago1.setText("");
        t_porcentaje_pago1.setValue(0.0d);
        t_importe_pago2.setValue(0.0d);
        t_notas_pago2.setText("");
        d_nuevo2.setVisible(false);
    }//GEN-LAST:event_b_cancelar_pago2ActionPerformed

    private void t_porcentaje_pago1FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_t_porcentaje_pago1FocusLost
        // TODO add your handling code here:
        DecimalFormat formatoPorcentaje = new DecimalFormat("#,##0.00");
        formatoPorcentaje.setMinimumFractionDigits(2);
       // t_importe_pagado1.setText("0");
       // t_importe_pagado1.setValue(0.0d);
        try
        {
            t_porcentaje_pago1.commitEdit();
        }catch(Exception e){}
        double nuevo_porcentaje=((Number)t_porcentaje_pago1.getValue()).doubleValue();
        double total_importe=((Number)t_importe1.getValue()).doubleValue();
        double suma_porcentaje=0.0d, suma_importe=0.0d;
        for(int x=0; x<t_datos1.getRowCount(); x++)
        {
            suma_porcentaje+=(double)t_datos1.getValueAt(x, 2);
            suma_importe+=(double)t_datos1.getValueAt(x, 3);
        }
        double total_porcentaje=suma_porcentaje+nuevo_porcentaje;
        
        if(total_porcentaje<=100.0d)
        {   
            double importe_porcentaje=(total_importe/100.0d)*total_porcentaje;
            t_importe_pago2.setValue(importe_porcentaje-suma_importe);
        }
        else
        {
            t_porcentaje_pago1.setValue(0.0d);
            t_importe_pago2.setValue(0.0d);
            JOptionPane.showMessageDialog(null, "¡el porcentaje acumulado supera el 100%!");
        }
      /*  Double pagado=((Number)t_importe_pago2.getValue()).doubleValue()-((Number)t_consumible1.getValue()).doubleValue();
        t_importe_pagado1.setText(""+formatoPorcentaje.format(pagado));
        t_importe_pagado1.setValue(pagado);*/
    }//GEN-LAST:event_t_porcentaje_pago1FocusLost

    private void t_porcentaje_pago1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_t_porcentaje_pago1ActionPerformed
        // TODO add your handling code here:
        try
        {
            t_porcentaje_pago1.commitEdit();
        }catch(Exception e){}
        t_notas_pago2.requestFocus();
    }//GEN-LAST:event_t_porcentaje_pago1ActionPerformed
    
    private void b_mas3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_mas3ActionPerformed
        // TODO add your handling code here:
        try{
                    
            Dimension d = Toolkit.getDefaultToolkit().getScreenSize(); 
            d_nuevo2.setSize(530, 295);
            d_nuevo2.setLocation((d.width/2)-265, (d.height/2)-140);
            d_nuevo2.setVisible(true);
        
        }catch(Exception e){
            System.out.println(e);
        }
    }//GEN-LAST:event_b_mas3ActionPerformed

    private void b_menos3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_menos3ActionPerformed
        // TODO add your handling code here:
        Session session = HibernateUtil.getSessionFactory().openSession();
        try
        {
            usr = (Usuario)session.get(Usuario.class, usr.getIdUsuario());
            if(usr.getRespaldar()==true)
            {
                if(t_datos1.getSelectedRow()>-1)
                {
                    int opt=JOptionPane.showConfirmDialog(this, "¡Confirma que deseas eliminar la partidas seleccionadas");
                    if(opt==0)
                    {
                        int [] renglones=t_datos1.getSelectedRows();
                        for(int x=0; x<renglones.length;x++)
                        {
                            session.beginTransaction().begin();
                            Hibernate.entidades.PagoAdicional elimina=(Hibernate.entidades.PagoAdicional) session.get(Hibernate.entidades.PagoAdicional.class, Integer.parseInt(t_datos1.getValueAt(renglones[x], 0).toString()));
                            if(elimina!=null)
                            {
                               session.delete(elimina);
                               session.getTransaction().commit();
                            }
                            else
                            {
                                session.getTransaction().rollback();
                                JOptionPane.showMessageDialog(null, "La partida ya fue eliminada");    
                            }
                        }
                        busca_pago_adicional(t_trabajo.getText());
                        buscaPagosAdicionales();
                    }
                }
                else
                {
                    javax.swing.JOptionPane.showMessageDialog(null, "Debes seleccionar una partida de la tabla para porde eliminarla");
                }
            }
            else
            {
                if(session.isOpen()==true)
                {
                    session.flush();
                    session.clear();
                    session.close();
                }
                JOptionPane.showMessageDialog(null, "¡Acceso denegado!");
            
            }
        }
        catch(Exception e)
        {
            System.out.println(e);
            JOptionPane.showMessageDialog(null, "¡No se pudo eliminar la partida seleccionada!");
        }
        if(session!=null)
            if(session.isOpen())
            {
                session.flush();
                session.clear();
                session.close();
            }
    }//GEN-LAST:event_b_menos3ActionPerformed

    private void b_pdf1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_pdf1ActionPerformed
        // TODO add your handling code here:
        Session session = HibernateUtil.getSessionFactory().openSession();
        try
        {
            session.beginTransaction().begin();
            BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.WINANSI, BaseFont.NOT_EMBEDDED);
            //Orden ord=buscaApertura();
            PDF reporte = new PDF();
            Date fecha = new Date();
            DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyyHH-mm-ss");//YYYY-MM-DD HH:MM:SS
            String valor=dateFormat.format(fecha);
            File folder = new File("reportes/"+ord);
            folder.mkdirs();
            reporte.Abrir(PageSize.LETTER.rotate(), "Valuación", "reportes/"+ord+"/"+valor+"-destajo.pdf");
            Font font = new Font(Font.FontFamily.HELVETICA, 6, Font.BOLD);
            BaseColor contenido=BaseColor.WHITE;
            int centro=Element.ALIGN_CENTER;
            int izquierda=Element.ALIGN_LEFT;
            int derecha=Element.ALIGN_RIGHT;
            float tam[]=new float[]{50,80,80,50,80,250};
            PdfPTable tabla=reporte.crearTabla(6, tam, 100, Element.ALIGN_LEFT);
            
            DecimalFormat formatoPorcentaje = new DecimalFormat("#,##0.00");
            formatoPorcentaje.setMinimumFractionDigits(2);   
            
            cabecera1(reporte, bf, tabla);
            
                session.beginTransaction().begin();
                //Orden dato = (Orden)session.get(Orden.class, Integer.parseInt(this.ord));
                //List cuentas=null;
                for(int x=0; x<t_datos1.getRowCount(); x++)
                {
                    tabla.addCell(reporte.celda(t_orden_trabajo.getText(), font, contenido, izquierda, 0,1,Rectangle.RECTANGLE));
                    
                    tabla.addCell(reporte.celda("ADICIONALES", font, contenido, izquierda, 0,1,Rectangle.RECTANGLE));
                    tabla.addCell(reporte.celda(t_datos1.getValueAt(x, 1).toString(), font, contenido, derecha, 0,1,Rectangle.RECTANGLE));
                    tabla.addCell(reporte.celda(t_datos1.getValueAt(x, 2).toString(), font, contenido, derecha, 0,1,Rectangle.RECTANGLE));
                    tabla.addCell(reporte.celda(t_datos1.getValueAt(x, 3).toString(), font, contenido, derecha, 0,1,Rectangle.RECTANGLE));
                    if(t_datos1.getValueAt(x, 4)!=null){
                        tabla.addCell(reporte.celda(t_datos1.getValueAt(x, 4).toString(), font, contenido, izquierda, 0,1,Rectangle.RECTANGLE));
                    }else{
                        tabla.addCell(reporte.celda(" ", font, contenido, izquierda, 0,1,Rectangle.RECTANGLE));
                    }
                }
                session.beginTransaction().rollback();
            
            tabla.setHeaderRows(1);
            reporte.agregaObjeto(tabla);
            reporte.cerrar();
            reporte.visualizar("reportes/"+ord+"/"+valor+"-destajo.pdf");
            
        }catch(Exception e)
        {
            System.out.println(e);
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "No se pudo realizar el reporte si el archivo esta abierto.");
        }
        if(session!=null)
            if(session.isOpen())
            {
                session.flush();
                session.clear();
                session.close();
            }
    }//GEN-LAST:event_b_pdf1ActionPerformed

    private void t_fechaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_t_fechaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_t_fechaActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        // TODO add your handling code here:
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        Material material = new Material(new JFrame(), true, this.ord, global, null, t_responsable.getText());
        material.setLocation((d.width/2)-(739/2), (d.height/2)-(345/2));
        material.setSize(739, 345);
        material.setVisible(true);
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        // TODO add your handling code here:
        if(t_orden_trabajo.getText().compareTo("")!=0){
            Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
            Material material = new Material(new JFrame(), true, t_orden_trabajo.getText(), "A", t_trabajo.getText(), t_responsable2.getText());
            material.setLocation((d.width/2)-(739/2), (d.height/2)-(345/2));
            material.setSize(739, 345);
            material.setVisible(true);
        }
    }//GEN-LAST:event_jButton8ActionPerformed
    
    public void cabecera2(PDF reporte, BaseFont bf, PdfPTable tabla)
    {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try
        {
            reporte.contenido.setLineWidth(0.5f);
            reporte.contenido.setColorStroke(new GrayColor(0.2f));
            reporte.contenido.setColorFill(new GrayColor(0.9f));
            reporte.contenido.roundRectangle(35, 515, 340, 45, 5);
            reporte.contenido.roundRectangle(380, 515, 375, 45, 5);
            reporte.contenido.roundRectangle(35, 480, 720, 30, 5);


            reporte.inicioTexto();
            reporte.contenido.setFontAndSize(bf, 14);
            reporte.contenido.setColorFill(BaseColor.BLACK);
            //Configuracion con= (Configuracion)session.get(Configuracion.class, 1);
            //reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, con.getEmpresa(), 160, 580, 0);
            reporte.contenido.setFontAndSize(bf, 8);
            reporte.contenido.setColorFill(BaseColor.BLACK);
            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "HOJA DE AVANCE", 35, 570, 0);
            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_RIGHT, "Fecha:"+new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date()), 760, 580, 0);

                    Orden dato = (Orden)session.get(Orden.class, Integer.parseInt(ord)); 
                    /*Foto foto = (Foto)session.createCriteria(Foto.class).add(Restrictions.eq("orden.idOrden", Integer.parseInt(ord))).addOrder(Order.desc("fecha")).setMaxResults(1).uniqueResult();
                    if(foto!=null)
                    {
                        reporte.agregaObjeto(reporte.crearImagen("ordenes/"+dato.getIdOrden()+"/"+foto.getDescripcion(), 0, -60, 120, 80, 0));
                    }*/
                    //************************datos de la orden****************************
                    reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Orden:"+dato.getIdOrden(), 45, 550, 0);

                    if(dato.getFecha()!=null)
                        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Apertura:"+dato.getFecha(), 265, 550, 0);
                    else
                        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Apertura:", 265, 550, 0);

                    reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Compañia:"+dato.getCompania().getIdCompania()+" "+dato.getCompania().getNombre(), 45, 540, 0);

                    if(dato.getSiniestro()!=null)
                        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Siniestro:"+dato.getSiniestro(), 45, 530, 0);
                    else
                        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Siniestro:", 45, 530, 0);

                    if(dato.getFechaSiniestro()!=null)
                        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "F. Siniestro:"+dato.getFechaSiniestro(), 265, 530, 0);
                    else
                        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "F.Siniestro:", 265, 530, 0);

                    if(dato.getPoliza()!=null)
                        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Poliza:"+dato.getPoliza(), 45, 520, 0);
                    else
                        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Poliza:", 45, 520, 0);

                    if(dato.getInciso()!=null)
                        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Inciso:"+dato.getInciso(), 265, 520, 0);
                    else
                        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Inciso:", 265, 520, 0);

                    //************datos de la unidad
                    reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Unidad:"+dato.getTipo().getTipoNombre(), 385, 550, 0);
                    reporte.contenido.showTextAligned(PdfContentByte.ALIGN_RIGHT, "Modelo:"+dato.getModelo(), 664, 550, 0);

                    reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Marca:"+dato.getMarca().getMarcaNombre(), 385, 540, 0);
                    if(dato.getNoEconomico()!=null)
                        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_RIGHT, "Economico:"+dato.getNoEconomico(), 673, 540, 0);
                    else
                        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_RIGHT, "Economico:", 673, 540, 0);

                    if(dato.getNoMotor()!=null)
                        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "N° Motor:"+dato.getNoMotor(), 385, 530, 0);
                    else
                        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "N° Motor:", 385, 530, 0);

                    if(dato.getNoSerie()!=null)
                        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "N° Serie:"+dato.getNoSerie(), 385, 520, 0);
                    else
                        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "N° Serie:", 385, 520, 0);
                    //*************************************************************

                    switch(this.global)
                    {
                        case "h":
                            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Especialidad: Hojalateria", 45, 495, 0);
                            break;
                        case "m":
                            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Especialidad: Mecanica", 45, 495, 0);
                            break;
                        case "s":
                            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Especialidad: Suspension", 45, 495, 0);
                            break;
                        case "e":
                            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Especialidad: electrico", 45, 495, 0);
                            break;
                        case "p":
                            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Especialidad: Pintura", 45, 495, 0);
                            break;
                    }
                    reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Empleado:"+t_responsable.getText(), 165, 495, 0);
                    reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Fecha Asignación:"+t_asignacion.getText(), 430, 495, 0);
                    reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Fecha Limite:"+t_limite.getText(), 600, 495, 0);
                    
                    reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Monto x Hora: $"+t_monto.getText(), 45, 485, 0);
                    reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Horas Totales: "+t_horas.getText(), 165, 485, 0);
                    reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Importe a Pagar: $"+t_importe.getText(), 600, 485, 0);
                    

                reporte.finTexto();
                //agregamos renglones vacios para dejar un espacio
                reporte.agregaObjeto(new Paragraph(" "));
                reporte.agregaObjeto(new Paragraph(" "));
                reporte.agregaObjeto(new Paragraph(" "));
                reporte.agregaObjeto(new Paragraph(" "));
                reporte.agregaObjeto(new Paragraph(" "));
                reporte.agregaObjeto(new Paragraph(" "));

                Font font = new Font(Font.FontFamily.HELVETICA, 8, Font.BOLD);

                BaseColor cabecera=BaseColor.GRAY;
                //BaseColor contenido=BaseColor.WHITE;
                int centro=Element.ALIGN_CENTER;
                //int izquierda=Element.ALIGN_LEFT;
                //int derecha=Element.ALIGN_RIGHT;

                tabla.addCell(reporte.celda("Fecha Avance", font, cabecera, centro, 0, 1, Rectangle.RECTANGLE));
                tabla.addCell(reporte.celda("% de Avance", font, cabecera, centro, 0, 1,Rectangle.RECTANGLE));
                tabla.addCell(reporte.celda("Importe Pagado a Fecha Avance", font, cabecera, centro, 2,1, Rectangle.RECTANGLE));
                tabla.addCell(reporte.celda("Notas", font, cabecera, centro, 0,1, Rectangle.RECTANGLE));
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
    
    public void busca_pago_adicional(String id){
       
        if(emp!=null)
        {
                DecimalFormat formatoPorcentaje = new DecimalFormat("#,##0.00");
                formatoPorcentaje.setMinimumFractionDigits(2);   
                t_datos1.setModel(ModeloTablaReporte3(0, columnas3));
                formatoTabla2();
                Session session = HibernateUtil.getSessionFactory().openSession();
                Configuracion con= (Configuracion)session.get(Configuracion.class, 1);
                double iva = con.getIva();
                double total = 0.0d;
                TrabajoExtra extra=(TrabajoExtra)session.get(TrabajoExtra.class, Integer.parseInt(id));
               try{
 
                if(extra.getOrden()!=null){
                    //material que se le ha entregado + suma debe ser menor
                    Query qry = session.createSQLQuery("select (select if(sum(cantidad*valor) is null, 0, sum(cantidad*valor)) \n" +
                    "from movimiento left join almacen on movimiento.id_almacen=almacen.id_almacen \n" +
                    "left join orden on almacen.id_orden=orden.id_orden where orden.id_orden="+extra.getOrden().getIdOrden()+" \n" +
                    "and almacen.operacion=8 and almacen.tipo_movimiento=2 and almacen.especialidad='A' \n" +
                    "and almacen.id_trabajo="+extra.getIdAdicional()+" and almacen.id_pago_adicional is null) \n" +
                    "-\n" +
                    "(select if(sum(cantidad*valor) is null, 0, sum(cantidad*valor)) \n" +
                    "from movimiento left join almacen on movimiento.id_almacen=almacen.id_almacen \n" +
                    "left join orden on almacen.id_orden=orden.id_orden where orden.id_orden="+extra.getOrden().getIdOrden()+" \n" +
                    "and almacen.operacion=8 and almacen.tipo_movimiento=1 and almacen.especialidad='A' \n" +
                    "and almacen.id_trabajo="+extra.getIdAdicional()+" and almacen.id_pago_adicional is null)as monto_consumible");

                    qry.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
                    ArrayList consumido = (ArrayList)qry.list();
                    for(int i =0; i<consumido.size(); i++){
                        java.util.HashMap map=(java.util.HashMap)consumido.get(i);
                        total = Double.parseDouble(map.get("monto_consumible").toString());
                    }
                    Double iva_m = total*(iva /100);
                    iva_m = total+iva_m;
                    t_trabajo1.setText(iva_m.toString());
                    t_orden_trabajo.setText(""+extra.getOrden().getIdOrden());
                }
                t_responsable2.setText(emp.getNombre());
                t_importe1.setText(""+((Number)extra.getImporte()).doubleValue());
                anterior1=((Number)extra.getImporte()).doubleValue();
                t_importe1.setValue(((Number)extra.getImporte()).doubleValue());
                t_asignacion1.setText(extra.getFechaDestajo().toString());
                t_trabajo.setText(""+extra.getIdAdicional());

                Query query = session.createSQLQuery("select id_pago_adicional, fecha, porcentaje, importe, total, nota from pago_adicional where id_trabajo_extra="+Integer.parseInt(id)+" order by id_pago_adicional desc");
                query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
                ArrayList pagos = (ArrayList)query.list();
                if(pagos.size()>0){
                    for(int i =0; i<pagos.size(); i++){
                        java.util.HashMap map=(java.util.HashMap)pagos.get(i);
                        Object[] obj = new Object[5];
                        obj[0] = map.get("id_pago_adicional");
                        obj[1] = map.get("fecha");
                        obj[2] = map.get("porcentaje");
                        obj[3] = map.get("importe");
                        obj[4] = map.get("nota");

                        model3.addRow(obj);
                    }
                }
            }catch(Exception e)
            {
                System.out.println(e);
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error en ventana Trabajo Extra");
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
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton b_adicionales;
    private javax.swing.JButton b_calendario_pago;
    private javax.swing.JButton b_calendario_pago1;
    private javax.swing.JButton b_cancelar_pago;
    private javax.swing.JButton b_cancelar_pago1;
    private javax.swing.JButton b_cancelar_pago2;
    private javax.swing.JRadioButton b_electrico;
    private javax.swing.JButton b_fecha_siniestro;
    private javax.swing.JButton b_general;
    private javax.swing.JButton b_guardar_pago;
    private javax.swing.JButton b_guardar_pago1;
    private javax.swing.JButton b_guardar_pago2;
    private javax.swing.JRadioButton b_hojalateria;
    private javax.swing.JButton b_mas;
    private javax.swing.JButton b_mas1;
    private javax.swing.JButton b_mas3;
    private javax.swing.JRadioButton b_mecanica;
    private javax.swing.JButton b_menos;
    private javax.swing.JButton b_menos1;
    private javax.swing.JButton b_menos3;
    private javax.swing.JButton b_pdf1;
    private javax.swing.JRadioButton b_pintura;
    private javax.swing.JRadioButton b_suspencion;
    private javax.swing.JCheckBox cb;
    private javax.swing.JDialog d_nuevo;
    private javax.swing.JDialog d_nuevo1;
    private javax.swing.JDialog d_nuevo2;
    private javax.swing.JDialog d_pagos;
    private javax.swing.ButtonGroup grupo;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JTable jTable1;
    private javax.swing.JLabel l_importe_pago;
    private javax.swing.JLabel l_importe_pago1;
    private javax.swing.JLabel l_importe_pago2;
    private javax.swing.JLabel l_nota_pago;
    private javax.swing.JLabel l_nota_pago1;
    private javax.swing.JLabel l_porcentaje_pago;
    private javax.swing.JLabel l_porcentaje_pago1;
    private javax.swing.JDialog pago_adicional;
    private javax.swing.JTable t_adicionales;
    private javax.swing.JTextField t_asignacion;
    private javax.swing.JTextField t_asignacion1;
    private javax.swing.JTextField t_consumibles;
    private javax.swing.JTable t_datos;
    private javax.swing.JTable t_datos1;
    private javax.swing.JTextField t_fecha;
    private javax.swing.JTextField t_fecha_pago;
    private javax.swing.JTextField t_fecha_pago1;
    private javax.swing.JFormattedTextField t_horas;
    private javax.swing.JFormattedTextField t_importe;
    private javax.swing.JFormattedTextField t_importe1;
    private javax.swing.JFormattedTextField t_importe_pago;
    private javax.swing.JFormattedTextField t_importe_pago1;
    private javax.swing.JFormattedTextField t_importe_pago2;
    private javax.swing.JTextField t_limite;
    private javax.swing.JFormattedTextField t_monto;
    private javax.swing.JFormattedTextField t_monto1;
    private javax.swing.JTextField t_notas_pago;
    private javax.swing.JTextField t_notas_pago1;
    private javax.swing.JTextField t_notas_pago2;
    private javax.swing.JTextField t_orden;
    private javax.swing.JTextField t_orden_trabajo;
    private javax.swing.JFormattedTextField t_porcentaje_pago;
    private javax.swing.JFormattedTextField t_porcentaje_pago1;
    private javax.swing.JTextField t_responsable;
    private javax.swing.JTextField t_responsable1;
    private javax.swing.JTextField t_responsable2;
    private javax.swing.JTextField t_trabajo;
    private javax.swing.JTextField t_trabajo1;
    // End of variables declaration//GEN-END:variables


    public void cabecera(PDF reporte, BaseFont bf, PdfPTable tabla)
    {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try
        {
            reporte.contenido.setLineWidth(0.5f);
            reporte.contenido.setColorStroke(new GrayColor(0.2f));
            reporte.contenido.setColorFill(new GrayColor(0.9f));
            reporte.contenido.roundRectangle(160, 515, 210, 45, 5);
            reporte.contenido.roundRectangle(380, 515, 375, 45, 5);
            reporte.contenido.roundRectangle(35, 480, 720, 30, 5);


            reporte.inicioTexto();
            reporte.contenido.setFontAndSize(bf, 14);
            reporte.contenido.setColorFill(BaseColor.BLACK);
            //Configuracion con= (Configuracion)session.get(Configuracion.class, 1);
            //reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, con.getEmpresa(), 160, 580, 0);
            reporte.contenido.setFontAndSize(bf, 8);
            reporte.contenido.setColorFill(BaseColor.BLACK);
            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Hoja de Avance", 160, 570, 0);
            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_RIGHT, "Fecha:"+new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date()), 760, 580, 0);

                    Orden dato = (Orden)session.get(Orden.class, Integer.parseInt(ord)); 
                    Foto foto = (Foto)session.createCriteria(Foto.class).add(Restrictions.eq("orden.idOrden", Integer.parseInt(ord))).addOrder(Order.desc("fecha")).setMaxResults(1).uniqueResult();
                    if(foto!=null)
                    {
                        reporte.agregaObjeto(reporte.crearImagen("ordenes/"+dato.getIdOrden()+"/"+foto.getDescripcion(), 0, -60, 120, 80, 0));
                    }
                    else{}
                    //************************datos de la orden****************************
                    reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Orden:"+dato.getIdOrden(), 164, 550, 0);

                    if(dato.getFecha()!=null)
                        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Apertura:"+dato.getFecha(), 285, 550, 0);
                    else
                        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Apertura:", 285, 550, 0);

                    reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Compañia:"+dato.getCompania().getIdCompania()+" "+dato.getCompania().getNombre(), 164, 540, 0);

                    if(dato.getSiniestro()!=null)
                        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Siniestro:"+dato.getSiniestro(), 164, 530, 0);
                    else
                        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Siniestro:", 164, 530, 0);

                    if(dato.getFechaSiniestro()!=null)
                        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "F. Siniestro:"+dato.getFechaSiniestro(), 285, 530, 0);
                    else
                        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "F.Siniestro:", 285, 530, 0);

                    if(dato.getPoliza()!=null)
                        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Poliza:"+dato.getPoliza(), 164, 520, 0);
                    else
                        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Poliza:", 164, 520, 0);

                    if(dato.getInciso()!=null)
                        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Inciso:"+dato.getInciso(), 285, 520, 0);
                    else
                        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Inciso:", 285, 520, 0);

                    //************datos de la unidad
                    reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Unidad:"+dato.getTipo().getTipoNombre(), 385, 550, 0);
                    reporte.contenido.showTextAligned(PdfContentByte.ALIGN_RIGHT, "Modelo:"+dato.getModelo(), 664, 550, 0);

                    reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Marca:"+dato.getMarca().getMarcaNombre(), 385, 540, 0);
                    if(dato.getNoEconomico()!=null)
                        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_RIGHT, "Economico:"+dato.getNoEconomico(), 664, 540, 0);
                    else
                        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_RIGHT, "Economico:", 664, 540, 0);

                    if(dato.getNoMotor()!=null)
                        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "N° Motor:"+dato.getNoMotor(), 385, 530, 0);
                    else
                        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "N° Motor:", 385, 530, 0);

                    if(dato.getNoSerie()!=null)
                        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "N° Serie:"+dato.getNoSerie(), 385, 520, 0);
                    else
                        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "N° Serie:", 385, 520, 0);
                    //*************************************************************

                    switch(this.global)
                    {
                        case "h":
                            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Especialidad: Hojalateria", 40, 495, 0);
                            break;
                        case "m":
                            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Especialidad: Mecanica", 40, 495, 0);
                            break;
                        case "s":
                            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Especialidad: Suspension", 40, 495, 0);
                            break;
                        case "e":
                            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Especialidad: electrico", 40, 495, 0);
                            break;
                        case "p":
                            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Especialidad: Pintura", 40, 495, 0);
                            break;
                    }
                    reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Empleado:"+t_responsable.getText(), 165, 495, 0);
                    reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Fecha Asignación:"+t_asignacion.getText(), 430, 495, 0);
                    reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Fecha Limite:"+t_limite.getText(), 600, 495, 0);
                    
                    reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Monto x Hora: $"+t_monto.getText(), 40, 485, 0);
                    reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Horas Totales: "+t_horas.getText(), 165, 485, 0);
                    reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Importe a Pagar: $"+t_importe.getText(), 600, 485, 0);
                    

                reporte.finTexto();
                //agregamos renglones vacios para dejar un espacio
                reporte.agregaObjeto(new Paragraph(" "));
                reporte.agregaObjeto(new Paragraph(" "));
                reporte.agregaObjeto(new Paragraph(" "));
                reporte.agregaObjeto(new Paragraph(" "));
                reporte.agregaObjeto(new Paragraph(" "));
                reporte.agregaObjeto(new Paragraph(" "));

                Font font = new Font(Font.FontFamily.HELVETICA, 8, Font.BOLD);

                BaseColor cabecera=BaseColor.GRAY;
                //BaseColor contenido=BaseColor.WHITE;
                int centro=Element.ALIGN_CENTER;
                //int izquierda=Element.ALIGN_LEFT;
                //int derecha=Element.ALIGN_RIGHT;

                tabla.addCell(reporte.celda("Fecha Avance", font, cabecera, centro, 0, 1, Rectangle.RECTANGLE));
                tabla.addCell(reporte.celda("% de Avance", font, cabecera, centro, 0, 1,Rectangle.RECTANGLE));
                tabla.addCell(reporte.celda("Importe Pagado", font, cabecera, centro, 0,1, Rectangle.RECTANGLE));
                tabla.addCell(reporte.celda("Notas", font, cabecera, centro, 0,1, Rectangle.RECTANGLE));
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
    
    public void cabecera1(PDF reporte, BaseFont bf, PdfPTable tabla)
    {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try
        {
            reporte.contenido.setLineWidth(0.5f);
            reporte.contenido.setColorStroke(new GrayColor(0.2f));
            reporte.contenido.setColorFill(new GrayColor(0.9f));
            reporte.contenido.roundRectangle(35, 545, 720, 17, 5);


            reporte.inicioTexto();
            reporte.contenido.setFontAndSize(bf, 14);
            reporte.contenido.setColorFill(BaseColor.BLACK);
            //Configuracion con= (Configuracion)session.get(Configuracion.class, 1);
            //reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, con.getEmpresa(), 160, 580, 0);
            reporte.contenido.setFontAndSize(bf, 8);
            reporte.contenido.setColorFill(BaseColor.BLACK);
            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_RIGHT, "Fecha:"+new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date()), 760, 580, 0);
            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Hoja de Avances General", 40, 550, 0);
            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Empleado:"+t_responsable.getText(), 280, 550, 0);
            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Monto x Hora: $"+t_monto.getText(), 600, 550, 0);
                    
            //Orden dato = (Orden)session.get(Orden.class, Integer.parseInt(ord)); 
                    
                reporte.finTexto();
                //agregamos renglones vacios para dejar un espacio
                reporte.agregaObjeto(new Paragraph(" "));
                reporte.agregaObjeto(new Paragraph(" "));
                reporte.agregaObjeto(new Paragraph(" "));

                Font font = new Font(Font.FontFamily.HELVETICA, 6, Font.BOLD);

                BaseColor cabecera=BaseColor.GRAY;
               // BaseColor contenido=BaseColor.WHITE;
                int centro=Element.ALIGN_CENTER;
               // int izquierda=Element.ALIGN_LEFT;
               // int derecha=Element.ALIGN_RIGHT;

                tabla.addCell(reporte.celda("Orden", font, cabecera, centro, 0, 1, Rectangle.RECTANGLE));
                tabla.addCell(reporte.celda("Especialidad", font, cabecera, centro, 0, 1, Rectangle.RECTANGLE));
                tabla.addCell(reporte.celda("Fecha Avance", font, cabecera, centro, 0, 1, Rectangle.RECTANGLE));
                tabla.addCell(reporte.celda("% de Avance", font, cabecera, centro, 0, 1,Rectangle.RECTANGLE));
                tabla.addCell(reporte.celda("Importe Pagado", font, cabecera, centro, 0,1, Rectangle.RECTANGLE));
                tabla.addCell(reporte.celda("Notas", font, cabecera, centro, 0,1, Rectangle.RECTANGLE));
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
    
    public void tabla_tamaños()
    {
        TableColumnModel col_model = t_datos.getColumnModel();

        for (int i=0; i<t_datos.getColumnCount(); i++)
        {
  	      TableColumn column = col_model.getColumn(i);
              switch(i)
              {
                  case 0:
                      column.setPreferredWidth(50);
                      break;
                  case 1:
                      column.setPreferredWidth(70);
                      break;
                  case 2:
                      column.setPreferredWidth(70);
                      break;    
                  case 3:
                      column.setPreferredWidth(100);
                      break;
                  case 4:
                      column.setPreferredWidth(100);
                      break;
                  case 5:
                      column.setPreferredWidth(100);
                      break;
                  
                  case 6:
                      column.setPreferredWidth(350);
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
    }
    
    public void formatoTabla1()
    {
        Color c1 = new java.awt.Color(2, 135, 242);
        for(int x=0; x<t_adicionales.getColumnModel().getColumnCount(); x++)
            t_adicionales.getColumnModel().getColumn(x).setHeaderRenderer(new Render1(c1));
        tabla_tamaños_adicionales();
        t_adicionales.setShowVerticalLines(true);
        t_adicionales.setShowHorizontalLines(true);
    }
    public void tabla_tamaños_adicionales()
    {
        TableColumnModel col_model = t_adicionales.getColumnModel();

        for (int i=0; i<t_adicionales.getColumnCount(); i++)
        {
  	      TableColumn column = col_model.getColumn(i);
              switch(i)
              {
                  case 0:
                      column.setPreferredWidth(50);
                      break;
                  case 1:
                      column.setPreferredWidth(70);
                      break;
                  case 2:
                      column.setPreferredWidth(115);
                      break;    
                  case 3:
                      column.setPreferredWidth(100);
                      break;
                  case 4:
                      column.setPreferredWidth(80);
                      break;
                  case 5:
                      column.setPreferredWidth(270);
                      break;
                  
                  case 6:
                      column.setPreferredWidth(50);
                      break;
              }
        }
        JTableHeader header = t_adicionales.getTableHeader();
        header.setForeground(Color.white);
    }
    
    public void buscaDestajos()
    {
        Session session = HibernateUtil.getSessionFactory().openSession();
        
        //total de consumibles.
        DecimalFormat formatoPorcentaje = new DecimalFormat("#,##0.00");
        formatoPorcentaje.setMinimumFractionDigits(2);
        Configuracion con= (Configuracion)session.get(Configuracion.class, 1);
        double iva = con.getIva();
        double total = 0.0d;
        
        //destajos
        try
        {
            Query query = session.createSQLQuery("select (select if(sum(cantidad*valor) is null, 0, sum(cantidad*valor)) from movimiento left join almacen on movimiento.id_almacen=almacen.id_almacen left join orden on almacen.id_orden=orden.id_orden where orden.id_orden="+ord+" and almacen.operacion=8 and almacen.tipo_movimiento=2 and almacen.especialidad='"+global+"' and almacen.id_destajo is null and almacen.id_trabajo is null) - (select if(sum(cantidad*valor) is null, 0, sum(cantidad*valor)) from movimiento left join almacen on movimiento.id_almacen=almacen.id_almacen left join orden on almacen.id_orden=orden.id_orden where orden.id_orden="+ord+" and almacen.operacion=8 and almacen.tipo_movimiento=1 and almacen.especialidad='"+global+"' and almacen.id_destajo is null and almacen.id_trabajo is null)as monto_consumible");
            query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
            ArrayList consumido = (ArrayList)query.list();
            for(int i =0; i<consumido.size(); i++){
                java.util.HashMap map=(java.util.HashMap)consumido.get(i);
                total = Double.parseDouble(map.get("monto_consumible").toString());
            }
            Double iva_m = total*(iva /100);
            iva_m = total+iva_m;
            t_consumibles.setText(""+formatoPorcentaje.format(iva_m));
            
            String consulta="select id_destajo, fecha_destajo, avance, importe, total,notas from destajo where id_orden="+ord+" and especialidad='"+this.global+"' order by id_destajo asc;";
            Query q = session.createSQLQuery(consulta);
            q.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
            List resultList = q.list();
            model.setRowCount(0);
            for (Object o : resultList) 
            {
                java.util.HashMap respuesta=(java.util.HashMap)o;
                Object[] renglon=new Object[7];
                //Orden actor = (Orden) o;
                renglon[0]=respuesta.get("id_destajo");
                renglon[1]=respuesta.get("fecha_destajo");
                renglon[2]=respuesta.get("avance");
                renglon[3]=respuesta.get("importe");
                renglon[4]=respuesta.get("notas");
                model.addRow(renglon);
            }
            resultList=null;
        }catch(Exception e)
        {
            System.out.println(e);
        }
        if(session!=null)
            if(session.isOpen())
                session.close();
    }
    
    public void buscaPagosAdicionales()
    {
        if(emp!=null)
        {
            Session session = HibernateUtil.getSessionFactory().openSession();
            String consulta="select id_adicional, id_orden, fecha_destajo, importe, notas, autorizado,(select sum(porcentaje) from pago_adicional where id_trabajo_extra=id_adicional group by id_trabajo_extra) as avance from trabajo_extra where id_empleado="+emp.getIdEmpleado();
            consulta+=" order by id_adicional desc ";
            if(cb.isSelected()==false)
                consulta+=" limit 20;";
            try
            {
                session.beginTransaction().begin();
                Query q = session.createSQLQuery(consulta);
                q.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
                List resultList = q.list();
                model1.setRowCount(0);
                for (Object o : resultList) 
                {
                    java.util.HashMap respuesta=(java.util.HashMap)o;
                    Object[] renglon=new Object[7];
                    renglon[0]=respuesta.get("id_adicional");
                    renglon[1]=respuesta.get("id_orden");
                    renglon[2]=respuesta.get("fecha_destajo");
                    renglon[3]=respuesta.get("importe");
                    renglon[4]=respuesta.get("avance");
                    renglon[5]=respuesta.get("notas");
                    renglon[6]=respuesta.get("autorizado");
                    model1.addRow(renglon);
                }
                resultList=null;
            }catch(Exception e)
            {
                e.printStackTrace();
            }
            if(session!=null)
                if(session.isOpen())
                    session.close();
        }
        else
            model1.setRowCount(0);
    }
    
    public void formatoTabla2()
    {
        Color c1 = new java.awt.Color(2, 135, 242);
        for(int x=0; x<t_datos1.getColumnModel().getColumnCount(); x++)
            t_datos1.getColumnModel().getColumn(x).setHeaderRenderer(new Render1(c1));
        tabla_tamaños_pagos();
        t_datos1.setShowVerticalLines(true);
        t_datos1.setShowHorizontalLines(true);
    }
    public void tabla_tamaños_pagos()
    {
        TableColumnModel col_model = t_datos1.getColumnModel();

        for (int i=0; i<t_datos1.getColumnCount(); i++)
        {
  	      TableColumn column = col_model.getColumn(i);
              switch(i)
              {
                  case 0:
                      column.setPreferredWidth(40);
                      break;
                  case 1:
                      column.setPreferredWidth(95);
                      break;
                  case 2:
                      column.setPreferredWidth(90);
                      break;    
                  case 3:
                      column.setPreferredWidth(110);
                      break;
                  case 4:
                      column.setPreferredWidth(120);
                      break;
                  case 5:
                      column.setPreferredWidth(110);
                      break;
                  
                  case 6:
                      column.setPreferredWidth(250);
                      break;
              }
        }
        JTableHeader header = t_datos1.getTableHeader();
        header.setForeground(Color.white);
    }
   
}
