/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Valuacion;

import Hibernate.Util.HibernateUtil;
import Hibernate.entidades.Configuracion;
import Hibernate.entidades.Foto;
import Hibernate.entidades.Orden;
import Hibernate.entidades.Partida;
import Hibernate.entidades.Usuario;
import Integral.DefaultTableHeaderCellRenderer;
import Integral.ExtensionFileFilter;
import Integral.FormatoEditor;
import Integral.FormatoTabla;
import Integral.Ftp;
import Integral.PDF;
import Integral.VerticalTableHeaderCellRenderer;
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
import java.awt.Desktop;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.Vector;
import javax.swing.DefaultCellEditor;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author I.S.C.Salvador
 */
public class Estadistico extends javax.swing.JPanel {

    int x=0;
    private String orden;
    Orden ord;
    int configuracion=1;
    private Usuario user;
    String sessionPrograma="";
    String estado, rutaFtp;
    FormatoTabla formato;
    /**
     * Creates new form Estadistico
     */
    public Estadistico(String ord, Usuario us, String edo, String ses, int configuracion, String rutaFtp) {
        initComponents();
        estado=edo;
        this.rutaFtp=rutaFtp;
        this.configuracion=configuracion;
        sessionPrograma=ses;
        orden=ord;
        user=us;
        formato = new FormatoTabla();
        
        //model=new MyModel(0, columnas, this.types);
        t_datos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "NO", "#", "Descripción", "# Pedido", "Unidad", "$ Cot.Com.", "$ Cot.Val.", "# Aut", "$ Autorizado", "$ compra", "$ Ajustado", "% Ajuste", "Tipo", "Aut"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Integer.class, java.lang.String.class, java.lang.Double.class, java.lang.String.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, true, false, false, false, false, false, false, false
            };

            @Override
            public void setValueAt(Object value, int row, int col)
                    {
                            Vector vector = (Vector) this.dataVector.elementAt(row);
                            Object celda = ((Vector)this.dataVector.elementAt(row)).elementAt(col);
                            switch(col)
                            {
                                case 6://DM
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
                                                user = (Usuario)session.get(Usuario.class, user.getIdUsuario());
                                                if(user.getEditaHoras()==true)
                                                {
                                                    /*if(r_cerrar_valuacion.isSelected()==false)
                                                    {*/
                                                        if(value.toString().compareTo("")!=0 && Double.parseDouble(value.toString())>=0) 
                                                        {       
                                                             Partida part=(Partida) session.createCriteria(Partida.class).add(Restrictions.eq("ordenByIdOrden.idOrden", Integer.parseInt(orden))).add(Restrictions.eq("idEvaluacion", Integer.parseInt(t_datos.getValueAt(t_datos.getSelectedRow(), 0).toString()))).add(Restrictions.eq("subPartida", Integer.parseInt(t_datos.getValueAt(t_datos.getSelectedRow(), 1).toString()))).setMaxResults(1).uniqueResult();
                                                             if(part!=null)
                                                             {
                                                                 part.setCuValuacion(BigDecimal.valueOf(Double.parseDouble(value.toString())).setScale(2, RoundingMode.HALF_UP).doubleValue());
                                                                 session.update(part);
                                                                 session.getTransaction().commit();
                                                                 vector.setElementAt(value, col);
                                                                 
                                                                 double ajuste=Double.parseDouble(value.toString())-Double.parseDouble(vector.elementAt(8).toString());
                                                                 double porcentaje=0;
                                                                 
                                                                 if(Double.parseDouble(value.toString())>0)
                                                                     porcentaje = ajuste/Double.parseDouble(value.toString());
                                                                 porcentaje=porcentaje*100;
                        
                                                                 vector.setElementAt(ajuste, 10);
                                                                 vector.setElementAt(porcentaje, 11);
                                                                 this.dataVector.setElementAt(vector, row);
                                                                 fireTableCellUpdated(row, col);
                                                                 fireTableCellUpdated(row, 10);
                                                                 fireTableCellUpdated(row, 11);
                                                                 if(session.isOpen()==true)
                                                                 {
                                                                     session.flush();
                                                                     session.clear();
                                                                     session.close();
                                                                 }
                                                                 //buscaCuentas(row, col);
                                                             }
                                                             else
                                                             {
                                                                 JOptionPane.showMessageDialog(null, "La partida ya no existe");
                                                                 buscaCuentas(-1,-1);
                                                             }
                                                        }
                                                        else
                                                        {
                                                            if(Double.parseDouble(value.toString()) < 0) 
                                                                JOptionPane.showMessageDialog(null, "El campo no permite numeros negativos"); 
                                                            buscaCuentas(-1,-1);
                                                        }
                                                    /*}
                                                    else
                                                    {
                                                        JOptionPane.showMessageDialog(null, "La valuación esta cerrada"); 
                                                    }*/
                                                }
                                                else
                                                {
                                                    JOptionPane.showMessageDialog(null, "Acceso denegado"); 
                                                }
                                            }
                                            catch(Exception e)
                                            {
                                                session.getTransaction().rollback();
                                                e.printStackTrace();
                                                JOptionPane.showMessageDialog(null, "Error al actualizar los datos"); 
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

                                default:
                                        vector.setElementAt(value, col);
                                        this.dataVector.setElementAt(vector, row);
                                        fireTableCellUpdated(row, col);
                                        break;
                            }
                            t_datos.requestFocus();
                    }
            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        formatoTabla();
        buscaCuentas(-1,-1);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        scroll = new javax.swing.JScrollPane();
        t_datos = new javax.swing.JTable();
        p_izquierdo = new javax.swing.JPanel();
        l4 = new javax.swing.JLabel();
        t_mo_autorizada = new javax.swing.JFormattedTextField();
        t_mo_proyectada = new javax.swing.JFormattedTextField();
        l6 = new javax.swing.JLabel();
        l8 = new javax.swing.JLabel();
        t_mo_indirecta = new javax.swing.JFormattedTextField();
        l9 = new javax.swing.JLabel();
        t_cv_mo = new javax.swing.JFormattedTextField();
        l10 = new javax.swing.JLabel();
        t_pv_mo = new javax.swing.JFormattedTextField();
        l11 = new javax.swing.JLabel();
        t_consumible_proyectado = new javax.swing.JFormattedTextField();
        l12 = new javax.swing.JLabel();
        t_pv_consumible = new javax.swing.JFormattedTextField();
        l13 = new javax.swing.JLabel();
        t_total_venta = new javax.swing.JFormattedTextField();
        l14 = new javax.swing.JLabel();
        t_porcentaje_ajuste_mo = new javax.swing.JFormattedTextField();
        l15 = new javax.swing.JLabel();
        t_gastos = new javax.swing.JFormattedTextField();
        l16 = new javax.swing.JLabel();
        t_utilidad = new javax.swing.JFormattedTextField();
        t_porcentaje_utilidad = new javax.swing.JFormattedTextField();
        l17 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        t_busca = new javax.swing.JTextField();
        b_consumibles1 = new javax.swing.JButton();
        b_pdf = new javax.swing.JButton();
        b_exel = new javax.swing.JButton();

