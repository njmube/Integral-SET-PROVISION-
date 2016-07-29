/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * acceso.java
 *
 * Created on 19/01/2012, 02:01:25 PM
 */
package Compania;

import Integral.Herramientas;
import Integral.Render1;
import Hibernate.Util.HibernateUtil;
import Hibernate.entidades.Compania;
import Hibernate.entidades.Cuenta;
import java.util.List;
import javax.swing.InputMap;
import javax.swing.JOptionPane;
import Hibernate.entidades.Marca;
import Hibernate.entidades.Usuario;
import java.awt.Color;
import java.util.Vector;
import javax.swing.ImageIcon;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author ISC_SALVADOR
 */
public class CuentasBancarias extends javax.swing.JDialog {

    public static final Marca RET_CANCEL =null;
    InputMap map = new InputMap();
    DefaultTableModel model;
    String[] columnas = new String [] {"Id","nombre del Banco","convenio", "transferencia", "nombre de la cuenta"};
    Compania aseguradora;
    private Session session;
    //Cuenta[] doc;
    Cuenta aux;
    String sessionPrograma="";
    Herramientas h;
    Usuario usr;
    
    /** Creates new form acceso */
    public CuentasBancarias(java.awt.Frame parent, boolean modal, Compania c, String ses, Usuario u) {
        super(parent, modal);
        aseguradora=c;
        sessionPrograma=ses;
        usr=u;
        initComponents();
        t_datos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        buscaCuentas();
        formatoTabla();
    }
    
