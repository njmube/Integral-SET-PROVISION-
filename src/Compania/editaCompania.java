/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Compania;

import Hibernate.Util.HibernateUtil;
import Hibernate.entidades.Compania;
import Hibernate.entidades.Documentos;
import Hibernate.entidades.Usuario;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.util.Random;
import java.util.Vector;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import Integral.ExtensionFileFilter;
import Integral.Herramientas;
import Integral.Imagen;

/**
 *
 * @author I.S.C Salvador
 */
public class editaCompania extends javax.swing.JPanel {

    /**
     * Creates new form NuevaCompania
     * @param usuario: el usuario que abrio la clase
     * @param envio_periodo: El periodo para el cual se abrio la clase
     */
    
    DefaultTableModel model;
    String[] columnas = new String [] {""};
    File archivo=null;
    Usuario usr;
    int entro_foto=0;
    Compania registro = null;
    
    String existe_foto="";
    Documentos[] doc;
    Documentos aux;
    String sessionPrograma="";
    Herramientas h;
    
    public editaCompania(Usuario usuario, String envio_periodo, String ses) {
        sessionPrograma=ses;
        usr=usuario;
        initComponents();
        t_documentos.setModel(ModeloTablaReporte(0, columnas));
        p_foto.add(new Imagen("imagenes/foto1.png", 119, 113, 1, 1,120, 114));
        p_foto.repaint();
        b_banco.setEnabled(false);
        borra_cajas("", "","","","","","","","","","","","","","","","","");
        estado(false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false);
        t_compania.requestFocus();
    }
    
    DefaultTableModel ModeloTablaReporte(int renglones, String columnas[])
        {
            model = new DefaultTableModel(new Object [renglones][1], columnas)
            {
                Class[] types = new Class [] {
                    java.lang.String.class
                };
                boolean[] canEdit = new boolean [] {
                    true
                };

                public void setValueAt(Object value, int row, int col)
                 {
                        Vector vector = (Vector)this.dataVector.elementAt(row);
                        Object celda = ((Vector)this.dataVector.elementAt(row)).elementAt(col);
                        switch(col)
                        {
                            case 0:
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
                                        Session session = HibernateUtil.getSessionFactory().openSession();
                                        try
                                        {
                                            session.beginTransaction().begin();
                                            Object resp=session.createQuery("from Documentos obj where obj.idDocumento='"+value.toString()+"'").uniqueResult();
                                            if(resp==null)
                                            {
                                                if(value.toString().compareTo("")!=0)
                                                {
                                                    if(vector.get(col).toString().compareTo("")==0)
                                                    {
                                                        Documentos aux= new Documentos(value.toString(), registro);
                                                        registro.addDocumento(aux);
                                                        session.save(aux);
                                                        session.getTransaction().commit();
                                                        vector.setElementAt(value, col);
                                                        this.dataVector.setElementAt(vector, row);
                                                        fireTableCellUpdated(row, col);
                                                        tablaDocumentos(registro);
                                                        JOptionPane.showMessageDialog(null, "El documento fue agregado");
                                                    }
                                                    else
                                                    {
                                                        resp=session.createQuery("from Documentos obj where obj.idDocumento='"+vector.get(col).toString()+"'").uniqueResult();
                                                        if(resp!=null)
                                                        {
                                                            aux=(Documentos) resp;
                                                            aux.setIdDocumento(value.toString());
                                                            session.delete(aux);
                                                            session.save(aux);
                                                            session.getTransaction().commit();
                                                            vector.setElementAt(value, col);
                                                            this.dataVector.setElementAt(vector, row);
                                                            fireTableCellUpdated(row, col);
                                                            tablaDocumentos(registro);
                                                            JOptionPane.showMessageDialog(null, "El documento fue actualizado");
                                                        }
                                                        else
                                                        {
                                                            tablaDocumentos(registro);
                                                            JOptionPane.showMessageDialog(null, "El documento ya no existe");
                                                        }
                                                    }
                                                }
                                                else
                                                {
                                                    session.getTransaction().rollback();
                                                    JOptionPane.showMessageDialog(null, "No se puede almacenar datos vacios");
                                                }
                                            }
                                            else
                                            {
                                                
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

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        l_nombre = new javax.swing.JLabel();
        l_direccion = new javax.swing.JLabel();
        i_colonia = new javax.swing.JLabel();
        l_poblacion = new javax.swing.JLabel();
        l_estado = new javax.swing.JLabel();
        l_cp = new javax.swing.JLabel();
        l_telefono = new javax.swing.JLabel();
        l_fax = new javax.swing.JLabel();
        l_email = new javax.swing.JLabel();
        t_nombre = new javax.swing.JTextField();
        t_direccion = new javax.swing.JTextField();
        t_colonia = new javax.swing.JTextField();
        t_poblacion = new javax.swing.JTextField();
        c_estado = new javax.swing.JComboBox();
        t_telefono = new javax.swing.JTextField();
        t_fax = new javax.swing.JTextField();
        t_cp = new javax.swing.JTextField();
        t_email = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        t_compania = new javax.swing.JTextField();
        b_busca_orden = new javax.swing.JButton();
        l_social = new javax.swing.JLabel();
        t_social = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        l_representante1 = new javax.swing.JLabel();
        l_puesto1 = new javax.swing.JLabel();
        l_representante2 = new javax.swing.JLabel();
        l_puesto2 = new javax.swing.JLabel();
        t_representante1 = new javax.swing.JTextField();
        t_representante2 = new javax.swing.JTextField();
        t_puesto1 = new javax.swing.JTextField();
        t_puesto2 = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        l_importe_hora = new javax.swing.JLabel();
        l_importe_maximo = new javax.swing.JLabel();
        l_pago = new javax.swing.JLabel();
        l_plazo = new javax.swing.JLabel();
        l_dias = new javax.swing.JLabel();
        l_programa = new javax.swing.JLabel();
        l_ejecutivo = new javax.swing.JLabel();
        t_importe_hora = new javax.swing.JTextField();
        t_importe_maximo = new javax.swing.JTextField();
        t_plazo = new javax.swing.JTextField();
        c_pago = new javax.swing.JComboBox();
        t_ejecutivo = new javax.swing.JTextField();
        c_programa = new javax.swing.JComboBox();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        t_comentarios = new javax.swing.JTextArea();
        b_cancelar = new javax.swing.JButton();
        b_guardar = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        t_documentos = new javax.swing.JTable();
        b_mas = new javax.swing.JButton();
        b_menos = new javax.swing.JButton();
        p_foto = new javax.swing.JPanel();
        b_banco = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();

        setBackground(new java.awt.Color(255, 255, 255));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(90, 66, 126), 1, true), "Datos de la Compañía", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.TOP));

        l_nombre.setForeground(new java.awt.Color(51, 0, 255));
        l_nombre.setText("Nombre:");

        l_direccion.setText("Dirección:");

        i_colonia.setText("Colonia:");

        l_poblacion.setText("Población:");

        l_estado.setText("Estado:");

        l_cp.setText("Cp:");

        l_telefono.setText("Teléfono:");

        l_fax.setText("Fax:");

        l_email.setText("Email:");

        t_nombre.setBackground(new java.awt.Color(204, 255, 255));
        t_nombre.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        t_nombre.setNextFocusableComponent(t_direccion);
        t_nombre.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                t_nombreKeyTyped(evt);
            }
        });