        setLayout(new java.awt.BorderLayout());

        scroll.setPreferredSize(new java.awt.Dimension(453, 150));

        t_datos.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        t_datos.setForeground(new java.awt.Color(102, 102, 102));
        t_datos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "NO", "#", "Descripción", "# Pedido", "Unidad", "$ Cot.Com.", "$ Cot.Val.", "# Aut", "$ Autorizado", "$ compra", "$ Ajustado", "% Ajuste", "Tipo", "Aut"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Integer.class, java.lang.String.class, java.lang.Double.class, java.lang.String.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, true, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        t_datos.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_LAST_COLUMN);
        t_datos.setGridColor(new java.awt.Color(102, 102, 102));
        t_datos.getTableHeader().setReorderingAllowed(false);
        scroll.setViewportView(t_datos);

        add(scroll, java.awt.BorderLayout.CENTER);

        p_izquierdo.setBackground(new java.awt.Color(2, 135, 242));

        l4.setFont(new java.awt.Font("Arial", 0, 9)); // NOI18N
        l4.setForeground(new java.awt.Color(255, 255, 255));
        l4.setText("MO Autorizada:");

        t_mo_autorizada.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        t_mo_autorizada.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.00"))));
        t_mo_autorizada.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        t_mo_autorizada.setText("0.00");
        t_mo_autorizada.setDisabledTextColor(new java.awt.Color(2, 38, 253));
        t_mo_autorizada.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        t_mo_autorizada.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                t_mo_autorizadaFocusLost(evt);
            }
        });
        t_mo_autorizada.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                t_mo_autorizadaActionPerformed(evt);
            }
        });

        t_mo_proyectada.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        t_mo_proyectada.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.00"))));
        t_mo_proyectada.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        t_mo_proyectada.setText("0.00");
        t_mo_proyectada.setDisabledTextColor(new java.awt.Color(2, 38, 253));
        t_mo_proyectada.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        t_mo_proyectada.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                t_mo_proyectadaFocusLost(evt);
            }
        });
        t_mo_proyectada.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                t_mo_proyectadaActionPerformed(evt);
            }
        });

        l6.setFont(new java.awt.Font("Arial", 0, 9)); // NOI18N
        l6.setForeground(new java.awt.Color(255, 255, 255));
        l6.setText("MO Proyectada:");

        l8.setFont(new java.awt.Font("Arial", 0, 9)); // NOI18N
        l8.setForeground(new java.awt.Color(255, 255, 255));
        l8.setText("MO indirecta 20%:");

        t_mo_indirecta.setEditable(false);
        t_mo_indirecta.setBackground(new java.awt.Color(2, 135, 242));
        t_mo_indirecta.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(0, 0, 0)));
        t_mo_indirecta.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.00"))));
        t_mo_indirecta.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        t_mo_indirecta.setText("0.00");
        t_mo_indirecta.setDisabledTextColor(new java.awt.Color(2, 38, 253));
        t_mo_indirecta.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N

        l9.setFont(new java.awt.Font("Arial", 0, 9)); // NOI18N
        l9.setForeground(new java.awt.Color(255, 255, 255));
        l9.setText("Costo Venta MO:");

        t_cv_mo.setEditable(false);
        t_cv_mo.setBackground(new java.awt.Color(2, 135, 242));
        t_cv_mo.setBorder(null);
        t_cv_mo.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.00"))));
        t_cv_mo.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        t_cv_mo.setText("0.00");
        t_cv_mo.setDisabledTextColor(new java.awt.Color(2, 38, 253));
        t_cv_mo.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N

        l10.setFont(new java.awt.Font("Arial", 0, 9)); // NOI18N
        l10.setForeground(new java.awt.Color(255, 255, 255));
        l10.setText("Precio Venta MO(50%):");

        t_pv_mo.setEditable(false);
        t_pv_mo.setBackground(new java.awt.Color(2, 135, 242));
        t_pv_mo.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        t_pv_mo.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.00"))));
        t_pv_mo.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        t_pv_mo.setText("0.00");
        t_pv_mo.setDisabledTextColor(new java.awt.Color(2, 38, 253));
        t_pv_mo.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N

        l11.setFont(new java.awt.Font("Arial", 0, 9)); // NOI18N
        l11.setForeground(new java.awt.Color(255, 255, 255));
        l11.setText("Consumible Proyectado");

        t_consumible_proyectado.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        t_consumible_proyectado.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.00"))));
        t_consumible_proyectado.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        t_consumible_proyectado.setText("0.00");
        t_consumible_proyectado.setDisabledTextColor(new java.awt.Color(2, 38, 253));
        t_consumible_proyectado.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        t_consumible_proyectado.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                t_consumible_proyectadoFocusLost(evt);
            }
        });
        t_consumible_proyectado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                t_consumible_proyectadoActionPerformed(evt);
            }
        });

        l12.setFont(new java.awt.Font("Arial", 0, 9)); // NOI18N
        l12.setForeground(new java.awt.Color(255, 255, 255));
        l12.setText("P. Venta Consumible (30%)");

        t_pv_consumible.setEditable(false);
        t_pv_consumible.setBackground(new java.awt.Color(2, 135, 242));
        t_pv_consumible.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        t_pv_consumible.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.00"))));
        t_pv_consumible.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        t_pv_consumible.setText("0.00");
        t_pv_consumible.setDisabledTextColor(new java.awt.Color(2, 38, 253));
        t_pv_consumible.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N

        l13.setFont(new java.awt.Font("Arial", 0, 9)); // NOI18N
        l13.setForeground(new java.awt.Color(255, 255, 255));
        l13.setText("Total Venta (MO+Consumible):");

        t_total_venta.setEditable(false);
        t_total_venta.setBackground(new java.awt.Color(2, 135, 242));
        t_total_venta.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 0, 0, new java.awt.Color(0, 0, 0)));
        t_total_venta.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.00"))));
        t_total_venta.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        t_total_venta.setText("0.00");
        t_total_venta.setDisabledTextColor(new java.awt.Color(2, 38, 253));
        t_total_venta.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N

        l14.setFont(new java.awt.Font("Arial", 0, 9)); // NOI18N
        l14.setForeground(new java.awt.Color(255, 255, 255));
        l14.setText("% ajuste MO:");

        t_porcentaje_ajuste_mo.setEditable(false);
        t_porcentaje_ajuste_mo.setBackground(new java.awt.Color(2, 135, 242));
        t_porcentaje_ajuste_mo.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        t_porcentaje_ajuste_mo.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.00"))));
        t_porcentaje_ajuste_mo.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        t_porcentaje_ajuste_mo.setText("0.00");
        t_porcentaje_ajuste_mo.setDisabledTextColor(new java.awt.Color(2, 38, 253));
        t_porcentaje_ajuste_mo.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N

        l15.setFont(new java.awt.Font("Arial", 0, 9)); // NOI18N
        l15.setForeground(new java.awt.Color(255, 255, 255));
        l15.setText("Gasto MO y Consumible:");

        t_gastos.setEditable(false);
        t_gastos.setBackground(new java.awt.Color(2, 135, 242));
        t_gastos.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(0, 0, 0)));
        t_gastos.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.00"))));
        t_gastos.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        t_gastos.setText("0.00");
        t_gastos.setDisabledTextColor(new java.awt.Color(2, 38, 253));
        t_gastos.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N

        l16.setFont(new java.awt.Font("Arial", 0, 9)); // NOI18N
        l16.setForeground(new java.awt.Color(255, 255, 255));
        l16.setText("Utilidad");

        t_utilidad.setEditable(false);
        t_utilidad.setBackground(new java.awt.Color(2, 135, 242));
        t_utilidad.setBorder(null);
        t_utilidad.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.00"))));
        t_utilidad.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        t_utilidad.setText("0.00");
        t_utilidad.setDisabledTextColor(new java.awt.Color(2, 38, 253));
        t_utilidad.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N

        t_porcentaje_utilidad.setEditable(false);
        t_porcentaje_utilidad.setBackground(new java.awt.Color(2, 135, 242));
        t_porcentaje_utilidad.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        t_porcentaje_utilidad.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.00"))));
        t_porcentaje_utilidad.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        t_porcentaje_utilidad.setText("0.00");
        t_porcentaje_utilidad.setDisabledTextColor(new java.awt.Color(2, 38, 253));
        t_porcentaje_utilidad.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N

        l17.setFont(new java.awt.Font("Arial", 0, 9)); // NOI18N
        l17.setForeground(new java.awt.Color(255, 255, 255));
        l17.setText("% Utilidad:");

        javax.swing.GroupLayout p_izquierdoLayout = new javax.swing.GroupLayout(p_izquierdo);
        p_izquierdo.setLayout(p_izquierdoLayout);
        p_izquierdoLayout.setHorizontalGroup(
            p_izquierdoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(p_izquierdoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(p_izquierdoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(p_izquierdoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(p_izquierdoLayout.createSequentialGroup()
                            .addComponent(l12)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(t_pv_consumible, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(p_izquierdoLayout.createSequentialGroup()
                            .addComponent(l13)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(t_total_venta, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(p_izquierdoLayout.createSequentialGroup()
                            .addGroup(p_izquierdoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(l8)
                                .addComponent(l6))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(p_izquierdoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(t_mo_indirecta, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(t_mo_proyectada, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(p_izquierdoLayout.createSequentialGroup()
                            .addGroup(p_izquierdoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(l9)
                                .addComponent(l10)
                                .addComponent(l11))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(p_izquierdoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(t_cv_mo, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(t_pv_mo, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(t_consumible_proyectado, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(p_izquierdoLayout.createSequentialGroup()
                            .addComponent(l16)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(t_utilidad, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(p_izquierdoLayout.createSequentialGroup()
                            .addGroup(p_izquierdoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(l15)
                                .addComponent(l4))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(p_izquierdoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(t_mo_autorizada)
                                .addComponent(t_gastos, javax.swing.GroupLayout.DEFAULT_SIZE, 88, Short.MAX_VALUE))))
                    .addGroup(p_izquierdoLayout.createSequentialGroup()
                        .addComponent(l14)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(t_porcentaje_ajuste_mo, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(l17)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(t_porcentaje_utilidad, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        p_izquierdoLayout.setVerticalGroup(
            p_izquierdoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(p_izquierdoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(p_izquierdoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(l6)
                    .addComponent(t_mo_proyectada, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(p_izquierdoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(t_mo_indirecta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(l8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(p_izquierdoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(t_cv_mo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(l9))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(p_izquierdoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(t_pv_mo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(l10))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(p_izquierdoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(t_consumible_proyectado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(l11))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(p_izquierdoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(t_pv_consumible, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(l12))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(p_izquierdoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(t_total_venta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(l13))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(p_izquierdoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(t_mo_autorizada, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(l4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(p_izquierdoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(t_gastos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(l15))
                .addGap(8, 8, 8)
                .addGroup(p_izquierdoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(t_utilidad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(l16))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(p_izquierdoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(p_izquierdoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(t_porcentaje_utilidad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(l17))
                    .addGroup(p_izquierdoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(t_porcentaje_ajuste_mo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(l14)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        add(p_izquierdo, java.awt.BorderLayout.LINE_START);

        jPanel2.setBackground(new java.awt.Color(2, 135, 242));

        jLabel1.setFont(new java.awt.Font("Arial", 0, 9)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Buscar:");

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

        b_consumibles1.setBackground(new java.awt.Color(2, 135, 242));
        b_consumibles1.setIcon(new ImageIcon("imagenes/menu.png"));
        b_consumibles1.setToolTipText("Calendario");
        b_consumibles1.setMaximumSize(new java.awt.Dimension(32, 8));
        b_consumibles1.setMinimumSize(new java.awt.Dimension(32, 8));
        b_consumibles1.setPreferredSize(new java.awt.Dimension(32, 8));
        b_consumibles1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_consumibles1ActionPerformed(evt);
            }
        });

        b_pdf.setIcon(new ImageIcon("imagenes/pdf_icon.png"));
        b_pdf.setToolTipText("Exporta a PDF");
        b_pdf.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_pdfActionPerformed(evt);
            }
        });

        b_exel.setIcon(new ImageIcon("imagenes/xls_icon.png"));
        b_exel.setToolTipText("Exporta a EXCEL");
        b_exel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_exelActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(b_consumibles1, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel1)
                .addGap(8, 8, 8)
                .addComponent(t_busca, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(b_pdf, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(7, 7, 7)
                .addComponent(b_exel, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(596, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(b_pdf, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(b_exel, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(2, 2, 2)
                                .addComponent(jLabel1))
                            .addComponent(t_busca, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(18, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(b_consumibles1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(10, 10, 10))))
        );

        add(jPanel2, java.awt.BorderLayout.PAGE_END);
    }// </editor-fold>//GEN-END:initComponents

    private void t_buscaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_t_buscaActionPerformed
        // TODO add your handling code here:
        if(this.t_busca.getText().compareToIgnoreCase("")!=0)
        {
            if(x>=t_datos.getRowCount())
            {
                x=0;
                java.awt.Rectangle r = t_datos.getCellRect( x, 2, true);
                t_datos.scrollRectToVisible(r);
            }
            for(; x<t_datos.getRowCount(); x++)
            {
                if(t_datos.getValueAt(x, 2).toString().indexOf(t_busca.getText()) != -1)
                {
                    t_datos.setRowSelectionInterval(x, x);
                    t_datos.setColumnSelectionInterval(2, 2);
                    java.awt.Rectangle r = t_datos.getCellRect( x, 2, true);
                    t_datos.scrollRectToVisible(r);
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

    private void b_consumibles1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_consumibles1ActionPerformed
        // TODO add your handling code here:
        if(p_izquierdo.isVisible()==true)
        {
            p_izquierdo.setVisible(false);
            b_consumibles1.setIcon(new ImageIcon("imagenes/menu.png"));
        }
        else
        {
            p_izquierdo.setVisible(true);
            b_consumibles1.setIcon(new ImageIcon("imagenes/menu1.png"));
        }
    }//GEN-LAST:event_b_consumibles1ActionPerformed

    private void b_pdfActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_pdfActionPerformed
        // TODO add your handling code here:
        Session session = HibernateUtil.getSessionFactory().openSession();
        try
        {
            session.beginTransaction().begin();
            DecimalFormat formatoPorcentaje = new DecimalFormat("#,##0.00");
            DecimalFormat formatoDecimal = new DecimalFormat("####0.0");
            formatoPorcentaje.setMinimumFractionDigits(2);
            BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.WINANSI, BaseFont.NOT_EMBEDDED);
            //Orden ord=buscaApertura();
            PDF reporte = new PDF();
            Date fecha = new Date();
            DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyyHH-mm-ss");//YYYY-MM-DD HH:MM:SS
            String valor=dateFormat.format(fecha);
            File folder = new File("reportes/"+ord.getIdOrden());
            folder.mkdirs();
            reporte.Abrir2(PageSize.LETTER.rotate(), "Estadistico", "reportes/"+ord.getIdOrden()+"/"+valor+"-estadistico.pdf");

            Font font = new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL);
            BaseColor contenido=BaseColor.WHITE;
            int centro=Element.ALIGN_CENTER;
            int izquierda=Element.ALIGN_LEFT;
            int derecha=Element.ALIGN_RIGHT;
            float tam[]=new float[]{5,5,65,5,5,8,10,10,10,10,13,6};
            PdfPTable tabla=reporte.crearTabla(12, tam, 100, Element.ALIGN_LEFT);
            
            cabecera2(reporte, bf, tabla);
            BigDecimal pres_total = new BigDecimal(0.0d);
            BigDecimal aut_total = new BigDecimal(0.0d);
            BigDecimal ajuste_total = new BigDecimal(0.0d);
            
            for(int ren=0;ren<t_datos.getRowCount();ren++)
            {
                tabla.addCell(reporte.celda(""+t_datos.getValueAt(ren, 0).toString(), font, contenido, derecha, 0,1,Rectangle.RECTANGLE));//no
                tabla.addCell(reporte.celda(""+t_datos.getValueAt(ren, 1).toString(), font, contenido, derecha, 0,1,Rectangle.RECTANGLE));//#
                tabla.addCell(reporte.celda(""+t_datos.getValueAt(ren, 2).toString(), font, contenido, izquierda, 0,1,Rectangle.RECTANGLE));//descripcion
                tabla.addCell(reporte.celda(""+t_datos.getValueAt(ren, 3).toString(), font, contenido, derecha, 0,1,Rectangle.RECTANGLE));//#Pedido
                tabla.addCell(reporte.celda(""+t_datos.getValueAt(ren, 4).toString(), font, contenido, derecha, 0,1,Rectangle.RECTANGLE));//unidadad

                BigDecimal pres = new BigDecimal((Double)t_datos.getValueAt(ren, 5)).setScale(2, RoundingMode.HALF_UP);
                tabla.addCell(reporte.celda(""+pres.doubleValue(), font, contenido, derecha, 0,1,Rectangle.RECTANGLE));//$ Cot com
                
                BigDecimal val = new BigDecimal((Double)t_datos.getValueAt(ren, 6)).setScale(2, RoundingMode.HALF_UP);
                tabla.addCell(reporte.celda(""+val.doubleValue(), font, contenido, derecha, 0,1,Rectangle.RECTANGLE));//$ cot val
                
                tabla.addCell(reporte.celda(""+t_datos.getValueAt(ren, 7).toString(), font, contenido, derecha, 0,1,Rectangle.RECTANGLE));//#Aut
                
                BigDecimal aut = new BigDecimal((Double)t_datos.getValueAt(ren, 8)).setScale(2, RoundingMode.HALF_UP);
                tabla.addCell(reporte.celda(""+aut.doubleValue(), font, contenido, derecha, 0,1,Rectangle.RECTANGLE));//$ Aut
                
                BigDecimal compra = new BigDecimal((Double)t_datos.getValueAt(ren, 9)).setScale(2, RoundingMode.HALF_UP);
                tabla.addCell(reporte.celda(""+compra.doubleValue(), font, contenido, derecha, 0,1,Rectangle.RECTANGLE));//$ compra
                
                BigDecimal monto_ajuste = new BigDecimal((Double)t_datos.getValueAt(ren, 10)).setScale(2, RoundingMode.HALF_UP);
                tabla.addCell(reporte.celda(""+monto_ajuste.doubleValue(), font, contenido, derecha, 0,1,Rectangle.RECTANGLE));//$ajuste
                
                BigDecimal ajuste = new BigDecimal((Double)t_datos.getValueAt(ren, 11)).setScale(2, RoundingMode.HALF_UP);
                tabla.addCell(reporte.celda(""+ajuste.doubleValue(), font, contenido, derecha, 0,1,Rectangle.RECTANGLE));// %ajuste
                
                tabla.addCell(reporte.celda(""+t_datos.getValueAt(ren, 12).toString(), font, contenido, izquierda, 0,1,Rectangle.RECTANGLE));//tipo
                tabla.addCell(reporte.celda(""+t_datos.getValueAt(ren, 13).toString(), font, contenido, izquierda, 0,1,Rectangle.RECTANGLE));//aut
                
                pres_total=pres_total.add(val);
                aut_total=aut_total.add(aut);
                ajuste_total=ajuste_total.add(monto_ajuste);
            }

            BigDecimal ajuste_porcentaje = ajuste_total.divide(pres_total, 3, RoundingMode.UP);
            
            tabla.addCell(reporte.celda("Totales", font, contenido, derecha, 3,1,Rectangle.RECTANGLE));
            tabla.addCell(reporte.celda("", font, contenido, derecha, 0,1,Rectangle.RECTANGLE));//#Pedido
            tabla.addCell(reporte.celda("", font, contenido, derecha, 0,1,Rectangle.RECTANGLE));//unidad
            tabla.addCell(reporte.celda("", font, contenido, derecha, 0,1,Rectangle.RECTANGLE));//$Cot.Com
            tabla.addCell(reporte.celda(""+pres_total.doubleValue(), font, contenido, derecha, 0,1,Rectangle.RECTANGLE));//$Cot.Val
            tabla.addCell(reporte.celda("", font, contenido, derecha, 0,1,Rectangle.RECTANGLE));//# Aut
            tabla.addCell(reporte.celda(""+aut_total.doubleValue(), font, contenido, derecha, 0,1,Rectangle.RECTANGLE));//$ Aut
            tabla.addCell(reporte.celda(""+ajuste_total.doubleValue(), font, contenido, derecha, 0,1,Rectangle.RECTANGLE));//$ajuste
            tabla.addCell(reporte.celda(""+ajuste_porcentaje.doubleValue()+"%", font, contenido, derecha, 0,1,Rectangle.RECTANGLE));// %ajuste
            tabla.addCell(reporte.celda("", font, contenido, izquierda, 0,1,Rectangle.RECTANGLE));//tipo
            tabla.addCell(reporte.celda("", font, contenido, izquierda, 0,1,Rectangle.RECTANGLE));//aut
            
            tabla.setHeaderRows(1);
            reporte.agregaObjeto(tabla);
            //Paragraph parrafo=new Paragraph("P(Prioridad de la Partida)= 1(Alta), 2(Media), 3(Baja)", font);
            //parrafo.setAlignment(centro);
            //reporte.agregaObjeto(parrafo);
            reporte.cerrar();
            reporte.visualizar2("reportes/"+ord.getIdOrden()+"/"+valor+"-estadistico.pdf");
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
    }//GEN-LAST:event_b_pdfActionPerformed

    private void b_exelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_exelActionPerformed
        // TODO add your handling code here:
        javax.swing.JFileChooser jF1= new javax.swing.JFileChooser();
        jF1.setFileFilter(new ExtensionFileFilter("Excel document (*.xls)", new String[] { "xls" }));
        String ruta = null;
        if(jF1.showSaveDialog(null)==jF1.APPROVE_OPTION)
        {
            ruta = jF1.getSelectedFile().getAbsolutePath();
            File archivoXLS = new File(ruta+".xls");
            try
            {
                if(archivoXLS.exists())
                archivoXLS.delete();
                archivoXLS.createNewFile();
                Workbook libro = new HSSFWorkbook();
                FileOutputStream archivo = new FileOutputStream(archivoXLS);
                Sheet hoja = libro.createSheet("VALUACION");
                //hoja.protectSheet("04650077");
                for(int ren=0;ren<(t_datos.getRowCount()+1);ren++)
                {
                    Row fila = hoja.createRow(ren);
                    for(int col=0; col<t_datos.getColumnCount(); col++)
                    {
                        Cell celda = fila.createCell(col);
                        if(ren==0)
                        {
                            celda.setCellValue(t_datos.getColumnName(col));
                        }
                        else
                        {
                            try
                            {
                                if(t_datos.getValueAt(ren-1, col).toString().compareToIgnoreCase("true")==0)
                                celda.setCellValue("✓");
                                else
                                {
                                    if(t_datos.getValueAt(ren-1, col).toString().compareToIgnoreCase("false")==0)
                                        celda.setCellValue("");
                                    else
                                        celda.setCellValue(t_datos.getValueAt(ren-1, col).toString());
                                }
                            }catch(Exception e)
                            {
                                celda.setCellValue("");
                            }
                        }
                    }
                }
                libro.write(archivo);
                archivo.close();
                Desktop.getDesktop().open(archivoXLS);
            }catch(Exception e)
            {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "No se pudo realizar el reporte si el archivo esta abierto");
            }
        }
    }//GEN-LAST:event_b_exelActionPerformed

    private void t_mo_autorizadaFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_t_mo_autorizadaFocusLost
        // TODO add your handling code here:
        if(t_mo_autorizada.getText().compareTo("")==0)
        {
            t_mo_autorizada.setText("0");
            t_mo_autorizada.setValue(0.00);
        }
        try
        {
            t_mo_autorizada.commitEdit();
        }catch(Exception e){e.printStackTrace();}
        
        Session session = HibernateUtil.getSessionFactory().openSession();
        try{
            session.getTransaction().begin();
            user = (Usuario)session.get(Usuario.class, user.getIdUsuario());
            ord=(Orden)session.load(Orden.class, ord.getIdOrden());
            ord.setMoDirecta(((Number)t_mo_autorizada.getValue()).doubleValue());
            
            session.update(ord);
            session.getTransaction().commit();
            totales();
        }
        catch(Exception e){
            session.getTransaction().rollback();
            JOptionPane.showMessageDialog(null, "¡Error al actualizar los datos!");
            e.printStackTrace();
        }
        if(session!=null)
        if(session.isOpen()==true)
        {
            session.flush();
            session.clear();
            session.close();
        }
    }//GEN-LAST:event_t_mo_autorizadaFocusLost

    private void t_mo_autorizadaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_t_mo_autorizadaActionPerformed
        // TODO add your handling code here:
        t_busca.requestFocus();
    }//GEN-LAST:event_t_mo_autorizadaActionPerformed

    private void t_mo_proyectadaFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_t_mo_proyectadaFocusLost
        // TODO add your handling code here:
        if(t_mo_proyectada.getText().compareTo("")==0)
        {
            t_mo_proyectada.setText("0");
            t_mo_proyectada.setValue(0.00);
        }
        try{
            t_mo_proyectada.commitEdit();
        }catch(Exception e){e.printStackTrace();}
        
        Session session = HibernateUtil.getSessionFactory().openSession();
        try{
            session.getTransaction().begin();
            user = (Usuario)session.get(Usuario.class, user.getIdUsuario());
            ord=(Orden)session.load(Orden.class, ord.getIdOrden());
            ord.setMoPresupuestada(((Number)t_mo_proyectada.getValue()).doubleValue());
            
            session.update(ord);
            session.getTransaction().commit();
            totales();
        }
        catch(Exception e){
            session.getTransaction().rollback();
            JOptionPane.showMessageDialog(null, "¡Error al actualizar los datos!");
            e.printStackTrace();
        }
        if(session!=null)
        if(session.isOpen()==true)
        {
            session.flush();
            session.clear();
            session.close();
        }
    }//GEN-LAST:event_t_mo_proyectadaFocusLost

    private void t_mo_proyectadaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_t_mo_proyectadaActionPerformed
        // TODO add your handling code here:
        t_busca.requestFocus();
    }//GEN-LAST:event_t_mo_proyectadaActionPerformed

    private void t_consumible_proyectadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_t_consumible_proyectadoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_t_consumible_proyectadoActionPerformed

    private void t_consumible_proyectadoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_t_consumible_proyectadoFocusLost
        // TODO add your handling code here:
        if(t_consumible_proyectado.getText().compareTo("")==0)
        {
            t_consumible_proyectado.setText("0");
            t_consumible_proyectado.setValue(0.00);
        }
        try{
            t_consumible_proyectado.commitEdit();
        }catch(Exception e){e.printStackTrace();}
        
        Session session = HibernateUtil.getSessionFactory().openSession();
        try{
            session.getTransaction().begin();
            user = (Usuario)session.get(Usuario.class, user.getIdUsuario());
            ord=(Orden)session.load(Orden.class, ord.getIdOrden());
            ord.setValesConsumibles(((Number)t_consumible_proyectado.getValue()).doubleValue());
            
            session.update(ord);
            session.getTransaction().commit();
            totales();
        }
        catch(Exception e){
            session.getTransaction().rollback();
            JOptionPane.showMessageDialog(null, "¡Error al actualizar los datos!");
            e.printStackTrace();
        }
        if(session!=null)
        if(session.isOpen()==true)
        {
            session.flush();
            session.clear();
            session.close();
        }
    }//GEN-LAST:event_t_consumible_proyectadoFocusLost

    private void buscaCuentas(int x, int y)
    {
        double imp=0.0;
        double suma=0.00;
        if(orden!=null)
        {
            Session session = HibernateUtil.getSessionFactory().openSession();
            try
            {
                session.beginTransaction().begin();
                ord = (Orden)session.get(Orden.class, Integer.parseInt(orden));
                
                //t_mo_proyectada.setText(""+ord.getMoPresupuestada());
                try{
                t_mo_proyectada.setValue(ord.getMoPresupuestada());
                }catch(Exception e){
                    t_mo_proyectada.setValue(0);
                }
                
                //t_consumible_proyectado.setText(""setMoDirecta);
                try{
                t_mo_autorizada.setValue(ord.getMoDirecta());
                }catch(Exception e){
                    t_mo_autorizada.setValue(0);
                }
                
                try{
                    t_consumible_proyectado.setValue(ord.getValesConsumibles());
                }catch(Exception e){
                    t_consumible_proyectado.setValue(0);
                }
                
                
                Query query;
                ArrayList lista=new ArrayList();
                query=session.createSQLQuery("select id_evaluacion, sub_partida, catalogo.nombre, cant, med, " +
                "Precio_cia_seguros_c_u, Cantidad_aut, Precio_aut_c_u, ((c_u/(1-(porcentaje*0.01)))*cant) as total_presupuesto, (cu_valuacion*cant) as valuacion_totizado, (Cantidad_aut*Precio_aut_c_u) as total_autorizado, \n" +
                "autorizado, ref_comp, surte_almacen, tot, so, pd, id_pedido, if(Int_camb=-1, 'false', 'true') as i_cam, " +
                "tipo, if(enlazada is null, '', enlazada) as ext, if(facturado is null, 'false', if(facturado=true, 'true', 'false'))as fac, (cant_pcp*pcp) as total_comprado \n" +
                "from partida inner join catalogo on partida.id_catalogo=catalogo.id_catalogo \n" +
                "inner join especialidad on catalogo.id_grupo_mecanico=especialidad.id_grupo_mecanico where id_orden="+ord.getIdOrden()+" and autorizado_valuacion=true and cam>-1 order by id_evaluacion, sub_partida asc;");
                query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
                lista=(ArrayList)query.list();
                
                Partida[] enlazadas=new Partida[0];
                enlazadas = (Partida[])session.createCriteria(Partida.class).add(Restrictions.eq("ordenByEnlazada.idOrden", ord.getIdOrden())).add(Restrictions.eq("autorizadoValuacion", true)).addOrder(Order.asc("idEvaluacion")).addOrder(Order.asc("subPartida")).list().toArray(new Partida[0]);
                if(lista.size()>0 || enlazadas.length>0)
                {
                    DefaultTableModel modelo = (DefaultTableModel)t_datos.getModel();
                    modelo.setRowCount(0);
                    for(int a=0; a<lista.size(); a++)
                    {
                        java.util.HashMap map=(java.util.HashMap)lista.get(a);
                        double total_presupuesto=Math.round((double)map.get("total_presupuesto"));
                        double total_presupuesto_valuacion=Math.round((double)map.get("valuacion_totizado"));
                        double total_autorizado=Math.round((double)map.get("total_autorizado"));
                        double total_comprado=Math.round((double)map.get("total_comprado"));
                        double ajuste=total_presupuesto_valuacion-total_autorizado;
                        double porcentaje=0;
                        String autorizado="NO";
                        if(total_autorizado>0)
                            autorizado="SI";
                        //boolean sa=(boolean)map.get("surte_almacen");
                        if(total_presupuesto_valuacion>0)
                            porcentaje = ajuste/total_presupuesto_valuacion;
                        porcentaje=porcentaje*100;
                        String tipo="EXTRA";
                        if( ((boolean)map.get("ref_comp")==true || (boolean)map.get("surte_almacen")==true) && (boolean)map.get("autorizado")==true)
                            tipo="PEDIDO";
                        modelo.addRow(new Object[]{
                            map.get("id_evaluacion"), 
                            map.get("sub_partida"), 
                            map.get("nombre"), 
                            map.get("cant"), 
                            map.get("med"), 
                            total_presupuesto,
                            total_presupuesto_valuacion,
                            map.get("Cantidad_aut"), 
                            total_autorizado,
                            total_comprado,
                            ajuste,
                            porcentaje,
                            tipo,
                            autorizado});
                    }
                    
                }
                
            }catch(Exception e){
                e.printStackTrace();
            }
            totales();
        }
    }
    
    public void formatoTabla()
    {
        TableCellRenderer textNormal = new DefaultTableHeaderCellRenderer();        
        TableCellRenderer headerRenderer = new VerticalTableHeaderCellRenderer();
        Enumeration columns = t_datos.getColumnModel().getColumns();
        
        for(int x=0; x<t_datos.getColumnModel().getColumnCount(); x++)
        {
            /*if((x>3 && x<7) || (x>20 && x<25) || (x>28 && x<38))
                t_datos.getColumnModel().getColumn(x).setHeaderRenderer(headerRenderer);
            else*/
                t_datos.getColumnModel().getColumn(x).setHeaderRenderer(textNormal);
        } 
        tabla_tamaños();
        t_datos.setShowVerticalLines(true);
        t_datos.setShowHorizontalLines(true);
        
        t_datos.setDefaultRenderer(String.class, formato); 
        t_datos.setDefaultRenderer(Double.class, formato); 
        t_datos.setDefaultRenderer(Integer.class, formato);
        t_datos.setDefaultRenderer(Boolean.class, formato);
        
    }

    
    public void tabla_tamaños()
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
                  case 0://N°
                      column.setPreferredWidth(45);
                      //column.setCellRenderer(tcr);
                      break;
                  case 1://#
                      column.setPreferredWidth(30);
                      //column.setCellRenderer(tcr);
                      break;
                  case 2://descripcion
                      column.setPreferredWidth(310);
                      break;
                  case 3://Cant pedida
                      column.setPreferredWidth(70);
                      break;
                  case 4://Uni
                      column.setPreferredWidth(60);
                      break;
                  case 5://Presupuesto compras
                      column.setPreferredWidth(90);
                      break;
                  case 6://Presupuesto Valuacion
                      column.setPreferredWidth(90);
                      break;
                  case 7://cant aut
                      column.setPreferredWidth(70);
                      break;
                  case 8://Autorizado
                      column.setPreferredWidth(90);
                      break;
                  case 9://Autorizado
                      column.setPreferredWidth(90);
                      break;
                  case 10://Ajustado
                      column.setPreferredWidth(90);
                      break;
                  case 11://% ajustado
                      column.setPreferredWidth(60);
                      break;
                  case 12://tipo Partida
                      column.setPreferredWidth(70);
                      break;
                  case 13://Autorizada
                      column.setPreferredWidth(50);
                      break;
                      
                  default:
                      column.setPreferredWidth(17);
                      break;
              }
        }
        JTableHeader header = t_datos.getTableHeader();
        header.setBackground(new java.awt.Color(2, 135, 242));//102,102,102
        header.setForeground(Color.white);
   }
    
    void totales()
    {
        try{
        DecimalFormat formatoPorcentaje = new DecimalFormat("#,##0.00");
        formatoPorcentaje.setMinimumFractionDigits(2);
        BigDecimal mo_proyectada = new BigDecimal(((Number)t_mo_proyectada.getValue()).doubleValue());
        BigDecimal mo_autorizada = new BigDecimal(((Number)t_mo_autorizada.getValue()).doubleValue());
        BigDecimal consumible_proyectado = new BigDecimal(((Number)t_consumible_proyectado.getValue()).doubleValue());
        BigDecimal mo_indirecta = mo_proyectada.multiply(new BigDecimal(0.2d));
        BigDecimal cv_mo = mo_proyectada.add(mo_indirecta);
        BigDecimal pv_mo = cv_mo.divide(new BigDecimal(0.5), 2, RoundingMode.HALF_UP);
        BigDecimal pv_consumible = consumible_proyectado.divide(new BigDecimal(0.7), 2, RoundingMode.HALF_UP);

        BigDecimal total_venta = pv_consumible.add(pv_mo);
        BigDecimal ajuste_mo = total_venta.subtract(mo_autorizada);
        try{
        ajuste_mo.divide(total_venta, 2, RoundingMode.HALF_UP);
        }catch(Exception e){};
        
        BigDecimal gastos = cv_mo.add(consumible_proyectado);
        BigDecimal utilidad = mo_autorizada.subtract(gastos);
        BigDecimal porcentaje_utilidad = new BigDecimal(0);
        try{
         porcentaje_utilidad = utilidad.divide(mo_autorizada, 2, RoundingMode.HALF_UP).multiply(new BigDecimal(100));
        }catch(Exception e){};
        
        t_mo_indirecta.setText(formatoPorcentaje.format(mo_indirecta.doubleValue()));
        t_cv_mo.setText(formatoPorcentaje.format(cv_mo.doubleValue()));
        t_pv_mo.setText(formatoPorcentaje.format(pv_mo.doubleValue()));
        //consumible_proyectado
        t_pv_consumible.setText(formatoPorcentaje.format(pv_consumible.doubleValue()));
        t_total_venta.setText(formatoPorcentaje.format(total_venta.doubleValue()));
        t_porcentaje_ajuste_mo.setText(formatoPorcentaje.format(ajuste_mo.doubleValue()));
        
        t_gastos.setText(formatoPorcentaje.format(gastos.doubleValue()));
        t_utilidad.setText(formatoPorcentaje.format(utilidad.doubleValue()));
        t_porcentaje_utilidad.setText(formatoPorcentaje.format(porcentaje_utilidad.doubleValue()));
        }catch(Exception e){e.printStackTrace();};
    }
            
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton b_consumibles1;
    private javax.swing.JButton b_exel;
    private javax.swing.JButton b_pdf;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JLabel l10;
    private javax.swing.JLabel l11;
    private javax.swing.JLabel l12;
    private javax.swing.JLabel l13;
    private javax.swing.JLabel l14;
    private javax.swing.JLabel l15;
    private javax.swing.JLabel l16;
    private javax.swing.JLabel l17;
    private javax.swing.JLabel l4;
    private javax.swing.JLabel l6;
    private javax.swing.JLabel l8;
    private javax.swing.JLabel l9;
    private javax.swing.JPanel p_izquierdo;
    private javax.swing.JScrollPane scroll;
    private javax.swing.JTextField t_busca;
    private javax.swing.JFormattedTextField t_consumible_proyectado;
    private javax.swing.JFormattedTextField t_cv_mo;
    private javax.swing.JTable t_datos;
    private javax.swing.JFormattedTextField t_gastos;
    private javax.swing.JFormattedTextField t_mo_autorizada;
    private javax.swing.JFormattedTextField t_mo_indirecta;
    private javax.swing.JFormattedTextField t_mo_proyectada;
    private javax.swing.JFormattedTextField t_porcentaje_ajuste_mo;
    private javax.swing.JFormattedTextField t_porcentaje_utilidad;
    private javax.swing.JFormattedTextField t_pv_consumible;
    private javax.swing.JFormattedTextField t_pv_mo;
    private javax.swing.JFormattedTextField t_total_venta;
    private javax.swing.JFormattedTextField t_utilidad;
    // End of variables declaration//GEN-END:variables

    public void cabecera2(PDF reporte, BaseFont bf, PdfPTable tabla)
   {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try
        {   
            reporte.contenido.setLineWidth(0.5f);
            reporte.contenido.setColorStroke(new GrayColor(0.2f));
            reporte.contenido.setColorFill(new GrayColor(0.9f));
            reporte.contenido.roundRectangle(160, 515, 210, 45, 5);
            reporte.contenido.roundRectangle(380, 515, 375, 45, 5);
            //reporte.contenido.roundRectangle(35, 490, 720, 20, 5);


            reporte.inicioTexto();
            reporte.contenido.setFontAndSize(bf, 14);
            reporte.contenido.setColorFill(BaseColor.BLACK);
            Configuracion con= (Configuracion)session.get(Configuracion.class, configuracion);
            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, con.getEmpresa(), 160, 580, 0);
            reporte.contenido.setFontAndSize(bf, 8);
            reporte.contenido.setColorFill(BaseColor.BLACK);
            //reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Refacciones de Operaciones", 160, 570, 0);
            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Estadistico de Valuación", 160, 570, 0);
            reporte.contenido.showTextAligned(PdfContentByte.ALIGN_RIGHT, "Fecha:"+new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date()), 760, 580, 0);

                    ord = (Orden)session.get(Orden.class, Integer.parseInt(orden)); 
                    Foto foto = (Foto)session.createCriteria(Foto.class).add(Restrictions.eq("orden.idOrden", Integer.parseInt(orden))).addOrder(Order.desc("fecha")).setMaxResults(1).uniqueResult();
                    if(foto!=null)
                    {
                        Ftp miFtp=new Ftp();
                        boolean respuesta=true;
                        respuesta=miFtp.conectar(rutaFtp, "compras", "04650077", 3310);
                        if(respuesta==true)
                        {
                            miFtp.cambiarDirectorio("ordenes/"+ord.getIdOrden()+"/miniatura");
                            String temporal=miFtp.descargaTemporal(foto.getDescripcion());
                            miFtp.desconectar();
                            reporte.agregaObjeto(reporte.crearImagen(temporal, 15, -50, 25, true));
                        }
                    }
                    //************************datos de la orden****************************
                    reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Orden:"+ord.getIdOrden(), 164, 550, 0);

                    if(ord.getFecha()!=null)
                        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Apertura:"+ord.getFecha(), 285, 550, 0);
                    else
                        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Apertura:", 285, 550, 0);

                    reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Compañia:"+ord.getCompania().getIdCompania()+" "+ord.getCompania().getNombre(), 164, 540, 0);

                    if(ord.getSiniestro()!=null)
                        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Siniestro:"+ord.getSiniestro(), 164, 530, 0);
                    else
                        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Siniestro:", 164, 530, 0);

                    if(ord.getFechaSiniestro()!=null)
                        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "F. Siniestro:"+ord.getFechaSiniestro(), 285, 530, 0);
                    else
                        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "F.Siniestro:", 285, 530, 0);

                    if(ord.getPoliza()!=null)
                        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Poliza:"+ord.getPoliza(), 164, 520, 0);
                    else
                        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Poliza:", 164, 520, 0);

                    if(ord.getInciso()!=null)
                        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Inciso:"+ord.getInciso(), 285, 520, 0);
                    else
                        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Inciso:", 285, 520, 0);
                    //**********************************************************

                    //************datos de la unidad
                    reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Unidad:"+ord.getTipo().getTipoNombre(), 385, 550, 0);
                    reporte.contenido.showTextAligned(PdfContentByte.ALIGN_RIGHT, "Modelo:"+ord.getModelo(), 664, 550, 0);

                    reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Marca:"+ord.getMarca().getMarcaNombre(), 385, 540, 0);
                    if(ord.getNoEconomico()!=null)
                        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_RIGHT, "Economico:"+ord.getNoEconomico(), 664, 540, 0);
                    else
                        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_RIGHT, "Economico:", 664, 540, 0);

                    if(ord.getNoMotor()!=null)
                        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "N° Motor:"+ord.getNoMotor(), 385, 530, 0);
                    else
                        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "N° Motor:", 385, 530, 0);

                    if(ord.getNoSerie()!=null)
                        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "N° Serie:"+ord.getNoSerie(), 385, 520, 0);
                    else
                        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "N° Serie:", 385, 520, 0);
                    
                    if(ord.getClientes()!=null)
                    {
                        String val=ord.getClientes().getNombre();
                        if(ord.getClientes().getNombre().length()>38)
                            val=ord.getClientes().getNombre().substring(0, 37);
                        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Cliente:"+val, 525, 520, 0);
                    }
                    else
                        reporte.contenido.showTextAligned(PdfContentByte.ALIGN_LEFT, "Cliente:", 525, 520, 0);
                    //*************************************************************

                reporte.finTexto();
                //agregamos renglones vacios para dejar un espacio
                reporte.agregaObjeto(new Paragraph(" "));
                reporte.agregaObjeto(new Paragraph(" "));
                reporte.agregaObjeto(new Paragraph(" "));
                reporte.agregaObjeto(new Paragraph(" "));

                Font font = new Font(Font.FontFamily.HELVETICA, 6, Font.BOLD);

                BaseColor cabecera=BaseColor.GRAY;
                BaseColor contenido=BaseColor.WHITE;
                int centro=Element.ALIGN_CENTER;
                int izquierda=Element.ALIGN_LEFT;
                int derecha=Element.ALIGN_RIGHT;

            tabla.addCell(reporte.celda("No", font, cabecera, centro, 1, 1, Rectangle.RECTANGLE));
            tabla.addCell(reporte.celda("#", font, cabecera, centro, 1, 1, Rectangle.RECTANGLE));
            tabla.addCell(reporte.celda("Descripción", font, cabecera, centro, 1, 1, Rectangle.RECTANGLE));
            tabla.addCell(reporte.celda("Cant Ped", font, cabecera, centro, 1, 1,Rectangle.RECTANGLE));
            tabla.addCell(reporte.celda("Cant Aut", font, cabecera, centro, 1,1, Rectangle.RECTANGLE));
            tabla.addCell(reporte.celda("Unidad", font, cabecera, centro, 1,1, Rectangle.RECTANGLE));
            tabla.addCell(reporte.celda("$ Presupuesto", font, cabecera, centro, 1,1,Rectangle.RECTANGLE));
            tabla.addCell(reporte.celda("$ Autorizado", font, cabecera, centro, 1,1, Rectangle.RECTANGLE));
            tabla.addCell(reporte.celda("$ Ajuste", font, cabecera, centro, 1, 1, Rectangle.RECTANGLE));
            tabla.addCell(reporte.celda("% Ajuste", font, cabecera, centro, 1, 1, Rectangle.RECTANGLE));
            tabla.addCell(reporte.celda("Tipo", font, cabecera, centro, 1, 1, Rectangle.RECTANGLE));
            tabla.addCell(reporte.celda("Aut", font, cabecera, centro, 1, 1, Rectangle.RECTANGLE));
        }catch(Exception e)
        {
            e.printStackTrace();
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