    DefaultTableModel ModeloTablaReporte(int renglones, String columnas[])
        {
            model = new DefaultTableModel(new Object [renglones][4], columnas)
            {
                Class[] types = new Class [] {
                    java.lang.String.class, 
                    java.lang.String.class, 
                    java.lang.String.class, 
                    java.lang.String.class, 
                    java.lang.String.class
                };
                boolean[] canEdit = new boolean [] {
                    false, true, true, true, true
                };

                public void setValueAt(Object value, int row, int col)
                 {                     
                        Vector vector = (Vector)this.dataVector.elementAt(row);
                        Object celda = ((Vector)this.dataVector.elementAt(row)).elementAt(col);
                        Object resp=null;
                        switch(col)
                        {
                            case 1:
                                    if(vector.get(col)==null)
                                    {
                                        value=value.toString().toUpperCase();
                                        vector.setElementAt(value, col);
                                        this.dataVector.setElementAt(vector, row);
                                        fireTableCellUpdated(row, col);
                                    }
                                    else
                                    {
                                        value=value.toString().toUpperCase();
                                        session = HibernateUtil.getSessionFactory().openSession();
                                        try
                                        {
                                            session.beginTransaction().begin();
                                            if(t_datos.getValueAt(row, 0).toString().compareTo("")==0)
                                            {
                                                String consulta="from Cuenta obj where obj.compania="+aseguradora.getIdCompania()+" and obj.banco='"+value.toString()+"'";
                                                consulta+=" and obj.convenio=0 and obj.transferencia='' and obj.nombre=''";
                                                resp=session.createQuery(consulta).uniqueResult();
                                                if(resp==null)
                                                {
                                                        aux= new Cuenta(aseguradora, value.toString(), 0, "", "");
                                                        aseguradora.addCuenta(aux);
                                                        session.save(aux);
                                                        session.getTransaction().commit();
                                                        vector.setElementAt(value, col);
                                                        this.dataVector.setElementAt(vector, row);
                                                        fireTableCellUpdated(row, col);
                                                        buscaCuentas();
                                                        JOptionPane.showMessageDialog(null, "La cuenta fue agregada");
                                                }
                                                else
                                                    JOptionPane.showMessageDialog(null, "Ya existe un registro idéntico");
                                            }
                                            else
                                            {
                                                String consulta="from Cuenta obj where obj.compania="+aseguradora.getIdCompania()+" and obj.banco='"+value.toString()+"'";
                                                consulta+=" and obj.convenio="+t_datos.getValueAt(row, 2).toString()+" and obj.transferencia='"+t_datos.getValueAt(row, 3).toString()+"' and obj.nombre='"+t_datos.getValueAt(row, 4).toString()+"'";
                                                resp=session.createQuery(consulta).uniqueResult();
                                                if(resp==null)
                                                {
                                                    consulta="from Cuenta obj where obj.idCuenta="+t_datos.getValueAt(row, 0);
                                                    resp=session.createQuery(consulta).uniqueResult();
                                                    if(resp!=null)
                                                    {
                                                        aux=(Cuenta) resp;
                                                        aux.setBanco(value.toString());
                                                        session.update(aux);
                                                        session.getTransaction().commit();
                                                        vector.setElementAt(value, col);
                                                        this.dataVector.setElementAt(vector, row);
                                                        fireTableCellUpdated(row, col);
                                                        if(session.isOpen()==true)
                                                            session.close();
                                                        buscaCuentas();
                                                        JOptionPane.showMessageDialog(null, "La cuenta fue actualizada");
                                                    }
                                                    else
                                                    {
                                                        buscaCuentas();
                                                        JOptionPane.showMessageDialog(null, "La cuenta ya no existe");
                                                    }
                                                }
                                                else
                                                {
                                                    buscaCuentas();
                                                    aux=(Cuenta) resp;
                                                    if(aux.getIdCuenta()!=t_datos.getValueAt(row, 0))
                                                        JOptionPane.showMessageDialog(null, "Ya existe un registor idéntico");
                                                } 
                                            }
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
                                        if(entero(value.toString())!=-1 && value.toString().compareTo("")!=0)
                                        {
                                            session = HibernateUtil.getSessionFactory().openSession();
                                            try
                                            {
                                                session.beginTransaction().begin();
                                                if(t_datos.getValueAt(row, 0).toString().compareTo("")==0)
                                                {
                                                    String consulta="from Cuenta obj where obj.compania="+aseguradora.getIdCompania()+" and obj.banco=''";
                                                    consulta+=" and obj.convenio="+value.toString()+" and obj.transferencia='' and obj.nombre=''";
                                                    resp=session.createQuery(consulta).uniqueResult();
                                                    if(resp==null)
                                                    {
                                                            int ent=entero(value.toString());
                                                            aux= new Cuenta(aseguradora, "", ent, "", "");
                                                            aseguradora.addCuenta(aux);
                                                            session.save(aux);
                                                            session.getTransaction().commit();
                                                            vector.setElementAt(value, col);
                                                            this.dataVector.setElementAt(vector, row);
                                                            fireTableCellUpdated(row, col);
                                                            buscaCuentas();
                                                            JOptionPane.showMessageDialog(null, "La cuenta fue agregada");
                                                    }
                                                    else
                                                        JOptionPane.showMessageDialog(null, "Ya existe un registor identico");
                                                }
                                                else
                                                {
                                                    String consulta="from Cuenta obj where obj.compania="+aseguradora.getIdCompania()+" and obj.banco='"+t_datos.getValueAt(row, 1)+"'";
                                                    consulta+=" and obj.convenio="+value.toString()+" and obj.transferencia='"+t_datos.getValueAt(row, 3).toString()+"' and obj.nombre='"+t_datos.getValueAt(row, 4).toString()+"'";
                                                    resp=session.createQuery(consulta).uniqueResult();
                                                    if(resp==null)
                                                    {
                                                        consulta="from Cuenta obj where obj.idCuenta="+t_datos.getValueAt(row, 0);
                                                        resp=session.createQuery(consulta).uniqueResult();
                                                        if(resp!=null)
                                                        {
                                                            aux=(Cuenta) resp;
                                                            aux.setConvenio(entero(value.toString()));
                                                            session.update(aux);
                                                            session.getTransaction().commit();
                                                            vector.setElementAt(value, col);
                                                            this.dataVector.setElementAt(vector, row);
                                                            fireTableCellUpdated(row, col);
                                                            if(session.isOpen()==true)
                                                                session.close();
                                                            buscaCuentas();
                                                            JOptionPane.showMessageDialog(null, "La cuenta fue actualizada");
                                                        }
                                                        else
                                                        {
                                                            buscaCuentas();
                                                            JOptionPane.showMessageDialog(null, "La cuenta ya no existe");
                                                        }
                                                    }
                                                    else
                                                    {
                                                        buscaCuentas();
                                                        aux=(Cuenta) resp;
                                                        if(aux.getIdCuenta()!=t_datos.getValueAt(row, 0))
                                                            JOptionPane.showMessageDialog(null, "Ya existe un registro idéntico");
                                                    } 
                                                }
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
                                            JOptionPane.showMessageDialog(null, "El campo solo admite numeros");
                                    }
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
                                                if(t_datos.getValueAt(row, 0).toString().compareTo("")==0)
                                                {
                                                    String consulta="from Cuenta obj where obj.compania="+aseguradora.getIdCompania()+" and obj.banco=''";
                                                    consulta+=" and obj.convenio=0 and obj.transferencia='"+value.toString()+"' and obj.nombre=''";
                                                    resp=session.createQuery(consulta).uniqueResult();
                                                    if(resp==null)
                                                    {
                                                            aux= new Cuenta(aseguradora, "", 0, value.toString(), "");
                                                            aseguradora.addCuenta(aux);
                                                            session.save(aux);
                                                            session.getTransaction().commit();
                                                            vector.setElementAt(value, col);
                                                            this.dataVector.setElementAt(vector, row);
                                                            fireTableCellUpdated(row, col);
                                                            buscaCuentas();
                                                            JOptionPane.showMessageDialog(null, "La Cuenta fue agregada");
                                                    }
                                                    else
                                                        JOptionPane.showMessageDialog(null, "Ya existe un registro idéntico");
                                                }
                                                else
                                                {
                                                    String consulta="from Cuenta obj where obj.compania="+aseguradora.getIdCompania()+" and obj.banco='"+t_datos.getValueAt(row, 1)+"'";
                                                    consulta+=" and obj.convenio="+t_datos.getValueAt(row, 2).toString()+" and obj.transferencia='"+value.toString()+"' and obj.nombre='"+t_datos.getValueAt(row, 4).toString()+"'";
                                                    resp=session.createQuery(consulta).uniqueResult();
                                                    if(resp==null)
                                                    {
                                                        consulta="from Cuenta obj where obj.idCuenta="+t_datos.getValueAt(row, 0);
                                                        resp=session.createQuery(consulta).uniqueResult();
                                                        if(resp!=null)
                                                        {
                                                            aux=(Cuenta) resp;
                                                            aux.setTransferencia(value.toString());
                                                            session.update(aux);
                                                            session.getTransaction().commit();
                                                            vector.setElementAt(value, col);
                                                            this.dataVector.setElementAt(vector, row);
                                                            fireTableCellUpdated(row, col);
                                                            if(session.isOpen()==true)
                                                                session.close();
                                                            buscaCuentas();
                                                            JOptionPane.showMessageDialog(null, "La cuenta fue actualizada");
                                                        }
                                                        else
                                                        {
                                                            buscaCuentas();
                                                            JOptionPane.showMessageDialog(null, "La cuenta ya no existe");
                                                        }
                                                    }
                                                    else
                                                    {
                                                        buscaCuentas();
                                                        aux=(Cuenta) resp;
                                                        if(aux.getIdCuenta()!=t_datos.getValueAt(row, 0))
                                                            JOptionPane.showMessageDialog(null, "Ya existe un registro idéntico");
                                                    } 
                                                }
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
                                            session = HibernateUtil.getSessionFactory().openSession();
                                            try
                                            {
                                                session.beginTransaction().begin();
                                                if(t_datos.getValueAt(row, 0).toString().compareTo("")==0)
                                                {
                                                    String consulta="from Cuenta obj where obj.compania="+aseguradora.getIdCompania()+" and obj.banco=''";
                                                    consulta+=" and obj.convenio=0 and obj.transferencia='' and obj.nombre='"+value.toString()+"'";
                                                    resp=session.createQuery(consulta).uniqueResult();
                                                    if(resp==null)
                                                    {
                                                        aux= new Cuenta(aseguradora, "", 0, "", value.toString());
                                                            aseguradora.addCuenta(aux);
                                                            session.save(aux);
                                                            session.getTransaction().commit();
                                                            vector.setElementAt(value, col);
                                                            this.dataVector.setElementAt(vector, row);
                                                            fireTableCellUpdated(row, col);
                                                            buscaCuentas();
                                                            JOptionPane.showMessageDialog(null, "La cuenta fue agregada");
                                                    }
                                                    else
                                                        JOptionPane.showMessageDialog(null, "Ya existe un registro idéntico");
                                                }
                                                else
                                                {
                                                    String consulta="from Cuenta obj where obj.compania="+aseguradora.getIdCompania()+" and obj.banco='"+t_datos.getValueAt(row, 1)+"'";
                                                    consulta+=" and obj.convenio="+t_datos.getValueAt(row, 2).toString()+" and obj.transferencia='"+t_datos.getValueAt(row, 3).toString()+"' and obj.nombre='"+value.toString()+"'";
                                                    resp=session.createQuery(consulta).uniqueResult();
                                                    if(resp==null)
                                                    {
                                                        consulta="from Cuenta obj where obj.idCuenta="+t_datos.getValueAt(row, 0);
                                                        resp=session.createQuery(consulta).uniqueResult();
                                                        if(resp!=null)
                                                        {
                                                            aux=(Cuenta) resp;
                                                            aux.setNombre(value.toString());
                                                            session.update(aux);
                                                            session.getTransaction().commit();
                                                            vector.setElementAt(value, col);
                                                            this.dataVector.setElementAt(vector, row);
                                                            fireTableCellUpdated(row, col);
                                                            if(session.isOpen()==true)
                                                                session.close();
                                                            buscaCuentas();
                                                            JOptionPane.showMessageDialog(null, "La cuenta fue actualizada");
                                                        }
                                                        else
                                                        {
                                                            buscaCuentas();
                                                            JOptionPane.showMessageDialog(null, "La cuenta ya no existe");
                                                        }
                                                    }
                                                    else
                                                    {
                                                        buscaCuentas();
                                                        aux=(Cuenta) resp;
                                                        if(aux.getIdCuenta()!=t_datos.getValueAt(row, 0))
                                                            JOptionPane.showMessageDialog(null, "Ya existe un registro idéntico");
                                                    } 
                                                }
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

    
    private void doClose(Marca o) {
        returnStatus = o;
        setVisible(false);
        dispose();
    }
    
    public Marca getReturnStatus() {
        return returnStatus;
    }
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        t_datos = new javax.swing.JTable();
        jButton2 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        l_compania = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        l_nombre = new javax.swing.JLabel();
        b_mas = new javax.swing.JButton();
        b_menos = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Cuentas bancarias");
        setResizable(false);

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(90, 66, 126), 1, true), "Resultados", javax.swing.border.TitledBorder.RIGHT, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Arial", 1, 11))); // NOI18N

        t_datos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "NOMBRE DEL BANCO", "CONVENIO", "TRANSFERENCIA", "NOMBRE DE CUENTA"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, true, true, true, true
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

        jButton2.setBackground(new java.awt.Color(2, 135, 242));
        jButton2.setForeground(new java.awt.Color(255, 255, 255));
        jButton2.setIcon(new ImageIcon("imagenes/tabla.png"));
        jButton2.setText("Actualiza Tabla");
        jButton2.setToolTipText("Consultar cambios en cuentas");
        jButton2.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 811, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButton2)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 246, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jLabel1.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel1.setText("No de compañía:");

        l_compania.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        l_compania.setText("no");

        jLabel2.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel2.setText("Nombre:");

        l_nombre.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        l_nombre.setText("nombre");

        b_mas.setBackground(new java.awt.Color(2, 135, 242));
        b_mas.setForeground(new java.awt.Color(255, 255, 255));
        b_mas.setIcon(new ImageIcon("imagenes/boton_mas.png"));
        b_mas.setToolTipText("Agrega una cuenta bancaria");
        b_mas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_masActionPerformed(evt);
            }
        });

        b_menos.setBackground(new java.awt.Color(2, 135, 242));
        b_menos.setForeground(new java.awt.Color(255, 255, 255));
        b_menos.setIcon(new ImageIcon("imagenes/boton_menos.png"));
        b_menos.setToolTipText("Eliminar la cuenta bancaria seleccionada");
        b_menos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_menosActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(l_compania, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(l_nombre, javax.swing.GroupLayout.PREFERRED_SIZE, 521, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 18, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(b_mas, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(b_menos, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(13, 13, 13))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(l_compania))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(l_nombre))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(75, 75, 75)
                        .addComponent(b_mas, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(b_menos, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        h=new Herramientas(usr, 0);
        h.session(sessionPrograma);
        buscaCuentas();
        tabla_tamaños();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void b_masActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_masActionPerformed
        h=new Herramientas(usr, 0);
        h.session(sessionPrograma);
        buscaCuentas();
        DefaultTableModel temp = (DefaultTableModel) t_datos.getModel();
        Object nuevo[]= {"","","","",""};
        temp.addRow(nuevo);
        formatoTabla();
    }//GEN-LAST:event_b_masActionPerformed

    private void b_menosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_menosActionPerformed
        h=new Herramientas(usr, 0);
        h.session(sessionPrograma);
        if(t_datos.getRowCount()>0)
        {
            if(t_datos.getSelectedRow()>=0)
            {
                session = HibernateUtil.getSessionFactory().openSession();
                try
                {
                    if(t_datos.getValueAt(t_datos.getSelectedRow(), 0).toString().compareTo("")!=0)
                    {
                        session.beginTransaction().begin();
                        aseguradora=(Compania)session.get(Compania.class, aseguradora.getIdCompania());
                        Cuenta doc = (Cuenta) session.get(Cuenta.class, Integer.parseInt(t_datos.getValueAt(t_datos.getSelectedRow(), 0).toString()));
                        if(doc!=null)
                        {
                            aseguradora.eliminaCuenta(doc);
                            session.update(aseguradora);
                            session.delete(doc);
                            DefaultTableModel temp = (DefaultTableModel) t_datos.getModel();
                            temp.removeRow(t_datos.getSelectedRow());
                            session.getTransaction().commit();
                            JOptionPane.showMessageDialog(null, "El documento fue eliminado");
                        }
                    }
                    else
                        if(t_datos.getValueAt(t_datos.getSelectedRow(), 0).toString().compareTo("")==0)
                        {
                            DefaultTableModel temp = (DefaultTableModel) t_datos.getModel();
                            temp.removeRow(t_datos.getSelectedRow());
                        }
                    buscaCuentas();
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                    session.getTransaction().rollback();
                    JOptionPane.showMessageDialog(null, "¡No se pudo eliminar el documento seleccionado!");
                }
                finally
                {
                    if(session.isOpen())
                    session.close();
                }
            }
            else
            JOptionPane.showMessageDialog(null, "¡No hay un documento seleccionado!");
            buscaCuentas();
            formatoTabla();
        }
    }//GEN-LAST:event_b_menosActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton b_mas;
    private javax.swing.JButton b_menos;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel l_compania;
    private javax.swing.JLabel l_nombre;
    private javax.swing.JTable t_datos;
    // End of variables declaration//GEN-END:variables

    private List<Object[]> executeHQLQuery(String hql) 
    {
        try {
            Session session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction().begin();
            Query q = session.createQuery(hql);
            List resultList = q.list();
            session.getTransaction().commit();
            session.disconnect();
            return resultList;
        } catch (HibernateException he) {
            he.printStackTrace();
            List lista= null;//new List(5);
            return lista;
        }
    }
    

    private void buscaCuentas()
    {
        if(aseguradora!=null)
        {
            session = HibernateUtil.getSessionFactory().openSession();
            try
            {
                session.beginTransaction().begin();
                aseguradora = (Compania)session.get(Compania.class, aseguradora.getIdCompania());

                l_compania.setText(""+aseguradora.getIdCompania());
                l_nombre.setText(aseguradora.getNombre());
                Cuenta[] cuentas = (Cuenta[]) aseguradora.getCuentas().toArray(new Cuenta[0]);
                for(int k=0;k<cuentas.length;k++) 
                {
                    for(int f=0;f<(cuentas.length-1)-k;f++) 
                    {
                       if (cuentas[f].getIdCuenta()>cuentas[f+1].getIdCuenta()) 
                       {
                           aux=cuentas[f];
                           cuentas[f]=cuentas[f+1];
                           cuentas[f+1]=aux;
                       }
                    }
                }
                if(cuentas.length>0)
                {
                    t_datos.setModel(ModeloTablaReporte(cuentas.length, columnas));
                    for(int i=0; i<cuentas.length; i++)
                    {
                        model.setValueAt(cuentas[i].getIdCuenta(), i, 0);
                        model.setValueAt(cuentas[i].getBanco(), i, 1);
                        model.setValueAt(cuentas[i].getConvenio(), i, 2);
                        model.setValueAt(cuentas[i].getTransferencia(), i, 3);
                        model.setValueAt(cuentas[i].getNombre(), i, 4);
                    }
                }
                else
                    t_datos.setModel(ModeloTablaReporte(0, columnas));    
                session.beginTransaction().rollback();
            }catch(Exception e){}
            finally
            {
                if(session.isOpen()==true)
                    session.close();
            }
        }
        else
            t_datos.setModel(ModeloTablaReporte(0, columnas));
        formatoTabla();
    }
    private Marca returnStatus = RET_CANCEL;
    
  public void tabla_tamaños()
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
                      column.setPreferredWidth(150);
                      break;
                  case 2:
                      column.setPreferredWidth(100);
                      break;
                  case 3:
                      column.setPreferredWidth(100);
                      break;
                  default:
                      column.setPreferredWidth(100);
                      break;
              }
        }
        JTableHeader header = t_datos.getTableHeader();
        header.setForeground(Color.white);
    }  
  public int entero(String op)
  {
      int entero=-1;
      try
      {
          entero=Integer.parseInt(op);
      }
      catch(Exception e){}
      return entero;
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
}