        t_direccion.setBackground(new java.awt.Color(204, 255, 255));
        t_direccion.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        t_direccion.setNextFocusableComponent(t_colonia);
        t_direccion.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                t_direccionKeyTyped(evt);
            }
        });

        t_colonia.setBackground(new java.awt.Color(204, 255, 255));
        t_colonia.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        t_colonia.setNextFocusableComponent(t_poblacion);
        t_colonia.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                t_coloniaKeyTyped(evt);
            }
        });

        t_poblacion.setBackground(new java.awt.Color(204, 255, 255));
        t_poblacion.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        t_poblacion.setNextFocusableComponent(c_estado);
        t_poblacion.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                t_poblacionKeyTyped(evt);
            }
        });

        c_estado.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "AGUASCALIENTES", "BAJA CALIFORNIA", "BAJA CALIFORNIA SUR", "CAMPECHE", "CHIAPAS", "CHIHUAHUA", "COAHUILA", "COLIMA", "DISTRITO FEDERAL", "DURANGO", "ESTADO DE MEXICO", "GUANAJUATO", "GUERRERO", "HIDALGO", "JALISCO", "MICHOACAN", "MORELOS", "NAYARIT", "NUEVO LEON", "OAXACA", "PUEBLA", "QUERETARO", "QUINTANA ROO", "SAN LUIS POTOSI", "SINALOA", "SONORA", "TABASCO", "TAMAULIPAS", "TLAXCALA", "VERACRUZ", "YUCATAN", "ZACATECAS" }));
        c_estado.setNextFocusableComponent(t_telefono);

        t_telefono.setBackground(new java.awt.Color(204, 255, 255));
        t_telefono.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        t_telefono.setNextFocusableComponent(t_fax);
        t_telefono.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                t_telefonoFocusLost(evt);
            }
        });
        t_telefono.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                t_telefonoKeyTyped(evt);
            }
        });

        t_fax.setBackground(new java.awt.Color(204, 255, 255));
        t_fax.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        t_fax.setNextFocusableComponent(t_cp);
        t_fax.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                t_faxKeyTyped(evt);
            }
        });

        t_cp.setBackground(new java.awt.Color(204, 255, 255));
        t_cp.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        t_cp.setNextFocusableComponent(t_email);
        t_cp.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                t_cpKeyTyped(evt);
            }
        });

        t_email.setBackground(new java.awt.Color(204, 255, 255));
        t_email.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        t_email.setNextFocusableComponent(t_representante1);
        t_email.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                t_emailKeyTyped(evt);
            }
        });

        jLabel1.setText("No Compañia:");

        t_compania.setBackground(new java.awt.Color(204, 255, 255));
        t_compania.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        t_compania.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                t_companiaFocusLost(evt);
            }
        });
        t_compania.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                t_companiaActionPerformed(evt);
            }
        });
        t_compania.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                t_companiaKeyTyped(evt);
            }
        });

        b_busca_orden.setBackground(new java.awt.Color(90, 66, 126));
        b_busca_orden.setIcon(new ImageIcon("imagenes/buscar.png"));
        b_busca_orden.setToolTipText("Busca una aseguradora");
        b_busca_orden.setMaximumSize(new java.awt.Dimension(32, 8));
        b_busca_orden.setMinimumSize(new java.awt.Dimension(32, 8));
        b_busca_orden.setPreferredSize(new java.awt.Dimension(32, 8));
        b_busca_orden.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_busca_ordenActionPerformed(evt);
            }
        });

        l_social.setText("Razon Social:");

        t_social.setBackground(new java.awt.Color(204, 255, 255));
        t_social.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        t_social.setNextFocusableComponent(t_direccion);
        t_social.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                t_socialKeyTyped(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(l_estado, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(i_colonia, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 62, Short.MAX_VALUE))
                    .addComponent(jLabel1)
                    .addComponent(l_social, javax.swing.GroupLayout.DEFAULT_SIZE, 99, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(t_social)
                    .addComponent(t_colonia)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(c_estado, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(l_cp, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(t_cp, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(t_compania, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(b_busca_orden, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(19, 19, 19)
                        .addComponent(l_nombre, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(t_nombre, javax.swing.GroupLayout.PREFERRED_SIZE, 229, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(l_direccion, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(l_poblacion, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(l_telefono, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(l_email, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(t_direccion)
                    .addComponent(t_poblacion)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(t_telefono, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(l_fax)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(t_fax, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(t_email)))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(l_direccion, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(t_direccion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(l_poblacion, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(t_poblacion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(l_telefono, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(t_telefono, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(l_fax, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(t_fax, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(l_email, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(t_email, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel1)
                                .addComponent(t_compania, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(b_busca_orden, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(l_nombre, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(t_nombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(l_social, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(t_social, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(12, 12, 12)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(i_colonia, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(t_colonia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(l_estado, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(c_estado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(l_cp, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(t_cp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(0, 11, Short.MAX_VALUE))
        );

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(90, 66, 126), 1, true), "Contacto", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.TOP));

        l_representante1.setText("Representante 1:");

        l_puesto1.setText("Puesto");

        l_representante2.setText("Receptor:");

        l_puesto2.setText("Mail:");

        t_representante1.setBackground(new java.awt.Color(204, 255, 255));
        t_representante1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        t_representante1.setNextFocusableComponent(t_puesto1);
        t_representante1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                t_representante1KeyTyped(evt);
            }
        });

        t_representante2.setBackground(new java.awt.Color(204, 255, 255));
        t_representante2.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        t_representante2.setNextFocusableComponent(t_puesto2);
        t_representante2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                t_representante2KeyTyped(evt);
            }
        });

        t_puesto1.setBackground(new java.awt.Color(204, 255, 255));
        t_puesto1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        t_puesto1.setNextFocusableComponent(t_representante2);
        t_puesto1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                t_puesto1KeyTyped(evt);
            }
        });

        t_puesto2.setBackground(new java.awt.Color(204, 255, 255));
        t_puesto2.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        t_puesto2.setNextFocusableComponent(t_importe_hora);
        t_puesto2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                t_puesto2KeyTyped(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(l_representante2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(t_representante2))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(l_representante1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(t_representante1, javax.swing.GroupLayout.PREFERRED_SIZE, 408, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(l_puesto2)
                    .addComponent(l_puesto1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(t_puesto2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 369, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(t_puesto1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 369, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(l_puesto1, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(t_puesto1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(l_puesto2, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(t_representante2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(t_puesto2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(l_representante1, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(t_representante1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(l_representante2, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 9, Short.MAX_VALUE))
        );

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(90, 66, 126), 1, true), "Importes", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.TOP));

        l_importe_hora.setForeground(new java.awt.Color(51, 0, 255));
        l_importe_hora.setText("Importe  por Hora:");

        l_importe_maximo.setForeground(new java.awt.Color(51, 0, 255));
        l_importe_maximo.setText("Importe  maximo:");

        l_pago.setText("Forma de Pago:");

        l_plazo.setText("Plazo:");

        l_dias.setText("Días:");

        l_programa.setText("Programa para Reportar:");

        l_ejecutivo.setText("Grupo Ejecutivo:");

        t_importe_hora.setBackground(new java.awt.Color(204, 255, 255));
        t_importe_hora.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        t_importe_hora.setNextFocusableComponent(t_importe_maximo);
        t_importe_hora.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                t_importe_horaFocusLost(evt);
            }
        });
        t_importe_hora.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                t_importe_horaKeyTyped(evt);
            }
        });

        t_importe_maximo.setBackground(new java.awt.Color(204, 255, 255));
        t_importe_maximo.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        t_importe_maximo.setNextFocusableComponent(c_pago);
        t_importe_maximo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                t_importe_maximoFocusLost(evt);
            }
        });
        t_importe_maximo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                t_importe_maximoKeyTyped(evt);
            }
        });

        t_plazo.setBackground(new java.awt.Color(204, 255, 255));
        t_plazo.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        t_plazo.setNextFocusableComponent(c_programa);
        t_plazo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                t_plazoKeyTyped(evt);
            }
        });

        c_pago.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "CREDITO", "CONTADO" }));
        c_pago.setNextFocusableComponent(t_plazo);

        t_ejecutivo.setBackground(new java.awt.Color(204, 255, 255));
        t_ejecutivo.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        t_ejecutivo.setNextFocusableComponent(t_comentarios);
        t_ejecutivo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                t_ejecutivoKeyTyped(evt);
            }
        });

        c_programa.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "-------", "AUTA TEX", "MITCHELL" }));
        c_programa.setNextFocusableComponent(t_ejecutivo);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(l_importe_hora)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(t_importe_hora)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(l_importe_maximo)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(t_importe_maximo))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel3Layout.createSequentialGroup()
                                .addComponent(l_programa)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(c_programa, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel3Layout.createSequentialGroup()
                                .addComponent(l_pago)
                                .addGap(23, 23, 23)
                                .addComponent(c_pago, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                                .addComponent(l_plazo)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(t_plazo, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(l_dias))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                                .addComponent(l_ejecutivo)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(t_ejecutivo, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(l_importe_hora, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(t_importe_hora, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(l_importe_maximo, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(t_importe_maximo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(l_pago, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(c_pago, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(l_plazo, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(l_dias, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(t_plazo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(l_programa, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(c_programa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(l_ejecutivo, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(t_ejecutivo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(90, 66, 126), 1, true), "Comentarios", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.TOP));

        t_comentarios.setBackground(new java.awt.Color(204, 255, 255));
        t_comentarios.setColumns(20);
        t_comentarios.setRows(5);
        t_comentarios.setNextFocusableComponent(b_guardar);
        t_comentarios.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                t_comentariosKeyTyped(evt);
            }
        });
        jScrollPane2.setViewportView(t_comentarios);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 30, Short.MAX_VALUE))
        );

        b_cancelar.setBackground(new java.awt.Color(2, 135, 242));
        b_cancelar.setForeground(new java.awt.Color(255, 255, 255));
        b_cancelar.setIcon(new ImageIcon("imagenes/cancelar.png"));
        b_cancelar.setText("Cancelar");
        b_cancelar.setToolTipText("Cancela el registro actual");
        b_cancelar.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        b_cancelar.setNextFocusableComponent(t_nombre);
        b_cancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_cancelarActionPerformed(evt);
            }
        });

        b_guardar.setBackground(new java.awt.Color(2, 135, 242));
        b_guardar.setForeground(new java.awt.Color(255, 255, 255));
        b_guardar.setIcon(new ImageIcon("imagenes/guardar.png"));
        b_guardar.setText("Guardar");
        b_guardar.setToolTipText("Guarda el registro actual");
        b_guardar.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        b_guardar.setNextFocusableComponent(b_cancelar);
        b_guardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_guardarActionPerformed(evt);
            }
        });

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));
        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(90, 66, 126), 1, true), "Documentos Requeridos", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.TOP));

        t_documentos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                ""
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        t_documentos.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        t_documentos.getTableHeader().setResizingAllowed(false);
        t_documentos.getTableHeader().setReorderingAllowed(false);
        t_documentos.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                t_documentosKeyTyped(evt);
            }
        });
        jScrollPane1.setViewportView(t_documentos);

        b_mas.setBackground(new java.awt.Color(2, 135, 242));
        b_mas.setIcon(new ImageIcon("imagenes/boton_mas.png"));
        b_mas.setToolTipText("Agrega un documento");
        b_mas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_masActionPerformed(evt);
            }
        });

        b_menos.setBackground(new java.awt.Color(2, 135, 242));
        b_menos.setIcon(new ImageIcon("imagenes/boton_menos.png"));
        b_menos.setToolTipText("Elimina el documento seleccionado");
        b_menos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_menosActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(b_mas, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(b_menos, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(b_mas, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(b_menos, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap())
        );

        p_foto.setBackground(new java.awt.Color(2, 135, 242));
        p_foto.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        p_foto.setToolTipText("Agregar imagen de la unidad");
        p_foto.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        p_foto.setPreferredSize(new java.awt.Dimension(87, 94));
        p_foto.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                p_fotoMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout p_fotoLayout = new javax.swing.GroupLayout(p_foto);
        p_foto.setLayout(p_fotoLayout);
        p_fotoLayout.setHorizontalGroup(
            p_fotoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 120, Short.MAX_VALUE)
        );
        p_fotoLayout.setVerticalGroup(
            p_fotoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        b_banco.setBackground(new java.awt.Color(2, 135, 242));
        b_banco.setIcon(new ImageIcon("imagenes/banco.png"));
        b_banco.setToolTipText("Cuentas Bancarias");
        b_banco.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_bancoActionPerformed(evt);
            }
        });

        jLabel2.setForeground(new java.awt.Color(51, 0, 255));
        jLabel2.setText("Nota: los campos en color azul son obligatorios.");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addComponent(p_foto, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(b_banco, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 50, Short.MAX_VALUE)
                                .addComponent(b_cancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(b_guardar, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(b_banco, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(b_cancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(b_guardar, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(p_foto, javax.swing.GroupLayout.DEFAULT_SIZE, 129, Short.MAX_VALUE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void b_cancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_cancelarActionPerformed
       h=new Herramientas(usr, 0);
       h.session(sessionPrograma);
       int opt=JOptionPane.showConfirmDialog(this, "¡Los datos capturados se eliminaran!");
       if(opt==0)
       {
           entro_foto=0;
           existe_foto="";
           borra_cajas("", "","","","","","","","","","","","","","","","","");
           estado(false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false);
           t_compania.setText("");
           t_compania.requestFocus();
           b_banco.setEnabled(false);
       }
    }//GEN-LAST:event_b_cancelarActionPerformed

    private void b_guardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_guardarActionPerformed
        h=new Herramientas(usr, 0);
        h.session(sessionPrograma);
        if(t_nombre.getText().compareTo("")!=0)
        {
            Session session = HibernateUtil.getSessionFactory().openSession();
            try
            {
                session.beginTransaction().begin();
                Object resp=session.createQuery("from Compania obj where obj.nombre='"+t_nombre.getText()+"' and obj.idCompania!="+t_compania.getText()).uniqueResult();
                if(resp==null)
                {
                    registro  = (Compania)session.get(Compania.class, Integer.parseInt(t_compania.getText()));
                    aux=null;
                    registro.setNombre(t_nombre.getText());
                    registro.setSocial(t_social.getText());
                    if(t_direccion.getText().compareTo("")!=0)
                        registro.setDireccion(t_direccion.getText());
                    else
                        registro.setDireccion(null);
                    if(t_colonia.getText().compareTo("")!=0)
                        registro.setColonia(t_colonia.getText());
                    else
                        registro.setColonia(null);
                    if(t_poblacion.getText().compareTo("")!=0)
                        registro.setPoblacion(t_poblacion.getText());
                    else
                        registro.setPoblacion(null);
                    registro.setEstado(c_estado.getSelectedItem().toString());
                    if(t_telefono.getText().compareTo("")!=0)
                        registro.setTelefono(t_telefono.getText());
                    else
                        registro.setTelefono(null);
                    if(t_fax.getText().compareTo("")!=0)
                        registro.setFax(t_fax.getText());
                    else
                        registro.setFax(null);
                    if(t_cp.getText().compareTo("")!=0)
                        registro.setCp(t_cp.getText());
                    else
                        registro.setCp(null);
                    if(t_email.getText().compareTo("")!=0)
                        registro.setEmail(t_email.getText());
                    else
                        registro.setEmail(null);
                    if(t_representante1.getText().compareTo("")!=0)
                        registro.setRepresentante1(t_representante1.getText());
                    else
                        registro.setRepresentante1(null);
                    if(t_representante2.getText().compareTo("")!=0)
                        registro.setRepresentante2(t_representante2.getText());
                    else
                        registro.setRepresentante2(null);
                    if(t_puesto1.getText().compareTo("")!=0)
                        registro.setR1Puesto(t_puesto1.getText());
                    else
                        registro.setR1Puesto(null);
                    if(t_puesto2.getText().compareTo("")!=0)
                        registro.setR2Puesto(t_puesto2.getText());
                    else
                        registro.setR2Puesto(null);
                    if(t_importe_hora.getText().compareTo("")!=0)
                        registro.setImporteHora(Float.parseFloat(t_importe_hora.getText()));
                    else
                        registro.setImporteHora(0f);
                    if(t_importe_maximo.getText().compareTo("")!=0)
                        registro.setImporteMax(Float.parseFloat(t_importe_maximo.getText()));
                    else
                        registro.setImporteMax(0f);
                   registro.setTipoPago(c_pago.getSelectedItem().toString());
                   if(t_plazo.getText().compareTo("")!=0)
                       registro.setPlazo(Integer.parseInt(t_plazo.getText()));
                   else
                       registro.setPlazo(null);
                   registro.setProgramaReporte(c_programa.getSelectedItem().toString());
                   if(t_ejecutivo.getText().compareTo("")!=0)
                       registro.setGrupoEjecutivo(t_ejecutivo.getText());
                   else
                       registro.setGrupoEjecutivo(null);
                   if(t_comentarios.getText().compareTo("")!=0)
                       registro.setComentarios(t_comentarios.getText());
                   else
                       registro.setComentarios(null);
                   doc=null;
                   if(entro_foto==1)
                   {
                       String nomb="";
                        if(existe_foto.compareTo("")==0)
                        {
                            Random rng=new Random();
                            long  dig8 = rng.nextInt(90000000)+10000000;
                            nomb="compania/"+dig8+".jpg";
                            registro.setFoto(nomb);
                        }
                        else
                            nomb=registro.getFoto();
                        if(guardaFoto(nomb))
                        {
                            session.update(registro);
                            session.getTransaction().commit();
                            p_foto.removeAll();
                            p_foto.add(new Imagen("imagenes/foto1.png", 119, 113, 1, 1,120, 114));
                            p_foto.repaint();
                            borra_cajas("", "","","","","","","","","","","","","","","","","");
                            t_compania.setText("");
                            t_documentos.setModel(ModeloTablaReporte(0, columnas));
                            entro_foto=0;
                            b_banco.setEnabled(false);
                            estado(false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false);
                            JOptionPane.showMessageDialog(null, "¡Registro actualizado!");
                        }
                        else
                        {
                            session.getTransaction().rollback();
                            JOptionPane.showMessageDialog(null, "Error al guardar la imagen");
                        }
                   }
                   else
                   {
                       if(existe_foto.compareTo("")==0)
                            registro.setFoto(null);
                       else
                           registro.setFoto(existe_foto);
                       session.update(registro);
                       session.getTransaction().commit();
                       p_foto.removeAll();
                       p_foto.add(new Imagen("imagenes/foto1.png", 119, 113, 1, 1,120, 114));
                       p_foto.repaint();
                       borra_cajas("", "","","","","","","","","","","","","","","","","");
                       t_compania.setText("");
                       t_documentos.setModel(ModeloTablaReporte(0, columnas));
                       entro_foto=0;
                       b_banco.setEnabled(false);
                       estado(false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false);
                       JOptionPane.showMessageDialog(null, "¡Registro almacenado!");
                   }
                }
                else
                {   
                    session.getTransaction().rollback();
                    JOptionPane.showMessageDialog(null, "¡En nombre de la Compañía ya existe!");
                    t_nombre.requestFocus();
                }
            }catch (HibernateException he) 
            {
                he.printStackTrace();
                session.getTransaction().rollback();
            }
            finally 
            {
                session.close(); 
            }
        }
        else
        {
            JOptionPane.showMessageDialog(null, "¡Debe introducir el nombre de la compañía!");
            t_nombre.requestFocus();
        }
    }//GEN-LAST:event_b_guardarActionPerformed

    private void t_nombreKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_nombreKeyTyped
        char car = evt.getKeyChar();
        evt.setKeyChar(Character.toUpperCase(evt.getKeyChar()));
        if(t_nombre.getText().length()>=100) 
            evt.consume();
    }//GEN-LAST:event_t_nombreKeyTyped

    private void p_fotoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_p_fotoMouseClicked
        h=new Herramientas(usr, 0);
        h.session(sessionPrograma);
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction().begin();
        usr = (Usuario)session.get(Usuario.class, usr.getIdUsuario());
        if(usr.getEditaCompania()==true)
        {
            if(t_compania.getText().compareTo("")!=0)
            {
                JFileChooser selector=new JFileChooser();
                selector.setFileFilter(new ExtensionFileFilter("JPG and JPEG", new String[] { "JPG", "JPEG" }));
                int estado=-1;
                estado=selector.showOpenDialog(null);
                if(estado==0)
                {
                    archivo=selector.getSelectedFile();
                    try{
                        if(archivo.exists())
                        {
                            String ruta=archivo.getPath();
                            p_foto.removeAll();
                            p_foto.add(new Imagen(ruta, 119, 119, 1, 1,120, 120));
                            p_foto.repaint();
                            entro_foto=1;
                        }
                        else
                        {
                            javax.swing.JOptionPane.showMessageDialog(null, "No se pudo cargar la imagen");
                        }
                    }catch(Exception e){}
                }
            }
        }
        if(session!=null)
            if(session.isOpen())
                session.close();
    }//GEN-LAST:event_p_fotoMouseClicked

    private void t_coloniaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_coloniaKeyTyped
        char car = evt.getKeyChar();
        evt.setKeyChar(Character.toUpperCase(evt.getKeyChar()));
        if(t_colonia.getText().length()>=150) 
            evt.consume();
    }//GEN-LAST:event_t_coloniaKeyTyped

    private void t_direccionKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_direccionKeyTyped
        char car = evt.getKeyChar();
        evt.setKeyChar(Character.toUpperCase(evt.getKeyChar()));
        if(t_direccion.getText().length()>=200) 
            evt.consume();
    }//GEN-LAST:event_t_direccionKeyTyped

    private void t_poblacionKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_poblacionKeyTyped
        char car = evt.getKeyChar();
        evt.setKeyChar(Character.toUpperCase(evt.getKeyChar()));
        if(t_poblacion.getText().length()>=150) 
            evt.consume();
    }//GEN-LAST:event_t_poblacionKeyTyped

    private void t_telefonoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_telefonoKeyTyped
        char car = evt.getKeyChar();
        if(t_telefono.getText().length()>=13) 
            evt.consume();
        if((car<'0' || car>'9')) 
            evt.consume();
    }//GEN-LAST:event_t_telefonoKeyTyped

    private void t_faxKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_faxKeyTyped
        char car = evt.getKeyChar();
        if(t_fax.getText().length()>=13) 
            evt.consume();
        if((car<'0' || car>'9')) 
            evt.consume();
    }//GEN-LAST:event_t_faxKeyTyped

    private void t_cpKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_cpKeyTyped
        char car = evt.getKeyChar();
        if(t_cp.getText().length()>=5) 
            evt.consume();
        if((car<'0' || car>'9')) 
            evt.consume();
    }//GEN-LAST:event_t_cpKeyTyped

    private void t_emailKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_emailKeyTyped
        if(t_email.getText().length()>=100) 
            evt.consume();
    }//GEN-LAST:event_t_emailKeyTyped

    private void t_representante1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_representante1KeyTyped
        evt.setKeyChar(Character.toUpperCase(evt.getKeyChar()));
        if(t_representante1.getText().length()>=200) 
            evt.consume();
    }//GEN-LAST:event_t_representante1KeyTyped

    private void t_representante2KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_representante2KeyTyped
        evt.setKeyChar(Character.toUpperCase(evt.getKeyChar()));
        if(t_representante2.getText().length()>=200) 
            evt.consume();
    }//GEN-LAST:event_t_representante2KeyTyped

    private void t_puesto1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_puesto1KeyTyped
        evt.setKeyChar(Character.toUpperCase(evt.getKeyChar()));
        if(t_puesto1.getText().length()>=100) 
            evt.consume();
    }//GEN-LAST:event_t_puesto1KeyTyped

    private void t_puesto2KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_puesto2KeyTyped
        evt.setKeyChar(Character.toUpperCase(evt.getKeyChar()));
        if(t_puesto2.getText().length()>=100) 
            evt.consume();
    }//GEN-LAST:event_t_puesto2KeyTyped

    private void t_comentariosKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_comentariosKeyTyped
        evt.setKeyChar(Character.toUpperCase(evt.getKeyChar()));
        if(t_comentarios.getText().length()>=300) 
            evt.consume();
    }//GEN-LAST:event_t_comentariosKeyTyped

    private void t_importe_horaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_importe_horaKeyTyped
         char car = evt.getKeyChar();
        //if(t_importe_hora.getText().length()>=9) 
          //  evt.consume();
        if((car<'0' && car!='.') || (car>'9' && car!='.')) 
            evt.consume();
    }//GEN-LAST:event_t_importe_horaKeyTyped

    private void t_importe_maximoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_importe_maximoKeyTyped
         char car = evt.getKeyChar();
        //if(t_importe_maximo.getText().length()>=9) 
            //evt.consume();
        if((car<'0' && car!='.') || (car>'9' && car!='.')) 
            evt.consume();
    }//GEN-LAST:event_t_importe_maximoKeyTyped

    private void t_plazoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_plazoKeyTyped
         char car = evt.getKeyChar();
        //if(t_plazo.getText().length()>=4) 
           // evt.consume();
        if((car<'0' || car>'9')) 
            evt.consume();
    }//GEN-LAST:event_t_plazoKeyTyped

    private void t_importe_horaFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_t_importe_horaFocusLost
        if(t_importe_hora.getText().compareTo("")!=0)
        {
            try{
                Float numero = Float.parseFloat(t_importe_hora.getText()); // convirtiendo la cadena
                t_importe_hora.setText(""+numero);
            }catch(NumberFormatException e){ 
                javax.swing.JOptionPane.showMessageDialog(null, "El valor ingresado no es valido, verifique");
                t_importe_hora.requestFocus();
            }
        }
    }//GEN-LAST:event_t_importe_horaFocusLost

    private void t_importe_maximoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_t_importe_maximoFocusLost
          if(t_importe_maximo.getText().compareTo("")!=0)
        {
            try{
                Float numero = Float.parseFloat(t_importe_maximo.getText()); // convirtiendo la cadena
                t_importe_maximo.setText(""+numero);
            }catch(NumberFormatException e){ 
                javax.swing.JOptionPane.showMessageDialog(null, "El valor ingresado no es valido, verifique");
                t_importe_maximo.requestFocus();
            }
        }
    }//GEN-LAST:event_t_importe_maximoFocusLost

    private void t_ejecutivoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_ejecutivoKeyTyped
        evt.setKeyChar(Character.toUpperCase(evt.getKeyChar()));  
        if(t_ejecutivo.getText().length()>=1) 
            evt.consume();
    }//GEN-LAST:event_t_ejecutivoKeyTyped

    private void b_menosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_menosActionPerformed
        h=new Herramientas(usr, 0);
        h.session(sessionPrograma);
        if(t_documentos.getRowCount()>0)
        {
            if(t_documentos.getSelectedRow()>=0)
            {
                Session session = HibernateUtil.getSessionFactory().openSession();
                try
                {
                    session.beginTransaction().begin();
                    registro=(Compania)session.get(Compania.class, Integer.parseInt(t_compania.getText()));
          
                    doc = (Documentos[]) registro.getDocumentoses().toArray(new Documentos[0]);
                    for(int x=0; x<doc.length; x++)
                    {
                        if(t_documentos.getValueAt(t_documentos.getSelectedRow(), 0).toString().compareToIgnoreCase(doc[x].getIdDocumento())==0)
                        {
                            registro.eliminaDocumento(doc[x]);
                            session.update(registro);
                            session.delete(doc[x]);
                            DefaultTableModel temp = (DefaultTableModel) t_documentos.getModel();
                            temp.removeRow(t_documentos.getSelectedRow());
                            x=doc.length;
                            session.getTransaction().commit();
                            JOptionPane.showMessageDialog(null, "El documento fue eliminado");
                        }
                    }
                    if(t_documentos.getValueAt(t_documentos.getSelectedRow(), 0).toString().compareTo("")==0)
                    {
                        DefaultTableModel temp = (DefaultTableModel) t_documentos.getModel();
                        temp.removeRow(t_documentos.getSelectedRow());
                    }
                    tablaDocumentos(registro);
                }
                catch(Exception e)
                {
                    System.out.println(e);
                }
                finally
                {
                    if(session.isOpen())
                        session.close();
                }
            }
            else
                JOptionPane.showMessageDialog(null, "¡No hay un documento seleccionado!");
                cargaDatos(registro);
        }
    }//GEN-LAST:event_b_menosActionPerformed

    private void b_masActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_masActionPerformed
        h=new Herramientas(usr, 0);
        h.session(sessionPrograma);
        cargaDatos(registro);
        DefaultTableModel temp = (DefaultTableModel) t_documentos.getModel();
        Object nuevo[]= {""};
        temp.addRow(nuevo);
    }//GEN-LAST:event_b_masActionPerformed

    private void t_documentosKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_documentosKeyTyped
       
    }//GEN-LAST:event_t_documentosKeyTyped

    private void b_busca_ordenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_busca_ordenActionPerformed
        h=new Herramientas(usr, 0);
        h.session(sessionPrograma);
        buscaCompania obj = new buscaCompania(new javax.swing.JFrame(), true, this.sessionPrograma, this.usr);
        obj.t_busca.requestFocus();
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        obj.setLocation((d.width/2)-(obj.getWidth()/2), (d.height/2)-(obj.getHeight()/2));
        obj.setVisible(true);
        registro=obj.getReturnStatus();
        if(registro!=null)
            cargaDatos(registro);
    }//GEN-LAST:event_b_busca_ordenActionPerformed

    private void t_companiaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_t_companiaActionPerformed
        this.b_busca_orden.requestFocus();
    }//GEN-LAST:event_t_companiaActionPerformed

    private void t_companiaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_companiaKeyTyped
        char car = evt.getKeyChar();
        if((car<'0' || car>'9')) 
            evt.consume();
    }//GEN-LAST:event_t_companiaKeyTyped

    private void t_companiaFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_t_companiaFocusLost
        cargaCompania();
    }//GEN-LAST:event_t_companiaFocusLost

    private void b_bancoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_bancoActionPerformed
        h=new Herramientas(usr, 0);
        h.session(sessionPrograma);
        CuentasBancarias obj = new CuentasBancarias(new javax.swing.JFrame(), true, buscarCompania(Integer.parseInt(t_compania.getText())), this.sessionPrograma, this.usr);
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        obj.setLocation((d.width/2)-(obj.getWidth()/2), (d.height/2)-(obj.getHeight()/2));
        obj.setVisible(true);
    }//GEN-LAST:event_b_bancoActionPerformed

    private void t_telefonoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_t_telefonoFocusLost
        // TODO add your handling code here:
        t_telefono.setText(t_telefono.getText().replaceAll(" ", ""));
        t_telefono.setText(t_telefono.getText().replaceAll("-", ""));
        t_telefono.setText(t_telefono.getText().replaceAll("ext", ""));
        t_telefono.setText(t_telefono.getText().replaceAll("Ext", ""));
        t_telefono.setText(t_telefono.getText().replaceAll(".", ""));
        if(t_telefono.getText().length()>10)
            t_telefono.setText(t_telefono.getText().substring(0, 9));
    }//GEN-LAST:event_t_telefonoFocusLost

    private void t_socialKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t_socialKeyTyped
        // TODO add your handling code here:
        char car = evt.getKeyChar();
        evt.setKeyChar(Character.toUpperCase(evt.getKeyChar()));
        if(t_social.getText().length()>=200) 
            evt.consume();
    }//GEN-LAST:event_t_socialKeyTyped


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton b_banco;
    public javax.swing.JButton b_busca_orden;
    private javax.swing.JButton b_cancelar;
    private javax.swing.JButton b_guardar;
    private javax.swing.JButton b_mas;
    private javax.swing.JButton b_menos;
    private javax.swing.JComboBox c_estado;
    private javax.swing.JComboBox c_pago;
    private javax.swing.JComboBox c_programa;
    private javax.swing.JLabel i_colonia;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel l_cp;
    private javax.swing.JLabel l_dias;
    private javax.swing.JLabel l_direccion;
    private javax.swing.JLabel l_ejecutivo;
    private javax.swing.JLabel l_email;
    private javax.swing.JLabel l_estado;
    private javax.swing.JLabel l_fax;
    private javax.swing.JLabel l_importe_hora;
    private javax.swing.JLabel l_importe_maximo;
    private javax.swing.JLabel l_nombre;
    private javax.swing.JLabel l_pago;
    private javax.swing.JLabel l_plazo;
    private javax.swing.JLabel l_poblacion;
    private javax.swing.JLabel l_programa;
    private javax.swing.JLabel l_puesto1;
    private javax.swing.JLabel l_puesto2;
    private javax.swing.JLabel l_representante1;
    private javax.swing.JLabel l_representante2;
    private javax.swing.JLabel l_social;
    private javax.swing.JLabel l_telefono;
    private javax.swing.JPanel p_foto;
    private javax.swing.JTextField t_colonia;
    private javax.swing.JTextArea t_comentarios;
    public javax.swing.JTextField t_compania;
    private javax.swing.JTextField t_cp;
    private javax.swing.JTextField t_direccion;
    private javax.swing.JTable t_documentos;
    private javax.swing.JTextField t_ejecutivo;
    private javax.swing.JTextField t_email;
    private javax.swing.JTextField t_fax;
    private javax.swing.JTextField t_importe_hora;
    private javax.swing.JTextField t_importe_maximo;
    private javax.swing.JTextField t_nombre;
    private javax.swing.JTextField t_plazo;
    private javax.swing.JTextField t_poblacion;
    private javax.swing.JTextField t_puesto1;
    private javax.swing.JTextField t_puesto2;
    private javax.swing.JTextField t_representante1;
    private javax.swing.JTextField t_representante2;
    private javax.swing.JTextField t_social;
    private javax.swing.JTextField t_telefono;
    // End of variables declaration//GEN-END:variables

    public void borra_cajas(String nombre, String direccion, String colonia, String poblacion, String telefono, String fax, String cp, String email, String representante1, String representante2, String puesto1, String puesto2, String hora, String maximo, String plazo, String ejecutivo, String nuevo, String comentario)
    {
        this.t_nombre.setText(nombre);
        this.t_social.setText(nombre);
        this.t_direccion.setText(direccion);
        this.t_colonia.setText(colonia);
        this.t_poblacion.setText(poblacion);
        this.t_telefono.setText(telefono);
        this.t_fax.setText(fax);
        this.t_cp.setText(cp);
        this.t_email.setText(email);
        this.t_representante1.setText(representante1);
        this.t_representante2.setText(representante2);
        this.t_puesto1.setText(puesto1);
        this.t_puesto2.setText(puesto2);
        this.t_importe_hora.setText(hora);
        this.t_importe_maximo.setText(maximo);
        this.t_plazo.setText(plazo);
        this.t_ejecutivo.setText(ejecutivo);
        this.t_documentos.removeAll();
        this.t_comentarios.setText(comentario);
    }
    
    public void estado(boolean nombre, boolean direccion, boolean colonia, boolean poblacion, boolean telefono, boolean fax, boolean cp, boolean email, boolean estado, boolean representante1, boolean representante2, boolean puesto1, boolean puesto2, boolean hora, boolean maximo, boolean plazo, boolean ejecutivo, boolean pago, boolean programa, boolean comentario, boolean documento, boolean mas, boolean menos, boolean cancelar, boolean guardar)
    {
        this.t_nombre.setEnabled(nombre);
        this.t_direccion.setEnabled(direccion);
        this.t_colonia.setEnabled(colonia);
        this.t_poblacion.setEnabled(poblacion);
        this.t_telefono.setEnabled(telefono);
        this.t_fax.setEnabled(fax);
        this.t_cp.setEnabled(cp);
        this.t_email.setEnabled(email);
        this.c_estado.setEnabled(estado);
        this.t_representante1.setEnabled(representante1);
        this.t_representante2.setEnabled(representante2);
        this.t_puesto1.setEnabled(puesto1);
        this.t_puesto2.setEnabled(puesto2);
        this.t_importe_hora.setEnabled(hora);
        this.t_importe_maximo.setEnabled(maximo);
        this.t_plazo.setEnabled(plazo);
        this.t_ejecutivo.setEnabled(ejecutivo);
        this.c_pago.setEnabled(pago);
        this.c_programa.setEnabled(programa);
        this.b_mas.setEnabled(mas);
        this.b_menos.setEnabled(menos);
        this.t_documentos.setEnabled(documento);
        this.t_comentarios.setEnabled(comentario);
        this.b_cancelar.setEnabled(cancelar);
        this.b_guardar.setEnabled(guardar);
        entro_foto=0;
    }
    
    private boolean guardaFoto(String no)
    {
        try 
        {
            File folder = new File("compania");
            folder.mkdirs();

            if(archivo.exists()==true)
            {
                System.out.println(no);
                File destino = new File(no);
                String ruta=archivo.getPath();
                javax.swing.JPanel p=new Imagen(ruta, 600, 253, 0, 0,600,253);
                BufferedImage dibujo =new BufferedImage(600, 253, BufferedImage.TYPE_INT_RGB);
                Graphics g = dibujo.getGraphics();
                p.paint(g);

                ImageIO.write((RenderedImage)dibujo, "jpg", destino); // Salvar la imagen en el fichero
                return true;
            }
            return false;
        } 
        catch (Exception ioe)
        {
            ioe.printStackTrace();
            System.out.println("Error al guardar archivo.");
            return false;
        }
    }
    
    public boolean cargaDatos(Compania com)
    {
        h=new Herramientas(usr, 0);
        h.session(sessionPrograma);
        t_documentos.setModel(ModeloTablaReporte(0, columnas));
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction().begin();
        registro = (Compania)session.get(Compania.class, com.getIdCompania());
        p_foto.removeAll();
        p_foto.add(new Imagen("imagenes/foto1.png", 119, 120, 1, 1,120, 120));
        p_foto.repaint();
        if (registro!=null)
        {   
            t_compania.setText(""+registro.getIdCompania());
            t_nombre.setText(registro.getNombre());
            t_social.setText(registro.getSocial());
            t_direccion.setText(registro.getDireccion());
            t_colonia.setText(registro.getColonia());
            t_poblacion.setText(registro.getPoblacion());
            c_estado.setSelectedItem(registro.getEstado());
            t_telefono.setText(registro.getTelefono());
            t_fax.setText(registro.getFax());
            t_cp.setText(registro.getCp());
            t_email.setText(registro.getEmail());
            t_representante1.setText(registro.getRepresentante1());
            t_representante2.setText(registro.getRepresentante2());
            t_puesto1.setText(registro.getR1Puesto());
            t_puesto2.setText(registro.getR2Puesto());
            if(registro.getImporteHora()!=null)
                t_importe_hora.setText(""+registro.getImporteHora());
            else
                t_importe_hora.setText("");
            if(registro.getImporteMax()!=null)
                t_importe_maximo.setText(""+registro.getImporteMax());
            else
                t_importe_maximo.setText("");
            c_pago.setSelectedItem(registro.getTipoPago());
            if(registro.getPlazo()!=null)
                t_plazo.setText(""+registro.getPlazo());
            else
                t_plazo.setText("");
            c_programa.setSelectedItem(registro.getProgramaReporte());
            t_ejecutivo.setText(registro.getGrupoEjecutivo());
            t_comentarios.setText(registro.getComentarios());            
            //**********************cargamos la imagen**********************
            if(registro.getFoto()!=null)
            {
                p_foto.removeAll();
                Imagen op;
                op=new Imagen(registro.getFoto(), 119, 120, 1, 1,120, 120);
                p_foto.add(op);
                p_foto.repaint();
                existe_foto=registro.getFoto();
            }
            else
                existe_foto="";
            //**************************************************************
            doc = (Documentos[]) registro.getDocumentoses().toArray(new Documentos[0]);
            for(int k=0;k<doc.length;k++) 
            {
                for(int f=0;f<(doc.length-1)-k;f++) 
                {
                    if (doc[f].getIdDocumento().compareToIgnoreCase(doc[f+1].getIdDocumento())>0) 
                    {
                        aux=doc[f];
                        doc[f]=doc[f+1];
                        doc[f+1]=aux;
                    }
                }
                t_documentos.setModel(ModeloTablaReporte(doc.length, columnas));
                for(int ren=0; ren<doc.length; ren ++)
                {
                    String op=doc[ren].getIdDocumento();
                    model.setValueAt(op, ren, 0);
                }
            }
            this.usr = (Usuario)session.get(Usuario.class, usr.getIdUsuario());
            if(usr.getEditaCompania()==true)
            {
                estado(true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true);
                b_banco.setEnabled(true);
            }
        }
        else
        {
            t_compania.setText("");
            borra_cajas("", "","","","","","","","","","","","","","","","","");
            estado(false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false);
            b_banco.setEnabled(false);
            t_compania.requestFocus();
        }
        session.beginTransaction().rollback();
        session.close();
        return true;
    }
    
    private void tablaDocumentos(Compania com)
    {
        t_documentos.setModel(ModeloTablaReporte(0, columnas));
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction().begin();
        registro = (Compania)session.get(Compania.class, com.getIdCompania());
        doc = (Documentos[]) registro.getDocumentoses().toArray(new Documentos[0]);
        for(int k=0;k<doc.length;k++) 
        {
            for(int f=0;f<(doc.length-1)-k;f++) 
            {
               if (doc[f].getIdDocumento().compareToIgnoreCase(doc[f+1].getIdDocumento())>0) 
               {
                   aux=doc[f];
                   doc[f]=doc[f+1];
                   doc[f+1]=aux;
               }
            }
        }
        t_documentos.setModel(ModeloTablaReporte(doc.length, columnas));
        for(int ren=0; ren<doc.length; ren ++)
        {
            String op=doc[ren].getIdDocumento();
            model.setValueAt(op, ren, 0);
        }
        session.beginTransaction().rollback();
        session.close();
    }
    public void cargaCompania()
    {
        if(t_compania.getText().compareTo("")!=0)
        {
            entro_foto=0;
            existe_foto="";
            borra_cajas("", "","","","","","","","","","","","","","","","","");
            registro=buscarCompania(Integer.parseInt(t_compania.getText()));
            if (registro!=null)
                cargaDatos(registro);
            else
            {
                t_compania.setText("");
                entro_foto=0;
                existe_foto="";
                borra_cajas("", "","","","","","","","","","","","","","","","","");
                t_documentos.setModel(ModeloTablaReporte(0, columnas));
                p_foto.removeAll();
                p_foto.add(new Imagen("imagenes/foto1.png", 119, 120, 1, 1,120, 120));
                p_foto.repaint();
                estado(false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false);
                t_compania.requestFocus();
            }
        }
         else
         {
             entro_foto=0;
             existe_foto="";
             borra_cajas("", "","","","","","","","","","","","","","","","","");
             estado(false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false);
         }
    }
    
    private Compania buscarCompania(int id)
    {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try 
        {
            session.beginTransaction().begin();
            Compania ord = (Compania)session.get(Compania.class, id); 
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
            if(session.isOpen())
                session.close();
        }
    }
}
